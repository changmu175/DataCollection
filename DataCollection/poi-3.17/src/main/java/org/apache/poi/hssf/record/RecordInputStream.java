/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;
/*   8:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.poi.util.LittleEndianInput;
/*  11:    */ import org.apache.poi.util.LittleEndianInputStream;
/*  12:    */ import org.apache.poi.util.RecordFormatException;
/*  13:    */ 
/*  14:    */ public final class RecordInputStream
/*  15:    */   implements LittleEndianInput
/*  16:    */ {
/*  17:    */   public static final short MAX_RECORD_DATA_SIZE = 8224;
/*  18:    */   private static final int INVALID_SID_VALUE = -1;
/*  19:    */   private static final int DATA_LEN_NEEDS_TO_BE_READ = -1;
/*  20: 47 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*  21:    */   private final BiffHeaderInput _bhi;
/*  22:    */   private final LittleEndianInput _dataInput;
/*  23:    */   private int _currentSid;
/*  24:    */   private int _currentDataLength;
/*  25:    */   private int _nextSid;
/*  26:    */   private int _currentDataOffset;
/*  27:    */   private int _markedDataOffset;
/*  28:    */   
/*  29:    */   public static final class LeftoverDataException
/*  30:    */     extends RuntimeException
/*  31:    */   {
/*  32:    */     public LeftoverDataException(int sid, int remainingByteCount)
/*  33:    */     {
/*  34: 56 */       super();
/*  35:    */     }
/*  36:    */     
/*  37:    */     private static String getRecordName(int sid)
/*  38:    */     {
/*  39: 62 */       Class<? extends Record> recordClass = RecordFactory.getRecordClass(sid);
/*  40: 63 */       if (recordClass == null) {
/*  41: 64 */         return null;
/*  42:    */       }
/*  43: 66 */       return recordClass.getSimpleName();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   private static final class SimpleHeaderInput
/*  48:    */     implements BiffHeaderInput
/*  49:    */   {
/*  50:    */     private final LittleEndianInput _lei;
/*  51:    */     
/*  52:    */     public SimpleHeaderInput(InputStream in)
/*  53:    */     {
/*  54:104 */       this._lei = RecordInputStream.getLEI(in);
/*  55:    */     }
/*  56:    */     
/*  57:    */     public int available()
/*  58:    */     {
/*  59:108 */       return this._lei.available();
/*  60:    */     }
/*  61:    */     
/*  62:    */     public int readDataSize()
/*  63:    */     {
/*  64:112 */       return this._lei.readUShort();
/*  65:    */     }
/*  66:    */     
/*  67:    */     public int readRecordSID()
/*  68:    */     {
/*  69:116 */       return this._lei.readUShort();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public RecordInputStream(InputStream in)
/*  74:    */     throws RecordFormatException
/*  75:    */   {
/*  76:121 */     this(in, null, 0);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public RecordInputStream(InputStream in, EncryptionInfo key, int initialOffset)
/*  80:    */     throws RecordFormatException
/*  81:    */   {
/*  82:125 */     if (key == null)
/*  83:    */     {
/*  84:126 */       this._dataInput = getLEI(in);
/*  85:127 */       this._bhi = new SimpleHeaderInput(in);
/*  86:    */     }
/*  87:    */     else
/*  88:    */     {
/*  89:129 */       Biff8DecryptingStream bds = new Biff8DecryptingStream(in, initialOffset, key);
/*  90:130 */       this._dataInput = bds;
/*  91:131 */       this._bhi = bds;
/*  92:    */     }
/*  93:133 */     this._nextSid = readNextSid();
/*  94:    */   }
/*  95:    */   
/*  96:    */   static LittleEndianInput getLEI(InputStream is)
/*  97:    */   {
/*  98:137 */     if ((is instanceof LittleEndianInput)) {
/*  99:139 */       return (LittleEndianInput)is;
/* 100:    */     }
/* 101:142 */     return new LittleEndianInputStream(is);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int available()
/* 105:    */   {
/* 106:151 */     return remaining();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int read(byte[] b, int off, int len)
/* 110:    */   {
/* 111:155 */     int limit = Math.min(len, remaining());
/* 112:156 */     if (limit == 0) {
/* 113:157 */       return 0;
/* 114:    */     }
/* 115:159 */     readFully(b, off, limit);
/* 116:160 */     return limit;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public short getSid()
/* 120:    */   {
/* 121:164 */     return (short)this._currentSid;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean hasNextRecord()
/* 125:    */     throws LeftoverDataException
/* 126:    */   {
/* 127:177 */     if ((this._currentDataLength != -1) && (this._currentDataLength != this._currentDataOffset)) {
/* 128:178 */       throw new LeftoverDataException(this._currentSid, remaining());
/* 129:    */     }
/* 130:180 */     if (this._currentDataLength != -1) {
/* 131:181 */       this._nextSid = readNextSid();
/* 132:    */     }
/* 133:183 */     return this._nextSid != -1;
/* 134:    */   }
/* 135:    */   
/* 136:    */   private int readNextSid()
/* 137:    */   {
/* 138:190 */     int nAvailable = this._bhi.available();
/* 139:191 */     if (nAvailable < 4)
/* 140:    */     {
/* 141:192 */       if (nAvailable > 0) {}
/* 142:197 */       return -1;
/* 143:    */     }
/* 144:199 */     int result = this._bhi.readRecordSID();
/* 145:200 */     if (result == -1) {
/* 146:201 */       throw new RecordFormatException("Found invalid sid (" + result + ")");
/* 147:    */     }
/* 148:203 */     this._currentDataLength = -1;
/* 149:204 */     return result;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void nextRecord()
/* 153:    */     throws RecordFormatException
/* 154:    */   {
/* 155:212 */     if (this._nextSid == -1) {
/* 156:213 */       throw new IllegalStateException("EOF - next record not available");
/* 157:    */     }
/* 158:215 */     if (this._currentDataLength != -1) {
/* 159:216 */       throw new IllegalStateException("Cannot call nextRecord() without checking hasNextRecord() first");
/* 160:    */     }
/* 161:218 */     this._currentSid = this._nextSid;
/* 162:219 */     this._currentDataOffset = 0;
/* 163:220 */     this._currentDataLength = this._bhi.readDataSize();
/* 164:221 */     if (this._currentDataLength > 8224) {
/* 165:222 */       throw new RecordFormatException("The content of an excel record cannot exceed 8224 bytes");
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   private void checkRecordPosition(int requiredByteCount)
/* 170:    */   {
/* 171:229 */     int nAvailable = remaining();
/* 172:230 */     if (nAvailable >= requiredByteCount) {
/* 173:232 */       return;
/* 174:    */     }
/* 175:234 */     if ((nAvailable == 0) && (isContinueNext()))
/* 176:    */     {
/* 177:235 */       nextRecord();
/* 178:236 */       return;
/* 179:    */     }
/* 180:238 */     throw new RecordFormatException("Not enough data (" + nAvailable + ") to read requested (" + requiredByteCount + ") bytes");
/* 181:    */   }
/* 182:    */   
/* 183:    */   public byte readByte()
/* 184:    */   {
/* 185:247 */     checkRecordPosition(1);
/* 186:248 */     this._currentDataOffset += 1;
/* 187:249 */     return this._dataInput.readByte();
/* 188:    */   }
/* 189:    */   
/* 190:    */   public short readShort()
/* 191:    */   {
/* 192:257 */     checkRecordPosition(2);
/* 193:258 */     this._currentDataOffset += 2;
/* 194:259 */     return this._dataInput.readShort();
/* 195:    */   }
/* 196:    */   
/* 197:    */   public int readInt()
/* 198:    */   {
/* 199:267 */     checkRecordPosition(4);
/* 200:268 */     this._currentDataOffset += 4;
/* 201:269 */     return this._dataInput.readInt();
/* 202:    */   }
/* 203:    */   
/* 204:    */   public long readLong()
/* 205:    */   {
/* 206:277 */     checkRecordPosition(8);
/* 207:278 */     this._currentDataOffset += 8;
/* 208:279 */     return this._dataInput.readLong();
/* 209:    */   }
/* 210:    */   
/* 211:    */   public int readUByte()
/* 212:    */   {
/* 213:287 */     return readByte() & 0xFF;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public int readUShort()
/* 217:    */   {
/* 218:295 */     checkRecordPosition(2);
/* 219:296 */     this._currentDataOffset += 2;
/* 220:297 */     return this._dataInput.readUShort();
/* 221:    */   }
/* 222:    */   
/* 223:    */   public double readDouble()
/* 224:    */   {
/* 225:302 */     long valueLongBits = readLong();
/* 226:303 */     double result = Double.longBitsToDouble(valueLongBits);
/* 227:304 */     if (Double.isNaN(result)) {}
/* 228:310 */     return result;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void readPlain(byte[] buf, int off, int len)
/* 232:    */   {
/* 233:314 */     readFully(buf, 0, buf.length, true);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void readFully(byte[] buf)
/* 237:    */   {
/* 238:319 */     readFully(buf, 0, buf.length, false);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void readFully(byte[] buf, int off, int len)
/* 242:    */   {
/* 243:324 */     readFully(buf, off, len, false);
/* 244:    */   }
/* 245:    */   
/* 246:    */   protected void readFully(byte[] buf, int off, int len, boolean isPlain)
/* 247:    */   {
/* 248:328 */     int origLen = len;
/* 249:329 */     if (buf == null) {
/* 250:330 */       throw new NullPointerException();
/* 251:    */     }
/* 252:331 */     if ((off < 0) || (len < 0) || (len > buf.length - off)) {
/* 253:332 */       throw new IndexOutOfBoundsException();
/* 254:    */     }
/* 255:335 */     while (len > 0)
/* 256:    */     {
/* 257:336 */       int nextChunk = Math.min(available(), len);
/* 258:337 */       if (nextChunk == 0)
/* 259:    */       {
/* 260:338 */         if (!hasNextRecord()) {
/* 261:339 */           throw new RecordFormatException("Can't read the remaining " + len + " bytes of the requested " + origLen + " bytes. No further record exists.");
/* 262:    */         }
/* 263:341 */         nextRecord();
/* 264:342 */         nextChunk = Math.min(available(), len);
/* 265:343 */         assert (nextChunk > 0);
/* 266:    */       }
/* 267:346 */       checkRecordPosition(nextChunk);
/* 268:347 */       if (isPlain) {
/* 269:348 */         this._dataInput.readPlain(buf, off, nextChunk);
/* 270:    */       } else {
/* 271:350 */         this._dataInput.readFully(buf, off, nextChunk);
/* 272:    */       }
/* 273:352 */       this._currentDataOffset += nextChunk;
/* 274:353 */       off += nextChunk;
/* 275:354 */       len -= nextChunk;
/* 276:    */     }
/* 277:    */   }
/* 278:    */   
/* 279:    */   public String readString()
/* 280:    */   {
/* 281:359 */     int requestedLength = readUShort();
/* 282:360 */     byte compressFlag = readByte();
/* 283:361 */     return readStringCommon(requestedLength, compressFlag == 0);
/* 284:    */   }
/* 285:    */   
/* 286:    */   public String readUnicodeLEString(int requestedLength)
/* 287:    */   {
/* 288:376 */     return readStringCommon(requestedLength, false);
/* 289:    */   }
/* 290:    */   
/* 291:    */   public String readCompressedUnicode(int requestedLength)
/* 292:    */   {
/* 293:380 */     return readStringCommon(requestedLength, true);
/* 294:    */   }
/* 295:    */   
/* 296:    */   private String readStringCommon(int requestedLength, boolean pIsCompressedEncoding)
/* 297:    */   {
/* 298:385 */     if ((requestedLength < 0) || (requestedLength > 1048576)) {
/* 299:386 */       throw new IllegalArgumentException("Bad requested string length (" + requestedLength + ")");
/* 300:    */     }
/* 301:388 */     char[] buf = new char[requestedLength];
/* 302:389 */     boolean isCompressedEncoding = pIsCompressedEncoding;
/* 303:390 */     int curLen = 0;
/* 304:    */     for (;;)
/* 305:    */     {
/* 306:392 */       int availableChars = isCompressedEncoding ? remaining() : remaining() / 2;
/* 307:393 */       if (requestedLength - curLen <= availableChars)
/* 308:    */       {
/* 309:395 */         while (curLen < requestedLength)
/* 310:    */         {
/* 311:    */           char ch;
/* 312:    */           char ch;
/* 313:397 */           if (isCompressedEncoding) {
/* 314:398 */             ch = (char)readUByte();
/* 315:    */           } else {
/* 316:400 */             ch = (char)readShort();
/* 317:    */           }
/* 318:402 */           buf[curLen] = ch;
/* 319:403 */           curLen++;
/* 320:    */         }
/* 321:405 */         return new String(buf);
/* 322:    */       }
/* 323:409 */       while (availableChars > 0)
/* 324:    */       {
/* 325:    */         char ch;
/* 326:    */         char ch;
/* 327:411 */         if (isCompressedEncoding) {
/* 328:412 */           ch = (char)readUByte();
/* 329:    */         } else {
/* 330:414 */           ch = (char)readShort();
/* 331:    */         }
/* 332:416 */         buf[curLen] = ch;
/* 333:417 */         curLen++;
/* 334:418 */         availableChars--;
/* 335:    */       }
/* 336:420 */       if (!isContinueNext()) {
/* 337:421 */         throw new RecordFormatException("Expected to find a ContinueRecord in order to read remaining " + (requestedLength - curLen) + " of " + requestedLength + " chars");
/* 338:    */       }
/* 339:424 */       if (remaining() != 0) {
/* 340:425 */         throw new RecordFormatException("Odd number of bytes(" + remaining() + ") left behind");
/* 341:    */       }
/* 342:427 */       nextRecord();
/* 343:    */       
/* 344:429 */       byte compressFlag = readByte();
/* 345:430 */       assert ((compressFlag == 0) || (compressFlag == 1));
/* 346:431 */       isCompressedEncoding = compressFlag == 0;
/* 347:    */     }
/* 348:    */   }
/* 349:    */   
/* 350:    */   public byte[] readRemainder()
/* 351:    */   {
/* 352:440 */     int size = remaining();
/* 353:441 */     if (size == 0) {
/* 354:442 */       return EMPTY_BYTE_ARRAY;
/* 355:    */     }
/* 356:444 */     byte[] result = new byte[size];
/* 357:445 */     readFully(result);
/* 358:446 */     return result;
/* 359:    */   }
/* 360:    */   
/* 361:    */   @Deprecated
/* 362:    */   public byte[] readAllContinuedRemainder()
/* 363:    */   {
/* 364:461 */     ByteArrayOutputStream out = new ByteArrayOutputStream(16448);
/* 365:    */     for (;;)
/* 366:    */     {
/* 367:464 */       byte[] b = readRemainder();
/* 368:465 */       out.write(b, 0, b.length);
/* 369:466 */       if (!isContinueNext()) {
/* 370:    */         break;
/* 371:    */       }
/* 372:469 */       nextRecord();
/* 373:    */     }
/* 374:471 */     return out.toByteArray();
/* 375:    */   }
/* 376:    */   
/* 377:    */   public int remaining()
/* 378:    */   {
/* 379:479 */     if (this._currentDataLength == -1) {
/* 380:481 */       return 0;
/* 381:    */     }
/* 382:483 */     return this._currentDataLength - this._currentDataOffset;
/* 383:    */   }
/* 384:    */   
/* 385:    */   private boolean isContinueNext()
/* 386:    */   {
/* 387:491 */     if ((this._currentDataLength != -1) && (this._currentDataOffset != this._currentDataLength)) {
/* 388:492 */       throw new IllegalStateException("Should never be called before end of current record");
/* 389:    */     }
/* 390:494 */     if (!hasNextRecord()) {
/* 391:495 */       return false;
/* 392:    */     }
/* 393:502 */     return this._nextSid == 60;
/* 394:    */   }
/* 395:    */   
/* 396:    */   public int getNextSid()
/* 397:    */   {
/* 398:509 */     return this._nextSid;
/* 399:    */   }
/* 400:    */   
/* 401:    */   @Internal
/* 402:    */   public void mark(int readlimit)
/* 403:    */   {
/* 404:521 */     ((InputStream)this._dataInput).mark(readlimit);
/* 405:522 */     this._markedDataOffset = this._currentDataOffset;
/* 406:    */   }
/* 407:    */   
/* 408:    */   @Internal
/* 409:    */   public void reset()
/* 410:    */     throws IOException
/* 411:    */   {
/* 412:535 */     ((InputStream)this._dataInput).reset();
/* 413:536 */     this._currentDataOffset = this._markedDataOffset;
/* 414:    */   }
/* 415:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.RecordInputStream

 * JD-Core Version:    0.7.0.1

 */