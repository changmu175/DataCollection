/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   7:    */ 
/*   8:    */ final class FormulaCellCacheEntry
/*   9:    */   extends CellCacheEntry
/*  10:    */ {
/*  11:    */   private CellCacheEntry[] _sensitiveInputCells;
/*  12:    */   private FormulaUsedBlankCellSet _usedBlankCellGroup;
/*  13:    */   
/*  14:    */   public boolean isInputSensitive()
/*  15:    */   {
/*  16: 47 */     if ((this._sensitiveInputCells != null) && 
/*  17: 48 */       (this._sensitiveInputCells.length > 0)) {
/*  18: 49 */       return true;
/*  19:    */     }
/*  20: 52 */     return this._usedBlankCellGroup != null;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setSensitiveInputCells(CellCacheEntry[] sensitiveInputCells)
/*  24:    */   {
/*  25: 58 */     if (sensitiveInputCells == null)
/*  26:    */     {
/*  27: 59 */       this._sensitiveInputCells = null;
/*  28: 60 */       changeConsumingCells(CellCacheEntry.EMPTY_ARRAY);
/*  29:    */     }
/*  30:    */     else
/*  31:    */     {
/*  32: 62 */       this._sensitiveInputCells = ((CellCacheEntry[])sensitiveInputCells.clone());
/*  33: 63 */       changeConsumingCells(this._sensitiveInputCells);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void clearFormulaEntry()
/*  38:    */   {
/*  39: 68 */     CellCacheEntry[] usedCells = this._sensitiveInputCells;
/*  40: 69 */     if (usedCells != null) {
/*  41: 70 */       for (int i = usedCells.length - 1; i >= 0; i--) {
/*  42: 71 */         usedCells[i].clearConsumingCell(this);
/*  43:    */       }
/*  44:    */     }
/*  45: 74 */     this._sensitiveInputCells = null;
/*  46: 75 */     clearValue();
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void changeConsumingCells(CellCacheEntry[] usedCells)
/*  50:    */   {
/*  51: 80 */     CellCacheEntry[] prevUsedCells = this._sensitiveInputCells;
/*  52: 81 */     int nUsed = usedCells.length;
/*  53: 82 */     for (int i = 0; i < nUsed; i++) {
/*  54: 83 */       usedCells[i].addConsumingCell(this);
/*  55:    */     }
/*  56: 85 */     if (prevUsedCells == null) {
/*  57: 86 */       return;
/*  58:    */     }
/*  59: 88 */     int nPrevUsed = prevUsedCells.length;
/*  60: 89 */     if (nPrevUsed < 1) {
/*  61:    */       return;
/*  62:    */     }
/*  63:    */     Set<CellCacheEntry> usedSet;
/*  64:    */     Set<CellCacheEntry> usedSet;
/*  65: 93 */     if (nUsed < 1)
/*  66:    */     {
/*  67: 94 */       usedSet = Collections.emptySet();
/*  68:    */     }
/*  69:    */     else
/*  70:    */     {
/*  71: 96 */       usedSet = new HashSet(nUsed * 3 / 2);
/*  72: 97 */       for (int i = 0; i < nUsed; i++) {
/*  73: 98 */         usedSet.add(usedCells[i]);
/*  74:    */       }
/*  75:    */     }
/*  76:101 */     for (int i = 0; i < nPrevUsed; i++)
/*  77:    */     {
/*  78:102 */       CellCacheEntry prevUsed = prevUsedCells[i];
/*  79:103 */       if (!usedSet.contains(prevUsed)) {
/*  80:105 */         prevUsed.clearConsumingCell(this);
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void updateFormulaResult(ValueEval result, CellCacheEntry[] sensitiveInputCells, FormulaUsedBlankCellSet usedBlankAreas)
/*  86:    */   {
/*  87:111 */     updateValue(result);
/*  88:112 */     setSensitiveInputCells(sensitiveInputCells);
/*  89:113 */     this._usedBlankCellGroup = usedBlankAreas;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void notifyUpdatedBlankCell(FormulaUsedBlankCellSet.BookSheetKey bsk, int rowIndex, int columnIndex, IEvaluationListener evaluationListener)
/*  93:    */   {
/*  94:117 */     if ((this._usedBlankCellGroup != null) && 
/*  95:118 */       (this._usedBlankCellGroup.containsCell(bsk, rowIndex, columnIndex)))
/*  96:    */     {
/*  97:119 */       clearFormulaEntry();
/*  98:120 */       recurseClearCachedFormulaResults(evaluationListener);
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaCellCacheEntry
 * JD-Core Version:    0.7.0.1
 */