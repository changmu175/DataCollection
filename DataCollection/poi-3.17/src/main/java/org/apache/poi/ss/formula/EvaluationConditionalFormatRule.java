/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  16:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  17:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  18:    */ import org.apache.poi.ss.formula.functions.AggregateFunction;
/*  19:    */ import org.apache.poi.ss.formula.functions.Function;
/*  20:    */ import org.apache.poi.ss.usermodel.Cell;
/*  21:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  22:    */ import org.apache.poi.ss.usermodel.CellType;
/*  23:    */ import org.apache.poi.ss.usermodel.ConditionFilterData;
/*  24:    */ import org.apache.poi.ss.usermodel.ConditionFilterType;
/*  25:    */ import org.apache.poi.ss.usermodel.ConditionType;
/*  26:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*  27:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*  28:    */ import org.apache.poi.ss.usermodel.ExcelNumberFormat;
/*  29:    */ import org.apache.poi.ss.usermodel.Row;
/*  30:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  31:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  32:    */ import org.apache.poi.ss.util.CellReference;
/*  33:    */ 
/*  34:    */ public class EvaluationConditionalFormatRule
/*  35:    */   implements Comparable<EvaluationConditionalFormatRule>
/*  36:    */ {
/*  37:    */   private final WorkbookEvaluator workbookEvaluator;
/*  38:    */   private final Sheet sheet;
/*  39:    */   private final ConditionalFormatting formatting;
/*  40:    */   private final ConditionalFormattingRule rule;
/*  41:    */   private final CellRangeAddress[] regions;
/*  42: 81 */   private final Map<CellRangeAddress, Set<ValueAndFormat>> meaningfulRegionValues = new HashMap();
/*  43:    */   private final int priority;
/*  44:    */   private final int formattingIndex;
/*  45:    */   private final int ruleIndex;
/*  46:    */   private final String formula1;
/*  47:    */   private final String formula2;
/*  48:    */   private final OperatorEnum operator;
/*  49:    */   private final ConditionType type;
/*  50:    */   private final ExcelNumberFormat numberFormat;
/*  51:    */   
/*  52:    */   public EvaluationConditionalFormatRule(WorkbookEvaluator workbookEvaluator, Sheet sheet, ConditionalFormatting formatting, int formattingIndex, ConditionalFormattingRule rule, int ruleIndex, CellRangeAddress[] regions)
/*  53:    */   {
/*  54:106 */     this.workbookEvaluator = workbookEvaluator;
/*  55:107 */     this.sheet = sheet;
/*  56:108 */     this.formatting = formatting;
/*  57:109 */     this.rule = rule;
/*  58:110 */     this.formattingIndex = formattingIndex;
/*  59:111 */     this.ruleIndex = ruleIndex;
/*  60:    */     
/*  61:113 */     this.priority = rule.getPriority();
/*  62:    */     
/*  63:115 */     this.regions = regions;
/*  64:116 */     this.formula1 = rule.getFormula1();
/*  65:117 */     this.formula2 = rule.getFormula2();
/*  66:118 */     this.numberFormat = rule.getNumberFormat();
/*  67:    */     
/*  68:120 */     this.operator = OperatorEnum.values()[rule.getComparisonOperation()];
/*  69:121 */     this.type = rule.getConditionType();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Sheet getSheet()
/*  73:    */   {
/*  74:128 */     return this.sheet;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public ConditionalFormatting getFormatting()
/*  78:    */   {
/*  79:135 */     return this.formatting;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int getFormattingIndex()
/*  83:    */   {
/*  84:142 */     return this.formattingIndex;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public ExcelNumberFormat getNumberFormat()
/*  88:    */   {
/*  89:149 */     return this.numberFormat;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public ConditionalFormattingRule getRule()
/*  93:    */   {
/*  94:156 */     return this.rule;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public int getRuleIndex()
/*  98:    */   {
/*  99:163 */     return this.ruleIndex;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public CellRangeAddress[] getRegions()
/* 103:    */   {
/* 104:170 */     return this.regions;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public int getPriority()
/* 108:    */   {
/* 109:177 */     return this.priority;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public String getFormula1()
/* 113:    */   {
/* 114:184 */     return this.formula1;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public String getFormula2()
/* 118:    */   {
/* 119:191 */     return this.formula2;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public OperatorEnum getOperator()
/* 123:    */   {
/* 124:198 */     return this.operator;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public ConditionType getType()
/* 128:    */   {
/* 129:205 */     return this.type;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean equals(Object obj)
/* 133:    */   {
/* 134:214 */     if (obj == null) {
/* 135:215 */       return false;
/* 136:    */     }
/* 137:217 */     if (!obj.getClass().equals(getClass())) {
/* 138:218 */       return false;
/* 139:    */     }
/* 140:220 */     EvaluationConditionalFormatRule r = (EvaluationConditionalFormatRule)obj;
/* 141:221 */     return (getSheet().getSheetName().equalsIgnoreCase(r.getSheet().getSheetName())) && (getFormattingIndex() == r.getFormattingIndex()) && (getRuleIndex() == r.getRuleIndex());
/* 142:    */   }
/* 143:    */   
/* 144:    */   public int compareTo(EvaluationConditionalFormatRule o)
/* 145:    */   {
/* 146:237 */     int cmp = getSheet().getSheetName().compareToIgnoreCase(o.getSheet().getSheetName());
/* 147:238 */     if (cmp != 0) {
/* 148:239 */       return cmp;
/* 149:    */     }
/* 150:242 */     int x = getPriority();
/* 151:243 */     int y = o.getPriority();
/* 152:    */     
/* 153:245 */     cmp = x == y ? 0 : x < y ? -1 : 1;
/* 154:246 */     if (cmp != 0) {
/* 155:247 */       return cmp;
/* 156:    */     }
/* 157:250 */     cmp = new Integer(getFormattingIndex()).compareTo(new Integer(o.getFormattingIndex()));
/* 158:251 */     if (cmp != 0) {
/* 159:252 */       return cmp;
/* 160:    */     }
/* 161:254 */     return new Integer(getRuleIndex()).compareTo(new Integer(o.getRuleIndex()));
/* 162:    */   }
/* 163:    */   
/* 164:    */   public int hashCode()
/* 165:    */   {
/* 166:259 */     int hash = this.sheet.getSheetName().hashCode();
/* 167:260 */     hash = 31 * hash + this.formattingIndex;
/* 168:261 */     hash = 31 * hash + this.ruleIndex;
/* 169:262 */     return hash;
/* 170:    */   }
/* 171:    */   
/* 172:    */   boolean matches(CellReference ref)
/* 173:    */   {
/* 174:271 */     CellRangeAddress region = null;
/* 175:272 */     for (CellRangeAddress r : this.regions) {
/* 176:273 */       if (r.isInRange(ref))
/* 177:    */       {
/* 178:274 */         region = r;
/* 179:275 */         break;
/* 180:    */       }
/* 181:    */     }
/* 182:279 */     if (region == null) {
/* 183:281 */       return false;
/* 184:    */     }
/* 185:284 */     ConditionType ruleType = getRule().getConditionType();
/* 186:288 */     if ((ruleType.equals(ConditionType.COLOR_SCALE)) || (ruleType.equals(ConditionType.DATA_BAR)) || (ruleType.equals(ConditionType.ICON_SET))) {
/* 187:291 */       return true;
/* 188:    */     }
/* 189:294 */     Cell cell = null;
/* 190:295 */     Row row = this.sheet.getRow(ref.getRow());
/* 191:296 */     if (row != null) {
/* 192:297 */       cell = row.getCell(ref.getCol());
/* 193:    */     }
/* 194:300 */     if (ruleType.equals(ConditionType.CELL_VALUE_IS))
/* 195:    */     {
/* 196:302 */       if (cell == null) {
/* 197:302 */         return false;
/* 198:    */       }
/* 199:303 */       return checkValue(cell, region);
/* 200:    */     }
/* 201:305 */     if (ruleType.equals(ConditionType.FORMULA)) {
/* 202:306 */       return checkFormula(ref, region);
/* 203:    */     }
/* 204:308 */     if (ruleType.equals(ConditionType.FILTER)) {
/* 205:309 */       return checkFilter(cell, ref, region);
/* 206:    */     }
/* 207:313 */     return false;
/* 208:    */   }
/* 209:    */   
/* 210:    */   private boolean checkValue(Cell cell, CellRangeAddress region)
/* 211:    */   {
/* 212:322 */     if ((cell == null) || (DataValidationEvaluator.isType(cell, CellType.BLANK)) || (DataValidationEvaluator.isType(cell, CellType.ERROR)) || ((DataValidationEvaluator.isType(cell, CellType.STRING)) && ((cell.getStringCellValue() == null) || (cell.getStringCellValue().isEmpty())))) {
/* 213:328 */       return false;
/* 214:    */     }
/* 215:331 */     ValueEval eval = unwrapEval(this.workbookEvaluator.evaluate(this.rule.getFormula1(), ConditionalFormattingEvaluator.getRef(cell), region));
/* 216:    */     
/* 217:333 */     String f2 = this.rule.getFormula2();
/* 218:334 */     ValueEval eval2 = null;
/* 219:335 */     if ((f2 != null) && (f2.length() > 0)) {
/* 220:336 */       eval2 = unwrapEval(this.workbookEvaluator.evaluate(f2, ConditionalFormattingEvaluator.getRef(cell), region));
/* 221:    */     }
/* 222:340 */     if (DataValidationEvaluator.isType(cell, CellType.BOOLEAN))
/* 223:    */     {
/* 224:341 */       if (((eval instanceof BoolEval)) && ((eval2 == null) || ((eval2 instanceof BoolEval)))) {
/* 225:342 */         return this.operator.isValid(Boolean.valueOf(cell.getBooleanCellValue()), Boolean.valueOf(((BoolEval)eval).getBooleanValue()), eval2 == null ? null : Boolean.valueOf(((BoolEval)eval2).getBooleanValue()));
/* 226:    */       }
/* 227:344 */       return false;
/* 228:    */     }
/* 229:346 */     if (DataValidationEvaluator.isType(cell, CellType.NUMERIC))
/* 230:    */     {
/* 231:347 */       if (((eval instanceof NumberEval)) && ((eval2 == null) || ((eval2 instanceof NumberEval)))) {
/* 232:348 */         return this.operator.isValid(Double.valueOf(cell.getNumericCellValue()), Double.valueOf(((NumberEval)eval).getNumberValue()), eval2 == null ? null : Double.valueOf(((NumberEval)eval2).getNumberValue()));
/* 233:    */       }
/* 234:350 */       return false;
/* 235:    */     }
/* 236:352 */     if (DataValidationEvaluator.isType(cell, CellType.STRING))
/* 237:    */     {
/* 238:353 */       if (((eval instanceof StringEval)) && ((eval2 == null) || ((eval2 instanceof StringEval)))) {
/* 239:354 */         return this.operator.isValid(cell.getStringCellValue(), ((StringEval)eval).getStringValue(), eval2 == null ? null : ((StringEval)eval2).getStringValue());
/* 240:    */       }
/* 241:356 */       return false;
/* 242:    */     }
/* 243:360 */     return false;
/* 244:    */   }
/* 245:    */   
/* 246:    */   private ValueEval unwrapEval(ValueEval eval)
/* 247:    */   {
/* 248:364 */     ValueEval comp = eval;
/* 249:366 */     while ((comp instanceof RefEval))
/* 250:    */     {
/* 251:367 */       RefEval ref = (RefEval)comp;
/* 252:368 */       comp = ref.getInnerValueEval(ref.getFirstSheetIndex());
/* 253:    */     }
/* 254:370 */     return comp;
/* 255:    */   }
/* 256:    */   
/* 257:    */   private boolean checkFormula(CellReference ref, CellRangeAddress region)
/* 258:    */   {
/* 259:378 */     ValueEval comp = unwrapEval(this.workbookEvaluator.evaluate(this.rule.getFormula1(), ref, region));
/* 260:381 */     if ((comp instanceof BlankEval)) {
/* 261:382 */       return true;
/* 262:    */     }
/* 263:384 */     if ((comp instanceof ErrorEval)) {
/* 264:385 */       return false;
/* 265:    */     }
/* 266:387 */     if ((comp instanceof BoolEval)) {
/* 267:388 */       return ((BoolEval)comp).getBooleanValue();
/* 268:    */     }
/* 269:392 */     if ((comp instanceof NumberEval)) {
/* 270:393 */       return ((NumberEval)comp).getNumberValue() != 0.0D;
/* 271:    */     }
/* 272:395 */     return false;
/* 273:    */   }
/* 274:    */   
/* 275:    */   private boolean checkFilter(Cell cell, CellReference ref, CellRangeAddress region)
/* 276:    */   {
/* 277:399 */     ConditionFilterType filterType = this.rule.getConditionFilterType();
/* 278:400 */     if (filterType == null) {
/* 279:401 */       return false;
/* 280:    */     }
/* 281:404 */     ValueAndFormat cv = getCellValue(cell);
/* 282:410 */     switch (5.$SwitchMap$org$apache$poi$ss$usermodel$ConditionFilterType[filterType.ordinal()])
/* 283:    */     {
/* 284:    */     case 1: 
/* 285:412 */       return false;
/* 286:    */     case 2: 
/* 287:417 */       if (!cv.isNumber()) {
/* 288:418 */         return false;
/* 289:    */       }
/* 290:421 */       getMeaningfulValues(region, false, new ValueFunction()
/* 291:    */       {
/* 292:    */         public Set<ValueAndFormat> evaluate(List<ValueAndFormat> allValues)
/* 293:    */         {
/* 294:424 */           List<ValueAndFormat> values = allValues;
/* 295:425 */           ConditionFilterData conf = EvaluationConditionalFormatRule.this.rule.getFilterConfiguration();
/* 296:427 */           if (!conf.getBottom()) {
/* 297:428 */             Collections.sort(values, Collections.reverseOrder());
/* 298:    */           } else {
/* 299:430 */             Collections.sort(values);
/* 300:    */           }
/* 301:433 */           int limit = (int)conf.getRank();
/* 302:434 */           if (conf.getPercent()) {
/* 303:435 */             limit = allValues.size() * limit / 100;
/* 304:    */           }
/* 305:437 */           if (allValues.size() <= limit) {
/* 306:438 */             return new HashSet(allValues);
/* 307:    */           }
/* 308:441 */           return new HashSet(allValues.subList(0, limit));
/* 309:    */         }
/* 310:441 */       }).contains(cv);
/* 311:    */     case 3: 
/* 312:447 */       getMeaningfulValues(region, true, new ValueFunction()
/* 313:    */       {
/* 314:    */         public Set<ValueAndFormat> evaluate(List<ValueAndFormat> allValues)
/* 315:    */         {
/* 316:450 */           List<ValueAndFormat> values = allValues;
/* 317:451 */           Collections.sort(values);
/* 318:    */           
/* 319:453 */           Set<ValueAndFormat> unique = new HashSet();
/* 320:455 */           for (int i = 0; i < values.size(); i++)
/* 321:    */           {
/* 322:456 */             ValueAndFormat v = (ValueAndFormat)values.get(i);
/* 323:458 */             if (((i < values.size() - 1) && (v.equals(values.get(i + 1)))) || ((i > 0) && (i == values.size() - 1) && (v.equals(values.get(i - 1))))) {
/* 324:460 */               i++;
/* 325:    */             } else {
/* 326:463 */               unique.add(v);
/* 327:    */             }
/* 328:    */           }
/* 329:466 */           return unique;
/* 330:    */         }
/* 331:466 */       }).contains(cv);
/* 332:    */     case 4: 
/* 333:472 */       getMeaningfulValues(region, true, new ValueFunction()
/* 334:    */       {
/* 335:    */         public Set<ValueAndFormat> evaluate(List<ValueAndFormat> allValues)
/* 336:    */         {
/* 337:475 */           List<ValueAndFormat> values = allValues;
/* 338:476 */           Collections.sort(values);
/* 339:    */           
/* 340:478 */           Set<ValueAndFormat> dup = new HashSet();
/* 341:480 */           for (int i = 0; i < values.size(); i++)
/* 342:    */           {
/* 343:481 */             ValueAndFormat v = (ValueAndFormat)values.get(i);
/* 344:483 */             if (((i < values.size() - 1) && (v.equals(values.get(i + 1)))) || ((i > 0) && (i == values.size() - 1) && (v.equals(values.get(i - 1)))))
/* 345:    */             {
/* 346:485 */               dup.add(v);
/* 347:486 */               i++;
/* 348:    */             }
/* 349:    */           }
/* 350:489 */           return dup;
/* 351:    */         }
/* 352:489 */       }).contains(cv);
/* 353:    */     case 5: 
/* 354:496 */       ConditionFilterData conf = this.rule.getFilterConfiguration();
/* 355:    */       
/* 356:    */ 
/* 357:499 */       List<ValueAndFormat> values = new ArrayList(getMeaningfulValues(region, false, new ValueFunction()
/* 358:    */       {
/* 359:    */         public Set<ValueAndFormat> evaluate(List<ValueAndFormat> allValues)
/* 360:    */         {
/* 361:502 */           List<ValueAndFormat> values = allValues;
/* 362:503 */           double total = 0.0D;
/* 363:504 */           ValueEval[] pop = new ValueEval[values.size()];
/* 364:505 */           for (int i = 0; i < values.size(); i++)
/* 365:    */           {
/* 366:506 */             ValueAndFormat v = (ValueAndFormat)values.get(i);
/* 367:507 */             total += v.value.doubleValue();
/* 368:508 */             pop[i] = new NumberEval(v.value.doubleValue());
/* 369:    */           }
/* 370:511 */           Set<ValueAndFormat> avgSet = new LinkedHashSet(1);
/* 371:512 */           avgSet.add(new ValueAndFormat(new Double(values.size() == 0 ? 0.0D : total / values.size()), null));
/* 372:    */           
/* 373:514 */           double stdDev = values.size() <= 1 ? 0.0D : ((NumberEval)AggregateFunction.STDEV.evaluate(pop, 0, 0)).getNumberValue();
/* 374:515 */           avgSet.add(new ValueAndFormat(new Double(stdDev), null));
/* 375:516 */           return avgSet;
/* 376:    */         }
/* 377:519 */       }));
/* 378:520 */       Double val = cv.isNumber() ? cv.getValue() : null;
/* 379:521 */       if (val == null) {
/* 380:522 */         return false;
/* 381:    */       }
/* 382:525 */       double avg = ((ValueAndFormat)values.get(0)).value.doubleValue();
/* 383:526 */       double stdDev = ((ValueAndFormat)values.get(1)).value.doubleValue();
/* 384:    */       
/* 385:    */ 
/* 386:    */ 
/* 387:    */ 
/* 388:    */ 
/* 389:    */ 
/* 390:    */ 
/* 391:534 */       Double comp = new Double(conf.getStdDev() > 0 ? avg + (conf.getAboveAverage() ? 1 : -1) * stdDev * conf.getStdDev() : avg);
/* 392:    */       
/* 393:536 */       OperatorEnum op = null;
/* 394:537 */       if (conf.getAboveAverage())
/* 395:    */       {
/* 396:538 */         if (conf.getEqualAverage()) {
/* 397:539 */           op = OperatorEnum.GREATER_OR_EQUAL;
/* 398:    */         } else {
/* 399:541 */           op = OperatorEnum.GREATER_THAN;
/* 400:    */         }
/* 401:    */       }
/* 402:544 */       else if (conf.getEqualAverage()) {
/* 403:545 */         op = OperatorEnum.LESS_OR_EQUAL;
/* 404:    */       } else {
/* 405:547 */         op = OperatorEnum.LESS_THAN;
/* 406:    */       }
/* 407:550 */       return (op != null) && (op.isValid(val, comp, null));
/* 408:    */     case 6: 
/* 409:553 */       return checkFormula(ref, region);
/* 410:    */     case 7: 
/* 411:556 */       return checkFormula(ref, region);
/* 412:    */     case 8: 
/* 413:559 */       return checkFormula(ref, region);
/* 414:    */     case 9: 
/* 415:562 */       return checkFormula(ref, region);
/* 416:    */     case 10: 
/* 417:    */       try
/* 418:    */       {
/* 419:565 */         String v = cv.getString();
/* 420:    */         
/* 421:567 */         return (v == null) || (v.trim().length() == 0);
/* 422:    */       }
/* 423:    */       catch (Exception e)
/* 424:    */       {
/* 425:570 */         return false;
/* 426:    */       }
/* 427:    */     case 11: 
/* 428:    */       try
/* 429:    */       {
/* 430:574 */         String v = cv.getString();
/* 431:    */         
/* 432:576 */         return (v != null) && (v.trim().length() > 0);
/* 433:    */       }
/* 434:    */       catch (Exception e)
/* 435:    */       {
/* 436:579 */         return true;
/* 437:    */       }
/* 438:    */     case 12: 
/* 439:582 */       return (cell != null) && (DataValidationEvaluator.isType(cell, CellType.ERROR));
/* 440:    */     case 13: 
/* 441:584 */       return (cell == null) || (!DataValidationEvaluator.isType(cell, CellType.ERROR));
/* 442:    */     case 14: 
/* 443:587 */       return checkFormula(ref, region);
/* 444:    */     }
/* 445:589 */     return false;
/* 446:    */   }
/* 447:    */   
/* 448:    */   private Set<ValueAndFormat> getMeaningfulValues(CellRangeAddress region, boolean withText, ValueFunction func)
/* 449:    */   {
/* 450:601 */     Set<ValueAndFormat> values = (Set)this.meaningfulRegionValues.get(region);
/* 451:602 */     if (values != null) {
/* 452:603 */       return values;
/* 453:    */     }
/* 454:606 */     List<ValueAndFormat> allValues = new ArrayList((region.getLastColumn() - region.getFirstColumn() + 1) * (region.getLastRow() - region.getFirstRow() + 1));
/* 455:608 */     for (int r = region.getFirstRow(); r <= region.getLastRow(); r++)
/* 456:    */     {
/* 457:609 */       Row row = this.sheet.getRow(r);
/* 458:610 */       if (row != null) {
/* 459:613 */         for (int c = region.getFirstColumn(); c <= region.getLastColumn(); c++)
/* 460:    */         {
/* 461:614 */           Cell cell = row.getCell(c);
/* 462:615 */           ValueAndFormat cv = getCellValue(cell);
/* 463:616 */           if ((cv != null) && ((withText) || (cv.isNumber()))) {
/* 464:617 */             allValues.add(cv);
/* 465:    */           }
/* 466:    */         }
/* 467:    */       }
/* 468:    */     }
/* 469:622 */     values = func.evaluate(allValues);
/* 470:623 */     this.meaningfulRegionValues.put(region, values);
/* 471:    */     
/* 472:625 */     return values;
/* 473:    */   }
/* 474:    */   
/* 475:    */   private ValueAndFormat getCellValue(Cell cell)
/* 476:    */   {
/* 477:629 */     if (cell != null)
/* 478:    */     {
/* 479:630 */       CellType type = cell.getCellTypeEnum();
/* 480:631 */       if ((type == CellType.NUMERIC) || ((type == CellType.FORMULA) && (cell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC))) {
/* 481:632 */         return new ValueAndFormat(new Double(cell.getNumericCellValue()), cell.getCellStyle().getDataFormatString());
/* 482:    */       }
/* 483:633 */       if ((type == CellType.STRING) || ((type == CellType.FORMULA) && (cell.getCachedFormulaResultTypeEnum() == CellType.STRING))) {
/* 484:634 */         return new ValueAndFormat(cell.getStringCellValue(), cell.getCellStyle().getDataFormatString());
/* 485:    */       }
/* 486:635 */       if ((type == CellType.BOOLEAN) || ((type == CellType.FORMULA) && (cell.getCachedFormulaResultTypeEnum() == CellType.BOOLEAN))) {
/* 487:636 */         return new ValueAndFormat(cell.getStringCellValue(), cell.getCellStyle().getDataFormatString());
/* 488:    */       }
/* 489:    */     }
/* 490:639 */     return new ValueAndFormat("", "");
/* 491:    */   }
/* 492:    */   
/* 493:    */   protected static abstract interface ValueFunction
/* 494:    */   {
/* 495:    */     public abstract Set<ValueAndFormat> evaluate(List<ValueAndFormat> paramList);
/* 496:    */   }
/* 497:    */   
/* 498:    */   public static abstract enum OperatorEnum
/* 499:    */   {
/* 500:662 */     NO_COMPARISON,  BETWEEN,  NOT_BETWEEN,  EQUAL,  NOT_EQUAL,  GREATER_THAN,  LESS_THAN,  GREATER_OR_EQUAL,  LESS_OR_EQUAL;
/* 501:    */     
/* 502:    */     private OperatorEnum() {}
/* 503:    */     
/* 504:    */     public abstract <C extends Comparable<C>> boolean isValid(C paramC1, C paramC2, C paramC3);
/* 505:    */   }
/* 506:    */   
/* 507:    */   protected static class ValueAndFormat
/* 508:    */     implements Comparable<ValueAndFormat>
/* 509:    */   {
/* 510:    */     private final Double value;
/* 511:    */     private final String string;
/* 512:    */     private final String format;
/* 513:    */     
/* 514:    */     public ValueAndFormat(Double value, String format)
/* 515:    */     {
/* 516:747 */       this.value = value;
/* 517:748 */       this.format = format;
/* 518:749 */       this.string = null;
/* 519:    */     }
/* 520:    */     
/* 521:    */     public ValueAndFormat(String value, String format)
/* 522:    */     {
/* 523:753 */       this.value = null;
/* 524:754 */       this.format = format;
/* 525:755 */       this.string = value;
/* 526:    */     }
/* 527:    */     
/* 528:    */     public boolean isNumber()
/* 529:    */     {
/* 530:759 */       return this.value != null;
/* 531:    */     }
/* 532:    */     
/* 533:    */     public Double getValue()
/* 534:    */     {
/* 535:763 */       return this.value;
/* 536:    */     }
/* 537:    */     
/* 538:    */     public String getString()
/* 539:    */     {
/* 540:767 */       return this.string;
/* 541:    */     }
/* 542:    */     
/* 543:    */     public boolean equals(Object obj)
/* 544:    */     {
/* 545:772 */       if (!(obj instanceof ValueAndFormat)) {
/* 546:773 */         return false;
/* 547:    */       }
/* 548:775 */       ValueAndFormat o = (ValueAndFormat)obj;
/* 549:776 */       return ((this.value == o.value) || (this.value.equals(o.value))) && ((this.format == o.format) || (this.format.equals(o.format))) && ((this.string == o.string) || (this.string.equals(o.string)));
/* 550:    */     }
/* 551:    */     
/* 552:    */     public int compareTo(ValueAndFormat o)
/* 553:    */     {
/* 554:788 */       if ((this.value == null) && (o.value != null)) {
/* 555:789 */         return 1;
/* 556:    */       }
/* 557:791 */       if ((o.value == null) && (this.value != null)) {
/* 558:792 */         return -1;
/* 559:    */       }
/* 560:794 */       int cmp = this.value == null ? 0 : this.value.compareTo(o.value);
/* 561:795 */       if (cmp != 0) {
/* 562:796 */         return cmp;
/* 563:    */       }
/* 564:799 */       if ((this.string == null) && (o.string != null)) {
/* 565:800 */         return 1;
/* 566:    */       }
/* 567:802 */       if ((o.string == null) && (this.string != null)) {
/* 568:803 */         return -1;
/* 569:    */       }
/* 570:806 */       return this.string == null ? 0 : this.string.compareTo(o.string);
/* 571:    */     }
/* 572:    */     
/* 573:    */     public int hashCode()
/* 574:    */     {
/* 575:811 */       return (this.string == null ? 0 : this.string.hashCode()) * 37 * 37 + 37 * (this.value == null ? 0 : this.value.hashCode()) + (this.format == null ? 0 : this.format.hashCode());
/* 576:    */     }
/* 577:    */   }
/* 578:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.EvaluationConditionalFormatRule

 * JD-Core Version:    0.7.0.1

 */