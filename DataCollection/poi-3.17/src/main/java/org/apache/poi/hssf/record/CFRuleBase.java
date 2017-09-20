/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   4:    */ import org.apache.poi.hssf.record.cf.BorderFormatting;
/*   5:    */ import org.apache.poi.hssf.record.cf.FontFormatting;
/*   6:    */ import org.apache.poi.hssf.record.cf.PatternFormatting;
/*   7:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*   8:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*   9:    */ import org.apache.poi.ss.formula.Formula;
/*  10:    */ import org.apache.poi.ss.formula.FormulaType;
/*  11:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  12:    */ import org.apache.poi.util.BitField;
/*  13:    */ import org.apache.poi.util.BitFieldFactory;
/*  14:    */ import org.apache.poi.util.LittleEndianOutput;
/*  15:    */ import org.apache.poi.util.POILogFactory;
/*  16:    */ import org.apache.poi.util.POILogger;
/*  17:    */ 
/*  18:    */ public abstract class CFRuleBase
/*  19:    */   extends StandardRecord
/*  20:    */   implements Cloneable
/*  21:    */ {
/*  22: 56 */   protected static final POILogger logger = POILogFactory.getLogger(CFRuleBase.class);
/*  23:    */   private byte condition_type;
/*  24:    */   public static final byte CONDITION_TYPE_CELL_VALUE_IS = 1;
/*  25:    */   public static final byte CONDITION_TYPE_FORMULA = 2;
/*  26:    */   public static final byte CONDITION_TYPE_COLOR_SCALE = 3;
/*  27:    */   public static final byte CONDITION_TYPE_DATA_BAR = 4;
/*  28:    */   public static final byte CONDITION_TYPE_FILTER = 5;
/*  29:    */   public static final byte CONDITION_TYPE_ICON_SET = 6;
/*  30:    */   private byte comparison_operator;
/*  31:    */   public static final int TEMPLATE_CELL_VALUE = 0;
/*  32:    */   public static final int TEMPLATE_FORMULA = 1;
/*  33:    */   public static final int TEMPLATE_COLOR_SCALE_FORMATTING = 2;
/*  34:    */   public static final int TEMPLATE_DATA_BAR_FORMATTING = 3;
/*  35:    */   public static final int TEMPLATE_ICON_SET_FORMATTING = 4;
/*  36:    */   public static final int TEMPLATE_FILTER = 5;
/*  37:    */   public static final int TEMPLATE_UNIQUE_VALUES = 7;
/*  38:    */   public static final int TEMPLATE_CONTAINS_TEXT = 8;
/*  39:    */   public static final int TEMPLATE_CONTAINS_BLANKS = 9;
/*  40:    */   public static final int TEMPLATE_CONTAINS_NO_BLANKS = 10;
/*  41:    */   public static final int TEMPLATE_CONTAINS_ERRORS = 11;
/*  42:    */   public static final int TEMPLATE_CONTAINS_NO_ERRORS = 12;
/*  43:    */   public static final int TEMPLATE_TODAY = 15;
/*  44:    */   public static final int TEMPLATE_TOMORROW = 16;
/*  45:    */   public static final int TEMPLATE_YESTERDAY = 17;
/*  46:    */   public static final int TEMPLATE_LAST_7_DAYS = 18;
/*  47:    */   public static final int TEMPLATE_LAST_MONTH = 19;
/*  48:    */   public static final int TEMPLATE_NEXT_MONTH = 20;
/*  49:    */   public static final int TEMPLATE_THIS_WEEK = 21;
/*  50:    */   public static final int TEMPLATE_NEXT_WEEK = 22;
/*  51:    */   public static final int TEMPLATE_LAST_WEEK = 23;
/*  52:    */   public static final int TEMPLATE_THIS_MONTH = 24;
/*  53:    */   public static final int TEMPLATE_ABOVE_AVERAGE = 25;
/*  54:    */   public static final int TEMPLATE_BELOW_AVERAGE = 26;
/*  55:    */   public static final int TEMPLATE_DUPLICATE_VALUES = 27;
/*  56:    */   public static final int TEMPLATE_ABOVE_OR_EQUAL_TO_AVERAGE = 29;
/*  57:    */   public static final int TEMPLATE_BELOW_OR_EQUAL_TO_AVERAGE = 30;
/*  58: 98 */   static final BitField modificationBits = bf(4194303);
/*  59: 99 */   static final BitField alignHor = bf(1);
/*  60:100 */   static final BitField alignVer = bf(2);
/*  61:101 */   static final BitField alignWrap = bf(4);
/*  62:102 */   static final BitField alignRot = bf(8);
/*  63:103 */   static final BitField alignJustLast = bf(16);
/*  64:104 */   static final BitField alignIndent = bf(32);
/*  65:105 */   static final BitField alignShrin = bf(64);
/*  66:106 */   static final BitField mergeCell = bf(128);
/*  67:107 */   static final BitField protLocked = bf(256);
/*  68:108 */   static final BitField protHidden = bf(512);
/*  69:109 */   static final BitField bordLeft = bf(1024);
/*  70:110 */   static final BitField bordRight = bf(2048);
/*  71:111 */   static final BitField bordTop = bf(4096);
/*  72:112 */   static final BitField bordBot = bf(8192);
/*  73:113 */   static final BitField bordTlBr = bf(16384);
/*  74:114 */   static final BitField bordBlTr = bf(32768);
/*  75:115 */   static final BitField pattStyle = bf(65536);
/*  76:116 */   static final BitField pattCol = bf(131072);
/*  77:117 */   static final BitField pattBgCol = bf(262144);
/*  78:118 */   static final BitField notUsed2 = bf(3670016);
/*  79:119 */   static final BitField undocumented = bf(62914560);
/*  80:120 */   static final BitField fmtBlockBits = bf(2080374784);
/*  81:121 */   static final BitField font = bf(67108864);
/*  82:122 */   static final BitField align = bf(134217728);
/*  83:123 */   static final BitField bord = bf(268435456);
/*  84:124 */   static final BitField patt = bf(536870912);
/*  85:125 */   static final BitField prot = bf(1073741824);
/*  86:126 */   static final BitField alignTextDir = bf(-2147483648);
/*  87:    */   protected int formatting_options;
/*  88:    */   protected short formatting_not_used;
/*  89:    */   protected FontFormatting _fontFormatting;
/*  90:    */   protected BorderFormatting _borderFormatting;
/*  91:    */   protected PatternFormatting _patternFormatting;
/*  92:    */   private Formula formula1;
/*  93:    */   private Formula formula2;
/*  94:    */   
/*  95:    */   private static BitField bf(int i)
/*  96:    */   {
/*  97:129 */     return BitFieldFactory.getInstance(i);
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected CFRuleBase(byte conditionType, byte comparisonOperation)
/* 101:    */   {
/* 102:149 */     setConditionType(conditionType);
/* 103:150 */     setComparisonOperation(comparisonOperation);
/* 104:151 */     this.formula1 = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/* 105:152 */     this.formula2 = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected CFRuleBase(byte conditionType, byte comparisonOperation, Ptg[] formula1, Ptg[] formula2)
/* 109:    */   {
/* 110:155 */     this(conditionType, comparisonOperation);
/* 111:156 */     this.formula1 = Formula.create(formula1);
/* 112:157 */     this.formula2 = Formula.create(formula2);
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected CFRuleBase() {}
/* 116:    */   
/* 117:    */   protected int readFormatOptions(RecordInputStream in)
/* 118:    */   {
/* 119:162 */     this.formatting_options = in.readInt();
/* 120:163 */     this.formatting_not_used = in.readShort();
/* 121:    */     
/* 122:165 */     int len = 6;
/* 123:167 */     if (containsFontFormattingBlock())
/* 124:    */     {
/* 125:168 */       this._fontFormatting = new FontFormatting(in);
/* 126:169 */       len += this._fontFormatting.getDataLength();
/* 127:    */     }
/* 128:172 */     if (containsBorderFormattingBlock())
/* 129:    */     {
/* 130:173 */       this._borderFormatting = new BorderFormatting(in);
/* 131:174 */       len += this._borderFormatting.getDataLength();
/* 132:    */     }
/* 133:177 */     if (containsPatternFormattingBlock())
/* 134:    */     {
/* 135:178 */       this._patternFormatting = new PatternFormatting(in);
/* 136:179 */       len += this._patternFormatting.getDataLength();
/* 137:    */     }
/* 138:182 */     return len;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public byte getConditionType()
/* 142:    */   {
/* 143:186 */     return this.condition_type;
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected void setConditionType(byte condition_type)
/* 147:    */   {
/* 148:189 */     if (((this instanceof CFRuleRecord)) && 
/* 149:190 */       (condition_type != 1) && (condition_type != 2)) {
/* 150:194 */       throw new IllegalArgumentException("CFRuleRecord only accepts Value-Is and Formula types");
/* 151:    */     }
/* 152:197 */     this.condition_type = condition_type;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setComparisonOperation(byte operation)
/* 156:    */   {
/* 157:201 */     if ((operation < 0) || (operation > 8)) {
/* 158:202 */       throw new IllegalArgumentException("Valid operators are only in the range 0 to 8");
/* 159:    */     }
/* 160:205 */     this.comparison_operator = operation;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public byte getComparisonOperation()
/* 164:    */   {
/* 165:208 */     return this.comparison_operator;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public boolean containsFontFormattingBlock()
/* 169:    */   {
/* 170:212 */     return getOptionFlag(font);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setFontFormatting(FontFormatting fontFormatting)
/* 174:    */   {
/* 175:215 */     this._fontFormatting = fontFormatting;
/* 176:216 */     setOptionFlag(fontFormatting != null, font);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public FontFormatting getFontFormatting()
/* 180:    */   {
/* 181:219 */     if (containsFontFormattingBlock()) {
/* 182:220 */       return this._fontFormatting;
/* 183:    */     }
/* 184:222 */     return null;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean containsAlignFormattingBlock()
/* 188:    */   {
/* 189:226 */     return getOptionFlag(align);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setAlignFormattingUnchanged()
/* 193:    */   {
/* 194:229 */     setOptionFlag(false, align);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean containsBorderFormattingBlock()
/* 198:    */   {
/* 199:233 */     return getOptionFlag(bord);
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void setBorderFormatting(BorderFormatting borderFormatting)
/* 203:    */   {
/* 204:236 */     this._borderFormatting = borderFormatting;
/* 205:237 */     setOptionFlag(borderFormatting != null, bord);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public BorderFormatting getBorderFormatting()
/* 209:    */   {
/* 210:240 */     if (containsBorderFormattingBlock()) {
/* 211:241 */       return this._borderFormatting;
/* 212:    */     }
/* 213:243 */     return null;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public boolean containsPatternFormattingBlock()
/* 217:    */   {
/* 218:247 */     return getOptionFlag(patt);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void setPatternFormatting(PatternFormatting patternFormatting)
/* 222:    */   {
/* 223:250 */     this._patternFormatting = patternFormatting;
/* 224:251 */     setOptionFlag(patternFormatting != null, patt);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public PatternFormatting getPatternFormatting()
/* 228:    */   {
/* 229:254 */     if (containsPatternFormattingBlock()) {
/* 230:256 */       return this._patternFormatting;
/* 231:    */     }
/* 232:258 */     return null;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public boolean containsProtectionFormattingBlock()
/* 236:    */   {
/* 237:262 */     return getOptionFlag(prot);
/* 238:    */   }
/* 239:    */   
/* 240:    */   public void setProtectionFormattingUnchanged()
/* 241:    */   {
/* 242:265 */     setOptionFlag(false, prot);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public int getOptions()
/* 246:    */   {
/* 247:274 */     return this.formatting_options;
/* 248:    */   }
/* 249:    */   
/* 250:    */   private boolean isModified(BitField field)
/* 251:    */   {
/* 252:278 */     return !field.isSet(this.formatting_options);
/* 253:    */   }
/* 254:    */   
/* 255:    */   private void setModified(boolean modified, BitField field)
/* 256:    */   {
/* 257:281 */     this.formatting_options = field.setBoolean(this.formatting_options, !modified);
/* 258:    */   }
/* 259:    */   
/* 260:    */   public boolean isLeftBorderModified()
/* 261:    */   {
/* 262:285 */     return isModified(bordLeft);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void setLeftBorderModified(boolean modified)
/* 266:    */   {
/* 267:288 */     setModified(modified, bordLeft);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public boolean isRightBorderModified()
/* 271:    */   {
/* 272:292 */     return isModified(bordRight);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void setRightBorderModified(boolean modified)
/* 276:    */   {
/* 277:296 */     setModified(modified, bordRight);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public boolean isTopBorderModified()
/* 281:    */   {
/* 282:300 */     return isModified(bordTop);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public void setTopBorderModified(boolean modified)
/* 286:    */   {
/* 287:303 */     setModified(modified, bordTop);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public boolean isBottomBorderModified()
/* 291:    */   {
/* 292:307 */     return isModified(bordBot);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public void setBottomBorderModified(boolean modified)
/* 296:    */   {
/* 297:310 */     setModified(modified, bordBot);
/* 298:    */   }
/* 299:    */   
/* 300:    */   public boolean isTopLeftBottomRightBorderModified()
/* 301:    */   {
/* 302:314 */     return isModified(bordTlBr);
/* 303:    */   }
/* 304:    */   
/* 305:    */   public void setTopLeftBottomRightBorderModified(boolean modified)
/* 306:    */   {
/* 307:317 */     setModified(modified, bordTlBr);
/* 308:    */   }
/* 309:    */   
/* 310:    */   public boolean isBottomLeftTopRightBorderModified()
/* 311:    */   {
/* 312:321 */     return isModified(bordBlTr);
/* 313:    */   }
/* 314:    */   
/* 315:    */   public void setBottomLeftTopRightBorderModified(boolean modified)
/* 316:    */   {
/* 317:324 */     setModified(modified, bordBlTr);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public boolean isPatternStyleModified()
/* 321:    */   {
/* 322:328 */     return isModified(pattStyle);
/* 323:    */   }
/* 324:    */   
/* 325:    */   public void setPatternStyleModified(boolean modified)
/* 326:    */   {
/* 327:331 */     setModified(modified, pattStyle);
/* 328:    */   }
/* 329:    */   
/* 330:    */   public boolean isPatternColorModified()
/* 331:    */   {
/* 332:335 */     return isModified(pattCol);
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void setPatternColorModified(boolean modified)
/* 336:    */   {
/* 337:338 */     setModified(modified, pattCol);
/* 338:    */   }
/* 339:    */   
/* 340:    */   public boolean isPatternBackgroundColorModified()
/* 341:    */   {
/* 342:342 */     return isModified(pattBgCol);
/* 343:    */   }
/* 344:    */   
/* 345:    */   public void setPatternBackgroundColorModified(boolean modified)
/* 346:    */   {
/* 347:345 */     setModified(modified, pattBgCol);
/* 348:    */   }
/* 349:    */   
/* 350:    */   private boolean getOptionFlag(BitField field)
/* 351:    */   {
/* 352:349 */     return field.isSet(this.formatting_options);
/* 353:    */   }
/* 354:    */   
/* 355:    */   private void setOptionFlag(boolean flag, BitField field)
/* 356:    */   {
/* 357:352 */     this.formatting_options = field.setBoolean(this.formatting_options, flag);
/* 358:    */   }
/* 359:    */   
/* 360:    */   protected int getFormattingBlockSize()
/* 361:    */   {
/* 362:356 */     return 6 + (containsFontFormattingBlock() ? this._fontFormatting.getRawRecord().length : 0) + (containsBorderFormattingBlock() ? 8 : 0) + (containsPatternFormattingBlock() ? 4 : 0);
/* 363:    */   }
/* 364:    */   
/* 365:    */   protected void serializeFormattingBlock(LittleEndianOutput out)
/* 366:    */   {
/* 367:362 */     out.writeInt(this.formatting_options);
/* 368:363 */     out.writeShort(this.formatting_not_used);
/* 369:365 */     if (containsFontFormattingBlock())
/* 370:    */     {
/* 371:366 */       byte[] fontFormattingRawRecord = this._fontFormatting.getRawRecord();
/* 372:367 */       out.write(fontFormattingRawRecord);
/* 373:    */     }
/* 374:370 */     if (containsBorderFormattingBlock()) {
/* 375:371 */       this._borderFormatting.serialize(out);
/* 376:    */     }
/* 377:374 */     if (containsPatternFormattingBlock()) {
/* 378:375 */       this._patternFormatting.serialize(out);
/* 379:    */     }
/* 380:    */   }
/* 381:    */   
/* 382:    */   public Ptg[] getParsedExpression1()
/* 383:    */   {
/* 384:388 */     return this.formula1.getTokens();
/* 385:    */   }
/* 386:    */   
/* 387:    */   public void setParsedExpression1(Ptg[] ptgs)
/* 388:    */   {
/* 389:391 */     this.formula1 = Formula.create(ptgs);
/* 390:    */   }
/* 391:    */   
/* 392:    */   protected Formula getFormula1()
/* 393:    */   {
/* 394:394 */     return this.formula1;
/* 395:    */   }
/* 396:    */   
/* 397:    */   protected void setFormula1(Formula formula1)
/* 398:    */   {
/* 399:397 */     this.formula1 = formula1;
/* 400:    */   }
/* 401:    */   
/* 402:    */   public Ptg[] getParsedExpression2()
/* 403:    */   {
/* 404:406 */     return Formula.getTokens(this.formula2);
/* 405:    */   }
/* 406:    */   
/* 407:    */   public void setParsedExpression2(Ptg[] ptgs)
/* 408:    */   {
/* 409:409 */     this.formula2 = Formula.create(ptgs);
/* 410:    */   }
/* 411:    */   
/* 412:    */   protected Formula getFormula2()
/* 413:    */   {
/* 414:412 */     return this.formula2;
/* 415:    */   }
/* 416:    */   
/* 417:    */   protected void setFormula2(Formula formula2)
/* 418:    */   {
/* 419:415 */     this.formula2 = formula2;
/* 420:    */   }
/* 421:    */   
/* 422:    */   protected static int getFormulaSize(Formula formula)
/* 423:    */   {
/* 424:423 */     return formula.getEncodedTokenSize();
/* 425:    */   }
/* 426:    */   
/* 427:    */   public static Ptg[] parseFormula(String formula, HSSFSheet sheet)
/* 428:    */   {
/* 429:437 */     if (formula == null) {
/* 430:438 */       return null;
/* 431:    */     }
/* 432:440 */     int sheetIndex = sheet.getWorkbook().getSheetIndex(sheet);
/* 433:441 */     return HSSFFormulaParser.parse(formula, sheet.getWorkbook(), FormulaType.CELL, sheetIndex);
/* 434:    */   }
/* 435:    */   
/* 436:    */   protected void copyTo(CFRuleBase rec)
/* 437:    */   {
/* 438:445 */     rec.condition_type = this.condition_type;
/* 439:446 */     rec.comparison_operator = this.comparison_operator;
/* 440:    */     
/* 441:448 */     rec.formatting_options = this.formatting_options;
/* 442:449 */     rec.formatting_not_used = this.formatting_not_used;
/* 443:450 */     if (containsFontFormattingBlock()) {
/* 444:451 */       rec._fontFormatting = this._fontFormatting.clone();
/* 445:    */     }
/* 446:453 */     if (containsBorderFormattingBlock()) {
/* 447:454 */       rec._borderFormatting = this._borderFormatting.clone();
/* 448:    */     }
/* 449:456 */     if (containsPatternFormattingBlock()) {
/* 450:457 */       rec._patternFormatting = ((PatternFormatting)this._patternFormatting.clone());
/* 451:    */     }
/* 452:460 */     rec.setFormula1(getFormula1().copy());
/* 453:461 */     rec.setFormula2(getFormula2().copy());
/* 454:    */   }
/* 455:    */   
/* 456:    */   public abstract CFRuleBase clone();
/* 457:    */   
/* 458:    */   public static final class ComparisonOperator
/* 459:    */   {
/* 460:    */     public static final byte NO_COMPARISON = 0;
/* 461:    */     public static final byte BETWEEN = 1;
/* 462:    */     public static final byte NOT_BETWEEN = 2;
/* 463:    */     public static final byte EQUAL = 3;
/* 464:    */     public static final byte NOT_EQUAL = 4;
/* 465:    */     public static final byte GT = 5;
/* 466:    */     public static final byte LT = 6;
/* 467:    */     public static final byte GE = 7;
/* 468:    */     public static final byte LE = 8;
/* 469:    */     private static final byte max_operator = 8;
/* 470:    */   }
/* 471:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CFRuleBase
 * JD-Core Version:    0.7.0.1
 */