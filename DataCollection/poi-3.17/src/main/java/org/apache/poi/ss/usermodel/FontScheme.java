/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum FontScheme
/*  4:   */ {
/*  5:33 */   NONE(1),  MAJOR(2),  MINOR(3);
/*  6:   */   
/*  7:   */   private int value;
/*  8:   */   private static FontScheme[] _table;
/*  9:   */   
/* 10:   */   private FontScheme(int val)
/* 11:   */   {
/* 12:40 */     this.value = val;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getValue()
/* 16:   */   {
/* 17:44 */     return this.value;
/* 18:   */   }
/* 19:   */   
/* 20:   */   static
/* 21:   */   {
/* 22:47 */     _table = new FontScheme[4];
/* 23:49 */     for (FontScheme c : values()) {
/* 24:50 */       _table[c.getValue()] = c;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static FontScheme valueOf(int value)
/* 29:   */   {
/* 30:55 */     return _table[value];
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FontScheme
 * JD-Core Version:    0.7.0.1
 */