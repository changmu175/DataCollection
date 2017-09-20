/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  7:   */ 
/*  8:   */ public final class Npv
/*  9:   */   implements Function
/* 10:   */ {
/* 11:   */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 12:   */   {
/* 13:38 */     int nArgs = args.length;
/* 14:39 */     if (nArgs < 2) {
/* 15:40 */       return ErrorEval.VALUE_INVALID;
/* 16:   */     }
/* 17:   */     try
/* 18:   */     {
/* 19:44 */       double rate = NumericFunction.singleOperandEvaluate(args[0], srcRowIndex, srcColumnIndex);
/* 20:   */       
/* 21:46 */       ValueEval[] vargs = new ValueEval[args.length - 1];
/* 22:47 */       System.arraycopy(args, 1, vargs, 0, vargs.length);
/* 23:48 */       double[] values = AggregateFunction.ValueCollector.collectValues(vargs);
/* 24:   */       
/* 25:50 */       double result = FinanceLib.npv(rate, values);
/* 26:51 */       NumericFunction.checkValue(result);
/* 27:52 */       return new NumberEval(result);
/* 28:   */     }
/* 29:   */     catch (EvaluationException e)
/* 30:   */     {
/* 31:54 */       return e.getErrorEval();
/* 32:   */     }
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Npv
 * JD-Core Version:    0.7.0.1
 */