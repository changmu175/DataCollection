/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum LineSpacingRule
/*  7:   */ {
/*  8:36 */   AUTO(1),  EXACT(2),  AT_LEAST(3);
/*  9:   */   
/* 10:   */   private static Map<Integer, LineSpacingRule> imap;
/* 11:   */   private final int value;
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:52 */     imap = new HashMap();
/* 16:55 */     for (LineSpacingRule p : values()) {
/* 17:56 */       imap.put(Integer.valueOf(p.getValue()), p);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private LineSpacingRule(int val)
/* 22:   */   {
/* 23:63 */     this.value = val;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static LineSpacingRule valueOf(int type)
/* 27:   */   {
/* 28:67 */     LineSpacingRule lineType = (LineSpacingRule)imap.get(Integer.valueOf(type));
/* 29:68 */     if (lineType == null) {
/* 30:69 */       throw new IllegalArgumentException("Unknown line type: " + type);
/* 31:   */     }
/* 32:70 */     return lineType;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getValue()
/* 36:   */   {
/* 37:74 */     return this.value;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.LineSpacingRule
 * JD-Core Version:    0.7.0.1
 */