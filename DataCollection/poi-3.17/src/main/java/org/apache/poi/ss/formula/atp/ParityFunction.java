/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/* 10:   */ 
/* 11:   */ final class ParityFunction
/* 12:   */   implements FreeRefFunction
/* 13:   */ {
/* 14:34 */   public static final FreeRefFunction IS_EVEN = new ParityFunction(0);
/* 15:35 */   public static final FreeRefFunction IS_ODD = new ParityFunction(1);
/* 16:   */   private final int _desiredParity;
/* 17:   */   
/* 18:   */   private ParityFunction(int desiredParity)
/* 19:   */   {
/* 20:39 */     this._desiredParity = desiredParity;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 24:   */   {
/* 25:43 */     if (args.length != 1) {
/* 26:44 */       return ErrorEval.VALUE_INVALID;
/* 27:   */     }
/* 28:   */     int val;
/* 29:   */     try
/* 30:   */     {
/* 31:49 */       val = evaluateArgParity(args[0], ec.getRowIndex(), ec.getColumnIndex());
/* 32:   */     }
/* 33:   */     catch (EvaluationException e)
/* 34:   */     {
/* 35:51 */       return e.getErrorEval();
/* 36:   */     }
/* 37:54 */     return BoolEval.valueOf(val == this._desiredParity);
/* 38:   */   }
/* 39:   */   
/* 40:   */   private static int evaluateArgParity(ValueEval arg, int srcCellRow, int srcCellCol)
/* 41:   */     throws EvaluationException
/* 42:   */   {
/* 43:58 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short)srcCellCol);
/* 44:   */     
/* 45:60 */     double d = OperandResolver.coerceValueToDouble(ve);
/* 46:61 */     if (d < 0.0D) {
/* 47:62 */       d = -d;
/* 48:   */     }
/* 49:64 */     long v = Math.floor(d);
/* 50:65 */     return (int)(v & 1L);
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.ParityFunction
 * JD-Core Version:    0.7.0.1
 */