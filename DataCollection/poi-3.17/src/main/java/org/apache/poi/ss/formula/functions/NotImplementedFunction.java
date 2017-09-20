/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
/*  4:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  5:   */ 
/*  6:   */ public final class NotImplementedFunction
/*  7:   */   implements Function
/*  8:   */ {
/*  9:   */   private final String _functionName;
/* 10:   */   
/* 11:   */   protected NotImplementedFunction()
/* 12:   */   {
/* 13:33 */     this._functionName = getClass().getName();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public NotImplementedFunction(String name)
/* 17:   */   {
/* 18:36 */     this._functionName = name;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ValueEval evaluate(ValueEval[] operands, int srcRow, int srcCol)
/* 22:   */   {
/* 23:40 */     throw new NotImplementedFunctionException(this._functionName);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getFunctionName()
/* 27:   */   {
/* 28:43 */     return this._functionName;
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.NotImplementedFunction
 * JD-Core Version:    0.7.0.1
 */