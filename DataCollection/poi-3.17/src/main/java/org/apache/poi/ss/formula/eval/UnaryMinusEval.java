/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed1ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class UnaryMinusEval
/*  7:   */   extends Fixed1ArgFunction
/*  8:   */ {
/*  9:28 */   public static final Function instance = new UnaryMinusEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 12:   */   {
/* 13:   */     double d;
/* 14:   */     try
/* 15:   */     {
/* 16:37 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 17:38 */       d = OperandResolver.coerceValueToDouble(ve);
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:40 */       return e.getErrorEval();
/* 22:   */     }
/* 23:42 */     if (d == 0.0D) {
/* 24:43 */       return NumberEval.ZERO;
/* 25:   */     }
/* 26:45 */     return new NumberEval(-d);
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.UnaryMinusEval
 * JD-Core Version:    0.7.0.1
 */