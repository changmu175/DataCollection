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
/*  13:    */ import java.awt.Polygon;
/*  14:    */ import java.awt.Rectangle;
/*  15:    */ import java.awt.RenderingHints;
/*  16:    */ import java.awt.RenderingHints.Key;
/*  17:    */ import java.awt.Shape;
/*  18:    */ import java.awt.Stroke;
/*  19:    */ import java.awt.font.FontRenderContext;
/*  20:    */ import java.awt.font.GlyphVector;
/*  21:    */ import java.awt.geom.AffineTransform;
/*  22:    */ import java.awt.image.BufferedImage;
/*  23:    */ import java.awt.image.BufferedImageOp;
/*  24:    */ import java.awt.image.ImageObserver;
/*  25:    */ import java.awt.image.RenderedImage;
/*  26:    */ import java.awt.image.renderable.RenderableImage;
/*  27:    */ import java.io.PrintStream;
/*  28:    */ import java.text.AttributedCharacterIterator;
/*  29:    */ import java.util.Arrays;
/*  30:    */ import java.util.Map;
/*  31:    */ import org.apache.poi.util.Internal;
/*  32:    */ 
/*  33:    */ public class DummyGraphics2d
/*  34:    */   extends Graphics2D
/*  35:    */ {
/*  36:    */   private BufferedImage bufimg;
/*  37:    */   private final Graphics2D g2D;
/*  38:    */   private final PrintStream log;
/*  39:    */   
/*  40:    */   public DummyGraphics2d()
/*  41:    */   {
/*  42: 57 */     this(System.out);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public DummyGraphics2d(PrintStream log)
/*  46:    */   {
/*  47: 61 */     this.bufimg = new BufferedImage(1000, 1000, 2);
/*  48: 62 */     this.g2D = ((Graphics2D)this.bufimg.getGraphics());
/*  49: 63 */     this.log = log;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public DummyGraphics2d(PrintStream log, Graphics2D g2D)
/*  53:    */   {
/*  54: 67 */     this.g2D = g2D;
/*  55: 68 */     this.log = log;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void addRenderingHints(Map<?, ?> hints)
/*  59:    */   {
/*  60: 72 */     String l = "addRenderingHinds(Map):\n  hints = " + hints;
/*  61:    */     
/*  62:    */ 
/*  63: 75 */     this.log.println(l);
/*  64: 76 */     this.g2D.addRenderingHints(hints);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void clip(Shape s)
/*  68:    */   {
/*  69: 80 */     String l = "clip(Shape):\n  s = " + s;
/*  70:    */     
/*  71:    */ 
/*  72: 83 */     this.log.println(l);
/*  73: 84 */     this.g2D.clip(s);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void draw(Shape s)
/*  77:    */   {
/*  78: 88 */     String l = "draw(Shape):\n  s = " + s;
/*  79:    */     
/*  80:    */ 
/*  81: 91 */     this.log.println(l);
/*  82: 92 */     this.g2D.draw(s);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void drawGlyphVector(GlyphVector g, float x, float y)
/*  86:    */   {
/*  87: 96 */     String l = "drawGlyphVector(GlyphVector, float, float):\n  g = " + g + "\n  x = " + x + "\n  y = " + y;
/*  88:    */     
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:101 */     this.log.println(l);
/*  93:102 */     this.g2D.drawGlyphVector(g, x, y);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y)
/*  97:    */   {
/*  98:106 */     String l = "drawImage(BufferedImage, BufferedImageOp, x, y):\n  img = " + img + "\n  op = " + op + "\n  x = " + x + "\n  y = " + y;
/*  99:    */     
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:112 */     this.log.println(l);
/* 105:113 */     this.g2D.drawImage(img, op, x, y);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs)
/* 109:    */   {
/* 110:117 */     String l = "drawImage(Image,AfflineTransform,ImageObserver):\n  img = " + img + "\n  xform = " + xform + "\n  obs = " + obs;
/* 111:    */     
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:122 */     this.log.println(l);
/* 116:123 */     return this.g2D.drawImage(img, xform, obs);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void drawRenderableImage(RenderableImage img, AffineTransform xform)
/* 120:    */   {
/* 121:127 */     String l = "drawRenderableImage(RenderableImage, AfflineTransform):\n  img = " + img + "\n  xform = " + xform;
/* 122:    */     
/* 123:    */ 
/* 124:    */ 
/* 125:131 */     this.log.println(l);
/* 126:132 */     this.g2D.drawRenderableImage(img, xform);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void drawRenderedImage(RenderedImage img, AffineTransform xform)
/* 130:    */   {
/* 131:136 */     String l = "drawRenderedImage(RenderedImage, AffineTransform):\n  img = " + img + "\n  xform = " + xform;
/* 132:    */     
/* 133:    */ 
/* 134:    */ 
/* 135:140 */     this.log.println(l);
/* 136:141 */     this.g2D.drawRenderedImage(img, xform);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void drawString(AttributedCharacterIterator iterator, float x, float y)
/* 140:    */   {
/* 141:145 */     String l = "drawString(AttributedCharacterIterator):\n  iterator = " + iterator + "\n  x = " + x + "\n  y = " + y;
/* 142:    */     
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:150 */     this.log.println(l);
/* 147:151 */     this.g2D.drawString(iterator, x, y);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void drawString(String s, float x, float y)
/* 151:    */   {
/* 152:155 */     String l = "drawString(s,x,y):\n  s = " + s + "\n  x = " + x + "\n  y = " + y;
/* 153:    */     
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:160 */     this.log.println(l);
/* 158:161 */     this.g2D.drawString(s, x, y);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void fill(Shape s)
/* 162:    */   {
/* 163:165 */     String l = "fill(Shape):\n  s = " + s;
/* 164:    */     
/* 165:    */ 
/* 166:168 */     this.log.println(l);
/* 167:169 */     this.g2D.fill(s);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public Color getBackground()
/* 171:    */   {
/* 172:173 */     this.log.println("getBackground():");
/* 173:174 */     return this.g2D.getBackground();
/* 174:    */   }
/* 175:    */   
/* 176:    */   public Composite getComposite()
/* 177:    */   {
/* 178:178 */     this.log.println("getComposite():");
/* 179:179 */     return this.g2D.getComposite();
/* 180:    */   }
/* 181:    */   
/* 182:    */   public GraphicsConfiguration getDeviceConfiguration()
/* 183:    */   {
/* 184:183 */     this.log.println("getDeviceConfiguration():");
/* 185:184 */     return this.g2D.getDeviceConfiguration();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public FontRenderContext getFontRenderContext()
/* 189:    */   {
/* 190:188 */     this.log.println("getFontRenderContext():");
/* 191:189 */     return this.g2D.getFontRenderContext();
/* 192:    */   }
/* 193:    */   
/* 194:    */   public Paint getPaint()
/* 195:    */   {
/* 196:193 */     this.log.println("getPaint():");
/* 197:194 */     return this.g2D.getPaint();
/* 198:    */   }
/* 199:    */   
/* 200:    */   public Object getRenderingHint(Key hintKey)
/* 201:    */   {
/* 202:198 */     String l = "getRenderingHint(RenderingHints.Key):\n  hintKey = " + hintKey;
/* 203:    */     
/* 204:    */ 
/* 205:201 */     this.log.println(l);
/* 206:202 */     return this.g2D.getRenderingHint(hintKey);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public RenderingHints getRenderingHints()
/* 210:    */   {
/* 211:206 */     this.log.println("getRenderingHints():");
/* 212:207 */     return this.g2D.getRenderingHints();
/* 213:    */   }
/* 214:    */   
/* 215:    */   public Stroke getStroke()
/* 216:    */   {
/* 217:211 */     this.log.println("getStroke():");
/* 218:212 */     return this.g2D.getStroke();
/* 219:    */   }
/* 220:    */   
/* 221:    */   public AffineTransform getTransform()
/* 222:    */   {
/* 223:216 */     this.log.println("getTransform():");
/* 224:217 */     return this.g2D.getTransform();
/* 225:    */   }
/* 226:    */   
/* 227:    */   public boolean hit(Rectangle rect, Shape s, boolean onStroke)
/* 228:    */   {
/* 229:221 */     String l = "hit(Rectangle, Shape, onStroke):\n  rect = " + rect + "\n  s = " + s + "\n  onStroke = " + onStroke;
/* 230:    */     
/* 231:    */ 
/* 232:    */ 
/* 233:    */ 
/* 234:226 */     this.log.println(l);
/* 235:227 */     return this.g2D.hit(rect, s, onStroke);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void rotate(double theta)
/* 239:    */   {
/* 240:231 */     String l = "rotate(theta):\n  theta = " + theta;
/* 241:    */     
/* 242:    */ 
/* 243:234 */     this.log.println(l);
/* 244:235 */     this.g2D.rotate(theta);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public void rotate(double theta, double x, double y)
/* 248:    */   {
/* 249:239 */     String l = "rotate(double,double,double):\n  theta = " + theta + "\n  x = " + x + "\n  y = " + y;
/* 250:    */     
/* 251:    */ 
/* 252:    */ 
/* 253:    */ 
/* 254:244 */     this.log.println(l);
/* 255:245 */     this.g2D.rotate(theta, x, y);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public void scale(double sx, double sy)
/* 259:    */   {
/* 260:249 */     String l = "scale(double,double):\n  sx = " + sx + "\n  sy";
/* 261:    */     
/* 262:    */ 
/* 263:    */ 
/* 264:253 */     this.log.println(l);
/* 265:254 */     this.g2D.scale(sx, sy);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void setBackground(Color color)
/* 269:    */   {
/* 270:258 */     String l = "setBackground(Color):\n  color = " + color;
/* 271:    */     
/* 272:    */ 
/* 273:261 */     this.log.println(l);
/* 274:262 */     this.g2D.setBackground(color);
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void setComposite(Composite comp)
/* 278:    */   {
/* 279:266 */     String l = "setComposite(Composite):\n  comp = " + comp;
/* 280:    */     
/* 281:    */ 
/* 282:269 */     this.log.println(l);
/* 283:270 */     this.g2D.setComposite(comp);
/* 284:    */   }
/* 285:    */   
/* 286:    */   public void setPaint(Paint paint)
/* 287:    */   {
/* 288:274 */     String l = "setPaint(Paint):\n  paint = " + paint;
/* 289:    */     
/* 290:    */ 
/* 291:277 */     this.log.println(l);
/* 292:278 */     this.g2D.setPaint(paint);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public void setRenderingHint(Key hintKey, Object hintValue)
/* 296:    */   {
/* 297:282 */     String l = "setRenderingHint(RenderingHints.Key, Object):\n  hintKey = " + hintKey + "\n  hintValue = " + hintValue;
/* 298:    */     
/* 299:    */ 
/* 300:    */ 
/* 301:286 */     this.log.println(l);
/* 302:287 */     this.g2D.setRenderingHint(hintKey, hintValue);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void setRenderingHints(Map<?, ?> hints)
/* 306:    */   {
/* 307:291 */     String l = "setRenderingHints(Map):\n  hints = " + hints;
/* 308:    */     
/* 309:    */ 
/* 310:294 */     this.log.println(l);
/* 311:295 */     this.g2D.setRenderingHints(hints);
/* 312:    */   }
/* 313:    */   
/* 314:    */   public void setStroke(Stroke s)
/* 315:    */   {
/* 316:    */     String l;
/* 317:    */     String l;
/* 318:300 */     if ((s instanceof BasicStroke))
/* 319:    */     {
/* 320:301 */       BasicStroke bs = (BasicStroke)s;
/* 321:302 */       l = "setStroke(Stoke):\n  s = BasicStroke(\n    dash[]: " + Arrays.toString(bs.getDashArray()) + "\n    dashPhase: " + bs.getDashPhase() + "\n    endCap: " + bs.getEndCap() + "\n    lineJoin: " + bs.getLineJoin() + "\n    width: " + bs.getLineWidth() + "\n    miterLimit: " + bs.getMiterLimit() + "\n  )";
/* 322:    */     }
/* 323:    */     else
/* 324:    */     {
/* 325:312 */       l = "setStroke(Stoke):\n  s = " + s;
/* 326:    */     }
/* 327:315 */     this.log.println(l);
/* 328:316 */     this.g2D.setStroke(s);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public void setTransform(AffineTransform Tx)
/* 332:    */   {
/* 333:320 */     String l = "setTransform():\n  Tx = " + Tx;
/* 334:    */     
/* 335:    */ 
/* 336:323 */     this.log.println(l);
/* 337:324 */     this.g2D.setTransform(Tx);
/* 338:    */   }
/* 339:    */   
/* 340:    */   public void shear(double shx, double shy)
/* 341:    */   {
/* 342:328 */     String l = "shear(shx, dhy):\n  shx = " + shx + "\n  shy = " + shy;
/* 343:    */     
/* 344:    */ 
/* 345:    */ 
/* 346:332 */     this.log.println(l);
/* 347:333 */     this.g2D.shear(shx, shy);
/* 348:    */   }
/* 349:    */   
/* 350:    */   public void transform(AffineTransform Tx)
/* 351:    */   {
/* 352:337 */     String l = "transform(AffineTransform):\n  Tx = " + Tx;
/* 353:    */     
/* 354:    */ 
/* 355:340 */     this.log.println(l);
/* 356:341 */     this.g2D.transform(Tx);
/* 357:    */   }
/* 358:    */   
/* 359:    */   public void translate(double tx, double ty)
/* 360:    */   {
/* 361:345 */     String l = "translate(double, double):\n  tx = " + tx + "\n  ty = " + ty;
/* 362:    */     
/* 363:    */ 
/* 364:    */ 
/* 365:349 */     this.log.println(l);
/* 366:350 */     this.g2D.translate(tx, ty);
/* 367:    */   }
/* 368:    */   
/* 369:    */   public void clearRect(int x, int y, int width, int height)
/* 370:    */   {
/* 371:354 */     String l = "clearRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 372:    */     
/* 373:    */ 
/* 374:    */ 
/* 375:    */ 
/* 376:    */ 
/* 377:360 */     this.log.println(l);
/* 378:361 */     this.g2D.clearRect(x, y, width, height);
/* 379:    */   }
/* 380:    */   
/* 381:    */   public void clipRect(int x, int y, int width, int height)
/* 382:    */   {
/* 383:365 */     String l = "clipRect(int, int, int, int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "height = " + height;
/* 384:    */     
/* 385:    */ 
/* 386:    */ 
/* 387:    */ 
/* 388:    */ 
/* 389:371 */     this.log.println(l);
/* 390:372 */     this.g2D.clipRect(x, y, width, height);
/* 391:    */   }
/* 392:    */   
/* 393:    */   public void copyArea(int x, int y, int width, int height, int dx, int dy)
/* 394:    */   {
/* 395:376 */     String l = "copyArea(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 396:    */     
/* 397:    */ 
/* 398:    */ 
/* 399:    */ 
/* 400:    */ 
/* 401:382 */     this.log.println(l);
/* 402:383 */     this.g2D.copyArea(x, y, width, height, dx, dy);
/* 403:    */   }
/* 404:    */   
/* 405:    */   public Graphics create()
/* 406:    */   {
/* 407:387 */     this.log.println("create():");
/* 408:388 */     return this.g2D.create();
/* 409:    */   }
/* 410:    */   
/* 411:    */   public Graphics create(int x, int y, int width, int height)
/* 412:    */   {
/* 413:392 */     String l = "create(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 414:    */     
/* 415:    */ 
/* 416:    */ 
/* 417:    */ 
/* 418:    */ 
/* 419:398 */     this.log.println(l);
/* 420:399 */     return this.g2D.create(x, y, width, height);
/* 421:    */   }
/* 422:    */   
/* 423:    */   public void dispose()
/* 424:    */   {
/* 425:403 */     this.log.println("dispose():");
/* 426:404 */     this.g2D.dispose();
/* 427:    */   }
/* 428:    */   
/* 429:    */   public void draw3DRect(int x, int y, int width, int height, boolean raised)
/* 430:    */   {
/* 431:408 */     String l = "draw3DRect(int,int,int,int,boolean):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  raised = " + raised;
/* 432:    */     
/* 433:    */ 
/* 434:    */ 
/* 435:    */ 
/* 436:    */ 
/* 437:    */ 
/* 438:415 */     this.log.println(l);
/* 439:416 */     this.g2D.draw3DRect(x, y, width, height, raised);
/* 440:    */   }
/* 441:    */   
/* 442:    */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/* 443:    */   {
/* 444:420 */     String l = "drawArc(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  startAngle = " + startAngle + "\n  arcAngle = " + arcAngle;
/* 445:    */     
/* 446:    */ 
/* 447:    */ 
/* 448:    */ 
/* 449:    */ 
/* 450:    */ 
/* 451:    */ 
/* 452:428 */     this.log.println(l);
/* 453:429 */     this.g2D.drawArc(x, y, width, height, startAngle, arcAngle);
/* 454:    */   }
/* 455:    */   
/* 456:    */   public void drawBytes(byte[] data, int offset, int length, int x, int y)
/* 457:    */   {
/* 458:433 */     String l = "drawBytes(byte[],int,int,int,int):\n  data = " + Arrays.toString(data) + "\n  offset = " + offset + "\n  length = " + length + "\n  x = " + x + "\n  y = " + y;
/* 459:    */     
/* 460:    */ 
/* 461:    */ 
/* 462:    */ 
/* 463:    */ 
/* 464:    */ 
/* 465:440 */     this.log.println(l);
/* 466:441 */     this.g2D.drawBytes(data, offset, length, x, y);
/* 467:    */   }
/* 468:    */   
/* 469:    */   public void drawChars(char[] data, int offset, int length, int x, int y)
/* 470:    */   {
/* 471:445 */     String l = "drawChars(data,int,int,int,int):\n  data = " + Arrays.toString(data) + "\n  offset = " + offset + "\n  length = " + length + "\n  x = " + x + "\n  y = " + y;
/* 472:    */     
/* 473:    */ 
/* 474:    */ 
/* 475:    */ 
/* 476:    */ 
/* 477:    */ 
/* 478:452 */     this.log.println(l);
/* 479:453 */     this.g2D.drawChars(data, offset, length, x, y);
/* 480:    */   }
/* 481:    */   
/* 482:    */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
/* 483:    */   {
/* 484:457 */     String l = "drawImage(Image,int,int,int,int,int,int,int,int,ImageObserver):\n  img = " + img + "\n  dx1 = " + dx1 + "\n  dy1 = " + dy1 + "\n  dx2 = " + dx2 + "\n  dy2 = " + dy2 + "\n  sx1 = " + sx1 + "\n  sy1 = " + sy1 + "\n  sx2 = " + sx2 + "\n  sy2 = " + sy2 + "\n  observer = " + observer;
/* 485:    */     
/* 486:    */ 
/* 487:    */ 
/* 488:    */ 
/* 489:    */ 
/* 490:    */ 
/* 491:    */ 
/* 492:    */ 
/* 493:    */ 
/* 494:    */ 
/* 495:    */ 
/* 496:469 */     this.log.println(l);
/* 497:470 */     return this.g2D.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
/* 498:    */   }
/* 499:    */   
/* 500:    */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer)
/* 501:    */   {
/* 502:474 */     String l = "drawImage(Image,int,int,int,int,int,int,int,int,Color,ImageObserver):\n  img = " + img + "\n  dx1 = " + dx1 + "\n  dy1 = " + dy1 + "\n  dx2 = " + dx2 + "\n  dy2 = " + dy2 + "\n  sx1 = " + sx1 + "\n  sy1 = " + sy1 + "\n  sx2 = " + sx2 + "\n  sy2 = " + sy2 + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer;
/* 503:    */     
/* 504:    */ 
/* 505:    */ 
/* 506:    */ 
/* 507:    */ 
/* 508:    */ 
/* 509:    */ 
/* 510:    */ 
/* 511:    */ 
/* 512:    */ 
/* 513:    */ 
/* 514:    */ 
/* 515:487 */     this.log.println(l);
/* 516:488 */     return this.g2D.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
/* 517:    */   }
/* 518:    */   
/* 519:    */   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer)
/* 520:    */   {
/* 521:492 */     String l = "drawImage(Image,int,int,Color,ImageObserver):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer;
/* 522:    */     
/* 523:    */ 
/* 524:    */ 
/* 525:    */ 
/* 526:    */ 
/* 527:    */ 
/* 528:499 */     this.log.println(l);
/* 529:500 */     return this.g2D.drawImage(img, x, y, bgcolor, observer);
/* 530:    */   }
/* 531:    */   
/* 532:    */   public boolean drawImage(Image img, int x, int y, ImageObserver observer)
/* 533:    */   {
/* 534:504 */     String l = "drawImage(Image,int,int,observer):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  observer = " + observer;
/* 535:    */     
/* 536:    */ 
/* 537:    */ 
/* 538:    */ 
/* 539:    */ 
/* 540:510 */     this.log.println(l);
/* 541:511 */     return this.g2D.drawImage(img, x, y, observer);
/* 542:    */   }
/* 543:    */   
/* 544:    */   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer)
/* 545:    */   {
/* 546:515 */     String l = "drawImage(Image,int,int,int,int,Color,ImageObserver):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer;
/* 547:    */     
/* 548:    */ 
/* 549:    */ 
/* 550:    */ 
/* 551:    */ 
/* 552:    */ 
/* 553:    */ 
/* 554:    */ 
/* 555:524 */     this.log.println(l);
/* 556:525 */     return this.g2D.drawImage(img, x, y, width, height, bgcolor, observer);
/* 557:    */   }
/* 558:    */   
/* 559:    */   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
/* 560:    */   {
/* 561:529 */     String l = "drawImage(Image,int,int,width,height,observer):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  observer = " + observer;
/* 562:    */     
/* 563:    */ 
/* 564:    */ 
/* 565:    */ 
/* 566:    */ 
/* 567:    */ 
/* 568:    */ 
/* 569:537 */     this.log.println(l);
/* 570:538 */     return this.g2D.drawImage(img, x, y, width, height, observer);
/* 571:    */   }
/* 572:    */   
/* 573:    */   public void drawLine(int x1, int y1, int x2, int y2)
/* 574:    */   {
/* 575:542 */     String l = "drawLine(int,int,int,int):\n  x1 = " + x1 + "\n  y1 = " + y1 + "\n  x2 = " + x2 + "\n  y2 = " + y2;
/* 576:    */     
/* 577:    */ 
/* 578:    */ 
/* 579:    */ 
/* 580:    */ 
/* 581:548 */     this.log.println(l);
/* 582:549 */     this.g2D.drawLine(x1, y1, x2, y2);
/* 583:    */   }
/* 584:    */   
/* 585:    */   public void drawOval(int x, int y, int width, int height)
/* 586:    */   {
/* 587:553 */     String l = "drawOval(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 588:    */     
/* 589:    */ 
/* 590:    */ 
/* 591:    */ 
/* 592:    */ 
/* 593:559 */     this.log.println(l);
/* 594:560 */     this.g2D.drawOval(x, y, width, height);
/* 595:    */   }
/* 596:    */   
/* 597:    */   public void drawPolygon(Polygon p)
/* 598:    */   {
/* 599:564 */     String l = "drawPolygon(Polygon):\n  p = " + p;
/* 600:    */     
/* 601:    */ 
/* 602:567 */     this.log.println(l);
/* 603:568 */     this.g2D.drawPolygon(p);
/* 604:    */   }
/* 605:    */   
/* 606:    */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 607:    */   {
/* 608:572 */     String l = "drawPolygon(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints;
/* 609:    */     
/* 610:    */ 
/* 611:    */ 
/* 612:    */ 
/* 613:577 */     this.log.println(l);
/* 614:578 */     this.g2D.drawPolygon(xPoints, yPoints, nPoints);
/* 615:    */   }
/* 616:    */   
/* 617:    */   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
/* 618:    */   {
/* 619:582 */     String l = "drawPolyline(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints;
/* 620:    */     
/* 621:    */ 
/* 622:    */ 
/* 623:    */ 
/* 624:587 */     this.log.println(l);
/* 625:588 */     this.g2D.drawPolyline(xPoints, yPoints, nPoints);
/* 626:    */   }
/* 627:    */   
/* 628:    */   public void drawRect(int x, int y, int width, int height)
/* 629:    */   {
/* 630:592 */     String l = "drawRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 631:    */     
/* 632:    */ 
/* 633:    */ 
/* 634:    */ 
/* 635:    */ 
/* 636:598 */     this.log.println(l);
/* 637:599 */     this.g2D.drawRect(x, y, width, height);
/* 638:    */   }
/* 639:    */   
/* 640:    */   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/* 641:    */   {
/* 642:603 */     String l = "drawRoundRect(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  arcWidth = " + arcWidth + "\n  arcHeight = " + arcHeight;
/* 643:    */     
/* 644:    */ 
/* 645:    */ 
/* 646:    */ 
/* 647:    */ 
/* 648:    */ 
/* 649:    */ 
/* 650:611 */     this.log.println(l);
/* 651:612 */     this.g2D.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
/* 652:    */   }
/* 653:    */   
/* 654:    */   public void drawString(AttributedCharacterIterator iterator, int x, int y)
/* 655:    */   {
/* 656:616 */     String l = "drawString(AttributedCharacterIterator,int,int):\n  iterator = " + iterator + "\n  x = " + x + "\n  y = " + y;
/* 657:    */     
/* 658:    */ 
/* 659:    */ 
/* 660:    */ 
/* 661:621 */     this.log.println(l);
/* 662:622 */     this.g2D.drawString(iterator, x, y);
/* 663:    */   }
/* 664:    */   
/* 665:    */   public void drawString(String str, int x, int y)
/* 666:    */   {
/* 667:626 */     String l = "drawString(str,int,int):\n  str = " + str + "\n  x = " + x + "\n  y = " + y;
/* 668:    */     
/* 669:    */ 
/* 670:    */ 
/* 671:    */ 
/* 672:631 */     this.log.println(l);
/* 673:632 */     this.g2D.drawString(str, x, y);
/* 674:    */   }
/* 675:    */   
/* 676:    */   public void fill3DRect(int x, int y, int width, int height, boolean raised)
/* 677:    */   {
/* 678:636 */     String l = "fill3DRect(int,int,int,int,boolean):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  raised = " + raised;
/* 679:    */     
/* 680:    */ 
/* 681:    */ 
/* 682:    */ 
/* 683:    */ 
/* 684:    */ 
/* 685:643 */     this.log.println(l);
/* 686:644 */     this.g2D.fill3DRect(x, y, width, height, raised);
/* 687:    */   }
/* 688:    */   
/* 689:    */   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/* 690:    */   {
/* 691:648 */     String l = "fillArc(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  startAngle = " + startAngle + "\n  arcAngle = " + arcAngle;
/* 692:    */     
/* 693:    */ 
/* 694:    */ 
/* 695:    */ 
/* 696:    */ 
/* 697:    */ 
/* 698:    */ 
/* 699:656 */     this.log.println(l);
/* 700:657 */     this.g2D.fillArc(x, y, width, height, startAngle, arcAngle);
/* 701:    */   }
/* 702:    */   
/* 703:    */   public void fillOval(int x, int y, int width, int height)
/* 704:    */   {
/* 705:661 */     String l = "fillOval(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 706:    */     
/* 707:    */ 
/* 708:    */ 
/* 709:    */ 
/* 710:    */ 
/* 711:667 */     this.log.println(l);
/* 712:668 */     this.g2D.fillOval(x, y, width, height);
/* 713:    */   }
/* 714:    */   
/* 715:    */   public void fillPolygon(Polygon p)
/* 716:    */   {
/* 717:672 */     String l = "fillPolygon(Polygon):\n  p = " + p;
/* 718:    */     
/* 719:    */ 
/* 720:675 */     this.log.println(l);
/* 721:676 */     this.g2D.fillPolygon(p);
/* 722:    */   }
/* 723:    */   
/* 724:    */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 725:    */   {
/* 726:680 */     String l = "fillPolygon(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints;
/* 727:    */     
/* 728:    */ 
/* 729:    */ 
/* 730:    */ 
/* 731:685 */     this.log.println(l);
/* 732:686 */     this.g2D.fillPolygon(xPoints, yPoints, nPoints);
/* 733:    */   }
/* 734:    */   
/* 735:    */   public void fillRect(int x, int y, int width, int height)
/* 736:    */   {
/* 737:690 */     String l = "fillRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 738:    */     
/* 739:    */ 
/* 740:    */ 
/* 741:    */ 
/* 742:    */ 
/* 743:696 */     this.log.println(l);
/* 744:697 */     this.g2D.fillRect(x, y, width, height);
/* 745:    */   }
/* 746:    */   
/* 747:    */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/* 748:    */   {
/* 749:701 */     String l = "fillRoundRect(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 750:    */     
/* 751:    */ 
/* 752:    */ 
/* 753:    */ 
/* 754:    */ 
/* 755:707 */     this.log.println(l);
/* 756:708 */     this.g2D.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
/* 757:    */   }
/* 758:    */   
/* 759:    */   @Internal
/* 760:    */   public final void finalize()
/* 761:    */   {
/* 762:717 */     this.log.println("finalize():");
/* 763:718 */     this.g2D.finalize();
/* 764:719 */     super.finalize();
/* 765:    */   }
/* 766:    */   
/* 767:    */   public Shape getClip()
/* 768:    */   {
/* 769:723 */     this.log.println("getClip():");
/* 770:724 */     return this.g2D.getClip();
/* 771:    */   }
/* 772:    */   
/* 773:    */   public Rectangle getClipBounds()
/* 774:    */   {
/* 775:728 */     this.log.println("getClipBounds():");
/* 776:729 */     return this.g2D.getClipBounds();
/* 777:    */   }
/* 778:    */   
/* 779:    */   public Rectangle getClipBounds(Rectangle r)
/* 780:    */   {
/* 781:733 */     String l = "getClipBounds(Rectangle):\n  r = " + r;
/* 782:    */     
/* 783:    */ 
/* 784:736 */     this.log.println(l);
/* 785:737 */     return this.g2D.getClipBounds(r);
/* 786:    */   }
/* 787:    */   
/* 788:    */   public Color getColor()
/* 789:    */   {
/* 790:741 */     this.log.println("getColor():");
/* 791:742 */     return this.g2D.getColor();
/* 792:    */   }
/* 793:    */   
/* 794:    */   public Font getFont()
/* 795:    */   {
/* 796:746 */     this.log.println("getFont():");
/* 797:747 */     return this.g2D.getFont();
/* 798:    */   }
/* 799:    */   
/* 800:    */   public FontMetrics getFontMetrics()
/* 801:    */   {
/* 802:751 */     this.log.println("getFontMetrics():");
/* 803:752 */     return this.g2D.getFontMetrics();
/* 804:    */   }
/* 805:    */   
/* 806:    */   public FontMetrics getFontMetrics(Font f)
/* 807:    */   {
/* 808:756 */     this.log.println("getFontMetrics():");
/* 809:757 */     return this.g2D.getFontMetrics(f);
/* 810:    */   }
/* 811:    */   
/* 812:    */   public boolean hitClip(int x, int y, int width, int height)
/* 813:    */   {
/* 814:761 */     String l = "hitClip(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 815:    */     
/* 816:    */ 
/* 817:    */ 
/* 818:    */ 
/* 819:    */ 
/* 820:767 */     this.log.println(l);
/* 821:768 */     return this.g2D.hitClip(x, y, width, height);
/* 822:    */   }
/* 823:    */   
/* 824:    */   public void setClip(Shape clip)
/* 825:    */   {
/* 826:772 */     String l = "setClip(Shape):\n  clip = " + clip;
/* 827:    */     
/* 828:    */ 
/* 829:775 */     this.log.println(l);
/* 830:776 */     this.g2D.setClip(clip);
/* 831:    */   }
/* 832:    */   
/* 833:    */   public void setClip(int x, int y, int width, int height)
/* 834:    */   {
/* 835:780 */     String l = "setClip(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height;
/* 836:    */     
/* 837:    */ 
/* 838:    */ 
/* 839:    */ 
/* 840:    */ 
/* 841:786 */     this.log.println(l);
/* 842:787 */     this.g2D.setClip(x, y, width, height);
/* 843:    */   }
/* 844:    */   
/* 845:    */   public void setColor(Color c)
/* 846:    */   {
/* 847:791 */     String l = "setColor():\n  c = " + c;
/* 848:    */     
/* 849:    */ 
/* 850:794 */     this.log.println(l);
/* 851:795 */     this.g2D.setColor(c);
/* 852:    */   }
/* 853:    */   
/* 854:    */   public void setFont(Font font)
/* 855:    */   {
/* 856:799 */     String l = "setFont(Font):\n  font = " + font;
/* 857:    */     
/* 858:    */ 
/* 859:802 */     this.log.println(l);
/* 860:803 */     this.g2D.setFont(font);
/* 861:    */   }
/* 862:    */   
/* 863:    */   public void setPaintMode()
/* 864:    */   {
/* 865:807 */     this.log.println("setPaintMode():");
/* 866:808 */     this.g2D.setPaintMode();
/* 867:    */   }
/* 868:    */   
/* 869:    */   public void setXORMode(Color c1)
/* 870:    */   {
/* 871:812 */     String l = "setXORMode(Color):\n  c1 = " + c1;
/* 872:    */     
/* 873:    */ 
/* 874:815 */     this.log.println(l);
/* 875:816 */     this.g2D.setXORMode(c1);
/* 876:    */   }
/* 877:    */   
/* 878:    */   public String toString()
/* 879:    */   {
/* 880:820 */     this.log.println("toString():");
/* 881:821 */     return this.g2D.toString();
/* 882:    */   }
/* 883:    */   
/* 884:    */   public void translate(int x, int y)
/* 885:    */   {
/* 886:825 */     String l = "translate(int,int):\n  x = " + x + "\n  y = " + y;
/* 887:    */     
/* 888:    */ 
/* 889:    */ 
/* 890:829 */     this.log.println(l);
/* 891:830 */     this.g2D.translate(x, y);
/* 892:    */   }
/* 893:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.DummyGraphics2d

 * JD-Core Version:    0.7.0.1

 */