/*    1:     */ package org.apache.poi.ss.formula;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.List;
/*    5:     */ import java.util.Locale;
/*    6:     */ import java.util.regex.Matcher;
/*    7:     */ import java.util.regex.Pattern;
/*    8:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*    9:     */ import org.apache.poi.ss.formula.constant.ErrorConstant;
/*   10:     */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*   11:     */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*   12:     */ import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
/*   13:     */ import org.apache.poi.ss.formula.ptg.AddPtg;
/*   14:     */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*   15:     */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*   16:     */ import org.apache.poi.ss.formula.ptg.ArrayPtg;
/*   17:     */ import org.apache.poi.ss.formula.ptg.AttrPtg;
/*   18:     */ import org.apache.poi.ss.formula.ptg.BoolPtg;
/*   19:     */ import org.apache.poi.ss.formula.ptg.ConcatPtg;
/*   20:     */ import org.apache.poi.ss.formula.ptg.DividePtg;
/*   21:     */ import org.apache.poi.ss.formula.ptg.EqualPtg;
/*   22:     */ import org.apache.poi.ss.formula.ptg.ErrPtg;
/*   23:     */ import org.apache.poi.ss.formula.ptg.FuncPtg;
/*   24:     */ import org.apache.poi.ss.formula.ptg.FuncVarPtg;
/*   25:     */ import org.apache.poi.ss.formula.ptg.GreaterEqualPtg;
/*   26:     */ import org.apache.poi.ss.formula.ptg.GreaterThanPtg;
/*   27:     */ import org.apache.poi.ss.formula.ptg.IntPtg;
/*   28:     */ import org.apache.poi.ss.formula.ptg.IntersectionPtg;
/*   29:     */ import org.apache.poi.ss.formula.ptg.LessEqualPtg;
/*   30:     */ import org.apache.poi.ss.formula.ptg.LessThanPtg;
/*   31:     */ import org.apache.poi.ss.formula.ptg.MemAreaPtg;
/*   32:     */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*   33:     */ import org.apache.poi.ss.formula.ptg.MissingArgPtg;
/*   34:     */ import org.apache.poi.ss.formula.ptg.MultiplyPtg;
/*   35:     */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*   36:     */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*   37:     */ import org.apache.poi.ss.formula.ptg.NameXPxg;
/*   38:     */ import org.apache.poi.ss.formula.ptg.NotEqualPtg;
/*   39:     */ import org.apache.poi.ss.formula.ptg.NumberPtg;
/*   40:     */ import org.apache.poi.ss.formula.ptg.OperandPtg;
/*   41:     */ import org.apache.poi.ss.formula.ptg.OperationPtg;
/*   42:     */ import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
/*   43:     */ import org.apache.poi.ss.formula.ptg.PercentPtg;
/*   44:     */ import org.apache.poi.ss.formula.ptg.PowerPtg;
/*   45:     */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   46:     */ import org.apache.poi.ss.formula.ptg.RangePtg;
/*   47:     */ import org.apache.poi.ss.formula.ptg.RefPtg;
/*   48:     */ import org.apache.poi.ss.formula.ptg.StringPtg;
/*   49:     */ import org.apache.poi.ss.formula.ptg.SubtractPtg;
/*   50:     */ import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
/*   51:     */ import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
/*   52:     */ import org.apache.poi.ss.formula.ptg.UnionPtg;
/*   53:     */ import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
/*   54:     */ import org.apache.poi.ss.usermodel.FormulaError;
/*   55:     */ import org.apache.poi.ss.usermodel.Name;
/*   56:     */ import org.apache.poi.ss.usermodel.Table;
/*   57:     */ import org.apache.poi.ss.util.AreaReference;
/*   58:     */ import org.apache.poi.ss.util.CellReference;
/*   59:     */ import org.apache.poi.ss.util.CellReference.NameType;
/*   60:     */ import org.apache.poi.util.Internal;
/*   61:     */ import org.apache.poi.util.POILogFactory;
/*   62:     */ import org.apache.poi.util.POILogger;
/*   63:     */ 
/*   64:     */ @Internal
/*   65:     */ public final class FormulaParser
/*   66:     */ {
/*   67:  96 */   private static final POILogger log = POILogFactory.getLogger(FormulaParser.class);
/*   68:     */   private final String _formulaString;
/*   69:     */   private final int _formulaLength;
/*   70:     */   private int _pointer;
/*   71:     */   private ParseNode _rootNode;
/*   72:     */   private static final char TAB = '\t';
/*   73:     */   private static final char CR = '\r';
/*   74:     */   private static final char LF = '\n';
/*   75:     */   private int look;
/*   76:     */   private boolean _inIntersection;
/*   77:     */   private final FormulaParsingWorkbook _book;
/*   78:     */   private final SpreadsheetVersion _ssVersion;
/*   79:     */   private final int _sheetIndex;
/*   80:     */   private final int _rowIndex;
/*   81:     */   private static final String specHeaders = "Headers";
/*   82:     */   private static final String specAll = "All";
/*   83:     */   private static final String specData = "Data";
/*   84:     */   private static final String specTotals = "Totals";
/*   85:     */   private static final String specThisRow = "This Row";
/*   86:     */   
/*   87:     */   private FormulaParser(String formula, FormulaParsingWorkbook book, int sheetIndex, int rowIndex)
/*   88:     */   {
/*   89: 140 */     this._formulaString = formula;
/*   90: 141 */     this._pointer = 0;
/*   91: 142 */     this._book = book;
/*   92: 143 */     this._ssVersion = (book == null ? SpreadsheetVersion.EXCEL97 : book.getSpreadsheetVersion());
/*   93: 144 */     this._formulaLength = this._formulaString.length();
/*   94: 145 */     this._sheetIndex = sheetIndex;
/*   95: 146 */     this._rowIndex = rowIndex;
/*   96:     */   }
/*   97:     */   
/*   98:     */   public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex, int rowIndex)
/*   99:     */   {
/*  100: 169 */     FormulaParser fp = new FormulaParser(formula, workbook, sheetIndex, rowIndex);
/*  101: 170 */     fp.parse();
/*  102: 171 */     return fp.getRPNPtg(formulaType);
/*  103:     */   }
/*  104:     */   
/*  105:     */   public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex)
/*  106:     */   {
/*  107: 190 */     return parse(formula, workbook, formulaType, sheetIndex, -1);
/*  108:     */   }
/*  109:     */   
/*  110:     */   public static Area3DPxg parseStructuredReference(String tableText, FormulaParsingWorkbook workbook, int rowIndex)
/*  111:     */   {
/*  112: 203 */     int sheetIndex = -1;
/*  113: 204 */     Ptg[] arr = parse(tableText, workbook, FormulaType.CELL, -1, rowIndex);
/*  114: 205 */     if ((arr.length != 1) || (!(arr[0] instanceof Area3DPxg))) {
/*  115: 206 */       throw new IllegalStateException("Illegal structured reference");
/*  116:     */     }
/*  117: 208 */     return (Area3DPxg)arr[0];
/*  118:     */   }
/*  119:     */   
/*  120:     */   private void GetChar()
/*  121:     */   {
/*  122: 215 */     if (IsWhite(this.look))
/*  123:     */     {
/*  124: 216 */       if (this.look == 32) {
/*  125: 217 */         this._inIntersection = true;
/*  126:     */       }
/*  127:     */     }
/*  128:     */     else {
/*  129: 221 */       this._inIntersection = false;
/*  130:     */     }
/*  131: 225 */     if (this._pointer > this._formulaLength) {
/*  132: 226 */       throw new RuntimeException("too far");
/*  133:     */     }
/*  134: 228 */     if (this._pointer < this._formulaLength)
/*  135:     */     {
/*  136: 229 */       this.look = this._formulaString.codePointAt(this._pointer);
/*  137:     */     }
/*  138:     */     else
/*  139:     */     {
/*  140: 233 */       this.look = 0;
/*  141: 234 */       this._inIntersection = false;
/*  142:     */     }
/*  143: 236 */     this._pointer += Character.charCount(this.look);
/*  144:     */   }
/*  145:     */   
/*  146:     */   private void resetPointer(int ptr)
/*  147:     */   {
/*  148: 240 */     this._pointer = ptr;
/*  149: 241 */     if (this._pointer <= this._formulaLength) {
/*  150: 242 */       this.look = this._formulaString.codePointAt(this._pointer - Character.charCount(this.look));
/*  151:     */     } else {
/*  152: 246 */       this.look = 0;
/*  153:     */     }
/*  154:     */   }
/*  155:     */   
/*  156:     */   private RuntimeException expected(String s)
/*  157:     */   {
/*  158:     */     String msg;
/*  159:     */     String msg;
/*  160: 254 */     if ((this.look == 61) && (this._formulaString.substring(0, this._pointer - 1).trim().length() < 1)) {
/*  161: 255 */       msg = "The specified formula '" + this._formulaString + "' starts with an equals sign which is not allowed.";
/*  162:     */     } else {
/*  163: 258 */       msg = "'" + " in specified formula '" + this._formulaString + "'. Expected " + s;
/*  164:     */     }
/*  165: 269 */     return new FormulaParseException(msg);
/*  166:     */   }
/*  167:     */   
/*  168:     */   private static boolean IsAlpha(int c)
/*  169:     */   {
/*  170: 274 */     return (Character.isLetter(c)) || (c == 36) || (c == 95);
/*  171:     */   }
/*  172:     */   
/*  173:     */   private static boolean IsDigit(int c)
/*  174:     */   {
/*  175: 279 */     return Character.isDigit(c);
/*  176:     */   }
/*  177:     */   
/*  178:     */   private static boolean IsWhite(int c)
/*  179:     */   {
/*  180: 284 */     return (c == 32) || (c == 9) || (c == 13) || (c == 10);
/*  181:     */   }
/*  182:     */   
/*  183:     */   private void SkipWhite()
/*  184:     */   {
/*  185: 289 */     while (IsWhite(this.look)) {
/*  186: 290 */       GetChar();
/*  187:     */     }
/*  188:     */   }
/*  189:     */   
/*  190:     */   private void Match(int x)
/*  191:     */   {
/*  192: 300 */     if (this.look != x) {
/*  193: 301 */       throw expected("'");
/*  194:     */     }
/*  195: 307 */     GetChar();
/*  196:     */   }
/*  197:     */   
/*  198:     */   private String GetNum()
/*  199:     */   {
/*  200: 312 */     StringBuilder value = new StringBuilder();
/*  201: 314 */     while (IsDigit(this.look))
/*  202:     */     {
/*  203: 315 */       value.appendCodePoint(this.look);
/*  204: 316 */       GetChar();
/*  205:     */     }
/*  206: 318 */     return value.length() == 0 ? null : value.toString();
/*  207:     */   }
/*  208:     */   
/*  209:     */   private ParseNode parseRangeExpression()
/*  210:     */   {
/*  211: 322 */     ParseNode result = parseRangeable();
/*  212: 323 */     boolean hasRange = false;
/*  213: 324 */     while (this.look == 58)
/*  214:     */     {
/*  215: 325 */       int pos = this._pointer;
/*  216: 326 */       GetChar();
/*  217: 327 */       ParseNode nextPart = parseRangeable();
/*  218:     */       
/*  219:     */ 
/*  220:     */ 
/*  221:     */ 
/*  222:     */ 
/*  223: 333 */       checkValidRangeOperand("LHS", pos, result);
/*  224: 334 */       checkValidRangeOperand("RHS", pos, nextPart);
/*  225:     */       
/*  226: 336 */       ParseNode[] children = { result, nextPart };
/*  227: 337 */       result = new ParseNode(RangePtg.instance, children);
/*  228: 338 */       hasRange = true;
/*  229:     */     }
/*  230: 340 */     if (hasRange) {
/*  231: 341 */       return augmentWithMemPtg(result);
/*  232:     */     }
/*  233: 343 */     return result;
/*  234:     */   }
/*  235:     */   
/*  236:     */   private static ParseNode augmentWithMemPtg(ParseNode root)
/*  237:     */   {
/*  238:     */     Ptg memPtg;
/*  239:     */     Ptg memPtg;
/*  240: 348 */     if (needsMemFunc(root)) {
/*  241: 349 */       memPtg = new MemFuncPtg(root.getEncodedSize());
/*  242:     */     } else {
/*  243: 351 */       memPtg = new MemAreaPtg(root.getEncodedSize());
/*  244:     */     }
/*  245: 353 */     return new ParseNode(memPtg, root);
/*  246:     */   }
/*  247:     */   
/*  248:     */   private static boolean needsMemFunc(ParseNode root)
/*  249:     */   {
/*  250: 362 */     Ptg token = root.getToken();
/*  251: 363 */     if ((token instanceof AbstractFunctionPtg)) {
/*  252: 364 */       return true;
/*  253:     */     }
/*  254: 366 */     if ((token instanceof ExternSheetReferenceToken)) {
/*  255: 367 */       return true;
/*  256:     */     }
/*  257: 369 */     if (((token instanceof NamePtg)) || ((token instanceof NameXPtg))) {
/*  258: 370 */       return true;
/*  259:     */     }
/*  260: 373 */     if (((token instanceof OperationPtg)) || ((token instanceof ParenthesisPtg)))
/*  261:     */     {
/*  262: 375 */       for (ParseNode child : root.getChildren()) {
/*  263: 376 */         if (needsMemFunc(child)) {
/*  264: 377 */           return true;
/*  265:     */         }
/*  266:     */       }
/*  267: 380 */       return false;
/*  268:     */     }
/*  269: 382 */     if ((token instanceof OperandPtg)) {
/*  270: 383 */       return false;
/*  271:     */     }
/*  272: 385 */     if ((token instanceof OperationPtg)) {
/*  273: 386 */       return true;
/*  274:     */     }
/*  275: 389 */     return false;
/*  276:     */   }
/*  277:     */   
/*  278:     */   private static void checkValidRangeOperand(String sideName, int currentParsePosition, ParseNode pn)
/*  279:     */   {
/*  280: 396 */     if (!isValidRangeOperand(pn)) {
/*  281: 397 */       throw new FormulaParseException("The " + sideName + " of the range operator ':' at position " + currentParsePosition + " is not a proper reference.");
/*  282:     */     }
/*  283:     */   }
/*  284:     */   
/*  285:     */   private static boolean isValidRangeOperand(ParseNode a)
/*  286:     */   {
/*  287: 408 */     Ptg tkn = a.getToken();
/*  288: 410 */     if ((tkn instanceof OperandPtg)) {
/*  289: 412 */       return true;
/*  290:     */     }
/*  291: 416 */     if ((tkn instanceof AbstractFunctionPtg))
/*  292:     */     {
/*  293: 417 */       AbstractFunctionPtg afp = (AbstractFunctionPtg)tkn;
/*  294: 418 */       byte returnClass = afp.getDefaultOperandClass();
/*  295: 419 */       return 0 == returnClass;
/*  296:     */     }
/*  297: 421 */     if ((tkn instanceof ValueOperatorPtg)) {
/*  298: 422 */       return false;
/*  299:     */     }
/*  300: 424 */     if ((tkn instanceof OperationPtg)) {
/*  301: 425 */       return true;
/*  302:     */     }
/*  303: 429 */     if ((tkn instanceof ParenthesisPtg)) {
/*  304: 431 */       return isValidRangeOperand(a.getChildren()[0]);
/*  305:     */     }
/*  306: 435 */     if (tkn == ErrPtg.REF_INVALID) {
/*  307: 436 */       return true;
/*  308:     */     }
/*  309: 440 */     return false;
/*  310:     */   }
/*  311:     */   
/*  312:     */   private ParseNode parseRangeable()
/*  313:     */   {
/*  314: 470 */     SkipWhite();
/*  315: 471 */     int savePointer = this._pointer;
/*  316: 472 */     SheetIdentifier sheetIden = parseSheetName();
/*  317: 474 */     if (sheetIden == null)
/*  318:     */     {
/*  319: 475 */       resetPointer(savePointer);
/*  320:     */     }
/*  321:     */     else
/*  322:     */     {
/*  323: 477 */       SkipWhite();
/*  324: 478 */       savePointer = this._pointer;
/*  325:     */     }
/*  326: 481 */     SimpleRangePart part1 = parseSimpleRangePart();
/*  327: 482 */     if (part1 == null)
/*  328:     */     {
/*  329: 483 */       if (sheetIden != null)
/*  330:     */       {
/*  331: 484 */         if (this.look == 35) {
/*  332: 485 */           return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
/*  333:     */         }
/*  334: 488 */         String name = parseAsName();
/*  335: 489 */         if (name.length() == 0) {
/*  336: 490 */           throw new FormulaParseException("Cell reference or Named Range expected after sheet name at index " + this._pointer + ".");
/*  337:     */         }
/*  338: 493 */         Ptg nameXPtg = this._book.getNameXPtg(name, sheetIden);
/*  339: 494 */         if (nameXPtg == null) {
/*  340: 495 */           throw new FormulaParseException("Specified name '" + name + "' for sheet " + sheetIden.asFormulaString() + " not found");
/*  341:     */         }
/*  342: 498 */         return new ParseNode(nameXPtg);
/*  343:     */       }
/*  344: 501 */       return parseNonRange(savePointer);
/*  345:     */     }
/*  346: 503 */     boolean whiteAfterPart1 = IsWhite(this.look);
/*  347: 504 */     if (whiteAfterPart1) {
/*  348: 505 */       SkipWhite();
/*  349:     */     }
/*  350: 508 */     if (this.look == 58)
/*  351:     */     {
/*  352: 509 */       int colonPos = this._pointer;
/*  353: 510 */       GetChar();
/*  354: 511 */       SkipWhite();
/*  355: 512 */       SimpleRangePart part2 = parseSimpleRangePart();
/*  356: 513 */       if ((part2 != null) && (!part1.isCompatibleForArea(part2))) {
/*  357: 517 */         part2 = null;
/*  358:     */       }
/*  359: 519 */       if (part2 == null)
/*  360:     */       {
/*  361: 522 */         resetPointer(colonPos);
/*  362: 523 */         if (!part1.isCell())
/*  363:     */         {
/*  364: 524 */           String prefix = "";
/*  365: 525 */           if (sheetIden != null) {
/*  366: 526 */             prefix = "'" + sheetIden.getSheetIdentifier().getName() + '!';
/*  367:     */           }
/*  368: 528 */           throw new FormulaParseException(prefix + part1.getRep() + "' is not a proper reference.");
/*  369:     */         }
/*  370:     */       }
/*  371: 531 */       return createAreaRefParseNode(sheetIden, part1, part2);
/*  372:     */     }
/*  373: 534 */     if (this.look == 46)
/*  374:     */     {
/*  375: 535 */       GetChar();
/*  376: 536 */       int dotCount = 1;
/*  377: 537 */       while (this.look == 46)
/*  378:     */       {
/*  379: 538 */         dotCount++;
/*  380: 539 */         GetChar();
/*  381:     */       }
/*  382: 541 */       boolean whiteBeforePart2 = IsWhite(this.look);
/*  383:     */       
/*  384: 543 */       SkipWhite();
/*  385: 544 */       SimpleRangePart part2 = parseSimpleRangePart();
/*  386: 545 */       String part1And2 = this._formulaString.substring(savePointer - 1, this._pointer - 1);
/*  387: 546 */       if (part2 == null)
/*  388:     */       {
/*  389: 547 */         if (sheetIden != null) {
/*  390: 548 */           throw new FormulaParseException("Complete area reference expected after sheet name at index " + this._pointer + ".");
/*  391:     */         }
/*  392: 551 */         return parseNonRange(savePointer);
/*  393:     */       }
/*  394: 555 */       if ((whiteAfterPart1) || (whiteBeforePart2))
/*  395:     */       {
/*  396: 556 */         if ((part1.isRowOrColumn()) || (part2.isRowOrColumn())) {
/*  397: 559 */           throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must not contain whitespace.");
/*  398:     */         }
/*  399: 562 */         return createAreaRefParseNode(sheetIden, part1, part2);
/*  400:     */       }
/*  401: 565 */       if ((dotCount == 1) && (part1.isRow()) && (part2.isRow())) {
/*  402: 567 */         return parseNonRange(savePointer);
/*  403:     */       }
/*  404: 570 */       if (((part1.isRowOrColumn()) || (part2.isRowOrColumn())) && 
/*  405: 571 */         (dotCount != 2)) {
/*  406: 572 */         throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must have exactly 2 dots.");
/*  407:     */       }
/*  408: 576 */       return createAreaRefParseNode(sheetIden, part1, part2);
/*  409:     */     }
/*  410: 578 */     if ((part1.isCell()) && (isValidCellReference(part1.getRep()))) {
/*  411: 579 */       return createAreaRefParseNode(sheetIden, part1, null);
/*  412:     */     }
/*  413: 581 */     if (sheetIden != null) {
/*  414: 582 */       throw new FormulaParseException("Second part of cell reference expected after sheet name at index " + this._pointer + ".");
/*  415:     */     }
/*  416: 586 */     return parseNonRange(savePointer);
/*  417:     */   }
/*  418:     */   
/*  419:     */   private ParseNode parseStructuredReference(String tableName)
/*  420:     */   {
/*  421: 625 */     if (!this._ssVersion.equals(SpreadsheetVersion.EXCEL2007)) {
/*  422: 626 */       throw new FormulaParseException("Structured references work only on XSSF (Excel 2007+)!");
/*  423:     */     }
/*  424: 628 */     Table tbl = this._book.getTable(tableName);
/*  425: 629 */     if (tbl == null) {
/*  426: 630 */       throw new FormulaParseException("Illegal table name: '" + tableName + "'");
/*  427:     */     }
/*  428: 632 */     String sheetName = tbl.getSheetName();
/*  429:     */     
/*  430: 634 */     int startCol = tbl.getStartColIndex();
/*  431: 635 */     int endCol = tbl.getEndColIndex();
/*  432: 636 */     int startRow = tbl.getStartRowIndex();
/*  433: 637 */     int endRow = tbl.getEndRowIndex();
/*  434:     */     
/*  435:     */ 
/*  436:     */ 
/*  437: 641 */     int savePtr0 = this._pointer;
/*  438: 642 */     GetChar();
/*  439:     */     
/*  440: 644 */     boolean isTotalsSpec = false;
/*  441: 645 */     boolean isThisRowSpec = false;
/*  442: 646 */     boolean isDataSpec = false;
/*  443: 647 */     boolean isHeadersSpec = false;
/*  444: 648 */     boolean isAllSpec = false;
/*  445: 649 */     int nSpecQuantifiers = 0;
/*  446:     */     for (;;)
/*  447:     */     {
/*  448: 651 */       int savePtr1 = this._pointer;
/*  449: 652 */       String specName = parseAsSpecialQuantifier();
/*  450: 653 */       if (specName == null)
/*  451:     */       {
/*  452: 654 */         resetPointer(savePtr1);
/*  453: 655 */         break;
/*  454:     */       }
/*  455: 657 */       if (specName.equals("All")) {
/*  456: 658 */         isAllSpec = true;
/*  457: 659 */       } else if (specName.equals("Data")) {
/*  458: 660 */         isDataSpec = true;
/*  459: 661 */       } else if (specName.equals("Headers")) {
/*  460: 662 */         isHeadersSpec = true;
/*  461: 663 */       } else if (specName.equals("This Row")) {
/*  462: 664 */         isThisRowSpec = true;
/*  463: 665 */       } else if (specName.equals("Totals")) {
/*  464: 666 */         isTotalsSpec = true;
/*  465:     */       } else {
/*  466: 668 */         throw new FormulaParseException("Unknown special quantifier " + specName);
/*  467:     */       }
/*  468: 670 */       nSpecQuantifiers++;
/*  469: 671 */       if (this.look != 44) {
/*  470:     */         break;
/*  471:     */       }
/*  472: 672 */       GetChar();
/*  473:     */     }
/*  474: 677 */     boolean isThisRow = false;
/*  475: 678 */     SkipWhite();
/*  476: 679 */     if (this.look == 64)
/*  477:     */     {
/*  478: 680 */       isThisRow = true;
/*  479: 681 */       GetChar();
/*  480:     */     }
/*  481: 685 */     String endColumnName = null;
/*  482: 686 */     int nColQuantifiers = 0;
/*  483: 687 */     int savePtr1 = this._pointer;
/*  484: 688 */     String startColumnName = parseAsColumnQuantifier();
/*  485: 689 */     if (startColumnName == null)
/*  486:     */     {
/*  487: 690 */       resetPointer(savePtr1);
/*  488:     */     }
/*  489:     */     else
/*  490:     */     {
/*  491: 692 */       nColQuantifiers++;
/*  492: 693 */       if (this.look == 44) {
/*  493: 694 */         throw new FormulaParseException("The formula " + this._formulaString + "is illegal: you should not use ',' with column quantifiers");
/*  494:     */       }
/*  495: 695 */       if (this.look == 58)
/*  496:     */       {
/*  497: 696 */         GetChar();
/*  498: 697 */         endColumnName = parseAsColumnQuantifier();
/*  499: 698 */         nColQuantifiers++;
/*  500: 699 */         if (endColumnName == null) {
/*  501: 700 */           throw new FormulaParseException("The formula " + this._formulaString + "is illegal: the string after ':' must be column quantifier");
/*  502:     */         }
/*  503:     */       }
/*  504:     */     }
/*  505: 705 */     if ((nColQuantifiers == 0) && (nSpecQuantifiers == 0))
/*  506:     */     {
/*  507: 706 */       resetPointer(savePtr0);
/*  508: 707 */       savePtr0 = this._pointer;
/*  509: 708 */       startColumnName = parseAsColumnQuantifier();
/*  510: 709 */       if (startColumnName != null)
/*  511:     */       {
/*  512: 710 */         nColQuantifiers++;
/*  513:     */       }
/*  514:     */       else
/*  515:     */       {
/*  516: 712 */         resetPointer(savePtr0);
/*  517: 713 */         String name = parseAsSpecialQuantifier();
/*  518: 714 */         if (name != null)
/*  519:     */         {
/*  520: 715 */           if (name.equals("All")) {
/*  521: 716 */             isAllSpec = true;
/*  522: 717 */           } else if (name.equals("Data")) {
/*  523: 718 */             isDataSpec = true;
/*  524: 719 */           } else if (name.equals("Headers")) {
/*  525: 720 */             isHeadersSpec = true;
/*  526: 721 */           } else if (name.equals("This Row")) {
/*  527: 722 */             isThisRowSpec = true;
/*  528: 723 */           } else if (name.equals("Totals")) {
/*  529: 724 */             isTotalsSpec = true;
/*  530:     */           } else {
/*  531: 726 */             throw new FormulaParseException("Unknown special quantifier " + name);
/*  532:     */           }
/*  533: 728 */           nSpecQuantifiers++;
/*  534:     */         }
/*  535:     */         else
/*  536:     */         {
/*  537: 730 */           throw new FormulaParseException("The formula " + this._formulaString + " is illegal");
/*  538:     */         }
/*  539:     */       }
/*  540:     */     }
/*  541:     */     else
/*  542:     */     {
/*  543: 734 */       Match(93);
/*  544:     */     }
/*  545: 739 */     if ((isTotalsSpec) && (tbl.getTotalsRowCount() == 0)) {
/*  546: 740 */       return new ParseNode(ErrPtg.REF_INVALID);
/*  547:     */     }
/*  548: 742 */     if (((isThisRow) || (isThisRowSpec)) && ((this._rowIndex < startRow) || (endRow < this._rowIndex)))
/*  549:     */     {
/*  550: 744 */       if (this._rowIndex >= 0) {
/*  551: 745 */         return new ParseNode(ErrPtg.VALUE_INVALID);
/*  552:     */       }
/*  553: 747 */       throw new FormulaParseException("Formula contained [#This Row] or [@] structured reference but this row < 0. Row index must be specified for row-referencing structured references.");
/*  554:     */     }
/*  555: 753 */     int actualStartRow = startRow;
/*  556: 754 */     int actualEndRow = endRow;
/*  557: 755 */     int actualStartCol = startCol;
/*  558: 756 */     int actualEndCol = endCol;
/*  559: 757 */     if (nSpecQuantifiers > 0)
/*  560:     */     {
/*  561: 759 */       if ((nSpecQuantifiers != 1) || (!isAllSpec)) {
/*  562: 761 */         if ((isDataSpec) && (isHeadersSpec))
/*  563:     */         {
/*  564: 762 */           if (tbl.getTotalsRowCount() > 0) {
/*  565: 763 */             actualEndRow = endRow - 1;
/*  566:     */           }
/*  567:     */         }
/*  568: 765 */         else if ((isDataSpec) && (isTotalsSpec))
/*  569:     */         {
/*  570: 766 */           actualStartRow = startRow + 1;
/*  571:     */         }
/*  572: 767 */         else if ((nSpecQuantifiers == 1) && (isDataSpec))
/*  573:     */         {
/*  574: 768 */           actualStartRow = startRow + 1;
/*  575: 769 */           if (tbl.getTotalsRowCount() > 0) {
/*  576: 770 */             actualEndRow = endRow - 1;
/*  577:     */           }
/*  578:     */         }
/*  579: 772 */         else if ((nSpecQuantifiers == 1) && (isHeadersSpec))
/*  580:     */         {
/*  581: 773 */           actualEndRow = actualStartRow;
/*  582:     */         }
/*  583: 774 */         else if ((nSpecQuantifiers == 1) && (isTotalsSpec))
/*  584:     */         {
/*  585: 775 */           actualStartRow = actualEndRow;
/*  586:     */         }
/*  587: 776 */         else if (((nSpecQuantifiers == 1) && (isThisRowSpec)) || (isThisRow))
/*  588:     */         {
/*  589: 777 */           actualStartRow = this._rowIndex;
/*  590: 778 */           actualEndRow = this._rowIndex;
/*  591:     */         }
/*  592:     */         else
/*  593:     */         {
/*  594: 780 */           throw new FormulaParseException("The formula " + this._formulaString + " is illegal");
/*  595:     */         }
/*  596:     */       }
/*  597:     */     }
/*  598: 783 */     else if (isThisRow)
/*  599:     */     {
/*  600: 784 */       actualStartRow = this._rowIndex;
/*  601: 785 */       actualEndRow = this._rowIndex;
/*  602:     */     }
/*  603:     */     else
/*  604:     */     {
/*  605: 787 */       actualStartRow++;
/*  606: 788 */       if (tbl.getTotalsRowCount() > 0) {
/*  607: 788 */         actualEndRow--;
/*  608:     */       }
/*  609:     */     }
/*  610: 794 */     if (nColQuantifiers == 2)
/*  611:     */     {
/*  612: 795 */       if ((startColumnName == null) || (endColumnName == null)) {
/*  613: 796 */         throw new IllegalStateException("Fatal error");
/*  614:     */       }
/*  615: 798 */       int startIdx = tbl.findColumnIndex(startColumnName);
/*  616: 799 */       int endIdx = tbl.findColumnIndex(endColumnName);
/*  617: 800 */       if ((startIdx == -1) || (endIdx == -1)) {
/*  618: 801 */         throw new FormulaParseException("One of the columns " + startColumnName + ", " + endColumnName + " doesn't exist in table " + tbl.getName());
/*  619:     */       }
/*  620: 803 */       actualStartCol = startCol + startIdx;
/*  621: 804 */       actualEndCol = startCol + endIdx;
/*  622:     */     }
/*  623: 806 */     else if ((nColQuantifiers == 1) && (!isThisRow))
/*  624:     */     {
/*  625: 807 */       if (startColumnName == null) {
/*  626: 808 */         throw new IllegalStateException("Fatal error");
/*  627:     */       }
/*  628: 810 */       int idx = tbl.findColumnIndex(startColumnName);
/*  629: 811 */       if (idx == -1) {
/*  630: 812 */         throw new FormulaParseException("The column " + startColumnName + " doesn't exist in table " + tbl.getName());
/*  631:     */       }
/*  632: 814 */       actualStartCol = startCol + idx;
/*  633: 815 */       actualEndCol = actualStartCol;
/*  634:     */     }
/*  635: 817 */     CellReference topLeft = new CellReference(actualStartRow, actualStartCol);
/*  636: 818 */     CellReference bottomRight = new CellReference(actualEndRow, actualEndCol);
/*  637: 819 */     SheetIdentifier sheetIden = new SheetIdentifier(null, new NameIdentifier(sheetName, true));
/*  638: 820 */     Ptg ptg = this._book.get3DReferencePtg(new AreaReference(topLeft, bottomRight, this._ssVersion), sheetIden);
/*  639: 821 */     return new ParseNode(ptg);
/*  640:     */   }
/*  641:     */   
/*  642:     */   private String parseAsColumnQuantifier()
/*  643:     */   {
/*  644: 829 */     if (this.look != 91) {
/*  645: 830 */       return null;
/*  646:     */     }
/*  647: 832 */     GetChar();
/*  648: 833 */     if (this.look == 35) {
/*  649: 834 */       return null;
/*  650:     */     }
/*  651: 836 */     if (this.look == 64) {
/*  652: 837 */       GetChar();
/*  653:     */     }
/*  654: 839 */     StringBuilder name = new StringBuilder();
/*  655: 840 */     while (this.look != 93)
/*  656:     */     {
/*  657: 841 */       name.appendCodePoint(this.look);
/*  658: 842 */       GetChar();
/*  659:     */     }
/*  660: 844 */     Match(93);
/*  661: 845 */     return name.toString();
/*  662:     */   }
/*  663:     */   
/*  664:     */   private String parseAsSpecialQuantifier()
/*  665:     */   {
/*  666: 852 */     if (this.look != 91) {
/*  667: 853 */       return null;
/*  668:     */     }
/*  669: 855 */     GetChar();
/*  670: 856 */     if (this.look != 35) {
/*  671: 857 */       return null;
/*  672:     */     }
/*  673: 859 */     GetChar();
/*  674: 860 */     String name = parseAsName();
/*  675: 861 */     if (name.equals("This")) {
/*  676: 862 */       name = name + ' ' + parseAsName();
/*  677:     */     }
/*  678: 864 */     Match(93);
/*  679: 865 */     return name;
/*  680:     */   }
/*  681:     */   
/*  682:     */   private ParseNode parseNonRange(int savePointer)
/*  683:     */   {
/*  684: 882 */     resetPointer(savePointer);
/*  685: 884 */     if (Character.isDigit(this.look)) {
/*  686: 885 */       return new ParseNode(parseNumber());
/*  687:     */     }
/*  688: 887 */     if (this.look == 34) {
/*  689: 888 */       return new ParseNode(new StringPtg(parseStringLiteral()));
/*  690:     */     }
/*  691: 893 */     String name = parseAsName();
/*  692: 895 */     if (this.look == 40) {
/*  693: 896 */       return function(name);
/*  694:     */     }
/*  695: 898 */     if (this.look == 91) {
/*  696: 899 */       return parseStructuredReference(name);
/*  697:     */     }
/*  698: 901 */     if ((name.equalsIgnoreCase("TRUE")) || (name.equalsIgnoreCase("FALSE"))) {
/*  699: 902 */       return new ParseNode(BoolPtg.valueOf(name.equalsIgnoreCase("TRUE")));
/*  700:     */     }
/*  701: 904 */     if (this._book == null) {
/*  702: 906 */       throw new IllegalStateException("Need book to evaluate name '" + name + "'");
/*  703:     */     }
/*  704: 908 */     EvaluationName evalName = this._book.getName(name, this._sheetIndex);
/*  705: 909 */     if (evalName == null) {
/*  706: 910 */       throw new FormulaParseException("Specified named range '" + name + "' does not exist in the current workbook.");
/*  707:     */     }
/*  708: 913 */     if (evalName.isRange()) {
/*  709: 914 */       return new ParseNode(evalName.createPtg());
/*  710:     */     }
/*  711: 917 */     throw new FormulaParseException("Specified name '" + name + "' is not a range as expected.");
/*  712:     */   }
/*  713:     */   
/*  714:     */   private String parseAsName()
/*  715:     */   {
/*  716: 922 */     StringBuilder sb = new StringBuilder();
/*  717: 925 */     if ((!Character.isLetter(this.look)) && (this.look != 95) && (this.look != 92)) {
/*  718: 926 */       throw expected("number, string, defined name, or data table");
/*  719:     */     }
/*  720: 928 */     while (isValidDefinedNameChar(this.look))
/*  721:     */     {
/*  722: 929 */       sb.appendCodePoint(this.look);
/*  723: 930 */       GetChar();
/*  724:     */     }
/*  725: 932 */     SkipWhite();
/*  726:     */     
/*  727: 934 */     return sb.toString();
/*  728:     */   }
/*  729:     */   
/*  730:     */   private static boolean isValidDefinedNameChar(int ch)
/*  731:     */   {
/*  732: 942 */     if (Character.isLetterOrDigit(ch)) {
/*  733: 943 */       return true;
/*  734:     */     }
/*  735: 947 */     if (ch > 128) {
/*  736: 948 */       return true;
/*  737:     */     }
/*  738: 950 */     switch (ch)
/*  739:     */     {
/*  740:     */     case 46: 
/*  741:     */     case 63: 
/*  742:     */     case 92: 
/*  743:     */     case 95: 
/*  744: 955 */       return true;
/*  745:     */     }
/*  746: 958 */     return false;
/*  747:     */   }
/*  748:     */   
/*  749:     */   private ParseNode createAreaRefParseNode(SheetIdentifier sheetIden, SimpleRangePart part1, SimpleRangePart part2)
/*  750:     */     throws FormulaParseException
/*  751:     */   {
/*  752:     */     Ptg ptg;
/*  753:     */     Ptg ptg;
/*  754: 970 */     if (part2 == null)
/*  755:     */     {
/*  756: 971 */       CellReference cr = part1.getCellReference();
/*  757:     */       Ptg ptg;
/*  758: 972 */       if (sheetIden == null) {
/*  759: 973 */         ptg = new RefPtg(cr);
/*  760:     */       } else {
/*  761: 975 */         ptg = this._book.get3DReferencePtg(cr, sheetIden);
/*  762:     */       }
/*  763:     */     }
/*  764:     */     else
/*  765:     */     {
/*  766: 978 */       AreaReference areaRef = createAreaRef(part1, part2);
/*  767:     */       Ptg ptg;
/*  768: 980 */       if (sheetIden == null) {
/*  769: 981 */         ptg = new AreaPtg(areaRef);
/*  770:     */       } else {
/*  771: 983 */         ptg = this._book.get3DReferencePtg(areaRef, sheetIden);
/*  772:     */       }
/*  773:     */     }
/*  774: 986 */     return new ParseNode(ptg);
/*  775:     */   }
/*  776:     */   
/*  777:     */   private AreaReference createAreaRef(SimpleRangePart part1, SimpleRangePart part2)
/*  778:     */   {
/*  779: 990 */     if (!part1.isCompatibleForArea(part2)) {
/*  780: 991 */       throw new FormulaParseException("has incompatible parts: '" + part1.getRep() + "' and '" + part2.getRep() + "'.");
/*  781:     */     }
/*  782: 994 */     if (part1.isRow()) {
/*  783: 995 */       return AreaReference.getWholeRow(this._ssVersion, part1.getRep(), part2.getRep());
/*  784:     */     }
/*  785: 997 */     if (part1.isColumn()) {
/*  786: 998 */       return AreaReference.getWholeColumn(this._ssVersion, part1.getRep(), part2.getRep());
/*  787:     */     }
/*  788:1000 */     return new AreaReference(part1.getCellReference(), part2.getCellReference(), this._ssVersion);
/*  789:     */   }
/*  790:     */   
/*  791:1008 */   private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Za-z]+)?(\\$?[0-9]+)?");
/*  792:     */   
/*  793:     */   private SimpleRangePart parseSimpleRangePart()
/*  794:     */   {
/*  795:1016 */     int ptr = this._pointer - 1;
/*  796:1017 */     boolean hasDigits = false;
/*  797:1018 */     boolean hasLetters = false;
/*  798:1019 */     while (ptr < this._formulaLength)
/*  799:     */     {
/*  800:1020 */       char ch = this._formulaString.charAt(ptr);
/*  801:1021 */       if (Character.isDigit(ch)) {
/*  802:1022 */         hasDigits = true;
/*  803:1023 */       } else if (Character.isLetter(ch)) {
/*  804:1024 */         hasLetters = true;
/*  805:     */       } else {
/*  806:1025 */         if ((ch != '$') && (ch != '_')) {
/*  807:     */           break;
/*  808:     */         }
/*  809:     */       }
/*  810:1030 */       ptr++;
/*  811:     */     }
/*  812:1032 */     if (ptr <= this._pointer - 1) {
/*  813:1033 */       return null;
/*  814:     */     }
/*  815:1035 */     String rep = this._formulaString.substring(this._pointer - 1, ptr);
/*  816:1036 */     if (!CELL_REF_PATTERN.matcher(rep).matches()) {
/*  817:1037 */       return null;
/*  818:     */     }
/*  819:1040 */     if ((hasLetters) && (hasDigits))
/*  820:     */     {
/*  821:1041 */       if (!isValidCellReference(rep)) {
/*  822:1042 */         return null;
/*  823:     */       }
/*  824:     */     }
/*  825:1044 */     else if (hasLetters)
/*  826:     */     {
/*  827:1045 */       if (!CellReference.isColumnWithinRange(rep.replace("$", ""), this._ssVersion)) {
/*  828:1046 */         return null;
/*  829:     */       }
/*  830:     */     }
/*  831:1048 */     else if (hasDigits)
/*  832:     */     {
/*  833:     */       int i;
/*  834:     */       try
/*  835:     */       {
/*  836:1051 */         i = Integer.parseInt(rep.replace("$", ""));
/*  837:     */       }
/*  838:     */       catch (NumberFormatException e)
/*  839:     */       {
/*  840:1053 */         return null;
/*  841:     */       }
/*  842:1055 */       if ((i < 1) || (i > this._ssVersion.getMaxRows())) {
/*  843:1056 */         return null;
/*  844:     */       }
/*  845:     */     }
/*  846:     */     else
/*  847:     */     {
/*  848:1060 */       return null;
/*  849:     */     }
/*  850:1064 */     resetPointer(ptr + 1);
/*  851:1065 */     return new SimpleRangePart(rep, hasLetters, hasDigits);
/*  852:     */   }
/*  853:     */   
/*  854:     */   private static final class SimpleRangePart
/*  855:     */   {
/*  856:     */     private final Type _type;
/*  857:     */     private final String _rep;
/*  858:     */     
/*  859:     */     private static enum Type
/*  860:     */     {
/*  861:1074 */       CELL,  ROW,  COLUMN;
/*  862:     */       
/*  863:     */       private Type() {}
/*  864:     */       
/*  865:     */       public static Type get(boolean hasLetters, boolean hasDigits)
/*  866:     */       {
/*  867:1077 */         if (hasLetters) {
/*  868:1078 */           return hasDigits ? CELL : COLUMN;
/*  869:     */         }
/*  870:1080 */         if (!hasDigits) {
/*  871:1081 */           throw new IllegalArgumentException("must have either letters or numbers");
/*  872:     */         }
/*  873:1083 */         return ROW;
/*  874:     */       }
/*  875:     */     }
/*  876:     */     
/*  877:     */     public SimpleRangePart(String rep, boolean hasLetters, boolean hasNumbers)
/*  878:     */     {
/*  879:1091 */       this._rep = rep;
/*  880:1092 */       this._type = Type.get(hasLetters, hasNumbers);
/*  881:     */     }
/*  882:     */     
/*  883:     */     public boolean isCell()
/*  884:     */     {
/*  885:1096 */       return this._type == Type.CELL;
/*  886:     */     }
/*  887:     */     
/*  888:     */     public boolean isRowOrColumn()
/*  889:     */     {
/*  890:1100 */       return this._type != Type.CELL;
/*  891:     */     }
/*  892:     */     
/*  893:     */     public CellReference getCellReference()
/*  894:     */     {
/*  895:1104 */       if (this._type != Type.CELL) {
/*  896:1105 */         throw new IllegalStateException("Not applicable to this type");
/*  897:     */       }
/*  898:1107 */       return new CellReference(this._rep);
/*  899:     */     }
/*  900:     */     
/*  901:     */     public boolean isColumn()
/*  902:     */     {
/*  903:1111 */       return this._type == Type.COLUMN;
/*  904:     */     }
/*  905:     */     
/*  906:     */     public boolean isRow()
/*  907:     */     {
/*  908:1115 */       return this._type == Type.ROW;
/*  909:     */     }
/*  910:     */     
/*  911:     */     public String getRep()
/*  912:     */     {
/*  913:1119 */       return this._rep;
/*  914:     */     }
/*  915:     */     
/*  916:     */     public boolean isCompatibleForArea(SimpleRangePart part2)
/*  917:     */     {
/*  918:1128 */       return this._type == part2._type;
/*  919:     */     }
/*  920:     */     
/*  921:     */     public String toString()
/*  922:     */     {
/*  923:1133 */       return getClass().getName() + " [" + this._rep + "]";
/*  924:     */     }
/*  925:     */   }
/*  926:     */   
/*  927:     */   private String getBookName()
/*  928:     */   {
/*  929:1138 */     StringBuilder sb = new StringBuilder();
/*  930:1139 */     GetChar();
/*  931:1140 */     while (this.look != 93)
/*  932:     */     {
/*  933:1141 */       sb.appendCodePoint(this.look);
/*  934:1142 */       GetChar();
/*  935:     */     }
/*  936:1144 */     GetChar();
/*  937:1145 */     return sb.toString();
/*  938:     */   }
/*  939:     */   
/*  940:     */   private SheetIdentifier parseSheetName()
/*  941:     */   {
/*  942:     */     String bookName;
/*  943:     */     String bookName;
/*  944:1154 */     if (this.look == 91) {
/*  945:1155 */       bookName = getBookName();
/*  946:     */     } else {
/*  947:1157 */       bookName = null;
/*  948:     */     }
/*  949:1160 */     if (this.look == 39)
/*  950:     */     {
/*  951:1161 */       Match(39);
/*  952:1163 */       if (this.look == 91) {
/*  953:1164 */         bookName = getBookName();
/*  954:     */       }
/*  955:1166 */       StringBuilder sb = new StringBuilder();
/*  956:1167 */       boolean done = this.look == 39;
/*  957:1168 */       while (!done)
/*  958:     */       {
/*  959:1169 */         sb.appendCodePoint(this.look);
/*  960:1170 */         GetChar();
/*  961:1171 */         if (this.look == 39)
/*  962:     */         {
/*  963:1173 */           Match(39);
/*  964:1174 */           done = this.look != 39;
/*  965:     */         }
/*  966:     */       }
/*  967:1178 */       NameIdentifier iden = new NameIdentifier(sb.toString(), true);
/*  968:     */       
/*  969:1180 */       SkipWhite();
/*  970:1181 */       if (this.look == 33)
/*  971:     */       {
/*  972:1182 */         GetChar();
/*  973:1183 */         return new SheetIdentifier(bookName, iden);
/*  974:     */       }
/*  975:1186 */       if (this.look == 58) {
/*  976:1187 */         return parseSheetRange(bookName, iden);
/*  977:     */       }
/*  978:1189 */       return null;
/*  979:     */     }
/*  980:1193 */     if ((this.look == 95) || (Character.isLetter(this.look)))
/*  981:     */     {
/*  982:1194 */       StringBuilder sb = new StringBuilder();
/*  983:1196 */       while (isUnquotedSheetNameChar(this.look))
/*  984:     */       {
/*  985:1197 */         sb.appendCodePoint(this.look);
/*  986:1198 */         GetChar();
/*  987:     */       }
/*  988:1200 */       NameIdentifier iden = new NameIdentifier(sb.toString(), false);
/*  989:1201 */       SkipWhite();
/*  990:1202 */       if (this.look == 33)
/*  991:     */       {
/*  992:1203 */         GetChar();
/*  993:1204 */         return new SheetIdentifier(bookName, iden);
/*  994:     */       }
/*  995:1207 */       if (this.look == 58) {
/*  996:1208 */         return parseSheetRange(bookName, iden);
/*  997:     */       }
/*  998:1210 */       return null;
/*  999:     */     }
/* 1000:1212 */     if ((this.look == 33) && (bookName != null))
/* 1001:     */     {
/* 1002:1214 */       GetChar();
/* 1003:1215 */       return new SheetIdentifier(bookName, null);
/* 1004:     */     }
/* 1005:1217 */     return null;
/* 1006:     */   }
/* 1007:     */   
/* 1008:     */   private SheetIdentifier parseSheetRange(String bookname, NameIdentifier sheet1Name)
/* 1009:     */   {
/* 1010:1225 */     GetChar();
/* 1011:1226 */     SheetIdentifier sheet2 = parseSheetName();
/* 1012:1227 */     if (sheet2 != null) {
/* 1013:1228 */       return new SheetRangeIdentifier(bookname, sheet1Name, sheet2.getSheetIdentifier());
/* 1014:     */     }
/* 1015:1230 */     return null;
/* 1016:     */   }
/* 1017:     */   
/* 1018:     */   private static boolean isUnquotedSheetNameChar(int ch)
/* 1019:     */   {
/* 1020:1238 */     if (Character.isLetterOrDigit(ch)) {
/* 1021:1239 */       return true;
/* 1022:     */     }
/* 1023:1243 */     if (ch > 128) {
/* 1024:1244 */       return true;
/* 1025:     */     }
/* 1026:1246 */     switch (ch)
/* 1027:     */     {
/* 1028:     */     case 46: 
/* 1029:     */     case 95: 
/* 1030:1249 */       return true;
/* 1031:     */     }
/* 1032:1251 */     return false;
/* 1033:     */   }
/* 1034:     */   
/* 1035:     */   private boolean isValidCellReference(String str)
/* 1036:     */   {
/* 1037:1259 */     boolean result = CellReference.classifyCellReference(str, this._ssVersion) == NameType.CELL;
/* 1038:1261 */     if (result)
/* 1039:     */     {
/* 1040:1269 */       boolean isFunc = FunctionMetadataRegistry.getFunctionByName(str.toUpperCase(Locale.ROOT)) != null;
/* 1041:1270 */       if (isFunc)
/* 1042:     */       {
/* 1043:1271 */         int savePointer = this._pointer;
/* 1044:1272 */         resetPointer(this._pointer + str.length());
/* 1045:1273 */         SkipWhite();
/* 1046:     */         
/* 1047:     */ 
/* 1048:1276 */         result = this.look != 40;
/* 1049:1277 */         resetPointer(savePointer);
/* 1050:     */       }
/* 1051:     */     }
/* 1052:1280 */     return result;
/* 1053:     */   }
/* 1054:     */   
/* 1055:     */   private ParseNode function(String name)
/* 1056:     */   {
/* 1057:1294 */     Ptg nameToken = null;
/* 1058:1295 */     if (!AbstractFunctionPtg.isBuiltInFunctionName(name))
/* 1059:     */     {
/* 1060:1299 */       if (this._book == null) {
/* 1061:1301 */         throw new IllegalStateException("Need book to evaluate name '" + name + "'");
/* 1062:     */       }
/* 1063:1304 */       EvaluationName hName = this._book.getName(name, this._sheetIndex);
/* 1064:1305 */       if (hName != null)
/* 1065:     */       {
/* 1066:1306 */         if (!hName.isFunctionName()) {
/* 1067:1307 */           throw new FormulaParseException("Attempt to use name '" + name + "' as a function, but defined name in workbook does not refer to a function");
/* 1068:     */         }
/* 1069:1313 */         nameToken = hName.createPtg();
/* 1070:     */       }
/* 1071:     */       else
/* 1072:     */       {
/* 1073:1316 */         nameToken = this._book.getNameXPtg(name, null);
/* 1074:1317 */         if (nameToken == null)
/* 1075:     */         {
/* 1076:1319 */           if (log.check(5)) {
/* 1077:1320 */             log.log(5, new Object[] { "FormulaParser.function: Name '" + name + "' is completely unknown in the current workbook." });
/* 1078:     */           }
/* 1079:1324 */           switch (1.$SwitchMap$org$apache$poi$ss$SpreadsheetVersion[this._book.getSpreadsheetVersion().ordinal()])
/* 1080:     */           {
/* 1081:     */           case 1: 
/* 1082:1327 */             addName(name);
/* 1083:1328 */             hName = this._book.getName(name, this._sheetIndex);
/* 1084:1329 */             nameToken = hName.createPtg();
/* 1085:1330 */             break;
/* 1086:     */           case 2: 
/* 1087:1333 */             nameToken = new NameXPxg(name);
/* 1088:1334 */             break;
/* 1089:     */           default: 
/* 1090:1336 */             throw new IllegalStateException("Unexpected spreadsheet version: " + this._book.getSpreadsheetVersion().name());
/* 1091:     */           }
/* 1092:     */         }
/* 1093:     */       }
/* 1094:     */     }
/* 1095:1342 */     Match(40);
/* 1096:1343 */     ParseNode[] args = Arguments();
/* 1097:1344 */     Match(41);
/* 1098:     */     
/* 1099:1346 */     return getFunction(name, nameToken, args);
/* 1100:     */   }
/* 1101:     */   
/* 1102:     */   private void addName(String functionName)
/* 1103:     */   {
/* 1104:1354 */     Name name = this._book.createName();
/* 1105:1355 */     name.setFunction(true);
/* 1106:1356 */     name.setNameName(functionName);
/* 1107:1357 */     name.setSheetIndex(this._sheetIndex);
/* 1108:     */   }
/* 1109:     */   
/* 1110:     */   private ParseNode getFunction(String name, Ptg namePtg, ParseNode[] args)
/* 1111:     */   {
/* 1112:1369 */     FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByName(name.toUpperCase(Locale.ROOT));
/* 1113:1370 */     int numArgs = args.length;
/* 1114:1371 */     if (fm == null)
/* 1115:     */     {
/* 1116:1372 */       if (namePtg == null) {
/* 1117:1373 */         throw new IllegalStateException("NamePtg must be supplied for external functions");
/* 1118:     */       }
/* 1119:1376 */       ParseNode[] allArgs = new ParseNode[numArgs + 1];
/* 1120:1377 */       allArgs[0] = new ParseNode(namePtg);
/* 1121:1378 */       System.arraycopy(args, 0, allArgs, 1, numArgs);
/* 1122:1379 */       return new ParseNode(FuncVarPtg.create(name, numArgs + 1), allArgs);
/* 1123:     */     }
/* 1124:1382 */     if (namePtg != null) {
/* 1125:1383 */       throw new IllegalStateException("NamePtg no applicable to internal functions");
/* 1126:     */     }
/* 1127:1385 */     boolean isVarArgs = !fm.hasFixedArgsLength();
/* 1128:1386 */     int funcIx = fm.getIndex();
/* 1129:1387 */     if ((funcIx == 4) && (args.length == 1)) {
/* 1130:1390 */       return new ParseNode(AttrPtg.getSumSingle(), args);
/* 1131:     */     }
/* 1132:1393 */     validateNumArgs(args.length, fm);
/* 1133:     */     AbstractFunctionPtg retval;
/* 1134:     */     AbstractFunctionPtg retval;
/* 1135:1396 */     if (isVarArgs) {
/* 1136:1397 */       retval = FuncVarPtg.create(name, numArgs);
/* 1137:     */     } else {
/* 1138:1399 */       retval = FuncPtg.create(funcIx);
/* 1139:     */     }
/* 1140:1401 */     return new ParseNode(retval, args);
/* 1141:     */   }
/* 1142:     */   
/* 1143:     */   private void validateNumArgs(int numArgs, FunctionMetadata fm)
/* 1144:     */   {
/* 1145:1405 */     if (numArgs < fm.getMinParams())
/* 1146:     */     {
/* 1147:1406 */       String msg = "Too few arguments to function '" + fm.getName() + "'. ";
/* 1148:1407 */       if (fm.hasFixedArgsLength()) {
/* 1149:1408 */         msg = msg + "Expected " + fm.getMinParams();
/* 1150:     */       } else {
/* 1151:1410 */         msg = msg + "At least " + fm.getMinParams() + " were expected";
/* 1152:     */       }
/* 1153:1412 */       msg = msg + " but got " + numArgs + ".";
/* 1154:1413 */       throw new FormulaParseException(msg);
/* 1155:     */     }
/* 1156:     */     int maxArgs;
/* 1157:     */     int maxArgs;
/* 1158:1417 */     if (fm.hasUnlimitedVarags())
/* 1159:     */     {
/* 1160:     */       int maxArgs;
/* 1161:1418 */       if (this._book != null) {
/* 1162:1419 */         maxArgs = this._book.getSpreadsheetVersion().getMaxFunctionArgs();
/* 1163:     */       } else {
/* 1164:1422 */         maxArgs = fm.getMaxParams();
/* 1165:     */       }
/* 1166:     */     }
/* 1167:     */     else
/* 1168:     */     {
/* 1169:1425 */       maxArgs = fm.getMaxParams();
/* 1170:     */     }
/* 1171:1428 */     if (numArgs > maxArgs)
/* 1172:     */     {
/* 1173:1429 */       String msg = "Too many arguments to function '" + fm.getName() + "'. ";
/* 1174:1430 */       if (fm.hasFixedArgsLength()) {
/* 1175:1431 */         msg = msg + "Expected " + maxArgs;
/* 1176:     */       } else {
/* 1177:1433 */         msg = msg + "At most " + maxArgs + " were expected";
/* 1178:     */       }
/* 1179:1435 */       msg = msg + " but got " + numArgs + ".";
/* 1180:1436 */       throw new FormulaParseException(msg);
/* 1181:     */     }
/* 1182:     */   }
/* 1183:     */   
/* 1184:     */   private static boolean isArgumentDelimiter(int ch)
/* 1185:     */   {
/* 1186:1445 */     return (ch == 44) || (ch == 41);
/* 1187:     */   }
/* 1188:     */   
/* 1189:     */   private ParseNode[] Arguments()
/* 1190:     */   {
/* 1191:1451 */     List<ParseNode> temp = new ArrayList(2);
/* 1192:1452 */     SkipWhite();
/* 1193:1453 */     if (this.look == 41) {
/* 1194:1454 */       return ParseNode.EMPTY_ARRAY;
/* 1195:     */     }
/* 1196:1457 */     boolean missedPrevArg = true;
/* 1197:     */     do
/* 1198:     */     {
/* 1199:     */       for (;;)
/* 1200:     */       {
/* 1201:1459 */         SkipWhite();
/* 1202:1460 */         if (!isArgumentDelimiter(this.look)) {
/* 1203:     */           break;
/* 1204:     */         }
/* 1205:1461 */         if (missedPrevArg) {
/* 1206:1462 */           temp.add(new ParseNode(MissingArgPtg.instance));
/* 1207:     */         }
/* 1208:1464 */         if (this.look == 41) {
/* 1209:     */           break label121;
/* 1210:     */         }
/* 1211:1467 */         Match(44);
/* 1212:1468 */         missedPrevArg = true;
/* 1213:     */       }
/* 1214:1471 */       temp.add(comparisonExpression());
/* 1215:1472 */       missedPrevArg = false;
/* 1216:1473 */       SkipWhite();
/* 1217:1474 */     } while (isArgumentDelimiter(this.look));
/* 1218:1475 */     throw expected("',' or ')'");
/* 1219:     */     label121:
/* 1220:1478 */     ParseNode[] result = new ParseNode[temp.size()];
/* 1221:1479 */     temp.toArray(result);
/* 1222:1480 */     return result;
/* 1223:     */   }
/* 1224:     */   
/* 1225:     */   private ParseNode powerFactor()
/* 1226:     */   {
/* 1227:1485 */     ParseNode result = percentFactor();
/* 1228:     */     for (;;)
/* 1229:     */     {
/* 1230:1487 */       SkipWhite();
/* 1231:1488 */       if (this.look != 94) {
/* 1232:1489 */         return result;
/* 1233:     */       }
/* 1234:1491 */       Match(94);
/* 1235:1492 */       ParseNode other = percentFactor();
/* 1236:1493 */       result = new ParseNode(PowerPtg.instance, result, other);
/* 1237:     */     }
/* 1238:     */   }
/* 1239:     */   
/* 1240:     */   private ParseNode percentFactor()
/* 1241:     */   {
/* 1242:1498 */     ParseNode result = parseSimpleFactor();
/* 1243:     */     for (;;)
/* 1244:     */     {
/* 1245:1500 */       SkipWhite();
/* 1246:1501 */       if (this.look != 37) {
/* 1247:1502 */         return result;
/* 1248:     */       }
/* 1249:1504 */       Match(37);
/* 1250:1505 */       result = new ParseNode(PercentPtg.instance, result);
/* 1251:     */     }
/* 1252:     */   }
/* 1253:     */   
/* 1254:     */   private ParseNode parseSimpleFactor()
/* 1255:     */   {
/* 1256:1514 */     SkipWhite();
/* 1257:1515 */     switch (this.look)
/* 1258:     */     {
/* 1259:     */     case 35: 
/* 1260:1517 */       return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
/* 1261:     */     case 45: 
/* 1262:1519 */       Match(45);
/* 1263:1520 */       return parseUnary(false);
/* 1264:     */     case 43: 
/* 1265:1522 */       Match(43);
/* 1266:1523 */       return parseUnary(true);
/* 1267:     */     case 40: 
/* 1268:1525 */       Match(40);
/* 1269:1526 */       ParseNode inside = unionExpression();
/* 1270:1527 */       Match(41);
/* 1271:1528 */       return new ParseNode(ParenthesisPtg.instance, inside);
/* 1272:     */     case 34: 
/* 1273:1530 */       return new ParseNode(new StringPtg(parseStringLiteral()));
/* 1274:     */     case 123: 
/* 1275:1532 */       Match(123);
/* 1276:1533 */       ParseNode arrayNode = parseArray();
/* 1277:1534 */       Match(125);
/* 1278:1535 */       return arrayNode;
/* 1279:     */     }
/* 1280:1539 */     if ((IsAlpha(this.look)) || (Character.isDigit(this.look)) || (this.look == 39) || (this.look == 91) || (this.look == 95) || (this.look == 92)) {
/* 1281:1540 */       return parseRangeExpression();
/* 1282:     */     }
/* 1283:1542 */     if (this.look == 46) {
/* 1284:1543 */       return new ParseNode(parseNumber());
/* 1285:     */     }
/* 1286:1545 */     throw expected("cell ref or constant literal");
/* 1287:     */   }
/* 1288:     */   
/* 1289:     */   private ParseNode parseUnary(boolean isPlus)
/* 1290:     */   {
/* 1291:1551 */     boolean numberFollows = (IsDigit(this.look)) || (this.look == 46);
/* 1292:1552 */     ParseNode factor = powerFactor();
/* 1293:1554 */     if (numberFollows)
/* 1294:     */     {
/* 1295:1557 */       Ptg token = factor.getToken();
/* 1296:1558 */       if ((token instanceof NumberPtg))
/* 1297:     */       {
/* 1298:1559 */         if (isPlus) {
/* 1299:1560 */           return factor;
/* 1300:     */         }
/* 1301:1562 */         token = new NumberPtg(-((NumberPtg)token).getValue());
/* 1302:1563 */         return new ParseNode(token);
/* 1303:     */       }
/* 1304:1565 */       if ((token instanceof IntPtg))
/* 1305:     */       {
/* 1306:1566 */         if (isPlus) {
/* 1307:1567 */           return factor;
/* 1308:     */         }
/* 1309:1569 */         int intVal = ((IntPtg)token).getValue();
/* 1310:     */         
/* 1311:1571 */         token = new NumberPtg(-intVal);
/* 1312:1572 */         return new ParseNode(token);
/* 1313:     */       }
/* 1314:     */     }
/* 1315:1575 */     return new ParseNode(isPlus ? UnaryPlusPtg.instance : UnaryMinusPtg.instance, factor);
/* 1316:     */   }
/* 1317:     */   
/* 1318:     */   private ParseNode parseArray()
/* 1319:     */   {
/* 1320:1579 */     List<Object[]> rowsData = new ArrayList();
/* 1321:     */     for (;;)
/* 1322:     */     {
/* 1323:1581 */       Object[] singleRowData = parseArrayRow();
/* 1324:1582 */       rowsData.add(singleRowData);
/* 1325:1583 */       if (this.look == 125) {
/* 1326:     */         break;
/* 1327:     */       }
/* 1328:1586 */       if (this.look != 59) {
/* 1329:1587 */         throw expected("'}' or ';'");
/* 1330:     */       }
/* 1331:1589 */       Match(59);
/* 1332:     */     }
/* 1333:1591 */     int nRows = rowsData.size();
/* 1334:1592 */     Object[][] values2d = new Object[nRows][];
/* 1335:1593 */     rowsData.toArray(values2d);
/* 1336:1594 */     int nColumns = values2d[0].length;
/* 1337:1595 */     checkRowLengths(values2d, nColumns);
/* 1338:     */     
/* 1339:1597 */     return new ParseNode(new ArrayPtg(values2d));
/* 1340:     */   }
/* 1341:     */   
/* 1342:     */   private void checkRowLengths(Object[][] values2d, int nColumns)
/* 1343:     */   {
/* 1344:1600 */     for (int i = 0; i < values2d.length; i++)
/* 1345:     */     {
/* 1346:1601 */       int rowLen = values2d[i].length;
/* 1347:1602 */       if (rowLen != nColumns) {
/* 1348:1603 */         throw new FormulaParseException("Array row " + i + " has length " + rowLen + " but row 0 has length " + nColumns);
/* 1349:     */       }
/* 1350:     */     }
/* 1351:     */   }
/* 1352:     */   
/* 1353:     */   private Object[] parseArrayRow()
/* 1354:     */   {
/* 1355:1610 */     List<Object> temp = new ArrayList();
/* 1356:     */     for (;;)
/* 1357:     */     {
/* 1358:1612 */       temp.add(parseArrayItem());
/* 1359:1613 */       SkipWhite();
/* 1360:1614 */       switch (this.look)
/* 1361:     */       {
/* 1362:     */       case 59: 
/* 1363:     */       case 125: 
/* 1364:     */         break;
/* 1365:     */       case 44: 
/* 1366:1619 */         Match(44);
/* 1367:     */       }
/* 1368:     */     }
/* 1369:1622 */     throw expected("'}' or ','");
/* 1370:     */     
/* 1371:     */ 
/* 1372:     */ 
/* 1373:     */ 
/* 1374:     */ 
/* 1375:1628 */     Object[] result = new Object[temp.size()];
/* 1376:1629 */     temp.toArray(result);
/* 1377:1630 */     return result;
/* 1378:     */   }
/* 1379:     */   
/* 1380:     */   private Object parseArrayItem()
/* 1381:     */   {
/* 1382:1634 */     SkipWhite();
/* 1383:1635 */     switch (this.look)
/* 1384:     */     {
/* 1385:     */     case 34: 
/* 1386:1636 */       return parseStringLiteral();
/* 1387:     */     case 35: 
/* 1388:1637 */       return ErrorConstant.valueOf(parseErrorLiteral());
/* 1389:     */     case 70: 
/* 1390:     */     case 84: 
/* 1391:     */     case 102: 
/* 1392:     */     case 116: 
/* 1393:1640 */       return parseBooleanLiteral();
/* 1394:     */     case 45: 
/* 1395:1642 */       Match(45);
/* 1396:1643 */       SkipWhite();
/* 1397:1644 */       return convertArrayNumber(parseNumber(), false);
/* 1398:     */     }
/* 1399:1647 */     return convertArrayNumber(parseNumber(), true);
/* 1400:     */   }
/* 1401:     */   
/* 1402:     */   private Boolean parseBooleanLiteral()
/* 1403:     */   {
/* 1404:1651 */     String iden = parseUnquotedIdentifier();
/* 1405:1652 */     if ("TRUE".equalsIgnoreCase(iden)) {
/* 1406:1653 */       return Boolean.TRUE;
/* 1407:     */     }
/* 1408:1655 */     if ("FALSE".equalsIgnoreCase(iden)) {
/* 1409:1656 */       return Boolean.FALSE;
/* 1410:     */     }
/* 1411:1658 */     throw expected("'TRUE' or 'FALSE'");
/* 1412:     */   }
/* 1413:     */   
/* 1414:     */   private static Double convertArrayNumber(Ptg ptg, boolean isPositive)
/* 1415:     */   {
/* 1416:     */     double value;
/* 1417:1663 */     if ((ptg instanceof IntPtg))
/* 1418:     */     {
/* 1419:1664 */       value = ((IntPtg)ptg).getValue();
/* 1420:     */     }
/* 1421:     */     else
/* 1422:     */     {
/* 1423:     */       double value;
/* 1424:1665 */       if ((ptg instanceof NumberPtg)) {
/* 1425:1666 */         value = ((NumberPtg)ptg).getValue();
/* 1426:     */       } else {
/* 1427:1668 */         throw new RuntimeException("Unexpected ptg (" + ptg.getClass().getName() + ")");
/* 1428:     */       }
/* 1429:     */     }
/* 1430:     */     double value;
/* 1431:1670 */     if (!isPositive) {
/* 1432:1671 */       value = -value;
/* 1433:     */     }
/* 1434:1673 */     return new Double(value);
/* 1435:     */   }
/* 1436:     */   
/* 1437:     */   private Ptg parseNumber()
/* 1438:     */   {
/* 1439:1677 */     String number2 = null;
/* 1440:1678 */     String exponent = null;
/* 1441:1679 */     String number1 = GetNum();
/* 1442:1681 */     if (this.look == 46)
/* 1443:     */     {
/* 1444:1682 */       GetChar();
/* 1445:1683 */       number2 = GetNum();
/* 1446:     */     }
/* 1447:1686 */     if (this.look == 69)
/* 1448:     */     {
/* 1449:1687 */       GetChar();
/* 1450:     */       
/* 1451:1689 */       String sign = "";
/* 1452:1690 */       if (this.look == 43)
/* 1453:     */       {
/* 1454:1691 */         GetChar();
/* 1455:     */       }
/* 1456:1692 */       else if (this.look == 45)
/* 1457:     */       {
/* 1458:1693 */         GetChar();
/* 1459:1694 */         sign = "-";
/* 1460:     */       }
/* 1461:1697 */       String number = GetNum();
/* 1462:1698 */       if (number == null) {
/* 1463:1699 */         throw expected("Integer");
/* 1464:     */       }
/* 1465:1701 */       exponent = sign + number;
/* 1466:     */     }
/* 1467:1704 */     if ((number1 == null) && (number2 == null)) {
/* 1468:1705 */       throw expected("Integer");
/* 1469:     */     }
/* 1470:1708 */     return getNumberPtgFromString(number1, number2, exponent);
/* 1471:     */   }
/* 1472:     */   
/* 1473:     */   private int parseErrorLiteral()
/* 1474:     */   {
/* 1475:1713 */     Match(35);
/* 1476:1714 */     String part1 = parseUnquotedIdentifier().toUpperCase(Locale.ROOT);
/* 1477:1715 */     if (part1 == null) {
/* 1478:1716 */       throw expected("remainder of error constant literal");
/* 1479:     */     }
/* 1480:1719 */     switch (part1.charAt(0))
/* 1481:     */     {
/* 1482:     */     case 'V': 
/* 1483:1721 */       FormulaError fe = FormulaError.VALUE;
/* 1484:1722 */       if (part1.equals(fe.name()))
/* 1485:     */       {
/* 1486:1723 */         Match(33);
/* 1487:1724 */         return fe.getCode();
/* 1488:     */       }
/* 1489:1726 */       throw expected(fe.getString());
/* 1490:     */     case 'R': 
/* 1491:1729 */       FormulaError fe = FormulaError.REF;
/* 1492:1730 */       if (part1.equals(fe.name()))
/* 1493:     */       {
/* 1494:1731 */         Match(33);
/* 1495:1732 */         return fe.getCode();
/* 1496:     */       }
/* 1497:1734 */       throw expected(fe.getString());
/* 1498:     */     case 'D': 
/* 1499:1737 */       FormulaError fe = FormulaError.DIV0;
/* 1500:1738 */       if (part1.equals("DIV"))
/* 1501:     */       {
/* 1502:1739 */         Match(47);
/* 1503:1740 */         Match(48);
/* 1504:1741 */         Match(33);
/* 1505:1742 */         return fe.getCode();
/* 1506:     */       }
/* 1507:1744 */       throw expected(fe.getString());
/* 1508:     */     case 'N': 
/* 1509:1747 */       FormulaError fe = FormulaError.NAME;
/* 1510:1748 */       if (part1.equals(fe.name()))
/* 1511:     */       {
/* 1512:1750 */         Match(63);
/* 1513:1751 */         return fe.getCode();
/* 1514:     */       }
/* 1515:1753 */       fe = FormulaError.NUM;
/* 1516:1754 */       if (part1.equals(fe.name()))
/* 1517:     */       {
/* 1518:1755 */         Match(33);
/* 1519:1756 */         return fe.getCode();
/* 1520:     */       }
/* 1521:1758 */       fe = FormulaError.NULL;
/* 1522:1759 */       if (part1.equals(fe.name()))
/* 1523:     */       {
/* 1524:1760 */         Match(33);
/* 1525:1761 */         return fe.getCode();
/* 1526:     */       }
/* 1527:1763 */       fe = FormulaError.NA;
/* 1528:1764 */       if (part1.equals("N"))
/* 1529:     */       {
/* 1530:1765 */         Match(47);
/* 1531:1766 */         if ((this.look != 65) && (this.look != 97)) {
/* 1532:1767 */           throw expected(fe.getString());
/* 1533:     */         }
/* 1534:1769 */         Match(this.look);
/* 1535:     */         
/* 1536:1771 */         return fe.getCode();
/* 1537:     */       }
/* 1538:1773 */       throw expected("#NAME?, #NUM!, #NULL! or #N/A");
/* 1539:     */     }
/* 1540:1776 */     throw expected("#VALUE!, #REF!, #DIV/0!, #NAME?, #NUM!, #NULL! or #N/A");
/* 1541:     */   }
/* 1542:     */   
/* 1543:     */   private String parseUnquotedIdentifier()
/* 1544:     */   {
/* 1545:1780 */     if (this.look == 39) {
/* 1546:1781 */       throw expected("unquoted identifier");
/* 1547:     */     }
/* 1548:1783 */     StringBuilder sb = new StringBuilder();
/* 1549:1784 */     while ((Character.isLetterOrDigit(this.look)) || (this.look == 46))
/* 1550:     */     {
/* 1551:1785 */       sb.appendCodePoint(this.look);
/* 1552:1786 */       GetChar();
/* 1553:     */     }
/* 1554:1788 */     if (sb.length() < 1) {
/* 1555:1789 */       return null;
/* 1556:     */     }
/* 1557:1792 */     return sb.toString();
/* 1558:     */   }
/* 1559:     */   
/* 1560:     */   private static Ptg getNumberPtgFromString(String number1, String number2, String exponent)
/* 1561:     */   {
/* 1562:1800 */     StringBuilder number = new StringBuilder();
/* 1563:1802 */     if (number2 == null)
/* 1564:     */     {
/* 1565:1803 */       number.append(number1);
/* 1566:1805 */       if (exponent != null)
/* 1567:     */       {
/* 1568:1806 */         number.append('E');
/* 1569:1807 */         number.append(exponent);
/* 1570:     */       }
/* 1571:1810 */       String numberStr = number.toString();
/* 1572:     */       int intVal;
/* 1573:     */       try
/* 1574:     */       {
/* 1575:1813 */         intVal = Integer.parseInt(numberStr);
/* 1576:     */       }
/* 1577:     */       catch (NumberFormatException e)
/* 1578:     */       {
/* 1579:1815 */         return new NumberPtg(numberStr);
/* 1580:     */       }
/* 1581:1817 */       if (IntPtg.isInRange(intVal)) {
/* 1582:1818 */         return new IntPtg(intVal);
/* 1583:     */       }
/* 1584:1820 */       return new NumberPtg(numberStr);
/* 1585:     */     }
/* 1586:1823 */     if (number1 != null) {
/* 1587:1824 */       number.append(number1);
/* 1588:     */     }
/* 1589:1827 */     number.append('.');
/* 1590:1828 */     number.append(number2);
/* 1591:1830 */     if (exponent != null)
/* 1592:     */     {
/* 1593:1831 */       number.append('E');
/* 1594:1832 */       number.append(exponent);
/* 1595:     */     }
/* 1596:1835 */     return new NumberPtg(number.toString());
/* 1597:     */   }
/* 1598:     */   
/* 1599:     */   private String parseStringLiteral()
/* 1600:     */   {
/* 1601:1840 */     Match(34);
/* 1602:     */     
/* 1603:1842 */     StringBuilder token = new StringBuilder();
/* 1604:     */     for (;;)
/* 1605:     */     {
/* 1606:1844 */       if (this.look == 34)
/* 1607:     */       {
/* 1608:1845 */         GetChar();
/* 1609:1846 */         if (this.look != 34) {
/* 1610:     */           break;
/* 1611:     */         }
/* 1612:     */       }
/* 1613:1850 */       token.appendCodePoint(this.look);
/* 1614:1851 */       GetChar();
/* 1615:     */     }
/* 1616:1853 */     return token.toString();
/* 1617:     */   }
/* 1618:     */   
/* 1619:     */   private ParseNode Term()
/* 1620:     */   {
/* 1621:1858 */     ParseNode result = powerFactor();
/* 1622:     */     for (;;)
/* 1623:     */     {
/* 1624:1860 */       SkipWhite();
/* 1625:     */       Ptg operator;
/* 1626:1862 */       switch (this.look)
/* 1627:     */       {
/* 1628:     */       case 42: 
/* 1629:1864 */         Match(42);
/* 1630:1865 */         operator = MultiplyPtg.instance;
/* 1631:1866 */         break;
/* 1632:     */       case 47: 
/* 1633:1868 */         Match(47);
/* 1634:1869 */         operator = DividePtg.instance;
/* 1635:1870 */         break;
/* 1636:     */       default: 
/* 1637:1872 */         return result;
/* 1638:     */       }
/* 1639:1874 */       ParseNode other = powerFactor();
/* 1640:1875 */       result = new ParseNode(operator, result, other);
/* 1641:     */     }
/* 1642:     */   }
/* 1643:     */   
/* 1644:     */   private ParseNode unionExpression()
/* 1645:     */   {
/* 1646:1880 */     ParseNode result = intersectionExpression();
/* 1647:1881 */     boolean hasUnions = false;
/* 1648:     */     for (;;)
/* 1649:     */     {
/* 1650:1883 */       SkipWhite();
/* 1651:1884 */       switch (this.look)
/* 1652:     */       {
/* 1653:     */       case 44: 
/* 1654:1886 */         GetChar();
/* 1655:1887 */         hasUnions = true;
/* 1656:1888 */         ParseNode other = intersectionExpression();
/* 1657:1889 */         result = new ParseNode(UnionPtg.instance, result, other);
/* 1658:     */       }
/* 1659:     */     }
/* 1660:1892 */     if (hasUnions) {
/* 1661:1893 */       return augmentWithMemPtg(result);
/* 1662:     */     }
/* 1663:1895 */     return result;
/* 1664:     */   }
/* 1665:     */   
/* 1666:     */   private ParseNode intersectionExpression()
/* 1667:     */   {
/* 1668:1900 */     ParseNode result = comparisonExpression();
/* 1669:1901 */     boolean hasIntersections = false;
/* 1670:     */     for (;;)
/* 1671:     */     {
/* 1672:1903 */       SkipWhite();
/* 1673:1904 */       if (this._inIntersection)
/* 1674:     */       {
/* 1675:1905 */         int savePointer = this._pointer;
/* 1676:     */         try
/* 1677:     */         {
/* 1678:1909 */           ParseNode other = comparisonExpression();
/* 1679:1910 */           result = new ParseNode(IntersectionPtg.instance, result, other);
/* 1680:1911 */           hasIntersections = true;
/* 1681:     */         }
/* 1682:     */         catch (FormulaParseException e)
/* 1683:     */         {
/* 1684:1916 */           resetPointer(savePointer);
/* 1685:     */         }
/* 1686:     */       }
/* 1687:     */     }
/* 1688:1919 */     if (hasIntersections) {
/* 1689:1920 */       return augmentWithMemPtg(result);
/* 1690:     */     }
/* 1691:1922 */     return result;
/* 1692:     */   }
/* 1693:     */   
/* 1694:     */   private ParseNode comparisonExpression()
/* 1695:     */   {
/* 1696:1927 */     ParseNode result = concatExpression();
/* 1697:     */     for (;;)
/* 1698:     */     {
/* 1699:1929 */       SkipWhite();
/* 1700:1930 */       switch (this.look)
/* 1701:     */       {
/* 1702:     */       case 60: 
/* 1703:     */       case 61: 
/* 1704:     */       case 62: 
/* 1705:1934 */         Ptg comparisonToken = getComparisonToken();
/* 1706:1935 */         ParseNode other = concatExpression();
/* 1707:1936 */         result = new ParseNode(comparisonToken, result, other);
/* 1708:     */       }
/* 1709:     */     }
/* 1710:1939 */     return result;
/* 1711:     */   }
/* 1712:     */   
/* 1713:     */   private Ptg getComparisonToken()
/* 1714:     */   {
/* 1715:1944 */     if (this.look == 61)
/* 1716:     */     {
/* 1717:1945 */       Match(this.look);
/* 1718:1946 */       return EqualPtg.instance;
/* 1719:     */     }
/* 1720:1948 */     boolean isGreater = this.look == 62;
/* 1721:1949 */     Match(this.look);
/* 1722:1950 */     if (isGreater)
/* 1723:     */     {
/* 1724:1951 */       if (this.look == 61)
/* 1725:     */       {
/* 1726:1952 */         Match(61);
/* 1727:1953 */         return GreaterEqualPtg.instance;
/* 1728:     */       }
/* 1729:1955 */       return GreaterThanPtg.instance;
/* 1730:     */     }
/* 1731:1957 */     switch (this.look)
/* 1732:     */     {
/* 1733:     */     case 61: 
/* 1734:1959 */       Match(61);
/* 1735:1960 */       return LessEqualPtg.instance;
/* 1736:     */     case 62: 
/* 1737:1962 */       Match(62);
/* 1738:1963 */       return NotEqualPtg.instance;
/* 1739:     */     }
/* 1740:1965 */     return LessThanPtg.instance;
/* 1741:     */   }
/* 1742:     */   
/* 1743:     */   private ParseNode concatExpression()
/* 1744:     */   {
/* 1745:1970 */     ParseNode result = additiveExpression();
/* 1746:     */     for (;;)
/* 1747:     */     {
/* 1748:1972 */       SkipWhite();
/* 1749:1973 */       if (this.look != 38) {
/* 1750:     */         break;
/* 1751:     */       }
/* 1752:1976 */       Match(38);
/* 1753:1977 */       ParseNode other = additiveExpression();
/* 1754:1978 */       result = new ParseNode(ConcatPtg.instance, result, other);
/* 1755:     */     }
/* 1756:1980 */     return result;
/* 1757:     */   }
/* 1758:     */   
/* 1759:     */   private ParseNode additiveExpression()
/* 1760:     */   {
/* 1761:1986 */     ParseNode result = Term();
/* 1762:     */     for (;;)
/* 1763:     */     {
/* 1764:1988 */       SkipWhite();
/* 1765:     */       Ptg operator;
/* 1766:1990 */       switch (this.look)
/* 1767:     */       {
/* 1768:     */       case 43: 
/* 1769:1992 */         Match(43);
/* 1770:1993 */         operator = AddPtg.instance;
/* 1771:1994 */         break;
/* 1772:     */       case 45: 
/* 1773:1996 */         Match(45);
/* 1774:1997 */         operator = SubtractPtg.instance;
/* 1775:1998 */         break;
/* 1776:     */       default: 
/* 1777:2000 */         return result;
/* 1778:     */       }
/* 1779:2002 */       ParseNode other = Term();
/* 1780:2003 */       result = new ParseNode(operator, result, other);
/* 1781:     */     }
/* 1782:     */   }
/* 1783:     */   
/* 1784:     */   private void parse()
/* 1785:     */   {
/* 1786:2026 */     this._pointer = 0;
/* 1787:2027 */     GetChar();
/* 1788:2028 */     this._rootNode = unionExpression();
/* 1789:2030 */     if (this._pointer <= this._formulaLength)
/* 1790:     */     {
/* 1791:2031 */       String msg = "Unused input [" + this._formulaString.substring(this._pointer - 1) + "] after attempting to parse the formula [" + this._formulaString + "]";
/* 1792:     */       
/* 1793:2033 */       throw new FormulaParseException(msg);
/* 1794:     */     }
/* 1795:     */   }
/* 1796:     */   
/* 1797:     */   private Ptg[] getRPNPtg(FormulaType formulaType)
/* 1798:     */   {
/* 1799:2038 */     OperandClassTransformer oct = new OperandClassTransformer(formulaType);
/* 1800:     */     
/* 1801:2040 */     oct.transformFormula(this._rootNode);
/* 1802:2041 */     return ParseNode.toTokenArray(this._rootNode);
/* 1803:     */   }
/* 1804:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.FormulaParser

 * JD-Core Version:    0.7.0.1

 */