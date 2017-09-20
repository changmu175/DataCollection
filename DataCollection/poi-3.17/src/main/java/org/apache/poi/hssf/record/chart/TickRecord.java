/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class TickRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 4126;
/*  14: 35 */   private static final BitField autoTextColor = BitFieldFactory.getInstance(1);
/*  15: 36 */   private static final BitField autoTextBackground = BitFieldFactory.getInstance(2);
/*  16: 37 */   private static final BitField rotation = BitFieldFactory.getInstance(28);
/*  17: 38 */   private static final BitField autorotate = BitFieldFactory.getInstance(32);
/*  18:    */   private byte field_1_majorTickType;
/*  19:    */   private byte field_2_minorTickType;
/*  20:    */   private byte field_3_labelPosition;
/*  21:    */   private byte field_4_background;
/*  22:    */   private int field_5_labelColorRgb;
/*  23:    */   private int field_6_zero1;
/*  24:    */   private int field_7_zero2;
/*  25:    */   private int field_8_zero3;
/*  26:    */   private int field_9_zero4;
/*  27:    */   private short field_10_options;
/*  28:    */   private short field_11_tickColor;
/*  29:    */   private short field_12_zero5;
/*  30:    */   
/*  31:    */   public TickRecord() {}
/*  32:    */   
/*  33:    */   public TickRecord(RecordInputStream in)
/*  34:    */   {
/*  35: 62 */     this.field_1_majorTickType = in.readByte();
/*  36: 63 */     this.field_2_minorTickType = in.readByte();
/*  37: 64 */     this.field_3_labelPosition = in.readByte();
/*  38: 65 */     this.field_4_background = in.readByte();
/*  39: 66 */     this.field_5_labelColorRgb = in.readInt();
/*  40: 67 */     this.field_6_zero1 = in.readInt();
/*  41: 68 */     this.field_7_zero2 = in.readInt();
/*  42: 69 */     this.field_8_zero3 = in.readInt();
/*  43: 70 */     this.field_9_zero4 = in.readInt();
/*  44:    */     
/*  45: 72 */     this.field_10_options = in.readShort();
/*  46: 73 */     this.field_11_tickColor = in.readShort();
/*  47: 74 */     this.field_12_zero5 = in.readShort();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String toString()
/*  51:    */   {
/*  52: 79 */     StringBuffer buffer = new StringBuffer();
/*  53:    */     
/*  54: 81 */     buffer.append("[TICK]\n");
/*  55: 82 */     buffer.append("    .majorTickType        = ").append("0x").append(HexDump.toHex(getMajorTickType())).append(" (").append(getMajorTickType()).append(" )");
/*  56:    */     
/*  57:    */ 
/*  58: 85 */     buffer.append(System.getProperty("line.separator"));
/*  59: 86 */     buffer.append("    .minorTickType        = ").append("0x").append(HexDump.toHex(getMinorTickType())).append(" (").append(getMinorTickType()).append(" )");
/*  60:    */     
/*  61:    */ 
/*  62: 89 */     buffer.append(System.getProperty("line.separator"));
/*  63: 90 */     buffer.append("    .labelPosition        = ").append("0x").append(HexDump.toHex(getLabelPosition())).append(" (").append(getLabelPosition()).append(" )");
/*  64:    */     
/*  65:    */ 
/*  66: 93 */     buffer.append(System.getProperty("line.separator"));
/*  67: 94 */     buffer.append("    .background           = ").append("0x").append(HexDump.toHex(getBackground())).append(" (").append(getBackground()).append(" )");
/*  68:    */     
/*  69:    */ 
/*  70: 97 */     buffer.append(System.getProperty("line.separator"));
/*  71: 98 */     buffer.append("    .labelColorRgb        = ").append("0x").append(HexDump.toHex(getLabelColorRgb())).append(" (").append(getLabelColorRgb()).append(" )");
/*  72:    */     
/*  73:    */ 
/*  74:101 */     buffer.append(System.getProperty("line.separator"));
/*  75:102 */     buffer.append("    .zero1                = ").append("0x").append(HexDump.toHex(getZero1())).append(" (").append(getZero1()).append(" )");
/*  76:    */     
/*  77:    */ 
/*  78:105 */     buffer.append(System.getProperty("line.separator"));
/*  79:106 */     buffer.append("    .zero2                = ").append("0x").append(HexDump.toHex(getZero2())).append(" (").append(getZero2()).append(" )");
/*  80:    */     
/*  81:    */ 
/*  82:109 */     buffer.append(System.getProperty("line.separator"));
/*  83:110 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  84:    */     
/*  85:    */ 
/*  86:113 */     buffer.append(System.getProperty("line.separator"));
/*  87:114 */     buffer.append("         .autoTextColor            = ").append(isAutoTextColor()).append('\n');
/*  88:115 */     buffer.append("         .autoTextBackground       = ").append(isAutoTextBackground()).append('\n');
/*  89:116 */     buffer.append("         .rotation                 = ").append(getRotation()).append('\n');
/*  90:117 */     buffer.append("         .autorotate               = ").append(isAutorotate()).append('\n');
/*  91:118 */     buffer.append("    .tickColor            = ").append("0x").append(HexDump.toHex(getTickColor())).append(" (").append(getTickColor()).append(" )");
/*  92:    */     
/*  93:    */ 
/*  94:121 */     buffer.append(System.getProperty("line.separator"));
/*  95:122 */     buffer.append("    .zero3                = ").append("0x").append(HexDump.toHex(getZero3())).append(" (").append(getZero3()).append(" )");
/*  96:    */     
/*  97:    */ 
/*  98:125 */     buffer.append(System.getProperty("line.separator"));
/*  99:    */     
/* 100:127 */     buffer.append("[/TICK]\n");
/* 101:128 */     return buffer.toString();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void serialize(LittleEndianOutput out)
/* 105:    */   {
/* 106:132 */     out.writeByte(this.field_1_majorTickType);
/* 107:133 */     out.writeByte(this.field_2_minorTickType);
/* 108:134 */     out.writeByte(this.field_3_labelPosition);
/* 109:135 */     out.writeByte(this.field_4_background);
/* 110:136 */     out.writeInt(this.field_5_labelColorRgb);
/* 111:137 */     out.writeInt(this.field_6_zero1);
/* 112:138 */     out.writeInt(this.field_7_zero2);
/* 113:139 */     out.writeInt(this.field_8_zero3);
/* 114:140 */     out.writeInt(this.field_9_zero4);
/* 115:141 */     out.writeShort(this.field_10_options);
/* 116:142 */     out.writeShort(this.field_11_tickColor);
/* 117:143 */     out.writeShort(this.field_12_zero5);
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected int getDataSize()
/* 121:    */   {
/* 122:147 */     return 30;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public short getSid()
/* 126:    */   {
/* 127:152 */     return 4126;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public Object clone()
/* 131:    */   {
/* 132:156 */     TickRecord rec = new TickRecord();
/* 133:    */     
/* 134:158 */     rec.field_1_majorTickType = this.field_1_majorTickType;
/* 135:159 */     rec.field_2_minorTickType = this.field_2_minorTickType;
/* 136:160 */     rec.field_3_labelPosition = this.field_3_labelPosition;
/* 137:161 */     rec.field_4_background = this.field_4_background;
/* 138:162 */     rec.field_5_labelColorRgb = this.field_5_labelColorRgb;
/* 139:163 */     rec.field_6_zero1 = this.field_6_zero1;
/* 140:164 */     rec.field_7_zero2 = this.field_7_zero2;
/* 141:165 */     rec.field_8_zero3 = this.field_8_zero3;
/* 142:166 */     rec.field_9_zero4 = this.field_9_zero4;
/* 143:167 */     rec.field_10_options = this.field_10_options;
/* 144:168 */     rec.field_11_tickColor = this.field_11_tickColor;
/* 145:169 */     rec.field_12_zero5 = this.field_12_zero5;
/* 146:170 */     return rec;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public byte getMajorTickType()
/* 150:    */   {
/* 151:181 */     return this.field_1_majorTickType;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setMajorTickType(byte field_1_majorTickType)
/* 155:    */   {
/* 156:189 */     this.field_1_majorTickType = field_1_majorTickType;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public byte getMinorTickType()
/* 160:    */   {
/* 161:197 */     return this.field_2_minorTickType;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setMinorTickType(byte field_2_minorTickType)
/* 165:    */   {
/* 166:205 */     this.field_2_minorTickType = field_2_minorTickType;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public byte getLabelPosition()
/* 170:    */   {
/* 171:213 */     return this.field_3_labelPosition;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public void setLabelPosition(byte field_3_labelPosition)
/* 175:    */   {
/* 176:221 */     this.field_3_labelPosition = field_3_labelPosition;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public byte getBackground()
/* 180:    */   {
/* 181:229 */     return this.field_4_background;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void setBackground(byte field_4_background)
/* 185:    */   {
/* 186:237 */     this.field_4_background = field_4_background;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public int getLabelColorRgb()
/* 190:    */   {
/* 191:245 */     return this.field_5_labelColorRgb;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void setLabelColorRgb(int field_5_labelColorRgb)
/* 195:    */   {
/* 196:253 */     this.field_5_labelColorRgb = field_5_labelColorRgb;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public int getZero1()
/* 200:    */   {
/* 201:261 */     return this.field_6_zero1;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public void setZero1(int field_6_zero1)
/* 205:    */   {
/* 206:269 */     this.field_6_zero1 = field_6_zero1;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public int getZero2()
/* 210:    */   {
/* 211:277 */     return this.field_7_zero2;
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void setZero2(int field_7_zero2)
/* 215:    */   {
/* 216:285 */     this.field_7_zero2 = field_7_zero2;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public short getOptions()
/* 220:    */   {
/* 221:293 */     return this.field_10_options;
/* 222:    */   }
/* 223:    */   
/* 224:    */   public void setOptions(short field_10_options)
/* 225:    */   {
/* 226:301 */     this.field_10_options = field_10_options;
/* 227:    */   }
/* 228:    */   
/* 229:    */   public short getTickColor()
/* 230:    */   {
/* 231:309 */     return this.field_11_tickColor;
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void setTickColor(short field_11_tickColor)
/* 235:    */   {
/* 236:317 */     this.field_11_tickColor = field_11_tickColor;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public short getZero3()
/* 240:    */   {
/* 241:325 */     return this.field_12_zero5;
/* 242:    */   }
/* 243:    */   
/* 244:    */   public void setZero3(short field_12_zero3)
/* 245:    */   {
/* 246:333 */     this.field_12_zero5 = field_12_zero3;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setAutoTextColor(boolean value)
/* 250:    */   {
/* 251:342 */     this.field_10_options = autoTextColor.setShortBoolean(this.field_10_options, value);
/* 252:    */   }
/* 253:    */   
/* 254:    */   public boolean isAutoTextColor()
/* 255:    */   {
/* 256:351 */     return autoTextColor.isSet(this.field_10_options);
/* 257:    */   }
/* 258:    */   
/* 259:    */   public void setAutoTextBackground(boolean value)
/* 260:    */   {
/* 261:360 */     this.field_10_options = autoTextBackground.setShortBoolean(this.field_10_options, value);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public boolean isAutoTextBackground()
/* 265:    */   {
/* 266:369 */     return autoTextBackground.isSet(this.field_10_options);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setRotation(short value)
/* 270:    */   {
/* 271:378 */     this.field_10_options = rotation.setShortValue(this.field_10_options, value);
/* 272:    */   }
/* 273:    */   
/* 274:    */   public short getRotation()
/* 275:    */   {
/* 276:387 */     return rotation.getShortValue(this.field_10_options);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public void setAutorotate(boolean value)
/* 280:    */   {
/* 281:396 */     this.field_10_options = autorotate.setShortBoolean(this.field_10_options, value);
/* 282:    */   }
/* 283:    */   
/* 284:    */   public boolean isAutorotate()
/* 285:    */   {
/* 286:405 */     return autorotate.isSet(this.field_10_options);
/* 287:    */   }
/* 288:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.TickRecord
 * JD-Core Version:    0.7.0.1
 */