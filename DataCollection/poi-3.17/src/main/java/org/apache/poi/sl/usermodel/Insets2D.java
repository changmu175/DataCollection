/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ public final class Insets2D
/*   4:    */   implements Cloneable
/*   5:    */ {
/*   6:    */   public double top;
/*   7:    */   public double left;
/*   8:    */   public double bottom;
/*   9:    */   public double right;
/*  10:    */   
/*  11:    */   public Insets2D(double top, double left, double bottom, double right)
/*  12:    */   {
/*  13: 63 */     this.top = top;
/*  14: 64 */     this.left = left;
/*  15: 65 */     this.bottom = bottom;
/*  16: 66 */     this.right = right;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void set(double top, double left, double bottom, double right)
/*  20:    */   {
/*  21: 79 */     this.top = top;
/*  22: 80 */     this.left = left;
/*  23: 81 */     this.bottom = bottom;
/*  24: 82 */     this.right = right;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean equals(Object obj)
/*  28:    */   {
/*  29: 95 */     if ((obj instanceof Insets2D))
/*  30:    */     {
/*  31: 96 */       Insets2D insets = (Insets2D)obj;
/*  32: 97 */       return (this.top == insets.top) && (this.left == insets.left) && (this.bottom == insets.bottom) && (this.right == insets.right);
/*  33:    */     }
/*  34:100 */     return false;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int hashCode()
/*  38:    */   {
/*  39:109 */     double sum1 = this.left + this.bottom;
/*  40:110 */     double sum2 = this.right + this.top;
/*  41:111 */     double val1 = sum1 * (sum1 + 1.0D) / 2.0D + this.left;
/*  42:112 */     double val2 = sum2 * (sum2 + 1.0D) / 2.0D + this.top;
/*  43:113 */     double sum3 = val1 + val2;
/*  44:114 */     return (int)(sum3 * (sum3 + 1.0D) / 2.0D + val2);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String toString()
/*  48:    */   {
/*  49:127 */     return getClass().getName() + "[top=" + this.top + ",left=" + this.left + ",bottom=" + this.bottom + ",right=" + this.right + "]";
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Insets2D clone()
/*  53:    */   {
/*  54:136 */     return new Insets2D(this.top, this.left, this.bottom, this.right);
/*  55:    */   }
/*  56:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.Insets2D
 * JD-Core Version:    0.7.0.1
 */