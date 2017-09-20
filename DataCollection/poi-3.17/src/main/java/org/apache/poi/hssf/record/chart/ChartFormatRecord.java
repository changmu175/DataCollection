/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class ChartFormatRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 4116;
/*  14: 36 */   private static final BitField varyDisplayPattern = BitFieldFactory.getInstance(1);
/*  15:    */   private int field1_x_position;
/*  16:    */   private int field2_y_position;
/*  17:    */   private int field3_width;
/*  18:    */   private int field4_height;
/*  19:    */   private int field5_grbit;
/*  20:    */   private int field6_unknown;
/*  21:    */   
/*  22:    */   public ChartFormatRecord() {}
/*  23:    */   
/*  24:    */   public ChartFormatRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 51 */     this.field1_x_position = in.readInt();
/*  27: 52 */     this.field2_y_position = in.readInt();
/*  28: 53 */     this.field3_width = in.readInt();
/*  29: 54 */     this.field4_height = in.readInt();
/*  30: 55 */     this.field5_grbit = in.readUShort();
/*  31: 56 */     this.field6_unknown = in.readUShort();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String toString()
/*  35:    */   {
/*  36: 60 */     StringBuffer buffer = new StringBuffer();
/*  37:    */     
/*  38: 62 */     buffer.append("[CHARTFORMAT]\n");
/*  39: 63 */     buffer.append("    .xPosition       = ").append(getXPosition()).append("\n");
/*  40: 64 */     buffer.append("    .yPosition       = ").append(getYPosition()).append("\n");
/*  41: 65 */     buffer.append("    .width           = ").append(getWidth()).append("\n");
/*  42: 66 */     buffer.append("    .height          = ").append(getHeight()).append("\n");
/*  43: 67 */     buffer.append("    .grBit           = ").append(HexDump.intToHex(this.field5_grbit)).append("\n");
/*  44: 68 */     buffer.append("[/CHARTFORMAT]\n");
/*  45: 69 */     return buffer.toString();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void serialize(LittleEndianOutput out)
/*  49:    */   {
/*  50: 73 */     out.writeInt(getXPosition());
/*  51: 74 */     out.writeInt(getYPosition());
/*  52: 75 */     out.writeInt(getWidth());
/*  53: 76 */     out.writeInt(getHeight());
/*  54: 77 */     out.writeShort(this.field5_grbit);
/*  55: 78 */     out.writeShort(this.field6_unknown);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected int getDataSize()
/*  59:    */   {
/*  60: 82 */     return 20;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public short getSid()
/*  64:    */   {
/*  65: 86 */     return 4116;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int getXPosition()
/*  69:    */   {
/*  70: 90 */     return this.field1_x_position;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setXPosition(int xPosition)
/*  74:    */   {
/*  75: 94 */     this.field1_x_position = xPosition;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getYPosition()
/*  79:    */   {
/*  80: 98 */     return this.field2_y_position;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setYPosition(int yPosition)
/*  84:    */   {
/*  85:102 */     this.field2_y_position = yPosition;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public int getWidth()
/*  89:    */   {
/*  90:106 */     return this.field3_width;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setWidth(int width)
/*  94:    */   {
/*  95:110 */     this.field3_width = width;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int getHeight()
/*  99:    */   {
/* 100:114 */     return this.field4_height;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setHeight(int height)
/* 104:    */   {
/* 105:118 */     this.field4_height = height;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean getVaryDisplayPattern()
/* 109:    */   {
/* 110:122 */     return varyDisplayPattern.isSet(this.field5_grbit);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setVaryDisplayPattern(boolean value)
/* 114:    */   {
/* 115:126 */     this.field5_grbit = varyDisplayPattern.setBoolean(this.field5_grbit, value);
/* 116:    */   }
/* 117:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartFormatRecord
 * JD-Core Version:    0.7.0.1
 */