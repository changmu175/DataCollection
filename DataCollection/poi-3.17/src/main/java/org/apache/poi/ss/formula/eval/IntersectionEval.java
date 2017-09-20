/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class IntersectionEval
/*  7:   */   extends Fixed2ArgFunction
/*  8:   */ {
/*  9:28 */   public static final Function instance = new IntersectionEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 12:   */   {
/* 13:   */     try
/* 14:   */     {
/* 15:37 */       AreaEval reA = evaluateRef(arg0);
/* 16:38 */       AreaEval reB = evaluateRef(arg1);
/* 17:39 */       AreaEval result = resolveRange(reA, reB);
/* 18:40 */       if (result == null) {
/* 19:41 */         return ErrorEval.NULL_INTERSECTION;
/* 20:   */       }
/* 21:43 */       return result;
/* 22:   */     }
/* 23:   */     catch (EvaluationException e)
/* 24:   */     {
/* 25:45 */       return e.getErrorEval();
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   private static AreaEval resolveRange(AreaEval aeA, AreaEval aeB)
/* 30:   */   {
/* 31:55 */     int aeAfr = aeA.getFirstRow();
/* 32:56 */     int aeAfc = aeA.getFirstColumn();
/* 33:57 */     int aeBlc = aeB.getLastColumn();
/* 34:58 */     if (aeAfc > aeBlc) {
/* 35:59 */       return null;
/* 36:   */     }
/* 37:61 */     int aeBfc = aeB.getFirstColumn();
/* 38:62 */     if (aeBfc > aeA.getLastColumn()) {
/* 39:63 */       return null;
/* 40:   */     }
/* 41:65 */     int aeBlr = aeB.getLastRow();
/* 42:66 */     if (aeAfr > aeBlr) {
/* 43:67 */       return null;
/* 44:   */     }
/* 45:69 */     int aeBfr = aeB.getFirstRow();
/* 46:70 */     int aeAlr = aeA.getLastRow();
/* 47:71 */     if (aeBfr > aeAlr) {
/* 48:72 */       return null;
/* 49:   */     }
/* 50:76 */     int top = Math.max(aeAfr, aeBfr);
/* 51:77 */     int bottom = Math.min(aeAlr, aeBlr);
/* 52:78 */     int left = Math.max(aeAfc, aeBfc);
/* 53:79 */     int right = Math.min(aeA.getLastColumn(), aeBlc);
/* 54:   */     
/* 55:81 */     return aeA.offset(top - aeAfr, bottom - aeAfr, left - aeAfc, right - aeAfc);
/* 56:   */   }
/* 57:   */   
/* 58:   */   private static AreaEval evaluateRef(ValueEval arg)
/* 59:   */     throws EvaluationException
/* 60:   */   {
/* 61:85 */     if ((arg instanceof AreaEval)) {
/* 62:86 */       return (AreaEval)arg;
/* 63:   */     }
/* 64:88 */     if ((arg instanceof RefEval)) {
/* 65:89 */       return ((RefEval)arg).offset(0, 0, 0, 0);
/* 66:   */     }
/* 67:91 */     if ((arg instanceof ErrorEval)) {
/* 68:92 */       throw new EvaluationException((ErrorEval)arg);
/* 69:   */     }
/* 70:94 */     throw new IllegalArgumentException("Unexpected ref arg class (" + arg.getClass().getName() + ")");
/* 71:   */   }
/* 72:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.IntersectionEval
 * JD-Core Version:    0.7.0.1
 */