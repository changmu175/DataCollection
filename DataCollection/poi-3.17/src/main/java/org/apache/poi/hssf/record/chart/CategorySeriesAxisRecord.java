/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class CategorySeriesAxisRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4128;
/*  15: 35 */   private static final BitField valueAxisCrossing = BitFieldFactory.getInstance(1);
/*  16: 36 */   private static final BitField crossesFarRight = BitFieldFactory.getInstance(2);
/*  17: 37 */   private static final BitField reversed = BitFieldFactory.getInstance(4);
/*  18:    */   private short field_1_crossingPoint;
/*  19:    */   private short field_2_labelFrequency;
/*  20:    */   private short field_3_tickMarkFrequency;
/*  21:    */   private short field_4_options;
/*  22:    */   
/*  23:    */   public CategorySeriesAxisRecord() {}
/*  24:    */   
/*  25:    */   public CategorySeriesAxisRecord(RecordInputStream in)
/*  26:    */   {
/*  27: 52 */     this.field_1_crossingPoint = in.readShort();
/*  28: 53 */     this.field_2_labelFrequency = in.readShort();
/*  29: 54 */     this.field_3_tickMarkFrequency = in.readShort();
/*  30: 55 */     this.field_4_options = in.readShort();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String toString()
/*  34:    */   {
/*  35: 60 */     StringBuffer buffer = new StringBuffer();
/*  36:    */     
/*  37: 62 */     buffer.append("[CATSERRANGE]\n");
/*  38: 63 */     buffer.append("    .crossingPoint        = ").append("0x").append(HexDump.toHex(getCrossingPoint())).append(" (").append(getCrossingPoint()).append(" )");
/*  39:    */     
/*  40:    */ 
/*  41: 66 */     buffer.append(System.getProperty("line.separator"));
/*  42: 67 */     buffer.append("    .labelFrequency       = ").append("0x").append(HexDump.toHex(getLabelFrequency())).append(" (").append(getLabelFrequency()).append(" )");
/*  43:    */     
/*  44:    */ 
/*  45: 70 */     buffer.append(System.getProperty("line.separator"));
/*  46: 71 */     buffer.append("    .tickMarkFrequency    = ").append("0x").append(HexDump.toHex(getTickMarkFrequency())).append(" (").append(getTickMarkFrequency()).append(" )");
/*  47:    */     
/*  48:    */ 
/*  49: 74 */     buffer.append(System.getProperty("line.separator"));
/*  50: 75 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  51:    */     
/*  52:    */ 
/*  53: 78 */     buffer.append(System.getProperty("line.separator"));
/*  54: 79 */     buffer.append("         .valueAxisCrossing        = ").append(isValueAxisCrossing()).append('\n');
/*  55: 80 */     buffer.append("         .crossesFarRight          = ").append(isCrossesFarRight()).append('\n');
/*  56: 81 */     buffer.append("         .reversed                 = ").append(isReversed()).append('\n');
/*  57:    */     
/*  58: 83 */     buffer.append("[/CATSERRANGE]\n");
/*  59: 84 */     return buffer.toString();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void serialize(LittleEndianOutput out)
/*  63:    */   {
/*  64: 88 */     out.writeShort(this.field_1_crossingPoint);
/*  65: 89 */     out.writeShort(this.field_2_labelFrequency);
/*  66: 90 */     out.writeShort(this.field_3_tickMarkFrequency);
/*  67: 91 */     out.writeShort(this.field_4_options);
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected int getDataSize()
/*  71:    */   {
/*  72: 95 */     return 8;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public short getSid()
/*  76:    */   {
/*  77:100 */     return 4128;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public CategorySeriesAxisRecord clone()
/*  81:    */   {
/*  82:105 */     CategorySeriesAxisRecord rec = new CategorySeriesAxisRecord();
/*  83:    */     
/*  84:107 */     rec.field_1_crossingPoint = this.field_1_crossingPoint;
/*  85:108 */     rec.field_2_labelFrequency = this.field_2_labelFrequency;
/*  86:109 */     rec.field_3_tickMarkFrequency = this.field_3_tickMarkFrequency;
/*  87:110 */     rec.field_4_options = this.field_4_options;
/*  88:111 */     return rec;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public short getCrossingPoint()
/*  92:    */   {
/*  93:122 */     return this.field_1_crossingPoint;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setCrossingPoint(short field_1_crossingPoint)
/*  97:    */   {
/*  98:130 */     this.field_1_crossingPoint = field_1_crossingPoint;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public short getLabelFrequency()
/* 102:    */   {
/* 103:138 */     return this.field_2_labelFrequency;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setLabelFrequency(short field_2_labelFrequency)
/* 107:    */   {
/* 108:146 */     this.field_2_labelFrequency = field_2_labelFrequency;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public short getTickMarkFrequency()
/* 112:    */   {
/* 113:154 */     return this.field_3_tickMarkFrequency;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setTickMarkFrequency(short field_3_tickMarkFrequency)
/* 117:    */   {
/* 118:162 */     this.field_3_tickMarkFrequency = field_3_tickMarkFrequency;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public short getOptions()
/* 122:    */   {
/* 123:170 */     return this.field_4_options;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setOptions(short field_4_options)
/* 127:    */   {
/* 128:178 */     this.field_4_options = field_4_options;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setValueAxisCrossing(boolean value)
/* 132:    */   {
/* 133:187 */     this.field_4_options = valueAxisCrossing.setShortBoolean(this.field_4_options, value);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public boolean isValueAxisCrossing()
/* 137:    */   {
/* 138:196 */     return valueAxisCrossing.isSet(this.field_4_options);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setCrossesFarRight(boolean value)
/* 142:    */   {
/* 143:205 */     this.field_4_options = crossesFarRight.setShortBoolean(this.field_4_options, value);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isCrossesFarRight()
/* 147:    */   {
/* 148:214 */     return crossesFarRight.isSet(this.field_4_options);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setReversed(boolean value)
/* 152:    */   {
/* 153:223 */     this.field_4_options = reversed.setShortBoolean(this.field_4_options, value);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public boolean isReversed()
/* 157:    */   {
/* 158:232 */     return reversed.isSet(this.field_4_options);
/* 159:    */   }
/* 160:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.CategorySeriesAxisRecord
 * JD-Core Version:    0.7.0.1
 */