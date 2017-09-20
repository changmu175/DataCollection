/*   1:    */ package org.apache.poi.hssf.dev;
/*   2:    */ 
/*   3:    */ import java.io.DataInputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.FileOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.InputStream;
/*   9:    */ import java.io.OutputStream;
/*  10:    */ import java.io.OutputStreamWriter;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.io.PrintWriter;
/*  13:    */ import java.io.Writer;
/*  14:    */ import java.nio.charset.Charset;
/*  15:    */ import java.util.ArrayList;
/*  16:    */ import java.util.List;
/*  17:    */ import org.apache.poi.hssf.record.ArrayRecord;
/*  18:    */ import org.apache.poi.hssf.record.AutoFilterInfoRecord;
/*  19:    */ import org.apache.poi.hssf.record.BOFRecord;
/*  20:    */ import org.apache.poi.hssf.record.BackupRecord;
/*  21:    */ import org.apache.poi.hssf.record.BlankRecord;
/*  22:    */ import org.apache.poi.hssf.record.BookBoolRecord;
/*  23:    */ import org.apache.poi.hssf.record.BoolErrRecord;
/*  24:    */ import org.apache.poi.hssf.record.BottomMarginRecord;
/*  25:    */ import org.apache.poi.hssf.record.BoundSheetRecord;
/*  26:    */ import org.apache.poi.hssf.record.CFHeader12Record;
/*  27:    */ import org.apache.poi.hssf.record.CFHeaderRecord;
/*  28:    */ import org.apache.poi.hssf.record.CFRule12Record;
/*  29:    */ import org.apache.poi.hssf.record.CFRuleRecord;
/*  30:    */ import org.apache.poi.hssf.record.CalcCountRecord;
/*  31:    */ import org.apache.poi.hssf.record.CalcModeRecord;
/*  32:    */ import org.apache.poi.hssf.record.CodepageRecord;
/*  33:    */ import org.apache.poi.hssf.record.ColumnInfoRecord;
/*  34:    */ import org.apache.poi.hssf.record.ContinueRecord;
/*  35:    */ import org.apache.poi.hssf.record.CountryRecord;
/*  36:    */ import org.apache.poi.hssf.record.DBCellRecord;
/*  37:    */ import org.apache.poi.hssf.record.DConRefRecord;
/*  38:    */ import org.apache.poi.hssf.record.DSFRecord;
/*  39:    */ import org.apache.poi.hssf.record.DVALRecord;
/*  40:    */ import org.apache.poi.hssf.record.DVRecord;
/*  41:    */ import org.apache.poi.hssf.record.DateWindow1904Record;
/*  42:    */ import org.apache.poi.hssf.record.DefaultColWidthRecord;
/*  43:    */ import org.apache.poi.hssf.record.DefaultRowHeightRecord;
/*  44:    */ import org.apache.poi.hssf.record.DeltaRecord;
/*  45:    */ import org.apache.poi.hssf.record.DimensionsRecord;
/*  46:    */ import org.apache.poi.hssf.record.DrawingGroupRecord;
/*  47:    */ import org.apache.poi.hssf.record.DrawingRecordForBiffViewer;
/*  48:    */ import org.apache.poi.hssf.record.DrawingSelectionRecord;
/*  49:    */ import org.apache.poi.hssf.record.EOFRecord;
/*  50:    */ import org.apache.poi.hssf.record.ExtSSTRecord;
/*  51:    */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*  52:    */ import org.apache.poi.hssf.record.ExternSheetRecord;
/*  53:    */ import org.apache.poi.hssf.record.ExternalNameRecord;
/*  54:    */ import org.apache.poi.hssf.record.FeatHdrRecord;
/*  55:    */ import org.apache.poi.hssf.record.FeatRecord;
/*  56:    */ import org.apache.poi.hssf.record.FilePassRecord;
/*  57:    */ import org.apache.poi.hssf.record.FileSharingRecord;
/*  58:    */ import org.apache.poi.hssf.record.FnGroupCountRecord;
/*  59:    */ import org.apache.poi.hssf.record.FontRecord;
/*  60:    */ import org.apache.poi.hssf.record.FooterRecord;
/*  61:    */ import org.apache.poi.hssf.record.FormatRecord;
/*  62:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  63:    */ import org.apache.poi.hssf.record.GridsetRecord;
/*  64:    */ import org.apache.poi.hssf.record.GutsRecord;
/*  65:    */ import org.apache.poi.hssf.record.HCenterRecord;
/*  66:    */ import org.apache.poi.hssf.record.HeaderRecord;
/*  67:    */ import org.apache.poi.hssf.record.HideObjRecord;
/*  68:    */ import org.apache.poi.hssf.record.HorizontalPageBreakRecord;
/*  69:    */ import org.apache.poi.hssf.record.HyperlinkRecord;
/*  70:    */ import org.apache.poi.hssf.record.IndexRecord;
/*  71:    */ import org.apache.poi.hssf.record.InterfaceEndRecord;
/*  72:    */ import org.apache.poi.hssf.record.InterfaceHdrRecord;
/*  73:    */ import org.apache.poi.hssf.record.IterationRecord;
/*  74:    */ import org.apache.poi.hssf.record.LabelRecord;
/*  75:    */ import org.apache.poi.hssf.record.LabelSSTRecord;
/*  76:    */ import org.apache.poi.hssf.record.LeftMarginRecord;
/*  77:    */ import org.apache.poi.hssf.record.MMSRecord;
/*  78:    */ import org.apache.poi.hssf.record.MergeCellsRecord;
/*  79:    */ import org.apache.poi.hssf.record.MulBlankRecord;
/*  80:    */ import org.apache.poi.hssf.record.MulRKRecord;
/*  81:    */ import org.apache.poi.hssf.record.NameCommentRecord;
/*  82:    */ import org.apache.poi.hssf.record.NameRecord;
/*  83:    */ import org.apache.poi.hssf.record.NoteRecord;
/*  84:    */ import org.apache.poi.hssf.record.NumberRecord;
/*  85:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  86:    */ import org.apache.poi.hssf.record.PaletteRecord;
/*  87:    */ import org.apache.poi.hssf.record.PaneRecord;
/*  88:    */ import org.apache.poi.hssf.record.PasswordRecord;
/*  89:    */ import org.apache.poi.hssf.record.PasswordRev4Record;
/*  90:    */ import org.apache.poi.hssf.record.PrecisionRecord;
/*  91:    */ import org.apache.poi.hssf.record.PrintGridlinesRecord;
/*  92:    */ import org.apache.poi.hssf.record.PrintHeadersRecord;
/*  93:    */ import org.apache.poi.hssf.record.PrintSetupRecord;
/*  94:    */ import org.apache.poi.hssf.record.ProtectRecord;
/*  95:    */ import org.apache.poi.hssf.record.ProtectionRev4Record;
/*  96:    */ import org.apache.poi.hssf.record.RKRecord;
/*  97:    */ import org.apache.poi.hssf.record.RecalcIdRecord;
/*  98:    */ import org.apache.poi.hssf.record.Record;
/*  99:    */ import org.apache.poi.hssf.record.RecordInputStream;
/* 100:    */ import org.apache.poi.hssf.record.RecordInputStream.LeftoverDataException;
/* 101:    */ import org.apache.poi.hssf.record.RefModeRecord;
/* 102:    */ import org.apache.poi.hssf.record.RefreshAllRecord;
/* 103:    */ import org.apache.poi.hssf.record.RightMarginRecord;
/* 104:    */ import org.apache.poi.hssf.record.RowRecord;
/* 105:    */ import org.apache.poi.hssf.record.SCLRecord;
/* 106:    */ import org.apache.poi.hssf.record.SSTRecord;
/* 107:    */ import org.apache.poi.hssf.record.SaveRecalcRecord;
/* 108:    */ import org.apache.poi.hssf.record.SelectionRecord;
/* 109:    */ import org.apache.poi.hssf.record.SharedFormulaRecord;
/* 110:    */ import org.apache.poi.hssf.record.StringRecord;
/* 111:    */ import org.apache.poi.hssf.record.StyleRecord;
/* 112:    */ import org.apache.poi.hssf.record.SupBookRecord;
/* 113:    */ import org.apache.poi.hssf.record.TabIdRecord;
/* 114:    */ import org.apache.poi.hssf.record.TableRecord;
/* 115:    */ import org.apache.poi.hssf.record.TableStylesRecord;
/* 116:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/* 117:    */ import org.apache.poi.hssf.record.TopMarginRecord;
/* 118:    */ import org.apache.poi.hssf.record.UncalcedRecord;
/* 119:    */ import org.apache.poi.hssf.record.UnknownRecord;
/* 120:    */ import org.apache.poi.hssf.record.UseSelFSRecord;
/* 121:    */ import org.apache.poi.hssf.record.VCenterRecord;
/* 122:    */ import org.apache.poi.hssf.record.VerticalPageBreakRecord;
/* 123:    */ import org.apache.poi.hssf.record.WSBoolRecord;
/* 124:    */ import org.apache.poi.hssf.record.WindowOneRecord;
/* 125:    */ import org.apache.poi.hssf.record.WindowProtectRecord;
/* 126:    */ import org.apache.poi.hssf.record.WindowTwoRecord;
/* 127:    */ import org.apache.poi.hssf.record.WriteAccessRecord;
/* 128:    */ import org.apache.poi.hssf.record.WriteProtectRecord;
/* 129:    */ import org.apache.poi.hssf.record.chart.AreaFormatRecord;
/* 130:    */ import org.apache.poi.hssf.record.chart.AreaRecord;
/* 131:    */ import org.apache.poi.hssf.record.chart.AxisLineFormatRecord;
/* 132:    */ import org.apache.poi.hssf.record.chart.AxisOptionsRecord;
/* 133:    */ import org.apache.poi.hssf.record.chart.AxisParentRecord;
/* 134:    */ import org.apache.poi.hssf.record.chart.AxisRecord;
/* 135:    */ import org.apache.poi.hssf.record.chart.AxisUsedRecord;
/* 136:    */ import org.apache.poi.hssf.record.chart.BarRecord;
/* 137:    */ import org.apache.poi.hssf.record.chart.BeginRecord;
/* 138:    */ import org.apache.poi.hssf.record.chart.CatLabRecord;
/* 139:    */ import org.apache.poi.hssf.record.chart.CategorySeriesAxisRecord;
/* 140:    */ import org.apache.poi.hssf.record.chart.ChartEndBlockRecord;
/* 141:    */ import org.apache.poi.hssf.record.chart.ChartEndObjectRecord;
/* 142:    */ import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;
/* 143:    */ import org.apache.poi.hssf.record.chart.ChartFormatRecord;
/* 144:    */ import org.apache.poi.hssf.record.chart.ChartRecord;
/* 145:    */ import org.apache.poi.hssf.record.chart.ChartStartBlockRecord;
/* 146:    */ import org.apache.poi.hssf.record.chart.ChartStartObjectRecord;
/* 147:    */ import org.apache.poi.hssf.record.chart.DatRecord;
/* 148:    */ import org.apache.poi.hssf.record.chart.DataFormatRecord;
/* 149:    */ import org.apache.poi.hssf.record.chart.DefaultDataLabelTextPropertiesRecord;
/* 150:    */ import org.apache.poi.hssf.record.chart.EndRecord;
/* 151:    */ import org.apache.poi.hssf.record.chart.FontBasisRecord;
/* 152:    */ import org.apache.poi.hssf.record.chart.FontIndexRecord;
/* 153:    */ import org.apache.poi.hssf.record.chart.FrameRecord;
/* 154:    */ import org.apache.poi.hssf.record.chart.LegendRecord;
/* 155:    */ import org.apache.poi.hssf.record.chart.LineFormatRecord;
/* 156:    */ import org.apache.poi.hssf.record.chart.LinkedDataRecord;
/* 157:    */ import org.apache.poi.hssf.record.chart.ObjectLinkRecord;
/* 158:    */ import org.apache.poi.hssf.record.chart.PlotAreaRecord;
/* 159:    */ import org.apache.poi.hssf.record.chart.PlotGrowthRecord;
/* 160:    */ import org.apache.poi.hssf.record.chart.SeriesIndexRecord;
/* 161:    */ import org.apache.poi.hssf.record.chart.SeriesListRecord;
/* 162:    */ import org.apache.poi.hssf.record.chart.SeriesRecord;
/* 163:    */ import org.apache.poi.hssf.record.chart.SeriesTextRecord;
/* 164:    */ import org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord;
/* 165:    */ import org.apache.poi.hssf.record.chart.SheetPropertiesRecord;
/* 166:    */ import org.apache.poi.hssf.record.chart.TextRecord;
/* 167:    */ import org.apache.poi.hssf.record.chart.TickRecord;
/* 168:    */ import org.apache.poi.hssf.record.chart.UnitsRecord;
/* 169:    */ import org.apache.poi.hssf.record.chart.ValueRangeRecord;
/* 170:    */ import org.apache.poi.hssf.record.pivottable.DataItemRecord;
/* 171:    */ import org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
/* 172:    */ import org.apache.poi.hssf.record.pivottable.PageItemRecord;
/* 173:    */ import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
/* 174:    */ import org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord;
/* 175:    */ import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
/* 176:    */ import org.apache.poi.hssf.record.pivottable.ViewSourceRecord;
/* 177:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/* 178:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/* 179:    */ import org.apache.poi.util.HexDump;
/* 180:    */ import org.apache.poi.util.IOUtils;
/* 181:    */ import org.apache.poi.util.LittleEndian;
/* 182:    */ import org.apache.poi.util.POILogFactory;
/* 183:    */ import org.apache.poi.util.POILogger;
/* 184:    */ import org.apache.poi.util.RecordFormatException;
/* 185:    */ import org.apache.poi.util.StringUtil;
/* 186:    */ import org.apache.poi.util.SuppressForbidden;
/* 187:    */ 
/* 188:    */ public final class BiffViewer
/* 189:    */ {
/* 190: 59 */   private static final char[] NEW_LINE_CHARS = System.getProperty("line.separator").toCharArray();
/* 191: 60 */   private static final POILogger logger = POILogFactory.getLogger(BiffViewer.class);
/* 192:    */   private static final int DUMP_LINE_LEN = 16;
/* 193:    */   
/* 194:    */   public static Record[] createRecords(InputStream is, PrintWriter ps, BiffRecordListener recListener, boolean dumpInterpretedRecords)
/* 195:    */     throws RecordFormatException
/* 196:    */   {
/* 197: 79 */     List<Record> temp = new ArrayList();
/* 198:    */     
/* 199: 81 */     RecordInputStream recStream = new RecordInputStream(is);
/* 200:    */     for (;;)
/* 201:    */     {
/* 202:    */       boolean hasNext;
/* 203:    */       try
/* 204:    */       {
/* 205: 85 */         hasNext = recStream.hasNextRecord();
/* 206:    */       }
/* 207:    */       catch (RecordInputStream.LeftoverDataException e)
/* 208:    */       {
/* 209: 87 */         logger.log(7, new Object[] { "Discarding " + recStream.remaining() + " bytes and continuing", e });
/* 210: 88 */         recStream.readRemainder();
/* 211: 89 */         hasNext = recStream.hasNextRecord();
/* 212:    */       }
/* 213: 91 */       if (!hasNext) {
/* 214:    */         break;
/* 215:    */       }
/* 216: 94 */       recStream.nextRecord();
/* 217: 95 */       if (recStream.getSid() != 0)
/* 218:    */       {
/* 219: 99 */         if (dumpInterpretedRecords)
/* 220:    */         {
/* 221:100 */           Record record = createRecord(recStream);
/* 222:101 */           if (record.getSid() == 60) {
/* 223:    */             continue;
/* 224:    */           }
/* 225:104 */           temp.add(record);
/* 226:106 */           for (String header : recListener.getRecentHeaders()) {
/* 227:107 */             ps.println(header);
/* 228:    */           }
/* 229:109 */           ps.print(record);
/* 230:    */         }
/* 231:    */         else
/* 232:    */         {
/* 233:111 */           recStream.readRemainder();
/* 234:    */         }
/* 235:113 */         ps.println();
/* 236:    */       }
/* 237:    */     }
/* 238:115 */     Record[] result = new Record[temp.size()];
/* 239:116 */     temp.toArray(result);
/* 240:117 */     return result;
/* 241:    */   }
/* 242:    */   
/* 243:    */   private static Record createRecord(RecordInputStream in)
/* 244:    */   {
/* 245:127 */     switch (in.getSid())
/* 246:    */     {
/* 247:    */     case 4106: 
/* 248:128 */       return new AreaFormatRecord(in);
/* 249:    */     case 4122: 
/* 250:129 */       return new AreaRecord(in);
/* 251:    */     case 545: 
/* 252:130 */       return new ArrayRecord(in);
/* 253:    */     case 4129: 
/* 254:131 */       return new AxisLineFormatRecord(in);
/* 255:    */     case 4194: 
/* 256:132 */       return new AxisOptionsRecord(in);
/* 257:    */     case 4161: 
/* 258:133 */       return new AxisParentRecord(in);
/* 259:    */     case 4125: 
/* 260:134 */       return new AxisRecord(in);
/* 261:    */     case 4166: 
/* 262:135 */       return new AxisUsedRecord(in);
/* 263:    */     case 157: 
/* 264:136 */       return new AutoFilterInfoRecord(in);
/* 265:    */     case 2057: 
/* 266:137 */       return new BOFRecord(in);
/* 267:    */     case 64: 
/* 268:138 */       return new BackupRecord(in);
/* 269:    */     case 4119: 
/* 270:139 */       return new BarRecord(in);
/* 271:    */     case 4147: 
/* 272:140 */       return new BeginRecord(in);
/* 273:    */     case 513: 
/* 274:141 */       return new BlankRecord(in);
/* 275:    */     case 218: 
/* 276:142 */       return new BookBoolRecord(in);
/* 277:    */     case 517: 
/* 278:143 */       return new BoolErrRecord(in);
/* 279:    */     case 41: 
/* 280:144 */       return new BottomMarginRecord(in);
/* 281:    */     case 133: 
/* 282:145 */       return new BoundSheetRecord(in);
/* 283:    */     case 432: 
/* 284:146 */       return new CFHeaderRecord(in);
/* 285:    */     case 2169: 
/* 286:147 */       return new CFHeader12Record(in);
/* 287:    */     case 433: 
/* 288:148 */       return new CFRuleRecord(in);
/* 289:    */     case 2170: 
/* 290:149 */       return new CFRule12Record(in);
/* 291:    */     case 12: 
/* 292:151 */       return new CalcCountRecord(in);
/* 293:    */     case 13: 
/* 294:152 */       return new CalcModeRecord(in);
/* 295:    */     case 4128: 
/* 296:153 */       return new CategorySeriesAxisRecord(in);
/* 297:    */     case 4116: 
/* 298:154 */       return new ChartFormatRecord(in);
/* 299:    */     case 4098: 
/* 300:155 */       return new ChartRecord(in);
/* 301:    */     case 66: 
/* 302:156 */       return new CodepageRecord(in);
/* 303:    */     case 125: 
/* 304:157 */       return new ColumnInfoRecord(in);
/* 305:    */     case 60: 
/* 306:158 */       return new ContinueRecord(in);
/* 307:    */     case 140: 
/* 308:159 */       return new CountryRecord(in);
/* 309:    */     case 215: 
/* 310:160 */       return new DBCellRecord(in);
/* 311:    */     case 353: 
/* 312:161 */       return new DSFRecord(in);
/* 313:    */     case 4195: 
/* 314:162 */       return new DatRecord(in);
/* 315:    */     case 4102: 
/* 316:163 */       return new DataFormatRecord(in);
/* 317:    */     case 34: 
/* 318:164 */       return new DateWindow1904Record(in);
/* 319:    */     case 81: 
/* 320:165 */       return new DConRefRecord(in);
/* 321:    */     case 85: 
/* 322:166 */       return new DefaultColWidthRecord(in);
/* 323:    */     case 4132: 
/* 324:167 */       return new DefaultDataLabelTextPropertiesRecord(in);
/* 325:    */     case 549: 
/* 326:168 */       return new DefaultRowHeightRecord(in);
/* 327:    */     case 16: 
/* 328:169 */       return new DeltaRecord(in);
/* 329:    */     case 512: 
/* 330:170 */       return new DimensionsRecord(in);
/* 331:    */     case 235: 
/* 332:171 */       return new DrawingGroupRecord(in);
/* 333:    */     case 236: 
/* 334:172 */       return new DrawingRecordForBiffViewer(in);
/* 335:    */     case 237: 
/* 336:173 */       return new DrawingSelectionRecord(in);
/* 337:    */     case 446: 
/* 338:174 */       return new DVRecord(in);
/* 339:    */     case 434: 
/* 340:175 */       return new DVALRecord(in);
/* 341:    */     case 10: 
/* 342:176 */       return new EOFRecord(in);
/* 343:    */     case 4148: 
/* 344:177 */       return new EndRecord(in);
/* 345:    */     case 255: 
/* 346:178 */       return new ExtSSTRecord(in);
/* 347:    */     case 224: 
/* 348:179 */       return new ExtendedFormatRecord(in);
/* 349:    */     case 23: 
/* 350:180 */       return new ExternSheetRecord(in);
/* 351:    */     case 35: 
/* 352:181 */       return new ExternalNameRecord(in);
/* 353:    */     case 2152: 
/* 354:182 */       return new FeatRecord(in);
/* 355:    */     case 2151: 
/* 356:183 */       return new FeatHdrRecord(in);
/* 357:    */     case 47: 
/* 358:184 */       return new FilePassRecord(in);
/* 359:    */     case 91: 
/* 360:185 */       return new FileSharingRecord(in);
/* 361:    */     case 156: 
/* 362:186 */       return new FnGroupCountRecord(in);
/* 363:    */     case 4192: 
/* 364:187 */       return new FontBasisRecord(in);
/* 365:    */     case 4134: 
/* 366:188 */       return new FontIndexRecord(in);
/* 367:    */     case 49: 
/* 368:189 */       return new FontRecord(in);
/* 369:    */     case 21: 
/* 370:190 */       return new FooterRecord(in);
/* 371:    */     case 1054: 
/* 372:191 */       return new FormatRecord(in);
/* 373:    */     case 6: 
/* 374:192 */       return new FormulaRecord(in);
/* 375:    */     case 4146: 
/* 376:193 */       return new FrameRecord(in);
/* 377:    */     case 130: 
/* 378:194 */       return new GridsetRecord(in);
/* 379:    */     case 128: 
/* 380:195 */       return new GutsRecord(in);
/* 381:    */     case 131: 
/* 382:196 */       return new HCenterRecord(in);
/* 383:    */     case 20: 
/* 384:197 */       return new HeaderRecord(in);
/* 385:    */     case 141: 
/* 386:198 */       return new HideObjRecord(in);
/* 387:    */     case 27: 
/* 388:199 */       return new HorizontalPageBreakRecord(in);
/* 389:    */     case 440: 
/* 390:200 */       return new HyperlinkRecord(in);
/* 391:    */     case 523: 
/* 392:201 */       return new IndexRecord(in);
/* 393:    */     case 226: 
/* 394:202 */       return InterfaceEndRecord.create(in);
/* 395:    */     case 225: 
/* 396:203 */       return new InterfaceHdrRecord(in);
/* 397:    */     case 17: 
/* 398:204 */       return new IterationRecord(in);
/* 399:    */     case 516: 
/* 400:205 */       return new LabelRecord(in);
/* 401:    */     case 253: 
/* 402:206 */       return new LabelSSTRecord(in);
/* 403:    */     case 38: 
/* 404:207 */       return new LeftMarginRecord(in);
/* 405:    */     case 4117: 
/* 406:208 */       return new LegendRecord(in);
/* 407:    */     case 4103: 
/* 408:209 */       return new LineFormatRecord(in);
/* 409:    */     case 4177: 
/* 410:210 */       return new LinkedDataRecord(in);
/* 411:    */     case 193: 
/* 412:211 */       return new MMSRecord(in);
/* 413:    */     case 229: 
/* 414:212 */       return new MergeCellsRecord(in);
/* 415:    */     case 190: 
/* 416:213 */       return new MulBlankRecord(in);
/* 417:    */     case 189: 
/* 418:214 */       return new MulRKRecord(in);
/* 419:    */     case 24: 
/* 420:215 */       return new NameRecord(in);
/* 421:    */     case 2196: 
/* 422:216 */       return new NameCommentRecord(in);
/* 423:    */     case 28: 
/* 424:217 */       return new NoteRecord(in);
/* 425:    */     case 515: 
/* 426:218 */       return new NumberRecord(in);
/* 427:    */     case 93: 
/* 428:219 */       return new ObjRecord(in);
/* 429:    */     case 4135: 
/* 430:220 */       return new ObjectLinkRecord(in);
/* 431:    */     case 146: 
/* 432:221 */       return new PaletteRecord(in);
/* 433:    */     case 65: 
/* 434:222 */       return new PaneRecord(in);
/* 435:    */     case 19: 
/* 436:223 */       return new PasswordRecord(in);
/* 437:    */     case 444: 
/* 438:224 */       return new PasswordRev4Record(in);
/* 439:    */     case 4149: 
/* 440:225 */       return new PlotAreaRecord(in);
/* 441:    */     case 4196: 
/* 442:226 */       return new PlotGrowthRecord(in);
/* 443:    */     case 14: 
/* 444:227 */       return new PrecisionRecord(in);
/* 445:    */     case 43: 
/* 446:228 */       return new PrintGridlinesRecord(in);
/* 447:    */     case 42: 
/* 448:229 */       return new PrintHeadersRecord(in);
/* 449:    */     case 161: 
/* 450:230 */       return new PrintSetupRecord(in);
/* 451:    */     case 18: 
/* 452:231 */       return new ProtectRecord(in);
/* 453:    */     case 431: 
/* 454:232 */       return new ProtectionRev4Record(in);
/* 455:    */     case 638: 
/* 456:233 */       return new RKRecord(in);
/* 457:    */     case 449: 
/* 458:234 */       return new RecalcIdRecord(in);
/* 459:    */     case 15: 
/* 460:235 */       return new RefModeRecord(in);
/* 461:    */     case 439: 
/* 462:236 */       return new RefreshAllRecord(in);
/* 463:    */     case 39: 
/* 464:237 */       return new RightMarginRecord(in);
/* 465:    */     case 520: 
/* 466:238 */       return new RowRecord(in);
/* 467:    */     case 160: 
/* 468:239 */       return new SCLRecord(in);
/* 469:    */     case 252: 
/* 470:240 */       return new SSTRecord(in);
/* 471:    */     case 95: 
/* 472:241 */       return new SaveRecalcRecord(in);
/* 473:    */     case 29: 
/* 474:242 */       return new SelectionRecord(in);
/* 475:    */     case 4197: 
/* 476:243 */       return new SeriesIndexRecord(in);
/* 477:    */     case 4118: 
/* 478:244 */       return new SeriesListRecord(in);
/* 479:    */     case 4099: 
/* 480:245 */       return new SeriesRecord(in);
/* 481:    */     case 4109: 
/* 482:246 */       return new SeriesTextRecord(in);
/* 483:    */     case 4165: 
/* 484:247 */       return new SeriesToChartGroupRecord(in);
/* 485:    */     case 1212: 
/* 486:248 */       return new SharedFormulaRecord(in);
/* 487:    */     case 4164: 
/* 488:249 */       return new SheetPropertiesRecord(in);
/* 489:    */     case 519: 
/* 490:250 */       return new StringRecord(in);
/* 491:    */     case 659: 
/* 492:251 */       return new StyleRecord(in);
/* 493:    */     case 430: 
/* 494:252 */       return new SupBookRecord(in);
/* 495:    */     case 317: 
/* 496:253 */       return new TabIdRecord(in);
/* 497:    */     case 2190: 
/* 498:254 */       return new TableStylesRecord(in);
/* 499:    */     case 566: 
/* 500:255 */       return new TableRecord(in);
/* 501:    */     case 438: 
/* 502:256 */       return new TextObjectRecord(in);
/* 503:    */     case 4133: 
/* 504:257 */       return new TextRecord(in);
/* 505:    */     case 4126: 
/* 506:258 */       return new TickRecord(in);
/* 507:    */     case 40: 
/* 508:259 */       return new TopMarginRecord(in);
/* 509:    */     case 94: 
/* 510:260 */       return new UncalcedRecord(in);
/* 511:    */     case 4097: 
/* 512:261 */       return new UnitsRecord(in);
/* 513:    */     case 352: 
/* 514:262 */       return new UseSelFSRecord(in);
/* 515:    */     case 132: 
/* 516:263 */       return new VCenterRecord(in);
/* 517:    */     case 4127: 
/* 518:264 */       return new ValueRangeRecord(in);
/* 519:    */     case 26: 
/* 520:265 */       return new VerticalPageBreakRecord(in);
/* 521:    */     case 129: 
/* 522:266 */       return new WSBoolRecord(in);
/* 523:    */     case 61: 
/* 524:267 */       return new WindowOneRecord(in);
/* 525:    */     case 25: 
/* 526:268 */       return new WindowProtectRecord(in);
/* 527:    */     case 574: 
/* 528:269 */       return new WindowTwoRecord(in);
/* 529:    */     case 92: 
/* 530:270 */       return new WriteAccessRecord(in);
/* 531:    */     case 134: 
/* 532:271 */       return new WriteProtectRecord(in);
/* 533:    */     case 2134: 
/* 534:274 */       return new CatLabRecord(in);
/* 535:    */     case 2131: 
/* 536:275 */       return new ChartEndBlockRecord(in);
/* 537:    */     case 2133: 
/* 538:276 */       return new ChartEndObjectRecord(in);
/* 539:    */     case 2128: 
/* 540:277 */       return new ChartFRTInfoRecord(in);
/* 541:    */     case 2130: 
/* 542:278 */       return new ChartStartBlockRecord(in);
/* 543:    */     case 2132: 
/* 544:279 */       return new ChartStartObjectRecord(in);
/* 545:    */     case 213: 
/* 546:282 */       return new StreamIDRecord(in);
/* 547:    */     case 227: 
/* 548:283 */       return new ViewSourceRecord(in);
/* 549:    */     case 182: 
/* 550:284 */       return new PageItemRecord(in);
/* 551:    */     case 176: 
/* 552:285 */       return new ViewDefinitionRecord(in);
/* 553:    */     case 177: 
/* 554:286 */       return new ViewFieldsRecord(in);
/* 555:    */     case 197: 
/* 556:287 */       return new DataItemRecord(in);
/* 557:    */     case 256: 
/* 558:288 */       return new ExtendedPivotTableViewFieldsRecord(in);
/* 559:    */     }
/* 560:290 */     return new UnknownRecord(in);
/* 561:    */   }
/* 562:    */   
/* 563:    */   private static final class CommandArgs
/* 564:    */   {
/* 565:    */     private final boolean _biffhex;
/* 566:    */     private final boolean _noint;
/* 567:    */     private final boolean _out;
/* 568:    */     private final boolean _rawhex;
/* 569:    */     private final boolean _noHeader;
/* 570:    */     private final File _file;
/* 571:    */     
/* 572:    */     private CommandArgs(boolean biffhex, boolean noint, boolean out, boolean rawhex, boolean noHeader, File file)
/* 573:    */     {
/* 574:303 */       this._biffhex = biffhex;
/* 575:304 */       this._noint = noint;
/* 576:305 */       this._out = out;
/* 577:306 */       this._rawhex = rawhex;
/* 578:307 */       this._file = file;
/* 579:308 */       this._noHeader = noHeader;
/* 580:    */     }
/* 581:    */     
/* 582:    */     public static CommandArgs parse(String[] args)
/* 583:    */       throws CommandParseException
/* 584:    */     {
/* 585:312 */       int nArgs = args.length;
/* 586:313 */       boolean biffhex = false;
/* 587:314 */       boolean noint = false;
/* 588:315 */       boolean out = false;
/* 589:316 */       boolean rawhex = false;
/* 590:317 */       boolean noheader = false;
/* 591:318 */       File file = null;
/* 592:319 */       for (int i = 0; i < nArgs; i++)
/* 593:    */       {
/* 594:320 */         String arg = args[i];
/* 595:321 */         if (arg.startsWith("--"))
/* 596:    */         {
/* 597:322 */           if ("--biffhex".equals(arg)) {
/* 598:323 */             biffhex = true;
/* 599:324 */           } else if ("--noint".equals(arg)) {
/* 600:325 */             noint = true;
/* 601:326 */           } else if ("--out".equals(arg)) {
/* 602:327 */             out = true;
/* 603:328 */           } else if ("--escher".equals(arg)) {
/* 604:329 */             System.setProperty("poi.deserialize.escher", "true");
/* 605:330 */           } else if ("--rawhex".equals(arg)) {
/* 606:331 */             rawhex = true;
/* 607:332 */           } else if ("--noheader".equals(arg)) {
/* 608:333 */             noheader = true;
/* 609:    */           } else {
/* 610:335 */             throw new CommandParseException("Unexpected option '" + arg + "'");
/* 611:    */           }
/* 612:    */         }
/* 613:    */         else
/* 614:    */         {
/* 615:339 */           file = new File(arg);
/* 616:340 */           if (!file.exists()) {
/* 617:341 */             throw new CommandParseException("Specified file '" + arg + "' does not exist");
/* 618:    */           }
/* 619:343 */           if (i + 1 < nArgs) {
/* 620:344 */             throw new CommandParseException("File name must be the last arg");
/* 621:    */           }
/* 622:    */         }
/* 623:    */       }
/* 624:347 */       if (file == null) {
/* 625:348 */         throw new CommandParseException("Biff viewer needs a filename");
/* 626:    */       }
/* 627:350 */       return new CommandArgs(biffhex, noint, out, rawhex, noheader, file);
/* 628:    */     }
/* 629:    */     
/* 630:    */     public boolean shouldDumpBiffHex()
/* 631:    */     {
/* 632:353 */       return this._biffhex;
/* 633:    */     }
/* 634:    */     
/* 635:    */     public boolean shouldDumpRecordInterpretations()
/* 636:    */     {
/* 637:356 */       return !this._noint;
/* 638:    */     }
/* 639:    */     
/* 640:    */     public boolean shouldOutputToFile()
/* 641:    */     {
/* 642:359 */       return this._out;
/* 643:    */     }
/* 644:    */     
/* 645:    */     public boolean shouldOutputRawHexOnly()
/* 646:    */     {
/* 647:362 */       return this._rawhex;
/* 648:    */     }
/* 649:    */     
/* 650:    */     public boolean suppressHeader()
/* 651:    */     {
/* 652:365 */       return this._noHeader;
/* 653:    */     }
/* 654:    */     
/* 655:    */     public File getFile()
/* 656:    */     {
/* 657:368 */       return this._file;
/* 658:    */     }
/* 659:    */   }
/* 660:    */   
/* 661:    */   private static final class CommandParseException
/* 662:    */     extends Exception
/* 663:    */   {
/* 664:    */     public CommandParseException(String msg)
/* 665:    */     {
/* 666:373 */       super();
/* 667:    */     }
/* 668:    */   }
/* 669:    */   
/* 670:    */   public static void main(String[] args)
/* 671:    */     throws IOException, CommandParseException
/* 672:    */   {
/* 673:402 */     CommandArgs cmdArgs = CommandArgs.parse(args);
/* 674:    */     PrintWriter pw;
/* 675:    */     PrintWriter pw;
/* 676:405 */     if (cmdArgs.shouldOutputToFile())
/* 677:    */     {
/* 678:406 */       OutputStream os = new FileOutputStream(cmdArgs.getFile().getAbsolutePath() + ".out");
/* 679:407 */       pw = new PrintWriter(new OutputStreamWriter(os, StringUtil.UTF8));
/* 680:    */     }
/* 681:    */     else
/* 682:    */     {
/* 683:410 */       pw = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
/* 684:    */     }
/* 685:413 */     NPOIFSFileSystem fs = null;
/* 686:414 */     InputStream is = null;
/* 687:    */     try
/* 688:    */     {
/* 689:416 */       fs = new NPOIFSFileSystem(cmdArgs.getFile(), true);
/* 690:417 */       is = getPOIFSInputStream(fs);
/* 691:419 */       if (cmdArgs.shouldOutputRawHexOnly())
/* 692:    */       {
/* 693:420 */         byte[] data = IOUtils.toByteArray(is);
/* 694:421 */         HexDump.dump(data, 0L, System.out, 0);
/* 695:    */       }
/* 696:    */       else
/* 697:    */       {
/* 698:423 */         boolean dumpInterpretedRecords = cmdArgs.shouldDumpRecordInterpretations();
/* 699:424 */         boolean dumpHex = cmdArgs.shouldDumpBiffHex();
/* 700:425 */         boolean zeroAlignHexDump = dumpInterpretedRecords;
/* 701:426 */         runBiffViewer(pw, is, dumpInterpretedRecords, dumpHex, zeroAlignHexDump, cmdArgs.suppressHeader());
/* 702:    */       }
/* 703:    */     }
/* 704:    */     finally
/* 705:    */     {
/* 706:430 */       IOUtils.closeQuietly(is);
/* 707:431 */       IOUtils.closeQuietly(fs);
/* 708:432 */       IOUtils.closeQuietly(pw);
/* 709:    */     }
/* 710:    */   }
/* 711:    */   
/* 712:    */   protected static InputStream getPOIFSInputStream(NPOIFSFileSystem fs)
/* 713:    */     throws IOException, FileNotFoundException
/* 714:    */   {
/* 715:438 */     String workbookName = HSSFWorkbook.getWorkbookDirEntryName(fs.getRoot());
/* 716:439 */     return fs.createDocumentInputStream(workbookName);
/* 717:    */   }
/* 718:    */   
/* 719:    */   protected static void runBiffViewer(PrintWriter pw, InputStream is, boolean dumpInterpretedRecords, boolean dumpHex, boolean zeroAlignHexDump, boolean suppressHeader)
/* 720:    */   {
/* 721:445 */     BiffRecordListener recListener = new BiffRecordListener(dumpHex ? pw : null, zeroAlignHexDump, suppressHeader);
/* 722:446 */     is = new BiffDumpingStream(is, recListener);
/* 723:447 */     createRecords(is, pw, recListener, dumpInterpretedRecords);
/* 724:    */   }
/* 725:    */   
/* 726:    */   private static final class BiffRecordListener
/* 727:    */     implements IBiffRecordListener
/* 728:    */   {
/* 729:    */     private final Writer _hexDumpWriter;
/* 730:    */     private List<String> _headers;
/* 731:    */     private final boolean _zeroAlignEachRecord;
/* 732:    */     private final boolean _noHeader;
/* 733:    */     
/* 734:    */     public BiffRecordListener(Writer hexDumpWriter, boolean zeroAlignEachRecord, boolean noHeader)
/* 735:    */     {
/* 736:456 */       this._hexDumpWriter = hexDumpWriter;
/* 737:457 */       this._zeroAlignEachRecord = zeroAlignEachRecord;
/* 738:458 */       this._noHeader = noHeader;
/* 739:459 */       this._headers = new ArrayList();
/* 740:    */     }
/* 741:    */     
/* 742:    */     public void processRecord(int globalOffset, int recordCounter, int sid, int dataSize, byte[] data)
/* 743:    */     {
/* 744:465 */       String header = formatRecordDetails(globalOffset, sid, dataSize, recordCounter);
/* 745:466 */       if (!this._noHeader) {
/* 746:467 */         this._headers.add(header);
/* 747:    */       }
/* 748:469 */       Writer w = this._hexDumpWriter;
/* 749:470 */       if (w != null) {
/* 750:    */         try
/* 751:    */         {
/* 752:472 */           w.write(header);
/* 753:473 */           w.write(BiffViewer.NEW_LINE_CHARS);
/* 754:474 */           BiffViewer.hexDumpAligned(w, data, dataSize + 4, globalOffset, this._zeroAlignEachRecord);
/* 755:475 */           w.flush();
/* 756:    */         }
/* 757:    */         catch (IOException e)
/* 758:    */         {
/* 759:477 */           throw new RuntimeException(e);
/* 760:    */         }
/* 761:    */       }
/* 762:    */     }
/* 763:    */     
/* 764:    */     public List<String> getRecentHeaders()
/* 765:    */     {
/* 766:482 */       List<String> result = this._headers;
/* 767:483 */       this._headers = new ArrayList();
/* 768:484 */       return result;
/* 769:    */     }
/* 770:    */     
/* 771:    */     private static String formatRecordDetails(int globalOffset, int sid, int size, int recordCounter)
/* 772:    */     {
/* 773:487 */       StringBuilder sb = new StringBuilder(64);
/* 774:488 */       sb.append("Offset=").append(HexDump.intToHex(globalOffset)).append("(").append(globalOffset).append(")");
/* 775:489 */       sb.append(" recno=").append(recordCounter);
/* 776:490 */       sb.append(" sid=").append(HexDump.shortToHex(sid));
/* 777:491 */       sb.append(" size=").append(HexDump.shortToHex(size)).append("(").append(size).append(")");
/* 778:492 */       return sb.toString();
/* 779:    */     }
/* 780:    */   }
/* 781:    */   
/* 782:    */   private static abstract interface IBiffRecordListener
/* 783:    */   {
/* 784:    */     public abstract void processRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);
/* 785:    */   }
/* 786:    */   
/* 787:    */   private static final class BiffDumpingStream
/* 788:    */     extends InputStream
/* 789:    */   {
/* 790:    */     private final DataInputStream _is;
/* 791:    */     private final IBiffRecordListener _listener;
/* 792:    */     private final byte[] _data;
/* 793:    */     private int _recordCounter;
/* 794:    */     private int _overallStreamPos;
/* 795:    */     private int _currentPos;
/* 796:    */     private int _currentSize;
/* 797:    */     private boolean _innerHasReachedEOF;
/* 798:    */     
/* 799:    */     public BiffDumpingStream(InputStream is, IBiffRecordListener listener)
/* 800:    */     {
/* 801:517 */       this._is = new DataInputStream(is);
/* 802:518 */       this._listener = listener;
/* 803:519 */       this._data = new byte[8228];
/* 804:520 */       this._recordCounter = 0;
/* 805:521 */       this._overallStreamPos = 0;
/* 806:522 */       this._currentSize = 0;
/* 807:523 */       this._currentPos = 0;
/* 808:    */     }
/* 809:    */     
/* 810:    */     public int read()
/* 811:    */       throws IOException
/* 812:    */     {
/* 813:528 */       if (this._currentPos >= this._currentSize) {
/* 814:529 */         fillNextBuffer();
/* 815:    */       }
/* 816:531 */       if (this._currentPos >= this._currentSize) {
/* 817:532 */         return -1;
/* 818:    */       }
/* 819:534 */       int result = this._data[this._currentPos] & 0xFF;
/* 820:535 */       this._currentPos += 1;
/* 821:536 */       this._overallStreamPos += 1;
/* 822:537 */       formatBufferIfAtEndOfRec();
/* 823:538 */       return result;
/* 824:    */     }
/* 825:    */     
/* 826:    */     public int read(byte[] b, int off, int len)
/* 827:    */       throws IOException
/* 828:    */     {
/* 829:542 */       if (this._currentPos >= this._currentSize) {
/* 830:543 */         fillNextBuffer();
/* 831:    */       }
/* 832:545 */       if (this._currentPos >= this._currentSize) {
/* 833:546 */         return -1;
/* 834:    */       }
/* 835:548 */       int availSize = this._currentSize - this._currentPos;
/* 836:    */       int result;
/* 837:    */       int result;
/* 838:550 */       if (len > availSize)
/* 839:    */       {
/* 840:551 */         System.err.println("Unexpected request to read past end of current biff record");
/* 841:552 */         result = availSize;
/* 842:    */       }
/* 843:    */       else
/* 844:    */       {
/* 845:554 */         result = len;
/* 846:    */       }
/* 847:556 */       System.arraycopy(this._data, this._currentPos, b, off, result);
/* 848:557 */       this._currentPos += result;
/* 849:558 */       this._overallStreamPos += result;
/* 850:559 */       formatBufferIfAtEndOfRec();
/* 851:560 */       return result;
/* 852:    */     }
/* 853:    */     
/* 854:    */     @SuppressForbidden("just delegating the call")
/* 855:    */     public int available()
/* 856:    */       throws IOException
/* 857:    */     {
/* 858:566 */       return this._currentSize - this._currentPos + this._is.available();
/* 859:    */     }
/* 860:    */     
/* 861:    */     private void fillNextBuffer()
/* 862:    */       throws IOException
/* 863:    */     {
/* 864:569 */       if (this._innerHasReachedEOF) {
/* 865:570 */         return;
/* 866:    */       }
/* 867:572 */       int b0 = this._is.read();
/* 868:573 */       if (b0 == -1)
/* 869:    */       {
/* 870:574 */         this._innerHasReachedEOF = true;
/* 871:575 */         return;
/* 872:    */       }
/* 873:577 */       this._data[0] = ((byte)b0);
/* 874:578 */       this._is.readFully(this._data, 1, 3);
/* 875:579 */       int len = LittleEndian.getShort(this._data, 2);
/* 876:580 */       this._is.readFully(this._data, 4, len);
/* 877:581 */       this._currentPos = 0;
/* 878:582 */       this._currentSize = (len + 4);
/* 879:583 */       this._recordCounter += 1;
/* 880:    */     }
/* 881:    */     
/* 882:    */     private void formatBufferIfAtEndOfRec()
/* 883:    */     {
/* 884:586 */       if (this._currentPos != this._currentSize) {
/* 885:587 */         return;
/* 886:    */       }
/* 887:589 */       int dataSize = this._currentSize - 4;
/* 888:590 */       int sid = LittleEndian.getShort(this._data, 0);
/* 889:591 */       int globalOffset = this._overallStreamPos - this._currentSize;
/* 890:592 */       this._listener.processRecord(globalOffset, this._recordCounter, sid, dataSize, this._data);
/* 891:    */     }
/* 892:    */     
/* 893:    */     public void close()
/* 894:    */       throws IOException
/* 895:    */     {
/* 896:596 */       this._is.close();
/* 897:    */     }
/* 898:    */   }
/* 899:    */   
/* 900:601 */   private static final char[] COLUMN_SEPARATOR = " | ".toCharArray();
/* 901:    */   
/* 902:    */   static void hexDumpAligned(Writer w, byte[] data, int dumpLen, int globalOffset, boolean zeroAlignEachRecord)
/* 903:    */   {
/* 904:609 */     int baseDataOffset = 0;
/* 905:    */     
/* 906:    */ 
/* 907:612 */     int globalStart = globalOffset + baseDataOffset;
/* 908:613 */     int globalEnd = globalOffset + baseDataOffset + dumpLen;
/* 909:614 */     int startDelta = globalStart % 16;
/* 910:615 */     int endDelta = globalEnd % 16;
/* 911:616 */     if (zeroAlignEachRecord)
/* 912:    */     {
/* 913:617 */       endDelta -= startDelta;
/* 914:618 */       if (endDelta < 0) {
/* 915:619 */         endDelta += 16;
/* 916:    */       }
/* 917:621 */       startDelta = 0;
/* 918:    */     }
/* 919:    */     int startLineAddr;
/* 920:    */     int startLineAddr;
/* 921:    */     int endLineAddr;
/* 922:625 */     if (zeroAlignEachRecord)
/* 923:    */     {
/* 924:626 */       int endLineAddr = globalEnd - endDelta - (globalStart - startDelta);
/* 925:627 */       startLineAddr = 0;
/* 926:    */     }
/* 927:    */     else
/* 928:    */     {
/* 929:629 */       startLineAddr = globalStart - startDelta;
/* 930:630 */       endLineAddr = globalEnd - endDelta;
/* 931:    */     }
/* 932:633 */     int lineDataOffset = baseDataOffset - startDelta;
/* 933:634 */     int lineAddr = startLineAddr;
/* 934:637 */     if (startLineAddr == endLineAddr)
/* 935:    */     {
/* 936:638 */       hexDumpLine(w, data, lineAddr, lineDataOffset, startDelta, endDelta);
/* 937:639 */       return;
/* 938:    */     }
/* 939:641 */     hexDumpLine(w, data, lineAddr, lineDataOffset, startDelta, 16);
/* 940:    */     for (;;)
/* 941:    */     {
/* 942:645 */       lineAddr += 16;
/* 943:646 */       lineDataOffset += 16;
/* 944:647 */       if (lineAddr >= endLineAddr) {
/* 945:    */         break;
/* 946:    */       }
/* 947:650 */       hexDumpLine(w, data, lineAddr, lineDataOffset, 0, 16);
/* 948:    */     }
/* 949:655 */     if (endDelta != 0) {
/* 950:656 */       hexDumpLine(w, data, lineAddr, lineDataOffset, 0, endDelta);
/* 951:    */     }
/* 952:    */   }
/* 953:    */   
/* 954:    */   private static void hexDumpLine(Writer w, byte[] data, int lineStartAddress, int lineDataOffset, int startDelta, int endDelta)
/* 955:    */   {
/* 956:661 */     char[] buf = new char[8 + 2 * COLUMN_SEPARATOR.length + 48 - 1 + 16 + NEW_LINE_CHARS.length];
/* 957:663 */     if (startDelta >= endDelta) {
/* 958:664 */       throw new IllegalArgumentException("Bad start/end delta");
/* 959:    */     }
/* 960:666 */     int idx = 0;
/* 961:    */     try
/* 962:    */     {
/* 963:668 */       writeHex(buf, idx, lineStartAddress, 8);
/* 964:669 */       idx = arraycopy(COLUMN_SEPARATOR, buf, idx + 8);
/* 965:671 */       for (int i = 0; i < 16; i++)
/* 966:    */       {
/* 967:672 */         if (i > 0) {
/* 968:673 */           buf[(idx++)] = ' ';
/* 969:    */         }
/* 970:675 */         if ((i >= startDelta) && (i < endDelta))
/* 971:    */         {
/* 972:676 */           writeHex(buf, idx, data[(lineDataOffset + i)], 2);
/* 973:    */         }
/* 974:    */         else
/* 975:    */         {
/* 976:678 */           buf[idx] = ' ';
/* 977:679 */           buf[(idx + 1)] = ' ';
/* 978:    */         }
/* 979:681 */         idx += 2;
/* 980:    */       }
/* 981:683 */       idx = arraycopy(COLUMN_SEPARATOR, buf, idx);
/* 982:686 */       for (int i = 0; i < 16; i++)
/* 983:    */       {
/* 984:687 */         char ch = ' ';
/* 985:688 */         if ((i >= startDelta) && (i < endDelta)) {
/* 986:689 */           ch = getPrintableChar(data[(lineDataOffset + i)]);
/* 987:    */         }
/* 988:691 */         buf[(idx++)] = ch;
/* 989:    */       }
/* 990:694 */       idx = arraycopy(NEW_LINE_CHARS, buf, idx);
/* 991:    */       
/* 992:696 */       w.write(buf, 0, idx);
/* 993:    */     }
/* 994:    */     catch (IOException e)
/* 995:    */     {
/* 996:698 */       throw new RuntimeException(e);
/* 997:    */     }
/* 998:    */   }
/* 999:    */   
/* :00:    */   private static int arraycopy(char[] in, char[] out, int pos)
/* :01:    */   {
/* :02:703 */     int idx = pos;
/* :03:704 */     for (char c : in) {
/* :04:705 */       out[(idx++)] = c;
/* :05:    */     }
/* :06:707 */     return idx;
/* :07:    */   }
/* :08:    */   
/* :09:    */   private static char getPrintableChar(byte b)
/* :10:    */   {
/* :11:711 */     char ib = (char)(b & 0xFF);
/* :12:712 */     if ((ib < ' ') || (ib > '~')) {
/* :13:713 */       return '.';
/* :14:    */     }
/* :15:715 */     return ib;
/* :16:    */   }
/* :17:    */   
/* :18:    */   private static void writeHex(char[] buf, int startInBuf, int value, int nDigits)
/* :19:    */     throws IOException
/* :20:    */   {
/* :21:719 */     int acc = value;
/* :22:720 */     for (int i = nDigits - 1; i >= 0; i--)
/* :23:    */     {
/* :24:721 */       int digit = acc & 0xF;
/* :25:722 */       buf[(startInBuf + i)] = ((char)(digit < 10 ? 48 + digit : 65 + digit - 10));
/* :26:723 */       acc >>>= 4;
/* :27:    */     }
/* :28:    */   }
/* :29:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.dev.BiffViewer

 * JD-Core Version:    0.7.0.1

 */