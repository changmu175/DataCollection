/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import org.apache.poi.common.usermodel.fonts.FontGroup;
/*  5:   */ import org.apache.poi.common.usermodel.fonts.FontInfo;
/*  6:   */ import org.apache.poi.util.Internal;
/*  7:   */ 
/*  8:   */ public abstract interface TextRun
/*  9:   */ {
/* 10:   */   public abstract String getRawText();
/* 11:   */   
/* 12:   */   public abstract void setText(String paramString);
/* 13:   */   
/* 14:   */   public abstract TextCap getTextCap();
/* 15:   */   
/* 16:   */   public abstract PaintStyle getFontColor();
/* 17:   */   
/* 18:   */   public abstract void setFontColor(Color paramColor);
/* 19:   */   
/* 20:   */   public abstract void setFontColor(PaintStyle paramPaintStyle);
/* 21:   */   
/* 22:   */   public abstract Double getFontSize();
/* 23:   */   
/* 24:   */   public abstract void setFontSize(Double paramDouble);
/* 25:   */   
/* 26:   */   public abstract String getFontFamily();
/* 27:   */   
/* 28:   */   public abstract String getFontFamily(FontGroup paramFontGroup);
/* 29:   */   
/* 30:   */   public abstract void setFontFamily(String paramString);
/* 31:   */   
/* 32:   */   public abstract void setFontFamily(String paramString, FontGroup paramFontGroup);
/* 33:   */   
/* 34:   */   public abstract FontInfo getFontInfo(FontGroup paramFontGroup);
/* 35:   */   
/* 36:   */   public abstract void setFontInfo(FontInfo paramFontInfo, FontGroup paramFontGroup);
/* 37:   */   
/* 38:   */   public abstract boolean isBold();
/* 39:   */   
/* 40:   */   public abstract void setBold(boolean paramBoolean);
/* 41:   */   
/* 42:   */   public abstract boolean isItalic();
/* 43:   */   
/* 44:   */   public abstract void setItalic(boolean paramBoolean);
/* 45:   */   
/* 46:   */   public abstract boolean isUnderlined();
/* 47:   */   
/* 48:   */   public abstract void setUnderlined(boolean paramBoolean);
/* 49:   */   
/* 50:   */   public abstract boolean isStrikethrough();
/* 51:   */   
/* 52:   */   public abstract void setStrikethrough(boolean paramBoolean);
/* 53:   */   
/* 54:   */   public abstract boolean isSubscript();
/* 55:   */   
/* 56:   */   public abstract boolean isSuperscript();
/* 57:   */   
/* 58:   */   public abstract byte getPitchAndFamily();
/* 59:   */   
/* 60:   */   public abstract Hyperlink<?, ?> getHyperlink();
/* 61:   */   
/* 62:   */   public abstract Hyperlink<?, ?> createHyperlink();
/* 63:   */   
/* 64:   */   @Internal
/* 65:   */   public abstract FieldType getFieldType();
/* 66:   */   
/* 67:   */   public static enum TextCap
/* 68:   */   {
/* 69:35 */     NONE,  SMALL,  ALL;
/* 70:   */     
/* 71:   */     private TextCap() {}
/* 72:   */   }
/* 73:   */   
/* 74:   */   public static enum FieldType
/* 75:   */   {
/* 76:44 */     SLIDE_NUMBER,  DATE_TIME;
/* 77:   */     
/* 78:   */     private FieldType() {}
/* 79:   */   }
/* 80:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.TextRun
 * JD-Core Version:    0.7.0.1
 */