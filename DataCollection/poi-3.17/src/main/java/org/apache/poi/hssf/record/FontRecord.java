/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class FontRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 49;
/*  13:    */   public static final short SS_NONE = 0;
/*  14:    */   public static final short SS_SUPER = 1;
/*  15:    */   public static final short SS_SUB = 2;
/*  16:    */   public static final byte U_NONE = 0;
/*  17:    */   public static final byte U_SINGLE = 1;
/*  18:    */   public static final byte U_DOUBLE = 2;
/*  19:    */   public static final byte U_SINGLE_ACCOUNTING = 33;
/*  20:    */   public static final byte U_DOUBLE_ACCOUNTING = 34;
/*  21:    */   private short field_1_font_height;
/*  22:    */   private short field_2_attributes;
/*  23: 46 */   private static final BitField italic = BitFieldFactory.getInstance(2);
/*  24: 49 */   private static final BitField strikeout = BitFieldFactory.getInstance(8);
/*  25: 50 */   private static final BitField macoutline = BitFieldFactory.getInstance(16);
/*  26: 51 */   private static final BitField macshadow = BitFieldFactory.getInstance(32);
/*  27:    */   private short field_3_color_palette_index;
/*  28:    */   private short field_4_bold_weight;
/*  29:    */   private short field_5_super_sub_script;
/*  30:    */   private byte field_6_underline;
/*  31:    */   private byte field_7_family;
/*  32:    */   private byte field_8_charset;
/*  33: 61 */   private byte field_9_zero = 0;
/*  34:    */   private String field_11_font_name;
/*  35:    */   
/*  36:    */   public FontRecord() {}
/*  37:    */   
/*  38:    */   public FontRecord(RecordInputStream in)
/*  39:    */   {
/*  40: 69 */     this.field_1_font_height = in.readShort();
/*  41: 70 */     this.field_2_attributes = in.readShort();
/*  42: 71 */     this.field_3_color_palette_index = in.readShort();
/*  43: 72 */     this.field_4_bold_weight = in.readShort();
/*  44: 73 */     this.field_5_super_sub_script = in.readShort();
/*  45: 74 */     this.field_6_underline = in.readByte();
/*  46: 75 */     this.field_7_family = in.readByte();
/*  47: 76 */     this.field_8_charset = in.readByte();
/*  48: 77 */     this.field_9_zero = in.readByte();
/*  49: 78 */     int field_10_font_name_len = in.readUByte();
/*  50: 79 */     int unicodeFlags = in.readUByte();
/*  51: 81 */     if (field_10_font_name_len > 0)
/*  52:    */     {
/*  53: 82 */       if (unicodeFlags == 0) {
/*  54: 83 */         this.field_11_font_name = in.readCompressedUnicode(field_10_font_name_len);
/*  55:    */       } else {
/*  56: 85 */         this.field_11_font_name = in.readUnicodeLEString(field_10_font_name_len);
/*  57:    */       }
/*  58:    */     }
/*  59:    */     else {
/*  60: 88 */       this.field_11_font_name = "";
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setFontHeight(short height)
/*  65:    */   {
/*  66: 98 */     this.field_1_font_height = height;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setAttributes(short attributes)
/*  70:    */   {
/*  71:107 */     this.field_2_attributes = attributes;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setItalic(boolean italics)
/*  75:    */   {
/*  76:119 */     this.field_2_attributes = italic.setShortBoolean(this.field_2_attributes, italics);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setStrikeout(boolean strike)
/*  80:    */   {
/*  81:129 */     this.field_2_attributes = strikeout.setShortBoolean(this.field_2_attributes, strike);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setMacoutline(boolean mac)
/*  85:    */   {
/*  86:140 */     this.field_2_attributes = macoutline.setShortBoolean(this.field_2_attributes, mac);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setMacshadow(boolean mac)
/*  90:    */   {
/*  91:151 */     this.field_2_attributes = macshadow.setShortBoolean(this.field_2_attributes, mac);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setColorPaletteIndex(short cpi)
/*  95:    */   {
/*  96:160 */     this.field_3_color_palette_index = cpi;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setBoldWeight(short bw)
/* 100:    */   {
/* 101:170 */     this.field_4_bold_weight = bw;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public void setSuperSubScript(short sss)
/* 105:    */   {
/* 106:182 */     this.field_5_super_sub_script = sss;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setUnderline(byte u)
/* 110:    */   {
/* 111:197 */     this.field_6_underline = u;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setFamily(byte f)
/* 115:    */   {
/* 116:206 */     this.field_7_family = f;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setCharset(byte charset)
/* 120:    */   {
/* 121:215 */     this.field_8_charset = charset;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setFontName(String fn)
/* 125:    */   {
/* 126:225 */     this.field_11_font_name = fn;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public short getFontHeight()
/* 130:    */   {
/* 131:234 */     return this.field_1_font_height;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public short getAttributes()
/* 135:    */   {
/* 136:243 */     return this.field_2_attributes;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean isItalic()
/* 140:    */   {
/* 141:253 */     return italic.isSet(this.field_2_attributes);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean isStruckout()
/* 145:    */   {
/* 146:263 */     return strikeout.isSet(this.field_2_attributes);
/* 147:    */   }
/* 148:    */   
/* 149:    */   public boolean isMacoutlined()
/* 150:    */   {
/* 151:274 */     return macoutline.isSet(this.field_2_attributes);
/* 152:    */   }
/* 153:    */   
/* 154:    */   public boolean isMacshadowed()
/* 155:    */   {
/* 156:285 */     return macshadow.isSet(this.field_2_attributes);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public short getColorPaletteIndex()
/* 160:    */   {
/* 161:294 */     return this.field_3_color_palette_index;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public short getBoldWeight()
/* 165:    */   {
/* 166:304 */     return this.field_4_bold_weight;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public short getSuperSubScript()
/* 170:    */   {
/* 171:316 */     return this.field_5_super_sub_script;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public byte getUnderline()
/* 175:    */   {
/* 176:331 */     return this.field_6_underline;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public byte getFamily()
/* 180:    */   {
/* 181:340 */     return this.field_7_family;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public byte getCharset()
/* 185:    */   {
/* 186:349 */     return this.field_8_charset;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public String getFontName()
/* 190:    */   {
/* 191:358 */     return this.field_11_font_name;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public String toString()
/* 195:    */   {
/* 196:362 */     StringBuffer sb = new StringBuffer();
/* 197:    */     
/* 198:364 */     sb.append("[FONT]\n");
/* 199:365 */     sb.append("    .fontheight    = ").append(HexDump.shortToHex(getFontHeight())).append("\n");
/* 200:366 */     sb.append("    .attributes    = ").append(HexDump.shortToHex(getAttributes())).append("\n");
/* 201:367 */     sb.append("       .italic     = ").append(isItalic()).append("\n");
/* 202:368 */     sb.append("       .strikout   = ").append(isStruckout()).append("\n");
/* 203:369 */     sb.append("       .macoutlined= ").append(isMacoutlined()).append("\n");
/* 204:370 */     sb.append("       .macshadowed= ").append(isMacshadowed()).append("\n");
/* 205:371 */     sb.append("    .colorpalette  = ").append(HexDump.shortToHex(getColorPaletteIndex())).append("\n");
/* 206:372 */     sb.append("    .boldweight    = ").append(HexDump.shortToHex(getBoldWeight())).append("\n");
/* 207:373 */     sb.append("    .supersubscript= ").append(HexDump.shortToHex(getSuperSubScript())).append("\n");
/* 208:374 */     sb.append("    .underline     = ").append(HexDump.byteToHex(getUnderline())).append("\n");
/* 209:375 */     sb.append("    .family        = ").append(HexDump.byteToHex(getFamily())).append("\n");
/* 210:376 */     sb.append("    .charset       = ").append(HexDump.byteToHex(getCharset())).append("\n");
/* 211:377 */     sb.append("    .fontname      = ").append(getFontName()).append("\n");
/* 212:378 */     sb.append("[/FONT]\n");
/* 213:379 */     return sb.toString();
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void serialize(LittleEndianOutput out)
/* 217:    */   {
/* 218:384 */     out.writeShort(getFontHeight());
/* 219:385 */     out.writeShort(getAttributes());
/* 220:386 */     out.writeShort(getColorPaletteIndex());
/* 221:387 */     out.writeShort(getBoldWeight());
/* 222:388 */     out.writeShort(getSuperSubScript());
/* 223:389 */     out.writeByte(getUnderline());
/* 224:390 */     out.writeByte(getFamily());
/* 225:391 */     out.writeByte(getCharset());
/* 226:392 */     out.writeByte(this.field_9_zero);
/* 227:393 */     int fontNameLen = this.field_11_font_name.length();
/* 228:394 */     out.writeByte(fontNameLen);
/* 229:395 */     boolean hasMultibyte = StringUtil.hasMultibyte(this.field_11_font_name);
/* 230:396 */     out.writeByte(hasMultibyte ? 1 : 0);
/* 231:397 */     if (fontNameLen > 0) {
/* 232:398 */       if (hasMultibyte) {
/* 233:399 */         StringUtil.putUnicodeLE(this.field_11_font_name, out);
/* 234:    */       } else {
/* 235:401 */         StringUtil.putCompressedUnicode(this.field_11_font_name, out);
/* 236:    */       }
/* 237:    */     }
/* 238:    */   }
/* 239:    */   
/* 240:    */   protected int getDataSize()
/* 241:    */   {
/* 242:406 */     int size = 16;
/* 243:407 */     int fontNameLen = this.field_11_font_name.length();
/* 244:408 */     if (fontNameLen < 1) {
/* 245:409 */       return size;
/* 246:    */     }
/* 247:412 */     boolean hasMultibyte = StringUtil.hasMultibyte(this.field_11_font_name);
/* 248:413 */     return size + fontNameLen * (hasMultibyte ? 2 : 1);
/* 249:    */   }
/* 250:    */   
/* 251:    */   public short getSid()
/* 252:    */   {
/* 253:417 */     return 49;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void cloneStyleFrom(FontRecord source)
/* 257:    */   {
/* 258:428 */     this.field_1_font_height = source.field_1_font_height;
/* 259:429 */     this.field_2_attributes = source.field_2_attributes;
/* 260:430 */     this.field_3_color_palette_index = source.field_3_color_palette_index;
/* 261:431 */     this.field_4_bold_weight = source.field_4_bold_weight;
/* 262:432 */     this.field_5_super_sub_script = source.field_5_super_sub_script;
/* 263:433 */     this.field_6_underline = source.field_6_underline;
/* 264:434 */     this.field_7_family = source.field_7_family;
/* 265:435 */     this.field_8_charset = source.field_8_charset;
/* 266:436 */     this.field_9_zero = source.field_9_zero;
/* 267:437 */     this.field_11_font_name = source.field_11_font_name;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public int hashCode()
/* 271:    */   {
/* 272:441 */     int prime = 31;
/* 273:442 */     int result = 1;
/* 274:443 */     result = 31 * result + (this.field_11_font_name == null ? 0 : this.field_11_font_name.hashCode());
/* 275:    */     
/* 276:    */ 
/* 277:    */ 
/* 278:447 */     result = 31 * result + this.field_1_font_height;
/* 279:448 */     result = 31 * result + this.field_2_attributes;
/* 280:449 */     result = 31 * result + this.field_3_color_palette_index;
/* 281:450 */     result = 31 * result + this.field_4_bold_weight;
/* 282:451 */     result = 31 * result + this.field_5_super_sub_script;
/* 283:452 */     result = 31 * result + this.field_6_underline;
/* 284:453 */     result = 31 * result + this.field_7_family;
/* 285:454 */     result = 31 * result + this.field_8_charset;
/* 286:455 */     result = 31 * result + this.field_9_zero;
/* 287:456 */     return result;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean sameProperties(FontRecord other)
/* 291:    */   {
/* 292:473 */     return (this.field_1_font_height == other.field_1_font_height) && (this.field_2_attributes == other.field_2_attributes) && (this.field_3_color_palette_index == other.field_3_color_palette_index) && (this.field_4_bold_weight == other.field_4_bold_weight) && (this.field_5_super_sub_script == other.field_5_super_sub_script) && (this.field_6_underline == other.field_6_underline) && (this.field_7_family == other.field_7_family) && (this.field_8_charset == other.field_8_charset) && (this.field_9_zero == other.field_9_zero) && (stringEquals(this.field_11_font_name, other.field_11_font_name));
/* 293:    */   }
/* 294:    */   
/* 295:    */   public boolean equals(Object o)
/* 296:    */   {
/* 297:488 */     return (o instanceof FontRecord) ? sameProperties((FontRecord)o) : false;
/* 298:    */   }
/* 299:    */   
/* 300:    */   private static boolean stringEquals(String s1, String s2)
/* 301:    */   {
/* 302:492 */     return (s1 == s2) || ((s1 != null) && (s1.equals(s2)));
/* 303:    */   }
/* 304:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FontRecord
 * JD-Core Version:    0.7.0.1
 */