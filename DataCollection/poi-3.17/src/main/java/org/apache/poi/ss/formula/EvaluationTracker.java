/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  10:    */ 
/*  11:    */ final class EvaluationTracker
/*  12:    */ {
/*  13:    */   private final List<CellEvaluationFrame> _evaluationFrames;
/*  14:    */   private final Set<FormulaCellCacheEntry> _currentlyEvaluatingCells;
/*  15:    */   private final EvaluationCache _cache;
/*  16:    */   
/*  17:    */   public EvaluationTracker(EvaluationCache cache)
/*  18:    */   {
/*  19: 45 */     this._cache = cache;
/*  20: 46 */     this._evaluationFrames = new ArrayList();
/*  21: 47 */     this._currentlyEvaluatingCells = new HashSet();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public boolean startEvaluate(FormulaCellCacheEntry cce)
/*  25:    */   {
/*  26: 65 */     if (cce == null) {
/*  27: 66 */       throw new IllegalArgumentException("cellLoc must not be null");
/*  28:    */     }
/*  29: 68 */     if (this._currentlyEvaluatingCells.contains(cce)) {
/*  30: 69 */       return false;
/*  31:    */     }
/*  32: 71 */     this._currentlyEvaluatingCells.add(cce);
/*  33: 72 */     this._evaluationFrames.add(new CellEvaluationFrame(cce));
/*  34: 73 */     return true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void updateCacheResult(ValueEval result)
/*  38:    */   {
/*  39: 78 */     int nFrames = this._evaluationFrames.size();
/*  40: 79 */     if (nFrames < 1) {
/*  41: 80 */       throw new IllegalStateException("Call to endEvaluate without matching call to startEvaluate");
/*  42:    */     }
/*  43: 82 */     CellEvaluationFrame frame = (CellEvaluationFrame)this._evaluationFrames.get(nFrames - 1);
/*  44: 83 */     if ((result == ErrorEval.CIRCULAR_REF_ERROR) && (nFrames > 1)) {
/*  45: 90 */       return;
/*  46:    */     }
/*  47: 93 */     frame.updateFormulaResult(result);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void endEvaluate(CellCacheEntry cce)
/*  51:    */   {
/*  52:108 */     int nFrames = this._evaluationFrames.size();
/*  53:109 */     if (nFrames < 1) {
/*  54:110 */       throw new IllegalStateException("Call to endEvaluate without matching call to startEvaluate");
/*  55:    */     }
/*  56:113 */     nFrames--;
/*  57:114 */     CellEvaluationFrame frame = (CellEvaluationFrame)this._evaluationFrames.get(nFrames);
/*  58:115 */     if (cce != frame.getCCE()) {
/*  59:116 */       throw new IllegalStateException("Wrong cell specified. ");
/*  60:    */     }
/*  61:119 */     this._evaluationFrames.remove(nFrames);
/*  62:120 */     this._currentlyEvaluatingCells.remove(cce);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void acceptFormulaDependency(CellCacheEntry cce)
/*  66:    */   {
/*  67:125 */     int prevFrameIndex = this._evaluationFrames.size() - 1;
/*  68:126 */     if (prevFrameIndex >= 0)
/*  69:    */     {
/*  70:129 */       CellEvaluationFrame consumingFrame = (CellEvaluationFrame)this._evaluationFrames.get(prevFrameIndex);
/*  71:130 */       consumingFrame.addSensitiveInputCell(cce);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void acceptPlainValueDependency(int bookIndex, int sheetIndex, int rowIndex, int columnIndex, ValueEval value)
/*  76:    */   {
/*  77:137 */     int prevFrameIndex = this._evaluationFrames.size() - 1;
/*  78:138 */     if (prevFrameIndex >= 0)
/*  79:    */     {
/*  80:141 */       CellEvaluationFrame consumingFrame = (CellEvaluationFrame)this._evaluationFrames.get(prevFrameIndex);
/*  81:142 */       if (value == BlankEval.instance)
/*  82:    */       {
/*  83:143 */         consumingFrame.addUsedBlankCell(bookIndex, sheetIndex, rowIndex, columnIndex);
/*  84:    */       }
/*  85:    */       else
/*  86:    */       {
/*  87:145 */         PlainValueCellCacheEntry cce = this._cache.getPlainValueEntry(bookIndex, sheetIndex, rowIndex, columnIndex, value);
/*  88:    */         
/*  89:147 */         consumingFrame.addSensitiveInputCell(cce);
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.EvaluationTracker
 * JD-Core Version:    0.7.0.1
 */