/*  1:   */ package org.apache.poi.hssf.eventusermodel.dummyrecord;
/*  2:   */ 
/*  3:   */ public final class MissingCellDummyRecord
/*  4:   */   extends DummyRecordBase
/*  5:   */ {
/*  6:   */   private int row;
/*  7:   */   private int column;
/*  8:   */   
/*  9:   */   public MissingCellDummyRecord(int row, int column)
/* 10:   */   {
/* 11:30 */     this.row = row;
/* 12:31 */     this.column = column;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getRow()
/* 16:   */   {
/* 17:33 */     return this.row;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public int getColumn()
/* 21:   */   {
/* 22:34 */     return this.column;
/* 23:   */   }
/* 24:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord
 * JD-Core Version:    0.7.0.1
 */