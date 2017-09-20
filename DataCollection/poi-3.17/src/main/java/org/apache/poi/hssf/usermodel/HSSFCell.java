/*    1:     */ package org.apache.poi.hssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.text.SimpleDateFormat;
/*    4:     */ import java.util.Calendar;
/*    5:     */ import java.util.Date;
/*    6:     */ import java.util.Iterator;
/*    7:     */ import java.util.List;
/*    8:     */ import org.apache.poi.common.usermodel.HyperlinkType;
/*    9:     */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   10:     */ import org.apache.poi.hssf.model.InternalSheet;
/*   11:     */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   12:     */ import org.apache.poi.hssf.record.BlankRecord;
/*   13:     */ import org.apache.poi.hssf.record.BoolErrRecord;
/*   14:     */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   15:     */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   16:     */ import org.apache.poi.hssf.record.FormulaRecord;
/*   17:     */ import org.apache.poi.hssf.record.HyperlinkRecord;
/*   18:     */ import org.apache.poi.hssf.record.LabelSSTRecord;
/*   19:     */ import org.apache.poi.hssf.record.NumberRecord;
/*   20:     */ import org.apache.poi.hssf.record.Record;
/*   21:     */ import org.apache.poi.hssf.record.RecordBase;
/*   22:     */ import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
/*   23:     */ import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate;
/*   24:     */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   25:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   26:     */ import org.apache.poi.ss.formula.FormulaType;
/*   27:     */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   28:     */ import org.apache.poi.ss.formula.ptg.ExpPtg;
/*   29:     */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   30:     */ import org.apache.poi.ss.usermodel.Cell;
/*   31:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   32:     */ import org.apache.poi.ss.usermodel.CellType;
/*   33:     */ import org.apache.poi.ss.usermodel.Comment;
/*   34:     */ import org.apache.poi.ss.usermodel.FormulaError;
/*   35:     */ import org.apache.poi.ss.usermodel.Hyperlink;
/*   36:     */ import org.apache.poi.ss.usermodel.RichTextString;
/*   37:     */ import org.apache.poi.ss.util.CellAddress;
/*   38:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   39:     */ import org.apache.poi.ss.util.CellReference;
/*   40:     */ import org.apache.poi.ss.util.NumberToTextConverter;
/*   41:     */ import org.apache.poi.util.LocaleUtil;
/*   42:     */ 
/*   43:     */ public class HSSFCell
/*   44:     */   implements Cell
/*   45:     */ {
/*   46:     */   private static final String FILE_FORMAT_NAME = "BIFF8";
/*   47:  76 */   public static final int LAST_COLUMN_NUMBER = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
/*   48:  77 */   private static final String LAST_COLUMN_NAME = SpreadsheetVersion.EXCEL97.getLastColumnName();
/*   49:     */   public static final short ENCODING_UNCHANGED = -1;
/*   50:     */   public static final short ENCODING_COMPRESSED_UNICODE = 0;
/*   51:     */   public static final short ENCODING_UTF_16 = 1;
/*   52:     */   private final HSSFWorkbook _book;
/*   53:     */   private final HSSFSheet _sheet;
/*   54:     */   private CellType _cellType;
/*   55:     */   private HSSFRichTextString _stringValue;
/*   56:     */   private CellValueRecordInterface _record;
/*   57:     */   private HSSFComment _comment;
/*   58:     */   
/*   59:     */   protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col)
/*   60:     */   {
/*   61: 108 */     checkBounds(col);
/*   62: 109 */     this._stringValue = null;
/*   63: 110 */     this._book = book;
/*   64: 111 */     this._sheet = sheet;
/*   65:     */     
/*   66:     */ 
/*   67:     */ 
/*   68:     */ 
/*   69: 116 */     short xfindex = sheet.getSheet().getXFIndexForColAt(col);
/*   70: 117 */     setCellType(CellType.BLANK, false, row, col, xfindex);
/*   71:     */   }
/*   72:     */   
/*   73:     */   public HSSFSheet getSheet()
/*   74:     */   {
/*   75: 126 */     return this._sheet;
/*   76:     */   }
/*   77:     */   
/*   78:     */   public HSSFRow getRow()
/*   79:     */   {
/*   80: 135 */     int rowIndex = getRowIndex();
/*   81: 136 */     return this._sheet.getRow(rowIndex);
/*   82:     */   }
/*   83:     */   
/*   84:     */   protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col, CellType type)
/*   85:     */   {
/*   86: 153 */     checkBounds(col);
/*   87: 154 */     this._cellType = CellType._NONE;
/*   88: 155 */     this._stringValue = null;
/*   89: 156 */     this._book = book;
/*   90: 157 */     this._sheet = sheet;
/*   91:     */     
/*   92: 159 */     short xfindex = sheet.getSheet().getXFIndexForColAt(col);
/*   93: 160 */     setCellType(type, false, row, col, xfindex);
/*   94:     */   }
/*   95:     */   
/*   96:     */   protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, CellValueRecordInterface cval)
/*   97:     */   {
/*   98: 172 */     this._record = cval;
/*   99: 173 */     this._cellType = determineType(cval);
/*  100: 174 */     this._stringValue = null;
/*  101: 175 */     this._book = book;
/*  102: 176 */     this._sheet = sheet;
/*  103: 177 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  104:     */     {
/*  105:     */     case 1: 
/*  106: 180 */       this._stringValue = new HSSFRichTextString(book.getWorkbook(), (LabelSSTRecord)cval);
/*  107: 181 */       break;
/*  108:     */     case 2: 
/*  109:     */       break;
/*  110:     */     case 3: 
/*  111: 187 */       this._stringValue = new HSSFRichTextString(((FormulaRecordAggregate)cval).getStringValue());
/*  112: 188 */       break;
/*  113:     */     }
/*  114:     */   }
/*  115:     */   
/*  116:     */   private static CellType determineType(CellValueRecordInterface cval)
/*  117:     */   {
/*  118: 200 */     if ((cval instanceof FormulaRecordAggregate)) {
/*  119: 201 */       return CellType.FORMULA;
/*  120:     */     }
/*  121: 204 */     Record record = (Record)cval;
/*  122: 205 */     switch (record.getSid())
/*  123:     */     {
/*  124:     */     case 515: 
/*  125: 207 */       return CellType.NUMERIC;
/*  126:     */     case 513: 
/*  127: 208 */       return CellType.BLANK;
/*  128:     */     case 253: 
/*  129: 209 */       return CellType.STRING;
/*  130:     */     case 517: 
/*  131: 211 */       BoolErrRecord boolErrRecord = (BoolErrRecord)record;
/*  132:     */       
/*  133: 213 */       return boolErrRecord.isBoolean() ? CellType.BOOLEAN : CellType.ERROR;
/*  134:     */     }
/*  135: 217 */     throw new RuntimeException("Bad cell value rec (" + cval.getClass().getName() + ")");
/*  136:     */   }
/*  137:     */   
/*  138:     */   protected InternalWorkbook getBoundWorkbook()
/*  139:     */   {
/*  140: 224 */     return this._book.getWorkbook();
/*  141:     */   }
/*  142:     */   
/*  143:     */   public int getRowIndex()
/*  144:     */   {
/*  145: 232 */     return this._record.getRow();
/*  146:     */   }
/*  147:     */   
/*  148:     */   protected void updateCellNum(short num)
/*  149:     */   {
/*  150: 242 */     this._record.setColumn(num);
/*  151:     */   }
/*  152:     */   
/*  153:     */   public int getColumnIndex()
/*  154:     */   {
/*  155: 247 */     return this._record.getColumn() & 0xFFFF;
/*  156:     */   }
/*  157:     */   
/*  158:     */   public CellAddress getAddress()
/*  159:     */   {
/*  160: 255 */     return new CellAddress(this);
/*  161:     */   }
/*  162:     */   
/*  163:     */   /**
/*  164:     */    * @deprecated
/*  165:     */    */
/*  166:     */   public void setCellType(int cellType)
/*  167:     */   {
/*  168: 272 */     setCellType(CellType.forInt(cellType));
/*  169:     */   }
/*  170:     */   
/*  171:     */   public void setCellType(CellType cellType)
/*  172:     */   {
/*  173: 281 */     notifyFormulaChanging();
/*  174: 282 */     if (isPartOfArrayFormulaGroup()) {
/*  175: 283 */       notifyArrayFormulaChanging();
/*  176:     */     }
/*  177: 285 */     int row = this._record.getRow();
/*  178: 286 */     short col = this._record.getColumn();
/*  179: 287 */     short styleIndex = this._record.getXFIndex();
/*  180: 288 */     setCellType(cellType, true, row, col, styleIndex);
/*  181:     */   }
/*  182:     */   
/*  183:     */   private void setCellType(CellType cellType, boolean setValue, int row, short col, short styleIndex)
/*  184:     */   {
/*  185: 302 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  186:     */     {
/*  187:     */     case 3: 
/*  188:     */       FormulaRecordAggregate frec;
/*  189:     */       FormulaRecordAggregate frec;
/*  190: 308 */       if (cellType != this._cellType)
/*  191:     */       {
/*  192: 309 */         frec = this._sheet.getSheet().getRowsAggregate().createFormula(row, col);
/*  193:     */       }
/*  194:     */       else
/*  195:     */       {
/*  196: 311 */         frec = (FormulaRecordAggregate)this._record;
/*  197: 312 */         frec.setRow(row);
/*  198: 313 */         frec.setColumn(col);
/*  199:     */       }
/*  200: 315 */       if (setValue) {
/*  201: 317 */         frec.getFormulaRecord().setValue(getNumericCellValue());
/*  202:     */       }
/*  203: 319 */       frec.setXFIndex(styleIndex);
/*  204: 320 */       this._record = frec;
/*  205: 321 */       break;
/*  206:     */     case 4: 
/*  207: 324 */       NumberRecord nrec = null;
/*  208: 326 */       if (cellType != this._cellType) {
/*  209: 328 */         nrec = new NumberRecord();
/*  210:     */       } else {
/*  211: 332 */         nrec = (NumberRecord)this._record;
/*  212:     */       }
/*  213: 334 */       nrec.setColumn(col);
/*  214: 335 */       if (setValue) {
/*  215: 337 */         nrec.setValue(getNumericCellValue());
/*  216:     */       }
/*  217: 339 */       nrec.setXFIndex(styleIndex);
/*  218: 340 */       nrec.setRow(row);
/*  219: 341 */       this._record = nrec;
/*  220: 342 */       break;
/*  221:     */     case 1: 
/*  222:     */       LabelSSTRecord lrec;
/*  223:     */       LabelSSTRecord lrec;
/*  224: 347 */       if (cellType == this._cellType)
/*  225:     */       {
/*  226: 348 */         lrec = (LabelSSTRecord)this._record;
/*  227:     */       }
/*  228:     */       else
/*  229:     */       {
/*  230: 350 */         lrec = new LabelSSTRecord();
/*  231: 351 */         lrec.setColumn(col);
/*  232: 352 */         lrec.setRow(row);
/*  233: 353 */         lrec.setXFIndex(styleIndex);
/*  234:     */       }
/*  235: 355 */       if (setValue)
/*  236:     */       {
/*  237: 356 */         String str = convertCellValueToString();
/*  238: 357 */         if (str == null)
/*  239:     */         {
/*  240: 360 */           setCellType(CellType.BLANK, false, row, col, styleIndex);
/*  241: 361 */           return;
/*  242:     */         }
/*  243: 363 */         int sstIndex = this._book.getWorkbook().addSSTString(new UnicodeString(str));
/*  244: 364 */         lrec.setSSTIndex(sstIndex);
/*  245: 365 */         UnicodeString us = this._book.getWorkbook().getSSTString(sstIndex);
/*  246: 366 */         this._stringValue = new HSSFRichTextString();
/*  247: 367 */         this._stringValue.setUnicodeString(us);
/*  248:     */       }
/*  249: 370 */       this._record = lrec;
/*  250: 371 */       break;
/*  251:     */     case 2: 
/*  252: 374 */       BlankRecord brec = null;
/*  253: 376 */       if (cellType != this._cellType) {
/*  254: 378 */         brec = new BlankRecord();
/*  255:     */       } else {
/*  256: 382 */         brec = (BlankRecord)this._record;
/*  257:     */       }
/*  258: 384 */       brec.setColumn(col);
/*  259:     */       
/*  260:     */ 
/*  261: 387 */       brec.setXFIndex(styleIndex);
/*  262: 388 */       brec.setRow(row);
/*  263: 389 */       this._record = brec;
/*  264: 390 */       break;
/*  265:     */     case 5: 
/*  266: 393 */       BoolErrRecord boolRec = null;
/*  267: 395 */       if (cellType != this._cellType) {
/*  268: 397 */         boolRec = new BoolErrRecord();
/*  269:     */       } else {
/*  270: 401 */         boolRec = (BoolErrRecord)this._record;
/*  271:     */       }
/*  272: 403 */       boolRec.setColumn(col);
/*  273: 404 */       if (setValue) {
/*  274: 406 */         boolRec.setValue(convertCellValueToBoolean());
/*  275:     */       }
/*  276: 408 */       boolRec.setXFIndex(styleIndex);
/*  277: 409 */       boolRec.setRow(row);
/*  278: 410 */       this._record = boolRec;
/*  279: 411 */       break;
/*  280:     */     case 6: 
/*  281: 414 */       BoolErrRecord errRec = null;
/*  282: 416 */       if (cellType != this._cellType) {
/*  283: 418 */         errRec = new BoolErrRecord();
/*  284:     */       } else {
/*  285: 422 */         errRec = (BoolErrRecord)this._record;
/*  286:     */       }
/*  287: 424 */       errRec.setColumn(col);
/*  288: 425 */       if (setValue) {
/*  289: 427 */         errRec.setValue(FormulaError.VALUE.getCode());
/*  290:     */       }
/*  291: 429 */       errRec.setXFIndex(styleIndex);
/*  292: 430 */       errRec.setRow(row);
/*  293: 431 */       this._record = errRec;
/*  294: 432 */       break;
/*  295:     */     default: 
/*  296: 434 */       throw new IllegalStateException("Invalid cell type: " + cellType);
/*  297:     */     }
/*  298: 436 */     if ((cellType != this._cellType) && (this._cellType != CellType._NONE)) {
/*  299: 439 */       this._sheet.getSheet().replaceValueRecord(this._record);
/*  300:     */     }
/*  301: 441 */     this._cellType = cellType;
/*  302:     */   }
/*  303:     */   
/*  304:     */   /**
/*  305:     */    * @deprecated
/*  306:     */    */
/*  307:     */   public int getCellType()
/*  308:     */   {
/*  309: 454 */     return getCellTypeEnum().getCode();
/*  310:     */   }
/*  311:     */   
/*  312:     */   public CellType getCellTypeEnum()
/*  313:     */   {
/*  314: 465 */     return this._cellType;
/*  315:     */   }
/*  316:     */   
/*  317:     */   public void setCellValue(double value)
/*  318:     */   {
/*  319: 478 */     if (Double.isInfinite(value))
/*  320:     */     {
/*  321: 481 */       setCellErrorValue(FormulaError.DIV0.getCode());
/*  322:     */     }
/*  323: 482 */     else if (Double.isNaN(value))
/*  324:     */     {
/*  325: 485 */       setCellErrorValue(FormulaError.NUM.getCode());
/*  326:     */     }
/*  327:     */     else
/*  328:     */     {
/*  329: 487 */       int row = this._record.getRow();
/*  330: 488 */       short col = this._record.getColumn();
/*  331: 489 */       short styleIndex = this._record.getXFIndex();
/*  332: 491 */       switch (this._cellType)
/*  333:     */       {
/*  334:     */       default: 
/*  335: 493 */         setCellType(CellType.NUMERIC, false, row, col, styleIndex);
/*  336:     */       case NUMERIC: 
/*  337: 496 */         ((NumberRecord)this._record).setValue(value);
/*  338: 497 */         break;
/*  339:     */       case FORMULA: 
/*  340: 499 */         ((FormulaRecordAggregate)this._record).setCachedDoubleResult(value);
/*  341:     */       }
/*  342:     */     }
/*  343:     */   }
/*  344:     */   
/*  345:     */   public void setCellValue(Date value)
/*  346:     */   {
/*  347: 516 */     setCellValue(HSSFDateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
/*  348:     */   }
/*  349:     */   
/*  350:     */   public void setCellValue(Calendar value)
/*  351:     */   {
/*  352: 536 */     setCellValue(HSSFDateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
/*  353:     */   }
/*  354:     */   
/*  355:     */   public void setCellValue(String value)
/*  356:     */   {
/*  357: 548 */     HSSFRichTextString str = value == null ? null : new HSSFRichTextString(value);
/*  358: 549 */     setCellValue(str);
/*  359:     */   }
/*  360:     */   
/*  361:     */   public void setCellValue(RichTextString value)
/*  362:     */   {
/*  363: 563 */     int row = this._record.getRow();
/*  364: 564 */     short col = this._record.getColumn();
/*  365: 565 */     short styleIndex = this._record.getXFIndex();
/*  366: 566 */     if (value == null)
/*  367:     */     {
/*  368: 568 */       notifyFormulaChanging();
/*  369: 569 */       setCellType(CellType.BLANK, false, row, col, styleIndex);
/*  370: 570 */       return;
/*  371:     */     }
/*  372: 573 */     if (value.length() > SpreadsheetVersion.EXCEL97.getMaxTextLength()) {
/*  373: 574 */       throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
/*  374:     */     }
/*  375: 577 */     if (this._cellType == CellType.FORMULA)
/*  376:     */     {
/*  377: 580 */       FormulaRecordAggregate fr = (FormulaRecordAggregate)this._record;
/*  378: 581 */       fr.setCachedStringResult(value.getString());
/*  379:     */       
/*  380: 583 */       this._stringValue = new HSSFRichTextString(value.getString());
/*  381:     */       
/*  382:     */ 
/*  383: 586 */       return;
/*  384:     */     }
/*  385: 592 */     if (this._cellType != CellType.STRING) {
/*  386: 593 */       setCellType(CellType.STRING, false, row, col, styleIndex);
/*  387:     */     }
/*  388: 595 */     int index = 0;
/*  389:     */     
/*  390: 597 */     HSSFRichTextString hvalue = (HSSFRichTextString)value;
/*  391: 598 */     UnicodeString str = hvalue.getUnicodeString();
/*  392: 599 */     index = this._book.getWorkbook().addSSTString(str);
/*  393: 600 */     ((LabelSSTRecord)this._record).setSSTIndex(index);
/*  394: 601 */     this._stringValue = hvalue;
/*  395: 602 */     this._stringValue.setWorkbookReferences(this._book.getWorkbook(), (LabelSSTRecord)this._record);
/*  396: 603 */     this._stringValue.setUnicodeString(this._book.getWorkbook().getSSTString(index));
/*  397:     */   }
/*  398:     */   
/*  399:     */   public void setCellFormula(String formula)
/*  400:     */   {
/*  401: 607 */     if (isPartOfArrayFormulaGroup()) {
/*  402: 608 */       notifyArrayFormulaChanging();
/*  403:     */     }
/*  404: 611 */     int row = this._record.getRow();
/*  405: 612 */     short col = this._record.getColumn();
/*  406: 613 */     short styleIndex = this._record.getXFIndex();
/*  407: 615 */     if (formula == null)
/*  408:     */     {
/*  409: 616 */       notifyFormulaChanging();
/*  410: 617 */       setCellType(CellType.BLANK, false, row, col, styleIndex);
/*  411: 618 */       return;
/*  412:     */     }
/*  413: 620 */     int sheetIndex = this._book.getSheetIndex(this._sheet);
/*  414: 621 */     Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._book, FormulaType.CELL, sheetIndex);
/*  415: 622 */     setCellType(CellType.FORMULA, false, row, col, styleIndex);
/*  416: 623 */     FormulaRecordAggregate agg = (FormulaRecordAggregate)this._record;
/*  417: 624 */     FormulaRecord frec = agg.getFormulaRecord();
/*  418: 625 */     frec.setOptions((short)2);
/*  419: 626 */     frec.setValue(0.0D);
/*  420: 629 */     if (agg.getXFIndex() == 0) {
/*  421: 630 */       agg.setXFIndex((short)15);
/*  422:     */     }
/*  423: 632 */     agg.setParsedExpression(ptgs);
/*  424:     */   }
/*  425:     */   
/*  426:     */   private void notifyFormulaChanging()
/*  427:     */   {
/*  428: 639 */     if ((this._record instanceof FormulaRecordAggregate)) {
/*  429: 640 */       ((FormulaRecordAggregate)this._record).notifyFormulaChanging();
/*  430:     */     }
/*  431:     */   }
/*  432:     */   
/*  433:     */   public String getCellFormula()
/*  434:     */   {
/*  435: 645 */     if (!(this._record instanceof FormulaRecordAggregate)) {
/*  436: 646 */       throw typeMismatch(CellType.FORMULA, this._cellType, true);
/*  437:     */     }
/*  438: 648 */     return HSSFFormulaParser.toFormulaString(this._book, ((FormulaRecordAggregate)this._record).getFormulaTokens());
/*  439:     */   }
/*  440:     */   
/*  441:     */   private static RuntimeException typeMismatch(CellType expectedTypeCode, CellType actualTypeCode, boolean isFormulaCell)
/*  442:     */   {
/*  443: 652 */     String msg = "Cannot get a " + expectedTypeCode + " value from a " + actualTypeCode + " " + (isFormulaCell ? "formula " : "") + "cell";
/*  444:     */     
/*  445: 654 */     return new IllegalStateException(msg);
/*  446:     */   }
/*  447:     */   
/*  448:     */   private static void checkFormulaCachedValueType(CellType expectedTypeCode, FormulaRecord fr)
/*  449:     */   {
/*  450: 657 */     CellType cachedValueType = CellType.forInt(fr.getCachedResultType());
/*  451: 658 */     if (cachedValueType != expectedTypeCode) {
/*  452: 659 */       throw typeMismatch(expectedTypeCode, cachedValueType, true);
/*  453:     */     }
/*  454:     */   }
/*  455:     */   
/*  456:     */   public double getNumericCellValue()
/*  457:     */   {
/*  458: 673 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  459:     */     {
/*  460:     */     case 2: 
/*  461: 675 */       return 0.0D;
/*  462:     */     case 4: 
/*  463: 677 */       return ((NumberRecord)this._record).getValue();
/*  464:     */     default: 
/*  465: 679 */       throw typeMismatch(CellType.NUMERIC, this._cellType, false);
/*  466:     */     }
/*  467: 683 */     FormulaRecord fr = ((FormulaRecordAggregate)this._record).getFormulaRecord();
/*  468: 684 */     checkFormulaCachedValueType(CellType.NUMERIC, fr);
/*  469: 685 */     return fr.getValue();
/*  470:     */   }
/*  471:     */   
/*  472:     */   public Date getDateCellValue()
/*  473:     */   {
/*  474: 697 */     if (this._cellType == CellType.BLANK) {
/*  475: 698 */       return null;
/*  476:     */     }
/*  477: 700 */     double value = getNumericCellValue();
/*  478: 701 */     if (this._book.getWorkbook().isUsing1904DateWindowing()) {
/*  479: 702 */       return HSSFDateUtil.getJavaDate(value, true);
/*  480:     */     }
/*  481: 704 */     return HSSFDateUtil.getJavaDate(value, false);
/*  482:     */   }
/*  483:     */   
/*  484:     */   public String getStringCellValue()
/*  485:     */   {
/*  486: 714 */     HSSFRichTextString str = getRichStringCellValue();
/*  487: 715 */     return str.getString();
/*  488:     */   }
/*  489:     */   
/*  490:     */   public HSSFRichTextString getRichStringCellValue()
/*  491:     */   {
/*  492: 725 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  493:     */     {
/*  494:     */     case 2: 
/*  495: 727 */       return new HSSFRichTextString("");
/*  496:     */     case 1: 
/*  497: 729 */       return this._stringValue;
/*  498:     */     default: 
/*  499: 731 */       throw typeMismatch(CellType.STRING, this._cellType, false);
/*  500:     */     }
/*  501: 735 */     FormulaRecordAggregate fra = (FormulaRecordAggregate)this._record;
/*  502: 736 */     checkFormulaCachedValueType(CellType.STRING, fra.getFormulaRecord());
/*  503: 737 */     String strVal = fra.getStringValue();
/*  504: 738 */     return new HSSFRichTextString(strVal == null ? "" : strVal);
/*  505:     */   }
/*  506:     */   
/*  507:     */   public void setCellValue(boolean value)
/*  508:     */   {
/*  509: 750 */     int row = this._record.getRow();
/*  510: 751 */     short col = this._record.getColumn();
/*  511: 752 */     short styleIndex = this._record.getXFIndex();
/*  512: 754 */     switch (this._cellType)
/*  513:     */     {
/*  514:     */     default: 
/*  515: 756 */       setCellType(CellType.BOOLEAN, false, row, col, styleIndex);
/*  516:     */     case BOOLEAN: 
/*  517: 759 */       ((BoolErrRecord)this._record).setValue(value);
/*  518: 760 */       break;
/*  519:     */     case FORMULA: 
/*  520: 762 */       ((FormulaRecordAggregate)this._record).setCachedBooleanResult(value);
/*  521:     */     }
/*  522:     */   }
/*  523:     */   
/*  524:     */   /**
/*  525:     */    * @deprecated
/*  526:     */    */
/*  527:     */   public void setCellErrorValue(byte errorCode)
/*  528:     */   {
/*  529: 778 */     FormulaError error = FormulaError.forInt(errorCode);
/*  530: 779 */     setCellErrorValue(error);
/*  531:     */   }
/*  532:     */   
/*  533:     */   public void setCellErrorValue(FormulaError error)
/*  534:     */   {
/*  535: 791 */     int row = this._record.getRow();
/*  536: 792 */     short col = this._record.getColumn();
/*  537: 793 */     short styleIndex = this._record.getXFIndex();
/*  538: 794 */     switch (this._cellType)
/*  539:     */     {
/*  540:     */     default: 
/*  541: 796 */       setCellType(CellType.ERROR, false, row, col, styleIndex);
/*  542:     */     case ERROR: 
/*  543: 799 */       ((BoolErrRecord)this._record).setValue(error);
/*  544: 800 */       break;
/*  545:     */     case FORMULA: 
/*  546: 802 */       ((FormulaRecordAggregate)this._record).setCachedErrorResult(error.getCode());
/*  547:     */     }
/*  548:     */   }
/*  549:     */   
/*  550:     */   private boolean convertCellValueToBoolean()
/*  551:     */   {
/*  552: 818 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  553:     */     {
/*  554:     */     case 5: 
/*  555: 820 */       return ((BoolErrRecord)this._record).getBooleanValue();
/*  556:     */     case 1: 
/*  557: 822 */       int sstIndex = ((LabelSSTRecord)this._record).getSSTIndex();
/*  558: 823 */       String text = this._book.getWorkbook().getSSTString(sstIndex).getString();
/*  559: 824 */       return Boolean.valueOf(text).booleanValue();
/*  560:     */     case 4: 
/*  561: 826 */       return ((NumberRecord)this._record).getValue() != 0.0D;
/*  562:     */     case 3: 
/*  563: 830 */       FormulaRecord fr = ((FormulaRecordAggregate)this._record).getFormulaRecord();
/*  564: 831 */       checkFormulaCachedValueType(CellType.BOOLEAN, fr);
/*  565: 832 */       return fr.getCachedBooleanValue();
/*  566:     */     case 2: 
/*  567:     */     case 6: 
/*  568: 837 */       return false;
/*  569:     */     }
/*  570: 839 */     throw new RuntimeException("Unexpected cell type (" + this._cellType + ")");
/*  571:     */   }
/*  572:     */   
/*  573:     */   private String convertCellValueToString()
/*  574:     */   {
/*  575: 843 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  576:     */     {
/*  577:     */     case 2: 
/*  578: 845 */       return "";
/*  579:     */     case 5: 
/*  580: 847 */       return ((BoolErrRecord)this._record).getBooleanValue() ? "TRUE" : "FALSE";
/*  581:     */     case 1: 
/*  582: 849 */       int sstIndex = ((LabelSSTRecord)this._record).getSSTIndex();
/*  583: 850 */       return this._book.getWorkbook().getSSTString(sstIndex).getString();
/*  584:     */     case 4: 
/*  585: 852 */       return NumberToTextConverter.toText(((NumberRecord)this._record).getValue());
/*  586:     */     case 6: 
/*  587: 854 */       return FormulaError.forInt(((BoolErrRecord)this._record).getErrorValue()).getString();
/*  588:     */     case 3: 
/*  589:     */       break;
/*  590:     */     default: 
/*  591: 860 */       throw new IllegalStateException("Unexpected cell type (" + this._cellType + ")");
/*  592:     */     }
/*  593: 862 */     FormulaRecordAggregate fra = (FormulaRecordAggregate)this._record;
/*  594: 863 */     FormulaRecord fr = fra.getFormulaRecord();
/*  595: 864 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.forInt(fr.getCachedResultType()).ordinal()])
/*  596:     */     {
/*  597:     */     case 5: 
/*  598: 866 */       return fr.getCachedBooleanValue() ? "TRUE" : "FALSE";
/*  599:     */     case 1: 
/*  600: 868 */       return fra.getStringValue();
/*  601:     */     case 4: 
/*  602: 870 */       return NumberToTextConverter.toText(fr.getValue());
/*  603:     */     case 6: 
/*  604: 872 */       return FormulaError.forInt(fr.getCachedErrorValue()).getString();
/*  605:     */     }
/*  606: 874 */     throw new IllegalStateException("Unexpected formula result type (" + this._cellType + ")");
/*  607:     */   }
/*  608:     */   
/*  609:     */   public boolean getBooleanCellValue()
/*  610:     */   {
/*  611: 886 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()])
/*  612:     */     {
/*  613:     */     case 2: 
/*  614: 888 */       return false;
/*  615:     */     case 5: 
/*  616: 890 */       return ((BoolErrRecord)this._record).getBooleanValue();
/*  617:     */     case 3: 
/*  618:     */       break;
/*  619:     */     case 4: 
/*  620:     */     default: 
/*  621: 894 */       throw typeMismatch(CellType.BOOLEAN, this._cellType, false);
/*  622:     */     }
/*  623: 896 */     FormulaRecord fr = ((FormulaRecordAggregate)this._record).getFormulaRecord();
/*  624: 897 */     checkFormulaCachedValueType(CellType.BOOLEAN, fr);
/*  625: 898 */     return fr.getCachedBooleanValue();
/*  626:     */   }
/*  627:     */   
/*  628:     */   public byte getErrorCellValue()
/*  629:     */   {
/*  630: 907 */     switch (this._cellType)
/*  631:     */     {
/*  632:     */     case ERROR: 
/*  633: 909 */       return ((BoolErrRecord)this._record).getErrorValue();
/*  634:     */     case FORMULA: 
/*  635: 911 */       FormulaRecord fr = ((FormulaRecordAggregate)this._record).getFormulaRecord();
/*  636: 912 */       checkFormulaCachedValueType(CellType.ERROR, fr);
/*  637: 913 */       return (byte)fr.getCachedErrorValue();
/*  638:     */     }
/*  639: 915 */     throw typeMismatch(CellType.ERROR, this._cellType, false);
/*  640:     */   }
/*  641:     */   
/*  642:     */   public void setCellStyle(CellStyle style)
/*  643:     */   {
/*  644: 931 */     setCellStyle((HSSFCellStyle)style);
/*  645:     */   }
/*  646:     */   
/*  647:     */   public void setCellStyle(HSSFCellStyle style)
/*  648:     */   {
/*  649: 935 */     if (style == null)
/*  650:     */     {
/*  651: 936 */       this._record.setXFIndex((short)15);
/*  652: 937 */       return;
/*  653:     */     }
/*  654: 941 */     style.verifyBelongsToWorkbook(this._book);
/*  655:     */     short styleIndex;
/*  656:     */     short styleIndex;
/*  657: 944 */     if (style.getUserStyleName() != null) {
/*  658: 945 */       styleIndex = applyUserCellStyle(style);
/*  659:     */     } else {
/*  660: 947 */       styleIndex = style.getIndex();
/*  661:     */     }
/*  662: 951 */     this._record.setXFIndex(styleIndex);
/*  663:     */   }
/*  664:     */   
/*  665:     */   public HSSFCellStyle getCellStyle()
/*  666:     */   {
/*  667: 961 */     short styleIndex = this._record.getXFIndex();
/*  668: 962 */     ExtendedFormatRecord xf = this._book.getWorkbook().getExFormatAt(styleIndex);
/*  669: 963 */     return new HSSFCellStyle(styleIndex, xf, this._book);
/*  670:     */   }
/*  671:     */   
/*  672:     */   protected CellValueRecordInterface getCellValueRecord()
/*  673:     */   {
/*  674: 974 */     return this._record;
/*  675:     */   }
/*  676:     */   
/*  677:     */   private static void checkBounds(int cellIndex)
/*  678:     */   {
/*  679: 981 */     if ((cellIndex < 0) || (cellIndex > LAST_COLUMN_NUMBER)) {
/*  680: 982 */       throw new IllegalArgumentException("Invalid column index (" + cellIndex + ").  Allowable column range for " + "BIFF8" + " is (0.." + LAST_COLUMN_NUMBER + ") or ('A'..'" + LAST_COLUMN_NAME + "')");
/*  681:     */     }
/*  682:     */   }
/*  683:     */   
/*  684:     */   public void setAsActiveCell()
/*  685:     */   {
/*  686: 994 */     int row = this._record.getRow();
/*  687: 995 */     short col = this._record.getColumn();
/*  688: 996 */     this._sheet.getSheet().setActiveCellRow(row);
/*  689: 997 */     this._sheet.getSheet().setActiveCellCol(col);
/*  690:     */   }
/*  691:     */   
/*  692:     */   public String toString()
/*  693:     */   {
/*  694:1013 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[getCellTypeEnum().ordinal()])
/*  695:     */     {
/*  696:     */     case 2: 
/*  697:1015 */       return "";
/*  698:     */     case 5: 
/*  699:1017 */       return getBooleanCellValue() ? "TRUE" : "FALSE";
/*  700:     */     case 6: 
/*  701:1019 */       return ErrorEval.getText(((BoolErrRecord)this._record).getErrorValue());
/*  702:     */     case 3: 
/*  703:1021 */       return getCellFormula();
/*  704:     */     case 4: 
/*  705:1024 */       if (HSSFDateUtil.isCellDateFormatted(this))
/*  706:     */       {
/*  707:1025 */         SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
/*  708:1026 */         sdf.setTimeZone(LocaleUtil.getUserTimeZone());
/*  709:1027 */         return sdf.format(getDateCellValue());
/*  710:     */       }
/*  711:1029 */       return String.valueOf(getNumericCellValue());
/*  712:     */     case 1: 
/*  713:1031 */       return getStringCellValue();
/*  714:     */     }
/*  715:1033 */     return "Unknown Cell Type: " + getCellTypeEnum();
/*  716:     */   }
/*  717:     */   
/*  718:     */   public void setCellComment(Comment comment)
/*  719:     */   {
/*  720:1045 */     if (comment == null)
/*  721:     */     {
/*  722:1046 */       removeCellComment();
/*  723:1047 */       return;
/*  724:     */     }
/*  725:1050 */     comment.setRow(this._record.getRow());
/*  726:1051 */     comment.setColumn(this._record.getColumn());
/*  727:1052 */     this._comment = ((HSSFComment)comment);
/*  728:     */   }
/*  729:     */   
/*  730:     */   public HSSFComment getCellComment()
/*  731:     */   {
/*  732:1061 */     if (this._comment == null) {
/*  733:1062 */       this._comment = this._sheet.findCellComment(this._record.getRow(), this._record.getColumn());
/*  734:     */     }
/*  735:1064 */     return this._comment;
/*  736:     */   }
/*  737:     */   
/*  738:     */   public void removeCellComment()
/*  739:     */   {
/*  740:1074 */     HSSFComment comment = this._sheet.findCellComment(this._record.getRow(), this._record.getColumn());
/*  741:1075 */     this._comment = null;
/*  742:1076 */     if (null == comment) {
/*  743:1077 */       return;
/*  744:     */     }
/*  745:1079 */     this._sheet.getDrawingPatriarch().removeShape(comment);
/*  746:     */   }
/*  747:     */   
/*  748:     */   public HSSFHyperlink getHyperlink()
/*  749:     */   {
/*  750:1087 */     return this._sheet.getHyperlink(this._record.getRow(), this._record.getColumn());
/*  751:     */   }
/*  752:     */   
/*  753:     */   public void setHyperlink(Hyperlink hyperlink)
/*  754:     */   {
/*  755:1098 */     if (hyperlink == null)
/*  756:     */     {
/*  757:1099 */       removeHyperlink();
/*  758:1100 */       return;
/*  759:     */     }
/*  760:1103 */     HSSFHyperlink link = (HSSFHyperlink)hyperlink;
/*  761:     */     
/*  762:1105 */     link.setFirstRow(this._record.getRow());
/*  763:1106 */     link.setLastRow(this._record.getRow());
/*  764:1107 */     link.setFirstColumn(this._record.getColumn());
/*  765:1108 */     link.setLastColumn(this._record.getColumn());
/*  766:1110 */     switch (1.$SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[link.getTypeEnum().ordinal()])
/*  767:     */     {
/*  768:     */     case 1: 
/*  769:     */     case 2: 
/*  770:1113 */       link.setLabel("url");
/*  771:1114 */       break;
/*  772:     */     case 3: 
/*  773:1116 */       link.setLabel("file");
/*  774:1117 */       break;
/*  775:     */     case 4: 
/*  776:1119 */       link.setLabel("place");
/*  777:1120 */       break;
/*  778:     */     }
/*  779:1125 */     List<RecordBase> records = this._sheet.getSheet().getRecords();
/*  780:1126 */     int eofLoc = records.size() - 1;
/*  781:1127 */     records.add(eofLoc, link.record);
/*  782:     */   }
/*  783:     */   
/*  784:     */   public void removeHyperlink()
/*  785:     */   {
/*  786:1134 */     for (Iterator<RecordBase> it = this._sheet.getSheet().getRecords().iterator(); it.hasNext();)
/*  787:     */     {
/*  788:1135 */       RecordBase rec = (RecordBase)it.next();
/*  789:1136 */       if ((rec instanceof HyperlinkRecord))
/*  790:     */       {
/*  791:1137 */         HyperlinkRecord link = (HyperlinkRecord)rec;
/*  792:1138 */         if ((link.getFirstColumn() == this._record.getColumn()) && (link.getFirstRow() == this._record.getRow()))
/*  793:     */         {
/*  794:1139 */           it.remove();
/*  795:1140 */           return;
/*  796:     */         }
/*  797:     */       }
/*  798:     */     }
/*  799:     */   }
/*  800:     */   
/*  801:     */   /**
/*  802:     */    * @deprecated
/*  803:     */    */
/*  804:     */   public int getCachedFormulaResultType()
/*  805:     */   {
/*  806:1159 */     return getCachedFormulaResultTypeEnum().getCode();
/*  807:     */   }
/*  808:     */   
/*  809:     */   public CellType getCachedFormulaResultTypeEnum()
/*  810:     */   {
/*  811:1172 */     if (this._cellType != CellType.FORMULA) {
/*  812:1173 */       throw new IllegalStateException("Only formula cells have cached results");
/*  813:     */     }
/*  814:1175 */     int code = ((FormulaRecordAggregate)this._record).getFormulaRecord().getCachedResultType();
/*  815:1176 */     return CellType.forInt(code);
/*  816:     */   }
/*  817:     */   
/*  818:     */   void setCellArrayFormula(CellRangeAddress range)
/*  819:     */   {
/*  820:1180 */     int row = this._record.getRow();
/*  821:1181 */     short col = this._record.getColumn();
/*  822:1182 */     short styleIndex = this._record.getXFIndex();
/*  823:1183 */     setCellType(CellType.FORMULA, false, row, col, styleIndex);
/*  824:     */     
/*  825:     */ 
/*  826:1186 */     Ptg[] ptgsForCell = { new ExpPtg(range.getFirstRow(), range.getFirstColumn()) };
/*  827:1187 */     FormulaRecordAggregate agg = (FormulaRecordAggregate)this._record;
/*  828:1188 */     agg.setParsedExpression(ptgsForCell);
/*  829:     */   }
/*  830:     */   
/*  831:     */   public CellRangeAddress getArrayFormulaRange()
/*  832:     */   {
/*  833:1192 */     if (this._cellType != CellType.FORMULA)
/*  834:     */     {
/*  835:1193 */       String ref = new CellReference(this).formatAsString();
/*  836:1194 */       throw new IllegalStateException("Cell " + ref + " is not part of an array formula.");
/*  837:     */     }
/*  838:1197 */     return ((FormulaRecordAggregate)this._record).getArrayFormulaRange();
/*  839:     */   }
/*  840:     */   
/*  841:     */   public boolean isPartOfArrayFormulaGroup()
/*  842:     */   {
/*  843:1201 */     if (this._cellType != CellType.FORMULA) {
/*  844:1202 */       return false;
/*  845:     */     }
/*  846:1204 */     return ((FormulaRecordAggregate)this._record).isPartOfArrayFormula();
/*  847:     */   }
/*  848:     */   
/*  849:     */   void notifyArrayFormulaChanging(String msg)
/*  850:     */   {
/*  851:1213 */     CellRangeAddress cra = getArrayFormulaRange();
/*  852:1214 */     if (cra.getNumberOfCells() > 1) {
/*  853:1215 */       throw new IllegalStateException(msg);
/*  854:     */     }
/*  855:1218 */     getRow().getSheet().removeArrayFormula(this);
/*  856:     */   }
/*  857:     */   
/*  858:     */   void notifyArrayFormulaChanging()
/*  859:     */   {
/*  860:1236 */     CellReference ref = new CellReference(this);
/*  861:1237 */     String msg = "Cell " + ref.formatAsString() + " is part of a multi-cell array formula. " + "You cannot change part of an array.";
/*  862:     */     
/*  863:1239 */     notifyArrayFormulaChanging(msg);
/*  864:     */   }
/*  865:     */   
/*  866:     */   private short applyUserCellStyle(HSSFCellStyle style)
/*  867:     */   {
/*  868:1259 */     if (style.getUserStyleName() == null) {
/*  869:1260 */       throw new IllegalArgumentException("Expected user-defined style");
/*  870:     */     }
/*  871:1263 */     InternalWorkbook iwb = this._book.getWorkbook();
/*  872:1264 */     short userXf = -1;
/*  873:1265 */     int numfmt = iwb.getNumExFormats();
/*  874:1266 */     for (short i = 0; i < numfmt; i = (short)(i + 1))
/*  875:     */     {
/*  876:1267 */       ExtendedFormatRecord xf = iwb.getExFormatAt(i);
/*  877:1268 */       if ((xf.getXFType() == 0) && (xf.getParentIndex() == style.getIndex()))
/*  878:     */       {
/*  879:1269 */         userXf = i;
/*  880:1270 */         break;
/*  881:     */       }
/*  882:     */     }
/*  883:     */     short styleIndex;
/*  884:     */     short styleIndex;
/*  885:1274 */     if (userXf == -1)
/*  886:     */     {
/*  887:1275 */       ExtendedFormatRecord xfr = iwb.createCellXF();
/*  888:1276 */       xfr.cloneStyleFrom(iwb.getExFormatAt(style.getIndex()));
/*  889:1277 */       xfr.setIndentionOptions((short)0);
/*  890:1278 */       xfr.setXFType((short)0);
/*  891:1279 */       xfr.setParentIndex(style.getIndex());
/*  892:1280 */       styleIndex = (short)numfmt;
/*  893:     */     }
/*  894:     */     else
/*  895:     */     {
/*  896:1282 */       styleIndex = userXf;
/*  897:     */     }
/*  898:1285 */     return styleIndex;
/*  899:     */   }
/*  900:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFCell
 * JD-Core Version:    0.7.0.1
 */