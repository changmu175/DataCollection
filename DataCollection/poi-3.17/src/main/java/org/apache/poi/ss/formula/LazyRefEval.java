/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.AreaEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.RefEvalBase;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ import org.apache.poi.ss.formula.ptg.AreaI;
/*  7:   */ import org.apache.poi.ss.formula.ptg.AreaI.OffsetArea;
/*  8:   */ import org.apache.poi.ss.util.CellReference;
/*  9:   */ 
/* 10:   */ public final class LazyRefEval
/* 11:   */   extends RefEvalBase
/* 12:   */ {
/* 13:   */   private final SheetRangeEvaluator _evaluator;
/* 14:   */   
/* 15:   */   public LazyRefEval(int rowIndex, int columnIndex, SheetRangeEvaluator sre)
/* 16:   */   {
/* 17:34 */     super(sre, rowIndex, columnIndex);
/* 18:35 */     this._evaluator = sre;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ValueEval getInnerValueEval(int sheetIndex)
/* 22:   */   {
/* 23:39 */     return this._evaluator.getEvalForCell(sheetIndex, getRow(), getColumn());
/* 24:   */   }
/* 25:   */   
/* 26:   */   public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx)
/* 27:   */   {
/* 28:44 */     AreaI area = new OffsetArea(getRow(), getColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
/* 29:   */     
/* 30:   */ 
/* 31:47 */     return new LazyAreaEval(area, this._evaluator);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public boolean isSubTotal()
/* 35:   */   {
/* 36:51 */     SheetRefEvaluator sheetEvaluator = this._evaluator.getSheetEvaluator(getFirstSheetIndex());
/* 37:52 */     return sheetEvaluator.isSubTotal(getRow(), getColumn());
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String toString()
/* 41:   */   {
/* 42:56 */     CellReference cr = new CellReference(getRow(), getColumn());
/* 43:57 */     return getClass().getName() + "[" + this._evaluator.getSheetNameRange() + '!' + cr.formatAsString() + "]";
/* 44:   */   }
/* 45:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.LazyRefEval

 * JD-Core Version:    0.7.0.1

 */