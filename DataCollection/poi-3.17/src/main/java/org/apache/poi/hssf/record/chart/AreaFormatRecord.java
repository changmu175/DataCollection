/*   1:    */ package org.apache.poi.hssf.record.chart;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class AreaFormatRecord
/*  11:    */   extends StandardRecord
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   public static final short sid = 4106;
/*  15: 35 */   private static final BitField automatic = BitFieldFactory.getInstance(1);
/*  16: 36 */   private static final BitField invert = BitFieldFactory.getInstance(2);
/*  17:    */   private int field_1_foregroundColor;
/*  18:    */   private int field_2_backgroundColor;
/*  19:    */   private short field_3_pattern;
/*  20:    */   private short field_4_formatFlags;
/*  21:    */   private short field_5_forecolorIndex;
/*  22:    */   private short field_6_backcolorIndex;
/*  23:    */   
/*  24:    */   public AreaFormatRecord() {}
/*  25:    */   
/*  26:    */   public AreaFormatRecord(RecordInputStream in)
/*  27:    */   {
/*  28: 53 */     this.field_1_foregroundColor = in.readInt();
/*  29: 54 */     this.field_2_backgroundColor = in.readInt();
/*  30: 55 */     this.field_3_pattern = in.readShort();
/*  31: 56 */     this.field_4_formatFlags = in.readShort();
/*  32: 57 */     this.field_5_forecolorIndex = in.readShort();
/*  33: 58 */     this.field_6_backcolorIndex = in.readShort();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String toString()
/*  37:    */   {
/*  38: 64 */     StringBuffer buffer = new StringBuffer();
/*  39:    */     
/*  40: 66 */     buffer.append("[AREAFORMAT]\n");
/*  41: 67 */     buffer.append("    .foregroundColor      = ").append("0x").append(HexDump.toHex(getForegroundColor())).append(" (").append(getForegroundColor()).append(" )");
/*  42:    */     
/*  43:    */ 
/*  44: 70 */     buffer.append(System.getProperty("line.separator"));
/*  45: 71 */     buffer.append("    .backgroundColor      = ").append("0x").append(HexDump.toHex(getBackgroundColor())).append(" (").append(getBackgroundColor()).append(" )");
/*  46:    */     
/*  47:    */ 
/*  48: 74 */     buffer.append(System.getProperty("line.separator"));
/*  49: 75 */     buffer.append("    .pattern              = ").append("0x").append(HexDump.toHex(getPattern())).append(" (").append(getPattern()).append(" )");
/*  50:    */     
/*  51:    */ 
/*  52: 78 */     buffer.append(System.getProperty("line.separator"));
/*  53: 79 */     buffer.append("    .formatFlags          = ").append("0x").append(HexDump.toHex(getFormatFlags())).append(" (").append(getFormatFlags()).append(" )");
/*  54:    */     
/*  55:    */ 
/*  56: 82 */     buffer.append(System.getProperty("line.separator"));
/*  57: 83 */     buffer.append("         .automatic                = ").append(isAutomatic()).append('\n');
/*  58: 84 */     buffer.append("         .invert                   = ").append(isInvert()).append('\n');
/*  59: 85 */     buffer.append("    .forecolorIndex       = ").append("0x").append(HexDump.toHex(getForecolorIndex())).append(" (").append(getForecolorIndex()).append(" )");
/*  60:    */     
/*  61:    */ 
/*  62: 88 */     buffer.append(System.getProperty("line.separator"));
/*  63: 89 */     buffer.append("    .backcolorIndex       = ").append("0x").append(HexDump.toHex(getBackcolorIndex())).append(" (").append(getBackcolorIndex()).append(" )");
/*  64:    */     
/*  65:    */ 
/*  66: 92 */     buffer.append(System.getProperty("line.separator"));
/*  67:    */     
/*  68: 94 */     buffer.append("[/AREAFORMAT]\n");
/*  69: 95 */     return buffer.toString();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void serialize(LittleEndianOutput out)
/*  73:    */   {
/*  74: 99 */     out.writeInt(this.field_1_foregroundColor);
/*  75:100 */     out.writeInt(this.field_2_backgroundColor);
/*  76:101 */     out.writeShort(this.field_3_pattern);
/*  77:102 */     out.writeShort(this.field_4_formatFlags);
/*  78:103 */     out.writeShort(this.field_5_forecolorIndex);
/*  79:104 */     out.writeShort(this.field_6_backcolorIndex);
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected int getDataSize()
/*  83:    */   {
/*  84:108 */     return 16;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public short getSid()
/*  88:    */   {
/*  89:113 */     return 4106;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public AreaFormatRecord clone()
/*  93:    */   {
/*  94:118 */     AreaFormatRecord rec = new AreaFormatRecord();
/*  95:    */     
/*  96:120 */     rec.field_1_foregroundColor = this.field_1_foregroundColor;
/*  97:121 */     rec.field_2_backgroundColor = this.field_2_backgroundColor;
/*  98:122 */     rec.field_3_pattern = this.field_3_pattern;
/*  99:123 */     rec.field_4_formatFlags = this.field_4_formatFlags;
/* 100:124 */     rec.field_5_forecolorIndex = this.field_5_forecolorIndex;
/* 101:125 */     rec.field_6_backcolorIndex = this.field_6_backcolorIndex;
/* 102:126 */     return rec;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int getForegroundColor()
/* 106:    */   {
/* 107:137 */     return this.field_1_foregroundColor;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setForegroundColor(int field_1_foregroundColor)
/* 111:    */   {
/* 112:145 */     this.field_1_foregroundColor = field_1_foregroundColor;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int getBackgroundColor()
/* 116:    */   {
/* 117:153 */     return this.field_2_backgroundColor;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setBackgroundColor(int field_2_backgroundColor)
/* 121:    */   {
/* 122:161 */     this.field_2_backgroundColor = field_2_backgroundColor;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public short getPattern()
/* 126:    */   {
/* 127:169 */     return this.field_3_pattern;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setPattern(short field_3_pattern)
/* 131:    */   {
/* 132:177 */     this.field_3_pattern = field_3_pattern;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public short getFormatFlags()
/* 136:    */   {
/* 137:185 */     return this.field_4_formatFlags;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setFormatFlags(short field_4_formatFlags)
/* 141:    */   {
/* 142:193 */     this.field_4_formatFlags = field_4_formatFlags;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public short getForecolorIndex()
/* 146:    */   {
/* 147:201 */     return this.field_5_forecolorIndex;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setForecolorIndex(short field_5_forecolorIndex)
/* 151:    */   {
/* 152:209 */     this.field_5_forecolorIndex = field_5_forecolorIndex;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public short getBackcolorIndex()
/* 156:    */   {
/* 157:217 */     return this.field_6_backcolorIndex;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setBackcolorIndex(short field_6_backcolorIndex)
/* 161:    */   {
/* 162:225 */     this.field_6_backcolorIndex = field_6_backcolorIndex;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void setAutomatic(boolean value)
/* 166:    */   {
/* 167:234 */     this.field_4_formatFlags = automatic.setShortBoolean(this.field_4_formatFlags, value);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public boolean isAutomatic()
/* 171:    */   {
/* 172:243 */     return automatic.isSet(this.field_4_formatFlags);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setInvert(boolean value)
/* 176:    */   {
/* 177:252 */     this.field_4_formatFlags = invert.setShortBoolean(this.field_4_formatFlags, value);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean isInvert()
/* 181:    */   {
/* 182:261 */     return invert.isSet(this.field_4_formatFlags);
/* 183:    */   }
/* 184:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.AreaFormatRecord
 * JD-Core Version:    0.7.0.1
 */