/*    1:     */ package org.apache.poi.xssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.text.DateFormat;
/*    4:     */ import java.text.SimpleDateFormat;
/*    5:     */ import java.util.Calendar;
/*    6:     */ import java.util.Date;
/*    7:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*    8:     */ import org.apache.poi.ss.formula.FormulaParser;
/*    9:     */ import org.apache.poi.ss.formula.FormulaRenderer;
/*   10:     */ import org.apache.poi.ss.formula.FormulaType;
/*   11:     */ import org.apache.poi.ss.formula.SharedFormula;
/*   12:     */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   13:     */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   14:     */ import org.apache.poi.ss.usermodel.Cell;
/*   15:     */ import org.apache.poi.ss.usermodel.CellCopyPolicy;
/*   16:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   17:     */ import org.apache.poi.ss.usermodel.CellType;
/*   18:     */ import org.apache.poi.ss.usermodel.Comment;
/*   19:     */ import org.apache.poi.ss.usermodel.DateUtil;
/*   20:     */ import org.apache.poi.ss.usermodel.FormulaError;
/*   21:     */ import org.apache.poi.ss.usermodel.Hyperlink;
/*   22:     */ import org.apache.poi.ss.usermodel.RichTextString;
/*   23:     */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*   24:     */ import org.apache.poi.ss.util.CellAddress;
/*   25:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   26:     */ import org.apache.poi.ss.util.CellReference;
/*   27:     */ import org.apache.poi.util.Internal;
/*   28:     */ import org.apache.poi.util.LocaleUtil;
/*   29:     */ import org.apache.poi.util.Removal;
/*   30:     */ import org.apache.poi.xssf.model.CommentsTable;
/*   31:     */ import org.apache.poi.xssf.model.SharedStringsTable;
/*   32:     */ import org.apache.poi.xssf.model.StylesTable;
/*   33:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
/*   34:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell.Factory;
/*   35:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
/*   36:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula.Factory;
/*   37:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;
/*   38:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
/*   39:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType.Enum;
/*   40:     */ 
/*   41:     */ public final class XSSFCell
/*   42:     */   implements Cell
/*   43:     */ {
/*   44:     */   private static final String FALSE_AS_STRING = "0";
/*   45:     */   private static final String TRUE_AS_STRING = "1";
/*   46:     */   private static final String FALSE = "FALSE";
/*   47:     */   private static final String TRUE = "TRUE";
/*   48:     */   private CTCell _cell;
/*   49:     */   private final XSSFRow _row;
/*   50:     */   private int _cellNum;
/*   51:     */   private SharedStringsTable _sharedStringSource;
/*   52:     */   private StylesTable _stylesSource;
/*   53:     */   
/*   54:     */   protected XSSFCell(XSSFRow row, CTCell cell)
/*   55:     */   {
/*   56: 112 */     this._cell = cell;
/*   57: 113 */     this._row = row;
/*   58: 114 */     if (cell.getR() != null)
/*   59:     */     {
/*   60: 115 */       this._cellNum = new CellReference(cell.getR()).getCol();
/*   61:     */     }
/*   62:     */     else
/*   63:     */     {
/*   64: 117 */       int prevNum = row.getLastCellNum();
/*   65: 118 */       if (prevNum != -1) {
/*   66: 119 */         this._cellNum = (row.getCell(prevNum - 1, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getColumnIndex() + 1);
/*   67:     */       }
/*   68:     */     }
/*   69: 122 */     this._sharedStringSource = row.getSheet().getWorkbook().getSharedStringSource();
/*   70: 123 */     this._stylesSource = row.getSheet().getWorkbook().getStylesSource();
/*   71:     */   }
/*   72:     */   
/*   73:     */   @Internal
/*   74:     */   public void copyCellFrom(Cell srcCell, CellCopyPolicy policy)
/*   75:     */   {
/*   76: 140 */     if (policy.isCopyCellValue()) {
/*   77: 141 */       if (srcCell != null)
/*   78:     */       {
/*   79: 142 */         CellType copyCellType = srcCell.getCellTypeEnum();
/*   80: 143 */         if ((copyCellType == CellType.FORMULA) && (!policy.isCopyCellFormula())) {
/*   81: 146 */           copyCellType = srcCell.getCachedFormulaResultTypeEnum();
/*   82:     */         }
/*   83: 148 */         switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[copyCellType.ordinal()])
/*   84:     */         {
/*   85:     */         case 1: 
/*   86: 151 */           if (DateUtil.isCellDateFormatted(srcCell)) {
/*   87: 152 */             setCellValue(srcCell.getDateCellValue());
/*   88:     */           } else {
/*   89: 155 */             setCellValue(srcCell.getNumericCellValue());
/*   90:     */           }
/*   91: 157 */           break;
/*   92:     */         case 2: 
/*   93: 159 */           setCellValue(srcCell.getStringCellValue());
/*   94: 160 */           break;
/*   95:     */         case 3: 
/*   96: 162 */           setCellFormula(srcCell.getCellFormula());
/*   97: 163 */           break;
/*   98:     */         case 4: 
/*   99: 165 */           setBlank();
/*  100: 166 */           break;
/*  101:     */         case 5: 
/*  102: 168 */           setCellValue(srcCell.getBooleanCellValue());
/*  103: 169 */           break;
/*  104:     */         case 6: 
/*  105: 171 */           setCellErrorValue(srcCell.getErrorCellValue());
/*  106: 172 */           break;
/*  107:     */         default: 
/*  108: 175 */           throw new IllegalArgumentException("Invalid cell type " + srcCell.getCellTypeEnum());
/*  109:     */         }
/*  110:     */       }
/*  111:     */       else
/*  112:     */       {
/*  113: 178 */         setBlank();
/*  114:     */       }
/*  115:     */     }
/*  116: 183 */     if (policy.isCopyCellStyle()) {
/*  117: 184 */       setCellStyle(srcCell == null ? null : srcCell.getCellStyle());
/*  118:     */     }
/*  119: 187 */     Hyperlink srcHyperlink = srcCell == null ? null : srcCell.getHyperlink();
/*  120: 189 */     if (policy.isMergeHyperlink())
/*  121:     */     {
/*  122: 191 */       if (srcHyperlink != null) {
/*  123: 192 */         setHyperlink(new XSSFHyperlink(srcHyperlink));
/*  124:     */       }
/*  125:     */     }
/*  126: 194 */     else if (policy.isCopyHyperlink()) {
/*  127: 197 */       setHyperlink(srcHyperlink == null ? null : new XSSFHyperlink(srcHyperlink));
/*  128:     */     }
/*  129:     */   }
/*  130:     */   
/*  131:     */   protected SharedStringsTable getSharedStringSource()
/*  132:     */   {
/*  133: 205 */     return this._sharedStringSource;
/*  134:     */   }
/*  135:     */   
/*  136:     */   protected StylesTable getStylesSource()
/*  137:     */   {
/*  138: 212 */     return this._stylesSource;
/*  139:     */   }
/*  140:     */   
/*  141:     */   public XSSFSheet getSheet()
/*  142:     */   {
/*  143: 222 */     return getRow().getSheet();
/*  144:     */   }
/*  145:     */   
/*  146:     */   public XSSFRow getRow()
/*  147:     */   {
/*  148: 232 */     return this._row;
/*  149:     */   }
/*  150:     */   
/*  151:     */   public boolean getBooleanCellValue()
/*  152:     */   {
/*  153: 246 */     CellType cellType = getCellTypeEnum();
/*  154: 247 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  155:     */     {
/*  156:     */     case 4: 
/*  157: 249 */       return false;
/*  158:     */     case 5: 
/*  159: 251 */       return (this._cell.isSetV()) && ("1".equals(this._cell.getV()));
/*  160:     */     case 3: 
/*  161: 254 */       return (this._cell.isSetV()) && ("1".equals(this._cell.getV()));
/*  162:     */     }
/*  163: 256 */     throw typeMismatch(CellType.BOOLEAN, cellType, false);
/*  164:     */   }
/*  165:     */   
/*  166:     */   public void setCellValue(boolean value)
/*  167:     */   {
/*  168: 269 */     this._cell.setT(STCellType.B);
/*  169: 270 */     this._cell.setV(value ? "1" : "0");
/*  170:     */   }
/*  171:     */   
/*  172:     */   public double getNumericCellValue()
/*  173:     */   {
/*  174: 286 */     CellType cellType = getCellTypeEnum();
/*  175: 287 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  176:     */     {
/*  177:     */     case 4: 
/*  178: 289 */       return 0.0D;
/*  179:     */     case 1: 
/*  180:     */     case 3: 
/*  181: 293 */       if (this._cell.isSetV())
/*  182:     */       {
/*  183: 294 */         String v = this._cell.getV();
/*  184: 295 */         if (v.isEmpty()) {
/*  185: 296 */           return 0.0D;
/*  186:     */         }
/*  187:     */         try
/*  188:     */         {
/*  189: 299 */           return Double.parseDouble(v);
/*  190:     */         }
/*  191:     */         catch (NumberFormatException e)
/*  192:     */         {
/*  193: 301 */           throw typeMismatch(CellType.NUMERIC, CellType.STRING, false);
/*  194:     */         }
/*  195:     */       }
/*  196: 304 */       return 0.0D;
/*  197:     */     }
/*  198: 307 */     throw typeMismatch(CellType.NUMERIC, cellType, false);
/*  199:     */   }
/*  200:     */   
/*  201:     */   public void setCellValue(double value)
/*  202:     */   {
/*  203: 321 */     if (Double.isInfinite(value))
/*  204:     */     {
/*  205: 324 */       this._cell.setT(STCellType.E);
/*  206: 325 */       this._cell.setV(FormulaError.DIV0.getString());
/*  207:     */     }
/*  208: 326 */     else if (Double.isNaN(value))
/*  209:     */     {
/*  210: 329 */       this._cell.setT(STCellType.E);
/*  211: 330 */       this._cell.setV(FormulaError.NUM.getString());
/*  212:     */     }
/*  213:     */     else
/*  214:     */     {
/*  215: 332 */       this._cell.setT(STCellType.N);
/*  216: 333 */       this._cell.setV(String.valueOf(value));
/*  217:     */     }
/*  218:     */   }
/*  219:     */   
/*  220:     */   public String getStringCellValue()
/*  221:     */   {
/*  222: 347 */     return getRichStringCellValue().getString();
/*  223:     */   }
/*  224:     */   
/*  225:     */   public XSSFRichTextString getRichStringCellValue()
/*  226:     */   {
/*  227: 360 */     CellType cellType = getCellTypeEnum();
/*  228:     */     XSSFRichTextString rt;
/*  229:     */     XSSFRichTextString rt;
/*  230: 362 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  231:     */     {
/*  232:     */     case 4: 
/*  233: 364 */       rt = new XSSFRichTextString("");
/*  234: 365 */       break;
/*  235:     */     case 2: 
/*  236:     */       XSSFRichTextString rt;
/*  237: 367 */       if (this._cell.getT() == STCellType.INLINE_STR)
/*  238:     */       {
/*  239: 368 */         if (this._cell.isSetIs())
/*  240:     */         {
/*  241: 370 */           rt = new XSSFRichTextString(this._cell.getIs());
/*  242:     */         }
/*  243:     */         else
/*  244:     */         {
/*  245:     */           XSSFRichTextString rt;
/*  246: 371 */           if (this._cell.isSetV()) {
/*  247: 373 */             rt = new XSSFRichTextString(this._cell.getV());
/*  248:     */           } else {
/*  249: 375 */             rt = new XSSFRichTextString("");
/*  250:     */           }
/*  251:     */         }
/*  252:     */       }
/*  253:     */       else
/*  254:     */       {
/*  255:     */         XSSFRichTextString rt;
/*  256: 377 */         if (this._cell.getT() == STCellType.STR)
/*  257:     */         {
/*  258: 379 */           rt = new XSSFRichTextString(this._cell.isSetV() ? this._cell.getV() : "");
/*  259:     */         }
/*  260:     */         else
/*  261:     */         {
/*  262:     */           XSSFRichTextString rt;
/*  263: 381 */           if (this._cell.isSetV())
/*  264:     */           {
/*  265: 382 */             int idx = Integer.parseInt(this._cell.getV());
/*  266: 383 */             rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(idx));
/*  267:     */           }
/*  268:     */           else
/*  269:     */           {
/*  270: 386 */             rt = new XSSFRichTextString("");
/*  271:     */           }
/*  272:     */         }
/*  273:     */       }
/*  274: 389 */       break;
/*  275:     */     case 3: 
/*  276: 391 */       checkFormulaCachedValueType(CellType.STRING, getBaseCellType(false));
/*  277: 392 */       rt = new XSSFRichTextString(this._cell.isSetV() ? this._cell.getV() : "");
/*  278: 393 */       break;
/*  279:     */     default: 
/*  280: 395 */       throw typeMismatch(CellType.STRING, cellType, false);
/*  281:     */     }
/*  282: 397 */     rt.setStylesTableReference(this._stylesSource);
/*  283: 398 */     return rt;
/*  284:     */   }
/*  285:     */   
/*  286:     */   private static void checkFormulaCachedValueType(CellType expectedTypeCode, CellType cachedValueType)
/*  287:     */   {
/*  288: 402 */     if (cachedValueType != expectedTypeCode) {
/*  289: 403 */       throw typeMismatch(expectedTypeCode, cachedValueType, true);
/*  290:     */     }
/*  291:     */   }
/*  292:     */   
/*  293:     */   public void setCellValue(String str)
/*  294:     */   {
/*  295: 417 */     setCellValue(str == null ? null : new XSSFRichTextString(str));
/*  296:     */   }
/*  297:     */   
/*  298:     */   public void setCellValue(RichTextString str)
/*  299:     */   {
/*  300: 430 */     if ((str == null) || (str.getString() == null))
/*  301:     */     {
/*  302: 431 */       setCellType(CellType.BLANK);
/*  303: 432 */       return;
/*  304:     */     }
/*  305: 435 */     if (str.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
/*  306: 436 */       throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
/*  307:     */     }
/*  308: 439 */     CellType cellType = getCellTypeEnum();
/*  309: 440 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  310:     */     {
/*  311:     */     case 3: 
/*  312: 442 */       this._cell.setV(str.getString());
/*  313: 443 */       this._cell.setT(STCellType.STR);
/*  314: 444 */       break;
/*  315:     */     default: 
/*  316: 446 */       if (this._cell.getT() == STCellType.INLINE_STR)
/*  317:     */       {
/*  318: 448 */         this._cell.setV(str.getString());
/*  319:     */       }
/*  320:     */       else
/*  321:     */       {
/*  322: 450 */         this._cell.setT(STCellType.S);
/*  323: 451 */         XSSFRichTextString rt = (XSSFRichTextString)str;
/*  324: 452 */         rt.setStylesTableReference(this._stylesSource);
/*  325: 453 */         int sRef = this._sharedStringSource.addEntry(rt.getCTRst());
/*  326: 454 */         this._cell.setV(Integer.toString(sRef));
/*  327:     */       }
/*  328:     */       break;
/*  329:     */     }
/*  330:     */   }
/*  331:     */   
/*  332:     */   public String getCellFormula()
/*  333:     */   {
/*  334: 469 */     return getCellFormula(null);
/*  335:     */   }
/*  336:     */   
/*  337:     */   protected String getCellFormula(XSSFEvaluationWorkbook fpb)
/*  338:     */   {
/*  339: 480 */     CellType cellType = getCellTypeEnum();
/*  340: 481 */     if (cellType != CellType.FORMULA) {
/*  341: 482 */       throw typeMismatch(CellType.FORMULA, cellType, false);
/*  342:     */     }
/*  343: 485 */     CTCellFormula f = this._cell.getF();
/*  344: 486 */     if ((isPartOfArrayFormulaGroup()) && (f == null))
/*  345:     */     {
/*  346: 487 */       XSSFCell cell = getSheet().getFirstCellInArrayFormula(this);
/*  347: 488 */       return cell.getCellFormula(fpb);
/*  348:     */     }
/*  349: 490 */     if (f.getT() == STCellFormulaType.SHARED) {
/*  350: 491 */       return convertSharedFormula((int)f.getSi(), fpb == null ? XSSFEvaluationWorkbook.create(getSheet().getWorkbook()) : fpb);
/*  351:     */     }
/*  352: 493 */     return f.getStringValue();
/*  353:     */   }
/*  354:     */   
/*  355:     */   private String convertSharedFormula(int si, XSSFEvaluationWorkbook fpb)
/*  356:     */   {
/*  357: 503 */     XSSFSheet sheet = getSheet();
/*  358:     */     
/*  359: 505 */     CTCellFormula f = sheet.getSharedFormula(si);
/*  360: 506 */     if (f == null) {
/*  361: 507 */       throw new IllegalStateException("Master cell of a shared formula with sid=" + si + " was not found");
/*  362:     */     }
/*  363: 511 */     String sharedFormula = f.getStringValue();
/*  364:     */     
/*  365: 513 */     String sharedFormulaRange = f.getRef();
/*  366:     */     
/*  367: 515 */     CellRangeAddress ref = CellRangeAddress.valueOf(sharedFormulaRange);
/*  368:     */     
/*  369: 517 */     int sheetIndex = sheet.getWorkbook().getSheetIndex(sheet);
/*  370: 518 */     SharedFormula sf = new SharedFormula(SpreadsheetVersion.EXCEL2007);
/*  371:     */     
/*  372: 520 */     Ptg[] ptgs = FormulaParser.parse(sharedFormula, fpb, FormulaType.CELL, sheetIndex, getRowIndex());
/*  373: 521 */     Ptg[] fmla = sf.convertSharedFormulas(ptgs, getRowIndex() - ref.getFirstRow(), getColumnIndex() - ref.getFirstColumn());
/*  374:     */     
/*  375: 523 */     return FormulaRenderer.toFormulaString(fpb, fmla);
/*  376:     */   }
/*  377:     */   
/*  378:     */   public void setCellFormula(String formula)
/*  379:     */   {
/*  380: 541 */     if (isPartOfArrayFormulaGroup()) {
/*  381: 542 */       notifyArrayFormulaChanging();
/*  382:     */     }
/*  383: 544 */     setFormula(formula, FormulaType.CELL);
/*  384:     */   }
/*  385:     */   
/*  386:     */   void setCellArrayFormula(String formula, CellRangeAddress range)
/*  387:     */   {
/*  388: 548 */     setFormula(formula, FormulaType.ARRAY);
/*  389: 549 */     CTCellFormula cellFormula = this._cell.getF();
/*  390: 550 */     cellFormula.setT(STCellFormulaType.ARRAY);
/*  391: 551 */     cellFormula.setRef(range.formatAsString());
/*  392:     */   }
/*  393:     */   
/*  394:     */   private void setFormula(String formula, FormulaType formulaType)
/*  395:     */   {
/*  396: 555 */     XSSFWorkbook wb = this._row.getSheet().getWorkbook();
/*  397: 556 */     if (formula == null)
/*  398:     */     {
/*  399: 557 */       wb.onDeleteFormula(this);
/*  400: 558 */       if (this._cell.isSetF()) {
/*  401: 559 */         this._cell.unsetF();
/*  402:     */       }
/*  403: 561 */       return;
/*  404:     */     }
/*  405: 564 */     XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create(wb);
/*  406:     */     
/*  407: 566 */     FormulaParser.parse(formula, fpb, formulaType, wb.getSheetIndex(getSheet()), getRowIndex());
/*  408:     */     
/*  409: 568 */     CTCellFormula f = CTCellFormula.Factory.newInstance();
/*  410: 569 */     f.setStringValue(formula);
/*  411: 570 */     this._cell.setF(f);
/*  412: 571 */     if (this._cell.isSetV()) {
/*  413: 572 */       this._cell.unsetV();
/*  414:     */     }
/*  415:     */   }
/*  416:     */   
/*  417:     */   public int getColumnIndex()
/*  418:     */   {
/*  419: 583 */     return this._cellNum;
/*  420:     */   }
/*  421:     */   
/*  422:     */   public int getRowIndex()
/*  423:     */   {
/*  424: 593 */     return this._row.getRowNum();
/*  425:     */   }
/*  426:     */   
/*  427:     */   public String getReference()
/*  428:     */   {
/*  429: 602 */     String ref = this._cell.getR();
/*  430: 603 */     if (ref == null) {
/*  431: 604 */       return getAddress().formatAsString();
/*  432:     */     }
/*  433: 606 */     return ref;
/*  434:     */   }
/*  435:     */   
/*  436:     */   public CellAddress getAddress()
/*  437:     */   {
/*  438: 614 */     return new CellAddress(this);
/*  439:     */   }
/*  440:     */   
/*  441:     */   public XSSFCellStyle getCellStyle()
/*  442:     */   {
/*  443: 624 */     XSSFCellStyle style = null;
/*  444: 625 */     if (this._stylesSource.getNumCellStyles() > 0)
/*  445:     */     {
/*  446: 626 */       long idx = this._cell.isSetS() ? this._cell.getS() : 0L;
/*  447: 627 */       style = this._stylesSource.getStyleAt((int)idx);
/*  448:     */     }
/*  449: 629 */     return style;
/*  450:     */   }
/*  451:     */   
/*  452:     */   public void setCellStyle(CellStyle style)
/*  453:     */   {
/*  454: 645 */     if (style == null)
/*  455:     */     {
/*  456: 646 */       if (this._cell.isSetS()) {
/*  457: 647 */         this._cell.unsetS();
/*  458:     */       }
/*  459:     */     }
/*  460:     */     else
/*  461:     */     {
/*  462: 650 */       XSSFCellStyle xStyle = (XSSFCellStyle)style;
/*  463: 651 */       xStyle.verifyBelongsToStylesSource(this._stylesSource);
/*  464:     */       
/*  465: 653 */       long idx = this._stylesSource.putStyle(xStyle);
/*  466: 654 */       this._cell.setS(idx);
/*  467:     */     }
/*  468:     */   }
/*  469:     */   
/*  470:     */   private boolean isFormulaCell()
/*  471:     */   {
/*  472: 669 */     if (((this._cell.isSetF()) && (this._cell.getF().getT() != STCellFormulaType.DATA_TABLE)) || (getSheet().isCellInArrayFormulaContext(this))) {
/*  473: 670 */       return true;
/*  474:     */     }
/*  475: 672 */     return false;
/*  476:     */   }
/*  477:     */   
/*  478:     */   @Deprecated
/*  479:     */   @Removal(version="3.17")
/*  480:     */   public int getCellType()
/*  481:     */   {
/*  482: 688 */     return getCellTypeEnum().getCode();
/*  483:     */   }
/*  484:     */   
/*  485:     */   public CellType getCellTypeEnum()
/*  486:     */   {
/*  487: 705 */     if (isFormulaCell()) {
/*  488: 706 */       return CellType.FORMULA;
/*  489:     */     }
/*  490: 709 */     return getBaseCellType(true);
/*  491:     */   }
/*  492:     */   
/*  493:     */   @Deprecated
/*  494:     */   @Removal(version="3.17")
/*  495:     */   public int getCachedFormulaResultType()
/*  496:     */   {
/*  497: 727 */     return getCachedFormulaResultTypeEnum().getCode();
/*  498:     */   }
/*  499:     */   
/*  500:     */   public CellType getCachedFormulaResultTypeEnum()
/*  501:     */   {
/*  502: 740 */     if (!isFormulaCell()) {
/*  503: 741 */       throw new IllegalStateException("Only formula cells have cached results");
/*  504:     */     }
/*  505: 744 */     return getBaseCellType(false);
/*  506:     */   }
/*  507:     */   
/*  508:     */   private CellType getBaseCellType(boolean blankCells)
/*  509:     */   {
/*  510: 751 */     switch (this._cell.getT().intValue())
/*  511:     */     {
/*  512:     */     case 1: 
/*  513: 753 */       return CellType.BOOLEAN;
/*  514:     */     case 2: 
/*  515: 755 */       if ((!this._cell.isSetV()) && (blankCells)) {
/*  516: 761 */         return CellType.BLANK;
/*  517:     */       }
/*  518: 763 */       return CellType.NUMERIC;
/*  519:     */     case 3: 
/*  520: 765 */       return CellType.ERROR;
/*  521:     */     case 4: 
/*  522:     */     case 5: 
/*  523:     */     case 6: 
/*  524: 769 */       return CellType.STRING;
/*  525:     */     }
/*  526: 771 */     throw new IllegalStateException("Illegal cell type: " + this._cell.getT());
/*  527:     */   }
/*  528:     */   
/*  529:     */   public Date getDateCellValue()
/*  530:     */   {
/*  531: 787 */     if (getCellTypeEnum() == CellType.BLANK) {
/*  532: 788 */       return null;
/*  533:     */     }
/*  534: 791 */     double value = getNumericCellValue();
/*  535: 792 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  536: 793 */     return DateUtil.getJavaDate(value, date1904);
/*  537:     */   }
/*  538:     */   
/*  539:     */   public void setCellValue(Date value)
/*  540:     */   {
/*  541: 806 */     if (value == null)
/*  542:     */     {
/*  543: 807 */       setCellType(CellType.BLANK);
/*  544: 808 */       return;
/*  545:     */     }
/*  546: 811 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  547: 812 */     setCellValue(DateUtil.getExcelDate(value, date1904));
/*  548:     */   }
/*  549:     */   
/*  550:     */   public void setCellValue(Calendar value)
/*  551:     */   {
/*  552: 833 */     if (value == null)
/*  553:     */     {
/*  554: 834 */       setCellType(CellType.BLANK);
/*  555: 835 */       return;
/*  556:     */     }
/*  557: 838 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  558: 839 */     setCellValue(DateUtil.getExcelDate(value, date1904));
/*  559:     */   }
/*  560:     */   
/*  561:     */   public String getErrorCellString()
/*  562:     */     throws IllegalStateException
/*  563:     */   {
/*  564: 850 */     CellType cellType = getBaseCellType(true);
/*  565: 851 */     if (cellType != CellType.ERROR) {
/*  566: 852 */       throw typeMismatch(CellType.ERROR, cellType, false);
/*  567:     */     }
/*  568: 855 */     return this._cell.getV();
/*  569:     */   }
/*  570:     */   
/*  571:     */   public byte getErrorCellValue()
/*  572:     */     throws IllegalStateException
/*  573:     */   {
/*  574: 870 */     String code = getErrorCellString();
/*  575: 871 */     if (code == null) {
/*  576: 872 */       return 0;
/*  577:     */     }
/*  578:     */     try
/*  579:     */     {
/*  580: 875 */       return FormulaError.forString(code).getCode();
/*  581:     */     }
/*  582:     */     catch (IllegalArgumentException e)
/*  583:     */     {
/*  584: 877 */       throw new IllegalStateException("Unexpected error code", e);
/*  585:     */     }
/*  586:     */   }
/*  587:     */   
/*  588:     */   public void setCellErrorValue(byte errorCode)
/*  589:     */   {
/*  590: 892 */     FormulaError error = FormulaError.forInt(errorCode);
/*  591: 893 */     setCellErrorValue(error);
/*  592:     */   }
/*  593:     */   
/*  594:     */   public void setCellErrorValue(FormulaError error)
/*  595:     */   {
/*  596: 905 */     this._cell.setT(STCellType.E);
/*  597: 906 */     this._cell.setV(error.getString());
/*  598:     */   }
/*  599:     */   
/*  600:     */   public void setAsActiveCell()
/*  601:     */   {
/*  602: 914 */     getSheet().setActiveCell(getAddress());
/*  603:     */   }
/*  604:     */   
/*  605:     */   private void setBlank()
/*  606:     */   {
/*  607: 922 */     CTCell blank = CTCell.Factory.newInstance();
/*  608: 923 */     blank.setR(this._cell.getR());
/*  609: 924 */     if (this._cell.isSetS()) {
/*  610: 925 */       blank.setS(this._cell.getS());
/*  611:     */     }
/*  612: 927 */     this._cell.set(blank);
/*  613:     */   }
/*  614:     */   
/*  615:     */   protected void setCellNum(int num)
/*  616:     */   {
/*  617: 936 */     checkBounds(num);
/*  618: 937 */     this._cellNum = num;
/*  619: 938 */     String ref = new CellReference(getRowIndex(), getColumnIndex()).formatAsString();
/*  620: 939 */     this._cell.setR(ref);
/*  621:     */   }
/*  622:     */   
/*  623:     */   @Deprecated
/*  624:     */   @Removal(version="3.17")
/*  625:     */   public void setCellType(int cellType)
/*  626:     */   {
/*  627: 958 */     setCellType(CellType.forInt(cellType));
/*  628:     */   }
/*  629:     */   
/*  630:     */   public void setCellType(CellType cellType)
/*  631:     */   {
/*  632: 967 */     CellType prevType = getCellTypeEnum();
/*  633: 969 */     if (isPartOfArrayFormulaGroup()) {
/*  634: 970 */       notifyArrayFormulaChanging();
/*  635:     */     }
/*  636: 972 */     if ((prevType == CellType.FORMULA) && (cellType != CellType.FORMULA)) {
/*  637: 973 */       getSheet().getWorkbook().onDeleteFormula(this);
/*  638:     */     }
/*  639: 976 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  640:     */     {
/*  641:     */     case 1: 
/*  642: 978 */       this._cell.setT(STCellType.N);
/*  643: 979 */       break;
/*  644:     */     case 2: 
/*  645: 981 */       if (prevType != CellType.STRING)
/*  646:     */       {
/*  647: 982 */         String str = convertCellValueToString();
/*  648: 983 */         XSSFRichTextString rt = new XSSFRichTextString(str);
/*  649: 984 */         rt.setStylesTableReference(this._stylesSource);
/*  650: 985 */         int sRef = this._sharedStringSource.addEntry(rt.getCTRst());
/*  651: 986 */         this._cell.setV(Integer.toString(sRef));
/*  652:     */       }
/*  653: 988 */       this._cell.setT(STCellType.S);
/*  654: 989 */       break;
/*  655:     */     case 3: 
/*  656: 991 */       if (!this._cell.isSetF())
/*  657:     */       {
/*  658: 992 */         CTCellFormula f = CTCellFormula.Factory.newInstance();
/*  659: 993 */         f.setStringValue("0");
/*  660: 994 */         this._cell.setF(f);
/*  661: 995 */         if (this._cell.isSetT()) {
/*  662: 996 */           this._cell.unsetT();
/*  663:     */         }
/*  664:     */       }
/*  665: 998 */       break;
/*  666:     */     case 4: 
/*  667:1001 */       setBlank();
/*  668:1002 */       break;
/*  669:     */     case 5: 
/*  670:1004 */       String newVal = convertCellValueToBoolean() ? "1" : "0";
/*  671:1005 */       this._cell.setT(STCellType.B);
/*  672:1006 */       this._cell.setV(newVal);
/*  673:1007 */       break;
/*  674:     */     case 6: 
/*  675:1010 */       this._cell.setT(STCellType.E);
/*  676:1011 */       break;
/*  677:     */     default: 
/*  678:1015 */       throw new IllegalArgumentException("Illegal cell type: " + cellType);
/*  679:     */     }
/*  680:1017 */     if ((cellType != CellType.FORMULA) && (this._cell.isSetF())) {
/*  681:1018 */       this._cell.unsetF();
/*  682:     */     }
/*  683:     */   }
/*  684:     */   
/*  685:     */   public String toString()
/*  686:     */   {
/*  687:1032 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[getCellTypeEnum().ordinal()])
/*  688:     */     {
/*  689:     */     case 1: 
/*  690:1034 */       if (DateUtil.isCellDateFormatted(this))
/*  691:     */       {
/*  692:1035 */         DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
/*  693:1036 */         sdf.setTimeZone(LocaleUtil.getUserTimeZone());
/*  694:1037 */         return sdf.format(getDateCellValue());
/*  695:     */       }
/*  696:1039 */       return Double.toString(getNumericCellValue());
/*  697:     */     case 2: 
/*  698:1041 */       return getRichStringCellValue().toString();
/*  699:     */     case 3: 
/*  700:1043 */       return getCellFormula();
/*  701:     */     case 4: 
/*  702:1045 */       return "";
/*  703:     */     case 5: 
/*  704:1047 */       return getBooleanCellValue() ? "TRUE" : "FALSE";
/*  705:     */     case 6: 
/*  706:1049 */       return ErrorEval.getText(getErrorCellValue());
/*  707:     */     }
/*  708:1051 */     return "Unknown Cell Type: " + getCellTypeEnum();
/*  709:     */   }
/*  710:     */   
/*  711:     */   public String getRawValue()
/*  712:     */   {
/*  713:1068 */     return this._cell.getV();
/*  714:     */   }
/*  715:     */   
/*  716:     */   private static RuntimeException typeMismatch(CellType expectedType, CellType actualType, boolean isFormulaCell)
/*  717:     */   {
/*  718:1076 */     String msg = "Cannot get a " + expectedType + " value from a " + actualType + " " + (isFormulaCell ? "formula " : "") + "cell";
/*  719:1077 */     return new IllegalStateException(msg);
/*  720:     */   }
/*  721:     */   
/*  722:     */   private static void checkBounds(int cellIndex)
/*  723:     */   {
/*  724:1084 */     SpreadsheetVersion v = SpreadsheetVersion.EXCEL2007;
/*  725:1085 */     int maxcol = SpreadsheetVersion.EXCEL2007.getLastColumnIndex();
/*  726:1086 */     if ((cellIndex < 0) || (cellIndex > maxcol)) {
/*  727:1087 */       throw new IllegalArgumentException("Invalid column index (" + cellIndex + ").  Allowable column range for " + v.name() + " is (0.." + maxcol + ") or ('A'..'" + v.getLastColumnName() + "')");
/*  728:     */     }
/*  729:     */   }
/*  730:     */   
/*  731:     */   public XSSFComment getCellComment()
/*  732:     */   {
/*  733:1100 */     return getSheet().getCellComment(new CellAddress(this));
/*  734:     */   }
/*  735:     */   
/*  736:     */   public void setCellComment(Comment comment)
/*  737:     */   {
/*  738:1111 */     if (comment == null)
/*  739:     */     {
/*  740:1112 */       removeCellComment();
/*  741:1113 */       return;
/*  742:     */     }
/*  743:1116 */     comment.setAddress(getRowIndex(), getColumnIndex());
/*  744:     */   }
/*  745:     */   
/*  746:     */   public void removeCellComment()
/*  747:     */   {
/*  748:1124 */     XSSFComment comment = getCellComment();
/*  749:1125 */     if (comment != null)
/*  750:     */     {
/*  751:1126 */       CellAddress ref = new CellAddress(getReference());
/*  752:1127 */       XSSFSheet sh = getSheet();
/*  753:1128 */       sh.getCommentsTable(false).removeComment(ref);
/*  754:1129 */       sh.getVMLDrawing(false).removeCommentShape(getRowIndex(), getColumnIndex());
/*  755:     */     }
/*  756:     */   }
/*  757:     */   
/*  758:     */   public XSSFHyperlink getHyperlink()
/*  759:     */   {
/*  760:1140 */     return getSheet().getHyperlink(this._row.getRowNum(), this._cellNum);
/*  761:     */   }
/*  762:     */   
/*  763:     */   public void setHyperlink(Hyperlink hyperlink)
/*  764:     */   {
/*  765:1151 */     if (hyperlink == null)
/*  766:     */     {
/*  767:1152 */       removeHyperlink();
/*  768:1153 */       return;
/*  769:     */     }
/*  770:1156 */     XSSFHyperlink link = (XSSFHyperlink)hyperlink;
/*  771:     */     
/*  772:     */ 
/*  773:1159 */     link.setCellReference(new CellReference(this._row.getRowNum(), this._cellNum).formatAsString());
/*  774:     */     
/*  775:     */ 
/*  776:1162 */     getSheet().addHyperlink(link);
/*  777:     */   }
/*  778:     */   
/*  779:     */   public void removeHyperlink()
/*  780:     */   {
/*  781:1170 */     getSheet().removeHyperlink(this._row.getRowNum(), this._cellNum);
/*  782:     */   }
/*  783:     */   
/*  784:     */   @Internal
/*  785:     */   public CTCell getCTCell()
/*  786:     */   {
/*  787:1181 */     return this._cell;
/*  788:     */   }
/*  789:     */   
/*  790:     */   @Internal
/*  791:     */   public void setCTCell(CTCell cell)
/*  792:     */   {
/*  793:1191 */     this._cell = cell;
/*  794:     */   }
/*  795:     */   
/*  796:     */   private boolean convertCellValueToBoolean()
/*  797:     */   {
/*  798:1203 */     CellType cellType = getCellTypeEnum();
/*  799:1205 */     if (cellType == CellType.FORMULA) {
/*  800:1206 */       cellType = getBaseCellType(false);
/*  801:     */     }
/*  802:1209 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  803:     */     {
/*  804:     */     case 5: 
/*  805:1211 */       return "1".equals(this._cell.getV());
/*  806:     */     case 2: 
/*  807:1213 */       int sstIndex = Integer.parseInt(this._cell.getV());
/*  808:1214 */       XSSFRichTextString rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(sstIndex));
/*  809:1215 */       String text = rt.getString();
/*  810:1216 */       return Boolean.parseBoolean(text);
/*  811:     */     case 1: 
/*  812:1218 */       return Double.parseDouble(this._cell.getV()) != 0.0D;
/*  813:     */     case 4: 
/*  814:     */     case 6: 
/*  815:1223 */       return false;
/*  816:     */     }
/*  817:1226 */     throw new RuntimeException("Unexpected cell type (" + cellType + ")");
/*  818:     */   }
/*  819:     */   
/*  820:     */   private String convertCellValueToString()
/*  821:     */   {
/*  822:1231 */     CellType cellType = getCellTypeEnum();
/*  823:1233 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  824:     */     {
/*  825:     */     case 4: 
/*  826:1235 */       return "";
/*  827:     */     case 5: 
/*  828:1237 */       return "1".equals(this._cell.getV()) ? "TRUE" : "FALSE";
/*  829:     */     case 2: 
/*  830:1239 */       int sstIndex = Integer.parseInt(this._cell.getV());
/*  831:1240 */       XSSFRichTextString rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(sstIndex));
/*  832:1241 */       return rt.getString();
/*  833:     */     case 1: 
/*  834:     */     case 6: 
/*  835:1244 */       return this._cell.getV();
/*  836:     */     case 3: 
/*  837:     */       break;
/*  838:     */     default: 
/*  839:1250 */       throw new IllegalStateException("Unexpected cell type (" + cellType + ")");
/*  840:     */     }
/*  841:1252 */     cellType = getBaseCellType(false);
/*  842:1253 */     String textValue = this._cell.getV();
/*  843:1254 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  844:     */     {
/*  845:     */     case 5: 
/*  846:1256 */       if ("1".equals(textValue)) {
/*  847:1257 */         return "TRUE";
/*  848:     */       }
/*  849:1259 */       if ("0".equals(textValue)) {
/*  850:1260 */         return "FALSE";
/*  851:     */       }
/*  852:1262 */       throw new IllegalStateException("Unexpected boolean cached formula value '" + textValue + "'.");
/*  853:     */     case 1: 
/*  854:     */     case 2: 
/*  855:     */     case 6: 
/*  856:1270 */       return textValue;
/*  857:     */     }
/*  858:1273 */     throw new IllegalStateException("Unexpected formula result type (" + cellType + ")");
/*  859:     */   }
/*  860:     */   
/*  861:     */   public CellRangeAddress getArrayFormulaRange()
/*  862:     */   {
/*  863:1280 */     XSSFCell cell = getSheet().getFirstCellInArrayFormula(this);
/*  864:1281 */     if (cell == null) {
/*  865:1282 */       throw new IllegalStateException("Cell " + getReference() + " is not part of an array formula.");
/*  866:     */     }
/*  867:1285 */     String formulaRef = cell._cell.getF().getRef();
/*  868:1286 */     return CellRangeAddress.valueOf(formulaRef);
/*  869:     */   }
/*  870:     */   
/*  871:     */   public boolean isPartOfArrayFormulaGroup()
/*  872:     */   {
/*  873:1291 */     return getSheet().isCellInArrayFormulaContext(this);
/*  874:     */   }
/*  875:     */   
/*  876:     */   void notifyArrayFormulaChanging(String msg)
/*  877:     */   {
/*  878:1300 */     if (isPartOfArrayFormulaGroup())
/*  879:     */     {
/*  880:1301 */       CellRangeAddress cra = getArrayFormulaRange();
/*  881:1302 */       if (cra.getNumberOfCells() > 1) {
/*  882:1303 */         throw new IllegalStateException(msg);
/*  883:     */       }
/*  884:1306 */       getRow().getSheet().removeArrayFormula(this);
/*  885:     */     }
/*  886:     */   }
/*  887:     */   
/*  888:     */   void notifyArrayFormulaChanging()
/*  889:     */   {
/*  890:1325 */     CellReference ref = new CellReference(this);
/*  891:1326 */     String msg = "Cell " + ref.formatAsString() + " is part of a multi-cell array formula. " + "You cannot change part of an array.";
/*  892:     */     
/*  893:1328 */     notifyArrayFormulaChanging(msg);
/*  894:     */   }
/*  895:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFCell
 * JD-Core Version:    0.7.0.1
 */