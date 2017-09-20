/*  1:   */ package org.apache.poi.xssf.streaming;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationCell;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ 
/*  7:   */ @Internal
/*  8:   */ final class SXSSFEvaluationSheet
/*  9:   */   implements EvaluationSheet
/* 10:   */ {
/* 11:   */   private final SXSSFSheet _xs;
/* 12:   */   
/* 13:   */   public SXSSFEvaluationSheet(SXSSFSheet sheet)
/* 14:   */   {
/* 15:32 */     this._xs = sheet;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public SXSSFSheet getSXSSFSheet()
/* 19:   */   {
/* 20:36 */     return this._xs;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public EvaluationCell getCell(int rowIndex, int columnIndex)
/* 24:   */   {
/* 25:40 */     SXSSFRow row = this._xs.getRow(rowIndex);
/* 26:41 */     if (row == null)
/* 27:   */     {
/* 28:42 */       if (rowIndex <= this._xs.getLastFlushedRowNum()) {
/* 29:43 */         throw new SXSSFFormulaEvaluator.RowFlushedException(rowIndex);
/* 30:   */       }
/* 31:45 */       return null;
/* 32:   */     }
/* 33:47 */     SXSSFCell cell = row.getCell(columnIndex);
/* 34:48 */     if (cell == null) {
/* 35:49 */       return null;
/* 36:   */     }
/* 37:51 */     return new SXSSFEvaluationCell(cell, this);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void clearAllCachedResultValues() {}
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFEvaluationSheet
 * JD-Core Version:    0.7.0.1
 */