/*   1:    */ package org.apache.poi.ss.formula.atp;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Date;
/*   5:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*   6:    */ import org.apache.poi.util.LocaleUtil;
/*   7:    */ import org.apache.poi.util.Removal;
/*   8:    */ 
/*   9:    */ public class WorkdayCalculator
/*  10:    */ {
/*  11: 31 */   public static final WorkdayCalculator instance = new WorkdayCalculator();
/*  12:    */   
/*  13:    */   public int calculateWorkdays(double start, double end, double[] holidays)
/*  14:    */   {
/*  15: 49 */     int saturdaysPast = pastDaysOfWeek(start, end, 7);
/*  16: 50 */     int sundaysPast = pastDaysOfWeek(start, end, 1);
/*  17: 51 */     int nonWeekendHolidays = calculateNonWeekendHolidays(start, end, holidays);
/*  18: 52 */     return (int)(end - start + 1.0D) - saturdaysPast - sundaysPast - nonWeekendHolidays;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Date calculateWorkdays(double start, int workdays, double[] holidays)
/*  22:    */   {
/*  23: 64 */     Date startDate = DateUtil.getJavaDate(start);
/*  24: 65 */     int direction = workdays < 0 ? -1 : 1;
/*  25: 66 */     Calendar endDate = LocaleUtil.getLocaleCalendar();
/*  26: 67 */     endDate.setTime(startDate);
/*  27: 68 */     double excelEndDate = DateUtil.getExcelDate(endDate.getTime());
/*  28: 69 */     while (workdays != 0)
/*  29:    */     {
/*  30: 70 */       endDate.add(6, direction);
/*  31: 71 */       excelEndDate += direction;
/*  32: 72 */       if ((endDate.get(7) != 7) && (endDate.get(7) != 1) && (!isHoliday(excelEndDate, holidays))) {
/*  33: 75 */         workdays -= direction;
/*  34:    */       }
/*  35:    */     }
/*  36: 78 */     return endDate.getTime();
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected int pastDaysOfWeek(double start, double end, int dayOfWeek)
/*  40:    */   {
/*  41: 90 */     int pastDaysOfWeek = 0;
/*  42: 91 */     int startDay = (int)Math.floor(start < end ? start : end);
/*  43: 92 */     int endDay = (int)Math.floor(end > start ? end : start);
/*  44: 93 */     for (; startDay <= endDay; startDay++)
/*  45:    */     {
/*  46: 94 */       Calendar today = LocaleUtil.getLocaleCalendar();
/*  47: 95 */       today.setTime(DateUtil.getJavaDate(startDay));
/*  48: 96 */       if (today.get(7) == dayOfWeek) {
/*  49: 97 */         pastDaysOfWeek++;
/*  50:    */       }
/*  51:    */     }
/*  52:100 */     return start <= end ? pastDaysOfWeek : -pastDaysOfWeek;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected int calculateNonWeekendHolidays(double start, double end, double[] holidays)
/*  56:    */   {
/*  57:112 */     int nonWeekendHolidays = 0;
/*  58:113 */     double startDay = start < end ? start : end;
/*  59:114 */     double endDay = end > start ? end : start;
/*  60:115 */     for (double holiday : holidays) {
/*  61:116 */       if ((isInARange(startDay, endDay, holiday)) && 
/*  62:117 */         (!isWeekend(holiday))) {
/*  63:118 */         nonWeekendHolidays++;
/*  64:    */       }
/*  65:    */     }
/*  66:122 */     return start <= end ? nonWeekendHolidays : -nonWeekendHolidays;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isWeekend(double aDate)
/*  70:    */   {
/*  71:130 */     Calendar date = LocaleUtil.getLocaleCalendar();
/*  72:131 */     date.setTime(DateUtil.getJavaDate(aDate));
/*  73:132 */     return (date.get(7) == 7) || (date.get(7) == 1);
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected boolean isHoliday(double aDate, double[] holidays)
/*  77:    */   {
/*  78:141 */     for (double holiday : holidays) {
/*  79:142 */       if (Math.round(holiday) == Math.round(aDate)) {
/*  80:143 */         return true;
/*  81:    */       }
/*  82:    */     }
/*  83:146 */     return false;
/*  84:    */   }
/*  85:    */   
/*  86:    */   /**
/*  87:    */    * @deprecated
/*  88:    */    */
/*  89:    */   @Removal(version="3.18")
/*  90:    */   protected int isNonWorkday(double aDate, double[] holidays)
/*  91:    */   {
/*  92:158 */     return (isWeekend(aDate)) || (isHoliday(aDate, holidays)) ? 1 : 0;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected boolean isInARange(double start, double end, double aDate)
/*  96:    */   {
/*  97:168 */     return (aDate >= start) && (aDate <= end);
/*  98:    */   }
/*  99:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.WorkdayCalculator
 * JD-Core Version:    0.7.0.1
 */