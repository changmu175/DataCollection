/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.SheetRange;
/*  4:   */ 
/*  5:   */ public abstract class RefEvalBase
/*  6:   */   implements RefEval
/*  7:   */ {
/*  8:   */   private final int _firstSheetIndex;
/*  9:   */   private final int _lastSheetIndex;
/* 10:   */   private final int _rowIndex;
/* 11:   */   private final int _columnIndex;
/* 12:   */   
/* 13:   */   protected RefEvalBase(SheetRange sheetRange, int rowIndex, int columnIndex)
/* 14:   */   {
/* 15:32 */     if (sheetRange == null) {
/* 16:33 */       throw new IllegalArgumentException("sheetRange must not be null");
/* 17:   */     }
/* 18:35 */     this._firstSheetIndex = sheetRange.getFirstSheetIndex();
/* 19:36 */     this._lastSheetIndex = sheetRange.getLastSheetIndex();
/* 20:37 */     this._rowIndex = rowIndex;
/* 21:38 */     this._columnIndex = columnIndex;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected RefEvalBase(int firstSheetIndex, int lastSheetIndex, int rowIndex, int columnIndex)
/* 25:   */   {
/* 26:41 */     this._firstSheetIndex = firstSheetIndex;
/* 27:42 */     this._lastSheetIndex = lastSheetIndex;
/* 28:43 */     this._rowIndex = rowIndex;
/* 29:44 */     this._columnIndex = columnIndex;
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected RefEvalBase(int onlySheetIndex, int rowIndex, int columnIndex)
/* 33:   */   {
/* 34:47 */     this(onlySheetIndex, onlySheetIndex, rowIndex, columnIndex);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int getNumberOfSheets()
/* 38:   */   {
/* 39:51 */     return this._lastSheetIndex - this._firstSheetIndex + 1;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public int getFirstSheetIndex()
/* 43:   */   {
/* 44:54 */     return this._firstSheetIndex;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public int getLastSheetIndex()
/* 48:   */   {
/* 49:57 */     return this._lastSheetIndex;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public final int getRow()
/* 53:   */   {
/* 54:60 */     return this._rowIndex;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public final int getColumn()
/* 58:   */   {
/* 59:63 */     return this._columnIndex;
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.RefEvalBase
 * JD-Core Version:    0.7.0.1
 */