/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class EndRecord
/*  8:   */   extends StandardRecord
/*  9:   */   implements Cloneable
/* 10:   */ {
/* 11:   */   public static final short sid = 4148;
/* 12:   */   
/* 13:   */   public EndRecord() {}
/* 14:   */   
/* 15:   */   public EndRecord(RecordInputStream in) {}
/* 16:   */   
/* 17:   */   public String toString()
/* 18:   */   {
/* 19:49 */     StringBuffer buffer = new StringBuffer();
/* 20:   */     
/* 21:51 */     buffer.append("[END]\n");
/* 22:52 */     buffer.append("[/END]\n");
/* 23:53 */     return buffer.toString();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void serialize(LittleEndianOutput out) {}
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:60 */     return 0;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:65 */     return 4148;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public EndRecord clone()
/* 39:   */   {
/* 40:70 */     EndRecord er = new EndRecord();
/* 41:   */     
/* 42:72 */     return er;
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.EndRecord
 * JD-Core Version:    0.7.0.1
 */