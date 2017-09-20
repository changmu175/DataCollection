/*  1:   */ package org.apache.poi.ss.usermodel;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
/*  5:   */ import org.apache.poi.ss.formula.EvaluationConditionalFormatRule;
/*  6:   */ 
/*  7:   */ public class ExcelNumberFormat
/*  8:   */ {
/*  9:   */   private final int idx;
/* 10:   */   private final String format;
/* 11:   */   
/* 12:   */   public static ExcelNumberFormat from(CellStyle style)
/* 13:   */   {
/* 14:41 */     if (style == null) {
/* 15:41 */       return null;
/* 16:   */     }
/* 17:42 */     return new ExcelNumberFormat(style.getDataFormat(), style.getDataFormatString());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static ExcelNumberFormat from(Cell cell, ConditionalFormattingEvaluator cfEvaluator)
/* 21:   */   {
/* 22:51 */     if (cell == null) {
/* 23:51 */       return null;
/* 24:   */     }
/* 25:53 */     ExcelNumberFormat nf = null;
/* 26:55 */     if (cfEvaluator != null)
/* 27:   */     {
/* 28:57 */       List<EvaluationConditionalFormatRule> rules = cfEvaluator.getConditionalFormattingForCell(cell);
/* 29:58 */       for (EvaluationConditionalFormatRule rule : rules)
/* 30:   */       {
/* 31:59 */         nf = rule.getNumberFormat();
/* 32:60 */         if (nf != null) {
/* 33:   */           break;
/* 34:   */         }
/* 35:   */       }
/* 36:   */     }
/* 37:63 */     if (nf == null)
/* 38:   */     {
/* 39:64 */       CellStyle style = cell.getCellStyle();
/* 40:65 */       nf = from(style);
/* 41:   */     }
/* 42:67 */     return nf;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public ExcelNumberFormat(int idx, String format)
/* 46:   */   {
/* 47:77 */     this.idx = idx;
/* 48:78 */     this.format = format;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public int getIdx()
/* 52:   */   {
/* 53:88 */     return this.idx;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public String getFormat()
/* 57:   */   {
/* 58:96 */     return this.format;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.ExcelNumberFormat
 * JD-Core Version:    0.7.0.1
 */