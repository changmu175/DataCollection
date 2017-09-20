/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  4:   */ 
/*  5:   */ public final class Slope
/*  6:   */   extends Fixed2ArgFunction
/*  7:   */ {
/*  8:   */   private final LinearRegressionFunction func;
/*  9:   */   
/* 10:   */   public Slope()
/* 11:   */   {
/* 12:40 */     this.func = new LinearRegressionFunction(LinearRegressionFunction.FUNCTION.SLOPE);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 16:   */   {
/* 17:45 */     return this.func.evaluate(srcRowIndex, srcColumnIndex, arg0, arg1);
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Slope
 * JD-Core Version:    0.7.0.1
 */