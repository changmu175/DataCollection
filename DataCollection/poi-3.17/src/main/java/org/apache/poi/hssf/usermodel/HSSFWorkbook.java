/*    1:     */ package org.apache.poi.hssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayInputStream;
/*    4:     */ import java.io.ByteArrayOutputStream;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileNotFoundException;
/*    7:     */ import java.io.IOException;
/*    8:     */ import java.io.InputStream;
/*    9:     */ import java.io.OutputStream;
/*   10:     */ import java.io.OutputStreamWriter;
/*   11:     */ import java.io.PrintStream;
/*   12:     */ import java.io.PrintWriter;
/*   13:     */ import java.nio.charset.Charset;
/*   14:     */ import java.security.GeneralSecurityException;
/*   15:     */ import java.util.ArrayList;
/*   16:     */ import java.util.Arrays;
/*   17:     */ import java.util.Collection;
/*   18:     */ import java.util.Collections;
/*   19:     */ import java.util.HashMap;
/*   20:     */ import java.util.HashSet;
/*   21:     */ import java.util.Iterator;
/*   22:     */ import java.util.List;
/*   23:     */ import java.util.Map;
/*   24:     */ import java.util.Map.Entry;
/*   25:     */ import java.util.NoSuchElementException;
/*   26:     */ import java.util.Set;
/*   27:     */ import java.util.regex.Pattern;
/*   28:     */ import org.apache.commons.codec.digest.DigestUtils;
/*   29:     */ import org.apache.poi.EncryptedDocumentException;
/*   30:     */ import org.apache.poi.POIDocument;
/*   31:     */ import org.apache.poi.ddf.EscherBSERecord;
/*   32:     */ import org.apache.poi.ddf.EscherBitmapBlip;
/*   33:     */ import org.apache.poi.ddf.EscherBlipRecord;
/*   34:     */ import org.apache.poi.ddf.EscherMetafileBlip;
/*   35:     */ import org.apache.poi.ddf.EscherRecord;
/*   36:     */ import org.apache.poi.hpsf.ClassID;
/*   37:     */ import org.apache.poi.hssf.OldExcelFormatException;
/*   38:     */ import org.apache.poi.hssf.model.DrawingManager2;
/*   39:     */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   40:     */ import org.apache.poi.hssf.model.InternalSheet;
/*   41:     */ import org.apache.poi.hssf.model.InternalSheet.UnsupportedBOFType;
/*   42:     */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   43:     */ import org.apache.poi.hssf.model.RecordStream;
/*   44:     */ import org.apache.poi.hssf.model.WorkbookRecordList;
/*   45:     */ import org.apache.poi.hssf.record.AbstractEscherHolderRecord;
/*   46:     */ import org.apache.poi.hssf.record.BackupRecord;
/*   47:     */ import org.apache.poi.hssf.record.DrawingGroupRecord;
/*   48:     */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   49:     */ import org.apache.poi.hssf.record.FilePassRecord;
/*   50:     */ import org.apache.poi.hssf.record.FontRecord;
/*   51:     */ import org.apache.poi.hssf.record.LabelRecord;
/*   52:     */ import org.apache.poi.hssf.record.LabelSSTRecord;
/*   53:     */ import org.apache.poi.hssf.record.NameRecord;
/*   54:     */ import org.apache.poi.hssf.record.RecalcIdRecord;
/*   55:     */ import org.apache.poi.hssf.record.Record;
/*   56:     */ import org.apache.poi.hssf.record.RecordFactory;
/*   57:     */ import org.apache.poi.hssf.record.UnknownRecord;
/*   58:     */ import org.apache.poi.hssf.record.WindowOneRecord;
/*   59:     */ import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
/*   60:     */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   61:     */ import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;
/*   62:     */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*   63:     */ import org.apache.poi.hssf.util.CellReference;
/*   64:     */ import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
/*   65:     */ import org.apache.poi.poifs.crypt.Decryptor;
/*   66:     */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*   67:     */ import org.apache.poi.poifs.crypt.EncryptionMode;
/*   68:     */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*   69:     */ import org.apache.poi.poifs.crypt.Encryptor;
/*   70:     */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*   71:     */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*   72:     */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*   73:     */ import org.apache.poi.poifs.filesystem.EntryUtils;
/*   74:     */ import org.apache.poi.poifs.filesystem.FilteringDirectoryNode;
/*   75:     */ import org.apache.poi.poifs.filesystem.NPOIFSDocument;
/*   76:     */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*   77:     */ import org.apache.poi.poifs.filesystem.Ole10Native;
/*   78:     */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*   79:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   80:     */ import org.apache.poi.ss.formula.FormulaShifter;
/*   81:     */ import org.apache.poi.ss.formula.FormulaType;
/*   82:     */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   83:     */ import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
/*   84:     */ import org.apache.poi.ss.formula.udf.IndexedUDFFinder;
/*   85:     */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   86:     */ import org.apache.poi.ss.usermodel.Name;
/*   87:     */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*   88:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   89:     */ import org.apache.poi.ss.usermodel.SheetVisibility;
/*   90:     */ import org.apache.poi.ss.usermodel.Workbook;
/*   91:     */ import org.apache.poi.util.Configurator;
/*   92:     */ import org.apache.poi.util.HexDump;
/*   93:     */ import org.apache.poi.util.Internal;
/*   94:     */ import org.apache.poi.util.LittleEndian;
/*   95:     */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   96:     */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   97:     */ import org.apache.poi.util.POILogFactory;
/*   98:     */ import org.apache.poi.util.POILogger;
/*   99:     */ import org.apache.poi.util.Removal;
/*  100:     */ 
/*  101:     */ public final class HSSFWorkbook
/*  102:     */   extends POIDocument
/*  103:     */   implements Workbook
/*  104:     */ {
/*  105: 132 */   private static final Pattern COMMA_PATTERN = Pattern.compile(",");
/*  106:     */   private static final int MAX_STYLES = 4030;
/*  107:     */   private static final int DEBUG = 1;
/*  108: 152 */   public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFWorkbook.SheetInitialCapacity", 3);
/*  109:     */   private InternalWorkbook workbook;
/*  110:     */   protected List<HSSFSheet> _sheets;
/*  111:     */   private ArrayList<HSSFName> names;
/*  112:     */   private Map<Short, HSSFFont> fonts;
/*  113:     */   private boolean preserveNodes;
/*  114:     */   private HSSFDataFormat formatter;
/*  115: 197 */   private Row.MissingCellPolicy missingCellPolicy = Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;
/*  116: 199 */   private static final POILogger log = POILogFactory.getLogger(HSSFWorkbook.class);
/*  117: 205 */   private UDFFinder _udfFinder = new IndexedUDFFinder(new UDFFinder[] { AggregatingUDFFinder.DEFAULT });
/*  118:     */   
/*  119:     */   public static HSSFWorkbook create(InternalWorkbook book)
/*  120:     */   {
/*  121: 208 */     return new HSSFWorkbook(book);
/*  122:     */   }
/*  123:     */   
/*  124:     */   public HSSFWorkbook()
/*  125:     */   {
/*  126: 215 */     this(InternalWorkbook.createWorkbook());
/*  127:     */   }
/*  128:     */   
/*  129:     */   private HSSFWorkbook(InternalWorkbook book)
/*  130:     */   {
/*  131: 219 */     super((DirectoryNode)null);
/*  132: 220 */     this.workbook = book;
/*  133: 221 */     this._sheets = new ArrayList(INITIAL_CAPACITY);
/*  134: 222 */     this.names = new ArrayList(INITIAL_CAPACITY);
/*  135:     */   }
/*  136:     */   
/*  137:     */   public HSSFWorkbook(POIFSFileSystem fs)
/*  138:     */     throws IOException
/*  139:     */   {
/*  140: 236 */     this(fs, true);
/*  141:     */   }
/*  142:     */   
/*  143:     */   public HSSFWorkbook(NPOIFSFileSystem fs)
/*  144:     */     throws IOException
/*  145:     */   {
/*  146: 249 */     this(fs.getRoot(), true);
/*  147:     */   }
/*  148:     */   
/*  149:     */   public HSSFWorkbook(POIFSFileSystem fs, boolean preserveNodes)
/*  150:     */     throws IOException
/*  151:     */   {
/*  152: 266 */     this(fs.getRoot(), fs, preserveNodes);
/*  153:     */   }
/*  154:     */   
/*  155:     */   public static String getWorkbookDirEntryName(DirectoryNode directory)
/*  156:     */   {
/*  157: 271 */     for (String wbName : InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES) {
/*  158:     */       try
/*  159:     */       {
/*  160: 273 */         directory.getEntry(wbName);
/*  161: 274 */         return wbName;
/*  162:     */       }
/*  163:     */       catch (FileNotFoundException e) {}
/*  164:     */     }
/*  165:     */     try
/*  166:     */     {
/*  167: 282 */       directory.getEntry("EncryptedPackage");
/*  168: 283 */       throw new EncryptedDocumentException("The supplied spreadsheet seems to be an Encrypted .xlsx file. It must be decrypted before use by XSSF, it cannot be used by HSSF");
/*  169:     */     }
/*  170:     */     catch (FileNotFoundException e)
/*  171:     */     {
/*  172:     */       try
/*  173:     */       {
/*  174: 291 */         directory.getEntry("Book");
/*  175: 292 */         throw new OldExcelFormatException("The supplied spreadsheet seems to be Excel 5.0/7.0 (BIFF5) format. POI only supports BIFF8 format (from Excel versions 97/2000/XP/2003)");
/*  176:     */       }
/*  177:     */       catch (FileNotFoundException e)
/*  178:     */       {
/*  179: 298 */         throw new IllegalArgumentException("The supplied POIFSFileSystem does not contain a BIFF8 'Workbook' entry. Is it really an excel file? Had: " + directory.getEntryNames());
/*  180:     */       }
/*  181:     */     }
/*  182:     */   }
/*  183:     */   
/*  184:     */   public HSSFWorkbook(DirectoryNode directory, POIFSFileSystem fs, boolean preserveNodes)
/*  185:     */     throws IOException
/*  186:     */   {
/*  187: 319 */     this(directory, preserveNodes);
/*  188:     */   }
/*  189:     */   
/*  190:     */   public HSSFWorkbook(DirectoryNode directory, boolean preserveNodes)
/*  191:     */     throws IOException
/*  192:     */   {
/*  193: 338 */     super(directory);
/*  194: 339 */     String workbookName = getWorkbookDirEntryName(directory);
/*  195:     */     
/*  196: 341 */     this.preserveNodes = preserveNodes;
/*  197: 345 */     if (!preserveNodes) {
/*  198: 346 */       clearDirectory();
/*  199:     */     }
/*  200: 349 */     this._sheets = new ArrayList(INITIAL_CAPACITY);
/*  201: 350 */     this.names = new ArrayList(INITIAL_CAPACITY);
/*  202:     */     
/*  203:     */ 
/*  204:     */ 
/*  205: 354 */     InputStream stream = directory.createDocumentInputStream(workbookName);
/*  206:     */     
/*  207: 356 */     List<Record> records = RecordFactory.createRecords(stream);
/*  208:     */     
/*  209: 358 */     this.workbook = InternalWorkbook.createWorkbook(records);
/*  210: 359 */     setPropertiesFromWorkbook(this.workbook);
/*  211: 360 */     int recOffset = this.workbook.getNumRecords();
/*  212:     */     
/*  213:     */ 
/*  214: 363 */     convertLabelRecords(records, recOffset);
/*  215: 364 */     RecordStream rs = new RecordStream(records, recOffset);
/*  216: 365 */     while (rs.hasNext()) {
/*  217:     */       try
/*  218:     */       {
/*  219: 367 */         InternalSheet sheet = InternalSheet.createSheet(rs);
/*  220: 368 */         this._sheets.add(new HSSFSheet(this, sheet));
/*  221:     */       }
/*  222:     */       catch (UnsupportedBOFType eb)
/*  223:     */       {
/*  224: 371 */         log.log(5, new Object[] { "Unsupported BOF found of type " + eb.getType() });
/*  225:     */       }
/*  226:     */     }
/*  227: 375 */     for (int i = 0; i < this.workbook.getNumNames(); i++)
/*  228:     */     {
/*  229: 376 */       NameRecord nameRecord = this.workbook.getNameRecord(i);
/*  230: 377 */       HSSFName name = new HSSFName(this, nameRecord, this.workbook.getNameCommentRecord(nameRecord));
/*  231: 378 */       this.names.add(name);
/*  232:     */     }
/*  233:     */   }
/*  234:     */   
/*  235:     */   public HSSFWorkbook(InputStream s)
/*  236:     */     throws IOException
/*  237:     */   {
/*  238: 394 */     this(s, true);
/*  239:     */   }
/*  240:     */   
/*  241:     */   public HSSFWorkbook(InputStream s, boolean preserveNodes)
/*  242:     */     throws IOException
/*  243:     */   {
/*  244: 413 */     this(new NPOIFSFileSystem(s).getRoot(), preserveNodes);
/*  245:     */   }
/*  246:     */   
/*  247:     */   private void setPropertiesFromWorkbook(InternalWorkbook book)
/*  248:     */   {
/*  249: 422 */     this.workbook = book;
/*  250:     */   }
/*  251:     */   
/*  252:     */   private void convertLabelRecords(List<Record> records, int offset)
/*  253:     */   {
/*  254: 446 */     if (log.check(1)) {
/*  255: 447 */       log.log(1, new Object[] { "convertLabelRecords called" });
/*  256:     */     }
/*  257: 449 */     for (int k = offset; k < records.size(); k++)
/*  258:     */     {
/*  259: 451 */       Record rec = (Record)records.get(k);
/*  260: 453 */       if (rec.getSid() == 516)
/*  261:     */       {
/*  262: 455 */         LabelRecord oldrec = (LabelRecord)rec;
/*  263:     */         
/*  264: 457 */         records.remove(k);
/*  265: 458 */         LabelSSTRecord newrec = new LabelSSTRecord();
/*  266: 459 */         int stringid = this.workbook.addSSTString(new UnicodeString(oldrec.getValue()));
/*  267:     */         
/*  268:     */ 
/*  269: 462 */         newrec.setRow(oldrec.getRow());
/*  270: 463 */         newrec.setColumn(oldrec.getColumn());
/*  271: 464 */         newrec.setXFIndex(oldrec.getXFIndex());
/*  272: 465 */         newrec.setSSTIndex(stringid);
/*  273: 466 */         records.add(k, newrec);
/*  274:     */       }
/*  275:     */     }
/*  276: 469 */     if (log.check(1)) {
/*  277: 470 */       log.log(1, new Object[] { "convertLabelRecords exit" });
/*  278:     */     }
/*  279:     */   }
/*  280:     */   
/*  281:     */   public Row.MissingCellPolicy getMissingCellPolicy()
/*  282:     */   {
/*  283: 482 */     return this.missingCellPolicy;
/*  284:     */   }
/*  285:     */   
/*  286:     */   public void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy)
/*  287:     */   {
/*  288: 497 */     this.missingCellPolicy = missingCellPolicy;
/*  289:     */   }
/*  290:     */   
/*  291:     */   public void setSheetOrder(String sheetname, int pos)
/*  292:     */   {
/*  293: 509 */     int oldSheetIndex = getSheetIndex(sheetname);
/*  294: 510 */     this._sheets.add(pos, this._sheets.remove(oldSheetIndex));
/*  295: 511 */     this.workbook.setSheetOrder(sheetname, pos);
/*  296:     */     
/*  297: 513 */     FormulaShifter shifter = FormulaShifter.createForSheetShift(oldSheetIndex, pos);
/*  298: 514 */     for (HSSFSheet sheet : this._sheets) {
/*  299: 515 */       sheet.getSheet().updateFormulasAfterCellShift(shifter, -1);
/*  300:     */     }
/*  301: 518 */     this.workbook.updateNamesAfterCellShift(shifter);
/*  302: 519 */     updateNamedRangesAfterSheetReorder(oldSheetIndex, pos);
/*  303:     */     
/*  304: 521 */     updateActiveSheetAfterSheetReorder(oldSheetIndex, pos);
/*  305:     */   }
/*  306:     */   
/*  307:     */   private void updateNamedRangesAfterSheetReorder(int oldIndex, int newIndex)
/*  308:     */   {
/*  309: 536 */     for (HSSFName name : this.names)
/*  310:     */     {
/*  311: 537 */       int i = name.getSheetIndex();
/*  312: 539 */       if (i != -1) {
/*  313: 541 */         if (i == oldIndex) {
/*  314: 542 */           name.setSheetIndex(newIndex);
/*  315: 545 */         } else if ((newIndex <= i) && (i < oldIndex)) {
/*  316: 546 */           name.setSheetIndex(i + 1);
/*  317: 549 */         } else if ((oldIndex < i) && (i <= newIndex)) {
/*  318: 550 */           name.setSheetIndex(i - 1);
/*  319:     */         }
/*  320:     */       }
/*  321:     */     }
/*  322:     */   }
/*  323:     */   
/*  324:     */   private void updateActiveSheetAfterSheetReorder(int oldIndex, int newIndex)
/*  325:     */   {
/*  326: 559 */     int active = getActiveSheetIndex();
/*  327: 560 */     if (active == oldIndex) {
/*  328: 562 */       setActiveSheet(newIndex);
/*  329: 563 */     } else if (((active >= oldIndex) || (active >= newIndex)) && ((active <= oldIndex) || (active <= newIndex))) {
/*  330: 566 */       if (newIndex > oldIndex) {
/*  331: 568 */         setActiveSheet(active - 1);
/*  332:     */       } else {
/*  333: 571 */         setActiveSheet(active + 1);
/*  334:     */       }
/*  335:     */     }
/*  336:     */   }
/*  337:     */   
/*  338:     */   private void validateSheetIndex(int index)
/*  339:     */   {
/*  340: 576 */     int lastSheetIx = this._sheets.size() - 1;
/*  341: 577 */     if ((index < 0) || (index > lastSheetIx))
/*  342:     */     {
/*  343: 578 */       String range = "(0.." + lastSheetIx + ")";
/*  344: 579 */       if (lastSheetIx == -1) {
/*  345: 580 */         range = "(no sheets)";
/*  346:     */       }
/*  347: 582 */       throw new IllegalArgumentException("Sheet index (" + index + ") is out of range " + range);
/*  348:     */     }
/*  349:     */   }
/*  350:     */   
/*  351:     */   public void setSelectedTab(int index)
/*  352:     */   {
/*  353: 594 */     validateSheetIndex(index);
/*  354: 595 */     int nSheets = this._sheets.size();
/*  355: 596 */     for (int i = 0; i < nSheets; i++) {
/*  356: 597 */       getSheetAt(i).setSelected(i == index);
/*  357:     */     }
/*  358: 599 */     this.workbook.getWindowOne().setNumSelectedTabs((short)1);
/*  359:     */   }
/*  360:     */   
/*  361:     */   public void setSelectedTabs(int[] indexes)
/*  362:     */   {
/*  363: 610 */     Collection<Integer> list = new ArrayList(indexes.length);
/*  364: 611 */     for (int index : indexes) {
/*  365: 612 */       list.add(Integer.valueOf(index));
/*  366:     */     }
/*  367: 614 */     setSelectedTabs(list);
/*  368:     */   }
/*  369:     */   
/*  370:     */   public void setSelectedTabs(Collection<Integer> indexes)
/*  371:     */   {
/*  372: 626 */     for (Iterator i$ = indexes.iterator(); i$.hasNext();)
/*  373:     */     {
/*  374: 626 */       int index = ((Integer)i$.next()).intValue();
/*  375: 627 */       validateSheetIndex(index);
/*  376:     */     }
/*  377: 630 */     Set<Integer> set = new HashSet(indexes);
/*  378: 631 */     int nSheets = this._sheets.size();
/*  379: 632 */     for (int i = 0; i < nSheets; i++)
/*  380:     */     {
/*  381: 633 */       boolean bSelect = set.contains(Integer.valueOf(i));
/*  382: 634 */       getSheetAt(i).setSelected(bSelect);
/*  383:     */     }
/*  384: 637 */     short nSelected = (short)set.size();
/*  385: 638 */     this.workbook.getWindowOne().setNumSelectedTabs(nSelected);
/*  386:     */   }
/*  387:     */   
/*  388:     */   public Collection<Integer> getSelectedTabs()
/*  389:     */   {
/*  390: 647 */     Collection<Integer> indexes = new ArrayList();
/*  391: 648 */     int nSheets = this._sheets.size();
/*  392: 649 */     for (int i = 0; i < nSheets; i++)
/*  393:     */     {
/*  394: 650 */       HSSFSheet sheet = getSheetAt(i);
/*  395: 651 */       if (sheet.isSelected()) {
/*  396: 652 */         indexes.add(Integer.valueOf(i));
/*  397:     */       }
/*  398:     */     }
/*  399: 655 */     return Collections.unmodifiableCollection(indexes);
/*  400:     */   }
/*  401:     */   
/*  402:     */   public void setActiveSheet(int index)
/*  403:     */   {
/*  404: 666 */     validateSheetIndex(index);
/*  405: 667 */     int nSheets = this._sheets.size();
/*  406: 668 */     for (int i = 0; i < nSheets; i++) {
/*  407: 669 */       getSheetAt(i).setActive(i == index);
/*  408:     */     }
/*  409: 671 */     this.workbook.getWindowOne().setActiveSheetIndex(index);
/*  410:     */   }
/*  411:     */   
/*  412:     */   public int getActiveSheetIndex()
/*  413:     */   {
/*  414: 683 */     return this.workbook.getWindowOne().getActiveSheetIndex();
/*  415:     */   }
/*  416:     */   
/*  417:     */   public void setFirstVisibleTab(int index)
/*  418:     */   {
/*  419: 696 */     this.workbook.getWindowOne().setFirstVisibleTab(index);
/*  420:     */   }
/*  421:     */   
/*  422:     */   public int getFirstVisibleTab()
/*  423:     */   {
/*  424: 704 */     return this.workbook.getWindowOne().getFirstVisibleTab();
/*  425:     */   }
/*  426:     */   
/*  427:     */   public void setSheetName(int sheetIx, String name)
/*  428:     */   {
/*  429: 718 */     if (name == null) {
/*  430: 719 */       throw new IllegalArgumentException("sheetName must not be null");
/*  431:     */     }
/*  432: 722 */     if (this.workbook.doesContainsSheetName(name, sheetIx)) {
/*  433: 723 */       throw new IllegalArgumentException("The workbook already contains a sheet named '" + name + "'");
/*  434:     */     }
/*  435: 725 */     validateSheetIndex(sheetIx);
/*  436: 726 */     this.workbook.setSheetName(sheetIx, name);
/*  437:     */   }
/*  438:     */   
/*  439:     */   public String getSheetName(int sheetIndex)
/*  440:     */   {
/*  441: 734 */     validateSheetIndex(sheetIndex);
/*  442: 735 */     return this.workbook.getSheetName(sheetIndex);
/*  443:     */   }
/*  444:     */   
/*  445:     */   public boolean isHidden()
/*  446:     */   {
/*  447: 740 */     return this.workbook.getWindowOne().getHidden();
/*  448:     */   }
/*  449:     */   
/*  450:     */   public void setHidden(boolean hiddenFlag)
/*  451:     */   {
/*  452: 745 */     this.workbook.getWindowOne().setHidden(hiddenFlag);
/*  453:     */   }
/*  454:     */   
/*  455:     */   public boolean isSheetHidden(int sheetIx)
/*  456:     */   {
/*  457: 750 */     validateSheetIndex(sheetIx);
/*  458: 751 */     return this.workbook.isSheetHidden(sheetIx);
/*  459:     */   }
/*  460:     */   
/*  461:     */   public boolean isSheetVeryHidden(int sheetIx)
/*  462:     */   {
/*  463: 756 */     validateSheetIndex(sheetIx);
/*  464: 757 */     return this.workbook.isSheetVeryHidden(sheetIx);
/*  465:     */   }
/*  466:     */   
/*  467:     */   public SheetVisibility getSheetVisibility(int sheetIx)
/*  468:     */   {
/*  469: 762 */     return this.workbook.getSheetVisibility(sheetIx);
/*  470:     */   }
/*  471:     */   
/*  472:     */   public void setSheetHidden(int sheetIx, boolean hidden)
/*  473:     */   {
/*  474: 767 */     setSheetVisibility(sheetIx, hidden ? SheetVisibility.HIDDEN : SheetVisibility.VISIBLE);
/*  475:     */   }
/*  476:     */   
/*  477:     */   @Removal(version="3.18")
/*  478:     */   @Deprecated
/*  479:     */   public void setSheetHidden(int sheetIx, int hidden)
/*  480:     */   {
/*  481: 774 */     switch (hidden)
/*  482:     */     {
/*  483:     */     case 0: 
/*  484: 776 */       setSheetVisibility(sheetIx, SheetVisibility.VISIBLE);
/*  485: 777 */       break;
/*  486:     */     case 1: 
/*  487: 779 */       setSheetVisibility(sheetIx, SheetVisibility.HIDDEN);
/*  488: 780 */       break;
/*  489:     */     case 2: 
/*  490: 782 */       setSheetVisibility(sheetIx, SheetVisibility.VERY_HIDDEN);
/*  491: 783 */       break;
/*  492:     */     default: 
/*  493: 785 */       throw new IllegalArgumentException("Invalid sheet state : " + hidden + "\n" + "Sheet state must beone of the Workbook.SHEET_STATE_* constants");
/*  494:     */     }
/*  495:     */   }
/*  496:     */   
/*  497:     */   public void setSheetVisibility(int sheetIx, SheetVisibility visibility)
/*  498:     */   {
/*  499: 792 */     validateSheetIndex(sheetIx);
/*  500: 793 */     this.workbook.setSheetHidden(sheetIx, visibility);
/*  501:     */   }
/*  502:     */   
/*  503:     */   public int getSheetIndex(String name)
/*  504:     */   {
/*  505: 802 */     return this.workbook.getSheetIndex(name);
/*  506:     */   }
/*  507:     */   
/*  508:     */   public int getSheetIndex(Sheet sheet)
/*  509:     */   {
/*  510: 811 */     return this._sheets.indexOf(sheet);
/*  511:     */   }
/*  512:     */   
/*  513:     */   public HSSFSheet createSheet()
/*  514:     */   {
/*  515: 824 */     HSSFSheet sheet = new HSSFSheet(this);
/*  516:     */     
/*  517: 826 */     this._sheets.add(sheet);
/*  518: 827 */     this.workbook.setSheetName(this._sheets.size() - 1, "Sheet" + (this._sheets.size() - 1));
/*  519: 828 */     boolean isOnlySheet = this._sheets.size() == 1;
/*  520: 829 */     sheet.setSelected(isOnlySheet);
/*  521: 830 */     sheet.setActive(isOnlySheet);
/*  522: 831 */     return sheet;
/*  523:     */   }
/*  524:     */   
/*  525:     */   public HSSFSheet cloneSheet(int sheetIndex)
/*  526:     */   {
/*  527: 842 */     validateSheetIndex(sheetIndex);
/*  528: 843 */     HSSFSheet srcSheet = (HSSFSheet)this._sheets.get(sheetIndex);
/*  529: 844 */     String srcName = this.workbook.getSheetName(sheetIndex);
/*  530: 845 */     HSSFSheet clonedSheet = srcSheet.cloneSheet(this);
/*  531: 846 */     clonedSheet.setSelected(false);
/*  532: 847 */     clonedSheet.setActive(false);
/*  533:     */     
/*  534: 849 */     String name = getUniqueSheetName(srcName);
/*  535: 850 */     int newSheetIndex = this._sheets.size();
/*  536: 851 */     this._sheets.add(clonedSheet);
/*  537: 852 */     this.workbook.setSheetName(newSheetIndex, name);
/*  538:     */     
/*  539:     */ 
/*  540: 855 */     int filterDbNameIndex = findExistingBuiltinNameRecordIdx(sheetIndex, (byte)13);
/*  541: 856 */     if (filterDbNameIndex != -1)
/*  542:     */     {
/*  543: 857 */       NameRecord newNameRecord = this.workbook.cloneFilter(filterDbNameIndex, newSheetIndex);
/*  544: 858 */       HSSFName newName = new HSSFName(this, newNameRecord);
/*  545: 859 */       this.names.add(newName);
/*  546:     */     }
/*  547: 864 */     return clonedSheet;
/*  548:     */   }
/*  549:     */   
/*  550:     */   private String getUniqueSheetName(String srcName)
/*  551:     */   {
/*  552: 868 */     int uniqueIndex = 2;
/*  553: 869 */     String baseName = srcName;
/*  554: 870 */     int bracketPos = srcName.lastIndexOf('(');
/*  555: 871 */     if ((bracketPos > 0) && (srcName.endsWith(")")))
/*  556:     */     {
/*  557: 872 */       String suffix = srcName.substring(bracketPos + 1, srcName.length() - ")".length());
/*  558:     */       try
/*  559:     */       {
/*  560: 874 */         uniqueIndex = Integer.parseInt(suffix.trim());
/*  561: 875 */         uniqueIndex++;
/*  562: 876 */         baseName = srcName.substring(0, bracketPos).trim();
/*  563:     */       }
/*  564:     */       catch (NumberFormatException e) {}
/*  565:     */     }
/*  566:     */     for (;;)
/*  567:     */     {
/*  568: 883 */       String index = Integer.toString(uniqueIndex++);
/*  569:     */       String name;
/*  570:     */       String name;
/*  571: 885 */       if (baseName.length() + index.length() + 2 < 31) {
/*  572: 886 */         name = baseName + " (" + index + ")";
/*  573:     */       } else {
/*  574: 888 */         name = baseName.substring(0, 31 - index.length() - 2) + "(" + index + ")";
/*  575:     */       }
/*  576: 892 */       if (this.workbook.getSheetIndex(name) == -1) {
/*  577: 893 */         return name;
/*  578:     */       }
/*  579:     */     }
/*  580:     */   }
/*  581:     */   
/*  582:     */   public HSSFSheet createSheet(String sheetname)
/*  583:     */   {
/*  584: 944 */     if (sheetname == null) {
/*  585: 945 */       throw new IllegalArgumentException("sheetName must not be null");
/*  586:     */     }
/*  587: 948 */     if (this.workbook.doesContainsSheetName(sheetname, this._sheets.size())) {
/*  588: 949 */       throw new IllegalArgumentException("The workbook already contains a sheet named '" + sheetname + "'");
/*  589:     */     }
/*  590: 952 */     HSSFSheet sheet = new HSSFSheet(this);
/*  591:     */     
/*  592: 954 */     this.workbook.setSheetName(this._sheets.size(), sheetname);
/*  593: 955 */     this._sheets.add(sheet);
/*  594: 956 */     boolean isOnlySheet = this._sheets.size() == 1;
/*  595: 957 */     sheet.setSelected(isOnlySheet);
/*  596: 958 */     sheet.setActive(isOnlySheet);
/*  597: 959 */     return sheet;
/*  598:     */   }
/*  599:     */   
/*  600:     */   public Iterator<Sheet> sheetIterator()
/*  601:     */   {
/*  602: 970 */     return new SheetIterator();
/*  603:     */   }
/*  604:     */   
/*  605:     */   public Iterator<Sheet> iterator()
/*  606:     */   {
/*  607: 979 */     return sheetIterator();
/*  608:     */   }
/*  609:     */   
/*  610:     */   private final class SheetIterator<T extends Sheet>
/*  611:     */     implements Iterator<T>
/*  612:     */   {
/*  613:     */     private final Iterator<T> it;
/*  614: 984 */     private T cursor = null;
/*  615:     */     
/*  616:     */     public SheetIterator()
/*  617:     */     {
/*  618: 987 */       this.it = HSSFWorkbook.this._sheets.iterator();
/*  619:     */     }
/*  620:     */     
/*  621:     */     public boolean hasNext()
/*  622:     */     {
/*  623: 991 */       return this.it.hasNext();
/*  624:     */     }
/*  625:     */     
/*  626:     */     public T next()
/*  627:     */       throws NoSuchElementException
/*  628:     */     {
/*  629: 995 */       this.cursor = ((Sheet)this.it.next());
/*  630: 996 */       return this.cursor;
/*  631:     */     }
/*  632:     */     
/*  633:     */     public void remove()
/*  634:     */       throws IllegalStateException
/*  635:     */     {
/*  636:1005 */       throw new UnsupportedOperationException("remove method not supported on HSSFWorkbook.iterator(). Use Sheet.removeSheetAt(int) instead.");
/*  637:     */     }
/*  638:     */   }
/*  639:     */   
/*  640:     */   public int getNumberOfSheets()
/*  641:     */   {
/*  642:1017 */     return this._sheets.size();
/*  643:     */   }
/*  644:     */   
/*  645:     */   private HSSFSheet[] getSheets()
/*  646:     */   {
/*  647:1021 */     HSSFSheet[] result = new HSSFSheet[this._sheets.size()];
/*  648:1022 */     this._sheets.toArray(result);
/*  649:1023 */     return result;
/*  650:     */   }
/*  651:     */   
/*  652:     */   public HSSFSheet getSheetAt(int index)
/*  653:     */   {
/*  654:1036 */     validateSheetIndex(index);
/*  655:1037 */     return (HSSFSheet)this._sheets.get(index);
/*  656:     */   }
/*  657:     */   
/*  658:     */   public HSSFSheet getSheet(String name)
/*  659:     */   {
/*  660:1049 */     HSSFSheet retval = null;
/*  661:1051 */     for (int k = 0; k < this._sheets.size(); k++)
/*  662:     */     {
/*  663:1053 */       String sheetname = this.workbook.getSheetName(k);
/*  664:1055 */       if (sheetname.equalsIgnoreCase(name)) {
/*  665:1057 */         retval = (HSSFSheet)this._sheets.get(k);
/*  666:     */       }
/*  667:     */     }
/*  668:1060 */     return retval;
/*  669:     */   }
/*  670:     */   
/*  671:     */   public void removeSheetAt(int index)
/*  672:     */   {
/*  673:1079 */     validateSheetIndex(index);
/*  674:1080 */     boolean wasSelected = getSheetAt(index).isSelected();
/*  675:     */     
/*  676:1082 */     this._sheets.remove(index);
/*  677:1083 */     this.workbook.removeSheet(index);
/*  678:     */     
/*  679:     */ 
/*  680:1086 */     int nSheets = this._sheets.size();
/*  681:1087 */     if (nSheets < 1) {
/*  682:1089 */       return;
/*  683:     */     }
/*  684:1092 */     int newSheetIndex = index;
/*  685:1093 */     if (newSheetIndex >= nSheets) {
/*  686:1094 */       newSheetIndex = nSheets - 1;
/*  687:     */     }
/*  688:1097 */     if (wasSelected)
/*  689:     */     {
/*  690:1098 */       boolean someOtherSheetIsStillSelected = false;
/*  691:1099 */       for (int i = 0; i < nSheets; i++) {
/*  692:1100 */         if (getSheetAt(i).isSelected())
/*  693:     */         {
/*  694:1101 */           someOtherSheetIsStillSelected = true;
/*  695:1102 */           break;
/*  696:     */         }
/*  697:     */       }
/*  698:1105 */       if (!someOtherSheetIsStillSelected) {
/*  699:1106 */         setSelectedTab(newSheetIndex);
/*  700:     */       }
/*  701:     */     }
/*  702:1111 */     int active = getActiveSheetIndex();
/*  703:1112 */     if (active == index) {
/*  704:1114 */       setActiveSheet(newSheetIndex);
/*  705:1115 */     } else if (active > index) {
/*  706:1117 */       setActiveSheet(active - 1);
/*  707:     */     }
/*  708:     */   }
/*  709:     */   
/*  710:     */   public void setBackupFlag(boolean backupValue)
/*  711:     */   {
/*  712:1129 */     BackupRecord backupRecord = this.workbook.getBackupRecord();
/*  713:     */     
/*  714:1131 */     backupRecord.setBackup((short)(backupValue ? 1 : 0));
/*  715:     */   }
/*  716:     */   
/*  717:     */   public boolean getBackupFlag()
/*  718:     */   {
/*  719:1143 */     BackupRecord backupRecord = this.workbook.getBackupRecord();
/*  720:     */     
/*  721:1145 */     return backupRecord.getBackup() != 0;
/*  722:     */   }
/*  723:     */   
/*  724:     */   int findExistingBuiltinNameRecordIdx(int sheetIndex, byte builtinCode)
/*  725:     */   {
/*  726:1149 */     for (int defNameIndex = 0; defNameIndex < this.names.size(); defNameIndex++)
/*  727:     */     {
/*  728:1150 */       NameRecord r = this.workbook.getNameRecord(defNameIndex);
/*  729:1151 */       if (r == null) {
/*  730:1152 */         throw new RuntimeException("Unable to find all defined names to iterate over");
/*  731:     */       }
/*  732:1154 */       if ((r.isBuiltInName()) && (r.getBuiltInName() == builtinCode)) {
/*  733:1157 */         if (r.getSheetNumber() - 1 == sheetIndex) {
/*  734:1158 */           return defNameIndex;
/*  735:     */         }
/*  736:     */       }
/*  737:     */     }
/*  738:1161 */     return -1;
/*  739:     */   }
/*  740:     */   
/*  741:     */   HSSFName createBuiltInName(byte builtinCode, int sheetIndex)
/*  742:     */   {
/*  743:1166 */     NameRecord nameRecord = this.workbook.createBuiltInName(builtinCode, sheetIndex + 1);
/*  744:     */     
/*  745:1168 */     HSSFName newName = new HSSFName(this, nameRecord, null);
/*  746:1169 */     this.names.add(newName);
/*  747:1170 */     return newName;
/*  748:     */   }
/*  749:     */   
/*  750:     */   HSSFName getBuiltInName(byte builtinCode, int sheetIndex)
/*  751:     */   {
/*  752:1175 */     int index = findExistingBuiltinNameRecordIdx(sheetIndex, builtinCode);
/*  753:1176 */     if (index < 0) {
/*  754:1177 */       return null;
/*  755:     */     }
/*  756:1179 */     return (HSSFName)this.names.get(index);
/*  757:     */   }
/*  758:     */   
/*  759:     */   public HSSFFont createFont()
/*  760:     */   {
/*  761:1192 */     this.workbook.createNewFont();
/*  762:1193 */     short fontindex = (short)(getNumberOfFonts() - 1);
/*  763:1195 */     if (fontindex > 3) {
/*  764:1197 */       fontindex = (short)(fontindex + 1);
/*  765:     */     }
/*  766:1199 */     if (fontindex == 32767) {
/*  767:1200 */       throw new IllegalArgumentException("Maximum number of fonts was exceeded");
/*  768:     */     }
/*  769:1205 */     return getFontAt(fontindex);
/*  770:     */   }
/*  771:     */   
/*  772:     */   public HSSFFont findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline)
/*  773:     */   {
/*  774:1216 */     short numberOfFonts = getNumberOfFonts();
/*  775:1217 */     for (short i = 0; i <= numberOfFonts; i = (short)(i + 1)) {
/*  776:1219 */       if (i != 4)
/*  777:     */       {
/*  778:1223 */         HSSFFont hssfFont = getFontAt(i);
/*  779:1224 */         if ((hssfFont.getBold() == bold) && (hssfFont.getColor() == color) && (hssfFont.getFontHeight() == fontHeight) && (hssfFont.getFontName().equals(name)) && (hssfFont.getItalic() == italic) && (hssfFont.getStrikeout() == strikeout) && (hssfFont.getTypeOffset() == typeOffset) && (hssfFont.getUnderline() == underline)) {
/*  780:1233 */           return hssfFont;
/*  781:     */         }
/*  782:     */       }
/*  783:     */     }
/*  784:1237 */     return null;
/*  785:     */   }
/*  786:     */   
/*  787:     */   public short getNumberOfFonts()
/*  788:     */   {
/*  789:1248 */     return (short)this.workbook.getNumberOfFontRecords();
/*  790:     */   }
/*  791:     */   
/*  792:     */   public HSSFFont getFontAt(short idx)
/*  793:     */   {
/*  794:1258 */     if (this.fonts == null) {
/*  795:1259 */       this.fonts = new HashMap();
/*  796:     */     }
/*  797:1265 */     Short sIdx = Short.valueOf(idx);
/*  798:1266 */     if (this.fonts.containsKey(sIdx)) {
/*  799:1267 */       return (HSSFFont)this.fonts.get(sIdx);
/*  800:     */     }
/*  801:1270 */     FontRecord font = this.workbook.getFontRecordAt(idx);
/*  802:1271 */     HSSFFont retval = new HSSFFont(idx, font);
/*  803:1272 */     this.fonts.put(sIdx, retval);
/*  804:     */     
/*  805:1274 */     return retval;
/*  806:     */   }
/*  807:     */   
/*  808:     */   protected void resetFontCache()
/*  809:     */   {
/*  810:1284 */     this.fonts = new HashMap();
/*  811:     */   }
/*  812:     */   
/*  813:     */   public HSSFCellStyle createCellStyle()
/*  814:     */   {
/*  815:1297 */     if (this.workbook.getNumExFormats() == 4030) {
/*  816:1298 */       throw new IllegalStateException("The maximum number of cell styles was exceeded. You can define up to 4000 styles in a .xls workbook");
/*  817:     */     }
/*  818:1301 */     ExtendedFormatRecord xfr = this.workbook.createCellXF();
/*  819:1302 */     short index = (short)(getNumCellStyles() - 1);
/*  820:1303 */     return new HSSFCellStyle(index, xfr, this);
/*  821:     */   }
/*  822:     */   
/*  823:     */   public int getNumCellStyles()
/*  824:     */   {
/*  825:1313 */     return this.workbook.getNumExFormats();
/*  826:     */   }
/*  827:     */   
/*  828:     */   public HSSFCellStyle getCellStyleAt(int idx)
/*  829:     */   {
/*  830:1324 */     ExtendedFormatRecord xfr = this.workbook.getExFormatAt(idx);
/*  831:1325 */     return new HSSFCellStyle((short)idx, xfr, this);
/*  832:     */   }
/*  833:     */   
/*  834:     */   public void close()
/*  835:     */     throws IOException
/*  836:     */   {
/*  837:1338 */     super.close();
/*  838:     */   }
/*  839:     */   
/*  840:     */   public void write()
/*  841:     */     throws IOException
/*  842:     */   {
/*  843:1353 */     validateInPlaceWritePossible();
/*  844:1354 */     DirectoryNode dir = getDirectory();
/*  845:     */     
/*  846:     */ 
/*  847:1357 */     DocumentNode workbookNode = (DocumentNode)dir.getEntry(getWorkbookDirEntryName(dir));
/*  848:     */     
/*  849:1359 */     NPOIFSDocument workbookDoc = new NPOIFSDocument(workbookNode);
/*  850:1360 */     workbookDoc.replaceContents(new ByteArrayInputStream(getBytes()));
/*  851:     */     
/*  852:     */ 
/*  853:1363 */     writeProperties();
/*  854:     */     
/*  855:     */ 
/*  856:1366 */     dir.getFileSystem().writeFilesystem();
/*  857:     */   }
/*  858:     */   
/*  859:     */   public void write(File newFile)
/*  860:     */     throws IOException
/*  861:     */   {
/*  862:1386 */     POIFSFileSystem fs = POIFSFileSystem.create(newFile);
/*  863:     */     try
/*  864:     */     {
/*  865:1388 */       write(fs);
/*  866:1389 */       fs.writeFilesystem();
/*  867:     */     }
/*  868:     */     finally
/*  869:     */     {
/*  870:1391 */       fs.close();
/*  871:     */     }
/*  872:     */   }
/*  873:     */   
/*  874:     */   public void write(OutputStream stream)
/*  875:     */     throws IOException
/*  876:     */   {
/*  877:1412 */     NPOIFSFileSystem fs = new NPOIFSFileSystem();
/*  878:     */     try
/*  879:     */     {
/*  880:1414 */       write(fs);
/*  881:1415 */       fs.writeFilesystem(stream);
/*  882:     */     }
/*  883:     */     finally
/*  884:     */     {
/*  885:1417 */       fs.close();
/*  886:     */     }
/*  887:     */   }
/*  888:     */   
/*  889:     */   private void write(NPOIFSFileSystem fs)
/*  890:     */     throws IOException
/*  891:     */   {
/*  892:1425 */     List<String> excepts = new ArrayList(1);
/*  893:     */     
/*  894:     */ 
/*  895:1428 */     fs.createDocument(new ByteArrayInputStream(getBytes()), "Workbook");
/*  896:     */     
/*  897:     */ 
/*  898:1431 */     writeProperties(fs, excepts);
/*  899:1433 */     if (this.preserveNodes)
/*  900:     */     {
/*  901:1437 */       excepts.addAll(Arrays.asList(InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES));
/*  902:     */       
/*  903:     */ 
/*  904:     */ 
/*  905:1441 */       excepts.addAll(Arrays.asList(new String[] { "\005DocumentSummaryInformation", "\005SummaryInformation", getEncryptedPropertyStreamName() }));
/*  906:     */       
/*  907:     */ 
/*  908:     */ 
/*  909:     */ 
/*  910:     */ 
/*  911:     */ 
/*  912:1448 */       EntryUtils.copyNodes(new FilteringDirectoryNode(getDirectory(), excepts), new FilteringDirectoryNode(fs.getRoot(), excepts));
/*  913:     */       
/*  914:     */ 
/*  915:     */ 
/*  916:     */ 
/*  917:     */ 
/*  918:     */ 
/*  919:1455 */       fs.getRoot().setStorageClsid(getDirectory().getStorageClsid());
/*  920:     */     }
/*  921:     */   }
/*  922:     */   
/*  923:     */   private static final class SheetRecordCollector
/*  924:     */     implements RecordAggregate.RecordVisitor
/*  925:     */   {
/*  926:     */     private List<Record> _list;
/*  927:     */     private int _totalSize;
/*  928:     */     
/*  929:     */     public SheetRecordCollector()
/*  930:     */     {
/*  931:1468 */       this._totalSize = 0;
/*  932:1469 */       this._list = new ArrayList(128);
/*  933:     */     }
/*  934:     */     
/*  935:     */     public int getTotalSize()
/*  936:     */     {
/*  937:1472 */       return this._totalSize;
/*  938:     */     }
/*  939:     */     
/*  940:     */     public void visitRecord(Record r)
/*  941:     */     {
/*  942:1476 */       this._list.add(r);
/*  943:1477 */       this._totalSize += r.getRecordSize();
/*  944:     */     }
/*  945:     */     
/*  946:     */     public int serialize(int offset, byte[] data)
/*  947:     */     {
/*  948:1481 */       int result = 0;
/*  949:1482 */       for (Record rec : this._list) {
/*  950:1483 */         result += rec.serialize(offset + result, data);
/*  951:     */       }
/*  952:1485 */       return result;
/*  953:     */     }
/*  954:     */   }
/*  955:     */   
/*  956:     */   public byte[] getBytes()
/*  957:     */   {
/*  958:1499 */     if (log.check(1)) {
/*  959:1500 */       log.log(1, new Object[] { "HSSFWorkbook.getBytes()" });
/*  960:     */     }
/*  961:1503 */     HSSFSheet[] sheets = getSheets();
/*  962:1504 */     int nSheets = sheets.length;
/*  963:     */     
/*  964:1506 */     updateEncryptionInfo();
/*  965:     */     
/*  966:     */ 
/*  967:     */ 
/*  968:1510 */     this.workbook.preSerialize();
/*  969:1511 */     for (HSSFSheet sheet : sheets)
/*  970:     */     {
/*  971:1512 */       sheet.getSheet().preSerialize();
/*  972:1513 */       sheet.preSerialize();
/*  973:     */     }
/*  974:1516 */     int totalsize = this.workbook.getSize();
/*  975:     */     
/*  976:     */ 
/*  977:1519 */     SheetRecordCollector[] srCollectors = new SheetRecordCollector[nSheets];
/*  978:1520 */     for (int k = 0; k < nSheets; k++)
/*  979:     */     {
/*  980:1521 */       this.workbook.setSheetBof(k, totalsize);
/*  981:1522 */       SheetRecordCollector src = new SheetRecordCollector();
/*  982:1523 */       sheets[k].getSheet().visitContainedRecords(src, totalsize);
/*  983:1524 */       totalsize += src.getTotalSize();
/*  984:1525 */       srCollectors[k] = src;
/*  985:     */     }
/*  986:1528 */     byte[] retval = new byte[totalsize];
/*  987:1529 */     int pos = this.workbook.serialize(0, retval);
/*  988:1531 */     for (int k = 0; k < nSheets; k++)
/*  989:     */     {
/*  990:1532 */       SheetRecordCollector src = srCollectors[k];
/*  991:1533 */       int serializedSize = src.serialize(pos, retval);
/*  992:1534 */       if (serializedSize != src.getTotalSize()) {
/*  993:1538 */         throw new IllegalStateException("Actual serialized sheet size (" + serializedSize + ") differs from pre-calculated size (" + src.getTotalSize() + ") for sheet (" + k + ")");
/*  994:     */       }
/*  995:1543 */       pos += serializedSize;
/*  996:     */     }
/*  997:1546 */     encryptBytes(retval);
/*  998:     */     
/*  999:1548 */     return retval;
/* 1000:     */   }
/* 1001:     */   
/* 1002:     */   protected void encryptBytes(byte[] buf)
/* 1003:     */   {
/* 1004:1553 */     EncryptionInfo ei = getEncryptionInfo();
/* 1005:1554 */     if (ei == null) {
/* 1006:1555 */       return;
/* 1007:     */     }
/* 1008:1557 */     Encryptor enc = ei.getEncryptor();
/* 1009:1558 */     int initialOffset = 0;
/* 1010:1559 */     LittleEndianByteArrayInputStream plain = new LittleEndianByteArrayInputStream(buf, 0);
/* 1011:1560 */     LittleEndianByteArrayOutputStream leos = new LittleEndianByteArrayOutputStream(buf, 0);
/* 1012:1561 */     enc.setChunkSize(1024);
/* 1013:1562 */     byte[] tmp = new byte[1024];
/* 1014:     */     try
/* 1015:     */     {
/* 1016:1564 */       ChunkedCipherOutputStream os = enc.getDataStream(leos, initialOffset);
/* 1017:1565 */       int totalBytes = 0;
/* 1018:1566 */       while (totalBytes < buf.length)
/* 1019:     */       {
/* 1020:1567 */         plain.read(tmp, 0, 4);
/* 1021:1568 */         int sid = LittleEndian.getUShort(tmp, 0);
/* 1022:1569 */         int len = LittleEndian.getUShort(tmp, 2);
/* 1023:1570 */         boolean isPlain = Biff8DecryptingStream.isNeverEncryptedRecord(sid);
/* 1024:1571 */         os.setNextRecordSize(len, isPlain);
/* 1025:1572 */         os.writePlain(tmp, 0, 4);
/* 1026:1573 */         if (sid == 133)
/* 1027:     */         {
/* 1028:1576 */           byte[] bsrBuf = new byte[len];
/* 1029:1577 */           plain.readFully(bsrBuf);
/* 1030:1578 */           os.writePlain(bsrBuf, 0, 4);
/* 1031:1579 */           os.write(bsrBuf, 4, len - 4);
/* 1032:     */         }
/* 1033:     */         else
/* 1034:     */         {
/* 1035:1581 */           int todo = len;
/* 1036:1582 */           while (todo > 0)
/* 1037:     */           {
/* 1038:1583 */             int nextLen = Math.min(todo, tmp.length);
/* 1039:1584 */             plain.readFully(tmp, 0, nextLen);
/* 1040:1585 */             if (isPlain) {
/* 1041:1586 */               os.writePlain(tmp, 0, nextLen);
/* 1042:     */             } else {
/* 1043:1588 */               os.write(tmp, 0, nextLen);
/* 1044:     */             }
/* 1045:1590 */             todo -= nextLen;
/* 1046:     */           }
/* 1047:     */         }
/* 1048:1593 */         totalBytes += 4 + len;
/* 1049:     */       }
/* 1050:1595 */       os.close();
/* 1051:     */     }
/* 1052:     */     catch (Exception e)
/* 1053:     */     {
/* 1054:1597 */       throw new EncryptedDocumentException(e);
/* 1055:     */     }
/* 1056:     */   }
/* 1057:     */   
/* 1058:     */   InternalWorkbook getWorkbook()
/* 1059:     */   {
/* 1060:1602 */     return this.workbook;
/* 1061:     */   }
/* 1062:     */   
/* 1063:     */   public int getNumberOfNames()
/* 1064:     */   {
/* 1065:1607 */     return this.names.size();
/* 1066:     */   }
/* 1067:     */   
/* 1068:     */   public HSSFName getName(String name)
/* 1069:     */   {
/* 1070:1612 */     int nameIndex = getNameIndex(name);
/* 1071:1613 */     if (nameIndex < 0) {
/* 1072:1614 */       return null;
/* 1073:     */     }
/* 1074:1616 */     return (HSSFName)this.names.get(nameIndex);
/* 1075:     */   }
/* 1076:     */   
/* 1077:     */   public List<HSSFName> getNames(String name)
/* 1078:     */   {
/* 1079:1621 */     List<HSSFName> nameList = new ArrayList();
/* 1080:1622 */     for (HSSFName nr : this.names) {
/* 1081:1623 */       if (nr.getNameName().equals(name)) {
/* 1082:1624 */         nameList.add(nr);
/* 1083:     */       }
/* 1084:     */     }
/* 1085:1628 */     return Collections.unmodifiableList(nameList);
/* 1086:     */   }
/* 1087:     */   
/* 1088:     */   public HSSFName getNameAt(int nameIndex)
/* 1089:     */   {
/* 1090:1633 */     int nNames = this.names.size();
/* 1091:1634 */     if (nNames < 1) {
/* 1092:1635 */       throw new IllegalStateException("There are no defined names in this workbook");
/* 1093:     */     }
/* 1094:1637 */     if ((nameIndex < 0) || (nameIndex > nNames)) {
/* 1095:1638 */       throw new IllegalArgumentException("Specified name index " + nameIndex + " is outside the allowable range (0.." + (nNames - 1) + ").");
/* 1096:     */     }
/* 1097:1641 */     return (HSSFName)this.names.get(nameIndex);
/* 1098:     */   }
/* 1099:     */   
/* 1100:     */   public List<HSSFName> getAllNames()
/* 1101:     */   {
/* 1102:1646 */     return Collections.unmodifiableList(this.names);
/* 1103:     */   }
/* 1104:     */   
/* 1105:     */   public NameRecord getNameRecord(int nameIndex)
/* 1106:     */   {
/* 1107:1650 */     return getWorkbook().getNameRecord(nameIndex);
/* 1108:     */   }
/* 1109:     */   
/* 1110:     */   public String getNameName(int index)
/* 1111:     */   {
/* 1112:1658 */     return getNameAt(index).getNameName();
/* 1113:     */   }
/* 1114:     */   
/* 1115:     */   public void setPrintArea(int sheetIndex, String reference)
/* 1116:     */   {
/* 1117:1671 */     NameRecord name = this.workbook.getSpecificBuiltinRecord((byte)6, sheetIndex + 1);
/* 1118:1674 */     if (name == null) {
/* 1119:1675 */       name = this.workbook.createBuiltInName((byte)6, sheetIndex + 1);
/* 1120:     */     }
/* 1121:1678 */     String[] parts = COMMA_PATTERN.split(reference);
/* 1122:1679 */     StringBuffer sb = new StringBuffer(32);
/* 1123:1680 */     for (int i = 0; i < parts.length; i++)
/* 1124:     */     {
/* 1125:1681 */       if (i > 0) {
/* 1126:1682 */         sb.append(",");
/* 1127:     */       }
/* 1128:1684 */       SheetNameFormatter.appendFormat(sb, getSheetName(sheetIndex));
/* 1129:1685 */       sb.append("!");
/* 1130:1686 */       sb.append(parts[i]);
/* 1131:     */     }
/* 1132:1688 */     name.setNameDefinition(HSSFFormulaParser.parse(sb.toString(), this, FormulaType.NAMEDRANGE, sheetIndex));
/* 1133:     */   }
/* 1134:     */   
/* 1135:     */   public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)
/* 1136:     */   {
/* 1137:1705 */     CellReference cell = new CellReference(startRow, startColumn, true, true);
/* 1138:1706 */     String reference = cell.formatAsString();
/* 1139:     */     
/* 1140:1708 */     cell = new CellReference(endRow, endColumn, true, true);
/* 1141:1709 */     reference = reference + ":" + cell.formatAsString();
/* 1142:     */     
/* 1143:1711 */     setPrintArea(sheetIndex, reference);
/* 1144:     */   }
/* 1145:     */   
/* 1146:     */   public String getPrintArea(int sheetIndex)
/* 1147:     */   {
/* 1148:1722 */     NameRecord name = this.workbook.getSpecificBuiltinRecord((byte)6, sheetIndex + 1);
/* 1149:1724 */     if (name == null) {
/* 1150:1725 */       return null;
/* 1151:     */     }
/* 1152:1728 */     return HSSFFormulaParser.toFormulaString(this, name.getNameDefinition());
/* 1153:     */   }
/* 1154:     */   
/* 1155:     */   public void removePrintArea(int sheetIndex)
/* 1156:     */   {
/* 1157:1737 */     getWorkbook().removeBuiltinRecord((byte)6, sheetIndex + 1);
/* 1158:     */   }
/* 1159:     */   
/* 1160:     */   public HSSFName createName()
/* 1161:     */   {
/* 1162:1745 */     NameRecord nameRecord = this.workbook.createName();
/* 1163:     */     
/* 1164:1747 */     HSSFName newName = new HSSFName(this, nameRecord);
/* 1165:     */     
/* 1166:1749 */     this.names.add(newName);
/* 1167:     */     
/* 1168:1751 */     return newName;
/* 1169:     */   }
/* 1170:     */   
/* 1171:     */   public int getNameIndex(String name)
/* 1172:     */   {
/* 1173:1757 */     for (int k = 0; k < this.names.size(); k++)
/* 1174:     */     {
/* 1175:1758 */       String nameName = getNameName(k);
/* 1176:1760 */       if (nameName.equalsIgnoreCase(name)) {
/* 1177:1761 */         return k;
/* 1178:     */       }
/* 1179:     */     }
/* 1180:1764 */     return -1;
/* 1181:     */   }
/* 1182:     */   
/* 1183:     */   int getNameIndex(HSSFName name)
/* 1184:     */   {
/* 1185:1778 */     for (int k = 0; k < this.names.size(); k++) {
/* 1186:1779 */       if (name == this.names.get(k)) {
/* 1187:1780 */         return k;
/* 1188:     */       }
/* 1189:     */     }
/* 1190:1783 */     return -1;
/* 1191:     */   }
/* 1192:     */   
/* 1193:     */   public void removeName(int index)
/* 1194:     */   {
/* 1195:1789 */     this.names.remove(index);
/* 1196:1790 */     this.workbook.removeName(index);
/* 1197:     */   }
/* 1198:     */   
/* 1199:     */   public HSSFDataFormat createDataFormat()
/* 1200:     */   {
/* 1201:1801 */     if (this.formatter == null) {
/* 1202:1802 */       this.formatter = new HSSFDataFormat(this.workbook);
/* 1203:     */     }
/* 1204:1804 */     return this.formatter;
/* 1205:     */   }
/* 1206:     */   
/* 1207:     */   public void removeName(String name)
/* 1208:     */   {
/* 1209:1810 */     int index = getNameIndex(name);
/* 1210:1811 */     removeName(index);
/* 1211:     */   }
/* 1212:     */   
/* 1213:     */   public void removeName(Name name)
/* 1214:     */   {
/* 1215:1823 */     int index = getNameIndex((HSSFName)name);
/* 1216:1824 */     removeName(index);
/* 1217:     */   }
/* 1218:     */   
/* 1219:     */   public HSSFPalette getCustomPalette()
/* 1220:     */   {
/* 1221:1829 */     return new HSSFPalette(this.workbook.getCustomPalette());
/* 1222:     */   }
/* 1223:     */   
/* 1224:     */   public void insertChartRecord()
/* 1225:     */   {
/* 1226:1835 */     int loc = this.workbook.findFirstRecordLocBySid((short)252);
/* 1227:1836 */     byte[] data = { 15, 0, 0, -16, 82, 0, 0, 0, 0, 0, 6, -16, 24, 0, 0, 0, 1, 8, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 51, 0, 11, -16, 18, 0, 0, 0, -65, 0, 8, 0, 8, 0, -127, 1, 9, 0, 0, 8, -64, 1, 64, 0, 0, 8, 64, 0, 30, -15, 16, 0, 0, 0, 13, 0, 0, 8, 12, 0, 0, 8, 23, 0, 0, 8, -9, 0, 0, 16 };
/* 1228:     */     
/* 1229:     */ 
/* 1230:     */ 
/* 1231:     */ 
/* 1232:     */ 
/* 1233:     */ 
/* 1234:     */ 
/* 1235:     */ 
/* 1236:     */ 
/* 1237:     */ 
/* 1238:     */ 
/* 1239:     */ 
/* 1240:     */ 
/* 1241:     */ 
/* 1242:     */ 
/* 1243:     */ 
/* 1244:     */ 
/* 1245:     */ 
/* 1246:     */ 
/* 1247:1856 */     UnknownRecord r = new UnknownRecord(235, data);
/* 1248:1857 */     this.workbook.getRecords().add(loc, r);
/* 1249:     */   }
/* 1250:     */   
/* 1251:     */   public void dumpDrawingGroupRecords(boolean fat)
/* 1252:     */   {
/* 1253:1865 */     DrawingGroupRecord r = (DrawingGroupRecord)this.workbook.findFirstRecordBySid((short)235);
/* 1254:1866 */     r.decode();
/* 1255:1867 */     List<EscherRecord> escherRecords = r.getEscherRecords();
/* 1256:1868 */     PrintWriter w = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
/* 1257:1869 */     for (EscherRecord escherRecord : escherRecords) {
/* 1258:1870 */       if (fat) {
/* 1259:1871 */         System.out.println(escherRecord);
/* 1260:     */       } else {
/* 1261:1873 */         escherRecord.display(w, 0);
/* 1262:     */       }
/* 1263:     */     }
/* 1264:1876 */     w.flush();
/* 1265:     */   }
/* 1266:     */   
/* 1267:     */   void initDrawings()
/* 1268:     */   {
/* 1269:1880 */     DrawingManager2 mgr = this.workbook.findDrawingGroup();
/* 1270:1881 */     if (mgr != null) {
/* 1271:1882 */       for (HSSFSheet sh : this._sheets) {
/* 1272:1883 */         sh.getDrawingPatriarch();
/* 1273:     */       }
/* 1274:     */     } else {
/* 1275:1886 */       this.workbook.createDrawingGroup();
/* 1276:     */     }
/* 1277:     */   }
/* 1278:     */   
/* 1279:     */   public int addPicture(byte[] pictureData, int format)
/* 1280:     */   {
/* 1281:1908 */     initDrawings();
/* 1282:     */     
/* 1283:1910 */     byte[] uid = DigestUtils.md5(pictureData);
/* 1284:1914 */     switch (format)
/* 1285:     */     {
/* 1286:     */     case 3: 
/* 1287:1918 */       if (LittleEndian.getInt(pictureData) == -1698247209)
/* 1288:     */       {
/* 1289:1919 */         byte[] picDataNoHeader = new byte[pictureData.length - 22];
/* 1290:1920 */         System.arraycopy(pictureData, 22, picDataNoHeader, 0, pictureData.length - 22);
/* 1291:1921 */         pictureData = picDataNoHeader;
/* 1292:     */       }
/* 1293:     */     case 2: 
/* 1294:1925 */       EscherMetafileBlip blipRecordMeta = new EscherMetafileBlip();
/* 1295:1926 */       blipRecord = blipRecordMeta;
/* 1296:1927 */       blipRecordMeta.setUID(uid);
/* 1297:1928 */       blipRecordMeta.setPictureData(pictureData);
/* 1298:     */       
/* 1299:1930 */       blipRecordMeta.setFilter((byte)-2);
/* 1300:1931 */       blipSize = blipRecordMeta.getCompressedSize() + 58;
/* 1301:1932 */       escherTag = 0;
/* 1302:1933 */       break;
/* 1303:     */     }
/* 1304:1935 */     EscherBitmapBlip blipRecordBitmap = new EscherBitmapBlip();
/* 1305:1936 */     EscherBlipRecord blipRecord = blipRecordBitmap;
/* 1306:1937 */     blipRecordBitmap.setUID(uid);
/* 1307:1938 */     blipRecordBitmap.setMarker((byte)-1);
/* 1308:1939 */     blipRecordBitmap.setPictureData(pictureData);
/* 1309:1940 */     int blipSize = pictureData.length + 25;
/* 1310:1941 */     short escherTag = 255;
/* 1311:     */     
/* 1312:     */ 
/* 1313:     */ 
/* 1314:1945 */     blipRecord.setRecordId((short)(-4072 + format));
/* 1315:1946 */     switch (format)
/* 1316:     */     {
/* 1317:     */     case 2: 
/* 1318:1949 */       blipRecord.setOptions((short)15680);
/* 1319:1950 */       break;
/* 1320:     */     case 3: 
/* 1321:1952 */       blipRecord.setOptions((short)8544);
/* 1322:1953 */       break;
/* 1323:     */     case 4: 
/* 1324:1955 */       blipRecord.setOptions((short)21536);
/* 1325:1956 */       break;
/* 1326:     */     case 6: 
/* 1327:1958 */       blipRecord.setOptions((short)28160);
/* 1328:1959 */       break;
/* 1329:     */     case 5: 
/* 1330:1961 */       blipRecord.setOptions((short)18080);
/* 1331:1962 */       break;
/* 1332:     */     case 7: 
/* 1333:1964 */       blipRecord.setOptions((short)31360);
/* 1334:1965 */       break;
/* 1335:     */     default: 
/* 1336:1967 */       throw new IllegalStateException("Unexpected picture format: " + format);
/* 1337:     */     }
/* 1338:1970 */     EscherBSERecord r = new EscherBSERecord();
/* 1339:1971 */     r.setRecordId((short)-4089);
/* 1340:1972 */     r.setOptions((short)(0x2 | format << 4));
/* 1341:1973 */     r.setBlipTypeMacOS((byte)format);
/* 1342:1974 */     r.setBlipTypeWin32((byte)format);
/* 1343:1975 */     r.setUid(uid);
/* 1344:1976 */     r.setTag(escherTag);
/* 1345:1977 */     r.setSize(blipSize);
/* 1346:1978 */     r.setRef(0);
/* 1347:1979 */     r.setOffset(0);
/* 1348:1980 */     r.setBlipRecord(blipRecord);
/* 1349:     */     
/* 1350:1982 */     return this.workbook.addBSERecord(r);
/* 1351:     */   }
/* 1352:     */   
/* 1353:     */   public List<HSSFPictureData> getAllPictures()
/* 1354:     */   {
/* 1355:1994 */     List<HSSFPictureData> pictures = new ArrayList();
/* 1356:1995 */     for (Record r : this.workbook.getRecords()) {
/* 1357:1996 */       if ((r instanceof AbstractEscherHolderRecord))
/* 1358:     */       {
/* 1359:1997 */         ((AbstractEscherHolderRecord)r).decode();
/* 1360:1998 */         List<EscherRecord> escherRecords = ((AbstractEscherHolderRecord)r).getEscherRecords();
/* 1361:1999 */         searchForPictures(escherRecords, pictures);
/* 1362:     */       }
/* 1363:     */     }
/* 1364:2002 */     return Collections.unmodifiableList(pictures);
/* 1365:     */   }
/* 1366:     */   
/* 1367:     */   private void searchForPictures(List<EscherRecord> escherRecords, List<HSSFPictureData> pictures)
/* 1368:     */   {
/* 1369:2013 */     for (EscherRecord escherRecord : escherRecords)
/* 1370:     */     {
/* 1371:2015 */       if ((escherRecord instanceof EscherBSERecord))
/* 1372:     */       {
/* 1373:2017 */         EscherBlipRecord blip = ((EscherBSERecord)escherRecord).getBlipRecord();
/* 1374:2018 */         if (blip != null)
/* 1375:     */         {
/* 1376:2021 */           HSSFPictureData picture = new HSSFPictureData(blip);
/* 1377:2022 */           pictures.add(picture);
/* 1378:     */         }
/* 1379:     */       }
/* 1380:2029 */       searchForPictures(escherRecord.getChildRecords(), pictures);
/* 1381:     */     }
/* 1382:     */   }
/* 1383:     */   
/* 1384:     */   protected static Map<String, ClassID> getOleMap()
/* 1385:     */   {
/* 1386:2035 */     Map<String, ClassID> olemap = new HashMap();
/* 1387:2036 */     olemap.put("PowerPoint Document", ClassID.PPT_SHOW);
/* 1388:2037 */     for (String str : InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES) {
/* 1389:2038 */       olemap.put(str, ClassID.XLS_WORKBOOK);
/* 1390:     */     }
/* 1391:2041 */     return olemap;
/* 1392:     */   }
/* 1393:     */   
/* 1394:     */   public int addOlePackage(POIFSFileSystem poiData, String label, String fileName, String command)
/* 1395:     */     throws IOException
/* 1396:     */   {
/* 1397:2056 */     DirectoryNode root = poiData.getRoot();
/* 1398:2057 */     Map<String, ClassID> olemap = getOleMap();
/* 1399:2058 */     for (Entry<String, ClassID> entry : olemap.entrySet()) {
/* 1400:2059 */       if (root.hasEntry((String)entry.getKey()))
/* 1401:     */       {
/* 1402:2060 */         root.setStorageClsid((ClassID)entry.getValue());
/* 1403:2061 */         break;
/* 1404:     */       }
/* 1405:     */     }
/* 1406:2065 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 1407:2066 */     poiData.writeFilesystem(bos);
/* 1408:2067 */     return addOlePackage(bos.toByteArray(), label, fileName, command);
/* 1409:     */   }
/* 1410:     */   
/* 1411:     */   public int addOlePackage(byte[] oleData, String label, String fileName, String command)
/* 1412:     */     throws IOException
/* 1413:     */   {
/* 1414:2074 */     if (initDirectory()) {
/* 1415:2075 */       this.preserveNodes = true;
/* 1416:     */     }
/* 1417:2079 */     int storageId = 0;
/* 1418:2080 */     DirectoryEntry oleDir = null;
/* 1419:     */     do
/* 1420:     */     {
/* 1421:2082 */       String storageStr = "MBD" + HexDump.toHex(++storageId);
/* 1422:2083 */       if (!getDirectory().hasEntry(storageStr))
/* 1423:     */       {
/* 1424:2084 */         oleDir = getDirectory().createDirectory(storageStr);
/* 1425:2085 */         oleDir.setStorageClsid(ClassID.OLE10_PACKAGE);
/* 1426:     */       }
/* 1427:2087 */     } while (oleDir == null);
/* 1428:2092 */     byte[] oleBytes = { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/* 1429:2093 */     oleDir.createDocument("\001Ole", new ByteArrayInputStream(oleBytes));
/* 1430:     */     
/* 1431:2095 */     Ole10Native oleNative = new Ole10Native(label, fileName, command, oleData);
/* 1432:2096 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 1433:2097 */     oleNative.writeOut(bos);
/* 1434:2098 */     oleDir.createDocument("\001Ole10Native", new ByteArrayInputStream(bos.toByteArray()));
/* 1435:     */     
/* 1436:2100 */     return storageId;
/* 1437:     */   }
/* 1438:     */   
/* 1439:     */   public int linkExternalWorkbook(String name, Workbook workbook)
/* 1440:     */   {
/* 1441:2114 */     return this.workbook.linkExternalWorkbook(name, workbook);
/* 1442:     */   }
/* 1443:     */   
/* 1444:     */   public boolean isWriteProtected()
/* 1445:     */   {
/* 1446:2121 */     return this.workbook.isWriteProtected();
/* 1447:     */   }
/* 1448:     */   
/* 1449:     */   public void writeProtectWorkbook(String password, String username)
/* 1450:     */   {
/* 1451:2130 */     this.workbook.writeProtectWorkbook(password, username);
/* 1452:     */   }
/* 1453:     */   
/* 1454:     */   public void unwriteProtectWorkbook()
/* 1455:     */   {
/* 1456:2137 */     this.workbook.unwriteProtectWorkbook();
/* 1457:     */   }
/* 1458:     */   
/* 1459:     */   public List<HSSFObjectData> getAllEmbeddedObjects()
/* 1460:     */   {
/* 1461:2147 */     List<HSSFObjectData> objects = new ArrayList();
/* 1462:2148 */     for (HSSFSheet sheet : this._sheets) {
/* 1463:2150 */       getAllEmbeddedObjects(sheet, objects);
/* 1464:     */     }
/* 1465:2152 */     return Collections.unmodifiableList(objects);
/* 1466:     */   }
/* 1467:     */   
/* 1468:     */   private void getAllEmbeddedObjects(HSSFSheet sheet, List<HSSFObjectData> objects)
/* 1469:     */   {
/* 1470:2163 */     HSSFPatriarch patriarch = sheet.getDrawingPatriarch();
/* 1471:2164 */     if (null == patriarch) {
/* 1472:2165 */       return;
/* 1473:     */     }
/* 1474:2167 */     getAllEmbeddedObjects(patriarch, objects);
/* 1475:     */   }
/* 1476:     */   
/* 1477:     */   private void getAllEmbeddedObjects(HSSFShapeContainer parent, List<HSSFObjectData> objects)
/* 1478:     */   {
/* 1479:2177 */     for (HSSFShape shape : parent.getChildren()) {
/* 1480:2178 */       if ((shape instanceof HSSFObjectData)) {
/* 1481:2179 */         objects.add((HSSFObjectData)shape);
/* 1482:2180 */       } else if ((shape instanceof HSSFShapeContainer)) {
/* 1483:2181 */         getAllEmbeddedObjects((HSSFShapeContainer)shape, objects);
/* 1484:     */       }
/* 1485:     */     }
/* 1486:     */   }
/* 1487:     */   
/* 1488:     */   public HSSFCreationHelper getCreationHelper()
/* 1489:     */   {
/* 1490:2187 */     return new HSSFCreationHelper(this);
/* 1491:     */   }
/* 1492:     */   
/* 1493:     */   UDFFinder getUDFFinder()
/* 1494:     */   {
/* 1495:2198 */     return this._udfFinder;
/* 1496:     */   }
/* 1497:     */   
/* 1498:     */   public void addToolPack(UDFFinder toopack)
/* 1499:     */   {
/* 1500:2208 */     AggregatingUDFFinder udfs = (AggregatingUDFFinder)this._udfFinder;
/* 1501:2209 */     udfs.add(toopack);
/* 1502:     */   }
/* 1503:     */   
/* 1504:     */   public void setForceFormulaRecalculation(boolean value)
/* 1505:     */   {
/* 1506:2231 */     InternalWorkbook iwb = getWorkbook();
/* 1507:2232 */     RecalcIdRecord recalc = iwb.getRecalcId();
/* 1508:2233 */     recalc.setEngineId(0);
/* 1509:     */   }
/* 1510:     */   
/* 1511:     */   public boolean getForceFormulaRecalculation()
/* 1512:     */   {
/* 1513:2243 */     InternalWorkbook iwb = getWorkbook();
/* 1514:2244 */     RecalcIdRecord recalc = (RecalcIdRecord)iwb.findFirstRecordBySid((short)449);
/* 1515:2245 */     return (recalc != null) && (recalc.getEngineId() != 0);
/* 1516:     */   }
/* 1517:     */   
/* 1518:     */   public boolean changeExternalReference(String oldUrl, String newUrl)
/* 1519:     */   {
/* 1520:2258 */     return this.workbook.changeExternalReference(oldUrl, newUrl);
/* 1521:     */   }
/* 1522:     */   
/* 1523:     */   @Deprecated
/* 1524:     */   @Removal(version="3.18")
/* 1525:     */   public DirectoryNode getRootDirectory()
/* 1526:     */   {
/* 1527:2267 */     return getDirectory();
/* 1528:     */   }
/* 1529:     */   
/* 1530:     */   @Internal
/* 1531:     */   public InternalWorkbook getInternalWorkbook()
/* 1532:     */   {
/* 1533:2272 */     return this.workbook;
/* 1534:     */   }
/* 1535:     */   
/* 1536:     */   public SpreadsheetVersion getSpreadsheetVersion()
/* 1537:     */   {
/* 1538:2283 */     return SpreadsheetVersion.EXCEL97;
/* 1539:     */   }
/* 1540:     */   
/* 1541:     */   public EncryptionInfo getEncryptionInfo()
/* 1542:     */   {
/* 1543:2288 */     FilePassRecord fpr = (FilePassRecord)this.workbook.findFirstRecordBySid((short)47);
/* 1544:2289 */     return fpr != null ? fpr.getEncryptionInfo() : null;
/* 1545:     */   }
/* 1546:     */   
/* 1547:     */   private void updateEncryptionInfo()
/* 1548:     */   {
/* 1549:2295 */     readProperties();
/* 1550:2296 */     FilePassRecord fpr = (FilePassRecord)this.workbook.findFirstRecordBySid((short)47);
/* 1551:     */     
/* 1552:2298 */     String password = Biff8EncryptionKey.getCurrentUserPassword();
/* 1553:2299 */     WorkbookRecordList wrl = this.workbook.getWorkbookRecordList();
/* 1554:2300 */     if (password == null)
/* 1555:     */     {
/* 1556:2301 */       if (fpr != null) {
/* 1557:2303 */         wrl.remove(fpr);
/* 1558:     */       }
/* 1559:     */     }
/* 1560:     */     else
/* 1561:     */     {
/* 1562:2307 */       if (fpr == null)
/* 1563:     */       {
/* 1564:2308 */         fpr = new FilePassRecord(EncryptionMode.cryptoAPI);
/* 1565:2309 */         wrl.add(1, fpr);
/* 1566:     */       }
/* 1567:2313 */       EncryptionInfo ei = fpr.getEncryptionInfo();
/* 1568:2314 */       EncryptionVerifier ver = ei.getVerifier();
/* 1569:2315 */       byte[] encVer = ver.getEncryptedVerifier();
/* 1570:2316 */       Decryptor dec = ei.getDecryptor();
/* 1571:2317 */       Encryptor enc = ei.getEncryptor();
/* 1572:     */       try
/* 1573:     */       {
/* 1574:2319 */         if ((encVer == null) || (!dec.verifyPassword(password)))
/* 1575:     */         {
/* 1576:2320 */           enc.confirmPassword(password);
/* 1577:     */         }
/* 1578:     */         else
/* 1579:     */         {
/* 1580:2322 */           byte[] verifier = dec.getVerifier();
/* 1581:2323 */           byte[] salt = ver.getSalt();
/* 1582:2324 */           enc.confirmPassword(password, null, null, verifier, salt, null);
/* 1583:     */         }
/* 1584:     */       }
/* 1585:     */       catch (GeneralSecurityException e)
/* 1586:     */       {
/* 1587:2327 */         throw new EncryptedDocumentException("can't validate/update encryption setting", e);
/* 1588:     */       }
/* 1589:     */     }
/* 1590:     */   }
/* 1591:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFWorkbook

 * JD-Core Version:    0.7.0.1

 */