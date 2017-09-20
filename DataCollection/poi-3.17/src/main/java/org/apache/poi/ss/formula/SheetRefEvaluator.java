/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  4:   */ import org.apache.poi.ss.formula.ptg.FuncVarPtg;
/*  5:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  6:   */ import org.apache.poi.ss.usermodel.CellType;
/*  7:   */ 
/*  8:   */ final class SheetRefEvaluator
/*  9:   */ {
/* 10:   */   private final WorkbookEvaluator _bookEvaluator;
/* 11:   */   private final EvaluationTracker _tracker;
/* 12:   */   private final int _sheetIndex;
/* 13:   */   private EvaluationSheet _sheet;
/* 14:   */   
/* 15:   */   public SheetRefEvaluator(WorkbookEvaluator bookEvaluator, EvaluationTracker tracker, int sheetIndex)
/* 16:   */   {
/* 17:35 */     if (sheetIndex < 0) {
/* 18:36 */       throw new IllegalArgumentException("Invalid sheetIndex: " + sheetIndex + ".");
/* 19:   */     }
/* 20:38 */     this._bookEvaluator = bookEvaluator;
/* 21:39 */     this._tracker = tracker;
/* 22:40 */     this._sheetIndex = sheetIndex;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getSheetName()
/* 26:   */   {
/* 27:44 */     return this._bookEvaluator.getSheetName(this._sheetIndex);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public ValueEval getEvalForCell(int rowIndex, int columnIndex)
/* 31:   */   {
/* 32:48 */     return this._bookEvaluator.evaluateReference(getSheet(), this._sheetIndex, rowIndex, columnIndex, this._tracker);
/* 33:   */   }
/* 34:   */   
/* 35:   */   private EvaluationSheet getSheet()
/* 36:   */   {
/* 37:52 */     if (this._sheet == null) {
/* 38:53 */       this._sheet = this._bookEvaluator.getSheet(this._sheetIndex);
/* 39:   */     }
/* 40:55 */     return this._sheet;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean isSubTotal(int rowIndex, int columnIndex)
/* 44:   */   {
/* 45:63 */     boolean subtotal = false;
/* 46:64 */     EvaluationCell cell = getSheet().getCell(rowIndex, columnIndex);
/* 47:65 */     if ((cell != null) && (cell.getCellTypeEnum() == CellType.FORMULA))
/* 48:   */     {
/* 49:66 */       EvaluationWorkbook wb = this._bookEvaluator.getWorkbook();
/* 50:67 */       for (Ptg ptg : wb.getFormulaTokens(cell)) {
/* 51:68 */         if ((ptg instanceof FuncVarPtg))
/* 52:   */         {
/* 53:69 */           FuncVarPtg f = (FuncVarPtg)ptg;
/* 54:70 */           if ("SUBTOTAL".equals(f.getName()))
/* 55:   */           {
/* 56:71 */             subtotal = true;
/* 57:72 */             break;
/* 58:   */           }
/* 59:   */         }
/* 60:   */       }
/* 61:   */     }
/* 62:77 */     return subtotal;
/* 63:   */   }
/* 64:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SheetRefEvaluator
 * JD-Core Version:    0.7.0.1
 */