/*  1:   */ package org.apache.poi.poifs.common;
/*  2:   */ 
/*  3:   */ public final class POIFSBigBlockSize
/*  4:   */ {
/*  5:   */   private int bigBlockSize;
/*  6:   */   private short headerValue;
/*  7:   */   
/*  8:   */   protected POIFSBigBlockSize(int bigBlockSize, short headerValue)
/*  9:   */   {
/* 10:33 */     this.bigBlockSize = bigBlockSize;
/* 11:34 */     this.headerValue = headerValue;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int getBigBlockSize()
/* 15:   */   {
/* 16:38 */     return this.bigBlockSize;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public short getHeaderValue()
/* 20:   */   {
/* 21:48 */     return this.headerValue;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getPropertiesPerBlock()
/* 25:   */   {
/* 26:52 */     return this.bigBlockSize / 128;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public int getBATEntriesPerBlock()
/* 30:   */   {
/* 31:56 */     return this.bigBlockSize / 4;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int getXBATEntriesPerBlock()
/* 35:   */   {
/* 36:59 */     return getBATEntriesPerBlock() - 1;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getNextXBATChainOffset()
/* 40:   */   {
/* 41:62 */     return getXBATEntriesPerBlock() * 4;
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.common.POIFSBigBlockSize
 * JD-Core Version:    0.7.0.1
 */