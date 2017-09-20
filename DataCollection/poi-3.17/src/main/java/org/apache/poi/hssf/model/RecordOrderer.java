/*   1:    */ package org.apache.poi.hssf.model;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.poi.hssf.record.DimensionsRecord;
/*   5:    */ import org.apache.poi.hssf.record.EOFRecord;
/*   6:    */ import org.apache.poi.hssf.record.GutsRecord;
/*   7:    */ import org.apache.poi.hssf.record.Record;
/*   8:    */ import org.apache.poi.hssf.record.RecordBase;
/*   9:    */ import org.apache.poi.hssf.record.aggregates.ColumnInfoRecordsAggregate;
/*  10:    */ import org.apache.poi.hssf.record.aggregates.ConditionalFormattingTable;
/*  11:    */ import org.apache.poi.hssf.record.aggregates.DataValidityTable;
/*  12:    */ import org.apache.poi.hssf.record.aggregates.MergedCellsTable;
/*  13:    */ import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
/*  14:    */ import org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock;
/*  15:    */ 
/*  16:    */ final class RecordOrderer
/*  17:    */ {
/*  18:    */   public static void addNewSheetRecord(List<RecordBase> sheetRecords, RecordBase newRecord)
/*  19:    */   {
/*  20: 92 */     int index = findSheetInsertPos(sheetRecords, newRecord.getClass());
/*  21: 93 */     sheetRecords.add(index, newRecord);
/*  22:    */   }
/*  23:    */   
/*  24:    */   private static int findSheetInsertPos(List<RecordBase> records, Class<? extends RecordBase> recClass)
/*  25:    */   {
/*  26: 97 */     if (recClass == DataValidityTable.class) {
/*  27: 98 */       return findDataValidationTableInsertPos(records);
/*  28:    */     }
/*  29:100 */     if (recClass == MergedCellsTable.class) {
/*  30:101 */       return findInsertPosForNewMergedRecordTable(records);
/*  31:    */     }
/*  32:103 */     if (recClass == ConditionalFormattingTable.class) {
/*  33:104 */       return findInsertPosForNewCondFormatTable(records);
/*  34:    */     }
/*  35:106 */     if (recClass == GutsRecord.class) {
/*  36:107 */       return getGutsRecordInsertPos(records);
/*  37:    */     }
/*  38:109 */     if (recClass == PageSettingsBlock.class) {
/*  39:110 */       return getPageBreakRecordInsertPos(records);
/*  40:    */     }
/*  41:112 */     if (recClass == WorksheetProtectionBlock.class) {
/*  42:113 */       return getWorksheetProtectionBlockInsertPos(records);
/*  43:    */     }
/*  44:115 */     throw new RuntimeException("Unexpected record class (" + recClass.getName() + ")");
/*  45:    */   }
/*  46:    */   
/*  47:    */   private static int getWorksheetProtectionBlockInsertPos(List<RecordBase> records)
/*  48:    */   {
/*  49:140 */     int i = getDimensionsIndex(records);
/*  50:141 */     while (i > 0)
/*  51:    */     {
/*  52:142 */       i--;
/*  53:143 */       Object rb = records.get(i);
/*  54:144 */       if (!isProtectionSubsequentRecord(rb)) {
/*  55:145 */         return i + 1;
/*  56:    */       }
/*  57:    */     }
/*  58:148 */     throw new IllegalStateException("did not find insert pos for protection block");
/*  59:    */   }
/*  60:    */   
/*  61:    */   private static boolean isProtectionSubsequentRecord(Object rb)
/*  62:    */   {
/*  63:161 */     if ((rb instanceof ColumnInfoRecordsAggregate)) {
/*  64:162 */       return true;
/*  65:    */     }
/*  66:164 */     if ((rb instanceof Record))
/*  67:    */     {
/*  68:165 */       Record record = (Record)rb;
/*  69:166 */       switch (record.getSid())
/*  70:    */       {
/*  71:    */       case 85: 
/*  72:    */       case 144: 
/*  73:169 */         return true;
/*  74:    */       }
/*  75:    */     }
/*  76:172 */     return false;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private static int getPageBreakRecordInsertPos(List<RecordBase> records)
/*  80:    */   {
/*  81:176 */     int dimensionsIndex = getDimensionsIndex(records);
/*  82:177 */     int i = dimensionsIndex - 1;
/*  83:178 */     while (i > 0)
/*  84:    */     {
/*  85:179 */       i--;
/*  86:180 */       Object rb = records.get(i);
/*  87:181 */       if (isPageBreakPriorRecord(rb)) {
/*  88:182 */         return i + 1;
/*  89:    */       }
/*  90:    */     }
/*  91:185 */     throw new RuntimeException("Did not find insert point for GUTS");
/*  92:    */   }
/*  93:    */   
/*  94:    */   private static boolean isPageBreakPriorRecord(Object rb)
/*  95:    */   {
/*  96:188 */     if ((rb instanceof Record))
/*  97:    */     {
/*  98:189 */       Record record = (Record)rb;
/*  99:190 */       switch (record.getSid())
/* 100:    */       {
/* 101:    */       case 12: 
/* 102:    */       case 13: 
/* 103:    */       case 14: 
/* 104:    */       case 15: 
/* 105:    */       case 16: 
/* 106:    */       case 17: 
/* 107:    */       case 34: 
/* 108:    */       case 42: 
/* 109:    */       case 43: 
/* 110:    */       case 94: 
/* 111:    */       case 95: 
/* 112:    */       case 129: 
/* 113:    */       case 130: 
/* 114:    */       case 523: 
/* 115:    */       case 549: 
/* 116:    */       case 2057: 
/* 117:209 */         return true;
/* 118:    */       }
/* 119:    */     }
/* 120:213 */     return false;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static int findInsertPosForNewCondFormatTable(List<RecordBase> records)
/* 124:    */   {
/* 125:220 */     for (int i = records.size() - 2; i >= 0; i--)
/* 126:    */     {
/* 127:221 */       Object rb = records.get(i);
/* 128:222 */       if ((rb instanceof MergedCellsTable)) {
/* 129:223 */         return i + 1;
/* 130:    */       }
/* 131:225 */       if (!(rb instanceof DataValidityTable))
/* 132:    */       {
/* 133:229 */         Record rec = (Record)rb;
/* 134:230 */         switch (rec.getSid())
/* 135:    */         {
/* 136:    */         case 29: 
/* 137:    */         case 65: 
/* 138:    */         case 153: 
/* 139:    */         case 160: 
/* 140:    */         case 239: 
/* 141:    */         case 351: 
/* 142:    */         case 574: 
/* 143:240 */           return i + 1;
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:245 */     throw new RuntimeException("Did not find Window2 record");
/* 148:    */   }
/* 149:    */   
/* 150:    */   private static int findInsertPosForNewMergedRecordTable(List<RecordBase> records)
/* 151:    */   {
/* 152:249 */     for (int i = records.size() - 2; i >= 0; i--)
/* 153:    */     {
/* 154:250 */       Object rb = records.get(i);
/* 155:251 */       if ((rb instanceof Record))
/* 156:    */       {
/* 157:256 */         Record rec = (Record)rb;
/* 158:257 */         switch (rec.getSid())
/* 159:    */         {
/* 160:    */         case 29: 
/* 161:    */         case 65: 
/* 162:    */         case 153: 
/* 163:    */         case 160: 
/* 164:    */         case 574: 
/* 165:265 */           return i + 1;
/* 166:    */         }
/* 167:    */       }
/* 168:    */     }
/* 169:268 */     throw new RuntimeException("Did not find Window2 record");
/* 170:    */   }
/* 171:    */   
/* 172:    */   private static int findDataValidationTableInsertPos(List<RecordBase> records)
/* 173:    */   {
/* 174:293 */     int i = records.size() - 1;
/* 175:294 */     if (!(records.get(i) instanceof EOFRecord)) {
/* 176:295 */       throw new IllegalStateException("Last sheet record should be EOFRecord");
/* 177:    */     }
/* 178:297 */     while (i > 0)
/* 179:    */     {
/* 180:298 */       i--;
/* 181:299 */       RecordBase rb = (RecordBase)records.get(i);
/* 182:300 */       if (isDVTPriorRecord(rb))
/* 183:    */       {
/* 184:301 */         Record nextRec = (Record)records.get(i + 1);
/* 185:302 */         if (!isDVTSubsequentRecord(nextRec.getSid())) {
/* 186:303 */           throw new IllegalStateException("Unexpected (" + nextRec.getClass().getName() + ") found after (" + rb.getClass().getName() + ")");
/* 187:    */         }
/* 188:306 */         return i + 1;
/* 189:    */       }
/* 190:308 */       Record rec = (Record)rb;
/* 191:309 */       if (!isDVTSubsequentRecord(rec.getSid())) {
/* 192:310 */         throw new IllegalStateException("Unexpected (" + rec.getClass().getName() + ") while looking for DV Table insert pos");
/* 193:    */       }
/* 194:    */     }
/* 195:314 */     return 0;
/* 196:    */   }
/* 197:    */   
/* 198:    */   private static boolean isDVTPriorRecord(RecordBase rb)
/* 199:    */   {
/* 200:319 */     if (((rb instanceof MergedCellsTable)) || ((rb instanceof ConditionalFormattingTable))) {
/* 201:320 */       return true;
/* 202:    */     }
/* 203:322 */     short sid = ((Record)rb).getSid();
/* 204:323 */     switch (sid)
/* 205:    */     {
/* 206:    */     case 29: 
/* 207:    */     case 65: 
/* 208:    */     case 153: 
/* 209:    */     case 160: 
/* 210:    */     case 239: 
/* 211:    */     case 351: 
/* 212:    */     case 440: 
/* 213:    */     case 442: 
/* 214:    */     case 574: 
/* 215:    */     case 2048: 
/* 216:337 */       return true;
/* 217:    */     }
/* 218:339 */     return false;
/* 219:    */   }
/* 220:    */   
/* 221:    */   private static boolean isDVTSubsequentRecord(short sid)
/* 222:    */   {
/* 223:343 */     switch (sid)
/* 224:    */     {
/* 225:    */     case 10: 
/* 226:    */     case 2146: 
/* 227:    */     case 2151: 
/* 228:    */     case 2152: 
/* 229:    */     case 2248: 
/* 230:349 */       return true;
/* 231:    */     }
/* 232:351 */     return false;
/* 233:    */   }
/* 234:    */   
/* 235:    */   private static int getDimensionsIndex(List<RecordBase> records)
/* 236:    */   {
/* 237:357 */     int nRecs = records.size();
/* 238:358 */     for (int i = 0; i < nRecs; i++) {
/* 239:359 */       if ((records.get(i) instanceof DimensionsRecord)) {
/* 240:360 */         return i;
/* 241:    */       }
/* 242:    */     }
/* 243:364 */     throw new RuntimeException("DimensionsRecord not found");
/* 244:    */   }
/* 245:    */   
/* 246:    */   private static int getGutsRecordInsertPos(List<RecordBase> records)
/* 247:    */   {
/* 248:368 */     int dimensionsIndex = getDimensionsIndex(records);
/* 249:369 */     int i = dimensionsIndex - 1;
/* 250:370 */     while (i > 0)
/* 251:    */     {
/* 252:371 */       i--;
/* 253:372 */       RecordBase rb = (RecordBase)records.get(i);
/* 254:373 */       if (isGutsPriorRecord(rb)) {
/* 255:374 */         return i + 1;
/* 256:    */       }
/* 257:    */     }
/* 258:377 */     throw new RuntimeException("Did not find insert point for GUTS");
/* 259:    */   }
/* 260:    */   
/* 261:    */   private static boolean isGutsPriorRecord(RecordBase rb)
/* 262:    */   {
/* 263:381 */     if ((rb instanceof Record))
/* 264:    */     {
/* 265:382 */       Record record = (Record)rb;
/* 266:383 */       switch (record.getSid())
/* 267:    */       {
/* 268:    */       case 12: 
/* 269:    */       case 13: 
/* 270:    */       case 14: 
/* 271:    */       case 15: 
/* 272:    */       case 16: 
/* 273:    */       case 17: 
/* 274:    */       case 34: 
/* 275:    */       case 42: 
/* 276:    */       case 43: 
/* 277:    */       case 94: 
/* 278:    */       case 95: 
/* 279:    */       case 130: 
/* 280:    */       case 523: 
/* 281:    */       case 2057: 
/* 282:400 */         return true;
/* 283:    */       }
/* 284:    */     }
/* 285:404 */     return false;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public static boolean isEndOfRowBlock(int sid)
/* 289:    */   {
/* 290:412 */     switch (sid)
/* 291:    */     {
/* 292:    */     case 61: 
/* 293:    */     case 93: 
/* 294:    */     case 125: 
/* 295:    */     case 128: 
/* 296:    */     case 176: 
/* 297:    */     case 236: 
/* 298:    */     case 237: 
/* 299:    */     case 438: 
/* 300:    */     case 574: 
/* 301:424 */       return true;
/* 302:    */     case 434: 
/* 303:427 */       return true;
/* 304:    */     case 10: 
/* 305:430 */       throw new RuntimeException("Found EOFRecord before WindowTwoRecord was encountered");
/* 306:    */     }
/* 307:432 */     return PageSettingsBlock.isComponentRecord(sid);
/* 308:    */   }
/* 309:    */   
/* 310:    */   public static boolean isRowBlockRecord(int sid)
/* 311:    */   {
/* 312:440 */     switch (sid)
/* 313:    */     {
/* 314:    */     case 6: 
/* 315:    */     case 253: 
/* 316:    */     case 513: 
/* 317:    */     case 515: 
/* 318:    */     case 516: 
/* 319:    */     case 517: 
/* 320:    */     case 520: 
/* 321:    */     case 545: 
/* 322:    */     case 566: 
/* 323:    */     case 638: 
/* 324:    */     case 1212: 
/* 325:454 */       return true;
/* 326:    */     }
/* 327:456 */     return false;
/* 328:    */   }
/* 329:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.model.RecordOrderer
 * JD-Core Version:    0.7.0.1
 */