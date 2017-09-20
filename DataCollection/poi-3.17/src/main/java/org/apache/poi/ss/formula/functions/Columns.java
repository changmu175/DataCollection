/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.TwoDEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.RefEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Columns
/* 10:   */   extends Fixed1ArgFunction
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 13:   */   {
/* 14:   */     int result;
/* 15:36 */     if ((arg0 instanceof TwoDEval))
/* 16:   */     {
/* 17:37 */       result = ((TwoDEval)arg0).getWidth();
/* 18:   */     }
/* 19:   */     else
/* 20:   */     {
/* 21:   */       int result;
/* 22:38 */       if ((arg0 instanceof RefEval)) {
/* 23:39 */         result = 1;
/* 24:   */       } else {
/* 25:41 */         return ErrorEval.VALUE_INVALID;
/* 26:   */       }
/* 27:   */     }
/* 28:   */     int result;
/* 29:43 */     return new NumberEval(result);
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Columns
 * JD-Core Version:    0.7.0.1
 */