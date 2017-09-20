/*    1:     */ package org.apache.poi.xssf.streaming;
/*    2:     */ 
/*    3:     */ import java.text.DateFormat;
/*    4:     */ import java.text.SimpleDateFormat;
/*    5:     */ import java.util.Calendar;
/*    6:     */ import java.util.Date;
/*    7:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*    8:     */ import org.apache.poi.ss.formula.FormulaParseException;
/*    9:     */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   10:     */ import org.apache.poi.ss.usermodel.Cell;
/*   11:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   12:     */ import org.apache.poi.ss.usermodel.CellType;
/*   13:     */ import org.apache.poi.ss.usermodel.Comment;
/*   14:     */ import org.apache.poi.ss.usermodel.CreationHelper;
/*   15:     */ import org.apache.poi.ss.usermodel.DateUtil;
/*   16:     */ import org.apache.poi.ss.usermodel.FormulaError;
/*   17:     */ import org.apache.poi.ss.usermodel.Hyperlink;
/*   18:     */ import org.apache.poi.ss.usermodel.RichTextString;
/*   19:     */ import org.apache.poi.ss.usermodel.Row;
/*   20:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   21:     */ import org.apache.poi.ss.util.CellAddress;
/*   22:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   23:     */ import org.apache.poi.ss.util.CellReference;
/*   24:     */ import org.apache.poi.util.LocaleUtil;
/*   25:     */ import org.apache.poi.util.NotImplemented;
/*   26:     */ import org.apache.poi.util.POILogFactory;
/*   27:     */ import org.apache.poi.util.POILogger;
/*   28:     */ import org.apache.poi.xssf.usermodel.XSSFHyperlink;
/*   29:     */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*   30:     */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*   31:     */ 
/*   32:     */ public class SXSSFCell
/*   33:     */   implements Cell
/*   34:     */ {
/*   35:  52 */   private static final POILogger logger = POILogFactory.getLogger(SXSSFCell.class);
/*   36:     */   private final SXSSFRow _row;
/*   37:     */   private Value _value;
/*   38:     */   private CellStyle _style;
/*   39:     */   private Property _firstProperty;
/*   40:     */   
/*   41:     */   public SXSSFCell(SXSSFRow row, CellType cellType)
/*   42:     */   {
/*   43:  61 */     this._row = row;
/*   44:  62 */     setType(cellType);
/*   45:     */   }
/*   46:     */   
/*   47:     */   public int getColumnIndex()
/*   48:     */   {
/*   49:  75 */     return this._row.getCellIndex(this);
/*   50:     */   }
/*   51:     */   
/*   52:     */   public int getRowIndex()
/*   53:     */   {
/*   54:  86 */     return this._row.getRowNum();
/*   55:     */   }
/*   56:     */   
/*   57:     */   public CellAddress getAddress()
/*   58:     */   {
/*   59:  94 */     return new CellAddress(this);
/*   60:     */   }
/*   61:     */   
/*   62:     */   public SXSSFSheet getSheet()
/*   63:     */   {
/*   64: 105 */     return this._row.getSheet();
/*   65:     */   }
/*   66:     */   
/*   67:     */   public Row getRow()
/*   68:     */   {
/*   69: 116 */     return this._row;
/*   70:     */   }
/*   71:     */   
/*   72:     */   /**
/*   73:     */    * @deprecated
/*   74:     */    */
/*   75:     */   public void setCellType(int cellType)
/*   76:     */   {
/*   77: 135 */     ensureType(CellType.forInt(cellType));
/*   78:     */   }
/*   79:     */   
/*   80:     */   public void setCellType(CellType cellType)
/*   81:     */   {
/*   82: 145 */     ensureType(cellType);
/*   83:     */   }
/*   84:     */   
/*   85:     */   /**
/*   86:     */    * @deprecated
/*   87:     */    */
/*   88:     */   public int getCellType()
/*   89:     */   {
/*   90: 157 */     return getCellTypeEnum().getCode();
/*   91:     */   }
/*   92:     */   
/*   93:     */   public CellType getCellTypeEnum()
/*   94:     */   {
/*   95: 170 */     return this._value.getType();
/*   96:     */   }
/*   97:     */   
/*   98:     */   /**
/*   99:     */    * @deprecated
/*  100:     */    */
/*  101:     */   public int getCachedFormulaResultType()
/*  102:     */   {
/*  103: 183 */     return getCachedFormulaResultTypeEnum().getCode();
/*  104:     */   }
/*  105:     */   
/*  106:     */   public CellType getCachedFormulaResultTypeEnum()
/*  107:     */   {
/*  108: 197 */     if (this._value.getType() != CellType.FORMULA) {
/*  109: 198 */       throw new IllegalStateException("Only formula cells have cached results");
/*  110:     */     }
/*  111: 201 */     return ((FormulaValue)this._value).getFormulaType();
/*  112:     */   }
/*  113:     */   
/*  114:     */   public void setCellValue(double value)
/*  115:     */   {
/*  116: 214 */     if (Double.isInfinite(value))
/*  117:     */     {
/*  118: 217 */       setCellErrorValue(FormulaError.DIV0.getCode());
/*  119:     */     }
/*  120: 218 */     else if (Double.isNaN(value))
/*  121:     */     {
/*  122: 219 */       setCellErrorValue(FormulaError.NUM.getCode());
/*  123:     */     }
/*  124:     */     else
/*  125:     */     {
/*  126: 221 */       ensureTypeOrFormulaType(CellType.NUMERIC);
/*  127: 222 */       if (this._value.getType() == CellType.FORMULA) {
/*  128: 223 */         ((NumericFormulaValue)this._value).setPreEvaluatedValue(value);
/*  129:     */       } else {
/*  130: 225 */         ((NumericValue)this._value).setValue(value);
/*  131:     */       }
/*  132:     */     }
/*  133:     */   }
/*  134:     */   
/*  135:     */   public void setCellValue(Date value)
/*  136:     */   {
/*  137: 246 */     if (value == null)
/*  138:     */     {
/*  139: 247 */       setCellType(CellType.BLANK);
/*  140: 248 */       return;
/*  141:     */     }
/*  142: 251 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  143: 252 */     setCellValue(DateUtil.getExcelDate(value, date1904));
/*  144:     */   }
/*  145:     */   
/*  146:     */   public void setCellValue(Calendar value)
/*  147:     */   {
/*  148: 273 */     if (value == null)
/*  149:     */     {
/*  150: 274 */       setCellType(CellType.BLANK);
/*  151: 275 */       return;
/*  152:     */     }
/*  153: 278 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  154: 279 */     setCellValue(DateUtil.getExcelDate(value, date1904));
/*  155:     */   }
/*  156:     */   
/*  157:     */   public void setCellValue(RichTextString value)
/*  158:     */   {
/*  159: 293 */     XSSFRichTextString xvalue = (XSSFRichTextString)value;
/*  160: 295 */     if ((xvalue != null) && (xvalue.getString() != null))
/*  161:     */     {
/*  162: 296 */       ensureRichTextStringType();
/*  163: 298 */       if (xvalue.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
/*  164: 299 */         throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
/*  165:     */       }
/*  166: 301 */       if (xvalue.hasFormatting()) {
/*  167: 302 */         logger.log(5, new Object[] { "SXSSF doesn't support Shared Strings, rich text formatting information has be lost" });
/*  168:     */       }
/*  169: 304 */       ((RichTextValue)this._value).setValue(xvalue);
/*  170:     */     }
/*  171:     */     else
/*  172:     */     {
/*  173: 306 */       setCellType(CellType.BLANK);
/*  174:     */     }
/*  175:     */   }
/*  176:     */   
/*  177:     */   public void setCellValue(String value)
/*  178:     */   {
/*  179: 321 */     if (value != null)
/*  180:     */     {
/*  181: 322 */       ensureTypeOrFormulaType(CellType.STRING);
/*  182: 324 */       if (value.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
/*  183: 325 */         throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
/*  184:     */       }
/*  185: 328 */       if (this._value.getType() == CellType.FORMULA)
/*  186:     */       {
/*  187: 329 */         if ((this._value instanceof NumericFormulaValue)) {
/*  188: 330 */           ((NumericFormulaValue)this._value).setPreEvaluatedValue(Double.parseDouble(value));
/*  189:     */         } else {
/*  190: 332 */           ((StringFormulaValue)this._value).setPreEvaluatedValue(value);
/*  191:     */         }
/*  192:     */       }
/*  193:     */       else {
/*  194: 335 */         ((PlainStringValue)this._value).setValue(value);
/*  195:     */       }
/*  196:     */     }
/*  197:     */     else
/*  198:     */     {
/*  199: 337 */       setCellType(CellType.BLANK);
/*  200:     */     }
/*  201:     */   }
/*  202:     */   
/*  203:     */   public void setCellFormula(String formula)
/*  204:     */     throws FormulaParseException
/*  205:     */   {
/*  206: 355 */     if (formula == null)
/*  207:     */     {
/*  208: 356 */       setType(CellType.BLANK);
/*  209: 357 */       return;
/*  210:     */     }
/*  211: 360 */     ensureFormulaType(computeTypeFromFormula(formula));
/*  212: 361 */     ((FormulaValue)this._value).setValue(formula);
/*  213:     */   }
/*  214:     */   
/*  215:     */   public String getCellFormula()
/*  216:     */   {
/*  217: 372 */     if (this._value.getType() != CellType.FORMULA) {
/*  218: 373 */       throw typeMismatch(CellType.FORMULA, this._value.getType(), false);
/*  219:     */     }
/*  220: 374 */     return ((FormulaValue)this._value).getValue();
/*  221:     */   }
/*  222:     */   
/*  223:     */   public double getNumericCellValue()
/*  224:     */   {
/*  225: 391 */     CellType cellType = getCellTypeEnum();
/*  226: 392 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  227:     */     {
/*  228:     */     case 1: 
/*  229: 395 */       return 0.0D;
/*  230:     */     case 2: 
/*  231: 398 */       FormulaValue fv = (FormulaValue)this._value;
/*  232: 399 */       if (fv.getFormulaType() != CellType.NUMERIC) {
/*  233: 400 */         throw typeMismatch(CellType.NUMERIC, CellType.FORMULA, false);
/*  234:     */       }
/*  235: 401 */       return ((NumericFormulaValue)this._value).getPreEvaluatedValue();
/*  236:     */     case 3: 
/*  237: 404 */       return ((NumericValue)this._value).getValue();
/*  238:     */     }
/*  239: 406 */     throw typeMismatch(CellType.NUMERIC, cellType, false);
/*  240:     */   }
/*  241:     */   
/*  242:     */   public Date getDateCellValue()
/*  243:     */   {
/*  244: 423 */     CellType cellType = getCellTypeEnum();
/*  245: 424 */     if (cellType == CellType.BLANK) {
/*  246: 426 */       return null;
/*  247:     */     }
/*  248: 429 */     double value = getNumericCellValue();
/*  249: 430 */     boolean date1904 = getSheet().getWorkbook().isDate1904();
/*  250: 431 */     return DateUtil.getJavaDate(value, date1904);
/*  251:     */   }
/*  252:     */   
/*  253:     */   public RichTextString getRichStringCellValue()
/*  254:     */   {
/*  255: 445 */     CellType cellType = getCellTypeEnum();
/*  256: 446 */     if (getCellTypeEnum() != CellType.STRING) {
/*  257: 447 */       throw typeMismatch(CellType.STRING, cellType, false);
/*  258:     */     }
/*  259: 449 */     StringValue sval = (StringValue)this._value;
/*  260: 450 */     if (sval.isRichText()) {
/*  261: 451 */       return ((RichTextValue)this._value).getValue();
/*  262:     */     }
/*  263: 453 */     String plainText = getStringCellValue();
/*  264: 454 */     return getSheet().getWorkbook().getCreationHelper().createRichTextString(plainText);
/*  265:     */   }
/*  266:     */   
/*  267:     */   public String getStringCellValue()
/*  268:     */   {
/*  269: 470 */     CellType cellType = getCellTypeEnum();
/*  270: 471 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  271:     */     {
/*  272:     */     case 1: 
/*  273: 474 */       return "";
/*  274:     */     case 2: 
/*  275: 477 */       FormulaValue fv = (FormulaValue)this._value;
/*  276: 478 */       if (fv.getFormulaType() != CellType.STRING) {
/*  277: 479 */         throw typeMismatch(CellType.STRING, CellType.FORMULA, false);
/*  278:     */       }
/*  279: 480 */       return ((StringFormulaValue)this._value).getPreEvaluatedValue();
/*  280:     */     case 4: 
/*  281: 484 */       if (((StringValue)this._value).isRichText()) {
/*  282: 485 */         return ((RichTextValue)this._value).getValue().getString();
/*  283:     */       }
/*  284: 487 */       return ((PlainStringValue)this._value).getValue();
/*  285:     */     }
/*  286: 490 */     throw typeMismatch(CellType.STRING, cellType, false);
/*  287:     */   }
/*  288:     */   
/*  289:     */   public void setCellValue(boolean value)
/*  290:     */   {
/*  291: 504 */     ensureTypeOrFormulaType(CellType.BOOLEAN);
/*  292: 505 */     if (this._value.getType() == CellType.FORMULA) {
/*  293: 506 */       ((BooleanFormulaValue)this._value).setPreEvaluatedValue(value);
/*  294:     */     } else {
/*  295: 508 */       ((BooleanValue)this._value).setValue(value);
/*  296:     */     }
/*  297:     */   }
/*  298:     */   
/*  299:     */   public void setCellErrorValue(byte value)
/*  300:     */   {
/*  301: 523 */     ensureType(CellType.ERROR);
/*  302: 524 */     if (this._value.getType() == CellType.FORMULA) {
/*  303: 525 */       ((ErrorFormulaValue)this._value).setPreEvaluatedValue(value);
/*  304:     */     } else {
/*  305: 527 */       ((ErrorValue)this._value).setValue(value);
/*  306:     */     }
/*  307:     */   }
/*  308:     */   
/*  309:     */   public boolean getBooleanCellValue()
/*  310:     */   {
/*  311: 542 */     CellType cellType = getCellTypeEnum();
/*  312: 543 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  313:     */     {
/*  314:     */     case 1: 
/*  315: 546 */       return false;
/*  316:     */     case 2: 
/*  317: 549 */       FormulaValue fv = (FormulaValue)this._value;
/*  318: 550 */       if (fv.getFormulaType() != CellType.BOOLEAN) {
/*  319: 551 */         throw typeMismatch(CellType.BOOLEAN, CellType.FORMULA, false);
/*  320:     */       }
/*  321: 552 */       return ((BooleanFormulaValue)this._value).getPreEvaluatedValue();
/*  322:     */     case 5: 
/*  323: 556 */       return ((BooleanValue)this._value).getValue();
/*  324:     */     }
/*  325: 559 */     throw typeMismatch(CellType.BOOLEAN, cellType, false);
/*  326:     */   }
/*  327:     */   
/*  328:     */   public byte getErrorCellValue()
/*  329:     */   {
/*  330: 577 */     CellType cellType = getCellTypeEnum();
/*  331: 578 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  332:     */     {
/*  333:     */     case 1: 
/*  334: 581 */       return 0;
/*  335:     */     case 2: 
/*  336: 584 */       FormulaValue fv = (FormulaValue)this._value;
/*  337: 585 */       if (fv.getFormulaType() != CellType.ERROR) {
/*  338: 586 */         throw typeMismatch(CellType.ERROR, CellType.FORMULA, false);
/*  339:     */       }
/*  340: 587 */       return ((ErrorFormulaValue)this._value).getPreEvaluatedValue();
/*  341:     */     case 6: 
/*  342: 591 */       return ((ErrorValue)this._value).getValue();
/*  343:     */     }
/*  344: 594 */     throw typeMismatch(CellType.ERROR, cellType, false);
/*  345:     */   }
/*  346:     */   
/*  347:     */   public void setCellStyle(CellStyle style)
/*  348:     */   {
/*  349: 612 */     this._style = style;
/*  350:     */   }
/*  351:     */   
/*  352:     */   public CellStyle getCellStyle()
/*  353:     */   {
/*  354: 625 */     if (this._style == null)
/*  355:     */     {
/*  356: 626 */       SXSSFWorkbook wb = (SXSSFWorkbook)getRow().getSheet().getWorkbook();
/*  357: 627 */       return wb.getCellStyleAt(0);
/*  358:     */     }
/*  359: 629 */     return this._style;
/*  360:     */   }
/*  361:     */   
/*  362:     */   public void setAsActiveCell()
/*  363:     */   {
/*  364: 639 */     getSheet().setActiveCell(getAddress());
/*  365:     */   }
/*  366:     */   
/*  367:     */   public void setCellComment(Comment comment)
/*  368:     */   {
/*  369: 650 */     setProperty(1, comment);
/*  370:     */   }
/*  371:     */   
/*  372:     */   public Comment getCellComment()
/*  373:     */   {
/*  374: 661 */     return (Comment)getPropertyValue(1);
/*  375:     */   }
/*  376:     */   
/*  377:     */   public void removeCellComment()
/*  378:     */   {
/*  379: 670 */     removeProperty(1);
/*  380:     */   }
/*  381:     */   
/*  382:     */   public Hyperlink getHyperlink()
/*  383:     */   {
/*  384: 679 */     return (Hyperlink)getPropertyValue(2);
/*  385:     */   }
/*  386:     */   
/*  387:     */   public void setHyperlink(Hyperlink link)
/*  388:     */   {
/*  389: 691 */     if (link == null)
/*  390:     */     {
/*  391: 692 */       removeHyperlink();
/*  392: 693 */       return;
/*  393:     */     }
/*  394: 696 */     setProperty(2, link);
/*  395:     */     
/*  396: 698 */     XSSFHyperlink xssfobj = (XSSFHyperlink)link;
/*  397:     */     
/*  398: 700 */     CellReference ref = new CellReference(getRowIndex(), getColumnIndex());
/*  399: 701 */     xssfobj.setCellReference(ref);
/*  400:     */     
/*  401:     */ 
/*  402: 704 */     getSheet()._sh.addHyperlink(xssfobj);
/*  403:     */   }
/*  404:     */   
/*  405:     */   public void removeHyperlink()
/*  406:     */   {
/*  407: 713 */     removeProperty(2);
/*  408:     */     
/*  409: 715 */     getSheet()._sh.removeHyperlink(getRowIndex(), getColumnIndex());
/*  410:     */   }
/*  411:     */   
/*  412:     */   @NotImplemented
/*  413:     */   public CellRangeAddress getArrayFormulaRange()
/*  414:     */   {
/*  415: 727 */     return null;
/*  416:     */   }
/*  417:     */   
/*  418:     */   @NotImplemented
/*  419:     */   public boolean isPartOfArrayFormulaGroup()
/*  420:     */   {
/*  421: 737 */     return false;
/*  422:     */   }
/*  423:     */   
/*  424:     */   public String toString()
/*  425:     */   {
/*  426: 751 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[getCellTypeEnum().ordinal()])
/*  427:     */     {
/*  428:     */     case 1: 
/*  429: 753 */       return "";
/*  430:     */     case 5: 
/*  431: 755 */       return getBooleanCellValue() ? "TRUE" : "FALSE";
/*  432:     */     case 6: 
/*  433: 757 */       return ErrorEval.getText(getErrorCellValue());
/*  434:     */     case 2: 
/*  435: 759 */       return getCellFormula();
/*  436:     */     case 3: 
/*  437: 761 */       if (DateUtil.isCellDateFormatted(this))
/*  438:     */       {
/*  439: 762 */         DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
/*  440: 763 */         sdf.setTimeZone(LocaleUtil.getUserTimeZone());
/*  441: 764 */         return sdf.format(getDateCellValue());
/*  442:     */       }
/*  443: 766 */       return getNumericCellValue() + "";
/*  444:     */     case 4: 
/*  445: 768 */       return getRichStringCellValue().toString();
/*  446:     */     }
/*  447: 770 */     return "Unknown Cell Type: " + getCellTypeEnum();
/*  448:     */   }
/*  449:     */   
/*  450:     */   void removeProperty(int type)
/*  451:     */   {
/*  452: 776 */     Property current = this._firstProperty;
/*  453: 777 */     Property previous = null;
/*  454: 778 */     while ((current != null) && (current.getType() != type))
/*  455:     */     {
/*  456: 780 */       previous = current;
/*  457: 781 */       current = current._next;
/*  458:     */     }
/*  459: 783 */     if (current != null) {
/*  460: 785 */       if (previous != null) {
/*  461: 787 */         previous._next = current._next;
/*  462:     */       } else {
/*  463: 791 */         this._firstProperty = current._next;
/*  464:     */       }
/*  465:     */     }
/*  466:     */   }
/*  467:     */   
/*  468:     */   void setProperty(int type, Object value)
/*  469:     */   {
/*  470: 797 */     Property current = this._firstProperty;
/*  471: 798 */     Property previous = null;
/*  472: 799 */     while ((current != null) && (current.getType() != type))
/*  473:     */     {
/*  474: 801 */       previous = current;
/*  475: 802 */       current = current._next;
/*  476:     */     }
/*  477: 804 */     if (current != null)
/*  478:     */     {
/*  479: 806 */       current.setValue(value);
/*  480:     */     }
/*  481:     */     else
/*  482:     */     {
/*  483: 810 */       switch (type)
/*  484:     */       {
/*  485:     */       case 1: 
/*  486: 814 */         current = new CommentProperty(value);
/*  487: 815 */         break;
/*  488:     */       case 2: 
/*  489: 819 */         current = new HyperlinkProperty(value);
/*  490: 820 */         break;
/*  491:     */       default: 
/*  492: 824 */         throw new IllegalArgumentException("Invalid type: " + type);
/*  493:     */       }
/*  494: 827 */       if (previous != null) {
/*  495: 829 */         previous._next = current;
/*  496:     */       } else {
/*  497: 833 */         this._firstProperty = current;
/*  498:     */       }
/*  499:     */     }
/*  500:     */   }
/*  501:     */   
/*  502:     */   Object getPropertyValue(int type)
/*  503:     */   {
/*  504: 839 */     return getPropertyValue(type, null);
/*  505:     */   }
/*  506:     */   
/*  507:     */   Object getPropertyValue(int type, String defaultValue)
/*  508:     */   {
/*  509: 843 */     Property current = this._firstProperty;
/*  510: 844 */     while ((current != null) && (current.getType() != type)) {
/*  511: 844 */       current = current._next;
/*  512:     */     }
/*  513: 845 */     return current == null ? defaultValue : current.getValue();
/*  514:     */   }
/*  515:     */   
/*  516:     */   void ensurePlainStringType()
/*  517:     */   {
/*  518: 849 */     if ((this._value.getType() != CellType.STRING) || (((StringValue)this._value).isRichText())) {
/*  519: 851 */       this._value = new PlainStringValue();
/*  520:     */     }
/*  521:     */   }
/*  522:     */   
/*  523:     */   void ensureRichTextStringType()
/*  524:     */   {
/*  525: 855 */     if ((this._value.getType() != CellType.STRING) || (!((StringValue)this._value).isRichText())) {
/*  526: 857 */       this._value = new RichTextValue();
/*  527:     */     }
/*  528:     */   }
/*  529:     */   
/*  530:     */   void ensureType(CellType type)
/*  531:     */   {
/*  532: 861 */     if (this._value.getType() != type) {
/*  533: 862 */       setType(type);
/*  534:     */     }
/*  535:     */   }
/*  536:     */   
/*  537:     */   void ensureFormulaType(CellType type)
/*  538:     */   {
/*  539: 866 */     if ((this._value.getType() != CellType.FORMULA) || (((FormulaValue)this._value).getFormulaType() != type)) {
/*  540: 868 */       setFormulaType(type);
/*  541:     */     }
/*  542:     */   }
/*  543:     */   
/*  544:     */   void ensureTypeOrFormulaType(CellType type)
/*  545:     */   {
/*  546: 875 */     if (this._value.getType() == type)
/*  547:     */     {
/*  548: 877 */       if ((type == CellType.STRING) && (((StringValue)this._value).isRichText())) {
/*  549: 878 */         setType(CellType.STRING);
/*  550:     */       }
/*  551: 879 */       return;
/*  552:     */     }
/*  553: 881 */     if (this._value.getType() == CellType.FORMULA)
/*  554:     */     {
/*  555: 883 */       if (((FormulaValue)this._value).getFormulaType() == type) {
/*  556: 884 */         return;
/*  557:     */       }
/*  558: 885 */       setFormulaType(type);
/*  559: 886 */       return;
/*  560:     */     }
/*  561: 888 */     setType(type);
/*  562:     */   }
/*  563:     */   
/*  564:     */   void setType(CellType type)
/*  565:     */   {
/*  566: 899 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[type.ordinal()])
/*  567:     */     {
/*  568:     */     case 3: 
/*  569: 903 */       this._value = new NumericValue();
/*  570: 904 */       break;
/*  571:     */     case 4: 
/*  572: 908 */       PlainStringValue sval = new PlainStringValue();
/*  573: 909 */       if (this._value != null)
/*  574:     */       {
/*  575: 911 */         String str = convertCellValueToString();
/*  576: 912 */         sval.setValue(str);
/*  577:     */       }
/*  578: 914 */       this._value = sval;
/*  579: 915 */       break;
/*  580:     */     case 2: 
/*  581: 919 */       this._value = new NumericFormulaValue();
/*  582: 920 */       break;
/*  583:     */     case 1: 
/*  584: 924 */       this._value = new BlankValue();
/*  585: 925 */       break;
/*  586:     */     case 5: 
/*  587: 929 */       BooleanValue bval = new BooleanValue();
/*  588: 930 */       if (this._value != null)
/*  589:     */       {
/*  590: 932 */         boolean val = convertCellValueToBoolean();
/*  591: 933 */         bval.setValue(val);
/*  592:     */       }
/*  593: 935 */       this._value = bval;
/*  594: 936 */       break;
/*  595:     */     case 6: 
/*  596: 940 */       this._value = new ErrorValue();
/*  597: 941 */       break;
/*  598:     */     default: 
/*  599: 945 */       throw new IllegalArgumentException("Illegal type " + type);
/*  600:     */     }
/*  601:     */   }
/*  602:     */   
/*  603:     */   void setFormulaType(CellType type)
/*  604:     */   {
/*  605: 951 */     Value prevValue = this._value;
/*  606: 952 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[type.ordinal()])
/*  607:     */     {
/*  608:     */     case 3: 
/*  609: 956 */       this._value = new NumericFormulaValue();
/*  610: 957 */       break;
/*  611:     */     case 4: 
/*  612: 961 */       this._value = new StringFormulaValue();
/*  613: 962 */       break;
/*  614:     */     case 5: 
/*  615: 966 */       this._value = new BooleanFormulaValue();
/*  616: 967 */       break;
/*  617:     */     case 6: 
/*  618: 971 */       this._value = new ErrorFormulaValue();
/*  619: 972 */       break;
/*  620:     */     default: 
/*  621: 976 */       throw new IllegalArgumentException("Illegal type " + type);
/*  622:     */     }
/*  623: 981 */     if ((prevValue instanceof FormulaValue)) {
/*  624: 982 */       ((FormulaValue)this._value)._value = ((FormulaValue)prevValue)._value;
/*  625:     */     }
/*  626:     */   }
/*  627:     */   
/*  628:     */   @NotImplemented
/*  629:     */   CellType computeTypeFromFormula(String formula)
/*  630:     */   {
/*  631: 990 */     return CellType.NUMERIC;
/*  632:     */   }
/*  633:     */   
/*  634:     */   private static RuntimeException typeMismatch(CellType expectedTypeCode, CellType actualTypeCode, boolean isFormulaCell)
/*  635:     */   {
/*  636: 997 */     String msg = "Cannot get a " + expectedTypeCode + " value from a " + actualTypeCode + " " + (isFormulaCell ? "formula " : "") + "cell";
/*  637:     */     
/*  638: 999 */     return new IllegalStateException(msg);
/*  639:     */   }
/*  640:     */   
/*  641:     */   private boolean convertCellValueToBoolean()
/*  642:     */   {
/*  643:1003 */     CellType cellType = getCellTypeEnum();
/*  644:1005 */     if (cellType == CellType.FORMULA) {
/*  645:1006 */       cellType = getCachedFormulaResultTypeEnum();
/*  646:     */     }
/*  647:1009 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  648:     */     {
/*  649:     */     case 5: 
/*  650:1011 */       return getBooleanCellValue();
/*  651:     */     case 4: 
/*  652:1014 */       String text = getStringCellValue();
/*  653:1015 */       return Boolean.parseBoolean(text);
/*  654:     */     case 3: 
/*  655:1017 */       return getNumericCellValue() != 0.0D;
/*  656:     */     case 1: 
/*  657:     */     case 6: 
/*  658:1020 */       return false;
/*  659:     */     }
/*  660:1021 */     throw new RuntimeException("Unexpected cell type (" + cellType + ")");
/*  661:     */   }
/*  662:     */   
/*  663:     */   private String convertCellValueToString()
/*  664:     */   {
/*  665:1026 */     CellType cellType = getCellTypeEnum();
/*  666:1027 */     return convertCellValueToString(cellType);
/*  667:     */   }
/*  668:     */   
/*  669:     */   private String convertCellValueToString(CellType cellType)
/*  670:     */   {
/*  671:1030 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  672:     */     {
/*  673:     */     case 1: 
/*  674:1032 */       return "";
/*  675:     */     case 5: 
/*  676:1034 */       return getBooleanCellValue() ? "TRUE" : "FALSE";
/*  677:     */     case 4: 
/*  678:1036 */       return getStringCellValue();
/*  679:     */     case 3: 
/*  680:1038 */       return Double.toString(getNumericCellValue());
/*  681:     */     case 6: 
/*  682:1040 */       byte errVal = getErrorCellValue();
/*  683:1041 */       return FormulaError.forInt(errVal).getString();
/*  684:     */     case 2: 
/*  685:1043 */       if (this._value != null)
/*  686:     */       {
/*  687:1044 */         FormulaValue fv = (FormulaValue)this._value;
/*  688:1045 */         if (fv.getFormulaType() != CellType.FORMULA) {
/*  689:1046 */           return convertCellValueToString(fv.getFormulaType());
/*  690:     */         }
/*  691:     */       }
/*  692:1049 */       return "";
/*  693:     */     }
/*  694:1051 */     throw new IllegalStateException("Unexpected cell type (" + cellType + ")");
/*  695:     */   }
/*  696:     */   
/*  697:     */   static abstract class Property
/*  698:     */   {
/*  699:     */     static final int COMMENT = 1;
/*  700:     */     static final int HYPERLINK = 2;
/*  701:     */     Object _value;
/*  702:     */     Property _next;
/*  703:     */     
/*  704:     */     public Property(Object value)
/*  705:     */     {
/*  706:1065 */       this._value = value;
/*  707:     */     }
/*  708:     */     
/*  709:     */     abstract int getType();
/*  710:     */     
/*  711:     */     void setValue(Object value)
/*  712:     */     {
/*  713:1070 */       this._value = value;
/*  714:     */     }
/*  715:     */     
/*  716:     */     Object getValue()
/*  717:     */     {
/*  718:1074 */       return this._value;
/*  719:     */     }
/*  720:     */   }
/*  721:     */   
/*  722:     */   static class CommentProperty
/*  723:     */     extends Property
/*  724:     */   {
/*  725:     */     public CommentProperty(Object value)
/*  726:     */     {
/*  727:1081 */       super();
/*  728:     */     }
/*  729:     */     
/*  730:     */     public int getType()
/*  731:     */     {
/*  732:1086 */       return 1;
/*  733:     */     }
/*  734:     */   }
/*  735:     */   
/*  736:     */   static class HyperlinkProperty
/*  737:     */     extends Property
/*  738:     */   {
/*  739:     */     public HyperlinkProperty(Object value)
/*  740:     */     {
/*  741:1093 */       super();
/*  742:     */     }
/*  743:     */     
/*  744:     */     public int getType()
/*  745:     */     {
/*  746:1098 */       return 2;
/*  747:     */     }
/*  748:     */   }
/*  749:     */   
/*  750:     */   static abstract interface Value
/*  751:     */   {
/*  752:     */     public abstract CellType getType();
/*  753:     */   }
/*  754:     */   
/*  755:     */   static class NumericValue
/*  756:     */     implements Value
/*  757:     */   {
/*  758:     */     double _value;
/*  759:     */     
/*  760:     */     public CellType getType()
/*  761:     */     {
/*  762:1110 */       return CellType.NUMERIC;
/*  763:     */     }
/*  764:     */     
/*  765:     */     void setValue(double value)
/*  766:     */     {
/*  767:1114 */       this._value = value;
/*  768:     */     }
/*  769:     */     
/*  770:     */     double getValue()
/*  771:     */     {
/*  772:1118 */       return this._value;
/*  773:     */     }
/*  774:     */   }
/*  775:     */   
/*  776:     */   static abstract class StringValue
/*  777:     */     implements Value
/*  778:     */   {
/*  779:     */     public CellType getType()
/*  780:     */     {
/*  781:1125 */       return CellType.STRING;
/*  782:     */     }
/*  783:     */     
/*  784:     */     abstract boolean isRichText();
/*  785:     */   }
/*  786:     */   
/*  787:     */   static class PlainStringValue
/*  788:     */     extends StringValue
/*  789:     */   {
/*  790:     */     String _value;
/*  791:     */     
/*  792:     */     void setValue(String value)
/*  793:     */     {
/*  794:1135 */       this._value = value;
/*  795:     */     }
/*  796:     */     
/*  797:     */     String getValue()
/*  798:     */     {
/*  799:1139 */       return this._value;
/*  800:     */     }
/*  801:     */     
/*  802:     */     boolean isRichText()
/*  803:     */     {
/*  804:1144 */       return false;
/*  805:     */     }
/*  806:     */   }
/*  807:     */   
/*  808:     */   static class RichTextValue
/*  809:     */     extends StringValue
/*  810:     */   {
/*  811:     */     RichTextString _value;
/*  812:     */     
/*  813:     */     public CellType getType()
/*  814:     */     {
/*  815:1153 */       return CellType.STRING;
/*  816:     */     }
/*  817:     */     
/*  818:     */     void setValue(RichTextString value)
/*  819:     */     {
/*  820:1157 */       this._value = value;
/*  821:     */     }
/*  822:     */     
/*  823:     */     RichTextString getValue()
/*  824:     */     {
/*  825:1161 */       return this._value;
/*  826:     */     }
/*  827:     */     
/*  828:     */     boolean isRichText()
/*  829:     */     {
/*  830:1166 */       return true;
/*  831:     */     }
/*  832:     */   }
/*  833:     */   
/*  834:     */   static abstract class FormulaValue
/*  835:     */     implements Value
/*  836:     */   {
/*  837:     */     String _value;
/*  838:     */     
/*  839:     */     public CellType getType()
/*  840:     */     {
/*  841:1174 */       return CellType.FORMULA;
/*  842:     */     }
/*  843:     */     
/*  844:     */     void setValue(String value)
/*  845:     */     {
/*  846:1178 */       this._value = value;
/*  847:     */     }
/*  848:     */     
/*  849:     */     String getValue()
/*  850:     */     {
/*  851:1182 */       return this._value;
/*  852:     */     }
/*  853:     */     
/*  854:     */     abstract CellType getFormulaType();
/*  855:     */   }
/*  856:     */   
/*  857:     */   static class NumericFormulaValue
/*  858:     */     extends FormulaValue
/*  859:     */   {
/*  860:     */     double _preEvaluatedValue;
/*  861:     */     
/*  862:     */     CellType getFormulaType()
/*  863:     */     {
/*  864:1192 */       return CellType.NUMERIC;
/*  865:     */     }
/*  866:     */     
/*  867:     */     void setPreEvaluatedValue(double value)
/*  868:     */     {
/*  869:1196 */       this._preEvaluatedValue = value;
/*  870:     */     }
/*  871:     */     
/*  872:     */     double getPreEvaluatedValue()
/*  873:     */     {
/*  874:1200 */       return this._preEvaluatedValue;
/*  875:     */     }
/*  876:     */   }
/*  877:     */   
/*  878:     */   static class StringFormulaValue
/*  879:     */     extends FormulaValue
/*  880:     */   {
/*  881:     */     String _preEvaluatedValue;
/*  882:     */     
/*  883:     */     CellType getFormulaType()
/*  884:     */     {
/*  885:1209 */       return CellType.STRING;
/*  886:     */     }
/*  887:     */     
/*  888:     */     void setPreEvaluatedValue(String value)
/*  889:     */     {
/*  890:1213 */       this._preEvaluatedValue = value;
/*  891:     */     }
/*  892:     */     
/*  893:     */     String getPreEvaluatedValue()
/*  894:     */     {
/*  895:1217 */       return this._preEvaluatedValue;
/*  896:     */     }
/*  897:     */   }
/*  898:     */   
/*  899:     */   static class BooleanFormulaValue
/*  900:     */     extends FormulaValue
/*  901:     */   {
/*  902:     */     boolean _preEvaluatedValue;
/*  903:     */     
/*  904:     */     CellType getFormulaType()
/*  905:     */     {
/*  906:1226 */       return CellType.BOOLEAN;
/*  907:     */     }
/*  908:     */     
/*  909:     */     void setPreEvaluatedValue(boolean value)
/*  910:     */     {
/*  911:1230 */       this._preEvaluatedValue = value;
/*  912:     */     }
/*  913:     */     
/*  914:     */     boolean getPreEvaluatedValue()
/*  915:     */     {
/*  916:1234 */       return this._preEvaluatedValue;
/*  917:     */     }
/*  918:     */   }
/*  919:     */   
/*  920:     */   static class ErrorFormulaValue
/*  921:     */     extends FormulaValue
/*  922:     */   {
/*  923:     */     byte _preEvaluatedValue;
/*  924:     */     
/*  925:     */     CellType getFormulaType()
/*  926:     */     {
/*  927:1243 */       return CellType.ERROR;
/*  928:     */     }
/*  929:     */     
/*  930:     */     void setPreEvaluatedValue(byte value)
/*  931:     */     {
/*  932:1247 */       this._preEvaluatedValue = value;
/*  933:     */     }
/*  934:     */     
/*  935:     */     byte getPreEvaluatedValue()
/*  936:     */     {
/*  937:1251 */       return this._preEvaluatedValue;
/*  938:     */     }
/*  939:     */   }
/*  940:     */   
/*  941:     */   static class BlankValue
/*  942:     */     implements Value
/*  943:     */   {
/*  944:     */     public CellType getType()
/*  945:     */     {
/*  946:1258 */       return CellType.BLANK;
/*  947:     */     }
/*  948:     */   }
/*  949:     */   
/*  950:     */   static class BooleanValue
/*  951:     */     implements Value
/*  952:     */   {
/*  953:     */     boolean _value;
/*  954:     */     
/*  955:     */     public CellType getType()
/*  956:     */     {
/*  957:1266 */       return CellType.BOOLEAN;
/*  958:     */     }
/*  959:     */     
/*  960:     */     void setValue(boolean value)
/*  961:     */     {
/*  962:1270 */       this._value = value;
/*  963:     */     }
/*  964:     */     
/*  965:     */     boolean getValue()
/*  966:     */     {
/*  967:1274 */       return this._value;
/*  968:     */     }
/*  969:     */   }
/*  970:     */   
/*  971:     */   static class ErrorValue
/*  972:     */     implements Value
/*  973:     */   {
/*  974:     */     byte _value;
/*  975:     */     
/*  976:     */     public CellType getType()
/*  977:     */     {
/*  978:1282 */       return CellType.ERROR;
/*  979:     */     }
/*  980:     */     
/*  981:     */     void setValue(byte value)
/*  982:     */     {
/*  983:1286 */       this._value = value;
/*  984:     */     }
/*  985:     */     
/*  986:     */     byte getValue()
/*  987:     */     {
/*  988:1290 */       return this._value;
/*  989:     */     }
/*  990:     */   }
/*  991:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFCell

 * JD-Core Version:    0.7.0.1

 */