/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Composite;
/*   6:    */ import java.awt.Font;
/*   7:    */ import java.awt.FontMetrics;
/*   8:    */ import java.awt.Graphics;
/*   9:    */ import java.awt.Graphics2D;
/*  10:    */ import java.awt.GraphicsConfiguration;
/*  11:    */ import java.awt.Image;
/*  12:    */ import java.awt.Paint;
/*  13:    */ import java.awt.Rectangle;
/*  14:    */ import java.awt.RenderingHints;
/*  15:    */ import java.awt.RenderingHints.Key;
/*  16:    */ import java.awt.Shape;
/*  17:    */ import java.awt.Stroke;
/*  18:    */ import java.awt.font.FontRenderContext;
/*  19:    */ import java.awt.font.GlyphVector;
/*  20:    */ import java.awt.font.TextLayout;
/*  21:    */ import java.awt.geom.AffineTransform;
/*  22:    */ import java.awt.geom.Arc2D.Float;
/*  23:    */ import java.awt.geom.Area;
/*  24:    */ import java.awt.geom.GeneralPath;
/*  25:    */ import java.awt.geom.Line2D;
/*  26:    */ import java.awt.geom.RoundRectangle2D.Float;
/*  27:    */ import java.awt.image.BufferedImage;
/*  28:    */ import java.awt.image.BufferedImageOp;
/*  29:    */ import java.awt.image.ImageObserver;
/*  30:    */ import java.awt.image.Raster;
/*  31:    */ import java.awt.image.RenderedImage;
/*  32:    */ import java.awt.image.renderable.RenderableImage;
/*  33:    */ import java.text.AttributedCharacterIterator;
/*  34:    */ import java.util.Map;
/*  35:    */ import org.apache.poi.util.POILogFactory;
/*  36:    */ import org.apache.poi.util.POILogger;
/*  37:    */ 
/*  38:    */ public final class EscherGraphics2d
/*  39:    */   extends Graphics2D
/*  40:    */ {
/*  41:    */   private EscherGraphics _escherGraphics;
/*  42:    */   private BufferedImage _img;
/*  43:    */   private AffineTransform _trans;
/*  44:    */   private Stroke _stroke;
/*  45:    */   private Paint _paint;
/*  46:    */   private Shape _deviceclip;
/*  47: 78 */   private POILogger logger = POILogFactory.getLogger(getClass());
/*  48:    */   
/*  49:    */   public EscherGraphics2d(EscherGraphics escherGraphics)
/*  50:    */   {
/*  51: 87 */     this._escherGraphics = escherGraphics;
/*  52: 88 */     setImg(new BufferedImage(1, 1, 2));
/*  53: 89 */     setColor(Color.black);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void addRenderingHints(Map<?, ?> map)
/*  57:    */   {
/*  58: 94 */     getG2D().addRenderingHints(map);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void clearRect(int i, int j, int k, int l)
/*  62:    */   {
/*  63: 99 */     Paint paint1 = getPaint();
/*  64:100 */     setColor(getBackground());
/*  65:101 */     fillRect(i, j, k, l);
/*  66:102 */     setPaint(paint1);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void clip(Shape shape)
/*  70:    */   {
/*  71:107 */     if (getDeviceclip() != null)
/*  72:    */     {
/*  73:109 */       Area area = new Area(getClip());
/*  74:110 */       if (shape != null) {
/*  75:111 */         area.intersect(new Area(shape));
/*  76:    */       }
/*  77:112 */       shape = area;
/*  78:    */     }
/*  79:114 */     setClip(shape);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void clipRect(int x, int y, int width, int height)
/*  83:    */   {
/*  84:119 */     clip(new Rectangle(x, y, width, height));
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void copyArea(int x, int y, int width, int height, int dx, int dy)
/*  88:    */   {
/*  89:125 */     getG2D().copyArea(x, y, width, height, dx, dy);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Graphics create()
/*  93:    */   {
/*  94:130 */     EscherGraphics2d g2d = new EscherGraphics2d(this._escherGraphics);
/*  95:131 */     return g2d;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void dispose()
/*  99:    */   {
/* 100:136 */     getEscherGraphics().dispose();
/* 101:137 */     getG2D().dispose();
/* 102:138 */     getImg().flush();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void draw(Shape shape)
/* 106:    */   {
/* 107:143 */     if ((shape instanceof Line2D))
/* 108:    */     {
/* 109:145 */       Line2D shape2d = (Line2D)shape;
/* 110:    */       
/* 111:147 */       int width = 0;
/* 112:148 */       if ((this._stroke != null) && ((this._stroke instanceof BasicStroke))) {
/* 113:149 */         width = (int)((BasicStroke)this._stroke).getLineWidth() * 12700;
/* 114:    */       }
/* 115:152 */       drawLine((int)shape2d.getX1(), (int)shape2d.getY1(), (int)shape2d.getX2(), (int)shape2d.getY2(), width);
/* 116:    */     }
/* 117:156 */     else if (this.logger.check(5))
/* 118:    */     {
/* 119:157 */       this.logger.log(5, new Object[] { "draw not fully supported" });
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/* 124:    */   {
/* 125:164 */     draw(new Arc2D.Float(x, y, width, height, startAngle, arcAngle, 0));
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void drawGlyphVector(GlyphVector g, float x, float y)
/* 129:    */   {
/* 130:169 */     fill(g.getOutline(x, y));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgColor, ImageObserver imageobserver)
/* 134:    */   {
/* 135:175 */     if (this.logger.check(5)) {
/* 136:176 */       this.logger.log(5, new Object[] { "drawImage() not supported" });
/* 137:    */     }
/* 138:177 */     return true;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver imageobserver)
/* 142:    */   {
/* 143:183 */     if (this.logger.check(5)) {
/* 144:184 */       this.logger.log(5, new Object[] { "drawImage() not supported" });
/* 145:    */     }
/* 146:185 */     return drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, imageobserver);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, Color bgColor, ImageObserver imageobserver)
/* 150:    */   {
/* 151:189 */     if (this.logger.check(5)) {
/* 152:190 */       this.logger.log(5, new Object[] { "drawImage() not supported" });
/* 153:    */     }
/* 154:191 */     return true;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
/* 158:    */   {
/* 159:198 */     return drawImage(img, x, y, width, height, null, observer);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public boolean drawImage(Image image, int x, int y, Color bgColor, ImageObserver imageobserver)
/* 163:    */   {
/* 164:203 */     return drawImage(image, x, y, image.getWidth(imageobserver), image.getHeight(imageobserver), bgColor, imageobserver);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean drawImage(Image image, int x, int y, ImageObserver imageobserver)
/* 168:    */   {
/* 169:208 */     return drawImage(image, x, y, image.getWidth(imageobserver), image.getHeight(imageobserver), imageobserver);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public boolean drawImage(Image image, AffineTransform affinetransform, ImageObserver imageobserver)
/* 173:    */   {
/* 174:213 */     AffineTransform affinetransform1 = (AffineTransform)getTrans().clone();
/* 175:214 */     getTrans().concatenate(affinetransform);
/* 176:215 */     drawImage(image, 0, 0, imageobserver);
/* 177:216 */     setTrans(affinetransform1);
/* 178:217 */     return true;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void drawImage(BufferedImage bufferedimage, BufferedImageOp op, int x, int y)
/* 182:    */   {
/* 183:222 */     BufferedImage img = op.filter(bufferedimage, null);
/* 184:223 */     drawImage(img, new AffineTransform(1.0F, 0.0F, 0.0F, 1.0F, x, y), null);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void drawLine(int x1, int y1, int x2, int y2, int width)
/* 188:    */   {
/* 189:228 */     getEscherGraphics().drawLine(x1, y1, x2, y2, width);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void drawLine(int x1, int y1, int x2, int y2)
/* 193:    */   {
/* 194:233 */     int width = 0;
/* 195:234 */     if ((this._stroke != null) && ((this._stroke instanceof BasicStroke))) {
/* 196:235 */       width = (int)((BasicStroke)this._stroke).getLineWidth() * 12700;
/* 197:    */     }
/* 198:237 */     getEscherGraphics().drawLine(x1, y1, x2, y2, width);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void drawOval(int x, int y, int width, int height)
/* 202:    */   {
/* 203:243 */     getEscherGraphics().drawOval(x, y, width, height);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 207:    */   {
/* 208:250 */     getEscherGraphics().drawPolygon(xPoints, yPoints, nPoints);
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
/* 212:    */   {
/* 213:255 */     if (nPoints > 0)
/* 214:    */     {
/* 215:257 */       GeneralPath generalpath = new GeneralPath();
/* 216:258 */       generalpath.moveTo(xPoints[0], yPoints[0]);
/* 217:259 */       for (int j = 1; j < nPoints; j++) {
/* 218:260 */         generalpath.lineTo(xPoints[j], yPoints[j]);
/* 219:    */       }
/* 220:262 */       draw(generalpath);
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void drawRect(int x, int y, int width, int height)
/* 225:    */   {
/* 226:268 */     this._escherGraphics.drawRect(x, y, width, height);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void drawRenderableImage(RenderableImage renderableimage, AffineTransform affinetransform)
/* 230:    */   {
/* 231:273 */     drawRenderedImage(renderableimage.createDefaultRendering(), affinetransform);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void drawRenderedImage(RenderedImage renderedimage, AffineTransform affinetransform)
/* 235:    */   {
/* 236:278 */     BufferedImage bufferedimage = new BufferedImage(renderedimage.getColorModel(), renderedimage.getData().createCompatibleWritableRaster(), false, null);
/* 237:279 */     bufferedimage.setData(renderedimage.getData());
/* 238:280 */     drawImage(bufferedimage, affinetransform, null);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void drawRoundRect(int i, int j, int k, int l, int i1, int j1)
/* 242:    */   {
/* 243:285 */     draw(new RoundRectangle2D.Float(i, j, k, l, i1, j1));
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void drawString(String string, float x, float y)
/* 247:    */   {
/* 248:290 */     getEscherGraphics().drawString(string, (int)x, (int)y);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void drawString(String string, int x, int y)
/* 252:    */   {
/* 253:295 */     getEscherGraphics().drawString(string, x, y);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void drawString(AttributedCharacterIterator attributedcharacteriterator, float x, float y)
/* 257:    */   {
/* 258:300 */     TextLayout textlayout = new TextLayout(attributedcharacteriterator, getFontRenderContext());
/* 259:301 */     Paint paint1 = getPaint();
/* 260:302 */     setColor(getColor());
/* 261:303 */     fill(textlayout.getOutline(AffineTransform.getTranslateInstance(x, y)));
/* 262:304 */     setPaint(paint1);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void drawString(AttributedCharacterIterator attributedcharacteriterator, int x, int y)
/* 266:    */   {
/* 267:309 */     getEscherGraphics().drawString(attributedcharacteriterator, x, y);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void fill(Shape shape)
/* 271:    */   {
/* 272:314 */     if (this.logger.check(5)) {
/* 273:315 */       this.logger.log(5, new Object[] { "fill(Shape) not supported" });
/* 274:    */     }
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void fillArc(int i, int j, int k, int l, int i1, int j1)
/* 278:    */   {
/* 279:320 */     fill(new Arc2D.Float(i, j, k, l, i1, j1, 2));
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void fillOval(int x, int y, int width, int height)
/* 283:    */   {
/* 284:325 */     this._escherGraphics.fillOval(x, y, width, height);
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 288:    */   {
/* 289:349 */     this._escherGraphics.fillPolygon(xPoints, yPoints, nPoints);
/* 290:    */   }
/* 291:    */   
/* 292:    */   public void fillRect(int x, int y, int width, int height)
/* 293:    */   {
/* 294:354 */     getEscherGraphics().fillRect(x, y, width, height);
/* 295:    */   }
/* 296:    */   
/* 297:    */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/* 298:    */   {
/* 299:360 */     fill(new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight));
/* 300:    */   }
/* 301:    */   
/* 302:    */   public Color getBackground()
/* 303:    */   {
/* 304:365 */     return getEscherGraphics().getBackground();
/* 305:    */   }
/* 306:    */   
/* 307:    */   public Shape getClip()
/* 308:    */   {
/* 309:    */     try
/* 310:    */     {
/* 311:372 */       return getTrans().createInverse().createTransformedShape(getDeviceclip());
/* 312:    */     }
/* 313:    */     catch (Exception _ex) {}
/* 314:376 */     return null;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public Rectangle getClipBounds()
/* 318:    */   {
/* 319:382 */     if (getDeviceclip() != null) {
/* 320:383 */       return getClip().getBounds();
/* 321:    */     }
/* 322:385 */     return null;
/* 323:    */   }
/* 324:    */   
/* 325:    */   public Color getColor()
/* 326:    */   {
/* 327:390 */     return this._escherGraphics.getColor();
/* 328:    */   }
/* 329:    */   
/* 330:    */   public Composite getComposite()
/* 331:    */   {
/* 332:395 */     return getG2D().getComposite();
/* 333:    */   }
/* 334:    */   
/* 335:    */   public GraphicsConfiguration getDeviceConfiguration()
/* 336:    */   {
/* 337:400 */     return getG2D().getDeviceConfiguration();
/* 338:    */   }
/* 339:    */   
/* 340:    */   public Font getFont()
/* 341:    */   {
/* 342:405 */     return getEscherGraphics().getFont();
/* 343:    */   }
/* 344:    */   
/* 345:    */   public FontMetrics getFontMetrics(Font font)
/* 346:    */   {
/* 347:410 */     return getEscherGraphics().getFontMetrics(font);
/* 348:    */   }
/* 349:    */   
/* 350:    */   public FontRenderContext getFontRenderContext()
/* 351:    */   {
/* 352:415 */     getG2D().setTransform(getTrans());
/* 353:416 */     return getG2D().getFontRenderContext();
/* 354:    */   }
/* 355:    */   
/* 356:    */   public Paint getPaint()
/* 357:    */   {
/* 358:421 */     return this._paint;
/* 359:    */   }
/* 360:    */   
/* 361:    */   public Object getRenderingHint(Key key)
/* 362:    */   {
/* 363:426 */     return getG2D().getRenderingHint(key);
/* 364:    */   }
/* 365:    */   
/* 366:    */   public RenderingHints getRenderingHints()
/* 367:    */   {
/* 368:431 */     return getG2D().getRenderingHints();
/* 369:    */   }
/* 370:    */   
/* 371:    */   public Stroke getStroke()
/* 372:    */   {
/* 373:436 */     return this._stroke;
/* 374:    */   }
/* 375:    */   
/* 376:    */   public AffineTransform getTransform()
/* 377:    */   {
/* 378:441 */     return (AffineTransform)getTrans().clone();
/* 379:    */   }
/* 380:    */   
/* 381:    */   public boolean hit(Rectangle rectangle, Shape shape, boolean flag)
/* 382:    */   {
/* 383:446 */     getG2D().setTransform(getTrans());
/* 384:447 */     getG2D().setStroke(getStroke());
/* 385:448 */     getG2D().setClip(getClip());
/* 386:449 */     return getG2D().hit(rectangle, shape, flag);
/* 387:    */   }
/* 388:    */   
/* 389:    */   public void rotate(double d)
/* 390:    */   {
/* 391:454 */     getTrans().rotate(d);
/* 392:    */   }
/* 393:    */   
/* 394:    */   public void rotate(double d, double d1, double d2)
/* 395:    */   {
/* 396:459 */     getTrans().rotate(d, d1, d2);
/* 397:    */   }
/* 398:    */   
/* 399:    */   public void scale(double d, double d1)
/* 400:    */   {
/* 401:464 */     getTrans().scale(d, d1);
/* 402:    */   }
/* 403:    */   
/* 404:    */   public void setBackground(Color c)
/* 405:    */   {
/* 406:469 */     getEscherGraphics().setBackground(c);
/* 407:    */   }
/* 408:    */   
/* 409:    */   public void setClip(int i, int j, int k, int l)
/* 410:    */   {
/* 411:474 */     setClip(new Rectangle(i, j, k, l));
/* 412:    */   }
/* 413:    */   
/* 414:    */   public void setClip(Shape shape)
/* 415:    */   {
/* 416:479 */     setDeviceclip(getTrans().createTransformedShape(shape));
/* 417:    */   }
/* 418:    */   
/* 419:    */   public void setColor(Color c)
/* 420:    */   {
/* 421:484 */     this._escherGraphics.setColor(c);
/* 422:    */   }
/* 423:    */   
/* 424:    */   public void setComposite(Composite composite)
/* 425:    */   {
/* 426:489 */     getG2D().setComposite(composite);
/* 427:    */   }
/* 428:    */   
/* 429:    */   public void setFont(Font font)
/* 430:    */   {
/* 431:494 */     getEscherGraphics().setFont(font);
/* 432:    */   }
/* 433:    */   
/* 434:    */   public void setPaint(Paint paint1)
/* 435:    */   {
/* 436:499 */     if (paint1 != null)
/* 437:    */     {
/* 438:501 */       this._paint = paint1;
/* 439:502 */       if ((paint1 instanceof Color)) {
/* 440:503 */         setColor((Color)paint1);
/* 441:    */       }
/* 442:    */     }
/* 443:    */   }
/* 444:    */   
/* 445:    */   public void setPaintMode()
/* 446:    */   {
/* 447:509 */     getEscherGraphics().setPaintMode();
/* 448:    */   }
/* 449:    */   
/* 450:    */   public void setRenderingHint(Key key, Object obj)
/* 451:    */   {
/* 452:514 */     getG2D().setRenderingHint(key, obj);
/* 453:    */   }
/* 454:    */   
/* 455:    */   public void setRenderingHints(Map<?, ?> map)
/* 456:    */   {
/* 457:519 */     getG2D().setRenderingHints(map);
/* 458:    */   }
/* 459:    */   
/* 460:    */   public void setStroke(Stroke s)
/* 461:    */   {
/* 462:524 */     this._stroke = s;
/* 463:    */   }
/* 464:    */   
/* 465:    */   public void setTransform(AffineTransform affinetransform)
/* 466:    */   {
/* 467:529 */     setTrans((AffineTransform)affinetransform.clone());
/* 468:    */   }
/* 469:    */   
/* 470:    */   public void setXORMode(Color color1)
/* 471:    */   {
/* 472:534 */     getEscherGraphics().setXORMode(color1);
/* 473:    */   }
/* 474:    */   
/* 475:    */   public void shear(double d, double d1)
/* 476:    */   {
/* 477:539 */     getTrans().shear(d, d1);
/* 478:    */   }
/* 479:    */   
/* 480:    */   public void transform(AffineTransform affinetransform)
/* 481:    */   {
/* 482:544 */     getTrans().concatenate(affinetransform);
/* 483:    */   }
/* 484:    */   
/* 485:    */   public void translate(double d, double d1)
/* 486:    */   {
/* 487:561 */     getTrans().translate(d, d1);
/* 488:    */   }
/* 489:    */   
/* 490:    */   public void translate(int i, int j)
/* 491:    */   {
/* 492:566 */     getTrans().translate(i, j);
/* 493:    */   }
/* 494:    */   
/* 495:    */   private EscherGraphics getEscherGraphics()
/* 496:    */   {
/* 497:571 */     return this._escherGraphics;
/* 498:    */   }
/* 499:    */   
/* 500:    */   private BufferedImage getImg()
/* 501:    */   {
/* 502:576 */     return this._img;
/* 503:    */   }
/* 504:    */   
/* 505:    */   private void setImg(BufferedImage img)
/* 506:    */   {
/* 507:581 */     this._img = img;
/* 508:    */   }
/* 509:    */   
/* 510:    */   private Graphics2D getG2D()
/* 511:    */   {
/* 512:586 */     return (Graphics2D)this._img.getGraphics();
/* 513:    */   }
/* 514:    */   
/* 515:    */   private AffineTransform getTrans()
/* 516:    */   {
/* 517:591 */     return this._trans;
/* 518:    */   }
/* 519:    */   
/* 520:    */   private void setTrans(AffineTransform trans)
/* 521:    */   {
/* 522:596 */     this._trans = trans;
/* 523:    */   }
/* 524:    */   
/* 525:    */   private Shape getDeviceclip()
/* 526:    */   {
/* 527:601 */     return this._deviceclip;
/* 528:    */   }
/* 529:    */   
/* 530:    */   private void setDeviceclip(Shape deviceclip)
/* 531:    */   {
/* 532:606 */     this._deviceclip = deviceclip;
/* 533:    */   }
/* 534:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.EscherGraphics2d

 * JD-Core Version:    0.7.0.1

 */