/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import org.apache.poi.hssf.model.RecordStream;
/*   5:    */ import org.apache.poi.hssf.record.BlankRecord;
/*   6:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   7:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*   8:    */ import org.apache.poi.hssf.record.MulBlankRecord;
/*   9:    */ import org.apache.poi.hssf.record.Record;
/*  10:    */ import org.apache.poi.hssf.record.RecordBase;
/*  11:    */ import org.apache.poi.hssf.record.StringRecord;
/*  12:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*  13:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  14:    */ 
/*  15:    */ public final class ValueRecordsAggregate
/*  16:    */   implements Iterable<CellValueRecordInterface>
/*  17:    */ {
/*  18:    */   private static final int MAX_ROW_INDEX = 65535;
/*  19:    */   private static final int INDEX_NOT_SET = -1;
/*  20: 45 */   private int firstcell = -1;
/*  21: 46 */   private int lastcell = -1;
/*  22:    */   private CellValueRecordInterface[][] records;
/*  23:    */   
/*  24:    */   public ValueRecordsAggregate()
/*  25:    */   {
/*  26: 52 */     this(-1, -1, new CellValueRecordInterface[30][]);
/*  27:    */   }
/*  28:    */   
/*  29:    */   private ValueRecordsAggregate(int firstCellIx, int lastCellIx, CellValueRecordInterface[][] pRecords)
/*  30:    */   {
/*  31: 55 */     this.firstcell = firstCellIx;
/*  32: 56 */     this.lastcell = lastCellIx;
/*  33: 57 */     this.records = pRecords;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void insertCell(CellValueRecordInterface cell)
/*  37:    */   {
/*  38: 61 */     short column = cell.getColumn();
/*  39: 62 */     int row = cell.getRow();
/*  40: 63 */     if (row >= this.records.length)
/*  41:    */     {
/*  42: 64 */       CellValueRecordInterface[][] oldRecords = this.records;
/*  43: 65 */       int newSize = oldRecords.length * 2;
/*  44: 66 */       if (newSize < row + 1) {
/*  45: 67 */         newSize = row + 1;
/*  46:    */       }
/*  47: 68 */       this.records = new CellValueRecordInterface[newSize][];
/*  48: 69 */       System.arraycopy(oldRecords, 0, this.records, 0, oldRecords.length);
/*  49:    */     }
/*  50: 71 */     CellValueRecordInterface[] rowCells = this.records[row];
/*  51: 72 */     if (rowCells == null)
/*  52:    */     {
/*  53: 73 */       int newSize = column + 1;
/*  54: 74 */       if (newSize < 10) {
/*  55: 75 */         newSize = 10;
/*  56:    */       }
/*  57: 76 */       rowCells = new CellValueRecordInterface[newSize];
/*  58: 77 */       this.records[row] = rowCells;
/*  59:    */     }
/*  60: 79 */     if (column >= rowCells.length)
/*  61:    */     {
/*  62: 80 */       CellValueRecordInterface[] oldRowCells = rowCells;
/*  63: 81 */       int newSize = oldRowCells.length * 2;
/*  64: 82 */       if (newSize < column + 1) {
/*  65: 83 */         newSize = column + 1;
/*  66:    */       }
/*  67: 85 */       rowCells = new CellValueRecordInterface[newSize];
/*  68: 86 */       System.arraycopy(oldRowCells, 0, rowCells, 0, oldRowCells.length);
/*  69: 87 */       this.records[row] = rowCells;
/*  70:    */     }
/*  71: 89 */     rowCells[column] = cell;
/*  72: 91 */     if ((column < this.firstcell) || (this.firstcell == -1)) {
/*  73: 92 */       this.firstcell = column;
/*  74:    */     }
/*  75: 94 */     if ((column > this.lastcell) || (this.lastcell == -1)) {
/*  76: 95 */       this.lastcell = column;
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void removeCell(CellValueRecordInterface cell)
/*  81:    */   {
/*  82:100 */     if (cell == null) {
/*  83:101 */       throw new IllegalArgumentException("cell must not be null");
/*  84:    */     }
/*  85:103 */     int row = cell.getRow();
/*  86:104 */     if (row >= this.records.length) {
/*  87:105 */       throw new RuntimeException("cell row is out of range");
/*  88:    */     }
/*  89:107 */     CellValueRecordInterface[] rowCells = this.records[row];
/*  90:108 */     if (rowCells == null) {
/*  91:109 */       throw new RuntimeException("cell row is already empty");
/*  92:    */     }
/*  93:111 */     short column = cell.getColumn();
/*  94:112 */     if (column >= rowCells.length) {
/*  95:113 */       throw new RuntimeException("cell column is out of range");
/*  96:    */     }
/*  97:115 */     rowCells[column] = null;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void removeAllCellsValuesForRow(int rowIndex)
/* 101:    */   {
/* 102:119 */     if ((rowIndex < 0) || (rowIndex > 65535)) {
/* 103:120 */       throw new IllegalArgumentException("Specified rowIndex " + rowIndex + " is outside the allowable range (0.." + 65535 + ")");
/* 104:    */     }
/* 105:123 */     if (rowIndex >= this.records.length) {
/* 106:126 */       return;
/* 107:    */     }
/* 108:129 */     this.records[rowIndex] = null;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public int getPhysicalNumberOfCells()
/* 112:    */   {
/* 113:134 */     int count = 0;
/* 114:135 */     for (int r = 0; r < this.records.length; r++)
/* 115:    */     {
/* 116:136 */       CellValueRecordInterface[] rowCells = this.records[r];
/* 117:137 */       if (rowCells != null) {
/* 118:138 */         for (int c = 0; c < rowCells.length; c++) {
/* 119:139 */           if (rowCells[c] != null) {
/* 120:140 */             count++;
/* 121:    */           }
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:144 */     return count;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int getFirstCellNum()
/* 129:    */   {
/* 130:148 */     return this.firstcell;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getLastCellNum()
/* 134:    */   {
/* 135:152 */     return this.lastcell;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void addMultipleBlanks(MulBlankRecord mbr)
/* 139:    */   {
/* 140:156 */     for (int j = 0; j < mbr.getNumColumns(); j++)
/* 141:    */     {
/* 142:157 */       BlankRecord br = new BlankRecord();
/* 143:    */       
/* 144:159 */       br.setColumn((short)(j + mbr.getFirstColumn()));
/* 145:160 */       br.setRow(mbr.getRow());
/* 146:161 */       br.setXFIndex(mbr.getXFAt(j));
/* 147:162 */       insertCell(br);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void construct(CellValueRecordInterface rec, RecordStream rs, SharedValueManager sfh)
/* 152:    */   {
/* 153:171 */     if ((rec instanceof FormulaRecord))
/* 154:    */     {
/* 155:172 */       FormulaRecord formulaRec = (FormulaRecord)rec;
/* 156:    */       
/* 157:    */ 
/* 158:175 */       Class<? extends Record> nextClass = rs.peekNextClass();
/* 159:    */       StringRecord cachedText;
/* 160:    */       StringRecord cachedText;
/* 161:176 */       if (nextClass == StringRecord.class) {
/* 162:177 */         cachedText = (StringRecord)rs.getNext();
/* 163:    */       } else {
/* 164:179 */         cachedText = null;
/* 165:    */       }
/* 166:181 */       insertCell(new FormulaRecordAggregate(formulaRec, cachedText, sfh));
/* 167:    */     }
/* 168:    */     else
/* 169:    */     {
/* 170:183 */       insertCell(rec);
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public int getRowCellBlockSize(int startRow, int endRow)
/* 175:    */   {
/* 176:191 */     int result = 0;
/* 177:192 */     for (int rowIx = startRow; (rowIx <= endRow) && (rowIx < this.records.length); rowIx++) {
/* 178:193 */       result += getRowSerializedSize(this.records[rowIx]);
/* 179:    */     }
/* 180:195 */     return result;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean rowHasCells(int row)
/* 184:    */   {
/* 185:200 */     if (row >= this.records.length) {
/* 186:201 */       return false;
/* 187:    */     }
/* 188:203 */     CellValueRecordInterface[] rowCells = this.records[row];
/* 189:204 */     if (rowCells == null) {
/* 190:204 */       return false;
/* 191:    */     }
/* 192:205 */     for (int col = 0; col < rowCells.length; col++) {
/* 193:206 */       if (rowCells[col] != null) {
/* 194:206 */         return true;
/* 195:    */       }
/* 196:    */     }
/* 197:208 */     return false;
/* 198:    */   }
/* 199:    */   
/* 200:    */   private static int getRowSerializedSize(CellValueRecordInterface[] rowCells)
/* 201:    */   {
/* 202:212 */     if (rowCells == null) {
/* 203:213 */       return 0;
/* 204:    */     }
/* 205:215 */     int result = 0;
/* 206:216 */     for (int i = 0; i < rowCells.length; i++)
/* 207:    */     {
/* 208:217 */       RecordBase cvr = (RecordBase)rowCells[i];
/* 209:218 */       if (cvr != null)
/* 210:    */       {
/* 211:221 */         int nBlank = countBlanks(rowCells, i);
/* 212:222 */         if (nBlank > 1)
/* 213:    */         {
/* 214:223 */           result += 10 + 2 * nBlank;
/* 215:224 */           i += nBlank - 1;
/* 216:    */         }
/* 217:    */         else
/* 218:    */         {
/* 219:226 */           result += cvr.getRecordSize();
/* 220:    */         }
/* 221:    */       }
/* 222:    */     }
/* 223:229 */     return result;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void visitCellsForRow(int rowIndex, RecordAggregate.RecordVisitor rv)
/* 227:    */   {
/* 228:234 */     CellValueRecordInterface[] rowCells = this.records[rowIndex];
/* 229:235 */     if (rowCells == null) {
/* 230:236 */       throw new IllegalArgumentException("Row [" + rowIndex + "] is empty");
/* 231:    */     }
/* 232:240 */     for (int i = 0; i < rowCells.length; i++)
/* 233:    */     {
/* 234:241 */       RecordBase cvr = (RecordBase)rowCells[i];
/* 235:242 */       if (cvr != null)
/* 236:    */       {
/* 237:245 */         int nBlank = countBlanks(rowCells, i);
/* 238:246 */         if (nBlank > 1)
/* 239:    */         {
/* 240:247 */           rv.visitRecord(createMBR(rowCells, i, nBlank));
/* 241:248 */           i += nBlank - 1;
/* 242:    */         }
/* 243:249 */         else if ((cvr instanceof RecordAggregate))
/* 244:    */         {
/* 245:250 */           RecordAggregate agg = (RecordAggregate)cvr;
/* 246:251 */           agg.visitContainedRecords(rv);
/* 247:    */         }
/* 248:    */         else
/* 249:    */         {
/* 250:253 */           rv.visitRecord((Record)cvr);
/* 251:    */         }
/* 252:    */       }
/* 253:    */     }
/* 254:    */   }
/* 255:    */   
/* 256:    */   private static int countBlanks(CellValueRecordInterface[] rowCellValues, int startIx)
/* 257:    */   {
/* 258:263 */     int i = startIx;
/* 259:264 */     while (i < rowCellValues.length)
/* 260:    */     {
/* 261:265 */       CellValueRecordInterface cvr = rowCellValues[i];
/* 262:266 */       if (!(cvr instanceof BlankRecord)) {
/* 263:    */         break;
/* 264:    */       }
/* 265:269 */       i++;
/* 266:    */     }
/* 267:271 */     return i - startIx;
/* 268:    */   }
/* 269:    */   
/* 270:    */   private MulBlankRecord createMBR(CellValueRecordInterface[] cellValues, int startIx, int nBlank)
/* 271:    */   {
/* 272:276 */     short[] xfs = new short[nBlank];
/* 273:277 */     for (int i = 0; i < xfs.length; i++) {
/* 274:278 */       xfs[i] = ((BlankRecord)cellValues[(startIx + i)]).getXFIndex();
/* 275:    */     }
/* 276:280 */     int rowIx = cellValues[startIx].getRow();
/* 277:281 */     return new MulBlankRecord(rowIx, startIx, xfs);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void updateFormulasAfterRowShift(FormulaShifter shifter, int currentExternSheetIndex)
/* 281:    */   {
/* 282:285 */     for (int i = 0; i < this.records.length; i++)
/* 283:    */     {
/* 284:286 */       CellValueRecordInterface[] rowCells = this.records[i];
/* 285:287 */       if (rowCells != null) {
/* 286:290 */         for (int j = 0; j < rowCells.length; j++)
/* 287:    */         {
/* 288:291 */           CellValueRecordInterface cell = rowCells[j];
/* 289:292 */           if ((cell instanceof FormulaRecordAggregate))
/* 290:    */           {
/* 291:293 */             FormulaRecordAggregate fra = (FormulaRecordAggregate)cell;
/* 292:294 */             Ptg[] ptgs = fra.getFormulaTokens();
/* 293:295 */             Ptg[] ptgs2 = ((FormulaRecordAggregate)cell).getFormulaRecord().getParsedExpression();
/* 294:297 */             if (shifter.adjustFormula(ptgs, currentExternSheetIndex)) {
/* 295:298 */               fra.setParsedExpression(ptgs);
/* 296:    */             }
/* 297:    */           }
/* 298:    */         }
/* 299:    */       }
/* 300:    */     }
/* 301:    */   }
/* 302:    */   
/* 303:    */   class ValueIterator
/* 304:    */     implements Iterator<CellValueRecordInterface>
/* 305:    */   {
/* 306:310 */     int curColIndex = -1;
/* 307:310 */     int curRowIndex = 0;
/* 308:311 */     int nextColIndex = -1;
/* 309:311 */     int nextRowIndex = 0;
/* 310:    */     
/* 311:    */     public ValueIterator()
/* 312:    */     {
/* 313:314 */       getNextPos();
/* 314:    */     }
/* 315:    */     
/* 316:    */     void getNextPos()
/* 317:    */     {
/* 318:318 */       if (this.nextRowIndex >= ValueRecordsAggregate.this.records.length) {
/* 319:319 */         return;
/* 320:    */       }
/* 321:321 */       while (this.nextRowIndex < ValueRecordsAggregate.this.records.length)
/* 322:    */       {
/* 323:322 */         this.nextColIndex += 1;
/* 324:323 */         if ((ValueRecordsAggregate.this.records[this.nextRowIndex] == null) || (this.nextColIndex >= ValueRecordsAggregate.this.records[this.nextRowIndex].length))
/* 325:    */         {
/* 326:324 */           this.nextRowIndex += 1;
/* 327:325 */           this.nextColIndex = -1;
/* 328:    */         }
/* 329:329 */         else if (ValueRecordsAggregate.this.records[this.nextRowIndex][this.nextColIndex] != null) {}
/* 330:    */       }
/* 331:    */     }
/* 332:    */     
/* 333:    */     public boolean hasNext()
/* 334:    */     {
/* 335:336 */       return this.nextRowIndex < ValueRecordsAggregate.this.records.length;
/* 336:    */     }
/* 337:    */     
/* 338:    */     public CellValueRecordInterface next()
/* 339:    */     {
/* 340:340 */       if (!hasNext()) {
/* 341:341 */         throw new IndexOutOfBoundsException("iterator has no next");
/* 342:    */       }
/* 343:343 */       this.curRowIndex = this.nextRowIndex;
/* 344:344 */       this.curColIndex = this.nextColIndex;
/* 345:345 */       CellValueRecordInterface ret = ValueRecordsAggregate.this.records[this.curRowIndex][this.curColIndex];
/* 346:346 */       getNextPos();
/* 347:347 */       return ret;
/* 348:    */     }
/* 349:    */     
/* 350:    */     public void remove()
/* 351:    */     {
/* 352:351 */       ValueRecordsAggregate.this.records[this.curRowIndex][this.curColIndex] = null;
/* 353:    */     }
/* 354:    */   }
/* 355:    */   
/* 356:    */   public Iterator<CellValueRecordInterface> iterator()
/* 357:    */   {
/* 358:357 */     return new ValueIterator();
/* 359:    */   }
/* 360:    */   
/* 361:    */   public Object clone()
/* 362:    */   {
/* 363:361 */     throw new RuntimeException("clone() should not be called.  ValueRecordsAggregate should be copied via Sheet.cloneSheet()");
/* 364:    */   }
/* 365:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.aggregates.ValueRecordsAggregate
 * JD-Core Version:    0.7.0.1
 */