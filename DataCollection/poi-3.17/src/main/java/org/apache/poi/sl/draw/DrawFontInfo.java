/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import org.apache.poi.common.usermodel.fonts.FontCharset;
/*  4:   */ import org.apache.poi.common.usermodel.fonts.FontFamily;
/*  5:   */ import org.apache.poi.common.usermodel.fonts.FontInfo;
/*  6:   */ import org.apache.poi.common.usermodel.fonts.FontPitch;
/*  7:   */ import org.apache.poi.util.Internal;
/*  8:   */ 
/*  9:   */ @Internal
/* 10:   */ class DrawFontInfo
/* 11:   */   implements FontInfo
/* 12:   */ {
/* 13:   */   private final String typeface;
/* 14:   */   
/* 15:   */   DrawFontInfo(String typeface)
/* 16:   */   {
/* 17:37 */     this.typeface = typeface;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Integer getIndex()
/* 21:   */   {
/* 22:42 */     return null;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setIndex(int index)
/* 26:   */   {
/* 27:47 */     throw new UnsupportedOperationException("DrawFontManagers FontInfo can't be changed.");
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getTypeface()
/* 31:   */   {
/* 32:52 */     return this.typeface;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setTypeface(String typeface)
/* 36:   */   {
/* 37:57 */     throw new UnsupportedOperationException("DrawFontManagers FontInfo can't be changed.");
/* 38:   */   }
/* 39:   */   
/* 40:   */   public FontCharset getCharset()
/* 41:   */   {
/* 42:62 */     return FontCharset.ANSI;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setCharset(FontCharset charset)
/* 46:   */   {
/* 47:67 */     throw new UnsupportedOperationException("DrawFontManagers FontInfo can't be changed.");
/* 48:   */   }
/* 49:   */   
/* 50:   */   public FontFamily getFamily()
/* 51:   */   {
/* 52:72 */     return FontFamily.FF_SWISS;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void setFamily(FontFamily family)
/* 56:   */   {
/* 57:77 */     throw new UnsupportedOperationException("DrawFontManagers FontInfo can't be changed.");
/* 58:   */   }
/* 59:   */   
/* 60:   */   public FontPitch getPitch()
/* 61:   */   {
/* 62:82 */     return FontPitch.VARIABLE;
/* 63:   */   }
/* 64:   */   
/* 65:   */   public void setPitch(FontPitch pitch)
/* 66:   */   {
/* 67:87 */     throw new UnsupportedOperationException("DrawFontManagers FontInfo can't be changed.");
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawFontInfo
 * JD-Core Version:    0.7.0.1
 */