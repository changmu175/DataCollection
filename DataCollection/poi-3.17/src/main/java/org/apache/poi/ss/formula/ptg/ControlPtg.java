/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ public abstract class ControlPtg
/*  4:   */   extends Ptg
/*  5:   */ {
/*  6:   */   public boolean isBaseToken()
/*  7:   */   {
/*  8:33 */     return true;
/*  9:   */   }
/* 10:   */   
/* 11:   */   public final byte getDefaultOperandClass()
/* 12:   */   {
/* 13:36 */     throw new IllegalStateException("Control tokens are not classified");
/* 14:   */   }
/* 15:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ControlPtg
 * JD-Core Version:    0.7.0.1
 */