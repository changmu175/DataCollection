/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.FontRecord;
/*   4:    */ import org.apache.poi.hssf.util.HSSFColor;
/*   5:    */ import org.apache.poi.ss.usermodel.Font;
/*   6:    */ 
/*   7:    */ public final class HSSFFont
/*   8:    */   implements Font
/*   9:    */ {
/*  10:    */   static final short BOLDWEIGHT_NORMAL = 400;
/*  11:    */   static final short BOLDWEIGHT_BOLD = 700;
/*  12:    */   public static final String FONT_ARIAL = "Arial";
/*  13:    */   private FontRecord font;
/*  14:    */   private short index;
/*  15:    */   
/*  16:    */   protected HSSFFont(short index, FontRecord rec)
/*  17:    */   {
/*  18: 56 */     this.font = rec;
/*  19: 57 */     this.index = index;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setFontName(String name)
/*  23:    */   {
/*  24: 68 */     this.font.setFontName(name);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String getFontName()
/*  28:    */   {
/*  29: 79 */     return this.font.getFontName();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public short getIndex()
/*  33:    */   {
/*  34: 90 */     return this.index;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setFontHeight(short height)
/*  38:    */   {
/*  39:102 */     this.font.setFontHeight(height);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setFontHeightInPoints(short height)
/*  43:    */   {
/*  44:113 */     this.font.setFontHeight((short)(height * 20));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public short getFontHeight()
/*  48:    */   {
/*  49:125 */     return this.font.getFontHeight();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public short getFontHeightInPoints()
/*  53:    */   {
/*  54:136 */     return (short)(this.font.getFontHeight() / 20);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setItalic(boolean italic)
/*  58:    */   {
/*  59:146 */     this.font.setItalic(italic);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean getItalic()
/*  63:    */   {
/*  64:156 */     return this.font.isItalic();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setStrikeout(boolean strikeout)
/*  68:    */   {
/*  69:166 */     this.font.setStrikeout(strikeout);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean getStrikeout()
/*  73:    */   {
/*  74:176 */     return this.font.isStruckout();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setColor(short color)
/*  78:    */   {
/*  79:188 */     this.font.setColorPaletteIndex(color);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public short getColor()
/*  83:    */   {
/*  84:200 */     return this.font.getColorPaletteIndex();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public HSSFColor getHSSFColor(HSSFWorkbook wb)
/*  88:    */   {
/*  89:208 */     HSSFPalette pallette = wb.getCustomPalette();
/*  90:209 */     return pallette.getColor(getColor());
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setBold(boolean bold)
/*  94:    */   {
/*  95:217 */     if (bold) {
/*  96:218 */       this.font.setBoldWeight((short)700);
/*  97:    */     } else {
/*  98:220 */       this.font.setBoldWeight((short)400);
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean getBold()
/* 103:    */   {
/* 104:228 */     return this.font.getBoldWeight() == 700;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setTypeOffset(short offset)
/* 108:    */   {
/* 109:241 */     this.font.setSuperSubScript(offset);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public short getTypeOffset()
/* 113:    */   {
/* 114:254 */     return this.font.getSuperSubScript();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setUnderline(byte underline)
/* 118:    */   {
/* 119:269 */     this.font.setUnderline(underline);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public byte getUnderline()
/* 123:    */   {
/* 124:284 */     return this.font.getUnderline();
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int getCharSet()
/* 128:    */   {
/* 129:297 */     byte charset = this.font.getCharset();
/* 130:298 */     if (charset >= 0) {
/* 131:299 */       return charset;
/* 132:    */     }
/* 133:301 */     return charset + 256;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setCharSet(int charset)
/* 137:    */   {
/* 138:313 */     byte cs = (byte)charset;
/* 139:314 */     if (charset > 127) {
/* 140:315 */       cs = (byte)(charset - 256);
/* 141:    */     }
/* 142:317 */     setCharSet(cs);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setCharSet(byte charset)
/* 146:    */   {
/* 147:328 */     this.font.setCharset(charset);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public String toString()
/* 151:    */   {
/* 152:333 */     return "org.apache.poi.hssf.usermodel.HSSFFont{" + this.font + "}";
/* 153:    */   }
/* 154:    */   
/* 155:    */   public int hashCode()
/* 156:    */   {
/* 157:339 */     int prime = 31;
/* 158:340 */     int result = 1;
/* 159:341 */     result = 31 * result + (this.font == null ? 0 : this.font.hashCode());
/* 160:342 */     result = 31 * result + this.index;
/* 161:343 */     return result;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean equals(Object obj)
/* 165:    */   {
/* 166:347 */     if (this == obj) {
/* 167:347 */       return true;
/* 168:    */     }
/* 169:348 */     if (obj == null) {
/* 170:348 */       return false;
/* 171:    */     }
/* 172:349 */     if ((obj instanceof HSSFFont))
/* 173:    */     {
/* 174:350 */       HSSFFont other = (HSSFFont)obj;
/* 175:351 */       if (this.font == null)
/* 176:    */       {
/* 177:352 */         if (other.font != null) {
/* 178:353 */           return false;
/* 179:    */         }
/* 180:    */       }
/* 181:354 */       else if (!this.font.equals(other.font)) {
/* 182:355 */         return false;
/* 183:    */       }
/* 184:356 */       if (this.index != other.index) {
/* 185:357 */         return false;
/* 186:    */       }
/* 187:358 */       return true;
/* 188:    */     }
/* 189:360 */     return false;
/* 190:    */   }
/* 191:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFFont
 * JD-Core Version:    0.7.0.1
 */