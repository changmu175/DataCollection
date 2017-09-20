/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class GreaterThanPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 13;
/*  7:   */   private static final String GREATERTHAN = ">";
/*  8:29 */   public static final ValueOperatorPtg instance = new GreaterThanPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:36 */     return 13;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getNumberOfOperands()
/* 16:   */   {
/* 17:44 */     return 2;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toFormulaString(String[] operands)
/* 21:   */   {
/* 22:54 */     StringBuffer buffer = new StringBuffer();
/* 23:   */     
/* 24:56 */     buffer.append(operands[0]);
/* 25:57 */     buffer.append(">");
/* 26:58 */     buffer.append(operands[1]);
/* 27:59 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.GreaterThanPtg
 * JD-Core Version:    0.7.0.1
 */