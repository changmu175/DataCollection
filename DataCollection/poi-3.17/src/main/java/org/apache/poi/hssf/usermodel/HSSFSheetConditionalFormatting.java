/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.InternalSheet;
/*   4:    */ import org.apache.poi.hssf.record.CFRule12Record;
/*   5:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   6:    */ import org.apache.poi.hssf.record.CFRuleRecord;
/*   7:    */ import org.apache.poi.hssf.record.aggregates.CFRecordsAggregate;
/*   8:    */ import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
/*   9:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  10:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*  11:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*  12:    */ import org.apache.poi.ss.usermodel.ExtendedColor;
/*  13:    */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  14:    */ import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
/*  15:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  16:    */ 
/*  17:    */ public final class HSSFSheetConditionalFormatting
/*  18:    */   implements SheetConditionalFormatting
/*  19:    */ {
/*  20:    */   private final HSSFSheet _sheet;
/*  21:    */   private final ConditionalFormattingTable _conditionalFormattingTable;
/*  22:    */   
/*  23:    */   HSSFSheetConditionalFormatting(HSSFSheet sheet)
/*  24:    */   {
/*  25: 41 */     this._sheet = sheet;
/*  26: 42 */     this._conditionalFormattingTable = sheet.getSheet().getConditionalFormattingTable();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula1, String formula2)
/*  30:    */   {
/*  31: 72 */     CFRuleRecord rr = CFRuleRecord.create(this._sheet, comparisonOperation, formula1, formula2);
/*  32: 73 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(byte comparisonOperation, String formula1)
/*  36:    */   {
/*  37: 79 */     CFRuleRecord rr = CFRuleRecord.create(this._sheet, comparisonOperation, formula1, null);
/*  38: 80 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(String formula)
/*  42:    */   {
/*  43: 91 */     CFRuleRecord rr = CFRuleRecord.create(this._sheet, formula);
/*  44: 92 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(IconMultiStateFormatting.IconSet iconSet)
/*  48:    */   {
/*  49:106 */     CFRule12Record rr = CFRule12Record.create(this._sheet, iconSet);
/*  50:107 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(HSSFExtendedColor color)
/*  54:    */   {
/*  55:121 */     CFRule12Record rr = CFRule12Record.create(this._sheet, color.getExtendedColor());
/*  56:122 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public HSSFConditionalFormattingRule createConditionalFormattingRule(ExtendedColor color)
/*  60:    */   {
/*  61:125 */     return createConditionalFormattingRule((HSSFExtendedColor)color);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public HSSFConditionalFormattingRule createConditionalFormattingColorScaleRule()
/*  65:    */   {
/*  66:139 */     CFRule12Record rr = CFRule12Record.createColorScale(this._sheet);
/*  67:140 */     return new HSSFConditionalFormattingRule(this._sheet, rr);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int addConditionalFormatting(HSSFConditionalFormatting cf)
/*  71:    */   {
/*  72:156 */     CFRecordsAggregate cfraClone = cf.getCFRecordsAggregate().cloneCFAggregate();
/*  73:    */     
/*  74:158 */     return this._conditionalFormattingTable.add(cfraClone);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int addConditionalFormatting(ConditionalFormatting cf)
/*  78:    */   {
/*  79:162 */     return addConditionalFormatting((HSSFConditionalFormatting)cf);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule[] cfRules)
/*  83:    */   {
/*  84:174 */     if (regions == null) {
/*  85:175 */       throw new IllegalArgumentException("regions must not be null");
/*  86:    */     }
/*  87:177 */     for (CellRangeAddress range : regions) {
/*  88:177 */       range.validate(SpreadsheetVersion.EXCEL97);
/*  89:    */     }
/*  90:179 */     if (cfRules == null) {
/*  91:180 */       throw new IllegalArgumentException("cfRules must not be null");
/*  92:    */     }
/*  93:182 */     if (cfRules.length == 0) {
/*  94:183 */       throw new IllegalArgumentException("cfRules must not be empty");
/*  95:    */     }
/*  96:185 */     if (cfRules.length > 3) {
/*  97:186 */       throw new IllegalArgumentException("Number of rules must not exceed 3");
/*  98:    */     }
/*  99:189 */     CFRuleBase[] rules = new CFRuleBase[cfRules.length];
/* 100:190 */     for (int i = 0; i != cfRules.length; i++) {
/* 101:191 */       rules[i] = cfRules[i].getCfRuleRecord();
/* 102:    */     }
/* 103:193 */     CFRecordsAggregate cfra = new CFRecordsAggregate(regions, rules);
/* 104:194 */     return this._conditionalFormattingTable.add(cfra);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule[] cfRules)
/* 108:    */   {
/* 109:    */     HSSFConditionalFormattingRule[] hfRules;
/* 110:    */     HSSFConditionalFormattingRule[] hfRules;
/* 111:199 */     if ((cfRules instanceof HSSFConditionalFormattingRule[]))
/* 112:    */     {
/* 113:199 */       hfRules = (HSSFConditionalFormattingRule[])cfRules;
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:201 */       hfRules = new HSSFConditionalFormattingRule[cfRules.length];
/* 118:202 */       System.arraycopy(cfRules, 0, hfRules, 0, hfRules.length);
/* 119:    */     }
/* 120:204 */     return addConditionalFormatting(regions, hfRules);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule rule1)
/* 124:    */   {
/* 125:209 */     return addConditionalFormatting(regions, new HSSFConditionalFormattingRule[] { rule1 == null ? null : rule1 });
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1)
/* 129:    */   {
/* 130:216 */     return addConditionalFormatting(regions, (HSSFConditionalFormattingRule)rule1);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public int addConditionalFormatting(CellRangeAddress[] regions, HSSFConditionalFormattingRule rule1, HSSFConditionalFormattingRule rule2)
/* 134:    */   {
/* 135:222 */     return addConditionalFormatting(regions, new HSSFConditionalFormattingRule[] { rule1, rule2 });
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int addConditionalFormatting(CellRangeAddress[] regions, ConditionalFormattingRule rule1, ConditionalFormattingRule rule2)
/* 139:    */   {
/* 140:229 */     return addConditionalFormatting(regions, (HSSFConditionalFormattingRule)rule1, (HSSFConditionalFormattingRule)rule2);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public HSSFConditionalFormatting getConditionalFormattingAt(int index)
/* 144:    */   {
/* 145:243 */     CFRecordsAggregate cf = this._conditionalFormattingTable.get(index);
/* 146:244 */     if (cf == null) {
/* 147:245 */       return null;
/* 148:    */     }
/* 149:247 */     return new HSSFConditionalFormatting(this._sheet, cf);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public int getNumConditionalFormattings()
/* 153:    */   {
/* 154:254 */     return this._conditionalFormattingTable.size();
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void removeConditionalFormatting(int index)
/* 158:    */   {
/* 159:262 */     this._conditionalFormattingTable.remove(index);
/* 160:    */   }
/* 161:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFSheetConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */