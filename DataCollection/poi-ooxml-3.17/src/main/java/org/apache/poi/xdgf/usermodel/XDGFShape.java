/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.ShapeSheetType;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.ShapesType;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.TextType;
/*   6:    */ import java.awt.BasicStroke;
/*   7:    */ import java.awt.Color;
/*   8:    */ import java.awt.Stroke;
/*   9:    */ import java.awt.geom.AffineTransform;
/*  10:    */ import java.awt.geom.Path2D.Double;
/*  11:    */ import java.awt.geom.Rectangle2D.Double;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Collection;
/*  14:    */ import java.util.Iterator;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.Map.Entry;
/*  18:    */ import java.util.SortedMap;
/*  19:    */ import org.apache.poi.POIXMLException;
/*  20:    */ import org.apache.poi.util.Internal;
/*  21:    */ import org.apache.poi.xdgf.exceptions.XDGFException;
/*  22:    */ import org.apache.poi.xdgf.usermodel.section.CombinedIterable;
/*  23:    */ import org.apache.poi.xdgf.usermodel.section.GeometrySection;
/*  24:    */ import org.apache.poi.xdgf.usermodel.section.XDGFSection;
/*  25:    */ import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
/*  26:    */ import org.apache.poi.xdgf.usermodel.shape.exceptions.StopVisitingThisBranch;
/*  27:    */ 
/*  28:    */ public class XDGFShape
/*  29:    */   extends XDGFSheet
/*  30:    */ {
/*  31:    */   XDGFBaseContents _parentPage;
/*  32:    */   XDGFShape _parent;
/*  33: 52 */   XDGFMaster _master = null;
/*  34: 53 */   XDGFShape _masterShape = null;
/*  35: 55 */   XDGFText _text = null;
/*  36: 58 */   List<XDGFShape> _shapes = null;
/*  37: 63 */   Double _pinX = null;
/*  38: 64 */   Double _pinY = null;
/*  39: 66 */   Double _width = null;
/*  40: 67 */   Double _height = null;
/*  41: 70 */   Double _locPinX = null;
/*  42: 71 */   Double _locPinY = null;
/*  43: 75 */   Double _beginX = null;
/*  44: 76 */   Double _beginY = null;
/*  45: 80 */   Double _endX = null;
/*  46: 81 */   Double _endY = null;
/*  47: 83 */   Double _angle = null;
/*  48: 84 */   Double _rotationXAngle = null;
/*  49: 85 */   Double _rotationYAngle = null;
/*  50: 86 */   Double _rotationZAngle = null;
/*  51: 89 */   Boolean _flipX = null;
/*  52: 90 */   Boolean _flipY = null;
/*  53: 93 */   Double _txtPinX = null;
/*  54: 94 */   Double _txtPinY = null;
/*  55: 97 */   Double _txtLocPinX = null;
/*  56: 98 */   Double _txtLocPinY = null;
/*  57:100 */   Double _txtAngle = null;
/*  58:102 */   Double _txtWidth = null;
/*  59:103 */   Double _txtHeight = null;
/*  60:    */   
/*  61:    */   public XDGFShape(ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document)
/*  62:    */   {
/*  63:107 */     this(null, shapeSheet, parentPage, document);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public XDGFShape(XDGFShape parent, ShapeSheetType shapeSheet, XDGFBaseContents parentPage, XDGFDocument document)
/*  67:    */   {
/*  68:113 */     super(shapeSheet, document);
/*  69:    */     
/*  70:115 */     this._parent = parent;
/*  71:116 */     this._parentPage = parentPage;
/*  72:    */     
/*  73:118 */     TextType text = shapeSheet.getText();
/*  74:119 */     if (text != null) {
/*  75:120 */       this._text = new XDGFText(text, this);
/*  76:    */     }
/*  77:122 */     if (shapeSheet.isSetShapes())
/*  78:    */     {
/*  79:123 */       this._shapes = new ArrayList();
/*  80:124 */       for (ShapeSheetType shape : shapeSheet.getShapes().getShapeArray()) {
/*  81:125 */         this._shapes.add(new XDGFShape(this, shape, parentPage, document));
/*  82:    */       }
/*  83:    */     }
/*  84:128 */     readProperties();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String toString()
/*  88:    */   {
/*  89:133 */     if ((this._parentPage instanceof XDGFMasterContents)) {
/*  90:134 */       return this._parentPage + ": <Shape ID=\"" + getID() + "\">";
/*  91:    */     }
/*  92:136 */     return "<Shape ID=\"" + getID() + "\">";
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected void readProperties()
/*  96:    */   {
/*  97:141 */     this._pinX = XDGFCell.maybeGetDouble(this._cells, "PinX");
/*  98:142 */     this._pinY = XDGFCell.maybeGetDouble(this._cells, "PinY");
/*  99:143 */     this._width = XDGFCell.maybeGetDouble(this._cells, "Width");
/* 100:144 */     this._height = XDGFCell.maybeGetDouble(this._cells, "Height");
/* 101:145 */     this._locPinX = XDGFCell.maybeGetDouble(this._cells, "LocPinX");
/* 102:146 */     this._locPinY = XDGFCell.maybeGetDouble(this._cells, "LocPinY");
/* 103:147 */     this._beginX = XDGFCell.maybeGetDouble(this._cells, "BeginX");
/* 104:148 */     this._beginY = XDGFCell.maybeGetDouble(this._cells, "BeginY");
/* 105:149 */     this._endX = XDGFCell.maybeGetDouble(this._cells, "EndX");
/* 106:150 */     this._endY = XDGFCell.maybeGetDouble(this._cells, "EndY");
/* 107:    */     
/* 108:152 */     this._angle = XDGFCell.maybeGetDouble(this._cells, "Angle");
/* 109:153 */     this._rotationXAngle = XDGFCell.maybeGetDouble(this._cells, "RotationXAngle");
/* 110:154 */     this._rotationYAngle = XDGFCell.maybeGetDouble(this._cells, "RotationYAngle");
/* 111:155 */     this._rotationZAngle = XDGFCell.maybeGetDouble(this._cells, "RotationZAngle");
/* 112:    */     
/* 113:157 */     this._flipX = XDGFCell.maybeGetBoolean(this._cells, "FlipX");
/* 114:158 */     this._flipY = XDGFCell.maybeGetBoolean(this._cells, "FlipY");
/* 115:    */     
/* 116:160 */     this._txtPinX = XDGFCell.maybeGetDouble(this._cells, "TxtPinX");
/* 117:161 */     this._txtPinY = XDGFCell.maybeGetDouble(this._cells, "TxtPinY");
/* 118:162 */     this._txtLocPinX = XDGFCell.maybeGetDouble(this._cells, "TxtLocPinX");
/* 119:163 */     this._txtLocPinY = XDGFCell.maybeGetDouble(this._cells, "TxtLocPinY");
/* 120:164 */     this._txtWidth = XDGFCell.maybeGetDouble(this._cells, "TxtWidth");
/* 121:165 */     this._txtHeight = XDGFCell.maybeGetDouble(this._cells, "TxtHeight");
/* 122:    */     
/* 123:167 */     this._txtAngle = XDGFCell.maybeGetDouble(this._cells, "TxtAngle");
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected void setupMaster(XDGFPageContents pageContents, XDGFMasterContents master)
/* 127:    */   {
/* 128:180 */     ShapeSheetType obj = getXmlObject();
/* 129:182 */     if (obj.isSetMaster())
/* 130:    */     {
/* 131:183 */       this._master = pageContents.getMasterById(obj.getMaster());
/* 132:184 */       if (this._master == null) {
/* 133:185 */         throw XDGFException.error("refers to non-existant master " + obj.getMaster(), this);
/* 134:    */       }
/* 135:196 */       Collection<XDGFShape> masterShapes = this._master.getContent().getTopLevelShapes();
/* 136:199 */       switch (masterShapes.size())
/* 137:    */       {
/* 138:    */       case 0: 
/* 139:201 */         throw XDGFException.error("Could not retrieve master shape from " + this._master, this);
/* 140:    */       case 1: 
/* 141:205 */         this._masterShape = ((XDGFShape)masterShapes.iterator().next());
/* 142:206 */         break;
/* 143:    */       }
/* 144:    */     }
/* 145:211 */     else if (obj.isSetMasterShape())
/* 146:    */     {
/* 147:212 */       this._masterShape = master.getShapeById(obj.getMasterShape());
/* 148:213 */       if (this._masterShape == null) {
/* 149:214 */         throw XDGFException.error("refers to non-existant master shape " + obj.getMasterShape(), this);
/* 150:    */       }
/* 151:    */     }
/* 152:220 */     setupSectionMasters();
/* 153:222 */     if (this._shapes != null) {
/* 154:223 */       for (XDGFShape shape : this._shapes) {
/* 155:224 */         shape.setupMaster(pageContents, this._master == null ? master : this._master.getContent());
/* 156:    */       }
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   protected void setupSectionMasters()
/* 161:    */   {
/* 162:232 */     if (this._masterShape == null) {
/* 163:233 */       return;
/* 164:    */     }
/* 165:    */     try
/* 166:    */     {
/* 167:236 */       for (Entry<String, XDGFSection> section : this._sections.entrySet())
/* 168:    */       {
/* 169:237 */         XDGFSection master = this._masterShape.getSection((String)section.getKey());
/* 170:238 */         if (master != null) {
/* 171:239 */           ((XDGFSection)section.getValue()).setupMaster(master);
/* 172:    */         }
/* 173:    */       }
/* 174:242 */       for (Entry<Long, GeometrySection> section : this._geometry.entrySet())
/* 175:    */       {
/* 176:243 */         GeometrySection master = this._masterShape.getGeometryByIdx(((Long)section.getKey()).longValue());
/* 177:245 */         if (master != null) {
/* 178:246 */           ((GeometrySection)section.getValue()).setupMaster(master);
/* 179:    */         }
/* 180:    */       }
/* 181:    */     }
/* 182:    */     catch (POIXMLException e)
/* 183:    */     {
/* 184:249 */       throw XDGFException.wrap(toString(), e);
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   @Internal
/* 189:    */   public ShapeSheetType getXmlObject()
/* 190:    */   {
/* 191:256 */     return (ShapeSheetType)this._sheet;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public long getID()
/* 195:    */   {
/* 196:260 */     return getXmlObject().getID();
/* 197:    */   }
/* 198:    */   
/* 199:    */   public String getType()
/* 200:    */   {
/* 201:264 */     return getXmlObject().getType();
/* 202:    */   }
/* 203:    */   
/* 204:    */   public String getTextAsString()
/* 205:    */   {
/* 206:268 */     XDGFText text = getText();
/* 207:269 */     if (text == null) {
/* 208:270 */       return "";
/* 209:    */     }
/* 210:272 */     return text.getTextContent();
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean hasText()
/* 214:    */   {
/* 215:276 */     return (this._text != null) || ((this._masterShape != null) && (this._masterShape._text != null));
/* 216:    */   }
/* 217:    */   
/* 218:    */   public XDGFCell getCell(String cellName)
/* 219:    */   {
/* 220:282 */     XDGFCell _cell = super.getCell(cellName);
/* 221:285 */     if ((_cell == null) && (this._masterShape != null)) {
/* 222:286 */       _cell = this._masterShape.getCell(cellName);
/* 223:    */     }
/* 224:289 */     return _cell;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public GeometrySection getGeometryByIdx(long idx)
/* 228:    */   {
/* 229:293 */     return (GeometrySection)this._geometry.get(Long.valueOf(idx));
/* 230:    */   }
/* 231:    */   
/* 232:    */   public List<XDGFShape> getShapes()
/* 233:    */   {
/* 234:301 */     return this._shapes;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public String getName()
/* 238:    */   {
/* 239:306 */     String name = getXmlObject().getName();
/* 240:307 */     if (name == null) {
/* 241:308 */       return "";
/* 242:    */     }
/* 243:309 */     return name;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public String getShapeType()
/* 247:    */   {
/* 248:314 */     String type = getXmlObject().getType();
/* 249:315 */     if (type == null) {
/* 250:316 */       return "";
/* 251:    */     }
/* 252:317 */     return type;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public String getSymbolName()
/* 256:    */   {
/* 257:323 */     if (this._master == null) {
/* 258:324 */       return "";
/* 259:    */     }
/* 260:326 */     String name = this._master.getName();
/* 261:327 */     if (name == null) {
/* 262:328 */       return "";
/* 263:    */     }
/* 264:330 */     return name;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public XDGFShape getMasterShape()
/* 268:    */   {
/* 269:334 */     return this._masterShape;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public XDGFShape getParentShape()
/* 273:    */   {
/* 274:341 */     return this._parent;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public XDGFShape getTopmostParentShape()
/* 278:    */   {
/* 279:345 */     XDGFShape top = null;
/* 280:346 */     if (this._parent != null)
/* 281:    */     {
/* 282:347 */       top = this._parent.getTopmostParentShape();
/* 283:348 */       if (top == null) {
/* 284:349 */         top = this._parent;
/* 285:    */       }
/* 286:    */     }
/* 287:352 */     return top;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean hasMaster()
/* 291:    */   {
/* 292:356 */     return this._master != null;
/* 293:    */   }
/* 294:    */   
/* 295:    */   public boolean hasMasterShape()
/* 296:    */   {
/* 297:360 */     return this._masterShape != null;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public boolean hasParent()
/* 301:    */   {
/* 302:364 */     return this._parent != null;
/* 303:    */   }
/* 304:    */   
/* 305:    */   public boolean hasShapes()
/* 306:    */   {
/* 307:368 */     return this._shapes != null;
/* 308:    */   }
/* 309:    */   
/* 310:    */   public boolean isTopmost()
/* 311:    */   {
/* 312:372 */     return this._parent == null;
/* 313:    */   }
/* 314:    */   
/* 315:    */   public boolean isShape1D()
/* 316:    */   {
/* 317:376 */     return getBeginX() != null;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public boolean isDeleted()
/* 321:    */   {
/* 322:380 */     return getXmlObject().isSetDel() ? getXmlObject().getDel() : false;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public XDGFText getText()
/* 326:    */   {
/* 327:384 */     if ((this._text == null) && (this._masterShape != null)) {
/* 328:385 */       return this._masterShape.getText();
/* 329:    */     }
/* 330:387 */     return this._text;
/* 331:    */   }
/* 332:    */   
/* 333:    */   public Double getPinX()
/* 334:    */   {
/* 335:391 */     if ((this._pinX == null) && (this._masterShape != null)) {
/* 336:392 */       return this._masterShape.getPinX();
/* 337:    */     }
/* 338:394 */     if (this._pinX == null) {
/* 339:395 */       throw XDGFException.error("PinX not set!", this);
/* 340:    */     }
/* 341:397 */     return this._pinX;
/* 342:    */   }
/* 343:    */   
/* 344:    */   public Double getPinY()
/* 345:    */   {
/* 346:401 */     if ((this._pinY == null) && (this._masterShape != null)) {
/* 347:402 */       return this._masterShape.getPinY();
/* 348:    */     }
/* 349:404 */     if (this._pinY == null) {
/* 350:405 */       throw XDGFException.error("PinY not specified!", this);
/* 351:    */     }
/* 352:407 */     return this._pinY;
/* 353:    */   }
/* 354:    */   
/* 355:    */   public Double getWidth()
/* 356:    */   {
/* 357:411 */     if ((this._width == null) && (this._masterShape != null)) {
/* 358:412 */       return this._masterShape.getWidth();
/* 359:    */     }
/* 360:414 */     if (this._width == null) {
/* 361:415 */       throw XDGFException.error("Width not specified!", this);
/* 362:    */     }
/* 363:417 */     return this._width;
/* 364:    */   }
/* 365:    */   
/* 366:    */   public Double getHeight()
/* 367:    */   {
/* 368:421 */     if ((this._height == null) && (this._masterShape != null)) {
/* 369:422 */       return this._masterShape.getHeight();
/* 370:    */     }
/* 371:424 */     if (this._height == null) {
/* 372:425 */       throw XDGFException.error("Height not specified!", this);
/* 373:    */     }
/* 374:427 */     return this._height;
/* 375:    */   }
/* 376:    */   
/* 377:    */   public Double getLocPinX()
/* 378:    */   {
/* 379:431 */     if ((this._locPinX == null) && (this._masterShape != null)) {
/* 380:432 */       return this._masterShape.getLocPinX();
/* 381:    */     }
/* 382:434 */     if (this._locPinX == null) {
/* 383:435 */       throw XDGFException.error("LocPinX not specified!", this);
/* 384:    */     }
/* 385:437 */     return this._locPinX;
/* 386:    */   }
/* 387:    */   
/* 388:    */   public Double getLocPinY()
/* 389:    */   {
/* 390:441 */     if ((this._locPinY == null) && (this._masterShape != null)) {
/* 391:442 */       return this._masterShape.getLocPinY();
/* 392:    */     }
/* 393:444 */     if (this._locPinY == null) {
/* 394:445 */       throw XDGFException.error("LocPinY not specified!", this);
/* 395:    */     }
/* 396:447 */     return this._locPinY;
/* 397:    */   }
/* 398:    */   
/* 399:    */   public Double getBeginX()
/* 400:    */   {
/* 401:451 */     if ((this._beginX == null) && (this._masterShape != null)) {
/* 402:452 */       return this._masterShape.getBeginX();
/* 403:    */     }
/* 404:454 */     return this._beginX;
/* 405:    */   }
/* 406:    */   
/* 407:    */   public Double getBeginY()
/* 408:    */   {
/* 409:458 */     if ((this._beginY == null) && (this._masterShape != null)) {
/* 410:459 */       return this._masterShape.getBeginY();
/* 411:    */     }
/* 412:461 */     return this._beginY;
/* 413:    */   }
/* 414:    */   
/* 415:    */   public Double getEndX()
/* 416:    */   {
/* 417:465 */     if ((this._endX == null) && (this._masterShape != null)) {
/* 418:466 */       return this._masterShape.getEndX();
/* 419:    */     }
/* 420:468 */     return this._endX;
/* 421:    */   }
/* 422:    */   
/* 423:    */   public Double getEndY()
/* 424:    */   {
/* 425:472 */     if ((this._endY == null) && (this._masterShape != null)) {
/* 426:473 */       return this._masterShape.getEndY();
/* 427:    */     }
/* 428:475 */     return this._endY;
/* 429:    */   }
/* 430:    */   
/* 431:    */   public Double getAngle()
/* 432:    */   {
/* 433:479 */     if ((this._angle == null) && (this._masterShape != null)) {
/* 434:480 */       return this._masterShape.getAngle();
/* 435:    */     }
/* 436:482 */     return this._angle;
/* 437:    */   }
/* 438:    */   
/* 439:    */   public Boolean getFlipX()
/* 440:    */   {
/* 441:486 */     if ((this._flipX == null) && (this._masterShape != null)) {
/* 442:487 */       return this._masterShape.getFlipX();
/* 443:    */     }
/* 444:489 */     return this._flipX;
/* 445:    */   }
/* 446:    */   
/* 447:    */   public Boolean getFlipY()
/* 448:    */   {
/* 449:493 */     if ((this._flipY == null) && (this._masterShape != null)) {
/* 450:494 */       return this._masterShape.getFlipY();
/* 451:    */     }
/* 452:496 */     return this._flipY;
/* 453:    */   }
/* 454:    */   
/* 455:    */   public Double getTxtPinX()
/* 456:    */   {
/* 457:500 */     if ((this._txtPinX == null) && (this._masterShape != null) && (this._masterShape._txtPinX != null)) {
/* 458:502 */       return this._masterShape._txtPinX;
/* 459:    */     }
/* 460:504 */     if (this._txtPinX == null) {
/* 461:505 */       return Double.valueOf(getWidth().doubleValue() * 0.5D);
/* 462:    */     }
/* 463:507 */     return this._txtPinX;
/* 464:    */   }
/* 465:    */   
/* 466:    */   public Double getTxtPinY()
/* 467:    */   {
/* 468:511 */     if ((this._txtLocPinY == null) && (this._masterShape != null) && (this._masterShape._txtLocPinY != null)) {
/* 469:513 */       return this._masterShape._txtLocPinY;
/* 470:    */     }
/* 471:515 */     if (this._txtPinY == null) {
/* 472:516 */       return Double.valueOf(getHeight().doubleValue() * 0.5D);
/* 473:    */     }
/* 474:518 */     return this._txtPinY;
/* 475:    */   }
/* 476:    */   
/* 477:    */   public Double getTxtLocPinX()
/* 478:    */   {
/* 479:522 */     if ((this._txtLocPinX == null) && (this._masterShape != null) && (this._masterShape._txtLocPinX != null)) {
/* 480:524 */       return this._masterShape._txtLocPinX;
/* 481:    */     }
/* 482:526 */     if (this._txtLocPinX == null) {
/* 483:527 */       return Double.valueOf(getTxtWidth().doubleValue() * 0.5D);
/* 484:    */     }
/* 485:529 */     return this._txtLocPinX;
/* 486:    */   }
/* 487:    */   
/* 488:    */   public Double getTxtLocPinY()
/* 489:    */   {
/* 490:533 */     if ((this._txtLocPinY == null) && (this._masterShape != null) && (this._masterShape._txtLocPinY != null)) {
/* 491:535 */       return this._masterShape._txtLocPinY;
/* 492:    */     }
/* 493:537 */     if (this._txtLocPinY == null) {
/* 494:538 */       return Double.valueOf(getTxtHeight().doubleValue() * 0.5D);
/* 495:    */     }
/* 496:540 */     return this._txtLocPinY;
/* 497:    */   }
/* 498:    */   
/* 499:    */   public Double getTxtAngle()
/* 500:    */   {
/* 501:544 */     if ((this._txtAngle == null) && (this._masterShape != null)) {
/* 502:545 */       return this._masterShape.getTxtAngle();
/* 503:    */     }
/* 504:547 */     return this._txtAngle;
/* 505:    */   }
/* 506:    */   
/* 507:    */   public Double getTxtWidth()
/* 508:    */   {
/* 509:551 */     if ((this._txtWidth == null) && (this._masterShape != null) && (this._masterShape._txtWidth != null)) {
/* 510:553 */       return this._masterShape._txtWidth;
/* 511:    */     }
/* 512:555 */     if (this._txtWidth == null) {
/* 513:556 */       return getWidth();
/* 514:    */     }
/* 515:558 */     return this._txtWidth;
/* 516:    */   }
/* 517:    */   
/* 518:    */   public Double getTxtHeight()
/* 519:    */   {
/* 520:562 */     if ((this._txtHeight == null) && (this._masterShape != null) && (this._masterShape._txtHeight != null)) {
/* 521:564 */       return this._masterShape._txtHeight;
/* 522:    */     }
/* 523:566 */     if (this._txtHeight == null) {
/* 524:567 */       return getHeight();
/* 525:    */     }
/* 526:569 */     return this._txtHeight;
/* 527:    */   }
/* 528:    */   
/* 529:    */   public Integer getLineCap()
/* 530:    */   {
/* 531:575 */     Integer lineCap = super.getLineCap();
/* 532:576 */     if (lineCap != null) {
/* 533:577 */       return lineCap;
/* 534:    */     }
/* 535:580 */     if (this._masterShape != null) {
/* 536:581 */       return this._masterShape.getLineCap();
/* 537:    */     }
/* 538:585 */     return this._document.getDefaultLineStyle().getLineCap();
/* 539:    */   }
/* 540:    */   
/* 541:    */   public Color getLineColor()
/* 542:    */   {
/* 543:591 */     Color lineColor = super.getLineColor();
/* 544:592 */     if (lineColor != null) {
/* 545:593 */       return lineColor;
/* 546:    */     }
/* 547:596 */     if (this._masterShape != null) {
/* 548:597 */       return this._masterShape.getLineColor();
/* 549:    */     }
/* 550:601 */     return this._document.getDefaultLineStyle().getLineColor();
/* 551:    */   }
/* 552:    */   
/* 553:    */   public Integer getLinePattern()
/* 554:    */   {
/* 555:607 */     Integer linePattern = super.getLinePattern();
/* 556:608 */     if (linePattern != null) {
/* 557:609 */       return linePattern;
/* 558:    */     }
/* 559:612 */     if (this._masterShape != null) {
/* 560:613 */       return this._masterShape.getLinePattern();
/* 561:    */     }
/* 562:617 */     return this._document.getDefaultLineStyle().getLinePattern();
/* 563:    */   }
/* 564:    */   
/* 565:    */   public Double getLineWeight()
/* 566:    */   {
/* 567:623 */     Double lineWeight = super.getLineWeight();
/* 568:624 */     if (lineWeight != null) {
/* 569:625 */       return lineWeight;
/* 570:    */     }
/* 571:628 */     if (this._masterShape != null) {
/* 572:629 */       return this._masterShape.getLineWeight();
/* 573:    */     }
/* 574:633 */     return this._document.getDefaultLineStyle().getLineWeight();
/* 575:    */   }
/* 576:    */   
/* 577:    */   public Color getFontColor()
/* 578:    */   {
/* 579:639 */     Color fontColor = super.getFontColor();
/* 580:640 */     if (fontColor != null) {
/* 581:641 */       return fontColor;
/* 582:    */     }
/* 583:644 */     if (this._masterShape != null) {
/* 584:645 */       return this._masterShape.getFontColor();
/* 585:    */     }
/* 586:649 */     return this._document.getDefaultTextStyle().getFontColor();
/* 587:    */   }
/* 588:    */   
/* 589:    */   public Double getFontSize()
/* 590:    */   {
/* 591:655 */     Double fontSize = super.getFontSize();
/* 592:656 */     if (fontSize != null) {
/* 593:657 */       return fontSize;
/* 594:    */     }
/* 595:660 */     if (this._masterShape != null) {
/* 596:661 */       return this._masterShape.getFontSize();
/* 597:    */     }
/* 598:665 */     return this._document.getDefaultTextStyle().getFontSize();
/* 599:    */   }
/* 600:    */   
/* 601:    */   public Stroke getStroke()
/* 602:    */   {
/* 603:670 */     float lineWeight = getLineWeight().floatValue();
/* 604:    */     
/* 605:672 */     int join = 0;
/* 606:673 */     float miterlimit = 10.0F;
/* 607:    */     int cap;
/* 608:675 */     switch (getLineCap().intValue())
/* 609:    */     {
/* 610:    */     case 0: 
/* 611:677 */       cap = 1;
/* 612:678 */       break;
/* 613:    */     case 1: 
/* 614:680 */       cap = 2;
/* 615:681 */       break;
/* 616:    */     case 2: 
/* 617:683 */       cap = 0;
/* 618:684 */       break;
/* 619:    */     default: 
/* 620:686 */       throw new POIXMLException("Invalid line cap specified");
/* 621:    */     }
/* 622:689 */     float[] dash = null;
/* 623:692 */     switch (getLinePattern().intValue())
/* 624:    */     {
/* 625:    */     case 0: 
/* 626:    */       break;
/* 627:    */     case 1: 
/* 628:    */       break;
/* 629:    */     case 2: 
/* 630:698 */       dash = new float[] { 5.0F, 3.0F };
/* 631:699 */       break;
/* 632:    */     case 3: 
/* 633:701 */       dash = new float[] { 1.0F, 4.0F };
/* 634:702 */       break;
/* 635:    */     case 4: 
/* 636:704 */       dash = new float[] { 6.0F, 3.0F, 1.0F, 3.0F };
/* 637:705 */       break;
/* 638:    */     case 5: 
/* 639:707 */       dash = new float[] { 6.0F, 3.0F, 1.0F, 3.0F, 1.0F, 3.0F };
/* 640:708 */       break;
/* 641:    */     case 6: 
/* 642:710 */       dash = new float[] { 1.0F, 3.0F, 6.0F, 3.0F, 6.0F, 3.0F };
/* 643:711 */       break;
/* 644:    */     case 7: 
/* 645:713 */       dash = new float[] { 15.0F, 3.0F, 6.0F, 3.0F };
/* 646:714 */       break;
/* 647:    */     case 8: 
/* 648:716 */       dash = new float[] { 6.0F, 3.0F, 6.0F, 3.0F };
/* 649:717 */       break;
/* 650:    */     case 9: 
/* 651:719 */       dash = new float[] { 3.0F, 2.0F };
/* 652:720 */       break;
/* 653:    */     case 10: 
/* 654:722 */       dash = new float[] { 1.0F, 2.0F };
/* 655:723 */       break;
/* 656:    */     case 11: 
/* 657:725 */       dash = new float[] { 3.0F, 2.0F, 1.0F, 2.0F };
/* 658:726 */       break;
/* 659:    */     case 12: 
/* 660:728 */       dash = new float[] { 3.0F, 2.0F, 1.0F, 2.0F, 1.0F };
/* 661:729 */       break;
/* 662:    */     case 13: 
/* 663:731 */       dash = new float[] { 1.0F, 2.0F, 3.0F, 2.0F, 3.0F, 2.0F };
/* 664:732 */       break;
/* 665:    */     case 14: 
/* 666:734 */       dash = new float[] { 3.0F, 2.0F, 7.0F, 2.0F };
/* 667:735 */       break;
/* 668:    */     case 15: 
/* 669:737 */       dash = new float[] { 7.0F, 2.0F, 3.0F, 2.0F, 3.0F, 2.0F };
/* 670:738 */       break;
/* 671:    */     case 16: 
/* 672:740 */       dash = new float[] { 12.0F, 6.0F };
/* 673:741 */       break;
/* 674:    */     case 17: 
/* 675:743 */       dash = new float[] { 1.0F, 6.0F };
/* 676:744 */       break;
/* 677:    */     case 18: 
/* 678:746 */       dash = new float[] { 1.0F, 6.0F, 12.0F, 6.0F };
/* 679:747 */       break;
/* 680:    */     case 19: 
/* 681:749 */       dash = new float[] { 1.0F, 6.0F, 1.0F, 6.0F, 12.0F, 6.0F };
/* 682:750 */       break;
/* 683:    */     case 20: 
/* 684:752 */       dash = new float[] { 1.0F, 6.0F, 12.0F, 6.0F, 12.0F, 6.0F };
/* 685:753 */       break;
/* 686:    */     case 21: 
/* 687:755 */       dash = new float[] { 30.0F, 6.0F, 12.0F, 6.0F };
/* 688:756 */       break;
/* 689:    */     case 22: 
/* 690:758 */       dash = new float[] { 30.0F, 6.0F, 12.0F, 6.0F, 12.0F, 6.0F };
/* 691:759 */       break;
/* 692:    */     case 23: 
/* 693:761 */       dash = new float[] { 1.0F };
/* 694:762 */       break;
/* 695:    */     case 254: 
/* 696:764 */       throw new POIXMLException("Unsupported line pattern value");
/* 697:    */     default: 
/* 698:766 */       throw new POIXMLException("Invalid line pattern value");
/* 699:    */     }
/* 700:770 */     if (dash != null) {
/* 701:771 */       for (int i = 0; i < dash.length; i++) {
/* 702:772 */         dash[i] *= lineWeight;
/* 703:    */       }
/* 704:    */     }
/* 705:776 */     return new BasicStroke(lineWeight, cap, join, miterlimit, dash, 0.0F);
/* 706:    */   }
/* 707:    */   
/* 708:    */   public Iterable<GeometrySection> getGeometrySections()
/* 709:    */   {
/* 710:784 */     return new CombinedIterable(this._geometry, this._masterShape != null ? this._masterShape._geometry : null);
/* 711:    */   }
/* 712:    */   
/* 713:    */   public Rectangle2D.Double getBounds()
/* 714:    */   {
/* 715:792 */     return new Rectangle2D.Double(0.0D, 0.0D, getWidth().doubleValue(), getHeight().doubleValue());
/* 716:    */   }
/* 717:    */   
/* 718:    */   public Path2D.Double getBoundsAsPath()
/* 719:    */   {
/* 720:804 */     Double w = getWidth();
/* 721:805 */     Double h = getHeight();
/* 722:    */     
/* 723:807 */     Path2D.Double bounds = new Path2D.Double();
/* 724:808 */     bounds.moveTo(0.0D, 0.0D);
/* 725:809 */     bounds.lineTo(w.doubleValue(), 0.0D);
/* 726:810 */     bounds.lineTo(w.doubleValue(), h.doubleValue());
/* 727:811 */     bounds.lineTo(0.0D, h.doubleValue());
/* 728:812 */     bounds.lineTo(0.0D, 0.0D);
/* 729:    */     
/* 730:814 */     return bounds;
/* 731:    */   }
/* 732:    */   
/* 733:    */   public Path2D.Double getPath()
/* 734:    */   {
/* 735:821 */     for (GeometrySection geoSection : getGeometrySections()) {
/* 736:822 */       if (geoSection.getNoShow().booleanValue() != true) {
/* 737:825 */         return geoSection.getPath(this);
/* 738:    */       }
/* 739:    */     }
/* 740:828 */     return null;
/* 741:    */   }
/* 742:    */   
/* 743:    */   public boolean hasGeometry()
/* 744:    */   {
/* 745:835 */     for (GeometrySection geoSection : getGeometrySections()) {
/* 746:836 */       if (!geoSection.getNoShow().booleanValue()) {
/* 747:837 */         return true;
/* 748:    */       }
/* 749:    */     }
/* 750:839 */     return false;
/* 751:    */   }
/* 752:    */   
/* 753:    */   protected AffineTransform getParentTransform()
/* 754:    */   {
/* 755:848 */     AffineTransform tr = new AffineTransform();
/* 756:    */     
/* 757:850 */     Double locX = getLocPinX();
/* 758:851 */     Double locY = getLocPinY();
/* 759:852 */     Boolean flipX = getFlipX();
/* 760:853 */     Boolean flipY = getFlipY();
/* 761:854 */     Double angle = getAngle();
/* 762:    */     
/* 763:856 */     tr.translate(-locX.doubleValue(), -locY.doubleValue());
/* 764:    */     
/* 765:858 */     tr.translate(getPinX().doubleValue(), getPinY().doubleValue());
/* 766:861 */     if ((angle != null) && (Math.abs(angle.doubleValue()) > 0.001D)) {
/* 767:862 */       tr.rotate(angle.doubleValue(), locX.doubleValue(), locY.doubleValue());
/* 768:    */     }
/* 769:867 */     if ((flipX != null) && (flipX.booleanValue()))
/* 770:    */     {
/* 771:868 */       tr.scale(-1.0D, 1.0D);
/* 772:869 */       tr.translate(-getWidth().doubleValue(), 0.0D);
/* 773:    */     }
/* 774:872 */     if ((flipY != null) && (flipY.booleanValue()))
/* 775:    */     {
/* 776:873 */       tr.scale(1.0D, -1.0D);
/* 777:874 */       tr.translate(0.0D, -getHeight().doubleValue());
/* 778:    */     }
/* 779:877 */     return tr;
/* 780:    */   }
/* 781:    */   
/* 782:    */   public void visitShapes(ShapeVisitor visitor, AffineTransform tr, int level)
/* 783:    */   {
/* 784:888 */     tr = (AffineTransform)tr.clone();
/* 785:889 */     tr.concatenate(getParentTransform());
/* 786:    */     try
/* 787:    */     {
/* 788:892 */       if (visitor.accept(this)) {
/* 789:893 */         visitor.visit(this, tr, level);
/* 790:    */       }
/* 791:895 */       if (this._shapes != null) {
/* 792:896 */         for (XDGFShape shape : this._shapes) {
/* 793:897 */           shape.visitShapes(visitor, tr, level + 1);
/* 794:    */         }
/* 795:    */       }
/* 796:    */     }
/* 797:    */     catch (StopVisitingThisBranch e) {}catch (POIXMLException e)
/* 798:    */     {
/* 799:903 */       throw XDGFException.wrap(toString(), e);
/* 800:    */     }
/* 801:    */   }
/* 802:    */   
/* 803:    */   public void visitShapes(ShapeVisitor visitor, int level)
/* 804:    */   {
/* 805:    */     try
/* 806:    */     {
/* 807:917 */       if (visitor.accept(this)) {
/* 808:918 */         visitor.visit(this, null, level);
/* 809:    */       }
/* 810:920 */       if (this._shapes != null) {
/* 811:921 */         for (XDGFShape shape : this._shapes) {
/* 812:922 */           shape.visitShapes(visitor, level + 1);
/* 813:    */         }
/* 814:    */       }
/* 815:    */     }
/* 816:    */     catch (StopVisitingThisBranch e) {}catch (POIXMLException e)
/* 817:    */     {
/* 818:928 */       throw XDGFException.wrap(toString(), e);
/* 819:    */     }
/* 820:    */   }
/* 821:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFShape

 * JD-Core Version:    0.7.0.1

 */