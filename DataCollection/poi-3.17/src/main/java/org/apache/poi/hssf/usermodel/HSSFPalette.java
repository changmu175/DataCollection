/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.hssf.record.PaletteRecord;
/*   5:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   6:    */ import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
/*   7:    */ 
/*   8:    */ public final class HSSFPalette
/*   9:    */ {
/*  10:    */   private PaletteRecord _palette;
/*  11:    */   
/*  12:    */   protected HSSFPalette(PaletteRecord palette)
/*  13:    */   {
/*  14: 37 */     this._palette = palette;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public HSSFColor getColor(short index)
/*  18:    */   {
/*  19: 49 */     if (index == HSSFColorPredefined.AUTOMATIC.getIndex()) {
/*  20: 50 */       return HSSFColorPredefined.AUTOMATIC.getColor();
/*  21:    */     }
/*  22: 52 */     byte[] b = this._palette.getColor(index);
/*  23: 53 */     return b == null ? null : new CustomColor(index, b);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public HSSFColor getColor(int index)
/*  27:    */   {
/*  28: 63 */     return getColor((short)index);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public HSSFColor findColor(byte red, byte green, byte blue)
/*  32:    */   {
/*  33: 76 */     byte[] b = this._palette.getColor(8);
/*  34: 77 */     for (short i = 8; b != null; b = this._palette.getColor(i))
/*  35:    */     {
/*  36: 80 */       if ((b[0] == red) && (b[1] == green) && (b[2] == blue)) {
/*  37: 82 */         return new CustomColor(i, b);
/*  38:    */       }
/*  39: 78 */       i = (short)(i + 1);
/*  40:    */     }
/*  41: 85 */     return null;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public HSSFColor findSimilarColor(byte red, byte green, byte blue)
/*  45:    */   {
/*  46:100 */     return findSimilarColor(unsignedInt(red), unsignedInt(green), unsignedInt(blue));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public HSSFColor findSimilarColor(int red, int green, int blue)
/*  50:    */   {
/*  51:114 */     HSSFColor result = null;
/*  52:115 */     int minColorDistance = 2147483647;
/*  53:116 */     byte[] b = this._palette.getColor(8);
/*  54:117 */     for (short i = 8; b != null; b = this._palette.getColor(i))
/*  55:    */     {
/*  56:120 */       int colorDistance = Math.abs(red - unsignedInt(b[0])) + Math.abs(green - unsignedInt(b[1])) + Math.abs(blue - unsignedInt(b[2]));
/*  57:123 */       if (colorDistance < minColorDistance)
/*  58:    */       {
/*  59:125 */         minColorDistance = colorDistance;
/*  60:126 */         result = getColor(i);
/*  61:    */       }
/*  62:118 */       i = (short)(i + 1);
/*  63:    */     }
/*  64:129 */     return result;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private int unsignedInt(byte b)
/*  68:    */   {
/*  69:137 */     return 0xFF & b;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setColorAtIndex(short index, byte red, byte green, byte blue)
/*  73:    */   {
/*  74:150 */     this._palette.setColor(index, red, green, blue);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public HSSFColor addColor(byte red, byte green, byte blue)
/*  78:    */   {
/*  79:165 */     byte[] b = this._palette.getColor(8);
/*  80:167 */     for (short i = 8; i < 64; b = this._palette.getColor(i))
/*  81:    */     {
/*  82:169 */       if (b == null)
/*  83:    */       {
/*  84:171 */         setColorAtIndex(i, red, green, blue);
/*  85:172 */         return getColor(i);
/*  86:    */       }
/*  87:167 */       i = (short)(i + 1);
/*  88:    */     }
/*  89:175 */     throw new RuntimeException("Could not find free color index");
/*  90:    */   }
/*  91:    */   
/*  92:    */   private static final class CustomColor
/*  93:    */     extends HSSFColor
/*  94:    */   {
/*  95:    */     private short _byteOffset;
/*  96:    */     private byte _red;
/*  97:    */     private byte _green;
/*  98:    */     private byte _blue;
/*  99:    */     
/* 100:    */     public CustomColor(short byteOffset, byte[] colors)
/* 101:    */     {
/* 102:186 */       this(byteOffset, colors[0], colors[1], colors[2]);
/* 103:    */     }
/* 104:    */     
/* 105:    */     private CustomColor(short byteOffset, byte red, byte green, byte blue)
/* 106:    */     {
/* 107:191 */       this._byteOffset = byteOffset;
/* 108:192 */       this._red = red;
/* 109:193 */       this._green = green;
/* 110:194 */       this._blue = blue;
/* 111:    */     }
/* 112:    */     
/* 113:    */     public short getIndex()
/* 114:    */     {
/* 115:200 */       return this._byteOffset;
/* 116:    */     }
/* 117:    */     
/* 118:    */     public short[] getTriplet()
/* 119:    */     {
/* 120:206 */       return new short[] { (short)(this._red & 0xFF), (short)(this._green & 0xFF), (short)(this._blue & 0xFF) };
/* 121:    */     }
/* 122:    */     
/* 123:    */     public String getHexString()
/* 124:    */     {
/* 125:217 */       StringBuffer sb = new StringBuffer();
/* 126:218 */       sb.append(getGnumericPart(this._red));
/* 127:219 */       sb.append(':');
/* 128:220 */       sb.append(getGnumericPart(this._green));
/* 129:221 */       sb.append(':');
/* 130:222 */       sb.append(getGnumericPart(this._blue));
/* 131:223 */       return sb.toString();
/* 132:    */     }
/* 133:    */     
/* 134:    */     private String getGnumericPart(byte color)
/* 135:    */     {
/* 136:    */       String s;
/* 137:    */       String s;
/* 138:229 */       if (color == 0)
/* 139:    */       {
/* 140:231 */         s = "0";
/* 141:    */       }
/* 142:    */       else
/* 143:    */       {
/* 144:235 */         int c = color & 0xFF;
/* 145:236 */         c = c << 8 | c;
/* 146:237 */         s = Integer.toHexString(c).toUpperCase(Locale.ROOT);
/* 147:238 */         while (s.length() < 4) {
/* 148:240 */           s = "0" + s;
/* 149:    */         }
/* 150:    */       }
/* 151:243 */       return s;
/* 152:    */     }
/* 153:    */   }
/* 154:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPalette

 * JD-Core Version:    0.7.0.1

 */