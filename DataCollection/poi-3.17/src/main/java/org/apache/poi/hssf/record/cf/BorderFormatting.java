/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndian;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public final class BorderFormatting
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12:    */   public static final short BORDER_NONE = 0;
/*  13:    */   public static final short BORDER_THIN = 1;
/*  14:    */   public static final short BORDER_MEDIUM = 2;
/*  15:    */   public static final short BORDER_DASHED = 3;
/*  16:    */   public static final short BORDER_HAIR = 4;
/*  17:    */   public static final short BORDER_THICK = 5;
/*  18:    */   public static final short BORDER_DOUBLE = 6;
/*  19:    */   public static final short BORDER_DOTTED = 7;
/*  20:    */   public static final short BORDER_MEDIUM_DASHED = 8;
/*  21:    */   public static final short BORDER_DASH_DOT = 9;
/*  22:    */   public static final short BORDER_MEDIUM_DASH_DOT = 10;
/*  23:    */   public static final short BORDER_DASH_DOT_DOT = 11;
/*  24:    */   public static final short BORDER_MEDIUM_DASH_DOT_DOT = 12;
/*  25:    */   public static final short BORDER_SLANTED_DASH_DOT = 13;
/*  26:    */   private int field_13_border_styles1;
/*  27: 62 */   private static final BitField bordLeftLineStyle = BitFieldFactory.getInstance(15);
/*  28: 63 */   private static final BitField bordRightLineStyle = BitFieldFactory.getInstance(240);
/*  29: 64 */   private static final BitField bordTopLineStyle = BitFieldFactory.getInstance(3840);
/*  30: 65 */   private static final BitField bordBottomLineStyle = BitFieldFactory.getInstance(61440);
/*  31: 66 */   private static final BitField bordLeftLineColor = BitFieldFactory.getInstance(8323072);
/*  32: 67 */   private static final BitField bordRightLineColor = BitFieldFactory.getInstance(1065353216);
/*  33: 68 */   private static final BitField bordTlBrLineOnOff = BitFieldFactory.getInstance(1073741824);
/*  34: 69 */   private static final BitField bordBlTrtLineOnOff = BitFieldFactory.getInstance(-2147483648);
/*  35:    */   private int field_14_border_styles2;
/*  36: 72 */   private static final BitField bordTopLineColor = BitFieldFactory.getInstance(127);
/*  37: 73 */   private static final BitField bordBottomLineColor = BitFieldFactory.getInstance(16256);
/*  38: 74 */   private static final BitField bordDiagLineColor = BitFieldFactory.getInstance(2080768);
/*  39: 75 */   private static final BitField bordDiagLineStyle = BitFieldFactory.getInstance(31457280);
/*  40:    */   
/*  41:    */   public BorderFormatting()
/*  42:    */   {
/*  43: 79 */     this.field_13_border_styles1 = 0;
/*  44: 80 */     this.field_14_border_styles2 = 0;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public BorderFormatting(LittleEndianInput in)
/*  48:    */   {
/*  49: 85 */     this.field_13_border_styles1 = in.readInt();
/*  50: 86 */     this.field_14_border_styles2 = in.readInt();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int getDataLength()
/*  54:    */   {
/*  55: 90 */     return 8;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setBorderLeft(int border)
/*  59:    */   {
/*  60:112 */     this.field_13_border_styles1 = bordLeftLineStyle.setValue(this.field_13_border_styles1, border);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getBorderLeft()
/*  64:    */   {
/*  65:134 */     return bordLeftLineStyle.getValue(this.field_13_border_styles1);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setBorderRight(int border)
/*  69:    */   {
/*  70:156 */     this.field_13_border_styles1 = bordRightLineStyle.setValue(this.field_13_border_styles1, border);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getBorderRight()
/*  74:    */   {
/*  75:178 */     return bordRightLineStyle.getValue(this.field_13_border_styles1);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setBorderTop(int border)
/*  79:    */   {
/*  80:200 */     this.field_13_border_styles1 = bordTopLineStyle.setValue(this.field_13_border_styles1, border);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public int getBorderTop()
/*  84:    */   {
/*  85:222 */     return bordTopLineStyle.getValue(this.field_13_border_styles1);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setBorderBottom(int border)
/*  89:    */   {
/*  90:244 */     this.field_13_border_styles1 = bordBottomLineStyle.setValue(this.field_13_border_styles1, border);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int getBorderBottom()
/*  94:    */   {
/*  95:266 */     return bordBottomLineStyle.getValue(this.field_13_border_styles1);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setBorderDiagonal(int border)
/*  99:    */   {
/* 100:288 */     this.field_14_border_styles2 = bordDiagLineStyle.setValue(this.field_14_border_styles2, border);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int getBorderDiagonal()
/* 104:    */   {
/* 105:310 */     return bordDiagLineStyle.getValue(this.field_14_border_styles2);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setLeftBorderColor(int color)
/* 109:    */   {
/* 110:318 */     this.field_13_border_styles1 = bordLeftLineColor.setValue(this.field_13_border_styles1, color);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int getLeftBorderColor()
/* 114:    */   {
/* 115:327 */     return bordLeftLineColor.getValue(this.field_13_border_styles1);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setRightBorderColor(int color)
/* 119:    */   {
/* 120:335 */     this.field_13_border_styles1 = bordRightLineColor.setValue(this.field_13_border_styles1, color);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int getRightBorderColor()
/* 124:    */   {
/* 125:344 */     return bordRightLineColor.getValue(this.field_13_border_styles1);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setTopBorderColor(int color)
/* 129:    */   {
/* 130:352 */     this.field_14_border_styles2 = bordTopLineColor.setValue(this.field_14_border_styles2, color);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int getTopBorderColor()
/* 134:    */   {
/* 135:361 */     return bordTopLineColor.getValue(this.field_14_border_styles2);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setBottomBorderColor(int color)
/* 139:    */   {
/* 140:370 */     this.field_14_border_styles2 = bordBottomLineColor.setValue(this.field_14_border_styles2, color);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public int getBottomBorderColor()
/* 144:    */   {
/* 145:379 */     return bordBottomLineColor.getValue(this.field_14_border_styles2);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setDiagonalBorderColor(int color)
/* 149:    */   {
/* 150:387 */     this.field_14_border_styles2 = bordDiagLineColor.setValue(this.field_14_border_styles2, color);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public int getDiagonalBorderColor()
/* 154:    */   {
/* 155:396 */     return bordDiagLineColor.getValue(this.field_14_border_styles2);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setForwardDiagonalOn(boolean on)
/* 159:    */   {
/* 160:405 */     this.field_13_border_styles1 = bordBlTrtLineOnOff.setBoolean(this.field_13_border_styles1, on);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setBackwardDiagonalOn(boolean on)
/* 164:    */   {
/* 165:414 */     this.field_13_border_styles1 = bordTlBrLineOnOff.setBoolean(this.field_13_border_styles1, on);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public boolean isForwardDiagonalOn()
/* 169:    */   {
/* 170:421 */     return bordBlTrtLineOnOff.isSet(this.field_13_border_styles1);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public boolean isBackwardDiagonalOn()
/* 174:    */   {
/* 175:428 */     return bordTlBrLineOnOff.isSet(this.field_13_border_styles1);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public String toString()
/* 179:    */   {
/* 180:433 */     StringBuffer buffer = new StringBuffer();
/* 181:434 */     buffer.append("    [Border Formatting]\n");
/* 182:435 */     buffer.append("          .lftln     = ").append(Integer.toHexString(getBorderLeft())).append("\n");
/* 183:436 */     buffer.append("          .rgtln     = ").append(Integer.toHexString(getBorderRight())).append("\n");
/* 184:437 */     buffer.append("          .topln     = ").append(Integer.toHexString(getBorderTop())).append("\n");
/* 185:438 */     buffer.append("          .btmln     = ").append(Integer.toHexString(getBorderBottom())).append("\n");
/* 186:439 */     buffer.append("          .leftborder= ").append(Integer.toHexString(getLeftBorderColor())).append("\n");
/* 187:440 */     buffer.append("          .rghtborder= ").append(Integer.toHexString(getRightBorderColor())).append("\n");
/* 188:441 */     buffer.append("          .topborder= ").append(Integer.toHexString(getTopBorderColor())).append("\n");
/* 189:442 */     buffer.append("          .bottomborder= ").append(Integer.toHexString(getBottomBorderColor())).append("\n");
/* 190:443 */     buffer.append("          .fwdiag= ").append(isForwardDiagonalOn()).append("\n");
/* 191:444 */     buffer.append("          .bwdiag= ").append(isBackwardDiagonalOn()).append("\n");
/* 192:445 */     buffer.append("    [/Border Formatting]\n");
/* 193:446 */     return buffer.toString();
/* 194:    */   }
/* 195:    */   
/* 196:    */   public BorderFormatting clone()
/* 197:    */   {
/* 198:451 */     BorderFormatting rec = new BorderFormatting();
/* 199:452 */     rec.field_13_border_styles1 = this.field_13_border_styles1;
/* 200:453 */     rec.field_14_border_styles2 = this.field_14_border_styles2;
/* 201:454 */     return rec;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public int serialize(int offset, byte[] data)
/* 205:    */   {
/* 206:458 */     LittleEndian.putInt(data, offset + 0, this.field_13_border_styles1);
/* 207:459 */     LittleEndian.putInt(data, offset + 4, this.field_14_border_styles2);
/* 208:460 */     return 8;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void serialize(LittleEndianOutput out)
/* 212:    */   {
/* 213:463 */     out.writeInt(this.field_13_border_styles1);
/* 214:464 */     out.writeInt(this.field_14_border_styles2);
/* 215:    */   }
/* 216:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.BorderFormatting
 * JD-Core Version:    0.7.0.1
 */