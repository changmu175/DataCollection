/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.NotImplementedException;
/*   8:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  10:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.StringValueEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  13:    */ import org.apache.poi.ss.util.NumberComparer;
/*  14:    */ 
/*  15:    */ public final class DStarRunner
/*  16:    */   implements Function3Arg
/*  17:    */ {
/*  18:    */   private final DStarAlgorithmEnum algoType;
/*  19:    */   
/*  20:    */   public static enum DStarAlgorithmEnum
/*  21:    */   {
/*  22: 43 */     DGET,  DMIN;
/*  23:    */     
/*  24:    */     private DStarAlgorithmEnum() {}
/*  25:    */   }
/*  26:    */   
/*  27:    */   public DStarRunner(DStarAlgorithmEnum algorithm)
/*  28:    */   {
/*  29: 50 */     this.algoType = algorithm;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  33:    */   {
/*  34: 54 */     if (args.length == 3) {
/*  35: 55 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
/*  36:    */     }
/*  37: 58 */     return ErrorEval.VALUE_INVALID;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval database, ValueEval filterColumn, ValueEval conditionDatabase)
/*  41:    */   {
/*  42: 65 */     if ((!(database instanceof AreaEval)) || (!(conditionDatabase instanceof AreaEval))) {
/*  43: 66 */       return ErrorEval.VALUE_INVALID;
/*  44:    */     }
/*  45: 68 */     AreaEval db = (AreaEval)database;
/*  46: 69 */     AreaEval cdb = (AreaEval)conditionDatabase;
/*  47:    */     try
/*  48:    */     {
/*  49: 72 */       filterColumn = OperandResolver.getSingleValue(filterColumn, srcRowIndex, srcColumnIndex);
/*  50:    */     }
/*  51:    */     catch (EvaluationException e)
/*  52:    */     {
/*  53: 74 */       return e.getErrorEval();
/*  54:    */     }
/*  55:    */     int fc;
/*  56:    */     try
/*  57:    */     {
/*  58: 79 */       fc = getColumnForName(filterColumn, db);
/*  59:    */     }
/*  60:    */     catch (EvaluationException e)
/*  61:    */     {
/*  62: 82 */       return ErrorEval.VALUE_INVALID;
/*  63:    */     }
/*  64: 84 */     if (fc == -1) {
/*  65: 85 */       return ErrorEval.VALUE_INVALID;
/*  66:    */     }
/*  67: 89 */     IDStarAlgorithm algorithm = null;
/*  68: 90 */     switch (this.algoType)
/*  69:    */     {
/*  70:    */     case DGET: 
/*  71: 91 */       algorithm = new DGet(); break;
/*  72:    */     case DMIN: 
/*  73: 92 */       algorithm = new DMin(); break;
/*  74:    */     default: 
/*  75: 94 */       throw new IllegalStateException("Unexpected algorithm type " + this.algoType + " encountered.");
/*  76:    */     }
/*  77: 98 */     int height = db.getHeight();
/*  78: 99 */     for (int row = 1; row < height; row++)
/*  79:    */     {
/*  80:100 */       boolean matches = true;
/*  81:    */       try
/*  82:    */       {
/*  83:102 */         matches = fullfillsConditions(db, row, cdb);
/*  84:    */       }
/*  85:    */       catch (EvaluationException e)
/*  86:    */       {
/*  87:105 */         return ErrorEval.VALUE_INVALID;
/*  88:    */       }
/*  89:108 */       if (matches)
/*  90:    */       {
/*  91:109 */         ValueEval currentValueEval = resolveReference(db, row, fc);
/*  92:    */         
/*  93:111 */         boolean shouldContinue = algorithm.processMatch(currentValueEval);
/*  94:112 */         if (!shouldContinue) {
/*  95:    */           break;
/*  96:    */         }
/*  97:    */       }
/*  98:    */     }
/*  99:119 */     return algorithm.getResult();
/* 100:    */   }
/* 101:    */   
/* 102:    */   private static enum operator
/* 103:    */   {
/* 104:123 */     largerThan,  largerEqualThan,  smallerThan,  smallerEqualThan,  equal;
/* 105:    */     
/* 106:    */     private operator() {}
/* 107:    */   }
/* 108:    */   
/* 109:    */   private static int getColumnForName(ValueEval nameValueEval, AreaEval db)
/* 110:    */     throws EvaluationException
/* 111:    */   {
/* 112:140 */     String name = OperandResolver.coerceValueToString(nameValueEval);
/* 113:141 */     return getColumnForString(db, name);
/* 114:    */   }
/* 115:    */   
/* 116:    */   private static int getColumnForString(AreaEval db, String name)
/* 117:    */     throws EvaluationException
/* 118:    */   {
/* 119:154 */     int resultColumn = -1;
/* 120:155 */     int width = db.getWidth();
/* 121:156 */     for (int column = 0; column < width; column++)
/* 122:    */     {
/* 123:157 */       ValueEval columnNameValueEval = resolveReference(db, 0, column);
/* 124:158 */       if (!(columnNameValueEval instanceof BlankEval)) {
/* 125:161 */         if (!(columnNameValueEval instanceof ErrorEval))
/* 126:    */         {
/* 127:164 */           String columnName = OperandResolver.coerceValueToString(columnNameValueEval);
/* 128:165 */           if (name.equals(columnName))
/* 129:    */           {
/* 130:166 */             resultColumn = column;
/* 131:167 */             break;
/* 132:    */           }
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:170 */     return resultColumn;
/* 137:    */   }
/* 138:    */   
/* 139:    */   private static boolean fullfillsConditions(AreaEval db, int row, AreaEval cdb)
/* 140:    */     throws EvaluationException
/* 141:    */   {
/* 142:188 */     int height = cdb.getHeight();
/* 143:189 */     for (int conditionRow = 1; conditionRow < height; conditionRow++)
/* 144:    */     {
/* 145:190 */       boolean matches = true;
/* 146:191 */       int width = cdb.getWidth();
/* 147:192 */       for (int column = 0; column < width; column++)
/* 148:    */       {
/* 149:195 */         boolean columnCondition = true;
/* 150:196 */         ValueEval condition = null;
/* 151:    */         
/* 152:    */ 
/* 153:199 */         condition = resolveReference(cdb, conditionRow, column);
/* 154:202 */         if (!(condition instanceof BlankEval))
/* 155:    */         {
/* 156:205 */           ValueEval targetHeader = resolveReference(cdb, 0, column);
/* 157:207 */           if (!(targetHeader instanceof StringValueEval)) {
/* 158:208 */             throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 159:    */           }
/* 160:211 */           if (getColumnForName(targetHeader, db) == -1) {
/* 161:213 */             columnCondition = false;
/* 162:    */           }
/* 163:215 */           if (columnCondition == true)
/* 164:    */           {
/* 165:217 */             ValueEval value = resolveReference(db, row, getColumnForName(targetHeader, db));
/* 166:218 */             if (!testNormalCondition(value, condition))
/* 167:    */             {
/* 168:219 */               matches = false;
/* 169:220 */               break;
/* 170:    */             }
/* 171:    */           }
/* 172:    */           else
/* 173:    */           {
/* 174:224 */             if (OperandResolver.coerceValueToString(condition).isEmpty()) {
/* 175:225 */               throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 176:    */             }
/* 177:227 */             throw new NotImplementedException("D* function with formula conditions");
/* 178:    */           }
/* 179:    */         }
/* 180:    */       }
/* 181:231 */       if (matches == true) {
/* 182:232 */         return true;
/* 183:    */       }
/* 184:    */     }
/* 185:235 */     return false;
/* 186:    */   }
/* 187:    */   
/* 188:    */   private static boolean testNormalCondition(ValueEval value, ValueEval condition)
/* 189:    */     throws EvaluationException
/* 190:    */   {
/* 191:248 */     if ((condition instanceof StringEval))
/* 192:    */     {
/* 193:249 */       String conditionString = ((StringEval)condition).getStringValue();
/* 194:251 */       if (conditionString.startsWith("<"))
/* 195:    */       {
/* 196:252 */         String number = conditionString.substring(1);
/* 197:253 */         if (number.startsWith("="))
/* 198:    */         {
/* 199:254 */           number = number.substring(1);
/* 200:255 */           return testNumericCondition(value, operator.smallerEqualThan, number);
/* 201:    */         }
/* 202:257 */         return testNumericCondition(value, operator.smallerThan, number);
/* 203:    */       }
/* 204:260 */       if (conditionString.startsWith(">"))
/* 205:    */       {
/* 206:261 */         String number = conditionString.substring(1);
/* 207:262 */         if (number.startsWith("="))
/* 208:    */         {
/* 209:263 */           number = number.substring(1);
/* 210:264 */           return testNumericCondition(value, operator.largerEqualThan, number);
/* 211:    */         }
/* 212:266 */         return testNumericCondition(value, operator.largerThan, number);
/* 213:    */       }
/* 214:269 */       if (conditionString.startsWith("="))
/* 215:    */       {
/* 216:270 */         String stringOrNumber = conditionString.substring(1);
/* 217:272 */         if (stringOrNumber.isEmpty()) {
/* 218:273 */           return value instanceof BlankEval;
/* 219:    */         }
/* 220:276 */         boolean itsANumber = false;
/* 221:    */         try
/* 222:    */         {
/* 223:278 */           Integer.parseInt(stringOrNumber);
/* 224:279 */           itsANumber = true;
/* 225:    */         }
/* 226:    */         catch (NumberFormatException e)
/* 227:    */         {
/* 228:    */           try
/* 229:    */           {
/* 230:282 */             Double.parseDouble(stringOrNumber);
/* 231:283 */             itsANumber = true;
/* 232:    */           }
/* 233:    */           catch (NumberFormatException e2)
/* 234:    */           {
/* 235:285 */             itsANumber = false;
/* 236:    */           }
/* 237:    */         }
/* 238:288 */         if (itsANumber) {
/* 239:289 */           return testNumericCondition(value, operator.equal, stringOrNumber);
/* 240:    */         }
/* 241:291 */         String valueString = (value instanceof BlankEval) ? "" : OperandResolver.coerceValueToString(value);
/* 242:292 */         return stringOrNumber.equals(valueString);
/* 243:    */       }
/* 244:295 */       if (conditionString.isEmpty()) {
/* 245:296 */         return value instanceof StringEval;
/* 246:    */       }
/* 247:299 */       String valueString = (value instanceof BlankEval) ? "" : OperandResolver.coerceValueToString(value);
/* 248:300 */       return valueString.startsWith(conditionString);
/* 249:    */     }
/* 250:304 */     if ((condition instanceof NumericValueEval))
/* 251:    */     {
/* 252:305 */       double conditionNumber = ((NumericValueEval)condition).getNumberValue();
/* 253:306 */       Double valueNumber = getNumberFromValueEval(value);
/* 254:307 */       if (valueNumber == null) {
/* 255:308 */         return false;
/* 256:    */       }
/* 257:311 */       return conditionNumber == valueNumber.doubleValue();
/* 258:    */     }
/* 259:313 */     if ((condition instanceof ErrorEval))
/* 260:    */     {
/* 261:314 */       if ((value instanceof ErrorEval)) {
/* 262:315 */         return ((ErrorEval)condition).getErrorCode() == ((ErrorEval)value).getErrorCode();
/* 263:    */       }
/* 264:318 */       return false;
/* 265:    */     }
/* 266:322 */     return false;
/* 267:    */   }
/* 268:    */   
/* 269:    */   private static boolean testNumericCondition(ValueEval valueEval, operator op, String condition)
/* 270:    */     throws EvaluationException
/* 271:    */   {
/* 272:338 */     if (!(valueEval instanceof NumericValueEval)) {
/* 273:339 */       return false;
/* 274:    */     }
/* 275:340 */     double value = ((NumericValueEval)valueEval).getNumberValue();
/* 276:    */     
/* 277:    */ 
/* 278:343 */     double conditionValue = 0.0D;
/* 279:    */     try
/* 280:    */     {
/* 281:345 */       int intValue = Integer.parseInt(condition);
/* 282:346 */       conditionValue = intValue;
/* 283:    */     }
/* 284:    */     catch (NumberFormatException e)
/* 285:    */     {
/* 286:    */       try
/* 287:    */       {
/* 288:349 */         conditionValue = Double.parseDouble(condition);
/* 289:    */       }
/* 290:    */       catch (NumberFormatException e2)
/* 291:    */       {
/* 292:351 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 293:    */       }
/* 294:    */     }
/* 295:355 */     int result = NumberComparer.compare(value, conditionValue);
/* 296:356 */     switch (1.$SwitchMap$org$apache$poi$ss$formula$functions$DStarRunner$operator[op.ordinal()])
/* 297:    */     {
/* 298:    */     case 1: 
/* 299:358 */       return result > 0;
/* 300:    */     case 2: 
/* 301:360 */       return result >= 0;
/* 302:    */     case 3: 
/* 303:362 */       return result < 0;
/* 304:    */     case 4: 
/* 305:364 */       return result <= 0;
/* 306:    */     case 5: 
/* 307:366 */       return result == 0;
/* 308:    */     }
/* 309:368 */     return false;
/* 310:    */   }
/* 311:    */   
/* 312:    */   private static Double getNumberFromValueEval(ValueEval value)
/* 313:    */   {
/* 314:372 */     if ((value instanceof NumericValueEval)) {
/* 315:373 */       return Double.valueOf(((NumericValueEval)value).getNumberValue());
/* 316:    */     }
/* 317:375 */     if ((value instanceof StringValueEval))
/* 318:    */     {
/* 319:376 */       String stringValue = ((StringValueEval)value).getStringValue();
/* 320:    */       try
/* 321:    */       {
/* 322:378 */         return Double.valueOf(Double.parseDouble(stringValue));
/* 323:    */       }
/* 324:    */       catch (NumberFormatException e2)
/* 325:    */       {
/* 326:380 */         return null;
/* 327:    */       }
/* 328:    */     }
/* 329:384 */     return null;
/* 330:    */   }
/* 331:    */   
/* 332:    */   private static ValueEval resolveReference(AreaEval db, int dbRow, int dbCol)
/* 333:    */   {
/* 334:    */     try
/* 335:    */     {
/* 336:398 */       return OperandResolver.getSingleValue(db.getValue(dbRow, dbCol), db.getFirstRow() + dbRow, db.getFirstColumn() + dbCol);
/* 337:    */     }
/* 338:    */     catch (EvaluationException e)
/* 339:    */     {
/* 340:400 */       return e.getErrorEval();
/* 341:    */     }
/* 342:    */   }
/* 343:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.DStarRunner
 * JD-Core Version:    0.7.0.1
 */