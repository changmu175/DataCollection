/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ExternalNameEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.FunctionNameEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*   9:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*  10:    */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*  11:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  12:    */ import org.apache.poi.ss.formula.ptg.NameXPxg;
/*  13:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  14:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*  15:    */ import org.apache.poi.ss.formula.ptg.Ref3DPxg;
/*  16:    */ import org.apache.poi.ss.util.CellReference;
/*  17:    */ import org.apache.poi.ss.util.CellReference.NameType;
/*  18:    */ 
/*  19:    */ public final class OperationEvaluationContext
/*  20:    */ {
/*  21: 49 */   public static final FreeRefFunction UDF = UserDefinedFunction.instance;
/*  22:    */   private final EvaluationWorkbook _workbook;
/*  23:    */   private final int _sheetIndex;
/*  24:    */   private final int _rowIndex;
/*  25:    */   private final int _columnIndex;
/*  26:    */   private final EvaluationTracker _tracker;
/*  27:    */   private final WorkbookEvaluator _bookEvaluator;
/*  28:    */   private final boolean _isSingleValue;
/*  29:    */   
/*  30:    */   public OperationEvaluationContext(WorkbookEvaluator bookEvaluator, EvaluationWorkbook workbook, int sheetIndex, int srcRowNum, int srcColNum, EvaluationTracker tracker)
/*  31:    */   {
/*  32: 60 */     this(bookEvaluator, workbook, sheetIndex, srcRowNum, srcColNum, tracker, true);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public OperationEvaluationContext(WorkbookEvaluator bookEvaluator, EvaluationWorkbook workbook, int sheetIndex, int srcRowNum, int srcColNum, EvaluationTracker tracker, boolean isSingleValue)
/*  36:    */   {
/*  37: 65 */     this._bookEvaluator = bookEvaluator;
/*  38: 66 */     this._workbook = workbook;
/*  39: 67 */     this._sheetIndex = sheetIndex;
/*  40: 68 */     this._rowIndex = srcRowNum;
/*  41: 69 */     this._columnIndex = srcColNum;
/*  42: 70 */     this._tracker = tracker;
/*  43: 71 */     this._isSingleValue = isSingleValue;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public EvaluationWorkbook getWorkbook()
/*  47:    */   {
/*  48: 75 */     return this._workbook;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getRowIndex()
/*  52:    */   {
/*  53: 79 */     return this._rowIndex;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getColumnIndex()
/*  57:    */   {
/*  58: 83 */     return this._columnIndex;
/*  59:    */   }
/*  60:    */   
/*  61:    */   SheetRangeEvaluator createExternSheetRefEvaluator(ExternSheetReferenceToken ptg)
/*  62:    */   {
/*  63: 87 */     return createExternSheetRefEvaluator(ptg.getExternSheetIndex());
/*  64:    */   }
/*  65:    */   
/*  66:    */   SheetRangeEvaluator createExternSheetRefEvaluator(String firstSheetName, String lastSheetName, int externalWorkbookNumber)
/*  67:    */   {
/*  68: 90 */     EvaluationWorkbook.ExternalSheet externalSheet = this._workbook.getExternalSheet(firstSheetName, lastSheetName, externalWorkbookNumber);
/*  69: 91 */     return createExternSheetRefEvaluator(externalSheet);
/*  70:    */   }
/*  71:    */   
/*  72:    */   SheetRangeEvaluator createExternSheetRefEvaluator(int externSheetIndex)
/*  73:    */   {
/*  74: 94 */     EvaluationWorkbook.ExternalSheet externalSheet = this._workbook.getExternalSheet(externSheetIndex);
/*  75: 95 */     return createExternSheetRefEvaluator(externalSheet);
/*  76:    */   }
/*  77:    */   
/*  78:    */   SheetRangeEvaluator createExternSheetRefEvaluator(EvaluationWorkbook.ExternalSheet externalSheet)
/*  79:    */   {
/*  80:100 */     int otherLastSheetIndex = -1;
/*  81:    */     WorkbookEvaluator targetEvaluator;
/*  82:    */     int otherFirstSheetIndex;
/*  83:101 */     if ((externalSheet == null) || (externalSheet.getWorkbookName() == null))
/*  84:    */     {
/*  85:103 */       WorkbookEvaluator targetEvaluator = this._bookEvaluator;
/*  86:    */       int otherFirstSheetIndex;
/*  87:    */       int otherFirstSheetIndex;
/*  88:104 */       if (externalSheet == null) {
/*  89:105 */         otherFirstSheetIndex = 0;
/*  90:    */       } else {
/*  91:107 */         otherFirstSheetIndex = this._workbook.getSheetIndex(externalSheet.getSheetName());
/*  92:    */       }
/*  93:110 */       if ((externalSheet instanceof EvaluationWorkbook.ExternalSheetRange))
/*  94:    */       {
/*  95:111 */         String lastSheetName = ((EvaluationWorkbook.ExternalSheetRange)externalSheet).getLastSheetName();
/*  96:112 */         otherLastSheetIndex = this._workbook.getSheetIndex(lastSheetName);
/*  97:    */       }
/*  98:    */     }
/*  99:    */     else
/* 100:    */     {
/* 101:116 */       String workbookName = externalSheet.getWorkbookName();
/* 102:    */       try
/* 103:    */       {
/* 104:118 */         targetEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
/* 105:    */       }
/* 106:    */       catch (CollaboratingWorkbooksEnvironment.WorkbookNotFoundException e)
/* 107:    */       {
/* 108:120 */         throw new RuntimeException(e.getMessage(), e);
/* 109:    */       }
/* 110:123 */       otherFirstSheetIndex = targetEvaluator.getSheetIndex(externalSheet.getSheetName());
/* 111:124 */       if ((externalSheet instanceof EvaluationWorkbook.ExternalSheetRange))
/* 112:    */       {
/* 113:125 */         String lastSheetName = ((EvaluationWorkbook.ExternalSheetRange)externalSheet).getLastSheetName();
/* 114:126 */         otherLastSheetIndex = targetEvaluator.getSheetIndex(lastSheetName);
/* 115:    */       }
/* 116:129 */       if (otherFirstSheetIndex < 0) {
/* 117:130 */         throw new RuntimeException("Invalid sheet name '" + externalSheet.getSheetName() + "' in bool '" + workbookName + "'.");
/* 118:    */       }
/* 119:    */     }
/* 120:135 */     if (otherLastSheetIndex == -1) {
/* 121:137 */       otherLastSheetIndex = otherFirstSheetIndex;
/* 122:    */     }
/* 123:140 */     SheetRefEvaluator[] evals = new SheetRefEvaluator[otherLastSheetIndex - otherFirstSheetIndex + 1];
/* 124:141 */     for (int i = 0; i < evals.length; i++)
/* 125:    */     {
/* 126:142 */       int otherSheetIndex = i + otherFirstSheetIndex;
/* 127:143 */       evals[i] = new SheetRefEvaluator(targetEvaluator, this._tracker, otherSheetIndex);
/* 128:    */     }
/* 129:145 */     return new SheetRangeEvaluator(otherFirstSheetIndex, otherLastSheetIndex, evals);
/* 130:    */   }
/* 131:    */   
/* 132:    */   private SheetRefEvaluator createExternSheetRefEvaluator(String workbookName, String sheetName)
/* 133:    */   {
/* 134:    */     WorkbookEvaluator targetEvaluator;
/* 135:    */     WorkbookEvaluator targetEvaluator;
/* 136:153 */     if (workbookName == null)
/* 137:    */     {
/* 138:154 */       targetEvaluator = this._bookEvaluator;
/* 139:    */     }
/* 140:    */     else
/* 141:    */     {
/* 142:156 */       if (sheetName == null) {
/* 143:157 */         throw new IllegalArgumentException("sheetName must not be null if workbookName is provided");
/* 144:    */       }
/* 145:    */       try
/* 146:    */       {
/* 147:160 */         targetEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
/* 148:    */       }
/* 149:    */       catch (CollaboratingWorkbooksEnvironment.WorkbookNotFoundException e)
/* 150:    */       {
/* 151:162 */         return null;
/* 152:    */       }
/* 153:    */     }
/* 154:165 */     int otherSheetIndex = sheetName == null ? this._sheetIndex : targetEvaluator.getSheetIndex(sheetName);
/* 155:166 */     if (otherSheetIndex < 0) {
/* 156:167 */       return null;
/* 157:    */     }
/* 158:169 */     return new SheetRefEvaluator(targetEvaluator, this._tracker, otherSheetIndex);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public SheetRangeEvaluator getRefEvaluatorForCurrentSheet()
/* 162:    */   {
/* 163:173 */     SheetRefEvaluator sre = new SheetRefEvaluator(this._bookEvaluator, this._tracker, this._sheetIndex);
/* 164:174 */     return new SheetRangeEvaluator(this._sheetIndex, sre);
/* 165:    */   }
/* 166:    */   
/* 167:    */   public ValueEval getDynamicReference(String workbookName, String sheetName, String refStrPart1, String refStrPart2, boolean isA1Style)
/* 168:    */   {
/* 169:198 */     if (!isA1Style) {
/* 170:199 */       throw new RuntimeException("R1C1 style not supported yet");
/* 171:    */     }
/* 172:201 */     SheetRefEvaluator se = createExternSheetRefEvaluator(workbookName, sheetName);
/* 173:202 */     if (se == null) {
/* 174:203 */       return ErrorEval.REF_INVALID;
/* 175:    */     }
/* 176:205 */     SheetRangeEvaluator sre = new SheetRangeEvaluator(this._sheetIndex, se);
/* 177:    */     
/* 178:    */ 
/* 179:208 */     SpreadsheetVersion ssVersion = ((FormulaParsingWorkbook)this._workbook).getSpreadsheetVersion();
/* 180:    */     
/* 181:210 */     NameType part1refType = classifyCellReference(refStrPart1, ssVersion);
/* 182:211 */     switch (1.$SwitchMap$org$apache$poi$ss$util$CellReference$NameType[part1refType.ordinal()])
/* 183:    */     {
/* 184:    */     case 1: 
/* 185:213 */       return ErrorEval.REF_INVALID;
/* 186:    */     case 2: 
/* 187:215 */       EvaluationName nm = ((FormulaParsingWorkbook)this._workbook).getName(refStrPart1, this._sheetIndex);
/* 188:216 */       if (!nm.isRange()) {
/* 189:217 */         throw new RuntimeException("Specified name '" + refStrPart1 + "' is not a range as expected.");
/* 190:    */       }
/* 191:219 */       return this._bookEvaluator.evaluateNameFormula(nm.getNameDefinition(), this);
/* 192:    */     }
/* 193:221 */     if (refStrPart2 == null)
/* 194:    */     {
/* 195:223 */       switch (1.$SwitchMap$org$apache$poi$ss$util$CellReference$NameType[part1refType.ordinal()])
/* 196:    */       {
/* 197:    */       case 3: 
/* 198:    */       case 4: 
/* 199:226 */         return ErrorEval.REF_INVALID;
/* 200:    */       case 5: 
/* 201:228 */         CellReference cr = new CellReference(refStrPart1);
/* 202:229 */         return new LazyRefEval(cr.getRow(), cr.getCol(), sre);
/* 203:    */       }
/* 204:231 */       throw new IllegalStateException("Unexpected reference classification of '" + refStrPart1 + "'.");
/* 205:    */     }
/* 206:233 */     NameType part2refType = classifyCellReference(refStrPart1, ssVersion);
/* 207:234 */     switch (1.$SwitchMap$org$apache$poi$ss$util$CellReference$NameType[part2refType.ordinal()])
/* 208:    */     {
/* 209:    */     case 1: 
/* 210:236 */       return ErrorEval.REF_INVALID;
/* 211:    */     case 2: 
/* 212:238 */       throw new RuntimeException("Cannot evaluate '" + refStrPart1 + "'. Indirect evaluation of defined names not supported yet");
/* 213:    */     }
/* 214:242 */     if (part2refType != part1refType) {
/* 215:244 */       return ErrorEval.REF_INVALID;
/* 216:    */     }
/* 217:    */     int firstRow;
/* 218:    */     int lastRow;
/* 219:    */     int firstCol;
/* 220:    */     int lastCol;
/* 221:    */     int lastCol;
/* 222:    */     int firstRow;
/* 223:    */     int lastRow;
/* 224:247 */     switch (1.$SwitchMap$org$apache$poi$ss$util$CellReference$NameType[part1refType.ordinal()])
/* 225:    */     {
/* 226:    */     case 3: 
/* 227:249 */       firstRow = 0;
/* 228:    */       int lastCol;
/* 229:250 */       if (part2refType.equals(NameType.COLUMN))
/* 230:    */       {
/* 231:252 */         int lastRow = ssVersion.getLastRowIndex();
/* 232:253 */         int firstCol = parseRowRef(refStrPart1);
/* 233:254 */         lastCol = parseRowRef(refStrPart2);
/* 234:    */       }
/* 235:    */       else
/* 236:    */       {
/* 237:257 */         lastRow = ssVersion.getLastRowIndex();
/* 238:258 */         firstCol = parseColRef(refStrPart1);
/* 239:259 */         lastCol = parseColRef(refStrPart2);
/* 240:    */       }
/* 241:261 */       break;
/* 242:    */     case 4: 
/* 243:264 */       firstCol = 0;
/* 244:265 */       if (part2refType.equals(NameType.ROW))
/* 245:    */       {
/* 246:267 */         firstRow = parseColRef(refStrPart1);
/* 247:268 */         lastRow = parseColRef(refStrPart2);
/* 248:269 */         lastCol = ssVersion.getLastColumnIndex();
/* 249:    */       }
/* 250:    */       else
/* 251:    */       {
/* 252:271 */         lastCol = ssVersion.getLastColumnIndex();
/* 253:272 */         firstRow = parseRowRef(refStrPart1);
/* 254:273 */         lastRow = parseRowRef(refStrPart2);
/* 255:    */       }
/* 256:275 */       break;
/* 257:    */     case 5: 
/* 258:278 */       CellReference cr = new CellReference(refStrPart1);
/* 259:279 */       firstRow = cr.getRow();
/* 260:280 */       firstCol = cr.getCol();
/* 261:281 */       cr = new CellReference(refStrPart2);
/* 262:282 */       lastRow = cr.getRow();
/* 263:283 */       lastCol = cr.getCol();
/* 264:284 */       break;
/* 265:    */     default: 
/* 266:286 */       throw new IllegalStateException("Unexpected reference classification of '" + refStrPart1 + "'.");
/* 267:    */     }
/* 268:288 */     return new LazyAreaEval(firstRow, firstCol, lastRow, lastCol, sre);
/* 269:    */   }
/* 270:    */   
/* 271:    */   private static int parseRowRef(String refStrPart)
/* 272:    */   {
/* 273:292 */     return CellReference.convertColStringToIndex(refStrPart);
/* 274:    */   }
/* 275:    */   
/* 276:    */   private static int parseColRef(String refStrPart)
/* 277:    */   {
/* 278:296 */     return Integer.parseInt(refStrPart) - 1;
/* 279:    */   }
/* 280:    */   
/* 281:    */   private static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion)
/* 282:    */   {
/* 283:300 */     int len = str.length();
/* 284:301 */     if (len < 1) {
/* 285:302 */       return NameType.BAD_CELL_OR_NAMED_RANGE;
/* 286:    */     }
/* 287:304 */     return CellReference.classifyCellReference(str, ssVersion);
/* 288:    */   }
/* 289:    */   
/* 290:    */   public FreeRefFunction findUserDefinedFunction(String functionName)
/* 291:    */   {
/* 292:308 */     return this._bookEvaluator.findUserDefinedFunction(functionName);
/* 293:    */   }
/* 294:    */   
/* 295:    */   public ValueEval getRefEval(int rowIndex, int columnIndex)
/* 296:    */   {
/* 297:312 */     SheetRangeEvaluator sre = getRefEvaluatorForCurrentSheet();
/* 298:313 */     return new LazyRefEval(rowIndex, columnIndex, sre);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public ValueEval getRef3DEval(Ref3DPtg rptg)
/* 302:    */   {
/* 303:316 */     SheetRangeEvaluator sre = createExternSheetRefEvaluator(rptg.getExternSheetIndex());
/* 304:317 */     return new LazyRefEval(rptg.getRow(), rptg.getColumn(), sre);
/* 305:    */   }
/* 306:    */   
/* 307:    */   public ValueEval getRef3DEval(Ref3DPxg rptg)
/* 308:    */   {
/* 309:320 */     SheetRangeEvaluator sre = createExternSheetRefEvaluator(rptg.getSheetName(), rptg.getLastSheetName(), rptg.getExternalWorkbookNumber());
/* 310:    */     
/* 311:322 */     return new LazyRefEval(rptg.getRow(), rptg.getColumn(), sre);
/* 312:    */   }
/* 313:    */   
/* 314:    */   public ValueEval getAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex)
/* 315:    */   {
/* 316:327 */     SheetRangeEvaluator sre = getRefEvaluatorForCurrentSheet();
/* 317:328 */     return new LazyAreaEval(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex, sre);
/* 318:    */   }
/* 319:    */   
/* 320:    */   public ValueEval getArea3DEval(Area3DPtg aptg)
/* 321:    */   {
/* 322:331 */     SheetRangeEvaluator sre = createExternSheetRefEvaluator(aptg.getExternSheetIndex());
/* 323:332 */     return new LazyAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn(), sre);
/* 324:    */   }
/* 325:    */   
/* 326:    */   public ValueEval getArea3DEval(Area3DPxg aptg)
/* 327:    */   {
/* 328:336 */     SheetRangeEvaluator sre = createExternSheetRefEvaluator(aptg.getSheetName(), aptg.getLastSheetName(), aptg.getExternalWorkbookNumber());
/* 329:    */     
/* 330:338 */     return new LazyAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn(), sre);
/* 331:    */   }
/* 332:    */   
/* 333:    */   public ValueEval getNameXEval(NameXPtg nameXPtg)
/* 334:    */   {
/* 335:344 */     EvaluationWorkbook.ExternalSheet externSheet = this._workbook.getExternalSheet(nameXPtg.getSheetRefIndex());
/* 336:345 */     if ((externSheet == null) || (externSheet.getWorkbookName() == null)) {
/* 337:347 */       return getLocalNameXEval(nameXPtg);
/* 338:    */     }
/* 339:351 */     String workbookName = externSheet.getWorkbookName();
/* 340:352 */     EvaluationWorkbook.ExternalName externName = this._workbook.getExternalName(nameXPtg.getSheetRefIndex(), nameXPtg.getNameIndex());
/* 341:    */     
/* 342:    */ 
/* 343:    */ 
/* 344:356 */     return getExternalNameXEval(externName, workbookName);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public ValueEval getNameXEval(NameXPxg nameXPxg)
/* 348:    */   {
/* 349:359 */     EvaluationWorkbook.ExternalSheet externSheet = this._workbook.getExternalSheet(nameXPxg.getSheetName(), null, nameXPxg.getExternalWorkbookNumber());
/* 350:360 */     if ((externSheet == null) || (externSheet.getWorkbookName() == null)) {
/* 351:362 */       return getLocalNameXEval(nameXPxg);
/* 352:    */     }
/* 353:366 */     String workbookName = externSheet.getWorkbookName();
/* 354:367 */     EvaluationWorkbook.ExternalName externName = this._workbook.getExternalName(nameXPxg.getNameName(), nameXPxg.getSheetName(), nameXPxg.getExternalWorkbookNumber());
/* 355:    */     
/* 356:    */ 
/* 357:    */ 
/* 358:    */ 
/* 359:372 */     return getExternalNameXEval(externName, workbookName);
/* 360:    */   }
/* 361:    */   
/* 362:    */   private ValueEval getLocalNameXEval(NameXPxg nameXPxg)
/* 363:    */   {
/* 364:377 */     int sIdx = -1;
/* 365:378 */     if (nameXPxg.getSheetName() != null) {
/* 366:379 */       sIdx = this._workbook.getSheetIndex(nameXPxg.getSheetName());
/* 367:    */     }
/* 368:383 */     String name = nameXPxg.getNameName();
/* 369:384 */     EvaluationName evalName = this._workbook.getName(name, sIdx);
/* 370:385 */     if (evalName != null) {
/* 371:387 */       return new ExternalNameEval(evalName);
/* 372:    */     }
/* 373:390 */     return new FunctionNameEval(name);
/* 374:    */   }
/* 375:    */   
/* 376:    */   private ValueEval getLocalNameXEval(NameXPtg nameXPtg)
/* 377:    */   {
/* 378:394 */     String name = this._workbook.resolveNameXText(nameXPtg);
/* 379:    */     
/* 380:    */ 
/* 381:397 */     int sheetNameAt = name.indexOf('!');
/* 382:398 */     EvaluationName evalName = null;
/* 383:399 */     if (sheetNameAt > -1)
/* 384:    */     {
/* 385:401 */       String sheetName = name.substring(0, sheetNameAt);
/* 386:402 */       String nameName = name.substring(sheetNameAt + 1);
/* 387:403 */       evalName = this._workbook.getName(nameName, this._workbook.getSheetIndex(sheetName));
/* 388:    */     }
/* 389:    */     else
/* 390:    */     {
/* 391:406 */       evalName = this._workbook.getName(name, -1);
/* 392:    */     }
/* 393:409 */     if (evalName != null) {
/* 394:411 */       return new ExternalNameEval(evalName);
/* 395:    */     }
/* 396:414 */     return new FunctionNameEval(name);
/* 397:    */   }
/* 398:    */   
/* 399:    */   public int getSheetIndex()
/* 400:    */   {
/* 401:418 */     return this._sheetIndex;
/* 402:    */   }
/* 403:    */   
/* 404:    */   public boolean isSingleValue()
/* 405:    */   {
/* 406:426 */     return this._isSingleValue;
/* 407:    */   }
/* 408:    */   
/* 409:    */   private ValueEval getExternalNameXEval(EvaluationWorkbook.ExternalName externName, String workbookName)
/* 410:    */   {
/* 411:    */     try
/* 412:    */     {
/* 413:432 */       WorkbookEvaluator refWorkbookEvaluator = this._bookEvaluator.getOtherWorkbookEvaluator(workbookName);
/* 414:433 */       EvaluationName evaluationName = refWorkbookEvaluator.getName(externName.getName(), externName.getIx() - 1);
/* 415:434 */       if ((evaluationName != null) && (evaluationName.hasFormula()))
/* 416:    */       {
/* 417:435 */         if (evaluationName.getNameDefinition().length > 1) {
/* 418:436 */           throw new RuntimeException("Complex name formulas not supported yet");
/* 419:    */         }
/* 420:440 */         OperationEvaluationContext refWorkbookContext = new OperationEvaluationContext(refWorkbookEvaluator, refWorkbookEvaluator.getWorkbook(), -1, -1, -1, this._tracker);
/* 421:    */         
/* 422:    */ 
/* 423:443 */         Ptg ptg = evaluationName.getNameDefinition()[0];
/* 424:444 */         if ((ptg instanceof Ref3DPtg))
/* 425:    */         {
/* 426:445 */           Ref3DPtg ref3D = (Ref3DPtg)ptg;
/* 427:446 */           return refWorkbookContext.getRef3DEval(ref3D);
/* 428:    */         }
/* 429:447 */         if ((ptg instanceof Ref3DPxg))
/* 430:    */         {
/* 431:448 */           Ref3DPxg ref3D = (Ref3DPxg)ptg;
/* 432:449 */           return refWorkbookContext.getRef3DEval(ref3D);
/* 433:    */         }
/* 434:450 */         if ((ptg instanceof Area3DPtg))
/* 435:    */         {
/* 436:451 */           Area3DPtg area3D = (Area3DPtg)ptg;
/* 437:452 */           return refWorkbookContext.getArea3DEval(area3D);
/* 438:    */         }
/* 439:453 */         if ((ptg instanceof Area3DPxg))
/* 440:    */         {
/* 441:454 */           Area3DPxg area3D = (Area3DPxg)ptg;
/* 442:455 */           return refWorkbookContext.getArea3DEval(area3D);
/* 443:    */         }
/* 444:    */       }
/* 445:458 */       return ErrorEval.REF_INVALID;
/* 446:    */     }
/* 447:    */     catch (CollaboratingWorkbooksEnvironment.WorkbookNotFoundException wnfe) {}
/* 448:460 */     return ErrorEval.REF_INVALID;
/* 449:    */   }
/* 450:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.OperationEvaluationContext

 * JD-Core Version:    0.7.0.1

 */