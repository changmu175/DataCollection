/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.HexRead;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ import org.apache.poi.util.LittleEndianInput;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ import org.apache.poi.util.POILogFactory;
/*  10:    */ import org.apache.poi.util.POILogger;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ import org.apache.poi.util.StringUtil;
/*  13:    */ 
/*  14:    */ public final class HyperlinkRecord
/*  15:    */   extends StandardRecord
/*  16:    */   implements Cloneable
/*  17:    */ {
/*  18:    */   public static final short sid = 440;
/*  19: 38 */   private static POILogger logger = POILogFactory.getLogger(HyperlinkRecord.class);
/*  20:    */   static final int HLINK_URL = 1;
/*  21:    */   static final int HLINK_ABS = 2;
/*  22:    */   static final int HLINK_LABEL = 20;
/*  23:    */   static final int HLINK_PLACE = 8;
/*  24:    */   private static final int HLINK_TARGET_FRAME = 128;
/*  25:    */   private static final int HLINK_UNC_PATH = 256;
/*  26:    */   public HyperlinkRecord() {}
/*  27:    */   
/*  28:    */   static final class GUID
/*  29:    */   {
/*  30:    */     private static final int TEXT_FORMAT_LENGTH = 36;
/*  31:    */     public static final int ENCODED_SIZE = 16;
/*  32:    */     private final int _d1;
/*  33:    */     private final int _d2;
/*  34:    */     private final int _d3;
/*  35:    */     private final long _d4;
/*  36:    */     
/*  37:    */     public GUID(LittleEndianInput in)
/*  38:    */     {
/*  39: 61 */       this(in.readInt(), in.readUShort(), in.readUShort(), in.readLong());
/*  40:    */     }
/*  41:    */     
/*  42:    */     public GUID(int d1, int d2, int d3, long d4)
/*  43:    */     {
/*  44: 65 */       this._d1 = d1;
/*  45: 66 */       this._d2 = d2;
/*  46: 67 */       this._d3 = d3;
/*  47: 68 */       this._d4 = d4;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public void serialize(LittleEndianOutput out)
/*  51:    */     {
/*  52: 72 */       out.writeInt(this._d1);
/*  53: 73 */       out.writeShort(this._d2);
/*  54: 74 */       out.writeShort(this._d3);
/*  55: 75 */       out.writeLong(this._d4);
/*  56:    */     }
/*  57:    */     
/*  58:    */     public boolean equals(Object obj)
/*  59:    */     {
/*  60: 80 */       if (!(obj instanceof GUID)) {
/*  61: 81 */         return false;
/*  62:    */       }
/*  63: 83 */       GUID other = (GUID)obj;
/*  64: 84 */       return (this._d1 == other._d1) && (this._d2 == other._d2) && (this._d3 == other._d3) && (this._d4 == other._d4);
/*  65:    */     }
/*  66:    */     
/*  67:    */     public int hashCode()
/*  68:    */     {
/*  69: 90 */       if (!$assertionsDisabled) {
/*  70: 90 */         throw new AssertionError("hashCode not designed");
/*  71:    */       }
/*  72: 91 */       return 42;
/*  73:    */     }
/*  74:    */     
/*  75:    */     public int getD1()
/*  76:    */     {
/*  77: 95 */       return this._d1;
/*  78:    */     }
/*  79:    */     
/*  80:    */     public int getD2()
/*  81:    */     {
/*  82: 99 */       return this._d2;
/*  83:    */     }
/*  84:    */     
/*  85:    */     public int getD3()
/*  86:    */     {
/*  87:103 */       return this._d3;
/*  88:    */     }
/*  89:    */     
/*  90:    */     public long getD4()
/*  91:    */     {
/*  92:107 */       byte[] result = new byte[8];
/*  93:108 */       long l = this._d4;
/*  94:109 */       for (int i = result.length - 1; i >= 0; i--)
/*  95:    */       {
/*  96:110 */         result[i] = ((byte)(int)(l & 0xFF));
/*  97:111 */         l >>= 8;
/*  98:    */       }
/*  99:114 */       return LittleEndian.getLong(result, 0);
/* 100:    */     }
/* 101:    */     
/* 102:    */     public String formatAsString()
/* 103:    */     {
/* 104:119 */       StringBuilder sb = new StringBuilder(36);
/* 105:    */       
/* 106:121 */       int PREFIX_LEN = "0x".length();
/* 107:122 */       sb.append(HexDump.intToHex(this._d1).substring(PREFIX_LEN));
/* 108:123 */       sb.append("-");
/* 109:124 */       sb.append(HexDump.shortToHex(this._d2).substring(PREFIX_LEN));
/* 110:125 */       sb.append("-");
/* 111:126 */       sb.append(HexDump.shortToHex(this._d3).substring(PREFIX_LEN));
/* 112:127 */       sb.append("-");
/* 113:128 */       String d4Chars = HexDump.longToHex(getD4());
/* 114:129 */       sb.append(d4Chars.substring(PREFIX_LEN, PREFIX_LEN + 4));
/* 115:130 */       sb.append("-");
/* 116:131 */       sb.append(d4Chars.substring(PREFIX_LEN + 4));
/* 117:132 */       return sb.toString();
/* 118:    */     }
/* 119:    */     
/* 120:    */     public String toString()
/* 121:    */     {
/* 122:137 */       StringBuilder sb = new StringBuilder(64);
/* 123:138 */       sb.append(getClass().getName()).append(" [");
/* 124:139 */       sb.append(formatAsString());
/* 125:140 */       sb.append("]");
/* 126:141 */       return sb.toString();
/* 127:    */     }
/* 128:    */     
/* 129:    */     public static GUID parse(String rep)
/* 130:    */     {
/* 131:151 */       char[] cc = rep.toCharArray();
/* 132:152 */       if (cc.length != 36) {
/* 133:153 */         throw new RecordFormatException("supplied text is the wrong length for a GUID");
/* 134:    */       }
/* 135:155 */       int d0 = (parseShort(cc, 0) << 16) + (parseShort(cc, 4) << 0);
/* 136:156 */       int d1 = parseShort(cc, 9);
/* 137:157 */       int d2 = parseShort(cc, 14);
/* 138:158 */       for (int i = 23; i > 19; i--) {
/* 139:159 */         cc[i] = cc[(i - 1)];
/* 140:    */       }
/* 141:161 */       long d3 = parseLELong(cc, 20);
/* 142:    */       
/* 143:163 */       return new GUID(d0, d1, d2, d3);
/* 144:    */     }
/* 145:    */     
/* 146:    */     private static long parseLELong(char[] cc, int startIndex)
/* 147:    */     {
/* 148:167 */       long acc = 0L;
/* 149:168 */       for (int i = startIndex + 14; i >= startIndex; i -= 2)
/* 150:    */       {
/* 151:169 */         acc <<= 4;
/* 152:170 */         acc += parseHexChar(cc[(i + 0)]);
/* 153:171 */         acc <<= 4;
/* 154:172 */         acc += parseHexChar(cc[(i + 1)]);
/* 155:    */       }
/* 156:174 */       return acc;
/* 157:    */     }
/* 158:    */     
/* 159:    */     private static int parseShort(char[] cc, int startIndex)
/* 160:    */     {
/* 161:178 */       int acc = 0;
/* 162:179 */       for (int i = 0; i < 4; i++)
/* 163:    */       {
/* 164:180 */         acc <<= 4;
/* 165:181 */         acc += parseHexChar(cc[(startIndex + i)]);
/* 166:    */       }
/* 167:183 */       return acc;
/* 168:    */     }
/* 169:    */     
/* 170:    */     private static int parseHexChar(char c)
/* 171:    */     {
/* 172:187 */       if ((c >= '0') && (c <= '9')) {
/* 173:188 */         return c - '0';
/* 174:    */       }
/* 175:190 */       if ((c >= 'A') && (c <= 'F')) {
/* 176:191 */         return c - 'A' + 10;
/* 177:    */       }
/* 178:193 */       if ((c >= 'a') && (c <= 'f')) {
/* 179:194 */         return c - 'a' + 10;
/* 180:    */       }
/* 181:196 */       throw new RecordFormatException("Bad hex char '" + c + "'");
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:211 */   static final GUID STD_MONIKER = GUID.parse("79EAC9D0-BAF9-11CE-8C82-00AA004BA90B");
/* 186:212 */   static final GUID URL_MONIKER = GUID.parse("79EAC9E0-BAF9-11CE-8C82-00AA004BA90B");
/* 187:213 */   static final GUID FILE_MONIKER = GUID.parse("00000303-0000-0000-C000-000000000046");
/* 188:215 */   private static final byte[] URL_TAIL = HexRead.readFromString("79 58 81 F4  3B 1D 7F 48   AF 2C 82 5D  C4 85 27 63   00 00 00 00  A5 AB 00 00");
/* 189:217 */   private static final byte[] FILE_TAIL = HexRead.readFromString("FF FF AD DE  00 00 00 00   00 00 00 00  00 00 00 00   00 00 00 00  00 00 00 00");
/* 190:219 */   private static final int TAIL_SIZE = FILE_TAIL.length;
/* 191:    */   private CellRangeAddress _range;
/* 192:    */   private GUID _guid;
/* 193:    */   private int _fileOpts;
/* 194:    */   private int _linkOpts;
/* 195:    */   private String _label;
/* 196:    */   private String _targetFrame;
/* 197:    */   private GUID _moniker;
/* 198:    */   private String _shortFilename;
/* 199:    */   private String _address;
/* 200:    */   private String _textMark;
/* 201:    */   private byte[] _uninterpretedTail;
/* 202:    */   
/* 203:    */   public int getFirstColumn()
/* 204:    */   {
/* 205:262 */     return this._range.getFirstColumn();
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setFirstColumn(int firstCol)
/* 209:    */   {
/* 210:271 */     this._range.setFirstColumn(firstCol);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public int getLastColumn()
/* 214:    */   {
/* 215:278 */     return this._range.getLastColumn();
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setLastColumn(int lastCol)
/* 219:    */   {
/* 220:287 */     this._range.setLastColumn(lastCol);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public int getFirstRow()
/* 224:    */   {
/* 225:294 */     return this._range.getFirstRow();
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setFirstRow(int firstRow)
/* 229:    */   {
/* 230:303 */     this._range.setFirstRow(firstRow);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public int getLastRow()
/* 234:    */   {
/* 235:310 */     return this._range.getLastRow();
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void setLastRow(int lastRow)
/* 239:    */   {
/* 240:319 */     this._range.setLastRow(lastRow);
/* 241:    */   }
/* 242:    */   
/* 243:    */   GUID getGuid()
/* 244:    */   {
/* 245:326 */     return this._guid;
/* 246:    */   }
/* 247:    */   
/* 248:    */   GUID getMoniker()
/* 249:    */   {
/* 250:334 */     return this._moniker;
/* 251:    */   }
/* 252:    */   
/* 253:    */   private static String cleanString(String s)
/* 254:    */   {
/* 255:338 */     if (s == null) {
/* 256:339 */       return null;
/* 257:    */     }
/* 258:341 */     int idx = s.indexOf(0);
/* 259:342 */     if (idx < 0) {
/* 260:343 */       return s;
/* 261:    */     }
/* 262:345 */     return s.substring(0, idx);
/* 263:    */   }
/* 264:    */   
/* 265:    */   private static String appendNullTerm(String s)
/* 266:    */   {
/* 267:348 */     if (s == null) {
/* 268:349 */       return null;
/* 269:    */     }
/* 270:351 */     return s + '\000';
/* 271:    */   }
/* 272:    */   
/* 273:    */   public String getLabel()
/* 274:    */   {
/* 275:360 */     return cleanString(this._label);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void setLabel(String label)
/* 279:    */   {
/* 280:369 */     this._label = appendNullTerm(label);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public String getTargetFrame()
/* 284:    */   {
/* 285:372 */     return cleanString(this._targetFrame);
/* 286:    */   }
/* 287:    */   
/* 288:    */   public String getAddress()
/* 289:    */   {
/* 290:381 */     if (((this._linkOpts & 0x1) != 0) && (FILE_MONIKER.equals(this._moniker))) {
/* 291:382 */       return cleanString(this._address != null ? this._address : this._shortFilename);
/* 292:    */     }
/* 293:383 */     if ((this._linkOpts & 0x8) != 0) {
/* 294:384 */       return cleanString(this._textMark);
/* 295:    */     }
/* 296:386 */     return cleanString(this._address);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public void setAddress(String address)
/* 300:    */   {
/* 301:396 */     if (((this._linkOpts & 0x1) != 0) && (FILE_MONIKER.equals(this._moniker))) {
/* 302:397 */       this._shortFilename = appendNullTerm(address);
/* 303:398 */     } else if ((this._linkOpts & 0x8) != 0) {
/* 304:399 */       this._textMark = appendNullTerm(address);
/* 305:    */     } else {
/* 306:401 */       this._address = appendNullTerm(address);
/* 307:    */     }
/* 308:    */   }
/* 309:    */   
/* 310:    */   public String getShortFilename()
/* 311:    */   {
/* 312:406 */     return cleanString(this._shortFilename);
/* 313:    */   }
/* 314:    */   
/* 315:    */   public void setShortFilename(String shortFilename)
/* 316:    */   {
/* 317:410 */     this._shortFilename = appendNullTerm(shortFilename);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public String getTextMark()
/* 321:    */   {
/* 322:414 */     return cleanString(this._textMark);
/* 323:    */   }
/* 324:    */   
/* 325:    */   public void setTextMark(String textMark)
/* 326:    */   {
/* 327:417 */     this._textMark = appendNullTerm(textMark);
/* 328:    */   }
/* 329:    */   
/* 330:    */   int getLinkOptions()
/* 331:    */   {
/* 332:428 */     return this._linkOpts;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public int getLabelOptions()
/* 336:    */   {
/* 337:435 */     return 2;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public int getFileOptions()
/* 341:    */   {
/* 342:442 */     return this._fileOpts;
/* 343:    */   }
/* 344:    */   
/* 345:    */   public HyperlinkRecord(RecordInputStream in)
/* 346:    */   {
/* 347:447 */     this._range = new CellRangeAddress(in);
/* 348:    */     
/* 349:449 */     this._guid = new GUID(in);
/* 350:    */     
/* 351:    */ 
/* 352:    */ 
/* 353:    */ 
/* 354:    */ 
/* 355:455 */     int streamVersion = in.readInt();
/* 356:456 */     if (streamVersion != 2) {
/* 357:457 */       throw new RecordFormatException("Stream Version must be 0x2 but found " + streamVersion);
/* 358:    */     }
/* 359:459 */     this._linkOpts = in.readInt();
/* 360:461 */     if ((this._linkOpts & 0x14) != 0)
/* 361:    */     {
/* 362:462 */       int label_len = in.readInt();
/* 363:463 */       this._label = in.readUnicodeLEString(label_len);
/* 364:    */     }
/* 365:466 */     if ((this._linkOpts & 0x80) != 0)
/* 366:    */     {
/* 367:467 */       int len = in.readInt();
/* 368:468 */       this._targetFrame = in.readUnicodeLEString(len);
/* 369:    */     }
/* 370:471 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) != 0))
/* 371:    */     {
/* 372:472 */       this._moniker = null;
/* 373:473 */       int nChars = in.readInt();
/* 374:474 */       this._address = in.readUnicodeLEString(nChars);
/* 375:    */     }
/* 376:477 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) == 0))
/* 377:    */     {
/* 378:478 */       this._moniker = new GUID(in);
/* 379:480 */       if (URL_MONIKER.equals(this._moniker))
/* 380:    */       {
/* 381:481 */         int length = in.readInt();
/* 382:    */         
/* 383:    */ 
/* 384:    */ 
/* 385:    */ 
/* 386:    */ 
/* 387:    */ 
/* 388:488 */         int remaining = in.remaining();
/* 389:489 */         if (length == remaining)
/* 390:    */         {
/* 391:490 */           int nChars = length / 2;
/* 392:491 */           this._address = in.readUnicodeLEString(nChars);
/* 393:    */         }
/* 394:    */         else
/* 395:    */         {
/* 396:493 */           int nChars = (length - TAIL_SIZE) / 2;
/* 397:494 */           this._address = in.readUnicodeLEString(nChars);
/* 398:    */           
/* 399:    */ 
/* 400:    */ 
/* 401:    */ 
/* 402:    */ 
/* 403:    */ 
/* 404:    */ 
/* 405:    */ 
/* 406:503 */           this._uninterpretedTail = readTail(URL_TAIL, in);
/* 407:    */         }
/* 408:    */       }
/* 409:505 */       else if (FILE_MONIKER.equals(this._moniker))
/* 410:    */       {
/* 411:506 */         this._fileOpts = in.readShort();
/* 412:    */         
/* 413:508 */         int len = in.readInt();
/* 414:509 */         this._shortFilename = StringUtil.readCompressedUnicode(in, len);
/* 415:510 */         this._uninterpretedTail = readTail(FILE_TAIL, in);
/* 416:511 */         int size = in.readInt();
/* 417:512 */         if (size > 0)
/* 418:    */         {
/* 419:513 */           int charDataSize = in.readInt();
/* 420:    */           
/* 421:    */ 
/* 422:    */ 
/* 423:517 */           in.readUShort();
/* 424:    */           
/* 425:519 */           this._address = StringUtil.readUnicodeLE(in, charDataSize / 2);
/* 426:    */         }
/* 427:    */         else
/* 428:    */         {
/* 429:521 */           this._address = null;
/* 430:    */         }
/* 431:    */       }
/* 432:523 */       else if (STD_MONIKER.equals(this._moniker))
/* 433:    */       {
/* 434:524 */         this._fileOpts = in.readShort();
/* 435:    */         
/* 436:526 */         int len = in.readInt();
/* 437:    */         
/* 438:528 */         byte[] path_bytes = new byte[len];
/* 439:529 */         in.readFully(path_bytes);
/* 440:    */         
/* 441:531 */         this._address = new String(path_bytes, StringUtil.UTF8);
/* 442:    */       }
/* 443:    */     }
/* 444:535 */     if ((this._linkOpts & 0x8) != 0)
/* 445:    */     {
/* 446:537 */       int len = in.readInt();
/* 447:538 */       this._textMark = in.readUnicodeLEString(len);
/* 448:    */     }
/* 449:541 */     if (in.remaining() > 0) {
/* 450:542 */       logger.log(5, new Object[] { "Hyperlink data remains: " + in.remaining() + " : " + HexDump.toHex(in.readRemainder()) });
/* 451:    */     }
/* 452:    */   }
/* 453:    */   
/* 454:    */   public void serialize(LittleEndianOutput out)
/* 455:    */   {
/* 456:551 */     this._range.serialize(out);
/* 457:    */     
/* 458:553 */     this._guid.serialize(out);
/* 459:554 */     out.writeInt(2);
/* 460:555 */     out.writeInt(this._linkOpts);
/* 461:557 */     if ((this._linkOpts & 0x14) != 0)
/* 462:    */     {
/* 463:558 */       out.writeInt(this._label.length());
/* 464:559 */       StringUtil.putUnicodeLE(this._label, out);
/* 465:    */     }
/* 466:561 */     if ((this._linkOpts & 0x80) != 0)
/* 467:    */     {
/* 468:562 */       out.writeInt(this._targetFrame.length());
/* 469:563 */       StringUtil.putUnicodeLE(this._targetFrame, out);
/* 470:    */     }
/* 471:566 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) != 0))
/* 472:    */     {
/* 473:567 */       out.writeInt(this._address.length());
/* 474:568 */       StringUtil.putUnicodeLE(this._address, out);
/* 475:    */     }
/* 476:571 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) == 0))
/* 477:    */     {
/* 478:572 */       this._moniker.serialize(out);
/* 479:573 */       if (URL_MONIKER.equals(this._moniker))
/* 480:    */       {
/* 481:574 */         if (this._uninterpretedTail == null)
/* 482:    */         {
/* 483:575 */           out.writeInt(this._address.length() * 2);
/* 484:576 */           StringUtil.putUnicodeLE(this._address, out);
/* 485:    */         }
/* 486:    */         else
/* 487:    */         {
/* 488:578 */           out.writeInt(this._address.length() * 2 + TAIL_SIZE);
/* 489:579 */           StringUtil.putUnicodeLE(this._address, out);
/* 490:580 */           writeTail(this._uninterpretedTail, out);
/* 491:    */         }
/* 492:    */       }
/* 493:582 */       else if (FILE_MONIKER.equals(this._moniker))
/* 494:    */       {
/* 495:583 */         out.writeShort(this._fileOpts);
/* 496:584 */         out.writeInt(this._shortFilename.length());
/* 497:585 */         StringUtil.putCompressedUnicode(this._shortFilename, out);
/* 498:586 */         writeTail(this._uninterpretedTail, out);
/* 499:587 */         if (this._address == null)
/* 500:    */         {
/* 501:588 */           out.writeInt(0);
/* 502:    */         }
/* 503:    */         else
/* 504:    */         {
/* 505:590 */           int addrLen = this._address.length() * 2;
/* 506:591 */           out.writeInt(addrLen + 6);
/* 507:592 */           out.writeInt(addrLen);
/* 508:593 */           out.writeShort(3);
/* 509:594 */           StringUtil.putUnicodeLE(this._address, out);
/* 510:    */         }
/* 511:    */       }
/* 512:    */     }
/* 513:598 */     if ((this._linkOpts & 0x8) != 0)
/* 514:    */     {
/* 515:599 */       out.writeInt(this._textMark.length());
/* 516:600 */       StringUtil.putUnicodeLE(this._textMark, out);
/* 517:    */     }
/* 518:    */   }
/* 519:    */   
/* 520:    */   protected int getDataSize()
/* 521:    */   {
/* 522:606 */     int size = 0;
/* 523:607 */     size += 8;
/* 524:608 */     size += 16;
/* 525:609 */     size += 4;
/* 526:610 */     size += 4;
/* 527:611 */     if ((this._linkOpts & 0x14) != 0)
/* 528:    */     {
/* 529:612 */       size += 4;
/* 530:613 */       size += this._label.length() * 2;
/* 531:    */     }
/* 532:615 */     if ((this._linkOpts & 0x80) != 0)
/* 533:    */     {
/* 534:616 */       size += 4;
/* 535:617 */       size += this._targetFrame.length() * 2;
/* 536:    */     }
/* 537:619 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) != 0))
/* 538:    */     {
/* 539:620 */       size += 4;
/* 540:621 */       size += this._address.length() * 2;
/* 541:    */     }
/* 542:623 */     if (((this._linkOpts & 0x1) != 0) && ((this._linkOpts & 0x100) == 0))
/* 543:    */     {
/* 544:624 */       size += 16;
/* 545:625 */       if (URL_MONIKER.equals(this._moniker))
/* 546:    */       {
/* 547:626 */         size += 4;
/* 548:627 */         size += this._address.length() * 2;
/* 549:628 */         if (this._uninterpretedTail != null) {
/* 550:629 */           size += TAIL_SIZE;
/* 551:    */         }
/* 552:    */       }
/* 553:631 */       else if (FILE_MONIKER.equals(this._moniker))
/* 554:    */       {
/* 555:632 */         size += 2;
/* 556:633 */         size += 4;
/* 557:634 */         size += this._shortFilename.length();
/* 558:635 */         size += TAIL_SIZE;
/* 559:636 */         size += 4;
/* 560:637 */         if (this._address != null)
/* 561:    */         {
/* 562:638 */           size += 6;
/* 563:639 */           size += this._address.length() * 2;
/* 564:    */         }
/* 565:    */       }
/* 566:    */     }
/* 567:644 */     if ((this._linkOpts & 0x8) != 0)
/* 568:    */     {
/* 569:645 */       size += 4;
/* 570:646 */       size += this._textMark.length() * 2;
/* 571:    */     }
/* 572:648 */     return size;
/* 573:    */   }
/* 574:    */   
/* 575:    */   private static byte[] readTail(byte[] expectedTail, LittleEndianInput in)
/* 576:    */   {
/* 577:653 */     byte[] result = new byte[TAIL_SIZE];
/* 578:654 */     in.readFully(result);
/* 579:    */     
/* 580:    */ 
/* 581:    */ 
/* 582:    */ 
/* 583:    */ 
/* 584:    */ 
/* 585:    */ 
/* 586:    */ 
/* 587:663 */     return result;
/* 588:    */   }
/* 589:    */   
/* 590:    */   private static void writeTail(byte[] tail, LittleEndianOutput out)
/* 591:    */   {
/* 592:666 */     out.write(tail);
/* 593:    */   }
/* 594:    */   
/* 595:    */   public short getSid()
/* 596:    */   {
/* 597:671 */     return 440;
/* 598:    */   }
/* 599:    */   
/* 600:    */   public String toString()
/* 601:    */   {
/* 602:677 */     StringBuffer buffer = new StringBuffer();
/* 603:    */     
/* 604:679 */     buffer.append("[HYPERLINK RECORD]\n");
/* 605:680 */     buffer.append("    .range   = ").append(this._range.formatAsString()).append("\n");
/* 606:681 */     buffer.append("    .guid    = ").append(this._guid.formatAsString()).append("\n");
/* 607:682 */     buffer.append("    .linkOpts= ").append(HexDump.intToHex(this._linkOpts)).append("\n");
/* 608:683 */     buffer.append("    .label   = ").append(getLabel()).append("\n");
/* 609:684 */     if ((this._linkOpts & 0x80) != 0) {
/* 610:685 */       buffer.append("    .targetFrame= ").append(getTargetFrame()).append("\n");
/* 611:    */     }
/* 612:687 */     if (((this._linkOpts & 0x1) != 0) && (this._moniker != null)) {
/* 613:688 */       buffer.append("    .moniker   = ").append(this._moniker.formatAsString()).append("\n");
/* 614:    */     }
/* 615:690 */     if ((this._linkOpts & 0x8) != 0) {
/* 616:691 */       buffer.append("    .textMark= ").append(getTextMark()).append("\n");
/* 617:    */     }
/* 618:693 */     buffer.append("    .address   = ").append(getAddress()).append("\n");
/* 619:694 */     buffer.append("[/HYPERLINK RECORD]\n");
/* 620:695 */     return buffer.toString();
/* 621:    */   }
/* 622:    */   
/* 623:    */   public boolean isUrlLink()
/* 624:    */   {
/* 625:704 */     return ((this._linkOpts & 0x1) > 0) && ((this._linkOpts & 0x2) > 0);
/* 626:    */   }
/* 627:    */   
/* 628:    */   public boolean isFileLink()
/* 629:    */   {
/* 630:713 */     return ((this._linkOpts & 0x1) > 0) && ((this._linkOpts & 0x2) == 0);
/* 631:    */   }
/* 632:    */   
/* 633:    */   public boolean isDocumentLink()
/* 634:    */   {
/* 635:722 */     return (this._linkOpts & 0x8) > 0;
/* 636:    */   }
/* 637:    */   
/* 638:    */   public void newUrlLink()
/* 639:    */   {
/* 640:729 */     this._range = new CellRangeAddress(0, 0, 0, 0);
/* 641:730 */     this._guid = STD_MONIKER;
/* 642:731 */     this._linkOpts = 23;
/* 643:732 */     setLabel("");
/* 644:733 */     this._moniker = URL_MONIKER;
/* 645:734 */     setAddress("");
/* 646:735 */     this._uninterpretedTail = URL_TAIL;
/* 647:    */   }
/* 648:    */   
/* 649:    */   public void newFileLink()
/* 650:    */   {
/* 651:742 */     this._range = new CellRangeAddress(0, 0, 0, 0);
/* 652:743 */     this._guid = STD_MONIKER;
/* 653:744 */     this._linkOpts = 21;
/* 654:745 */     this._fileOpts = 0;
/* 655:746 */     setLabel("");
/* 656:747 */     this._moniker = FILE_MONIKER;
/* 657:748 */     setAddress(null);
/* 658:749 */     setShortFilename("");
/* 659:750 */     this._uninterpretedTail = FILE_TAIL;
/* 660:    */   }
/* 661:    */   
/* 662:    */   public void newDocumentLink()
/* 663:    */   {
/* 664:757 */     this._range = new CellRangeAddress(0, 0, 0, 0);
/* 665:758 */     this._guid = STD_MONIKER;
/* 666:759 */     this._linkOpts = 28;
/* 667:760 */     setLabel("");
/* 668:761 */     this._moniker = FILE_MONIKER;
/* 669:762 */     setAddress("");
/* 670:763 */     setTextMark("");
/* 671:    */   }
/* 672:    */   
/* 673:    */   public HyperlinkRecord clone()
/* 674:    */   {
/* 675:768 */     HyperlinkRecord rec = new HyperlinkRecord();
/* 676:769 */     rec._range = this._range.copy();
/* 677:770 */     rec._guid = this._guid;
/* 678:771 */     rec._linkOpts = this._linkOpts;
/* 679:772 */     rec._fileOpts = this._fileOpts;
/* 680:773 */     rec._label = this._label;
/* 681:774 */     rec._address = this._address;
/* 682:775 */     rec._moniker = this._moniker;
/* 683:776 */     rec._shortFilename = this._shortFilename;
/* 684:777 */     rec._targetFrame = this._targetFrame;
/* 685:778 */     rec._textMark = this._textMark;
/* 686:779 */     rec._uninterpretedTail = this._uninterpretedTail;
/* 687:780 */     return rec;
/* 688:    */   }
/* 689:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HyperlinkRecord
 * JD-Core Version:    0.7.0.1
 */