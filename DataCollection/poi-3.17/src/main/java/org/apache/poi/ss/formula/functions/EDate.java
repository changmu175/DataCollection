/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import java.util.Date;
/*  5:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  6:   */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  8:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  9:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/* 10:   */ import org.apache.poi.ss.formula.eval.RefEval;
/* 11:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 12:   */ import org.apache.poi.ss.usermodel.DateUtil;
/* 13:   */ import org.apache.poi.util.LocaleUtil;
/* 14:   */ 
/* 15:   */ public class EDate
/* 16:   */   implements FreeRefFunction
/* 17:   */ {
/* 18:37 */   public static final FreeRefFunction instance = new EDate();
/* 19:   */   
/* 20:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 21:   */   {
/* 22:40 */     if (args.length != 2) {
/* 23:41 */       return ErrorEval.VALUE_INVALID;
/* 24:   */     }
/* 25:   */     try
/* 26:   */     {
/* 27:44 */       double startDateAsNumber = getValue(args[0]);
/* 28:45 */       int offsetInMonthAsNumber = (int)getValue(args[1]);
/* 29:   */       
/* 30:47 */       Date startDate = DateUtil.getJavaDate(startDateAsNumber);
/* 31:48 */       Calendar calendar = LocaleUtil.getLocaleCalendar();
/* 32:49 */       calendar.setTime(startDate);
/* 33:50 */       calendar.add(2, offsetInMonthAsNumber);
/* 34:51 */       return new NumberEval(DateUtil.getExcelDate(calendar.getTime()));
/* 35:   */     }
/* 36:   */     catch (EvaluationException e)
/* 37:   */     {
/* 38:53 */       return e.getErrorEval();
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   private double getValue(ValueEval arg)
/* 43:   */     throws EvaluationException
/* 44:   */   {
/* 45:58 */     if ((arg instanceof NumberEval)) {
/* 46:59 */       return ((NumberEval)arg).getNumberValue();
/* 47:   */     }
/* 48:61 */     if ((arg instanceof BlankEval)) {
/* 49:62 */       return 0.0D;
/* 50:   */     }
/* 51:64 */     if ((arg instanceof RefEval))
/* 52:   */     {
/* 53:65 */       RefEval refEval = (RefEval)arg;
/* 54:66 */       if (refEval.getNumberOfSheets() > 1) {
/* 55:68 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 56:   */       }
/* 57:71 */       ValueEval innerValueEval = refEval.getInnerValueEval(refEval.getFirstSheetIndex());
/* 58:72 */       if ((innerValueEval instanceof NumberEval)) {
/* 59:73 */         return ((NumberEval)innerValueEval).getNumberValue();
/* 60:   */       }
/* 61:75 */       if ((innerValueEval instanceof BlankEval)) {
/* 62:76 */         return 0.0D;
/* 63:   */       }
/* 64:   */     }
/* 65:79 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.EDate
 * JD-Core Version:    0.7.0.1
 */