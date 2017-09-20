/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import java.util.Date;
/*  5:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  6:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  8:   */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  9:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 10:   */ import org.apache.poi.ss.usermodel.DateUtil;
/* 11:   */ import org.apache.poi.util.LocaleUtil;
/* 12:   */ 
/* 13:   */ public class EOMonth
/* 14:   */   implements FreeRefFunction
/* 15:   */ {
/* 16:46 */   public static final FreeRefFunction instance = new EOMonth();
/* 17:   */   
/* 18:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 19:   */   {
/* 20:50 */     if (args.length != 2) {
/* 21:51 */       return ErrorEval.VALUE_INVALID;
/* 22:   */     }
/* 23:   */     try
/* 24:   */     {
/* 25:55 */       double startDateAsNumber = NumericFunction.singleOperandEvaluate(args[0], ec.getRowIndex(), ec.getColumnIndex());
/* 26:56 */       int months = (int)NumericFunction.singleOperandEvaluate(args[1], ec.getRowIndex(), ec.getColumnIndex());
/* 27:59 */       if ((startDateAsNumber >= 0.0D) && (startDateAsNumber < 1.0D)) {
/* 28:60 */         startDateAsNumber = 1.0D;
/* 29:   */       }
/* 30:63 */       Date startDate = DateUtil.getJavaDate(startDateAsNumber, false);
/* 31:   */       
/* 32:65 */       Calendar cal = LocaleUtil.getLocaleCalendar();
/* 33:66 */       cal.setTime(startDate);
/* 34:67 */       cal.clear(10);
/* 35:68 */       cal.set(11, 0);
/* 36:69 */       cal.clear(12);
/* 37:70 */       cal.clear(13);
/* 38:71 */       cal.clear(14);
/* 39:   */       
/* 40:73 */       cal.add(2, months + 1);
/* 41:74 */       cal.set(5, 1);
/* 42:75 */       cal.add(5, -1);
/* 43:   */       
/* 44:77 */       return new NumberEval(DateUtil.getExcelDate(cal.getTime()));
/* 45:   */     }
/* 46:   */     catch (EvaluationException e)
/* 47:   */     {
/* 48:79 */       return e.getErrorEval();
/* 49:   */     }
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.EOMonth
 * JD-Core Version:    0.7.0.1
 */