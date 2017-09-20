/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  4:   */ 
/*  5:   */ final class SheetRangeEvaluator
/*  6:   */   implements SheetRange
/*  7:   */ {
/*  8:   */   private final int _firstSheetIndex;
/*  9:   */   private final int _lastSheetIndex;
/* 10:   */   private SheetRefEvaluator[] _sheetEvaluators;
/* 11:   */   
/* 12:   */   public SheetRangeEvaluator(int firstSheetIndex, int lastSheetIndex, SheetRefEvaluator[] sheetEvaluators)
/* 13:   */   {
/* 14:31 */     if (firstSheetIndex < 0) {
/* 15:32 */       throw new IllegalArgumentException("Invalid firstSheetIndex: " + firstSheetIndex + ".");
/* 16:   */     }
/* 17:34 */     if (lastSheetIndex < firstSheetIndex) {
/* 18:35 */       throw new IllegalArgumentException("Invalid lastSheetIndex: " + lastSheetIndex + " for firstSheetIndex: " + firstSheetIndex + ".");
/* 19:   */     }
/* 20:37 */     this._firstSheetIndex = firstSheetIndex;
/* 21:38 */     this._lastSheetIndex = lastSheetIndex;
/* 22:39 */     this._sheetEvaluators = ((SheetRefEvaluator[])sheetEvaluators.clone());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public SheetRangeEvaluator(int onlySheetIndex, SheetRefEvaluator sheetEvaluator)
/* 26:   */   {
/* 27:42 */     this(onlySheetIndex, onlySheetIndex, new SheetRefEvaluator[] { sheetEvaluator });
/* 28:   */   }
/* 29:   */   
/* 30:   */   public SheetRefEvaluator getSheetEvaluator(int sheetIndex)
/* 31:   */   {
/* 32:46 */     if ((sheetIndex < this._firstSheetIndex) || (sheetIndex > this._lastSheetIndex)) {
/* 33:47 */       throw new IllegalArgumentException("Invalid SheetIndex: " + sheetIndex + " - Outside range " + this._firstSheetIndex + " : " + this._lastSheetIndex);
/* 34:   */     }
/* 35:50 */     return this._sheetEvaluators[(sheetIndex - this._firstSheetIndex)];
/* 36:   */   }
/* 37:   */   
/* 38:   */   public int getFirstSheetIndex()
/* 39:   */   {
/* 40:54 */     return this._firstSheetIndex;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getLastSheetIndex()
/* 44:   */   {
/* 45:57 */     return this._lastSheetIndex;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String getSheetName(int sheetIndex)
/* 49:   */   {
/* 50:61 */     return getSheetEvaluator(sheetIndex).getSheetName();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String getSheetNameRange()
/* 54:   */   {
/* 55:64 */     StringBuilder sb = new StringBuilder();
/* 56:65 */     sb.append(getSheetName(this._firstSheetIndex));
/* 57:66 */     if (this._firstSheetIndex != this._lastSheetIndex)
/* 58:   */     {
/* 59:67 */       sb.append(':');
/* 60:68 */       sb.append(getSheetName(this._lastSheetIndex));
/* 61:   */     }
/* 62:70 */     return sb.toString();
/* 63:   */   }
/* 64:   */   
/* 65:   */   public ValueEval getEvalForCell(int sheetIndex, int rowIndex, int columnIndex)
/* 66:   */   {
/* 67:74 */     return getSheetEvaluator(sheetIndex).getEvalForCell(rowIndex, columnIndex);
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SheetRangeEvaluator
 * JD-Core Version:    0.7.0.1
 */