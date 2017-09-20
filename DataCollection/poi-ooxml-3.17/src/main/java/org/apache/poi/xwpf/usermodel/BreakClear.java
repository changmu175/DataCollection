/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ public enum BreakClear
/*   7:    */ {
/*   8: 40 */   NONE(1),  LEFT(2),  RIGHT(3),  ALL(4);
/*   9:    */   
/*  10:    */   private static Map<Integer, BreakClear> imap;
/*  11:    */   private final int value;
/*  12:    */   
/*  13:    */   static
/*  14:    */   {
/*  15: 86 */     imap = new HashMap();
/*  16: 89 */     for (BreakClear p : values()) {
/*  17: 90 */       imap.put(Integer.valueOf(p.getValue()), p);
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   private BreakClear(int val)
/*  22:    */   {
/*  23: 97 */     this.value = val;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static BreakClear valueOf(int type)
/*  27:    */   {
/*  28:101 */     BreakClear bType = (BreakClear)imap.get(Integer.valueOf(type));
/*  29:102 */     if (bType == null) {
/*  30:103 */       throw new IllegalArgumentException("Unknown break clear type: " + type);
/*  31:    */     }
/*  32:105 */     return bType;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getValue()
/*  36:    */   {
/*  37:109 */     return this.value;
/*  38:    */   }
/*  39:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.BreakClear
 * JD-Core Version:    0.7.0.1
 */