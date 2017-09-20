/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.math.BigDecimal;
/*  4:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  7:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  9:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 10:   */ 
/* 11:   */ public final class Delta
/* 12:   */   extends Fixed2ArgFunction
/* 13:   */   implements FreeRefFunction
/* 14:   */ {
/* 15:43 */   public static final FreeRefFunction instance = new Delta();
/* 16:45 */   private static final NumberEval ONE = new NumberEval(1.0D);
/* 17:46 */   private static final NumberEval ZERO = new NumberEval(0.0D);
/* 18:   */   
/* 19:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg1, ValueEval arg2)
/* 20:   */   {
/* 21:   */     ValueEval veText1;
/* 22:   */     try
/* 23:   */     {
/* 24:51 */       veText1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
/* 25:   */     }
/* 26:   */     catch (EvaluationException e)
/* 27:   */     {
/* 28:53 */       return e.getErrorEval();
/* 29:   */     }
/* 30:55 */     String strText1 = OperandResolver.coerceValueToString(veText1);
/* 31:56 */     Double number1 = OperandResolver.parseDouble(strText1);
/* 32:57 */     if (number1 == null) {
/* 33:58 */       return ErrorEval.VALUE_INVALID;
/* 34:   */     }
/* 35:   */     ValueEval veText2;
/* 36:   */     try
/* 37:   */     {
/* 38:63 */       veText2 = OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex);
/* 39:   */     }
/* 40:   */     catch (EvaluationException e)
/* 41:   */     {
/* 42:65 */       return e.getErrorEval();
/* 43:   */     }
/* 44:68 */     String strText2 = OperandResolver.coerceValueToString(veText2);
/* 45:69 */     Double number2 = OperandResolver.parseDouble(strText2);
/* 46:70 */     if (number2 == null) {
/* 47:71 */       return ErrorEval.VALUE_INVALID;
/* 48:   */     }
/* 49:74 */     int result = new BigDecimal(number1.doubleValue()).compareTo(new BigDecimal(number2.doubleValue()));
/* 50:75 */     return result == 0 ? ONE : ZERO;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 54:   */   {
/* 55:79 */     if (args.length == 2) {
/* 56:80 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/* 57:   */     }
/* 58:83 */     return ErrorEval.VALUE_INVALID;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Delta
 * JD-Core Version:    0.7.0.1
 */