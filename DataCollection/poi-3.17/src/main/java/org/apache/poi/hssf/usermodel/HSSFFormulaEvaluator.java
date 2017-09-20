/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.apache.poi.ss.formula.BaseFormulaEvaluator;
/*   5:    */ import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
/*   6:    */ import org.apache.poi.ss.formula.IStabilityClassifier;
/*   7:    */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*   8:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.StringValueEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  13:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  14:    */ import org.apache.poi.ss.usermodel.Cell;
/*  15:    */ import org.apache.poi.ss.usermodel.CellValue;
/*  16:    */ import org.apache.poi.ss.usermodel.FormulaEvaluator;
/*  17:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  18:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  19:    */ 
/*  20:    */ public class HSSFFormulaEvaluator
/*  21:    */   extends BaseFormulaEvaluator
/*  22:    */ {
/*  23:    */   private final HSSFWorkbook _book;
/*  24:    */   
/*  25:    */   public HSSFFormulaEvaluator(HSSFWorkbook workbook)
/*  26:    */   {
/*  27: 49 */     this(workbook, null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier)
/*  31:    */   {
/*  32: 58 */     this(workbook, stabilityClassifier, null);
/*  33:    */   }
/*  34:    */   
/*  35:    */   private HSSFFormulaEvaluator(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  36:    */   {
/*  37: 69 */     super(new WorkbookEvaluator(HSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder));
/*  38: 70 */     this._book = workbook;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static HSSFFormulaEvaluator create(HSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  42:    */   {
/*  43: 81 */     return new HSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected RichTextString createRichTextString(String str)
/*  47:    */   {
/*  48: 86 */     return new HSSFRichTextString(str);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static void setupEnvironment(String[] workbookNames, HSSFFormulaEvaluator[] evaluators)
/*  52:    */   {
/*  53: 98 */     BaseFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> evaluators)
/*  57:    */   {
/*  58:103 */     CollaboratingWorkbooksEnvironment.setupFormulaEvaluator(evaluators);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void notifyUpdateCell(HSSFCell cell)
/*  62:    */   {
/*  63:113 */     this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell(cell));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void notifyUpdateCell(Cell cell)
/*  67:    */   {
/*  68:117 */     this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell)cell));
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void notifyDeleteCell(HSSFCell cell)
/*  72:    */   {
/*  73:126 */     this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell(cell));
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void notifyDeleteCell(Cell cell)
/*  77:    */   {
/*  78:130 */     this._bookEvaluator.notifyDeleteCell(new HSSFEvaluationCell((HSSFCell)cell));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void notifySetFormula(Cell cell)
/*  82:    */   {
/*  83:141 */     this._bookEvaluator.notifyUpdateCell(new HSSFEvaluationCell((HSSFCell)cell));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public HSSFCell evaluateInCell(Cell cell)
/*  87:    */   {
/*  88:146 */     return (HSSFCell)super.evaluateInCell(cell);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void evaluateAllFormulaCells(HSSFWorkbook wb)
/*  92:    */   {
/*  93:161 */     evaluateAllFormulaCells(wb, new HSSFFormulaEvaluator(wb));
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static void evaluateAllFormulaCells(Workbook wb)
/*  97:    */   {
/*  98:176 */     BaseFormulaEvaluator.evaluateAllFormulaCells(wb);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void evaluateAll()
/* 102:    */   {
/* 103:192 */     evaluateAllFormulaCells(this._book, this);
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected CellValue evaluateFormulaCellValue(Cell cell)
/* 107:    */   {
/* 108:200 */     ValueEval eval = this._bookEvaluator.evaluate(new HSSFEvaluationCell((HSSFCell)cell));
/* 109:201 */     if ((eval instanceof BoolEval))
/* 110:    */     {
/* 111:202 */       BoolEval be = (BoolEval)eval;
/* 112:203 */       return CellValue.valueOf(be.getBooleanValue());
/* 113:    */     }
/* 114:205 */     if ((eval instanceof NumericValueEval))
/* 115:    */     {
/* 116:206 */       NumericValueEval ne = (NumericValueEval)eval;
/* 117:207 */       return new CellValue(ne.getNumberValue());
/* 118:    */     }
/* 119:209 */     if ((eval instanceof StringValueEval))
/* 120:    */     {
/* 121:210 */       StringValueEval ne = (StringValueEval)eval;
/* 122:211 */       return new CellValue(ne.getStringValue());
/* 123:    */     }
/* 124:213 */     if ((eval instanceof ErrorEval)) {
/* 125:214 */       return CellValue.getError(((ErrorEval)eval).getErrorCode());
/* 126:    */     }
/* 127:216 */     throw new RuntimeException("Unexpected eval class (" + eval.getClass().getName() + ")");
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setIgnoreMissingWorkbooks(boolean ignore)
/* 131:    */   {
/* 132:222 */     this._bookEvaluator.setIgnoreMissingWorkbooks(ignore);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setDebugEvaluationOutputForNextEval(boolean value)
/* 136:    */   {
/* 137:228 */     this._bookEvaluator.setDebugEvaluationOutputForNextEval(value);
/* 138:    */   }
/* 139:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */