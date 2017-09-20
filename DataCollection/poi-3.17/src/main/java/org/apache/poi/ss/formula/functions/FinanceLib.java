/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ public final class FinanceLib
/*   4:    */ {
/*   5:    */   public static double fv(double r, double n, double y, double p, boolean t)
/*   6:    */   {
/*   7: 77 */     double retval = 0.0D;
/*   8: 78 */     if (r == 0.0D)
/*   9:    */     {
/*  10: 79 */       retval = -1.0D * (p + n * y);
/*  11:    */     }
/*  12:    */     else
/*  13:    */     {
/*  14: 82 */       double r1 = r + 1.0D;
/*  15: 83 */       retval = (1.0D - Math.pow(r1, n)) * (t ? r1 : 1.0D) * y / r - p * Math.pow(r1, n);
/*  16:    */     }
/*  17: 87 */     return retval;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static double pv(double r, double n, double y, double f, boolean t)
/*  21:    */   {
/*  22:102 */     double retval = 0.0D;
/*  23:103 */     if (r == 0.0D)
/*  24:    */     {
/*  25:104 */       retval = -1.0D * (n * y + f);
/*  26:    */     }
/*  27:    */     else
/*  28:    */     {
/*  29:107 */       double r1 = r + 1.0D;
/*  30:108 */       retval = ((1.0D - Math.pow(r1, n)) / r * (t ? r1 : 1.0D) * y - f) / Math.pow(r1, n);
/*  31:    */     }
/*  32:112 */     return retval;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static double npv(double r, double[] cfs)
/*  36:    */   {
/*  37:125 */     double npv = 0.0D;
/*  38:126 */     double r1 = r + 1.0D;
/*  39:127 */     double trate = r1;
/*  40:128 */     int i = 0;
/*  41:128 */     for (int iSize = cfs.length; i < iSize; i++)
/*  42:    */     {
/*  43:129 */       npv += cfs[i] / trate;
/*  44:130 */       trate *= r1;
/*  45:    */     }
/*  46:132 */     return npv;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static double pmt(double r, double n, double p, double f, boolean t)
/*  50:    */   {
/*  51:144 */     double retval = 0.0D;
/*  52:145 */     if (r == 0.0D)
/*  53:    */     {
/*  54:146 */       retval = -1.0D * (f + p) / n;
/*  55:    */     }
/*  56:    */     else
/*  57:    */     {
/*  58:149 */       double r1 = r + 1.0D;
/*  59:150 */       retval = (f + p * Math.pow(r1, n)) * r / ((t ? r1 : 1.0D) * (1.0D - Math.pow(r1, n)));
/*  60:    */     }
/*  61:154 */     return retval;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static double nper(double r, double y, double p, double f, boolean t)
/*  65:    */   {
/*  66:166 */     double retval = 0.0D;
/*  67:167 */     if (r == 0.0D)
/*  68:    */     {
/*  69:168 */       retval = -1.0D * (f + p) / y;
/*  70:    */     }
/*  71:    */     else
/*  72:    */     {
/*  73:170 */       double r1 = r + 1.0D;
/*  74:171 */       double ryr = (t ? r1 : 1.0D) * y / r;
/*  75:172 */       double a1 = ryr - f < 0.0D ? Math.log(f - ryr) : Math.log(ryr - f);
/*  76:    */       
/*  77:    */ 
/*  78:175 */       double a2 = ryr - f < 0.0D ? Math.log(-p - ryr) : Math.log(p + ryr);
/*  79:    */       
/*  80:    */ 
/*  81:178 */       double a3 = Math.log(r1);
/*  82:179 */       retval = (a1 - a2) / a3;
/*  83:    */     }
/*  84:181 */     return retval;
/*  85:    */   }
/*  86:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.FinanceLib
 * JD-Core Version:    0.7.0.1
 */