/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.geom.Rectangle2D;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  10:    */ import org.apache.poi.sl.draw.DrawTextShape;
/*  11:    */ import org.apache.poi.sl.usermodel.Insets2D;
/*  12:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*  13:    */ import org.apache.poi.sl.usermodel.TextShape;
/*  14:    */ import org.apache.poi.sl.usermodel.TextShape.TextAutofit;
/*  15:    */ import org.apache.poi.sl.usermodel.TextShape.TextDirection;
/*  16:    */ import org.apache.poi.sl.usermodel.TextShape.TextPlaceholder;
/*  17:    */ import org.apache.poi.sl.usermodel.VerticalAlignment;
/*  18:    */ import org.apache.poi.util.Units;
/*  19:    */ import org.apache.poi.xslf.model.PropertyFetcher;
/*  20:    */ import org.apache.poi.xslf.model.TextBodyPropertyFetcher;
/*  21:    */ import org.apache.xmlbeans.XmlObject;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType;
/*  31:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  32:    */ import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum;
/*  33:    */ 
/*  34:    */ public abstract class XSLFTextShape
/*  35:    */   extends XSLFSimpleShape
/*  36:    */   implements TextShape<XSLFShape, XSLFTextParagraph>
/*  37:    */ {
/*  38:    */   private final List<XSLFTextParagraph> _paragraphs;
/*  39:    */   
/*  40:    */   XSLFTextShape(XmlObject shape, XSLFSheet sheet)
/*  41:    */   {
/*  42: 60 */     super(shape, sheet);
/*  43:    */     
/*  44: 62 */     this._paragraphs = new ArrayList();
/*  45: 63 */     CTTextBody txBody = getTextBody(false);
/*  46: 64 */     if (txBody != null) {
/*  47: 65 */       for (CTTextParagraph p : txBody.getPArray()) {
/*  48: 66 */         this._paragraphs.add(newTextParagraph(p));
/*  49:    */       }
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Iterator<XSLFTextParagraph> iterator()
/*  54:    */   {
/*  55: 73 */     return getTextParagraphs().iterator();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getText()
/*  59:    */   {
/*  60: 78 */     StringBuilder out = new StringBuilder();
/*  61: 79 */     for (XSLFTextParagraph p : this._paragraphs)
/*  62:    */     {
/*  63: 80 */       if (out.length() > 0) {
/*  64: 81 */         out.append('\n');
/*  65:    */       }
/*  66: 83 */       out.append(p.getText());
/*  67:    */     }
/*  68: 85 */     return out.toString();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void clearText()
/*  72:    */   {
/*  73: 92 */     this._paragraphs.clear();
/*  74: 93 */     CTTextBody txBody = getTextBody(true);
/*  75: 94 */     txBody.setPArray(null);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public XSLFTextRun setText(String text)
/*  79:    */   {
/*  80:100 */     if (!this._paragraphs.isEmpty())
/*  81:    */     {
/*  82:101 */       CTTextBody txBody = getTextBody(false);
/*  83:102 */       int cntPs = txBody.sizeOfPArray();
/*  84:103 */       for (int i = cntPs; i > 1; i--)
/*  85:    */       {
/*  86:104 */         txBody.removeP(i - 1);
/*  87:105 */         this._paragraphs.remove(i - 1);
/*  88:    */       }
/*  89:108 */       ((XSLFTextParagraph)this._paragraphs.get(0)).clearButKeepProperties();
/*  90:    */     }
/*  91:111 */     return appendText(text, false);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public XSLFTextRun appendText(String text, boolean newParagraph)
/*  95:    */   {
/*  96:116 */     if (text == null) {
/*  97:117 */       return null;
/*  98:    */     }
/*  99:121 */     CTTextParagraphProperties otherPPr = null;
/* 100:122 */     CTTextCharacterProperties otherRPr = null;
/* 101:    */     XSLFTextParagraph para;
/* 102:    */     boolean firstPara;
/* 103:    */     XSLFTextParagraph para;
/* 104:126 */     if (this._paragraphs.isEmpty())
/* 105:    */     {
/* 106:127 */       boolean firstPara = false;
/* 107:128 */       para = null;
/* 108:    */     }
/* 109:    */     else
/* 110:    */     {
/* 111:130 */       firstPara = !newParagraph;
/* 112:131 */       para = (XSLFTextParagraph)this._paragraphs.get(this._paragraphs.size() - 1);
/* 113:132 */       CTTextParagraph ctp = para.getXmlObject();
/* 114:133 */       otherPPr = ctp.getPPr();
/* 115:134 */       List<XSLFTextRun> runs = para.getTextRuns();
/* 116:135 */       if (!runs.isEmpty())
/* 117:    */       {
/* 118:136 */         XSLFTextRun r0 = (XSLFTextRun)runs.get(runs.size() - 1);
/* 119:137 */         otherRPr = r0.getRPr(false);
/* 120:138 */         if (otherRPr == null) {
/* 121:139 */           otherRPr = ctp.getEndParaRPr();
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:147 */     XSLFTextRun run = null;
/* 126:148 */     for (String lineTxt : text.split("\\r\\n?|\\n"))
/* 127:    */     {
/* 128:149 */       if (!firstPara)
/* 129:    */       {
/* 130:150 */         if (para != null)
/* 131:    */         {
/* 132:151 */           CTTextParagraph ctp = para.getXmlObject();
/* 133:152 */           CTTextCharacterProperties unexpectedRPr = ctp.getEndParaRPr();
/* 134:153 */           if ((unexpectedRPr != null) && (unexpectedRPr != otherRPr)) {
/* 135:154 */             ctp.unsetEndParaRPr();
/* 136:    */           }
/* 137:    */         }
/* 138:157 */         para = addNewTextParagraph();
/* 139:158 */         if (otherPPr != null) {
/* 140:159 */           para.getXmlObject().setPPr(otherPPr);
/* 141:    */         }
/* 142:    */       }
/* 143:162 */       boolean firstRun = true;
/* 144:163 */       for (String runText : lineTxt.split("[\013]"))
/* 145:    */       {
/* 146:164 */         if (!firstRun) {
/* 147:165 */           para.addLineBreak();
/* 148:    */         }
/* 149:167 */         run = para.addNewTextRun();
/* 150:168 */         run.setText(runText);
/* 151:169 */         if (otherRPr != null) {
/* 152:170 */           run.getRPr(true).set(otherRPr);
/* 153:    */         }
/* 154:172 */         firstRun = false;
/* 155:    */       }
/* 156:174 */       firstPara = false;
/* 157:    */     }
/* 158:177 */     assert (run != null);
/* 159:178 */     return run;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public List<XSLFTextParagraph> getTextParagraphs()
/* 163:    */   {
/* 164:183 */     return this._paragraphs;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public XSLFTextParagraph addNewTextParagraph()
/* 168:    */   {
/* 169:192 */     CTTextBody txBody = getTextBody(false);
/* 170:    */     CTTextParagraph p;
/* 171:194 */     if (txBody == null)
/* 172:    */     {
/* 173:195 */       txBody = getTextBody(true);
/* 174:196 */       CTTextParagraph p = txBody.getPArray(0);
/* 175:197 */       p.removeR(0);
/* 176:    */     }
/* 177:    */     else
/* 178:    */     {
/* 179:199 */       p = txBody.addNewP();
/* 180:    */     }
/* 181:201 */     XSLFTextParagraph paragraph = newTextParagraph(p);
/* 182:202 */     this._paragraphs.add(paragraph);
/* 183:203 */     return paragraph;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setVerticalAlignment(VerticalAlignment anchor)
/* 187:    */   {
/* 188:208 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 189:209 */     if (bodyPr != null) {
/* 190:210 */       if (anchor == null)
/* 191:    */       {
/* 192:211 */         if (bodyPr.isSetAnchor()) {
/* 193:212 */           bodyPr.unsetAnchor();
/* 194:    */         }
/* 195:    */       }
/* 196:    */       else {
/* 197:215 */         bodyPr.setAnchor(STTextAnchoringType.Enum.forInt(anchor.ordinal() + 1));
/* 198:    */       }
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   public VerticalAlignment getVerticalAlignment()
/* 203:    */   {
/* 204:222 */     PropertyFetcher<VerticalAlignment> fetcher = new TextBodyPropertyFetcher()
/* 205:    */     {
/* 206:    */       public boolean fetch(CTTextBodyProperties props)
/* 207:    */       {
/* 208:225 */         if (props.isSetAnchor())
/* 209:    */         {
/* 210:226 */           int val = props.getAnchor().intValue();
/* 211:227 */           setValue(VerticalAlignment.values()[(val - 1)]);
/* 212:228 */           return true;
/* 213:    */         }
/* 214:230 */         return false;
/* 215:    */       }
/* 216:232 */     };
/* 217:233 */     fetchShapeProperty(fetcher);
/* 218:234 */     return fetcher.getValue() == null ? VerticalAlignment.TOP : (VerticalAlignment)fetcher.getValue();
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void setHorizontalCentered(Boolean isCentered)
/* 222:    */   {
/* 223:239 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 224:240 */     if (bodyPr != null) {
/* 225:241 */       if (isCentered == null)
/* 226:    */       {
/* 227:242 */         if (bodyPr.isSetAnchorCtr()) {
/* 228:243 */           bodyPr.unsetAnchorCtr();
/* 229:    */         }
/* 230:    */       }
/* 231:    */       else {
/* 232:246 */         bodyPr.setAnchorCtr(isCentered.booleanValue());
/* 233:    */       }
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   public boolean isHorizontalCentered()
/* 238:    */   {
/* 239:253 */     PropertyFetcher<Boolean> fetcher = new TextBodyPropertyFetcher()
/* 240:    */     {
/* 241:    */       public boolean fetch(CTTextBodyProperties props)
/* 242:    */       {
/* 243:256 */         if (props.isSetAnchorCtr())
/* 244:    */         {
/* 245:257 */           setValue(Boolean.valueOf(props.getAnchorCtr()));
/* 246:258 */           return true;
/* 247:    */         }
/* 248:260 */         return false;
/* 249:    */       }
/* 250:262 */     };
/* 251:263 */     fetchShapeProperty(fetcher);
/* 252:264 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setTextDirection(TextShape.TextDirection orientation)
/* 256:    */   {
/* 257:269 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 258:270 */     if (bodyPr != null) {
/* 259:271 */       if (orientation == null)
/* 260:    */       {
/* 261:272 */         if (bodyPr.isSetVert()) {
/* 262:273 */           bodyPr.unsetVert();
/* 263:    */         }
/* 264:    */       }
/* 265:    */       else {
/* 266:276 */         bodyPr.setVert(STTextVerticalType.Enum.forInt(orientation.ordinal() + 1));
/* 267:    */       }
/* 268:    */     }
/* 269:    */   }
/* 270:    */   
/* 271:    */   public TextShape.TextDirection getTextDirection()
/* 272:    */   {
/* 273:283 */     CTTextBodyProperties bodyPr = getTextBodyPr();
/* 274:284 */     if (bodyPr != null)
/* 275:    */     {
/* 276:285 */       STTextVerticalType.Enum val = bodyPr.getVert();
/* 277:286 */       if (val != null)
/* 278:    */       {
/* 279:287 */         switch (val.intValue())
/* 280:    */         {
/* 281:    */         case 1: 
/* 282:    */         default: 
/* 283:290 */           return TextShape.TextDirection.HORIZONTAL;
/* 284:    */         case 2: 
/* 285:    */         case 5: 
/* 286:    */         case 6: 
/* 287:294 */           return TextShape.TextDirection.VERTICAL;
/* 288:    */         case 3: 
/* 289:296 */           return TextShape.TextDirection.VERTICAL_270;
/* 290:    */         }
/* 291:299 */         return TextShape.TextDirection.STACKED;
/* 292:    */       }
/* 293:    */     }
/* 294:303 */     return TextShape.TextDirection.HORIZONTAL;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public Double getTextRotation()
/* 298:    */   {
/* 299:308 */     CTTextBodyProperties bodyPr = getTextBodyPr();
/* 300:309 */     if ((bodyPr != null) && (bodyPr.isSetRot())) {
/* 301:310 */       return Double.valueOf(bodyPr.getRot() / 60000.0D);
/* 302:    */     }
/* 303:312 */     return null;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public void setTextRotation(Double rotation)
/* 307:    */   {
/* 308:317 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 309:318 */     if (bodyPr != null) {
/* 310:319 */       bodyPr.setRot((int)(rotation.doubleValue() * 60000.0D));
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   public double getBottomInset()
/* 315:    */   {
/* 316:331 */     PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher()
/* 317:    */     {
/* 318:    */       public boolean fetch(CTTextBodyProperties props)
/* 319:    */       {
/* 320:334 */         if (props.isSetBIns())
/* 321:    */         {
/* 322:335 */           double val = Units.toPoints(props.getBIns());
/* 323:336 */           setValue(Double.valueOf(val));
/* 324:337 */           return true;
/* 325:    */         }
/* 326:339 */         return false;
/* 327:    */       }
/* 328:341 */     };
/* 329:342 */     fetchShapeProperty(fetcher);
/* 330:    */     
/* 331:344 */     return fetcher.getValue() == null ? 3.6D : ((Double)fetcher.getValue()).doubleValue();
/* 332:    */   }
/* 333:    */   
/* 334:    */   public double getLeftInset()
/* 335:    */   {
/* 336:355 */     PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher()
/* 337:    */     {
/* 338:    */       public boolean fetch(CTTextBodyProperties props)
/* 339:    */       {
/* 340:358 */         if (props.isSetLIns())
/* 341:    */         {
/* 342:359 */           double val = Units.toPoints(props.getLIns());
/* 343:360 */           setValue(Double.valueOf(val));
/* 344:361 */           return true;
/* 345:    */         }
/* 346:363 */         return false;
/* 347:    */       }
/* 348:365 */     };
/* 349:366 */     fetchShapeProperty(fetcher);
/* 350:    */     
/* 351:368 */     return fetcher.getValue() == null ? 7.2D : ((Double)fetcher.getValue()).doubleValue();
/* 352:    */   }
/* 353:    */   
/* 354:    */   public double getRightInset()
/* 355:    */   {
/* 356:379 */     PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher()
/* 357:    */     {
/* 358:    */       public boolean fetch(CTTextBodyProperties props)
/* 359:    */       {
/* 360:382 */         if (props.isSetRIns())
/* 361:    */         {
/* 362:383 */           double val = Units.toPoints(props.getRIns());
/* 363:384 */           setValue(Double.valueOf(val));
/* 364:385 */           return true;
/* 365:    */         }
/* 366:387 */         return false;
/* 367:    */       }
/* 368:389 */     };
/* 369:390 */     fetchShapeProperty(fetcher);
/* 370:    */     
/* 371:392 */     return fetcher.getValue() == null ? 7.2D : ((Double)fetcher.getValue()).doubleValue();
/* 372:    */   }
/* 373:    */   
/* 374:    */   public double getTopInset()
/* 375:    */   {
/* 376:402 */     PropertyFetcher<Double> fetcher = new TextBodyPropertyFetcher()
/* 377:    */     {
/* 378:    */       public boolean fetch(CTTextBodyProperties props)
/* 379:    */       {
/* 380:405 */         if (props.isSetTIns())
/* 381:    */         {
/* 382:406 */           double val = Units.toPoints(props.getTIns());
/* 383:407 */           setValue(Double.valueOf(val));
/* 384:408 */           return true;
/* 385:    */         }
/* 386:410 */         return false;
/* 387:    */       }
/* 388:412 */     };
/* 389:413 */     fetchShapeProperty(fetcher);
/* 390:    */     
/* 391:415 */     return fetcher.getValue() == null ? 3.6D : ((Double)fetcher.getValue()).doubleValue();
/* 392:    */   }
/* 393:    */   
/* 394:    */   public void setBottomInset(double margin)
/* 395:    */   {
/* 396:425 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 397:426 */     if (bodyPr != null) {
/* 398:427 */       if (margin == -1.0D) {
/* 399:428 */         bodyPr.unsetBIns();
/* 400:    */       } else {
/* 401:430 */         bodyPr.setBIns(Units.toEMU(margin));
/* 402:    */       }
/* 403:    */     }
/* 404:    */   }
/* 405:    */   
/* 406:    */   public void setLeftInset(double margin)
/* 407:    */   {
/* 408:442 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 409:443 */     if (bodyPr != null) {
/* 410:444 */       if (margin == -1.0D) {
/* 411:445 */         bodyPr.unsetLIns();
/* 412:    */       } else {
/* 413:447 */         bodyPr.setLIns(Units.toEMU(margin));
/* 414:    */       }
/* 415:    */     }
/* 416:    */   }
/* 417:    */   
/* 418:    */   public void setRightInset(double margin)
/* 419:    */   {
/* 420:459 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 421:460 */     if (bodyPr != null) {
/* 422:461 */       if (margin == -1.0D) {
/* 423:462 */         bodyPr.unsetRIns();
/* 424:    */       } else {
/* 425:464 */         bodyPr.setRIns(Units.toEMU(margin));
/* 426:    */       }
/* 427:    */     }
/* 428:    */   }
/* 429:    */   
/* 430:    */   public void setTopInset(double margin)
/* 431:    */   {
/* 432:476 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 433:477 */     if (bodyPr != null) {
/* 434:478 */       if (margin == -1.0D) {
/* 435:479 */         bodyPr.unsetTIns();
/* 436:    */       } else {
/* 437:481 */         bodyPr.setTIns(Units.toEMU(margin));
/* 438:    */       }
/* 439:    */     }
/* 440:    */   }
/* 441:    */   
/* 442:    */   public Insets2D getInsets()
/* 443:    */   {
/* 444:488 */     Insets2D insets = new Insets2D(getTopInset(), getLeftInset(), getBottomInset(), getRightInset());
/* 445:489 */     return insets;
/* 446:    */   }
/* 447:    */   
/* 448:    */   public void setInsets(Insets2D insets)
/* 449:    */   {
/* 450:494 */     setTopInset(insets.top);
/* 451:495 */     setLeftInset(insets.left);
/* 452:496 */     setBottomInset(insets.bottom);
/* 453:497 */     setRightInset(insets.right);
/* 454:    */   }
/* 455:    */   
/* 456:    */   public boolean getWordWrap()
/* 457:    */   {
/* 458:502 */     PropertyFetcher<Boolean> fetcher = new TextBodyPropertyFetcher()
/* 459:    */     {
/* 460:    */       public boolean fetch(CTTextBodyProperties props)
/* 461:    */       {
/* 462:505 */         if (props.isSetWrap())
/* 463:    */         {
/* 464:506 */           setValue(Boolean.valueOf(props.getWrap() == STTextWrappingType.SQUARE));
/* 465:507 */           return true;
/* 466:    */         }
/* 467:509 */         return false;
/* 468:    */       }
/* 469:511 */     };
/* 470:512 */     fetchShapeProperty(fetcher);
/* 471:513 */     return fetcher.getValue() == null ? true : ((Boolean)fetcher.getValue()).booleanValue();
/* 472:    */   }
/* 473:    */   
/* 474:    */   public void setWordWrap(boolean wrap)
/* 475:    */   {
/* 476:518 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 477:519 */     if (bodyPr != null) {
/* 478:520 */       bodyPr.setWrap(wrap ? STTextWrappingType.SQUARE : STTextWrappingType.NONE);
/* 479:    */     }
/* 480:    */   }
/* 481:    */   
/* 482:    */   public void setTextAutofit(TextShape.TextAutofit value)
/* 483:    */   {
/* 484:532 */     CTTextBodyProperties bodyPr = getTextBodyPr(true);
/* 485:533 */     if (bodyPr != null)
/* 486:    */     {
/* 487:534 */       if (bodyPr.isSetSpAutoFit()) {
/* 488:535 */         bodyPr.unsetSpAutoFit();
/* 489:    */       }
/* 490:537 */       if (bodyPr.isSetNoAutofit()) {
/* 491:538 */         bodyPr.unsetNoAutofit();
/* 492:    */       }
/* 493:540 */       if (bodyPr.isSetNormAutofit()) {
/* 494:541 */         bodyPr.unsetNormAutofit();
/* 495:    */       }
/* 496:544 */       switch (8.$SwitchMap$org$apache$poi$sl$usermodel$TextShape$TextAutofit[value.ordinal()])
/* 497:    */       {
/* 498:    */       case 1: 
/* 499:545 */         bodyPr.addNewNoAutofit(); break;
/* 500:    */       case 2: 
/* 501:546 */         bodyPr.addNewNormAutofit(); break;
/* 502:    */       case 3: 
/* 503:547 */         bodyPr.addNewSpAutoFit();
/* 504:    */       }
/* 505:    */     }
/* 506:    */   }
/* 507:    */   
/* 508:    */   public TextShape.TextAutofit getTextAutofit()
/* 509:    */   {
/* 510:557 */     CTTextBodyProperties bodyPr = getTextBodyPr();
/* 511:558 */     if (bodyPr != null)
/* 512:    */     {
/* 513:559 */       if (bodyPr.isSetNoAutofit()) {
/* 514:560 */         return TextShape.TextAutofit.NONE;
/* 515:    */       }
/* 516:561 */       if (bodyPr.isSetNormAutofit()) {
/* 517:562 */         return TextShape.TextAutofit.NORMAL;
/* 518:    */       }
/* 519:563 */       if (bodyPr.isSetSpAutoFit()) {
/* 520:564 */         return TextShape.TextAutofit.SHAPE;
/* 521:    */       }
/* 522:    */     }
/* 523:567 */     return TextShape.TextAutofit.NORMAL;
/* 524:    */   }
/* 525:    */   
/* 526:    */   protected CTTextBodyProperties getTextBodyPr()
/* 527:    */   {
/* 528:571 */     return getTextBodyPr(false);
/* 529:    */   }
/* 530:    */   
/* 531:    */   protected CTTextBodyProperties getTextBodyPr(boolean create)
/* 532:    */   {
/* 533:575 */     CTTextBody textBody = getTextBody(create);
/* 534:576 */     if (textBody == null) {
/* 535:577 */       return null;
/* 536:    */     }
/* 537:579 */     CTTextBodyProperties textBodyPr = textBody.getBodyPr();
/* 538:580 */     if ((textBodyPr == null) && (create)) {
/* 539:581 */       textBodyPr = textBody.addNewBodyPr();
/* 540:    */     }
/* 541:583 */     return textBodyPr;
/* 542:    */   }
/* 543:    */   
/* 544:    */   protected abstract CTTextBody getTextBody(boolean paramBoolean);
/* 545:    */   
/* 546:    */   public void setPlaceholder(Placeholder placeholder)
/* 547:    */   {
/* 548:590 */     super.setPlaceholder(placeholder);
/* 549:    */   }
/* 550:    */   
/* 551:    */   public Placeholder getTextType()
/* 552:    */   {
/* 553:594 */     CTPlaceholder ph = getCTPlaceholder();
/* 554:595 */     if (ph == null) {
/* 555:596 */       return null;
/* 556:    */     }
/* 557:599 */     int val = ph.getType().intValue();
/* 558:600 */     return Placeholder.lookupOoxml(val);
/* 559:    */   }
/* 560:    */   
/* 561:    */   public double getTextHeight()
/* 562:    */   {
/* 563:605 */     return getTextHeight(null);
/* 564:    */   }
/* 565:    */   
/* 566:    */   public double getTextHeight(Graphics2D graphics)
/* 567:    */   {
/* 568:610 */     DrawFactory drawFact = DrawFactory.getInstance(graphics);
/* 569:611 */     DrawTextShape dts = drawFact.getDrawable(this);
/* 570:612 */     return dts.getTextHeight(graphics);
/* 571:    */   }
/* 572:    */   
/* 573:    */   public Rectangle2D resizeToFitText()
/* 574:    */   {
/* 575:617 */     return resizeToFitText(null);
/* 576:    */   }
/* 577:    */   
/* 578:    */   public Rectangle2D resizeToFitText(Graphics2D graphics)
/* 579:    */   {
/* 580:622 */     Rectangle2D anchor = getAnchor();
/* 581:624 */     if (anchor.getWidth() == 0.0D) {
/* 582:625 */       throw new POIXMLException("Anchor of the shape was not set.");
/* 583:    */     }
/* 584:627 */     double height = getTextHeight(graphics);
/* 585:628 */     height += 1.0D;
/* 586:    */     
/* 587:630 */     Insets2D insets = getInsets();
/* 588:631 */     anchor.setRect(anchor.getX(), anchor.getY(), anchor.getWidth(), height + insets.top + insets.bottom);
/* 589:632 */     setAnchor(anchor);
/* 590:    */     
/* 591:634 */     return anchor;
/* 592:    */   }
/* 593:    */   
/* 594:    */   void copy(XSLFShape other)
/* 595:    */   {
/* 596:640 */     super.copy(other);
/* 597:    */     
/* 598:642 */     XSLFTextShape otherTS = (XSLFTextShape)other;
/* 599:643 */     CTTextBody otherTB = otherTS.getTextBody(false);
/* 600:644 */     CTTextBody thisTB = getTextBody(true);
/* 601:645 */     if (otherTB == null) {
/* 602:646 */       return;
/* 603:    */     }
/* 604:649 */     thisTB.setBodyPr((CTTextBodyProperties)otherTB.getBodyPr().copy());
/* 605:651 */     if (thisTB.isSetLstStyle()) {
/* 606:652 */       thisTB.unsetLstStyle();
/* 607:    */     }
/* 608:654 */     if (otherTB.isSetLstStyle()) {
/* 609:655 */       thisTB.setLstStyle((CTTextListStyle)otherTB.getLstStyle().copy());
/* 610:    */     }
/* 611:658 */     boolean srcWordWrap = otherTS.getWordWrap();
/* 612:659 */     if (srcWordWrap != getWordWrap()) {
/* 613:660 */       setWordWrap(srcWordWrap);
/* 614:    */     }
/* 615:663 */     double leftInset = otherTS.getLeftInset();
/* 616:664 */     if (leftInset != getLeftInset()) {
/* 617:665 */       setLeftInset(leftInset);
/* 618:    */     }
/* 619:667 */     double rightInset = otherTS.getRightInset();
/* 620:668 */     if (rightInset != getRightInset()) {
/* 621:669 */       setRightInset(rightInset);
/* 622:    */     }
/* 623:671 */     double topInset = otherTS.getTopInset();
/* 624:672 */     if (topInset != getTopInset()) {
/* 625:673 */       setTopInset(topInset);
/* 626:    */     }
/* 627:675 */     double bottomInset = otherTS.getBottomInset();
/* 628:676 */     if (bottomInset != getBottomInset()) {
/* 629:677 */       setBottomInset(bottomInset);
/* 630:    */     }
/* 631:680 */     VerticalAlignment vAlign = otherTS.getVerticalAlignment();
/* 632:681 */     if (vAlign != getVerticalAlignment()) {
/* 633:682 */       setVerticalAlignment(vAlign);
/* 634:    */     }
/* 635:685 */     clearText();
/* 636:687 */     for (XSLFTextParagraph srcP : otherTS.getTextParagraphs())
/* 637:    */     {
/* 638:688 */       XSLFTextParagraph tgtP = addNewTextParagraph();
/* 639:689 */       tgtP.copy(srcP);
/* 640:    */     }
/* 641:    */   }
/* 642:    */   
/* 643:    */   public void setTextPlaceholder(TextShape.TextPlaceholder placeholder)
/* 644:    */   {
/* 645:695 */     switch (8.$SwitchMap$org$apache$poi$sl$usermodel$TextShape$TextPlaceholder[placeholder.ordinal()])
/* 646:    */     {
/* 647:    */     case 1: 
/* 648:    */     case 2: 
/* 649:    */     case 3: 
/* 650:    */     case 4: 
/* 651:    */     default: 
/* 652:701 */       setPlaceholder(Placeholder.BODY);
/* 653:702 */       break;
/* 654:    */     case 5: 
/* 655:704 */       setPlaceholder(Placeholder.TITLE);
/* 656:705 */       break;
/* 657:    */     case 6: 
/* 658:707 */       setPlaceholder(Placeholder.BODY);
/* 659:708 */       setHorizontalCentered(Boolean.valueOf(true));
/* 660:709 */       break;
/* 661:    */     case 7: 
/* 662:711 */       setPlaceholder(Placeholder.CENTERED_TITLE);
/* 663:712 */       break;
/* 664:    */     case 8: 
/* 665:714 */       setPlaceholder(Placeholder.CONTENT);
/* 666:    */     }
/* 667:    */   }
/* 668:    */   
/* 669:    */   public TextShape.TextPlaceholder getTextPlaceholder()
/* 670:    */   {
/* 671:721 */     Placeholder ph = getTextType();
/* 672:722 */     if (ph == null) {
/* 673:723 */       return TextShape.TextPlaceholder.BODY;
/* 674:    */     }
/* 675:725 */     switch (8.$SwitchMap$org$apache$poi$sl$usermodel$Placeholder[ph.ordinal()])
/* 676:    */     {
/* 677:    */     case 1: 
/* 678:726 */       return TextShape.TextPlaceholder.BODY;
/* 679:    */     case 2: 
/* 680:727 */       return TextShape.TextPlaceholder.TITLE;
/* 681:    */     case 3: 
/* 682:728 */       return TextShape.TextPlaceholder.CENTER_TITLE;
/* 683:    */     }
/* 684:730 */     return TextShape.TextPlaceholder.OTHER;
/* 685:    */   }
/* 686:    */   
/* 687:    */   protected XSLFTextParagraph newTextParagraph(CTTextParagraph p)
/* 688:    */   {
/* 689:744 */     return new XSLFTextParagraph(p, this);
/* 690:    */   }
/* 691:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTextShape
 * JD-Core Version:    0.7.0.1
 */