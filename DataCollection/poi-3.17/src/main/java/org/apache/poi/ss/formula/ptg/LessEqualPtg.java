/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class LessEqualPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 10;
/*  7:32 */   public static final ValueOperatorPtg instance = new LessEqualPtg();
/*  8:   */   
/*  9:   */   protected byte getSid()
/* 10:   */   {
/* 11:39 */     return 10;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getNumberOfOperands()
/* 15:   */   {
/* 16:43 */     return 2;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toFormulaString(String[] operands)
/* 20:   */   {
/* 21:47 */     StringBuffer buffer = new StringBuffer();
/* 22:48 */     buffer.append(operands[0]);
/* 23:49 */     buffer.append("<=");
/* 24:50 */     buffer.append(operands[1]);
/* 25:51 */     return buffer.toString();
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.LessEqualPtg
 * JD-Core Version:    0.7.0.1
 */