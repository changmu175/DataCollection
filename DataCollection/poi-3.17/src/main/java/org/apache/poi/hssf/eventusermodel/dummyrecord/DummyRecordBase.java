/*  1:   */ package org.apache.poi.hssf.eventusermodel.dummyrecord;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.Record;
/*  4:   */ import org.apache.poi.util.RecordFormatException;
/*  5:   */ 
/*  6:   */ abstract class DummyRecordBase
/*  7:   */   extends Record
/*  8:   */ {
/*  9:   */   public final short getSid()
/* 10:   */   {
/* 11:32 */     return -1;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public int serialize(int offset, byte[] data)
/* 15:   */   {
/* 16:35 */     throw new RecordFormatException("Cannot serialize a dummy record");
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final int getRecordSize()
/* 20:   */   {
/* 21:38 */     throw new RecordFormatException("Cannot serialize a dummy record");
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.dummyrecord.DummyRecordBase
 * JD-Core Version:    0.7.0.1
 */