/*  1:   */ package org.apache.poi.hssf.eventusermodel.dummyrecord;
/*  2:   */ 
/*  3:   */ public final class LastCellOfRowDummyRecord
/*  4:   */   extends DummyRecordBase
/*  5:   */ {
/*  6:   */   private final int row;
/*  7:   */   private final int lastColumnNumber;
/*  8:   */   
/*  9:   */   public LastCellOfRowDummyRecord(int row, int lastColumnNumber)
/* 10:   */   {
/* 11:30 */     this.row = row;
/* 12:31 */     this.lastColumnNumber = lastColumnNumber;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getRow()
/* 16:   */   {
/* 17:41 */     return this.row;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getLastColumnNumber()
/* 21:   */   {
/* 22:54 */     return this.lastColumnNumber;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String toString()
/* 26:   */   {
/* 27:59 */     return "End-of-Row for Row=" + this.row + " at Column=" + this.lastColumnNumber;
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord
 * JD-Core Version:    0.7.0.1
 */