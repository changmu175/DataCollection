/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public final class PlotAreaRecord
/*  8:   */   extends StandardRecord
/*  9:   */ {
/* 10:   */   public static final short sid = 4149;
/* 11:   */   
/* 12:   */   public PlotAreaRecord() {}
/* 13:   */   
/* 14:   */   public PlotAreaRecord(RecordInputStream in) {}
/* 15:   */   
/* 16:   */   public String toString()
/* 17:   */   {
/* 18:46 */     StringBuffer buffer = new StringBuffer();
/* 19:   */     
/* 20:48 */     buffer.append("[PLOTAREA]\n");
/* 21:   */     
/* 22:50 */     buffer.append("[/PLOTAREA]\n");
/* 23:51 */     return buffer.toString();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void serialize(LittleEndianOutput out) {}
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:58 */     return 0;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:63 */     return 4149;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public Object clone()
/* 39:   */   {
/* 40:67 */     PlotAreaRecord rec = new PlotAreaRecord();
/* 41:   */     
/* 42:69 */     return rec;
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.PlotAreaRecord
 * JD-Core Version:    0.7.0.1
 */