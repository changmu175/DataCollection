/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.Map.Entry;
/*   5:    */ import java.util.Set;
/*   6:    */ import java.util.SortedMap;
/*   7:    */ import java.util.TreeMap;
/*   8:    */ import java.util.regex.Matcher;
/*   9:    */ import java.util.regex.Pattern;
/*  10:    */ import javax.xml.namespace.QName;
/*  11:    */ import org.apache.poi.ss.usermodel.Font;
/*  12:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  13:    */ import org.apache.poi.util.Internal;
/*  14:    */ import org.apache.poi.xssf.model.StylesTable;
/*  15:    */ import org.apache.poi.xssf.model.ThemesTable;
/*  16:    */ import org.apache.xmlbeans.XmlCursor;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont.Factory;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName;
/*  22:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRPrElt;
/*  27:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRPrElt.Factory;
/*  28:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;
/*  29:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst.Factory;
/*  30:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty;
/*  31:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty;
/*  32:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;
/*  33:    */ 
/*  34:    */ public class XSSFRichTextString
/*  35:    */   implements RichTextString
/*  36:    */ {
/*  37: 78 */   private static final Pattern utfPtrn = Pattern.compile("_x([0-9A-Fa-f]{4})_");
/*  38:    */   private CTRst st;
/*  39:    */   private StylesTable styles;
/*  40:    */   
/*  41:    */   public XSSFRichTextString(String str)
/*  42:    */   {
/*  43: 87 */     this.st = CTRst.Factory.newInstance();
/*  44: 88 */     this.st.setT(str);
/*  45: 89 */     preserveSpaces(this.st.xgetT());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public XSSFRichTextString()
/*  49:    */   {
/*  50: 96 */     this.st = CTRst.Factory.newInstance();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public XSSFRichTextString(CTRst st)
/*  54:    */   {
/*  55:103 */     this.st = st;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void applyFont(int startIndex, int endIndex, short fontIndex)
/*  59:    */   {
/*  60:    */     XSSFFont font;
/*  61:115 */     if (this.styles == null)
/*  62:    */     {
/*  63:118 */       XSSFFont font = new XSSFFont();
/*  64:119 */       font.setFontName("#" + fontIndex);
/*  65:    */     }
/*  66:    */     else
/*  67:    */     {
/*  68:121 */       font = this.styles.getFontAt(fontIndex);
/*  69:    */     }
/*  70:123 */     applyFont(startIndex, endIndex, font);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void applyFont(int startIndex, int endIndex, Font font)
/*  74:    */   {
/*  75:134 */     if (startIndex > endIndex) {
/*  76:135 */       throw new IllegalArgumentException("Start index must be less than end index, but had " + startIndex + " and " + endIndex);
/*  77:    */     }
/*  78:136 */     if ((startIndex < 0) || (endIndex > length())) {
/*  79:137 */       throw new IllegalArgumentException("Start and end index not in range, but had " + startIndex + " and " + endIndex);
/*  80:    */     }
/*  81:139 */     if (startIndex == endIndex) {
/*  82:140 */       return;
/*  83:    */     }
/*  84:142 */     if ((this.st.sizeOfRArray() == 0) && (this.st.isSetT()))
/*  85:    */     {
/*  86:144 */       this.st.addNewR().setT(this.st.getT());
/*  87:145 */       this.st.unsetT();
/*  88:    */     }
/*  89:148 */     String text = getString();
/*  90:149 */     XSSFFont xssfFont = (XSSFFont)font;
/*  91:    */     
/*  92:151 */     TreeMap<Integer, CTRPrElt> formats = getFormatMap(this.st);
/*  93:152 */     CTRPrElt fmt = CTRPrElt.Factory.newInstance();
/*  94:153 */     setRunAttributes(xssfFont.getCTFont(), fmt);
/*  95:154 */     applyFont(formats, startIndex, endIndex, fmt);
/*  96:    */     
/*  97:156 */     CTRst newSt = buildCTRst(text, formats);
/*  98:157 */     this.st.set(newSt);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void applyFont(Font font)
/* 102:    */   {
/* 103:165 */     String text = getString();
/* 104:166 */     applyFont(0, text.length(), font);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void applyFont(short fontIndex)
/* 108:    */   {
/* 109:    */     XSSFFont font;
/* 110:176 */     if (this.styles == null)
/* 111:    */     {
/* 112:177 */       XSSFFont font = new XSSFFont();
/* 113:178 */       font.setFontName("#" + fontIndex);
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:180 */       font = this.styles.getFontAt(fontIndex);
/* 118:    */     }
/* 119:182 */     String text = getString();
/* 120:183 */     applyFont(0, text.length(), font);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void append(String text, XSSFFont font)
/* 124:    */   {
/* 125:193 */     if ((this.st.sizeOfRArray() == 0) && (this.st.isSetT()))
/* 126:    */     {
/* 127:195 */       CTRElt lt = this.st.addNewR();
/* 128:196 */       lt.setT(this.st.getT());
/* 129:197 */       preserveSpaces(lt.xgetT());
/* 130:198 */       this.st.unsetT();
/* 131:    */     }
/* 132:200 */     CTRElt lt = this.st.addNewR();
/* 133:201 */     lt.setT(text);
/* 134:202 */     preserveSpaces(lt.xgetT());
/* 135:204 */     if (font != null)
/* 136:    */     {
/* 137:205 */       CTRPrElt pr = lt.addNewRPr();
/* 138:206 */       setRunAttributes(font.getCTFont(), pr);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void append(String text)
/* 143:    */   {
/* 144:216 */     append(text, null);
/* 145:    */   }
/* 146:    */   
/* 147:    */   private void setRunAttributes(CTFont ctFont, CTRPrElt pr)
/* 148:    */   {
/* 149:223 */     if (ctFont.sizeOfBArray() > 0) {
/* 150:223 */       pr.addNewB().setVal(ctFont.getBArray(0).getVal());
/* 151:    */     }
/* 152:224 */     if (ctFont.sizeOfUArray() > 0) {
/* 153:224 */       pr.addNewU().setVal(ctFont.getUArray(0).getVal());
/* 154:    */     }
/* 155:225 */     if (ctFont.sizeOfIArray() > 0) {
/* 156:225 */       pr.addNewI().setVal(ctFont.getIArray(0).getVal());
/* 157:    */     }
/* 158:226 */     if (ctFont.sizeOfColorArray() > 0)
/* 159:    */     {
/* 160:227 */       CTColor c1 = ctFont.getColorArray(0);
/* 161:228 */       CTColor c2 = pr.addNewColor();
/* 162:229 */       if (c1.isSetAuto()) {
/* 163:229 */         c2.setAuto(c1.getAuto());
/* 164:    */       }
/* 165:230 */       if (c1.isSetIndexed()) {
/* 166:230 */         c2.setIndexed(c1.getIndexed());
/* 167:    */       }
/* 168:231 */       if (c1.isSetRgb()) {
/* 169:231 */         c2.setRgb(c1.getRgb());
/* 170:    */       }
/* 171:232 */       if (c1.isSetTheme()) {
/* 172:232 */         c2.setTheme(c1.getTheme());
/* 173:    */       }
/* 174:233 */       if (c1.isSetTint()) {
/* 175:233 */         c2.setTint(c1.getTint());
/* 176:    */       }
/* 177:    */     }
/* 178:235 */     if (ctFont.sizeOfSzArray() > 0) {
/* 179:235 */       pr.addNewSz().setVal(ctFont.getSzArray(0).getVal());
/* 180:    */     }
/* 181:236 */     if (ctFont.sizeOfNameArray() > 0) {
/* 182:236 */       pr.addNewRFont().setVal(ctFont.getNameArray(0).getVal());
/* 183:    */     }
/* 184:237 */     if (ctFont.sizeOfFamilyArray() > 0) {
/* 185:237 */       pr.addNewFamily().setVal(ctFont.getFamilyArray(0).getVal());
/* 186:    */     }
/* 187:238 */     if (ctFont.sizeOfSchemeArray() > 0) {
/* 188:238 */       pr.addNewScheme().setVal(ctFont.getSchemeArray(0).getVal());
/* 189:    */     }
/* 190:239 */     if (ctFont.sizeOfCharsetArray() > 0) {
/* 191:239 */       pr.addNewCharset().setVal(ctFont.getCharsetArray(0).getVal());
/* 192:    */     }
/* 193:240 */     if (ctFont.sizeOfCondenseArray() > 0) {
/* 194:240 */       pr.addNewCondense().setVal(ctFont.getCondenseArray(0).getVal());
/* 195:    */     }
/* 196:241 */     if (ctFont.sizeOfExtendArray() > 0) {
/* 197:241 */       pr.addNewExtend().setVal(ctFont.getExtendArray(0).getVal());
/* 198:    */     }
/* 199:242 */     if (ctFont.sizeOfVertAlignArray() > 0) {
/* 200:242 */       pr.addNewVertAlign().setVal(ctFont.getVertAlignArray(0).getVal());
/* 201:    */     }
/* 202:243 */     if (ctFont.sizeOfOutlineArray() > 0) {
/* 203:243 */       pr.addNewOutline().setVal(ctFont.getOutlineArray(0).getVal());
/* 204:    */     }
/* 205:244 */     if (ctFont.sizeOfShadowArray() > 0) {
/* 206:244 */       pr.addNewShadow().setVal(ctFont.getShadowArray(0).getVal());
/* 207:    */     }
/* 208:245 */     if (ctFont.sizeOfStrikeArray() > 0) {
/* 209:245 */       pr.addNewStrike().setVal(ctFont.getStrikeArray(0).getVal());
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean hasFormatting()
/* 214:    */   {
/* 215:254 */     CTRElt[] rs = this.st.getRArray();
/* 216:255 */     if ((rs == null) || (rs.length == 0)) {
/* 217:256 */       return false;
/* 218:    */     }
/* 219:258 */     for (CTRElt r : rs) {
/* 220:259 */       if (r.isSetRPr()) {
/* 221:259 */         return true;
/* 222:    */       }
/* 223:    */     }
/* 224:261 */     return false;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public void clearFormatting()
/* 228:    */   {
/* 229:268 */     String text = getString();
/* 230:269 */     this.st.setRArray(null);
/* 231:270 */     this.st.setT(text);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public int getIndexOfFormattingRun(int index)
/* 235:    */   {
/* 236:280 */     if (this.st.sizeOfRArray() == 0) {
/* 237:280 */       return 0;
/* 238:    */     }
/* 239:282 */     int pos = 0;
/* 240:283 */     for (int i = 0; i < this.st.sizeOfRArray(); i++)
/* 241:    */     {
/* 242:284 */       CTRElt r = this.st.getRArray(i);
/* 243:285 */       if (i == index) {
/* 244:285 */         return pos;
/* 245:    */       }
/* 246:287 */       pos += r.getT().length();
/* 247:    */     }
/* 248:289 */     return -1;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public int getLengthOfFormattingRun(int index)
/* 252:    */   {
/* 253:299 */     if ((this.st.sizeOfRArray() == 0) || (index >= this.st.sizeOfRArray())) {
/* 254:300 */       return -1;
/* 255:    */     }
/* 256:303 */     CTRElt r = this.st.getRArray(index);
/* 257:304 */     return r.getT().length();
/* 258:    */   }
/* 259:    */   
/* 260:    */   public String getString()
/* 261:    */   {
/* 262:311 */     if (this.st.sizeOfRArray() == 0) {
/* 263:312 */       return utfDecode(this.st.getT());
/* 264:    */     }
/* 265:314 */     StringBuilder buf = new StringBuilder();
/* 266:316 */     for (CTRElt r : this.st.getRArray()) {
/* 267:317 */       buf.append(r.getT());
/* 268:    */     }
/* 269:319 */     return utfDecode(buf.toString());
/* 270:    */   }
/* 271:    */   
/* 272:    */   public void setString(String s)
/* 273:    */   {
/* 274:328 */     clearFormatting();
/* 275:329 */     this.st.setT(s);
/* 276:330 */     preserveSpaces(this.st.xgetT());
/* 277:    */   }
/* 278:    */   
/* 279:    */   public String toString()
/* 280:    */   {
/* 281:337 */     return getString();
/* 282:    */   }
/* 283:    */   
/* 284:    */   public int length()
/* 285:    */   {
/* 286:344 */     return getString().length();
/* 287:    */   }
/* 288:    */   
/* 289:    */   public int numFormattingRuns()
/* 290:    */   {
/* 291:351 */     return this.st.sizeOfRArray();
/* 292:    */   }
/* 293:    */   
/* 294:    */   public XSSFFont getFontOfFormattingRun(int index)
/* 295:    */   {
/* 296:361 */     if ((this.st.sizeOfRArray() == 0) || (index >= this.st.sizeOfRArray())) {
/* 297:362 */       return null;
/* 298:    */     }
/* 299:365 */     CTRElt r = this.st.getRArray(index);
/* 300:366 */     if (r.getRPr() != null)
/* 301:    */     {
/* 302:367 */       XSSFFont fnt = new XSSFFont(toCTFont(r.getRPr()));
/* 303:368 */       fnt.setThemesTable(getThemesTable());
/* 304:369 */       return fnt;
/* 305:    */     }
/* 306:372 */     return null;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public XSSFFont getFontAtIndex(int index)
/* 310:    */   {
/* 311:384 */     ThemesTable themes = getThemesTable();
/* 312:385 */     int pos = 0;
/* 313:387 */     for (CTRElt r : this.st.getRArray())
/* 314:    */     {
/* 315:388 */       int length = r.getT().length();
/* 316:389 */       if ((index >= pos) && (index < pos + length))
/* 317:    */       {
/* 318:390 */         XSSFFont fnt = new XSSFFont(toCTFont(r.getRPr()));
/* 319:391 */         fnt.setThemesTable(themes);
/* 320:392 */         return fnt;
/* 321:    */       }
/* 322:395 */       pos += length;
/* 323:    */     }
/* 324:397 */     return null;
/* 325:    */   }
/* 326:    */   
/* 327:    */   @Internal
/* 328:    */   public CTRst getCTRst()
/* 329:    */   {
/* 330:406 */     return this.st;
/* 331:    */   }
/* 332:    */   
/* 333:    */   protected void setStylesTableReference(StylesTable tbl)
/* 334:    */   {
/* 335:410 */     this.styles = tbl;
/* 336:411 */     if (this.st.sizeOfRArray() > 0) {
/* 337:413 */       for (CTRElt r : this.st.getRArray())
/* 338:    */       {
/* 339:414 */         CTRPrElt pr = r.getRPr();
/* 340:415 */         if ((pr != null) && (pr.sizeOfRFontArray() > 0))
/* 341:    */         {
/* 342:416 */           String fontName = pr.getRFontArray(0).getVal();
/* 343:417 */           if (fontName.startsWith("#"))
/* 344:    */           {
/* 345:418 */             int idx = Integer.parseInt(fontName.substring(1));
/* 346:419 */             XSSFFont font = this.styles.getFontAt(idx);
/* 347:420 */             pr.removeRFont(0);
/* 348:421 */             setRunAttributes(font.getCTFont(), pr);
/* 349:    */           }
/* 350:    */         }
/* 351:    */       }
/* 352:    */     }
/* 353:    */   }
/* 354:    */   
/* 355:    */   protected static CTFont toCTFont(CTRPrElt pr)
/* 356:    */   {
/* 357:433 */     CTFont ctFont = CTFont.Factory.newInstance();
/* 358:436 */     if (pr == null) {
/* 359:437 */       return ctFont;
/* 360:    */     }
/* 361:440 */     if (pr.sizeOfBArray() > 0) {
/* 362:440 */       ctFont.addNewB().setVal(pr.getBArray(0).getVal());
/* 363:    */     }
/* 364:441 */     if (pr.sizeOfUArray() > 0) {
/* 365:441 */       ctFont.addNewU().setVal(pr.getUArray(0).getVal());
/* 366:    */     }
/* 367:442 */     if (pr.sizeOfIArray() > 0) {
/* 368:442 */       ctFont.addNewI().setVal(pr.getIArray(0).getVal());
/* 369:    */     }
/* 370:443 */     if (pr.sizeOfColorArray() > 0)
/* 371:    */     {
/* 372:444 */       CTColor c1 = pr.getColorArray(0);
/* 373:445 */       CTColor c2 = ctFont.addNewColor();
/* 374:446 */       if (c1.isSetAuto()) {
/* 375:446 */         c2.setAuto(c1.getAuto());
/* 376:    */       }
/* 377:447 */       if (c1.isSetIndexed()) {
/* 378:447 */         c2.setIndexed(c1.getIndexed());
/* 379:    */       }
/* 380:448 */       if (c1.isSetRgb()) {
/* 381:448 */         c2.setRgb(c1.getRgb());
/* 382:    */       }
/* 383:449 */       if (c1.isSetTheme()) {
/* 384:449 */         c2.setTheme(c1.getTheme());
/* 385:    */       }
/* 386:450 */       if (c1.isSetTint()) {
/* 387:450 */         c2.setTint(c1.getTint());
/* 388:    */       }
/* 389:    */     }
/* 390:452 */     if (pr.sizeOfSzArray() > 0) {
/* 391:452 */       ctFont.addNewSz().setVal(pr.getSzArray(0).getVal());
/* 392:    */     }
/* 393:453 */     if (pr.sizeOfRFontArray() > 0) {
/* 394:453 */       ctFont.addNewName().setVal(pr.getRFontArray(0).getVal());
/* 395:    */     }
/* 396:454 */     if (pr.sizeOfFamilyArray() > 0) {
/* 397:454 */       ctFont.addNewFamily().setVal(pr.getFamilyArray(0).getVal());
/* 398:    */     }
/* 399:455 */     if (pr.sizeOfSchemeArray() > 0) {
/* 400:455 */       ctFont.addNewScheme().setVal(pr.getSchemeArray(0).getVal());
/* 401:    */     }
/* 402:456 */     if (pr.sizeOfCharsetArray() > 0) {
/* 403:456 */       ctFont.addNewCharset().setVal(pr.getCharsetArray(0).getVal());
/* 404:    */     }
/* 405:457 */     if (pr.sizeOfCondenseArray() > 0) {
/* 406:457 */       ctFont.addNewCondense().setVal(pr.getCondenseArray(0).getVal());
/* 407:    */     }
/* 408:458 */     if (pr.sizeOfExtendArray() > 0) {
/* 409:458 */       ctFont.addNewExtend().setVal(pr.getExtendArray(0).getVal());
/* 410:    */     }
/* 411:459 */     if (pr.sizeOfVertAlignArray() > 0) {
/* 412:459 */       ctFont.addNewVertAlign().setVal(pr.getVertAlignArray(0).getVal());
/* 413:    */     }
/* 414:460 */     if (pr.sizeOfOutlineArray() > 0) {
/* 415:460 */       ctFont.addNewOutline().setVal(pr.getOutlineArray(0).getVal());
/* 416:    */     }
/* 417:461 */     if (pr.sizeOfShadowArray() > 0) {
/* 418:461 */       ctFont.addNewShadow().setVal(pr.getShadowArray(0).getVal());
/* 419:    */     }
/* 420:462 */     if (pr.sizeOfStrikeArray() > 0) {
/* 421:462 */       ctFont.addNewStrike().setVal(pr.getStrikeArray(0).getVal());
/* 422:    */     }
/* 423:464 */     return ctFont;
/* 424:    */   }
/* 425:    */   
/* 426:    */   protected static void preserveSpaces(STXstring xs)
/* 427:    */   {
/* 428:473 */     String text = xs.getStringValue();
/* 429:474 */     if ((text != null) && (text.length() > 0))
/* 430:    */     {
/* 431:475 */       char firstChar = text.charAt(0);
/* 432:476 */       char lastChar = text.charAt(text.length() - 1);
/* 433:477 */       if ((Character.isWhitespace(firstChar)) || (Character.isWhitespace(lastChar)))
/* 434:    */       {
/* 435:478 */         XmlCursor c = xs.newCursor();
/* 436:479 */         c.toNextToken();
/* 437:480 */         c.insertAttributeWithValue(new QName("http://www.w3.org/XML/1998/namespace", "space"), "preserve");
/* 438:481 */         c.dispose();
/* 439:    */       }
/* 440:    */     }
/* 441:    */   }
/* 442:    */   
/* 443:    */   static String utfDecode(String value)
/* 444:    */   {
/* 445:500 */     if ((value == null) || (!value.contains("_x"))) {
/* 446:501 */       return value;
/* 447:    */     }
/* 448:504 */     StringBuilder buf = new StringBuilder();
/* 449:505 */     Matcher m = utfPtrn.matcher(value);
/* 450:506 */     int idx = 0;
/* 451:507 */     while (m.find())
/* 452:    */     {
/* 453:508 */       int pos = m.start();
/* 454:509 */       if (pos > idx) {
/* 455:510 */         buf.append(value.substring(idx, pos));
/* 456:    */       }
/* 457:513 */       String code = m.group(1);
/* 458:514 */       int icode = Integer.decode("0x" + code).intValue();
/* 459:515 */       buf.append((char)icode);
/* 460:    */       
/* 461:517 */       idx = m.end();
/* 462:    */     }
/* 463:522 */     if (idx == 0) {
/* 464:523 */       return value;
/* 465:    */     }
/* 466:526 */     buf.append(value.substring(idx));
/* 467:527 */     return buf.toString();
/* 468:    */   }
/* 469:    */   
/* 470:    */   void applyFont(TreeMap<Integer, CTRPrElt> formats, int startIndex, int endIndex, CTRPrElt fmt)
/* 471:    */   {
/* 472:533 */     int runStartIdx = 0;
/* 473:534 */     for (Iterator<Integer> it = formats.keySet().iterator(); it.hasNext();)
/* 474:    */     {
/* 475:535 */       int runEndIdx = ((Integer)it.next()).intValue();
/* 476:536 */       if ((runStartIdx >= startIndex) && (runEndIdx < endIndex)) {
/* 477:537 */         it.remove();
/* 478:    */       }
/* 479:539 */       runStartIdx = runEndIdx;
/* 480:    */     }
/* 481:542 */     if ((startIndex > 0) && (!formats.containsKey(Integer.valueOf(startIndex)))) {
/* 482:544 */       for (Map.Entry<Integer, CTRPrElt> entry : formats.entrySet()) {
/* 483:545 */         if (((Integer)entry.getKey()).intValue() > startIndex)
/* 484:    */         {
/* 485:546 */           formats.put(Integer.valueOf(startIndex), entry.getValue());
/* 486:547 */           break;
/* 487:    */         }
/* 488:    */       }
/* 489:    */     }
/* 490:551 */     formats.put(Integer.valueOf(endIndex), fmt);
/* 491:    */     
/* 492:    */ 
/* 493:    */ 
/* 494:    */ 
/* 495:556 */     SortedMap<Integer, CTRPrElt> sub = formats.subMap(Integer.valueOf(startIndex), Integer.valueOf(endIndex));
/* 496:557 */     while (sub.size() > 1) {
/* 497:557 */       sub.remove(sub.lastKey());
/* 498:    */     }
/* 499:    */   }
/* 500:    */   
/* 501:    */   TreeMap<Integer, CTRPrElt> getFormatMap(CTRst entry)
/* 502:    */   {
/* 503:561 */     int length = 0;
/* 504:562 */     TreeMap<Integer, CTRPrElt> formats = new TreeMap();
/* 505:564 */     for (CTRElt r : entry.getRArray())
/* 506:    */     {
/* 507:565 */       String txt = r.getT();
/* 508:566 */       CTRPrElt fmt = r.getRPr();
/* 509:    */       
/* 510:568 */       length += txt.length();
/* 511:569 */       formats.put(Integer.valueOf(length), fmt);
/* 512:    */     }
/* 513:571 */     return formats;
/* 514:    */   }
/* 515:    */   
/* 516:    */   CTRst buildCTRst(String text, TreeMap<Integer, CTRPrElt> formats)
/* 517:    */   {
/* 518:575 */     if (text.length() != ((Integer)formats.lastKey()).intValue()) {
/* 519:576 */       throw new IllegalArgumentException("Text length was " + text.length() + " but the last format index was " + formats.lastKey());
/* 520:    */     }
/* 521:579 */     CTRst stf = CTRst.Factory.newInstance();
/* 522:580 */     int runStartIdx = 0;
/* 523:581 */     for (Map.Entry<Integer, CTRPrElt> me : formats.entrySet())
/* 524:    */     {
/* 525:582 */       int runEndIdx = ((Integer)me.getKey()).intValue();
/* 526:583 */       CTRElt run = stf.addNewR();
/* 527:584 */       String fragment = text.substring(runStartIdx, runEndIdx);
/* 528:585 */       run.setT(fragment);
/* 529:586 */       preserveSpaces(run.xgetT());
/* 530:    */       
/* 531:588 */       CTRPrElt fmt = (CTRPrElt)me.getValue();
/* 532:589 */       if (fmt != null) {
/* 533:590 */         run.setRPr(fmt);
/* 534:    */       }
/* 535:592 */       runStartIdx = runEndIdx;
/* 536:    */     }
/* 537:594 */     return stf;
/* 538:    */   }
/* 539:    */   
/* 540:    */   private ThemesTable getThemesTable()
/* 541:    */   {
/* 542:598 */     if (this.styles == null) {
/* 543:598 */       return null;
/* 544:    */     }
/* 545:599 */     return this.styles.getTheme();
/* 546:    */   }
/* 547:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFRichTextString
 * JD-Core Version:    0.7.0.1
 */