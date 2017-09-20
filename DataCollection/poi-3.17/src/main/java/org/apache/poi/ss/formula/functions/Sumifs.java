/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public final class Sumifs
/*  4:   */   extends Baseifs
/*  5:   */ {
/*  6:46 */   public static final FreeRefFunction instance = new Sumifs();
/*  7:   */   
/*  8:   */   protected boolean hasInitialRange()
/*  9:   */   {
/* 10:56 */     return true;
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Sumifs
 * JD-Core Version:    0.7.0.1
 */