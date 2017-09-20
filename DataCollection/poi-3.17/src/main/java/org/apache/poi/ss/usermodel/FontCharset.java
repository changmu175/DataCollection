/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ 
/*  5:   */ @Removal(version="4.0")
/*  6:   */ @Deprecated
/*  7:   */ public enum FontCharset
/*  8:   */ {
/*  9:32 */   ANSI(0),  DEFAULT(1),  SYMBOL(2),  MAC(77),  SHIFTJIS(128),  HANGEUL(129),  JOHAB(130),  GB2312(134),  CHINESEBIG5(136),  GREEK(161),  TURKISH(162),  VIETNAMESE(163),  HEBREW(177),  ARABIC(178),  BALTIC(186),  RUSSIAN(204),  THAI(222),  EASTEUROPE(238),  OEM(255);
/* 10:   */   
/* 11:   */   private int charset;
/* 12:   */   private static FontCharset[] _table;
/* 13:   */   
/* 14:   */   private FontCharset(int value)
/* 15:   */   {
/* 16:56 */     this.charset = value;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public int getValue()
/* 20:   */   {
/* 21:65 */     return this.charset;
/* 22:   */   }
/* 23:   */   
/* 24:   */   static
/* 25:   */   {
/* 26:68 */     _table = new FontCharset[256];
/* 27:70 */     for (FontCharset c : values()) {
/* 28:71 */       _table[c.getValue()] = c;
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static FontCharset valueOf(int value)
/* 33:   */   {
/* 34:76 */     if (value >= _table.length) {
/* 35:77 */       return null;
/* 36:   */     }
/* 37:78 */     return _table[value];
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FontCharset
 * JD-Core Version:    0.7.0.1
 */