/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public final class Sumxmy2
/*  4:   */   extends XYNumericFunction
/*  5:   */ {
/*  6:33 */   private static final XYNumericFunction.Accumulator XMinusYSquaredAccumulator = new XYNumericFunction.Accumulator()
/*  7:   */   {
/*  8:   */     public double accumulate(double x, double y)
/*  9:   */     {
/* 10:35 */       double xmy = x - y;
/* 11:36 */       return xmy * xmy;
/* 12:   */     }
/* 13:   */   };
/* 14:   */   
/* 15:   */   protected XYNumericFunction.Accumulator createAccumulator()
/* 16:   */   {
/* 17:41 */     return XMinusYSquaredAccumulator;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Sumxmy2
 * JD-Core Version:    0.7.0.1
 */