/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.apache.poi.hssf.util.HSSFColor;
/*  5:   */ 
/*  6:   */ public class DefaultIndexedColorMap
/*  7:   */   implements IndexedColorMap
/*  8:   */ {
/*  9:   */   public byte[] getRGB(int index)
/* 10:   */   {
/* 11:30 */     return getDefaultRGB(index);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static byte[] getDefaultRGB(int index)
/* 15:   */   {
/* 16:38 */     HSSFColor hssfColor = (HSSFColor)HSSFColor.getIndexHash().get(Integer.valueOf(index));
/* 17:39 */     if (hssfColor == null) {
/* 18:39 */       return null;
/* 19:   */     }
/* 20:40 */     short[] rgbShort = hssfColor.getTriplet();
/* 21:41 */     return new byte[] { (byte)rgbShort[0], (byte)rgbShort[1], (byte)rgbShort[2] };
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.DefaultIndexedColorMap
 * JD-Core Version:    0.7.0.1
 */