/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public enum UnderlinePatterns
/*   7:    */ {
/*   8: 34 */   SINGLE(1),  WORDS(2),  DOUBLE(3),  THICK(4),  DOTTED(5),  DOTTED_HEAVY(6),  DASH(7),  DASHED_HEAVY(8),  DASH_LONG(9),  DASH_LONG_HEAVY(10),  DOT_DASH(11),  DASH_DOT_HEAVY(12),  DOT_DOT_DASH(13),  DASH_DOT_DOT_HEAVY(14),  WAVE(15),  WAVY_HEAVY(16),  WAVY_DOUBLE(17),  NONE(18);
/*   9:    */   
/*  10:    */   private static Map<Integer, UnderlinePatterns> imap;
/*  11:    */   private final int value;
/*  12:    */   
/*  13:    */   static
/*  14:    */   {
/*  15:138 */     imap = new HashMap();
/*  16:141 */     for (UnderlinePatterns p : values()) {
/*  17:142 */       imap.put(Integer.valueOf(p.getValue()), p);
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   private UnderlinePatterns(int val)
/*  22:    */   {
/*  23:149 */     this.value = val;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static UnderlinePatterns valueOf(int type)
/*  27:    */   {
/*  28:153 */     UnderlinePatterns align = (UnderlinePatterns)imap.get(Integer.valueOf(type));
/*  29:154 */     if (align == null) {
/*  30:155 */       throw new IllegalArgumentException("Unknown underline pattern: " + type);
/*  31:    */     }
/*  32:157 */     return align;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getValue()
/*  36:    */   {
/*  37:161 */     return this.value;
/*  38:    */   }
/*  39:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.UnderlinePatterns
 * JD-Core Version:    0.7.0.1
 */