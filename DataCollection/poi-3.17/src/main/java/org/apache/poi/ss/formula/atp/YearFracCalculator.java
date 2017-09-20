/*   1:    */ package org.apache.poi.ss.formula.atp;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*   7:    */ import org.apache.poi.util.LocaleUtil;
/*   8:    */ 
/*   9:    */ final class YearFracCalculator
/*  10:    */ {
/*  11:    */   private static final int MS_PER_HOUR = 3600000;
/*  12:    */   private static final int MS_PER_DAY = 86400000;
/*  13:    */   private static final int DAYS_PER_NORMAL_YEAR = 365;
/*  14:    */   private static final int DAYS_PER_LEAP_YEAR = 366;
/*  15:    */   private static final int LONG_MONTH_LEN = 31;
/*  16:    */   private static final int SHORT_MONTH_LEN = 30;
/*  17:    */   private static final int SHORT_FEB_LEN = 28;
/*  18:    */   private static final int LONG_FEB_LEN = 29;
/*  19:    */   
/*  20:    */   public static double calculate(double pStartDateVal, double pEndDateVal, int basis)
/*  21:    */     throws EvaluationException
/*  22:    */   {
/*  23: 53 */     if ((basis < 0) || (basis >= 5)) {
/*  24: 55 */       throw new EvaluationException(ErrorEval.NUM_ERROR);
/*  25:    */     }
/*  26: 61 */     int startDateVal = (int)Math.floor(pStartDateVal);
/*  27: 62 */     int endDateVal = (int)Math.floor(pEndDateVal);
/*  28: 63 */     if (startDateVal == endDateVal) {
/*  29: 65 */       return 0.0D;
/*  30:    */     }
/*  31: 68 */     if (startDateVal > endDateVal)
/*  32:    */     {
/*  33: 69 */       int temp = startDateVal;
/*  34: 70 */       startDateVal = endDateVal;
/*  35: 71 */       endDateVal = temp;
/*  36:    */     }
/*  37: 74 */     switch (basis)
/*  38:    */     {
/*  39:    */     case 0: 
/*  40: 75 */       return basis0(startDateVal, endDateVal);
/*  41:    */     case 1: 
/*  42: 76 */       return basis1(startDateVal, endDateVal);
/*  43:    */     case 2: 
/*  44: 77 */       return basis2(startDateVal, endDateVal);
/*  45:    */     case 3: 
/*  46: 78 */       return basis3(startDateVal, endDateVal);
/*  47:    */     case 4: 
/*  48: 79 */       return basis4(startDateVal, endDateVal);
/*  49:    */     }
/*  50: 81 */     throw new IllegalStateException("cannot happen");
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static double basis0(int startDateVal, int endDateVal)
/*  54:    */   {
/*  55: 90 */     SimpleDate startDate = createDate(startDateVal);
/*  56: 91 */     SimpleDate endDate = createDate(endDateVal);
/*  57: 92 */     int date1day = startDate.day;
/*  58: 93 */     int date2day = endDate.day;
/*  59: 96 */     if ((date1day == 31) && (date2day == 31))
/*  60:    */     {
/*  61: 97 */       date1day = 30;
/*  62: 98 */       date2day = 30;
/*  63:    */     }
/*  64: 99 */     else if (date1day == 31)
/*  65:    */     {
/*  66:100 */       date1day = 30;
/*  67:    */     }
/*  68:101 */     else if ((date1day == 30) && (date2day == 31))
/*  69:    */     {
/*  70:102 */       date2day = 30;
/*  71:    */     }
/*  72:105 */     else if ((startDate.month == 2) && (isLastDayOfMonth(startDate)))
/*  73:    */     {
/*  74:107 */       date1day = 30;
/*  75:108 */       if ((endDate.month == 2) && (isLastDayOfMonth(endDate))) {
/*  76:110 */         date2day = 30;
/*  77:    */       }
/*  78:    */     }
/*  79:113 */     return calculateAdjusted(startDate, endDate, date1day, date2day);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static double basis1(int startDateVal, int endDateVal)
/*  83:    */   {
/*  84:120 */     SimpleDate startDate = createDate(startDateVal);
/*  85:121 */     SimpleDate endDate = createDate(endDateVal);
/*  86:    */     double yearLength;
/*  87:    */     double yearLength;
/*  88:123 */     if (isGreaterThanOneYear(startDate, endDate))
/*  89:    */     {
/*  90:124 */       yearLength = averageYearLength(startDate.year, endDate.year);
/*  91:    */     }
/*  92:    */     else
/*  93:    */     {
/*  94:    */       double yearLength;
/*  95:125 */       if (shouldCountFeb29(startDate, endDate)) {
/*  96:126 */         yearLength = 366.0D;
/*  97:    */       } else {
/*  98:128 */         yearLength = 365.0D;
/*  99:    */       }
/* 100:    */     }
/* 101:130 */     return dateDiff(startDate.tsMilliseconds, endDate.tsMilliseconds) / yearLength;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static double basis2(int startDateVal, int endDateVal)
/* 105:    */   {
/* 106:138 */     return (endDateVal - startDateVal) / 360.0D;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static double basis3(double startDateVal, double endDateVal)
/* 110:    */   {
/* 111:145 */     return (endDateVal - startDateVal) / 365.0D;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static double basis4(int startDateVal, int endDateVal)
/* 115:    */   {
/* 116:152 */     SimpleDate startDate = createDate(startDateVal);
/* 117:153 */     SimpleDate endDate = createDate(endDateVal);
/* 118:154 */     int date1day = startDate.day;
/* 119:155 */     int date2day = endDate.day;
/* 120:159 */     if (date1day == 31) {
/* 121:160 */       date1day = 30;
/* 122:    */     }
/* 123:162 */     if (date2day == 31) {
/* 124:163 */       date2day = 30;
/* 125:    */     }
/* 126:166 */     return calculateAdjusted(startDate, endDate, date1day, date2day);
/* 127:    */   }
/* 128:    */   
/* 129:    */   private static double calculateAdjusted(SimpleDate startDate, SimpleDate endDate, int date1day, int date2day)
/* 130:    */   {
/* 131:172 */     double dayCount = (endDate.year - startDate.year) * 360 + (endDate.month - startDate.month) * 30 + (date2day - date1day) * 1;
/* 132:    */     
/* 133:    */ 
/* 134:    */ 
/* 135:176 */     return dayCount / 360.0D;
/* 136:    */   }
/* 137:    */   
/* 138:    */   private static boolean isLastDayOfMonth(SimpleDate date)
/* 139:    */   {
/* 140:180 */     if (date.day < 28) {
/* 141:181 */       return false;
/* 142:    */     }
/* 143:183 */     return date.day == getLastDayOfMonth(date);
/* 144:    */   }
/* 145:    */   
/* 146:    */   private static int getLastDayOfMonth(SimpleDate date)
/* 147:    */   {
/* 148:187 */     switch (date.month)
/* 149:    */     {
/* 150:    */     case 1: 
/* 151:    */     case 3: 
/* 152:    */     case 5: 
/* 153:    */     case 7: 
/* 154:    */     case 8: 
/* 155:    */     case 10: 
/* 156:    */     case 12: 
/* 157:195 */       return 31;
/* 158:    */     case 4: 
/* 159:    */     case 6: 
/* 160:    */     case 9: 
/* 161:    */     case 11: 
/* 162:200 */       return 30;
/* 163:    */     }
/* 164:202 */     if (isLeapYear(date.year)) {
/* 165:203 */       return 29;
/* 166:    */     }
/* 167:205 */     return 28;
/* 168:    */   }
/* 169:    */   
/* 170:    */   private static boolean shouldCountFeb29(SimpleDate start, SimpleDate end)
/* 171:    */   {
/* 172:213 */     if (isLeapYear(start.year))
/* 173:    */     {
/* 174:214 */       if (start.year == end.year) {
/* 175:216 */         return true;
/* 176:    */       }
/* 177:219 */       switch (start.month)
/* 178:    */       {
/* 179:    */       case 1: 
/* 180:    */       case 2: 
/* 181:222 */         return true;
/* 182:    */       }
/* 183:224 */       return false;
/* 184:    */     }
/* 185:227 */     if (isLeapYear(end.year))
/* 186:    */     {
/* 187:228 */       switch (end.month)
/* 188:    */       {
/* 189:    */       case 1: 
/* 190:230 */         return false;
/* 191:    */       case 2: 
/* 192:    */         break;
/* 193:    */       default: 
/* 194:234 */         return true;
/* 195:    */       }
/* 196:236 */       return end.day == 29;
/* 197:    */     }
/* 198:238 */     return false;
/* 199:    */   }
/* 200:    */   
/* 201:    */   private static int dateDiff(long startDateMS, long endDateMS)
/* 202:    */   {
/* 203:246 */     long msDiff = endDateMS - startDateMS;
/* 204:    */     
/* 205:    */ 
/* 206:249 */     int remainderHours = (int)(msDiff % 86400000L / 3600000L);
/* 207:250 */     switch (remainderHours)
/* 208:    */     {
/* 209:    */     case 0: 
/* 210:    */       break;
/* 211:    */     case 1: 
/* 212:    */     case 23: 
/* 213:    */     default: 
/* 214:257 */       throw new RuntimeException("Unexpected date diff between " + startDateMS + " and " + endDateMS);
/* 215:    */     }
/* 216:260 */     return (int)(0.5D + msDiff / 86400000.0D);
/* 217:    */   }
/* 218:    */   
/* 219:    */   private static double averageYearLength(int startYear, int endYear)
/* 220:    */   {
/* 221:264 */     int dayCount = 0;
/* 222:265 */     for (int i = startYear; i <= endYear; i++)
/* 223:    */     {
/* 224:266 */       dayCount += 365;
/* 225:267 */       if (isLeapYear(i)) {
/* 226:268 */         dayCount++;
/* 227:    */       }
/* 228:    */     }
/* 229:271 */     double numberOfYears = endYear - startYear + 1;
/* 230:272 */     return dayCount / numberOfYears;
/* 231:    */   }
/* 232:    */   
/* 233:    */   private static boolean isLeapYear(int i)
/* 234:    */   {
/* 235:277 */     if (i % 4 != 0) {
/* 236:278 */       return false;
/* 237:    */     }
/* 238:281 */     if (i % 400 == 0) {
/* 239:282 */       return true;
/* 240:    */     }
/* 241:285 */     if (i % 100 == 0) {
/* 242:286 */       return false;
/* 243:    */     }
/* 244:288 */     return true;
/* 245:    */   }
/* 246:    */   
/* 247:    */   private static boolean isGreaterThanOneYear(SimpleDate start, SimpleDate end)
/* 248:    */   {
/* 249:292 */     if (start.year == end.year) {
/* 250:293 */       return false;
/* 251:    */     }
/* 252:295 */     if (start.year + 1 != end.year) {
/* 253:296 */       return true;
/* 254:    */     }
/* 255:299 */     if (start.month > end.month) {
/* 256:300 */       return false;
/* 257:    */     }
/* 258:302 */     if (start.month < end.month) {
/* 259:303 */       return true;
/* 260:    */     }
/* 261:306 */     return start.day < end.day;
/* 262:    */   }
/* 263:    */   
/* 264:    */   private static SimpleDate createDate(int dayCount)
/* 265:    */   {
/* 266:311 */     Calendar cal = LocaleUtil.getLocaleCalendar(LocaleUtil.TIMEZONE_UTC);
/* 267:312 */     DateUtil.setCalendar(cal, dayCount, 0, false, false);
/* 268:313 */     return new SimpleDate(cal);
/* 269:    */   }
/* 270:    */   
/* 271:    */   private static final class SimpleDate
/* 272:    */   {
/* 273:    */     public static final int JANUARY = 1;
/* 274:    */     public static final int FEBRUARY = 2;
/* 275:    */     public final int year;
/* 276:    */     public final int month;
/* 277:    */     public final int day;
/* 278:    */     public long tsMilliseconds;
/* 279:    */     
/* 280:    */     public SimpleDate(Calendar cal)
/* 281:    */     {
/* 282:330 */       this.year = cal.get(1);
/* 283:331 */       this.month = (cal.get(2) + 1);
/* 284:332 */       this.day = cal.get(5);
/* 285:333 */       this.tsMilliseconds = cal.getTimeInMillis();
/* 286:    */     }
/* 287:    */   }
/* 288:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.YearFracCalculator
 * JD-Core Version:    0.7.0.1
 */