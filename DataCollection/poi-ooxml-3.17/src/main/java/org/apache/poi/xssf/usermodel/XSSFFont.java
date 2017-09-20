/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.POIXMLException;
/*   4:    */ import org.apache.poi.ss.usermodel.Font;
/*   5:    */ import org.apache.poi.ss.usermodel.FontCharset;
/*   6:    */ import org.apache.poi.ss.usermodel.FontFamily;
/*   7:    */ import org.apache.poi.ss.usermodel.FontScheme;
/*   8:    */ import org.apache.poi.ss.usermodel.FontUnderline;
/*   9:    */ import org.apache.poi.ss.usermodel.IndexedColors;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ import org.apache.poi.xssf.model.StylesTable;
/*  12:    */ import org.apache.poi.xssf.model.ThemesTable;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont.Factory;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty;
/*  22:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontScheme.Enum;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues.Enum;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignRun;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignRun.Enum;
/*  27:    */ 
/*  28:    */ public class XSSFFont
/*  29:    */   implements Font
/*  30:    */ {
/*  31:    */   public static final String DEFAULT_FONT_NAME = "Calibri";
/*  32:    */   public static final short DEFAULT_FONT_SIZE = 11;
/*  33: 61 */   public static final short DEFAULT_FONT_COLOR = IndexedColors.BLACK.getIndex();
/*  34:    */   private IndexedColorMap _indexedColorMap;
/*  35:    */   private ThemesTable _themes;
/*  36:    */   private CTFont _ctFont;
/*  37:    */   private short _index;
/*  38:    */   
/*  39:    */   public XSSFFont(CTFont font)
/*  40:    */   {
/*  41: 74 */     this._ctFont = font;
/*  42: 75 */     this._index = 0;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public XSSFFont(CTFont font, int index, IndexedColorMap colorMap)
/*  46:    */   {
/*  47: 85 */     this._ctFont = font;
/*  48: 86 */     this._index = ((short)index);
/*  49: 87 */     this._indexedColorMap = colorMap;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected XSSFFont()
/*  53:    */   {
/*  54: 94 */     this._ctFont = CTFont.Factory.newInstance();
/*  55: 95 */     setFontName("Calibri");
/*  56: 96 */     setFontHeight(11.0D);
/*  57:    */   }
/*  58:    */   
/*  59:    */   @Internal
/*  60:    */   public CTFont getCTFont()
/*  61:    */   {
/*  62:104 */     return this._ctFont;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean getBold()
/*  66:    */   {
/*  67:113 */     CTBooleanProperty bold = this._ctFont.sizeOfBArray() == 0 ? null : this._ctFont.getBArray(0);
/*  68:114 */     return (bold != null) && (bold.getVal());
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getCharSet()
/*  72:    */   {
/*  73:124 */     CTIntProperty charset = this._ctFont.sizeOfCharsetArray() == 0 ? null : this._ctFont.getCharsetArray(0);
/*  74:125 */     int val = charset == null ? FontCharset.ANSI.getValue() : FontCharset.valueOf(charset.getVal()).getValue();
/*  75:126 */     return val;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public short getColor()
/*  79:    */   {
/*  80:138 */     CTColor color = this._ctFont.sizeOfColorArray() == 0 ? null : this._ctFont.getColorArray(0);
/*  81:139 */     if (color == null) {
/*  82:139 */       return IndexedColors.BLACK.getIndex();
/*  83:    */     }
/*  84:141 */     long index = color.getIndexed();
/*  85:142 */     if (index == DEFAULT_FONT_COLOR) {
/*  86:143 */       return IndexedColors.BLACK.getIndex();
/*  87:    */     }
/*  88:144 */     if (index == IndexedColors.RED.getIndex()) {
/*  89:145 */       return IndexedColors.RED.getIndex();
/*  90:    */     }
/*  91:147 */     return (short)(int)index;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public XSSFColor getXSSFColor()
/*  95:    */   {
/*  96:159 */     CTColor ctColor = this._ctFont.sizeOfColorArray() == 0 ? null : this._ctFont.getColorArray(0);
/*  97:160 */     if (ctColor != null)
/*  98:    */     {
/*  99:161 */       XSSFColor color = new XSSFColor(ctColor, this._indexedColorMap);
/* 100:162 */       if (this._themes != null) {
/* 101:163 */         this._themes.inheritFromThemeAsRequired(color);
/* 102:    */       }
/* 103:165 */       return color;
/* 104:    */     }
/* 105:167 */     return null;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public short getThemeColor()
/* 109:    */   {
/* 110:179 */     CTColor color = this._ctFont.sizeOfColorArray() == 0 ? null : this._ctFont.getColorArray(0);
/* 111:180 */     long index = color == null ? 0L : color.getTheme();
/* 112:181 */     return (short)(int)index;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public short getFontHeight()
/* 116:    */   {
/* 117:195 */     return (short)(int)(getFontHeightRaw() * 20.0D);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public short getFontHeightInPoints()
/* 121:    */   {
/* 122:207 */     return (short)(int)getFontHeightRaw();
/* 123:    */   }
/* 124:    */   
/* 125:    */   private double getFontHeightRaw()
/* 126:    */   {
/* 127:215 */     CTFontSize size = this._ctFont.sizeOfSzArray() == 0 ? null : this._ctFont.getSzArray(0);
/* 128:216 */     if (size != null)
/* 129:    */     {
/* 130:217 */       double fontHeight = size.getVal();
/* 131:218 */       return fontHeight;
/* 132:    */     }
/* 133:220 */     return 11.0D;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String getFontName()
/* 137:    */   {
/* 138:229 */     CTFontName name = this._ctFont.sizeOfNameArray() == 0 ? null : this._ctFont.getNameArray(0);
/* 139:230 */     return name == null ? "Calibri" : name.getVal();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean getItalic()
/* 143:    */   {
/* 144:239 */     CTBooleanProperty italic = this._ctFont.sizeOfIArray() == 0 ? null : this._ctFont.getIArray(0);
/* 145:240 */     return (italic != null) && (italic.getVal());
/* 146:    */   }
/* 147:    */   
/* 148:    */   public boolean getStrikeout()
/* 149:    */   {
/* 150:249 */     CTBooleanProperty strike = this._ctFont.sizeOfStrikeArray() == 0 ? null : this._ctFont.getStrikeArray(0);
/* 151:250 */     return (strike != null) && (strike.getVal());
/* 152:    */   }
/* 153:    */   
/* 154:    */   public short getTypeOffset()
/* 155:    */   {
/* 156:262 */     CTVerticalAlignFontProperty vAlign = this._ctFont.sizeOfVertAlignArray() == 0 ? null : this._ctFont.getVertAlignArray(0);
/* 157:263 */     if (vAlign == null) {
/* 158:264 */       return 0;
/* 159:    */     }
/* 160:266 */     int val = vAlign.getVal().intValue();
/* 161:267 */     switch (val)
/* 162:    */     {
/* 163:    */     case 1: 
/* 164:269 */       return 0;
/* 165:    */     case 3: 
/* 166:271 */       return 2;
/* 167:    */     case 2: 
/* 168:273 */       return 1;
/* 169:    */     }
/* 170:275 */     throw new POIXMLException("Wrong offset value " + val);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public byte getUnderline()
/* 174:    */   {
/* 175:286 */     CTUnderlineProperty underline = this._ctFont.sizeOfUArray() == 0 ? null : this._ctFont.getUArray(0);
/* 176:287 */     if (underline != null)
/* 177:    */     {
/* 178:288 */       FontUnderline val = FontUnderline.valueOf(underline.getVal().intValue());
/* 179:289 */       return val.getByteValue();
/* 180:    */     }
/* 181:291 */     return 0;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setBold(boolean bold)
/* 185:    */   {
/* 186:300 */     if (bold)
/* 187:    */     {
/* 188:301 */       CTBooleanProperty ctBold = this._ctFont.sizeOfBArray() == 0 ? this._ctFont.addNewB() : this._ctFont.getBArray(0);
/* 189:302 */       ctBold.setVal(bold);
/* 190:    */     }
/* 191:    */     else
/* 192:    */     {
/* 193:304 */       this._ctFont.setBArray(null);
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setCharSet(byte charset)
/* 198:    */   {
/* 199:315 */     int cs = charset & 0xFF;
/* 200:316 */     setCharSet(cs);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void setCharSet(int charset)
/* 204:    */   {
/* 205:325 */     FontCharset fontCharset = FontCharset.valueOf(charset);
/* 206:326 */     if (fontCharset != null) {
/* 207:327 */       setCharSet(fontCharset);
/* 208:    */     } else {
/* 209:329 */       throw new POIXMLException("Attention: an attempt to set a type of unknow charset and charset");
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setCharSet(FontCharset charSet)
/* 214:    */   {
/* 215:    */     CTIntProperty charsetProperty;
/* 216:    */     CTIntProperty charsetProperty;
/* 217:340 */     if (this._ctFont.sizeOfCharsetArray() == 0) {
/* 218:341 */       charsetProperty = this._ctFont.addNewCharset();
/* 219:    */     } else {
/* 220:343 */       charsetProperty = this._ctFont.getCharsetArray(0);
/* 221:    */     }
/* 222:347 */     charsetProperty.setVal(charSet.getValue());
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void setColor(short color)
/* 226:    */   {
/* 227:358 */     CTColor ctColor = this._ctFont.sizeOfColorArray() == 0 ? this._ctFont.addNewColor() : this._ctFont.getColorArray(0);
/* 228:359 */     switch (color)
/* 229:    */     {
/* 230:    */     case 32767: 
/* 231:361 */       ctColor.setIndexed(DEFAULT_FONT_COLOR);
/* 232:362 */       break;
/* 233:    */     case 10: 
/* 234:365 */       ctColor.setIndexed(IndexedColors.RED.getIndex());
/* 235:366 */       break;
/* 236:    */     default: 
/* 237:369 */       ctColor.setIndexed(color);
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void setColor(XSSFColor color)
/* 242:    */   {
/* 243:379 */     if (color == null)
/* 244:    */     {
/* 245:379 */       this._ctFont.setColorArray(null);
/* 246:    */     }
/* 247:    */     else
/* 248:    */     {
/* 249:381 */       CTColor ctColor = this._ctFont.sizeOfColorArray() == 0 ? this._ctFont.addNewColor() : this._ctFont.getColorArray(0);
/* 250:382 */       if (ctColor.isSetIndexed()) {
/* 251:383 */         ctColor.unsetIndexed();
/* 252:    */       }
/* 253:385 */       ctColor.setRgb(color.getRGB());
/* 254:    */     }
/* 255:    */   }
/* 256:    */   
/* 257:    */   public void setFontHeight(short height)
/* 258:    */   {
/* 259:395 */     setFontHeight(height / 20.0D);
/* 260:    */   }
/* 261:    */   
/* 262:    */   public void setFontHeight(double height)
/* 263:    */   {
/* 264:404 */     CTFontSize fontSize = this._ctFont.sizeOfSzArray() == 0 ? this._ctFont.addNewSz() : this._ctFont.getSzArray(0);
/* 265:405 */     fontSize.setVal(height);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void setFontHeightInPoints(short height)
/* 269:    */   {
/* 270:414 */     setFontHeight(height);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void setThemeColor(short theme)
/* 274:    */   {
/* 275:423 */     CTColor ctColor = this._ctFont.sizeOfColorArray() == 0 ? this._ctFont.addNewColor() : this._ctFont.getColorArray(0);
/* 276:424 */     ctColor.setTheme(theme);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public void setFontName(String name)
/* 280:    */   {
/* 281:439 */     CTFontName fontName = this._ctFont.sizeOfNameArray() == 0 ? this._ctFont.addNewName() : this._ctFont.getNameArray(0);
/* 282:440 */     fontName.setVal(name == null ? "Calibri" : name);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public void setItalic(boolean italic)
/* 286:    */   {
/* 287:451 */     if (italic)
/* 288:    */     {
/* 289:452 */       CTBooleanProperty bool = this._ctFont.sizeOfIArray() == 0 ? this._ctFont.addNewI() : this._ctFont.getIArray(0);
/* 290:453 */       bool.setVal(italic);
/* 291:    */     }
/* 292:    */     else
/* 293:    */     {
/* 294:455 */       this._ctFont.setIArray(null);
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void setStrikeout(boolean strikeout)
/* 299:    */   {
/* 300:467 */     if (!strikeout)
/* 301:    */     {
/* 302:467 */       this._ctFont.setStrikeArray(null);
/* 303:    */     }
/* 304:    */     else
/* 305:    */     {
/* 306:469 */       CTBooleanProperty strike = this._ctFont.sizeOfStrikeArray() == 0 ? this._ctFont.addNewStrike() : this._ctFont.getStrikeArray(0);
/* 307:470 */       strike.setVal(strikeout);
/* 308:    */     }
/* 309:    */   }
/* 310:    */   
/* 311:    */   public void setTypeOffset(short offset)
/* 312:    */   {
/* 313:485 */     if (offset == 0)
/* 314:    */     {
/* 315:486 */       this._ctFont.setVertAlignArray(null);
/* 316:    */     }
/* 317:    */     else
/* 318:    */     {
/* 319:488 */       CTVerticalAlignFontProperty offsetProperty = this._ctFont.sizeOfVertAlignArray() == 0 ? this._ctFont.addNewVertAlign() : this._ctFont.getVertAlignArray(0);
/* 320:489 */       switch (offset)
/* 321:    */       {
/* 322:    */       case 0: 
/* 323:491 */         offsetProperty.setVal(STVerticalAlignRun.BASELINE);
/* 324:492 */         break;
/* 325:    */       case 2: 
/* 326:494 */         offsetProperty.setVal(STVerticalAlignRun.SUBSCRIPT);
/* 327:495 */         break;
/* 328:    */       case 1: 
/* 329:497 */         offsetProperty.setVal(STVerticalAlignRun.SUPERSCRIPT);
/* 330:498 */         break;
/* 331:    */       default: 
/* 332:500 */         throw new IllegalStateException("Invalid type offset: " + offset);
/* 333:    */       }
/* 334:    */     }
/* 335:    */   }
/* 336:    */   
/* 337:    */   public void setUnderline(byte underline)
/* 338:    */   {
/* 339:513 */     setUnderline(FontUnderline.valueOf(underline));
/* 340:    */   }
/* 341:    */   
/* 342:    */   public void setUnderline(FontUnderline underline)
/* 343:    */   {
/* 344:524 */     if ((underline == FontUnderline.NONE) && (this._ctFont.sizeOfUArray() > 0))
/* 345:    */     {
/* 346:525 */       this._ctFont.setUArray(null);
/* 347:    */     }
/* 348:    */     else
/* 349:    */     {
/* 350:527 */       CTUnderlineProperty ctUnderline = this._ctFont.sizeOfUArray() == 0 ? this._ctFont.addNewU() : this._ctFont.getUArray(0);
/* 351:528 */       STUnderlineValues.Enum val = STUnderlineValues.Enum.forInt(underline.getValue());
/* 352:529 */       ctUnderline.setVal(val);
/* 353:    */     }
/* 354:    */   }
/* 355:    */   
/* 356:    */   public String toString()
/* 357:    */   {
/* 358:535 */     return this._ctFont.toString();
/* 359:    */   }
/* 360:    */   
/* 361:    */   public long registerTo(StylesTable styles)
/* 362:    */   {
/* 363:544 */     this._themes = styles.getTheme();
/* 364:545 */     short idx = (short)styles.putFont(this, true);
/* 365:546 */     this._index = idx;
/* 366:547 */     return idx;
/* 367:    */   }
/* 368:    */   
/* 369:    */   public void setThemesTable(ThemesTable themes)
/* 370:    */   {
/* 371:555 */     this._themes = themes;
/* 372:    */   }
/* 373:    */   
/* 374:    */   public FontScheme getScheme()
/* 375:    */   {
/* 376:566 */     CTFontScheme scheme = this._ctFont.sizeOfSchemeArray() == 0 ? null : this._ctFont.getSchemeArray(0);
/* 377:567 */     return scheme == null ? FontScheme.NONE : FontScheme.valueOf(scheme.getVal().intValue());
/* 378:    */   }
/* 379:    */   
/* 380:    */   public void setScheme(FontScheme scheme)
/* 381:    */   {
/* 382:577 */     CTFontScheme ctFontScheme = this._ctFont.sizeOfSchemeArray() == 0 ? this._ctFont.addNewScheme() : this._ctFont.getSchemeArray(0);
/* 383:578 */     STFontScheme.Enum val = STFontScheme.Enum.forInt(scheme.getValue());
/* 384:579 */     ctFontScheme.setVal(val);
/* 385:    */   }
/* 386:    */   
/* 387:    */   public int getFamily()
/* 388:    */   {
/* 389:589 */     CTIntProperty family = this._ctFont.sizeOfFamilyArray() == 0 ? null : this._ctFont.getFamilyArray(0);
/* 390:590 */     return family == null ? FontFamily.NOT_APPLICABLE.getValue() : FontFamily.valueOf(family.getVal()).getValue();
/* 391:    */   }
/* 392:    */   
/* 393:    */   public void setFamily(int value)
/* 394:    */   {
/* 395:602 */     CTIntProperty family = this._ctFont.sizeOfFamilyArray() == 0 ? this._ctFont.addNewFamily() : this._ctFont.getFamilyArray(0);
/* 396:603 */     family.setVal(value);
/* 397:    */   }
/* 398:    */   
/* 399:    */   public void setFamily(FontFamily family)
/* 400:    */   {
/* 401:614 */     setFamily(family.getValue());
/* 402:    */   }
/* 403:    */   
/* 404:    */   public short getIndex()
/* 405:    */   {
/* 406:624 */     return this._index;
/* 407:    */   }
/* 408:    */   
/* 409:    */   public int hashCode()
/* 410:    */   {
/* 411:628 */     return this._ctFont.toString().hashCode();
/* 412:    */   }
/* 413:    */   
/* 414:    */   public boolean equals(Object o)
/* 415:    */   {
/* 416:632 */     if (!(o instanceof XSSFFont)) {
/* 417:632 */       return false;
/* 418:    */     }
/* 419:634 */     XSSFFont cf = (XSSFFont)o;
/* 420:635 */     return this._ctFont.toString().equals(cf.getCTFont().toString());
/* 421:    */   }
/* 422:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFFont
 * JD-Core Version:    0.7.0.1
 */