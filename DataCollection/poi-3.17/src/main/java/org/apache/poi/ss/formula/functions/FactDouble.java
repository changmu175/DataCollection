/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  6:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  8:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  9:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/* 10:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 11:   */ 
/* 12:   */ public class FactDouble
/* 13:   */   extends Fixed1ArgFunction
/* 14:   */   implements FreeRefFunction
/* 15:   */ {
/* 16:46 */   public static final FreeRefFunction instance = new FactDouble();
/* 17:49 */   static HashMap<Integer, BigInteger> cache = new HashMap();
/* 18:   */   
/* 19:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE)
/* 20:   */   {
/* 21:   */     int number;
/* 22:   */     try
/* 23:   */     {
/* 24:54 */       number = OperandResolver.coerceValueToInt(numberVE);
/* 25:   */     }
/* 26:   */     catch (EvaluationException e)
/* 27:   */     {
/* 28:56 */       return ErrorEval.VALUE_INVALID;
/* 29:   */     }
/* 30:59 */     if (number < 0) {
/* 31:60 */       return ErrorEval.NUM_ERROR;
/* 32:   */     }
/* 33:63 */     return new NumberEval(factorial(number).longValue());
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static BigInteger factorial(int n)
/* 37:   */   {
/* 38:67 */     if ((n == 0) || (n < 0)) {
/* 39:68 */       return BigInteger.ONE;
/* 40:   */     }
/* 41:71 */     if (cache.containsKey(Integer.valueOf(n))) {
/* 42:72 */       return (BigInteger)cache.get(Integer.valueOf(n));
/* 43:   */     }
/* 44:75 */     BigInteger result = BigInteger.valueOf(n).multiply(factorial(n - 2));
/* 45:76 */     cache.put(Integer.valueOf(n), result);
/* 46:77 */     return result;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 50:   */   {
/* 51:81 */     if (args.length != 1) {
/* 52:82 */       return ErrorEval.VALUE_INVALID;
/* 53:   */     }
/* 54:84 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/* 55:   */   }
/* 56:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.FactDouble
 * JD-Core Version:    0.7.0.1
 */