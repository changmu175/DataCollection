/*  1:   */ package org.apache.poi.wp.usermodel;
/*  2:   */ 
/*  3:   */ public enum HeaderFooterType
/*  4:   */ {
/*  5:30 */   DEFAULT(2),  EVEN(1),  FIRST(3);
/*  6:   */   
/*  7:   */   private final int code;
/*  8:   */   
/*  9:   */   private HeaderFooterType(int i)
/* 10:   */   {
/* 11:47 */     this.code = i;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int toInt()
/* 15:   */   {
/* 16:51 */     return this.code;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static HeaderFooterType forInt(int i)
/* 20:   */   {
/* 21:55 */     for (HeaderFooterType type : ) {
/* 22:56 */       if (type.code == i) {
/* 23:57 */         return type;
/* 24:   */       }
/* 25:   */     }
/* 26:60 */     throw new IllegalArgumentException("Invalid HeaderFooterType code: " + i);
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.wp.usermodel.HeaderFooterType
 * JD-Core Version:    0.7.0.1
 */