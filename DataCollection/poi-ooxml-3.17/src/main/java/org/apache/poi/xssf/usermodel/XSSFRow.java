/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.TreeMap;
/*   8:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   9:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*  10:    */ import org.apache.poi.ss.usermodel.Cell;
/*  11:    */ import org.apache.poi.ss.usermodel.CellCopyPolicy;
/*  12:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  13:    */ import org.apache.poi.ss.usermodel.CellType;
/*  14:    */ import org.apache.poi.ss.usermodel.Row;
/*  15:    */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*  16:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  17:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  18:    */ import org.apache.poi.ss.util.CellReference;
/*  19:    */ import org.apache.poi.util.Internal;
/*  20:    */ import org.apache.poi.xssf.model.CalculationChain;
/*  21:    */ import org.apache.poi.xssf.model.StylesTable;
/*  22:    */ import org.apache.poi.xssf.usermodel.helpers.XSSFRowShifter;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell.Factory;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
/*  27:    */ 
/*  28:    */ public class XSSFRow
/*  29:    */   implements Row, Comparable<XSSFRow>
/*  30:    */ {
/*  31:    */   private final CTRow _row;
/*  32:    */   private final TreeMap<Integer, XSSFCell> _cells;
/*  33:    */   private final XSSFSheet _sheet;
/*  34:    */   
/*  35:    */   protected XSSFRow(CTRow row, XSSFSheet sheet)
/*  36:    */   {
/*  37: 71 */     this._row = row;
/*  38: 72 */     this._sheet = sheet;
/*  39: 73 */     this._cells = new TreeMap();
/*  40: 74 */     for (CTCell c : row.getCArray())
/*  41:    */     {
/*  42: 75 */       XSSFCell cell = new XSSFCell(this, c);
/*  43:    */       
/*  44: 77 */       Integer colI = new Integer(cell.getColumnIndex());
/*  45: 78 */       this._cells.put(colI, cell);
/*  46: 79 */       sheet.onReadCell(cell);
/*  47:    */     }
/*  48: 82 */     if (!row.isSetR())
/*  49:    */     {
/*  50: 85 */       int nextRowNum = sheet.getLastRowNum() + 2;
/*  51: 86 */       if ((nextRowNum == 2) && (sheet.getPhysicalNumberOfRows() == 0)) {
/*  52: 87 */         nextRowNum = 1;
/*  53:    */       }
/*  54: 89 */       row.setR(nextRowNum);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public XSSFSheet getSheet()
/*  59:    */   {
/*  60:100 */     return this._sheet;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Iterator<Cell> cellIterator()
/*  64:    */   {
/*  65:117 */     return this._cells.values().iterator();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Iterator<Cell> iterator()
/*  69:    */   {
/*  70:132 */     return cellIterator();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int compareTo(XSSFRow other)
/*  74:    */   {
/*  75:158 */     if (getSheet() != other.getSheet()) {
/*  76:159 */       throw new IllegalArgumentException("The compared rows must belong to the same sheet");
/*  77:    */     }
/*  78:162 */     Integer thisRow = Integer.valueOf(getRowNum());
/*  79:163 */     Integer otherRow = Integer.valueOf(other.getRowNum());
/*  80:164 */     return thisRow.compareTo(otherRow);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public boolean equals(Object obj)
/*  84:    */   {
/*  85:170 */     if (!(obj instanceof XSSFRow)) {
/*  86:172 */       return false;
/*  87:    */     }
/*  88:174 */     XSSFRow other = (XSSFRow)obj;
/*  89:    */     
/*  90:176 */     return (getRowNum() == other.getRowNum()) && (getSheet() == other.getSheet());
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int hashCode()
/*  94:    */   {
/*  95:182 */     return this._row.hashCode();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public XSSFCell createCell(int columnIndex)
/*  99:    */   {
/* 100:198 */     return createCell(columnIndex, CellType.BLANK);
/* 101:    */   }
/* 102:    */   
/* 103:    */   /**
/* 104:    */    * @deprecated
/* 105:    */    */
/* 106:    */   public XSSFCell createCell(int columnIndex, int type)
/* 107:    */   {
/* 108:219 */     return createCell(columnIndex, CellType.forInt(type));
/* 109:    */   }
/* 110:    */   
/* 111:    */   public XSSFCell createCell(int columnIndex, CellType type)
/* 112:    */   {
/* 113:233 */     Integer colI = new Integer(columnIndex);
/* 114:    */     
/* 115:235 */     XSSFCell prev = (XSSFCell)this._cells.get(colI);
/* 116:    */     CTCell ctCell;
/* 117:236 */     if (prev != null)
/* 118:    */     {
/* 119:237 */       CTCell ctCell = prev.getCTCell();
/* 120:238 */       ctCell.set(CTCell.Factory.newInstance());
/* 121:    */     }
/* 122:    */     else
/* 123:    */     {
/* 124:240 */       ctCell = this._row.addNewC();
/* 125:    */     }
/* 126:242 */     XSSFCell xcell = new XSSFCell(this, ctCell);
/* 127:243 */     xcell.setCellNum(columnIndex);
/* 128:244 */     if (type != CellType.BLANK) {
/* 129:245 */       xcell.setCellType(type);
/* 130:    */     }
/* 131:247 */     this._cells.put(colI, xcell);
/* 132:248 */     return xcell;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public XSSFCell getCell(int cellnum)
/* 136:    */   {
/* 137:259 */     return getCell(cellnum, this._sheet.getWorkbook().getMissingCellPolicy());
/* 138:    */   }
/* 139:    */   
/* 140:    */   public XSSFCell getCell(int cellnum, Row.MissingCellPolicy policy)
/* 141:    */   {
/* 142:270 */     if (cellnum < 0) {
/* 143:270 */       throw new IllegalArgumentException("Cell index must be >= 0");
/* 144:    */     }
/* 145:273 */     Integer colI = new Integer(cellnum);
/* 146:274 */     XSSFCell cell = (XSSFCell)this._cells.get(colI);
/* 147:275 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[policy.ordinal()])
/* 148:    */     {
/* 149:    */     case 1: 
/* 150:277 */       return cell;
/* 151:    */     case 2: 
/* 152:279 */       boolean isBlank = (cell != null) && (cell.getCellTypeEnum() == CellType.BLANK);
/* 153:280 */       return isBlank ? null : cell;
/* 154:    */     case 3: 
/* 155:282 */       return cell == null ? createCell(cellnum, CellType.BLANK) : cell;
/* 156:    */     }
/* 157:284 */     throw new IllegalArgumentException("Illegal policy " + policy);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public short getFirstCellNum()
/* 161:    */   {
/* 162:296 */     return (short)(this._cells.size() == 0 ? -1 : ((Integer)this._cells.firstKey()).intValue());
/* 163:    */   }
/* 164:    */   
/* 165:    */   public short getLastCellNum()
/* 166:    */   {
/* 167:320 */     return (short)(this._cells.size() == 0 ? -1 : ((Integer)this._cells.lastKey()).intValue() + 1);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public short getHeight()
/* 171:    */   {
/* 172:331 */     return (short)(int)(getHeightInPoints() * 20.0F);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public float getHeightInPoints()
/* 176:    */   {
/* 177:343 */     if (this._row.isSetHt()) {
/* 178:344 */       return (float)this._row.getHt();
/* 179:    */     }
/* 180:346 */     return this._sheet.getDefaultRowHeightInPoints();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setHeight(short height)
/* 184:    */   {
/* 185:356 */     if (height == -1)
/* 186:    */     {
/* 187:357 */       if (this._row.isSetHt()) {
/* 188:357 */         this._row.unsetHt();
/* 189:    */       }
/* 190:358 */       if (this._row.isSetCustomHeight()) {
/* 191:358 */         this._row.unsetCustomHeight();
/* 192:    */       }
/* 193:    */     }
/* 194:    */     else
/* 195:    */     {
/* 196:360 */       this._row.setHt(height / 20.0D);
/* 197:361 */       this._row.setCustomHeight(true);
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setHeightInPoints(float height)
/* 202:    */   {
/* 203:373 */     setHeight((short)(int)(height == -1.0F ? -1.0F : height * 20.0F));
/* 204:    */   }
/* 205:    */   
/* 206:    */   public int getPhysicalNumberOfCells()
/* 207:    */   {
/* 208:384 */     return this._cells.size();
/* 209:    */   }
/* 210:    */   
/* 211:    */   public int getRowNum()
/* 212:    */   {
/* 213:394 */     return (int)(this._row.getR() - 1L);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setRowNum(int rowIndex)
/* 217:    */   {
/* 218:405 */     int maxrow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
/* 219:406 */     if ((rowIndex < 0) || (rowIndex > maxrow)) {
/* 220:407 */       throw new IllegalArgumentException("Invalid row number (" + rowIndex + ") outside allowable range (0.." + maxrow + ")");
/* 221:    */     }
/* 222:410 */     this._row.setR(rowIndex + 1);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public boolean getZeroHeight()
/* 226:    */   {
/* 227:420 */     return this._row.getHidden();
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void setZeroHeight(boolean height)
/* 231:    */   {
/* 232:430 */     this._row.setHidden(height);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public boolean isFormatted()
/* 236:    */   {
/* 237:441 */     return this._row.isSetS();
/* 238:    */   }
/* 239:    */   
/* 240:    */   public XSSFCellStyle getRowStyle()
/* 241:    */   {
/* 242:450 */     if (!isFormatted()) {
/* 243:450 */       return null;
/* 244:    */     }
/* 245:452 */     StylesTable stylesSource = getSheet().getWorkbook().getStylesSource();
/* 246:453 */     if (stylesSource.getNumCellStyles() > 0) {
/* 247:454 */       return stylesSource.getStyleAt((int)this._row.getS());
/* 248:    */     }
/* 249:456 */     return null;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void setRowStyle(CellStyle style)
/* 253:    */   {
/* 254:467 */     if (style == null)
/* 255:    */     {
/* 256:468 */       if (this._row.isSetS())
/* 257:    */       {
/* 258:469 */         this._row.unsetS();
/* 259:470 */         this._row.unsetCustomFormat();
/* 260:    */       }
/* 261:    */     }
/* 262:    */     else
/* 263:    */     {
/* 264:473 */       StylesTable styleSource = getSheet().getWorkbook().getStylesSource();
/* 265:    */       
/* 266:475 */       XSSFCellStyle xStyle = (XSSFCellStyle)style;
/* 267:476 */       xStyle.verifyBelongsToStylesSource(styleSource);
/* 268:    */       
/* 269:478 */       long idx = styleSource.putStyle(xStyle);
/* 270:479 */       this._row.setS(idx);
/* 271:480 */       this._row.setCustomFormat(true);
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void removeCell(Cell cell)
/* 276:    */   {
/* 277:491 */     if (cell.getRow() != this) {
/* 278:492 */       throw new IllegalArgumentException("Specified cell does not belong to this row");
/* 279:    */     }
/* 280:495 */     XSSFCell xcell = (XSSFCell)cell;
/* 281:496 */     if (xcell.isPartOfArrayFormulaGroup()) {
/* 282:497 */       xcell.notifyArrayFormulaChanging();
/* 283:    */     }
/* 284:499 */     if (cell.getCellTypeEnum() == CellType.FORMULA) {
/* 285:500 */       this._sheet.getWorkbook().onDeleteFormula(xcell);
/* 286:    */     }
/* 287:503 */     Integer colI = new Integer(cell.getColumnIndex());
/* 288:504 */     this._cells.remove(colI);
/* 289:    */   }
/* 290:    */   
/* 291:    */   @Internal
/* 292:    */   public CTRow getCTRow()
/* 293:    */   {
/* 294:514 */     return this._row;
/* 295:    */   }
/* 296:    */   
/* 297:    */   protected void onDocumentWrite()
/* 298:    */   {
/* 299:524 */     boolean isOrdered = true;
/* 300:525 */     CTCell[] cArray = this._row.getCArray();
/* 301:    */     int i;
/* 302:526 */     if (cArray.length != this._cells.size())
/* 303:    */     {
/* 304:527 */       isOrdered = false;
/* 305:    */     }
/* 306:    */     else
/* 307:    */     {
/* 308:529 */       i = 0;
/* 309:530 */       for (XSSFCell cell : this._cells.values())
/* 310:    */       {
/* 311:531 */         CTCell c1 = cell.getCTCell();
/* 312:532 */         CTCell c2 = cArray[(i++)];
/* 313:    */         
/* 314:534 */         String r1 = c1.getR();
/* 315:535 */         String r2 = c2.getR();
/* 316:536 */         if (r1 == null ? r2 != null : !r1.equals(r2))
/* 317:    */         {
/* 318:537 */           isOrdered = false;
/* 319:538 */           break;
/* 320:    */         }
/* 321:    */       }
/* 322:    */     }
/* 323:543 */     if (!isOrdered)
/* 324:    */     {
/* 325:544 */       cArray = new CTCell[this._cells.size()];
/* 326:545 */       int i = 0;
/* 327:546 */       for (XSSFCell xssfCell : this._cells.values())
/* 328:    */       {
/* 329:547 */         cArray[i] = ((CTCell)xssfCell.getCTCell().copy());
/* 330:    */         
/* 331:    */ 
/* 332:    */ 
/* 333:    */ 
/* 334:    */ 
/* 335:    */ 
/* 336:554 */         xssfCell.setCTCell(cArray[i]);
/* 337:555 */         i++;
/* 338:    */       }
/* 339:558 */       this._row.setCArray(cArray);
/* 340:    */     }
/* 341:    */   }
/* 342:    */   
/* 343:    */   public String toString()
/* 344:    */   {
/* 345:567 */     return this._row.toString();
/* 346:    */   }
/* 347:    */   
/* 348:    */   protected void shift(int n)
/* 349:    */   {
/* 350:576 */     int rownum = getRowNum() + n;
/* 351:577 */     CalculationChain calcChain = this._sheet.getWorkbook().getCalculationChain();
/* 352:578 */     int sheetId = (int)this._sheet.sheet.getSheetId();
/* 353:579 */     String msg = "Row[rownum=" + getRowNum() + "] contains cell(s) included in a multi-cell array formula. " + "You cannot change part of an array.";
/* 354:581 */     for (Cell c : this)
/* 355:    */     {
/* 356:582 */       XSSFCell cell = (XSSFCell)c;
/* 357:583 */       if (cell.isPartOfArrayFormulaGroup()) {
/* 358:584 */         cell.notifyArrayFormulaChanging(msg);
/* 359:    */       }
/* 360:588 */       if (calcChain != null) {
/* 361:588 */         calcChain.removeItem(sheetId, cell.getReference());
/* 362:    */       }
/* 363:590 */       CTCell ctCell = cell.getCTCell();
/* 364:591 */       String r = new CellReference(rownum, cell.getColumnIndex()).formatAsString();
/* 365:592 */       ctCell.setR(r);
/* 366:    */     }
/* 367:594 */     setRowNum(rownum);
/* 368:    */   }
/* 369:    */   
/* 370:    */   public void copyRowFrom(Row srcRow, CellCopyPolicy policy)
/* 371:    */   {
/* 372:608 */     if (srcRow == null)
/* 373:    */     {
/* 374:610 */       for (Cell destCell : this)
/* 375:    */       {
/* 376:611 */         XSSFCell srcCell = null;
/* 377:    */         
/* 378:613 */         ((XSSFCell)destCell).copyCellFrom(srcCell, policy);
/* 379:    */       }
/* 380:616 */       if (policy.isCopyMergedRegions())
/* 381:    */       {
/* 382:618 */         int destRowNum = getRowNum();
/* 383:619 */         int index = 0;
/* 384:620 */         Set<Integer> indices = new HashSet();
/* 385:621 */         for (CellRangeAddress destRegion : getSheet().getMergedRegions())
/* 386:    */         {
/* 387:622 */           if ((destRowNum == destRegion.getFirstRow()) && (destRowNum == destRegion.getLastRow())) {
/* 388:623 */             indices.add(Integer.valueOf(index));
/* 389:    */           }
/* 390:625 */           index++;
/* 391:    */         }
/* 392:627 */         getSheet().removeMergedRegions(indices);
/* 393:    */       }
/* 394:630 */       if (policy.isCopyRowHeight()) {
/* 395:632 */         setHeight((short)-1);
/* 396:    */       }
/* 397:    */     }
/* 398:    */     else
/* 399:    */     {
/* 400:637 */       for (Cell c : srcRow)
/* 401:    */       {
/* 402:638 */         XSSFCell srcCell = (XSSFCell)c;
/* 403:639 */         XSSFCell destCell = createCell(srcCell.getColumnIndex(), srcCell.getCellTypeEnum());
/* 404:640 */         destCell.copyCellFrom(srcCell, policy);
/* 405:    */       }
/* 406:643 */       XSSFRowShifter rowShifter = new XSSFRowShifter(this._sheet);
/* 407:644 */       int sheetIndex = this._sheet.getWorkbook().getSheetIndex(this._sheet);
/* 408:645 */       String sheetName = this._sheet.getWorkbook().getSheetName(sheetIndex);
/* 409:646 */       int srcRowNum = srcRow.getRowNum();
/* 410:647 */       int destRowNum = getRowNum();
/* 411:648 */       int rowDifference = destRowNum - srcRowNum;
/* 412:649 */       FormulaShifter shifter = FormulaShifter.createForRowCopy(sheetIndex, sheetName, srcRowNum, srcRowNum, rowDifference, SpreadsheetVersion.EXCEL2007);
/* 413:650 */       rowShifter.updateRowFormulas(this, shifter);
/* 414:654 */       if (policy.isCopyMergedRegions()) {
/* 415:655 */         for (CellRangeAddress srcRegion : srcRow.getSheet().getMergedRegions()) {
/* 416:656 */           if ((srcRowNum == srcRegion.getFirstRow()) && (srcRowNum == srcRegion.getLastRow()))
/* 417:    */           {
/* 418:657 */             CellRangeAddress destRegion = srcRegion.copy();
/* 419:658 */             destRegion.setFirstRow(destRowNum);
/* 420:659 */             destRegion.setLastRow(destRowNum);
/* 421:660 */             getSheet().addMergedRegion(destRegion);
/* 422:    */           }
/* 423:    */         }
/* 424:    */       }
/* 425:665 */       if (policy.isCopyRowHeight()) {
/* 426:666 */         setHeight(srcRow.getHeight());
/* 427:    */       }
/* 428:    */     }
/* 429:    */   }
/* 430:    */   
/* 431:    */   public int getOutlineLevel()
/* 432:    */   {
/* 433:673 */     return this._row.getOutlineLevel();
/* 434:    */   }
/* 435:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFRow
 * JD-Core Version:    0.7.0.1
 */