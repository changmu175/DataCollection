/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ 
/*   5:    */ public final class NumberComparer
/*   6:    */ {
/*   7:    */   public static int compare(double a, double b)
/*   8:    */   {
/*   9: 62 */     long rawBitsA = Double.doubleToLongBits(a);
/*  10: 63 */     long rawBitsB = Double.doubleToLongBits(b);
/*  11:    */     
/*  12: 65 */     int biasedExponentA = IEEEDouble.getBiasedExponent(rawBitsA);
/*  13: 66 */     int biasedExponentB = IEEEDouble.getBiasedExponent(rawBitsB);
/*  14: 68 */     if (biasedExponentA == 2047) {
/*  15: 69 */       throw new IllegalArgumentException("Special double values are not allowed: " + toHex(a));
/*  16:    */     }
/*  17: 71 */     if (biasedExponentB == 2047) {
/*  18: 72 */       throw new IllegalArgumentException("Special double values are not allowed: " + toHex(a));
/*  19:    */     }
/*  20: 78 */     boolean aIsNegative = rawBitsA < 0L;
/*  21: 79 */     boolean bIsNegative = rawBitsB < 0L;
/*  22: 82 */     if (aIsNegative != bIsNegative) {
/*  23: 85 */       return aIsNegative ? -1 : 1;
/*  24:    */     }
/*  25: 89 */     int cmp = biasedExponentA - biasedExponentB;
/*  26: 90 */     int absExpDiff = Math.abs(cmp);
/*  27: 91 */     if (absExpDiff > 1) {
/*  28: 92 */       return aIsNegative ? -cmp : cmp;
/*  29:    */     }
/*  30: 95 */     if (absExpDiff != 1) {
/*  31:100 */       if (rawBitsA == rawBitsB) {
/*  32:102 */         return 0;
/*  33:    */       }
/*  34:    */     }
/*  35:105 */     if (biasedExponentA == 0)
/*  36:    */     {
/*  37:106 */       if (biasedExponentB == 0) {
/*  38:107 */         return compareSubnormalNumbers(rawBitsA & 0xFFFFFFFF, rawBitsB & 0xFFFFFFFF, aIsNegative);
/*  39:    */       }
/*  40:110 */       return -compareAcrossSubnormalThreshold(rawBitsB, rawBitsA, aIsNegative);
/*  41:    */     }
/*  42:112 */     if (biasedExponentB == 0) {
/*  43:114 */       return compareAcrossSubnormalThreshold(rawBitsA, rawBitsB, aIsNegative);
/*  44:    */     }
/*  45:119 */     ExpandedDouble edA = ExpandedDouble.fromRawBitsAndExponent(rawBitsA, biasedExponentA - 1023);
/*  46:120 */     ExpandedDouble edB = ExpandedDouble.fromRawBitsAndExponent(rawBitsB, biasedExponentB - 1023);
/*  47:121 */     NormalisedDecimal ndA = edA.normaliseBaseTen().roundUnits();
/*  48:122 */     NormalisedDecimal ndB = edB.normaliseBaseTen().roundUnits();
/*  49:123 */     cmp = ndA.compareNormalised(ndB);
/*  50:124 */     if (aIsNegative) {
/*  51:125 */       return -cmp;
/*  52:    */     }
/*  53:127 */     return cmp;
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static int compareSubnormalNumbers(long fracA, long fracB, boolean isNegative)
/*  57:    */   {
/*  58:134 */     int cmp = fracA < fracB ? -1 : fracA > fracB ? 1 : 0;
/*  59:    */     
/*  60:136 */     return isNegative ? -cmp : cmp;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static int compareAcrossSubnormalThreshold(long normalRawBitsA, long subnormalRawBitsB, boolean isNegative)
/*  64:    */   {
/*  65:148 */     long fracB = subnormalRawBitsB & 0xFFFFFFFF;
/*  66:149 */     if (fracB == 0L) {
/*  67:151 */       return isNegative ? -1 : 1;
/*  68:    */     }
/*  69:153 */     long fracA = normalRawBitsA & 0xFFFFFFFF;
/*  70:154 */     if ((fracA <= 7L) && (fracB >= 4503599627370490L))
/*  71:    */     {
/*  72:156 */       if ((fracA == 7L) && (fracB == 4503599627370490L)) {
/*  73:158 */         return 0;
/*  74:    */       }
/*  75:161 */       return isNegative ? 1 : -1;
/*  76:    */     }
/*  77:164 */     return isNegative ? -1 : 1;
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static String toHex(double a)
/*  81:    */   {
/*  82:173 */     return "0x" + Long.toHexString(Double.doubleToLongBits(a)).toUpperCase(Locale.ROOT);
/*  83:    */   }
/*  84:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.NumberComparer
 * JD-Core Version:    0.7.0.1
 */