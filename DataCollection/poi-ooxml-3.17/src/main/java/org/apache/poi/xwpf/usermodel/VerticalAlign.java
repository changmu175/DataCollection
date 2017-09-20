/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum VerticalAlign
/*  7:   */ {
/*  8:36 */   BASELINE(1),  SUPERSCRIPT(2),  SUBSCRIPT(3);
/*  9:   */   
/* 10:   */   private static Map<Integer, VerticalAlign> imap;
/* 11:   */   private final int value;
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:50 */     imap = new HashMap();
/* 16:53 */     for (VerticalAlign p : values()) {
/* 17:54 */       imap.put(Integer.valueOf(p.getValue()), p);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private VerticalAlign(int val)
/* 22:   */   {
/* 23:61 */     this.value = val;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static VerticalAlign valueOf(int type)
/* 27:   */   {
/* 28:65 */     VerticalAlign align = (VerticalAlign)imap.get(Integer.valueOf(type));
/* 29:66 */     if (align == null) {
/* 30:67 */       throw new IllegalArgumentException("Unknown vertical alignment: " + type);
/* 31:   */     }
/* 32:69 */     return align;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getValue()
/* 36:   */   {
/* 37:73 */     return this.value;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.VerticalAlign
 * JD-Core Version:    0.7.0.1
 */