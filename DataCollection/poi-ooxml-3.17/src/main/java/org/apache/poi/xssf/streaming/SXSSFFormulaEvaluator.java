/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   4:    */ import org.apache.poi.ss.formula.IStabilityClassifier;
/*   5:    */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*   6:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   7:    */ import org.apache.poi.ss.usermodel.Cell;
/*   8:    */ import org.apache.poi.ss.usermodel.CellType;
/*   9:    */ import org.apache.poi.ss.usermodel.Row;
/*  10:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ import org.apache.poi.xssf.usermodel.BaseXSSFFormulaEvaluator;
/*  14:    */ 
/*  15:    */ public final class SXSSFFormulaEvaluator
/*  16:    */   extends BaseXSSFFormulaEvaluator
/*  17:    */ {
/*  18: 37 */   private static final POILogger logger = POILogFactory.getLogger(SXSSFFormulaEvaluator.class);
/*  19:    */   private SXSSFWorkbook wb;
/*  20:    */   
/*  21:    */   public SXSSFFormulaEvaluator(SXSSFWorkbook workbook)
/*  22:    */   {
/*  23: 42 */     this(workbook, null, null);
/*  24:    */   }
/*  25:    */   
/*  26:    */   private SXSSFFormulaEvaluator(SXSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  27:    */   {
/*  28: 45 */     this(workbook, new WorkbookEvaluator(SXSSFEvaluationWorkbook.create(workbook), stabilityClassifier, udfFinder));
/*  29:    */   }
/*  30:    */   
/*  31:    */   private SXSSFFormulaEvaluator(SXSSFWorkbook workbook, WorkbookEvaluator bookEvaluator)
/*  32:    */   {
/*  33: 48 */     super(bookEvaluator);
/*  34: 49 */     this.wb = workbook;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static SXSSFFormulaEvaluator create(SXSSFWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  38:    */   {
/*  39: 59 */     return new SXSSFFormulaEvaluator(workbook, stabilityClassifier, udfFinder);
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected EvaluationCell toEvaluationCell(Cell cell)
/*  43:    */   {
/*  44: 67 */     if (!(cell instanceof SXSSFCell)) {
/*  45: 68 */       throw new IllegalArgumentException("Unexpected type of cell: " + cell.getClass() + "." + " Only SXSSFCells can be evaluated.");
/*  46:    */     }
/*  47: 72 */     return new SXSSFEvaluationCell((SXSSFCell)cell);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public SXSSFCell evaluateInCell(Cell cell)
/*  51:    */   {
/*  52: 77 */     return (SXSSFCell)super.evaluateInCell(cell);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static void evaluateAllFormulaCells(SXSSFWorkbook wb, boolean skipOutOfWindow)
/*  56:    */   {
/*  57: 87 */     SXSSFFormulaEvaluator eval = new SXSSFFormulaEvaluator(wb);
/*  58: 90 */     for (Sheet sheet : wb) {
/*  59: 91 */       if (((SXSSFSheet)sheet).areAllRowsFlushed()) {
/*  60: 92 */         throw new SheetsFlushedException();
/*  61:    */       }
/*  62:    */     }
/*  63: 97 */     for (Sheet sheet : wb)
/*  64:    */     {
/*  65:100 */       int lastFlushedRowNum = ((SXSSFSheet)sheet).getLastFlushedRowNum();
/*  66:101 */       if (lastFlushedRowNum > -1)
/*  67:    */       {
/*  68:102 */         if (!skipOutOfWindow) {
/*  69:102 */           throw new RowFlushedException(0);
/*  70:    */         }
/*  71:103 */         logger.log(3, new Object[] { "Rows up to " + lastFlushedRowNum + " have already been flushed, skipping" });
/*  72:    */       }
/*  73:107 */       for (Row r : sheet) {
/*  74:108 */         for (Cell c : r) {
/*  75:109 */           if (c.getCellTypeEnum() == CellType.FORMULA) {
/*  76:110 */             eval.evaluateFormulaCellEnum(c);
/*  77:    */           }
/*  78:    */         }
/*  79:    */       }
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void evaluateAll()
/*  84:    */   {
/*  85:126 */     evaluateAllFormulaCells(this.wb, false);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static class SheetsFlushedException
/*  89:    */     extends IllegalStateException
/*  90:    */   {
/*  91:    */     protected SheetsFlushedException()
/*  92:    */     {
/*  93:131 */       super();
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static class RowFlushedException
/*  98:    */     extends IllegalStateException
/*  99:    */   {
/* 100:    */     protected RowFlushedException(int rowNum)
/* 101:    */     {
/* 102:136 */       super();
/* 103:    */     }
/* 104:    */   }
/* 105:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFFormulaEvaluator
 * JD-Core Version:    0.7.0.1
 */