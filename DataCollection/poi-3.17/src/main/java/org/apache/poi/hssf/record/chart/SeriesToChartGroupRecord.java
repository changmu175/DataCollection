/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class SeriesToChartGroupRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 4165;
/* 12:   */   private short field_1_chartGroupIndex;
/* 13:   */   
/* 14:   */   public SeriesToChartGroupRecord() {}
/* 15:   */   
/* 16:   */   public SeriesToChartGroupRecord(RecordInputStream in)
/* 17:   */   {
/* 18:42 */     this.field_1_chartGroupIndex = in.readShort();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toString()
/* 22:   */   {
/* 23:47 */     StringBuffer buffer = new StringBuffer();
/* 24:   */     
/* 25:49 */     buffer.append("[SeriesToChartGroup]\n");
/* 26:50 */     buffer.append("    .chartGroupIndex      = ").append("0x").append(HexDump.toHex(getChartGroupIndex())).append(" (").append(getChartGroupIndex()).append(" )");
/* 27:   */     
/* 28:   */ 
/* 29:53 */     buffer.append(System.getProperty("line.separator"));
/* 30:   */     
/* 31:55 */     buffer.append("[/SeriesToChartGroup]\n");
/* 32:56 */     return buffer.toString();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void serialize(LittleEndianOutput out)
/* 36:   */   {
/* 37:60 */     out.writeShort(this.field_1_chartGroupIndex);
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected int getDataSize()
/* 41:   */   {
/* 42:64 */     return 2;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public short getSid()
/* 46:   */   {
/* 47:69 */     return 4165;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public Object clone()
/* 51:   */   {
/* 52:73 */     SeriesToChartGroupRecord rec = new SeriesToChartGroupRecord();
/* 53:   */     
/* 54:75 */     rec.field_1_chartGroupIndex = this.field_1_chartGroupIndex;
/* 55:76 */     return rec;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public short getChartGroupIndex()
/* 59:   */   {
/* 60:87 */     return this.field_1_chartGroupIndex;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void setChartGroupIndex(short field_1_chartGroupIndex)
/* 64:   */   {
/* 65:95 */     this.field_1_chartGroupIndex = field_1_chartGroupIndex;
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord
 * JD-Core Version:    0.7.0.1
 */