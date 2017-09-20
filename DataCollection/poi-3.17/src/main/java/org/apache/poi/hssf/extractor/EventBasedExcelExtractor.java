/*   1:    */ package org.apache.poi.hssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.POIDocument;
/*   7:    */ import org.apache.poi.POIOLE2TextExtractor;
/*   8:    */ import org.apache.poi.hpsf.DocumentSummaryInformation;
/*   9:    */ import org.apache.poi.hpsf.SummaryInformation;
/*  10:    */ import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
/*  11:    */ import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
/*  12:    */ import org.apache.poi.hssf.eventusermodel.HSSFListener;
/*  13:    */ import org.apache.poi.hssf.eventusermodel.HSSFRequest;
/*  14:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*  15:    */ import org.apache.poi.hssf.record.BOFRecord;
/*  16:    */ import org.apache.poi.hssf.record.BoundSheetRecord;
/*  17:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  18:    */ import org.apache.poi.hssf.record.LabelRecord;
/*  19:    */ import org.apache.poi.hssf.record.LabelSSTRecord;
/*  20:    */ import org.apache.poi.hssf.record.NoteRecord;
/*  21:    */ import org.apache.poi.hssf.record.NumberRecord;
/*  22:    */ import org.apache.poi.hssf.record.Record;
/*  23:    */ import org.apache.poi.hssf.record.SSTRecord;
/*  24:    */ import org.apache.poi.hssf.record.StringRecord;
/*  25:    */ import org.apache.poi.hssf.record.common.UnicodeString;
/*  26:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  27:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  28:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  29:    */ import org.apache.poi.ss.extractor.ExcelExtractor;
/*  30:    */ 
/*  31:    */ public class EventBasedExcelExtractor
/*  32:    */   extends POIOLE2TextExtractor
/*  33:    */   implements ExcelExtractor
/*  34:    */ {
/*  35:    */   private DirectoryNode _dir;
/*  36: 65 */   boolean _includeSheetNames = true;
/*  37: 66 */   boolean _formulasNotResults = false;
/*  38:    */   
/*  39:    */   public EventBasedExcelExtractor(DirectoryNode dir)
/*  40:    */   {
/*  41: 70 */     super((POIDocument)null);
/*  42: 71 */     this._dir = dir;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public EventBasedExcelExtractor(POIFSFileSystem fs)
/*  46:    */   {
/*  47: 75 */     this(fs.getRoot());
/*  48: 76 */     super.setFilesystem(fs);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public DocumentSummaryInformation getDocSummaryInformation()
/*  52:    */   {
/*  53: 84 */     throw new IllegalStateException("Metadata extraction not supported in streaming mode, please use ExcelExtractor");
/*  54:    */   }
/*  55:    */   
/*  56:    */   public SummaryInformation getSummaryInformation()
/*  57:    */   {
/*  58: 91 */     throw new IllegalStateException("Metadata extraction not supported in streaming mode, please use ExcelExtractor");
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setIncludeCellComments(boolean includeComments)
/*  62:    */   {
/*  63:100 */     throw new IllegalStateException("Comment extraction not supported in streaming mode, please use ExcelExtractor");
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setIncludeHeadersFooters(boolean includeHeadersFooters)
/*  67:    */   {
/*  68:108 */     throw new IllegalStateException("Header/Footer extraction not supported in streaming mode, please use ExcelExtractor");
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setIncludeSheetNames(boolean includeSheetNames)
/*  72:    */   {
/*  73:116 */     this._includeSheetNames = includeSheetNames;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setFormulasNotResults(boolean formulasNotResults)
/*  77:    */   {
/*  78:123 */     this._formulasNotResults = formulasNotResults;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getText()
/*  82:    */   {
/*  83:131 */     String text = null;
/*  84:    */     try
/*  85:    */     {
/*  86:133 */       TextListener tl = triggerExtraction();
/*  87:    */       
/*  88:135 */       text = tl._text.toString();
/*  89:136 */       if (!text.endsWith("\n")) {
/*  90:137 */         text = text + "\n";
/*  91:    */       }
/*  92:    */     }
/*  93:    */     catch (IOException e)
/*  94:    */     {
/*  95:140 */       throw new RuntimeException(e);
/*  96:    */     }
/*  97:143 */     return text;
/*  98:    */   }
/*  99:    */   
/* 100:    */   private TextListener triggerExtraction()
/* 101:    */     throws IOException
/* 102:    */   {
/* 103:147 */     TextListener tl = new TextListener();
/* 104:148 */     FormatTrackingHSSFListener ft = new FormatTrackingHSSFListener(tl);
/* 105:149 */     tl._ft = ft;
/* 106:    */     
/* 107:    */ 
/* 108:152 */     HSSFEventFactory factory = new HSSFEventFactory();
/* 109:153 */     HSSFRequest request = new HSSFRequest();
/* 110:154 */     request.addListenerForAllRecords(ft);
/* 111:    */     
/* 112:156 */     factory.processWorkbookEvents(request, this._dir);
/* 113:    */     
/* 114:158 */     return tl;
/* 115:    */   }
/* 116:    */   
/* 117:    */   private class TextListener
/* 118:    */     implements HSSFListener
/* 119:    */   {
/* 120:    */     FormatTrackingHSSFListener _ft;
/* 121:    */     private SSTRecord sstRecord;
/* 122:    */     private final List<String> sheetNames;
/* 123:166 */     final StringBuffer _text = new StringBuffer();
/* 124:167 */     private int sheetNum = -1;
/* 125:    */     private int rowNum;
/* 126:170 */     private boolean outputNextStringValue = false;
/* 127:171 */     private int nextRow = -1;
/* 128:    */     
/* 129:    */     public TextListener()
/* 130:    */     {
/* 131:174 */       this.sheetNames = new ArrayList();
/* 132:    */     }
/* 133:    */     
/* 134:    */     public void processRecord(Record record)
/* 135:    */     {
/* 136:177 */       String thisText = null;
/* 137:178 */       int thisRow = -1;
/* 138:180 */       switch (record.getSid())
/* 139:    */       {
/* 140:    */       case 133: 
/* 141:182 */         BoundSheetRecord sr = (BoundSheetRecord)record;
/* 142:183 */         this.sheetNames.add(sr.getSheetname());
/* 143:184 */         break;
/* 144:    */       case 2057: 
/* 145:186 */         BOFRecord bof = (BOFRecord)record;
/* 146:187 */         if (bof.getType() == 16)
/* 147:    */         {
/* 148:188 */           this.sheetNum += 1;
/* 149:189 */           this.rowNum = -1;
/* 150:191 */           if (EventBasedExcelExtractor.this._includeSheetNames)
/* 151:    */           {
/* 152:192 */             if (this._text.length() > 0) {
/* 153:192 */               this._text.append("\n");
/* 154:    */             }
/* 155:193 */             this._text.append((String)this.sheetNames.get(this.sheetNum));
/* 156:    */           }
/* 157:    */         }
/* 158:    */         break;
/* 159:    */       case 252: 
/* 160:198 */         this.sstRecord = ((SSTRecord)record);
/* 161:199 */         break;
/* 162:    */       case 6: 
/* 163:202 */         FormulaRecord frec = (FormulaRecord)record;
/* 164:203 */         thisRow = frec.getRow();
/* 165:205 */         if (EventBasedExcelExtractor.this._formulasNotResults)
/* 166:    */         {
/* 167:206 */           thisText = HSSFFormulaParser.toFormulaString((HSSFWorkbook)null, frec.getParsedExpression());
/* 168:    */         }
/* 169:208 */         else if (frec.hasCachedResultString())
/* 170:    */         {
/* 171:211 */           this.outputNextStringValue = true;
/* 172:212 */           this.nextRow = frec.getRow();
/* 173:    */         }
/* 174:    */         else
/* 175:    */         {
/* 176:214 */           thisText = this._ft.formatNumberDateCell(frec);
/* 177:    */         }
/* 178:217 */         break;
/* 179:    */       case 519: 
/* 180:219 */         if (this.outputNextStringValue)
/* 181:    */         {
/* 182:221 */           StringRecord srec = (StringRecord)record;
/* 183:222 */           thisText = srec.getString();
/* 184:223 */           thisRow = this.nextRow;
/* 185:224 */           this.outputNextStringValue = false;
/* 186:    */         }
/* 187:225 */         break;
/* 188:    */       case 516: 
/* 189:228 */         LabelRecord lrec = (LabelRecord)record;
/* 190:229 */         thisRow = lrec.getRow();
/* 191:230 */         thisText = lrec.getValue();
/* 192:231 */         break;
/* 193:    */       case 253: 
/* 194:233 */         LabelSSTRecord lsrec = (LabelSSTRecord)record;
/* 195:234 */         thisRow = lsrec.getRow();
/* 196:235 */         if (this.sstRecord == null) {
/* 197:236 */           throw new IllegalStateException("No SST record found");
/* 198:    */         }
/* 199:238 */         thisText = this.sstRecord.getString(lsrec.getSSTIndex()).toString();
/* 200:239 */         break;
/* 201:    */       case 28: 
/* 202:241 */         NoteRecord nrec = (NoteRecord)record;
/* 203:242 */         thisRow = nrec.getRow();
/* 204:    */         
/* 205:244 */         break;
/* 206:    */       case 515: 
/* 207:246 */         NumberRecord numrec = (NumberRecord)record;
/* 208:247 */         thisRow = numrec.getRow();
/* 209:248 */         thisText = this._ft.formatNumberDateCell(numrec);
/* 210:249 */         break;
/* 211:    */       }
/* 212:254 */       if (thisText != null)
/* 213:    */       {
/* 214:255 */         if (thisRow != this.rowNum)
/* 215:    */         {
/* 216:256 */           this.rowNum = thisRow;
/* 217:257 */           if (this._text.length() > 0) {
/* 218:258 */             this._text.append("\n");
/* 219:    */           }
/* 220:    */         }
/* 221:    */         else
/* 222:    */         {
/* 223:260 */           this._text.append("\t");
/* 224:    */         }
/* 225:262 */         this._text.append(thisText);
/* 226:    */       }
/* 227:    */     }
/* 228:    */   }
/* 229:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.extractor.EventBasedExcelExtractor
 * JD-Core Version:    0.7.0.1
 */