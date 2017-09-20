/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  5:   */ 
/*  6:   */ public abstract class Fixed3ArgFunction
/*  7:   */   implements Function3Arg
/*  8:   */ {
/*  9:   */   public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 10:   */   {
/* 11:30 */     if (args.length != 3) {
/* 12:31 */       return ErrorEval.VALUE_INVALID;
/* 13:   */     }
/* 14:33 */     return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Fixed3ArgFunction
 * JD-Core Version:    0.7.0.1
 */