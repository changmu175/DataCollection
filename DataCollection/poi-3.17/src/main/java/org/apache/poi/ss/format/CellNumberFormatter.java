/*   1:    */ package org.apache.poi.ss.format;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.DecimalFormatSymbols;
/*   5:    */ import java.text.FieldPosition;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.BitSet;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.Formatter;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.ListIterator;
/*  13:    */ import java.util.Locale;
/*  14:    */ import java.util.Set;
/*  15:    */ import java.util.TreeSet;
/*  16:    */ import org.apache.poi.util.LocaleUtil;
/*  17:    */ import org.apache.poi.util.POILogFactory;
/*  18:    */ import org.apache.poi.util.POILogger;
/*  19:    */ 
/*  20:    */ public class CellNumberFormatter
/*  21:    */   extends CellFormatter
/*  22:    */ {
/*  23: 41 */   private static final POILogger LOG = POILogFactory.getLogger(CellNumberFormatter.class);
/*  24:    */   private final String desc;
/*  25:    */   private final String printfFmt;
/*  26:    */   private final double scale;
/*  27:    */   private final Special decimalPoint;
/*  28:    */   private final Special slash;
/*  29:    */   private final Special exponent;
/*  30:    */   private final Special numerator;
/*  31:    */   private final Special afterInteger;
/*  32:    */   private final Special afterFractional;
/*  33:    */   private final boolean showGroupingSeparator;
/*  34: 53 */   private final List<Special> specials = new ArrayList();
/*  35: 54 */   private final List<Special> integerSpecials = new ArrayList();
/*  36: 55 */   private final List<Special> fractionalSpecials = new ArrayList();
/*  37: 56 */   private final List<Special> numeratorSpecials = new ArrayList();
/*  38: 57 */   private final List<Special> denominatorSpecials = new ArrayList();
/*  39: 58 */   private final List<Special> exponentSpecials = new ArrayList();
/*  40: 59 */   private final List<Special> exponentDigitSpecials = new ArrayList();
/*  41:    */   private final int maxDenominator;
/*  42:    */   private final String numeratorFmt;
/*  43:    */   private final String denominatorFmt;
/*  44:    */   private final boolean improperFraction;
/*  45:    */   private final DecimalFormat decimalFmt;
/*  46: 73 */   private final CellFormatter SIMPLE_NUMBER = new GeneralNumberFormatter(this.locale, null);
/*  47:    */   
/*  48:    */   private static class GeneralNumberFormatter
/*  49:    */     extends CellFormatter
/*  50:    */   {
/*  51:    */     private GeneralNumberFormatter(Locale locale)
/*  52:    */     {
/*  53: 77 */       super("General");
/*  54:    */     }
/*  55:    */     
/*  56:    */     public void formatValue(StringBuffer toAppendTo, Object value)
/*  57:    */     {
/*  58: 81 */       if (value == null) {
/*  59:    */         return;
/*  60:    */       }
/*  61:    */       CellFormatter cf;
/*  62:    */       CellFormatter cf;
/*  63: 86 */       if ((value instanceof Number))
/*  64:    */       {
/*  65: 87 */         Number num = (Number)value;
/*  66: 88 */         cf = num.doubleValue() % 1.0D == 0.0D ? new CellNumberFormatter(this.locale, "#") : new CellNumberFormatter(this.locale, "#.#");
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70: 91 */         cf = CellTextFormatter.SIMPLE_TEXT;
/*  71:    */       }
/*  72: 93 */       cf.formatValue(toAppendTo, value);
/*  73:    */     }
/*  74:    */     
/*  75:    */     public void simpleValue(StringBuffer toAppendTo, Object value)
/*  76:    */     {
/*  77: 97 */       formatValue(toAppendTo, value);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   static class Special
/*  82:    */   {
/*  83:    */     final char ch;
/*  84:    */     int pos;
/*  85:    */     
/*  86:    */     Special(char ch, int pos)
/*  87:    */     {
/*  88:111 */       this.ch = ch;
/*  89:112 */       this.pos = pos;
/*  90:    */     }
/*  91:    */     
/*  92:    */     public String toString()
/*  93:    */     {
/*  94:117 */       return "'" + this.ch + "' @ " + this.pos;
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   public CellNumberFormatter(String format)
/*  99:    */   {
/* 100:127 */     this(LocaleUtil.getUserLocale(), format);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public CellNumberFormatter(Locale locale, String format)
/* 104:    */   {
/* 105:137 */     super(locale, format);
/* 106:    */     
/* 107:139 */     CellNumberPartHandler ph = new CellNumberPartHandler();
/* 108:140 */     StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.NUMBER, ph);
/* 109:    */     
/* 110:142 */     this.exponent = ph.getExponent();
/* 111:143 */     this.specials.addAll(ph.getSpecials());
/* 112:144 */     this.improperFraction = ph.isImproperFraction();
/* 113:147 */     if (((ph.getDecimalPoint() != null) || (ph.getExponent() != null)) && (ph.getSlash() != null))
/* 114:    */     {
/* 115:148 */       this.slash = null;
/* 116:149 */       this.numerator = null;
/* 117:    */     }
/* 118:    */     else
/* 119:    */     {
/* 120:151 */       this.slash = ph.getSlash();
/* 121:152 */       this.numerator = ph.getNumerator();
/* 122:    */     }
/* 123:155 */     int precision = interpretPrecision(ph.getDecimalPoint(), this.specials);
/* 124:156 */     int fractionPartWidth = 0;
/* 125:157 */     if (ph.getDecimalPoint() != null)
/* 126:    */     {
/* 127:158 */       fractionPartWidth = 1 + precision;
/* 128:159 */       if (precision == 0)
/* 129:    */       {
/* 130:162 */         this.specials.remove(ph.getDecimalPoint());
/* 131:163 */         this.decimalPoint = null;
/* 132:    */       }
/* 133:    */       else
/* 134:    */       {
/* 135:165 */         this.decimalPoint = ph.getDecimalPoint();
/* 136:    */       }
/* 137:    */     }
/* 138:    */     else
/* 139:    */     {
/* 140:168 */       this.decimalPoint = null;
/* 141:    */     }
/* 142:171 */     if (this.decimalPoint != null) {
/* 143:172 */       this.afterInteger = this.decimalPoint;
/* 144:173 */     } else if (this.exponent != null) {
/* 145:174 */       this.afterInteger = this.exponent;
/* 146:175 */     } else if (this.numerator != null) {
/* 147:176 */       this.afterInteger = this.numerator;
/* 148:    */     } else {
/* 149:178 */       this.afterInteger = null;
/* 150:    */     }
/* 151:181 */     if (this.exponent != null) {
/* 152:182 */       this.afterFractional = this.exponent;
/* 153:183 */     } else if (this.numerator != null) {
/* 154:184 */       this.afterFractional = this.numerator;
/* 155:    */     } else {
/* 156:186 */       this.afterFractional = null;
/* 157:    */     }
/* 158:189 */     double[] scaleByRef = { ph.getScale() };
/* 159:190 */     this.showGroupingSeparator = interpretIntegerCommas(descBuf, this.specials, this.decimalPoint, integerEnd(), fractionalEnd(), scaleByRef);
/* 160:191 */     if (this.exponent == null) {
/* 161:192 */       this.scale = scaleByRef[0];
/* 162:    */     } else {
/* 163:195 */       this.scale = 1.0D;
/* 164:    */     }
/* 165:198 */     if (precision != 0) {
/* 166:200 */       this.fractionalSpecials.addAll(this.specials.subList(this.specials.indexOf(this.decimalPoint) + 1, fractionalEnd()));
/* 167:    */     }
/* 168:203 */     if (this.exponent != null)
/* 169:    */     {
/* 170:204 */       int exponentPos = this.specials.indexOf(this.exponent);
/* 171:205 */       this.exponentSpecials.addAll(specialsFor(exponentPos, 2));
/* 172:206 */       this.exponentDigitSpecials.addAll(specialsFor(exponentPos + 2));
/* 173:    */     }
/* 174:209 */     if (this.slash != null)
/* 175:    */     {
/* 176:210 */       if (this.numerator != null) {
/* 177:211 */         this.numeratorSpecials.addAll(specialsFor(this.specials.indexOf(this.numerator)));
/* 178:    */       }
/* 179:214 */       this.denominatorSpecials.addAll(specialsFor(this.specials.indexOf(this.slash) + 1));
/* 180:215 */       if (this.denominatorSpecials.isEmpty())
/* 181:    */       {
/* 182:217 */         this.numeratorSpecials.clear();
/* 183:218 */         this.maxDenominator = 1;
/* 184:219 */         this.numeratorFmt = null;
/* 185:220 */         this.denominatorFmt = null;
/* 186:    */       }
/* 187:    */       else
/* 188:    */       {
/* 189:222 */         this.maxDenominator = maxValue(this.denominatorSpecials);
/* 190:223 */         this.numeratorFmt = singleNumberFormat(this.numeratorSpecials);
/* 191:224 */         this.denominatorFmt = singleNumberFormat(this.denominatorSpecials);
/* 192:    */       }
/* 193:    */     }
/* 194:    */     else
/* 195:    */     {
/* 196:227 */       this.maxDenominator = 1;
/* 197:228 */       this.numeratorFmt = null;
/* 198:229 */       this.denominatorFmt = null;
/* 199:    */     }
/* 200:232 */     this.integerSpecials.addAll(this.specials.subList(0, integerEnd()));
/* 201:234 */     if (this.exponent == null)
/* 202:    */     {
/* 203:235 */       StringBuffer fmtBuf = new StringBuffer("%");
/* 204:    */       
/* 205:237 */       int integerPartWidth = calculateIntegerPartWidth();
/* 206:238 */       int totalWidth = integerPartWidth + fractionPartWidth;
/* 207:    */       
/* 208:240 */       fmtBuf.append('0').append(totalWidth).append('.').append(precision);
/* 209:    */       
/* 210:242 */       fmtBuf.append("f");
/* 211:243 */       this.printfFmt = fmtBuf.toString();
/* 212:244 */       this.decimalFmt = null;
/* 213:    */     }
/* 214:    */     else
/* 215:    */     {
/* 216:246 */       StringBuffer fmtBuf = new StringBuffer();
/* 217:247 */       boolean first = true;
/* 218:248 */       List<Special> specialList = this.integerSpecials;
/* 219:249 */       if (this.integerSpecials.size() == 1)
/* 220:    */       {
/* 221:251 */         fmtBuf.append("0");
/* 222:252 */         first = false;
/* 223:    */       }
/* 224:    */       else
/* 225:    */       {
/* 226:254 */         for (Special s : specialList) {
/* 227:255 */           if (isDigitFmt(s))
/* 228:    */           {
/* 229:256 */             fmtBuf.append(first ? '#' : '0');
/* 230:257 */             first = false;
/* 231:    */           }
/* 232:    */         }
/* 233:    */       }
/* 234:260 */       if (this.fractionalSpecials.size() > 0)
/* 235:    */       {
/* 236:261 */         fmtBuf.append('.');
/* 237:262 */         for (Special s : this.fractionalSpecials) {
/* 238:263 */           if (isDigitFmt(s))
/* 239:    */           {
/* 240:264 */             if (!first) {
/* 241:265 */               fmtBuf.append('0');
/* 242:    */             }
/* 243:266 */             first = false;
/* 244:    */           }
/* 245:    */         }
/* 246:    */       }
/* 247:270 */       fmtBuf.append('E');
/* 248:271 */       placeZeros(fmtBuf, this.exponentSpecials.subList(2, this.exponentSpecials.size()));
/* 249:272 */       this.decimalFmt = new DecimalFormat(fmtBuf.toString(), getDecimalFormatSymbols());
/* 250:273 */       this.printfFmt = null;
/* 251:    */     }
/* 252:276 */     this.desc = descBuf.toString();
/* 253:    */   }
/* 254:    */   
/* 255:    */   private DecimalFormatSymbols getDecimalFormatSymbols()
/* 256:    */   {
/* 257:280 */     return DecimalFormatSymbols.getInstance(this.locale);
/* 258:    */   }
/* 259:    */   
/* 260:    */   private static void placeZeros(StringBuffer sb, List<Special> specials)
/* 261:    */   {
/* 262:284 */     for (Special s : specials) {
/* 263:285 */       if (isDigitFmt(s)) {
/* 264:286 */         sb.append('0');
/* 265:    */       }
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   private static CellNumberStringMod insertMod(Special special, CharSequence toAdd, int where)
/* 270:    */   {
/* 271:292 */     return new CellNumberStringMod(special, toAdd, where);
/* 272:    */   }
/* 273:    */   
/* 274:    */   private static CellNumberStringMod deleteMod(Special start, boolean startInclusive, Special end, boolean endInclusive)
/* 275:    */   {
/* 276:296 */     return new CellNumberStringMod(start, startInclusive, end, endInclusive);
/* 277:    */   }
/* 278:    */   
/* 279:    */   private static CellNumberStringMod replaceMod(Special start, boolean startInclusive, Special end, boolean endInclusive, char withChar)
/* 280:    */   {
/* 281:300 */     return new CellNumberStringMod(start, startInclusive, end, endInclusive, withChar);
/* 282:    */   }
/* 283:    */   
/* 284:    */   private static String singleNumberFormat(List<Special> numSpecials)
/* 285:    */   {
/* 286:304 */     return "%0" + numSpecials.size() + "d";
/* 287:    */   }
/* 288:    */   
/* 289:    */   private static int maxValue(List<Special> s)
/* 290:    */   {
/* 291:308 */     return (int)Math.round(Math.pow(10.0D, s.size()) - 1.0D);
/* 292:    */   }
/* 293:    */   
/* 294:    */   private List<Special> specialsFor(int pos, int takeFirst)
/* 295:    */   {
/* 296:312 */     if (pos >= this.specials.size()) {
/* 297:313 */       return Collections.emptyList();
/* 298:    */     }
/* 299:315 */     ListIterator<Special> it = this.specials.listIterator(pos + takeFirst);
/* 300:316 */     Special last = (Special)it.next();
/* 301:317 */     int end = pos + takeFirst;
/* 302:318 */     while (it.hasNext())
/* 303:    */     {
/* 304:319 */       Special s = (Special)it.next();
/* 305:320 */       if ((!isDigitFmt(s)) || (s.pos - last.pos > 1)) {
/* 306:    */         break;
/* 307:    */       }
/* 308:322 */       end++;
/* 309:323 */       last = s;
/* 310:    */     }
/* 311:325 */     return this.specials.subList(pos, end + 1);
/* 312:    */   }
/* 313:    */   
/* 314:    */   private List<Special> specialsFor(int pos)
/* 315:    */   {
/* 316:329 */     return specialsFor(pos, 0);
/* 317:    */   }
/* 318:    */   
/* 319:    */   private static boolean isDigitFmt(Special s)
/* 320:    */   {
/* 321:333 */     return (s.ch == '0') || (s.ch == '?') || (s.ch == '#');
/* 322:    */   }
/* 323:    */   
/* 324:    */   private int calculateIntegerPartWidth()
/* 325:    */   {
/* 326:337 */     int digitCount = 0;
/* 327:338 */     for (Special s : this.specials)
/* 328:    */     {
/* 329:340 */       if (s == this.afterInteger) {
/* 330:    */         break;
/* 331:    */       }
/* 332:342 */       if (isDigitFmt(s)) {
/* 333:343 */         digitCount++;
/* 334:    */       }
/* 335:    */     }
/* 336:346 */     return digitCount;
/* 337:    */   }
/* 338:    */   
/* 339:    */   private static int interpretPrecision(Special decimalPoint, List<Special> specials)
/* 340:    */   {
/* 341:350 */     int idx = specials.indexOf(decimalPoint);
/* 342:351 */     int precision = 0;
/* 343:352 */     if (idx != -1)
/* 344:    */     {
/* 345:354 */       ListIterator<Special> it = specials.listIterator(idx + 1);
/* 346:355 */       while (it.hasNext())
/* 347:    */       {
/* 348:356 */         Special s = (Special)it.next();
/* 349:357 */         if (!isDigitFmt(s)) {
/* 350:    */           break;
/* 351:    */         }
/* 352:360 */         precision++;
/* 353:    */       }
/* 354:    */     }
/* 355:363 */     return precision;
/* 356:    */   }
/* 357:    */   
/* 358:    */   private static boolean interpretIntegerCommas(StringBuffer sb, List<Special> specials, Special decimalPoint, int integerEnd, int fractionalEnd, double[] scale)
/* 359:    */   {
/* 360:369 */     ListIterator<Special> it = specials.listIterator(integerEnd);
/* 361:    */     
/* 362:371 */     boolean stillScaling = true;
/* 363:372 */     boolean integerCommas = false;
/* 364:373 */     while (it.hasPrevious())
/* 365:    */     {
/* 366:374 */       Special s = (Special)it.previous();
/* 367:375 */       if (s.ch != ',') {
/* 368:376 */         stillScaling = false;
/* 369:378 */       } else if (stillScaling) {
/* 370:379 */         scale[0] /= 1000.0D;
/* 371:    */       } else {
/* 372:381 */         integerCommas = true;
/* 373:    */       }
/* 374:    */     }
/* 375:386 */     if (decimalPoint != null)
/* 376:    */     {
/* 377:387 */       it = specials.listIterator(fractionalEnd);
/* 378:388 */       while (it.hasPrevious())
/* 379:    */       {
/* 380:389 */         Special s = (Special)it.previous();
/* 381:390 */         if (s.ch != ',') {
/* 382:    */           break;
/* 383:    */         }
/* 384:393 */         scale[0] /= 1000.0D;
/* 385:    */       }
/* 386:    */     }
/* 387:399 */     it = specials.listIterator();
/* 388:400 */     int removed = 0;
/* 389:401 */     while (it.hasNext())
/* 390:    */     {
/* 391:402 */       Special s = (Special)it.next();
/* 392:403 */       s.pos -= removed;
/* 393:404 */       if (s.ch == ',')
/* 394:    */       {
/* 395:405 */         removed++;
/* 396:406 */         it.remove();
/* 397:407 */         sb.deleteCharAt(s.pos);
/* 398:    */       }
/* 399:    */     }
/* 400:411 */     return integerCommas;
/* 401:    */   }
/* 402:    */   
/* 403:    */   private int integerEnd()
/* 404:    */   {
/* 405:415 */     return this.afterInteger == null ? this.specials.size() : this.specials.indexOf(this.afterInteger);
/* 406:    */   }
/* 407:    */   
/* 408:    */   private int fractionalEnd()
/* 409:    */   {
/* 410:419 */     return this.afterFractional == null ? this.specials.size() : this.specials.indexOf(this.afterFractional);
/* 411:    */   }
/* 412:    */   
/* 413:    */   public void formatValue(StringBuffer toAppendTo, Object valueObject)
/* 414:    */   {
/* 415:424 */     double value = ((Number)valueObject).doubleValue();
/* 416:425 */     value *= this.scale;
/* 417:    */     
/* 418:    */ 
/* 419:    */ 
/* 420:    */ 
/* 421:    */ 
/* 422:    */ 
/* 423:    */ 
/* 424:    */ 
/* 425:434 */     boolean negative = value < 0.0D;
/* 426:435 */     if (negative) {
/* 427:436 */       value = -value;
/* 428:    */     }
/* 429:439 */     double fractional = 0.0D;
/* 430:440 */     if (this.slash != null) {
/* 431:441 */       if (this.improperFraction)
/* 432:    */       {
/* 433:442 */         fractional = value;
/* 434:443 */         value = 0.0D;
/* 435:    */       }
/* 436:    */       else
/* 437:    */       {
/* 438:445 */         fractional = value % 1.0D;
/* 439:    */         
/* 440:447 */         value = value;
/* 441:    */       }
/* 442:    */     }
/* 443:451 */     Set<CellNumberStringMod> mods = new TreeSet();
/* 444:452 */     StringBuffer output = new StringBuffer(localiseFormat(this.desc));
/* 445:454 */     if (this.exponent != null)
/* 446:    */     {
/* 447:455 */       writeScientific(value, output, mods);
/* 448:    */     }
/* 449:456 */     else if (this.improperFraction)
/* 450:    */     {
/* 451:457 */       writeFraction(value, null, fractional, output, mods);
/* 452:    */     }
/* 453:    */     else
/* 454:    */     {
/* 455:459 */       StringBuffer result = new StringBuffer();
/* 456:460 */       Formatter f = new Formatter(result, this.locale);
/* 457:    */       try
/* 458:    */       {
/* 459:462 */         f.format(this.locale, this.printfFmt, new Object[] { Double.valueOf(value) });
/* 460:    */       }
/* 461:    */       finally
/* 462:    */       {
/* 463:464 */         f.close();
/* 464:    */       }
/* 465:467 */       if (this.numerator == null)
/* 466:    */       {
/* 467:468 */         writeFractional(result, output);
/* 468:469 */         writeInteger(result, output, this.integerSpecials, mods, this.showGroupingSeparator);
/* 469:    */       }
/* 470:    */       else
/* 471:    */       {
/* 472:471 */         writeFraction(value, result, fractional, output, mods);
/* 473:    */       }
/* 474:    */     }
/* 475:475 */     DecimalFormatSymbols dfs = getDecimalFormatSymbols();
/* 476:476 */     String groupingSeparator = Character.toString(dfs.getGroupingSeparator());
/* 477:    */     
/* 478:    */ 
/* 479:479 */     Object changes = mods.iterator();
/* 480:480 */     CellNumberStringMod nextChange = ((Iterator)changes).hasNext() ? (CellNumberStringMod)((Iterator)changes).next() : null;
/* 481:    */     
/* 482:482 */     BitSet deletedChars = new BitSet();
/* 483:483 */     int adjust = 0;
/* 484:484 */     for (Special s : this.specials)
/* 485:    */     {
/* 486:485 */       int adjustedPos = s.pos + adjust;
/* 487:486 */       if ((!deletedChars.get(s.pos)) && (output.charAt(adjustedPos) == '#'))
/* 488:    */       {
/* 489:487 */         output.deleteCharAt(adjustedPos);
/* 490:488 */         adjust--;
/* 491:489 */         deletedChars.set(s.pos);
/* 492:    */       }
/* 493:491 */       while ((nextChange != null) && (s == nextChange.getSpecial()))
/* 494:    */       {
/* 495:492 */         int lenBefore = output.length();
/* 496:493 */         int modPos = s.pos + adjust;
/* 497:494 */         switch (nextChange.getOp())
/* 498:    */         {
/* 499:    */         case 2: 
/* 500:497 */           if ((!nextChange.getToAdd().equals(groupingSeparator)) || (!deletedChars.get(s.pos))) {
/* 501:500 */             output.insert(modPos + 1, nextChange.getToAdd());
/* 502:    */           }
/* 503:501 */           break;
/* 504:    */         case 1: 
/* 505:503 */           output.insert(modPos, nextChange.getToAdd());
/* 506:504 */           break;
/* 507:    */         case 3: 
/* 508:508 */           int delPos = s.pos;
/* 509:509 */           if (!nextChange.isStartInclusive())
/* 510:    */           {
/* 511:510 */             delPos++;
/* 512:511 */             modPos++;
/* 513:    */           }
/* 514:515 */           while (deletedChars.get(delPos))
/* 515:    */           {
/* 516:516 */             delPos++;
/* 517:517 */             modPos++;
/* 518:    */           }
/* 519:521 */           int delEndPos = nextChange.getEnd().pos;
/* 520:522 */           if (nextChange.isEndInclusive()) {
/* 521:523 */             delEndPos++;
/* 522:    */           }
/* 523:527 */           int modEndPos = delEndPos + adjust;
/* 524:529 */           if (modPos < modEndPos)
/* 525:    */           {
/* 526:530 */             if ("".equals(nextChange.getToAdd()))
/* 527:    */             {
/* 528:531 */               output.delete(modPos, modEndPos);
/* 529:    */             }
/* 530:    */             else
/* 531:    */             {
/* 532:534 */               char fillCh = nextChange.getToAdd().charAt(0);
/* 533:535 */               for (int i = modPos; i < modEndPos; i++) {
/* 534:536 */                 output.setCharAt(i, fillCh);
/* 535:    */               }
/* 536:    */             }
/* 537:539 */             deletedChars.set(delPos, delEndPos);
/* 538:    */           }
/* 539:    */           break;
/* 540:    */         default: 
/* 541:544 */           throw new IllegalStateException("Unknown op: " + nextChange.getOp());
/* 542:    */         }
/* 543:546 */         adjust += output.length() - lenBefore;
/* 544:    */         
/* 545:548 */         nextChange = ((Iterator)changes).hasNext() ? (CellNumberStringMod)((Iterator)changes).next() : null;
/* 546:    */       }
/* 547:    */     }
/* 548:553 */     if (negative) {
/* 549:554 */       toAppendTo.append('-');
/* 550:    */     }
/* 551:556 */     toAppendTo.append(output);
/* 552:    */   }
/* 553:    */   
/* 554:    */   private void writeScientific(double value, StringBuffer output, Set<CellNumberStringMod> mods)
/* 555:    */   {
/* 556:561 */     StringBuffer result = new StringBuffer();
/* 557:562 */     FieldPosition fractionPos = new FieldPosition(1);
/* 558:563 */     this.decimalFmt.format(value, result, fractionPos);
/* 559:564 */     writeInteger(result, output, this.integerSpecials, mods, this.showGroupingSeparator);
/* 560:565 */     writeFractional(result, output);
/* 561:    */     
/* 562:    */ 
/* 563:    */ 
/* 564:    */ 
/* 565:    */ 
/* 566:    */ 
/* 567:    */ 
/* 568:    */ 
/* 569:    */ 
/* 570:    */ 
/* 571:    */ 
/* 572:    */ 
/* 573:    */ 
/* 574:    */ 
/* 575:    */ 
/* 576:    */ 
/* 577:    */ 
/* 578:    */ 
/* 579:    */ 
/* 580:    */ 
/* 581:    */ 
/* 582:    */ 
/* 583:    */ 
/* 584:    */ 
/* 585:    */ 
/* 586:    */ 
/* 587:    */ 
/* 588:    */ 
/* 589:    */ 
/* 590:    */ 
/* 591:    */ 
/* 592:    */ 
/* 593:    */ 
/* 594:    */ 
/* 595:    */ 
/* 596:    */ 
/* 597:    */ 
/* 598:    */ 
/* 599:604 */     int ePos = fractionPos.getEndIndex();
/* 600:605 */     int signPos = ePos + 1;
/* 601:606 */     char expSignRes = result.charAt(signPos);
/* 602:607 */     if (expSignRes != '-')
/* 603:    */     {
/* 604:609 */       expSignRes = '+';
/* 605:    */       
/* 606:    */ 
/* 607:612 */       result.insert(signPos, '+');
/* 608:    */     }
/* 609:616 */     ListIterator<Special> it = this.exponentSpecials.listIterator(1);
/* 610:617 */     Special expSign = (Special)it.next();
/* 611:618 */     char expSignFmt = expSign.ch;
/* 612:622 */     if ((expSignRes == '-') || (expSignFmt == '+')) {
/* 613:623 */       mods.add(replaceMod(expSign, true, expSign, true, expSignRes));
/* 614:    */     } else {
/* 615:625 */       mods.add(deleteMod(expSign, true, expSign, true));
/* 616:    */     }
/* 617:628 */     StringBuffer exponentNum = new StringBuffer(result.substring(signPos + 1));
/* 618:629 */     writeInteger(exponentNum, output, this.exponentDigitSpecials, mods, false);
/* 619:    */   }
/* 620:    */   
/* 621:    */   private void writeFraction(double value, StringBuffer result, double fractional, StringBuffer output, Set<CellNumberStringMod> mods)
/* 622:    */   {
/* 623:638 */     if (!this.improperFraction)
/* 624:    */     {
/* 625:641 */       if (fractional == 0.0D) {
/* 626:641 */         if (!hasChar('0', new List[] { this.numeratorSpecials }))
/* 627:    */         {
/* 628:642 */           writeInteger(result, output, this.integerSpecials, mods, false);
/* 629:    */           
/* 630:644 */           Special start = lastSpecial(this.integerSpecials);
/* 631:645 */           Special end = lastSpecial(this.denominatorSpecials);
/* 632:646 */           if (hasChar('?', new List[] { this.integerSpecials, this.numeratorSpecials, this.denominatorSpecials })) {
/* 633:648 */             mods.add(replaceMod(start, false, end, true, ' '));
/* 634:    */           } else {
/* 635:651 */             mods.add(deleteMod(start, false, end, true));
/* 636:    */           }
/* 637:655 */           return;
/* 638:    */         }
/* 639:    */       }
/* 640:658 */       boolean numNoZero = !hasChar('0', new List[] { this.numeratorSpecials });
/* 641:659 */       boolean intNoZero = !hasChar('0', new List[] { this.integerSpecials });
/* 642:660 */       if (!this.integerSpecials.isEmpty()) {
/* 643:660 */         if (this.integerSpecials.size() != 1) {
/* 644:    */           break label238;
/* 645:    */         }
/* 646:    */       }
/* 647:    */       label238:
/* 648:660 */       boolean intOnlyHash = hasChar('#', new List[] { this.integerSpecials });
/* 649:662 */       boolean removeBecauseZero = (fractional == 0.0D) && ((intOnlyHash) || (numNoZero));
/* 650:663 */       boolean removeBecauseFraction = (fractional != 0.0D) && (intNoZero);
/* 651:665 */       if ((value == 0.0D) && ((removeBecauseZero) || (removeBecauseFraction)))
/* 652:    */       {
/* 653:666 */         Special start = lastSpecial(this.integerSpecials);
/* 654:667 */         boolean hasPlaceHolder = hasChar('?', new List[] { this.integerSpecials, this.numeratorSpecials });
/* 655:668 */         CellNumberStringMod sm = hasPlaceHolder ? replaceMod(start, true, this.numerator, false, ' ') : deleteMod(start, true, this.numerator, false);
/* 656:    */         
/* 657:    */ 
/* 658:671 */         mods.add(sm);
/* 659:    */       }
/* 660:    */       else
/* 661:    */       {
/* 662:674 */         writeInteger(result, output, this.integerSpecials, mods, false);
/* 663:    */       }
/* 664:    */     }
/* 665:    */     try
/* 666:    */     {
/* 667:    */       int d;
/* 668:    */       int n;
/* 669:    */       int d;
/* 670:684 */       if ((fractional == 0.0D) || ((this.improperFraction) && (fractional % 1.0D == 0.0D)))
/* 671:    */       {
/* 672:686 */         int n = (int)Math.round(fractional);
/* 673:687 */         d = 1;
/* 674:    */       }
/* 675:    */       else
/* 676:    */       {
/* 677:689 */         SimpleFraction frac = SimpleFraction.buildFractionMaxDenominator(fractional, this.maxDenominator);
/* 678:690 */         n = frac.getNumerator();
/* 679:691 */         d = frac.getDenominator();
/* 680:    */       }
/* 681:693 */       if (this.improperFraction) {
/* 682:694 */         n = (int)(n + Math.round(value * d));
/* 683:    */       }
/* 684:696 */       writeSingleInteger(this.numeratorFmt, n, output, this.numeratorSpecials, mods);
/* 685:697 */       writeSingleInteger(this.denominatorFmt, d, output, this.denominatorSpecials, mods);
/* 686:    */     }
/* 687:    */     catch (RuntimeException ignored)
/* 688:    */     {
/* 689:699 */       LOG.log(7, new Object[] { "error while fraction evaluation", ignored });
/* 690:    */     }
/* 691:    */   }
/* 692:    */   
/* 693:    */   private String localiseFormat(String format)
/* 694:    */   {
/* 695:704 */     DecimalFormatSymbols dfs = getDecimalFormatSymbols();
/* 696:705 */     if ((format.contains(",")) && (dfs.getGroupingSeparator() != ','))
/* 697:    */     {
/* 698:706 */       if ((format.contains(".")) && (dfs.getDecimalSeparator() != '.'))
/* 699:    */       {
/* 700:707 */         format = replaceLast(format, "\\.", "[DECIMAL_SEPARATOR]");
/* 701:708 */         format = format.replace(',', dfs.getGroupingSeparator()).replace("[DECIMAL_SEPARATOR]", Character.toString(dfs.getDecimalSeparator()));
/* 702:    */       }
/* 703:    */       else
/* 704:    */       {
/* 705:711 */         format = format.replace(',', dfs.getGroupingSeparator());
/* 706:    */       }
/* 707:    */     }
/* 708:713 */     else if ((format.contains(".")) && (dfs.getDecimalSeparator() != '.')) {
/* 709:714 */       format = format.replace('.', dfs.getDecimalSeparator());
/* 710:    */     }
/* 711:716 */     return format;
/* 712:    */   }
/* 713:    */   
/* 714:    */   private static String replaceLast(String text, String regex, String replacement)
/* 715:    */   {
/* 716:721 */     return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
/* 717:    */   }
/* 718:    */   
/* 719:    */   private static boolean hasChar(char ch, List<Special>... numSpecials)
/* 720:    */   {
/* 721:725 */     for (List<Special> specials : numSpecials) {
/* 722:726 */       for (Special s : specials) {
/* 723:727 */         if (s.ch == ch) {
/* 724:728 */           return true;
/* 725:    */         }
/* 726:    */       }
/* 727:    */     }
/* 728:732 */     return false;
/* 729:    */   }
/* 730:    */   
/* 731:    */   private void writeSingleInteger(String fmt, int num, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods)
/* 732:    */   {
/* 733:737 */     StringBuffer sb = new StringBuffer();
/* 734:738 */     Formatter formatter = new Formatter(sb, this.locale);
/* 735:    */     try
/* 736:    */     {
/* 737:740 */       formatter.format(this.locale, fmt, new Object[] { Integer.valueOf(num) });
/* 738:    */     }
/* 739:    */     finally
/* 740:    */     {
/* 741:742 */       formatter.close();
/* 742:    */     }
/* 743:744 */     writeInteger(sb, output, numSpecials, mods, false);
/* 744:    */   }
/* 745:    */   
/* 746:    */   private void writeInteger(StringBuffer result, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods, boolean showGroupingSeparator)
/* 747:    */   {
/* 748:751 */     DecimalFormatSymbols dfs = getDecimalFormatSymbols();
/* 749:752 */     String decimalSeparator = Character.toString(dfs.getDecimalSeparator());
/* 750:753 */     String groupingSeparator = Character.toString(dfs.getGroupingSeparator());
/* 751:    */     
/* 752:755 */     int pos = result.indexOf(decimalSeparator) - 1;
/* 753:756 */     if (pos < 0) {
/* 754:757 */       if ((this.exponent != null) && (numSpecials == this.integerSpecials)) {
/* 755:758 */         pos = result.indexOf("E") - 1;
/* 756:    */       } else {
/* 757:760 */         pos = result.length() - 1;
/* 758:    */       }
/* 759:    */     }
/* 760:765 */     for (int strip = 0; strip < pos; strip++)
/* 761:    */     {
/* 762:766 */       char resultCh = result.charAt(strip);
/* 763:767 */       if ((resultCh != '0') && (resultCh != dfs.getGroupingSeparator())) {
/* 764:    */         break;
/* 765:    */       }
/* 766:    */     }
/* 767:772 */     ListIterator<Special> it = numSpecials.listIterator(numSpecials.size());
/* 768:773 */     boolean followWithGroupingSeparator = false;
/* 769:774 */     Special lastOutputIntegerDigit = null;
/* 770:775 */     int digit = 0;
/* 771:776 */     while (it.hasPrevious())
/* 772:    */     {
/* 773:    */       char resultCh;
/* 774:    */       char resultCh;
/* 775:778 */       if (pos >= 0) {
/* 776:779 */         resultCh = result.charAt(pos);
/* 777:    */       } else {
/* 778:782 */         resultCh = '0';
/* 779:    */       }
/* 780:784 */       Special s = (Special)it.previous();
/* 781:785 */       followWithGroupingSeparator = (showGroupingSeparator) && (digit > 0) && (digit % 3 == 0);
/* 782:786 */       boolean zeroStrip = false;
/* 783:787 */       if ((resultCh != '0') || (s.ch == '0') || (s.ch == '?') || (pos >= strip))
/* 784:    */       {
/* 785:788 */         zeroStrip = (s.ch == '?') && (pos < strip);
/* 786:789 */         output.setCharAt(s.pos, zeroStrip ? ' ' : resultCh);
/* 787:790 */         lastOutputIntegerDigit = s;
/* 788:    */       }
/* 789:792 */       if (followWithGroupingSeparator)
/* 790:    */       {
/* 791:793 */         mods.add(insertMod(s, zeroStrip ? " " : groupingSeparator, 2));
/* 792:794 */         followWithGroupingSeparator = false;
/* 793:    */       }
/* 794:796 */       digit++;
/* 795:797 */       pos--;
/* 796:    */     }
/* 797:799 */     StringBuffer extraLeadingDigits = new StringBuffer();
/* 798:800 */     if (pos >= 0)
/* 799:    */     {
/* 800:803 */       pos++;
/* 801:804 */       extraLeadingDigits = new StringBuffer(result.substring(0, pos));
/* 802:805 */       if (showGroupingSeparator) {
/* 803:806 */         while (pos > 0)
/* 804:    */         {
/* 805:807 */           if ((digit > 0) && (digit % 3 == 0)) {
/* 806:808 */             extraLeadingDigits.insert(pos, groupingSeparator);
/* 807:    */           }
/* 808:810 */           digit++;
/* 809:811 */           pos--;
/* 810:    */         }
/* 811:    */       }
/* 812:814 */       mods.add(insertMod(lastOutputIntegerDigit, extraLeadingDigits, 1));
/* 813:    */     }
/* 814:    */   }
/* 815:    */   
/* 816:    */   private void writeFractional(StringBuffer result, StringBuffer output)
/* 817:    */   {
/* 818:    */     int digit;
/* 819:    */     int strip;
/* 820:821 */     if (this.fractionalSpecials.size() > 0)
/* 821:    */     {
/* 822:822 */       String decimalSeparator = Character.toString(getDecimalFormatSymbols().getDecimalSeparator());
/* 823:823 */       digit = result.indexOf(decimalSeparator) + 1;
/* 824:    */       int strip;
/* 825:824 */       if (this.exponent != null) {
/* 826:825 */         strip = result.indexOf("e") - 1;
/* 827:    */       } else {
/* 828:827 */         strip = result.length() - 1;
/* 829:    */       }
/* 830:830 */       while ((strip > digit) && (result.charAt(strip) == '0')) {
/* 831:831 */         strip--;
/* 832:    */       }
/* 833:834 */       for (Special s : this.fractionalSpecials)
/* 834:    */       {
/* 835:835 */         char resultCh = result.charAt(digit);
/* 836:836 */         if ((resultCh != '0') || (s.ch == '0') || (digit < strip)) {
/* 837:837 */           output.setCharAt(s.pos, resultCh);
/* 838:838 */         } else if (s.ch == '?') {
/* 839:841 */           output.setCharAt(s.pos, ' ');
/* 840:    */         }
/* 841:843 */         digit++;
/* 842:    */       }
/* 843:    */     }
/* 844:    */   }
/* 845:    */   
/* 846:    */   public void simpleValue(StringBuffer toAppendTo, Object value)
/* 847:    */   {
/* 848:855 */     this.SIMPLE_NUMBER.formatValue(toAppendTo, value);
/* 849:    */   }
/* 850:    */   
/* 851:    */   private static Special lastSpecial(List<Special> s)
/* 852:    */   {
/* 853:859 */     return (Special)s.get(s.size() - 1);
/* 854:    */   }
/* 855:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellNumberFormatter
 * JD-Core Version:    0.7.0.1
 */