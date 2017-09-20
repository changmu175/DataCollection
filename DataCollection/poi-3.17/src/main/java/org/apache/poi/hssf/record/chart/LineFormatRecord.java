/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class LineFormatRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4103;
/*  15: 33 */   private static final BitField auto = BitFieldFactory.getInstance(1);
/*  16: 34 */   private static final BitField drawTicks = BitFieldFactory.getInstance(4);
/*  17: 35 */   private static final BitField unknown = BitFieldFactory.getInstance(4);
/*  18:    */   private int field_1_lineColor;
/*  19:    */   private short field_2_linePattern;
/*  20:    */   public static final short LINE_PATTERN_SOLID = 0;
/*  21:    */   public static final short LINE_PATTERN_DASH = 1;
/*  22:    */   public static final short LINE_PATTERN_DOT = 2;
/*  23:    */   public static final short LINE_PATTERN_DASH_DOT = 3;
/*  24:    */   public static final short LINE_PATTERN_DASH_DOT_DOT = 4;
/*  25:    */   public static final short LINE_PATTERN_NONE = 5;
/*  26:    */   public static final short LINE_PATTERN_DARK_GRAY_PATTERN = 6;
/*  27:    */   public static final short LINE_PATTERN_MEDIUM_GRAY_PATTERN = 7;
/*  28:    */   public static final short LINE_PATTERN_LIGHT_GRAY_PATTERN = 8;
/*  29:    */   private short field_3_weight;
/*  30:    */   public static final short WEIGHT_HAIRLINE = -1;
/*  31:    */   public static final short WEIGHT_NARROW = 0;
/*  32:    */   public static final short WEIGHT_MEDIUM = 1;
/*  33:    */   public static final short WEIGHT_WIDE = 2;
/*  34:    */   private short field_4_format;
/*  35:    */   private short field_5_colourPaletteIndex;
/*  36:    */   
/*  37:    */   public LineFormatRecord() {}
/*  38:    */   
/*  39:    */   public LineFormatRecord(RecordInputStream in)
/*  40:    */   {
/*  41: 64 */     this.field_1_lineColor = in.readInt();
/*  42: 65 */     this.field_2_linePattern = in.readShort();
/*  43: 66 */     this.field_3_weight = in.readShort();
/*  44: 67 */     this.field_4_format = in.readShort();
/*  45: 68 */     this.field_5_colourPaletteIndex = in.readShort();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String toString()
/*  49:    */   {
/*  50: 74 */     StringBuffer buffer = new StringBuffer();
/*  51:    */     
/*  52: 76 */     buffer.append("[LINEFORMAT]\n");
/*  53: 77 */     buffer.append("    .lineColor            = ").append("0x").append(HexDump.toHex(getLineColor())).append(" (").append(getLineColor()).append(" )");
/*  54:    */     
/*  55:    */ 
/*  56: 80 */     buffer.append(System.getProperty("line.separator"));
/*  57: 81 */     buffer.append("    .linePattern          = ").append("0x").append(HexDump.toHex(getLinePattern())).append(" (").append(getLinePattern()).append(" )");
/*  58:    */     
/*  59:    */ 
/*  60: 84 */     buffer.append(System.getProperty("line.separator"));
/*  61: 85 */     buffer.append("    .weight               = ").append("0x").append(HexDump.toHex(getWeight())).append(" (").append(getWeight()).append(" )");
/*  62:    */     
/*  63:    */ 
/*  64: 88 */     buffer.append(System.getProperty("line.separator"));
/*  65: 89 */     buffer.append("    .format               = ").append("0x").append(HexDump.toHex(getFormat())).append(" (").append(getFormat()).append(" )");
/*  66:    */     
/*  67:    */ 
/*  68: 92 */     buffer.append(System.getProperty("line.separator"));
/*  69: 93 */     buffer.append("         .auto                     = ").append(isAuto()).append('\n');
/*  70: 94 */     buffer.append("         .drawTicks                = ").append(isDrawTicks()).append('\n');
/*  71: 95 */     buffer.append("         .unknown                  = ").append(isUnknown()).append('\n');
/*  72: 96 */     buffer.append("    .colourPaletteIndex   = ").append("0x").append(HexDump.toHex(getColourPaletteIndex())).append(" (").append(getColourPaletteIndex()).append(" )");
/*  73:    */     
/*  74:    */ 
/*  75: 99 */     buffer.append(System.getProperty("line.separator"));
/*  76:    */     
/*  77:101 */     buffer.append("[/LINEFORMAT]\n");
/*  78:102 */     return buffer.toString();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void serialize(LittleEndianOutput out)
/*  82:    */   {
/*  83:106 */     out.writeInt(this.field_1_lineColor);
/*  84:107 */     out.writeShort(this.field_2_linePattern);
/*  85:108 */     out.writeShort(this.field_3_weight);
/*  86:109 */     out.writeShort(this.field_4_format);
/*  87:110 */     out.writeShort(this.field_5_colourPaletteIndex);
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected int getDataSize()
/*  91:    */   {
/*  92:114 */     return 12;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public short getSid()
/*  96:    */   {
/*  97:119 */     return 4103;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public LineFormatRecord clone()
/* 101:    */   {
/* 102:124 */     LineFormatRecord rec = new LineFormatRecord();
/* 103:    */     
/* 104:126 */     rec.field_1_lineColor = this.field_1_lineColor;
/* 105:127 */     rec.field_2_linePattern = this.field_2_linePattern;
/* 106:128 */     rec.field_3_weight = this.field_3_weight;
/* 107:129 */     rec.field_4_format = this.field_4_format;
/* 108:130 */     rec.field_5_colourPaletteIndex = this.field_5_colourPaletteIndex;
/* 109:131 */     return rec;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getLineColor()
/* 113:    */   {
/* 114:142 */     return this.field_1_lineColor;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setLineColor(int field_1_lineColor)
/* 118:    */   {
/* 119:150 */     this.field_1_lineColor = field_1_lineColor;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public short getLinePattern()
/* 123:    */   {
/* 124:169 */     return this.field_2_linePattern;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setLinePattern(short field_2_linePattern)
/* 128:    */   {
/* 129:189 */     this.field_2_linePattern = field_2_linePattern;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public short getWeight()
/* 133:    */   {
/* 134:203 */     return this.field_3_weight;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void setWeight(short field_3_weight)
/* 138:    */   {
/* 139:218 */     this.field_3_weight = field_3_weight;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public short getFormat()
/* 143:    */   {
/* 144:226 */     return this.field_4_format;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setFormat(short field_4_format)
/* 148:    */   {
/* 149:234 */     this.field_4_format = field_4_format;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public short getColourPaletteIndex()
/* 153:    */   {
/* 154:242 */     return this.field_5_colourPaletteIndex;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void setColourPaletteIndex(short field_5_colourPaletteIndex)
/* 158:    */   {
/* 159:250 */     this.field_5_colourPaletteIndex = field_5_colourPaletteIndex;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setAuto(boolean value)
/* 163:    */   {
/* 164:259 */     this.field_4_format = auto.setShortBoolean(this.field_4_format, value);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean isAuto()
/* 168:    */   {
/* 169:268 */     return auto.isSet(this.field_4_format);
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setDrawTicks(boolean value)
/* 173:    */   {
/* 174:277 */     this.field_4_format = drawTicks.setShortBoolean(this.field_4_format, value);
/* 175:    */   }
/* 176:    */   
/* 177:    */   public boolean isDrawTicks()
/* 178:    */   {
/* 179:286 */     return drawTicks.isSet(this.field_4_format);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setUnknown(boolean value)
/* 183:    */   {
/* 184:295 */     this.field_4_format = unknown.setShortBoolean(this.field_4_format, value);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean isUnknown()
/* 188:    */   {
/* 189:304 */     return unknown.isSet(this.field_4_format);
/* 190:    */   }
/* 191:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.LineFormatRecord
 * JD-Core Version:    0.7.0.1
 */