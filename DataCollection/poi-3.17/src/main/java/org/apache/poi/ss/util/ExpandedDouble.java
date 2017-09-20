/*  1:   */ package org.apache.poi.ss.util;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ 
/*  5:   */ final class ExpandedDouble
/*  6:   */ {
/*  7:38 */   private static final BigInteger BI_FRAC_MASK = BigInteger.valueOf(4503599627370495L);
/*  8:39 */   private static final BigInteger BI_IMPLIED_FRAC_MSB = BigInteger.valueOf(4503599627370496L);
/*  9:   */   private final BigInteger _significand;
/* 10:   */   private final int _binaryExponent;
/* 11:   */   
/* 12:   */   private static BigInteger getFrac(long rawBits)
/* 13:   */   {
/* 14:42 */     return BigInteger.valueOf(rawBits).and(BI_FRAC_MASK).or(BI_IMPLIED_FRAC_MSB).shiftLeft(11);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public static ExpandedDouble fromRawBitsAndExponent(long rawBits, int exp)
/* 18:   */   {
/* 19:47 */     return new ExpandedDouble(getFrac(rawBits), exp);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ExpandedDouble(long rawBits)
/* 23:   */   {
/* 24:57 */     int biasedExp = (int)(rawBits >> 52);
/* 25:58 */     if (biasedExp == 0)
/* 26:   */     {
/* 27:60 */       BigInteger frac = BigInteger.valueOf(rawBits).and(BI_FRAC_MASK);
/* 28:61 */       int expAdj = 64 - frac.bitLength();
/* 29:62 */       this._significand = frac.shiftLeft(expAdj);
/* 30:63 */       this._binaryExponent = ((biasedExp & 0x7FF) - 1023 - expAdj);
/* 31:   */     }
/* 32:   */     else
/* 33:   */     {
/* 34:65 */       BigInteger frac = getFrac(rawBits);
/* 35:66 */       this._significand = frac;
/* 36:67 */       this._binaryExponent = ((biasedExp & 0x7FF) - 1023);
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   ExpandedDouble(BigInteger frac, int binaryExp)
/* 41:   */   {
/* 42:72 */     if (frac.bitLength() != 64) {
/* 43:73 */       throw new IllegalArgumentException("bad bit length");
/* 44:   */     }
/* 45:75 */     this._significand = frac;
/* 46:76 */     this._binaryExponent = binaryExp;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public NormalisedDecimal normaliseBaseTen()
/* 50:   */   {
/* 51:85 */     return NormalisedDecimal.create(this._significand, this._binaryExponent);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public int getBinaryExponent()
/* 55:   */   {
/* 56:92 */     return this._binaryExponent;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public BigInteger getSignificand()
/* 60:   */   {
/* 61:96 */     return this._significand;
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.ExpandedDouble
 * JD-Core Version:    0.7.0.1
 */