/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.poi.hssf.record.cf.ColorGradientFormatting;
/*   5:    */ import org.apache.poi.hssf.record.cf.ColorGradientThreshold;
/*   6:    */ import org.apache.poi.hssf.record.cf.DataBarFormatting;
/*   7:    */ import org.apache.poi.hssf.record.cf.DataBarThreshold;
/*   8:    */ import org.apache.poi.hssf.record.cf.IconMultiStateFormatting;
/*   9:    */ import org.apache.poi.hssf.record.cf.IconMultiStateThreshold;
/*  10:    */ import org.apache.poi.hssf.record.cf.Threshold;
/*  11:    */ import org.apache.poi.hssf.record.common.ExtendedColor;
/*  12:    */ import org.apache.poi.hssf.record.common.FtrHeader;
/*  13:    */ import org.apache.poi.hssf.record.common.FutureRecord;
/*  14:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  15:    */ import org.apache.poi.ss.formula.Formula;
/*  16:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  17:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
/*  18:    */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  19:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  20:    */ import org.apache.poi.util.HexDump;
/*  21:    */ import org.apache.poi.util.LittleEndianOutput;
/*  22:    */ import org.apache.poi.util.POILogger;
/*  23:    */ 
/*  24:    */ public final class CFRule12Record
/*  25:    */   extends CFRuleBase
/*  26:    */   implements FutureRecord, Cloneable
/*  27:    */ {
/*  28:    */   public static final short sid = 2170;
/*  29:    */   private FtrHeader futureHeader;
/*  30:    */   private int ext_formatting_length;
/*  31:    */   private byte[] ext_formatting_data;
/*  32:    */   private Formula formula_scale;
/*  33:    */   private byte ext_opts;
/*  34:    */   private int priority;
/*  35:    */   private int template_type;
/*  36:    */   private byte template_param_length;
/*  37:    */   private byte[] template_params;
/*  38:    */   private DataBarFormatting data_bar;
/*  39:    */   private IconMultiStateFormatting multistate;
/*  40:    */   private ColorGradientFormatting color_gradient;
/*  41:    */   private byte[] filter_data;
/*  42:    */   
/*  43:    */   private CFRule12Record(byte conditionType, byte comparisonOperation)
/*  44:    */   {
/*  45: 73 */     super(conditionType, comparisonOperation);
/*  46: 74 */     setDefaults();
/*  47:    */   }
/*  48:    */   
/*  49:    */   private CFRule12Record(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2, Ptg[] formulaScale)
/*  50:    */   {
/*  51: 78 */     super(conditionType, comparisonOperation, formula1, formula2);
/*  52: 79 */     setDefaults();
/*  53: 80 */     this.formula_scale = Formula.create(formulaScale);
/*  54:    */   }
/*  55:    */   
/*  56:    */   private void setDefaults()
/*  57:    */   {
/*  58: 83 */     this.futureHeader = new FtrHeader();
/*  59: 84 */     this.futureHeader.setRecordType((short)2170);
/*  60:    */     
/*  61: 86 */     this.ext_formatting_length = 0;
/*  62: 87 */     this.ext_formatting_data = new byte[4];
/*  63:    */     
/*  64: 89 */     this.formula_scale = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/*  65:    */     
/*  66: 91 */     this.ext_opts = 0;
/*  67: 92 */     this.priority = 0;
/*  68: 93 */     this.template_type = getConditionType();
/*  69: 94 */     this.template_param_length = 16;
/*  70: 95 */     this.template_params = new byte[this.template_param_length];
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static CFRule12Record create(HSSFSheet sheet, String formulaText)
/*  74:    */   {
/*  75:107 */     Ptg[] formula1 = parseFormula(formulaText, sheet);
/*  76:108 */     return new CFRule12Record((byte)2, (byte)0, formula1, null, null);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static CFRule12Record create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2)
/*  80:    */   {
/*  81:124 */     Ptg[] formula1 = parseFormula(formulaText1, sheet);
/*  82:125 */     Ptg[] formula2 = parseFormula(formulaText2, sheet);
/*  83:126 */     return new CFRule12Record((byte)1, comparisonOperation, formula1, formula2, null);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static CFRule12Record create(HSSFSheet sheet, byte comparisonOperation, String formulaText1, String formulaText2, String formulaTextScale)
/*  87:    */   {
/*  88:143 */     Ptg[] formula1 = parseFormula(formulaText1, sheet);
/*  89:144 */     Ptg[] formula2 = parseFormula(formulaText2, sheet);
/*  90:145 */     Ptg[] formula3 = parseFormula(formulaTextScale, sheet);
/*  91:146 */     return new CFRule12Record((byte)1, comparisonOperation, formula1, formula2, formula3);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static CFRule12Record create(HSSFSheet sheet, ExtendedColor color)
/*  95:    */   {
/*  96:159 */     CFRule12Record r = new CFRule12Record((byte)4, (byte)0);
/*  97:    */     
/*  98:161 */     DataBarFormatting dbf = r.createDataBarFormatting();
/*  99:162 */     dbf.setColor(color);
/* 100:163 */     dbf.setPercentMin((byte)0);
/* 101:164 */     dbf.setPercentMax((byte)100);
/* 102:    */     
/* 103:166 */     DataBarThreshold min = new DataBarThreshold();
/* 104:167 */     min.setType(ConditionalFormattingThreshold.RangeType.MIN.id);
/* 105:168 */     dbf.setThresholdMin(min);
/* 106:    */     
/* 107:170 */     DataBarThreshold max = new DataBarThreshold();
/* 108:171 */     max.setType(ConditionalFormattingThreshold.RangeType.MAX.id);
/* 109:172 */     dbf.setThresholdMax(max);
/* 110:    */     
/* 111:174 */     return r;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static CFRule12Record create(HSSFSheet sheet, IconMultiStateFormatting.IconSet iconSet)
/* 115:    */   {
/* 116:186 */     Threshold[] ts = new Threshold[iconSet.num];
/* 117:187 */     for (int i = 0; i < ts.length; i++) {
/* 118:188 */       ts[i] = new IconMultiStateThreshold();
/* 119:    */     }
/* 120:191 */     CFRule12Record r = new CFRule12Record((byte)6, (byte)0);
/* 121:    */     
/* 122:193 */     IconMultiStateFormatting imf = r.createMultiStateFormatting();
/* 123:194 */     imf.setIconSet(iconSet);
/* 124:195 */     imf.setThresholds(ts);
/* 125:196 */     return r;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public static CFRule12Record createColorScale(HSSFSheet sheet)
/* 129:    */   {
/* 130:207 */     int numPoints = 3;
/* 131:208 */     ExtendedColor[] colors = new ExtendedColor[numPoints];
/* 132:209 */     ColorGradientThreshold[] ts = new ColorGradientThreshold[numPoints];
/* 133:210 */     for (int i = 0; i < ts.length; i++)
/* 134:    */     {
/* 135:211 */       ts[i] = new ColorGradientThreshold();
/* 136:212 */       colors[i] = new ExtendedColor();
/* 137:    */     }
/* 138:215 */     CFRule12Record r = new CFRule12Record((byte)3, (byte)0);
/* 139:    */     
/* 140:217 */     ColorGradientFormatting cgf = r.createColorGradientFormatting();
/* 141:218 */     cgf.setNumControlPoints(numPoints);
/* 142:219 */     cgf.setThresholds(ts);
/* 143:220 */     cgf.setColors(colors);
/* 144:221 */     return r;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public CFRule12Record(RecordInputStream in)
/* 148:    */   {
/* 149:225 */     this.futureHeader = new FtrHeader(in);
/* 150:226 */     setConditionType(in.readByte());
/* 151:227 */     setComparisonOperation(in.readByte());
/* 152:228 */     int field_3_formula1_len = in.readUShort();
/* 153:229 */     int field_4_formula2_len = in.readUShort();
/* 154:    */     
/* 155:231 */     this.ext_formatting_length = in.readInt();
/* 156:232 */     this.ext_formatting_data = new byte[0];
/* 157:233 */     if (this.ext_formatting_length == 0)
/* 158:    */     {
/* 159:235 */       in.readUShort();
/* 160:    */     }
/* 161:    */     else
/* 162:    */     {
/* 163:237 */       int len = readFormatOptions(in);
/* 164:238 */       if (len < this.ext_formatting_length)
/* 165:    */       {
/* 166:239 */         this.ext_formatting_data = new byte[this.ext_formatting_length - len];
/* 167:240 */         in.readFully(this.ext_formatting_data);
/* 168:    */       }
/* 169:    */     }
/* 170:244 */     setFormula1(Formula.read(field_3_formula1_len, in));
/* 171:245 */     setFormula2(Formula.read(field_4_formula2_len, in));
/* 172:    */     
/* 173:247 */     int formula_scale_len = in.readUShort();
/* 174:248 */     this.formula_scale = Formula.read(formula_scale_len, in);
/* 175:    */     
/* 176:250 */     this.ext_opts = in.readByte();
/* 177:251 */     this.priority = in.readUShort();
/* 178:252 */     this.template_type = in.readUShort();
/* 179:253 */     this.template_param_length = in.readByte();
/* 180:254 */     if ((this.template_param_length == 0) || (this.template_param_length == 16))
/* 181:    */     {
/* 182:255 */       this.template_params = new byte[this.template_param_length];
/* 183:256 */       in.readFully(this.template_params);
/* 184:    */     }
/* 185:    */     else
/* 186:    */     {
/* 187:258 */       logger.log(5, new Object[] { "CF Rule v12 template params length should be 0 or 16, found " + this.template_param_length });
/* 188:259 */       in.readRemainder();
/* 189:    */     }
/* 190:262 */     byte type = getConditionType();
/* 191:263 */     if (type == 3) {
/* 192:264 */       this.color_gradient = new ColorGradientFormatting(in);
/* 193:265 */     } else if (type == 4) {
/* 194:266 */       this.data_bar = new DataBarFormatting(in);
/* 195:267 */     } else if (type == 5) {
/* 196:268 */       this.filter_data = in.readRemainder();
/* 197:269 */     } else if (type == 6) {
/* 198:270 */       this.multistate = new IconMultiStateFormatting(in);
/* 199:    */     }
/* 200:    */   }
/* 201:    */   
/* 202:    */   public boolean containsDataBarBlock()
/* 203:    */   {
/* 204:275 */     return this.data_bar != null;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public DataBarFormatting getDataBarFormatting()
/* 208:    */   {
/* 209:278 */     return this.data_bar;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public DataBarFormatting createDataBarFormatting()
/* 213:    */   {
/* 214:281 */     if (this.data_bar != null) {
/* 215:281 */       return this.data_bar;
/* 216:    */     }
/* 217:284 */     setConditionType((byte)4);
/* 218:285 */     this.data_bar = new DataBarFormatting();
/* 219:286 */     return this.data_bar;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public boolean containsMultiStateBlock()
/* 223:    */   {
/* 224:290 */     return this.multistate != null;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public IconMultiStateFormatting getMultiStateFormatting()
/* 228:    */   {
/* 229:293 */     return this.multistate;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public IconMultiStateFormatting createMultiStateFormatting()
/* 233:    */   {
/* 234:296 */     if (this.multistate != null) {
/* 235:296 */       return this.multistate;
/* 236:    */     }
/* 237:299 */     setConditionType((byte)6);
/* 238:300 */     this.multistate = new IconMultiStateFormatting();
/* 239:301 */     return this.multistate;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public boolean containsColorGradientBlock()
/* 243:    */   {
/* 244:305 */     return this.color_gradient != null;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public ColorGradientFormatting getColorGradientFormatting()
/* 248:    */   {
/* 249:308 */     return this.color_gradient;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public ColorGradientFormatting createColorGradientFormatting()
/* 253:    */   {
/* 254:311 */     if (this.color_gradient != null) {
/* 255:311 */       return this.color_gradient;
/* 256:    */     }
/* 257:314 */     setConditionType((byte)3);
/* 258:315 */     this.color_gradient = new ColorGradientFormatting();
/* 259:316 */     return this.color_gradient;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public Ptg[] getParsedExpressionScale()
/* 263:    */   {
/* 264:328 */     return this.formula_scale.getTokens();
/* 265:    */   }
/* 266:    */   
/* 267:    */   public void setParsedExpressionScale(Ptg[] ptgs)
/* 268:    */   {
/* 269:331 */     this.formula_scale = Formula.create(ptgs);
/* 270:    */   }
/* 271:    */   
/* 272:    */   public int getPriority()
/* 273:    */   {
/* 274:335 */     return this.priority;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void setPriority(int priority)
/* 278:    */   {
/* 279:338 */     this.priority = priority;
/* 280:    */   }
/* 281:    */   
/* 282:    */   public short getSid()
/* 283:    */   {
/* 284:342 */     return 2170;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public void serialize(LittleEndianOutput out)
/* 288:    */   {
/* 289:353 */     this.futureHeader.serialize(out);
/* 290:    */     
/* 291:355 */     int formula1Len = getFormulaSize(getFormula1());
/* 292:356 */     int formula2Len = getFormulaSize(getFormula2());
/* 293:    */     
/* 294:358 */     out.writeByte(getConditionType());
/* 295:359 */     out.writeByte(getComparisonOperation());
/* 296:360 */     out.writeShort(formula1Len);
/* 297:361 */     out.writeShort(formula2Len);
/* 298:364 */     if (this.ext_formatting_length == 0)
/* 299:    */     {
/* 300:365 */       out.writeInt(0);
/* 301:366 */       out.writeShort(0);
/* 302:    */     }
/* 303:    */     else
/* 304:    */     {
/* 305:368 */       out.writeInt(this.ext_formatting_length);
/* 306:369 */       serializeFormattingBlock(out);
/* 307:370 */       out.write(this.ext_formatting_data);
/* 308:    */     }
/* 309:373 */     getFormula1().serializeTokens(out);
/* 310:374 */     getFormula2().serializeTokens(out);
/* 311:375 */     out.writeShort(getFormulaSize(this.formula_scale));
/* 312:376 */     this.formula_scale.serializeTokens(out);
/* 313:    */     
/* 314:378 */     out.writeByte(this.ext_opts);
/* 315:379 */     out.writeShort(this.priority);
/* 316:380 */     out.writeShort(this.template_type);
/* 317:381 */     out.writeByte(this.template_param_length);
/* 318:382 */     out.write(this.template_params);
/* 319:    */     
/* 320:384 */     byte type = getConditionType();
/* 321:385 */     if (type == 3) {
/* 322:386 */       this.color_gradient.serialize(out);
/* 323:387 */     } else if (type == 4) {
/* 324:388 */       this.data_bar.serialize(out);
/* 325:389 */     } else if (type == 5) {
/* 326:390 */       out.write(this.filter_data);
/* 327:391 */     } else if (type == 6) {
/* 328:392 */       this.multistate.serialize(out);
/* 329:    */     }
/* 330:    */   }
/* 331:    */   
/* 332:    */   protected int getDataSize()
/* 333:    */   {
/* 334:397 */     int len = FtrHeader.getDataSize() + 6;
/* 335:398 */     if (this.ext_formatting_length == 0) {
/* 336:399 */       len += 6;
/* 337:    */     } else {
/* 338:401 */       len += 4 + getFormattingBlockSize() + this.ext_formatting_data.length;
/* 339:    */     }
/* 340:403 */     len += getFormulaSize(getFormula1());
/* 341:404 */     len += getFormulaSize(getFormula2());
/* 342:405 */     len += 2 + getFormulaSize(this.formula_scale);
/* 343:406 */     len += 6 + this.template_params.length;
/* 344:    */     
/* 345:408 */     byte type = getConditionType();
/* 346:409 */     if (type == 3) {
/* 347:410 */       len += this.color_gradient.getDataLength();
/* 348:411 */     } else if (type == 4) {
/* 349:412 */       len += this.data_bar.getDataLength();
/* 350:413 */     } else if (type == 5) {
/* 351:414 */       len += this.filter_data.length;
/* 352:415 */     } else if (type == 6) {
/* 353:416 */       len += this.multistate.getDataLength();
/* 354:    */     }
/* 355:418 */     return len;
/* 356:    */   }
/* 357:    */   
/* 358:    */   public String toString()
/* 359:    */   {
/* 360:422 */     StringBuilder buffer = new StringBuilder();
/* 361:423 */     buffer.append("[CFRULE12]\n");
/* 362:424 */     buffer.append("    .condition_type=").append(getConditionType()).append("\n");
/* 363:425 */     buffer.append("    .dxfn12_length =0x").append(Integer.toHexString(this.ext_formatting_length)).append("\n");
/* 364:426 */     buffer.append("    .option_flags  =0x").append(Integer.toHexString(getOptions())).append("\n");
/* 365:427 */     if (containsFontFormattingBlock()) {
/* 366:428 */       buffer.append(this._fontFormatting).append("\n");
/* 367:    */     }
/* 368:430 */     if (containsBorderFormattingBlock()) {
/* 369:431 */       buffer.append(this._borderFormatting).append("\n");
/* 370:    */     }
/* 371:433 */     if (containsPatternFormattingBlock()) {
/* 372:434 */       buffer.append(this._patternFormatting).append("\n");
/* 373:    */     }
/* 374:436 */     buffer.append("    .dxfn12_ext=").append(HexDump.toHex(this.ext_formatting_data)).append("\n");
/* 375:437 */     buffer.append("    .formula_1 =").append(Arrays.toString(getFormula1().getTokens())).append("\n");
/* 376:438 */     buffer.append("    .formula_2 =").append(Arrays.toString(getFormula2().getTokens())).append("\n");
/* 377:439 */     buffer.append("    .formula_S =").append(Arrays.toString(this.formula_scale.getTokens())).append("\n");
/* 378:440 */     buffer.append("    .ext_opts  =").append(this.ext_opts).append("\n");
/* 379:441 */     buffer.append("    .priority  =").append(this.priority).append("\n");
/* 380:442 */     buffer.append("    .template_type  =").append(this.template_type).append("\n");
/* 381:443 */     buffer.append("    .template_params=").append(HexDump.toHex(this.template_params)).append("\n");
/* 382:444 */     buffer.append("    .filter_data    =").append(HexDump.toHex(this.filter_data)).append("\n");
/* 383:445 */     if (this.color_gradient != null) {
/* 384:446 */       buffer.append(this.color_gradient);
/* 385:    */     }
/* 386:448 */     if (this.multistate != null) {
/* 387:449 */       buffer.append(this.multistate);
/* 388:    */     }
/* 389:451 */     if (this.data_bar != null) {
/* 390:452 */       buffer.append(this.data_bar);
/* 391:    */     }
/* 392:454 */     buffer.append("[/CFRULE12]\n");
/* 393:455 */     return buffer.toString();
/* 394:    */   }
/* 395:    */   
/* 396:    */   public CFRule12Record clone()
/* 397:    */   {
/* 398:460 */     CFRule12Record rec = new CFRule12Record(getConditionType(), getComparisonOperation());
/* 399:461 */     rec.futureHeader.setAssociatedRange(this.futureHeader.getAssociatedRange().copy());
/* 400:    */     
/* 401:463 */     super.copyTo(rec);
/* 402:    */     
/* 403:    */ 
/* 404:    */ 
/* 405:467 */     rec.ext_formatting_length = Math.min(this.ext_formatting_length, this.ext_formatting_data.length);
/* 406:468 */     rec.ext_formatting_data = new byte[this.ext_formatting_length];
/* 407:469 */     System.arraycopy(this.ext_formatting_data, 0, rec.ext_formatting_data, 0, rec.ext_formatting_length);
/* 408:    */     
/* 409:471 */     rec.formula_scale = this.formula_scale.copy();
/* 410:    */     
/* 411:473 */     rec.ext_opts = this.ext_opts;
/* 412:474 */     rec.priority = this.priority;
/* 413:475 */     rec.template_type = this.template_type;
/* 414:476 */     rec.template_param_length = this.template_param_length;
/* 415:477 */     rec.template_params = new byte[this.template_param_length];
/* 416:478 */     System.arraycopy(this.template_params, 0, rec.template_params, 0, this.template_param_length);
/* 417:480 */     if (this.color_gradient != null) {
/* 418:481 */       rec.color_gradient = ((ColorGradientFormatting)this.color_gradient.clone());
/* 419:    */     }
/* 420:483 */     if (this.multistate != null) {
/* 421:484 */       rec.multistate = ((IconMultiStateFormatting)this.multistate.clone());
/* 422:    */     }
/* 423:486 */     if (this.data_bar != null) {
/* 424:487 */       rec.data_bar = ((DataBarFormatting)this.data_bar.clone());
/* 425:    */     }
/* 426:489 */     if (this.filter_data != null)
/* 427:    */     {
/* 428:490 */       rec.filter_data = new byte[this.filter_data.length];
/* 429:491 */       System.arraycopy(this.filter_data, 0, rec.filter_data, 0, this.filter_data.length);
/* 430:    */     }
/* 431:494 */     return rec;
/* 432:    */   }
/* 433:    */   
/* 434:    */   public short getFutureRecordType()
/* 435:    */   {
/* 436:498 */     return this.futureHeader.getRecordType();
/* 437:    */   }
/* 438:    */   
/* 439:    */   public FtrHeader getFutureHeader()
/* 440:    */   {
/* 441:501 */     return this.futureHeader;
/* 442:    */   }
/* 443:    */   
/* 444:    */   public CellRangeAddress getAssociatedRange()
/* 445:    */   {
/* 446:504 */     return this.futureHeader.getAssociatedRange();
/* 447:    */   }
/* 448:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFRule12Record
 * JD-Core Version:    0.7.0.1
 */