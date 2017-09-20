/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Font;
/*   5:    */ import java.awt.FontMetrics;
/*   6:    */ import java.awt.Graphics;
/*   7:    */ import java.awt.Image;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.awt.Shape;
/*  10:    */ import java.awt.Toolkit;
/*  11:    */ import java.awt.image.ImageObserver;
/*  12:    */ import java.text.AttributedCharacterIterator;
/*  13:    */ import org.apache.poi.hssf.util.HSSFColor;
/*  14:    */ import org.apache.poi.util.NotImplemented;
/*  15:    */ import org.apache.poi.util.POILogFactory;
/*  16:    */ import org.apache.poi.util.POILogger;
/*  17:    */ import org.apache.poi.util.SuppressForbidden;
/*  18:    */ 
/*  19:    */ public class EscherGraphics
/*  20:    */   extends Graphics
/*  21:    */ {
/*  22:    */   private final HSSFShapeGroup escherGroup;
/*  23:    */   private final HSSFWorkbook workbook;
/*  24: 66 */   private float verticalPointsPerPixel = 1.0F;
/*  25:    */   private final float verticalPixelsPerPoint;
/*  26:    */   private Color foreground;
/*  27: 69 */   private Color background = Color.white;
/*  28:    */   private Font font;
/*  29: 71 */   private static final POILogger logger = POILogFactory.getLogger(EscherGraphics.class);
/*  30:    */   
/*  31:    */   public EscherGraphics(HSSFShapeGroup escherGroup, HSSFWorkbook workbook, Color forecolor, float verticalPointsPerPixel)
/*  32:    */   {
/*  33: 83 */     this.escherGroup = escherGroup;
/*  34: 84 */     this.workbook = workbook;
/*  35: 85 */     this.verticalPointsPerPixel = verticalPointsPerPixel;
/*  36: 86 */     this.verticalPixelsPerPoint = (1.0F / verticalPointsPerPixel);
/*  37: 87 */     this.font = new Font("Arial", 0, 10);
/*  38: 88 */     this.foreground = forecolor;
/*  39:    */   }
/*  40:    */   
/*  41:    */   EscherGraphics(HSSFShapeGroup escherGroup, HSSFWorkbook workbook, Color foreground, Font font, float verticalPointsPerPixel)
/*  42:    */   {
/*  43:103 */     this.escherGroup = escherGroup;
/*  44:104 */     this.workbook = workbook;
/*  45:105 */     this.foreground = foreground;
/*  46:    */     
/*  47:107 */     this.font = font;
/*  48:108 */     this.verticalPointsPerPixel = verticalPointsPerPixel;
/*  49:109 */     this.verticalPixelsPerPoint = (1.0F / verticalPointsPerPixel);
/*  50:    */   }
/*  51:    */   
/*  52:    */   @NotImplemented
/*  53:    */   public void clearRect(int x, int y, int width, int height)
/*  54:    */   {
/*  55:129 */     Color color = this.foreground;
/*  56:130 */     setColor(this.background);
/*  57:131 */     fillRect(x, y, width, height);
/*  58:132 */     setColor(color);
/*  59:    */   }
/*  60:    */   
/*  61:    */   @NotImplemented
/*  62:    */   public void clipRect(int x, int y, int width, int height)
/*  63:    */   {
/*  64:139 */     if (logger.check(5)) {
/*  65:140 */       logger.log(5, new Object[] { "clipRect not supported" });
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   @NotImplemented
/*  70:    */   public void copyArea(int x, int y, int width, int height, int dx, int dy)
/*  71:    */   {
/*  72:147 */     if (logger.check(5)) {
/*  73:148 */       logger.log(5, new Object[] { "copyArea not supported" });
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public Graphics create()
/*  78:    */   {
/*  79:154 */     EscherGraphics g = new EscherGraphics(this.escherGroup, this.workbook, this.foreground, this.font, this.verticalPointsPerPixel);
/*  80:    */     
/*  81:156 */     return g;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void dispose() {}
/*  85:    */   
/*  86:    */   @NotImplemented
/*  87:    */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/*  88:    */   {
/*  89:169 */     if (logger.check(5)) {
/*  90:170 */       logger.log(5, new Object[] { "drawArc not supported" });
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   @NotImplemented
/*  95:    */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer)
/*  96:    */   {
/*  97:181 */     if (logger.check(5)) {
/*  98:182 */       logger.log(5, new Object[] { "drawImage not supported" });
/*  99:    */     }
/* 100:184 */     return true;
/* 101:    */   }
/* 102:    */   
/* 103:    */   @NotImplemented
/* 104:    */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
/* 105:    */   {
/* 106:194 */     if (logger.check(5)) {
/* 107:195 */       logger.log(5, new Object[] { "drawImage not supported" });
/* 108:    */     }
/* 109:196 */     return true;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean drawImage(Image image, int i, int j, int k, int l, Color color, ImageObserver imageobserver)
/* 113:    */   {
/* 114:202 */     return drawImage(image, i, j, i + k, j + l, 0, 0, image.getWidth(imageobserver), image.getHeight(imageobserver), color, imageobserver);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean drawImage(Image image, int i, int j, int k, int l, ImageObserver imageobserver)
/* 118:    */   {
/* 119:208 */     return drawImage(image, i, j, i + k, j + l, 0, 0, image.getWidth(imageobserver), image.getHeight(imageobserver), imageobserver);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean drawImage(Image image, int i, int j, Color color, ImageObserver imageobserver)
/* 123:    */   {
/* 124:214 */     return drawImage(image, i, j, image.getWidth(imageobserver), image.getHeight(imageobserver), color, imageobserver);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean drawImage(Image image, int i, int j, ImageObserver imageobserver)
/* 128:    */   {
/* 129:220 */     return drawImage(image, i, j, image.getWidth(imageobserver), image.getHeight(imageobserver), imageobserver);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void drawLine(int x1, int y1, int x2, int y2)
/* 133:    */   {
/* 134:226 */     drawLine(x1, y1, x2, y2, 0);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void drawLine(int x1, int y1, int x2, int y2, int width)
/* 138:    */   {
/* 139:231 */     HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x1, y1, x2, y2));
/* 140:232 */     shape.setShapeType(20);
/* 141:233 */     shape.setLineWidth(width);
/* 142:234 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void drawOval(int x, int y, int width, int height)
/* 146:    */   {
/* 147:240 */     HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
/* 148:241 */     shape.setShapeType(3);
/* 149:242 */     shape.setLineWidth(0);
/* 150:243 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 151:244 */     shape.setNoFill(true);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 155:    */   {
/* 156:251 */     int right = findBiggest(xPoints);
/* 157:252 */     int bottom = findBiggest(yPoints);
/* 158:253 */     int left = findSmallest(xPoints);
/* 159:254 */     int top = findSmallest(yPoints);
/* 160:255 */     HSSFPolygon shape = this.escherGroup.createPolygon(new HSSFChildAnchor(left, top, right, bottom));
/* 161:256 */     shape.setPolygonDrawArea(right - left, bottom - top);
/* 162:257 */     shape.setPoints(addToAll(xPoints, -left), addToAll(yPoints, -top));
/* 163:258 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 164:259 */     shape.setLineWidth(0);
/* 165:260 */     shape.setNoFill(true);
/* 166:    */   }
/* 167:    */   
/* 168:    */   private int[] addToAll(int[] values, int amount)
/* 169:    */   {
/* 170:265 */     int[] result = new int[values.length];
/* 171:266 */     for (int i = 0; i < values.length; i++) {
/* 172:267 */       values[i] += amount;
/* 173:    */     }
/* 174:268 */     return result;
/* 175:    */   }
/* 176:    */   
/* 177:    */   @NotImplemented
/* 178:    */   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
/* 179:    */   {
/* 180:276 */     if (logger.check(5)) {
/* 181:277 */       logger.log(5, new Object[] { "drawPolyline not supported" });
/* 182:    */     }
/* 183:    */   }
/* 184:    */   
/* 185:    */   @NotImplemented
/* 186:    */   public void drawRect(int x, int y, int width, int height)
/* 187:    */   {
/* 188:284 */     if (logger.check(5)) {
/* 189:285 */       logger.log(5, new Object[] { "drawRect not supported" });
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   @NotImplemented
/* 194:    */   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/* 195:    */   {
/* 196:293 */     if (logger.check(5)) {
/* 197:294 */       logger.log(5, new Object[] { "drawRoundRect not supported" });
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void drawString(String str, int x, int y)
/* 202:    */   {
/* 203:300 */     if ((str == null) || (str.equals(""))) {
/* 204:301 */       return;
/* 205:    */     }
/* 206:303 */     Font excelFont = this.font;
/* 207:304 */     if (this.font.getName().equals("SansSerif")) {
/* 208:306 */       excelFont = new Font("Arial", this.font.getStyle(), (int)(this.font.getSize() / this.verticalPixelsPerPoint));
/* 209:    */     } else {
/* 210:310 */       excelFont = new Font(this.font.getName(), this.font.getStyle(), (int)(this.font.getSize() / this.verticalPixelsPerPoint));
/* 211:    */     }
/* 212:312 */     FontDetails d = StaticFontMetrics.getFontDetails(excelFont);
/* 213:313 */     int width = d.getStringWidth(str) * 8 + 12;
/* 214:314 */     int height = (int)(this.font.getSize() / this.verticalPixelsPerPoint + 6.0F) * 2;
/* 215:315 */     y = (int)(y - (this.font.getSize() / this.verticalPixelsPerPoint + 2.0F * this.verticalPixelsPerPoint));
/* 216:316 */     HSSFTextbox textbox = this.escherGroup.createTextbox(new HSSFChildAnchor(x, y, x + width, y + height));
/* 217:317 */     textbox.setNoFill(true);
/* 218:318 */     textbox.setLineStyle(-1);
/* 219:319 */     HSSFRichTextString s = new HSSFRichTextString(str);
/* 220:320 */     HSSFFont hssfFont = matchFont(excelFont);
/* 221:321 */     s.applyFont(hssfFont);
/* 222:322 */     textbox.setString(s);
/* 223:    */   }
/* 224:    */   
/* 225:    */   private HSSFFont matchFont(Font matchFont)
/* 226:    */   {
/* 227:327 */     HSSFColor hssfColor = this.workbook.getCustomPalette().findColor((byte)this.foreground.getRed(), (byte)this.foreground.getGreen(), (byte)this.foreground.getBlue());
/* 228:329 */     if (hssfColor == null) {
/* 229:330 */       hssfColor = this.workbook.getCustomPalette().findSimilarColor((byte)this.foreground.getRed(), (byte)this.foreground.getGreen(), (byte)this.foreground.getBlue());
/* 230:    */     }
/* 231:331 */     boolean bold = (matchFont.getStyle() & 0x1) != 0;
/* 232:332 */     boolean italic = (matchFont.getStyle() & 0x2) != 0;
/* 233:333 */     HSSFFont hssfFont = this.workbook.findFont(bold, hssfColor.getIndex(), (short)(matchFont.getSize() * 20), matchFont.getName(), italic, false, (short)0, (byte)0);
/* 234:341 */     if (hssfFont == null)
/* 235:    */     {
/* 236:343 */       hssfFont = this.workbook.createFont();
/* 237:344 */       hssfFont.setBold(bold);
/* 238:345 */       hssfFont.setColor(hssfColor.getIndex());
/* 239:346 */       hssfFont.setFontHeight((short)(matchFont.getSize() * 20));
/* 240:347 */       hssfFont.setFontName(matchFont.getName());
/* 241:348 */       hssfFont.setItalic(italic);
/* 242:349 */       hssfFont.setStrikeout(false);
/* 243:350 */       hssfFont.setTypeOffset((short)0);
/* 244:351 */       hssfFont.setUnderline((byte)0);
/* 245:    */     }
/* 246:354 */     return hssfFont;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void drawString(AttributedCharacterIterator iterator, int x, int y)
/* 250:    */   {
/* 251:362 */     if (logger.check(5)) {
/* 252:363 */       logger.log(5, new Object[] { "drawString not supported" });
/* 253:    */     }
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/* 257:    */   {
/* 258:370 */     if (logger.check(5)) {
/* 259:371 */       logger.log(5, new Object[] { "fillArc not supported" });
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void fillOval(int x, int y, int width, int height)
/* 264:    */   {
/* 265:377 */     HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
/* 266:378 */     shape.setShapeType(3);
/* 267:379 */     shape.setLineStyle(-1);
/* 268:380 */     shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 269:381 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 270:382 */     shape.setNoFill(false);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
/* 274:    */   {
/* 275:408 */     int right = findBiggest(xPoints);
/* 276:409 */     int bottom = findBiggest(yPoints);
/* 277:410 */     int left = findSmallest(xPoints);
/* 278:411 */     int top = findSmallest(yPoints);
/* 279:412 */     HSSFPolygon shape = this.escherGroup.createPolygon(new HSSFChildAnchor(left, top, right, bottom));
/* 280:413 */     shape.setPolygonDrawArea(right - left, bottom - top);
/* 281:414 */     shape.setPoints(addToAll(xPoints, -left), addToAll(yPoints, -top));
/* 282:415 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 283:416 */     shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 284:    */   }
/* 285:    */   
/* 286:    */   private int findBiggest(int[] values)
/* 287:    */   {
/* 288:421 */     int result = -2147483648;
/* 289:422 */     for (int i = 0; i < values.length; i++) {
/* 290:424 */       if (values[i] > result) {
/* 291:425 */         result = values[i];
/* 292:    */       }
/* 293:    */     }
/* 294:427 */     return result;
/* 295:    */   }
/* 296:    */   
/* 297:    */   private int findSmallest(int[] values)
/* 298:    */   {
/* 299:432 */     int result = 2147483647;
/* 300:433 */     for (int i = 0; i < values.length; i++) {
/* 301:435 */       if (values[i] < result) {
/* 302:436 */         result = values[i];
/* 303:    */       }
/* 304:    */     }
/* 305:438 */     return result;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public void fillRect(int x, int y, int width, int height)
/* 309:    */   {
/* 310:444 */     HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
/* 311:445 */     shape.setShapeType(1);
/* 312:446 */     shape.setLineStyle(-1);
/* 313:447 */     shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 314:448 */     shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
/* 315:    */   }
/* 316:    */   
/* 317:    */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/* 318:    */   {
/* 319:455 */     if (logger.check(5)) {
/* 320:456 */       logger.log(5, new Object[] { "fillRoundRect not supported" });
/* 321:    */     }
/* 322:    */   }
/* 323:    */   
/* 324:    */   public Shape getClip()
/* 325:    */   {
/* 326:462 */     return getClipBounds();
/* 327:    */   }
/* 328:    */   
/* 329:    */   public Rectangle getClipBounds()
/* 330:    */   {
/* 331:468 */     return null;
/* 332:    */   }
/* 333:    */   
/* 334:    */   public Color getColor()
/* 335:    */   {
/* 336:474 */     return this.foreground;
/* 337:    */   }
/* 338:    */   
/* 339:    */   public Font getFont()
/* 340:    */   {
/* 341:480 */     return this.font;
/* 342:    */   }
/* 343:    */   
/* 344:    */   @SuppressForbidden
/* 345:    */   public FontMetrics getFontMetrics(Font f)
/* 346:    */   {
/* 347:488 */     return Toolkit.getDefaultToolkit().getFontMetrics(f);
/* 348:    */   }
/* 349:    */   
/* 350:    */   public void setClip(int x, int y, int width, int height)
/* 351:    */   {
/* 352:494 */     setClip(new Rectangle(x, y, width, height));
/* 353:    */   }
/* 354:    */   
/* 355:    */   @NotImplemented
/* 356:    */   public void setClip(Shape shape) {}
/* 357:    */   
/* 358:    */   public void setColor(Color color)
/* 359:    */   {
/* 360:507 */     this.foreground = color;
/* 361:    */   }
/* 362:    */   
/* 363:    */   public void setFont(Font f)
/* 364:    */   {
/* 365:513 */     this.font = f;
/* 366:    */   }
/* 367:    */   
/* 368:    */   @NotImplemented
/* 369:    */   public void setPaintMode()
/* 370:    */   {
/* 371:520 */     if (logger.check(5)) {
/* 372:521 */       logger.log(5, new Object[] { "setPaintMode not supported" });
/* 373:    */     }
/* 374:    */   }
/* 375:    */   
/* 376:    */   @NotImplemented
/* 377:    */   public void setXORMode(Color color)
/* 378:    */   {
/* 379:528 */     if (logger.check(5)) {
/* 380:529 */       logger.log(5, new Object[] { "setXORMode not supported" });
/* 381:    */     }
/* 382:    */   }
/* 383:    */   
/* 384:    */   @NotImplemented
/* 385:    */   public void translate(int x, int y)
/* 386:    */   {
/* 387:535 */     if (logger.check(5)) {
/* 388:536 */       logger.log(5, new Object[] { "translate not supported" });
/* 389:    */     }
/* 390:    */   }
/* 391:    */   
/* 392:    */   public Color getBackground()
/* 393:    */   {
/* 394:541 */     return this.background;
/* 395:    */   }
/* 396:    */   
/* 397:    */   public void setBackground(Color background)
/* 398:    */   {
/* 399:546 */     this.background = background;
/* 400:    */   }
/* 401:    */   
/* 402:    */   HSSFShapeGroup getEscherGraphics()
/* 403:    */   {
/* 404:551 */     return this.escherGroup;
/* 405:    */   }
/* 406:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.EscherGraphics
 * JD-Core Version:    0.7.0.1
 */