/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.ss.usermodel.Cell;
/*   9:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*  10:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*  11:    */ import org.apache.poi.ss.usermodel.Row;
/*  12:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  13:    */ import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
/*  14:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  15:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  16:    */ import org.apache.poi.ss.util.CellReference;
/*  17:    */ 
/*  18:    */ public class ConditionalFormattingEvaluator
/*  19:    */ {
/*  20:    */   private final WorkbookEvaluator workbookEvaluator;
/*  21:    */   private final Workbook workbook;
/*  22: 63 */   private final Map<String, List<EvaluationConditionalFormatRule>> formats = new HashMap();
/*  23: 73 */   private final Map<CellReference, List<EvaluationConditionalFormatRule>> values = new HashMap();
/*  24:    */   
/*  25:    */   public ConditionalFormattingEvaluator(Workbook wb, WorkbookEvaluatorProvider provider)
/*  26:    */   {
/*  27: 76 */     this.workbook = wb;
/*  28: 77 */     this.workbookEvaluator = provider._getWorkbookEvaluator();
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected WorkbookEvaluator getWorkbookEvaluator()
/*  32:    */   {
/*  33: 81 */     return this.workbookEvaluator;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void clearAllCachedFormats()
/*  37:    */   {
/*  38: 89 */     this.formats.clear();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void clearAllCachedValues()
/*  42:    */   {
/*  43:100 */     this.values.clear();
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected List<EvaluationConditionalFormatRule> getRules(Sheet sheet)
/*  47:    */   {
/*  48:110 */     String sheetName = sheet.getSheetName();
/*  49:111 */     List<EvaluationConditionalFormatRule> rules = (List)this.formats.get(sheetName);
/*  50:112 */     if (rules == null)
/*  51:    */     {
/*  52:113 */       if (this.formats.containsKey(sheetName)) {
/*  53:114 */         return Collections.emptyList();
/*  54:    */       }
/*  55:116 */       SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();
/*  56:117 */       int count = scf.getNumConditionalFormattings();
/*  57:118 */       rules = new ArrayList(count);
/*  58:119 */       this.formats.put(sheetName, rules);
/*  59:120 */       for (int i = 0; i < count; i++)
/*  60:    */       {
/*  61:121 */         ConditionalFormatting f = scf.getConditionalFormattingAt(i);
/*  62:    */         
/*  63:123 */         CellRangeAddress[] regions = f.getFormattingRanges();
/*  64:124 */         for (int r = 0; r < f.getNumberOfRules(); r++)
/*  65:    */         {
/*  66:125 */           ConditionalFormattingRule rule = f.getRule(r);
/*  67:126 */           rules.add(new EvaluationConditionalFormatRule(this.workbookEvaluator, sheet, f, i, rule, r, regions));
/*  68:    */         }
/*  69:    */       }
/*  70:130 */       Collections.sort(rules);
/*  71:    */     }
/*  72:132 */     return Collections.unmodifiableList(rules);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public List<EvaluationConditionalFormatRule> getConditionalFormattingForCell(CellReference cellRef)
/*  76:    */   {
/*  77:153 */     List<EvaluationConditionalFormatRule> rules = (List)this.values.get(cellRef);
/*  78:155 */     if (rules == null)
/*  79:    */     {
/*  80:157 */       rules = new ArrayList();
/*  81:    */       
/*  82:159 */       Sheet sheet = null;
/*  83:160 */       if (cellRef.getSheetName() != null) {
/*  84:160 */         sheet = this.workbook.getSheet(cellRef.getSheetName());
/*  85:    */       } else {
/*  86:161 */         sheet = this.workbook.getSheetAt(this.workbook.getActiveSheetIndex());
/*  87:    */       }
/*  88:169 */       boolean stopIfTrue = false;
/*  89:170 */       for (EvaluationConditionalFormatRule rule : getRules(sheet)) {
/*  90:172 */         if (!stopIfTrue) {
/*  91:176 */           if (rule.matches(cellRef))
/*  92:    */           {
/*  93:177 */             rules.add(rule);
/*  94:178 */             stopIfTrue = rule.getRule().getStopIfTrue();
/*  95:    */           }
/*  96:    */         }
/*  97:    */       }
/*  98:181 */       Collections.sort(rules);
/*  99:182 */       this.values.put(cellRef, rules);
/* 100:    */     }
/* 101:185 */     return Collections.unmodifiableList(rules);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public List<EvaluationConditionalFormatRule> getConditionalFormattingForCell(Cell cell)
/* 105:    */   {
/* 106:206 */     return getConditionalFormattingForCell(getRef(cell));
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static CellReference getRef(Cell cell)
/* 110:    */   {
/* 111:210 */     return new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public List<EvaluationConditionalFormatRule> getFormatRulesForSheet(String sheetName)
/* 115:    */   {
/* 116:218 */     return getFormatRulesForSheet(this.workbook.getSheet(sheetName));
/* 117:    */   }
/* 118:    */   
/* 119:    */   public List<EvaluationConditionalFormatRule> getFormatRulesForSheet(Sheet sheet)
/* 120:    */   {
/* 121:226 */     return getRules(sheet);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public List<Cell> getMatchingCells(Sheet sheet, int conditionalFormattingIndex, int ruleIndex)
/* 125:    */   {
/* 126:241 */     for (EvaluationConditionalFormatRule rule : getRules(sheet)) {
/* 127:242 */       if ((rule.getSheet().equals(sheet)) && (rule.getFormattingIndex() == conditionalFormattingIndex) && (rule.getRuleIndex() == ruleIndex)) {
/* 128:243 */         return getMatchingCells(rule);
/* 129:    */       }
/* 130:    */     }
/* 131:246 */     return Collections.emptyList();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public List<Cell> getMatchingCells(EvaluationConditionalFormatRule rule)
/* 135:    */   {
/* 136:255 */     List<Cell> cells = new ArrayList();
/* 137:256 */     Sheet sheet = rule.getSheet();
/* 138:258 */     for (CellRangeAddress region : rule.getRegions()) {
/* 139:259 */       for (int r = region.getFirstRow(); r <= region.getLastRow(); r++)
/* 140:    */       {
/* 141:260 */         Row row = sheet.getRow(r);
/* 142:261 */         if (row != null) {
/* 143:264 */           for (int c = region.getFirstColumn(); c <= region.getLastColumn(); c++)
/* 144:    */           {
/* 145:265 */             Cell cell = row.getCell(c);
/* 146:266 */             if (cell != null)
/* 147:    */             {
/* 148:270 */               List<EvaluationConditionalFormatRule> cellRules = getConditionalFormattingForCell(cell);
/* 149:271 */               if (cellRules.contains(rule)) {
/* 150:272 */                 cells.add(cell);
/* 151:    */               }
/* 152:    */             }
/* 153:    */           }
/* 154:    */         }
/* 155:    */       }
/* 156:    */     }
/* 157:277 */     return Collections.unmodifiableList(cells);
/* 158:    */   }
/* 159:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ConditionalFormattingEvaluator
 * JD-Core Version:    0.7.0.1
 */