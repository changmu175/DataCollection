/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.geom.Rectangle2D;
/*   5:    */ import java.util.List;
/*   6:    */ 
/*   7:    */ public abstract interface TextShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
/*   8:    */   extends SimpleShape<S, P>, Iterable<P>
/*   9:    */ {
/*  10:    */   public abstract String getText();
/*  11:    */   
/*  12:    */   public abstract TextRun setText(String paramString);
/*  13:    */   
/*  14:    */   public abstract TextRun appendText(String paramString, boolean paramBoolean);
/*  15:    */   
/*  16:    */   public abstract List<? extends TextParagraph<S, P, ?>> getTextParagraphs();
/*  17:    */   
/*  18:    */   public abstract Insets2D getInsets();
/*  19:    */   
/*  20:    */   public abstract void setInsets(Insets2D paramInsets2D);
/*  21:    */   
/*  22:    */   public abstract double getTextHeight();
/*  23:    */   
/*  24:    */   public abstract double getTextHeight(Graphics2D paramGraphics2D);
/*  25:    */   
/*  26:    */   public abstract VerticalAlignment getVerticalAlignment();
/*  27:    */   
/*  28:    */   public abstract void setVerticalAlignment(VerticalAlignment paramVerticalAlignment);
/*  29:    */   
/*  30:    */   public abstract boolean isHorizontalCentered();
/*  31:    */   
/*  32:    */   public abstract void setHorizontalCentered(Boolean paramBoolean);
/*  33:    */   
/*  34:    */   public abstract boolean getWordWrap();
/*  35:    */   
/*  36:    */   public abstract void setWordWrap(boolean paramBoolean);
/*  37:    */   
/*  38:    */   public abstract TextDirection getTextDirection();
/*  39:    */   
/*  40:    */   public abstract void setTextDirection(TextDirection paramTextDirection);
/*  41:    */   
/*  42:    */   public abstract Double getTextRotation();
/*  43:    */   
/*  44:    */   public abstract void setTextRotation(Double paramDouble);
/*  45:    */   
/*  46:    */   public abstract void setTextPlaceholder(TextPlaceholder paramTextPlaceholder);
/*  47:    */   
/*  48:    */   public abstract TextPlaceholder getTextPlaceholder();
/*  49:    */   
/*  50:    */   public abstract Rectangle2D resizeToFitText();
/*  51:    */   
/*  52:    */   public abstract Rectangle2D resizeToFitText(Graphics2D paramGraphics2D);
/*  53:    */   
/*  54:    */   public static enum TextDirection
/*  55:    */   {
/*  56: 35 */     HORIZONTAL,  VERTICAL,  VERTICAL_270,  STACKED;
/*  57:    */     
/*  58:    */     private TextDirection() {}
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static enum TextAutofit
/*  62:    */   {
/*  63: 72 */     NONE,  NORMAL,  SHAPE;
/*  64:    */     
/*  65:    */     private TextAutofit() {}
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static enum TextPlaceholder
/*  69:    */   {
/*  70:108 */     TITLE,  BODY,  CENTER_TITLE,  CENTER_BODY,  HALF_BODY,  QUARTER_BODY,  NOTES,  OTHER;
/*  71:    */     
/*  72:    */     private TextPlaceholder() {}
/*  73:    */   }
/*  74:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.TextShape
 * JD-Core Version:    0.7.0.1
 */