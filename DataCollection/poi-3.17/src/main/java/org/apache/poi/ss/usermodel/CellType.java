/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ 
/*  5:   */ public enum CellType
/*  6:   */ {
/*  7:32 */   _NONE(-1),  NUMERIC(0),  STRING(1),  FORMULA(2),  BLANK(3),  BOOLEAN(4),  ERROR(5);
/*  8:   */   
/*  9:   */   /**
/* 10:   */    * @deprecated
/* 11:   */    */
/* 12:   */   private final int code;
/* 13:   */   
/* 14:   */   /**
/* 15:   */    * @deprecated
/* 16:   */    */
/* 17:   */   private CellType(int code)
/* 18:   */   {
/* 19:76 */     this.code = code;
/* 20:   */   }
/* 21:   */   
/* 22:   */   /**
/* 23:   */    * @deprecated
/* 24:   */    */
/* 25:   */   public static CellType forInt(int code)
/* 26:   */   {
/* 27:84 */     for (CellType type : ) {
/* 28:85 */       if (type.code == code) {
/* 29:86 */         return type;
/* 30:   */       }
/* 31:   */     }
/* 32:89 */     throw new IllegalArgumentException("Invalid CellType code: " + code);
/* 33:   */   }
/* 34:   */   
/* 35:   */   /**
/* 36:   */    * @deprecated
/* 37:   */    */
/* 38:   */   public int getCode()
/* 39:   */   {
/* 40:97 */     return this.code;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CellType
 * JD-Core Version:    0.7.0.1
 */