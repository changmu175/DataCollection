/*   1:    */ package org.apache.poi.hssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.apache.poi.hssf.record.CellValueRecordInterface;
/*  10:    */ import org.apache.poi.hssf.record.ExtendedFormatRecord;
/*  11:    */ import org.apache.poi.hssf.record.FormatRecord;
/*  12:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  13:    */ import org.apache.poi.hssf.record.NumberRecord;
/*  14:    */ import org.apache.poi.hssf.record.Record;
/*  15:    */ import org.apache.poi.hssf.usermodel.HSSFDataFormat;
/*  16:    */ import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
/*  17:    */ import org.apache.poi.util.LocaleUtil;
/*  18:    */ import org.apache.poi.util.POILogFactory;
/*  19:    */ import org.apache.poi.util.POILogger;
/*  20:    */ 
/*  21:    */ public class FormatTrackingHSSFListener
/*  22:    */   implements HSSFListener
/*  23:    */ {
/*  24: 44 */   private static final POILogger logger = POILogFactory.getLogger(FormatTrackingHSSFListener.class);
/*  25:    */   private final HSSFListener _childListener;
/*  26:    */   private final HSSFDataFormatter _formatter;
/*  27:    */   private final NumberFormat _defaultFormat;
/*  28: 48 */   private final Map<Integer, FormatRecord> _customFormatRecords = new HashMap();
/*  29: 49 */   private final List<ExtendedFormatRecord> _xfRecords = new ArrayList();
/*  30:    */   
/*  31:    */   public FormatTrackingHSSFListener(HSSFListener childListener)
/*  32:    */   {
/*  33: 58 */     this(childListener, LocaleUtil.getUserLocale());
/*  34:    */   }
/*  35:    */   
/*  36:    */   public FormatTrackingHSSFListener(HSSFListener childListener, Locale locale)
/*  37:    */   {
/*  38: 70 */     this._childListener = childListener;
/*  39: 71 */     this._formatter = new HSSFDataFormatter(locale);
/*  40: 72 */     this._defaultFormat = NumberFormat.getInstance(locale);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected int getNumberOfCustomFormats()
/*  44:    */   {
/*  45: 76 */     return this._customFormatRecords.size();
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected int getNumberOfExtendedFormats()
/*  49:    */   {
/*  50: 80 */     return this._xfRecords.size();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void processRecord(Record record)
/*  54:    */   {
/*  55: 89 */     processRecordInternally(record);
/*  56:    */     
/*  57:    */ 
/*  58: 92 */     this._childListener.processRecord(record);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void processRecordInternally(Record record)
/*  62:    */   {
/*  63:102 */     if ((record instanceof FormatRecord))
/*  64:    */     {
/*  65:103 */       FormatRecord fr = (FormatRecord)record;
/*  66:104 */       this._customFormatRecords.put(Integer.valueOf(fr.getIndexCode()), fr);
/*  67:    */     }
/*  68:106 */     if ((record instanceof ExtendedFormatRecord))
/*  69:    */     {
/*  70:107 */       ExtendedFormatRecord xr = (ExtendedFormatRecord)record;
/*  71:108 */       this._xfRecords.add(xr);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String formatNumberDateCell(CellValueRecordInterface cell)
/*  76:    */   {
/*  77:    */     double value;
/*  78:126 */     if ((cell instanceof NumberRecord))
/*  79:    */     {
/*  80:127 */       value = ((NumberRecord)cell).getValue();
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84:    */       double value;
/*  85:128 */       if ((cell instanceof FormulaRecord)) {
/*  86:129 */         value = ((FormulaRecord)cell).getValue();
/*  87:    */       } else {
/*  88:131 */         throw new IllegalArgumentException("Unsupported CellValue Record passed in " + cell);
/*  89:    */       }
/*  90:    */     }
/*  91:    */     double value;
/*  92:135 */     int formatIndex = getFormatIndex(cell);
/*  93:136 */     String formatString = getFormatString(cell);
/*  94:138 */     if (formatString == null) {
/*  95:139 */       return this._defaultFormat.format(value);
/*  96:    */     }
/*  97:143 */     return this._formatter.formatRawCellContents(value, formatIndex, formatString);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getFormatString(int formatIndex)
/* 101:    */   {
/* 102:154 */     String format = null;
/* 103:155 */     if (formatIndex >= HSSFDataFormat.getNumberOfBuiltinBuiltinFormats())
/* 104:    */     {
/* 105:156 */       FormatRecord tfr = (FormatRecord)this._customFormatRecords.get(Integer.valueOf(formatIndex));
/* 106:157 */       if (tfr == null) {
/* 107:158 */         logger.log(7, new Object[] { "Requested format at index " + formatIndex + ", but it wasn't found" });
/* 108:    */       } else {
/* 109:161 */         format = tfr.getFormatString();
/* 110:    */       }
/* 111:    */     }
/* 112:    */     else
/* 113:    */     {
/* 114:164 */       format = HSSFDataFormat.getBuiltinFormat((short)formatIndex);
/* 115:    */     }
/* 116:166 */     return format;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String getFormatString(CellValueRecordInterface cell)
/* 120:    */   {
/* 121:177 */     int formatIndex = getFormatIndex(cell);
/* 122:178 */     if (formatIndex == -1) {
/* 123:180 */       return null;
/* 124:    */     }
/* 125:182 */     return getFormatString(formatIndex);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public int getFormatIndex(CellValueRecordInterface cell)
/* 129:    */   {
/* 130:193 */     ExtendedFormatRecord xfr = (ExtendedFormatRecord)this._xfRecords.get(cell.getXFIndex());
/* 131:194 */     if (xfr == null)
/* 132:    */     {
/* 133:195 */       logger.log(7, new Object[] { "Cell " + cell.getRow() + "," + cell.getColumn() + " uses XF with index " + cell.getXFIndex() + ", but we don't have that" });
/* 134:    */       
/* 135:197 */       return -1;
/* 136:    */     }
/* 137:199 */     return xfr.getFormatIndex();
/* 138:    */   }
/* 139:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener
 * JD-Core Version:    0.7.0.1
 */