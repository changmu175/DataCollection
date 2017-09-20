/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class EqualPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 11;
/*  7:27 */   public static final ValueOperatorPtg instance = new EqualPtg();
/*  8:   */   
/*  9:   */   protected byte getSid()
/* 10:   */   {
/* 11:34 */     return 11;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getNumberOfOperands()
/* 15:   */   {
/* 16:38 */     return 2;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toFormulaString(String[] operands)
/* 20:   */   {
/* 21:42 */     StringBuffer buffer = new StringBuffer();
/* 22:   */     
/* 23:   */ 
/* 24:45 */     buffer.append(operands[0]);
/* 25:46 */     buffer.append("=");
/* 26:47 */     buffer.append(operands[1]);
/* 27:48 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.EqualPtg
 * JD-Core Version:    0.7.0.1
 */