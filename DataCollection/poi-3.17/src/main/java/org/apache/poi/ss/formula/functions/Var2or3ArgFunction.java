/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  5:   */ 
/*  6:   */ abstract class Var2or3ArgFunction
/*  7:   */   implements Function2Arg, Function3Arg
/*  8:   */ {
/*  9:   */   public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 10:   */   {
/* 11:32 */     switch (args.length)
/* 12:   */     {
/* 13:   */     case 2: 
/* 14:34 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
/* 15:   */     case 3: 
/* 16:36 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
/* 17:   */     }
/* 18:38 */     return ErrorEval.VALUE_INVALID;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Var2or3ArgFunction
 * JD-Core Version:    0.7.0.1
 */