/*  1:   */ package org.apache.poi.openxml4j.opc;
/*  2:   */ 
/*  3:   */ public enum CompressionOption
/*  4:   */ {
/*  5:30 */   FAST(1),  MAXIMUM(9),  NORMAL(-1),  NOT_COMPRESSED(0);
/*  6:   */   
/*  7:   */   private final int value;
/*  8:   */   
/*  9:   */   private CompressionOption(int value)
/* 10:   */   {
/* 11:41 */     this.value = value;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int value()
/* 15:   */   {
/* 16:45 */     return this.value;
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.CompressionOption
 * JD-Core Version:    0.7.0.1
 */