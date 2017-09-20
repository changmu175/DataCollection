/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.geom.Rectangle2D;
/*   5:    */ import org.apache.poi.sl.draw.DrawPaint;
/*   6:    */ import org.apache.poi.sl.usermodel.ColorStyle;
/*   7:    */ import org.apache.poi.sl.usermodel.PaintStyle;
/*   8:    */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*   9:    */ import org.apache.poi.sl.usermodel.StrokeStyle;
/*  10:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCap;
/*  11:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCompound;
/*  12:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
/*  13:    */ import org.apache.poi.sl.usermodel.TableCell;
/*  14:    */ import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
/*  15:    */ import org.apache.poi.sl.usermodel.TextShape.TextDirection;
/*  16:    */ import org.apache.poi.sl.usermodel.VerticalAlignment;
/*  17:    */ import org.apache.poi.util.Units;
/*  18:    */ import org.apache.xmlbeans.XmlObject;
/*  19:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference;
/*  20:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties;
/*  21:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
/*  22:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*  23:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*  24:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties;
/*  25:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  26:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*  27:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*  28:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
/*  29:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
/*  30:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell.Factory;
/*  31:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellProperties;
/*  32:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle;
/*  33:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties;
/*  34:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle;
/*  35:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle;
/*  36:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  37:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  38:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*  39:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D.Factory;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine;
/*  41:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine.Enum;
/*  42:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineCap;
/*  43:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineCap.Enum;
/*  44:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength;
/*  45:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType;
/*  46:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth;
/*  47:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum;
/*  48:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment;
/*  49:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPresetLineDashVal;
/*  50:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STPresetLineDashVal.Enum;
/*  51:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum;
/*  52:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;
/*  53:    */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum;
/*  54:    */ 
/*  55:    */ public class XSLFTableCell
/*  56:    */   extends XSLFTextShape
/*  57:    */   implements TableCell<XSLFShape, XSLFTextParagraph>
/*  58:    */ {
/*  59: 72 */   private CTTableCellProperties _tcPr = null;
/*  60:    */   private final XSLFTable table;
/*  61: 74 */   private int row = 0;
/*  62: 74 */   private int col = 0;
/*  63: 79 */   private Rectangle2D anchor = null;
/*  64:    */   
/*  65:    */   XSLFTableCell(CTTableCell cell, XSLFTable table)
/*  66:    */   {
/*  67: 82 */     super(cell, table.getSheet());
/*  68: 83 */     this.table = table;
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected CTTextBody getTextBody(boolean create)
/*  72:    */   {
/*  73: 88 */     CTTableCell cell = getCell();
/*  74: 89 */     CTTextBody txBody = cell.getTxBody();
/*  75: 90 */     if ((txBody == null) && (create))
/*  76:    */     {
/*  77: 91 */       txBody = cell.addNewTxBody();
/*  78: 92 */       XSLFAutoShape.initTextBody(txBody);
/*  79:    */     }
/*  80: 94 */     return txBody;
/*  81:    */   }
/*  82:    */   
/*  83:    */   static CTTableCell prototype()
/*  84:    */   {
/*  85: 98 */     CTTableCell cell = CTTableCell.Factory.newInstance();
/*  86: 99 */     CTTableCellProperties pr = cell.addNewTcPr();
/*  87:100 */     pr.addNewLnL().addNewNoFill();
/*  88:101 */     pr.addNewLnR().addNewNoFill();
/*  89:102 */     pr.addNewLnT().addNewNoFill();
/*  90:103 */     pr.addNewLnB().addNewNoFill();
/*  91:104 */     return cell;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected CTTableCellProperties getCellProperties(boolean create)
/*  95:    */   {
/*  96:108 */     if (this._tcPr == null)
/*  97:    */     {
/*  98:109 */       CTTableCell cell = getCell();
/*  99:110 */       this._tcPr = cell.getTcPr();
/* 100:111 */       if ((this._tcPr == null) && (create)) {
/* 101:112 */         this._tcPr = cell.addNewTcPr();
/* 102:    */       }
/* 103:    */     }
/* 104:115 */     return this._tcPr;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setLeftInset(double margin)
/* 108:    */   {
/* 109:120 */     CTTableCellProperties pr = getCellProperties(true);
/* 110:121 */     pr.setMarL(Units.toEMU(margin));
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setRightInset(double margin)
/* 114:    */   {
/* 115:126 */     CTTableCellProperties pr = getCellProperties(true);
/* 116:127 */     pr.setMarR(Units.toEMU(margin));
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setTopInset(double margin)
/* 120:    */   {
/* 121:132 */     CTTableCellProperties pr = getCellProperties(true);
/* 122:133 */     pr.setMarT(Units.toEMU(margin));
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setBottomInset(double margin)
/* 126:    */   {
/* 127:138 */     CTTableCellProperties pr = getCellProperties(true);
/* 128:139 */     pr.setMarB(Units.toEMU(margin));
/* 129:    */   }
/* 130:    */   
/* 131:    */   private CTLineProperties getCTLine(TableCell.BorderEdge edge, boolean create)
/* 132:    */   {
/* 133:143 */     if (edge == null) {
/* 134:144 */       throw new IllegalArgumentException("BorderEdge needs to be specified.");
/* 135:    */     }
/* 136:147 */     CTTableCellProperties pr = getCellProperties(create);
/* 137:148 */     if (pr == null) {
/* 138:149 */       return null;
/* 139:    */     }
/* 140:152 */     switch (2.$SwitchMap$org$apache$poi$sl$usermodel$TableCell$BorderEdge[edge.ordinal()])
/* 141:    */     {
/* 142:    */     case 1: 
/* 143:154 */       return create ? pr.addNewLnB() : pr.isSetLnB() ? pr.getLnB() : null;
/* 144:    */     case 2: 
/* 145:156 */       return create ? pr.addNewLnL() : pr.isSetLnL() ? pr.getLnL() : null;
/* 146:    */     case 3: 
/* 147:158 */       return create ? pr.addNewLnT() : pr.isSetLnT() ? pr.getLnT() : null;
/* 148:    */     case 4: 
/* 149:160 */       return create ? pr.addNewLnR() : pr.isSetLnR() ? pr.getLnR() : null;
/* 150:    */     }
/* 151:162 */     return null;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void removeBorder(TableCell.BorderEdge edge)
/* 155:    */   {
/* 156:168 */     CTTableCellProperties pr = getCellProperties(false);
/* 157:169 */     if (pr == null) {
/* 158:170 */       return;
/* 159:    */     }
/* 160:172 */     switch (2.$SwitchMap$org$apache$poi$sl$usermodel$TableCell$BorderEdge[edge.ordinal()])
/* 161:    */     {
/* 162:    */     case 1: 
/* 163:174 */       if (pr.isSetLnB()) {
/* 164:175 */         pr.unsetLnB();
/* 165:    */       }
/* 166:    */       break;
/* 167:    */     case 2: 
/* 168:179 */       if (pr.isSetLnL()) {
/* 169:180 */         pr.unsetLnL();
/* 170:    */       }
/* 171:    */       break;
/* 172:    */     case 3: 
/* 173:184 */       if (pr.isSetLnT()) {
/* 174:185 */         pr.unsetLnT();
/* 175:    */       }
/* 176:    */       break;
/* 177:    */     case 4: 
/* 178:189 */       if (pr.isSetLnR()) {
/* 179:190 */         pr.unsetLnB();
/* 180:    */       }
/* 181:    */       break;
/* 182:    */     default: 
/* 183:194 */       throw new IllegalArgumentException();
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public StrokeStyle getBorderStyle(final TableCell.BorderEdge edge)
/* 188:    */   {
/* 189:200 */     final Double width = getBorderWidth(edge);
/* 190:201 */     width == null ? null : new StrokeStyle()
/* 191:    */     {
/* 192:    */       public PaintStyle getPaint()
/* 193:    */       {
/* 194:204 */         return DrawPaint.createSolidPaint(XSLFTableCell.this.getBorderColor(edge));
/* 195:    */       }
/* 196:    */       
/* 197:    */       public StrokeStyle.LineCap getLineCap()
/* 198:    */       {
/* 199:209 */         return XSLFTableCell.this.getBorderCap(edge);
/* 200:    */       }
/* 201:    */       
/* 202:    */       public StrokeStyle.LineDash getLineDash()
/* 203:    */       {
/* 204:214 */         return XSLFTableCell.this.getBorderDash(edge);
/* 205:    */       }
/* 206:    */       
/* 207:    */       public StrokeStyle.LineCompound getLineCompound()
/* 208:    */       {
/* 209:219 */         return XSLFTableCell.this.getBorderCompound(edge);
/* 210:    */       }
/* 211:    */       
/* 212:    */       public double getLineWidth()
/* 213:    */       {
/* 214:224 */         return width.doubleValue();
/* 215:    */       }
/* 216:    */     };
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setBorderStyle(TableCell.BorderEdge edge, StrokeStyle style)
/* 220:    */   {
/* 221:231 */     if (style == null) {
/* 222:232 */       throw new IllegalArgumentException("StrokeStyle needs to be specified.");
/* 223:    */     }
/* 224:235 */     StrokeStyle.LineCap cap = style.getLineCap();
/* 225:236 */     if (cap != null) {
/* 226:237 */       setBorderCap(edge, cap);
/* 227:    */     }
/* 228:240 */     StrokeStyle.LineCompound compound = style.getLineCompound();
/* 229:241 */     if (compound != null) {
/* 230:242 */       setBorderCompound(edge, compound);
/* 231:    */     }
/* 232:245 */     StrokeStyle.LineDash dash = style.getLineDash();
/* 233:246 */     if (dash != null) {
/* 234:247 */       setBorderDash(edge, dash);
/* 235:    */     }
/* 236:250 */     double width = style.getLineWidth();
/* 237:251 */     setBorderWidth(edge, width);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public Double getBorderWidth(TableCell.BorderEdge edge)
/* 241:    */   {
/* 242:255 */     CTLineProperties ln = getCTLine(edge, false);
/* 243:256 */     return (ln == null) || (!ln.isSetW()) ? null : Double.valueOf(Units.toPoints(ln.getW()));
/* 244:    */   }
/* 245:    */   
/* 246:    */   public void setBorderWidth(TableCell.BorderEdge edge, double width)
/* 247:    */   {
/* 248:261 */     CTLineProperties ln = getCTLine(edge, true);
/* 249:262 */     ln.setW(Units.toEMU(width));
/* 250:    */   }
/* 251:    */   
/* 252:    */   private CTLineProperties setBorderDefaults(TableCell.BorderEdge edge)
/* 253:    */   {
/* 254:266 */     CTLineProperties ln = getCTLine(edge, true);
/* 255:267 */     if (ln.isSetNoFill()) {
/* 256:268 */       ln.unsetNoFill();
/* 257:    */     }
/* 258:271 */     if (!ln.isSetPrstDash()) {
/* 259:272 */       ln.addNewPrstDash().setVal(STPresetLineDashVal.SOLID);
/* 260:    */     }
/* 261:274 */     if (!ln.isSetCmpd()) {
/* 262:275 */       ln.setCmpd(STCompoundLine.SNG);
/* 263:    */     }
/* 264:277 */     if (!ln.isSetAlgn()) {
/* 265:278 */       ln.setAlgn(STPenAlignment.CTR);
/* 266:    */     }
/* 267:280 */     if (!ln.isSetCap()) {
/* 268:281 */       ln.setCap(STLineCap.FLAT);
/* 269:    */     }
/* 270:283 */     if (!ln.isSetRound()) {
/* 271:284 */       ln.addNewRound();
/* 272:    */     }
/* 273:287 */     if (!ln.isSetHeadEnd())
/* 274:    */     {
/* 275:288 */       CTLineEndProperties hd = ln.addNewHeadEnd();
/* 276:289 */       hd.setType(STLineEndType.NONE);
/* 277:290 */       hd.setW(STLineEndWidth.MED);
/* 278:291 */       hd.setLen(STLineEndLength.MED);
/* 279:    */     }
/* 280:294 */     if (!ln.isSetTailEnd())
/* 281:    */     {
/* 282:295 */       CTLineEndProperties tl = ln.addNewTailEnd();
/* 283:296 */       tl.setType(STLineEndType.NONE);
/* 284:297 */       tl.setW(STLineEndWidth.MED);
/* 285:298 */       tl.setLen(STLineEndLength.MED);
/* 286:    */     }
/* 287:301 */     return ln;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public void setBorderColor(TableCell.BorderEdge edge, Color color)
/* 291:    */   {
/* 292:306 */     if (color == null) {
/* 293:307 */       throw new IllegalArgumentException("Colors need to be specified.");
/* 294:    */     }
/* 295:310 */     CTLineProperties ln = setBorderDefaults(edge);
/* 296:311 */     CTSolidColorFillProperties fill = ln.addNewSolidFill();
/* 297:312 */     XSLFColor c = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/* 298:313 */     c.setColor(color);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public Color getBorderColor(TableCell.BorderEdge edge)
/* 302:    */   {
/* 303:317 */     CTLineProperties ln = getCTLine(edge, false);
/* 304:318 */     if ((ln == null) || (ln.isSetNoFill()) || (!ln.isSetSolidFill())) {
/* 305:319 */       return null;
/* 306:    */     }
/* 307:322 */     CTSolidColorFillProperties fill = ln.getSolidFill();
/* 308:323 */     XSLFColor c = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/* 309:324 */     return c.getColor();
/* 310:    */   }
/* 311:    */   
/* 312:    */   public StrokeStyle.LineCompound getBorderCompound(TableCell.BorderEdge edge)
/* 313:    */   {
/* 314:328 */     CTLineProperties ln = getCTLine(edge, false);
/* 315:329 */     if ((ln == null) || (ln.isSetNoFill()) || (!ln.isSetSolidFill()) || (!ln.isSetCmpd())) {
/* 316:330 */       return null;
/* 317:    */     }
/* 318:333 */     return StrokeStyle.LineCompound.fromOoxmlId(ln.getCmpd().intValue());
/* 319:    */   }
/* 320:    */   
/* 321:    */   public void setBorderCompound(TableCell.BorderEdge edge, StrokeStyle.LineCompound compound)
/* 322:    */   {
/* 323:338 */     if (compound == null) {
/* 324:339 */       throw new IllegalArgumentException("LineCompound need to be specified.");
/* 325:    */     }
/* 326:342 */     CTLineProperties ln = setBorderDefaults(edge);
/* 327:343 */     ln.setCmpd(STCompoundLine.Enum.forInt(compound.ooxmlId));
/* 328:    */   }
/* 329:    */   
/* 330:    */   public StrokeStyle.LineDash getBorderDash(TableCell.BorderEdge edge)
/* 331:    */   {
/* 332:347 */     CTLineProperties ln = getCTLine(edge, false);
/* 333:348 */     if ((ln == null) || (ln.isSetNoFill()) || (!ln.isSetSolidFill()) || (!ln.isSetPrstDash())) {
/* 334:349 */       return null;
/* 335:    */     }
/* 336:352 */     return StrokeStyle.LineDash.fromOoxmlId(ln.getPrstDash().getVal().intValue());
/* 337:    */   }
/* 338:    */   
/* 339:    */   public void setBorderDash(TableCell.BorderEdge edge, StrokeStyle.LineDash dash)
/* 340:    */   {
/* 341:357 */     if (dash == null) {
/* 342:358 */       throw new IllegalArgumentException("LineDash need to be specified.");
/* 343:    */     }
/* 344:361 */     CTLineProperties ln = setBorderDefaults(edge);
/* 345:362 */     ln.getPrstDash().setVal(STPresetLineDashVal.Enum.forInt(dash.ooxmlId));
/* 346:    */   }
/* 347:    */   
/* 348:    */   public StrokeStyle.LineCap getBorderCap(TableCell.BorderEdge edge)
/* 349:    */   {
/* 350:366 */     CTLineProperties ln = getCTLine(edge, false);
/* 351:367 */     if ((ln == null) || (ln.isSetNoFill()) || (!ln.isSetSolidFill()) || (!ln.isSetCap())) {
/* 352:368 */       return null;
/* 353:    */     }
/* 354:371 */     return StrokeStyle.LineCap.fromOoxmlId(ln.getCap().intValue());
/* 355:    */   }
/* 356:    */   
/* 357:    */   public void setBorderCap(TableCell.BorderEdge edge, StrokeStyle.LineCap cap)
/* 358:    */   {
/* 359:375 */     if (cap == null) {
/* 360:376 */       throw new IllegalArgumentException("LineCap need to be specified.");
/* 361:    */     }
/* 362:379 */     CTLineProperties ln = setBorderDefaults(edge);
/* 363:380 */     ln.setCap(STLineCap.Enum.forInt(cap.ooxmlId));
/* 364:    */   }
/* 365:    */   
/* 366:    */   public void setFillColor(Color color)
/* 367:    */   {
/* 368:393 */     CTTableCellProperties spPr = getCellProperties(true);
/* 369:394 */     if (color == null)
/* 370:    */     {
/* 371:395 */       if (spPr.isSetSolidFill()) {
/* 372:396 */         spPr.unsetSolidFill();
/* 373:    */       }
/* 374:    */     }
/* 375:    */     else
/* 376:    */     {
/* 377:399 */       CTSolidColorFillProperties fill = spPr.isSetSolidFill() ? spPr.getSolidFill() : spPr.addNewSolidFill();
/* 378:400 */       XSLFColor c = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/* 379:401 */       c.setColor(color);
/* 380:    */     }
/* 381:    */   }
/* 382:    */   
/* 383:    */   public Color getFillColor()
/* 384:    */   {
/* 385:411 */     PaintStyle ps = getFillPaint();
/* 386:412 */     if ((ps instanceof PaintStyle.SolidPaint))
/* 387:    */     {
/* 388:413 */       ColorStyle cs = ((PaintStyle.SolidPaint)ps).getSolidColor();
/* 389:414 */       return DrawPaint.applyColorTransform(cs);
/* 390:    */     }
/* 391:417 */     return null;
/* 392:    */   }
/* 393:    */   
/* 394:    */   public PaintStyle getFillPaint()
/* 395:    */   {
/* 396:423 */     XSLFSheet sheet = getSheet();
/* 397:424 */     XSLFTheme theme = sheet.getTheme();
/* 398:425 */     boolean hasPlaceholder = getPlaceholder() != null;
/* 399:426 */     XmlObject props = getCellProperties(false);
/* 400:427 */     XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(props);
/* 401:428 */     if (fp != null)
/* 402:    */     {
/* 403:429 */       PaintStyle paint = selectPaint(fp, null, sheet.getPackagePart(), theme, hasPlaceholder);
/* 404:430 */       if (paint != null) {
/* 405:431 */         return paint;
/* 406:    */       }
/* 407:    */     }
/* 408:435 */     CTTablePartStyle tps = getTablePartStyle(null);
/* 409:436 */     if ((tps == null) || (!tps.isSetTcStyle()))
/* 410:    */     {
/* 411:437 */       tps = getTablePartStyle(XSLFTableStyle.TablePartStyle.wholeTbl);
/* 412:438 */       if ((tps == null) || (!tps.isSetTcStyle())) {
/* 413:439 */         return null;
/* 414:    */       }
/* 415:    */     }
/* 416:443 */     XMLSlideShow slideShow = sheet.getSlideShow();
/* 417:444 */     CTTableStyleCellStyle tcStyle = tps.getTcStyle();
/* 418:445 */     if (tcStyle.isSetFill()) {
/* 419:446 */       props = tcStyle.getFill();
/* 420:447 */     } else if (tcStyle.isSetFillRef()) {
/* 421:448 */       props = tcStyle.getFillRef();
/* 422:    */     } else {
/* 423:450 */       return null;
/* 424:    */     }
/* 425:453 */     fp = XSLFPropertiesDelegate.getFillDelegate(props);
/* 426:454 */     if (fp != null)
/* 427:    */     {
/* 428:455 */       PaintStyle paint = XSLFShape.selectPaint(fp, null, slideShow.getPackagePart(), theme, hasPlaceholder);
/* 429:456 */       if (paint != null) {
/* 430:457 */         return paint;
/* 431:    */       }
/* 432:    */     }
/* 433:461 */     return null;
/* 434:    */   }
/* 435:    */   
/* 436:    */   private CTTablePartStyle getTablePartStyle(XSLFTableStyle.TablePartStyle tablePartStyle)
/* 437:    */   {
/* 438:472 */     CTTable ct = this.table.getCTTable();
/* 439:473 */     if (!ct.isSetTblPr()) {
/* 440:474 */       return null;
/* 441:    */     }
/* 442:477 */     CTTableProperties pr = ct.getTblPr();
/* 443:478 */     boolean bandRow = (pr.isSetBandRow()) && (pr.getBandRow());
/* 444:479 */     boolean firstRow = (pr.isSetFirstRow()) && (pr.getFirstRow());
/* 445:480 */     boolean lastRow = (pr.isSetLastRow()) && (pr.getLastRow());
/* 446:481 */     boolean bandCol = (pr.isSetBandCol()) && (pr.getBandCol());
/* 447:482 */     boolean firstCol = (pr.isSetFirstCol()) && (pr.getFirstCol());
/* 448:483 */     boolean lastCol = (pr.isSetLastCol()) && (pr.getLastCol());
/* 449:    */     XSLFTableStyle.TablePartStyle tps;
/* 450:    */     XSLFTableStyle.TablePartStyle tps;
/* 451:486 */     if (tablePartStyle != null)
/* 452:    */     {
/* 453:487 */       tps = tablePartStyle;
/* 454:    */     }
/* 455:    */     else
/* 456:    */     {
/* 457:    */       XSLFTableStyle.TablePartStyle tps;
/* 458:488 */       if ((this.row == 0) && (firstRow))
/* 459:    */       {
/* 460:489 */         tps = XSLFTableStyle.TablePartStyle.firstRow;
/* 461:    */       }
/* 462:    */       else
/* 463:    */       {
/* 464:    */         XSLFTableStyle.TablePartStyle tps;
/* 465:490 */         if ((this.row == this.table.getNumberOfRows() - 1) && (lastRow))
/* 466:    */         {
/* 467:491 */           tps = XSLFTableStyle.TablePartStyle.lastRow;
/* 468:    */         }
/* 469:    */         else
/* 470:    */         {
/* 471:    */           XSLFTableStyle.TablePartStyle tps;
/* 472:492 */           if ((this.col == 0) && (firstCol))
/* 473:    */           {
/* 474:493 */             tps = XSLFTableStyle.TablePartStyle.firstCol;
/* 475:    */           }
/* 476:    */           else
/* 477:    */           {
/* 478:    */             XSLFTableStyle.TablePartStyle tps;
/* 479:494 */             if ((this.col == this.table.getNumberOfColumns() - 1) && (lastCol))
/* 480:    */             {
/* 481:495 */               tps = XSLFTableStyle.TablePartStyle.lastCol;
/* 482:    */             }
/* 483:    */             else
/* 484:    */             {
/* 485:497 */               tps = XSLFTableStyle.TablePartStyle.wholeTbl;
/* 486:    */               
/* 487:499 */               int br = this.row + (firstRow ? 1 : 0);
/* 488:500 */               int bc = this.col + (firstCol ? 1 : 0);
/* 489:501 */               if ((bandRow) && ((br & 0x1) == 0)) {
/* 490:502 */                 tps = XSLFTableStyle.TablePartStyle.band1H;
/* 491:503 */               } else if ((bandCol) && ((bc & 0x1) == 0)) {
/* 492:504 */                 tps = XSLFTableStyle.TablePartStyle.band1V;
/* 493:    */               }
/* 494:    */             }
/* 495:    */           }
/* 496:    */         }
/* 497:    */       }
/* 498:    */     }
/* 499:508 */     XSLFTableStyle tabStyle = this.table.getTableStyle();
/* 500:509 */     if (tabStyle == null) {
/* 501:510 */       return null;
/* 502:    */     }
/* 503:513 */     CTTablePartStyle part = tabStyle.getTablePartStyle(tps);
/* 504:514 */     return part == null ? tabStyle.getTablePartStyle(XSLFTableStyle.TablePartStyle.wholeTbl) : part;
/* 505:    */   }
/* 506:    */   
/* 507:    */   void setGridSpan(int gridSpan_)
/* 508:    */   {
/* 509:518 */     getCell().setGridSpan(gridSpan_);
/* 510:    */   }
/* 511:    */   
/* 512:    */   public int getGridSpan()
/* 513:    */   {
/* 514:523 */     CTTableCell c = getCell();
/* 515:524 */     return c.isSetGridSpan() ? c.getGridSpan() : 1;
/* 516:    */   }
/* 517:    */   
/* 518:    */   void setRowSpan(int rowSpan_)
/* 519:    */   {
/* 520:528 */     getCell().setRowSpan(rowSpan_);
/* 521:    */   }
/* 522:    */   
/* 523:    */   public int getRowSpan()
/* 524:    */   {
/* 525:533 */     CTTableCell c = getCell();
/* 526:534 */     return c.isSetRowSpan() ? c.getRowSpan() : 1;
/* 527:    */   }
/* 528:    */   
/* 529:    */   void setHMerge(boolean merge_)
/* 530:    */   {
/* 531:538 */     getCell().setHMerge(merge_);
/* 532:    */   }
/* 533:    */   
/* 534:    */   void setVMerge(boolean merge_)
/* 535:    */   {
/* 536:542 */     getCell().setVMerge(merge_);
/* 537:    */   }
/* 538:    */   
/* 539:    */   public void setVerticalAlignment(VerticalAlignment anchor)
/* 540:    */   {
/* 541:547 */     CTTableCellProperties cellProps = getCellProperties(true);
/* 542:548 */     if (anchor == null)
/* 543:    */     {
/* 544:549 */       if (cellProps.isSetAnchor()) {
/* 545:550 */         cellProps.unsetAnchor();
/* 546:    */       }
/* 547:    */     }
/* 548:    */     else {
/* 549:553 */       cellProps.setAnchor(STTextAnchoringType.Enum.forInt(anchor.ordinal() + 1));
/* 550:    */     }
/* 551:    */   }
/* 552:    */   
/* 553:    */   public VerticalAlignment getVerticalAlignment()
/* 554:    */   {
/* 555:559 */     CTTableCellProperties cellProps = getCellProperties(false);
/* 556:    */     
/* 557:561 */     VerticalAlignment align = VerticalAlignment.TOP;
/* 558:562 */     if ((cellProps != null) && (cellProps.isSetAnchor()))
/* 559:    */     {
/* 560:563 */       int ival = cellProps.getAnchor().intValue();
/* 561:564 */       align = VerticalAlignment.values()[(ival - 1)];
/* 562:    */     }
/* 563:566 */     return align;
/* 564:    */   }
/* 565:    */   
/* 566:    */   public void setTextDirection(TextShape.TextDirection orientation)
/* 567:    */   {
/* 568:574 */     CTTableCellProperties cellProps = getCellProperties(true);
/* 569:575 */     if (orientation == null)
/* 570:    */     {
/* 571:576 */       if (cellProps.isSetVert()) {
/* 572:577 */         cellProps.unsetVert();
/* 573:    */       }
/* 574:    */     }
/* 575:    */     else
/* 576:    */     {
/* 577:    */       STTextVerticalType.Enum vt;
/* 578:581 */       switch (2.$SwitchMap$org$apache$poi$sl$usermodel$TextShape$TextDirection[orientation.ordinal()])
/* 579:    */       {
/* 580:    */       case 1: 
/* 581:    */       default: 
/* 582:584 */         vt = STTextVerticalType.HORZ;
/* 583:585 */         break;
/* 584:    */       case 2: 
/* 585:587 */         vt = STTextVerticalType.VERT;
/* 586:588 */         break;
/* 587:    */       case 3: 
/* 588:590 */         vt = STTextVerticalType.VERT_270;
/* 589:591 */         break;
/* 590:    */       case 4: 
/* 591:593 */         vt = STTextVerticalType.WORD_ART_VERT;
/* 592:    */       }
/* 593:597 */       cellProps.setVert(vt);
/* 594:    */     }
/* 595:    */   }
/* 596:    */   
/* 597:    */   public TextShape.TextDirection getTextDirection()
/* 598:    */   {
/* 599:606 */     CTTableCellProperties cellProps = getCellProperties(false);
/* 600:    */     STTextVerticalType.Enum orientation;
/* 601:    */     STTextVerticalType.Enum orientation;
/* 602:609 */     if ((cellProps != null) && (cellProps.isSetVert())) {
/* 603:610 */       orientation = cellProps.getVert();
/* 604:    */     } else {
/* 605:612 */       orientation = STTextVerticalType.HORZ;
/* 606:    */     }
/* 607:615 */     switch (orientation.intValue())
/* 608:    */     {
/* 609:    */     case 1: 
/* 610:    */     default: 
/* 611:618 */       return TextShape.TextDirection.HORIZONTAL;
/* 612:    */     case 2: 
/* 613:    */     case 5: 
/* 614:    */     case 6: 
/* 615:622 */       return TextShape.TextDirection.VERTICAL;
/* 616:    */     case 3: 
/* 617:624 */       return TextShape.TextDirection.VERTICAL_270;
/* 618:    */     }
/* 619:627 */     return TextShape.TextDirection.STACKED;
/* 620:    */   }
/* 621:    */   
/* 622:    */   private CTTableCell getCell()
/* 623:    */   {
/* 624:632 */     return (CTTableCell)getXmlObject();
/* 625:    */   }
/* 626:    */   
/* 627:    */   void setRowColIndex(int row, int col)
/* 628:    */   {
/* 629:636 */     this.row = row;
/* 630:637 */     this.col = col;
/* 631:    */   }
/* 632:    */   
/* 633:    */   protected CTTransform2D getXfrm()
/* 634:    */   {
/* 635:644 */     Rectangle2D anc = getAnchor();
/* 636:645 */     CTTransform2D xfrm = CTTransform2D.Factory.newInstance();
/* 637:646 */     CTPoint2D off = xfrm.addNewOff();
/* 638:647 */     off.setX(Units.toEMU(anc.getX()));
/* 639:648 */     off.setY(Units.toEMU(anc.getY()));
/* 640:649 */     CTPositiveSize2D size = xfrm.addNewExt();
/* 641:650 */     size.setCx(Units.toEMU(anc.getWidth()));
/* 642:651 */     size.setCy(Units.toEMU(anc.getHeight()));
/* 643:652 */     return xfrm;
/* 644:    */   }
/* 645:    */   
/* 646:    */   public void setAnchor(Rectangle2D anchor)
/* 647:    */   {
/* 648:663 */     if (this.anchor == null) {
/* 649:664 */       this.anchor = ((Rectangle2D)anchor.clone());
/* 650:    */     } else {
/* 651:666 */       this.anchor.setRect(anchor);
/* 652:    */     }
/* 653:    */   }
/* 654:    */   
/* 655:    */   public Rectangle2D getAnchor()
/* 656:    */   {
/* 657:675 */     if (this.anchor == null) {
/* 658:676 */       this.table.updateCellAnchor();
/* 659:    */     }
/* 660:679 */     assert (this.anchor != null);
/* 661:680 */     return this.anchor;
/* 662:    */   }
/* 663:    */   
/* 664:    */   public boolean isMerged()
/* 665:    */   {
/* 666:688 */     CTTableCell c = getCell();
/* 667:689 */     return ((c.isSetHMerge()) && (c.getHMerge())) || ((c.isSetVMerge()) && (c.getVMerge()));
/* 668:    */   }
/* 669:    */   
/* 670:    */   protected XSLFCellTextParagraph newTextParagraph(CTTextParagraph p)
/* 671:    */   {
/* 672:697 */     return new XSLFCellTextParagraph(p, this);
/* 673:    */   }
/* 674:    */   
/* 675:    */   protected XmlObject getShapeProperties()
/* 676:    */   {
/* 677:702 */     return getCellProperties(false);
/* 678:    */   }
/* 679:    */   
/* 680:    */   private class XSLFCellTextParagraph
/* 681:    */     extends XSLFTextParagraph
/* 682:    */   {
/* 683:    */     protected XSLFCellTextParagraph(CTTextParagraph p, XSLFTextShape shape)
/* 684:    */     {
/* 685:710 */       super(shape);
/* 686:    */     }
/* 687:    */     
/* 688:    */     protected XSLFCellTextRun newTextRun(CTRegularTextRun r)
/* 689:    */     {
/* 690:715 */       return new XSLFCellTextRun(XSLFTableCell.this, r, this);
/* 691:    */     }
/* 692:    */   }
/* 693:    */   
/* 694:    */   private class XSLFCellTextRun
/* 695:    */     extends XSLFTextRun
/* 696:    */   {
/* 697:    */     protected XSLFCellTextRun(CTRegularTextRun r, XSLFTextParagraph p)
/* 698:    */     {
/* 699:724 */       super(p);
/* 700:    */     }
/* 701:    */     
/* 702:    */     public PaintStyle getFontColor()
/* 703:    */     {
/* 704:729 */       CTTableStyleTextStyle txStyle = getTextStyle();
/* 705:730 */       if (txStyle == null) {
/* 706:731 */         return super.getFontColor();
/* 707:    */       }
/* 708:734 */       CTSchemeColor phClr = null;
/* 709:735 */       CTFontReference fontRef = txStyle.getFontRef();
/* 710:736 */       if (fontRef != null) {
/* 711:737 */         phClr = fontRef.getSchemeClr();
/* 712:    */       }
/* 713:740 */       XSLFTheme theme = XSLFTableCell.this.getSheet().getTheme();
/* 714:741 */       XSLFColor c = new XSLFColor(txStyle, theme, phClr);
/* 715:742 */       return DrawPaint.createSolidPaint(c.getColorStyle());
/* 716:    */     }
/* 717:    */     
/* 718:    */     public boolean isBold()
/* 719:    */     {
/* 720:747 */       CTTableStyleTextStyle txStyle = getTextStyle();
/* 721:748 */       if (txStyle == null) {
/* 722:749 */         return super.isBold();
/* 723:    */       }
/* 724:751 */       return (txStyle.isSetB()) && (txStyle.getB().intValue() == 1);
/* 725:    */     }
/* 726:    */     
/* 727:    */     public boolean isItalic()
/* 728:    */     {
/* 729:757 */       CTTableStyleTextStyle txStyle = getTextStyle();
/* 730:758 */       if (txStyle == null) {
/* 731:759 */         return super.isItalic();
/* 732:    */       }
/* 733:761 */       return (txStyle.isSetI()) && (txStyle.getI().intValue() == 1);
/* 734:    */     }
/* 735:    */     
/* 736:    */     private CTTableStyleTextStyle getTextStyle()
/* 737:    */     {
/* 738:766 */       CTTablePartStyle tps = XSLFTableCell.this.getTablePartStyle(null);
/* 739:767 */       if ((tps == null) || (!tps.isSetTcTxStyle())) {
/* 740:768 */         tps = XSLFTableCell.this.getTablePartStyle(XSLFTableStyle.TablePartStyle.wholeTbl);
/* 741:    */       }
/* 742:770 */       return tps == null ? null : tps.getTcTxStyle();
/* 743:    */     }
/* 744:    */   }
/* 745:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTableCell

 * JD-Core Version:    0.7.0.1

 */