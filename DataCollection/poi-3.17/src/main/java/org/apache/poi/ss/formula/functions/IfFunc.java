/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public final class IfFunc
/* 11:   */   extends Var2or3ArgFunction
/* 12:   */ {
/* 13:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 14:   */   {
/* 15:   */     boolean b;
/* 16:   */     try
/* 17:   */     {
/* 18:44 */       b = evaluateFirstArg(arg0, srcRowIndex, srcColumnIndex);
/* 19:   */     }
/* 20:   */     catch (EvaluationException e)
/* 21:   */     {
/* 22:46 */       return e.getErrorEval();
/* 23:   */     }
/* 24:48 */     if (b)
/* 25:   */     {
/* 26:49 */       if (arg1 == MissingArgEval.instance) {
/* 27:50 */         return BlankEval.instance;
/* 28:   */       }
/* 29:52 */       return arg1;
/* 30:   */     }
/* 31:54 */     return BoolEval.FALSE;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 35:   */   {
/* 36:   */     boolean b;
/* 37:   */     try
/* 38:   */     {
/* 39:61 */       b = evaluateFirstArg(arg0, srcRowIndex, srcColumnIndex);
/* 40:   */     }
/* 41:   */     catch (EvaluationException e)
/* 42:   */     {
/* 43:63 */       return e.getErrorEval();
/* 44:   */     }
/* 45:65 */     if (b)
/* 46:   */     {
/* 47:66 */       if (arg1 == MissingArgEval.instance) {
/* 48:67 */         return BlankEval.instance;
/* 49:   */       }
/* 50:69 */       return arg1;
/* 51:   */     }
/* 52:71 */     if (arg2 == MissingArgEval.instance) {
/* 53:72 */       return BlankEval.instance;
/* 54:   */     }
/* 55:74 */     return arg2;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public static boolean evaluateFirstArg(ValueEval arg, int srcCellRow, int srcCellCol)
/* 59:   */     throws EvaluationException
/* 60:   */   {
/* 61:79 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/* 62:80 */     Boolean b = OperandResolver.coerceValueToBoolean(ve, false);
/* 63:81 */     if (b == null) {
/* 64:82 */       return false;
/* 65:   */     }
/* 66:84 */     return b.booleanValue();
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.IfFunc
 * JD-Core Version:    0.7.0.1
 */