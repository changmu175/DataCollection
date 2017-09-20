/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ 
/*   5:    */ final class StatsLib
/*   6:    */ {
/*   7:    */   public static double avedev(double[] v)
/*   8:    */   {
/*   9: 39 */     double r = 0.0D;
/*  10: 40 */     double m = 0.0D;
/*  11: 41 */     double s = 0.0D;
/*  12: 42 */     int i = 0;
/*  13: 42 */     for (int iSize = v.length; i < iSize; i++) {
/*  14: 43 */       s += v[i];
/*  15:    */     }
/*  16: 45 */     m = s / v.length;
/*  17: 46 */     s = 0.0D;
/*  18: 47 */     int i = 0;
/*  19: 47 */     for (int iSize = v.length; i < iSize; i++) {
/*  20: 48 */       s += Math.abs(v[i] - m);
/*  21:    */     }
/*  22: 50 */     r = s / v.length;
/*  23: 51 */     return r;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static double stdev(double[] v)
/*  27:    */   {
/*  28: 55 */     double r = (0.0D / 0.0D);
/*  29: 56 */     if ((v != null) && (v.length > 1)) {
/*  30: 57 */       r = Math.sqrt(devsq(v) / (v.length - 1));
/*  31:    */     }
/*  32: 59 */     return r;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static double var(double[] v)
/*  36:    */   {
/*  37: 63 */     double r = (0.0D / 0.0D);
/*  38: 64 */     if ((v != null) && (v.length > 1)) {
/*  39: 65 */       r = devsq(v) / (v.length - 1);
/*  40:    */     }
/*  41: 67 */     return r;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static double varp(double[] v)
/*  45:    */   {
/*  46: 71 */     double r = (0.0D / 0.0D);
/*  47: 72 */     if ((v != null) && (v.length > 1)) {
/*  48: 73 */       r = devsq(v) / v.length;
/*  49:    */     }
/*  50: 75 */     return r;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static double median(double[] v)
/*  54:    */   {
/*  55: 79 */     double r = (0.0D / 0.0D);
/*  56: 81 */     if ((v != null) && (v.length >= 1))
/*  57:    */     {
/*  58: 82 */       int n = v.length;
/*  59: 83 */       Arrays.sort(v);
/*  60: 84 */       r = n % 2 == 0 ? (v[(n / 2)] + v[(n / 2 - 1)]) / 2.0D : v[(n / 2)];
/*  61:    */     }
/*  62: 89 */     return r;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static double devsq(double[] v)
/*  66:    */   {
/*  67: 94 */     double r = (0.0D / 0.0D);
/*  68: 95 */     if ((v != null) && (v.length >= 1))
/*  69:    */     {
/*  70: 96 */       double m = 0.0D;
/*  71: 97 */       double s = 0.0D;
/*  72: 98 */       int n = v.length;
/*  73: 99 */       for (int i = 0; i < n; i++) {
/*  74:100 */         s += v[i];
/*  75:    */       }
/*  76:102 */       m = s / n;
/*  77:103 */       s = 0.0D;
/*  78:104 */       for (int i = 0; i < n; i++) {
/*  79:105 */         s += (v[i] - m) * (v[i] - m);
/*  80:    */       }
/*  81:108 */       r = n == 1 ? 0.0D : s;
/*  82:    */     }
/*  83:112 */     return r;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static double kthLargest(double[] v, int k)
/*  87:    */   {
/*  88:124 */     double r = (0.0D / 0.0D);
/*  89:125 */     int index = k - 1;
/*  90:126 */     if ((v != null) && (v.length > index) && (index >= 0))
/*  91:    */     {
/*  92:127 */       Arrays.sort(v);
/*  93:128 */       r = v[(v.length - index - 1)];
/*  94:    */     }
/*  95:130 */     return r;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static double kthSmallest(double[] v, int k)
/*  99:    */   {
/* 100:144 */     double r = (0.0D / 0.0D);
/* 101:145 */     int index = k - 1;
/* 102:146 */     if ((v != null) && (v.length > index) && (index >= 0))
/* 103:    */     {
/* 104:147 */       Arrays.sort(v);
/* 105:148 */       r = v[index];
/* 106:    */     }
/* 107:150 */     return r;
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.StatsLib
 * JD-Core Version:    0.7.0.1
 */