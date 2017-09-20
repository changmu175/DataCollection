/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum VerticalAlignment
/*  4:   */ {
/*  5:31 */   TOP,  CENTER,  BOTTOM,  JUSTIFY,  DISTRIBUTED;
/*  6:   */   
/*  7:   */   private VerticalAlignment() {}
/*  8:   */   
/*  9:   */   public short getCode()
/* 10:   */   {
/* 11:74 */     return (short)ordinal();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static VerticalAlignment forInt(int code)
/* 15:   */   {
/* 16:77 */     if ((code < 0) || (code >= values().length)) {
/* 17:78 */       throw new IllegalArgumentException("Invalid VerticalAlignment code: " + code);
/* 18:   */     }
/* 19:80 */     return values()[code];
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.VerticalAlignment
 * JD-Core Version:    0.7.0.1
 */