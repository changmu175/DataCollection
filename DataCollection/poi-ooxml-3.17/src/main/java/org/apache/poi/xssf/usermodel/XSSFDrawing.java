/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.excel.CTClientData;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.List;
/*  10:    */ import javax.xml.namespace.QName;
/*  11:    */ import org.apache.poi.POIXMLDocumentPart;
/*  12:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  13:    */ import org.apache.poi.POIXMLException;
/*  14:    */ import org.apache.poi.POIXMLTypeLoader;
/*  15:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  16:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  20:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  21:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  22:    */ import org.apache.poi.ss.usermodel.ClientAnchor;
/*  23:    */ import org.apache.poi.ss.usermodel.Drawing;
/*  24:    */ import org.apache.poi.ss.util.CellAddress;
/*  25:    */ import org.apache.poi.ss.util.ImageUtils;
/*  26:    */ import org.apache.poi.util.Internal;
/*  27:    */ import org.apache.poi.util.POILogFactory;
/*  28:    */ import org.apache.poi.util.POILogger;
/*  29:    */ import org.apache.poi.util.Units;
/*  30:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  31:    */ import org.apache.xmlbeans.SchemaType;
/*  32:    */ import org.apache.xmlbeans.XmlCursor;
/*  33:    */ import org.apache.xmlbeans.XmlException;
/*  34:    */ import org.apache.xmlbeans.XmlObject;
/*  35:    */ import org.apache.xmlbeans.XmlOptions;
/*  36:    */ import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  39:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
/*  41:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  42:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
/*  43:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
/*  44:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  45:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  46:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  47:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties;
/*  48:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  49:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D.Factory;
/*  50:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor;
/*  51:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector;
/*  52:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing;
/*  53:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing.Factory;
/*  54:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame;
/*  55:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape;
/*  56:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
/*  57:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor;
/*  58:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
/*  59:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual;
/*  60:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShapeNonVisual;
/*  61:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
/*  62:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
/*  63:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.Enum;
/*  64:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
/*  65:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
/*  66:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  67:    */ 
/*  68:    */ public final class XSSFDrawing
/*  69:    */   extends POIXMLDocumentPart
/*  70:    */   implements Drawing<XSSFShape>
/*  71:    */ {
/*  72: 80 */   private static final POILogger LOG = POILogFactory.getLogger(XSSFDrawing.class);
/*  73:    */   private CTDrawing drawing;
/*  74: 86 */   private long numOfGraphicFrames = 0L;
/*  75:    */   protected static final String NAMESPACE_A = "http://schemas.openxmlformats.org/drawingml/2006/main";
/*  76:    */   protected static final String NAMESPACE_C = "http://schemas.openxmlformats.org/drawingml/2006/chart";
/*  77:    */   
/*  78:    */   protected XSSFDrawing()
/*  79:    */   {
/*  80: 98 */     this.drawing = newDrawing();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public XSSFDrawing(PackagePart part)
/*  84:    */     throws IOException, XmlException
/*  85:    */   {
/*  86:110 */     super(part);
/*  87:111 */     XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  88:    */     
/*  89:113 */     options.setLoadReplaceDocumentElement(null);
/*  90:114 */     InputStream is = part.getInputStream();
/*  91:    */     try
/*  92:    */     {
/*  93:116 */       this.drawing = CTDrawing.Factory.parse(is, options);
/*  94:    */     }
/*  95:    */     finally
/*  96:    */     {
/*  97:118 */       is.close();
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   private static CTDrawing newDrawing()
/* 102:    */   {
/* 103:128 */     return CTDrawing.Factory.newInstance();
/* 104:    */   }
/* 105:    */   
/* 106:    */   @Internal
/* 107:    */   public CTDrawing getCTDrawing()
/* 108:    */   {
/* 109:138 */     return this.drawing;
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected void commit()
/* 113:    */     throws IOException
/* 114:    */   {
/* 115:143 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 116:    */     
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:151 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTDrawing.type.getName().getNamespaceURI(), "wsDr", "xdr"));
/* 124:    */     
/* 125:    */ 
/* 126:    */ 
/* 127:155 */     PackagePart part = getPackagePart();
/* 128:156 */     OutputStream out = part.getOutputStream();
/* 129:157 */     this.drawing.save(out, xmlOptions);
/* 130:158 */     out.close();
/* 131:    */   }
/* 132:    */   
/* 133:    */   public XSSFClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2)
/* 134:    */   {
/* 135:164 */     return new XSSFClientAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public XSSFTextBox createTextbox(XSSFClientAnchor anchor)
/* 139:    */   {
/* 140:175 */     long shapeId = newShapeId();
/* 141:176 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 142:177 */     org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape ctShape = ctAnchor.addNewSp();
/* 143:178 */     ctShape.set(XSSFSimpleShape.prototype());
/* 144:179 */     ctShape.getNvSpPr().getCNvPr().setId(shapeId);
/* 145:180 */     XSSFTextBox shape = new XSSFTextBox(this, ctShape);
/* 146:181 */     shape.anchor = anchor;
/* 147:182 */     return shape;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public XSSFPicture createPicture(XSSFClientAnchor anchor, int pictureIndex)
/* 151:    */   {
/* 152:197 */     PackageRelationship rel = addPictureReference(pictureIndex);
/* 153:    */     
/* 154:199 */     long shapeId = newShapeId();
/* 155:200 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 156:201 */     CTPicture ctShape = ctAnchor.addNewPic();
/* 157:202 */     ctShape.set(XSSFPicture.prototype());
/* 158:    */     
/* 159:204 */     ctShape.getNvPicPr().getCNvPr().setId(shapeId);
/* 160:    */     
/* 161:206 */     XSSFPicture shape = new XSSFPicture(this, ctShape);
/* 162:207 */     shape.anchor = anchor;
/* 163:208 */     shape.setPictureReference(rel);
/* 164:209 */     ctShape.getSpPr().setXfrm(createXfrm(anchor));
/* 165:    */     
/* 166:211 */     return shape;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public XSSFPicture createPicture(ClientAnchor anchor, int pictureIndex)
/* 170:    */   {
/* 171:216 */     return createPicture((XSSFClientAnchor)anchor, pictureIndex);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public XSSFChart createChart(XSSFClientAnchor anchor)
/* 175:    */   {
/* 176:227 */     int chartNumber = getPackagePart().getPackage().getPartsByContentType(XSSFRelation.CHART.getContentType()).size() + 1;
/* 177:    */     
/* 178:    */ 
/* 179:230 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.CHART, XSSFFactory.getInstance(), chartNumber, false);
/* 180:    */     
/* 181:232 */     XSSFChart chart = (XSSFChart)rp.getDocumentPart();
/* 182:233 */     String chartRelId = rp.getRelationship().getId();
/* 183:    */     
/* 184:235 */     XSSFGraphicFrame frame = createGraphicFrame(anchor);
/* 185:236 */     frame.setChart(chart, chartRelId);
/* 186:237 */     frame.getCTGraphicalObjectFrame().setXfrm(createXfrm(anchor));
/* 187:    */     
/* 188:239 */     return chart;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public XSSFChart createChart(ClientAnchor anchor)
/* 192:    */   {
/* 193:244 */     return createChart((XSSFClientAnchor)anchor);
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected PackageRelationship addPictureReference(int pictureIndex)
/* 197:    */   {
/* 198:255 */     XSSFWorkbook wb = (XSSFWorkbook)getParent().getParent();
/* 199:256 */     XSSFPictureData data = (XSSFPictureData)wb.getAllPictures().get(pictureIndex);
/* 200:257 */     XSSFPictureData pic = new XSSFPictureData(data.getPackagePart());
/* 201:258 */     POIXMLDocumentPart.RelationPart rp = addRelation(null, XSSFRelation.IMAGES, pic);
/* 202:259 */     return rp.getRelationship();
/* 203:    */   }
/* 204:    */   
/* 205:    */   public XSSFSimpleShape createSimpleShape(XSSFClientAnchor anchor)
/* 206:    */   {
/* 207:272 */     long shapeId = newShapeId();
/* 208:273 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 209:274 */     org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape ctShape = ctAnchor.addNewSp();
/* 210:275 */     ctShape.set(XSSFSimpleShape.prototype());
/* 211:276 */     ctShape.getNvSpPr().getCNvPr().setId(shapeId);
/* 212:277 */     ctShape.getSpPr().setXfrm(createXfrm(anchor));
/* 213:278 */     XSSFSimpleShape shape = new XSSFSimpleShape(this, ctShape);
/* 214:279 */     shape.anchor = anchor;
/* 215:280 */     return shape;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public XSSFConnector createConnector(XSSFClientAnchor anchor)
/* 219:    */   {
/* 220:293 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 221:294 */     CTConnector ctShape = ctAnchor.addNewCxnSp();
/* 222:295 */     ctShape.set(XSSFConnector.prototype());
/* 223:    */     
/* 224:297 */     XSSFConnector shape = new XSSFConnector(this, ctShape);
/* 225:298 */     shape.anchor = anchor;
/* 226:299 */     return shape;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public XSSFShapeGroup createGroup(XSSFClientAnchor anchor)
/* 230:    */   {
/* 231:312 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 232:313 */     CTGroupShape ctGroup = ctAnchor.addNewGrpSp();
/* 233:314 */     ctGroup.set(XSSFShapeGroup.prototype());
/* 234:315 */     CTTransform2D xfrm = createXfrm(anchor);
/* 235:316 */     CTGroupTransform2D grpXfrm = ctGroup.getGrpSpPr().getXfrm();
/* 236:317 */     grpXfrm.setOff(xfrm.getOff());
/* 237:318 */     grpXfrm.setExt(xfrm.getExt());
/* 238:319 */     grpXfrm.setChExt(xfrm.getExt());
/* 239:    */     
/* 240:321 */     XSSFShapeGroup shape = new XSSFShapeGroup(this, ctGroup);
/* 241:322 */     shape.anchor = anchor;
/* 242:323 */     return shape;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public XSSFComment createCellComment(ClientAnchor anchor)
/* 246:    */   {
/* 247:334 */     XSSFClientAnchor ca = (XSSFClientAnchor)anchor;
/* 248:335 */     XSSFSheet sheet = getSheet();
/* 249:    */     
/* 250:    */ 
/* 251:338 */     CommentsTable comments = sheet.getCommentsTable(true);
/* 252:339 */     XSSFVMLDrawing vml = sheet.getVMLDrawing(true);
/* 253:340 */     com.microsoft.schemas.vml.CTShape vmlShape = vml.newCommentShape();
/* 254:341 */     if (ca.isSet())
/* 255:    */     {
/* 256:344 */       int dx1Pixels = ca.getDx1() / 9525;
/* 257:345 */       int dy1Pixels = ca.getDy1() / 9525;
/* 258:346 */       int dx2Pixels = ca.getDx2() / 9525;
/* 259:347 */       int dy2Pixels = ca.getDy2() / 9525;
/* 260:348 */       String position = ca.getCol1() + ", " + dx1Pixels + ", " + ca.getRow1() + ", " + dy1Pixels + ", " + ca.getCol2() + ", " + dx2Pixels + ", " + ca.getRow2() + ", " + dy2Pixels;
/* 261:    */       
/* 262:    */ 
/* 263:    */ 
/* 264:    */ 
/* 265:353 */       vmlShape.getClientDataArray(0).setAnchorArray(0, position);
/* 266:    */     }
/* 267:355 */     CellAddress ref = new CellAddress(ca.getRow1(), ca.getCol1());
/* 268:357 */     if (comments.findCellComment(ref) != null) {
/* 269:358 */       throw new IllegalArgumentException("Multiple cell comments in one cell are not allowed, cell: " + ref);
/* 270:    */     }
/* 271:361 */     return new XSSFComment(comments, comments.newComment(ref), vmlShape);
/* 272:    */   }
/* 273:    */   
/* 274:    */   private XSSFGraphicFrame createGraphicFrame(XSSFClientAnchor anchor)
/* 275:    */   {
/* 276:372 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
/* 277:373 */     CTGraphicalObjectFrame ctGraphicFrame = ctAnchor.addNewGraphicFrame();
/* 278:374 */     ctGraphicFrame.set(XSSFGraphicFrame.prototype());
/* 279:375 */     ctGraphicFrame.setXfrm(createXfrm(anchor));
/* 280:    */     
/* 281:377 */     long frameId = this.numOfGraphicFrames++;
/* 282:378 */     XSSFGraphicFrame graphicFrame = new XSSFGraphicFrame(this, ctGraphicFrame);
/* 283:379 */     graphicFrame.setAnchor(anchor);
/* 284:380 */     graphicFrame.setId(frameId);
/* 285:381 */     graphicFrame.setName("Diagramm" + frameId);
/* 286:382 */     return graphicFrame;
/* 287:    */   }
/* 288:    */   
/* 289:    */   public XSSFObjectData createObjectData(ClientAnchor anchor, int storageId, int pictureIndex)
/* 290:    */   {
/* 291:387 */     XSSFSheet sh = getSheet();
/* 292:388 */     PackagePart sheetPart = sh.getPackagePart();
/* 293:    */     
/* 294:    */ 
/* 295:    */ 
/* 296:    */ 
/* 297:    */ 
/* 298:    */ 
/* 299:    */ 
/* 300:    */ 
/* 301:    */ 
/* 302:    */ 
/* 303:    */ 
/* 304:    */ 
/* 305:    */ 
/* 306:    */ 
/* 307:403 */     XSSFSheet sheet = getSheet();
/* 308:404 */     XSSFWorkbook wb = sheet.getWorkbook();
/* 309:405 */     int sheetIndex = wb.getSheetIndex(sheet);
/* 310:406 */     long shapeId = (sheetIndex + 1) * 1024 + newShapeId();
/* 311:    */     PackagePartName olePN;
/* 312:    */     try
/* 313:    */     {
/* 314:411 */       olePN = PackagingURIHelper.createPartName("/xl/embeddings/oleObject" + storageId + ".bin");
/* 315:    */     }
/* 316:    */     catch (InvalidFormatException e)
/* 317:    */     {
/* 318:413 */       throw new POIXMLException(e);
/* 319:    */     }
/* 320:415 */     PackageRelationship olePR = sheetPart.addRelationship(olePN, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject");
/* 321:    */     
/* 322:    */ 
/* 323:418 */     XSSFPictureData imgPD = (XSSFPictureData)sh.getWorkbook().getAllPictures().get(pictureIndex);
/* 324:419 */     PackagePartName imgPN = imgPD.getPackagePart().getPartName();
/* 325:420 */     PackageRelationship imgSheetPR = sheetPart.addRelationship(imgPN, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
/* 326:421 */     PackageRelationship imgDrawPR = getPackagePart().addRelationship(imgPN, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
/* 327:    */     
/* 328:    */ 
/* 329:    */ 
/* 330:425 */     CTWorksheet cwb = sh.getCTWorksheet();
/* 331:426 */     CTOleObjects oo = cwb.isSetOleObjects() ? cwb.getOleObjects() : cwb.addNewOleObjects();
/* 332:    */     
/* 333:428 */     CTOleObject ole1 = oo.addNewOleObject();
/* 334:429 */     ole1.setProgId("Package");
/* 335:430 */     ole1.setShapeId(shapeId);
/* 336:431 */     ole1.setId(olePR.getId());
/* 337:    */     
/* 338:433 */     XmlCursor cur1 = ole1.newCursor();
/* 339:434 */     cur1.toEndToken();
/* 340:435 */     cur1.beginElement("objectPr", "http://schemas.openxmlformats.org/spreadsheetml/2006/main");
/* 341:436 */     cur1.insertAttributeWithValue("id", "http://schemas.openxmlformats.org/officeDocument/2006/relationships", imgSheetPR.getId());
/* 342:437 */     cur1.insertAttributeWithValue("defaultSize", "0");
/* 343:438 */     cur1.beginElement("anchor", "http://schemas.openxmlformats.org/spreadsheetml/2006/main");
/* 344:439 */     cur1.insertAttributeWithValue("moveWithCells", "1");
/* 345:    */     
/* 346:441 */     CTTwoCellAnchor ctAnchor = createTwoCellAnchor((XSSFClientAnchor)anchor);
/* 347:    */     
/* 348:443 */     XmlCursor cur2 = ctAnchor.newCursor();
/* 349:444 */     cur2.copyXmlContents(cur1);
/* 350:445 */     cur2.dispose();
/* 351:    */     
/* 352:447 */     cur1.toParent();
/* 353:448 */     cur1.toFirstChild();
/* 354:449 */     cur1.setName(new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "from"));
/* 355:450 */     cur1.toNextSibling();
/* 356:451 */     cur1.setName(new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "to"));
/* 357:    */     
/* 358:453 */     cur1.dispose();
/* 359:    */     
/* 360:    */ 
/* 361:456 */     org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape ctShape = ctAnchor.addNewSp();
/* 362:457 */     ctShape.set(XSSFObjectData.prototype());
/* 363:458 */     ctShape.getSpPr().setXfrm(createXfrm((XSSFClientAnchor)anchor));
/* 364:    */     
/* 365:    */ 
/* 366:461 */     CTBlipFillProperties blipFill = ctShape.getSpPr().addNewBlipFill();
/* 367:462 */     blipFill.addNewBlip().setEmbed(imgDrawPR.getId());
/* 368:463 */     blipFill.addNewStretch().addNewFillRect();
/* 369:    */     
/* 370:465 */     CTNonVisualDrawingProps cNvPr = ctShape.getNvSpPr().getCNvPr();
/* 371:466 */     cNvPr.setId(shapeId);
/* 372:467 */     cNvPr.setName("Object " + shapeId);
/* 373:    */     
/* 374:469 */     XmlCursor extCur = cNvPr.getExtLst().getExtArray(0).newCursor();
/* 375:470 */     extCur.toFirstChild();
/* 376:471 */     extCur.setAttributeText(new QName("spid"), "_x0000_s" + shapeId);
/* 377:472 */     extCur.dispose();
/* 378:    */     
/* 379:474 */     XSSFObjectData shape = new XSSFObjectData(this, ctShape);
/* 380:475 */     shape.anchor = ((XSSFClientAnchor)anchor);
/* 381:    */     
/* 382:477 */     return shape;
/* 383:    */   }
/* 384:    */   
/* 385:    */   public List<XSSFChart> getCharts()
/* 386:    */   {
/* 387:485 */     List<XSSFChart> charts = new ArrayList();
/* 388:486 */     for (POIXMLDocumentPart part : getRelations()) {
/* 389:487 */       if ((part instanceof XSSFChart)) {
/* 390:488 */         charts.add((XSSFChart)part);
/* 391:    */       }
/* 392:    */     }
/* 393:491 */     return charts;
/* 394:    */   }
/* 395:    */   
/* 396:    */   private CTTwoCellAnchor createTwoCellAnchor(XSSFClientAnchor anchor)
/* 397:    */   {
/* 398:500 */     CTTwoCellAnchor ctAnchor = this.drawing.addNewTwoCellAnchor();
/* 399:501 */     ctAnchor.setFrom(anchor.getFrom());
/* 400:502 */     ctAnchor.setTo(anchor.getTo());
/* 401:503 */     ctAnchor.addNewClientData();
/* 402:504 */     anchor.setTo(ctAnchor.getTo());
/* 403:505 */     anchor.setFrom(ctAnchor.getFrom());
/* 404:    */     STEditAs.Enum aditAs;
/* 405:507 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$ClientAnchor$AnchorType[anchor.getAnchorType().ordinal()])
/* 406:    */     {
/* 407:    */     case 1: 
/* 408:508 */       aditAs = STEditAs.ABSOLUTE; break;
/* 409:    */     case 2: 
/* 410:509 */       aditAs = STEditAs.TWO_CELL; break;
/* 411:    */     case 3: 
/* 412:510 */       aditAs = STEditAs.ONE_CELL; break;
/* 413:    */     default: 
/* 414:511 */       aditAs = STEditAs.ONE_CELL;
/* 415:    */     }
/* 416:513 */     ctAnchor.setEditAs(aditAs);
/* 417:514 */     return ctAnchor;
/* 418:    */   }
/* 419:    */   
/* 420:    */   private CTTransform2D createXfrm(XSSFClientAnchor anchor)
/* 421:    */   {
/* 422:518 */     CTTransform2D xfrm = CTTransform2D.Factory.newInstance();
/* 423:519 */     CTPoint2D off = xfrm.addNewOff();
/* 424:520 */     off.setX(anchor.getDx1());
/* 425:521 */     off.setY(anchor.getDy1());
/* 426:522 */     XSSFSheet sheet = getSheet();
/* 427:523 */     double widthPx = 0.0D;
/* 428:524 */     for (int col = anchor.getCol1(); col < anchor.getCol2(); col++) {
/* 429:525 */       widthPx += sheet.getColumnWidthInPixels(col);
/* 430:    */     }
/* 431:527 */     double heightPx = 0.0D;
/* 432:528 */     for (int row = anchor.getRow1(); row < anchor.getRow2(); row++) {
/* 433:529 */       heightPx += ImageUtils.getRowHeightInPixels(sheet, row);
/* 434:    */     }
/* 435:531 */     long width = Units.pixelToEMU((int)widthPx);
/* 436:532 */     long height = Units.pixelToEMU((int)heightPx);
/* 437:533 */     CTPositiveSize2D ext = xfrm.addNewExt();
/* 438:534 */     ext.setCx(width - anchor.getDx1() + anchor.getDx2());
/* 439:535 */     ext.setCy(height - anchor.getDy1() + anchor.getDy2());
/* 440:    */     
/* 441:    */ 
/* 442:538 */     return xfrm;
/* 443:    */   }
/* 444:    */   
/* 445:    */   private long newShapeId()
/* 446:    */   {
/* 447:542 */     return 1 + this.drawing.sizeOfAbsoluteAnchorArray() + this.drawing.sizeOfOneCellAnchorArray() + this.drawing.sizeOfTwoCellAnchorArray();
/* 448:    */   }
/* 449:    */   
/* 450:    */   public List<XSSFShape> getShapes()
/* 451:    */   {
/* 452:552 */     List<XSSFShape> lst = new ArrayList();
/* 453:553 */     XmlCursor cur = this.drawing.newCursor();
/* 454:    */     try
/* 455:    */     {
/* 456:555 */       if (cur.toFirstChild()) {
/* 457:556 */         addShapes(cur, lst);
/* 458:    */       }
/* 459:    */     }
/* 460:    */     finally
/* 461:    */     {
/* 462:559 */       cur.dispose();
/* 463:    */     }
/* 464:561 */     return lst;
/* 465:    */   }
/* 466:    */   
/* 467:    */   public List<XSSFShape> getShapes(XSSFShapeGroup groupshape)
/* 468:    */   {
/* 469:568 */     List<XSSFShape> lst = new ArrayList();
/* 470:569 */     XmlCursor cur = groupshape.getCTGroupShape().newCursor();
/* 471:    */     try
/* 472:    */     {
/* 473:571 */       addShapes(cur, lst);
/* 474:    */     }
/* 475:    */     finally
/* 476:    */     {
/* 477:573 */       cur.dispose();
/* 478:    */     }
/* 479:575 */     return lst;
/* 480:    */   }
/* 481:    */   
/* 482:    */   private void addShapes(XmlCursor cur, List<XSSFShape> lst)
/* 483:    */   {
/* 484:    */     try
/* 485:    */     {
/* 486:    */       do
/* 487:    */       {
/* 488:581 */         cur.push();
/* 489:582 */         if (cur.toFirstChild()) {
/* 490:    */           do
/* 491:    */           {
/* 492:584 */             XmlObject obj = cur.getObject();
/* 493:587 */             if (!(obj instanceof CTMarker))
/* 494:    */             {
/* 495:    */               XSSFShape shape;
/* 496:590 */               if ((obj instanceof CTPicture))
/* 497:    */               {
/* 498:591 */                 shape = new XSSFPicture(this, (CTPicture)obj);
/* 499:    */               }
/* 500:    */               else
/* 501:    */               {
/* 502:    */                 XSSFShape shape;
/* 503:592 */                 if ((obj instanceof CTConnector))
/* 504:    */                 {
/* 505:593 */                   shape = new XSSFConnector(this, (CTConnector)obj);
/* 506:    */                 }
/* 507:    */                 else
/* 508:    */                 {
/* 509:    */                   XSSFShape shape;
/* 510:594 */                   if ((obj instanceof org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape))
/* 511:    */                   {
/* 512:595 */                     shape = hasOleLink(obj) ? new XSSFObjectData(this, (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)obj) : new XSSFSimpleShape(this, (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)obj);
/* 513:    */                   }
/* 514:    */                   else
/* 515:    */                   {
/* 516:    */                     XSSFShape shape;
/* 517:598 */                     if ((obj instanceof CTGraphicalObjectFrame))
/* 518:    */                     {
/* 519:599 */                       shape = new XSSFGraphicFrame(this, (CTGraphicalObjectFrame)obj);
/* 520:    */                     }
/* 521:    */                     else
/* 522:    */                     {
/* 523:    */                       XSSFShape shape;
/* 524:600 */                       if ((obj instanceof CTGroupShape))
/* 525:    */                       {
/* 526:601 */                         shape = new XSSFShapeGroup(this, (CTGroupShape)obj);
/* 527:    */                       }
/* 528:    */                       else
/* 529:    */                       {
/* 530:602 */                         if (!(obj instanceof XmlAnyTypeImpl)) {
/* 531:    */                           continue;
/* 532:    */                         }
/* 533:603 */                         LOG.log(5, new Object[] { "trying to parse AlternateContent, this unlinks the returned Shapes from the underlying xml content, so those shapes can't be used to modify the drawing, i.e. modifications will be ignored!" });
/* 534:    */                         
/* 535:    */ 
/* 536:    */ 
/* 537:    */ 
/* 538:    */ 
/* 539:609 */                         cur.push();
/* 540:610 */                         cur.toFirstChild();
/* 541:611 */                         XmlCursor cur2 = null;
/* 542:    */                         try
/* 543:    */                         {
/* 544:615 */                           CTDrawing alterWS = CTDrawing.Factory.parse(cur.newXMLStreamReader());
/* 545:616 */                           cur2 = alterWS.newCursor();
/* 546:617 */                           if (cur2.toFirstChild()) {
/* 547:618 */                             addShapes(cur2, lst);
/* 548:    */                           }
/* 549:    */                         }
/* 550:    */                         catch (XmlException e)
/* 551:    */                         {
/* 552:621 */                           LOG.log(5, new Object[] { "unable to parse CTDrawing in alternate content.", e });
/* 553:    */                         }
/* 554:    */                         finally
/* 555:    */                         {
/* 556:623 */                           if (cur2 != null) {
/* 557:624 */                             cur2.dispose();
/* 558:    */                           }
/* 559:626 */                           cur.pop();
/* 560:    */                         }
/* 561:628 */                         continue;
/* 562:    */                       }
/* 563:    */                     }
/* 564:    */                   }
/* 565:    */                 }
/* 566:    */               }
/* 567:    */               XSSFShape shape;
/* 568:634 */               assert (shape != null);
/* 569:635 */               shape.anchor = getAnchorFromParent(obj);
/* 570:636 */               lst.add(shape);
/* 571:    */             }
/* 572:638 */           } while (cur.toNextSibling());
/* 573:    */         }
/* 574:640 */         cur.pop();
/* 575:641 */       } while (cur.toNextSibling());
/* 576:    */     }
/* 577:    */     finally
/* 578:    */     {
/* 579:643 */       cur.dispose();
/* 580:    */     }
/* 581:    */   }
/* 582:    */   
/* 583:    */   private boolean hasOleLink(XmlObject shape)
/* 584:    */   {
/* 585:648 */     QName uriName = new QName(null, "uri");
/* 586:649 */     String xquery = "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//a:extLst/a:ext";
/* 587:650 */     XmlCursor cur = shape.newCursor();
/* 588:651 */     cur.selectPath(xquery);
/* 589:    */     try
/* 590:    */     {
/* 591:653 */       while (cur.toNextSelection())
/* 592:    */       {
/* 593:654 */         String uri = cur.getAttributeText(uriName);
/* 594:655 */         if ("{63B3BB69-23CF-44E3-9099-C40C66FF867C}".equals(uri)) {
/* 595:656 */           return true;
/* 596:    */         }
/* 597:    */       }
/* 598:    */     }
/* 599:    */     finally
/* 600:    */     {
/* 601:660 */       cur.dispose();
/* 602:    */     }
/* 603:662 */     return false;
/* 604:    */   }
/* 605:    */   
/* 606:    */   private XSSFAnchor getAnchorFromParent(XmlObject obj)
/* 607:    */   {
/* 608:666 */     XSSFAnchor anchor = null;
/* 609:    */     
/* 610:668 */     XmlObject parentXbean = null;
/* 611:669 */     XmlCursor cursor = obj.newCursor();
/* 612:670 */     if (cursor.toParent()) {
/* 613:671 */       parentXbean = cursor.getObject();
/* 614:    */     }
/* 615:673 */     cursor.dispose();
/* 616:674 */     if (parentXbean != null) {
/* 617:675 */       if ((parentXbean instanceof CTTwoCellAnchor))
/* 618:    */       {
/* 619:676 */         CTTwoCellAnchor ct = (CTTwoCellAnchor)parentXbean;
/* 620:677 */         anchor = new XSSFClientAnchor(ct.getFrom(), ct.getTo());
/* 621:    */       }
/* 622:678 */       else if ((parentXbean instanceof CTOneCellAnchor))
/* 623:    */       {
/* 624:679 */         CTOneCellAnchor ct = (CTOneCellAnchor)parentXbean;
/* 625:680 */         anchor = new XSSFClientAnchor(getSheet(), ct.getFrom(), ct.getExt());
/* 626:    */       }
/* 627:681 */       else if ((parentXbean instanceof CTAbsoluteAnchor))
/* 628:    */       {
/* 629:682 */         CTAbsoluteAnchor ct = (CTAbsoluteAnchor)parentXbean;
/* 630:683 */         anchor = new XSSFClientAnchor(getSheet(), ct.getPos(), ct.getExt());
/* 631:    */       }
/* 632:    */     }
/* 633:686 */     return anchor;
/* 634:    */   }
/* 635:    */   
/* 636:    */   public Iterator<XSSFShape> iterator()
/* 637:    */   {
/* 638:691 */     return getShapes().iterator();
/* 639:    */   }
/* 640:    */   
/* 641:    */   public XSSFSheet getSheet()
/* 642:    */   {
/* 643:698 */     return (XSSFSheet)getParent();
/* 644:    */   }
/* 645:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFDrawing
 * JD-Core Version:    0.7.0.1
 */