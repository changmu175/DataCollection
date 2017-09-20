/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public class Countifs
/*  4:   */   extends Baseifs
/*  5:   */ {
/*  6:32 */   public static final FreeRefFunction instance = new Countifs();
/*  7:   */   
/*  8:   */   protected boolean hasInitialRange()
/*  9:   */   {
/* 10:41 */     return false;
/* 11:   */   }
/* 12:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Countifs
 * JD-Core Version:    0.7.0.1
 */