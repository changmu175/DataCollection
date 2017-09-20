/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  9:   */ import org.apache.poi.ss.usermodel.DateUtil;
/* 10:   */ 
/* 11:   */ final class WorkdayFunction
/* 12:   */   implements FreeRefFunction
/* 13:   */ {
/* 14:41 */   public static final FreeRefFunction instance = new WorkdayFunction(ArgumentsEvaluator.instance);
/* 15:   */   private ArgumentsEvaluator evaluator;
/* 16:   */   
/* 17:   */   private WorkdayFunction(ArgumentsEvaluator anEvaluator)
/* 18:   */   {
/* 19:47 */     this.evaluator = anEvaluator;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 23:   */   {
/* 24:57 */     if ((args.length < 2) || (args.length > 3)) {
/* 25:58 */       return ErrorEval.VALUE_INVALID;
/* 26:   */     }
/* 27:61 */     int srcCellRow = ec.getRowIndex();
/* 28:62 */     int srcCellCol = ec.getColumnIndex();
/* 29:   */     try
/* 30:   */     {
/* 31:68 */       double start = this.evaluator.evaluateDateArg(args[0], srcCellRow, srcCellCol);
/* 32:69 */       int days = (int)Math.floor(this.evaluator.evaluateNumberArg(args[1], srcCellRow, srcCellCol));
/* 33:70 */       ValueEval holidaysCell = args.length == 3 ? args[2] : null;
/* 34:71 */       double[] holidays = this.evaluator.evaluateDatesArg(holidaysCell, srcCellRow, srcCellCol);
/* 35:72 */       return new NumberEval(DateUtil.getExcelDate(WorkdayCalculator.instance.calculateWorkdays(start, days, holidays)));
/* 36:   */     }
/* 37:   */     catch (EvaluationException e) {}
/* 38:74 */     return ErrorEval.VALUE_INVALID;
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.WorkdayFunction
 * JD-Core Version:    0.7.0.1
 */