/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*   9:    */ import org.apache.poi.util.LocaleUtil;
/*  10:    */ 
/*  11:    */ public class Days360
/*  12:    */   extends Var2or3ArgFunction
/*  13:    */ {
/*  14:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  15:    */   {
/*  16:    */     try
/*  17:    */     {
/*  18: 71 */       double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  19: 72 */       double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/*  20: 73 */       return new NumberEval(evaluate(d0, d1, false));
/*  21:    */     }
/*  22:    */     catch (EvaluationException e)
/*  23:    */     {
/*  24: 75 */       return e.getErrorEval();
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  29:    */   {
/*  30:    */     try
/*  31:    */     {
/*  32: 82 */       double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  33: 83 */       double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/*  34: 84 */       ValueEval ve = OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex);
/*  35: 85 */       Boolean method = OperandResolver.coerceValueToBoolean(ve, false);
/*  36: 86 */       return new NumberEval(evaluate(d0, d1, (method != null) && (method.booleanValue())));
/*  37:    */     }
/*  38:    */     catch (EvaluationException e)
/*  39:    */     {
/*  40: 88 */       return e.getErrorEval();
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   private static double evaluate(double d0, double d1, boolean method)
/*  45:    */   {
/*  46: 93 */     Calendar realStart = getDate(d0);
/*  47: 94 */     Calendar realEnd = getDate(d1);
/*  48: 95 */     int[] startingDate = getStartingDate(realStart, method);
/*  49: 96 */     int[] endingDate = getEndingDate(realEnd, startingDate, method);
/*  50: 97 */     return endingDate[0] * 360 + endingDate[1] * 30 + endingDate[2] - (startingDate[0] * 360 + startingDate[1] * 30 + startingDate[2]);
/*  51:    */   }
/*  52:    */   
/*  53:    */   private static Calendar getDate(double date)
/*  54:    */   {
/*  55:103 */     Calendar processedDate = LocaleUtil.getLocaleCalendar();
/*  56:104 */     processedDate.setTime(DateUtil.getJavaDate(date, false));
/*  57:105 */     return processedDate;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private static int[] getStartingDate(Calendar realStart, boolean method)
/*  61:    */   {
/*  62:109 */     int yyyy = realStart.get(1);
/*  63:110 */     int mm = realStart.get(2);
/*  64:111 */     int dd = Math.min(30, realStart.get(5));
/*  65:113 */     if ((!method) && (isLastDayOfMonth(realStart))) {
/*  66:113 */       dd = 30;
/*  67:    */     }
/*  68:115 */     return new int[] { yyyy, mm, dd };
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static int[] getEndingDate(Calendar realEnd, int[] startingDate, boolean method)
/*  72:    */   {
/*  73:119 */     int yyyy = realEnd.get(1);
/*  74:120 */     int mm = realEnd.get(2);
/*  75:121 */     int dd = Math.min(30, realEnd.get(5));
/*  76:123 */     if ((!method) && (realEnd.get(5) == 31)) {
/*  77:124 */       if (startingDate[2] < 30)
/*  78:    */       {
/*  79:125 */         realEnd.set(5, 1);
/*  80:126 */         realEnd.add(2, 1);
/*  81:127 */         yyyy = realEnd.get(1);
/*  82:128 */         mm = realEnd.get(2);
/*  83:129 */         dd = 1;
/*  84:    */       }
/*  85:    */       else
/*  86:    */       {
/*  87:131 */         dd = 30;
/*  88:    */       }
/*  89:    */     }
/*  90:135 */     return new int[] { yyyy, mm, dd };
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static boolean isLastDayOfMonth(Calendar date)
/*  94:    */   {
/*  95:139 */     int dayOfMonth = date.get(5);
/*  96:140 */     int lastDayOfMonth = date.getActualMaximum(5);
/*  97:141 */     return dayOfMonth == lastDayOfMonth;
/*  98:    */   }
/*  99:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Days360
 * JD-Core Version:    0.7.0.1
 */