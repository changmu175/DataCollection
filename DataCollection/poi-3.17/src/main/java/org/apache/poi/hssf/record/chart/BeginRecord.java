/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class BeginRecord
/*  8:   */   extends StandardRecord
/*  9:   */   implements Cloneable
/* 10:   */ {
/* 11:   */   public static final short sid = 4147;
/* 12:   */   
/* 13:   */   public BeginRecord() {}
/* 14:   */   
/* 15:   */   public BeginRecord(RecordInputStream in) {}
/* 16:   */   
/* 17:   */   public String toString()
/* 18:   */   {
/* 19:48 */     StringBuffer buffer = new StringBuffer();
/* 20:   */     
/* 21:50 */     buffer.append("[BEGIN]\n");
/* 22:51 */     buffer.append("[/BEGIN]\n");
/* 23:52 */     return buffer.toString();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void serialize(LittleEndianOutput out) {}
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:59 */     return 0;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:64 */     return 4147;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public BeginRecord clone()
/* 39:   */   {
/* 40:69 */     BeginRecord br = new BeginRecord();
/* 41:   */     
/* 42:71 */     return br;
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.BeginRecord
 * JD-Core Version:    0.7.0.1
 */