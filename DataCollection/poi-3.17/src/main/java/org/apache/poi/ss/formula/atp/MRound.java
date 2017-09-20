/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/* 10:   */ import org.apache.poi.ss.formula.functions.NumericFunction;
/* 11:   */ 
/* 12:   */ final class MRound
/* 13:   */   implements FreeRefFunction
/* 14:   */ {
/* 15:39 */   public static final FreeRefFunction instance = new MRound();
/* 16:   */   
/* 17:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 18:   */   {
/* 19:48 */     if (args.length != 2) {
/* 20:49 */       return ErrorEval.VALUE_INVALID;
/* 21:   */     }
/* 22:   */     try
/* 23:   */     {
/* 24:53 */       double number = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
/* 25:54 */       double multiple = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[1], ec.getRowIndex(), ec.getColumnIndex()));
/* 26:   */       double result;
/* 27:   */       double result;
/* 28:56 */       if (multiple == 0.0D)
/* 29:   */       {
/* 30:57 */         result = 0.0D;
/* 31:   */       }
/* 32:   */       else
/* 33:   */       {
/* 34:59 */         if (number * multiple < 0.0D) {
/* 35:61 */           throw new EvaluationException(ErrorEval.NUM_ERROR);
/* 36:   */         }
/* 37:63 */         result = multiple * Math.round(number / multiple);
/* 38:   */       }
/* 39:65 */       NumericFunction.checkValue(result);
/* 40:66 */       return new NumberEval(result);
/* 41:   */     }
/* 42:   */     catch (EvaluationException e)
/* 43:   */     {
/* 44:68 */       return e.getErrorEval();
/* 45:   */     }
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.MRound
 * JD-Core Version:    0.7.0.1
 */