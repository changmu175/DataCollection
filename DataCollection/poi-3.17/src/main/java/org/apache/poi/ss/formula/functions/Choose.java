/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public final class Choose
/* 11:   */   implements Function
/* 12:   */ {
/* 13:   */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 14:   */   {
/* 15:33 */     if (args.length < 2) {
/* 16:34 */       return ErrorEval.VALUE_INVALID;
/* 17:   */     }
/* 18:   */     try
/* 19:   */     {
/* 20:38 */       int ix = evaluateFirstArg(args[0], srcRowIndex, srcColumnIndex);
/* 21:39 */       if ((ix < 1) || (ix >= args.length)) {
/* 22:40 */         return ErrorEval.VALUE_INVALID;
/* 23:   */       }
/* 24:42 */       ValueEval result = OperandResolver.getSingleValue(args[ix], srcRowIndex, srcColumnIndex);
/* 25:43 */       if (result == MissingArgEval.instance) {
/* 26:44 */         return BlankEval.instance;
/* 27:   */       }
/* 28:46 */       return result;
/* 29:   */     }
/* 30:   */     catch (EvaluationException e)
/* 31:   */     {
/* 32:48 */       return e.getErrorEval();
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static int evaluateFirstArg(ValueEval arg0, int srcRowIndex, int srcColumnIndex)
/* 37:   */     throws EvaluationException
/* 38:   */   {
/* 39:54 */     ValueEval ev = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 40:55 */     return OperandResolver.coerceValueToInt(ev);
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Choose
 * JD-Core Version:    0.7.0.1
 */