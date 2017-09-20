/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import java.util.HashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ 
/*  7:   */ final class CellEvaluationFrame
/*  8:   */ {
/*  9:   */   private final FormulaCellCacheEntry _cce;
/* 10:   */   private final Set<CellCacheEntry> _sensitiveInputCells;
/* 11:   */   private FormulaUsedBlankCellSet _usedBlankCellGroup;
/* 12:   */   
/* 13:   */   public CellEvaluationFrame(FormulaCellCacheEntry cce)
/* 14:   */   {
/* 15:35 */     this._cce = cce;
/* 16:36 */     this._sensitiveInputCells = new HashSet();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public CellCacheEntry getCCE()
/* 20:   */   {
/* 21:39 */     return this._cce;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String toString()
/* 25:   */   {
/* 26:43 */     StringBuffer sb = new StringBuffer(64);
/* 27:44 */     sb.append(getClass().getName()).append(" [");
/* 28:45 */     sb.append("]");
/* 29:46 */     return sb.toString();
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void addSensitiveInputCell(CellCacheEntry inputCell)
/* 33:   */   {
/* 34:52 */     this._sensitiveInputCells.add(inputCell);
/* 35:   */   }
/* 36:   */   
/* 37:   */   private CellCacheEntry[] getSensitiveInputCells()
/* 38:   */   {
/* 39:59 */     int nItems = this._sensitiveInputCells.size();
/* 40:60 */     if (nItems < 1) {
/* 41:61 */       return CellCacheEntry.EMPTY_ARRAY;
/* 42:   */     }
/* 43:63 */     CellCacheEntry[] result = new CellCacheEntry[nItems];
/* 44:64 */     this._sensitiveInputCells.toArray(result);
/* 45:65 */     return result;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void addUsedBlankCell(int bookIndex, int sheetIndex, int rowIndex, int columnIndex)
/* 49:   */   {
/* 50:68 */     if (this._usedBlankCellGroup == null) {
/* 51:69 */       this._usedBlankCellGroup = new FormulaUsedBlankCellSet();
/* 52:   */     }
/* 53:71 */     this._usedBlankCellGroup.addCell(bookIndex, sheetIndex, rowIndex, columnIndex);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void updateFormulaResult(ValueEval result)
/* 57:   */   {
/* 58:75 */     this._cce.updateFormulaResult(result, getSensitiveInputCells(), this._usedBlankCellGroup);
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.CellEvaluationFrame
 * JD-Core Version:    0.7.0.1
 */