/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public abstract interface AreaI
/*  4:   */ {
/*  5:   */   public abstract int getFirstRow();
/*  6:   */   
/*  7:   */   public abstract int getLastRow();
/*  8:   */   
/*  9:   */   public abstract int getFirstColumn();
/* 10:   */   
/* 11:   */   public abstract int getLastColumn();
/* 12:   */   
/* 13:   */   public static class OffsetArea
/* 14:   */     implements AreaI
/* 15:   */   {
/* 16:   */     private final int _firstColumn;
/* 17:   */     private final int _firstRow;
/* 18:   */     private final int _lastColumn;
/* 19:   */     private final int _lastRow;
/* 20:   */     
/* 21:   */     public OffsetArea(int baseRow, int baseColumn, int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx)
/* 22:   */     {
/* 23:53 */       this._firstRow = (baseRow + Math.min(relFirstRowIx, relLastRowIx));
/* 24:54 */       this._lastRow = (baseRow + Math.max(relFirstRowIx, relLastRowIx));
/* 25:55 */       this._firstColumn = (baseColumn + Math.min(relFirstColIx, relLastColIx));
/* 26:56 */       this._lastColumn = (baseColumn + Math.max(relFirstColIx, relLastColIx));
/* 27:   */     }
/* 28:   */     
/* 29:   */     public int getFirstColumn()
/* 30:   */     {
/* 31:60 */       return this._firstColumn;
/* 32:   */     }
/* 33:   */     
/* 34:   */     public int getFirstRow()
/* 35:   */     {
/* 36:64 */       return this._firstRow;
/* 37:   */     }
/* 38:   */     
/* 39:   */     public int getLastColumn()
/* 40:   */     {
/* 41:68 */       return this._lastColumn;
/* 42:   */     }
/* 43:   */     
/* 44:   */     public int getLastRow()
/* 45:   */     {
/* 46:72 */       return this._lastRow;
/* 47:   */     }
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AreaI
 * JD-Core Version:    0.7.0.1
 */