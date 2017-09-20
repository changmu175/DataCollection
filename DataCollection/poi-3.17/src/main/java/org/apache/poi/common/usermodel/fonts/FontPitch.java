/*  1:   */ package org.apache.poi.common.usermodel.fonts;
/*  2:   */ 
/*  3:   */ public enum FontPitch
/*  4:   */ {
/*  5:29 */   DEFAULT(0),  FIXED(1),  VARIABLE(2);
/*  6:   */   
/*  7:   */   private int nativeId;
/*  8:   */   
/*  9:   */   private FontPitch(int nativeId)
/* 10:   */   {
/* 11:44 */     this.nativeId = nativeId;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getNativeId()
/* 15:   */   {
/* 16:48 */     return this.nativeId;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static FontPitch valueOf(int flag)
/* 20:   */   {
/* 21:52 */     for (FontPitch fp : ) {
/* 22:53 */       if (fp.nativeId == flag) {
/* 23:53 */         return fp;
/* 24:   */       }
/* 25:   */     }
/* 26:55 */     return null;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static byte getNativeId(FontPitch pitch, FontFamily family)
/* 30:   */   {
/* 31:64 */     return (byte)(pitch.getNativeId() | family.getFlag() << 4);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public static FontPitch valueOfPitchFamily(byte pitchAndFamily)
/* 35:   */   {
/* 36:71 */     return valueOf(pitchAndFamily & 0x3);
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.common.usermodel.fonts.FontPitch
 * JD-Core Version:    0.7.0.1
 */