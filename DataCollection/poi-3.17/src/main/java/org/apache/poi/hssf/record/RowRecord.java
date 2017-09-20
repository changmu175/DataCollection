/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class RowRecord
/*   9:    */   extends StandardRecord
/*  10:    */ {
/*  11:    */   public static final short sid = 520;
/*  12:    */   public static final int ENCODED_SIZE = 20;
/*  13:    */   private static final int OPTION_BITS_ALWAYS_SET = 256;
/*  14:    */   private int field_1_row_number;
/*  15:    */   private int field_2_first_col;
/*  16:    */   private int field_3_last_col;
/*  17:    */   private short field_4_height;
/*  18:    */   private short field_5_optimize;
/*  19:    */   private short field_6_reserved;
/*  20:    */   private int field_7_option_flags;
/*  21: 50 */   private static final BitField outlineLevel = BitFieldFactory.getInstance(7);
/*  22: 52 */   private static final BitField colapsed = BitFieldFactory.getInstance(16);
/*  23: 53 */   private static final BitField zeroHeight = BitFieldFactory.getInstance(32);
/*  24: 54 */   private static final BitField badFontHeight = BitFieldFactory.getInstance(64);
/*  25: 55 */   private static final BitField formatted = BitFieldFactory.getInstance(128);
/*  26:    */   private int field_8_option_flags;
/*  27: 59 */   private static final BitField xfIndex = BitFieldFactory.getInstance(4095);
/*  28: 60 */   private static final BitField topBorder = BitFieldFactory.getInstance(4096);
/*  29: 61 */   private static final BitField bottomBorder = BitFieldFactory.getInstance(8192);
/*  30: 62 */   private static final BitField phoeneticGuide = BitFieldFactory.getInstance(16384);
/*  31:    */   
/*  32:    */   public RowRecord(int rowNumber)
/*  33:    */   {
/*  34: 66 */     if (rowNumber < 0) {
/*  35: 67 */       throw new IllegalArgumentException("Invalid row number (" + rowNumber + ")");
/*  36:    */     }
/*  37: 69 */     this.field_1_row_number = rowNumber;
/*  38: 70 */     this.field_4_height = 255;
/*  39: 71 */     this.field_5_optimize = 0;
/*  40: 72 */     this.field_6_reserved = 0;
/*  41: 73 */     this.field_7_option_flags = 256;
/*  42:    */     
/*  43: 75 */     this.field_8_option_flags = 15;
/*  44: 76 */     setEmpty();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RowRecord(RecordInputStream in)
/*  48:    */   {
/*  49: 80 */     this.field_1_row_number = in.readUShort();
/*  50: 81 */     if (this.field_1_row_number < 0) {
/*  51: 82 */       throw new IllegalArgumentException("Invalid row number " + this.field_1_row_number + " found in InputStream");
/*  52:    */     }
/*  53: 84 */     this.field_2_first_col = in.readShort();
/*  54: 85 */     this.field_3_last_col = in.readShort();
/*  55: 86 */     this.field_4_height = in.readShort();
/*  56: 87 */     this.field_5_optimize = in.readShort();
/*  57: 88 */     this.field_6_reserved = in.readShort();
/*  58: 89 */     this.field_7_option_flags = in.readShort();
/*  59: 90 */     this.field_8_option_flags = in.readShort();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setEmpty()
/*  63:    */   {
/*  64: 98 */     this.field_2_first_col = 0;
/*  65: 99 */     this.field_3_last_col = 0;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isEmpty()
/*  69:    */   {
/*  70:102 */     return (this.field_2_first_col | this.field_3_last_col) == 0;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setRowNumber(int row)
/*  74:    */   {
/*  75:110 */     this.field_1_row_number = row;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setFirstCol(int col)
/*  79:    */   {
/*  80:118 */     this.field_2_first_col = col;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setLastCol(int col)
/*  84:    */   {
/*  85:125 */     this.field_3_last_col = col;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setHeight(short height)
/*  89:    */   {
/*  90:133 */     this.field_4_height = height;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setOptimize(short optimize)
/*  94:    */   {
/*  95:141 */     this.field_5_optimize = optimize;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setOutlineLevel(short ol)
/*  99:    */   {
/* 100:151 */     this.field_7_option_flags = outlineLevel.setValue(this.field_7_option_flags, ol);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setColapsed(boolean c)
/* 104:    */   {
/* 105:159 */     this.field_7_option_flags = colapsed.setBoolean(this.field_7_option_flags, c);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setZeroHeight(boolean z)
/* 109:    */   {
/* 110:167 */     this.field_7_option_flags = zeroHeight.setBoolean(this.field_7_option_flags, z);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setBadFontHeight(boolean f)
/* 114:    */   {
/* 115:175 */     this.field_7_option_flags = badFontHeight.setBoolean(this.field_7_option_flags, f);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setFormatted(boolean f)
/* 119:    */   {
/* 120:183 */     this.field_7_option_flags = formatted.setBoolean(this.field_7_option_flags, f);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setXFIndex(short index)
/* 124:    */   {
/* 125:194 */     this.field_8_option_flags = xfIndex.setValue(this.field_8_option_flags, index);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setTopBorder(boolean f)
/* 129:    */   {
/* 130:203 */     this.field_8_option_flags = topBorder.setBoolean(this.field_8_option_flags, f);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setBottomBorder(boolean f)
/* 134:    */   {
/* 135:213 */     this.field_8_option_flags = bottomBorder.setBoolean(this.field_8_option_flags, f);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setPhoeneticGuide(boolean f)
/* 139:    */   {
/* 140:222 */     this.field_8_option_flags = phoeneticGuide.setBoolean(this.field_8_option_flags, f);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getRowNumber()
/* 144:    */   {
/* 145:230 */     return this.field_1_row_number;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getFirstCol()
/* 149:    */   {
/* 150:238 */     return this.field_2_first_col;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public int getLastCol()
/* 154:    */   {
/* 155:246 */     return this.field_3_last_col;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public short getHeight()
/* 159:    */   {
/* 160:254 */     return this.field_4_height;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public short getOptimize()
/* 164:    */   {
/* 165:262 */     return this.field_5_optimize;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public short getOptionFlags()
/* 169:    */   {
/* 170:271 */     return (short)this.field_7_option_flags;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public short getOutlineLevel()
/* 174:    */   {
/* 175:282 */     return (short)outlineLevel.getValue(this.field_7_option_flags);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean getColapsed()
/* 179:    */   {
/* 180:291 */     return colapsed.isSet(this.field_7_option_flags);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean getZeroHeight()
/* 184:    */   {
/* 185:300 */     return zeroHeight.isSet(this.field_7_option_flags);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean getBadFontHeight()
/* 189:    */   {
/* 190:309 */     return badFontHeight.isSet(this.field_7_option_flags);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean getFormatted()
/* 194:    */   {
/* 195:318 */     return formatted.isSet(this.field_7_option_flags);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public short getOptionFlags2()
/* 199:    */   {
/* 200:329 */     return (short)this.field_8_option_flags;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public short getXFIndex()
/* 204:    */   {
/* 205:338 */     return xfIndex.getShortValue((short)this.field_8_option_flags);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public boolean getTopBorder()
/* 209:    */   {
/* 210:347 */     return topBorder.isSet(this.field_8_option_flags);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public boolean getBottomBorder()
/* 214:    */   {
/* 215:356 */     return bottomBorder.isSet(this.field_8_option_flags);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public boolean getPhoeneticGuide()
/* 219:    */   {
/* 220:365 */     return phoeneticGuide.isSet(this.field_8_option_flags);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public String toString()
/* 224:    */   {
/* 225:369 */     StringBuffer sb = new StringBuffer();
/* 226:    */     
/* 227:371 */     sb.append("[ROW]\n");
/* 228:372 */     sb.append("    .rownumber      = ").append(Integer.toHexString(getRowNumber())).append("\n");
/* 229:    */     
/* 230:374 */     sb.append("    .firstcol       = ").append(HexDump.shortToHex(getFirstCol())).append("\n");
/* 231:375 */     sb.append("    .lastcol        = ").append(HexDump.shortToHex(getLastCol())).append("\n");
/* 232:376 */     sb.append("    .height         = ").append(HexDump.shortToHex(getHeight())).append("\n");
/* 233:377 */     sb.append("    .optimize       = ").append(HexDump.shortToHex(getOptimize())).append("\n");
/* 234:378 */     sb.append("    .reserved       = ").append(HexDump.shortToHex(this.field_6_reserved)).append("\n");
/* 235:379 */     sb.append("    .optionflags    = ").append(HexDump.shortToHex(getOptionFlags())).append("\n");
/* 236:380 */     sb.append("        .outlinelvl = ").append(Integer.toHexString(getOutlineLevel())).append("\n");
/* 237:381 */     sb.append("        .colapsed   = ").append(getColapsed()).append("\n");
/* 238:382 */     sb.append("        .zeroheight = ").append(getZeroHeight()).append("\n");
/* 239:383 */     sb.append("        .badfontheig= ").append(getBadFontHeight()).append("\n");
/* 240:384 */     sb.append("        .formatted  = ").append(getFormatted()).append("\n");
/* 241:385 */     sb.append("    .optionsflags2  = ").append(HexDump.shortToHex(getOptionFlags2())).append("\n");
/* 242:386 */     sb.append("        .xfindex       = ").append(Integer.toHexString(getXFIndex())).append("\n");
/* 243:387 */     sb.append("        .topBorder     = ").append(getTopBorder()).append("\n");
/* 244:388 */     sb.append("        .bottomBorder  = ").append(getBottomBorder()).append("\n");
/* 245:389 */     sb.append("        .phoeneticGuide= ").append(getPhoeneticGuide()).append("\n");
/* 246:390 */     sb.append("[/ROW]\n");
/* 247:391 */     return sb.toString();
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void serialize(LittleEndianOutput out)
/* 251:    */   {
/* 252:395 */     out.writeShort(getRowNumber());
/* 253:396 */     out.writeShort(getFirstCol() == -1 ? 0 : getFirstCol());
/* 254:397 */     out.writeShort(getLastCol() == -1 ? 0 : getLastCol());
/* 255:398 */     out.writeShort(getHeight());
/* 256:399 */     out.writeShort(getOptimize());
/* 257:400 */     out.writeShort(this.field_6_reserved);
/* 258:401 */     out.writeShort(getOptionFlags());
/* 259:402 */     out.writeShort(getOptionFlags2());
/* 260:    */   }
/* 261:    */   
/* 262:    */   protected int getDataSize()
/* 263:    */   {
/* 264:406 */     return 16;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public short getSid()
/* 268:    */   {
/* 269:410 */     return 520;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public Object clone()
/* 273:    */   {
/* 274:414 */     RowRecord rec = new RowRecord(this.field_1_row_number);
/* 275:415 */     rec.field_2_first_col = this.field_2_first_col;
/* 276:416 */     rec.field_3_last_col = this.field_3_last_col;
/* 277:417 */     rec.field_4_height = this.field_4_height;
/* 278:418 */     rec.field_5_optimize = this.field_5_optimize;
/* 279:419 */     rec.field_6_reserved = this.field_6_reserved;
/* 280:420 */     rec.field_7_option_flags = this.field_7_option_flags;
/* 281:421 */     rec.field_8_option_flags = this.field_8_option_flags;
/* 282:422 */     return rec;
/* 283:    */   }
/* 284:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RowRecord
 * JD-Core Version:    0.7.0.1
 */