/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.NoSuchElementException;
/*   5:    */ import org.apache.poi.hssf.model.InternalSheet;
/*   6:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   7:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   8:    */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   9:    */ import org.apache.poi.hssf.record.RowRecord;
/*  10:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  11:    */ import org.apache.poi.ss.usermodel.Cell;
/*  12:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  13:    */ import org.apache.poi.ss.usermodel.CellType;
/*  14:    */ import org.apache.poi.ss.usermodel.Row;
/*  15:    */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*  16:    */ import org.apache.poi.util.Configurator;
/*  17:    */ 
/*  18:    */ public final class HSSFRow
/*  19:    */   implements Row, Comparable<HSSFRow>
/*  20:    */ {
/*  21: 41 */   public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFRow.ColInitialCapacity", 5);
/*  22:    */   private int rowNum;
/*  23:    */   private HSSFCell[] cells;
/*  24:    */   private final RowRecord row;
/*  25:    */   private final HSSFWorkbook book;
/*  26:    */   private final HSSFSheet sheet;
/*  27:    */   
/*  28:    */   HSSFRow(HSSFWorkbook book, HSSFSheet sheet, int rowNum)
/*  29:    */   {
/*  30: 70 */     this(book, sheet, new RowRecord(rowNum));
/*  31:    */   }
/*  32:    */   
/*  33:    */   HSSFRow(HSSFWorkbook book, HSSFSheet sheet, RowRecord record)
/*  34:    */   {
/*  35: 83 */     this.book = book;
/*  36: 84 */     this.sheet = sheet;
/*  37: 85 */     this.row = record;
/*  38: 86 */     setRowNum(record.getRowNumber());
/*  39:    */     
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43: 91 */     this.cells = new HSSFCell[record.getLastCol() + INITIAL_CAPACITY];
/*  44:    */     
/*  45:    */ 
/*  46:    */ 
/*  47: 95 */     record.setEmpty();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public HSSFCell createCell(int column)
/*  51:    */   {
/*  52:114 */     return createCell(column, CellType.BLANK);
/*  53:    */   }
/*  54:    */   
/*  55:    */   /**
/*  56:    */    * @deprecated
/*  57:    */    */
/*  58:    */   public HSSFCell createCell(int columnIndex, int type)
/*  59:    */   {
/*  60:135 */     return createCell(columnIndex, CellType.forInt(type));
/*  61:    */   }
/*  62:    */   
/*  63:    */   public HSSFCell createCell(int columnIndex, CellType type)
/*  64:    */   {
/*  65:154 */     short shortCellNum = (short)columnIndex;
/*  66:155 */     if (columnIndex > 32767) {
/*  67:156 */       shortCellNum = (short)(65535 - columnIndex);
/*  68:    */     }
/*  69:159 */     HSSFCell cell = new HSSFCell(this.book, this.sheet, getRowNum(), shortCellNum, type);
/*  70:160 */     addCell(cell);
/*  71:161 */     this.sheet.getSheet().addValueRecord(getRowNum(), cell.getCellValueRecord());
/*  72:162 */     return cell;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void removeCell(Cell cell)
/*  76:    */   {
/*  77:171 */     if (cell == null) {
/*  78:172 */       throw new IllegalArgumentException("cell must not be null");
/*  79:    */     }
/*  80:174 */     removeCell((HSSFCell)cell, true);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void removeCell(HSSFCell cell, boolean alsoRemoveRecords)
/*  84:    */   {
/*  85:178 */     int column = cell.getColumnIndex();
/*  86:179 */     if (column < 0) {
/*  87:180 */       throw new RuntimeException("Negative cell indexes not allowed");
/*  88:    */     }
/*  89:182 */     if ((column >= this.cells.length) || (cell != this.cells[column])) {
/*  90:183 */       throw new RuntimeException("Specified cell is not from this row");
/*  91:    */     }
/*  92:185 */     if (cell.isPartOfArrayFormulaGroup()) {
/*  93:186 */       cell.notifyArrayFormulaChanging();
/*  94:    */     }
/*  95:189 */     this.cells[column] = null;
/*  96:191 */     if (alsoRemoveRecords)
/*  97:    */     {
/*  98:192 */       CellValueRecordInterface cval = cell.getCellValueRecord();
/*  99:193 */       this.sheet.getSheet().removeValueRecord(getRowNum(), cval);
/* 100:    */     }
/* 101:195 */     if (cell.getColumnIndex() + 1 == this.row.getLastCol()) {
/* 102:196 */       this.row.setLastCol(calculateNewLastCellPlusOne(this.row.getLastCol()));
/* 103:    */     }
/* 104:198 */     if (cell.getColumnIndex() == this.row.getFirstCol()) {
/* 105:199 */       this.row.setFirstCol(calculateNewFirstCell(this.row.getFirstCol()));
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected void removeAllCells()
/* 110:    */   {
/* 111:208 */     for (HSSFCell cell : this.cells) {
/* 112:209 */       if (cell != null) {
/* 113:210 */         removeCell(cell, true);
/* 114:    */       }
/* 115:    */     }
/* 116:213 */     this.cells = new HSSFCell[INITIAL_CAPACITY];
/* 117:    */   }
/* 118:    */   
/* 119:    */   HSSFCell createCellFromRecord(CellValueRecordInterface cell)
/* 120:    */   {
/* 121:223 */     HSSFCell hcell = new HSSFCell(this.book, this.sheet, cell);
/* 122:    */     
/* 123:225 */     addCell(hcell);
/* 124:226 */     int colIx = cell.getColumn();
/* 125:227 */     if (this.row.isEmpty())
/* 126:    */     {
/* 127:228 */       this.row.setFirstCol(colIx);
/* 128:229 */       this.row.setLastCol(colIx + 1);
/* 129:    */     }
/* 130:231 */     else if (colIx < this.row.getFirstCol())
/* 131:    */     {
/* 132:232 */       this.row.setFirstCol(colIx);
/* 133:    */     }
/* 134:233 */     else if (colIx > this.row.getLastCol())
/* 135:    */     {
/* 136:234 */       this.row.setLastCol(colIx + 1);
/* 137:    */     }
/* 138:240 */     return hcell;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setRowNum(int rowIndex)
/* 142:    */   {
/* 143:250 */     int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/* 144:251 */     if ((rowIndex < 0) || (rowIndex > maxrow)) {
/* 145:252 */       throw new IllegalArgumentException("Invalid row number (" + rowIndex + ") outside allowable range (0.." + maxrow + ")");
/* 146:    */     }
/* 147:255 */     this.rowNum = rowIndex;
/* 148:256 */     if (this.row != null) {
/* 149:257 */       this.row.setRowNumber(rowIndex);
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public int getRowNum()
/* 154:    */   {
/* 155:268 */     return this.rowNum;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public HSSFSheet getSheet()
/* 159:    */   {
/* 160:279 */     return this.sheet;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int getOutlineLevel()
/* 164:    */   {
/* 165:289 */     return this.row.getOutlineLevel();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void moveCell(HSSFCell cell, short newColumn)
/* 169:    */   {
/* 170:300 */     if ((this.cells.length > newColumn) && (this.cells[newColumn] != null)) {
/* 171:301 */       throw new IllegalArgumentException("Asked to move cell to column " + newColumn + " but there's already a cell there");
/* 172:    */     }
/* 173:305 */     if (!this.cells[cell.getColumnIndex()].equals(cell)) {
/* 174:306 */       throw new IllegalArgumentException("Asked to move a cell, but it didn't belong to our row");
/* 175:    */     }
/* 176:311 */     removeCell(cell, false);
/* 177:312 */     cell.updateCellNum(newColumn);
/* 178:313 */     addCell(cell);
/* 179:    */   }
/* 180:    */   
/* 181:    */   private void addCell(HSSFCell cell)
/* 182:    */   {
/* 183:321 */     int column = cell.getColumnIndex();
/* 184:323 */     if (column >= this.cells.length)
/* 185:    */     {
/* 186:324 */       HSSFCell[] oldCells = this.cells;
/* 187:    */       
/* 188:326 */       int newSize = oldCells.length * 3 / 2 + 1;
/* 189:327 */       if (newSize < column + 1) {
/* 190:328 */         newSize = column + INITIAL_CAPACITY;
/* 191:    */       }
/* 192:330 */       this.cells = new HSSFCell[newSize];
/* 193:331 */       System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
/* 194:    */     }
/* 195:333 */     this.cells[column] = cell;
/* 196:336 */     if ((this.row.isEmpty()) || (column < this.row.getFirstCol())) {
/* 197:337 */       this.row.setFirstCol((short)column);
/* 198:    */     }
/* 199:340 */     if ((this.row.isEmpty()) || (column >= this.row.getLastCol())) {
/* 200:341 */       this.row.setLastCol((short)(column + 1));
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   private HSSFCell retrieveCell(int cellIndex)
/* 205:    */   {
/* 206:355 */     if ((cellIndex < 0) || (cellIndex >= this.cells.length)) {
/* 207:356 */       return null;
/* 208:    */     }
/* 209:358 */     return this.cells[cellIndex];
/* 210:    */   }
/* 211:    */   
/* 212:    */   public HSSFCell getCell(int cellnum)
/* 213:    */   {
/* 214:372 */     return getCell(cellnum, this.book.getMissingCellPolicy());
/* 215:    */   }
/* 216:    */   
/* 217:    */   public HSSFCell getCell(int cellnum, MissingCellPolicy policy)
/* 218:    */   {
/* 219:386 */     HSSFCell cell = retrieveCell(cellnum);
/* 220:387 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[policy.ordinal()])
/* 221:    */     {
/* 222:    */     case 1: 
/* 223:389 */       return cell;
/* 224:    */     case 2: 
/* 225:391 */       boolean isBlank = (cell != null) && (cell.getCellTypeEnum() == CellType.BLANK);
/* 226:392 */       return isBlank ? null : cell;
/* 227:    */     case 3: 
/* 228:394 */       return cell == null ? createCell(cellnum, CellType.BLANK) : cell;
/* 229:    */     }
/* 230:396 */     throw new IllegalArgumentException("Illegal policy " + policy);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public short getFirstCellNum()
/* 234:    */   {
/* 235:406 */     if (this.row.isEmpty()) {
/* 236:407 */       return -1;
/* 237:    */     }
/* 238:409 */     return (short)this.row.getFirstCol();
/* 239:    */   }
/* 240:    */   
/* 241:    */   public short getLastCellNum()
/* 242:    */   {
/* 243:433 */     if (this.row.isEmpty()) {
/* 244:434 */       return -1;
/* 245:    */     }
/* 246:436 */     return (short)this.row.getLastCol();
/* 247:    */   }
/* 248:    */   
/* 249:    */   public int getPhysicalNumberOfCells()
/* 250:    */   {
/* 251:449 */     int count = 0;
/* 252:450 */     for (HSSFCell cell : this.cells) {
/* 253:451 */       if (cell != null) {
/* 254:451 */         count++;
/* 255:    */       }
/* 256:    */     }
/* 257:453 */     return count;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void setHeight(short height)
/* 261:    */   {
/* 262:465 */     if (height == -1)
/* 263:    */     {
/* 264:466 */       this.row.setHeight((short)-32513);
/* 265:467 */       this.row.setBadFontHeight(false);
/* 266:    */     }
/* 267:    */     else
/* 268:    */     {
/* 269:469 */       this.row.setBadFontHeight(true);
/* 270:470 */       this.row.setHeight(height);
/* 271:    */     }
/* 272:    */   }
/* 273:    */   
/* 274:    */   public void setZeroHeight(boolean zHeight)
/* 275:    */   {
/* 276:480 */     this.row.setZeroHeight(zHeight);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public boolean getZeroHeight()
/* 280:    */   {
/* 281:489 */     return this.row.getZeroHeight();
/* 282:    */   }
/* 283:    */   
/* 284:    */   public void setHeightInPoints(float height)
/* 285:    */   {
/* 286:500 */     if (height == -1.0F)
/* 287:    */     {
/* 288:501 */       this.row.setHeight((short)-32513);
/* 289:502 */       this.row.setBadFontHeight(false);
/* 290:    */     }
/* 291:    */     else
/* 292:    */     {
/* 293:504 */       this.row.setBadFontHeight(true);
/* 294:505 */       this.row.setHeight((short)(int)(height * 20.0F));
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   public short getHeight()
/* 299:    */   {
/* 300:517 */     short height = this.row.getHeight();
/* 301:521 */     if ((height & 0x8000) != 0) {
/* 302:521 */       height = this.sheet.getSheet().getDefaultRowHeight();
/* 303:    */     } else {
/* 304:522 */       height = (short)(height & 0x7FFF);
/* 305:    */     }
/* 306:524 */     return height;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public float getHeightInPoints()
/* 310:    */   {
/* 311:535 */     return getHeight() / 20.0F;
/* 312:    */   }
/* 313:    */   
/* 314:    */   protected RowRecord getRowRecord()
/* 315:    */   {
/* 316:547 */     return this.row;
/* 317:    */   }
/* 318:    */   
/* 319:    */   private int calculateNewLastCellPlusOne(int lastcell)
/* 320:    */   {
/* 321:555 */     int cellIx = lastcell - 1;
/* 322:556 */     HSSFCell r = retrieveCell(cellIx);
/* 323:558 */     while (r == null)
/* 324:    */     {
/* 325:559 */       if (cellIx < 0) {
/* 326:560 */         return 0;
/* 327:    */       }
/* 328:562 */       r = retrieveCell(--cellIx);
/* 329:    */     }
/* 330:564 */     return cellIx + 1;
/* 331:    */   }
/* 332:    */   
/* 333:    */   private int calculateNewFirstCell(int firstcell)
/* 334:    */   {
/* 335:572 */     int cellIx = firstcell + 1;
/* 336:573 */     HSSFCell r = retrieveCell(cellIx);
/* 337:575 */     while (r == null)
/* 338:    */     {
/* 339:576 */       if (cellIx <= this.cells.length) {
/* 340:577 */         return 0;
/* 341:    */       }
/* 342:579 */       r = retrieveCell(++cellIx);
/* 343:    */     }
/* 344:581 */     return cellIx;
/* 345:    */   }
/* 346:    */   
/* 347:    */   public boolean isFormatted()
/* 348:    */   {
/* 349:591 */     return this.row.getFormatted();
/* 350:    */   }
/* 351:    */   
/* 352:    */   public HSSFCellStyle getRowStyle()
/* 353:    */   {
/* 354:600 */     if (!isFormatted()) {
/* 355:600 */       return null;
/* 356:    */     }
/* 357:601 */     short styleIndex = this.row.getXFIndex();
/* 358:602 */     ExtendedFormatRecord xf = this.book.getWorkbook().getExFormatAt(styleIndex);
/* 359:603 */     return new HSSFCellStyle(styleIndex, xf, this.book);
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void setRowStyle(HSSFCellStyle style)
/* 363:    */   {
/* 364:609 */     this.row.setFormatted(true);
/* 365:610 */     this.row.setXFIndex(style.getIndex());
/* 366:    */   }
/* 367:    */   
/* 368:    */   public void setRowStyle(CellStyle style)
/* 369:    */   {
/* 370:617 */     setRowStyle((HSSFCellStyle)style);
/* 371:    */   }
/* 372:    */   
/* 373:    */   public Iterator<Cell> cellIterator()
/* 374:    */   {
/* 375:631 */     return new CellIterator();
/* 376:    */   }
/* 377:    */   
/* 378:    */   public Iterator<Cell> iterator()
/* 379:    */   {
/* 380:639 */     return cellIterator();
/* 381:    */   }
/* 382:    */   
/* 383:    */   private class CellIterator
/* 384:    */     implements Iterator<Cell>
/* 385:    */   {
/* 386:646 */     int thisId = -1;
/* 387:647 */     int nextId = -1;
/* 388:    */     
/* 389:    */     public CellIterator()
/* 390:    */     {
/* 391:651 */       findNext();
/* 392:    */     }
/* 393:    */     
/* 394:    */     public boolean hasNext()
/* 395:    */     {
/* 396:656 */       return this.nextId < HSSFRow.this.cells.length;
/* 397:    */     }
/* 398:    */     
/* 399:    */     public Cell next()
/* 400:    */     {
/* 401:661 */       if (!hasNext()) {
/* 402:662 */         throw new NoSuchElementException("At last element");
/* 403:    */       }
/* 404:663 */       HSSFCell cell = HSSFRow.this.cells[this.nextId];
/* 405:664 */       this.thisId = this.nextId;
/* 406:665 */       findNext();
/* 407:666 */       return cell;
/* 408:    */     }
/* 409:    */     
/* 410:    */     public void remove()
/* 411:    */     {
/* 412:671 */       if (this.thisId == -1) {
/* 413:672 */         throw new IllegalStateException("remove() called before next()");
/* 414:    */       }
/* 415:673 */       HSSFRow.this.cells[this.thisId] = null;
/* 416:    */     }
/* 417:    */     
/* 418:    */     private void findNext()
/* 419:    */     {
/* 420:678 */       for (int i = this.nextId + 1; i < HSSFRow.this.cells.length; i++) {
/* 421:681 */         if (HSSFRow.this.cells[i] != null) {
/* 422:    */           break;
/* 423:    */         }
/* 424:    */       }
/* 425:683 */       this.nextId = i;
/* 426:    */     }
/* 427:    */   }
/* 428:    */   
/* 429:    */   public int compareTo(HSSFRow other)
/* 430:    */   {
/* 431:712 */     if (getSheet() != other.getSheet()) {
/* 432:713 */       throw new IllegalArgumentException("The compared rows must belong to the same sheet");
/* 433:    */     }
/* 434:716 */     Integer thisRow = Integer.valueOf(getRowNum());
/* 435:717 */     Integer otherRow = Integer.valueOf(other.getRowNum());
/* 436:718 */     return thisRow.compareTo(otherRow);
/* 437:    */   }
/* 438:    */   
/* 439:    */   public boolean equals(Object obj)
/* 440:    */   {
/* 441:724 */     if (!(obj instanceof HSSFRow)) {
/* 442:726 */       return false;
/* 443:    */     }
/* 444:728 */     HSSFRow other = (HSSFRow)obj;
/* 445:    */     
/* 446:730 */     return (getRowNum() == other.getRowNum()) && (getSheet() == other.getSheet());
/* 447:    */   }
/* 448:    */   
/* 449:    */   public int hashCode()
/* 450:    */   {
/* 451:736 */     return this.row.hashCode();
/* 452:    */   }
/* 453:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFRow

 * JD-Core Version:    0.7.0.1

 */