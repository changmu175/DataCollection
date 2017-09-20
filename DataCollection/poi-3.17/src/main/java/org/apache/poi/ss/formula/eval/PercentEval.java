/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed1ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class PercentEval
/*  7:   */   extends Fixed1ArgFunction
/*  8:   */ {
/*  9:30 */   public static final Function instance = new PercentEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 12:   */   {
/* 13:   */     double d;
/* 14:   */     try
/* 15:   */     {
/* 16:39 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 17:40 */       d = OperandResolver.coerceValueToDouble(ve);
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:42 */       return e.getErrorEval();
/* 22:   */     }
/* 23:44 */     if (d == 0.0D) {
/* 24:45 */       return NumberEval.ZERO;
/* 25:   */     }
/* 26:47 */     return new NumberEval(d / 100.0D);
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.PercentEval
 * JD-Core Version:    0.7.0.1
 */