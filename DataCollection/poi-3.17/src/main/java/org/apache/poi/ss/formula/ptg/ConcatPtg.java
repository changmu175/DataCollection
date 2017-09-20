/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class ConcatPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 8;
/*  7:   */   private static final String CONCAT = "&";
/*  8:30 */   public static final ValueOperatorPtg instance = new ConcatPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:37 */     return 8;
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
/* 25:48 */     buffer.append("&");
/* 26:49 */     buffer.append(operands[1]);
/* 27:50 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ConcatPtg
 * JD-Core Version:    0.7.0.1
 */