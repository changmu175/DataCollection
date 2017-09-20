/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.lang.reflect.Constructor;
/*   5:    */ import java.lang.reflect.Field;
/*   6:    */ import java.lang.reflect.InvocationTargetException;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.lang.reflect.Modifier;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.HashSet;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Locale;
/*  15:    */ import java.util.Map;
/*  16:    */ import java.util.Set;
/*  17:    */ import org.apache.poi.EncryptedDocumentException;
/*  18:    */ import org.apache.poi.hssf.record.chart.BeginRecord;
/*  19:    */ import org.apache.poi.hssf.record.chart.CatLabRecord;
/*  20:    */ import org.apache.poi.hssf.record.chart.ChartEndBlockRecord;
/*  21:    */ import org.apache.poi.hssf.record.chart.ChartEndObjectRecord;
/*  22:    */ import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;
/*  23:    */ import org.apache.poi.hssf.record.chart.ChartRecord;
/*  24:    */ import org.apache.poi.hssf.record.chart.ChartStartBlockRecord;
/*  25:    */ import org.apache.poi.hssf.record.chart.ChartStartObjectRecord;
/*  26:    */ import org.apache.poi.hssf.record.chart.ChartTitleFormatRecord;
/*  27:    */ import org.apache.poi.hssf.record.chart.DataFormatRecord;
/*  28:    */ import org.apache.poi.hssf.record.chart.EndRecord;
/*  29:    */ import org.apache.poi.hssf.record.chart.LegendRecord;
/*  30:    */ import org.apache.poi.hssf.record.chart.LinkedDataRecord;
/*  31:    */ import org.apache.poi.hssf.record.chart.SeriesRecord;
/*  32:    */ import org.apache.poi.hssf.record.chart.SeriesTextRecord;
/*  33:    */ import org.apache.poi.hssf.record.chart.SeriesToChartGroupRecord;
/*  34:    */ import org.apache.poi.hssf.record.chart.ValueRangeRecord;
/*  35:    */ import org.apache.poi.hssf.record.pivottable.DataItemRecord;
/*  36:    */ import org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord;
/*  37:    */ import org.apache.poi.hssf.record.pivottable.PageItemRecord;
/*  38:    */ import org.apache.poi.hssf.record.pivottable.StreamIDRecord;
/*  39:    */ import org.apache.poi.hssf.record.pivottable.ViewDefinitionRecord;
/*  40:    */ import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
/*  41:    */ import org.apache.poi.hssf.record.pivottable.ViewSourceRecord;
/*  42:    */ import org.apache.poi.util.RecordFormatException;
/*  43:    */ 
/*  44:    */ public final class RecordFactory
/*  45:    */ {
/*  46:    */   private static final int NUM_RECORDS = 512;
/*  47:    */   
/*  48:    */   private static abstract interface I_RecordCreator
/*  49:    */   {
/*  50:    */     public abstract Record create(RecordInputStream paramRecordInputStream);
/*  51:    */     
/*  52:    */     public abstract Class<? extends Record> getRecordClass();
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static final class ReflectionConstructorRecordCreator
/*  56:    */     implements I_RecordCreator
/*  57:    */   {
/*  58:    */     private final Constructor<? extends Record> _c;
/*  59:    */     
/*  60:    */     public ReflectionConstructorRecordCreator(Constructor<? extends Record> c)
/*  61:    */     {
/*  62: 78 */       this._c = c;
/*  63:    */     }
/*  64:    */     
/*  65:    */     public Record create(RecordInputStream in)
/*  66:    */     {
/*  67: 82 */       Object[] args = { in };
/*  68:    */       try
/*  69:    */       {
/*  70: 84 */         return (Record)this._c.newInstance(args);
/*  71:    */       }
/*  72:    */       catch (IllegalArgumentException e)
/*  73:    */       {
/*  74: 86 */         throw new RuntimeException(e);
/*  75:    */       }
/*  76:    */       catch (InstantiationException e)
/*  77:    */       {
/*  78: 88 */         throw new RuntimeException(e);
/*  79:    */       }
/*  80:    */       catch (IllegalAccessException e)
/*  81:    */       {
/*  82: 90 */         throw new RuntimeException(e);
/*  83:    */       }
/*  84:    */       catch (InvocationTargetException e)
/*  85:    */       {
/*  86: 92 */         Throwable t = e.getTargetException();
/*  87: 93 */         if ((t instanceof RecordFormatException)) {
/*  88: 94 */           throw ((RecordFormatException)t);
/*  89:    */         }
/*  90: 95 */         if ((t instanceof EncryptedDocumentException)) {
/*  91: 96 */           throw ((EncryptedDocumentException)t);
/*  92:    */         }
/*  93: 98 */         throw new RecordFormatException("Unable to construct record instance", t);
/*  94:    */       }
/*  95:    */     }
/*  96:    */     
/*  97:    */     public Class<? extends Record> getRecordClass()
/*  98:    */     {
/*  99:104 */       return this._c.getDeclaringClass();
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static final class ReflectionMethodRecordCreator
/* 104:    */     implements I_RecordCreator
/* 105:    */   {
/* 106:    */     private final Method _m;
/* 107:    */     
/* 108:    */     public ReflectionMethodRecordCreator(Method m)
/* 109:    */     {
/* 110:114 */       this._m = m;
/* 111:    */     }
/* 112:    */     
/* 113:    */     public Record create(RecordInputStream in)
/* 114:    */     {
/* 115:118 */       Object[] args = { in };
/* 116:    */       try
/* 117:    */       {
/* 118:120 */         return (Record)this._m.invoke(null, args);
/* 119:    */       }
/* 120:    */       catch (IllegalArgumentException e)
/* 121:    */       {
/* 122:122 */         throw new RuntimeException(e);
/* 123:    */       }
/* 124:    */       catch (IllegalAccessException e)
/* 125:    */       {
/* 126:124 */         throw new RuntimeException(e);
/* 127:    */       }
/* 128:    */       catch (InvocationTargetException e)
/* 129:    */       {
/* 130:126 */         throw new RecordFormatException("Unable to construct record instance", e.getTargetException());
/* 131:    */       }
/* 132:    */     }
/* 133:    */     
/* 134:    */     public Class<? extends Record> getRecordClass()
/* 135:    */     {
/* 136:132 */       return this._m.getDeclaringClass();
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:136 */   private static final Class<?>[] CONSTRUCTOR_ARGS = { RecordInputStream.class };
/* 141:143 */   private static final Class<? extends Record>[] recordClasses = { ArrayRecord.class, AutoFilterInfoRecord.class, BackupRecord.class, BlankRecord.class, BOFRecord.class, BookBoolRecord.class, BoolErrRecord.class, BottomMarginRecord.class, BoundSheetRecord.class, CalcCountRecord.class, CalcModeRecord.class, CFHeaderRecord.class, CFHeader12Record.class, CFRuleRecord.class, CFRule12Record.class, ChartRecord.class, ChartTitleFormatRecord.class, CodepageRecord.class, ColumnInfoRecord.class, ContinueRecord.class, CountryRecord.class, CRNCountRecord.class, CRNRecord.class, DateWindow1904Record.class, DBCellRecord.class, DConRefRecord.class, DefaultColWidthRecord.class, DefaultRowHeightRecord.class, DeltaRecord.class, DimensionsRecord.class, DrawingGroupRecord.class, DrawingRecord.class, DrawingSelectionRecord.class, DSFRecord.class, DVALRecord.class, DVRecord.class, EOFRecord.class, ExtendedFormatRecord.class, ExternalNameRecord.class, ExternSheetRecord.class, ExtSSTRecord.class, FeatRecord.class, FeatHdrRecord.class, FilePassRecord.class, FileSharingRecord.class, FnGroupCountRecord.class, FontRecord.class, FooterRecord.class, FormatRecord.class, FormulaRecord.class, GridsetRecord.class, GutsRecord.class, HCenterRecord.class, HeaderRecord.class, HeaderFooterRecord.class, HideObjRecord.class, HorizontalPageBreakRecord.class, HyperlinkRecord.class, IndexRecord.class, InterfaceEndRecord.class, InterfaceHdrRecord.class, IterationRecord.class, LabelRecord.class, LabelSSTRecord.class, LeftMarginRecord.class, LegendRecord.class, MergeCellsRecord.class, MMSRecord.class, MulBlankRecord.class, MulRKRecord.class, NameRecord.class, NameCommentRecord.class, NoteRecord.class, NumberRecord.class, ObjectProtectRecord.class, ObjRecord.class, PaletteRecord.class, PaneRecord.class, PasswordRecord.class, PasswordRev4Record.class, PrecisionRecord.class, PrintGridlinesRecord.class, PrintHeadersRecord.class, PrintSetupRecord.class, ProtectionRev4Record.class, ProtectRecord.class, RecalcIdRecord.class, RefModeRecord.class, RefreshAllRecord.class, RightMarginRecord.class, RKRecord.class, RowRecord.class, SaveRecalcRecord.class, ScenarioProtectRecord.class, SelectionRecord.class, SeriesRecord.class, SeriesTextRecord.class, SharedFormulaRecord.class, SSTRecord.class, StringRecord.class, StyleRecord.class, SupBookRecord.class, TabIdRecord.class, TableRecord.class, TableStylesRecord.class, TextObjectRecord.class, TopMarginRecord.class, UncalcedRecord.class, UseSelFSRecord.class, UserSViewBegin.class, UserSViewEnd.class, ValueRangeRecord.class, VCenterRecord.class, VerticalPageBreakRecord.class, WindowOneRecord.class, WindowProtectRecord.class, WindowTwoRecord.class, WriteAccessRecord.class, WriteProtectRecord.class, WSBoolRecord.class, BeginRecord.class, ChartFRTInfoRecord.class, ChartStartBlockRecord.class, ChartEndBlockRecord.class, ChartStartObjectRecord.class, ChartEndObjectRecord.class, CatLabRecord.class, DataFormatRecord.class, EndRecord.class, LinkedDataRecord.class, SeriesToChartGroupRecord.class, DataItemRecord.class, ExtendedPivotTableViewFieldsRecord.class, PageItemRecord.class, StreamIDRecord.class, ViewDefinitionRecord.class, ViewFieldsRecord.class, ViewSourceRecord.class };
/* 142:292 */   private static final Map<Integer, I_RecordCreator> _recordCreatorsById = recordsToMap(recordClasses);
/* 143:    */   private static short[] _allKnownRecordSIDs;
/* 144:    */   
/* 145:    */   public static Class<? extends Record> getRecordClass(int sid)
/* 146:    */   {
/* 147:309 */     I_RecordCreator rc = (I_RecordCreator)_recordCreatorsById.get(Integer.valueOf(sid));
/* 148:310 */     if (rc == null) {
/* 149:311 */       return null;
/* 150:    */     }
/* 151:313 */     return rc.getRecordClass();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static Record[] createRecord(RecordInputStream in)
/* 155:    */   {
/* 156:324 */     Record record = createSingleRecord(in);
/* 157:325 */     if ((record instanceof DBCellRecord)) {
/* 158:327 */       return new Record[] { null };
/* 159:    */     }
/* 160:329 */     if ((record instanceof RKRecord)) {
/* 161:330 */       return new Record[] { convertToNumberRecord((RKRecord)record) };
/* 162:    */     }
/* 163:332 */     if ((record instanceof MulRKRecord)) {
/* 164:333 */       return convertRKRecords((MulRKRecord)record);
/* 165:    */     }
/* 166:335 */     return new Record[] { record };
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static Record createSingleRecord(RecordInputStream in)
/* 170:    */   {
/* 171:339 */     I_RecordCreator constructor = (I_RecordCreator)_recordCreatorsById.get(Integer.valueOf(in.getSid()));
/* 172:341 */     if (constructor == null) {
/* 173:342 */       return new UnknownRecord(in);
/* 174:    */     }
/* 175:345 */     return constructor.create(in);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static NumberRecord convertToNumberRecord(RKRecord rk)
/* 179:    */   {
/* 180:356 */     NumberRecord num = new NumberRecord();
/* 181:    */     
/* 182:358 */     num.setColumn(rk.getColumn());
/* 183:359 */     num.setRow(rk.getRow());
/* 184:360 */     num.setXFIndex(rk.getXFIndex());
/* 185:361 */     num.setValue(rk.getRKNumber());
/* 186:362 */     return num;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static NumberRecord[] convertRKRecords(MulRKRecord mrk)
/* 190:    */   {
/* 191:372 */     NumberRecord[] mulRecs = new NumberRecord[mrk.getNumColumns()];
/* 192:373 */     for (int k = 0; k < mrk.getNumColumns(); k++)
/* 193:    */     {
/* 194:374 */       NumberRecord nr = new NumberRecord();
/* 195:    */       
/* 196:376 */       nr.setColumn((short)(k + mrk.getFirstColumn()));
/* 197:377 */       nr.setRow(mrk.getRow());
/* 198:378 */       nr.setXFIndex(mrk.getXFAt(k));
/* 199:379 */       nr.setValue(mrk.getRKNumberAt(k));
/* 200:380 */       mulRecs[k] = nr;
/* 201:    */     }
/* 202:382 */     return mulRecs;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public static BlankRecord[] convertBlankRecords(MulBlankRecord mbk)
/* 206:    */   {
/* 207:392 */     BlankRecord[] mulRecs = new BlankRecord[mbk.getNumColumns()];
/* 208:393 */     for (int k = 0; k < mbk.getNumColumns(); k++)
/* 209:    */     {
/* 210:394 */       BlankRecord br = new BlankRecord();
/* 211:    */       
/* 212:396 */       br.setColumn((short)(k + mbk.getFirstColumn()));
/* 213:397 */       br.setRow(mbk.getRow());
/* 214:398 */       br.setXFIndex(mbk.getXFAt(k));
/* 215:399 */       mulRecs[k] = br;
/* 216:    */     }
/* 217:401 */     return mulRecs;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public static short[] getAllKnownRecordSIDs()
/* 221:    */   {
/* 222:408 */     if (_allKnownRecordSIDs == null)
/* 223:    */     {
/* 224:409 */       short[] results = new short[_recordCreatorsById.size()];
/* 225:410 */       int i = 0;
/* 226:412 */       for (Integer sid : _recordCreatorsById.keySet()) {
/* 227:413 */         results[(i++)] = sid.shortValue();
/* 228:    */       }
/* 229:415 */       Arrays.sort(results);
/* 230:416 */       _allKnownRecordSIDs = results;
/* 231:    */     }
/* 232:419 */     return (short[])_allKnownRecordSIDs.clone();
/* 233:    */   }
/* 234:    */   
/* 235:    */   private static Map<Integer, I_RecordCreator> recordsToMap(Class<? extends Record>[] records)
/* 236:    */   {
/* 237:428 */     Map<Integer, I_RecordCreator> result = new HashMap();
/* 238:429 */     Set<Class<?>> uniqueRecClasses = new HashSet(records.length * 3 / 2);
/* 239:431 */     for (Class<? extends Record> recClass : records)
/* 240:    */     {
/* 241:432 */       if (!Record.class.isAssignableFrom(recClass)) {
/* 242:433 */         throw new RuntimeException("Invalid record sub-class (" + recClass.getName() + ")");
/* 243:    */       }
/* 244:435 */       if (Modifier.isAbstract(recClass.getModifiers())) {
/* 245:436 */         throw new RuntimeException("Invalid record class (" + recClass.getName() + ") - must not be abstract");
/* 246:    */       }
/* 247:438 */       if (!uniqueRecClasses.add(recClass)) {
/* 248:439 */         throw new RuntimeException("duplicate record class (" + recClass.getName() + ")");
/* 249:    */       }
/* 250:    */       int sid;
/* 251:    */       try
/* 252:    */       {
/* 253:444 */         sid = recClass.getField("sid").getShort(null);
/* 254:    */       }
/* 255:    */       catch (Exception illegalArgumentException)
/* 256:    */       {
/* 257:446 */         throw new RecordFormatException("Unable to determine record types");
/* 258:    */       }
/* 259:449 */       Integer key = Integer.valueOf(sid);
/* 260:450 */       if (result.containsKey(key))
/* 261:    */       {
/* 262:451 */         Class<?> prevClass = ((I_RecordCreator)result.get(key)).getRecordClass();
/* 263:452 */         throw new RuntimeException("duplicate record sid 0x" + Integer.toHexString(sid).toUpperCase(Locale.ROOT) + " for classes (" + recClass.getName() + ") and (" + prevClass.getName() + ")");
/* 264:    */       }
/* 265:457 */       result.put(key, getRecordCreator(recClass));
/* 266:    */     }
/* 267:460 */     return result;
/* 268:    */   }
/* 269:    */   
/* 270:    */   private static I_RecordCreator getRecordCreator(Class<? extends Record> recClass)
/* 271:    */   {
/* 272:    */     try
/* 273:    */     {
/* 274:466 */       Constructor<? extends Record> constructor = recClass.getConstructor(CONSTRUCTOR_ARGS);
/* 275:467 */       return new ReflectionConstructorRecordCreator(constructor);
/* 276:    */     }
/* 277:    */     catch (NoSuchMethodException e)
/* 278:    */     {
/* 279:    */       try
/* 280:    */       {
/* 281:472 */         Method m = recClass.getDeclaredMethod("create", CONSTRUCTOR_ARGS);
/* 282:473 */         return new ReflectionMethodRecordCreator(m);
/* 283:    */       }
/* 284:    */       catch (NoSuchMethodException e)
/* 285:    */       {
/* 286:475 */         throw new RuntimeException("Failed to find constructor or create method for (" + recClass.getName() + ").");
/* 287:    */       }
/* 288:    */     }
/* 289:    */   }
/* 290:    */   
/* 291:    */   public static List<Record> createRecords(InputStream in)
/* 292:    */     throws RecordFormatException
/* 293:    */   {
/* 294:489 */     List<Record> records = new ArrayList(512);
/* 295:    */     
/* 296:491 */     RecordFactoryInputStream recStream = new RecordFactoryInputStream(in, true);
/* 297:    */     Record record;
/* 298:494 */     while ((record = recStream.nextRecord()) != null) {
/* 299:495 */       records.add(record);
/* 300:    */     }
/* 301:498 */     return records;
/* 302:    */   }
/* 303:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.RecordFactory

 * JD-Core Version:    0.7.0.1

 */