/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.hssf.model.RecordStream;
/*   9:    */ import org.apache.poi.hssf.record.BottomMarginRecord;
/*  10:    */ import org.apache.poi.hssf.record.ContinueRecord;
/*  11:    */ import org.apache.poi.hssf.record.FooterRecord;
/*  12:    */ import org.apache.poi.hssf.record.HCenterRecord;
/*  13:    */ import org.apache.poi.hssf.record.HeaderFooterRecord;
/*  14:    */ import org.apache.poi.hssf.record.HeaderRecord;
/*  15:    */ import org.apache.poi.hssf.record.HorizontalPageBreakRecord;
/*  16:    */ import org.apache.poi.hssf.record.LeftMarginRecord;
/*  17:    */ import org.apache.poi.hssf.record.Margin;
/*  18:    */ import org.apache.poi.hssf.record.PageBreakRecord;
/*  19:    */ import org.apache.poi.hssf.record.PageBreakRecord.Break;
/*  20:    */ import org.apache.poi.hssf.record.PrintSetupRecord;
/*  21:    */ import org.apache.poi.hssf.record.Record;
/*  22:    */ import org.apache.poi.hssf.record.RecordBase;
/*  23:    */ import org.apache.poi.hssf.record.RightMarginRecord;
/*  24:    */ import org.apache.poi.hssf.record.TopMarginRecord;
/*  25:    */ import org.apache.poi.hssf.record.UserSViewBegin;
/*  26:    */ import org.apache.poi.hssf.record.VCenterRecord;
/*  27:    */ import org.apache.poi.hssf.record.VerticalPageBreakRecord;
/*  28:    */ import org.apache.poi.util.HexDump;
/*  29:    */ import org.apache.poi.util.RecordFormatException;
/*  30:    */ 
/*  31:    */ public final class PageSettingsBlock
/*  32:    */   extends RecordAggregate
/*  33:    */ {
/*  34:    */   private PageBreakRecord _rowBreaksRecord;
/*  35:    */   private PageBreakRecord _columnBreaksRecord;
/*  36:    */   private HeaderRecord _header;
/*  37:    */   private FooterRecord _footer;
/*  38:    */   private HCenterRecord _hCenter;
/*  39:    */   private VCenterRecord _vCenter;
/*  40:    */   private LeftMarginRecord _leftMargin;
/*  41:    */   private RightMarginRecord _rightMargin;
/*  42:    */   private TopMarginRecord _topMargin;
/*  43:    */   private BottomMarginRecord _bottomMargin;
/*  44:    */   private final List<PLSAggregate> _plsRecords;
/*  45:    */   private PrintSetupRecord _printSetup;
/*  46:    */   private Record _bitmap;
/*  47:    */   private HeaderFooterRecord _headerFooter;
/*  48:    */   
/*  49:    */   private static final class PLSAggregate
/*  50:    */     extends RecordAggregate
/*  51:    */   {
/*  52: 42 */     private static final ContinueRecord[] EMPTY_CONTINUE_RECORD_ARRAY = new ContinueRecord[0];
/*  53:    */     private final Record _pls;
/*  54:    */     private ContinueRecord[] _plsContinues;
/*  55:    */     
/*  56:    */     public PLSAggregate(RecordStream rs)
/*  57:    */     {
/*  58: 53 */       this._pls = rs.getNext();
/*  59: 54 */       if (rs.peekNextSid() == 60)
/*  60:    */       {
/*  61: 55 */         List<ContinueRecord> temp = new ArrayList();
/*  62: 56 */         while (rs.peekNextSid() == 60) {
/*  63: 57 */           temp.add((ContinueRecord)rs.getNext());
/*  64:    */         }
/*  65: 59 */         this._plsContinues = new ContinueRecord[temp.size()];
/*  66: 60 */         temp.toArray(this._plsContinues);
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70: 62 */         this._plsContinues = EMPTY_CONTINUE_RECORD_ARRAY;
/*  71:    */       }
/*  72:    */     }
/*  73:    */     
/*  74:    */     public void visitContainedRecords(RecordVisitor rv)
/*  75:    */     {
/*  76: 68 */       rv.visitRecord(this._pls);
/*  77: 69 */       for (ContinueRecord _plsContinue : this._plsContinues) {
/*  78: 70 */         rv.visitRecord(_plsContinue);
/*  79:    */       }
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83: 96 */   private final List<HeaderFooterRecord> _sviewHeaderFooters = new ArrayList();
/*  84:    */   private Record _printSize;
/*  85:    */   
/*  86:    */   public PageSettingsBlock(RecordStream rs)
/*  87:    */   {
/*  88:100 */     this._plsRecords = new ArrayList();
/*  89:    */     for (;;)
/*  90:    */     {
/*  91:102 */       if (!readARecord(rs)) {
/*  92:    */         break;
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public PageSettingsBlock()
/*  98:    */   {
/*  99:112 */     this._plsRecords = new ArrayList();
/* 100:113 */     this._rowBreaksRecord = new HorizontalPageBreakRecord();
/* 101:114 */     this._columnBreaksRecord = new VerticalPageBreakRecord();
/* 102:115 */     this._header = new HeaderRecord("");
/* 103:116 */     this._footer = new FooterRecord("");
/* 104:117 */     this._hCenter = createHCenter();
/* 105:118 */     this._vCenter = createVCenter();
/* 106:119 */     this._printSetup = createPrintSetup();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static boolean isComponentRecord(int sid)
/* 110:    */   {
/* 111:129 */     switch (sid)
/* 112:    */     {
/* 113:    */     case 20: 
/* 114:    */     case 21: 
/* 115:    */     case 26: 
/* 116:    */     case 27: 
/* 117:    */     case 38: 
/* 118:    */     case 39: 
/* 119:    */     case 40: 
/* 120:    */     case 41: 
/* 121:    */     case 51: 
/* 122:    */     case 77: 
/* 123:    */     case 131: 
/* 124:    */     case 132: 
/* 125:    */     case 161: 
/* 126:    */     case 233: 
/* 127:    */     case 2204: 
/* 128:145 */       return true;
/* 129:    */     }
/* 130:147 */     return false;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private boolean readARecord(RecordStream rs)
/* 134:    */   {
/* 135:151 */     switch (rs.peekNextSid())
/* 136:    */     {
/* 137:    */     case 27: 
/* 138:153 */       checkNotPresent(this._rowBreaksRecord);
/* 139:154 */       this._rowBreaksRecord = ((PageBreakRecord)rs.getNext());
/* 140:155 */       break;
/* 141:    */     case 26: 
/* 142:157 */       checkNotPresent(this._columnBreaksRecord);
/* 143:158 */       this._columnBreaksRecord = ((PageBreakRecord)rs.getNext());
/* 144:159 */       break;
/* 145:    */     case 20: 
/* 146:161 */       checkNotPresent(this._header);
/* 147:162 */       this._header = ((HeaderRecord)rs.getNext());
/* 148:163 */       break;
/* 149:    */     case 21: 
/* 150:165 */       checkNotPresent(this._footer);
/* 151:166 */       this._footer = ((FooterRecord)rs.getNext());
/* 152:167 */       break;
/* 153:    */     case 131: 
/* 154:169 */       checkNotPresent(this._hCenter);
/* 155:170 */       this._hCenter = ((HCenterRecord)rs.getNext());
/* 156:171 */       break;
/* 157:    */     case 132: 
/* 158:173 */       checkNotPresent(this._vCenter);
/* 159:174 */       this._vCenter = ((VCenterRecord)rs.getNext());
/* 160:175 */       break;
/* 161:    */     case 38: 
/* 162:177 */       checkNotPresent(this._leftMargin);
/* 163:178 */       this._leftMargin = ((LeftMarginRecord)rs.getNext());
/* 164:179 */       break;
/* 165:    */     case 39: 
/* 166:181 */       checkNotPresent(this._rightMargin);
/* 167:182 */       this._rightMargin = ((RightMarginRecord)rs.getNext());
/* 168:183 */       break;
/* 169:    */     case 40: 
/* 170:185 */       checkNotPresent(this._topMargin);
/* 171:186 */       this._topMargin = ((TopMarginRecord)rs.getNext());
/* 172:187 */       break;
/* 173:    */     case 41: 
/* 174:189 */       checkNotPresent(this._bottomMargin);
/* 175:190 */       this._bottomMargin = ((BottomMarginRecord)rs.getNext());
/* 176:191 */       break;
/* 177:    */     case 77: 
/* 178:193 */       this._plsRecords.add(new PLSAggregate(rs));
/* 179:194 */       break;
/* 180:    */     case 161: 
/* 181:196 */       checkNotPresent(this._printSetup);
/* 182:197 */       this._printSetup = ((PrintSetupRecord)rs.getNext());
/* 183:198 */       break;
/* 184:    */     case 233: 
/* 185:200 */       checkNotPresent(this._bitmap);
/* 186:201 */       this._bitmap = rs.getNext();
/* 187:202 */       break;
/* 188:    */     case 51: 
/* 189:204 */       checkNotPresent(this._printSize);
/* 190:205 */       this._printSize = rs.getNext();
/* 191:206 */       break;
/* 192:    */     case 2204: 
/* 193:209 */       HeaderFooterRecord hf = (HeaderFooterRecord)rs.getNext();
/* 194:210 */       if (hf.isCurrentSheet()) {
/* 195:211 */         this._headerFooter = hf;
/* 196:    */       } else {
/* 197:213 */         this._sviewHeaderFooters.add(hf);
/* 198:    */       }
/* 199:215 */       break;
/* 200:    */     default: 
/* 201:218 */       return false;
/* 202:    */     }
/* 203:220 */     return true;
/* 204:    */   }
/* 205:    */   
/* 206:    */   private void checkNotPresent(Record rec)
/* 207:    */   {
/* 208:224 */     if (rec != null) {
/* 209:225 */       throw new RecordFormatException("Duplicate PageSettingsBlock record (sid=0x" + Integer.toHexString(rec.getSid()) + ")");
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   private PageBreakRecord getRowBreaksRecord()
/* 214:    */   {
/* 215:231 */     if (this._rowBreaksRecord == null) {
/* 216:232 */       this._rowBreaksRecord = new HorizontalPageBreakRecord();
/* 217:    */     }
/* 218:234 */     return this._rowBreaksRecord;
/* 219:    */   }
/* 220:    */   
/* 221:    */   private PageBreakRecord getColumnBreaksRecord()
/* 222:    */   {
/* 223:238 */     if (this._columnBreaksRecord == null) {
/* 224:239 */       this._columnBreaksRecord = new VerticalPageBreakRecord();
/* 225:    */     }
/* 226:241 */     return this._columnBreaksRecord;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void setColumnBreak(short column, short fromRow, short toRow)
/* 230:    */   {
/* 231:253 */     getColumnBreaksRecord().addBreak(column, fromRow, toRow);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void removeColumnBreak(int column)
/* 235:    */   {
/* 236:262 */     getColumnBreaksRecord().removeBreak(column);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void visitContainedRecords(RecordVisitor rv)
/* 240:    */   {
/* 241:272 */     visitIfPresent(this._rowBreaksRecord, rv);
/* 242:273 */     visitIfPresent(this._columnBreaksRecord, rv);
/* 243:275 */     if (this._header == null) {
/* 244:276 */       rv.visitRecord(new HeaderRecord(""));
/* 245:    */     } else {
/* 246:278 */       rv.visitRecord(this._header);
/* 247:    */     }
/* 248:280 */     if (this._footer == null) {
/* 249:281 */       rv.visitRecord(new FooterRecord(""));
/* 250:    */     } else {
/* 251:283 */       rv.visitRecord(this._footer);
/* 252:    */     }
/* 253:285 */     visitIfPresent(this._hCenter, rv);
/* 254:286 */     visitIfPresent(this._vCenter, rv);
/* 255:287 */     visitIfPresent(this._leftMargin, rv);
/* 256:288 */     visitIfPresent(this._rightMargin, rv);
/* 257:289 */     visitIfPresent(this._topMargin, rv);
/* 258:290 */     visitIfPresent(this._bottomMargin, rv);
/* 259:291 */     for (RecordAggregate pls : this._plsRecords) {
/* 260:292 */       pls.visitContainedRecords(rv);
/* 261:    */     }
/* 262:294 */     visitIfPresent(this._printSetup, rv);
/* 263:295 */     visitIfPresent(this._printSize, rv);
/* 264:296 */     visitIfPresent(this._headerFooter, rv);
/* 265:297 */     visitIfPresent(this._bitmap, rv);
/* 266:    */   }
/* 267:    */   
/* 268:    */   private static void visitIfPresent(Record r, RecordVisitor rv)
/* 269:    */   {
/* 270:300 */     if (r != null) {
/* 271:301 */       rv.visitRecord(r);
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   private static void visitIfPresent(PageBreakRecord r, RecordVisitor rv)
/* 276:    */   {
/* 277:305 */     if (r != null)
/* 278:    */     {
/* 279:306 */       if (r.isEmpty()) {
/* 280:308 */         return;
/* 281:    */       }
/* 282:310 */       rv.visitRecord(r);
/* 283:    */     }
/* 284:    */   }
/* 285:    */   
/* 286:    */   private static HCenterRecord createHCenter()
/* 287:    */   {
/* 288:318 */     HCenterRecord retval = new HCenterRecord();
/* 289:    */     
/* 290:320 */     retval.setHCenter(false);
/* 291:321 */     return retval;
/* 292:    */   }
/* 293:    */   
/* 294:    */   private static VCenterRecord createVCenter()
/* 295:    */   {
/* 296:328 */     VCenterRecord retval = new VCenterRecord();
/* 297:    */     
/* 298:330 */     retval.setVCenter(false);
/* 299:331 */     return retval;
/* 300:    */   }
/* 301:    */   
/* 302:    */   private static PrintSetupRecord createPrintSetup()
/* 303:    */   {
/* 304:341 */     PrintSetupRecord retval = new PrintSetupRecord();
/* 305:    */     
/* 306:343 */     retval.setPaperSize((short)1);
/* 307:344 */     retval.setScale((short)100);
/* 308:345 */     retval.setPageStart((short)1);
/* 309:346 */     retval.setFitWidth((short)1);
/* 310:347 */     retval.setFitHeight((short)1);
/* 311:348 */     retval.setOptions((short)2);
/* 312:349 */     retval.setHResolution((short)300);
/* 313:350 */     retval.setVResolution((short)300);
/* 314:351 */     retval.setHeaderMargin(0.5D);
/* 315:352 */     retval.setFooterMargin(0.5D);
/* 316:353 */     retval.setCopies((short)1);
/* 317:354 */     return retval;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public HeaderRecord getHeader()
/* 321:    */   {
/* 322:364 */     return this._header;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public void setHeader(HeaderRecord newHeader)
/* 326:    */   {
/* 327:373 */     this._header = newHeader;
/* 328:    */   }
/* 329:    */   
/* 330:    */   public FooterRecord getFooter()
/* 331:    */   {
/* 332:382 */     return this._footer;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void setFooter(FooterRecord newFooter)
/* 336:    */   {
/* 337:391 */     this._footer = newFooter;
/* 338:    */   }
/* 339:    */   
/* 340:    */   public PrintSetupRecord getPrintSetup()
/* 341:    */   {
/* 342:400 */     return this._printSetup;
/* 343:    */   }
/* 344:    */   
/* 345:    */   public void setPrintSetup(PrintSetupRecord newPrintSetup)
/* 346:    */   {
/* 347:409 */     this._printSetup = newPrintSetup;
/* 348:    */   }
/* 349:    */   
/* 350:    */   private Margin getMarginRec(int marginIndex)
/* 351:    */   {
/* 352:414 */     switch (marginIndex)
/* 353:    */     {
/* 354:    */     case 0: 
/* 355:415 */       return this._leftMargin;
/* 356:    */     case 1: 
/* 357:416 */       return this._rightMargin;
/* 358:    */     case 2: 
/* 359:417 */       return this._topMargin;
/* 360:    */     case 3: 
/* 361:418 */       return this._bottomMargin;
/* 362:    */     }
/* 363:420 */     throw new IllegalArgumentException("Unknown margin constant:  " + marginIndex);
/* 364:    */   }
/* 365:    */   
/* 366:    */   public double getMargin(short margin)
/* 367:    */   {
/* 368:430 */     Margin m = getMarginRec(margin);
/* 369:431 */     if (m != null) {
/* 370:432 */       return m.getMargin();
/* 371:    */     }
/* 372:434 */     switch (margin)
/* 373:    */     {
/* 374:    */     case 0: 
/* 375:435 */       return 0.75D;
/* 376:    */     case 1: 
/* 377:436 */       return 0.75D;
/* 378:    */     case 2: 
/* 379:437 */       return 1.0D;
/* 380:    */     case 3: 
/* 381:438 */       return 1.0D;
/* 382:    */     }
/* 383:440 */     throw new IllegalArgumentException("Unknown margin constant:  " + margin);
/* 384:    */   }
/* 385:    */   
/* 386:    */   public void setMargin(short margin, double size)
/* 387:    */   {
/* 388:449 */     Margin m = getMarginRec(margin);
/* 389:450 */     if (m == null) {
/* 390:451 */       switch (margin)
/* 391:    */       {
/* 392:    */       case 0: 
/* 393:453 */         this._leftMargin = new LeftMarginRecord();
/* 394:454 */         m = this._leftMargin;
/* 395:455 */         break;
/* 396:    */       case 1: 
/* 397:457 */         this._rightMargin = new RightMarginRecord();
/* 398:458 */         m = this._rightMargin;
/* 399:459 */         break;
/* 400:    */       case 2: 
/* 401:461 */         this._topMargin = new TopMarginRecord();
/* 402:462 */         m = this._topMargin;
/* 403:463 */         break;
/* 404:    */       case 3: 
/* 405:465 */         this._bottomMargin = new BottomMarginRecord();
/* 406:466 */         m = this._bottomMargin;
/* 407:467 */         break;
/* 408:    */       default: 
/* 409:469 */         throw new IllegalArgumentException("Unknown margin constant:  " + margin);
/* 410:    */       }
/* 411:    */     }
/* 412:472 */     m.setMargin(size);
/* 413:    */   }
/* 414:    */   
/* 415:    */   private static void shiftBreaks(PageBreakRecord breaks, int start, int stop, int count)
/* 416:    */   {
/* 417:484 */     Iterator<PageBreakRecord.Break> iterator = breaks.getBreaksIterator();
/* 418:485 */     List<PageBreakRecord.Break> shiftedBreak = new ArrayList();
/* 419:486 */     while (iterator.hasNext())
/* 420:    */     {
/* 421:488 */       PageBreakRecord.Break breakItem = (PageBreakRecord.Break)iterator.next();
/* 422:489 */       int breakLocation = breakItem.main;
/* 423:490 */       boolean inStart = breakLocation >= start;
/* 424:491 */       boolean inEnd = breakLocation <= stop;
/* 425:492 */       if ((inStart) && (inEnd)) {
/* 426:493 */         shiftedBreak.add(breakItem);
/* 427:    */       }
/* 428:    */     }
/* 429:497 */     iterator = shiftedBreak.iterator();
/* 430:498 */     while (iterator.hasNext())
/* 431:    */     {
/* 432:499 */       PageBreakRecord.Break breakItem = (PageBreakRecord.Break)iterator.next();
/* 433:500 */       breaks.removeBreak(breakItem.main);
/* 434:501 */       breaks.addBreak((short)(breakItem.main + count), breakItem.subFrom, breakItem.subTo);
/* 435:    */     }
/* 436:    */   }
/* 437:    */   
/* 438:    */   public void setRowBreak(int row, short fromCol, short toCol)
/* 439:    */   {
/* 440:513 */     getRowBreaksRecord().addBreak((short)row, fromCol, toCol);
/* 441:    */   }
/* 442:    */   
/* 443:    */   public void removeRowBreak(int row)
/* 444:    */   {
/* 445:521 */     if (getRowBreaksRecord().getBreaks().length < 1) {
/* 446:522 */       throw new IllegalArgumentException("Sheet does not define any row breaks");
/* 447:    */     }
/* 448:524 */     getRowBreaksRecord().removeBreak((short)row);
/* 449:    */   }
/* 450:    */   
/* 451:    */   public boolean isRowBroken(int row)
/* 452:    */   {
/* 453:535 */     return getRowBreaksRecord().getBreak(row) != null;
/* 454:    */   }
/* 455:    */   
/* 456:    */   public boolean isColumnBroken(int column)
/* 457:    */   {
/* 458:547 */     return getColumnBreaksRecord().getBreak(column) != null;
/* 459:    */   }
/* 460:    */   
/* 461:    */   public void shiftRowBreaks(int startingRow, int endingRow, int count)
/* 462:    */   {
/* 463:557 */     shiftBreaks(getRowBreaksRecord(), startingRow, endingRow, count);
/* 464:    */   }
/* 465:    */   
/* 466:    */   public void shiftColumnBreaks(short startingCol, short endingCol, short count)
/* 467:    */   {
/* 468:567 */     shiftBreaks(getColumnBreaksRecord(), startingCol, endingCol, count);
/* 469:    */   }
/* 470:    */   
/* 471:    */   public int[] getRowBreaks()
/* 472:    */   {
/* 473:574 */     return getRowBreaksRecord().getBreaks();
/* 474:    */   }
/* 475:    */   
/* 476:    */   public int getNumRowBreaks()
/* 477:    */   {
/* 478:581 */     return getRowBreaksRecord().getNumBreaks();
/* 479:    */   }
/* 480:    */   
/* 481:    */   public int[] getColumnBreaks()
/* 482:    */   {
/* 483:588 */     return getColumnBreaksRecord().getBreaks();
/* 484:    */   }
/* 485:    */   
/* 486:    */   public int getNumColumnBreaks()
/* 487:    */   {
/* 488:595 */     return getColumnBreaksRecord().getNumBreaks();
/* 489:    */   }
/* 490:    */   
/* 491:    */   public VCenterRecord getVCenter()
/* 492:    */   {
/* 493:599 */     return this._vCenter;
/* 494:    */   }
/* 495:    */   
/* 496:    */   public HCenterRecord getHCenter()
/* 497:    */   {
/* 498:603 */     return this._hCenter;
/* 499:    */   }
/* 500:    */   
/* 501:    */   public void addLateHeaderFooter(HeaderFooterRecord rec)
/* 502:    */   {
/* 503:613 */     if (this._headerFooter != null) {
/* 504:614 */       throw new IllegalStateException("This page settings block already has a header/footer record");
/* 505:    */     }
/* 506:616 */     if (rec.getSid() != 2204) {
/* 507:617 */       throw new RecordFormatException("Unexpected header-footer record sid: 0x" + Integer.toHexString(rec.getSid()));
/* 508:    */     }
/* 509:619 */     this._headerFooter = rec;
/* 510:    */   }
/* 511:    */   
/* 512:    */   public void addLateRecords(RecordStream rs)
/* 513:    */   {
/* 514:    */     for (;;)
/* 515:    */     {
/* 516:655 */       if (!readARecord(rs)) {
/* 517:    */         break;
/* 518:    */       }
/* 519:    */     }
/* 520:    */   }
/* 521:    */   
/* 522:    */   public void positionRecords(List<RecordBase> sheetRecords)
/* 523:    */   {
/* 524:675 */     List<HeaderFooterRecord> hfRecordsToIterate = new ArrayList(this._sviewHeaderFooters);
/* 525:    */     
/* 526:677 */     final Map<String, HeaderFooterRecord> hfGuidMap = new HashMap();
/* 527:679 */     for (HeaderFooterRecord hf : hfRecordsToIterate) {
/* 528:680 */       hfGuidMap.put(HexDump.toHex(hf.getGuid()), hf);
/* 529:    */     }
/* 530:685 */     for (RecordBase rb : sheetRecords) {
/* 531:686 */       if ((rb instanceof CustomViewSettingsRecordAggregate))
/* 532:    */       {
/* 533:687 */         final CustomViewSettingsRecordAggregate cv = (CustomViewSettingsRecordAggregate)rb;
/* 534:688 */         cv.visitContainedRecords(new RecordVisitor()
/* 535:    */         {
/* 536:    */           public void visitRecord(Record r)
/* 537:    */           {
/* 538:691 */             if (r.getSid() == 426)
/* 539:    */             {
/* 540:692 */               String guid = HexDump.toHex(((UserSViewBegin)r).getGuid());
/* 541:693 */               HeaderFooterRecord hf = (HeaderFooterRecord)hfGuidMap.get(guid);
/* 542:695 */               if (hf != null)
/* 543:    */               {
/* 544:696 */                 cv.append(hf);
/* 545:697 */                 PageSettingsBlock.this._sviewHeaderFooters.remove(hf);
/* 546:    */               }
/* 547:    */             }
/* 548:    */           }
/* 549:    */         });
/* 550:    */       }
/* 551:    */     }
/* 552:    */   }
/* 553:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.PageSettingsBlock

 * JD-Core Version:    0.7.0.1

 */