/*   1:    */ package org.apache.poi.hssf.record.common;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   9:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordInput;
/*  10:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*  11:    */ import org.apache.poi.util.BitField;
/*  12:    */ import org.apache.poi.util.BitFieldFactory;
/*  13:    */ import org.apache.poi.util.LittleEndianInput;
/*  14:    */ import org.apache.poi.util.LittleEndianOutput;
/*  15:    */ import org.apache.poi.util.POILogFactory;
/*  16:    */ import org.apache.poi.util.POILogger;
/*  17:    */ import org.apache.poi.util.StringUtil;
/*  18:    */ 
/*  19:    */ public class UnicodeString
/*  20:    */   implements Comparable<UnicodeString>
/*  21:    */ {
/*  22: 47 */   private static POILogger _logger = POILogFactory.getLogger(UnicodeString.class);
/*  23:    */   private short field_1_charCount;
/*  24:    */   private byte field_2_optionflags;
/*  25:    */   private String field_3_string;
/*  26:    */   private List<FormatRun> field_4_format_runs;
/*  27:    */   private ExtRst field_5_ext_rst;
/*  28: 54 */   private static final BitField highByte = BitFieldFactory.getInstance(1);
/*  29: 56 */   private static final BitField extBit = BitFieldFactory.getInstance(4);
/*  30: 57 */   private static final BitField richText = BitFieldFactory.getInstance(8);
/*  31:    */   private UnicodeString() {}
/*  32:    */   
/*  33:    */   public static class FormatRun
/*  34:    */     implements Comparable<FormatRun>
/*  35:    */   {
/*  36:    */     final short _character;
/*  37:    */     short _fontIndex;
/*  38:    */     
/*  39:    */     public FormatRun(short character, short fontIndex)
/*  40:    */     {
/*  41: 64 */       this._character = character;
/*  42: 65 */       this._fontIndex = fontIndex;
/*  43:    */     }
/*  44:    */     
/*  45:    */     public FormatRun(LittleEndianInput in)
/*  46:    */     {
/*  47: 69 */       this(in.readShort(), in.readShort());
/*  48:    */     }
/*  49:    */     
/*  50:    */     public short getCharacterPos()
/*  51:    */     {
/*  52: 73 */       return this._character;
/*  53:    */     }
/*  54:    */     
/*  55:    */     public short getFontIndex()
/*  56:    */     {
/*  57: 77 */       return this._fontIndex;
/*  58:    */     }
/*  59:    */     
/*  60:    */     public boolean equals(Object o)
/*  61:    */     {
/*  62: 81 */       if (!(o instanceof FormatRun)) {
/*  63: 82 */         return false;
/*  64:    */       }
/*  65: 84 */       FormatRun other = (FormatRun)o;
/*  66:    */       
/*  67: 86 */       return (this._character == other._character) && (this._fontIndex == other._fontIndex);
/*  68:    */     }
/*  69:    */     
/*  70:    */     public int compareTo(FormatRun r)
/*  71:    */     {
/*  72: 90 */       if ((this._character == r._character) && (this._fontIndex == r._fontIndex)) {
/*  73: 91 */         return 0;
/*  74:    */       }
/*  75: 93 */       if (this._character == r._character) {
/*  76: 94 */         return this._fontIndex - r._fontIndex;
/*  77:    */       }
/*  78: 96 */       return this._character - r._character;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public int hashCode()
/*  82:    */     {
/*  83:101 */       if (!$assertionsDisabled) {
/*  84:101 */         throw new AssertionError("hashCode not designed");
/*  85:    */       }
/*  86:102 */       return 42;
/*  87:    */     }
/*  88:    */     
/*  89:    */     public String toString()
/*  90:    */     {
/*  91:106 */       return "character=" + this._character + ",fontIndex=" + this._fontIndex;
/*  92:    */     }
/*  93:    */     
/*  94:    */     public void serialize(LittleEndianOutput out)
/*  95:    */     {
/*  96:110 */       out.writeShort(this._character);
/*  97:111 */       out.writeShort(this._fontIndex);
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static class ExtRst
/* 102:    */     implements Comparable<ExtRst>
/* 103:    */   {
/* 104:    */     private short reserved;
/* 105:    */     private short formattingFontIndex;
/* 106:    */     private short formattingOptions;
/* 107:    */     private int numberOfRuns;
/* 108:    */     private String phoneticText;
/* 109:    */     private PhRun[] phRuns;
/* 110:    */     private byte[] extraData;
/* 111:    */     
/* 112:    */     private void populateEmpty()
/* 113:    */     {
/* 114:133 */       this.reserved = 1;
/* 115:134 */       this.phoneticText = "";
/* 116:135 */       this.phRuns = new PhRun[0];
/* 117:136 */       this.extraData = new byte[0];
/* 118:    */     }
/* 119:    */     
/* 120:    */     protected ExtRst()
/* 121:    */     {
/* 122:140 */       populateEmpty();
/* 123:    */     }
/* 124:    */     
/* 125:    */     protected ExtRst(LittleEndianInput in, int expectedLength)
/* 126:    */     {
/* 127:143 */       this.reserved = in.readShort();
/* 128:146 */       if (this.reserved == -1)
/* 129:    */       {
/* 130:147 */         populateEmpty();
/* 131:148 */         return;
/* 132:    */       }
/* 133:152 */       if (this.reserved != 1)
/* 134:    */       {
/* 135:153 */         UnicodeString._logger.log(5, new Object[] { "Warning - ExtRst has wrong magic marker, expecting 1 but found " + this.reserved + " - ignoring" });
/* 136:155 */         for (int i = 0; i < expectedLength - 2; i++) {
/* 137:156 */           in.readByte();
/* 138:    */         }
/* 139:159 */         populateEmpty();
/* 140:160 */         return;
/* 141:    */       }
/* 142:164 */       short stringDataSize = in.readShort();
/* 143:    */       
/* 144:166 */       this.formattingFontIndex = in.readShort();
/* 145:167 */       this.formattingOptions = in.readShort();
/* 146:    */       
/* 147:    */ 
/* 148:170 */       this.numberOfRuns = in.readUShort();
/* 149:171 */       short length1 = in.readShort();
/* 150:    */       
/* 151:    */ 
/* 152:174 */       short length2 = in.readShort();
/* 153:176 */       if ((length1 == 0) && (length2 > 0)) {
/* 154:177 */         length2 = 0;
/* 155:    */       }
/* 156:179 */       if (length1 != length2) {
/* 157:180 */         throw new IllegalStateException("The two length fields of the Phonetic Text don't agree! " + length1 + " vs " + length2);
/* 158:    */       }
/* 159:185 */       this.phoneticText = StringUtil.readUnicodeLE(in, length1);
/* 160:    */       
/* 161:187 */       int runData = stringDataSize - 4 - 6 - 2 * this.phoneticText.length();
/* 162:188 */       int numRuns = runData / 6;
/* 163:189 */       this.phRuns = new PhRun[numRuns];
/* 164:190 */       for (int i = 0; i < this.phRuns.length; i++) {
/* 165:191 */         this.phRuns[i] = new PhRun(in, null);
/* 166:    */       }
/* 167:194 */       int extraDataLength = runData - numRuns * 6;
/* 168:195 */       if (extraDataLength < 0)
/* 169:    */       {
/* 170:196 */         UnicodeString._logger.log(5, new Object[] { "Warning - ExtRst overran by " + (0 - extraDataLength) + " bytes" });
/* 171:197 */         extraDataLength = 0;
/* 172:    */       }
/* 173:199 */       this.extraData = new byte[extraDataLength];
/* 174:200 */       for (int i = 0; i < this.extraData.length; i++) {
/* 175:201 */         this.extraData[i] = in.readByte();
/* 176:    */       }
/* 177:    */     }
/* 178:    */     
/* 179:    */     protected int getDataSize()
/* 180:    */     {
/* 181:209 */       return 10 + 2 * this.phoneticText.length() + 6 * this.phRuns.length + this.extraData.length;
/* 182:    */     }
/* 183:    */     
/* 184:    */     protected void serialize(ContinuableRecordOutput out)
/* 185:    */     {
/* 186:213 */       int dataSize = getDataSize();
/* 187:    */       
/* 188:215 */       out.writeContinueIfRequired(8);
/* 189:216 */       out.writeShort(this.reserved);
/* 190:217 */       out.writeShort(dataSize);
/* 191:218 */       out.writeShort(this.formattingFontIndex);
/* 192:219 */       out.writeShort(this.formattingOptions);
/* 193:    */       
/* 194:221 */       out.writeContinueIfRequired(6);
/* 195:222 */       out.writeShort(this.numberOfRuns);
/* 196:223 */       out.writeShort(this.phoneticText.length());
/* 197:224 */       out.writeShort(this.phoneticText.length());
/* 198:    */       
/* 199:226 */       out.writeContinueIfRequired(this.phoneticText.length() * 2);
/* 200:227 */       StringUtil.putUnicodeLE(this.phoneticText, out);
/* 201:229 */       for (int i = 0; i < this.phRuns.length; i++) {
/* 202:230 */         this.phRuns[i].serialize(out);
/* 203:    */       }
/* 204:233 */       out.write(this.extraData);
/* 205:    */     }
/* 206:    */     
/* 207:    */     public boolean equals(Object obj)
/* 208:    */     {
/* 209:237 */       if (!(obj instanceof ExtRst)) {
/* 210:238 */         return false;
/* 211:    */       }
/* 212:240 */       ExtRst other = (ExtRst)obj;
/* 213:241 */       return compareTo(other) == 0;
/* 214:    */     }
/* 215:    */     
/* 216:    */     public int compareTo(ExtRst o)
/* 217:    */     {
/* 218:246 */       int result = this.reserved - o.reserved;
/* 219:247 */       if (result != 0) {
/* 220:248 */         return result;
/* 221:    */       }
/* 222:250 */       result = this.formattingFontIndex - o.formattingFontIndex;
/* 223:251 */       if (result != 0) {
/* 224:252 */         return result;
/* 225:    */       }
/* 226:254 */       result = this.formattingOptions - o.formattingOptions;
/* 227:255 */       if (result != 0) {
/* 228:256 */         return result;
/* 229:    */       }
/* 230:258 */       result = this.numberOfRuns - o.numberOfRuns;
/* 231:259 */       if (result != 0) {
/* 232:260 */         return result;
/* 233:    */       }
/* 234:263 */       result = this.phoneticText.compareTo(o.phoneticText);
/* 235:264 */       if (result != 0) {
/* 236:265 */         return result;
/* 237:    */       }
/* 238:268 */       result = this.phRuns.length - o.phRuns.length;
/* 239:269 */       if (result != 0) {
/* 240:270 */         return result;
/* 241:    */       }
/* 242:272 */       for (int i = 0; i < this.phRuns.length; i++)
/* 243:    */       {
/* 244:273 */         result = this.phRuns[i].phoneticTextFirstCharacterOffset - o.phRuns[i].phoneticTextFirstCharacterOffset;
/* 245:274 */         if (result != 0) {
/* 246:275 */           return result;
/* 247:    */         }
/* 248:277 */         result = this.phRuns[i].realTextFirstCharacterOffset - o.phRuns[i].realTextFirstCharacterOffset;
/* 249:278 */         if (result != 0) {
/* 250:279 */           return result;
/* 251:    */         }
/* 252:281 */         result = this.phRuns[i].realTextLength - o.phRuns[i].realTextLength;
/* 253:282 */         if (result != 0) {
/* 254:283 */           return result;
/* 255:    */         }
/* 256:    */       }
/* 257:287 */       result = Arrays.hashCode(this.extraData) - Arrays.hashCode(o.extraData);
/* 258:    */       
/* 259:289 */       return result;
/* 260:    */     }
/* 261:    */     
/* 262:    */     public int hashCode()
/* 263:    */     {
/* 264:294 */       int hash = this.reserved;
/* 265:295 */       hash = 31 * hash + this.formattingFontIndex;
/* 266:296 */       hash = 31 * hash + this.formattingOptions;
/* 267:297 */       hash = 31 * hash + this.numberOfRuns;
/* 268:298 */       hash = 31 * hash + this.phoneticText.hashCode();
/* 269:300 */       if (this.phRuns != null) {
/* 270:301 */         for (PhRun ph : this.phRuns)
/* 271:    */         {
/* 272:302 */           hash = 31 * hash + ph.phoneticTextFirstCharacterOffset;
/* 273:303 */           hash = 31 * hash + ph.realTextFirstCharacterOffset;
/* 274:304 */           hash = 31 * hash + ph.realTextLength;
/* 275:    */         }
/* 276:    */       }
/* 277:307 */       return hash;
/* 278:    */     }
/* 279:    */     
/* 280:    */     protected ExtRst clone()
/* 281:    */     {
/* 282:311 */       ExtRst ext = new ExtRst();
/* 283:312 */       ext.reserved = this.reserved;
/* 284:313 */       ext.formattingFontIndex = this.formattingFontIndex;
/* 285:314 */       ext.formattingOptions = this.formattingOptions;
/* 286:315 */       ext.numberOfRuns = this.numberOfRuns;
/* 287:316 */       ext.phoneticText = this.phoneticText;
/* 288:317 */       ext.phRuns = new PhRun[this.phRuns.length];
/* 289:318 */       for (int i = 0; i < ext.phRuns.length; i++) {
/* 290:319 */         ext.phRuns[i] = new PhRun(this.phRuns[i].phoneticTextFirstCharacterOffset, this.phRuns[i].realTextFirstCharacterOffset, this.phRuns[i].realTextLength);
/* 291:    */       }
/* 292:325 */       return ext;
/* 293:    */     }
/* 294:    */     
/* 295:    */     public short getFormattingFontIndex()
/* 296:    */     {
/* 297:329 */       return this.formattingFontIndex;
/* 298:    */     }
/* 299:    */     
/* 300:    */     public short getFormattingOptions()
/* 301:    */     {
/* 302:332 */       return this.formattingOptions;
/* 303:    */     }
/* 304:    */     
/* 305:    */     public int getNumberOfRuns()
/* 306:    */     {
/* 307:335 */       return this.numberOfRuns;
/* 308:    */     }
/* 309:    */     
/* 310:    */     public String getPhoneticText()
/* 311:    */     {
/* 312:338 */       return this.phoneticText;
/* 313:    */     }
/* 314:    */     
/* 315:    */     public PhRun[] getPhRuns()
/* 316:    */     {
/* 317:341 */       return this.phRuns;
/* 318:    */     }
/* 319:    */   }
/* 320:    */   
/* 321:    */   public static class PhRun
/* 322:    */   {
/* 323:    */     private int phoneticTextFirstCharacterOffset;
/* 324:    */     private int realTextFirstCharacterOffset;
/* 325:    */     private int realTextLength;
/* 326:    */     
/* 327:    */     public PhRun(int phoneticTextFirstCharacterOffset, int realTextFirstCharacterOffset, int realTextLength)
/* 328:    */     {
/* 329:351 */       this.phoneticTextFirstCharacterOffset = phoneticTextFirstCharacterOffset;
/* 330:352 */       this.realTextFirstCharacterOffset = realTextFirstCharacterOffset;
/* 331:353 */       this.realTextLength = realTextLength;
/* 332:    */     }
/* 333:    */     
/* 334:    */     private PhRun(LittleEndianInput in)
/* 335:    */     {
/* 336:356 */       this.phoneticTextFirstCharacterOffset = in.readUShort();
/* 337:357 */       this.realTextFirstCharacterOffset = in.readUShort();
/* 338:358 */       this.realTextLength = in.readUShort();
/* 339:    */     }
/* 340:    */     
/* 341:    */     private void serialize(ContinuableRecordOutput out)
/* 342:    */     {
/* 343:361 */       out.writeContinueIfRequired(6);
/* 344:362 */       out.writeShort(this.phoneticTextFirstCharacterOffset);
/* 345:363 */       out.writeShort(this.realTextFirstCharacterOffset);
/* 346:364 */       out.writeShort(this.realTextLength);
/* 347:    */     }
/* 348:    */   }
/* 349:    */   
/* 350:    */   public UnicodeString(String str)
/* 351:    */   {
/* 352:374 */     setString(str);
/* 353:    */   }
/* 354:    */   
/* 355:    */   public int hashCode()
/* 356:    */   {
/* 357:381 */     int stringHash = 0;
/* 358:382 */     if (this.field_3_string != null) {
/* 359:383 */       stringHash = this.field_3_string.hashCode();
/* 360:    */     }
/* 361:385 */     return this.field_1_charCount + stringHash;
/* 362:    */   }
/* 363:    */   
/* 364:    */   public boolean equals(Object o)
/* 365:    */   {
/* 366:397 */     if (!(o instanceof UnicodeString)) {
/* 367:398 */       return false;
/* 368:    */     }
/* 369:400 */     UnicodeString other = (UnicodeString)o;
/* 370:403 */     if ((this.field_1_charCount != other.field_1_charCount) || (this.field_2_optionflags != other.field_2_optionflags) || (!this.field_3_string.equals(other.field_3_string))) {
/* 371:406 */       return false;
/* 372:    */     }
/* 373:410 */     if (this.field_4_format_runs == null) {
/* 374:412 */       return other.field_4_format_runs == null;
/* 375:    */     }
/* 376:413 */     if (other.field_4_format_runs == null) {
/* 377:415 */       return false;
/* 378:    */     }
/* 379:419 */     int size = this.field_4_format_runs.size();
/* 380:420 */     if (size != other.field_4_format_runs.size()) {
/* 381:421 */       return false;
/* 382:    */     }
/* 383:424 */     for (int i = 0; i < size; i++)
/* 384:    */     {
/* 385:425 */       FormatRun run1 = (FormatRun)this.field_4_format_runs.get(i);
/* 386:426 */       FormatRun run2 = (FormatRun)other.field_4_format_runs.get(i);
/* 387:428 */       if (!run1.equals(run2)) {
/* 388:429 */         return false;
/* 389:    */       }
/* 390:    */     }
/* 391:434 */     if (this.field_5_ext_rst == null) {
/* 392:435 */       return other.field_5_ext_rst == null;
/* 393:    */     }
/* 394:436 */     if (other.field_5_ext_rst == null) {
/* 395:437 */       return false;
/* 396:    */     }
/* 397:440 */     return this.field_5_ext_rst.equals(other.field_5_ext_rst);
/* 398:    */   }
/* 399:    */   
/* 400:    */   public UnicodeString(RecordInputStream in)
/* 401:    */   {
/* 402:448 */     this.field_1_charCount = in.readShort();
/* 403:449 */     this.field_2_optionflags = in.readByte();
/* 404:    */     
/* 405:451 */     int runCount = 0;
/* 406:452 */     int extensionLength = 0;
/* 407:454 */     if (isRichText()) {
/* 408:455 */       runCount = in.readShort();
/* 409:    */     }
/* 410:458 */     if (isExtendedText()) {
/* 411:459 */       extensionLength = in.readInt();
/* 412:    */     }
/* 413:462 */     boolean isCompressed = (this.field_2_optionflags & 0x1) == 0;
/* 414:463 */     int cc = getCharCount();
/* 415:464 */     this.field_3_string = (isCompressed ? in.readCompressedUnicode(cc) : in.readUnicodeLEString(cc));
/* 416:466 */     if ((isRichText()) && (runCount > 0))
/* 417:    */     {
/* 418:467 */       this.field_4_format_runs = new ArrayList(runCount);
/* 419:468 */       for (int i = 0; i < runCount; i++) {
/* 420:469 */         this.field_4_format_runs.add(new FormatRun(in));
/* 421:    */       }
/* 422:    */     }
/* 423:473 */     if ((isExtendedText()) && (extensionLength > 0))
/* 424:    */     {
/* 425:474 */       this.field_5_ext_rst = new ExtRst(new ContinuableRecordInput(in), extensionLength);
/* 426:475 */       if (this.field_5_ext_rst.getDataSize() + 4 != extensionLength) {
/* 427:476 */         _logger.log(5, new Object[] { "ExtRst was supposed to be " + extensionLength + " bytes long, but seems to actually be " + (this.field_5_ext_rst.getDataSize() + 4) });
/* 428:    */       }
/* 429:    */     }
/* 430:    */   }
/* 431:    */   
/* 432:    */   public int getCharCount()
/* 433:    */   {
/* 434:490 */     if (this.field_1_charCount < 0) {
/* 435:491 */       return this.field_1_charCount + 65536;
/* 436:    */     }
/* 437:493 */     return this.field_1_charCount;
/* 438:    */   }
/* 439:    */   
/* 440:    */   public short getCharCountShort()
/* 441:    */   {
/* 442:503 */     return this.field_1_charCount;
/* 443:    */   }
/* 444:    */   
/* 445:    */   public void setCharCount(short cc)
/* 446:    */   {
/* 447:513 */     this.field_1_charCount = cc;
/* 448:    */   }
/* 449:    */   
/* 450:    */   public byte getOptionFlags()
/* 451:    */   {
/* 452:526 */     return this.field_2_optionflags;
/* 453:    */   }
/* 454:    */   
/* 455:    */   public void setOptionFlags(byte of)
/* 456:    */   {
/* 457:539 */     this.field_2_optionflags = of;
/* 458:    */   }
/* 459:    */   
/* 460:    */   public String getString()
/* 461:    */   {
/* 462:547 */     return this.field_3_string;
/* 463:    */   }
/* 464:    */   
/* 465:    */   public void setString(String string)
/* 466:    */   {
/* 467:557 */     this.field_3_string = string;
/* 468:558 */     setCharCount((short)this.field_3_string.length());
/* 469:    */     
/* 470:    */ 
/* 471:    */ 
/* 472:562 */     boolean useUTF16 = false;
/* 473:563 */     int strlen = string.length();
/* 474:565 */     for (int j = 0; j < strlen; j++) {
/* 475:566 */       if (string.charAt(j) > 'ÿ')
/* 476:    */       {
/* 477:567 */         useUTF16 = true;
/* 478:568 */         break;
/* 479:    */       }
/* 480:    */     }
/* 481:571 */     if (useUTF16) {
/* 482:573 */       this.field_2_optionflags = highByte.setByte(this.field_2_optionflags);
/* 483:    */     } else {
/* 484:575 */       this.field_2_optionflags = highByte.clearByte(this.field_2_optionflags);
/* 485:    */     }
/* 486:    */   }
/* 487:    */   
/* 488:    */   public int getFormatRunCount()
/* 489:    */   {
/* 490:580 */     return this.field_4_format_runs == null ? 0 : this.field_4_format_runs.size();
/* 491:    */   }
/* 492:    */   
/* 493:    */   public FormatRun getFormatRun(int index)
/* 494:    */   {
/* 495:584 */     if (this.field_4_format_runs == null) {
/* 496:585 */       return null;
/* 497:    */     }
/* 498:587 */     if ((index < 0) || (index >= this.field_4_format_runs.size())) {
/* 499:588 */       return null;
/* 500:    */     }
/* 501:590 */     return (FormatRun)this.field_4_format_runs.get(index);
/* 502:    */   }
/* 503:    */   
/* 504:    */   private int findFormatRunAt(int characterPos)
/* 505:    */   {
/* 506:594 */     int size = this.field_4_format_runs.size();
/* 507:595 */     for (int i = 0; i < size; i++)
/* 508:    */     {
/* 509:596 */       FormatRun r = (FormatRun)this.field_4_format_runs.get(i);
/* 510:597 */       if (r._character == characterPos) {
/* 511:598 */         return i;
/* 512:    */       }
/* 513:599 */       if (r._character > characterPos) {
/* 514:600 */         return -1;
/* 515:    */       }
/* 516:    */     }
/* 517:603 */     return -1;
/* 518:    */   }
/* 519:    */   
/* 520:    */   public void addFormatRun(FormatRun r)
/* 521:    */   {
/* 522:612 */     if (this.field_4_format_runs == null) {
/* 523:613 */       this.field_4_format_runs = new ArrayList();
/* 524:    */     }
/* 525:616 */     int index = findFormatRunAt(r._character);
/* 526:617 */     if (index != -1) {
/* 527:618 */       this.field_4_format_runs.remove(index);
/* 528:    */     }
/* 529:621 */     this.field_4_format_runs.add(r);
/* 530:    */     
/* 531:    */ 
/* 532:624 */     Collections.sort(this.field_4_format_runs);
/* 533:    */     
/* 534:    */ 
/* 535:627 */     this.field_2_optionflags = richText.setByte(this.field_2_optionflags);
/* 536:    */   }
/* 537:    */   
/* 538:    */   public Iterator<FormatRun> formatIterator()
/* 539:    */   {
/* 540:631 */     if (this.field_4_format_runs != null) {
/* 541:632 */       return this.field_4_format_runs.iterator();
/* 542:    */     }
/* 543:634 */     return null;
/* 544:    */   }
/* 545:    */   
/* 546:    */   public void removeFormatRun(FormatRun r)
/* 547:    */   {
/* 548:638 */     this.field_4_format_runs.remove(r);
/* 549:639 */     if (this.field_4_format_runs.size() == 0)
/* 550:    */     {
/* 551:640 */       this.field_4_format_runs = null;
/* 552:641 */       this.field_2_optionflags = richText.clearByte(this.field_2_optionflags);
/* 553:    */     }
/* 554:    */   }
/* 555:    */   
/* 556:    */   public void clearFormatting()
/* 557:    */   {
/* 558:646 */     this.field_4_format_runs = null;
/* 559:647 */     this.field_2_optionflags = richText.clearByte(this.field_2_optionflags);
/* 560:    */   }
/* 561:    */   
/* 562:    */   public ExtRst getExtendedRst()
/* 563:    */   {
/* 564:652 */     return this.field_5_ext_rst;
/* 565:    */   }
/* 566:    */   
/* 567:    */   void setExtendedRst(ExtRst ext_rst)
/* 568:    */   {
/* 569:655 */     if (ext_rst != null) {
/* 570:656 */       this.field_2_optionflags = extBit.setByte(this.field_2_optionflags);
/* 571:    */     } else {
/* 572:658 */       this.field_2_optionflags = extBit.clearByte(this.field_2_optionflags);
/* 573:    */     }
/* 574:660 */     this.field_5_ext_rst = ext_rst;
/* 575:    */   }
/* 576:    */   
/* 577:    */   public void swapFontUse(short oldFontIndex, short newFontIndex)
/* 578:    */   {
/* 579:671 */     for (FormatRun run : this.field_4_format_runs) {
/* 580:672 */       if (run._fontIndex == oldFontIndex) {
/* 581:673 */         run._fontIndex = newFontIndex;
/* 582:    */       }
/* 583:    */     }
/* 584:    */   }
/* 585:    */   
/* 586:    */   public String toString()
/* 587:    */   {
/* 588:686 */     return getString();
/* 589:    */   }
/* 590:    */   
/* 591:    */   public String getDebugInfo()
/* 592:    */   {
/* 593:698 */     StringBuffer buffer = new StringBuffer();
/* 594:    */     
/* 595:700 */     buffer.append("[UNICODESTRING]\n");
/* 596:701 */     buffer.append("    .charcount       = ").append(Integer.toHexString(getCharCount())).append("\n");
/* 597:    */     
/* 598:703 */     buffer.append("    .optionflags     = ").append(Integer.toHexString(getOptionFlags())).append("\n");
/* 599:    */     
/* 600:705 */     buffer.append("    .string          = ").append(getString()).append("\n");
/* 601:706 */     if (this.field_4_format_runs != null) {
/* 602:707 */       for (int i = 0; i < this.field_4_format_runs.size(); i++)
/* 603:    */       {
/* 604:708 */         FormatRun r = (FormatRun)this.field_4_format_runs.get(i);
/* 605:709 */         buffer.append("      .format_run" + i + "          = ").append(r).append("\n");
/* 606:    */       }
/* 607:    */     }
/* 608:712 */     if (this.field_5_ext_rst != null)
/* 609:    */     {
/* 610:713 */       buffer.append("    .field_5_ext_rst          = ").append("\n");
/* 611:714 */       buffer.append(this.field_5_ext_rst).append("\n");
/* 612:    */     }
/* 613:716 */     buffer.append("[/UNICODESTRING]\n");
/* 614:717 */     return buffer.toString();
/* 615:    */   }
/* 616:    */   
/* 617:    */   public void serialize(ContinuableRecordOutput out)
/* 618:    */   {
/* 619:726 */     int numberOfRichTextRuns = 0;
/* 620:727 */     int extendedDataSize = 0;
/* 621:728 */     if ((isRichText()) && (this.field_4_format_runs != null)) {
/* 622:729 */       numberOfRichTextRuns = this.field_4_format_runs.size();
/* 623:    */     }
/* 624:731 */     if ((isExtendedText()) && (this.field_5_ext_rst != null)) {
/* 625:732 */       extendedDataSize = 4 + this.field_5_ext_rst.getDataSize();
/* 626:    */     }
/* 627:737 */     out.writeString(this.field_3_string, numberOfRichTextRuns, extendedDataSize);
/* 628:739 */     if (numberOfRichTextRuns > 0) {
/* 629:742 */       for (int i = 0; i < numberOfRichTextRuns; i++)
/* 630:    */       {
/* 631:743 */         if (out.getAvailableSpace() < 4) {
/* 632:744 */           out.writeContinue();
/* 633:    */         }
/* 634:746 */         FormatRun r = (FormatRun)this.field_4_format_runs.get(i);
/* 635:747 */         r.serialize(out);
/* 636:    */       }
/* 637:    */     }
/* 638:751 */     if ((extendedDataSize > 0) && (this.field_5_ext_rst != null)) {
/* 639:752 */       this.field_5_ext_rst.serialize(out);
/* 640:    */     }
/* 641:    */   }
/* 642:    */   
/* 643:    */   public int compareTo(UnicodeString str)
/* 644:    */   {
/* 645:758 */     int result = getString().compareTo(str.getString());
/* 646:761 */     if (result != 0) {
/* 647:762 */       return result;
/* 648:    */     }
/* 649:766 */     if (this.field_4_format_runs == null) {
/* 650:769 */       return str.field_4_format_runs == null ? 0 : 1;
/* 651:    */     }
/* 652:770 */     if (str.field_4_format_runs == null) {
/* 653:772 */       return -1;
/* 654:    */     }
/* 655:776 */     int size = this.field_4_format_runs.size();
/* 656:777 */     if (size != str.field_4_format_runs.size()) {
/* 657:778 */       return size - str.field_4_format_runs.size();
/* 658:    */     }
/* 659:781 */     for (int i = 0; i < size; i++)
/* 660:    */     {
/* 661:782 */       FormatRun run1 = (FormatRun)this.field_4_format_runs.get(i);
/* 662:783 */       FormatRun run2 = (FormatRun)str.field_4_format_runs.get(i);
/* 663:    */       
/* 664:785 */       result = run1.compareTo(run2);
/* 665:786 */       if (result != 0) {
/* 666:787 */         return result;
/* 667:    */       }
/* 668:    */     }
/* 669:792 */     if (this.field_5_ext_rst == null) {
/* 670:793 */       return str.field_5_ext_rst == null ? 0 : 1;
/* 671:    */     }
/* 672:794 */     if (str.field_5_ext_rst == null) {
/* 673:795 */       return -1;
/* 674:    */     }
/* 675:797 */     return this.field_5_ext_rst.compareTo(str.field_5_ext_rst);
/* 676:    */   }
/* 677:    */   
/* 678:    */   private boolean isRichText()
/* 679:    */   {
/* 680:802 */     return richText.isSet(getOptionFlags());
/* 681:    */   }
/* 682:    */   
/* 683:    */   private boolean isExtendedText()
/* 684:    */   {
/* 685:806 */     return extBit.isSet(getOptionFlags());
/* 686:    */   }
/* 687:    */   
/* 688:    */   public Object clone()
/* 689:    */   {
/* 690:810 */     UnicodeString str = new UnicodeString();
/* 691:811 */     str.field_1_charCount = this.field_1_charCount;
/* 692:812 */     str.field_2_optionflags = this.field_2_optionflags;
/* 693:813 */     str.field_3_string = this.field_3_string;
/* 694:814 */     if (this.field_4_format_runs != null)
/* 695:    */     {
/* 696:815 */       str.field_4_format_runs = new ArrayList();
/* 697:816 */       for (FormatRun r : this.field_4_format_runs) {
/* 698:817 */         str.field_4_format_runs.add(new FormatRun(r._character, r._fontIndex));
/* 699:    */       }
/* 700:    */     }
/* 701:820 */     if (this.field_5_ext_rst != null) {
/* 702:821 */       str.field_5_ext_rst = this.field_5_ext_rst.clone();
/* 703:    */     }
/* 704:824 */     return str;
/* 705:    */   }
/* 706:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.common.UnicodeString

 * JD-Core Version:    0.7.0.1

 */