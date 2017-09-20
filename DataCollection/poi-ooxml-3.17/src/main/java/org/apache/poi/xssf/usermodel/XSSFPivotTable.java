/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.POIXMLTypeLoader;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  14:    */ import org.apache.poi.ss.usermodel.Cell;
/*  15:    */ import org.apache.poi.ss.usermodel.CellType;
/*  16:    */ import org.apache.poi.ss.usermodel.DataConsolidateFunction;
/*  17:    */ import org.apache.poi.ss.usermodel.Row;
/*  18:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  19:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  20:    */ import org.apache.poi.ss.util.AreaReference;
/*  21:    */ import org.apache.poi.ss.util.CellReference;
/*  22:    */ import org.apache.poi.util.Internal;
/*  23:    */ import org.apache.xmlbeans.SchemaType;
/*  24:    */ import org.apache.xmlbeans.XmlException;
/*  25:    */ import org.apache.xmlbeans.XmlOptions;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource;
/*  27:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields;
/*  28:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataField;
/*  29:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields;
/*  30:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTField;
/*  31:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItem;
/*  32:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems;
/*  33:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation;
/*  34:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField;
/*  35:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields;
/*  36:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache;
/*  37:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition;
/*  38:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField;
/*  39:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField.Factory;
/*  40:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields;
/*  41:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition;
/*  42:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition.Factory;
/*  43:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle;
/*  44:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields;
/*  45:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheetSource;
/*  46:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis;
/*  47:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction.Enum;
/*  48:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STItemType;
/*  49:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;
/*  50:    */ 
/*  51:    */ public class XSSFPivotTable
/*  52:    */   extends POIXMLDocumentPart
/*  53:    */ {
/*  54:    */   protected static final short CREATED_VERSION = 3;
/*  55:    */   protected static final short MIN_REFRESHABLE_VERSION = 3;
/*  56:    */   protected static final short UPDATED_VERSION = 3;
/*  57:    */   private CTPivotTableDefinition pivotTableDefinition;
/*  58:    */   private XSSFPivotCacheDefinition pivotCacheDefinition;
/*  59:    */   private XSSFPivotCache pivotCache;
/*  60:    */   private XSSFPivotCacheRecords pivotCacheRecords;
/*  61:    */   private Sheet parentSheet;
/*  62:    */   private Sheet dataSheet;
/*  63:    */   
/*  64:    */   protected XSSFPivotTable()
/*  65:    */   {
/*  66: 80 */     this.pivotTableDefinition = CTPivotTableDefinition.Factory.newInstance();
/*  67: 81 */     this.pivotCache = new XSSFPivotCache();
/*  68: 82 */     this.pivotCacheDefinition = new XSSFPivotCacheDefinition();
/*  69: 83 */     this.pivotCacheRecords = new XSSFPivotCacheRecords();
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected XSSFPivotTable(PackagePart part)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75: 96 */     super(part);
/*  76: 97 */     readFrom(part.getInputStream());
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void readFrom(InputStream is)
/*  80:    */     throws IOException
/*  81:    */   {
/*  82:    */     try
/*  83:    */     {
/*  84:103 */       XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  85:    */       
/*  86:105 */       options.setLoadReplaceDocumentElement(null);
/*  87:106 */       this.pivotTableDefinition = CTPivotTableDefinition.Factory.parse(is, options);
/*  88:    */     }
/*  89:    */     catch (XmlException e)
/*  90:    */     {
/*  91:108 */       throw new IOException(e.getLocalizedMessage());
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setPivotCache(XSSFPivotCache pivotCache)
/*  96:    */   {
/*  97:114 */     this.pivotCache = pivotCache;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public XSSFPivotCache getPivotCache()
/* 101:    */   {
/* 102:119 */     return this.pivotCache;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Sheet getParentSheet()
/* 106:    */   {
/* 107:124 */     return this.parentSheet;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setParentSheet(XSSFSheet parentSheet)
/* 111:    */   {
/* 112:129 */     this.parentSheet = parentSheet;
/* 113:    */   }
/* 114:    */   
/* 115:    */   @Internal
/* 116:    */   public CTPivotTableDefinition getCTPivotTableDefinition()
/* 117:    */   {
/* 118:135 */     return this.pivotTableDefinition;
/* 119:    */   }
/* 120:    */   
/* 121:    */   @Internal
/* 122:    */   public void setCTPivotTableDefinition(CTPivotTableDefinition pivotTableDefinition)
/* 123:    */   {
/* 124:141 */     this.pivotTableDefinition = pivotTableDefinition;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public XSSFPivotCacheDefinition getPivotCacheDefinition()
/* 128:    */   {
/* 129:146 */     return this.pivotCacheDefinition;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setPivotCacheDefinition(XSSFPivotCacheDefinition pivotCacheDefinition)
/* 133:    */   {
/* 134:151 */     this.pivotCacheDefinition = pivotCacheDefinition;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public XSSFPivotCacheRecords getPivotCacheRecords()
/* 138:    */   {
/* 139:156 */     return this.pivotCacheRecords;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setPivotCacheRecords(XSSFPivotCacheRecords pivotCacheRecords)
/* 143:    */   {
/* 144:161 */     this.pivotCacheRecords = pivotCacheRecords;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public Sheet getDataSheet()
/* 148:    */   {
/* 149:166 */     return this.dataSheet;
/* 150:    */   }
/* 151:    */   
/* 152:    */   private void setDataSheet(Sheet dataSheet)
/* 153:    */   {
/* 154:171 */     this.dataSheet = dataSheet;
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected void commit()
/* 158:    */     throws IOException
/* 159:    */   {
/* 160:177 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 161:    */     
/* 162:179 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTPivotTableDefinition.type.getName().getNamespaceURI(), "pivotTableDefinition"));
/* 163:    */     
/* 164:181 */     PackagePart part = getPackagePart();
/* 165:182 */     OutputStream out = part.getOutputStream();
/* 166:183 */     this.pivotTableDefinition.save(out, xmlOptions);
/* 167:184 */     out.close();
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected void setDefaultPivotTableDefinition()
/* 171:    */   {
/* 172:193 */     this.pivotTableDefinition.setMultipleFieldFilters(false);
/* 173:    */     
/* 174:195 */     this.pivotTableDefinition.setIndent(0L);
/* 175:    */     
/* 176:197 */     this.pivotTableDefinition.setCreatedVersion((short)3);
/* 177:    */     
/* 178:199 */     this.pivotTableDefinition.setMinRefreshableVersion((short)3);
/* 179:    */     
/* 180:201 */     this.pivotTableDefinition.setUpdatedVersion((short)3);
/* 181:    */     
/* 182:203 */     this.pivotTableDefinition.setItemPrintTitles(true);
/* 183:    */     
/* 184:205 */     this.pivotTableDefinition.setUseAutoFormatting(true);
/* 185:206 */     this.pivotTableDefinition.setApplyNumberFormats(false);
/* 186:207 */     this.pivotTableDefinition.setApplyWidthHeightFormats(true);
/* 187:208 */     this.pivotTableDefinition.setApplyAlignmentFormats(false);
/* 188:209 */     this.pivotTableDefinition.setApplyPatternFormats(false);
/* 189:210 */     this.pivotTableDefinition.setApplyFontFormats(false);
/* 190:211 */     this.pivotTableDefinition.setApplyBorderFormats(false);
/* 191:212 */     this.pivotTableDefinition.setCacheId(this.pivotCache.getCTPivotCache().getCacheId());
/* 192:213 */     this.pivotTableDefinition.setName("PivotTable" + this.pivotTableDefinition.getCacheId());
/* 193:214 */     this.pivotTableDefinition.setDataCaption("Values");
/* 194:    */     
/* 195:    */ 
/* 196:217 */     CTPivotTableStyle style = this.pivotTableDefinition.addNewPivotTableStyleInfo();
/* 197:218 */     style.setName("PivotStyleLight16");
/* 198:219 */     style.setShowLastColumn(true);
/* 199:220 */     style.setShowColStripes(false);
/* 200:221 */     style.setShowRowStripes(false);
/* 201:222 */     style.setShowColHeaders(true);
/* 202:223 */     style.setShowRowHeaders(true);
/* 203:    */   }
/* 204:    */   
/* 205:    */   protected AreaReference getPivotArea()
/* 206:    */   {
/* 207:227 */     Workbook wb = getDataSheet().getWorkbook();
/* 208:228 */     AreaReference pivotArea = getPivotCacheDefinition().getPivotArea(wb);
/* 209:229 */     return pivotArea;
/* 210:    */   }
/* 211:    */   
/* 212:    */   private void checkColumnIndex(int columnIndex)
/* 213:    */     throws IndexOutOfBoundsException
/* 214:    */   {
/* 215:240 */     AreaReference pivotArea = getPivotArea();
/* 216:241 */     int size = pivotArea.getLastCell().getCol() - pivotArea.getFirstCell().getCol() + 1;
/* 217:243 */     if ((columnIndex < 0) || (columnIndex >= size)) {
/* 218:244 */       throw new IndexOutOfBoundsException("Column Index: " + columnIndex + ", Size: " + size);
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void addRowLabel(int columnIndex)
/* 223:    */   {
/* 224:255 */     checkColumnIndex(columnIndex);
/* 225:    */     
/* 226:257 */     AreaReference pivotArea = getPivotArea();
/* 227:258 */     int lastRowIndex = pivotArea.getLastCell().getRow() - pivotArea.getFirstCell().getRow();
/* 228:259 */     CTPivotFields pivotFields = this.pivotTableDefinition.getPivotFields();
/* 229:    */     
/* 230:261 */     CTPivotField pivotField = CTPivotField.Factory.newInstance();
/* 231:262 */     CTItems items = pivotField.addNewItems();
/* 232:    */     
/* 233:264 */     pivotField.setAxis(STAxis.AXIS_ROW);
/* 234:265 */     pivotField.setShowAll(false);
/* 235:266 */     for (int i = 0; i <= lastRowIndex; i++) {
/* 236:267 */       items.addNewItem().setT(STItemType.DEFAULT);
/* 237:    */     }
/* 238:269 */     items.setCount(items.sizeOfItemArray());
/* 239:270 */     pivotFields.setPivotFieldArray(columnIndex, pivotField);
/* 240:    */     CTRowFields rowFields;
/* 241:    */     CTRowFields rowFields;
/* 242:273 */     if (this.pivotTableDefinition.getRowFields() != null) {
/* 243:274 */       rowFields = this.pivotTableDefinition.getRowFields();
/* 244:    */     } else {
/* 245:276 */       rowFields = this.pivotTableDefinition.addNewRowFields();
/* 246:    */     }
/* 247:279 */     rowFields.addNewField().setX(columnIndex);
/* 248:280 */     rowFields.setCount(rowFields.sizeOfFieldArray());
/* 249:    */   }
/* 250:    */   
/* 251:    */   public List<Integer> getRowLabelColumns()
/* 252:    */   {
/* 253:285 */     if (this.pivotTableDefinition.getRowFields() != null)
/* 254:    */     {
/* 255:286 */       List<Integer> columnIndexes = new ArrayList();
/* 256:287 */       for (CTField f : this.pivotTableDefinition.getRowFields().getFieldArray()) {
/* 257:288 */         columnIndexes.add(Integer.valueOf(f.getX()));
/* 258:    */       }
/* 259:290 */       return columnIndexes;
/* 260:    */     }
/* 261:292 */     return Collections.emptyList();
/* 262:    */   }
/* 263:    */   
/* 264:    */   public void addColumnLabel(DataConsolidateFunction function, int columnIndex, String valueFieldName)
/* 265:    */   {
/* 266:307 */     checkColumnIndex(columnIndex);
/* 267:    */     
/* 268:309 */     addDataColumn(columnIndex, true);
/* 269:310 */     addDataField(function, columnIndex, valueFieldName);
/* 270:313 */     if (this.pivotTableDefinition.getDataFields().getCount() == 2L)
/* 271:    */     {
/* 272:    */       CTColFields colFields;
/* 273:    */       CTColFields colFields;
/* 274:315 */       if (this.pivotTableDefinition.getColFields() != null) {
/* 275:316 */         colFields = this.pivotTableDefinition.getColFields();
/* 276:    */       } else {
/* 277:318 */         colFields = this.pivotTableDefinition.addNewColFields();
/* 278:    */       }
/* 279:320 */       colFields.addNewField().setX(-2);
/* 280:321 */       colFields.setCount(colFields.sizeOfFieldArray());
/* 281:    */     }
/* 282:    */   }
/* 283:    */   
/* 284:    */   public void addColumnLabel(DataConsolidateFunction function, int columnIndex)
/* 285:    */   {
/* 286:335 */     addColumnLabel(function, columnIndex, function.getName());
/* 287:    */   }
/* 288:    */   
/* 289:    */   private void addDataField(DataConsolidateFunction function, int columnIndex, String valueFieldName)
/* 290:    */   {
/* 291:348 */     checkColumnIndex(columnIndex);
/* 292:    */     
/* 293:350 */     AreaReference pivotArea = getPivotArea();
/* 294:    */     CTDataFields dataFields;
/* 295:    */     CTDataFields dataFields;
/* 296:353 */     if (this.pivotTableDefinition.getDataFields() != null) {
/* 297:354 */       dataFields = this.pivotTableDefinition.getDataFields();
/* 298:    */     } else {
/* 299:356 */       dataFields = this.pivotTableDefinition.addNewDataFields();
/* 300:    */     }
/* 301:358 */     CTDataField dataField = dataFields.addNewDataField();
/* 302:359 */     dataField.setSubtotal(STDataConsolidateFunction.Enum.forInt(function.getValue()));
/* 303:360 */     Cell cell = getDataSheet().getRow(pivotArea.getFirstCell().getRow()).getCell(pivotArea.getFirstCell().getCol() + columnIndex);
/* 304:    */     
/* 305:362 */     cell.setCellType(CellType.STRING);
/* 306:363 */     dataField.setName(valueFieldName);
/* 307:364 */     dataField.setFld(columnIndex);
/* 308:365 */     dataFields.setCount(dataFields.sizeOfDataFieldArray());
/* 309:    */   }
/* 310:    */   
/* 311:    */   public void addDataColumn(int columnIndex, boolean isDataField)
/* 312:    */   {
/* 313:375 */     checkColumnIndex(columnIndex);
/* 314:    */     
/* 315:377 */     CTPivotFields pivotFields = this.pivotTableDefinition.getPivotFields();
/* 316:378 */     CTPivotField pivotField = CTPivotField.Factory.newInstance();
/* 317:    */     
/* 318:380 */     pivotField.setDataField(isDataField);
/* 319:381 */     pivotField.setShowAll(false);
/* 320:382 */     pivotFields.setPivotFieldArray(columnIndex, pivotField);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public void addReportFilter(int columnIndex)
/* 324:    */   {
/* 325:391 */     checkColumnIndex(columnIndex);
/* 326:    */     
/* 327:393 */     AreaReference pivotArea = getPivotArea();
/* 328:394 */     int lastRowIndex = pivotArea.getLastCell().getRow() - pivotArea.getFirstCell().getRow();
/* 329:    */     
/* 330:396 */     CTPivotFields pivotFields = this.pivotTableDefinition.getPivotFields();
/* 331:    */     
/* 332:398 */     CTPivotField pivotField = CTPivotField.Factory.newInstance();
/* 333:399 */     CTItems items = pivotField.addNewItems();
/* 334:    */     
/* 335:401 */     pivotField.setAxis(STAxis.AXIS_PAGE);
/* 336:402 */     pivotField.setShowAll(false);
/* 337:403 */     for (int i = 0; i <= lastRowIndex; i++) {
/* 338:404 */       items.addNewItem().setT(STItemType.DEFAULT);
/* 339:    */     }
/* 340:406 */     items.setCount(items.sizeOfItemArray());
/* 341:407 */     pivotFields.setPivotFieldArray(columnIndex, pivotField);
/* 342:    */     CTPageFields pageFields;
/* 343:410 */     if (this.pivotTableDefinition.getPageFields() != null)
/* 344:    */     {
/* 345:411 */       CTPageFields pageFields = this.pivotTableDefinition.getPageFields();
/* 346:    */       
/* 347:413 */       this.pivotTableDefinition.setMultipleFieldFilters(true);
/* 348:    */     }
/* 349:    */     else
/* 350:    */     {
/* 351:415 */       pageFields = this.pivotTableDefinition.addNewPageFields();
/* 352:    */     }
/* 353:417 */     CTPageField pageField = pageFields.addNewPageField();
/* 354:418 */     pageField.setHier(-1);
/* 355:419 */     pageField.setFld(columnIndex);
/* 356:    */     
/* 357:421 */     pageFields.setCount(pageFields.sizeOfPageFieldArray());
/* 358:422 */     this.pivotTableDefinition.getLocation().setColPageCount(pageFields.getCount());
/* 359:    */   }
/* 360:    */   
/* 361:    */   protected void createSourceReferences(CellReference position, Sheet sourceSheet, PivotTableReferenceConfigurator refConfig)
/* 362:    */   {
/* 363:435 */     AreaReference destination = new AreaReference(position, new CellReference(position.getRow() + 1, position.getCol() + 1), SpreadsheetVersion.EXCEL2007);
/* 364:    */     CTLocation location;
/* 365:439 */     if (this.pivotTableDefinition.getLocation() == null)
/* 366:    */     {
/* 367:440 */       CTLocation location = this.pivotTableDefinition.addNewLocation();
/* 368:441 */       location.setFirstDataCol(1L);
/* 369:442 */       location.setFirstDataRow(1L);
/* 370:443 */       location.setFirstHeaderRow(1L);
/* 371:    */     }
/* 372:    */     else
/* 373:    */     {
/* 374:445 */       location = this.pivotTableDefinition.getLocation();
/* 375:    */     }
/* 376:447 */     location.setRef(destination.formatAsString());
/* 377:448 */     this.pivotTableDefinition.setLocation(location);
/* 378:    */     
/* 379:    */ 
/* 380:451 */     CTPivotCacheDefinition cacheDef = getPivotCacheDefinition().getCTPivotCacheDefinition();
/* 381:452 */     CTCacheSource cacheSource = cacheDef.addNewCacheSource();
/* 382:453 */     cacheSource.setType(STSourceType.WORKSHEET);
/* 383:454 */     CTWorksheetSource worksheetSource = cacheSource.addNewWorksheetSource();
/* 384:455 */     worksheetSource.setSheet(sourceSheet.getSheetName());
/* 385:456 */     setDataSheet(sourceSheet);
/* 386:    */     
/* 387:458 */     refConfig.configureReference(worksheetSource);
/* 388:459 */     if ((worksheetSource.getName() == null) && (worksheetSource.getRef() == null)) {
/* 389:459 */       throw new IllegalArgumentException("Pivot table source area reference or name must be specified.");
/* 390:    */     }
/* 391:    */   }
/* 392:    */   
/* 393:    */   protected void createDefaultDataColumns()
/* 394:    */   {
/* 395:    */     CTPivotFields pivotFields;
/* 396:    */     CTPivotFields pivotFields;
/* 397:465 */     if (this.pivotTableDefinition.getPivotFields() != null) {
/* 398:466 */       pivotFields = this.pivotTableDefinition.getPivotFields();
/* 399:    */     } else {
/* 400:468 */       pivotFields = this.pivotTableDefinition.addNewPivotFields();
/* 401:    */     }
/* 402:470 */     AreaReference sourceArea = getPivotArea();
/* 403:471 */     int firstColumn = sourceArea.getFirstCell().getCol();
/* 404:472 */     int lastColumn = sourceArea.getLastCell().getCol();
/* 405:474 */     for (int i = firstColumn; i <= lastColumn; i++)
/* 406:    */     {
/* 407:475 */       CTPivotField pivotField = pivotFields.addNewPivotField();
/* 408:476 */       pivotField.setDataField(false);
/* 409:477 */       pivotField.setShowAll(false);
/* 410:    */     }
/* 411:479 */     pivotFields.setCount(pivotFields.sizeOfPivotFieldArray());
/* 412:    */   }
/* 413:    */   
/* 414:    */   protected static abstract interface PivotTableReferenceConfigurator
/* 415:    */   {
/* 416:    */     public abstract void configureReference(CTWorksheetSource paramCTWorksheetSource);
/* 417:    */   }
/* 418:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPivotTable
 * JD-Core Version:    0.7.0.1
 */