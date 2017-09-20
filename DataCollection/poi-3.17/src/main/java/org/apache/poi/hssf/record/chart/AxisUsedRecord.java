/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class AxisUsedRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 4166;
/* 13:   */   private short field_1_numAxis;
/* 14:   */   
/* 15:   */   public AxisUsedRecord() {}
/* 16:   */   
/* 17:   */   public AxisUsedRecord(RecordInputStream in)
/* 18:   */   {
/* 19:42 */     this.field_1_numAxis = in.readShort();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String toString()
/* 23:   */   {
/* 24:47 */     StringBuffer buffer = new StringBuffer();
/* 25:   */     
/* 26:49 */     buffer.append("[AXISUSED]\n");
/* 27:50 */     buffer.append("    .numAxis              = ").append("0x").append(HexDump.toHex(getNumAxis())).append(" (").append(getNumAxis()).append(" )");
/* 28:   */     
/* 29:   */ 
/* 30:53 */     buffer.append(System.getProperty("line.separator"));
/* 31:   */     
/* 32:55 */     buffer.append("[/AXISUSED]\n");
/* 33:56 */     return buffer.toString();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void serialize(LittleEndianOutput out)
/* 37:   */   {
/* 38:60 */     out.writeShort(this.field_1_numAxis);
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected int getDataSize()
/* 42:   */   {
/* 43:64 */     return 2;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public short getSid()
/* 47:   */   {
/* 48:69 */     return 4166;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public AxisUsedRecord clone()
/* 52:   */   {
/* 53:74 */     AxisUsedRecord rec = new AxisUsedRecord();
/* 54:   */     
/* 55:76 */     rec.field_1_numAxis = this.field_1_numAxis;
/* 56:77 */     return rec;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public short getNumAxis()
/* 60:   */   {
/* 61:88 */     return this.field_1_numAxis;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public void setNumAxis(short field_1_numAxis)
/* 65:   */   {
/* 66:96 */     this.field_1_numAxis = field_1_numAxis;
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AxisUsedRecord
 * JD-Core Version:    0.7.0.1
 */