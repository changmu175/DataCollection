/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class AxisLineFormatRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4129;
/*  13:    */   private short field_1_axisType;
/*  14:    */   public static final short AXIS_TYPE_AXIS_LINE = 0;
/*  15:    */   public static final short AXIS_TYPE_MAJOR_GRID_LINE = 1;
/*  16:    */   public static final short AXIS_TYPE_MINOR_GRID_LINE = 2;
/*  17:    */   public static final short AXIS_TYPE_WALLS_OR_FLOOR = 3;
/*  18:    */   
/*  19:    */   public AxisLineFormatRecord() {}
/*  20:    */   
/*  21:    */   public AxisLineFormatRecord(RecordInputStream in)
/*  22:    */   {
/*  23: 46 */     this.field_1_axisType = in.readShort();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String toString()
/*  27:    */   {
/*  28: 51 */     StringBuffer buffer = new StringBuffer();
/*  29:    */     
/*  30: 53 */     buffer.append("[AXISLINEFORMAT]\n");
/*  31: 54 */     buffer.append("    .axisType             = ").append("0x").append(HexDump.toHex(getAxisType())).append(" (").append(getAxisType()).append(" )");
/*  32:    */     
/*  33:    */ 
/*  34: 57 */     buffer.append(System.getProperty("line.separator"));
/*  35:    */     
/*  36: 59 */     buffer.append("[/AXISLINEFORMAT]\n");
/*  37: 60 */     return buffer.toString();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void serialize(LittleEndianOutput out)
/*  41:    */   {
/*  42: 64 */     out.writeShort(this.field_1_axisType);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected int getDataSize()
/*  46:    */   {
/*  47: 68 */     return 2;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getSid()
/*  51:    */   {
/*  52: 73 */     return 4129;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public AxisLineFormatRecord clone()
/*  56:    */   {
/*  57: 78 */     AxisLineFormatRecord rec = new AxisLineFormatRecord();
/*  58:    */     
/*  59: 80 */     rec.field_1_axisType = this.field_1_axisType;
/*  60: 81 */     return rec;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public short getAxisType()
/*  64:    */   {
/*  65: 98 */     return this.field_1_axisType;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setAxisType(short field_1_axisType)
/*  69:    */   {
/*  70:113 */     this.field_1_axisType = field_1_axisType;
/*  71:    */   }
/*  72:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AxisLineFormatRecord
 * JD-Core Version:    0.7.0.1
 */