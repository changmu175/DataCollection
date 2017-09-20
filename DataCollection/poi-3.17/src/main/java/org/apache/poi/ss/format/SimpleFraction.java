/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ public class SimpleFraction
/*   4:    */ {
/*   5:    */   private final int denominator;
/*   6:    */   private final int numerator;
/*   7:    */   
/*   8:    */   public static SimpleFraction buildFractionExactDenominator(double val, int exactDenom)
/*   9:    */   {
/*  10: 35 */     int num = (int)Math.round(val * exactDenom);
/*  11: 36 */     return new SimpleFraction(num, exactDenom);
/*  12:    */   }
/*  13:    */   
/*  14:    */   public static SimpleFraction buildFractionMaxDenominator(double value, int maxDenominator)
/*  15:    */   {
/*  16: 51 */     return buildFractionMaxDenominator(value, 0.0D, maxDenominator, 100);
/*  17:    */   }
/*  18:    */   
/*  19:    */   private static SimpleFraction buildFractionMaxDenominator(double value, double epsilon, int maxDenominator, int maxIterations)
/*  20:    */   {
/*  21: 78 */     long overflow = 2147483647L;
/*  22: 79 */     double r0 = value;
/*  23: 80 */     long a0 = Math.floor(r0);
/*  24: 81 */     if (a0 > overflow) {
/*  25: 82 */       throw new IllegalArgumentException("Overflow trying to convert " + value + " to fraction (" + a0 + "/" + 1L + ")");
/*  26:    */     }
/*  27: 87 */     if (Math.abs(a0 - value) < epsilon) {
/*  28: 88 */       return new SimpleFraction((int)a0, 1);
/*  29:    */     }
/*  30: 91 */     long p0 = 1L;
/*  31: 92 */     long q0 = 0L;
/*  32: 93 */     long p1 = a0;
/*  33: 94 */     long q1 = 1L;
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38: 99 */     int n = 0;
/*  39:100 */     boolean stop = false;
/*  40:    */     long p2;
/*  41:    */     long q2;
/*  42:    */     do
/*  43:    */     {
/*  44:102 */       n++;
/*  45:103 */       double r1 = 1.0D / (r0 - a0);
/*  46:104 */       long a1 = Math.floor(r1);
/*  47:105 */       p2 = a1 * p1 + p0;
/*  48:106 */       q2 = a1 * q1 + q0;
/*  49:108 */       if ((epsilon == 0.0D) && (maxDenominator > 0) && (Math.abs(q2) > maxDenominator) && (Math.abs(q1) < maxDenominator)) {
/*  50:111 */         return new SimpleFraction((int)p1, (int)q1);
/*  51:    */       }
/*  52:113 */       if ((p2 > overflow) || (q2 > overflow)) {
/*  53:114 */         throw new RuntimeException("Overflow trying to convert " + value + " to fraction (" + p2 + "/" + q2 + ")");
/*  54:    */       }
/*  55:117 */       double convergent = p2 / q2;
/*  56:118 */       if ((n < maxIterations) && (Math.abs(convergent - value) > epsilon) && (q2 < maxDenominator))
/*  57:    */       {
/*  58:119 */         p0 = p1;
/*  59:120 */         p1 = p2;
/*  60:121 */         q0 = q1;
/*  61:122 */         q1 = q2;
/*  62:123 */         a0 = a1;
/*  63:124 */         r0 = r1;
/*  64:    */       }
/*  65:    */       else
/*  66:    */       {
/*  67:126 */         stop = true;
/*  68:    */       }
/*  69:128 */     } while (!stop);
/*  70:130 */     if (n >= maxIterations) {
/*  71:131 */       throw new RuntimeException("Unable to convert " + value + " to fraction after " + maxIterations + " iterations");
/*  72:    */     }
/*  73:134 */     if (q2 < maxDenominator) {
/*  74:135 */       return new SimpleFraction((int)p2, (int)q2);
/*  75:    */     }
/*  76:137 */     return new SimpleFraction((int)p1, (int)q1);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public SimpleFraction(int numerator, int denominator)
/*  80:    */   {
/*  81:149 */     this.numerator = numerator;
/*  82:150 */     this.denominator = denominator;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getDenominator()
/*  86:    */   {
/*  87:158 */     return this.denominator;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getNumerator()
/*  91:    */   {
/*  92:166 */     return this.numerator;
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.SimpleFraction
 * JD-Core Version:    0.7.0.1
 */