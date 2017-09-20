/*  1:   */ package org.apache.poi.hssf.eventusermodel.dummyrecord;
/*  2:   */ 
/*  3:   */ public final class MissingRowDummyRecord
/*  4:   */   extends DummyRecordBase
/*  5:   */ {
/*  6:   */   private int rowNumber;
/*  7:   */   
/*  8:   */   public MissingRowDummyRecord(int rowNumber)
/*  9:   */   {
/* 10:29 */     this.rowNumber = rowNumber;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public int getRowNumber()
/* 14:   */   {
/* 15:32 */     return this.rowNumber;
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord
 * JD-Core Version:    0.7.0.1
 */