/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.security.GeneralSecurityException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.EncryptedDocumentException;
/*   8:    */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*   9:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  10:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ 
/*  13:    */ public final class RecordFactoryInputStream
/*  14:    */ {
/*  15:    */   private final RecordInputStream _recStream;
/*  16:    */   private final boolean _shouldIncludeContinueRecords;
/*  17:    */   private Record[] _unreadRecordBuffer;
/*  18:    */   
/*  19:    */   private static final class StreamEncryptionInfo
/*  20:    */   {
/*  21:    */     private final int _initialRecordsSize;
/*  22:    */     private final FilePassRecord _filePassRec;
/*  23:    */     private final Record _lastRecord;
/*  24:    */     private final boolean _hasBOFRecord;
/*  25:    */     
/*  26:    */     public StreamEncryptionInfo(RecordInputStream rs, List<Record> outputRecs)
/*  27:    */     {
/*  28: 57 */       rs.nextRecord();
/*  29: 58 */       int recSize = 4 + rs.remaining();
/*  30: 59 */       Record rec = RecordFactory.createSingleRecord(rs);
/*  31: 60 */       outputRecs.add(rec);
/*  32: 61 */       FilePassRecord fpr = null;
/*  33: 62 */       if ((rec instanceof BOFRecord))
/*  34:    */       {
/*  35: 63 */         this._hasBOFRecord = true;
/*  36: 67 */         if (rs.hasNextRecord())
/*  37:    */         {
/*  38: 68 */           rs.nextRecord();
/*  39: 69 */           rec = RecordFactory.createSingleRecord(rs);
/*  40: 70 */           recSize += rec.getRecordSize();
/*  41: 71 */           outputRecs.add(rec);
/*  42: 75 */           if (((rec instanceof WriteProtectRecord)) && (rs.hasNextRecord()))
/*  43:    */           {
/*  44: 76 */             rs.nextRecord();
/*  45: 77 */             rec = RecordFactory.createSingleRecord(rs);
/*  46: 78 */             recSize += rec.getRecordSize();
/*  47: 79 */             outputRecs.add(rec);
/*  48:    */           }
/*  49: 83 */           if ((rec instanceof FilePassRecord)) {
/*  50: 84 */             fpr = (FilePassRecord)rec;
/*  51:    */           }
/*  52: 88 */           if ((rec instanceof EOFRecord)) {
/*  53: 91 */             throw new IllegalStateException("Nothing between BOF and EOF");
/*  54:    */           }
/*  55:    */         }
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59: 98 */         this._hasBOFRecord = false;
/*  60:    */       }
/*  61:100 */       this._initialRecordsSize = recSize;
/*  62:101 */       this._filePassRec = fpr;
/*  63:102 */       this._lastRecord = rec;
/*  64:    */     }
/*  65:    */     
/*  66:    */     public RecordInputStream createDecryptingStream(InputStream original)
/*  67:    */     {
/*  68:106 */       FilePassRecord fpr = this._filePassRec;
/*  69:107 */       String userPassword = Biff8EncryptionKey.getCurrentUserPassword();
/*  70:108 */       if (userPassword == null) {
/*  71:109 */         userPassword = "VelvetSweatshop";
/*  72:    */       }
/*  73:112 */       EncryptionInfo info = fpr.getEncryptionInfo();
/*  74:    */       try
/*  75:    */       {
/*  76:114 */         if (!info.getDecryptor().verifyPassword(userPassword)) {
/*  77:115 */           throw new EncryptedDocumentException(("VelvetSweatshop".equals(userPassword) ? "Default" : "Supplied") + " password is invalid for salt/verifier/verifierHash");
/*  78:    */         }
/*  79:    */       }
/*  80:    */       catch (GeneralSecurityException e)
/*  81:    */       {
/*  82:120 */         throw new EncryptedDocumentException(e);
/*  83:    */       }
/*  84:123 */       return new RecordInputStream(original, info, this._initialRecordsSize);
/*  85:    */     }
/*  86:    */     
/*  87:    */     public boolean hasEncryption()
/*  88:    */     {
/*  89:127 */       return this._filePassRec != null;
/*  90:    */     }
/*  91:    */     
/*  92:    */     public Record getLastRecord()
/*  93:    */     {
/*  94:136 */       return this._lastRecord;
/*  95:    */     }
/*  96:    */     
/*  97:    */     public boolean hasBOFRecord()
/*  98:    */     {
/*  99:143 */       return this._hasBOFRecord;
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:161 */   private int _unreadRecordIndex = -1;
/* 104:166 */   private Record _lastRecord = null;
/* 105:170 */   private DrawingRecord _lastDrawingRecord = new DrawingRecord();
/* 106:    */   private int _bofDepth;
/* 107:    */   private boolean _lastRecordWasEOFLevelZero;
/* 108:    */   
/* 109:    */   public RecordFactoryInputStream(InputStream in, boolean shouldIncludeContinueRecords)
/* 110:    */   {
/* 111:185 */     RecordInputStream rs = new RecordInputStream(in);
/* 112:186 */     List<Record> records = new ArrayList();
/* 113:187 */     StreamEncryptionInfo sei = new StreamEncryptionInfo(rs, records);
/* 114:188 */     if (sei.hasEncryption()) {
/* 115:189 */       rs = sei.createDecryptingStream(in);
/* 116:    */     }
/* 117:194 */     if (!records.isEmpty())
/* 118:    */     {
/* 119:195 */       this._unreadRecordBuffer = new Record[records.size()];
/* 120:196 */       records.toArray(this._unreadRecordBuffer);
/* 121:197 */       this._unreadRecordIndex = 0;
/* 122:    */     }
/* 123:199 */     this._recStream = rs;
/* 124:200 */     this._shouldIncludeContinueRecords = shouldIncludeContinueRecords;
/* 125:201 */     this._lastRecord = sei.getLastRecord();
/* 126:    */     
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:220 */     this._bofDepth = (sei.hasBOFRecord() ? 1 : 0);
/* 145:221 */     this._lastRecordWasEOFLevelZero = false;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public Record nextRecord()
/* 149:    */   {
/* 150:229 */     Record r = getNextUnreadRecord();
/* 151:230 */     if (r != null) {
/* 152:232 */       return r;
/* 153:    */     }
/* 154:    */     do
/* 155:    */     {
/* 156:235 */       if (!this._recStream.hasNextRecord()) {
/* 157:237 */         return null;
/* 158:    */       }
/* 159:240 */       if (this._lastRecordWasEOFLevelZero) {
/* 160:246 */         if (this._recStream.getNextSid() != 2057) {
/* 161:247 */           return null;
/* 162:    */         }
/* 163:    */       }
/* 164:253 */       this._recStream.nextRecord();
/* 165:    */       
/* 166:255 */       r = readNextRecord();
/* 167:256 */     } while (r == null);
/* 168:260 */     return r;
/* 169:    */   }
/* 170:    */   
/* 171:    */   private Record getNextUnreadRecord()
/* 172:    */   {
/* 173:269 */     if (this._unreadRecordBuffer != null)
/* 174:    */     {
/* 175:270 */       int ix = this._unreadRecordIndex;
/* 176:271 */       if (ix < this._unreadRecordBuffer.length)
/* 177:    */       {
/* 178:272 */         Record result = this._unreadRecordBuffer[ix];
/* 179:273 */         this._unreadRecordIndex = (ix + 1);
/* 180:274 */         return result;
/* 181:    */       }
/* 182:276 */       this._unreadRecordIndex = -1;
/* 183:277 */       this._unreadRecordBuffer = null;
/* 184:    */     }
/* 185:279 */     return null;
/* 186:    */   }
/* 187:    */   
/* 188:    */   private Record readNextRecord()
/* 189:    */   {
/* 190:289 */     Record record = RecordFactory.createSingleRecord(this._recStream);
/* 191:290 */     this._lastRecordWasEOFLevelZero = false;
/* 192:292 */     if ((record instanceof BOFRecord))
/* 193:    */     {
/* 194:293 */       this._bofDepth += 1;
/* 195:294 */       return record;
/* 196:    */     }
/* 197:297 */     if ((record instanceof EOFRecord))
/* 198:    */     {
/* 199:298 */       this._bofDepth -= 1;
/* 200:299 */       if (this._bofDepth < 1) {
/* 201:300 */         this._lastRecordWasEOFLevelZero = true;
/* 202:    */       }
/* 203:303 */       return record;
/* 204:    */     }
/* 205:306 */     if ((record instanceof DBCellRecord)) {
/* 206:308 */       return null;
/* 207:    */     }
/* 208:311 */     if ((record instanceof RKRecord)) {
/* 209:312 */       return RecordFactory.convertToNumberRecord((RKRecord)record);
/* 210:    */     }
/* 211:315 */     if ((record instanceof MulRKRecord))
/* 212:    */     {
/* 213:316 */       Record[] records = RecordFactory.convertRKRecords((MulRKRecord)record);
/* 214:    */       
/* 215:318 */       this._unreadRecordBuffer = records;
/* 216:319 */       this._unreadRecordIndex = 1;
/* 217:320 */       return records[0];
/* 218:    */     }
/* 219:323 */     if ((record.getSid() == 235) && ((this._lastRecord instanceof DrawingGroupRecord)))
/* 220:    */     {
/* 221:325 */       DrawingGroupRecord lastDGRecord = (DrawingGroupRecord)this._lastRecord;
/* 222:326 */       lastDGRecord.join((AbstractEscherHolderRecord)record);
/* 223:327 */       return null;
/* 224:    */     }
/* 225:329 */     if (record.getSid() == 60)
/* 226:    */     {
/* 227:330 */       ContinueRecord contRec = (ContinueRecord)record;
/* 228:332 */       if (((this._lastRecord instanceof ObjRecord)) || ((this._lastRecord instanceof TextObjectRecord)))
/* 229:    */       {
/* 230:335 */         this._lastDrawingRecord.processContinueRecord(contRec.getData());
/* 231:338 */         if (this._shouldIncludeContinueRecords) {
/* 232:339 */           return record;
/* 233:    */         }
/* 234:341 */         return null;
/* 235:    */       }
/* 236:343 */       if ((this._lastRecord instanceof DrawingGroupRecord))
/* 237:    */       {
/* 238:344 */         ((DrawingGroupRecord)this._lastRecord).processContinueRecord(contRec.getData());
/* 239:345 */         return null;
/* 240:    */       }
/* 241:347 */       if ((this._lastRecord instanceof DrawingRecord)) {
/* 242:349 */         return contRec;
/* 243:    */       }
/* 244:351 */       if ((this._lastRecord instanceof UnknownRecord)) {
/* 245:354 */         return record;
/* 246:    */       }
/* 247:356 */       if ((this._lastRecord instanceof EOFRecord)) {
/* 248:359 */         return record;
/* 249:    */       }
/* 250:361 */       throw new RecordFormatException("Unhandled Continue Record followining " + this._lastRecord.getClass());
/* 251:    */     }
/* 252:363 */     this._lastRecord = record;
/* 253:364 */     if ((record instanceof DrawingRecord)) {
/* 254:365 */       this._lastDrawingRecord = ((DrawingRecord)record);
/* 255:    */     }
/* 256:367 */     return record;
/* 257:    */   }
/* 258:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RecordFactoryInputStream
 * JD-Core Version:    0.7.0.1
 */