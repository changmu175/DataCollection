/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.hssf.model.RecordStream;
/*   8:    */ import org.apache.poi.hssf.record.ColumnInfoRecord;
/*   9:    */ 
/*  10:    */ public final class ColumnInfoRecordsAggregate
/*  11:    */   extends RecordAggregate
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14:    */   private final List<ColumnInfoRecord> records;
/*  15:    */   
/*  16:    */   private static final class CIRComparator
/*  17:    */     implements Comparator<ColumnInfoRecord>
/*  18:    */   {
/*  19: 39 */     public static final Comparator<ColumnInfoRecord> instance = new CIRComparator();
/*  20:    */     
/*  21:    */     public int compare(ColumnInfoRecord a, ColumnInfoRecord b)
/*  22:    */     {
/*  23: 44 */       return compareColInfos(a, b);
/*  24:    */     }
/*  25:    */     
/*  26:    */     public static int compareColInfos(ColumnInfoRecord a, ColumnInfoRecord b)
/*  27:    */     {
/*  28: 47 */       return a.getFirstColumn() - b.getFirstColumn();
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ColumnInfoRecordsAggregate()
/*  33:    */   {
/*  34: 55 */     this.records = new ArrayList();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ColumnInfoRecordsAggregate(RecordStream rs)
/*  38:    */   {
/*  39: 58 */     this();
/*  40:    */     
/*  41: 60 */     boolean isInOrder = true;
/*  42: 61 */     ColumnInfoRecord cirPrev = null;
/*  43: 62 */     while (rs.peekNextClass() == ColumnInfoRecord.class)
/*  44:    */     {
/*  45: 63 */       ColumnInfoRecord cir = (ColumnInfoRecord)rs.getNext();
/*  46: 64 */       this.records.add(cir);
/*  47: 65 */       if ((cirPrev != null) && (CIRComparator.compareColInfos(cirPrev, cir) > 0)) {
/*  48: 66 */         isInOrder = false;
/*  49:    */       }
/*  50: 68 */       cirPrev = cir;
/*  51:    */     }
/*  52: 70 */     if (this.records.size() < 1) {
/*  53: 71 */       throw new RuntimeException("No column info records found");
/*  54:    */     }
/*  55: 73 */     if (!isInOrder) {
/*  56: 74 */       Collections.sort(this.records, CIRComparator.instance);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public ColumnInfoRecordsAggregate clone()
/*  61:    */   {
/*  62: 80 */     ColumnInfoRecordsAggregate rec = new ColumnInfoRecordsAggregate();
/*  63: 81 */     for (ColumnInfoRecord ci : this.records) {
/*  64: 82 */       rec.records.add(ci.clone());
/*  65:    */     }
/*  66: 84 */     return rec;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void insertColumn(ColumnInfoRecord col)
/*  70:    */   {
/*  71: 91 */     this.records.add(col);
/*  72: 92 */     Collections.sort(this.records, CIRComparator.instance);
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void insertColumn(int idx, ColumnInfoRecord col)
/*  76:    */   {
/*  77:100 */     this.records.add(idx, col);
/*  78:    */   }
/*  79:    */   
/*  80:    */   int getNumColumns()
/*  81:    */   {
/*  82:104 */     return this.records.size();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void visitContainedRecords(RecordVisitor rv)
/*  86:    */   {
/*  87:108 */     int nItems = this.records.size();
/*  88:109 */     if (nItems < 1) {
/*  89:110 */       return;
/*  90:    */     }
/*  91:112 */     ColumnInfoRecord cirPrev = null;
/*  92:113 */     for (int i = 0; i < nItems; i++)
/*  93:    */     {
/*  94:114 */       ColumnInfoRecord cir = (ColumnInfoRecord)this.records.get(i);
/*  95:115 */       rv.visitRecord(cir);
/*  96:116 */       if ((cirPrev != null) && (CIRComparator.compareColInfos(cirPrev, cir) > 0)) {
/*  97:119 */         throw new RuntimeException("Column info records are out of order");
/*  98:    */       }
/*  99:121 */       cirPrev = cir;
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private int findStartOfColumnOutlineGroup(int pIdx)
/* 104:    */   {
/* 105:127 */     ColumnInfoRecord columnInfo = (ColumnInfoRecord)this.records.get(pIdx);
/* 106:128 */     int level = columnInfo.getOutlineLevel();
/* 107:129 */     int idx = pIdx;
/* 108:130 */     while (idx != 0)
/* 109:    */     {
/* 110:131 */       ColumnInfoRecord prevColumnInfo = (ColumnInfoRecord)this.records.get(idx - 1);
/* 111:132 */       if (!prevColumnInfo.isAdjacentBefore(columnInfo)) {
/* 112:    */         break;
/* 113:    */       }
/* 114:135 */       if (prevColumnInfo.getOutlineLevel() < level) {
/* 115:    */         break;
/* 116:    */       }
/* 117:138 */       idx--;
/* 118:139 */       columnInfo = prevColumnInfo;
/* 119:    */     }
/* 120:142 */     return idx;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private int findEndOfColumnOutlineGroup(int colInfoIndex)
/* 124:    */   {
/* 125:147 */     ColumnInfoRecord columnInfo = (ColumnInfoRecord)this.records.get(colInfoIndex);
/* 126:148 */     int level = columnInfo.getOutlineLevel();
/* 127:149 */     int idx = colInfoIndex;
/* 128:150 */     while (idx < this.records.size() - 1)
/* 129:    */     {
/* 130:151 */       ColumnInfoRecord nextColumnInfo = (ColumnInfoRecord)this.records.get(idx + 1);
/* 131:152 */       if (!columnInfo.isAdjacentBefore(nextColumnInfo)) {
/* 132:    */         break;
/* 133:    */       }
/* 134:155 */       if (nextColumnInfo.getOutlineLevel() < level) {
/* 135:    */         break;
/* 136:    */       }
/* 137:158 */       idx++;
/* 138:159 */       columnInfo = nextColumnInfo;
/* 139:    */     }
/* 140:161 */     return idx;
/* 141:    */   }
/* 142:    */   
/* 143:    */   private ColumnInfoRecord getColInfo(int idx)
/* 144:    */   {
/* 145:165 */     return (ColumnInfoRecord)this.records.get(idx);
/* 146:    */   }
/* 147:    */   
/* 148:    */   private boolean isColumnGroupCollapsed(int idx)
/* 149:    */   {
/* 150:174 */     int endOfOutlineGroupIdx = findEndOfColumnOutlineGroup(idx);
/* 151:175 */     int nextColInfoIx = endOfOutlineGroupIdx + 1;
/* 152:176 */     if (nextColInfoIx >= this.records.size()) {
/* 153:177 */       return false;
/* 154:    */     }
/* 155:179 */     ColumnInfoRecord nextColInfo = getColInfo(nextColInfoIx);
/* 156:180 */     if (!getColInfo(endOfOutlineGroupIdx).isAdjacentBefore(nextColInfo)) {
/* 157:181 */       return false;
/* 158:    */     }
/* 159:183 */     return nextColInfo.getCollapsed();
/* 160:    */   }
/* 161:    */   
/* 162:    */   private boolean isColumnGroupHiddenByParent(int idx)
/* 163:    */   {
/* 164:189 */     int endLevel = 0;
/* 165:190 */     boolean endHidden = false;
/* 166:191 */     int endOfOutlineGroupIdx = findEndOfColumnOutlineGroup(idx);
/* 167:192 */     if (endOfOutlineGroupIdx < this.records.size())
/* 168:    */     {
/* 169:193 */       ColumnInfoRecord nextInfo = getColInfo(endOfOutlineGroupIdx + 1);
/* 170:194 */       if (getColInfo(endOfOutlineGroupIdx).isAdjacentBefore(nextInfo))
/* 171:    */       {
/* 172:195 */         endLevel = nextInfo.getOutlineLevel();
/* 173:196 */         endHidden = nextInfo.getHidden();
/* 174:    */       }
/* 175:    */     }
/* 176:200 */     int startLevel = 0;
/* 177:201 */     boolean startHidden = false;
/* 178:202 */     int startOfOutlineGroupIdx = findStartOfColumnOutlineGroup(idx);
/* 179:203 */     if (startOfOutlineGroupIdx > 0)
/* 180:    */     {
/* 181:204 */       ColumnInfoRecord prevInfo = getColInfo(startOfOutlineGroupIdx - 1);
/* 182:205 */       if (prevInfo.isAdjacentBefore(getColInfo(startOfOutlineGroupIdx)))
/* 183:    */       {
/* 184:206 */         startLevel = prevInfo.getOutlineLevel();
/* 185:207 */         startHidden = prevInfo.getHidden();
/* 186:    */       }
/* 187:    */     }
/* 188:210 */     if (endLevel > startLevel) {
/* 189:211 */       return endHidden;
/* 190:    */     }
/* 191:213 */     return startHidden;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public void collapseColumn(int columnIndex)
/* 195:    */   {
/* 196:217 */     int colInfoIx = findColInfoIdx(columnIndex, 0);
/* 197:218 */     if (colInfoIx == -1) {
/* 198:219 */       return;
/* 199:    */     }
/* 200:223 */     int groupStartColInfoIx = findStartOfColumnOutlineGroup(colInfoIx);
/* 201:224 */     ColumnInfoRecord columnInfo = getColInfo(groupStartColInfoIx);
/* 202:    */     
/* 203:    */ 
/* 204:227 */     int lastColIx = setGroupHidden(groupStartColInfoIx, columnInfo.getOutlineLevel(), true);
/* 205:    */     
/* 206:    */ 
/* 207:230 */     setColumn(lastColIx + 1, null, null, null, null, Boolean.TRUE);
/* 208:    */   }
/* 209:    */   
/* 210:    */   private int setGroupHidden(int pIdx, int level, boolean hidden)
/* 211:    */   {
/* 212:238 */     int idx = pIdx;
/* 213:239 */     ColumnInfoRecord columnInfo = getColInfo(idx);
/* 214:240 */     while (idx < this.records.size())
/* 215:    */     {
/* 216:241 */       columnInfo.setHidden(hidden);
/* 217:242 */       if (idx + 1 < this.records.size())
/* 218:    */       {
/* 219:243 */         ColumnInfoRecord nextColumnInfo = getColInfo(idx + 1);
/* 220:244 */         if (!columnInfo.isAdjacentBefore(nextColumnInfo)) {
/* 221:    */           break;
/* 222:    */         }
/* 223:247 */         if (nextColumnInfo.getOutlineLevel() < level) {
/* 224:    */           break;
/* 225:    */         }
/* 226:250 */         columnInfo = nextColumnInfo;
/* 227:    */       }
/* 228:252 */       idx++;
/* 229:    */     }
/* 230:254 */     return columnInfo.getLastColumn();
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void expandColumn(int columnIndex)
/* 234:    */   {
/* 235:259 */     int idx = findColInfoIdx(columnIndex, 0);
/* 236:260 */     if (idx == -1) {
/* 237:261 */       return;
/* 238:    */     }
/* 239:265 */     if (!isColumnGroupCollapsed(idx)) {
/* 240:266 */       return;
/* 241:    */     }
/* 242:270 */     int startIdx = findStartOfColumnOutlineGroup(idx);
/* 243:271 */     int endIdx = findEndOfColumnOutlineGroup(idx);
/* 244:    */     
/* 245:    */ 
/* 246:    */ 
/* 247:    */ 
/* 248:    */ 
/* 249:    */ 
/* 250:    */ 
/* 251:    */ 
/* 252:280 */     ColumnInfoRecord columnInfo = getColInfo(endIdx);
/* 253:281 */     if (!isColumnGroupHiddenByParent(idx))
/* 254:    */     {
/* 255:282 */       int outlineLevel = columnInfo.getOutlineLevel();
/* 256:283 */       for (int i = startIdx; i <= endIdx; i++)
/* 257:    */       {
/* 258:284 */         ColumnInfoRecord ci = getColInfo(i);
/* 259:285 */         if (outlineLevel == ci.getOutlineLevel()) {
/* 260:286 */           ci.setHidden(false);
/* 261:    */         }
/* 262:    */       }
/* 263:    */     }
/* 264:291 */     setColumn(columnInfo.getLastColumn() + 1, null, null, null, null, Boolean.FALSE);
/* 265:    */   }
/* 266:    */   
/* 267:    */   private static ColumnInfoRecord copyColInfo(ColumnInfoRecord ci)
/* 268:    */   {
/* 269:295 */     return ci.clone();
/* 270:    */   }
/* 271:    */   
/* 272:    */   public void setColumn(int targetColumnIx, Short xfIndex, Integer width, Integer level, Boolean hidden, Boolean collapsed)
/* 273:    */   {
/* 274:301 */     ColumnInfoRecord ci = null;
/* 275:302 */     int k = 0;
/* 276:304 */     for (k = 0; k < this.records.size(); k++)
/* 277:    */     {
/* 278:305 */       ColumnInfoRecord tci = (ColumnInfoRecord)this.records.get(k);
/* 279:306 */       if (tci.containsColumn(targetColumnIx)) {
/* 280:307 */         ci = tci;
/* 281:    */       } else {
/* 282:310 */         if (tci.getFirstColumn() > targetColumnIx) {
/* 283:    */           break;
/* 284:    */         }
/* 285:    */       }
/* 286:    */     }
/* 287:316 */     if (ci == null)
/* 288:    */     {
/* 289:318 */       ColumnInfoRecord nci = new ColumnInfoRecord();
/* 290:    */       
/* 291:320 */       nci.setFirstColumn(targetColumnIx);
/* 292:321 */       nci.setLastColumn(targetColumnIx);
/* 293:322 */       setColumnInfoFields(nci, xfIndex, width, level, hidden, collapsed);
/* 294:323 */       insertColumn(k, nci);
/* 295:324 */       attemptMergeColInfoRecords(k);
/* 296:325 */       return;
/* 297:    */     }
/* 298:328 */     boolean styleChanged = (xfIndex != null) && (ci.getXFIndex() != xfIndex.shortValue());
/* 299:329 */     boolean widthChanged = (width != null) && (ci.getColumnWidth() != width.shortValue());
/* 300:330 */     boolean levelChanged = (level != null) && (ci.getOutlineLevel() != level.intValue());
/* 301:331 */     boolean hiddenChanged = (hidden != null) && (ci.getHidden() != hidden.booleanValue());
/* 302:332 */     boolean collapsedChanged = (collapsed != null) && (ci.getCollapsed() != collapsed.booleanValue());
/* 303:    */     
/* 304:334 */     boolean columnChanged = (styleChanged) || (widthChanged) || (levelChanged) || (hiddenChanged) || (collapsedChanged);
/* 305:335 */     if (!columnChanged) {
/* 306:337 */       return;
/* 307:    */     }
/* 308:340 */     if ((ci.getFirstColumn() == targetColumnIx) && (ci.getLastColumn() == targetColumnIx))
/* 309:    */     {
/* 310:342 */       setColumnInfoFields(ci, xfIndex, width, level, hidden, collapsed);
/* 311:343 */       attemptMergeColInfoRecords(k);
/* 312:344 */       return;
/* 313:    */     }
/* 314:347 */     if ((ci.getFirstColumn() == targetColumnIx) || (ci.getLastColumn() == targetColumnIx))
/* 315:    */     {
/* 316:350 */       if (ci.getFirstColumn() == targetColumnIx)
/* 317:    */       {
/* 318:351 */         ci.setFirstColumn(targetColumnIx + 1);
/* 319:    */       }
/* 320:    */       else
/* 321:    */       {
/* 322:353 */         ci.setLastColumn(targetColumnIx - 1);
/* 323:354 */         k++;
/* 324:    */       }
/* 325:356 */       ColumnInfoRecord nci = copyColInfo(ci);
/* 326:    */       
/* 327:358 */       nci.setFirstColumn(targetColumnIx);
/* 328:359 */       nci.setLastColumn(targetColumnIx);
/* 329:360 */       setColumnInfoFields(nci, xfIndex, width, level, hidden, collapsed);
/* 330:    */       
/* 331:362 */       insertColumn(k, nci);
/* 332:363 */       attemptMergeColInfoRecords(k);
/* 333:    */     }
/* 334:    */     else
/* 335:    */     {
/* 336:366 */       ColumnInfoRecord ciStart = ci;
/* 337:367 */       ColumnInfoRecord ciMid = copyColInfo(ci);
/* 338:368 */       ColumnInfoRecord ciEnd = copyColInfo(ci);
/* 339:369 */       int lastcolumn = ci.getLastColumn();
/* 340:    */       
/* 341:371 */       ciStart.setLastColumn(targetColumnIx - 1);
/* 342:    */       
/* 343:373 */       ciMid.setFirstColumn(targetColumnIx);
/* 344:374 */       ciMid.setLastColumn(targetColumnIx);
/* 345:375 */       setColumnInfoFields(ciMid, xfIndex, width, level, hidden, collapsed);
/* 346:376 */       insertColumn(++k, ciMid);
/* 347:    */       
/* 348:378 */       ciEnd.setFirstColumn(targetColumnIx + 1);
/* 349:379 */       ciEnd.setLastColumn(lastcolumn);
/* 350:380 */       insertColumn(++k, ciEnd);
/* 351:    */     }
/* 352:    */   }
/* 353:    */   
/* 354:    */   private static void setColumnInfoFields(ColumnInfoRecord ci, Short xfStyle, Integer width, Integer level, Boolean hidden, Boolean collapsed)
/* 355:    */   {
/* 356:391 */     if (xfStyle != null) {
/* 357:392 */       ci.setXFIndex(xfStyle.shortValue());
/* 358:    */     }
/* 359:394 */     if (width != null) {
/* 360:395 */       ci.setColumnWidth(width.intValue());
/* 361:    */     }
/* 362:397 */     if (level != null) {
/* 363:398 */       ci.setOutlineLevel(level.shortValue());
/* 364:    */     }
/* 365:400 */     if (hidden != null) {
/* 366:401 */       ci.setHidden(hidden.booleanValue());
/* 367:    */     }
/* 368:403 */     if (collapsed != null) {
/* 369:404 */       ci.setCollapsed(collapsed.booleanValue());
/* 370:    */     }
/* 371:    */   }
/* 372:    */   
/* 373:    */   private int findColInfoIdx(int columnIx, int fromColInfoIdx)
/* 374:    */   {
/* 375:409 */     if (columnIx < 0) {
/* 376:410 */       throw new IllegalArgumentException("column parameter out of range: " + columnIx);
/* 377:    */     }
/* 378:412 */     if (fromColInfoIdx < 0) {
/* 379:413 */       throw new IllegalArgumentException("fromIdx parameter out of range: " + fromColInfoIdx);
/* 380:    */     }
/* 381:416 */     for (int k = fromColInfoIdx; k < this.records.size(); k++)
/* 382:    */     {
/* 383:417 */       ColumnInfoRecord ci = getColInfo(k);
/* 384:418 */       if (ci.containsColumn(columnIx)) {
/* 385:419 */         return k;
/* 386:    */       }
/* 387:421 */       if (ci.getFirstColumn() > columnIx) {
/* 388:    */         break;
/* 389:    */       }
/* 390:    */     }
/* 391:425 */     return -1;
/* 392:    */   }
/* 393:    */   
/* 394:    */   private void attemptMergeColInfoRecords(int colInfoIx)
/* 395:    */   {
/* 396:433 */     int nRecords = this.records.size();
/* 397:434 */     if ((colInfoIx < 0) || (colInfoIx >= nRecords)) {
/* 398:435 */       throw new IllegalArgumentException("colInfoIx " + colInfoIx + " is out of range (0.." + (nRecords - 1) + ")");
/* 399:    */     }
/* 400:438 */     ColumnInfoRecord currentCol = getColInfo(colInfoIx);
/* 401:439 */     int nextIx = colInfoIx + 1;
/* 402:440 */     if ((nextIx < nRecords) && 
/* 403:441 */       (mergeColInfoRecords(currentCol, getColInfo(nextIx)))) {
/* 404:442 */       this.records.remove(nextIx);
/* 405:    */     }
/* 406:445 */     if ((colInfoIx > 0) && 
/* 407:446 */       (mergeColInfoRecords(getColInfo(colInfoIx - 1), currentCol))) {
/* 408:447 */       this.records.remove(colInfoIx);
/* 409:    */     }
/* 410:    */   }
/* 411:    */   
/* 412:    */   private static boolean mergeColInfoRecords(ColumnInfoRecord ciA, ColumnInfoRecord ciB)
/* 413:    */   {
/* 414:456 */     if ((ciA.isAdjacentBefore(ciB)) && (ciA.formatMatches(ciB)))
/* 415:    */     {
/* 416:457 */       ciA.setLastColumn(ciB.getLastColumn());
/* 417:458 */       return true;
/* 418:    */     }
/* 419:460 */     return false;
/* 420:    */   }
/* 421:    */   
/* 422:    */   public void groupColumnRange(int fromColumnIx, int toColumnIx, boolean indent)
/* 423:    */   {
/* 424:478 */     int colInfoSearchStartIdx = 0;
/* 425:479 */     for (int i = fromColumnIx; i <= toColumnIx; i++)
/* 426:    */     {
/* 427:480 */       int level = 1;
/* 428:481 */       int colInfoIdx = findColInfoIdx(i, colInfoSearchStartIdx);
/* 429:482 */       if (colInfoIdx != -1)
/* 430:    */       {
/* 431:483 */         level = getColInfo(colInfoIdx).getOutlineLevel();
/* 432:484 */         if (indent) {
/* 433:485 */           level++;
/* 434:    */         } else {
/* 435:487 */           level--;
/* 436:    */         }
/* 437:489 */         level = Math.max(0, level);
/* 438:490 */         level = Math.min(7, level);
/* 439:491 */         colInfoSearchStartIdx = Math.max(0, colInfoIdx - 1);
/* 440:    */       }
/* 441:493 */       setColumn(i, null, null, Integer.valueOf(level), null, null);
/* 442:    */     }
/* 443:    */   }
/* 444:    */   
/* 445:    */   public ColumnInfoRecord findColumnInfo(int columnIndex)
/* 446:    */   {
/* 447:502 */     int nInfos = this.records.size();
/* 448:503 */     for (int i = 0; i < nInfos; i++)
/* 449:    */     {
/* 450:504 */       ColumnInfoRecord ci = getColInfo(i);
/* 451:505 */       if (ci.containsColumn(columnIndex)) {
/* 452:506 */         return ci;
/* 453:    */       }
/* 454:    */     }
/* 455:509 */     return null;
/* 456:    */   }
/* 457:    */   
/* 458:    */   public int getMaxOutlineLevel()
/* 459:    */   {
/* 460:512 */     int result = 0;
/* 461:513 */     int count = this.records.size();
/* 462:514 */     for (int i = 0; i < count; i++)
/* 463:    */     {
/* 464:515 */       ColumnInfoRecord columnInfoRecord = getColInfo(i);
/* 465:516 */       result = Math.max(columnInfoRecord.getOutlineLevel(), result);
/* 466:    */     }
/* 467:518 */     return result;
/* 468:    */   }
/* 469:    */   
/* 470:    */   public int getOutlineLevel(int columnIndex)
/* 471:    */   {
/* 472:521 */     ColumnInfoRecord ci = findColumnInfo(columnIndex);
/* 473:522 */     if (ci != null) {
/* 474:523 */       return ci.getOutlineLevel();
/* 475:    */     }
/* 476:525 */     return 0;
/* 477:    */   }
/* 478:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate

 * JD-Core Version:    0.7.0.1

 */