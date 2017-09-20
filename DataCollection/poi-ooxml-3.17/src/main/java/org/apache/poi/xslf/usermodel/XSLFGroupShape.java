/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.geom.Rectangle2D;
/*   4:    */ import java.awt.geom.Rectangle2D.Double;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  10:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  11:    */ import org.apache.poi.sl.draw.DrawPictureShape;
/*  12:    */ import org.apache.poi.sl.usermodel.GroupShape;
/*  13:    */ import org.apache.poi.sl.usermodel.PictureData;
/*  14:    */ import org.apache.poi.util.POILogFactory;
/*  15:    */ import org.apache.poi.util.POILogger;
/*  16:    */ import org.apache.poi.util.Units;
/*  17:    */ import org.apache.xmlbeans.XmlObject;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  23:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTConnector;
/*  24:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
/*  25:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  26:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape.Factory;
/*  27:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual;
/*  28:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;
/*  29:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  30:    */ 
/*  31:    */ public class XSLFGroupShape
/*  32:    */   extends XSLFShape
/*  33:    */   implements XSLFShapeContainer, GroupShape<XSLFShape, XSLFTextParagraph>
/*  34:    */ {
/*  35: 58 */   private static final POILogger _logger = POILogFactory.getLogger(XSLFGroupShape.class);
/*  36:    */   private final List<XSLFShape> _shapes;
/*  37:    */   private final CTGroupShapeProperties _grpSpPr;
/*  38:    */   private XSLFDrawing _drawing;
/*  39:    */   
/*  40:    */   protected XSLFGroupShape(CTGroupShape shape, XSLFSheet sheet)
/*  41:    */   {
/*  42: 65 */     super(shape, sheet);
/*  43: 66 */     this._shapes = XSLFSheet.buildShapes(shape, sheet);
/*  44: 67 */     this._grpSpPr = shape.getGrpSpPr();
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected CTGroupShapeProperties getGrpSpPr()
/*  48:    */   {
/*  49: 72 */     return this._grpSpPr;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected CTGroupTransform2D getSafeXfrm()
/*  53:    */   {
/*  54: 76 */     CTGroupTransform2D xfrm = getXfrm();
/*  55: 77 */     return xfrm == null ? getGrpSpPr().addNewXfrm() : xfrm;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected CTGroupTransform2D getXfrm()
/*  59:    */   {
/*  60: 81 */     return getGrpSpPr().getXfrm();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Rectangle2D getAnchor()
/*  64:    */   {
/*  65: 86 */     CTGroupTransform2D xfrm = getXfrm();
/*  66: 87 */     CTPoint2D off = xfrm.getOff();
/*  67: 88 */     double x = Units.toPoints(off.getX());
/*  68: 89 */     double y = Units.toPoints(off.getY());
/*  69: 90 */     CTPositiveSize2D ext = xfrm.getExt();
/*  70: 91 */     double cx = Units.toPoints(ext.getCx());
/*  71: 92 */     double cy = Units.toPoints(ext.getCy());
/*  72: 93 */     return new Double(x, y, cx, cy);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setAnchor(Rectangle2D anchor)
/*  76:    */   {
/*  77: 98 */     CTGroupTransform2D xfrm = getSafeXfrm();
/*  78: 99 */     CTPoint2D off = xfrm.isSetOff() ? xfrm.getOff() : xfrm.addNewOff();
/*  79:100 */     long x = Units.toEMU(anchor.getX());
/*  80:101 */     long y = Units.toEMU(anchor.getY());
/*  81:102 */     off.setX(x);
/*  82:103 */     off.setY(y);
/*  83:104 */     CTPositiveSize2D ext = xfrm.isSetExt() ? xfrm.getExt() : xfrm.addNewExt();
/*  84:105 */     long cx = Units.toEMU(anchor.getWidth());
/*  85:106 */     long cy = Units.toEMU(anchor.getHeight());
/*  86:107 */     ext.setCx(cx);
/*  87:108 */     ext.setCy(cy);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Rectangle2D getInteriorAnchor()
/*  91:    */   {
/*  92:119 */     CTGroupTransform2D xfrm = getXfrm();
/*  93:120 */     CTPoint2D off = xfrm.getChOff();
/*  94:121 */     double x = Units.toPoints(off.getX());
/*  95:122 */     double y = Units.toPoints(off.getY());
/*  96:123 */     CTPositiveSize2D ext = xfrm.getChExt();
/*  97:124 */     double cx = Units.toPoints(ext.getCx());
/*  98:125 */     double cy = Units.toPoints(ext.getCy());
/*  99:126 */     return new Double(x, y, cx, cy);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setInteriorAnchor(Rectangle2D anchor)
/* 103:    */   {
/* 104:137 */     CTGroupTransform2D xfrm = getSafeXfrm();
/* 105:138 */     CTPoint2D off = xfrm.isSetChOff() ? xfrm.getChOff() : xfrm.addNewChOff();
/* 106:139 */     long x = Units.toEMU(anchor.getX());
/* 107:140 */     long y = Units.toEMU(anchor.getY());
/* 108:141 */     off.setX(x);
/* 109:142 */     off.setY(y);
/* 110:143 */     CTPositiveSize2D ext = xfrm.isSetChExt() ? xfrm.getChExt() : xfrm.addNewChExt();
/* 111:144 */     long cx = Units.toEMU(anchor.getWidth());
/* 112:145 */     long cy = Units.toEMU(anchor.getHeight());
/* 113:146 */     ext.setCx(cx);
/* 114:147 */     ext.setCy(cy);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public List<XSLFShape> getShapes()
/* 118:    */   {
/* 119:155 */     return this._shapes;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Iterator<XSLFShape> iterator()
/* 123:    */   {
/* 124:165 */     return this._shapes.iterator();
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean removeShape(XSLFShape xShape)
/* 128:    */   {
/* 129:173 */     XmlObject obj = xShape.getXmlObject();
/* 130:174 */     CTGroupShape grpSp = (CTGroupShape)getXmlObject();
/* 131:175 */     if ((obj instanceof CTShape))
/* 132:    */     {
/* 133:176 */       grpSp.getSpList().remove(obj);
/* 134:    */     }
/* 135:177 */     else if ((obj instanceof CTGroupShape))
/* 136:    */     {
/* 137:178 */       grpSp.getGrpSpList().remove(obj);
/* 138:    */     }
/* 139:179 */     else if ((obj instanceof CTConnector))
/* 140:    */     {
/* 141:180 */       grpSp.getCxnSpList().remove(obj);
/* 142:    */     }
/* 143:181 */     else if ((obj instanceof CTGraphicalObjectFrame))
/* 144:    */     {
/* 145:182 */       grpSp.getGraphicFrameList().remove(obj);
/* 146:    */     }
/* 147:183 */     else if ((obj instanceof CTPicture))
/* 148:    */     {
/* 149:184 */       XSLFPictureShape ps = (XSLFPictureShape)xShape;
/* 150:185 */       XSLFSheet sh = getSheet();
/* 151:186 */       if (sh != null) {
/* 152:187 */         sh.removePictureRelation(ps);
/* 153:    */       }
/* 154:189 */       grpSp.getPicList().remove(obj);
/* 155:    */     }
/* 156:    */     else
/* 157:    */     {
/* 158:191 */       throw new IllegalArgumentException("Unsupported shape: " + xShape);
/* 159:    */     }
/* 160:193 */     return this._shapes.remove(xShape);
/* 161:    */   }
/* 162:    */   
/* 163:    */   static CTGroupShape prototype(int shapeId)
/* 164:    */   {
/* 165:200 */     CTGroupShape ct = CTGroupShape.Factory.newInstance();
/* 166:201 */     CTGroupShapeNonVisual nvSpPr = ct.addNewNvGrpSpPr();
/* 167:202 */     CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
/* 168:203 */     cnv.setName("Group " + shapeId);
/* 169:204 */     cnv.setId(shapeId + 1);
/* 170:    */     
/* 171:206 */     nvSpPr.addNewCNvGrpSpPr();
/* 172:207 */     nvSpPr.addNewNvPr();
/* 173:208 */     ct.addNewGrpSpPr();
/* 174:209 */     return ct;
/* 175:    */   }
/* 176:    */   
/* 177:    */   private XSLFDrawing getDrawing()
/* 178:    */   {
/* 179:214 */     if (this._drawing == null) {
/* 180:215 */       this._drawing = new XSLFDrawing(getSheet(), (CTGroupShape)getXmlObject());
/* 181:    */     }
/* 182:217 */     return this._drawing;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public XSLFAutoShape createAutoShape()
/* 186:    */   {
/* 187:222 */     XSLFAutoShape sh = getDrawing().createAutoShape();
/* 188:223 */     this._shapes.add(sh);
/* 189:224 */     sh.setParent(this);
/* 190:225 */     return sh;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public XSLFFreeformShape createFreeform()
/* 194:    */   {
/* 195:230 */     XSLFFreeformShape sh = getDrawing().createFreeform();
/* 196:231 */     this._shapes.add(sh);
/* 197:232 */     sh.setParent(this);
/* 198:233 */     return sh;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public XSLFTextBox createTextBox()
/* 202:    */   {
/* 203:238 */     XSLFTextBox sh = getDrawing().createTextBox();
/* 204:239 */     this._shapes.add(sh);
/* 205:240 */     sh.setParent(this);
/* 206:241 */     return sh;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public XSLFConnectorShape createConnector()
/* 210:    */   {
/* 211:246 */     XSLFConnectorShape sh = getDrawing().createConnector();
/* 212:247 */     this._shapes.add(sh);
/* 213:248 */     sh.setParent(this);
/* 214:249 */     return sh;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public XSLFGroupShape createGroup()
/* 218:    */   {
/* 219:254 */     XSLFGroupShape sh = getDrawing().createGroup();
/* 220:255 */     this._shapes.add(sh);
/* 221:256 */     sh.setParent(this);
/* 222:257 */     return sh;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public XSLFPictureShape createPicture(PictureData pictureData)
/* 226:    */   {
/* 227:262 */     if (!(pictureData instanceof XSLFPictureData)) {
/* 228:263 */       throw new IllegalArgumentException("pictureData needs to be of type XSLFPictureData");
/* 229:    */     }
/* 230:265 */     XSLFPictureData xPictureData = (XSLFPictureData)pictureData;
/* 231:266 */     PackagePart pic = xPictureData.getPackagePart();
/* 232:    */     
/* 233:268 */     PackageRelationship rel = getSheet().getPackagePart().addRelationship(pic.getPartName(), TargetMode.INTERNAL, XSLFRelation.IMAGES.getRelation());
/* 234:    */     
/* 235:    */ 
/* 236:271 */     XSLFPictureShape sh = getDrawing().createPicture(rel.getId());
/* 237:272 */     new DrawPictureShape(sh).resize();
/* 238:273 */     this._shapes.add(sh);
/* 239:274 */     sh.setParent(this);
/* 240:275 */     return sh;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public XSLFTable createTable()
/* 244:    */   {
/* 245:279 */     XSLFTable sh = getDrawing().createTable();
/* 246:280 */     this._shapes.add(sh);
/* 247:281 */     sh.setParent(this);
/* 248:282 */     return sh;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public XSLFTable createTable(int numRows, int numCols)
/* 252:    */   {
/* 253:287 */     if ((numRows < 1) || (numCols < 1)) {
/* 254:288 */       throw new IllegalArgumentException("numRows and numCols must be greater than 0");
/* 255:    */     }
/* 256:290 */     XSLFTable sh = getDrawing().createTable();
/* 257:291 */     this._shapes.add(sh);
/* 258:292 */     sh.setParent(this);
/* 259:293 */     for (int r = 0; r < numRows; r++)
/* 260:    */     {
/* 261:294 */       XSLFTableRow row = sh.addRow();
/* 262:295 */       for (int c = 0; c < numCols; c++) {
/* 263:296 */         row.addCell();
/* 264:    */       }
/* 265:    */     }
/* 266:299 */     return sh;
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setFlipHorizontal(boolean flip)
/* 270:    */   {
/* 271:305 */     getSafeXfrm().setFlipH(flip);
/* 272:    */   }
/* 273:    */   
/* 274:    */   public void setFlipVertical(boolean flip)
/* 275:    */   {
/* 276:310 */     getSafeXfrm().setFlipV(flip);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public boolean getFlipHorizontal()
/* 280:    */   {
/* 281:315 */     CTGroupTransform2D xfrm = getXfrm();
/* 282:316 */     return (xfrm != null) && (xfrm.isSetFlipH()) && (xfrm.getFlipH());
/* 283:    */   }
/* 284:    */   
/* 285:    */   public boolean getFlipVertical()
/* 286:    */   {
/* 287:321 */     CTGroupTransform2D xfrm = getXfrm();
/* 288:322 */     return (xfrm != null) && (xfrm.isSetFlipV()) && (xfrm.getFlipV());
/* 289:    */   }
/* 290:    */   
/* 291:    */   public void setRotation(double theta)
/* 292:    */   {
/* 293:327 */     getSafeXfrm().setRot((int)(theta * 60000.0D));
/* 294:    */   }
/* 295:    */   
/* 296:    */   public double getRotation()
/* 297:    */   {
/* 298:332 */     CTGroupTransform2D xfrm = getXfrm();
/* 299:333 */     return (xfrm == null) || (!xfrm.isSetRot()) ? 0.0D : xfrm.getRot() / 60000.0D;
/* 300:    */   }
/* 301:    */   
/* 302:    */   void copy(XSLFShape src)
/* 303:    */   {
/* 304:338 */     XSLFGroupShape gr = (XSLFGroupShape)src;
/* 305:    */     
/* 306:    */ 
/* 307:341 */     List<XSLFShape> tgtShapes = getShapes();
/* 308:342 */     List<XSLFShape> srcShapes = gr.getShapes();
/* 309:347 */     if (tgtShapes.size() == srcShapes.size())
/* 310:    */     {
/* 311:348 */       for (int i = 0; i < tgtShapes.size(); i++)
/* 312:    */       {
/* 313:349 */         XSLFShape s1 = (XSLFShape)srcShapes.get(i);
/* 314:350 */         XSLFShape s2 = (XSLFShape)tgtShapes.get(i);
/* 315:    */         
/* 316:352 */         s2.copy(s1);
/* 317:    */       }
/* 318:    */     }
/* 319:    */     else
/* 320:    */     {
/* 321:356 */       clear();
/* 322:359 */       for (XSLFShape shape : srcShapes)
/* 323:    */       {
/* 324:    */         XSLFShape newShape;
/* 325:361 */         if ((shape instanceof XSLFTextBox))
/* 326:    */         {
/* 327:362 */           newShape = createTextBox();
/* 328:    */         }
/* 329:    */         else
/* 330:    */         {
/* 331:    */           XSLFShape newShape;
/* 332:363 */           if ((shape instanceof XSLFAutoShape))
/* 333:    */           {
/* 334:364 */             newShape = createAutoShape();
/* 335:    */           }
/* 336:    */           else
/* 337:    */           {
/* 338:    */             XSLFShape newShape;
/* 339:365 */             if ((shape instanceof XSLFConnectorShape))
/* 340:    */             {
/* 341:366 */               newShape = createConnector();
/* 342:    */             }
/* 343:    */             else
/* 344:    */             {
/* 345:    */               XSLFShape newShape;
/* 346:367 */               if ((shape instanceof XSLFFreeformShape))
/* 347:    */               {
/* 348:368 */                 newShape = createFreeform();
/* 349:    */               }
/* 350:    */               else
/* 351:    */               {
/* 352:    */                 XSLFShape newShape;
/* 353:369 */                 if ((shape instanceof XSLFPictureShape))
/* 354:    */                 {
/* 355:370 */                   XSLFPictureShape p = (XSLFPictureShape)shape;
/* 356:371 */                   XSLFPictureData pd = p.getPictureData();
/* 357:372 */                   XSLFPictureData pdNew = getSheet().getSlideShow().addPicture(pd.getData(), pd.getType());
/* 358:373 */                   newShape = createPicture(pdNew);
/* 359:    */                 }
/* 360:    */                 else
/* 361:    */                 {
/* 362:    */                   XSLFShape newShape;
/* 363:374 */                   if ((shape instanceof XSLFGroupShape))
/* 364:    */                   {
/* 365:375 */                     newShape = createGroup();
/* 366:    */                   }
/* 367:    */                   else
/* 368:    */                   {
/* 369:    */                     XSLFShape newShape;
/* 370:376 */                     if ((shape instanceof XSLFTable))
/* 371:    */                     {
/* 372:377 */                       newShape = createTable();
/* 373:    */                     }
/* 374:    */                     else
/* 375:    */                     {
/* 376:379 */                       _logger.log(5, new Object[] { "copying of class " + shape.getClass() + " not supported." });
/* 377:380 */                       continue;
/* 378:    */                     }
/* 379:    */                   }
/* 380:    */                 }
/* 381:    */               }
/* 382:    */             }
/* 383:    */           }
/* 384:    */         }
/* 385:    */         XSLFShape newShape;
/* 386:383 */         newShape.copy(shape);
/* 387:    */       }
/* 388:    */     }
/* 389:    */   }
/* 390:    */   
/* 391:    */   public void clear()
/* 392:    */   {
/* 393:394 */     List<XSLFShape> shapes = new ArrayList(getShapes());
/* 394:395 */     for (XSLFShape shape : shapes) {
/* 395:396 */       removeShape(shape);
/* 396:    */     }
/* 397:    */   }
/* 398:    */   
/* 399:    */   public void addShape(XSLFShape shape)
/* 400:    */   {
/* 401:402 */     throw new UnsupportedOperationException("Adding a shape from a different container is not supported - create it from scratch with XSLFGroupShape.create* methods");
/* 402:    */   }
/* 403:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFGroupShape

 * JD-Core Version:    0.7.0.1

 */