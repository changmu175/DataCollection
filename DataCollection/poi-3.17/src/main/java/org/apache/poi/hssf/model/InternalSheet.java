/*    1:     */ package org.apache.poi.hssf.model;
/*    2:     */ 
/*    3:     */ import java.util.ArrayList;
/*    4:     */ import java.util.Iterator;
/*    5:     */ import java.util.List;
/*    6:     */ import org.apache.poi.hssf.record.BOFRecord;
/*    7:     */ import org.apache.poi.hssf.record.CalcCountRecord;
/*    8:     */ import org.apache.poi.hssf.record.CalcModeRecord;
/*    9:     */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*   10:     */ import org.apache.poi.hssf.record.ColumnInfoRecord;
/*   11:     */ import org.apache.poi.hssf.record.DefaultColWidthRecord;
/*   12:     */ import org.apache.poi.hssf.record.DefaultRowHeightRecord;
/*   13:     */ import org.apache.poi.hssf.record.DeltaRecord;
/*   14:     */ import org.apache.poi.hssf.record.DimensionsRecord;
/*   15:     */ import org.apache.poi.hssf.record.DrawingRecord;
/*   16:     */ import org.apache.poi.hssf.record.EOFRecord;
/*   17:     */ import org.apache.poi.hssf.record.EscherAggregate;
/*   18:     */ import org.apache.poi.hssf.record.GridsetRecord;
/*   19:     */ import org.apache.poi.hssf.record.GutsRecord;
/*   20:     */ import org.apache.poi.hssf.record.IterationRecord;
/*   21:     */ import org.apache.poi.hssf.record.NoteRecord;
/*   22:     */ import org.apache.poi.hssf.record.PaneRecord;
/*   23:     */ import org.apache.poi.hssf.record.PrintGridlinesRecord;
/*   24:     */ import org.apache.poi.hssf.record.PrintHeadersRecord;
/*   25:     */ import org.apache.poi.hssf.record.Record;
/*   26:     */ import org.apache.poi.hssf.record.RecordBase;
/*   27:     */ import org.apache.poi.hssf.record.RefModeRecord;
/*   28:     */ import org.apache.poi.hssf.record.RowRecord;
/*   29:     */ import org.apache.poi.hssf.record.SCLRecord;
/*   30:     */ import org.apache.poi.hssf.record.SaveRecalcRecord;
/*   31:     */ import org.apache.poi.hssf.record.SelectionRecord;
/*   32:     */ import org.apache.poi.hssf.record.UncalcedRecord;
/*   33:     */ import org.apache.poi.hssf.record.WSBoolRecord;
/*   34:     */ import org.apache.poi.hssf.record.WindowTwoRecord;
/*   35:     */ import org.apache.poi.hssf.record.aggregates.ChartSubstreamRecordAggregate;
/*   36:     */ import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
/*   37:     */ import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
/*   38:     */ import org.apache.poi.hssf.record.aggregates.CustomViewSettingsRecordAggregate;
/*   39:     */ import org.apache.poi.hssf.record.aggregates.DataValidityTable;
/*   40:     */ import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
/*   41:     */ import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
/*   42:     */ import org.apache.poi.hssf.record.aggregates.RecordAggregate;
/*   43:     */ import org.apache.poi.hssf.record.aggregates.RecordAggregate.PositionTrackingVisitor;
/*   44:     */ import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
/*   45:     */ import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate;
/*   46:     */ import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
/*   47:     */ import org.apache.poi.ss.formula.FormulaShifter;
/*   48:     */ import org.apache.poi.ss.util.CellRangeAddress;
/*   49:     */ import org.apache.poi.ss.util.PaneInformation;
/*   50:     */ import org.apache.poi.util.Internal;
/*   51:     */ import org.apache.poi.util.POILogFactory;
/*   52:     */ import org.apache.poi.util.POILogger;
/*   53:     */ import org.apache.poi.util.RecordFormatException;
/*   54:     */ 
/*   55:     */ @Internal
/*   56:     */ public final class InternalSheet
/*   57:     */ {
/*   58:     */   public static final short LeftMargin = 0;
/*   59:     */   public static final short RightMargin = 1;
/*   60:     */   public static final short TopMargin = 2;
/*   61:     */   public static final short BottomMargin = 3;
/*   62:  66 */   private static POILogger log = POILogFactory.getLogger(InternalSheet.class);
/*   63:     */   private List<RecordBase> _records;
/*   64:  69 */   protected PrintGridlinesRecord printGridlines = null;
/*   65:  70 */   protected PrintHeadersRecord printHeaders = null;
/*   66:  71 */   protected GridsetRecord gridset = null;
/*   67:     */   private GutsRecord _gutsRecord;
/*   68:  73 */   protected DefaultColWidthRecord defaultcolwidth = new DefaultColWidthRecord();
/*   69:  74 */   protected DefaultRowHeightRecord defaultrowheight = new DefaultRowHeightRecord();
/*   70:     */   private PageSettingsBlock _psBlock;
/*   71:  81 */   private final WorksheetProtectionBlock _protectionBlock = new WorksheetProtectionBlock();
/*   72:  83 */   protected WindowTwoRecord windowTwo = null;
/*   73:  84 */   protected SelectionRecord _selection = null;
/*   74:     */   private final MergedCellsTable _mergedCellsTable;
/*   75:     */   ColumnInfoRecordsAggregate _columnInfos;
/*   76:     */   private DimensionsRecord _dimensions;
/*   77:     */   protected final RowRecordsAggregate _rowsAggregate;
/*   78:  93 */   private DataValidityTable _dataValidityTable = null;
/*   79:     */   private ConditionalFormattingTable condFormatting;
/*   80:  96 */   private Iterator<RowRecord> rowRecIterator = null;
/*   81:  99 */   protected boolean _isUncalced = false;
/*   82:     */   public static final byte PANE_LOWER_RIGHT = 0;
/*   83:     */   public static final byte PANE_UPPER_RIGHT = 1;
/*   84:     */   public static final byte PANE_LOWER_LEFT = 2;
/*   85:     */   public static final byte PANE_UPPER_LEFT = 3;
/*   86:     */   
/*   87:     */   public static InternalSheet createSheet(RecordStream rs)
/*   88:     */   {
/*   89: 122 */     return new InternalSheet(rs);
/*   90:     */   }
/*   91:     */   
/*   92:     */   private InternalSheet(RecordStream rs)
/*   93:     */   {
/*   94: 125 */     this._mergedCellsTable = new MergedCellsTable();
/*   95: 126 */     RowRecordsAggregate rra = null;
/*   96:     */     
/*   97: 128 */     List<RecordBase> records = new ArrayList(128);
/*   98: 129 */     this._records = records;
/*   99: 130 */     int dimsloc = -1;
/*  100: 132 */     if (rs.peekNextSid() != 2057) {
/*  101: 133 */       throw new RecordFormatException("BOF record expected");
/*  102:     */     }
/*  103: 136 */     BOFRecord bof = (BOFRecord)rs.getNext();
/*  104: 137 */     if (bof.getType() != 16) {
/*  105: 139 */       if ((bof.getType() != 32) && (bof.getType() != 64))
/*  106:     */       {
/*  107: 146 */         while (rs.hasNext())
/*  108:     */         {
/*  109: 147 */           Record rec = rs.getNext();
/*  110: 148 */           if ((rec instanceof EOFRecord)) {
/*  111:     */             break;
/*  112:     */           }
/*  113:     */         }
/*  114: 152 */         throw new UnsupportedBOFType(bof.getType());
/*  115:     */       }
/*  116:     */     }
/*  117: 154 */     records.add(bof);
/*  118: 156 */     while (rs.hasNext())
/*  119:     */     {
/*  120: 157 */       int recSid = rs.peekNextSid();
/*  121: 159 */       if ((recSid == 432) || (recSid == 2169))
/*  122:     */       {
/*  123: 160 */         this.condFormatting = new ConditionalFormattingTable(rs);
/*  124: 161 */         records.add(this.condFormatting);
/*  125:     */       }
/*  126: 165 */       else if (recSid == 125)
/*  127:     */       {
/*  128: 166 */         this._columnInfos = new ColumnInfoRecordsAggregate(rs);
/*  129: 167 */         records.add(this._columnInfos);
/*  130:     */       }
/*  131: 170 */       else if (recSid == 434)
/*  132:     */       {
/*  133: 171 */         this._dataValidityTable = new DataValidityTable(rs);
/*  134: 172 */         records.add(this._dataValidityTable);
/*  135:     */       }
/*  136: 176 */       else if (RecordOrderer.isRowBlockRecord(recSid))
/*  137:     */       {
/*  138: 178 */         if (rra != null) {
/*  139: 179 */           throw new RecordFormatException("row/cell records found in the wrong place");
/*  140:     */         }
/*  141: 181 */         RowBlocksReader rbr = new RowBlocksReader(rs);
/*  142: 182 */         this._mergedCellsTable.addRecords(rbr.getLooseMergedCells());
/*  143: 183 */         rra = new RowRecordsAggregate(rbr.getPlainRecordStream(), rbr.getSharedFormulaManager());
/*  144: 184 */         records.add(rra);
/*  145:     */       }
/*  146: 188 */       else if (CustomViewSettingsRecordAggregate.isBeginRecord(recSid))
/*  147:     */       {
/*  148: 191 */         records.add(new CustomViewSettingsRecordAggregate(rs));
/*  149:     */       }
/*  150: 195 */       else if (PageSettingsBlock.isComponentRecord(recSid))
/*  151:     */       {
/*  152: 196 */         if (this._psBlock == null)
/*  153:     */         {
/*  154: 198 */           this._psBlock = new PageSettingsBlock(rs);
/*  155: 199 */           records.add(this._psBlock);
/*  156:     */         }
/*  157:     */         else
/*  158:     */         {
/*  159: 202 */           this._psBlock.addLateRecords(rs);
/*  160:     */         }
/*  161: 206 */         this._psBlock.positionRecords(records);
/*  162:     */       }
/*  163: 210 */       else if (WorksheetProtectionBlock.isComponentRecord(recSid))
/*  164:     */       {
/*  165: 211 */         this._protectionBlock.addRecords(rs);
/*  166:     */       }
/*  167: 215 */       else if (recSid == 229)
/*  168:     */       {
/*  169: 217 */         this._mergedCellsTable.read(rs);
/*  170:     */       }
/*  171: 221 */       else if (recSid == 2057)
/*  172:     */       {
/*  173: 222 */         ChartSubstreamRecordAggregate chartAgg = new ChartSubstreamRecordAggregate(rs);
/*  174:     */         
/*  175:     */ 
/*  176:     */ 
/*  177:     */ 
/*  178: 227 */         spillAggregate(chartAgg, records);
/*  179:     */       }
/*  180:     */       else
/*  181:     */       {
/*  182: 232 */         Record rec = rs.getNext();
/*  183: 233 */         if (recSid != 523) {
/*  184: 240 */           if (recSid == 94)
/*  185:     */           {
/*  186: 242 */             this._isUncalced = true;
/*  187:     */           }
/*  188: 246 */           else if ((recSid == 2152) || (recSid == 2151))
/*  189:     */           {
/*  190: 248 */             records.add(rec);
/*  191:     */           }
/*  192:     */           else
/*  193:     */           {
/*  194: 252 */             if (recSid == 10)
/*  195:     */             {
/*  196: 253 */               records.add(rec);
/*  197: 254 */               break;
/*  198:     */             }
/*  199: 257 */             if (recSid == 512)
/*  200:     */             {
/*  201: 260 */               if (this._columnInfos == null)
/*  202:     */               {
/*  203: 262 */                 this._columnInfos = new ColumnInfoRecordsAggregate();
/*  204: 263 */                 records.add(this._columnInfos);
/*  205:     */               }
/*  206: 266 */               this._dimensions = ((DimensionsRecord)rec);
/*  207: 267 */               dimsloc = records.size();
/*  208:     */             }
/*  209: 269 */             else if (recSid == 85)
/*  210:     */             {
/*  211: 271 */               this.defaultcolwidth = ((DefaultColWidthRecord)rec);
/*  212:     */             }
/*  213: 273 */             else if (recSid == 549)
/*  214:     */             {
/*  215: 275 */               this.defaultrowheight = ((DefaultRowHeightRecord)rec);
/*  216:     */             }
/*  217: 277 */             else if (recSid == 43)
/*  218:     */             {
/*  219: 279 */               this.printGridlines = ((PrintGridlinesRecord)rec);
/*  220:     */             }
/*  221: 281 */             else if (recSid == 42)
/*  222:     */             {
/*  223: 283 */               this.printHeaders = ((PrintHeadersRecord)rec);
/*  224:     */             }
/*  225: 285 */             else if (recSid == 130)
/*  226:     */             {
/*  227: 287 */               this.gridset = ((GridsetRecord)rec);
/*  228:     */             }
/*  229: 289 */             else if (recSid == 29)
/*  230:     */             {
/*  231: 291 */               this._selection = ((SelectionRecord)rec);
/*  232:     */             }
/*  233: 293 */             else if (recSid == 574)
/*  234:     */             {
/*  235: 295 */               this.windowTwo = ((WindowTwoRecord)rec);
/*  236:     */             }
/*  237: 297 */             else if (recSid == 128)
/*  238:     */             {
/*  239: 299 */               this._gutsRecord = ((GutsRecord)rec);
/*  240:     */             }
/*  241: 302 */             records.add(rec);
/*  242:     */           }
/*  243:     */         }
/*  244:     */       }
/*  245:     */     }
/*  246: 304 */     if (this.windowTwo == null) {
/*  247: 305 */       throw new RecordFormatException("WINDOW2 was not found");
/*  248:     */     }
/*  249: 307 */     if (this._dimensions == null)
/*  250:     */     {
/*  251: 310 */       if (rra == null) {
/*  252: 314 */         rra = new RowRecordsAggregate();
/*  253: 316 */       } else if (log.check(5)) {
/*  254: 317 */         log.log(5, new Object[] { "DIMENSION record not found even though row/cells present" });
/*  255:     */       }
/*  256: 321 */       dimsloc = findFirstRecordLocBySid((short)574);
/*  257: 322 */       this._dimensions = rra.createDimensions();
/*  258: 323 */       records.add(dimsloc, this._dimensions);
/*  259:     */     }
/*  260: 325 */     if (rra == null)
/*  261:     */     {
/*  262: 326 */       rra = new RowRecordsAggregate();
/*  263: 327 */       records.add(dimsloc + 1, rra);
/*  264:     */     }
/*  265: 329 */     this._rowsAggregate = rra;
/*  266:     */     
/*  267: 331 */     RecordOrderer.addNewSheetRecord(records, this._mergedCellsTable);
/*  268: 332 */     RecordOrderer.addNewSheetRecord(records, this._protectionBlock);
/*  269: 333 */     if (log.check(1)) {
/*  270: 334 */       log.log(1, new Object[] { "sheet createSheet (existing file) exited" });
/*  271:     */     }
/*  272:     */   }
/*  273:     */   
/*  274:     */   private static void spillAggregate(RecordAggregate ra, List<RecordBase> recs)
/*  275:     */   {
/*  276: 337 */     ra.visitContainedRecords(new RecordAggregate.RecordVisitor()
/*  277:     */     {
/*  278:     */       public void visitRecord(Record r)
/*  279:     */       {
/*  280: 339 */         this.val$recs.add(r);
/*  281:     */       }
/*  282:     */     });
/*  283:     */   }
/*  284:     */   
/*  285:     */   public static class UnsupportedBOFType
/*  286:     */     extends RecordFormatException
/*  287:     */   {
/*  288:     */     private final int type;
/*  289:     */     
/*  290:     */     protected UnsupportedBOFType(int type)
/*  291:     */     {
/*  292: 346 */       super();
/*  293: 347 */       this.type = type;
/*  294:     */     }
/*  295:     */     
/*  296:     */     public int getType()
/*  297:     */     {
/*  298: 351 */       return this.type;
/*  299:     */     }
/*  300:     */   }
/*  301:     */   
/*  302:     */   private static final class RecordCloner
/*  303:     */     implements RecordAggregate.RecordVisitor
/*  304:     */   {
/*  305:     */     private final List<Record> _destList;
/*  306:     */     
/*  307:     */     public RecordCloner(List<Record> destList)
/*  308:     */     {
/*  309: 360 */       this._destList = destList;
/*  310:     */     }
/*  311:     */     
/*  312:     */     public void visitRecord(Record r)
/*  313:     */     {
/*  314:     */       try
/*  315:     */       {
/*  316: 364 */         this._destList.add((Record)r.clone());
/*  317:     */       }
/*  318:     */       catch (CloneNotSupportedException e)
/*  319:     */       {
/*  320: 366 */         throw new RecordFormatException(e);
/*  321:     */       }
/*  322:     */     }
/*  323:     */   }
/*  324:     */   
/*  325:     */   public InternalSheet cloneSheet()
/*  326:     */   {
/*  327: 381 */     List<Record> clonedRecords = new ArrayList(this._records.size());
/*  328: 382 */     for (int i = 0; i < this._records.size(); i++)
/*  329:     */     {
/*  330: 383 */       RecordBase rb = (RecordBase)this._records.get(i);
/*  331: 384 */       if ((rb instanceof RecordAggregate))
/*  332:     */       {
/*  333: 385 */         ((RecordAggregate)rb).visitContainedRecords(new RecordCloner(clonedRecords));
/*  334:     */       }
/*  335:     */       else
/*  336:     */       {
/*  337: 388 */         if ((rb instanceof EscherAggregate)) {
/*  338: 392 */           rb = new DrawingRecord();
/*  339:     */         }
/*  340:     */         try
/*  341:     */         {
/*  342: 395 */           Record rec = (Record)((Record)rb).clone();
/*  343: 396 */           clonedRecords.add(rec);
/*  344:     */         }
/*  345:     */         catch (CloneNotSupportedException e)
/*  346:     */         {
/*  347: 398 */           throw new RecordFormatException(e);
/*  348:     */         }
/*  349:     */       }
/*  350:     */     }
/*  351: 401 */     return createSheet(new RecordStream(clonedRecords, 0));
/*  352:     */   }
/*  353:     */   
/*  354:     */   public static InternalSheet createSheet()
/*  355:     */   {
/*  356: 412 */     return new InternalSheet();
/*  357:     */   }
/*  358:     */   
/*  359:     */   private InternalSheet()
/*  360:     */   {
/*  361: 415 */     this._mergedCellsTable = new MergedCellsTable();
/*  362: 416 */     List<RecordBase> records = new ArrayList(32);
/*  363: 418 */     if (log.check(1)) {
/*  364: 419 */       log.log(1, new Object[] { "Sheet createsheet from scratch called" });
/*  365:     */     }
/*  366: 421 */     records.add(createBOF());
/*  367:     */     
/*  368: 423 */     records.add(createCalcMode());
/*  369: 424 */     records.add(createCalcCount());
/*  370: 425 */     records.add(createRefMode());
/*  371: 426 */     records.add(createIteration());
/*  372: 427 */     records.add(createDelta());
/*  373: 428 */     records.add(createSaveRecalc());
/*  374: 429 */     this.printHeaders = createPrintHeaders();
/*  375: 430 */     records.add(this.printHeaders);
/*  376: 431 */     this.printGridlines = createPrintGridlines();
/*  377: 432 */     records.add(this.printGridlines);
/*  378: 433 */     this.gridset = createGridset();
/*  379: 434 */     records.add(this.gridset);
/*  380: 435 */     this._gutsRecord = createGuts();
/*  381: 436 */     records.add(this._gutsRecord);
/*  382: 437 */     this.defaultrowheight = createDefaultRowHeight();
/*  383: 438 */     records.add(this.defaultrowheight);
/*  384: 439 */     records.add(createWSBool());
/*  385:     */     
/*  386:     */ 
/*  387: 442 */     this._psBlock = new PageSettingsBlock();
/*  388: 443 */     records.add(this._psBlock);
/*  389:     */     
/*  390:     */ 
/*  391: 446 */     records.add(this._protectionBlock);
/*  392:     */     
/*  393: 448 */     this.defaultcolwidth = createDefaultColWidth();
/*  394: 449 */     records.add(this.defaultcolwidth);
/*  395: 450 */     ColumnInfoRecordsAggregate columns = new ColumnInfoRecordsAggregate();
/*  396: 451 */     records.add(columns);
/*  397: 452 */     this._columnInfos = columns;
/*  398: 453 */     this._dimensions = createDimensions();
/*  399: 454 */     records.add(this._dimensions);
/*  400: 455 */     this._rowsAggregate = new RowRecordsAggregate();
/*  401: 456 */     records.add(this._rowsAggregate);
/*  402:     */     
/*  403: 458 */     records.add(this.windowTwo = createWindowTwo());
/*  404: 459 */     this._selection = createSelection();
/*  405: 460 */     records.add(this._selection);
/*  406:     */     
/*  407: 462 */     records.add(this._mergedCellsTable);
/*  408: 463 */     records.add(EOFRecord.instance);
/*  409:     */     
/*  410: 465 */     this._records = records;
/*  411: 466 */     if (log.check(1)) {
/*  412: 467 */       log.log(1, new Object[] { "Sheet createsheet from scratch exit" });
/*  413:     */     }
/*  414:     */   }
/*  415:     */   
/*  416:     */   public RowRecordsAggregate getRowsAggregate()
/*  417:     */   {
/*  418: 471 */     return this._rowsAggregate;
/*  419:     */   }
/*  420:     */   
/*  421:     */   private MergedCellsTable getMergedRecords()
/*  422:     */   {
/*  423: 476 */     return this._mergedCellsTable;
/*  424:     */   }
/*  425:     */   
/*  426:     */   public void updateFormulasAfterCellShift(FormulaShifter shifter, int externSheetIndex)
/*  427:     */   {
/*  428: 486 */     getRowsAggregate().updateFormulasAfterRowShift(shifter, externSheetIndex);
/*  429: 487 */     if (this.condFormatting != null) {
/*  430: 488 */       getConditionalFormattingTable().updateFormulasAfterCellShift(shifter, externSheetIndex);
/*  431:     */     }
/*  432:     */   }
/*  433:     */   
/*  434:     */   public int addMergedRegion(int rowFrom, int colFrom, int rowTo, int colTo)
/*  435:     */   {
/*  436: 495 */     if (rowTo < rowFrom) {
/*  437: 496 */       throw new IllegalArgumentException("The 'to' row (" + rowTo + ") must not be less than the 'from' row (" + rowFrom + ")");
/*  438:     */     }
/*  439: 499 */     if (colTo < colFrom) {
/*  440: 500 */       throw new IllegalArgumentException("The 'to' col (" + colTo + ") must not be less than the 'from' col (" + colFrom + ")");
/*  441:     */     }
/*  442: 504 */     MergedCellsTable mrt = getMergedRecords();
/*  443: 505 */     mrt.addArea(rowFrom, colFrom, rowTo, colTo);
/*  444: 506 */     return mrt.getNumberOfMergedRegions() - 1;
/*  445:     */   }
/*  446:     */   
/*  447:     */   public void removeMergedRegion(int index)
/*  448:     */   {
/*  449: 511 */     MergedCellsTable mrt = getMergedRecords();
/*  450: 512 */     if (index >= mrt.getNumberOfMergedRegions()) {
/*  451: 513 */       return;
/*  452:     */     }
/*  453: 515 */     mrt.remove(index);
/*  454:     */   }
/*  455:     */   
/*  456:     */   public CellRangeAddress getMergedRegionAt(int index)
/*  457:     */   {
/*  458: 520 */     MergedCellsTable mrt = getMergedRecords();
/*  459: 521 */     if (index >= mrt.getNumberOfMergedRegions()) {
/*  460: 522 */       return null;
/*  461:     */     }
/*  462: 524 */     return mrt.get(index);
/*  463:     */   }
/*  464:     */   
/*  465:     */   public int getNumMergedRegions()
/*  466:     */   {
/*  467: 528 */     return getMergedRecords().getNumberOfMergedRegions();
/*  468:     */   }
/*  469:     */   
/*  470:     */   public ConditionalFormattingTable getConditionalFormattingTable()
/*  471:     */   {
/*  472: 531 */     if (this.condFormatting == null)
/*  473:     */     {
/*  474: 532 */       this.condFormatting = new ConditionalFormattingTable();
/*  475: 533 */       RecordOrderer.addNewSheetRecord(this._records, this.condFormatting);
/*  476:     */     }
/*  477: 535 */     return this.condFormatting;
/*  478:     */   }
/*  479:     */   
/*  480:     */   public void setDimensions(int firstrow, short firstcol, int lastrow, short lastcol)
/*  481:     */   {
/*  482: 552 */     if (log.check(1))
/*  483:     */     {
/*  484: 554 */       log.log(1, new Object[] { "Sheet.setDimensions" });
/*  485: 555 */       log.log(1, new Object[] { "firstrow" + firstrow + "firstcol" + firstcol + "lastrow" + lastrow + "lastcol" + lastcol });
/*  486:     */     }
/*  487: 561 */     this._dimensions.setFirstCol(firstcol);
/*  488: 562 */     this._dimensions.setFirstRow(firstrow);
/*  489: 563 */     this._dimensions.setLastCol(lastcol);
/*  490: 564 */     this._dimensions.setLastRow(lastrow);
/*  491: 565 */     if (log.check(1)) {
/*  492: 566 */       log.log(1, new Object[] { "Sheet.setDimensions exiting" });
/*  493:     */     }
/*  494:     */   }
/*  495:     */   
/*  496:     */   public void visitContainedRecords(RecordAggregate.RecordVisitor rv, int offset)
/*  497:     */   {
/*  498: 571 */     RecordAggregate.PositionTrackingVisitor ptv = new RecordAggregate.PositionTrackingVisitor(rv, offset);
/*  499:     */     
/*  500: 573 */     boolean haveSerializedIndex = false;
/*  501: 575 */     for (int k = 0; k < this._records.size(); k++)
/*  502:     */     {
/*  503: 576 */       RecordBase record = (RecordBase)this._records.get(k);
/*  504: 578 */       if ((record instanceof RecordAggregate))
/*  505:     */       {
/*  506: 579 */         RecordAggregate agg = (RecordAggregate)record;
/*  507: 580 */         agg.visitContainedRecords(ptv);
/*  508:     */       }
/*  509:     */       else
/*  510:     */       {
/*  511: 582 */         ptv.visitRecord((Record)record);
/*  512:     */       }
/*  513: 586 */       if (((record instanceof BOFRecord)) && 
/*  514: 587 */         (!haveSerializedIndex))
/*  515:     */       {
/*  516: 588 */         haveSerializedIndex = true;
/*  517: 593 */         if (this._isUncalced) {
/*  518: 594 */           ptv.visitRecord(new UncalcedRecord());
/*  519:     */         }
/*  520: 598 */         if (this._rowsAggregate != null)
/*  521:     */         {
/*  522: 600 */           int initRecsSize = getSizeOfInitialSheetRecords(k);
/*  523: 601 */           int currentPos = ptv.getPosition();
/*  524: 602 */           ptv.visitRecord(this._rowsAggregate.createIndexRecord(currentPos, initRecsSize));
/*  525:     */         }
/*  526:     */       }
/*  527:     */     }
/*  528:     */   }
/*  529:     */   
/*  530:     */   private int getSizeOfInitialSheetRecords(int bofRecordIndex)
/*  531:     */   {
/*  532: 615 */     int result = 0;
/*  533: 617 */     for (int j = bofRecordIndex + 1; j < this._records.size(); j++)
/*  534:     */     {
/*  535: 618 */       RecordBase tmpRec = (RecordBase)this._records.get(j);
/*  536: 619 */       if ((tmpRec instanceof RowRecordsAggregate)) {
/*  537:     */         break;
/*  538:     */       }
/*  539: 622 */       result += tmpRec.getRecordSize();
/*  540:     */     }
/*  541: 624 */     if (this._isUncalced) {
/*  542: 625 */       result += UncalcedRecord.getStaticRecordSize();
/*  543:     */     }
/*  544: 627 */     return result;
/*  545:     */   }
/*  546:     */   
/*  547:     */   public void addValueRecord(int row, CellValueRecordInterface col)
/*  548:     */   {
/*  549: 644 */     if (log.check(1)) {
/*  550: 645 */       log.log(1, new Object[] { "add value record  row" + row });
/*  551:     */     }
/*  552: 647 */     DimensionsRecord d = this._dimensions;
/*  553: 649 */     if (col.getColumn() >= d.getLastCol()) {
/*  554: 650 */       d.setLastCol((short)(col.getColumn() + 1));
/*  555:     */     }
/*  556: 652 */     if (col.getColumn() < d.getFirstCol()) {
/*  557: 653 */       d.setFirstCol(col.getColumn());
/*  558:     */     }
/*  559: 655 */     this._rowsAggregate.insertCell(col);
/*  560:     */   }
/*  561:     */   
/*  562:     */   public void removeValueRecord(int row, CellValueRecordInterface col)
/*  563:     */   {
/*  564: 669 */     log.log(1, new Object[] { "remove value record row " + row });
/*  565: 670 */     this._rowsAggregate.removeCell(col);
/*  566:     */   }
/*  567:     */   
/*  568:     */   public void replaceValueRecord(CellValueRecordInterface newval)
/*  569:     */   {
/*  570: 685 */     if (log.check(1)) {
/*  571: 686 */       log.log(1, new Object[] { "replaceValueRecord " });
/*  572:     */     }
/*  573: 692 */     this._rowsAggregate.removeCell(newval);
/*  574: 693 */     this._rowsAggregate.insertCell(newval);
/*  575:     */   }
/*  576:     */   
/*  577:     */   public void addRow(RowRecord row)
/*  578:     */   {
/*  579: 709 */     if (log.check(1)) {
/*  580: 710 */       log.log(1, new Object[] { "addRow " });
/*  581:     */     }
/*  582: 711 */     DimensionsRecord d = this._dimensions;
/*  583: 713 */     if (row.getRowNumber() >= d.getLastRow()) {
/*  584: 714 */       d.setLastRow(row.getRowNumber() + 1);
/*  585:     */     }
/*  586: 716 */     if (row.getRowNumber() < d.getFirstRow()) {
/*  587: 717 */       d.setFirstRow(row.getRowNumber());
/*  588:     */     }
/*  589: 721 */     RowRecord existingRow = this._rowsAggregate.getRow(row.getRowNumber());
/*  590: 722 */     if (existingRow != null) {
/*  591: 723 */       this._rowsAggregate.removeRow(existingRow);
/*  592:     */     }
/*  593: 726 */     this._rowsAggregate.insertRow(row);
/*  594: 728 */     if (log.check(1)) {
/*  595: 729 */       log.log(1, new Object[] { "exit addRow" });
/*  596:     */     }
/*  597:     */   }
/*  598:     */   
/*  599:     */   public void removeRow(RowRecord row)
/*  600:     */   {
/*  601: 740 */     this._rowsAggregate.removeRow(row);
/*  602:     */   }
/*  603:     */   
/*  604:     */   public Iterator<CellValueRecordInterface> getCellValueIterator()
/*  605:     */   {
/*  606: 757 */     return this._rowsAggregate.getCellValueIterator();
/*  607:     */   }
/*  608:     */   
/*  609:     */   public RowRecord getNextRow()
/*  610:     */   {
/*  611: 773 */     if (this.rowRecIterator == null) {
/*  612: 775 */       this.rowRecIterator = this._rowsAggregate.getIterator();
/*  613:     */     }
/*  614: 777 */     if (!this.rowRecIterator.hasNext()) {
/*  615: 779 */       return null;
/*  616:     */     }
/*  617: 781 */     return (RowRecord)this.rowRecIterator.next();
/*  618:     */   }
/*  619:     */   
/*  620:     */   public RowRecord getRow(int rownum)
/*  621:     */   {
/*  622: 800 */     return this._rowsAggregate.getRow(rownum);
/*  623:     */   }
/*  624:     */   
/*  625:     */   static BOFRecord createBOF()
/*  626:     */   {
/*  627: 807 */     BOFRecord retval = new BOFRecord();
/*  628:     */     
/*  629: 809 */     retval.setVersion(1536);
/*  630: 810 */     retval.setType(16);
/*  631:     */     
/*  632: 812 */     retval.setBuild(3515);
/*  633: 813 */     retval.setBuildYear(1996);
/*  634: 814 */     retval.setHistoryBitMask(193);
/*  635: 815 */     retval.setRequiredVersion(6);
/*  636: 816 */     return retval;
/*  637:     */   }
/*  638:     */   
/*  639:     */   private static CalcModeRecord createCalcMode()
/*  640:     */   {
/*  641: 823 */     CalcModeRecord retval = new CalcModeRecord();
/*  642:     */     
/*  643: 825 */     retval.setCalcMode((short)1);
/*  644: 826 */     return retval;
/*  645:     */   }
/*  646:     */   
/*  647:     */   private static CalcCountRecord createCalcCount()
/*  648:     */   {
/*  649: 833 */     CalcCountRecord retval = new CalcCountRecord();
/*  650:     */     
/*  651: 835 */     retval.setIterations((short)100);
/*  652: 836 */     return retval;
/*  653:     */   }
/*  654:     */   
/*  655:     */   private static RefModeRecord createRefMode()
/*  656:     */   {
/*  657: 843 */     RefModeRecord retval = new RefModeRecord();
/*  658:     */     
/*  659: 845 */     retval.setMode((short)1);
/*  660: 846 */     return retval;
/*  661:     */   }
/*  662:     */   
/*  663:     */   private static IterationRecord createIteration()
/*  664:     */   {
/*  665: 853 */     return new IterationRecord(false);
/*  666:     */   }
/*  667:     */   
/*  668:     */   private static DeltaRecord createDelta()
/*  669:     */   {
/*  670: 860 */     return new DeltaRecord(0.001D);
/*  671:     */   }
/*  672:     */   
/*  673:     */   private static SaveRecalcRecord createSaveRecalc()
/*  674:     */   {
/*  675: 867 */     SaveRecalcRecord retval = new SaveRecalcRecord();
/*  676:     */     
/*  677: 869 */     retval.setRecalc(true);
/*  678: 870 */     return retval;
/*  679:     */   }
/*  680:     */   
/*  681:     */   private static PrintHeadersRecord createPrintHeaders()
/*  682:     */   {
/*  683: 877 */     PrintHeadersRecord retval = new PrintHeadersRecord();
/*  684:     */     
/*  685: 879 */     retval.setPrintHeaders(false);
/*  686: 880 */     return retval;
/*  687:     */   }
/*  688:     */   
/*  689:     */   private static PrintGridlinesRecord createPrintGridlines()
/*  690:     */   {
/*  691: 888 */     PrintGridlinesRecord retval = new PrintGridlinesRecord();
/*  692:     */     
/*  693: 890 */     retval.setPrintGridlines(false);
/*  694: 891 */     return retval;
/*  695:     */   }
/*  696:     */   
/*  697:     */   private static GridsetRecord createGridset()
/*  698:     */   {
/*  699: 898 */     GridsetRecord retval = new GridsetRecord();
/*  700:     */     
/*  701: 900 */     retval.setGridset(true);
/*  702: 901 */     return retval;
/*  703:     */   }
/*  704:     */   
/*  705:     */   private static GutsRecord createGuts()
/*  706:     */   {
/*  707: 908 */     GutsRecord retval = new GutsRecord();
/*  708:     */     
/*  709: 910 */     retval.setLeftRowGutter((short)0);
/*  710: 911 */     retval.setTopColGutter((short)0);
/*  711: 912 */     retval.setRowLevelMax((short)0);
/*  712: 913 */     retval.setColLevelMax((short)0);
/*  713: 914 */     return retval;
/*  714:     */   }
/*  715:     */   
/*  716:     */   private GutsRecord getGutsRecord()
/*  717:     */   {
/*  718: 918 */     if (this._gutsRecord == null)
/*  719:     */     {
/*  720: 919 */       GutsRecord result = createGuts();
/*  721: 920 */       RecordOrderer.addNewSheetRecord(this._records, result);
/*  722: 921 */       this._gutsRecord = result;
/*  723:     */     }
/*  724: 924 */     return this._gutsRecord;
/*  725:     */   }
/*  726:     */   
/*  727:     */   private static DefaultRowHeightRecord createDefaultRowHeight()
/*  728:     */   {
/*  729: 931 */     DefaultRowHeightRecord retval = new DefaultRowHeightRecord();
/*  730:     */     
/*  731: 933 */     retval.setOptionFlags((short)0);
/*  732: 934 */     retval.setRowHeight((short)255);
/*  733: 935 */     return retval;
/*  734:     */   }
/*  735:     */   
/*  736:     */   private static WSBoolRecord createWSBool()
/*  737:     */   {
/*  738: 942 */     WSBoolRecord retval = new WSBoolRecord();
/*  739:     */     
/*  740: 944 */     retval.setWSBool1((byte)4);
/*  741: 945 */     retval.setWSBool2((byte)-63);
/*  742: 946 */     return retval;
/*  743:     */   }
/*  744:     */   
/*  745:     */   private static DefaultColWidthRecord createDefaultColWidth()
/*  746:     */   {
/*  747: 954 */     DefaultColWidthRecord retval = new DefaultColWidthRecord();
/*  748: 955 */     retval.setColWidth(8);
/*  749: 956 */     return retval;
/*  750:     */   }
/*  751:     */   
/*  752:     */   public int getDefaultColumnWidth()
/*  753:     */   {
/*  754: 964 */     return this.defaultcolwidth.getColWidth();
/*  755:     */   }
/*  756:     */   
/*  757:     */   public boolean isGridsPrinted()
/*  758:     */   {
/*  759: 971 */     if (this.gridset == null)
/*  760:     */     {
/*  761: 972 */       this.gridset = createGridset();
/*  762:     */       
/*  763: 974 */       int loc = findFirstRecordLocBySid((short)10);
/*  764: 975 */       this._records.add(loc, this.gridset);
/*  765:     */     }
/*  766: 977 */     return !this.gridset.getGridset();
/*  767:     */   }
/*  768:     */   
/*  769:     */   public void setGridsPrinted(boolean value)
/*  770:     */   {
/*  771: 985 */     this.gridset.setGridset(!value);
/*  772:     */   }
/*  773:     */   
/*  774:     */   public void setDefaultColumnWidth(int dcw)
/*  775:     */   {
/*  776: 993 */     this.defaultcolwidth.setColWidth(dcw);
/*  777:     */   }
/*  778:     */   
/*  779:     */   public void setDefaultRowHeight(short dch)
/*  780:     */   {
/*  781:1002 */     this.defaultrowheight.setRowHeight(dch);
/*  782:     */     
/*  783:1004 */     this.defaultrowheight.setOptionFlags((short)1);
/*  784:     */   }
/*  785:     */   
/*  786:     */   public short getDefaultRowHeight()
/*  787:     */   {
/*  788:1012 */     return this.defaultrowheight.getRowHeight();
/*  789:     */   }
/*  790:     */   
/*  791:     */   public int getColumnWidth(int columnIndex)
/*  792:     */   {
/*  793:1025 */     ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
/*  794:1026 */     if (ci != null) {
/*  795:1027 */       return ci.getColumnWidth();
/*  796:     */     }
/*  797:1031 */     return 256 * this.defaultcolwidth.getColWidth();
/*  798:     */   }
/*  799:     */   
/*  800:     */   public short getXFIndexForColAt(short columnIndex)
/*  801:     */   {
/*  802:1049 */     ColumnInfoRecord ci = this._columnInfos.findColumnInfo(columnIndex);
/*  803:1050 */     if (ci != null) {
/*  804:1051 */       return (short)ci.getXFIndex();
/*  805:     */     }
/*  806:1053 */     return 15;
/*  807:     */   }
/*  808:     */   
/*  809:     */   public void setColumnWidth(int column, int width)
/*  810:     */   {
/*  811:1065 */     if (width > 65280) {
/*  812:1065 */       throw new IllegalArgumentException("The maximum column width for an individual cell is 255 characters.");
/*  813:     */     }
/*  814:1067 */     setColumn(column, null, Integer.valueOf(width), null, null, null);
/*  815:     */   }
/*  816:     */   
/*  817:     */   public boolean isColumnHidden(int columnIndex)
/*  818:     */   {
/*  819:1079 */     ColumnInfoRecord cir = this._columnInfos.findColumnInfo(columnIndex);
/*  820:1080 */     if (cir == null) {
/*  821:1081 */       return false;
/*  822:     */     }
/*  823:1083 */     return cir.getHidden();
/*  824:     */   }
/*  825:     */   
/*  826:     */   public void setColumnHidden(int column, boolean hidden)
/*  827:     */   {
/*  828:1092 */     setColumn(column, null, null, null, Boolean.valueOf(hidden), null);
/*  829:     */   }
/*  830:     */   
/*  831:     */   public void setDefaultColumnStyle(int column, int styleIndex)
/*  832:     */   {
/*  833:1095 */     setColumn(column, Short.valueOf((short)styleIndex), null, null, null, null);
/*  834:     */   }
/*  835:     */   
/*  836:     */   private void setColumn(int column, Short xfStyle, Integer width, Integer level, Boolean hidden, Boolean collapsed)
/*  837:     */   {
/*  838:1099 */     this._columnInfos.setColumn(column, xfStyle, width, level, hidden, collapsed);
/*  839:     */   }
/*  840:     */   
/*  841:     */   public void groupColumnRange(int fromColumn, int toColumn, boolean indent)
/*  842:     */   {
/*  843:1113 */     this._columnInfos.groupColumnRange(fromColumn, toColumn, indent);
/*  844:     */     
/*  845:     */ 
/*  846:1116 */     int maxLevel = this._columnInfos.getMaxOutlineLevel();
/*  847:     */     
/*  848:1118 */     GutsRecord guts = getGutsRecord();
/*  849:1119 */     guts.setColLevelMax((short)(maxLevel + 1));
/*  850:1120 */     if (maxLevel == 0) {
/*  851:1121 */       guts.setTopColGutter((short)0);
/*  852:     */     } else {
/*  853:1123 */       guts.setTopColGutter((short)(29 + 12 * (maxLevel - 1)));
/*  854:     */     }
/*  855:     */   }
/*  856:     */   
/*  857:     */   private static DimensionsRecord createDimensions()
/*  858:     */   {
/*  859:1132 */     DimensionsRecord retval = new DimensionsRecord();
/*  860:     */     
/*  861:1134 */     retval.setFirstCol((short)0);
/*  862:1135 */     retval.setLastRow(1);
/*  863:1136 */     retval.setFirstRow(0);
/*  864:1137 */     retval.setLastCol((short)1);
/*  865:1138 */     return retval;
/*  866:     */   }
/*  867:     */   
/*  868:     */   private static WindowTwoRecord createWindowTwo()
/*  869:     */   {
/*  870:1151 */     WindowTwoRecord retval = new WindowTwoRecord();
/*  871:     */     
/*  872:1153 */     retval.setOptions((short)1718);
/*  873:1154 */     retval.setTopRow((short)0);
/*  874:1155 */     retval.setLeftCol((short)0);
/*  875:1156 */     retval.setHeaderColor(64);
/*  876:1157 */     retval.setPageBreakZoom((short)0);
/*  877:1158 */     retval.setNormalZoom((short)0);
/*  878:1159 */     return retval;
/*  879:     */   }
/*  880:     */   
/*  881:     */   private static SelectionRecord createSelection()
/*  882:     */   {
/*  883:1166 */     return new SelectionRecord(0, 0);
/*  884:     */   }
/*  885:     */   
/*  886:     */   public short getTopRow()
/*  887:     */   {
/*  888:1170 */     return this.windowTwo == null ? 0 : this.windowTwo.getTopRow();
/*  889:     */   }
/*  890:     */   
/*  891:     */   public void setTopRow(short topRow)
/*  892:     */   {
/*  893:1174 */     if (this.windowTwo != null) {
/*  894:1175 */       this.windowTwo.setTopRow(topRow);
/*  895:     */     }
/*  896:     */   }
/*  897:     */   
/*  898:     */   public void setLeftCol(short leftCol)
/*  899:     */   {
/*  900:1184 */     if (this.windowTwo != null) {
/*  901:1185 */       this.windowTwo.setLeftCol(leftCol);
/*  902:     */     }
/*  903:     */   }
/*  904:     */   
/*  905:     */   public short getLeftCol()
/*  906:     */   {
/*  907:1190 */     return this.windowTwo == null ? 0 : this.windowTwo.getLeftCol();
/*  908:     */   }
/*  909:     */   
/*  910:     */   public int getActiveCellRow()
/*  911:     */   {
/*  912:1200 */     if (this._selection == null) {
/*  913:1201 */       return 0;
/*  914:     */     }
/*  915:1203 */     return this._selection.getActiveCellRow();
/*  916:     */   }
/*  917:     */   
/*  918:     */   public void setActiveCellRow(int row)
/*  919:     */   {
/*  920:1214 */     if (this._selection != null) {
/*  921:1215 */       this._selection.setActiveCellRow(row);
/*  922:     */     }
/*  923:     */   }
/*  924:     */   
/*  925:     */   public short getActiveCellCol()
/*  926:     */   {
/*  927:1224 */     if (this._selection == null) {
/*  928:1225 */       return 0;
/*  929:     */     }
/*  930:1227 */     return (short)this._selection.getActiveCellCol();
/*  931:     */   }
/*  932:     */   
/*  933:     */   public void setActiveCellCol(short col)
/*  934:     */   {
/*  935:1238 */     if (this._selection != null) {
/*  936:1240 */       this._selection.setActiveCellCol(col);
/*  937:     */     }
/*  938:     */   }
/*  939:     */   
/*  940:     */   public List<RecordBase> getRecords()
/*  941:     */   {
/*  942:1245 */     return this._records;
/*  943:     */   }
/*  944:     */   
/*  945:     */   public GridsetRecord getGridsetRecord()
/*  946:     */   {
/*  947:1255 */     return this.gridset;
/*  948:     */   }
/*  949:     */   
/*  950:     */   public Record findFirstRecordBySid(short sid)
/*  951:     */   {
/*  952:1266 */     int ix = findFirstRecordLocBySid(sid);
/*  953:1267 */     if (ix < 0) {
/*  954:1268 */       return null;
/*  955:     */     }
/*  956:1270 */     return (Record)this._records.get(ix);
/*  957:     */   }
/*  958:     */   
/*  959:     */   public void setSCLRecord(SCLRecord sclRecord)
/*  960:     */   {
/*  961:1280 */     int oldRecordLoc = findFirstRecordLocBySid((short)160);
/*  962:1281 */     if (oldRecordLoc == -1)
/*  963:     */     {
/*  964:1283 */       int windowRecordLoc = findFirstRecordLocBySid((short)574);
/*  965:1284 */       this._records.add(windowRecordLoc + 1, sclRecord);
/*  966:     */     }
/*  967:     */     else
/*  968:     */     {
/*  969:1286 */       this._records.set(oldRecordLoc, sclRecord);
/*  970:     */     }
/*  971:     */   }
/*  972:     */   
/*  973:     */   public int findFirstRecordLocBySid(short sid)
/*  974:     */   {
/*  975:1298 */     int max = this._records.size();
/*  976:1299 */     for (int i = 0; i < max; i++)
/*  977:     */     {
/*  978:1300 */       Object rb = this._records.get(i);
/*  979:1301 */       if ((rb instanceof Record))
/*  980:     */       {
/*  981:1304 */         Record record = (Record)rb;
/*  982:1305 */         if (record.getSid() == sid) {
/*  983:1306 */           return i;
/*  984:     */         }
/*  985:     */       }
/*  986:     */     }
/*  987:1309 */     return -1;
/*  988:     */   }
/*  989:     */   
/*  990:     */   public WindowTwoRecord getWindowTwo()
/*  991:     */   {
/*  992:1313 */     return this.windowTwo;
/*  993:     */   }
/*  994:     */   
/*  995:     */   public PrintGridlinesRecord getPrintGridlines()
/*  996:     */   {
/*  997:1322 */     return this.printGridlines;
/*  998:     */   }
/*  999:     */   
/* 1000:     */   public void setPrintGridlines(PrintGridlinesRecord newPrintGridlines)
/* 1001:     */   {
/* 1002:1331 */     this.printGridlines = newPrintGridlines;
/* 1003:     */   }
/* 1004:     */   
/* 1005:     */   public PrintHeadersRecord getPrintHeaders()
/* 1006:     */   {
/* 1007:1340 */     return this.printHeaders;
/* 1008:     */   }
/* 1009:     */   
/* 1010:     */   public void setPrintHeaders(PrintHeadersRecord newPrintHeaders)
/* 1011:     */   {
/* 1012:1349 */     this.printHeaders = newPrintHeaders;
/* 1013:     */   }
/* 1014:     */   
/* 1015:     */   public void setSelected(boolean sel)
/* 1016:     */   {
/* 1017:1357 */     this.windowTwo.setSelected(sel);
/* 1018:     */   }
/* 1019:     */   
/* 1020:     */   public void createFreezePane(int colSplit, int rowSplit, int topRow, int leftmostColumn)
/* 1021:     */   {
/* 1022:1371 */     int paneLoc = findFirstRecordLocBySid((short)65);
/* 1023:1372 */     if (paneLoc != -1) {
/* 1024:1373 */       this._records.remove(paneLoc);
/* 1025:     */     }
/* 1026:1376 */     if ((colSplit == 0) && (rowSplit == 0))
/* 1027:     */     {
/* 1028:1377 */       this.windowTwo.setFreezePanes(false);
/* 1029:1378 */       this.windowTwo.setFreezePanesNoSplit(false);
/* 1030:1379 */       SelectionRecord sel = (SelectionRecord)findFirstRecordBySid((short)29);
/* 1031:1380 */       sel.setPane((byte)3);
/* 1032:1381 */       return;
/* 1033:     */     }
/* 1034:1384 */     int loc = findFirstRecordLocBySid((short)574);
/* 1035:1385 */     PaneRecord pane = new PaneRecord();
/* 1036:1386 */     pane.setX((short)colSplit);
/* 1037:1387 */     pane.setY((short)rowSplit);
/* 1038:1388 */     pane.setTopRow((short)topRow);
/* 1039:1389 */     pane.setLeftColumn((short)leftmostColumn);
/* 1040:1390 */     if (rowSplit == 0)
/* 1041:     */     {
/* 1042:1391 */       pane.setTopRow((short)0);
/* 1043:1392 */       pane.setActivePane((short)1);
/* 1044:     */     }
/* 1045:1393 */     else if (colSplit == 0)
/* 1046:     */     {
/* 1047:1394 */       pane.setLeftColumn((short)0);
/* 1048:1395 */       pane.setActivePane((short)2);
/* 1049:     */     }
/* 1050:     */     else
/* 1051:     */     {
/* 1052:1397 */       pane.setActivePane((short)0);
/* 1053:     */     }
/* 1054:1399 */     this._records.add(loc + 1, pane);
/* 1055:     */     
/* 1056:1401 */     this.windowTwo.setFreezePanes(true);
/* 1057:1402 */     this.windowTwo.setFreezePanesNoSplit(true);
/* 1058:     */     
/* 1059:1404 */     SelectionRecord sel = (SelectionRecord)findFirstRecordBySid((short)29);
/* 1060:1405 */     sel.setPane((byte)pane.getActivePane());
/* 1061:     */   }
/* 1062:     */   
/* 1063:     */   public void createSplitPane(int xSplitPos, int ySplitPos, int topRow, int leftmostColumn, int activePane)
/* 1064:     */   {
/* 1065:1423 */     int paneLoc = findFirstRecordLocBySid((short)65);
/* 1066:1424 */     if (paneLoc != -1) {
/* 1067:1425 */       this._records.remove(paneLoc);
/* 1068:     */     }
/* 1069:1427 */     int loc = findFirstRecordLocBySid((short)574);
/* 1070:1428 */     PaneRecord r = new PaneRecord();
/* 1071:1429 */     r.setX((short)xSplitPos);
/* 1072:1430 */     r.setY((short)ySplitPos);
/* 1073:1431 */     r.setTopRow((short)topRow);
/* 1074:1432 */     r.setLeftColumn((short)leftmostColumn);
/* 1075:1433 */     r.setActivePane((short)activePane);
/* 1076:1434 */     this._records.add(loc + 1, r);
/* 1077:     */     
/* 1078:1436 */     this.windowTwo.setFreezePanes(false);
/* 1079:1437 */     this.windowTwo.setFreezePanesNoSplit(false);
/* 1080:     */     
/* 1081:1439 */     SelectionRecord sel = (SelectionRecord)findFirstRecordBySid((short)29);
/* 1082:1440 */     sel.setPane((byte)0);
/* 1083:     */   }
/* 1084:     */   
/* 1085:     */   public PaneInformation getPaneInformation()
/* 1086:     */   {
/* 1087:1449 */     PaneRecord rec = (PaneRecord)findFirstRecordBySid((short)65);
/* 1088:1450 */     if (rec == null) {
/* 1089:1451 */       return null;
/* 1090:     */     }
/* 1091:1453 */     return new PaneInformation(rec.getX(), rec.getY(), rec.getTopRow(), rec.getLeftColumn(), (byte)rec.getActivePane(), this.windowTwo.getFreezePanes());
/* 1092:     */   }
/* 1093:     */   
/* 1094:     */   public SelectionRecord getSelection()
/* 1095:     */   {
/* 1096:1458 */     return this._selection;
/* 1097:     */   }
/* 1098:     */   
/* 1099:     */   public void setSelection(SelectionRecord selection)
/* 1100:     */   {
/* 1101:1462 */     this._selection = selection;
/* 1102:     */   }
/* 1103:     */   
/* 1104:     */   public WorksheetProtectionBlock getProtectionBlock()
/* 1105:     */   {
/* 1106:1469 */     return this._protectionBlock;
/* 1107:     */   }
/* 1108:     */   
/* 1109:     */   public void setDisplayGridlines(boolean show)
/* 1110:     */   {
/* 1111:1476 */     this.windowTwo.setDisplayGridlines(show);
/* 1112:     */   }
/* 1113:     */   
/* 1114:     */   public boolean isDisplayGridlines()
/* 1115:     */   {
/* 1116:1483 */     return this.windowTwo.getDisplayGridlines();
/* 1117:     */   }
/* 1118:     */   
/* 1119:     */   public void setDisplayFormulas(boolean show)
/* 1120:     */   {
/* 1121:1491 */     this.windowTwo.setDisplayFormulas(show);
/* 1122:     */   }
/* 1123:     */   
/* 1124:     */   public boolean isDisplayFormulas()
/* 1125:     */   {
/* 1126:1499 */     return this.windowTwo.getDisplayFormulas();
/* 1127:     */   }
/* 1128:     */   
/* 1129:     */   public void setDisplayRowColHeadings(boolean show)
/* 1130:     */   {
/* 1131:1507 */     this.windowTwo.setDisplayRowColHeadings(show);
/* 1132:     */   }
/* 1133:     */   
/* 1134:     */   public boolean isDisplayRowColHeadings()
/* 1135:     */   {
/* 1136:1515 */     return this.windowTwo.getDisplayRowColHeadings();
/* 1137:     */   }
/* 1138:     */   
/* 1139:     */   public void setPrintRowColHeadings(boolean show)
/* 1140:     */   {
/* 1141:1523 */     this.windowTwo.setDisplayRowColHeadings(show);
/* 1142:     */   }
/* 1143:     */   
/* 1144:     */   public boolean isPrintRowColHeadings()
/* 1145:     */   {
/* 1146:1531 */     return this.windowTwo.getDisplayRowColHeadings();
/* 1147:     */   }
/* 1148:     */   
/* 1149:     */   public boolean getUncalced()
/* 1150:     */   {
/* 1151:1539 */     return this._isUncalced;
/* 1152:     */   }
/* 1153:     */   
/* 1154:     */   public void setUncalced(boolean uncalced)
/* 1155:     */   {
/* 1156:1545 */     this._isUncalced = uncalced;
/* 1157:     */   }
/* 1158:     */   
/* 1159:     */   public int aggregateDrawingRecords(DrawingManager2 drawingManager, boolean createIfMissing)
/* 1160:     */   {
/* 1161:1559 */     int loc = findFirstRecordLocBySid((short)236);
/* 1162:1560 */     boolean noDrawingRecordsFound = loc == -1;
/* 1163:1561 */     if (noDrawingRecordsFound)
/* 1164:     */     {
/* 1165:1562 */       if (!createIfMissing) {
/* 1166:1564 */         return -1;
/* 1167:     */       }
/* 1168:1567 */       EscherAggregate aggregate = new EscherAggregate(true);
/* 1169:1568 */       loc = findFirstRecordLocBySid((short)9876);
/* 1170:1569 */       if (loc == -1) {
/* 1171:1570 */         loc = findFirstRecordLocBySid((short)574);
/* 1172:     */       } else {
/* 1173:1572 */         getRecords().remove(loc);
/* 1174:     */       }
/* 1175:1574 */       getRecords().add(loc, aggregate);
/* 1176:1575 */       return loc;
/* 1177:     */     }
/* 1178:1577 */     List<RecordBase> records = getRecords();
/* 1179:     */     
/* 1180:1579 */     EscherAggregate.createAggregate(records, loc);
/* 1181:     */     
/* 1182:1581 */     return loc;
/* 1183:     */   }
/* 1184:     */   
/* 1185:     */   public void preSerialize()
/* 1186:     */   {
/* 1187:1590 */     for (RecordBase r : getRecords()) {
/* 1188:1591 */       if ((r instanceof EscherAggregate)) {
/* 1189:1593 */         r.getRecordSize();
/* 1190:     */       }
/* 1191:     */     }
/* 1192:     */   }
/* 1193:     */   
/* 1194:     */   public PageSettingsBlock getPageSettings()
/* 1195:     */   {
/* 1196:1600 */     if (this._psBlock == null)
/* 1197:     */     {
/* 1198:1601 */       this._psBlock = new PageSettingsBlock();
/* 1199:1602 */       RecordOrderer.addNewSheetRecord(this._records, this._psBlock);
/* 1200:     */     }
/* 1201:1604 */     return this._psBlock;
/* 1202:     */   }
/* 1203:     */   
/* 1204:     */   public void setColumnGroupCollapsed(int columnNumber, boolean collapsed)
/* 1205:     */   {
/* 1206:1609 */     if (collapsed) {
/* 1207:1610 */       this._columnInfos.collapseColumn(columnNumber);
/* 1208:     */     } else {
/* 1209:1612 */       this._columnInfos.expandColumn(columnNumber);
/* 1210:     */     }
/* 1211:     */   }
/* 1212:     */   
/* 1213:     */   public void groupRowRange(int fromRow, int toRow, boolean indent)
/* 1214:     */   {
/* 1215:1619 */     for (int rowNum = fromRow; rowNum <= toRow; rowNum++)
/* 1216:     */     {
/* 1217:1621 */       RowRecord row = getRow(rowNum);
/* 1218:1622 */       if (row == null)
/* 1219:     */       {
/* 1220:1624 */         row = RowRecordsAggregate.createRow(rowNum);
/* 1221:1625 */         addRow(row);
/* 1222:     */       }
/* 1223:1627 */       int level = row.getOutlineLevel();
/* 1224:1628 */       if (indent) {
/* 1225:1628 */         level++;
/* 1226:     */       } else {
/* 1227:1628 */         level--;
/* 1228:     */       }
/* 1229:1629 */       level = Math.max(0, level);
/* 1230:1630 */       level = Math.min(7, level);
/* 1231:1631 */       row.setOutlineLevel((short)level);
/* 1232:     */     }
/* 1233:1634 */     recalcRowGutter();
/* 1234:     */   }
/* 1235:     */   
/* 1236:     */   private void recalcRowGutter()
/* 1237:     */   {
/* 1238:1638 */     int maxLevel = 0;
/* 1239:1639 */     Iterator<RowRecord> iterator = this._rowsAggregate.getIterator();
/* 1240:1640 */     while (iterator.hasNext())
/* 1241:     */     {
/* 1242:1641 */       RowRecord rowRecord = (RowRecord)iterator.next();
/* 1243:1642 */       maxLevel = Math.max(rowRecord.getOutlineLevel(), maxLevel);
/* 1244:     */     }
/* 1245:1646 */     GutsRecord guts = getGutsRecord();
/* 1246:     */     
/* 1247:1648 */     guts.setRowLevelMax((short)(maxLevel + 1));
/* 1248:1649 */     guts.setLeftRowGutter((short)(29 + 12 * maxLevel));
/* 1249:     */   }
/* 1250:     */   
/* 1251:     */   public DataValidityTable getOrCreateDataValidityTable()
/* 1252:     */   {
/* 1253:1653 */     if (this._dataValidityTable == null)
/* 1254:     */     {
/* 1255:1654 */       DataValidityTable result = new DataValidityTable();
/* 1256:1655 */       RecordOrderer.addNewSheetRecord(this._records, result);
/* 1257:1656 */       this._dataValidityTable = result;
/* 1258:     */     }
/* 1259:1658 */     return this._dataValidityTable;
/* 1260:     */   }
/* 1261:     */   
/* 1262:     */   public NoteRecord[] getNoteRecords()
/* 1263:     */   {
/* 1264:1665 */     List<NoteRecord> temp = new ArrayList();
/* 1265:1666 */     for (int i = this._records.size() - 1; i >= 0; i--)
/* 1266:     */     {
/* 1267:1667 */       RecordBase rec = (RecordBase)this._records.get(i);
/* 1268:1668 */       if ((rec instanceof NoteRecord)) {
/* 1269:1669 */         temp.add((NoteRecord)rec);
/* 1270:     */       }
/* 1271:     */     }
/* 1272:1672 */     if (temp.size() < 1) {
/* 1273:1673 */       return NoteRecord.EMPTY_ARRAY;
/* 1274:     */     }
/* 1275:1675 */     NoteRecord[] result = new NoteRecord[temp.size()];
/* 1276:1676 */     temp.toArray(result);
/* 1277:1677 */     return result;
/* 1278:     */   }
/* 1279:     */   
/* 1280:     */   public int getColumnOutlineLevel(int columnIndex)
/* 1281:     */   {
/* 1282:1681 */     return this._columnInfos.getOutlineLevel(columnIndex);
/* 1283:     */   }
/* 1284:     */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.InternalSheet
 * JD-Core Version:    0.7.0.1
 */