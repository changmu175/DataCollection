/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ public enum RectAlign
/*  4:   */ {
/*  5:29 */   TOP_LEFT("tl"),  TOP("t"),  TOP_RIGHT("tr"),  LEFT("l"),  CENTER("ctr"),  RIGHT("r"),  BOTTOM_LEFT("bl"),  BOTTOM("b"),  BOTTOM_RIGHT("br");
/*  6:   */   
/*  7:   */   private final String dir;
/*  8:   */   
/*  9:   */   private RectAlign(String dir)
/* 10:   */   {
/* 11:59 */     this.dir = dir;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String toString()
/* 15:   */   {
/* 16:69 */     return this.dir;
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.RectAlign
 * JD-Core Version:    0.7.0.1
 */