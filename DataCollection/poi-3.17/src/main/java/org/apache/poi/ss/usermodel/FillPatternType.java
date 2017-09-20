/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ public enum FillPatternType
/*  4:   */ {
/*  5:27 */   NO_FILL(0),  SOLID_FOREGROUND(1),  FINE_DOTS(2),  ALT_BARS(3),  SPARSE_DOTS(4),  THICK_HORZ_BANDS(5),  THICK_VERT_BANDS(6),  THICK_BACKWARD_DIAG(7),  THICK_FORWARD_DIAG(8),  BIG_SPOTS(9),  BRICKS(10),  THIN_HORZ_BANDS(11),  THIN_VERT_BANDS(12),  THIN_BACKWARD_DIAG(13),  THIN_FORWARD_DIAG(14),  SQUARES(15),  DIAMONDS(16),  LESS_DOTS(17),  LEAST_DOTS(18);
/*  6:   */   
/*  7:   */   private final short code;
/*  8:   */   
/*  9:   */   private FillPatternType(int code)
/* 10:   */   {
/* 11:86 */     this.code = ((short)code);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public short getCode()
/* 15:   */   {
/* 16:90 */     return this.code;
/* 17:   */   }
/* 18:   */   
/* 19:93 */   private static final int length = values().length;
/* 20:   */   
/* 21:   */   public static FillPatternType forInt(int code)
/* 22:   */   {
/* 23:95 */     if ((code < 0) || (code > length)) {
/* 24:96 */       throw new IllegalArgumentException("Invalid FillPatternType code: " + code);
/* 25:   */     }
/* 26:98 */     return values()[code];
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FillPatternType
 * JD-Core Version:    0.7.0.1
 */