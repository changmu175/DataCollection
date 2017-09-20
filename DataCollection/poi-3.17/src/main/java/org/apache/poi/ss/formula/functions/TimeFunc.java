/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public final class TimeFunc
/* 11:   */   extends Fixed3ArgFunction
/* 12:   */ {
/* 13:   */   private static final int SECONDS_PER_MINUTE = 60;
/* 14:   */   private static final int SECONDS_PER_HOUR = 3600;
/* 15:   */   private static final int HOURS_PER_DAY = 24;
/* 16:   */   private static final int SECONDS_PER_DAY = 86400;
/* 17:   */   
/* 18:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 19:   */   {
/* 20:   */     double result;
/* 21:   */     try
/* 22:   */     {
/* 23:46 */       result = evaluate(evalArg(arg0, srcRowIndex, srcColumnIndex), evalArg(arg1, srcRowIndex, srcColumnIndex), evalArg(arg2, srcRowIndex, srcColumnIndex));
/* 24:   */     }
/* 25:   */     catch (EvaluationException e)
/* 26:   */     {
/* 27:48 */       return e.getErrorEval();
/* 28:   */     }
/* 29:50 */     return new NumberEval(result);
/* 30:   */   }
/* 31:   */   
/* 32:   */   private static int evalArg(ValueEval arg, int srcRowIndex, int srcColumnIndex)
/* 33:   */     throws EvaluationException
/* 34:   */   {
/* 35:53 */     if (arg == MissingArgEval.instance) {
/* 36:54 */       return 0;
/* 37:   */     }
/* 38:56 */     ValueEval ev = OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex);
/* 39:   */     
/* 40:58 */     return OperandResolver.coerceValueToInt(ev);
/* 41:   */   }
/* 42:   */   
/* 43:   */   private static double evaluate(int hours, int minutes, int seconds)
/* 44:   */     throws EvaluationException
/* 45:   */   {
/* 46:77 */     if ((hours > 32767) || (minutes > 32767) || (seconds > 32767)) {
/* 47:78 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 48:   */     }
/* 49:80 */     int totalSeconds = hours * 3600 + minutes * 60 + seconds;
/* 50:82 */     if (totalSeconds < 0) {
/* 51:83 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 52:   */     }
/* 53:85 */     return totalSeconds % 86400 / 86400.0D;
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.TimeFunc
 * JD-Core Version:    0.7.0.1
 */