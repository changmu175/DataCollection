/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.EvaluationName;
/*  4:   */ 
/*  5:   */ public final class ExternalNameEval
/*  6:   */   implements ValueEval
/*  7:   */ {
/*  8:   */   private final EvaluationName _name;
/*  9:   */   
/* 10:   */   public ExternalNameEval(EvaluationName name)
/* 11:   */   {
/* 12:29 */     this._name = name;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public EvaluationName getName()
/* 16:   */   {
/* 17:33 */     return this._name;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String toString()
/* 21:   */   {
/* 22:37 */     return getClass().getName() + " [" + this._name.getNameText() + "]";
/* 23:   */   }
/* 24:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.ExternalNameEval
 * JD-Core Version:    0.7.0.1
 */