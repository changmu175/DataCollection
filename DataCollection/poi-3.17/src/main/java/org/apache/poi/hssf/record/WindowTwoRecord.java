/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class WindowTwoRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 574;
/*  11: 36 */   private static final BitField displayFormulas = BitFieldFactory.getInstance(1);
/*  12: 37 */   private static final BitField displayGridlines = BitFieldFactory.getInstance(2);
/*  13: 38 */   private static final BitField displayRowColHeadings = BitFieldFactory.getInstance(4);
/*  14: 39 */   private static final BitField freezePanes = BitFieldFactory.getInstance(8);
/*  15: 40 */   private static final BitField displayZeros = BitFieldFactory.getInstance(16);
/*  16: 42 */   private static final BitField defaultHeader = BitFieldFactory.getInstance(32);
/*  17: 43 */   private static final BitField arabic = BitFieldFactory.getInstance(64);
/*  18: 44 */   private static final BitField displayGuts = BitFieldFactory.getInstance(128);
/*  19: 45 */   private static final BitField freezePanesNoSplit = BitFieldFactory.getInstance(256);
/*  20: 46 */   private static final BitField selected = BitFieldFactory.getInstance(512);
/*  21: 47 */   private static final BitField active = BitFieldFactory.getInstance(1024);
/*  22: 48 */   private static final BitField savedInPageBreakPreview = BitFieldFactory.getInstance(2048);
/*  23:    */   private short field_1_options;
/*  24:    */   private short field_2_top_row;
/*  25:    */   private short field_3_left_col;
/*  26:    */   private int field_4_header_color;
/*  27:    */   private short field_5_page_break_zoom;
/*  28:    */   private short field_6_normal_zoom;
/*  29:    */   private int field_7_reserved;
/*  30:    */   
/*  31:    */   public WindowTwoRecord() {}
/*  32:    */   
/*  33:    */   public WindowTwoRecord(RecordInputStream in)
/*  34:    */   {
/*  35: 66 */     int size = in.remaining();
/*  36: 67 */     this.field_1_options = in.readShort();
/*  37: 68 */     this.field_2_top_row = in.readShort();
/*  38: 69 */     this.field_3_left_col = in.readShort();
/*  39: 70 */     this.field_4_header_color = in.readInt();
/*  40: 71 */     if (size > 10)
/*  41:    */     {
/*  42: 73 */       this.field_5_page_break_zoom = in.readShort();
/*  43: 74 */       this.field_6_normal_zoom = in.readShort();
/*  44:    */     }
/*  45: 76 */     if (size > 14) {
/*  46: 78 */       this.field_7_reserved = in.readInt();
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setOptions(short options)
/*  51:    */   {
/*  52: 89 */     this.field_1_options = options;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setDisplayFormulas(boolean formulas)
/*  56:    */   {
/*  57:101 */     this.field_1_options = displayFormulas.setShortBoolean(this.field_1_options, formulas);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setDisplayGridlines(boolean gridlines)
/*  61:    */   {
/*  62:111 */     this.field_1_options = displayGridlines.setShortBoolean(this.field_1_options, gridlines);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setDisplayRowColHeadings(boolean headings)
/*  66:    */   {
/*  67:121 */     this.field_1_options = displayRowColHeadings.setShortBoolean(this.field_1_options, headings);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setFreezePanes(boolean freezepanes)
/*  71:    */   {
/*  72:131 */     this.field_1_options = freezePanes.setShortBoolean(this.field_1_options, freezepanes);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setDisplayZeros(boolean zeros)
/*  76:    */   {
/*  77:141 */     this.field_1_options = displayZeros.setShortBoolean(this.field_1_options, zeros);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setDefaultHeader(boolean header)
/*  81:    */   {
/*  82:151 */     this.field_1_options = defaultHeader.setShortBoolean(this.field_1_options, header);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setArabic(boolean isarabic)
/*  86:    */   {
/*  87:161 */     this.field_1_options = arabic.setShortBoolean(this.field_1_options, isarabic);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setDisplayGuts(boolean guts)
/*  91:    */   {
/*  92:171 */     this.field_1_options = displayGuts.setShortBoolean(this.field_1_options, guts);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setFreezePanesNoSplit(boolean freeze)
/*  96:    */   {
/*  97:181 */     this.field_1_options = freezePanesNoSplit.setShortBoolean(this.field_1_options, freeze);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setSelected(boolean sel)
/* 101:    */   {
/* 102:191 */     this.field_1_options = selected.setShortBoolean(this.field_1_options, sel);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setActive(boolean p)
/* 106:    */   {
/* 107:199 */     this.field_1_options = active.setShortBoolean(this.field_1_options, p);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setSavedInPageBreakPreview(boolean p)
/* 111:    */   {
/* 112:209 */     this.field_1_options = savedInPageBreakPreview.setShortBoolean(this.field_1_options, p);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setTopRow(short topRow)
/* 116:    */   {
/* 117:221 */     this.field_2_top_row = topRow;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setLeftCol(short leftCol)
/* 121:    */   {
/* 122:231 */     this.field_3_left_col = leftCol;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setHeaderColor(int color)
/* 126:    */   {
/* 127:241 */     this.field_4_header_color = color;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setPageBreakZoom(short zoom)
/* 131:    */   {
/* 132:251 */     this.field_5_page_break_zoom = zoom;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setNormalZoom(short zoom)
/* 136:    */   {
/* 137:261 */     this.field_6_normal_zoom = zoom;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setReserved(int reserved)
/* 141:    */   {
/* 142:270 */     this.field_7_reserved = reserved;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public short getOptions()
/* 146:    */   {
/* 147:280 */     return this.field_1_options;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean getDisplayFormulas()
/* 151:    */   {
/* 152:292 */     return displayFormulas.isSet(this.field_1_options);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public boolean getDisplayGridlines()
/* 156:    */   {
/* 157:302 */     return displayGridlines.isSet(this.field_1_options);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public boolean getDisplayRowColHeadings()
/* 161:    */   {
/* 162:312 */     return displayRowColHeadings.isSet(this.field_1_options);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean getFreezePanes()
/* 166:    */   {
/* 167:322 */     return freezePanes.isSet(this.field_1_options);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public boolean getDisplayZeros()
/* 171:    */   {
/* 172:332 */     return displayZeros.isSet(this.field_1_options);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public boolean getDefaultHeader()
/* 176:    */   {
/* 177:342 */     return defaultHeader.isSet(this.field_1_options);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean getArabic()
/* 181:    */   {
/* 182:352 */     return arabic.isSet(this.field_1_options);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public boolean getDisplayGuts()
/* 186:    */   {
/* 187:362 */     return displayGuts.isSet(this.field_1_options);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public boolean getFreezePanesNoSplit()
/* 191:    */   {
/* 192:372 */     return freezePanesNoSplit.isSet(this.field_1_options);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean getSelected()
/* 196:    */   {
/* 197:382 */     return selected.isSet(this.field_1_options);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public boolean isActive()
/* 201:    */   {
/* 202:391 */     return active.isSet(this.field_1_options);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public boolean getSavedInPageBreakPreview()
/* 206:    */   {
/* 207:401 */     return savedInPageBreakPreview.isSet(this.field_1_options);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public short getTopRow()
/* 211:    */   {
/* 212:413 */     return this.field_2_top_row;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public short getLeftCol()
/* 216:    */   {
/* 217:423 */     return this.field_3_left_col;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getHeaderColor()
/* 221:    */   {
/* 222:433 */     return this.field_4_header_color;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public short getPageBreakZoom()
/* 226:    */   {
/* 227:443 */     return this.field_5_page_break_zoom;
/* 228:    */   }
/* 229:    */   
/* 230:    */   public short getNormalZoom()
/* 231:    */   {
/* 232:453 */     return this.field_6_normal_zoom;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public int getReserved()
/* 236:    */   {
/* 237:463 */     return this.field_7_reserved;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public String toString()
/* 241:    */   {
/* 242:468 */     StringBuffer buffer = new StringBuffer();
/* 243:    */     
/* 244:470 */     buffer.append("[WINDOW2]\n");
/* 245:471 */     buffer.append("    .options        = ").append(Integer.toHexString(getOptions())).append("\n");
/* 246:    */     
/* 247:473 */     buffer.append("       .dispformulas= ").append(getDisplayFormulas()).append("\n");
/* 248:    */     
/* 249:475 */     buffer.append("       .dispgridlins= ").append(getDisplayGridlines()).append("\n");
/* 250:    */     
/* 251:477 */     buffer.append("       .disprcheadin= ").append(getDisplayRowColHeadings()).append("\n");
/* 252:    */     
/* 253:479 */     buffer.append("       .freezepanes = ").append(getFreezePanes()).append("\n");
/* 254:    */     
/* 255:481 */     buffer.append("       .displayzeros= ").append(getDisplayZeros()).append("\n");
/* 256:    */     
/* 257:483 */     buffer.append("       .defaultheadr= ").append(getDefaultHeader()).append("\n");
/* 258:    */     
/* 259:485 */     buffer.append("       .arabic      = ").append(getArabic()).append("\n");
/* 260:    */     
/* 261:487 */     buffer.append("       .displayguts = ").append(getDisplayGuts()).append("\n");
/* 262:    */     
/* 263:489 */     buffer.append("       .frzpnsnosplt= ").append(getFreezePanesNoSplit()).append("\n");
/* 264:    */     
/* 265:491 */     buffer.append("       .selected    = ").append(getSelected()).append("\n");
/* 266:    */     
/* 267:493 */     buffer.append("       .active       = ").append(isActive()).append("\n");
/* 268:    */     
/* 269:495 */     buffer.append("       .svdinpgbrkpv= ").append(getSavedInPageBreakPreview()).append("\n");
/* 270:    */     
/* 271:497 */     buffer.append("    .toprow         = ").append(Integer.toHexString(getTopRow())).append("\n");
/* 272:    */     
/* 273:499 */     buffer.append("    .leftcol        = ").append(Integer.toHexString(getLeftCol())).append("\n");
/* 274:    */     
/* 275:501 */     buffer.append("    .headercolor    = ").append(Integer.toHexString(getHeaderColor())).append("\n");
/* 276:    */     
/* 277:503 */     buffer.append("    .pagebreakzoom  = ").append(Integer.toHexString(getPageBreakZoom())).append("\n");
/* 278:    */     
/* 279:505 */     buffer.append("    .normalzoom     = ").append(Integer.toHexString(getNormalZoom())).append("\n");
/* 280:    */     
/* 281:507 */     buffer.append("    .reserved       = ").append(Integer.toHexString(getReserved())).append("\n");
/* 282:    */     
/* 283:509 */     buffer.append("[/WINDOW2]\n");
/* 284:510 */     return buffer.toString();
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void serialize(LittleEndianOutput out)
/* 288:    */   {
/* 289:514 */     out.writeShort(getOptions());
/* 290:515 */     out.writeShort(getTopRow());
/* 291:516 */     out.writeShort(getLeftCol());
/* 292:517 */     out.writeInt(getHeaderColor());
/* 293:518 */     out.writeShort(getPageBreakZoom());
/* 294:519 */     out.writeShort(getNormalZoom());
/* 295:520 */     out.writeInt(getReserved());
/* 296:    */   }
/* 297:    */   
/* 298:    */   protected int getDataSize()
/* 299:    */   {
/* 300:524 */     return 18;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public short getSid()
/* 304:    */   {
/* 305:529 */     return 574;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public Object clone()
/* 309:    */   {
/* 310:533 */     WindowTwoRecord rec = new WindowTwoRecord();
/* 311:534 */     rec.field_1_options = this.field_1_options;
/* 312:535 */     rec.field_2_top_row = this.field_2_top_row;
/* 313:536 */     rec.field_3_left_col = this.field_3_left_col;
/* 314:537 */     rec.field_4_header_color = this.field_4_header_color;
/* 315:538 */     rec.field_5_page_break_zoom = this.field_5_page_break_zoom;
/* 316:539 */     rec.field_6_normal_zoom = this.field_6_normal_zoom;
/* 317:540 */     rec.field_7_reserved = this.field_7_reserved;
/* 318:541 */     return rec;
/* 319:    */   }
/* 320:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WindowTwoRecord
 * JD-Core Version:    0.7.0.1
 */