/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.EnumMap;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.POIXMLDocumentPart;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
/*  11:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
/*  12:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  13:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*  14:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*  15:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  16:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
/*  25:    */ 
/*  26:    */ public class XWPFTable
/*  27:    */   implements IBodyElement, ISDTContents
/*  28:    */ {
/*  29: 53 */   private static EnumMap<XWPFBorderType, STBorder.Enum> xwpfBorderTypeMap = new EnumMap(XWPFBorderType.class);
/*  30:    */   private static HashMap<Integer, XWPFBorderType> stBorderTypeMap;
/*  31:    */   
/*  32:    */   static
/*  33:    */   {
/*  34: 54 */     xwpfBorderTypeMap.put(XWPFBorderType.NIL, STBorder.Enum.forInt(1));
/*  35: 55 */     xwpfBorderTypeMap.put(XWPFBorderType.NONE, STBorder.Enum.forInt(2));
/*  36: 56 */     xwpfBorderTypeMap.put(XWPFBorderType.SINGLE, STBorder.Enum.forInt(3));
/*  37: 57 */     xwpfBorderTypeMap.put(XWPFBorderType.THICK, STBorder.Enum.forInt(4));
/*  38: 58 */     xwpfBorderTypeMap.put(XWPFBorderType.DOUBLE, STBorder.Enum.forInt(5));
/*  39: 59 */     xwpfBorderTypeMap.put(XWPFBorderType.DOTTED, STBorder.Enum.forInt(6));
/*  40: 60 */     xwpfBorderTypeMap.put(XWPFBorderType.DASHED, STBorder.Enum.forInt(7));
/*  41: 61 */     xwpfBorderTypeMap.put(XWPFBorderType.DOT_DASH, STBorder.Enum.forInt(8));
/*  42:    */     
/*  43: 63 */     stBorderTypeMap = new HashMap();
/*  44: 64 */     stBorderTypeMap.put(Integer.valueOf(1), XWPFBorderType.NIL);
/*  45: 65 */     stBorderTypeMap.put(Integer.valueOf(2), XWPFBorderType.NONE);
/*  46: 66 */     stBorderTypeMap.put(Integer.valueOf(3), XWPFBorderType.SINGLE);
/*  47: 67 */     stBorderTypeMap.put(Integer.valueOf(4), XWPFBorderType.THICK);
/*  48: 68 */     stBorderTypeMap.put(Integer.valueOf(5), XWPFBorderType.DOUBLE);
/*  49: 69 */     stBorderTypeMap.put(Integer.valueOf(6), XWPFBorderType.DOTTED);
/*  50: 70 */     stBorderTypeMap.put(Integer.valueOf(7), XWPFBorderType.DASHED);
/*  51: 71 */     stBorderTypeMap.put(Integer.valueOf(8), XWPFBorderType.DOT_DASH);
/*  52:    */   }
/*  53:    */   
/*  54: 74 */   protected StringBuffer text = new StringBuffer();
/*  55:    */   protected List<XWPFTableRow> tableRows;
/*  56:    */   protected IBody part;
/*  57:    */   private CTTbl ctTbl;
/*  58:    */   
/*  59:    */   public XWPFTable(CTTbl table, IBody part, int row, int col)
/*  60:    */   {
/*  61: 83 */     this(table, part);
/*  62: 85 */     for (int i = 0; i < row; i++)
/*  63:    */     {
/*  64: 86 */       XWPFTableRow tabRow = getRow(i) == null ? createRow() : getRow(i);
/*  65: 87 */       for (int k = 0; k < col; k++) {
/*  66: 88 */         if (tabRow.getCell(k) == null) {
/*  67: 89 */           tabRow.createCell();
/*  68:    */         }
/*  69:    */       }
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public XWPFTable(CTTbl table, IBody part)
/*  74:    */   {
/*  75: 96 */     this.part = part;
/*  76: 97 */     this.ctTbl = table;
/*  77:    */     
/*  78: 99 */     this.tableRows = new ArrayList();
/*  79:102 */     if (table.sizeOfTrArray() == 0) {
/*  80:103 */       createEmptyTable(table);
/*  81:    */     }
/*  82:105 */     for (CTRow row : table.getTrArray())
/*  83:    */     {
/*  84:106 */       StringBuilder rowText = new StringBuilder();
/*  85:107 */       XWPFTableRow tabRow = new XWPFTableRow(row, this);
/*  86:108 */       this.tableRows.add(tabRow);
/*  87:109 */       for (CTTc cell : row.getTcArray()) {
/*  88:110 */         for (CTP ctp : cell.getPArray())
/*  89:    */         {
/*  90:111 */           XWPFParagraph p = new XWPFParagraph(ctp, part);
/*  91:112 */           if (rowText.length() > 0) {
/*  92:113 */             rowText.append('\t');
/*  93:    */           }
/*  94:115 */           rowText.append(p.getText());
/*  95:    */         }
/*  96:    */       }
/*  97:118 */       if (rowText.length() > 0)
/*  98:    */       {
/*  99:119 */         this.text.append(rowText);
/* 100:120 */         this.text.append('\n');
/* 101:    */       }
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   private void createEmptyTable(CTTbl table)
/* 106:    */   {
/* 107:127 */     table.addNewTr().addNewTc().addNewP();
/* 108:    */     
/* 109:129 */     CTTblPr tblpro = table.addNewTblPr();
/* 110:130 */     tblpro.addNewTblW().setW(new BigInteger("0"));
/* 111:131 */     tblpro.getTblW().setType(STTblWidth.AUTO);
/* 112:    */     
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:137 */     CTTblBorders borders = tblpro.addNewTblBorders();
/* 118:138 */     borders.addNewBottom().setVal(STBorder.SINGLE);
/* 119:139 */     borders.addNewInsideH().setVal(STBorder.SINGLE);
/* 120:140 */     borders.addNewInsideV().setVal(STBorder.SINGLE);
/* 121:141 */     borders.addNewLeft().setVal(STBorder.SINGLE);
/* 122:142 */     borders.addNewRight().setVal(STBorder.SINGLE);
/* 123:143 */     borders.addNewTop().setVal(STBorder.SINGLE);
/* 124:    */     
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:149 */     getRows();
/* 130:    */   }
/* 131:    */   
/* 132:    */   @Internal
/* 133:    */   public CTTbl getCTTbl()
/* 134:    */   {
/* 135:157 */     return this.ctTbl;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public String getText()
/* 139:    */   {
/* 140:171 */     return this.text.toString();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void addNewCol()
/* 144:    */   {
/* 145:182 */     if (this.ctTbl.sizeOfTrArray() == 0) {
/* 146:183 */       createRow();
/* 147:    */     }
/* 148:185 */     for (int i = 0; i < this.ctTbl.sizeOfTrArray(); i++)
/* 149:    */     {
/* 150:186 */       XWPFTableRow tabRow = new XWPFTableRow(this.ctTbl.getTrArray(i), this);
/* 151:187 */       tabRow.createCell();
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public XWPFTableRow createRow()
/* 156:    */   {
/* 157:197 */     int sizeCol = this.ctTbl.sizeOfTrArray() > 0 ? this.ctTbl.getTrArray(0).sizeOfTcArray() : 0;
/* 158:    */     
/* 159:199 */     XWPFTableRow tabRow = new XWPFTableRow(this.ctTbl.addNewTr(), this);
/* 160:200 */     addColumn(tabRow, sizeCol);
/* 161:201 */     this.tableRows.add(tabRow);
/* 162:202 */     return tabRow;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public XWPFTableRow getRow(int pos)
/* 166:    */   {
/* 167:210 */     if ((pos >= 0) && (pos < this.ctTbl.sizeOfTrArray())) {
/* 168:212 */       return (XWPFTableRow)getRows().get(pos);
/* 169:    */     }
/* 170:214 */     return null;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public int getWidth()
/* 174:    */   {
/* 175:221 */     CTTblPr tblPr = getTrPr();
/* 176:222 */     return tblPr.isSetTblW() ? tblPr.getTblW().getW().intValue() : -1;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setWidth(int width)
/* 180:    */   {
/* 181:229 */     CTTblPr tblPr = getTrPr();
/* 182:230 */     CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
/* 183:231 */     tblWidth.setW(new BigInteger("" + width));
/* 184:    */   }
/* 185:    */   
/* 186:    */   public int getNumberOfRows()
/* 187:    */   {
/* 188:238 */     return this.ctTbl.sizeOfTrArray();
/* 189:    */   }
/* 190:    */   
/* 191:    */   private CTTblPr getTrPr()
/* 192:    */   {
/* 193:242 */     return this.ctTbl.getTblPr() != null ? this.ctTbl.getTblPr() : this.ctTbl.addNewTblPr();
/* 194:    */   }
/* 195:    */   
/* 196:    */   private void addColumn(XWPFTableRow tabRow, int sizeCol)
/* 197:    */   {
/* 198:247 */     if (sizeCol > 0) {
/* 199:248 */       for (int i = 0; i < sizeCol; i++) {
/* 200:249 */         tabRow.createCell();
/* 201:    */       }
/* 202:    */     }
/* 203:    */   }
/* 204:    */   
/* 205:    */   public String getStyleID()
/* 206:    */   {
/* 207:260 */     String styleId = null;
/* 208:261 */     CTTblPr tblPr = this.ctTbl.getTblPr();
/* 209:262 */     if (tblPr != null)
/* 210:    */     {
/* 211:263 */       CTString styleStr = tblPr.getTblStyle();
/* 212:264 */       if (styleStr != null) {
/* 213:265 */         styleId = styleStr.getVal();
/* 214:    */       }
/* 215:    */     }
/* 216:268 */     return styleId;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setStyleID(String styleName)
/* 220:    */   {
/* 221:278 */     CTTblPr tblPr = getTrPr();
/* 222:279 */     CTString styleStr = tblPr.getTblStyle();
/* 223:280 */     if (styleStr == null) {
/* 224:281 */       styleStr = tblPr.addNewTblStyle();
/* 225:    */     }
/* 226:283 */     styleStr.setVal(styleName);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public XWPFBorderType getInsideHBorderType()
/* 230:    */   {
/* 231:287 */     XWPFBorderType bt = null;
/* 232:    */     
/* 233:289 */     CTTblPr tblPr = getTrPr();
/* 234:290 */     if (tblPr.isSetTblBorders())
/* 235:    */     {
/* 236:291 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 237:292 */       if (ctb.isSetInsideH())
/* 238:    */       {
/* 239:293 */         CTBorder border = ctb.getInsideH();
/* 240:294 */         bt = (XWPFBorderType)stBorderTypeMap.get(Integer.valueOf(border.getVal().intValue()));
/* 241:    */       }
/* 242:    */     }
/* 243:297 */     return bt;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public int getInsideHBorderSize()
/* 247:    */   {
/* 248:301 */     int size = -1;
/* 249:    */     
/* 250:303 */     CTTblPr tblPr = getTrPr();
/* 251:304 */     if (tblPr.isSetTblBorders())
/* 252:    */     {
/* 253:305 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 254:306 */       if (ctb.isSetInsideH())
/* 255:    */       {
/* 256:307 */         CTBorder border = ctb.getInsideH();
/* 257:308 */         size = border.getSz().intValue();
/* 258:    */       }
/* 259:    */     }
/* 260:311 */     return size;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public int getInsideHBorderSpace()
/* 264:    */   {
/* 265:315 */     int space = -1;
/* 266:    */     
/* 267:317 */     CTTblPr tblPr = getTrPr();
/* 268:318 */     if (tblPr.isSetTblBorders())
/* 269:    */     {
/* 270:319 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 271:320 */       if (ctb.isSetInsideH())
/* 272:    */       {
/* 273:321 */         CTBorder border = ctb.getInsideH();
/* 274:322 */         space = border.getSpace().intValue();
/* 275:    */       }
/* 276:    */     }
/* 277:325 */     return space;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public String getInsideHBorderColor()
/* 281:    */   {
/* 282:329 */     String color = null;
/* 283:    */     
/* 284:331 */     CTTblPr tblPr = getTrPr();
/* 285:332 */     if (tblPr.isSetTblBorders())
/* 286:    */     {
/* 287:333 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 288:334 */       if (ctb.isSetInsideH())
/* 289:    */       {
/* 290:335 */         CTBorder border = ctb.getInsideH();
/* 291:336 */         color = border.xgetColor().getStringValue();
/* 292:    */       }
/* 293:    */     }
/* 294:339 */     return color;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public XWPFBorderType getInsideVBorderType()
/* 298:    */   {
/* 299:343 */     XWPFBorderType bt = null;
/* 300:    */     
/* 301:345 */     CTTblPr tblPr = getTrPr();
/* 302:346 */     if (tblPr.isSetTblBorders())
/* 303:    */     {
/* 304:347 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 305:348 */       if (ctb.isSetInsideV())
/* 306:    */       {
/* 307:349 */         CTBorder border = ctb.getInsideV();
/* 308:350 */         bt = (XWPFBorderType)stBorderTypeMap.get(Integer.valueOf(border.getVal().intValue()));
/* 309:    */       }
/* 310:    */     }
/* 311:353 */     return bt;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public int getInsideVBorderSize()
/* 315:    */   {
/* 316:357 */     int size = -1;
/* 317:    */     
/* 318:359 */     CTTblPr tblPr = getTrPr();
/* 319:360 */     if (tblPr.isSetTblBorders())
/* 320:    */     {
/* 321:361 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 322:362 */       if (ctb.isSetInsideV())
/* 323:    */       {
/* 324:363 */         CTBorder border = ctb.getInsideV();
/* 325:364 */         size = border.getSz().intValue();
/* 326:    */       }
/* 327:    */     }
/* 328:367 */     return size;
/* 329:    */   }
/* 330:    */   
/* 331:    */   public int getInsideVBorderSpace()
/* 332:    */   {
/* 333:371 */     int space = -1;
/* 334:    */     
/* 335:373 */     CTTblPr tblPr = getTrPr();
/* 336:374 */     if (tblPr.isSetTblBorders())
/* 337:    */     {
/* 338:375 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 339:376 */       if (ctb.isSetInsideV())
/* 340:    */       {
/* 341:377 */         CTBorder border = ctb.getInsideV();
/* 342:378 */         space = border.getSpace().intValue();
/* 343:    */       }
/* 344:    */     }
/* 345:381 */     return space;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public String getInsideVBorderColor()
/* 349:    */   {
/* 350:385 */     String color = null;
/* 351:    */     
/* 352:387 */     CTTblPr tblPr = getTrPr();
/* 353:388 */     if (tblPr.isSetTblBorders())
/* 354:    */     {
/* 355:389 */       CTTblBorders ctb = tblPr.getTblBorders();
/* 356:390 */       if (ctb.isSetInsideV())
/* 357:    */       {
/* 358:391 */         CTBorder border = ctb.getInsideV();
/* 359:392 */         color = border.xgetColor().getStringValue();
/* 360:    */       }
/* 361:    */     }
/* 362:395 */     return color;
/* 363:    */   }
/* 364:    */   
/* 365:    */   public int getRowBandSize()
/* 366:    */   {
/* 367:399 */     int size = 0;
/* 368:400 */     CTTblPr tblPr = getTrPr();
/* 369:401 */     if (tblPr.isSetTblStyleRowBandSize())
/* 370:    */     {
/* 371:402 */       CTDecimalNumber rowSize = tblPr.getTblStyleRowBandSize();
/* 372:403 */       size = rowSize.getVal().intValue();
/* 373:    */     }
/* 374:405 */     return size;
/* 375:    */   }
/* 376:    */   
/* 377:    */   public void setRowBandSize(int size)
/* 378:    */   {
/* 379:409 */     CTTblPr tblPr = getTrPr();
/* 380:410 */     CTDecimalNumber rowSize = tblPr.isSetTblStyleRowBandSize() ? tblPr.getTblStyleRowBandSize() : tblPr.addNewTblStyleRowBandSize();
/* 381:411 */     rowSize.setVal(BigInteger.valueOf(size));
/* 382:    */   }
/* 383:    */   
/* 384:    */   public int getColBandSize()
/* 385:    */   {
/* 386:415 */     int size = 0;
/* 387:416 */     CTTblPr tblPr = getTrPr();
/* 388:417 */     if (tblPr.isSetTblStyleColBandSize())
/* 389:    */     {
/* 390:418 */       CTDecimalNumber colSize = tblPr.getTblStyleColBandSize();
/* 391:419 */       size = colSize.getVal().intValue();
/* 392:    */     }
/* 393:421 */     return size;
/* 394:    */   }
/* 395:    */   
/* 396:    */   public void setColBandSize(int size)
/* 397:    */   {
/* 398:425 */     CTTblPr tblPr = getTrPr();
/* 399:426 */     CTDecimalNumber colSize = tblPr.isSetTblStyleColBandSize() ? tblPr.getTblStyleColBandSize() : tblPr.addNewTblStyleColBandSize();
/* 400:427 */     colSize.setVal(BigInteger.valueOf(size));
/* 401:    */   }
/* 402:    */   
/* 403:    */   public void setInsideHBorder(XWPFBorderType type, int size, int space, String rgbColor)
/* 404:    */   {
/* 405:431 */     CTTblPr tblPr = getTrPr();
/* 406:432 */     CTTblBorders ctb = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
/* 407:433 */     CTBorder b = ctb.isSetInsideH() ? ctb.getInsideH() : ctb.addNewInsideH();
/* 408:434 */     b.setVal((STBorder.Enum)xwpfBorderTypeMap.get(type));
/* 409:435 */     b.setSz(BigInteger.valueOf(size));
/* 410:436 */     b.setSpace(BigInteger.valueOf(space));
/* 411:437 */     b.setColor(rgbColor);
/* 412:    */   }
/* 413:    */   
/* 414:    */   public void setInsideVBorder(XWPFBorderType type, int size, int space, String rgbColor)
/* 415:    */   {
/* 416:441 */     CTTblPr tblPr = getTrPr();
/* 417:442 */     CTTblBorders ctb = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
/* 418:443 */     CTBorder b = ctb.isSetInsideV() ? ctb.getInsideV() : ctb.addNewInsideV();
/* 419:444 */     b.setVal((STBorder.Enum)xwpfBorderTypeMap.get(type));
/* 420:445 */     b.setSz(BigInteger.valueOf(size));
/* 421:446 */     b.setSpace(BigInteger.valueOf(space));
/* 422:447 */     b.setColor(rgbColor);
/* 423:    */   }
/* 424:    */   
/* 425:    */   public int getCellMarginTop()
/* 426:    */   {
/* 427:451 */     int margin = 0;
/* 428:452 */     CTTblPr tblPr = getTrPr();
/* 429:453 */     CTTblCellMar tcm = tblPr.getTblCellMar();
/* 430:454 */     if (tcm != null)
/* 431:    */     {
/* 432:455 */       CTTblWidth tw = tcm.getTop();
/* 433:456 */       if (tw != null) {
/* 434:457 */         margin = tw.getW().intValue();
/* 435:    */       }
/* 436:    */     }
/* 437:460 */     return margin;
/* 438:    */   }
/* 439:    */   
/* 440:    */   public int getCellMarginLeft()
/* 441:    */   {
/* 442:464 */     int margin = 0;
/* 443:465 */     CTTblPr tblPr = getTrPr();
/* 444:466 */     CTTblCellMar tcm = tblPr.getTblCellMar();
/* 445:467 */     if (tcm != null)
/* 446:    */     {
/* 447:468 */       CTTblWidth tw = tcm.getLeft();
/* 448:469 */       if (tw != null) {
/* 449:470 */         margin = tw.getW().intValue();
/* 450:    */       }
/* 451:    */     }
/* 452:473 */     return margin;
/* 453:    */   }
/* 454:    */   
/* 455:    */   public int getCellMarginBottom()
/* 456:    */   {
/* 457:477 */     int margin = 0;
/* 458:478 */     CTTblPr tblPr = getTrPr();
/* 459:479 */     CTTblCellMar tcm = tblPr.getTblCellMar();
/* 460:480 */     if (tcm != null)
/* 461:    */     {
/* 462:481 */       CTTblWidth tw = tcm.getBottom();
/* 463:482 */       if (tw != null) {
/* 464:483 */         margin = tw.getW().intValue();
/* 465:    */       }
/* 466:    */     }
/* 467:486 */     return margin;
/* 468:    */   }
/* 469:    */   
/* 470:    */   public int getCellMarginRight()
/* 471:    */   {
/* 472:490 */     int margin = 0;
/* 473:491 */     CTTblPr tblPr = getTrPr();
/* 474:492 */     CTTblCellMar tcm = tblPr.getTblCellMar();
/* 475:493 */     if (tcm != null)
/* 476:    */     {
/* 477:494 */       CTTblWidth tw = tcm.getRight();
/* 478:495 */       if (tw != null) {
/* 479:496 */         margin = tw.getW().intValue();
/* 480:    */       }
/* 481:    */     }
/* 482:499 */     return margin;
/* 483:    */   }
/* 484:    */   
/* 485:    */   public void setCellMargins(int top, int left, int bottom, int right)
/* 486:    */   {
/* 487:503 */     CTTblPr tblPr = getTrPr();
/* 488:504 */     CTTblCellMar tcm = tblPr.isSetTblCellMar() ? tblPr.getTblCellMar() : tblPr.addNewTblCellMar();
/* 489:    */     
/* 490:506 */     CTTblWidth tw = tcm.isSetLeft() ? tcm.getLeft() : tcm.addNewLeft();
/* 491:507 */     tw.setType(STTblWidth.DXA);
/* 492:508 */     tw.setW(BigInteger.valueOf(left));
/* 493:    */     
/* 494:510 */     tw = tcm.isSetTop() ? tcm.getTop() : tcm.addNewTop();
/* 495:511 */     tw.setType(STTblWidth.DXA);
/* 496:512 */     tw.setW(BigInteger.valueOf(top));
/* 497:    */     
/* 498:514 */     tw = tcm.isSetBottom() ? tcm.getBottom() : tcm.addNewBottom();
/* 499:515 */     tw.setType(STTblWidth.DXA);
/* 500:516 */     tw.setW(BigInteger.valueOf(bottom));
/* 501:    */     
/* 502:518 */     tw = tcm.isSetRight() ? tcm.getRight() : tcm.addNewRight();
/* 503:519 */     tw.setType(STTblWidth.DXA);
/* 504:520 */     tw.setW(BigInteger.valueOf(right));
/* 505:    */   }
/* 506:    */   
/* 507:    */   public void addRow(XWPFTableRow row)
/* 508:    */   {
/* 509:529 */     this.ctTbl.addNewTr();
/* 510:530 */     this.ctTbl.setTrArray(getNumberOfRows() - 1, row.getCtRow());
/* 511:531 */     this.tableRows.add(row);
/* 512:    */   }
/* 513:    */   
/* 514:    */   public boolean addRow(XWPFTableRow row, int pos)
/* 515:    */   {
/* 516:541 */     if ((pos >= 0) && (pos <= this.tableRows.size()))
/* 517:    */     {
/* 518:542 */       this.ctTbl.insertNewTr(pos);
/* 519:543 */       this.ctTbl.setTrArray(pos, row.getCtRow());
/* 520:544 */       this.tableRows.add(pos, row);
/* 521:545 */       return true;
/* 522:    */     }
/* 523:547 */     return false;
/* 524:    */   }
/* 525:    */   
/* 526:    */   public XWPFTableRow insertNewTableRow(int pos)
/* 527:    */   {
/* 528:557 */     if ((pos >= 0) && (pos <= this.tableRows.size()))
/* 529:    */     {
/* 530:558 */       CTRow row = this.ctTbl.insertNewTr(pos);
/* 531:559 */       XWPFTableRow tableRow = new XWPFTableRow(row, this);
/* 532:560 */       this.tableRows.add(pos, tableRow);
/* 533:561 */       return tableRow;
/* 534:    */     }
/* 535:563 */     return null;
/* 536:    */   }
/* 537:    */   
/* 538:    */   public boolean removeRow(int pos)
/* 539:    */     throws IndexOutOfBoundsException
/* 540:    */   {
/* 541:572 */     if ((pos >= 0) && (pos < this.tableRows.size()))
/* 542:    */     {
/* 543:573 */       if (this.ctTbl.sizeOfTrArray() > 0) {
/* 544:574 */         this.ctTbl.removeTr(pos);
/* 545:    */       }
/* 546:576 */       this.tableRows.remove(pos);
/* 547:577 */       return true;
/* 548:    */     }
/* 549:579 */     return false;
/* 550:    */   }
/* 551:    */   
/* 552:    */   public List<XWPFTableRow> getRows()
/* 553:    */   {
/* 554:583 */     return this.tableRows;
/* 555:    */   }
/* 556:    */   
/* 557:    */   public BodyElementType getElementType()
/* 558:    */   {
/* 559:592 */     return BodyElementType.TABLE;
/* 560:    */   }
/* 561:    */   
/* 562:    */   public IBody getBody()
/* 563:    */   {
/* 564:596 */     return this.part;
/* 565:    */   }
/* 566:    */   
/* 567:    */   public POIXMLDocumentPart getPart()
/* 568:    */   {
/* 569:605 */     if (this.part != null) {
/* 570:606 */       return this.part.getPart();
/* 571:    */     }
/* 572:608 */     return null;
/* 573:    */   }
/* 574:    */   
/* 575:    */   public BodyType getPartType()
/* 576:    */   {
/* 577:617 */     return this.part.getPartType();
/* 578:    */   }
/* 579:    */   
/* 580:    */   public XWPFTableRow getRow(CTRow row)
/* 581:    */   {
/* 582:625 */     for (int i = 0; i < getRows().size(); i++) {
/* 583:626 */       if (((XWPFTableRow)getRows().get(i)).getCtRow() == row) {
/* 584:626 */         return getRow(i);
/* 585:    */       }
/* 586:    */     }
/* 587:628 */     return null;
/* 588:    */   }
/* 589:    */   
/* 590:    */   public void addNewRowBetween(int start, int end) {}
/* 591:    */   
/* 592:    */   public static enum XWPFBorderType
/* 593:    */   {
/* 594:633 */     NIL,  NONE,  SINGLE,  THICK,  DOUBLE,  DOTTED,  DASHED,  DOT_DASH;
/* 595:    */     
/* 596:    */     private XWPFBorderType() {}
/* 597:    */   }
/* 598:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFTable
 * JD-Core Version:    0.7.0.1
 */