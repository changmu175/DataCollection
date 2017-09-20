/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.text.ParseException;
/*   4:    */ import java.text.SimpleDateFormat;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.regex.Pattern;
/*   7:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   8:    */ import org.apache.poi.hssf.record.DVRecord;
/*   9:    */ import org.apache.poi.ss.formula.FormulaRenderer;
/*  10:    */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  11:    */ import org.apache.poi.ss.formula.FormulaType;
/*  12:    */ import org.apache.poi.ss.formula.ptg.NumberPtg;
/*  13:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  14:    */ import org.apache.poi.ss.formula.ptg.StringPtg;
/*  15:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint;
/*  16:    */ import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
/*  17:    */ import org.apache.poi.util.LocaleUtil;
/*  18:    */ 
/*  19:    */ public class DVConstraint
/*  20:    */   implements DataValidationConstraint
/*  21:    */ {
/*  22:    */   private final int _validationType;
/*  23:    */   private int _operator;
/*  24:    */   private String[] _explicitListValues;
/*  25:    */   private String _formula1;
/*  26:    */   private String _formula2;
/*  27:    */   private Double _value1;
/*  28:    */   private Double _value2;
/*  29:    */   
/*  30:    */   static final class FormulaPair
/*  31:    */   {
/*  32:    */     private final Ptg[] _formula1;
/*  33:    */     private final Ptg[] _formula2;
/*  34:    */     
/*  35:    */     FormulaPair(Ptg[] formula1, Ptg[] formula2)
/*  36:    */     {
/*  37: 46 */       this._formula1 = (formula1 == null ? null : (Ptg[])formula1.clone());
/*  38: 47 */       this._formula2 = (formula2 == null ? null : (Ptg[])formula2.clone());
/*  39:    */     }
/*  40:    */     
/*  41:    */     public Ptg[] getFormula1()
/*  42:    */     {
/*  43: 50 */       return this._formula1;
/*  44:    */     }
/*  45:    */     
/*  46:    */     public Ptg[] getFormula2()
/*  47:    */     {
/*  48: 53 */       return this._formula2;
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   private DVConstraint(int validationType, int comparisonOperator, String formulaA, String formulaB, Double value1, Double value2, String[] excplicitListValues)
/*  53:    */   {
/*  54: 70 */     this._validationType = validationType;
/*  55: 71 */     this._operator = comparisonOperator;
/*  56: 72 */     this._formula1 = formulaA;
/*  57: 73 */     this._formula2 = formulaB;
/*  58: 74 */     this._value1 = value1;
/*  59: 75 */     this._value2 = value2;
/*  60: 76 */     this._explicitListValues = (excplicitListValues == null ? null : (String[])excplicitListValues.clone());
/*  61:    */   }
/*  62:    */   
/*  63:    */   private DVConstraint(String listFormula, String[] excplicitListValues)
/*  64:    */   {
/*  65: 84 */     this(3, 0, listFormula, null, null, null, excplicitListValues);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static DVConstraint createNumericConstraint(int validationType, int comparisonOperator, String expr1, String expr2)
/*  69:    */   {
/*  70:103 */     switch (validationType)
/*  71:    */     {
/*  72:    */     case 0: 
/*  73:105 */       if ((expr1 != null) || (expr2 != null)) {
/*  74:106 */         throw new IllegalArgumentException("expr1 and expr2 must be null for validation type 'any'");
/*  75:    */       }
/*  76:    */       break;
/*  77:    */     case 1: 
/*  78:    */     case 2: 
/*  79:    */     case 6: 
/*  80:112 */       if (expr1 == null) {
/*  81:113 */         throw new IllegalArgumentException("expr1 must be supplied");
/*  82:    */       }
/*  83:115 */       OperatorType.validateSecondArg(comparisonOperator, expr2);
/*  84:116 */       break;
/*  85:    */     case 3: 
/*  86:    */     case 4: 
/*  87:    */     case 5: 
/*  88:    */     default: 
/*  89:118 */       throw new IllegalArgumentException("Validation Type (" + validationType + ") not supported with this method");
/*  90:    */     }
/*  91:122 */     String formula1 = getFormulaFromTextExpression(expr1);
/*  92:123 */     Double value1 = formula1 == null ? convertNumber(expr1) : null;
/*  93:    */     
/*  94:125 */     String formula2 = getFormulaFromTextExpression(expr2);
/*  95:126 */     Double value2 = formula2 == null ? convertNumber(expr2) : null;
/*  96:127 */     return new DVConstraint(validationType, comparisonOperator, formula1, formula2, value1, value2, null);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static DVConstraint createFormulaListConstraint(String listFormula)
/* 100:    */   {
/* 101:131 */     return new DVConstraint(listFormula, null);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static DVConstraint createExplicitListConstraint(String[] explicitListValues)
/* 105:    */   {
/* 106:134 */     return new DVConstraint(null, explicitListValues);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static DVConstraint createTimeConstraint(int comparisonOperator, String expr1, String expr2)
/* 110:    */   {
/* 111:150 */     if (expr1 == null) {
/* 112:151 */       throw new IllegalArgumentException("expr1 must be supplied");
/* 113:    */     }
/* 114:153 */     OperatorType.validateSecondArg(comparisonOperator, expr1);
/* 115:    */     
/* 116:    */ 
/* 117:156 */     String formula1 = getFormulaFromTextExpression(expr1);
/* 118:157 */     Double value1 = formula1 == null ? convertTime(expr1) : null;
/* 119:    */     
/* 120:159 */     String formula2 = getFormulaFromTextExpression(expr2);
/* 121:160 */     Double value2 = formula2 == null ? convertTime(expr2) : null;
/* 122:161 */     return new DVConstraint(5, comparisonOperator, formula1, formula2, value1, value2, null);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static DVConstraint createDateConstraint(int comparisonOperator, String expr1, String expr2, String dateFormat)
/* 126:    */   {
/* 127:179 */     if (expr1 == null) {
/* 128:180 */       throw new IllegalArgumentException("expr1 must be supplied");
/* 129:    */     }
/* 130:182 */     OperatorType.validateSecondArg(comparisonOperator, expr2);
/* 131:183 */     SimpleDateFormat df = null;
/* 132:184 */     if (dateFormat != null)
/* 133:    */     {
/* 134:185 */       df = new SimpleDateFormat(dateFormat, LocaleUtil.getUserLocale());
/* 135:186 */       df.setTimeZone(LocaleUtil.getUserTimeZone());
/* 136:    */     }
/* 137:190 */     String formula1 = getFormulaFromTextExpression(expr1);
/* 138:191 */     Double value1 = formula1 == null ? convertDate(expr1, df) : null;
/* 139:    */     
/* 140:193 */     String formula2 = getFormulaFromTextExpression(expr2);
/* 141:194 */     Double value2 = formula2 == null ? convertDate(expr2, df) : null;
/* 142:195 */     return new DVConstraint(4, comparisonOperator, formula1, formula2, value1, value2, null);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private static String getFormulaFromTextExpression(String textExpr)
/* 146:    */   {
/* 147:210 */     if (textExpr == null) {
/* 148:211 */       return null;
/* 149:    */     }
/* 150:213 */     if (textExpr.length() < 1) {
/* 151:214 */       throw new IllegalArgumentException("Empty string is not a valid formula/value expression");
/* 152:    */     }
/* 153:216 */     if (textExpr.charAt(0) == '=') {
/* 154:217 */       return textExpr.substring(1);
/* 155:    */     }
/* 156:219 */     return null;
/* 157:    */   }
/* 158:    */   
/* 159:    */   private static Double convertNumber(String numberStr)
/* 160:    */   {
/* 161:227 */     if (numberStr == null) {
/* 162:228 */       return null;
/* 163:    */     }
/* 164:    */     try
/* 165:    */     {
/* 166:231 */       return new Double(numberStr);
/* 167:    */     }
/* 168:    */     catch (NumberFormatException e)
/* 169:    */     {
/* 170:233 */       throw new RuntimeException("The supplied text '" + numberStr + "' could not be parsed as a number");
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   private static Double convertTime(String timeStr)
/* 175:    */   {
/* 176:242 */     if (timeStr == null) {
/* 177:243 */       return null;
/* 178:    */     }
/* 179:245 */     return new Double(HSSFDateUtil.convertTime(timeStr));
/* 180:    */   }
/* 181:    */   
/* 182:    */   private static Double convertDate(String dateStr, SimpleDateFormat dateFormat)
/* 183:    */   {
/* 184:252 */     if (dateStr == null) {
/* 185:253 */       return null;
/* 186:    */     }
/* 187:    */     Date dateVal;
/* 188:    */     Date dateVal;
/* 189:256 */     if (dateFormat == null) {
/* 190:257 */       dateVal = HSSFDateUtil.parseYYYYMMDDDate(dateStr);
/* 191:    */     } else {
/* 192:    */       try
/* 193:    */       {
/* 194:260 */         dateVal = dateFormat.parse(dateStr);
/* 195:    */       }
/* 196:    */       catch (ParseException e)
/* 197:    */       {
/* 198:262 */         throw new RuntimeException("Failed to parse date '" + dateStr + "' using specified format '" + dateFormat + "'", e);
/* 199:    */       }
/* 200:    */     }
/* 201:266 */     return new Double(HSSFDateUtil.getExcelDate(dateVal));
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static DVConstraint createCustomFormulaConstraint(String formula)
/* 205:    */   {
/* 206:270 */     if (formula == null) {
/* 207:271 */       throw new IllegalArgumentException("formula must be supplied");
/* 208:    */     }
/* 209:273 */     return new DVConstraint(7, 0, formula, null, null, null, null);
/* 210:    */   }
/* 211:    */   
/* 212:    */   public int getValidationType()
/* 213:    */   {
/* 214:280 */     return this._validationType;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public boolean isListValidationType()
/* 218:    */   {
/* 219:287 */     return this._validationType == 3;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public boolean isExplicitList()
/* 223:    */   {
/* 224:294 */     return (this._validationType == 3) && (this._explicitListValues != null);
/* 225:    */   }
/* 226:    */   
/* 227:    */   public int getOperator()
/* 228:    */   {
/* 229:300 */     return this._operator;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public void setOperator(int operator)
/* 233:    */   {
/* 234:306 */     this._operator = operator;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public String[] getExplicitListValues()
/* 238:    */   {
/* 239:313 */     return this._explicitListValues;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void setExplicitListValues(String[] explicitListValues)
/* 243:    */   {
/* 244:319 */     if (this._validationType != 3) {
/* 245:320 */       throw new RuntimeException("Cannot setExplicitListValues on non-list constraint");
/* 246:    */     }
/* 247:322 */     this._formula1 = null;
/* 248:323 */     this._explicitListValues = explicitListValues;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public String getFormula1()
/* 252:    */   {
/* 253:330 */     return this._formula1;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setFormula1(String formula1)
/* 257:    */   {
/* 258:336 */     this._value1 = null;
/* 259:337 */     this._explicitListValues = null;
/* 260:338 */     this._formula1 = formula1;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public String getFormula2()
/* 264:    */   {
/* 265:345 */     return this._formula2;
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void setFormula2(String formula2)
/* 269:    */   {
/* 270:351 */     this._value2 = null;
/* 271:352 */     this._formula2 = formula2;
/* 272:    */   }
/* 273:    */   
/* 274:    */   public Double getValue1()
/* 275:    */   {
/* 276:359 */     return this._value1;
/* 277:    */   }
/* 278:    */   
/* 279:    */   public void setValue1(double value1)
/* 280:    */   {
/* 281:365 */     this._formula1 = null;
/* 282:366 */     this._value1 = new Double(value1);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public Double getValue2()
/* 286:    */   {
/* 287:373 */     return this._value2;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public void setValue2(double value2)
/* 291:    */   {
/* 292:379 */     this._formula2 = null;
/* 293:380 */     this._value2 = new Double(value2);
/* 294:    */   }
/* 295:    */   
/* 296:    */   FormulaPair createFormulas(HSSFSheet sheet)
/* 297:    */   {
/* 298:    */     Ptg[] formula2;
/* 299:    */     Ptg[] formula1;
/* 300:    */     Ptg[] formula2;
/* 301:389 */     if (isListValidationType())
/* 302:    */     {
/* 303:390 */       Ptg[] formula1 = createListFormula(sheet);
/* 304:391 */       formula2 = Ptg.EMPTY_PTG_ARRAY;
/* 305:    */     }
/* 306:    */     else
/* 307:    */     {
/* 308:393 */       formula1 = convertDoubleFormula(this._formula1, this._value1, sheet);
/* 309:394 */       formula2 = convertDoubleFormula(this._formula2, this._value2, sheet);
/* 310:    */     }
/* 311:396 */     return new FormulaPair(formula1, formula2);
/* 312:    */   }
/* 313:    */   
/* 314:    */   private Ptg[] createListFormula(HSSFSheet sheet)
/* 315:    */   {
/* 316:402 */     if (this._explicitListValues == null)
/* 317:    */     {
/* 318:403 */       HSSFWorkbook wb = sheet.getWorkbook();
/* 319:    */       
/* 320:405 */       return HSSFFormulaParser.parse(this._formula1, wb, FormulaType.DATAVALIDATION_LIST, wb.getSheetIndex(sheet));
/* 321:    */     }
/* 322:410 */     StringBuffer sb = new StringBuffer(this._explicitListValues.length * 16);
/* 323:411 */     for (int i = 0; i < this._explicitListValues.length; i++)
/* 324:    */     {
/* 325:412 */       if (i > 0) {
/* 326:413 */         sb.append('\000');
/* 327:    */       }
/* 328:415 */       sb.append(this._explicitListValues[i]);
/* 329:    */     }
/* 330:418 */     return new Ptg[] { new StringPtg(sb.toString()) };
/* 331:    */   }
/* 332:    */   
/* 333:    */   private static Ptg[] convertDoubleFormula(String formula, Double value, HSSFSheet sheet)
/* 334:    */   {
/* 335:427 */     if (formula == null)
/* 336:    */     {
/* 337:428 */       if (value == null) {
/* 338:429 */         return Ptg.EMPTY_PTG_ARRAY;
/* 339:    */       }
/* 340:431 */       return new Ptg[] { new NumberPtg(value.doubleValue()) };
/* 341:    */     }
/* 342:433 */     if (value != null) {
/* 343:434 */       throw new IllegalStateException("Both formula and value cannot be present");
/* 344:    */     }
/* 345:436 */     HSSFWorkbook wb = sheet.getWorkbook();
/* 346:437 */     return HSSFFormulaParser.parse(formula, wb, FormulaType.CELL, wb.getSheetIndex(sheet));
/* 347:    */   }
/* 348:    */   
/* 349:    */   static DVConstraint createDVConstraint(DVRecord dvRecord, FormulaRenderingWorkbook book)
/* 350:    */   {
/* 351:441 */     switch (dvRecord.getDataType())
/* 352:    */     {
/* 353:    */     case 0: 
/* 354:443 */       return new DVConstraint(0, dvRecord.getConditionOperator(), null, null, null, null, null);
/* 355:    */     case 1: 
/* 356:    */     case 2: 
/* 357:    */     case 4: 
/* 358:    */     case 5: 
/* 359:    */     case 6: 
/* 360:449 */       FormulaValuePair pair1 = toFormulaString(dvRecord.getFormula1(), book);
/* 361:450 */       FormulaValuePair pair2 = toFormulaString(dvRecord.getFormula2(), book);
/* 362:451 */       return new DVConstraint(dvRecord.getDataType(), dvRecord.getConditionOperator(), pair1.formula(), pair2.formula(), pair1.value(), pair2.value(), null);
/* 363:    */     case 3: 
/* 364:454 */       if (dvRecord.getListExplicitFormula())
/* 365:    */       {
/* 366:455 */         String values = toFormulaString(dvRecord.getFormula1(), book).string();
/* 367:456 */         if (values.startsWith("\"")) {
/* 368:457 */           values = values.substring(1);
/* 369:    */         }
/* 370:459 */         if (values.endsWith("\"")) {
/* 371:460 */           values = values.substring(0, values.length() - 1);
/* 372:    */         }
/* 373:462 */         String[] explicitListValues = values.split(Pattern.quote(""));
/* 374:463 */         return createExplicitListConstraint(explicitListValues);
/* 375:    */       }
/* 376:465 */       String listFormula = toFormulaString(dvRecord.getFormula1(), book).string();
/* 377:466 */       return createFormulaListConstraint(listFormula);
/* 378:    */     case 7: 
/* 379:469 */       return createCustomFormulaConstraint(toFormulaString(dvRecord.getFormula1(), book).string());
/* 380:    */     }
/* 381:471 */     throw new UnsupportedOperationException("validationType=" + dvRecord.getDataType());
/* 382:    */   }
/* 383:    */   
/* 384:    */   private static class FormulaValuePair
/* 385:    */   {
/* 386:    */     private String _formula;
/* 387:    */     private String _value;
/* 388:    */     
/* 389:    */     public String formula()
/* 390:    */     {
/* 391:480 */       return this._formula;
/* 392:    */     }
/* 393:    */     
/* 394:    */     public Double value()
/* 395:    */     {
/* 396:484 */       if (this._value == null) {
/* 397:485 */         return null;
/* 398:    */       }
/* 399:487 */       return new Double(this._value);
/* 400:    */     }
/* 401:    */     
/* 402:    */     public String string()
/* 403:    */     {
/* 404:491 */       if (this._formula != null) {
/* 405:492 */         return this._formula;
/* 406:    */       }
/* 407:494 */       if (this._value != null) {
/* 408:495 */         return this._value;
/* 409:    */       }
/* 410:497 */       return null;
/* 411:    */     }
/* 412:    */   }
/* 413:    */   
/* 414:    */   private static FormulaValuePair toFormulaString(Ptg[] ptgs, FormulaRenderingWorkbook book)
/* 415:    */   {
/* 416:502 */     FormulaValuePair pair = new FormulaValuePair(null);
/* 417:503 */     if ((ptgs != null) && (ptgs.length > 0))
/* 418:    */     {
/* 419:504 */       String string = FormulaRenderer.toFormulaString(book, ptgs);
/* 420:505 */       if ((ptgs.length == 1) && (ptgs[0].getClass() == NumberPtg.class)) {
/* 421:506 */         pair._value = string;
/* 422:    */       } else {
/* 423:508 */         pair._formula = string;
/* 424:    */       }
/* 425:    */     }
/* 426:511 */     return pair;
/* 427:    */   }
/* 428:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.usermodel.DVConstraint

 * JD-Core Version:    0.7.0.1

 */