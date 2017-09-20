/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.geom.Rectangle2D;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Comparator;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  12:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  13:    */ import org.apache.poi.sl.draw.DrawPaint;
/*  14:    */ import org.apache.poi.sl.usermodel.ColorStyle;
/*  15:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  16:    */ import org.apache.poi.sl.usermodel.PaintStyle;
/*  17:    */ import org.apache.poi.sl.usermodel.PaintStyle.GradientPaint;
/*  18:    */ import org.apache.poi.sl.usermodel.PaintStyle.GradientPaint.GradientType;
/*  19:    */ import org.apache.poi.sl.usermodel.PaintStyle.TexturePaint;
/*  20:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*  21:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  22:    */ import org.apache.poi.sl.usermodel.Shape;
/*  23:    */ import org.apache.poi.util.Internal;
/*  24:    */ import org.apache.poi.xslf.model.PropertyFetcher;
/*  25:    */ import org.apache.xmlbeans.XmlCursor;
/*  26:    */ import org.apache.xmlbeans.XmlObject;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties;
/*  39:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  41:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
/*  42:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  43:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix;
/*  44:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
/*  45:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType;
/*  46:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum;
/*  47:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps;
/*  48:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties;
/*  49:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  50:    */ import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum;
/*  51:    */ 
/*  52:    */ public abstract class XSLFShape
/*  53:    */   implements Shape<XSLFShape, XSLFTextParagraph>
/*  54:    */ {
/*  55:    */   protected static final String PML_NS = "http://schemas.openxmlformats.org/presentationml/2006/main";
/*  56:    */   private final XmlObject _shape;
/*  57:    */   private final XSLFSheet _sheet;
/*  58:    */   private XSLFShapeContainer _parent;
/*  59:    */   private CTShapeStyle _spStyle;
/*  60:    */   private CTNonVisualDrawingProps _nvPr;
/*  61:    */   private CTPlaceholder _ph;
/*  62:    */   
/*  63:    */   protected XSLFShape(XmlObject shape, XSLFSheet sheet)
/*  64:    */   {
/*  65: 83 */     this._shape = shape;
/*  66: 84 */     this._sheet = sheet;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final XmlObject getXmlObject()
/*  70:    */   {
/*  71: 93 */     return this._shape;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public XSLFSheet getSheet()
/*  75:    */   {
/*  76: 97 */     return this._sheet;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getShapeName()
/*  80:    */   {
/*  81:104 */     return getCNvPr().getName();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getShapeId()
/*  85:    */   {
/*  86:119 */     return (int)getCNvPr().getId();
/*  87:    */   }
/*  88:    */   
/*  89:    */   @Internal
/*  90:    */   void copy(XSLFShape sh)
/*  91:    */   {
/*  92:131 */     if (!getClass().isInstance(sh)) {
/*  93:132 */       throw new IllegalArgumentException("Can't copy " + sh.getClass().getSimpleName() + " into " + getClass().getSimpleName());
/*  94:    */     }
/*  95:136 */     if ((this instanceof PlaceableShape))
/*  96:    */     {
/*  97:137 */       PlaceableShape<?, ?> ps = (PlaceableShape)this;
/*  98:138 */       ps.setAnchor(sh.getAnchor());
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setParent(XSLFShapeContainer parent)
/* 103:    */   {
/* 104:145 */     this._parent = parent;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public XSLFShapeContainer getParent()
/* 108:    */   {
/* 109:149 */     return this._parent;
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected PaintStyle getFillPaint()
/* 113:    */   {
/* 114:153 */     final XSLFTheme theme = getSheet().getTheme();
/* 115:154 */     final boolean hasPlaceholder = getPlaceholder() != null;
/* 116:155 */     PropertyFetcher<PaintStyle> fetcher = new PropertyFetcher()
/* 117:    */     {
/* 118:    */       public boolean fetch(XSLFShape shape)
/* 119:    */       {
/* 120:157 */         XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(shape.getShapeProperties());
/* 121:158 */         if (fp == null) {
/* 122:159 */           return false;
/* 123:    */         }
/* 124:162 */         if (fp.isSetNoFill())
/* 125:    */         {
/* 126:163 */           setValue(null);
/* 127:164 */           return true;
/* 128:    */         }
/* 129:167 */         PackagePart pp = shape.getSheet().getPackagePart();
/* 130:168 */         PaintStyle paint = XSLFShape.selectPaint(fp, null, pp, theme, hasPlaceholder);
/* 131:169 */         if (paint != null)
/* 132:    */         {
/* 133:170 */           setValue(paint);
/* 134:171 */           return true;
/* 135:    */         }
/* 136:174 */         CTShapeStyle style = shape.getSpStyle();
/* 137:175 */         if (style != null)
/* 138:    */         {
/* 139:176 */           fp = XSLFPropertiesDelegate.getFillDelegate(style.getFillRef());
/* 140:177 */           paint = XSLFShape.selectPaint(fp, null, pp, theme, hasPlaceholder);
/* 141:    */         }
/* 142:179 */         if (paint != null)
/* 143:    */         {
/* 144:180 */           setValue(paint);
/* 145:181 */           return true;
/* 146:    */         }
/* 147:185 */         return false;
/* 148:    */       }
/* 149:187 */     };
/* 150:188 */     fetchShapeProperty(fetcher);
/* 151:    */     
/* 152:190 */     return (PaintStyle)fetcher.getValue();
/* 153:    */   }
/* 154:    */   
/* 155:    */   protected CTBackgroundProperties getBgPr()
/* 156:    */   {
/* 157:194 */     return (CTBackgroundProperties)getChild(CTBackgroundProperties.class, "http://schemas.openxmlformats.org/presentationml/2006/main", "bgPr");
/* 158:    */   }
/* 159:    */   
/* 160:    */   protected CTStyleMatrixReference getBgRef()
/* 161:    */   {
/* 162:198 */     return (CTStyleMatrixReference)getChild(CTStyleMatrixReference.class, "http://schemas.openxmlformats.org/presentationml/2006/main", "bgRef");
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected CTGroupShapeProperties getGrpSpPr()
/* 166:    */   {
/* 167:202 */     return (CTGroupShapeProperties)getChild(CTGroupShapeProperties.class, "http://schemas.openxmlformats.org/presentationml/2006/main", "grpSpPr");
/* 168:    */   }
/* 169:    */   
/* 170:    */   protected CTNonVisualDrawingProps getCNvPr()
/* 171:    */   {
/* 172:206 */     if (this._nvPr == null)
/* 173:    */     {
/* 174:207 */       String xquery = "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:cNvPr";
/* 175:208 */       this._nvPr = ((CTNonVisualDrawingProps)selectProperty(CTNonVisualDrawingProps.class, xquery));
/* 176:    */     }
/* 177:210 */     return this._nvPr;
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected CTShapeStyle getSpStyle()
/* 181:    */   {
/* 182:214 */     if (this._spStyle == null) {
/* 183:215 */       this._spStyle = ((CTShapeStyle)getChild(CTShapeStyle.class, "http://schemas.openxmlformats.org/presentationml/2006/main", "style"));
/* 184:    */     }
/* 185:217 */     return this._spStyle;
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected <T extends XmlObject> T getChild(Class<T> childClass, String namespace, String nodename)
/* 189:    */   {
/* 190:230 */     XmlCursor cur = getXmlObject().newCursor();
/* 191:231 */     T child = null;
/* 192:232 */     if (cur.toChild(namespace, nodename)) {
/* 193:233 */       child = cur.getObject();
/* 194:    */     }
/* 195:235 */     if (cur.toChild("http://schemas.openxmlformats.org/drawingml/2006/main", nodename)) {
/* 196:236 */       child = cur.getObject();
/* 197:    */     }
/* 198:238 */     cur.dispose();
/* 199:239 */     return child;
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected CTPlaceholder getCTPlaceholder()
/* 203:    */   {
/* 204:243 */     if (this._ph == null)
/* 205:    */     {
/* 206:244 */       String xquery = "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:nvPr/p:ph";
/* 207:245 */       this._ph = ((CTPlaceholder)selectProperty(CTPlaceholder.class, xquery));
/* 208:    */     }
/* 209:247 */     return this._ph;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public Placeholder getPlaceholder()
/* 213:    */   {
/* 214:251 */     CTPlaceholder ph = getCTPlaceholder();
/* 215:252 */     if ((ph == null) || ((!ph.isSetType()) && (!ph.isSetIdx()))) {
/* 216:253 */       return null;
/* 217:    */     }
/* 218:255 */     return Placeholder.lookupOoxml(ph.getType().intValue());
/* 219:    */   }
/* 220:    */   
/* 221:    */   protected void setPlaceholder(Placeholder placeholder)
/* 222:    */   {
/* 223:268 */     String xquery = "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:nvPr";
/* 224:269 */     CTApplicationNonVisualDrawingProps nv = (CTApplicationNonVisualDrawingProps)selectProperty(CTApplicationNonVisualDrawingProps.class, xquery);
/* 225:270 */     if (nv == null) {
/* 226:270 */       return;
/* 227:    */     }
/* 228:271 */     if (placeholder == null)
/* 229:    */     {
/* 230:272 */       if (nv.isSetPh()) {
/* 231:272 */         nv.unsetPh();
/* 232:    */       }
/* 233:273 */       this._ph = null;
/* 234:    */     }
/* 235:    */     else
/* 236:    */     {
/* 237:275 */       nv.addNewPh().setType(STPlaceholderType.Enum.forInt(placeholder.ooxmlId));
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   protected <T extends XmlObject> T selectProperty(Class<T> resultClass, String xquery)
/* 242:    */   {
/* 243:292 */     XmlObject[] rs = getXmlObject().selectPath(xquery);
/* 244:293 */     if (rs.length == 0) {
/* 245:293 */       return null;
/* 246:    */     }
/* 247:294 */     return resultClass.isInstance(rs[0]) ? rs[0] : null;
/* 248:    */   }
/* 249:    */   
/* 250:    */   protected boolean fetchShapeProperty(PropertyFetcher<?> visitor)
/* 251:    */   {
/* 252:314 */     if (visitor.fetch(this)) {
/* 253:315 */       return true;
/* 254:    */     }
/* 255:318 */     CTPlaceholder ph = getCTPlaceholder();
/* 256:319 */     if (ph == null) {
/* 257:320 */       return false;
/* 258:    */     }
/* 259:322 */     MasterSheet<XSLFShape, XSLFTextParagraph> sm = getSheet().getMasterSheet();
/* 260:325 */     if ((sm instanceof XSLFSlideLayout))
/* 261:    */     {
/* 262:326 */       XSLFSlideLayout slideLayout = (XSLFSlideLayout)sm;
/* 263:327 */       XSLFSimpleShape placeholderShape = slideLayout.getPlaceholder(ph);
/* 264:328 */       if ((placeholderShape != null) && (visitor.fetch(placeholderShape))) {
/* 265:329 */         return true;
/* 266:    */       }
/* 267:331 */       sm = slideLayout.getMasterSheet();
/* 268:    */     }
/* 269:335 */     if ((sm instanceof XSLFSlideMaster))
/* 270:    */     {
/* 271:336 */       XSLFSlideMaster master = (XSLFSlideMaster)sm;
/* 272:337 */       int textType = getPlaceholderType(ph);
/* 273:338 */       XSLFSimpleShape masterShape = master.getPlaceholderByType(textType);
/* 274:339 */       if ((masterShape != null) && (visitor.fetch(masterShape))) {
/* 275:340 */         return true;
/* 276:    */       }
/* 277:    */     }
/* 278:344 */     return false;
/* 279:    */   }
/* 280:    */   
/* 281:    */   private static int getPlaceholderType(CTPlaceholder ph)
/* 282:    */   {
/* 283:348 */     if (!ph.isSetType()) {
/* 284:349 */       return 2;
/* 285:    */     }
/* 286:352 */     switch (ph.getType().intValue())
/* 287:    */     {
/* 288:    */     case 1: 
/* 289:    */     case 3: 
/* 290:355 */       return 1;
/* 291:    */     case 5: 
/* 292:    */     case 6: 
/* 293:    */     case 7: 
/* 294:359 */       return ph.getType().intValue();
/* 295:    */     }
/* 296:361 */     return 2;
/* 297:    */   }
/* 298:    */   
/* 299:    */   protected static PaintStyle selectPaint(XSLFPropertiesDelegate.XSLFFillProperties fp, CTSchemeColor phClr, PackagePart parentPart, XSLFTheme theme, boolean hasPlaceholder)
/* 300:    */   {
/* 301:377 */     if ((fp == null) || (fp.isSetNoFill())) {
/* 302:378 */       return null;
/* 303:    */     }
/* 304:379 */     if (fp.isSetSolidFill()) {
/* 305:380 */       return selectPaint(fp.getSolidFill(), phClr, theme);
/* 306:    */     }
/* 307:381 */     if (fp.isSetBlipFill()) {
/* 308:382 */       return selectPaint(fp.getBlipFill(), parentPart);
/* 309:    */     }
/* 310:383 */     if (fp.isSetGradFill()) {
/* 311:384 */       return selectPaint(fp.getGradFill(), phClr, theme);
/* 312:    */     }
/* 313:385 */     if (fp.isSetMatrixStyle()) {
/* 314:386 */       return selectPaint(fp.getMatrixStyle(), theme, fp.isLineStyle(), hasPlaceholder);
/* 315:    */     }
/* 316:388 */     return null;
/* 317:    */   }
/* 318:    */   
/* 319:    */   protected static PaintStyle selectPaint(CTSolidColorFillProperties solidFill, CTSchemeColor phClr, XSLFTheme theme)
/* 320:    */   {
/* 321:393 */     if (solidFill.isSetSchemeClr()) {
/* 322:400 */       if (phClr == null) {
/* 323:401 */         phClr = solidFill.getSchemeClr();
/* 324:    */       }
/* 325:    */     }
/* 326:404 */     XSLFColor c = new XSLFColor(solidFill, theme, phClr);
/* 327:405 */     return DrawPaint.createSolidPaint(c.getColorStyle());
/* 328:    */   }
/* 329:    */   
/* 330:    */   protected static PaintStyle selectPaint(CTBlipFillProperties blipFill, final PackagePart parentPart)
/* 331:    */   {
/* 332:409 */     CTBlip blip = blipFill.getBlip();
/* 333:410 */     new PaintStyle.TexturePaint()
/* 334:    */     {
/* 335:    */       private PackagePart getPart()
/* 336:    */       {
/* 337:    */         try
/* 338:    */         {
/* 339:413 */           String blipId = this.val$blip.getEmbed();
/* 340:414 */           PackageRelationship rel = parentPart.getRelationship(blipId);
/* 341:415 */           return parentPart.getRelatedPart(rel);
/* 342:    */         }
/* 343:    */         catch (InvalidFormatException e)
/* 344:    */         {
/* 345:417 */           throw new RuntimeException(e);
/* 346:    */         }
/* 347:    */       }
/* 348:    */       
/* 349:    */       public InputStream getImageData()
/* 350:    */       {
/* 351:    */         try
/* 352:    */         {
/* 353:423 */           return getPart().getInputStream();
/* 354:    */         }
/* 355:    */         catch (IOException e)
/* 356:    */         {
/* 357:425 */           throw new RuntimeException(e);
/* 358:    */         }
/* 359:    */       }
/* 360:    */       
/* 361:    */       public String getContentType()
/* 362:    */       {
/* 363:431 */         return getPart().getContentType();
/* 364:    */       }
/* 365:    */       
/* 366:    */       public int getAlpha()
/* 367:    */       {
/* 368:435 */         return this.val$blip.sizeOfAlphaModFixArray() > 0 ? this.val$blip.getAlphaModFixArray(0).getAmt() : 100000;
/* 369:    */       }
/* 370:    */     };
/* 371:    */   }
/* 372:    */   
/* 373:    */   protected static PaintStyle selectPaint(CTGradientFillProperties gradFill, CTSchemeColor phClr, XSLFTheme theme)
/* 374:    */   {
/* 375:444 */     CTGradientStop[] gs = gradFill.getGsLst().getGsArray();
/* 376:    */     
/* 377:446 */     Arrays.sort(gs, new Comparator()
/* 378:    */     {
/* 379:    */       public int compare(CTGradientStop o1, CTGradientStop o2)
/* 380:    */       {
/* 381:448 */         Integer pos1 = Integer.valueOf(o1.getPos());
/* 382:449 */         Integer pos2 = Integer.valueOf(o2.getPos());
/* 383:450 */         return pos1.compareTo(pos2);
/* 384:    */       }
/* 385:453 */     });
/* 386:454 */     final ColorStyle[] cs = new ColorStyle[gs.length];
/* 387:455 */     final float[] fractions = new float[gs.length];
/* 388:    */     
/* 389:457 */     int i = 0;
/* 390:458 */     for (CTGradientStop cgs : gs)
/* 391:    */     {
/* 392:459 */       CTSchemeColor phClrCgs = phClr;
/* 393:460 */       if ((phClrCgs == null) && (cgs.isSetSchemeClr())) {
/* 394:461 */         phClrCgs = cgs.getSchemeClr();
/* 395:    */       }
/* 396:463 */       cs[i] = new XSLFColor(cgs, theme, phClrCgs).getColorStyle();
/* 397:464 */       fractions[i] = (cgs.getPos() / 100000.0F);
/* 398:465 */       i++;
/* 399:    */     }
/* 400:468 */     new PaintStyle.GradientPaint()
/* 401:    */     {
/* 402:    */       public double getGradientAngle()
/* 403:    */       {
/* 404:471 */         return this.val$gradFill.isSetLin() ? this.val$gradFill.getLin().getAng() / 60000.0D : 0.0D;
/* 405:    */       }
/* 406:    */       
/* 407:    */       public ColorStyle[] getGradientColors()
/* 408:    */       {
/* 409:477 */         return cs;
/* 410:    */       }
/* 411:    */       
/* 412:    */       public float[] getGradientFractions()
/* 413:    */       {
/* 414:481 */         return fractions;
/* 415:    */       }
/* 416:    */       
/* 417:    */       public boolean isRotatedWithShape()
/* 418:    */       {
/* 419:485 */         return this.val$gradFill.getRotWithShape();
/* 420:    */       }
/* 421:    */       
/* 422:    */       public PaintStyle.GradientPaint.GradientType getGradientType()
/* 423:    */       {
/* 424:489 */         if (this.val$gradFill.isSetLin()) {
/* 425:490 */           return PaintStyle.GradientPaint.GradientType.linear;
/* 426:    */         }
/* 427:493 */         if (this.val$gradFill.isSetPath())
/* 428:    */         {
/* 429:495 */           STPathShadeType.Enum ps = this.val$gradFill.getPath().getPath();
/* 430:496 */           if (ps == STPathShadeType.CIRCLE) {
/* 431:497 */             return PaintStyle.GradientPaint.GradientType.circular;
/* 432:    */           }
/* 433:498 */           if (ps == STPathShadeType.SHAPE) {
/* 434:499 */             return PaintStyle.GradientPaint.GradientType.shape;
/* 435:    */           }
/* 436:    */         }
/* 437:503 */         return PaintStyle.GradientPaint.GradientType.linear;
/* 438:    */       }
/* 439:    */     };
/* 440:    */   }
/* 441:    */   
/* 442:    */   protected static PaintStyle selectPaint(CTStyleMatrixReference fillRef, XSLFTheme theme, boolean isLineStyle, boolean hasPlaceholder)
/* 443:    */   {
/* 444:509 */     if (fillRef == null) {
/* 445:509 */       return null;
/* 446:    */     }
/* 447:516 */     int idx = (int)fillRef.getIdx();
/* 448:517 */     CTStyleMatrix matrix = theme.getXmlObject().getThemeElements().getFmtScheme();
/* 449:    */     XmlObject styleLst;
/* 450:520 */     if ((idx >= 1) && (idx <= 999))
/* 451:    */     {
/* 452:521 */       int childIdx = idx - 1;
/* 453:522 */       styleLst = isLineStyle ? matrix.getLnStyleLst() : matrix.getFillStyleLst();
/* 454:    */     }
/* 455:    */     else
/* 456:    */     {
/* 457:    */       XmlObject styleLst;
/* 458:523 */       if (idx >= 1001)
/* 459:    */       {
/* 460:524 */         int childIdx = idx - 1001;
/* 461:525 */         styleLst = matrix.getBgFillStyleLst();
/* 462:    */       }
/* 463:    */       else
/* 464:    */       {
/* 465:527 */         return null;
/* 466:    */       }
/* 467:    */     }
/* 468:    */     int childIdx;
/* 469:    */     XmlObject styleLst;
/* 470:529 */     XmlCursor cur = styleLst.newCursor();
/* 471:530 */     XSLFPropertiesDelegate.XSLFFillProperties fp = null;
/* 472:531 */     if (cur.toChild(childIdx)) {
/* 473:532 */       fp = XSLFPropertiesDelegate.getFillDelegate(cur.getObject());
/* 474:    */     }
/* 475:534 */     cur.dispose();
/* 476:    */     
/* 477:536 */     CTSchemeColor phClr = fillRef.getSchemeClr();
/* 478:537 */     PaintStyle res = selectPaint(fp, phClr, theme.getPackagePart(), theme, hasPlaceholder);
/* 479:540 */     if ((res != null) || (hasPlaceholder)) {
/* 480:541 */       return res;
/* 481:    */     }
/* 482:543 */     XSLFColor col = new XSLFColor(fillRef, theme, phClr);
/* 483:544 */     return DrawPaint.createSolidPaint(col.getColorStyle());
/* 484:    */   }
/* 485:    */   
/* 486:    */   public void draw(Graphics2D graphics, Rectangle2D bounds)
/* 487:    */   {
/* 488:549 */     DrawFactory.getInstance(graphics).drawShape(graphics, this, bounds);
/* 489:    */   }
/* 490:    */   
/* 491:    */   protected XmlObject getShapeProperties()
/* 492:    */   {
/* 493:558 */     return getChild(CTShapeProperties.class, "http://schemas.openxmlformats.org/presentationml/2006/main", "spPr");
/* 494:    */   }
/* 495:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFShape
 * JD-Core Version:    0.7.0.1
 */