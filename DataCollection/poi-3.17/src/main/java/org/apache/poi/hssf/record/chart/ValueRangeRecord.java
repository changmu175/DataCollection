/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class ValueRangeRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 4127;
/*  14: 33 */   private static final BitField automaticMinimum = BitFieldFactory.getInstance(1);
/*  15: 34 */   private static final BitField automaticMaximum = BitFieldFactory.getInstance(2);
/*  16: 35 */   private static final BitField automaticMajor = BitFieldFactory.getInstance(4);
/*  17: 36 */   private static final BitField automaticMinor = BitFieldFactory.getInstance(8);
/*  18: 37 */   private static final BitField automaticCategoryCrossing = BitFieldFactory.getInstance(16);
/*  19: 38 */   private static final BitField logarithmicScale = BitFieldFactory.getInstance(32);
/*  20: 39 */   private static final BitField valuesInReverse = BitFieldFactory.getInstance(64);
/*  21: 40 */   private static final BitField crossCategoryAxisAtMaximum = BitFieldFactory.getInstance(128);
/*  22: 41 */   private static final BitField reserved = BitFieldFactory.getInstance(256);
/*  23:    */   private double field_1_minimumAxisValue;
/*  24:    */   private double field_2_maximumAxisValue;
/*  25:    */   private double field_3_majorIncrement;
/*  26:    */   private double field_4_minorIncrement;
/*  27:    */   private double field_5_categoryAxisCross;
/*  28:    */   private short field_6_options;
/*  29:    */   
/*  30:    */   public ValueRangeRecord() {}
/*  31:    */   
/*  32:    */   public ValueRangeRecord(RecordInputStream in)
/*  33:    */   {
/*  34: 58 */     this.field_1_minimumAxisValue = in.readDouble();
/*  35: 59 */     this.field_2_maximumAxisValue = in.readDouble();
/*  36: 60 */     this.field_3_majorIncrement = in.readDouble();
/*  37: 61 */     this.field_4_minorIncrement = in.readDouble();
/*  38: 62 */     this.field_5_categoryAxisCross = in.readDouble();
/*  39: 63 */     this.field_6_options = in.readShort();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String toString()
/*  43:    */   {
/*  44: 69 */     StringBuffer buffer = new StringBuffer();
/*  45:    */     
/*  46: 71 */     buffer.append("[VALUERANGE]\n");
/*  47: 72 */     buffer.append("    .minimumAxisValue     = ").append(" (").append(getMinimumAxisValue()).append(" )");
/*  48:    */     
/*  49: 74 */     buffer.append(System.getProperty("line.separator"));
/*  50: 75 */     buffer.append("    .maximumAxisValue     = ").append(" (").append(getMaximumAxisValue()).append(" )");
/*  51:    */     
/*  52: 77 */     buffer.append(System.getProperty("line.separator"));
/*  53: 78 */     buffer.append("    .majorIncrement       = ").append(" (").append(getMajorIncrement()).append(" )");
/*  54:    */     
/*  55: 80 */     buffer.append(System.getProperty("line.separator"));
/*  56: 81 */     buffer.append("    .minorIncrement       = ").append(" (").append(getMinorIncrement()).append(" )");
/*  57:    */     
/*  58: 83 */     buffer.append(System.getProperty("line.separator"));
/*  59: 84 */     buffer.append("    .categoryAxisCross    = ").append(" (").append(getCategoryAxisCross()).append(" )");
/*  60:    */     
/*  61: 86 */     buffer.append(System.getProperty("line.separator"));
/*  62: 87 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  63:    */     
/*  64:    */ 
/*  65: 90 */     buffer.append(System.getProperty("line.separator"));
/*  66: 91 */     buffer.append("         .automaticMinimum         = ").append(isAutomaticMinimum()).append('\n');
/*  67: 92 */     buffer.append("         .automaticMaximum         = ").append(isAutomaticMaximum()).append('\n');
/*  68: 93 */     buffer.append("         .automaticMajor           = ").append(isAutomaticMajor()).append('\n');
/*  69: 94 */     buffer.append("         .automaticMinor           = ").append(isAutomaticMinor()).append('\n');
/*  70: 95 */     buffer.append("         .automaticCategoryCrossing     = ").append(isAutomaticCategoryCrossing()).append('\n');
/*  71: 96 */     buffer.append("         .logarithmicScale         = ").append(isLogarithmicScale()).append('\n');
/*  72: 97 */     buffer.append("         .valuesInReverse          = ").append(isValuesInReverse()).append('\n');
/*  73: 98 */     buffer.append("         .crossCategoryAxisAtMaximum     = ").append(isCrossCategoryAxisAtMaximum()).append('\n');
/*  74: 99 */     buffer.append("         .reserved                 = ").append(isReserved()).append('\n');
/*  75:    */     
/*  76:101 */     buffer.append("[/VALUERANGE]\n");
/*  77:102 */     return buffer.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void serialize(LittleEndianOutput out)
/*  81:    */   {
/*  82:106 */     out.writeDouble(this.field_1_minimumAxisValue);
/*  83:107 */     out.writeDouble(this.field_2_maximumAxisValue);
/*  84:108 */     out.writeDouble(this.field_3_majorIncrement);
/*  85:109 */     out.writeDouble(this.field_4_minorIncrement);
/*  86:110 */     out.writeDouble(this.field_5_categoryAxisCross);
/*  87:111 */     out.writeShort(this.field_6_options);
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected int getDataSize()
/*  91:    */   {
/*  92:115 */     return 42;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public short getSid()
/*  96:    */   {
/*  97:120 */     return 4127;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Object clone()
/* 101:    */   {
/* 102:124 */     ValueRangeRecord rec = new ValueRangeRecord();
/* 103:    */     
/* 104:126 */     rec.field_1_minimumAxisValue = this.field_1_minimumAxisValue;
/* 105:127 */     rec.field_2_maximumAxisValue = this.field_2_maximumAxisValue;
/* 106:128 */     rec.field_3_majorIncrement = this.field_3_majorIncrement;
/* 107:129 */     rec.field_4_minorIncrement = this.field_4_minorIncrement;
/* 108:130 */     rec.field_5_categoryAxisCross = this.field_5_categoryAxisCross;
/* 109:131 */     rec.field_6_options = this.field_6_options;
/* 110:132 */     return rec;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public double getMinimumAxisValue()
/* 114:    */   {
/* 115:143 */     return this.field_1_minimumAxisValue;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setMinimumAxisValue(double field_1_minimumAxisValue)
/* 119:    */   {
/* 120:151 */     this.field_1_minimumAxisValue = field_1_minimumAxisValue;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public double getMaximumAxisValue()
/* 124:    */   {
/* 125:159 */     return this.field_2_maximumAxisValue;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setMaximumAxisValue(double field_2_maximumAxisValue)
/* 129:    */   {
/* 130:167 */     this.field_2_maximumAxisValue = field_2_maximumAxisValue;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public double getMajorIncrement()
/* 134:    */   {
/* 135:175 */     return this.field_3_majorIncrement;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setMajorIncrement(double field_3_majorIncrement)
/* 139:    */   {
/* 140:183 */     this.field_3_majorIncrement = field_3_majorIncrement;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public double getMinorIncrement()
/* 144:    */   {
/* 145:191 */     return this.field_4_minorIncrement;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setMinorIncrement(double field_4_minorIncrement)
/* 149:    */   {
/* 150:199 */     this.field_4_minorIncrement = field_4_minorIncrement;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public double getCategoryAxisCross()
/* 154:    */   {
/* 155:207 */     return this.field_5_categoryAxisCross;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setCategoryAxisCross(double field_5_categoryAxisCross)
/* 159:    */   {
/* 160:215 */     this.field_5_categoryAxisCross = field_5_categoryAxisCross;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public short getOptions()
/* 164:    */   {
/* 165:223 */     return this.field_6_options;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setOptions(short field_6_options)
/* 169:    */   {
/* 170:231 */     this.field_6_options = field_6_options;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setAutomaticMinimum(boolean value)
/* 174:    */   {
/* 175:240 */     this.field_6_options = automaticMinimum.setShortBoolean(this.field_6_options, value);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean isAutomaticMinimum()
/* 179:    */   {
/* 180:249 */     return automaticMinimum.isSet(this.field_6_options);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setAutomaticMaximum(boolean value)
/* 184:    */   {
/* 185:258 */     this.field_6_options = automaticMaximum.setShortBoolean(this.field_6_options, value);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean isAutomaticMaximum()
/* 189:    */   {
/* 190:267 */     return automaticMaximum.isSet(this.field_6_options);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setAutomaticMajor(boolean value)
/* 194:    */   {
/* 195:276 */     this.field_6_options = automaticMajor.setShortBoolean(this.field_6_options, value);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean isAutomaticMajor()
/* 199:    */   {
/* 200:285 */     return automaticMajor.isSet(this.field_6_options);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void setAutomaticMinor(boolean value)
/* 204:    */   {
/* 205:294 */     this.field_6_options = automaticMinor.setShortBoolean(this.field_6_options, value);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public boolean isAutomaticMinor()
/* 209:    */   {
/* 210:303 */     return automaticMinor.isSet(this.field_6_options);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setAutomaticCategoryCrossing(boolean value)
/* 214:    */   {
/* 215:312 */     this.field_6_options = automaticCategoryCrossing.setShortBoolean(this.field_6_options, value);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public boolean isAutomaticCategoryCrossing()
/* 219:    */   {
/* 220:321 */     return automaticCategoryCrossing.isSet(this.field_6_options);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public void setLogarithmicScale(boolean value)
/* 224:    */   {
/* 225:330 */     this.field_6_options = logarithmicScale.setShortBoolean(this.field_6_options, value);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public boolean isLogarithmicScale()
/* 229:    */   {
/* 230:339 */     return logarithmicScale.isSet(this.field_6_options);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setValuesInReverse(boolean value)
/* 234:    */   {
/* 235:348 */     this.field_6_options = valuesInReverse.setShortBoolean(this.field_6_options, value);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public boolean isValuesInReverse()
/* 239:    */   {
/* 240:357 */     return valuesInReverse.isSet(this.field_6_options);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setCrossCategoryAxisAtMaximum(boolean value)
/* 244:    */   {
/* 245:366 */     this.field_6_options = crossCategoryAxisAtMaximum.setShortBoolean(this.field_6_options, value);
/* 246:    */   }
/* 247:    */   
/* 248:    */   public boolean isCrossCategoryAxisAtMaximum()
/* 249:    */   {
/* 250:375 */     return crossCategoryAxisAtMaximum.isSet(this.field_6_options);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public void setReserved(boolean value)
/* 254:    */   {
/* 255:384 */     this.field_6_options = reserved.setShortBoolean(this.field_6_options, value);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public boolean isReserved()
/* 259:    */   {
/* 260:393 */     return reserved.isSet(this.field_6_options);
/* 261:    */   }
/* 262:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ValueRangeRecord
 * JD-Core Version:    0.7.0.1
 */