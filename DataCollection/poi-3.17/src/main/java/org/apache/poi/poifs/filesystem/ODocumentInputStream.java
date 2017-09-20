/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.poifs.storage.DataInputBlock;
/*   5:    */ import org.apache.poi.util.RecordFormatException;
/*   6:    */ 
/*   7:    */ public final class ODocumentInputStream
/*   8:    */   extends DocumentInputStream
/*   9:    */ {
/*  10:    */   private int _current_offset;
/*  11:    */   private int _marked_offset;
/*  12:    */   private final int _document_size;
/*  13:    */   private boolean _closed;
/*  14:    */   private final OPOIFSDocument _document;
/*  15:    */   private DataInputBlock _currentBlock;
/*  16:    */   
/*  17:    */   public ODocumentInputStream(DocumentEntry document)
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 57 */     if (!(document instanceof DocumentNode)) {
/*  21: 58 */       throw new IOException("Cannot open internal document storage");
/*  22:    */     }
/*  23: 60 */     DocumentNode documentNode = (DocumentNode)document;
/*  24: 61 */     if (documentNode.getDocument() == null) {
/*  25: 62 */       throw new IOException("Cannot open internal document storage");
/*  26:    */     }
/*  27: 65 */     this._current_offset = 0;
/*  28: 66 */     this._marked_offset = 0;
/*  29: 67 */     this._document_size = document.getSize();
/*  30: 68 */     if (this._document_size < 0) {
/*  31: 69 */       throw new RecordFormatException("document_size cannot be < 0");
/*  32:    */     }
/*  33: 71 */     this._closed = false;
/*  34: 72 */     this._document = documentNode.getDocument();
/*  35: 73 */     this._currentBlock = getDataInputBlock(0);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public ODocumentInputStream(OPOIFSDocument document)
/*  39:    */   {
/*  40: 82 */     this._current_offset = 0;
/*  41: 83 */     this._marked_offset = 0;
/*  42: 84 */     this._document_size = document.getSize();
/*  43: 85 */     this._closed = false;
/*  44: 86 */     this._document = document;
/*  45: 87 */     this._currentBlock = getDataInputBlock(0);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int available()
/*  49:    */   {
/*  50: 92 */     if (this._closed) {
/*  51: 93 */       throw new IllegalStateException("cannot perform requested operation on a closed stream");
/*  52:    */     }
/*  53: 95 */     return this._document_size - this._current_offset;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void close()
/*  57:    */   {
/*  58:100 */     this._closed = true;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void mark(int ignoredReadlimit)
/*  62:    */   {
/*  63:105 */     this._marked_offset = this._current_offset;
/*  64:    */   }
/*  65:    */   
/*  66:    */   private DataInputBlock getDataInputBlock(int offset)
/*  67:    */   {
/*  68:109 */     return this._document.getDataInputBlock(offset);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int read()
/*  72:    */     throws IOException
/*  73:    */   {
/*  74:114 */     dieIfClosed();
/*  75:115 */     if (atEOD()) {
/*  76:116 */       return -1;
/*  77:    */     }
/*  78:118 */     int result = this._currentBlock.readUByte();
/*  79:119 */     this._current_offset += 1;
/*  80:120 */     if (this._currentBlock.available() < 1) {
/*  81:121 */       this._currentBlock = getDataInputBlock(this._current_offset);
/*  82:    */     }
/*  83:123 */     return result;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int read(byte[] b, int off, int len)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:128 */     dieIfClosed();
/*  90:129 */     if (b == null) {
/*  91:130 */       throw new IllegalArgumentException("buffer must not be null");
/*  92:    */     }
/*  93:132 */     if ((off < 0) || (len < 0) || (b.length < off + len)) {
/*  94:133 */       throw new IndexOutOfBoundsException("can't read past buffer boundaries");
/*  95:    */     }
/*  96:135 */     if (len == 0) {
/*  97:136 */       return 0;
/*  98:    */     }
/*  99:138 */     if (atEOD()) {
/* 100:139 */       return -1;
/* 101:    */     }
/* 102:141 */     int limit = Math.min(available(), len);
/* 103:142 */     readFully(b, off, limit);
/* 104:143 */     return limit;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void reset()
/* 108:    */   {
/* 109:153 */     this._current_offset = this._marked_offset;
/* 110:154 */     this._currentBlock = getDataInputBlock(this._current_offset);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public long skip(long n)
/* 114:    */     throws IOException
/* 115:    */   {
/* 116:159 */     dieIfClosed();
/* 117:160 */     if (n < 0L) {
/* 118:161 */       return 0L;
/* 119:    */     }
/* 120:163 */     int new_offset = this._current_offset + (int)n;
/* 121:165 */     if (new_offset < this._current_offset) {
/* 122:168 */       new_offset = this._document_size;
/* 123:169 */     } else if (new_offset > this._document_size) {
/* 124:170 */       new_offset = this._document_size;
/* 125:    */     }
/* 126:172 */     long rval = new_offset - this._current_offset;
/* 127:    */     
/* 128:174 */     this._current_offset = new_offset;
/* 129:175 */     this._currentBlock = getDataInputBlock(this._current_offset);
/* 130:176 */     return rval;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private void dieIfClosed()
/* 134:    */     throws IOException
/* 135:    */   {
/* 136:180 */     if (this._closed) {
/* 137:181 */       throw new IOException("cannot perform requested operation on a closed stream");
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   private boolean atEOD()
/* 142:    */   {
/* 143:186 */     return this._current_offset == this._document_size;
/* 144:    */   }
/* 145:    */   
/* 146:    */   private void checkAvaliable(int requestedSize)
/* 147:    */   {
/* 148:190 */     if (this._closed) {
/* 149:191 */       throw new IllegalStateException("cannot perform requested operation on a closed stream");
/* 150:    */     }
/* 151:193 */     if (requestedSize > this._document_size - this._current_offset) {
/* 152:194 */       throw new RuntimeException("Buffer underrun - requested " + requestedSize + " bytes but " + (this._document_size - this._current_offset) + " was available");
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   public byte readByte()
/* 157:    */   {
/* 158:201 */     return (byte)readUByte();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public double readDouble()
/* 162:    */   {
/* 163:206 */     return Double.longBitsToDouble(readLong());
/* 164:    */   }
/* 165:    */   
/* 166:    */   public short readShort()
/* 167:    */   {
/* 168:211 */     return (short)readUShort();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void readFully(byte[] buf, int off, int len)
/* 172:    */   {
/* 173:216 */     checkAvaliable(len);
/* 174:217 */     int blockAvailable = this._currentBlock.available();
/* 175:218 */     if (blockAvailable > len)
/* 176:    */     {
/* 177:219 */       this._currentBlock.readFully(buf, off, len);
/* 178:220 */       this._current_offset += len;
/* 179:221 */       return;
/* 180:    */     }
/* 181:224 */     int remaining = len;
/* 182:225 */     int writePos = off;
/* 183:226 */     while (remaining > 0)
/* 184:    */     {
/* 185:227 */       boolean blockIsExpiring = remaining >= blockAvailable;
/* 186:    */       int reqSize;
/* 187:    */       int reqSize;
/* 188:229 */       if (blockIsExpiring) {
/* 189:230 */         reqSize = blockAvailable;
/* 190:    */       } else {
/* 191:232 */         reqSize = remaining;
/* 192:    */       }
/* 193:234 */       this._currentBlock.readFully(buf, writePos, reqSize);
/* 194:235 */       remaining -= reqSize;
/* 195:236 */       writePos += reqSize;
/* 196:237 */       this._current_offset += reqSize;
/* 197:238 */       if (blockIsExpiring)
/* 198:    */       {
/* 199:239 */         if (this._current_offset == this._document_size)
/* 200:    */         {
/* 201:240 */           if (remaining > 0) {
/* 202:241 */             throw new IllegalStateException("reached end of document stream unexpectedly");
/* 203:    */           }
/* 204:244 */           this._currentBlock = null;
/* 205:245 */           break;
/* 206:    */         }
/* 207:247 */         this._currentBlock = getDataInputBlock(this._current_offset);
/* 208:248 */         blockAvailable = this._currentBlock.available();
/* 209:    */       }
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public long readLong()
/* 214:    */   {
/* 215:255 */     checkAvaliable(8);
/* 216:256 */     int blockAvailable = this._currentBlock.available();
/* 217:    */     long result;
/* 218:    */     long result;
/* 219:258 */     if (blockAvailable > 8)
/* 220:    */     {
/* 221:259 */       result = this._currentBlock.readLongLE();
/* 222:    */     }
/* 223:    */     else
/* 224:    */     {
/* 225:261 */       DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
/* 226:    */       long result;
/* 227:262 */       if (blockAvailable == 8) {
/* 228:263 */         result = this._currentBlock.readLongLE();
/* 229:    */       } else {
/* 230:265 */         result = nextBlock.readLongLE(this._currentBlock, blockAvailable);
/* 231:    */       }
/* 232:267 */       this._currentBlock = nextBlock;
/* 233:    */     }
/* 234:269 */     this._current_offset += 8;
/* 235:270 */     return result;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public int readInt()
/* 239:    */   {
/* 240:275 */     checkAvaliable(4);
/* 241:276 */     int blockAvailable = this._currentBlock.available();
/* 242:    */     int result;
/* 243:    */     int result;
/* 244:278 */     if (blockAvailable > 4)
/* 245:    */     {
/* 246:279 */       result = this._currentBlock.readIntLE();
/* 247:    */     }
/* 248:    */     else
/* 249:    */     {
/* 250:281 */       DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
/* 251:    */       int result;
/* 252:282 */       if (blockAvailable == 4) {
/* 253:283 */         result = this._currentBlock.readIntLE();
/* 254:    */       } else {
/* 255:285 */         result = nextBlock.readIntLE(this._currentBlock, blockAvailable);
/* 256:    */       }
/* 257:287 */       this._currentBlock = nextBlock;
/* 258:    */     }
/* 259:289 */     this._current_offset += 4;
/* 260:290 */     return result;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public int readUShort()
/* 264:    */   {
/* 265:295 */     checkAvaliable(2);
/* 266:296 */     int blockAvailable = this._currentBlock.available();
/* 267:    */     int result;
/* 268:    */     int result;
/* 269:298 */     if (blockAvailable > 2)
/* 270:    */     {
/* 271:299 */       result = this._currentBlock.readUShortLE();
/* 272:    */     }
/* 273:    */     else
/* 274:    */     {
/* 275:301 */       DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
/* 276:    */       int result;
/* 277:302 */       if (blockAvailable == 2) {
/* 278:303 */         result = this._currentBlock.readUShortLE();
/* 279:    */       } else {
/* 280:305 */         result = nextBlock.readUShortLE(this._currentBlock);
/* 281:    */       }
/* 282:307 */       this._currentBlock = nextBlock;
/* 283:    */     }
/* 284:309 */     this._current_offset += 2;
/* 285:310 */     return result;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public int readUByte()
/* 289:    */   {
/* 290:315 */     checkAvaliable(1);
/* 291:316 */     int result = this._currentBlock.readUByte();
/* 292:317 */     this._current_offset += 1;
/* 293:318 */     if (this._currentBlock.available() < 1) {
/* 294:319 */       this._currentBlock = getDataInputBlock(this._current_offset);
/* 295:    */     }
/* 296:321 */     return result;
/* 297:    */   }
/* 298:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.ODocumentInputStream
 * JD-Core Version:    0.7.0.1
 */