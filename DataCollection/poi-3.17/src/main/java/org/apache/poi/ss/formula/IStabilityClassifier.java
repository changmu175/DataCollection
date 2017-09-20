/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ public abstract interface IStabilityClassifier
/*  4:   */ {
/*  5:64 */   public static final IStabilityClassifier TOTALLY_IMMUTABLE = new IStabilityClassifier()
/*  6:   */   {
/*  7:   */     public boolean isCellFinal(int sheetIndex, int rowIndex, int columnIndex)
/*  8:   */     {
/*  9:66 */       return true;
/* 10:   */     }
/* 11:   */   };
/* 12:   */   
/* 13:   */   public abstract boolean isCellFinal(int paramInt1, int paramInt2, int paramInt3);
/* 14:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.IStabilityClassifier
 * JD-Core Version:    0.7.0.1
 */