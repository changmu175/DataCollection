/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Locale;
/*  11:    */ import org.apache.poi.POIXMLDocumentPart;
/*  12:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  13:    */ import org.apache.poi.POIXMLTypeLoader;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  15:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  16:    */ import org.apache.poi.ss.usermodel.Cell;
/*  17:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  18:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  19:    */ import org.apache.poi.ss.usermodel.Table;
/*  20:    */ import org.apache.poi.ss.usermodel.TableStyleInfo;
/*  21:    */ import org.apache.poi.ss.util.AreaReference;
/*  22:    */ import org.apache.poi.ss.util.CellReference;
/*  23:    */ import org.apache.poi.util.Internal;
/*  24:    */ import org.apache.poi.util.StringUtil;
/*  25:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;
/*  26:    */ import org.apache.xmlbeans.XmlException;
/*  27:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
/*  28:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
/*  29:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
/*  30:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable.Factory;
/*  31:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
/*  32:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
/*  33:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
/*  34:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlColumnPr;
/*  35:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument;
/*  36:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument.Factory;
/*  37:    */ 
/*  38:    */ public class XSSFTable
/*  39:    */   extends POIXMLDocumentPart
/*  40:    */   implements Table
/*  41:    */ {
/*  42:    */   private CTTable ctTable;
/*  43:    */   private transient List<XSSFXmlColumnPr> xmlColumnPr;
/*  44:    */   private transient CTTableColumn[] ctColumns;
/*  45:    */   private transient HashMap<String, Integer> columnMap;
/*  46:    */   private transient CellReference startCellReference;
/*  47:    */   private transient CellReference endCellReference;
/*  48:    */   private transient String commonXPath;
/*  49:    */   private transient String name;
/*  50:    */   private transient String styleName;
/*  51:    */   
/*  52:    */   public XSSFTable()
/*  53:    */   {
/*  54: 79 */     this.ctTable = CTTable.Factory.newInstance();
/*  55:    */   }
/*  56:    */   
/*  57:    */   public XSSFTable(PackagePart part)
/*  58:    */     throws IOException
/*  59:    */   {
/*  60: 88 */     super(part);
/*  61: 89 */     readFrom(part.getInputStream());
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void readFrom(InputStream is)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67:    */     try
/*  68:    */     {
/*  69: 99 */       TableDocument doc = TableDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  70:100 */       this.ctTable = doc.getTable();
/*  71:    */     }
/*  72:    */     catch (XmlException e)
/*  73:    */     {
/*  74:102 */       throw new IOException(e.getLocalizedMessage());
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public XSSFSheet getXSSFSheet()
/*  79:    */   {
/*  80:110 */     return (XSSFSheet)getParent();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void writeTo(OutputStream out)
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:119 */     updateHeaders();
/*  87:    */     
/*  88:121 */     TableDocument doc = TableDocument.Factory.newInstance();
/*  89:122 */     doc.setTable(this.ctTable);
/*  90:123 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected void commit()
/*  94:    */     throws IOException
/*  95:    */   {
/*  96:128 */     PackagePart part = getPackagePart();
/*  97:129 */     OutputStream out = part.getOutputStream();
/*  98:130 */     writeTo(out);
/*  99:131 */     out.close();
/* 100:    */   }
/* 101:    */   
/* 102:    */   @Internal(since="POI 3.15 beta 3")
/* 103:    */   public CTTable getCTTable()
/* 104:    */   {
/* 105:140 */     return this.ctTable;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean mapsTo(long id)
/* 109:    */   {
/* 110:149 */     List<XSSFXmlColumnPr> pointers = getXmlColumnPrs();
/* 111:151 */     for (XSSFXmlColumnPr pointer : pointers) {
/* 112:152 */       if (pointer.getMapId() == id) {
/* 113:153 */         return true;
/* 114:    */       }
/* 115:    */     }
/* 116:157 */     return false;
/* 117:    */   }
/* 118:    */   
/* 119:    */   private CTTableColumn[] getTableColumns()
/* 120:    */   {
/* 121:166 */     if (this.ctColumns == null) {
/* 122:167 */       this.ctColumns = this.ctTable.getTableColumns().getTableColumnArray();
/* 123:    */     }
/* 124:169 */     return this.ctColumns;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String getCommonXpath()
/* 128:    */   {
/* 129:181 */     if (this.commonXPath == null)
/* 130:    */     {
/* 131:182 */       String[] commonTokens = new String[0];
/* 132:183 */       for (CTTableColumn column : getTableColumns()) {
/* 133:184 */         if (column.getXmlColumnPr() != null)
/* 134:    */         {
/* 135:185 */           String xpath = column.getXmlColumnPr().getXpath();
/* 136:186 */           String[] tokens = xpath.split("/");
/* 137:187 */           if (commonTokens.length == 0)
/* 138:    */           {
/* 139:188 */             commonTokens = tokens;
/* 140:    */           }
/* 141:    */           else
/* 142:    */           {
/* 143:191 */             int maxLength = Math.min(commonTokens.length, tokens.length);
/* 144:193 */             for (int i = 0; i < maxLength; i++) {
/* 145:194 */               if (!commonTokens[i].equals(tokens[i]))
/* 146:    */               {
/* 147:195 */                 List<String> subCommonTokens = Arrays.asList(commonTokens).subList(0, i);
/* 148:    */                 
/* 149:197 */                 String[] container = new String[0];
/* 150:    */                 
/* 151:199 */                 commonTokens = (String[])subCommonTokens.toArray(container);
/* 152:200 */                 break;
/* 153:    */               }
/* 154:    */             }
/* 155:    */           }
/* 156:    */         }
/* 157:    */       }
/* 158:207 */       commonTokens[0] = "";
/* 159:208 */       this.commonXPath = StringUtil.join(commonTokens, "/");
/* 160:    */     }
/* 161:211 */     return this.commonXPath;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public List<XSSFXmlColumnPr> getXmlColumnPrs()
/* 165:    */   {
/* 166:221 */     if (this.xmlColumnPr == null)
/* 167:    */     {
/* 168:222 */       this.xmlColumnPr = new ArrayList();
/* 169:223 */       for (CTTableColumn column : getTableColumns()) {
/* 170:224 */         if (column.getXmlColumnPr() != null)
/* 171:    */         {
/* 172:225 */           XSSFXmlColumnPr columnPr = new XSSFXmlColumnPr(this, column, column.getXmlColumnPr());
/* 173:226 */           this.xmlColumnPr.add(columnPr);
/* 174:    */         }
/* 175:    */       }
/* 176:    */     }
/* 177:230 */     return this.xmlColumnPr;
/* 178:    */   }
/* 179:    */   
/* 180:    */   @Internal("Return type likely to change")
/* 181:    */   public void addColumn()
/* 182:    */   {
/* 183:241 */     CTTableColumns columns = this.ctTable.getTableColumns();
/* 184:242 */     if (columns == null) {
/* 185:243 */       columns = this.ctTable.addNewTableColumns();
/* 186:    */     }
/* 187:247 */     CTTableColumn column = columns.addNewTableColumn();
/* 188:248 */     int num = columns.sizeOfTableColumnArray();
/* 189:249 */     columns.setCount(num);
/* 190:250 */     column.setId(num);
/* 191:    */     
/* 192:    */ 
/* 193:253 */     updateHeaders();
/* 194:    */   }
/* 195:    */   
/* 196:    */   public String getName()
/* 197:    */   {
/* 198:260 */     if (this.name == null) {
/* 199:261 */       setName(this.ctTable.getName());
/* 200:    */     }
/* 201:263 */     return this.name;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setName(String newName)
/* 205:    */   {
/* 206:271 */     if (newName == null)
/* 207:    */     {
/* 208:272 */       this.ctTable.unsetName();
/* 209:273 */       this.name = null;
/* 210:274 */       return;
/* 211:    */     }
/* 212:276 */     this.ctTable.setName(newName);
/* 213:277 */     this.name = newName;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public String getStyleName()
/* 217:    */   {
/* 218:285 */     if ((this.styleName == null) && (this.ctTable.isSetTableStyleInfo())) {
/* 219:286 */       setStyleName(this.ctTable.getTableStyleInfo().getName());
/* 220:    */     }
/* 221:288 */     return this.styleName;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void setStyleName(String newStyleName)
/* 225:    */   {
/* 226:297 */     if (newStyleName == null)
/* 227:    */     {
/* 228:298 */       if (this.ctTable.isSetTableStyleInfo()) {
/* 229:299 */         this.ctTable.getTableStyleInfo().unsetName();
/* 230:    */       }
/* 231:301 */       this.styleName = null;
/* 232:302 */       return;
/* 233:    */     }
/* 234:304 */     if (!this.ctTable.isSetTableStyleInfo()) {
/* 235:305 */       this.ctTable.addNewTableStyleInfo();
/* 236:    */     }
/* 237:307 */     this.ctTable.getTableStyleInfo().setName(newStyleName);
/* 238:308 */     this.styleName = newStyleName;
/* 239:    */   }
/* 240:    */   
/* 241:    */   public String getDisplayName()
/* 242:    */   {
/* 243:315 */     return this.ctTable.getDisplayName();
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setDisplayName(String name)
/* 247:    */   {
/* 248:323 */     this.ctTable.setDisplayName(name);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public long getNumberOfMappedColumns()
/* 252:    */   {
/* 253:330 */     return this.ctTable.getTableColumns().getCount();
/* 254:    */   }
/* 255:    */   
/* 256:    */   /**
/* 257:    */    * @deprecated
/* 258:    */    */
/* 259:    */   public long getNumerOfMappedColumns()
/* 260:    */   {
/* 261:338 */     return getNumberOfMappedColumns();
/* 262:    */   }
/* 263:    */   
/* 264:    */   public AreaReference getCellReferences()
/* 265:    */   {
/* 266:352 */     return new AreaReference(getStartCellReference(), getEndCellReference(), SpreadsheetVersion.EXCEL2007);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setCellReferences(AreaReference refs)
/* 270:    */   {
/* 271:367 */     String ref = refs.formatAsString();
/* 272:368 */     if (ref.indexOf('!') != -1) {
/* 273:369 */       ref = ref.substring(ref.indexOf('!') + 1);
/* 274:    */     }
/* 275:373 */     this.ctTable.setRef(ref);
/* 276:374 */     if (this.ctTable.isSetAutoFilter()) {
/* 277:375 */       this.ctTable.getAutoFilter().setRef(ref);
/* 278:    */     }
/* 279:379 */     updateReferences();
/* 280:380 */     updateHeaders();
/* 281:    */   }
/* 282:    */   
/* 283:    */   public CellReference getStartCellReference()
/* 284:    */   {
/* 285:392 */     if (this.startCellReference == null) {
/* 286:393 */       setCellReferences();
/* 287:    */     }
/* 288:395 */     return this.startCellReference;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public CellReference getEndCellReference()
/* 292:    */   {
/* 293:407 */     if (this.endCellReference == null) {
/* 294:408 */       setCellReferences();
/* 295:    */     }
/* 296:410 */     return this.endCellReference;
/* 297:    */   }
/* 298:    */   
/* 299:    */   private void setCellReferences()
/* 300:    */   {
/* 301:417 */     String ref = this.ctTable.getRef();
/* 302:418 */     if (ref != null)
/* 303:    */     {
/* 304:419 */       String[] boundaries = ref.split(":", 2);
/* 305:420 */       String from = boundaries[0];
/* 306:421 */       String to = boundaries[1];
/* 307:422 */       this.startCellReference = new CellReference(from);
/* 308:423 */       this.endCellReference = new CellReference(to);
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public void updateReferences()
/* 313:    */   {
/* 314:439 */     this.startCellReference = null;
/* 315:440 */     this.endCellReference = null;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public int getRowCount()
/* 319:    */   {
/* 320:453 */     CellReference from = getStartCellReference();
/* 321:454 */     CellReference to = getEndCellReference();
/* 322:    */     
/* 323:456 */     int rowCount = 0;
/* 324:457 */     if ((from != null) && (to != null)) {
/* 325:458 */       rowCount = to.getRow() - from.getRow() + 1;
/* 326:    */     }
/* 327:460 */     return rowCount;
/* 328:    */   }
/* 329:    */   
/* 330:    */   public void updateHeaders()
/* 331:    */   {
/* 332:477 */     XSSFSheet sheet = (XSSFSheet)getParent();
/* 333:478 */     CellReference ref = getStartCellReference();
/* 334:479 */     if (ref == null) {
/* 335:479 */       return;
/* 336:    */     }
/* 337:481 */     int headerRow = ref.getRow();
/* 338:482 */     int firstHeaderColumn = ref.getCol();
/* 339:483 */     XSSFRow row = sheet.getRow(headerRow);
/* 340:484 */     DataFormatter formatter = new DataFormatter();
/* 341:486 */     if ((row != null) && (row.getCTRow().validate()))
/* 342:    */     {
/* 343:487 */       int cellnum = firstHeaderColumn;
/* 344:488 */       for (CTTableColumn col : getCTTable().getTableColumns().getTableColumnArray())
/* 345:    */       {
/* 346:489 */         XSSFCell cell = row.getCell(cellnum);
/* 347:490 */         if (cell != null) {
/* 348:491 */           col.setName(formatter.formatCellValue(cell));
/* 349:    */         }
/* 350:493 */         cellnum++;
/* 351:    */       }
/* 352:495 */       this.ctColumns = null;
/* 353:496 */       this.columnMap = null;
/* 354:497 */       this.xmlColumnPr = null;
/* 355:498 */       this.commonXPath = null;
/* 356:    */     }
/* 357:    */   }
/* 358:    */   
/* 359:    */   private static String caseInsensitive(String s)
/* 360:    */   {
/* 361:503 */     return s.toUpperCase(Locale.ROOT);
/* 362:    */   }
/* 363:    */   
/* 364:    */   public int findColumnIndex(String columnHeader)
/* 365:    */   {
/* 366:519 */     if (columnHeader == null) {
/* 367:519 */       return -1;
/* 368:    */     }
/* 369:520 */     if (this.columnMap == null)
/* 370:    */     {
/* 371:522 */       int count = getTableColumns().length;
/* 372:523 */       this.columnMap = new HashMap(count * 3 / 2);
/* 373:    */       
/* 374:525 */       int i = 0;
/* 375:526 */       for (CTTableColumn column : getTableColumns())
/* 376:    */       {
/* 377:527 */         String columnName = column.getName();
/* 378:528 */         this.columnMap.put(caseInsensitive(columnName), Integer.valueOf(i));
/* 379:529 */         i++;
/* 380:    */       }
/* 381:    */     }
/* 382:534 */     Integer idx = (Integer)this.columnMap.get(caseInsensitive(columnHeader.replace("'", "")));
/* 383:535 */     return idx == null ? -1 : idx.intValue();
/* 384:    */   }
/* 385:    */   
/* 386:    */   public String getSheetName()
/* 387:    */   {
/* 388:542 */     return getXSSFSheet().getSheetName();
/* 389:    */   }
/* 390:    */   
/* 391:    */   public boolean isHasTotalsRow()
/* 392:    */   {
/* 393:553 */     return this.ctTable.getTotalsRowShown();
/* 394:    */   }
/* 395:    */   
/* 396:    */   public int getTotalsRowCount()
/* 397:    */   {
/* 398:563 */     return (int)this.ctTable.getTotalsRowCount();
/* 399:    */   }
/* 400:    */   
/* 401:    */   public int getHeaderRowCount()
/* 402:    */   {
/* 403:572 */     return (int)this.ctTable.getHeaderRowCount();
/* 404:    */   }
/* 405:    */   
/* 406:    */   public int getStartColIndex()
/* 407:    */   {
/* 408:579 */     return getStartCellReference().getCol();
/* 409:    */   }
/* 410:    */   
/* 411:    */   public int getStartRowIndex()
/* 412:    */   {
/* 413:586 */     return getStartCellReference().getRow();
/* 414:    */   }
/* 415:    */   
/* 416:    */   public int getEndColIndex()
/* 417:    */   {
/* 418:593 */     return getEndCellReference().getCol();
/* 419:    */   }
/* 420:    */   
/* 421:    */   public int getEndRowIndex()
/* 422:    */   {
/* 423:600 */     return getEndCellReference().getRow();
/* 424:    */   }
/* 425:    */   
/* 426:    */   public TableStyleInfo getStyle()
/* 427:    */   {
/* 428:607 */     if (!this.ctTable.isSetTableStyleInfo()) {
/* 429:607 */       return null;
/* 430:    */     }
/* 431:608 */     return new XSSFTableStyleInfo(((XSSFSheet)getParent()).getWorkbook().getStylesSource(), this.ctTable.getTableStyleInfo());
/* 432:    */   }
/* 433:    */   
/* 434:    */   public boolean contains(Cell cell)
/* 435:    */   {
/* 436:616 */     if (cell == null) {
/* 437:616 */       return false;
/* 438:    */     }
/* 439:618 */     if (!getSheetName().equals(cell.getSheet().getSheetName())) {
/* 440:618 */       return false;
/* 441:    */     }
/* 442:620 */     if ((cell.getRowIndex() >= getStartRowIndex()) && (cell.getRowIndex() <= getEndRowIndex()) && (cell.getColumnIndex() >= getStartColIndex()) && (cell.getColumnIndex() <= getEndColIndex())) {
/* 443:624 */       return true;
/* 444:    */     }
/* 445:626 */     return false;
/* 446:    */   }
/* 447:    */   
/* 448:    */   protected void onTableDelete()
/* 449:    */   {
/* 450:633 */     for (POIXMLDocumentPart.RelationPart part : getRelationParts()) {
/* 451:634 */       removeRelation(part.getDocumentPart(), true);
/* 452:    */     }
/* 453:    */   }
/* 454:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFTable
 * JD-Core Version:    0.7.0.1
 */