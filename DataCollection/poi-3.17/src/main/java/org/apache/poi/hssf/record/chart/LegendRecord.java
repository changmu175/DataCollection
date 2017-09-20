/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class LegendRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4117;
/*  15: 33 */   private static final BitField autoPosition = BitFieldFactory.getInstance(1);
/*  16: 34 */   private static final BitField autoSeries = BitFieldFactory.getInstance(2);
/*  17: 35 */   private static final BitField autoXPositioning = BitFieldFactory.getInstance(4);
/*  18: 36 */   private static final BitField autoYPositioning = BitFieldFactory.getInstance(8);
/*  19: 37 */   private static final BitField vertical = BitFieldFactory.getInstance(16);
/*  20: 38 */   private static final BitField dataTable = BitFieldFactory.getInstance(32);
/*  21:    */   private int field_1_xAxisUpperLeft;
/*  22:    */   private int field_2_yAxisUpperLeft;
/*  23:    */   private int field_3_xSize;
/*  24:    */   private int field_4_ySize;
/*  25:    */   private byte field_5_type;
/*  26:    */   public static final byte TYPE_BOTTOM = 0;
/*  27:    */   public static final byte TYPE_CORNER = 1;
/*  28:    */   public static final byte TYPE_TOP = 2;
/*  29:    */   public static final byte TYPE_RIGHT = 3;
/*  30:    */   public static final byte TYPE_LEFT = 4;
/*  31:    */   public static final byte TYPE_UNDOCKED = 7;
/*  32:    */   private byte field_6_spacing;
/*  33:    */   public static final byte SPACING_CLOSE = 0;
/*  34:    */   public static final byte SPACING_MEDIUM = 1;
/*  35:    */   public static final byte SPACING_OPEN = 2;
/*  36:    */   private short field_7_options;
/*  37:    */   
/*  38:    */   public LegendRecord() {}
/*  39:    */   
/*  40:    */   public LegendRecord(RecordInputStream in)
/*  41:    */   {
/*  42: 65 */     this.field_1_xAxisUpperLeft = in.readInt();
/*  43: 66 */     this.field_2_yAxisUpperLeft = in.readInt();
/*  44: 67 */     this.field_3_xSize = in.readInt();
/*  45: 68 */     this.field_4_ySize = in.readInt();
/*  46: 69 */     this.field_5_type = in.readByte();
/*  47: 70 */     this.field_6_spacing = in.readByte();
/*  48: 71 */     this.field_7_options = in.readShort();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String toString()
/*  52:    */   {
/*  53: 76 */     StringBuffer buffer = new StringBuffer();
/*  54:    */     
/*  55: 78 */     buffer.append("[LEGEND]\n");
/*  56: 79 */     buffer.append("    .xAxisUpperLeft       = ").append("0x").append(HexDump.toHex(getXAxisUpperLeft())).append(" (").append(getXAxisUpperLeft()).append(" )");
/*  57:    */     
/*  58:    */ 
/*  59: 82 */     buffer.append(System.getProperty("line.separator"));
/*  60: 83 */     buffer.append("    .yAxisUpperLeft       = ").append("0x").append(HexDump.toHex(getYAxisUpperLeft())).append(" (").append(getYAxisUpperLeft()).append(" )");
/*  61:    */     
/*  62:    */ 
/*  63: 86 */     buffer.append(System.getProperty("line.separator"));
/*  64: 87 */     buffer.append("    .xSize                = ").append("0x").append(HexDump.toHex(getXSize())).append(" (").append(getXSize()).append(" )");
/*  65:    */     
/*  66:    */ 
/*  67: 90 */     buffer.append(System.getProperty("line.separator"));
/*  68: 91 */     buffer.append("    .ySize                = ").append("0x").append(HexDump.toHex(getYSize())).append(" (").append(getYSize()).append(" )");
/*  69:    */     
/*  70:    */ 
/*  71: 94 */     buffer.append(System.getProperty("line.separator"));
/*  72: 95 */     buffer.append("    .type                 = ").append("0x").append(HexDump.toHex(getType())).append(" (").append(getType()).append(" )");
/*  73:    */     
/*  74:    */ 
/*  75: 98 */     buffer.append(System.getProperty("line.separator"));
/*  76: 99 */     buffer.append("    .spacing              = ").append("0x").append(HexDump.toHex(getSpacing())).append(" (").append(getSpacing()).append(" )");
/*  77:    */     
/*  78:    */ 
/*  79:102 */     buffer.append(System.getProperty("line.separator"));
/*  80:103 */     buffer.append("    .options              = ").append("0x").append(HexDump.toHex(getOptions())).append(" (").append(getOptions()).append(" )");
/*  81:    */     
/*  82:    */ 
/*  83:106 */     buffer.append(System.getProperty("line.separator"));
/*  84:107 */     buffer.append("         .autoPosition             = ").append(isAutoPosition()).append('\n');
/*  85:108 */     buffer.append("         .autoSeries               = ").append(isAutoSeries()).append('\n');
/*  86:109 */     buffer.append("         .autoXPositioning         = ").append(isAutoXPositioning()).append('\n');
/*  87:110 */     buffer.append("         .autoYPositioning         = ").append(isAutoYPositioning()).append('\n');
/*  88:111 */     buffer.append("         .vertical                 = ").append(isVertical()).append('\n');
/*  89:112 */     buffer.append("         .dataTable                = ").append(isDataTable()).append('\n');
/*  90:    */     
/*  91:114 */     buffer.append("[/LEGEND]\n");
/*  92:115 */     return buffer.toString();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void serialize(LittleEndianOutput out)
/*  96:    */   {
/*  97:119 */     out.writeInt(this.field_1_xAxisUpperLeft);
/*  98:120 */     out.writeInt(this.field_2_yAxisUpperLeft);
/*  99:121 */     out.writeInt(this.field_3_xSize);
/* 100:122 */     out.writeInt(this.field_4_ySize);
/* 101:123 */     out.writeByte(this.field_5_type);
/* 102:124 */     out.writeByte(this.field_6_spacing);
/* 103:125 */     out.writeShort(this.field_7_options);
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected int getDataSize()
/* 107:    */   {
/* 108:129 */     return 20;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public short getSid()
/* 112:    */   {
/* 113:134 */     return 4117;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public LegendRecord clone()
/* 117:    */   {
/* 118:139 */     LegendRecord rec = new LegendRecord();
/* 119:    */     
/* 120:141 */     rec.field_1_xAxisUpperLeft = this.field_1_xAxisUpperLeft;
/* 121:142 */     rec.field_2_yAxisUpperLeft = this.field_2_yAxisUpperLeft;
/* 122:143 */     rec.field_3_xSize = this.field_3_xSize;
/* 123:144 */     rec.field_4_ySize = this.field_4_ySize;
/* 124:145 */     rec.field_5_type = this.field_5_type;
/* 125:146 */     rec.field_6_spacing = this.field_6_spacing;
/* 126:147 */     rec.field_7_options = this.field_7_options;
/* 127:148 */     return rec;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public int getXAxisUpperLeft()
/* 131:    */   {
/* 132:159 */     return this.field_1_xAxisUpperLeft;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setXAxisUpperLeft(int field_1_xAxisUpperLeft)
/* 136:    */   {
/* 137:167 */     this.field_1_xAxisUpperLeft = field_1_xAxisUpperLeft;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public int getYAxisUpperLeft()
/* 141:    */   {
/* 142:175 */     return this.field_2_yAxisUpperLeft;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setYAxisUpperLeft(int field_2_yAxisUpperLeft)
/* 146:    */   {
/* 147:183 */     this.field_2_yAxisUpperLeft = field_2_yAxisUpperLeft;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getXSize()
/* 151:    */   {
/* 152:191 */     return this.field_3_xSize;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setXSize(int field_3_xSize)
/* 156:    */   {
/* 157:199 */     this.field_3_xSize = field_3_xSize;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public int getYSize()
/* 161:    */   {
/* 162:207 */     return this.field_4_ySize;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setYSize(int field_4_ySize)
/* 166:    */   {
/* 167:215 */     this.field_4_ySize = field_4_ySize;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public byte getType()
/* 171:    */   {
/* 172:231 */     return this.field_5_type;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setType(byte field_5_type)
/* 176:    */   {
/* 177:248 */     this.field_5_type = field_5_type;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public byte getSpacing()
/* 181:    */   {
/* 182:261 */     return this.field_6_spacing;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setSpacing(byte field_6_spacing)
/* 186:    */   {
/* 187:275 */     this.field_6_spacing = field_6_spacing;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public short getOptions()
/* 191:    */   {
/* 192:283 */     return this.field_7_options;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setOptions(short field_7_options)
/* 196:    */   {
/* 197:291 */     this.field_7_options = field_7_options;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setAutoPosition(boolean value)
/* 201:    */   {
/* 202:300 */     this.field_7_options = autoPosition.setShortBoolean(this.field_7_options, value);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public boolean isAutoPosition()
/* 206:    */   {
/* 207:309 */     return autoPosition.isSet(this.field_7_options);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void setAutoSeries(boolean value)
/* 211:    */   {
/* 212:318 */     this.field_7_options = autoSeries.setShortBoolean(this.field_7_options, value);
/* 213:    */   }
/* 214:    */   
/* 215:    */   public boolean isAutoSeries()
/* 216:    */   {
/* 217:327 */     return autoSeries.isSet(this.field_7_options);
/* 218:    */   }
/* 219:    */   
/* 220:    */   public void setAutoXPositioning(boolean value)
/* 221:    */   {
/* 222:336 */     this.field_7_options = autoXPositioning.setShortBoolean(this.field_7_options, value);
/* 223:    */   }
/* 224:    */   
/* 225:    */   public boolean isAutoXPositioning()
/* 226:    */   {
/* 227:345 */     return autoXPositioning.isSet(this.field_7_options);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void setAutoYPositioning(boolean value)
/* 231:    */   {
/* 232:354 */     this.field_7_options = autoYPositioning.setShortBoolean(this.field_7_options, value);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public boolean isAutoYPositioning()
/* 236:    */   {
/* 237:363 */     return autoYPositioning.isSet(this.field_7_options);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public void setVertical(boolean value)
/* 241:    */   {
/* 242:372 */     this.field_7_options = vertical.setShortBoolean(this.field_7_options, value);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public boolean isVertical()
/* 246:    */   {
/* 247:381 */     return vertical.isSet(this.field_7_options);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void setDataTable(boolean value)
/* 251:    */   {
/* 252:390 */     this.field_7_options = dataTable.setShortBoolean(this.field_7_options, value);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public boolean isDataTable()
/* 256:    */   {
/* 257:399 */     return dataTable.isSet(this.field_7_options);
/* 258:    */   }
/* 259:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.LegendRecord
 * JD-Core Version:    0.7.0.1
 */