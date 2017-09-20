/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public abstract class NumericFunction
/*  11:    */   implements Function
/*  12:    */ {
/*  13:    */   static final double ZERO = 0.0D;
/*  14:    */   static final double TEN = 10.0D;
/*  15: 31 */   static final double LOG_10_TO_BASE_e = Math.log(10.0D);
/*  16:    */   
/*  17:    */   protected static final double singleOperandEvaluate(ValueEval arg, int srcRowIndex, int srcColumnIndex)
/*  18:    */     throws EvaluationException
/*  19:    */   {
/*  20: 34 */     if (arg == null) {
/*  21: 35 */       throw new IllegalArgumentException("arg must not be null");
/*  22:    */     }
/*  23: 37 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex);
/*  24: 38 */     double result = OperandResolver.coerceValueToDouble(ve);
/*  25: 39 */     checkValue(result);
/*  26: 40 */     return result;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static final void checkValue(double result)
/*  30:    */     throws EvaluationException
/*  31:    */   {
/*  32: 47 */     if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/*  33: 48 */       throw new EvaluationException(ErrorEval.NUM_ERROR);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  38:    */   {
/*  39:    */     double result;
/*  40:    */     try
/*  41:    */     {
/*  42: 55 */       result = eval(args, srcCellRow, srcCellCol);
/*  43: 56 */       checkValue(result);
/*  44:    */     }
/*  45:    */     catch (EvaluationException e)
/*  46:    */     {
/*  47: 58 */       return e.getErrorEval();
/*  48:    */     }
/*  49: 60 */     return new NumberEval(result);
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected abstract double eval(ValueEval[] paramArrayOfValueEval, int paramInt1, int paramInt2)
/*  53:    */     throws EvaluationException;
/*  54:    */   
/*  55:    */   public static abstract class OneArg
/*  56:    */     extends Fixed1ArgFunction
/*  57:    */   {
/*  58:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  59:    */     {
/*  60:    */       double result;
/*  61:    */       try
/*  62:    */       {
/*  63: 75 */         double d = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  64: 76 */         result = evaluate(d);
/*  65: 77 */         NumericFunction.checkValue(result);
/*  66:    */       }
/*  67:    */       catch (EvaluationException e)
/*  68:    */       {
/*  69: 79 */         return e.getErrorEval();
/*  70:    */       }
/*  71: 81 */       return new NumberEval(result);
/*  72:    */     }
/*  73:    */     
/*  74:    */     protected final double eval(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  75:    */       throws EvaluationException
/*  76:    */     {
/*  77: 84 */       if (args.length != 1) {
/*  78: 85 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  79:    */       }
/*  80: 87 */       double d = NumericFunction.singleOperandEvaluate(args[0], srcCellRow, srcCellCol);
/*  81: 88 */       return evaluate(d);
/*  82:    */     }
/*  83:    */     
/*  84:    */     protected abstract double evaluate(double paramDouble)
/*  85:    */       throws EvaluationException;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static abstract class TwoArg
/*  89:    */     extends Fixed2ArgFunction
/*  90:    */   {
/*  91:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  92:    */     {
/*  93:    */       double result;
/*  94:    */       try
/*  95:    */       {
/*  96:102 */         double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  97:103 */         double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/*  98:104 */         result = evaluate(d0, d1);
/*  99:105 */         NumericFunction.checkValue(result);
/* 100:    */       }
/* 101:    */       catch (EvaluationException e)
/* 102:    */       {
/* 103:107 */         return e.getErrorEval();
/* 104:    */       }
/* 105:109 */       return new NumberEval(result);
/* 106:    */     }
/* 107:    */     
/* 108:    */     protected abstract double evaluate(double paramDouble1, double paramDouble2)
/* 109:    */       throws EvaluationException;
/* 110:    */   }
/* 111:    */   
/* 112:117 */   public static final Function ABS = new OneArg()
/* 113:    */   {
/* 114:    */     protected double evaluate(double d)
/* 115:    */     {
/* 116:119 */       return Math.abs(d);
/* 117:    */     }
/* 118:    */   };
/* 119:122 */   public static final Function ACOS = new OneArg()
/* 120:    */   {
/* 121:    */     protected double evaluate(double d)
/* 122:    */     {
/* 123:124 */       return Math.acos(d);
/* 124:    */     }
/* 125:    */   };
/* 126:127 */   public static final Function ACOSH = new OneArg()
/* 127:    */   {
/* 128:    */     protected double evaluate(double d)
/* 129:    */     {
/* 130:129 */       return MathX.acosh(d);
/* 131:    */     }
/* 132:    */   };
/* 133:132 */   public static final Function ASIN = new OneArg()
/* 134:    */   {
/* 135:    */     protected double evaluate(double d)
/* 136:    */     {
/* 137:134 */       return Math.asin(d);
/* 138:    */     }
/* 139:    */   };
/* 140:137 */   public static final Function ASINH = new OneArg()
/* 141:    */   {
/* 142:    */     protected double evaluate(double d)
/* 143:    */     {
/* 144:139 */       return MathX.asinh(d);
/* 145:    */     }
/* 146:    */   };
/* 147:142 */   public static final Function ATAN = new OneArg()
/* 148:    */   {
/* 149:    */     protected double evaluate(double d)
/* 150:    */     {
/* 151:144 */       return Math.atan(d);
/* 152:    */     }
/* 153:    */   };
/* 154:147 */   public static final Function ATANH = new OneArg()
/* 155:    */   {
/* 156:    */     protected double evaluate(double d)
/* 157:    */     {
/* 158:149 */       return MathX.atanh(d);
/* 159:    */     }
/* 160:    */   };
/* 161:152 */   public static final Function COS = new OneArg()
/* 162:    */   {
/* 163:    */     protected double evaluate(double d)
/* 164:    */     {
/* 165:154 */       return Math.cos(d);
/* 166:    */     }
/* 167:    */   };
/* 168:157 */   public static final Function COSH = new OneArg()
/* 169:    */   {
/* 170:    */     protected double evaluate(double d)
/* 171:    */     {
/* 172:159 */       return MathX.cosh(d);
/* 173:    */     }
/* 174:    */   };
/* 175:162 */   public static final Function DEGREES = new OneArg()
/* 176:    */   {
/* 177:    */     protected double evaluate(double d)
/* 178:    */     {
/* 179:164 */       return Math.toDegrees(d);
/* 180:    */     }
/* 181:    */   };
/* 182:167 */   static final NumberEval DOLLAR_ARG2_DEFAULT = new NumberEval(2.0D);
/* 183:168 */   public static final Function DOLLAR = new Var1or2ArgFunction()
/* 184:    */   {
/* 185:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 186:    */     {
/* 187:170 */       return evaluate(srcRowIndex, srcColumnIndex, arg0, NumericFunction.DOLLAR_ARG2_DEFAULT);
/* 188:    */     }
/* 189:    */     
/* 190:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 191:    */     {
/* 192:    */       double val;
/* 193:    */       double d1;
/* 194:    */       try
/* 195:    */       {
/* 196:178 */         val = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 197:179 */         d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/* 198:    */       }
/* 199:    */       catch (EvaluationException e)
/* 200:    */       {
/* 201:181 */         return e.getErrorEval();
/* 202:    */       }
/* 203:184 */       int nPlaces = (int)d1;
/* 204:186 */       if (nPlaces > 127) {
/* 205:187 */         return ErrorEval.VALUE_INVALID;
/* 206:    */       }
/* 207:194 */       return new NumberEval(val);
/* 208:    */     }
/* 209:    */   };
/* 210:197 */   public static final Function EXP = new OneArg()
/* 211:    */   {
/* 212:    */     protected double evaluate(double d)
/* 213:    */     {
/* 214:199 */       return Math.pow(2.718281828459045D, d);
/* 215:    */     }
/* 216:    */   };
/* 217:202 */   public static final Function FACT = new OneArg()
/* 218:    */   {
/* 219:    */     protected double evaluate(double d)
/* 220:    */     {
/* 221:204 */       return MathX.factorial((int)d);
/* 222:    */     }
/* 223:    */   };
/* 224:207 */   public static final Function INT = new OneArg()
/* 225:    */   {
/* 226:    */     protected double evaluate(double d)
/* 227:    */     {
/* 228:209 */       return Math.round(d - 0.5D);
/* 229:    */     }
/* 230:    */   };
/* 231:212 */   public static final Function LN = new OneArg()
/* 232:    */   {
/* 233:    */     protected double evaluate(double d)
/* 234:    */     {
/* 235:214 */       return Math.log(d);
/* 236:    */     }
/* 237:    */   };
/* 238:217 */   public static final Function LOG10 = new OneArg()
/* 239:    */   {
/* 240:    */     protected double evaluate(double d)
/* 241:    */     {
/* 242:219 */       return Math.log(d) / NumericFunction.LOG_10_TO_BASE_e;
/* 243:    */     }
/* 244:    */   };
/* 245:222 */   public static final Function RADIANS = new OneArg()
/* 246:    */   {
/* 247:    */     protected double evaluate(double d)
/* 248:    */     {
/* 249:224 */       return Math.toRadians(d);
/* 250:    */     }
/* 251:    */   };
/* 252:227 */   public static final Function SIGN = new OneArg()
/* 253:    */   {
/* 254:    */     protected double evaluate(double d)
/* 255:    */     {
/* 256:229 */       return MathX.sign(d);
/* 257:    */     }
/* 258:    */   };
/* 259:232 */   public static final Function SIN = new OneArg()
/* 260:    */   {
/* 261:    */     protected double evaluate(double d)
/* 262:    */     {
/* 263:234 */       return Math.sin(d);
/* 264:    */     }
/* 265:    */   };
/* 266:237 */   public static final Function SINH = new OneArg()
/* 267:    */   {
/* 268:    */     protected double evaluate(double d)
/* 269:    */     {
/* 270:239 */       return MathX.sinh(d);
/* 271:    */     }
/* 272:    */   };
/* 273:242 */   public static final Function SQRT = new OneArg()
/* 274:    */   {
/* 275:    */     protected double evaluate(double d)
/* 276:    */     {
/* 277:244 */       return Math.sqrt(d);
/* 278:    */     }
/* 279:    */   };
/* 280:248 */   public static final Function TAN = new OneArg()
/* 281:    */   {
/* 282:    */     protected double evaluate(double d)
/* 283:    */     {
/* 284:250 */       return Math.tan(d);
/* 285:    */     }
/* 286:    */   };
/* 287:253 */   public static final Function TANH = new OneArg()
/* 288:    */   {
/* 289:    */     protected double evaluate(double d)
/* 290:    */     {
/* 291:255 */       return MathX.tanh(d);
/* 292:    */     }
/* 293:    */   };
/* 294:261 */   public static final Function ATAN2 = new TwoArg()
/* 295:    */   {
/* 296:    */     protected double evaluate(double d0, double d1)
/* 297:    */       throws EvaluationException
/* 298:    */     {
/* 299:263 */       if ((d0 == 0.0D) && (d1 == 0.0D)) {
/* 300:264 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 301:    */       }
/* 302:266 */       return Math.atan2(d1, d0);
/* 303:    */     }
/* 304:    */   };
/* 305:269 */   public static final Function CEILING = new TwoArg()
/* 306:    */   {
/* 307:    */     protected double evaluate(double d0, double d1)
/* 308:    */     {
/* 309:271 */       return MathX.ceiling(d0, d1);
/* 310:    */     }
/* 311:    */   };
/* 312:274 */   public static final Function COMBIN = new TwoArg()
/* 313:    */   {
/* 314:    */     protected double evaluate(double d0, double d1)
/* 315:    */       throws EvaluationException
/* 316:    */     {
/* 317:276 */       if ((d0 > 2147483647.0D) || (d1 > 2147483647.0D)) {
/* 318:277 */         throw new EvaluationException(ErrorEval.NUM_ERROR);
/* 319:    */       }
/* 320:279 */       return MathX.nChooseK((int)d0, (int)d1);
/* 321:    */     }
/* 322:    */   };
/* 323:282 */   public static final Function FLOOR = new TwoArg()
/* 324:    */   {
/* 325:    */     protected double evaluate(double d0, double d1)
/* 326:    */       throws EvaluationException
/* 327:    */     {
/* 328:284 */       if (d1 == 0.0D)
/* 329:    */       {
/* 330:285 */         if (d0 == 0.0D) {
/* 331:286 */           return 0.0D;
/* 332:    */         }
/* 333:288 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 334:    */       }
/* 335:290 */       return MathX.floor(d0, d1);
/* 336:    */     }
/* 337:    */   };
/* 338:293 */   public static final Function MOD = new TwoArg()
/* 339:    */   {
/* 340:    */     protected double evaluate(double d0, double d1)
/* 341:    */       throws EvaluationException
/* 342:    */     {
/* 343:295 */       if (d1 == 0.0D) {
/* 344:296 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 345:    */       }
/* 346:298 */       return MathX.mod(d0, d1);
/* 347:    */     }
/* 348:    */   };
/* 349:301 */   public static final Function POWER = new TwoArg()
/* 350:    */   {
/* 351:    */     protected double evaluate(double d0, double d1)
/* 352:    */     {
/* 353:303 */       return Math.pow(d0, d1);
/* 354:    */     }
/* 355:    */   };
/* 356:306 */   public static final Function ROUND = new TwoArg()
/* 357:    */   {
/* 358:    */     protected double evaluate(double d0, double d1)
/* 359:    */     {
/* 360:308 */       return MathX.round(d0, (int)d1);
/* 361:    */     }
/* 362:    */   };
/* 363:311 */   public static final Function ROUNDDOWN = new TwoArg()
/* 364:    */   {
/* 365:    */     protected double evaluate(double d0, double d1)
/* 366:    */     {
/* 367:313 */       return MathX.roundDown(d0, (int)d1);
/* 368:    */     }
/* 369:    */   };
/* 370:316 */   public static final Function ROUNDUP = new TwoArg()
/* 371:    */   {
/* 372:    */     protected double evaluate(double d0, double d1)
/* 373:    */     {
/* 374:318 */       return MathX.roundUp(d0, (int)d1);
/* 375:    */     }
/* 376:    */   };
/* 377:321 */   static final NumberEval TRUNC_ARG2_DEFAULT = new NumberEval(0.0D);
/* 378:322 */   public static final Function TRUNC = new Var1or2ArgFunction()
/* 379:    */   {
/* 380:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 381:    */     {
/* 382:325 */       return evaluate(srcRowIndex, srcColumnIndex, arg0, NumericFunction.TRUNC_ARG2_DEFAULT);
/* 383:    */     }
/* 384:    */     
/* 385:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 386:    */     {
/* 387:    */       double result;
/* 388:    */       try
/* 389:    */       {
/* 390:331 */         double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 391:332 */         double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/* 392:333 */         double multi = Math.pow(10.0D, d1);
/* 393:    */         double result;
/* 394:334 */         if (d0 < 0.0D) {
/* 395:334 */           result = -Math.floor(-d0 * multi) / multi;
/* 396:    */         } else {
/* 397:335 */           result = Math.floor(d0 * multi) / multi;
/* 398:    */         }
/* 399:336 */         NumericFunction.checkValue(result);
/* 400:    */       }
/* 401:    */       catch (EvaluationException e)
/* 402:    */       {
/* 403:338 */         return e.getErrorEval();
/* 404:    */       }
/* 405:340 */       return new NumberEval(result);
/* 406:    */     }
/* 407:    */   };
/* 408:    */   
/* 409:    */   private static final class Log
/* 410:    */     extends Var1or2ArgFunction
/* 411:    */   {
/* 412:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 413:    */     {
/* 414:    */       double result;
/* 415:    */       try
/* 416:    */       {
/* 417:353 */         double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 418:354 */         result = Math.log(d0) / NumericFunction.LOG_10_TO_BASE_e;
/* 419:355 */         NumericFunction.checkValue(result);
/* 420:    */       }
/* 421:    */       catch (EvaluationException e)
/* 422:    */       {
/* 423:357 */         return e.getErrorEval();
/* 424:    */       }
/* 425:359 */       return new NumberEval(result);
/* 426:    */     }
/* 427:    */     
/* 428:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 429:    */     {
/* 430:    */       double result;
/* 431:    */       try
/* 432:    */       {
/* 433:365 */         double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 434:366 */         double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/* 435:367 */         double logE = Math.log(d0);
/* 436:368 */         double base = d1;
/* 437:    */         double result;
/* 438:369 */         if (Double.compare(base, 2.718281828459045D) == 0) {
/* 439:370 */           result = logE;
/* 440:    */         } else {
/* 441:372 */           result = logE / Math.log(base);
/* 442:    */         }
/* 443:374 */         NumericFunction.checkValue(result);
/* 444:    */       }
/* 445:    */       catch (EvaluationException e)
/* 446:    */       {
/* 447:376 */         return e.getErrorEval();
/* 448:    */       }
/* 449:378 */       return new NumberEval(result);
/* 450:    */     }
/* 451:    */   }
/* 452:    */   
/* 453:382 */   public static final Function LOG = new Log();
/* 454:384 */   static final NumberEval PI_EVAL = new NumberEval(3.141592653589793D);
/* 455:385 */   public static final Function PI = new Fixed0ArgFunction()
/* 456:    */   {
/* 457:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 458:    */     {
/* 459:387 */       return NumericFunction.PI_EVAL;
/* 460:    */     }
/* 461:    */   };
/* 462:390 */   public static final Function RAND = new Fixed0ArgFunction()
/* 463:    */   {
/* 464:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 465:    */     {
/* 466:392 */       return new NumberEval(Math.random());
/* 467:    */     }
/* 468:    */   };
/* 469:395 */   public static final Function POISSON = new Fixed3ArgFunction()
/* 470:    */   {
/* 471:    */     private static final double DEFAULT_RETURN_RESULT = 1.0D;
/* 472:    */     
/* 473:    */     private boolean isDefaultResult(double x, double mean)
/* 474:    */     {
/* 475:409 */       if ((x == 0.0D) && (mean == 0.0D)) {
/* 476:410 */         return true;
/* 477:    */       }
/* 478:412 */       return false;
/* 479:    */     }
/* 480:    */     
/* 481:    */     private boolean checkArgument(double aDouble)
/* 482:    */       throws EvaluationException
/* 483:    */     {
/* 484:417 */       NumericFunction.checkValue(aDouble);
/* 485:420 */       if (aDouble < 0.0D) {
/* 486:421 */         throw new EvaluationException(ErrorEval.NUM_ERROR);
/* 487:    */       }
/* 488:424 */       return true;
/* 489:    */     }
/* 490:    */     
/* 491:    */     private double probability(int k, double lambda)
/* 492:    */     {
/* 493:428 */       return Math.pow(lambda, k) * Math.exp(-lambda) / factorial(k);
/* 494:    */     }
/* 495:    */     
/* 496:    */     private double cumulativeProbability(int x, double lambda)
/* 497:    */     {
/* 498:432 */       double result = 0.0D;
/* 499:433 */       for (int k = 0; k <= x; k++) {
/* 500:434 */         result += probability(k, lambda);
/* 501:    */       }
/* 502:436 */       return result;
/* 503:    */     }
/* 504:    */     
/* 505:440 */     private final long[] FACTORIALS = { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/* 506:    */     
/* 507:    */     public long factorial(int n)
/* 508:    */     {
/* 509:451 */       if ((n < 0) || (n > 20)) {
/* 510:452 */         throw new IllegalArgumentException("Valid argument should be in the range [0..20]");
/* 511:    */       }
/* 512:454 */       return this.FACTORIALS[n];
/* 513:    */     }
/* 514:    */     
/* 515:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/* 516:    */     {
/* 517:460 */       double mean = 0.0D;
/* 518:461 */       double x = 0.0D;
/* 519:462 */       boolean cumulative = ((BoolEval)arg2).getBooleanValue();
/* 520:463 */       double result = 0.0D;
/* 521:    */       try
/* 522:    */       {
/* 523:466 */         x = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/* 524:467 */         mean = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/* 525:471 */         if (isDefaultResult(x, mean)) {
/* 526:472 */           return new NumberEval(1.0D);
/* 527:    */         }
/* 528:475 */         checkArgument(x);
/* 529:476 */         checkArgument(mean);
/* 530:479 */         if (cumulative) {
/* 531:480 */           result = cumulativeProbability((int)x, mean);
/* 532:    */         } else {
/* 533:482 */           result = probability((int)x, mean);
/* 534:    */         }
/* 535:486 */         NumericFunction.checkValue(result);
/* 536:    */       }
/* 537:    */       catch (EvaluationException e)
/* 538:    */       {
/* 539:489 */         return e.getErrorEval();
/* 540:    */       }
/* 541:492 */       return new NumberEval(result);
/* 542:    */     }
/* 543:    */   };
/* 544:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.NumericFunction
 * JD-Core Version:    0.7.0.1
 */