/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.InputStream;
/*  4:   */ 
/*  5:   */ public abstract interface PaintStyle
/*  6:   */ {
/*  7:   */   public static enum PaintModifier
/*  8:   */   {
/*  9:31 */     NONE,  NORM,  LIGHTEN,  LIGHTEN_LESS,  DARKEN,  DARKEN_LESS;
/* 10:   */     
/* 11:   */     private PaintModifier() {}
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static abstract interface SolidPaint
/* 15:   */     extends PaintStyle
/* 16:   */   {
/* 17:   */     public abstract ColorStyle getSolidColor();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static abstract interface GradientPaint
/* 21:   */     extends PaintStyle
/* 22:   */   {
/* 23:   */     public abstract double getGradientAngle();
/* 24:   */     
/* 25:   */     public abstract ColorStyle[] getGradientColors();
/* 26:   */     
/* 27:   */     public abstract float[] getGradientFractions();
/* 28:   */     
/* 29:   */     public abstract boolean isRotatedWithShape();
/* 30:   */     
/* 31:   */     public abstract GradientType getGradientType();
/* 32:   */     
/* 33:   */     public static enum GradientType
/* 34:   */     {
/* 35:49 */       linear,  circular,  shape;
/* 36:   */       
/* 37:   */       private GradientType() {}
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static abstract interface TexturePaint
/* 42:   */     extends PaintStyle
/* 43:   */   {
/* 44:   */     public abstract InputStream getImageData();
/* 45:   */     
/* 46:   */     public abstract String getContentType();
/* 47:   */     
/* 48:   */     public abstract int getAlpha();
/* 49:   */   }
/* 50:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.PaintStyle
 * JD-Core Version:    0.7.0.1
 */