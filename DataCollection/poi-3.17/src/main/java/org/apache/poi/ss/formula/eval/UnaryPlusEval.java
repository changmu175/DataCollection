/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed1ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class UnaryPlusEval
/*  7:   */   extends Fixed1ArgFunction
/*  8:   */ {
/*  9:29 */   public static final Function instance = new UnaryPlusEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcCellRow, int srcCellCol, ValueEval arg0)
/* 12:   */   {
/* 13:   */     double d;
/* 14:   */     try
/* 15:   */     {
/* 16:38 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcCellRow, srcCellCol);
/* 17:39 */       if ((ve instanceof StringEval)) {
/* 18:43 */         return ve;
/* 19:   */       }
/* 20:45 */       d = OperandResolver.coerceValueToDouble(ve);
/* 21:   */     }
/* 22:   */     catch (EvaluationException e)
/* 23:   */     {
/* 24:47 */       return e.getErrorEval();
/* 25:   */     }
/* 26:49 */     return new NumberEval(d);
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.UnaryPlusEval
 * JD-Core Version:    0.7.0.1
 */