/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum PageOrder
/*  4:   */ {
/*  5:30 */   DOWN_THEN_OVER(1),  OVER_THEN_DOWN(2);
/*  6:   */   
/*  7:   */   private final int order;
/*  8:   */   private static PageOrder[] _table;
/*  9:   */   
/* 10:   */   private PageOrder(int order)
/* 11:   */   {
/* 12:41 */     this.order = order;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getValue()
/* 16:   */   {
/* 17:45 */     return this.order;
/* 18:   */   }
/* 19:   */   
/* 20:   */   static
/* 21:   */   {
/* 22:49 */     _table = new PageOrder[3];
/* 23:51 */     for (PageOrder c : values()) {
/* 24:52 */       _table[c.getValue()] = c;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static PageOrder valueOf(int value)
/* 29:   */   {
/* 30:57 */     return _table[value];
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.PageOrder
 * JD-Core Version:    0.7.0.1
 */