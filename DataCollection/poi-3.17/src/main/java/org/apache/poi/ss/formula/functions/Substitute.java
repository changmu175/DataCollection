/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   7:    */ 
/*   8:    */ public final class Substitute
/*   9:    */   extends Var3or4ArgFunction
/*  10:    */ {
/*  11:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  12:    */   {
/*  13:    */     String result;
/*  14:    */     try
/*  15:    */     {
/*  16: 36 */       String oldStr = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/*  17: 37 */       String searchStr = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/*  18: 38 */       String newStr = TextFunction.evaluateStringArg(arg2, srcRowIndex, srcColumnIndex);
/*  19:    */       
/*  20: 40 */       result = replaceAllOccurrences(oldStr, searchStr, newStr);
/*  21:    */     }
/*  22:    */     catch (EvaluationException e)
/*  23:    */     {
/*  24: 42 */       return e.getErrorEval();
/*  25:    */     }
/*  26: 44 */     return new StringEval(result);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3)
/*  30:    */   {
/*  31:    */     String result;
/*  32:    */     try
/*  33:    */     {
/*  34: 51 */       String oldStr = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
/*  35: 52 */       String searchStr = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
/*  36: 53 */       String newStr = TextFunction.evaluateStringArg(arg2, srcRowIndex, srcColumnIndex);
/*  37:    */       
/*  38: 55 */       int instanceNumber = TextFunction.evaluateIntArg(arg3, srcRowIndex, srcColumnIndex);
/*  39: 56 */       if (instanceNumber < 1) {
/*  40: 57 */         return ErrorEval.VALUE_INVALID;
/*  41:    */       }
/*  42: 59 */       result = replaceOneOccurrence(oldStr, searchStr, newStr, instanceNumber);
/*  43:    */     }
/*  44:    */     catch (EvaluationException e)
/*  45:    */     {
/*  46: 61 */       return e.getErrorEval();
/*  47:    */     }
/*  48: 63 */     return new StringEval(result);
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static String replaceAllOccurrences(String oldStr, String searchStr, String newStr)
/*  52:    */   {
/*  53: 67 */     StringBuffer sb = new StringBuffer();
/*  54: 68 */     int startIndex = 0;
/*  55: 69 */     int nextMatch = -1;
/*  56:    */     for (;;)
/*  57:    */     {
/*  58: 71 */       nextMatch = oldStr.indexOf(searchStr, startIndex);
/*  59: 72 */       if (nextMatch < 0)
/*  60:    */       {
/*  61: 74 */         sb.append(oldStr.substring(startIndex));
/*  62: 75 */         return sb.toString();
/*  63:    */       }
/*  64: 78 */       sb.append(oldStr.substring(startIndex, nextMatch));
/*  65: 79 */       sb.append(newStr);
/*  66: 80 */       startIndex = nextMatch + searchStr.length();
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   private static String replaceOneOccurrence(String oldStr, String searchStr, String newStr, int instanceNumber)
/*  71:    */   {
/*  72: 85 */     if (searchStr.length() < 1) {
/*  73: 86 */       return oldStr;
/*  74:    */     }
/*  75: 88 */     int startIndex = 0;
/*  76: 89 */     int nextMatch = -1;
/*  77: 90 */     int count = 0;
/*  78:    */     for (;;)
/*  79:    */     {
/*  80: 92 */       nextMatch = oldStr.indexOf(searchStr, startIndex);
/*  81: 93 */       if (nextMatch < 0) {
/*  82: 95 */         return oldStr;
/*  83:    */       }
/*  84: 97 */       count++;
/*  85: 98 */       if (count == instanceNumber)
/*  86:    */       {
/*  87: 99 */         StringBuffer sb = new StringBuffer(oldStr.length() + newStr.length());
/*  88:100 */         sb.append(oldStr.substring(0, nextMatch));
/*  89:101 */         sb.append(newStr);
/*  90:102 */         sb.append(oldStr.substring(nextMatch + searchStr.length()));
/*  91:103 */         return sb.toString();
/*  92:    */       }
/*  93:105 */       startIndex = nextMatch + searchStr.length();
/*  94:    */     }
/*  95:    */   }
/*  96:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Substitute
 * JD-Core Version:    0.7.0.1
 */