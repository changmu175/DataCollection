/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class AxisParentRecord
/*   9:    */   extends StandardRecord
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short sid = 4161;
/*  13:    */   private short field_1_axisType;
/*  14:    */   public static final short AXIS_TYPE_MAIN = 0;
/*  15:    */   public static final short AXIS_TYPE_SECONDARY = 1;
/*  16:    */   private int field_2_x;
/*  17:    */   private int field_3_y;
/*  18:    */   private int field_4_width;
/*  19:    */   private int field_5_height;
/*  20:    */   
/*  21:    */   public AxisParentRecord() {}
/*  22:    */   
/*  23:    */   public AxisParentRecord(RecordInputStream in)
/*  24:    */   {
/*  25: 48 */     this.field_1_axisType = in.readShort();
/*  26: 49 */     this.field_2_x = in.readInt();
/*  27: 50 */     this.field_3_y = in.readInt();
/*  28: 51 */     this.field_4_width = in.readInt();
/*  29: 52 */     this.field_5_height = in.readInt();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 57 */     StringBuffer buffer = new StringBuffer();
/*  35:    */     
/*  36: 59 */     buffer.append("[AXISPARENT]\n");
/*  37: 60 */     buffer.append("    .axisType             = ").append("0x").append(HexDump.toHex(getAxisType())).append(" (").append(getAxisType()).append(" )");
/*  38:    */     
/*  39:    */ 
/*  40: 63 */     buffer.append(System.getProperty("line.separator"));
/*  41: 64 */     buffer.append("    .x                    = ").append("0x").append(HexDump.toHex(getX())).append(" (").append(getX()).append(" )");
/*  42:    */     
/*  43:    */ 
/*  44: 67 */     buffer.append(System.getProperty("line.separator"));
/*  45: 68 */     buffer.append("    .y                    = ").append("0x").append(HexDump.toHex(getY())).append(" (").append(getY()).append(" )");
/*  46:    */     
/*  47:    */ 
/*  48: 71 */     buffer.append(System.getProperty("line.separator"));
/*  49: 72 */     buffer.append("    .width                = ").append("0x").append(HexDump.toHex(getWidth())).append(" (").append(getWidth()).append(" )");
/*  50:    */     
/*  51:    */ 
/*  52: 75 */     buffer.append(System.getProperty("line.separator"));
/*  53: 76 */     buffer.append("    .height               = ").append("0x").append(HexDump.toHex(getHeight())).append(" (").append(getHeight()).append(" )");
/*  54:    */     
/*  55:    */ 
/*  56: 79 */     buffer.append(System.getProperty("line.separator"));
/*  57:    */     
/*  58: 81 */     buffer.append("[/AXISPARENT]\n");
/*  59: 82 */     return buffer.toString();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void serialize(LittleEndianOutput out)
/*  63:    */   {
/*  64: 86 */     out.writeShort(this.field_1_axisType);
/*  65: 87 */     out.writeInt(this.field_2_x);
/*  66: 88 */     out.writeInt(this.field_3_y);
/*  67: 89 */     out.writeInt(this.field_4_width);
/*  68: 90 */     out.writeInt(this.field_5_height);
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected int getDataSize()
/*  72:    */   {
/*  73: 94 */     return 18;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public short getSid()
/*  77:    */   {
/*  78: 99 */     return 4161;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public AxisParentRecord clone()
/*  82:    */   {
/*  83:104 */     AxisParentRecord rec = new AxisParentRecord();
/*  84:    */     
/*  85:106 */     rec.field_1_axisType = this.field_1_axisType;
/*  86:107 */     rec.field_2_x = this.field_2_x;
/*  87:108 */     rec.field_3_y = this.field_3_y;
/*  88:109 */     rec.field_4_width = this.field_4_width;
/*  89:110 */     rec.field_5_height = this.field_5_height;
/*  90:111 */     return rec;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public short getAxisType()
/*  94:    */   {
/*  95:126 */     return this.field_1_axisType;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setAxisType(short field_1_axisType)
/*  99:    */   {
/* 100:139 */     this.field_1_axisType = field_1_axisType;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int getX()
/* 104:    */   {
/* 105:147 */     return this.field_2_x;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setX(int field_2_x)
/* 109:    */   {
/* 110:155 */     this.field_2_x = field_2_x;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getY()
/* 114:    */   {
/* 115:163 */     return this.field_3_y;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setY(int field_3_y)
/* 119:    */   {
/* 120:171 */     this.field_3_y = field_3_y;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int getWidth()
/* 124:    */   {
/* 125:179 */     return this.field_4_width;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setWidth(int field_4_width)
/* 129:    */   {
/* 130:187 */     this.field_4_width = field_4_width;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getHeight()
/* 134:    */   {
/* 135:195 */     return this.field_5_height;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setHeight(int field_5_height)
/* 139:    */   {
/* 140:203 */     this.field_5_height = field_5_height;
/* 141:    */   }
/* 142:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AxisParentRecord
 * JD-Core Version:    0.7.0.1
 */