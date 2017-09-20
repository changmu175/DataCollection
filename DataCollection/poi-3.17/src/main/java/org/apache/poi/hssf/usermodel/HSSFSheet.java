/*    1:     */ package org.apache.poi.hssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.PrintWriter;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.Iterator;
/*    7:     */ import java.util.List;
/*    8:     */ import java.util.Map;
/*    9:     */ import java.util.NavigableSet;
/*   10:     */ import java.util.TreeMap;
/*   11:     */ import java.util.TreeSet;
/*   12:     */ import org.apache.poi.ddf.EscherRecord;
/*   13:     */ import org.apache.poi.hssf.model.DrawingManager2;
/*   14:     */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   15:     */ import org.apache.poi.hssf.model.InternalSheet;
/*   16:     */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   17:     */ import org.apache.poi.hssf.record.AutoFilterInfoRecord;
/*   18:     */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   19:     */ import org.apache.poi.hssf.record.DVRecord;
/*   20:     */ import org.apache.poi.hssf.record.DrawingRecord;
/*   21:     */ import org.apache.poi.hssf.record.EscherAggregate;
/*   22:     */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   23:     */ import org.apache.poi.hssf.record.HCenterRecord;
/*   24:     */ import org.apache.poi.hssf.record.HyperlinkRecord;
/*   25:     */ import org.apache.poi.hssf.record.NameRecord;
/*   26:     */ import org.apache.poi.hssf.record.PrintGridlinesRecord;
/*   27:     */ import org.apache.poi.hssf.record.PrintHeadersRecord;
/*   28:     */ import org.apache.poi.hssf.record.PrintSetupRecord;
/*   29:     */ import org.apache.poi.hssf.record.Record;
/*   30:     */ import org.apache.poi.hssf.record.RecordBase;
/*   31:     */ import org.apache.poi.hssf.record.RowRecord;
/*   32:     */ import org.apache.poi.hssf.record.SCLRecord;
/*   33:     */ import org.apache.poi.hssf.record.VCenterRecord;
/*   34:     */ import org.apache.poi.hssf.record.WSBoolRecord;
/*   35:     */ import org.apache.poi.hssf.record.WindowTwoRecord;
/*   36:     */ import org.apache.poi.hssf.record.aggregates.DataValidityTable;
/*   37:     */ import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
/*   38:     */ import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
/*   39:     */ import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
/*   40:     */ import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate;
/*   41:     */ import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
/*   42:     */ import org.apache.poi.hssf.usermodel.helpers.HSSFRowShifter;
/*   43:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   44:     */ import org.apache.poi.ss.formula.FormulaShifter;
/*   45:     */ import org.apache.poi.ss.formula.FormulaType;
/*   46:     */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*   47:     */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*   48:     */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   49:     */ import org.apache.poi.ss.formula.ptg.UnionPtg;
/*   50:     */ import org.apache.poi.ss.usermodel.Cell;
/*   51:     */ import org.apache.poi.ss.usermodel.CellRange;
/*   52:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   53:     */ import org.apache.poi.ss.usermodel.CellType;
/*   54:     */ import org.apache.poi.ss.usermodel.DataValidation;
/*   55:     */ import org.apache.poi.ss.usermodel.DataValidationHelper;
/*   56:     */ import org.apache.poi.ss.usermodel.Row;
/*   57:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   58:     */ import org.apache.poi.ss.usermodel.helpers.RowShifter;
/*   59:     */ import org.apache.poi.ss.util.CellAddress;
/*   60:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   61:     */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   62:     */ import org.apache.poi.ss.util.CellReference;
/*   63:     */ import org.apache.poi.ss.util.PaneInformation;
/*   64:     */ import org.apache.poi.ss.util.SSCellRange;
/*   65:     */ import org.apache.poi.ss.util.SheetUtil;
/*   66:     */ import org.apache.poi.util.Configurator;
/*   67:     */ import org.apache.poi.util.POILogFactory;
/*   68:     */ import org.apache.poi.util.POILogger;
/*   69:     */ 
/*   70:     */ public final class HSSFSheet
/*   71:     */   implements Sheet
/*   72:     */ {
/*   73:  84 */   private static final POILogger log = POILogFactory.getLogger(HSSFSheet.class);
/*   74:     */   private static final int DEBUG = 1;
/*   75:     */   private static final float PX_DEFAULT = 32.0F;
/*   76:     */   private static final float PX_MODIFIED = 36.560001F;
/*   77: 102 */   public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFSheet.RowInitialCapacity", 20);
/*   78:     */   private final InternalSheet _sheet;
/*   79:     */   private final TreeMap<Integer, HSSFRow> _rows;
/*   80:     */   protected final InternalWorkbook _book;
/*   81:     */   protected final HSSFWorkbook _workbook;
/*   82:     */   private HSSFPatriarch _patriarch;
/*   83:     */   private int _firstrow;
/*   84:     */   private int _lastrow;
/*   85:     */   
/*   86:     */   protected HSSFSheet(HSSFWorkbook workbook)
/*   87:     */   {
/*   88: 126 */     this._sheet = InternalSheet.createSheet();
/*   89: 127 */     this._rows = new TreeMap();
/*   90: 128 */     this._workbook = workbook;
/*   91: 129 */     this._book = workbook.getWorkbook();
/*   92:     */   }
/*   93:     */   
/*   94:     */   protected HSSFSheet(HSSFWorkbook workbook, InternalSheet sheet)
/*   95:     */   {
/*   96: 141 */     this._sheet = sheet;
/*   97: 142 */     this._rows = new TreeMap();
/*   98: 143 */     this._workbook = workbook;
/*   99: 144 */     this._book = workbook.getWorkbook();
/*  100: 145 */     setPropertiesFromSheet(sheet);
/*  101:     */   }
/*  102:     */   
/*  103:     */   HSSFSheet cloneSheet(HSSFWorkbook workbook)
/*  104:     */   {
/*  105: 149 */     getDrawingPatriarch();
/*  106: 150 */     HSSFSheet sheet = new HSSFSheet(workbook, this._sheet.cloneSheet());
/*  107: 151 */     int pos = sheet._sheet.findFirstRecordLocBySid((short)236);
/*  108: 152 */     DrawingRecord dr = (DrawingRecord)sheet._sheet.findFirstRecordBySid((short)236);
/*  109: 153 */     if (null != dr) {
/*  110: 154 */       sheet._sheet.getRecords().remove(dr);
/*  111:     */     }
/*  112: 156 */     if (getDrawingPatriarch() != null)
/*  113:     */     {
/*  114: 157 */       HSSFPatriarch patr = HSSFPatriarch.createPatriarch(getDrawingPatriarch(), sheet);
/*  115: 158 */       sheet._sheet.getRecords().add(pos, patr.getBoundAggregate());
/*  116: 159 */       sheet._patriarch = patr;
/*  117:     */     }
/*  118: 161 */     return sheet;
/*  119:     */   }
/*  120:     */   
/*  121:     */   protected void preSerialize()
/*  122:     */   {
/*  123: 168 */     if (this._patriarch != null) {
/*  124: 169 */       this._patriarch.preSerialize();
/*  125:     */     }
/*  126:     */   }
/*  127:     */   
/*  128:     */   public HSSFWorkbook getWorkbook()
/*  129:     */   {
/*  130: 180 */     return this._workbook;
/*  131:     */   }
/*  132:     */   
/*  133:     */   private void setPropertiesFromSheet(InternalSheet sheet)
/*  134:     */   {
/*  135: 187 */     RowRecord row = sheet.getNextRow();
/*  136: 189 */     while (row != null)
/*  137:     */     {
/*  138: 190 */       createRowFromRecord(row);
/*  139:     */       
/*  140: 192 */       row = sheet.getNextRow();
/*  141:     */     }
/*  142: 195 */     Iterator<CellValueRecordInterface> iter = sheet.getCellValueIterator();
/*  143: 196 */     long timestart = System.currentTimeMillis();
/*  144: 198 */     if (log.check(1)) {
/*  145: 199 */       log.log(1, new Object[] { "Time at start of cell creating in HSSF sheet = ", Long.valueOf(timestart) });
/*  146:     */     }
/*  147: 202 */     HSSFRow lastrow = null;
/*  148: 205 */     while (iter.hasNext())
/*  149:     */     {
/*  150: 206 */       CellValueRecordInterface cval = (CellValueRecordInterface)iter.next();
/*  151:     */       
/*  152: 208 */       long cellstart = System.currentTimeMillis();
/*  153: 209 */       HSSFRow hrow = lastrow;
/*  154: 211 */       if ((hrow == null) || (hrow.getRowNum() != cval.getRow()))
/*  155:     */       {
/*  156: 212 */         hrow = getRow(cval.getRow());
/*  157: 213 */         lastrow = hrow;
/*  158: 214 */         if (hrow == null)
/*  159:     */         {
/*  160: 224 */           RowRecord rowRec = new RowRecord(cval.getRow());
/*  161: 225 */           sheet.addRow(rowRec);
/*  162: 226 */           hrow = createRowFromRecord(rowRec);
/*  163:     */         }
/*  164:     */       }
/*  165: 229 */       if (log.check(1)) {
/*  166: 230 */         if ((cval instanceof Record)) {
/*  167: 231 */           log.log(1, new Object[] { "record id = " + Integer.toHexString(((Record)cval).getSid()) });
/*  168:     */         } else {
/*  169: 233 */           log.log(1, new Object[] { "record = " + cval });
/*  170:     */         }
/*  171:     */       }
/*  172: 236 */       hrow.createCellFromRecord(cval);
/*  173: 237 */       if (log.check(1)) {
/*  174: 238 */         log.log(1, new Object[] { "record took ", Long.valueOf(System.currentTimeMillis() - cellstart) });
/*  175:     */       }
/*  176:     */     }
/*  177: 243 */     if (log.check(1)) {
/*  178: 244 */       log.log(1, new Object[] { "total sheet cell creation took ", Long.valueOf(System.currentTimeMillis() - timestart) });
/*  179:     */     }
/*  180:     */   }
/*  181:     */   
/*  182:     */   public HSSFRow createRow(int rownum)
/*  183:     */   {
/*  184: 259 */     HSSFRow row = new HSSFRow(this._workbook, this, rownum);
/*  185:     */     
/*  186: 261 */     row.setHeight(getDefaultRowHeight());
/*  187: 262 */     row.getRowRecord().setBadFontHeight(false);
/*  188:     */     
/*  189: 264 */     addRow(row, true);
/*  190: 265 */     return row;
/*  191:     */   }
/*  192:     */   
/*  193:     */   private HSSFRow createRowFromRecord(RowRecord row)
/*  194:     */   {
/*  195: 277 */     HSSFRow hrow = new HSSFRow(this._workbook, this, row);
/*  196:     */     
/*  197: 279 */     addRow(hrow, false);
/*  198: 280 */     return hrow;
/*  199:     */   }
/*  200:     */   
/*  201:     */   public void removeRow(Row row)
/*  202:     */   {
/*  203: 290 */     HSSFRow hrow = (HSSFRow)row;
/*  204: 291 */     if (row.getSheet() != this) {
/*  205: 292 */       throw new IllegalArgumentException("Specified row does not belong to this sheet");
/*  206:     */     }
/*  207: 294 */     for (Cell cell : row)
/*  208:     */     {
/*  209: 295 */       HSSFCell xcell = (HSSFCell)cell;
/*  210: 296 */       if (xcell.isPartOfArrayFormulaGroup())
/*  211:     */       {
/*  212: 297 */         String msg = "Row[rownum=" + row.getRowNum() + "] contains cell(s) included in a multi-cell array formula. You cannot change part of an array.";
/*  213: 298 */         xcell.notifyArrayFormulaChanging(msg);
/*  214:     */       }
/*  215:     */     }
/*  216: 302 */     if (this._rows.size() > 0)
/*  217:     */     {
/*  218: 303 */       Integer key = Integer.valueOf(row.getRowNum());
/*  219: 304 */       HSSFRow removedRow = (HSSFRow)this._rows.remove(key);
/*  220: 305 */       if (removedRow != row) {
/*  221: 307 */         throw new IllegalArgumentException("Specified row does not belong to this sheet");
/*  222:     */       }
/*  223: 309 */       if (hrow.getRowNum() == getLastRowNum()) {
/*  224: 310 */         this._lastrow = findLastRow(this._lastrow);
/*  225:     */       }
/*  226: 312 */       if (hrow.getRowNum() == getFirstRowNum()) {
/*  227: 313 */         this._firstrow = findFirstRow(this._firstrow);
/*  228:     */       }
/*  229: 315 */       this._sheet.removeRow(hrow.getRowRecord());
/*  230:     */     }
/*  231:     */   }
/*  232:     */   
/*  233:     */   private int findLastRow(int lastrow)
/*  234:     */   {
/*  235: 323 */     if (lastrow < 1) {
/*  236: 324 */       return 0;
/*  237:     */     }
/*  238: 326 */     int rownum = lastrow - 1;
/*  239: 327 */     HSSFRow r = getRow(rownum);
/*  240: 329 */     while ((r == null) && (rownum > 0)) {
/*  241: 330 */       r = getRow(--rownum);
/*  242:     */     }
/*  243: 332 */     if (r == null) {
/*  244: 333 */       return 0;
/*  245:     */     }
/*  246: 335 */     return rownum;
/*  247:     */   }
/*  248:     */   
/*  249:     */   private int findFirstRow(int firstrow)
/*  250:     */   {
/*  251: 343 */     int rownum = firstrow + 1;
/*  252: 344 */     HSSFRow r = getRow(rownum);
/*  253: 346 */     while ((r == null) && (rownum <= getLastRowNum())) {
/*  254: 347 */       r = getRow(++rownum);
/*  255:     */     }
/*  256: 350 */     if (rownum > getLastRowNum()) {
/*  257: 351 */       return 0;
/*  258:     */     }
/*  259: 353 */     return rownum;
/*  260:     */   }
/*  261:     */   
/*  262:     */   private void addRow(HSSFRow row, boolean addLow)
/*  263:     */   {
/*  264: 363 */     this._rows.put(Integer.valueOf(row.getRowNum()), row);
/*  265: 364 */     if (addLow) {
/*  266: 365 */       this._sheet.addRow(row.getRowRecord());
/*  267:     */     }
/*  268: 367 */     boolean firstRow = this._rows.size() == 1;
/*  269: 368 */     if ((row.getRowNum() > getLastRowNum()) || (firstRow)) {
/*  270: 369 */       this._lastrow = row.getRowNum();
/*  271:     */     }
/*  272: 371 */     if ((row.getRowNum() < getFirstRowNum()) || (firstRow)) {
/*  273: 372 */       this._firstrow = row.getRowNum();
/*  274:     */     }
/*  275:     */   }
/*  276:     */   
/*  277:     */   public HSSFRow getRow(int rowIndex)
/*  278:     */   {
/*  279: 385 */     return (HSSFRow)this._rows.get(Integer.valueOf(rowIndex));
/*  280:     */   }
/*  281:     */   
/*  282:     */   public int getPhysicalNumberOfRows()
/*  283:     */   {
/*  284: 393 */     return this._rows.size();
/*  285:     */   }
/*  286:     */   
/*  287:     */   public int getFirstRowNum()
/*  288:     */   {
/*  289: 403 */     return this._firstrow;
/*  290:     */   }
/*  291:     */   
/*  292:     */   public int getLastRowNum()
/*  293:     */   {
/*  294: 421 */     return this._lastrow;
/*  295:     */   }
/*  296:     */   
/*  297:     */   public List<HSSFDataValidation> getDataValidations()
/*  298:     */   {
/*  299: 426 */     DataValidityTable dvt = this._sheet.getOrCreateDataValidityTable();
/*  300: 427 */     final List<HSSFDataValidation> hssfValidations = new ArrayList();
/*  301: 428 */     RecordAggregate.RecordVisitor visitor = new RecordAggregate.RecordVisitor()
/*  302:     */     {
/*  303: 429 */       private HSSFEvaluationWorkbook book = HSSFEvaluationWorkbook.create(HSSFSheet.this.getWorkbook());
/*  304:     */       
/*  305:     */       public void visitRecord(Record r)
/*  306:     */       {
/*  307: 433 */         if (!(r instanceof DVRecord)) {
/*  308: 434 */           return;
/*  309:     */         }
/*  310: 436 */         DVRecord dvRecord = (DVRecord)r;
/*  311: 437 */         CellRangeAddressList regions = dvRecord.getCellRangeAddress().copy();
/*  312: 438 */         DVConstraint constraint = DVConstraint.createDVConstraint(dvRecord, this.book);
/*  313: 439 */         HSSFDataValidation hssfDataValidation = new HSSFDataValidation(regions, constraint);
/*  314: 440 */         hssfDataValidation.setErrorStyle(dvRecord.getErrorStyle());
/*  315: 441 */         hssfDataValidation.setEmptyCellAllowed(dvRecord.getEmptyCellAllowed());
/*  316: 442 */         hssfDataValidation.setSuppressDropDownArrow(dvRecord.getSuppressDropdownArrow());
/*  317: 443 */         hssfDataValidation.createPromptBox(dvRecord.getPromptTitle(), dvRecord.getPromptText());
/*  318: 444 */         hssfDataValidation.setShowPromptBox(dvRecord.getShowPromptOnCellSelected());
/*  319: 445 */         hssfDataValidation.createErrorBox(dvRecord.getErrorTitle(), dvRecord.getErrorText());
/*  320: 446 */         hssfDataValidation.setShowErrorBox(dvRecord.getShowErrorOnInvalidValue());
/*  321: 447 */         hssfValidations.add(hssfDataValidation);
/*  322:     */       }
/*  323: 449 */     };
/*  324: 450 */     dvt.visitContainedRecords(visitor);
/*  325: 451 */     return hssfValidations;
/*  326:     */   }
/*  327:     */   
/*  328:     */   public void addValidationData(DataValidation dataValidation)
/*  329:     */   {
/*  330: 461 */     if (dataValidation == null) {
/*  331: 462 */       throw new IllegalArgumentException("objValidation must not be null");
/*  332:     */     }
/*  333: 464 */     HSSFDataValidation hssfDataValidation = (HSSFDataValidation)dataValidation;
/*  334: 465 */     DataValidityTable dvt = this._sheet.getOrCreateDataValidityTable();
/*  335:     */     
/*  336: 467 */     DVRecord dvRecord = hssfDataValidation.createDVRecord(this);
/*  337: 468 */     dvt.addDataValidation(dvRecord);
/*  338:     */   }
/*  339:     */   
/*  340:     */   public void setColumnHidden(int columnIndex, boolean hidden)
/*  341:     */   {
/*  342: 479 */     this._sheet.setColumnHidden(columnIndex, hidden);
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean isColumnHidden(int columnIndex)
/*  346:     */   {
/*  347: 490 */     return this._sheet.isColumnHidden(columnIndex);
/*  348:     */   }
/*  349:     */   
/*  350:     */   public void setColumnWidth(int columnIndex, int width)
/*  351:     */   {
/*  352: 533 */     this._sheet.setColumnWidth(columnIndex, width);
/*  353:     */   }
/*  354:     */   
/*  355:     */   public int getColumnWidth(int columnIndex)
/*  356:     */   {
/*  357: 544 */     return this._sheet.getColumnWidth(columnIndex);
/*  358:     */   }
/*  359:     */   
/*  360:     */   public float getColumnWidthInPixels(int column)
/*  361:     */   {
/*  362: 549 */     int cw = getColumnWidth(column);
/*  363: 550 */     int def = getDefaultColumnWidth() * 256;
/*  364: 551 */     float px = cw == def ? 32.0F : 36.560001F;
/*  365:     */     
/*  366: 553 */     return cw / px;
/*  367:     */   }
/*  368:     */   
/*  369:     */   public int getDefaultColumnWidth()
/*  370:     */   {
/*  371: 564 */     return this._sheet.getDefaultColumnWidth();
/*  372:     */   }
/*  373:     */   
/*  374:     */   public void setDefaultColumnWidth(int width)
/*  375:     */   {
/*  376: 575 */     this._sheet.setDefaultColumnWidth(width);
/*  377:     */   }
/*  378:     */   
/*  379:     */   public short getDefaultRowHeight()
/*  380:     */   {
/*  381: 587 */     return this._sheet.getDefaultRowHeight();
/*  382:     */   }
/*  383:     */   
/*  384:     */   public float getDefaultRowHeightInPoints()
/*  385:     */   {
/*  386: 598 */     return this._sheet.getDefaultRowHeight() / 20.0F;
/*  387:     */   }
/*  388:     */   
/*  389:     */   public void setDefaultRowHeight(short height)
/*  390:     */   {
/*  391: 609 */     this._sheet.setDefaultRowHeight(height);
/*  392:     */   }
/*  393:     */   
/*  394:     */   public void setDefaultRowHeightInPoints(float height)
/*  395:     */   {
/*  396: 620 */     this._sheet.setDefaultRowHeight((short)(int)(height * 20.0F));
/*  397:     */   }
/*  398:     */   
/*  399:     */   public HSSFCellStyle getColumnStyle(int column)
/*  400:     */   {
/*  401: 630 */     short styleIndex = this._sheet.getXFIndexForColAt((short)column);
/*  402: 632 */     if (styleIndex == 15) {
/*  403: 634 */       return null;
/*  404:     */     }
/*  405: 637 */     ExtendedFormatRecord xf = this._book.getExFormatAt(styleIndex);
/*  406: 638 */     return new HSSFCellStyle(styleIndex, xf, this._book);
/*  407:     */   }
/*  408:     */   
/*  409:     */   public boolean isGridsPrinted()
/*  410:     */   {
/*  411: 647 */     return this._sheet.isGridsPrinted();
/*  412:     */   }
/*  413:     */   
/*  414:     */   public void setGridsPrinted(boolean value)
/*  415:     */   {
/*  416: 656 */     this._sheet.setGridsPrinted(value);
/*  417:     */   }
/*  418:     */   
/*  419:     */   public int addMergedRegion(CellRangeAddress region)
/*  420:     */   {
/*  421: 670 */     return addMergedRegion(region, true);
/*  422:     */   }
/*  423:     */   
/*  424:     */   public int addMergedRegionUnsafe(CellRangeAddress region)
/*  425:     */   {
/*  426: 688 */     return addMergedRegion(region, false);
/*  427:     */   }
/*  428:     */   
/*  429:     */   public void validateMergedRegions()
/*  430:     */   {
/*  431: 700 */     checkForMergedRegionsIntersectingArrayFormulas();
/*  432: 701 */     checkForIntersectingMergedRegions();
/*  433:     */   }
/*  434:     */   
/*  435:     */   private int addMergedRegion(CellRangeAddress region, boolean validate)
/*  436:     */   {
/*  437: 715 */     if (region.getNumberOfCells() < 2) {
/*  438: 716 */       throw new IllegalArgumentException("Merged region " + region.formatAsString() + " must contain 2 or more cells");
/*  439:     */     }
/*  440: 718 */     region.validate(SpreadsheetVersion.EXCEL97);
/*  441: 720 */     if (validate)
/*  442:     */     {
/*  443: 723 */       validateArrayFormulas(region);
/*  444:     */       
/*  445:     */ 
/*  446:     */ 
/*  447: 727 */       validateMergedRegions(region);
/*  448:     */     }
/*  449: 730 */     return this._sheet.addMergedRegion(region.getFirstRow(), region.getFirstColumn(), region.getLastRow(), region.getLastColumn());
/*  450:     */   }
/*  451:     */   
/*  452:     */   private void validateArrayFormulas(CellRangeAddress region)
/*  453:     */   {
/*  454: 739 */     int firstRow = region.getFirstRow();
/*  455: 740 */     int firstColumn = region.getFirstColumn();
/*  456: 741 */     int lastRow = region.getLastRow();
/*  457: 742 */     int lastColumn = region.getLastColumn();
/*  458: 743 */     for (int rowIn = firstRow; rowIn <= lastRow; rowIn++)
/*  459:     */     {
/*  460: 744 */       HSSFRow row = getRow(rowIn);
/*  461: 745 */       if (row != null) {
/*  462: 747 */         for (int colIn = firstColumn; colIn <= lastColumn; colIn++)
/*  463:     */         {
/*  464: 748 */           HSSFCell cell = row.getCell(colIn);
/*  465: 749 */           if (cell != null) {
/*  466: 751 */             if (cell.isPartOfArrayFormulaGroup())
/*  467:     */             {
/*  468: 752 */               CellRangeAddress arrayRange = cell.getArrayFormulaRange();
/*  469: 753 */               if ((arrayRange.getNumberOfCells() > 1) && (region.intersects(arrayRange)))
/*  470:     */               {
/*  471: 754 */                 String msg = "The range " + region.formatAsString() + " intersects with a multi-cell array formula. " + "You cannot merge cells of an array.";
/*  472:     */                 
/*  473: 756 */                 throw new IllegalStateException(msg);
/*  474:     */               }
/*  475:     */             }
/*  476:     */           }
/*  477:     */         }
/*  478:     */       }
/*  479:     */     }
/*  480:     */   }
/*  481:     */   
/*  482:     */   private void checkForMergedRegionsIntersectingArrayFormulas()
/*  483:     */   {
/*  484: 770 */     for (CellRangeAddress region : getMergedRegions()) {
/*  485: 771 */       validateArrayFormulas(region);
/*  486:     */     }
/*  487:     */   }
/*  488:     */   
/*  489:     */   private void validateMergedRegions(CellRangeAddress candidateRegion)
/*  490:     */   {
/*  491: 776 */     for (CellRangeAddress existingRegion : getMergedRegions()) {
/*  492: 777 */       if (existingRegion.intersects(candidateRegion)) {
/*  493: 778 */         throw new IllegalStateException("Cannot add merged region " + candidateRegion.formatAsString() + " to sheet because it overlaps with an existing merged region (" + existingRegion.formatAsString() + ").");
/*  494:     */       }
/*  495:     */     }
/*  496:     */   }
/*  497:     */   
/*  498:     */   private void checkForIntersectingMergedRegions()
/*  499:     */   {
/*  500: 790 */     List<CellRangeAddress> regions = getMergedRegions();
/*  501: 791 */     int size = regions.size();
/*  502:     */     CellRangeAddress region;
/*  503: 792 */     for (int i = 0; i < size; i++)
/*  504:     */     {
/*  505: 793 */       region = (CellRangeAddress)regions.get(i);
/*  506: 794 */       for (CellRangeAddress other : regions.subList(i + 1, regions.size())) {
/*  507: 795 */         if (region.intersects(other))
/*  508:     */         {
/*  509: 796 */           String msg = "The range " + region.formatAsString() + " intersects with another merged region " + other.formatAsString() + " in this sheet";
/*  510:     */           
/*  511:     */ 
/*  512: 799 */           throw new IllegalStateException(msg);
/*  513:     */         }
/*  514:     */       }
/*  515:     */     }
/*  516:     */   }
/*  517:     */   
/*  518:     */   public void setForceFormulaRecalculation(boolean value)
/*  519:     */   {
/*  520: 823 */     this._sheet.setUncalced(value);
/*  521:     */   }
/*  522:     */   
/*  523:     */   public boolean getForceFormulaRecalculation()
/*  524:     */   {
/*  525: 834 */     return this._sheet.getUncalced();
/*  526:     */   }
/*  527:     */   
/*  528:     */   public void setVerticallyCenter(boolean value)
/*  529:     */   {
/*  530: 845 */     this._sheet.getPageSettings().getVCenter().setVCenter(value);
/*  531:     */   }
/*  532:     */   
/*  533:     */   public boolean getVerticallyCenter()
/*  534:     */   {
/*  535: 853 */     return this._sheet.getPageSettings().getVCenter().getVCenter();
/*  536:     */   }
/*  537:     */   
/*  538:     */   public void setHorizontallyCenter(boolean value)
/*  539:     */   {
/*  540: 863 */     this._sheet.getPageSettings().getHCenter().setHCenter(value);
/*  541:     */   }
/*  542:     */   
/*  543:     */   public boolean getHorizontallyCenter()
/*  544:     */   {
/*  545: 871 */     return this._sheet.getPageSettings().getHCenter().getHCenter();
/*  546:     */   }
/*  547:     */   
/*  548:     */   public void setRightToLeft(boolean value)
/*  549:     */   {
/*  550: 881 */     this._sheet.getWindowTwo().setArabic(value);
/*  551:     */   }
/*  552:     */   
/*  553:     */   public boolean isRightToLeft()
/*  554:     */   {
/*  555: 891 */     return this._sheet.getWindowTwo().getArabic();
/*  556:     */   }
/*  557:     */   
/*  558:     */   public void removeMergedRegion(int index)
/*  559:     */   {
/*  560: 901 */     this._sheet.removeMergedRegion(index);
/*  561:     */   }
/*  562:     */   
/*  563:     */   public void removeMergedRegions(Collection<Integer> indices)
/*  564:     */   {
/*  565: 911 */     for (Iterator i$ = new TreeSet(indices).descendingSet().iterator(); i$.hasNext();)
/*  566:     */     {
/*  567: 911 */       int i = ((Integer)i$.next()).intValue();
/*  568: 912 */       this._sheet.removeMergedRegion(i);
/*  569:     */     }
/*  570:     */   }
/*  571:     */   
/*  572:     */   public int getNumMergedRegions()
/*  573:     */   {
/*  574: 923 */     return this._sheet.getNumMergedRegions();
/*  575:     */   }
/*  576:     */   
/*  577:     */   public CellRangeAddress getMergedRegion(int index)
/*  578:     */   {
/*  579: 931 */     return this._sheet.getMergedRegionAt(index);
/*  580:     */   }
/*  581:     */   
/*  582:     */   public List<CellRangeAddress> getMergedRegions()
/*  583:     */   {
/*  584: 939 */     List<CellRangeAddress> addresses = new ArrayList();
/*  585: 940 */     int count = this._sheet.getNumMergedRegions();
/*  586: 941 */     for (int i = 0; i < count; i++) {
/*  587: 942 */       addresses.add(this._sheet.getMergedRegionAt(i));
/*  588:     */     }
/*  589: 944 */     return addresses;
/*  590:     */   }
/*  591:     */   
/*  592:     */   public Iterator<Row> rowIterator()
/*  593:     */   {
/*  594: 955 */     Iterator<Row> result = this._rows.values().iterator();
/*  595: 956 */     return result;
/*  596:     */   }
/*  597:     */   
/*  598:     */   public Iterator<Row> iterator()
/*  599:     */   {
/*  600: 965 */     return rowIterator();
/*  601:     */   }
/*  602:     */   
/*  603:     */   InternalSheet getSheet()
/*  604:     */   {
/*  605: 976 */     return this._sheet;
/*  606:     */   }
/*  607:     */   
/*  608:     */   public void setAlternativeExpression(boolean b)
/*  609:     */   {
/*  610: 985 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  611:     */     
/*  612:     */ 
/*  613: 988 */     record.setAlternateExpression(b);
/*  614:     */   }
/*  615:     */   
/*  616:     */   public void setAlternativeFormula(boolean b)
/*  617:     */   {
/*  618: 997 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  619:     */     
/*  620:     */ 
/*  621:1000 */     record.setAlternateFormula(b);
/*  622:     */   }
/*  623:     */   
/*  624:     */   public void setAutobreaks(boolean b)
/*  625:     */   {
/*  626:1010 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  627:     */     
/*  628:     */ 
/*  629:1013 */     record.setAutobreaks(b);
/*  630:     */   }
/*  631:     */   
/*  632:     */   public void setDialog(boolean b)
/*  633:     */   {
/*  634:1022 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  635:     */     
/*  636:     */ 
/*  637:1025 */     record.setDialog(b);
/*  638:     */   }
/*  639:     */   
/*  640:     */   public void setDisplayGuts(boolean b)
/*  641:     */   {
/*  642:1035 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  643:     */     
/*  644:     */ 
/*  645:1038 */     record.setDisplayGuts(b);
/*  646:     */   }
/*  647:     */   
/*  648:     */   public void setFitToPage(boolean b)
/*  649:     */   {
/*  650:1048 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  651:     */     
/*  652:     */ 
/*  653:1051 */     record.setFitToPage(b);
/*  654:     */   }
/*  655:     */   
/*  656:     */   public void setRowSumsBelow(boolean b)
/*  657:     */   {
/*  658:1061 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  659:     */     
/*  660:     */ 
/*  661:1064 */     record.setRowSumsBelow(b);
/*  662:     */     
/*  663:1066 */     record.setAlternateExpression(b);
/*  664:     */   }
/*  665:     */   
/*  666:     */   public void setRowSumsRight(boolean b)
/*  667:     */   {
/*  668:1076 */     WSBoolRecord record = (WSBoolRecord)this._sheet.findFirstRecordBySid((short)129);
/*  669:     */     
/*  670:     */ 
/*  671:1079 */     record.setRowSumsRight(b);
/*  672:     */   }
/*  673:     */   
/*  674:     */   public boolean getAlternateExpression()
/*  675:     */   {
/*  676:1088 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getAlternateExpression();
/*  677:     */   }
/*  678:     */   
/*  679:     */   public boolean getAlternateFormula()
/*  680:     */   {
/*  681:1098 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getAlternateFormula();
/*  682:     */   }
/*  683:     */   
/*  684:     */   public boolean getAutobreaks()
/*  685:     */   {
/*  686:1109 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getAutobreaks();
/*  687:     */   }
/*  688:     */   
/*  689:     */   public boolean getDialog()
/*  690:     */   {
/*  691:1119 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getDialog();
/*  692:     */   }
/*  693:     */   
/*  694:     */   public boolean getDisplayGuts()
/*  695:     */   {
/*  696:1130 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getDisplayGuts();
/*  697:     */   }
/*  698:     */   
/*  699:     */   public boolean isDisplayZeros()
/*  700:     */   {
/*  701:1146 */     return this._sheet.getWindowTwo().getDisplayZeros();
/*  702:     */   }
/*  703:     */   
/*  704:     */   public void setDisplayZeros(boolean value)
/*  705:     */   {
/*  706:1160 */     this._sheet.getWindowTwo().setDisplayZeros(value);
/*  707:     */   }
/*  708:     */   
/*  709:     */   public boolean getFitToPage()
/*  710:     */   {
/*  711:1170 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getFitToPage();
/*  712:     */   }
/*  713:     */   
/*  714:     */   public boolean getRowSumsBelow()
/*  715:     */   {
/*  716:1181 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getRowSumsBelow();
/*  717:     */   }
/*  718:     */   
/*  719:     */   public boolean getRowSumsRight()
/*  720:     */   {
/*  721:1192 */     return ((WSBoolRecord)this._sheet.findFirstRecordBySid((short)129)).getRowSumsRight();
/*  722:     */   }
/*  723:     */   
/*  724:     */   public boolean isPrintGridlines()
/*  725:     */   {
/*  726:1203 */     return getSheet().getPrintGridlines().getPrintGridlines();
/*  727:     */   }
/*  728:     */   
/*  729:     */   public void setPrintGridlines(boolean show)
/*  730:     */   {
/*  731:1214 */     getSheet().getPrintGridlines().setPrintGridlines(show);
/*  732:     */   }
/*  733:     */   
/*  734:     */   public boolean isPrintRowAndColumnHeadings()
/*  735:     */   {
/*  736:1224 */     return getSheet().getPrintHeaders().getPrintHeaders();
/*  737:     */   }
/*  738:     */   
/*  739:     */   public void setPrintRowAndColumnHeadings(boolean show)
/*  740:     */   {
/*  741:1235 */     getSheet().getPrintHeaders().setPrintHeaders(show);
/*  742:     */   }
/*  743:     */   
/*  744:     */   public HSSFPrintSetup getPrintSetup()
/*  745:     */   {
/*  746:1245 */     return new HSSFPrintSetup(this._sheet.getPageSettings().getPrintSetup());
/*  747:     */   }
/*  748:     */   
/*  749:     */   public HSSFHeader getHeader()
/*  750:     */   {
/*  751:1250 */     return new HSSFHeader(this._sheet.getPageSettings());
/*  752:     */   }
/*  753:     */   
/*  754:     */   public HSSFFooter getFooter()
/*  755:     */   {
/*  756:1255 */     return new HSSFFooter(this._sheet.getPageSettings());
/*  757:     */   }
/*  758:     */   
/*  759:     */   public boolean isSelected()
/*  760:     */   {
/*  761:1265 */     return getSheet().getWindowTwo().getSelected();
/*  762:     */   }
/*  763:     */   
/*  764:     */   public void setSelected(boolean sel)
/*  765:     */   {
/*  766:1275 */     getSheet().getWindowTwo().setSelected(sel);
/*  767:     */   }
/*  768:     */   
/*  769:     */   public boolean isActive()
/*  770:     */   {
/*  771:1282 */     return getSheet().getWindowTwo().isActive();
/*  772:     */   }
/*  773:     */   
/*  774:     */   public void setActive(boolean sel)
/*  775:     */   {
/*  776:1291 */     getSheet().getWindowTwo().setActive(sel);
/*  777:     */   }
/*  778:     */   
/*  779:     */   public double getMargin(short margin)
/*  780:     */   {
/*  781:1302 */     switch (margin)
/*  782:     */     {
/*  783:     */     case 5: 
/*  784:1304 */       return this._sheet.getPageSettings().getPrintSetup().getFooterMargin();
/*  785:     */     case 4: 
/*  786:1306 */       return this._sheet.getPageSettings().getPrintSetup().getHeaderMargin();
/*  787:     */     }
/*  788:1308 */     return this._sheet.getPageSettings().getMargin(margin);
/*  789:     */   }
/*  790:     */   
/*  791:     */   public void setMargin(short margin, double size)
/*  792:     */   {
/*  793:1320 */     switch (margin)
/*  794:     */     {
/*  795:     */     case 5: 
/*  796:1322 */       this._sheet.getPageSettings().getPrintSetup().setFooterMargin(size);
/*  797:1323 */       break;
/*  798:     */     case 4: 
/*  799:1325 */       this._sheet.getPageSettings().getPrintSetup().setHeaderMargin(size);
/*  800:1326 */       break;
/*  801:     */     default: 
/*  802:1328 */       this._sheet.getPageSettings().setMargin(margin, size);
/*  803:     */     }
/*  804:     */   }
/*  805:     */   
/*  806:     */   private WorksheetProtectionBlock getProtectionBlock()
/*  807:     */   {
/*  808:1333 */     return this._sheet.getProtectionBlock();
/*  809:     */   }
/*  810:     */   
/*  811:     */   public boolean getProtect()
/*  812:     */   {
/*  813:1343 */     return getProtectionBlock().isSheetProtected();
/*  814:     */   }
/*  815:     */   
/*  816:     */   public short getPassword()
/*  817:     */   {
/*  818:1350 */     return (short)getProtectionBlock().getPasswordHash();
/*  819:     */   }
/*  820:     */   
/*  821:     */   public boolean getObjectProtect()
/*  822:     */   {
/*  823:1359 */     return getProtectionBlock().isObjectProtected();
/*  824:     */   }
/*  825:     */   
/*  826:     */   public boolean getScenarioProtect()
/*  827:     */   {
/*  828:1369 */     return getProtectionBlock().isScenarioProtected();
/*  829:     */   }
/*  830:     */   
/*  831:     */   public void protectSheet(String password)
/*  832:     */   {
/*  833:1379 */     getProtectionBlock().protectSheet(password, true, true);
/*  834:     */   }
/*  835:     */   
/*  836:     */   public void setZoom(int numerator, int denominator)
/*  837:     */   {
/*  838:1392 */     if ((numerator < 1) || (numerator > 65535)) {
/*  839:1393 */       throw new IllegalArgumentException("Numerator must be greater than 0 and less than 65536");
/*  840:     */     }
/*  841:1394 */     if ((denominator < 1) || (denominator > 65535)) {
/*  842:1395 */       throw new IllegalArgumentException("Denominator must be greater than 0 and less than 65536");
/*  843:     */     }
/*  844:1397 */     SCLRecord sclRecord = new SCLRecord();
/*  845:1398 */     sclRecord.setNumerator((short)numerator);
/*  846:1399 */     sclRecord.setDenominator((short)denominator);
/*  847:1400 */     getSheet().setSCLRecord(sclRecord);
/*  848:     */   }
/*  849:     */   
/*  850:     */   public void setZoom(int scale)
/*  851:     */   {
/*  852:1422 */     setZoom(scale, 100);
/*  853:     */   }
/*  854:     */   
/*  855:     */   public short getTopRow()
/*  856:     */   {
/*  857:1433 */     return this._sheet.getTopRow();
/*  858:     */   }
/*  859:     */   
/*  860:     */   public short getLeftCol()
/*  861:     */   {
/*  862:1444 */     return this._sheet.getLeftCol();
/*  863:     */   }
/*  864:     */   
/*  865:     */   public void showInPane(int toprow, int leftcol)
/*  866:     */   {
/*  867:1456 */     int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/*  868:1457 */     if (toprow > maxrow) {
/*  869:1457 */       throw new IllegalArgumentException("Maximum row number is " + maxrow);
/*  870:     */     }
/*  871:1459 */     showInPane((short)toprow, (short)leftcol);
/*  872:     */   }
/*  873:     */   
/*  874:     */   private void showInPane(short toprow, short leftcol)
/*  875:     */   {
/*  876:1469 */     this._sheet.setTopRow(toprow);
/*  877:1470 */     this._sheet.setLeftCol(leftcol);
/*  878:     */   }
/*  879:     */   
/*  880:     */   /**
/*  881:     */    * @deprecated
/*  882:     */    */
/*  883:     */   protected void shiftMerged(int startRow, int endRow, int n, boolean isRow)
/*  884:     */   {
/*  885:1483 */     RowShifter rowShifter = new HSSFRowShifter(this);
/*  886:1484 */     rowShifter.shiftMergedRegions(startRow, endRow, n);
/*  887:     */   }
/*  888:     */   
/*  889:     */   public void shiftRows(int startRow, int endRow, int n)
/*  890:     */   {
/*  891:1503 */     shiftRows(startRow, endRow, n, false, false);
/*  892:     */   }
/*  893:     */   
/*  894:     */   public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight)
/*  895:     */   {
/*  896:1525 */     shiftRows(startRow, endRow, n, copyRowHeight, resetOriginalRowHeight, true);
/*  897:     */   }
/*  898:     */   
/*  899:     */   private static int clip(int row)
/*  900:     */   {
/*  901:1534 */     return Math.min(Math.max(0, row), SpreadsheetVersion.EXCEL97.getLastRowIndex());
/*  902:     */   }
/*  903:     */   
/*  904:     */   public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight, boolean moveComments)
/*  905:     */   {
/*  906:1559 */     if (endRow < startRow) {
/*  907:1560 */       throw new IllegalArgumentException("startRow must be less than or equal to endRow. To shift rows up, use n<0.");
/*  908:     */     }
/*  909:     */     int inc;
/*  910:1562 */     if (n < 0)
/*  911:     */     {
/*  912:1563 */       int s = startRow;
/*  913:1564 */       inc = 1;
/*  914:     */     }
/*  915:     */     else
/*  916:     */     {
/*  917:     */       int inc;
/*  918:1565 */       if (n > 0)
/*  919:     */       {
/*  920:1566 */         int s = endRow;
/*  921:1567 */         inc = -1;
/*  922:     */       }
/*  923:     */       else
/*  924:     */       {
/*  925:     */         return;
/*  926:     */       }
/*  927:     */     }
/*  928:     */     int inc;
/*  929:     */     int s;
/*  930:1573 */     RowShifter rowShifter = new HSSFRowShifter(this);
/*  931:1582 */     if (moveComments)
/*  932:     */     {
/*  933:1583 */       HSSFPatriarch patriarch = createDrawingPatriarch();
/*  934:1584 */       for (HSSFShape shape : patriarch.getChildren()) {
/*  935:1585 */         if ((shape instanceof HSSFComment))
/*  936:     */         {
/*  937:1588 */           HSSFComment comment = (HSSFComment)shape;
/*  938:1589 */           int r = comment.getRow();
/*  939:1590 */           if ((startRow <= r) && (r <= endRow)) {
/*  940:1591 */             comment.setRow(clip(r + n));
/*  941:     */           }
/*  942:     */         }
/*  943:     */       }
/*  944:     */     }
/*  945:1597 */     rowShifter.shiftMergedRegions(startRow, endRow, n);
/*  946:     */     
/*  947:     */ 
/*  948:1600 */     this._sheet.getPageSettings().shiftRowBreaks(startRow, endRow, n);
/*  949:     */     
/*  950:     */ 
/*  951:1603 */     int firstOverwrittenRow = startRow + n;
/*  952:1604 */     int lastOverwrittenRow = endRow + n;
/*  953:1605 */     for (HSSFHyperlink link : getHyperlinkList())
/*  954:     */     {
/*  955:1607 */       int firstRow = link.getFirstRow();
/*  956:1608 */       int lastRow = link.getLastRow();
/*  957:1609 */       if ((firstOverwrittenRow <= firstRow) && (firstRow <= lastOverwrittenRow) && (lastOverwrittenRow <= lastRow) && (lastRow <= lastOverwrittenRow)) {
/*  958:1611 */         removeHyperlink(link);
/*  959:     */       }
/*  960:     */     }
/*  961:1615 */     for (int rowNum = s; (rowNum >= startRow) && (rowNum <= endRow) && (rowNum >= 0) && (rowNum < 65536); rowNum += inc)
/*  962:     */     {
/*  963:1616 */       HSSFRow row = getRow(rowNum);
/*  964:1620 */       if (row != null) {
/*  965:1620 */         notifyRowShifting(row);
/*  966:     */       }
/*  967:1622 */       HSSFRow row2Replace = getRow(rowNum + n);
/*  968:1623 */       if (row2Replace == null) {
/*  969:1624 */         row2Replace = createRow(rowNum + n);
/*  970:     */       }
/*  971:1632 */       row2Replace.removeAllCells();
/*  972:1636 */       if (row != null)
/*  973:     */       {
/*  974:1639 */         if (copyRowHeight) {
/*  975:1640 */           row2Replace.setHeight(row.getHeight());
/*  976:     */         }
/*  977:1642 */         if (resetOriginalRowHeight) {
/*  978:1643 */           row.setHeight((short)255);
/*  979:     */         }
/*  980:1648 */         for (Iterator<Cell> cells = row.cellIterator(); cells.hasNext();)
/*  981:     */         {
/*  982:1649 */           HSSFCell cell = (HSSFCell)cells.next();
/*  983:1650 */           HSSFHyperlink link = cell.getHyperlink();
/*  984:1651 */           row.removeCell(cell);
/*  985:1652 */           CellValueRecordInterface cellRecord = cell.getCellValueRecord();
/*  986:1653 */           cellRecord.setRow(rowNum + n);
/*  987:1654 */           row2Replace.createCellFromRecord(cellRecord);
/*  988:1655 */           this._sheet.addValueRecord(rowNum + n, cellRecord);
/*  989:1657 */           if (link != null)
/*  990:     */           {
/*  991:1658 */             link.setFirstRow(link.getFirstRow() + n);
/*  992:1659 */             link.setLastRow(link.getLastRow() + n);
/*  993:     */           }
/*  994:     */         }
/*  995:1663 */         row.removeAllCells();
/*  996:     */       }
/*  997:     */     }
/*  998:1667 */     if (n > 0)
/*  999:     */     {
/* 1000:1669 */       if (startRow == this._firstrow)
/* 1001:     */       {
/* 1002:1671 */         this._firstrow = Math.max(startRow + n, 0);
/* 1003:1672 */         for (int i = startRow + 1; i < startRow + n; i++) {
/* 1004:1673 */           if (getRow(i) != null)
/* 1005:     */           {
/* 1006:1674 */             this._firstrow = i;
/* 1007:1675 */             break;
/* 1008:     */           }
/* 1009:     */         }
/* 1010:     */       }
/* 1011:1679 */       if (endRow + n > this._lastrow) {
/* 1012:1680 */         this._lastrow = Math.min(endRow + n, SpreadsheetVersion.EXCEL97.getLastRowIndex());
/* 1013:     */       }
/* 1014:     */     }
/* 1015:     */     else
/* 1016:     */     {
/* 1017:1684 */       if (startRow + n < this._firstrow) {
/* 1018:1685 */         this._firstrow = Math.max(startRow + n, 0);
/* 1019:     */       }
/* 1020:1687 */       if (endRow == this._lastrow)
/* 1021:     */       {
/* 1022:1690 */         this._lastrow = Math.min(endRow + n, SpreadsheetVersion.EXCEL97.getLastRowIndex());
/* 1023:1691 */         for (int i = endRow - 1; i > endRow + n; i--) {
/* 1024:1692 */           if (getRow(i) != null)
/* 1025:     */           {
/* 1026:1693 */             this._lastrow = i;
/* 1027:1694 */             break;
/* 1028:     */           }
/* 1029:     */         }
/* 1030:     */       }
/* 1031:     */     }
/* 1032:1702 */     int sheetIndex = this._workbook.getSheetIndex(this);
/* 1033:1703 */     String sheetName = this._workbook.getSheetName(sheetIndex);
/* 1034:1704 */     short externSheetIndex = this._book.checkExternSheet(sheetIndex);
/* 1035:1705 */     FormulaShifter shifter = FormulaShifter.createForRowShift(externSheetIndex, sheetName, startRow, endRow, n, SpreadsheetVersion.EXCEL97);
/* 1036:     */     
/* 1037:1707 */     this._sheet.updateFormulasAfterCellShift(shifter, externSheetIndex);
/* 1038:     */     
/* 1039:1709 */     int nSheets = this._workbook.getNumberOfSheets();
/* 1040:1710 */     for (int i = 0; i < nSheets; i++)
/* 1041:     */     {
/* 1042:1711 */       InternalSheet otherSheet = this._workbook.getSheetAt(i).getSheet();
/* 1043:1712 */       if (otherSheet != this._sheet)
/* 1044:     */       {
/* 1045:1715 */         short otherExtSheetIx = this._book.checkExternSheet(i);
/* 1046:1716 */         otherSheet.updateFormulasAfterCellShift(shifter, otherExtSheetIx);
/* 1047:     */       }
/* 1048:     */     }
/* 1049:1718 */     this._workbook.getWorkbook().updateNamesAfterCellShift(shifter);
/* 1050:     */   }
/* 1051:     */   
/* 1052:     */   protected void insertChartRecords(List<Record> records)
/* 1053:     */   {
/* 1054:1722 */     int window2Loc = this._sheet.findFirstRecordLocBySid((short)574);
/* 1055:1723 */     this._sheet.getRecords().addAll(window2Loc, records);
/* 1056:     */   }
/* 1057:     */   
/* 1058:     */   private void notifyRowShifting(HSSFRow row)
/* 1059:     */   {
/* 1060:1727 */     String msg = "Row[rownum=" + row.getRowNum() + "] contains cell(s) included in a multi-cell array formula. " + "You cannot change part of an array.";
/* 1061:1729 */     for (Cell cell : row)
/* 1062:     */     {
/* 1063:1730 */       HSSFCell hcell = (HSSFCell)cell;
/* 1064:1731 */       if (hcell.isPartOfArrayFormulaGroup()) {
/* 1065:1732 */         hcell.notifyArrayFormulaChanging(msg);
/* 1066:     */       }
/* 1067:     */     }
/* 1068:     */   }
/* 1069:     */   
/* 1070:     */   public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
/* 1071:     */   {
/* 1072:1749 */     validateColumn(colSplit);
/* 1073:1750 */     validateRow(rowSplit);
/* 1074:1751 */     if (leftmostColumn < colSplit) {
/* 1075:1752 */       throw new IllegalArgumentException("leftmostColumn parameter must not be less than colSplit parameter");
/* 1076:     */     }
/* 1077:1753 */     if (topRow < rowSplit) {
/* 1078:1754 */       throw new IllegalArgumentException("topRow parameter must not be less than leftmostColumn parameter");
/* 1079:     */     }
/* 1080:1755 */     getSheet().createFreezePane(colSplit, rowSplit, topRow, leftmostColumn);
/* 1081:     */   }
/* 1082:     */   
/* 1083:     */   public void createFreezePane(int colSplit, int rowSplit)
/* 1084:     */   {
/* 1085:1768 */     createFreezePane(colSplit, rowSplit, colSplit, rowSplit);
/* 1086:     */   }
/* 1087:     */   
/* 1088:     */   public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane)
/* 1089:     */   {
/* 1090:1787 */     getSheet().createSplitPane(xSplitPos, ySplitPos, topRow, leftmostColumn, activePane);
/* 1091:     */   }
/* 1092:     */   
/* 1093:     */   public PaneInformation getPaneInformation()
/* 1094:     */   {
/* 1095:1797 */     return getSheet().getPaneInformation();
/* 1096:     */   }
/* 1097:     */   
/* 1098:     */   public void setDisplayGridlines(boolean show)
/* 1099:     */   {
/* 1100:1807 */     this._sheet.setDisplayGridlines(show);
/* 1101:     */   }
/* 1102:     */   
/* 1103:     */   public boolean isDisplayGridlines()
/* 1104:     */   {
/* 1105:1817 */     return this._sheet.isDisplayGridlines();
/* 1106:     */   }
/* 1107:     */   
/* 1108:     */   public void setDisplayFormulas(boolean show)
/* 1109:     */   {
/* 1110:1827 */     this._sheet.setDisplayFormulas(show);
/* 1111:     */   }
/* 1112:     */   
/* 1113:     */   public boolean isDisplayFormulas()
/* 1114:     */   {
/* 1115:1837 */     return this._sheet.isDisplayFormulas();
/* 1116:     */   }
/* 1117:     */   
/* 1118:     */   public void setDisplayRowColHeadings(boolean show)
/* 1119:     */   {
/* 1120:1847 */     this._sheet.setDisplayRowColHeadings(show);
/* 1121:     */   }
/* 1122:     */   
/* 1123:     */   public boolean isDisplayRowColHeadings()
/* 1124:     */   {
/* 1125:1857 */     return this._sheet.isDisplayRowColHeadings();
/* 1126:     */   }
/* 1127:     */   
/* 1128:     */   public void setRowBreak(int row)
/* 1129:     */   {
/* 1130:1873 */     validateRow(row);
/* 1131:1874 */     this._sheet.getPageSettings().setRowBreak(row, (short)0, (short)255);
/* 1132:     */   }
/* 1133:     */   
/* 1134:     */   public boolean isRowBroken(int row)
/* 1135:     */   {
/* 1136:1882 */     return this._sheet.getPageSettings().isRowBroken(row);
/* 1137:     */   }
/* 1138:     */   
/* 1139:     */   public void removeRowBreak(int row)
/* 1140:     */   {
/* 1141:1890 */     this._sheet.getPageSettings().removeRowBreak(row);
/* 1142:     */   }
/* 1143:     */   
/* 1144:     */   public int[] getRowBreaks()
/* 1145:     */   {
/* 1146:1899 */     return this._sheet.getPageSettings().getRowBreaks();
/* 1147:     */   }
/* 1148:     */   
/* 1149:     */   public int[] getColumnBreaks()
/* 1150:     */   {
/* 1151:1908 */     return this._sheet.getPageSettings().getColumnBreaks();
/* 1152:     */   }
/* 1153:     */   
/* 1154:     */   public void setColumnBreak(int column)
/* 1155:     */   {
/* 1156:1925 */     validateColumn((short)column);
/* 1157:1926 */     this._sheet.getPageSettings().setColumnBreak((short)column, (short)0, (short)SpreadsheetVersion.EXCEL97.getLastRowIndex());
/* 1158:     */   }
/* 1159:     */   
/* 1160:     */   public boolean isColumnBroken(int column)
/* 1161:     */   {
/* 1162:1937 */     return this._sheet.getPageSettings().isColumnBroken(column);
/* 1163:     */   }
/* 1164:     */   
/* 1165:     */   public void removeColumnBreak(int column)
/* 1166:     */   {
/* 1167:1947 */     this._sheet.getPageSettings().removeColumnBreak(column);
/* 1168:     */   }
/* 1169:     */   
/* 1170:     */   protected void validateRow(int row)
/* 1171:     */   {
/* 1172:1956 */     int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/* 1173:1957 */     if (row > maxrow) {
/* 1174:1957 */       throw new IllegalArgumentException("Maximum row number is " + maxrow);
/* 1175:     */     }
/* 1176:1958 */     if (row < 0) {
/* 1177:1958 */       throw new IllegalArgumentException("Minumum row number is 0");
/* 1178:     */     }
/* 1179:     */   }
/* 1180:     */   
/* 1181:     */   protected void validateColumn(int column)
/* 1182:     */   {
/* 1183:1967 */     int maxcol = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
/* 1184:1968 */     if (column > maxcol) {
/* 1185:1968 */       throw new IllegalArgumentException("Maximum column number is " + maxcol);
/* 1186:     */     }
/* 1187:1969 */     if (column < 0) {
/* 1188:1969 */       throw new IllegalArgumentException("Minimum column number is 0");
/* 1189:     */     }
/* 1190:     */   }
/* 1191:     */   
/* 1192:     */   public void dumpDrawingRecords(boolean fat, PrintWriter pw)
/* 1193:     */   {
/* 1194:1977 */     this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false);
/* 1195:     */     
/* 1196:1979 */     EscherAggregate r = (EscherAggregate)getSheet().findFirstRecordBySid((short)9876);
/* 1197:1980 */     List<EscherRecord> escherRecords = r.getEscherRecords();
/* 1198:1981 */     for (EscherRecord escherRecord : escherRecords) {
/* 1199:1982 */       if (fat) {
/* 1200:1983 */         pw.println(escherRecord);
/* 1201:     */       } else {
/* 1202:1985 */         escherRecord.display(pw, 0);
/* 1203:     */       }
/* 1204:     */     }
/* 1205:1988 */     pw.flush();
/* 1206:     */   }
/* 1207:     */   
/* 1208:     */   public EscherAggregate getDrawingEscherAggregate()
/* 1209:     */   {
/* 1210:1996 */     this._book.findDrawingGroup();
/* 1211:2000 */     if (this._book.getDrawingManager() == null) {
/* 1212:2001 */       return null;
/* 1213:     */     }
/* 1214:2004 */     int found = this._sheet.aggregateDrawingRecords(this._book.getDrawingManager(), false);
/* 1215:2007 */     if (found == -1) {
/* 1216:2009 */       return null;
/* 1217:     */     }
/* 1218:2013 */     return (EscherAggregate)this._sheet.findFirstRecordBySid((short)9876);
/* 1219:     */   }
/* 1220:     */   
/* 1221:     */   public HSSFPatriarch getDrawingPatriarch()
/* 1222:     */   {
/* 1223:2023 */     this._patriarch = getPatriarch(false);
/* 1224:2024 */     return this._patriarch;
/* 1225:     */   }
/* 1226:     */   
/* 1227:     */   public HSSFPatriarch createDrawingPatriarch()
/* 1228:     */   {
/* 1229:2037 */     this._patriarch = getPatriarch(true);
/* 1230:2038 */     return this._patriarch;
/* 1231:     */   }
/* 1232:     */   
/* 1233:     */   private HSSFPatriarch getPatriarch(boolean createIfMissing)
/* 1234:     */   {
/* 1235:2042 */     if (this._patriarch != null) {
/* 1236:2043 */       return this._patriarch;
/* 1237:     */     }
/* 1238:2045 */     DrawingManager2 dm = this._book.findDrawingGroup();
/* 1239:2046 */     if (null == dm)
/* 1240:     */     {
/* 1241:2047 */       if (!createIfMissing) {
/* 1242:2048 */         return null;
/* 1243:     */       }
/* 1244:2050 */       this._book.createDrawingGroup();
/* 1245:2051 */       dm = this._book.getDrawingManager();
/* 1246:     */     }
/* 1247:2054 */     EscherAggregate agg = (EscherAggregate)this._sheet.findFirstRecordBySid((short)9876);
/* 1248:2055 */     if (null == agg)
/* 1249:     */     {
/* 1250:2056 */       int pos = this._sheet.aggregateDrawingRecords(dm, false);
/* 1251:2057 */       if (-1 == pos)
/* 1252:     */       {
/* 1253:2058 */         if (createIfMissing)
/* 1254:     */         {
/* 1255:2059 */           pos = this._sheet.aggregateDrawingRecords(dm, true);
/* 1256:2060 */           agg = (EscherAggregate)this._sheet.getRecords().get(pos);
/* 1257:2061 */           HSSFPatriarch patriarch = new HSSFPatriarch(this, agg);
/* 1258:2062 */           patriarch.afterCreate();
/* 1259:2063 */           return patriarch;
/* 1260:     */         }
/* 1261:2065 */         return null;
/* 1262:     */       }
/* 1263:2068 */       agg = (EscherAggregate)this._sheet.getRecords().get(pos);
/* 1264:     */     }
/* 1265:2070 */     return new HSSFPatriarch(this, agg);
/* 1266:     */   }
/* 1267:     */   
/* 1268:     */   public void setColumnGroupCollapsed(int columnNumber, boolean collapsed)
/* 1269:     */   {
/* 1270:2081 */     this._sheet.setColumnGroupCollapsed(columnNumber, collapsed);
/* 1271:     */   }
/* 1272:     */   
/* 1273:     */   public void groupColumn(int fromColumn, int toColumn)
/* 1274:     */   {
/* 1275:2092 */     this._sheet.groupColumnRange(fromColumn, toColumn, true);
/* 1276:     */   }
/* 1277:     */   
/* 1278:     */   public void ungroupColumn(int fromColumn, int toColumn)
/* 1279:     */   {
/* 1280:2097 */     this._sheet.groupColumnRange(fromColumn, toColumn, false);
/* 1281:     */   }
/* 1282:     */   
/* 1283:     */   public void groupRow(int fromRow, int toRow)
/* 1284:     */   {
/* 1285:2108 */     this._sheet.groupRowRange(fromRow, toRow, true);
/* 1286:     */   }
/* 1287:     */   
/* 1288:     */   public void ungroupRow(int fromRow, int toRow)
/* 1289:     */   {
/* 1290:2113 */     this._sheet.groupRowRange(fromRow, toRow, false);
/* 1291:     */   }
/* 1292:     */   
/* 1293:     */   public void setRowGroupCollapsed(int rowIndex, boolean collapse)
/* 1294:     */   {
/* 1295:2118 */     if (collapse) {
/* 1296:2119 */       this._sheet.getRowsAggregate().collapseRow(rowIndex);
/* 1297:     */     } else {
/* 1298:2121 */       this._sheet.getRowsAggregate().expandRow(rowIndex);
/* 1299:     */     }
/* 1300:     */   }
/* 1301:     */   
/* 1302:     */   public void setDefaultColumnStyle(int column, CellStyle style)
/* 1303:     */   {
/* 1304:2133 */     this._sheet.setDefaultColumnStyle(column, ((HSSFCellStyle)style).getIndex());
/* 1305:     */   }
/* 1306:     */   
/* 1307:     */   public void autoSizeColumn(int column)
/* 1308:     */   {
/* 1309:2147 */     autoSizeColumn(column, false);
/* 1310:     */   }
/* 1311:     */   
/* 1312:     */   public void autoSizeColumn(int column, boolean useMergedCells)
/* 1313:     */   {
/* 1314:2165 */     double width = SheetUtil.getColumnWidth(this, column, useMergedCells);
/* 1315:2167 */     if (width != -1.0D)
/* 1316:     */     {
/* 1317:2168 */       width *= 256.0D;
/* 1318:2169 */       int maxColumnWidth = 65280;
/* 1319:2170 */       if (width > maxColumnWidth) {
/* 1320:2171 */         width = maxColumnWidth;
/* 1321:     */       }
/* 1322:2173 */       setColumnWidth(column, (int)width);
/* 1323:     */     }
/* 1324:     */   }
/* 1325:     */   
/* 1326:     */   public HSSFComment getCellComment(CellAddress ref)
/* 1327:     */   {
/* 1328:2184 */     return findCellComment(ref.getRow(), ref.getColumn());
/* 1329:     */   }
/* 1330:     */   
/* 1331:     */   public HSSFHyperlink getHyperlink(int row, int column)
/* 1332:     */   {
/* 1333:2196 */     for (RecordBase rec : this._sheet.getRecords()) {
/* 1334:2197 */       if ((rec instanceof HyperlinkRecord))
/* 1335:     */       {
/* 1336:2198 */         HyperlinkRecord link = (HyperlinkRecord)rec;
/* 1337:2199 */         if ((link.getFirstColumn() == column) && (link.getFirstRow() == row)) {
/* 1338:2200 */           return new HSSFHyperlink(link);
/* 1339:     */         }
/* 1340:     */       }
/* 1341:     */     }
/* 1342:2204 */     return null;
/* 1343:     */   }
/* 1344:     */   
/* 1345:     */   public HSSFHyperlink getHyperlink(CellAddress addr)
/* 1346:     */   {
/* 1347:2216 */     return getHyperlink(addr.getRow(), addr.getColumn());
/* 1348:     */   }
/* 1349:     */   
/* 1350:     */   public List<HSSFHyperlink> getHyperlinkList()
/* 1351:     */   {
/* 1352:2226 */     List<HSSFHyperlink> hyperlinkList = new ArrayList();
/* 1353:2227 */     for (RecordBase rec : this._sheet.getRecords()) {
/* 1354:2228 */       if ((rec instanceof HyperlinkRecord))
/* 1355:     */       {
/* 1356:2229 */         HyperlinkRecord link = (HyperlinkRecord)rec;
/* 1357:2230 */         hyperlinkList.add(new HSSFHyperlink(link));
/* 1358:     */       }
/* 1359:     */     }
/* 1360:2233 */     return hyperlinkList;
/* 1361:     */   }
/* 1362:     */   
/* 1363:     */   protected void removeHyperlink(HSSFHyperlink link)
/* 1364:     */   {
/* 1365:2243 */     removeHyperlink(link.record);
/* 1366:     */   }
/* 1367:     */   
/* 1368:     */   protected void removeHyperlink(HyperlinkRecord link)
/* 1369:     */   {
/* 1370:2252 */     for (Iterator<RecordBase> it = this._sheet.getRecords().iterator(); it.hasNext();)
/* 1371:     */     {
/* 1372:2253 */       RecordBase rec = (RecordBase)it.next();
/* 1373:2254 */       if ((rec instanceof HyperlinkRecord))
/* 1374:     */       {
/* 1375:2255 */         HyperlinkRecord recLink = (HyperlinkRecord)rec;
/* 1376:2256 */         if (link == recLink)
/* 1377:     */         {
/* 1378:2257 */           it.remove();
/* 1379:     */           
/* 1380:2259 */           return;
/* 1381:     */         }
/* 1382:     */       }
/* 1383:     */     }
/* 1384:     */   }
/* 1385:     */   
/* 1386:     */   public HSSFSheetConditionalFormatting getSheetConditionalFormatting()
/* 1387:     */   {
/* 1388:2267 */     return new HSSFSheetConditionalFormatting(this);
/* 1389:     */   }
/* 1390:     */   
/* 1391:     */   public String getSheetName()
/* 1392:     */   {
/* 1393:2278 */     HSSFWorkbook wb = getWorkbook();
/* 1394:2279 */     int idx = wb.getSheetIndex(this);
/* 1395:2280 */     return wb.getSheetName(idx);
/* 1396:     */   }
/* 1397:     */   
/* 1398:     */   private CellRange<HSSFCell> getCellRange(CellRangeAddress range)
/* 1399:     */   {
/* 1400:2287 */     int firstRow = range.getFirstRow();
/* 1401:2288 */     int firstColumn = range.getFirstColumn();
/* 1402:2289 */     int lastRow = range.getLastRow();
/* 1403:2290 */     int lastColumn = range.getLastColumn();
/* 1404:2291 */     int height = lastRow - firstRow + 1;
/* 1405:2292 */     int width = lastColumn - firstColumn + 1;
/* 1406:2293 */     List<HSSFCell> temp = new ArrayList(height * width);
/* 1407:2294 */     for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
/* 1408:2295 */       for (int colIn = firstColumn; colIn <= lastColumn; colIn++)
/* 1409:     */       {
/* 1410:2296 */         HSSFRow row = getRow(rowIn);
/* 1411:2297 */         if (row == null) {
/* 1412:2298 */           row = createRow(rowIn);
/* 1413:     */         }
/* 1414:2300 */         HSSFCell cell = row.getCell(colIn);
/* 1415:2301 */         if (cell == null) {
/* 1416:2302 */           cell = row.createCell(colIn);
/* 1417:     */         }
/* 1418:2304 */         temp.add(cell);
/* 1419:     */       }
/* 1420:     */     }
/* 1421:2307 */     return SSCellRange.create(firstRow, firstColumn, height, width, temp, HSSFCell.class);
/* 1422:     */   }
/* 1423:     */   
/* 1424:     */   public CellRange<HSSFCell> setArrayFormula(String formula, CellRangeAddress range)
/* 1425:     */   {
/* 1426:2313 */     int sheetIndex = this._workbook.getSheetIndex(this);
/* 1427:2314 */     Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._workbook, FormulaType.ARRAY, sheetIndex);
/* 1428:2315 */     CellRange<HSSFCell> cells = getCellRange(range);
/* 1429:2317 */     for (HSSFCell c : cells) {
/* 1430:2318 */       c.setCellArrayFormula(range);
/* 1431:     */     }
/* 1432:2320 */     HSSFCell mainArrayFormulaCell = (HSSFCell)cells.getTopLeftCell();
/* 1433:2321 */     FormulaRecordAggregate agg = (FormulaRecordAggregate)mainArrayFormulaCell.getCellValueRecord();
/* 1434:2322 */     agg.setArrayFormula(range, ptgs);
/* 1435:2323 */     return cells;
/* 1436:     */   }
/* 1437:     */   
/* 1438:     */   public CellRange<HSSFCell> removeArrayFormula(Cell cell)
/* 1439:     */   {
/* 1440:2328 */     if (cell.getSheet() != this) {
/* 1441:2329 */       throw new IllegalArgumentException("Specified cell does not belong to this sheet.");
/* 1442:     */     }
/* 1443:2331 */     CellValueRecordInterface rec = ((HSSFCell)cell).getCellValueRecord();
/* 1444:2332 */     if (!(rec instanceof FormulaRecordAggregate))
/* 1445:     */     {
/* 1446:2333 */       String ref = new CellReference(cell).formatAsString();
/* 1447:2334 */       throw new IllegalArgumentException("Cell " + ref + " is not part of an array formula.");
/* 1448:     */     }
/* 1449:2336 */     FormulaRecordAggregate fra = (FormulaRecordAggregate)rec;
/* 1450:2337 */     CellRangeAddress range = fra.removeArrayFormula(cell.getRowIndex(), cell.getColumnIndex());
/* 1451:     */     
/* 1452:2339 */     CellRange<HSSFCell> result = getCellRange(range);
/* 1453:2341 */     for (Cell c : result) {
/* 1454:2342 */       c.setCellType(CellType.BLANK);
/* 1455:     */     }
/* 1456:2344 */     return result;
/* 1457:     */   }
/* 1458:     */   
/* 1459:     */   public DataValidationHelper getDataValidationHelper()
/* 1460:     */   {
/* 1461:2349 */     return new HSSFDataValidationHelper(this);
/* 1462:     */   }
/* 1463:     */   
/* 1464:     */   public HSSFAutoFilter setAutoFilter(CellRangeAddress range)
/* 1465:     */   {
/* 1466:2354 */     InternalWorkbook workbook = this._workbook.getWorkbook();
/* 1467:2355 */     int sheetIndex = this._workbook.getSheetIndex(this);
/* 1468:     */     
/* 1469:2357 */     NameRecord name = workbook.getSpecificBuiltinRecord((byte)13, sheetIndex + 1);
/* 1470:2359 */     if (name == null) {
/* 1471:2360 */       name = workbook.createBuiltInName((byte)13, sheetIndex + 1);
/* 1472:     */     }
/* 1473:2363 */     int firstRow = range.getFirstRow();
/* 1474:2366 */     if (firstRow == -1) {
/* 1475:2367 */       firstRow = 0;
/* 1476:     */     }
/* 1477:2371 */     Area3DPtg ptg = new Area3DPtg(firstRow, range.getLastRow(), range.getFirstColumn(), range.getLastColumn(), false, false, false, false, sheetIndex);
/* 1478:     */     
/* 1479:     */ 
/* 1480:2374 */     name.setNameDefinition(new Ptg[] { ptg });
/* 1481:     */     
/* 1482:2376 */     AutoFilterInfoRecord r = new AutoFilterInfoRecord();
/* 1483:     */     
/* 1484:2378 */     int numcols = 1 + range.getLastColumn() - range.getFirstColumn();
/* 1485:2379 */     r.setNumEntries((short)numcols);
/* 1486:2380 */     int idx = this._sheet.findFirstRecordLocBySid((short)512);
/* 1487:2381 */     this._sheet.getRecords().add(idx, r);
/* 1488:     */     
/* 1489:     */ 
/* 1490:2384 */     HSSFPatriarch p = createDrawingPatriarch();
/* 1491:2385 */     int firstColumn = range.getFirstColumn();
/* 1492:2386 */     int lastColumn = range.getLastColumn();
/* 1493:2387 */     for (int col = firstColumn; col <= lastColumn; col++) {
/* 1494:2388 */       p.createComboBox(new HSSFClientAnchor(0, 0, 0, 0, (short)col, firstRow, (short)(col + 1), firstRow + 1));
/* 1495:     */     }
/* 1496:2392 */     return new HSSFAutoFilter(this);
/* 1497:     */   }
/* 1498:     */   
/* 1499:     */   protected HSSFComment findCellComment(int row, int column)
/* 1500:     */   {
/* 1501:2396 */     HSSFPatriarch patriarch = getDrawingPatriarch();
/* 1502:2397 */     if (null == patriarch) {
/* 1503:2398 */       patriarch = createDrawingPatriarch();
/* 1504:     */     }
/* 1505:2400 */     return lookForComment(patriarch, row, column);
/* 1506:     */   }
/* 1507:     */   
/* 1508:     */   private HSSFComment lookForComment(HSSFShapeContainer container, int row, int column)
/* 1509:     */   {
/* 1510:2404 */     for (Object object : container.getChildren())
/* 1511:     */     {
/* 1512:2405 */       HSSFShape shape = (HSSFShape)object;
/* 1513:2406 */       if ((shape instanceof HSSFShapeGroup))
/* 1514:     */       {
/* 1515:2407 */         HSSFShape res = lookForComment((HSSFShapeContainer)shape, row, column);
/* 1516:2408 */         if (null != res) {
/* 1517:2409 */           return (HSSFComment)res;
/* 1518:     */         }
/* 1519:     */       }
/* 1520:2413 */       else if ((shape instanceof HSSFComment))
/* 1521:     */       {
/* 1522:2414 */         HSSFComment comment = (HSSFComment)shape;
/* 1523:2415 */         if ((comment.hasPosition()) && (comment.getColumn() == column) && (comment.getRow() == row)) {
/* 1524:2416 */           return comment;
/* 1525:     */         }
/* 1526:     */       }
/* 1527:     */     }
/* 1528:2420 */     return null;
/* 1529:     */   }
/* 1530:     */   
/* 1531:     */   public Map<CellAddress, HSSFComment> getCellComments()
/* 1532:     */   {
/* 1533:2430 */     HSSFPatriarch patriarch = getDrawingPatriarch();
/* 1534:2431 */     if (null == patriarch) {
/* 1535:2432 */       patriarch = createDrawingPatriarch();
/* 1536:     */     }
/* 1537:2435 */     Map<CellAddress, HSSFComment> locations = new TreeMap();
/* 1538:2436 */     findCellCommentLocations(patriarch, locations);
/* 1539:2437 */     return locations;
/* 1540:     */   }
/* 1541:     */   
/* 1542:     */   private void findCellCommentLocations(HSSFShapeContainer container, Map<CellAddress, HSSFComment> locations)
/* 1543:     */   {
/* 1544:2446 */     for (Object object : container.getChildren())
/* 1545:     */     {
/* 1546:2447 */       HSSFShape shape = (HSSFShape)object;
/* 1547:2448 */       if ((shape instanceof HSSFShapeGroup))
/* 1548:     */       {
/* 1549:2449 */         findCellCommentLocations((HSSFShapeGroup)shape, locations);
/* 1550:     */       }
/* 1551:2452 */       else if ((shape instanceof HSSFComment))
/* 1552:     */       {
/* 1553:2453 */         HSSFComment comment = (HSSFComment)shape;
/* 1554:2454 */         if (comment.hasPosition()) {
/* 1555:2455 */           locations.put(new CellAddress(comment.getRow(), comment.getColumn()), comment);
/* 1556:     */         }
/* 1557:     */       }
/* 1558:     */     }
/* 1559:     */   }
/* 1560:     */   
/* 1561:     */   public CellRangeAddress getRepeatingRows()
/* 1562:     */   {
/* 1563:2463 */     return getRepeatingRowsOrColums(true);
/* 1564:     */   }
/* 1565:     */   
/* 1566:     */   public CellRangeAddress getRepeatingColumns()
/* 1567:     */   {
/* 1568:2468 */     return getRepeatingRowsOrColums(false);
/* 1569:     */   }
/* 1570:     */   
/* 1571:     */   public void setRepeatingRows(CellRangeAddress rowRangeRef)
/* 1572:     */   {
/* 1573:2473 */     CellRangeAddress columnRangeRef = getRepeatingColumns();
/* 1574:2474 */     setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
/* 1575:     */   }
/* 1576:     */   
/* 1577:     */   public void setRepeatingColumns(CellRangeAddress columnRangeRef)
/* 1578:     */   {
/* 1579:2479 */     CellRangeAddress rowRangeRef = getRepeatingRows();
/* 1580:2480 */     setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
/* 1581:     */   }
/* 1582:     */   
/* 1583:     */   private void setRepeatingRowsAndColumns(CellRangeAddress rowDef, CellRangeAddress colDef)
/* 1584:     */   {
/* 1585:2486 */     int sheetIndex = this._workbook.getSheetIndex(this);
/* 1586:2487 */     int maxRowIndex = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/* 1587:2488 */     int maxColIndex = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
/* 1588:     */     
/* 1589:2490 */     int col1 = -1;
/* 1590:2491 */     int col2 = -1;
/* 1591:2492 */     int row1 = -1;
/* 1592:2493 */     int row2 = -1;
/* 1593:2495 */     if (rowDef != null)
/* 1594:     */     {
/* 1595:2496 */       row1 = rowDef.getFirstRow();
/* 1596:2497 */       row2 = rowDef.getLastRow();
/* 1597:2498 */       if (((row1 == -1) && (row2 != -1)) || (row1 > row2) || (row1 < 0) || (row1 > maxRowIndex) || (row2 < 0) || (row2 > maxRowIndex)) {
/* 1598:2501 */         throw new IllegalArgumentException("Invalid row range specification");
/* 1599:     */       }
/* 1600:     */     }
/* 1601:2504 */     if (colDef != null)
/* 1602:     */     {
/* 1603:2505 */       col1 = colDef.getFirstColumn();
/* 1604:2506 */       col2 = colDef.getLastColumn();
/* 1605:2507 */       if (((col1 == -1) && (col2 != -1)) || (col1 > col2) || (col1 < 0) || (col1 > maxColIndex) || (col2 < 0) || (col2 > maxColIndex)) {
/* 1606:2510 */         throw new IllegalArgumentException("Invalid column range specification");
/* 1607:     */       }
/* 1608:     */     }
/* 1609:2514 */     short externSheetIndex = this._workbook.getWorkbook().checkExternSheet(sheetIndex);
/* 1610:     */     
/* 1611:     */ 
/* 1612:2517 */     boolean setBoth = (rowDef != null) && (colDef != null);
/* 1613:2518 */     boolean removeAll = (rowDef == null) && (colDef == null);
/* 1614:     */     
/* 1615:2520 */     HSSFName name = this._workbook.getBuiltInName((byte)7, sheetIndex);
/* 1616:2522 */     if (removeAll)
/* 1617:     */     {
/* 1618:2523 */       if (name != null) {
/* 1619:2524 */         this._workbook.removeName(name);
/* 1620:     */       }
/* 1621:2526 */       return;
/* 1622:     */     }
/* 1623:2528 */     if (name == null) {
/* 1624:2529 */       name = this._workbook.createBuiltInName((byte)7, sheetIndex);
/* 1625:     */     }
/* 1626:2533 */     List<Ptg> ptgList = new ArrayList();
/* 1627:2534 */     if (setBoth)
/* 1628:     */     {
/* 1629:2535 */       int exprsSize = 23;
/* 1630:2536 */       ptgList.add(new MemFuncPtg(23));
/* 1631:     */     }
/* 1632:2538 */     if (colDef != null)
/* 1633:     */     {
/* 1634:2539 */       Area3DPtg colArea = new Area3DPtg(0, maxRowIndex, col1, col2, false, false, false, false, externSheetIndex);
/* 1635:     */       
/* 1636:2541 */       ptgList.add(colArea);
/* 1637:     */     }
/* 1638:2543 */     if (rowDef != null)
/* 1639:     */     {
/* 1640:2544 */       Area3DPtg rowArea = new Area3DPtg(row1, row2, 0, maxColIndex, false, false, false, false, externSheetIndex);
/* 1641:     */       
/* 1642:2546 */       ptgList.add(rowArea);
/* 1643:     */     }
/* 1644:2548 */     if (setBoth) {
/* 1645:2549 */       ptgList.add(UnionPtg.instance);
/* 1646:     */     }
/* 1647:2552 */     Ptg[] ptgs = new Ptg[ptgList.size()];
/* 1648:2553 */     ptgList.toArray(ptgs);
/* 1649:2554 */     name.setNameDefinition(ptgs);
/* 1650:     */     
/* 1651:2556 */     HSSFPrintSetup printSetup = getPrintSetup();
/* 1652:2557 */     printSetup.setValidSettings(false);
/* 1653:2558 */     setActive(true);
/* 1654:     */   }
/* 1655:     */   
/* 1656:     */   private CellRangeAddress getRepeatingRowsOrColums(boolean rows)
/* 1657:     */   {
/* 1658:2563 */     NameRecord rec = getBuiltinNameRecord((byte)7);
/* 1659:2564 */     if (rec == null) {
/* 1660:2565 */       return null;
/* 1661:     */     }
/* 1662:2568 */     Ptg[] nameDefinition = rec.getNameDefinition();
/* 1663:2569 */     if (nameDefinition == null) {
/* 1664:2570 */       return null;
/* 1665:     */     }
/* 1666:2573 */     int maxRowIndex = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/* 1667:2574 */     int maxColIndex = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
/* 1668:2576 */     for (Ptg ptg : nameDefinition) {
/* 1669:2578 */       if ((ptg instanceof Area3DPtg))
/* 1670:     */       {
/* 1671:2579 */         Area3DPtg areaPtg = (Area3DPtg)ptg;
/* 1672:2581 */         if ((areaPtg.getFirstColumn() == 0) && (areaPtg.getLastColumn() == maxColIndex))
/* 1673:     */         {
/* 1674:2583 */           if (rows) {
/* 1675:2584 */             return new CellRangeAddress(areaPtg.getFirstRow(), areaPtg.getLastRow(), -1, -1);
/* 1676:     */           }
/* 1677:     */         }
/* 1678:2587 */         else if ((areaPtg.getFirstRow() == 0) && (areaPtg.getLastRow() == maxRowIndex)) {
/* 1679:2589 */           if (!rows) {
/* 1680:2590 */             return new CellRangeAddress(-1, -1, areaPtg.getFirstColumn(), areaPtg.getLastColumn());
/* 1681:     */           }
/* 1682:     */         }
/* 1683:     */       }
/* 1684:     */     }
/* 1685:2599 */     return null;
/* 1686:     */   }
/* 1687:     */   
/* 1688:     */   private NameRecord getBuiltinNameRecord(byte builtinCode)
/* 1689:     */   {
/* 1690:2604 */     int sheetIndex = this._workbook.getSheetIndex(this);
/* 1691:2605 */     int recIndex = this._workbook.findExistingBuiltinNameRecordIdx(sheetIndex, builtinCode);
/* 1692:2607 */     if (recIndex == -1) {
/* 1693:2608 */       return null;
/* 1694:     */     }
/* 1695:2610 */     return this._workbook.getNameRecord(recIndex);
/* 1696:     */   }
/* 1697:     */   
/* 1698:     */   public int getColumnOutlineLevel(int columnIndex)
/* 1699:     */   {
/* 1700:2620 */     return this._sheet.getColumnOutlineLevel(columnIndex);
/* 1701:     */   }
/* 1702:     */   
/* 1703:     */   public CellAddress getActiveCell()
/* 1704:     */   {
/* 1705:2628 */     int row = this._sheet.getActiveCellRow();
/* 1706:2629 */     int col = this._sheet.getActiveCellCol();
/* 1707:2630 */     return new CellAddress(row, col);
/* 1708:     */   }
/* 1709:     */   
/* 1710:     */   public void setActiveCell(CellAddress address)
/* 1711:     */   {
/* 1712:2638 */     int row = address.getRow();
/* 1713:2639 */     short col = (short)address.getColumn();
/* 1714:2640 */     this._sheet.setActiveCellRow(row);
/* 1715:2641 */     this._sheet.setActiveCellCol(col);
/* 1716:     */   }
/* 1717:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFSheet
 * JD-Core Version:    0.7.0.1
 */