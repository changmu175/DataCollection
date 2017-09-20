/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class UnitsRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 4097;
/* 12:   */   private short field_1_units;
/* 13:   */   
/* 14:   */   public UnitsRecord() {}
/* 15:   */   
/* 16:   */   public UnitsRecord(RecordInputStream in)
/* 17:   */   {
/* 18:40 */     this.field_1_units = in.readShort();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String toString()
/* 22:   */   {
/* 23:46 */     StringBuffer buffer = new StringBuffer();
/* 24:   */     
/* 25:48 */     buffer.append("[UNITS]\n");
/* 26:49 */     buffer.append("    .units                = ").append("0x").append(HexDump.toHex(getUnits())).append(" (").append(getUnits()).append(" )");
/* 27:   */     
/* 28:   */ 
/* 29:52 */     buffer.append(System.getProperty("line.separator"));
/* 30:   */     
/* 31:54 */     buffer.append("[/UNITS]\n");
/* 32:55 */     return buffer.toString();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void serialize(LittleEndianOutput out)
/* 36:   */   {
/* 37:59 */     out.writeShort(this.field_1_units);
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected int getDataSize()
/* 41:   */   {
/* 42:63 */     return 2;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public short getSid()
/* 46:   */   {
/* 47:68 */     return 4097;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public Object clone()
/* 51:   */   {
/* 52:72 */     UnitsRecord rec = new UnitsRecord();
/* 53:   */     
/* 54:74 */     rec.field_1_units = this.field_1_units;
/* 55:75 */     return rec;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public short getUnits()
/* 59:   */   {
/* 60:86 */     return this.field_1_units;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void setUnits(short field_1_units)
/* 64:   */   {
/* 65:94 */     this.field_1_units = field_1_units;
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.UnitsRecord
 * JD-Core Version:    0.7.0.1
 */