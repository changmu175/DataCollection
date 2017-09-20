/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.AreaEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.AreaEvalBase;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ import org.apache.poi.ss.formula.ptg.AreaI;
/*  7:   */ import org.apache.poi.ss.formula.ptg.AreaI.OffsetArea;
/*  8:   */ import org.apache.poi.ss.util.CellReference;
/*  9:   */ 
/* 10:   */ final class LazyAreaEval
/* 11:   */   extends AreaEvalBase
/* 12:   */ {
/* 13:   */   private final SheetRangeEvaluator _evaluator;
/* 14:   */   
/* 15:   */   LazyAreaEval(AreaI ptg, SheetRangeEvaluator evaluator)
/* 16:   */   {
/* 17:34 */     super(ptg, evaluator);
/* 18:35 */     this._evaluator = evaluator;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public LazyAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex, SheetRangeEvaluator evaluator)
/* 22:   */   {
/* 23:40 */     super(evaluator, firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex);
/* 24:41 */     this._evaluator = evaluator;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public ValueEval getRelativeValue(int relativeRowIndex, int relativeColumnIndex)
/* 28:   */   {
/* 29:45 */     return getRelativeValue(getFirstSheetIndex(), relativeRowIndex, relativeColumnIndex);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public ValueEval getRelativeValue(int sheetIndex, int relativeRowIndex, int relativeColumnIndex)
/* 33:   */   {
/* 34:48 */     int rowIx = relativeRowIndex + getFirstRow();
/* 35:49 */     int colIx = relativeColumnIndex + getFirstColumn();
/* 36:   */     
/* 37:51 */     return this._evaluator.getEvalForCell(sheetIndex, rowIx, colIx);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx)
/* 41:   */   {
/* 42:55 */     AreaI area = new OffsetArea(getFirstRow(), getFirstColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
/* 43:   */     
/* 44:   */ 
/* 45:58 */     return new LazyAreaEval(area, this._evaluator);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public LazyAreaEval getRow(int rowIndex)
/* 49:   */   {
/* 50:61 */     if (rowIndex >= getHeight()) {
/* 51:62 */       throw new IllegalArgumentException("Invalid rowIndex " + rowIndex + ".  Allowable range is (0.." + getHeight() + ").");
/* 52:   */     }
/* 53:65 */     int absRowIx = getFirstRow() + rowIndex;
/* 54:66 */     return new LazyAreaEval(absRowIx, getFirstColumn(), absRowIx, getLastColumn(), this._evaluator);
/* 55:   */   }
/* 56:   */   
/* 57:   */   public LazyAreaEval getColumn(int columnIndex)
/* 58:   */   {
/* 59:69 */     if (columnIndex >= getWidth()) {
/* 60:70 */       throw new IllegalArgumentException("Invalid columnIndex " + columnIndex + ".  Allowable range is (0.." + getWidth() + ").");
/* 61:   */     }
/* 62:73 */     int absColIx = getFirstColumn() + columnIndex;
/* 63:74 */     return new LazyAreaEval(getFirstRow(), absColIx, getLastRow(), absColIx, this._evaluator);
/* 64:   */   }
/* 65:   */   
/* 66:   */   public String toString()
/* 67:   */   {
/* 68:78 */     CellReference crA = new CellReference(getFirstRow(), getFirstColumn());
/* 69:79 */     CellReference crB = new CellReference(getLastRow(), getLastColumn());
/* 70:80 */     return getClass().getName() + "[" + this._evaluator.getSheetNameRange() + '!' + crA.formatAsString() + ':' + crB.formatAsString() + "]";
/* 71:   */   }
/* 72:   */   
/* 73:   */   public boolean isSubTotal(int rowIndex, int columnIndex)
/* 74:   */   {
/* 75:94 */     SheetRefEvaluator _sre = this._evaluator.getSheetEvaluator(this._evaluator.getFirstSheetIndex());
/* 76:95 */     return _sre.isSubTotal(getFirstRow() + rowIndex, getFirstColumn() + columnIndex);
/* 77:   */   }
/* 78:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.LazyAreaEval

 * JD-Core Version:    0.7.0.1

 */