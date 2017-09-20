/*    1:     */ package org.apache.poi.xssf.usermodel;
/*    2:     */ 
/*    3:     */ import org.apache.poi.POIXMLException;
/*    4:     */ import org.apache.poi.POIXMLTypeLoader;
/*    5:     */ import org.apache.poi.ss.usermodel.BorderStyle;
/*    6:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*    7:     */ import org.apache.poi.ss.usermodel.FillPatternType;
/*    8:     */ import org.apache.poi.ss.usermodel.Font;
/*    9:     */ import org.apache.poi.ss.usermodel.HorizontalAlignment;
/*   10:     */ import org.apache.poi.ss.usermodel.IndexedColors;
/*   11:     */ import org.apache.poi.ss.usermodel.VerticalAlignment;
/*   12:     */ import org.apache.poi.util.Internal;
/*   13:     */ import org.apache.poi.xssf.model.StylesTable;
/*   14:     */ import org.apache.poi.xssf.model.ThemesTable;
/*   15:     */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellAlignment;
/*   16:     */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
/*   17:     */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
/*   18:     */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
/*   19:     */ import org.apache.xmlbeans.XmlException;
/*   20:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
/*   21:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder.Factory;
/*   22:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
/*   23:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment;
/*   24:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment.Factory;
/*   25:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection;
/*   26:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
/*   27:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill.Factory;
/*   28:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*   29:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont.Factory;
/*   30:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
/*   31:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
/*   32:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf.Factory;
/*   33:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum;
/*   34:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.Enum;
/*   35:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType.Enum;
/*   36:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment.Enum;
/*   37:     */ 
/*   38:     */ public class XSSFCellStyle
/*   39:     */   implements CellStyle
/*   40:     */ {
/*   41:     */   private int _cellXfId;
/*   42:     */   private final StylesTable _stylesSource;
/*   43:     */   private CTXf _cellXf;
/*   44:     */   private final CTXf _cellStyleXf;
/*   45:     */   private XSSFFont _font;
/*   46:     */   private XSSFCellAlignment _cellAlignment;
/*   47:     */   private ThemesTable _theme;
/*   48:     */   
/*   49:     */   public XSSFCellStyle(int cellXfId, int cellStyleXfId, StylesTable stylesSource, ThemesTable theme)
/*   50:     */   {
/*   51:  74 */     this._cellXfId = cellXfId;
/*   52:  75 */     this._stylesSource = stylesSource;
/*   53:  76 */     this._cellXf = stylesSource.getCellXfAt(this._cellXfId);
/*   54:  77 */     this._cellStyleXf = (cellStyleXfId == -1 ? null : stylesSource.getCellStyleXfAt(cellStyleXfId));
/*   55:  78 */     this._theme = theme;
/*   56:     */   }
/*   57:     */   
/*   58:     */   @Internal
/*   59:     */   public CTXf getCoreXf()
/*   60:     */   {
/*   61:  86 */     return this._cellXf;
/*   62:     */   }
/*   63:     */   
/*   64:     */   @Internal
/*   65:     */   public CTXf getStyleXf()
/*   66:     */   {
/*   67:  94 */     return this._cellStyleXf;
/*   68:     */   }
/*   69:     */   
/*   70:     */   public XSSFCellStyle(StylesTable stylesSource)
/*   71:     */   {
/*   72: 101 */     this._stylesSource = stylesSource;
/*   73:     */     
/*   74:     */ 
/*   75: 104 */     this._cellXf = CTXf.Factory.newInstance();
/*   76: 105 */     this._cellStyleXf = null;
/*   77:     */   }
/*   78:     */   
/*   79:     */   public void verifyBelongsToStylesSource(StylesTable src)
/*   80:     */   {
/*   81: 118 */     if (this._stylesSource != src) {
/*   82: 119 */       throw new IllegalArgumentException("This Style does not belong to the supplied Workbook Stlyes Source. Are you trying to assign a style from one workbook to the cell of a differnt workbook?");
/*   83:     */     }
/*   84:     */   }
/*   85:     */   
/*   86:     */   public void cloneStyleFrom(CellStyle source)
/*   87:     */   {
/*   88: 137 */     if ((source instanceof XSSFCellStyle))
/*   89:     */     {
/*   90: 138 */       XSSFCellStyle src = (XSSFCellStyle)source;
/*   91: 141 */       if (src._stylesSource == this._stylesSource)
/*   92:     */       {
/*   93: 143 */         this._cellXf.set(src.getCoreXf());
/*   94: 144 */         this._cellStyleXf.set(src.getStyleXf());
/*   95:     */       }
/*   96:     */       else
/*   97:     */       {
/*   98:     */         try
/*   99:     */         {
/*  100: 150 */           if (this._cellXf.isSetAlignment()) {
/*  101: 151 */             this._cellXf.unsetAlignment();
/*  102:     */           }
/*  103: 152 */           if (this._cellXf.isSetExtLst()) {
/*  104: 153 */             this._cellXf.unsetExtLst();
/*  105:     */           }
/*  106: 156 */           this._cellXf = CTXf.Factory.parse(src.getCoreXf().toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  107:     */           
/*  108:     */ 
/*  109:     */ 
/*  110:     */ 
/*  111: 161 */           CTFill fill = CTFill.Factory.parse(src.getCTFill().toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  112:     */           
/*  113:     */ 
/*  114: 164 */           addFill(fill);
/*  115:     */           
/*  116:     */ 
/*  117: 167 */           CTBorder border = CTBorder.Factory.parse(src.getCTBorder().toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  118:     */           
/*  119:     */ 
/*  120: 170 */           addBorder(border);
/*  121:     */           
/*  122:     */ 
/*  123: 173 */           this._stylesSource.replaceCellXfAt(this._cellXfId, this._cellXf);
/*  124:     */         }
/*  125:     */         catch (XmlException e)
/*  126:     */         {
/*  127: 175 */           throw new POIXMLException(e);
/*  128:     */         }
/*  129: 179 */         String fmt = src.getDataFormatString();
/*  130: 180 */         setDataFormat(new XSSFDataFormat(this._stylesSource).getFormat(fmt));
/*  131:     */         try
/*  132:     */         {
/*  133: 186 */           CTFont ctFont = CTFont.Factory.parse(src.getFont().getCTFont().toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  134:     */           
/*  135:     */ 
/*  136: 189 */           XSSFFont font = new XSSFFont(ctFont);
/*  137: 190 */           font.registerTo(this._stylesSource);
/*  138: 191 */           setFont(font);
/*  139:     */         }
/*  140:     */         catch (XmlException e)
/*  141:     */         {
/*  142: 193 */           throw new POIXMLException(e);
/*  143:     */         }
/*  144:     */       }
/*  145: 198 */       this._font = null;
/*  146: 199 */       this._cellAlignment = null;
/*  147:     */     }
/*  148:     */     else
/*  149:     */     {
/*  150: 201 */       throw new IllegalArgumentException("Can only clone from one XSSFCellStyle to another, not between HSSFCellStyle and XSSFCellStyle");
/*  151:     */     }
/*  152:     */   }
/*  153:     */   
/*  154:     */   private void addFill(CTFill fill)
/*  155:     */   {
/*  156: 206 */     int idx = this._stylesSource.putFill(new XSSFCellFill(fill, this._stylesSource.getIndexedColors()));
/*  157:     */     
/*  158: 208 */     this._cellXf.setFillId(idx);
/*  159: 209 */     this._cellXf.setApplyFill(true);
/*  160:     */   }
/*  161:     */   
/*  162:     */   private void addBorder(CTBorder border)
/*  163:     */   {
/*  164: 213 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(border, this._theme, this._stylesSource.getIndexedColors()));
/*  165:     */     
/*  166: 215 */     this._cellXf.setBorderId(idx);
/*  167: 216 */     this._cellXf.setApplyBorder(true);
/*  168:     */   }
/*  169:     */   
/*  170:     */   /**
/*  171:     */    * @deprecated
/*  172:     */    */
/*  173:     */   public short getAlignment()
/*  174:     */   {
/*  175: 227 */     return getAlignmentEnum().getCode();
/*  176:     */   }
/*  177:     */   
/*  178:     */   public HorizontalAlignment getAlignmentEnum()
/*  179:     */   {
/*  180: 237 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  181: 238 */     if ((align != null) && (align.isSetHorizontal())) {
/*  182: 239 */       return HorizontalAlignment.forInt(align.getHorizontal().intValue() - 1);
/*  183:     */     }
/*  184: 241 */     return HorizontalAlignment.GENERAL;
/*  185:     */   }
/*  186:     */   
/*  187:     */   public BorderStyle getBorderBottomEnum()
/*  188:     */   {
/*  189: 253 */     if (!this._cellXf.getApplyBorder()) {
/*  190: 253 */       return BorderStyle.NONE;
/*  191:     */     }
/*  192: 255 */     int idx = (int)this._cellXf.getBorderId();
/*  193: 256 */     CTBorder ct = this._stylesSource.getBorderAt(idx).getCTBorder();
/*  194: 257 */     STBorderStyle.Enum ptrn = ct.isSetBottom() ? ct.getBottom().getStyle() : null;
/*  195: 258 */     if (ptrn == null) {
/*  196: 259 */       return BorderStyle.NONE;
/*  197:     */     }
/*  198: 261 */     return BorderStyle.valueOf((short)(ptrn.intValue() - 1));
/*  199:     */   }
/*  200:     */   
/*  201:     */   /**
/*  202:     */    * @deprecated
/*  203:     */    */
/*  204:     */   public short getBorderBottom()
/*  205:     */   {
/*  206: 271 */     return getBorderBottomEnum().getCode();
/*  207:     */   }
/*  208:     */   
/*  209:     */   public BorderStyle getBorderLeftEnum()
/*  210:     */   {
/*  211: 283 */     if (!this._cellXf.getApplyBorder()) {
/*  212: 283 */       return BorderStyle.NONE;
/*  213:     */     }
/*  214: 285 */     int idx = (int)this._cellXf.getBorderId();
/*  215: 286 */     CTBorder ct = this._stylesSource.getBorderAt(idx).getCTBorder();
/*  216: 287 */     STBorderStyle.Enum ptrn = ct.isSetLeft() ? ct.getLeft().getStyle() : null;
/*  217: 288 */     if (ptrn == null) {
/*  218: 289 */       return BorderStyle.NONE;
/*  219:     */     }
/*  220: 291 */     return BorderStyle.valueOf((short)(ptrn.intValue() - 1));
/*  221:     */   }
/*  222:     */   
/*  223:     */   /**
/*  224:     */    * @deprecated
/*  225:     */    */
/*  226:     */   public short getBorderLeft()
/*  227:     */   {
/*  228: 302 */     return getBorderLeftEnum().getCode();
/*  229:     */   }
/*  230:     */   
/*  231:     */   public BorderStyle getBorderRightEnum()
/*  232:     */   {
/*  233: 314 */     if (!this._cellXf.getApplyBorder()) {
/*  234: 314 */       return BorderStyle.NONE;
/*  235:     */     }
/*  236: 316 */     int idx = (int)this._cellXf.getBorderId();
/*  237: 317 */     CTBorder ct = this._stylesSource.getBorderAt(idx).getCTBorder();
/*  238: 318 */     STBorderStyle.Enum ptrn = ct.isSetRight() ? ct.getRight().getStyle() : null;
/*  239: 319 */     if (ptrn == null) {
/*  240: 320 */       return BorderStyle.NONE;
/*  241:     */     }
/*  242: 322 */     return BorderStyle.valueOf((short)(ptrn.intValue() - 1));
/*  243:     */   }
/*  244:     */   
/*  245:     */   /**
/*  246:     */    * @deprecated
/*  247:     */    */
/*  248:     */   public short getBorderRight()
/*  249:     */   {
/*  250: 332 */     return getBorderRightEnum().getCode();
/*  251:     */   }
/*  252:     */   
/*  253:     */   public BorderStyle getBorderTopEnum()
/*  254:     */   {
/*  255: 344 */     if (!this._cellXf.getApplyBorder()) {
/*  256: 344 */       return BorderStyle.NONE;
/*  257:     */     }
/*  258: 346 */     int idx = (int)this._cellXf.getBorderId();
/*  259: 347 */     CTBorder ct = this._stylesSource.getBorderAt(idx).getCTBorder();
/*  260: 348 */     STBorderStyle.Enum ptrn = ct.isSetTop() ? ct.getTop().getStyle() : null;
/*  261: 349 */     if (ptrn == null) {
/*  262: 350 */       return BorderStyle.NONE;
/*  263:     */     }
/*  264: 352 */     return BorderStyle.valueOf((short)(ptrn.intValue() - 1));
/*  265:     */   }
/*  266:     */   
/*  267:     */   /**
/*  268:     */    * @deprecated
/*  269:     */    */
/*  270:     */   public short getBorderTop()
/*  271:     */   {
/*  272: 362 */     return getBorderTopEnum().getCode();
/*  273:     */   }
/*  274:     */   
/*  275:     */   public short getBottomBorderColor()
/*  276:     */   {
/*  277: 374 */     XSSFColor clr = getBottomBorderXSSFColor();
/*  278: 375 */     return clr == null ? IndexedColors.BLACK.getIndex() : clr.getIndexed();
/*  279:     */   }
/*  280:     */   
/*  281:     */   public XSSFColor getBottomBorderXSSFColor()
/*  282:     */   {
/*  283: 384 */     if (!this._cellXf.getApplyBorder()) {
/*  284: 384 */       return null;
/*  285:     */     }
/*  286: 386 */     int idx = (int)this._cellXf.getBorderId();
/*  287: 387 */     XSSFCellBorder border = this._stylesSource.getBorderAt(idx);
/*  288:     */     
/*  289: 389 */     return border.getBorderColor(BorderSide.BOTTOM);
/*  290:     */   }
/*  291:     */   
/*  292:     */   public short getDataFormat()
/*  293:     */   {
/*  294: 399 */     return (short)(int)this._cellXf.getNumFmtId();
/*  295:     */   }
/*  296:     */   
/*  297:     */   public String getDataFormatString()
/*  298:     */   {
/*  299: 410 */     int idx = getDataFormat();
/*  300: 411 */     return new XSSFDataFormat(this._stylesSource).getFormat((short)idx);
/*  301:     */   }
/*  302:     */   
/*  303:     */   public short getFillBackgroundColor()
/*  304:     */   {
/*  305: 425 */     XSSFColor clr = getFillBackgroundXSSFColor();
/*  306: 426 */     return clr == null ? IndexedColors.AUTOMATIC.getIndex() : clr.getIndexed();
/*  307:     */   }
/*  308:     */   
/*  309:     */   public XSSFColor getFillBackgroundColorColor()
/*  310:     */   {
/*  311: 431 */     return getFillBackgroundXSSFColor();
/*  312:     */   }
/*  313:     */   
/*  314:     */   public XSSFColor getFillBackgroundXSSFColor()
/*  315:     */   {
/*  316: 445 */     if ((this._cellXf.isSetApplyFill()) && (!this._cellXf.getApplyFill())) {
/*  317: 445 */       return null;
/*  318:     */     }
/*  319: 447 */     int fillIndex = (int)this._cellXf.getFillId();
/*  320: 448 */     XSSFCellFill fg = this._stylesSource.getFillAt(fillIndex);
/*  321:     */     
/*  322: 450 */     XSSFColor fillBackgroundColor = fg.getFillBackgroundColor();
/*  323: 451 */     if ((fillBackgroundColor != null) && (this._theme != null)) {
/*  324: 452 */       this._theme.inheritFromThemeAsRequired(fillBackgroundColor);
/*  325:     */     }
/*  326: 454 */     return fillBackgroundColor;
/*  327:     */   }
/*  328:     */   
/*  329:     */   public short getFillForegroundColor()
/*  330:     */   {
/*  331: 468 */     XSSFColor clr = getFillForegroundXSSFColor();
/*  332: 469 */     return clr == null ? IndexedColors.AUTOMATIC.getIndex() : clr.getIndexed();
/*  333:     */   }
/*  334:     */   
/*  335:     */   public XSSFColor getFillForegroundColorColor()
/*  336:     */   {
/*  337: 474 */     return getFillForegroundXSSFColor();
/*  338:     */   }
/*  339:     */   
/*  340:     */   public XSSFColor getFillForegroundXSSFColor()
/*  341:     */   {
/*  342: 484 */     if ((this._cellXf.isSetApplyFill()) && (!this._cellXf.getApplyFill())) {
/*  343: 484 */       return null;
/*  344:     */     }
/*  345: 486 */     int fillIndex = (int)this._cellXf.getFillId();
/*  346: 487 */     XSSFCellFill fg = this._stylesSource.getFillAt(fillIndex);
/*  347:     */     
/*  348: 489 */     XSSFColor fillForegroundColor = fg.getFillForegroundColor();
/*  349: 490 */     if ((fillForegroundColor != null) && (this._theme != null)) {
/*  350: 491 */       this._theme.inheritFromThemeAsRequired(fillForegroundColor);
/*  351:     */     }
/*  352: 493 */     return fillForegroundColor;
/*  353:     */   }
/*  354:     */   
/*  355:     */   /**
/*  356:     */    * @deprecated
/*  357:     */    */
/*  358:     */   public short getFillPattern()
/*  359:     */   {
/*  360: 503 */     return getFillPatternEnum().getCode();
/*  361:     */   }
/*  362:     */   
/*  363:     */   public FillPatternType getFillPatternEnum()
/*  364:     */   {
/*  365: 514 */     if ((this._cellXf.isSetApplyFill()) && (!this._cellXf.getApplyFill())) {
/*  366: 514 */       return FillPatternType.NO_FILL;
/*  367:     */     }
/*  368: 516 */     int fillIndex = (int)this._cellXf.getFillId();
/*  369: 517 */     XSSFCellFill fill = this._stylesSource.getFillAt(fillIndex);
/*  370:     */     
/*  371: 519 */     STPatternType.Enum ptrn = fill.getPatternType();
/*  372: 520 */     if (ptrn == null) {
/*  373: 520 */       return FillPatternType.NO_FILL;
/*  374:     */     }
/*  375: 521 */     return FillPatternType.forInt(ptrn.intValue() - 1);
/*  376:     */   }
/*  377:     */   
/*  378:     */   public XSSFFont getFont()
/*  379:     */   {
/*  380: 529 */     if (this._font == null) {
/*  381: 530 */       this._font = this._stylesSource.getFontAt(getFontId());
/*  382:     */     }
/*  383: 532 */     return this._font;
/*  384:     */   }
/*  385:     */   
/*  386:     */   public short getFontIndex()
/*  387:     */   {
/*  388: 543 */     return (short)getFontId();
/*  389:     */   }
/*  390:     */   
/*  391:     */   public boolean getHidden()
/*  392:     */   {
/*  393: 553 */     if ((!this._cellXf.isSetProtection()) || (!this._cellXf.getProtection().isSetHidden())) {
/*  394: 554 */       return false;
/*  395:     */     }
/*  396: 556 */     return this._cellXf.getProtection().getHidden();
/*  397:     */   }
/*  398:     */   
/*  399:     */   public short getIndention()
/*  400:     */   {
/*  401: 566 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  402: 567 */     return (short)(int)(align == null ? 0L : align.getIndent());
/*  403:     */   }
/*  404:     */   
/*  405:     */   public short getIndex()
/*  406:     */   {
/*  407: 577 */     return (short)this._cellXfId;
/*  408:     */   }
/*  409:     */   
/*  410:     */   protected int getUIndex()
/*  411:     */   {
/*  412: 588 */     return this._cellXfId;
/*  413:     */   }
/*  414:     */   
/*  415:     */   public short getLeftBorderColor()
/*  416:     */   {
/*  417: 599 */     XSSFColor clr = getLeftBorderXSSFColor();
/*  418: 600 */     return clr == null ? IndexedColors.BLACK.getIndex() : clr.getIndexed();
/*  419:     */   }
/*  420:     */   
/*  421:     */   public XSSFColor getLeftBorderXSSFColor()
/*  422:     */   {
/*  423: 610 */     if (!this._cellXf.getApplyBorder()) {
/*  424: 610 */       return null;
/*  425:     */     }
/*  426: 612 */     int idx = (int)this._cellXf.getBorderId();
/*  427: 613 */     XSSFCellBorder border = this._stylesSource.getBorderAt(idx);
/*  428:     */     
/*  429: 615 */     return border.getBorderColor(BorderSide.LEFT);
/*  430:     */   }
/*  431:     */   
/*  432:     */   public boolean getLocked()
/*  433:     */   {
/*  434: 625 */     if ((!this._cellXf.isSetProtection()) || (!this._cellXf.getProtection().isSetLocked())) {
/*  435: 626 */       return true;
/*  436:     */     }
/*  437: 628 */     return this._cellXf.getProtection().getLocked();
/*  438:     */   }
/*  439:     */   
/*  440:     */   public boolean getQuotePrefixed()
/*  441:     */   {
/*  442: 636 */     return this._cellXf.getQuotePrefix();
/*  443:     */   }
/*  444:     */   
/*  445:     */   public short getRightBorderColor()
/*  446:     */   {
/*  447: 647 */     XSSFColor clr = getRightBorderXSSFColor();
/*  448: 648 */     return clr == null ? IndexedColors.BLACK.getIndex() : clr.getIndexed();
/*  449:     */   }
/*  450:     */   
/*  451:     */   public XSSFColor getRightBorderXSSFColor()
/*  452:     */   {
/*  453: 656 */     if (!this._cellXf.getApplyBorder()) {
/*  454: 656 */       return null;
/*  455:     */     }
/*  456: 658 */     int idx = (int)this._cellXf.getBorderId();
/*  457: 659 */     XSSFCellBorder border = this._stylesSource.getBorderAt(idx);
/*  458:     */     
/*  459: 661 */     return border.getBorderColor(BorderSide.RIGHT);
/*  460:     */   }
/*  461:     */   
/*  462:     */   public short getRotation()
/*  463:     */   {
/*  464: 680 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  465: 681 */     return (short)(int)(align == null ? 0L : align.getTextRotation());
/*  466:     */   }
/*  467:     */   
/*  468:     */   public boolean getShrinkToFit()
/*  469:     */   {
/*  470: 686 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  471: 687 */     return (align != null) && (align.getShrinkToFit());
/*  472:     */   }
/*  473:     */   
/*  474:     */   public short getTopBorderColor()
/*  475:     */   {
/*  476: 698 */     XSSFColor clr = getTopBorderXSSFColor();
/*  477: 699 */     return clr == null ? IndexedColors.BLACK.getIndex() : clr.getIndexed();
/*  478:     */   }
/*  479:     */   
/*  480:     */   public XSSFColor getTopBorderXSSFColor()
/*  481:     */   {
/*  482: 708 */     if (!this._cellXf.getApplyBorder()) {
/*  483: 708 */       return null;
/*  484:     */     }
/*  485: 710 */     int idx = (int)this._cellXf.getBorderId();
/*  486: 711 */     XSSFCellBorder border = this._stylesSource.getBorderAt(idx);
/*  487:     */     
/*  488: 713 */     return border.getBorderColor(BorderSide.TOP);
/*  489:     */   }
/*  490:     */   
/*  491:     */   /**
/*  492:     */    * @deprecated
/*  493:     */    */
/*  494:     */   public short getVerticalAlignment()
/*  495:     */   {
/*  496: 724 */     return getVerticalAlignmentEnum().getCode();
/*  497:     */   }
/*  498:     */   
/*  499:     */   public VerticalAlignment getVerticalAlignmentEnum()
/*  500:     */   {
/*  501: 734 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  502: 735 */     if ((align != null) && (align.isSetVertical())) {
/*  503: 736 */       return VerticalAlignment.forInt(align.getVertical().intValue() - 1);
/*  504:     */     }
/*  505: 738 */     return VerticalAlignment.BOTTOM;
/*  506:     */   }
/*  507:     */   
/*  508:     */   public boolean getWrapText()
/*  509:     */   {
/*  510: 748 */     CTCellAlignment align = this._cellXf.getAlignment();
/*  511: 749 */     return (align != null) && (align.getWrapText());
/*  512:     */   }
/*  513:     */   
/*  514:     */   public void setAlignment(HorizontalAlignment align)
/*  515:     */   {
/*  516: 759 */     getCellAlignment().setHorizontal(align);
/*  517:     */   }
/*  518:     */   
/*  519:     */   public void setBorderBottom(BorderStyle border)
/*  520:     */   {
/*  521: 771 */     CTBorder ct = getCTBorder();
/*  522: 772 */     CTBorderPr pr = ct.isSetBottom() ? ct.getBottom() : ct.addNewBottom();
/*  523: 773 */     if (border == BorderStyle.NONE) {
/*  524: 773 */       ct.unsetBottom();
/*  525:     */     } else {
/*  526: 774 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/*  527:     */     }
/*  528: 776 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  529:     */     
/*  530: 778 */     this._cellXf.setBorderId(idx);
/*  531: 779 */     this._cellXf.setApplyBorder(true);
/*  532:     */   }
/*  533:     */   
/*  534:     */   public void setBorderLeft(BorderStyle border)
/*  535:     */   {
/*  536: 790 */     CTBorder ct = getCTBorder();
/*  537: 791 */     CTBorderPr pr = ct.isSetLeft() ? ct.getLeft() : ct.addNewLeft();
/*  538: 792 */     if (border == BorderStyle.NONE) {
/*  539: 792 */       ct.unsetLeft();
/*  540:     */     } else {
/*  541: 793 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/*  542:     */     }
/*  543: 795 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  544:     */     
/*  545: 797 */     this._cellXf.setBorderId(idx);
/*  546: 798 */     this._cellXf.setApplyBorder(true);
/*  547:     */   }
/*  548:     */   
/*  549:     */   public void setBorderRight(BorderStyle border)
/*  550:     */   {
/*  551: 809 */     CTBorder ct = getCTBorder();
/*  552: 810 */     CTBorderPr pr = ct.isSetRight() ? ct.getRight() : ct.addNewRight();
/*  553: 811 */     if (border == BorderStyle.NONE) {
/*  554: 811 */       ct.unsetRight();
/*  555:     */     } else {
/*  556: 812 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/*  557:     */     }
/*  558: 814 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  559:     */     
/*  560: 816 */     this._cellXf.setBorderId(idx);
/*  561: 817 */     this._cellXf.setApplyBorder(true);
/*  562:     */   }
/*  563:     */   
/*  564:     */   public void setBorderTop(BorderStyle border)
/*  565:     */   {
/*  566: 828 */     CTBorder ct = getCTBorder();
/*  567: 829 */     CTBorderPr pr = ct.isSetTop() ? ct.getTop() : ct.addNewTop();
/*  568: 830 */     if (border == BorderStyle.NONE) {
/*  569: 830 */       ct.unsetTop();
/*  570:     */     } else {
/*  571: 831 */       pr.setStyle(STBorderStyle.Enum.forInt(border.getCode() + 1));
/*  572:     */     }
/*  573: 833 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  574:     */     
/*  575: 835 */     this._cellXf.setBorderId(idx);
/*  576: 836 */     this._cellXf.setApplyBorder(true);
/*  577:     */   }
/*  578:     */   
/*  579:     */   public void setBottomBorderColor(short color)
/*  580:     */   {
/*  581: 846 */     XSSFColor clr = new XSSFColor();
/*  582: 847 */     clr.setIndexed(color);
/*  583: 848 */     setBottomBorderColor(clr);
/*  584:     */   }
/*  585:     */   
/*  586:     */   public void setBottomBorderColor(XSSFColor color)
/*  587:     */   {
/*  588: 857 */     CTBorder ct = getCTBorder();
/*  589: 858 */     if ((color == null) && (!ct.isSetBottom())) {
/*  590: 858 */       return;
/*  591:     */     }
/*  592: 860 */     CTBorderPr pr = ct.isSetBottom() ? ct.getBottom() : ct.addNewBottom();
/*  593: 861 */     if (color != null) {
/*  594: 861 */       pr.setColor(color.getCTColor());
/*  595:     */     } else {
/*  596: 862 */       pr.unsetColor();
/*  597:     */     }
/*  598: 864 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  599:     */     
/*  600: 866 */     this._cellXf.setBorderId(idx);
/*  601: 867 */     this._cellXf.setApplyBorder(true);
/*  602:     */   }
/*  603:     */   
/*  604:     */   public void setDataFormat(short fmt)
/*  605:     */   {
/*  606: 878 */     setDataFormat(fmt & 0xFFFF);
/*  607:     */   }
/*  608:     */   
/*  609:     */   public void setDataFormat(int fmt)
/*  610:     */   {
/*  611: 886 */     this._cellXf.setApplyNumberFormat(true);
/*  612: 887 */     this._cellXf.setNumFmtId(fmt);
/*  613:     */   }
/*  614:     */   
/*  615:     */   public void setFillBackgroundColor(XSSFColor color)
/*  616:     */   {
/*  617: 916 */     CTFill ct = getCTFill();
/*  618: 917 */     CTPatternFill ptrn = ct.getPatternFill();
/*  619: 918 */     if (color == null)
/*  620:     */     {
/*  621: 919 */       if ((ptrn != null) && (ptrn.isSetBgColor())) {
/*  622: 919 */         ptrn.unsetBgColor();
/*  623:     */       }
/*  624:     */     }
/*  625:     */     else
/*  626:     */     {
/*  627: 921 */       if (ptrn == null) {
/*  628: 921 */         ptrn = ct.addNewPatternFill();
/*  629:     */       }
/*  630: 922 */       ptrn.setBgColor(color.getCTColor());
/*  631:     */     }
/*  632: 925 */     addFill(ct);
/*  633:     */   }
/*  634:     */   
/*  635:     */   public void setFillBackgroundColor(short bg)
/*  636:     */   {
/*  637: 956 */     XSSFColor clr = new XSSFColor();
/*  638: 957 */     clr.setIndexed(bg);
/*  639: 958 */     setFillBackgroundColor(clr);
/*  640:     */   }
/*  641:     */   
/*  642:     */   public void setFillForegroundColor(XSSFColor color)
/*  643:     */   {
/*  644: 969 */     CTFill ct = getCTFill();
/*  645:     */     
/*  646: 971 */     CTPatternFill ptrn = ct.getPatternFill();
/*  647: 972 */     if (color == null)
/*  648:     */     {
/*  649: 973 */       if ((ptrn != null) && (ptrn.isSetFgColor())) {
/*  650: 973 */         ptrn.unsetFgColor();
/*  651:     */       }
/*  652:     */     }
/*  653:     */     else
/*  654:     */     {
/*  655: 975 */       if (ptrn == null) {
/*  656: 975 */         ptrn = ct.addNewPatternFill();
/*  657:     */       }
/*  658: 976 */       ptrn.setFgColor(color.getCTColor());
/*  659:     */     }
/*  660: 979 */     addFill(ct);
/*  661:     */   }
/*  662:     */   
/*  663:     */   public void setFillForegroundColor(short fg)
/*  664:     */   {
/*  665: 991 */     XSSFColor clr = new XSSFColor();
/*  666: 992 */     clr.setIndexed(fg);
/*  667: 993 */     setFillForegroundColor(clr);
/*  668:     */   }
/*  669:     */   
/*  670:     */   private CTFill getCTFill()
/*  671:     */   {
/*  672:     */     CTFill ct;
/*  673:     */     CTFill ct;
/*  674:1002 */     if ((!this._cellXf.isSetApplyFill()) || (this._cellXf.getApplyFill()))
/*  675:     */     {
/*  676:1003 */       int fillIndex = (int)this._cellXf.getFillId();
/*  677:1004 */       XSSFCellFill cf = this._stylesSource.getFillAt(fillIndex);
/*  678:     */       
/*  679:1006 */       ct = (CTFill)cf.getCTFill().copy();
/*  680:     */     }
/*  681:     */     else
/*  682:     */     {
/*  683:1008 */       ct = CTFill.Factory.newInstance();
/*  684:     */     }
/*  685:1010 */     return ct;
/*  686:     */   }
/*  687:     */   
/*  688:     */   private CTBorder getCTBorder()
/*  689:     */   {
/*  690:     */     CTBorder ct;
/*  691:     */     CTBorder ct;
/*  692:1018 */     if (this._cellXf.getApplyBorder())
/*  693:     */     {
/*  694:1019 */       int idx = (int)this._cellXf.getBorderId();
/*  695:1020 */       XSSFCellBorder cf = this._stylesSource.getBorderAt(idx);
/*  696:     */       
/*  697:1022 */       ct = (CTBorder)cf.getCTBorder().copy();
/*  698:     */     }
/*  699:     */     else
/*  700:     */     {
/*  701:1024 */       ct = CTBorder.Factory.newInstance();
/*  702:     */     }
/*  703:1026 */     return ct;
/*  704:     */   }
/*  705:     */   
/*  706:     */   public void setFillPattern(FillPatternType pattern)
/*  707:     */   {
/*  708:1040 */     CTFill ct = getCTFill();
/*  709:1041 */     CTPatternFill ctptrn = ct.isSetPatternFill() ? ct.getPatternFill() : ct.addNewPatternFill();
/*  710:1042 */     if ((pattern == FillPatternType.NO_FILL) && (ctptrn.isSetPatternType())) {
/*  711:1043 */       ctptrn.unsetPatternType();
/*  712:     */     } else {
/*  713:1045 */       ctptrn.setPatternType(STPatternType.Enum.forInt(pattern.getCode() + 1));
/*  714:     */     }
/*  715:1048 */     addFill(ct);
/*  716:     */   }
/*  717:     */   
/*  718:     */   public void setFont(Font font)
/*  719:     */   {
/*  720:1060 */     if (font != null)
/*  721:     */     {
/*  722:1061 */       long index = font.getIndex();
/*  723:1062 */       this._cellXf.setFontId(index);
/*  724:1063 */       this._cellXf.setApplyFont(true);
/*  725:     */     }
/*  726:     */     else
/*  727:     */     {
/*  728:1065 */       this._cellXf.setApplyFont(false);
/*  729:     */     }
/*  730:     */   }
/*  731:     */   
/*  732:     */   public void setHidden(boolean hidden)
/*  733:     */   {
/*  734:1076 */     if (!this._cellXf.isSetProtection()) {
/*  735:1077 */       this._cellXf.addNewProtection();
/*  736:     */     }
/*  737:1079 */     this._cellXf.getProtection().setHidden(hidden);
/*  738:     */   }
/*  739:     */   
/*  740:     */   public void setIndention(short indent)
/*  741:     */   {
/*  742:1089 */     getCellAlignment().setIndent(indent);
/*  743:     */   }
/*  744:     */   
/*  745:     */   public void setLeftBorderColor(short color)
/*  746:     */   {
/*  747:1100 */     XSSFColor clr = new XSSFColor();
/*  748:1101 */     clr.setIndexed(color);
/*  749:1102 */     setLeftBorderColor(clr);
/*  750:     */   }
/*  751:     */   
/*  752:     */   public void setLeftBorderColor(XSSFColor color)
/*  753:     */   {
/*  754:1111 */     CTBorder ct = getCTBorder();
/*  755:1112 */     if ((color == null) && (!ct.isSetLeft())) {
/*  756:1112 */       return;
/*  757:     */     }
/*  758:1114 */     CTBorderPr pr = ct.isSetLeft() ? ct.getLeft() : ct.addNewLeft();
/*  759:1115 */     if (color != null) {
/*  760:1115 */       pr.setColor(color.getCTColor());
/*  761:     */     } else {
/*  762:1116 */       pr.unsetColor();
/*  763:     */     }
/*  764:1118 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  765:     */     
/*  766:1120 */     this._cellXf.setBorderId(idx);
/*  767:1121 */     this._cellXf.setApplyBorder(true);
/*  768:     */   }
/*  769:     */   
/*  770:     */   public void setLocked(boolean locked)
/*  771:     */   {
/*  772:1131 */     if (!this._cellXf.isSetProtection()) {
/*  773:1132 */       this._cellXf.addNewProtection();
/*  774:     */     }
/*  775:1134 */     this._cellXf.getProtection().setLocked(locked);
/*  776:     */   }
/*  777:     */   
/*  778:     */   public void setQuotePrefixed(boolean quotePrefix)
/*  779:     */   {
/*  780:1144 */     this._cellXf.setQuotePrefix(quotePrefix);
/*  781:     */   }
/*  782:     */   
/*  783:     */   public void setRightBorderColor(short color)
/*  784:     */   {
/*  785:1155 */     XSSFColor clr = new XSSFColor();
/*  786:1156 */     clr.setIndexed(color);
/*  787:1157 */     setRightBorderColor(clr);
/*  788:     */   }
/*  789:     */   
/*  790:     */   public void setRightBorderColor(XSSFColor color)
/*  791:     */   {
/*  792:1166 */     CTBorder ct = getCTBorder();
/*  793:1167 */     if ((color == null) && (!ct.isSetRight())) {
/*  794:1167 */       return;
/*  795:     */     }
/*  796:1169 */     CTBorderPr pr = ct.isSetRight() ? ct.getRight() : ct.addNewRight();
/*  797:1170 */     if (color != null) {
/*  798:1170 */       pr.setColor(color.getCTColor());
/*  799:     */     } else {
/*  800:1171 */       pr.unsetColor();
/*  801:     */     }
/*  802:1173 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  803:     */     
/*  804:1175 */     this._cellXf.setBorderId(idx);
/*  805:1176 */     this._cellXf.setApplyBorder(true);
/*  806:     */   }
/*  807:     */   
/*  808:     */   public void setRotation(short rotation)
/*  809:     */   {
/*  810:1200 */     getCellAlignment().setTextRotation(rotation);
/*  811:     */   }
/*  812:     */   
/*  813:     */   public void setTopBorderColor(short color)
/*  814:     */   {
/*  815:1212 */     XSSFColor clr = new XSSFColor();
/*  816:1213 */     clr.setIndexed(color);
/*  817:1214 */     setTopBorderColor(clr);
/*  818:     */   }
/*  819:     */   
/*  820:     */   public void setTopBorderColor(XSSFColor color)
/*  821:     */   {
/*  822:1223 */     CTBorder ct = getCTBorder();
/*  823:1224 */     if ((color == null) && (!ct.isSetTop())) {
/*  824:1224 */       return;
/*  825:     */     }
/*  826:1226 */     CTBorderPr pr = ct.isSetTop() ? ct.getTop() : ct.addNewTop();
/*  827:1227 */     if (color != null) {
/*  828:1227 */       pr.setColor(color.getCTColor());
/*  829:     */     } else {
/*  830:1228 */       pr.unsetColor();
/*  831:     */     }
/*  832:1230 */     int idx = this._stylesSource.putBorder(new XSSFCellBorder(ct, this._theme, this._stylesSource.getIndexedColors()));
/*  833:     */     
/*  834:1232 */     this._cellXf.setBorderId(idx);
/*  835:1233 */     this._cellXf.setApplyBorder(true);
/*  836:     */   }
/*  837:     */   
/*  838:     */   public void setVerticalAlignment(VerticalAlignment align)
/*  839:     */   {
/*  840:1242 */     getCellAlignment().setVertical(align);
/*  841:     */   }
/*  842:     */   
/*  843:     */   public void setWrapText(boolean wrapped)
/*  844:     */   {
/*  845:1256 */     getCellAlignment().setWrapText(wrapped);
/*  846:     */   }
/*  847:     */   
/*  848:     */   public XSSFColor getBorderColor(BorderSide side)
/*  849:     */   {
/*  850:1266 */     switch (1.$SwitchMap$org$apache$poi$xssf$usermodel$extensions$XSSFCellBorder$BorderSide[side.ordinal()])
/*  851:     */     {
/*  852:     */     case 1: 
/*  853:1268 */       return getBottomBorderXSSFColor();
/*  854:     */     case 2: 
/*  855:1270 */       return getRightBorderXSSFColor();
/*  856:     */     case 3: 
/*  857:1272 */       return getTopBorderXSSFColor();
/*  858:     */     case 4: 
/*  859:1274 */       return getLeftBorderXSSFColor();
/*  860:     */     }
/*  861:1276 */     throw new IllegalArgumentException("Unknown border: " + side);
/*  862:     */   }
/*  863:     */   
/*  864:     */   public void setBorderColor(BorderSide side, XSSFColor color)
/*  865:     */   {
/*  866:1287 */     switch (1.$SwitchMap$org$apache$poi$xssf$usermodel$extensions$XSSFCellBorder$BorderSide[side.ordinal()])
/*  867:     */     {
/*  868:     */     case 1: 
/*  869:1289 */       setBottomBorderColor(color);
/*  870:1290 */       break;
/*  871:     */     case 2: 
/*  872:1292 */       setRightBorderColor(color);
/*  873:1293 */       break;
/*  874:     */     case 3: 
/*  875:1295 */       setTopBorderColor(color);
/*  876:1296 */       break;
/*  877:     */     case 4: 
/*  878:1298 */       setLeftBorderColor(color);
/*  879:     */     }
/*  880:     */   }
/*  881:     */   
/*  882:     */   public void setShrinkToFit(boolean shrinkToFit)
/*  883:     */   {
/*  884:1305 */     getCellAlignment().setShrinkToFit(shrinkToFit);
/*  885:     */   }
/*  886:     */   
/*  887:     */   private int getFontId()
/*  888:     */   {
/*  889:1309 */     if (this._cellXf.isSetFontId()) {
/*  890:1310 */       return (int)this._cellXf.getFontId();
/*  891:     */     }
/*  892:1312 */     return (int)this._cellStyleXf.getFontId();
/*  893:     */   }
/*  894:     */   
/*  895:     */   protected XSSFCellAlignment getCellAlignment()
/*  896:     */   {
/*  897:1320 */     if (this._cellAlignment == null) {
/*  898:1321 */       this._cellAlignment = new XSSFCellAlignment(getCTCellAlignment());
/*  899:     */     }
/*  900:1323 */     return this._cellAlignment;
/*  901:     */   }
/*  902:     */   
/*  903:     */   private CTCellAlignment getCTCellAlignment()
/*  904:     */   {
/*  905:1332 */     if (this._cellXf.getAlignment() == null) {
/*  906:1333 */       this._cellXf.setAlignment(CTCellAlignment.Factory.newInstance());
/*  907:     */     }
/*  908:1335 */     return this._cellXf.getAlignment();
/*  909:     */   }
/*  910:     */   
/*  911:     */   public int hashCode()
/*  912:     */   {
/*  913:1345 */     return this._cellXf.toString().hashCode();
/*  914:     */   }
/*  915:     */   
/*  916:     */   public boolean equals(Object o)
/*  917:     */   {
/*  918:1356 */     if ((o == null) || (!(o instanceof XSSFCellStyle))) {
/*  919:1356 */       return false;
/*  920:     */     }
/*  921:1358 */     XSSFCellStyle cf = (XSSFCellStyle)o;
/*  922:1359 */     return this._cellXf.toString().equals(cf.getCoreXf().toString());
/*  923:     */   }
/*  924:     */   
/*  925:     */   public Object clone()
/*  926:     */   {
/*  927:1370 */     CTXf xf = (CTXf)this._cellXf.copy();
/*  928:     */     
/*  929:1372 */     int xfSize = this._stylesSource._getStyleXfsSize();
/*  930:1373 */     int indexXf = this._stylesSource.putCellXf(xf);
/*  931:1374 */     return new XSSFCellStyle(indexXf - 1, xfSize - 1, this._stylesSource, this._theme);
/*  932:     */   }
/*  933:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFCellStyle

 * JD-Core Version:    0.7.0.1

 */