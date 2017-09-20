/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.ss.usermodel.ConditionFilterData;
/*   6:    */ import org.apache.poi.ss.usermodel.ConditionFilterType;
/*   7:    */ import org.apache.poi.ss.usermodel.ConditionType;
/*   8:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*   9:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
/*  10:    */ import org.apache.poi.ss.usermodel.ExcelNumberFormat;
/*  11:    */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  12:    */ import org.apache.poi.xssf.model.StylesTable;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule.Factory;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorScale;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf.Factory;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
/*  22:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
/*  26:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType.Enum;
/*  27:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.Enum;
/*  28:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STConditionalFormattingOperator.Enum;
/*  29:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum;
/*  30:    */ 
/*  31:    */ public class XSSFConditionalFormattingRule
/*  32:    */   implements ConditionalFormattingRule
/*  33:    */ {
/*  34:    */   private final CTCfRule _cfRule;
/*  35:    */   private XSSFSheet _sh;
/*  36: 39 */   private static Map<STCfType.Enum, ConditionType> typeLookup = new HashMap();
/*  37: 40 */   private static Map<STCfType.Enum, ConditionFilterType> filterTypeLookup = new HashMap();
/*  38:    */   
/*  39:    */   static
/*  40:    */   {
/*  41: 42 */     typeLookup.put(STCfType.CELL_IS, ConditionType.CELL_VALUE_IS);
/*  42: 43 */     typeLookup.put(STCfType.EXPRESSION, ConditionType.FORMULA);
/*  43: 44 */     typeLookup.put(STCfType.COLOR_SCALE, ConditionType.COLOR_SCALE);
/*  44: 45 */     typeLookup.put(STCfType.DATA_BAR, ConditionType.DATA_BAR);
/*  45: 46 */     typeLookup.put(STCfType.ICON_SET, ConditionType.ICON_SET);
/*  46:    */     
/*  47:    */ 
/*  48: 49 */     typeLookup.put(STCfType.TOP_10, ConditionType.FILTER);
/*  49: 50 */     typeLookup.put(STCfType.UNIQUE_VALUES, ConditionType.FILTER);
/*  50: 51 */     typeLookup.put(STCfType.DUPLICATE_VALUES, ConditionType.FILTER);
/*  51: 52 */     typeLookup.put(STCfType.CONTAINS_TEXT, ConditionType.FILTER);
/*  52: 53 */     typeLookup.put(STCfType.NOT_CONTAINS_TEXT, ConditionType.FILTER);
/*  53: 54 */     typeLookup.put(STCfType.BEGINS_WITH, ConditionType.FILTER);
/*  54: 55 */     typeLookup.put(STCfType.ENDS_WITH, ConditionType.FILTER);
/*  55: 56 */     typeLookup.put(STCfType.CONTAINS_BLANKS, ConditionType.FILTER);
/*  56: 57 */     typeLookup.put(STCfType.NOT_CONTAINS_BLANKS, ConditionType.FILTER);
/*  57: 58 */     typeLookup.put(STCfType.CONTAINS_ERRORS, ConditionType.FILTER);
/*  58: 59 */     typeLookup.put(STCfType.NOT_CONTAINS_ERRORS, ConditionType.FILTER);
/*  59: 60 */     typeLookup.put(STCfType.TIME_PERIOD, ConditionType.FILTER);
/*  60: 61 */     typeLookup.put(STCfType.ABOVE_AVERAGE, ConditionType.FILTER);
/*  61:    */     
/*  62: 63 */     filterTypeLookup.put(STCfType.TOP_10, ConditionFilterType.TOP_10);
/*  63: 64 */     filterTypeLookup.put(STCfType.UNIQUE_VALUES, ConditionFilterType.UNIQUE_VALUES);
/*  64: 65 */     filterTypeLookup.put(STCfType.DUPLICATE_VALUES, ConditionFilterType.DUPLICATE_VALUES);
/*  65: 66 */     filterTypeLookup.put(STCfType.CONTAINS_TEXT, ConditionFilterType.CONTAINS_TEXT);
/*  66: 67 */     filterTypeLookup.put(STCfType.NOT_CONTAINS_TEXT, ConditionFilterType.NOT_CONTAINS_TEXT);
/*  67: 68 */     filterTypeLookup.put(STCfType.BEGINS_WITH, ConditionFilterType.BEGINS_WITH);
/*  68: 69 */     filterTypeLookup.put(STCfType.ENDS_WITH, ConditionFilterType.ENDS_WITH);
/*  69: 70 */     filterTypeLookup.put(STCfType.CONTAINS_BLANKS, ConditionFilterType.CONTAINS_BLANKS);
/*  70: 71 */     filterTypeLookup.put(STCfType.NOT_CONTAINS_BLANKS, ConditionFilterType.NOT_CONTAINS_BLANKS);
/*  71: 72 */     filterTypeLookup.put(STCfType.CONTAINS_ERRORS, ConditionFilterType.CONTAINS_ERRORS);
/*  72: 73 */     filterTypeLookup.put(STCfType.NOT_CONTAINS_ERRORS, ConditionFilterType.NOT_CONTAINS_ERRORS);
/*  73: 74 */     filterTypeLookup.put(STCfType.TIME_PERIOD, ConditionFilterType.TIME_PERIOD);
/*  74: 75 */     filterTypeLookup.put(STCfType.ABOVE_AVERAGE, ConditionFilterType.ABOVE_AVERAGE);
/*  75:    */   }
/*  76:    */   
/*  77:    */   XSSFConditionalFormattingRule(XSSFSheet sh)
/*  78:    */   {
/*  79: 84 */     this._cfRule = CTCfRule.Factory.newInstance();
/*  80: 85 */     this._sh = sh;
/*  81:    */   }
/*  82:    */   
/*  83:    */   XSSFConditionalFormattingRule(XSSFSheet sh, CTCfRule cfRule)
/*  84:    */   {
/*  85: 89 */     this._cfRule = cfRule;
/*  86: 90 */     this._sh = sh;
/*  87:    */   }
/*  88:    */   
/*  89:    */   CTCfRule getCTCfRule()
/*  90:    */   {
/*  91: 94 */     return this._cfRule;
/*  92:    */   }
/*  93:    */   
/*  94:    */   CTDxf getDxf(boolean create)
/*  95:    */   {
/*  96: 98 */     StylesTable styles = this._sh.getWorkbook().getStylesSource();
/*  97: 99 */     CTDxf dxf = null;
/*  98:100 */     if ((styles._getDXfsSize() > 0) && (this._cfRule.isSetDxfId()))
/*  99:    */     {
/* 100:101 */       int dxfId = (int)this._cfRule.getDxfId();
/* 101:102 */       dxf = styles.getDxfAt(dxfId);
/* 102:    */     }
/* 103:104 */     if ((create) && (dxf == null))
/* 104:    */     {
/* 105:105 */       dxf = CTDxf.Factory.newInstance();
/* 106:106 */       int dxfId = styles.putDxf(dxf);
/* 107:107 */       this._cfRule.setDxfId(dxfId - 1);
/* 108:    */     }
/* 109:109 */     return dxf;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getPriority()
/* 113:    */   {
/* 114:113 */     int priority = this._cfRule.getPriority();
/* 115:    */     
/* 116:115 */     return priority >= 1 ? priority : 0;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public boolean getStopIfTrue()
/* 120:    */   {
/* 121:119 */     return this._cfRule.getStopIfTrue();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public XSSFBorderFormatting createBorderFormatting()
/* 125:    */   {
/* 126:129 */     CTDxf dxf = getDxf(true);
/* 127:    */     CTBorder border;
/* 128:    */     CTBorder border;
/* 129:131 */     if (!dxf.isSetBorder()) {
/* 130:132 */       border = dxf.addNewBorder();
/* 131:    */     } else {
/* 132:134 */       border = dxf.getBorder();
/* 133:    */     }
/* 134:137 */     return new XSSFBorderFormatting(border, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 135:    */   }
/* 136:    */   
/* 137:    */   public XSSFBorderFormatting getBorderFormatting()
/* 138:    */   {
/* 139:144 */     CTDxf dxf = getDxf(false);
/* 140:145 */     if ((dxf == null) || (!dxf.isSetBorder())) {
/* 141:145 */       return null;
/* 142:    */     }
/* 143:147 */     return new XSSFBorderFormatting(dxf.getBorder(), this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 144:    */   }
/* 145:    */   
/* 146:    */   public XSSFFontFormatting createFontFormatting()
/* 147:    */   {
/* 148:157 */     CTDxf dxf = getDxf(true);
/* 149:    */     CTFont font;
/* 150:    */     CTFont font;
/* 151:159 */     if (!dxf.isSetFont()) {
/* 152:160 */       font = dxf.addNewFont();
/* 153:    */     } else {
/* 154:162 */       font = dxf.getFont();
/* 155:    */     }
/* 156:165 */     return new XSSFFontFormatting(font, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 157:    */   }
/* 158:    */   
/* 159:    */   public XSSFFontFormatting getFontFormatting()
/* 160:    */   {
/* 161:172 */     CTDxf dxf = getDxf(false);
/* 162:173 */     if ((dxf == null) || (!dxf.isSetFont())) {
/* 163:173 */       return null;
/* 164:    */     }
/* 165:175 */     return new XSSFFontFormatting(dxf.getFont(), this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 166:    */   }
/* 167:    */   
/* 168:    */   public XSSFPatternFormatting createPatternFormatting()
/* 169:    */   {
/* 170:185 */     CTDxf dxf = getDxf(true);
/* 171:    */     CTFill fill;
/* 172:    */     CTFill fill;
/* 173:187 */     if (!dxf.isSetFill()) {
/* 174:188 */       fill = dxf.addNewFill();
/* 175:    */     } else {
/* 176:190 */       fill = dxf.getFill();
/* 177:    */     }
/* 178:193 */     return new XSSFPatternFormatting(fill, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 179:    */   }
/* 180:    */   
/* 181:    */   public XSSFPatternFormatting getPatternFormatting()
/* 182:    */   {
/* 183:200 */     CTDxf dxf = getDxf(false);
/* 184:201 */     if ((dxf == null) || (!dxf.isSetFill())) {
/* 185:201 */       return null;
/* 186:    */     }
/* 187:203 */     return new XSSFPatternFormatting(dxf.getFill(), this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 188:    */   }
/* 189:    */   
/* 190:    */   public XSSFDataBarFormatting createDataBarFormatting(XSSFColor color)
/* 191:    */   {
/* 192:213 */     if ((this._cfRule.isSetDataBar()) && (this._cfRule.getType() == STCfType.DATA_BAR)) {
/* 193:214 */       return getDataBarFormatting();
/* 194:    */     }
/* 195:217 */     this._cfRule.setType(STCfType.DATA_BAR);
/* 196:    */     
/* 197:    */ 
/* 198:220 */     CTDataBar bar = null;
/* 199:221 */     if (this._cfRule.isSetDataBar()) {
/* 200:222 */       bar = this._cfRule.getDataBar();
/* 201:    */     } else {
/* 202:224 */       bar = this._cfRule.addNewDataBar();
/* 203:    */     }
/* 204:227 */     bar.setColor(color.getCTColor());
/* 205:    */     
/* 206:    */ 
/* 207:230 */     CTCfvo min = bar.addNewCfvo();
/* 208:231 */     min.setType(STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.MIN.name));
/* 209:232 */     CTCfvo max = bar.addNewCfvo();
/* 210:233 */     max.setType(STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.MAX.name));
/* 211:    */     
/* 212:    */ 
/* 213:236 */     return new XSSFDataBarFormatting(bar, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 214:    */   }
/* 215:    */   
/* 216:    */   public XSSFDataBarFormatting getDataBarFormatting()
/* 217:    */   {
/* 218:239 */     if (this._cfRule.isSetDataBar())
/* 219:    */     {
/* 220:240 */       CTDataBar bar = this._cfRule.getDataBar();
/* 221:241 */       return new XSSFDataBarFormatting(bar, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 222:    */     }
/* 223:243 */     return null;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public XSSFIconMultiStateFormatting createMultiStateFormatting(IconMultiStateFormatting.IconSet iconSet)
/* 227:    */   {
/* 228:249 */     if ((this._cfRule.isSetIconSet()) && (this._cfRule.getType() == STCfType.ICON_SET)) {
/* 229:250 */       return getMultiStateFormatting();
/* 230:    */     }
/* 231:253 */     this._cfRule.setType(STCfType.ICON_SET);
/* 232:    */     
/* 233:    */ 
/* 234:256 */     CTIconSet icons = null;
/* 235:257 */     if (this._cfRule.isSetIconSet()) {
/* 236:258 */       icons = this._cfRule.getIconSet();
/* 237:    */     } else {
/* 238:260 */       icons = this._cfRule.addNewIconSet();
/* 239:    */     }
/* 240:263 */     if (iconSet.name != null)
/* 241:    */     {
/* 242:264 */       STIconSetType.Enum xIconSet = STIconSetType.Enum.forString(iconSet.name);
/* 243:265 */       icons.setIconSet(xIconSet);
/* 244:    */     }
/* 245:269 */     int jump = 100 / iconSet.num;
/* 246:270 */     STCfvoType.Enum type = STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.PERCENT.name);
/* 247:271 */     for (int i = 0; i < iconSet.num; i++)
/* 248:    */     {
/* 249:272 */       CTCfvo cfvo = icons.addNewCfvo();
/* 250:273 */       cfvo.setType(type);
/* 251:274 */       cfvo.setVal(Integer.toString(i * jump));
/* 252:    */     }
/* 253:278 */     return new XSSFIconMultiStateFormatting(icons);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public XSSFIconMultiStateFormatting getMultiStateFormatting()
/* 257:    */   {
/* 258:281 */     if (this._cfRule.isSetIconSet())
/* 259:    */     {
/* 260:282 */       CTIconSet icons = this._cfRule.getIconSet();
/* 261:283 */       return new XSSFIconMultiStateFormatting(icons);
/* 262:    */     }
/* 263:285 */     return null;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public XSSFColorScaleFormatting createColorScaleFormatting()
/* 267:    */   {
/* 268:291 */     if ((this._cfRule.isSetColorScale()) && (this._cfRule.getType() == STCfType.COLOR_SCALE)) {
/* 269:292 */       return getColorScaleFormatting();
/* 270:    */     }
/* 271:295 */     this._cfRule.setType(STCfType.COLOR_SCALE);
/* 272:    */     
/* 273:    */ 
/* 274:298 */     CTColorScale scale = null;
/* 275:299 */     if (this._cfRule.isSetColorScale()) {
/* 276:300 */       scale = this._cfRule.getColorScale();
/* 277:    */     } else {
/* 278:302 */       scale = this._cfRule.addNewColorScale();
/* 279:    */     }
/* 280:306 */     if (scale.sizeOfCfvoArray() == 0)
/* 281:    */     {
/* 282:308 */       CTCfvo cfvo = scale.addNewCfvo();
/* 283:309 */       cfvo.setType(STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.MIN.name));
/* 284:310 */       cfvo = scale.addNewCfvo();
/* 285:311 */       cfvo.setType(STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.PERCENTILE.name));
/* 286:312 */       cfvo.setVal("50");
/* 287:313 */       cfvo = scale.addNewCfvo();
/* 288:314 */       cfvo.setType(STCfvoType.Enum.forString(ConditionalFormattingThreshold.RangeType.MAX.name));
/* 289:316 */       for (int i = 0; i < 3; i++) {
/* 290:317 */         scale.addNewColor();
/* 291:    */       }
/* 292:    */     }
/* 293:322 */     return new XSSFColorScaleFormatting(scale, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 294:    */   }
/* 295:    */   
/* 296:    */   public XSSFColorScaleFormatting getColorScaleFormatting()
/* 297:    */   {
/* 298:325 */     if (this._cfRule.isSetColorScale())
/* 299:    */     {
/* 300:326 */       CTColorScale scale = this._cfRule.getColorScale();
/* 301:327 */       return new XSSFColorScaleFormatting(scale, this._sh.getWorkbook().getStylesSource().getIndexedColors());
/* 302:    */     }
/* 303:329 */     return null;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public ExcelNumberFormat getNumberFormat()
/* 307:    */   {
/* 308:338 */     CTDxf dxf = getDxf(false);
/* 309:339 */     if ((dxf == null) || (!dxf.isSetNumFmt())) {
/* 310:339 */       return null;
/* 311:    */     }
/* 312:341 */     CTNumFmt numFmt = dxf.getNumFmt();
/* 313:342 */     return new ExcelNumberFormat((int)numFmt.getNumFmtId(), numFmt.getFormatCode());
/* 314:    */   }
/* 315:    */   
/* 316:    */   public ConditionType getConditionType()
/* 317:    */   {
/* 318:350 */     return (ConditionType)typeLookup.get(this._cfRule.getType());
/* 319:    */   }
/* 320:    */   
/* 321:    */   public ConditionFilterType getConditionFilterType()
/* 322:    */   {
/* 323:358 */     return (ConditionFilterType)filterTypeLookup.get(this._cfRule.getType());
/* 324:    */   }
/* 325:    */   
/* 326:    */   public ConditionFilterData getFilterConfiguration()
/* 327:    */   {
/* 328:362 */     return new XSSFConditionFilterData(this._cfRule);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public byte getComparisonOperation()
/* 332:    */   {
/* 333:376 */     STConditionalFormattingOperator.Enum op = this._cfRule.getOperator();
/* 334:377 */     if (op == null) {
/* 335:377 */       return 0;
/* 336:    */     }
/* 337:379 */     switch (op.intValue())
/* 338:    */     {
/* 339:    */     case 1: 
/* 340:380 */       return 6;
/* 341:    */     case 2: 
/* 342:381 */       return 8;
/* 343:    */     case 6: 
/* 344:382 */       return 5;
/* 345:    */     case 5: 
/* 346:383 */       return 7;
/* 347:    */     case 3: 
/* 348:384 */       return 3;
/* 349:    */     case 4: 
/* 350:385 */       return 4;
/* 351:    */     case 7: 
/* 352:386 */       return 1;
/* 353:    */     case 8: 
/* 354:387 */       return 2;
/* 355:    */     }
/* 356:389 */     return 0;
/* 357:    */   }
/* 358:    */   
/* 359:    */   public String getFormula1()
/* 360:    */   {
/* 361:407 */     return this._cfRule.sizeOfFormulaArray() > 0 ? this._cfRule.getFormulaArray(0) : null;
/* 362:    */   }
/* 363:    */   
/* 364:    */   public String getFormula2()
/* 365:    */   {
/* 366:418 */     return this._cfRule.sizeOfFormulaArray() == 2 ? this._cfRule.getFormulaArray(1) : null;
/* 367:    */   }
/* 368:    */   
/* 369:    */   public int getStripeSize()
/* 370:    */   {
/* 371:426 */     return 0;
/* 372:    */   }
/* 373:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule
 * JD-Core Version:    0.7.0.1
 */