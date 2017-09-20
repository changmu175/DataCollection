/*  1:   */ package org.apache.poi.ss.util;
/*  2:   */ 
/*  3:   */ final class IEEEDouble
/*  4:   */ {
/*  5:   */   private static final long EXPONENT_MASK = 9218868437227405312L;
/*  6:   */   private static final int EXPONENT_SHIFT = 52;
/*  7:   */   public static final long FRAC_MASK = 4503599627370495L;
/*  8:   */   public static final int EXPONENT_BIAS = 1023;
/*  9:   */   public static final long FRAC_ASSUMED_HIGH_BIT = 4503599627370496L;
/* 10:   */   public static final int BIASED_EXPONENT_SPECIAL_VALUE = 2047;
/* 11:   */   
/* 12:   */   public static int getBiasedExponent(long rawBits)
/* 13:   */   {
/* 14:42 */     return (int)((rawBits & 0x0) >> 52);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.IEEEDouble
 * JD-Core Version:    0.7.0.1
 */