/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.IdentityHashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Stack;
/*   9:    */ import java.util.TreeSet;
/*  10:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  11:    */ import org.apache.poi.ss.formula.atp.AnalysisToolPak;
/*  12:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  16:    */ import org.apache.poi.ss.formula.eval.ExternalNameEval;
/*  17:    */ import org.apache.poi.ss.formula.eval.FunctionEval;
/*  18:    */ import org.apache.poi.ss.formula.eval.FunctionNameEval;
/*  19:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*  20:    */ import org.apache.poi.ss.formula.eval.NotImplementedException;
/*  21:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  22:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  23:    */ import org.apache.poi.ss.formula.eval.RefListEval;
/*  24:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  25:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  26:    */ import org.apache.poi.ss.formula.functions.Choose;
/*  27:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  28:    */ import org.apache.poi.ss.formula.functions.Function;
/*  29:    */ import org.apache.poi.ss.formula.functions.IfFunc;
/*  30:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*  31:    */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*  32:    */ import org.apache.poi.ss.formula.ptg.AreaErrPtg;
/*  33:    */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*  34:    */ import org.apache.poi.ss.formula.ptg.AttrPtg;
/*  35:    */ import org.apache.poi.ss.formula.ptg.BoolPtg;
/*  36:    */ import org.apache.poi.ss.formula.ptg.ControlPtg;
/*  37:    */ import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
/*  38:    */ import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
/*  39:    */ import org.apache.poi.ss.formula.ptg.ErrPtg;
/*  40:    */ import org.apache.poi.ss.formula.ptg.ExpPtg;
/*  41:    */ import org.apache.poi.ss.formula.ptg.FuncVarPtg;
/*  42:    */ import org.apache.poi.ss.formula.ptg.IntPtg;
/*  43:    */ import org.apache.poi.ss.formula.ptg.MemAreaPtg;
/*  44:    */ import org.apache.poi.ss.formula.ptg.MemErrPtg;
/*  45:    */ import org.apache.poi.ss.formula.ptg.MemFuncPtg;
/*  46:    */ import org.apache.poi.ss.formula.ptg.MissingArgPtg;
/*  47:    */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*  48:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  49:    */ import org.apache.poi.ss.formula.ptg.NameXPxg;
/*  50:    */ import org.apache.poi.ss.formula.ptg.NumberPtg;
/*  51:    */ import org.apache.poi.ss.formula.ptg.OperationPtg;
/*  52:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  53:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*  54:    */ import org.apache.poi.ss.formula.ptg.Ref3DPxg;
/*  55:    */ import org.apache.poi.ss.formula.ptg.RefErrorPtg;
/*  56:    */ import org.apache.poi.ss.formula.ptg.RefPtg;
/*  57:    */ import org.apache.poi.ss.formula.ptg.RefPtgBase;
/*  58:    */ import org.apache.poi.ss.formula.ptg.StringPtg;
/*  59:    */ import org.apache.poi.ss.formula.ptg.UnionPtg;
/*  60:    */ import org.apache.poi.ss.formula.ptg.UnknownPtg;
/*  61:    */ import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
/*  62:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  63:    */ import org.apache.poi.ss.usermodel.CellType;
/*  64:    */ import org.apache.poi.ss.util.CellRangeAddressBase;
/*  65:    */ import org.apache.poi.ss.util.CellReference;
/*  66:    */ import org.apache.poi.util.Internal;
/*  67:    */ import org.apache.poi.util.POILogFactory;
/*  68:    */ import org.apache.poi.util.POILogger;
/*  69:    */ 
/*  70:    */ @Internal
/*  71:    */ public final class WorkbookEvaluator
/*  72:    */ {
/*  73: 61 */   private static final POILogger LOG = POILogFactory.getLogger(WorkbookEvaluator.class);
/*  74:    */   private final EvaluationWorkbook _workbook;
/*  75:    */   private EvaluationCache _cache;
/*  76:    */   private int _workbookIx;
/*  77:    */   private final IEvaluationListener _evaluationListener;
/*  78:    */   private final Map<EvaluationSheet, Integer> _sheetIndexesBySheet;
/*  79:    */   private final Map<String, Integer> _sheetIndexesByName;
/*  80:    */   private CollaboratingWorkbooksEnvironment _collaboratingWorkbookEnvironment;
/*  81:    */   private final IStabilityClassifier _stabilityClassifier;
/*  82:    */   private final AggregatingUDFFinder _udfFinder;
/*  83:    */   private boolean _ignoreMissingWorkbooks;
/*  84:    */   private boolean dbgEvaluationOutputForNextEval;
/*  85: 83 */   private final POILogger EVAL_LOG = POILogFactory.getLogger("POI.FormulaEval");
/*  86: 85 */   private int dbgEvaluationOutputIndent = -1;
/*  87:    */   
/*  88:    */   public WorkbookEvaluator(EvaluationWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  89:    */   {
/*  90: 91 */     this(workbook, null, stabilityClassifier, udfFinder);
/*  91:    */   }
/*  92:    */   
/*  93:    */   WorkbookEvaluator(EvaluationWorkbook workbook, IEvaluationListener evaluationListener, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder)
/*  94:    */   {
/*  95: 95 */     this._workbook = workbook;
/*  96: 96 */     this._evaluationListener = evaluationListener;
/*  97: 97 */     this._cache = new EvaluationCache(evaluationListener);
/*  98: 98 */     this._sheetIndexesBySheet = new IdentityHashMap();
/*  99: 99 */     this._sheetIndexesByName = new IdentityHashMap();
/* 100:100 */     this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
/* 101:101 */     this._workbookIx = 0;
/* 102:102 */     this._stabilityClassifier = stabilityClassifier;
/* 103:    */     
/* 104:104 */     AggregatingUDFFinder defaultToolkit = workbook == null ? null : (AggregatingUDFFinder)workbook.getUDFFinder();
/* 105:106 */     if ((defaultToolkit != null) && (udfFinder != null)) {
/* 106:107 */       defaultToolkit.add(udfFinder);
/* 107:    */     }
/* 108:109 */     this._udfFinder = defaultToolkit;
/* 109:    */   }
/* 110:    */   
/* 111:    */   String getSheetName(int sheetIndex)
/* 112:    */   {
/* 113:116 */     return this._workbook.getSheetName(sheetIndex);
/* 114:    */   }
/* 115:    */   
/* 116:    */   EvaluationSheet getSheet(int sheetIndex)
/* 117:    */   {
/* 118:120 */     return this._workbook.getSheet(sheetIndex);
/* 119:    */   }
/* 120:    */   
/* 121:    */   EvaluationWorkbook getWorkbook()
/* 122:    */   {
/* 123:124 */     return this._workbook;
/* 124:    */   }
/* 125:    */   
/* 126:    */   EvaluationName getName(String name, int sheetIndex)
/* 127:    */   {
/* 128:128 */     EvaluationName evalName = this._workbook.getName(name, sheetIndex);
/* 129:129 */     return evalName;
/* 130:    */   }
/* 131:    */   
/* 132:    */   private static boolean isDebugLogEnabled()
/* 133:    */   {
/* 134:133 */     return LOG.check(1);
/* 135:    */   }
/* 136:    */   
/* 137:    */   private static boolean isInfoLogEnabled()
/* 138:    */   {
/* 139:136 */     return LOG.check(3);
/* 140:    */   }
/* 141:    */   
/* 142:    */   private static void logDebug(String s)
/* 143:    */   {
/* 144:139 */     if (isDebugLogEnabled()) {
/* 145:140 */       LOG.log(1, new Object[] { s });
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   private static void logInfo(String s)
/* 150:    */   {
/* 151:144 */     if (isInfoLogEnabled()) {
/* 152:145 */       LOG.log(3, new Object[] { s });
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   void attachToEnvironment(CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment, EvaluationCache cache, int workbookIx)
/* 157:    */   {
/* 158:149 */     this._collaboratingWorkbookEnvironment = collaboratingWorkbooksEnvironment;
/* 159:150 */     this._cache = cache;
/* 160:151 */     this._workbookIx = workbookIx;
/* 161:    */   }
/* 162:    */   
/* 163:    */   CollaboratingWorkbooksEnvironment getEnvironment()
/* 164:    */   {
/* 165:154 */     return this._collaboratingWorkbookEnvironment;
/* 166:    */   }
/* 167:    */   
/* 168:    */   void detachFromEnvironment()
/* 169:    */   {
/* 170:162 */     this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
/* 171:163 */     this._cache = new EvaluationCache(this._evaluationListener);
/* 172:164 */     this._workbookIx = 0;
/* 173:    */   }
/* 174:    */   
/* 175:    */   WorkbookEvaluator getOtherWorkbookEvaluator(String workbookName)
/* 176:    */     throws CollaboratingWorkbooksEnvironment.WorkbookNotFoundException
/* 177:    */   {
/* 178:170 */     return this._collaboratingWorkbookEnvironment.getWorkbookEvaluator(workbookName);
/* 179:    */   }
/* 180:    */   
/* 181:    */   IEvaluationListener getEvaluationListener()
/* 182:    */   {
/* 183:174 */     return this._evaluationListener;
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void clearAllCachedResultValues()
/* 187:    */   {
/* 188:183 */     this._cache.clear();
/* 189:184 */     this._sheetIndexesBySheet.clear();
/* 190:185 */     this._workbook.clearAllCachedResultValues();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void notifyUpdateCell(EvaluationCell cell)
/* 194:    */   {
/* 195:193 */     int sheetIndex = getSheetIndex(cell.getSheet());
/* 196:194 */     this._cache.notifyUpdateCell(this._workbookIx, sheetIndex, cell);
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void notifyDeleteCell(EvaluationCell cell)
/* 200:    */   {
/* 201:201 */     int sheetIndex = getSheetIndex(cell.getSheet());
/* 202:202 */     this._cache.notifyDeleteCell(this._workbookIx, sheetIndex, cell);
/* 203:    */   }
/* 204:    */   
/* 205:    */   private int getSheetIndex(EvaluationSheet sheet)
/* 206:    */   {
/* 207:206 */     Integer result = (Integer)this._sheetIndexesBySheet.get(sheet);
/* 208:207 */     if (result == null)
/* 209:    */     {
/* 210:208 */       int sheetIndex = this._workbook.getSheetIndex(sheet);
/* 211:209 */       if (sheetIndex < 0) {
/* 212:210 */         throw new RuntimeException("Specified sheet from a different book");
/* 213:    */       }
/* 214:212 */       result = Integer.valueOf(sheetIndex);
/* 215:213 */       this._sheetIndexesBySheet.put(sheet, result);
/* 216:    */     }
/* 217:215 */     return result.intValue();
/* 218:    */   }
/* 219:    */   
/* 220:    */   public ValueEval evaluate(EvaluationCell srcCell)
/* 221:    */   {
/* 222:219 */     int sheetIndex = getSheetIndex(srcCell.getSheet());
/* 223:220 */     return evaluateAny(srcCell, sheetIndex, srcCell.getRowIndex(), srcCell.getColumnIndex(), new EvaluationTracker(this._cache));
/* 224:    */   }
/* 225:    */   
/* 226:    */   int getSheetIndex(String sheetName)
/* 227:    */   {
/* 228:228 */     Integer result = (Integer)this._sheetIndexesByName.get(sheetName);
/* 229:229 */     if (result == null)
/* 230:    */     {
/* 231:230 */       int sheetIndex = this._workbook.getSheetIndex(sheetName);
/* 232:231 */       if (sheetIndex < 0) {
/* 233:232 */         return -1;
/* 234:    */       }
/* 235:234 */       result = Integer.valueOf(sheetIndex);
/* 236:235 */       this._sheetIndexesByName.put(sheetName, result);
/* 237:    */     }
/* 238:237 */     return result.intValue();
/* 239:    */   }
/* 240:    */   
/* 241:    */   int getSheetIndexByExternIndex(int externSheetIndex)
/* 242:    */   {
/* 243:241 */     return this._workbook.convertFromExternSheetIndex(externSheetIndex);
/* 244:    */   }
/* 245:    */   
/* 246:    */   private ValueEval evaluateAny(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker)
/* 247:    */   {
/* 248:252 */     boolean shouldCellDependencyBeRecorded = this._stabilityClassifier == null;
/* 249:254 */     if ((srcCell == null) || (srcCell.getCellTypeEnum() != CellType.FORMULA))
/* 250:    */     {
/* 251:255 */       ValueEval result = getValueFromNonFormulaCell(srcCell);
/* 252:256 */       if (shouldCellDependencyBeRecorded) {
/* 253:257 */         tracker.acceptPlainValueDependency(this._workbookIx, sheetIndex, rowIndex, columnIndex, result);
/* 254:    */       }
/* 255:259 */       return result;
/* 256:    */     }
/* 257:262 */     FormulaCellCacheEntry cce = this._cache.getOrCreateFormulaCellEntry(srcCell);
/* 258:263 */     if ((shouldCellDependencyBeRecorded) || (cce.isInputSensitive())) {
/* 259:264 */       tracker.acceptFormulaDependency(cce);
/* 260:    */     }
/* 261:266 */     IEvaluationListener evalListener = this._evaluationListener;
/* 262:268 */     if (cce.getValue() == null)
/* 263:    */     {
/* 264:269 */       if (!tracker.startEvaluate(cce)) {
/* 265:270 */         return ErrorEval.CIRCULAR_REF_ERROR;
/* 266:    */       }
/* 267:272 */       OperationEvaluationContext ec = new OperationEvaluationContext(this, this._workbook, sheetIndex, rowIndex, columnIndex, tracker);
/* 268:    */       try
/* 269:    */       {
/* 270:276 */         Ptg[] ptgs = this._workbook.getFormulaTokens(srcCell);
/* 271:    */         ValueEval result;
/* 272:277 */         if (evalListener == null)
/* 273:    */         {
/* 274:278 */           result = evaluateFormula(ec, ptgs);
/* 275:    */         }
/* 276:    */         else
/* 277:    */         {
/* 278:280 */           evalListener.onStartEvaluate(srcCell, cce);
/* 279:281 */           result = evaluateFormula(ec, ptgs);
/* 280:282 */           evalListener.onEndEvaluate(cce, result);
/* 281:    */         }
/* 282:285 */         tracker.updateCacheResult(result);
/* 283:    */       }
/* 284:    */       catch (NotImplementedException e)
/* 285:    */       {
/* 286:288 */         throw addExceptionInfo(e, sheetIndex, rowIndex, columnIndex);
/* 287:    */       }
/* 288:    */       catch (RuntimeException re)
/* 289:    */       {
/* 290:    */         ValueEval result;
/* 291:290 */         if (((re.getCause() instanceof CollaboratingWorkbooksEnvironment.WorkbookNotFoundException)) && (this._ignoreMissingWorkbooks)) {
/* 292:291 */           logInfo(re.getCause().getMessage() + " - Continuing with cached value!");
/* 293:    */         }
/* 294:292 */         switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[srcCell.getCachedFormulaResultTypeEnum().ordinal()])
/* 295:    */         {
/* 296:    */         case 1: 
/* 297:294 */           result = new NumberEval(srcCell.getNumericCellValue());
/* 298:295 */           break;
/* 299:    */         case 2: 
/* 300:297 */           result = new StringEval(srcCell.getStringCellValue());
/* 301:298 */           break;
/* 302:    */         case 3: 
/* 303:300 */           result = BlankEval.instance;
/* 304:301 */           break;
/* 305:    */         case 4: 
/* 306:303 */           result = BoolEval.valueOf(srcCell.getBooleanCellValue());
/* 307:304 */           break;
/* 308:    */         case 5: 
/* 309:306 */           result = ErrorEval.valueOf(srcCell.getErrorCellValue());
/* 310:307 */           break;
/* 311:    */         case 6: 
/* 312:    */         default: 
/* 313:310 */           throw new RuntimeException("Unexpected cell type '" + srcCell.getCellTypeEnum() + "' found!");
/* 314:    */           
/* 315:    */ 
/* 316:313 */           throw re;
/* 317:    */         }
/* 318:    */       }
/* 319:    */       finally
/* 320:    */       {
/* 321:316 */         tracker.endEvaluate(cce);
/* 322:    */       }
/* 323:    */     }
/* 324:    */     else
/* 325:    */     {
/* 326:319 */       if (evalListener != null) {
/* 327:320 */         evalListener.onCacheHit(sheetIndex, rowIndex, columnIndex, cce.getValue());
/* 328:    */       }
/* 329:322 */       return cce.getValue();
/* 330:    */     }
/* 331:    */     ValueEval result;
/* 332:324 */     if (isDebugLogEnabled())
/* 333:    */     {
/* 334:325 */       String sheetName = getSheetName(sheetIndex);
/* 335:326 */       CellReference cr = new CellReference(rowIndex, columnIndex);
/* 336:327 */       logDebug("Evaluated " + sheetName + "!" + cr.formatAsString() + " to " + result);
/* 337:    */     }
/* 338:333 */     return result;
/* 339:    */   }
/* 340:    */   
/* 341:    */   private NotImplementedException addExceptionInfo(NotImplementedException inner, int sheetIndex, int rowIndex, int columnIndex)
/* 342:    */   {
/* 343:    */     try
/* 344:    */     {
/* 345:344 */       String sheetName = this._workbook.getSheetName(sheetIndex);
/* 346:345 */       CellReference cr = new CellReference(sheetName, rowIndex, columnIndex, false, false);
/* 347:346 */       String msg = "Error evaluating cell " + cr.formatAsString();
/* 348:347 */       return new NotImplementedException(msg, inner);
/* 349:    */     }
/* 350:    */     catch (Exception e)
/* 351:    */     {
/* 352:350 */       LOG.log(7, new Object[] { "Can't add exception info", e });
/* 353:    */     }
/* 354:351 */     return inner;
/* 355:    */   }
/* 356:    */   
/* 357:    */   static ValueEval getValueFromNonFormulaCell(EvaluationCell cell)
/* 358:    */   {
/* 359:360 */     if (cell == null) {
/* 360:361 */       return BlankEval.instance;
/* 361:    */     }
/* 362:363 */     CellType cellType = cell.getCellTypeEnum();
/* 363:364 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/* 364:    */     {
/* 365:    */     case 1: 
/* 366:366 */       return new NumberEval(cell.getNumericCellValue());
/* 367:    */     case 2: 
/* 368:368 */       return new StringEval(cell.getStringCellValue());
/* 369:    */     case 4: 
/* 370:370 */       return BoolEval.valueOf(cell.getBooleanCellValue());
/* 371:    */     case 3: 
/* 372:372 */       return BlankEval.instance;
/* 373:    */     case 5: 
/* 374:374 */       return ErrorEval.valueOf(cell.getErrorCellValue());
/* 375:    */     }
/* 376:376 */     throw new RuntimeException("Unexpected cell type (" + cellType + ")");
/* 377:    */   }
/* 378:    */   
/* 379:    */   @Internal
/* 380:    */   ValueEval evaluateFormula(OperationEvaluationContext ec, Ptg[] ptgs)
/* 381:    */   {
/* 382:386 */     String dbgIndentStr = "";
/* 383:387 */     if (this.dbgEvaluationOutputForNextEval)
/* 384:    */     {
/* 385:389 */       this.dbgEvaluationOutputIndent = 1;
/* 386:390 */       this.dbgEvaluationOutputForNextEval = false;
/* 387:    */     }
/* 388:392 */     if (this.dbgEvaluationOutputIndent > 0)
/* 389:    */     {
/* 390:395 */       dbgIndentStr = "                                                                                                    ";
/* 391:396 */       dbgIndentStr = dbgIndentStr.substring(0, Math.min(dbgIndentStr.length(), this.dbgEvaluationOutputIndent * 2));
/* 392:397 */       this.EVAL_LOG.log(5, new Object[] { dbgIndentStr + "- evaluateFormula('" + ec.getRefEvaluatorForCurrentSheet().getSheetNameRange() + "'/" + new CellReference(ec.getRowIndex(), ec.getColumnIndex()).formatAsString() + "): " + Arrays.toString(ptgs).replaceAll("\\Qorg.apache.poi.ss.formula.ptg.\\E", "") });
/* 393:    */       
/* 394:    */ 
/* 395:    */ 
/* 396:401 */       this.dbgEvaluationOutputIndent += 1;
/* 397:    */     }
/* 398:404 */     Stack<ValueEval> stack = new Stack();
/* 399:405 */     int i = 0;
/* 400:405 */     for (int iSize = ptgs.length; i < iSize; i++)
/* 401:    */     {
/* 402:407 */       Ptg ptg = ptgs[i];
/* 403:408 */       if (this.dbgEvaluationOutputIndent > 0) {
/* 404:409 */         this.EVAL_LOG.log(3, new Object[] { dbgIndentStr + "  * ptg " + i + ": " + ptg + ", stack: " + stack });
/* 405:    */       }
/* 406:411 */       if ((ptg instanceof AttrPtg))
/* 407:    */       {
/* 408:412 */         AttrPtg attrPtg = (AttrPtg)ptg;
/* 409:413 */         if (attrPtg.isSum()) {
/* 410:416 */           ptg = FuncVarPtg.SUM;
/* 411:    */         }
/* 412:418 */         if (attrPtg.isOptimizedChoose())
/* 413:    */         {
/* 414:419 */           ValueEval arg0 = (ValueEval)stack.pop();
/* 415:420 */           int[] jumpTable = attrPtg.getJumpTable();
/* 416:    */           
/* 417:422 */           int nChoices = jumpTable.length;
/* 418:    */           int dist;
/* 419:    */           try
/* 420:    */           {
/* 421:424 */             int switchIndex = Choose.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
/* 422:    */             int dist;
/* 423:425 */             if ((switchIndex < 1) || (switchIndex > nChoices))
/* 424:    */             {
/* 425:426 */               stack.push(ErrorEval.VALUE_INVALID);
/* 426:427 */               dist = attrPtg.getChooseFuncOffset() + 4;
/* 427:    */             }
/* 428:    */             else
/* 429:    */             {
/* 430:429 */               dist = jumpTable[(switchIndex - 1)];
/* 431:    */             }
/* 432:    */           }
/* 433:    */           catch (EvaluationException e)
/* 434:    */           {
/* 435:432 */             stack.push(e.getErrorEval());
/* 436:433 */             dist = attrPtg.getChooseFuncOffset() + 4;
/* 437:    */           }
/* 438:437 */           dist -= nChoices * 2 + 2;
/* 439:438 */           i += countTokensToBeSkipped(ptgs, i, dist);
/* 440:439 */           continue;
/* 441:    */         }
/* 442:441 */         if (attrPtg.isOptimizedIf())
/* 443:    */         {
/* 444:442 */           ValueEval arg0 = (ValueEval)stack.pop();
/* 445:    */           boolean evaluatedPredicate;
/* 446:    */           try
/* 447:    */           {
/* 448:445 */             evaluatedPredicate = IfFunc.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
/* 449:    */           }
/* 450:    */           catch (EvaluationException e)
/* 451:    */           {
/* 452:447 */             stack.push(e.getErrorEval());
/* 453:448 */             int dist = attrPtg.getData();
/* 454:449 */             i += countTokensToBeSkipped(ptgs, i, dist);
/* 455:450 */             attrPtg = (AttrPtg)ptgs[i];
/* 456:451 */             dist = attrPtg.getData() + 1;
/* 457:452 */             i += countTokensToBeSkipped(ptgs, i, dist);
/* 458:453 */             continue;
/* 459:    */           }
/* 460:455 */           if (evaluatedPredicate) {
/* 461:    */             continue;
/* 462:    */           }
/* 463:458 */           int dist = attrPtg.getData();
/* 464:459 */           i += countTokensToBeSkipped(ptgs, i, dist);
/* 465:460 */           Ptg nextPtg = ptgs[(i + 1)];
/* 466:461 */           if (((ptgs[i] instanceof AttrPtg)) && ((nextPtg instanceof FuncVarPtg)) && (((FuncVarPtg)nextPtg).getFunctionIndex() == 1))
/* 467:    */           {
/* 468:466 */             i++;
/* 469:467 */             stack.push(BoolEval.FALSE);
/* 470:    */           }
/* 471:470 */           continue;
/* 472:    */         }
/* 473:472 */         if (attrPtg.isSkip())
/* 474:    */         {
/* 475:473 */           int dist = attrPtg.getData() + 1;
/* 476:474 */           i += countTokensToBeSkipped(ptgs, i, dist);
/* 477:475 */           if (stack.peek() != MissingArgEval.instance) {
/* 478:    */             continue;
/* 479:    */           }
/* 480:476 */           stack.pop();
/* 481:477 */           stack.push(BlankEval.instance); continue;
/* 482:    */         }
/* 483:    */       }
/* 484:482 */       if (!(ptg instanceof ControlPtg)) {
/* 485:486 */         if ((!(ptg instanceof MemFuncPtg)) && (!(ptg instanceof MemAreaPtg))) {
/* 486:490 */           if (!(ptg instanceof MemErrPtg)) {
/* 487:494 */             if ((ptg instanceof UnionPtg))
/* 488:    */             {
/* 489:495 */               ValueEval v2 = (ValueEval)stack.pop();
/* 490:496 */               ValueEval v1 = (ValueEval)stack.pop();
/* 491:497 */               stack.push(new RefListEval(v1, v2));
/* 492:    */             }
/* 493:    */             else
/* 494:    */             {
/* 495:    */               ValueEval opResult;
/* 496:    */               ValueEval opResult;
/* 497:502 */               if ((ptg instanceof OperationPtg))
/* 498:    */               {
/* 499:503 */                 OperationPtg optg = (OperationPtg)ptg;
/* 500:    */                 
/* 501:505 */                 int numops = optg.getNumberOfOperands();
/* 502:506 */                 ValueEval[] ops = new ValueEval[numops];
/* 503:509 */                 for (int j = numops - 1; j >= 0; j--)
/* 504:    */                 {
/* 505:510 */                   ValueEval p = (ValueEval)stack.pop();
/* 506:511 */                   ops[j] = p;
/* 507:    */                 }
/* 508:514 */                 opResult = OperationEvaluatorFactory.evaluate(optg, ops, ec);
/* 509:    */               }
/* 510:    */               else
/* 511:    */               {
/* 512:516 */                 opResult = getEvalForPtg(ptg, ec);
/* 513:    */               }
/* 514:518 */               if (opResult == null) {
/* 515:519 */                 throw new RuntimeException("Evaluation result must not be null");
/* 516:    */               }
/* 517:522 */               stack.push(opResult);
/* 518:523 */               if (this.dbgEvaluationOutputIndent > 0) {
/* 519:524 */                 this.EVAL_LOG.log(3, new Object[] { dbgIndentStr + "    = " + opResult });
/* 520:    */               }
/* 521:    */             }
/* 522:    */           }
/* 523:    */         }
/* 524:    */       }
/* 525:    */     }
/* 526:528 */     ValueEval value = (ValueEval)stack.pop();
/* 527:529 */     if (!stack.isEmpty()) {
/* 528:530 */       throw new IllegalStateException("evaluation stack not empty");
/* 529:    */     }
/* 530:    */     ValueEval result;
/* 531:    */     ValueEval result;
/* 532:535 */     if (ec.isSingleValue()) {
/* 533:536 */       result = dereferenceResult(value, ec.getRowIndex(), ec.getColumnIndex());
/* 534:    */     } else {
/* 535:538 */       result = value;
/* 536:    */     }
/* 537:541 */     if (this.dbgEvaluationOutputIndent > 0)
/* 538:    */     {
/* 539:542 */       this.EVAL_LOG.log(3, new Object[] { dbgIndentStr + "finshed eval of " + new CellReference(ec.getRowIndex(), ec.getColumnIndex()).formatAsString() + ": " + result });
/* 540:    */       
/* 541:    */ 
/* 542:545 */       this.dbgEvaluationOutputIndent -= 1;
/* 543:546 */       if (this.dbgEvaluationOutputIndent == 1) {
/* 544:548 */         this.dbgEvaluationOutputIndent = -1;
/* 545:    */       }
/* 546:    */     }
/* 547:551 */     return result;
/* 548:    */   }
/* 549:    */   
/* 550:    */   private static int countTokensToBeSkipped(Ptg[] ptgs, int startIndex, int distInBytes)
/* 551:    */   {
/* 552:562 */     int remBytes = distInBytes;
/* 553:563 */     int index = startIndex;
/* 554:564 */     while (remBytes != 0)
/* 555:    */     {
/* 556:565 */       index++;
/* 557:566 */       remBytes -= ptgs[index].getSize();
/* 558:567 */       if (remBytes < 0) {
/* 559:568 */         throw new RuntimeException("Bad skip distance (wrong token size calculation).");
/* 560:    */       }
/* 561:570 */       if (index >= ptgs.length) {
/* 562:571 */         throw new RuntimeException("Skip distance too far (ran out of formula tokens).");
/* 563:    */       }
/* 564:    */     }
/* 565:574 */     return index - startIndex;
/* 566:    */   }
/* 567:    */   
/* 568:    */   public static ValueEval dereferenceResult(ValueEval evaluationResult, int srcRowNum, int srcColNum)
/* 569:    */   {
/* 570:    */     ValueEval value;
/* 571:    */     try
/* 572:    */     {
/* 573:589 */       value = OperandResolver.getSingleValue(evaluationResult, srcRowNum, srcColNum);
/* 574:    */     }
/* 575:    */     catch (EvaluationException e)
/* 576:    */     {
/* 577:591 */       return e.getErrorEval();
/* 578:    */     }
/* 579:593 */     if (value == BlankEval.instance) {
/* 580:595 */       return NumberEval.ZERO;
/* 581:    */     }
/* 582:599 */     return value;
/* 583:    */   }
/* 584:    */   
/* 585:    */   private ValueEval getEvalForPtg(Ptg ptg, OperationEvaluationContext ec)
/* 586:    */   {
/* 587:612 */     if ((ptg instanceof NamePtg))
/* 588:    */     {
/* 589:614 */       NamePtg namePtg = (NamePtg)ptg;
/* 590:615 */       EvaluationName nameRecord = this._workbook.getName(namePtg);
/* 591:616 */       return getEvalForNameRecord(nameRecord, ec);
/* 592:    */     }
/* 593:618 */     if ((ptg instanceof NameXPtg)) {
/* 594:620 */       return processNameEval(ec.getNameXEval((NameXPtg)ptg), ec);
/* 595:    */     }
/* 596:622 */     if ((ptg instanceof NameXPxg)) {
/* 597:624 */       return processNameEval(ec.getNameXEval((NameXPxg)ptg), ec);
/* 598:    */     }
/* 599:627 */     if ((ptg instanceof IntPtg)) {
/* 600:628 */       return new NumberEval(((IntPtg)ptg).getValue());
/* 601:    */     }
/* 602:630 */     if ((ptg instanceof NumberPtg)) {
/* 603:631 */       return new NumberEval(((NumberPtg)ptg).getValue());
/* 604:    */     }
/* 605:633 */     if ((ptg instanceof StringPtg)) {
/* 606:634 */       return new StringEval(((StringPtg)ptg).getValue());
/* 607:    */     }
/* 608:636 */     if ((ptg instanceof BoolPtg)) {
/* 609:637 */       return BoolEval.valueOf(((BoolPtg)ptg).getValue());
/* 610:    */     }
/* 611:639 */     if ((ptg instanceof ErrPtg)) {
/* 612:640 */       return ErrorEval.valueOf(((ErrPtg)ptg).getErrorCode());
/* 613:    */     }
/* 614:642 */     if ((ptg instanceof MissingArgPtg)) {
/* 615:643 */       return MissingArgEval.instance;
/* 616:    */     }
/* 617:645 */     if (((ptg instanceof AreaErrPtg)) || ((ptg instanceof RefErrorPtg)) || ((ptg instanceof DeletedArea3DPtg)) || ((ptg instanceof DeletedRef3DPtg))) {
/* 618:647 */       return ErrorEval.REF_INVALID;
/* 619:    */     }
/* 620:649 */     if ((ptg instanceof Ref3DPtg)) {
/* 621:650 */       return ec.getRef3DEval((Ref3DPtg)ptg);
/* 622:    */     }
/* 623:652 */     if ((ptg instanceof Ref3DPxg)) {
/* 624:653 */       return ec.getRef3DEval((Ref3DPxg)ptg);
/* 625:    */     }
/* 626:655 */     if ((ptg instanceof Area3DPtg)) {
/* 627:656 */       return ec.getArea3DEval((Area3DPtg)ptg);
/* 628:    */     }
/* 629:658 */     if ((ptg instanceof Area3DPxg)) {
/* 630:659 */       return ec.getArea3DEval((Area3DPxg)ptg);
/* 631:    */     }
/* 632:661 */     if ((ptg instanceof RefPtg))
/* 633:    */     {
/* 634:662 */       RefPtg rptg = (RefPtg)ptg;
/* 635:663 */       return ec.getRefEval(rptg.getRow(), rptg.getColumn());
/* 636:    */     }
/* 637:665 */     if ((ptg instanceof AreaPtg))
/* 638:    */     {
/* 639:666 */       AreaPtg aptg = (AreaPtg)ptg;
/* 640:667 */       return ec.getAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn());
/* 641:    */     }
/* 642:670 */     if ((ptg instanceof UnknownPtg)) {
/* 643:674 */       throw new RuntimeException("UnknownPtg not allowed");
/* 644:    */     }
/* 645:676 */     if ((ptg instanceof ExpPtg)) {
/* 646:679 */       throw new RuntimeException("ExpPtg currently not supported");
/* 647:    */     }
/* 648:682 */     throw new RuntimeException("Unexpected ptg class (" + ptg.getClass().getName() + ")");
/* 649:    */   }
/* 650:    */   
/* 651:    */   private ValueEval processNameEval(ValueEval eval, OperationEvaluationContext ec)
/* 652:    */   {
/* 653:686 */     if ((eval instanceof ExternalNameEval))
/* 654:    */     {
/* 655:687 */       EvaluationName name = ((ExternalNameEval)eval).getName();
/* 656:688 */       return getEvalForNameRecord(name, ec);
/* 657:    */     }
/* 658:690 */     return eval;
/* 659:    */   }
/* 660:    */   
/* 661:    */   private ValueEval getEvalForNameRecord(EvaluationName nameRecord, OperationEvaluationContext ec)
/* 662:    */   {
/* 663:694 */     if (nameRecord.isFunctionName()) {
/* 664:695 */       return new FunctionNameEval(nameRecord.getNameText());
/* 665:    */     }
/* 666:697 */     if (nameRecord.hasFormula()) {
/* 667:698 */       return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
/* 668:    */     }
/* 669:701 */     throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
/* 670:    */   }
/* 671:    */   
/* 672:    */   ValueEval evaluateNameFormula(Ptg[] ptgs, OperationEvaluationContext ec)
/* 673:    */   {
/* 674:708 */     if (ptgs.length == 1) {
/* 675:709 */       return getEvalForPtg(ptgs[0], ec);
/* 676:    */     }
/* 677:711 */     return evaluateFormula(ec, ptgs);
/* 678:    */   }
/* 679:    */   
/* 680:    */   ValueEval evaluateReference(EvaluationSheet sheet, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker)
/* 681:    */   {
/* 682:721 */     EvaluationCell cell = sheet.getCell(rowIndex, columnIndex);
/* 683:722 */     return evaluateAny(cell, sheetIndex, rowIndex, columnIndex, tracker);
/* 684:    */   }
/* 685:    */   
/* 686:    */   public FreeRefFunction findUserDefinedFunction(String functionName)
/* 687:    */   {
/* 688:725 */     return this._udfFinder.findFunction(functionName);
/* 689:    */   }
/* 690:    */   
/* 691:    */   public ValueEval evaluate(String formula, CellReference ref)
/* 692:    */   {
/* 693:736 */     String sheetName = ref == null ? null : ref.getSheetName();
/* 694:    */     int sheetIndex;
/* 695:    */     int sheetIndex;
/* 696:738 */     if (sheetName == null) {
/* 697:739 */       sheetIndex = -1;
/* 698:    */     } else {
/* 699:741 */       sheetIndex = getWorkbook().getSheetIndex(sheetName);
/* 700:    */     }
/* 701:743 */     int rowIndex = ref == null ? -1 : ref.getRow();
/* 702:744 */     short colIndex = ref == null ? -1 : ref.getCol();
/* 703:745 */     OperationEvaluationContext ec = new OperationEvaluationContext(this, getWorkbook(), sheetIndex, rowIndex, colIndex, new EvaluationTracker(this._cache));
/* 704:    */     
/* 705:    */ 
/* 706:    */ 
/* 707:    */ 
/* 708:    */ 
/* 709:    */ 
/* 710:    */ 
/* 711:753 */     Ptg[] ptgs = FormulaParser.parse(formula, (FormulaParsingWorkbook)getWorkbook(), FormulaType.CELL, sheetIndex, rowIndex);
/* 712:754 */     return evaluateNameFormula(ptgs, ec);
/* 713:    */   }
/* 714:    */   
/* 715:    */   public ValueEval evaluate(String formula, CellReference target, CellRangeAddressBase region)
/* 716:    */   {
/* 717:772 */     return evaluate(formula, target, region, FormulaType.CELL);
/* 718:    */   }
/* 719:    */   
/* 720:    */   public ValueEval evaluateList(String formula, CellReference target, CellRangeAddressBase region)
/* 721:    */   {
/* 722:790 */     return evaluate(formula, target, region, FormulaType.DATAVALIDATION_LIST);
/* 723:    */   }
/* 724:    */   
/* 725:    */   private ValueEval evaluate(String formula, CellReference target, CellRangeAddressBase region, FormulaType formulaType)
/* 726:    */   {
/* 727:794 */     String sheetName = target == null ? null : target.getSheetName();
/* 728:795 */     if (sheetName == null) {
/* 729:795 */       throw new IllegalArgumentException("Sheet name is required");
/* 730:    */     }
/* 731:797 */     int sheetIndex = getWorkbook().getSheetIndex(sheetName);
/* 732:798 */     Ptg[] ptgs = FormulaParser.parse(formula, (FormulaParsingWorkbook)getWorkbook(), formulaType, sheetIndex, target.getRow());
/* 733:    */     
/* 734:800 */     adjustRegionRelativeReference(ptgs, target, region);
/* 735:    */     
/* 736:802 */     OperationEvaluationContext ec = new OperationEvaluationContext(this, getWorkbook(), sheetIndex, target.getRow(), target.getCol(), new EvaluationTracker(this._cache), formulaType.isSingleValue());
/* 737:803 */     return evaluateNameFormula(ptgs, ec);
/* 738:    */   }
/* 739:    */   
/* 740:    */   protected boolean adjustRegionRelativeReference(Ptg[] ptgs, CellReference target, CellRangeAddressBase region)
/* 741:    */   {
/* 742:816 */     if (!region.isInRange(target)) {
/* 743:817 */       throw new IllegalArgumentException(target + " is not within " + region);
/* 744:    */     }
/* 745:820 */     return adjustRegionRelativeReference(ptgs, target.getRow() - region.getFirstRow(), target.getCol() - region.getFirstColumn());
/* 746:    */   }
/* 747:    */   
/* 748:    */   protected boolean adjustRegionRelativeReference(Ptg[] ptgs, int deltaRow, int deltaColumn)
/* 749:    */   {
/* 750:834 */     if (deltaRow < 0) {
/* 751:834 */       throw new IllegalArgumentException("offset row must be positive");
/* 752:    */     }
/* 753:835 */     if (deltaColumn < 0) {
/* 754:835 */       throw new IllegalArgumentException("offset column must be positive");
/* 755:    */     }
/* 756:836 */     boolean shifted = false;
/* 757:837 */     for (Ptg ptg : ptgs) {
/* 758:839 */       if ((ptg instanceof RefPtgBase))
/* 759:    */       {
/* 760:840 */         RefPtgBase ref = (RefPtgBase)ptg;
/* 761:    */         
/* 762:842 */         SpreadsheetVersion version = this._workbook.getSpreadsheetVersion();
/* 763:843 */         if (ref.isRowRelative())
/* 764:    */         {
/* 765:844 */           int rowIndex = ref.getRow() + deltaRow;
/* 766:845 */           if (rowIndex > version.getMaxRows()) {
/* 767:846 */             throw new IndexOutOfBoundsException(version.name() + " files can only have " + version.getMaxRows() + " rows, but row " + rowIndex + " was requested.");
/* 768:    */           }
/* 769:848 */           ref.setRow(rowIndex);
/* 770:849 */           shifted = true;
/* 771:    */         }
/* 772:851 */         if (ref.isColRelative())
/* 773:    */         {
/* 774:852 */           int colIndex = ref.getColumn() + deltaColumn;
/* 775:853 */           if (colIndex > version.getMaxColumns()) {
/* 776:854 */             throw new IndexOutOfBoundsException(version.name() + " files can only have " + version.getMaxColumns() + " columns, but column " + colIndex + " was requested.");
/* 777:    */           }
/* 778:856 */           ref.setColumn(colIndex);
/* 779:857 */           shifted = true;
/* 780:    */         }
/* 781:    */       }
/* 782:    */     }
/* 783:861 */     return shifted;
/* 784:    */   }
/* 785:    */   
/* 786:    */   public void setIgnoreMissingWorkbooks(boolean ignore)
/* 787:    */   {
/* 788:881 */     this._ignoreMissingWorkbooks = ignore;
/* 789:    */   }
/* 790:    */   
/* 791:    */   public boolean isIgnoreMissingWorkbooks()
/* 792:    */   {
/* 793:884 */     return this._ignoreMissingWorkbooks;
/* 794:    */   }
/* 795:    */   
/* 796:    */   public static Collection<String> getSupportedFunctionNames()
/* 797:    */   {
/* 798:893 */     Collection<String> lst = new TreeSet();
/* 799:894 */     lst.addAll(FunctionEval.getSupportedFunctionNames());
/* 800:895 */     lst.addAll(AnalysisToolPak.getSupportedFunctionNames());
/* 801:896 */     return Collections.unmodifiableCollection(lst);
/* 802:    */   }
/* 803:    */   
/* 804:    */   public static Collection<String> getNotSupportedFunctionNames()
/* 805:    */   {
/* 806:905 */     Collection<String> lst = new TreeSet();
/* 807:906 */     lst.addAll(FunctionEval.getNotSupportedFunctionNames());
/* 808:907 */     lst.addAll(AnalysisToolPak.getNotSupportedFunctionNames());
/* 809:908 */     return Collections.unmodifiableCollection(lst);
/* 810:    */   }
/* 811:    */   
/* 812:    */   public static void registerFunction(String name, FreeRefFunction func)
/* 813:    */   {
/* 814:920 */     AnalysisToolPak.registerFunction(name, func);
/* 815:    */   }
/* 816:    */   
/* 817:    */   public static void registerFunction(String name, Function func)
/* 818:    */   {
/* 819:932 */     FunctionEval.registerFunction(name, func);
/* 820:    */   }
/* 821:    */   
/* 822:    */   public void setDebugEvaluationOutputForNextEval(boolean value)
/* 823:    */   {
/* 824:936 */     this.dbgEvaluationOutputForNextEval = value;
/* 825:    */   }
/* 826:    */   
/* 827:    */   public boolean isDebugEvaluationOutputForNextEval()
/* 828:    */   {
/* 829:939 */     return this.dbgEvaluationOutputForNextEval;
/* 830:    */   }
/* 831:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.WorkbookEvaluator
 * JD-Core Version:    0.7.0.1
 */