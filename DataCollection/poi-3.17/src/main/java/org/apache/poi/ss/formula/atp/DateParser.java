/*  1:   */ package org.apache.poi.ss.formula.atp;
/*  2:   */ 
/*  3:   */ import java.util.Calendar;
/*  4:   */ import java.util.regex.Pattern;
/*  5:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  6:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  7:   */ import org.apache.poi.util.LocaleUtil;
/*  8:   */ 
/*  9:   */ public class DateParser
/* 10:   */ {
/* 11:   */   public static Calendar parseDate(String strVal)
/* 12:   */     throws EvaluationException
/* 13:   */   {
/* 14:43 */     String[] parts = Pattern.compile("/").split(strVal);
/* 15:44 */     if (parts.length != 3) {
/* 16:45 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 17:   */     }
/* 18:47 */     String part2 = parts[2];
/* 19:48 */     int spacePos = part2.indexOf(' ');
/* 20:49 */     if (spacePos > 0) {
/* 21:51 */       part2 = part2.substring(0, spacePos);
/* 22:   */     }
/* 23:   */     int f0;
/* 24:   */     int f1;
/* 25:   */     int f2;
/* 26:   */     try
/* 27:   */     {
/* 28:57 */       f0 = Integer.parseInt(parts[0]);
/* 29:58 */       f1 = Integer.parseInt(parts[1]);
/* 30:59 */       f2 = Integer.parseInt(part2);
/* 31:   */     }
/* 32:   */     catch (NumberFormatException e)
/* 33:   */     {
/* 34:61 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 35:   */     }
/* 36:63 */     if ((f0 < 0) || (f1 < 0) || (f2 < 0) || ((f0 > 12) && (f1 > 12) && (f2 > 12))) {
/* 37:65 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 38:   */     }
/* 39:68 */     if ((f0 >= 1900) && (f0 < 9999)) {
/* 40:70 */       return makeDate(f0, f1, f2);
/* 41:   */     }
/* 42:78 */     throw new RuntimeException("Unable to determine date format for text '" + strVal + "'");
/* 43:   */   }
/* 44:   */   
/* 45:   */   private static Calendar makeDate(int year, int month, int day)
/* 46:   */     throws EvaluationException
/* 47:   */   {
/* 48:85 */     if ((month < 1) || (month > 12)) {
/* 49:86 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 50:   */     }
/* 51:88 */     Calendar cal = LocaleUtil.getLocaleCalendar(year, month - 1, 1, 0, 0, 0);
/* 52:89 */     if ((day < 1) || (day > cal.getActualMaximum(5))) {
/* 53:90 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 54:   */     }
/* 55:92 */     cal.set(5, day);
/* 56:93 */     return cal;
/* 57:   */   }
/* 58:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.DateParser
 * JD-Core Version:    0.7.0.1
 */