/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  10:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  13:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  16:    */ 
/*  17:    */ final class LookupUtils
/*  18:    */ {
/*  19:    */   public static abstract interface ValueVector
/*  20:    */   {
/*  21:    */     public abstract ValueEval getItem(int paramInt);
/*  22:    */     
/*  23:    */     public abstract int getSize();
/*  24:    */   }
/*  25:    */   
/*  26:    */   private static final class RowVector
/*  27:    */     implements ValueVector
/*  28:    */   {
/*  29:    */     private final TwoDEval _tableArray;
/*  30:    */     private final int _size;
/*  31:    */     private final int _rowIndex;
/*  32:    */     
/*  33:    */     public RowVector(TwoDEval tableArray, int rowIndex)
/*  34:    */     {
/*  35: 56 */       this._rowIndex = rowIndex;
/*  36: 57 */       int lastRowIx = tableArray.getHeight() - 1;
/*  37: 58 */       if ((rowIndex < 0) || (rowIndex > lastRowIx)) {
/*  38: 59 */         throw new IllegalArgumentException("Specified row index (" + rowIndex + ") is outside the allowed range (0.." + lastRowIx + ")");
/*  39:    */       }
/*  40: 62 */       this._tableArray = tableArray;
/*  41: 63 */       this._size = tableArray.getWidth();
/*  42:    */     }
/*  43:    */     
/*  44:    */     public ValueEval getItem(int index)
/*  45:    */     {
/*  46: 67 */       if (index > this._size) {
/*  47: 68 */         throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
/*  48:    */       }
/*  49: 71 */       return this._tableArray.getValue(this._rowIndex, index);
/*  50:    */     }
/*  51:    */     
/*  52:    */     public int getSize()
/*  53:    */     {
/*  54: 74 */       return this._size;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static final class ColumnVector
/*  59:    */     implements ValueVector
/*  60:    */   {
/*  61:    */     private final TwoDEval _tableArray;
/*  62:    */     private final int _size;
/*  63:    */     private final int _columnIndex;
/*  64:    */     
/*  65:    */     public ColumnVector(TwoDEval tableArray, int columnIndex)
/*  66:    */     {
/*  67: 85 */       this._columnIndex = columnIndex;
/*  68: 86 */       int lastColIx = tableArray.getWidth() - 1;
/*  69: 87 */       if ((columnIndex < 0) || (columnIndex > lastColIx)) {
/*  70: 88 */         throw new IllegalArgumentException("Specified column index (" + columnIndex + ") is outside the allowed range (0.." + lastColIx + ")");
/*  71:    */       }
/*  72: 91 */       this._tableArray = tableArray;
/*  73: 92 */       this._size = this._tableArray.getHeight();
/*  74:    */     }
/*  75:    */     
/*  76:    */     public ValueEval getItem(int index)
/*  77:    */     {
/*  78: 96 */       if (index > this._size) {
/*  79: 97 */         throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
/*  80:    */       }
/*  81:100 */       return this._tableArray.getValue(index, this._columnIndex);
/*  82:    */     }
/*  83:    */     
/*  84:    */     public int getSize()
/*  85:    */     {
/*  86:103 */       return this._size;
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   private static final class SheetVector
/*  91:    */     implements ValueVector
/*  92:    */   {
/*  93:    */     private final RefEval _re;
/*  94:    */     private final int _size;
/*  95:    */     
/*  96:    */     public SheetVector(RefEval re)
/*  97:    */     {
/*  98:112 */       this._size = re.getNumberOfSheets();
/*  99:113 */       this._re = re;
/* 100:    */     }
/* 101:    */     
/* 102:    */     public ValueEval getItem(int index)
/* 103:    */     {
/* 104:117 */       if (index >= this._size) {
/* 105:118 */         throw new ArrayIndexOutOfBoundsException("Specified index (" + index + ") is outside the allowed range (0.." + (this._size - 1) + ")");
/* 106:    */       }
/* 107:121 */       int sheetIndex = this._re.getFirstSheetIndex() + index;
/* 108:122 */       return this._re.getInnerValueEval(sheetIndex);
/* 109:    */     }
/* 110:    */     
/* 111:    */     public int getSize()
/* 112:    */     {
/* 113:125 */       return this._size;
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static ValueVector createRowVector(TwoDEval tableArray, int relativeRowIndex)
/* 118:    */   {
/* 119:130 */     return new RowVector(tableArray, relativeRowIndex);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static ValueVector createColumnVector(TwoDEval tableArray, int relativeColumnIndex)
/* 123:    */   {
/* 124:133 */     return new ColumnVector(tableArray, relativeColumnIndex);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static ValueVector createVector(TwoDEval ae)
/* 128:    */   {
/* 129:139 */     if (ae.isColumn()) {
/* 130:140 */       return createColumnVector(ae, 0);
/* 131:    */     }
/* 132:142 */     if (ae.isRow()) {
/* 133:143 */       return createRowVector(ae, 0);
/* 134:    */     }
/* 135:145 */     return null;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static ValueVector createVector(RefEval re)
/* 139:    */   {
/* 140:149 */     return new SheetVector(re);
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static final class CompareResult
/* 144:    */   {
/* 145:    */     private final boolean _isTypeMismatch;
/* 146:    */     private final boolean _isLessThan;
/* 147:    */     private final boolean _isEqual;
/* 148:    */     private final boolean _isGreaterThan;
/* 149:    */     
/* 150:    */     private CompareResult(boolean isTypeMismatch, int simpleCompareResult)
/* 151:    */     {
/* 152:170 */       if (isTypeMismatch)
/* 153:    */       {
/* 154:171 */         this._isTypeMismatch = true;
/* 155:172 */         this._isLessThan = false;
/* 156:173 */         this._isEqual = false;
/* 157:174 */         this._isGreaterThan = false;
/* 158:    */       }
/* 159:    */       else
/* 160:    */       {
/* 161:176 */         this._isTypeMismatch = false;
/* 162:177 */         this._isLessThan = (simpleCompareResult < 0);
/* 163:178 */         this._isEqual = (simpleCompareResult == 0);
/* 164:179 */         this._isGreaterThan = (simpleCompareResult > 0);
/* 165:    */       }
/* 166:    */     }
/* 167:    */     
/* 168:182 */     public static final CompareResult TYPE_MISMATCH = new CompareResult(true, 0);
/* 169:183 */     public static final CompareResult LESS_THAN = new CompareResult(false, -1);
/* 170:184 */     public static final CompareResult EQUAL = new CompareResult(false, 0);
/* 171:185 */     public static final CompareResult GREATER_THAN = new CompareResult(false, 1);
/* 172:    */     
/* 173:    */     public static CompareResult valueOf(int simpleCompareResult)
/* 174:    */     {
/* 175:188 */       if (simpleCompareResult < 0) {
/* 176:189 */         return LESS_THAN;
/* 177:    */       }
/* 178:191 */       if (simpleCompareResult > 0) {
/* 179:192 */         return GREATER_THAN;
/* 180:    */       }
/* 181:194 */       return EQUAL;
/* 182:    */     }
/* 183:    */     
/* 184:    */     public static CompareResult valueOf(boolean matches)
/* 185:    */     {
/* 186:198 */       if (matches) {
/* 187:199 */         return EQUAL;
/* 188:    */       }
/* 189:201 */       return LESS_THAN;
/* 190:    */     }
/* 191:    */     
/* 192:    */     public boolean isTypeMismatch()
/* 193:    */     {
/* 194:206 */       return this._isTypeMismatch;
/* 195:    */     }
/* 196:    */     
/* 197:    */     public boolean isLessThan()
/* 198:    */     {
/* 199:209 */       return this._isLessThan;
/* 200:    */     }
/* 201:    */     
/* 202:    */     public boolean isEqual()
/* 203:    */     {
/* 204:212 */       return this._isEqual;
/* 205:    */     }
/* 206:    */     
/* 207:    */     public boolean isGreaterThan()
/* 208:    */     {
/* 209:215 */       return this._isGreaterThan;
/* 210:    */     }
/* 211:    */     
/* 212:    */     public String toString()
/* 213:    */     {
/* 214:218 */       return getClass().getName() + " [" + formatAsString() + "]";
/* 215:    */     }
/* 216:    */     
/* 217:    */     private String formatAsString()
/* 218:    */     {
/* 219:224 */       if (this._isTypeMismatch) {
/* 220:225 */         return "TYPE_MISMATCH";
/* 221:    */       }
/* 222:227 */       if (this._isLessThan) {
/* 223:228 */         return "LESS_THAN";
/* 224:    */       }
/* 225:230 */       if (this._isEqual) {
/* 226:231 */         return "EQUAL";
/* 227:    */       }
/* 228:233 */       if (this._isGreaterThan) {
/* 229:234 */         return "GREATER_THAN";
/* 230:    */       }
/* 231:237 */       return "??error??";
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static abstract interface LookupValueComparer
/* 236:    */   {
/* 237:    */     public abstract CompareResult compareTo(ValueEval paramValueEval);
/* 238:    */   }
/* 239:    */   
/* 240:    */   private static abstract class LookupValueComparerBase
/* 241:    */     implements LookupValueComparer
/* 242:    */   {
/* 243:    */     private final Class<? extends ValueEval> _targetClass;
/* 244:    */     
/* 245:    */     protected LookupValueComparerBase(ValueEval targetValue)
/* 246:    */     {
/* 247:253 */       if (targetValue == null) {
/* 248:254 */         throw new RuntimeException("targetValue cannot be null");
/* 249:    */       }
/* 250:256 */       this._targetClass = targetValue.getClass();
/* 251:    */     }
/* 252:    */     
/* 253:    */     public final CompareResult compareTo(ValueEval other)
/* 254:    */     {
/* 255:259 */       if (other == null) {
/* 256:260 */         throw new RuntimeException("compare to value cannot be null");
/* 257:    */       }
/* 258:262 */       if (this._targetClass != other.getClass()) {
/* 259:263 */         return CompareResult.TYPE_MISMATCH;
/* 260:    */       }
/* 261:265 */       return compareSameType(other);
/* 262:    */     }
/* 263:    */     
/* 264:    */     public String toString()
/* 265:    */     {
/* 266:268 */       return getClass().getName() + " [" + getValueAsString() + "]";
/* 267:    */     }
/* 268:    */     
/* 269:    */     protected abstract CompareResult compareSameType(ValueEval paramValueEval);
/* 270:    */     
/* 271:    */     protected abstract String getValueAsString();
/* 272:    */   }
/* 273:    */   
/* 274:    */   private static final class StringLookupComparer
/* 275:    */     extends LookupValueComparerBase
/* 276:    */   {
/* 277:    */     private String _value;
/* 278:    */     private final Pattern _wildCardPattern;
/* 279:    */     private boolean _matchExact;
/* 280:    */     private boolean _isMatchFunction;
/* 281:    */     
/* 282:    */     protected StringLookupComparer(StringEval se, boolean matchExact, boolean isMatchFunction)
/* 283:    */     {
/* 284:286 */       super();
/* 285:287 */       this._value = se.getStringValue();
/* 286:288 */       this._wildCardPattern = Countif.StringMatcher.getWildCardPattern(this._value);
/* 287:289 */       this._matchExact = matchExact;
/* 288:290 */       this._isMatchFunction = isMatchFunction;
/* 289:    */     }
/* 290:    */     
/* 291:    */     protected CompareResult compareSameType(ValueEval other)
/* 292:    */     {
/* 293:294 */       StringEval se = (StringEval)other;
/* 294:    */       
/* 295:296 */       String stringValue = se.getStringValue();
/* 296:297 */       if (this._wildCardPattern != null)
/* 297:    */       {
/* 298:298 */         Matcher matcher = this._wildCardPattern.matcher(stringValue);
/* 299:299 */         boolean matches = matcher.matches();
/* 300:301 */         if ((this._isMatchFunction) || (!this._matchExact)) {
/* 301:303 */           return CompareResult.valueOf(matches);
/* 302:    */         }
/* 303:    */       }
/* 304:307 */       return CompareResult.valueOf(this._value.compareToIgnoreCase(stringValue));
/* 305:    */     }
/* 306:    */     
/* 307:    */     protected String getValueAsString()
/* 308:    */     {
/* 309:310 */       return this._value;
/* 310:    */     }
/* 311:    */   }
/* 312:    */   
/* 313:    */   private static final class NumberLookupComparer
/* 314:    */     extends LookupValueComparerBase
/* 315:    */   {
/* 316:    */     private double _value;
/* 317:    */     
/* 318:    */     protected NumberLookupComparer(NumberEval ne)
/* 319:    */     {
/* 320:317 */       super();
/* 321:318 */       this._value = ne.getNumberValue();
/* 322:    */     }
/* 323:    */     
/* 324:    */     protected CompareResult compareSameType(ValueEval other)
/* 325:    */     {
/* 326:321 */       NumberEval ne = (NumberEval)other;
/* 327:322 */       return CompareResult.valueOf(Double.compare(this._value, ne.getNumberValue()));
/* 328:    */     }
/* 329:    */     
/* 330:    */     protected String getValueAsString()
/* 331:    */     {
/* 332:325 */       return String.valueOf(this._value);
/* 333:    */     }
/* 334:    */   }
/* 335:    */   
/* 336:    */   private static final class BooleanLookupComparer
/* 337:    */     extends LookupValueComparerBase
/* 338:    */   {
/* 339:    */     private boolean _value;
/* 340:    */     
/* 341:    */     protected BooleanLookupComparer(BoolEval be)
/* 342:    */     {
/* 343:332 */       super();
/* 344:333 */       this._value = be.getBooleanValue();
/* 345:    */     }
/* 346:    */     
/* 347:    */     protected CompareResult compareSameType(ValueEval other)
/* 348:    */     {
/* 349:336 */       BoolEval be = (BoolEval)other;
/* 350:337 */       boolean otherVal = be.getBooleanValue();
/* 351:338 */       if (this._value == otherVal) {
/* 352:339 */         return CompareResult.EQUAL;
/* 353:    */       }
/* 354:342 */       if (this._value) {
/* 355:343 */         return CompareResult.GREATER_THAN;
/* 356:    */       }
/* 357:345 */       return CompareResult.LESS_THAN;
/* 358:    */     }
/* 359:    */     
/* 360:    */     protected String getValueAsString()
/* 361:    */     {
/* 362:348 */       return String.valueOf(this._value);
/* 363:    */     }
/* 364:    */   }
/* 365:    */   
/* 366:    */   public static int resolveRowOrColIndexArg(ValueEval rowColIndexArg, int srcCellRow, int srcCellCol)
/* 367:    */     throws EvaluationException
/* 368:    */   {
/* 369:376 */     if (rowColIndexArg == null) {
/* 370:377 */       throw new IllegalArgumentException("argument must not be null");
/* 371:    */     }
/* 372:    */     ValueEval veRowColIndexArg;
/* 373:    */     try
/* 374:    */     {
/* 375:382 */       veRowColIndexArg = OperandResolver.getSingleValue(rowColIndexArg, srcCellRow, (short)srcCellCol);
/* 376:    */     }
/* 377:    */     catch (EvaluationException e)
/* 378:    */     {
/* 379:385 */       throw EvaluationException.invalidRef();
/* 380:    */     }
/* 381:388 */     if ((veRowColIndexArg instanceof StringEval))
/* 382:    */     {
/* 383:389 */       StringEval se = (StringEval)veRowColIndexArg;
/* 384:390 */       String strVal = se.getStringValue();
/* 385:391 */       Double dVal = OperandResolver.parseDouble(strVal);
/* 386:392 */       if (dVal == null) {
/* 387:394 */         throw EvaluationException.invalidRef();
/* 388:    */       }
/* 389:    */     }
/* 390:400 */     int oneBasedIndex = OperandResolver.coerceValueToInt(veRowColIndexArg);
/* 391:401 */     if (oneBasedIndex < 1) {
/* 392:403 */       throw EvaluationException.invalidValue();
/* 393:    */     }
/* 394:405 */     return oneBasedIndex - 1;
/* 395:    */   }
/* 396:    */   
/* 397:    */   public static TwoDEval resolveTableArrayArg(ValueEval eval)
/* 398:    */     throws EvaluationException
/* 399:    */   {
/* 400:415 */     if ((eval instanceof TwoDEval)) {
/* 401:416 */       return (TwoDEval)eval;
/* 402:    */     }
/* 403:419 */     if ((eval instanceof RefEval))
/* 404:    */     {
/* 405:420 */       RefEval refEval = (RefEval)eval;
/* 406:    */       
/* 407:    */ 
/* 408:    */ 
/* 409:424 */       return refEval.offset(0, 0, 0, 0);
/* 410:    */     }
/* 411:426 */     throw EvaluationException.invalidValue();
/* 412:    */   }
/* 413:    */   
/* 414:    */   public static boolean resolveRangeLookupArg(ValueEval rangeLookupArg, int srcCellRow, int srcCellCol)
/* 415:    */     throws EvaluationException
/* 416:    */   {
/* 417:436 */     ValueEval valEval = OperandResolver.getSingleValue(rangeLookupArg, srcCellRow, srcCellCol);
/* 418:437 */     if ((valEval instanceof BlankEval)) {
/* 419:441 */       return false;
/* 420:    */     }
/* 421:443 */     if ((valEval instanceof BoolEval))
/* 422:    */     {
/* 423:445 */       BoolEval boolEval = (BoolEval)valEval;
/* 424:446 */       return boolEval.getBooleanValue();
/* 425:    */     }
/* 426:449 */     if ((valEval instanceof StringEval))
/* 427:    */     {
/* 428:450 */       String stringValue = ((StringEval)valEval).getStringValue();
/* 429:451 */       if (stringValue.length() < 1) {
/* 430:454 */         throw EvaluationException.invalidValue();
/* 431:    */       }
/* 432:457 */       Boolean b = Countif.parseBoolean(stringValue);
/* 433:458 */       if (b != null) {
/* 434:460 */         return b.booleanValue();
/* 435:    */       }
/* 436:465 */       throw EvaluationException.invalidValue();
/* 437:    */     }
/* 438:469 */     if ((valEval instanceof NumericValueEval))
/* 439:    */     {
/* 440:470 */       NumericValueEval nve = (NumericValueEval)valEval;
/* 441:    */       
/* 442:472 */       return 0.0D != nve.getNumberValue();
/* 443:    */     }
/* 444:474 */     throw new RuntimeException("Unexpected eval type (" + valEval + ")");
/* 445:    */   }
/* 446:    */   
/* 447:    */   public static int lookupIndexOfValue(ValueEval lookupValue, ValueVector vector, boolean isRangeLookup)
/* 448:    */     throws EvaluationException
/* 449:    */   {
/* 450:478 */     LookupValueComparer lookupComparer = createLookupComparer(lookupValue, isRangeLookup, false);
/* 451:    */     int result;
/* 452:    */     int result;
/* 453:480 */     if (isRangeLookup) {
/* 454:481 */       result = performBinarySearch(vector, lookupComparer);
/* 455:    */     } else {
/* 456:483 */       result = lookupIndexOfExactValue(lookupComparer, vector);
/* 457:    */     }
/* 458:485 */     if (result < 0) {
/* 459:486 */       throw new EvaluationException(ErrorEval.NA);
/* 460:    */     }
/* 461:488 */     return result;
/* 462:    */   }
/* 463:    */   
/* 464:    */   private static int lookupIndexOfExactValue(LookupValueComparer lookupComparer, ValueVector vector)
/* 465:    */   {
/* 466:502 */     int size = vector.getSize();
/* 467:503 */     for (int i = 0; i < size; i++) {
/* 468:504 */       if (lookupComparer.compareTo(vector.getItem(i)).isEqual()) {
/* 469:505 */         return i;
/* 470:    */       }
/* 471:    */     }
/* 472:508 */     return -1;
/* 473:    */   }
/* 474:    */   
/* 475:    */   private static final class BinarySearchIndexes
/* 476:    */   {
/* 477:    */     private int _lowIx;
/* 478:    */     private int _highIx;
/* 479:    */     
/* 480:    */     public BinarySearchIndexes(int highIx)
/* 481:    */     {
/* 482:522 */       this._lowIx = -1;
/* 483:523 */       this._highIx = highIx;
/* 484:    */     }
/* 485:    */     
/* 486:    */     public int getMidIx()
/* 487:    */     {
/* 488:530 */       int ixDiff = this._highIx - this._lowIx;
/* 489:531 */       if (ixDiff < 2) {
/* 490:532 */         return -1;
/* 491:    */       }
/* 492:534 */       return this._lowIx + ixDiff / 2;
/* 493:    */     }
/* 494:    */     
/* 495:    */     public int getLowIx()
/* 496:    */     {
/* 497:538 */       return this._lowIx;
/* 498:    */     }
/* 499:    */     
/* 500:    */     public int getHighIx()
/* 501:    */     {
/* 502:541 */       return this._highIx;
/* 503:    */     }
/* 504:    */     
/* 505:    */     public void narrowSearch(int midIx, boolean isLessThan)
/* 506:    */     {
/* 507:544 */       if (isLessThan) {
/* 508:545 */         this._highIx = midIx;
/* 509:    */       } else {
/* 510:547 */         this._lowIx = midIx;
/* 511:    */       }
/* 512:    */     }
/* 513:    */   }
/* 514:    */   
/* 515:    */   private static int performBinarySearch(ValueVector vector, LookupValueComparer lookupComparer)
/* 516:    */   {
/* 517:557 */     BinarySearchIndexes bsi = new BinarySearchIndexes(vector.getSize());
/* 518:    */     for (;;)
/* 519:    */     {
/* 520:560 */       int midIx = bsi.getMidIx();
/* 521:562 */       if (midIx < 0) {
/* 522:563 */         return bsi.getLowIx();
/* 523:    */       }
/* 524:565 */       CompareResult cr = lookupComparer.compareTo(vector.getItem(midIx));
/* 525:566 */       if (cr.isTypeMismatch())
/* 526:    */       {
/* 527:567 */         int newMidIx = handleMidValueTypeMismatch(lookupComparer, vector, bsi, midIx);
/* 528:568 */         if (newMidIx >= 0)
/* 529:    */         {
/* 530:571 */           midIx = newMidIx;
/* 531:572 */           cr = lookupComparer.compareTo(vector.getItem(midIx));
/* 532:    */         }
/* 533:    */       }
/* 534:    */       else
/* 535:    */       {
/* 536:574 */         if (cr.isEqual()) {
/* 537:575 */           return findLastIndexInRunOfEqualValues(lookupComparer, vector, midIx, bsi.getHighIx());
/* 538:    */         }
/* 539:577 */         bsi.narrowSearch(midIx, cr.isLessThan());
/* 540:    */       }
/* 541:    */     }
/* 542:    */   }
/* 543:    */   
/* 544:    */   private static int handleMidValueTypeMismatch(LookupValueComparer lookupComparer, ValueVector vector, BinarySearchIndexes bsi, int midIx)
/* 545:    */   {
/* 546:589 */     int newMid = midIx;
/* 547:590 */     int highIx = bsi.getHighIx();
/* 548:    */     CompareResult cr;
/* 549:    */     do
/* 550:    */     {
/* 551:593 */       newMid++;
/* 552:594 */       if (newMid == highIx)
/* 553:    */       {
/* 554:597 */         bsi.narrowSearch(midIx, true);
/* 555:598 */         return -1;
/* 556:    */       }
/* 557:600 */       cr = lookupComparer.compareTo(vector.getItem(newMid));
/* 558:601 */       if ((cr.isLessThan()) && (newMid == highIx - 1))
/* 559:    */       {
/* 560:603 */         bsi.narrowSearch(midIx, true);
/* 561:604 */         return -1;
/* 562:    */       }
/* 563:608 */     } while (cr.isTypeMismatch());
/* 564:612 */     if (cr.isEqual()) {
/* 565:613 */       return newMid;
/* 566:    */     }
/* 567:618 */     bsi.narrowSearch(newMid, cr.isLessThan());
/* 568:619 */     return -1;
/* 569:    */   }
/* 570:    */   
/* 571:    */   private static int findLastIndexInRunOfEqualValues(LookupValueComparer lookupComparer, ValueVector vector, int firstFoundIndex, int maxIx)
/* 572:    */   {
/* 573:628 */     for (int i = firstFoundIndex + 1; i < maxIx; i++) {
/* 574:629 */       if (!lookupComparer.compareTo(vector.getItem(i)).isEqual()) {
/* 575:630 */         return i - 1;
/* 576:    */       }
/* 577:    */     }
/* 578:633 */     return maxIx - 1;
/* 579:    */   }
/* 580:    */   
/* 581:    */   public static LookupValueComparer createLookupComparer(ValueEval lookupValue, boolean matchExact, boolean isMatchFunction)
/* 582:    */   {
/* 583:638 */     if (lookupValue == BlankEval.instance) {
/* 584:642 */       return new NumberLookupComparer(NumberEval.ZERO);
/* 585:    */     }
/* 586:644 */     if ((lookupValue instanceof StringEval)) {
/* 587:646 */       return new StringLookupComparer((StringEval)lookupValue, matchExact, isMatchFunction);
/* 588:    */     }
/* 589:648 */     if ((lookupValue instanceof NumberEval)) {
/* 590:649 */       return new NumberLookupComparer((NumberEval)lookupValue);
/* 591:    */     }
/* 592:651 */     if ((lookupValue instanceof BoolEval)) {
/* 593:652 */       return new BooleanLookupComparer((BoolEval)lookupValue);
/* 594:    */     }
/* 595:654 */     throw new IllegalArgumentException("Bad lookup value type (" + lookupValue.getClass().getName() + ")");
/* 596:    */   }
/* 597:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.LookupUtils

 * JD-Core Version:    0.7.0.1

 */