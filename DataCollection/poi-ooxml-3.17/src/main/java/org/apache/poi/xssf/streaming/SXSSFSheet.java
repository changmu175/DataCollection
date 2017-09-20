/*    1:     */ package org.apache.poi.xssf.streaming;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.InputStream;
/*    5:     */ import java.util.Collection;
/*    6:     */ import java.util.Iterator;
/*    7:     */ import java.util.List;
/*    8:     */ import java.util.Map;
/*    9:     */ import java.util.Map.Entry;
/*   10:     */ import java.util.Set;
/*   11:     */ import java.util.SortedMap;
/*   12:     */ import java.util.TreeMap;
/*   13:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   14:     */ import org.apache.poi.ss.usermodel.AutoFilter;
/*   15:     */ import org.apache.poi.ss.usermodel.Cell;
/*   16:     */ import org.apache.poi.ss.usermodel.CellRange;
/*   17:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   18:     */ import org.apache.poi.ss.usermodel.DataValidation;
/*   19:     */ import org.apache.poi.ss.usermodel.DataValidationHelper;
/*   20:     */ import org.apache.poi.ss.usermodel.Footer;
/*   21:     */ import org.apache.poi.ss.usermodel.Header;
/*   22:     */ import org.apache.poi.ss.usermodel.PrintSetup;
/*   23:     */ import org.apache.poi.ss.usermodel.Row;
/*   24:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   25:     */ import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
/*   26:     */ import org.apache.poi.ss.util.CellAddress;
/*   27:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   28:     */ import org.apache.poi.ss.util.PaneInformation;
/*   29:     */ import org.apache.poi.ss.util.SheetUtil;
/*   30:     */ import org.apache.poi.util.Internal;
/*   31:     */ import org.apache.poi.util.NotImplemented;
/*   32:     */ import org.apache.poi.xssf.usermodel.XSSFColor;
/*   33:     */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*   34:     */ import org.apache.poi.xssf.usermodel.XSSFDataValidation;
/*   35:     */ import org.apache.poi.xssf.usermodel.XSSFDrawing;
/*   36:     */ import org.apache.poi.xssf.usermodel.XSSFHyperlink;
/*   37:     */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*   38:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*   39:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor.Factory;
/*   40:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
/*   41:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr;
/*   42:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;
/*   43:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*   44:     */ 
/*   45:     */ public class SXSSFSheet
/*   46:     */   implements Sheet
/*   47:     */ {
/*   48:     */   final XSSFSheet _sh;
/*   49:     */   private final SXSSFWorkbook _workbook;
/*   50:  69 */   private final TreeMap<Integer, SXSSFRow> _rows = new TreeMap();
/*   51:     */   private final SheetDataWriter _writer;
/*   52:  71 */   private int _randomAccessWindowSize = 100;
/*   53:     */   private final AutoSizeColumnTracker _autoSizeColumnTracker;
/*   54:  73 */   private int outlineLevelRow = 0;
/*   55:  74 */   private int lastFlushedRowNumber = -1;
/*   56:  75 */   private boolean allFlushed = false;
/*   57:     */   
/*   58:     */   public SXSSFSheet(SXSSFWorkbook workbook, XSSFSheet xSheet)
/*   59:     */     throws IOException
/*   60:     */   {
/*   61:  78 */     this._workbook = workbook;
/*   62:  79 */     this._sh = xSheet;
/*   63:  80 */     this._writer = workbook.createSheetDataWriter();
/*   64:  81 */     setRandomAccessWindowSize(this._workbook.getRandomAccessWindowSize());
/*   65:  82 */     this._autoSizeColumnTracker = new AutoSizeColumnTracker(this);
/*   66:     */   }
/*   67:     */   
/*   68:     */   @Internal
/*   69:     */   SheetDataWriter getSheetDataWriter()
/*   70:     */   {
/*   71:  90 */     return this._writer;
/*   72:     */   }
/*   73:     */   
/*   74:     */   public InputStream getWorksheetXMLInputStream()
/*   75:     */     throws IOException
/*   76:     */   {
/*   77:  97 */     flushRows(0);
/*   78:  98 */     this._writer.close();
/*   79:  99 */     return this._writer.getWorksheetXMLInputStream();
/*   80:     */   }
/*   81:     */   
/*   82:     */   public Iterator<Row> iterator()
/*   83:     */   {
/*   84: 106 */     return rowIterator();
/*   85:     */   }
/*   86:     */   
/*   87:     */   public SXSSFRow createRow(int rownum)
/*   88:     */   {
/*   89: 121 */     int maxrow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
/*   90: 122 */     if ((rownum < 0) || (rownum > maxrow)) {
/*   91: 123 */       throw new IllegalArgumentException("Invalid row number (" + rownum + ") outside allowable range (0.." + maxrow + ")");
/*   92:     */     }
/*   93: 128 */     if (rownum <= this._writer.getLastFlushedRow()) {
/*   94: 129 */       throw new IllegalArgumentException("Attempting to write a row[" + rownum + "] " + "in the range [0," + this._writer.getLastFlushedRow() + "] that is already written to disk.");
/*   95:     */     }
/*   96: 135 */     if ((this._sh.getPhysicalNumberOfRows() > 0) && (rownum <= this._sh.getLastRowNum())) {
/*   97: 136 */       throw new IllegalArgumentException("Attempting to write a row[" + rownum + "] " + "in the range [0," + this._sh.getLastRowNum() + "] that is already written to disk.");
/*   98:     */     }
/*   99: 141 */     SXSSFRow newRow = new SXSSFRow(this);
/*  100: 142 */     this._rows.put(Integer.valueOf(rownum), newRow);
/*  101: 143 */     this.allFlushed = false;
/*  102: 144 */     if ((this._randomAccessWindowSize >= 0) && (this._rows.size() > this._randomAccessWindowSize)) {
/*  103:     */       try
/*  104:     */       {
/*  105: 148 */         flushRows(this._randomAccessWindowSize);
/*  106:     */       }
/*  107:     */       catch (IOException ioe)
/*  108:     */       {
/*  109: 152 */         throw new RuntimeException(ioe);
/*  110:     */       }
/*  111:     */     }
/*  112: 155 */     return newRow;
/*  113:     */   }
/*  114:     */   
/*  115:     */   public void removeRow(Row row)
/*  116:     */   {
/*  117: 166 */     if (row.getSheet() != this) {
/*  118: 167 */       throw new IllegalArgumentException("Specified row does not belong to this sheet");
/*  119:     */     }
/*  120: 170 */     for (Iterator<Entry<Integer, SXSSFRow>> iter = this._rows.entrySet().iterator(); iter.hasNext();)
/*  121:     */     {
/*  122: 172 */       Entry<Integer, SXSSFRow> entry = (Entry)iter.next();
/*  123: 173 */       if (entry.getValue() == row)
/*  124:     */       {
/*  125: 175 */         iter.remove();
/*  126: 176 */         return;
/*  127:     */       }
/*  128:     */     }
/*  129:     */   }
/*  130:     */   
/*  131:     */   public SXSSFRow getRow(int rownum)
/*  132:     */   {
/*  133: 191 */     return (SXSSFRow)this._rows.get(Integer.valueOf(rownum));
/*  134:     */   }
/*  135:     */   
/*  136:     */   public int getPhysicalNumberOfRows()
/*  137:     */   {
/*  138: 202 */     return this._rows.size() + this._writer.getNumberOfFlushedRows();
/*  139:     */   }
/*  140:     */   
/*  141:     */   public int getFirstRowNum()
/*  142:     */   {
/*  143: 213 */     if (this._writer.getNumberOfFlushedRows() > 0) {
/*  144: 214 */       return this._writer.getLowestIndexOfFlushedRows();
/*  145:     */     }
/*  146: 216 */     return this._rows.size() == 0 ? 0 : ((Integer)this._rows.firstKey()).intValue();
/*  147:     */   }
/*  148:     */   
/*  149:     */   public int getLastRowNum()
/*  150:     */   {
/*  151: 227 */     return this._rows.size() == 0 ? 0 : ((Integer)this._rows.lastKey()).intValue();
/*  152:     */   }
/*  153:     */   
/*  154:     */   public void setColumnHidden(int columnIndex, boolean hidden)
/*  155:     */   {
/*  156: 239 */     this._sh.setColumnHidden(columnIndex, hidden);
/*  157:     */   }
/*  158:     */   
/*  159:     */   public boolean isColumnHidden(int columnIndex)
/*  160:     */   {
/*  161: 251 */     return this._sh.isColumnHidden(columnIndex);
/*  162:     */   }
/*  163:     */   
/*  164:     */   public void setColumnWidth(int columnIndex, int width)
/*  165:     */   {
/*  166: 268 */     this._sh.setColumnWidth(columnIndex, width);
/*  167:     */   }
/*  168:     */   
/*  169:     */   public int getColumnWidth(int columnIndex)
/*  170:     */   {
/*  171: 279 */     return this._sh.getColumnWidth(columnIndex);
/*  172:     */   }
/*  173:     */   
/*  174:     */   public float getColumnWidthInPixels(int columnIndex)
/*  175:     */   {
/*  176: 292 */     return this._sh.getColumnWidthInPixels(columnIndex);
/*  177:     */   }
/*  178:     */   
/*  179:     */   public void setDefaultColumnWidth(int width)
/*  180:     */   {
/*  181: 304 */     this._sh.setDefaultColumnWidth(width);
/*  182:     */   }
/*  183:     */   
/*  184:     */   public int getDefaultColumnWidth()
/*  185:     */   {
/*  186: 316 */     return this._sh.getDefaultColumnWidth();
/*  187:     */   }
/*  188:     */   
/*  189:     */   public short getDefaultRowHeight()
/*  190:     */   {
/*  191: 329 */     return this._sh.getDefaultRowHeight();
/*  192:     */   }
/*  193:     */   
/*  194:     */   public float getDefaultRowHeightInPoints()
/*  195:     */   {
/*  196: 341 */     return this._sh.getDefaultRowHeightInPoints();
/*  197:     */   }
/*  198:     */   
/*  199:     */   public void setDefaultRowHeight(short height)
/*  200:     */   {
/*  201: 353 */     this._sh.setDefaultRowHeight(height);
/*  202:     */   }
/*  203:     */   
/*  204:     */   public void setDefaultRowHeightInPoints(float height)
/*  205:     */   {
/*  206: 364 */     this._sh.setDefaultRowHeightInPoints(height);
/*  207:     */   }
/*  208:     */   
/*  209:     */   public CellStyle getColumnStyle(int column)
/*  210:     */   {
/*  211: 376 */     return this._sh.getColumnStyle(column);
/*  212:     */   }
/*  213:     */   
/*  214:     */   public int addMergedRegion(CellRangeAddress region)
/*  215:     */   {
/*  216: 394 */     return this._sh.addMergedRegion(region);
/*  217:     */   }
/*  218:     */   
/*  219:     */   public int addMergedRegionUnsafe(CellRangeAddress region)
/*  220:     */   {
/*  221: 406 */     return this._sh.addMergedRegionUnsafe(region);
/*  222:     */   }
/*  223:     */   
/*  224:     */   public void validateMergedRegions()
/*  225:     */   {
/*  226: 418 */     this._sh.validateMergedRegions();
/*  227:     */   }
/*  228:     */   
/*  229:     */   public void setVerticallyCenter(boolean value)
/*  230:     */   {
/*  231: 429 */     this._sh.setVerticallyCenter(value);
/*  232:     */   }
/*  233:     */   
/*  234:     */   public void setHorizontallyCenter(boolean value)
/*  235:     */   {
/*  236: 440 */     this._sh.setHorizontallyCenter(value);
/*  237:     */   }
/*  238:     */   
/*  239:     */   public boolean getHorizontallyCenter()
/*  240:     */   {
/*  241: 449 */     return this._sh.getHorizontallyCenter();
/*  242:     */   }
/*  243:     */   
/*  244:     */   public boolean getVerticallyCenter()
/*  245:     */   {
/*  246: 458 */     return this._sh.getVerticallyCenter();
/*  247:     */   }
/*  248:     */   
/*  249:     */   public void removeMergedRegion(int index)
/*  250:     */   {
/*  251: 469 */     this._sh.removeMergedRegion(index);
/*  252:     */   }
/*  253:     */   
/*  254:     */   public void removeMergedRegions(Collection<Integer> indices)
/*  255:     */   {
/*  256: 480 */     this._sh.removeMergedRegions(indices);
/*  257:     */   }
/*  258:     */   
/*  259:     */   public int getNumMergedRegions()
/*  260:     */   {
/*  261: 491 */     return this._sh.getNumMergedRegions();
/*  262:     */   }
/*  263:     */   
/*  264:     */   public CellRangeAddress getMergedRegion(int index)
/*  265:     */   {
/*  266: 504 */     return this._sh.getMergedRegion(index);
/*  267:     */   }
/*  268:     */   
/*  269:     */   public List<CellRangeAddress> getMergedRegions()
/*  270:     */   {
/*  271: 515 */     return this._sh.getMergedRegions();
/*  272:     */   }
/*  273:     */   
/*  274:     */   public Iterator<Row> rowIterator()
/*  275:     */   {
/*  276: 528 */     Iterator<Row> result = this._rows.values().iterator();
/*  277: 529 */     return result;
/*  278:     */   }
/*  279:     */   
/*  280:     */   public void setAutobreaks(boolean value)
/*  281:     */   {
/*  282: 540 */     this._sh.setAutobreaks(value);
/*  283:     */   }
/*  284:     */   
/*  285:     */   public void setDisplayGuts(boolean value)
/*  286:     */   {
/*  287: 551 */     this._sh.setDisplayGuts(value);
/*  288:     */   }
/*  289:     */   
/*  290:     */   public void setDisplayZeros(boolean value)
/*  291:     */   {
/*  292: 563 */     this._sh.setDisplayZeros(value);
/*  293:     */   }
/*  294:     */   
/*  295:     */   public boolean isDisplayZeros()
/*  296:     */   {
/*  297: 576 */     return this._sh.isDisplayZeros();
/*  298:     */   }
/*  299:     */   
/*  300:     */   public void setRightToLeft(boolean value)
/*  301:     */   {
/*  302: 587 */     this._sh.setRightToLeft(value);
/*  303:     */   }
/*  304:     */   
/*  305:     */   public boolean isRightToLeft()
/*  306:     */   {
/*  307: 598 */     return this._sh.isRightToLeft();
/*  308:     */   }
/*  309:     */   
/*  310:     */   public void setFitToPage(boolean value)
/*  311:     */   {
/*  312: 609 */     this._sh.setFitToPage(value);
/*  313:     */   }
/*  314:     */   
/*  315:     */   public void setRowSumsBelow(boolean value)
/*  316:     */   {
/*  317: 628 */     this._sh.setRowSumsBelow(value);
/*  318:     */   }
/*  319:     */   
/*  320:     */   public void setRowSumsRight(boolean value)
/*  321:     */   {
/*  322: 647 */     this._sh.setRowSumsRight(value);
/*  323:     */   }
/*  324:     */   
/*  325:     */   public boolean getAutobreaks()
/*  326:     */   {
/*  327: 658 */     return this._sh.getAutobreaks();
/*  328:     */   }
/*  329:     */   
/*  330:     */   public boolean getDisplayGuts()
/*  331:     */   {
/*  332: 670 */     return this._sh.getDisplayGuts();
/*  333:     */   }
/*  334:     */   
/*  335:     */   public boolean getFitToPage()
/*  336:     */   {
/*  337: 681 */     return this._sh.getFitToPage();
/*  338:     */   }
/*  339:     */   
/*  340:     */   public boolean getRowSumsBelow()
/*  341:     */   {
/*  342: 700 */     return this._sh.getRowSumsBelow();
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean getRowSumsRight()
/*  346:     */   {
/*  347: 719 */     return this._sh.getRowSumsRight();
/*  348:     */   }
/*  349:     */   
/*  350:     */   public boolean isPrintGridlines()
/*  351:     */   {
/*  352: 730 */     return this._sh.isPrintGridlines();
/*  353:     */   }
/*  354:     */   
/*  355:     */   public void setPrintGridlines(boolean show)
/*  356:     */   {
/*  357: 741 */     this._sh.setPrintGridlines(show);
/*  358:     */   }
/*  359:     */   
/*  360:     */   public boolean isPrintRowAndColumnHeadings()
/*  361:     */   {
/*  362: 752 */     return this._sh.isPrintRowAndColumnHeadings();
/*  363:     */   }
/*  364:     */   
/*  365:     */   public void setPrintRowAndColumnHeadings(boolean show)
/*  366:     */   {
/*  367: 763 */     this._sh.setPrintRowAndColumnHeadings(show);
/*  368:     */   }
/*  369:     */   
/*  370:     */   public PrintSetup getPrintSetup()
/*  371:     */   {
/*  372: 774 */     return this._sh.getPrintSetup();
/*  373:     */   }
/*  374:     */   
/*  375:     */   public Header getHeader()
/*  376:     */   {
/*  377: 787 */     return this._sh.getHeader();
/*  378:     */   }
/*  379:     */   
/*  380:     */   public Footer getFooter()
/*  381:     */   {
/*  382: 800 */     return this._sh.getFooter();
/*  383:     */   }
/*  384:     */   
/*  385:     */   public void setSelected(boolean value)
/*  386:     */   {
/*  387: 814 */     this._sh.setSelected(value);
/*  388:     */   }
/*  389:     */   
/*  390:     */   public double getMargin(short margin)
/*  391:     */   {
/*  392: 826 */     return this._sh.getMargin(margin);
/*  393:     */   }
/*  394:     */   
/*  395:     */   public void setMargin(short margin, double size)
/*  396:     */   {
/*  397: 838 */     this._sh.setMargin(margin, size);
/*  398:     */   }
/*  399:     */   
/*  400:     */   public boolean getProtect()
/*  401:     */   {
/*  402: 849 */     return this._sh.getProtect();
/*  403:     */   }
/*  404:     */   
/*  405:     */   public void protectSheet(String password)
/*  406:     */   {
/*  407: 859 */     this._sh.protectSheet(password);
/*  408:     */   }
/*  409:     */   
/*  410:     */   public boolean getScenarioProtect()
/*  411:     */   {
/*  412: 870 */     return this._sh.getScenarioProtect();
/*  413:     */   }
/*  414:     */   
/*  415:     */   public void setZoom(int scale)
/*  416:     */   {
/*  417: 894 */     this._sh.setZoom(scale);
/*  418:     */   }
/*  419:     */   
/*  420:     */   public short getTopRow()
/*  421:     */   {
/*  422: 906 */     return this._sh.getTopRow();
/*  423:     */   }
/*  424:     */   
/*  425:     */   public short getLeftCol()
/*  426:     */   {
/*  427: 918 */     return this._sh.getLeftCol();
/*  428:     */   }
/*  429:     */   
/*  430:     */   public void showInPane(int toprow, int leftcol)
/*  431:     */   {
/*  432: 931 */     this._sh.showInPane(toprow, leftcol);
/*  433:     */   }
/*  434:     */   
/*  435:     */   public void setForceFormulaRecalculation(boolean value)
/*  436:     */   {
/*  437: 943 */     this._sh.setForceFormulaRecalculation(value);
/*  438:     */   }
/*  439:     */   
/*  440:     */   public boolean getForceFormulaRecalculation()
/*  441:     */   {
/*  442: 952 */     return this._sh.getForceFormulaRecalculation();
/*  443:     */   }
/*  444:     */   
/*  445:     */   @NotImplemented
/*  446:     */   public void shiftRows(int startRow, int endRow, int n)
/*  447:     */   {
/*  448: 975 */     throw new RuntimeException("NotImplemented");
/*  449:     */   }
/*  450:     */   
/*  451:     */   @NotImplemented
/*  452:     */   public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight)
/*  453:     */   {
/*  454:1000 */     throw new RuntimeException("NotImplemented");
/*  455:     */   }
/*  456:     */   
/*  457:     */   public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
/*  458:     */   {
/*  459:1013 */     this._sh.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
/*  460:     */   }
/*  461:     */   
/*  462:     */   public void createFreezePane(int colSplit, int rowSplit)
/*  463:     */   {
/*  464:1024 */     this._sh.createFreezePane(colSplit, rowSplit);
/*  465:     */   }
/*  466:     */   
/*  467:     */   public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane)
/*  468:     */   {
/*  469:1043 */     this._sh.createSplitPane(xSplitPos, ySplitPos, leftmostColumn, topRow, activePane);
/*  470:     */   }
/*  471:     */   
/*  472:     */   public PaneInformation getPaneInformation()
/*  473:     */   {
/*  474:1054 */     return this._sh.getPaneInformation();
/*  475:     */   }
/*  476:     */   
/*  477:     */   public void setDisplayGridlines(boolean show)
/*  478:     */   {
/*  479:1065 */     this._sh.setDisplayGridlines(show);
/*  480:     */   }
/*  481:     */   
/*  482:     */   public boolean isDisplayGridlines()
/*  483:     */   {
/*  484:1076 */     return this._sh.isDisplayGridlines();
/*  485:     */   }
/*  486:     */   
/*  487:     */   public void setDisplayFormulas(boolean show)
/*  488:     */   {
/*  489:1087 */     this._sh.setDisplayFormulas(show);
/*  490:     */   }
/*  491:     */   
/*  492:     */   public boolean isDisplayFormulas()
/*  493:     */   {
/*  494:1098 */     return this._sh.isDisplayFormulas();
/*  495:     */   }
/*  496:     */   
/*  497:     */   public void setDisplayRowColHeadings(boolean show)
/*  498:     */   {
/*  499:1109 */     this._sh.setDisplayRowColHeadings(show);
/*  500:     */   }
/*  501:     */   
/*  502:     */   public boolean isDisplayRowColHeadings()
/*  503:     */   {
/*  504:1119 */     return this._sh.isDisplayRowColHeadings();
/*  505:     */   }
/*  506:     */   
/*  507:     */   public void setRowBreak(int row)
/*  508:     */   {
/*  509:1129 */     this._sh.setRowBreak(row);
/*  510:     */   }
/*  511:     */   
/*  512:     */   public boolean isRowBroken(int row)
/*  513:     */   {
/*  514:1140 */     return this._sh.isRowBroken(row);
/*  515:     */   }
/*  516:     */   
/*  517:     */   public void removeRowBreak(int row)
/*  518:     */   {
/*  519:1150 */     this._sh.removeRowBreak(row);
/*  520:     */   }
/*  521:     */   
/*  522:     */   public int[] getRowBreaks()
/*  523:     */   {
/*  524:1160 */     return this._sh.getRowBreaks();
/*  525:     */   }
/*  526:     */   
/*  527:     */   public int[] getColumnBreaks()
/*  528:     */   {
/*  529:1170 */     return this._sh.getColumnBreaks();
/*  530:     */   }
/*  531:     */   
/*  532:     */   public void setColumnBreak(int column)
/*  533:     */   {
/*  534:1180 */     this._sh.setColumnBreak(column);
/*  535:     */   }
/*  536:     */   
/*  537:     */   public boolean isColumnBroken(int column)
/*  538:     */   {
/*  539:1191 */     return this._sh.isColumnBroken(column);
/*  540:     */   }
/*  541:     */   
/*  542:     */   public void removeColumnBreak(int column)
/*  543:     */   {
/*  544:1201 */     this._sh.removeColumnBreak(column);
/*  545:     */   }
/*  546:     */   
/*  547:     */   public void setColumnGroupCollapsed(int columnNumber, boolean collapsed)
/*  548:     */   {
/*  549:1213 */     this._sh.setColumnGroupCollapsed(columnNumber, collapsed);
/*  550:     */   }
/*  551:     */   
/*  552:     */   public void groupColumn(int fromColumn, int toColumn)
/*  553:     */   {
/*  554:1225 */     this._sh.groupColumn(fromColumn, toColumn);
/*  555:     */   }
/*  556:     */   
/*  557:     */   public void ungroupColumn(int fromColumn, int toColumn)
/*  558:     */   {
/*  559:1237 */     this._sh.ungroupColumn(fromColumn, toColumn);
/*  560:     */   }
/*  561:     */   
/*  562:     */   public void groupRow(int fromRow, int toRow)
/*  563:     */   {
/*  564:1280 */     for (SXSSFRow row : this._rows.subMap(Integer.valueOf(fromRow), Integer.valueOf(toRow + 1)).values())
/*  565:     */     {
/*  566:1281 */       int level = row.getOutlineLevel() + 1;
/*  567:1282 */       row.setOutlineLevel(level);
/*  568:1284 */       if (level > this.outlineLevelRow) {
/*  569:1285 */         this.outlineLevelRow = level;
/*  570:     */       }
/*  571:     */     }
/*  572:1289 */     setWorksheetOutlineLevelRow();
/*  573:     */   }
/*  574:     */   
/*  575:     */   public void setRowOutlineLevel(int rownum, int level)
/*  576:     */   {
/*  577:1308 */     SXSSFRow row = (SXSSFRow)this._rows.get(Integer.valueOf(rownum));
/*  578:1309 */     row.setOutlineLevel(level);
/*  579:1310 */     if ((level > 0) && (level > this.outlineLevelRow))
/*  580:     */     {
/*  581:1311 */       this.outlineLevelRow = level;
/*  582:1312 */       setWorksheetOutlineLevelRow();
/*  583:     */     }
/*  584:     */   }
/*  585:     */   
/*  586:     */   private void setWorksheetOutlineLevelRow()
/*  587:     */   {
/*  588:1317 */     CTWorksheet ct = this._sh.getCTWorksheet();
/*  589:1318 */     CTSheetFormatPr pr = ct.isSetSheetFormatPr() ? ct.getSheetFormatPr() : ct.addNewSheetFormatPr();
/*  590:1321 */     if (this.outlineLevelRow > 0) {
/*  591:1322 */       pr.setOutlineLevelRow((short)this.outlineLevelRow);
/*  592:     */     }
/*  593:     */   }
/*  594:     */   
/*  595:     */   public void ungroupRow(int fromRow, int toRow)
/*  596:     */   {
/*  597:1335 */     this._sh.ungroupRow(fromRow, toRow);
/*  598:     */   }
/*  599:     */   
/*  600:     */   public void setRowGroupCollapsed(int row, boolean collapse)
/*  601:     */   {
/*  602:1350 */     if (collapse) {
/*  603:1351 */       collapseRow(row);
/*  604:     */     } else {
/*  605:1354 */       throw new RuntimeException("Unable to expand row: Not Implemented");
/*  606:     */     }
/*  607:     */   }
/*  608:     */   
/*  609:     */   private void collapseRow(int rowIndex)
/*  610:     */   {
/*  611:1362 */     SXSSFRow row = getRow(rowIndex);
/*  612:1363 */     if (row == null) {
/*  613:1364 */       throw new IllegalArgumentException("Invalid row number(" + rowIndex + "). Row does not exist.");
/*  614:     */     }
/*  615:1366 */     int startRow = findStartOfRowOutlineGroup(rowIndex);
/*  616:     */     
/*  617:     */ 
/*  618:1369 */     int lastRow = writeHidden(row, startRow, true);
/*  619:1370 */     SXSSFRow lastRowObj = getRow(lastRow);
/*  620:1371 */     if (lastRowObj != null)
/*  621:     */     {
/*  622:1372 */       lastRowObj.setCollapsed(Boolean.valueOf(true));
/*  623:     */     }
/*  624:     */     else
/*  625:     */     {
/*  626:1374 */       SXSSFRow newRow = createRow(lastRow);
/*  627:1375 */       newRow.setCollapsed(Boolean.valueOf(true));
/*  628:     */     }
/*  629:     */   }
/*  630:     */   
/*  631:     */   private int findStartOfRowOutlineGroup(int rowIndex)
/*  632:     */   {
/*  633:1385 */     Row row = getRow(rowIndex);
/*  634:1386 */     int level = ((SXSSFRow)row).getOutlineLevel();
/*  635:1387 */     if (level == 0) {
/*  636:1388 */       throw new IllegalArgumentException("Outline level is zero for the row (" + rowIndex + ").");
/*  637:     */     }
/*  638:1390 */     int currentRow = rowIndex;
/*  639:1391 */     while (getRow(currentRow) != null)
/*  640:     */     {
/*  641:1392 */       if (getRow(currentRow).getOutlineLevel() < level) {
/*  642:1393 */         return currentRow + 1;
/*  643:     */       }
/*  644:1395 */       currentRow--;
/*  645:     */     }
/*  646:1397 */     return currentRow + 1;
/*  647:     */   }
/*  648:     */   
/*  649:     */   private int writeHidden(SXSSFRow xRow, int rowIndex, boolean hidden)
/*  650:     */   {
/*  651:1401 */     int level = xRow.getOutlineLevel();
/*  652:1402 */     SXSSFRow currRow = getRow(rowIndex);
/*  653:1404 */     while ((currRow != null) && (currRow.getOutlineLevel() >= level))
/*  654:     */     {
/*  655:1405 */       currRow.setHidden(Boolean.valueOf(hidden));
/*  656:1406 */       rowIndex++;
/*  657:1407 */       currRow = getRow(rowIndex);
/*  658:     */     }
/*  659:1409 */     return rowIndex;
/*  660:     */   }
/*  661:     */   
/*  662:     */   public void setDefaultColumnStyle(int column, CellStyle style)
/*  663:     */   {
/*  664:1421 */     this._sh.setDefaultColumnStyle(column, style);
/*  665:     */   }
/*  666:     */   
/*  667:     */   public void trackColumnForAutoSizing(int column)
/*  668:     */   {
/*  669:1437 */     this._autoSizeColumnTracker.trackColumn(column);
/*  670:     */   }
/*  671:     */   
/*  672:     */   public void trackColumnsForAutoSizing(Collection<Integer> columns)
/*  673:     */   {
/*  674:1450 */     this._autoSizeColumnTracker.trackColumns(columns);
/*  675:     */   }
/*  676:     */   
/*  677:     */   public void trackAllColumnsForAutoSizing()
/*  678:     */   {
/*  679:1460 */     this._autoSizeColumnTracker.trackAllColumns();
/*  680:     */   }
/*  681:     */   
/*  682:     */   public boolean untrackColumnForAutoSizing(int column)
/*  683:     */   {
/*  684:1476 */     return this._autoSizeColumnTracker.untrackColumn(column);
/*  685:     */   }
/*  686:     */   
/*  687:     */   public boolean untrackColumnsForAutoSizing(Collection<Integer> columns)
/*  688:     */   {
/*  689:1490 */     return this._autoSizeColumnTracker.untrackColumns(columns);
/*  690:     */   }
/*  691:     */   
/*  692:     */   public void untrackAllColumnsForAutoSizing()
/*  693:     */   {
/*  694:1500 */     this._autoSizeColumnTracker.untrackAllColumns();
/*  695:     */   }
/*  696:     */   
/*  697:     */   public boolean isColumnTrackedForAutoSizing(int column)
/*  698:     */   {
/*  699:1512 */     return this._autoSizeColumnTracker.isColumnTracked(column);
/*  700:     */   }
/*  701:     */   
/*  702:     */   public Set<Integer> getTrackedColumnsForAutoSizing()
/*  703:     */   {
/*  704:1525 */     return this._autoSizeColumnTracker.getTrackedColumns();
/*  705:     */   }
/*  706:     */   
/*  707:     */   public void autoSizeColumn(int column)
/*  708:     */   {
/*  709:1553 */     autoSizeColumn(column, false);
/*  710:     */   }
/*  711:     */   
/*  712:     */   public void autoSizeColumn(int column, boolean useMergedCells)
/*  713:     */   {
/*  714:     */     int flushedWidth;
/*  715:     */     try
/*  716:     */     {
/*  717:1596 */       flushedWidth = this._autoSizeColumnTracker.getBestFitColumnWidth(column, useMergedCells);
/*  718:     */     }
/*  719:     */     catch (IllegalStateException e)
/*  720:     */     {
/*  721:1599 */       throw new IllegalStateException("Could not auto-size column. Make sure the column was tracked prior to auto-sizing the column.", e);
/*  722:     */     }
/*  723:1603 */     int activeWidth = (int)(256.0D * SheetUtil.getColumnWidth(this, column, useMergedCells));
/*  724:     */     
/*  725:     */ 
/*  726:     */ 
/*  727:1607 */     int bestFitWidth = Math.max(flushedWidth, activeWidth);
/*  728:1609 */     if (bestFitWidth > 0)
/*  729:     */     {
/*  730:1610 */       int maxColumnWidth = 65280;
/*  731:1611 */       int width = Math.min(bestFitWidth, 65280);
/*  732:1612 */       setColumnWidth(column, width);
/*  733:     */     }
/*  734:     */   }
/*  735:     */   
/*  736:     */   public XSSFComment getCellComment(CellAddress ref)
/*  737:     */   {
/*  738:1624 */     return this._sh.getCellComment(ref);
/*  739:     */   }
/*  740:     */   
/*  741:     */   public Map<CellAddress, XSSFComment> getCellComments()
/*  742:     */   {
/*  743:1634 */     return this._sh.getCellComments();
/*  744:     */   }
/*  745:     */   
/*  746:     */   public XSSFHyperlink getHyperlink(int row, int column)
/*  747:     */   {
/*  748:1646 */     return this._sh.getHyperlink(row, column);
/*  749:     */   }
/*  750:     */   
/*  751:     */   public XSSFHyperlink getHyperlink(CellAddress addr)
/*  752:     */   {
/*  753:1658 */     return this._sh.getHyperlink(addr);
/*  754:     */   }
/*  755:     */   
/*  756:     */   public List<XSSFHyperlink> getHyperlinkList()
/*  757:     */   {
/*  758:1668 */     return this._sh.getHyperlinkList();
/*  759:     */   }
/*  760:     */   
/*  761:     */   public XSSFDrawing getDrawingPatriarch()
/*  762:     */   {
/*  763:1677 */     return this._sh.getDrawingPatriarch();
/*  764:     */   }
/*  765:     */   
/*  766:     */   public SXSSFDrawing createDrawingPatriarch()
/*  767:     */   {
/*  768:1688 */     return new SXSSFDrawing(getWorkbook(), this._sh.createDrawingPatriarch());
/*  769:     */   }
/*  770:     */   
/*  771:     */   public SXSSFWorkbook getWorkbook()
/*  772:     */   {
/*  773:1700 */     return this._workbook;
/*  774:     */   }
/*  775:     */   
/*  776:     */   public String getSheetName()
/*  777:     */   {
/*  778:1711 */     return this._sh.getSheetName();
/*  779:     */   }
/*  780:     */   
/*  781:     */   public boolean isSelected()
/*  782:     */   {
/*  783:1721 */     return this._sh.isSelected();
/*  784:     */   }
/*  785:     */   
/*  786:     */   public CellRange<? extends Cell> setArrayFormula(String formula, CellRangeAddress range)
/*  787:     */   {
/*  788:1735 */     return this._sh.setArrayFormula(formula, range);
/*  789:     */   }
/*  790:     */   
/*  791:     */   public CellRange<? extends Cell> removeArrayFormula(Cell cell)
/*  792:     */   {
/*  793:1747 */     return this._sh.removeArrayFormula(cell);
/*  794:     */   }
/*  795:     */   
/*  796:     */   public DataValidationHelper getDataValidationHelper()
/*  797:     */   {
/*  798:1753 */     return this._sh.getDataValidationHelper();
/*  799:     */   }
/*  800:     */   
/*  801:     */   public List<XSSFDataValidation> getDataValidations()
/*  802:     */   {
/*  803:1759 */     return this._sh.getDataValidations();
/*  804:     */   }
/*  805:     */   
/*  806:     */   public void addValidationData(DataValidation dataValidation)
/*  807:     */   {
/*  808:1769 */     this._sh.addValidationData(dataValidation);
/*  809:     */   }
/*  810:     */   
/*  811:     */   public AutoFilter setAutoFilter(CellRangeAddress range)
/*  812:     */   {
/*  813:1780 */     return this._sh.setAutoFilter(range);
/*  814:     */   }
/*  815:     */   
/*  816:     */   public SheetConditionalFormatting getSheetConditionalFormatting()
/*  817:     */   {
/*  818:1785 */     return this._sh.getSheetConditionalFormatting();
/*  819:     */   }
/*  820:     */   
/*  821:     */   public CellRangeAddress getRepeatingRows()
/*  822:     */   {
/*  823:1791 */     return this._sh.getRepeatingRows();
/*  824:     */   }
/*  825:     */   
/*  826:     */   public CellRangeAddress getRepeatingColumns()
/*  827:     */   {
/*  828:1796 */     return this._sh.getRepeatingColumns();
/*  829:     */   }
/*  830:     */   
/*  831:     */   public void setRepeatingRows(CellRangeAddress rowRangeRef)
/*  832:     */   {
/*  833:1801 */     this._sh.setRepeatingRows(rowRangeRef);
/*  834:     */   }
/*  835:     */   
/*  836:     */   public void setRepeatingColumns(CellRangeAddress columnRangeRef)
/*  837:     */   {
/*  838:1806 */     this._sh.setRepeatingColumns(columnRangeRef);
/*  839:     */   }
/*  840:     */   
/*  841:     */   public void setRandomAccessWindowSize(int value)
/*  842:     */   {
/*  843:1826 */     if ((value == 0) || (value < -1)) {
/*  844:1827 */       throw new IllegalArgumentException("RandomAccessWindowSize must be either -1 or a positive integer");
/*  845:     */     }
/*  846:1829 */     this._randomAccessWindowSize = value;
/*  847:     */   }
/*  848:     */   
/*  849:     */   public boolean areAllRowsFlushed()
/*  850:     */   {
/*  851:1836 */     return this.allFlushed;
/*  852:     */   }
/*  853:     */   
/*  854:     */   public int getLastFlushedRowNum()
/*  855:     */   {
/*  856:1842 */     return this.lastFlushedRowNumber;
/*  857:     */   }
/*  858:     */   
/*  859:     */   public void flushRows(int remaining)
/*  860:     */     throws IOException
/*  861:     */   {
/*  862:1852 */     while (this._rows.size() > remaining) {
/*  863:1853 */       flushOneRow();
/*  864:     */     }
/*  865:1855 */     if (remaining == 0) {
/*  866:1856 */       this.allFlushed = true;
/*  867:     */     }
/*  868:     */   }
/*  869:     */   
/*  870:     */   public void flushRows()
/*  871:     */     throws IOException
/*  872:     */   {
/*  873:1867 */     flushRows(0);
/*  874:     */   }
/*  875:     */   
/*  876:     */   private void flushOneRow()
/*  877:     */     throws IOException
/*  878:     */   {
/*  879:1872 */     Integer firstRowNum = (Integer)this._rows.firstKey();
/*  880:1873 */     if (firstRowNum != null)
/*  881:     */     {
/*  882:1874 */       int rowIndex = firstRowNum.intValue();
/*  883:1875 */       SXSSFRow row = (SXSSFRow)this._rows.get(firstRowNum);
/*  884:     */       
/*  885:1877 */       this._autoSizeColumnTracker.updateColumnWidths(row);
/*  886:1878 */       this._writer.writeRow(rowIndex, row);
/*  887:1879 */       this._rows.remove(firstRowNum);
/*  888:1880 */       this.lastFlushedRowNumber = rowIndex;
/*  889:     */     }
/*  890:     */   }
/*  891:     */   
/*  892:     */   public void changeRowNum(SXSSFRow row, int newRowNum)
/*  893:     */   {
/*  894:1886 */     removeRow(row);
/*  895:1887 */     this._rows.put(Integer.valueOf(newRowNum), row);
/*  896:     */   }
/*  897:     */   
/*  898:     */   public int getRowNum(SXSSFRow row)
/*  899:     */   {
/*  900:1892 */     for (Iterator<Entry<Integer, SXSSFRow>> iter = this._rows.entrySet().iterator(); iter.hasNext();)
/*  901:     */     {
/*  902:1894 */       Entry<Integer, SXSSFRow> entry = (Entry)iter.next();
/*  903:1895 */       if (entry.getValue() == row) {
/*  904:1896 */         return ((Integer)entry.getKey()).intValue();
/*  905:     */       }
/*  906:     */     }
/*  907:1899 */     return -1;
/*  908:     */   }
/*  909:     */   
/*  910:     */   boolean dispose()
/*  911:     */     throws IOException
/*  912:     */   {
/*  913:1907 */     if (!this.allFlushed) {
/*  914:1908 */       flushRows();
/*  915:     */     }
/*  916:1910 */     return this._writer.dispose();
/*  917:     */   }
/*  918:     */   
/*  919:     */   public int getColumnOutlineLevel(int columnIndex)
/*  920:     */   {
/*  921:1915 */     return this._sh.getColumnOutlineLevel(columnIndex);
/*  922:     */   }
/*  923:     */   
/*  924:     */   public CellAddress getActiveCell()
/*  925:     */   {
/*  926:1923 */     return this._sh.getActiveCell();
/*  927:     */   }
/*  928:     */   
/*  929:     */   public void setActiveCell(CellAddress address)
/*  930:     */   {
/*  931:1931 */     this._sh.setActiveCell(address);
/*  932:     */   }
/*  933:     */   
/*  934:     */   public XSSFColor getTabColor()
/*  935:     */   {
/*  936:1935 */     return this._sh.getTabColor();
/*  937:     */   }
/*  938:     */   
/*  939:     */   public void setTabColor(XSSFColor color)
/*  940:     */   {
/*  941:1939 */     this._sh.setTabColor(color);
/*  942:     */   }
/*  943:     */   
/*  944:     */   public void enableLocking()
/*  945:     */   {
/*  946:1946 */     safeGetProtectionField().setSheet(true);
/*  947:     */   }
/*  948:     */   
/*  949:     */   public void disableLocking()
/*  950:     */   {
/*  951:1953 */     safeGetProtectionField().setSheet(false);
/*  952:     */   }
/*  953:     */   
/*  954:     */   public void lockAutoFilter(boolean enabled)
/*  955:     */   {
/*  956:1962 */     safeGetProtectionField().setAutoFilter(enabled);
/*  957:     */   }
/*  958:     */   
/*  959:     */   public void lockDeleteColumns(boolean enabled)
/*  960:     */   {
/*  961:1971 */     safeGetProtectionField().setDeleteColumns(enabled);
/*  962:     */   }
/*  963:     */   
/*  964:     */   public void lockDeleteRows(boolean enabled)
/*  965:     */   {
/*  966:1980 */     safeGetProtectionField().setDeleteRows(enabled);
/*  967:     */   }
/*  968:     */   
/*  969:     */   public void lockFormatCells(boolean enabled)
/*  970:     */   {
/*  971:1989 */     safeGetProtectionField().setFormatCells(enabled);
/*  972:     */   }
/*  973:     */   
/*  974:     */   public void lockFormatColumns(boolean enabled)
/*  975:     */   {
/*  976:1998 */     safeGetProtectionField().setFormatColumns(enabled);
/*  977:     */   }
/*  978:     */   
/*  979:     */   public void lockFormatRows(boolean enabled)
/*  980:     */   {
/*  981:2007 */     safeGetProtectionField().setFormatRows(enabled);
/*  982:     */   }
/*  983:     */   
/*  984:     */   public void lockInsertColumns(boolean enabled)
/*  985:     */   {
/*  986:2016 */     safeGetProtectionField().setInsertColumns(enabled);
/*  987:     */   }
/*  988:     */   
/*  989:     */   public void lockInsertHyperlinks(boolean enabled)
/*  990:     */   {
/*  991:2025 */     safeGetProtectionField().setInsertHyperlinks(enabled);
/*  992:     */   }
/*  993:     */   
/*  994:     */   public void lockInsertRows(boolean enabled)
/*  995:     */   {
/*  996:2034 */     safeGetProtectionField().setInsertRows(enabled);
/*  997:     */   }
/*  998:     */   
/*  999:     */   public void lockPivotTables(boolean enabled)
/* 1000:     */   {
/* 1001:2043 */     safeGetProtectionField().setPivotTables(enabled);
/* 1002:     */   }
/* 1003:     */   
/* 1004:     */   public void lockSort(boolean enabled)
/* 1005:     */   {
/* 1006:2052 */     safeGetProtectionField().setSort(enabled);
/* 1007:     */   }
/* 1008:     */   
/* 1009:     */   public void lockObjects(boolean enabled)
/* 1010:     */   {
/* 1011:2061 */     safeGetProtectionField().setObjects(enabled);
/* 1012:     */   }
/* 1013:     */   
/* 1014:     */   public void lockScenarios(boolean enabled)
/* 1015:     */   {
/* 1016:2070 */     safeGetProtectionField().setScenarios(enabled);
/* 1017:     */   }
/* 1018:     */   
/* 1019:     */   public void lockSelectLockedCells(boolean enabled)
/* 1020:     */   {
/* 1021:2079 */     safeGetProtectionField().setSelectLockedCells(enabled);
/* 1022:     */   }
/* 1023:     */   
/* 1024:     */   public void lockSelectUnlockedCells(boolean enabled)
/* 1025:     */   {
/* 1026:2088 */     safeGetProtectionField().setSelectUnlockedCells(enabled);
/* 1027:     */   }
/* 1028:     */   
/* 1029:     */   private CTSheetProtection safeGetProtectionField()
/* 1030:     */   {
/* 1031:2093 */     CTWorksheet ct = this._sh.getCTWorksheet();
/* 1032:2094 */     if (!isSheetProtectionEnabled()) {
/* 1033:2095 */       return ct.addNewSheetProtection();
/* 1034:     */     }
/* 1035:2097 */     return ct.getSheetProtection();
/* 1036:     */   }
/* 1037:     */   
/* 1038:     */   boolean isSheetProtectionEnabled()
/* 1039:     */   {
/* 1040:2101 */     CTWorksheet ct = this._sh.getCTWorksheet();
/* 1041:2102 */     return ct.isSetSheetProtection();
/* 1042:     */   }
/* 1043:     */   
/* 1044:     */   public void setTabColor(int colorIndex)
/* 1045:     */   {
/* 1046:2111 */     CTWorksheet ct = this._sh.getCTWorksheet();
/* 1047:2112 */     CTSheetPr pr = ct.getSheetPr();
/* 1048:2113 */     if (pr == null) {
/* 1049:2113 */       pr = ct.addNewSheetPr();
/* 1050:     */     }
/* 1051:2114 */     CTColor color = CTColor.Factory.newInstance();
/* 1052:2115 */     color.setIndexed(colorIndex);
/* 1053:2116 */     pr.setTabColor(color);
/* 1054:     */   }
/* 1055:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFSheet

 * JD-Core Version:    0.7.0.1

 */