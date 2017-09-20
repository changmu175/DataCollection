/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum PrintCellComments
/*  4:   */ {
/*  5:30 */   NONE(1),  AS_DISPLAYED(2),  AT_END(3);
/*  6:   */   
/*  7:   */   private int comments;
/*  8:   */   private static PrintCellComments[] _table;
/*  9:   */   
/* 10:   */   private PrintCellComments(int comments)
/* 11:   */   {
/* 12:44 */     this.comments = comments;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getValue()
/* 16:   */   {
/* 17:48 */     return this.comments;
/* 18:   */   }
/* 19:   */   
/* 20:   */   static
/* 21:   */   {
/* 22:51 */     _table = new PrintCellComments[4];
/* 23:53 */     for (PrintCellComments c : values()) {
/* 24:54 */       _table[c.getValue()] = c;
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static PrintCellComments valueOf(int value)
/* 29:   */   {
/* 30:59 */     return _table[value];
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.PrintCellComments
 * JD-Core Version:    0.7.0.1
 */