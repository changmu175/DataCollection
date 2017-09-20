/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public enum ParagraphAlignment
/*  7:   */ {
/*  8:32 */   LEFT(1),  CENTER(2),  RIGHT(3),  BOTH(4),  MEDIUM_KASHIDA(5),  DISTRIBUTE(6),  NUM_TAB(7),  HIGH_KASHIDA(8),  LOW_KASHIDA(9),  THAI_DISTRIBUTE(10);
/*  9:   */   
/* 10:   */   private static Map<Integer, ParagraphAlignment> imap;
/* 11:   */   private final int value;
/* 12:   */   
/* 13:   */   static
/* 14:   */   {
/* 15:43 */     imap = new HashMap();
/* 16:46 */     for (ParagraphAlignment p : values()) {
/* 17:47 */       imap.put(Integer.valueOf(p.getValue()), p);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private ParagraphAlignment(int val)
/* 22:   */   {
/* 23:54 */     this.value = val;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static ParagraphAlignment valueOf(int type)
/* 27:   */   {
/* 28:58 */     ParagraphAlignment err = (ParagraphAlignment)imap.get(Integer.valueOf(type));
/* 29:59 */     if (err == null) {
/* 30:59 */       throw new IllegalArgumentException("Unknown paragraph alignment: " + type);
/* 31:   */     }
/* 32:60 */     return err;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getValue()
/* 36:   */   {
/* 37:64 */     return this.value;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.ParagraphAlignment
 * JD-Core Version:    0.7.0.1
 */