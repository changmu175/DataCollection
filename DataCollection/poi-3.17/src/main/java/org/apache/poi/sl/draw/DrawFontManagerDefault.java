/*  1:   */ package org.apache.poi.sl.draw;
/*  2:   */ 
/*  3:   */ import java.awt.Font;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ import java.util.Map;
/*  6:   */ import org.apache.poi.common.usermodel.fonts.FontInfo;
/*  7:   */ 
/*  8:   */ public class DrawFontManagerDefault
/*  9:   */   implements DrawFontManager
/* 10:   */ {
/* 11:   */   public FontInfo getMappedFont(Graphics2D graphics, FontInfo fontInfo)
/* 12:   */   {
/* 13:38 */     return getFontWithFallback(graphics, Drawable.FONT_MAP, fontInfo);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public FontInfo getFallbackFont(Graphics2D graphics, FontInfo fontInfo)
/* 17:   */   {
/* 18:43 */     FontInfo fi = getFontWithFallback(graphics, Drawable.FONT_FALLBACK, fontInfo);
/* 19:44 */     if (fi == null) {
/* 20:45 */       fi = new DrawFontInfo("SansSerif");
/* 21:   */     }
/* 22:47 */     return fi;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String mapFontCharset(Graphics2D graphics, FontInfo fontInfo, String text)
/* 26:   */   {
/* 27:52 */     String attStr = text;
/* 28:53 */     if ((fontInfo != null) && ("Wingdings".equalsIgnoreCase(fontInfo.getTypeface())))
/* 29:   */     {
/* 30:55 */       boolean changed = false;
/* 31:56 */       char[] chrs = attStr.toCharArray();
/* 32:57 */       for (int i = 0; i < chrs.length; i++) {
/* 33:59 */         if (((' ' <= chrs[i]) && (chrs[i] <= '')) || ((' ' <= chrs[i]) && (chrs[i] <= 'ÿ')))
/* 34:   */         {
/* 35:61 */           int tmp88_86 = i; char[] tmp88_84 = chrs;tmp88_84[tmp88_86] = ((char)(tmp88_84[tmp88_86] | 0xF000));
/* 36:62 */           changed = true;
/* 37:   */         }
/* 38:   */       }
/* 39:66 */       if (changed) {
/* 40:67 */         attStr = new String(chrs);
/* 41:   */       }
/* 42:   */     }
/* 43:70 */     return attStr;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public Font createAWTFont(Graphics2D graphics, FontInfo fontInfo, double fontSize, boolean bold, boolean italic)
/* 47:   */   {
/* 48:75 */     int style = (bold ? 1 : 0) | (italic ? 2 : 0);
/* 49:76 */     Font font = new Font(fontInfo.getTypeface(), style, 12);
/* 50:77 */     if ("Dialog".equals(font.getFamily())) {
/* 51:79 */       font = new Font("SansSerif", style, 12);
/* 52:   */     }
/* 53:81 */     return font.deriveFont((float)fontSize);
/* 54:   */   }
/* 55:   */   
/* 56:   */   private FontInfo getFontWithFallback(Graphics2D graphics, Drawable.DrawableHint hint, FontInfo fontInfo)
/* 57:   */   {
/* 58:86 */     Map<String, String> fontMap = (Map)graphics.getRenderingHint(hint);
/* 59:87 */     if (fontMap == null) {
/* 60:88 */       return fontInfo;
/* 61:   */     }
/* 62:91 */     String f = fontInfo != null ? fontInfo.getTypeface() : null;
/* 63:92 */     String mappedTypeface = null;
/* 64:93 */     if (fontMap.containsKey(f)) {
/* 65:94 */       mappedTypeface = (String)fontMap.get(f);
/* 66:95 */     } else if (fontMap.containsKey("*")) {
/* 67:96 */       mappedTypeface = (String)fontMap.get("*");
/* 68:   */     }
/* 69:99 */     return mappedTypeface != null ? new DrawFontInfo(mappedTypeface) : fontInfo;
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawFontManagerDefault
 * JD-Core Version:    0.7.0.1
 */