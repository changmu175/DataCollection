/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum FontFamily
/*  4:   */ {
/*  5:29 */   NOT_APPLICABLE(0),  ROMAN(1),  SWISS(2),  MODERN(3),  SCRIPT(4),  DECORATIVE(5);
/*  6:   */   
/*  7:   */   private int family;
/*  8:   */   private static FontFamily[] _table;
/*  9:   */   
/* 10:   */   private FontFamily(int value)
/* 11:   */   {
/* 12:39 */     this.family = value;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getValue()
/* 16:   */   {
/* 17:48 */     return this.family;
/* 18:   */   }
/* 19:   */   
/* 20:   */   static
/* 21:   */   {
/* 22:51 */     _table = new FontFamily[6];
/* 23:54 */     for (FontFamily c : values()) {
/* 24:55 */       _table[c.getValue()] = c;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static FontFamily valueOf(int family)
/* 29:   */   {
/* 30:60 */     return _table[family];
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FontFamily
 * JD-Core Version:    0.7.0.1
 */