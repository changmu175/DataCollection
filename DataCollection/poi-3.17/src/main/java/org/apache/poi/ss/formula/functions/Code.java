/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  6:   */ import org.apache.poi.ss.formula.eval.StringEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public class Code
/* 10:   */   extends Fixed1ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval textArg)
/* 13:   */   {
/* 14:   */     ValueEval veText1;
/* 15:   */     try
/* 16:   */     {
/* 17:39 */       veText1 = OperandResolver.getSingleValue(textArg, srcRowIndex, srcColumnIndex);
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:41 */       return e.getErrorEval();
/* 22:   */     }
/* 23:43 */     String text = OperandResolver.coerceValueToString(veText1);
/* 24:45 */     if (text.length() == 0) {
/* 25:46 */       return ErrorEval.VALUE_INVALID;
/* 26:   */     }
/* 27:49 */     int code = text.charAt(0);
/* 28:   */     
/* 29:51 */     return new StringEval(String.valueOf(code));
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Code
 * JD-Core Version:    0.7.0.1
 */