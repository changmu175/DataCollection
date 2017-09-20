/*    1:     */ package org.apache.poi.hssf.model;
/*    2:     */ 
/*    3:     */ import java.security.AccessControlException;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.LinkedHashMap;
/*    6:     */ import java.util.List;
/*    7:     */ import java.util.Locale;
/*    8:     */ import java.util.Map;
/*    9:     */ import java.util.Map.Entry;
/*   10:     */ import org.apache.poi.ddf.EscherBSERecord;
/*   11:     */ import org.apache.poi.ddf.EscherBoolProperty;
/*   12:     */ import org.apache.poi.ddf.EscherContainerRecord;
/*   13:     */ import org.apache.poi.ddf.EscherDgRecord;
/*   14:     */ import org.apache.poi.ddf.EscherDggRecord;
/*   15:     */ import org.apache.poi.ddf.EscherDggRecord.FileIdCluster;
/*   16:     */ import org.apache.poi.ddf.EscherOptRecord;
/*   17:     */ import org.apache.poi.ddf.EscherRGBProperty;
/*   18:     */ import org.apache.poi.ddf.EscherRecord;
/*   19:     */ import org.apache.poi.ddf.EscherSimpleProperty;
/*   20:     */ import org.apache.poi.ddf.EscherSpRecord;
/*   21:     */ import org.apache.poi.ddf.EscherSplitMenuColorsRecord;
/*   22:     */ import org.apache.poi.hssf.record.BOFRecord;
/*   23:     */ import org.apache.poi.hssf.record.BackupRecord;
/*   24:     */ import org.apache.poi.hssf.record.BookBoolRecord;
/*   25:     */ import org.apache.poi.hssf.record.BoundSheetRecord;
/*   26:     */ import org.apache.poi.hssf.record.CodepageRecord;
/*   27:     */ import org.apache.poi.hssf.record.CountryRecord;
/*   28:     */ import org.apache.poi.hssf.record.DSFRecord;
/*   29:     */ import org.apache.poi.hssf.record.DateWindow1904Record;
/*   30:     */ import org.apache.poi.hssf.record.DrawingGroupRecord;
/*   31:     */ import org.apache.poi.hssf.record.EOFRecord;
/*   32:     */ import org.apache.poi.hssf.record.EscherAggregate;
/*   33:     */ import org.apache.poi.hssf.record.ExtSSTRecord;
/*   34:     */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*   35:     */ import org.apache.poi.hssf.record.FileSharingRecord;
/*   36:     */ import org.apache.poi.hssf.record.FnGroupCountRecord;
/*   37:     */ import org.apache.poi.hssf.record.FontRecord;
/*   38:     */ import org.apache.poi.hssf.record.FormatRecord;
/*   39:     */ import org.apache.poi.hssf.record.HideObjRecord;
/*   40:     */ import org.apache.poi.hssf.record.HyperlinkRecord;
/*   41:     */ import org.apache.poi.hssf.record.InterfaceEndRecord;
/*   42:     */ import org.apache.poi.hssf.record.InterfaceHdrRecord;
/*   43:     */ import org.apache.poi.hssf.record.MMSRecord;
/*   44:     */ import org.apache.poi.hssf.record.NameCommentRecord;
/*   45:     */ import org.apache.poi.hssf.record.NameRecord;
/*   46:     */ import org.apache.poi.hssf.record.PaletteRecord;
/*   47:     */ import org.apache.poi.hssf.record.PasswordRecord;
/*   48:     */ import org.apache.poi.hssf.record.PasswordRev4Record;
/*   49:     */ import org.apache.poi.hssf.record.PrecisionRecord;
/*   50:     */ import org.apache.poi.hssf.record.ProtectRecord;
/*   51:     */ import org.apache.poi.hssf.record.ProtectionRev4Record;
/*   52:     */ import org.apache.poi.hssf.record.RecalcIdRecord;
/*   53:     */ import org.apache.poi.hssf.record.Record;
/*   54:     */ import org.apache.poi.hssf.record.RefreshAllRecord;
/*   55:     */ import org.apache.poi.hssf.record.SSTRecord;
/*   56:     */ import org.apache.poi.hssf.record.StyleRecord;
/*   57:     */ import org.apache.poi.hssf.record.TabIdRecord;
/*   58:     */ import org.apache.poi.hssf.record.UseSelFSRecord;
/*   59:     */ import org.apache.poi.hssf.record.WindowOneRecord;
/*   60:     */ import org.apache.poi.hssf.record.WindowProtectRecord;
/*   61:     */ import org.apache.poi.hssf.record.WriteAccessRecord;
/*   62:     */ import org.apache.poi.hssf.record.WriteProtectRecord;
/*   63:     */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   64:     */ import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
/*   65:     */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*   66:     */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
/*   67:     */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
/*   68:     */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
/*   69:     */ import org.apache.poi.ss.formula.FormulaShifter;
/*   70:     */ import org.apache.poi.ss.formula.ptg.Area3DPtg;
/*   71:     */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*   72:     */ import org.apache.poi.ss.formula.ptg.OperandPtg;
/*   73:     */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   74:     */ import org.apache.poi.ss.formula.ptg.Ref3DPtg;
/*   75:     */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*   76:     */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*   77:     */ import org.apache.poi.ss.usermodel.SheetVisibility;
/*   78:     */ import org.apache.poi.ss.usermodel.Workbook;
/*   79:     */ import org.apache.poi.util.Internal;
/*   80:     */ import org.apache.poi.util.LocaleUtil;
/*   81:     */ import org.apache.poi.util.POILogFactory;
/*   82:     */ import org.apache.poi.util.POILogger;
/*   83:     */ import org.apache.poi.util.RecordFormatException;
/*   84:     */ 
/*   85:     */ @Internal
/*   86:     */ public final class InternalWorkbook
/*   87:     */ {
/*   88:     */   private static final int MAX_SENSITIVE_SHEET_NAME_LEN = 31;
/*   89: 135 */   public static final String[] WORKBOOK_DIR_ENTRY_NAMES = { "Workbook", "WORKBOOK", "BOOK" };
/*   90:     */   public static final String OLD_WORKBOOK_DIR_ENTRY_NAME = "Book";
/*   91: 147 */   private static final POILogger LOG = POILogFactory.getLogger(InternalWorkbook.class);
/*   92:     */   private static final short CODEPAGE = 1200;
/*   93:     */   private final WorkbookRecordList records;
/*   94:     */   protected SSTRecord sst;
/*   95:     */   private LinkTable linkTable;
/*   96:     */   private final List<BoundSheetRecord> boundsheets;
/*   97:     */   private final List<FormatRecord> formats;
/*   98:     */   private final List<HyperlinkRecord> hyperlinks;
/*   99:     */   private int numxfs;
/*  100:     */   private int numfonts;
/*  101:     */   private int maxformatid;
/*  102:     */   private boolean uses1904datewindowing;
/*  103:     */   private DrawingManager2 drawingManager;
/*  104:     */   private List<EscherBSERecord> escherBSERecords;
/*  105:     */   private WindowOneRecord windowOne;
/*  106:     */   private FileSharingRecord fileShare;
/*  107:     */   private WriteAccessRecord writeAccess;
/*  108:     */   private WriteProtectRecord writeProtect;
/*  109:     */   private final Map<String, NameCommentRecord> commentRecords;
/*  110:     */   
/*  111:     */   private InternalWorkbook()
/*  112:     */   {
/*  113: 198 */     this.records = new WorkbookRecordList();
/*  114:     */     
/*  115: 200 */     this.boundsheets = new ArrayList();
/*  116: 201 */     this.formats = new ArrayList();
/*  117: 202 */     this.hyperlinks = new ArrayList();
/*  118: 203 */     this.numxfs = 0;
/*  119: 204 */     this.numfonts = 0;
/*  120: 205 */     this.maxformatid = -1;
/*  121: 206 */     this.uses1904datewindowing = false;
/*  122: 207 */     this.escherBSERecords = new ArrayList();
/*  123: 208 */     this.commentRecords = new LinkedHashMap();
/*  124:     */   }
/*  125:     */   
/*  126:     */   public static InternalWorkbook createWorkbook(List<Record> recs)
/*  127:     */   {
/*  128: 224 */     LOG.log(1, new Object[] { "Workbook (readfile) created with reclen=", Integer.valueOf(recs.size()) });
/*  129: 225 */     InternalWorkbook retval = new InternalWorkbook();
/*  130: 226 */     List<Record> records = new ArrayList(recs.size() / 3);
/*  131: 227 */     retval.records.setRecords(records);
/*  132:     */     
/*  133: 229 */     boolean eofPassed = false;
/*  134: 230 */     for (int k = 0; k < recs.size(); k++)
/*  135:     */     {
/*  136: 231 */       Record rec = (Record)recs.get(k);
/*  137:     */       String logObj;
/*  138: 233 */       switch (rec.getSid())
/*  139:     */       {
/*  140:     */       case 10: 
/*  141: 236 */         logObj = "workbook eof";
/*  142: 237 */         break;
/*  143:     */       case 133: 
/*  144: 240 */         logObj = "boundsheet";
/*  145: 241 */         retval.boundsheets.add((BoundSheetRecord)rec);
/*  146: 242 */         retval.records.setBspos(k);
/*  147: 243 */         break;
/*  148:     */       case 252: 
/*  149: 246 */         logObj = "sst";
/*  150: 247 */         retval.sst = ((SSTRecord)rec);
/*  151: 248 */         break;
/*  152:     */       case 49: 
/*  153: 251 */         logObj = "font";
/*  154: 252 */         retval.records.setFontpos(k);
/*  155: 253 */         retval.numfonts += 1;
/*  156: 254 */         break;
/*  157:     */       case 224: 
/*  158: 257 */         logObj = "XF";
/*  159: 258 */         retval.records.setXfpos(k);
/*  160: 259 */         retval.numxfs += 1;
/*  161: 260 */         break;
/*  162:     */       case 317: 
/*  163: 263 */         logObj = "tabid";
/*  164: 264 */         retval.records.setTabpos(k);
/*  165: 265 */         break;
/*  166:     */       case 18: 
/*  167: 268 */         logObj = "protect";
/*  168: 269 */         retval.records.setProtpos(k);
/*  169: 270 */         break;
/*  170:     */       case 64: 
/*  171: 273 */         logObj = "backup";
/*  172: 274 */         retval.records.setBackuppos(k);
/*  173: 275 */         break;
/*  174:     */       case 23: 
/*  175: 278 */         throw new RecordFormatException("Extern sheet is part of LinkTable");
/*  176:     */       case 24: 
/*  177:     */       case 430: 
/*  178: 283 */         LOG.log(1, new Object[] { "found SupBook record at " + k });
/*  179: 284 */         retval.linkTable = new LinkTable(recs, k, retval.records, retval.commentRecords);
/*  180: 285 */         k += retval.linkTable.getRecordCount() - 1;
/*  181: 286 */         break;
/*  182:     */       case 1054: 
/*  183: 289 */         logObj = "format";
/*  184: 290 */         FormatRecord fr = (FormatRecord)rec;
/*  185: 291 */         retval.formats.add(fr);
/*  186: 292 */         retval.maxformatid = (retval.maxformatid >= fr.getIndexCode() ? retval.maxformatid : fr.getIndexCode());
/*  187: 293 */         break;
/*  188:     */       case 34: 
/*  189: 296 */         logObj = "datewindow1904";
/*  190: 297 */         retval.uses1904datewindowing = (((DateWindow1904Record)rec).getWindowing() == 1);
/*  191: 298 */         break;
/*  192:     */       case 146: 
/*  193: 301 */         logObj = "palette";
/*  194: 302 */         retval.records.setPalettepos(k);
/*  195: 303 */         break;
/*  196:     */       case 61: 
/*  197: 306 */         logObj = "WindowOneRecord";
/*  198: 307 */         retval.windowOne = ((WindowOneRecord)rec);
/*  199: 308 */         break;
/*  200:     */       case 92: 
/*  201: 311 */         logObj = "WriteAccess";
/*  202: 312 */         retval.writeAccess = ((WriteAccessRecord)rec);
/*  203: 313 */         break;
/*  204:     */       case 134: 
/*  205: 316 */         logObj = "WriteProtect";
/*  206: 317 */         retval.writeProtect = ((WriteProtectRecord)rec);
/*  207: 318 */         break;
/*  208:     */       case 91: 
/*  209: 321 */         logObj = "FileSharing";
/*  210: 322 */         retval.fileShare = ((FileSharingRecord)rec);
/*  211: 323 */         break;
/*  212:     */       case 2196: 
/*  213: 326 */         logObj = "NameComment";
/*  214: 327 */         NameCommentRecord ncr = (NameCommentRecord)rec;
/*  215: 328 */         retval.commentRecords.put(ncr.getNameText(), ncr);
/*  216: 329 */         break;
/*  217:     */       case 440: 
/*  218: 333 */         logObj = "Hyperlink";
/*  219: 334 */         retval.hyperlinks.add((HyperlinkRecord)rec);
/*  220: 335 */         break;
/*  221:     */       default: 
/*  222: 338 */         logObj = "(sid=" + rec.getSid() + ")";
/*  223:     */       }
/*  224: 341 */       if (!eofPassed) {
/*  225: 342 */         records.add(rec);
/*  226:     */       }
/*  227: 344 */       LOG.log(1, new Object[] { "found " + logObj + " record at " + k });
/*  228: 345 */       if (rec.getSid() == 10) {
/*  229: 346 */         eofPassed = true;
/*  230:     */       }
/*  231:     */     }
/*  232: 355 */     if (retval.windowOne == null) {
/*  233: 356 */       retval.windowOne = createWindowOne();
/*  234:     */     }
/*  235: 358 */     LOG.log(1, new Object[] { "exit create workbook from existing file function" });
/*  236: 359 */     return retval;
/*  237:     */   }
/*  238:     */   
/*  239:     */   public static InternalWorkbook createWorkbook()
/*  240:     */   {
/*  241: 369 */     LOG.log(1, new Object[] { "creating new workbook from scratch" });
/*  242:     */     
/*  243: 371 */     InternalWorkbook retval = new InternalWorkbook();
/*  244: 372 */     List<Record> records = new ArrayList(30);
/*  245: 373 */     retval.records.setRecords(records);
/*  246: 374 */     List<FormatRecord> formats = retval.formats;
/*  247:     */     
/*  248: 376 */     records.add(createBOF());
/*  249: 377 */     records.add(new InterfaceHdrRecord(1200));
/*  250: 378 */     records.add(createMMS());
/*  251: 379 */     records.add(InterfaceEndRecord.instance);
/*  252: 380 */     records.add(createWriteAccess());
/*  253: 381 */     records.add(createCodepage());
/*  254: 382 */     records.add(createDSF());
/*  255: 383 */     records.add(createTabId());
/*  256: 384 */     retval.records.setTabpos(records.size() - 1);
/*  257: 385 */     records.add(createFnGroupCount());
/*  258: 386 */     records.add(createWindowProtect());
/*  259: 387 */     records.add(createProtect());
/*  260: 388 */     retval.records.setProtpos(records.size() - 1);
/*  261: 389 */     records.add(createPassword());
/*  262: 390 */     records.add(createProtectionRev4());
/*  263: 391 */     records.add(createPasswordRev4());
/*  264: 392 */     retval.windowOne = createWindowOne();
/*  265: 393 */     records.add(retval.windowOne);
/*  266: 394 */     records.add(createBackup());
/*  267: 395 */     retval.records.setBackuppos(records.size() - 1);
/*  268: 396 */     records.add(createHideObj());
/*  269: 397 */     records.add(createDateWindow1904());
/*  270: 398 */     records.add(createPrecision());
/*  271: 399 */     records.add(createRefreshAll());
/*  272: 400 */     records.add(createBookBool());
/*  273: 401 */     records.add(createFont());
/*  274: 402 */     records.add(createFont());
/*  275: 403 */     records.add(createFont());
/*  276: 404 */     records.add(createFont());
/*  277: 405 */     retval.records.setFontpos(records.size() - 1);
/*  278: 406 */     retval.numfonts = 4;
/*  279: 409 */     for (int i = 0; i <= 7; i++)
/*  280:     */     {
/*  281: 410 */       FormatRecord rec = createFormat(i);
/*  282: 411 */       retval.maxformatid = (retval.maxformatid >= rec.getIndexCode() ? retval.maxformatid : rec.getIndexCode());
/*  283: 412 */       formats.add(rec);
/*  284: 413 */       records.add(rec);
/*  285:     */     }
/*  286: 416 */     for (int k = 0; k < 21; k++)
/*  287:     */     {
/*  288: 417 */       records.add(createExtendedFormat(k));
/*  289: 418 */       retval.numxfs += 1;
/*  290:     */     }
/*  291: 420 */     retval.records.setXfpos(records.size() - 1);
/*  292: 421 */     for (int k = 0; k < 6; k++) {
/*  293: 422 */       records.add(createStyle(k));
/*  294:     */     }
/*  295: 424 */     records.add(createUseSelFS());
/*  296:     */     
/*  297: 426 */     int nBoundSheets = 1;
/*  298: 427 */     for (int k = 0; k < nBoundSheets; k++)
/*  299:     */     {
/*  300: 428 */       BoundSheetRecord bsr = createBoundSheet(k);
/*  301:     */       
/*  302: 430 */       records.add(bsr);
/*  303: 431 */       retval.boundsheets.add(bsr);
/*  304: 432 */       retval.records.setBspos(records.size() - 1);
/*  305:     */     }
/*  306: 434 */     records.add(createCountry());
/*  307: 435 */     for (int k = 0; k < nBoundSheets; k++) {
/*  308: 436 */       retval.getOrCreateLinkTable().checkExternSheet(k);
/*  309:     */     }
/*  310: 438 */     retval.sst = new SSTRecord();
/*  311: 439 */     records.add(retval.sst);
/*  312: 440 */     records.add(createExtendedSST());
/*  313:     */     
/*  314: 442 */     records.add(EOFRecord.instance);
/*  315: 443 */     LOG.log(1, new Object[] { "exit create new workbook from scratch" });
/*  316:     */     
/*  317: 445 */     return retval;
/*  318:     */   }
/*  319:     */   
/*  320:     */   public NameRecord getSpecificBuiltinRecord(byte name, int sheetNumber)
/*  321:     */   {
/*  322: 457 */     return getOrCreateLinkTable().getSpecificBuiltinRecord(name, sheetNumber);
/*  323:     */   }
/*  324:     */   
/*  325:     */   public void removeBuiltinRecord(byte name, int sheetIndex)
/*  326:     */   {
/*  327: 466 */     this.linkTable.removeBuiltinRecord(name, sheetIndex);
/*  328:     */   }
/*  329:     */   
/*  330:     */   public int getNumRecords()
/*  331:     */   {
/*  332: 471 */     return this.records.size();
/*  333:     */   }
/*  334:     */   
/*  335:     */   public FontRecord getFontRecordAt(int idx)
/*  336:     */   {
/*  337: 484 */     int index = idx;
/*  338: 486 */     if (index > 4) {
/*  339: 487 */       index--;
/*  340:     */     }
/*  341: 489 */     if (index > this.numfonts - 1) {
/*  342: 490 */       throw new ArrayIndexOutOfBoundsException("There are only " + this.numfonts + " font records, you asked for " + idx);
/*  343:     */     }
/*  344: 494 */     FontRecord retval = (FontRecord)this.records.get(this.records.getFontpos() - (this.numfonts - 1) + index);
/*  345:     */     
/*  346:     */ 
/*  347: 497 */     return retval;
/*  348:     */   }
/*  349:     */   
/*  350:     */   public int getFontIndex(FontRecord font)
/*  351:     */   {
/*  352: 510 */     for (int i = 0; i <= this.numfonts; i++)
/*  353:     */     {
/*  354: 511 */       FontRecord thisFont = (FontRecord)this.records.get(this.records.getFontpos() - (this.numfonts - 1) + i);
/*  355: 513 */       if (thisFont == font) {
/*  356: 515 */         return i > 3 ? i + 1 : i;
/*  357:     */       }
/*  358:     */     }
/*  359: 518 */     throw new IllegalArgumentException("Could not find that font!");
/*  360:     */   }
/*  361:     */   
/*  362:     */   public FontRecord createNewFont()
/*  363:     */   {
/*  364: 530 */     FontRecord rec = createFont();
/*  365:     */     
/*  366: 532 */     this.records.add(this.records.getFontpos() + 1, rec);
/*  367: 533 */     this.records.setFontpos(this.records.getFontpos() + 1);
/*  368: 534 */     this.numfonts += 1;
/*  369: 535 */     return rec;
/*  370:     */   }
/*  371:     */   
/*  372:     */   public void removeFontRecord(FontRecord rec)
/*  373:     */   {
/*  374: 547 */     this.records.remove(rec);
/*  375: 548 */     this.numfonts -= 1;
/*  376:     */   }
/*  377:     */   
/*  378:     */   public int getNumberOfFontRecords()
/*  379:     */   {
/*  380: 558 */     return this.numfonts;
/*  381:     */   }
/*  382:     */   
/*  383:     */   public void setSheetBof(int sheetIndex, int pos)
/*  384:     */   {
/*  385: 569 */     LOG.log(1, new Object[] { "setting bof for sheetnum =", Integer.valueOf(sheetIndex), " at pos=", Integer.valueOf(pos) });
/*  386:     */     
/*  387: 571 */     checkSheets(sheetIndex);
/*  388: 572 */     getBoundSheetRec(sheetIndex).setPositionOfBof(pos);
/*  389:     */   }
/*  390:     */   
/*  391:     */   private BoundSheetRecord getBoundSheetRec(int sheetIndex)
/*  392:     */   {
/*  393: 577 */     return (BoundSheetRecord)this.boundsheets.get(sheetIndex);
/*  394:     */   }
/*  395:     */   
/*  396:     */   public BackupRecord getBackupRecord()
/*  397:     */   {
/*  398: 586 */     return (BackupRecord)this.records.get(this.records.getBackuppos());
/*  399:     */   }
/*  400:     */   
/*  401:     */   public void setSheetName(int sheetnum, String sheetname)
/*  402:     */   {
/*  403: 599 */     checkSheets(sheetnum);
/*  404:     */     
/*  405:     */ 
/*  406: 602 */     String sn = sheetname.length() > 31 ? sheetname.substring(0, 31) : sheetname;
/*  407:     */     
/*  408: 604 */     BoundSheetRecord sheet = (BoundSheetRecord)this.boundsheets.get(sheetnum);
/*  409: 605 */     sheet.setSheetname(sn);
/*  410:     */   }
/*  411:     */   
/*  412:     */   public boolean doesContainsSheetName(String name, int excludeSheetIdx)
/*  413:     */   {
/*  414: 617 */     String aName = name;
/*  415: 618 */     if (aName.length() > 31) {
/*  416: 619 */       aName = aName.substring(0, 31);
/*  417:     */     }
/*  418: 621 */     int i = 0;
/*  419: 622 */     for (BoundSheetRecord boundSheetRecord : this.boundsheets) {
/*  420: 623 */       if (excludeSheetIdx != i++)
/*  421:     */       {
/*  422: 626 */         String bName = boundSheetRecord.getSheetname();
/*  423: 627 */         if (bName.length() > 31) {
/*  424: 628 */           bName = bName.substring(0, 31);
/*  425:     */         }
/*  426: 630 */         if (aName.equalsIgnoreCase(bName)) {
/*  427: 631 */           return true;
/*  428:     */         }
/*  429:     */       }
/*  430:     */     }
/*  431: 634 */     return false;
/*  432:     */   }
/*  433:     */   
/*  434:     */   public void setSheetOrder(String sheetname, int pos)
/*  435:     */   {
/*  436: 644 */     int sheetNumber = getSheetIndex(sheetname);
/*  437:     */     
/*  438: 646 */     this.boundsheets.add(pos, this.boundsheets.remove(sheetNumber));
/*  439:     */     
/*  440:     */ 
/*  441: 649 */     int initialBspos = this.records.getBspos();
/*  442: 650 */     int pos0 = initialBspos - (this.boundsheets.size() - 1);
/*  443: 651 */     Record removed = this.records.get(pos0 + sheetNumber);
/*  444: 652 */     this.records.remove(pos0 + sheetNumber);
/*  445: 653 */     this.records.add(pos0 + pos, removed);
/*  446: 654 */     this.records.setBspos(initialBspos);
/*  447:     */   }
/*  448:     */   
/*  449:     */   public String getSheetName(int sheetIndex)
/*  450:     */   {
/*  451: 664 */     return getBoundSheetRec(sheetIndex).getSheetname();
/*  452:     */   }
/*  453:     */   
/*  454:     */   public boolean isSheetHidden(int sheetnum)
/*  455:     */   {
/*  456: 677 */     return getBoundSheetRec(sheetnum).isHidden();
/*  457:     */   }
/*  458:     */   
/*  459:     */   public boolean isSheetVeryHidden(int sheetnum)
/*  460:     */   {
/*  461: 690 */     return getBoundSheetRec(sheetnum).isVeryHidden();
/*  462:     */   }
/*  463:     */   
/*  464:     */   public SheetVisibility getSheetVisibility(int sheetnum)
/*  465:     */   {
/*  466: 704 */     BoundSheetRecord bsr = getBoundSheetRec(sheetnum);
/*  467: 705 */     if (bsr.isVeryHidden()) {
/*  468: 706 */       return SheetVisibility.VERY_HIDDEN;
/*  469:     */     }
/*  470: 708 */     if (bsr.isHidden()) {
/*  471: 709 */       return SheetVisibility.HIDDEN;
/*  472:     */     }
/*  473: 711 */     return SheetVisibility.VISIBLE;
/*  474:     */   }
/*  475:     */   
/*  476:     */   public void setSheetHidden(int sheetnum, boolean hidden)
/*  477:     */   {
/*  478: 721 */     setSheetHidden(sheetnum, hidden ? SheetVisibility.HIDDEN : SheetVisibility.VISIBLE);
/*  479:     */   }
/*  480:     */   
/*  481:     */   public void setSheetHidden(int sheetnum, SheetVisibility visibility)
/*  482:     */   {
/*  483: 732 */     BoundSheetRecord bsr = getBoundSheetRec(sheetnum);
/*  484: 733 */     bsr.setHidden(visibility == SheetVisibility.HIDDEN);
/*  485: 734 */     bsr.setVeryHidden(visibility == SheetVisibility.VERY_HIDDEN);
/*  486:     */   }
/*  487:     */   
/*  488:     */   public int getSheetIndex(String name)
/*  489:     */   {
/*  490: 744 */     int retval = -1;
/*  491:     */     
/*  492: 746 */     int size = this.boundsheets.size();
/*  493: 747 */     for (int k = 0; k < size; k++)
/*  494:     */     {
/*  495: 748 */       String sheet = getSheetName(k);
/*  496: 750 */       if (sheet.equalsIgnoreCase(name))
/*  497:     */       {
/*  498: 751 */         retval = k;
/*  499: 752 */         break;
/*  500:     */       }
/*  501:     */     }
/*  502: 755 */     return retval;
/*  503:     */   }
/*  504:     */   
/*  505:     */   private void checkSheets(int sheetnum)
/*  506:     */   {
/*  507: 763 */     if (this.boundsheets.size() <= sheetnum)
/*  508:     */     {
/*  509: 764 */       if (this.boundsheets.size() + 1 <= sheetnum) {
/*  510: 765 */         throw new RuntimeException("Sheet number out of bounds!");
/*  511:     */       }
/*  512: 767 */       BoundSheetRecord bsr = createBoundSheet(sheetnum);
/*  513:     */       
/*  514: 769 */       this.records.add(this.records.getBspos() + 1, bsr);
/*  515: 770 */       this.records.setBspos(this.records.getBspos() + 1);
/*  516: 771 */       this.boundsheets.add(bsr);
/*  517: 772 */       getOrCreateLinkTable().checkExternSheet(sheetnum);
/*  518: 773 */       fixTabIdRecord();
/*  519:     */     }
/*  520:     */   }
/*  521:     */   
/*  522:     */   public void removeSheet(int sheetIndex)
/*  523:     */   {
/*  524: 781 */     if (this.boundsheets.size() > sheetIndex)
/*  525:     */     {
/*  526: 782 */       this.records.remove(this.records.getBspos() - (this.boundsheets.size() - 1) + sheetIndex);
/*  527: 783 */       this.boundsheets.remove(sheetIndex);
/*  528: 784 */       fixTabIdRecord();
/*  529:     */     }
/*  530: 794 */     int sheetNum1Based = sheetIndex + 1;
/*  531: 795 */     for (int i = 0; i < getNumNames(); i++)
/*  532:     */     {
/*  533: 796 */       NameRecord nr = getNameRecord(i);
/*  534: 798 */       if (nr.getSheetNumber() == sheetNum1Based) {
/*  535: 800 */         nr.setSheetNumber(0);
/*  536: 801 */       } else if (nr.getSheetNumber() > sheetNum1Based) {
/*  537: 804 */         nr.setSheetNumber(nr.getSheetNumber() - 1);
/*  538:     */       }
/*  539:     */     }
/*  540: 808 */     if (this.linkTable != null) {
/*  541: 811 */       this.linkTable.removeSheet(sheetIndex);
/*  542:     */     }
/*  543:     */   }
/*  544:     */   
/*  545:     */   private void fixTabIdRecord()
/*  546:     */   {
/*  547: 819 */     Record rec = this.records.get(this.records.getTabpos());
/*  548: 824 */     if (this.records.getTabpos() <= 0) {
/*  549: 825 */       return;
/*  550:     */     }
/*  551: 828 */     TabIdRecord tir = (TabIdRecord)rec;
/*  552: 829 */     short[] tia = new short[this.boundsheets.size()];
/*  553: 831 */     for (short k = 0; k < tia.length; k = (short)(k + 1)) {
/*  554: 832 */       tia[k] = k;
/*  555:     */     }
/*  556: 834 */     tir.setTabIdArray(tia);
/*  557:     */   }
/*  558:     */   
/*  559:     */   public int getNumSheets()
/*  560:     */   {
/*  561: 844 */     LOG.log(1, new Object[] { "getNumSheets=", Integer.valueOf(this.boundsheets.size()) });
/*  562: 845 */     return this.boundsheets.size();
/*  563:     */   }
/*  564:     */   
/*  565:     */   public int getNumExFormats()
/*  566:     */   {
/*  567: 855 */     LOG.log(1, new Object[] { "getXF=", Integer.valueOf(this.numxfs) });
/*  568: 856 */     return this.numxfs;
/*  569:     */   }
/*  570:     */   
/*  571:     */   public ExtendedFormatRecord getExFormatAt(int index)
/*  572:     */   {
/*  573: 867 */     int xfptr = this.records.getXfpos() - (this.numxfs - 1);
/*  574:     */     
/*  575: 869 */     xfptr += index;
/*  576: 870 */     ExtendedFormatRecord retval = (ExtendedFormatRecord)this.records.get(xfptr);
/*  577:     */     
/*  578:     */ 
/*  579: 873 */     return retval;
/*  580:     */   }
/*  581:     */   
/*  582:     */   public void removeExFormatRecord(ExtendedFormatRecord rec)
/*  583:     */   {
/*  584: 885 */     this.records.remove(rec);
/*  585: 886 */     this.numxfs -= 1;
/*  586:     */   }
/*  587:     */   
/*  588:     */   public void removeExFormatRecord(int index)
/*  589:     */   {
/*  590: 897 */     int xfptr = this.records.getXfpos() - (this.numxfs - 1) + index;
/*  591: 898 */     this.records.remove(xfptr);
/*  592: 899 */     this.numxfs -= 1;
/*  593:     */   }
/*  594:     */   
/*  595:     */   public ExtendedFormatRecord createCellXF()
/*  596:     */   {
/*  597: 911 */     ExtendedFormatRecord xf = createExtendedFormat();
/*  598:     */     
/*  599: 913 */     this.records.add(this.records.getXfpos() + 1, xf);
/*  600: 914 */     this.records.setXfpos(this.records.getXfpos() + 1);
/*  601: 915 */     this.numxfs += 1;
/*  602: 916 */     return xf;
/*  603:     */   }
/*  604:     */   
/*  605:     */   public StyleRecord getStyleRecord(int xfIndex)
/*  606:     */   {
/*  607: 931 */     for (int i = this.records.getXfpos(); i < this.records.size(); i++)
/*  608:     */     {
/*  609: 932 */       Record r = this.records.get(i);
/*  610: 933 */       if ((r instanceof StyleRecord))
/*  611:     */       {
/*  612: 934 */         StyleRecord sr = (StyleRecord)r;
/*  613: 935 */         if (sr.getXFIndex() == xfIndex) {
/*  614: 936 */           return sr;
/*  615:     */         }
/*  616:     */       }
/*  617:     */     }
/*  618: 940 */     return null;
/*  619:     */   }
/*  620:     */   
/*  621:     */   public StyleRecord createStyleRecord(int xfIndex)
/*  622:     */   {
/*  623: 955 */     StyleRecord newSR = new StyleRecord();
/*  624: 956 */     newSR.setXFIndex(xfIndex);
/*  625:     */     
/*  626:     */ 
/*  627: 959 */     int addAt = -1;
/*  628: 960 */     for (int i = this.records.getXfpos(); (i < this.records.size()) && (addAt == -1); i++)
/*  629:     */     {
/*  630: 962 */       Record r = this.records.get(i);
/*  631: 963 */       if ((!(r instanceof ExtendedFormatRecord)) && (!(r instanceof StyleRecord))) {
/*  632: 967 */         addAt = i;
/*  633:     */       }
/*  634:     */     }
/*  635: 970 */     if (addAt == -1) {
/*  636: 971 */       throw new IllegalStateException("No XF Records found!");
/*  637:     */     }
/*  638: 973 */     this.records.add(addAt, newSR);
/*  639:     */     
/*  640: 975 */     return newSR;
/*  641:     */   }
/*  642:     */   
/*  643:     */   public int addSSTString(UnicodeString string)
/*  644:     */   {
/*  645: 989 */     LOG.log(1, new Object[] { "insert to sst string='", string });
/*  646: 990 */     if (this.sst == null) {
/*  647: 991 */       insertSST();
/*  648:     */     }
/*  649: 993 */     return this.sst.addString(string);
/*  650:     */   }
/*  651:     */   
/*  652:     */   public UnicodeString getSSTString(int str)
/*  653:     */   {
/*  654:1002 */     if (this.sst == null) {
/*  655:1003 */       insertSST();
/*  656:     */     }
/*  657:1005 */     UnicodeString retval = this.sst.getString(str);
/*  658:     */     
/*  659:1007 */     LOG.log(1, new Object[] { "Returning SST for index=", Integer.valueOf(str), " String= ", retval });
/*  660:1008 */     return retval;
/*  661:     */   }
/*  662:     */   
/*  663:     */   public void insertSST()
/*  664:     */   {
/*  665:1019 */     LOG.log(1, new Object[] { "creating new SST via insertSST!" });
/*  666:     */     
/*  667:1021 */     this.sst = new SSTRecord();
/*  668:1022 */     this.records.add(this.records.size() - 1, createExtendedSST());
/*  669:1023 */     this.records.add(this.records.size() - 2, this.sst);
/*  670:     */   }
/*  671:     */   
/*  672:     */   public int serialize(int offset, byte[] data)
/*  673:     */   {
/*  674:1034 */     LOG.log(1, new Object[] { "Serializing Workbook with offsets" });
/*  675:     */     
/*  676:1036 */     int pos = 0;
/*  677:     */     
/*  678:1038 */     SSTRecord lSST = null;
/*  679:1039 */     int sstPos = 0;
/*  680:1040 */     boolean wroteBoundSheets = false;
/*  681:1041 */     for (Record record : this.records.getRecords())
/*  682:     */     {
/*  683:1042 */       int len = 0;
/*  684:1043 */       if ((record instanceof SSTRecord))
/*  685:     */       {
/*  686:1044 */         lSST = (SSTRecord)record;
/*  687:1045 */         sstPos = pos;
/*  688:     */       }
/*  689:1047 */       if ((record.getSid() == 255) && (lSST != null)) {
/*  690:1048 */         record = lSST.createExtSSTRecord(sstPos + offset);
/*  691:     */       }
/*  692:1050 */       if ((record instanceof BoundSheetRecord))
/*  693:     */       {
/*  694:1051 */         if (!wroteBoundSheets)
/*  695:     */         {
/*  696:1052 */           for (BoundSheetRecord bsr : this.boundsheets) {
/*  697:1053 */             len += bsr.serialize(pos + offset + len, data);
/*  698:     */           }
/*  699:1055 */           wroteBoundSheets = true;
/*  700:     */         }
/*  701:     */       }
/*  702:     */       else {
/*  703:1058 */         len = record.serialize(pos + offset, data);
/*  704:     */       }
/*  705:1060 */       pos += len;
/*  706:     */     }
/*  707:1063 */     LOG.log(1, new Object[] { "Exiting serialize workbook" });
/*  708:1064 */     return pos;
/*  709:     */   }
/*  710:     */   
/*  711:     */   public void preSerialize()
/*  712:     */   {
/*  713:1075 */     if (this.records.getTabpos() > 0)
/*  714:     */     {
/*  715:1076 */       TabIdRecord tir = (TabIdRecord)this.records.get(this.records.getTabpos());
/*  716:1077 */       if (tir._tabids.length < this.boundsheets.size()) {
/*  717:1078 */         fixTabIdRecord();
/*  718:     */       }
/*  719:     */     }
/*  720:     */   }
/*  721:     */   
/*  722:     */   public int getSize()
/*  723:     */   {
/*  724:1084 */     int retval = 0;
/*  725:     */     
/*  726:1086 */     SSTRecord lSST = null;
/*  727:1087 */     for (Record record : this.records.getRecords())
/*  728:     */     {
/*  729:1088 */       if ((record instanceof SSTRecord)) {
/*  730:1089 */         lSST = (SSTRecord)record;
/*  731:     */       }
/*  732:1092 */       if ((record.getSid() == 255) && (lSST != null)) {
/*  733:1093 */         retval += lSST.calcExtSSTRecordSize();
/*  734:     */       } else {
/*  735:1095 */         retval += record.getRecordSize();
/*  736:     */       }
/*  737:     */     }
/*  738:1099 */     return retval;
/*  739:     */   }
/*  740:     */   
/*  741:     */   private static BOFRecord createBOF()
/*  742:     */   {
/*  743:1103 */     BOFRecord retval = new BOFRecord();
/*  744:     */     
/*  745:1105 */     retval.setVersion(1536);
/*  746:1106 */     retval.setType(5);
/*  747:1107 */     retval.setBuild(4307);
/*  748:1108 */     retval.setBuildYear(1996);
/*  749:     */     
/*  750:1110 */     retval.setHistoryBitMask(65);
/*  751:1111 */     retval.setRequiredVersion(6);
/*  752:1112 */     return retval;
/*  753:     */   }
/*  754:     */   
/*  755:     */   private static MMSRecord createMMS()
/*  756:     */   {
/*  757:1117 */     MMSRecord retval = new MMSRecord();
/*  758:     */     
/*  759:1119 */     retval.setAddMenuCount((byte)0);
/*  760:1120 */     retval.setDelMenuCount((byte)0);
/*  761:1121 */     return retval;
/*  762:     */   }
/*  763:     */   
/*  764:     */   private static WriteAccessRecord createWriteAccess()
/*  765:     */   {
/*  766:1128 */     WriteAccessRecord retval = new WriteAccessRecord();
/*  767:     */     
/*  768:1130 */     String defaultUserName = "POI";
/*  769:     */     try
/*  770:     */     {
/*  771:1132 */       String username = System.getProperty("user.name");
/*  772:1134 */       if (username == null) {
/*  773:1135 */         username = defaultUserName;
/*  774:     */       }
/*  775:1138 */       retval.setUsername(username);
/*  776:     */     }
/*  777:     */     catch (AccessControlException e)
/*  778:     */     {
/*  779:1140 */       LOG.log(5, new Object[] { "can't determine user.name", e });
/*  780:     */       
/*  781:     */ 
/*  782:1143 */       retval.setUsername(defaultUserName);
/*  783:     */     }
/*  784:1145 */     return retval;
/*  785:     */   }
/*  786:     */   
/*  787:     */   private static CodepageRecord createCodepage()
/*  788:     */   {
/*  789:1149 */     CodepageRecord retval = new CodepageRecord();
/*  790:     */     
/*  791:1151 */     retval.setCodepage((short)1200);
/*  792:1152 */     return retval;
/*  793:     */   }
/*  794:     */   
/*  795:     */   private static DSFRecord createDSF()
/*  796:     */   {
/*  797:1156 */     return new DSFRecord(false);
/*  798:     */   }
/*  799:     */   
/*  800:     */   private static TabIdRecord createTabId()
/*  801:     */   {
/*  802:1163 */     return new TabIdRecord();
/*  803:     */   }
/*  804:     */   
/*  805:     */   private static FnGroupCountRecord createFnGroupCount()
/*  806:     */   {
/*  807:1170 */     FnGroupCountRecord retval = new FnGroupCountRecord();
/*  808:     */     
/*  809:1172 */     retval.setCount((short)14);
/*  810:1173 */     return retval;
/*  811:     */   }
/*  812:     */   
/*  813:     */   private static WindowProtectRecord createWindowProtect()
/*  814:     */   {
/*  815:1182 */     return new WindowProtectRecord(false);
/*  816:     */   }
/*  817:     */   
/*  818:     */   private static ProtectRecord createProtect()
/*  819:     */   {
/*  820:1191 */     return new ProtectRecord(false);
/*  821:     */   }
/*  822:     */   
/*  823:     */   private static PasswordRecord createPassword()
/*  824:     */   {
/*  825:1198 */     return new PasswordRecord(0);
/*  826:     */   }
/*  827:     */   
/*  828:     */   private static ProtectionRev4Record createProtectionRev4()
/*  829:     */   {
/*  830:1205 */     return new ProtectionRev4Record(false);
/*  831:     */   }
/*  832:     */   
/*  833:     */   private static PasswordRev4Record createPasswordRev4()
/*  834:     */   {
/*  835:1212 */     return new PasswordRev4Record(0);
/*  836:     */   }
/*  837:     */   
/*  838:     */   private static WindowOneRecord createWindowOne()
/*  839:     */   {
/*  840:1228 */     WindowOneRecord retval = new WindowOneRecord();
/*  841:     */     
/*  842:1230 */     retval.setHorizontalHold((short)360);
/*  843:1231 */     retval.setVerticalHold((short)270);
/*  844:1232 */     retval.setWidth((short)14940);
/*  845:1233 */     retval.setHeight((short)9150);
/*  846:1234 */     retval.setOptions((short)56);
/*  847:1235 */     retval.setActiveSheetIndex(0);
/*  848:1236 */     retval.setFirstVisibleTab(0);
/*  849:1237 */     retval.setNumSelectedTabs((short)1);
/*  850:1238 */     retval.setTabWidthRatio((short)600);
/*  851:1239 */     return retval;
/*  852:     */   }
/*  853:     */   
/*  854:     */   private static BackupRecord createBackup()
/*  855:     */   {
/*  856:1246 */     BackupRecord retval = new BackupRecord();
/*  857:     */     
/*  858:1248 */     retval.setBackup((short)0);
/*  859:1249 */     return retval;
/*  860:     */   }
/*  861:     */   
/*  862:     */   private static HideObjRecord createHideObj()
/*  863:     */   {
/*  864:1256 */     HideObjRecord retval = new HideObjRecord();
/*  865:1257 */     retval.setHideObj((short)0);
/*  866:1258 */     return retval;
/*  867:     */   }
/*  868:     */   
/*  869:     */   private static DateWindow1904Record createDateWindow1904()
/*  870:     */   {
/*  871:1265 */     DateWindow1904Record retval = new DateWindow1904Record();
/*  872:     */     
/*  873:1267 */     retval.setWindowing((short)0);
/*  874:1268 */     return retval;
/*  875:     */   }
/*  876:     */   
/*  877:     */   private static PrecisionRecord createPrecision()
/*  878:     */   {
/*  879:1275 */     PrecisionRecord retval = new PrecisionRecord();
/*  880:1276 */     retval.setFullPrecision(true);
/*  881:1277 */     return retval;
/*  882:     */   }
/*  883:     */   
/*  884:     */   private static RefreshAllRecord createRefreshAll()
/*  885:     */   {
/*  886:1284 */     return new RefreshAllRecord(false);
/*  887:     */   }
/*  888:     */   
/*  889:     */   private static BookBoolRecord createBookBool()
/*  890:     */   {
/*  891:1291 */     BookBoolRecord retval = new BookBoolRecord();
/*  892:1292 */     retval.setSaveLinkValues((short)0);
/*  893:1293 */     return retval;
/*  894:     */   }
/*  895:     */   
/*  896:     */   private static FontRecord createFont()
/*  897:     */   {
/*  898:1306 */     FontRecord retval = new FontRecord();
/*  899:     */     
/*  900:1308 */     retval.setFontHeight((short)200);
/*  901:1309 */     retval.setAttributes((short)0);
/*  902:1310 */     retval.setColorPaletteIndex((short)32767);
/*  903:1311 */     retval.setBoldWeight((short)400);
/*  904:1312 */     retval.setFontName("Arial");
/*  905:1313 */     return retval;
/*  906:     */   }
/*  907:     */   
/*  908:     */   private static FormatRecord createFormat(int id)
/*  909:     */   {
/*  910:1323 */     int[] mappings = { 5, 6, 7, 8, 42, 41, 44, 43 };
/*  911:1324 */     if ((id < 0) || (id >= mappings.length)) {
/*  912:1325 */       throw new IllegalArgumentException("Unexpected id " + id);
/*  913:     */     }
/*  914:1327 */     return new FormatRecord(mappings[id], BuiltinFormats.getBuiltinFormat(mappings[id]));
/*  915:     */   }
/*  916:     */   
/*  917:     */   private static ExtendedFormatRecord createExtendedFormat(int id)
/*  918:     */   {
/*  919:1337 */     switch (id)
/*  920:     */     {
/*  921:     */     case 0: 
/*  922:1338 */       return createExtendedFormat(0, 0, -11, 0);
/*  923:     */     case 1: 
/*  924:     */     case 2: 
/*  925:1340 */       return createExtendedFormat(1, 0, -11, -3072);
/*  926:     */     case 3: 
/*  927:     */     case 4: 
/*  928:1342 */       return createExtendedFormat(2, 0, -11, -3072);
/*  929:     */     case 5: 
/*  930:     */     case 6: 
/*  931:     */     case 7: 
/*  932:     */     case 8: 
/*  933:     */     case 9: 
/*  934:     */     case 10: 
/*  935:     */     case 11: 
/*  936:     */     case 12: 
/*  937:     */     case 13: 
/*  938:     */     case 14: 
/*  939:1352 */       return createExtendedFormat(0, 0, -11, -3072);
/*  940:     */     case 15: 
/*  941:1354 */       return createExtendedFormat(0, 0, 1, 0);
/*  942:     */     case 16: 
/*  943:1356 */       return createExtendedFormat(1, 43, -11, -2048);
/*  944:     */     case 17: 
/*  945:1357 */       return createExtendedFormat(1, 41, -11, -2048);
/*  946:     */     case 18: 
/*  947:1358 */       return createExtendedFormat(1, 44, -11, -2048);
/*  948:     */     case 19: 
/*  949:1359 */       return createExtendedFormat(1, 42, -11, -2048);
/*  950:     */     case 20: 
/*  951:1360 */       return createExtendedFormat(1, 9, -11, -2048);
/*  952:     */     case 21: 
/*  953:1362 */       return createExtendedFormat(5, 0, 1, 2048);
/*  954:     */     case 22: 
/*  955:1363 */       return createExtendedFormat(6, 0, 1, 23552);
/*  956:     */     case 23: 
/*  957:1364 */       return createExtendedFormat(0, 49, 1, 23552);
/*  958:     */     case 24: 
/*  959:1365 */       return createExtendedFormat(0, 8, 1, 23552);
/*  960:     */     case 25: 
/*  961:1366 */       return createExtendedFormat(6, 8, 1, 23552);
/*  962:     */     }
/*  963:1368 */     throw new IllegalStateException("Unrecognized format id: " + id);
/*  964:     */   }
/*  965:     */   
/*  966:     */   private static ExtendedFormatRecord createExtendedFormat(int fontIndex, int formatIndex, int cellOptions, int indentionOptions)
/*  967:     */   {
/*  968:1375 */     ExtendedFormatRecord retval = new ExtendedFormatRecord();
/*  969:1376 */     retval.setFontIndex((short)fontIndex);
/*  970:1377 */     retval.setFormatIndex((short)formatIndex);
/*  971:1378 */     retval.setCellOptions((short)cellOptions);
/*  972:1379 */     retval.setAlignmentOptions((short)32);
/*  973:1380 */     retval.setIndentionOptions((short)indentionOptions);
/*  974:1381 */     retval.setBorderOptions((short)0);
/*  975:1382 */     retval.setPaletteOptions((short)0);
/*  976:1383 */     retval.setAdtlPaletteOptions((short)0);
/*  977:1384 */     retval.setFillPaletteOptions((short)8384);
/*  978:1385 */     return retval;
/*  979:     */   }
/*  980:     */   
/*  981:     */   private static ExtendedFormatRecord createExtendedFormat()
/*  982:     */   {
/*  983:1393 */     ExtendedFormatRecord retval = new ExtendedFormatRecord();
/*  984:     */     
/*  985:1395 */     retval.setFontIndex((short)0);
/*  986:1396 */     retval.setFormatIndex((short)0);
/*  987:1397 */     retval.setCellOptions((short)1);
/*  988:1398 */     retval.setAlignmentOptions((short)32);
/*  989:1399 */     retval.setIndentionOptions((short)0);
/*  990:1400 */     retval.setBorderOptions((short)0);
/*  991:1401 */     retval.setPaletteOptions((short)0);
/*  992:1402 */     retval.setAdtlPaletteOptions((short)0);
/*  993:1403 */     retval.setFillPaletteOptions((short)8384);
/*  994:1404 */     retval.setTopBorderPaletteIdx(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
/*  995:1405 */     retval.setBottomBorderPaletteIdx(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
/*  996:1406 */     retval.setLeftBorderPaletteIdx(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
/*  997:1407 */     retval.setRightBorderPaletteIdx(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
/*  998:1408 */     return retval;
/*  999:     */   }
/* 1000:     */   
/* 1001:     */   private static StyleRecord createStyle(int id)
/* 1002:     */   {
/* 1003:1418 */     int[][] mappings = { { 16, 3 }, { 17, 6 }, { 18, 4 }, { 19, 7 }, { 0, 0 }, { 20, 5 } };
/* 1004:1421 */     if ((id < 0) || (id >= mappings.length)) {
/* 1005:1422 */       throw new IllegalArgumentException("Unexpected style id " + id);
/* 1006:     */     }
/* 1007:1425 */     StyleRecord retval = new StyleRecord();
/* 1008:1426 */     retval.setOutlineStyleLevel(-1);
/* 1009:1427 */     retval.setXFIndex(mappings[id][0]);
/* 1010:1428 */     retval.setBuiltinStyle(mappings[id][1]);
/* 1011:1429 */     return retval;
/* 1012:     */   }
/* 1013:     */   
/* 1014:     */   private static PaletteRecord createPalette()
/* 1015:     */   {
/* 1016:1436 */     return new PaletteRecord();
/* 1017:     */   }
/* 1018:     */   
/* 1019:     */   private static UseSelFSRecord createUseSelFS()
/* 1020:     */   {
/* 1021:1443 */     return new UseSelFSRecord(false);
/* 1022:     */   }
/* 1023:     */   
/* 1024:     */   private static BoundSheetRecord createBoundSheet(int id)
/* 1025:     */   {
/* 1026:1455 */     return new BoundSheetRecord("Sheet" + (id + 1));
/* 1027:     */   }
/* 1028:     */   
/* 1029:     */   private static CountryRecord createCountry()
/* 1030:     */   {
/* 1031:1463 */     CountryRecord retval = new CountryRecord();
/* 1032:     */     
/* 1033:1465 */     retval.setDefaultCountry((short)1);
/* 1034:1468 */     if ("ru_RU".equals(LocaleUtil.getUserLocale().toString())) {
/* 1035:1469 */       retval.setCurrentCountry((short)7);
/* 1036:     */     } else {
/* 1037:1471 */       retval.setCurrentCountry((short)1);
/* 1038:     */     }
/* 1039:1474 */     return retval;
/* 1040:     */   }
/* 1041:     */   
/* 1042:     */   private static ExtSSTRecord createExtendedSST()
/* 1043:     */   {
/* 1044:1483 */     ExtSSTRecord retval = new ExtSSTRecord();
/* 1045:1484 */     retval.setNumStringsPerBucket((short)8);
/* 1046:1485 */     return retval;
/* 1047:     */   }
/* 1048:     */   
/* 1049:     */   private LinkTable getOrCreateLinkTable()
/* 1050:     */   {
/* 1051:1493 */     if (this.linkTable == null) {
/* 1052:1494 */       this.linkTable = new LinkTable((short)getNumSheets(), this.records);
/* 1053:     */     }
/* 1054:1496 */     return this.linkTable;
/* 1055:     */   }
/* 1056:     */   
/* 1057:     */   public int linkExternalWorkbook(String name, Workbook externalWorkbook)
/* 1058:     */   {
/* 1059:1500 */     return getOrCreateLinkTable().linkExternalWorkbook(name, externalWorkbook);
/* 1060:     */   }
/* 1061:     */   
/* 1062:     */   public String findSheetFirstNameFromExternSheet(int externSheetIndex)
/* 1063:     */   {
/* 1064:1509 */     int indexToSheet = this.linkTable.getFirstInternalSheetIndexForExtIndex(externSheetIndex);
/* 1065:1510 */     return findSheetNameFromIndex(indexToSheet);
/* 1066:     */   }
/* 1067:     */   
/* 1068:     */   public String findSheetLastNameFromExternSheet(int externSheetIndex)
/* 1069:     */   {
/* 1070:1513 */     int indexToSheet = this.linkTable.getLastInternalSheetIndexForExtIndex(externSheetIndex);
/* 1071:1514 */     return findSheetNameFromIndex(indexToSheet);
/* 1072:     */   }
/* 1073:     */   
/* 1074:     */   private String findSheetNameFromIndex(int internalSheetIndex)
/* 1075:     */   {
/* 1076:1517 */     if (internalSheetIndex < 0) {
/* 1077:1520 */       return "";
/* 1078:     */     }
/* 1079:1522 */     if (internalSheetIndex >= this.boundsheets.size()) {
/* 1080:1524 */       return "";
/* 1081:     */     }
/* 1082:1526 */     return getSheetName(internalSheetIndex);
/* 1083:     */   }
/* 1084:     */   
/* 1085:     */   public EvaluationWorkbook.ExternalSheet getExternalSheet(int externSheetIndex)
/* 1086:     */   {
/* 1087:1530 */     String[] extNames = this.linkTable.getExternalBookAndSheetName(externSheetIndex);
/* 1088:1531 */     if (extNames == null) {
/* 1089:1532 */       return null;
/* 1090:     */     }
/* 1091:1534 */     if (extNames.length == 2) {
/* 1092:1535 */       return new EvaluationWorkbook.ExternalSheet(extNames[0], extNames[1]);
/* 1093:     */     }
/* 1094:1537 */     return new EvaluationWorkbook.ExternalSheetRange(extNames[0], extNames[1], extNames[2]);
/* 1095:     */   }
/* 1096:     */   
/* 1097:     */   public EvaluationWorkbook.ExternalName getExternalName(int externSheetIndex, int externNameIndex)
/* 1098:     */   {
/* 1099:1541 */     String nameName = this.linkTable.resolveNameXText(externSheetIndex, externNameIndex, this);
/* 1100:1542 */     if (nameName == null) {
/* 1101:1543 */       return null;
/* 1102:     */     }
/* 1103:1545 */     int ix = this.linkTable.resolveNameXIx(externSheetIndex, externNameIndex);
/* 1104:1546 */     return new EvaluationWorkbook.ExternalName(nameName, externNameIndex, ix);
/* 1105:     */   }
/* 1106:     */   
/* 1107:     */   public int getFirstSheetIndexFromExternSheetIndex(int externSheetNumber)
/* 1108:     */   {
/* 1109:1556 */     return this.linkTable.getFirstInternalSheetIndexForExtIndex(externSheetNumber);
/* 1110:     */   }
/* 1111:     */   
/* 1112:     */   public int getLastSheetIndexFromExternSheetIndex(int externSheetNumber)
/* 1113:     */   {
/* 1114:1567 */     return this.linkTable.getLastInternalSheetIndexForExtIndex(externSheetNumber);
/* 1115:     */   }
/* 1116:     */   
/* 1117:     */   public short checkExternSheet(int sheetNumber)
/* 1118:     */   {
/* 1119:1577 */     return (short)getOrCreateLinkTable().checkExternSheet(sheetNumber);
/* 1120:     */   }
/* 1121:     */   
/* 1122:     */   public short checkExternSheet(int firstSheetNumber, int lastSheetNumber)
/* 1123:     */   {
/* 1124:1587 */     return (short)getOrCreateLinkTable().checkExternSheet(firstSheetNumber, lastSheetNumber);
/* 1125:     */   }
/* 1126:     */   
/* 1127:     */   public int getExternalSheetIndex(String workbookName, String sheetName)
/* 1128:     */   {
/* 1129:1591 */     return getOrCreateLinkTable().getExternalSheetIndex(workbookName, sheetName, sheetName);
/* 1130:     */   }
/* 1131:     */   
/* 1132:     */   public int getExternalSheetIndex(String workbookName, String firstSheetName, String lastSheetName)
/* 1133:     */   {
/* 1134:1594 */     return getOrCreateLinkTable().getExternalSheetIndex(workbookName, firstSheetName, lastSheetName);
/* 1135:     */   }
/* 1136:     */   
/* 1137:     */   public int getNumNames()
/* 1138:     */   {
/* 1139:1602 */     if (this.linkTable == null) {
/* 1140:1603 */       return 0;
/* 1141:     */     }
/* 1142:1605 */     return this.linkTable.getNumNames();
/* 1143:     */   }
/* 1144:     */   
/* 1145:     */   public NameRecord getNameRecord(int index)
/* 1146:     */   {
/* 1147:1614 */     return this.linkTable.getNameRecord(index);
/* 1148:     */   }
/* 1149:     */   
/* 1150:     */   public NameCommentRecord getNameCommentRecord(NameRecord nameRecord)
/* 1151:     */   {
/* 1152:1623 */     return (NameCommentRecord)this.commentRecords.get(nameRecord.getNameText());
/* 1153:     */   }
/* 1154:     */   
/* 1155:     */   public NameRecord createName()
/* 1156:     */   {
/* 1157:1631 */     return addName(new NameRecord());
/* 1158:     */   }
/* 1159:     */   
/* 1160:     */   public NameRecord addName(NameRecord name)
/* 1161:     */   {
/* 1162:1642 */     getOrCreateLinkTable().addName(name);
/* 1163:1643 */     return name;
/* 1164:     */   }
/* 1165:     */   
/* 1166:     */   public NameRecord createBuiltInName(byte builtInName, int sheetNumber)
/* 1167:     */   {
/* 1168:1655 */     if ((sheetNumber < 0) || (sheetNumber + 1 > 32767)) {
/* 1169:1656 */       throw new IllegalArgumentException("Sheet number [" + sheetNumber + "]is not valid ");
/* 1170:     */     }
/* 1171:1659 */     NameRecord name = new NameRecord(builtInName, sheetNumber);
/* 1172:1661 */     if (this.linkTable.nameAlreadyExists(name)) {
/* 1173:1662 */       throw new RuntimeException("Builtin (" + builtInName + ") already exists for sheet (" + sheetNumber + ")");
/* 1174:     */     }
/* 1175:1665 */     addName(name);
/* 1176:1666 */     return name;
/* 1177:     */   }
/* 1178:     */   
/* 1179:     */   public void removeName(int nameIndex)
/* 1180:     */   {
/* 1181:1675 */     if (this.linkTable.getNumNames() > nameIndex)
/* 1182:     */     {
/* 1183:1676 */       int idx = findFirstRecordLocBySid((short)24);
/* 1184:1677 */       this.records.remove(idx + nameIndex);
/* 1185:1678 */       this.linkTable.removeName(nameIndex);
/* 1186:     */     }
/* 1187:     */   }
/* 1188:     */   
/* 1189:     */   public void updateNameCommentRecordCache(NameCommentRecord commentRecord)
/* 1190:     */   {
/* 1191:1689 */     if (this.commentRecords.containsValue(commentRecord)) {
/* 1192:1690 */       for (Entry<String, NameCommentRecord> entry : this.commentRecords.entrySet()) {
/* 1193:1691 */         if (((NameCommentRecord)entry.getValue()).equals(commentRecord))
/* 1194:     */         {
/* 1195:1692 */           this.commentRecords.remove(entry.getKey());
/* 1196:1693 */           break;
/* 1197:     */         }
/* 1198:     */       }
/* 1199:     */     }
/* 1200:1697 */     this.commentRecords.put(commentRecord.getNameText(), commentRecord);
/* 1201:     */   }
/* 1202:     */   
/* 1203:     */   public short getFormat(String format, boolean createIfNotFound)
/* 1204:     */   {
/* 1205:1707 */     for (FormatRecord r : this.formats) {
/* 1206:1708 */       if (r.getFormatString().equals(format)) {
/* 1207:1709 */         return (short)r.getIndexCode();
/* 1208:     */       }
/* 1209:     */     }
/* 1210:1713 */     if (createIfNotFound) {
/* 1211:1714 */       return (short)createFormat(format);
/* 1212:     */     }
/* 1213:1717 */     return -1;
/* 1214:     */   }
/* 1215:     */   
/* 1216:     */   public List<FormatRecord> getFormats()
/* 1217:     */   {
/* 1218:1725 */     return this.formats;
/* 1219:     */   }
/* 1220:     */   
/* 1221:     */   public int createFormat(String formatString)
/* 1222:     */   {
/* 1223:1737 */     this.maxformatid = (this.maxformatid >= 164 ? this.maxformatid + 1 : 164);
/* 1224:1738 */     FormatRecord rec = new FormatRecord(this.maxformatid, formatString);
/* 1225:     */     
/* 1226:1740 */     int pos = 0;
/* 1227:1741 */     while ((pos < this.records.size()) && (this.records.get(pos).getSid() != 1054)) {
/* 1228:1742 */       pos++;
/* 1229:     */     }
/* 1230:1744 */     pos += this.formats.size();
/* 1231:1745 */     this.formats.add(rec);
/* 1232:1746 */     this.records.add(pos, rec);
/* 1233:1747 */     return this.maxformatid;
/* 1234:     */   }
/* 1235:     */   
/* 1236:     */   public Record findFirstRecordBySid(short sid)
/* 1237:     */   {
/* 1238:1760 */     for (Record record : this.records.getRecords()) {
/* 1239:1761 */       if (record.getSid() == sid) {
/* 1240:1762 */         return record;
/* 1241:     */       }
/* 1242:     */     }
/* 1243:1765 */     return null;
/* 1244:     */   }
/* 1245:     */   
/* 1246:     */   public int findFirstRecordLocBySid(short sid)
/* 1247:     */   {
/* 1248:1774 */     int index = 0;
/* 1249:1775 */     for (Record record : this.records.getRecords())
/* 1250:     */     {
/* 1251:1776 */       if (record.getSid() == sid) {
/* 1252:1777 */         return index;
/* 1253:     */       }
/* 1254:1779 */       index++;
/* 1255:     */     }
/* 1256:1781 */     return -1;
/* 1257:     */   }
/* 1258:     */   
/* 1259:     */   public Record findNextRecordBySid(short sid, int pos)
/* 1260:     */   {
/* 1261:1793 */     int matches = 0;
/* 1262:1794 */     for (Record record : this.records.getRecords()) {
/* 1263:1795 */       if ((record.getSid() == sid) && (matches++ == pos)) {
/* 1264:1796 */         return record;
/* 1265:     */       }
/* 1266:     */     }
/* 1267:1799 */     return null;
/* 1268:     */   }
/* 1269:     */   
/* 1270:     */   public List<HyperlinkRecord> getHyperlinks()
/* 1271:     */   {
/* 1272:1804 */     return this.hyperlinks;
/* 1273:     */   }
/* 1274:     */   
/* 1275:     */   public List<Record> getRecords()
/* 1276:     */   {
/* 1277:1808 */     return this.records.getRecords();
/* 1278:     */   }
/* 1279:     */   
/* 1280:     */   public boolean isUsing1904DateWindowing()
/* 1281:     */   {
/* 1282:1818 */     return this.uses1904datewindowing;
/* 1283:     */   }
/* 1284:     */   
/* 1285:     */   public PaletteRecord getCustomPalette()
/* 1286:     */   {
/* 1287:1829 */     int palettePos = this.records.getPalettepos();
/* 1288:     */     PaletteRecord palette;
/* 1289:     */     PaletteRecord palette;
/* 1290:1830 */     if (palettePos != -1)
/* 1291:     */     {
/* 1292:1831 */       Record rec = this.records.get(palettePos);
/* 1293:     */       PaletteRecord palette;
/* 1294:1832 */       if ((rec instanceof PaletteRecord)) {
/* 1295:1833 */         palette = (PaletteRecord)rec;
/* 1296:     */       } else {
/* 1297:1835 */         throw new RuntimeException("InternalError: Expected PaletteRecord but got a '" + rec + "'");
/* 1298:     */       }
/* 1299:     */     }
/* 1300:     */     else
/* 1301:     */     {
/* 1302:1838 */       palette = createPalette();
/* 1303:     */       
/* 1304:1840 */       this.records.add(1, palette);
/* 1305:1841 */       this.records.setPalettepos(1);
/* 1306:     */     }
/* 1307:1843 */     return palette;
/* 1308:     */   }
/* 1309:     */   
/* 1310:     */   public DrawingManager2 findDrawingGroup()
/* 1311:     */   {
/* 1312:1852 */     if (this.drawingManager != null) {
/* 1313:1854 */       return this.drawingManager;
/* 1314:     */     }
/* 1315:1858 */     for (Record r : this.records.getRecords()) {
/* 1316:1859 */       if ((r instanceof DrawingGroupRecord))
/* 1317:     */       {
/* 1318:1862 */         DrawingGroupRecord dg = (DrawingGroupRecord)r;
/* 1319:1863 */         dg.processChildRecords();
/* 1320:1864 */         this.drawingManager = findDrawingManager(dg, this.escherBSERecords);
/* 1321:1865 */         if (this.drawingManager != null) {
/* 1322:1866 */           return this.drawingManager;
/* 1323:     */         }
/* 1324:     */       }
/* 1325:     */     }
/* 1326:1872 */     DrawingGroupRecord dg = (DrawingGroupRecord)findFirstRecordBySid((short)235);
/* 1327:1873 */     this.drawingManager = findDrawingManager(dg, this.escherBSERecords);
/* 1328:1874 */     return this.drawingManager;
/* 1329:     */   }
/* 1330:     */   
/* 1331:     */   private static DrawingManager2 findDrawingManager(DrawingGroupRecord dg, List<EscherBSERecord> escherBSERecords)
/* 1332:     */   {
/* 1333:1878 */     if (dg == null) {
/* 1334:1879 */       return null;
/* 1335:     */     }
/* 1336:1882 */     EscherContainerRecord cr = dg.getEscherContainer();
/* 1337:1883 */     if (cr == null) {
/* 1338:1884 */       return null;
/* 1339:     */     }
/* 1340:1887 */     EscherDggRecord dgg = null;
/* 1341:1888 */     EscherContainerRecord bStore = null;
/* 1342:1889 */     for (EscherRecord er : cr) {
/* 1343:1890 */       if ((er instanceof EscherDggRecord)) {
/* 1344:1891 */         dgg = (EscherDggRecord)er;
/* 1345:1892 */       } else if (er.getRecordId() == -4095) {
/* 1346:1893 */         bStore = (EscherContainerRecord)er;
/* 1347:     */       }
/* 1348:     */     }
/* 1349:1897 */     if (dgg == null) {
/* 1350:1898 */       return null;
/* 1351:     */     }
/* 1352:1901 */     DrawingManager2 dm = new DrawingManager2(dgg);
/* 1353:1902 */     if (bStore != null) {
/* 1354:1903 */       for (EscherRecord bs : bStore.getChildRecords()) {
/* 1355:1904 */         if ((bs instanceof EscherBSERecord)) {
/* 1356:1905 */           escherBSERecords.add((EscherBSERecord)bs);
/* 1357:     */         }
/* 1358:     */       }
/* 1359:     */     }
/* 1360:1909 */     return dm;
/* 1361:     */   }
/* 1362:     */   
/* 1363:     */   public void createDrawingGroup()
/* 1364:     */   {
/* 1365:1917 */     if (this.drawingManager == null)
/* 1366:     */     {
/* 1367:1918 */       EscherContainerRecord dggContainer = new EscherContainerRecord();
/* 1368:1919 */       EscherDggRecord dgg = new EscherDggRecord();
/* 1369:1920 */       EscherOptRecord opt = new EscherOptRecord();
/* 1370:1921 */       EscherSplitMenuColorsRecord splitMenuColors = new EscherSplitMenuColorsRecord();
/* 1371:     */       
/* 1372:1923 */       dggContainer.setRecordId((short)-4096);
/* 1373:1924 */       dggContainer.setOptions((short)15);
/* 1374:1925 */       dgg.setRecordId((short)-4090);
/* 1375:1926 */       dgg.setOptions((short)0);
/* 1376:1927 */       dgg.setShapeIdMax(1024);
/* 1377:1928 */       dgg.setNumShapesSaved(0);
/* 1378:1929 */       dgg.setDrawingsSaved(0);
/* 1379:1930 */       dgg.setFileIdClusters(new FileIdCluster[0]);
/* 1380:1931 */       this.drawingManager = new DrawingManager2(dgg);
/* 1381:1932 */       EscherContainerRecord bstoreContainer = null;
/* 1382:1933 */       if (!this.escherBSERecords.isEmpty())
/* 1383:     */       {
/* 1384:1935 */         bstoreContainer = new EscherContainerRecord();
/* 1385:1936 */         bstoreContainer.setRecordId((short)-4095);
/* 1386:1937 */         bstoreContainer.setOptions((short)(this.escherBSERecords.size() << 4 | 0xF));
/* 1387:1938 */         for (EscherRecord escherRecord : this.escherBSERecords) {
/* 1388:1939 */           bstoreContainer.addChildRecord(escherRecord);
/* 1389:     */         }
/* 1390:     */       }
/* 1391:1942 */       opt.setRecordId((short)-4085);
/* 1392:1943 */       opt.setOptions((short)51);
/* 1393:1944 */       opt.addEscherProperty(new EscherBoolProperty((short)191, 524296));
/* 1394:1945 */       opt.addEscherProperty(new EscherRGBProperty((short)385, 134217793));
/* 1395:1946 */       opt.addEscherProperty(new EscherRGBProperty((short)448, 134217792));
/* 1396:1947 */       splitMenuColors.setRecordId((short)-3810);
/* 1397:1948 */       splitMenuColors.setOptions((short)64);
/* 1398:1949 */       splitMenuColors.setColor1(134217741);
/* 1399:1950 */       splitMenuColors.setColor2(134217740);
/* 1400:1951 */       splitMenuColors.setColor3(134217751);
/* 1401:1952 */       splitMenuColors.setColor4(268435703);
/* 1402:     */       
/* 1403:1954 */       dggContainer.addChildRecord(dgg);
/* 1404:1955 */       if (bstoreContainer != null) {
/* 1405:1956 */         dggContainer.addChildRecord(bstoreContainer);
/* 1406:     */       }
/* 1407:1958 */       dggContainer.addChildRecord(opt);
/* 1408:1959 */       dggContainer.addChildRecord(splitMenuColors);
/* 1409:     */       
/* 1410:1961 */       int dgLoc = findFirstRecordLocBySid((short)235);
/* 1411:1962 */       if (dgLoc == -1)
/* 1412:     */       {
/* 1413:1963 */         DrawingGroupRecord drawingGroup = new DrawingGroupRecord();
/* 1414:1964 */         drawingGroup.addEscherRecord(dggContainer);
/* 1415:1965 */         int loc = findFirstRecordLocBySid((short)140);
/* 1416:     */         
/* 1417:1967 */         getRecords().add(loc + 1, drawingGroup);
/* 1418:     */       }
/* 1419:     */       else
/* 1420:     */       {
/* 1421:1969 */         DrawingGroupRecord drawingGroup = new DrawingGroupRecord();
/* 1422:1970 */         drawingGroup.addEscherRecord(dggContainer);
/* 1423:1971 */         getRecords().set(dgLoc, drawingGroup);
/* 1424:     */       }
/* 1425:     */     }
/* 1426:     */   }
/* 1427:     */   
/* 1428:     */   public WindowOneRecord getWindowOne()
/* 1429:     */   {
/* 1430:1978 */     return this.windowOne;
/* 1431:     */   }
/* 1432:     */   
/* 1433:     */   public EscherBSERecord getBSERecord(int pictureIndex)
/* 1434:     */   {
/* 1435:1982 */     return (EscherBSERecord)this.escherBSERecords.get(pictureIndex - 1);
/* 1436:     */   }
/* 1437:     */   
/* 1438:     */   public int addBSERecord(EscherBSERecord e)
/* 1439:     */   {
/* 1440:1986 */     createDrawingGroup();
/* 1441:     */     
/* 1442:     */ 
/* 1443:1989 */     this.escherBSERecords.add(e);
/* 1444:     */     
/* 1445:1991 */     int dgLoc = findFirstRecordLocBySid((short)235);
/* 1446:1992 */     DrawingGroupRecord drawingGroup = (DrawingGroupRecord)getRecords().get(dgLoc);
/* 1447:     */     
/* 1448:1994 */     EscherContainerRecord dggContainer = (EscherContainerRecord)drawingGroup.getEscherRecord(0);
/* 1449:     */     EscherContainerRecord bstoreContainer;
/* 1450:     */     EscherContainerRecord bstoreContainer;
/* 1451:1996 */     if (dggContainer.getChild(1).getRecordId() == -4095)
/* 1452:     */     {
/* 1453:1998 */       bstoreContainer = (EscherContainerRecord)dggContainer.getChild(1);
/* 1454:     */     }
/* 1455:     */     else
/* 1456:     */     {
/* 1457:2000 */       bstoreContainer = new EscherContainerRecord();
/* 1458:2001 */       bstoreContainer.setRecordId((short)-4095);
/* 1459:2002 */       List<EscherRecord> childRecords = dggContainer.getChildRecords();
/* 1460:2003 */       childRecords.add(1, bstoreContainer);
/* 1461:2004 */       dggContainer.setChildRecords(childRecords);
/* 1462:     */     }
/* 1463:2006 */     bstoreContainer.setOptions((short)(this.escherBSERecords.size() << 4 | 0xF));
/* 1464:     */     
/* 1465:2008 */     bstoreContainer.addChildRecord(e);
/* 1466:     */     
/* 1467:2010 */     return this.escherBSERecords.size();
/* 1468:     */   }
/* 1469:     */   
/* 1470:     */   public DrawingManager2 getDrawingManager()
/* 1471:     */   {
/* 1472:2015 */     return this.drawingManager;
/* 1473:     */   }
/* 1474:     */   
/* 1475:     */   public WriteProtectRecord getWriteProtect()
/* 1476:     */   {
/* 1477:2019 */     if (this.writeProtect == null)
/* 1478:     */     {
/* 1479:2020 */       this.writeProtect = new WriteProtectRecord();
/* 1480:2021 */       int i = findFirstRecordLocBySid((short)2057);
/* 1481:2022 */       this.records.add(i + 1, this.writeProtect);
/* 1482:     */     }
/* 1483:2024 */     return this.writeProtect;
/* 1484:     */   }
/* 1485:     */   
/* 1486:     */   public WriteAccessRecord getWriteAccess()
/* 1487:     */   {
/* 1488:2028 */     if (this.writeAccess == null)
/* 1489:     */     {
/* 1490:2029 */       this.writeAccess = createWriteAccess();
/* 1491:2030 */       int i = findFirstRecordLocBySid((short)226);
/* 1492:2031 */       this.records.add(i + 1, this.writeAccess);
/* 1493:     */     }
/* 1494:2033 */     return this.writeAccess;
/* 1495:     */   }
/* 1496:     */   
/* 1497:     */   public FileSharingRecord getFileSharing()
/* 1498:     */   {
/* 1499:2037 */     if (this.fileShare == null)
/* 1500:     */     {
/* 1501:2038 */       this.fileShare = new FileSharingRecord();
/* 1502:2039 */       int i = findFirstRecordLocBySid((short)92);
/* 1503:2040 */       this.records.add(i + 1, this.fileShare);
/* 1504:     */     }
/* 1505:2042 */     return this.fileShare;
/* 1506:     */   }
/* 1507:     */   
/* 1508:     */   public boolean isWriteProtected()
/* 1509:     */   {
/* 1510:2051 */     if (this.fileShare == null) {
/* 1511:2052 */       return false;
/* 1512:     */     }
/* 1513:2054 */     FileSharingRecord frec = getFileSharing();
/* 1514:2055 */     return frec.getReadOnly() == 1;
/* 1515:     */   }
/* 1516:     */   
/* 1517:     */   public void writeProtectWorkbook(String password, String username)
/* 1518:     */   {
/* 1519:2066 */     FileSharingRecord frec = getFileSharing();
/* 1520:2067 */     WriteAccessRecord waccess = getWriteAccess();
/* 1521:2068 */     getWriteProtect();
/* 1522:2069 */     frec.setReadOnly((short)1);
/* 1523:2070 */     frec.setPassword((short)CryptoFunctions.createXorVerifier1(password));
/* 1524:2071 */     frec.setUsername(username);
/* 1525:2072 */     waccess.setUsername(username);
/* 1526:     */   }
/* 1527:     */   
/* 1528:     */   public void unwriteProtectWorkbook()
/* 1529:     */   {
/* 1530:2079 */     this.records.remove(this.fileShare);
/* 1531:2080 */     this.records.remove(this.writeProtect);
/* 1532:2081 */     this.fileShare = null;
/* 1533:2082 */     this.writeProtect = null;
/* 1534:     */   }
/* 1535:     */   
/* 1536:     */   public String resolveNameXText(int refIndex, int definedNameIndex)
/* 1537:     */   {
/* 1538:2091 */     return this.linkTable.resolveNameXText(refIndex, definedNameIndex, this);
/* 1539:     */   }
/* 1540:     */   
/* 1541:     */   public NameXPtg getNameXPtg(String name, int sheetRefIndex, UDFFinder udf)
/* 1542:     */   {
/* 1543:2102 */     LinkTable lnk = getOrCreateLinkTable();
/* 1544:2103 */     NameXPtg xptg = lnk.getNameXPtg(name, sheetRefIndex);
/* 1545:2105 */     if ((xptg == null) && (udf.findFunction(name) != null)) {
/* 1546:2108 */       xptg = lnk.addNameXPtg(name);
/* 1547:     */     }
/* 1548:2110 */     return xptg;
/* 1549:     */   }
/* 1550:     */   
/* 1551:     */   public NameXPtg getNameXPtg(String name, UDFFinder udf)
/* 1552:     */   {
/* 1553:2113 */     return getNameXPtg(name, -1, udf);
/* 1554:     */   }
/* 1555:     */   
/* 1556:     */   public void cloneDrawings(InternalSheet sheet)
/* 1557:     */   {
/* 1558:2124 */     findDrawingGroup();
/* 1559:2126 */     if (this.drawingManager == null) {
/* 1560:2128 */       return;
/* 1561:     */     }
/* 1562:2132 */     int aggLoc = sheet.aggregateDrawingRecords(this.drawingManager, false);
/* 1563:2133 */     if (aggLoc == -1) {
/* 1564:2134 */       return;
/* 1565:     */     }
/* 1566:2137 */     EscherAggregate agg = (EscherAggregate)sheet.findFirstRecordBySid((short)9876);
/* 1567:2138 */     EscherContainerRecord escherContainer = agg.getEscherContainer();
/* 1568:2139 */     if (escherContainer == null) {
/* 1569:2140 */       return;
/* 1570:     */     }
/* 1571:2143 */     EscherDggRecord dgg = this.drawingManager.getDgg();
/* 1572:     */     
/* 1573:     */ 
/* 1574:2146 */     int dgId = this.drawingManager.findNewDrawingGroupId();
/* 1575:2147 */     dgg.addCluster(dgId, 0);
/* 1576:2148 */     dgg.setDrawingsSaved(dgg.getDrawingsSaved() + 1);
/* 1577:     */     
/* 1578:2150 */     EscherDgRecord dg = null;
/* 1579:2151 */     for (EscherRecord er : escherContainer) {
/* 1580:2152 */       if ((er instanceof EscherDgRecord))
/* 1581:     */       {
/* 1582:2153 */         dg = (EscherDgRecord)er;
/* 1583:     */         
/* 1584:2155 */         dg.setOptions((short)(dgId << 4));
/* 1585:     */       }
/* 1586:2156 */       else if ((er instanceof EscherContainerRecord))
/* 1587:     */       {
/* 1588:2158 */         for (EscherRecord er2 : (EscherContainerRecord)er) {
/* 1589:2159 */           for (EscherRecord shapeChildRecord : (EscherContainerRecord)er2)
/* 1590:     */           {
/* 1591:2160 */             int recordId = shapeChildRecord.getRecordId();
/* 1592:2161 */             if (recordId == -4086)
/* 1593:     */             {
/* 1594:2162 */               if (dg == null) {
/* 1595:2163 */                 throw new RecordFormatException("EscherDgRecord wasn't set/processed before.");
/* 1596:     */               }
/* 1597:2165 */               EscherSpRecord sp = (EscherSpRecord)shapeChildRecord;
/* 1598:2166 */               int shapeId = this.drawingManager.allocateShapeId(dg);
/* 1599:     */               
/* 1600:2168 */               dg.setNumShapes(dg.getNumShapes() - 1);
/* 1601:2169 */               sp.setShapeId(shapeId);
/* 1602:     */             }
/* 1603:2170 */             else if (recordId == -4085)
/* 1604:     */             {
/* 1605:2171 */               EscherOptRecord opt = (EscherOptRecord)shapeChildRecord;
/* 1606:2172 */               EscherSimpleProperty prop = (EscherSimpleProperty)opt.lookup(260);
/* 1607:2174 */               if (prop != null)
/* 1608:     */               {
/* 1609:2175 */                 int pictureIndex = prop.getPropertyValue();
/* 1610:     */                 
/* 1611:2177 */                 EscherBSERecord bse = getBSERecord(pictureIndex);
/* 1612:2178 */                 bse.setRef(bse.getRef() + 1);
/* 1613:     */               }
/* 1614:     */             }
/* 1615:     */           }
/* 1616:     */         }
/* 1617:     */       }
/* 1618:     */     }
/* 1619:     */   }
/* 1620:     */   
/* 1621:     */   public NameRecord cloneFilter(int filterDbNameIndex, int newSheetIndex)
/* 1622:     */   {
/* 1623:2189 */     NameRecord origNameRecord = getNameRecord(filterDbNameIndex);
/* 1624:     */     
/* 1625:2191 */     int newExtSheetIx = checkExternSheet(newSheetIndex);
/* 1626:2192 */     Ptg[] ptgs = origNameRecord.getNameDefinition();
/* 1627:2193 */     for (int i = 0; i < ptgs.length; i++)
/* 1628:     */     {
/* 1629:2194 */       Ptg ptg = ptgs[i];
/* 1630:2196 */       if ((ptg instanceof Area3DPtg))
/* 1631:     */       {
/* 1632:2197 */         Area3DPtg a3p = (Area3DPtg)((OperandPtg)ptg).copy();
/* 1633:2198 */         a3p.setExternSheetIndex(newExtSheetIx);
/* 1634:2199 */         ptgs[i] = a3p;
/* 1635:     */       }
/* 1636:2200 */       else if ((ptg instanceof Ref3DPtg))
/* 1637:     */       {
/* 1638:2201 */         Ref3DPtg r3p = (Ref3DPtg)((OperandPtg)ptg).copy();
/* 1639:2202 */         r3p.setExternSheetIndex(newExtSheetIx);
/* 1640:2203 */         ptgs[i] = r3p;
/* 1641:     */       }
/* 1642:     */     }
/* 1643:2206 */     NameRecord newNameRecord = createBuiltInName((byte)13, newSheetIndex + 1);
/* 1644:2207 */     newNameRecord.setNameDefinition(ptgs);
/* 1645:2208 */     newNameRecord.setHidden(true);
/* 1646:2209 */     return newNameRecord;
/* 1647:     */   }
/* 1648:     */   
/* 1649:     */   public void updateNamesAfterCellShift(FormulaShifter shifter)
/* 1650:     */   {
/* 1651:2218 */     for (int i = 0; i < getNumNames(); i++)
/* 1652:     */     {
/* 1653:2219 */       NameRecord nr = getNameRecord(i);
/* 1654:2220 */       Ptg[] ptgs = nr.getNameDefinition();
/* 1655:2221 */       if (shifter.adjustFormula(ptgs, nr.getSheetNumber())) {
/* 1656:2222 */         nr.setNameDefinition(ptgs);
/* 1657:     */       }
/* 1658:     */     }
/* 1659:     */   }
/* 1660:     */   
/* 1661:     */   public RecalcIdRecord getRecalcId()
/* 1662:     */   {
/* 1663:2235 */     RecalcIdRecord record = (RecalcIdRecord)findFirstRecordBySid((short)449);
/* 1664:2236 */     if (record == null)
/* 1665:     */     {
/* 1666:2237 */       record = new RecalcIdRecord();
/* 1667:     */       
/* 1668:2239 */       int pos = findFirstRecordLocBySid((short)140);
/* 1669:2240 */       this.records.add(pos + 1, record);
/* 1670:     */     }
/* 1671:2242 */     return record;
/* 1672:     */   }
/* 1673:     */   
/* 1674:     */   public boolean changeExternalReference(String oldUrl, String newUrl)
/* 1675:     */   {
/* 1676:2256 */     return this.linkTable.changeExternalReference(oldUrl, newUrl);
/* 1677:     */   }
/* 1678:     */   
/* 1679:     */   @Internal
/* 1680:     */   public WorkbookRecordList getWorkbookRecordList()
/* 1681:     */   {
/* 1682:2264 */     return this.records;
/* 1683:     */   }
/* 1684:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.model.InternalWorkbook

 * JD-Core Version:    0.7.0.1

 */