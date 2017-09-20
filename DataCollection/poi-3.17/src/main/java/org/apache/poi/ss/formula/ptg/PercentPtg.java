/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class PercentPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final int SIZE = 1;
/*  7:   */   public static final byte sid = 20;
/*  8:   */   private static final String PERCENT = "%";
/*  9:31 */   public static final ValueOperatorPtg instance = new PercentPtg();
/* 10:   */   
/* 11:   */   protected byte getSid()
/* 12:   */   {
/* 13:38 */     return 20;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public int getNumberOfOperands()
/* 17:   */   {
/* 18:42 */     return 1;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toFormulaString(String[] operands)
/* 22:   */   {
/* 23:46 */     StringBuffer buffer = new StringBuffer();
/* 24:   */     
/* 25:48 */     buffer.append(operands[0]);
/* 26:49 */     buffer.append("%");
/* 27:50 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.PercentPtg
 * JD-Core Version:    0.7.0.1
 */