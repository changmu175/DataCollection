/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public enum HorizontalAlignment
/*   4:    */ {
/*   5: 31 */   GENERAL,  LEFT,  CENTER,  RIGHT,  FILL,  JUSTIFY,  CENTER_SELECTION,  DISTRIBUTED;
/*   6:    */   
/*   7:    */   private HorizontalAlignment() {}
/*   8:    */   
/*   9:    */   public short getCode()
/*  10:    */   {
/*  11: 97 */     return (short)ordinal();
/*  12:    */   }
/*  13:    */   
/*  14:    */   public static HorizontalAlignment forInt(int code)
/*  15:    */   {
/*  16:100 */     if ((code < 0) || (code >= values().length)) {
/*  17:101 */       throw new IllegalArgumentException("Invalid HorizontalAlignment code: " + code);
/*  18:    */     }
/*  19:103 */     return values()[code];
/*  20:    */   }
/*  21:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.HorizontalAlignment
 * JD-Core Version:    0.7.0.1
 */