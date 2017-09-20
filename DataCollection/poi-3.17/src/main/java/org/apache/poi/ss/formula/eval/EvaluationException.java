/*   1:    */ package org.apache.poi.ss.formula.eval;
/*   2:    */ 
/*   3:    */ public final class EvaluationException
/*   4:    */   extends Exception
/*   5:    */ {
/*   6:    */   private final ErrorEval _errorEval;
/*   7:    */   
/*   8:    */   public EvaluationException(ErrorEval errorEval)
/*   9:    */   {
/*  10:114 */     this._errorEval = errorEval;
/*  11:    */   }
/*  12:    */   
/*  13:    */   public static EvaluationException invalidValue()
/*  14:    */   {
/*  15:120 */     return new EvaluationException(ErrorEval.VALUE_INVALID);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static EvaluationException invalidRef()
/*  19:    */   {
/*  20:124 */     return new EvaluationException(ErrorEval.REF_INVALID);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static EvaluationException numberError()
/*  24:    */   {
/*  25:128 */     return new EvaluationException(ErrorEval.NUM_ERROR);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ErrorEval getErrorEval()
/*  29:    */   {
/*  30:132 */     return this._errorEval;
/*  31:    */   }
/*  32:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.EvaluationException
 * JD-Core Version:    0.7.0.1
 */