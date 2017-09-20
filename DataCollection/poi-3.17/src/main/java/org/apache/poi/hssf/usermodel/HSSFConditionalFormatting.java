/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.CFHeaderBase;
/*   4:    */ import org.apache.poi.hssf.record.CFRuleBase;
/*   5:    */ import org.apache.poi.hssf.record.aggregates.CFRecordsAggregate;
/*   6:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*   7:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*   8:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   9:    */ 
/*  10:    */ public final class HSSFConditionalFormatting
/*  11:    */   implements ConditionalFormatting
/*  12:    */ {
/*  13:    */   private final HSSFSheet sheet;
/*  14:    */   private final CFRecordsAggregate cfAggregate;
/*  15:    */   
/*  16:    */   HSSFConditionalFormatting(HSSFSheet sheet, CFRecordsAggregate cfAggregate)
/*  17:    */   {
/*  18: 81 */     if (sheet == null) {
/*  19: 82 */       throw new IllegalArgumentException("sheet must not be null");
/*  20:    */     }
/*  21: 84 */     if (cfAggregate == null) {
/*  22: 85 */       throw new IllegalArgumentException("cfAggregate must not be null");
/*  23:    */     }
/*  24: 87 */     this.sheet = sheet;
/*  25: 88 */     this.cfAggregate = cfAggregate;
/*  26:    */   }
/*  27:    */   
/*  28:    */   CFRecordsAggregate getCFRecordsAggregate()
/*  29:    */   {
/*  30: 91 */     return this.cfAggregate;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public CellRangeAddress[] getFormattingRanges()
/*  34:    */   {
/*  35: 99 */     return this.cfAggregate.getHeader().getCellRanges();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setFormattingRanges(CellRangeAddress[] ranges)
/*  39:    */   {
/*  40:105 */     this.cfAggregate.getHeader().setCellRanges(ranges);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setRule(int idx, HSSFConditionalFormattingRule cfRule)
/*  44:    */   {
/*  45:118 */     this.cfAggregate.setRule(idx, cfRule.getCfRuleRecord());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setRule(int idx, ConditionalFormattingRule cfRule)
/*  49:    */   {
/*  50:123 */     setRule(idx, (HSSFConditionalFormattingRule)cfRule);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void addRule(HSSFConditionalFormattingRule cfRule)
/*  54:    */   {
/*  55:132 */     this.cfAggregate.addRule(cfRule.getCfRuleRecord());
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void addRule(ConditionalFormattingRule cfRule)
/*  59:    */   {
/*  60:137 */     addRule((HSSFConditionalFormattingRule)cfRule);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public HSSFConditionalFormattingRule getRule(int idx)
/*  64:    */   {
/*  65:145 */     CFRuleBase ruleRecord = this.cfAggregate.getRule(idx);
/*  66:146 */     return new HSSFConditionalFormattingRule(this.sheet, ruleRecord);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getNumberOfRules()
/*  70:    */   {
/*  71:154 */     return this.cfAggregate.getNumberOfRules();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String toString()
/*  75:    */   {
/*  76:159 */     return this.cfAggregate.toString();
/*  77:    */   }
/*  78:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */