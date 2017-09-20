/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public final class Odd
/*  4:   */   extends NumericFunction.OneArg
/*  5:   */ {
/*  6:   */   private static final long PARITY_MASK = -2L;
/*  7:   */   
/*  8:   */   protected double evaluate(double d)
/*  9:   */   {
/* 10:28 */     if (d == 0.0D) {
/* 11:29 */       return 1.0D;
/* 12:   */     }
/* 13:31 */     return d > 0.0D ? calcOdd(d) : -calcOdd(-d);
/* 14:   */   }
/* 15:   */   
/* 16:   */   private static long calcOdd(double d)
/* 17:   */   {
/* 18:35 */     double dpm1 = d + 1.0D;
/* 19:36 */     long x = dpm1 & 0xFFFFFFFE;
/* 20:37 */     return Double.compare(x, dpm1) == 0 ? x - 1L : x + 1L;
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Odd
 * JD-Core Version:    0.7.0.1
 */