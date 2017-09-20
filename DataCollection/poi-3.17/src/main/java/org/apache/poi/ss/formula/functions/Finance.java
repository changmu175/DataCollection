/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ public class Finance
/*   4:    */ {
/*   5:    */   public static double pmt(double r, int nper, double pv, double fv, int type)
/*   6:    */   {
/*   7: 43 */     double pmt = -r * (pv * Math.pow(1.0D + r, nper) + fv) / ((1.0D + r * type) * (Math.pow(1.0D + r, nper) - 1.0D));
/*   8: 44 */     return pmt;
/*   9:    */   }
/*  10:    */   
/*  11:    */   public static double pmt(double r, int nper, double pv, double fv)
/*  12:    */   {
/*  13: 54 */     return pmt(r, nper, pv, fv, 0);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public static double pmt(double r, int nper, double pv)
/*  17:    */   {
/*  18: 63 */     return pmt(r, nper, pv, 0.0D);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static double ipmt(double r, int per, int nper, double pv, double fv, int type)
/*  22:    */   {
/*  23: 91 */     double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type), pv, type) * r;
/*  24: 92 */     if (type == 1) {
/*  25: 92 */       ipmt /= (1.0D + r);
/*  26:    */     }
/*  27: 93 */     return ipmt;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static double ipmt(double r, int per, int nper, double pv, double fv)
/*  31:    */   {
/*  32: 97 */     return ipmt(r, per, nper, pv, fv, 0);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static double ipmt(double r, int per, int nper, double pv)
/*  36:    */   {
/*  37:101 */     return ipmt(r, per, nper, pv, 0.0D);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static double ppmt(double r, int per, int nper, double pv, double fv, int type)
/*  41:    */   {
/*  42:127 */     return pmt(r, nper, pv, fv, type) - ipmt(r, per, nper, pv, fv, type);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static double ppmt(double r, int per, int nper, double pv, double fv)
/*  46:    */   {
/*  47:131 */     return pmt(r, nper, pv, fv) - ipmt(r, per, nper, pv, fv);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static double ppmt(double r, int per, int nper, double pv)
/*  51:    */   {
/*  52:135 */     return pmt(r, nper, pv) - ipmt(r, per, nper, pv);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static double fv(double r, int nper, double pmt, double pv, int type)
/*  56:    */   {
/*  57:156 */     double fv = -(pv * Math.pow(1.0D + r, nper) + pmt * (1.0D + r * type) * (Math.pow(1.0D + r, nper) - 1.0D) / r);
/*  58:157 */     return fv;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static double fv(double r, int nper, double c, double pv)
/*  62:    */   {
/*  63:166 */     return fv(r, nper, c, pv, 0);
/*  64:    */   }
/*  65:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Finance
 * JD-Core Version:    0.7.0.1
 */