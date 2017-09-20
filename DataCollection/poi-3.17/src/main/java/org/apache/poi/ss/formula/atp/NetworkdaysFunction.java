/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  9:   */ 
/* 10:   */ final class NetworkdaysFunction
/* 11:   */   implements FreeRefFunction
/* 12:   */ {
/* 13:41 */   public static final FreeRefFunction instance = new NetworkdaysFunction(ArgumentsEvaluator.instance);
/* 14:   */   private ArgumentsEvaluator evaluator;
/* 15:   */   
/* 16:   */   private NetworkdaysFunction(ArgumentsEvaluator anEvaluator)
/* 17:   */   {
/* 18:52 */     this.evaluator = anEvaluator;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 22:   */   {
/* 23:62 */     if ((args.length < 2) || (args.length > 3)) {
/* 24:63 */       return ErrorEval.VALUE_INVALID;
/* 25:   */     }
/* 26:66 */     int srcCellRow = ec.getRowIndex();
/* 27:67 */     int srcCellCol = ec.getColumnIndex();
/* 28:   */     try
/* 29:   */     {
/* 30:72 */       double start = this.evaluator.evaluateDateArg(args[0], srcCellRow, srcCellCol);
/* 31:73 */       double end = this.evaluator.evaluateDateArg(args[1], srcCellRow, srcCellCol);
/* 32:74 */       if (start > end) {
/* 33:75 */         return ErrorEval.NAME_INVALID;
/* 34:   */       }
/* 35:77 */       ValueEval holidaysCell = args.length == 3 ? args[2] : null;
/* 36:78 */       double[] holidays = this.evaluator.evaluateDatesArg(holidaysCell, srcCellRow, srcCellCol);
/* 37:79 */       return new NumberEval(WorkdayCalculator.instance.calculateWorkdays(start, end, holidays));
/* 38:   */     }
/* 39:   */     catch (EvaluationException e) {}
/* 40:81 */     return ErrorEval.VALUE_INVALID;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.NetworkdaysFunction
 * JD-Core Version:    0.7.0.1
 */