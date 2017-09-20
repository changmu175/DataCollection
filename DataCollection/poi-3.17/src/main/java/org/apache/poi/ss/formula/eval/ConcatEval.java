/*  1:   */ package org.apache.poi.ss.formula.eval;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.functions.Fixed2ArgFunction;
/*  4:   */ import org.apache.poi.ss.formula.functions.Function;
/*  5:   */ 
/*  6:   */ public final class ConcatEval
/*  7:   */   extends Fixed2ArgFunction
/*  8:   */ {
/*  9:28 */   public static final Function instance = new ConcatEval();
/* 10:   */   
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 12:   */   {
/* 13:   */     ValueEval ve0;
/* 14:   */     ValueEval ve1;
/* 15:   */     try
/* 16:   */     {
/* 17:38 */       ve0 = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 18:39 */       ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
/* 19:   */     }
/* 20:   */     catch (EvaluationException e)
/* 21:   */     {
/* 22:41 */       return e.getErrorEval();
/* 23:   */     }
/* 24:43 */     StringBuilder sb = new StringBuilder();
/* 25:44 */     sb.append(getText(ve0));
/* 26:45 */     sb.append(getText(ve1));
/* 27:46 */     return new StringEval(sb.toString());
/* 28:   */   }
/* 29:   */   
/* 30:   */   private Object getText(ValueEval ve)
/* 31:   */   {
/* 32:50 */     if ((ve instanceof StringValueEval))
/* 33:   */     {
/* 34:51 */       StringValueEval sve = (StringValueEval)ve;
/* 35:52 */       return sve.getStringValue();
/* 36:   */     }
/* 37:54 */     if (ve == BlankEval.instance) {
/* 38:55 */       return "";
/* 39:   */     }
/* 40:57 */     throw new IllegalAccessError("Unexpected value type (" + ve.getClass().getName() + ")");
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.ConcatEval
 * JD-Core Version:    0.7.0.1
 */