/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum BreakType
/*  7:   */ {
/*  8:37 */   PAGE(1),  COLUMN(2),  TEXT_WRAPPING(3);
/*  9:   */   
/* 10:   */   private static Map<Integer, BreakType> imap;
/* 11:   */   private final int value;
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:59 */     imap = new HashMap();
/* 16:62 */     for (BreakType p : values()) {
/* 17:63 */       imap.put(Integer.valueOf(p.getValue()), p);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private BreakType(int val)
/* 22:   */   {
/* 23:70 */     this.value = val;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static BreakType valueOf(int type)
/* 27:   */   {
/* 28:74 */     BreakType bType = (BreakType)imap.get(Integer.valueOf(type));
/* 29:75 */     if (bType == null) {
/* 30:76 */       throw new IllegalArgumentException("Unknown break type: " + type);
/* 31:   */     }
/* 32:78 */     return bType;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getValue()
/* 36:   */   {
/* 37:82 */     return this.value;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.BreakType
 * JD-Core Version:    0.7.0.1
 */