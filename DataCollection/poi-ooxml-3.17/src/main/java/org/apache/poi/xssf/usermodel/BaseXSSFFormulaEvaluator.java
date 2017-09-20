/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.BaseFormulaEvaluator;
/*  4:   */ import org.apache.poi.ss.formula.EvaluationCell;
/*  5:   */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*  6:   */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  9:   */ import org.apache.poi.ss.formula.eval.StringEval;
/* 10:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 11:   */ import org.apache.poi.ss.usermodel.Cell;
/* 12:   */ import org.apache.poi.ss.usermodel.CellValue;
/* 13:   */ import org.apache.poi.ss.usermodel.RichTextString;
/* 14:   */ 
/* 15:   */ public abstract class BaseXSSFFormulaEvaluator
/* 16:   */   extends BaseFormulaEvaluator
/* 17:   */ {
/* 18:   */   protected BaseXSSFFormulaEvaluator(WorkbookEvaluator bookEvaluator)
/* 19:   */   {
/* 20:37 */     super(bookEvaluator);
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected RichTextString createRichTextString(String str)
/* 24:   */   {
/* 25:41 */     return new XSSFRichTextString(str);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void notifySetFormula(Cell cell)
/* 29:   */   {
/* 30:45 */     this._bookEvaluator.notifyUpdateCell(new XSSFEvaluationCell((XSSFCell)cell));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void notifyDeleteCell(Cell cell)
/* 34:   */   {
/* 35:48 */     this._bookEvaluator.notifyDeleteCell(new XSSFEvaluationCell((XSSFCell)cell));
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void notifyUpdateCell(Cell cell)
/* 39:   */   {
/* 40:51 */     this._bookEvaluator.notifyUpdateCell(new XSSFEvaluationCell((XSSFCell)cell));
/* 41:   */   }
/* 42:   */   
/* 43:   */   protected abstract EvaluationCell toEvaluationCell(Cell paramCell);
/* 44:   */   
/* 45:   */   protected CellValue evaluateFormulaCellValue(Cell cell)
/* 46:   */   {
/* 47:63 */     EvaluationCell evalCell = toEvaluationCell(cell);
/* 48:64 */     ValueEval eval = this._bookEvaluator.evaluate(evalCell);
/* 49:65 */     if ((eval instanceof NumberEval))
/* 50:   */     {
/* 51:66 */       NumberEval ne = (NumberEval)eval;
/* 52:67 */       return new CellValue(ne.getNumberValue());
/* 53:   */     }
/* 54:69 */     if ((eval instanceof BoolEval))
/* 55:   */     {
/* 56:70 */       BoolEval be = (BoolEval)eval;
/* 57:71 */       return CellValue.valueOf(be.getBooleanValue());
/* 58:   */     }
/* 59:73 */     if ((eval instanceof StringEval))
/* 60:   */     {
/* 61:74 */       StringEval ne = (StringEval)eval;
/* 62:75 */       return new CellValue(ne.getStringValue());
/* 63:   */     }
/* 64:77 */     if ((eval instanceof ErrorEval)) {
/* 65:78 */       return CellValue.getError(((ErrorEval)eval).getErrorCode());
/* 66:   */     }
/* 67:80 */     throw new RuntimeException("Unexpected eval class (" + eval.getClass().getName() + ")");
/* 68:   */   }
/* 69:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.BaseXSSFFormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */