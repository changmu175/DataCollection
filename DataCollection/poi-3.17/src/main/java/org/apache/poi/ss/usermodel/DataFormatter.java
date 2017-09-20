/*    1:     */ package org.apache.poi.ss.usermodel;
/*    2:     */ 
/*    3:     */ import java.math.BigDecimal;
/*    4:     */ import java.math.RoundingMode;
/*    5:     */ import java.text.DateFormat;
/*    6:     */ import java.text.DateFormatSymbols;
/*    7:     */ import java.text.DecimalFormat;
/*    8:     */ import java.text.DecimalFormatSymbols;
/*    9:     */ import java.text.FieldPosition;
/*   10:     */ import java.text.Format;
/*   11:     */ import java.text.ParsePosition;
/*   12:     */ import java.text.SimpleDateFormat;
/*   13:     */ import java.util.ArrayList;
/*   14:     */ import java.util.Date;
/*   15:     */ import java.util.HashMap;
/*   16:     */ import java.util.Iterator;
/*   17:     */ import java.util.List;
/*   18:     */ import java.util.Locale;
/*   19:     */ import java.util.Map;
/*   20:     */ import java.util.Map.Entry;
/*   21:     */ import java.util.Observable;
/*   22:     */ import java.util.Observer;
/*   23:     */ import java.util.regex.Matcher;
/*   24:     */ import java.util.regex.Pattern;
/*   25:     */ import org.apache.poi.ss.format.CellFormat;
/*   26:     */ import org.apache.poi.ss.format.CellFormatResult;
/*   27:     */ import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
/*   28:     */ import org.apache.poi.ss.util.NumberToTextConverter;
/*   29:     */ import org.apache.poi.util.LocaleUtil;
/*   30:     */ import org.apache.poi.util.POILogFactory;
/*   31:     */ import org.apache.poi.util.POILogger;
/*   32:     */ 
/*   33:     */ public class DataFormatter
/*   34:     */   implements Observer
/*   35:     */ {
/*   36:     */   private static final String defaultFractionWholePartFormat = "#";
/*   37:     */   private static final String defaultFractionFractionPartFormat = "#/##";
/*   38: 125 */   private static final Pattern numPattern = Pattern.compile("[0#]+");
/*   39: 128 */   private static final Pattern daysAsText = Pattern.compile("([d]{3,})", 2);
/*   40: 131 */   private static final Pattern amPmPattern = Pattern.compile("((A|P)[M/P]*)", 2);
/*   41: 134 */   private static final Pattern rangeConditionalPattern = Pattern.compile(".*\\[\\s*(>|>=|<|<=|=)\\s*[0-9]*\\.*[0-9].*");
/*   42: 140 */   private static final Pattern localePatternGroup = Pattern.compile("(\\[\\$[^-\\]]*-[0-9A-Z]+\\])");
/*   43: 147 */   private static final Pattern colorPattern = Pattern.compile("(\\[BLACK\\])|(\\[BLUE\\])|(\\[CYAN\\])|(\\[GREEN\\])|(\\[MAGENTA\\])|(\\[RED\\])|(\\[WHITE\\])|(\\[YELLOW\\])|(\\[COLOR\\s*\\d\\])|(\\[COLOR\\s*[0-5]\\d\\])", 2);
/*   44: 156 */   private static final Pattern fractionPattern = Pattern.compile("(?:([#\\d]+)\\s+)?(#+)\\s*\\/\\s*([#\\d]+)");
/*   45: 161 */   private static final Pattern fractionStripper = Pattern.compile("(\"[^\"]*\")|([^ \\?#\\d\\/]+)");
/*   46: 167 */   private static final Pattern alternateGrouping = Pattern.compile("([#0]([^.#0])[#0]{3})");
/*   47:     */   private static final String invalidDateTimeString;
/*   48:     */   private DecimalFormatSymbols decimalSymbols;
/*   49:     */   private DateFormatSymbols dateSymbols;
/*   50:     */   private DateFormat defaultDateformat;
/*   51:     */   private Format generalNumberFormat;
/*   52:     */   private Format defaultNumFormat;
/*   53:     */   
/*   54:     */   static
/*   55:     */   {
/*   56: 175 */     StringBuilder buf = new StringBuilder();
/*   57: 176 */     for (int i = 0; i < 255; i++) {
/*   58: 176 */       buf.append('#');
/*   59:     */     }
/*   60: 177 */     invalidDateTimeString = buf.toString();
/*   61:     */   }
/*   62:     */   
/*   63: 205 */   private final Map<String, Format> formats = new HashMap();
/*   64:     */   private final boolean emulateCSV;
/*   65:     */   private Locale locale;
/*   66:     */   private boolean localeIsAdapting;
/*   67:     */   
/*   68:     */   private class LocaleChangeObservable
/*   69:     */     extends Observable
/*   70:     */   {
/*   71:     */     private LocaleChangeObservable() {}
/*   72:     */     
/*   73:     */     void checkForLocaleChange()
/*   74:     */     {
/*   75: 217 */       checkForLocaleChange(LocaleUtil.getUserLocale());
/*   76:     */     }
/*   77:     */     
/*   78:     */     void checkForLocaleChange(Locale newLocale)
/*   79:     */     {
/*   80: 220 */       if (!DataFormatter.this.localeIsAdapting) {
/*   81: 220 */         return;
/*   82:     */       }
/*   83: 221 */       if (newLocale.equals(DataFormatter.this.locale)) {
/*   84: 221 */         return;
/*   85:     */       }
/*   86: 222 */       super.setChanged();
/*   87: 223 */       notifyObservers(newLocale);
/*   88:     */     }
/*   89:     */   }
/*   90:     */   
/*   91: 228 */   private final LocaleChangeObservable localeChangedObservable = new LocaleChangeObservable(null);
/*   92: 231 */   private static POILogger logger = POILogFactory.getLogger(DataFormatter.class);
/*   93:     */   
/*   94:     */   public DataFormatter()
/*   95:     */   {
/*   96: 237 */     this(false);
/*   97:     */   }
/*   98:     */   
/*   99:     */   public DataFormatter(boolean emulateCSV)
/*  100:     */   {
/*  101: 246 */     this(LocaleUtil.getUserLocale(), true, emulateCSV);
/*  102:     */   }
/*  103:     */   
/*  104:     */   public DataFormatter(Locale locale)
/*  105:     */   {
/*  106: 253 */     this(locale, false);
/*  107:     */   }
/*  108:     */   
/*  109:     */   public DataFormatter(Locale locale, boolean emulateCSV)
/*  110:     */   {
/*  111: 262 */     this(locale, false, emulateCSV);
/*  112:     */   }
/*  113:     */   
/*  114:     */   private DataFormatter(Locale locale, boolean localeIsAdapting, boolean emulateCSV)
/*  115:     */   {
/*  116: 271 */     this.localeIsAdapting = true;
/*  117: 272 */     this.localeChangedObservable.addObserver(this);
/*  118:     */     
/*  119: 274 */     this.localeChangedObservable.checkForLocaleChange(locale);
/*  120:     */     
/*  121:     */ 
/*  122:     */ 
/*  123: 278 */     this.localeIsAdapting = localeIsAdapting;
/*  124: 279 */     this.emulateCSV = emulateCSV;
/*  125:     */   }
/*  126:     */   
/*  127:     */   private Format getFormat(Cell cell, ConditionalFormattingEvaluator cfEvaluator)
/*  128:     */   {
/*  129: 296 */     if (cell == null) {
/*  130: 296 */       return null;
/*  131:     */     }
/*  132: 298 */     ExcelNumberFormat numFmt = ExcelNumberFormat.from(cell, cfEvaluator);
/*  133: 300 */     if (numFmt == null) {
/*  134: 301 */       return null;
/*  135:     */     }
/*  136: 304 */     int formatIndex = numFmt.getIdx();
/*  137: 305 */     String formatStr = numFmt.getFormat();
/*  138: 306 */     if ((formatStr == null) || (formatStr.trim().length() == 0)) {
/*  139: 307 */       return null;
/*  140:     */     }
/*  141: 309 */     return getFormat(cell.getNumericCellValue(), formatIndex, formatStr);
/*  142:     */   }
/*  143:     */   
/*  144:     */   private Format getFormat(double cellValue, int formatIndex, String formatStrIn)
/*  145:     */   {
/*  146: 313 */     this.localeChangedObservable.checkForLocaleChange();
/*  147:     */     
/*  148:     */ 
/*  149:     */ 
/*  150:     */ 
/*  151:     */ 
/*  152:     */ 
/*  153:     */ 
/*  154: 321 */     String formatStr = formatStrIn;
/*  155: 329 */     if ((formatStr.contains(";")) && ((formatStr.indexOf(';') != formatStr.lastIndexOf(';')) || (rangeConditionalPattern.matcher(formatStr).matches()))) {
/*  156:     */       try
/*  157:     */       {
/*  158: 335 */         CellFormat cfmt = CellFormat.getInstance(this.locale, formatStr);
/*  159:     */         
/*  160: 337 */         Object cellValueO = Double.valueOf(cellValue);
/*  161: 338 */         if ((DateUtil.isADateFormat(formatIndex, formatStr)) && (((Double)cellValueO).doubleValue() != 0.0D)) {
/*  162: 341 */           cellValueO = DateUtil.getJavaDate(cellValue);
/*  163:     */         }
/*  164: 344 */         return new CellFormatResultWrapper(cfmt.apply(cellValueO), null);
/*  165:     */       }
/*  166:     */       catch (Exception e)
/*  167:     */       {
/*  168: 346 */         logger.log(5, new Object[] { "Formatting failed for format " + formatStr + ", falling back", e });
/*  169:     */       }
/*  170:     */     }
/*  171: 351 */     if ((this.emulateCSV) && (cellValue == 0.0D) && (formatStr.contains("#")) && (!formatStr.contains("0"))) {
/*  172: 352 */       formatStr = formatStr.replaceAll("#", "");
/*  173:     */     }
/*  174: 356 */     Format format = (Format)this.formats.get(formatStr);
/*  175: 357 */     if (format != null) {
/*  176: 358 */       return format;
/*  177:     */     }
/*  178: 362 */     if (("General".equalsIgnoreCase(formatStr)) || ("@".equals(formatStr))) {
/*  179: 363 */       return this.generalNumberFormat;
/*  180:     */     }
/*  181: 367 */     format = createFormat(cellValue, formatIndex, formatStr);
/*  182: 368 */     this.formats.put(formatStr, format);
/*  183: 369 */     return format;
/*  184:     */   }
/*  185:     */   
/*  186:     */   public Format createFormat(Cell cell)
/*  187:     */   {
/*  188: 381 */     int formatIndex = cell.getCellStyle().getDataFormat();
/*  189: 382 */     String formatStr = cell.getCellStyle().getDataFormatString();
/*  190: 383 */     return createFormat(cell.getNumericCellValue(), formatIndex, formatStr);
/*  191:     */   }
/*  192:     */   
/*  193:     */   private Format createFormat(double cellValue, int formatIndex, String sFormat)
/*  194:     */   {
/*  195: 387 */     this.localeChangedObservable.checkForLocaleChange();
/*  196:     */     
/*  197: 389 */     String formatStr = sFormat;
/*  198:     */     
/*  199:     */ 
/*  200: 392 */     Matcher colourM = colorPattern.matcher(formatStr);
/*  201: 393 */     while (colourM.find())
/*  202:     */     {
/*  203: 394 */       String colour = colourM.group();
/*  204:     */       
/*  205:     */ 
/*  206: 397 */       int at = formatStr.indexOf(colour);
/*  207: 398 */       if (at == -1) {
/*  208:     */         break;
/*  209:     */       }
/*  210: 399 */       String nFormatStr = formatStr.substring(0, at) + formatStr.substring(at + colour.length());
/*  211: 401 */       if (nFormatStr.equals(formatStr)) {
/*  212:     */         break;
/*  213:     */       }
/*  214: 404 */       formatStr = nFormatStr;
/*  215: 405 */       colourM = colorPattern.matcher(formatStr);
/*  216:     */     }
/*  217: 409 */     Matcher m = localePatternGroup.matcher(formatStr);
/*  218: 410 */     while (m.find())
/*  219:     */     {
/*  220: 411 */       String match = m.group();
/*  221: 412 */       String symbol = match.substring(match.indexOf('$') + 1, match.indexOf('-'));
/*  222: 413 */       if (symbol.indexOf('$') > -1) {
/*  223: 414 */         symbol = symbol.substring(0, symbol.indexOf('$')) + '\\' + symbol.substring(symbol.indexOf('$'), symbol.length());
/*  224:     */       }
/*  225: 418 */       formatStr = m.replaceAll(symbol);
/*  226: 419 */       m = localePatternGroup.matcher(formatStr);
/*  227:     */     }
/*  228: 423 */     if ((formatStr == null) || (formatStr.trim().length() == 0)) {
/*  229: 424 */       return getDefaultFormat(cellValue);
/*  230:     */     }
/*  231: 427 */     if (("General".equalsIgnoreCase(formatStr)) || ("@".equals(formatStr))) {
/*  232: 428 */       return this.generalNumberFormat;
/*  233:     */     }
/*  234: 431 */     if ((DateUtil.isADateFormat(formatIndex, formatStr)) && (DateUtil.isValidExcelDate(cellValue))) {
/*  235: 433 */       return createDateFormat(formatStr, cellValue);
/*  236:     */     }
/*  237: 436 */     if ((formatStr.contains("#/")) || (formatStr.contains("?/")))
/*  238:     */     {
/*  239: 437 */       String[] chunks = formatStr.split(";");
/*  240: 438 */       for (String chunk1 : chunks)
/*  241:     */       {
/*  242: 439 */         String chunk = chunk1.replaceAll("\\?", "#");
/*  243: 440 */         Matcher matcher = fractionStripper.matcher(chunk);
/*  244: 441 */         chunk = matcher.replaceAll(" ");
/*  245: 442 */         chunk = chunk.replaceAll(" +", " ");
/*  246: 443 */         Matcher fractionMatcher = fractionPattern.matcher(chunk);
/*  247: 445 */         if (fractionMatcher.find())
/*  248:     */         {
/*  249: 446 */           String wholePart = fractionMatcher.group(1) == null ? "" : "#";
/*  250: 447 */           return new FractionFormat(wholePart, fractionMatcher.group(3));
/*  251:     */         }
/*  252:     */       }
/*  253: 454 */       return new FractionFormat("#", "#/##");
/*  254:     */     }
/*  255: 457 */     if (numPattern.matcher(formatStr).find()) {
/*  256: 458 */       return createNumberFormat(formatStr, cellValue);
/*  257:     */     }
/*  258: 461 */     if (this.emulateCSV) {
/*  259: 462 */       return new ConstantStringFormat(cleanFormatForNumber(formatStr));
/*  260:     */     }
/*  261: 465 */     return null;
/*  262:     */   }
/*  263:     */   
/*  264:     */   private Format createDateFormat(String pFormatStr, double cellValue)
/*  265:     */   {
/*  266: 471 */     String formatStr = pFormatStr;
/*  267: 472 */     formatStr = formatStr.replaceAll("\\\\-", "-");
/*  268: 473 */     formatStr = formatStr.replaceAll("\\\\,", ",");
/*  269: 474 */     formatStr = formatStr.replaceAll("\\\\\\.", ".");
/*  270: 475 */     formatStr = formatStr.replaceAll("\\\\ ", " ");
/*  271: 476 */     formatStr = formatStr.replaceAll("\\\\/", "/");
/*  272: 477 */     formatStr = formatStr.replaceAll(";@", "");
/*  273: 478 */     formatStr = formatStr.replaceAll("\"/\"", "/");
/*  274: 479 */     formatStr = formatStr.replace("\"\"", "'");
/*  275: 480 */     formatStr = formatStr.replaceAll("\\\\T", "'T'");
/*  276:     */     
/*  277:     */ 
/*  278: 483 */     boolean hasAmPm = false;
/*  279: 484 */     Matcher amPmMatcher = amPmPattern.matcher(formatStr);
/*  280: 485 */     while (amPmMatcher.find())
/*  281:     */     {
/*  282: 486 */       formatStr = amPmMatcher.replaceAll("@");
/*  283: 487 */       hasAmPm = true;
/*  284: 488 */       amPmMatcher = amPmPattern.matcher(formatStr);
/*  285:     */     }
/*  286: 490 */     formatStr = formatStr.replaceAll("@", "a");
/*  287:     */     
/*  288:     */ 
/*  289: 493 */     Matcher dateMatcher = daysAsText.matcher(formatStr);
/*  290: 494 */     if (dateMatcher.find())
/*  291:     */     {
/*  292: 495 */       String match = dateMatcher.group(0).toUpperCase(Locale.ROOT).replaceAll("D", "E");
/*  293: 496 */       formatStr = dateMatcher.replaceAll(match);
/*  294:     */     }
/*  295: 508 */     StringBuilder sb = new StringBuilder();
/*  296: 509 */     char[] chars = formatStr.toCharArray();
/*  297: 510 */     boolean mIsMonth = true;
/*  298: 511 */     List<Integer> ms = new ArrayList();
/*  299: 512 */     boolean isElapsed = false;
/*  300: 513 */     for (int j = 0; j < chars.length; j++)
/*  301:     */     {
/*  302: 514 */       char c = chars[j];
/*  303: 515 */       if (c == '\'')
/*  304:     */       {
/*  305: 516 */         sb.append(c);
/*  306: 517 */         j++;
/*  307: 520 */         while (j < chars.length)
/*  308:     */         {
/*  309: 521 */           c = chars[j];
/*  310: 522 */           sb.append(c);
/*  311: 523 */           if (c == '\'') {
/*  312:     */             break;
/*  313:     */           }
/*  314: 526 */           j++;
/*  315:     */         }
/*  316:     */       }
/*  317: 529 */       if ((c == '[') && (!isElapsed))
/*  318:     */       {
/*  319: 530 */         isElapsed = true;
/*  320: 531 */         mIsMonth = false;
/*  321: 532 */         sb.append(c);
/*  322:     */       }
/*  323: 534 */       else if ((c == ']') && (isElapsed))
/*  324:     */       {
/*  325: 535 */         isElapsed = false;
/*  326: 536 */         sb.append(c);
/*  327:     */       }
/*  328: 538 */       else if (isElapsed)
/*  329:     */       {
/*  330: 539 */         if ((c == 'h') || (c == 'H')) {
/*  331: 540 */           sb.append('H');
/*  332: 542 */         } else if ((c == 'm') || (c == 'M')) {
/*  333: 543 */           sb.append('m');
/*  334: 545 */         } else if ((c == 's') || (c == 'S')) {
/*  335: 546 */           sb.append('s');
/*  336:     */         } else {
/*  337: 549 */           sb.append(c);
/*  338:     */         }
/*  339:     */       }
/*  340: 552 */       else if ((c == 'h') || (c == 'H'))
/*  341:     */       {
/*  342: 553 */         mIsMonth = false;
/*  343: 554 */         if (hasAmPm) {
/*  344: 555 */           sb.append('h');
/*  345:     */         } else {
/*  346: 557 */           sb.append('H');
/*  347:     */         }
/*  348:     */       }
/*  349: 560 */       else if ((c == 'm') || (c == 'M'))
/*  350:     */       {
/*  351: 561 */         if (mIsMonth)
/*  352:     */         {
/*  353: 562 */           sb.append('M');
/*  354: 563 */           ms.add(Integer.valueOf(sb.length() - 1));
/*  355:     */         }
/*  356:     */         else
/*  357:     */         {
/*  358: 567 */           sb.append('m');
/*  359:     */         }
/*  360:     */       }
/*  361: 570 */       else if ((c == 's') || (c == 'S'))
/*  362:     */       {
/*  363: 571 */         sb.append('s');
/*  364: 573 */         for (Iterator i$ = ms.iterator(); i$.hasNext();)
/*  365:     */         {
/*  366: 573 */           int index = ((Integer)i$.next()).intValue();
/*  367: 574 */           if (sb.charAt(index) == 'M') {
/*  368: 575 */             sb.replace(index, index + 1, "m");
/*  369:     */           }
/*  370:     */         }
/*  371: 578 */         mIsMonth = true;
/*  372: 579 */         ms.clear();
/*  373:     */       }
/*  374: 581 */       else if (Character.isLetter(c))
/*  375:     */       {
/*  376: 582 */         mIsMonth = true;
/*  377: 583 */         ms.clear();
/*  378: 584 */         if ((c == 'y') || (c == 'Y')) {
/*  379: 585 */           sb.append('y');
/*  380: 587 */         } else if ((c == 'd') || (c == 'D')) {
/*  381: 588 */           sb.append('d');
/*  382:     */         } else {
/*  383: 591 */           sb.append(c);
/*  384:     */         }
/*  385:     */       }
/*  386:     */       else
/*  387:     */       {
/*  388: 595 */         if (Character.isWhitespace(c)) {
/*  389: 596 */           ms.clear();
/*  390:     */         }
/*  391: 598 */         sb.append(c);
/*  392:     */       }
/*  393:     */     }
/*  394: 601 */     formatStr = sb.toString();
/*  395:     */     try
/*  396:     */     {
/*  397: 604 */       return new ExcelStyleDateFormatter(formatStr, this.dateSymbols);
/*  398:     */     }
/*  399:     */     catch (IllegalArgumentException iae)
/*  400:     */     {
/*  401: 606 */       logger.log(1, new Object[] { "Formatting failed for format " + formatStr + ", falling back", iae });
/*  402:     */     }
/*  403: 609 */     return getDefaultFormat(cellValue);
/*  404:     */   }
/*  405:     */   
/*  406:     */   private String cleanFormatForNumber(String formatStr)
/*  407:     */   {
/*  408: 615 */     StringBuilder sb = new StringBuilder(formatStr);
/*  409: 617 */     if (this.emulateCSV) {
/*  410: 622 */       for (int i = 0; i < sb.length(); i++)
/*  411:     */       {
/*  412: 623 */         char c = sb.charAt(i);
/*  413: 624 */         if (((c == '_') || (c == '*') || (c == '?')) && (
/*  414: 625 */           (i <= 0) || (sb.charAt(i - 1) != '\\'))) {
/*  415: 629 */           if (c == '?')
/*  416:     */           {
/*  417: 630 */             sb.setCharAt(i, ' ');
/*  418:     */           }
/*  419: 631 */           else if (i < sb.length() - 1)
/*  420:     */           {
/*  421: 635 */             if (c == '_') {
/*  422: 636 */               sb.setCharAt(i + 1, ' ');
/*  423:     */             } else {
/*  424: 638 */               sb.deleteCharAt(i + 1);
/*  425:     */             }
/*  426: 641 */             sb.deleteCharAt(i);
/*  427: 642 */             i--;
/*  428:     */           }
/*  429:     */         }
/*  430:     */       }
/*  431:     */     } else {
/*  432: 651 */       for (int i = 0; i < sb.length(); i++)
/*  433:     */       {
/*  434: 652 */         char c = sb.charAt(i);
/*  435: 653 */         if (((c == '_') || (c == '*')) && (
/*  436: 654 */           (i <= 0) || (sb.charAt(i - 1) != '\\')))
/*  437:     */         {
/*  438: 658 */           if (i < sb.length() - 1) {
/*  439: 662 */             sb.deleteCharAt(i + 1);
/*  440:     */           }
/*  441: 665 */           sb.deleteCharAt(i);
/*  442: 666 */           i--;
/*  443:     */         }
/*  444:     */       }
/*  445:     */     }
/*  446: 673 */     for (int i = 0; i < sb.length(); i++)
/*  447:     */     {
/*  448: 674 */       char c = sb.charAt(i);
/*  449: 676 */       if ((c == '\\') || (c == '"'))
/*  450:     */       {
/*  451: 677 */         sb.deleteCharAt(i);
/*  452: 678 */         i--;
/*  453:     */       }
/*  454: 681 */       else if ((c == '+') && (i > 0) && (sb.charAt(i - 1) == 'E'))
/*  455:     */       {
/*  456: 682 */         sb.deleteCharAt(i);
/*  457: 683 */         i--;
/*  458:     */       }
/*  459:     */     }
/*  460: 687 */     return sb.toString();
/*  461:     */   }
/*  462:     */   
/*  463:     */   private static class InternalDecimalFormatWithScale
/*  464:     */     extends Format
/*  465:     */   {
/*  466: 692 */     private static final Pattern endsWithCommas = Pattern.compile("(,+)$");
/*  467:     */     private BigDecimal divider;
/*  468: 694 */     private static final BigDecimal ONE_THOUSAND = new BigDecimal(1000);
/*  469:     */     private final DecimalFormat df;
/*  470:     */     
/*  471:     */     private static final String trimTrailingCommas(String s)
/*  472:     */     {
/*  473: 697 */       return s.replaceAll(",+$", "");
/*  474:     */     }
/*  475:     */     
/*  476:     */     public InternalDecimalFormatWithScale(String pattern, DecimalFormatSymbols symbols)
/*  477:     */     {
/*  478: 701 */       this.df = new DecimalFormat(trimTrailingCommas(pattern), symbols);
/*  479: 702 */       DataFormatter.setExcelStyleRoundingMode(this.df);
/*  480: 703 */       Matcher endsWithCommasMatcher = endsWithCommas.matcher(pattern);
/*  481: 704 */       if (endsWithCommasMatcher.find())
/*  482:     */       {
/*  483: 705 */         String commas = endsWithCommasMatcher.group(1);
/*  484: 706 */         BigDecimal temp = BigDecimal.ONE;
/*  485: 707 */         for (int i = 0; i < commas.length(); i++) {
/*  486: 708 */           temp = temp.multiply(ONE_THOUSAND);
/*  487:     */         }
/*  488: 710 */         this.divider = temp;
/*  489:     */       }
/*  490:     */       else
/*  491:     */       {
/*  492: 712 */         this.divider = null;
/*  493:     */       }
/*  494:     */     }
/*  495:     */     
/*  496:     */     private Object scaleInput(Object obj)
/*  497:     */     {
/*  498: 717 */       if (this.divider != null) {
/*  499: 718 */         if ((obj instanceof BigDecimal)) {
/*  500: 719 */           obj = ((BigDecimal)obj).divide(this.divider, RoundingMode.HALF_UP);
/*  501: 720 */         } else if ((obj instanceof Double)) {
/*  502: 721 */           obj = Double.valueOf(((Double)obj).doubleValue() / this.divider.doubleValue());
/*  503:     */         } else {
/*  504: 723 */           throw new UnsupportedOperationException();
/*  505:     */         }
/*  506:     */       }
/*  507: 726 */       return obj;
/*  508:     */     }
/*  509:     */     
/*  510:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  511:     */     {
/*  512: 731 */       obj = scaleInput(obj);
/*  513: 732 */       return this.df.format(obj, toAppendTo, pos);
/*  514:     */     }
/*  515:     */     
/*  516:     */     public Object parseObject(String source, ParsePosition pos)
/*  517:     */     {
/*  518: 737 */       throw new UnsupportedOperationException();
/*  519:     */     }
/*  520:     */   }
/*  521:     */   
/*  522:     */   private Format createNumberFormat(String formatStr, double cellValue)
/*  523:     */   {
/*  524: 742 */     String format = cleanFormatForNumber(formatStr);
/*  525: 743 */     DecimalFormatSymbols symbols = this.decimalSymbols;
/*  526:     */     
/*  527:     */ 
/*  528:     */ 
/*  529: 747 */     Matcher agm = alternateGrouping.matcher(format);
/*  530: 748 */     if (agm.find())
/*  531:     */     {
/*  532: 749 */       char grouping = agm.group(2).charAt(0);
/*  533: 753 */       if (grouping != ',')
/*  534:     */       {
/*  535: 754 */         symbols = DecimalFormatSymbols.getInstance(this.locale);
/*  536:     */         
/*  537: 756 */         symbols.setGroupingSeparator(grouping);
/*  538: 757 */         String oldPart = agm.group(1);
/*  539: 758 */         String newPart = oldPart.replace(grouping, ',');
/*  540: 759 */         format = format.replace(oldPart, newPart);
/*  541:     */       }
/*  542:     */     }
/*  543:     */     try
/*  544:     */     {
/*  545: 764 */       return new InternalDecimalFormatWithScale(format, symbols);
/*  546:     */     }
/*  547:     */     catch (IllegalArgumentException iae)
/*  548:     */     {
/*  549: 766 */       logger.log(1, new Object[] { "Formatting failed for format " + formatStr + ", falling back", iae });
/*  550:     */     }
/*  551: 769 */     return getDefaultFormat(cellValue);
/*  552:     */   }
/*  553:     */   
/*  554:     */   public Format getDefaultFormat(Cell cell)
/*  555:     */   {
/*  556: 779 */     return getDefaultFormat(cell.getNumericCellValue());
/*  557:     */   }
/*  558:     */   
/*  559:     */   private Format getDefaultFormat(double cellValue)
/*  560:     */   {
/*  561: 782 */     this.localeChangedObservable.checkForLocaleChange();
/*  562: 785 */     if (this.defaultNumFormat != null) {
/*  563: 786 */       return this.defaultNumFormat;
/*  564:     */     }
/*  565: 790 */     return this.generalNumberFormat;
/*  566:     */   }
/*  567:     */   
/*  568:     */   private String performDateFormatting(Date d, Format dateFormat)
/*  569:     */   {
/*  570: 798 */     return (dateFormat != null ? dateFormat : this.defaultDateformat).format(d);
/*  571:     */   }
/*  572:     */   
/*  573:     */   private String getFormattedDateString(Cell cell, ConditionalFormattingEvaluator cfEvaluator)
/*  574:     */   {
/*  575: 815 */     Format dateFormat = getFormat(cell, cfEvaluator);
/*  576: 816 */     if ((dateFormat instanceof ExcelStyleDateFormatter)) {
/*  577: 818 */       ((ExcelStyleDateFormatter)dateFormat).setDateToBeFormatted(cell.getNumericCellValue());
/*  578:     */     }
/*  579: 822 */     Date d = cell.getDateCellValue();
/*  580: 823 */     return performDateFormatting(d, dateFormat);
/*  581:     */   }
/*  582:     */   
/*  583:     */   private String getFormattedNumberString(Cell cell, ConditionalFormattingEvaluator cfEvaluator)
/*  584:     */   {
/*  585: 841 */     Format numberFormat = getFormat(cell, cfEvaluator);
/*  586: 842 */     double d = cell.getNumericCellValue();
/*  587: 843 */     if (numberFormat == null) {
/*  588: 844 */       return String.valueOf(d);
/*  589:     */     }
/*  590: 846 */     String formatted = numberFormat.format(new Double(d));
/*  591: 847 */     return formatted.replaceFirst("E(\\d)", "E+$1");
/*  592:     */   }
/*  593:     */   
/*  594:     */   public String formatRawCellContents(double value, int formatIndex, String formatString)
/*  595:     */   {
/*  596: 856 */     return formatRawCellContents(value, formatIndex, formatString, false);
/*  597:     */   }
/*  598:     */   
/*  599:     */   public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing)
/*  600:     */   {
/*  601: 864 */     this.localeChangedObservable.checkForLocaleChange();
/*  602: 867 */     if (DateUtil.isADateFormat(formatIndex, formatString))
/*  603:     */     {
/*  604: 868 */       if (DateUtil.isValidExcelDate(value))
/*  605:     */       {
/*  606: 869 */         Format dateFormat = getFormat(value, formatIndex, formatString);
/*  607: 870 */         if ((dateFormat instanceof ExcelStyleDateFormatter)) {
/*  608: 872 */           ((ExcelStyleDateFormatter)dateFormat).setDateToBeFormatted(value);
/*  609:     */         }
/*  610: 874 */         Date d = DateUtil.getJavaDate(value, use1904Windowing);
/*  611: 875 */         return performDateFormatting(d, dateFormat);
/*  612:     */       }
/*  613: 878 */       if (this.emulateCSV) {
/*  614: 879 */         return invalidDateTimeString;
/*  615:     */       }
/*  616:     */     }
/*  617: 884 */     Format numberFormat = getFormat(value, formatIndex, formatString);
/*  618: 885 */     if (numberFormat == null) {
/*  619: 886 */       return String.valueOf(value);
/*  620:     */     }
/*  621: 895 */     String textValue = NumberToTextConverter.toText(value);
/*  622:     */     String result;
/*  623:     */     String result;
/*  624: 896 */     if (textValue.indexOf('E') > -1) {
/*  625: 897 */       result = numberFormat.format(new Double(value));
/*  626:     */     } else {
/*  627: 900 */       result = numberFormat.format(new BigDecimal(textValue));
/*  628:     */     }
/*  629: 903 */     if ((result.indexOf('E') > -1) && (!result.contains("E-"))) {
/*  630: 904 */       result = result.replaceFirst("E", "E+");
/*  631:     */     }
/*  632: 906 */     return result;
/*  633:     */   }
/*  634:     */   
/*  635:     */   public String formatCellValue(Cell cell)
/*  636:     */   {
/*  637: 923 */     return formatCellValue(cell, null);
/*  638:     */   }
/*  639:     */   
/*  640:     */   public String formatCellValue(Cell cell, FormulaEvaluator evaluator)
/*  641:     */   {
/*  642: 944 */     return formatCellValue(cell, evaluator, null);
/*  643:     */   }
/*  644:     */   
/*  645:     */   public String formatCellValue(Cell cell, FormulaEvaluator evaluator, ConditionalFormattingEvaluator cfEvaluator)
/*  646:     */   {
/*  647: 975 */     this.localeChangedObservable.checkForLocaleChange();
/*  648: 977 */     if (cell == null) {
/*  649: 978 */       return "";
/*  650:     */     }
/*  651: 981 */     CellType cellType = cell.getCellTypeEnum();
/*  652: 982 */     if (cellType == CellType.FORMULA)
/*  653:     */     {
/*  654: 983 */       if (evaluator == null) {
/*  655: 984 */         return cell.getCellFormula();
/*  656:     */       }
/*  657: 986 */       cellType = evaluator.evaluateFormulaCellEnum(cell);
/*  658:     */     }
/*  659: 988 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/*  660:     */     {
/*  661:     */     case 1: 
/*  662: 991 */       if (DateUtil.isCellDateFormatted(cell, cfEvaluator)) {
/*  663: 992 */         return getFormattedDateString(cell, cfEvaluator);
/*  664:     */       }
/*  665: 994 */       return getFormattedNumberString(cell, cfEvaluator);
/*  666:     */     case 2: 
/*  667: 997 */       return cell.getRichStringCellValue().getString();
/*  668:     */     case 3: 
/*  669:1000 */       return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
/*  670:     */     case 4: 
/*  671:1002 */       return "";
/*  672:     */     case 5: 
/*  673:1004 */       return FormulaError.forInt(cell.getErrorCellValue()).getString();
/*  674:     */     }
/*  675:1006 */     throw new RuntimeException("Unexpected celltype (" + cellType + ")");
/*  676:     */   }
/*  677:     */   
/*  678:     */   public void setDefaultNumberFormat(Format format)
/*  679:     */   {
/*  680:1029 */     for (Entry<String, Format> entry : this.formats.entrySet()) {
/*  681:1030 */       if (entry.getValue() == this.generalNumberFormat) {
/*  682:1031 */         entry.setValue(format);
/*  683:     */       }
/*  684:     */     }
/*  685:1034 */     this.defaultNumFormat = format;
/*  686:     */   }
/*  687:     */   
/*  688:     */   public void addFormat(String excelFormatStr, Format format)
/*  689:     */   {
/*  690:1049 */     this.formats.put(excelFormatStr, format);
/*  691:     */   }
/*  692:     */   
/*  693:     */   private static DecimalFormat createIntegerOnlyFormat(String fmt)
/*  694:     */   {
/*  695:1058 */     DecimalFormatSymbols dsf = DecimalFormatSymbols.getInstance(Locale.ROOT);
/*  696:1059 */     DecimalFormat result = new DecimalFormat(fmt, dsf);
/*  697:1060 */     result.setParseIntegerOnly(true);
/*  698:1061 */     return result;
/*  699:     */   }
/*  700:     */   
/*  701:     */   public static void setExcelStyleRoundingMode(DecimalFormat format)
/*  702:     */   {
/*  703:1069 */     setExcelStyleRoundingMode(format, RoundingMode.HALF_UP);
/*  704:     */   }
/*  705:     */   
/*  706:     */   public static void setExcelStyleRoundingMode(DecimalFormat format, RoundingMode roundingMode)
/*  707:     */   {
/*  708:1078 */     format.setRoundingMode(roundingMode);
/*  709:     */   }
/*  710:     */   
/*  711:     */   public Observable getLocaleChangedObservable()
/*  712:     */   {
/*  713:1092 */     return this.localeChangedObservable;
/*  714:     */   }
/*  715:     */   
/*  716:     */   public void update(Observable observable, Object localeObj)
/*  717:     */   {
/*  718:1102 */     if (!(localeObj instanceof Locale)) {
/*  719:1102 */       return;
/*  720:     */     }
/*  721:1103 */     Locale newLocale = (Locale)localeObj;
/*  722:1104 */     if ((!this.localeIsAdapting) || (newLocale.equals(this.locale))) {
/*  723:1104 */       return;
/*  724:     */     }
/*  725:1106 */     this.locale = newLocale;
/*  726:     */     
/*  727:1108 */     this.dateSymbols = DateFormatSymbols.getInstance(this.locale);
/*  728:1109 */     this.decimalSymbols = DecimalFormatSymbols.getInstance(this.locale);
/*  729:1110 */     this.generalNumberFormat = new ExcelGeneralNumberFormat(this.locale);
/*  730:     */     
/*  731:     */ 
/*  732:1113 */     this.defaultDateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", this.dateSymbols);
/*  733:1114 */     this.defaultDateformat.setTimeZone(LocaleUtil.getUserTimeZone());
/*  734:     */     
/*  735:     */ 
/*  736:     */ 
/*  737:1118 */     this.formats.clear();
/*  738:1119 */     Format zipFormat = ZipPlusFourFormat.instance;
/*  739:1120 */     addFormat("00000\\-0000", zipFormat);
/*  740:1121 */     addFormat("00000-0000", zipFormat);
/*  741:     */     
/*  742:1123 */     Format phoneFormat = PhoneFormat.instance;
/*  743:     */     
/*  744:1125 */     addFormat("[<=9999999]###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
/*  745:1126 */     addFormat("[<=9999999]###-####;(###) ###-####", phoneFormat);
/*  746:1127 */     addFormat("###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
/*  747:1128 */     addFormat("###-####;(###) ###-####", phoneFormat);
/*  748:     */     
/*  749:1130 */     Format ssnFormat = SSNFormat.instance;
/*  750:1131 */     addFormat("000\\-00\\-0000", ssnFormat);
/*  751:1132 */     addFormat("000-00-0000", ssnFormat);
/*  752:     */   }
/*  753:     */   
/*  754:     */   private static final class SSNFormat
/*  755:     */     extends Format
/*  756:     */   {
/*  757:1145 */     public static final Format instance = new SSNFormat();
/*  758:1146 */     private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("000000000");
/*  759:     */     
/*  760:     */     public static String format(Number num)
/*  761:     */     {
/*  762:1153 */       String result = df.format(num);
/*  763:1154 */       return result.substring(0, 3) + '-' + result.substring(3, 5) + '-' + result.substring(5, 9);
/*  764:     */     }
/*  765:     */     
/*  766:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  767:     */     {
/*  768:1161 */       return toAppendTo.append(format((Number)obj));
/*  769:     */     }
/*  770:     */     
/*  771:     */     public Object parseObject(String source, ParsePosition pos)
/*  772:     */     {
/*  773:1166 */       return df.parseObject(source, pos);
/*  774:     */     }
/*  775:     */   }
/*  776:     */   
/*  777:     */   private static final class ZipPlusFourFormat
/*  778:     */     extends Format
/*  779:     */   {
/*  780:1177 */     public static final Format instance = new ZipPlusFourFormat();
/*  781:1178 */     private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("000000000");
/*  782:     */     
/*  783:     */     public static String format(Number num)
/*  784:     */     {
/*  785:1185 */       String result = df.format(num);
/*  786:1186 */       return result.substring(0, 5) + '-' + result.substring(5, 9);
/*  787:     */     }
/*  788:     */     
/*  789:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  790:     */     {
/*  791:1192 */       return toAppendTo.append(format((Number)obj));
/*  792:     */     }
/*  793:     */     
/*  794:     */     public Object parseObject(String source, ParsePosition pos)
/*  795:     */     {
/*  796:1197 */       return df.parseObject(source, pos);
/*  797:     */     }
/*  798:     */   }
/*  799:     */   
/*  800:     */   private static final class PhoneFormat
/*  801:     */     extends Format
/*  802:     */   {
/*  803:1208 */     public static final Format instance = new PhoneFormat();
/*  804:1209 */     private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("##########");
/*  805:     */     
/*  806:     */     public static String format(Number num)
/*  807:     */     {
/*  808:1216 */       String result = df.format(num);
/*  809:1217 */       StringBuilder sb = new StringBuilder();
/*  810:     */       
/*  811:1219 */       int len = result.length();
/*  812:1220 */       if (len <= 4) {
/*  813:1221 */         return result;
/*  814:     */       }
/*  815:1224 */       String seg3 = result.substring(len - 4, len);
/*  816:1225 */       String seg2 = result.substring(Math.max(0, len - 7), len - 4);
/*  817:1226 */       String seg1 = result.substring(Math.max(0, len - 10), Math.max(0, len - 7));
/*  818:1228 */       if (seg1.trim().length() > 0) {
/*  819:1229 */         sb.append('(').append(seg1).append(") ");
/*  820:     */       }
/*  821:1231 */       if (seg2.trim().length() > 0) {
/*  822:1232 */         sb.append(seg2).append('-');
/*  823:     */       }
/*  824:1234 */       sb.append(seg3);
/*  825:1235 */       return sb.toString();
/*  826:     */     }
/*  827:     */     
/*  828:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  829:     */     {
/*  830:1240 */       return toAppendTo.append(format((Number)obj));
/*  831:     */     }
/*  832:     */     
/*  833:     */     public Object parseObject(String source, ParsePosition pos)
/*  834:     */     {
/*  835:1245 */       return df.parseObject(source, pos);
/*  836:     */     }
/*  837:     */   }
/*  838:     */   
/*  839:     */   private static final class ConstantStringFormat
/*  840:     */     extends Format
/*  841:     */   {
/*  842:1262 */     private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("##########");
/*  843:     */     private final String str;
/*  844:     */     
/*  845:     */     public ConstantStringFormat(String s)
/*  846:     */     {
/*  847:1265 */       this.str = s;
/*  848:     */     }
/*  849:     */     
/*  850:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  851:     */     {
/*  852:1270 */       return toAppendTo.append(this.str);
/*  853:     */     }
/*  854:     */     
/*  855:     */     public Object parseObject(String source, ParsePosition pos)
/*  856:     */     {
/*  857:1275 */       return df.parseObject(source, pos);
/*  858:     */     }
/*  859:     */   }
/*  860:     */   
/*  861:     */   private final class CellFormatResultWrapper
/*  862:     */     extends Format
/*  863:     */   {
/*  864:     */     private final CellFormatResult result;
/*  865:     */     
/*  866:     */     private CellFormatResultWrapper(CellFormatResult result)
/*  867:     */     {
/*  868:1286 */       this.result = result;
/*  869:     */     }
/*  870:     */     
/*  871:     */     public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*  872:     */     {
/*  873:1289 */       if (DataFormatter.this.emulateCSV) {
/*  874:1290 */         return toAppendTo.append(this.result.text);
/*  875:     */       }
/*  876:1292 */       return toAppendTo.append(this.result.text.trim());
/*  877:     */     }
/*  878:     */     
/*  879:     */     public Object parseObject(String source, ParsePosition pos)
/*  880:     */     {
/*  881:1296 */       return null;
/*  882:     */     }
/*  883:     */   }
/*  884:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.usermodel.DataFormatter

 * JD-Core Version:    0.7.0.1

 */