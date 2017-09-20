/*  1:   */ package org.apache.poi.common.usermodel.fonts;
/*  2:   */ 
/*  3:   */ public enum FontFamily
/*  4:   */ {
/*  5:29 */   FF_DONTCARE(0),  FF_ROMAN(1),  FF_SWISS(2),  FF_MODERN(3),  FF_SCRIPT(4),  FF_DECORATIVE(5);
/*  6:   */   
/*  7:   */   private int nativeId;
/*  8:   */   
/*  9:   */   private FontFamily(int nativeId)
/* 10:   */   {
/* 11:56 */     this.nativeId = nativeId;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getFlag()
/* 15:   */   {
/* 16:60 */     return this.nativeId;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static FontFamily valueOf(int nativeId)
/* 20:   */   {
/* 21:64 */     for (FontFamily ff : ) {
/* 22:65 */       if (ff.nativeId == nativeId) {
/* 23:66 */         return ff;
/* 24:   */       }
/* 25:   */     }
/* 26:69 */     return null;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static FontFamily valueOfPitchFamily(byte pitchAndFamily)
/* 30:   */   {
/* 31:77 */     return valueOf(pitchAndFamily >>> 4);
/* 32:   */   }
/* 33:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.fonts.FontFamily
 * JD-Core Version:    0.7.0.1
 */