/*    1:     */ package org.apache.poi.xssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayInputStream;
/*    4:     */ import java.io.ByteArrayOutputStream;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.IOException;
/*    7:     */ import java.io.InputStream;
/*    8:     */ import java.io.OutputStream;
/*    9:     */ import java.net.URI;
/*   10:     */ import java.util.ArrayList;
/*   11:     */ import java.util.Collection;
/*   12:     */ import java.util.Collections;
/*   13:     */ import java.util.HashMap;
/*   14:     */ import java.util.Iterator;
/*   15:     */ import java.util.LinkedList;
/*   16:     */ import java.util.List;
/*   17:     */ import java.util.Locale;
/*   18:     */ import java.util.Map;
/*   19:     */ import java.util.NoSuchElementException;
/*   20:     */ import java.util.regex.Pattern;
/*   21:     */ import javax.xml.namespace.QName;
/*   22:     */ import org.apache.commons.collections4.ListValuedMap;
/*   23:     */ import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
/*   24:     */ import org.apache.poi.POIXMLDocument;
/*   25:     */ import org.apache.poi.POIXMLDocumentPart;
/*   26:     */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*   27:     */ import org.apache.poi.POIXMLException;
/*   28:     */ import org.apache.poi.POIXMLProperties;
/*   29:     */ import org.apache.poi.POIXMLProperties.ExtendedProperties;
/*   30:     */ import org.apache.poi.POIXMLTypeLoader;
/*   31:     */ import org.apache.poi.hpsf.ClassID;
/*   32:     */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   33:     */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   34:     */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   35:     */ import org.apache.poi.openxml4j.opc.PackageAccess;
/*   36:     */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   37:     */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   38:     */ import org.apache.poi.openxml4j.opc.PackageProperties;
/*   39:     */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   40:     */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*   41:     */ import org.apache.poi.openxml4j.opc.TargetMode;
/*   42:     */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*   43:     */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*   44:     */ import org.apache.poi.poifs.filesystem.Ole10Native;
/*   45:     */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*   46:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   47:     */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   48:     */ import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
/*   49:     */ import org.apache.poi.ss.formula.udf.IndexedUDFFinder;
/*   50:     */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   51:     */ import org.apache.poi.ss.usermodel.Name;
/*   52:     */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*   53:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   54:     */ import org.apache.poi.ss.usermodel.SheetVisibility;
/*   55:     */ import org.apache.poi.ss.usermodel.Workbook;
/*   56:     */ import org.apache.poi.ss.util.CellReference;
/*   57:     */ import org.apache.poi.ss.util.WorkbookUtil;
/*   58:     */ import org.apache.poi.util.IOUtils;
/*   59:     */ import org.apache.poi.util.Internal;
/*   60:     */ import org.apache.poi.util.NotImplemented;
/*   61:     */ import org.apache.poi.util.POILogFactory;
/*   62:     */ import org.apache.poi.util.POILogger;
/*   63:     */ import org.apache.poi.util.PackageHelper;
/*   64:     */ import org.apache.poi.util.Removal;
/*   65:     */ import org.apache.poi.xssf.XLSBUnsupportedException;
/*   66:     */ import org.apache.poi.xssf.model.CalculationChain;
/*   67:     */ import org.apache.poi.xssf.model.ExternalLinksTable;
/*   68:     */ import org.apache.poi.xssf.model.MapInfo;
/*   69:     */ import org.apache.poi.xssf.model.SharedStringsTable;
/*   70:     */ import org.apache.poi.xssf.model.StylesTable;
/*   71:     */ import org.apache.poi.xssf.model.ThemesTable;
/*   72:     */ import org.apache.poi.xssf.usermodel.helpers.XSSFFormulaUtils;
/*   73:     */ import org.apache.poi.xssf.usermodel.helpers.XSSFPasswordHelper;
/*   74:     */ import org.apache.xmlbeans.SchemaType;
/*   75:     */ import org.apache.xmlbeans.XmlException;
/*   76:     */ import org.apache.xmlbeans.XmlObject;
/*   77:     */ import org.apache.xmlbeans.XmlOptions;
/*   78:     */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing;
/*   79:     */ import org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties;
/*   80:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView;
/*   81:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews;
/*   82:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain;
/*   83:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr;
/*   84:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
/*   85:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName.Factory;
/*   86:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames;
/*   87:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames.Factory;
/*   88:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet;
/*   89:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference;
/*   90:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences;
/*   91:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache;
/*   92:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches;
/*   93:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
/*   94:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets;
/*   95:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
/*   96:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook.Factory;
/*   97:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
/*   98:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection;
/*   99:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  100:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode;
/*  101:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState;
/*  102:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum;
/*  103:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
/*  104:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument.Factory;
/*  105:     */ 
/*  106:     */ public class XSSFWorkbook
/*  107:     */   extends POIXMLDocument
/*  108:     */   implements Workbook
/*  109:     */ {
/*  110: 123 */   private static final Pattern COMMA_PATTERN = Pattern.compile(",");
/*  111:     */   /**
/*  112:     */    * @deprecated
/*  113:     */    */
/*  114:     */   @Removal(version="3.19")
/*  115:     */   public static final float DEFAULT_CHARACTER_WIDTH = 7.0017F;
/*  116:     */   private static final int MAX_SENSITIVE_SHEET_NAME_LEN = 31;
/*  117:     */   public static final int PICTURE_TYPE_GIF = 8;
/*  118:     */   public static final int PICTURE_TYPE_TIFF = 9;
/*  119:     */   public static final int PICTURE_TYPE_EPS = 10;
/*  120:     */   public static final int PICTURE_TYPE_BMP = 11;
/*  121:     */   public static final int PICTURE_TYPE_WPG = 12;
/*  122:     */   private CTWorkbook workbook;
/*  123:     */   private List<XSSFSheet> sheets;
/*  124:     */   private ListValuedMap<String, XSSFName> namedRangesByName;
/*  125:     */   private List<XSSFName> namedRanges;
/*  126:     */   private SharedStringsTable sharedStringSource;
/*  127:     */   private StylesTable stylesSource;
/*  128: 183 */   private IndexedUDFFinder _udfFinder = new IndexedUDFFinder(new UDFFinder[] { AggregatingUDFFinder.DEFAULT });
/*  129:     */   private CalculationChain calcChain;
/*  130:     */   private List<ExternalLinksTable> externalLinks;
/*  131:     */   private MapInfo mapInfo;
/*  132:     */   private XSSFDataFormat formatter;
/*  133: 213 */   private Row.MissingCellPolicy _missingCellPolicy = Row.MissingCellPolicy.RETURN_NULL_AND_BLANK;
/*  134:     */   private List<XSSFPictureData> pictures;
/*  135: 220 */   private static POILogger logger = POILogFactory.getLogger(XSSFWorkbook.class);
/*  136:     */   private XSSFCreationHelper _creationHelper;
/*  137:     */   private List<XSSFPivotTable> pivotTables;
/*  138:     */   private List<CTPivotCache> pivotCaches;
/*  139:     */   
/*  140:     */   public XSSFWorkbook()
/*  141:     */   {
/*  142: 239 */     this(XSSFWorkbookType.XLSX);
/*  143:     */   }
/*  144:     */   
/*  145:     */   public XSSFWorkbook(XSSFWorkbookType workbookType)
/*  146:     */   {
/*  147: 247 */     super(newPackage(workbookType));
/*  148: 248 */     onWorkbookCreate();
/*  149:     */   }
/*  150:     */   
/*  151:     */   public XSSFWorkbook(OPCPackage pkg)
/*  152:     */     throws IOException
/*  153:     */   {
/*  154: 265 */     super(pkg);
/*  155:     */     
/*  156: 267 */     beforeDocumentRead();
/*  157:     */     
/*  158:     */ 
/*  159: 270 */     load(XSSFFactory.getInstance());
/*  160: 273 */     if (!this.workbook.isSetBookViews())
/*  161:     */     {
/*  162: 274 */       CTBookViews bvs = this.workbook.addNewBookViews();
/*  163: 275 */       CTBookView bv = bvs.addNewWorkbookView();
/*  164: 276 */       bv.setActiveTab(0L);
/*  165:     */     }
/*  166:     */   }
/*  167:     */   
/*  168:     */   public XSSFWorkbook(InputStream is)
/*  169:     */     throws IOException
/*  170:     */   {
/*  171: 295 */     super(PackageHelper.open(is));
/*  172:     */     
/*  173: 297 */     beforeDocumentRead();
/*  174:     */     
/*  175:     */ 
/*  176: 300 */     load(XSSFFactory.getInstance());
/*  177: 303 */     if (!this.workbook.isSetBookViews())
/*  178:     */     {
/*  179: 304 */       CTBookViews bvs = this.workbook.addNewBookViews();
/*  180: 305 */       CTBookView bv = bvs.addNewWorkbookView();
/*  181: 306 */       bv.setActiveTab(0L);
/*  182:     */     }
/*  183:     */   }
/*  184:     */   
/*  185:     */   public XSSFWorkbook(File file)
/*  186:     */     throws IOException, InvalidFormatException
/*  187:     */   {
/*  188: 323 */     this(OPCPackage.open(file));
/*  189:     */   }
/*  190:     */   
/*  191:     */   public XSSFWorkbook(String path)
/*  192:     */     throws IOException
/*  193:     */   {
/*  194: 340 */     this(openPackage(path));
/*  195:     */   }
/*  196:     */   
/*  197:     */   protected void beforeDocumentRead()
/*  198:     */   {
/*  199: 345 */     if (getCorePart().getContentType().equals(XSSFRelation.XLSB_BINARY_WORKBOOK.getContentType())) {
/*  200: 346 */       throw new XLSBUnsupportedException();
/*  201:     */     }
/*  202: 350 */     this.pivotTables = new ArrayList();
/*  203: 351 */     this.pivotCaches = new ArrayList();
/*  204:     */   }
/*  205:     */   
/*  206:     */   protected void onDocumentRead()
/*  207:     */     throws IOException
/*  208:     */   {
/*  209:     */     try
/*  210:     */     {
/*  211: 357 */       WorkbookDocument doc = WorkbookDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  212: 358 */       this.workbook = doc.getWorkbook();
/*  213:     */       
/*  214: 360 */       ThemesTable theme = null;
/*  215: 361 */       Map<String, XSSFSheet> shIdMap = new HashMap();
/*  216: 362 */       Map<String, ExternalLinksTable> elIdMap = new HashMap();
/*  217: 363 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  218:     */       {
/*  219: 364 */         POIXMLDocumentPart p = rp.getDocumentPart();
/*  220: 365 */         if ((p instanceof SharedStringsTable)) {
/*  221: 366 */           this.sharedStringSource = ((SharedStringsTable)p);
/*  222: 367 */         } else if ((p instanceof StylesTable)) {
/*  223: 368 */           this.stylesSource = ((StylesTable)p);
/*  224: 369 */         } else if ((p instanceof ThemesTable)) {
/*  225: 370 */           theme = (ThemesTable)p;
/*  226: 371 */         } else if ((p instanceof CalculationChain)) {
/*  227: 372 */           this.calcChain = ((CalculationChain)p);
/*  228: 373 */         } else if ((p instanceof MapInfo)) {
/*  229: 374 */           this.mapInfo = ((MapInfo)p);
/*  230: 375 */         } else if ((p instanceof XSSFSheet)) {
/*  231: 376 */           shIdMap.put(rp.getRelationship().getId(), (XSSFSheet)p);
/*  232: 377 */         } else if ((p instanceof ExternalLinksTable)) {
/*  233: 378 */           elIdMap.put(rp.getRelationship().getId(), (ExternalLinksTable)p);
/*  234:     */         }
/*  235:     */       }
/*  236: 381 */       boolean packageReadOnly = getPackage().getPackageAccess() == PackageAccess.READ;
/*  237: 383 */       if (this.stylesSource == null) {
/*  238: 385 */         if (packageReadOnly) {
/*  239: 386 */           this.stylesSource = new StylesTable();
/*  240:     */         } else {
/*  241: 388 */           this.stylesSource = ((StylesTable)createRelationship(XSSFRelation.STYLES, XSSFFactory.getInstance()));
/*  242:     */         }
/*  243:     */       }
/*  244: 391 */       this.stylesSource.setWorkbook(this);
/*  245: 392 */       this.stylesSource.setTheme(theme);
/*  246: 394 */       if (this.sharedStringSource == null) {
/*  247: 396 */         if (packageReadOnly) {
/*  248: 397 */           this.sharedStringSource = new SharedStringsTable();
/*  249:     */         } else {
/*  250: 399 */           this.sharedStringSource = ((SharedStringsTable)createRelationship(XSSFRelation.SHARED_STRINGS, XSSFFactory.getInstance()));
/*  251:     */         }
/*  252:     */       }
/*  253: 405 */       this.sheets = new ArrayList(shIdMap.size());
/*  254: 407 */       for (CTSheet ctSheet : this.workbook.getSheets().getSheetArray()) {
/*  255: 408 */         parseSheet(shIdMap, ctSheet);
/*  256:     */       }
/*  257: 413 */       this.externalLinks = new ArrayList(elIdMap.size());
/*  258: 414 */       if (this.workbook.isSetExternalReferences()) {
/*  259: 415 */         for (CTExternalReference er : this.workbook.getExternalReferences().getExternalReferenceArray())
/*  260:     */         {
/*  261: 416 */           ExternalLinksTable el = (ExternalLinksTable)elIdMap.get(er.getId());
/*  262: 417 */           if (el == null) {
/*  263: 418 */             logger.log(5, new Object[] { "ExternalLinksTable with r:id " + er.getId() + " was defined, but didn't exist in package, skipping" });
/*  264:     */           } else {
/*  265: 421 */             this.externalLinks.add(el);
/*  266:     */           }
/*  267:     */         }
/*  268:     */       }
/*  269: 426 */       reprocessNamedRanges();
/*  270:     */     }
/*  271:     */     catch (XmlException e)
/*  272:     */     {
/*  273: 428 */       throw new POIXMLException(e);
/*  274:     */     }
/*  275:     */   }
/*  276:     */   
/*  277:     */   public void parseSheet(Map<String, XSSFSheet> shIdMap, CTSheet ctSheet)
/*  278:     */   {
/*  279: 437 */     XSSFSheet sh = (XSSFSheet)shIdMap.get(ctSheet.getId());
/*  280: 438 */     if (sh == null)
/*  281:     */     {
/*  282: 439 */       logger.log(5, new Object[] { "Sheet with name " + ctSheet.getName() + " and r:id " + ctSheet.getId() + " was defined, but didn't exist in package, skipping" });
/*  283: 440 */       return;
/*  284:     */     }
/*  285: 442 */     sh.sheet = ctSheet;
/*  286: 443 */     sh.onDocumentRead();
/*  287: 444 */     this.sheets.add(sh);
/*  288:     */   }
/*  289:     */   
/*  290:     */   private void onWorkbookCreate()
/*  291:     */   {
/*  292: 451 */     this.workbook = CTWorkbook.Factory.newInstance();
/*  293:     */     
/*  294:     */ 
/*  295: 454 */     CTWorkbookPr workbookPr = this.workbook.addNewWorkbookPr();
/*  296: 455 */     workbookPr.setDate1904(false);
/*  297:     */     
/*  298: 457 */     CTBookViews bvs = this.workbook.addNewBookViews();
/*  299: 458 */     CTBookView bv = bvs.addNewWorkbookView();
/*  300: 459 */     bv.setActiveTab(0L);
/*  301: 460 */     this.workbook.addNewSheets();
/*  302:     */     
/*  303: 462 */     POIXMLProperties.ExtendedProperties expProps = getProperties().getExtendedProperties();
/*  304: 463 */     expProps.getUnderlyingProperties().setApplication("Apache POI");
/*  305:     */     
/*  306: 465 */     this.sharedStringSource = ((SharedStringsTable)createRelationship(XSSFRelation.SHARED_STRINGS, XSSFFactory.getInstance()));
/*  307: 466 */     this.stylesSource = ((StylesTable)createRelationship(XSSFRelation.STYLES, XSSFFactory.getInstance()));
/*  308: 467 */     this.stylesSource.setWorkbook(this);
/*  309:     */     
/*  310: 469 */     this.namedRanges = new ArrayList();
/*  311: 470 */     this.namedRangesByName = new ArrayListValuedHashMap();
/*  312: 471 */     this.sheets = new ArrayList();
/*  313: 472 */     this.pivotTables = new ArrayList();
/*  314:     */   }
/*  315:     */   
/*  316:     */   protected static OPCPackage newPackage(XSSFWorkbookType workbookType)
/*  317:     */   {
/*  318:     */     try
/*  319:     */     {
/*  320: 480 */       OPCPackage pkg = OPCPackage.create(new ByteArrayOutputStream());
/*  321:     */       
/*  322: 482 */       PackagePartName corePartName = PackagingURIHelper.createPartName(XSSFRelation.WORKBOOK.getDefaultFileName());
/*  323:     */       
/*  324: 484 */       pkg.addRelationship(corePartName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
/*  325:     */       
/*  326: 486 */       pkg.createPart(corePartName, workbookType.getContentType());
/*  327:     */       
/*  328: 488 */       pkg.getPackageProperties().setCreatorProperty("Apache POI");
/*  329:     */       
/*  330: 490 */       return pkg;
/*  331:     */     }
/*  332:     */     catch (Exception e)
/*  333:     */     {
/*  334: 492 */       throw new POIXMLException(e);
/*  335:     */     }
/*  336:     */   }
/*  337:     */   
/*  338:     */   @Internal
/*  339:     */   public CTWorkbook getCTWorkbook()
/*  340:     */   {
/*  341: 503 */     return this.workbook;
/*  342:     */   }
/*  343:     */   
/*  344:     */   public int addPicture(byte[] pictureData, int format)
/*  345:     */   {
/*  346: 523 */     int imageNumber = getAllPictures().size() + 1;
/*  347: 524 */     XSSFPictureData img = (XSSFPictureData)createRelationship(XSSFPictureData.RELATIONS[format], XSSFFactory.getInstance(), imageNumber, true).getDocumentPart();
/*  348:     */     try
/*  349:     */     {
/*  350: 526 */       OutputStream out = img.getPackagePart().getOutputStream();
/*  351: 527 */       out.write(pictureData);
/*  352: 528 */       out.close();
/*  353:     */     }
/*  354:     */     catch (IOException e)
/*  355:     */     {
/*  356: 530 */       throw new POIXMLException(e);
/*  357:     */     }
/*  358: 532 */     this.pictures.add(img);
/*  359: 533 */     return imageNumber - 1;
/*  360:     */   }
/*  361:     */   
/*  362:     */   public int addPicture(InputStream is, int format)
/*  363:     */     throws IOException
/*  364:     */   {
/*  365: 552 */     int imageNumber = getAllPictures().size() + 1;
/*  366: 553 */     XSSFPictureData img = (XSSFPictureData)createRelationship(XSSFPictureData.RELATIONS[format], XSSFFactory.getInstance(), imageNumber, true).getDocumentPart();
/*  367: 554 */     OutputStream out = img.getPackagePart().getOutputStream();
/*  368: 555 */     IOUtils.copy(is, out);
/*  369: 556 */     out.close();
/*  370: 557 */     this.pictures.add(img);
/*  371: 558 */     return imageNumber - 1;
/*  372:     */   }
/*  373:     */   
/*  374:     */   public XSSFSheet cloneSheet(int sheetNum)
/*  375:     */   {
/*  376: 572 */     return cloneSheet(sheetNum, null);
/*  377:     */   }
/*  378:     */   
/*  379:     */   public XSSFSheet cloneSheet(int sheetNum, String newName)
/*  380:     */   {
/*  381: 588 */     validateSheetIndex(sheetNum);
/*  382: 589 */     XSSFSheet srcSheet = (XSSFSheet)this.sheets.get(sheetNum);
/*  383: 591 */     if (newName == null)
/*  384:     */     {
/*  385: 592 */       String srcName = srcSheet.getSheetName();
/*  386: 593 */       newName = getUniqueSheetName(srcName);
/*  387:     */     }
/*  388:     */     else
/*  389:     */     {
/*  390: 595 */       validateSheetName(newName);
/*  391:     */     }
/*  392: 598 */     XSSFSheet clonedSheet = createSheet(newName);
/*  393:     */     
/*  394:     */ 
/*  395: 601 */     List<POIXMLDocumentPart.RelationPart> rels = srcSheet.getRelationParts();
/*  396:     */     
/*  397: 603 */     XSSFDrawing dg = null;
/*  398: 604 */     for (POIXMLDocumentPart.RelationPart rp : rels)
/*  399:     */     {
/*  400: 605 */       POIXMLDocumentPart r = rp.getDocumentPart();
/*  401: 607 */       if ((r instanceof XSSFDrawing)) {
/*  402: 608 */         dg = (XSSFDrawing)r;
/*  403:     */       } else {
/*  404: 612 */         addRelation(rp, clonedSheet);
/*  405:     */       }
/*  406:     */     }
/*  407:     */     try
/*  408:     */     {
/*  409: 616 */       for (PackageRelationship pr : srcSheet.getPackagePart().getRelationships()) {
/*  410: 617 */         if (pr.getTargetMode() == TargetMode.EXTERNAL) {
/*  411: 618 */           clonedSheet.getPackagePart().addExternalRelationship(pr.getTargetURI().toASCIIString(), pr.getRelationshipType(), pr.getId());
/*  412:     */         }
/*  413:     */       }
/*  414:     */     }
/*  415:     */     catch (InvalidFormatException e)
/*  416:     */     {
/*  417: 623 */       throw new POIXMLException("Failed to clone sheet", e);
/*  418:     */     }
/*  419:     */     try
/*  420:     */     {
/*  421: 628 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  422: 629 */       srcSheet.write(out);
/*  423: 630 */       clonedSheet.read(new ByteArrayInputStream(out.toByteArray()));
/*  424:     */     }
/*  425:     */     catch (IOException e)
/*  426:     */     {
/*  427: 632 */       throw new POIXMLException("Failed to clone sheet", e);
/*  428:     */     }
/*  429: 634 */     CTWorksheet ct = clonedSheet.getCTWorksheet();
/*  430: 635 */     if (ct.isSetLegacyDrawing())
/*  431:     */     {
/*  432: 636 */       logger.log(5, new Object[] { "Cloning sheets with comments is not yet supported." });
/*  433: 637 */       ct.unsetLegacyDrawing();
/*  434:     */     }
/*  435: 639 */     if (ct.isSetPageSetup())
/*  436:     */     {
/*  437: 640 */       logger.log(5, new Object[] { "Cloning sheets with page setup is not yet supported." });
/*  438: 641 */       ct.unsetPageSetup();
/*  439:     */     }
/*  440: 644 */     clonedSheet.setSelected(false);
/*  441:     */     XSSFDrawing clonedDg;
/*  442: 647 */     if (dg != null)
/*  443:     */     {
/*  444: 648 */       if (ct.isSetDrawing()) {
/*  445: 651 */         ct.unsetDrawing();
/*  446:     */       }
/*  447: 653 */       clonedDg = clonedSheet.createDrawingPatriarch();
/*  448:     */       
/*  449: 655 */       clonedDg.getCTDrawing().set(dg.getCTDrawing());
/*  450:     */       
/*  451: 657 */       clonedDg = clonedSheet.createDrawingPatriarch();
/*  452:     */       
/*  453:     */ 
/*  454: 660 */       List<POIXMLDocumentPart.RelationPart> srcRels = srcSheet.createDrawingPatriarch().getRelationParts();
/*  455: 661 */       for (POIXMLDocumentPart.RelationPart rp : srcRels) {
/*  456: 662 */         addRelation(rp, clonedDg);
/*  457:     */       }
/*  458:     */     }
/*  459: 665 */     return clonedSheet;
/*  460:     */   }
/*  461:     */   
/*  462:     */   private static void addRelation(POIXMLDocumentPart.RelationPart rp, POIXMLDocumentPart target)
/*  463:     */   {
/*  464: 672 */     PackageRelationship rel = rp.getRelationship();
/*  465: 673 */     if (rel.getTargetMode() == TargetMode.EXTERNAL)
/*  466:     */     {
/*  467: 674 */       target.getPackagePart().addRelationship(rel.getTargetURI(), rel.getTargetMode(), rel.getRelationshipType(), rel.getId());
/*  468:     */     }
/*  469:     */     else
/*  470:     */     {
/*  471: 677 */       XSSFRelation xssfRel = XSSFRelation.getInstance(rel.getRelationshipType());
/*  472: 678 */       if (xssfRel == null) {
/*  473: 680 */         throw new POIXMLException("Can't clone sheet - unknown relation type found: " + rel.getRelationshipType());
/*  474:     */       }
/*  475: 682 */       target.addRelation(rel.getId(), xssfRel, rp.getDocumentPart());
/*  476:     */     }
/*  477:     */   }
/*  478:     */   
/*  479:     */   private String getUniqueSheetName(String srcName)
/*  480:     */   {
/*  481: 693 */     int uniqueIndex = 2;
/*  482: 694 */     String baseName = srcName;
/*  483: 695 */     int bracketPos = srcName.lastIndexOf('(');
/*  484: 696 */     if ((bracketPos > 0) && (srcName.endsWith(")")))
/*  485:     */     {
/*  486: 697 */       String suffix = srcName.substring(bracketPos + 1, srcName.length() - ")".length());
/*  487:     */       try
/*  488:     */       {
/*  489: 699 */         uniqueIndex = Integer.parseInt(suffix.trim());
/*  490: 700 */         uniqueIndex++;
/*  491: 701 */         baseName = srcName.substring(0, bracketPos).trim();
/*  492:     */       }
/*  493:     */       catch (NumberFormatException e) {}
/*  494:     */     }
/*  495:     */     for (;;)
/*  496:     */     {
/*  497: 708 */       String index = Integer.toString(uniqueIndex++);
/*  498:     */       String name;
/*  499:     */       String name;
/*  500: 710 */       if (baseName.length() + index.length() + 2 < 31) {
/*  501: 711 */         name = baseName + " (" + index + ")";
/*  502:     */       } else {
/*  503: 713 */         name = baseName.substring(0, 31 - index.length() - 2) + "(" + index + ")";
/*  504:     */       }
/*  505: 717 */       if (getSheetIndex(name) == -1) {
/*  506: 718 */         return name;
/*  507:     */       }
/*  508:     */     }
/*  509:     */   }
/*  510:     */   
/*  511:     */   public XSSFCellStyle createCellStyle()
/*  512:     */   {
/*  513: 730 */     return this.stylesSource.createCellStyle();
/*  514:     */   }
/*  515:     */   
/*  516:     */   public XSSFDataFormat createDataFormat()
/*  517:     */   {
/*  518: 741 */     if (this.formatter == null) {
/*  519: 742 */       this.formatter = new XSSFDataFormat(this.stylesSource);
/*  520:     */     }
/*  521: 744 */     return this.formatter;
/*  522:     */   }
/*  523:     */   
/*  524:     */   public XSSFFont createFont()
/*  525:     */   {
/*  526: 754 */     XSSFFont font = new XSSFFont();
/*  527: 755 */     font.registerTo(this.stylesSource);
/*  528: 756 */     return font;
/*  529:     */   }
/*  530:     */   
/*  531:     */   public XSSFName createName()
/*  532:     */   {
/*  533: 761 */     CTDefinedName ctName = CTDefinedName.Factory.newInstance();
/*  534: 762 */     ctName.setName("");
/*  535: 763 */     return createAndStoreName(ctName);
/*  536:     */   }
/*  537:     */   
/*  538:     */   private XSSFName createAndStoreName(CTDefinedName ctName)
/*  539:     */   {
/*  540: 767 */     XSSFName name = new XSSFName(ctName, this);
/*  541: 768 */     this.namedRanges.add(name);
/*  542: 769 */     this.namedRangesByName.put(ctName.getName().toLowerCase(Locale.ENGLISH), name);
/*  543: 770 */     return name;
/*  544:     */   }
/*  545:     */   
/*  546:     */   public XSSFSheet createSheet()
/*  547:     */   {
/*  548: 781 */     String sheetname = "Sheet" + this.sheets.size();
/*  549: 782 */     int idx = 0;
/*  550: 783 */     while (getSheet(sheetname) != null)
/*  551:     */     {
/*  552: 784 */       sheetname = "Sheet" + idx;
/*  553: 785 */       idx++;
/*  554:     */     }
/*  555: 787 */     return createSheet(sheetname);
/*  556:     */   }
/*  557:     */   
/*  558:     */   public XSSFSheet createSheet(String sheetname)
/*  559:     */   {
/*  560: 839 */     if (sheetname == null) {
/*  561: 840 */       throw new IllegalArgumentException("sheetName must not be null");
/*  562:     */     }
/*  563: 843 */     validateSheetName(sheetname);
/*  564: 846 */     if (sheetname.length() > 31) {
/*  565: 847 */       sheetname = sheetname.substring(0, 31);
/*  566:     */     }
/*  567: 849 */     WorkbookUtil.validateSheetName(sheetname);
/*  568:     */     
/*  569: 851 */     CTSheet sheet = addSheet(sheetname);
/*  570:     */     
/*  571: 853 */     int sheetNumber = 1;
/*  572: 856 */     for (XSSFSheet sh : this.sheets) {
/*  573: 857 */       sheetNumber = (int)Math.max(sh.sheet.getSheetId() + 1L, sheetNumber);
/*  574:     */     }
/*  575: 862 */     String sheetName = XSSFRelation.WORKSHEET.getFileName(sheetNumber);
/*  576: 863 */     Iterator i$ = getRelations().iterator();
/*  577:     */     for (;;)
/*  578:     */     {
/*  579: 863 */       if (!i$.hasNext()) {
/*  580:     */         break label181;
/*  581:     */       }
/*  582: 863 */       POIXMLDocumentPart relation = (POIXMLDocumentPart)i$.next();
/*  583: 864 */       if ((relation.getPackagePart() != null) && (sheetName.equals(relation.getPackagePart().getPartName().getName())))
/*  584:     */       {
/*  585: 867 */         sheetNumber++;
/*  586: 868 */         break;
/*  587:     */       }
/*  588:     */     }
/*  589:     */     label181:
/*  590: 876 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.WORKSHEET, XSSFFactory.getInstance(), sheetNumber, false);
/*  591: 877 */     XSSFSheet wrapper = (XSSFSheet)rp.getDocumentPart();
/*  592: 878 */     wrapper.sheet = sheet;
/*  593: 879 */     sheet.setId(rp.getRelationship().getId());
/*  594: 880 */     sheet.setSheetId(sheetNumber);
/*  595: 881 */     if (this.sheets.isEmpty()) {
/*  596: 882 */       wrapper.setSelected(true);
/*  597:     */     }
/*  598: 884 */     this.sheets.add(wrapper);
/*  599: 885 */     return wrapper;
/*  600:     */   }
/*  601:     */   
/*  602:     */   private void validateSheetName(String sheetName)
/*  603:     */     throws IllegalArgumentException
/*  604:     */   {
/*  605: 889 */     if (containsSheet(sheetName, this.sheets.size())) {
/*  606: 890 */       throw new IllegalArgumentException("The workbook already contains a sheet named '" + sheetName + "'");
/*  607:     */     }
/*  608:     */   }
/*  609:     */   
/*  610:     */   protected XSSFDialogsheet createDialogsheet(String sheetname, CTDialogsheet dialogsheet)
/*  611:     */   {
/*  612: 895 */     XSSFSheet sheet = createSheet(sheetname);
/*  613: 896 */     return new XSSFDialogsheet(sheet);
/*  614:     */   }
/*  615:     */   
/*  616:     */   private CTSheet addSheet(String sheetname)
/*  617:     */   {
/*  618: 900 */     CTSheet sheet = this.workbook.getSheets().addNewSheet();
/*  619: 901 */     sheet.setName(sheetname);
/*  620: 902 */     return sheet;
/*  621:     */   }
/*  622:     */   
/*  623:     */   public XSSFFont findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline)
/*  624:     */   {
/*  625: 910 */     return this.stylesSource.findFont(bold, color, fontHeight, name, italic, strikeout, typeOffset, underline);
/*  626:     */   }
/*  627:     */   
/*  628:     */   public int getActiveSheetIndex()
/*  629:     */   {
/*  630: 922 */     return (int)this.workbook.getBookViews().getWorkbookViewArray(0).getActiveTab();
/*  631:     */   }
/*  632:     */   
/*  633:     */   public List<XSSFPictureData> getAllPictures()
/*  634:     */   {
/*  635: 933 */     if (this.pictures == null)
/*  636:     */     {
/*  637: 934 */       List<PackagePart> mediaParts = getPackage().getPartsByName(Pattern.compile("/xl/media/.*?"));
/*  638: 935 */       this.pictures = new ArrayList(mediaParts.size());
/*  639: 936 */       for (PackagePart part : mediaParts) {
/*  640: 937 */         this.pictures.add(new XSSFPictureData(part));
/*  641:     */       }
/*  642:     */     }
/*  643: 940 */     return this.pictures;
/*  644:     */   }
/*  645:     */   
/*  646:     */   public XSSFCellStyle getCellStyleAt(int idx)
/*  647:     */   {
/*  648: 951 */     return this.stylesSource.getStyleAt(idx);
/*  649:     */   }
/*  650:     */   
/*  651:     */   public XSSFFont getFontAt(short idx)
/*  652:     */   {
/*  653: 962 */     return this.stylesSource.getFontAt(idx);
/*  654:     */   }
/*  655:     */   
/*  656:     */   public XSSFName getName(String name)
/*  657:     */   {
/*  658: 976 */     Collection<XSSFName> list = getNames(name);
/*  659: 977 */     if (list.isEmpty()) {
/*  660: 978 */       return null;
/*  661:     */     }
/*  662: 980 */     return (XSSFName)list.iterator().next();
/*  663:     */   }
/*  664:     */   
/*  665:     */   public List<XSSFName> getNames(String name)
/*  666:     */   {
/*  667: 993 */     return Collections.unmodifiableList(this.namedRangesByName.get(name.toLowerCase(Locale.ENGLISH)));
/*  668:     */   }
/*  669:     */   
/*  670:     */   @Deprecated
/*  671:     */   public XSSFName getNameAt(int nameIndex)
/*  672:     */   {
/*  673:1007 */     int nNames = this.namedRanges.size();
/*  674:1008 */     if (nNames < 1) {
/*  675:1009 */       throw new IllegalStateException("There are no defined names in this workbook");
/*  676:     */     }
/*  677:1011 */     if ((nameIndex < 0) || (nameIndex > nNames)) {
/*  678:1012 */       throw new IllegalArgumentException("Specified name index " + nameIndex + " is outside the allowable range (0.." + (nNames - 1) + ").");
/*  679:     */     }
/*  680:1015 */     return (XSSFName)this.namedRanges.get(nameIndex);
/*  681:     */   }
/*  682:     */   
/*  683:     */   public List<XSSFName> getAllNames()
/*  684:     */   {
/*  685:1025 */     return Collections.unmodifiableList(this.namedRanges);
/*  686:     */   }
/*  687:     */   
/*  688:     */   @Deprecated
/*  689:     */   public int getNameIndex(String name)
/*  690:     */   {
/*  691:1040 */     XSSFName nm = getName(name);
/*  692:1041 */     if (nm != null) {
/*  693:1042 */       return this.namedRanges.indexOf(nm);
/*  694:     */     }
/*  695:1044 */     return -1;
/*  696:     */   }
/*  697:     */   
/*  698:     */   public int getNumCellStyles()
/*  699:     */   {
/*  700:1054 */     return this.stylesSource.getNumCellStyles();
/*  701:     */   }
/*  702:     */   
/*  703:     */   public short getNumberOfFonts()
/*  704:     */   {
/*  705:1064 */     return (short)this.stylesSource.getFonts().size();
/*  706:     */   }
/*  707:     */   
/*  708:     */   public int getNumberOfNames()
/*  709:     */   {
/*  710:1074 */     return this.namedRanges.size();
/*  711:     */   }
/*  712:     */   
/*  713:     */   public int getNumberOfSheets()
/*  714:     */   {
/*  715:1084 */     return this.sheets.size();
/*  716:     */   }
/*  717:     */   
/*  718:     */   public String getPrintArea(int sheetIndex)
/*  719:     */   {
/*  720:1094 */     XSSFName name = getBuiltInName("_xlnm.Print_Area", sheetIndex);
/*  721:1095 */     if (name == null) {
/*  722:1096 */       return null;
/*  723:     */     }
/*  724:1099 */     return name.getRefersToFormula();
/*  725:     */   }
/*  726:     */   
/*  727:     */   public XSSFSheet getSheet(String name)
/*  728:     */   {
/*  729:1111 */     for (XSSFSheet sheet : this.sheets) {
/*  730:1112 */       if (name.equalsIgnoreCase(sheet.getSheetName())) {
/*  731:1113 */         return sheet;
/*  732:     */       }
/*  733:     */     }
/*  734:1116 */     return null;
/*  735:     */   }
/*  736:     */   
/*  737:     */   public XSSFSheet getSheetAt(int index)
/*  738:     */   {
/*  739:1129 */     validateSheetIndex(index);
/*  740:1130 */     return (XSSFSheet)this.sheets.get(index);
/*  741:     */   }
/*  742:     */   
/*  743:     */   public int getSheetIndex(String name)
/*  744:     */   {
/*  745:1141 */     int idx = 0;
/*  746:1142 */     for (XSSFSheet sh : this.sheets)
/*  747:     */     {
/*  748:1143 */       if (name.equalsIgnoreCase(sh.getSheetName())) {
/*  749:1144 */         return idx;
/*  750:     */       }
/*  751:1146 */       idx++;
/*  752:     */     }
/*  753:1148 */     return -1;
/*  754:     */   }
/*  755:     */   
/*  756:     */   public int getSheetIndex(Sheet sheet)
/*  757:     */   {
/*  758:1159 */     int idx = 0;
/*  759:1160 */     for (XSSFSheet sh : this.sheets)
/*  760:     */     {
/*  761:1161 */       if (sh == sheet) {
/*  762:1162 */         return idx;
/*  763:     */       }
/*  764:1164 */       idx++;
/*  765:     */     }
/*  766:1166 */     return -1;
/*  767:     */   }
/*  768:     */   
/*  769:     */   public String getSheetName(int sheetIx)
/*  770:     */   {
/*  771:1177 */     validateSheetIndex(sheetIx);
/*  772:1178 */     return ((XSSFSheet)this.sheets.get(sheetIx)).getSheetName();
/*  773:     */   }
/*  774:     */   
/*  775:     */   public Iterator<Sheet> sheetIterator()
/*  776:     */   {
/*  777:1192 */     return new SheetIterator();
/*  778:     */   }
/*  779:     */   
/*  780:     */   public Iterator<Sheet> iterator()
/*  781:     */   {
/*  782:1206 */     return sheetIterator();
/*  783:     */   }
/*  784:     */   
/*  785:     */   private final class SheetIterator<T extends Sheet>
/*  786:     */     implements Iterator<T>
/*  787:     */   {
/*  788:     */     private final Iterator<T> it;
/*  789:     */     
/*  790:     */     public SheetIterator()
/*  791:     */     {
/*  792:1213 */       this.it = XSSFWorkbook.this.sheets.iterator();
/*  793:     */     }
/*  794:     */     
/*  795:     */     public boolean hasNext()
/*  796:     */     {
/*  797:1217 */       return this.it.hasNext();
/*  798:     */     }
/*  799:     */     
/*  800:     */     public T next()
/*  801:     */       throws NoSuchElementException
/*  802:     */     {
/*  803:1221 */       return (Sheet)this.it.next();
/*  804:     */     }
/*  805:     */     
/*  806:     */     public void remove()
/*  807:     */       throws IllegalStateException
/*  808:     */     {
/*  809:1230 */       throw new UnsupportedOperationException("remove method not supported on XSSFWorkbook.iterator(). Use Sheet.removeSheetAt(int) instead.");
/*  810:     */     }
/*  811:     */   }
/*  812:     */   
/*  813:     */   public boolean isMacroEnabled()
/*  814:     */   {
/*  815:1240 */     return getPackagePart().getContentType().equals(XSSFRelation.MACROS_WORKBOOK.getContentType());
/*  816:     */   }
/*  817:     */   
/*  818:     */   @Deprecated
/*  819:     */   public void removeName(int nameIndex)
/*  820:     */   {
/*  821:1253 */     removeName(getNameAt(nameIndex));
/*  822:     */   }
/*  823:     */   
/*  824:     */   @Deprecated
/*  825:     */   public void removeName(String name)
/*  826:     */   {
/*  827:1272 */     List<XSSFName> names = this.namedRangesByName.get(name.toLowerCase(Locale.ENGLISH));
/*  828:1273 */     if (names.isEmpty()) {
/*  829:1274 */       throw new IllegalArgumentException("Named range was not found: " + name);
/*  830:     */     }
/*  831:1276 */     removeName((Name)names.get(0));
/*  832:     */   }
/*  833:     */   
/*  834:     */   public void removeName(Name name)
/*  835:     */   {
/*  836:1290 */     if ((!this.namedRangesByName.removeMapping(name.getNameName().toLowerCase(Locale.ENGLISH), name)) || (!this.namedRanges.remove(name))) {
/*  837:1292 */       throw new IllegalArgumentException("Name was not found: " + name);
/*  838:     */     }
/*  839:     */   }
/*  840:     */   
/*  841:     */   void updateName(XSSFName name, String oldName)
/*  842:     */   {
/*  843:1297 */     if (!this.namedRangesByName.removeMapping(oldName.toLowerCase(Locale.ENGLISH), name)) {
/*  844:1298 */       throw new IllegalArgumentException("Name was not found: " + name);
/*  845:     */     }
/*  846:1300 */     this.namedRangesByName.put(name.getNameName().toLowerCase(Locale.ENGLISH), name);
/*  847:     */   }
/*  848:     */   
/*  849:     */   public void removePrintArea(int sheetIndex)
/*  850:     */   {
/*  851:1311 */     XSSFName name = getBuiltInName("_xlnm.Print_Area", sheetIndex);
/*  852:1312 */     if (name != null) {
/*  853:1313 */       removeName(name);
/*  854:     */     }
/*  855:     */   }
/*  856:     */   
/*  857:     */   public void removeSheetAt(int index)
/*  858:     */   {
/*  859:1333 */     validateSheetIndex(index);
/*  860:     */     
/*  861:1335 */     onSheetDelete(index);
/*  862:     */     
/*  863:1337 */     XSSFSheet sheet = getSheetAt(index);
/*  864:1338 */     removeRelation(sheet);
/*  865:1339 */     this.sheets.remove(index);
/*  866:1342 */     if (this.sheets.size() == 0) {
/*  867:1343 */       return;
/*  868:     */     }
/*  869:1347 */     int newSheetIndex = index;
/*  870:1348 */     if (newSheetIndex >= this.sheets.size()) {
/*  871:1349 */       newSheetIndex = this.sheets.size() - 1;
/*  872:     */     }
/*  873:1353 */     int active = getActiveSheetIndex();
/*  874:1354 */     if (active == index) {
/*  875:1356 */       setActiveSheet(newSheetIndex);
/*  876:1357 */     } else if (active > index) {
/*  877:1359 */       setActiveSheet(active - 1);
/*  878:     */     }
/*  879:     */   }
/*  880:     */   
/*  881:     */   private void onSheetDelete(int index)
/*  882:     */   {
/*  883:1370 */     XSSFSheet sheet = getSheetAt(index);
/*  884:     */     
/*  885:1372 */     sheet.onSheetDelete();
/*  886:     */     
/*  887:     */ 
/*  888:1375 */     this.workbook.getSheets().removeSheet(index);
/*  889:1378 */     if (this.calcChain != null)
/*  890:     */     {
/*  891:1379 */       removeRelation(this.calcChain);
/*  892:1380 */       this.calcChain = null;
/*  893:     */     }
/*  894:1384 */     List<XSSFName> toRemove = new ArrayList();
/*  895:1385 */     for (XSSFName nm : this.namedRanges)
/*  896:     */     {
/*  897:1386 */       CTDefinedName ct = nm.getCTName();
/*  898:1387 */       if (ct.isSetLocalSheetId()) {
/*  899:1390 */         if (ct.getLocalSheetId() == index) {
/*  900:1391 */           toRemove.add(nm);
/*  901:1392 */         } else if (ct.getLocalSheetId() > index) {
/*  902:1394 */           ct.setLocalSheetId(ct.getLocalSheetId() - 1L);
/*  903:     */         }
/*  904:     */       }
/*  905:     */     }
/*  906:1397 */     for (XSSFName nm : toRemove) {
/*  907:1398 */       removeName(nm);
/*  908:     */     }
/*  909:     */   }
/*  910:     */   
/*  911:     */   public Row.MissingCellPolicy getMissingCellPolicy()
/*  912:     */   {
/*  913:1410 */     return this._missingCellPolicy;
/*  914:     */   }
/*  915:     */   
/*  916:     */   public void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy)
/*  917:     */   {
/*  918:1421 */     this._missingCellPolicy = missingCellPolicy;
/*  919:     */   }
/*  920:     */   
/*  921:     */   public void setActiveSheet(int index)
/*  922:     */   {
/*  923:1432 */     validateSheetIndex(index);
/*  924:1434 */     for (CTBookView arrayBook : this.workbook.getBookViews().getWorkbookViewArray()) {
/*  925:1435 */       arrayBook.setActiveTab(index);
/*  926:     */     }
/*  927:     */   }
/*  928:     */   
/*  929:     */   private void validateSheetIndex(int index)
/*  930:     */   {
/*  931:1447 */     int lastSheetIx = this.sheets.size() - 1;
/*  932:1448 */     if ((index < 0) || (index > lastSheetIx))
/*  933:     */     {
/*  934:1449 */       String range = "(0.." + lastSheetIx + ")";
/*  935:1450 */       if (lastSheetIx == -1) {
/*  936:1451 */         range = "(no sheets)";
/*  937:     */       }
/*  938:1453 */       throw new IllegalArgumentException("Sheet index (" + index + ") is out of range " + range);
/*  939:     */     }
/*  940:     */   }
/*  941:     */   
/*  942:     */   public int getFirstVisibleTab()
/*  943:     */   {
/*  944:1465 */     CTBookViews bookViews = this.workbook.getBookViews();
/*  945:1466 */     CTBookView bookView = bookViews.getWorkbookViewArray(0);
/*  946:1467 */     return (short)(int)bookView.getFirstSheet();
/*  947:     */   }
/*  948:     */   
/*  949:     */   public void setFirstVisibleTab(int index)
/*  950:     */   {
/*  951:1477 */     CTBookViews bookViews = this.workbook.getBookViews();
/*  952:1478 */     CTBookView bookView = bookViews.getWorkbookViewArray(0);
/*  953:1479 */     bookView.setFirstSheet(index);
/*  954:     */   }
/*  955:     */   
/*  956:     */   public void setPrintArea(int sheetIndex, String reference)
/*  957:     */   {
/*  958:1491 */     XSSFName name = getBuiltInName("_xlnm.Print_Area", sheetIndex);
/*  959:1492 */     if (name == null) {
/*  960:1493 */       name = createBuiltInName("_xlnm.Print_Area", sheetIndex);
/*  961:     */     }
/*  962:1497 */     String[] parts = COMMA_PATTERN.split(reference);
/*  963:1498 */     StringBuffer sb = new StringBuffer(32);
/*  964:1499 */     for (int i = 0; i < parts.length; i++)
/*  965:     */     {
/*  966:1500 */       if (i > 0) {
/*  967:1501 */         sb.append(",");
/*  968:     */       }
/*  969:1503 */       SheetNameFormatter.appendFormat(sb, getSheetName(sheetIndex));
/*  970:1504 */       sb.append("!");
/*  971:1505 */       sb.append(parts[i]);
/*  972:     */     }
/*  973:1507 */     name.setRefersToFormula(sb.toString());
/*  974:     */   }
/*  975:     */   
/*  976:     */   public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)
/*  977:     */   {
/*  978:1521 */     String reference = getReferencePrintArea(getSheetName(sheetIndex), startColumn, endColumn, startRow, endRow);
/*  979:1522 */     setPrintArea(sheetIndex, reference);
/*  980:     */   }
/*  981:     */   
/*  982:     */   private static String getReferencePrintArea(String sheetName, int startC, int endC, int startR, int endR)
/*  983:     */   {
/*  984:1527 */     CellReference colRef = new CellReference(sheetName, startR, startC, true, true);
/*  985:1528 */     CellReference colRef2 = new CellReference(sheetName, endR, endC, true, true);
/*  986:     */     
/*  987:1530 */     return "$" + colRef.getCellRefParts()[2] + "$" + colRef.getCellRefParts()[1] + ":$" + colRef2.getCellRefParts()[2] + "$" + colRef2.getCellRefParts()[1];
/*  988:     */   }
/*  989:     */   
/*  990:     */   XSSFName getBuiltInName(String builtInCode, int sheetNumber)
/*  991:     */   {
/*  992:1534 */     for (XSSFName name : this.namedRangesByName.get(builtInCode.toLowerCase(Locale.ENGLISH))) {
/*  993:1535 */       if (name.getSheetIndex() == sheetNumber) {
/*  994:1536 */         return name;
/*  995:     */       }
/*  996:     */     }
/*  997:1539 */     return null;
/*  998:     */   }
/*  999:     */   
/* 1000:     */   XSSFName createBuiltInName(String builtInName, int sheetNumber)
/* 1001:     */   {
/* 1002:1550 */     validateSheetIndex(sheetNumber);
/* 1003:     */     
/* 1004:1552 */     CTDefinedNames names = this.workbook.getDefinedNames() == null ? this.workbook.addNewDefinedNames() : this.workbook.getDefinedNames();
/* 1005:1553 */     CTDefinedName nameRecord = names.addNewDefinedName();
/* 1006:1554 */     nameRecord.setName(builtInName);
/* 1007:1555 */     nameRecord.setLocalSheetId(sheetNumber);
/* 1008:1557 */     if (getBuiltInName(builtInName, sheetNumber) != null) {
/* 1009:1558 */       throw new POIXMLException("Builtin (" + builtInName + ") already exists for sheet (" + sheetNumber + ")");
/* 1010:     */     }
/* 1011:1562 */     return createAndStoreName(nameRecord);
/* 1012:     */   }
/* 1013:     */   
/* 1014:     */   public void setSelectedTab(int index)
/* 1015:     */   {
/* 1016:1570 */     int idx = 0;
/* 1017:1571 */     for (XSSFSheet sh : this.sheets)
/* 1018:     */     {
/* 1019:1572 */       sh.setSelected(idx == index);
/* 1020:1573 */       idx++;
/* 1021:     */     }
/* 1022:     */   }
/* 1023:     */   
/* 1024:     */   public void setSheetName(int sheetIndex, String sheetname)
/* 1025:     */   {
/* 1026:1589 */     if (sheetname == null) {
/* 1027:1590 */       throw new IllegalArgumentException("sheetName must not be null");
/* 1028:     */     }
/* 1029:1593 */     validateSheetIndex(sheetIndex);
/* 1030:1594 */     String oldSheetName = getSheetName(sheetIndex);
/* 1031:1597 */     if (sheetname.length() > 31) {
/* 1032:1598 */       sheetname = sheetname.substring(0, 31);
/* 1033:     */     }
/* 1034:1600 */     WorkbookUtil.validateSheetName(sheetname);
/* 1035:1603 */     if (sheetname.equals(oldSheetName)) {
/* 1036:1604 */       return;
/* 1037:     */     }
/* 1038:1608 */     if (containsSheet(sheetname, sheetIndex)) {
/* 1039:1609 */       throw new IllegalArgumentException("The workbook already contains a sheet of this name");
/* 1040:     */     }
/* 1041:1613 */     XSSFFormulaUtils utils = new XSSFFormulaUtils(this);
/* 1042:1614 */     utils.updateSheetName(sheetIndex, oldSheetName, sheetname);
/* 1043:     */     
/* 1044:1616 */     this.workbook.getSheets().getSheetArray(sheetIndex).setName(sheetname);
/* 1045:     */   }
/* 1046:     */   
/* 1047:     */   public void setSheetOrder(String sheetname, int pos)
/* 1048:     */   {
/* 1049:1627 */     int idx = getSheetIndex(sheetname);
/* 1050:1628 */     this.sheets.add(pos, this.sheets.remove(idx));
/* 1051:     */     
/* 1052:     */ 
/* 1053:1631 */     CTSheets ct = this.workbook.getSheets();
/* 1054:1632 */     XmlObject cts = ct.getSheetArray(idx).copy();
/* 1055:1633 */     this.workbook.getSheets().removeSheet(idx);
/* 1056:1634 */     CTSheet newcts = ct.insertNewSheet(pos);
/* 1057:1635 */     newcts.set(cts);
/* 1058:     */     
/* 1059:     */ 
/* 1060:     */ 
/* 1061:1639 */     CTSheet[] sheetArray = ct.getSheetArray();
/* 1062:1640 */     for (int i = 0; i < sheetArray.length; i++) {
/* 1063:1641 */       ((XSSFSheet)this.sheets.get(i)).sheet = sheetArray[i];
/* 1064:     */     }
/* 1065:1644 */     updateNamedRangesAfterSheetReorder(idx, pos);
/* 1066:1645 */     updateActiveSheetAfterSheetReorder(idx, pos);
/* 1067:     */   }
/* 1068:     */   
/* 1069:     */   private void updateNamedRangesAfterSheetReorder(int oldIndex, int newIndex)
/* 1070:     */   {
/* 1071:1658 */     for (XSSFName name : this.namedRanges)
/* 1072:     */     {
/* 1073:1659 */       int i = name.getSheetIndex();
/* 1074:1661 */       if (i != -1) {
/* 1075:1663 */         if (i == oldIndex) {
/* 1076:1664 */           name.setSheetIndex(newIndex);
/* 1077:1667 */         } else if ((newIndex <= i) && (i < oldIndex)) {
/* 1078:1668 */           name.setSheetIndex(i + 1);
/* 1079:1671 */         } else if ((oldIndex < i) && (i <= newIndex)) {
/* 1080:1672 */           name.setSheetIndex(i - 1);
/* 1081:     */         }
/* 1082:     */       }
/* 1083:     */     }
/* 1084:     */   }
/* 1085:     */   
/* 1086:     */   private void updateActiveSheetAfterSheetReorder(int oldIndex, int newIndex)
/* 1087:     */   {
/* 1088:1680 */     int active = getActiveSheetIndex();
/* 1089:1681 */     if (active == oldIndex) {
/* 1090:1683 */       setActiveSheet(newIndex);
/* 1091:1684 */     } else if (((active >= oldIndex) || (active >= newIndex)) && ((active <= oldIndex) || (active <= newIndex))) {
/* 1092:1687 */       if (newIndex > oldIndex) {
/* 1093:1689 */         setActiveSheet(active - 1);
/* 1094:     */       } else {
/* 1095:1692 */         setActiveSheet(active + 1);
/* 1096:     */       }
/* 1097:     */     }
/* 1098:     */   }
/* 1099:     */   
/* 1100:     */   private void saveNamedRanges()
/* 1101:     */   {
/* 1102:1701 */     if (this.namedRanges.size() > 0)
/* 1103:     */     {
/* 1104:1702 */       CTDefinedNames names = CTDefinedNames.Factory.newInstance();
/* 1105:1703 */       CTDefinedName[] nr = new CTDefinedName[this.namedRanges.size()];
/* 1106:1704 */       int i = 0;
/* 1107:1705 */       for (XSSFName name : this.namedRanges)
/* 1108:     */       {
/* 1109:1706 */         nr[i] = name.getCTName();
/* 1110:1707 */         i++;
/* 1111:     */       }
/* 1112:1709 */       names.setDefinedNameArray(nr);
/* 1113:1710 */       if (this.workbook.isSetDefinedNames()) {
/* 1114:1711 */         this.workbook.unsetDefinedNames();
/* 1115:     */       }
/* 1116:1713 */       this.workbook.setDefinedNames(names);
/* 1117:     */       
/* 1118:     */ 
/* 1119:1716 */       reprocessNamedRanges();
/* 1120:     */     }
/* 1121:1718 */     else if (this.workbook.isSetDefinedNames())
/* 1122:     */     {
/* 1123:1719 */       this.workbook.unsetDefinedNames();
/* 1124:     */     }
/* 1125:     */   }
/* 1126:     */   
/* 1127:     */   private void reprocessNamedRanges()
/* 1128:     */   {
/* 1129:1725 */     this.namedRangesByName = new ArrayListValuedHashMap();
/* 1130:1726 */     this.namedRanges = new ArrayList();
/* 1131:1727 */     if (this.workbook.isSetDefinedNames()) {
/* 1132:1728 */       for (CTDefinedName ctName : this.workbook.getDefinedNames().getDefinedNameArray()) {
/* 1133:1729 */         createAndStoreName(ctName);
/* 1134:     */       }
/* 1135:     */     }
/* 1136:     */   }
/* 1137:     */   
/* 1138:     */   private void saveCalculationChain()
/* 1139:     */   {
/* 1140:1735 */     if (this.calcChain != null)
/* 1141:     */     {
/* 1142:1736 */       int count = this.calcChain.getCTCalcChain().sizeOfCArray();
/* 1143:1737 */       if (count == 0)
/* 1144:     */       {
/* 1145:1738 */         removeRelation(this.calcChain);
/* 1146:1739 */         this.calcChain = null;
/* 1147:     */       }
/* 1148:     */     }
/* 1149:     */   }
/* 1150:     */   
/* 1151:     */   protected void commit()
/* 1152:     */     throws IOException
/* 1153:     */   {
/* 1154:1746 */     saveNamedRanges();
/* 1155:1747 */     saveCalculationChain();
/* 1156:     */     
/* 1157:1749 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 1158:1750 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTWorkbook.type.getName().getNamespaceURI(), "workbook"));
/* 1159:     */     
/* 1160:1752 */     PackagePart part = getPackagePart();
/* 1161:1753 */     OutputStream out = part.getOutputStream();
/* 1162:1754 */     this.workbook.save(out, xmlOptions);
/* 1163:1755 */     out.close();
/* 1164:     */   }
/* 1165:     */   
/* 1166:     */   @Internal
/* 1167:     */   public SharedStringsTable getSharedStringSource()
/* 1168:     */   {
/* 1169:1765 */     return this.sharedStringSource;
/* 1170:     */   }
/* 1171:     */   
/* 1172:     */   public StylesTable getStylesSource()
/* 1173:     */   {
/* 1174:1773 */     return this.stylesSource;
/* 1175:     */   }
/* 1176:     */   
/* 1177:     */   public ThemesTable getTheme()
/* 1178:     */   {
/* 1179:1780 */     if (this.stylesSource == null) {
/* 1180:1781 */       return null;
/* 1181:     */     }
/* 1182:1783 */     return this.stylesSource.getTheme();
/* 1183:     */   }
/* 1184:     */   
/* 1185:     */   public XSSFCreationHelper getCreationHelper()
/* 1186:     */   {
/* 1187:1792 */     if (this._creationHelper == null) {
/* 1188:1793 */       this._creationHelper = new XSSFCreationHelper(this);
/* 1189:     */     }
/* 1190:1795 */     return this._creationHelper;
/* 1191:     */   }
/* 1192:     */   
/* 1193:     */   private boolean containsSheet(String name, int excludeSheetIdx)
/* 1194:     */   {
/* 1195:1808 */     CTSheet[] ctSheetArray = this.workbook.getSheets().getSheetArray();
/* 1196:1810 */     if (name.length() > 31) {
/* 1197:1811 */       name = name.substring(0, 31);
/* 1198:     */     }
/* 1199:1814 */     for (int i = 0; i < ctSheetArray.length; i++)
/* 1200:     */     {
/* 1201:1815 */       String ctName = ctSheetArray[i].getName();
/* 1202:1816 */       if (ctName.length() > 31) {
/* 1203:1817 */         ctName = ctName.substring(0, 31);
/* 1204:     */       }
/* 1205:1820 */       if ((excludeSheetIdx != i) && (name.equalsIgnoreCase(ctName))) {
/* 1206:1821 */         return true;
/* 1207:     */       }
/* 1208:     */     }
/* 1209:1824 */     return false;
/* 1210:     */   }
/* 1211:     */   
/* 1212:     */   @Internal
/* 1213:     */   public boolean isDate1904()
/* 1214:     */   {
/* 1215:1837 */     CTWorkbookPr workbookPr = this.workbook.getWorkbookPr();
/* 1216:1838 */     return (workbookPr != null) && (workbookPr.getDate1904());
/* 1217:     */   }
/* 1218:     */   
/* 1219:     */   public List<PackagePart> getAllEmbedds()
/* 1220:     */     throws OpenXML4JException
/* 1221:     */   {
/* 1222:1846 */     List<PackagePart> embedds = new LinkedList();
/* 1223:1848 */     for (Iterator i$ = this.sheets.iterator(); i$.hasNext();)
/* 1224:     */     {
/* 1225:1848 */       sheet = (XSSFSheet)i$.next();
/* 1226:1850 */       for (PackageRelationship rel : sheet.getPackagePart().getRelationshipsByType(XSSFRelation.OLEEMBEDDINGS.getRelation())) {
/* 1227:1851 */         embedds.add(sheet.getPackagePart().getRelatedPart(rel));
/* 1228:     */       }
/* 1229:1854 */       for (PackageRelationship rel : sheet.getPackagePart().getRelationshipsByType(XSSFRelation.PACKEMBEDDINGS.getRelation())) {
/* 1230:1855 */         embedds.add(sheet.getPackagePart().getRelatedPart(rel));
/* 1231:     */       }
/* 1232:     */     }
/* 1233:     */     XSSFSheet sheet;
/* 1234:1858 */     return embedds;
/* 1235:     */   }
/* 1236:     */   
/* 1237:     */   @NotImplemented
/* 1238:     */   public boolean isHidden()
/* 1239:     */   {
/* 1240:1864 */     throw new RuntimeException("Not implemented yet");
/* 1241:     */   }
/* 1242:     */   
/* 1243:     */   @NotImplemented
/* 1244:     */   public void setHidden(boolean hiddenFlag)
/* 1245:     */   {
/* 1246:1870 */     throw new RuntimeException("Not implemented yet");
/* 1247:     */   }
/* 1248:     */   
/* 1249:     */   public boolean isSheetHidden(int sheetIx)
/* 1250:     */   {
/* 1251:1875 */     validateSheetIndex(sheetIx);
/* 1252:1876 */     CTSheet ctSheet = ((XSSFSheet)this.sheets.get(sheetIx)).sheet;
/* 1253:1877 */     return ctSheet.getState() == STSheetState.HIDDEN;
/* 1254:     */   }
/* 1255:     */   
/* 1256:     */   public boolean isSheetVeryHidden(int sheetIx)
/* 1257:     */   {
/* 1258:1882 */     validateSheetIndex(sheetIx);
/* 1259:1883 */     CTSheet ctSheet = ((XSSFSheet)this.sheets.get(sheetIx)).sheet;
/* 1260:1884 */     return ctSheet.getState() == STSheetState.VERY_HIDDEN;
/* 1261:     */   }
/* 1262:     */   
/* 1263:     */   public SheetVisibility getSheetVisibility(int sheetIx)
/* 1264:     */   {
/* 1265:1889 */     validateSheetIndex(sheetIx);
/* 1266:1890 */     CTSheet ctSheet = ((XSSFSheet)this.sheets.get(sheetIx)).sheet;
/* 1267:1891 */     STSheetState.Enum state = ctSheet.getState();
/* 1268:1892 */     if (state == STSheetState.VISIBLE) {
/* 1269:1893 */       return SheetVisibility.VISIBLE;
/* 1270:     */     }
/* 1271:1895 */     if (state == STSheetState.HIDDEN) {
/* 1272:1896 */       return SheetVisibility.HIDDEN;
/* 1273:     */     }
/* 1274:1898 */     if (state == STSheetState.VERY_HIDDEN) {
/* 1275:1899 */       return SheetVisibility.VERY_HIDDEN;
/* 1276:     */     }
/* 1277:1901 */     throw new IllegalArgumentException("This should never happen");
/* 1278:     */   }
/* 1279:     */   
/* 1280:     */   public void setSheetHidden(int sheetIx, boolean hidden)
/* 1281:     */   {
/* 1282:1906 */     setSheetVisibility(sheetIx, hidden ? SheetVisibility.HIDDEN : SheetVisibility.VISIBLE);
/* 1283:     */   }
/* 1284:     */   
/* 1285:     */   @Deprecated
/* 1286:     */   @Removal(version="3.18")
/* 1287:     */   public void setSheetHidden(int sheetIx, int state)
/* 1288:     */   {
/* 1289:1913 */     WorkbookUtil.validateSheetState(state);
/* 1290:1914 */     setSheetVisibility(sheetIx, SheetVisibility.values()[state]);
/* 1291:     */   }
/* 1292:     */   
/* 1293:     */   public void setSheetVisibility(int sheetIx, SheetVisibility visibility)
/* 1294:     */   {
/* 1295:1919 */     validateSheetIndex(sheetIx);
/* 1296:     */     
/* 1297:1921 */     CTSheet ctSheet = ((XSSFSheet)this.sheets.get(sheetIx)).sheet;
/* 1298:1922 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$SheetVisibility[visibility.ordinal()])
/* 1299:     */     {
/* 1300:     */     case 1: 
/* 1301:1924 */       ctSheet.setState(STSheetState.VISIBLE);
/* 1302:1925 */       break;
/* 1303:     */     case 2: 
/* 1304:1927 */       ctSheet.setState(STSheetState.HIDDEN);
/* 1305:1928 */       break;
/* 1306:     */     case 3: 
/* 1307:1930 */       ctSheet.setState(STSheetState.VERY_HIDDEN);
/* 1308:1931 */       break;
/* 1309:     */     default: 
/* 1310:1933 */       throw new IllegalArgumentException("This should never happen");
/* 1311:     */     }
/* 1312:     */   }
/* 1313:     */   
/* 1314:     */   protected void onDeleteFormula(XSSFCell cell)
/* 1315:     */   {
/* 1316:1947 */     if (this.calcChain != null)
/* 1317:     */     {
/* 1318:1948 */       int sheetId = (int)cell.getSheet().sheet.getSheetId();
/* 1319:1949 */       this.calcChain.removeItem(sheetId, cell.getReference());
/* 1320:     */     }
/* 1321:     */   }
/* 1322:     */   
/* 1323:     */   @Internal
/* 1324:     */   public CalculationChain getCalculationChain()
/* 1325:     */   {
/* 1326:1963 */     return this.calcChain;
/* 1327:     */   }
/* 1328:     */   
/* 1329:     */   @Internal
/* 1330:     */   public List<ExternalLinksTable> getExternalLinksTable()
/* 1331:     */   {
/* 1332:1981 */     return this.externalLinks;
/* 1333:     */   }
/* 1334:     */   
/* 1335:     */   public Collection<XSSFMap> getCustomXMLMappings()
/* 1336:     */   {
/* 1337:1989 */     return this.mapInfo == null ? new ArrayList() : this.mapInfo.getAllXSSFMaps();
/* 1338:     */   }
/* 1339:     */   
/* 1340:     */   @Internal
/* 1341:     */   public MapInfo getMapInfo()
/* 1342:     */   {
/* 1343:1998 */     return this.mapInfo;
/* 1344:     */   }
/* 1345:     */   
/* 1346:     */   @NotImplemented
/* 1347:     */   public int linkExternalWorkbook(String name, Workbook workbook)
/* 1348:     */   {
/* 1349:2017 */     throw new RuntimeException("Not Implemented - see bug #57184");
/* 1350:     */   }
/* 1351:     */   
/* 1352:     */   public boolean isStructureLocked()
/* 1353:     */   {
/* 1354:2029 */     return (workbookProtectionPresent()) && (this.workbook.getWorkbookProtection().getLockStructure());
/* 1355:     */   }
/* 1356:     */   
/* 1357:     */   public boolean isWindowsLocked()
/* 1358:     */   {
/* 1359:2041 */     return (workbookProtectionPresent()) && (this.workbook.getWorkbookProtection().getLockWindows());
/* 1360:     */   }
/* 1361:     */   
/* 1362:     */   public boolean isRevisionLocked()
/* 1363:     */   {
/* 1364:2050 */     return (workbookProtectionPresent()) && (this.workbook.getWorkbookProtection().getLockRevision());
/* 1365:     */   }
/* 1366:     */   
/* 1367:     */   public void lockStructure()
/* 1368:     */   {
/* 1369:2057 */     safeGetWorkbookProtection().setLockStructure(true);
/* 1370:     */   }
/* 1371:     */   
/* 1372:     */   public void unLockStructure()
/* 1373:     */   {
/* 1374:2064 */     safeGetWorkbookProtection().setLockStructure(false);
/* 1375:     */   }
/* 1376:     */   
/* 1377:     */   public void lockWindows()
/* 1378:     */   {
/* 1379:2071 */     safeGetWorkbookProtection().setLockWindows(true);
/* 1380:     */   }
/* 1381:     */   
/* 1382:     */   public void unLockWindows()
/* 1383:     */   {
/* 1384:2078 */     safeGetWorkbookProtection().setLockWindows(false);
/* 1385:     */   }
/* 1386:     */   
/* 1387:     */   public void lockRevision()
/* 1388:     */   {
/* 1389:2085 */     safeGetWorkbookProtection().setLockRevision(true);
/* 1390:     */   }
/* 1391:     */   
/* 1392:     */   public void unLockRevision()
/* 1393:     */   {
/* 1394:2092 */     safeGetWorkbookProtection().setLockRevision(false);
/* 1395:     */   }
/* 1396:     */   
/* 1397:     */   public void setWorkbookPassword(String password, HashAlgorithm hashAlgo)
/* 1398:     */   {
/* 1399:2103 */     if ((password == null) && (!workbookProtectionPresent())) {
/* 1400:2104 */       return;
/* 1401:     */     }
/* 1402:2106 */     XSSFPasswordHelper.setPassword(safeGetWorkbookProtection(), password, hashAlgo, "workbook");
/* 1403:     */   }
/* 1404:     */   
/* 1405:     */   public boolean validateWorkbookPassword(String password)
/* 1406:     */   {
/* 1407:2115 */     if (!workbookProtectionPresent()) {
/* 1408:2116 */       return password == null;
/* 1409:     */     }
/* 1410:2118 */     return XSSFPasswordHelper.validatePassword(safeGetWorkbookProtection(), password, "workbook");
/* 1411:     */   }
/* 1412:     */   
/* 1413:     */   public void setRevisionsPassword(String password, HashAlgorithm hashAlgo)
/* 1414:     */   {
/* 1415:2129 */     if ((password == null) && (!workbookProtectionPresent())) {
/* 1416:2130 */       return;
/* 1417:     */     }
/* 1418:2132 */     XSSFPasswordHelper.setPassword(safeGetWorkbookProtection(), password, hashAlgo, "revisions");
/* 1419:     */   }
/* 1420:     */   
/* 1421:     */   public boolean validateRevisionsPassword(String password)
/* 1422:     */   {
/* 1423:2141 */     if (!workbookProtectionPresent()) {
/* 1424:2142 */       return password == null;
/* 1425:     */     }
/* 1426:2144 */     return XSSFPasswordHelper.validatePassword(safeGetWorkbookProtection(), password, "revisions");
/* 1427:     */   }
/* 1428:     */   
/* 1429:     */   public void unLock()
/* 1430:     */   {
/* 1431:2151 */     if (workbookProtectionPresent()) {
/* 1432:2152 */       this.workbook.unsetWorkbookProtection();
/* 1433:     */     }
/* 1434:     */   }
/* 1435:     */   
/* 1436:     */   private boolean workbookProtectionPresent()
/* 1437:     */   {
/* 1438:2157 */     return this.workbook.isSetWorkbookProtection();
/* 1439:     */   }
/* 1440:     */   
/* 1441:     */   private CTWorkbookProtection safeGetWorkbookProtection()
/* 1442:     */   {
/* 1443:2161 */     if (!workbookProtectionPresent()) {
/* 1444:2162 */       return this.workbook.addNewWorkbookProtection();
/* 1445:     */     }
/* 1446:2164 */     return this.workbook.getWorkbookProtection();
/* 1447:     */   }
/* 1448:     */   
/* 1449:     */   UDFFinder getUDFFinder()
/* 1450:     */   {
/* 1451:2180 */     return this._udfFinder;
/* 1452:     */   }
/* 1453:     */   
/* 1454:     */   public void addToolPack(UDFFinder toopack)
/* 1455:     */   {
/* 1456:2190 */     this._udfFinder.add(toopack);
/* 1457:     */   }
/* 1458:     */   
/* 1459:     */   public void setForceFormulaRecalculation(boolean value)
/* 1460:     */   {
/* 1461:2212 */     CTWorkbook ctWorkbook = getCTWorkbook();
/* 1462:2213 */     CTCalcPr calcPr = ctWorkbook.isSetCalcPr() ? ctWorkbook.getCalcPr() : ctWorkbook.addNewCalcPr();
/* 1463:     */     
/* 1464:     */ 
/* 1465:2216 */     calcPr.setCalcId(0L);
/* 1466:2218 */     if ((value) && (calcPr.getCalcMode() == STCalcMode.MANUAL)) {
/* 1467:2219 */       calcPr.setCalcMode(STCalcMode.AUTO);
/* 1468:     */     }
/* 1469:     */   }
/* 1470:     */   
/* 1471:     */   public boolean getForceFormulaRecalculation()
/* 1472:     */   {
/* 1473:2230 */     CTWorkbook ctWorkbook = getCTWorkbook();
/* 1474:2231 */     CTCalcPr calcPr = ctWorkbook.getCalcPr();
/* 1475:2232 */     return (calcPr != null) && (calcPr.getCalcId() != 0L);
/* 1476:     */   }
/* 1477:     */   
/* 1478:     */   protected CTPivotCache addPivotCache(String rId)
/* 1479:     */   {
/* 1480:2242 */     CTWorkbook ctWorkbook = getCTWorkbook();
/* 1481:     */     CTPivotCaches caches;
/* 1482:     */     CTPivotCaches caches;
/* 1483:2244 */     if (ctWorkbook.isSetPivotCaches()) {
/* 1484:2245 */       caches = ctWorkbook.getPivotCaches();
/* 1485:     */     } else {
/* 1486:2247 */       caches = ctWorkbook.addNewPivotCaches();
/* 1487:     */     }
/* 1488:2249 */     CTPivotCache cache = caches.addNewPivotCache();
/* 1489:     */     
/* 1490:2251 */     int tableId = getPivotTables().size() + 1;
/* 1491:2252 */     cache.setCacheId(tableId);
/* 1492:2253 */     cache.setId(rId);
/* 1493:2254 */     if (this.pivotCaches == null) {
/* 1494:2255 */       this.pivotCaches = new ArrayList();
/* 1495:     */     }
/* 1496:2257 */     this.pivotCaches.add(cache);
/* 1497:2258 */     return cache;
/* 1498:     */   }
/* 1499:     */   
/* 1500:     */   public List<XSSFPivotTable> getPivotTables()
/* 1501:     */   {
/* 1502:2263 */     return this.pivotTables;
/* 1503:     */   }
/* 1504:     */   
/* 1505:     */   protected void setPivotTables(List<XSSFPivotTable> pivotTables)
/* 1506:     */   {
/* 1507:2268 */     this.pivotTables = pivotTables;
/* 1508:     */   }
/* 1509:     */   
/* 1510:     */   public XSSFWorkbookType getWorkbookType()
/* 1511:     */   {
/* 1512:2272 */     return isMacroEnabled() ? XSSFWorkbookType.XLSM : XSSFWorkbookType.XLSX;
/* 1513:     */   }
/* 1514:     */   
/* 1515:     */   public void setWorkbookType(XSSFWorkbookType type)
/* 1516:     */   {
/* 1517:     */     try
/* 1518:     */     {
/* 1519:2280 */       getPackagePart().setContentType(type.getContentType());
/* 1520:     */     }
/* 1521:     */     catch (InvalidFormatException e)
/* 1522:     */     {
/* 1523:2282 */       throw new POIXMLException(e);
/* 1524:     */     }
/* 1525:     */   }
/* 1526:     */   
/* 1527:     */   public void setVBAProject(InputStream vbaProjectStream)
/* 1528:     */     throws IOException
/* 1529:     */   {
/* 1530:2293 */     if (!isMacroEnabled()) {
/* 1531:2294 */       setWorkbookType(XSSFWorkbookType.XLSM);
/* 1532:     */     }
/* 1533:     */     PackagePartName ppName;
/* 1534:     */     try
/* 1535:     */     {
/* 1536:2299 */       ppName = PackagingURIHelper.createPartName(XSSFRelation.VBA_MACROS.getDefaultFileName());
/* 1537:     */     }
/* 1538:     */     catch (InvalidFormatException e)
/* 1539:     */     {
/* 1540:2301 */       throw new POIXMLException(e);
/* 1541:     */     }
/* 1542:2303 */     OPCPackage opc = getPackage();
/* 1543:     */     OutputStream outputStream;
/* 1544:     */     OutputStream outputStream;
/* 1545:2305 */     if (!opc.containPart(ppName))
/* 1546:     */     {
/* 1547:2306 */       POIXMLDocumentPart relationship = createRelationship(XSSFRelation.VBA_MACROS, XSSFFactory.getInstance());
/* 1548:2307 */       outputStream = relationship.getPackagePart().getOutputStream();
/* 1549:     */     }
/* 1550:     */     else
/* 1551:     */     {
/* 1552:2309 */       PackagePart part = opc.getPart(ppName);
/* 1553:2310 */       outputStream = part.getOutputStream();
/* 1554:     */     }
/* 1555:     */     try
/* 1556:     */     {
/* 1557:2313 */       IOUtils.copy(vbaProjectStream, outputStream);
/* 1558:     */     }
/* 1559:     */     finally
/* 1560:     */     {
/* 1561:2315 */       IOUtils.closeQuietly(outputStream);
/* 1562:     */     }
/* 1563:     */   }
/* 1564:     */   
/* 1565:     */   public void setVBAProject(XSSFWorkbook macroWorkbook)
/* 1566:     */     throws IOException, InvalidFormatException
/* 1567:     */   {
/* 1568:2325 */     if (!macroWorkbook.isMacroEnabled()) {
/* 1569:2326 */       return;
/* 1570:     */     }
/* 1571:2328 */     InputStream vbaProjectStream = XSSFRelation.VBA_MACROS.getContents(macroWorkbook.getCorePart());
/* 1572:2329 */     if (vbaProjectStream != null) {
/* 1573:2330 */       setVBAProject(vbaProjectStream);
/* 1574:     */     }
/* 1575:     */   }
/* 1576:     */   
/* 1577:     */   public SpreadsheetVersion getSpreadsheetVersion()
/* 1578:     */   {
/* 1579:2342 */     return SpreadsheetVersion.EXCEL2007;
/* 1580:     */   }
/* 1581:     */   
/* 1582:     */   public XSSFTable getTable(String name)
/* 1583:     */   {
/* 1584:2353 */     if ((name != null) && (this.sheets != null)) {
/* 1585:2354 */       for (XSSFSheet sheet : this.sheets) {
/* 1586:2355 */         for (XSSFTable tbl : sheet.getTables()) {
/* 1587:2356 */           if (name.equalsIgnoreCase(tbl.getName())) {
/* 1588:2357 */             return tbl;
/* 1589:     */           }
/* 1590:     */         }
/* 1591:     */       }
/* 1592:     */     }
/* 1593:2362 */     return null;
/* 1594:     */   }
/* 1595:     */   
/* 1596:     */   public int addOlePackage(byte[] oleData, String label, String fileName, String command)
/* 1597:     */     throws IOException
/* 1598:     */   {
/* 1599:2369 */     OPCPackage opc = getPackage();
/* 1600:     */     
/* 1601:2371 */     int oleId = 0;
/* 1602:     */     PackagePartName pnOLE;
/* 1603:     */     do
/* 1604:     */     {
/* 1605:     */       try
/* 1606:     */       {
/* 1607:2374 */         pnOLE = PackagingURIHelper.createPartName("/xl/embeddings/oleObject" + ++oleId + ".bin");
/* 1608:     */       }
/* 1609:     */       catch (InvalidFormatException e)
/* 1610:     */       {
/* 1611:2376 */         throw new IOException("ole object name not recognized", e);
/* 1612:     */       }
/* 1613:2378 */     } while (opc.containPart(pnOLE));
/* 1614:2380 */     PackagePart pp = opc.createPart(pnOLE, "application/vnd.openxmlformats-officedocument.oleObject");
/* 1615:     */     
/* 1616:2382 */     Ole10Native ole10 = new Ole10Native(label, fileName, command, oleData);
/* 1617:     */     
/* 1618:2384 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(oleData.length + 500);
/* 1619:2385 */     ole10.writeOut(bos);
/* 1620:     */     
/* 1621:2387 */     POIFSFileSystem poifs = new POIFSFileSystem();
/* 1622:2388 */     DirectoryNode root = poifs.getRoot();
/* 1623:2389 */     root.createDocument("\001Ole10Native", new ByteArrayInputStream(bos.toByteArray()));
/* 1624:2390 */     root.setStorageClsid(ClassID.OLE10_PACKAGE);
/* 1625:     */     
/* 1626:     */ 
/* 1627:     */ 
/* 1628:2394 */     OutputStream os = pp.getOutputStream();
/* 1629:2395 */     poifs.writeFilesystem(os);
/* 1630:2396 */     os.close();
/* 1631:2397 */     poifs.close();
/* 1632:     */     
/* 1633:2399 */     return oleId;
/* 1634:     */   }
/* 1635:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFWorkbook
 * JD-Core Version:    0.7.0.1
 */