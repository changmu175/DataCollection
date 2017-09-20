/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import javax.xml.namespace.QName;
/*  13:    */ import org.apache.poi.POIXMLDocumentPart;
/*  14:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  15:    */ import org.apache.poi.POIXMLException;
/*  16:    */ import org.apache.poi.POIXMLTypeLoader;
/*  17:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  18:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  20:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  21:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  22:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  23:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  24:    */ import org.apache.poi.sl.draw.DrawPictureShape;
/*  25:    */ import org.apache.poi.sl.draw.Drawable;
/*  26:    */ import org.apache.poi.sl.usermodel.PictureData;
/*  27:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  28:    */ import org.apache.poi.sl.usermodel.Sheet;
/*  29:    */ import org.apache.poi.util.IOUtils;
/*  30:    */ import org.apache.poi.util.Internal;
/*  31:    */ import org.apache.poi.util.POILogFactory;
/*  32:    */ import org.apache.poi.util.POILogger;
/*  33:    */ import org.apache.poi.util.Removal;
/*  34:    */ import org.apache.xmlbeans.XmlCursor;
/*  35:    */ import org.apache.xmlbeans.XmlException;
/*  36:    */ import org.apache.xmlbeans.XmlObject;
/*  37:    */ import org.apache.xmlbeans.XmlOptions;
/*  38:    */ import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
/*  39:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  40:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector;
/*  41:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*  42:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  43:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape.Factory;
/*  44:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
/*  45:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  46:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  47:    */ import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum;
/*  48:    */ 
/*  49:    */ public abstract class XSLFSheet
/*  50:    */   extends POIXMLDocumentPart
/*  51:    */   implements XSLFShapeContainer, Sheet<XSLFShape, XSLFTextParagraph>
/*  52:    */ {
/*  53: 70 */   private static POILogger LOG = POILogFactory.getLogger(XSLFSheet.class);
/*  54:    */   private XSLFCommonSlideData _commonSlideData;
/*  55:    */   private XSLFDrawing _drawing;
/*  56:    */   private List<XSLFShape> _shapes;
/*  57:    */   private CTGroupShape _spTree;
/*  58:    */   private List<XSLFTextShape> _placeholders;
/*  59:    */   private Map<Integer, XSLFSimpleShape> _placeholderByIdMap;
/*  60:    */   private Map<Integer, XSLFSimpleShape> _placeholderByTypeMap;
/*  61:    */   
/*  62:    */   public XSLFSheet() {}
/*  63:    */   
/*  64:    */   public XSLFSheet(PackagePart part)
/*  65:    */   {
/*  66: 89 */     super(part);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public XMLSlideShow getSlideShow()
/*  70:    */   {
/*  71: 97 */     POIXMLDocumentPart p = getParent();
/*  72: 98 */     while (p != null)
/*  73:    */     {
/*  74: 99 */       if ((p instanceof XMLSlideShow)) {
/*  75:100 */         return (XMLSlideShow)p;
/*  76:    */       }
/*  77:102 */       p = p.getParent();
/*  78:    */     }
/*  79:104 */     throw new IllegalStateException("SlideShow was not found");
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected static List<XSLFShape> buildShapes(CTGroupShape spTree, XSLFSheet sheet)
/*  83:    */   {
/*  84:108 */     List<XSLFShape> shapes = new ArrayList();
/*  85:109 */     XmlCursor cur = spTree.newCursor();
/*  86:    */     try
/*  87:    */     {
/*  88:111 */       for (boolean b = cur.toFirstChild(); b; b = cur.toNextSibling())
/*  89:    */       {
/*  90:112 */         XmlObject ch = cur.getObject();
/*  91:113 */         if ((ch instanceof CTShape))
/*  92:    */         {
/*  93:115 */           XSLFAutoShape shape = XSLFAutoShape.create((CTShape)ch, sheet);
/*  94:116 */           shapes.add(shape);
/*  95:    */         }
/*  96:117 */         else if ((ch instanceof CTGroupShape))
/*  97:    */         {
/*  98:118 */           shapes.add(new XSLFGroupShape((CTGroupShape)ch, sheet));
/*  99:    */         }
/* 100:119 */         else if ((ch instanceof CTConnector))
/* 101:    */         {
/* 102:120 */           shapes.add(new XSLFConnectorShape((CTConnector)ch, sheet));
/* 103:    */         }
/* 104:121 */         else if ((ch instanceof CTPicture))
/* 105:    */         {
/* 106:122 */           shapes.add(new XSLFPictureShape((CTPicture)ch, sheet));
/* 107:    */         }
/* 108:123 */         else if ((ch instanceof CTGraphicalObjectFrame))
/* 109:    */         {
/* 110:124 */           XSLFGraphicFrame shape = XSLFGraphicFrame.create((CTGraphicalObjectFrame)ch, sheet);
/* 111:125 */           shapes.add(shape);
/* 112:    */         }
/* 113:126 */         else if ((ch instanceof XmlAnyTypeImpl))
/* 114:    */         {
/* 115:131 */           cur.push();
/* 116:132 */           if ((cur.toChild("http://schemas.openxmlformats.org/markup-compatibility/2006", "Choice")) && (cur.toFirstChild())) {
/* 117:    */             try
/* 118:    */             {
/* 119:134 */               CTGroupShape grp = CTGroupShape.Factory.parse(cur.newXMLStreamReader());
/* 120:135 */               shapes.addAll(buildShapes(grp, sheet));
/* 121:    */             }
/* 122:    */             catch (XmlException e)
/* 123:    */             {
/* 124:137 */               LOG.log(1, new Object[] { "unparsable alternate content", e });
/* 125:    */             }
/* 126:    */           }
/* 127:140 */           cur.pop();
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:    */     finally
/* 132:    */     {
/* 133:144 */       cur.dispose();
/* 134:    */     }
/* 135:147 */     return shapes;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public abstract XmlObject getXmlObject();
/* 139:    */   
/* 140:    */   @Removal(version="3.18")
/* 141:    */   @Internal
/* 142:    */   public XSLFCommonSlideData getCommonSlideData()
/* 143:    */   {
/* 144:161 */     return this._commonSlideData;
/* 145:    */   }
/* 146:    */   
/* 147:    */   @Removal(version="3.18")
/* 148:    */   protected void setCommonSlideData(CTCommonSlideData data)
/* 149:    */   {
/* 150:169 */     if (data == null) {
/* 151:170 */       this._commonSlideData = null;
/* 152:    */     } else {
/* 153:172 */       this._commonSlideData = new XSLFCommonSlideData(data);
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   private XSLFDrawing getDrawing()
/* 158:    */   {
/* 159:177 */     initDrawingAndShapes();
/* 160:178 */     return this._drawing;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public List<XSLFShape> getShapes()
/* 164:    */   {
/* 165:188 */     initDrawingAndShapes();
/* 166:189 */     return this._shapes;
/* 167:    */   }
/* 168:    */   
/* 169:    */   private void initDrawingAndShapes()
/* 170:    */   {
/* 171:199 */     CTGroupShape cgs = getSpTree();
/* 172:200 */     if (this._drawing == null) {
/* 173:201 */       this._drawing = new XSLFDrawing(this, cgs);
/* 174:    */     }
/* 175:203 */     if (this._shapes == null) {
/* 176:204 */       this._shapes = buildShapes(cgs, this);
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:    */   public XSLFAutoShape createAutoShape()
/* 181:    */   {
/* 182:212 */     XSLFAutoShape sh = getDrawing().createAutoShape();
/* 183:213 */     getShapes().add(sh);
/* 184:214 */     sh.setParent(this);
/* 185:215 */     return sh;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public XSLFFreeformShape createFreeform()
/* 189:    */   {
/* 190:220 */     XSLFFreeformShape sh = getDrawing().createFreeform();
/* 191:221 */     getShapes().add(sh);
/* 192:222 */     sh.setParent(this);
/* 193:223 */     return sh;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public XSLFTextBox createTextBox()
/* 197:    */   {
/* 198:228 */     XSLFTextBox sh = getDrawing().createTextBox();
/* 199:229 */     getShapes().add(sh);
/* 200:230 */     sh.setParent(this);
/* 201:231 */     return sh;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public XSLFConnectorShape createConnector()
/* 205:    */   {
/* 206:236 */     XSLFConnectorShape sh = getDrawing().createConnector();
/* 207:237 */     getShapes().add(sh);
/* 208:238 */     sh.setParent(this);
/* 209:239 */     return sh;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public XSLFGroupShape createGroup()
/* 213:    */   {
/* 214:244 */     XSLFGroupShape sh = getDrawing().createGroup();
/* 215:245 */     getShapes().add(sh);
/* 216:246 */     sh.setParent(this);
/* 217:247 */     return sh;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public XSLFPictureShape createPicture(PictureData pictureData)
/* 221:    */   {
/* 222:252 */     if (!(pictureData instanceof XSLFPictureData)) {
/* 223:253 */       throw new IllegalArgumentException("pictureData needs to be of type XSLFPictureData");
/* 224:    */     }
/* 225:255 */     XSLFPictureData xPictureData = (XSLFPictureData)pictureData;
/* 226:256 */     PackagePart pic = xPictureData.getPackagePart();
/* 227:    */     
/* 228:258 */     POIXMLDocumentPart.RelationPart rp = addRelation(null, XSLFRelation.IMAGES, new XSLFPictureData(pic));
/* 229:    */     
/* 230:260 */     XSLFPictureShape sh = getDrawing().createPicture(rp.getRelationship().getId());
/* 231:261 */     new DrawPictureShape(sh).resize();
/* 232:262 */     getShapes().add(sh);
/* 233:263 */     sh.setParent(this);
/* 234:264 */     return sh;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public XSLFTable createTable()
/* 238:    */   {
/* 239:268 */     XSLFTable sh = getDrawing().createTable();
/* 240:269 */     getShapes().add(sh);
/* 241:270 */     sh.setParent(this);
/* 242:271 */     return sh;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public XSLFTable createTable(int numRows, int numCols)
/* 246:    */   {
/* 247:276 */     if ((numRows < 1) || (numCols < 1)) {
/* 248:277 */       throw new IllegalArgumentException("numRows and numCols must be greater than 0");
/* 249:    */     }
/* 250:279 */     XSLFTable sh = getDrawing().createTable();
/* 251:280 */     getShapes().add(sh);
/* 252:281 */     sh.setParent(this);
/* 253:282 */     for (int r = 0; r < numRows; r++)
/* 254:    */     {
/* 255:283 */       XSLFTableRow row = sh.addRow();
/* 256:284 */       for (int c = 0; c < numCols; c++) {
/* 257:285 */         row.addCell();
/* 258:    */       }
/* 259:    */     }
/* 260:288 */     return sh;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public Iterator<XSLFShape> iterator()
/* 264:    */   {
/* 265:299 */     return getShapes().iterator();
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void addShape(XSLFShape shape)
/* 269:    */   {
/* 270:304 */     throw new UnsupportedOperationException("Adding a shape from a different container is not supported - create it from scratch witht XSLFSheet.create* methods");
/* 271:    */   }
/* 272:    */   
/* 273:    */   public boolean removeShape(XSLFShape xShape)
/* 274:    */   {
/* 275:321 */     XmlObject obj = xShape.getXmlObject();
/* 276:322 */     CTGroupShape spTree = getSpTree();
/* 277:323 */     if ((obj instanceof CTShape))
/* 278:    */     {
/* 279:324 */       spTree.getSpList().remove(obj);
/* 280:    */     }
/* 281:325 */     else if ((obj instanceof CTGroupShape))
/* 282:    */     {
/* 283:326 */       spTree.getGrpSpList().remove(obj);
/* 284:    */     }
/* 285:327 */     else if ((obj instanceof CTConnector))
/* 286:    */     {
/* 287:328 */       spTree.getCxnSpList().remove(obj);
/* 288:    */     }
/* 289:329 */     else if ((obj instanceof CTGraphicalObjectFrame))
/* 290:    */     {
/* 291:330 */       spTree.getGraphicFrameList().remove(obj);
/* 292:    */     }
/* 293:331 */     else if ((obj instanceof CTPicture))
/* 294:    */     {
/* 295:332 */       XSLFPictureShape ps = (XSLFPictureShape)xShape;
/* 296:333 */       removePictureRelation(ps);
/* 297:334 */       spTree.getPicList().remove(obj);
/* 298:    */     }
/* 299:    */     else
/* 300:    */     {
/* 301:336 */       throw new IllegalArgumentException("Unsupported shape: " + xShape);
/* 302:    */     }
/* 303:338 */     return getShapes().remove(xShape);
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void clear()
/* 307:    */   {
/* 308:347 */     List<XSLFShape> shapes = new ArrayList(getShapes());
/* 309:348 */     for (XSLFShape shape : shapes) {
/* 310:349 */       removeShape(shape);
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   protected abstract String getRootElementName();
/* 315:    */   
/* 316:    */   protected CTGroupShape getSpTree()
/* 317:    */   {
/* 318:356 */     if (this._spTree == null)
/* 319:    */     {
/* 320:357 */       XmlObject root = getXmlObject();
/* 321:358 */       XmlObject[] sp = root.selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:spTree");
/* 322:360 */       if (sp.length == 0) {
/* 323:361 */         throw new IllegalStateException("CTGroupShape was not found");
/* 324:    */       }
/* 325:363 */       this._spTree = ((CTGroupShape)sp[0]);
/* 326:    */     }
/* 327:365 */     return this._spTree;
/* 328:    */   }
/* 329:    */   
/* 330:    */   protected final void commit()
/* 331:    */     throws IOException
/* 332:    */   {
/* 333:370 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 334:371 */     String docName = getRootElementName();
/* 335:372 */     if (docName != null) {
/* 336:373 */       xmlOptions.setSaveSyntheticDocumentElement(new QName("http://schemas.openxmlformats.org/presentationml/2006/main", docName));
/* 337:    */     }
/* 338:377 */     PackagePart part = getPackagePart();
/* 339:378 */     OutputStream out = part.getOutputStream();
/* 340:379 */     getXmlObject().save(out, xmlOptions);
/* 341:380 */     out.close();
/* 342:    */   }
/* 343:    */   
/* 344:    */   public XSLFSheet importContent(XSLFSheet src)
/* 345:    */   {
/* 346:392 */     this._shapes = null;
/* 347:393 */     this._spTree = null;
/* 348:394 */     this._drawing = null;
/* 349:395 */     this._spTree = null;
/* 350:396 */     this._placeholders = null;
/* 351:    */     
/* 352:    */ 
/* 353:    */ 
/* 354:    */ 
/* 355:    */ 
/* 356:402 */     getSpTree().set(src.getSpTree());
/* 357:    */     
/* 358:    */ 
/* 359:405 */     List<XSLFShape> tgtShapes = getShapes();
/* 360:406 */     List<XSLFShape> srcShapes = src.getShapes();
/* 361:407 */     for (int i = 0; i < tgtShapes.size(); i++)
/* 362:    */     {
/* 363:408 */       XSLFShape s1 = (XSLFShape)srcShapes.get(i);
/* 364:409 */       XSLFShape s2 = (XSLFShape)tgtShapes.get(i);
/* 365:    */       
/* 366:411 */       s2.copy(s1);
/* 367:    */     }
/* 368:413 */     return this;
/* 369:    */   }
/* 370:    */   
/* 371:    */   public XSLFSheet appendContent(XSLFSheet src)
/* 372:    */   {
/* 373:423 */     CTGroupShape spTree = getSpTree();
/* 374:424 */     int numShapes = getShapes().size();
/* 375:    */     
/* 376:426 */     CTGroupShape srcTree = src.getSpTree();
/* 377:427 */     for (XmlObject ch : srcTree.selectPath("*")) {
/* 378:428 */       if ((ch instanceof CTShape)) {
/* 379:429 */         spTree.addNewSp().set(ch);
/* 380:430 */       } else if ((ch instanceof CTGroupShape)) {
/* 381:431 */         spTree.addNewGrpSp().set(ch);
/* 382:432 */       } else if ((ch instanceof CTConnector)) {
/* 383:433 */         spTree.addNewCxnSp().set(ch);
/* 384:434 */       } else if ((ch instanceof CTPicture)) {
/* 385:435 */         spTree.addNewPic().set(ch);
/* 386:436 */       } else if ((ch instanceof CTGraphicalObjectFrame)) {
/* 387:437 */         spTree.addNewGraphicFrame().set(ch);
/* 388:    */       }
/* 389:    */     }
/* 390:441 */     this._shapes = null;
/* 391:442 */     this._spTree = null;
/* 392:443 */     this._drawing = null;
/* 393:444 */     this._spTree = null;
/* 394:445 */     this._placeholders = null;
/* 395:    */     
/* 396:    */ 
/* 397:448 */     List<XSLFShape> tgtShapes = getShapes();
/* 398:449 */     List<XSLFShape> srcShapes = src.getShapes();
/* 399:450 */     for (int i = 0; i < srcShapes.size(); i++)
/* 400:    */     {
/* 401:451 */       XSLFShape s1 = (XSLFShape)srcShapes.get(i);
/* 402:452 */       XSLFShape s2 = (XSLFShape)tgtShapes.get(numShapes + i);
/* 403:    */       
/* 404:454 */       s2.copy(s1);
/* 405:    */     }
/* 406:456 */     return this;
/* 407:    */   }
/* 408:    */   
/* 409:    */   XSLFTheme getTheme()
/* 410:    */   {
/* 411:466 */     return null;
/* 412:    */   }
/* 413:    */   
/* 414:    */   protected XSLFTextShape getTextShapeByType(Placeholder type)
/* 415:    */   {
/* 416:470 */     for (XSLFShape shape : getShapes()) {
/* 417:471 */       if ((shape instanceof XSLFTextShape))
/* 418:    */       {
/* 419:472 */         XSLFTextShape txt = (XSLFTextShape)shape;
/* 420:473 */         if (txt.getTextType() == type) {
/* 421:474 */           return txt;
/* 422:    */         }
/* 423:    */       }
/* 424:    */     }
/* 425:478 */     return null;
/* 426:    */   }
/* 427:    */   
/* 428:    */   XSLFSimpleShape getPlaceholder(CTPlaceholder ph)
/* 429:    */   {
/* 430:482 */     XSLFSimpleShape shape = null;
/* 431:483 */     if (ph.isSetIdx()) {
/* 432:484 */       shape = getPlaceholderById((int)ph.getIdx());
/* 433:    */     }
/* 434:487 */     if ((shape == null) && (ph.isSetType())) {
/* 435:488 */       shape = getPlaceholderByType(ph.getType().intValue());
/* 436:    */     }
/* 437:490 */     return shape;
/* 438:    */   }
/* 439:    */   
/* 440:    */   void initPlaceholders()
/* 441:    */   {
/* 442:494 */     if (this._placeholders == null)
/* 443:    */     {
/* 444:495 */       this._placeholders = new ArrayList();
/* 445:496 */       this._placeholderByIdMap = new HashMap();
/* 446:497 */       this._placeholderByTypeMap = new HashMap();
/* 447:499 */       for (XSLFShape sh : getShapes()) {
/* 448:500 */         if ((sh instanceof XSLFTextShape))
/* 449:    */         {
/* 450:501 */           XSLFTextShape sShape = (XSLFTextShape)sh;
/* 451:502 */           CTPlaceholder ph = sShape.getCTPlaceholder();
/* 452:503 */           if (ph != null)
/* 453:    */           {
/* 454:504 */             this._placeholders.add(sShape);
/* 455:505 */             if (ph.isSetIdx())
/* 456:    */             {
/* 457:506 */               int idx = (int)ph.getIdx();
/* 458:507 */               this._placeholderByIdMap.put(Integer.valueOf(idx), sShape);
/* 459:    */             }
/* 460:509 */             if (ph.isSetType()) {
/* 461:510 */               this._placeholderByTypeMap.put(Integer.valueOf(ph.getType().intValue()), sShape);
/* 462:    */             }
/* 463:    */           }
/* 464:    */         }
/* 465:    */       }
/* 466:    */     }
/* 467:    */   }
/* 468:    */   
/* 469:    */   XSLFSimpleShape getPlaceholderById(int id)
/* 470:    */   {
/* 471:519 */     initPlaceholders();
/* 472:520 */     return (XSLFSimpleShape)this._placeholderByIdMap.get(Integer.valueOf(id));
/* 473:    */   }
/* 474:    */   
/* 475:    */   XSLFSimpleShape getPlaceholderByType(int ordinal)
/* 476:    */   {
/* 477:524 */     initPlaceholders();
/* 478:525 */     return (XSLFSimpleShape)this._placeholderByTypeMap.get(Integer.valueOf(ordinal));
/* 479:    */   }
/* 480:    */   
/* 481:    */   public XSLFTextShape getPlaceholder(int idx)
/* 482:    */   {
/* 483:534 */     initPlaceholders();
/* 484:535 */     return (XSLFTextShape)this._placeholders.get(idx);
/* 485:    */   }
/* 486:    */   
/* 487:    */   public XSLFTextShape[] getPlaceholders()
/* 488:    */   {
/* 489:543 */     initPlaceholders();
/* 490:544 */     return (XSLFTextShape[])this._placeholders.toArray(new XSLFTextShape[this._placeholders.size()]);
/* 491:    */   }
/* 492:    */   
/* 493:    */   protected boolean canDraw(XSLFShape shape)
/* 494:    */   {
/* 495:554 */     return true;
/* 496:    */   }
/* 497:    */   
/* 498:    */   public boolean getFollowMasterGraphics()
/* 499:    */   {
/* 500:565 */     return false;
/* 501:    */   }
/* 502:    */   
/* 503:    */   public XSLFBackground getBackground()
/* 504:    */   {
/* 505:573 */     return null;
/* 506:    */   }
/* 507:    */   
/* 508:    */   public void draw(Graphics2D graphics)
/* 509:    */   {
/* 510:583 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 511:584 */     Drawable draw = drawFact.getDrawable(this);
/* 512:585 */     draw.draw(graphics);
/* 513:    */   }
/* 514:    */   
/* 515:    */   String importBlip(String blipId, PackagePart packagePart)
/* 516:    */   {
/* 517:596 */     PackageRelationship blipRel = packagePart.getRelationship(blipId);
/* 518:    */     PackagePart blipPart;
/* 519:    */     try
/* 520:    */     {
/* 521:599 */       blipPart = packagePart.getRelatedPart(blipRel);
/* 522:    */     }
/* 523:    */     catch (InvalidFormatException e)
/* 524:    */     {
/* 525:601 */       throw new POIXMLException(e);
/* 526:    */     }
/* 527:603 */     XSLFPictureData data = new XSLFPictureData(blipPart);
/* 528:    */     
/* 529:605 */     XMLSlideShow ppt = getSlideShow();
/* 530:606 */     XSLFPictureData pictureData = ppt.addPicture(data.getData(), data.getType());
/* 531:607 */     PackagePart pic = pictureData.getPackagePart();
/* 532:    */     
/* 533:609 */     POIXMLDocumentPart.RelationPart rp = addRelation(blipId, XSLFRelation.IMAGES, new XSLFPictureData(pic));
/* 534:    */     
/* 535:611 */     return rp.getRelationship().getId();
/* 536:    */   }
/* 537:    */   
/* 538:    */   PackagePart importPart(PackageRelationship srcRel, PackagePart srcPafrt)
/* 539:    */   {
/* 540:618 */     PackagePart destPP = getPackagePart();
/* 541:619 */     PackagePartName srcPPName = srcPafrt.getPartName();
/* 542:    */     
/* 543:621 */     OPCPackage pkg = destPP.getPackage();
/* 544:622 */     if (pkg.containPart(srcPPName)) {
/* 545:624 */       return pkg.getPart(srcPPName);
/* 546:    */     }
/* 547:627 */     destPP.addRelationship(srcPPName, TargetMode.INTERNAL, srcRel.getRelationshipType());
/* 548:    */     
/* 549:629 */     PackagePart part = pkg.createPart(srcPPName, srcPafrt.getContentType());
/* 550:    */     try
/* 551:    */     {
/* 552:631 */       OutputStream out = part.getOutputStream();
/* 553:632 */       InputStream is = srcPafrt.getInputStream();
/* 554:633 */       IOUtils.copy(is, out);
/* 555:634 */       is.close();
/* 556:635 */       out.close();
/* 557:    */     }
/* 558:    */     catch (IOException e)
/* 559:    */     {
/* 560:637 */       throw new POIXMLException(e);
/* 561:    */     }
/* 562:639 */     return part;
/* 563:    */   }
/* 564:    */   
/* 565:    */   void removePictureRelation(XSLFPictureShape pictureShape)
/* 566:    */   {
/* 567:648 */     POIXMLDocumentPart pd = getRelationById(pictureShape.getBlipId());
/* 568:649 */     removeRelation(pd);
/* 569:    */   }
/* 570:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSheet
 * JD-Core Version:    0.7.0.1
 */