/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import org.apache.poi.common.usermodel.fonts.FontCharset;
/*   5:    */ import org.apache.poi.common.usermodel.fonts.FontFamily;
/*   6:    */ import org.apache.poi.common.usermodel.fonts.FontGroup;
/*   7:    */ import org.apache.poi.common.usermodel.fonts.FontInfo;
/*   8:    */ import org.apache.poi.common.usermodel.fonts.FontPitch;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JRuntimeException;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.sl.draw.DrawPaint;
/*  12:    */ import org.apache.poi.sl.usermodel.PaintStyle;
/*  13:    */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*  14:    */ import org.apache.poi.sl.usermodel.TextRun;
/*  15:    */ import org.apache.poi.sl.usermodel.TextRun.FieldType;
/*  16:    */ import org.apache.poi.sl.usermodel.TextRun.TextCap;
/*  17:    */ import org.apache.poi.xslf.model.CharacterPropertyFetcher;
/*  18:    */ import org.apache.xmlbeans.XmlObject;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType.Enum;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
/*  39:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  40:    */ 
/*  41:    */ public class XSLFTextRun
/*  42:    */   implements TextRun
/*  43:    */ {
/*  44:    */   private final XmlObject _r;
/*  45:    */   private final XSLFTextParagraph _p;
/*  46:    */   
/*  47:    */   protected XSLFTextRun(XmlObject r, XSLFTextParagraph p)
/*  48:    */   {
/*  49: 63 */     this._r = r;
/*  50: 64 */     this._p = p;
/*  51: 65 */     if ((!(r instanceof CTRegularTextRun)) && (!(r instanceof CTTextLineBreak)) && (!(r instanceof CTTextField))) {
/*  52: 66 */       throw new OpenXML4JRuntimeException("unsupported text run of type " + r.getClass());
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   XSLFTextParagraph getParentParagraph()
/*  57:    */   {
/*  58: 71 */     return this._p;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getRawText()
/*  62:    */   {
/*  63: 76 */     if ((this._r instanceof CTTextField)) {
/*  64: 77 */       return ((CTTextField)this._r).getT();
/*  65:    */     }
/*  66: 78 */     if ((this._r instanceof CTTextLineBreak)) {
/*  67: 79 */       return "\n";
/*  68:    */     }
/*  69: 81 */     return ((CTRegularTextRun)this._r).getT();
/*  70:    */   }
/*  71:    */   
/*  72:    */   String getRenderableText()
/*  73:    */   {
/*  74: 85 */     if ((this._r instanceof CTTextField))
/*  75:    */     {
/*  76: 86 */       CTTextField tf = (CTTextField)this._r;
/*  77: 87 */       XSLFSheet sheet = this._p.getParentShape().getSheet();
/*  78: 88 */       if (("slidenum".equals(tf.getType())) && ((sheet instanceof XSLFSlide))) {
/*  79: 89 */         return Integer.toString(((XSLFSlide)sheet).getSlideNumber());
/*  80:    */       }
/*  81: 91 */       return tf.getT();
/*  82:    */     }
/*  83: 92 */     if ((this._r instanceof CTTextLineBreak)) {
/*  84: 93 */       return "\n";
/*  85:    */     }
/*  86: 97 */     String txt = ((CTRegularTextRun)this._r).getT();
/*  87: 98 */     TextRun.TextCap cap = getTextCap();
/*  88: 99 */     StringBuffer buf = new StringBuffer();
/*  89:100 */     for (int i = 0; i < txt.length(); i++)
/*  90:    */     {
/*  91:101 */       char c = txt.charAt(i);
/*  92:102 */       if (c == '\t') {
/*  93:104 */         buf.append("  ");
/*  94:    */       } else {
/*  95:106 */         switch (cap)
/*  96:    */         {
/*  97:    */         case ALL: 
/*  98:108 */           buf.append(Character.toUpperCase(c));
/*  99:109 */           break;
/* 100:    */         case SMALL: 
/* 101:111 */           buf.append(Character.toLowerCase(c));
/* 102:112 */           break;
/* 103:    */         default: 
/* 104:114 */           buf.append(c);
/* 105:    */         }
/* 106:    */       }
/* 107:    */     }
/* 108:119 */     return buf.toString();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setText(String text)
/* 112:    */   {
/* 113:124 */     if ((this._r instanceof CTTextField))
/* 114:    */     {
/* 115:125 */       ((CTTextField)this._r).setT(text);
/* 116:    */     }
/* 117:    */     else
/* 118:    */     {
/* 119:126 */       if ((this._r instanceof CTTextLineBreak)) {
/* 120:128 */         return;
/* 121:    */       }
/* 122:130 */       ((CTRegularTextRun)this._r).setT(text);
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   public XmlObject getXmlObject()
/* 127:    */   {
/* 128:142 */     return this._r;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setFontColor(Color color)
/* 132:    */   {
/* 133:147 */     setFontColor(DrawPaint.createSolidPaint(color));
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setFontColor(PaintStyle color)
/* 137:    */   {
/* 138:152 */     if (!(color instanceof PaintStyle.SolidPaint)) {
/* 139:153 */       throw new IllegalArgumentException("Currently only SolidPaint is supported!");
/* 140:    */     }
/* 141:155 */     PaintStyle.SolidPaint sp = (PaintStyle.SolidPaint)color;
/* 142:156 */     Color c = DrawPaint.applyColorTransform(sp.getSolidColor());
/* 143:    */     
/* 144:158 */     CTTextCharacterProperties rPr = getRPr(true);
/* 145:159 */     CTSolidColorFillProperties fill = rPr.isSetSolidFill() ? rPr.getSolidFill() : rPr.addNewSolidFill();
/* 146:    */     
/* 147:161 */     XSLFColor col = new XSLFColor(fill, getParentParagraph().getParentShape().getSheet().getTheme(), fill.getSchemeClr());
/* 148:162 */     col.setColor(c);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public PaintStyle getFontColor()
/* 152:    */   {
/* 153:167 */     final boolean hasPlaceholder = getParentParagraph().getParentShape().getPlaceholder() != null;
/* 154:168 */     CharacterPropertyFetcher<PaintStyle> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 155:    */     {
/* 156:    */       public boolean fetch(CTTextCharacterProperties props)
/* 157:    */       {
/* 158:171 */         if (props == null) {
/* 159:172 */           return false;
/* 160:    */         }
/* 161:175 */         XSLFShape shape = XSLFTextRun.this._p.getParentShape();
/* 162:176 */         CTShapeStyle style = shape.getSpStyle();
/* 163:177 */         CTSchemeColor phClr = null;
/* 164:178 */         if ((style != null) && (style.getFontRef() != null)) {
/* 165:179 */           phClr = style.getFontRef().getSchemeClr();
/* 166:    */         }
/* 167:182 */         XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(props);
/* 168:183 */         XSLFSheet sheet = shape.getSheet();
/* 169:184 */         PackagePart pp = sheet.getPackagePart();
/* 170:185 */         XSLFTheme theme = sheet.getTheme();
/* 171:186 */         PaintStyle ps = XSLFShape.selectPaint(fp, phClr, pp, theme, hasPlaceholder);
/* 172:188 */         if (ps != null)
/* 173:    */         {
/* 174:189 */           setValue(ps);
/* 175:190 */           return true;
/* 176:    */         }
/* 177:193 */         return false;
/* 178:    */       }
/* 179:195 */     };
/* 180:196 */     fetchCharacterProperty(fetcher);
/* 181:197 */     return (PaintStyle)fetcher.getValue();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setFontSize(Double fontSize)
/* 185:    */   {
/* 186:202 */     CTTextCharacterProperties rPr = getRPr(true);
/* 187:203 */     if (fontSize == null)
/* 188:    */     {
/* 189:204 */       if (rPr.isSetSz()) {
/* 190:205 */         rPr.unsetSz();
/* 191:    */       }
/* 192:    */     }
/* 193:    */     else
/* 194:    */     {
/* 195:208 */       if (fontSize.doubleValue() < 1.0D) {
/* 196:209 */         throw new IllegalArgumentException("Minimum font size is 1pt but was " + fontSize);
/* 197:    */       }
/* 198:212 */       rPr.setSz((int)(100.0D * fontSize.doubleValue()));
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   public Double getFontSize()
/* 203:    */   {
/* 204:218 */     double scale = 1.0D;
/* 205:219 */     CTTextNormalAutofit afit = getParentParagraph().getParentShape().getTextBodyPr().getNormAutofit();
/* 206:220 */     if (afit != null) {
/* 207:221 */       scale = afit.getFontScale() / 100000.0D;
/* 208:    */     }
/* 209:224 */     CharacterPropertyFetcher<Double> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 210:    */     {
/* 211:    */       public boolean fetch(CTTextCharacterProperties props)
/* 212:    */       {
/* 213:227 */         if ((props != null) && (props.isSetSz()))
/* 214:    */         {
/* 215:228 */           setValue(Double.valueOf(props.getSz() * 0.01D));
/* 216:229 */           return true;
/* 217:    */         }
/* 218:231 */         return false;
/* 219:    */       }
/* 220:233 */     };
/* 221:234 */     fetchCharacterProperty(fetcher);
/* 222:235 */     return fetcher.getValue() == null ? null : Double.valueOf(((Double)fetcher.getValue()).doubleValue() * scale);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public double getCharacterSpacing()
/* 226:    */   {
/* 227:245 */     CharacterPropertyFetcher<Double> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 228:    */     {
/* 229:    */       public boolean fetch(CTTextCharacterProperties props)
/* 230:    */       {
/* 231:248 */         if ((props != null) && (props.isSetSpc()))
/* 232:    */         {
/* 233:249 */           setValue(Double.valueOf(props.getSpc() * 0.01D));
/* 234:250 */           return true;
/* 235:    */         }
/* 236:252 */         return false;
/* 237:    */       }
/* 238:254 */     };
/* 239:255 */     fetchCharacterProperty(fetcher);
/* 240:256 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setCharacterSpacing(double spc)
/* 244:    */   {
/* 245:269 */     CTTextCharacterProperties rPr = getRPr(true);
/* 246:270 */     if (spc == 0.0D)
/* 247:    */     {
/* 248:271 */       if (rPr.isSetSpc()) {
/* 249:272 */         rPr.unsetSpc();
/* 250:    */       }
/* 251:    */     }
/* 252:    */     else {
/* 253:275 */       rPr.setSpc((int)(100.0D * spc));
/* 254:    */     }
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setFontFamily(String typeface)
/* 258:    */   {
/* 259:281 */     FontGroup fg = FontGroup.getFontGroupFirst(getRawText());
/* 260:282 */     new XSLFFontInfo(fg, null).setTypeface(typeface);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void setFontFamily(String typeface, FontGroup fontGroup)
/* 264:    */   {
/* 265:287 */     new XSLFFontInfo(fontGroup, null).setTypeface(typeface);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void setFontInfo(FontInfo fontInfo, FontGroup fontGroup)
/* 269:    */   {
/* 270:292 */     new XSLFFontInfo(fontGroup, null).copyFrom(fontInfo);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public String getFontFamily()
/* 274:    */   {
/* 275:297 */     FontGroup fg = FontGroup.getFontGroupFirst(getRawText());
/* 276:298 */     return new XSLFFontInfo(fg, null).getTypeface();
/* 277:    */   }
/* 278:    */   
/* 279:    */   public String getFontFamily(FontGroup fontGroup)
/* 280:    */   {
/* 281:303 */     return new XSLFFontInfo(fontGroup, null).getTypeface();
/* 282:    */   }
/* 283:    */   
/* 284:    */   public FontInfo getFontInfo(FontGroup fontGroup)
/* 285:    */   {
/* 286:308 */     XSLFFontInfo fontInfo = new XSLFFontInfo(fontGroup, null);
/* 287:309 */     return fontInfo.getTypeface() != null ? fontInfo : null;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public byte getPitchAndFamily()
/* 291:    */   {
/* 292:314 */     FontGroup fg = FontGroup.getFontGroupFirst(getRawText());
/* 293:315 */     XSLFFontInfo fontInfo = new XSLFFontInfo(fg, null);
/* 294:316 */     FontPitch pitch = fontInfo.getPitch();
/* 295:317 */     if (pitch == null) {
/* 296:318 */       pitch = FontPitch.VARIABLE;
/* 297:    */     }
/* 298:320 */     FontFamily family = fontInfo.getFamily();
/* 299:321 */     if (family == null) {
/* 300:322 */       family = FontFamily.FF_SWISS;
/* 301:    */     }
/* 302:324 */     return FontPitch.getNativeId(pitch, family);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void setStrikethrough(boolean strike)
/* 306:    */   {
/* 307:329 */     getRPr(true).setStrike(strike ? STTextStrikeType.SNG_STRIKE : STTextStrikeType.NO_STRIKE);
/* 308:    */   }
/* 309:    */   
/* 310:    */   public boolean isStrikethrough()
/* 311:    */   {
/* 312:334 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 313:    */     {
/* 314:    */       public boolean fetch(CTTextCharacterProperties props)
/* 315:    */       {
/* 316:337 */         if ((props != null) && (props.isSetStrike()))
/* 317:    */         {
/* 318:338 */           setValue(Boolean.valueOf(props.getStrike() != STTextStrikeType.NO_STRIKE));
/* 319:339 */           return true;
/* 320:    */         }
/* 321:341 */         return false;
/* 322:    */       }
/* 323:343 */     };
/* 324:344 */     fetchCharacterProperty(fetcher);
/* 325:345 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 326:    */   }
/* 327:    */   
/* 328:    */   public boolean isSuperscript()
/* 329:    */   {
/* 330:350 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 331:    */     {
/* 332:    */       public boolean fetch(CTTextCharacterProperties props)
/* 333:    */       {
/* 334:353 */         if ((props != null) && (props.isSetBaseline()))
/* 335:    */         {
/* 336:354 */           setValue(Boolean.valueOf(props.getBaseline() > 0));
/* 337:355 */           return true;
/* 338:    */         }
/* 339:357 */         return false;
/* 340:    */       }
/* 341:359 */     };
/* 342:360 */     fetchCharacterProperty(fetcher);
/* 343:361 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 344:    */   }
/* 345:    */   
/* 346:    */   public void setBaselineOffset(double baselineOffset)
/* 347:    */   {
/* 348:374 */     getRPr(true).setBaseline((int)baselineOffset * 1000);
/* 349:    */   }
/* 350:    */   
/* 351:    */   public void setSuperscript(boolean flag)
/* 352:    */   {
/* 353:384 */     setBaselineOffset(flag ? 30.0D : 0.0D);
/* 354:    */   }
/* 355:    */   
/* 356:    */   public void setSubscript(boolean flag)
/* 357:    */   {
/* 358:394 */     setBaselineOffset(flag ? -25.0D : 0.0D);
/* 359:    */   }
/* 360:    */   
/* 361:    */   public boolean isSubscript()
/* 362:    */   {
/* 363:399 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 364:    */     {
/* 365:    */       public boolean fetch(CTTextCharacterProperties props)
/* 366:    */       {
/* 367:402 */         if ((props != null) && (props.isSetBaseline()))
/* 368:    */         {
/* 369:403 */           setValue(Boolean.valueOf(props.getBaseline() < 0));
/* 370:404 */           return true;
/* 371:    */         }
/* 372:406 */         return false;
/* 373:    */       }
/* 374:408 */     };
/* 375:409 */     fetchCharacterProperty(fetcher);
/* 376:410 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 377:    */   }
/* 378:    */   
/* 379:    */   public TextRun.TextCap getTextCap()
/* 380:    */   {
/* 381:418 */     CharacterPropertyFetcher<TextRun.TextCap> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 382:    */     {
/* 383:    */       public boolean fetch(CTTextCharacterProperties props)
/* 384:    */       {
/* 385:421 */         if ((props != null) && (props.isSetCap()))
/* 386:    */         {
/* 387:422 */           int idx = props.getCap().intValue() - 1;
/* 388:423 */           setValue(TextRun.TextCap.values()[idx]);
/* 389:424 */           return true;
/* 390:    */         }
/* 391:426 */         return false;
/* 392:    */       }
/* 393:428 */     };
/* 394:429 */     fetchCharacterProperty(fetcher);
/* 395:430 */     return fetcher.getValue() == null ? TextRun.TextCap.NONE : (TextRun.TextCap)fetcher.getValue();
/* 396:    */   }
/* 397:    */   
/* 398:    */   public void setBold(boolean bold)
/* 399:    */   {
/* 400:435 */     getRPr(true).setB(bold);
/* 401:    */   }
/* 402:    */   
/* 403:    */   public boolean isBold()
/* 404:    */   {
/* 405:440 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 406:    */     {
/* 407:    */       public boolean fetch(CTTextCharacterProperties props)
/* 408:    */       {
/* 409:443 */         if ((props != null) && (props.isSetB()))
/* 410:    */         {
/* 411:444 */           setValue(Boolean.valueOf(props.getB()));
/* 412:445 */           return true;
/* 413:    */         }
/* 414:447 */         return false;
/* 415:    */       }
/* 416:449 */     };
/* 417:450 */     fetchCharacterProperty(fetcher);
/* 418:451 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void setItalic(boolean italic)
/* 422:    */   {
/* 423:456 */     getRPr(true).setI(italic);
/* 424:    */   }
/* 425:    */   
/* 426:    */   public boolean isItalic()
/* 427:    */   {
/* 428:461 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 429:    */     {
/* 430:    */       public boolean fetch(CTTextCharacterProperties props)
/* 431:    */       {
/* 432:464 */         if ((props != null) && (props.isSetI()))
/* 433:    */         {
/* 434:465 */           setValue(Boolean.valueOf(props.getI()));
/* 435:466 */           return true;
/* 436:    */         }
/* 437:468 */         return false;
/* 438:    */       }
/* 439:470 */     };
/* 440:471 */     fetchCharacterProperty(fetcher);
/* 441:472 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 442:    */   }
/* 443:    */   
/* 444:    */   public void setUnderlined(boolean underline)
/* 445:    */   {
/* 446:477 */     getRPr(true).setU(underline ? STTextUnderlineType.SNG : STTextUnderlineType.NONE);
/* 447:    */   }
/* 448:    */   
/* 449:    */   public boolean isUnderlined()
/* 450:    */   {
/* 451:482 */     CharacterPropertyFetcher<Boolean> fetcher = new CharacterPropertyFetcher(this._p.getIndentLevel())
/* 452:    */     {
/* 453:    */       public boolean fetch(CTTextCharacterProperties props)
/* 454:    */       {
/* 455:485 */         if ((props != null) && (props.isSetU()))
/* 456:    */         {
/* 457:486 */           setValue(Boolean.valueOf(props.getU() != STTextUnderlineType.NONE));
/* 458:487 */           return true;
/* 459:    */         }
/* 460:489 */         return false;
/* 461:    */       }
/* 462:491 */     };
/* 463:492 */     fetchCharacterProperty(fetcher);
/* 464:493 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 465:    */   }
/* 466:    */   
/* 467:    */   protected CTTextCharacterProperties getRPr(boolean create)
/* 468:    */   {
/* 469:503 */     if ((this._r instanceof CTTextField))
/* 470:    */     {
/* 471:504 */       CTTextField tf = (CTTextField)this._r;
/* 472:505 */       if (tf.isSetRPr()) {
/* 473:506 */         return tf.getRPr();
/* 474:    */       }
/* 475:507 */       if (create) {
/* 476:508 */         return tf.addNewRPr();
/* 477:    */       }
/* 478:    */     }
/* 479:510 */     else if ((this._r instanceof CTTextLineBreak))
/* 480:    */     {
/* 481:511 */       CTTextLineBreak tlb = (CTTextLineBreak)this._r;
/* 482:512 */       if (tlb.isSetRPr()) {
/* 483:513 */         return tlb.getRPr();
/* 484:    */       }
/* 485:514 */       if (create) {
/* 486:515 */         return tlb.addNewRPr();
/* 487:    */       }
/* 488:    */     }
/* 489:    */     else
/* 490:    */     {
/* 491:518 */       CTRegularTextRun tr = (CTRegularTextRun)this._r;
/* 492:519 */       if (tr.isSetRPr()) {
/* 493:520 */         return tr.getRPr();
/* 494:    */       }
/* 495:521 */       if (create) {
/* 496:522 */         return tr.addNewRPr();
/* 497:    */       }
/* 498:    */     }
/* 499:525 */     return null;
/* 500:    */   }
/* 501:    */   
/* 502:    */   public String toString()
/* 503:    */   {
/* 504:530 */     return "[" + getClass() + "]" + getRawText();
/* 505:    */   }
/* 506:    */   
/* 507:    */   public XSLFHyperlink createHyperlink()
/* 508:    */   {
/* 509:535 */     XSLFHyperlink hl = getHyperlink();
/* 510:536 */     if (hl != null) {
/* 511:537 */       return hl;
/* 512:    */     }
/* 513:540 */     CTTextCharacterProperties rPr = getRPr(true);
/* 514:541 */     return new XSLFHyperlink(rPr.addNewHlinkClick(), this._p.getParentShape().getSheet());
/* 515:    */   }
/* 516:    */   
/* 517:    */   public XSLFHyperlink getHyperlink()
/* 518:    */   {
/* 519:546 */     CTTextCharacterProperties rPr = getRPr(false);
/* 520:547 */     if (rPr == null) {
/* 521:548 */       return null;
/* 522:    */     }
/* 523:550 */     CTHyperlink hl = rPr.getHlinkClick();
/* 524:551 */     if (hl == null) {
/* 525:552 */       return null;
/* 526:    */     }
/* 527:554 */     return new XSLFHyperlink(hl, this._p.getParentShape().getSheet());
/* 528:    */   }
/* 529:    */   
/* 530:    */   private boolean fetchCharacterProperty(CharacterPropertyFetcher<?> fetcher)
/* 531:    */   {
/* 532:558 */     XSLFTextShape shape = this._p.getParentShape();
/* 533:559 */     XSLFSheet sheet = shape.getSheet();
/* 534:    */     
/* 535:561 */     CTTextCharacterProperties rPr = getRPr(false);
/* 536:562 */     if ((rPr != null) && (fetcher.fetch(rPr))) {
/* 537:563 */       return true;
/* 538:    */     }
/* 539:566 */     if (shape.fetchShapeProperty(fetcher)) {
/* 540:567 */       return true;
/* 541:    */     }
/* 542:570 */     CTPlaceholder ph = shape.getCTPlaceholder();
/* 543:571 */     if (ph == null)
/* 544:    */     {
/* 545:574 */       XMLSlideShow ppt = sheet.getSlideShow();
/* 546:    */       
/* 547:576 */       CTTextParagraphProperties themeProps = ppt.getDefaultParagraphStyle(this._p.getIndentLevel());
/* 548:577 */       if ((themeProps != null) && (fetcher.fetch(themeProps))) {
/* 549:578 */         return true;
/* 550:    */       }
/* 551:    */     }
/* 552:583 */     CTTextParagraphProperties defaultProps = this._p.getDefaultMasterStyle();
/* 553:584 */     if ((defaultProps != null) && (fetcher.fetch(defaultProps))) {
/* 554:585 */       return true;
/* 555:    */     }
/* 556:588 */     return false;
/* 557:    */   }
/* 558:    */   
/* 559:    */   void copy(XSLFTextRun r)
/* 560:    */   {
/* 561:592 */     String srcFontFamily = r.getFontFamily();
/* 562:593 */     if ((srcFontFamily != null) && (!srcFontFamily.equals(getFontFamily()))) {
/* 563:594 */       setFontFamily(srcFontFamily);
/* 564:    */     }
/* 565:597 */     PaintStyle srcFontColor = r.getFontColor();
/* 566:598 */     if ((srcFontColor != null) && (!srcFontColor.equals(getFontColor()))) {
/* 567:599 */       setFontColor(srcFontColor);
/* 568:    */     }
/* 569:602 */     double srcFontSize = r.getFontSize().doubleValue();
/* 570:603 */     if (srcFontSize != getFontSize().doubleValue()) {
/* 571:604 */       setFontSize(Double.valueOf(srcFontSize));
/* 572:    */     }
/* 573:607 */     boolean bold = r.isBold();
/* 574:608 */     if (bold != isBold()) {
/* 575:609 */       setBold(bold);
/* 576:    */     }
/* 577:612 */     boolean italic = r.isItalic();
/* 578:613 */     if (italic != isItalic()) {
/* 579:614 */       setItalic(italic);
/* 580:    */     }
/* 581:617 */     boolean underline = r.isUnderlined();
/* 582:618 */     if (underline != isUnderlined()) {
/* 583:619 */       setUnderlined(underline);
/* 584:    */     }
/* 585:622 */     boolean strike = r.isStrikethrough();
/* 586:623 */     if (strike != isStrikethrough()) {
/* 587:624 */       setStrikethrough(strike);
/* 588:    */     }
/* 589:    */   }
/* 590:    */   
/* 591:    */   public TextRun.FieldType getFieldType()
/* 592:    */   {
/* 593:631 */     if ((this._r instanceof CTTextField))
/* 594:    */     {
/* 595:632 */       CTTextField tf = (CTTextField)this._r;
/* 596:633 */       if ("slidenum".equals(tf.getType())) {
/* 597:634 */         return TextRun.FieldType.SLIDE_NUMBER;
/* 598:    */       }
/* 599:    */     }
/* 600:637 */     return null;
/* 601:    */   }
/* 602:    */   
/* 603:    */   private class XSLFFontInfo
/* 604:    */     implements FontInfo
/* 605:    */   {
/* 606:    */     private final FontGroup fontGroup;
/* 607:    */     
/* 608:    */     private XSLFFontInfo(FontGroup fontGroup)
/* 609:    */     {
/* 610:645 */       this.fontGroup = (fontGroup != null ? fontGroup : FontGroup.getFontGroupFirst(XSLFTextRun.this.getRawText()));
/* 611:    */     }
/* 612:    */     
/* 613:    */     public void copyFrom(FontInfo fontInfo)
/* 614:    */     {
/* 615:649 */       CTTextFont tf = getXmlObject(true);
/* 616:650 */       setTypeface(fontInfo.getTypeface());
/* 617:651 */       setCharset(fontInfo.getCharset());
/* 618:652 */       FontPitch pitch = fontInfo.getPitch();
/* 619:653 */       FontFamily family = fontInfo.getFamily();
/* 620:654 */       if ((pitch == null) && (family == null))
/* 621:    */       {
/* 622:655 */         if (tf.isSetPitchFamily()) {
/* 623:656 */           tf.unsetPitchFamily();
/* 624:    */         }
/* 625:    */       }
/* 626:    */       else
/* 627:    */       {
/* 628:659 */         setPitch(pitch);
/* 629:660 */         setFamily(family);
/* 630:    */       }
/* 631:    */     }
/* 632:    */     
/* 633:    */     public Integer getIndex()
/* 634:    */     {
/* 635:666 */       return null;
/* 636:    */     }
/* 637:    */     
/* 638:    */     public void setIndex(int index)
/* 639:    */     {
/* 640:671 */       throw new UnsupportedOperationException("setIndex not supported by XSLFFontInfo.");
/* 641:    */     }
/* 642:    */     
/* 643:    */     public String getTypeface()
/* 644:    */     {
/* 645:676 */       CTTextFont tf = getXmlObject(false);
/* 646:677 */       return (tf != null) && (tf.isSetTypeface()) ? tf.getTypeface() : null;
/* 647:    */     }
/* 648:    */     
/* 649:    */     public void setTypeface(String typeface)
/* 650:    */     {
/* 651:682 */       if (typeface != null)
/* 652:    */       {
/* 653:683 */         getXmlObject(true).setTypeface(typeface);
/* 654:684 */         return;
/* 655:    */       }
/* 656:687 */       CTTextCharacterProperties props = XSLFTextRun.this.getRPr(false);
/* 657:688 */       if (props == null) {
/* 658:689 */         return;
/* 659:    */       }
/* 660:691 */       FontGroup fg = FontGroup.getFontGroupFirst(XSLFTextRun.this.getRawText());
/* 661:692 */       switch (XSLFTextRun.11.$SwitchMap$org$apache$poi$common$usermodel$fonts$FontGroup[fg.ordinal()])
/* 662:    */       {
/* 663:    */       case 1: 
/* 664:    */       default: 
/* 665:695 */         if (props.isSetLatin()) {
/* 666:696 */           props.unsetLatin();
/* 667:    */         }
/* 668:    */         break;
/* 669:    */       case 2: 
/* 670:700 */         if (props.isSetEa()) {
/* 671:701 */           props.unsetEa();
/* 672:    */         }
/* 673:    */         break;
/* 674:    */       case 3: 
/* 675:705 */         if (props.isSetCs()) {
/* 676:706 */           props.unsetCs();
/* 677:    */         }
/* 678:    */         break;
/* 679:    */       case 4: 
/* 680:710 */         if (props.isSetSym()) {
/* 681:711 */           props.unsetSym();
/* 682:    */         }
/* 683:    */         break;
/* 684:    */       }
/* 685:    */     }
/* 686:    */     
/* 687:    */     public FontCharset getCharset()
/* 688:    */     {
/* 689:719 */       CTTextFont tf = getXmlObject(false);
/* 690:720 */       return (tf != null) && (tf.isSetCharset()) ? FontCharset.valueOf(tf.getCharset() & 0xFF) : null;
/* 691:    */     }
/* 692:    */     
/* 693:    */     public void setCharset(FontCharset charset)
/* 694:    */     {
/* 695:725 */       CTTextFont tf = getXmlObject(true);
/* 696:726 */       if (charset != null) {
/* 697:727 */         tf.setCharset((byte)charset.getNativeId());
/* 698:729 */       } else if (tf.isSetCharset()) {
/* 699:730 */         tf.unsetCharset();
/* 700:    */       }
/* 701:    */     }
/* 702:    */     
/* 703:    */     public FontFamily getFamily()
/* 704:    */     {
/* 705:737 */       CTTextFont tf = getXmlObject(false);
/* 706:738 */       return (tf != null) && (tf.isSetPitchFamily()) ? FontFamily.valueOfPitchFamily(tf.getPitchFamily()) : null;
/* 707:    */     }
/* 708:    */     
/* 709:    */     public void setFamily(FontFamily family)
/* 710:    */     {
/* 711:743 */       CTTextFont tf = getXmlObject(true);
/* 712:744 */       if ((family == null) && (!tf.isSetPitchFamily())) {
/* 713:745 */         return;
/* 714:    */       }
/* 715:747 */       FontPitch pitch = tf.isSetPitchFamily() ? FontPitch.valueOfPitchFamily(tf.getPitchFamily()) : FontPitch.VARIABLE;
/* 716:    */       
/* 717:    */ 
/* 718:750 */       byte pitchFamily = FontPitch.getNativeId(pitch, family != null ? family : FontFamily.FF_SWISS);
/* 719:751 */       tf.setPitchFamily(pitchFamily);
/* 720:    */     }
/* 721:    */     
/* 722:    */     public FontPitch getPitch()
/* 723:    */     {
/* 724:756 */       CTTextFont tf = getXmlObject(false);
/* 725:757 */       return (tf != null) && (tf.isSetPitchFamily()) ? FontPitch.valueOfPitchFamily(tf.getPitchFamily()) : null;
/* 726:    */     }
/* 727:    */     
/* 728:    */     public void setPitch(FontPitch pitch)
/* 729:    */     {
/* 730:762 */       CTTextFont tf = getXmlObject(true);
/* 731:763 */       if ((pitch == null) && (!tf.isSetPitchFamily())) {
/* 732:764 */         return;
/* 733:    */       }
/* 734:766 */       FontFamily family = tf.isSetPitchFamily() ? FontFamily.valueOfPitchFamily(tf.getPitchFamily()) : FontFamily.FF_SWISS;
/* 735:    */       
/* 736:    */ 
/* 737:769 */       byte pitchFamily = FontPitch.getNativeId(pitch != null ? pitch : FontPitch.VARIABLE, family);
/* 738:770 */       tf.setPitchFamily(pitchFamily);
/* 739:    */     }
/* 740:    */     
/* 741:    */     private CTTextFont getXmlObject(boolean create)
/* 742:    */     {
/* 743:774 */       if (create) {
/* 744:775 */         return getCTTextFont(XSLFTextRun.this.getRPr(true), true);
/* 745:    */       }
/* 746:778 */       CharacterPropertyFetcher<CTTextFont> visitor = new CharacterPropertyFetcher(XSLFTextRun.this._p.getIndentLevel())
/* 747:    */       {
/* 748:    */         public boolean fetch(CTTextCharacterProperties props)
/* 749:    */         {
/* 750:781 */           CTTextFont font = XSLFFontInfo.this.getCTTextFont(props, false);
/* 751:782 */           if (font == null) {
/* 752:783 */             return false;
/* 753:    */           }
/* 754:785 */           setValue(font);
/* 755:786 */           return true;
/* 756:    */         }
/* 757:788 */       };
/* 758:789 */       XSLFTextRun.this.fetchCharacterProperty(visitor);
/* 759:    */       
/* 760:791 */       return (CTTextFont)visitor.getValue();
/* 761:    */     }
/* 762:    */     
/* 763:    */     private CTTextFont getCTTextFont(CTTextCharacterProperties props, boolean create)
/* 764:    */     {
/* 765:795 */       if (props == null) {
/* 766:796 */         return null;
/* 767:    */       }
/* 768:    */       CTTextFont font;
/* 769:800 */       switch (XSLFTextRun.11.$SwitchMap$org$apache$poi$common$usermodel$fonts$FontGroup[this.fontGroup.ordinal()])
/* 770:    */       {
/* 771:    */       case 1: 
/* 772:    */       default: 
/* 773:803 */         font = props.getLatin();
/* 774:804 */         if ((font == null) && (create)) {
/* 775:805 */           font = props.addNewLatin();
/* 776:    */         }
/* 777:    */         break;
/* 778:    */       case 2: 
/* 779:809 */         font = props.getEa();
/* 780:810 */         if ((font == null) && (create)) {
/* 781:811 */           font = props.addNewEa();
/* 782:    */         }
/* 783:    */         break;
/* 784:    */       case 3: 
/* 785:815 */         font = props.getCs();
/* 786:816 */         if ((font == null) && (create)) {
/* 787:817 */           font = props.addNewCs();
/* 788:    */         }
/* 789:    */         break;
/* 790:    */       case 4: 
/* 791:821 */         font = props.getSym();
/* 792:822 */         if ((font == null) && (create)) {
/* 793:823 */           font = props.addNewSym();
/* 794:    */         }
/* 795:    */         break;
/* 796:    */       }
/* 797:828 */       if (font == null) {
/* 798:829 */         return null;
/* 799:    */       }
/* 800:832 */       String typeface = font.isSetTypeface() ? font.getTypeface() : "";
/* 801:833 */       if ((typeface.startsWith("+mj-")) || (typeface.startsWith("+mn-")))
/* 802:    */       {
/* 803:835 */         XSLFTheme theme = XSLFTextRun.this._p.getParentShape().getSheet().getTheme();
/* 804:836 */         CTFontScheme fontTheme = theme.getXmlObject().getThemeElements().getFontScheme();
/* 805:837 */         CTFontCollection coll = typeface.startsWith("+mj-") ? fontTheme.getMajorFont() : fontTheme.getMinorFont();
/* 806:    */         
/* 807:    */ 
/* 808:    */ 
/* 809:841 */         String fgStr = typeface.substring(4);
/* 810:842 */         if ("ea".equals(fgStr)) {
/* 811:843 */           font = coll.getEa();
/* 812:844 */         } else if ("cs".equals(fgStr)) {
/* 813:845 */           font = coll.getCs();
/* 814:    */         } else {
/* 815:847 */           font = coll.getLatin();
/* 816:    */         }
/* 817:851 */         if ((font == null) || (!font.isSetTypeface()) || ("".equals(font.getTypeface()))) {
/* 818:852 */           font = coll.getLatin();
/* 819:    */         }
/* 820:    */       }
/* 821:856 */       return font;
/* 822:    */     }
/* 823:    */   }
/* 824:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTextRun

 * JD-Core Version:    0.7.0.1

 */