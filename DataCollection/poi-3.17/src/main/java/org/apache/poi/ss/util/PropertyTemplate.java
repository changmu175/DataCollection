/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   9:    */ import org.apache.poi.ss.usermodel.BorderExtent;
/*  10:    */ import org.apache.poi.ss.usermodel.BorderStyle;
/*  11:    */ import org.apache.poi.ss.usermodel.Cell;
/*  12:    */ import org.apache.poi.ss.usermodel.Row;
/*  13:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  14:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  15:    */ 
/*  16:    */ public final class PropertyTemplate
/*  17:    */ {
/*  18:    */   private Map<CellAddress, Map<String, Object>> _propertyTemplate;
/*  19:    */   
/*  20:    */   public PropertyTemplate()
/*  21:    */   {
/*  22: 66 */     this._propertyTemplate = new HashMap();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public PropertyTemplate(PropertyTemplate template)
/*  26:    */   {
/*  27: 75 */     this();
/*  28: 76 */     for (Entry<CellAddress, Map<String, Object>> entry : template.getTemplate().entrySet()) {
/*  29: 77 */       this._propertyTemplate.put(new CellAddress((CellAddress)entry.getKey()), cloneCellProperties((Map)entry.getValue()));
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   private Map<CellAddress, Map<String, Object>> getTemplate()
/*  34:    */   {
/*  35: 82 */     return this._propertyTemplate;
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static Map<String, Object> cloneCellProperties(Map<String, Object> properties)
/*  39:    */   {
/*  40: 86 */     Map<String, Object> newProperties = new HashMap();
/*  41: 87 */     for (Entry<String, Object> entry : properties.entrySet()) {
/*  42: 88 */       newProperties.put(entry.getKey(), entry.getValue());
/*  43:    */     }
/*  44: 90 */     return newProperties;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void drawBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent)
/*  48:    */   {
/*  49:109 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/*  50:    */     {
/*  51:    */     case 1: 
/*  52:111 */       removeBorders(range);
/*  53:112 */       break;
/*  54:    */     case 2: 
/*  55:114 */       drawHorizontalBorders(range, borderType, BorderExtent.ALL);
/*  56:115 */       drawVerticalBorders(range, borderType, BorderExtent.ALL);
/*  57:116 */       break;
/*  58:    */     case 3: 
/*  59:118 */       drawHorizontalBorders(range, borderType, BorderExtent.INSIDE);
/*  60:119 */       drawVerticalBorders(range, borderType, BorderExtent.INSIDE);
/*  61:120 */       break;
/*  62:    */     case 4: 
/*  63:122 */       drawOutsideBorders(range, borderType, BorderExtent.ALL);
/*  64:123 */       break;
/*  65:    */     case 5: 
/*  66:125 */       drawTopBorder(range, borderType);
/*  67:126 */       break;
/*  68:    */     case 6: 
/*  69:128 */       drawBottomBorder(range, borderType);
/*  70:129 */       break;
/*  71:    */     case 7: 
/*  72:131 */       drawLeftBorder(range, borderType);
/*  73:132 */       break;
/*  74:    */     case 8: 
/*  75:134 */       drawRightBorder(range, borderType);
/*  76:135 */       break;
/*  77:    */     case 9: 
/*  78:137 */       drawHorizontalBorders(range, borderType, BorderExtent.ALL);
/*  79:138 */       break;
/*  80:    */     case 10: 
/*  81:140 */       drawHorizontalBorders(range, borderType, BorderExtent.INSIDE);
/*  82:141 */       break;
/*  83:    */     case 11: 
/*  84:143 */       drawOutsideBorders(range, borderType, BorderExtent.HORIZONTAL);
/*  85:144 */       break;
/*  86:    */     case 12: 
/*  87:146 */       drawVerticalBorders(range, borderType, BorderExtent.ALL);
/*  88:147 */       break;
/*  89:    */     case 13: 
/*  90:149 */       drawVerticalBorders(range, borderType, BorderExtent.INSIDE);
/*  91:150 */       break;
/*  92:    */     case 14: 
/*  93:152 */       drawOutsideBorders(range, borderType, BorderExtent.VERTICAL);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void drawBorders(CellRangeAddress range, BorderStyle borderType, short color, BorderExtent extent)
/*  98:    */   {
/*  99:176 */     drawBorders(range, borderType, extent);
/* 100:177 */     if (borderType != BorderStyle.NONE) {
/* 101:178 */       drawBorderColors(range, color, extent);
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   private void drawTopBorder(CellRangeAddress range, BorderStyle borderType)
/* 106:    */   {
/* 107:194 */     int row = range.getFirstRow();
/* 108:195 */     int firstCol = range.getFirstColumn();
/* 109:196 */     int lastCol = range.getLastColumn();
/* 110:197 */     for (int i = firstCol; i <= lastCol; i++)
/* 111:    */     {
/* 112:198 */       addProperty(row, i, "borderTop", borderType);
/* 113:199 */       if ((borderType == BorderStyle.NONE) && (row > 0)) {
/* 114:200 */         addProperty(row - 1, i, "borderBottom", borderType);
/* 115:    */       }
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   private void drawBottomBorder(CellRangeAddress range, BorderStyle borderType)
/* 120:    */   {
/* 121:218 */     int row = range.getLastRow();
/* 122:219 */     int firstCol = range.getFirstColumn();
/* 123:220 */     int lastCol = range.getLastColumn();
/* 124:221 */     for (int i = firstCol; i <= lastCol; i++)
/* 125:    */     {
/* 126:222 */       addProperty(row, i, "borderBottom", borderType);
/* 127:223 */       if ((borderType == BorderStyle.NONE) && (row < SpreadsheetVersion.EXCEL2007.getMaxRows() - 1)) {
/* 128:225 */         addProperty(row + 1, i, "borderTop", borderType);
/* 129:    */       }
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   private void drawLeftBorder(CellRangeAddress range, BorderStyle borderType)
/* 134:    */   {
/* 135:243 */     int firstRow = range.getFirstRow();
/* 136:244 */     int lastRow = range.getLastRow();
/* 137:245 */     int col = range.getFirstColumn();
/* 138:246 */     for (int i = firstRow; i <= lastRow; i++)
/* 139:    */     {
/* 140:247 */       addProperty(i, col, "borderLeft", borderType);
/* 141:248 */       if ((borderType == BorderStyle.NONE) && (col > 0)) {
/* 142:249 */         addProperty(i, col - 1, "borderRight", borderType);
/* 143:    */       }
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   private void drawRightBorder(CellRangeAddress range, BorderStyle borderType)
/* 148:    */   {
/* 149:267 */     int firstRow = range.getFirstRow();
/* 150:268 */     int lastRow = range.getLastRow();
/* 151:269 */     int col = range.getLastColumn();
/* 152:270 */     for (int i = firstRow; i <= lastRow; i++)
/* 153:    */     {
/* 154:271 */       addProperty(i, col, "borderRight", borderType);
/* 155:272 */       if ((borderType == BorderStyle.NONE) && (col < SpreadsheetVersion.EXCEL2007.getMaxColumns() - 1)) {
/* 156:274 */         addProperty(i, col + 1, "borderLeft", borderType);
/* 157:    */       }
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   private void drawOutsideBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent)
/* 162:    */   {
/* 163:300 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 164:    */     {
/* 165:    */     case 2: 
/* 166:    */     case 9: 
/* 167:    */     case 12: 
/* 168:304 */       if ((extent == BorderExtent.ALL) || (extent == BorderExtent.HORIZONTAL))
/* 169:    */       {
/* 170:305 */         drawTopBorder(range, borderType);
/* 171:306 */         drawBottomBorder(range, borderType);
/* 172:    */       }
/* 173:308 */       if ((extent == BorderExtent.ALL) || (extent == BorderExtent.VERTICAL))
/* 174:    */       {
/* 175:309 */         drawLeftBorder(range, borderType);
/* 176:310 */         drawRightBorder(range, borderType);
/* 177:    */       }
/* 178:    */       break;
/* 179:    */     default: 
/* 180:314 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL, HORIZONTAL, and VERTICAL");
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   private void drawHorizontalBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent)
/* 185:    */   {
/* 186:339 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 187:    */     {
/* 188:    */     case 2: 
/* 189:    */     case 3: 
/* 190:342 */       int firstRow = range.getFirstRow();
/* 191:343 */       int lastRow = range.getLastRow();
/* 192:344 */       int firstCol = range.getFirstColumn();
/* 193:345 */       int lastCol = range.getLastColumn();
/* 194:346 */       for (int i = firstRow; i <= lastRow; i++)
/* 195:    */       {
/* 196:347 */         CellRangeAddress row = new CellRangeAddress(i, i, firstCol, lastCol);
/* 197:349 */         if ((extent == BorderExtent.ALL) || (i > firstRow)) {
/* 198:350 */           drawTopBorder(row, borderType);
/* 199:    */         }
/* 200:352 */         if ((extent == BorderExtent.ALL) || (i < lastRow)) {
/* 201:353 */           drawBottomBorder(row, borderType);
/* 202:    */         }
/* 203:    */       }
/* 204:356 */       break;
/* 205:    */     default: 
/* 206:358 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   private void drawVerticalBorders(CellRangeAddress range, BorderStyle borderType, BorderExtent extent)
/* 211:    */   {
/* 212:383 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 213:    */     {
/* 214:    */     case 2: 
/* 215:    */     case 3: 
/* 216:386 */       int firstRow = range.getFirstRow();
/* 217:387 */       int lastRow = range.getLastRow();
/* 218:388 */       int firstCol = range.getFirstColumn();
/* 219:389 */       int lastCol = range.getLastColumn();
/* 220:390 */       for (int i = firstCol; i <= lastCol; i++)
/* 221:    */       {
/* 222:391 */         CellRangeAddress row = new CellRangeAddress(firstRow, lastRow, i, i);
/* 223:393 */         if ((extent == BorderExtent.ALL) || (i > firstCol)) {
/* 224:394 */           drawLeftBorder(row, borderType);
/* 225:    */         }
/* 226:396 */         if ((extent == BorderExtent.ALL) || (i < lastCol)) {
/* 227:397 */           drawRightBorder(row, borderType);
/* 228:    */         }
/* 229:    */       }
/* 230:400 */       break;
/* 231:    */     default: 
/* 232:402 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   private void removeBorders(CellRangeAddress range)
/* 237:    */   {
/* 238:414 */     Set<String> properties = new HashSet();
/* 239:415 */     properties.add("borderTop");
/* 240:416 */     properties.add("borderBottom");
/* 241:417 */     properties.add("borderLeft");
/* 242:418 */     properties.add("borderRight");
/* 243:419 */     for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
/* 244:420 */       for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
/* 245:422 */         removeProperties(row, col, properties);
/* 246:    */       }
/* 247:    */     }
/* 248:425 */     removeBorderColors(range);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void applyBorders(Sheet sheet)
/* 252:    */   {
/* 253:437 */     Workbook wb = sheet.getWorkbook();
/* 254:438 */     for (Entry<CellAddress, Map<String, Object>> entry : this._propertyTemplate.entrySet())
/* 255:    */     {
/* 256:440 */       CellAddress cellAddress = (CellAddress)entry.getKey();
/* 257:441 */       if ((cellAddress.getRow() < wb.getSpreadsheetVersion().getMaxRows()) && (cellAddress.getColumn() < wb.getSpreadsheetVersion().getMaxColumns()))
/* 258:    */       {
/* 259:444 */         Map<String, Object> properties = (Map)entry.getValue();
/* 260:445 */         Row row = CellUtil.getRow(cellAddress.getRow(), sheet);
/* 261:446 */         Cell cell = CellUtil.getCell(row, cellAddress.getColumn());
/* 262:447 */         CellUtil.setCellStyleProperties(cell, properties);
/* 263:    */       }
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   public void drawBorderColors(CellRangeAddress range, short color, BorderExtent extent)
/* 268:    */   {
/* 269:470 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 270:    */     {
/* 271:    */     case 1: 
/* 272:472 */       removeBorderColors(range);
/* 273:473 */       break;
/* 274:    */     case 2: 
/* 275:475 */       drawHorizontalBorderColors(range, color, BorderExtent.ALL);
/* 276:476 */       drawVerticalBorderColors(range, color, BorderExtent.ALL);
/* 277:477 */       break;
/* 278:    */     case 3: 
/* 279:479 */       drawHorizontalBorderColors(range, color, BorderExtent.INSIDE);
/* 280:480 */       drawVerticalBorderColors(range, color, BorderExtent.INSIDE);
/* 281:481 */       break;
/* 282:    */     case 4: 
/* 283:483 */       drawOutsideBorderColors(range, color, BorderExtent.ALL);
/* 284:484 */       break;
/* 285:    */     case 5: 
/* 286:486 */       drawTopBorderColor(range, color);
/* 287:487 */       break;
/* 288:    */     case 6: 
/* 289:489 */       drawBottomBorderColor(range, color);
/* 290:490 */       break;
/* 291:    */     case 7: 
/* 292:492 */       drawLeftBorderColor(range, color);
/* 293:493 */       break;
/* 294:    */     case 8: 
/* 295:495 */       drawRightBorderColor(range, color);
/* 296:496 */       break;
/* 297:    */     case 9: 
/* 298:498 */       drawHorizontalBorderColors(range, color, BorderExtent.ALL);
/* 299:499 */       break;
/* 300:    */     case 10: 
/* 301:501 */       drawHorizontalBorderColors(range, color, BorderExtent.INSIDE);
/* 302:502 */       break;
/* 303:    */     case 11: 
/* 304:504 */       drawOutsideBorderColors(range, color, BorderExtent.HORIZONTAL);
/* 305:505 */       break;
/* 306:    */     case 12: 
/* 307:507 */       drawVerticalBorderColors(range, color, BorderExtent.ALL);
/* 308:508 */       break;
/* 309:    */     case 13: 
/* 310:510 */       drawVerticalBorderColors(range, color, BorderExtent.INSIDE);
/* 311:511 */       break;
/* 312:    */     case 14: 
/* 313:513 */       drawOutsideBorderColors(range, color, BorderExtent.VERTICAL);
/* 314:    */     }
/* 315:    */   }
/* 316:    */   
/* 317:    */   private void drawTopBorderColor(CellRangeAddress range, short color)
/* 318:    */   {
/* 319:531 */     int row = range.getFirstRow();
/* 320:532 */     int firstCol = range.getFirstColumn();
/* 321:533 */     int lastCol = range.getLastColumn();
/* 322:534 */     for (int i = firstCol; i <= lastCol; i++)
/* 323:    */     {
/* 324:535 */       if (getBorderStyle(row, i, "borderTop") == BorderStyle.NONE) {
/* 325:537 */         drawTopBorder(new CellRangeAddress(row, row, i, i), BorderStyle.THIN);
/* 326:    */       }
/* 327:540 */       addProperty(row, i, "topBorderColor", color);
/* 328:    */     }
/* 329:    */   }
/* 330:    */   
/* 331:    */   private void drawBottomBorderColor(CellRangeAddress range, short color)
/* 332:    */   {
/* 333:557 */     int row = range.getLastRow();
/* 334:558 */     int firstCol = range.getFirstColumn();
/* 335:559 */     int lastCol = range.getLastColumn();
/* 336:560 */     for (int i = firstCol; i <= lastCol; i++)
/* 337:    */     {
/* 338:561 */       if (getBorderStyle(row, i, "borderBottom") == BorderStyle.NONE) {
/* 339:563 */         drawBottomBorder(new CellRangeAddress(row, row, i, i), BorderStyle.THIN);
/* 340:    */       }
/* 341:566 */       addProperty(row, i, "bottomBorderColor", color);
/* 342:    */     }
/* 343:    */   }
/* 344:    */   
/* 345:    */   private void drawLeftBorderColor(CellRangeAddress range, short color)
/* 346:    */   {
/* 347:583 */     int firstRow = range.getFirstRow();
/* 348:584 */     int lastRow = range.getLastRow();
/* 349:585 */     int col = range.getFirstColumn();
/* 350:586 */     for (int i = firstRow; i <= lastRow; i++)
/* 351:    */     {
/* 352:587 */       if (getBorderStyle(i, col, "borderLeft") == BorderStyle.NONE) {
/* 353:589 */         drawLeftBorder(new CellRangeAddress(i, i, col, col), BorderStyle.THIN);
/* 354:    */       }
/* 355:592 */       addProperty(i, col, "leftBorderColor", color);
/* 356:    */     }
/* 357:    */   }
/* 358:    */   
/* 359:    */   private void drawRightBorderColor(CellRangeAddress range, short color)
/* 360:    */   {
/* 361:610 */     int firstRow = range.getFirstRow();
/* 362:611 */     int lastRow = range.getLastRow();
/* 363:612 */     int col = range.getLastColumn();
/* 364:613 */     for (int i = firstRow; i <= lastRow; i++)
/* 365:    */     {
/* 366:614 */       if (getBorderStyle(i, col, "borderRight") == BorderStyle.NONE) {
/* 367:616 */         drawRightBorder(new CellRangeAddress(i, i, col, col), BorderStyle.THIN);
/* 368:    */       }
/* 369:619 */       addProperty(i, col, "rightBorderColor", color);
/* 370:    */     }
/* 371:    */   }
/* 372:    */   
/* 373:    */   private void drawOutsideBorderColors(CellRangeAddress range, short color, BorderExtent extent)
/* 374:    */   {
/* 375:645 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 376:    */     {
/* 377:    */     case 2: 
/* 378:    */     case 9: 
/* 379:    */     case 12: 
/* 380:649 */       if ((extent == BorderExtent.ALL) || (extent == BorderExtent.HORIZONTAL))
/* 381:    */       {
/* 382:650 */         drawTopBorderColor(range, color);
/* 383:651 */         drawBottomBorderColor(range, color);
/* 384:    */       }
/* 385:653 */       if ((extent == BorderExtent.ALL) || (extent == BorderExtent.VERTICAL))
/* 386:    */       {
/* 387:654 */         drawLeftBorderColor(range, color);
/* 388:655 */         drawRightBorderColor(range, color);
/* 389:    */       }
/* 390:    */       break;
/* 391:    */     default: 
/* 392:659 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL, HORIZONTAL, and VERTICAL");
/* 393:    */     }
/* 394:    */   }
/* 395:    */   
/* 396:    */   private void drawHorizontalBorderColors(CellRangeAddress range, short color, BorderExtent extent)
/* 397:    */   {
/* 398:685 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 399:    */     {
/* 400:    */     case 2: 
/* 401:    */     case 3: 
/* 402:688 */       int firstRow = range.getFirstRow();
/* 403:689 */       int lastRow = range.getLastRow();
/* 404:690 */       int firstCol = range.getFirstColumn();
/* 405:691 */       int lastCol = range.getLastColumn();
/* 406:692 */       for (int i = firstRow; i <= lastRow; i++)
/* 407:    */       {
/* 408:693 */         CellRangeAddress row = new CellRangeAddress(i, i, firstCol, lastCol);
/* 409:695 */         if ((extent == BorderExtent.ALL) || (i > firstRow)) {
/* 410:696 */           drawTopBorderColor(row, color);
/* 411:    */         }
/* 412:698 */         if ((extent == BorderExtent.ALL) || (i < lastRow)) {
/* 413:699 */           drawBottomBorderColor(row, color);
/* 414:    */         }
/* 415:    */       }
/* 416:702 */       break;
/* 417:    */     default: 
/* 418:704 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
/* 419:    */     }
/* 420:    */   }
/* 421:    */   
/* 422:    */   private void drawVerticalBorderColors(CellRangeAddress range, short color, BorderExtent extent)
/* 423:    */   {
/* 424:730 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$BorderExtent[extent.ordinal()])
/* 425:    */     {
/* 426:    */     case 2: 
/* 427:    */     case 3: 
/* 428:733 */       int firstRow = range.getFirstRow();
/* 429:734 */       int lastRow = range.getLastRow();
/* 430:735 */       int firstCol = range.getFirstColumn();
/* 431:736 */       int lastCol = range.getLastColumn();
/* 432:737 */       for (int i = firstCol; i <= lastCol; i++)
/* 433:    */       {
/* 434:738 */         CellRangeAddress row = new CellRangeAddress(firstRow, lastRow, i, i);
/* 435:740 */         if ((extent == BorderExtent.ALL) || (i > firstCol)) {
/* 436:741 */           drawLeftBorderColor(row, color);
/* 437:    */         }
/* 438:743 */         if ((extent == BorderExtent.ALL) || (i < lastCol)) {
/* 439:744 */           drawRightBorderColor(row, color);
/* 440:    */         }
/* 441:    */       }
/* 442:747 */       break;
/* 443:    */     default: 
/* 444:749 */       throw new IllegalArgumentException("Unsupported PropertyTemplate.Extent, valid Extents are ALL and INSIDE");
/* 445:    */     }
/* 446:    */   }
/* 447:    */   
/* 448:    */   private void removeBorderColors(CellRangeAddress range)
/* 449:    */   {
/* 450:761 */     Set<String> properties = new HashSet();
/* 451:762 */     properties.add("topBorderColor");
/* 452:763 */     properties.add("bottomBorderColor");
/* 453:764 */     properties.add("leftBorderColor");
/* 454:765 */     properties.add("rightBorderColor");
/* 455:766 */     for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
/* 456:767 */       for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
/* 457:769 */         removeProperties(row, col, properties);
/* 458:    */       }
/* 459:    */     }
/* 460:    */   }
/* 461:    */   
/* 462:    */   private void addProperty(int row, int col, String property, short value)
/* 463:    */   {
/* 464:783 */     addProperty(row, col, property, Short.valueOf(value));
/* 465:    */   }
/* 466:    */   
/* 467:    */   private void addProperty(int row, int col, String property, Object value)
/* 468:    */   {
/* 469:795 */     CellAddress cell = new CellAddress(row, col);
/* 470:796 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 471:797 */     if (cellProperties == null) {
/* 472:798 */       cellProperties = new HashMap();
/* 473:    */     }
/* 474:800 */     cellProperties.put(property, value);
/* 475:801 */     this._propertyTemplate.put(cell, cellProperties);
/* 476:    */   }
/* 477:    */   
/* 478:    */   private void removeProperties(int row, int col, Set<String> properties)
/* 479:    */   {
/* 480:813 */     CellAddress cell = new CellAddress(row, col);
/* 481:814 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 482:815 */     if (cellProperties != null)
/* 483:    */     {
/* 484:816 */       cellProperties.keySet().removeAll(properties);
/* 485:817 */       if (cellProperties.isEmpty()) {
/* 486:818 */         this._propertyTemplate.remove(cell);
/* 487:    */       } else {
/* 488:820 */         this._propertyTemplate.put(cell, cellProperties);
/* 489:    */       }
/* 490:    */     }
/* 491:    */   }
/* 492:    */   
/* 493:    */   public int getNumBorders(CellAddress cell)
/* 494:    */   {
/* 495:831 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 496:832 */     if (cellProperties == null) {
/* 497:833 */       return 0;
/* 498:    */     }
/* 499:836 */     int count = 0;
/* 500:837 */     for (String property : cellProperties.keySet())
/* 501:    */     {
/* 502:838 */       if (property.equals("borderTop")) {
/* 503:839 */         count++;
/* 504:    */       }
/* 505:840 */       if (property.equals("borderBottom")) {
/* 506:841 */         count++;
/* 507:    */       }
/* 508:842 */       if (property.equals("borderLeft")) {
/* 509:843 */         count++;
/* 510:    */       }
/* 511:844 */       if (property.equals("borderRight")) {
/* 512:845 */         count++;
/* 513:    */       }
/* 514:    */     }
/* 515:847 */     return count;
/* 516:    */   }
/* 517:    */   
/* 518:    */   public int getNumBorders(int row, int col)
/* 519:    */   {
/* 520:857 */     return getNumBorders(new CellAddress(row, col));
/* 521:    */   }
/* 522:    */   
/* 523:    */   public int getNumBorderColors(CellAddress cell)
/* 524:    */   {
/* 525:866 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 526:867 */     if (cellProperties == null) {
/* 527:868 */       return 0;
/* 528:    */     }
/* 529:871 */     int count = 0;
/* 530:872 */     for (String property : cellProperties.keySet())
/* 531:    */     {
/* 532:873 */       if (property.equals("topBorderColor")) {
/* 533:874 */         count++;
/* 534:    */       }
/* 535:875 */       if (property.equals("bottomBorderColor")) {
/* 536:876 */         count++;
/* 537:    */       }
/* 538:877 */       if (property.equals("leftBorderColor")) {
/* 539:878 */         count++;
/* 540:    */       }
/* 541:879 */       if (property.equals("rightBorderColor")) {
/* 542:880 */         count++;
/* 543:    */       }
/* 544:    */     }
/* 545:882 */     return count;
/* 546:    */   }
/* 547:    */   
/* 548:    */   public int getNumBorderColors(int row, int col)
/* 549:    */   {
/* 550:892 */     return getNumBorderColors(new CellAddress(row, col));
/* 551:    */   }
/* 552:    */   
/* 553:    */   public BorderStyle getBorderStyle(CellAddress cell, String property)
/* 554:    */   {
/* 555:902 */     BorderStyle value = BorderStyle.NONE;
/* 556:903 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 557:904 */     if (cellProperties != null)
/* 558:    */     {
/* 559:905 */       Object obj = cellProperties.get(property);
/* 560:906 */       if ((obj instanceof BorderStyle)) {
/* 561:907 */         value = (BorderStyle)obj;
/* 562:    */       }
/* 563:    */     }
/* 564:910 */     return value;
/* 565:    */   }
/* 566:    */   
/* 567:    */   public BorderStyle getBorderStyle(int row, int col, String property)
/* 568:    */   {
/* 569:921 */     return getBorderStyle(new CellAddress(row, col), property);
/* 570:    */   }
/* 571:    */   
/* 572:    */   public short getTemplateProperty(CellAddress cell, String property)
/* 573:    */   {
/* 574:931 */     short value = 0;
/* 575:932 */     Map<String, Object> cellProperties = (Map)this._propertyTemplate.get(cell);
/* 576:933 */     if (cellProperties != null)
/* 577:    */     {
/* 578:934 */       Object obj = cellProperties.get(property);
/* 579:935 */       if (obj != null) {
/* 580:936 */         value = getShort(obj);
/* 581:    */       }
/* 582:    */     }
/* 583:939 */     return value;
/* 584:    */   }
/* 585:    */   
/* 586:    */   public short getTemplateProperty(int row, int col, String property)
/* 587:    */   {
/* 588:950 */     return getTemplateProperty(new CellAddress(row, col), property);
/* 589:    */   }
/* 590:    */   
/* 591:    */   private static short getShort(Object value)
/* 592:    */   {
/* 593:961 */     if ((value instanceof Short)) {
/* 594:962 */       return ((Short)value).shortValue();
/* 595:    */     }
/* 596:964 */     return 0;
/* 597:    */   }
/* 598:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.util.PropertyTemplate

 * JD-Core Version:    0.7.0.1

 */