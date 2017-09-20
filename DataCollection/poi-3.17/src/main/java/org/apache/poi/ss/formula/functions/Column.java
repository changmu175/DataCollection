/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.AreaEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.RefEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  8:   */ 
/*  9:   */ public final class Column
/* 10:   */   implements Function0Arg, Function1Arg
/* 11:   */ {
/* 12:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 13:   */   {
/* 14:29 */     return new NumberEval(srcColumnIndex + 1);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 18:   */   {
/* 19:   */     int rnum;
/* 20:34 */     if ((arg0 instanceof AreaEval))
/* 21:   */     {
/* 22:35 */       rnum = ((AreaEval)arg0).getFirstColumn();
/* 23:   */     }
/* 24:   */     else
/* 25:   */     {
/* 26:   */       int rnum;
/* 27:36 */       if ((arg0 instanceof RefEval)) {
/* 28:37 */         rnum = ((RefEval)arg0).getColumn();
/* 29:   */       } else {
/* 30:40 */         return ErrorEval.VALUE_INVALID;
/* 31:   */       }
/* 32:   */     }
/* 33:   */     int rnum;
/* 34:43 */     return new NumberEval(rnum + 1);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/* 38:   */   {
/* 39:46 */     switch (args.length)
/* 40:   */     {
/* 41:   */     case 1: 
/* 42:48 */       return evaluate(srcRowIndex, srcColumnIndex, args[0]);
/* 43:   */     case 0: 
/* 44:50 */       return new NumberEval(srcColumnIndex + 1);
/* 45:   */     }
/* 46:52 */     return ErrorEval.VALUE_INVALID;
/* 47:   */   }
/* 48:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Column
 * JD-Core Version:    0.7.0.1
 */