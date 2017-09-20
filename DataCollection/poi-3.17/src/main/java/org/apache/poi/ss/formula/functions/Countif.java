/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.apache.poi.ss.formula.ThreeDEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  10:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  12:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  15:    */ import org.apache.poi.ss.usermodel.FormulaError;
/*  16:    */ 
/*  17:    */ public final class Countif
/*  18:    */   extends Fixed2ArgFunction
/*  19:    */ {
/*  20:    */   private static final class CmpOp
/*  21:    */   {
/*  22:    */     public static final int NONE = 0;
/*  23:    */     public static final int EQ = 1;
/*  24:    */     public static final int NE = 2;
/*  25:    */     public static final int LE = 3;
/*  26:    */     public static final int LT = 4;
/*  27:    */     public static final int GT = 5;
/*  28:    */     public static final int GE = 6;
/*  29: 56 */     public static final CmpOp OP_NONE = op("", 0);
/*  30: 57 */     public static final CmpOp OP_EQ = op("=", 1);
/*  31: 58 */     public static final CmpOp OP_NE = op("<>", 2);
/*  32: 59 */     public static final CmpOp OP_LE = op("<=", 3);
/*  33: 60 */     public static final CmpOp OP_LT = op("<", 4);
/*  34: 61 */     public static final CmpOp OP_GT = op(">", 5);
/*  35: 62 */     public static final CmpOp OP_GE = op(">=", 6);
/*  36:    */     private final String _representation;
/*  37:    */     private final int _code;
/*  38:    */     
/*  39:    */     private static CmpOp op(String rep, int code)
/*  40:    */     {
/*  41: 67 */       return new CmpOp(rep, code);
/*  42:    */     }
/*  43:    */     
/*  44:    */     private CmpOp(String representation, int code)
/*  45:    */     {
/*  46: 70 */       this._representation = representation;
/*  47: 71 */       this._code = code;
/*  48:    */     }
/*  49:    */     
/*  50:    */     public int getLength()
/*  51:    */     {
/*  52: 77 */       return this._representation.length();
/*  53:    */     }
/*  54:    */     
/*  55:    */     public int getCode()
/*  56:    */     {
/*  57: 80 */       return this._code;
/*  58:    */     }
/*  59:    */     
/*  60:    */     public static CmpOp getOperator(String value)
/*  61:    */     {
/*  62: 83 */       int len = value.length();
/*  63: 84 */       if (len < 1) {
/*  64: 85 */         return OP_NONE;
/*  65:    */       }
/*  66: 88 */       char firstChar = value.charAt(0);
/*  67: 90 */       switch (firstChar)
/*  68:    */       {
/*  69:    */       case '=': 
/*  70: 92 */         return OP_EQ;
/*  71:    */       case '>': 
/*  72: 94 */         if (len > 1) {
/*  73: 95 */           switch (value.charAt(1))
/*  74:    */           {
/*  75:    */           case '=': 
/*  76: 97 */             return OP_GE;
/*  77:    */           }
/*  78:    */         }
/*  79:100 */         return OP_GT;
/*  80:    */       case '<': 
/*  81:102 */         if (len > 1) {
/*  82:103 */           switch (value.charAt(1))
/*  83:    */           {
/*  84:    */           case '=': 
/*  85:105 */             return OP_LE;
/*  86:    */           case '>': 
/*  87:107 */             return OP_NE;
/*  88:    */           }
/*  89:    */         }
/*  90:110 */         return OP_LT;
/*  91:    */       }
/*  92:112 */       return OP_NONE;
/*  93:    */     }
/*  94:    */     
/*  95:    */     public boolean evaluate(boolean cmpResult)
/*  96:    */     {
/*  97:115 */       switch (this._code)
/*  98:    */       {
/*  99:    */       case 0: 
/* 100:    */       case 1: 
/* 101:118 */         return cmpResult;
/* 102:    */       case 2: 
/* 103:120 */         return !cmpResult;
/* 104:    */       }
/* 105:122 */       throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '" + this._representation + "'");
/* 106:    */     }
/* 107:    */     
/* 108:    */     public boolean evaluate(int cmpResult)
/* 109:    */     {
/* 110:126 */       switch (this._code)
/* 111:    */       {
/* 112:    */       case 0: 
/* 113:    */       case 1: 
/* 114:129 */         return cmpResult == 0;
/* 115:    */       case 2: 
/* 116:130 */         return cmpResult != 0;
/* 117:    */       case 4: 
/* 118:131 */         return cmpResult < 0;
/* 119:    */       case 3: 
/* 120:132 */         return cmpResult <= 0;
/* 121:    */       case 5: 
/* 122:133 */         return cmpResult > 0;
/* 123:    */       case 6: 
/* 124:134 */         return cmpResult >= 0;
/* 125:    */       }
/* 126:136 */       throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '" + this._representation + "'");
/* 127:    */     }
/* 128:    */     
/* 129:    */     public String toString()
/* 130:    */     {
/* 131:141 */       StringBuffer sb = new StringBuffer(64);
/* 132:142 */       sb.append(getClass().getName());
/* 133:143 */       sb.append(" [").append(this._representation).append("]");
/* 134:144 */       return sb.toString();
/* 135:    */     }
/* 136:    */     
/* 137:    */     public String getRepresentation()
/* 138:    */     {
/* 139:147 */       return this._representation;
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   private static abstract class MatcherBase
/* 144:    */     implements CountUtils.I_MatchPredicate
/* 145:    */   {
/* 146:    */     private final CmpOp _operator;
/* 147:    */     
/* 148:    */     MatcherBase(CmpOp operator)
/* 149:    */     {
/* 150:155 */       this._operator = operator;
/* 151:    */     }
/* 152:    */     
/* 153:    */     protected final int getCode()
/* 154:    */     {
/* 155:158 */       return this._operator.getCode();
/* 156:    */     }
/* 157:    */     
/* 158:    */     protected final boolean evaluate(int cmpResult)
/* 159:    */     {
/* 160:161 */       return this._operator.evaluate(cmpResult);
/* 161:    */     }
/* 162:    */     
/* 163:    */     protected final boolean evaluate(boolean cmpResult)
/* 164:    */     {
/* 165:164 */       return this._operator.evaluate(cmpResult);
/* 166:    */     }
/* 167:    */     
/* 168:    */     public final String toString()
/* 169:    */     {
/* 170:168 */       StringBuffer sb = new StringBuffer(64);
/* 171:169 */       sb.append(getClass().getName()).append(" [");
/* 172:170 */       sb.append(this._operator.getRepresentation());
/* 173:171 */       sb.append(getValueText());
/* 174:172 */       sb.append("]");
/* 175:173 */       return sb.toString();
/* 176:    */     }
/* 177:    */     
/* 178:    */     protected abstract String getValueText();
/* 179:    */   }
/* 180:    */   
/* 181:    */   private static final class NumberMatcher
/* 182:    */     extends MatcherBase
/* 183:    */   {
/* 184:    */     private final double _value;
/* 185:    */     
/* 186:    */     public NumberMatcher(double value, CmpOp operator)
/* 187:    */     {
/* 188:183 */       super();
/* 189:184 */       this._value = value;
/* 190:    */     }
/* 191:    */     
/* 192:    */     protected String getValueText()
/* 193:    */     {
/* 194:188 */       return String.valueOf(this._value);
/* 195:    */     }
/* 196:    */     
/* 197:    */     public boolean matches(ValueEval x)
/* 198:    */     {
/* 199:194 */       if ((x instanceof StringEval))
/* 200:    */       {
/* 201:197 */         switch (getCode())
/* 202:    */         {
/* 203:    */         case 0: 
/* 204:    */         case 1: 
/* 205:    */           break;
/* 206:    */         case 2: 
/* 207:204 */           return true;
/* 208:    */         default: 
/* 209:208 */           return false;
/* 210:    */         }
/* 211:210 */         StringEval se = (StringEval)x;
/* 212:211 */         Double val = OperandResolver.parseDouble(se.getStringValue());
/* 213:212 */         if (val == null) {
/* 214:214 */           return false;
/* 215:    */         }
/* 216:216 */         return this._value == val.doubleValue();
/* 217:    */       }
/* 218:    */       double testValue;
/* 219:217 */       if ((x instanceof NumberEval))
/* 220:    */       {
/* 221:218 */         NumberEval ne = (NumberEval)x;
/* 222:219 */         testValue = ne.getNumberValue();
/* 223:    */       }
/* 224:    */       else
/* 225:    */       {
/* 226:220 */         if ((x instanceof BlankEval))
/* 227:    */         {
/* 228:221 */           switch (getCode())
/* 229:    */           {
/* 230:    */           case 2: 
/* 231:224 */             return true;
/* 232:    */           }
/* 233:226 */           return false;
/* 234:    */         }
/* 235:229 */         return false;
/* 236:    */       }
/* 237:    */       double testValue;
/* 238:231 */       return evaluate(Double.compare(testValue, this._value));
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   private static final class BooleanMatcher
/* 243:    */     extends MatcherBase
/* 244:    */   {
/* 245:    */     private final int _value;
/* 246:    */     
/* 247:    */     public BooleanMatcher(boolean value, CmpOp operator)
/* 248:    */     {
/* 249:239 */       super();
/* 250:240 */       this._value = boolToInt(value);
/* 251:    */     }
/* 252:    */     
/* 253:    */     protected String getValueText()
/* 254:    */     {
/* 255:244 */       return this._value == 1 ? "TRUE" : "FALSE";
/* 256:    */     }
/* 257:    */     
/* 258:    */     private static int boolToInt(boolean value)
/* 259:    */     {
/* 260:248 */       return value ? 1 : 0;
/* 261:    */     }
/* 262:    */     
/* 263:    */     public boolean matches(ValueEval x)
/* 264:    */     {
/* 265:254 */       if ((x instanceof StringEval)) {
/* 266:257 */         return false;
/* 267:    */       }
/* 268:    */       int testValue;
/* 269:266 */       if ((x instanceof BoolEval))
/* 270:    */       {
/* 271:267 */         BoolEval be = (BoolEval)x;
/* 272:268 */         testValue = boolToInt(be.getBooleanValue());
/* 273:    */       }
/* 274:    */       else
/* 275:    */       {
/* 276:269 */         if ((x instanceof BlankEval))
/* 277:    */         {
/* 278:270 */           switch (getCode())
/* 279:    */           {
/* 280:    */           case 2: 
/* 281:273 */             return true;
/* 282:    */           }
/* 283:275 */           return false;
/* 284:    */         }
/* 285:277 */         if ((x instanceof NumberEval))
/* 286:    */         {
/* 287:278 */           switch (getCode())
/* 288:    */           {
/* 289:    */           case 2: 
/* 290:281 */             return true;
/* 291:    */           }
/* 292:283 */           return false;
/* 293:    */         }
/* 294:286 */         return false;
/* 295:    */       }
/* 296:    */       int testValue;
/* 297:288 */       return evaluate(testValue - this._value);
/* 298:    */     }
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static final class ErrorMatcher
/* 302:    */     extends MatcherBase
/* 303:    */   {
/* 304:    */     private final int _value;
/* 305:    */     
/* 306:    */     public ErrorMatcher(int errorCode, CmpOp operator)
/* 307:    */     {
/* 308:296 */       super();
/* 309:297 */       this._value = errorCode;
/* 310:    */     }
/* 311:    */     
/* 312:    */     protected String getValueText()
/* 313:    */     {
/* 314:301 */       return FormulaError.forInt(this._value).getString();
/* 315:    */     }
/* 316:    */     
/* 317:    */     public boolean matches(ValueEval x)
/* 318:    */     {
/* 319:306 */       if ((x instanceof ErrorEval))
/* 320:    */       {
/* 321:307 */         int testValue = ((ErrorEval)x).getErrorCode();
/* 322:308 */         return evaluate(testValue - this._value);
/* 323:    */       }
/* 324:310 */       return false;
/* 325:    */     }
/* 326:    */     
/* 327:    */     public int getValue()
/* 328:    */     {
/* 329:314 */       return this._value;
/* 330:    */     }
/* 331:    */   }
/* 332:    */   
/* 333:    */   public static final class StringMatcher
/* 334:    */     extends MatcherBase
/* 335:    */   {
/* 336:    */     private final String _value;
/* 337:    */     private final Pattern _pattern;
/* 338:    */     
/* 339:    */     public StringMatcher(String value, CmpOp operator)
/* 340:    */     {
/* 341:323 */       super();
/* 342:324 */       this._value = value;
/* 343:325 */       switch (operator.getCode())
/* 344:    */       {
/* 345:    */       case 0: 
/* 346:    */       case 1: 
/* 347:    */       case 2: 
/* 348:329 */         this._pattern = getWildCardPattern(value);
/* 349:330 */         break;
/* 350:    */       default: 
/* 351:333 */         this._pattern = null;
/* 352:    */       }
/* 353:    */     }
/* 354:    */     
/* 355:    */     protected String getValueText()
/* 356:    */     {
/* 357:338 */       if (this._pattern == null) {
/* 358:339 */         return this._value;
/* 359:    */       }
/* 360:341 */       return this._pattern.pattern();
/* 361:    */     }
/* 362:    */     
/* 363:    */     public boolean matches(ValueEval x)
/* 364:    */     {
/* 365:346 */       if ((x instanceof BlankEval))
/* 366:    */       {
/* 367:347 */         switch (getCode())
/* 368:    */         {
/* 369:    */         case 0: 
/* 370:    */         case 1: 
/* 371:350 */           return this._value.length() == 0;
/* 372:    */         case 2: 
/* 373:354 */           return this._value.length() != 0;
/* 374:    */         }
/* 375:357 */         return false;
/* 376:    */       }
/* 377:359 */       if (!(x instanceof StringEval)) {
/* 378:363 */         return false;
/* 379:    */       }
/* 380:365 */       String testedValue = ((StringEval)x).getStringValue();
/* 381:366 */       if ((testedValue.length() < 1) && (this._value.length() < 1))
/* 382:    */       {
/* 383:369 */         switch (getCode())
/* 384:    */         {
/* 385:    */         case 0: 
/* 386:370 */           return true;
/* 387:    */         case 1: 
/* 388:371 */           return false;
/* 389:    */         case 2: 
/* 390:372 */           return true;
/* 391:    */         }
/* 392:374 */         return false;
/* 393:    */       }
/* 394:376 */       if (this._pattern != null) {
/* 395:377 */         return evaluate(this._pattern.matcher(testedValue).matches());
/* 396:    */       }
/* 397:381 */       return evaluate(testedValue.compareToIgnoreCase(this._value));
/* 398:    */     }
/* 399:    */     
/* 400:    */     public static Pattern getWildCardPattern(String value)
/* 401:    */     {
/* 402:388 */       int len = value.length();
/* 403:389 */       StringBuffer sb = new StringBuffer(len);
/* 404:390 */       boolean hasWildCard = false;
/* 405:391 */       for (int i = 0; i < len; i++)
/* 406:    */       {
/* 407:392 */         char ch = value.charAt(i);
/* 408:393 */         switch (ch)
/* 409:    */         {
/* 410:    */         case '?': 
/* 411:395 */           hasWildCard = true;
/* 412:    */           
/* 413:397 */           sb.append('.');
/* 414:398 */           break;
/* 415:    */         case '*': 
/* 416:400 */           hasWildCard = true;
/* 417:    */           
/* 418:402 */           sb.append(".*");
/* 419:403 */           break;
/* 420:    */         case '~': 
/* 421:405 */           if (i + 1 < len)
/* 422:    */           {
/* 423:406 */             ch = value.charAt(i + 1);
/* 424:407 */             switch (ch)
/* 425:    */             {
/* 426:    */             case '*': 
/* 427:    */             case '?': 
/* 428:410 */               hasWildCard = true;
/* 429:411 */               sb.append('[').append(ch).append(']');
/* 430:412 */               i++;
/* 431:413 */               break;
/* 432:    */             }
/* 433:    */           }
/* 434:    */           else
/* 435:    */           {
/* 436:417 */             sb.append('~');
/* 437:    */           }
/* 438:418 */           break;
/* 439:    */         case '$': 
/* 440:    */         case '(': 
/* 441:    */         case ')': 
/* 442:    */         case '.': 
/* 443:    */         case '[': 
/* 444:    */         case ']': 
/* 445:    */         case '^': 
/* 446:427 */           sb.append("\\").append(ch);
/* 447:428 */           break;
/* 448:    */         default: 
/* 449:430 */           sb.append(ch);
/* 450:    */         }
/* 451:    */       }
/* 452:432 */       if (hasWildCard) {
/* 453:433 */         return Pattern.compile(sb.toString(), 2);
/* 454:    */       }
/* 455:435 */       return null;
/* 456:    */     }
/* 457:    */   }
/* 458:    */   
/* 459:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 460:    */   {
/* 461:442 */     CountUtils.I_MatchPredicate mp = createCriteriaPredicate(arg1, srcRowIndex, srcColumnIndex);
/* 462:443 */     if (mp == null) {
/* 463:445 */       return NumberEval.ZERO;
/* 464:    */     }
/* 465:447 */     double result = countMatchingCellsInArea(arg0, mp);
/* 466:448 */     return new NumberEval(result);
/* 467:    */   }
/* 468:    */   
/* 469:    */   private double countMatchingCellsInArea(ValueEval rangeArg, CountUtils.I_MatchPredicate criteriaPredicate)
/* 470:    */   {
/* 471:455 */     if ((rangeArg instanceof RefEval)) {
/* 472:456 */       return CountUtils.countMatchingCellsInRef((RefEval)rangeArg, criteriaPredicate);
/* 473:    */     }
/* 474:457 */     if ((rangeArg instanceof ThreeDEval)) {
/* 475:458 */       return CountUtils.countMatchingCellsInArea((ThreeDEval)rangeArg, criteriaPredicate);
/* 476:    */     }
/* 477:460 */     throw new IllegalArgumentException("Bad range arg type (" + rangeArg.getClass().getName() + ")");
/* 478:    */   }
/* 479:    */   
/* 480:    */   static CountUtils.I_MatchPredicate createCriteriaPredicate(ValueEval arg, int srcRowIndex, int srcColumnIndex)
/* 481:    */   {
/* 482:470 */     ValueEval evaluatedCriteriaArg = evaluateCriteriaArg(arg, srcRowIndex, srcColumnIndex);
/* 483:472 */     if ((evaluatedCriteriaArg instanceof NumberEval)) {
/* 484:473 */       return new NumberMatcher(((NumberEval)evaluatedCriteriaArg).getNumberValue(), CmpOp.OP_NONE);
/* 485:    */     }
/* 486:475 */     if ((evaluatedCriteriaArg instanceof BoolEval)) {
/* 487:476 */       return new BooleanMatcher(((BoolEval)evaluatedCriteriaArg).getBooleanValue(), CmpOp.OP_NONE);
/* 488:    */     }
/* 489:479 */     if ((evaluatedCriteriaArg instanceof StringEval)) {
/* 490:480 */       return createGeneralMatchPredicate((StringEval)evaluatedCriteriaArg);
/* 491:    */     }
/* 492:482 */     if ((evaluatedCriteriaArg instanceof ErrorEval)) {
/* 493:483 */       return new ErrorMatcher(((ErrorEval)evaluatedCriteriaArg).getErrorCode(), CmpOp.OP_NONE);
/* 494:    */     }
/* 495:485 */     if (evaluatedCriteriaArg == BlankEval.instance) {
/* 496:486 */       return null;
/* 497:    */     }
/* 498:488 */     throw new RuntimeException("Unexpected type for criteria (" + evaluatedCriteriaArg.getClass().getName() + ")");
/* 499:    */   }
/* 500:    */   
/* 501:    */   private static ValueEval evaluateCriteriaArg(ValueEval arg, int srcRowIndex, int srcColumnIndex)
/* 502:    */   {
/* 503:    */     try
/* 504:    */     {
/* 505:498 */       return OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex);
/* 506:    */     }
/* 507:    */     catch (EvaluationException e)
/* 508:    */     {
/* 509:500 */       return e.getErrorEval();
/* 510:    */     }
/* 511:    */   }
/* 512:    */   
/* 513:    */   private static CountUtils.I_MatchPredicate createGeneralMatchPredicate(StringEval stringEval)
/* 514:    */   {
/* 515:507 */     String value = stringEval.getStringValue();
/* 516:508 */     CmpOp operator = CmpOp.getOperator(value);
/* 517:509 */     value = value.substring(operator.getLength());
/* 518:    */     
/* 519:511 */     Boolean booleanVal = parseBoolean(value);
/* 520:512 */     if (booleanVal != null) {
/* 521:513 */       return new BooleanMatcher(booleanVal.booleanValue(), operator);
/* 522:    */     }
/* 523:516 */     Double doubleVal = OperandResolver.parseDouble(value);
/* 524:517 */     if (doubleVal != null) {
/* 525:518 */       return new NumberMatcher(doubleVal.doubleValue(), operator);
/* 526:    */     }
/* 527:520 */     ErrorEval ee = parseError(value);
/* 528:521 */     if (ee != null) {
/* 529:522 */       return new ErrorMatcher(ee.getErrorCode(), operator);
/* 530:    */     }
/* 531:526 */     return new StringMatcher(value, operator);
/* 532:    */   }
/* 533:    */   
/* 534:    */   private static ErrorEval parseError(String value)
/* 535:    */   {
/* 536:529 */     if ((value.length() < 4) || (value.charAt(0) != '#')) {
/* 537:530 */       return null;
/* 538:    */     }
/* 539:532 */     if (value.equals("#NULL!")) {
/* 540:533 */       return ErrorEval.NULL_INTERSECTION;
/* 541:    */     }
/* 542:535 */     if (value.equals("#DIV/0!")) {
/* 543:536 */       return ErrorEval.DIV_ZERO;
/* 544:    */     }
/* 545:538 */     if (value.equals("#VALUE!")) {
/* 546:539 */       return ErrorEval.VALUE_INVALID;
/* 547:    */     }
/* 548:541 */     if (value.equals("#REF!")) {
/* 549:542 */       return ErrorEval.REF_INVALID;
/* 550:    */     }
/* 551:544 */     if (value.equals("#NAME?")) {
/* 552:545 */       return ErrorEval.NAME_INVALID;
/* 553:    */     }
/* 554:547 */     if (value.equals("#NUM!")) {
/* 555:548 */       return ErrorEval.NUM_ERROR;
/* 556:    */     }
/* 557:550 */     if (value.equals("#N/A")) {
/* 558:551 */       return ErrorEval.NA;
/* 559:    */     }
/* 560:554 */     return null;
/* 561:    */   }
/* 562:    */   
/* 563:    */   static Boolean parseBoolean(String strRep)
/* 564:    */   {
/* 565:561 */     if (strRep.length() < 1) {
/* 566:562 */       return null;
/* 567:    */     }
/* 568:564 */     switch (strRep.charAt(0))
/* 569:    */     {
/* 570:    */     case 'T': 
/* 571:    */     case 't': 
/* 572:567 */       if ("TRUE".equalsIgnoreCase(strRep)) {
/* 573:568 */         return Boolean.TRUE;
/* 574:    */       }
/* 575:    */       break;
/* 576:    */     case 'F': 
/* 577:    */     case 'f': 
/* 578:573 */       if ("FALSE".equalsIgnoreCase(strRep)) {
/* 579:574 */         return Boolean.FALSE;
/* 580:    */       }
/* 581:    */       break;
/* 582:    */     }
/* 583:578 */     return null;
/* 584:    */   }
/* 585:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.Countif

 * JD-Core Version:    0.7.0.1

 */