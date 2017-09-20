/*  1:   */ package org.apache.poi.xssf.streaming;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationCell;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  5:   */ import org.apache.poi.ss.formula.FormulaParser;
/*  6:   */ import org.apache.poi.ss.formula.FormulaType;
/*  7:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ import org.apache.poi.xssf.usermodel.BaseXSSFEvaluationWorkbook;
/* 10:   */ 
/* 11:   */ @Internal
/* 12:   */ public final class SXSSFEvaluationWorkbook
/* 13:   */   extends BaseXSSFEvaluationWorkbook
/* 14:   */ {
/* 15:   */   private final SXSSFWorkbook _uBook;
/* 16:   */   
/* 17:   */   public static SXSSFEvaluationWorkbook create(SXSSFWorkbook book)
/* 18:   */   {
/* 19:36 */     if (book == null) {
/* 20:37 */       return null;
/* 21:   */     }
/* 22:39 */     return new SXSSFEvaluationWorkbook(book);
/* 23:   */   }
/* 24:   */   
/* 25:   */   private SXSSFEvaluationWorkbook(SXSSFWorkbook book)
/* 26:   */   {
/* 27:43 */     super(book.getXSSFWorkbook());
/* 28:44 */     this._uBook = book;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public int getSheetIndex(EvaluationSheet evalSheet)
/* 32:   */   {
/* 33:49 */     SXSSFSheet sheet = ((SXSSFEvaluationSheet)evalSheet).getSXSSFSheet();
/* 34:50 */     return this._uBook.getSheetIndex(sheet);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public EvaluationSheet getSheet(int sheetIndex)
/* 38:   */   {
/* 39:55 */     return new SXSSFEvaluationSheet(this._uBook.getSheetAt(sheetIndex));
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Ptg[] getFormulaTokens(EvaluationCell evalCell)
/* 43:   */   {
/* 44:60 */     SXSSFCell cell = ((SXSSFEvaluationCell)evalCell).getSXSSFCell();
/* 45:61 */     return FormulaParser.parse(cell.getCellFormula(), this, FormulaType.CELL, this._uBook.getSheetIndex(cell.getSheet()));
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFEvaluationWorkbook
 * JD-Core Version:    0.7.0.1
 */