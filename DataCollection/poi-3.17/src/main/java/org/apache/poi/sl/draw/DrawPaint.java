/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.LinearGradientPaint;
/*   7:    */ import java.awt.Paint;
/*   8:    */ import java.awt.RadialGradientPaint;
/*   9:    */ import java.awt.TexturePaint;
/*  10:    */ import java.awt.geom.AffineTransform;
/*  11:    */ import java.awt.geom.Point2D;
/*  12:    */ import java.awt.geom.Point2D.Double;
/*  13:    */ import java.awt.geom.Rectangle2D;
/*  14:    */ import java.awt.image.BufferedImage;
/*  15:    */ import java.io.IOException;
/*  16:    */ import java.io.InputStream;
/*  17:    */ import org.apache.poi.sl.usermodel.ColorStyle;
/*  18:    */ import org.apache.poi.sl.usermodel.PaintStyle;
/*  19:    */ import org.apache.poi.sl.usermodel.PaintStyle.GradientPaint;
/*  20:    */ import org.apache.poi.sl.usermodel.PaintStyle.GradientPaint.GradientType;
/*  21:    */ import org.apache.poi.sl.usermodel.PaintStyle.PaintModifier;
/*  22:    */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*  23:    */ import org.apache.poi.sl.usermodel.PaintStyle.TexturePaint;
/*  24:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*  25:    */ import org.apache.poi.util.POILogFactory;
/*  26:    */ import org.apache.poi.util.POILogger;
/*  27:    */ 
/*  28:    */ public class DrawPaint
/*  29:    */ {
/*  30: 52 */   private static final POILogger LOG = POILogFactory.getLogger(DrawPaint.class);
/*  31: 54 */   private static final Color TRANSPARENT = new Color(1.0F, 1.0F, 1.0F, 0.0F);
/*  32:    */   protected PlaceableShape<?, ?> shape;
/*  33:    */   
/*  34:    */   public DrawPaint(PlaceableShape<?, ?> shape)
/*  35:    */   {
/*  36: 59 */     this.shape = shape;
/*  37:    */   }
/*  38:    */   
/*  39:    */   private static class SimpleSolidPaint
/*  40:    */     implements PaintStyle.SolidPaint
/*  41:    */   {
/*  42:    */     private final ColorStyle solidColor;
/*  43:    */     
/*  44:    */     SimpleSolidPaint(final Color color)
/*  45:    */     {
/*  46: 66 */       if (color == null) {
/*  47: 67 */         throw new NullPointerException("Color needs to be specified");
/*  48:    */       }
/*  49: 69 */       this.solidColor = new ColorStyle()
/*  50:    */       {
/*  51:    */         public Color getColor()
/*  52:    */         {
/*  53: 72 */           return new Color(color.getRed(), color.getGreen(), color.getBlue());
/*  54:    */         }
/*  55:    */         
/*  56:    */         public int getAlpha()
/*  57:    */         {
/*  58: 75 */           return (int)Math.round(color.getAlpha() * 100000.0D / 255.0D);
/*  59:    */         }
/*  60:    */         
/*  61:    */         public int getHueOff()
/*  62:    */         {
/*  63: 77 */           return -1;
/*  64:    */         }
/*  65:    */         
/*  66:    */         public int getHueMod()
/*  67:    */         {
/*  68: 79 */           return -1;
/*  69:    */         }
/*  70:    */         
/*  71:    */         public int getSatOff()
/*  72:    */         {
/*  73: 81 */           return -1;
/*  74:    */         }
/*  75:    */         
/*  76:    */         public int getSatMod()
/*  77:    */         {
/*  78: 83 */           return -1;
/*  79:    */         }
/*  80:    */         
/*  81:    */         public int getLumOff()
/*  82:    */         {
/*  83: 85 */           return -1;
/*  84:    */         }
/*  85:    */         
/*  86:    */         public int getLumMod()
/*  87:    */         {
/*  88: 87 */           return -1;
/*  89:    */         }
/*  90:    */         
/*  91:    */         public int getShade()
/*  92:    */         {
/*  93: 89 */           return -1;
/*  94:    */         }
/*  95:    */         
/*  96:    */         public int getTint()
/*  97:    */         {
/*  98: 91 */           return -1;
/*  99:    */         }
/* 100:    */       };
/* 101:    */     }
/* 102:    */     
/* 103:    */     SimpleSolidPaint(ColorStyle color)
/* 104:    */     {
/* 105: 96 */       if (color == null) {
/* 106: 97 */         throw new NullPointerException("Color needs to be specified");
/* 107:    */       }
/* 108: 99 */       this.solidColor = color;
/* 109:    */     }
/* 110:    */     
/* 111:    */     public ColorStyle getSolidColor()
/* 112:    */     {
/* 113:104 */       return this.solidColor;
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static PaintStyle.SolidPaint createSolidPaint(Color color)
/* 118:    */   {
/* 119:109 */     return color == null ? null : new SimpleSolidPaint(color);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static PaintStyle.SolidPaint createSolidPaint(ColorStyle color)
/* 123:    */   {
/* 124:113 */     return color == null ? null : new SimpleSolidPaint(color);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Paint getPaint(Graphics2D graphics, PaintStyle paint)
/* 128:    */   {
/* 129:117 */     return getPaint(graphics, paint, PaintStyle.PaintModifier.NORM);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Paint getPaint(Graphics2D graphics, PaintStyle paint, PaintStyle.PaintModifier modifier)
/* 133:    */   {
/* 134:121 */     if (modifier == PaintStyle.PaintModifier.NONE) {
/* 135:122 */       return null;
/* 136:    */     }
/* 137:124 */     if ((paint instanceof PaintStyle.SolidPaint)) {
/* 138:125 */       return getSolidPaint((PaintStyle.SolidPaint)paint, graphics, modifier);
/* 139:    */     }
/* 140:126 */     if ((paint instanceof PaintStyle.GradientPaint)) {
/* 141:127 */       return getGradientPaint((PaintStyle.GradientPaint)paint, graphics);
/* 142:    */     }
/* 143:128 */     if ((paint instanceof PaintStyle.TexturePaint)) {
/* 144:129 */       return getTexturePaint((PaintStyle.TexturePaint)paint, graphics);
/* 145:    */     }
/* 146:131 */     return null;
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected Paint getSolidPaint(PaintStyle.SolidPaint fill, Graphics2D graphics, final PaintStyle.PaintModifier modifier)
/* 150:    */   {
/* 151:135 */     final ColorStyle orig = fill.getSolidColor();
/* 152:136 */     ColorStyle cs = new ColorStyle()
/* 153:    */     {
/* 154:    */       public Color getColor()
/* 155:    */       {
/* 156:139 */         return orig.getColor();
/* 157:    */       }
/* 158:    */       
/* 159:    */       public int getAlpha()
/* 160:    */       {
/* 161:144 */         return orig.getAlpha();
/* 162:    */       }
/* 163:    */       
/* 164:    */       public int getHueOff()
/* 165:    */       {
/* 166:149 */         return orig.getHueOff();
/* 167:    */       }
/* 168:    */       
/* 169:    */       public int getHueMod()
/* 170:    */       {
/* 171:154 */         return orig.getHueMod();
/* 172:    */       }
/* 173:    */       
/* 174:    */       public int getSatOff()
/* 175:    */       {
/* 176:159 */         return orig.getSatOff();
/* 177:    */       }
/* 178:    */       
/* 179:    */       public int getSatMod()
/* 180:    */       {
/* 181:164 */         return orig.getSatMod();
/* 182:    */       }
/* 183:    */       
/* 184:    */       public int getLumOff()
/* 185:    */       {
/* 186:169 */         return orig.getLumOff();
/* 187:    */       }
/* 188:    */       
/* 189:    */       public int getLumMod()
/* 190:    */       {
/* 191:174 */         return orig.getLumMod();
/* 192:    */       }
/* 193:    */       
/* 194:    */       public int getShade()
/* 195:    */       {
/* 196:179 */         int shade = orig.getShade();
/* 197:180 */         switch (DrawPaint.2.$SwitchMap$org$apache$poi$sl$usermodel$PaintStyle$PaintModifier[modifier.ordinal()])
/* 198:    */         {
/* 199:    */         case 1: 
/* 200:182 */           return Math.min(100000, Math.max(0, shade) + 40000);
/* 201:    */         case 2: 
/* 202:184 */           return Math.min(100000, Math.max(0, shade) + 20000);
/* 203:    */         }
/* 204:186 */         return shade;
/* 205:    */       }
/* 206:    */       
/* 207:    */       public int getTint()
/* 208:    */       {
/* 209:192 */         int tint = orig.getTint();
/* 210:193 */         switch (DrawPaint.2.$SwitchMap$org$apache$poi$sl$usermodel$PaintStyle$PaintModifier[modifier.ordinal()])
/* 211:    */         {
/* 212:    */         case 3: 
/* 213:195 */           return Math.min(100000, Math.max(0, tint) + 40000);
/* 214:    */         case 4: 
/* 215:197 */           return Math.min(100000, Math.max(0, tint) + 20000);
/* 216:    */         }
/* 217:199 */         return tint;
/* 218:    */       }
/* 219:203 */     };
/* 220:204 */     return applyColorTransform(cs);
/* 221:    */   }
/* 222:    */   
/* 223:    */   protected Paint getGradientPaint(PaintStyle.GradientPaint fill, Graphics2D graphics)
/* 224:    */   {
/* 225:208 */     switch (2.$SwitchMap$org$apache$poi$sl$usermodel$PaintStyle$GradientPaint$GradientType[fill.getGradientType().ordinal()])
/* 226:    */     {
/* 227:    */     case 1: 
/* 228:210 */       return createLinearGradientPaint(fill, graphics);
/* 229:    */     case 2: 
/* 230:212 */       return createRadialGradientPaint(fill, graphics);
/* 231:    */     case 3: 
/* 232:214 */       return createPathGradientPaint(fill, graphics);
/* 233:    */     }
/* 234:216 */     throw new UnsupportedOperationException("gradient fill of type " + fill + " not supported.");
/* 235:    */   }
/* 236:    */   
/* 237:    */   protected Paint getTexturePaint(PaintStyle.TexturePaint fill, Graphics2D graphics)
/* 238:    */   {
/* 239:221 */     InputStream is = fill.getImageData();
/* 240:222 */     if (is == null) {
/* 241:223 */       return null;
/* 242:    */     }
/* 243:225 */     assert (graphics != null);
/* 244:    */     
/* 245:227 */     ImageRenderer renderer = DrawPictureShape.getImageRenderer(graphics, fill.getContentType());
/* 246:    */     try
/* 247:    */     {
/* 248:    */       try
/* 249:    */       {
/* 250:231 */         renderer.loadImage(is, fill.getContentType());
/* 251:    */       }
/* 252:    */       finally
/* 253:    */       {
/* 254:233 */         is.close();
/* 255:    */       }
/* 256:    */     }
/* 257:    */     catch (IOException e)
/* 258:    */     {
/* 259:236 */       LOG.log(7, new Object[] { "Can't load image data - using transparent color", e });
/* 260:237 */       return null;
/* 261:    */     }
/* 262:240 */     int alpha = fill.getAlpha();
/* 263:241 */     if ((0 <= alpha) && (alpha < 100000)) {
/* 264:242 */       renderer.setAlpha(alpha / 100000.0F);
/* 265:    */     }
/* 266:245 */     Rectangle2D textAnchor = this.shape.getAnchor();
/* 267:    */     BufferedImage image;
/* 268:    */     BufferedImage image;
/* 269:247 */     if ("image/x-wmf".equals(fill.getContentType())) {
/* 270:250 */       image = renderer.getImage(new Dimension((int)textAnchor.getWidth(), (int)textAnchor.getHeight()));
/* 271:    */     } else {
/* 272:252 */       image = renderer.getImage();
/* 273:    */     }
/* 274:255 */     if (image == null)
/* 275:    */     {
/* 276:256 */       LOG.log(7, new Object[] { "Can't load image data" });
/* 277:257 */       return null;
/* 278:    */     }
/* 279:259 */     Paint paint = new TexturePaint(image, textAnchor);
/* 280:    */     
/* 281:261 */     return paint;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public static Color applyColorTransform(ColorStyle color)
/* 285:    */   {
/* 286:274 */     if ((color == null) || (color.getColor() == null)) {
/* 287:275 */       return TRANSPARENT;
/* 288:    */     }
/* 289:278 */     Color result = color.getColor();
/* 290:    */     
/* 291:280 */     double alpha = getAlpha(result, color);
/* 292:281 */     double[] hsl = RGB2HSL(result);
/* 293:282 */     applyHslModOff(hsl, 0, color.getHueMod(), color.getHueOff());
/* 294:283 */     applyHslModOff(hsl, 1, color.getSatMod(), color.getSatOff());
/* 295:284 */     applyHslModOff(hsl, 2, color.getLumMod(), color.getLumOff());
/* 296:285 */     applyShade(hsl, color);
/* 297:286 */     applyTint(hsl, color);
/* 298:    */     
/* 299:288 */     result = HSL2RGB(hsl[0], hsl[1], hsl[2], alpha);
/* 300:    */     
/* 301:290 */     return result;
/* 302:    */   }
/* 303:    */   
/* 304:    */   private static double getAlpha(Color c, ColorStyle fc)
/* 305:    */   {
/* 306:294 */     double alpha = c.getAlpha() / 255.0D;
/* 307:295 */     int fcAlpha = fc.getAlpha();
/* 308:296 */     if (fcAlpha != -1) {
/* 309:297 */       alpha *= fcAlpha / 100000.0D;
/* 310:    */     }
/* 311:299 */     return Math.min(1.0D, Math.max(0.0D, alpha));
/* 312:    */   }
/* 313:    */   
/* 314:    */   private static void applyHslModOff(double[] hsl, int hslPart, int mod, int off)
/* 315:    */   {
/* 316:328 */     if (mod == -1) {
/* 317:329 */       mod = 100000;
/* 318:    */     }
/* 319:331 */     if (off == -1) {
/* 320:332 */       off = 0;
/* 321:    */     }
/* 322:334 */     if ((mod != 100000) || (off != 0))
/* 323:    */     {
/* 324:335 */       double fOff = off / 1000.0D;
/* 325:336 */       double fMod = mod / 100000.0D;
/* 326:337 */       hsl[hslPart] = (hsl[hslPart] * fMod + fOff);
/* 327:    */     }
/* 328:    */   }
/* 329:    */   
/* 330:    */   private static void applyShade(double[] hsl, ColorStyle fc)
/* 331:    */   {
/* 332:347 */     int shade = fc.getShade();
/* 333:348 */     if (shade == -1) {
/* 334:349 */       return;
/* 335:    */     }
/* 336:352 */     double shadePct = shade / 100000.0D;
/* 337:    */     
/* 338:354 */     hsl[2] *= (1.0D - shadePct);
/* 339:    */   }
/* 340:    */   
/* 341:    */   private static void applyTint(double[] hsl, ColorStyle fc)
/* 342:    */   {
/* 343:364 */     int tint = fc.getTint();
/* 344:365 */     if (tint == -1) {
/* 345:366 */       return;
/* 346:    */     }
/* 347:370 */     double tintPct = tint / 100000.0D;
/* 348:371 */     hsl[2] = (hsl[2] * (1.0D - tintPct) + (100.0D - 100.0D * (1.0D - tintPct)));
/* 349:    */   }
/* 350:    */   
/* 351:    */   protected Paint createLinearGradientPaint(PaintStyle.GradientPaint fill, Graphics2D graphics)
/* 352:    */   {
/* 353:379 */     double angle = fill.getGradientAngle();
/* 354:380 */     if (!fill.isRotatedWithShape()) {
/* 355:381 */       angle -= this.shape.getRotation();
/* 356:    */     }
/* 357:384 */     Rectangle2D anchor = DrawShape.getAnchor(graphics, this.shape);
/* 358:385 */     double h = anchor.getHeight();double w = anchor.getWidth();double x = anchor.getX();double y = anchor.getY();
/* 359:    */     
/* 360:387 */     AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), anchor.getCenterX(), anchor.getCenterY());
/* 361:    */     
/* 362:389 */     double diagonal = Math.sqrt(h * h + w * w);
/* 363:390 */     Point2D p1 = new Double(x + w / 2.0D - diagonal / 2.0D, y + h / 2.0D);
/* 364:391 */     p1 = at.transform(p1, null);
/* 365:    */     
/* 366:393 */     Point2D p2 = new Double(x + w, y + h / 2.0D);
/* 367:394 */     p2 = at.transform(p2, null);
/* 368:399 */     if (p1.equals(p2)) {
/* 369:401 */       return null;
/* 370:    */     }
/* 371:404 */     float[] fractions = fill.getGradientFractions();
/* 372:405 */     Color[] colors = new Color[fractions.length];
/* 373:    */     
/* 374:407 */     int i = 0;
/* 375:408 */     for (ColorStyle fc : fill.getGradientColors()) {
/* 376:410 */       colors[(i++)] = (fc == null ? TRANSPARENT : applyColorTransform(fc));
/* 377:    */     }
/* 378:413 */     return new LinearGradientPaint(p1, p2, fractions, colors);
/* 379:    */   }
/* 380:    */   
/* 381:    */   protected Paint createRadialGradientPaint(PaintStyle.GradientPaint fill, Graphics2D graphics)
/* 382:    */   {
/* 383:417 */     Rectangle2D anchor = DrawShape.getAnchor(graphics, this.shape);
/* 384:    */     
/* 385:419 */     Point2D pCenter = new Double(anchor.getX() + anchor.getWidth() / 2.0D, anchor.getY() + anchor.getHeight() / 2.0D);
/* 386:    */     
/* 387:    */ 
/* 388:422 */     float radius = (float)Math.max(anchor.getWidth(), anchor.getHeight());
/* 389:    */     
/* 390:424 */     float[] fractions = fill.getGradientFractions();
/* 391:425 */     Color[] colors = new Color[fractions.length];
/* 392:    */     
/* 393:427 */     int i = 0;
/* 394:428 */     for (ColorStyle fc : fill.getGradientColors()) {
/* 395:429 */       colors[(i++)] = applyColorTransform(fc);
/* 396:    */     }
/* 397:432 */     return new RadialGradientPaint(pCenter, radius, fractions, colors);
/* 398:    */   }
/* 399:    */   
/* 400:    */   protected Paint createPathGradientPaint(PaintStyle.GradientPaint fill, Graphics2D graphics)
/* 401:    */   {
/* 402:438 */     float[] fractions = fill.getGradientFractions();
/* 403:439 */     Color[] colors = new Color[fractions.length];
/* 404:    */     
/* 405:441 */     int i = 0;
/* 406:442 */     for (ColorStyle fc : fill.getGradientColors()) {
/* 407:443 */       colors[(i++)] = applyColorTransform(fc);
/* 408:    */     }
/* 409:446 */     return new PathGradientPaint(colors, fractions);
/* 410:    */   }
/* 411:    */   
/* 412:    */   protected void snapToAnchor(Point2D p, Rectangle2D anchor)
/* 413:    */   {
/* 414:450 */     if (p.getX() < anchor.getX()) {
/* 415:451 */       p.setLocation(anchor.getX(), p.getY());
/* 416:452 */     } else if (p.getX() > anchor.getX() + anchor.getWidth()) {
/* 417:453 */       p.setLocation(anchor.getX() + anchor.getWidth(), p.getY());
/* 418:    */     }
/* 419:456 */     if (p.getY() < anchor.getY()) {
/* 420:457 */       p.setLocation(p.getX(), anchor.getY());
/* 421:458 */     } else if (p.getY() > anchor.getY() + anchor.getHeight()) {
/* 422:459 */       p.setLocation(p.getX(), anchor.getY() + anchor.getHeight());
/* 423:    */     }
/* 424:    */   }
/* 425:    */   
/* 426:    */   public static Color HSL2RGB(double h, double s, double l, double alpha)
/* 427:    */   {
/* 428:476 */     s = Math.max(0.0D, Math.min(100.0D, s));
/* 429:477 */     l = Math.max(0.0D, Math.min(100.0D, l));
/* 430:479 */     if ((alpha < 0.0D) || (alpha > 1.0D))
/* 431:    */     {
/* 432:480 */       String message = "Color parameter outside of expected range - Alpha: " + alpha;
/* 433:481 */       throw new IllegalArgumentException(message);
/* 434:    */     }
/* 435:486 */     h %= 360.0D;
/* 436:487 */     h /= 360.0D;
/* 437:488 */     s /= 100.0D;
/* 438:489 */     l /= 100.0D;
/* 439:    */     
/* 440:491 */     double q = l < 0.5D ? l * (1.0D + s) : l + s - s * l;
/* 441:    */     
/* 442:    */ 
/* 443:    */ 
/* 444:495 */     double p = 2.0D * l - q;
/* 445:    */     
/* 446:497 */     double r = Math.max(0.0D, HUE2RGB(p, q, h + 0.3333333333333333D));
/* 447:498 */     double g = Math.max(0.0D, HUE2RGB(p, q, h));
/* 448:499 */     double b = Math.max(0.0D, HUE2RGB(p, q, h - 0.3333333333333333D));
/* 449:    */     
/* 450:501 */     r = Math.min(r, 1.0D);
/* 451:502 */     g = Math.min(g, 1.0D);
/* 452:503 */     b = Math.min(b, 1.0D);
/* 453:    */     
/* 454:505 */     return new Color((float)r, (float)g, (float)b, (float)alpha);
/* 455:    */   }
/* 456:    */   
/* 457:    */   private static double HUE2RGB(double p, double q, double h)
/* 458:    */   {
/* 459:509 */     if (h < 0.0D) {
/* 460:510 */       h += 1.0D;
/* 461:    */     }
/* 462:513 */     if (h > 1.0D) {
/* 463:514 */       h -= 1.0D;
/* 464:    */     }
/* 465:517 */     if (6.0D * h < 1.0D) {
/* 466:518 */       return p + (q - p) * 6.0D * h;
/* 467:    */     }
/* 468:521 */     if (2.0D * h < 1.0D) {
/* 469:522 */       return q;
/* 470:    */     }
/* 471:525 */     if (3.0D * h < 2.0D) {
/* 472:526 */       return p + (q - p) * 6.0D * (0.6666666666666666D - h);
/* 473:    */     }
/* 474:529 */     return p;
/* 475:    */   }
/* 476:    */   
/* 477:    */   private static double[] RGB2HSL(Color color)
/* 478:    */   {
/* 479:542 */     float[] rgb = color.getRGBColorComponents(null);
/* 480:543 */     double r = rgb[0];
/* 481:544 */     double g = rgb[1];
/* 482:545 */     double b = rgb[2];
/* 483:    */     
/* 484:    */ 
/* 485:    */ 
/* 486:549 */     double min = Math.min(r, Math.min(g, b));
/* 487:550 */     double max = Math.max(r, Math.max(g, b));
/* 488:    */     
/* 489:    */ 
/* 490:    */ 
/* 491:554 */     double h = 0.0D;
/* 492:556 */     if (max == min) {
/* 493:557 */       h = 0.0D;
/* 494:558 */     } else if (max == r) {
/* 495:559 */       h = (60.0D * (g - b) / (max - min) + 360.0D) % 360.0D;
/* 496:560 */     } else if (max == g) {
/* 497:561 */       h = 60.0D * (b - r) / (max - min) + 120.0D;
/* 498:562 */     } else if (max == b) {
/* 499:563 */       h = 60.0D * (r - g) / (max - min) + 240.0D;
/* 500:    */     }
/* 501:568 */     double l = (max + min) / 2.0D;
/* 502:    */     
/* 503:    */ 
/* 504:    */ 
/* 505:572 */     double s = 0.0D;
/* 506:574 */     if (max == min) {
/* 507:575 */       s = 0.0D;
/* 508:576 */     } else if (l <= 0.5D) {
/* 509:577 */       s = (max - min) / (max + min);
/* 510:    */     } else {
/* 511:579 */       s = (max - min) / (2.0D - max - min);
/* 512:    */     }
/* 513:582 */     return new double[] { h, s * 100.0D, l * 100.0D };
/* 514:    */   }
/* 515:    */   
/* 516:    */   public static int srgb2lin(float sRGB)
/* 517:    */   {
/* 518:593 */     if (sRGB <= 0.04045D) {
/* 519:594 */       return (int)Math.rint(100000.0D * sRGB / 12.92D);
/* 520:    */     }
/* 521:596 */     return (int)Math.rint(100000.0D * Math.pow((sRGB + 0.055D) / 1.055D, 2.4D));
/* 522:    */   }
/* 523:    */   
/* 524:    */   public static float lin2srgb(int linRGB)
/* 525:    */   {
/* 526:608 */     if (linRGB <= 0.0031308D) {
/* 527:609 */       return (float)(linRGB / 100000.0D * 12.92D);
/* 528:    */     }
/* 529:611 */     return (float)(1.055D * Math.pow(linRGB / 100000.0D, 0.4166666666666667D) - 0.055D);
/* 530:    */   }
/* 531:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.DrawPaint

 * JD-Core Version:    0.7.0.1

 */