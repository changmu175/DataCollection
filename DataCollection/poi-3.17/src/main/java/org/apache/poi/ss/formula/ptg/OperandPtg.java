/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public abstract class OperandPtg
/*  4:   */   extends Ptg
/*  5:   */   implements Cloneable
/*  6:   */ {
/*  7:   */   public final boolean isBaseToken()
/*  8:   */   {
/*  9:29 */     return false;
/* 10:   */   }
/* 11:   */   
/* 12:   */   public final OperandPtg copy()
/* 13:   */   {
/* 14:   */     try
/* 15:   */     {
/* 16:33 */       return (OperandPtg)clone();
/* 17:   */     }
/* 18:   */     catch (CloneNotSupportedException e)
/* 19:   */     {
/* 20:35 */       throw new RuntimeException(e);
/* 21:   */     }
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.OperandPtg
 * JD-Core Version:    0.7.0.1
 */