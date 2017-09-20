/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class LessThanPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 9;
/*  7:   */   private static final String LESSTHAN = "<";
/*  8:33 */   public static final ValueOperatorPtg instance = new LessThanPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:40 */     return 9;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getNumberOfOperands()
/* 16:   */   {
/* 17:48 */     return 2;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toFormulaString(String[] operands)
/* 21:   */   {
/* 22:58 */     StringBuffer buffer = new StringBuffer();
/* 23:59 */     buffer.append(operands[0]);
/* 24:60 */     buffer.append("<");
/* 25:61 */     buffer.append(operands[1]);
/* 26:62 */     return buffer.toString();
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.LessThanPtg
 * JD-Core Version:    0.7.0.1
 */