/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ public final class PngUtils
/*  4:   */ {
/*  5:26 */   private static final byte[] PNG_FILE_HEADER = { -119, 80, 78, 71, 13, 10, 26, 10 };
/*  6:   */   
/*  7:   */   public static boolean matchesPngHeader(byte[] data, int offset)
/*  8:   */   {
/*  9:41 */     if ((data == null) || (data.length - offset < PNG_FILE_HEADER.length)) {
/* 10:42 */       return false;
/* 11:   */     }
/* 12:45 */     for (int i = 0; i < PNG_FILE_HEADER.length; i++) {
/* 13:46 */       if (PNG_FILE_HEADER[i] != data[(i + offset)]) {
/* 14:47 */         return false;
/* 15:   */       }
/* 16:   */     }
/* 17:51 */     return true;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.PngUtils
 * JD-Core Version:    0.7.0.1
 */