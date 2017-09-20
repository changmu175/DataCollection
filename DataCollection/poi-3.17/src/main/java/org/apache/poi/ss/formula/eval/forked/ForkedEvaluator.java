/*   1:    */ package org.apache.poi.ss.formula.eval.forked;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
/*   5:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*   6:    */ import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
/*   7:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   8:    */ import org.apache.poi.ss.formula.EvaluationWorkbook;
/*   9:    */ import org.apache.poi.ss.formula.IStabilityClassifier;
/*  10:    */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*  11:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  16:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  17:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  18:    */ 
/*  19:    */ public final class ForkedEvaluator
/*  20:    */ {
/*  21:    */   private WorkbookEvaluator _evaluator;
/*  22:    */   private ForkedEvaluationWorkbook _sewb;
/*  23:    */   
/*  24:    */   private ForkedEvaluator(EvaluationWorkbook masterWorkbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  25:    */   {
/*  26: 52 */     this._sewb = new ForkedEvaluationWorkbook(masterWorkbook);
/*  27: 53 */     this._evaluator = new WorkbookEvaluator(this._sewb, stabilityClassifier, udfFinder);
/*  28:    */   }
/*  29:    */   
/*  30:    */   private static EvaluationWorkbook createEvaluationWorkbook(Workbook wb)
/*  31:    */   {
/*  32: 56 */     if ((wb instanceof HSSFWorkbook)) {
/*  33: 57 */       return HSSFEvaluationWorkbook.create((HSSFWorkbook)wb);
/*  34:    */     }
/*  35:    */     try
/*  36:    */     {
/*  37: 61 */       Class<?> evalWB = Class.forName("org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook");
/*  38: 62 */       Class<?> xssfWB = Class.forName("org.apache.poi.xssf.usermodel.XSSFWorkbook");
/*  39: 63 */       Method createM = evalWB.getDeclaredMethod("create", new Class[] { xssfWB });
/*  40: 64 */       return (EvaluationWorkbook)createM.invoke(null, new Object[] { wb });
/*  41:    */     }
/*  42:    */     catch (Exception e)
/*  43:    */     {
/*  44: 66 */       throw new IllegalArgumentException("Unexpected workbook type (" + wb.getClass().getName() + ") - check for poi-ooxml and poi-ooxml schemas jar in the classpath", e);
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static ForkedEvaluator create(Workbook wb, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  49:    */   {
/*  50: 75 */     return new ForkedEvaluator(createEvaluationWorkbook(wb), stabilityClassifier, udfFinder);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void updateCell(String sheetName, int rowIndex, int columnIndex, ValueEval value)
/*  54:    */   {
/*  55: 86 */     ForkedEvaluationCell cell = this._sewb.getOrCreateUpdatableCell(sheetName, rowIndex, columnIndex);
/*  56: 87 */     cell.setValue(value);
/*  57: 88 */     this._evaluator.notifyUpdateCell(cell);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void copyUpdatedCells(Workbook workbook)
/*  61:    */   {
/*  62: 97 */     this._sewb.copyUpdatedCells(workbook);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public ValueEval evaluate(String sheetName, int rowIndex, int columnIndex)
/*  66:    */   {
/*  67:113 */     EvaluationCell cell = this._sewb.getEvaluationCell(sheetName, rowIndex, columnIndex);
/*  68:115 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()])
/*  69:    */     {
/*  70:    */     case 1: 
/*  71:117 */       return BoolEval.valueOf(cell.getBooleanCellValue());
/*  72:    */     case 2: 
/*  73:119 */       return ErrorEval.valueOf(cell.getErrorCellValue());
/*  74:    */     case 3: 
/*  75:121 */       return this._evaluator.evaluate(cell);
/*  76:    */     case 4: 
/*  77:123 */       return new NumberEval(cell.getNumericCellValue());
/*  78:    */     case 5: 
/*  79:125 */       return new StringEval(cell.getStringCellValue());
/*  80:    */     case 6: 
/*  81:127 */       return null;
/*  82:    */     }
/*  83:129 */     throw new IllegalStateException("Bad cell type (" + cell.getCellTypeEnum() + ")");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static void setupEnvironment(String[] workbookNames, ForkedEvaluator[] evaluators)
/*  87:    */   {
/*  88:140 */     WorkbookEvaluator[] wbEvals = new WorkbookEvaluator[evaluators.length];
/*  89:141 */     for (int i = 0; i < wbEvals.length; i++) {
/*  90:142 */       wbEvals[i] = evaluators[i]._evaluator;
/*  91:    */     }
/*  92:144 */     CollaboratingWorkbooksEnvironment.setup(workbookNames, wbEvals);
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.forked.ForkedEvaluator
 * JD-Core Version:    0.7.0.1
 */