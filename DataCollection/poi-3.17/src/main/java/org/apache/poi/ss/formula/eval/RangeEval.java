/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class RangeEval
/*  7:   */   extends Fixed2ArgFunction
/*  8:   */ {
/*  9:30 */   public static final Function instance = new RangeEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 12:   */   {
/* 13:   */     try
/* 14:   */     {
/* 15:39 */       AreaEval reA = evaluateRef(arg0);
/* 16:40 */       AreaEval reB = evaluateRef(arg1);
/* 17:41 */       return resolveRange(reA, reB);
/* 18:   */     }
/* 19:   */     catch (EvaluationException e)
/* 20:   */     {
/* 21:43 */       return e.getErrorEval();
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   private static AreaEval resolveRange(AreaEval aeA, AreaEval aeB)
/* 26:   */   {
/* 27:52 */     int aeAfr = aeA.getFirstRow();
/* 28:53 */     int aeAfc = aeA.getFirstColumn();
/* 29:   */     
/* 30:55 */     int top = Math.min(aeAfr, aeB.getFirstRow());
/* 31:56 */     int bottom = Math.max(aeA.getLastRow(), aeB.getLastRow());
/* 32:57 */     int left = Math.min(aeAfc, aeB.getFirstColumn());
/* 33:58 */     int right = Math.max(aeA.getLastColumn(), aeB.getLastColumn());
/* 34:   */     
/* 35:60 */     return aeA.offset(top - aeAfr, bottom - aeAfr, left - aeAfc, right - aeAfc);
/* 36:   */   }
/* 37:   */   
/* 38:   */   private static AreaEval evaluateRef(ValueEval arg)
/* 39:   */     throws EvaluationException
/* 40:   */   {
/* 41:64 */     if ((arg instanceof AreaEval)) {
/* 42:65 */       return (AreaEval)arg;
/* 43:   */     }
/* 44:67 */     if ((arg instanceof RefEval)) {
/* 45:68 */       return ((RefEval)arg).offset(0, 0, 0, 0);
/* 46:   */     }
/* 47:70 */     if ((arg instanceof ErrorEval)) {
/* 48:71 */       throw new EvaluationException((ErrorEval)arg);
/* 49:   */     }
/* 50:73 */     throw new IllegalArgumentException("Unexpected ref arg class (" + arg.getClass().getName() + ")");
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.RangeEval
 * JD-Core Version:    0.7.0.1
 */