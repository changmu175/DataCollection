/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class UnaryMinusPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 19;
/*  7:   */   private static final String MINUS = "-";
/*  8:30 */   public static final ValueOperatorPtg instance = new UnaryMinusPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:37 */     return 19;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getNumberOfOperands()
/* 16:   */   {
/* 17:41 */     return 1;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toFormulaString(String[] operands)
/* 21:   */   {
/* 22:46 */     StringBuffer buffer = new StringBuffer();
/* 23:47 */     buffer.append("-");
/* 24:48 */     buffer.append(operands[0]);
/* 25:49 */     return buffer.toString();
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.UnaryMinusPtg
 * JD-Core Version:    0.7.0.1
 */