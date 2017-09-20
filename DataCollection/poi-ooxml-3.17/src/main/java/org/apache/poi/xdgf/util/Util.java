/*  1:   */ package org.apache.poi.xdgf.util;
/*  2:   */ 
/*  3:   */ public class Util
/*  4:   */ {
/*  5:   */   public static int countLines(String str)
/*  6:   */   {
/*  7:23 */     int lines = 1;
/*  8:24 */     int pos = 0;
/*  9:25 */     while ((pos = str.indexOf("\n", pos) + 1) != 0) {
/* 10:26 */       lines++;
/* 11:   */     }
/* 12:28 */     return lines;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static String sanitizeFilename(String name)
/* 16:   */   {
/* 17:35 */     return name.replaceAll("[:\\\\/*\"?|<>]", "_");
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.util.Util
 * JD-Core Version:    0.7.0.1
 */