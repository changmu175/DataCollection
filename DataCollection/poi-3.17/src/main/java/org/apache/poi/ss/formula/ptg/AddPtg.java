/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public final class AddPtg
/*  4:   */   extends ValueOperatorPtg
/*  5:   */ {
/*  6:   */   public static final byte sid = 3;
/*  7:   */   private static final String ADD = "+";
/*  8:31 */   public static final ValueOperatorPtg instance = new AddPtg();
/*  9:   */   
/* 10:   */   protected byte getSid()
/* 11:   */   {
/* 12:38 */     return 3;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getNumberOfOperands()
/* 16:   */   {
/* 17:42 */     return 2;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toFormulaString(String[] operands)
/* 21:   */   {
/* 22:47 */     StringBuffer buffer = new StringBuffer();
/* 23:   */     
/* 24:49 */     buffer.append(operands[0]);
/* 25:50 */     buffer.append("+");
/* 26:51 */     buffer.append(operands[1]);
/* 27:52 */     return buffer.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AddPtg
 * JD-Core Version:    0.7.0.1
 */