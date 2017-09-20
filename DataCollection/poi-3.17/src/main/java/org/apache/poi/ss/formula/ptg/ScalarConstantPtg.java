/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public abstract class ScalarConstantPtg
/*  4:   */   extends Ptg
/*  5:   */ {
/*  6:   */   public final boolean isBaseToken()
/*  7:   */   {
/*  8:28 */     return true;
/*  9:   */   }
/* 10:   */   
/* 11:   */   public final byte getDefaultOperandClass()
/* 12:   */   {
/* 13:32 */     return 32;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final String toString()
/* 17:   */   {
/* 18:36 */     StringBuffer sb = new StringBuffer(64);
/* 19:37 */     sb.append(getClass().getName()).append(" [");
/* 20:38 */     sb.append(toFormulaString());
/* 21:39 */     sb.append("]");
/* 22:40 */     return sb.toString();
/* 23:   */   }
/* 24:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ScalarConstantPtg
 * JD-Core Version:    0.7.0.1
 */