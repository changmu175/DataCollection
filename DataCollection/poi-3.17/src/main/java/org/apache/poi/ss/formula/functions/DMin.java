/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  6:   */ 
/*  7:   */ public final class DMin
/*  8:   */   implements IDStarAlgorithm
/*  9:   */ {
/* 10:   */   private ValueEval minimumValue;
/* 11:   */   
/* 12:   */   public boolean processMatch(ValueEval eval)
/* 13:   */   {
/* 14:37 */     if ((eval instanceof NumericValueEval)) {
/* 15:38 */       if (this.minimumValue == null)
/* 16:   */       {
/* 17:39 */         this.minimumValue = eval;
/* 18:   */       }
/* 19:   */       else
/* 20:   */       {
/* 21:41 */         double currentValue = ((NumericValueEval)eval).getNumberValue();
/* 22:42 */         double oldValue = ((NumericValueEval)this.minimumValue).getNumberValue();
/* 23:43 */         if (currentValue < oldValue) {
/* 24:44 */           this.minimumValue = eval;
/* 25:   */         }
/* 26:   */       }
/* 27:   */     }
/* 28:49 */     return true;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public ValueEval getResult()
/* 32:   */   {
/* 33:54 */     if (this.minimumValue == null) {
/* 34:55 */       return NumberEval.ZERO;
/* 35:   */     }
/* 36:57 */     return this.minimumValue;
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.DMin
 * JD-Core Version:    0.7.0.1
 */