/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor;
/*  7:   */ 
/*  8:   */ public class CustomIndexedColorMap
/*  9:   */   implements IndexedColorMap
/* 10:   */ {
/* 11:   */   private final byte[][] colorIndex;
/* 12:   */   
/* 13:   */   private CustomIndexedColorMap(byte[][] colors)
/* 14:   */   {
/* 15:35 */     this.colorIndex = colors;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public byte[] getRGB(int index)
/* 19:   */   {
/* 20:39 */     if ((this.colorIndex == null) || (index < 0) || (index >= this.colorIndex.length)) {
/* 21:39 */       return null;
/* 22:   */     }
/* 23:40 */     return this.colorIndex[index];
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static CustomIndexedColorMap fromColors(CTColors colors)
/* 27:   */   {
/* 28:56 */     if ((colors == null) || (!colors.isSetIndexedColors())) {
/* 29:56 */       return null;
/* 30:   */     }
/* 31:58 */     List<CTRgbColor> rgbColorList = colors.getIndexedColors().getRgbColorList();
/* 32:59 */     byte[][] customColorIndex = new byte[rgbColorList.size()][3];
/* 33:60 */     for (int i = 0; i < rgbColorList.size(); i++) {
/* 34:61 */       customColorIndex[i] = ((CTRgbColor)rgbColorList.get(i)).getRgb();
/* 35:   */     }
/* 36:63 */     return new CustomIndexedColorMap(customColorIndex);
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.CustomIndexedColorMap
 * JD-Core Version:    0.7.0.1
 */