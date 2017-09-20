/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public final class Sumx2py2
/*  4:   */   extends XYNumericFunction
/*  5:   */ {
/*  6:34 */   private static final XYNumericFunction.Accumulator XSquaredPlusYSquaredAccumulator = new XYNumericFunction.Accumulator()
/*  7:   */   {
/*  8:   */     public double accumulate(double x, double y)
/*  9:   */     {
/* 10:36 */       return x * x + y * y;
/* 11:   */     }
/* 12:   */   };
/* 13:   */   
/* 14:   */   protected XYNumericFunction.Accumulator createAccumulator()
/* 15:   */   {
/* 16:41 */     return XSquaredPlusYSquaredAccumulator;
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Sumx2py2
 * JD-Core Version:    0.7.0.1
 */