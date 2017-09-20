/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Date;
/*   5:    */ import java.util.TimeZone;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import java.util.regex.Pattern;
/*   8:    */ import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
/*   9:    */ import org.apache.poi.util.LocaleUtil;
/*  10:    */ 
/*  11:    */ public class DateUtil
/*  12:    */ {
/*  13:    */   public static final int SECONDS_PER_MINUTE = 60;
/*  14:    */   public static final int MINUTES_PER_HOUR = 60;
/*  15:    */   public static final int HOURS_PER_DAY = 24;
/*  16:    */   public static final int SECONDS_PER_DAY = 86400;
/*  17:    */   private static final int BAD_DATE = -1;
/*  18:    */   public static final long DAY_MILLISECONDS = 86400000L;
/*  19: 45 */   private static final Pattern TIME_SEPARATOR_PATTERN = Pattern.compile(":");
/*  20: 50 */   private static final Pattern date_ptrn1 = Pattern.compile("^\\[\\$\\-.*?\\]");
/*  21: 51 */   private static final Pattern date_ptrn2 = Pattern.compile("^\\[[a-zA-Z]+\\]");
/*  22: 52 */   private static final Pattern date_ptrn3a = Pattern.compile("[yYmMdDhHsS]");
/*  23: 54 */   private static final Pattern date_ptrn3b = Pattern.compile("^[\\[\\]yYmMdDhHsS\\-T/年月日,. :\"\\\\]+0*[ampAMP/]*$");
/*  24: 56 */   private static final Pattern date_ptrn4 = Pattern.compile("^\\[([hH]+|[mM]+|[sS]+)\\]");
/*  25: 59 */   private static final Pattern date_ptrn5 = Pattern.compile("^\\[DBNum(1|2|3)\\]");
/*  26:    */   
/*  27:    */   public static double getExcelDate(Date date)
/*  28:    */   {
/*  29: 69 */     return getExcelDate(date, false);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static double getExcelDate(Date date, boolean use1904windowing)
/*  33:    */   {
/*  34: 80 */     Calendar calStart = LocaleUtil.getLocaleCalendar();
/*  35: 81 */     calStart.setTime(date);
/*  36: 82 */     return internalGetExcelDate(calStart, use1904windowing);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static double getExcelDate(Calendar date, boolean use1904windowing)
/*  40:    */   {
/*  41: 96 */     return internalGetExcelDate((Calendar)date.clone(), use1904windowing);
/*  42:    */   }
/*  43:    */   
/*  44:    */   private static double internalGetExcelDate(Calendar date, boolean use1904windowing)
/*  45:    */   {
/*  46: 99 */     if (((!use1904windowing) && (date.get(1) < 1900)) || ((use1904windowing) && (date.get(1) < 1904))) {
/*  47:102 */       return -1.0D;
/*  48:    */     }
/*  49:111 */     double fraction = (((date.get(11) * 60 + date.get(12)) * 60 + date.get(13)) * 1000 + date.get(14)) / 86400000.0D;
/*  50:    */     
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:116 */     Calendar calStart = dayStart(date);
/*  55:    */     
/*  56:118 */     double value = fraction + absoluteDay(calStart, use1904windowing);
/*  57:120 */     if ((!use1904windowing) && (value >= 60.0D)) {
/*  58:121 */       value += 1.0D;
/*  59:122 */     } else if (use1904windowing) {
/*  60:123 */       value -= 1.0D;
/*  61:    */     }
/*  62:126 */     return value;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static Date getJavaDate(double date, TimeZone tz)
/*  66:    */   {
/*  67:143 */     return getJavaDate(date, false, tz, false);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static Date getJavaDate(double date)
/*  71:    */   {
/*  72:163 */     return getJavaDate(date, false, null, false);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static Date getJavaDate(double date, boolean use1904windowing, TimeZone tz)
/*  76:    */   {
/*  77:182 */     return getJavaDate(date, use1904windowing, tz, false);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static Date getJavaDate(double date, boolean use1904windowing, TimeZone tz, boolean roundSeconds)
/*  81:    */   {
/*  82:202 */     Calendar calendar = getJavaCalendar(date, use1904windowing, tz, roundSeconds);
/*  83:203 */     return calendar == null ? null : calendar.getTime();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static Date getJavaDate(double date, boolean use1904windowing)
/*  87:    */   {
/*  88:226 */     return getJavaDate(date, use1904windowing, null, false);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static void setCalendar(Calendar calendar, int wholeDays, int millisecondsInDay, boolean use1904windowing, boolean roundSeconds)
/*  92:    */   {
/*  93:231 */     int startYear = 1900;
/*  94:232 */     int dayAdjust = -1;
/*  95:233 */     if (use1904windowing)
/*  96:    */     {
/*  97:234 */       startYear = 1904;
/*  98:235 */       dayAdjust = 1;
/*  99:    */     }
/* 100:237 */     else if (wholeDays < 61)
/* 101:    */     {
/* 102:240 */       dayAdjust = 0;
/* 103:    */     }
/* 104:242 */     calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
/* 105:243 */     calendar.set(14, millisecondsInDay);
/* 106:244 */     if (calendar.get(14) == 0) {
/* 107:245 */       calendar.clear(14);
/* 108:    */     }
/* 109:247 */     if (roundSeconds)
/* 110:    */     {
/* 111:248 */       calendar.add(14, 500);
/* 112:249 */       calendar.clear(14);
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static Calendar getJavaCalendar(double date)
/* 117:    */   {
/* 118:261 */     return getJavaCalendar(date, false, (TimeZone)null, false);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static Calendar getJavaCalendar(double date, boolean use1904windowing)
/* 122:    */   {
/* 123:273 */     return getJavaCalendar(date, use1904windowing, (TimeZone)null, false);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static Calendar getJavaCalendarUTC(double date, boolean use1904windowing)
/* 127:    */   {
/* 128:286 */     return getJavaCalendar(date, use1904windowing, LocaleUtil.TIMEZONE_UTC, false);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static Calendar getJavaCalendar(double date, boolean use1904windowing, TimeZone timeZone)
/* 132:    */   {
/* 133:299 */     return getJavaCalendar(date, use1904windowing, timeZone, false);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static Calendar getJavaCalendar(double date, boolean use1904windowing, TimeZone timeZone, boolean roundSeconds)
/* 137:    */   {
/* 138:312 */     if (!isValidExcelDate(date)) {
/* 139:313 */       return null;
/* 140:    */     }
/* 141:315 */     int wholeDays = (int)Math.floor(date);
/* 142:316 */     int millisecondsInDay = (int)((date - wholeDays) * 86400000.0D + 0.5D);
/* 143:    */     Calendar calendar;
/* 144:    */     Calendar calendar;
/* 145:318 */     if (timeZone != null) {
/* 146:319 */       calendar = LocaleUtil.getLocaleCalendar(timeZone);
/* 147:    */     } else {
/* 148:321 */       calendar = LocaleUtil.getLocaleCalendar();
/* 149:    */     }
/* 150:323 */     setCalendar(calendar, wholeDays, millisecondsInDay, use1904windowing, roundSeconds);
/* 151:324 */     return calendar;
/* 152:    */   }
/* 153:    */   
/* 154:331 */   private static ThreadLocal<Integer> lastFormatIndex = new ThreadLocal()
/* 155:    */   {
/* 156:    */     protected Integer initialValue()
/* 157:    */     {
/* 158:333 */       return Integer.valueOf(-1);
/* 159:    */     }
/* 160:    */   };
/* 161:336 */   private static ThreadLocal<String> lastFormatString = new ThreadLocal();
/* 162:337 */   private static ThreadLocal<Boolean> lastCachedResult = new ThreadLocal();
/* 163:    */   
/* 164:    */   private static boolean isCached(String formatString, int formatIndex)
/* 165:    */   {
/* 166:340 */     String cachedFormatString = (String)lastFormatString.get();
/* 167:341 */     return (cachedFormatString != null) && (formatIndex == ((Integer)lastFormatIndex.get()).intValue()) && (formatString.equals(cachedFormatString));
/* 168:    */   }
/* 169:    */   
/* 170:    */   private static void cache(String formatString, int formatIndex, boolean cached)
/* 171:    */   {
/* 172:346 */     lastFormatIndex.set(Integer.valueOf(formatIndex));
/* 173:347 */     lastFormatString.set(formatString);
/* 174:348 */     lastCachedResult.set(Boolean.valueOf(cached));
/* 175:    */   }
/* 176:    */   
/* 177:    */   public static boolean isADateFormat(ExcelNumberFormat numFmt)
/* 178:    */   {
/* 179:366 */     if (numFmt == null) {
/* 180:366 */       return false;
/* 181:    */     }
/* 182:368 */     return isADateFormat(numFmt.getIdx(), numFmt.getFormat());
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static boolean isADateFormat(int formatIndex, String formatString)
/* 186:    */   {
/* 187:388 */     if (isInternalDateFormat(formatIndex))
/* 188:    */     {
/* 189:389 */       cache(formatString, formatIndex, true);
/* 190:390 */       return true;
/* 191:    */     }
/* 192:394 */     if ((formatString == null) || (formatString.length() == 0)) {
/* 193:395 */       return false;
/* 194:    */     }
/* 195:399 */     if (isCached(formatString, formatIndex)) {
/* 196:400 */       return ((Boolean)lastCachedResult.get()).booleanValue();
/* 197:    */     }
/* 198:403 */     String fs = formatString;
/* 199:    */     
/* 200:    */ 
/* 201:    */ 
/* 202:    */ 
/* 203:    */ 
/* 204:    */ 
/* 205:    */ 
/* 206:    */ 
/* 207:    */ 
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:424 */     int length = fs.length();
/* 220:425 */     StringBuilder sb = new StringBuilder(length);
/* 221:426 */     for (int i = 0; i < length; i++)
/* 222:    */     {
/* 223:427 */       char c = fs.charAt(i);
/* 224:428 */       if (i < length - 1)
/* 225:    */       {
/* 226:429 */         char nc = fs.charAt(i + 1);
/* 227:430 */         if (c == '\\') {
/* 228:431 */           switch (nc)
/* 229:    */           {
/* 230:    */           case ' ': 
/* 231:    */           case ',': 
/* 232:    */           case '-': 
/* 233:    */           case '.': 
/* 234:    */           case '\\': 
/* 235:    */             break;
/* 236:    */           }
/* 237:    */         }
/* 238:440 */         if ((c == ';') && (nc == '@'))
/* 239:    */         {
/* 240:441 */           i++;
/* 241:    */           
/* 242:443 */           continue;
/* 243:    */         }
/* 244:    */       }
/* 245:446 */       sb.append(c);
/* 246:    */     }
/* 247:448 */     fs = sb.toString();
/* 248:451 */     if (date_ptrn4.matcher(fs).matches())
/* 249:    */     {
/* 250:452 */       cache(formatString, formatIndex, true);
/* 251:453 */       return true;
/* 252:    */     }
/* 253:457 */     fs = date_ptrn5.matcher(fs).replaceAll("");
/* 254:    */     
/* 255:    */ 
/* 256:460 */     fs = date_ptrn1.matcher(fs).replaceAll("");
/* 257:    */     
/* 258:    */ 
/* 259:463 */     fs = date_ptrn2.matcher(fs).replaceAll("");
/* 260:    */     
/* 261:    */ 
/* 262:    */ 
/* 263:467 */     int separatorIndex = fs.indexOf(';');
/* 264:468 */     if ((0 < separatorIndex) && (separatorIndex < fs.length() - 1)) {
/* 265:469 */       fs = fs.substring(0, separatorIndex);
/* 266:    */     }
/* 267:474 */     if (!date_ptrn3a.matcher(fs).find()) {
/* 268:475 */       return false;
/* 269:    */     }
/* 270:482 */     boolean result = date_ptrn3b.matcher(fs).matches();
/* 271:483 */     cache(formatString, formatIndex, result);
/* 272:484 */     return result;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static boolean isInternalDateFormat(int format)
/* 276:    */   {
/* 277:493 */     switch (format)
/* 278:    */     {
/* 279:    */     case 14: 
/* 280:    */     case 15: 
/* 281:    */     case 16: 
/* 282:    */     case 17: 
/* 283:    */     case 18: 
/* 284:    */     case 19: 
/* 285:    */     case 20: 
/* 286:    */     case 21: 
/* 287:    */     case 22: 
/* 288:    */     case 45: 
/* 289:    */     case 46: 
/* 290:    */     case 47: 
/* 291:508 */       return true;
/* 292:    */     }
/* 293:510 */     return false;
/* 294:    */   }
/* 295:    */   
/* 296:    */   public static boolean isCellDateFormatted(Cell cell)
/* 297:    */   {
/* 298:523 */     return isCellDateFormatted(cell, null);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static boolean isCellDateFormatted(Cell cell, ConditionalFormattingEvaluator cfEvaluator)
/* 302:    */   {
/* 303:539 */     if (cell == null) {
/* 304:539 */       return false;
/* 305:    */     }
/* 306:540 */     boolean bDate = false;
/* 307:    */     
/* 308:542 */     double d = cell.getNumericCellValue();
/* 309:543 */     if (isValidExcelDate(d))
/* 310:    */     {
/* 311:544 */       ExcelNumberFormat nf = ExcelNumberFormat.from(cell, cfEvaluator);
/* 312:545 */       if (nf == null) {
/* 313:545 */         return false;
/* 314:    */       }
/* 315:546 */       bDate = isADateFormat(nf);
/* 316:    */     }
/* 317:548 */     return bDate;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public static boolean isCellInternalDateFormatted(Cell cell)
/* 321:    */   {
/* 322:560 */     if (cell == null) {
/* 323:560 */       return false;
/* 324:    */     }
/* 325:561 */     boolean bDate = false;
/* 326:    */     
/* 327:563 */     double d = cell.getNumericCellValue();
/* 328:564 */     if (isValidExcelDate(d))
/* 329:    */     {
/* 330:565 */       CellStyle style = cell.getCellStyle();
/* 331:566 */       int i = style.getDataFormat();
/* 332:567 */       bDate = isInternalDateFormat(i);
/* 333:    */     }
/* 334:569 */     return bDate;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public static boolean isValidExcelDate(double value)
/* 338:    */   {
/* 339:582 */     return value > -4.940656458412465E-324D;
/* 340:    */   }
/* 341:    */   
/* 342:    */   protected static int absoluteDay(Calendar cal, boolean use1904windowing)
/* 343:    */   {
/* 344:594 */     return cal.get(6) + daysInPriorYears(cal.get(1), use1904windowing);
/* 345:    */   }
/* 346:    */   
/* 347:    */   private static int daysInPriorYears(int yr, boolean use1904windowing)
/* 348:    */   {
/* 349:609 */     if (((!use1904windowing) && (yr < 1900)) || ((use1904windowing) && (yr < 1904))) {
/* 350:610 */       throw new IllegalArgumentException("'year' must be 1900 or greater");
/* 351:    */     }
/* 352:613 */     int yr1 = yr - 1;
/* 353:614 */     int leapDays = yr1 / 4 - yr1 / 100 + yr1 / 400 - 460;
/* 354:    */     
/* 355:    */ 
/* 356:    */ 
/* 357:    */ 
/* 358:619 */     return 365 * (yr - (use1904windowing ? 1904 : 1900)) + leapDays;
/* 359:    */   }
/* 360:    */   
/* 361:    */   private static Calendar dayStart(Calendar cal)
/* 362:    */   {
/* 363:625 */     cal.get(11);
/* 364:    */     
/* 365:627 */     cal.set(11, 0);
/* 366:628 */     cal.set(12, 0);
/* 367:629 */     cal.set(13, 0);
/* 368:630 */     cal.set(14, 0);
/* 369:631 */     cal.get(11);
/* 370:    */     
/* 371:633 */     return cal;
/* 372:    */   }
/* 373:    */   
/* 374:    */   private static final class FormatException
/* 375:    */     extends Exception
/* 376:    */   {
/* 377:    */     public FormatException(String msg)
/* 378:    */     {
/* 379:640 */       super();
/* 380:    */     }
/* 381:    */   }
/* 382:    */   
/* 383:    */   public static double convertTime(String timeStr)
/* 384:    */   {
/* 385:    */     try
/* 386:    */     {
/* 387:651 */       return convertTimeInternal(timeStr);
/* 388:    */     }
/* 389:    */     catch (FormatException e)
/* 390:    */     {
/* 391:653 */       String msg = "Bad time format '" + timeStr + "' expected 'HH:MM' or 'HH:MM:SS' - " + e.getMessage();
/* 392:    */       
/* 393:655 */       throw new IllegalArgumentException(msg);
/* 394:    */     }
/* 395:    */   }
/* 396:    */   
/* 397:    */   private static double convertTimeInternal(String timeStr)
/* 398:    */     throws FormatException
/* 399:    */   {
/* 400:659 */     int len = timeStr.length();
/* 401:660 */     if ((len < 4) || (len > 8)) {
/* 402:661 */       throw new FormatException("Bad length");
/* 403:    */     }
/* 404:663 */     String[] parts = TIME_SEPARATOR_PATTERN.split(timeStr);
/* 405:    */     String secStr;
/* 406:666 */     switch (parts.length)
/* 407:    */     {
/* 408:    */     case 2: 
/* 409:667 */       secStr = "00"; break;
/* 410:    */     case 3: 
/* 411:668 */       secStr = parts[2]; break;
/* 412:    */     default: 
/* 413:670 */       throw new FormatException("Expected 2 or 3 fields but got (" + parts.length + ")");
/* 414:    */     }
/* 415:672 */     String hourStr = parts[0];
/* 416:673 */     String minStr = parts[1];
/* 417:674 */     int hours = parseInt(hourStr, "hour", 24);
/* 418:675 */     int minutes = parseInt(minStr, "minute", 60);
/* 419:676 */     int seconds = parseInt(secStr, "second", 60);
/* 420:    */     
/* 421:678 */     double totalSeconds = seconds + (minutes + hours * 60) * 60;
/* 422:679 */     return totalSeconds / 86400.0D;
/* 423:    */   }
/* 424:    */   
/* 425:    */   public static Date parseYYYYMMDDDate(String dateStr)
/* 426:    */   {
/* 427:    */     try
/* 428:    */     {
/* 429:688 */       return parseYYYYMMDDDateInternal(dateStr);
/* 430:    */     }
/* 431:    */     catch (FormatException e)
/* 432:    */     {
/* 433:690 */       String msg = "Bad time format " + dateStr + " expected 'YYYY/MM/DD' - " + e.getMessage();
/* 434:    */       
/* 435:692 */       throw new IllegalArgumentException(msg);
/* 436:    */     }
/* 437:    */   }
/* 438:    */   
/* 439:    */   private static Date parseYYYYMMDDDateInternal(String timeStr)
/* 440:    */     throws FormatException
/* 441:    */   {
/* 442:696 */     if (timeStr.length() != 10) {
/* 443:697 */       throw new FormatException("Bad length");
/* 444:    */     }
/* 445:700 */     String yearStr = timeStr.substring(0, 4);
/* 446:701 */     String monthStr = timeStr.substring(5, 7);
/* 447:702 */     String dayStr = timeStr.substring(8, 10);
/* 448:703 */     int year = parseInt(yearStr, "year", -32768, 32767);
/* 449:704 */     int month = parseInt(monthStr, "month", 1, 12);
/* 450:705 */     int day = parseInt(dayStr, "day", 1, 31);
/* 451:    */     
/* 452:707 */     Calendar cal = LocaleUtil.getLocaleCalendar(year, month - 1, day);
/* 453:708 */     return cal.getTime();
/* 454:    */   }
/* 455:    */   
/* 456:    */   private static int parseInt(String strVal, String fieldName, int rangeMax)
/* 457:    */     throws FormatException
/* 458:    */   {
/* 459:711 */     return parseInt(strVal, fieldName, 0, rangeMax - 1);
/* 460:    */   }
/* 461:    */   
/* 462:    */   private static int parseInt(String strVal, String fieldName, int lowerLimit, int upperLimit)
/* 463:    */     throws FormatException
/* 464:    */   {
/* 465:    */     int result;
/* 466:    */     try
/* 467:    */     {
/* 468:717 */       result = Integer.parseInt(strVal);
/* 469:    */     }
/* 470:    */     catch (NumberFormatException e)
/* 471:    */     {
/* 472:719 */       throw new FormatException("Bad int format '" + strVal + "' for " + fieldName + " field");
/* 473:    */     }
/* 474:721 */     if ((result < lowerLimit) || (result > upperLimit)) {
/* 475:722 */       throw new FormatException(fieldName + " value (" + result + ") is outside the allowable range(0.." + upperLimit + ")");
/* 476:    */     }
/* 477:725 */     return result;
/* 478:    */   }
/* 479:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.usermodel.DateUtil

 * JD-Core Version:    0.7.0.1

 */