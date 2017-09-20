/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Errortype
/* 10:   */   extends Fixed1ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 13:   */   {
/* 14:   */     try
/* 15:   */     {
/* 16:58 */       OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 17:59 */       return ErrorEval.NA;
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:61 */       int result = translateErrorCodeToErrorTypeValue(e.getErrorEval().getErrorCode());
/* 22:62 */       return new NumberEval(result);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   private int translateErrorCodeToErrorTypeValue(int errorCode)
/* 27:   */   {
/* 28:67 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[org.apache.poi.ss.usermodel.FormulaError.forInt(errorCode).ordinal()])
/* 29:   */     {
/* 30:   */     case 1: 
/* 31:68 */       return 1;
/* 32:   */     case 2: 
/* 33:69 */       return 2;
/* 34:   */     case 3: 
/* 35:70 */       return 3;
/* 36:   */     case 4: 
/* 37:71 */       return 4;
/* 38:   */     case 5: 
/* 39:72 */       return 5;
/* 40:   */     case 6: 
/* 41:73 */       return 6;
/* 42:   */     case 7: 
/* 43:74 */       return 7;
/* 44:   */     }
/* 45:76 */     throw new IllegalArgumentException("Invalid error code (" + errorCode + ")");
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Errortype
 * JD-Core Version:    0.7.0.1
 */