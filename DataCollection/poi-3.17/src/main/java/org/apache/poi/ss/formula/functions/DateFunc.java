/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*   9:    */ import org.apache.poi.util.LocaleUtil;
/*  10:    */ 
/*  11:    */ public final class DateFunc
/*  12:    */   extends Fixed3ArgFunction
/*  13:    */ {
/*  14: 34 */   public static final Function instance = new DateFunc();
/*  15:    */   
/*  16:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  17:    */   {
/*  18:    */     double result;
/*  19:    */     try
/*  20:    */     {
/*  21: 43 */       double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  22: 44 */       double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/*  23: 45 */       double d2 = NumericFunction.singleOperandEvaluate(arg2, srcRowIndex, srcColumnIndex);
/*  24: 46 */       result = evaluate(getYear(d0), (int)(d1 - 1.0D), (int)d2);
/*  25: 47 */       NumericFunction.checkValue(result);
/*  26:    */     }
/*  27:    */     catch (EvaluationException e)
/*  28:    */     {
/*  29: 49 */       return e.getErrorEval();
/*  30:    */     }
/*  31: 51 */     return new NumberEval(result);
/*  32:    */   }
/*  33:    */   
/*  34:    */   private static double evaluate(int year, int month, int pDay)
/*  35:    */     throws EvaluationException
/*  36:    */   {
/*  37: 59 */     if (year < 0) {
/*  38: 60 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  39:    */     }
/*  40: 63 */     while (month < 0)
/*  41:    */     {
/*  42: 64 */       year--;
/*  43: 65 */       month += 12;
/*  44:    */     }
/*  45: 71 */     if ((year == 1900) && (month == 1) && (pDay == 29)) {
/*  46: 72 */       return 60.0D;
/*  47:    */     }
/*  48: 77 */     int day = pDay;
/*  49: 78 */     if ((year == 1900) && (
/*  50: 79 */       ((month == 0) && (day >= 60)) || ((month == 1) && (day >= 30)))) {
/*  51: 81 */       day--;
/*  52:    */     }
/*  53: 86 */     Calendar c = LocaleUtil.getLocaleCalendar(year, month, day);
/*  54: 90 */     if ((pDay < 0) && (c.get(1) == 1900) && (month > 1) && (c.get(2) < 2)) {
/*  55: 93 */       c.add(5, 1);
/*  56:    */     }
/*  57: 97 */     boolean use1904windowing = false;
/*  58:    */     
/*  59:    */ 
/*  60:100 */     return DateUtil.getExcelDate(c.getTime(), use1904windowing);
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static int getYear(double d)
/*  64:    */   {
/*  65:104 */     int year = (int)d;
/*  66:106 */     if (year < 0) {
/*  67:107 */       return -1;
/*  68:    */     }
/*  69:110 */     return year < 1900 ? 1900 + year : year;
/*  70:    */   }
/*  71:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.DateFunc
 * JD-Core Version:    0.7.0.1
 */