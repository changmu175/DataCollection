/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum TextAlignment
/*  7:   */ {
/*  8:33 */   TOP(1),  CENTER(2),  BASELINE(3),  BOTTOM(4),  AUTO(5);
/*  9:   */   
/* 10:   */   private static Map<Integer, TextAlignment> imap;
/* 11:   */   private final int value;
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:55 */     imap = new HashMap();
/* 16:58 */     for (TextAlignment p : values()) {
/* 17:59 */       imap.put(Integer.valueOf(p.getValue()), p);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private TextAlignment(int val)
/* 22:   */   {
/* 23:66 */     this.value = val;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static TextAlignment valueOf(int type)
/* 27:   */   {
/* 28:70 */     TextAlignment align = (TextAlignment)imap.get(Integer.valueOf(type));
/* 29:71 */     if (align == null) {
/* 30:71 */       throw new IllegalArgumentException("Unknown text alignment: " + type);
/* 31:   */     }
/* 32:72 */     return align;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getValue()
/* 36:   */   {
/* 37:76 */     return this.value;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.TextAlignment
 * JD-Core Version:    0.7.0.1
 */