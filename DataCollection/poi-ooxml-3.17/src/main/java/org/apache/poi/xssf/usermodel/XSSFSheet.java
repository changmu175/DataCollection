/*    1:     */ package org.apache.poi.xssf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.InputStream;
/*    5:     */ import java.io.OutputStream;
/*    6:     */ import java.util.ArrayList;
/*    7:     */ import java.util.Arrays;
/*    8:     */ import java.util.Collection;
/*    9:     */ import java.util.Collections;
/*   10:     */ import java.util.Comparator;
/*   11:     */ import java.util.HashMap;
/*   12:     */ import java.util.Iterator;
/*   13:     */ import java.util.LinkedHashMap;
/*   14:     */ import java.util.LinkedHashSet;
/*   15:     */ import java.util.List;
/*   16:     */ import java.util.Map;
/*   17:     */ import java.util.Map.Entry;
/*   18:     */ import java.util.Set;
/*   19:     */ import java.util.SortedMap;
/*   20:     */ import java.util.TreeMap;
/*   21:     */ import javax.xml.namespace.QName;
/*   22:     */ import javax.xml.stream.XMLStreamException;
/*   23:     */ import javax.xml.stream.XMLStreamReader;
/*   24:     */ import org.apache.poi.POIXMLDocumentPart;
/*   25:     */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*   26:     */ import org.apache.poi.POIXMLException;
/*   27:     */ import org.apache.poi.POIXMLTypeLoader;
/*   28:     */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   29:     */ import org.apache.poi.openxml4j.exceptions.PartAlreadyExistsException;
/*   30:     */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   31:     */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   32:     */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   33:     */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*   34:     */ import org.apache.poi.openxml4j.opc.TargetMode;
/*   35:     */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*   36:     */ import org.apache.poi.ss.SpreadsheetVersion;
/*   37:     */ import org.apache.poi.ss.formula.FormulaShifter;
/*   38:     */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   39:     */ import org.apache.poi.ss.usermodel.Cell;
/*   40:     */ import org.apache.poi.ss.usermodel.CellCopyPolicy;
/*   41:     */ import org.apache.poi.ss.usermodel.CellRange;
/*   42:     */ import org.apache.poi.ss.usermodel.CellStyle;
/*   43:     */ import org.apache.poi.ss.usermodel.CellType;
/*   44:     */ import org.apache.poi.ss.usermodel.DataValidation;
/*   45:     */ import org.apache.poi.ss.usermodel.DataValidationHelper;
/*   46:     */ import org.apache.poi.ss.usermodel.Footer;
/*   47:     */ import org.apache.poi.ss.usermodel.Header;
/*   48:     */ import org.apache.poi.ss.usermodel.IgnoredErrorType;
/*   49:     */ import org.apache.poi.ss.usermodel.Name;
/*   50:     */ import org.apache.poi.ss.usermodel.Row;
/*   51:     */ import org.apache.poi.ss.usermodel.Sheet;
/*   52:     */ import org.apache.poi.ss.usermodel.Table;
/*   53:     */ import org.apache.poi.ss.util.AreaReference;
/*   54:     */ import org.apache.poi.ss.util.CellAddress;
/*   55:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   56:     */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   57:     */ import org.apache.poi.ss.util.CellReference;
/*   58:     */ import org.apache.poi.ss.util.PaneInformation;
/*   59:     */ import org.apache.poi.ss.util.SSCellRange;
/*   60:     */ import org.apache.poi.ss.util.SheetUtil;
/*   61:     */ import org.apache.poi.util.Internal;
/*   62:     */ import org.apache.poi.util.POILogFactory;
/*   63:     */ import org.apache.poi.util.POILogger;
/*   64:     */ import org.apache.poi.xssf.model.CommentsTable;
/*   65:     */ import org.apache.poi.xssf.model.StylesTable;
/*   66:     */ import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
/*   67:     */ import org.apache.poi.xssf.usermodel.helpers.XSSFIgnoredErrorHelper;
/*   68:     */ import org.apache.poi.xssf.usermodel.helpers.XSSFPasswordHelper;
/*   69:     */ import org.apache.poi.xssf.usermodel.helpers.XSSFRowShifter;
/*   70:     */ import org.apache.xmlbeans.SchemaType;
/*   71:     */ import org.apache.xmlbeans.XmlCursor;
/*   72:     */ import org.apache.xmlbeans.XmlException;
/*   73:     */ import org.apache.xmlbeans.XmlObject;
/*   74:     */ import org.apache.xmlbeans.XmlOptions;
/*   75:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
/*   76:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak;
/*   77:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr;
/*   78:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
/*   79:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
/*   80:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
/*   81:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol.Factory;
/*   82:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
/*   83:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
/*   84:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList;
/*   85:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments;
/*   86:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation;
/*   87:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations;
/*   88:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
/*   89:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing;
/*   90:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*   91:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter.Factory;
/*   92:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink;
/*   93:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks;
/*   94:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError;
/*   95:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors;
/*   96:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing;
/*   97:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell;
/*   98:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells;
/*   99:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
/*  100:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
/*  101:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects.Factory;
/*  102:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr;
/*  103:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr.Factory;
/*  104:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak;
/*  105:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
/*  106:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr;
/*  107:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr.Factory;
/*  108:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane;
/*  109:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition;
/*  110:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions;
/*  111:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
/*  112:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow.Factory;
/*  113:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection;
/*  114:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
/*  115:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr;
/*  116:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData;
/*  117:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension;
/*  118:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
/*  119:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr;
/*  120:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr.Factory;
/*  121:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;
/*  122:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
/*  123:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView.Factory;
/*  124:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews;
/*  125:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews.Factory;
/*  126:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
/*  127:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTablePart;
/*  128:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts;
/*  129:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
/*  130:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  131:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet.Factory;
/*  132:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheetSource;
/*  133:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCalcMode;
/*  134:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;
/*  135:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane;
/*  136:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.Enum;
/*  137:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState;
/*  138:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument;
/*  139:     */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument.Factory;
/*  140:     */ 
/*  141:     */ public class XSSFSheet
/*  142:     */   extends POIXMLDocumentPart
/*  143:     */   implements Sheet
/*  144:     */ {
/*  145: 150 */   private static final POILogger logger = POILogFactory.getLogger(XSSFSheet.class);
/*  146:     */   private static final double DEFAULT_ROW_HEIGHT = 15.0D;
/*  147:     */   private static final double DEFAULT_MARGIN_HEADER = 0.3D;
/*  148:     */   private static final double DEFAULT_MARGIN_FOOTER = 0.3D;
/*  149:     */   private static final double DEFAULT_MARGIN_TOP = 0.75D;
/*  150:     */   private static final double DEFAULT_MARGIN_BOTTOM = 0.75D;
/*  151:     */   private static final double DEFAULT_MARGIN_LEFT = 0.7D;
/*  152:     */   private static final double DEFAULT_MARGIN_RIGHT = 0.7D;
/*  153:     */   public static final int TWIPS_PER_POINT = 20;
/*  154:     */   protected CTSheet sheet;
/*  155:     */   protected CTWorksheet worksheet;
/*  156: 165 */   private final SortedMap<Integer, XSSFRow> _rows = new TreeMap();
/*  157:     */   private List<XSSFHyperlink> hyperlinks;
/*  158:     */   private ColumnHelper columnHelper;
/*  159:     */   private CommentsTable sheetComments;
/*  160:     */   private Map<Integer, CTCellFormula> sharedFormulas;
/*  161:     */   private SortedMap<String, XSSFTable> tables;
/*  162:     */   private List<CellRangeAddress> arrayFormulas;
/*  163:     */   private XSSFDataValidationHelper dataValidationHelper;
/*  164:     */   
/*  165:     */   protected XSSFSheet()
/*  166:     */   {
/*  167: 185 */     this.dataValidationHelper = new XSSFDataValidationHelper(this);
/*  168: 186 */     onDocumentCreate();
/*  169:     */   }
/*  170:     */   
/*  171:     */   protected XSSFSheet(PackagePart part)
/*  172:     */   {
/*  173: 198 */     super(part);
/*  174: 199 */     this.dataValidationHelper = new XSSFDataValidationHelper(this);
/*  175:     */   }
/*  176:     */   
/*  177:     */   public XSSFWorkbook getWorkbook()
/*  178:     */   {
/*  179: 209 */     return (XSSFWorkbook)getParent();
/*  180:     */   }
/*  181:     */   
/*  182:     */   protected void onDocumentRead()
/*  183:     */   {
/*  184:     */     try
/*  185:     */     {
/*  186: 218 */       read(getPackagePart().getInputStream());
/*  187:     */     }
/*  188:     */     catch (IOException e)
/*  189:     */     {
/*  190: 220 */       throw new POIXMLException(e);
/*  191:     */     }
/*  192:     */   }
/*  193:     */   
/*  194:     */   protected void read(InputStream is)
/*  195:     */     throws IOException
/*  196:     */   {
/*  197:     */     try
/*  198:     */     {
/*  199: 226 */       this.worksheet = WorksheetDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getWorksheet();
/*  200:     */     }
/*  201:     */     catch (XmlException e)
/*  202:     */     {
/*  203: 228 */       throw new POIXMLException(e);
/*  204:     */     }
/*  205: 231 */     initRows(this.worksheet);
/*  206: 232 */     this.columnHelper = new ColumnHelper(this.worksheet);
/*  207: 234 */     for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  208:     */     {
/*  209: 235 */       POIXMLDocumentPart p = rp.getDocumentPart();
/*  210: 236 */       if ((p instanceof CommentsTable)) {
/*  211: 237 */         this.sheetComments = ((CommentsTable)p);
/*  212:     */       }
/*  213: 239 */       if ((p instanceof XSSFTable)) {
/*  214: 240 */         this.tables.put(rp.getRelationship().getId(), (XSSFTable)p);
/*  215:     */       }
/*  216: 242 */       if ((p instanceof XSSFPivotTable)) {
/*  217: 243 */         getWorkbook().getPivotTables().add((XSSFPivotTable)p);
/*  218:     */       }
/*  219:     */     }
/*  220: 248 */     initHyperlinks();
/*  221:     */   }
/*  222:     */   
/*  223:     */   protected void onDocumentCreate()
/*  224:     */   {
/*  225: 256 */     this.worksheet = newSheet();
/*  226: 257 */     initRows(this.worksheet);
/*  227: 258 */     this.columnHelper = new ColumnHelper(this.worksheet);
/*  228: 259 */     this.hyperlinks = new ArrayList();
/*  229:     */   }
/*  230:     */   
/*  231:     */   private void initRows(CTWorksheet worksheetParam)
/*  232:     */   {
/*  233: 263 */     this._rows.clear();
/*  234: 264 */     this.tables = new TreeMap();
/*  235: 265 */     this.sharedFormulas = new HashMap();
/*  236: 266 */     this.arrayFormulas = new ArrayList();
/*  237: 267 */     for (CTRow row : worksheetParam.getSheetData().getRowArray())
/*  238:     */     {
/*  239: 268 */       XSSFRow r = new XSSFRow(row, this);
/*  240:     */       
/*  241: 270 */       Integer rownumI = new Integer(r.getRowNum());
/*  242: 271 */       this._rows.put(rownumI, r);
/*  243:     */     }
/*  244:     */   }
/*  245:     */   
/*  246:     */   private void initHyperlinks()
/*  247:     */   {
/*  248: 280 */     this.hyperlinks = new ArrayList();
/*  249: 282 */     if (!this.worksheet.isSetHyperlinks()) {
/*  250: 283 */       return;
/*  251:     */     }
/*  252:     */     try
/*  253:     */     {
/*  254: 287 */       PackageRelationshipCollection hyperRels = getPackagePart().getRelationshipsByType(XSSFRelation.SHEET_HYPERLINKS.getRelation());
/*  255: 291 */       for (CTHyperlink hyperlink : this.worksheet.getHyperlinks().getHyperlinkArray())
/*  256:     */       {
/*  257: 292 */         PackageRelationship hyperRel = null;
/*  258: 293 */         if (hyperlink.getId() != null) {
/*  259: 294 */           hyperRel = hyperRels.getRelationshipByID(hyperlink.getId());
/*  260:     */         }
/*  261: 297 */         this.hyperlinks.add(new XSSFHyperlink(hyperlink, hyperRel));
/*  262:     */       }
/*  263:     */     }
/*  264:     */     catch (InvalidFormatException e)
/*  265:     */     {
/*  266: 300 */       throw new POIXMLException(e);
/*  267:     */     }
/*  268:     */   }
/*  269:     */   
/*  270:     */   private static CTWorksheet newSheet()
/*  271:     */   {
/*  272: 310 */     CTWorksheet worksheet = CTWorksheet.Factory.newInstance();
/*  273: 311 */     CTSheetFormatPr ctFormat = worksheet.addNewSheetFormatPr();
/*  274: 312 */     ctFormat.setDefaultRowHeight(15.0D);
/*  275:     */     
/*  276: 314 */     CTSheetView ctView = worksheet.addNewSheetViews().addNewSheetView();
/*  277: 315 */     ctView.setWorkbookViewId(0L);
/*  278:     */     
/*  279: 317 */     worksheet.addNewDimension().setRef("A1");
/*  280:     */     
/*  281: 319 */     worksheet.addNewSheetData();
/*  282:     */     
/*  283: 321 */     CTPageMargins ctMargins = worksheet.addNewPageMargins();
/*  284: 322 */     ctMargins.setBottom(0.75D);
/*  285: 323 */     ctMargins.setFooter(0.3D);
/*  286: 324 */     ctMargins.setHeader(0.3D);
/*  287: 325 */     ctMargins.setLeft(0.7D);
/*  288: 326 */     ctMargins.setRight(0.7D);
/*  289: 327 */     ctMargins.setTop(0.75D);
/*  290:     */     
/*  291: 329 */     return worksheet;
/*  292:     */   }
/*  293:     */   
/*  294:     */   @Internal
/*  295:     */   public CTWorksheet getCTWorksheet()
/*  296:     */   {
/*  297: 339 */     return this.worksheet;
/*  298:     */   }
/*  299:     */   
/*  300:     */   public ColumnHelper getColumnHelper()
/*  301:     */   {
/*  302: 343 */     return this.columnHelper;
/*  303:     */   }
/*  304:     */   
/*  305:     */   public String getSheetName()
/*  306:     */   {
/*  307: 353 */     return this.sheet.getName();
/*  308:     */   }
/*  309:     */   
/*  310:     */   public int addMergedRegion(CellRangeAddress region)
/*  311:     */   {
/*  312: 367 */     return addMergedRegion(region, true);
/*  313:     */   }
/*  314:     */   
/*  315:     */   public int addMergedRegionUnsafe(CellRangeAddress region)
/*  316:     */   {
/*  317: 385 */     return addMergedRegion(region, false);
/*  318:     */   }
/*  319:     */   
/*  320:     */   private int addMergedRegion(CellRangeAddress region, boolean validate)
/*  321:     */   {
/*  322: 401 */     if (region.getNumberOfCells() < 2) {
/*  323: 402 */       throw new IllegalArgumentException("Merged region " + region.formatAsString() + " must contain 2 or more cells");
/*  324:     */     }
/*  325: 404 */     region.validate(SpreadsheetVersion.EXCEL2007);
/*  326: 406 */     if (validate)
/*  327:     */     {
/*  328: 409 */       validateArrayFormulas(region);
/*  329:     */       
/*  330:     */ 
/*  331:     */ 
/*  332: 413 */       validateMergedRegions(region);
/*  333:     */     }
/*  334: 416 */     CTMergeCells ctMergeCells = this.worksheet.isSetMergeCells() ? this.worksheet.getMergeCells() : this.worksheet.addNewMergeCells();
/*  335: 417 */     CTMergeCell ctMergeCell = ctMergeCells.addNewMergeCell();
/*  336: 418 */     ctMergeCell.setRef(region.formatAsString());
/*  337: 419 */     return ctMergeCells.sizeOfMergeCellArray();
/*  338:     */   }
/*  339:     */   
/*  340:     */   private void validateArrayFormulas(CellRangeAddress region)
/*  341:     */   {
/*  342: 431 */     int firstRow = region.getFirstRow();
/*  343: 432 */     int firstColumn = region.getFirstColumn();
/*  344: 433 */     int lastRow = region.getLastRow();
/*  345: 434 */     int lastColumn = region.getLastColumn();
/*  346: 436 */     for (int rowIn = firstRow; rowIn <= lastRow; rowIn++)
/*  347:     */     {
/*  348: 437 */       XSSFRow row = getRow(rowIn);
/*  349: 438 */       if (row != null) {
/*  350: 442 */         for (int colIn = firstColumn; colIn <= lastColumn; colIn++)
/*  351:     */         {
/*  352: 443 */           XSSFCell cell = row.getCell(colIn);
/*  353: 444 */           if (cell != null) {
/*  354: 448 */             if (cell.isPartOfArrayFormulaGroup())
/*  355:     */             {
/*  356: 449 */               CellRangeAddress arrayRange = cell.getArrayFormulaRange();
/*  357: 450 */               if ((arrayRange.getNumberOfCells() > 1) && (region.intersects(arrayRange)))
/*  358:     */               {
/*  359: 451 */                 String msg = "The range " + region.formatAsString() + " intersects with a multi-cell array formula. " + "You cannot merge cells of an array.";
/*  360:     */                 
/*  361: 453 */                 throw new IllegalStateException(msg);
/*  362:     */               }
/*  363:     */             }
/*  364:     */           }
/*  365:     */         }
/*  366:     */       }
/*  367:     */     }
/*  368:     */   }
/*  369:     */   
/*  370:     */   private void checkForMergedRegionsIntersectingArrayFormulas()
/*  371:     */   {
/*  372: 466 */     for (CellRangeAddress region : getMergedRegions()) {
/*  373: 467 */       validateArrayFormulas(region);
/*  374:     */     }
/*  375:     */   }
/*  376:     */   
/*  377:     */   private void validateMergedRegions(CellRangeAddress candidateRegion)
/*  378:     */   {
/*  379: 478 */     for (CellRangeAddress existingRegion : getMergedRegions()) {
/*  380: 479 */       if (existingRegion.intersects(candidateRegion)) {
/*  381: 480 */         throw new IllegalStateException("Cannot add merged region " + candidateRegion.formatAsString() + " to sheet because it overlaps with an existing merged region (" + existingRegion.formatAsString() + ").");
/*  382:     */       }
/*  383:     */     }
/*  384:     */   }
/*  385:     */   
/*  386:     */   private void checkForIntersectingMergedRegions()
/*  387:     */   {
/*  388: 492 */     List<CellRangeAddress> regions = getMergedRegions();
/*  389: 493 */     int size = regions.size();
/*  390:     */     CellRangeAddress region;
/*  391: 494 */     for (int i = 0; i < size; i++)
/*  392:     */     {
/*  393: 495 */       region = (CellRangeAddress)regions.get(i);
/*  394: 496 */       for (CellRangeAddress other : regions.subList(i + 1, regions.size())) {
/*  395: 497 */         if (region.intersects(other))
/*  396:     */         {
/*  397: 498 */           String msg = "The range " + region.formatAsString() + " intersects with another merged region " + other.formatAsString() + " in this sheet";
/*  398:     */           
/*  399:     */ 
/*  400: 501 */           throw new IllegalStateException(msg);
/*  401:     */         }
/*  402:     */       }
/*  403:     */     }
/*  404:     */   }
/*  405:     */   
/*  406:     */   public void validateMergedRegions()
/*  407:     */   {
/*  408: 516 */     checkForMergedRegionsIntersectingArrayFormulas();
/*  409: 517 */     checkForIntersectingMergedRegions();
/*  410:     */   }
/*  411:     */   
/*  412:     */   public void autoSizeColumn(int column)
/*  413:     */   {
/*  414: 531 */     autoSizeColumn(column, false);
/*  415:     */   }
/*  416:     */   
/*  417:     */   public void autoSizeColumn(int column, boolean useMergedCells)
/*  418:     */   {
/*  419: 549 */     double width = SheetUtil.getColumnWidth(this, column, useMergedCells);
/*  420: 551 */     if (width != -1.0D)
/*  421:     */     {
/*  422: 552 */       width *= 256.0D;
/*  423: 553 */       int maxColumnWidth = 65280;
/*  424: 554 */       if (width > maxColumnWidth) {
/*  425: 555 */         width = maxColumnWidth;
/*  426:     */       }
/*  427: 557 */       setColumnWidth(column, (int)width);
/*  428: 558 */       this.columnHelper.setColBestFit(column, true);
/*  429:     */     }
/*  430:     */   }
/*  431:     */   
/*  432:     */   public XSSFDrawing getDrawingPatriarch()
/*  433:     */   {
/*  434: 571 */     CTDrawing ctDrawing = getCTDrawing();
/*  435: 572 */     if (ctDrawing != null)
/*  436:     */     {
/*  437: 574 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  438:     */       {
/*  439: 575 */         POIXMLDocumentPart p = rp.getDocumentPart();
/*  440: 576 */         if ((p instanceof XSSFDrawing))
/*  441:     */         {
/*  442: 577 */           XSSFDrawing dr = (XSSFDrawing)p;
/*  443: 578 */           String drId = rp.getRelationship().getId();
/*  444: 579 */           if (!drId.equals(ctDrawing.getId())) {
/*  445:     */             break;
/*  446:     */           }
/*  447: 580 */           return dr;
/*  448:     */         }
/*  449:     */       }
/*  450: 585 */       logger.log(7, new Object[] { "Can't find drawing with id=" + ctDrawing.getId() + " in the list of the sheet's relationships" });
/*  451:     */     }
/*  452: 587 */     return null;
/*  453:     */   }
/*  454:     */   
/*  455:     */   public XSSFDrawing createDrawingPatriarch()
/*  456:     */   {
/*  457: 597 */     CTDrawing ctDrawing = getCTDrawing();
/*  458: 598 */     if (ctDrawing != null) {
/*  459: 599 */       return getDrawingPatriarch();
/*  460:     */     }
/*  461: 603 */     int drawingNumber = getPackagePart().getPackage().getPartsByContentType(XSSFRelation.DRAWINGS.getContentType()).size() + 1;
/*  462: 604 */     drawingNumber = getNextPartNumber(XSSFRelation.DRAWINGS, drawingNumber);
/*  463: 605 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.DRAWINGS, XSSFFactory.getInstance(), drawingNumber, false);
/*  464: 606 */     XSSFDrawing drawing = (XSSFDrawing)rp.getDocumentPart();
/*  465: 607 */     String relId = rp.getRelationship().getId();
/*  466:     */     
/*  467:     */ 
/*  468:     */ 
/*  469: 611 */     ctDrawing = this.worksheet.addNewDrawing();
/*  470: 612 */     ctDrawing.setId(relId);
/*  471:     */     
/*  472:     */ 
/*  473: 615 */     return drawing;
/*  474:     */   }
/*  475:     */   
/*  476:     */   protected XSSFVMLDrawing getVMLDrawing(boolean autoCreate)
/*  477:     */   {
/*  478: 626 */     XSSFVMLDrawing drawing = null;
/*  479: 627 */     CTLegacyDrawing ctDrawing = getCTLegacyDrawing();
/*  480: 628 */     if (ctDrawing == null)
/*  481:     */     {
/*  482: 629 */       if (autoCreate)
/*  483:     */       {
/*  484: 631 */         int drawingNumber = getPackagePart().getPackage().getPartsByContentType(XSSFRelation.VML_DRAWINGS.getContentType()).size() + 1;
/*  485: 632 */         POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.VML_DRAWINGS, XSSFFactory.getInstance(), drawingNumber, false);
/*  486: 633 */         drawing = (XSSFVMLDrawing)rp.getDocumentPart();
/*  487: 634 */         String relId = rp.getRelationship().getId();
/*  488:     */         
/*  489:     */ 
/*  490:     */ 
/*  491: 638 */         ctDrawing = this.worksheet.addNewLegacyDrawing();
/*  492: 639 */         ctDrawing.setId(relId);
/*  493:     */       }
/*  494:     */     }
/*  495:     */     else
/*  496:     */     {
/*  497: 643 */       String id = ctDrawing.getId();
/*  498: 644 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/*  499:     */       {
/*  500: 645 */         POIXMLDocumentPart p = rp.getDocumentPart();
/*  501: 646 */         if ((p instanceof XSSFVMLDrawing))
/*  502:     */         {
/*  503: 647 */           XSSFVMLDrawing dr = (XSSFVMLDrawing)p;
/*  504: 648 */           String drId = rp.getRelationship().getId();
/*  505: 649 */           if (drId.equals(id))
/*  506:     */           {
/*  507: 650 */             drawing = dr;
/*  508: 651 */             break;
/*  509:     */           }
/*  510:     */         }
/*  511:     */       }
/*  512: 656 */       if (drawing == null) {
/*  513: 657 */         logger.log(7, new Object[] { "Can't find VML drawing with id=" + id + " in the list of the sheet's relationships" });
/*  514:     */       }
/*  515:     */     }
/*  516: 660 */     return drawing;
/*  517:     */   }
/*  518:     */   
/*  519:     */   protected CTDrawing getCTDrawing()
/*  520:     */   {
/*  521: 664 */     return this.worksheet.getDrawing();
/*  522:     */   }
/*  523:     */   
/*  524:     */   protected CTLegacyDrawing getCTLegacyDrawing()
/*  525:     */   {
/*  526: 667 */     return this.worksheet.getLegacyDrawing();
/*  527:     */   }
/*  528:     */   
/*  529:     */   public void createFreezePane(int colSplit, int rowSplit)
/*  530:     */   {
/*  531: 677 */     createFreezePane(colSplit, rowSplit, colSplit, rowSplit);
/*  532:     */   }
/*  533:     */   
/*  534:     */   public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
/*  535:     */   {
/*  536: 694 */     CTSheetView ctView = getDefaultSheetView();
/*  537: 697 */     if ((colSplit == 0) && (rowSplit == 0))
/*  538:     */     {
/*  539: 698 */       if (ctView.isSetPane()) {
/*  540: 699 */         ctView.unsetPane();
/*  541:     */       }
/*  542: 701 */       ctView.setSelectionArray(null);
/*  543: 702 */       return;
/*  544:     */     }
/*  545: 705 */     if (!ctView.isSetPane()) {
/*  546: 706 */       ctView.addNewPane();
/*  547:     */     }
/*  548: 708 */     CTPane pane = ctView.getPane();
/*  549: 710 */     if (colSplit > 0) {
/*  550: 711 */       pane.setXSplit(colSplit);
/*  551: 713 */     } else if (pane.isSetXSplit()) {
/*  552: 714 */       pane.unsetXSplit();
/*  553:     */     }
/*  554: 717 */     if (rowSplit > 0) {
/*  555: 718 */       pane.setYSplit(rowSplit);
/*  556: 720 */     } else if (pane.isSetYSplit()) {
/*  557: 721 */       pane.unsetYSplit();
/*  558:     */     }
/*  559: 725 */     pane.setState(STPaneState.FROZEN);
/*  560: 726 */     if (rowSplit == 0)
/*  561:     */     {
/*  562: 727 */       pane.setTopLeftCell(new CellReference(0, leftmostColumn).formatAsString());
/*  563: 728 */       pane.setActivePane(STPane.TOP_RIGHT);
/*  564:     */     }
/*  565: 729 */     else if (colSplit == 0)
/*  566:     */     {
/*  567: 730 */       pane.setTopLeftCell(new CellReference(topRow, 0).formatAsString());
/*  568: 731 */       pane.setActivePane(STPane.BOTTOM_LEFT);
/*  569:     */     }
/*  570:     */     else
/*  571:     */     {
/*  572: 733 */       pane.setTopLeftCell(new CellReference(topRow, leftmostColumn).formatAsString());
/*  573: 734 */       pane.setActivePane(STPane.BOTTOM_RIGHT);
/*  574:     */     }
/*  575: 737 */     ctView.setSelectionArray(null);
/*  576: 738 */     CTSelection sel = ctView.addNewSelection();
/*  577: 739 */     sel.setPane(pane.getActivePane());
/*  578:     */   }
/*  579:     */   
/*  580:     */   public XSSFRow createRow(int rownum)
/*  581:     */   {
/*  582: 755 */     Integer rownumI = new Integer(rownum);
/*  583:     */     
/*  584: 757 */     XSSFRow prev = (XSSFRow)this._rows.get(rownumI);
/*  585:     */     CTRow ctRow;
/*  586: 758 */     if (prev != null)
/*  587:     */     {
/*  588: 764 */       while (prev.getFirstCellNum() != -1) {
/*  589: 765 */         prev.removeCell(prev.getCell(prev.getFirstCellNum()));
/*  590:     */       }
/*  591: 768 */       CTRow ctRow = prev.getCTRow();
/*  592: 769 */       ctRow.set(CTRow.Factory.newInstance());
/*  593:     */     }
/*  594:     */     else
/*  595:     */     {
/*  596:     */       CTRow ctRow;
/*  597: 771 */       if ((this._rows.isEmpty()) || (rownum > ((Integer)this._rows.lastKey()).intValue()))
/*  598:     */       {
/*  599: 773 */         ctRow = this.worksheet.getSheetData().addNewRow();
/*  600:     */       }
/*  601:     */       else
/*  602:     */       {
/*  603: 777 */         int idx = this._rows.headMap(rownumI).size();
/*  604: 778 */         ctRow = this.worksheet.getSheetData().insertNewRow(idx);
/*  605:     */       }
/*  606:     */     }
/*  607: 781 */     XSSFRow r = new XSSFRow(ctRow, this);
/*  608: 782 */     r.setRowNum(rownum);
/*  609: 783 */     this._rows.put(rownumI, r);
/*  610: 784 */     return r;
/*  611:     */   }
/*  612:     */   
/*  613:     */   public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane)
/*  614:     */   {
/*  615: 802 */     createFreezePane(xSplitPos, ySplitPos, leftmostColumn, topRow);
/*  616: 803 */     getPane().setState(STPaneState.SPLIT);
/*  617: 804 */     getPane().setActivePane(STPane.Enum.forInt(activePane));
/*  618:     */   }
/*  619:     */   
/*  620:     */   public XSSFComment getCellComment(CellAddress address)
/*  621:     */   {
/*  622: 815 */     if (this.sheetComments == null) {
/*  623: 816 */       return null;
/*  624:     */     }
/*  625: 819 */     int row = address.getRow();
/*  626: 820 */     int column = address.getColumn();
/*  627:     */     
/*  628: 822 */     CellAddress ref = new CellAddress(row, column);
/*  629: 823 */     CTComment ctComment = this.sheetComments.getCTComment(ref);
/*  630: 824 */     if (ctComment == null) {
/*  631: 825 */       return null;
/*  632:     */     }
/*  633: 828 */     XSSFVMLDrawing vml = getVMLDrawing(false);
/*  634: 829 */     return new XSSFComment(this.sheetComments, ctComment, vml == null ? null : vml.findCommentShape(row, column));
/*  635:     */   }
/*  636:     */   
/*  637:     */   public Map<CellAddress, XSSFComment> getCellComments()
/*  638:     */   {
/*  639: 840 */     if (this.sheetComments == null) {
/*  640: 841 */       return Collections.emptyMap();
/*  641:     */     }
/*  642: 843 */     return this.sheetComments.getCellComments();
/*  643:     */   }
/*  644:     */   
/*  645:     */   public XSSFHyperlink getHyperlink(int row, int column)
/*  646:     */   {
/*  647: 855 */     return getHyperlink(new CellAddress(row, column));
/*  648:     */   }
/*  649:     */   
/*  650:     */   public XSSFHyperlink getHyperlink(CellAddress addr)
/*  651:     */   {
/*  652: 867 */     String ref = addr.formatAsString();
/*  653: 868 */     for (XSSFHyperlink hyperlink : this.hyperlinks) {
/*  654: 869 */       if (hyperlink.getCellRef().equals(ref)) {
/*  655: 870 */         return hyperlink;
/*  656:     */       }
/*  657:     */     }
/*  658: 873 */     return null;
/*  659:     */   }
/*  660:     */   
/*  661:     */   public List<XSSFHyperlink> getHyperlinkList()
/*  662:     */   {
/*  663: 883 */     return Collections.unmodifiableList(this.hyperlinks);
/*  664:     */   }
/*  665:     */   
/*  666:     */   private int[] getBreaks(CTPageBreak ctPageBreak)
/*  667:     */   {
/*  668: 887 */     CTBreak[] brkArray = ctPageBreak.getBrkArray();
/*  669: 888 */     int[] breaks = new int[brkArray.length];
/*  670: 889 */     for (int i = 0; i < brkArray.length; i++) {
/*  671: 890 */       breaks[i] = ((int)brkArray[i].getId() - 1);
/*  672:     */     }
/*  673: 892 */     return breaks;
/*  674:     */   }
/*  675:     */   
/*  676:     */   private void removeBreak(int index, CTPageBreak ctPageBreak)
/*  677:     */   {
/*  678: 896 */     int index1 = index + 1;
/*  679: 897 */     CTBreak[] brkArray = ctPageBreak.getBrkArray();
/*  680: 898 */     for (int i = 0; i < brkArray.length; i++) {
/*  681: 899 */       if (brkArray[i].getId() == index1) {
/*  682: 900 */         ctPageBreak.removeBrk(i);
/*  683:     */       }
/*  684:     */     }
/*  685:     */   }
/*  686:     */   
/*  687:     */   public int[] getColumnBreaks()
/*  688:     */   {
/*  689: 914 */     return this.worksheet.isSetColBreaks() ? getBreaks(this.worksheet.getColBreaks()) : new int[0];
/*  690:     */   }
/*  691:     */   
/*  692:     */   public int getColumnWidth(int columnIndex)
/*  693:     */   {
/*  694: 932 */     CTCol col = this.columnHelper.getColumn(columnIndex, false);
/*  695: 933 */     double width = (col == null) || (!col.isSetWidth()) ? getDefaultColumnWidth() : col.getWidth();
/*  696: 934 */     return (int)(width * 256.0D);
/*  697:     */   }
/*  698:     */   
/*  699:     */   public float getColumnWidthInPixels(int columnIndex)
/*  700:     */   {
/*  701: 947 */     float widthIn256 = getColumnWidth(columnIndex);
/*  702: 948 */     return (float)(widthIn256 / 256.0D * 7.001699924468994D);
/*  703:     */   }
/*  704:     */   
/*  705:     */   public int getDefaultColumnWidth()
/*  706:     */   {
/*  707: 962 */     CTSheetFormatPr pr = this.worksheet.getSheetFormatPr();
/*  708: 963 */     return pr == null ? 8 : (int)pr.getBaseColWidth();
/*  709:     */   }
/*  710:     */   
/*  711:     */   public short getDefaultRowHeight()
/*  712:     */   {
/*  713: 974 */     return (short)(int)(getDefaultRowHeightInPoints() * 20.0F);
/*  714:     */   }
/*  715:     */   
/*  716:     */   public float getDefaultRowHeightInPoints()
/*  717:     */   {
/*  718: 985 */     CTSheetFormatPr pr = this.worksheet.getSheetFormatPr();
/*  719: 986 */     return (float)(pr == null ? 0.0D : pr.getDefaultRowHeight());
/*  720:     */   }
/*  721:     */   
/*  722:     */   private CTSheetFormatPr getSheetTypeSheetFormatPr()
/*  723:     */   {
/*  724: 990 */     return this.worksheet.isSetSheetFormatPr() ? this.worksheet.getSheetFormatPr() : this.worksheet.addNewSheetFormatPr();
/*  725:     */   }
/*  726:     */   
/*  727:     */   public CellStyle getColumnStyle(int column)
/*  728:     */   {
/*  729:1002 */     int idx = this.columnHelper.getColDefaultStyle(column);
/*  730:1003 */     return getWorkbook().getCellStyleAt((short)(idx == -1 ? 0 : idx));
/*  731:     */   }
/*  732:     */   
/*  733:     */   public void setRightToLeft(boolean value)
/*  734:     */   {
/*  735:1013 */     CTSheetView view = getDefaultSheetView();
/*  736:1014 */     view.setRightToLeft(value);
/*  737:     */   }
/*  738:     */   
/*  739:     */   public boolean isRightToLeft()
/*  740:     */   {
/*  741:1024 */     CTSheetView view = getDefaultSheetView();
/*  742:1025 */     return (view != null) && (view.getRightToLeft());
/*  743:     */   }
/*  744:     */   
/*  745:     */   public boolean getDisplayGuts()
/*  746:     */   {
/*  747:1036 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/*  748:1037 */     CTOutlinePr outlinePr = sheetPr.getOutlinePr() == null ? CTOutlinePr.Factory.newInstance() : sheetPr.getOutlinePr();
/*  749:1038 */     return outlinePr.getShowOutlineSymbols();
/*  750:     */   }
/*  751:     */   
/*  752:     */   public void setDisplayGuts(boolean value)
/*  753:     */   {
/*  754:1048 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/*  755:1049 */     CTOutlinePr outlinePr = sheetPr.getOutlinePr() == null ? sheetPr.addNewOutlinePr() : sheetPr.getOutlinePr();
/*  756:1050 */     outlinePr.setShowOutlineSymbols(value);
/*  757:     */   }
/*  758:     */   
/*  759:     */   public boolean isDisplayZeros()
/*  760:     */   {
/*  761:1061 */     CTSheetView view = getDefaultSheetView();
/*  762:1062 */     return (view == null) || (view.getShowZeros());
/*  763:     */   }
/*  764:     */   
/*  765:     */   public void setDisplayZeros(boolean value)
/*  766:     */   {
/*  767:1073 */     CTSheetView view = getSheetTypeSheetView();
/*  768:1074 */     view.setShowZeros(value);
/*  769:     */   }
/*  770:     */   
/*  771:     */   public int getFirstRowNum()
/*  772:     */   {
/*  773:1084 */     return this._rows.isEmpty() ? 0 : ((Integer)this._rows.firstKey()).intValue();
/*  774:     */   }
/*  775:     */   
/*  776:     */   public boolean getFitToPage()
/*  777:     */   {
/*  778:1094 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/*  779:1095 */     CTPageSetUpPr psSetup = (sheetPr == null) || (!sheetPr.isSetPageSetUpPr()) ? CTPageSetUpPr.Factory.newInstance() : sheetPr.getPageSetUpPr();
/*  780:     */     
/*  781:1097 */     return psSetup.getFitToPage();
/*  782:     */   }
/*  783:     */   
/*  784:     */   private CTSheetPr getSheetTypeSheetPr()
/*  785:     */   {
/*  786:1101 */     if (this.worksheet.getSheetPr() == null) {
/*  787:1102 */       this.worksheet.setSheetPr(CTSheetPr.Factory.newInstance());
/*  788:     */     }
/*  789:1104 */     return this.worksheet.getSheetPr();
/*  790:     */   }
/*  791:     */   
/*  792:     */   private CTHeaderFooter getSheetTypeHeaderFooter()
/*  793:     */   {
/*  794:1108 */     if (this.worksheet.getHeaderFooter() == null) {
/*  795:1109 */       this.worksheet.setHeaderFooter(CTHeaderFooter.Factory.newInstance());
/*  796:     */     }
/*  797:1111 */     return this.worksheet.getHeaderFooter();
/*  798:     */   }
/*  799:     */   
/*  800:     */   public Footer getFooter()
/*  801:     */   {
/*  802:1127 */     return getOddFooter();
/*  803:     */   }
/*  804:     */   
/*  805:     */   public Header getHeader()
/*  806:     */   {
/*  807:1141 */     return getOddHeader();
/*  808:     */   }
/*  809:     */   
/*  810:     */   public Footer getOddFooter()
/*  811:     */   {
/*  812:1150 */     return new XSSFOddFooter(getSheetTypeHeaderFooter());
/*  813:     */   }
/*  814:     */   
/*  815:     */   public Footer getEvenFooter()
/*  816:     */   {
/*  817:1157 */     return new XSSFEvenFooter(getSheetTypeHeaderFooter());
/*  818:     */   }
/*  819:     */   
/*  820:     */   public Footer getFirstFooter()
/*  821:     */   {
/*  822:1164 */     return new XSSFFirstFooter(getSheetTypeHeaderFooter());
/*  823:     */   }
/*  824:     */   
/*  825:     */   public Header getOddHeader()
/*  826:     */   {
/*  827:1173 */     return new XSSFOddHeader(getSheetTypeHeaderFooter());
/*  828:     */   }
/*  829:     */   
/*  830:     */   public Header getEvenHeader()
/*  831:     */   {
/*  832:1180 */     return new XSSFEvenHeader(getSheetTypeHeaderFooter());
/*  833:     */   }
/*  834:     */   
/*  835:     */   public Header getFirstHeader()
/*  836:     */   {
/*  837:1187 */     return new XSSFFirstHeader(getSheetTypeHeaderFooter());
/*  838:     */   }
/*  839:     */   
/*  840:     */   public boolean getHorizontallyCenter()
/*  841:     */   {
/*  842:1196 */     CTPrintOptions opts = this.worksheet.getPrintOptions();
/*  843:1197 */     return (opts != null) && (opts.getHorizontalCentered());
/*  844:     */   }
/*  845:     */   
/*  846:     */   public int getLastRowNum()
/*  847:     */   {
/*  848:1202 */     return this._rows.isEmpty() ? 0 : ((Integer)this._rows.lastKey()).intValue();
/*  849:     */   }
/*  850:     */   
/*  851:     */   public short getLeftCol()
/*  852:     */   {
/*  853:1207 */     String cellRef = this.worksheet.getSheetViews().getSheetViewArray(0).getTopLeftCell();
/*  854:1208 */     if (cellRef == null) {
/*  855:1209 */       return 0;
/*  856:     */     }
/*  857:1211 */     CellReference cellReference = new CellReference(cellRef);
/*  858:1212 */     return cellReference.getCol();
/*  859:     */   }
/*  860:     */   
/*  861:     */   public double getMargin(short margin)
/*  862:     */   {
/*  863:1229 */     if (!this.worksheet.isSetPageMargins()) {
/*  864:1230 */       return 0.0D;
/*  865:     */     }
/*  866:1233 */     CTPageMargins pageMargins = this.worksheet.getPageMargins();
/*  867:1234 */     switch (margin)
/*  868:     */     {
/*  869:     */     case 0: 
/*  870:1236 */       return pageMargins.getLeft();
/*  871:     */     case 1: 
/*  872:1238 */       return pageMargins.getRight();
/*  873:     */     case 2: 
/*  874:1240 */       return pageMargins.getTop();
/*  875:     */     case 3: 
/*  876:1242 */       return pageMargins.getBottom();
/*  877:     */     case 4: 
/*  878:1244 */       return pageMargins.getHeader();
/*  879:     */     case 5: 
/*  880:1246 */       return pageMargins.getFooter();
/*  881:     */     }
/*  882:1248 */     throw new IllegalArgumentException("Unknown margin constant:  " + margin);
/*  883:     */   }
/*  884:     */   
/*  885:     */   public void setMargin(short margin, double size)
/*  886:     */   {
/*  887:1266 */     CTPageMargins pageMargins = this.worksheet.isSetPageMargins() ? this.worksheet.getPageMargins() : this.worksheet.addNewPageMargins();
/*  888:1268 */     switch (margin)
/*  889:     */     {
/*  890:     */     case 0: 
/*  891:1270 */       pageMargins.setLeft(size);
/*  892:1271 */       break;
/*  893:     */     case 1: 
/*  894:1273 */       pageMargins.setRight(size);
/*  895:1274 */       break;
/*  896:     */     case 2: 
/*  897:1276 */       pageMargins.setTop(size);
/*  898:1277 */       break;
/*  899:     */     case 3: 
/*  900:1279 */       pageMargins.setBottom(size);
/*  901:1280 */       break;
/*  902:     */     case 4: 
/*  903:1282 */       pageMargins.setHeader(size);
/*  904:1283 */       break;
/*  905:     */     case 5: 
/*  906:1285 */       pageMargins.setFooter(size);
/*  907:1286 */       break;
/*  908:     */     default: 
/*  909:1288 */       throw new IllegalArgumentException("Unknown margin constant:  " + margin);
/*  910:     */     }
/*  911:     */   }
/*  912:     */   
/*  913:     */   public CellRangeAddress getMergedRegion(int index)
/*  914:     */   {
/*  915:1301 */     CTMergeCells ctMergeCells = this.worksheet.getMergeCells();
/*  916:1302 */     if (ctMergeCells == null) {
/*  917:1303 */       throw new IllegalStateException("This worksheet does not contain merged regions");
/*  918:     */     }
/*  919:1306 */     CTMergeCell ctMergeCell = ctMergeCells.getMergeCellArray(index);
/*  920:1307 */     String ref = ctMergeCell.getRef();
/*  921:1308 */     return CellRangeAddress.valueOf(ref);
/*  922:     */   }
/*  923:     */   
/*  924:     */   public List<CellRangeAddress> getMergedRegions()
/*  925:     */   {
/*  926:1319 */     List<CellRangeAddress> addresses = new ArrayList();
/*  927:1320 */     CTMergeCells ctMergeCells = this.worksheet.getMergeCells();
/*  928:1321 */     if (ctMergeCells == null) {
/*  929:1322 */       return addresses;
/*  930:     */     }
/*  931:1325 */     for (CTMergeCell ctMergeCell : ctMergeCells.getMergeCellArray())
/*  932:     */     {
/*  933:1326 */       String ref = ctMergeCell.getRef();
/*  934:1327 */       addresses.add(CellRangeAddress.valueOf(ref));
/*  935:     */     }
/*  936:1329 */     return addresses;
/*  937:     */   }
/*  938:     */   
/*  939:     */   public int getNumMergedRegions()
/*  940:     */   {
/*  941:1339 */     CTMergeCells ctMergeCells = this.worksheet.getMergeCells();
/*  942:1340 */     return ctMergeCells == null ? 0 : ctMergeCells.sizeOfMergeCellArray();
/*  943:     */   }
/*  944:     */   
/*  945:     */   public int getNumHyperlinks()
/*  946:     */   {
/*  947:1344 */     return this.hyperlinks.size();
/*  948:     */   }
/*  949:     */   
/*  950:     */   public PaneInformation getPaneInformation()
/*  951:     */   {
/*  952:1354 */     CTPane pane = getDefaultSheetView().getPane();
/*  953:1356 */     if (pane == null) {
/*  954:1357 */       return null;
/*  955:     */     }
/*  956:1360 */     CellReference cellRef = pane.isSetTopLeftCell() ? new CellReference(pane.getTopLeftCell()) : null;
/*  957:1361 */     return new PaneInformation((short)(int)pane.getXSplit(), (short)(int)pane.getYSplit(), (short)(cellRef == null ? 0 : cellRef.getRow()), cellRef == null ? 0 : cellRef.getCol(), (byte)(pane.getActivePane().intValue() - 1), pane.getState() == STPaneState.FROZEN);
/*  958:     */   }
/*  959:     */   
/*  960:     */   public int getPhysicalNumberOfRows()
/*  961:     */   {
/*  962:1373 */     return this._rows.size();
/*  963:     */   }
/*  964:     */   
/*  965:     */   public XSSFPrintSetup getPrintSetup()
/*  966:     */   {
/*  967:1383 */     return new XSSFPrintSetup(this.worksheet);
/*  968:     */   }
/*  969:     */   
/*  970:     */   public boolean getProtect()
/*  971:     */   {
/*  972:1393 */     return isSheetLocked();
/*  973:     */   }
/*  974:     */   
/*  975:     */   public void protectSheet(String password)
/*  976:     */   {
/*  977:1405 */     if (password != null)
/*  978:     */     {
/*  979:1406 */       CTSheetProtection sheetProtection = safeGetProtectionField();
/*  980:1407 */       setSheetPassword(password, null);
/*  981:1408 */       sheetProtection.setSheet(true);
/*  982:1409 */       sheetProtection.setScenarios(true);
/*  983:1410 */       sheetProtection.setObjects(true);
/*  984:     */     }
/*  985:     */     else
/*  986:     */     {
/*  987:1412 */       this.worksheet.unsetSheetProtection();
/*  988:     */     }
/*  989:     */   }
/*  990:     */   
/*  991:     */   public void setSheetPassword(String password, HashAlgorithm hashAlgo)
/*  992:     */   {
/*  993:1424 */     if ((password == null) && (!isSheetProtectionEnabled())) {
/*  994:1425 */       return;
/*  995:     */     }
/*  996:1427 */     XSSFPasswordHelper.setPassword(safeGetProtectionField(), password, hashAlgo, null);
/*  997:     */   }
/*  998:     */   
/*  999:     */   public boolean validateSheetPassword(String password)
/* 1000:     */   {
/* 1001:1436 */     if (!isSheetProtectionEnabled()) {
/* 1002:1437 */       return password == null;
/* 1003:     */     }
/* 1004:1439 */     return XSSFPasswordHelper.validatePassword(safeGetProtectionField(), password, null);
/* 1005:     */   }
/* 1006:     */   
/* 1007:     */   public XSSFRow getRow(int rownum)
/* 1008:     */   {
/* 1009:1452 */     Integer rownumI = new Integer(rownum);
/* 1010:1453 */     return (XSSFRow)this._rows.get(rownumI);
/* 1011:     */   }
/* 1012:     */   
/* 1013:     */   private List<XSSFRow> getRows(int startRowNum, int endRowNum, boolean createRowIfMissing)
/* 1014:     */   {
/* 1015:1468 */     if (startRowNum > endRowNum) {
/* 1016:1469 */       throw new IllegalArgumentException("getRows: startRowNum must be less than or equal to endRowNum");
/* 1017:     */     }
/* 1018:1471 */     List<XSSFRow> rows = new ArrayList();
/* 1019:1472 */     if (createRowIfMissing)
/* 1020:     */     {
/* 1021:1473 */       for (int i = startRowNum; i <= endRowNum; i++)
/* 1022:     */       {
/* 1023:1474 */         XSSFRow row = getRow(i);
/* 1024:1475 */         if (row == null) {
/* 1025:1476 */           row = createRow(i);
/* 1026:     */         }
/* 1027:1478 */         rows.add(row);
/* 1028:     */       }
/* 1029:     */     }
/* 1030:     */     else
/* 1031:     */     {
/* 1032:1483 */       Integer startI = new Integer(startRowNum);
/* 1033:1484 */       Integer endI = new Integer(endRowNum + 1);
/* 1034:1485 */       Collection<XSSFRow> inclusive = this._rows.subMap(startI, endI).values();
/* 1035:1486 */       rows.addAll(inclusive);
/* 1036:     */     }
/* 1037:1488 */     return rows;
/* 1038:     */   }
/* 1039:     */   
/* 1040:     */   public int[] getRowBreaks()
/* 1041:     */   {
/* 1042:1499 */     return this.worksheet.isSetRowBreaks() ? getBreaks(this.worksheet.getRowBreaks()) : new int[0];
/* 1043:     */   }
/* 1044:     */   
/* 1045:     */   public boolean getRowSumsBelow()
/* 1046:     */   {
/* 1047:1518 */     CTSheetPr sheetPr = this.worksheet.getSheetPr();
/* 1048:1519 */     CTOutlinePr outlinePr = (sheetPr != null) && (sheetPr.isSetOutlinePr()) ? sheetPr.getOutlinePr() : null;
/* 1049:     */     
/* 1050:1521 */     return (outlinePr == null) || (outlinePr.getSummaryBelow());
/* 1051:     */   }
/* 1052:     */   
/* 1053:     */   public void setRowSumsBelow(boolean value)
/* 1054:     */   {
/* 1055:1539 */     ensureOutlinePr().setSummaryBelow(value);
/* 1056:     */   }
/* 1057:     */   
/* 1058:     */   public boolean getRowSumsRight()
/* 1059:     */   {
/* 1060:1557 */     CTSheetPr sheetPr = this.worksheet.getSheetPr();
/* 1061:1558 */     CTOutlinePr outlinePr = (sheetPr != null) && (sheetPr.isSetOutlinePr()) ? sheetPr.getOutlinePr() : CTOutlinePr.Factory.newInstance();
/* 1062:     */     
/* 1063:1560 */     return outlinePr.getSummaryRight();
/* 1064:     */   }
/* 1065:     */   
/* 1066:     */   public void setRowSumsRight(boolean value)
/* 1067:     */   {
/* 1068:1578 */     ensureOutlinePr().setSummaryRight(value);
/* 1069:     */   }
/* 1070:     */   
/* 1071:     */   private CTOutlinePr ensureOutlinePr()
/* 1072:     */   {
/* 1073:1586 */     CTSheetPr sheetPr = this.worksheet.isSetSheetPr() ? this.worksheet.getSheetPr() : this.worksheet.addNewSheetPr();
/* 1074:1587 */     return sheetPr.isSetOutlinePr() ? sheetPr.getOutlinePr() : sheetPr.addNewOutlinePr();
/* 1075:     */   }
/* 1076:     */   
/* 1077:     */   public boolean getScenarioProtect()
/* 1078:     */   {
/* 1079:1597 */     return (this.worksheet.isSetSheetProtection()) && (this.worksheet.getSheetProtection().getScenarios());
/* 1080:     */   }
/* 1081:     */   
/* 1082:     */   public short getTopRow()
/* 1083:     */   {
/* 1084:1608 */     String cellRef = getSheetTypeSheetView().getTopLeftCell();
/* 1085:1609 */     if (cellRef == null) {
/* 1086:1610 */       return 0;
/* 1087:     */     }
/* 1088:1612 */     CellReference cellReference = new CellReference(cellRef);
/* 1089:1613 */     return (short)cellReference.getRow();
/* 1090:     */   }
/* 1091:     */   
/* 1092:     */   public boolean getVerticallyCenter()
/* 1093:     */   {
/* 1094:1623 */     CTPrintOptions opts = this.worksheet.getPrintOptions();
/* 1095:1624 */     return (opts != null) && (opts.getVerticalCentered());
/* 1096:     */   }
/* 1097:     */   
/* 1098:     */   public void groupColumn(int fromColumn, int toColumn)
/* 1099:     */   {
/* 1100:1632 */     groupColumn1Based(fromColumn + 1, toColumn + 1);
/* 1101:     */   }
/* 1102:     */   
/* 1103:     */   private void groupColumn1Based(int fromColumn, int toColumn)
/* 1104:     */   {
/* 1105:1635 */     CTCols ctCols = this.worksheet.getColsArray(0);
/* 1106:1636 */     CTCol ctCol = CTCol.Factory.newInstance();
/* 1107:     */     
/* 1108:     */ 
/* 1109:     */ 
/* 1110:     */ 
/* 1111:1641 */     CTCol fixCol_before = this.columnHelper.getColumn1Based(toColumn, false);
/* 1112:1642 */     if (fixCol_before != null) {
/* 1113:1643 */       fixCol_before = (CTCol)fixCol_before.copy();
/* 1114:     */     }
/* 1115:1646 */     ctCol.setMin(fromColumn);
/* 1116:1647 */     ctCol.setMax(toColumn);
/* 1117:1648 */     this.columnHelper.addCleanColIntoCols(ctCols, ctCol);
/* 1118:     */     
/* 1119:1650 */     CTCol fixCol_after = this.columnHelper.getColumn1Based(toColumn, false);
/* 1120:1651 */     if ((fixCol_before != null) && (fixCol_after != null)) {
/* 1121:1652 */       this.columnHelper.setColumnAttributes(fixCol_before, fixCol_after);
/* 1122:     */     }
/* 1123:1655 */     for (int index = fromColumn; index <= toColumn; index++)
/* 1124:     */     {
/* 1125:1656 */       CTCol col = this.columnHelper.getColumn1Based(index, false);
/* 1126:     */       
/* 1127:1658 */       short outlineLevel = col.getOutlineLevel();
/* 1128:1659 */       col.setOutlineLevel((short)(outlineLevel + 1));
/* 1129:1660 */       index = (int)col.getMax();
/* 1130:     */     }
/* 1131:1662 */     this.worksheet.setColsArray(0, ctCols);
/* 1132:1663 */     setSheetFormatPrOutlineLevelCol();
/* 1133:     */   }
/* 1134:     */   
/* 1135:     */   private void setColWidthAttribute(CTCols ctCols)
/* 1136:     */   {
/* 1137:1670 */     for (CTCol col : ctCols.getColArray()) {
/* 1138:1671 */       if (!col.isSetWidth())
/* 1139:     */       {
/* 1140:1672 */         col.setWidth(getDefaultColumnWidth());
/* 1141:1673 */         col.setCustomWidth(false);
/* 1142:     */       }
/* 1143:     */     }
/* 1144:     */   }
/* 1145:     */   
/* 1146:     */   public void groupRow(int fromRow, int toRow)
/* 1147:     */   {
/* 1148:1686 */     for (int i = fromRow; i <= toRow; i++)
/* 1149:     */     {
/* 1150:1687 */       XSSFRow xrow = getRow(i);
/* 1151:1688 */       if (xrow == null) {
/* 1152:1689 */         xrow = createRow(i);
/* 1153:     */       }
/* 1154:1691 */       CTRow ctrow = xrow.getCTRow();
/* 1155:1692 */       short outlineLevel = ctrow.getOutlineLevel();
/* 1156:1693 */       ctrow.setOutlineLevel((short)(outlineLevel + 1));
/* 1157:     */     }
/* 1158:1695 */     setSheetFormatPrOutlineLevelRow();
/* 1159:     */   }
/* 1160:     */   
/* 1161:     */   private short getMaxOutlineLevelRows()
/* 1162:     */   {
/* 1163:1699 */     int outlineLevel = 0;
/* 1164:1700 */     for (XSSFRow xrow : this._rows.values()) {
/* 1165:1701 */       outlineLevel = Math.max(outlineLevel, xrow.getCTRow().getOutlineLevel());
/* 1166:     */     }
/* 1167:1703 */     return (short)outlineLevel;
/* 1168:     */   }
/* 1169:     */   
/* 1170:     */   private short getMaxOutlineLevelCols()
/* 1171:     */   {
/* 1172:1707 */     CTCols ctCols = this.worksheet.getColsArray(0);
/* 1173:1708 */     int outlineLevel = 0;
/* 1174:1709 */     for (CTCol col : ctCols.getColArray()) {
/* 1175:1710 */       outlineLevel = Math.max(outlineLevel, col.getOutlineLevel());
/* 1176:     */     }
/* 1177:1712 */     return (short)outlineLevel;
/* 1178:     */   }
/* 1179:     */   
/* 1180:     */   public boolean isColumnBroken(int column)
/* 1181:     */   {
/* 1182:1720 */     for (int colBreak : getColumnBreaks()) {
/* 1183:1721 */       if (colBreak == column) {
/* 1184:1722 */         return true;
/* 1185:     */       }
/* 1186:     */     }
/* 1187:1725 */     return false;
/* 1188:     */   }
/* 1189:     */   
/* 1190:     */   public boolean isColumnHidden(int columnIndex)
/* 1191:     */   {
/* 1192:1736 */     CTCol col = this.columnHelper.getColumn(columnIndex, false);
/* 1193:1737 */     return (col != null) && (col.getHidden());
/* 1194:     */   }
/* 1195:     */   
/* 1196:     */   public boolean isDisplayFormulas()
/* 1197:     */   {
/* 1198:1747 */     return getSheetTypeSheetView().getShowFormulas();
/* 1199:     */   }
/* 1200:     */   
/* 1201:     */   public boolean isDisplayGridlines()
/* 1202:     */   {
/* 1203:1759 */     return getSheetTypeSheetView().getShowGridLines();
/* 1204:     */   }
/* 1205:     */   
/* 1206:     */   public void setDisplayGridlines(boolean show)
/* 1207:     */   {
/* 1208:1773 */     getSheetTypeSheetView().setShowGridLines(show);
/* 1209:     */   }
/* 1210:     */   
/* 1211:     */   public boolean isDisplayRowColHeadings()
/* 1212:     */   {
/* 1213:1789 */     return getSheetTypeSheetView().getShowRowColHeaders();
/* 1214:     */   }
/* 1215:     */   
/* 1216:     */   public void setDisplayRowColHeadings(boolean show)
/* 1217:     */   {
/* 1218:1805 */     getSheetTypeSheetView().setShowRowColHeaders(show);
/* 1219:     */   }
/* 1220:     */   
/* 1221:     */   public boolean isPrintGridlines()
/* 1222:     */   {
/* 1223:1815 */     CTPrintOptions opts = this.worksheet.getPrintOptions();
/* 1224:1816 */     return (opts != null) && (opts.getGridLines());
/* 1225:     */   }
/* 1226:     */   
/* 1227:     */   public void setPrintGridlines(boolean value)
/* 1228:     */   {
/* 1229:1826 */     CTPrintOptions opts = this.worksheet.isSetPrintOptions() ? this.worksheet.getPrintOptions() : this.worksheet.addNewPrintOptions();
/* 1230:     */     
/* 1231:1828 */     opts.setGridLines(value);
/* 1232:     */   }
/* 1233:     */   
/* 1234:     */   public boolean isPrintRowAndColumnHeadings()
/* 1235:     */   {
/* 1236:1838 */     CTPrintOptions opts = this.worksheet.getPrintOptions();
/* 1237:1839 */     return (opts != null) && (opts.getHeadings());
/* 1238:     */   }
/* 1239:     */   
/* 1240:     */   public void setPrintRowAndColumnHeadings(boolean value)
/* 1241:     */   {
/* 1242:1849 */     CTPrintOptions opts = this.worksheet.isSetPrintOptions() ? this.worksheet.getPrintOptions() : this.worksheet.addNewPrintOptions();
/* 1243:     */     
/* 1244:1851 */     opts.setHeadings(value);
/* 1245:     */   }
/* 1246:     */   
/* 1247:     */   public boolean isRowBroken(int row)
/* 1248:     */   {
/* 1249:1862 */     for (int rowBreak : getRowBreaks()) {
/* 1250:1863 */       if (rowBreak == row) {
/* 1251:1864 */         return true;
/* 1252:     */       }
/* 1253:     */     }
/* 1254:1867 */     return false;
/* 1255:     */   }
/* 1256:     */   
/* 1257:     */   private void setBreak(int id, CTPageBreak ctPgBreak, int lastIndex)
/* 1258:     */   {
/* 1259:1871 */     CTBreak brk = ctPgBreak.addNewBrk();
/* 1260:1872 */     brk.setId(id + 1);
/* 1261:1873 */     brk.setMan(true);
/* 1262:1874 */     brk.setMax(lastIndex);
/* 1263:     */     
/* 1264:1876 */     int nPageBreaks = ctPgBreak.sizeOfBrkArray();
/* 1265:1877 */     ctPgBreak.setCount(nPageBreaks);
/* 1266:1878 */     ctPgBreak.setManualBreakCount(nPageBreaks);
/* 1267:     */   }
/* 1268:     */   
/* 1269:     */   public void setRowBreak(int row)
/* 1270:     */   {
/* 1271:1894 */     if (!isRowBroken(row))
/* 1272:     */     {
/* 1273:1895 */       CTPageBreak pgBreak = this.worksheet.isSetRowBreaks() ? this.worksheet.getRowBreaks() : this.worksheet.addNewRowBreaks();
/* 1274:1896 */       setBreak(row, pgBreak, SpreadsheetVersion.EXCEL2007.getLastColumnIndex());
/* 1275:     */     }
/* 1276:     */   }
/* 1277:     */   
/* 1278:     */   public void removeColumnBreak(int column)
/* 1279:     */   {
/* 1280:1905 */     if (this.worksheet.isSetColBreaks()) {
/* 1281:1906 */       removeBreak(column, this.worksheet.getColBreaks());
/* 1282:     */     }
/* 1283:     */   }
/* 1284:     */   
/* 1285:     */   public void removeMergedRegion(int index)
/* 1286:     */   {
/* 1287:1917 */     if (!this.worksheet.isSetMergeCells()) {
/* 1288:1918 */       return;
/* 1289:     */     }
/* 1290:1921 */     CTMergeCells ctMergeCells = this.worksheet.getMergeCells();
/* 1291:1922 */     int size = ctMergeCells.sizeOfMergeCellArray();
/* 1292:1923 */     assert ((0 <= index) && (index < size));
/* 1293:1924 */     if (size > 1) {
/* 1294:1925 */       ctMergeCells.removeMergeCell(index);
/* 1295:     */     } else {
/* 1296:1927 */       this.worksheet.unsetMergeCells();
/* 1297:     */     }
/* 1298:     */   }
/* 1299:     */   
/* 1300:     */   public void removeMergedRegions(Collection<Integer> indices)
/* 1301:     */   {
/* 1302:1942 */     if (!this.worksheet.isSetMergeCells()) {
/* 1303:1943 */       return;
/* 1304:     */     }
/* 1305:1946 */     CTMergeCells ctMergeCells = this.worksheet.getMergeCells();
/* 1306:1947 */     List<CTMergeCell> newMergeCells = new ArrayList(ctMergeCells.sizeOfMergeCellArray());
/* 1307:     */     
/* 1308:1949 */     int idx = 0;
/* 1309:1950 */     for (CTMergeCell mc : ctMergeCells.getMergeCellArray()) {
/* 1310:1951 */       if (!indices.contains(Integer.valueOf(idx++))) {
/* 1311:1952 */         newMergeCells.add(mc);
/* 1312:     */       }
/* 1313:     */     }
/* 1314:1956 */     if (newMergeCells.isEmpty())
/* 1315:     */     {
/* 1316:1957 */       this.worksheet.unsetMergeCells();
/* 1317:     */     }
/* 1318:     */     else
/* 1319:     */     {
/* 1320:1959 */       CTMergeCell[] newMergeCellsArray = new CTMergeCell[newMergeCells.size()];
/* 1321:1960 */       ctMergeCells.setMergeCellArray((CTMergeCell[])newMergeCells.toArray(newMergeCellsArray));
/* 1322:     */     }
/* 1323:     */   }
/* 1324:     */   
/* 1325:     */   public void removeRow(Row row)
/* 1326:     */   {
/* 1327:1971 */     if (row.getSheet() != this) {
/* 1328:1972 */       throw new IllegalArgumentException("Specified row does not belong to this sheet");
/* 1329:     */     }
/* 1330:1975 */     ArrayList<XSSFCell> cellsToDelete = new ArrayList();
/* 1331:1976 */     for (Cell cell : row) {
/* 1332:1977 */       cellsToDelete.add((XSSFCell)cell);
/* 1333:     */     }
/* 1334:1980 */     for (XSSFCell cell : cellsToDelete) {
/* 1335:1981 */       row.removeCell(cell);
/* 1336:     */     }
/* 1337:1985 */     int rowNum = row.getRowNum();
/* 1338:1986 */     Integer rowNumI = new Integer(rowNum);
/* 1339:     */     
/* 1340:1988 */     int idx = this._rows.headMap(rowNumI).size();
/* 1341:1989 */     this._rows.remove(rowNumI);
/* 1342:1990 */     this.worksheet.getSheetData().removeRow(idx);
/* 1343:1993 */     if (this.sheetComments != null) {
/* 1344:1994 */       for (CellAddress ref : getCellComments().keySet()) {
/* 1345:1995 */         if (ref.getRow() == rowNum) {
/* 1346:1996 */           this.sheetComments.removeComment(ref);
/* 1347:     */         }
/* 1348:     */       }
/* 1349:     */     }
/* 1350:     */   }
/* 1351:     */   
/* 1352:     */   public void removeRowBreak(int row)
/* 1353:     */   {
/* 1354:2007 */     if (this.worksheet.isSetRowBreaks()) {
/* 1355:2008 */       removeBreak(row, this.worksheet.getRowBreaks());
/* 1356:     */     }
/* 1357:     */   }
/* 1358:     */   
/* 1359:     */   public void setForceFormulaRecalculation(boolean value)
/* 1360:     */   {
/* 1361:2034 */     CTCalcPr calcPr = getWorkbook().getCTWorkbook().getCalcPr();
/* 1362:2036 */     if (this.worksheet.isSetSheetCalcPr())
/* 1363:     */     {
/* 1364:2038 */       CTSheetCalcPr calc = this.worksheet.getSheetCalcPr();
/* 1365:2039 */       calc.setFullCalcOnLoad(value);
/* 1366:     */     }
/* 1367:2041 */     else if (value)
/* 1368:     */     {
/* 1369:2043 */       CTSheetCalcPr calc = this.worksheet.addNewSheetCalcPr();
/* 1370:2044 */       calc.setFullCalcOnLoad(value);
/* 1371:     */     }
/* 1372:2046 */     if ((value) && (calcPr != null) && (calcPr.getCalcMode() == STCalcMode.MANUAL)) {
/* 1373:2047 */       calcPr.setCalcMode(STCalcMode.AUTO);
/* 1374:     */     }
/* 1375:     */   }
/* 1376:     */   
/* 1377:     */   public boolean getForceFormulaRecalculation()
/* 1378:     */   {
/* 1379:2058 */     if (this.worksheet.isSetSheetCalcPr())
/* 1380:     */     {
/* 1381:2059 */       CTSheetCalcPr calc = this.worksheet.getSheetCalcPr();
/* 1382:2060 */       return calc.getFullCalcOnLoad();
/* 1383:     */     }
/* 1384:2062 */     return false;
/* 1385:     */   }
/* 1386:     */   
/* 1387:     */   public Iterator<Row> rowIterator()
/* 1388:     */   {
/* 1389:2073 */     return this._rows.values().iterator();
/* 1390:     */   }
/* 1391:     */   
/* 1392:     */   public Iterator<Row> iterator()
/* 1393:     */   {
/* 1394:2082 */     return rowIterator();
/* 1395:     */   }
/* 1396:     */   
/* 1397:     */   public boolean getAutobreaks()
/* 1398:     */   {
/* 1399:2092 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/* 1400:2093 */     CTPageSetUpPr psSetup = (sheetPr == null) || (!sheetPr.isSetPageSetUpPr()) ? CTPageSetUpPr.Factory.newInstance() : sheetPr.getPageSetUpPr();
/* 1401:     */     
/* 1402:2095 */     return psSetup.getAutoPageBreaks();
/* 1403:     */   }
/* 1404:     */   
/* 1405:     */   public void setAutobreaks(boolean value)
/* 1406:     */   {
/* 1407:2105 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/* 1408:2106 */     CTPageSetUpPr psSetup = sheetPr.isSetPageSetUpPr() ? sheetPr.getPageSetUpPr() : sheetPr.addNewPageSetUpPr();
/* 1409:2107 */     psSetup.setAutoPageBreaks(value);
/* 1410:     */   }
/* 1411:     */   
/* 1412:     */   public void setColumnBreak(int column)
/* 1413:     */   {
/* 1414:2123 */     if (!isColumnBroken(column))
/* 1415:     */     {
/* 1416:2124 */       CTPageBreak pgBreak = this.worksheet.isSetColBreaks() ? this.worksheet.getColBreaks() : this.worksheet.addNewColBreaks();
/* 1417:2125 */       setBreak(column, pgBreak, SpreadsheetVersion.EXCEL2007.getLastRowIndex());
/* 1418:     */     }
/* 1419:     */   }
/* 1420:     */   
/* 1421:     */   public void setColumnGroupCollapsed(int columnNumber, boolean collapsed)
/* 1422:     */   {
/* 1423:2131 */     if (collapsed) {
/* 1424:2132 */       collapseColumn(columnNumber);
/* 1425:     */     } else {
/* 1426:2134 */       expandColumn(columnNumber);
/* 1427:     */     }
/* 1428:     */   }
/* 1429:     */   
/* 1430:     */   private void collapseColumn(int columnNumber)
/* 1431:     */   {
/* 1432:2139 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1433:2140 */     CTCol col = this.columnHelper.getColumn(columnNumber, false);
/* 1434:2141 */     int colInfoIx = this.columnHelper.getIndexOfColumn(cols, col);
/* 1435:2142 */     if (colInfoIx == -1) {
/* 1436:2143 */       return;
/* 1437:     */     }
/* 1438:2146 */     int groupStartColInfoIx = findStartOfColumnOutlineGroup(colInfoIx);
/* 1439:     */     
/* 1440:2148 */     CTCol columnInfo = cols.getColArray(groupStartColInfoIx);
/* 1441:     */     
/* 1442:     */ 
/* 1443:2151 */     int lastColMax = setGroupHidden(groupStartColInfoIx, columnInfo.getOutlineLevel(), true);
/* 1444:     */     
/* 1445:     */ 
/* 1446:     */ 
/* 1447:2155 */     setColumn(lastColMax + 1, Integer.valueOf(0), null, null, Boolean.TRUE);
/* 1448:     */   }
/* 1449:     */   
/* 1450:     */   private void setColumn(int targetColumnIx, Integer style, Integer level, Boolean hidden, Boolean collapsed)
/* 1451:     */   {
/* 1452:2161 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1453:2162 */     CTCol ci = null;
/* 1454:2163 */     for (CTCol tci : cols.getColArray())
/* 1455:     */     {
/* 1456:2164 */       long tciMin = tci.getMin();
/* 1457:2165 */       long tciMax = tci.getMax();
/* 1458:2166 */       if ((tciMin >= targetColumnIx) && (tciMax <= targetColumnIx)) {
/* 1459:2167 */         ci = tci;
/* 1460:     */       } else {
/* 1461:2170 */         if (tciMin > targetColumnIx) {
/* 1462:     */           break;
/* 1463:     */         }
/* 1464:     */       }
/* 1465:     */     }
/* 1466:2176 */     if (ci == null)
/* 1467:     */     {
/* 1468:2179 */       CTCol nci = CTCol.Factory.newInstance();
/* 1469:2180 */       nci.setMin(targetColumnIx);
/* 1470:2181 */       nci.setMax(targetColumnIx);
/* 1471:2182 */       unsetCollapsed(collapsed.booleanValue(), nci);
/* 1472:2183 */       this.columnHelper.addCleanColIntoCols(cols, nci);
/* 1473:2184 */       return;
/* 1474:     */     }
/* 1475:2187 */     boolean styleChanged = (style != null) && (ci.getStyle() != style.intValue());
/* 1476:2188 */     boolean levelChanged = (level != null) && (ci.getOutlineLevel() != level.intValue());
/* 1477:2189 */     boolean hiddenChanged = (hidden != null) && (ci.getHidden() != hidden.booleanValue());
/* 1478:2190 */     boolean collapsedChanged = (collapsed != null) && (ci.getCollapsed() != collapsed.booleanValue());
/* 1479:2191 */     boolean columnChanged = (levelChanged) || (hiddenChanged) || (collapsedChanged) || (styleChanged);
/* 1480:2192 */     if (!columnChanged) {
/* 1481:2194 */       return;
/* 1482:     */     }
/* 1483:2197 */     long ciMin = ci.getMin();
/* 1484:2198 */     long ciMax = ci.getMax();
/* 1485:2199 */     if ((ciMin == targetColumnIx) && (ciMax == targetColumnIx))
/* 1486:     */     {
/* 1487:2201 */       unsetCollapsed(collapsed.booleanValue(), ci);
/* 1488:2202 */       return;
/* 1489:     */     }
/* 1490:2205 */     if ((ciMin == targetColumnIx) || (ciMax == targetColumnIx))
/* 1491:     */     {
/* 1492:2209 */       if (ciMin == targetColumnIx) {
/* 1493:2210 */         ci.setMin(targetColumnIx + 1);
/* 1494:     */       } else {
/* 1495:2212 */         ci.setMax(targetColumnIx - 1);
/* 1496:     */       }
/* 1497:2214 */       CTCol nci = this.columnHelper.cloneCol(cols, ci);
/* 1498:2215 */       nci.setMin(targetColumnIx);
/* 1499:2216 */       unsetCollapsed(collapsed.booleanValue(), nci);
/* 1500:2217 */       this.columnHelper.addCleanColIntoCols(cols, nci);
/* 1501:     */     }
/* 1502:     */     else
/* 1503:     */     {
/* 1504:2221 */       CTCol ciMid = this.columnHelper.cloneCol(cols, ci);
/* 1505:2222 */       CTCol ciEnd = this.columnHelper.cloneCol(cols, ci);
/* 1506:2223 */       int lastcolumn = (int)ciMax;
/* 1507:     */       
/* 1508:2225 */       ci.setMax(targetColumnIx - 1);
/* 1509:     */       
/* 1510:2227 */       ciMid.setMin(targetColumnIx);
/* 1511:2228 */       ciMid.setMax(targetColumnIx);
/* 1512:2229 */       unsetCollapsed(collapsed.booleanValue(), ciMid);
/* 1513:2230 */       this.columnHelper.addCleanColIntoCols(cols, ciMid);
/* 1514:     */       
/* 1515:2232 */       ciEnd.setMin(targetColumnIx + 1);
/* 1516:2233 */       ciEnd.setMax(lastcolumn);
/* 1517:2234 */       this.columnHelper.addCleanColIntoCols(cols, ciEnd);
/* 1518:     */     }
/* 1519:     */   }
/* 1520:     */   
/* 1521:     */   private void unsetCollapsed(boolean collapsed, CTCol ci)
/* 1522:     */   {
/* 1523:2239 */     if (collapsed) {
/* 1524:2240 */       ci.setCollapsed(collapsed);
/* 1525:     */     } else {
/* 1526:2242 */       ci.unsetCollapsed();
/* 1527:     */     }
/* 1528:     */   }
/* 1529:     */   
/* 1530:     */   private int setGroupHidden(int pIdx, int level, boolean hidden)
/* 1531:     */   {
/* 1532:2255 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1533:2256 */     int idx = pIdx;
/* 1534:2257 */     CTCol[] colArray = cols.getColArray();
/* 1535:2258 */     CTCol columnInfo = colArray[idx];
/* 1536:2259 */     while (idx < colArray.length)
/* 1537:     */     {
/* 1538:2260 */       columnInfo.setHidden(hidden);
/* 1539:2261 */       if (idx + 1 < colArray.length)
/* 1540:     */       {
/* 1541:2262 */         CTCol nextColumnInfo = colArray[(idx + 1)];
/* 1542:2264 */         if (!isAdjacentBefore(columnInfo, nextColumnInfo)) {
/* 1543:     */           break;
/* 1544:     */         }
/* 1545:2268 */         if (nextColumnInfo.getOutlineLevel() < level) {
/* 1546:     */           break;
/* 1547:     */         }
/* 1548:2271 */         columnInfo = nextColumnInfo;
/* 1549:     */       }
/* 1550:2273 */       idx++;
/* 1551:     */     }
/* 1552:2275 */     return (int)columnInfo.getMax();
/* 1553:     */   }
/* 1554:     */   
/* 1555:     */   private boolean isAdjacentBefore(CTCol col, CTCol otherCol)
/* 1556:     */   {
/* 1557:2279 */     return col.getMax() == otherCol.getMin() - 1L;
/* 1558:     */   }
/* 1559:     */   
/* 1560:     */   private int findStartOfColumnOutlineGroup(int pIdx)
/* 1561:     */   {
/* 1562:2284 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1563:2285 */     CTCol[] colArray = cols.getColArray();
/* 1564:2286 */     CTCol columnInfo = colArray[pIdx];
/* 1565:2287 */     int level = columnInfo.getOutlineLevel();
/* 1566:2288 */     int idx = pIdx;
/* 1567:2289 */     while (idx != 0)
/* 1568:     */     {
/* 1569:2290 */       CTCol prevColumnInfo = colArray[(idx - 1)];
/* 1570:2291 */       if (!isAdjacentBefore(prevColumnInfo, columnInfo)) {
/* 1571:     */         break;
/* 1572:     */       }
/* 1573:2294 */       if (prevColumnInfo.getOutlineLevel() < level) {
/* 1574:     */         break;
/* 1575:     */       }
/* 1576:2297 */       idx--;
/* 1577:2298 */       columnInfo = prevColumnInfo;
/* 1578:     */     }
/* 1579:2300 */     return idx;
/* 1580:     */   }
/* 1581:     */   
/* 1582:     */   private int findEndOfColumnOutlineGroup(int colInfoIndex)
/* 1583:     */   {
/* 1584:2304 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1585:     */     
/* 1586:2306 */     CTCol[] colArray = cols.getColArray();
/* 1587:2307 */     CTCol columnInfo = colArray[colInfoIndex];
/* 1588:2308 */     int level = columnInfo.getOutlineLevel();
/* 1589:2309 */     int idx = colInfoIndex;
/* 1590:2310 */     int lastIdx = colArray.length - 1;
/* 1591:2311 */     while (idx < lastIdx)
/* 1592:     */     {
/* 1593:2312 */       CTCol nextColumnInfo = colArray[(idx + 1)];
/* 1594:2313 */       if (!isAdjacentBefore(columnInfo, nextColumnInfo)) {
/* 1595:     */         break;
/* 1596:     */       }
/* 1597:2316 */       if (nextColumnInfo.getOutlineLevel() < level) {
/* 1598:     */         break;
/* 1599:     */       }
/* 1600:2319 */       idx++;
/* 1601:2320 */       columnInfo = nextColumnInfo;
/* 1602:     */     }
/* 1603:2322 */     return idx;
/* 1604:     */   }
/* 1605:     */   
/* 1606:     */   private void expandColumn(int columnIndex)
/* 1607:     */   {
/* 1608:2326 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1609:2327 */     CTCol col = this.columnHelper.getColumn(columnIndex, false);
/* 1610:2328 */     int colInfoIx = this.columnHelper.getIndexOfColumn(cols, col);
/* 1611:     */     
/* 1612:2330 */     int idx = findColInfoIdx((int)col.getMax(), colInfoIx);
/* 1613:2331 */     if (idx == -1) {
/* 1614:2332 */       return;
/* 1615:     */     }
/* 1616:2336 */     if (!isColumnGroupCollapsed(idx)) {
/* 1617:2337 */       return;
/* 1618:     */     }
/* 1619:2341 */     int startIdx = findStartOfColumnOutlineGroup(idx);
/* 1620:2342 */     int endIdx = findEndOfColumnOutlineGroup(idx);
/* 1621:     */     
/* 1622:     */ 
/* 1623:     */ 
/* 1624:     */ 
/* 1625:     */ 
/* 1626:     */ 
/* 1627:     */ 
/* 1628:     */ 
/* 1629:     */ 
/* 1630:     */ 
/* 1631:     */ 
/* 1632:     */ 
/* 1633:2355 */     CTCol[] colArray = cols.getColArray();
/* 1634:2356 */     CTCol columnInfo = colArray[endIdx];
/* 1635:2357 */     if (!isColumnGroupHiddenByParent(idx))
/* 1636:     */     {
/* 1637:2358 */       short outlineLevel = columnInfo.getOutlineLevel();
/* 1638:2359 */       boolean nestedGroup = false;
/* 1639:2360 */       for (int i = startIdx; i <= endIdx; i++)
/* 1640:     */       {
/* 1641:2361 */         CTCol ci = colArray[i];
/* 1642:2362 */         if (outlineLevel == ci.getOutlineLevel())
/* 1643:     */         {
/* 1644:2363 */           ci.unsetHidden();
/* 1645:2364 */           if (nestedGroup)
/* 1646:     */           {
/* 1647:2365 */             nestedGroup = false;
/* 1648:2366 */             ci.setCollapsed(true);
/* 1649:     */           }
/* 1650:     */         }
/* 1651:     */         else
/* 1652:     */         {
/* 1653:2369 */           nestedGroup = true;
/* 1654:     */         }
/* 1655:     */       }
/* 1656:     */     }
/* 1657:2375 */     setColumn((int)columnInfo.getMax() + 1, null, null, Boolean.FALSE, Boolean.FALSE);
/* 1658:     */   }
/* 1659:     */   
/* 1660:     */   private boolean isColumnGroupHiddenByParent(int idx)
/* 1661:     */   {
/* 1662:2380 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1663:     */     
/* 1664:2382 */     int endLevel = 0;
/* 1665:2383 */     boolean endHidden = false;
/* 1666:2384 */     int endOfOutlineGroupIdx = findEndOfColumnOutlineGroup(idx);
/* 1667:2385 */     CTCol[] colArray = cols.getColArray();
/* 1668:2386 */     if (endOfOutlineGroupIdx < colArray.length)
/* 1669:     */     {
/* 1670:2387 */       CTCol nextInfo = colArray[(endOfOutlineGroupIdx + 1)];
/* 1671:2388 */       if (isAdjacentBefore(colArray[endOfOutlineGroupIdx], nextInfo))
/* 1672:     */       {
/* 1673:2389 */         endLevel = nextInfo.getOutlineLevel();
/* 1674:2390 */         endHidden = nextInfo.getHidden();
/* 1675:     */       }
/* 1676:     */     }
/* 1677:2394 */     int startLevel = 0;
/* 1678:2395 */     boolean startHidden = false;
/* 1679:2396 */     int startOfOutlineGroupIdx = findStartOfColumnOutlineGroup(idx);
/* 1680:2397 */     if (startOfOutlineGroupIdx > 0)
/* 1681:     */     {
/* 1682:2398 */       CTCol prevInfo = colArray[(startOfOutlineGroupIdx - 1)];
/* 1683:2400 */       if (isAdjacentBefore(prevInfo, colArray[startOfOutlineGroupIdx]))
/* 1684:     */       {
/* 1685:2401 */         startLevel = prevInfo.getOutlineLevel();
/* 1686:2402 */         startHidden = prevInfo.getHidden();
/* 1687:     */       }
/* 1688:     */     }
/* 1689:2406 */     if (endLevel > startLevel) {
/* 1690:2407 */       return endHidden;
/* 1691:     */     }
/* 1692:2409 */     return startHidden;
/* 1693:     */   }
/* 1694:     */   
/* 1695:     */   private int findColInfoIdx(int columnValue, int fromColInfoIdx)
/* 1696:     */   {
/* 1697:2413 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1698:2415 */     if (columnValue < 0) {
/* 1699:2416 */       throw new IllegalArgumentException("column parameter out of range: " + columnValue);
/* 1700:     */     }
/* 1701:2419 */     if (fromColInfoIdx < 0) {
/* 1702:2420 */       throw new IllegalArgumentException("fromIdx parameter out of range: " + fromColInfoIdx);
/* 1703:     */     }
/* 1704:2424 */     CTCol[] colArray = cols.getColArray();
/* 1705:2425 */     for (int k = fromColInfoIdx; k < colArray.length; k++)
/* 1706:     */     {
/* 1707:2426 */       CTCol ci = colArray[k];
/* 1708:2428 */       if (containsColumn(ci, columnValue)) {
/* 1709:2429 */         return k;
/* 1710:     */       }
/* 1711:2432 */       if (ci.getMin() > fromColInfoIdx) {
/* 1712:     */         break;
/* 1713:     */       }
/* 1714:     */     }
/* 1715:2437 */     return -1;
/* 1716:     */   }
/* 1717:     */   
/* 1718:     */   private boolean containsColumn(CTCol col, int columnIndex)
/* 1719:     */   {
/* 1720:2441 */     return (col.getMin() <= columnIndex) && (columnIndex <= col.getMax());
/* 1721:     */   }
/* 1722:     */   
/* 1723:     */   private boolean isColumnGroupCollapsed(int idx)
/* 1724:     */   {
/* 1725:2452 */     CTCols cols = this.worksheet.getColsArray(0);
/* 1726:2453 */     CTCol[] colArray = cols.getColArray();
/* 1727:2454 */     int endOfOutlineGroupIdx = findEndOfColumnOutlineGroup(idx);
/* 1728:2455 */     int nextColInfoIx = endOfOutlineGroupIdx + 1;
/* 1729:2456 */     if (nextColInfoIx >= colArray.length) {
/* 1730:2457 */       return false;
/* 1731:     */     }
/* 1732:2459 */     CTCol nextColInfo = colArray[nextColInfoIx];
/* 1733:     */     
/* 1734:2461 */     CTCol col = colArray[endOfOutlineGroupIdx];
/* 1735:2462 */     if (!isAdjacentBefore(col, nextColInfo)) {
/* 1736:2463 */       return false;
/* 1737:     */     }
/* 1738:2466 */     return nextColInfo.getCollapsed();
/* 1739:     */   }
/* 1740:     */   
/* 1741:     */   public void setColumnHidden(int columnIndex, boolean hidden)
/* 1742:     */   {
/* 1743:2477 */     this.columnHelper.setColHidden(columnIndex, hidden);
/* 1744:     */   }
/* 1745:     */   
/* 1746:     */   public void setColumnWidth(int columnIndex, int width)
/* 1747:     */   {
/* 1748:2526 */     if (width > 65280) {
/* 1749:2527 */       throw new IllegalArgumentException("The maximum column width for an individual cell is 255 characters.");
/* 1750:     */     }
/* 1751:2530 */     this.columnHelper.setColWidth(columnIndex, width / 256.0D);
/* 1752:2531 */     this.columnHelper.setCustomWidth(columnIndex, true);
/* 1753:     */   }
/* 1754:     */   
/* 1755:     */   public void setDefaultColumnStyle(int column, CellStyle style)
/* 1756:     */   {
/* 1757:2536 */     this.columnHelper.setColDefaultStyle(column, style);
/* 1758:     */   }
/* 1759:     */   
/* 1760:     */   public void setDefaultColumnWidth(int width)
/* 1761:     */   {
/* 1762:2548 */     getSheetTypeSheetFormatPr().setBaseColWidth(width);
/* 1763:     */   }
/* 1764:     */   
/* 1765:     */   public void setDefaultRowHeight(short height)
/* 1766:     */   {
/* 1767:2559 */     setDefaultRowHeightInPoints(height / 20.0F);
/* 1768:     */   }
/* 1769:     */   
/* 1770:     */   public void setDefaultRowHeightInPoints(float height)
/* 1771:     */   {
/* 1772:2569 */     CTSheetFormatPr pr = getSheetTypeSheetFormatPr();
/* 1773:2570 */     pr.setDefaultRowHeight(height);
/* 1774:2571 */     pr.setCustomHeight(true);
/* 1775:     */   }
/* 1776:     */   
/* 1777:     */   public void setDisplayFormulas(boolean show)
/* 1778:     */   {
/* 1779:2581 */     getSheetTypeSheetView().setShowFormulas(show);
/* 1780:     */   }
/* 1781:     */   
/* 1782:     */   private CTSheetView getSheetTypeSheetView()
/* 1783:     */   {
/* 1784:2585 */     if (getDefaultSheetView() == null) {
/* 1785:2586 */       getSheetTypeSheetViews().setSheetViewArray(0, CTSheetView.Factory.newInstance());
/* 1786:     */     }
/* 1787:2588 */     return getDefaultSheetView();
/* 1788:     */   }
/* 1789:     */   
/* 1790:     */   public void setFitToPage(boolean b)
/* 1791:     */   {
/* 1792:2598 */     getSheetTypePageSetUpPr().setFitToPage(b);
/* 1793:     */   }
/* 1794:     */   
/* 1795:     */   public void setHorizontallyCenter(boolean value)
/* 1796:     */   {
/* 1797:2608 */     CTPrintOptions opts = this.worksheet.isSetPrintOptions() ? this.worksheet.getPrintOptions() : this.worksheet.addNewPrintOptions();
/* 1798:     */     
/* 1799:2610 */     opts.setHorizontalCentered(value);
/* 1800:     */   }
/* 1801:     */   
/* 1802:     */   public void setVerticallyCenter(boolean value)
/* 1803:     */   {
/* 1804:2620 */     CTPrintOptions opts = this.worksheet.isSetPrintOptions() ? this.worksheet.getPrintOptions() : this.worksheet.addNewPrintOptions();
/* 1805:     */     
/* 1806:2622 */     opts.setVerticalCentered(value);
/* 1807:     */   }
/* 1808:     */   
/* 1809:     */   public void setRowGroupCollapsed(int rowIndex, boolean collapse)
/* 1810:     */   {
/* 1811:2639 */     if (collapse) {
/* 1812:2640 */       collapseRow(rowIndex);
/* 1813:     */     } else {
/* 1814:2642 */       expandRow(rowIndex);
/* 1815:     */     }
/* 1816:     */   }
/* 1817:     */   
/* 1818:     */   private void collapseRow(int rowIndex)
/* 1819:     */   {
/* 1820:2650 */     XSSFRow row = getRow(rowIndex);
/* 1821:2651 */     if (row != null)
/* 1822:     */     {
/* 1823:2652 */       int startRow = findStartOfRowOutlineGroup(rowIndex);
/* 1824:     */       
/* 1825:     */ 
/* 1826:2655 */       int lastRow = writeHidden(row, startRow, true);
/* 1827:2656 */       if (getRow(lastRow) != null)
/* 1828:     */       {
/* 1829:2657 */         getRow(lastRow).getCTRow().setCollapsed(true);
/* 1830:     */       }
/* 1831:     */       else
/* 1832:     */       {
/* 1833:2659 */         XSSFRow newRow = createRow(lastRow);
/* 1834:2660 */         newRow.getCTRow().setCollapsed(true);
/* 1835:     */       }
/* 1836:     */     }
/* 1837:     */   }
/* 1838:     */   
/* 1839:     */   private int findStartOfRowOutlineGroup(int rowIndex)
/* 1840:     */   {
/* 1841:2670 */     short level = getRow(rowIndex).getCTRow().getOutlineLevel();
/* 1842:2671 */     int currentRow = rowIndex;
/* 1843:2672 */     while (getRow(currentRow) != null)
/* 1844:     */     {
/* 1845:2673 */       if (getRow(currentRow).getCTRow().getOutlineLevel() < level) {
/* 1846:2674 */         return currentRow + 1;
/* 1847:     */       }
/* 1848:2676 */       currentRow--;
/* 1849:     */     }
/* 1850:2678 */     return currentRow;
/* 1851:     */   }
/* 1852:     */   
/* 1853:     */   private int writeHidden(XSSFRow xRow, int rowIndex, boolean hidden)
/* 1854:     */   {
/* 1855:2682 */     short level = xRow.getCTRow().getOutlineLevel();
/* 1856:2683 */     for (Iterator<Row> it = rowIterator(); it.hasNext();)
/* 1857:     */     {
/* 1858:2684 */       xRow = (XSSFRow)it.next();
/* 1859:2687 */       if ((xRow.getRowNum() >= rowIndex) && 
/* 1860:     */       
/* 1861:     */ 
/* 1862:     */ 
/* 1863:2691 */         (xRow.getCTRow().getOutlineLevel() >= level))
/* 1864:     */       {
/* 1865:2692 */         xRow.getCTRow().setHidden(hidden);
/* 1866:2693 */         rowIndex++;
/* 1867:     */       }
/* 1868:     */     }
/* 1869:2697 */     return rowIndex;
/* 1870:     */   }
/* 1871:     */   
/* 1872:     */   private void expandRow(int rowNumber)
/* 1873:     */   {
/* 1874:2704 */     if (rowNumber == -1) {
/* 1875:2705 */       return;
/* 1876:     */     }
/* 1877:2707 */     XSSFRow row = getRow(rowNumber);
/* 1878:2709 */     if (!row.getCTRow().isSetHidden()) {
/* 1879:2710 */       return;
/* 1880:     */     }
/* 1881:2714 */     int startIdx = findStartOfRowOutlineGroup(rowNumber);
/* 1882:     */     
/* 1883:     */ 
/* 1884:2717 */     int endIdx = findEndOfRowOutlineGroup(rowNumber);
/* 1885:     */     
/* 1886:     */ 
/* 1887:     */ 
/* 1888:     */ 
/* 1889:     */ 
/* 1890:     */ 
/* 1891:     */ 
/* 1892:     */ 
/* 1893:     */ 
/* 1894:     */ 
/* 1895:     */ 
/* 1896:     */ 
/* 1897:2730 */     short level = row.getCTRow().getOutlineLevel();
/* 1898:2731 */     if (!isRowGroupHiddenByParent(rowNumber)) {
/* 1899:2732 */       for (int i = startIdx; i < endIdx; i++) {
/* 1900:2733 */         if (level == getRow(i).getCTRow().getOutlineLevel()) {
/* 1901:2734 */           getRow(i).getCTRow().unsetHidden();
/* 1902:2735 */         } else if (!isRowGroupCollapsed(i)) {
/* 1903:2736 */           getRow(i).getCTRow().unsetHidden();
/* 1904:     */         }
/* 1905:     */       }
/* 1906:     */     }
/* 1907:2741 */     CTRow ctRow = getRow(endIdx).getCTRow();
/* 1908:2743 */     if (ctRow.getCollapsed()) {
/* 1909:2744 */       ctRow.unsetCollapsed();
/* 1910:     */     }
/* 1911:     */   }
/* 1912:     */   
/* 1913:     */   public int findEndOfRowOutlineGroup(int row)
/* 1914:     */   {
/* 1915:2752 */     short level = getRow(row).getCTRow().getOutlineLevel();
/* 1916:     */     
/* 1917:2754 */     int lastRowNum = getLastRowNum();
/* 1918:2755 */     for (int currentRow = row; currentRow < lastRowNum; currentRow++) {
/* 1919:2756 */       if ((getRow(currentRow) == null) || (getRow(currentRow).getCTRow().getOutlineLevel() < level)) {
/* 1920:     */         break;
/* 1921:     */       }
/* 1922:     */     }
/* 1923:2761 */     return currentRow;
/* 1924:     */   }
/* 1925:     */   
/* 1926:     */   private boolean isRowGroupHiddenByParent(int row)
/* 1927:     */   {
/* 1928:2771 */     int endOfOutlineGroupIdx = findEndOfRowOutlineGroup(row);
/* 1929:     */     boolean endHidden;
/* 1930:     */     int endLevel;
/* 1931:     */     boolean endHidden;
/* 1932:2772 */     if (getRow(endOfOutlineGroupIdx) == null)
/* 1933:     */     {
/* 1934:2773 */       int endLevel = 0;
/* 1935:2774 */       endHidden = false;
/* 1936:     */     }
/* 1937:     */     else
/* 1938:     */     {
/* 1939:2776 */       endLevel = getRow(endOfOutlineGroupIdx).getCTRow().getOutlineLevel();
/* 1940:2777 */       endHidden = getRow(endOfOutlineGroupIdx).getCTRow().getHidden();
/* 1941:     */     }
/* 1942:2783 */     int startOfOutlineGroupIdx = findStartOfRowOutlineGroup(row);
/* 1943:     */     boolean startHidden;
/* 1944:     */     int startLevel;
/* 1945:     */     boolean startHidden;
/* 1946:2784 */     if ((startOfOutlineGroupIdx < 0) || (getRow(startOfOutlineGroupIdx) == null))
/* 1947:     */     {
/* 1948:2786 */       int startLevel = 0;
/* 1949:2787 */       startHidden = false;
/* 1950:     */     }
/* 1951:     */     else
/* 1952:     */     {
/* 1953:2789 */       startLevel = getRow(startOfOutlineGroupIdx).getCTRow().getOutlineLevel();
/* 1954:     */       
/* 1955:2791 */       startHidden = getRow(startOfOutlineGroupIdx).getCTRow().getHidden();
/* 1956:     */     }
/* 1957:2794 */     if (endLevel > startLevel) {
/* 1958:2795 */       return endHidden;
/* 1959:     */     }
/* 1960:2797 */     return startHidden;
/* 1961:     */   }
/* 1962:     */   
/* 1963:     */   private boolean isRowGroupCollapsed(int row)
/* 1964:     */   {
/* 1965:2804 */     int collapseRow = findEndOfRowOutlineGroup(row) + 1;
/* 1966:2805 */     if (getRow(collapseRow) == null) {
/* 1967:2806 */       return false;
/* 1968:     */     }
/* 1969:2808 */     return getRow(collapseRow).getCTRow().getCollapsed();
/* 1970:     */   }
/* 1971:     */   
/* 1972:     */   public void setZoom(int scale)
/* 1973:     */   {
/* 1974:2832 */     if ((scale < 10) || (scale > 400)) {
/* 1975:2833 */       throw new IllegalArgumentException("Valid scale values range from 10 to 400");
/* 1976:     */     }
/* 1977:2835 */     getSheetTypeSheetView().setZoomScale(scale);
/* 1978:     */   }
/* 1979:     */   
/* 1980:     */   public void copyRows(List<? extends Row> srcRows, int destStartRow, CellCopyPolicy policy)
/* 1981:     */   {
/* 1982:2854 */     if ((srcRows == null) || (srcRows.size() == 0)) {
/* 1983:2855 */       throw new IllegalArgumentException("No rows to copy");
/* 1984:     */     }
/* 1985:2857 */     Row srcStartRow = (Row)srcRows.get(0);
/* 1986:2858 */     Row srcEndRow = (Row)srcRows.get(srcRows.size() - 1);
/* 1987:2860 */     if (srcStartRow == null) {
/* 1988:2861 */       throw new IllegalArgumentException("copyRows: First row cannot be null");
/* 1989:     */     }
/* 1990:2864 */     int srcStartRowNum = srcStartRow.getRowNum();
/* 1991:2865 */     int srcEndRowNum = srcEndRow.getRowNum();
/* 1992:     */     
/* 1993:     */ 
/* 1994:     */ 
/* 1995:2869 */     int size = srcRows.size();
/* 1996:2870 */     for (int index = 1; index < size; index++)
/* 1997:     */     {
/* 1998:2871 */       Row curRow = (Row)srcRows.get(index);
/* 1999:2872 */       if (curRow == null) {
/* 2000:2873 */         throw new IllegalArgumentException("srcRows may not contain null rows. Found null row at index " + index + ".");
/* 2001:     */       }
/* 2002:2878 */       if (srcStartRow.getSheet().getWorkbook() != curRow.getSheet().getWorkbook()) {
/* 2003:2879 */         throw new IllegalArgumentException("All rows in srcRows must belong to the same sheet in the same workbook.Expected all rows from same workbook (" + srcStartRow.getSheet().getWorkbook() + "). " + "Got srcRows[" + index + "] from different workbook (" + curRow.getSheet().getWorkbook() + ").");
/* 2004:     */       }
/* 2005:2882 */       if (srcStartRow.getSheet() != curRow.getSheet()) {
/* 2006:2883 */         throw new IllegalArgumentException("All rows in srcRows must belong to the same sheet. Expected all rows from " + srcStartRow.getSheet().getSheetName() + ". " + "Got srcRows[" + index + "] from " + curRow.getSheet().getSheetName());
/* 2007:     */       }
/* 2008:     */     }
/* 2009:2891 */     CellCopyPolicy options = new CellCopyPolicy(policy);
/* 2010:     */     
/* 2011:     */ 
/* 2012:2894 */     options.setCopyMergedRegions(false);
/* 2013:     */     
/* 2014:     */ 
/* 2015:     */ 
/* 2016:     */ 
/* 2017:2899 */     int r = destStartRow;
/* 2018:2900 */     for (Row srcRow : srcRows)
/* 2019:     */     {
/* 2020:     */       int destRowNum;
/* 2021:     */       int destRowNum;
/* 2022:2902 */       if (policy.isCondenseRows())
/* 2023:     */       {
/* 2024:2903 */         destRowNum = r++;
/* 2025:     */       }
/* 2026:     */       else
/* 2027:     */       {
/* 2028:2905 */         int shift = srcRow.getRowNum() - srcStartRowNum;
/* 2029:2906 */         destRowNum = destStartRow + shift;
/* 2030:     */       }
/* 2031:2909 */       XSSFRow destRow = createRow(destRowNum);
/* 2032:2910 */       destRow.copyRowFrom(srcRow, options);
/* 2033:     */     }
/* 2034:     */     int shift;
/* 2035:2918 */     if (policy.isCopyMergedRegions())
/* 2036:     */     {
/* 2037:2920 */       shift = destStartRow - srcStartRowNum;
/* 2038:2921 */       for (CellRangeAddress srcRegion : srcStartRow.getSheet().getMergedRegions()) {
/* 2039:2922 */         if ((srcStartRowNum <= srcRegion.getFirstRow()) && (srcRegion.getLastRow() <= srcEndRowNum))
/* 2040:     */         {
/* 2041:2924 */           CellRangeAddress destRegion = srcRegion.copy();
/* 2042:2925 */           destRegion.setFirstRow(destRegion.getFirstRow() + shift);
/* 2043:2926 */           destRegion.setLastRow(destRegion.getLastRow() + shift);
/* 2044:2927 */           addMergedRegion(destRegion);
/* 2045:     */         }
/* 2046:     */       }
/* 2047:     */     }
/* 2048:     */   }
/* 2049:     */   
/* 2050:     */   public void copyRows(int srcStartRow, int srcEndRow, int destStartRow, CellCopyPolicy cellCopyPolicy)
/* 2051:     */   {
/* 2052:2946 */     List<XSSFRow> srcRows = getRows(srcStartRow, srcEndRow, false);
/* 2053:2947 */     copyRows(srcRows, destStartRow, cellCopyPolicy);
/* 2054:     */   }
/* 2055:     */   
/* 2056:     */   public void shiftRows(int startRow, int endRow, int n)
/* 2057:     */   {
/* 2058:2966 */     shiftRows(startRow, endRow, n, false, false);
/* 2059:     */   }
/* 2060:     */   
/* 2061:     */   public void shiftRows(int startRow, int endRow, final int n, boolean copyRowHeight, boolean resetOriginalRowHeight)
/* 2062:     */   {
/* 2063:2987 */     XSSFVMLDrawing vml = getVMLDrawing(false);
/* 2064:2990 */     for (Iterator<Row> it = rowIterator(); it.hasNext();)
/* 2065:     */     {
/* 2066:2991 */       XSSFRow row = (XSSFRow)it.next();
/* 2067:2992 */       rownum = row.getRowNum();
/* 2068:2995 */       if (shouldRemoveRow(startRow, endRow, n, rownum))
/* 2069:     */       {
/* 2070:2998 */         Integer rownumI = new Integer(row.getRowNum());
/* 2071:2999 */         int idx = this._rows.headMap(rownumI).size();
/* 2072:3000 */         this.worksheet.getSheetData().removeRow(idx);
/* 2073:     */         
/* 2074:     */ 
/* 2075:3003 */         it.remove();
/* 2076:3007 */         if (this.sheetComments != null)
/* 2077:     */         {
/* 2078:3008 */           CTCommentList lst = this.sheetComments.getCTComments().getCommentList();
/* 2079:3009 */           for (CTComment comment : lst.getCommentArray())
/* 2080:     */           {
/* 2081:3010 */             String strRef = comment.getRef();
/* 2082:3011 */             CellAddress ref = new CellAddress(strRef);
/* 2083:3014 */             if (ref.getRow() == rownum)
/* 2084:     */             {
/* 2085:3015 */               this.sheetComments.removeComment(ref);
/* 2086:3016 */               vml.removeCommentShape(ref.getRow(), ref.getColumn());
/* 2087:     */             }
/* 2088:     */           }
/* 2089:     */         }
/* 2090:3022 */         if (this.hyperlinks != null) {
/* 2091:3023 */           for (XSSFHyperlink link : new ArrayList(this.hyperlinks))
/* 2092:     */           {
/* 2093:3024 */             CellReference ref = new CellReference(link.getCellRef());
/* 2094:3025 */             if (ref.getRow() == rownum) {
/* 2095:3026 */               this.hyperlinks.remove(link);
/* 2096:     */             }
/* 2097:     */           }
/* 2098:     */         }
/* 2099:     */       }
/* 2100:     */     }
/* 2101:     */     int rownum;
/* 2102:3036 */     SortedMap<XSSFComment, Integer> commentsToShift = new TreeMap(new Comparator()
/* 2103:     */     {
/* 2104:     */       public int compare(XSSFComment o1, XSSFComment o2)
/* 2105:     */       {
/* 2106:3039 */         int row1 = o1.getRow();
/* 2107:3040 */         int row2 = o2.getRow();
/* 2108:3042 */         if (row1 == row2) {
/* 2109:3045 */           return o1.hashCode() - o2.hashCode();
/* 2110:     */         }
/* 2111:3049 */         if (n > 0) {
/* 2112:3050 */           return row1 < row2 ? 1 : -1;
/* 2113:     */         }
/* 2114:3053 */         return row1 > row2 ? 1 : -1;
/* 2115:     */       }
/* 2116:     */     });
/* 2117:3059 */     for (Iterator<Row> it = rowIterator(); it.hasNext();)
/* 2118:     */     {
/* 2119:3060 */       XSSFRow row = (XSSFRow)it.next();
/* 2120:3061 */       int rownum = row.getRowNum();
/* 2121:3063 */       if (this.sheetComments != null)
/* 2122:     */       {
/* 2123:3065 */         int newrownum = shiftedRowNum(startRow, endRow, n, rownum);
/* 2124:3068 */         if (newrownum != rownum)
/* 2125:     */         {
/* 2126:3069 */           CTCommentList lst = this.sheetComments.getCTComments().getCommentList();
/* 2127:3070 */           for (CTComment comment : lst.getCommentArray())
/* 2128:     */           {
/* 2129:3071 */             String oldRef = comment.getRef();
/* 2130:3072 */             CellReference ref = new CellReference(oldRef);
/* 2131:3075 */             if (ref.getRow() == rownum)
/* 2132:     */             {
/* 2133:3076 */               XSSFComment xssfComment = new XSSFComment(this.sheetComments, comment, vml == null ? null : vml.findCommentShape(rownum, ref.getCol()));
/* 2134:     */               
/* 2135:     */ 
/* 2136:     */ 
/* 2137:     */ 
/* 2138:3081 */               commentsToShift.put(xssfComment, Integer.valueOf(newrownum));
/* 2139:     */             }
/* 2140:     */           }
/* 2141:     */         }
/* 2142:     */       }
/* 2143:3087 */       if ((rownum >= startRow) && (rownum <= endRow))
/* 2144:     */       {
/* 2145:3091 */         if (!copyRowHeight) {
/* 2146:3092 */           row.setHeight((short)-1);
/* 2147:     */         }
/* 2148:3095 */         row.shift(n);
/* 2149:     */       }
/* 2150:     */     }
/* 2151:3101 */     for (Entry<XSSFComment, Integer> entry : commentsToShift.entrySet()) {
/* 2152:3102 */       ((XSSFComment)entry.getKey()).setRow(((Integer)entry.getValue()).intValue());
/* 2153:     */     }
/* 2154:3105 */     XSSFRowShifter rowShifter = new XSSFRowShifter(this);
/* 2155:     */     
/* 2156:3107 */     int sheetIndex = getWorkbook().getSheetIndex(this);
/* 2157:3108 */     String sheetName = getWorkbook().getSheetName(sheetIndex);
/* 2158:3109 */     FormulaShifter shifter = FormulaShifter.createForRowShift(sheetIndex, sheetName, startRow, endRow, n, SpreadsheetVersion.EXCEL2007);
/* 2159:     */     
/* 2160:     */ 
/* 2161:3112 */     rowShifter.updateNamedRanges(shifter);
/* 2162:3113 */     rowShifter.updateFormulas(shifter);
/* 2163:3114 */     rowShifter.shiftMergedRegions(startRow, endRow, n);
/* 2164:3115 */     rowShifter.updateConditionalFormatting(shifter);
/* 2165:3116 */     rowShifter.updateHyperlinks(shifter);
/* 2166:     */     
/* 2167:     */ 
/* 2168:3119 */     Map<Integer, XSSFRow> map = new HashMap();
/* 2169:3120 */     for (XSSFRow r : this._rows.values())
/* 2170:     */     {
/* 2171:3122 */       Integer rownumI = new Integer(r.getRowNum());
/* 2172:3123 */       map.put(rownumI, r);
/* 2173:     */     }
/* 2174:3125 */     this._rows.clear();
/* 2175:3126 */     this._rows.putAll(map);
/* 2176:     */   }
/* 2177:     */   
/* 2178:     */   private int shiftedRowNum(int startRow, int endRow, int n, int rownum)
/* 2179:     */   {
/* 2180:3131 */     if ((rownum < startRow) && ((n > 0) || (startRow - rownum > n))) {
/* 2181:3132 */       return rownum;
/* 2182:     */     }
/* 2183:3136 */     if ((rownum > endRow) && ((n < 0) || (rownum - endRow > n))) {
/* 2184:3137 */       return rownum;
/* 2185:     */     }
/* 2186:3141 */     if (rownum < startRow) {
/* 2187:3143 */       return rownum + (endRow - startRow);
/* 2188:     */     }
/* 2189:3147 */     if (rownum > endRow) {
/* 2190:3149 */       return rownum - (endRow - startRow);
/* 2191:     */     }
/* 2192:3153 */     return rownum + n;
/* 2193:     */   }
/* 2194:     */   
/* 2195:     */   public void showInPane(int toprow, int leftcol)
/* 2196:     */   {
/* 2197:3165 */     CellReference cellReference = new CellReference(toprow, leftcol);
/* 2198:3166 */     String cellRef = cellReference.formatAsString();
/* 2199:3167 */     getPane().setTopLeftCell(cellRef);
/* 2200:     */   }
/* 2201:     */   
/* 2202:     */   public void ungroupColumn(int fromColumn, int toColumn)
/* 2203:     */   {
/* 2204:3172 */     CTCols cols = this.worksheet.getColsArray(0);
/* 2205:3173 */     for (int index = fromColumn; index <= toColumn; index++)
/* 2206:     */     {
/* 2207:3174 */       CTCol col = this.columnHelper.getColumn(index, false);
/* 2208:3175 */       if (col != null)
/* 2209:     */       {
/* 2210:3176 */         short outlineLevel = col.getOutlineLevel();
/* 2211:3177 */         col.setOutlineLevel((short)(outlineLevel - 1));
/* 2212:3178 */         index = (int)col.getMax();
/* 2213:3180 */         if (col.getOutlineLevel() <= 0)
/* 2214:     */         {
/* 2215:3181 */           int colIndex = this.columnHelper.getIndexOfColumn(cols, col);
/* 2216:3182 */           this.worksheet.getColsArray(0).removeCol(colIndex);
/* 2217:     */         }
/* 2218:     */       }
/* 2219:     */     }
/* 2220:3186 */     this.worksheet.setColsArray(0, cols);
/* 2221:3187 */     setSheetFormatPrOutlineLevelCol();
/* 2222:     */   }
/* 2223:     */   
/* 2224:     */   public void ungroupRow(int fromRow, int toRow)
/* 2225:     */   {
/* 2226:3198 */     for (int i = fromRow; i <= toRow; i++)
/* 2227:     */     {
/* 2228:3199 */       XSSFRow xrow = getRow(i);
/* 2229:3200 */       if (xrow != null)
/* 2230:     */       {
/* 2231:3201 */         CTRow ctRow = xrow.getCTRow();
/* 2232:3202 */         int outlineLevel = ctRow.getOutlineLevel();
/* 2233:3203 */         ctRow.setOutlineLevel((short)(outlineLevel - 1));
/* 2234:3205 */         if ((outlineLevel == 1) && (xrow.getFirstCellNum() == -1)) {
/* 2235:3206 */           removeRow(xrow);
/* 2236:     */         }
/* 2237:     */       }
/* 2238:     */     }
/* 2239:3210 */     setSheetFormatPrOutlineLevelRow();
/* 2240:     */   }
/* 2241:     */   
/* 2242:     */   private void setSheetFormatPrOutlineLevelRow()
/* 2243:     */   {
/* 2244:3214 */     short maxLevelRow = getMaxOutlineLevelRows();
/* 2245:3215 */     getSheetTypeSheetFormatPr().setOutlineLevelRow(maxLevelRow);
/* 2246:     */   }
/* 2247:     */   
/* 2248:     */   private void setSheetFormatPrOutlineLevelCol()
/* 2249:     */   {
/* 2250:3219 */     short maxLevelCol = getMaxOutlineLevelCols();
/* 2251:3220 */     getSheetTypeSheetFormatPr().setOutlineLevelCol(maxLevelCol);
/* 2252:     */   }
/* 2253:     */   
/* 2254:     */   private CTSheetViews getSheetTypeSheetViews()
/* 2255:     */   {
/* 2256:3224 */     if (this.worksheet.getSheetViews() == null)
/* 2257:     */     {
/* 2258:3225 */       this.worksheet.setSheetViews(CTSheetViews.Factory.newInstance());
/* 2259:3226 */       this.worksheet.getSheetViews().addNewSheetView();
/* 2260:     */     }
/* 2261:3228 */     return this.worksheet.getSheetViews();
/* 2262:     */   }
/* 2263:     */   
/* 2264:     */   public boolean isSelected()
/* 2265:     */   {
/* 2266:3243 */     CTSheetView view = getDefaultSheetView();
/* 2267:3244 */     return (view != null) && (view.getTabSelected());
/* 2268:     */   }
/* 2269:     */   
/* 2270:     */   public void setSelected(boolean value)
/* 2271:     */   {
/* 2272:3260 */     CTSheetViews views = getSheetTypeSheetViews();
/* 2273:3261 */     for (CTSheetView view : views.getSheetViewArray()) {
/* 2274:3262 */       view.setTabSelected(value);
/* 2275:     */     }
/* 2276:     */   }
/* 2277:     */   
/* 2278:     */   @Internal
/* 2279:     */   public void addHyperlink(XSSFHyperlink hyperlink)
/* 2280:     */   {
/* 2281:3273 */     this.hyperlinks.add(hyperlink);
/* 2282:     */   }
/* 2283:     */   
/* 2284:     */   @Internal
/* 2285:     */   public void removeHyperlink(int row, int column)
/* 2286:     */   {
/* 2287:3287 */     String ref = new CellReference(row, column).formatAsString();
/* 2288:3288 */     for (Iterator<XSSFHyperlink> it = this.hyperlinks.iterator(); it.hasNext();)
/* 2289:     */     {
/* 2290:3289 */       XSSFHyperlink hyperlink = (XSSFHyperlink)it.next();
/* 2291:3290 */       if (hyperlink.getCellRef().equals(ref))
/* 2292:     */       {
/* 2293:3291 */         it.remove();
/* 2294:3292 */         return;
/* 2295:     */       }
/* 2296:     */     }
/* 2297:     */   }
/* 2298:     */   
/* 2299:     */   public CellAddress getActiveCell()
/* 2300:     */   {
/* 2301:3304 */     String address = getSheetTypeSelection().getActiveCell();
/* 2302:3305 */     if (address == null) {
/* 2303:3306 */       return null;
/* 2304:     */     }
/* 2305:3308 */     return new CellAddress(address);
/* 2306:     */   }
/* 2307:     */   
/* 2308:     */   public void setActiveCell(CellAddress address)
/* 2309:     */   {
/* 2310:3316 */     String ref = address.formatAsString();
/* 2311:3317 */     CTSelection ctsel = getSheetTypeSelection();
/* 2312:3318 */     ctsel.setActiveCell(ref);
/* 2313:3319 */     ctsel.setSqref(Arrays.asList(new String[] { ref }));
/* 2314:     */   }
/* 2315:     */   
/* 2316:     */   public boolean hasComments()
/* 2317:     */   {
/* 2318:3327 */     return (this.sheetComments != null) && (this.sheetComments.getNumberOfComments() > 0);
/* 2319:     */   }
/* 2320:     */   
/* 2321:     */   protected int getNumberOfComments()
/* 2322:     */   {
/* 2323:3331 */     return this.sheetComments == null ? 0 : this.sheetComments.getNumberOfComments();
/* 2324:     */   }
/* 2325:     */   
/* 2326:     */   private CTSelection getSheetTypeSelection()
/* 2327:     */   {
/* 2328:3335 */     if (getSheetTypeSheetView().sizeOfSelectionArray() == 0) {
/* 2329:3336 */       getSheetTypeSheetView().insertNewSelection(0);
/* 2330:     */     }
/* 2331:3338 */     return getSheetTypeSheetView().getSelectionArray(0);
/* 2332:     */   }
/* 2333:     */   
/* 2334:     */   private CTSheetView getDefaultSheetView()
/* 2335:     */   {
/* 2336:3351 */     CTSheetViews views = getSheetTypeSheetViews();
/* 2337:3352 */     if (views == null) {
/* 2338:3353 */       return null;
/* 2339:     */     }
/* 2340:3355 */     int sz = views.sizeOfSheetViewArray();
/* 2341:3356 */     return sz == 0 ? null : views.getSheetViewArray(sz - 1);
/* 2342:     */   }
/* 2343:     */   
/* 2344:     */   protected CommentsTable getCommentsTable(boolean create)
/* 2345:     */   {
/* 2346:3366 */     if ((this.sheetComments == null) && (create)) {
/* 2347:     */       try
/* 2348:     */       {
/* 2349:3370 */         this.sheetComments = ((CommentsTable)createRelationship(XSSFRelation.SHEET_COMMENTS, XSSFFactory.getInstance(), (int)this.sheet.getSheetId()));
/* 2350:     */       }
/* 2351:     */       catch (PartAlreadyExistsException e)
/* 2352:     */       {
/* 2353:3376 */         this.sheetComments = ((CommentsTable)createRelationship(XSSFRelation.SHEET_COMMENTS, XSSFFactory.getInstance(), -1));
/* 2354:     */       }
/* 2355:     */     }
/* 2356:3380 */     return this.sheetComments;
/* 2357:     */   }
/* 2358:     */   
/* 2359:     */   private CTPageSetUpPr getSheetTypePageSetUpPr()
/* 2360:     */   {
/* 2361:3384 */     CTSheetPr sheetPr = getSheetTypeSheetPr();
/* 2362:3385 */     return sheetPr.isSetPageSetUpPr() ? sheetPr.getPageSetUpPr() : sheetPr.addNewPageSetUpPr();
/* 2363:     */   }
/* 2364:     */   
/* 2365:     */   private static boolean shouldRemoveRow(int startRow, int endRow, int n, int rownum)
/* 2366:     */   {
/* 2367:3390 */     if ((rownum >= startRow + n) && (rownum <= endRow + n))
/* 2368:     */     {
/* 2369:3392 */       if ((n > 0) && (rownum > endRow)) {
/* 2370:3393 */         return true;
/* 2371:     */       }
/* 2372:3395 */       if ((n < 0) && (rownum < startRow)) {
/* 2373:3396 */         return true;
/* 2374:     */       }
/* 2375:     */     }
/* 2376:3399 */     return false;
/* 2377:     */   }
/* 2378:     */   
/* 2379:     */   private CTPane getPane()
/* 2380:     */   {
/* 2381:3403 */     if (getDefaultSheetView().getPane() == null) {
/* 2382:3404 */       getDefaultSheetView().addNewPane();
/* 2383:     */     }
/* 2384:3406 */     return getDefaultSheetView().getPane();
/* 2385:     */   }
/* 2386:     */   
/* 2387:     */   @Internal
/* 2388:     */   public CTCellFormula getSharedFormula(int sid)
/* 2389:     */   {
/* 2390:3417 */     return (CTCellFormula)this.sharedFormulas.get(Integer.valueOf(sid));
/* 2391:     */   }
/* 2392:     */   
/* 2393:     */   void onReadCell(XSSFCell cell)
/* 2394:     */   {
/* 2395:3422 */     CTCell ct = cell.getCTCell();
/* 2396:3423 */     CTCellFormula f = ct.getF();
/* 2397:3424 */     if ((f != null) && (f.getT() == STCellFormulaType.SHARED) && (f.isSetRef()) && (f.getStringValue() != null))
/* 2398:     */     {
/* 2399:3427 */       CTCellFormula sf = (CTCellFormula)f.copy();
/* 2400:3428 */       CellRangeAddress sfRef = CellRangeAddress.valueOf(sf.getRef());
/* 2401:3429 */       CellReference cellRef = new CellReference(cell);
/* 2402:3433 */       if ((cellRef.getCol() > sfRef.getFirstColumn()) || (cellRef.getRow() > sfRef.getFirstRow()))
/* 2403:     */       {
/* 2404:3434 */         String effectiveRef = new CellRangeAddress(Math.max(cellRef.getRow(), sfRef.getFirstRow()), sfRef.getLastRow(), Math.max(cellRef.getCol(), sfRef.getFirstColumn()), sfRef.getLastColumn()).formatAsString();
/* 2405:     */         
/* 2406:     */ 
/* 2407:3437 */         sf.setRef(effectiveRef);
/* 2408:     */       }
/* 2409:3440 */       this.sharedFormulas.put(Integer.valueOf((int)f.getSi()), sf);
/* 2410:     */     }
/* 2411:3442 */     if ((f != null) && (f.getT() == STCellFormulaType.ARRAY) && (f.getRef() != null)) {
/* 2412:3443 */       this.arrayFormulas.add(CellRangeAddress.valueOf(f.getRef()));
/* 2413:     */     }
/* 2414:     */   }
/* 2415:     */   
/* 2416:     */   protected void commit()
/* 2417:     */     throws IOException
/* 2418:     */   {
/* 2419:3449 */     PackagePart part = getPackagePart();
/* 2420:3450 */     OutputStream out = part.getOutputStream();
/* 2421:3451 */     write(out);
/* 2422:3452 */     out.close();
/* 2423:     */   }
/* 2424:     */   
/* 2425:     */   protected void write(OutputStream out)
/* 2426:     */     throws IOException
/* 2427:     */   {
/* 2428:3456 */     boolean setToNull = false;
/* 2429:3457 */     if (this.worksheet.sizeOfColsArray() == 1)
/* 2430:     */     {
/* 2431:3458 */       CTCols col = this.worksheet.getColsArray(0);
/* 2432:3459 */       if (col.sizeOfColArray() == 0)
/* 2433:     */       {
/* 2434:3460 */         setToNull = true;
/* 2435:     */         
/* 2436:     */ 
/* 2437:3463 */         this.worksheet.setColsArray(null);
/* 2438:     */       }
/* 2439:     */       else
/* 2440:     */       {
/* 2441:3465 */         setColWidthAttribute(col);
/* 2442:     */       }
/* 2443:     */     }
/* 2444:3470 */     if (this.hyperlinks.size() > 0)
/* 2445:     */     {
/* 2446:3471 */       if (this.worksheet.getHyperlinks() == null) {
/* 2447:3472 */         this.worksheet.addNewHyperlinks();
/* 2448:     */       }
/* 2449:3474 */       CTHyperlink[] ctHls = new CTHyperlink[this.hyperlinks.size()];
/* 2450:3475 */       for (int i = 0; i < ctHls.length; i++)
/* 2451:     */       {
/* 2452:3478 */         XSSFHyperlink hyperlink = (XSSFHyperlink)this.hyperlinks.get(i);
/* 2453:3479 */         hyperlink.generateRelationIfNeeded(getPackagePart());
/* 2454:     */         
/* 2455:3481 */         ctHls[i] = hyperlink.getCTHyperlink();
/* 2456:     */       }
/* 2457:3483 */       this.worksheet.getHyperlinks().setHyperlinkArray(ctHls);
/* 2458:     */     }
/* 2459:3486 */     else if (this.worksheet.getHyperlinks() != null)
/* 2460:     */     {
/* 2461:3487 */       int count = this.worksheet.getHyperlinks().sizeOfHyperlinkArray();
/* 2462:3488 */       for (int i = count - 1; i >= 0; i--) {
/* 2463:3489 */         this.worksheet.getHyperlinks().removeHyperlink(i);
/* 2464:     */       }
/* 2465:3495 */       this.worksheet.unsetHyperlinks();
/* 2466:     */     }
/* 2467:3501 */     int minCell = 2147483647;int maxCell = -2147483648;
/* 2468:3502 */     for (XSSFRow row : this._rows.values())
/* 2469:     */     {
/* 2470:3504 */       row.onDocumentWrite();
/* 2471:3507 */       if (row.getFirstCellNum() != -1) {
/* 2472:3508 */         minCell = Math.min(minCell, row.getFirstCellNum());
/* 2473:     */       }
/* 2474:3510 */       if (row.getLastCellNum() != -1) {
/* 2475:3511 */         maxCell = Math.max(maxCell, row.getLastCellNum());
/* 2476:     */       }
/* 2477:     */     }
/* 2478:3516 */     if (minCell != 2147483647)
/* 2479:     */     {
/* 2480:3517 */       String ref = new CellRangeAddress(getFirstRowNum(), getLastRowNum(), minCell, maxCell).formatAsString();
/* 2481:3518 */       if (this.worksheet.isSetDimension()) {
/* 2482:3519 */         this.worksheet.getDimension().setRef(ref);
/* 2483:     */       } else {
/* 2484:3521 */         this.worksheet.addNewDimension().setRef(ref);
/* 2485:     */       }
/* 2486:     */     }
/* 2487:3525 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 2488:3526 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTWorksheet.type.getName().getNamespaceURI(), "worksheet"));
/* 2489:     */     
/* 2490:3528 */     this.worksheet.save(out, xmlOptions);
/* 2491:3531 */     if (setToNull) {
/* 2492:3532 */       this.worksheet.addNewCols();
/* 2493:     */     }
/* 2494:     */   }
/* 2495:     */   
/* 2496:     */   public boolean isAutoFilterLocked()
/* 2497:     */   {
/* 2498:3540 */     return (isSheetLocked()) && (safeGetProtectionField().getAutoFilter());
/* 2499:     */   }
/* 2500:     */   
/* 2501:     */   public boolean isDeleteColumnsLocked()
/* 2502:     */   {
/* 2503:3547 */     return (isSheetLocked()) && (safeGetProtectionField().getDeleteColumns());
/* 2504:     */   }
/* 2505:     */   
/* 2506:     */   public boolean isDeleteRowsLocked()
/* 2507:     */   {
/* 2508:3554 */     return (isSheetLocked()) && (safeGetProtectionField().getDeleteRows());
/* 2509:     */   }
/* 2510:     */   
/* 2511:     */   public boolean isFormatCellsLocked()
/* 2512:     */   {
/* 2513:3561 */     return (isSheetLocked()) && (safeGetProtectionField().getFormatCells());
/* 2514:     */   }
/* 2515:     */   
/* 2516:     */   public boolean isFormatColumnsLocked()
/* 2517:     */   {
/* 2518:3568 */     return (isSheetLocked()) && (safeGetProtectionField().getFormatColumns());
/* 2519:     */   }
/* 2520:     */   
/* 2521:     */   public boolean isFormatRowsLocked()
/* 2522:     */   {
/* 2523:3575 */     return (isSheetLocked()) && (safeGetProtectionField().getFormatRows());
/* 2524:     */   }
/* 2525:     */   
/* 2526:     */   public boolean isInsertColumnsLocked()
/* 2527:     */   {
/* 2528:3582 */     return (isSheetLocked()) && (safeGetProtectionField().getInsertColumns());
/* 2529:     */   }
/* 2530:     */   
/* 2531:     */   public boolean isInsertHyperlinksLocked()
/* 2532:     */   {
/* 2533:3589 */     return (isSheetLocked()) && (safeGetProtectionField().getInsertHyperlinks());
/* 2534:     */   }
/* 2535:     */   
/* 2536:     */   public boolean isInsertRowsLocked()
/* 2537:     */   {
/* 2538:3596 */     return (isSheetLocked()) && (safeGetProtectionField().getInsertRows());
/* 2539:     */   }
/* 2540:     */   
/* 2541:     */   public boolean isPivotTablesLocked()
/* 2542:     */   {
/* 2543:3603 */     return (isSheetLocked()) && (safeGetProtectionField().getPivotTables());
/* 2544:     */   }
/* 2545:     */   
/* 2546:     */   public boolean isSortLocked()
/* 2547:     */   {
/* 2548:3610 */     return (isSheetLocked()) && (safeGetProtectionField().getSort());
/* 2549:     */   }
/* 2550:     */   
/* 2551:     */   public boolean isObjectsLocked()
/* 2552:     */   {
/* 2553:3617 */     return (isSheetLocked()) && (safeGetProtectionField().getObjects());
/* 2554:     */   }
/* 2555:     */   
/* 2556:     */   public boolean isScenariosLocked()
/* 2557:     */   {
/* 2558:3624 */     return (isSheetLocked()) && (safeGetProtectionField().getScenarios());
/* 2559:     */   }
/* 2560:     */   
/* 2561:     */   public boolean isSelectLockedCellsLocked()
/* 2562:     */   {
/* 2563:3631 */     return (isSheetLocked()) && (safeGetProtectionField().getSelectLockedCells());
/* 2564:     */   }
/* 2565:     */   
/* 2566:     */   public boolean isSelectUnlockedCellsLocked()
/* 2567:     */   {
/* 2568:3638 */     return (isSheetLocked()) && (safeGetProtectionField().getSelectUnlockedCells());
/* 2569:     */   }
/* 2570:     */   
/* 2571:     */   public boolean isSheetLocked()
/* 2572:     */   {
/* 2573:3645 */     return (this.worksheet.isSetSheetProtection()) && (safeGetProtectionField().getSheet());
/* 2574:     */   }
/* 2575:     */   
/* 2576:     */   public void enableLocking()
/* 2577:     */   {
/* 2578:3652 */     safeGetProtectionField().setSheet(true);
/* 2579:     */   }
/* 2580:     */   
/* 2581:     */   public void disableLocking()
/* 2582:     */   {
/* 2583:3659 */     safeGetProtectionField().setSheet(false);
/* 2584:     */   }
/* 2585:     */   
/* 2586:     */   public void lockAutoFilter(boolean enabled)
/* 2587:     */   {
/* 2588:3668 */     safeGetProtectionField().setAutoFilter(enabled);
/* 2589:     */   }
/* 2590:     */   
/* 2591:     */   public void lockDeleteColumns(boolean enabled)
/* 2592:     */   {
/* 2593:3677 */     safeGetProtectionField().setDeleteColumns(enabled);
/* 2594:     */   }
/* 2595:     */   
/* 2596:     */   public void lockDeleteRows(boolean enabled)
/* 2597:     */   {
/* 2598:3686 */     safeGetProtectionField().setDeleteRows(enabled);
/* 2599:     */   }
/* 2600:     */   
/* 2601:     */   public void lockFormatCells(boolean enabled)
/* 2602:     */   {
/* 2603:3695 */     safeGetProtectionField().setFormatCells(enabled);
/* 2604:     */   }
/* 2605:     */   
/* 2606:     */   public void lockFormatColumns(boolean enabled)
/* 2607:     */   {
/* 2608:3704 */     safeGetProtectionField().setFormatColumns(enabled);
/* 2609:     */   }
/* 2610:     */   
/* 2611:     */   public void lockFormatRows(boolean enabled)
/* 2612:     */   {
/* 2613:3713 */     safeGetProtectionField().setFormatRows(enabled);
/* 2614:     */   }
/* 2615:     */   
/* 2616:     */   public void lockInsertColumns(boolean enabled)
/* 2617:     */   {
/* 2618:3722 */     safeGetProtectionField().setInsertColumns(enabled);
/* 2619:     */   }
/* 2620:     */   
/* 2621:     */   public void lockInsertHyperlinks(boolean enabled)
/* 2622:     */   {
/* 2623:3731 */     safeGetProtectionField().setInsertHyperlinks(enabled);
/* 2624:     */   }
/* 2625:     */   
/* 2626:     */   public void lockInsertRows(boolean enabled)
/* 2627:     */   {
/* 2628:3740 */     safeGetProtectionField().setInsertRows(enabled);
/* 2629:     */   }
/* 2630:     */   
/* 2631:     */   public void lockPivotTables(boolean enabled)
/* 2632:     */   {
/* 2633:3749 */     safeGetProtectionField().setPivotTables(enabled);
/* 2634:     */   }
/* 2635:     */   
/* 2636:     */   public void lockSort(boolean enabled)
/* 2637:     */   {
/* 2638:3758 */     safeGetProtectionField().setSort(enabled);
/* 2639:     */   }
/* 2640:     */   
/* 2641:     */   public void lockObjects(boolean enabled)
/* 2642:     */   {
/* 2643:3767 */     safeGetProtectionField().setObjects(enabled);
/* 2644:     */   }
/* 2645:     */   
/* 2646:     */   public void lockScenarios(boolean enabled)
/* 2647:     */   {
/* 2648:3776 */     safeGetProtectionField().setScenarios(enabled);
/* 2649:     */   }
/* 2650:     */   
/* 2651:     */   public void lockSelectLockedCells(boolean enabled)
/* 2652:     */   {
/* 2653:3785 */     safeGetProtectionField().setSelectLockedCells(enabled);
/* 2654:     */   }
/* 2655:     */   
/* 2656:     */   public void lockSelectUnlockedCells(boolean enabled)
/* 2657:     */   {
/* 2658:3794 */     safeGetProtectionField().setSelectUnlockedCells(enabled);
/* 2659:     */   }
/* 2660:     */   
/* 2661:     */   private CTSheetProtection safeGetProtectionField()
/* 2662:     */   {
/* 2663:3798 */     if (!isSheetProtectionEnabled()) {
/* 2664:3799 */       return this.worksheet.addNewSheetProtection();
/* 2665:     */     }
/* 2666:3801 */     return this.worksheet.getSheetProtection();
/* 2667:     */   }
/* 2668:     */   
/* 2669:     */   boolean isSheetProtectionEnabled()
/* 2670:     */   {
/* 2671:3805 */     return this.worksheet.isSetSheetProtection();
/* 2672:     */   }
/* 2673:     */   
/* 2674:     */   boolean isCellInArrayFormulaContext(XSSFCell cell)
/* 2675:     */   {
/* 2676:3809 */     for (CellRangeAddress range : this.arrayFormulas) {
/* 2677:3810 */       if (range.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
/* 2678:3811 */         return true;
/* 2679:     */       }
/* 2680:     */     }
/* 2681:3814 */     return false;
/* 2682:     */   }
/* 2683:     */   
/* 2684:     */   XSSFCell getFirstCellInArrayFormula(XSSFCell cell)
/* 2685:     */   {
/* 2686:3818 */     for (CellRangeAddress range : this.arrayFormulas) {
/* 2687:3819 */       if (range.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
/* 2688:3820 */         return getRow(range.getFirstRow()).getCell(range.getFirstColumn());
/* 2689:     */       }
/* 2690:     */     }
/* 2691:3823 */     return null;
/* 2692:     */   }
/* 2693:     */   
/* 2694:     */   private CellRange<XSSFCell> getCellRange(CellRangeAddress range)
/* 2695:     */   {
/* 2696:3830 */     int firstRow = range.getFirstRow();
/* 2697:3831 */     int firstColumn = range.getFirstColumn();
/* 2698:3832 */     int lastRow = range.getLastRow();
/* 2699:3833 */     int lastColumn = range.getLastColumn();
/* 2700:3834 */     int height = lastRow - firstRow + 1;
/* 2701:3835 */     int width = lastColumn - firstColumn + 1;
/* 2702:3836 */     List<XSSFCell> temp = new ArrayList(height * width);
/* 2703:3837 */     for (int rowIn = firstRow; rowIn <= lastRow; rowIn++) {
/* 2704:3838 */       for (int colIn = firstColumn; colIn <= lastColumn; colIn++)
/* 2705:     */       {
/* 2706:3839 */         XSSFRow row = getRow(rowIn);
/* 2707:3840 */         if (row == null) {
/* 2708:3841 */           row = createRow(rowIn);
/* 2709:     */         }
/* 2710:3843 */         XSSFCell cell = row.getCell(colIn);
/* 2711:3844 */         if (cell == null) {
/* 2712:3845 */           cell = row.createCell(colIn);
/* 2713:     */         }
/* 2714:3847 */         temp.add(cell);
/* 2715:     */       }
/* 2716:     */     }
/* 2717:3850 */     return SSCellRange.create(firstRow, firstColumn, height, width, temp, XSSFCell.class);
/* 2718:     */   }
/* 2719:     */   
/* 2720:     */   public CellRange<XSSFCell> setArrayFormula(String formula, CellRangeAddress range)
/* 2721:     */   {
/* 2722:3856 */     CellRange<XSSFCell> cr = getCellRange(range);
/* 2723:     */     
/* 2724:3858 */     XSSFCell mainArrayFormulaCell = (XSSFCell)cr.getTopLeftCell();
/* 2725:3859 */     mainArrayFormulaCell.setCellArrayFormula(formula, range);
/* 2726:3860 */     this.arrayFormulas.add(range);
/* 2727:3861 */     return cr;
/* 2728:     */   }
/* 2729:     */   
/* 2730:     */   public CellRange<XSSFCell> removeArrayFormula(Cell cell)
/* 2731:     */   {
/* 2732:3866 */     if (cell.getSheet() != this) {
/* 2733:3867 */       throw new IllegalArgumentException("Specified cell does not belong to this sheet.");
/* 2734:     */     }
/* 2735:3869 */     for (CellRangeAddress range : this.arrayFormulas) {
/* 2736:3870 */       if (range.isInRange(cell))
/* 2737:     */       {
/* 2738:3871 */         this.arrayFormulas.remove(range);
/* 2739:3872 */         CellRange<XSSFCell> cr = getCellRange(range);
/* 2740:3873 */         for (XSSFCell c : cr) {
/* 2741:3874 */           c.setCellType(CellType.BLANK);
/* 2742:     */         }
/* 2743:3876 */         return cr;
/* 2744:     */       }
/* 2745:     */     }
/* 2746:3879 */     String ref = ((XSSFCell)cell).getCTCell().getR();
/* 2747:3880 */     throw new IllegalArgumentException("Cell " + ref + " is not part of an array formula.");
/* 2748:     */   }
/* 2749:     */   
/* 2750:     */   public DataValidationHelper getDataValidationHelper()
/* 2751:     */   {
/* 2752:3886 */     return this.dataValidationHelper;
/* 2753:     */   }
/* 2754:     */   
/* 2755:     */   public List<XSSFDataValidation> getDataValidations()
/* 2756:     */   {
/* 2757:3891 */     List<XSSFDataValidation> xssfValidations = new ArrayList();
/* 2758:3892 */     CTDataValidations dataValidations = this.worksheet.getDataValidations();
/* 2759:3893 */     if ((dataValidations != null) && (dataValidations.getCount() > 0L)) {
/* 2760:3894 */       for (CTDataValidation ctDataValidation : dataValidations.getDataValidationArray())
/* 2761:     */       {
/* 2762:3895 */         CellRangeAddressList addressList = new CellRangeAddressList();
/* 2763:     */         
/* 2764:     */ 
/* 2765:3898 */         List<String> sqref = ctDataValidation.getSqref();
/* 2766:3899 */         for (String stRef : sqref)
/* 2767:     */         {
/* 2768:3900 */           String[] regions = stRef.split(" ");
/* 2769:3901 */           for (String region : regions)
/* 2770:     */           {
/* 2771:3902 */             String[] parts = region.split(":");
/* 2772:3903 */             CellReference begin = new CellReference(parts[0]);
/* 2773:3904 */             CellReference end = parts.length > 1 ? new CellReference(parts[1]) : begin;
/* 2774:3905 */             CellRangeAddress cellRangeAddress = new CellRangeAddress(begin.getRow(), end.getRow(), begin.getCol(), end.getCol());
/* 2775:3906 */             addressList.addCellRangeAddress(cellRangeAddress);
/* 2776:     */           }
/* 2777:     */         }
/* 2778:3909 */         XSSFDataValidation xssfDataValidation = new XSSFDataValidation(addressList, ctDataValidation);
/* 2779:3910 */         xssfValidations.add(xssfDataValidation);
/* 2780:     */       }
/* 2781:     */     }
/* 2782:3913 */     return xssfValidations;
/* 2783:     */   }
/* 2784:     */   
/* 2785:     */   public void addValidationData(DataValidation dataValidation)
/* 2786:     */   {
/* 2787:3918 */     XSSFDataValidation xssfDataValidation = (XSSFDataValidation)dataValidation;
/* 2788:3919 */     CTDataValidations dataValidations = this.worksheet.getDataValidations();
/* 2789:3920 */     if (dataValidations == null) {
/* 2790:3921 */       dataValidations = this.worksheet.addNewDataValidations();
/* 2791:     */     }
/* 2792:3923 */     int currentCount = dataValidations.sizeOfDataValidationArray();
/* 2793:3924 */     CTDataValidation newval = dataValidations.addNewDataValidation();
/* 2794:3925 */     newval.set(xssfDataValidation.getCtDdataValidation());
/* 2795:3926 */     dataValidations.setCount(currentCount + 1);
/* 2796:     */   }
/* 2797:     */   
/* 2798:     */   public XSSFAutoFilter setAutoFilter(CellRangeAddress range)
/* 2799:     */   {
/* 2800:3932 */     CTAutoFilter af = this.worksheet.getAutoFilter();
/* 2801:3933 */     if (af == null) {
/* 2802:3934 */       af = this.worksheet.addNewAutoFilter();
/* 2803:     */     }
/* 2804:3937 */     CellRangeAddress norm = new CellRangeAddress(range.getFirstRow(), range.getLastRow(), range.getFirstColumn(), range.getLastColumn());
/* 2805:     */     
/* 2806:3939 */     String ref = norm.formatAsString();
/* 2807:3940 */     af.setRef(ref);
/* 2808:     */     
/* 2809:3942 */     XSSFWorkbook wb = getWorkbook();
/* 2810:3943 */     int sheetIndex = getWorkbook().getSheetIndex(this);
/* 2811:3944 */     XSSFName name = wb.getBuiltInName("_xlnm._FilterDatabase", sheetIndex);
/* 2812:3945 */     if (name == null) {
/* 2813:3946 */       name = wb.createBuiltInName("_xlnm._FilterDatabase", sheetIndex);
/* 2814:     */     }
/* 2815:3949 */     name.getCTName().setHidden(true);
/* 2816:3950 */     CellReference r1 = new CellReference(getSheetName(), range.getFirstRow(), range.getFirstColumn(), true, true);
/* 2817:3951 */     CellReference r2 = new CellReference(null, range.getLastRow(), range.getLastColumn(), true, true);
/* 2818:3952 */     String fmla = r1.formatAsString() + ":" + r2.formatAsString();
/* 2819:3953 */     name.setRefersToFormula(fmla);
/* 2820:     */     
/* 2821:3955 */     return new XSSFAutoFilter(this);
/* 2822:     */   }
/* 2823:     */   
/* 2824:     */   public XSSFTable createTable()
/* 2825:     */   {
/* 2826:3962 */     if (!this.worksheet.isSetTableParts()) {
/* 2827:3963 */       this.worksheet.addNewTableParts();
/* 2828:     */     }
/* 2829:3966 */     CTTableParts tblParts = this.worksheet.getTableParts();
/* 2830:3967 */     CTTablePart tbl = tblParts.addNewTablePart();
/* 2831:     */     
/* 2832:     */ 
/* 2833:     */ 
/* 2834:3971 */     int tableNumber = getPackagePart().getPackage().getPartsByContentType(XSSFRelation.TABLE.getContentType()).size() + 1;
/* 2835:3972 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.TABLE, XSSFFactory.getInstance(), tableNumber, false);
/* 2836:3973 */     XSSFTable table = (XSSFTable)rp.getDocumentPart();
/* 2837:3974 */     tbl.setId(rp.getRelationship().getId());
/* 2838:3975 */     table.getCTTable().setId(tableNumber);
/* 2839:     */     
/* 2840:3977 */     this.tables.put(tbl.getId(), table);
/* 2841:     */     
/* 2842:3979 */     return table;
/* 2843:     */   }
/* 2844:     */   
/* 2845:     */   public List<XSSFTable> getTables()
/* 2846:     */   {
/* 2847:3986 */     return new ArrayList(this.tables.values());
/* 2848:     */   }
/* 2849:     */   
/* 2850:     */   public void removeTable(XSSFTable t)
/* 2851:     */   {
/* 2852:3994 */     long id = t.getCTTable().getId();
/* 2853:3995 */     Entry<String, XSSFTable> toDelete = null;
/* 2854:3997 */     for (Entry<String, XSSFTable> entry : this.tables.entrySet()) {
/* 2855:3998 */       if (((XSSFTable)entry.getValue()).getCTTable().getId() == id) {
/* 2856:3998 */         toDelete = entry;
/* 2857:     */       }
/* 2858:     */     }
/* 2859:4000 */     if (toDelete != null)
/* 2860:     */     {
/* 2861:4001 */       removeRelation(getRelationById((String)toDelete.getKey()), true);
/* 2862:4002 */       this.tables.remove(toDelete.getKey());
/* 2863:4003 */       ((XSSFTable)toDelete.getValue()).onTableDelete();
/* 2864:     */     }
/* 2865:     */   }
/* 2866:     */   
/* 2867:     */   public XSSFSheetConditionalFormatting getSheetConditionalFormatting()
/* 2868:     */   {
/* 2869:4009 */     return new XSSFSheetConditionalFormatting(this);
/* 2870:     */   }
/* 2871:     */   
/* 2872:     */   public XSSFColor getTabColor()
/* 2873:     */   {
/* 2874:4019 */     CTSheetPr pr = this.worksheet.getSheetPr();
/* 2875:4020 */     if (pr == null) {
/* 2876:4021 */       pr = this.worksheet.addNewSheetPr();
/* 2877:     */     }
/* 2878:4023 */     if (!pr.isSetTabColor()) {
/* 2879:4024 */       return null;
/* 2880:     */     }
/* 2881:4026 */     return new XSSFColor(pr.getTabColor(), getWorkbook().getStylesSource().getIndexedColors());
/* 2882:     */   }
/* 2883:     */   
/* 2884:     */   public void setTabColor(XSSFColor color)
/* 2885:     */   {
/* 2886:4035 */     CTSheetPr pr = this.worksheet.getSheetPr();
/* 2887:4036 */     if (pr == null) {
/* 2888:4037 */       pr = this.worksheet.addNewSheetPr();
/* 2889:     */     }
/* 2890:4039 */     pr.setTabColor(color.getCTColor());
/* 2891:     */   }
/* 2892:     */   
/* 2893:     */   public CellRangeAddress getRepeatingRows()
/* 2894:     */   {
/* 2895:4044 */     return getRepeatingRowsOrColums(true);
/* 2896:     */   }
/* 2897:     */   
/* 2898:     */   public CellRangeAddress getRepeatingColumns()
/* 2899:     */   {
/* 2900:4050 */     return getRepeatingRowsOrColums(false);
/* 2901:     */   }
/* 2902:     */   
/* 2903:     */   public void setRepeatingRows(CellRangeAddress rowRangeRef)
/* 2904:     */   {
/* 2905:4055 */     CellRangeAddress columnRangeRef = getRepeatingColumns();
/* 2906:4056 */     setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
/* 2907:     */   }
/* 2908:     */   
/* 2909:     */   public void setRepeatingColumns(CellRangeAddress columnRangeRef)
/* 2910:     */   {
/* 2911:4062 */     CellRangeAddress rowRangeRef = getRepeatingRows();
/* 2912:4063 */     setRepeatingRowsAndColumns(rowRangeRef, columnRangeRef);
/* 2913:     */   }
/* 2914:     */   
/* 2915:     */   private void setRepeatingRowsAndColumns(CellRangeAddress rowDef, CellRangeAddress colDef)
/* 2916:     */   {
/* 2917:4069 */     int col1 = -1;
/* 2918:4070 */     int col2 = -1;
/* 2919:4071 */     int row1 = -1;
/* 2920:4072 */     int row2 = -1;
/* 2921:4074 */     if (rowDef != null)
/* 2922:     */     {
/* 2923:4075 */       row1 = rowDef.getFirstRow();
/* 2924:4076 */       row2 = rowDef.getLastRow();
/* 2925:4077 */       if (((row1 == -1) && (row2 != -1)) || (row1 < -1) || (row2 < -1) || (row1 > row2)) {
/* 2926:4079 */         throw new IllegalArgumentException("Invalid row range specification");
/* 2927:     */       }
/* 2928:     */     }
/* 2929:4082 */     if (colDef != null)
/* 2930:     */     {
/* 2931:4083 */       col1 = colDef.getFirstColumn();
/* 2932:4084 */       col2 = colDef.getLastColumn();
/* 2933:4085 */       if (((col1 == -1) && (col2 != -1)) || (col1 < -1) || (col2 < -1) || (col1 > col2)) {
/* 2934:4087 */         throw new IllegalArgumentException("Invalid column range specification");
/* 2935:     */       }
/* 2936:     */     }
/* 2937:4092 */     int sheetIndex = getWorkbook().getSheetIndex(this);
/* 2938:     */     
/* 2939:4094 */     boolean removeAll = (rowDef == null) && (colDef == null);
/* 2940:     */     
/* 2941:4096 */     XSSFName name = getWorkbook().getBuiltInName("_xlnm.Print_Titles", sheetIndex);
/* 2942:4098 */     if (removeAll)
/* 2943:     */     {
/* 2944:4099 */       if (name != null) {
/* 2945:4100 */         getWorkbook().removeName(name);
/* 2946:     */       }
/* 2947:4102 */       return;
/* 2948:     */     }
/* 2949:4104 */     if (name == null) {
/* 2950:4105 */       name = getWorkbook().createBuiltInName("_xlnm.Print_Titles", sheetIndex);
/* 2951:     */     }
/* 2952:4109 */     String reference = getReferenceBuiltInRecord(name.getSheetName(), col1, col2, row1, row2);
/* 2953:     */     
/* 2954:4111 */     name.setRefersToFormula(reference);
/* 2955:4116 */     if ((!this.worksheet.isSetPageSetup()) || (!this.worksheet.isSetPageMargins())) {
/* 2956:4120 */       getPrintSetup().setValidSettings(false);
/* 2957:     */     }
/* 2958:     */   }
/* 2959:     */   
/* 2960:     */   private static String getReferenceBuiltInRecord(String sheetName, int startC, int endC, int startR, int endR)
/* 2961:     */   {
/* 2962:4129 */     CellReference colRef = new CellReference(sheetName, 0, startC, true, true);
/* 2963:     */     
/* 2964:4131 */     CellReference colRef2 = new CellReference(sheetName, 0, endC, true, true);
/* 2965:     */     
/* 2966:4133 */     CellReference rowRef = new CellReference(sheetName, startR, 0, true, true);
/* 2967:     */     
/* 2968:4135 */     CellReference rowRef2 = new CellReference(sheetName, endR, 0, true, true);
/* 2969:     */     
/* 2970:     */ 
/* 2971:4138 */     String escapedName = SheetNameFormatter.format(sheetName);
/* 2972:     */     
/* 2973:4140 */     String c = "";
/* 2974:4141 */     String r = "";
/* 2975:4143 */     if ((startC != -1) || (endC != -1))
/* 2976:     */     {
/* 2977:4144 */       String col1 = colRef.getCellRefParts()[2];
/* 2978:4145 */       String col2 = colRef2.getCellRefParts()[2];
/* 2979:4146 */       c = escapedName + "!$" + col1 + ":$" + col2;
/* 2980:     */     }
/* 2981:4149 */     if ((startR != -1) || (endR != -1))
/* 2982:     */     {
/* 2983:4150 */       String row1 = rowRef.getCellRefParts()[1];
/* 2984:4151 */       String row2 = rowRef2.getCellRefParts()[1];
/* 2985:4152 */       if ((!row1.equals("0")) && (!row2.equals("0"))) {
/* 2986:4153 */         r = escapedName + "!$" + row1 + ":$" + row2;
/* 2987:     */       }
/* 2988:     */     }
/* 2989:4157 */     StringBuilder rng = new StringBuilder();
/* 2990:4158 */     rng.append(c);
/* 2991:4159 */     if ((rng.length() > 0) && (r.length() > 0)) {
/* 2992:4160 */       rng.append(',');
/* 2993:     */     }
/* 2994:4162 */     rng.append(r);
/* 2995:4163 */     return rng.toString();
/* 2996:     */   }
/* 2997:     */   
/* 2998:     */   private CellRangeAddress getRepeatingRowsOrColums(boolean rows)
/* 2999:     */   {
/* 3000:4168 */     int sheetIndex = getWorkbook().getSheetIndex(this);
/* 3001:4169 */     XSSFName name = getWorkbook().getBuiltInName("_xlnm.Print_Titles", sheetIndex);
/* 3002:4171 */     if (name == null) {
/* 3003:4172 */       return null;
/* 3004:     */     }
/* 3005:4174 */     String refStr = name.getRefersToFormula();
/* 3006:4175 */     if (refStr == null) {
/* 3007:4176 */       return null;
/* 3008:     */     }
/* 3009:4178 */     String[] parts = refStr.split(",");
/* 3010:4179 */     int maxRowIndex = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
/* 3011:4180 */     int maxColIndex = SpreadsheetVersion.EXCEL2007.getLastColumnIndex();
/* 3012:4181 */     for (String part : parts)
/* 3013:     */     {
/* 3014:4182 */       CellRangeAddress range = CellRangeAddress.valueOf(part);
/* 3015:4183 */       if (((range.getFirstColumn() == 0) && (range.getLastColumn() == maxColIndex)) || ((range.getFirstColumn() == -1) && (range.getLastColumn() == -1)))
/* 3016:     */       {
/* 3017:4187 */         if (rows) {
/* 3018:4188 */           return range;
/* 3019:     */         }
/* 3020:     */       }
/* 3021:4190 */       else if (((range.getFirstRow() == 0) && (range.getLastRow() == maxRowIndex)) || ((range.getFirstRow() == -1) && (range.getLastRow() == -1))) {
/* 3022:4194 */         if (!rows) {
/* 3023:4195 */           return range;
/* 3024:     */         }
/* 3025:     */       }
/* 3026:     */     }
/* 3027:4199 */     return null;
/* 3028:     */   }
/* 3029:     */   
/* 3030:     */   private XSSFPivotTable createPivotTable()
/* 3031:     */   {
/* 3032:4209 */     XSSFWorkbook wb = getWorkbook();
/* 3033:4210 */     List<XSSFPivotTable> pivotTables = wb.getPivotTables();
/* 3034:4211 */     int tableId = getWorkbook().getPivotTables().size() + 1;
/* 3035:     */     
/* 3036:4213 */     XSSFPivotTable pivotTable = (XSSFPivotTable)createRelationship(XSSFRelation.PIVOT_TABLE, XSSFFactory.getInstance(), tableId);
/* 3037:     */     
/* 3038:4215 */     pivotTable.setParentSheet(this);
/* 3039:4216 */     pivotTables.add(pivotTable);
/* 3040:4217 */     XSSFWorkbook workbook = getWorkbook();
/* 3041:     */     
/* 3042:     */ 
/* 3043:4220 */     XSSFPivotCacheDefinition pivotCacheDefinition = (XSSFPivotCacheDefinition)workbook.createRelationship(XSSFRelation.PIVOT_CACHE_DEFINITION, XSSFFactory.getInstance(), tableId);
/* 3044:     */     
/* 3045:4222 */     String rId = workbook.getRelationId(pivotCacheDefinition);
/* 3046:     */     
/* 3047:4224 */     PackagePart pivotPackagePart = pivotTable.getPackagePart();
/* 3048:4225 */     pivotPackagePart.addRelationship(pivotCacheDefinition.getPackagePart().getPartName(), TargetMode.INTERNAL, XSSFRelation.PIVOT_CACHE_DEFINITION.getRelation());
/* 3049:     */     
/* 3050:     */ 
/* 3051:4228 */     pivotTable.setPivotCacheDefinition(pivotCacheDefinition);
/* 3052:     */     
/* 3053:     */ 
/* 3054:4231 */     pivotTable.setPivotCache(new XSSFPivotCache(workbook.addPivotCache(rId)));
/* 3055:     */     
/* 3056:     */ 
/* 3057:4234 */     XSSFPivotCacheRecords pivotCacheRecords = (XSSFPivotCacheRecords)pivotCacheDefinition.createRelationship(XSSFRelation.PIVOT_CACHE_RECORDS, XSSFFactory.getInstance(), tableId);
/* 3058:     */     
/* 3059:     */ 
/* 3060:     */ 
/* 3061:4238 */     pivotTable.getPivotCacheDefinition().getCTPivotCacheDefinition().setId(pivotCacheDefinition.getRelationId(pivotCacheRecords));
/* 3062:     */     
/* 3063:4240 */     wb.setPivotTables(pivotTables);
/* 3064:     */     
/* 3065:4242 */     return pivotTable;
/* 3066:     */   }
/* 3067:     */   
/* 3068:     */   public XSSFPivotTable createPivotTable(final AreaReference source, CellReference position, Sheet sourceSheet)
/* 3069:     */   {
/* 3070:4256 */     String sourceSheetName = source.getFirstCell().getSheetName();
/* 3071:4257 */     if ((sourceSheetName != null) && (!sourceSheetName.equalsIgnoreCase(sourceSheet.getSheetName()))) {
/* 3072:4258 */       throw new IllegalArgumentException("The area is referenced in another sheet than the defined source sheet " + sourceSheet.getSheetName() + ".");
/* 3073:     */     }
/* 3074:4262 */     createPivotTable(position, sourceSheet, new XSSFPivotTable.PivotTableReferenceConfigurator()
/* 3075:     */     {
/* 3076:     */       public void configureReference(CTWorksheetSource wsSource)
/* 3077:     */       {
/* 3078:4265 */         String[] firstCell = source.getFirstCell().getCellRefParts();
/* 3079:4266 */         String firstRow = firstCell[1];
/* 3080:4267 */         String firstCol = firstCell[2];
/* 3081:4268 */         String[] lastCell = source.getLastCell().getCellRefParts();
/* 3082:4269 */         String lastRow = lastCell[1];
/* 3083:4270 */         String lastCol = lastCell[2];
/* 3084:4271 */         String ref = firstCol + firstRow + ':' + lastCol + lastRow;
/* 3085:4272 */         wsSource.setRef(ref);
/* 3086:     */       }
/* 3087:     */     });
/* 3088:     */   }
/* 3089:     */   
/* 3090:     */   private XSSFPivotTable createPivotTable(CellReference position, Sheet sourceSheet, XSSFPivotTable.PivotTableReferenceConfigurator refConfig)
/* 3091:     */   {
/* 3092:4288 */     XSSFPivotTable pivotTable = createPivotTable();
/* 3093:     */     
/* 3094:4290 */     pivotTable.setDefaultPivotTableDefinition();
/* 3095:     */     
/* 3096:     */ 
/* 3097:4293 */     pivotTable.createSourceReferences(position, sourceSheet, refConfig);
/* 3098:     */     
/* 3099:     */ 
/* 3100:4296 */     pivotTable.getPivotCacheDefinition().createCacheFields(sourceSheet);
/* 3101:4297 */     pivotTable.createDefaultDataColumns();
/* 3102:     */     
/* 3103:4299 */     return pivotTable;
/* 3104:     */   }
/* 3105:     */   
/* 3106:     */   public XSSFPivotTable createPivotTable(AreaReference source, CellReference position)
/* 3107:     */   {
/* 3108:4311 */     String sourceSheetName = source.getFirstCell().getSheetName();
/* 3109:4312 */     if ((sourceSheetName != null) && (!sourceSheetName.equalsIgnoreCase(getSheetName())))
/* 3110:     */     {
/* 3111:4313 */       XSSFSheet sourceSheet = getWorkbook().getSheet(sourceSheetName);
/* 3112:4314 */       return createPivotTable(source, position, sourceSheet);
/* 3113:     */     }
/* 3114:4316 */     return createPivotTable(source, position, this);
/* 3115:     */   }
/* 3116:     */   
/* 3117:     */   public XSSFPivotTable createPivotTable(final Name source, CellReference position, Sheet sourceSheet)
/* 3118:     */   {
/* 3119:4330 */     if ((source.getSheetName() != null) && (!source.getSheetName().equals(sourceSheet.getSheetName()))) {
/* 3120:4331 */       throw new IllegalArgumentException("The named range references another sheet than the defined source sheet " + sourceSheet.getSheetName() + ".");
/* 3121:     */     }
/* 3122:4335 */     createPivotTable(position, sourceSheet, new XSSFPivotTable.PivotTableReferenceConfigurator()
/* 3123:     */     {
/* 3124:     */       public void configureReference(CTWorksheetSource wsSource)
/* 3125:     */       {
/* 3126:4338 */         wsSource.setName(source.getNameName());
/* 3127:     */       }
/* 3128:     */     });
/* 3129:     */   }
/* 3130:     */   
/* 3131:     */   public XSSFPivotTable createPivotTable(Name source, CellReference position)
/* 3132:     */   {
/* 3133:4352 */     return createPivotTable(source, position, getWorkbook().getSheet(source.getSheetName()));
/* 3134:     */   }
/* 3135:     */   
/* 3136:     */   public XSSFPivotTable createPivotTable(final Table source, CellReference position)
/* 3137:     */   {
/* 3138:4364 */     createPivotTable(position, getWorkbook().getSheet(source.getSheetName()), new XSSFPivotTable.PivotTableReferenceConfigurator()
/* 3139:     */     {
/* 3140:     */       public void configureReference(CTWorksheetSource wsSource)
/* 3141:     */       {
/* 3142:4367 */         wsSource.setName(source.getName());
/* 3143:     */       }
/* 3144:     */     });
/* 3145:     */   }
/* 3146:     */   
/* 3147:     */   public List<XSSFPivotTable> getPivotTables()
/* 3148:     */   {
/* 3149:4377 */     List<XSSFPivotTable> tables = new ArrayList();
/* 3150:4378 */     for (XSSFPivotTable table : getWorkbook().getPivotTables()) {
/* 3151:4379 */       if (table.getParent() == this) {
/* 3152:4380 */         tables.add(table);
/* 3153:     */       }
/* 3154:     */     }
/* 3155:4383 */     return tables;
/* 3156:     */   }
/* 3157:     */   
/* 3158:     */   public int getColumnOutlineLevel(int columnIndex)
/* 3159:     */   {
/* 3160:4388 */     CTCol col = this.columnHelper.getColumn(columnIndex, false);
/* 3161:4389 */     if (col == null) {
/* 3162:4390 */       return 0;
/* 3163:     */     }
/* 3164:4392 */     return col.getOutlineLevel();
/* 3165:     */   }
/* 3166:     */   
/* 3167:     */   public void addIgnoredErrors(CellReference cell, IgnoredErrorType... ignoredErrorTypes)
/* 3168:     */   {
/* 3169:4403 */     addIgnoredErrors(cell.formatAsString(), ignoredErrorTypes);
/* 3170:     */   }
/* 3171:     */   
/* 3172:     */   public void addIgnoredErrors(CellRangeAddress region, IgnoredErrorType... ignoredErrorTypes)
/* 3173:     */   {
/* 3174:4413 */     region.validate(SpreadsheetVersion.EXCEL2007);
/* 3175:4414 */     addIgnoredErrors(region.formatAsString(), ignoredErrorTypes);
/* 3176:     */   }
/* 3177:     */   
/* 3178:     */   public Map<IgnoredErrorType, Set<CellRangeAddress>> getIgnoredErrors()
/* 3179:     */   {
/* 3180:4424 */     Map<IgnoredErrorType, Set<CellRangeAddress>> result = new LinkedHashMap();
/* 3181:     */     Iterator i$;
/* 3182:4425 */     if (this.worksheet.isSetIgnoredErrors()) {
/* 3183:4426 */       for (i$ = this.worksheet.getIgnoredErrors().getIgnoredErrorList().iterator(); i$.hasNext();)
/* 3184:     */       {
/* 3185:4426 */         err = (CTIgnoredError)i$.next();
/* 3186:4427 */         for (i$ = XSSFIgnoredErrorHelper.getErrorTypes(err).iterator(); i$.hasNext();)
/* 3187:     */         {
/* 3188:4427 */           errType = (IgnoredErrorType)i$.next();
/* 3189:4428 */           if (!result.containsKey(errType)) {
/* 3190:4429 */             result.put(errType, new LinkedHashSet());
/* 3191:     */           }
/* 3192:4431 */           for (Object ref : err.getSqref()) {
/* 3193:4432 */             ((Set)result.get(errType)).add(CellRangeAddress.valueOf(ref.toString()));
/* 3194:     */           }
/* 3195:     */         }
/* 3196:     */       }
/* 3197:     */     }
/* 3198:     */     CTIgnoredError err;
/* 3199:     */     Iterator i$;
/* 3200:     */     IgnoredErrorType errType;
/* 3201:4437 */     return result;
/* 3202:     */   }
/* 3203:     */   
/* 3204:     */   private void addIgnoredErrors(String ref, IgnoredErrorType... ignoredErrorTypes)
/* 3205:     */   {
/* 3206:4441 */     CTIgnoredErrors ctIgnoredErrors = this.worksheet.isSetIgnoredErrors() ? this.worksheet.getIgnoredErrors() : this.worksheet.addNewIgnoredErrors();
/* 3207:4442 */     CTIgnoredError ctIgnoredError = ctIgnoredErrors.addNewIgnoredError();
/* 3208:4443 */     XSSFIgnoredErrorHelper.addIgnoredErrors(ctIgnoredError, ref, ignoredErrorTypes);
/* 3209:     */   }
/* 3210:     */   
/* 3211:     */   protected void onSheetDelete()
/* 3212:     */   {
/* 3213:4450 */     for (POIXMLDocumentPart.RelationPart part : getRelationParts()) {
/* 3214:4451 */       if ((part.getDocumentPart() instanceof XSSFTable)) {
/* 3215:4453 */         removeTable((XSSFTable)part.getDocumentPart());
/* 3216:     */       } else {
/* 3217:4456 */         removeRelation(part.getDocumentPart(), true);
/* 3218:     */       }
/* 3219:     */     }
/* 3220:     */   }
/* 3221:     */   
/* 3222:     */   protected CTOleObject readOleObject(long shapeId)
/* 3223:     */   {
/* 3224:4467 */     if (!getCTWorksheet().isSetOleObjects()) {
/* 3225:4468 */       return null;
/* 3226:     */     }
/* 3227:4472 */     String xquery = "declare namespace p='http://schemas.openxmlformats.org/spreadsheetml/2006/main' .//p:oleObject";
/* 3228:4473 */     XmlCursor cur = getCTWorksheet().getOleObjects().newCursor();
/* 3229:     */     try
/* 3230:     */     {
/* 3231:4475 */       cur.selectPath(xquery);
/* 3232:4476 */       CTOleObject coo = null;
/* 3233:     */       String sId;
/* 3234:     */       label349:
/* 3235:4477 */       while (cur.toNextSelection())
/* 3236:     */       {
/* 3237:4478 */         sId = cur.getAttributeText(new QName(null, "shapeId"));
/* 3238:4479 */         if ((sId != null) && (Long.parseLong(sId) == shapeId))
/* 3239:     */         {
/* 3240:4483 */           XmlObject xObj = cur.getObject();
/* 3241:4484 */           if ((xObj instanceof CTOleObject))
/* 3242:     */           {
/* 3243:4486 */             coo = (CTOleObject)xObj;
/* 3244:     */           }
/* 3245:     */           else
/* 3246:     */           {
/* 3247:4488 */             XMLStreamReader reader = cur.newXMLStreamReader();
/* 3248:     */             try
/* 3249:     */             {
/* 3250:4490 */               CTOleObjects coos = CTOleObjects.Factory.parse(reader);
/* 3251:4491 */               if (coos.sizeOfOleObjectArray() == 0)
/* 3252:     */               {
/* 3253:     */                 try {}catch (XMLStreamException e)
/* 3254:     */                 {
/* 3255:4501 */                   logger.log(3, new Object[] { "can't close reader", e });
/* 3256:     */                 }
/* 3257:4502 */                 continue;
/* 3258:     */               }
/* 3259:4494 */               coo = coos.getOleObjectArray(0);
/* 3260:     */               try {}catch (XMLStreamException e)
/* 3261:     */               {
/* 3262:4501 */                 logger.log(3, new Object[] { "can't close reader", e });
/* 3263:     */               }
/* 3264:4508 */               if (!cur.toChild("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "objectPr")) {
/* 3265:     */                 break label349;
/* 3266:     */               }
/* 3267:     */             }
/* 3268:     */             catch (XmlException e)
/* 3269:     */             {
/* 3270:4496 */               logger.log(3, new Object[] { "can't parse CTOleObjects", e });
/* 3271:     */             }
/* 3272:     */             finally
/* 3273:     */             {
/* 3274:     */               try {}catch (XMLStreamException e)
/* 3275:     */               {
/* 3276:4501 */                 logger.log(3, new Object[] { "can't close reader", e });
/* 3277:     */               }
/* 3278:     */             }
/* 3279:     */           }
/* 3280:4509 */           break;
/* 3281:     */         }
/* 3282:     */       }
/* 3283:4512 */       return coo == null ? null : coo;
/* 3284:     */     }
/* 3285:     */     finally
/* 3286:     */     {
/* 3287:4514 */       cur.dispose();
/* 3288:     */     }
/* 3289:     */   }
/* 3290:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFSheet

 * JD-Core Version:    0.7.0.1

 */