/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Font;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Paint;
/*   7:    */ import java.awt.font.FontRenderContext;
/*   8:    */ import java.awt.font.LineBreakMeasurer;
/*   9:    */ import java.awt.font.TextAttribute;
/*  10:    */ import java.awt.font.TextLayout;
/*  11:    */ import java.awt.geom.Rectangle2D;
/*  12:    */ import java.io.InvalidObjectException;
/*  13:    */ import java.text.AttributedCharacterIterator;
/*  14:    */ import java.text.AttributedCharacterIterator.Attribute;
/*  15:    */ import java.text.AttributedString;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.List;
/*  18:    */ import java.util.Locale;
/*  19:    */ import org.apache.poi.common.usermodel.fonts.FontGroup;
/*  20:    */ import org.apache.poi.common.usermodel.fonts.FontGroup.FontGroupRange;
/*  21:    */ import org.apache.poi.common.usermodel.fonts.FontInfo;
/*  22:    */ import org.apache.poi.sl.usermodel.AutoNumberingScheme;
/*  23:    */ import org.apache.poi.sl.usermodel.Hyperlink;
/*  24:    */ import org.apache.poi.sl.usermodel.Insets2D;
/*  25:    */ import org.apache.poi.sl.usermodel.PaintStyle;
/*  26:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*  27:    */ import org.apache.poi.sl.usermodel.ShapeContainer;
/*  28:    */ import org.apache.poi.sl.usermodel.Sheet;
/*  29:    */ import org.apache.poi.sl.usermodel.Slide;
/*  30:    */ import org.apache.poi.sl.usermodel.SlideShow;
/*  31:    */ import org.apache.poi.sl.usermodel.TextParagraph;
/*  32:    */ import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
/*  33:    */ import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
/*  34:    */ import org.apache.poi.sl.usermodel.TextRun;
/*  35:    */ import org.apache.poi.sl.usermodel.TextRun.FieldType;
/*  36:    */ import org.apache.poi.sl.usermodel.TextRun.TextCap;
/*  37:    */ import org.apache.poi.sl.usermodel.TextShape;
/*  38:    */ import org.apache.poi.sl.usermodel.TextShape.TextDirection;
/*  39:    */ import org.apache.poi.util.POILogFactory;
/*  40:    */ import org.apache.poi.util.POILogger;
/*  41:    */ import org.apache.poi.util.Units;
/*  42:    */ 
/*  43:    */ public class DrawTextParagraph
/*  44:    */   implements Drawable
/*  45:    */ {
/*  46: 62 */   private static final POILogger LOG = POILogFactory.getLogger(DrawTextParagraph.class);
/*  47: 65 */   public static final XlinkAttribute HYPERLINK_HREF = new XlinkAttribute("href");
/*  48: 66 */   public static final XlinkAttribute HYPERLINK_LABEL = new XlinkAttribute("label");
/*  49:    */   protected TextParagraph<?, ?, ?> paragraph;
/*  50:    */   double x;
/*  51:    */   double y;
/*  52: 70 */   protected List<DrawTextFragment> lines = new ArrayList();
/*  53:    */   protected String rawText;
/*  54:    */   protected DrawTextFragment bullet;
/*  55:    */   protected int autoNbrIdx;
/*  56:    */   protected double maxLineHeight;
/*  57:    */   
/*  58:    */   private static class XlinkAttribute
/*  59:    */     extends Attribute
/*  60:    */   {
/*  61:    */     XlinkAttribute(String name)
/*  62:    */     {
/*  63: 87 */       super();
/*  64:    */     }
/*  65:    */     
/*  66:    */     protected Object readResolve()
/*  67:    */       throws InvalidObjectException
/*  68:    */     {
/*  69: 95 */       if (DrawTextParagraph.HYPERLINK_HREF.getName().equals(getName())) {
/*  70: 96 */         return DrawTextParagraph.HYPERLINK_HREF;
/*  71:    */       }
/*  72: 98 */       if (DrawTextParagraph.HYPERLINK_LABEL.getName().equals(getName())) {
/*  73: 99 */         return DrawTextParagraph.HYPERLINK_LABEL;
/*  74:    */       }
/*  75:101 */       throw new InvalidObjectException("unknown attribute name");
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public DrawTextParagraph(TextParagraph<?, ?, ?> paragraph)
/*  80:    */   {
/*  81:107 */     this.paragraph = paragraph;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setPosition(double x, double y)
/*  85:    */   {
/*  86:112 */     this.x = x;
/*  87:113 */     this.y = y;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public double getY()
/*  91:    */   {
/*  92:117 */     return this.y;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setAutoNumberingIdx(int index)
/*  96:    */   {
/*  97:125 */     this.autoNbrIdx = index;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void draw(Graphics2D graphics)
/* 101:    */   {
/* 102:130 */     if (this.lines.isEmpty()) {
/* 103:131 */       return;
/* 104:    */     }
/* 105:134 */     double penY = this.y;
/* 106:    */     
/* 107:136 */     boolean firstLine = true;
/* 108:137 */     int indentLevel = this.paragraph.getIndentLevel();
/* 109:138 */     Double leftMargin = this.paragraph.getLeftMargin();
/* 110:139 */     if (leftMargin == null) {
/* 111:141 */       leftMargin = Double.valueOf(Units.toPoints(347663L * indentLevel));
/* 112:    */     }
/* 113:143 */     Double indent = this.paragraph.getIndent();
/* 114:144 */     if (indent == null) {
/* 115:145 */       indent = Double.valueOf(Units.toPoints(347663L * indentLevel));
/* 116:    */     }
/* 117:147 */     if (isHSLF()) {
/* 118:149 */       indent = Double.valueOf(indent.doubleValue() - leftMargin.doubleValue());
/* 119:    */     }
/* 120:158 */     Double spacing = this.paragraph.getLineSpacing();
/* 121:159 */     if (spacing == null) {
/* 122:160 */       spacing = Double.valueOf(100.0D);
/* 123:    */     }
/* 124:163 */     for (DrawTextFragment line : this.lines)
/* 125:    */     {
/* 126:    */       double penX;
/* 127:    */       double penX;
/* 128:166 */       if (firstLine)
/* 129:    */       {
/* 130:167 */         if (!isEmptyParagraph()) {
/* 131:169 */           this.bullet = getBullet(graphics, line.getAttributedString().getIterator());
/* 132:    */         }
/* 133:    */         double penX;
/* 134:172 */         if (this.bullet != null)
/* 135:    */         {
/* 136:173 */           this.bullet.setPosition(this.x + leftMargin.doubleValue() + indent.doubleValue(), penY);
/* 137:174 */           this.bullet.draw(graphics);
/* 138:    */           
/* 139:176 */           double bulletWidth = this.bullet.getLayout().getAdvance() + 1.0F;
/* 140:177 */           penX = this.x + Math.max(leftMargin.doubleValue(), leftMargin.doubleValue() + indent.doubleValue() + bulletWidth);
/* 141:    */         }
/* 142:    */         else
/* 143:    */         {
/* 144:179 */           penX = this.x + leftMargin.doubleValue();
/* 145:    */         }
/* 146:    */       }
/* 147:    */       else
/* 148:    */       {
/* 149:182 */         penX = this.x + leftMargin.doubleValue();
/* 150:    */       }
/* 151:185 */       Rectangle2D anchor = DrawShape.getAnchor(graphics, this.paragraph.getParentShape());
/* 152:    */       
/* 153:    */ 
/* 154:188 */       Insets2D insets = this.paragraph.getParentShape().getInsets();
/* 155:189 */       double leftInset = insets.left;
/* 156:190 */       double rightInset = insets.right;
/* 157:    */       
/* 158:192 */       TextParagraph.TextAlign ta = this.paragraph.getTextAlign();
/* 159:193 */       if (ta == null) {
/* 160:194 */         ta = TextParagraph.TextAlign.LEFT;
/* 161:    */       }
/* 162:196 */       switch (ta)
/* 163:    */       {
/* 164:    */       case CENTER: 
/* 165:198 */         penX += (anchor.getWidth() - line.getWidth() - leftInset - rightInset - leftMargin.doubleValue()) / 2.0D;
/* 166:199 */         break;
/* 167:    */       case RIGHT: 
/* 168:201 */         penX += anchor.getWidth() - line.getWidth() - leftInset - rightInset;
/* 169:202 */         break;
/* 170:    */       }
/* 171:207 */       line.setPosition(penX, penY);
/* 172:208 */       line.draw(graphics);
/* 173:210 */       if (spacing.doubleValue() > 0.0D) {
/* 174:212 */         penY += spacing.doubleValue() * 0.01D * line.getHeight();
/* 175:    */       } else {
/* 176:215 */         penY += -spacing.doubleValue();
/* 177:    */       }
/* 178:218 */       firstLine = false;
/* 179:    */     }
/* 180:221 */     this.y = (penY - this.y);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public float getFirstLineLeading()
/* 184:    */   {
/* 185:225 */     return this.lines.isEmpty() ? 0.0F : ((DrawTextFragment)this.lines.get(0)).getLeading();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public float getFirstLineHeight()
/* 189:    */   {
/* 190:229 */     return this.lines.isEmpty() ? 0.0F : ((DrawTextFragment)this.lines.get(0)).getHeight();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public float getLastLineHeight()
/* 194:    */   {
/* 195:233 */     return this.lines.isEmpty() ? 0.0F : ((DrawTextFragment)this.lines.get(this.lines.size() - 1)).getHeight();
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean isEmptyParagraph()
/* 199:    */   {
/* 200:237 */     return (this.lines.isEmpty()) || (this.rawText.trim().isEmpty());
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void applyTransform(Graphics2D graphics) {}
/* 204:    */   
/* 205:    */   public void drawContent(Graphics2D graphics) {}
/* 206:    */   
/* 207:    */   protected void breakText(Graphics2D graphics)
/* 208:    */   {
/* 209:254 */     this.lines.clear();
/* 210:    */     
/* 211:256 */     DrawFactory fact = DrawFactory.getInstance(graphics);
/* 212:257 */     fact.fixFonts(graphics);
/* 213:258 */     StringBuilder text = new StringBuilder();
/* 214:259 */     AttributedString at = getAttributedString(graphics, text);
/* 215:260 */     boolean emptyParagraph = "".equals(text.toString().trim());
/* 216:    */     
/* 217:262 */     AttributedCharacterIterator it = at.getIterator();
/* 218:263 */     LineBreakMeasurer measurer = new LineBreakMeasurer(it, graphics.getFontRenderContext());
/* 219:    */     for (;;)
/* 220:    */     {
/* 221:265 */       int startIndex = measurer.getPosition();
/* 222:    */       
/* 223:    */ 
/* 224:268 */       double wrappingWidth = getWrappingWidth(this.lines.isEmpty(), graphics) + 1.0D;
/* 225:270 */       if (wrappingWidth < 0.0D) {
/* 226:271 */         wrappingWidth = 1.0D;
/* 227:    */       }
/* 228:274 */       int nextBreak = text.indexOf("\n", startIndex + 1);
/* 229:275 */       if (nextBreak == -1) {
/* 230:276 */         nextBreak = it.getEndIndex();
/* 231:    */       }
/* 232:279 */       TextLayout layout = measurer.nextLayout((float)wrappingWidth, nextBreak, true);
/* 233:280 */       if (layout == null) {
/* 234:283 */         layout = measurer.nextLayout((float)wrappingWidth, nextBreak, false);
/* 235:    */       }
/* 236:286 */       if (layout == null) {
/* 237:    */         break;
/* 238:    */       }
/* 239:291 */       int endIndex = measurer.getPosition();
/* 240:293 */       if ((endIndex < it.getEndIndex()) && (text.charAt(endIndex) == '\n')) {
/* 241:294 */         measurer.setPosition(endIndex + 1);
/* 242:    */       }
/* 243:297 */       TextParagraph.TextAlign hAlign = this.paragraph.getTextAlign();
/* 244:298 */       if ((hAlign == TextParagraph.TextAlign.JUSTIFY) || (hAlign == TextParagraph.TextAlign.JUSTIFY_LOW)) {
/* 245:299 */         layout = layout.getJustifiedLayout((float)wrappingWidth);
/* 246:    */       }
/* 247:302 */       AttributedString str = emptyParagraph ? null : new AttributedString(it, startIndex, endIndex);
/* 248:    */       
/* 249:    */ 
/* 250:305 */       DrawTextFragment line = fact.getTextFragment(layout, str);
/* 251:306 */       this.lines.add(line);
/* 252:    */       
/* 253:308 */       this.maxLineHeight = Math.max(this.maxLineHeight, line.getHeight());
/* 254:310 */       if (endIndex == it.getEndIndex()) {
/* 255:    */         break;
/* 256:    */       }
/* 257:    */     }
/* 258:315 */     this.rawText = text.toString();
/* 259:    */   }
/* 260:    */   
/* 261:    */   protected DrawTextFragment getBullet(Graphics2D graphics, AttributedCharacterIterator firstLineAttr)
/* 262:    */   {
/* 263:319 */     TextParagraph.BulletStyle bulletStyle = this.paragraph.getBulletStyle();
/* 264:320 */     if (bulletStyle == null) {
/* 265:321 */       return null;
/* 266:    */     }
/* 267:325 */     AutoNumberingScheme ans = bulletStyle.getAutoNumberingScheme();
/* 268:    */     String buCharacter;
/* 269:    */     String buCharacter;
/* 270:326 */     if (ans != null) {
/* 271:327 */       buCharacter = ans.format(this.autoNbrIdx);
/* 272:    */     } else {
/* 273:329 */       buCharacter = bulletStyle.getBulletCharacter();
/* 274:    */     }
/* 275:331 */     if (buCharacter == null) {
/* 276:332 */       return null;
/* 277:    */     }
/* 278:335 */     PlaceableShape<?, ?> ps = getParagraphShape();
/* 279:336 */     PaintStyle fgPaintStyle = bulletStyle.getBulletFontColor();
/* 280:    */     Paint fgPaint;
/* 281:    */     Paint fgPaint;
/* 282:338 */     if (fgPaintStyle == null) {
/* 283:339 */       fgPaint = (Paint)firstLineAttr.getAttribute(TextAttribute.FOREGROUND);
/* 284:    */     } else {
/* 285:341 */       fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
/* 286:    */     }
/* 287:344 */     float fontSize = ((Float)firstLineAttr.getAttribute(TextAttribute.SIZE)).floatValue();
/* 288:345 */     Double buSz = bulletStyle.getBulletFontSize();
/* 289:346 */     if (buSz == null) {
/* 290:347 */       buSz = Double.valueOf(100.0D);
/* 291:    */     }
/* 292:349 */     if (buSz.doubleValue() > 0.0D) {
/* 293:350 */       fontSize = (float)(fontSize * (buSz.doubleValue() * 0.01D));
/* 294:    */     } else {
/* 295:352 */       fontSize = (float)-buSz.doubleValue();
/* 296:    */     }
/* 297:355 */     String buFontStr = bulletStyle.getBulletFont();
/* 298:356 */     if (buFontStr == null) {
/* 299:357 */       buFontStr = this.paragraph.getDefaultFontFamily();
/* 300:    */     }
/* 301:359 */     assert (buFontStr != null);
/* 302:360 */     FontInfo buFont = new DrawFontInfo(buFontStr);
/* 303:    */     
/* 304:    */ 
/* 305:363 */     DrawFontManager dfm = DrawFactory.getInstance(graphics).getFontManager(graphics);
/* 306:    */     
/* 307:365 */     buFont = dfm.getMappedFont(graphics, buFont);
/* 308:    */     
/* 309:367 */     AttributedString str = new AttributedString(dfm.mapFontCharset(graphics, buFont, buCharacter));
/* 310:368 */     str.addAttribute(TextAttribute.FOREGROUND, fgPaint);
/* 311:369 */     str.addAttribute(TextAttribute.FAMILY, buFont.getTypeface());
/* 312:370 */     str.addAttribute(TextAttribute.SIZE, Float.valueOf(fontSize));
/* 313:    */     
/* 314:372 */     TextLayout layout = new TextLayout(str.getIterator(), graphics.getFontRenderContext());
/* 315:373 */     DrawFactory fact = DrawFactory.getInstance(graphics);
/* 316:374 */     return fact.getTextFragment(layout, str);
/* 317:    */   }
/* 318:    */   
/* 319:    */   protected String getRenderableText(Graphics2D graphics, TextRun tr)
/* 320:    */   {
/* 321:378 */     if (tr.getFieldType() == TextRun.FieldType.SLIDE_NUMBER)
/* 322:    */     {
/* 323:379 */       Slide<?, ?> slide = (Slide)graphics.getRenderingHint(Drawable.CURRENT_SLIDE);
/* 324:380 */       return slide == null ? "" : Integer.toString(slide.getSlideNumber());
/* 325:    */     }
/* 326:382 */     StringBuilder buf = new StringBuilder();
/* 327:383 */     TextRun.TextCap cap = tr.getTextCap();
/* 328:384 */     String tabs = null;
/* 329:385 */     for (char c : tr.getRawText().toCharArray()) {
/* 330:386 */       switch (c)
/* 331:    */       {
/* 332:    */       case '\t': 
/* 333:388 */         if (tabs == null) {
/* 334:389 */           tabs = tab2space(tr);
/* 335:    */         }
/* 336:391 */         buf.append(tabs);
/* 337:392 */         break;
/* 338:    */       case '\013': 
/* 339:394 */         buf.append('\n');
/* 340:395 */         break;
/* 341:    */       default: 
/* 342:397 */         switch (2.$SwitchMap$org$apache$poi$sl$usermodel$TextRun$TextCap[cap.ordinal()])
/* 343:    */         {
/* 344:    */         case 1: 
/* 345:398 */           c = Character.toUpperCase(c); break;
/* 346:    */         case 2: 
/* 347:399 */           c = Character.toLowerCase(c); break;
/* 348:    */         }
/* 349:403 */         buf.append(c);
/* 350:    */       }
/* 351:    */     }
/* 352:408 */     return buf.toString();
/* 353:    */   }
/* 354:    */   
/* 355:    */   private String tab2space(TextRun tr)
/* 356:    */   {
/* 357:415 */     AttributedString string = new AttributedString(" ");
/* 358:416 */     String fontFamily = tr.getFontFamily();
/* 359:417 */     if (fontFamily == null) {
/* 360:418 */       fontFamily = "Lucida Sans";
/* 361:    */     }
/* 362:420 */     string.addAttribute(TextAttribute.FAMILY, fontFamily);
/* 363:    */     
/* 364:422 */     Double fs = tr.getFontSize();
/* 365:423 */     if (fs == null) {
/* 366:424 */       fs = Double.valueOf(12.0D);
/* 367:    */     }
/* 368:426 */     string.addAttribute(TextAttribute.SIZE, Float.valueOf(fs.floatValue()));
/* 369:    */     
/* 370:428 */     TextLayout l = new TextLayout(string.getIterator(), new FontRenderContext(null, true, true));
/* 371:429 */     double wspace = l.getAdvance();
/* 372:    */     
/* 373:431 */     Double tabSz = this.paragraph.getDefaultTabSize();
/* 374:432 */     if (tabSz == null) {
/* 375:433 */       tabSz = Double.valueOf(wspace * 4.0D);
/* 376:    */     }
/* 377:436 */     int numSpaces = (int)Math.ceil(tabSz.doubleValue() / wspace);
/* 378:437 */     StringBuilder buf = new StringBuilder();
/* 379:438 */     for (int i = 0; i < numSpaces; i++) {
/* 380:439 */       buf.append(' ');
/* 381:    */     }
/* 382:441 */     return buf.toString();
/* 383:    */   }
/* 384:    */   
/* 385:    */   protected double getWrappingWidth(boolean firstLine, Graphics2D graphics)
/* 386:    */   {
/* 387:453 */     TextShape<?, ?> ts = this.paragraph.getParentShape();
/* 388:    */     
/* 389:    */ 
/* 390:456 */     Insets2D insets = ts.getInsets();
/* 391:457 */     double leftInset = insets.left;
/* 392:458 */     double rightInset = insets.right;
/* 393:    */     
/* 394:460 */     int indentLevel = this.paragraph.getIndentLevel();
/* 395:461 */     if (indentLevel == -1) {
/* 396:463 */       indentLevel = 0;
/* 397:    */     }
/* 398:465 */     Double leftMargin = this.paragraph.getLeftMargin();
/* 399:466 */     if (leftMargin == null) {
/* 400:468 */       leftMargin = Double.valueOf(Units.toPoints(347663L * (indentLevel + 1)));
/* 401:    */     }
/* 402:470 */     Double indent = this.paragraph.getIndent();
/* 403:471 */     if (indent == null) {
/* 404:472 */       indent = Double.valueOf(Units.toPoints(347663L * indentLevel));
/* 405:    */     }
/* 406:474 */     Double rightMargin = this.paragraph.getRightMargin();
/* 407:475 */     if (rightMargin == null) {
/* 408:476 */       rightMargin = Double.valueOf(0.0D);
/* 409:    */     }
/* 410:479 */     Rectangle2D anchor = DrawShape.getAnchor(graphics, ts);
/* 411:480 */     TextShape.TextDirection textDir = ts.getTextDirection();
/* 412:    */     double width;
/* 413:482 */     if (!ts.getWordWrap())
/* 414:    */     {
/* 415:483 */       Dimension pageDim = ts.getSheet().getSlideShow().getPageSize();
/* 416:    */       double width;
/* 417:485 */       switch (textDir)
/* 418:    */       {
/* 419:    */       default: 
/* 420:487 */         width = pageDim.getWidth() - anchor.getX();
/* 421:488 */         break;
/* 422:    */       case VERTICAL: 
/* 423:490 */         width = pageDim.getHeight() - anchor.getX();
/* 424:491 */         break;
/* 425:    */       case VERTICAL_270: 
/* 426:493 */         width = anchor.getX();
/* 427:    */       }
/* 428:    */     }
/* 429:    */     else
/* 430:    */     {
/* 431:497 */       switch (textDir)
/* 432:    */       {
/* 433:    */       default: 
/* 434:499 */         width = anchor.getWidth() - leftInset - rightInset - leftMargin.doubleValue() - rightMargin.doubleValue();
/* 435:500 */         break;
/* 436:    */       case VERTICAL: 
/* 437:    */       case VERTICAL_270: 
/* 438:503 */         width = anchor.getHeight() - leftInset - rightInset - leftMargin.doubleValue() - rightMargin.doubleValue();
/* 439:    */       }
/* 440:506 */       if ((firstLine) && (!isHSLF())) {
/* 441:507 */         if (this.bullet != null)
/* 442:    */         {
/* 443:508 */           if (indent.doubleValue() > 0.0D) {
/* 444:509 */             width -= indent.doubleValue();
/* 445:    */           }
/* 446:    */         }
/* 447:512 */         else if (indent.doubleValue() > 0.0D) {
/* 448:513 */           width -= indent.doubleValue();
/* 449:514 */         } else if (indent.doubleValue() < 0.0D) {
/* 450:515 */           width += leftMargin.doubleValue();
/* 451:    */         }
/* 452:    */       }
/* 453:    */     }
/* 454:521 */     return width;
/* 455:    */   }
/* 456:    */   
/* 457:    */   private static class AttributedStringData
/* 458:    */   {
/* 459:    */     Attribute attribute;
/* 460:    */     Object value;
/* 461:    */     int beginIndex;
/* 462:    */     int endIndex;
/* 463:    */     
/* 464:    */     AttributedStringData(Attribute attribute, Object value, int beginIndex, int endIndex)
/* 465:    */     {
/* 466:529 */       this.attribute = attribute;
/* 467:530 */       this.value = value;
/* 468:531 */       this.beginIndex = beginIndex;
/* 469:532 */       this.endIndex = endIndex;
/* 470:    */     }
/* 471:    */   }
/* 472:    */   
/* 473:    */   private PlaceableShape<?, ?> getParagraphShape()
/* 474:    */   {
/* 475:541 */     new PlaceableShape()
/* 476:    */     {
/* 477:    */       public ShapeContainer<?, ?> getParent()
/* 478:    */       {
/* 479:543 */         return null;
/* 480:    */       }
/* 481:    */       
/* 482:    */       public Rectangle2D getAnchor()
/* 483:    */       {
/* 484:545 */         return DrawTextParagraph.this.paragraph.getParentShape().getAnchor();
/* 485:    */       }
/* 486:    */       
/* 487:    */       public void setAnchor(Rectangle2D anchor) {}
/* 488:    */       
/* 489:    */       public double getRotation()
/* 490:    */       {
/* 491:549 */         return 0.0D;
/* 492:    */       }
/* 493:    */       
/* 494:    */       public void setRotation(double theta) {}
/* 495:    */       
/* 496:    */       public void setFlipHorizontal(boolean flip) {}
/* 497:    */       
/* 498:    */       public void setFlipVertical(boolean flip) {}
/* 499:    */       
/* 500:    */       public boolean getFlipHorizontal()
/* 501:    */       {
/* 502:557 */         return false;
/* 503:    */       }
/* 504:    */       
/* 505:    */       public boolean getFlipVertical()
/* 506:    */       {
/* 507:559 */         return false;
/* 508:    */       }
/* 509:    */       
/* 510:    */       public Sheet<?, ?> getSheet()
/* 511:    */       {
/* 512:561 */         return DrawTextParagraph.this.paragraph.getParentShape().getSheet();
/* 513:    */       }
/* 514:    */     };
/* 515:    */   }
/* 516:    */   
/* 517:    */   protected AttributedString getAttributedString(Graphics2D graphics, StringBuilder text)
/* 518:    */   {
/* 519:566 */     List<AttributedStringData> attList = new ArrayList();
/* 520:567 */     if (text == null) {
/* 521:568 */       text = new StringBuilder();
/* 522:    */     }
/* 523:571 */     PlaceableShape<?, ?> ps = getParagraphShape();
/* 524:572 */     DrawFontManager dfm = DrawFactory.getInstance(graphics).getFontManager(graphics);
/* 525:573 */     assert (dfm != null);
/* 526:575 */     for (TextRun run : this.paragraph)
/* 527:    */     {
/* 528:576 */       String runText = getRenderableText(graphics, run);
/* 529:578 */       if (!runText.isEmpty())
/* 530:    */       {
/* 531:584 */         runText = dfm.mapFontCharset(graphics, run.getFontInfo(null), runText);
/* 532:585 */         int beginIndex = text.length();
/* 533:586 */         text.append(runText);
/* 534:587 */         int endIndex = text.length();
/* 535:    */         
/* 536:589 */         PaintStyle fgPaintStyle = run.getFontColor();
/* 537:590 */         Paint fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
/* 538:591 */         attList.add(new AttributedStringData(TextAttribute.FOREGROUND, fgPaint, beginIndex, endIndex));
/* 539:    */         
/* 540:593 */         Double fontSz = run.getFontSize();
/* 541:594 */         if (fontSz == null) {
/* 542:595 */           fontSz = this.paragraph.getDefaultFontSize();
/* 543:    */         }
/* 544:597 */         attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz.floatValue()), beginIndex, endIndex));
/* 545:599 */         if (run.isBold()) {
/* 546:600 */           attList.add(new AttributedStringData(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, beginIndex, endIndex));
/* 547:    */         }
/* 548:602 */         if (run.isItalic()) {
/* 549:603 */           attList.add(new AttributedStringData(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, beginIndex, endIndex));
/* 550:    */         }
/* 551:605 */         if (run.isUnderlined())
/* 552:    */         {
/* 553:606 */           attList.add(new AttributedStringData(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, beginIndex, endIndex));
/* 554:607 */           attList.add(new AttributedStringData(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, beginIndex, endIndex));
/* 555:    */         }
/* 556:609 */         if (run.isStrikethrough()) {
/* 557:610 */           attList.add(new AttributedStringData(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, beginIndex, endIndex));
/* 558:    */         }
/* 559:612 */         if (run.isSubscript()) {
/* 560:613 */           attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, beginIndex, endIndex));
/* 561:    */         }
/* 562:615 */         if (run.isSuperscript()) {
/* 563:616 */           attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, beginIndex, endIndex));
/* 564:    */         }
/* 565:619 */         Hyperlink<?, ?> hl = run.getHyperlink();
/* 566:620 */         if (hl != null)
/* 567:    */         {
/* 568:621 */           attList.add(new AttributedStringData(HYPERLINK_HREF, hl.getAddress(), beginIndex, endIndex));
/* 569:622 */           attList.add(new AttributedStringData(HYPERLINK_LABEL, hl.getLabel(), beginIndex, endIndex));
/* 570:    */         }
/* 571:625 */         processGlyphs(graphics, dfm, attList, beginIndex, run, runText);
/* 572:    */       }
/* 573:    */     }
/* 574:630 */     if (text.length() == 0)
/* 575:    */     {
/* 576:631 */       Double fontSz = this.paragraph.getDefaultFontSize();
/* 577:632 */       text.append(" ");
/* 578:633 */       attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz.floatValue()), 0, 1));
/* 579:    */     }
/* 580:636 */     AttributedString string = new AttributedString(text.toString());
/* 581:637 */     for (AttributedStringData asd : attList) {
/* 582:638 */       string.addAttribute(asd.attribute, asd.value, asd.beginIndex, asd.endIndex);
/* 583:    */     }
/* 584:641 */     return string;
/* 585:    */   }
/* 586:    */   
/* 587:    */   private void processGlyphs(Graphics2D graphics, DrawFontManager dfm, List<AttributedStringData> attList, int beginIndex, TextRun run, String runText)
/* 588:    */   {
/* 589:660 */     List<FontGroup.FontGroupRange> ttrList = FontGroup.getFontGroupRanges(runText);
/* 590:661 */     int rangeBegin = 0;
/* 591:662 */     for (FontGroup.FontGroupRange ttr : ttrList)
/* 592:    */     {
/* 593:663 */       FontInfo fiRun = run.getFontInfo(ttr.getFontGroup());
/* 594:664 */       if (fiRun == null) {
/* 595:666 */         fiRun = run.getFontInfo(FontGroup.LATIN);
/* 596:    */       }
/* 597:668 */       FontInfo fiMapped = dfm.getMappedFont(graphics, fiRun);
/* 598:669 */       FontInfo fiFallback = dfm.getFallbackFont(graphics, fiRun);
/* 599:670 */       assert (fiFallback != null);
/* 600:671 */       if (fiMapped == null) {
/* 601:672 */         fiMapped = dfm.getMappedFont(graphics, new DrawFontInfo(this.paragraph.getDefaultFontFamily()));
/* 602:    */       }
/* 603:674 */       if (fiMapped == null) {
/* 604:675 */         fiMapped = fiFallback;
/* 605:    */       }
/* 606:678 */       Font fontMapped = dfm.createAWTFont(graphics, fiMapped, 10.0D, run.isBold(), run.isItalic());
/* 607:679 */       Font fontFallback = dfm.createAWTFont(graphics, fiFallback, 10.0D, run.isBold(), run.isItalic());
/* 608:    */       
/* 609:    */ 
/* 610:682 */       int rangeLen = ttr.getLength();
/* 611:683 */       int partEnd = rangeBegin;
/* 612:684 */       while (partEnd < rangeBegin + rangeLen)
/* 613:    */       {
/* 614:686 */         int partBegin = partEnd;
/* 615:687 */         partEnd = nextPart(fontMapped, runText, partBegin, rangeBegin + rangeLen, true);
/* 616:694 */         if (partBegin < partEnd)
/* 617:    */         {
/* 618:696 */           attList.add(new AttributedStringData(TextAttribute.FAMILY, fontMapped.getFontName(Locale.ROOT), beginIndex + partBegin, beginIndex + partEnd));
/* 619:697 */           if (LOG.check(1)) {
/* 620:698 */             LOG.log(1, new Object[] { "mapped: ", fontMapped.getFontName(Locale.ROOT), " ", Integer.valueOf(beginIndex + partBegin), " ", Integer.valueOf(beginIndex + partEnd), " - ", runText.substring(beginIndex + partBegin, beginIndex + partEnd) });
/* 621:    */           }
/* 622:    */         }
/* 623:703 */         partBegin = partEnd;
/* 624:704 */         partEnd = nextPart(fontMapped, runText, partBegin, rangeBegin + rangeLen, false);
/* 625:706 */         if (partBegin < partEnd)
/* 626:    */         {
/* 627:708 */           attList.add(new AttributedStringData(TextAttribute.FAMILY, fontFallback.getFontName(Locale.ROOT), beginIndex + partBegin, beginIndex + partEnd));
/* 628:709 */           if (LOG.check(1)) {
/* 629:710 */             LOG.log(1, new Object[] { "fallback: ", fontFallback.getFontName(Locale.ROOT), " ", Integer.valueOf(beginIndex + partBegin), " ", Integer.valueOf(beginIndex + partEnd), " - ", runText.substring(beginIndex + partBegin, beginIndex + partEnd) });
/* 630:    */           }
/* 631:    */         }
/* 632:    */       }
/* 633:715 */       rangeBegin += rangeLen;
/* 634:    */     }
/* 635:    */   }
/* 636:    */   
/* 637:    */   private static int nextPart(Font fontMapped, String runText, int beginPart, int endPart, boolean isDisplayed)
/* 638:    */   {
/* 639:720 */     int rIdx = beginPart;
/* 640:721 */     while (rIdx < endPart)
/* 641:    */     {
/* 642:722 */       int codepoint = runText.codePointAt(rIdx);
/* 643:723 */       if (fontMapped.canDisplay(codepoint) != isDisplayed) {
/* 644:    */         break;
/* 645:    */       }
/* 646:726 */       rIdx += Character.charCount(codepoint);
/* 647:    */     }
/* 648:728 */     return rIdx;
/* 649:    */   }
/* 650:    */   
/* 651:    */   protected boolean isHSLF()
/* 652:    */   {
/* 653:735 */     return DrawShape.isHSLF(this.paragraph.getParentShape());
/* 654:    */   }
/* 655:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.DrawTextParagraph

 * JD-Core Version:    0.7.0.1

 */