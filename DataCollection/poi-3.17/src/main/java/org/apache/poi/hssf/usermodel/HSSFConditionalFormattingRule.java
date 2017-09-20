/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   4:    */ import org.apache.poi.hssf.record.CFRule12Record;
/*   5:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   6:    */ import org.apache.poi.hssf.record.cf.BorderFormatting;
/*   7:    */ import org.apache.poi.hssf.record.cf.ColorGradientFormatting;
/*   8:    */ import org.apache.poi.hssf.record.cf.DataBarFormatting;
/*   9:    */ import org.apache.poi.hssf.record.cf.FontFormatting;
/*  10:    */ import org.apache.poi.hssf.record.cf.IconMultiStateFormatting;
/*  11:    */ import org.apache.poi.hssf.record.cf.PatternFormatting;
/*  12:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  13:    */ import org.apache.poi.ss.usermodel.ConditionFilterData;
/*  14:    */ import org.apache.poi.ss.usermodel.ConditionFilterType;
/*  15:    */ import org.apache.poi.ss.usermodel.ConditionType;
/*  16:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*  17:    */ import org.apache.poi.ss.usermodel.ExcelNumberFormat;
/*  18:    */ 
/*  19:    */ public final class HSSFConditionalFormattingRule
/*  20:    */   implements ConditionalFormattingRule
/*  21:    */ {
/*  22:    */   private static final byte CELL_COMPARISON = 1;
/*  23:    */   private final CFRuleBase cfRuleRecord;
/*  24:    */   private final HSSFWorkbook workbook;
/*  25:    */   private final HSSFSheet sheet;
/*  26:    */   
/*  27:    */   HSSFConditionalFormattingRule(HSSFSheet pSheet, CFRuleBase pRuleRecord)
/*  28:    */   {
/*  29: 52 */     if (pSheet == null) {
/*  30: 53 */       throw new IllegalArgumentException("pSheet must not be null");
/*  31:    */     }
/*  32: 55 */     if (pRuleRecord == null) {
/*  33: 56 */       throw new IllegalArgumentException("pRuleRecord must not be null");
/*  34:    */     }
/*  35: 58 */     this.sheet = pSheet;
/*  36: 59 */     this.workbook = pSheet.getWorkbook();
/*  37: 60 */     this.cfRuleRecord = pRuleRecord;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getPriority()
/*  41:    */   {
/*  42: 70 */     CFRule12Record rule12 = getCFRule12Record(false);
/*  43: 71 */     if (rule12 == null) {
/*  44: 71 */       return 0;
/*  45:    */     }
/*  46: 72 */     return rule12.getPriority();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean getStopIfTrue()
/*  50:    */   {
/*  51: 80 */     return true;
/*  52:    */   }
/*  53:    */   
/*  54:    */   CFRuleBase getCfRuleRecord()
/*  55:    */   {
/*  56: 84 */     return this.cfRuleRecord;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private CFRule12Record getCFRule12Record(boolean create)
/*  60:    */   {
/*  61: 87 */     if (!(this.cfRuleRecord instanceof CFRule12Record))
/*  62:    */     {
/*  63: 90 */       if (create) {
/*  64: 90 */         throw new IllegalArgumentException("Can't convert a CF into a CF12 record");
/*  65:    */       }
/*  66: 91 */       return null;
/*  67:    */     }
/*  68: 93 */     return (CFRule12Record)this.cfRuleRecord;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public ExcelNumberFormat getNumberFormat()
/*  72:    */   {
/*  73:101 */     return null;
/*  74:    */   }
/*  75:    */   
/*  76:    */   private HSSFFontFormatting getFontFormatting(boolean create)
/*  77:    */   {
/*  78:105 */     FontFormatting fontFormatting = this.cfRuleRecord.getFontFormatting();
/*  79:106 */     if (fontFormatting == null)
/*  80:    */     {
/*  81:107 */       if (!create) {
/*  82:107 */         return null;
/*  83:    */       }
/*  84:108 */       fontFormatting = new FontFormatting();
/*  85:109 */       this.cfRuleRecord.setFontFormatting(fontFormatting);
/*  86:    */     }
/*  87:111 */     return new HSSFFontFormatting(this.cfRuleRecord, this.workbook);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public HSSFFontFormatting getFontFormatting()
/*  91:    */   {
/*  92:118 */     return getFontFormatting(false);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public HSSFFontFormatting createFontFormatting()
/*  96:    */   {
/*  97:126 */     return getFontFormatting(true);
/*  98:    */   }
/*  99:    */   
/* 100:    */   private HSSFBorderFormatting getBorderFormatting(boolean create)
/* 101:    */   {
/* 102:130 */     BorderFormatting borderFormatting = this.cfRuleRecord.getBorderFormatting();
/* 103:131 */     if (borderFormatting == null)
/* 104:    */     {
/* 105:132 */       if (!create) {
/* 106:132 */         return null;
/* 107:    */       }
/* 108:133 */       borderFormatting = new BorderFormatting();
/* 109:134 */       this.cfRuleRecord.setBorderFormatting(borderFormatting);
/* 110:    */     }
/* 111:136 */     return new HSSFBorderFormatting(this.cfRuleRecord, this.workbook);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public HSSFBorderFormatting getBorderFormatting()
/* 115:    */   {
/* 116:143 */     return getBorderFormatting(false);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public HSSFBorderFormatting createBorderFormatting()
/* 120:    */   {
/* 121:151 */     return getBorderFormatting(true);
/* 122:    */   }
/* 123:    */   
/* 124:    */   private HSSFPatternFormatting getPatternFormatting(boolean create)
/* 125:    */   {
/* 126:155 */     PatternFormatting patternFormatting = this.cfRuleRecord.getPatternFormatting();
/* 127:156 */     if (patternFormatting == null)
/* 128:    */     {
/* 129:157 */       if (!create) {
/* 130:157 */         return null;
/* 131:    */       }
/* 132:158 */       patternFormatting = new PatternFormatting();
/* 133:159 */       this.cfRuleRecord.setPatternFormatting(patternFormatting);
/* 134:    */     }
/* 135:161 */     return new HSSFPatternFormatting(this.cfRuleRecord, this.workbook);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public HSSFPatternFormatting getPatternFormatting()
/* 139:    */   {
/* 140:169 */     return getPatternFormatting(false);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public HSSFPatternFormatting createPatternFormatting()
/* 144:    */   {
/* 145:178 */     return getPatternFormatting(true);
/* 146:    */   }
/* 147:    */   
/* 148:    */   private HSSFDataBarFormatting getDataBarFormatting(boolean create)
/* 149:    */   {
/* 150:182 */     CFRule12Record cfRule12Record = getCFRule12Record(create);
/* 151:183 */     if (cfRule12Record == null) {
/* 152:183 */       return null;
/* 153:    */     }
/* 154:185 */     DataBarFormatting databarFormatting = cfRule12Record.getDataBarFormatting();
/* 155:186 */     if (databarFormatting == null)
/* 156:    */     {
/* 157:187 */       if (!create) {
/* 158:187 */         return null;
/* 159:    */       }
/* 160:188 */       cfRule12Record.createDataBarFormatting();
/* 161:    */     }
/* 162:191 */     return new HSSFDataBarFormatting(cfRule12Record, this.sheet);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public HSSFDataBarFormatting getDataBarFormatting()
/* 166:    */   {
/* 167:198 */     return getDataBarFormatting(false);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public HSSFDataBarFormatting createDataBarFormatting()
/* 171:    */   {
/* 172:205 */     return getDataBarFormatting(true);
/* 173:    */   }
/* 174:    */   
/* 175:    */   private HSSFIconMultiStateFormatting getMultiStateFormatting(boolean create)
/* 176:    */   {
/* 177:209 */     CFRule12Record cfRule12Record = getCFRule12Record(create);
/* 178:210 */     if (cfRule12Record == null) {
/* 179:210 */       return null;
/* 180:    */     }
/* 181:212 */     IconMultiStateFormatting iconFormatting = cfRule12Record.getMultiStateFormatting();
/* 182:213 */     if (iconFormatting == null)
/* 183:    */     {
/* 184:214 */       if (!create) {
/* 185:214 */         return null;
/* 186:    */       }
/* 187:215 */       cfRule12Record.createMultiStateFormatting();
/* 188:    */     }
/* 189:217 */     return new HSSFIconMultiStateFormatting(cfRule12Record, this.sheet);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public HSSFIconMultiStateFormatting getMultiStateFormatting()
/* 193:    */   {
/* 194:224 */     return getMultiStateFormatting(false);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public HSSFIconMultiStateFormatting createMultiStateFormatting()
/* 198:    */   {
/* 199:231 */     return getMultiStateFormatting(true);
/* 200:    */   }
/* 201:    */   
/* 202:    */   private HSSFColorScaleFormatting getColorScaleFormatting(boolean create)
/* 203:    */   {
/* 204:235 */     CFRule12Record cfRule12Record = getCFRule12Record(create);
/* 205:236 */     if (cfRule12Record == null) {
/* 206:236 */       return null;
/* 207:    */     }
/* 208:238 */     ColorGradientFormatting colorFormatting = cfRule12Record.getColorGradientFormatting();
/* 209:239 */     if (colorFormatting == null)
/* 210:    */     {
/* 211:240 */       if (!create) {
/* 212:240 */         return null;
/* 213:    */       }
/* 214:241 */       cfRule12Record.createColorGradientFormatting();
/* 215:    */     }
/* 216:244 */     return new HSSFColorScaleFormatting(cfRule12Record, this.sheet);
/* 217:    */   }
/* 218:    */   
/* 219:    */   public HSSFColorScaleFormatting getColorScaleFormatting()
/* 220:    */   {
/* 221:251 */     return getColorScaleFormatting(false);
/* 222:    */   }
/* 223:    */   
/* 224:    */   public HSSFColorScaleFormatting createColorScaleFormatting()
/* 225:    */   {
/* 226:258 */     return getColorScaleFormatting(true);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public ConditionType getConditionType()
/* 230:    */   {
/* 231:266 */     byte code = this.cfRuleRecord.getConditionType();
/* 232:267 */     return ConditionType.forId(code);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public ConditionFilterType getConditionFilterType()
/* 236:    */   {
/* 237:275 */     return getConditionType() == ConditionType.FILTER ? ConditionFilterType.FILTER : null;
/* 238:    */   }
/* 239:    */   
/* 240:    */   public ConditionFilterData getFilterConfiguration()
/* 241:    */   {
/* 242:279 */     return null;
/* 243:    */   }
/* 244:    */   
/* 245:    */   public byte getComparisonOperation()
/* 246:    */   {
/* 247:287 */     return this.cfRuleRecord.getComparisonOperation();
/* 248:    */   }
/* 249:    */   
/* 250:    */   public String getFormula1()
/* 251:    */   {
/* 252:292 */     return toFormulaString(this.cfRuleRecord.getParsedExpression1());
/* 253:    */   }
/* 254:    */   
/* 255:    */   public String getFormula2()
/* 256:    */   {
/* 257:296 */     byte conditionType = this.cfRuleRecord.getConditionType();
/* 258:297 */     if (conditionType == 1)
/* 259:    */     {
/* 260:298 */       byte comparisonOperation = this.cfRuleRecord.getComparisonOperation();
/* 261:299 */       switch (comparisonOperation)
/* 262:    */       {
/* 263:    */       case 1: 
/* 264:    */       case 2: 
/* 265:302 */         return toFormulaString(this.cfRuleRecord.getParsedExpression2());
/* 266:    */       }
/* 267:    */     }
/* 268:305 */     return null;
/* 269:    */   }
/* 270:    */   
/* 271:    */   protected String toFormulaString(Ptg[] parsedExpression)
/* 272:    */   {
/* 273:309 */     return toFormulaString(parsedExpression, this.workbook);
/* 274:    */   }
/* 275:    */   
/* 276:    */   protected static String toFormulaString(Ptg[] parsedExpression, HSSFWorkbook workbook)
/* 277:    */   {
/* 278:312 */     if ((parsedExpression == null) || (parsedExpression.length == 0)) {
/* 279:313 */       return null;
/* 280:    */     }
/* 281:315 */     return HSSFFormulaParser.toFormulaString(workbook, parsedExpression);
/* 282:    */   }
/* 283:    */   
/* 284:    */   public int getStripeSize()
/* 285:    */   {
/* 286:323 */     return 0;
/* 287:    */   }
/* 288:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFConditionalFormattingRule
 * JD-Core Version:    0.7.0.1
 */