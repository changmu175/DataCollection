/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.util.List;
/*   5:    */ 
/*   6:    */ public abstract interface TextParagraph<S extends Shape<S, P>, P extends TextParagraph<S, P, T>, T extends TextRun>
/*   7:    */   extends Iterable<T>
/*   8:    */ {
/*   9:    */   public abstract Double getSpaceBefore();
/*  10:    */   
/*  11:    */   public abstract void setSpaceBefore(Double paramDouble);
/*  12:    */   
/*  13:    */   public abstract Double getSpaceAfter();
/*  14:    */   
/*  15:    */   public abstract void setSpaceAfter(Double paramDouble);
/*  16:    */   
/*  17:    */   public abstract Double getLeftMargin();
/*  18:    */   
/*  19:    */   public abstract void setLeftMargin(Double paramDouble);
/*  20:    */   
/*  21:    */   public abstract Double getRightMargin();
/*  22:    */   
/*  23:    */   public abstract void setRightMargin(Double paramDouble);
/*  24:    */   
/*  25:    */   public abstract Double getIndent();
/*  26:    */   
/*  27:    */   public abstract void setIndent(Double paramDouble);
/*  28:    */   
/*  29:    */   public abstract int getIndentLevel();
/*  30:    */   
/*  31:    */   public abstract void setIndentLevel(int paramInt);
/*  32:    */   
/*  33:    */   public abstract Double getLineSpacing();
/*  34:    */   
/*  35:    */   public abstract void setLineSpacing(Double paramDouble);
/*  36:    */   
/*  37:    */   public abstract String getDefaultFontFamily();
/*  38:    */   
/*  39:    */   public abstract Double getDefaultFontSize();
/*  40:    */   
/*  41:    */   public abstract TextAlign getTextAlign();
/*  42:    */   
/*  43:    */   public abstract void setTextAlign(TextAlign paramTextAlign);
/*  44:    */   
/*  45:    */   public abstract FontAlign getFontAlign();
/*  46:    */   
/*  47:    */   public abstract BulletStyle getBulletStyle();
/*  48:    */   
/*  49:    */   public abstract void setBulletStyle(Object... paramVarArgs);
/*  50:    */   
/*  51:    */   public abstract Double getDefaultTabSize();
/*  52:    */   
/*  53:    */   public abstract TextShape<S, P> getParentShape();
/*  54:    */   
/*  55:    */   public abstract List<T> getTextRuns();
/*  56:    */   
/*  57:    */   public abstract boolean isHeaderOrFooter();
/*  58:    */   
/*  59:    */   public static enum TextAlign
/*  60:    */   {
/*  61: 39 */     LEFT,  CENTER,  RIGHT,  JUSTIFY,  JUSTIFY_LOW,  DIST,  THAI_DIST;
/*  62:    */     
/*  63:    */     private TextAlign() {}
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static enum FontAlign
/*  67:    */   {
/*  68: 83 */     AUTO,  TOP,  CENTER,  BASELINE,  BOTTOM;
/*  69:    */     
/*  70:    */     private FontAlign() {}
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static abstract interface BulletStyle
/*  74:    */   {
/*  75:    */     public abstract String getBulletCharacter();
/*  76:    */     
/*  77:    */     public abstract String getBulletFont();
/*  78:    */     
/*  79:    */     public abstract Double getBulletFontSize();
/*  80:    */     
/*  81:    */     public abstract void setBulletFontColor(Color paramColor);
/*  82:    */     
/*  83:    */     public abstract void setBulletFontColor(PaintStyle paramPaintStyle);
/*  84:    */     
/*  85:    */     public abstract PaintStyle getBulletFontColor();
/*  86:    */     
/*  87:    */     public abstract AutoNumberingScheme getAutoNumberingScheme();
/*  88:    */     
/*  89:    */     public abstract Integer getAutoNumberingStartAt();
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.TextParagraph
 * JD-Core Version:    0.7.0.1
 */