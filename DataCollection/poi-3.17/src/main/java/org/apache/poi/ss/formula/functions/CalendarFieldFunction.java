/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  5:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  6:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  8:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  9:   */ import org.apache.poi.ss.usermodel.DateUtil;
/* 10:   */ 
/* 11:   */ public final class CalendarFieldFunction
/* 12:   */   extends Fixed1ArgFunction
/* 13:   */ {
/* 14:38 */   public static final Function YEAR = new CalendarFieldFunction(1);
/* 15:39 */   public static final Function MONTH = new CalendarFieldFunction(2);
/* 16:40 */   public static final Function DAY = new CalendarFieldFunction(5);
/* 17:41 */   public static final Function HOUR = new CalendarFieldFunction(11);
/* 18:42 */   public static final Function MINUTE = new CalendarFieldFunction(12);
/* 19:43 */   public static final Function SECOND = new CalendarFieldFunction(13);
/* 20:   */   private final int _dateFieldId;
/* 21:   */   
/* 22:   */   private CalendarFieldFunction(int dateFieldId)
/* 23:   */   {
/* 24:48 */     this._dateFieldId = dateFieldId;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public final ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 28:   */   {
/* 29:   */     double val;
/* 30:   */     try
/* 31:   */     {
/* 32:54 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 33:55 */       val = OperandResolver.coerceValueToDouble(ve);
/* 34:   */     }
/* 35:   */     catch (EvaluationException e)
/* 36:   */     {
/* 37:57 */       return e.getErrorEval();
/* 38:   */     }
/* 39:59 */     if (val < 0.0D) {
/* 40:60 */       return ErrorEval.NUM_ERROR;
/* 41:   */     }
/* 42:62 */     return new NumberEval(getCalField(val));
/* 43:   */   }
/* 44:   */   
/* 45:   */   private int getCalField(double serialDate)
/* 46:   */   {
/* 47:68 */     if ((int)serialDate == 0) {
/* 48:69 */       switch (this._dateFieldId)
/* 49:   */       {
/* 50:   */       case 1: 
/* 51:70 */         return 1900;
/* 52:   */       case 2: 
/* 53:71 */         return 1;
/* 54:   */       case 5: 
/* 55:72 */         return 0;
/* 56:   */       }
/* 57:   */     }
/* 58:80 */     Calendar c = DateUtil.getJavaCalendarUTC(serialDate + 5.78125E-006D, false);
/* 59:81 */     int result = c.get(this._dateFieldId);
/* 60:84 */     if (this._dateFieldId == 2) {
/* 61:85 */       result++;
/* 62:   */     }
/* 63:88 */     return result;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.CalendarFieldFunction
 * JD-Core Version:    0.7.0.1
 */