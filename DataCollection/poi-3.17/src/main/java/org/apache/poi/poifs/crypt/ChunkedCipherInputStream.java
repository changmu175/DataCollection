/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.EOFException;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.security.GeneralSecurityException;
/*   7:    */ import javax.crypto.Cipher;
/*   8:    */ import org.apache.poi.EncryptedDocumentException;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.util.LittleEndianInputStream;
/*  11:    */ 
/*  12:    */ @Internal
/*  13:    */ public abstract class ChunkedCipherInputStream
/*  14:    */   extends LittleEndianInputStream
/*  15:    */ {
/*  16:    */   private final int chunkSize;
/*  17:    */   private final int chunkBits;
/*  18:    */   private final long size;
/*  19:    */   private final byte[] chunk;
/*  20:    */   private final byte[] plain;
/*  21:    */   private final Cipher cipher;
/*  22:    */   private int lastIndex;
/*  23:    */   private long pos;
/*  24: 44 */   private boolean chunkIsValid = false;
/*  25:    */   
/*  26:    */   public ChunkedCipherInputStream(InputStream stream, long size, int chunkSize)
/*  27:    */     throws GeneralSecurityException
/*  28:    */   {
/*  29: 48 */     this(stream, size, chunkSize, 0);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ChunkedCipherInputStream(InputStream stream, long size, int chunkSize, int initialPos)
/*  33:    */     throws GeneralSecurityException
/*  34:    */   {
/*  35: 53 */     super(stream);
/*  36: 54 */     this.size = size;
/*  37: 55 */     this.pos = initialPos;
/*  38: 56 */     this.chunkSize = chunkSize;
/*  39: 57 */     int cs = chunkSize == -1 ? 4096 : chunkSize;
/*  40: 58 */     this.chunk = new byte[cs];
/*  41: 59 */     this.plain = new byte[cs];
/*  42: 60 */     this.chunkBits = Integer.bitCount(this.chunk.length - 1);
/*  43: 61 */     this.lastIndex = ((int)(this.pos >> this.chunkBits));
/*  44: 62 */     this.cipher = initCipherForBlock(null, this.lastIndex);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public final Cipher initCipherForBlock(int block)
/*  48:    */     throws IOException, GeneralSecurityException
/*  49:    */   {
/*  50: 66 */     if (this.chunkSize != -1) {
/*  51: 67 */       throw new GeneralSecurityException("the cipher block can only be set for streaming encryption, e.g. CryptoAPI...");
/*  52:    */     }
/*  53: 70 */     this.chunkIsValid = false;
/*  54: 71 */     return initCipherForBlock(this.cipher, block);
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected abstract Cipher initCipherForBlock(Cipher paramCipher, int paramInt)
/*  58:    */     throws GeneralSecurityException;
/*  59:    */   
/*  60:    */   public int read()
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 79 */     byte[] b = { 0 };
/*  64:    */     
/*  65: 81 */     return read(b) == 1 ? -1 : b[0];
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int read(byte[] b, int off, int len)
/*  69:    */     throws IOException
/*  70:    */   {
/*  71: 89 */     return read(b, off, len, false);
/*  72:    */   }
/*  73:    */   
/*  74:    */   private int read(byte[] b, int off, int len, boolean readPlain)
/*  75:    */     throws IOException
/*  76:    */   {
/*  77: 93 */     int total = 0;
/*  78: 95 */     if (available() <= 0) {
/*  79: 96 */       return -1;
/*  80:    */     }
/*  81: 99 */     int chunkMask = getChunkMask();
/*  82:100 */     while (len > 0)
/*  83:    */     {
/*  84:101 */       if (!this.chunkIsValid) {
/*  85:    */         try
/*  86:    */         {
/*  87:103 */           nextChunk();
/*  88:104 */           this.chunkIsValid = true;
/*  89:    */         }
/*  90:    */         catch (GeneralSecurityException e)
/*  91:    */         {
/*  92:106 */           throw new EncryptedDocumentException(e.getMessage(), e);
/*  93:    */         }
/*  94:    */       }
/*  95:109 */       int count = (int)(this.chunk.length - (this.pos & chunkMask));
/*  96:110 */       int avail = available();
/*  97:111 */       if (avail == 0) {
/*  98:112 */         return total;
/*  99:    */       }
/* 100:114 */       count = Math.min(avail, Math.min(count, len));
/* 101:    */       
/* 102:116 */       System.arraycopy(readPlain ? this.plain : this.chunk, (int)(this.pos & chunkMask), b, off, count);
/* 103:    */       
/* 104:118 */       off += count;
/* 105:119 */       len -= count;
/* 106:120 */       this.pos += count;
/* 107:121 */       if ((this.pos & chunkMask) == 0L) {
/* 108:122 */         this.chunkIsValid = false;
/* 109:    */       }
/* 110:124 */       total += count;
/* 111:    */     }
/* 112:127 */     return total;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public long skip(long n)
/* 116:    */     throws IOException
/* 117:    */   {
/* 118:132 */     long start = this.pos;
/* 119:133 */     long skip = Math.min(remainingBytes(), n);
/* 120:135 */     if (((this.pos + skip ^ start) & (getChunkMask() ^ 0xFFFFFFFF)) != 0L) {
/* 121:136 */       this.chunkIsValid = false;
/* 122:    */     }
/* 123:138 */     this.pos += skip;
/* 124:139 */     return skip;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int available()
/* 128:    */   {
/* 129:144 */     return remainingBytes();
/* 130:    */   }
/* 131:    */   
/* 132:    */   private int remainingBytes()
/* 133:    */   {
/* 134:153 */     return (int)(this.size - this.pos);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public boolean markSupported()
/* 138:    */   {
/* 139:158 */     return false;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public synchronized void mark(int readlimit)
/* 143:    */   {
/* 144:163 */     throw new UnsupportedOperationException();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public synchronized void reset()
/* 148:    */     throws IOException
/* 149:    */   {
/* 150:168 */     throw new UnsupportedOperationException();
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected int getChunkMask()
/* 154:    */   {
/* 155:172 */     return this.chunk.length - 1;
/* 156:    */   }
/* 157:    */   
/* 158:    */   private void nextChunk()
/* 159:    */     throws GeneralSecurityException, IOException
/* 160:    */   {
/* 161:176 */     if (this.chunkSize != -1)
/* 162:    */     {
/* 163:177 */       int index = (int)(this.pos >> this.chunkBits);
/* 164:178 */       initCipherForBlock(this.cipher, index);
/* 165:180 */       if (this.lastIndex != index)
/* 166:    */       {
/* 167:181 */         long skipN = index - this.lastIndex << this.chunkBits;
/* 168:182 */         if (super.skip(skipN) < skipN) {
/* 169:183 */           throw new EOFException("buffer underrun");
/* 170:    */         }
/* 171:    */       }
/* 172:187 */       this.lastIndex = (index + 1);
/* 173:    */     }
/* 174:190 */     int todo = (int)Math.min(this.size, this.chunk.length);
/* 175:191 */     int readBytes = 0;int totalBytes = 0;
/* 176:    */     do
/* 177:    */     {
/* 178:193 */       readBytes = super.read(this.plain, totalBytes, todo - totalBytes);
/* 179:194 */       totalBytes += Math.max(0, readBytes);
/* 180:195 */     } while ((readBytes != -1) && (totalBytes < todo));
/* 181:197 */     if ((readBytes == -1) && (this.pos + totalBytes < this.size) && (this.size < 2147483647L)) {
/* 182:198 */       throw new EOFException("buffer underrun");
/* 183:    */     }
/* 184:201 */     System.arraycopy(this.plain, 0, this.chunk, 0, totalBytes);
/* 185:    */     
/* 186:203 */     invokeCipher(totalBytes, totalBytes == this.chunkSize);
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected int invokeCipher(int totalBytes, boolean doFinal)
/* 190:    */     throws GeneralSecurityException
/* 191:    */   {
/* 192:215 */     if (doFinal) {
/* 193:216 */       return this.cipher.doFinal(this.chunk, 0, totalBytes, this.chunk);
/* 194:    */     }
/* 195:218 */     return this.cipher.update(this.chunk, 0, totalBytes, this.chunk);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void readPlain(byte[] b, int off, int len)
/* 199:    */   {
/* 200:229 */     if (len <= 0) {
/* 201:230 */       return;
/* 202:    */     }
/* 203:    */     try
/* 204:    */     {
/* 205:234 */       int total = 0;
/* 206:    */       int readBytes;
/* 207:    */       do
/* 208:    */       {
/* 209:236 */         readBytes = read(b, off, len, true);
/* 210:237 */         total += Math.max(0, readBytes);
/* 211:238 */       } while ((readBytes > -1) && (total < len));
/* 212:240 */       if (total < len) {
/* 213:241 */         throw new EOFException("buffer underrun");
/* 214:    */       }
/* 215:    */     }
/* 216:    */     catch (IOException e)
/* 217:    */     {
/* 218:245 */       throw new RuntimeException(e);
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void setNextRecordSize(int recordSize) {}
/* 223:    */   
/* 224:    */   protected byte[] getChunk()
/* 225:    */   {
/* 226:262 */     return this.chunk;
/* 227:    */   }
/* 228:    */   
/* 229:    */   protected byte[] getPlain()
/* 230:    */   {
/* 231:269 */     return this.plain;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public long getPos()
/* 235:    */   {
/* 236:276 */     return this.pos;
/* 237:    */   }
/* 238:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.ChunkedCipherInputStream
 * JD-Core Version:    0.7.0.1
 */