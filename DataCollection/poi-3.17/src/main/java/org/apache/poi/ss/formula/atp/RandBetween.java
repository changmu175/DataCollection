/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/* 10:   */ 
/* 11:   */ final class RandBetween
/* 12:   */   implements FreeRefFunction
/* 13:   */ {
/* 14:42 */   public static final FreeRefFunction instance = new RandBetween();
/* 15:   */   
/* 16:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 17:   */   {
/* 18:59 */     if (args.length != 2) {
/* 19:60 */       return ErrorEval.VALUE_INVALID;
/* 20:   */     }
/* 21:   */     try
/* 22:   */     {
/* 23:64 */       bottom = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
/* 24:65 */       top = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[1], ec.getRowIndex(), ec.getColumnIndex()));
/* 25:66 */       if (bottom > top) {
/* 26:67 */         return ErrorEval.NUM_ERROR;
/* 27:   */       }
/* 28:   */     }
/* 29:   */     catch (EvaluationException e)
/* 30:   */     {
/* 31:70 */       return ErrorEval.VALUE_INVALID;
/* 32:   */     }
/* 33:73 */     double bottom = Math.ceil(bottom);
/* 34:74 */     double top = Math.floor(top);
/* 35:76 */     if (bottom > top) {
/* 36:77 */       top = bottom;
/* 37:   */     }
/* 38:80 */     return new NumberEval(bottom + (int)(Math.random() * (top - bottom + 1.0D)));
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.RandBetween
 * JD-Core Version:    0.7.0.1
 */