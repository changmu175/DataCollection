/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public abstract class OperationPtg
/*  4:   */   extends Ptg
/*  5:   */ {
/*  6:   */   public static final int TYPE_UNARY = 0;
/*  7:   */   public static final int TYPE_BINARY = 1;
/*  8:   */   public static final int TYPE_FUNCTION = 2;
/*  9:   */   
/* 10:   */   public abstract String toFormulaString(String[] paramArrayOfString);
/* 11:   */   
/* 12:   */   public abstract int getNumberOfOperands();
/* 13:   */   
/* 14:   */   public byte getDefaultOperandClass()
/* 15:   */   {
/* 16:43 */     return 32;
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.OperationPtg
 * JD-Core Version:    0.7.0.1
 */