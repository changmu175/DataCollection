/*   1:    */ package org.apache.poi.xssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.LinkedList;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.Set;
/*  15:    */ import javax.xml.parsers.ParserConfigurationException;
/*  16:    */ import org.apache.poi.POIXMLException;
/*  17:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  18:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  19:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  20:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  21:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  22:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  23:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  24:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  25:    */ import org.apache.poi.util.POILogFactory;
/*  26:    */ import org.apache.poi.util.POILogger;
/*  27:    */ import org.apache.poi.util.SAXHelper;
/*  28:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  29:    */ import org.apache.poi.xssf.model.SharedStringsTable;
/*  30:    */ import org.apache.poi.xssf.model.StylesTable;
/*  31:    */ import org.apache.poi.xssf.model.ThemesTable;
/*  32:    */ import org.apache.poi.xssf.usermodel.XSSFDrawing;
/*  33:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  34:    */ import org.apache.poi.xssf.usermodel.XSSFShape;
/*  35:    */ import org.apache.xmlbeans.XmlException;
/*  36:    */ import org.xml.sax.Attributes;
/*  37:    */ import org.xml.sax.InputSource;
/*  38:    */ import org.xml.sax.SAXException;
/*  39:    */ import org.xml.sax.XMLReader;
/*  40:    */ import org.xml.sax.helpers.DefaultHandler;
/*  41:    */ 
/*  42:    */ public class XSSFReader
/*  43:    */ {
/*  44: 69 */   private static final Set<String> WORKSHEET_RELS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { XSSFRelation.WORKSHEET.getRelation(), XSSFRelation.CHARTSHEET.getRelation() })));
/*  45: 76 */   private static final POILogger LOGGER = POILogFactory.getLogger(XSSFReader.class);
/*  46:    */   protected OPCPackage pkg;
/*  47:    */   protected PackagePart workbookPart;
/*  48:    */   
/*  49:    */   public XSSFReader(OPCPackage pkg)
/*  50:    */     throws IOException, OpenXML4JException
/*  51:    */   {
/*  52: 85 */     this.pkg = pkg;
/*  53:    */     
/*  54: 87 */     PackageRelationship coreDocRelationship = this.pkg.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument").getRelationship(0);
/*  55: 93 */     if (coreDocRelationship == null)
/*  56:    */     {
/*  57: 94 */       if (this.pkg.getRelationshipsByType("http://purl.oclc.org/ooxml/officeDocument/relationships/officeDocument").getRelationship(0) != null) {
/*  58: 96 */         throw new POIXMLException("Strict OOXML isn't currently supported, please see bug #57699");
/*  59:    */       }
/*  60: 99 */       throw new POIXMLException("OOXML file structure broken/invalid - no core document found!");
/*  61:    */     }
/*  62:103 */     this.workbookPart = this.pkg.getPart(coreDocRelationship);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public SharedStringsTable getSharedStringsTable()
/*  66:    */     throws IOException, InvalidFormatException
/*  67:    */   {
/*  68:113 */     ArrayList<PackagePart> parts = this.pkg.getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());
/*  69:114 */     return parts.size() == 0 ? null : new SharedStringsTable((PackagePart)parts.get(0));
/*  70:    */   }
/*  71:    */   
/*  72:    */   public StylesTable getStylesTable()
/*  73:    */     throws IOException, InvalidFormatException
/*  74:    */   {
/*  75:122 */     ArrayList<PackagePart> parts = this.pkg.getPartsByContentType(XSSFRelation.STYLES.getContentType());
/*  76:123 */     if (parts.size() == 0) {
/*  77:123 */       return null;
/*  78:    */     }
/*  79:126 */     StylesTable styles = new StylesTable((PackagePart)parts.get(0));
/*  80:127 */     parts = this.pkg.getPartsByContentType(XSSFRelation.THEME.getContentType());
/*  81:128 */     if (parts.size() != 0) {
/*  82:129 */       styles.setTheme(new ThemesTable((PackagePart)parts.get(0)));
/*  83:    */     }
/*  84:131 */     return styles;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public InputStream getSharedStringsData()
/*  88:    */     throws IOException, InvalidFormatException
/*  89:    */   {
/*  90:140 */     return XSSFRelation.SHARED_STRINGS.getContents(this.workbookPart);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public InputStream getStylesData()
/*  94:    */     throws IOException, InvalidFormatException
/*  95:    */   {
/*  96:148 */     return XSSFRelation.STYLES.getContents(this.workbookPart);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public InputStream getThemesData()
/* 100:    */     throws IOException, InvalidFormatException
/* 101:    */   {
/* 102:156 */     return XSSFRelation.THEME.getContents(this.workbookPart);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public InputStream getWorkbookData()
/* 106:    */     throws IOException, InvalidFormatException
/* 107:    */   {
/* 108:165 */     return this.workbookPart.getInputStream();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public InputStream getSheet(String relId)
/* 112:    */     throws IOException, InvalidFormatException
/* 113:    */   {
/* 114:174 */     PackageRelationship rel = this.workbookPart.getRelationship(relId);
/* 115:175 */     if (rel == null) {
/* 116:176 */       throw new IllegalArgumentException("No Sheet found with r:id " + relId);
/* 117:    */     }
/* 118:179 */     PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
/* 119:180 */     PackagePart sheet = this.pkg.getPart(relName);
/* 120:181 */     if (sheet == null) {
/* 121:182 */       throw new IllegalArgumentException("No data found for Sheet with r:id " + relId);
/* 122:    */     }
/* 123:184 */     return sheet.getInputStream();
/* 124:    */   }
/* 125:    */   
/* 126:    */   public Iterator<InputStream> getSheetsData()
/* 127:    */     throws IOException, InvalidFormatException
/* 128:    */   {
/* 129:195 */     return new SheetIterator(this.workbookPart);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static class SheetIterator
/* 133:    */     implements Iterator<InputStream>
/* 134:    */   {
/* 135:    */     private final Map<String, PackagePart> sheetMap;
/* 136:    */     XSSFSheetRef xssfSheetRef;
/* 137:    */     final Iterator<XSSFSheetRef> sheetIterator;
/* 138:    */     
/* 139:    */     SheetIterator(PackagePart wb)
/* 140:    */       throws IOException
/* 141:    */     {
/* 142:    */       try
/* 143:    */       {
/* 144:232 */         this.sheetMap = new HashMap();
/* 145:233 */         OPCPackage pkg = wb.getPackage();
/* 146:234 */         Set<String> worksheetRels = getSheetRelationships();
/* 147:235 */         for (PackageRelationship rel : wb.getRelationships())
/* 148:    */         {
/* 149:236 */           String relType = rel.getRelationshipType();
/* 150:237 */           if (worksheetRels.contains(relType))
/* 151:    */           {
/* 152:238 */             PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
/* 153:239 */             this.sheetMap.put(rel.getId(), pkg.getPart(relName));
/* 154:    */           }
/* 155:    */         }
/* 156:244 */         this.sheetIterator = createSheetIteratorFromWB(wb);
/* 157:    */       }
/* 158:    */       catch (InvalidFormatException e)
/* 159:    */       {
/* 160:246 */         throw new POIXMLException(e);
/* 161:    */       }
/* 162:    */     }
/* 163:    */     
/* 164:    */     Iterator<XSSFSheetRef> createSheetIteratorFromWB(PackagePart wb)
/* 165:    */       throws IOException
/* 166:    */     {
/* 167:252 */       XMLSheetRefReader xmlSheetRefReader = new XMLSheetRefReader(null);
/* 168:    */       XMLReader xmlReader;
/* 169:    */       try
/* 170:    */       {
/* 171:255 */         xmlReader = SAXHelper.newXMLReader();
/* 172:    */       }
/* 173:    */       catch (ParserConfigurationException e)
/* 174:    */       {
/* 175:257 */         throw new POIXMLException(e);
/* 176:    */       }
/* 177:    */       catch (SAXException e)
/* 178:    */       {
/* 179:259 */         throw new POIXMLException(e);
/* 180:    */       }
/* 181:261 */       xmlReader.setContentHandler(xmlSheetRefReader);
/* 182:    */       try
/* 183:    */       {
/* 184:263 */         xmlReader.parse(new InputSource(wb.getInputStream()));
/* 185:    */       }
/* 186:    */       catch (SAXException e)
/* 187:    */       {
/* 188:265 */         throw new POIXMLException(e);
/* 189:    */       }
/* 190:268 */       List<XSSFSheetRef> validSheets = new ArrayList();
/* 191:269 */       for (XSSFSheetRef xssfSheetRef : xmlSheetRefReader.getSheetRefs())
/* 192:    */       {
/* 193:271 */         String sheetId = xssfSheetRef.getId();
/* 194:272 */         if ((sheetId != null) && (sheetId.length() > 0)) {
/* 195:273 */           validSheets.add(xssfSheetRef);
/* 196:    */         }
/* 197:    */       }
/* 198:276 */       return validSheets.iterator();
/* 199:    */     }
/* 200:    */     
/* 201:    */     Set<String> getSheetRelationships()
/* 202:    */     {
/* 203:288 */       return XSSFReader.WORKSHEET_RELS;
/* 204:    */     }
/* 205:    */     
/* 206:    */     public boolean hasNext()
/* 207:    */     {
/* 208:298 */       return this.sheetIterator.hasNext();
/* 209:    */     }
/* 210:    */     
/* 211:    */     public InputStream next()
/* 212:    */     {
/* 213:308 */       this.xssfSheetRef = ((XSSFSheetRef)this.sheetIterator.next());
/* 214:    */       
/* 215:310 */       String sheetId = this.xssfSheetRef.getId();
/* 216:    */       try
/* 217:    */       {
/* 218:312 */         PackagePart sheetPkg = (PackagePart)this.sheetMap.get(sheetId);
/* 219:313 */         return sheetPkg.getInputStream();
/* 220:    */       }
/* 221:    */       catch (IOException e)
/* 222:    */       {
/* 223:315 */         throw new POIXMLException(e);
/* 224:    */       }
/* 225:    */     }
/* 226:    */     
/* 227:    */     public String getSheetName()
/* 228:    */     {
/* 229:325 */       return this.xssfSheetRef.getName();
/* 230:    */     }
/* 231:    */     
/* 232:    */     public CommentsTable getSheetComments()
/* 233:    */     {
/* 234:333 */       PackagePart sheetPkg = getSheetPart();
/* 235:    */       try
/* 236:    */       {
/* 237:337 */         PackageRelationshipCollection commentsList = sheetPkg.getRelationshipsByType(XSSFRelation.SHEET_COMMENTS.getRelation());
/* 238:339 */         if (commentsList.size() > 0)
/* 239:    */         {
/* 240:340 */           PackageRelationship comments = commentsList.getRelationship(0);
/* 241:341 */           PackagePartName commentsName = PackagingURIHelper.createPartName(comments.getTargetURI());
/* 242:342 */           PackagePart commentsPart = sheetPkg.getPackage().getPart(commentsName);
/* 243:343 */           return new CommentsTable(commentsPart);
/* 244:    */         }
/* 245:    */       }
/* 246:    */       catch (InvalidFormatException e)
/* 247:    */       {
/* 248:346 */         return null;
/* 249:    */       }
/* 250:    */       catch (IOException e)
/* 251:    */       {
/* 252:348 */         return null;
/* 253:    */       }
/* 254:350 */       return null;
/* 255:    */     }
/* 256:    */     
/* 257:    */     public List<XSSFShape> getShapes()
/* 258:    */     {
/* 259:358 */       PackagePart sheetPkg = getSheetPart();
/* 260:359 */       List<XSSFShape> shapes = new LinkedList();
/* 261:    */       try
/* 262:    */       {
/* 263:362 */         PackageRelationshipCollection drawingsList = sheetPkg.getRelationshipsByType(XSSFRelation.DRAWINGS.getRelation());
/* 264:363 */         for (int i = 0; i < drawingsList.size(); i++)
/* 265:    */         {
/* 266:364 */           PackageRelationship drawings = drawingsList.getRelationship(i);
/* 267:365 */           PackagePartName drawingsName = PackagingURIHelper.createPartName(drawings.getTargetURI());
/* 268:366 */           PackagePart drawingsPart = sheetPkg.getPackage().getPart(drawingsName);
/* 269:367 */           if (drawingsPart == null)
/* 270:    */           {
/* 271:369 */             XSSFReader.LOGGER.log(5, new Object[] { "Missing drawing: " + drawingsName + ". Skipping it." });
/* 272:    */           }
/* 273:    */           else
/* 274:    */           {
/* 275:372 */             XSSFDrawing drawing = new XSSFDrawing(drawingsPart);
/* 276:373 */             shapes.addAll(drawing.getShapes());
/* 277:    */           }
/* 278:    */         }
/* 279:    */       }
/* 280:    */       catch (XmlException e)
/* 281:    */       {
/* 282:376 */         return null;
/* 283:    */       }
/* 284:    */       catch (InvalidFormatException e)
/* 285:    */       {
/* 286:378 */         return null;
/* 287:    */       }
/* 288:    */       catch (IOException e)
/* 289:    */       {
/* 290:380 */         return null;
/* 291:    */       }
/* 292:382 */       return shapes;
/* 293:    */     }
/* 294:    */     
/* 295:    */     public PackagePart getSheetPart()
/* 296:    */     {
/* 297:386 */       String sheetId = this.xssfSheetRef.getId();
/* 298:387 */       return (PackagePart)this.sheetMap.get(sheetId);
/* 299:    */     }
/* 300:    */     
/* 301:    */     public void remove()
/* 302:    */     {
/* 303:395 */       throw new IllegalStateException("Not supported");
/* 304:    */     }
/* 305:    */   }
/* 306:    */   
/* 307:    */   protected static final class XSSFSheetRef
/* 308:    */   {
/* 309:    */     private final String id;
/* 310:    */     private final String name;
/* 311:    */     
/* 312:    */     public XSSFSheetRef(String id, String name)
/* 313:    */     {
/* 314:405 */       this.id = id;
/* 315:406 */       this.name = name;
/* 316:    */     }
/* 317:    */     
/* 318:    */     public String getId()
/* 319:    */     {
/* 320:410 */       return this.id;
/* 321:    */     }
/* 322:    */     
/* 323:    */     public String getName()
/* 324:    */     {
/* 325:414 */       return this.name;
/* 326:    */     }
/* 327:    */   }
/* 328:    */   
/* 329:    */   private static class XMLSheetRefReader
/* 330:    */     extends DefaultHandler
/* 331:    */   {
/* 332:    */     private static final String SHEET = "sheet";
/* 333:    */     private static final String ID = "id";
/* 334:    */     private static final String NAME = "name";
/* 335:424 */     private final List<XSSFSheetRef> sheetRefs = new LinkedList();
/* 336:    */     
/* 337:    */     public void startElement(String uri, String localName, String qName, Attributes attrs)
/* 338:    */       throws SAXException
/* 339:    */     {
/* 340:430 */       if (localName.equalsIgnoreCase("sheet"))
/* 341:    */       {
/* 342:431 */         String name = null;
/* 343:432 */         String id = null;
/* 344:433 */         for (int i = 0; i < attrs.getLength(); i++)
/* 345:    */         {
/* 346:434 */           String attrName = attrs.getLocalName(i);
/* 347:435 */           if (attrName.equalsIgnoreCase("name")) {
/* 348:436 */             name = attrs.getValue(i);
/* 349:437 */           } else if (attrName.equalsIgnoreCase("id")) {
/* 350:438 */             id = attrs.getValue(i);
/* 351:    */           }
/* 352:440 */           if ((name != null) && (id != null))
/* 353:    */           {
/* 354:441 */             this.sheetRefs.add(new XSSFSheetRef(id, name));
/* 355:442 */             break;
/* 356:    */           }
/* 357:    */         }
/* 358:    */       }
/* 359:    */     }
/* 360:    */     
/* 361:    */     List<XSSFSheetRef> getSheetRefs()
/* 362:    */     {
/* 363:449 */       return Collections.unmodifiableList(this.sheetRefs);
/* 364:    */     }
/* 365:    */   }
/* 366:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.eventusermodel.XSSFReader

 * JD-Core Version:    0.7.0.1

 */