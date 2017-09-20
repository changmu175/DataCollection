/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class DGet
/* 10:   */   implements IDStarAlgorithm
/* 11:   */ {
/* 12:   */   private ValueEval result;
/* 13:   */   
/* 14:   */   public boolean processMatch(ValueEval eval)
/* 15:   */   {
/* 16:35 */     if (this.result == null)
/* 17:   */     {
/* 18:37 */       this.result = eval;
/* 19:   */     }
/* 20:41 */     else if ((this.result instanceof BlankEval))
/* 21:   */     {
/* 22:42 */       this.result = eval;
/* 23:   */     }
/* 24:46 */     else if (!(eval instanceof BlankEval))
/* 25:   */     {
/* 26:47 */       this.result = ErrorEval.NUM_ERROR;
/* 27:48 */       return false;
/* 28:   */     }
/* 29:53 */     return true;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public ValueEval getResult()
/* 33:   */   {
/* 34:58 */     if (this.result == null) {
/* 35:59 */       return ErrorEval.VALUE_INVALID;
/* 36:   */     }
/* 37:60 */     if ((this.result instanceof BlankEval)) {
/* 38:61 */       return ErrorEval.VALUE_INVALID;
/* 39:   */     }
/* 40:   */     try
/* 41:   */     {
/* 42:64 */       if (OperandResolver.coerceValueToString(OperandResolver.getSingleValue(this.result, 0, 0)).equals("")) {
/* 43:65 */         return ErrorEval.VALUE_INVALID;
/* 44:   */       }
/* 45:68 */       return this.result;
/* 46:   */     }
/* 47:   */     catch (EvaluationException e)
/* 48:   */     {
/* 49:71 */       return e.getErrorEval();
/* 50:   */     }
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.DGet
 * JD-Core Version:    0.7.0.1
 */