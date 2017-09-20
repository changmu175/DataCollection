/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public abstract class TwoOperandNumericOperation
/*  7:   */   extends Fixed2ArgFunction
/*  8:   */ {
/*  9:   */   protected final double singleOperandEvaluate(ValueEval arg, int srcCellRow, int srcCellCol)
/* 10:   */     throws EvaluationException
/* 11:   */   {
/* 12:29 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/* 13:30 */     return OperandResolver.coerceValueToDouble(ve);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 17:   */   {
/* 18:   */     double result;
/* 19:   */     try
/* 20:   */     {
/* 21:35 */       double d0 = singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 22:36 */       double d1 = singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/* 23:37 */       result = evaluate(d0, d1);
/* 24:38 */       if (result == 0.0D) {
/* 25:40 */         if (!(this instanceof SubtractEvalClass)) {
/* 26:41 */           return NumberEval.ZERO;
/* 27:   */         }
/* 28:   */       }
/* 29:44 */       if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/* 30:45 */         return ErrorEval.NUM_ERROR;
/* 31:   */       }
/* 32:   */     }
/* 33:   */     catch (EvaluationException e)
/* 34:   */     {
/* 35:48 */       return e.getErrorEval();
/* 36:   */     }
/* 37:50 */     return new NumberEval(result);
/* 38:   */   }
/* 39:   */   
/* 40:55 */   public static final Function AddEval = new TwoOperandNumericOperation()
/* 41:   */   {
/* 42:   */     protected double evaluate(double d0, double d1)
/* 43:   */     {
/* 44:57 */       return d0 + d1;
/* 45:   */     }
/* 46:   */   };
/* 47:60 */   public static final Function DivideEval = new TwoOperandNumericOperation()
/* 48:   */   {
/* 49:   */     protected double evaluate(double d0, double d1)
/* 50:   */       throws EvaluationException
/* 51:   */     {
/* 52:62 */       if (d1 == 0.0D) {
/* 53:63 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 54:   */       }
/* 55:65 */       return d0 / d1;
/* 56:   */     }
/* 57:   */   };
/* 58:68 */   public static final Function MultiplyEval = new TwoOperandNumericOperation()
/* 59:   */   {
/* 60:   */     protected double evaluate(double d0, double d1)
/* 61:   */     {
/* 62:70 */       return d0 * d1;
/* 63:   */     }
/* 64:   */   };
/* 65:73 */   public static final Function PowerEval = new TwoOperandNumericOperation()
/* 66:   */   {
/* 67:   */     protected double evaluate(double d0, double d1)
/* 68:   */     {
/* 69:75 */       return Math.pow(d0, d1);
/* 70:   */     }
/* 71:   */   };
/* 72:   */   
/* 73:   */   protected abstract double evaluate(double paramDouble1, double paramDouble2)
/* 74:   */     throws EvaluationException;
/* 75:   */   
/* 76:   */   private static final class SubtractEvalClass
/* 77:   */     extends TwoOperandNumericOperation
/* 78:   */   {
/* 79:   */     protected double evaluate(double d0, double d1)
/* 80:   */     {
/* 81:83 */       return d0 - d1;
/* 82:   */     }
/* 83:   */   }
/* 84:   */   
/* 85:86 */   public static final Function SubtractEval = new SubtractEvalClass();
/* 86:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.TwoOperandNumericOperation
 * JD-Core Version:    0.7.0.1
 */