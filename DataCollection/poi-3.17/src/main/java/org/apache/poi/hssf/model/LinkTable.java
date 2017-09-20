/*   1:    */ package org.apache.poi.hssf.model;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.hssf.record.CRNCountRecord;
/*   8:    */ import org.apache.poi.hssf.record.CRNRecord;
/*   9:    */ import org.apache.poi.hssf.record.ExternSheetRecord;
/*  10:    */ import org.apache.poi.hssf.record.ExternalNameRecord;
/*  11:    */ import org.apache.poi.hssf.record.NameCommentRecord;
/*  12:    */ import org.apache.poi.hssf.record.NameRecord;
/*  13:    */ import org.apache.poi.hssf.record.Record;
/*  14:    */ import org.apache.poi.hssf.record.SupBookRecord;
/*  15:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*  16:    */ import org.apache.poi.ss.formula.ptg.ErrPtg;
/*  17:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  18:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  19:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  20:    */ 
/*  21:    */ final class LinkTable
/*  22:    */ {
/*  23:    */   private ExternalBookBlock[] _externalBookBlocks;
/*  24:    */   private final ExternSheetRecord _externSheetRecord;
/*  25:    */   private final List<NameRecord> _definedNames;
/*  26:    */   private final int _recordCount;
/*  27:    */   private final WorkbookRecordList _workbookRecordList;
/*  28:    */   
/*  29:    */   private static final class CRNBlock
/*  30:    */   {
/*  31:    */     private final CRNCountRecord _countRecord;
/*  32:    */     private final CRNRecord[] _crns;
/*  33:    */     
/*  34:    */     public CRNBlock(RecordStream rs)
/*  35:    */     {
/*  36: 79 */       this._countRecord = ((CRNCountRecord)rs.getNext());
/*  37: 80 */       int nCRNs = this._countRecord.getNumberOfCRNs();
/*  38: 81 */       CRNRecord[] crns = new CRNRecord[nCRNs];
/*  39: 82 */       for (int i = 0; i < crns.length; i++) {
/*  40: 83 */         crns[i] = ((CRNRecord)rs.getNext());
/*  41:    */       }
/*  42: 85 */       this._crns = crns;
/*  43:    */     }
/*  44:    */     
/*  45:    */     public CRNRecord[] getCrns()
/*  46:    */     {
/*  47: 88 */       return (CRNRecord[])this._crns.clone();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static final class ExternalBookBlock
/*  52:    */   {
/*  53:    */     private final SupBookRecord _externalBookRecord;
/*  54:    */     private ExternalNameRecord[] _externalNameRecords;
/*  55:    */     private final CRNBlock[] _crnBlocks;
/*  56:    */     
/*  57:    */     public ExternalBookBlock(RecordStream rs)
/*  58:    */     {
/*  59: 98 */       this._externalBookRecord = ((SupBookRecord)rs.getNext());
/*  60: 99 */       List<Object> temp = new ArrayList();
/*  61:100 */       while (rs.peekNextClass() == ExternalNameRecord.class) {
/*  62:101 */         temp.add(rs.getNext());
/*  63:    */       }
/*  64:103 */       this._externalNameRecords = new ExternalNameRecord[temp.size()];
/*  65:104 */       temp.toArray(this._externalNameRecords);
/*  66:    */       
/*  67:106 */       temp.clear();
/*  68:108 */       while (rs.peekNextClass() == CRNCountRecord.class) {
/*  69:109 */         temp.add(new CRNBlock(rs));
/*  70:    */       }
/*  71:111 */       this._crnBlocks = new CRNBlock[temp.size()];
/*  72:112 */       temp.toArray(this._crnBlocks);
/*  73:    */     }
/*  74:    */     
/*  75:    */     public ExternalBookBlock(String url, String[] sheetNames)
/*  76:    */     {
/*  77:119 */       this._externalBookRecord = SupBookRecord.createExternalReferences(url, sheetNames);
/*  78:120 */       this._crnBlocks = new CRNBlock[0];
/*  79:    */     }
/*  80:    */     
/*  81:    */     public ExternalBookBlock(int numberOfSheets)
/*  82:    */     {
/*  83:129 */       this._externalBookRecord = SupBookRecord.createInternalReferences((short)numberOfSheets);
/*  84:130 */       this._externalNameRecords = new ExternalNameRecord[0];
/*  85:131 */       this._crnBlocks = new CRNBlock[0];
/*  86:    */     }
/*  87:    */     
/*  88:    */     public ExternalBookBlock()
/*  89:    */     {
/*  90:140 */       this._externalBookRecord = SupBookRecord.createAddInFunctions();
/*  91:141 */       this._externalNameRecords = new ExternalNameRecord[0];
/*  92:142 */       this._crnBlocks = new CRNBlock[0];
/*  93:    */     }
/*  94:    */     
/*  95:    */     public SupBookRecord getExternalBookRecord()
/*  96:    */     {
/*  97:146 */       return this._externalBookRecord;
/*  98:    */     }
/*  99:    */     
/* 100:    */     public String getNameText(int definedNameIndex)
/* 101:    */     {
/* 102:150 */       return this._externalNameRecords[definedNameIndex].getText();
/* 103:    */     }
/* 104:    */     
/* 105:    */     public int getNameIx(int definedNameIndex)
/* 106:    */     {
/* 107:154 */       return this._externalNameRecords[definedNameIndex].getIx();
/* 108:    */     }
/* 109:    */     
/* 110:    */     public int getIndexOfName(String name)
/* 111:    */     {
/* 112:162 */       for (int i = 0; i < this._externalNameRecords.length; i++) {
/* 113:163 */         if (this._externalNameRecords[i].getText().equalsIgnoreCase(name)) {
/* 114:164 */           return i;
/* 115:    */         }
/* 116:    */       }
/* 117:167 */       return -1;
/* 118:    */     }
/* 119:    */     
/* 120:    */     public int getNumberOfNames()
/* 121:    */     {
/* 122:171 */       return this._externalNameRecords.length;
/* 123:    */     }
/* 124:    */     
/* 125:    */     public int addExternalName(ExternalNameRecord rec)
/* 126:    */     {
/* 127:175 */       ExternalNameRecord[] tmp = new ExternalNameRecord[this._externalNameRecords.length + 1];
/* 128:176 */       System.arraycopy(this._externalNameRecords, 0, tmp, 0, this._externalNameRecords.length);
/* 129:177 */       tmp[(tmp.length - 1)] = rec;
/* 130:178 */       this._externalNameRecords = tmp;
/* 131:179 */       return this._externalNameRecords.length - 1;
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public LinkTable(List<Record> inputList, int startIndex, WorkbookRecordList workbookRecordList, Map<String, NameCommentRecord> commentRecords)
/* 136:    */   {
/* 137:191 */     this._workbookRecordList = workbookRecordList;
/* 138:192 */     RecordStream rs = new RecordStream(inputList, startIndex);
/* 139:    */     
/* 140:194 */     List<ExternalBookBlock> temp = new ArrayList();
/* 141:195 */     while (rs.peekNextClass() == SupBookRecord.class) {
/* 142:196 */       temp.add(new ExternalBookBlock(rs));
/* 143:    */     }
/* 144:199 */     this._externalBookBlocks = new ExternalBookBlock[temp.size()];
/* 145:200 */     temp.toArray(this._externalBookBlocks);
/* 146:201 */     temp.clear();
/* 147:203 */     if (this._externalBookBlocks.length > 0)
/* 148:    */     {
/* 149:205 */       if (rs.peekNextClass() != ExternSheetRecord.class) {
/* 150:207 */         this._externSheetRecord = null;
/* 151:    */       } else {
/* 152:209 */         this._externSheetRecord = readExtSheetRecord(rs);
/* 153:    */       }
/* 154:    */     }
/* 155:    */     else {
/* 156:212 */       this._externSheetRecord = null;
/* 157:    */     }
/* 158:215 */     this._definedNames = new ArrayList();
/* 159:    */     for (;;)
/* 160:    */     {
/* 161:219 */       Class<? extends Record> nextClass = rs.peekNextClass();
/* 162:220 */       if (nextClass == NameRecord.class)
/* 163:    */       {
/* 164:221 */         NameRecord nr = (NameRecord)rs.getNext();
/* 165:222 */         this._definedNames.add(nr);
/* 166:    */       }
/* 167:    */       else
/* 168:    */       {
/* 169:224 */         if (nextClass != NameCommentRecord.class) {
/* 170:    */           break;
/* 171:    */         }
/* 172:225 */         NameCommentRecord ncr = (NameCommentRecord)rs.getNext();
/* 173:226 */         commentRecords.put(ncr.getNameText(), ncr);
/* 174:    */       }
/* 175:    */     }
/* 176:233 */     this._recordCount = rs.getCountRead();
/* 177:234 */     this._workbookRecordList.getRecords().addAll(inputList.subList(startIndex, startIndex + this._recordCount));
/* 178:    */   }
/* 179:    */   
/* 180:    */   private static ExternSheetRecord readExtSheetRecord(RecordStream rs)
/* 181:    */   {
/* 182:238 */     List<ExternSheetRecord> temp = new ArrayList(2);
/* 183:239 */     while (rs.peekNextClass() == ExternSheetRecord.class) {
/* 184:240 */       temp.add((ExternSheetRecord)rs.getNext());
/* 185:    */     }
/* 186:243 */     int nItems = temp.size();
/* 187:244 */     if (nItems < 1) {
/* 188:245 */       throw new RuntimeException("Expected an EXTERNSHEET record but got (" + rs.peekNextClass().getName() + ")");
/* 189:    */     }
/* 190:248 */     if (nItems == 1) {
/* 191:250 */       return (ExternSheetRecord)temp.get(0);
/* 192:    */     }
/* 193:254 */     ExternSheetRecord[] esrs = new ExternSheetRecord[nItems];
/* 194:255 */     temp.toArray(esrs);
/* 195:256 */     return ExternSheetRecord.combine(esrs);
/* 196:    */   }
/* 197:    */   
/* 198:    */   public LinkTable(int numberOfSheets, WorkbookRecordList workbookRecordList)
/* 199:    */   {
/* 200:260 */     this._workbookRecordList = workbookRecordList;
/* 201:261 */     this._definedNames = new ArrayList();
/* 202:262 */     this._externalBookBlocks = new ExternalBookBlock[] { new ExternalBookBlock(numberOfSheets) };
/* 203:    */     
/* 204:    */ 
/* 205:265 */     this._externSheetRecord = new ExternSheetRecord();
/* 206:266 */     this._recordCount = 2;
/* 207:    */     
/* 208:    */ 
/* 209:    */ 
/* 210:270 */     SupBookRecord supbook = this._externalBookBlocks[0].getExternalBookRecord();
/* 211:    */     
/* 212:272 */     int idx = findFirstRecordLocBySid((short)140);
/* 213:273 */     if (idx < 0) {
/* 214:274 */       throw new RuntimeException("CountryRecord not found");
/* 215:    */     }
/* 216:276 */     this._workbookRecordList.add(idx + 1, this._externSheetRecord);
/* 217:277 */     this._workbookRecordList.add(idx + 1, supbook);
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getRecordCount()
/* 221:    */   {
/* 222:284 */     return this._recordCount;
/* 223:    */   }
/* 224:    */   
/* 225:    */   public NameRecord getSpecificBuiltinRecord(byte builtInCode, int sheetNumber)
/* 226:    */   {
/* 227:293 */     Iterator<NameRecord> iterator = this._definedNames.iterator();
/* 228:294 */     while (iterator.hasNext())
/* 229:    */     {
/* 230:295 */       NameRecord record = (NameRecord)iterator.next();
/* 231:298 */       if ((record.getBuiltInName() == builtInCode) && (record.getSheetNumber() == sheetNumber)) {
/* 232:299 */         return record;
/* 233:    */       }
/* 234:    */     }
/* 235:303 */     return null;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void removeBuiltinRecord(byte name, int sheetIndex)
/* 239:    */   {
/* 240:309 */     NameRecord record = getSpecificBuiltinRecord(name, sheetIndex);
/* 241:310 */     if (record != null) {
/* 242:311 */       this._definedNames.remove(record);
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   public int getNumNames()
/* 247:    */   {
/* 248:317 */     return this._definedNames.size();
/* 249:    */   }
/* 250:    */   
/* 251:    */   public NameRecord getNameRecord(int index)
/* 252:    */   {
/* 253:321 */     return (NameRecord)this._definedNames.get(index);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void addName(NameRecord name)
/* 257:    */   {
/* 258:325 */     this._definedNames.add(name);
/* 259:    */     
/* 260:    */ 
/* 261:    */ 
/* 262:329 */     int idx = findFirstRecordLocBySid((short)23);
/* 263:330 */     if (idx == -1) {
/* 264:330 */       idx = findFirstRecordLocBySid((short)430);
/* 265:    */     }
/* 266:331 */     if (idx == -1) {
/* 267:331 */       idx = findFirstRecordLocBySid((short)140);
/* 268:    */     }
/* 269:332 */     int countNames = this._definedNames.size();
/* 270:333 */     this._workbookRecordList.add(idx + countNames, name);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void removeName(int namenum)
/* 274:    */   {
/* 275:337 */     this._definedNames.remove(namenum);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public boolean nameAlreadyExists(NameRecord name)
/* 279:    */   {
/* 280:346 */     for (int i = getNumNames() - 1; i >= 0; i--)
/* 281:    */     {
/* 282:347 */       NameRecord rec = getNameRecord(i);
/* 283:348 */       if ((rec != name) && 
/* 284:349 */         (isDuplicatedNames(name, rec))) {
/* 285:350 */         return true;
/* 286:    */       }
/* 287:    */     }
/* 288:353 */     return false;
/* 289:    */   }
/* 290:    */   
/* 291:    */   private static boolean isDuplicatedNames(NameRecord firstName, NameRecord lastName)
/* 292:    */   {
/* 293:357 */     return (lastName.getNameText().equalsIgnoreCase(firstName.getNameText())) && (isSameSheetNames(firstName, lastName));
/* 294:    */   }
/* 295:    */   
/* 296:    */   private static boolean isSameSheetNames(NameRecord firstName, NameRecord lastName)
/* 297:    */   {
/* 298:361 */     return lastName.getSheetNumber() == firstName.getSheetNumber();
/* 299:    */   }
/* 300:    */   
/* 301:    */   public String[] getExternalBookAndSheetName(int extRefIndex)
/* 302:    */   {
/* 303:365 */     int ebIx = this._externSheetRecord.getExtbookIndexFromRefIndex(extRefIndex);
/* 304:366 */     SupBookRecord ebr = this._externalBookBlocks[ebIx].getExternalBookRecord();
/* 305:367 */     if (!ebr.isExternalReferences()) {
/* 306:368 */       return null;
/* 307:    */     }
/* 308:371 */     int shIx1 = this._externSheetRecord.getFirstSheetIndexFromRefIndex(extRefIndex);
/* 309:372 */     int shIx2 = this._externSheetRecord.getLastSheetIndexFromRefIndex(extRefIndex);
/* 310:373 */     String firstSheetName = null;
/* 311:374 */     String lastSheetName = null;
/* 312:375 */     if (shIx1 >= 0) {
/* 313:376 */       firstSheetName = ebr.getSheetNames()[shIx1];
/* 314:    */     }
/* 315:378 */     if (shIx2 >= 0) {
/* 316:379 */       lastSheetName = ebr.getSheetNames()[shIx2];
/* 317:    */     }
/* 318:381 */     if (shIx1 == shIx2) {
/* 319:382 */       return new String[] { ebr.getURL(), firstSheetName };
/* 320:    */     }
/* 321:387 */     return new String[] { ebr.getURL(), firstSheetName, lastSheetName };
/* 322:    */   }
/* 323:    */   
/* 324:    */   private int getExternalWorkbookIndex(String workbookName)
/* 325:    */   {
/* 326:396 */     for (int i = 0; i < this._externalBookBlocks.length; i++)
/* 327:    */     {
/* 328:397 */       SupBookRecord ebr = this._externalBookBlocks[i].getExternalBookRecord();
/* 329:398 */       if (ebr.isExternalReferences()) {
/* 330:401 */         if (workbookName.equals(ebr.getURL())) {
/* 331:402 */           return i;
/* 332:    */         }
/* 333:    */       }
/* 334:    */     }
/* 335:405 */     return -1;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public int linkExternalWorkbook(String name, Workbook externalWorkbook)
/* 339:    */   {
/* 340:409 */     int extBookIndex = getExternalWorkbookIndex(name);
/* 341:410 */     if (extBookIndex != -1) {
/* 342:412 */       return extBookIndex;
/* 343:    */     }
/* 344:416 */     String[] sheetNames = new String[externalWorkbook.getNumberOfSheets()];
/* 345:417 */     for (int sn = 0; sn < sheetNames.length; sn++) {
/* 346:418 */       sheetNames[sn] = externalWorkbook.getSheetName(sn);
/* 347:    */     }
/* 348:420 */     String url = "" + name;
/* 349:421 */     ExternalBookBlock block = new ExternalBookBlock(url, sheetNames);
/* 350:    */     
/* 351:    */ 
/* 352:424 */     extBookIndex = extendExternalBookBlocks(block);
/* 353:    */     
/* 354:    */ 
/* 355:427 */     int idx = findFirstRecordLocBySid((short)23);
/* 356:428 */     if (idx == -1) {
/* 357:429 */       idx = this._workbookRecordList.size();
/* 358:    */     }
/* 359:431 */     this._workbookRecordList.add(idx, block.getExternalBookRecord());
/* 360:434 */     for (int sn = 0; sn < sheetNames.length; sn++) {
/* 361:435 */       this._externSheetRecord.addRef(extBookIndex, sn, sn);
/* 362:    */     }
/* 363:439 */     return extBookIndex;
/* 364:    */   }
/* 365:    */   
/* 366:    */   public int getExternalSheetIndex(String workbookName, String firstSheetName, String lastSheetName)
/* 367:    */   {
/* 368:443 */     int externalBookIndex = getExternalWorkbookIndex(workbookName);
/* 369:444 */     if (externalBookIndex == -1) {
/* 370:445 */       throw new RuntimeException("No external workbook with name '" + workbookName + "'");
/* 371:    */     }
/* 372:447 */     SupBookRecord ebrTarget = this._externalBookBlocks[externalBookIndex].getExternalBookRecord();
/* 373:    */     
/* 374:449 */     int firstSheetIndex = getSheetIndex(ebrTarget.getSheetNames(), firstSheetName);
/* 375:450 */     int lastSheetIndex = getSheetIndex(ebrTarget.getSheetNames(), lastSheetName);
/* 376:    */     
/* 377:    */ 
/* 378:453 */     int result = this._externSheetRecord.getRefIxForSheet(externalBookIndex, firstSheetIndex, lastSheetIndex);
/* 379:454 */     if (result < 0) {
/* 380:455 */       result = this._externSheetRecord.addRef(externalBookIndex, firstSheetIndex, lastSheetIndex);
/* 381:    */     }
/* 382:457 */     return result;
/* 383:    */   }
/* 384:    */   
/* 385:    */   private static int getSheetIndex(String[] sheetNames, String sheetName)
/* 386:    */   {
/* 387:461 */     for (int i = 0; i < sheetNames.length; i++) {
/* 388:462 */       if (sheetNames[i].equals(sheetName)) {
/* 389:463 */         return i;
/* 390:    */       }
/* 391:    */     }
/* 392:467 */     throw new RuntimeException("External workbook does not contain sheet '" + sheetName + "'");
/* 393:    */   }
/* 394:    */   
/* 395:    */   public int getFirstInternalSheetIndexForExtIndex(int extRefIndex)
/* 396:    */   {
/* 397:475 */     if ((extRefIndex >= this._externSheetRecord.getNumOfRefs()) || (extRefIndex < 0)) {
/* 398:476 */       return -1;
/* 399:    */     }
/* 400:478 */     return this._externSheetRecord.getFirstSheetIndexFromRefIndex(extRefIndex);
/* 401:    */   }
/* 402:    */   
/* 403:    */   public int getLastInternalSheetIndexForExtIndex(int extRefIndex)
/* 404:    */   {
/* 405:485 */     if ((extRefIndex >= this._externSheetRecord.getNumOfRefs()) || (extRefIndex < 0)) {
/* 406:486 */       return -1;
/* 407:    */     }
/* 408:488 */     return this._externSheetRecord.getLastSheetIndexFromRefIndex(extRefIndex);
/* 409:    */   }
/* 410:    */   
/* 411:    */   public void removeSheet(int sheetIdx)
/* 412:    */   {
/* 413:492 */     this._externSheetRecord.removeSheet(sheetIdx);
/* 414:    */   }
/* 415:    */   
/* 416:    */   public int checkExternSheet(int sheetIndex)
/* 417:    */   {
/* 418:496 */     return checkExternSheet(sheetIndex, sheetIndex);
/* 419:    */   }
/* 420:    */   
/* 421:    */   public int checkExternSheet(int firstSheetIndex, int lastSheetIndex)
/* 422:    */   {
/* 423:499 */     int thisWbIndex = -1;
/* 424:500 */     for (int i = 0; i < this._externalBookBlocks.length; i++)
/* 425:    */     {
/* 426:501 */       SupBookRecord ebr = this._externalBookBlocks[i].getExternalBookRecord();
/* 427:502 */       if (ebr.isInternalReferences())
/* 428:    */       {
/* 429:503 */         thisWbIndex = i;
/* 430:504 */         break;
/* 431:    */       }
/* 432:    */     }
/* 433:507 */     if (thisWbIndex < 0) {
/* 434:508 */       throw new RuntimeException("Could not find 'internal references' EXTERNALBOOK");
/* 435:    */     }
/* 436:512 */     int i = this._externSheetRecord.getRefIxForSheet(thisWbIndex, firstSheetIndex, lastSheetIndex);
/* 437:513 */     if (i >= 0) {
/* 438:514 */       return i;
/* 439:    */     }
/* 440:517 */     return this._externSheetRecord.addRef(thisWbIndex, firstSheetIndex, lastSheetIndex);
/* 441:    */   }
/* 442:    */   
/* 443:    */   private int findFirstRecordLocBySid(short sid)
/* 444:    */   {
/* 445:524 */     int index = 0;
/* 446:525 */     for (Record record : this._workbookRecordList.getRecords())
/* 447:    */     {
/* 448:526 */       if (record.getSid() == sid) {
/* 449:527 */         return index;
/* 450:    */       }
/* 451:529 */       index++;
/* 452:    */     }
/* 453:531 */     return -1;
/* 454:    */   }
/* 455:    */   
/* 456:    */   public String resolveNameXText(int refIndex, int definedNameIndex, InternalWorkbook workbook)
/* 457:    */   {
/* 458:535 */     int extBookIndex = this._externSheetRecord.getExtbookIndexFromRefIndex(refIndex);
/* 459:536 */     int firstTabIndex = this._externSheetRecord.getFirstSheetIndexFromRefIndex(refIndex);
/* 460:537 */     if (firstTabIndex == -1) {
/* 461:539 */       throw new RuntimeException("Referenced sheet could not be found");
/* 462:    */     }
/* 463:543 */     ExternalBookBlock externalBook = this._externalBookBlocks[extBookIndex];
/* 464:544 */     if (externalBook._externalNameRecords.length > definedNameIndex) {
/* 465:545 */       return this._externalBookBlocks[extBookIndex].getNameText(definedNameIndex);
/* 466:    */     }
/* 467:546 */     if (firstTabIndex == -2)
/* 468:    */     {
/* 469:548 */       NameRecord nr = getNameRecord(definedNameIndex);
/* 470:549 */       int sheetNumber = nr.getSheetNumber();
/* 471:    */       
/* 472:551 */       StringBuffer text = new StringBuffer();
/* 473:552 */       if (sheetNumber > 0)
/* 474:    */       {
/* 475:553 */         String sheetName = workbook.getSheetName(sheetNumber - 1);
/* 476:554 */         SheetNameFormatter.appendFormat(text, sheetName);
/* 477:555 */         text.append("!");
/* 478:    */       }
/* 479:557 */       text.append(nr.getNameText());
/* 480:558 */       return text.toString();
/* 481:    */     }
/* 482:560 */     throw new ArrayIndexOutOfBoundsException("Ext Book Index relative but beyond the supported length, was " + extBookIndex + " but maximum is " + this._externalBookBlocks.length);
/* 483:    */   }
/* 484:    */   
/* 485:    */   public int resolveNameXIx(int refIndex, int definedNameIndex)
/* 486:    */   {
/* 487:567 */     int extBookIndex = this._externSheetRecord.getExtbookIndexFromRefIndex(refIndex);
/* 488:568 */     return this._externalBookBlocks[extBookIndex].getNameIx(definedNameIndex);
/* 489:    */   }
/* 490:    */   
/* 491:    */   public NameXPtg getNameXPtg(String name, int sheetRefIndex)
/* 492:    */   {
/* 493:579 */     for (int i = 0; i < this._externalBookBlocks.length; i++)
/* 494:    */     {
/* 495:580 */       int definedNameIndex = this._externalBookBlocks[i].getIndexOfName(name);
/* 496:581 */       if (definedNameIndex >= 0)
/* 497:    */       {
/* 498:586 */         int thisSheetRefIndex = findRefIndexFromExtBookIndex(i);
/* 499:587 */         if (thisSheetRefIndex >= 0) {
/* 500:589 */           if ((sheetRefIndex == -1) || (thisSheetRefIndex == sheetRefIndex)) {
/* 501:590 */             return new NameXPtg(thisSheetRefIndex, definedNameIndex);
/* 502:    */           }
/* 503:    */         }
/* 504:    */       }
/* 505:    */     }
/* 506:594 */     return null;
/* 507:    */   }
/* 508:    */   
/* 509:    */   public NameXPtg addNameXPtg(String name)
/* 510:    */   {
/* 511:604 */     int extBlockIndex = -1;
/* 512:605 */     ExternalBookBlock extBlock = null;
/* 513:608 */     for (int i = 0; i < this._externalBookBlocks.length; i++)
/* 514:    */     {
/* 515:609 */       SupBookRecord ebr = this._externalBookBlocks[i].getExternalBookRecord();
/* 516:610 */       if (ebr.isAddInFunctions())
/* 517:    */       {
/* 518:611 */         extBlock = this._externalBookBlocks[i];
/* 519:612 */         extBlockIndex = i;
/* 520:613 */         break;
/* 521:    */       }
/* 522:    */     }
/* 523:617 */     if (extBlock == null)
/* 524:    */     {
/* 525:618 */       extBlock = new ExternalBookBlock();
/* 526:619 */       extBlockIndex = extendExternalBookBlocks(extBlock);
/* 527:    */       
/* 528:    */ 
/* 529:622 */       int idx = findFirstRecordLocBySid((short)23);
/* 530:623 */       this._workbookRecordList.add(idx, extBlock.getExternalBookRecord());
/* 531:    */       
/* 532:    */ 
/* 533:    */ 
/* 534:627 */       this._externSheetRecord.addRef(this._externalBookBlocks.length - 1, -2, -2);
/* 535:    */     }
/* 536:631 */     ExternalNameRecord extNameRecord = new ExternalNameRecord();
/* 537:632 */     extNameRecord.setText(name);
/* 538:    */     
/* 539:634 */     extNameRecord.setParsedExpression(new Ptg[] { ErrPtg.REF_INVALID });
/* 540:    */     
/* 541:636 */     int nameIndex = extBlock.addExternalName(extNameRecord);
/* 542:637 */     int supLinkIndex = 0;
/* 543:640 */     for (Record record : this._workbookRecordList.getRecords())
/* 544:    */     {
/* 545:641 */       if (((record instanceof SupBookRecord)) && (((SupBookRecord)record).isAddInFunctions())) {
/* 546:    */         break;
/* 547:    */       }
/* 548:644 */       supLinkIndex++;
/* 549:    */     }
/* 550:646 */     int numberOfNames = extBlock.getNumberOfNames();
/* 551:    */     
/* 552:648 */     this._workbookRecordList.add(supLinkIndex + numberOfNames, extNameRecord);
/* 553:649 */     int fakeSheetIdx = -2;
/* 554:650 */     int ix = this._externSheetRecord.getRefIxForSheet(extBlockIndex, fakeSheetIdx, fakeSheetIdx);
/* 555:651 */     return new NameXPtg(ix, nameIndex);
/* 556:    */   }
/* 557:    */   
/* 558:    */   private int extendExternalBookBlocks(ExternalBookBlock newBlock)
/* 559:    */   {
/* 560:654 */     ExternalBookBlock[] tmp = new ExternalBookBlock[this._externalBookBlocks.length + 1];
/* 561:655 */     System.arraycopy(this._externalBookBlocks, 0, tmp, 0, this._externalBookBlocks.length);
/* 562:656 */     tmp[(tmp.length - 1)] = newBlock;
/* 563:657 */     this._externalBookBlocks = tmp;
/* 564:    */     
/* 565:659 */     return this._externalBookBlocks.length - 1;
/* 566:    */   }
/* 567:    */   
/* 568:    */   private int findRefIndexFromExtBookIndex(int extBookIndex)
/* 569:    */   {
/* 570:663 */     return this._externSheetRecord.findRefIndexFromExtBookIndex(extBookIndex);
/* 571:    */   }
/* 572:    */   
/* 573:    */   public boolean changeExternalReference(String oldUrl, String newUrl)
/* 574:    */   {
/* 575:676 */     for (ExternalBookBlock ex : this._externalBookBlocks)
/* 576:    */     {
/* 577:677 */       SupBookRecord externalRecord = ex.getExternalBookRecord();
/* 578:678 */       if ((externalRecord.isExternalReferences()) && (externalRecord.getURL().equals(oldUrl)))
/* 579:    */       {
/* 580:681 */         externalRecord.setURL(newUrl);
/* 581:682 */         return true;
/* 582:    */       }
/* 583:    */     }
/* 584:685 */     return false;
/* 585:    */   }
/* 586:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.model.LinkTable

 * JD-Core Version:    0.7.0.1

 */