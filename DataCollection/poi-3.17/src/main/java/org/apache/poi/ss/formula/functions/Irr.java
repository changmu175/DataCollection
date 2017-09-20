/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   7:    */ 
/*   8:    */ public final class Irr
/*   9:    */   implements Function
/*  10:    */ {
/*  11:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  12:    */   {
/*  13: 34 */     if ((args.length == 0) || (args.length > 2)) {
/*  14: 36 */       return ErrorEval.VALUE_INVALID;
/*  15:    */     }
/*  16:    */     try
/*  17:    */     {
/*  18: 40 */       double[] values = AggregateFunction.ValueCollector.collectValues(new ValueEval[] { args[0] });
/*  19:    */       double guess;
/*  20:    */       double guess;
/*  21: 42 */       if (args.length == 2) {
/*  22: 43 */         guess = NumericFunction.singleOperandEvaluate(args[1], srcRowIndex, srcColumnIndex);
/*  23:    */       } else {
/*  24: 45 */         guess = 0.1D;
/*  25:    */       }
/*  26: 47 */       double result = irr(values, guess);
/*  27: 48 */       NumericFunction.checkValue(result);
/*  28: 49 */       return new NumberEval(result);
/*  29:    */     }
/*  30:    */     catch (EvaluationException e)
/*  31:    */     {
/*  32: 51 */       return e.getErrorEval();
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static double irr(double[] income)
/*  37:    */   {
/*  38: 62 */     return irr(income, 0.1D);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static double irr(double[] values, double guess)
/*  42:    */   {
/*  43: 89 */     int maxIterationCount = 20;
/*  44: 90 */     double absoluteAccuracy = 1.0E-007D;
/*  45:    */     
/*  46: 92 */     double x0 = guess;
/*  47:    */     
/*  48:    */ 
/*  49: 95 */     int i = 0;
/*  50: 96 */     while (i < 20)
/*  51:    */     {
/*  52: 99 */       double factor = 1.0D + x0;
/*  53:100 */       int k = 0;
/*  54:101 */       double fValue = values[k];
/*  55:102 */       double fDerivative = 0.0D;
/*  56:103 */       double denominator = factor;
/*  57:    */       for (;;)
/*  58:    */       {
/*  59:103 */         k++;
/*  60:103 */         if (k >= values.length) {
/*  61:    */           break;
/*  62:    */         }
/*  63:104 */         double value = values[k];
/*  64:105 */         fValue += value / denominator;
/*  65:106 */         denominator *= factor;
/*  66:107 */         fDerivative -= k * value / denominator;
/*  67:    */       }
/*  68:111 */       double x1 = x0 - fValue / fDerivative;
/*  69:113 */       if (Math.abs(x1 - x0) <= 1.0E-007D) {
/*  70:114 */         return x1;
/*  71:    */       }
/*  72:117 */       x0 = x1;
/*  73:118 */       i++;
/*  74:    */     }
/*  75:121 */     return (0.0D / 0.0D);
/*  76:    */   }
/*  77:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Irr
 * JD-Core Version:    0.7.0.1
 */