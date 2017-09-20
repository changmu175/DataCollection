/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ public final class Even
/*  4:   */   extends NumericFunction.OneArg
/*  5:   */ {
/*  6:   */   private static final long PARITY_MASK = -2L;
/*  7:   */   
/*  8:   */   protected double evaluate(double d)
/*  9:   */   {
/* 10:30 */     if (d == 0.0D) {
/* 11:31 */       return 0.0D;
/* 12:   */     }
/* 13:   */     long result;
/* 14:   */     long result;
/* 15:34 */     if (d > 0.0D) {
/* 16:35 */       result = calcEven(d);
/* 17:   */     } else {
/* 18:37 */       result = -calcEven(-d);
/* 19:   */     }
/* 20:39 */     return result;
/* 21:   */   }
/* 22:   */   
/* 23:   */   private static long calcEven(double d)
/* 24:   */   {
/* 25:43 */     long x = d & 0xFFFFFFFE;
/* 26:44 */     if (x == d) {
/* 27:45 */       return x;
/* 28:   */     }
/* 29:47 */     return x + 2L;
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Even
 * JD-Core Version:    0.7.0.1
 */