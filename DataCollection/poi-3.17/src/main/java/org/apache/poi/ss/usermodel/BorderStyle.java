/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public enum BorderStyle
/*   4:    */ {
/*   5: 30 */   NONE(0),  THIN(1),  MEDIUM(2),  DASHED(3),  DOTTED(4),  THICK(5),  DOUBLE(6),  HAIR(7),  MEDIUM_DASHED(8),  DASH_DOT(9),  MEDIUM_DASH_DOT(10),  DASH_DOT_DOT(11),  MEDIUM_DASH_DOT_DOT(12),  SLANTED_DASH_DOT(13);
/*   6:    */   
/*   7:    */   private final short code;
/*   8:    */   private static final BorderStyle[] _table;
/*   9:    */   
/*  10:    */   private BorderStyle(int code)
/*  11:    */   {
/*  12:100 */     this.code = ((short)code);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public short getCode()
/*  16:    */   {
/*  17:104 */     return this.code;
/*  18:    */   }
/*  19:    */   
/*  20:    */   static
/*  21:    */   {
/*  22:107 */     _table = new BorderStyle[14];
/*  23:109 */     for (BorderStyle c : values()) {
/*  24:110 */       _table[c.getCode()] = c;
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static BorderStyle valueOf(short code)
/*  29:    */   {
/*  30:115 */     return _table[code];
/*  31:    */   }
/*  32:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.BorderStyle
 * JD-Core Version:    0.7.0.1
 */