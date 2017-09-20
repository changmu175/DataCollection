/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  7:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  9:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 10:   */ import org.apache.poi.ss.usermodel.DateUtil;
/* 11:   */ import org.apache.poi.util.LocaleUtil;
/* 12:   */ 
/* 13:   */ public class WeekNum
/* 14:   */   extends Fixed2ArgFunction
/* 15:   */   implements FreeRefFunction
/* 16:   */ {
/* 17:47 */   public static final FreeRefFunction instance = new WeekNum();
/* 18:   */   
/* 19:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval serialNumVE, ValueEval returnTypeVE)
/* 20:   */   {
/* 21:   */     double serialNum;
/* 22:   */     try
/* 23:   */     {
/* 24:52 */       serialNum = NumericFunction.singleOperandEvaluate(serialNumVE, srcRowIndex, srcColumnIndex);
/* 25:   */     }
/* 26:   */     catch (EvaluationException e)
/* 27:   */     {
/* 28:54 */       return ErrorEval.VALUE_INVALID;
/* 29:   */     }
/* 30:56 */     Calendar serialNumCalendar = LocaleUtil.getLocaleCalendar();
/* 31:57 */     serialNumCalendar.setTime(DateUtil.getJavaDate(serialNum, false));
/* 32:   */     int returnType;
/* 33:   */     try
/* 34:   */     {
/* 35:61 */       ValueEval ve = OperandResolver.getSingleValue(returnTypeVE, srcRowIndex, srcColumnIndex);
/* 36:62 */       returnType = OperandResolver.coerceValueToInt(ve);
/* 37:   */     }
/* 38:   */     catch (EvaluationException e)
/* 39:   */     {
/* 40:64 */       return ErrorEval.NUM_ERROR;
/* 41:   */     }
/* 42:67 */     if ((returnType != 1) && (returnType != 2)) {
/* 43:68 */       return ErrorEval.NUM_ERROR;
/* 44:   */     }
/* 45:71 */     return new NumberEval(getWeekNo(serialNumCalendar, returnType));
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int getWeekNo(Calendar cal, int weekStartOn)
/* 49:   */   {
/* 50:75 */     if (weekStartOn == 1) {
/* 51:76 */       cal.setFirstDayOfWeek(1);
/* 52:   */     } else {
/* 53:78 */       cal.setFirstDayOfWeek(2);
/* 54:   */     }
/* 55:80 */     return cal.get(3);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 59:   */   {
/* 60:84 */     if (args.length == 2) {
/* 61:85 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/* 62:   */     }
/* 63:87 */     return ErrorEval.VALUE_INVALID;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.WeekNum
 * JD-Core Version:    0.7.0.1
 */