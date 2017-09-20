/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  5:   */ 
/*  6:   */ public final class Na
/*  7:   */   extends Fixed0ArgFunction
/*  8:   */ {
/*  9:   */   public ValueEval evaluate(int srcCellRow, int srcCellCol)
/* 10:   */   {
/* 11:31 */     return ErrorEval.NA;
/* 12:   */   }
/* 13:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Na
 * JD-Core Version:    0.7.0.1
 */