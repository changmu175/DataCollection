/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class AxisRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4125;
/*  13:    */   private short field_1_axisType;
/*  14:    */   public static final short AXIS_TYPE_CATEGORY_OR_X_AXIS = 0;
/*  15:    */   public static final short AXIS_TYPE_VALUE_AXIS = 1;
/*  16:    */   public static final short AXIS_TYPE_SERIES_AXIS = 2;
/*  17:    */   private int field_2_reserved1;
/*  18:    */   private int field_3_reserved2;
/*  19:    */   private int field_4_reserved3;
/*  20:    */   private int field_5_reserved4;
/*  21:    */   
/*  22:    */   public AxisRecord() {}
/*  23:    */   
/*  24:    */   public AxisRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 49 */     this.field_1_axisType = in.readShort();
/*  27: 50 */     this.field_2_reserved1 = in.readInt();
/*  28: 51 */     this.field_3_reserved2 = in.readInt();
/*  29: 52 */     this.field_4_reserved3 = in.readInt();
/*  30: 53 */     this.field_5_reserved4 = in.readInt();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 58 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 60 */     buffer.append("[AXIS]\n");
/*  38: 61 */     buffer.append("    .axisType             = ").append("0x").append(HexDump.toHex(getAxisType())).append(" (").append(getAxisType()).append(" )");
/*  39:    */     
/*  40:    */ 
/*  41: 64 */     buffer.append(System.getProperty("line.separator"));
/*  42: 65 */     buffer.append("    .reserved1            = ").append("0x").append(HexDump.toHex(getReserved1())).append(" (").append(getReserved1()).append(" )");
/*  43:    */     
/*  44:    */ 
/*  45: 68 */     buffer.append(System.getProperty("line.separator"));
/*  46: 69 */     buffer.append("    .reserved2            = ").append("0x").append(HexDump.toHex(getReserved2())).append(" (").append(getReserved2()).append(" )");
/*  47:    */     
/*  48:    */ 
/*  49: 72 */     buffer.append(System.getProperty("line.separator"));
/*  50: 73 */     buffer.append("    .reserved3            = ").append("0x").append(HexDump.toHex(getReserved3())).append(" (").append(getReserved3()).append(" )");
/*  51:    */     
/*  52:    */ 
/*  53: 76 */     buffer.append(System.getProperty("line.separator"));
/*  54: 77 */     buffer.append("    .reserved4            = ").append("0x").append(HexDump.toHex(getReserved4())).append(" (").append(getReserved4()).append(" )");
/*  55:    */     
/*  56:    */ 
/*  57: 80 */     buffer.append(System.getProperty("line.separator"));
/*  58:    */     
/*  59: 82 */     buffer.append("[/AXIS]\n");
/*  60: 83 */     return buffer.toString();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void serialize(LittleEndianOutput out)
/*  64:    */   {
/*  65: 87 */     out.writeShort(this.field_1_axisType);
/*  66: 88 */     out.writeInt(this.field_2_reserved1);
/*  67: 89 */     out.writeInt(this.field_3_reserved2);
/*  68: 90 */     out.writeInt(this.field_4_reserved3);
/*  69: 91 */     out.writeInt(this.field_5_reserved4);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected int getDataSize()
/*  73:    */   {
/*  74: 95 */     return 18;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public short getSid()
/*  78:    */   {
/*  79:100 */     return 4125;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public AxisRecord clone()
/*  83:    */   {
/*  84:105 */     AxisRecord rec = new AxisRecord();
/*  85:    */     
/*  86:107 */     rec.field_1_axisType = this.field_1_axisType;
/*  87:108 */     rec.field_2_reserved1 = this.field_2_reserved1;
/*  88:109 */     rec.field_3_reserved2 = this.field_3_reserved2;
/*  89:110 */     rec.field_4_reserved3 = this.field_4_reserved3;
/*  90:111 */     rec.field_5_reserved4 = this.field_5_reserved4;
/*  91:112 */     return rec;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public short getAxisType()
/*  95:    */   {
/*  96:128 */     return this.field_1_axisType;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setAxisType(short field_1_axisType)
/* 100:    */   {
/* 101:142 */     this.field_1_axisType = field_1_axisType;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getReserved1()
/* 105:    */   {
/* 106:150 */     return this.field_2_reserved1;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setReserved1(int field_2_reserved1)
/* 110:    */   {
/* 111:158 */     this.field_2_reserved1 = field_2_reserved1;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int getReserved2()
/* 115:    */   {
/* 116:166 */     return this.field_3_reserved2;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setReserved2(int field_3_reserved2)
/* 120:    */   {
/* 121:174 */     this.field_3_reserved2 = field_3_reserved2;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public int getReserved3()
/* 125:    */   {
/* 126:182 */     return this.field_4_reserved3;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setReserved3(int field_4_reserved3)
/* 130:    */   {
/* 131:190 */     this.field_4_reserved3 = field_4_reserved3;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public int getReserved4()
/* 135:    */   {
/* 136:198 */     return this.field_5_reserved4;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setReserved4(int field_5_reserved4)
/* 140:    */   {
/* 141:206 */     this.field_5_reserved4 = field_5_reserved4;
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AxisRecord
 * JD-Core Version:    0.7.0.1
 */