/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.CFRuleBase;
/*  4:   */ import org.apache.poi.hssf.record.cf.Threshold;
/*  5:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/*  6:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
/*  7:   */ 
/*  8:   */ public final class HSSFConditionalFormattingThreshold
/*  9:   */   implements ConditionalFormattingThreshold
/* 10:   */ {
/* 11:   */   private final Threshold threshold;
/* 12:   */   private final HSSFSheet sheet;
/* 13:   */   private final HSSFWorkbook workbook;
/* 14:   */   
/* 15:   */   protected HSSFConditionalFormattingThreshold(Threshold threshold, HSSFSheet sheet)
/* 16:   */   {
/* 17:35 */     this.threshold = threshold;
/* 18:36 */     this.sheet = sheet;
/* 19:37 */     this.workbook = sheet.getWorkbook();
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected Threshold getThreshold()
/* 23:   */   {
/* 24:40 */     return this.threshold;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public RangeType getRangeType()
/* 28:   */   {
/* 29:44 */     return RangeType.byId(this.threshold.getType());
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setRangeType(RangeType type)
/* 33:   */   {
/* 34:47 */     this.threshold.setType((byte)type.id);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String getFormula()
/* 38:   */   {
/* 39:51 */     return HSSFConditionalFormattingRule.toFormulaString(this.threshold.getParsedExpression(), this.workbook);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void setFormula(String formula)
/* 43:   */   {
/* 44:54 */     this.threshold.setParsedExpression(CFRuleBase.parseFormula(formula, this.sheet));
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Double getValue()
/* 48:   */   {
/* 49:58 */     return this.threshold.getValue();
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void setValue(Double value)
/* 53:   */   {
/* 54:61 */     this.threshold.setValue(value);
/* 55:   */   }
/* 56:   */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFConditionalFormattingThreshold

 * JD-Core Version:    0.7.0.1

 */