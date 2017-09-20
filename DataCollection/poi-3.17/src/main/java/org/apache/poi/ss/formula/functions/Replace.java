/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  4:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  5:   */ import org.apache.poi.ss.formula.eval.StringEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  7:   */ 
/*  8:   */ public final class Replace
/*  9:   */   extends Fixed4ArgFunction
/* 10:   */ {
/* 11:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3)
/* 12:   */   {
/* 13:   */     String oldStr;
/* 14:   */     int startNum;
/* 15:   */     int numChars;
/* 16:   */     String newStr;
/* 17:   */     try
/* 18:   */     {
/* 19:50 */       oldStr = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/* 20:51 */       startNum = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
/* 21:52 */       numChars = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex);
/* 22:53 */       newStr = TextFunction.evaluateStringArg(arg3, srcRowIndex, srcColumnIndex);
/* 23:   */     }
/* 24:   */     catch (EvaluationException e)
/* 25:   */     {
/* 26:55 */       return e.getErrorEval();
/* 27:   */     }
/* 28:58 */     if ((startNum < 1) || (numChars < 0)) {
/* 29:59 */       return ErrorEval.VALUE_INVALID;
/* 30:   */     }
/* 31:61 */     StringBuffer strBuff = new StringBuffer(oldStr);
/* 32:63 */     if ((startNum <= oldStr.length()) && (numChars != 0)) {
/* 33:64 */       strBuff.delete(startNum - 1, startNum - 1 + numChars);
/* 34:   */     }
/* 35:67 */     if (startNum > strBuff.length()) {
/* 36:68 */       strBuff.append(newStr);
/* 37:   */     } else {
/* 38:70 */       strBuff.insert(startNum - 1, newStr);
/* 39:   */     }
/* 40:72 */     return new StringEval(strBuff.toString());
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Replace
 * JD-Core Version:    0.7.0.1
 */