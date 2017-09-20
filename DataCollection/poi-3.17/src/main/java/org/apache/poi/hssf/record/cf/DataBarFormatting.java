/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.common.ExtendedColor;
/*   4:    */ import org.apache.poi.util.BitField;
/*   5:    */ import org.apache.poi.util.BitFieldFactory;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ 
/*  11:    */ public final class DataBarFormatting
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14: 32 */   private static POILogger log = POILogFactory.getLogger(DataBarFormatting.class);
/*  15: 34 */   private byte options = 0;
/*  16: 35 */   private byte percentMin = 0;
/*  17: 36 */   private byte percentMax = 0;
/*  18:    */   private ExtendedColor color;
/*  19:    */   private DataBarThreshold thresholdMin;
/*  20:    */   private DataBarThreshold thresholdMax;
/*  21: 41 */   private static BitField iconOnly = BitFieldFactory.getInstance(1);
/*  22: 42 */   private static BitField reversed = BitFieldFactory.getInstance(4);
/*  23:    */   
/*  24:    */   public DataBarFormatting()
/*  25:    */   {
/*  26: 45 */     this.options = 2;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public DataBarFormatting(LittleEndianInput in)
/*  30:    */   {
/*  31: 48 */     in.readShort();
/*  32: 49 */     in.readByte();
/*  33: 50 */     this.options = in.readByte();
/*  34:    */     
/*  35: 52 */     this.percentMin = in.readByte();
/*  36: 53 */     this.percentMax = in.readByte();
/*  37: 54 */     if ((this.percentMin < 0) || (this.percentMin > 100)) {
/*  38: 55 */       log.log(5, new Object[] { "Inconsistent Minimum Percentage found " + this.percentMin });
/*  39:    */     }
/*  40: 56 */     if ((this.percentMax < 0) || (this.percentMax > 100)) {
/*  41: 57 */       log.log(5, new Object[] { "Inconsistent Minimum Percentage found " + this.percentMin });
/*  42:    */     }
/*  43: 59 */     this.color = new ExtendedColor(in);
/*  44: 60 */     this.thresholdMin = new DataBarThreshold(in);
/*  45: 61 */     this.thresholdMax = new DataBarThreshold(in);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isIconOnly()
/*  49:    */   {
/*  50: 65 */     return getOptionFlag(iconOnly);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setIconOnly(boolean only)
/*  54:    */   {
/*  55: 68 */     setOptionFlag(only, iconOnly);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean isReversed()
/*  59:    */   {
/*  60: 72 */     return getOptionFlag(reversed);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setReversed(boolean rev)
/*  64:    */   {
/*  65: 75 */     setOptionFlag(rev, reversed);
/*  66:    */   }
/*  67:    */   
/*  68:    */   private boolean getOptionFlag(BitField field)
/*  69:    */   {
/*  70: 79 */     int value = field.getValue(this.options);
/*  71: 80 */     return value != 0;
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void setOptionFlag(boolean option, BitField field)
/*  75:    */   {
/*  76: 83 */     this.options = field.setByteBoolean(this.options, option);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public byte getPercentMin()
/*  80:    */   {
/*  81: 87 */     return this.percentMin;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setPercentMin(byte percentMin)
/*  85:    */   {
/*  86: 90 */     this.percentMin = percentMin;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public byte getPercentMax()
/*  90:    */   {
/*  91: 94 */     return this.percentMax;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setPercentMax(byte percentMax)
/*  95:    */   {
/*  96: 97 */     this.percentMax = percentMax;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ExtendedColor getColor()
/* 100:    */   {
/* 101:101 */     return this.color;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setColor(ExtendedColor color)
/* 105:    */   {
/* 106:104 */     this.color = color;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public DataBarThreshold getThresholdMin()
/* 110:    */   {
/* 111:108 */     return this.thresholdMin;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setThresholdMin(DataBarThreshold thresholdMin)
/* 115:    */   {
/* 116:111 */     this.thresholdMin = thresholdMin;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public DataBarThreshold getThresholdMax()
/* 120:    */   {
/* 121:115 */     return this.thresholdMax;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setThresholdMax(DataBarThreshold thresholdMax)
/* 125:    */   {
/* 126:118 */     this.thresholdMax = thresholdMax;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String toString()
/* 130:    */   {
/* 131:122 */     StringBuffer buffer = new StringBuffer();
/* 132:123 */     buffer.append("    [Data Bar Formatting]\n");
/* 133:124 */     buffer.append("          .icon_only= ").append(isIconOnly()).append("\n");
/* 134:125 */     buffer.append("          .reversed = ").append(isReversed()).append("\n");
/* 135:126 */     buffer.append(this.color);
/* 136:127 */     buffer.append(this.thresholdMin);
/* 137:128 */     buffer.append(this.thresholdMax);
/* 138:129 */     buffer.append("    [/Data Bar Formatting]\n");
/* 139:130 */     return buffer.toString();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public Object clone()
/* 143:    */   {
/* 144:134 */     DataBarFormatting rec = new DataBarFormatting();
/* 145:135 */     rec.options = this.options;
/* 146:136 */     rec.percentMin = this.percentMin;
/* 147:137 */     rec.percentMax = this.percentMax;
/* 148:138 */     rec.color = this.color.clone();
/* 149:139 */     rec.thresholdMin = this.thresholdMin.clone();
/* 150:140 */     rec.thresholdMax = this.thresholdMax.clone();
/* 151:141 */     return rec;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public int getDataLength()
/* 155:    */   {
/* 156:145 */     return 6 + this.color.getDataLength() + this.thresholdMin.getDataLength() + this.thresholdMax.getDataLength();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void serialize(LittleEndianOutput out)
/* 160:    */   {
/* 161:151 */     out.writeShort(0);
/* 162:152 */     out.writeByte(0);
/* 163:153 */     out.writeByte(this.options);
/* 164:154 */     out.writeByte(this.percentMin);
/* 165:155 */     out.writeByte(this.percentMax);
/* 166:156 */     this.color.serialize(out);
/* 167:157 */     this.thresholdMin.serialize(out);
/* 168:158 */     this.thresholdMax.serialize(out);
/* 169:    */   }
/* 170:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.DataBarFormatting
 * JD-Core Version:    0.7.0.1
 */