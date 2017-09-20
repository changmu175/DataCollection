/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.ThreeDEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.RefEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.StringEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ 
/* 10:   */ public final class Countblank
/* 11:   */   extends Fixed1ArgFunction
/* 12:   */ {
/* 13:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 14:   */   {
/* 15:   */     double result;
/* 16:40 */     if ((arg0 instanceof RefEval))
/* 17:   */     {
/* 18:41 */       result = CountUtils.countMatchingCellsInRef((RefEval)arg0, predicate);
/* 19:   */     }
/* 20:   */     else
/* 21:   */     {
/* 22:   */       double result;
/* 23:42 */       if ((arg0 instanceof ThreeDEval)) {
/* 24:43 */         result = CountUtils.countMatchingCellsInArea((ThreeDEval)arg0, predicate);
/* 25:   */       } else {
/* 26:45 */         throw new IllegalArgumentException("Bad range arg type (" + arg0.getClass().getName() + ")");
/* 27:   */       }
/* 28:   */     }
/* 29:   */     double result;
/* 30:47 */     return new NumberEval(result);
/* 31:   */   }
/* 32:   */   
/* 33:50 */   private static final CountUtils.I_MatchPredicate predicate = new CountUtils.I_MatchPredicate()
/* 34:   */   {
/* 35:   */     public boolean matches(ValueEval valueEval)
/* 36:   */     {
/* 37:54 */       return (valueEval == BlankEval.instance) || (((valueEval instanceof StringEval)) && ("".equals(((StringEval)valueEval).getStringValue())));
/* 38:   */     }
/* 39:   */   };
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Countblank
 * JD-Core Version:    0.7.0.1
 */