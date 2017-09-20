/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public class Quotient
/* 11:   */   extends Fixed2ArgFunction
/* 12:   */   implements FreeRefFunction
/* 13:   */ {
/* 14:42 */   public static final FreeRefFunction instance = new Quotient();
/* 15:   */   
/* 16:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval venumerator, ValueEval vedenominator)
/* 17:   */   {
/* 18:46 */     double enumerator = 0.0D;
/* 19:   */     try
/* 20:   */     {
/* 21:48 */       enumerator = OperandResolver.coerceValueToDouble(venumerator);
/* 22:   */     }
/* 23:   */     catch (EvaluationException e)
/* 24:   */     {
/* 25:50 */       return ErrorEval.VALUE_INVALID;
/* 26:   */     }
/* 27:53 */     double denominator = 0.0D;
/* 28:   */     try
/* 29:   */     {
/* 30:55 */       denominator = OperandResolver.coerceValueToDouble(vedenominator);
/* 31:   */     }
/* 32:   */     catch (EvaluationException e)
/* 33:   */     {
/* 34:57 */       return ErrorEval.VALUE_INVALID;
/* 35:   */     }
/* 36:60 */     if (denominator == 0.0D) {
/* 37:61 */       return ErrorEval.DIV_ZERO;
/* 38:   */     }
/* 39:64 */     return new NumberEval((int)(enumerator / denominator));
/* 40:   */   }
/* 41:   */   
/* 42:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 43:   */   {
/* 44:68 */     if (args.length != 2) {
/* 45:69 */       return ErrorEval.VALUE_INVALID;
/* 46:   */     }
/* 47:71 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Quotient
 * JD-Core Version:    0.7.0.1
 */