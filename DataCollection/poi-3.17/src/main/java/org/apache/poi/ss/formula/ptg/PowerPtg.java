/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class PowerPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 7;
/*  7:28 */   public static final ValueOperatorPtg instance = new PowerPtg();
/*  8:   */   
/*  9:   */   protected byte getSid()
/* 10:   */   {
/* 11:35 */     return 7;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getNumberOfOperands()
/* 15:   */   {
/* 16:39 */     return 2;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toFormulaString(String[] operands)
/* 20:   */   {
/* 21:43 */     StringBuffer buffer = new StringBuffer();
/* 22:   */     
/* 23:   */ 
/* 24:46 */     buffer.append(operands[0]);
/* 25:47 */     buffer.append("^");
/* 26:48 */     buffer.append(operands[1]);
/* 27:49 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.PowerPtg
 * JD-Core Version:    0.7.0.1
 */