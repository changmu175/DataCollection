/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationCell;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ 
/*  7:   */ @Internal
/*  8:   */ final class HSSFEvaluationSheet
/*  9:   */   implements EvaluationSheet
/* 10:   */ {
/* 11:   */   private final HSSFSheet _hs;
/* 12:   */   
/* 13:   */   public HSSFEvaluationSheet(HSSFSheet hs)
/* 14:   */   {
/* 15:33 */     this._hs = hs;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public HSSFSheet getHSSFSheet()
/* 19:   */   {
/* 20:37 */     return this._hs;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public EvaluationCell getCell(int rowIndex, int columnIndex)
/* 24:   */   {
/* 25:41 */     HSSFRow row = this._hs.getRow(rowIndex);
/* 26:42 */     if (row == null) {
/* 27:43 */       return null;
/* 28:   */     }
/* 29:45 */     HSSFCell cell = row.getCell(columnIndex);
/* 30:46 */     if (cell == null) {
/* 31:47 */       return null;
/* 32:   */     }
/* 33:49 */     return new HSSFEvaluationCell(cell, this);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void clearAllCachedResultValues() {}
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFEvaluationSheet
 * JD-Core Version:    0.7.0.1
 */