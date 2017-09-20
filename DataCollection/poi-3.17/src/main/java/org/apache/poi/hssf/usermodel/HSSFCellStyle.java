/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   5:    */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   6:    */ import org.apache.poi.hssf.record.FontRecord;
/*   7:    */ import org.apache.poi.hssf.record.FormatRecord;
/*   8:    */ import org.apache.poi.hssf.record.StyleRecord;
/*   9:    */ import org.apache.poi.hssf.util.HSSFColor;
/*  10:    */ import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
/*  11:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*  12:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  13:    */ import org.apache.poi.ss.usermodel.FillPatternType;
/*  14:    */ import org.apache.poi.ss.usermodel.Font;
/*  15:    */ import org.apache.poi.ss.usermodel.HorizontalAlignment;
/*  16:    */ import org.apache.poi.ss.usermodel.VerticalAlignment;
/*  17:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  18:    */ 
/*  19:    */ public final class HSSFCellStyle
/*  20:    */   implements CellStyle
/*  21:    */ {
/*  22:    */   private final ExtendedFormatRecord _format;
/*  23:    */   private final short _index;
/*  24:    */   private final InternalWorkbook _workbook;
/*  25:    */   
/*  26:    */   protected HSSFCellStyle(short index, ExtendedFormatRecord rec, HSSFWorkbook workbook)
/*  27:    */   {
/*  28: 53 */     this(index, rec, workbook.getWorkbook());
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected HSSFCellStyle(short index, ExtendedFormatRecord rec, InternalWorkbook workbook)
/*  32:    */   {
/*  33: 57 */     this._workbook = workbook;
/*  34: 58 */     this._index = index;
/*  35: 59 */     this._format = rec;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public short getIndex()
/*  39:    */   {
/*  40: 69 */     return this._index;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public HSSFCellStyle getParentStyle()
/*  44:    */   {
/*  45: 78 */     short parentIndex = this._format.getParentIndex();
/*  46: 80 */     if ((parentIndex == 0) || (parentIndex == 4095)) {
/*  47: 81 */       return null;
/*  48:    */     }
/*  49: 83 */     return new HSSFCellStyle(parentIndex, this._workbook.getExFormatAt(parentIndex), this._workbook);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setDataFormat(short fmt)
/*  53:    */   {
/*  54: 97 */     this._format.setFormatIndex(fmt);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public short getDataFormat()
/*  58:    */   {
/*  59:107 */     return this._format.getFormatIndex();
/*  60:    */   }
/*  61:    */   
/*  62:113 */   private static final ThreadLocal<Short> lastDateFormat = new ThreadLocal()
/*  63:    */   {
/*  64:    */     protected Short initialValue()
/*  65:    */     {
/*  66:116 */       return Short.valueOf();
/*  67:    */     }
/*  68:    */   };
/*  69:119 */   private static final ThreadLocal<List<FormatRecord>> lastFormats = new ThreadLocal();
/*  70:120 */   private static final ThreadLocal<String> getDataFormatStringCache = new ThreadLocal();
/*  71:    */   
/*  72:    */   public String getDataFormatString()
/*  73:    */   {
/*  74:130 */     if ((getDataFormatStringCache.get() != null) && 
/*  75:131 */       (((Short)lastDateFormat.get()).shortValue() == getDataFormat()) && (this._workbook.getFormats().equals(lastFormats.get()))) {
/*  76:132 */       return (String)getDataFormatStringCache.get();
/*  77:    */     }
/*  78:136 */     lastFormats.set(this._workbook.getFormats());
/*  79:137 */     lastDateFormat.set(Short.valueOf(getDataFormat()));
/*  80:    */     
/*  81:139 */     getDataFormatStringCache.set(getDataFormatString(this._workbook));
/*  82:    */     
/*  83:141 */     return (String)getDataFormatStringCache.get();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public String getDataFormatString(Workbook workbook)
/*  87:    */   {
/*  88:152 */     HSSFDataFormat format = new HSSFDataFormat(((HSSFWorkbook)workbook).getWorkbook());
/*  89:    */     
/*  90:154 */     int idx = getDataFormat();
/*  91:155 */     return idx == -1 ? "General" : format.getFormat(getDataFormat());
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String getDataFormatString(InternalWorkbook workbook)
/*  95:    */   {
/*  96:163 */     HSSFDataFormat format = new HSSFDataFormat(workbook);
/*  97:    */     
/*  98:165 */     return format.getFormat(getDataFormat());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setFont(Font font)
/* 102:    */   {
/* 103:176 */     setFont((HSSFFont)font);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setFont(HSSFFont font)
/* 107:    */   {
/* 108:179 */     this._format.setIndentNotParentFont(true);
/* 109:180 */     short fontindex = font.getIndex();
/* 110:181 */     this._format.setFontIndex(fontindex);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public short getFontIndex()
/* 114:    */   {
/* 115:191 */     return this._format.getFontIndex();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public HSSFFont getFont(Workbook parentWorkbook)
/* 119:    */   {
/* 120:201 */     return ((HSSFWorkbook)parentWorkbook).getFontAt(getFontIndex());
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setHidden(boolean hidden)
/* 124:    */   {
/* 125:211 */     this._format.setIndentNotParentCellOptions(true);
/* 126:212 */     this._format.setHidden(hidden);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean getHidden()
/* 130:    */   {
/* 131:222 */     return this._format.isHidden();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setLocked(boolean locked)
/* 135:    */   {
/* 136:232 */     this._format.setIndentNotParentCellOptions(true);
/* 137:233 */     this._format.setLocked(locked);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean getLocked()
/* 141:    */   {
/* 142:243 */     return this._format.isLocked();
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setQuotePrefixed(boolean quotePrefix)
/* 146:    */   {
/* 147:253 */     this._format.set123Prefix(quotePrefix);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean getQuotePrefixed()
/* 151:    */   {
/* 152:261 */     return this._format.get123Prefix();
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setAlignment(HorizontalAlignment align)
/* 156:    */   {
/* 157:271 */     this._format.setIndentNotParentAlignment(true);
/* 158:272 */     this._format.setAlignment(align.getCode());
/* 159:    */   }
/* 160:    */   
/* 161:    */   @Deprecated
/* 162:    */   public short getAlignment()
/* 163:    */   {
/* 164:284 */     return this._format.getAlignment();
/* 165:    */   }
/* 166:    */   
/* 167:    */   public HorizontalAlignment getAlignmentEnum()
/* 168:    */   {
/* 169:293 */     return HorizontalAlignment.forInt(this._format.getAlignment());
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setWrapText(boolean wrapped)
/* 173:    */   {
/* 174:303 */     this._format.setIndentNotParentAlignment(true);
/* 175:304 */     this._format.setWrapText(wrapped);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean getWrapText()
/* 179:    */   {
/* 180:314 */     return this._format.getWrapText();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setVerticalAlignment(VerticalAlignment align)
/* 184:    */   {
/* 185:324 */     this._format.setVerticalAlignment(align.getCode());
/* 186:    */   }
/* 187:    */   
/* 188:    */   @Deprecated
/* 189:    */   public short getVerticalAlignment()
/* 190:    */   {
/* 191:337 */     return this._format.getVerticalAlignment();
/* 192:    */   }
/* 193:    */   
/* 194:    */   public VerticalAlignment getVerticalAlignmentEnum()
/* 195:    */   {
/* 196:346 */     return VerticalAlignment.forInt(this._format.getVerticalAlignment());
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setRotation(short rotation)
/* 200:    */   {
/* 201:362 */     if (rotation != 255) {
/* 202:365 */       if ((rotation < 0) && (rotation >= -90)) {
/* 203:368 */         rotation = (short)(90 - rotation);
/* 204:370 */       } else if ((rotation <= 90) || (rotation > 180)) {
/* 205:374 */         if ((rotation < -90) || (rotation > 90)) {
/* 206:376 */           throw new IllegalArgumentException("The rotation must be between -90 and 90 degrees, or 0xff");
/* 207:    */         }
/* 208:    */       }
/* 209:    */     }
/* 210:378 */     this._format.setRotation(rotation);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public short getRotation()
/* 214:    */   {
/* 215:388 */     short rotation = this._format.getRotation();
/* 216:389 */     if (rotation == 255) {
/* 217:391 */       return rotation;
/* 218:    */     }
/* 219:393 */     if (rotation > 90) {
/* 220:395 */       rotation = (short)(90 - rotation);
/* 221:    */     }
/* 222:397 */     return rotation;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void setIndention(short indent)
/* 226:    */   {
/* 227:407 */     this._format.setIndent(indent);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public short getIndention()
/* 231:    */   {
/* 232:417 */     return this._format.getIndent();
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void setBorderLeft(BorderStyle border)
/* 236:    */   {
/* 237:428 */     this._format.setIndentNotParentBorder(true);
/* 238:429 */     this._format.setBorderLeft(border.getCode());
/* 239:    */   }
/* 240:    */   
/* 241:    */   @Deprecated
/* 242:    */   public short getBorderLeft()
/* 243:    */   {
/* 244:441 */     return this._format.getBorderLeft();
/* 245:    */   }
/* 246:    */   
/* 247:    */   public BorderStyle getBorderLeftEnum()
/* 248:    */   {
/* 249:451 */     return BorderStyle.valueOf(this._format.getBorderLeft());
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void setBorderRight(BorderStyle border)
/* 253:    */   {
/* 254:462 */     this._format.setIndentNotParentBorder(true);
/* 255:463 */     this._format.setBorderRight(border.getCode());
/* 256:    */   }
/* 257:    */   
/* 258:    */   @Deprecated
/* 259:    */   public short getBorderRight()
/* 260:    */   {
/* 261:475 */     return this._format.getBorderRight();
/* 262:    */   }
/* 263:    */   
/* 264:    */   public BorderStyle getBorderRightEnum()
/* 265:    */   {
/* 266:485 */     return BorderStyle.valueOf(this._format.getBorderRight());
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setBorderTop(BorderStyle border)
/* 270:    */   {
/* 271:496 */     this._format.setIndentNotParentBorder(true);
/* 272:497 */     this._format.setBorderTop(border.getCode());
/* 273:    */   }
/* 274:    */   
/* 275:    */   @Deprecated
/* 276:    */   public short getBorderTop()
/* 277:    */   {
/* 278:509 */     return this._format.getBorderTop();
/* 279:    */   }
/* 280:    */   
/* 281:    */   public BorderStyle getBorderTopEnum()
/* 282:    */   {
/* 283:519 */     return BorderStyle.valueOf(this._format.getBorderTop());
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void setBorderBottom(BorderStyle border)
/* 287:    */   {
/* 288:530 */     this._format.setIndentNotParentBorder(true);
/* 289:531 */     this._format.setBorderBottom(border.getCode());
/* 290:    */   }
/* 291:    */   
/* 292:    */   @Deprecated
/* 293:    */   public short getBorderBottom()
/* 294:    */   {
/* 295:543 */     return this._format.getBorderBottom();
/* 296:    */   }
/* 297:    */   
/* 298:    */   public BorderStyle getBorderBottomEnum()
/* 299:    */   {
/* 300:553 */     return BorderStyle.valueOf(this._format.getBorderBottom());
/* 301:    */   }
/* 302:    */   
/* 303:    */   public void setLeftBorderColor(short color)
/* 304:    */   {
/* 305:563 */     this._format.setLeftBorderPaletteIdx(color);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public short getLeftBorderColor()
/* 309:    */   {
/* 310:574 */     return this._format.getLeftBorderPaletteIdx();
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void setRightBorderColor(short color)
/* 314:    */   {
/* 315:584 */     this._format.setRightBorderPaletteIdx(color);
/* 316:    */   }
/* 317:    */   
/* 318:    */   public short getRightBorderColor()
/* 319:    */   {
/* 320:595 */     return this._format.getRightBorderPaletteIdx();
/* 321:    */   }
/* 322:    */   
/* 323:    */   public void setTopBorderColor(short color)
/* 324:    */   {
/* 325:605 */     this._format.setTopBorderPaletteIdx(color);
/* 326:    */   }
/* 327:    */   
/* 328:    */   public short getTopBorderColor()
/* 329:    */   {
/* 330:616 */     return this._format.getTopBorderPaletteIdx();
/* 331:    */   }
/* 332:    */   
/* 333:    */   public void setBottomBorderColor(short color)
/* 334:    */   {
/* 335:626 */     this._format.setBottomBorderPaletteIdx(color);
/* 336:    */   }
/* 337:    */   
/* 338:    */   public short getBottomBorderColor()
/* 339:    */   {
/* 340:637 */     return this._format.getBottomBorderPaletteIdx();
/* 341:    */   }
/* 342:    */   
/* 343:    */   public void setFillPattern(FillPatternType fp)
/* 344:    */   {
/* 345:649 */     this._format.setAdtlFillPattern(fp.getCode());
/* 346:    */   }
/* 347:    */   
/* 348:    */   @Deprecated
/* 349:    */   public short getFillPattern()
/* 350:    */   {
/* 351:661 */     return getFillPatternEnum().getCode();
/* 352:    */   }
/* 353:    */   
/* 354:    */   public FillPatternType getFillPatternEnum()
/* 355:    */   {
/* 356:671 */     return FillPatternType.forInt(this._format.getAdtlFillPattern());
/* 357:    */   }
/* 358:    */   
/* 359:    */   private void checkDefaultBackgroundFills()
/* 360:    */   {
/* 361:685 */     short autoIdx = HSSFColorPredefined.AUTOMATIC.getIndex();
/* 362:686 */     if (this._format.getFillForeground() == autoIdx)
/* 363:    */     {
/* 364:690 */       if (this._format.getFillBackground() != autoIdx + 1) {
/* 365:691 */         setFillBackgroundColor((short)(autoIdx + 1));
/* 366:    */       }
/* 367:    */     }
/* 368:693 */     else if (this._format.getFillBackground() == autoIdx + 1) {
/* 369:695 */       if (this._format.getFillForeground() != autoIdx) {
/* 370:696 */         setFillBackgroundColor(autoIdx);
/* 371:    */       }
/* 372:    */     }
/* 373:    */   }
/* 374:    */   
/* 375:    */   public void setFillBackgroundColor(short bg)
/* 376:    */   {
/* 377:729 */     this._format.setFillBackground(bg);
/* 378:730 */     checkDefaultBackgroundFills();
/* 379:    */   }
/* 380:    */   
/* 381:    */   public short getFillBackgroundColor()
/* 382:    */   {
/* 383:742 */     short autoIndex = HSSFColorPredefined.AUTOMATIC.getIndex();
/* 384:743 */     short result = this._format.getFillBackground();
/* 385:746 */     if (result == autoIndex + 1) {
/* 386:747 */       return autoIndex;
/* 387:    */     }
/* 388:749 */     return result;
/* 389:    */   }
/* 390:    */   
/* 391:    */   public HSSFColor getFillBackgroundColorColor()
/* 392:    */   {
/* 393:754 */     HSSFPalette pallette = new HSSFPalette(this._workbook.getCustomPalette());
/* 394:    */     
/* 395:    */ 
/* 396:757 */     return pallette.getColor(getFillBackgroundColor());
/* 397:    */   }
/* 398:    */   
/* 399:    */   public void setFillForegroundColor(short bg)
/* 400:    */   {
/* 401:770 */     this._format.setFillForeground(bg);
/* 402:771 */     checkDefaultBackgroundFills();
/* 403:    */   }
/* 404:    */   
/* 405:    */   public short getFillForegroundColor()
/* 406:    */   {
/* 407:784 */     return this._format.getFillForeground();
/* 408:    */   }
/* 409:    */   
/* 410:    */   public HSSFColor getFillForegroundColorColor()
/* 411:    */   {
/* 412:789 */     HSSFPalette pallette = new HSSFPalette(this._workbook.getCustomPalette());
/* 413:    */     
/* 414:    */ 
/* 415:792 */     return pallette.getColor(getFillForegroundColor());
/* 416:    */   }
/* 417:    */   
/* 418:    */   public String getUserStyleName()
/* 419:    */   {
/* 420:803 */     StyleRecord sr = this._workbook.getStyleRecord(this._index);
/* 421:804 */     if (sr == null) {
/* 422:805 */       return null;
/* 423:    */     }
/* 424:807 */     if (sr.isBuiltin()) {
/* 425:808 */       return null;
/* 426:    */     }
/* 427:810 */     return sr.getName();
/* 428:    */   }
/* 429:    */   
/* 430:    */   public void setUserStyleName(String styleName)
/* 431:    */   {
/* 432:818 */     StyleRecord sr = this._workbook.getStyleRecord(this._index);
/* 433:819 */     if (sr == null) {
/* 434:820 */       sr = this._workbook.createStyleRecord(this._index);
/* 435:    */     }
/* 436:824 */     if ((sr.isBuiltin()) && (this._index <= 20)) {
/* 437:825 */       throw new IllegalArgumentException("Unable to set user specified style names for built in styles!");
/* 438:    */     }
/* 439:827 */     sr.setName(styleName);
/* 440:    */   }
/* 441:    */   
/* 442:    */   public void setShrinkToFit(boolean shrinkToFit)
/* 443:    */   {
/* 444:836 */     this._format.setShrinkToFit(shrinkToFit);
/* 445:    */   }
/* 446:    */   
/* 447:    */   public boolean getShrinkToFit()
/* 448:    */   {
/* 449:844 */     return this._format.getShrinkToFit();
/* 450:    */   }
/* 451:    */   
/* 452:    */   public short getReadingOrder()
/* 453:    */   {
/* 454:856 */     return this._format.getReadingOrder();
/* 455:    */   }
/* 456:    */   
/* 457:    */   public void setReadingOrder(short order)
/* 458:    */   {
/* 459:867 */     this._format.setReadingOrder(order);
/* 460:    */   }
/* 461:    */   
/* 462:    */   public void verifyBelongsToWorkbook(HSSFWorkbook wb)
/* 463:    */   {
/* 464:879 */     if (wb.getWorkbook() != this._workbook) {
/* 465:880 */       throw new IllegalArgumentException("This Style does not belong to the supplied Workbook. Are you trying to assign a style from one workbook to the cell of a differnt workbook?");
/* 466:    */     }
/* 467:    */   }
/* 468:    */   
/* 469:    */   public void cloneStyleFrom(CellStyle source)
/* 470:    */   {
/* 471:898 */     if ((source instanceof HSSFCellStyle)) {
/* 472:899 */       cloneStyleFrom((HSSFCellStyle)source);
/* 473:    */     } else {
/* 474:901 */       throw new IllegalArgumentException("Can only clone from one HSSFCellStyle to another, not between HSSFCellStyle and XSSFCellStyle");
/* 475:    */     }
/* 476:    */   }
/* 477:    */   
/* 478:    */   public void cloneStyleFrom(HSSFCellStyle source)
/* 479:    */   {
/* 480:907 */     this._format.cloneStyleFrom(source._format);
/* 481:910 */     if (this._workbook != source._workbook)
/* 482:    */     {
/* 483:912 */       lastDateFormat.set(Short.valueOf((short)-32768));
/* 484:913 */       lastFormats.set(null);
/* 485:914 */       getDataFormatStringCache.set(null);
/* 486:    */       
/* 487:    */ 
/* 488:    */ 
/* 489:918 */       short fmt = (short)this._workbook.createFormat(source.getDataFormatString());
/* 490:919 */       setDataFormat(fmt);
/* 491:    */       
/* 492:    */ 
/* 493:    */ 
/* 494:923 */       FontRecord fr = this._workbook.createNewFont();
/* 495:924 */       fr.cloneStyleFrom(source._workbook.getFontRecordAt(source.getFontIndex()));
/* 496:    */       
/* 497:    */ 
/* 498:    */ 
/* 499:    */ 
/* 500:    */ 
/* 501:930 */       HSSFFont font = new HSSFFont((short)this._workbook.getFontIndex(fr), fr);
/* 502:    */       
/* 503:    */ 
/* 504:933 */       setFont(font);
/* 505:    */     }
/* 506:    */   }
/* 507:    */   
/* 508:    */   public int hashCode()
/* 509:    */   {
/* 510:940 */     int prime = 31;
/* 511:941 */     int result = 1;
/* 512:942 */     result = 31 * result + (this._format == null ? 0 : this._format.hashCode());
/* 513:943 */     result = 31 * result + this._index;
/* 514:944 */     return result;
/* 515:    */   }
/* 516:    */   
/* 517:    */   public boolean equals(Object obj)
/* 518:    */   {
/* 519:949 */     if (this == obj) {
/* 520:950 */       return true;
/* 521:    */     }
/* 522:952 */     if (obj == null) {
/* 523:953 */       return false;
/* 524:    */     }
/* 525:955 */     if ((obj instanceof HSSFCellStyle))
/* 526:    */     {
/* 527:956 */       HSSFCellStyle other = (HSSFCellStyle)obj;
/* 528:957 */       if (this._format == null)
/* 529:    */       {
/* 530:958 */         if (other._format != null) {
/* 531:959 */           return false;
/* 532:    */         }
/* 533:    */       }
/* 534:961 */       else if (!this._format.equals(other._format)) {
/* 535:962 */         return false;
/* 536:    */       }
/* 537:964 */       if (this._index != other._index) {
/* 538:965 */         return false;
/* 539:    */       }
/* 540:967 */       return true;
/* 541:    */     }
/* 542:969 */     return false;
/* 543:    */   }
/* 544:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFCellStyle

 * JD-Core Version:    0.7.0.1

 */