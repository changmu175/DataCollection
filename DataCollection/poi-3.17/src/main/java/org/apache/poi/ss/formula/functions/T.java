/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.AreaEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.RefEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.StringEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class T
/* 10:   */   extends Fixed1ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 13:   */   {
/* 14:36 */     ValueEval arg = arg0;
/* 15:37 */     if ((arg instanceof RefEval))
/* 16:   */     {
/* 17:39 */       RefEval re = (RefEval)arg;
/* 18:40 */       arg = re.getInnerValueEval(re.getFirstSheetIndex());
/* 19:   */     }
/* 20:41 */     else if ((arg instanceof AreaEval))
/* 21:   */     {
/* 22:43 */       arg = ((AreaEval)arg).getRelativeValue(0, 0);
/* 23:   */     }
/* 24:46 */     if ((arg instanceof StringEval)) {
/* 25:48 */       return arg;
/* 26:   */     }
/* 27:51 */     if ((arg instanceof ErrorEval)) {
/* 28:53 */       return arg;
/* 29:   */     }
/* 30:56 */     return StringEval.EMPTY_INSTANCE;
/* 31:   */   }
/* 32:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.T
 * JD-Core Version:    0.7.0.1
 */