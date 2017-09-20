/*   1:    */ package org.apache.poi.ss.formula.eval;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.TreeSet;
/*   6:    */ import org.apache.poi.ss.formula.atp.AnalysisToolPak;
/*   7:    */ import org.apache.poi.ss.formula.function.FunctionMetadata;
/*   8:    */ import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
/*   9:    */ import org.apache.poi.ss.formula.functions.Address;
/*  10:    */ import org.apache.poi.ss.formula.functions.AggregateFunction;
/*  11:    */ import org.apache.poi.ss.formula.functions.BooleanFunction;
/*  12:    */ import org.apache.poi.ss.formula.functions.CalendarFieldFunction;
/*  13:    */ import org.apache.poi.ss.formula.functions.Choose;
/*  14:    */ import org.apache.poi.ss.formula.functions.Code;
/*  15:    */ import org.apache.poi.ss.formula.functions.Column;
/*  16:    */ import org.apache.poi.ss.formula.functions.Columns;
/*  17:    */ import org.apache.poi.ss.formula.functions.Count;
/*  18:    */ import org.apache.poi.ss.formula.functions.Counta;
/*  19:    */ import org.apache.poi.ss.formula.functions.Countblank;
/*  20:    */ import org.apache.poi.ss.formula.functions.Countif;
/*  21:    */ import org.apache.poi.ss.formula.functions.DStarRunner;
/*  22:    */ import org.apache.poi.ss.formula.functions.DStarRunner.DStarAlgorithmEnum;
/*  23:    */ import org.apache.poi.ss.formula.functions.DateFunc;
/*  24:    */ import org.apache.poi.ss.formula.functions.Days360;
/*  25:    */ import org.apache.poi.ss.formula.functions.Errortype;
/*  26:    */ import org.apache.poi.ss.formula.functions.Even;
/*  27:    */ import org.apache.poi.ss.formula.functions.FinanceFunction;
/*  28:    */ import org.apache.poi.ss.formula.functions.Fixed;
/*  29:    */ import org.apache.poi.ss.formula.functions.Function;
/*  30:    */ import org.apache.poi.ss.formula.functions.Hlookup;
/*  31:    */ import org.apache.poi.ss.formula.functions.Hyperlink;
/*  32:    */ import org.apache.poi.ss.formula.functions.IPMT;
/*  33:    */ import org.apache.poi.ss.formula.functions.IfFunc;
/*  34:    */ import org.apache.poi.ss.formula.functions.Index;
/*  35:    */ import org.apache.poi.ss.formula.functions.Intercept;
/*  36:    */ import org.apache.poi.ss.formula.functions.Irr;
/*  37:    */ import org.apache.poi.ss.formula.functions.LogicalFunction;
/*  38:    */ import org.apache.poi.ss.formula.functions.Lookup;
/*  39:    */ import org.apache.poi.ss.formula.functions.Match;
/*  40:    */ import org.apache.poi.ss.formula.functions.MinaMaxa;
/*  41:    */ import org.apache.poi.ss.formula.functions.Mirr;
/*  42:    */ import org.apache.poi.ss.formula.functions.Mode;
/*  43:    */ import org.apache.poi.ss.formula.functions.Na;
/*  44:    */ import org.apache.poi.ss.formula.functions.NotImplementedFunction;
/*  45:    */ import org.apache.poi.ss.formula.functions.Now;
/*  46:    */ import org.apache.poi.ss.formula.functions.Npv;
/*  47:    */ import org.apache.poi.ss.formula.functions.NumericFunction;
/*  48:    */ import org.apache.poi.ss.formula.functions.Odd;
/*  49:    */ import org.apache.poi.ss.formula.functions.Offset;
/*  50:    */ import org.apache.poi.ss.formula.functions.PPMT;
/*  51:    */ import org.apache.poi.ss.formula.functions.Rank;
/*  52:    */ import org.apache.poi.ss.formula.functions.Rate;
/*  53:    */ import org.apache.poi.ss.formula.functions.Replace;
/*  54:    */ import org.apache.poi.ss.formula.functions.Rept;
/*  55:    */ import org.apache.poi.ss.formula.functions.Roman;
/*  56:    */ import org.apache.poi.ss.formula.functions.RowFunc;
/*  57:    */ import org.apache.poi.ss.formula.functions.Rows;
/*  58:    */ import org.apache.poi.ss.formula.functions.Slope;
/*  59:    */ import org.apache.poi.ss.formula.functions.Substitute;
/*  60:    */ import org.apache.poi.ss.formula.functions.Subtotal;
/*  61:    */ import org.apache.poi.ss.formula.functions.Sumif;
/*  62:    */ import org.apache.poi.ss.formula.functions.Sumproduct;
/*  63:    */ import org.apache.poi.ss.formula.functions.Sumx2my2;
/*  64:    */ import org.apache.poi.ss.formula.functions.Sumx2py2;
/*  65:    */ import org.apache.poi.ss.formula.functions.Sumxmy2;
/*  66:    */ import org.apache.poi.ss.formula.functions.T;
/*  67:    */ import org.apache.poi.ss.formula.functions.TextFunction;
/*  68:    */ import org.apache.poi.ss.formula.functions.TimeFunc;
/*  69:    */ import org.apache.poi.ss.formula.functions.Today;
/*  70:    */ import org.apache.poi.ss.formula.functions.Value;
/*  71:    */ import org.apache.poi.ss.formula.functions.Vlookup;
/*  72:    */ import org.apache.poi.ss.formula.functions.WeekdayFunc;
/*  73:    */ 
/*  74:    */ public final class FunctionEval
/*  75:    */ {
/*  76: 59 */   protected static final Function[] functions = ;
/*  77:    */   
/*  78:    */   private static Function[] produceFunctions()
/*  79:    */   {
/*  80: 66 */     Function[] retval = new Function[368];
/*  81:    */     
/*  82: 68 */     retval[0] = new Count();
/*  83: 69 */     retval[1] = new IfFunc();
/*  84: 70 */     retval[2] = LogicalFunction.ISNA;
/*  85: 71 */     retval[3] = LogicalFunction.ISERROR;
/*  86: 72 */     retval[4] = AggregateFunction.SUM;
/*  87: 73 */     retval[5] = AggregateFunction.AVERAGE;
/*  88: 74 */     retval[6] = AggregateFunction.MIN;
/*  89: 75 */     retval[7] = AggregateFunction.MAX;
/*  90: 76 */     retval[8] = new RowFunc();
/*  91: 77 */     retval[9] = new Column();
/*  92: 78 */     retval[10] = new Na();
/*  93: 79 */     retval[11] = new Npv();
/*  94: 80 */     retval[12] = AggregateFunction.STDEV;
/*  95: 81 */     retval[13] = NumericFunction.DOLLAR;
/*  96: 82 */     retval[14] = new Fixed();
/*  97: 83 */     retval[15] = NumericFunction.SIN;
/*  98: 84 */     retval[16] = NumericFunction.COS;
/*  99: 85 */     retval[17] = NumericFunction.TAN;
/* 100: 86 */     retval[18] = NumericFunction.ATAN;
/* 101: 87 */     retval[19] = NumericFunction.PI;
/* 102: 88 */     retval[20] = NumericFunction.SQRT;
/* 103: 89 */     retval[21] = NumericFunction.EXP;
/* 104: 90 */     retval[22] = NumericFunction.LN;
/* 105: 91 */     retval[23] = NumericFunction.LOG10;
/* 106: 92 */     retval[24] = NumericFunction.ABS;
/* 107: 93 */     retval[25] = NumericFunction.INT;
/* 108: 94 */     retval[26] = NumericFunction.SIGN;
/* 109: 95 */     retval[27] = NumericFunction.ROUND;
/* 110: 96 */     retval[28] = new Lookup();
/* 111: 97 */     retval[29] = new Index();
/* 112: 98 */     retval[30] = new Rept();
/* 113: 99 */     retval[31] = TextFunction.MID;
/* 114:100 */     retval[32] = TextFunction.LEN;
/* 115:101 */     retval[33] = new Value();
/* 116:102 */     retval[34] = BooleanFunction.TRUE;
/* 117:103 */     retval[35] = BooleanFunction.FALSE;
/* 118:104 */     retval[36] = BooleanFunction.AND;
/* 119:105 */     retval[37] = BooleanFunction.OR;
/* 120:106 */     retval[38] = BooleanFunction.NOT;
/* 121:107 */     retval[39] = NumericFunction.MOD;
/* 122:    */     
/* 123:    */ 
/* 124:    */ 
/* 125:111 */     retval[43] = new DStarRunner(DStarRunner.DStarAlgorithmEnum.DMIN);
/* 126:    */     
/* 127:    */ 
/* 128:114 */     retval[46] = AggregateFunction.VAR;
/* 129:    */     
/* 130:116 */     retval[48] = TextFunction.TEXT;
/* 131:    */     
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:122 */     retval[56] = FinanceFunction.PV;
/* 137:123 */     retval[57] = FinanceFunction.FV;
/* 138:124 */     retval[58] = FinanceFunction.NPER;
/* 139:125 */     retval[59] = FinanceFunction.PMT;
/* 140:126 */     retval[60] = new Rate();
/* 141:127 */     retval[61] = new Mirr();
/* 142:128 */     retval[62] = new Irr();
/* 143:129 */     retval[63] = NumericFunction.RAND;
/* 144:130 */     retval[64] = new Match();
/* 145:131 */     retval[65] = DateFunc.instance;
/* 146:132 */     retval[66] = new TimeFunc();
/* 147:133 */     retval[67] = CalendarFieldFunction.DAY;
/* 148:134 */     retval[68] = CalendarFieldFunction.MONTH;
/* 149:135 */     retval[69] = CalendarFieldFunction.YEAR;
/* 150:136 */     retval[70] = WeekdayFunc.instance;
/* 151:137 */     retval[71] = CalendarFieldFunction.HOUR;
/* 152:138 */     retval[72] = CalendarFieldFunction.MINUTE;
/* 153:139 */     retval[73] = CalendarFieldFunction.SECOND;
/* 154:140 */     retval[74] = new Now();
/* 155:    */     
/* 156:142 */     retval[76] = new Rows();
/* 157:143 */     retval[77] = new Columns();
/* 158:144 */     retval[78] = new Offset();
/* 159:    */     
/* 160:146 */     retval[82] = TextFunction.SEARCH;
/* 161:    */     
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:151 */     retval[97] = NumericFunction.ATAN2;
/* 166:152 */     retval[98] = NumericFunction.ASIN;
/* 167:153 */     retval[99] = NumericFunction.ACOS;
/* 168:154 */     retval[100] = new Choose();
/* 169:155 */     retval[101] = new Hlookup();
/* 170:156 */     retval[102] = new Vlookup();
/* 171:    */     
/* 172:158 */     retval[105] = LogicalFunction.ISREF;
/* 173:    */     
/* 174:160 */     retval[109] = NumericFunction.LOG;
/* 175:    */     
/* 176:162 */     retval[111] = TextFunction.CHAR;
/* 177:163 */     retval[112] = TextFunction.LOWER;
/* 178:164 */     retval[113] = TextFunction.UPPER;
/* 179:165 */     retval[114] = TextFunction.PROPER;
/* 180:166 */     retval[115] = TextFunction.LEFT;
/* 181:167 */     retval[116] = TextFunction.RIGHT;
/* 182:168 */     retval[117] = TextFunction.EXACT;
/* 183:169 */     retval[118] = TextFunction.TRIM;
/* 184:170 */     retval[119] = new Replace();
/* 185:171 */     retval[120] = new Substitute();
/* 186:172 */     retval[121] = new Code();
/* 187:    */     
/* 188:174 */     retval[124] = TextFunction.FIND;
/* 189:    */     
/* 190:176 */     retval[126] = LogicalFunction.ISERR;
/* 191:177 */     retval[127] = LogicalFunction.ISTEXT;
/* 192:178 */     retval[''] = LogicalFunction.ISNUMBER;
/* 193:179 */     retval[''] = LogicalFunction.ISBLANK;
/* 194:180 */     retval[''] = new T();
/* 195:    */     
/* 196:182 */     retval[''] = null;
/* 197:    */     
/* 198:184 */     retval['¢'] = TextFunction.CLEAN;
/* 199:    */     
/* 200:186 */     retval['§'] = new IPMT();
/* 201:187 */     retval['¨'] = new PPMT();
/* 202:188 */     retval['©'] = new Counta();
/* 203:    */     
/* 204:190 */     retval['·'] = AggregateFunction.PRODUCT;
/* 205:191 */     retval['¸'] = NumericFunction.FACT;
/* 206:    */     
/* 207:193 */     retval['¾'] = LogicalFunction.ISNONTEXT;
/* 208:    */     
/* 209:195 */     retval['Â'] = AggregateFunction.VARP;
/* 210:    */     
/* 211:197 */     retval['Å'] = NumericFunction.TRUNC;
/* 212:198 */     retval['Æ'] = LogicalFunction.ISLOGICAL;
/* 213:    */     
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:    */ 
/* 222:208 */     retval['Ô'] = NumericFunction.ROUNDUP;
/* 223:209 */     retval['Õ'] = NumericFunction.ROUNDDOWN;
/* 224:    */     
/* 225:    */ 
/* 226:212 */     retval['Ø'] = new Rank();
/* 227:213 */     retval['Û'] = new Address();
/* 228:214 */     retval['Ü'] = new Days360();
/* 229:215 */     retval['Ý'] = new Today();
/* 230:    */     
/* 231:    */ 
/* 232:218 */     retval['ã'] = AggregateFunction.MEDIAN;
/* 233:219 */     retval['ä'] = new Sumproduct();
/* 234:220 */     retval['å'] = NumericFunction.SINH;
/* 235:221 */     retval['æ'] = NumericFunction.COSH;
/* 236:222 */     retval['ç'] = NumericFunction.TANH;
/* 237:223 */     retval['è'] = NumericFunction.ASINH;
/* 238:224 */     retval['é'] = NumericFunction.ACOSH;
/* 239:225 */     retval['ê'] = NumericFunction.ATANH;
/* 240:226 */     retval['ë'] = new DStarRunner(DStarRunner.DStarAlgorithmEnum.DGET);
/* 241:    */     
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:    */ 
/* 246:232 */     retval['ÿ'] = null;
/* 247:    */     
/* 248:234 */     retval[261] = new Errortype();
/* 249:    */     
/* 250:236 */     retval[269] = AggregateFunction.AVEDEV;
/* 251:    */     
/* 252:    */ 
/* 253:    */ 
/* 254:    */ 
/* 255:    */ 
/* 256:    */ 
/* 257:243 */     retval[276] = NumericFunction.COMBIN;
/* 258:    */     
/* 259:    */ 
/* 260:246 */     retval[279] = new Even();
/* 261:    */     
/* 262:    */ 
/* 263:    */ 
/* 264:    */ 
/* 265:    */ 
/* 266:252 */     retval[285] = NumericFunction.FLOOR;
/* 267:    */     
/* 268:    */ 
/* 269:255 */     retval[288] = NumericFunction.CEILING;
/* 270:    */     
/* 271:    */ 
/* 272:    */ 
/* 273:    */ 
/* 274:    */ 
/* 275:    */ 
/* 276:    */ 
/* 277:    */ 
/* 278:    */ 
/* 279:265 */     retval[298] = new Odd();
/* 280:    */     
/* 281:267 */     retval[300] = NumericFunction.POISSON;
/* 282:    */     
/* 283:    */ 
/* 284:270 */     retval[303] = new Sumxmy2();
/* 285:271 */     retval[304] = new Sumx2my2();
/* 286:272 */     retval[305] = new Sumx2py2();
/* 287:    */     
/* 288:    */ 
/* 289:    */ 
/* 290:    */ 
/* 291:    */ 
/* 292:278 */     retval[311] = new Intercept();
/* 293:    */     
/* 294:    */ 
/* 295:    */ 
/* 296:282 */     retval[315] = new Slope();
/* 297:    */     
/* 298:    */ 
/* 299:285 */     retval[318] = AggregateFunction.DEVSQ;
/* 300:    */     
/* 301:    */ 
/* 302:288 */     retval[321] = AggregateFunction.SUMSQ;
/* 303:    */     
/* 304:    */ 
/* 305:    */ 
/* 306:292 */     retval[325] = AggregateFunction.LARGE;
/* 307:293 */     retval[326] = AggregateFunction.SMALL;
/* 308:    */     
/* 309:295 */     retval[328] = AggregateFunction.PERCENTILE;
/* 310:    */     
/* 311:297 */     retval[330] = new Mode();
/* 312:    */     
/* 313:    */ 
/* 314:    */ 
/* 315:301 */     retval[336] = TextFunction.CONCATENATE;
/* 316:302 */     retval[337] = NumericFunction.POWER;
/* 317:    */     
/* 318:304 */     retval[342] = NumericFunction.RADIANS;
/* 319:305 */     retval[343] = NumericFunction.DEGREES;
/* 320:306 */     retval[344] = new Subtotal();
/* 321:307 */     retval[345] = new Sumif();
/* 322:308 */     retval[346] = new Countif();
/* 323:309 */     retval[347] = new Countblank();
/* 324:    */     
/* 325:    */ 
/* 326:    */ 
/* 327:    */ 
/* 328:    */ 
/* 329:315 */     retval[354] = new Roman();
/* 330:    */     
/* 331:    */ 
/* 332:318 */     retval[359] = new Hyperlink();
/* 333:    */     
/* 334:    */ 
/* 335:321 */     retval[362] = MinaMaxa.MAXA;
/* 336:322 */     retval[363] = MinaMaxa.MINA;
/* 337:328 */     for (int i = 0; i < retval.length; i++)
/* 338:    */     {
/* 339:329 */       Function f = retval[i];
/* 340:330 */       if (f == null)
/* 341:    */       {
/* 342:331 */         FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(i);
/* 343:332 */         if (fm != null) {
/* 344:335 */           retval[i] = new NotImplementedFunction(fm.getName());
/* 345:    */         }
/* 346:    */       }
/* 347:    */     }
/* 348:338 */     return retval;
/* 349:    */   }
/* 350:    */   
/* 351:    */   public static Function getBasicFunction(int functionIndex)
/* 352:    */   {
/* 353:345 */     switch (functionIndex)
/* 354:    */     {
/* 355:    */     case 148: 
/* 356:    */     case 255: 
/* 357:348 */       return null;
/* 358:    */     }
/* 359:351 */     Function result = functions[functionIndex];
/* 360:352 */     if (result == null) {
/* 361:353 */       throw new NotImplementedException("FuncIx=" + functionIndex);
/* 362:    */     }
/* 363:355 */     return result;
/* 364:    */   }
/* 365:    */   
/* 366:    */   public static void registerFunction(String name, Function func)
/* 367:    */   {
/* 368:367 */     FunctionMetadata metaData = FunctionMetadataRegistry.getFunctionByName(name);
/* 369:368 */     if (metaData == null)
/* 370:    */     {
/* 371:369 */       if (AnalysisToolPak.isATPFunction(name)) {
/* 372:370 */         throw new IllegalArgumentException(name + " is a function from the Excel Analysis Toolpack. " + "Use AnalysisToolpack.registerFunction(String name, FreeRefFunction func) instead.");
/* 373:    */       }
/* 374:374 */       throw new IllegalArgumentException("Unknown function: " + name);
/* 375:    */     }
/* 376:377 */     int idx = metaData.getIndex();
/* 377:378 */     if ((functions[idx] instanceof NotImplementedFunction)) {
/* 378:379 */       functions[idx] = func;
/* 379:    */     } else {
/* 380:381 */       throw new IllegalArgumentException("POI already implememts " + name + ". You cannot override POI's implementations of Excel functions");
/* 381:    */     }
/* 382:    */   }
/* 383:    */   
/* 384:    */   public static Collection<String> getSupportedFunctionNames()
/* 385:    */   {
/* 386:393 */     Collection<String> lst = new TreeSet();
/* 387:394 */     for (int i = 0; i < functions.length; i++)
/* 388:    */     {
/* 389:395 */       Function func = functions[i];
/* 390:396 */       FunctionMetadata metaData = FunctionMetadataRegistry.getFunctionByIndex(i);
/* 391:397 */       if ((func != null) && (!(func instanceof NotImplementedFunction))) {
/* 392:398 */         lst.add(metaData.getName());
/* 393:    */       }
/* 394:    */     }
/* 395:401 */     lst.add("INDIRECT");
/* 396:402 */     return Collections.unmodifiableCollection(lst);
/* 397:    */   }
/* 398:    */   
/* 399:    */   public static Collection<String> getNotSupportedFunctionNames()
/* 400:    */   {
/* 401:412 */     Collection<String> lst = new TreeSet();
/* 402:413 */     for (int i = 0; i < functions.length; i++)
/* 403:    */     {
/* 404:414 */       Function func = functions[i];
/* 405:415 */       if ((func != null) && ((func instanceof NotImplementedFunction)))
/* 406:    */       {
/* 407:416 */         FunctionMetadata metaData = FunctionMetadataRegistry.getFunctionByIndex(i);
/* 408:417 */         lst.add(metaData.getName());
/* 409:    */       }
/* 410:    */     }
/* 411:420 */     lst.remove("INDIRECT");
/* 412:421 */     return Collections.unmodifiableCollection(lst);
/* 413:    */   }
/* 414:    */   
/* 415:    */   private static final class FunctionID
/* 416:    */   {
/* 417:    */     public static final int IF = 1;
/* 418:    */     public static final int SUM = 4;
/* 419:    */     public static final int OFFSET = 78;
/* 420:    */     public static final int CHOOSE = 100;
/* 421:    */     public static final int INDIRECT = 148;
/* 422:    */     public static final int EXTERNAL_FUNC = 255;
/* 423:    */   }
/* 424:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.FunctionEval
 * JD-Core Version:    0.7.0.1
 */