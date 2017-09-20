/*   1:    */ package org.apache.poi.hssf.record.common;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianInput;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class ExtendedColor
/*   8:    */   implements Cloneable
/*   9:    */ {
/*  10:    */   public static final int TYPE_AUTO = 0;
/*  11:    */   public static final int TYPE_INDEXED = 1;
/*  12:    */   public static final int TYPE_RGB = 2;
/*  13:    */   public static final int TYPE_THEMED = 3;
/*  14:    */   public static final int TYPE_UNSET = 4;
/*  15:    */   public static final int THEME_DARK_1 = 0;
/*  16:    */   public static final int THEME_LIGHT_1 = 1;
/*  17:    */   public static final int THEME_DARK_2 = 2;
/*  18:    */   public static final int THEME_LIGHT_2 = 3;
/*  19:    */   public static final int THEME_ACCENT_1 = 4;
/*  20:    */   public static final int THEME_ACCENT_2 = 5;
/*  21:    */   public static final int THEME_ACCENT_3 = 6;
/*  22:    */   public static final int THEME_ACCENT_4 = 7;
/*  23:    */   public static final int THEME_ACCENT_5 = 8;
/*  24:    */   public static final int THEME_ACCENT_6 = 9;
/*  25:    */   public static final int THEME_HYPERLINK = 10;
/*  26:    */   public static final int THEME_FOLLOWED_HYPERLINK = 11;
/*  27:    */   private int type;
/*  28:    */   private int colorIndex;
/*  29:    */   private byte[] rgba;
/*  30:    */   private int themeIndex;
/*  31:    */   private double tint;
/*  32:    */   
/*  33:    */   public ExtendedColor()
/*  34:    */   {
/*  35: 66 */     this.type = 1;
/*  36: 67 */     this.colorIndex = 0;
/*  37: 68 */     this.tint = 0.0D;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ExtendedColor(LittleEndianInput in)
/*  41:    */   {
/*  42: 71 */     this.type = in.readInt();
/*  43: 72 */     if (this.type == 1)
/*  44:    */     {
/*  45: 73 */       this.colorIndex = in.readInt();
/*  46:    */     }
/*  47: 74 */     else if (this.type == 2)
/*  48:    */     {
/*  49: 75 */       this.rgba = new byte[4];
/*  50: 76 */       in.readFully(this.rgba);
/*  51:    */     }
/*  52: 77 */     else if (this.type == 3)
/*  53:    */     {
/*  54: 78 */       this.themeIndex = in.readInt();
/*  55:    */     }
/*  56:    */     else
/*  57:    */     {
/*  58: 81 */       in.readInt();
/*  59:    */     }
/*  60: 83 */     this.tint = in.readDouble();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getType()
/*  64:    */   {
/*  65: 87 */     return this.type;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setType(int type)
/*  69:    */   {
/*  70: 90 */     this.type = type;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getColorIndex()
/*  74:    */   {
/*  75: 97 */     return this.colorIndex;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setColorIndex(int colorIndex)
/*  79:    */   {
/*  80:100 */     this.colorIndex = colorIndex;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public byte[] getRGBA()
/*  84:    */   {
/*  85:107 */     return this.rgba;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setRGBA(byte[] rgba)
/*  89:    */   {
/*  90:110 */     this.rgba = (rgba == null ? null : (byte[])rgba.clone());
/*  91:    */   }
/*  92:    */   
/*  93:    */   public int getThemeIndex()
/*  94:    */   {
/*  95:117 */     return this.themeIndex;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setThemeIndex(int themeIndex)
/*  99:    */   {
/* 100:120 */     this.themeIndex = themeIndex;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public double getTint()
/* 104:    */   {
/* 105:126 */     return this.tint;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setTint(double tint)
/* 109:    */   {
/* 110:132 */     if ((tint < -1.0D) || (tint > 1.0D)) {
/* 111:133 */       throw new IllegalArgumentException("Tint/Shade must be between -1 and +1");
/* 112:    */     }
/* 113:135 */     this.tint = tint;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public String toString()
/* 117:    */   {
/* 118:139 */     StringBuffer buffer = new StringBuffer();
/* 119:140 */     buffer.append("    [Extended Color]\n");
/* 120:141 */     buffer.append("          .type  = ").append(this.type).append("\n");
/* 121:142 */     buffer.append("          .tint  = ").append(this.tint).append("\n");
/* 122:143 */     buffer.append("          .c_idx = ").append(this.colorIndex).append("\n");
/* 123:144 */     buffer.append("          .rgba  = ").append(HexDump.toHex(this.rgba)).append("\n");
/* 124:145 */     buffer.append("          .t_idx = ").append(this.themeIndex).append("\n");
/* 125:146 */     buffer.append("    [/Extended Color]\n");
/* 126:147 */     return buffer.toString();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public ExtendedColor clone()
/* 130:    */   {
/* 131:152 */     ExtendedColor exc = new ExtendedColor();
/* 132:153 */     exc.type = this.type;
/* 133:154 */     exc.tint = this.tint;
/* 134:155 */     if (this.type == 1)
/* 135:    */     {
/* 136:156 */       exc.colorIndex = this.colorIndex;
/* 137:    */     }
/* 138:157 */     else if (this.type == 2)
/* 139:    */     {
/* 140:158 */       exc.rgba = new byte[4];
/* 141:159 */       System.arraycopy(this.rgba, 0, exc.rgba, 0, 4);
/* 142:    */     }
/* 143:160 */     else if (this.type == 3)
/* 144:    */     {
/* 145:161 */       exc.themeIndex = this.themeIndex;
/* 146:    */     }
/* 147:163 */     return exc;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public int getDataLength()
/* 151:    */   {
/* 152:167 */     return 16;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void serialize(LittleEndianOutput out)
/* 156:    */   {
/* 157:171 */     out.writeInt(this.type);
/* 158:172 */     if (this.type == 1) {
/* 159:173 */       out.writeInt(this.colorIndex);
/* 160:174 */     } else if (this.type == 2) {
/* 161:175 */       out.write(this.rgba);
/* 162:176 */     } else if (this.type == 3) {
/* 163:177 */       out.writeInt(this.themeIndex);
/* 164:    */     } else {
/* 165:179 */       out.writeInt(0);
/* 166:    */     }
/* 167:181 */     out.writeDouble(this.tint);
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.ExtendedColor
 * JD-Core Version:    0.7.0.1
 */