/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.TreeMap;
/*   9:    */ import org.apache.poi.hssf.model.RecordStream;
/*  10:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*  11:    */ import org.apache.poi.hssf.record.DBCellRecord.Builder;
/*  12:    */ import org.apache.poi.hssf.record.DimensionsRecord;
/*  13:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  14:    */ import org.apache.poi.hssf.record.IndexRecord;
/*  15:    */ import org.apache.poi.hssf.record.MulBlankRecord;
/*  16:    */ import org.apache.poi.hssf.record.Record;
/*  17:    */ import org.apache.poi.hssf.record.RowRecord;
/*  18:    */ import org.apache.poi.hssf.record.UnknownRecord;
/*  19:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  20:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*  21:    */ 
/*  22:    */ public final class RowRecordsAggregate
/*  23:    */   extends RecordAggregate
/*  24:    */ {
/*  25: 37 */   private int _firstrow = -1;
/*  26: 38 */   private int _lastrow = -1;
/*  27:    */   private final Map<Integer, RowRecord> _rowRecords;
/*  28:    */   private final ValueRecordsAggregate _valuesAgg;
/*  29:    */   private final List<Record> _unknownRecords;
/*  30:    */   private final SharedValueManager _sharedValueManager;
/*  31: 46 */   private RowRecord[] _rowRecordValues = null;
/*  32:    */   
/*  33:    */   public RowRecordsAggregate()
/*  34:    */   {
/*  35: 50 */     this(SharedValueManager.createEmpty());
/*  36:    */   }
/*  37:    */   
/*  38:    */   private RowRecordsAggregate(SharedValueManager svm)
/*  39:    */   {
/*  40: 53 */     if (svm == null) {
/*  41: 54 */       throw new IllegalArgumentException("SharedValueManager must be provided.");
/*  42:    */     }
/*  43: 56 */     this._rowRecords = new TreeMap();
/*  44: 57 */     this._valuesAgg = new ValueRecordsAggregate();
/*  45: 58 */     this._unknownRecords = new ArrayList();
/*  46: 59 */     this._sharedValueManager = svm;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public RowRecordsAggregate(RecordStream rs, SharedValueManager svm)
/*  50:    */   {
/*  51: 69 */     this(svm);
/*  52: 70 */     while (rs.hasNext())
/*  53:    */     {
/*  54: 71 */       Record rec = rs.getNext();
/*  55: 72 */       switch (rec.getSid())
/*  56:    */       {
/*  57:    */       case 520: 
/*  58: 74 */         insertRow((RowRecord)rec);
/*  59: 75 */         break;
/*  60:    */       case 81: 
/*  61: 77 */         addUnknownRecord(rec);
/*  62: 78 */         break;
/*  63:    */       case 215: 
/*  64:    */         break;
/*  65:    */       default: 
/*  66: 84 */         if ((rec instanceof UnknownRecord))
/*  67:    */         {
/*  68: 86 */           addUnknownRecord(rec);
/*  69: 87 */           while (rs.peekNextSid() == 60) {
/*  70: 88 */             addUnknownRecord(rs.getNext());
/*  71:    */           }
/*  72:    */         }
/*  73: 92 */         else if ((rec instanceof MulBlankRecord))
/*  74:    */         {
/*  75: 93 */           this._valuesAgg.addMultipleBlanks((MulBlankRecord)rec);
/*  76:    */         }
/*  77:    */         else
/*  78:    */         {
/*  79: 96 */           if (!(rec instanceof CellValueRecordInterface)) {
/*  80: 97 */             throw new RuntimeException("Unexpected record type (" + rec.getClass().getName() + ")");
/*  81:    */           }
/*  82: 99 */           this._valuesAgg.construct((CellValueRecordInterface)rec, rs, svm);
/*  83:    */         }
/*  84:    */         break;
/*  85:    */       }
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   private void addUnknownRecord(Record rec)
/*  90:    */   {
/*  91:113 */     this._unknownRecords.add(rec);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void insertRow(RowRecord row)
/*  95:    */   {
/*  96:117 */     this._rowRecords.put(Integer.valueOf(row.getRowNumber()), row);
/*  97:    */     
/*  98:119 */     this._rowRecordValues = null;
/*  99:120 */     if ((row.getRowNumber() < this._firstrow) || (this._firstrow == -1)) {
/* 100:121 */       this._firstrow = row.getRowNumber();
/* 101:    */     }
/* 102:123 */     if ((row.getRowNumber() > this._lastrow) || (this._lastrow == -1)) {
/* 103:124 */       this._lastrow = row.getRowNumber();
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void removeRow(RowRecord row)
/* 108:    */   {
/* 109:129 */     int rowIndex = row.getRowNumber();
/* 110:130 */     this._valuesAgg.removeAllCellsValuesForRow(rowIndex);
/* 111:131 */     Integer key = Integer.valueOf(rowIndex);
/* 112:132 */     RowRecord rr = (RowRecord)this._rowRecords.remove(key);
/* 113:133 */     if (rr == null) {
/* 114:134 */       throw new RuntimeException("Invalid row index (" + key.intValue() + ")");
/* 115:    */     }
/* 116:136 */     if (row != rr)
/* 117:    */     {
/* 118:137 */       this._rowRecords.put(key, rr);
/* 119:138 */       throw new RuntimeException("Attempt to remove row that does not belong to this sheet");
/* 120:    */     }
/* 121:142 */     this._rowRecordValues = null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public RowRecord getRow(int rowIndex)
/* 125:    */   {
/* 126:146 */     int maxrow = SpreadsheetVersion.EXCEL97.getLastRowIndex();
/* 127:147 */     if ((rowIndex < 0) || (rowIndex > maxrow)) {
/* 128:148 */       throw new IllegalArgumentException("The row number must be between 0 and " + maxrow + ", but had: " + rowIndex);
/* 129:    */     }
/* 130:150 */     return (RowRecord)this._rowRecords.get(Integer.valueOf(rowIndex));
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getPhysicalNumberOfRows()
/* 134:    */   {
/* 135:155 */     return this._rowRecords.size();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int getFirstRowNum()
/* 139:    */   {
/* 140:160 */     return this._firstrow;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getLastRowNum()
/* 144:    */   {
/* 145:165 */     return this._lastrow;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getRowBlockCount()
/* 149:    */   {
/* 150:173 */     int size = this._rowRecords.size() / 32;
/* 151:174 */     if (this._rowRecords.size() % 32 != 0) {
/* 152:175 */       size++;
/* 153:    */     }
/* 154:176 */     return size;
/* 155:    */   }
/* 156:    */   
/* 157:    */   private int getRowBlockSize(int block)
/* 158:    */   {
/* 159:180 */     return 20 * getRowCountForBlock(block);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int getRowCountForBlock(int block)
/* 163:    */   {
/* 164:185 */     int startIndex = block * 32;
/* 165:186 */     int endIndex = startIndex + 32 - 1;
/* 166:187 */     if (endIndex >= this._rowRecords.size()) {
/* 167:188 */       endIndex = this._rowRecords.size() - 1;
/* 168:    */     }
/* 169:190 */     return endIndex - startIndex + 1;
/* 170:    */   }
/* 171:    */   
/* 172:    */   private int getStartRowNumberForBlock(int block)
/* 173:    */   {
/* 174:195 */     int startIndex = block * 32;
/* 175:197 */     if (this._rowRecordValues == null) {
/* 176:198 */       this._rowRecordValues = ((RowRecord[])this._rowRecords.values().toArray(new RowRecord[this._rowRecords.size()]));
/* 177:    */     }
/* 178:    */     try
/* 179:    */     {
/* 180:202 */       return this._rowRecordValues[startIndex].getRowNumber();
/* 181:    */     }
/* 182:    */     catch (ArrayIndexOutOfBoundsException e)
/* 183:    */     {
/* 184:204 */       throw new RuntimeException("Did not find start row for block " + block);
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   private int getEndRowNumberForBlock(int block)
/* 189:    */   {
/* 190:210 */     int endIndex = (block + 1) * 32 - 1;
/* 191:211 */     if (endIndex >= this._rowRecords.size()) {
/* 192:212 */       endIndex = this._rowRecords.size() - 1;
/* 193:    */     }
/* 194:214 */     if (this._rowRecordValues == null) {
/* 195:215 */       this._rowRecordValues = ((RowRecord[])this._rowRecords.values().toArray(new RowRecord[this._rowRecords.size()]));
/* 196:    */     }
/* 197:    */     try
/* 198:    */     {
/* 199:219 */       return this._rowRecordValues[endIndex].getRowNumber();
/* 200:    */     }
/* 201:    */     catch (ArrayIndexOutOfBoundsException e)
/* 202:    */     {
/* 203:221 */       throw new RuntimeException("Did not find end row for block " + block);
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   private int visitRowRecordsForBlock(int blockIndex, RecordVisitor rv)
/* 208:    */   {
/* 209:226 */     int startIndex = blockIndex * 32;
/* 210:227 */     int endIndex = startIndex + 32;
/* 211:    */     
/* 212:229 */     Iterator<RowRecord> rowIterator = this._rowRecords.values().iterator();
/* 213:235 */     for (int i = 0; i < startIndex; i++) {
/* 214:237 */       rowIterator.next();
/* 215:    */     }
/* 216:238 */     int result = 0;
/* 217:239 */     while ((rowIterator.hasNext()) && (i++ < endIndex))
/* 218:    */     {
/* 219:240 */       Record rec = (Record)rowIterator.next();
/* 220:241 */       result += rec.getRecordSize();
/* 221:242 */       rv.visitRecord(rec);
/* 222:    */     }
/* 223:244 */     return result;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void visitContainedRecords(RecordVisitor rv)
/* 227:    */   {
/* 228:250 */     PositionTrackingVisitor stv = new PositionTrackingVisitor(rv, 0);
/* 229:    */     
/* 230:252 */     int blockCount = getRowBlockCount();
/* 231:253 */     for (int blockIndex = 0; blockIndex < blockCount; blockIndex++)
/* 232:    */     {
/* 233:256 */       int pos = 0;
/* 234:    */       
/* 235:258 */       int rowBlockSize = visitRowRecordsForBlock(blockIndex, rv);
/* 236:259 */       pos += rowBlockSize;
/* 237:    */       
/* 238:261 */       int startRowNumber = getStartRowNumberForBlock(blockIndex);
/* 239:262 */       int endRowNumber = getEndRowNumberForBlock(blockIndex);
/* 240:263 */       DBCellRecord.Builder dbcrBuilder = new DBCellRecord.Builder();
/* 241:    */       
/* 242:265 */       int cellRefOffset = rowBlockSize - 20;
/* 243:266 */       for (int row = startRowNumber; row <= endRowNumber; row++) {
/* 244:267 */         if (this._valuesAgg.rowHasCells(row))
/* 245:    */         {
/* 246:268 */           stv.setPosition(0);
/* 247:269 */           this._valuesAgg.visitCellsForRow(row, stv);
/* 248:270 */           int rowCellSize = stv.getPosition();
/* 249:271 */           pos += rowCellSize;
/* 250:    */           
/* 251:    */ 
/* 252:274 */           dbcrBuilder.addCellOffset(cellRefOffset);
/* 253:275 */           cellRefOffset = rowCellSize;
/* 254:    */         }
/* 255:    */       }
/* 256:279 */       rv.visitRecord(dbcrBuilder.build(pos));
/* 257:    */     }
/* 258:281 */     for (Record _unknownRecord : this._unknownRecords) {
/* 259:283 */       rv.visitRecord(_unknownRecord);
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   public Iterator<RowRecord> getIterator()
/* 264:    */   {
/* 265:288 */     return this._rowRecords.values().iterator();
/* 266:    */   }
/* 267:    */   
/* 268:    */   public int findStartOfRowOutlineGroup(int row)
/* 269:    */   {
/* 270:293 */     RowRecord rowRecord = getRow(row);
/* 271:294 */     int level = rowRecord.getOutlineLevel();
/* 272:295 */     int currentRow = row;
/* 273:296 */     while ((currentRow >= 0) && (getRow(currentRow) != null))
/* 274:    */     {
/* 275:297 */       rowRecord = getRow(currentRow);
/* 276:298 */       if (rowRecord.getOutlineLevel() < level) {
/* 277:299 */         return currentRow + 1;
/* 278:    */       }
/* 279:301 */       currentRow--;
/* 280:    */     }
/* 281:304 */     return currentRow + 1;
/* 282:    */   }
/* 283:    */   
/* 284:    */   public int findEndOfRowOutlineGroup(int row)
/* 285:    */   {
/* 286:308 */     int level = getRow(row).getOutlineLevel();
/* 287:310 */     for (int currentRow = row; currentRow < getLastRowNum(); currentRow++) {
/* 288:311 */       if ((getRow(currentRow) == null) || (getRow(currentRow).getOutlineLevel() < level)) {
/* 289:    */         break;
/* 290:    */       }
/* 291:    */     }
/* 292:316 */     return currentRow - 1;
/* 293:    */   }
/* 294:    */   
/* 295:    */   private int writeHidden(RowRecord pRowRecord, int row)
/* 296:    */   {
/* 297:324 */     int rowIx = row;
/* 298:325 */     RowRecord rowRecord = pRowRecord;
/* 299:326 */     int level = rowRecord.getOutlineLevel();
/* 300:327 */     while ((rowRecord != null) && (getRow(rowIx).getOutlineLevel() >= level))
/* 301:    */     {
/* 302:328 */       rowRecord.setZeroHeight(true);
/* 303:329 */       rowIx++;
/* 304:330 */       rowRecord = getRow(rowIx);
/* 305:    */     }
/* 306:332 */     return rowIx;
/* 307:    */   }
/* 308:    */   
/* 309:    */   public void collapseRow(int rowNumber)
/* 310:    */   {
/* 311:338 */     int startRow = findStartOfRowOutlineGroup(rowNumber);
/* 312:339 */     RowRecord rowRecord = getRow(startRow);
/* 313:    */     
/* 314:    */ 
/* 315:342 */     int nextRowIx = writeHidden(rowRecord, startRow);
/* 316:    */     
/* 317:344 */     RowRecord row = getRow(nextRowIx);
/* 318:345 */     if (row == null)
/* 319:    */     {
/* 320:346 */       row = createRow(nextRowIx);
/* 321:347 */       insertRow(row);
/* 322:    */     }
/* 323:350 */     row.setColapsed(true);
/* 324:    */   }
/* 325:    */   
/* 326:    */   public static RowRecord createRow(int rowNumber)
/* 327:    */   {
/* 328:361 */     return new RowRecord(rowNumber);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public boolean isRowGroupCollapsed(int row)
/* 332:    */   {
/* 333:365 */     int collapseRow = findEndOfRowOutlineGroup(row) + 1;
/* 334:    */     
/* 335:367 */     return (getRow(collapseRow) != null) && (getRow(collapseRow).getColapsed());
/* 336:    */   }
/* 337:    */   
/* 338:    */   public void expandRow(int rowNumber)
/* 339:    */   {
/* 340:371 */     if (rowNumber == -1) {
/* 341:372 */       return;
/* 342:    */     }
/* 343:375 */     if (!isRowGroupCollapsed(rowNumber)) {
/* 344:376 */       return;
/* 345:    */     }
/* 346:380 */     int startIdx = findStartOfRowOutlineGroup(rowNumber);
/* 347:381 */     RowRecord row = getRow(startIdx);
/* 348:    */     
/* 349:    */ 
/* 350:384 */     int endIdx = findEndOfRowOutlineGroup(rowNumber);
/* 351:393 */     if (!isRowGroupHiddenByParent(rowNumber)) {
/* 352:394 */       for (int i = startIdx; i <= endIdx; i++)
/* 353:    */       {
/* 354:395 */         RowRecord otherRow = getRow(i);
/* 355:396 */         if ((row.getOutlineLevel() == otherRow.getOutlineLevel()) || (!isRowGroupCollapsed(i))) {
/* 356:397 */           otherRow.setZeroHeight(false);
/* 357:    */         }
/* 358:    */       }
/* 359:    */     }
/* 360:403 */     getRow(endIdx + 1).setColapsed(false);
/* 361:    */   }
/* 362:    */   
/* 363:    */   public boolean isRowGroupHiddenByParent(int row)
/* 364:    */   {
/* 365:410 */     int endOfOutlineGroupIdx = findEndOfRowOutlineGroup(row);
/* 366:    */     boolean endHidden;
/* 367:    */     int endLevel;
/* 368:    */     boolean endHidden;
/* 369:411 */     if (getRow(endOfOutlineGroupIdx + 1) == null)
/* 370:    */     {
/* 371:412 */       int endLevel = 0;
/* 372:413 */       endHidden = false;
/* 373:    */     }
/* 374:    */     else
/* 375:    */     {
/* 376:415 */       endLevel = getRow(endOfOutlineGroupIdx + 1).getOutlineLevel();
/* 377:416 */       endHidden = getRow(endOfOutlineGroupIdx + 1).getZeroHeight();
/* 378:    */     }
/* 379:422 */     int startOfOutlineGroupIdx = findStartOfRowOutlineGroup(row);
/* 380:    */     boolean startHidden;
/* 381:    */     int startLevel;
/* 382:    */     boolean startHidden;
/* 383:423 */     if ((startOfOutlineGroupIdx - 1 < 0) || (getRow(startOfOutlineGroupIdx - 1) == null))
/* 384:    */     {
/* 385:424 */       int startLevel = 0;
/* 386:425 */       startHidden = false;
/* 387:    */     }
/* 388:    */     else
/* 389:    */     {
/* 390:427 */       startLevel = getRow(startOfOutlineGroupIdx - 1).getOutlineLevel();
/* 391:428 */       startHidden = getRow(startOfOutlineGroupIdx - 1).getZeroHeight();
/* 392:    */     }
/* 393:431 */     if (endLevel > startLevel) {
/* 394:432 */       return endHidden;
/* 395:    */     }
/* 396:435 */     return startHidden;
/* 397:    */   }
/* 398:    */   
/* 399:    */   public Iterator<CellValueRecordInterface> getCellValueIterator()
/* 400:    */   {
/* 401:442 */     return this._valuesAgg.iterator();
/* 402:    */   }
/* 403:    */   
/* 404:    */   public IndexRecord createIndexRecord(int indexRecordOffset, int sizeOfInitialSheetRecords)
/* 405:    */   {
/* 406:446 */     IndexRecord result = new IndexRecord();
/* 407:447 */     result.setFirstRow(this._firstrow);
/* 408:448 */     result.setLastRowAdd1(this._lastrow + 1);
/* 409:    */     
/* 410:    */ 
/* 411:    */ 
/* 412:    */ 
/* 413:    */ 
/* 414:    */ 
/* 415:    */ 
/* 416:456 */     int blockCount = getRowBlockCount();
/* 417:    */     
/* 418:458 */     int indexRecSize = IndexRecord.getRecordSizeForBlockCount(blockCount);
/* 419:    */     
/* 420:460 */     int currentOffset = indexRecordOffset + indexRecSize + sizeOfInitialSheetRecords;
/* 421:462 */     for (int block = 0; block < blockCount; block++)
/* 422:    */     {
/* 423:467 */       currentOffset += getRowBlockSize(block);
/* 424:    */       
/* 425:469 */       currentOffset += this._valuesAgg.getRowCellBlockSize(getStartRowNumberForBlock(block), getEndRowNumberForBlock(block));
/* 426:    */       
/* 427:    */ 
/* 428:    */ 
/* 429:473 */       result.addDbcell(currentOffset);
/* 430:    */       
/* 431:475 */       currentOffset += 8 + getRowCountForBlock(block) * 2;
/* 432:    */     }
/* 433:477 */     return result;
/* 434:    */   }
/* 435:    */   
/* 436:    */   public void insertCell(CellValueRecordInterface cvRec)
/* 437:    */   {
/* 438:480 */     this._valuesAgg.insertCell(cvRec);
/* 439:    */   }
/* 440:    */   
/* 441:    */   public void removeCell(CellValueRecordInterface cvRec)
/* 442:    */   {
/* 443:483 */     if ((cvRec instanceof FormulaRecordAggregate)) {
/* 444:484 */       ((FormulaRecordAggregate)cvRec).notifyFormulaChanging();
/* 445:    */     }
/* 446:486 */     this._valuesAgg.removeCell(cvRec);
/* 447:    */   }
/* 448:    */   
/* 449:    */   public FormulaRecordAggregate createFormula(int row, int col)
/* 450:    */   {
/* 451:489 */     FormulaRecord fr = new FormulaRecord();
/* 452:490 */     fr.setRow(row);
/* 453:491 */     fr.setColumn((short)col);
/* 454:492 */     return new FormulaRecordAggregate(fr, null, this._sharedValueManager);
/* 455:    */   }
/* 456:    */   
/* 457:    */   public void updateFormulasAfterRowShift(FormulaShifter formulaShifter, int currentExternSheetIndex)
/* 458:    */   {
/* 459:495 */     this._valuesAgg.updateFormulasAfterRowShift(formulaShifter, currentExternSheetIndex);
/* 460:    */   }
/* 461:    */   
/* 462:    */   public DimensionsRecord createDimensions()
/* 463:    */   {
/* 464:498 */     DimensionsRecord result = new DimensionsRecord();
/* 465:499 */     result.setFirstRow(this._firstrow);
/* 466:500 */     result.setLastRow(this._lastrow);
/* 467:501 */     result.setFirstCol((short)this._valuesAgg.getFirstCellNum());
/* 468:502 */     result.setLastCol((short)this._valuesAgg.getLastCellNum());
/* 469:503 */     return result;
/* 470:    */   }
/* 471:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.RowRecordsAggregate

 * JD-Core Version:    0.7.0.1

 */