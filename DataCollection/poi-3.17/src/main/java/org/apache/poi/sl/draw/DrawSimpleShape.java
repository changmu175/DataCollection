/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Paint;
/*   7:    */ import java.awt.Shape;
/*   8:    */ import java.awt.geom.AffineTransform;
/*   9:    */ import java.awt.geom.Ellipse2D.Double;
/*  10:    */ import java.awt.geom.Path2D.Double;
/*  11:    */ import java.awt.geom.Rectangle2D;
/*  12:    */ import java.awt.geom.Rectangle2D.Double;
/*  13:    */ import java.io.InputStream;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Collection;
/*  16:    */ import java.util.HashMap;
/*  17:    */ import java.util.List;
/*  18:    */ import java.util.Map;
/*  19:    */ import javax.xml.bind.JAXBContext;
/*  20:    */ import javax.xml.bind.JAXBElement;
/*  21:    */ import javax.xml.bind.Unmarshaller;
/*  22:    */ import javax.xml.namespace.QName;
/*  23:    */ import javax.xml.stream.EventFilter;
/*  24:    */ import javax.xml.stream.XMLEventReader;
/*  25:    */ import javax.xml.stream.XMLInputFactory;
/*  26:    */ import javax.xml.stream.events.StartElement;
/*  27:    */ import javax.xml.stream.events.XMLEvent;
/*  28:    */ import org.apache.poi.sl.draw.binding.CTCustomGeometry2D;
/*  29:    */ import org.apache.poi.sl.draw.geom.Context;
/*  30:    */ import org.apache.poi.sl.draw.geom.CustomGeometry;
/*  31:    */ import org.apache.poi.sl.draw.geom.Outline;
/*  32:    */ import org.apache.poi.sl.draw.geom.Path;
/*  33:    */ import org.apache.poi.sl.usermodel.FillStyle;
/*  34:    */ import org.apache.poi.sl.usermodel.LineDecoration;
/*  35:    */ import org.apache.poi.sl.usermodel.LineDecoration.DecorationShape;
/*  36:    */ import org.apache.poi.sl.usermodel.LineDecoration.DecorationSize;
/*  37:    */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*  38:    */ import org.apache.poi.sl.usermodel.Shadow;
/*  39:    */ import org.apache.poi.sl.usermodel.SimpleShape;
/*  40:    */ import org.apache.poi.sl.usermodel.StrokeStyle;
/*  41:    */ import org.apache.poi.util.IOUtils;
/*  42:    */ import org.apache.poi.util.StaxHelper;
/*  43:    */ import org.apache.poi.util.Units;
/*  44:    */ 
/*  45:    */ public class DrawSimpleShape
/*  46:    */   extends DrawShape
/*  47:    */ {
/*  48:    */   private static final double DECO_SIZE_POW = 1.5D;
/*  49:    */   
/*  50:    */   public DrawSimpleShape(SimpleShape<?, ?> shape)
/*  51:    */   {
/*  52: 65 */     super(shape);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void draw(Graphics2D graphics)
/*  56:    */   {
/*  57: 70 */     DrawPaint drawPaint = DrawFactory.getInstance(graphics).getPaint(getShape());
/*  58: 71 */     Paint fill = drawPaint.getPaint(graphics, getShape().getFillStyle().getPaint());
/*  59: 72 */     Paint line = drawPaint.getPaint(graphics, getShape().getStrokeStyle().getPaint());
/*  60: 73 */     BasicStroke stroke = getStroke();
/*  61: 74 */     graphics.setStroke(stroke);
/*  62:    */     
/*  63: 76 */     Collection<Outline> elems = computeOutlines(graphics);
/*  64:    */     
/*  65:    */ 
/*  66: 79 */     drawShadow(graphics, elems, fill, line);
/*  67: 82 */     if (fill != null) {
/*  68: 83 */       for (Outline o : elems) {
/*  69: 84 */         if (o.getPath().isFilled())
/*  70:    */         {
/*  71: 85 */           Paint fillMod = drawPaint.getPaint(graphics, getShape().getFillStyle().getPaint(), o.getPath().getFill());
/*  72: 86 */           if (fillMod != null)
/*  73:    */           {
/*  74: 87 */             graphics.setPaint(fillMod);
/*  75: 88 */             Shape s = o.getOutline();
/*  76: 89 */             graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
/*  77: 90 */             graphics.fill(s);
/*  78:    */           }
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82: 97 */     drawContent(graphics);
/*  83:100 */     if (line != null)
/*  84:    */     {
/*  85:101 */       graphics.setPaint(line);
/*  86:102 */       graphics.setStroke(stroke);
/*  87:103 */       for (Outline o : elems) {
/*  88:104 */         if (o.getPath().isStroked())
/*  89:    */         {
/*  90:105 */           Shape s = o.getOutline();
/*  91:106 */           graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
/*  92:107 */           graphics.draw(s);
/*  93:    */         }
/*  94:    */       }
/*  95:    */     }
/*  96:113 */     drawDecoration(graphics, line, stroke);
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void drawDecoration(Graphics2D graphics, Paint line, BasicStroke stroke)
/* 100:    */   {
/* 101:117 */     if (line == null) {
/* 102:118 */       return;
/* 103:    */     }
/* 104:120 */     graphics.setPaint(line);
/* 105:    */     
/* 106:122 */     List<Outline> lst = new ArrayList();
/* 107:123 */     LineDecoration deco = getShape().getLineDecoration();
/* 108:124 */     Outline head = getHeadDecoration(graphics, deco, stroke);
/* 109:125 */     if (head != null) {
/* 110:126 */       lst.add(head);
/* 111:    */     }
/* 112:128 */     Outline tail = getTailDecoration(graphics, deco, stroke);
/* 113:129 */     if (tail != null) {
/* 114:130 */       lst.add(tail);
/* 115:    */     }
/* 116:134 */     for (Outline o : lst)
/* 117:    */     {
/* 118:135 */       Shape s = o.getOutline();
/* 119:136 */       Path p = o.getPath();
/* 120:137 */       graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
/* 121:139 */       if (p.isFilled()) {
/* 122:140 */         graphics.fill(s);
/* 123:    */       }
/* 124:142 */       if (p.isStroked()) {
/* 125:143 */         graphics.draw(s);
/* 126:    */       }
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected Outline getTailDecoration(Graphics2D graphics, LineDecoration deco, BasicStroke stroke)
/* 131:    */   {
/* 132:149 */     if ((deco == null) || (stroke == null)) {
/* 133:150 */       return null;
/* 134:    */     }
/* 135:152 */     LineDecoration.DecorationSize tailLength = deco.getTailLength();
/* 136:153 */     if (tailLength == null) {
/* 137:154 */       tailLength = LineDecoration.DecorationSize.MEDIUM;
/* 138:    */     }
/* 139:156 */     LineDecoration.DecorationSize tailWidth = deco.getTailWidth();
/* 140:157 */     if (tailWidth == null) {
/* 141:158 */       tailWidth = LineDecoration.DecorationSize.MEDIUM;
/* 142:    */     }
/* 143:161 */     double lineWidth = Math.max(2.5D, stroke.getLineWidth());
/* 144:    */     
/* 145:163 */     Rectangle2D anchor = getAnchor(graphics, getShape());
/* 146:164 */     double x2 = anchor.getX() + anchor.getWidth();
/* 147:165 */     double y2 = anchor.getY() + anchor.getHeight();
/* 148:    */     
/* 149:167 */     double alpha = Math.atan(anchor.getHeight() / anchor.getWidth());
/* 150:    */     
/* 151:169 */     AffineTransform at = new AffineTransform();
/* 152:170 */     Shape tailShape = null;
/* 153:171 */     Path p = null;
/* 154:    */     
/* 155:173 */     double scaleY = Math.pow(1.5D, tailWidth.ordinal() + 1.0D);
/* 156:174 */     double scaleX = Math.pow(1.5D, tailLength.ordinal() + 1.0D);
/* 157:    */     
/* 158:176 */     LineDecoration.DecorationShape tailShapeEnum = deco.getTailShape();
/* 159:178 */     if (tailShapeEnum == null) {
/* 160:179 */       return null;
/* 161:    */     }
/* 162:182 */     switch (2.$SwitchMap$org$apache$poi$sl$usermodel$LineDecoration$DecorationShape[tailShapeEnum.ordinal()])
/* 163:    */     {
/* 164:    */     case 1: 
/* 165:184 */       p = new Path();
/* 166:185 */       tailShape = new Ellipse2D.Double(0.0D, 0.0D, lineWidth * scaleX, lineWidth * scaleY);
/* 167:186 */       Rectangle2D bounds = tailShape.getBounds2D();
/* 168:187 */       at.translate(x2 - bounds.getWidth() / 2.0D, y2 - bounds.getHeight() / 2.0D);
/* 169:188 */       at.rotate(alpha, bounds.getX() + bounds.getWidth() / 2.0D, bounds.getY() + bounds.getHeight() / 2.0D);
/* 170:189 */       break;
/* 171:    */     case 2: 
/* 172:    */     case 3: 
/* 173:192 */       p = new Path(false, true);
/* 174:193 */       Path2D.Double arrow = new Path2D.Double();
/* 175:194 */       arrow.moveTo(-lineWidth * scaleX, -lineWidth * scaleY / 2.0D);
/* 176:195 */       arrow.lineTo(0.0D, 0.0D);
/* 177:196 */       arrow.lineTo(-lineWidth * scaleX, lineWidth * scaleY / 2.0D);
/* 178:197 */       tailShape = arrow;
/* 179:198 */       at.translate(x2, y2);
/* 180:199 */       at.rotate(alpha);
/* 181:200 */       break;
/* 182:    */     case 4: 
/* 183:202 */       p = new Path();
/* 184:203 */       Path2D.Double triangle = new Path2D.Double();
/* 185:204 */       triangle.moveTo(-lineWidth * scaleX, -lineWidth * scaleY / 2.0D);
/* 186:205 */       triangle.lineTo(0.0D, 0.0D);
/* 187:206 */       triangle.lineTo(-lineWidth * scaleX, lineWidth * scaleY / 2.0D);
/* 188:207 */       triangle.closePath();
/* 189:208 */       tailShape = triangle;
/* 190:209 */       at.translate(x2, y2);
/* 191:210 */       at.rotate(alpha);
/* 192:211 */       break;
/* 193:    */     }
/* 194:216 */     if (tailShape != null) {
/* 195:217 */       tailShape = at.createTransformedShape(tailShape);
/* 196:    */     }
/* 197:219 */     return tailShape == null ? null : new Outline(tailShape, p);
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected Outline getHeadDecoration(Graphics2D graphics, LineDecoration deco, BasicStroke stroke)
/* 201:    */   {
/* 202:223 */     if ((deco == null) || (stroke == null)) {
/* 203:224 */       return null;
/* 204:    */     }
/* 205:226 */     LineDecoration.DecorationSize headLength = deco.getHeadLength();
/* 206:227 */     if (headLength == null) {
/* 207:228 */       headLength = LineDecoration.DecorationSize.MEDIUM;
/* 208:    */     }
/* 209:230 */     LineDecoration.DecorationSize headWidth = deco.getHeadWidth();
/* 210:231 */     if (headWidth == null) {
/* 211:232 */       headWidth = LineDecoration.DecorationSize.MEDIUM;
/* 212:    */     }
/* 213:235 */     double lineWidth = Math.max(2.5D, stroke.getLineWidth());
/* 214:    */     
/* 215:237 */     Rectangle2D anchor = getAnchor(graphics, getShape());
/* 216:238 */     double x1 = anchor.getX();double y1 = anchor.getY();
/* 217:    */     
/* 218:240 */     double alpha = Math.atan(anchor.getHeight() / anchor.getWidth());
/* 219:    */     
/* 220:242 */     AffineTransform at = new AffineTransform();
/* 221:243 */     Shape headShape = null;
/* 222:244 */     Path p = null;
/* 223:    */     
/* 224:246 */     double scaleY = Math.pow(1.5D, headWidth.ordinal() + 1.0D);
/* 225:247 */     double scaleX = Math.pow(1.5D, headLength.ordinal() + 1.0D);
/* 226:248 */     LineDecoration.DecorationShape headShapeEnum = deco.getHeadShape();
/* 227:250 */     if (headShapeEnum == null) {
/* 228:251 */       return null;
/* 229:    */     }
/* 230:254 */     switch (2.$SwitchMap$org$apache$poi$sl$usermodel$LineDecoration$DecorationShape[headShapeEnum.ordinal()])
/* 231:    */     {
/* 232:    */     case 1: 
/* 233:256 */       p = new Path();
/* 234:257 */       headShape = new Ellipse2D.Double(0.0D, 0.0D, lineWidth * scaleX, lineWidth * scaleY);
/* 235:258 */       Rectangle2D bounds = headShape.getBounds2D();
/* 236:259 */       at.translate(x1 - bounds.getWidth() / 2.0D, y1 - bounds.getHeight() / 2.0D);
/* 237:260 */       at.rotate(alpha, bounds.getX() + bounds.getWidth() / 2.0D, bounds.getY() + bounds.getHeight() / 2.0D);
/* 238:261 */       break;
/* 239:    */     case 2: 
/* 240:    */     case 3: 
/* 241:264 */       p = new Path(false, true);
/* 242:265 */       Path2D.Double arrow = new Path2D.Double();
/* 243:266 */       arrow.moveTo(lineWidth * scaleX, -lineWidth * scaleY / 2.0D);
/* 244:267 */       arrow.lineTo(0.0D, 0.0D);
/* 245:268 */       arrow.lineTo(lineWidth * scaleX, lineWidth * scaleY / 2.0D);
/* 246:269 */       headShape = arrow;
/* 247:270 */       at.translate(x1, y1);
/* 248:271 */       at.rotate(alpha);
/* 249:272 */       break;
/* 250:    */     case 4: 
/* 251:274 */       p = new Path();
/* 252:275 */       Path2D.Double triangle = new Path2D.Double();
/* 253:276 */       triangle.moveTo(lineWidth * scaleX, -lineWidth * scaleY / 2.0D);
/* 254:277 */       triangle.lineTo(0.0D, 0.0D);
/* 255:278 */       triangle.lineTo(lineWidth * scaleX, lineWidth * scaleY / 2.0D);
/* 256:279 */       triangle.closePath();
/* 257:280 */       headShape = triangle;
/* 258:281 */       at.translate(x1, y1);
/* 259:282 */       at.rotate(alpha);
/* 260:283 */       break;
/* 261:    */     }
/* 262:288 */     if (headShape != null) {
/* 263:289 */       headShape = at.createTransformedShape(headShape);
/* 264:    */     }
/* 265:291 */     return headShape == null ? null : new Outline(headShape, p);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public BasicStroke getStroke()
/* 269:    */   {
/* 270:295 */     return getStroke(getShape().getStrokeStyle());
/* 271:    */   }
/* 272:    */   
/* 273:    */   protected void drawShadow(Graphics2D graphics, Collection<Outline> outlines, Paint fill, Paint line)
/* 274:    */   {
/* 275:304 */     Shadow<?, ?> shadow = getShape().getShadow();
/* 276:305 */     if ((shadow == null) || ((fill == null) && (line == null))) {
/* 277:306 */       return;
/* 278:    */     }
/* 279:309 */     PaintStyle.SolidPaint shadowPaint = shadow.getFillStyle();
/* 280:310 */     Color shadowColor = DrawPaint.applyColorTransform(shadowPaint.getSolidColor());
/* 281:    */     
/* 282:312 */     double shapeRotation = getShape().getRotation();
/* 283:313 */     if (getShape().getFlipVertical()) {
/* 284:314 */       shapeRotation += 180.0D;
/* 285:    */     }
/* 286:316 */     double angle = shadow.getAngle() - shapeRotation;
/* 287:317 */     double dist = shadow.getDistance();
/* 288:318 */     double dx = dist * Math.cos(Math.toRadians(angle));
/* 289:319 */     double dy = dist * Math.sin(Math.toRadians(angle));
/* 290:    */     
/* 291:321 */     graphics.translate(dx, dy);
/* 292:323 */     for (Outline o : outlines)
/* 293:    */     {
/* 294:324 */       Shape s = o.getOutline();
/* 295:325 */       Path p = o.getPath();
/* 296:326 */       graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
/* 297:327 */       graphics.setPaint(shadowColor);
/* 298:329 */       if ((fill != null) && (p.isFilled())) {
/* 299:330 */         graphics.fill(s);
/* 300:331 */       } else if ((line != null) && (p.isStroked())) {
/* 301:332 */         graphics.draw(s);
/* 302:    */       }
/* 303:    */     }
/* 304:336 */     graphics.translate(-dx, -dy);
/* 305:    */   }
/* 306:    */   
/* 307:    */   protected static CustomGeometry getCustomGeometry(String name)
/* 308:    */   {
/* 309:340 */     return getCustomGeometry(name, null);
/* 310:    */   }
/* 311:    */   
/* 312:    */   protected static CustomGeometry getCustomGeometry(String name, Graphics2D graphics)
/* 313:    */   {
/* 314:345 */     Map<String, CustomGeometry> presets = graphics == null ? null : (Map)graphics.getRenderingHint(Drawable.PRESET_GEOMETRY_CACHE);
/* 315:349 */     if (presets == null)
/* 316:    */     {
/* 317:350 */       presets = new HashMap();
/* 318:351 */       if (graphics != null) {
/* 319:352 */         graphics.setRenderingHint(Drawable.PRESET_GEOMETRY_CACHE, presets);
/* 320:    */       }
/* 321:355 */       String packageName = "org.apache.poi.sl.draw.binding";
/* 322:356 */       InputStream presetIS = Drawable.class.getResourceAsStream("presetShapeDefinitions.xml");
/* 323:    */       
/* 324:    */ 
/* 325:359 */       EventFilter startElementFilter = new EventFilter()
/* 326:    */       {
/* 327:    */         public boolean accept(XMLEvent event)
/* 328:    */         {
/* 329:362 */           return event.isStartElement();
/* 330:    */         }
/* 331:    */       };
/* 332:    */       try
/* 333:    */       {
/* 334:367 */         XMLInputFactory staxFactory = StaxHelper.newXMLInputFactory();
/* 335:368 */         XMLEventReader staxReader = staxFactory.createXMLEventReader(presetIS);
/* 336:369 */         XMLEventReader staxFiltRd = staxFactory.createFilteredReader(staxReader, startElementFilter);
/* 337:    */         
/* 338:371 */         staxFiltRd.nextEvent();
/* 339:    */         
/* 340:373 */         JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
/* 341:374 */         Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/* 342:376 */         while (staxFiltRd.peek() != null)
/* 343:    */         {
/* 344:377 */           StartElement evRoot = (StartElement)staxFiltRd.peek();
/* 345:378 */           String cusName = evRoot.getName().getLocalPart();
/* 346:    */           
/* 347:380 */           JAXBElement<CTCustomGeometry2D> el = unmarshaller.unmarshal(staxReader, CTCustomGeometry2D.class);
/* 348:381 */           CTCustomGeometry2D cusGeom = (CTCustomGeometry2D)el.getValue();
/* 349:    */           
/* 350:383 */           presets.put(cusName, new CustomGeometry(cusGeom));
/* 351:    */         }
/* 352:386 */         staxFiltRd.close();
/* 353:387 */         staxReader.close();
/* 354:    */       }
/* 355:    */       catch (Exception e)
/* 356:    */       {
/* 357:389 */         throw new RuntimeException("Unable to load preset geometries.", e);
/* 358:    */       }
/* 359:    */       finally
/* 360:    */       {
/* 361:391 */         IOUtils.closeQuietly(presetIS);
/* 362:    */       }
/* 363:    */     }
/* 364:395 */     return (CustomGeometry)presets.get(name);
/* 365:    */   }
/* 366:    */   
/* 367:    */   protected Collection<Outline> computeOutlines(Graphics2D graphics)
/* 368:    */   {
/* 369:399 */     SimpleShape<?, ?> sh = getShape();
/* 370:    */     
/* 371:401 */     List<Outline> lst = new ArrayList();
/* 372:402 */     CustomGeometry geom = sh.getGeometry();
/* 373:403 */     if (geom == null) {
/* 374:404 */       return lst;
/* 375:    */     }
/* 376:407 */     Rectangle2D anchor = getAnchor(graphics, sh);
/* 377:408 */     for (Path p : geom)
/* 378:    */     {
/* 379:410 */       double w = p.getW();double h = p.getH();double scaleX = Units.toPoints(1L);double scaleY = scaleX;
/* 380:411 */       if (w == -1.0D) {
/* 381:412 */         w = Units.toEMU(anchor.getWidth());
/* 382:    */       } else {
/* 383:414 */         scaleX = anchor.getWidth() / w;
/* 384:    */       }
/* 385:416 */       if (h == -1.0D) {
/* 386:417 */         h = Units.toEMU(anchor.getHeight());
/* 387:    */       } else {
/* 388:419 */         scaleY = anchor.getHeight() / h;
/* 389:    */       }
/* 390:424 */       Rectangle2D pathAnchor = new Rectangle2D.Double(0.0D, 0.0D, w, h);
/* 391:    */       
/* 392:426 */       Context ctx = new Context(geom, pathAnchor, sh);
/* 393:    */       
/* 394:428 */       Shape gp = p.getPath(ctx);
/* 395:    */       
/* 396:    */ 
/* 397:431 */       AffineTransform at = new AffineTransform();
/* 398:432 */       at.translate(anchor.getX(), anchor.getY());
/* 399:433 */       at.scale(scaleX, scaleY);
/* 400:    */       
/* 401:435 */       Shape canvasShape = at.createTransformedShape(gp);
/* 402:    */       
/* 403:437 */       lst.add(new Outline(canvasShape, p));
/* 404:    */     }
/* 405:440 */     return lst;
/* 406:    */   }
/* 407:    */   
/* 408:    */   protected SimpleShape<?, ?> getShape()
/* 409:    */   {
/* 410:445 */     return (SimpleShape)this.shape;
/* 411:    */   }
/* 412:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawSimpleShape
 * JD-Core Version:    0.7.0.1
 */