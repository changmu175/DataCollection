/*   1:    */ package org.apache.poi.ss.formula.eval.forked;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   8:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   9:    */ import org.apache.poi.ss.formula.EvaluationWorkbook;
/*  10:    */ import org.apache.poi.ss.usermodel.Cell;
/*  11:    */ import org.apache.poi.ss.usermodel.Row;
/*  12:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  13:    */ import org.apache.poi.ss.util.CellReference;
/*  14:    */ import org.apache.poi.util.Internal;
/*  15:    */ 
/*  16:    */ @Internal
/*  17:    */ final class ForkedEvaluationSheet
/*  18:    */   implements EvaluationSheet
/*  19:    */ {
/*  20:    */   private final EvaluationSheet _masterSheet;
/*  21:    */   private final Map<RowColKey, ForkedEvaluationCell> _sharedCellsByRowCol;
/*  22:    */   
/*  23:    */   public ForkedEvaluationSheet(EvaluationSheet masterSheet)
/*  24:    */   {
/*  25: 51 */     this._masterSheet = masterSheet;
/*  26: 52 */     this._sharedCellsByRowCol = new HashMap();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public EvaluationCell getCell(int rowIndex, int columnIndex)
/*  30:    */   {
/*  31: 57 */     RowColKey key = new RowColKey(rowIndex, columnIndex);
/*  32:    */     
/*  33: 59 */     ForkedEvaluationCell result = (ForkedEvaluationCell)this._sharedCellsByRowCol.get(key);
/*  34: 60 */     if (result == null) {
/*  35: 61 */       return this._masterSheet.getCell(rowIndex, columnIndex);
/*  36:    */     }
/*  37: 63 */     return result;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ForkedEvaluationCell getOrCreateUpdatableCell(int rowIndex, int columnIndex)
/*  41:    */   {
/*  42: 67 */     RowColKey key = new RowColKey(rowIndex, columnIndex);
/*  43:    */     
/*  44: 69 */     ForkedEvaluationCell result = (ForkedEvaluationCell)this._sharedCellsByRowCol.get(key);
/*  45: 70 */     if (result == null)
/*  46:    */     {
/*  47: 71 */       EvaluationCell mcell = this._masterSheet.getCell(rowIndex, columnIndex);
/*  48: 72 */       if (mcell == null)
/*  49:    */       {
/*  50: 73 */         CellReference cr = new CellReference(rowIndex, columnIndex);
/*  51: 74 */         throw new UnsupportedOperationException("Underlying cell '" + cr.formatAsString() + "' is missing in master sheet.");
/*  52:    */       }
/*  53: 77 */       result = new ForkedEvaluationCell(this, mcell);
/*  54: 78 */       this._sharedCellsByRowCol.put(key, result);
/*  55:    */     }
/*  56: 80 */     return result;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void copyUpdatedCells(Sheet sheet)
/*  60:    */   {
/*  61: 84 */     RowColKey[] keys = new RowColKey[this._sharedCellsByRowCol.size()];
/*  62: 85 */     this._sharedCellsByRowCol.keySet().toArray(keys);
/*  63: 86 */     Arrays.sort(keys);
/*  64: 87 */     for (int i = 0; i < keys.length; i++)
/*  65:    */     {
/*  66: 88 */       RowColKey key = keys[i];
/*  67: 89 */       Row row = sheet.getRow(key.getRowIndex());
/*  68: 90 */       if (row == null) {
/*  69: 91 */         row = sheet.createRow(key.getRowIndex());
/*  70:    */       }
/*  71: 93 */       Cell destCell = row.getCell(key.getColumnIndex());
/*  72: 94 */       if (destCell == null) {
/*  73: 95 */         destCell = row.createCell(key.getColumnIndex());
/*  74:    */       }
/*  75: 98 */       ForkedEvaluationCell srcCell = (ForkedEvaluationCell)this._sharedCellsByRowCol.get(key);
/*  76: 99 */       srcCell.copyValue(destCell);
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getSheetIndex(EvaluationWorkbook mewb)
/*  81:    */   {
/*  82:104 */     return mewb.getSheetIndex(this._masterSheet);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void clearAllCachedResultValues()
/*  86:    */   {
/*  87:115 */     this._masterSheet.clearAllCachedResultValues();
/*  88:    */   }
/*  89:    */   
/*  90:    */   private static final class RowColKey
/*  91:    */     implements Comparable<RowColKey>
/*  92:    */   {
/*  93:    */     private final int _rowIndex;
/*  94:    */     private final int _columnIndex;
/*  95:    */     
/*  96:    */     public RowColKey(int rowIndex, int columnIndex)
/*  97:    */     {
/*  98:124 */       this._rowIndex = rowIndex;
/*  99:125 */       this._columnIndex = columnIndex;
/* 100:    */     }
/* 101:    */     
/* 102:    */     public boolean equals(Object obj)
/* 103:    */     {
/* 104:129 */       if (!(obj instanceof RowColKey)) {
/* 105:130 */         return false;
/* 106:    */       }
/* 107:132 */       RowColKey other = (RowColKey)obj;
/* 108:133 */       return (this._rowIndex == other._rowIndex) && (this._columnIndex == other._columnIndex);
/* 109:    */     }
/* 110:    */     
/* 111:    */     public int hashCode()
/* 112:    */     {
/* 113:137 */       return this._rowIndex ^ this._columnIndex;
/* 114:    */     }
/* 115:    */     
/* 116:    */     public int compareTo(RowColKey o)
/* 117:    */     {
/* 118:141 */       int cmp = this._rowIndex - o._rowIndex;
/* 119:142 */       if (cmp != 0) {
/* 120:143 */         return cmp;
/* 121:    */       }
/* 122:145 */       return this._columnIndex - o._columnIndex;
/* 123:    */     }
/* 124:    */     
/* 125:    */     public int getRowIndex()
/* 126:    */     {
/* 127:148 */       return this._rowIndex;
/* 128:    */     }
/* 129:    */     
/* 130:    */     public int getColumnIndex()
/* 131:    */     {
/* 132:151 */       return this._columnIndex;
/* 133:    */     }
/* 134:    */   }
/* 135:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.forked.ForkedEvaluationSheet
 * JD-Core Version:    0.7.0.1
 */