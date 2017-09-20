/*    1:     */ package org.apache.poi.xssf.streaming;
/*    2:     */ 
/*    3:     */ import java.io.File;
/*    4:     */ import java.io.FileOutputStream;
/*    5:     */ import java.io.IOException;
/*    6:     */ import java.io.InputStream;
/*    7:     */ import java.io.InputStreamReader;
/*    8:     */ import java.io.OutputStream;
/*    9:     */ import java.io.OutputStreamWriter;
/*   10:     */ import java.util.Enumeration;
/*   11:     */ import java.util.HashMap;
/*   12:     */ import java.util.Iterator;
/*   13:     */ import java.util.List;
/*   14:     */ import java.util.Map;
/*   15:     */ import java.util.NoSuchElementException;
/*   16:     */ import java.util.zip.ZipEntry;
/*   17:     */ import java.util.zip.ZipFile;
/*   18:     */ import java.util.zip.ZipOutputStream;
/*   19:     */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   20:     */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   21:     */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*   22:     */ import org.apache.poi.openxml4j.util.ZipFileZipEntrySource;
/*   23:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   24:     */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   25:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   26:     */ import org.apache.poi.ss.usermodel.CreationHelper;
/*   27:     */ import org.apache.poi.ss.usermodel.DataFormat;
/*   28:     */ import org.apache.poi.ss.usermodel.Font;
/*   29:     */ import org.apache.poi.ss.usermodel.Name;
/*   30:     */ import org.apache.poi.ss.usermodel.PictureData;
/*   31:     */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*   32:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   33:     */ import org.apache.poi.ss.usermodel.SheetVisibility;
/*   34:     */ import org.apache.poi.ss.usermodel.Workbook;
/*   35:     */ import org.apache.poi.util.IOUtils;
/*   36:     */ import org.apache.poi.util.Internal;
/*   37:     */ import org.apache.poi.util.NotImplemented;
/*   38:     */ import org.apache.poi.util.POILogFactory;
/*   39:     */ import org.apache.poi.util.POILogger;
/*   40:     */ import org.apache.poi.util.Removal;
/*   41:     */ import org.apache.poi.util.TempFile;
/*   42:     */ import org.apache.poi.xssf.model.SharedStringsTable;
/*   43:     */ import org.apache.poi.xssf.usermodel.XSSFChartSheet;
/*   44:     */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*   45:     */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*   46:     */ 
/*   47:     */ public class SXSSFWorkbook
/*   48:     */   implements Workbook
/*   49:     */ {
/*   50:     */   public static final int DEFAULT_WINDOW_SIZE = 100;
/*   51:  99 */   private static final POILogger logger = POILogFactory.getLogger(SXSSFWorkbook.class);
/*   52:     */   private final XSSFWorkbook _wb;
/*   53: 103 */   private final Map<SXSSFSheet, XSSFSheet> _sxFromXHash = new HashMap();
/*   54: 104 */   private final Map<XSSFSheet, SXSSFSheet> _xFromSxHash = new HashMap();
/*   55: 106 */   private int _randomAccessWindowSize = 100;
/*   56:     */   private boolean _compressTmpFiles;
/*   57:     */   private final SharedStringsTable _sharedStringSource;
/*   58:     */   
/*   59:     */   public SXSSFWorkbook()
/*   60:     */   {
/*   61: 122 */     this(null);
/*   62:     */   }
/*   63:     */   
/*   64:     */   public SXSSFWorkbook(XSSFWorkbook workbook)
/*   65:     */   {
/*   66: 161 */     this(workbook, 100);
/*   67:     */   }
/*   68:     */   
/*   69:     */   public SXSSFWorkbook(XSSFWorkbook workbook, int rowAccessWindowSize)
/*   70:     */   {
/*   71: 186 */     this(workbook, rowAccessWindowSize, false);
/*   72:     */   }
/*   73:     */   
/*   74:     */   public SXSSFWorkbook(XSSFWorkbook workbook, int rowAccessWindowSize, boolean compressTmpFiles)
/*   75:     */   {
/*   76: 211 */     this(workbook, rowAccessWindowSize, compressTmpFiles, false);
/*   77:     */   }
/*   78:     */   
/*   79:     */   public SXSSFWorkbook(XSSFWorkbook workbook, int rowAccessWindowSize, boolean compressTmpFiles, boolean useSharedStringsTable)
/*   80:     */   {
/*   81: 238 */     setRandomAccessWindowSize(rowAccessWindowSize);
/*   82: 239 */     setCompressTempFiles(compressTmpFiles);
/*   83: 240 */     if (workbook == null)
/*   84:     */     {
/*   85: 241 */       this._wb = new XSSFWorkbook();
/*   86: 242 */       this._sharedStringSource = (useSharedStringsTable ? this._wb.getSharedStringSource() : null);
/*   87:     */     }
/*   88:     */     else
/*   89:     */     {
/*   90: 244 */       this._wb = workbook;
/*   91: 245 */       this._sharedStringSource = (useSharedStringsTable ? this._wb.getSharedStringSource() : null);
/*   92: 246 */       for (Sheet sheet : this._wb) {
/*   93: 247 */         createAndRegisterSXSSFSheet((XSSFSheet)sheet);
/*   94:     */       }
/*   95:     */     }
/*   96:     */   }
/*   97:     */   
/*   98:     */   public SXSSFWorkbook(int rowAccessWindowSize)
/*   99:     */   {
/*  100: 272 */     this(null, rowAccessWindowSize);
/*  101:     */   }
/*  102:     */   
/*  103:     */   public int getRandomAccessWindowSize()
/*  104:     */   {
/*  105: 281 */     return this._randomAccessWindowSize;
/*  106:     */   }
/*  107:     */   
/*  108:     */   private void setRandomAccessWindowSize(int rowAccessWindowSize)
/*  109:     */   {
/*  110: 285 */     if ((rowAccessWindowSize == 0) || (rowAccessWindowSize < -1)) {
/*  111: 286 */       throw new IllegalArgumentException("rowAccessWindowSize must be greater than 0 or -1");
/*  112:     */     }
/*  113: 288 */     this._randomAccessWindowSize = rowAccessWindowSize;
/*  114:     */   }
/*  115:     */   
/*  116:     */   public boolean isCompressTempFiles()
/*  117:     */   {
/*  118: 297 */     return this._compressTmpFiles;
/*  119:     */   }
/*  120:     */   
/*  121:     */   public void setCompressTempFiles(boolean compress)
/*  122:     */   {
/*  123: 317 */     this._compressTmpFiles = compress;
/*  124:     */   }
/*  125:     */   
/*  126:     */   @Internal
/*  127:     */   protected SharedStringsTable getSharedStringSource()
/*  128:     */   {
/*  129: 322 */     return this._sharedStringSource;
/*  130:     */   }
/*  131:     */   
/*  132:     */   protected SheetDataWriter createSheetDataWriter()
/*  133:     */     throws IOException
/*  134:     */   {
/*  135: 326 */     if (this._compressTmpFiles) {
/*  136: 327 */       return new GZIPSheetDataWriter(this._sharedStringSource);
/*  137:     */     }
/*  138: 330 */     return new SheetDataWriter(this._sharedStringSource);
/*  139:     */   }
/*  140:     */   
/*  141:     */   XSSFSheet getXSSFSheet(SXSSFSheet sheet)
/*  142:     */   {
/*  143: 335 */     return (XSSFSheet)this._sxFromXHash.get(sheet);
/*  144:     */   }
/*  145:     */   
/*  146:     */   SXSSFSheet getSXSSFSheet(XSSFSheet sheet)
/*  147:     */   {
/*  148: 340 */     return (SXSSFSheet)this._xFromSxHash.get(sheet);
/*  149:     */   }
/*  150:     */   
/*  151:     */   void registerSheetMapping(SXSSFSheet sxSheet, XSSFSheet xSheet)
/*  152:     */   {
/*  153: 345 */     this._sxFromXHash.put(sxSheet, xSheet);
/*  154: 346 */     this._xFromSxHash.put(xSheet, sxSheet);
/*  155:     */   }
/*  156:     */   
/*  157:     */   void deregisterSheetMapping(XSSFSheet xSheet)
/*  158:     */   {
/*  159: 351 */     SXSSFSheet sxSheet = getSXSSFSheet(xSheet);
/*  160:     */     try
/*  161:     */     {
/*  162: 355 */       sxSheet.getSheetDataWriter().close();
/*  163:     */     }
/*  164:     */     catch (IOException e) {}
/*  165: 360 */     this._sxFromXHash.remove(sxSheet);
/*  166:     */     
/*  167: 362 */     this._xFromSxHash.remove(xSheet);
/*  168:     */   }
/*  169:     */   
/*  170:     */   private XSSFSheet getSheetFromZipEntryName(String sheetRef)
/*  171:     */   {
/*  172: 367 */     for (XSSFSheet sheet : this._sxFromXHash.values()) {
/*  173: 369 */       if (sheetRef.equals(sheet.getPackagePart().getPartName().getName().substring(1))) {
/*  174: 370 */         return sheet;
/*  175:     */       }
/*  176:     */     }
/*  177: 373 */     return null;
/*  178:     */   }
/*  179:     */   
/*  180:     */   protected void injectData(ZipEntrySource zipEntrySource, OutputStream out)
/*  181:     */     throws IOException
/*  182:     */   {
/*  183:     */     try
/*  184:     */     {
/*  185: 378 */       ZipOutputStream zos = new ZipOutputStream(out);
/*  186:     */       try
/*  187:     */       {
/*  188: 380 */         Enumeration<? extends ZipEntry> en = zipEntrySource.getEntries();
/*  189: 381 */         while (en.hasMoreElements())
/*  190:     */         {
/*  191: 382 */           ZipEntry ze = (ZipEntry)en.nextElement();
/*  192: 383 */           zos.putNextEntry(new ZipEntry(ze.getName()));
/*  193: 384 */           InputStream is = zipEntrySource.getInputStream(ze);
/*  194: 385 */           XSSFSheet xSheet = getSheetFromZipEntryName(ze.getName());
/*  195: 387 */           if ((xSheet != null) && (!(xSheet instanceof XSSFChartSheet)))
/*  196:     */           {
/*  197: 388 */             SXSSFSheet sxSheet = getSXSSFSheet(xSheet);
/*  198: 389 */             InputStream xis = sxSheet.getWorksheetXMLInputStream();
/*  199:     */             try
/*  200:     */             {
/*  201: 391 */               copyStreamAndInjectWorksheet(is, zos, xis);
/*  202:     */             }
/*  203:     */             finally {}
/*  204:     */           }
/*  205:     */           else
/*  206:     */           {
/*  207: 396 */             IOUtils.copy(is, zos);
/*  208:     */           }
/*  209: 398 */           is.close();
/*  210:     */         }
/*  211:     */       }
/*  212:     */       finally
/*  213:     */       {
/*  214: 401 */         zos.close();
/*  215:     */       }
/*  216:     */     }
/*  217:     */     finally
/*  218:     */     {
/*  219: 404 */       zipEntrySource.close();
/*  220:     */     }
/*  221:     */   }
/*  222:     */   
/*  223:     */   private static void copyStreamAndInjectWorksheet(InputStream in, OutputStream out, InputStream worksheetData)
/*  224:     */     throws IOException
/*  225:     */   {
/*  226: 409 */     InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
/*  227: 410 */     OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
/*  228: 411 */     boolean needsStartTag = true;
/*  229:     */     
/*  230: 413 */     int pos = 0;
/*  231: 414 */     String s = "<sheetData";
/*  232: 415 */     int n = s.length();
/*  233:     */     int c;
/*  234: 417 */     while ((c = inReader.read()) != -1) {
/*  235: 419 */       if (c == s.charAt(pos))
/*  236:     */       {
/*  237: 421 */         pos++;
/*  238: 422 */         if (pos == n)
/*  239:     */         {
/*  240: 424 */           if (!"<sheetData".equals(s)) {
/*  241:     */             break;
/*  242:     */           }
/*  243: 426 */           c = inReader.read();
/*  244: 427 */           if (c == -1)
/*  245:     */           {
/*  246: 429 */             outWriter.write(s);
/*  247: 430 */             break;
/*  248:     */           }
/*  249: 432 */           if (c == 62)
/*  250:     */           {
/*  251: 435 */             outWriter.write(s);
/*  252: 436 */             outWriter.write(c);
/*  253: 437 */             s = "</sheetData>";
/*  254: 438 */             n = s.length();
/*  255: 439 */             pos = 0;
/*  256: 440 */             needsStartTag = false;
/*  257:     */           }
/*  258: 443 */           else if (c == 47)
/*  259:     */           {
/*  260: 446 */             c = inReader.read();
/*  261: 447 */             if (c == -1)
/*  262:     */             {
/*  263: 449 */               outWriter.write(s);
/*  264: 450 */               break;
/*  265:     */             }
/*  266: 452 */             if (c == 62) {
/*  267:     */               break;
/*  268:     */             }
/*  269: 458 */             outWriter.write(s);
/*  270: 459 */             outWriter.write(47);
/*  271: 460 */             outWriter.write(c);
/*  272: 461 */             pos = 0;
/*  273:     */           }
/*  274:     */           else
/*  275:     */           {
/*  276: 465 */             outWriter.write(s);
/*  277: 466 */             outWriter.write(47);
/*  278: 467 */             outWriter.write(c);
/*  279: 468 */             pos = 0;
/*  280:     */           }
/*  281:     */         }
/*  282:     */       }
/*  283:     */       else
/*  284:     */       {
/*  285: 480 */         if (pos > 0) {
/*  286: 481 */           outWriter.write(s, 0, pos);
/*  287:     */         }
/*  288: 483 */         if (c == s.charAt(0))
/*  289:     */         {
/*  290: 485 */           pos = 1;
/*  291:     */         }
/*  292:     */         else
/*  293:     */         {
/*  294: 489 */           outWriter.write(c);
/*  295: 490 */           pos = 0;
/*  296:     */         }
/*  297:     */       }
/*  298:     */     }
/*  299: 494 */     outWriter.flush();
/*  300: 495 */     if (needsStartTag)
/*  301:     */     {
/*  302: 497 */       outWriter.write("<sheetData>\n");
/*  303: 498 */       outWriter.flush();
/*  304:     */     }
/*  305: 501 */     IOUtils.copy(worksheetData, out);
/*  306: 502 */     outWriter.write("</sheetData>");
/*  307: 503 */     outWriter.flush();
/*  308: 505 */     while ((c = inReader.read()) != -1) {
/*  309: 506 */       outWriter.write(c);
/*  310:     */     }
/*  311: 508 */     outWriter.flush();
/*  312:     */   }
/*  313:     */   
/*  314:     */   public XSSFWorkbook getXSSFWorkbook()
/*  315:     */   {
/*  316: 513 */     return this._wb;
/*  317:     */   }
/*  318:     */   
/*  319:     */   public int getActiveSheetIndex()
/*  320:     */   {
/*  321: 528 */     return this._wb.getActiveSheetIndex();
/*  322:     */   }
/*  323:     */   
/*  324:     */   public void setActiveSheet(int sheetIndex)
/*  325:     */   {
/*  326: 541 */     this._wb.setActiveSheet(sheetIndex);
/*  327:     */   }
/*  328:     */   
/*  329:     */   public int getFirstVisibleTab()
/*  330:     */   {
/*  331: 552 */     return this._wb.getFirstVisibleTab();
/*  332:     */   }
/*  333:     */   
/*  334:     */   public void setFirstVisibleTab(int sheetIndex)
/*  335:     */   {
/*  336: 563 */     this._wb.setFirstVisibleTab(sheetIndex);
/*  337:     */   }
/*  338:     */   
/*  339:     */   public void setSheetOrder(String sheetname, int pos)
/*  340:     */   {
/*  341: 575 */     this._wb.setSheetOrder(sheetname, pos);
/*  342:     */   }
/*  343:     */   
/*  344:     */   public void setSelectedTab(int index)
/*  345:     */   {
/*  346: 590 */     this._wb.setSelectedTab(index);
/*  347:     */   }
/*  348:     */   
/*  349:     */   public void setSheetName(int sheet, String name)
/*  350:     */   {
/*  351: 602 */     this._wb.setSheetName(sheet, name);
/*  352:     */   }
/*  353:     */   
/*  354:     */   public String getSheetName(int sheet)
/*  355:     */   {
/*  356: 614 */     return this._wb.getSheetName(sheet);
/*  357:     */   }
/*  358:     */   
/*  359:     */   public int getSheetIndex(String name)
/*  360:     */   {
/*  361: 626 */     return this._wb.getSheetIndex(name);
/*  362:     */   }
/*  363:     */   
/*  364:     */   public int getSheetIndex(Sheet sheet)
/*  365:     */   {
/*  366: 638 */     return this._wb.getSheetIndex(getXSSFSheet((SXSSFSheet)sheet));
/*  367:     */   }
/*  368:     */   
/*  369:     */   public SXSSFSheet createSheet()
/*  370:     */   {
/*  371: 650 */     return createAndRegisterSXSSFSheet(this._wb.createSheet());
/*  372:     */   }
/*  373:     */   
/*  374:     */   SXSSFSheet createAndRegisterSXSSFSheet(XSSFSheet xSheet)
/*  375:     */   {
/*  376:     */     SXSSFSheet sxSheet;
/*  377:     */     try
/*  378:     */     {
/*  379: 658 */       sxSheet = new SXSSFSheet(this, xSheet);
/*  380:     */     }
/*  381:     */     catch (IOException ioe)
/*  382:     */     {
/*  383: 662 */       throw new RuntimeException(ioe);
/*  384:     */     }
/*  385: 664 */     registerSheetMapping(sxSheet, xSheet);
/*  386: 665 */     return sxSheet;
/*  387:     */   }
/*  388:     */   
/*  389:     */   public SXSSFSheet createSheet(String sheetname)
/*  390:     */   {
/*  391: 679 */     return createAndRegisterSXSSFSheet(this._wb.createSheet(sheetname));
/*  392:     */   }
/*  393:     */   
/*  394:     */   @NotImplemented
/*  395:     */   public Sheet cloneSheet(int sheetNum)
/*  396:     */   {
/*  397: 693 */     throw new RuntimeException("NotImplemented");
/*  398:     */   }
/*  399:     */   
/*  400:     */   public int getNumberOfSheets()
/*  401:     */   {
/*  402: 705 */     return this._wb.getNumberOfSheets();
/*  403:     */   }
/*  404:     */   
/*  405:     */   public Iterator<Sheet> sheetIterator()
/*  406:     */   {
/*  407: 716 */     return new SheetIterator();
/*  408:     */   }
/*  409:     */   
/*  410:     */   private final class SheetIterator<T extends Sheet>
/*  411:     */     implements Iterator<T>
/*  412:     */   {
/*  413:     */     private final Iterator<XSSFSheet> it;
/*  414:     */     
/*  415:     */     public SheetIterator()
/*  416:     */     {
/*  417: 723 */       this.it = SXSSFWorkbook.this._wb.iterator();
/*  418:     */     }
/*  419:     */     
/*  420:     */     public boolean hasNext()
/*  421:     */     {
/*  422: 727 */       return this.it.hasNext();
/*  423:     */     }
/*  424:     */     
/*  425:     */     public T next()
/*  426:     */       throws NoSuchElementException
/*  427:     */     {
/*  428: 732 */       XSSFSheet xssfSheet = (XSSFSheet)this.it.next();
/*  429: 733 */       return SXSSFWorkbook.this.getSXSSFSheet(xssfSheet);
/*  430:     */     }
/*  431:     */     
/*  432:     */     public void remove()
/*  433:     */       throws IllegalStateException
/*  434:     */     {
/*  435: 742 */       throw new UnsupportedOperationException("remove method not supported on XSSFWorkbook.iterator(). Use Sheet.removeSheetAt(int) instead.");
/*  436:     */     }
/*  437:     */   }
/*  438:     */   
/*  439:     */   public Iterator<Sheet> iterator()
/*  440:     */   {
/*  441: 753 */     return sheetIterator();
/*  442:     */   }
/*  443:     */   
/*  444:     */   public SXSSFSheet getSheetAt(int index)
/*  445:     */   {
/*  446: 765 */     return getSXSSFSheet(this._wb.getSheetAt(index));
/*  447:     */   }
/*  448:     */   
/*  449:     */   public SXSSFSheet getSheet(String name)
/*  450:     */   {
/*  451: 777 */     return getSXSSFSheet(this._wb.getSheet(name));
/*  452:     */   }
/*  453:     */   
/*  454:     */   public void removeSheetAt(int index)
/*  455:     */   {
/*  456: 789 */     XSSFSheet xSheet = this._wb.getSheetAt(index);
/*  457: 790 */     SXSSFSheet sxSheet = getSXSSFSheet(xSheet);
/*  458:     */     
/*  459:     */ 
/*  460: 793 */     this._wb.removeSheetAt(index);
/*  461: 794 */     deregisterSheetMapping(xSheet);
/*  462:     */     try
/*  463:     */     {
/*  464: 798 */       sxSheet.dispose();
/*  465:     */     }
/*  466:     */     catch (IOException e)
/*  467:     */     {
/*  468: 800 */       logger.log(5, new Object[] { e });
/*  469:     */     }
/*  470:     */   }
/*  471:     */   
/*  472:     */   public Font createFont()
/*  473:     */   {
/*  474: 812 */     return this._wb.createFont();
/*  475:     */   }
/*  476:     */   
/*  477:     */   public Font findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline)
/*  478:     */   {
/*  479: 823 */     return this._wb.findFont(bold, color, fontHeight, name, italic, strikeout, typeOffset, underline);
/*  480:     */   }
/*  481:     */   
/*  482:     */   public short getNumberOfFonts()
/*  483:     */   {
/*  484: 835 */     return this._wb.getNumberOfFonts();
/*  485:     */   }
/*  486:     */   
/*  487:     */   public Font getFontAt(short idx)
/*  488:     */   {
/*  489: 847 */     return this._wb.getFontAt(idx);
/*  490:     */   }
/*  491:     */   
/*  492:     */   public CellStyle createCellStyle()
/*  493:     */   {
/*  494: 858 */     return this._wb.createCellStyle();
/*  495:     */   }
/*  496:     */   
/*  497:     */   public int getNumCellStyles()
/*  498:     */   {
/*  499: 869 */     return this._wb.getNumCellStyles();
/*  500:     */   }
/*  501:     */   
/*  502:     */   public CellStyle getCellStyleAt(int idx)
/*  503:     */   {
/*  504: 881 */     return this._wb.getCellStyleAt(idx);
/*  505:     */   }
/*  506:     */   
/*  507:     */   public void close()
/*  508:     */     throws IOException
/*  509:     */   {
/*  510: 895 */     for (SXSSFSheet sheet : this._xFromSxHash.values()) {
/*  511:     */       try
/*  512:     */       {
/*  513: 898 */         sheet.getSheetDataWriter().close();
/*  514:     */       }
/*  515:     */       catch (IOException e)
/*  516:     */       {
/*  517: 900 */         logger.log(5, new Object[] { "An exception occurred while closing sheet data writer for sheet " + sheet.getSheetName() + ".", e });
/*  518:     */       }
/*  519:     */     }
/*  520: 909 */     this._wb.close();
/*  521:     */   }
/*  522:     */   
/*  523:     */   public void write(OutputStream stream)
/*  524:     */     throws IOException
/*  525:     */   {
/*  526: 921 */     flushSheets();
/*  527:     */     
/*  528:     */ 
/*  529: 924 */     File tmplFile = TempFile.createTempFile("poi-sxssf-template", ".xlsx");
/*  530:     */     boolean deleted;
/*  531:     */     try
/*  532:     */     {
/*  533: 927 */       FileOutputStream os = new FileOutputStream(tmplFile);
/*  534:     */       try
/*  535:     */       {
/*  536: 929 */         this._wb.write(os);
/*  537:     */       }
/*  538:     */       finally
/*  539:     */       {
/*  540: 931 */         os.close();
/*  541:     */       }
/*  542: 935 */       Object source = new ZipFileZipEntrySource(new ZipFile(tmplFile));
/*  543: 936 */       injectData((ZipEntrySource)source, stream);
/*  544:     */     }
/*  545:     */     finally
/*  546:     */     {
/*  547: 938 */       deleted = tmplFile.delete();
/*  548:     */     }
/*  549: 940 */     if (!deleted) {
/*  550: 941 */       throw new IOException("Could not delete temporary file after processing: " + tmplFile);
/*  551:     */     }
/*  552:     */   }
/*  553:     */   
/*  554:     */   protected void flushSheets()
/*  555:     */     throws IOException
/*  556:     */   {
/*  557: 946 */     for (SXSSFSheet sheet : this._xFromSxHash.values()) {
/*  558: 948 */       sheet.flushRows();
/*  559:     */     }
/*  560:     */   }
/*  561:     */   
/*  562:     */   public boolean dispose()
/*  563:     */   {
/*  564: 959 */     boolean success = true;
/*  565: 960 */     for (SXSSFSheet sheet : this._sxFromXHash.keySet()) {
/*  566:     */       try
/*  567:     */       {
/*  568: 963 */         success = (sheet.dispose()) && (success);
/*  569:     */       }
/*  570:     */       catch (IOException e)
/*  571:     */       {
/*  572: 965 */         logger.log(5, new Object[] { e });
/*  573: 966 */         success = false;
/*  574:     */       }
/*  575:     */     }
/*  576: 969 */     return success;
/*  577:     */   }
/*  578:     */   
/*  579:     */   public int getNumberOfNames()
/*  580:     */   {
/*  581: 978 */     return this._wb.getNumberOfNames();
/*  582:     */   }
/*  583:     */   
/*  584:     */   public Name getName(String name)
/*  585:     */   {
/*  586: 988 */     return this._wb.getName(name);
/*  587:     */   }
/*  588:     */   
/*  589:     */   public List<? extends Name> getNames(String name)
/*  590:     */   {
/*  591: 999 */     return this._wb.getNames(name);
/*  592:     */   }
/*  593:     */   
/*  594:     */   public List<? extends Name> getAllNames()
/*  595:     */   {
/*  596:1010 */     return this._wb.getAllNames();
/*  597:     */   }
/*  598:     */   
/*  599:     */   @Deprecated
/*  600:     */   @Removal(version="3.18")
/*  601:     */   public Name getNameAt(int nameIndex)
/*  602:     */   {
/*  603:1024 */     return this._wb.getNameAt(nameIndex);
/*  604:     */   }
/*  605:     */   
/*  606:     */   public Name createName()
/*  607:     */   {
/*  608:1035 */     return this._wb.createName();
/*  609:     */   }
/*  610:     */   
/*  611:     */   @Deprecated
/*  612:     */   @Removal(version="3.18")
/*  613:     */   public int getNameIndex(String name)
/*  614:     */   {
/*  615:1055 */     return this._wb.getNameIndex(name);
/*  616:     */   }
/*  617:     */   
/*  618:     */   @Deprecated
/*  619:     */   @Removal(version="3.18")
/*  620:     */   public void removeName(int index)
/*  621:     */   {
/*  622:1070 */     this._wb.removeName(index);
/*  623:     */   }
/*  624:     */   
/*  625:     */   @Deprecated
/*  626:     */   @Removal(version="3.18")
/*  627:     */   public void removeName(String name)
/*  628:     */   {
/*  629:1085 */     this._wb.removeName(name);
/*  630:     */   }
/*  631:     */   
/*  632:     */   public void removeName(Name name)
/*  633:     */   {
/*  634:1096 */     this._wb.removeName(name);
/*  635:     */   }
/*  636:     */   
/*  637:     */   public void setPrintArea(int sheetIndex, String reference)
/*  638:     */   {
/*  639:1109 */     this._wb.setPrintArea(sheetIndex, reference);
/*  640:     */   }
/*  641:     */   
/*  642:     */   public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)
/*  643:     */   {
/*  644:1124 */     this._wb.setPrintArea(sheetIndex, startColumn, endColumn, startRow, endRow);
/*  645:     */   }
/*  646:     */   
/*  647:     */   public String getPrintArea(int sheetIndex)
/*  648:     */   {
/*  649:1137 */     return this._wb.getPrintArea(sheetIndex);
/*  650:     */   }
/*  651:     */   
/*  652:     */   public void removePrintArea(int sheetIndex)
/*  653:     */   {
/*  654:1148 */     this._wb.removePrintArea(sheetIndex);
/*  655:     */   }
/*  656:     */   
/*  657:     */   public Row.MissingCellPolicy getMissingCellPolicy()
/*  658:     */   {
/*  659:1162 */     return this._wb.getMissingCellPolicy();
/*  660:     */   }
/*  661:     */   
/*  662:     */   public void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy)
/*  663:     */   {
/*  664:1176 */     this._wb.setMissingCellPolicy(missingCellPolicy);
/*  665:     */   }
/*  666:     */   
/*  667:     */   public DataFormat createDataFormat()
/*  668:     */   {
/*  669:1187 */     return this._wb.createDataFormat();
/*  670:     */   }
/*  671:     */   
/*  672:     */   public int addPicture(byte[] pictureData, int format)
/*  673:     */   {
/*  674:1207 */     return this._wb.addPicture(pictureData, format);
/*  675:     */   }
/*  676:     */   
/*  677:     */   public List<? extends PictureData> getAllPictures()
/*  678:     */   {
/*  679:1218 */     return this._wb.getAllPictures();
/*  680:     */   }
/*  681:     */   
/*  682:     */   public CreationHelper getCreationHelper()
/*  683:     */   {
/*  684:1228 */     return new SXSSFCreationHelper(this);
/*  685:     */   }
/*  686:     */   
/*  687:     */   protected boolean isDate1904()
/*  688:     */   {
/*  689:1232 */     return this._wb.isDate1904();
/*  690:     */   }
/*  691:     */   
/*  692:     */   @NotImplemented("XSSFWorkbook#isHidden is not implemented")
/*  693:     */   public boolean isHidden()
/*  694:     */   {
/*  695:1239 */     return this._wb.isHidden();
/*  696:     */   }
/*  697:     */   
/*  698:     */   @NotImplemented("XSSFWorkbook#setHidden is not implemented")
/*  699:     */   public void setHidden(boolean hiddenFlag)
/*  700:     */   {
/*  701:1246 */     this._wb.setHidden(hiddenFlag);
/*  702:     */   }
/*  703:     */   
/*  704:     */   public boolean isSheetHidden(int sheetIx)
/*  705:     */   {
/*  706:1252 */     return this._wb.isSheetHidden(sheetIx);
/*  707:     */   }
/*  708:     */   
/*  709:     */   public boolean isSheetVeryHidden(int sheetIx)
/*  710:     */   {
/*  711:1258 */     return this._wb.isSheetVeryHidden(sheetIx);
/*  712:     */   }
/*  713:     */   
/*  714:     */   public SheetVisibility getSheetVisibility(int sheetIx)
/*  715:     */   {
/*  716:1263 */     return this._wb.getSheetVisibility(sheetIx);
/*  717:     */   }
/*  718:     */   
/*  719:     */   public void setSheetHidden(int sheetIx, boolean hidden)
/*  720:     */   {
/*  721:1269 */     this._wb.setSheetHidden(sheetIx, hidden);
/*  722:     */   }
/*  723:     */   
/*  724:     */   @Removal(version="3.18")
/*  725:     */   @Deprecated
/*  726:     */   public void setSheetHidden(int sheetIx, int hidden)
/*  727:     */   {
/*  728:1277 */     this._wb.setSheetHidden(sheetIx, hidden);
/*  729:     */   }
/*  730:     */   
/*  731:     */   public void setSheetVisibility(int sheetIx, SheetVisibility visibility)
/*  732:     */   {
/*  733:1282 */     this._wb.setSheetVisibility(sheetIx, visibility);
/*  734:     */   }
/*  735:     */   
/*  736:     */   @NotImplemented
/*  737:     */   public int linkExternalWorkbook(String name, Workbook workbook)
/*  738:     */   {
/*  739:1303 */     throw new RuntimeException("NotImplemented");
/*  740:     */   }
/*  741:     */   
/*  742:     */   public void addToolPack(UDFFinder toopack)
/*  743:     */   {
/*  744:1314 */     this._wb.addToolPack(toopack);
/*  745:     */   }
/*  746:     */   
/*  747:     */   public void setForceFormulaRecalculation(boolean value)
/*  748:     */   {
/*  749:1331 */     this._wb.setForceFormulaRecalculation(value);
/*  750:     */   }
/*  751:     */   
/*  752:     */   public boolean getForceFormulaRecalculation()
/*  753:     */   {
/*  754:1339 */     return this._wb.getForceFormulaRecalculation();
/*  755:     */   }
/*  756:     */   
/*  757:     */   public SpreadsheetVersion getSpreadsheetVersion()
/*  758:     */   {
/*  759:1350 */     return SpreadsheetVersion.EXCEL2007;
/*  760:     */   }
/*  761:     */   
/*  762:     */   public int addOlePackage(byte[] oleData, String label, String fileName, String command)
/*  763:     */     throws IOException
/*  764:     */   {
/*  765:1355 */     return this._wb.addOlePackage(oleData, label, fileName, command);
/*  766:     */   }
/*  767:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFWorkbook
 * JD-Core Version:    0.7.0.1
 */