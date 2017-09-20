/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.FilterOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.security.GeneralSecurityException;
/*  10:    */ import java.util.BitSet;
/*  11:    */ import javax.crypto.Cipher;
/*  12:    */ import org.apache.poi.EncryptedDocumentException;
/*  13:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  14:    */ import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
/*  15:    */ import org.apache.poi.poifs.filesystem.POIFSWriterListener;
/*  16:    */ import org.apache.poi.util.IOUtils;
/*  17:    */ import org.apache.poi.util.Internal;
/*  18:    */ import org.apache.poi.util.LittleEndian;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ import org.apache.poi.util.TempFile;
/*  22:    */ 
/*  23:    */ @Internal
/*  24:    */ public abstract class ChunkedCipherOutputStream
/*  25:    */   extends FilterOutputStream
/*  26:    */ {
/*  27: 49 */   private static final POILogger LOG = POILogFactory.getLogger(ChunkedCipherOutputStream.class);
/*  28:    */   private static final int STREAMING = -1;
/*  29:    */   private final int chunkSize;
/*  30:    */   private final int chunkBits;
/*  31:    */   private final byte[] chunk;
/*  32:    */   private final BitSet plainByteFlags;
/*  33:    */   private final File fileOut;
/*  34:    */   private final DirectoryNode dir;
/*  35:    */   private long pos;
/*  36:    */   private long totalPos;
/*  37:    */   private long written;
/*  38:    */   private Cipher cipher;
/*  39: 67 */   private boolean isClosed = false;
/*  40:    */   
/*  41:    */   public ChunkedCipherOutputStream(DirectoryNode dir, int chunkSize)
/*  42:    */     throws IOException, GeneralSecurityException
/*  43:    */   {
/*  44: 70 */     super(null);
/*  45: 71 */     this.chunkSize = chunkSize;
/*  46: 72 */     int cs = chunkSize == -1 ? 4096 : chunkSize;
/*  47: 73 */     this.chunk = new byte[cs];
/*  48: 74 */     this.plainByteFlags = new BitSet(cs);
/*  49: 75 */     this.chunkBits = Integer.bitCount(cs - 1);
/*  50: 76 */     this.fileOut = TempFile.createTempFile("encrypted_package", "crypt");
/*  51: 77 */     this.fileOut.deleteOnExit();
/*  52: 78 */     this.out = new FileOutputStream(this.fileOut);
/*  53: 79 */     this.dir = dir;
/*  54: 80 */     this.cipher = initCipherForBlock(null, 0, false);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public ChunkedCipherOutputStream(OutputStream stream, int chunkSize)
/*  58:    */     throws IOException, GeneralSecurityException
/*  59:    */   {
/*  60: 84 */     super(stream);
/*  61: 85 */     this.chunkSize = chunkSize;
/*  62: 86 */     int cs = chunkSize == -1 ? 4096 : chunkSize;
/*  63: 87 */     this.chunk = new byte[cs];
/*  64: 88 */     this.plainByteFlags = new BitSet(cs);
/*  65: 89 */     this.chunkBits = Integer.bitCount(cs - 1);
/*  66: 90 */     this.fileOut = null;
/*  67: 91 */     this.dir = null;
/*  68: 92 */     this.cipher = initCipherForBlock(null, 0, false);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public final Cipher initCipherForBlock(int block, boolean lastChunk)
/*  72:    */     throws IOException, GeneralSecurityException
/*  73:    */   {
/*  74: 96 */     return initCipherForBlock(this.cipher, block, lastChunk);
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected abstract Cipher initCipherForBlock(Cipher paramCipher, int paramInt, boolean paramBoolean)
/*  78:    */     throws IOException, GeneralSecurityException;
/*  79:    */   
/*  80:    */   protected abstract void calculateChecksum(File paramFile, int paramInt)
/*  81:    */     throws GeneralSecurityException, IOException;
/*  82:    */   
/*  83:    */   protected abstract void createEncryptionInfoEntry(DirectoryNode paramDirectoryNode, File paramFile)
/*  84:    */     throws IOException, GeneralSecurityException;
/*  85:    */   
/*  86:    */   public void write(int b)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:110 */     write(new byte[] { (byte)b });
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void write(byte[] b)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:115 */     write(b, 0, b.length);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void write(byte[] b, int off, int len)
/*  99:    */     throws IOException
/* 100:    */   {
/* 101:120 */     write(b, off, len, false);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void writePlain(byte[] b, int off, int len)
/* 105:    */     throws IOException
/* 106:    */   {
/* 107:124 */     write(b, off, len, true);
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected void write(byte[] b, int off, int len, boolean writePlain)
/* 111:    */     throws IOException
/* 112:    */   {
/* 113:128 */     if (len == 0) {
/* 114:129 */       return;
/* 115:    */     }
/* 116:132 */     if ((len < 0) || (b.length < off + len)) {
/* 117:133 */       throw new IOException("not enough bytes in your input buffer");
/* 118:    */     }
/* 119:136 */     int chunkMask = getChunkMask();
/* 120:137 */     while (len > 0)
/* 121:    */     {
/* 122:138 */       int posInChunk = (int)(this.pos & chunkMask);
/* 123:139 */       int nextLen = Math.min(this.chunk.length - posInChunk, len);
/* 124:140 */       System.arraycopy(b, off, this.chunk, posInChunk, nextLen);
/* 125:141 */       if (writePlain) {
/* 126:142 */         this.plainByteFlags.set(posInChunk, posInChunk + nextLen);
/* 127:    */       }
/* 128:144 */       this.pos += nextLen;
/* 129:145 */       this.totalPos += nextLen;
/* 130:146 */       off += nextLen;
/* 131:147 */       len -= nextLen;
/* 132:148 */       if ((this.pos & chunkMask) == 0L) {
/* 133:149 */         writeChunk(len > 0);
/* 134:    */       }
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected int getChunkMask()
/* 139:    */   {
/* 140:155 */     return this.chunk.length - 1;
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected void writeChunk(boolean continued)
/* 144:    */     throws IOException
/* 145:    */   {
/* 146:159 */     if ((this.pos == 0L) || (this.totalPos == this.written)) {
/* 147:160 */       return;
/* 148:    */     }
/* 149:163 */     int posInChunk = (int)(this.pos & getChunkMask());
/* 150:    */     
/* 151:    */ 
/* 152:    */ 
/* 153:167 */     int index = (int)(this.pos >> this.chunkBits);
/* 154:    */     boolean lastChunk;
/* 155:    */     boolean lastChunk;
/* 156:169 */     if (posInChunk == 0)
/* 157:    */     {
/* 158:170 */       index--;
/* 159:171 */       posInChunk = this.chunk.length;
/* 160:172 */       lastChunk = false;
/* 161:    */     }
/* 162:    */     else
/* 163:    */     {
/* 164:175 */       lastChunk = true;
/* 165:    */     }
/* 166:    */     int ciLen;
/* 167:    */     try
/* 168:    */     {
/* 169:180 */       boolean doFinal = true;
/* 170:181 */       long oldPos = this.pos;
/* 171:    */       
/* 172:    */ 
/* 173:184 */       this.pos = 0L;
/* 174:185 */       if (this.chunkSize == -1)
/* 175:    */       {
/* 176:186 */         if (continued) {
/* 177:187 */           doFinal = false;
/* 178:    */         }
/* 179:    */       }
/* 180:    */       else
/* 181:    */       {
/* 182:190 */         this.cipher = initCipherForBlock(this.cipher, index, lastChunk);
/* 183:    */         
/* 184:192 */         this.pos = oldPos;
/* 185:    */       }
/* 186:194 */       ciLen = invokeCipher(posInChunk, doFinal);
/* 187:    */     }
/* 188:    */     catch (GeneralSecurityException e)
/* 189:    */     {
/* 190:196 */       throw new IOException("can't re-/initialize cipher", e);
/* 191:    */     }
/* 192:199 */     this.out.write(this.chunk, 0, ciLen);
/* 193:200 */     this.plainByteFlags.clear();
/* 194:201 */     this.written += ciLen;
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected int invokeCipher(int posInChunk, boolean doFinal)
/* 198:    */     throws GeneralSecurityException
/* 199:    */   {
/* 200:213 */     byte[] plain = this.plainByteFlags.isEmpty() ? null : (byte[])this.chunk.clone();
/* 201:    */     
/* 202:215 */     int ciLen = doFinal ? this.cipher.doFinal(this.chunk, 0, posInChunk, this.chunk) : this.cipher.update(this.chunk, 0, posInChunk, this.chunk);
/* 203:219 */     for (int i = this.plainByteFlags.nextSetBit(0); (i >= 0) && (i < posInChunk); i = this.plainByteFlags.nextSetBit(i + 1)) {
/* 204:220 */       this.chunk[i] = plain[i];
/* 205:    */     }
/* 206:223 */     return ciLen;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void close()
/* 210:    */     throws IOException
/* 211:    */   {
/* 212:228 */     if (this.isClosed)
/* 213:    */     {
/* 214:229 */       LOG.log(1, new Object[] { "ChunkedCipherOutputStream was already closed - ignoring" });
/* 215:230 */       return;
/* 216:    */     }
/* 217:233 */     this.isClosed = true;
/* 218:    */     try
/* 219:    */     {
/* 220:236 */       writeChunk(false);
/* 221:    */       
/* 222:238 */       super.close();
/* 223:240 */       if (this.fileOut != null)
/* 224:    */       {
/* 225:241 */         int oleStreamSize = (int)(this.fileOut.length() + 8L);
/* 226:242 */         calculateChecksum(this.fileOut, (int)this.pos);
/* 227:243 */         this.dir.createDocument("EncryptedPackage", oleStreamSize, new EncryptedPackageWriter(null));
/* 228:244 */         createEncryptionInfoEntry(this.dir, this.fileOut);
/* 229:    */       }
/* 230:    */     }
/* 231:    */     catch (GeneralSecurityException e)
/* 232:    */     {
/* 233:247 */       throw new IOException(e);
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   protected byte[] getChunk()
/* 238:    */   {
/* 239:252 */     return this.chunk;
/* 240:    */   }
/* 241:    */   
/* 242:    */   protected BitSet getPlainByteFlags()
/* 243:    */   {
/* 244:256 */     return this.plainByteFlags;
/* 245:    */   }
/* 246:    */   
/* 247:    */   protected long getPos()
/* 248:    */   {
/* 249:260 */     return this.pos;
/* 250:    */   }
/* 251:    */   
/* 252:    */   protected long getTotalPos()
/* 253:    */   {
/* 254:264 */     return this.totalPos;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setNextRecordSize(int recordSize, boolean isPlain) {}
/* 258:    */   
/* 259:    */   private class EncryptedPackageWriter
/* 260:    */     implements POIFSWriterListener
/* 261:    */   {
/* 262:    */     private EncryptedPackageWriter() {}
/* 263:    */     
/* 264:    */     public void processPOIFSWriterEvent(POIFSWriterEvent event)
/* 265:    */     {
/* 266:    */       try
/* 267:    */       {
/* 268:281 */         OutputStream os = event.getStream();
/* 269:    */         
/* 270:    */ 
/* 271:    */ 
/* 272:    */ 
/* 273:    */ 
/* 274:287 */         byte[] buf = new byte[8];
/* 275:288 */         LittleEndian.putLong(buf, 0, ChunkedCipherOutputStream.this.pos);
/* 276:289 */         os.write(buf);
/* 277:    */         
/* 278:291 */         FileInputStream fis = new FileInputStream(ChunkedCipherOutputStream.this.fileOut);
/* 279:    */         try
/* 280:    */         {
/* 281:293 */           IOUtils.copy(fis, os);
/* 282:    */         }
/* 283:    */         finally
/* 284:    */         {
/* 285:295 */           fis.close();
/* 286:    */         }
/* 287:298 */         os.close();
/* 288:300 */         if (!ChunkedCipherOutputStream.this.fileOut.delete()) {
/* 289:301 */           ChunkedCipherOutputStream.LOG.log(7, new Object[] { "Can't delete temporary encryption file: " + ChunkedCipherOutputStream.this.fileOut });
/* 290:    */         }
/* 291:    */       }
/* 292:    */       catch (IOException e)
/* 293:    */       {
/* 294:304 */         throw new EncryptedDocumentException(e);
/* 295:    */       }
/* 296:    */     }
/* 297:    */   }
/* 298:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.ChunkedCipherOutputStream
 * JD-Core Version:    0.7.0.1
 */