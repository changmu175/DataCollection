/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.WorkbookEvaluator;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  9:   */ 
/* 10:   */ final class IfError
/* 11:   */   implements FreeRefFunction
/* 12:   */ {
/* 13:38 */   public static final FreeRefFunction instance = new IfError();
/* 14:   */   
/* 15:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 16:   */   {
/* 17:45 */     if (args.length != 2) {
/* 18:46 */       return ErrorEval.VALUE_INVALID;
/* 19:   */     }
/* 20:   */     ValueEval val;
/* 21:   */     try
/* 22:   */     {
/* 23:51 */       val = evaluateInternal(args[0], args[1], ec.getRowIndex(), ec.getColumnIndex());
/* 24:   */     }
/* 25:   */     catch (EvaluationException e)
/* 26:   */     {
/* 27:53 */       return e.getErrorEval();
/* 28:   */     }
/* 29:56 */     return val;
/* 30:   */   }
/* 31:   */   
/* 32:   */   private static ValueEval evaluateInternal(ValueEval arg, ValueEval iferror, int srcCellRow, int srcCellCol)
/* 33:   */     throws EvaluationException
/* 34:   */   {
/* 35:60 */     arg = WorkbookEvaluator.dereferenceResult(arg, srcCellRow, srcCellCol);
/* 36:61 */     if ((arg instanceof ErrorEval)) {
/* 37:62 */       return iferror;
/* 38:   */     }
/* 39:64 */     return arg;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.IfError
 * JD-Core Version:    0.7.0.1
 */