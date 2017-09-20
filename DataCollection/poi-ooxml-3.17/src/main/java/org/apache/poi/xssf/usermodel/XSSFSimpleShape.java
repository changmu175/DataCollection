/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Locale;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.hssf.util.HSSFColor;
/*  10:    */ import org.apache.poi.ss.usermodel.SimpleShape;
/*  11:    */ import org.apache.poi.ss.usermodel.VerticalAlignment;
/*  12:    */ import org.apache.poi.util.Internal;
/*  13:    */ import org.apache.poi.util.Units;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph.Factory;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.Enum;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType.Enum;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum;
/*  39:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  41:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape.Factory;
/*  42:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShapeNonVisual;
/*  43:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty;
/*  44:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
/*  45:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName;
/*  46:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize;
/*  47:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt;
/*  48:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRPrElt;
/*  49:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;
/*  50:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty;
/*  51:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues;
/*  52:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues.Enum;
/*  53:    */ 
/*  54:    */ public class XSSFSimpleShape
/*  55:    */   extends XSSFShape
/*  56:    */   implements Iterable<XSSFTextParagraph>, SimpleShape
/*  57:    */ {
/*  58:    */   private final List<XSSFTextParagraph> _paragraphs;
/*  59: 49 */   private static CTShape prototype = null;
/*  60:    */   private CTShape ctShape;
/*  61:    */   
/*  62:    */   protected XSSFSimpleShape(XSSFDrawing drawing, CTShape ctShape)
/*  63:    */   {
/*  64: 57 */     this.drawing = drawing;
/*  65: 58 */     this.ctShape = ctShape;
/*  66:    */     
/*  67: 60 */     this._paragraphs = new ArrayList();
/*  68:    */     
/*  69:    */ 
/*  70:    */ 
/*  71: 64 */     CTTextBody body = ctShape.getTxBody();
/*  72: 65 */     if (body != null) {
/*  73: 66 */       for (int i = 0; i < body.sizeOfPArray(); i++) {
/*  74: 67 */         this._paragraphs.add(new XSSFTextParagraph(body.getPArray(i), ctShape));
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected static CTShape prototype()
/*  80:    */   {
/*  81: 76 */     if (prototype == null)
/*  82:    */     {
/*  83: 77 */       CTShape shape = CTShape.Factory.newInstance();
/*  84:    */       
/*  85: 79 */       CTShapeNonVisual nv = shape.addNewNvSpPr();
/*  86: 80 */       CTNonVisualDrawingProps nvp = nv.addNewCNvPr();
/*  87: 81 */       nvp.setId(1L);
/*  88: 82 */       nvp.setName("Shape 1");
/*  89: 83 */       nv.addNewCNvSpPr();
/*  90:    */       
/*  91: 85 */       CTShapeProperties sp = shape.addNewSpPr();
/*  92: 86 */       CTTransform2D t2d = sp.addNewXfrm();
/*  93: 87 */       CTPositiveSize2D p1 = t2d.addNewExt();
/*  94: 88 */       p1.setCx(0L);
/*  95: 89 */       p1.setCy(0L);
/*  96: 90 */       CTPoint2D p2 = t2d.addNewOff();
/*  97: 91 */       p2.setX(0L);
/*  98: 92 */       p2.setY(0L);
/*  99:    */       
/* 100: 94 */       CTPresetGeometry2D geom = sp.addNewPrstGeom();
/* 101: 95 */       geom.setPrst(STShapeType.RECT);
/* 102: 96 */       geom.addNewAvLst();
/* 103:    */       
/* 104: 98 */       CTTextBody body = shape.addNewTxBody();
/* 105: 99 */       CTTextBodyProperties bodypr = body.addNewBodyPr();
/* 106:100 */       bodypr.setAnchor(STTextAnchoringType.T);
/* 107:101 */       bodypr.setRtlCol(false);
/* 108:102 */       CTTextParagraph p = body.addNewP();
/* 109:103 */       p.addNewPPr().setAlgn(STTextAlignType.L);
/* 110:104 */       CTTextCharacterProperties endPr = p.addNewEndParaRPr();
/* 111:105 */       endPr.setLang("en-US");
/* 112:106 */       endPr.setSz(1100);
/* 113:107 */       CTSolidColorFillProperties scfpr = endPr.addNewSolidFill();
/* 114:108 */       scfpr.addNewSrgbClr().setVal(new byte[] { 0, 0, 0 });
/* 115:    */       
/* 116:110 */       body.addNewLstStyle();
/* 117:    */       
/* 118:112 */       prototype = shape;
/* 119:    */     }
/* 120:114 */     return prototype;
/* 121:    */   }
/* 122:    */   
/* 123:    */   @Internal
/* 124:    */   public CTShape getCTShape()
/* 125:    */   {
/* 126:119 */     return this.ctShape;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public Iterator<XSSFTextParagraph> iterator()
/* 130:    */   {
/* 131:124 */     return this._paragraphs.iterator();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getText()
/* 135:    */   {
/* 136:133 */     int MAX_LEVELS = 9;
/* 137:134 */     StringBuilder out = new StringBuilder();
/* 138:135 */     List<Integer> levelCount = new ArrayList(9);
/* 139:136 */     XSSFTextParagraph p = null;
/* 140:139 */     for (int k = 0; k < 9; k++) {
/* 141:140 */       levelCount.add(Integer.valueOf(0));
/* 142:    */     }
/* 143:143 */     for (int i = 0; i < this._paragraphs.size(); i++)
/* 144:    */     {
/* 145:144 */       if (out.length() > 0) {
/* 146:144 */         out.append('\n');
/* 147:    */       }
/* 148:145 */       p = (XSSFTextParagraph)this._paragraphs.get(i);
/* 149:147 */       if ((p.isBullet()) && (p.getText().length() > 0))
/* 150:    */       {
/* 151:149 */         int level = Math.min(p.getLevel(), 8);
/* 152:151 */         if (p.isBulletAutoNumber())
/* 153:    */         {
/* 154:152 */           i = processAutoNumGroup(i, level, levelCount, out);
/* 155:    */         }
/* 156:    */         else
/* 157:    */         {
/* 158:155 */           for (int j = 0; j < level; j++) {
/* 159:156 */             out.append('\t');
/* 160:    */           }
/* 161:158 */           String character = p.getBulletCharacter();
/* 162:159 */           out.append(character.length() > 0 ? character + " " : "- ");
/* 163:160 */           out.append(p.getText());
/* 164:    */         }
/* 165:    */       }
/* 166:    */       else
/* 167:    */       {
/* 168:163 */         out.append(p.getText());
/* 169:166 */         for (int k = 0; k < 9; k++) {
/* 170:167 */           levelCount.set(k, Integer.valueOf(0));
/* 171:    */         }
/* 172:    */       }
/* 173:    */     }
/* 174:172 */     return out.toString();
/* 175:    */   }
/* 176:    */   
/* 177:    */   private int processAutoNumGroup(int index, int level, List<Integer> levelCount, StringBuilder out)
/* 178:    */   {
/* 179:179 */     XSSFTextParagraph p = null;
/* 180:180 */     XSSFTextParagraph nextp = null;
/* 181:    */     
/* 182:    */ 
/* 183:    */ 
/* 184:184 */     p = (XSSFTextParagraph)this._paragraphs.get(index);
/* 185:    */     
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:193 */     int startAt = p.getBulletAutoNumberStart();
/* 194:194 */     ListAutoNumber scheme = p.getBulletAutoNumberScheme();
/* 195:195 */     if (((Integer)levelCount.get(level)).intValue() == 0) {
/* 196:196 */       levelCount.set(level, Integer.valueOf(startAt == 0 ? 1 : startAt));
/* 197:    */     }
/* 198:199 */     for (int j = 0; j < level; j++) {
/* 199:200 */       out.append('\t');
/* 200:    */     }
/* 201:202 */     if (p.getText().length() > 0)
/* 202:    */     {
/* 203:203 */       out.append(getBulletPrefix(scheme, ((Integer)levelCount.get(level)).intValue()));
/* 204:204 */       out.append(p.getText());
/* 205:    */     }
/* 206:    */     for (;;)
/* 207:    */     {
/* 208:207 */       nextp = index + 1 == this._paragraphs.size() ? null : (XSSFTextParagraph)this._paragraphs.get(index + 1);
/* 209:208 */       if ((nextp == null) || 
/* 210:209 */         (!nextp.isBullet()) || (!p.isBulletAutoNumber())) {
/* 211:    */         break;
/* 212:    */       }
/* 213:210 */       if (nextp.getLevel() > level)
/* 214:    */       {
/* 215:212 */         if (out.length() > 0) {
/* 216:212 */           out.append('\n');
/* 217:    */         }
/* 218:213 */         index = processAutoNumGroup(index + 1, nextp.getLevel(), levelCount, out);
/* 219:    */       }
/* 220:    */       else
/* 221:    */       {
/* 222:215 */         if (nextp.getLevel() < level) {
/* 223:    */           break;
/* 224:    */         }
/* 225:218 */         ListAutoNumber nextScheme = nextp.getBulletAutoNumberScheme();
/* 226:219 */         int nextStartAt = nextp.getBulletAutoNumberStart();
/* 227:221 */         if ((nextScheme != scheme) || (nextStartAt != startAt)) {
/* 228:    */           break;
/* 229:    */         }
/* 230:223 */         index++;
/* 231:224 */         if (out.length() > 0) {
/* 232:224 */           out.append('\n');
/* 233:    */         }
/* 234:226 */         for (int j = 0; j < level; j++) {
/* 235:227 */           out.append('\t');
/* 236:    */         }
/* 237:230 */         if (nextp.getText().length() > 0)
/* 238:    */         {
/* 239:232 */           levelCount.set(level, Integer.valueOf(((Integer)levelCount.get(level)).intValue() + 1));
/* 240:233 */           out.append(getBulletPrefix(nextScheme, ((Integer)levelCount.get(level)).intValue()));
/* 241:234 */           out.append(nextp.getText());
/* 242:    */         }
/* 243:    */       }
/* 244:    */     }
/* 245:242 */     levelCount.set(level, Integer.valueOf(0));
/* 246:    */     
/* 247:244 */     return index;
/* 248:    */   }
/* 249:    */   
/* 250:    */   private String getBulletPrefix(ListAutoNumber scheme, int value)
/* 251:    */   {
/* 252:253 */     StringBuilder out = new StringBuilder();
/* 253:255 */     switch (1.$SwitchMap$org$apache$poi$xssf$usermodel$ListAutoNumber[scheme.ordinal()])
/* 254:    */     {
/* 255:    */     case 1: 
/* 256:    */     case 2: 
/* 257:258 */       if (scheme == ListAutoNumber.ALPHA_LC_PARENT_BOTH) {
/* 258:258 */         out.append('(');
/* 259:    */       }
/* 260:259 */       out.append(valueToAlpha(value).toLowerCase(Locale.ROOT));
/* 261:260 */       out.append(')');
/* 262:261 */       break;
/* 263:    */     case 3: 
/* 264:    */     case 4: 
/* 265:264 */       if (scheme == ListAutoNumber.ALPHA_UC_PARENT_BOTH) {
/* 266:264 */         out.append('(');
/* 267:    */       }
/* 268:265 */       out.append(valueToAlpha(value));
/* 269:266 */       out.append(')');
/* 270:267 */       break;
/* 271:    */     case 5: 
/* 272:269 */       out.append(valueToAlpha(value).toLowerCase(Locale.ROOT));
/* 273:270 */       out.append('.');
/* 274:271 */       break;
/* 275:    */     case 6: 
/* 276:273 */       out.append(valueToAlpha(value));
/* 277:274 */       out.append('.');
/* 278:275 */       break;
/* 279:    */     case 7: 
/* 280:    */     case 8: 
/* 281:278 */       if (scheme == ListAutoNumber.ARABIC_PARENT_BOTH) {
/* 282:278 */         out.append('(');
/* 283:    */       }
/* 284:279 */       out.append(value);
/* 285:280 */       out.append(')');
/* 286:281 */       break;
/* 287:    */     case 9: 
/* 288:283 */       out.append(value);
/* 289:284 */       out.append('.');
/* 290:285 */       break;
/* 291:    */     case 10: 
/* 292:287 */       out.append(value);
/* 293:288 */       break;
/* 294:    */     case 11: 
/* 295:    */     case 12: 
/* 296:291 */       if (scheme == ListAutoNumber.ROMAN_LC_PARENT_BOTH) {
/* 297:291 */         out.append('(');
/* 298:    */       }
/* 299:292 */       out.append(valueToRoman(value).toLowerCase(Locale.ROOT));
/* 300:293 */       out.append(')');
/* 301:294 */       break;
/* 302:    */     case 13: 
/* 303:    */     case 14: 
/* 304:297 */       if (scheme == ListAutoNumber.ROMAN_UC_PARENT_BOTH) {
/* 305:297 */         out.append('(');
/* 306:    */       }
/* 307:298 */       out.append(valueToRoman(value));
/* 308:299 */       out.append(')');
/* 309:300 */       break;
/* 310:    */     case 15: 
/* 311:302 */       out.append(valueToRoman(value).toLowerCase(Locale.ROOT));
/* 312:303 */       out.append('.');
/* 313:304 */       break;
/* 314:    */     case 16: 
/* 315:306 */       out.append(valueToRoman(value));
/* 316:307 */       out.append('.');
/* 317:308 */       break;
/* 318:    */     default: 
/* 319:310 */       out.append('•');
/* 320:    */     }
/* 321:313 */     out.append(" ");
/* 322:314 */     return out.toString();
/* 323:    */   }
/* 324:    */   
/* 325:    */   private String valueToAlpha(int value)
/* 326:    */   {
/* 327:321 */     String alpha = "";
/* 328:323 */     while (value > 0)
/* 329:    */     {
/* 330:324 */       int modulo = (value - 1) % 26;
/* 331:325 */       alpha = (char)(65 + modulo) + alpha;
/* 332:326 */       value = (value - modulo) / 26;
/* 333:    */     }
/* 334:328 */     return alpha;
/* 335:    */   }
/* 336:    */   
/* 337:331 */   private static String[] _romanChars = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
/* 338:332 */   private static int[] _romanAlphaValues = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
/* 339:    */   
/* 340:    */   private String valueToRoman(int value)
/* 341:    */   {
/* 342:338 */     StringBuilder out = new StringBuilder();
/* 343:339 */     for (int i = 0; (value > 0) && (i < _romanChars.length); i++) {
/* 344:340 */       while (_romanAlphaValues[i] <= value)
/* 345:    */       {
/* 346:341 */         out.append(_romanChars[i]);
/* 347:342 */         value -= _romanAlphaValues[i];
/* 348:    */       }
/* 349:    */     }
/* 350:345 */     return out.toString();
/* 351:    */   }
/* 352:    */   
/* 353:    */   public void clearText()
/* 354:    */   {
/* 355:352 */     this._paragraphs.clear();
/* 356:353 */     CTTextBody txBody = this.ctShape.getTxBody();
/* 357:354 */     txBody.setPArray(null);
/* 358:    */   }
/* 359:    */   
/* 360:    */   public void setText(String text)
/* 361:    */   {
/* 362:362 */     clearText();
/* 363:    */     
/* 364:364 */     addNewTextParagraph().addNewTextRun().setText(text);
/* 365:    */   }
/* 366:    */   
/* 367:    */   public void setText(XSSFRichTextString str)
/* 368:    */   {
/* 369:373 */     XSSFWorkbook wb = (XSSFWorkbook)getDrawing().getParent().getParent();
/* 370:374 */     str.setStylesTableReference(wb.getStylesSource());
/* 371:    */     
/* 372:376 */     CTTextParagraph p = CTTextParagraph.Factory.newInstance();
/* 373:377 */     if (str.numFormattingRuns() == 0)
/* 374:    */     {
/* 375:378 */       CTRegularTextRun r = p.addNewR();
/* 376:379 */       CTTextCharacterProperties rPr = r.addNewRPr();
/* 377:380 */       rPr.setLang("en-US");
/* 378:381 */       rPr.setSz(1100);
/* 379:382 */       r.setT(str.getString());
/* 380:    */     }
/* 381:    */     else
/* 382:    */     {
/* 383:385 */       for (int i = 0; i < str.getCTRst().sizeOfRArray(); i++)
/* 384:    */       {
/* 385:386 */         CTRElt lt = str.getCTRst().getRArray(i);
/* 386:387 */         CTRPrElt ltPr = lt.getRPr();
/* 387:388 */         if (ltPr == null) {
/* 388:388 */           ltPr = lt.addNewRPr();
/* 389:    */         }
/* 390:390 */         CTRegularTextRun r = p.addNewR();
/* 391:391 */         CTTextCharacterProperties rPr = r.addNewRPr();
/* 392:392 */         rPr.setLang("en-US");
/* 393:    */         
/* 394:394 */         applyAttributes(ltPr, rPr);
/* 395:    */         
/* 396:396 */         r.setT(lt.getT());
/* 397:    */       }
/* 398:    */     }
/* 399:400 */     clearText();
/* 400:401 */     this.ctShape.getTxBody().setPArray(new CTTextParagraph[] { p });
/* 401:402 */     this._paragraphs.add(new XSSFTextParagraph(this.ctShape.getTxBody().getPArray(0), this.ctShape));
/* 402:    */   }
/* 403:    */   
/* 404:    */   public List<XSSFTextParagraph> getTextParagraphs()
/* 405:    */   {
/* 406:411 */     return this._paragraphs;
/* 407:    */   }
/* 408:    */   
/* 409:    */   public XSSFTextParagraph addNewTextParagraph()
/* 410:    */   {
/* 411:420 */     CTTextBody txBody = this.ctShape.getTxBody();
/* 412:421 */     CTTextParagraph p = txBody.addNewP();
/* 413:422 */     XSSFTextParagraph paragraph = new XSSFTextParagraph(p, this.ctShape);
/* 414:423 */     this._paragraphs.add(paragraph);
/* 415:424 */     return paragraph;
/* 416:    */   }
/* 417:    */   
/* 418:    */   public XSSFTextParagraph addNewTextParagraph(String text)
/* 419:    */   {
/* 420:433 */     XSSFTextParagraph paragraph = addNewTextParagraph();
/* 421:434 */     paragraph.addNewTextRun().setText(text);
/* 422:435 */     return paragraph;
/* 423:    */   }
/* 424:    */   
/* 425:    */   public XSSFTextParagraph addNewTextParagraph(XSSFRichTextString str)
/* 426:    */   {
/* 427:444 */     CTTextBody txBody = this.ctShape.getTxBody();
/* 428:445 */     CTTextParagraph p = txBody.addNewP();
/* 429:447 */     if (str.numFormattingRuns() == 0)
/* 430:    */     {
/* 431:448 */       CTRegularTextRun r = p.addNewR();
/* 432:449 */       CTTextCharacterProperties rPr = r.addNewRPr();
/* 433:450 */       rPr.setLang("en-US");
/* 434:451 */       rPr.setSz(1100);
/* 435:452 */       r.setT(str.getString());
/* 436:    */     }
/* 437:    */     else
/* 438:    */     {
/* 439:455 */       for (int i = 0; i < str.getCTRst().sizeOfRArray(); i++)
/* 440:    */       {
/* 441:456 */         CTRElt lt = str.getCTRst().getRArray(i);
/* 442:457 */         CTRPrElt ltPr = lt.getRPr();
/* 443:458 */         if (ltPr == null) {
/* 444:458 */           ltPr = lt.addNewRPr();
/* 445:    */         }
/* 446:460 */         CTRegularTextRun r = p.addNewR();
/* 447:461 */         CTTextCharacterProperties rPr = r.addNewRPr();
/* 448:462 */         rPr.setLang("en-US");
/* 449:    */         
/* 450:464 */         applyAttributes(ltPr, rPr);
/* 451:    */         
/* 452:466 */         r.setT(lt.getT());
/* 453:    */       }
/* 454:    */     }
/* 455:471 */     XSSFTextParagraph paragraph = new XSSFTextParagraph(p, this.ctShape);
/* 456:472 */     this._paragraphs.add(paragraph);
/* 457:    */     
/* 458:474 */     return paragraph;
/* 459:    */   }
/* 460:    */   
/* 461:    */   public void setTextHorizontalOverflow(TextHorizontalOverflow overflow)
/* 462:    */   {
/* 463:484 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 464:485 */     if (bodyPr != null) {
/* 465:486 */       if (overflow == null)
/* 466:    */       {
/* 467:487 */         if (bodyPr.isSetHorzOverflow()) {
/* 468:487 */           bodyPr.unsetHorzOverflow();
/* 469:    */         }
/* 470:    */       }
/* 471:    */       else {
/* 472:489 */         bodyPr.setHorzOverflow(STTextHorzOverflowType.Enum.forInt(overflow.ordinal() + 1));
/* 473:    */       }
/* 474:    */     }
/* 475:    */   }
/* 476:    */   
/* 477:    */   public TextHorizontalOverflow getTextHorizontalOverflow()
/* 478:    */   {
/* 479:500 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 480:501 */     if ((bodyPr != null) && 
/* 481:502 */       (bodyPr.isSetHorzOverflow())) {
/* 482:503 */       return TextHorizontalOverflow.values()[(bodyPr.getHorzOverflow().intValue() - 1)];
/* 483:    */     }
/* 484:506 */     return TextHorizontalOverflow.OVERFLOW;
/* 485:    */   }
/* 486:    */   
/* 487:    */   public void setTextVerticalOverflow(TextVerticalOverflow overflow)
/* 488:    */   {
/* 489:516 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 490:517 */     if (bodyPr != null) {
/* 491:518 */       if (overflow == null)
/* 492:    */       {
/* 493:519 */         if (bodyPr.isSetVertOverflow()) {
/* 494:519 */           bodyPr.unsetVertOverflow();
/* 495:    */         }
/* 496:    */       }
/* 497:    */       else {
/* 498:521 */         bodyPr.setVertOverflow(STTextVertOverflowType.Enum.forInt(overflow.ordinal() + 1));
/* 499:    */       }
/* 500:    */     }
/* 501:    */   }
/* 502:    */   
/* 503:    */   public TextVerticalOverflow getTextVerticalOverflow()
/* 504:    */   {
/* 505:532 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 506:533 */     if ((bodyPr != null) && 
/* 507:534 */       (bodyPr.isSetVertOverflow())) {
/* 508:535 */       return TextVerticalOverflow.values()[(bodyPr.getVertOverflow().intValue() - 1)];
/* 509:    */     }
/* 510:538 */     return TextVerticalOverflow.OVERFLOW;
/* 511:    */   }
/* 512:    */   
/* 513:    */   public void setVerticalAlignment(VerticalAlignment anchor)
/* 514:    */   {
/* 515:548 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 516:549 */     if (bodyPr != null) {
/* 517:550 */       if (anchor == null)
/* 518:    */       {
/* 519:551 */         if (bodyPr.isSetAnchor()) {
/* 520:551 */           bodyPr.unsetAnchor();
/* 521:    */         }
/* 522:    */       }
/* 523:    */       else {
/* 524:553 */         bodyPr.setAnchor(STTextAnchoringType.Enum.forInt(anchor.ordinal() + 1));
/* 525:    */       }
/* 526:    */     }
/* 527:    */   }
/* 528:    */   
/* 529:    */   public VerticalAlignment getVerticalAlignment()
/* 530:    */   {
/* 531:564 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 532:565 */     if ((bodyPr != null) && 
/* 533:566 */       (bodyPr.isSetAnchor())) {
/* 534:567 */       return VerticalAlignment.values()[(bodyPr.getAnchor().intValue() - 1)];
/* 535:    */     }
/* 536:570 */     return VerticalAlignment.TOP;
/* 537:    */   }
/* 538:    */   
/* 539:    */   public void setTextDirection(TextDirection orientation)
/* 540:    */   {
/* 541:580 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 542:581 */     if (bodyPr != null) {
/* 543:582 */       if (orientation == null)
/* 544:    */       {
/* 545:583 */         if (bodyPr.isSetVert()) {
/* 546:583 */           bodyPr.unsetVert();
/* 547:    */         }
/* 548:    */       }
/* 549:    */       else {
/* 550:585 */         bodyPr.setVert(STTextVerticalType.Enum.forInt(orientation.ordinal() + 1));
/* 551:    */       }
/* 552:    */     }
/* 553:    */   }
/* 554:    */   
/* 555:    */   public TextDirection getTextDirection()
/* 556:    */   {
/* 557:596 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 558:597 */     if (bodyPr != null)
/* 559:    */     {
/* 560:598 */       STTextVerticalType.Enum val = bodyPr.getVert();
/* 561:599 */       if (val != null) {
/* 562:600 */         return TextDirection.values()[(val.intValue() - 1)];
/* 563:    */       }
/* 564:    */     }
/* 565:603 */     return TextDirection.HORIZONTAL;
/* 566:    */   }
/* 567:    */   
/* 568:    */   public double getBottomInset()
/* 569:    */   {
/* 570:614 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 571:615 */     if ((bodyPr != null) && 
/* 572:616 */       (bodyPr.isSetBIns())) {
/* 573:617 */       return Units.toPoints(bodyPr.getBIns());
/* 574:    */     }
/* 575:621 */     return 3.6D;
/* 576:    */   }
/* 577:    */   
/* 578:    */   public double getLeftInset()
/* 579:    */   {
/* 580:632 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 581:633 */     if ((bodyPr != null) && 
/* 582:634 */       (bodyPr.isSetLIns())) {
/* 583:635 */       return Units.toPoints(bodyPr.getLIns());
/* 584:    */     }
/* 585:639 */     return 3.6D;
/* 586:    */   }
/* 587:    */   
/* 588:    */   public double getRightInset()
/* 589:    */   {
/* 590:650 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 591:651 */     if ((bodyPr != null) && 
/* 592:652 */       (bodyPr.isSetRIns())) {
/* 593:653 */       return Units.toPoints(bodyPr.getRIns());
/* 594:    */     }
/* 595:657 */     return 3.6D;
/* 596:    */   }
/* 597:    */   
/* 598:    */   public double getTopInset()
/* 599:    */   {
/* 600:667 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 601:668 */     if ((bodyPr != null) && 
/* 602:669 */       (bodyPr.isSetTIns())) {
/* 603:670 */       return Units.toPoints(bodyPr.getTIns());
/* 604:    */     }
/* 605:674 */     return 3.6D;
/* 606:    */   }
/* 607:    */   
/* 608:    */   public void setBottomInset(double margin)
/* 609:    */   {
/* 610:684 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 611:685 */     if (bodyPr != null) {
/* 612:686 */       if (margin == -1.0D)
/* 613:    */       {
/* 614:687 */         if (bodyPr.isSetBIns()) {
/* 615:687 */           bodyPr.unsetBIns();
/* 616:    */         }
/* 617:    */       }
/* 618:    */       else {
/* 619:688 */         bodyPr.setBIns(Units.toEMU(margin));
/* 620:    */       }
/* 621:    */     }
/* 622:    */   }
/* 623:    */   
/* 624:    */   public void setLeftInset(double margin)
/* 625:    */   {
/* 626:699 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 627:700 */     if (bodyPr != null) {
/* 628:701 */       if (margin == -1.0D)
/* 629:    */       {
/* 630:702 */         if (bodyPr.isSetLIns()) {
/* 631:702 */           bodyPr.unsetLIns();
/* 632:    */         }
/* 633:    */       }
/* 634:    */       else {
/* 635:703 */         bodyPr.setLIns(Units.toEMU(margin));
/* 636:    */       }
/* 637:    */     }
/* 638:    */   }
/* 639:    */   
/* 640:    */   public void setRightInset(double margin)
/* 641:    */   {
/* 642:714 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 643:715 */     if (bodyPr != null) {
/* 644:716 */       if (margin == -1.0D)
/* 645:    */       {
/* 646:717 */         if (bodyPr.isSetRIns()) {
/* 647:717 */           bodyPr.unsetRIns();
/* 648:    */         }
/* 649:    */       }
/* 650:    */       else {
/* 651:718 */         bodyPr.setRIns(Units.toEMU(margin));
/* 652:    */       }
/* 653:    */     }
/* 654:    */   }
/* 655:    */   
/* 656:    */   public void setTopInset(double margin)
/* 657:    */   {
/* 658:729 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 659:730 */     if (bodyPr != null) {
/* 660:731 */       if (margin == -1.0D)
/* 661:    */       {
/* 662:732 */         if (bodyPr.isSetTIns()) {
/* 663:732 */           bodyPr.unsetTIns();
/* 664:    */         }
/* 665:    */       }
/* 666:    */       else {
/* 667:733 */         bodyPr.setTIns(Units.toEMU(margin));
/* 668:    */       }
/* 669:    */     }
/* 670:    */   }
/* 671:    */   
/* 672:    */   public boolean getWordWrap()
/* 673:    */   {
/* 674:742 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 675:743 */     if ((bodyPr != null) && 
/* 676:744 */       (bodyPr.isSetWrap())) {
/* 677:745 */       return bodyPr.getWrap() == STTextWrappingType.SQUARE;
/* 678:    */     }
/* 679:748 */     return true;
/* 680:    */   }
/* 681:    */   
/* 682:    */   public void setWordWrap(boolean wrap)
/* 683:    */   {
/* 684:756 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 685:757 */     if (bodyPr != null) {
/* 686:758 */       bodyPr.setWrap(wrap ? STTextWrappingType.SQUARE : STTextWrappingType.NONE);
/* 687:    */     }
/* 688:    */   }
/* 689:    */   
/* 690:    */   public void setTextAutofit(TextAutofit value)
/* 691:    */   {
/* 692:770 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 693:771 */     if (bodyPr != null)
/* 694:    */     {
/* 695:772 */       if (bodyPr.isSetSpAutoFit()) {
/* 696:772 */         bodyPr.unsetSpAutoFit();
/* 697:    */       }
/* 698:773 */       if (bodyPr.isSetNoAutofit()) {
/* 699:773 */         bodyPr.unsetNoAutofit();
/* 700:    */       }
/* 701:774 */       if (bodyPr.isSetNormAutofit()) {
/* 702:774 */         bodyPr.unsetNormAutofit();
/* 703:    */       }
/* 704:776 */       switch (1.$SwitchMap$org$apache$poi$xssf$usermodel$TextAutofit[value.ordinal()])
/* 705:    */       {
/* 706:    */       case 1: 
/* 707:777 */         bodyPr.addNewNoAutofit(); break;
/* 708:    */       case 2: 
/* 709:778 */         bodyPr.addNewNormAutofit(); break;
/* 710:    */       case 3: 
/* 711:779 */         bodyPr.addNewSpAutoFit();
/* 712:    */       }
/* 713:    */     }
/* 714:    */   }
/* 715:    */   
/* 716:    */   public TextAutofit getTextAutofit()
/* 717:    */   {
/* 718:789 */     CTTextBodyProperties bodyPr = this.ctShape.getTxBody().getBodyPr();
/* 719:790 */     if (bodyPr != null)
/* 720:    */     {
/* 721:791 */       if (bodyPr.isSetNoAutofit()) {
/* 722:791 */         return TextAutofit.NONE;
/* 723:    */       }
/* 724:792 */       if (bodyPr.isSetNormAutofit()) {
/* 725:792 */         return TextAutofit.NORMAL;
/* 726:    */       }
/* 727:793 */       if (bodyPr.isSetSpAutoFit()) {
/* 728:793 */         return TextAutofit.SHAPE;
/* 729:    */       }
/* 730:    */     }
/* 731:795 */     return TextAutofit.NORMAL;
/* 732:    */   }
/* 733:    */   
/* 734:    */   public int getShapeType()
/* 735:    */   {
/* 736:805 */     return this.ctShape.getSpPr().getPrstGeom().getPrst().intValue();
/* 737:    */   }
/* 738:    */   
/* 739:    */   public void setShapeType(int type)
/* 740:    */   {
/* 741:815 */     this.ctShape.getSpPr().getPrstGeom().setPrst(STShapeType.Enum.forInt(type));
/* 742:    */   }
/* 743:    */   
/* 744:    */   protected CTShapeProperties getShapeProperties()
/* 745:    */   {
/* 746:819 */     return this.ctShape.getSpPr();
/* 747:    */   }
/* 748:    */   
/* 749:    */   private static void applyAttributes(CTRPrElt pr, CTTextCharacterProperties rPr)
/* 750:    */   {
/* 751:828 */     if (pr.sizeOfBArray() > 0) {
/* 752:828 */       rPr.setB(pr.getBArray(0).getVal());
/* 753:    */     }
/* 754:829 */     if (pr.sizeOfUArray() > 0)
/* 755:    */     {
/* 756:830 */       STUnderlineValues.Enum u1 = pr.getUArray(0).getVal();
/* 757:831 */       if (u1 == STUnderlineValues.SINGLE) {
/* 758:831 */         rPr.setU(STTextUnderlineType.SNG);
/* 759:832 */       } else if (u1 == STUnderlineValues.DOUBLE) {
/* 760:832 */         rPr.setU(STTextUnderlineType.DBL);
/* 761:833 */       } else if (u1 == STUnderlineValues.NONE) {
/* 762:833 */         rPr.setU(STTextUnderlineType.NONE);
/* 763:    */       }
/* 764:    */     }
/* 765:835 */     if (pr.sizeOfIArray() > 0) {
/* 766:835 */       rPr.setI(pr.getIArray(0).getVal());
/* 767:    */     }
/* 768:837 */     if (pr.sizeOfRFontArray() > 0)
/* 769:    */     {
/* 770:838 */       CTTextFont rFont = rPr.isSetLatin() ? rPr.getLatin() : rPr.addNewLatin();
/* 771:839 */       rFont.setTypeface(pr.getRFontArray(0).getVal());
/* 772:    */     }
/* 773:842 */     if (pr.sizeOfSzArray() > 0)
/* 774:    */     {
/* 775:843 */       int sz = (int)(pr.getSzArray(0).getVal() * 100.0D);
/* 776:844 */       rPr.setSz(sz);
/* 777:    */     }
/* 778:847 */     if (pr.sizeOfColorArray() > 0)
/* 779:    */     {
/* 780:848 */       CTSolidColorFillProperties fill = rPr.isSetSolidFill() ? rPr.getSolidFill() : rPr.addNewSolidFill();
/* 781:849 */       CTColor xlsColor = pr.getColorArray(0);
/* 782:850 */       if (xlsColor.isSetRgb())
/* 783:    */       {
/* 784:851 */         CTSRgbColor clr = fill.isSetSrgbClr() ? fill.getSrgbClr() : fill.addNewSrgbClr();
/* 785:852 */         clr.setVal(xlsColor.getRgb());
/* 786:    */       }
/* 787:854 */       else if (xlsColor.isSetIndexed())
/* 788:    */       {
/* 789:855 */         HSSFColor indexed = (HSSFColor)HSSFColor.getIndexHash().get(Integer.valueOf((int)xlsColor.getIndexed()));
/* 790:856 */         if (indexed != null)
/* 791:    */         {
/* 792:857 */           byte[] rgb = new byte[3];
/* 793:858 */           rgb[0] = ((byte)indexed.getTriplet()[0]);
/* 794:859 */           rgb[1] = ((byte)indexed.getTriplet()[1]);
/* 795:860 */           rgb[2] = ((byte)indexed.getTriplet()[2]);
/* 796:861 */           CTSRgbColor clr = fill.isSetSrgbClr() ? fill.getSrgbClr() : fill.addNewSrgbClr();
/* 797:862 */           clr.setVal(rgb);
/* 798:    */         }
/* 799:    */       }
/* 800:    */     }
/* 801:    */   }
/* 802:    */   
/* 803:    */   public String getShapeName()
/* 804:    */   {
/* 805:870 */     return this.ctShape.getNvSpPr().getCNvPr().getName();
/* 806:    */   }
/* 807:    */   
/* 808:    */   public int getShapeId()
/* 809:    */   {
/* 810:875 */     return (int)this.ctShape.getNvSpPr().getCNvPr().getId();
/* 811:    */   }
/* 812:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFSimpleShape
 * JD-Core Version:    0.7.0.1
 */