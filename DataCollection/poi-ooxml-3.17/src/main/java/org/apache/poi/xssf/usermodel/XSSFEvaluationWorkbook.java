/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationCell;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationSheet;
/*  5:   */ import org.apache.poi.ss.formula.FormulaParser;
/*  6:   */ import org.apache.poi.ss.formula.FormulaType;
/*  7:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ 
/* 10:   */ @Internal
/* 11:   */ public final class XSSFEvaluationWorkbook
/* 12:   */   extends BaseXSSFEvaluationWorkbook
/* 13:   */ {
/* 14:   */   private XSSFEvaluationSheet[] _sheetCache;
/* 15:   */   
/* 16:   */   public static XSSFEvaluationWorkbook create(XSSFWorkbook book)
/* 17:   */   {
/* 18:35 */     if (book == null) {
/* 19:36 */       return null;
/* 20:   */     }
/* 21:38 */     return new XSSFEvaluationWorkbook(book);
/* 22:   */   }
/* 23:   */   
/* 24:   */   private XSSFEvaluationWorkbook(XSSFWorkbook book)
/* 25:   */   {
/* 26:42 */     super(book);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void clearAllCachedResultValues()
/* 30:   */   {
/* 31:50 */     super.clearAllCachedResultValues();
/* 32:51 */     this._sheetCache = null;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public int getSheetIndex(EvaluationSheet evalSheet)
/* 36:   */   {
/* 37:56 */     XSSFSheet sheet = ((XSSFEvaluationSheet)evalSheet).getXSSFSheet();
/* 38:57 */     return this._uBook.getSheetIndex(sheet);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public EvaluationSheet getSheet(int sheetIndex)
/* 42:   */   {
/* 43:66 */     if (this._sheetCache == null)
/* 44:   */     {
/* 45:67 */       int numberOfSheets = this._uBook.getNumberOfSheets();
/* 46:68 */       this._sheetCache = new XSSFEvaluationSheet[numberOfSheets];
/* 47:69 */       for (int i = 0; i < numberOfSheets; i++) {
/* 48:70 */         this._sheetCache[i] = new XSSFEvaluationSheet(this._uBook.getSheetAt(i));
/* 49:   */       }
/* 50:   */     }
/* 51:73 */     if ((sheetIndex < 0) || (sheetIndex >= this._sheetCache.length)) {
/* 52:75 */       this._uBook.getSheetAt(sheetIndex);
/* 53:   */     }
/* 54:77 */     return this._sheetCache[sheetIndex];
/* 55:   */   }
/* 56:   */   
/* 57:   */   public Ptg[] getFormulaTokens(EvaluationCell evalCell)
/* 58:   */   {
/* 59:82 */     XSSFCell cell = ((XSSFEvaluationCell)evalCell).getXSSFCell();
/* 60:83 */     int sheetIndex = this._uBook.getSheetIndex(cell.getSheet());
/* 61:84 */     int rowIndex = cell.getRowIndex();
/* 62:85 */     return FormulaParser.parse(cell.getCellFormula(this), this, FormulaType.CELL, sheetIndex, rowIndex);
/* 63:   */   }
/* 64:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook
 * JD-Core Version:    0.7.0.1
 */