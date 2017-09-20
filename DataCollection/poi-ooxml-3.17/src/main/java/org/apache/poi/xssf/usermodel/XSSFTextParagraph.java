/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.apache.poi.util.Units;
/*   9:    */ import org.apache.poi.xssf.model.ParagraphPropertyFetcher;
/*  10:    */ import org.apache.xmlbeans.XmlObject;
/*  11:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun.Factory;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  17:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  18:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing.Factory;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.Enum;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.Enum;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType.Enum;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  38:    */ 
/*  39:    */ public class XSSFTextParagraph
/*  40:    */   implements Iterable<XSSFTextRun>
/*  41:    */ {
/*  42:    */   private final CTTextParagraph _p;
/*  43:    */   private final CTShape _shape;
/*  44:    */   private final List<XSSFTextRun> _runs;
/*  45:    */   
/*  46:    */   XSSFTextParagraph(CTTextParagraph p, CTShape ctShape)
/*  47:    */   {
/*  48: 43 */     this._p = p;
/*  49: 44 */     this._shape = ctShape;
/*  50: 45 */     this._runs = new ArrayList();
/*  51: 47 */     for (XmlObject ch : this._p.selectPath("*")) {
/*  52: 48 */       if ((ch instanceof CTRegularTextRun))
/*  53:    */       {
/*  54: 49 */         CTRegularTextRun r = (CTRegularTextRun)ch;
/*  55: 50 */         this._runs.add(new XSSFTextRun(r, this));
/*  56:    */       }
/*  57: 51 */       else if ((ch instanceof CTTextLineBreak))
/*  58:    */       {
/*  59: 52 */         CTTextLineBreak br = (CTTextLineBreak)ch;
/*  60: 53 */         CTRegularTextRun r = CTRegularTextRun.Factory.newInstance();
/*  61: 54 */         r.setRPr(br.getRPr());
/*  62: 55 */         r.setT("\n");
/*  63: 56 */         this._runs.add(new XSSFTextRun(r, this));
/*  64:    */       }
/*  65: 57 */       else if ((ch instanceof CTTextField))
/*  66:    */       {
/*  67: 58 */         CTTextField f = (CTTextField)ch;
/*  68: 59 */         CTRegularTextRun r = CTRegularTextRun.Factory.newInstance();
/*  69: 60 */         r.setRPr(f.getRPr());
/*  70: 61 */         r.setT(f.getT());
/*  71: 62 */         this._runs.add(new XSSFTextRun(r, this));
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String getText()
/*  77:    */   {
/*  78: 68 */     StringBuilder out = new StringBuilder();
/*  79: 69 */     for (XSSFTextRun r : this._runs) {
/*  80: 70 */       out.append(r.getText());
/*  81:    */     }
/*  82: 72 */     return out.toString();
/*  83:    */   }
/*  84:    */   
/*  85:    */   @Internal
/*  86:    */   public CTTextParagraph getXmlObject()
/*  87:    */   {
/*  88: 77 */     return this._p;
/*  89:    */   }
/*  90:    */   
/*  91:    */   @Internal
/*  92:    */   public CTShape getParentShape()
/*  93:    */   {
/*  94: 82 */     return this._shape;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public List<XSSFTextRun> getTextRuns()
/*  98:    */   {
/*  99: 86 */     return this._runs;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public Iterator<XSSFTextRun> iterator()
/* 103:    */   {
/* 104: 90 */     return this._runs.iterator();
/* 105:    */   }
/* 106:    */   
/* 107:    */   public XSSFTextRun addNewTextRun()
/* 108:    */   {
/* 109: 99 */     CTRegularTextRun r = this._p.addNewR();
/* 110:100 */     CTTextCharacterProperties rPr = r.addNewRPr();
/* 111:101 */     rPr.setLang("en-US");
/* 112:102 */     XSSFTextRun run = new XSSFTextRun(r, this);
/* 113:103 */     this._runs.add(run);
/* 114:104 */     return run;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public XSSFTextRun addLineBreak()
/* 118:    */   {
/* 119:113 */     CTTextLineBreak br = this._p.addNewBr();
/* 120:114 */     CTTextCharacterProperties brProps = br.addNewRPr();
/* 121:115 */     if (this._runs.size() > 0)
/* 122:    */     {
/* 123:117 */       CTTextCharacterProperties prevRun = ((XSSFTextRun)this._runs.get(this._runs.size() - 1)).getRPr();
/* 124:118 */       brProps.set(prevRun);
/* 125:    */     }
/* 126:120 */     CTRegularTextRun r = CTRegularTextRun.Factory.newInstance();
/* 127:121 */     r.setRPr(brProps);
/* 128:122 */     r.setT("\n");
/* 129:123 */     XSSFTextRun run = new XSSFLineBreak(r, this, brProps);
/* 130:124 */     this._runs.add(run);
/* 131:125 */     return run;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public TextAlign getTextAlign()
/* 135:    */   {
/* 136:135 */     ParagraphPropertyFetcher<TextAlign> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 137:    */     {
/* 138:    */       public boolean fetch(CTTextParagraphProperties props)
/* 139:    */       {
/* 140:137 */         if (props.isSetAlgn())
/* 141:    */         {
/* 142:138 */           TextAlign val = TextAlign.values()[(props.getAlgn().intValue() - 1)];
/* 143:139 */           setValue(val);
/* 144:140 */           return true;
/* 145:    */         }
/* 146:142 */         return false;
/* 147:    */       }
/* 148:144 */     };
/* 149:145 */     fetchParagraphProperty(fetcher);
/* 150:146 */     return fetcher.getValue() == null ? TextAlign.LEFT : (TextAlign)fetcher.getValue();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setTextAlign(TextAlign align)
/* 154:    */   {
/* 155:157 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 156:158 */     if (align == null)
/* 157:    */     {
/* 158:159 */       if (pr.isSetAlgn()) {
/* 159:159 */         pr.unsetAlgn();
/* 160:    */       }
/* 161:    */     }
/* 162:    */     else {
/* 163:161 */       pr.setAlgn(STTextAlignType.Enum.forInt(align.ordinal() + 1));
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public TextFontAlign getTextFontAlign()
/* 168:    */   {
/* 169:173 */     ParagraphPropertyFetcher<TextFontAlign> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 170:    */     {
/* 171:    */       public boolean fetch(CTTextParagraphProperties props)
/* 172:    */       {
/* 173:175 */         if (props.isSetFontAlgn())
/* 174:    */         {
/* 175:176 */           TextFontAlign val = TextFontAlign.values()[(props.getFontAlgn().intValue() - 1)];
/* 176:177 */           setValue(val);
/* 177:178 */           return true;
/* 178:    */         }
/* 179:180 */         return false;
/* 180:    */       }
/* 181:182 */     };
/* 182:183 */     fetchParagraphProperty(fetcher);
/* 183:184 */     return fetcher.getValue() == null ? TextFontAlign.BASELINE : (TextFontAlign)fetcher.getValue();
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setTextFontAlign(TextFontAlign align)
/* 187:    */   {
/* 188:196 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 189:197 */     if (align == null)
/* 190:    */     {
/* 191:198 */       if (pr.isSetFontAlgn()) {
/* 192:198 */         pr.unsetFontAlgn();
/* 193:    */       }
/* 194:    */     }
/* 195:    */     else {
/* 196:200 */       pr.setFontAlgn(STTextFontAlignType.Enum.forInt(align.ordinal() + 1));
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public String getBulletFont()
/* 201:    */   {
/* 202:208 */     ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 203:    */     {
/* 204:    */       public boolean fetch(CTTextParagraphProperties props)
/* 205:    */       {
/* 206:210 */         if (props.isSetBuFont())
/* 207:    */         {
/* 208:211 */           setValue(props.getBuFont().getTypeface());
/* 209:212 */           return true;
/* 210:    */         }
/* 211:214 */         return false;
/* 212:    */       }
/* 213:216 */     };
/* 214:217 */     fetchParagraphProperty(fetcher);
/* 215:218 */     return (String)fetcher.getValue();
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setBulletFont(String typeface)
/* 219:    */   {
/* 220:222 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 221:223 */     CTTextFont font = pr.isSetBuFont() ? pr.getBuFont() : pr.addNewBuFont();
/* 222:224 */     font.setTypeface(typeface);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public String getBulletCharacter()
/* 226:    */   {
/* 227:231 */     ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 228:    */     {
/* 229:    */       public boolean fetch(CTTextParagraphProperties props)
/* 230:    */       {
/* 231:233 */         if (props.isSetBuChar())
/* 232:    */         {
/* 233:234 */           setValue(props.getBuChar().getChar());
/* 234:235 */           return true;
/* 235:    */         }
/* 236:237 */         return false;
/* 237:    */       }
/* 238:239 */     };
/* 239:240 */     fetchParagraphProperty(fetcher);
/* 240:241 */     return (String)fetcher.getValue();
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setBulletCharacter(String str)
/* 244:    */   {
/* 245:245 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 246:246 */     CTTextCharBullet c = pr.isSetBuChar() ? pr.getBuChar() : pr.addNewBuChar();
/* 247:247 */     c.setChar(str);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public Color getBulletFontColor()
/* 251:    */   {
/* 252:256 */     ParagraphPropertyFetcher<Color> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 253:    */     {
/* 254:    */       public boolean fetch(CTTextParagraphProperties props)
/* 255:    */       {
/* 256:258 */         if ((props.isSetBuClr()) && 
/* 257:259 */           (props.getBuClr().isSetSrgbClr()))
/* 258:    */         {
/* 259:260 */           CTSRgbColor clr = props.getBuClr().getSrgbClr();
/* 260:261 */           byte[] rgb = clr.getVal();
/* 261:262 */           setValue(new Color(0xFF & rgb[0], 0xFF & rgb[1], 0xFF & rgb[2]));
/* 262:263 */           return true;
/* 263:    */         }
/* 264:266 */         return false;
/* 265:    */       }
/* 266:268 */     };
/* 267:269 */     fetchParagraphProperty(fetcher);
/* 268:270 */     return (Color)fetcher.getValue();
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void setBulletFontColor(Color color)
/* 272:    */   {
/* 273:279 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 274:280 */     CTColor c = pr.isSetBuClr() ? pr.getBuClr() : pr.addNewBuClr();
/* 275:281 */     CTSRgbColor clr = c.isSetSrgbClr() ? c.getSrgbClr() : c.addNewSrgbClr();
/* 276:282 */     clr.setVal(new byte[] { (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() });
/* 277:    */   }
/* 278:    */   
/* 279:    */   public double getBulletFontSize()
/* 280:    */   {
/* 281:296 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 282:    */     {
/* 283:    */       public boolean fetch(CTTextParagraphProperties props)
/* 284:    */       {
/* 285:298 */         if (props.isSetBuSzPct())
/* 286:    */         {
/* 287:299 */           setValue(Double.valueOf(props.getBuSzPct().getVal() * 0.001D));
/* 288:300 */           return true;
/* 289:    */         }
/* 290:302 */         if (props.isSetBuSzPts())
/* 291:    */         {
/* 292:303 */           setValue(Double.valueOf(-props.getBuSzPts().getVal() * 0.01D));
/* 293:304 */           return true;
/* 294:    */         }
/* 295:306 */         return false;
/* 296:    */       }
/* 297:308 */     };
/* 298:309 */     fetchParagraphProperty(fetcher);
/* 299:310 */     return fetcher.getValue() == null ? 100.0D : ((Double)fetcher.getValue()).doubleValue();
/* 300:    */   }
/* 301:    */   
/* 302:    */   public void setBulletFontSize(double bulletSize)
/* 303:    */   {
/* 304:322 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 305:324 */     if (bulletSize >= 0.0D)
/* 306:    */     {
/* 307:326 */       CTTextBulletSizePercent pt = pr.isSetBuSzPct() ? pr.getBuSzPct() : pr.addNewBuSzPct();
/* 308:327 */       pt.setVal((int)(bulletSize * 1000.0D));
/* 309:329 */       if (pr.isSetBuSzPts()) {
/* 310:329 */         pr.unsetBuSzPts();
/* 311:    */       }
/* 312:    */     }
/* 313:    */     else
/* 314:    */     {
/* 315:332 */       CTTextBulletSizePoint pt = pr.isSetBuSzPts() ? pr.getBuSzPts() : pr.addNewBuSzPts();
/* 316:333 */       pt.setVal((int)(-bulletSize * 100.0D));
/* 317:335 */       if (pr.isSetBuSzPct()) {
/* 318:335 */         pr.unsetBuSzPct();
/* 319:    */       }
/* 320:    */     }
/* 321:    */   }
/* 322:    */   
/* 323:    */   public void setIndent(double value)
/* 324:    */   {
/* 325:345 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 326:346 */     if (value == -1.0D)
/* 327:    */     {
/* 328:347 */       if (pr.isSetIndent()) {
/* 329:347 */         pr.unsetIndent();
/* 330:    */       }
/* 331:    */     }
/* 332:    */     else {
/* 333:349 */       pr.setIndent(Units.toEMU(value));
/* 334:    */     }
/* 335:    */   }
/* 336:    */   
/* 337:    */   public double getIndent()
/* 338:    */   {
/* 339:358 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 340:    */     {
/* 341:    */       public boolean fetch(CTTextParagraphProperties props)
/* 342:    */       {
/* 343:360 */         if (props.isSetIndent())
/* 344:    */         {
/* 345:361 */           setValue(Double.valueOf(Units.toPoints(props.getIndent())));
/* 346:362 */           return true;
/* 347:    */         }
/* 348:364 */         return false;
/* 349:    */       }
/* 350:366 */     };
/* 351:367 */     fetchParagraphProperty(fetcher);
/* 352:    */     
/* 353:369 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 354:    */   }
/* 355:    */   
/* 356:    */   public void setLeftMargin(double value)
/* 357:    */   {
/* 358:382 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 359:383 */     if (value == -1.0D)
/* 360:    */     {
/* 361:384 */       if (pr.isSetMarL()) {
/* 362:384 */         pr.unsetMarL();
/* 363:    */       }
/* 364:    */     }
/* 365:    */     else {
/* 366:386 */       pr.setMarL(Units.toEMU(value));
/* 367:    */     }
/* 368:    */   }
/* 369:    */   
/* 370:    */   public double getLeftMargin()
/* 371:    */   {
/* 372:396 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 373:    */     {
/* 374:    */       public boolean fetch(CTTextParagraphProperties props)
/* 375:    */       {
/* 376:398 */         if (props.isSetMarL())
/* 377:    */         {
/* 378:399 */           double val = Units.toPoints(props.getMarL());
/* 379:400 */           setValue(Double.valueOf(val));
/* 380:401 */           return true;
/* 381:    */         }
/* 382:403 */         return false;
/* 383:    */       }
/* 384:405 */     };
/* 385:406 */     fetchParagraphProperty(fetcher);
/* 386:    */     
/* 387:408 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void setRightMargin(double value)
/* 391:    */   {
/* 392:419 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 393:420 */     if (value == -1.0D)
/* 394:    */     {
/* 395:421 */       if (pr.isSetMarR()) {
/* 396:421 */         pr.unsetMarR();
/* 397:    */       }
/* 398:    */     }
/* 399:    */     else {
/* 400:423 */       pr.setMarR(Units.toEMU(value));
/* 401:    */     }
/* 402:    */   }
/* 403:    */   
/* 404:    */   public double getRightMargin()
/* 405:    */   {
/* 406:433 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 407:    */     {
/* 408:    */       public boolean fetch(CTTextParagraphProperties props)
/* 409:    */       {
/* 410:435 */         if (props.isSetMarR())
/* 411:    */         {
/* 412:436 */           double val = Units.toPoints(props.getMarR());
/* 413:437 */           setValue(Double.valueOf(val));
/* 414:438 */           return true;
/* 415:    */         }
/* 416:440 */         return false;
/* 417:    */       }
/* 418:442 */     };
/* 419:443 */     fetchParagraphProperty(fetcher);
/* 420:    */     
/* 421:445 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 422:    */   }
/* 423:    */   
/* 424:    */   public double getDefaultTabSize()
/* 425:    */   {
/* 426:453 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 427:    */     {
/* 428:    */       public boolean fetch(CTTextParagraphProperties props)
/* 429:    */       {
/* 430:455 */         if (props.isSetDefTabSz())
/* 431:    */         {
/* 432:456 */           double val = Units.toPoints(props.getDefTabSz());
/* 433:457 */           setValue(Double.valueOf(val));
/* 434:458 */           return true;
/* 435:    */         }
/* 436:460 */         return false;
/* 437:    */       }
/* 438:462 */     };
/* 439:463 */     fetchParagraphProperty(fetcher);
/* 440:464 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 441:    */   }
/* 442:    */   
/* 443:    */   public double getTabStop(final int idx)
/* 444:    */   {
/* 445:468 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 446:    */     {
/* 447:    */       public boolean fetch(CTTextParagraphProperties props)
/* 448:    */       {
/* 449:470 */         if (props.isSetTabLst())
/* 450:    */         {
/* 451:471 */           CTTextTabStopList tabStops = props.getTabLst();
/* 452:472 */           if (idx < tabStops.sizeOfTabArray())
/* 453:    */           {
/* 454:473 */             CTTextTabStop ts = tabStops.getTabArray(idx);
/* 455:474 */             double val = Units.toPoints(ts.getPos());
/* 456:475 */             setValue(Double.valueOf(val));
/* 457:476 */             return true;
/* 458:    */           }
/* 459:    */         }
/* 460:479 */         return false;
/* 461:    */       }
/* 462:481 */     };
/* 463:482 */     fetchParagraphProperty(fetcher);
/* 464:483 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 465:    */   }
/* 466:    */   
/* 467:    */   public void addTabStop(double value)
/* 468:    */   {
/* 469:492 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 470:493 */     CTTextTabStopList tabStops = pr.isSetTabLst() ? pr.getTabLst() : pr.addNewTabLst();
/* 471:494 */     tabStops.addNewTab().setPos(Units.toEMU(value));
/* 472:    */   }
/* 473:    */   
/* 474:    */   public void setLineSpacing(double linespacing)
/* 475:    */   {
/* 476:519 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 477:520 */     CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
/* 478:521 */     if (linespacing >= 0.0D) {
/* 479:521 */       spc.addNewSpcPct().setVal((int)(linespacing * 1000.0D));
/* 480:    */     } else {
/* 481:522 */       spc.addNewSpcPts().setVal((int)(-linespacing * 100.0D));
/* 482:    */     }
/* 483:523 */     pr.setLnSpc(spc);
/* 484:    */   }
/* 485:    */   
/* 486:    */   public double getLineSpacing()
/* 487:    */   {
/* 488:537 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 489:    */     {
/* 490:    */       public boolean fetch(CTTextParagraphProperties props)
/* 491:    */       {
/* 492:539 */         if (props.isSetLnSpc())
/* 493:    */         {
/* 494:540 */           CTTextSpacing spc = props.getLnSpc();
/* 495:542 */           if (spc.isSetSpcPct()) {
/* 496:542 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/* 497:543 */           } else if (spc.isSetSpcPts()) {
/* 498:543 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/* 499:    */           }
/* 500:544 */           return true;
/* 501:    */         }
/* 502:546 */         return false;
/* 503:    */       }
/* 504:548 */     };
/* 505:549 */     fetchParagraphProperty(fetcher);
/* 506:    */     
/* 507:551 */     double lnSpc = fetcher.getValue() == null ? 100.0D : ((Double)fetcher.getValue()).doubleValue();
/* 508:552 */     if (lnSpc > 0.0D)
/* 509:    */     {
/* 510:554 */       CTTextNormalAutofit normAutofit = this._shape.getTxBody().getBodyPr().getNormAutofit();
/* 511:555 */       if (normAutofit != null)
/* 512:    */       {
/* 513:556 */         double scale = 1.0D - normAutofit.getLnSpcReduction() / 100000.0D;
/* 514:557 */         lnSpc *= scale;
/* 515:    */       }
/* 516:    */     }
/* 517:561 */     return lnSpc;
/* 518:    */   }
/* 519:    */   
/* 520:    */   public void setSpaceBefore(double spaceBefore)
/* 521:    */   {
/* 522:584 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 523:585 */     CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
/* 524:586 */     if (spaceBefore >= 0.0D) {
/* 525:586 */       spc.addNewSpcPct().setVal((int)(spaceBefore * 1000.0D));
/* 526:    */     } else {
/* 527:587 */       spc.addNewSpcPts().setVal((int)(-spaceBefore * 100.0D));
/* 528:    */     }
/* 529:588 */     pr.setSpcBef(spc);
/* 530:    */   }
/* 531:    */   
/* 532:    */   public double getSpaceBefore()
/* 533:    */   {
/* 534:602 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 535:    */     {
/* 536:    */       public boolean fetch(CTTextParagraphProperties props)
/* 537:    */       {
/* 538:604 */         if (props.isSetSpcBef())
/* 539:    */         {
/* 540:605 */           CTTextSpacing spc = props.getSpcBef();
/* 541:607 */           if (spc.isSetSpcPct()) {
/* 542:607 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/* 543:608 */           } else if (spc.isSetSpcPts()) {
/* 544:608 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/* 545:    */           }
/* 546:609 */           return true;
/* 547:    */         }
/* 548:611 */         return false;
/* 549:    */       }
/* 550:613 */     };
/* 551:614 */     fetchParagraphProperty(fetcher);
/* 552:    */     
/* 553:616 */     double spcBef = fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 554:617 */     return spcBef;
/* 555:    */   }
/* 556:    */   
/* 557:    */   public void setSpaceAfter(double spaceAfter)
/* 558:    */   {
/* 559:640 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 560:641 */     CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
/* 561:642 */     if (spaceAfter >= 0.0D) {
/* 562:642 */       spc.addNewSpcPct().setVal((int)(spaceAfter * 1000.0D));
/* 563:    */     } else {
/* 564:643 */       spc.addNewSpcPts().setVal((int)(-spaceAfter * 100.0D));
/* 565:    */     }
/* 566:644 */     pr.setSpcAft(spc);
/* 567:    */   }
/* 568:    */   
/* 569:    */   public double getSpaceAfter()
/* 570:    */   {
/* 571:658 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 572:    */     {
/* 573:    */       public boolean fetch(CTTextParagraphProperties props)
/* 574:    */       {
/* 575:660 */         if (props.isSetSpcAft())
/* 576:    */         {
/* 577:661 */           CTTextSpacing spc = props.getSpcAft();
/* 578:663 */           if (spc.isSetSpcPct()) {
/* 579:663 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/* 580:664 */           } else if (spc.isSetSpcPts()) {
/* 581:664 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/* 582:    */           }
/* 583:665 */           return true;
/* 584:    */         }
/* 585:667 */         return false;
/* 586:    */       }
/* 587:669 */     };
/* 588:670 */     fetchParagraphProperty(fetcher);
/* 589:671 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/* 590:    */   }
/* 591:    */   
/* 592:    */   public void setLevel(int level)
/* 593:    */   {
/* 594:688 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 595:    */     
/* 596:690 */     pr.setLvl(level);
/* 597:    */   }
/* 598:    */   
/* 599:    */   public int getLevel()
/* 600:    */   {
/* 601:699 */     CTTextParagraphProperties pr = this._p.getPPr();
/* 602:700 */     if (pr == null) {
/* 603:700 */       return 0;
/* 604:    */     }
/* 605:702 */     return pr.getLvl();
/* 606:    */   }
/* 607:    */   
/* 608:    */   public boolean isBullet()
/* 609:    */   {
/* 610:710 */     ParagraphPropertyFetcher<Boolean> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 611:    */     {
/* 612:    */       public boolean fetch(CTTextParagraphProperties props)
/* 613:    */       {
/* 614:712 */         if (props.isSetBuNone())
/* 615:    */         {
/* 616:713 */           setValue(Boolean.valueOf(false));
/* 617:714 */           return true;
/* 618:    */         }
/* 619:716 */         if ((props.isSetBuFont()) && (
/* 620:717 */           (props.isSetBuChar()) || (props.isSetBuAutoNum())))
/* 621:    */         {
/* 622:718 */           setValue(Boolean.valueOf(true));
/* 623:719 */           return true;
/* 624:    */         }
/* 625:726 */         return false;
/* 626:    */       }
/* 627:728 */     };
/* 628:729 */     fetchParagraphProperty(fetcher);
/* 629:730 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 630:    */   }
/* 631:    */   
/* 632:    */   public void setBullet(boolean flag)
/* 633:    */   {
/* 634:739 */     if (isBullet() == flag) {
/* 635:739 */       return;
/* 636:    */     }
/* 637:741 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 638:742 */     if (!flag)
/* 639:    */     {
/* 640:743 */       pr.addNewBuNone();
/* 641:745 */       if (pr.isSetBuAutoNum()) {
/* 642:745 */         pr.unsetBuAutoNum();
/* 643:    */       }
/* 644:746 */       if (pr.isSetBuBlip()) {
/* 645:746 */         pr.unsetBuBlip();
/* 646:    */       }
/* 647:747 */       if (pr.isSetBuChar()) {
/* 648:747 */         pr.unsetBuChar();
/* 649:    */       }
/* 650:748 */       if (pr.isSetBuClr()) {
/* 651:748 */         pr.unsetBuClr();
/* 652:    */       }
/* 653:749 */       if (pr.isSetBuClrTx()) {
/* 654:749 */         pr.unsetBuClrTx();
/* 655:    */       }
/* 656:750 */       if (pr.isSetBuFont()) {
/* 657:750 */         pr.unsetBuFont();
/* 658:    */       }
/* 659:751 */       if (pr.isSetBuFontTx()) {
/* 660:751 */         pr.unsetBuFontTx();
/* 661:    */       }
/* 662:752 */       if (pr.isSetBuSzPct()) {
/* 663:752 */         pr.unsetBuSzPct();
/* 664:    */       }
/* 665:753 */       if (pr.isSetBuSzPts()) {
/* 666:753 */         pr.unsetBuSzPts();
/* 667:    */       }
/* 668:754 */       if (pr.isSetBuSzTx()) {
/* 669:754 */         pr.unsetBuSzTx();
/* 670:    */       }
/* 671:    */     }
/* 672:    */     else
/* 673:    */     {
/* 674:756 */       if (pr.isSetBuNone()) {
/* 675:756 */         pr.unsetBuNone();
/* 676:    */       }
/* 677:757 */       if (!pr.isSetBuFont()) {
/* 678:757 */         pr.addNewBuFont().setTypeface("Arial");
/* 679:    */       }
/* 680:758 */       if (!pr.isSetBuAutoNum()) {
/* 681:758 */         pr.addNewBuChar().setChar("•");
/* 682:    */       }
/* 683:    */     }
/* 684:    */   }
/* 685:    */   
/* 686:    */   public void setBullet(ListAutoNumber scheme, int startAt)
/* 687:    */   {
/* 688:770 */     if (startAt < 1) {
/* 689:770 */       throw new IllegalArgumentException("Start Number must be greater or equal that 1");
/* 690:    */     }
/* 691:771 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 692:772 */     CTTextAutonumberBullet lst = pr.isSetBuAutoNum() ? pr.getBuAutoNum() : pr.addNewBuAutoNum();
/* 693:773 */     lst.setType(STTextAutonumberScheme.Enum.forInt(scheme.ordinal() + 1));
/* 694:774 */     lst.setStartAt(startAt);
/* 695:776 */     if (!pr.isSetBuFont()) {
/* 696:776 */       pr.addNewBuFont().setTypeface("Arial");
/* 697:    */     }
/* 698:777 */     if (pr.isSetBuNone()) {
/* 699:777 */       pr.unsetBuNone();
/* 700:    */     }
/* 701:779 */     if (pr.isSetBuBlip()) {
/* 702:779 */       pr.unsetBuBlip();
/* 703:    */     }
/* 704:780 */     if (pr.isSetBuChar()) {
/* 705:780 */       pr.unsetBuChar();
/* 706:    */     }
/* 707:    */   }
/* 708:    */   
/* 709:    */   public void setBullet(ListAutoNumber scheme)
/* 710:    */   {
/* 711:789 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/* 712:790 */     CTTextAutonumberBullet lst = pr.isSetBuAutoNum() ? pr.getBuAutoNum() : pr.addNewBuAutoNum();
/* 713:791 */     lst.setType(STTextAutonumberScheme.Enum.forInt(scheme.ordinal() + 1));
/* 714:793 */     if (!pr.isSetBuFont()) {
/* 715:793 */       pr.addNewBuFont().setTypeface("Arial");
/* 716:    */     }
/* 717:794 */     if (pr.isSetBuNone()) {
/* 718:794 */       pr.unsetBuNone();
/* 719:    */     }
/* 720:796 */     if (pr.isSetBuBlip()) {
/* 721:796 */       pr.unsetBuBlip();
/* 722:    */     }
/* 723:797 */     if (pr.isSetBuChar()) {
/* 724:797 */       pr.unsetBuChar();
/* 725:    */     }
/* 726:    */   }
/* 727:    */   
/* 728:    */   public boolean isBulletAutoNumber()
/* 729:    */   {
/* 730:804 */     ParagraphPropertyFetcher<Boolean> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 731:    */     {
/* 732:    */       public boolean fetch(CTTextParagraphProperties props)
/* 733:    */       {
/* 734:806 */         if (props.isSetBuAutoNum())
/* 735:    */         {
/* 736:807 */           setValue(Boolean.valueOf(true));
/* 737:808 */           return true;
/* 738:    */         }
/* 739:810 */         return false;
/* 740:    */       }
/* 741:812 */     };
/* 742:813 */     fetchParagraphProperty(fetcher);
/* 743:814 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 744:    */   }
/* 745:    */   
/* 746:    */   public int getBulletAutoNumberStart()
/* 747:    */   {
/* 748:821 */     ParagraphPropertyFetcher<Integer> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 749:    */     {
/* 750:    */       public boolean fetch(CTTextParagraphProperties props)
/* 751:    */       {
/* 752:823 */         if ((props.isSetBuAutoNum()) && (props.getBuAutoNum().isSetStartAt()))
/* 753:    */         {
/* 754:824 */           setValue(Integer.valueOf(props.getBuAutoNum().getStartAt()));
/* 755:825 */           return true;
/* 756:    */         }
/* 757:827 */         return false;
/* 758:    */       }
/* 759:829 */     };
/* 760:830 */     fetchParagraphProperty(fetcher);
/* 761:831 */     return fetcher.getValue() == null ? 0 : ((Integer)fetcher.getValue()).intValue();
/* 762:    */   }
/* 763:    */   
/* 764:    */   public ListAutoNumber getBulletAutoNumberScheme()
/* 765:    */   {
/* 766:838 */     ParagraphPropertyFetcher<ListAutoNumber> fetcher = new ParagraphPropertyFetcher(getLevel())
/* 767:    */     {
/* 768:    */       public boolean fetch(CTTextParagraphProperties props)
/* 769:    */       {
/* 770:840 */         if (props.isSetBuAutoNum())
/* 771:    */         {
/* 772:841 */           setValue(ListAutoNumber.values()[(props.getBuAutoNum().getType().intValue() - 1)]);
/* 773:842 */           return true;
/* 774:    */         }
/* 775:844 */         return false;
/* 776:    */       }
/* 777:846 */     };
/* 778:847 */     fetchParagraphProperty(fetcher);
/* 779:    */     
/* 780:    */ 
/* 781:850 */     return fetcher.getValue() == null ? ListAutoNumber.ARABIC_PLAIN : (ListAutoNumber)fetcher.getValue();
/* 782:    */   }
/* 783:    */   
/* 784:    */   private boolean fetchParagraphProperty(ParagraphPropertyFetcher visitor)
/* 785:    */   {
/* 786:856 */     boolean ok = false;
/* 787:858 */     if (this._p.isSetPPr()) {
/* 788:858 */       ok = visitor.fetch(this._p.getPPr());
/* 789:    */     }
/* 790:860 */     if (!ok) {
/* 791:861 */       ok = visitor.fetch(this._shape);
/* 792:    */     }
/* 793:864 */     return ok;
/* 794:    */   }
/* 795:    */   
/* 796:    */   public String toString()
/* 797:    */   {
/* 798:869 */     return "[" + getClass() + "]" + getText();
/* 799:    */   }
/* 800:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFTextParagraph
 * JD-Core Version:    0.7.0.1
 */