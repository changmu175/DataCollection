/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  5:   */ 
/*  6:   */ public abstract class Fixed2ArgFunction
/*  7:   */   implements Function2Arg
/*  8:   */ {
/*  9:   */   public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 10:   */   {
/* 11:30 */     if (args.length != 2) {
/* 12:31 */       return ErrorEval.VALUE_INVALID;
/* 13:   */     }
/* 14:33 */     return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Fixed2ArgFunction
 * JD-Core Version:    0.7.0.1
 */