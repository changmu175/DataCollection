/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  4:   */ 
/*  5:   */ public final class Hyperlink
/*  6:   */   extends Var1or2ArgFunction
/*  7:   */ {
/*  8:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  9:   */   {
/* 10:41 */     return arg0;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 14:   */   {
/* 15:46 */     return arg1;
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Hyperlink
 * JD-Core Version:    0.7.0.1
 */