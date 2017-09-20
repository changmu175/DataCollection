/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum PrintOrientation
/*  4:   */ {
/*  5:30 */   DEFAULT(1),  PORTRAIT(2),  LANDSCAPE(3);
/*  6:   */   
/*  7:   */   private int orientation;
/*  8:   */   private static PrintOrientation[] _table;
/*  9:   */   
/* 10:   */   private PrintOrientation(int orientation)
/* 11:   */   {
/* 12:44 */     this.orientation = orientation;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getValue()
/* 16:   */   {
/* 17:49 */     return this.orientation;
/* 18:   */   }
/* 19:   */   
/* 20:   */   static
/* 21:   */   {
/* 22:53 */     _table = new PrintOrientation[4];
/* 23:55 */     for (PrintOrientation c : values()) {
/* 24:56 */       _table[c.getValue()] = c;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static PrintOrientation valueOf(int value)
/* 29:   */   {
/* 30:61 */     return _table[value];
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.PrintOrientation
 * JD-Core Version:    0.7.0.1
 */