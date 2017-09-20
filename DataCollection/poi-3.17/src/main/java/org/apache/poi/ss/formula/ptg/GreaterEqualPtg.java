/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class GreaterEqualPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final int SIZE = 1;
/*  7:   */   public static final byte sid = 12;
/*  8:30 */   public static final ValueOperatorPtg instance = new GreaterEqualPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:37 */     return 12;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getNumberOfOperands()
/* 16:   */   {
/* 17:41 */     return 2;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toFormulaString(String[] operands)
/* 21:   */   {
/* 22:45 */     StringBuffer buffer = new StringBuffer();
/* 23:   */     
/* 24:47 */     buffer.append(operands[0]);
/* 25:   */     
/* 26:49 */     buffer.append(">=");
/* 27:50 */     buffer.append(operands[1]);
/* 28:   */     
/* 29:52 */     return buffer.toString();
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.GreaterEqualPtg
 * JD-Core Version:    0.7.0.1
 */