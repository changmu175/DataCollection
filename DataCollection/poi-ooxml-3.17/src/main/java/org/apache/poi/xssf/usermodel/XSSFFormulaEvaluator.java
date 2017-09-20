/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.BaseFormulaEvaluator;
/*   4:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   5:    */ import org.apache.poi.ss.formula.IStabilityClassifier;
/*   6:    */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*   7:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   8:    */ import org.apache.poi.ss.usermodel.Cell;
/*   9:    */ 
/*  10:    */ public final class XSSFFormulaEvaluator
/*  11:    */   extends BaseXSSFFormulaEvaluator
/*  12:    */ {
/*  13:    */   private XSSFWorkbook _book;
/*  14:    */   
/*  15:    */   public XSSFFormulaEvaluator(XSSFWorkbook workbook)
/*  16:    */   {
/*  17: 40 */     this(workbook, null, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   private XSSFFormulaEvaluator(XSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  21:    */   {
/*  22: 43 */     this(workbook, new WorkbookEvaluator(XSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder));
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected XSSFFormulaEvaluator(XSSFWorkbook workbook, WorkbookEvaluator bookEvaluator)
/*  26:    */   {
/*  27: 46 */     super(bookEvaluator);
/*  28: 47 */     this._book = workbook;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static XSSFFormulaEvaluator create(XSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  32:    */   {
/*  33: 57 */     return new XSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static void evaluateAllFormulaCells(XSSFWorkbook wb)
/*  37:    */   {
/*  38: 72 */     BaseFormulaEvaluator.evaluateAllFormulaCells(wb);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public XSSFCell evaluateInCell(Cell cell)
/*  42:    */   {
/*  43: 77 */     return (XSSFCell)super.evaluateInCell(cell);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void evaluateAll()
/*  47:    */   {
/*  48: 92 */     evaluateAllFormulaCells(this._book, this);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected EvaluationCell toEvaluationCell(Cell cell)
/*  52:    */   {
/*  53: 99 */     if (!(cell instanceof XSSFCell)) {
/*  54:100 */       throw new IllegalArgumentException("Unexpected type of cell: " + cell.getClass() + "." + " Only XSSFCells can be evaluated.");
/*  55:    */     }
/*  56:104 */     return new XSSFEvaluationCell((XSSFCell)cell);
/*  57:    */   }
/*  58:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */