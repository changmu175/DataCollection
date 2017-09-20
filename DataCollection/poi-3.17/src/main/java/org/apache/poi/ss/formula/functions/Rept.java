/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  6:   */ import org.apache.poi.ss.formula.eval.StringEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public class Rept
/* 10:   */   extends Fixed2ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval text, ValueEval number_times)
/* 13:   */   {
/* 14:   */     ValueEval veText1;
/* 15:   */     try
/* 16:   */     {
/* 17:50 */       veText1 = OperandResolver.getSingleValue(text, srcRowIndex, srcColumnIndex);
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:52 */       return e.getErrorEval();
/* 22:   */     }
/* 23:54 */     String strText1 = OperandResolver.coerceValueToString(veText1);
/* 24:55 */     double numberOfTime = 0.0D;
/* 25:   */     try
/* 26:   */     {
/* 27:57 */       numberOfTime = OperandResolver.coerceValueToDouble(number_times);
/* 28:   */     }
/* 29:   */     catch (EvaluationException e)
/* 30:   */     {
/* 31:59 */       return ErrorEval.VALUE_INVALID;
/* 32:   */     }
/* 33:62 */     int numberOfTimeInt = (int)numberOfTime;
/* 34:63 */     StringBuffer strb = new StringBuffer(strText1.length() * numberOfTimeInt);
/* 35:64 */     for (int i = 0; i < numberOfTimeInt; i++) {
/* 36:65 */       strb.append(strText1);
/* 37:   */     }
/* 38:68 */     if (strb.toString().length() > 32767) {
/* 39:69 */       return ErrorEval.VALUE_INVALID;
/* 40:   */     }
/* 41:72 */     return new StringEval(strb.toString());
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Rept
 * JD-Core Version:    0.7.0.1
 */