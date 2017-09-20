/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   6:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*   7:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*   8:    */ import org.apache.poi.ss.usermodel.ExtendedColor;
/*   9:    */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  10:    */ import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
/*  11:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  12:    */ import org.apache.poi.ss.util.CellRangeUtil;
/*  13:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
/*  14:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STConditionalFormattingOperator;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STConditionalFormattingOperator.Enum;
/*  19:    */ 
/*  20:    */ public class XSSFSheetConditionalFormatting
/*  21:    */   implements SheetConditionalFormatting
/*  22:    */ {
/*  23:    */   protected static final String CF_EXT_2009_NS_X14 = "http://schemas.microsoft.com/office/spreadsheetml/2009/9/main";
/*  24:    */   private final XSSFSheet _sheet;
/*  25:    */   
/*  26:    */   XSSFSheetConditionalFormatting(XSSFSheet sheet)
/*  27:    */   {
/*  28: 50 */     this._sheet = sheet;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula1, String formula2)
/*  32:    */   {
/*  33: 81 */     XSSFConditionalFormattingRule rule = new XSSFConditionalFormattingRule(this._sheet);
/*  34: 82 */     CTCfRule cfRule = rule.getCTCfRule();
/*  35: 83 */     cfRule.addFormula(formula1);
/*  36: 84 */     if (formula2 != null) {
/*  37: 84 */       cfRule.addFormula(formula2);
/*  38:    */     }
/*  39: 85 */     cfRule.setType(STCfType.CELL_IS);
/*  40:    */     STConditionalFormattingOperator.Enum operator;
/*  41: 87 */     switch (comparisonOperation)
/*  42:    */     {
/*  43:    */     case 1: 
/*  44: 88 */       operator = STConditionalFormattingOperator.BETWEEN; break;
/*  45:    */     case 2: 
/*  46: 89 */       operator = STConditionalFormattingOperator.NOT_BETWEEN; break;
/*  47:    */     case 6: 
/*  48: 90 */       operator = STConditionalFormattingOperator.LESS_THAN; break;
/*  49:    */     case 8: 
/*  50: 91 */       operator = STConditionalFormattingOperator.LESS_THAN_OR_EQUAL; break;
/*  51:    */     case 5: 
/*  52: 92 */       operator = STConditionalFormattingOperator.GREATER_THAN; break;
/*  53:    */     case 7: 
/*  54: 93 */       operator = STConditionalFormattingOperator.GREATER_THAN_OR_EQUAL; break;
/*  55:    */     case 3: 
/*  56: 94 */       operator = STConditionalFormattingOperator.EQUAL; break;
/*  57:    */     case 4: 
/*  58: 95 */       operator = STConditionalFormattingOperator.NOT_EQUAL; break;
/*  59:    */     default: 
/*  60: 96 */       throw new IllegalArgumentException("Unknown comparison operator: " + comparisonOperation);
/*  61:    */     }
/*  62: 98 */     cfRule.setOperator(operator);
/*  63:    */     
/*  64:100 */     return rule;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula)
/*  68:    */   {
/*  69:107 */     return createConditionalFormattingRule(comparisonOperation, formula, null);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(String formula)
/*  73:    */   {
/*  74:116 */     XSSFConditionalFormattingRule rule = new XSSFConditionalFormattingRule(this._sheet);
/*  75:117 */     CTCfRule cfRule = rule.getCTCfRule();
/*  76:118 */     cfRule.addFormula(formula);
/*  77:119 */     cfRule.setType(STCfType.EXPRESSION);
/*  78:120 */     return rule;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(XSSFColor color)
/*  82:    */   {
/*  83:134 */     XSSFConditionalFormattingRule rule = new XSSFConditionalFormattingRule(this._sheet);
/*  84:    */     
/*  85:    */ 
/*  86:137 */     rule.createDataBarFormatting(color);
/*  87:    */     
/*  88:    */ 
/*  89:140 */     return rule;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(ExtendedColor color)
/*  93:    */   {
/*  94:143 */     return createConditionalFormattingRule((XSSFColor)color);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public XSSFConditionalFormattingRule createConditionalFormattingRule(IconMultiStateFormatting.IconSet iconSet)
/*  98:    */   {
/*  99:156 */     XSSFConditionalFormattingRule rule = new XSSFConditionalFormattingRule(this._sheet);
/* 100:    */     
/* 101:    */ 
/* 102:159 */     rule.createMultiStateFormatting(iconSet);
/* 103:    */     
/* 104:    */ 
/* 105:162 */     return rule;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public XSSFConditionalFormattingRule createConditionalFormattingColorScaleRule()
/* 109:    */   {
/* 110:176 */     XSSFConditionalFormattingRule rule = new XSSFConditionalFormattingRule(this._sheet);
/* 111:    */     
/* 112:    */ 
/* 113:179 */     rule.createColorScaleFormatting();
/* 114:    */     
/* 115:    */ 
/* 116:182 */     return rule;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule[] cfRules)
/* 120:    */   {
/* 121:186 */     if (regions == null) {
/* 122:187 */       throw new IllegalArgumentException("regions must not be null");
/* 123:    */     }
/* 124:189 */     for (CellRangeAddress range : regions) {
/* 125:189 */       range.validate(SpreadsheetVersion.EXCEL2007);
/* 126:    */     }
/* 127:191 */     if (cfRules == null) {
/* 128:192 */       throw new IllegalArgumentException("cfRules must not be null");
/* 129:    */     }
/* 130:194 */     if (cfRules.length == 0) {
/* 131:195 */       throw new IllegalArgumentException("cfRules must not be empty");
/* 132:    */     }
/* 133:197 */     if (cfRules.length > 3) {
/* 134:198 */       throw new IllegalArgumentException("Number of rules must not exceed 3");
/* 135:    */     }
/* 136:201 */     CellRangeAddress[] mergeCellRanges = CellRangeUtil.mergeCellRanges(regions);
/* 137:202 */     CTConditionalFormatting cf = this._sheet.getCTWorksheet().addNewConditionalFormatting();
/* 138:203 */     List<String> refs = new ArrayList();
/* 139:204 */     for (CellRangeAddress a : mergeCellRanges) {
/* 140:204 */       refs.add(a.formatAsString());
/* 141:    */     }
/* 142:205 */     cf.setSqref(refs);
/* 143:    */     
/* 144:207 */     int priority = 1;
/* 145:208 */     for (CTConditionalFormatting c : this._sheet.getCTWorksheet().getConditionalFormattingArray()) {
/* 146:209 */       priority += c.sizeOfCfRuleArray();
/* 147:    */     }
/* 148:212 */     for (ConditionalFormattingRule rule : cfRules)
/* 149:    */     {
/* 150:213 */       XSSFConditionalFormattingRule xRule = (XSSFConditionalFormattingRule)rule;
/* 151:214 */       xRule.getCTCfRule().setPriority(priority++);
/* 152:215 */       cf.addNewCfRule().set(xRule.getCTCfRule());
/* 153:    */     }
/* 154:217 */     return this._sheet.getCTWorksheet().sizeOfConditionalFormattingArray() - 1;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1)
/* 158:    */   {
/* 159:223 */     return addConditionalFormatting(regions, new XSSFConditionalFormattingRule[] { rule1 == null ? null : (XSSFConditionalFormattingRule)rule1 });
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1, ConditionalFormattingRule rule2)
/* 163:    */   {
/* 164:232 */     return addConditionalFormatting(regions, new XSSFConditionalFormattingRule[] { (XSSFConditionalFormattingRule)rule1, rule1 == null ? null : (XSSFConditionalFormattingRule)rule2 });
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int addConditionalFormatting(ConditionalFormatting cf)
/* 168:    */   {
/* 169:252 */     XSSFConditionalFormatting xcf = (XSSFConditionalFormatting)cf;
/* 170:253 */     CTWorksheet sh = this._sheet.getCTWorksheet();
/* 171:254 */     sh.addNewConditionalFormatting().set(xcf.getCTConditionalFormatting().copy());
/* 172:255 */     return sh.sizeOfConditionalFormattingArray() - 1;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public XSSFConditionalFormatting getConditionalFormattingAt(int index)
/* 176:    */   {
/* 177:266 */     checkIndex(index);
/* 178:267 */     CTConditionalFormatting cf = this._sheet.getCTWorksheet().getConditionalFormattingArray(index);
/* 179:268 */     return new XSSFConditionalFormatting(this._sheet, cf);
/* 180:    */   }
/* 181:    */   
/* 182:    */   public int getNumConditionalFormattings()
/* 183:    */   {
/* 184:275 */     return this._sheet.getCTWorksheet().sizeOfConditionalFormattingArray();
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void removeConditionalFormatting(int index)
/* 188:    */   {
/* 189:283 */     checkIndex(index);
/* 190:284 */     this._sheet.getCTWorksheet().removeConditionalFormatting(index);
/* 191:    */   }
/* 192:    */   
/* 193:    */   private void checkIndex(int index)
/* 194:    */   {
/* 195:288 */     int cnt = getNumConditionalFormattings();
/* 196:289 */     if ((index < 0) || (index >= cnt)) {
/* 197:290 */       throw new IllegalArgumentException("Specified CF index " + index + " is outside the allowable range (0.." + (cnt - 1) + ")");
/* 198:    */     }
/* 199:    */   }
/* 200:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */