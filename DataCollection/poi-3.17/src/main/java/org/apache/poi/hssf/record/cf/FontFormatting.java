/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.LittleEndian;
/*   8:    */ 
/*   9:    */ public final class FontFormatting
/*  10:    */   implements Cloneable
/*  11:    */ {
/*  12: 32 */   private final byte[] _rawData = new byte[118];
/*  13:    */   private static final int OFFSET_FONT_NAME = 0;
/*  14:    */   private static final int OFFSET_FONT_HEIGHT = 64;
/*  15:    */   private static final int OFFSET_FONT_OPTIONS = 68;
/*  16:    */   private static final int OFFSET_FONT_WEIGHT = 72;
/*  17:    */   private static final int OFFSET_ESCAPEMENT_TYPE = 74;
/*  18:    */   private static final int OFFSET_UNDERLINE_TYPE = 76;
/*  19:    */   private static final int OFFSET_FONT_COLOR_INDEX = 80;
/*  20:    */   private static final int OFFSET_OPTION_FLAGS = 88;
/*  21:    */   private static final int OFFSET_ESCAPEMENT_TYPE_MODIFIED = 92;
/*  22:    */   private static final int OFFSET_UNDERLINE_TYPE_MODIFIED = 96;
/*  23:    */   private static final int OFFSET_FONT_WEIGHT_MODIFIED = 100;
/*  24:    */   private static final int OFFSET_NOT_USED1 = 104;
/*  25:    */   private static final int OFFSET_NOT_USED2 = 108;
/*  26:    */   private static final int OFFSET_NOT_USED3 = 112;
/*  27:    */   private static final int OFFSET_FONT_FORMATING_END = 116;
/*  28:    */   private static final int RAW_DATA_SIZE = 118;
/*  29:    */   public static final int FONT_CELL_HEIGHT_PRESERVED = -1;
/*  30: 55 */   private static final BitField posture = BitFieldFactory.getInstance(2);
/*  31: 56 */   private static final BitField outline = BitFieldFactory.getInstance(8);
/*  32: 57 */   private static final BitField shadow = BitFieldFactory.getInstance(16);
/*  33: 58 */   private static final BitField cancellation = BitFieldFactory.getInstance(128);
/*  34: 62 */   private static final BitField styleModified = BitFieldFactory.getInstance(2);
/*  35: 63 */   private static final BitField outlineModified = BitFieldFactory.getInstance(8);
/*  36: 64 */   private static final BitField shadowModified = BitFieldFactory.getInstance(16);
/*  37: 65 */   private static final BitField cancellationModified = BitFieldFactory.getInstance(128);
/*  38:    */   public static final short SS_NONE = 0;
/*  39:    */   public static final short SS_SUPER = 1;
/*  40:    */   public static final short SS_SUB = 2;
/*  41:    */   public static final byte U_NONE = 0;
/*  42:    */   public static final byte U_SINGLE = 1;
/*  43:    */   public static final byte U_DOUBLE = 2;
/*  44:    */   public static final byte U_SINGLE_ACCOUNTING = 33;
/*  45:    */   public static final byte U_DOUBLE_ACCOUNTING = 34;
/*  46:    */   private static final short FONT_WEIGHT_NORMAL = 400;
/*  47:    */   private static final short FONT_WEIGHT_BOLD = 700;
/*  48:    */   
/*  49:    */   public FontFormatting()
/*  50:    */   {
/*  51: 92 */     setFontHeight(-1);
/*  52: 93 */     setItalic(false);
/*  53: 94 */     setFontWieghtModified(false);
/*  54: 95 */     setOutline(false);
/*  55: 96 */     setShadow(false);
/*  56: 97 */     setStrikeout(false);
/*  57: 98 */     setEscapementType((short)0);
/*  58: 99 */     setUnderlineType((short)0);
/*  59:100 */     setFontColorIndex((short)-1);
/*  60:    */     
/*  61:102 */     setFontStyleModified(false);
/*  62:103 */     setFontOutlineModified(false);
/*  63:104 */     setFontShadowModified(false);
/*  64:105 */     setFontCancellationModified(false);
/*  65:    */     
/*  66:107 */     setEscapementTypeModified(false);
/*  67:108 */     setUnderlineTypeModified(false);
/*  68:    */     
/*  69:110 */     setShort(0, 0);
/*  70:111 */     setInt(104, 1);
/*  71:112 */     setInt(108, 0);
/*  72:113 */     setInt(112, 2147483647);
/*  73:114 */     setShort(116, 1);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public FontFormatting(RecordInputStream in)
/*  77:    */   {
/*  78:119 */     for (int i = 0; i < this._rawData.length; i++) {
/*  79:120 */       this._rawData[i] = in.readByte();
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   private short getShort(int offset)
/*  84:    */   {
/*  85:125 */     return LittleEndian.getShort(this._rawData, offset);
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void setShort(int offset, int value)
/*  89:    */   {
/*  90:128 */     LittleEndian.putShort(this._rawData, offset, (short)value);
/*  91:    */   }
/*  92:    */   
/*  93:    */   private int getInt(int offset)
/*  94:    */   {
/*  95:131 */     return LittleEndian.getInt(this._rawData, offset);
/*  96:    */   }
/*  97:    */   
/*  98:    */   private void setInt(int offset, int value)
/*  99:    */   {
/* 100:134 */     LittleEndian.putInt(this._rawData, offset, value);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public byte[] getRawRecord()
/* 104:    */   {
/* 105:139 */     return this._rawData;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int getDataLength()
/* 109:    */   {
/* 110:143 */     return 118;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setFontHeight(int height)
/* 114:    */   {
/* 115:155 */     setInt(64, height);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int getFontHeight()
/* 119:    */   {
/* 120:165 */     return getInt(64);
/* 121:    */   }
/* 122:    */   
/* 123:    */   private void setFontOption(boolean option, BitField field)
/* 124:    */   {
/* 125:170 */     int options = getInt(68);
/* 126:171 */     options = field.setBoolean(options, option);
/* 127:172 */     setInt(68, options);
/* 128:    */   }
/* 129:    */   
/* 130:    */   private boolean getFontOption(BitField field)
/* 131:    */   {
/* 132:177 */     int options = getInt(68);
/* 133:178 */     return field.isSet(options);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setItalic(boolean italic)
/* 137:    */   {
/* 138:190 */     setFontOption(italic, posture);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public boolean isItalic()
/* 142:    */   {
/* 143:202 */     return getFontOption(posture);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setOutline(boolean on)
/* 147:    */   {
/* 148:207 */     setFontOption(on, outline);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean isOutlineOn()
/* 152:    */   {
/* 153:212 */     return getFontOption(outline);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void setShadow(boolean on)
/* 157:    */   {
/* 158:217 */     setFontOption(on, shadow);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public boolean isShadowOn()
/* 162:    */   {
/* 163:222 */     return getFontOption(shadow);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setStrikeout(boolean strike)
/* 167:    */   {
/* 168:232 */     setFontOption(strike, cancellation);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public boolean isStruckout()
/* 172:    */   {
/* 173:243 */     return getFontOption(cancellation);
/* 174:    */   }
/* 175:    */   
/* 176:    */   private void setFontWeight(short pbw)
/* 177:    */   {
/* 178:255 */     short bw = pbw;
/* 179:256 */     if (bw < 100) {
/* 180:256 */       bw = 100;
/* 181:    */     }
/* 182:257 */     if (bw > 1000) {
/* 183:257 */       bw = 1000;
/* 184:    */     }
/* 185:258 */     setShort(72, bw);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setBold(boolean bold)
/* 189:    */   {
/* 190:268 */     setFontWeight((short)(bold ? 700 : 400));
/* 191:    */   }
/* 192:    */   
/* 193:    */   public short getFontWeight()
/* 194:    */   {
/* 195:280 */     return getShort(72);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean isBold()
/* 199:    */   {
/* 200:291 */     return getFontWeight() == 700;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public short getEscapementType()
/* 204:    */   {
/* 205:304 */     return getShort(74);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setEscapementType(short escapementType)
/* 209:    */   {
/* 210:317 */     setShort(74, escapementType);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public short getUnderlineType()
/* 214:    */   {
/* 215:333 */     return getShort(76);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setUnderlineType(short underlineType)
/* 219:    */   {
/* 220:349 */     setShort(76, underlineType);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public short getFontColorIndex()
/* 224:    */   {
/* 225:355 */     return (short)getInt(80);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setFontColorIndex(short fci)
/* 229:    */   {
/* 230:360 */     setInt(80, fci);
/* 231:    */   }
/* 232:    */   
/* 233:    */   private boolean getOptionFlag(BitField field)
/* 234:    */   {
/* 235:364 */     int optionFlags = getInt(88);
/* 236:365 */     int value = field.getValue(optionFlags);
/* 237:366 */     return value == 0;
/* 238:    */   }
/* 239:    */   
/* 240:    */   private void setOptionFlag(boolean modified, BitField field)
/* 241:    */   {
/* 242:371 */     int value = modified ? 0 : 1;
/* 243:372 */     int optionFlags = getInt(88);
/* 244:373 */     optionFlags = field.setValue(optionFlags, value);
/* 245:374 */     setInt(88, optionFlags);
/* 246:    */   }
/* 247:    */   
/* 248:    */   public boolean isFontStyleModified()
/* 249:    */   {
/* 250:380 */     return getOptionFlag(styleModified);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public void setFontStyleModified(boolean modified)
/* 254:    */   {
/* 255:386 */     setOptionFlag(modified, styleModified);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public boolean isFontOutlineModified()
/* 259:    */   {
/* 260:391 */     return getOptionFlag(outlineModified);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void setFontOutlineModified(boolean modified)
/* 264:    */   {
/* 265:396 */     setOptionFlag(modified, outlineModified);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public boolean isFontShadowModified()
/* 269:    */   {
/* 270:401 */     return getOptionFlag(shadowModified);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void setFontShadowModified(boolean modified)
/* 274:    */   {
/* 275:406 */     setOptionFlag(modified, shadowModified);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void setFontCancellationModified(boolean modified)
/* 279:    */   {
/* 280:410 */     setOptionFlag(modified, cancellationModified);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public boolean isFontCancellationModified()
/* 284:    */   {
/* 285:415 */     return getOptionFlag(cancellationModified);
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void setEscapementTypeModified(boolean modified)
/* 289:    */   {
/* 290:420 */     int value = modified ? 0 : 1;
/* 291:421 */     setInt(92, value);
/* 292:    */   }
/* 293:    */   
/* 294:    */   public boolean isEscapementTypeModified()
/* 295:    */   {
/* 296:425 */     int escapementModified = getInt(92);
/* 297:426 */     return escapementModified == 0;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void setUnderlineTypeModified(boolean modified)
/* 301:    */   {
/* 302:431 */     int value = modified ? 0 : 1;
/* 303:432 */     setInt(96, value);
/* 304:    */   }
/* 305:    */   
/* 306:    */   public boolean isUnderlineTypeModified()
/* 307:    */   {
/* 308:437 */     int underlineModified = getInt(96);
/* 309:438 */     return underlineModified == 0;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public void setFontWieghtModified(boolean modified)
/* 313:    */   {
/* 314:443 */     int value = modified ? 0 : 1;
/* 315:444 */     setInt(100, value);
/* 316:    */   }
/* 317:    */   
/* 318:    */   public boolean isFontWeightModified()
/* 319:    */   {
/* 320:449 */     int fontStyleModified = getInt(100);
/* 321:450 */     return fontStyleModified == 0;
/* 322:    */   }
/* 323:    */   
/* 324:    */   public String toString()
/* 325:    */   {
/* 326:455 */     StringBuffer buffer = new StringBuffer();
/* 327:456 */     buffer.append("    [Font Formatting]\n");
/* 328:    */     
/* 329:458 */     buffer.append("\t.font height = ").append(getFontHeight()).append(" twips\n");
/* 330:460 */     if (isFontStyleModified()) {
/* 331:462 */       buffer.append("\t.font posture = ").append(isItalic() ? "Italic" : "Normal").append("\n");
/* 332:    */     } else {
/* 333:466 */       buffer.append("\t.font posture = ]not modified]").append("\n");
/* 334:    */     }
/* 335:469 */     if (isFontOutlineModified()) {
/* 336:471 */       buffer.append("\t.font outline = ").append(isOutlineOn()).append("\n");
/* 337:    */     } else {
/* 338:475 */       buffer.append("\t.font outline is not modified\n");
/* 339:    */     }
/* 340:478 */     if (isFontShadowModified()) {
/* 341:480 */       buffer.append("\t.font shadow = ").append(isShadowOn()).append("\n");
/* 342:    */     } else {
/* 343:484 */       buffer.append("\t.font shadow is not modified\n");
/* 344:    */     }
/* 345:487 */     if (isFontCancellationModified()) {
/* 346:489 */       buffer.append("\t.font strikeout = ").append(isStruckout()).append("\n");
/* 347:    */     } else {
/* 348:493 */       buffer.append("\t.font strikeout is not modified\n");
/* 349:    */     }
/* 350:496 */     if (isFontStyleModified()) {
/* 351:498 */       buffer.append("\t.font weight = ").append(getFontWeight()).append("0x" + Integer.toHexString(getFontWeight())).append("\n");
/* 352:    */     } else {
/* 353:508 */       buffer.append("\t.font weight = ]not modified]").append("\n");
/* 354:    */     }
/* 355:511 */     if (isEscapementTypeModified()) {
/* 356:513 */       buffer.append("\t.escapement type = ").append(getEscapementType()).append("\n");
/* 357:    */     } else {
/* 358:517 */       buffer.append("\t.escapement type is not modified\n");
/* 359:    */     }
/* 360:520 */     if (isUnderlineTypeModified()) {
/* 361:522 */       buffer.append("\t.underline type = ").append(getUnderlineType()).append("\n");
/* 362:    */     } else {
/* 363:526 */       buffer.append("\t.underline type is not modified\n");
/* 364:    */     }
/* 365:528 */     buffer.append("\t.color index = ").append("0x" + Integer.toHexString(getFontColorIndex()).toUpperCase(Locale.ROOT)).append("\n");
/* 366:    */     
/* 367:530 */     buffer.append("    [/Font Formatting]\n");
/* 368:531 */     return buffer.toString();
/* 369:    */   }
/* 370:    */   
/* 371:    */   public FontFormatting clone()
/* 372:    */   {
/* 373:536 */     FontFormatting other = new FontFormatting();
/* 374:537 */     System.arraycopy(this._rawData, 0, other._rawData, 0, this._rawData.length);
/* 375:538 */     return other;
/* 376:    */   }
/* 377:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.FontFormatting
 * JD-Core Version:    0.7.0.1
 */