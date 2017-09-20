/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ public final class NotImplementedFunctionException
/*  4:   */   extends NotImplementedException
/*  5:   */ {
/*  6:   */   private static final long serialVersionUID = 1208119411557559057L;
/*  7:   */   private String functionName;
/*  8:   */   
/*  9:   */   public NotImplementedFunctionException(String functionName)
/* 10:   */   {
/* 11:33 */     super(functionName);
/* 12:34 */     this.functionName = functionName;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public NotImplementedFunctionException(String functionName, NotImplementedException cause)
/* 16:   */   {
/* 17:37 */     super(functionName, cause);
/* 18:38 */     this.functionName = functionName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getFunctionName()
/* 22:   */   {
/* 23:42 */     return this.functionName;
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.NotImplementedFunctionException
 * JD-Core Version:    0.7.0.1
 */