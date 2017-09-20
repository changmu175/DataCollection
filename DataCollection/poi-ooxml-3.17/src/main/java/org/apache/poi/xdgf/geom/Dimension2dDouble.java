/*  1:   */ package org.apache.poi.xdgf.geom;
/*  2:   */ 
/*  3:   */ import java.awt.geom.Dimension2D;
/*  4:   */ 
/*  5:   */ public class Dimension2dDouble
/*  6:   */   extends Dimension2D
/*  7:   */ {
/*  8:   */   double width;
/*  9:   */   double height;
/* 10:   */   
/* 11:   */   public Dimension2dDouble()
/* 12:   */   {
/* 13:28 */     this.width = 0.0D;
/* 14:29 */     this.height = 0.0D;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Dimension2dDouble(double width, double height)
/* 18:   */   {
/* 19:33 */     this.width = width;
/* 20:34 */     this.height = height;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public double getWidth()
/* 24:   */   {
/* 25:39 */     return this.width;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public double getHeight()
/* 29:   */   {
/* 30:44 */     return this.height;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setSize(double width, double height)
/* 34:   */   {
/* 35:49 */     this.width = width;
/* 36:50 */     this.height = height;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean equals(Object obj)
/* 40:   */   {
/* 41:55 */     if ((obj instanceof Dimension2dDouble))
/* 42:   */     {
/* 43:56 */       Dimension2dDouble other = (Dimension2dDouble)obj;
/* 44:57 */       return (this.width == other.width) && (this.height == other.height);
/* 45:   */     }
/* 46:60 */     return false;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public int hashCode()
/* 50:   */   {
/* 51:65 */     double sum = this.width + this.height;
/* 52:66 */     return (int)Math.ceil(sum * (sum + 1.0D) / 2.0D + this.width);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String toString()
/* 56:   */   {
/* 57:71 */     return "Dimension2dDouble[" + this.width + ", " + this.height + "]";
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.geom.Dimension2dDouble
 * JD-Core Version:    0.7.0.1
 */