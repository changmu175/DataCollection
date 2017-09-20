/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class AxisOptionsRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4194;
/*  15: 35 */   private static final BitField defaultMinimum = BitFieldFactory.getInstance(1);
/*  16: 36 */   private static final BitField defaultMaximum = BitFieldFactory.getInstance(2);
/*  17: 37 */   private static final BitField defaultMajor = BitFieldFactory.getInstance(4);
/*  18: 38 */   private static final BitField defaultMinorUnit = BitFieldFactory.getInstance(8);
/*  19: 39 */   private static final BitField isDate = BitFieldFactory.getInstance(16);
/*  20: 40 */   private static final BitField defaultBase = BitFieldFactory.getInstance(32);
/*  21: 41 */   private static final BitField defaultCross = BitFieldFactory.getInstance(64);
/*  22: 42 */   private static final BitField defaultDateSettings = BitFieldFactory.getInstance(128);
/*  23:    */   private short field_1_minimumCategory;
/*  24:    */   private short field_2_maximumCategory;
/*  25:    */   private short field_3_majorUnitValue;
/*  26:    */   private short field_4_majorUnit;
/*  27:    */   private short field_5_minorUnitValue;
/*  28:    */   private short field_6_minorUnit;
/*  29:    */   private short field_7_baseUnit;
/*  30:    */   private short field_8_crossingPoint;
/*  31:    */   private short field_9_options;
/*  32:    */   
/*  33:    */   public AxisOptionsRecord() {}
/*  34:    */   
/*  35:    */   public AxisOptionsRecord(RecordInputStream in)
/*  36:    */   {
/*  37: 62 */     this.field_1_minimumCategory = in.readShort();
/*  38: 63 */     this.field_2_maximumCategory = in.readShort();
/*  39: 64 */     this.field_3_majorUnitValue = in.readShort();
/*  40: 65 */     this.field_4_majorUnit = in.readShort();
/*  41: 66 */     this.field_5_minorUnitValue = in.readShort();
/*  42: 67 */     this.field_6_minorUnit = in.readShort();
/*  43: 68 */     this.field_7_baseUnit = in.readShort();
/*  44: 69 */     this.field_8_crossingPoint = in.readShort();
/*  45: 70 */     this.field_9_options = in.readShort();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String toString()
/*  49:    */   {
/*  50: 75 */     StringBuffer buffer = new StringBuffer();
/*  51:    */     
/*  52: 77 */     buffer.append("[AXCEXT]\n");
/*  53: 78 */     buffer.append("    .minimumCategory      = ").append("0x").append(HexDump.toHex(getMinimumCategory())).append(" (").append(getMinimumCategory()).append(" )");
/*  54:    */     
/*  55:    */ 
/*  56: 81 */     buffer.append(System.getProperty("line.separator"));
/*  57: 82 */     buffer.append("    .maximumCategory      = ").append("0x").append(HexDump.toHex(getMaximumCategory())).append(" (").append(getMaximumCategory()).append(" )");
/*  58:    */     
/*  59:    */ 
/*  60: 85 */     buffer.append(System.getProperty("line.separator"));
/*  61: 86 */     buffer.append("    .majorUnitValue       = ").append("0x").append(HexDump.toHex(getMajorUnitValue())).append(" (").append(getMajorUnitValue()).append(" )");
/*  62:    */     
/*  63:    */ 
/*  64: 89 */     buffer.append(System.getProperty("line.separator"));
/*  65: 90 */     buffer.append("    .majorUnit            = ").append("0x").append(HexDump.toHex(getMajorUnit())).append(" (").append(getMajorUnit()).append(" )");
/*  66:    */     
/*  67:    */ 
/*  68: 93 */     buffer.append(System.getProperty("line.separator"));
/*  69: 94 */     buffer.append("    .minorUnitValue       = ").append("0x").append(HexDump.toHex(getMinorUnitValue())).append(" (").append(getMinorUnitValue()).append(" )");
/*  70:    */     
/*  71:    */ 
/*  72: 97 */     buffer.append(System.getProperty("line.separator"));
/*  73: 98 */     buffer.append("    .minorUnit            = ").append("0x").append(HexDump.toHex(getMinorUnit())).append(" (").append(getMinorUnit()).append(" )");
/*  74:    */     
/*  75:    */ 
/*  76:101 */     buffer.append(System.getProperty("line.separator"));
/*  77:102 */     buffer.append("    .baseUnit             = ").append("0x").append(HexDump.toHex(getBaseUnit())).append(" (").append(getBaseUnit()).append(" )");
/*  78:    */     
/*  79:    */ 
/*  80:105 */     buffer.append(System.getProperty("line.separator"));
/*  81:106 */     buffer.append("    .crossingPoint        = ").append("0x").append(HexDump.toHex(getCrossingPoint())).append(" (").append(getCrossingPoint()).append(" )");
/*  82:    */     
/*  83:    */ 
/*  84:109 */     buffer.append(System.getProperty("line.separator"));
/*  85:110 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  86:    */     
/*  87:    */ 
/*  88:113 */     buffer.append(System.getProperty("line.separator"));
/*  89:114 */     buffer.append("         .defaultMinimum           = ").append(isDefaultMinimum()).append('\n');
/*  90:115 */     buffer.append("         .defaultMaximum           = ").append(isDefaultMaximum()).append('\n');
/*  91:116 */     buffer.append("         .defaultMajor             = ").append(isDefaultMajor()).append('\n');
/*  92:117 */     buffer.append("         .defaultMinorUnit         = ").append(isDefaultMinorUnit()).append('\n');
/*  93:118 */     buffer.append("         .isDate                   = ").append(isIsDate()).append('\n');
/*  94:119 */     buffer.append("         .defaultBase              = ").append(isDefaultBase()).append('\n');
/*  95:120 */     buffer.append("         .defaultCross             = ").append(isDefaultCross()).append('\n');
/*  96:121 */     buffer.append("         .defaultDateSettings      = ").append(isDefaultDateSettings()).append('\n');
/*  97:    */     
/*  98:123 */     buffer.append("[/AXCEXT]\n");
/*  99:124 */     return buffer.toString();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void serialize(LittleEndianOutput out)
/* 103:    */   {
/* 104:128 */     out.writeShort(this.field_1_minimumCategory);
/* 105:129 */     out.writeShort(this.field_2_maximumCategory);
/* 106:130 */     out.writeShort(this.field_3_majorUnitValue);
/* 107:131 */     out.writeShort(this.field_4_majorUnit);
/* 108:132 */     out.writeShort(this.field_5_minorUnitValue);
/* 109:133 */     out.writeShort(this.field_6_minorUnit);
/* 110:134 */     out.writeShort(this.field_7_baseUnit);
/* 111:135 */     out.writeShort(this.field_8_crossingPoint);
/* 112:136 */     out.writeShort(this.field_9_options);
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected int getDataSize()
/* 116:    */   {
/* 117:140 */     return 18;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public short getSid()
/* 121:    */   {
/* 122:145 */     return 4194;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public AxisOptionsRecord clone()
/* 126:    */   {
/* 127:150 */     AxisOptionsRecord rec = new AxisOptionsRecord();
/* 128:    */     
/* 129:152 */     rec.field_1_minimumCategory = this.field_1_minimumCategory;
/* 130:153 */     rec.field_2_maximumCategory = this.field_2_maximumCategory;
/* 131:154 */     rec.field_3_majorUnitValue = this.field_3_majorUnitValue;
/* 132:155 */     rec.field_4_majorUnit = this.field_4_majorUnit;
/* 133:156 */     rec.field_5_minorUnitValue = this.field_5_minorUnitValue;
/* 134:157 */     rec.field_6_minorUnit = this.field_6_minorUnit;
/* 135:158 */     rec.field_7_baseUnit = this.field_7_baseUnit;
/* 136:159 */     rec.field_8_crossingPoint = this.field_8_crossingPoint;
/* 137:160 */     rec.field_9_options = this.field_9_options;
/* 138:161 */     return rec;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public short getMinimumCategory()
/* 142:    */   {
/* 143:172 */     return this.field_1_minimumCategory;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setMinimumCategory(short field_1_minimumCategory)
/* 147:    */   {
/* 148:180 */     this.field_1_minimumCategory = field_1_minimumCategory;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public short getMaximumCategory()
/* 152:    */   {
/* 153:188 */     return this.field_2_maximumCategory;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void setMaximumCategory(short field_2_maximumCategory)
/* 157:    */   {
/* 158:196 */     this.field_2_maximumCategory = field_2_maximumCategory;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public short getMajorUnitValue()
/* 162:    */   {
/* 163:204 */     return this.field_3_majorUnitValue;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setMajorUnitValue(short field_3_majorUnitValue)
/* 167:    */   {
/* 168:212 */     this.field_3_majorUnitValue = field_3_majorUnitValue;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public short getMajorUnit()
/* 172:    */   {
/* 173:220 */     return this.field_4_majorUnit;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void setMajorUnit(short field_4_majorUnit)
/* 177:    */   {
/* 178:228 */     this.field_4_majorUnit = field_4_majorUnit;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public short getMinorUnitValue()
/* 182:    */   {
/* 183:236 */     return this.field_5_minorUnitValue;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void setMinorUnitValue(short field_5_minorUnitValue)
/* 187:    */   {
/* 188:244 */     this.field_5_minorUnitValue = field_5_minorUnitValue;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public short getMinorUnit()
/* 192:    */   {
/* 193:252 */     return this.field_6_minorUnit;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void setMinorUnit(short field_6_minorUnit)
/* 197:    */   {
/* 198:260 */     this.field_6_minorUnit = field_6_minorUnit;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public short getBaseUnit()
/* 202:    */   {
/* 203:268 */     return this.field_7_baseUnit;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setBaseUnit(short field_7_baseUnit)
/* 207:    */   {
/* 208:276 */     this.field_7_baseUnit = field_7_baseUnit;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public short getCrossingPoint()
/* 212:    */   {
/* 213:284 */     return this.field_8_crossingPoint;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setCrossingPoint(short field_8_crossingPoint)
/* 217:    */   {
/* 218:292 */     this.field_8_crossingPoint = field_8_crossingPoint;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public short getOptions()
/* 222:    */   {
/* 223:300 */     return this.field_9_options;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setOptions(short field_9_options)
/* 227:    */   {
/* 228:308 */     this.field_9_options = field_9_options;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void setDefaultMinimum(boolean value)
/* 232:    */   {
/* 233:317 */     this.field_9_options = defaultMinimum.setShortBoolean(this.field_9_options, value);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public boolean isDefaultMinimum()
/* 237:    */   {
/* 238:326 */     return defaultMinimum.isSet(this.field_9_options);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void setDefaultMaximum(boolean value)
/* 242:    */   {
/* 243:335 */     this.field_9_options = defaultMaximum.setShortBoolean(this.field_9_options, value);
/* 244:    */   }
/* 245:    */   
/* 246:    */   public boolean isDefaultMaximum()
/* 247:    */   {
/* 248:344 */     return defaultMaximum.isSet(this.field_9_options);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void setDefaultMajor(boolean value)
/* 252:    */   {
/* 253:353 */     this.field_9_options = defaultMajor.setShortBoolean(this.field_9_options, value);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public boolean isDefaultMajor()
/* 257:    */   {
/* 258:362 */     return defaultMajor.isSet(this.field_9_options);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public void setDefaultMinorUnit(boolean value)
/* 262:    */   {
/* 263:371 */     this.field_9_options = defaultMinorUnit.setShortBoolean(this.field_9_options, value);
/* 264:    */   }
/* 265:    */   
/* 266:    */   public boolean isDefaultMinorUnit()
/* 267:    */   {
/* 268:380 */     return defaultMinorUnit.isSet(this.field_9_options);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void setIsDate(boolean value)
/* 272:    */   {
/* 273:389 */     this.field_9_options = isDate.setShortBoolean(this.field_9_options, value);
/* 274:    */   }
/* 275:    */   
/* 276:    */   public boolean isIsDate()
/* 277:    */   {
/* 278:398 */     return isDate.isSet(this.field_9_options);
/* 279:    */   }
/* 280:    */   
/* 281:    */   public void setDefaultBase(boolean value)
/* 282:    */   {
/* 283:407 */     this.field_9_options = defaultBase.setShortBoolean(this.field_9_options, value);
/* 284:    */   }
/* 285:    */   
/* 286:    */   public boolean isDefaultBase()
/* 287:    */   {
/* 288:416 */     return defaultBase.isSet(this.field_9_options);
/* 289:    */   }
/* 290:    */   
/* 291:    */   public void setDefaultCross(boolean value)
/* 292:    */   {
/* 293:425 */     this.field_9_options = defaultCross.setShortBoolean(this.field_9_options, value);
/* 294:    */   }
/* 295:    */   
/* 296:    */   public boolean isDefaultCross()
/* 297:    */   {
/* 298:434 */     return defaultCross.isSet(this.field_9_options);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public void setDefaultDateSettings(boolean value)
/* 302:    */   {
/* 303:443 */     this.field_9_options = defaultDateSettings.setShortBoolean(this.field_9_options, value);
/* 304:    */   }
/* 305:    */   
/* 306:    */   public boolean isDefaultDateSettings()
/* 307:    */   {
/* 308:452 */     return defaultDateSettings.isSet(this.field_9_options);
/* 309:    */   }
/* 310:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AxisOptionsRecord
 * JD-Core Version:    0.7.0.1
 */