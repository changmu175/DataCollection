/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ public final class FunctionNameEval
/*  4:   */   implements ValueEval
/*  5:   */ {
/*  6:   */   private final String _functionName;
/*  7:   */   
/*  8:   */   public FunctionNameEval(String functionName)
/*  9:   */   {
/* 10:31 */     this._functionName = functionName;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public String getFunctionName()
/* 14:   */   {
/* 15:36 */     return this._functionName;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String toString()
/* 19:   */   {
/* 20:40 */     return getClass().getName() + " [" + this._functionName + "]";
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.FunctionNameEval
 * JD-Core Version:    0.7.0.1
 */