/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.hssf.record.ArrayRecord;
/*   8:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*   9:    */ import org.apache.poi.hssf.record.SharedFormulaRecord;
/*  10:    */ import org.apache.poi.hssf.record.SharedValueRecordBase;
/*  11:    */ import org.apache.poi.hssf.record.TableRecord;
/*  12:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*  13:    */ import org.apache.poi.ss.formula.Formula;
/*  14:    */ import org.apache.poi.ss.util.CellReference;
/*  15:    */ 
/*  16:    */ public final class SharedValueManager
/*  17:    */ {
/*  18:    */   private final List<ArrayRecord> _arrayRecords;
/*  19:    */   private final TableRecord[] _tableRecords;
/*  20:    */   private final Map<SharedFormulaRecord, SharedFormulaGroup> _groupsBySharedFormulaRecord;
/*  21:    */   private Map<Integer, SharedFormulaGroup> _groupsCache;
/*  22:    */   
/*  23:    */   private static final class SharedFormulaGroup
/*  24:    */   {
/*  25:    */     private final SharedFormulaRecord _sfr;
/*  26:    */     private final FormulaRecordAggregate[] _frAggs;
/*  27:    */     private int _numberOfFormulas;
/*  28:    */     private final CellReference _firstCell;
/*  29:    */     
/*  30:    */     public SharedFormulaGroup(SharedFormulaRecord sfr, CellReference firstCell)
/*  31:    */     {
/*  32: 60 */       if (!sfr.isInRange(firstCell.getRow(), firstCell.getCol())) {
/*  33: 61 */         throw new IllegalArgumentException("First formula cell " + firstCell.formatAsString() + " is not shared formula range " + sfr.getRange() + ".");
/*  34:    */       }
/*  35: 64 */       this._sfr = sfr;
/*  36: 65 */       this._firstCell = firstCell;
/*  37: 66 */       int width = sfr.getLastColumn() - sfr.getFirstColumn() + 1;
/*  38: 67 */       int height = sfr.getLastRow() - sfr.getFirstRow() + 1;
/*  39: 68 */       this._frAggs = new FormulaRecordAggregate[width * height];
/*  40: 69 */       this._numberOfFormulas = 0;
/*  41:    */     }
/*  42:    */     
/*  43:    */     public void add(FormulaRecordAggregate agg)
/*  44:    */     {
/*  45: 73 */       if ((this._numberOfFormulas == 0) && (
/*  46: 74 */         (this._firstCell.getRow() != agg.getRow()) || (this._firstCell.getCol() != agg.getColumn()))) {
/*  47: 75 */         throw new IllegalStateException("shared formula coding error: " + this._firstCell.getCol() + '/' + this._firstCell.getRow() + " != " + agg.getColumn() + '/' + agg.getRow());
/*  48:    */       }
/*  49: 78 */       if (this._numberOfFormulas >= this._frAggs.length) {
/*  50: 79 */         throw new RuntimeException("Too many formula records for shared formula group");
/*  51:    */       }
/*  52: 81 */       this._frAggs[(this._numberOfFormulas++)] = agg;
/*  53:    */     }
/*  54:    */     
/*  55:    */     public void unlinkSharedFormulas()
/*  56:    */     {
/*  57: 85 */       for (int i = 0; i < this._numberOfFormulas; i++) {
/*  58: 86 */         this._frAggs[i].unlinkSharedFormula();
/*  59:    */       }
/*  60:    */     }
/*  61:    */     
/*  62:    */     public SharedFormulaRecord getSFR()
/*  63:    */     {
/*  64: 91 */       return this._sfr;
/*  65:    */     }
/*  66:    */     
/*  67:    */     public final String toString()
/*  68:    */     {
/*  69: 95 */       StringBuffer sb = new StringBuffer(64);
/*  70: 96 */       sb.append(getClass().getName()).append(" [");
/*  71: 97 */       sb.append(this._sfr.getRange());
/*  72: 98 */       sb.append("]");
/*  73: 99 */       return sb.toString();
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static SharedValueManager createEmpty()
/*  78:    */   {
/*  79:108 */     return new SharedValueManager(new SharedFormulaRecord[0], new CellReference[0], new ArrayRecord[0], new TableRecord[0]);
/*  80:    */   }
/*  81:    */   
/*  82:    */   private SharedValueManager(SharedFormulaRecord[] sharedFormulaRecords, CellReference[] firstCells, ArrayRecord[] arrayRecords, TableRecord[] tableRecords)
/*  83:    */   {
/*  84:119 */     int nShF = sharedFormulaRecords.length;
/*  85:120 */     if (nShF != firstCells.length) {
/*  86:121 */       throw new IllegalArgumentException("array sizes don't match: " + nShF + "!=" + firstCells.length + ".");
/*  87:    */     }
/*  88:123 */     this._arrayRecords = toList(arrayRecords);
/*  89:124 */     this._tableRecords = tableRecords;
/*  90:125 */     Map<SharedFormulaRecord, SharedFormulaGroup> m = new HashMap(nShF * 3 / 2);
/*  91:126 */     for (int i = 0; i < nShF; i++)
/*  92:    */     {
/*  93:127 */       SharedFormulaRecord sfr = sharedFormulaRecords[i];
/*  94:128 */       m.put(sfr, new SharedFormulaGroup(sfr, firstCells[i]));
/*  95:    */     }
/*  96:130 */     this._groupsBySharedFormulaRecord = m;
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static <Z> List<Z> toList(Z[] zz)
/* 100:    */   {
/* 101:137 */     List<Z> result = new ArrayList(zz.length);
/* 102:138 */     for (int i = 0; i < zz.length; i++) {
/* 103:139 */       result.add(zz[i]);
/* 104:    */     }
/* 105:141 */     return result;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static SharedValueManager create(SharedFormulaRecord[] sharedFormulaRecords, CellReference[] firstCells, ArrayRecord[] arrayRecords, TableRecord[] tableRecords)
/* 109:    */   {
/* 110:148 */     if (sharedFormulaRecords.length + firstCells.length + arrayRecords.length + tableRecords.length < 1) {
/* 111:149 */       return createEmpty();
/* 112:    */     }
/* 113:151 */     return new SharedValueManager(sharedFormulaRecords, firstCells, arrayRecords, tableRecords);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public SharedFormulaRecord linkSharedFormulaRecord(CellReference firstCell, FormulaRecordAggregate agg)
/* 117:    */   {
/* 118:160 */     SharedFormulaGroup result = findFormulaGroupForCell(firstCell);
/* 119:161 */     if (null == result) {
/* 120:162 */       throw new RuntimeException("Failed to find a matching shared formula record");
/* 121:    */     }
/* 122:164 */     result.add(agg);
/* 123:165 */     return result.getSFR();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private SharedFormulaGroup findFormulaGroupForCell(CellReference cellRef)
/* 127:    */   {
/* 128:169 */     if (null == this._groupsCache)
/* 129:    */     {
/* 130:170 */       this._groupsCache = new HashMap(this._groupsBySharedFormulaRecord.size());
/* 131:171 */       for (SharedFormulaGroup group : this._groupsBySharedFormulaRecord.values()) {
/* 132:172 */         this._groupsCache.put(getKeyForCache(group._firstCell), group);
/* 133:    */       }
/* 134:    */     }
/* 135:175 */     SharedFormulaGroup sfg = (SharedFormulaGroup)this._groupsCache.get(getKeyForCache(cellRef));
/* 136:176 */     return sfg;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private Integer getKeyForCache(CellReference cellRef)
/* 140:    */   {
/* 141:181 */     return Integer.valueOf(cellRef.getCol() + 1 << 16 | cellRef.getRow());
/* 142:    */   }
/* 143:    */   
/* 144:    */   public SharedValueRecordBase getRecordForFirstCell(FormulaRecordAggregate agg)
/* 145:    */   {
/* 146:197 */     CellReference firstCell = agg.getFormulaRecord().getFormula().getExpReference();
/* 147:200 */     if (firstCell == null) {
/* 148:202 */       return null;
/* 149:    */     }
/* 150:206 */     int row = firstCell.getRow();
/* 151:207 */     int column = firstCell.getCol();
/* 152:208 */     if ((agg.getRow() != row) || (agg.getColumn() != column)) {
/* 153:210 */       return null;
/* 154:    */     }
/* 155:213 */     if (!this._groupsBySharedFormulaRecord.isEmpty())
/* 156:    */     {
/* 157:214 */       SharedFormulaGroup sfg = findFormulaGroupForCell(firstCell);
/* 158:215 */       if (null != sfg) {
/* 159:216 */         return sfg.getSFR();
/* 160:    */       }
/* 161:    */     }
/* 162:224 */     for (TableRecord tr : this._tableRecords) {
/* 163:225 */       if (tr.isFirstCell(row, column)) {
/* 164:226 */         return tr;
/* 165:    */       }
/* 166:    */     }
/* 167:229 */     for (ArrayRecord ar : this._arrayRecords) {
/* 168:230 */       if (ar.isFirstCell(row, column)) {
/* 169:231 */         return ar;
/* 170:    */       }
/* 171:    */     }
/* 172:234 */     return null;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void unlink(SharedFormulaRecord sharedFormulaRecord)
/* 176:    */   {
/* 177:242 */     SharedFormulaGroup svg = (SharedFormulaGroup)this._groupsBySharedFormulaRecord.remove(sharedFormulaRecord);
/* 178:243 */     if (svg == null) {
/* 179:244 */       throw new IllegalStateException("Failed to find formulas for shared formula");
/* 180:    */     }
/* 181:246 */     this._groupsCache = null;
/* 182:247 */     svg.unlinkSharedFormulas();
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void addArrayRecord(ArrayRecord ar)
/* 186:    */   {
/* 187:255 */     this._arrayRecords.add(ar);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public CellRangeAddress8Bit removeArrayFormula(int rowIndex, int columnIndex)
/* 191:    */   {
/* 192:264 */     for (ArrayRecord ar : this._arrayRecords) {
/* 193:265 */       if (ar.isInRange(rowIndex, columnIndex))
/* 194:    */       {
/* 195:266 */         this._arrayRecords.remove(ar);
/* 196:267 */         return ar.getRange();
/* 197:    */       }
/* 198:    */     }
/* 199:270 */     String ref = new CellReference(rowIndex, columnIndex, false, false).formatAsString();
/* 200:271 */     throw new IllegalArgumentException("Specified cell " + ref + " is not part of an array formula.");
/* 201:    */   }
/* 202:    */   
/* 203:    */   public ArrayRecord getArrayRecord(int firstRow, int firstColumn)
/* 204:    */   {
/* 205:279 */     for (ArrayRecord ar : this._arrayRecords) {
/* 206:280 */       if (ar.isFirstCell(firstRow, firstColumn)) {
/* 207:281 */         return ar;
/* 208:    */       }
/* 209:    */     }
/* 210:284 */     return null;
/* 211:    */   }
/* 212:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.aggregates.SharedValueManager
 * JD-Core Version:    0.7.0.1
 */