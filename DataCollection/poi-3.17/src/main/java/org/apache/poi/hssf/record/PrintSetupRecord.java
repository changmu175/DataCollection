/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class PrintSetupRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 161;
/*  11:    */   private short field_1_paper_size;
/*  12:    */   private short field_2_scale;
/*  13:    */   private short field_3_page_start;
/*  14:    */   private short field_4_fit_width;
/*  15:    */   private short field_5_fit_height;
/*  16:    */   private short field_6_options;
/*  17: 42 */   private static final BitField lefttoright = BitFieldFactory.getInstance(1);
/*  18: 44 */   private static final BitField landscape = BitFieldFactory.getInstance(2);
/*  19: 46 */   private static final BitField validsettings = BitFieldFactory.getInstance(4);
/*  20: 51 */   private static final BitField nocolor = BitFieldFactory.getInstance(8);
/*  21: 53 */   private static final BitField draft = BitFieldFactory.getInstance(16);
/*  22: 55 */   private static final BitField notes = BitFieldFactory.getInstance(32);
/*  23: 57 */   private static final BitField noOrientation = BitFieldFactory.getInstance(64);
/*  24: 59 */   private static final BitField usepage = BitFieldFactory.getInstance(128);
/*  25:    */   private short field_7_hresolution;
/*  26:    */   private short field_8_vresolution;
/*  27:    */   private double field_9_headermargin;
/*  28:    */   private double field_10_footermargin;
/*  29:    */   private short field_11_copies;
/*  30:    */   
/*  31:    */   public PrintSetupRecord() {}
/*  32:    */   
/*  33:    */   public PrintSetupRecord(RecordInputStream in)
/*  34:    */   {
/*  35: 73 */     this.field_1_paper_size = in.readShort();
/*  36: 74 */     this.field_2_scale = in.readShort();
/*  37: 75 */     this.field_3_page_start = in.readShort();
/*  38: 76 */     this.field_4_fit_width = in.readShort();
/*  39: 77 */     this.field_5_fit_height = in.readShort();
/*  40: 78 */     this.field_6_options = in.readShort();
/*  41: 79 */     this.field_7_hresolution = in.readShort();
/*  42: 80 */     this.field_8_vresolution = in.readShort();
/*  43: 81 */     this.field_9_headermargin = in.readDouble();
/*  44: 82 */     this.field_10_footermargin = in.readDouble();
/*  45: 83 */     this.field_11_copies = in.readShort();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setPaperSize(short size)
/*  49:    */   {
/*  50: 88 */     this.field_1_paper_size = size;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setScale(short scale)
/*  54:    */   {
/*  55: 93 */     this.field_2_scale = scale;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setPageStart(short start)
/*  59:    */   {
/*  60: 98 */     this.field_3_page_start = start;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setFitWidth(short width)
/*  64:    */   {
/*  65:103 */     this.field_4_fit_width = width;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setFitHeight(short height)
/*  69:    */   {
/*  70:108 */     this.field_5_fit_height = height;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setOptions(short options)
/*  74:    */   {
/*  75:113 */     this.field_6_options = options;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setLeftToRight(boolean ltor)
/*  79:    */   {
/*  80:119 */     this.field_6_options = lefttoright.setShortBoolean(this.field_6_options, ltor);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setLandscape(boolean ls)
/*  84:    */   {
/*  85:124 */     this.field_6_options = landscape.setShortBoolean(this.field_6_options, ls);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void setValidSettings(boolean valid)
/*  89:    */   {
/*  90:129 */     this.field_6_options = validsettings.setShortBoolean(this.field_6_options, valid);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setNoColor(boolean mono)
/*  94:    */   {
/*  95:134 */     this.field_6_options = nocolor.setShortBoolean(this.field_6_options, mono);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setDraft(boolean d)
/*  99:    */   {
/* 100:139 */     this.field_6_options = draft.setShortBoolean(this.field_6_options, d);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setNotes(boolean printnotes)
/* 104:    */   {
/* 105:144 */     this.field_6_options = notes.setShortBoolean(this.field_6_options, printnotes);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setNoOrientation(boolean orientation)
/* 109:    */   {
/* 110:149 */     this.field_6_options = noOrientation.setShortBoolean(this.field_6_options, orientation);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setUsePage(boolean page)
/* 114:    */   {
/* 115:154 */     this.field_6_options = usepage.setShortBoolean(this.field_6_options, page);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setHResolution(short resolution)
/* 119:    */   {
/* 120:160 */     this.field_7_hresolution = resolution;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setVResolution(short resolution)
/* 124:    */   {
/* 125:165 */     this.field_8_vresolution = resolution;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setHeaderMargin(double headermargin)
/* 129:    */   {
/* 130:170 */     this.field_9_headermargin = headermargin;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setFooterMargin(double footermargin)
/* 134:    */   {
/* 135:175 */     this.field_10_footermargin = footermargin;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setCopies(short copies)
/* 139:    */   {
/* 140:180 */     this.field_11_copies = copies;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public short getPaperSize()
/* 144:    */   {
/* 145:185 */     return this.field_1_paper_size;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public short getScale()
/* 149:    */   {
/* 150:190 */     return this.field_2_scale;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public short getPageStart()
/* 154:    */   {
/* 155:195 */     return this.field_3_page_start;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public short getFitWidth()
/* 159:    */   {
/* 160:200 */     return this.field_4_fit_width;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public short getFitHeight()
/* 164:    */   {
/* 165:205 */     return this.field_5_fit_height;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public short getOptions()
/* 169:    */   {
/* 170:210 */     return this.field_6_options;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public boolean getLeftToRight()
/* 174:    */   {
/* 175:216 */     return lefttoright.isSet(this.field_6_options);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public boolean getLandscape()
/* 179:    */   {
/* 180:221 */     return landscape.isSet(this.field_6_options);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean getValidSettings()
/* 184:    */   {
/* 185:226 */     return validsettings.isSet(this.field_6_options);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public boolean getNoColor()
/* 189:    */   {
/* 190:231 */     return nocolor.isSet(this.field_6_options);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean getDraft()
/* 194:    */   {
/* 195:236 */     return draft.isSet(this.field_6_options);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public boolean getNotes()
/* 199:    */   {
/* 200:241 */     return notes.isSet(this.field_6_options);
/* 201:    */   }
/* 202:    */   
/* 203:    */   public boolean getNoOrientation()
/* 204:    */   {
/* 205:246 */     return noOrientation.isSet(this.field_6_options);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public boolean getUsePage()
/* 209:    */   {
/* 210:251 */     return usepage.isSet(this.field_6_options);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public short getHResolution()
/* 214:    */   {
/* 215:257 */     return this.field_7_hresolution;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public short getVResolution()
/* 219:    */   {
/* 220:262 */     return this.field_8_vresolution;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public double getHeaderMargin()
/* 224:    */   {
/* 225:267 */     return this.field_9_headermargin;
/* 226:    */   }
/* 227:    */   
/* 228:    */   public double getFooterMargin()
/* 229:    */   {
/* 230:272 */     return this.field_10_footermargin;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public short getCopies()
/* 234:    */   {
/* 235:277 */     return this.field_11_copies;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public String toString()
/* 239:    */   {
/* 240:282 */     StringBuffer buffer = new StringBuffer();
/* 241:    */     
/* 242:284 */     buffer.append("[PRINTSETUP]\n");
/* 243:285 */     buffer.append("    .papersize      = ").append(getPaperSize()).append("\n");
/* 244:    */     
/* 245:287 */     buffer.append("    .scale          = ").append(getScale()).append("\n");
/* 246:    */     
/* 247:289 */     buffer.append("    .pagestart      = ").append(getPageStart()).append("\n");
/* 248:    */     
/* 249:291 */     buffer.append("    .fitwidth       = ").append(getFitWidth()).append("\n");
/* 250:    */     
/* 251:293 */     buffer.append("    .fitheight      = ").append(getFitHeight()).append("\n");
/* 252:    */     
/* 253:295 */     buffer.append("    .options        = ").append(getOptions()).append("\n");
/* 254:    */     
/* 255:297 */     buffer.append("        .ltor       = ").append(getLeftToRight()).append("\n");
/* 256:    */     
/* 257:299 */     buffer.append("        .landscape  = ").append(getLandscape()).append("\n");
/* 258:    */     
/* 259:301 */     buffer.append("        .valid      = ").append(getValidSettings()).append("\n");
/* 260:    */     
/* 261:303 */     buffer.append("        .mono       = ").append(getNoColor()).append("\n");
/* 262:    */     
/* 263:305 */     buffer.append("        .draft      = ").append(getDraft()).append("\n");
/* 264:    */     
/* 265:307 */     buffer.append("        .notes      = ").append(getNotes()).append("\n");
/* 266:    */     
/* 267:309 */     buffer.append("        .noOrientat = ").append(getNoOrientation()).append("\n");
/* 268:    */     
/* 269:311 */     buffer.append("        .usepage    = ").append(getUsePage()).append("\n");
/* 270:    */     
/* 271:313 */     buffer.append("    .hresolution    = ").append(getHResolution()).append("\n");
/* 272:    */     
/* 273:315 */     buffer.append("    .vresolution    = ").append(getVResolution()).append("\n");
/* 274:    */     
/* 275:317 */     buffer.append("    .headermargin   = ").append(getHeaderMargin()).append("\n");
/* 276:    */     
/* 277:319 */     buffer.append("    .footermargin   = ").append(getFooterMargin()).append("\n");
/* 278:    */     
/* 279:321 */     buffer.append("    .copies         = ").append(getCopies()).append("\n");
/* 280:    */     
/* 281:323 */     buffer.append("[/PRINTSETUP]\n");
/* 282:324 */     return buffer.toString();
/* 283:    */   }
/* 284:    */   
/* 285:    */   public void serialize(LittleEndianOutput out)
/* 286:    */   {
/* 287:328 */     out.writeShort(getPaperSize());
/* 288:329 */     out.writeShort(getScale());
/* 289:330 */     out.writeShort(getPageStart());
/* 290:331 */     out.writeShort(getFitWidth());
/* 291:332 */     out.writeShort(getFitHeight());
/* 292:333 */     out.writeShort(getOptions());
/* 293:334 */     out.writeShort(getHResolution());
/* 294:335 */     out.writeShort(getVResolution());
/* 295:336 */     out.writeDouble(getHeaderMargin());
/* 296:337 */     out.writeDouble(getFooterMargin());
/* 297:338 */     out.writeShort(getCopies());
/* 298:    */   }
/* 299:    */   
/* 300:    */   protected int getDataSize()
/* 301:    */   {
/* 302:342 */     return 34;
/* 303:    */   }
/* 304:    */   
/* 305:    */   public short getSid()
/* 306:    */   {
/* 307:347 */     return 161;
/* 308:    */   }
/* 309:    */   
/* 310:    */   public Object clone()
/* 311:    */   {
/* 312:351 */     PrintSetupRecord rec = new PrintSetupRecord();
/* 313:352 */     rec.field_1_paper_size = this.field_1_paper_size;
/* 314:353 */     rec.field_2_scale = this.field_2_scale;
/* 315:354 */     rec.field_3_page_start = this.field_3_page_start;
/* 316:355 */     rec.field_4_fit_width = this.field_4_fit_width;
/* 317:356 */     rec.field_5_fit_height = this.field_5_fit_height;
/* 318:357 */     rec.field_6_options = this.field_6_options;
/* 319:358 */     rec.field_7_hresolution = this.field_7_hresolution;
/* 320:359 */     rec.field_8_vresolution = this.field_8_vresolution;
/* 321:360 */     rec.field_9_headermargin = this.field_9_headermargin;
/* 322:361 */     rec.field_10_footermargin = this.field_10_footermargin;
/* 323:362 */     rec.field_11_copies = this.field_11_copies;
/* 324:363 */     return rec;
/* 325:    */   }
/* 326:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PrintSetupRecord
 * JD-Core Version:    0.7.0.1
 */