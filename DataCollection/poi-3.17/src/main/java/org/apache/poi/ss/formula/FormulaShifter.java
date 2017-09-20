/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.ptg.Area2DPtgBase;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*   6:    */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*   7:    */ import org.apache.poi.ss.formula.ptg.AreaErrPtg;
/*   8:    */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*   9:    */ import org.apache.poi.ss.formula.ptg.AreaPtgBase;
/*  10:    */ import org.apache.poi.ss.formula.ptg.Deleted3DPxg;
/*  11:    */ import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
/*  12:    */ import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
/*  13:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  14:    */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*  15:    */ import org.apache.poi.ss.formula.ptg.Ref3DPxg;
/*  16:    */ import org.apache.poi.ss.formula.ptg.RefErrorPtg;
/*  17:    */ import org.apache.poi.ss.formula.ptg.RefPtg;
/*  18:    */ import org.apache.poi.ss.formula.ptg.RefPtgBase;
/*  19:    */ 
/*  20:    */ public final class FormulaShifter
/*  21:    */ {
/*  22:    */   private final int _externSheetIndex;
/*  23:    */   private final String _sheetName;
/*  24:    */   private final int _firstMovedIndex;
/*  25:    */   private final int _lastMovedIndex;
/*  26:    */   private final int _amountToMove;
/*  27:    */   private final int _srcSheetIndex;
/*  28:    */   private final int _dstSheetIndex;
/*  29:    */   private final SpreadsheetVersion _version;
/*  30:    */   private final ShiftMode _mode;
/*  31:    */   
/*  32:    */   private static enum ShiftMode
/*  33:    */   {
/*  34: 44 */     RowMove,  RowCopy,  SheetMove;
/*  35:    */     
/*  36:    */     private ShiftMode() {}
/*  37:    */   }
/*  38:    */   
/*  39:    */   private FormulaShifter(int externSheetIndex, String sheetName, int firstMovedIndex, int lastMovedIndex, int amountToMove, ShiftMode mode, SpreadsheetVersion version)
/*  40:    */   {
/*  41: 77 */     if (amountToMove == 0) {
/*  42: 78 */       throw new IllegalArgumentException("amountToMove must not be zero");
/*  43:    */     }
/*  44: 80 */     if (firstMovedIndex > lastMovedIndex) {
/*  45: 81 */       throw new IllegalArgumentException("firstMovedIndex, lastMovedIndex out of order");
/*  46:    */     }
/*  47: 83 */     this._externSheetIndex = externSheetIndex;
/*  48: 84 */     this._sheetName = sheetName;
/*  49: 85 */     this._firstMovedIndex = firstMovedIndex;
/*  50: 86 */     this._lastMovedIndex = lastMovedIndex;
/*  51: 87 */     this._amountToMove = amountToMove;
/*  52: 88 */     this._mode = mode;
/*  53: 89 */     this._version = version;
/*  54:    */     
/*  55: 91 */     this._srcSheetIndex = (this._dstSheetIndex = -1);
/*  56:    */   }
/*  57:    */   
/*  58:    */   private FormulaShifter(int srcSheetIndex, int dstSheetIndex)
/*  59:    */   {
/*  60:100 */     this._externSheetIndex = (this._firstMovedIndex = this._lastMovedIndex = this._amountToMove = -1);
/*  61:101 */     this._sheetName = null;
/*  62:102 */     this._version = null;
/*  63:    */     
/*  64:104 */     this._srcSheetIndex = srcSheetIndex;
/*  65:105 */     this._dstSheetIndex = dstSheetIndex;
/*  66:106 */     this._mode = ShiftMode.SheetMove;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static FormulaShifter createForRowShift(int externSheetIndex, String sheetName, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove, SpreadsheetVersion version)
/*  70:    */   {
/*  71:111 */     return new FormulaShifter(externSheetIndex, sheetName, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove, ShiftMode.RowMove, version);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static FormulaShifter createForRowCopy(int externSheetIndex, String sheetName, int firstMovedRowIndex, int lastMovedRowIndex, int numberOfRowsToMove, SpreadsheetVersion version)
/*  75:    */   {
/*  76:116 */     return new FormulaShifter(externSheetIndex, sheetName, firstMovedRowIndex, lastMovedRowIndex, numberOfRowsToMove, ShiftMode.RowCopy, version);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static FormulaShifter createForSheetShift(int srcSheetIndex, int dstSheetIndex)
/*  80:    */   {
/*  81:120 */     return new FormulaShifter(srcSheetIndex, dstSheetIndex);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String toString()
/*  85:    */   {
/*  86:125 */     StringBuffer sb = new StringBuffer();
/*  87:    */     
/*  88:127 */     sb.append(getClass().getName());
/*  89:128 */     sb.append(" [");
/*  90:129 */     sb.append(this._firstMovedIndex);
/*  91:130 */     sb.append(this._lastMovedIndex);
/*  92:131 */     sb.append(this._amountToMove);
/*  93:132 */     return sb.toString();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean adjustFormula(Ptg[] ptgs, int currentExternSheetIx)
/*  97:    */   {
/*  98:141 */     boolean refsWereChanged = false;
/*  99:142 */     for (int i = 0; i < ptgs.length; i++)
/* 100:    */     {
/* 101:143 */       Ptg newPtg = adjustPtg(ptgs[i], currentExternSheetIx);
/* 102:144 */       if (newPtg != null)
/* 103:    */       {
/* 104:145 */         refsWereChanged = true;
/* 105:146 */         ptgs[i] = newPtg;
/* 106:    */       }
/* 107:    */     }
/* 108:149 */     return refsWereChanged;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private Ptg adjustPtg(Ptg ptg, int currentExternSheetIx)
/* 112:    */   {
/* 113:153 */     switch (1.$SwitchMap$org$apache$poi$ss$formula$FormulaShifter$ShiftMode[this._mode.ordinal()])
/* 114:    */     {
/* 115:    */     case 1: 
/* 116:155 */       return adjustPtgDueToRowMove(ptg, currentExternSheetIx);
/* 117:    */     case 2: 
/* 118:160 */       return adjustPtgDueToRowCopy(ptg);
/* 119:    */     case 3: 
/* 120:162 */       return adjustPtgDueToSheetMove(ptg);
/* 121:    */     }
/* 122:164 */     throw new IllegalStateException("Unsupported shift mode: " + this._mode);
/* 123:    */   }
/* 124:    */   
/* 125:    */   private Ptg adjustPtgDueToRowMove(Ptg ptg, int currentExternSheetIx)
/* 126:    */   {
/* 127:173 */     if ((ptg instanceof RefPtg))
/* 128:    */     {
/* 129:174 */       if (currentExternSheetIx != this._externSheetIndex) {
/* 130:176 */         return null;
/* 131:    */       }
/* 132:178 */       RefPtg rptg = (RefPtg)ptg;
/* 133:179 */       return rowMoveRefPtg(rptg);
/* 134:    */     }
/* 135:181 */     if ((ptg instanceof Ref3DPtg))
/* 136:    */     {
/* 137:182 */       Ref3DPtg rptg = (Ref3DPtg)ptg;
/* 138:183 */       if (this._externSheetIndex != rptg.getExternSheetIndex()) {
/* 139:186 */         return null;
/* 140:    */       }
/* 141:188 */       return rowMoveRefPtg(rptg);
/* 142:    */     }
/* 143:190 */     if ((ptg instanceof Ref3DPxg))
/* 144:    */     {
/* 145:191 */       Ref3DPxg rpxg = (Ref3DPxg)ptg;
/* 146:192 */       if ((rpxg.getExternalWorkbookNumber() > 0) || (!this._sheetName.equals(rpxg.getSheetName()))) {
/* 147:195 */         return null;
/* 148:    */       }
/* 149:197 */       return rowMoveRefPtg(rpxg);
/* 150:    */     }
/* 151:199 */     if ((ptg instanceof Area2DPtgBase))
/* 152:    */     {
/* 153:200 */       if (currentExternSheetIx != this._externSheetIndex) {
/* 154:202 */         return ptg;
/* 155:    */       }
/* 156:204 */       return rowMoveAreaPtg((Area2DPtgBase)ptg);
/* 157:    */     }
/* 158:206 */     if ((ptg instanceof Area3DPtg))
/* 159:    */     {
/* 160:207 */       Area3DPtg aptg = (Area3DPtg)ptg;
/* 161:208 */       if (this._externSheetIndex != aptg.getExternSheetIndex()) {
/* 162:211 */         return null;
/* 163:    */       }
/* 164:213 */       return rowMoveAreaPtg(aptg);
/* 165:    */     }
/* 166:215 */     if ((ptg instanceof Area3DPxg))
/* 167:    */     {
/* 168:216 */       Area3DPxg apxg = (Area3DPxg)ptg;
/* 169:217 */       if ((apxg.getExternalWorkbookNumber() > 0) || (!this._sheetName.equals(apxg.getSheetName()))) {
/* 170:220 */         return null;
/* 171:    */       }
/* 172:222 */       return rowMoveAreaPtg(apxg);
/* 173:    */     }
/* 174:224 */     return null;
/* 175:    */   }
/* 176:    */   
/* 177:    */   private Ptg adjustPtgDueToRowCopy(Ptg ptg)
/* 178:    */   {
/* 179:243 */     if ((ptg instanceof RefPtg))
/* 180:    */     {
/* 181:244 */       RefPtg rptg = (RefPtg)ptg;
/* 182:245 */       return rowCopyRefPtg(rptg);
/* 183:    */     }
/* 184:247 */     if ((ptg instanceof Ref3DPtg))
/* 185:    */     {
/* 186:248 */       Ref3DPtg rptg = (Ref3DPtg)ptg;
/* 187:249 */       return rowCopyRefPtg(rptg);
/* 188:    */     }
/* 189:251 */     if ((ptg instanceof Ref3DPxg))
/* 190:    */     {
/* 191:252 */       Ref3DPxg rpxg = (Ref3DPxg)ptg;
/* 192:253 */       return rowCopyRefPtg(rpxg);
/* 193:    */     }
/* 194:255 */     if ((ptg instanceof Area2DPtgBase)) {
/* 195:256 */       return rowCopyAreaPtg((Area2DPtgBase)ptg);
/* 196:    */     }
/* 197:258 */     if ((ptg instanceof Area3DPtg))
/* 198:    */     {
/* 199:259 */       Area3DPtg aptg = (Area3DPtg)ptg;
/* 200:260 */       return rowCopyAreaPtg(aptg);
/* 201:    */     }
/* 202:262 */     if ((ptg instanceof Area3DPxg))
/* 203:    */     {
/* 204:263 */       Area3DPxg apxg = (Area3DPxg)ptg;
/* 205:264 */       return rowCopyAreaPtg(apxg);
/* 206:    */     }
/* 207:266 */     return null;
/* 208:    */   }
/* 209:    */   
/* 210:    */   private Ptg adjustPtgDueToSheetMove(Ptg ptg)
/* 211:    */   {
/* 212:271 */     if ((ptg instanceof Ref3DPtg))
/* 213:    */     {
/* 214:272 */       Ref3DPtg ref = (Ref3DPtg)ptg;
/* 215:273 */       int oldSheetIndex = ref.getExternSheetIndex();
/* 216:278 */       if ((oldSheetIndex < this._srcSheetIndex) && (oldSheetIndex < this._dstSheetIndex)) {
/* 217:280 */         return null;
/* 218:    */       }
/* 219:282 */       if ((oldSheetIndex > this._srcSheetIndex) && (oldSheetIndex > this._dstSheetIndex)) {
/* 220:284 */         return null;
/* 221:    */       }
/* 222:288 */       if (oldSheetIndex == this._srcSheetIndex)
/* 223:    */       {
/* 224:289 */         ref.setExternSheetIndex(this._dstSheetIndex);
/* 225:290 */         return ref;
/* 226:    */       }
/* 227:294 */       if (this._dstSheetIndex < this._srcSheetIndex)
/* 228:    */       {
/* 229:295 */         ref.setExternSheetIndex(oldSheetIndex + 1);
/* 230:296 */         return ref;
/* 231:    */       }
/* 232:300 */       if (this._dstSheetIndex > this._srcSheetIndex)
/* 233:    */       {
/* 234:301 */         ref.setExternSheetIndex(oldSheetIndex - 1);
/* 235:302 */         return ref;
/* 236:    */       }
/* 237:    */     }
/* 238:306 */     return null;
/* 239:    */   }
/* 240:    */   
/* 241:    */   private Ptg rowMoveRefPtg(RefPtgBase rptg)
/* 242:    */   {
/* 243:310 */     int refRow = rptg.getRow();
/* 244:311 */     if ((this._firstMovedIndex <= refRow) && (refRow <= this._lastMovedIndex))
/* 245:    */     {
/* 246:314 */       rptg.setRow(refRow + this._amountToMove);
/* 247:315 */       return rptg;
/* 248:    */     }
/* 249:319 */     int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
/* 250:320 */     int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
/* 251:325 */     if ((destLastRowIndex < refRow) || (refRow < destFirstRowIndex)) {
/* 252:327 */       return null;
/* 253:    */     }
/* 254:330 */     if ((destFirstRowIndex <= refRow) && (refRow <= destLastRowIndex)) {
/* 255:332 */       return createDeletedRef(rptg);
/* 256:    */     }
/* 257:334 */     throw new IllegalStateException("Situation not covered: (" + this._firstMovedIndex + ", " + this._lastMovedIndex + ", " + this._amountToMove + ", " + refRow + ", " + refRow + ")");
/* 258:    */   }
/* 259:    */   
/* 260:    */   private Ptg rowMoveAreaPtg(AreaPtgBase aptg)
/* 261:    */   {
/* 262:339 */     int aFirstRow = aptg.getFirstRow();
/* 263:340 */     int aLastRow = aptg.getLastRow();
/* 264:341 */     if ((this._firstMovedIndex <= aFirstRow) && (aLastRow <= this._lastMovedIndex))
/* 265:    */     {
/* 266:344 */       aptg.setFirstRow(aFirstRow + this._amountToMove);
/* 267:345 */       aptg.setLastRow(aLastRow + this._amountToMove);
/* 268:346 */       return aptg;
/* 269:    */     }
/* 270:350 */     int destFirstRowIndex = this._firstMovedIndex + this._amountToMove;
/* 271:351 */     int destLastRowIndex = this._lastMovedIndex + this._amountToMove;
/* 272:353 */     if ((aFirstRow < this._firstMovedIndex) && (this._lastMovedIndex < aLastRow))
/* 273:    */     {
/* 274:358 */       if ((destFirstRowIndex < aFirstRow) && (aFirstRow <= destLastRowIndex))
/* 275:    */       {
/* 276:360 */         aptg.setFirstRow(destLastRowIndex + 1);
/* 277:361 */         return aptg;
/* 278:    */       }
/* 279:362 */       if ((destFirstRowIndex <= aLastRow) && (aLastRow < destLastRowIndex))
/* 280:    */       {
/* 281:364 */         aptg.setLastRow(destFirstRowIndex - 1);
/* 282:365 */         return aptg;
/* 283:    */       }
/* 284:369 */       return null;
/* 285:    */     }
/* 286:371 */     if ((this._firstMovedIndex <= aFirstRow) && (aFirstRow <= this._lastMovedIndex))
/* 287:    */     {
/* 288:374 */       if (this._amountToMove < 0)
/* 289:    */       {
/* 290:376 */         aptg.setFirstRow(aFirstRow + this._amountToMove);
/* 291:377 */         return aptg;
/* 292:    */       }
/* 293:379 */       if (destFirstRowIndex > aLastRow) {
/* 294:381 */         return null;
/* 295:    */       }
/* 296:383 */       int newFirstRowIx = aFirstRow + this._amountToMove;
/* 297:384 */       if (destLastRowIndex < aLastRow)
/* 298:    */       {
/* 299:387 */         aptg.setFirstRow(newFirstRowIx);
/* 300:388 */         return aptg;
/* 301:    */       }
/* 302:391 */       int areaRemainingTopRowIx = this._lastMovedIndex + 1;
/* 303:392 */       if (destFirstRowIndex > areaRemainingTopRowIx) {
/* 304:394 */         newFirstRowIx = areaRemainingTopRowIx;
/* 305:    */       }
/* 306:396 */       aptg.setFirstRow(newFirstRowIx);
/* 307:397 */       aptg.setLastRow(Math.max(aLastRow, destLastRowIndex));
/* 308:398 */       return aptg;
/* 309:    */     }
/* 310:400 */     if ((this._firstMovedIndex <= aLastRow) && (aLastRow <= this._lastMovedIndex))
/* 311:    */     {
/* 312:403 */       if (this._amountToMove > 0)
/* 313:    */       {
/* 314:405 */         aptg.setLastRow(aLastRow + this._amountToMove);
/* 315:406 */         return aptg;
/* 316:    */       }
/* 317:408 */       if (destLastRowIndex < aFirstRow) {
/* 318:410 */         return null;
/* 319:    */       }
/* 320:412 */       int newLastRowIx = aLastRow + this._amountToMove;
/* 321:413 */       if (destFirstRowIndex > aFirstRow)
/* 322:    */       {
/* 323:416 */         aptg.setLastRow(newLastRowIx);
/* 324:417 */         return aptg;
/* 325:    */       }
/* 326:420 */       int areaRemainingBottomRowIx = this._firstMovedIndex - 1;
/* 327:421 */       if (destLastRowIndex < areaRemainingBottomRowIx) {
/* 328:423 */         newLastRowIx = areaRemainingBottomRowIx;
/* 329:    */       }
/* 330:425 */       aptg.setFirstRow(Math.min(aFirstRow, destFirstRowIndex));
/* 331:426 */       aptg.setLastRow(newLastRowIx);
/* 332:427 */       return aptg;
/* 333:    */     }
/* 334:432 */     if ((destLastRowIndex < aFirstRow) || (aLastRow < destFirstRowIndex)) {
/* 335:434 */       return null;
/* 336:    */     }
/* 337:437 */     if ((destFirstRowIndex <= aFirstRow) && (aLastRow <= destLastRowIndex)) {
/* 338:439 */       return createDeletedRef(aptg);
/* 339:    */     }
/* 340:442 */     if ((aFirstRow <= destFirstRowIndex) && (destLastRowIndex <= aLastRow)) {
/* 341:444 */       return null;
/* 342:    */     }
/* 343:447 */     if ((destFirstRowIndex < aFirstRow) && (aFirstRow <= destLastRowIndex))
/* 344:    */     {
/* 345:450 */       aptg.setFirstRow(destLastRowIndex + 1);
/* 346:451 */       return aptg;
/* 347:    */     }
/* 348:453 */     if ((destFirstRowIndex <= aLastRow) && (aLastRow < destLastRowIndex))
/* 349:    */     {
/* 350:456 */       aptg.setLastRow(destFirstRowIndex - 1);
/* 351:457 */       return aptg;
/* 352:    */     }
/* 353:459 */     throw new IllegalStateException("Situation not covered: (" + this._firstMovedIndex + ", " + this._lastMovedIndex + ", " + this._amountToMove + ", " + aFirstRow + ", " + aLastRow + ")");
/* 354:    */   }
/* 355:    */   
/* 356:    */   private Ptg rowCopyRefPtg(RefPtgBase rptg)
/* 357:    */   {
/* 358:472 */     int refRow = rptg.getRow();
/* 359:473 */     if (rptg.isRowRelative())
/* 360:    */     {
/* 361:474 */       int destRowIndex = this._firstMovedIndex + this._amountToMove;
/* 362:475 */       if ((destRowIndex < 0) || (this._version.getLastRowIndex() < destRowIndex)) {
/* 363:476 */         return createDeletedRef(rptg);
/* 364:    */       }
/* 365:477 */       rptg.setRow(refRow + this._amountToMove);
/* 366:478 */       return rptg;
/* 367:    */     }
/* 368:480 */     return null;
/* 369:    */   }
/* 370:    */   
/* 371:    */   private Ptg rowCopyAreaPtg(AreaPtgBase aptg)
/* 372:    */   {
/* 373:492 */     boolean changed = false;
/* 374:    */     
/* 375:494 */     int aFirstRow = aptg.getFirstRow();
/* 376:495 */     int aLastRow = aptg.getLastRow();
/* 377:497 */     if (aptg.isFirstRowRelative())
/* 378:    */     {
/* 379:498 */       int destFirstRowIndex = aFirstRow + this._amountToMove;
/* 380:499 */       if ((destFirstRowIndex < 0) || (this._version.getLastRowIndex() < destFirstRowIndex)) {
/* 381:500 */         return createDeletedRef(aptg);
/* 382:    */       }
/* 383:501 */       aptg.setFirstRow(destFirstRowIndex);
/* 384:502 */       changed = true;
/* 385:    */     }
/* 386:504 */     if (aptg.isLastRowRelative())
/* 387:    */     {
/* 388:505 */       int destLastRowIndex = aLastRow + this._amountToMove;
/* 389:506 */       if ((destLastRowIndex < 0) || (this._version.getLastRowIndex() < destLastRowIndex)) {
/* 390:507 */         return createDeletedRef(aptg);
/* 391:    */       }
/* 392:508 */       aptg.setLastRow(destLastRowIndex);
/* 393:509 */       changed = true;
/* 394:    */     }
/* 395:511 */     if (changed) {
/* 396:512 */       aptg.sortTopLeftToBottomRight();
/* 397:    */     }
/* 398:515 */     return changed ? aptg : null;
/* 399:    */   }
/* 400:    */   
/* 401:    */   private static Ptg createDeletedRef(Ptg ptg)
/* 402:    */   {
/* 403:519 */     if ((ptg instanceof RefPtg)) {
/* 404:520 */       return new RefErrorPtg();
/* 405:    */     }
/* 406:522 */     if ((ptg instanceof Ref3DPtg))
/* 407:    */     {
/* 408:523 */       Ref3DPtg rptg = (Ref3DPtg)ptg;
/* 409:524 */       return new DeletedRef3DPtg(rptg.getExternSheetIndex());
/* 410:    */     }
/* 411:526 */     if ((ptg instanceof AreaPtg)) {
/* 412:527 */       return new AreaErrPtg();
/* 413:    */     }
/* 414:529 */     if ((ptg instanceof Area3DPtg))
/* 415:    */     {
/* 416:530 */       Area3DPtg area3DPtg = (Area3DPtg)ptg;
/* 417:531 */       return new DeletedArea3DPtg(area3DPtg.getExternSheetIndex());
/* 418:    */     }
/* 419:533 */     if ((ptg instanceof Ref3DPxg))
/* 420:    */     {
/* 421:534 */       Ref3DPxg pxg = (Ref3DPxg)ptg;
/* 422:535 */       return new Deleted3DPxg(pxg.getExternalWorkbookNumber(), pxg.getSheetName());
/* 423:    */     }
/* 424:537 */     if ((ptg instanceof Area3DPxg))
/* 425:    */     {
/* 426:538 */       Area3DPxg pxg = (Area3DPxg)ptg;
/* 427:539 */       return new Deleted3DPxg(pxg.getExternalWorkbookNumber(), pxg.getSheetName());
/* 428:    */     }
/* 429:542 */     throw new IllegalArgumentException("Unexpected ref ptg class (" + ptg.getClass().getName() + ")");
/* 430:    */   }
/* 431:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.FormulaShifter
 * JD-Core Version:    0.7.0.1
 */