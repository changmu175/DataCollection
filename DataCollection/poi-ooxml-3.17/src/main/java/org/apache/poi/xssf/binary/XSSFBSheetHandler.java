/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.util.Queue;
/*   5:    */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*   6:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*   7:    */ import org.apache.poi.ss.util.CellAddress;
/*   8:    */ import org.apache.poi.util.Internal;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
/*  11:    */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*  12:    */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*  13:    */ 
/*  14:    */ @Internal
/*  15:    */ public class XSSFBSheetHandler
/*  16:    */   extends XSSFBParser
/*  17:    */ {
/*  18:    */   private static final int CHECK_ALL_ROWS = -1;
/*  19:    */   private final XSSFBSharedStringsTable stringsTable;
/*  20:    */   private final XSSFSheetXMLHandler.SheetContentsHandler handler;
/*  21:    */   private final XSSFBStylesTable styles;
/*  22:    */   private final XSSFBCommentsTable comments;
/*  23:    */   private final DataFormatter dataFormatter;
/*  24:    */   private final boolean formulasNotResults;
/*  25: 48 */   private int lastEndedRow = -1;
/*  26: 49 */   private int lastStartedRow = -1;
/*  27: 50 */   private int currentRow = 0;
/*  28: 51 */   private byte[] rkBuffer = new byte[8];
/*  29: 52 */   private XSSFBCellRange hyperlinkCellRange = null;
/*  30: 53 */   private StringBuilder xlWideStringBuffer = new StringBuilder();
/*  31: 55 */   private final XSSFBCellHeader cellBuffer = new XSSFBCellHeader();
/*  32:    */   
/*  33:    */   public XSSFBSheetHandler(InputStream is, XSSFBStylesTable styles, XSSFBCommentsTable comments, XSSFBSharedStringsTable strings, XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults)
/*  34:    */   {
/*  35: 63 */     super(is);
/*  36: 64 */     this.styles = styles;
/*  37: 65 */     this.comments = comments;
/*  38: 66 */     this.stringsTable = strings;
/*  39: 67 */     this.handler = sheetContentsHandler;
/*  40: 68 */     this.dataFormatter = dataFormatter;
/*  41: 69 */     this.formulasNotResults = formulasNotResults;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void handleRecord(int id, byte[] data)
/*  45:    */     throws XSSFBParseException
/*  46:    */   {
/*  47: 74 */     XSSFBRecordType type = XSSFBRecordType.lookup(id);
/*  48: 76 */     switch (1.$SwitchMap$org$apache$poi$xssf$binary$XSSFBRecordType[type.ordinal()])
/*  49:    */     {
/*  50:    */     case 1: 
/*  51: 78 */       int rw = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 0));
/*  52: 79 */       if (rw > 1048576) {
/*  53: 80 */         throw new XSSFBParseException("Row number beyond allowable range: " + rw);
/*  54:    */       }
/*  55: 82 */       this.currentRow = rw;
/*  56: 83 */       checkMissedComments(this.currentRow);
/*  57: 84 */       startRow(this.currentRow);
/*  58: 85 */       break;
/*  59:    */     case 2: 
/*  60: 87 */       handleBrtCellIsst(data);
/*  61: 88 */       break;
/*  62:    */     case 3: 
/*  63: 90 */       handleCellSt(data);
/*  64: 91 */       break;
/*  65:    */     case 4: 
/*  66: 93 */       handleCellRk(data);
/*  67: 94 */       break;
/*  68:    */     case 5: 
/*  69: 96 */       handleCellReal(data);
/*  70: 97 */       break;
/*  71:    */     case 6: 
/*  72: 99 */       handleBoolean(data);
/*  73:100 */       break;
/*  74:    */     case 7: 
/*  75:102 */       handleCellError(data);
/*  76:103 */       break;
/*  77:    */     case 8: 
/*  78:105 */       beforeCellValue(data);
/*  79:106 */       break;
/*  80:    */     case 9: 
/*  81:108 */       handleFmlaString(data);
/*  82:109 */       break;
/*  83:    */     case 10: 
/*  84:111 */       handleFmlaNum(data);
/*  85:112 */       break;
/*  86:    */     case 11: 
/*  87:114 */       handleFmlaError(data);
/*  88:115 */       break;
/*  89:    */     case 12: 
/*  90:118 */       checkMissedComments(-1);
/*  91:119 */       endRow(this.lastStartedRow);
/*  92:120 */       break;
/*  93:    */     case 13: 
/*  94:122 */       handleHeaderFooter(data);
/*  95:    */     }
/*  96:    */   }
/*  97:    */   
/*  98:    */   private void beforeCellValue(byte[] data)
/*  99:    */   {
/* 100:129 */     XSSFBCellHeader.parse(data, 0, this.currentRow, this.cellBuffer);
/* 101:130 */     checkMissedComments(this.currentRow, this.cellBuffer.getColNum());
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void handleCellValue(String formattedValue)
/* 105:    */   {
/* 106:134 */     CellAddress cellAddress = new CellAddress(this.currentRow, this.cellBuffer.getColNum());
/* 107:135 */     XSSFBComment comment = null;
/* 108:136 */     if (this.comments != null) {
/* 109:137 */       comment = this.comments.get(cellAddress);
/* 110:    */     }
/* 111:139 */     this.handler.cell(cellAddress.formatAsString(), formattedValue, comment);
/* 112:    */   }
/* 113:    */   
/* 114:    */   private void handleFmlaNum(byte[] data)
/* 115:    */   {
/* 116:143 */     beforeCellValue(data);
/* 117:    */     
/* 118:145 */     double val = LittleEndian.getDouble(data, XSSFBCellHeader.length);
/* 119:146 */     handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void handleCellSt(byte[] data)
/* 123:    */   {
/* 124:150 */     beforeCellValue(data);
/* 125:151 */     this.xlWideStringBuffer.setLength(0);
/* 126:152 */     XSSFBUtils.readXLWideString(data, XSSFBCellHeader.length, this.xlWideStringBuffer);
/* 127:153 */     handleCellValue(this.xlWideStringBuffer.toString());
/* 128:    */   }
/* 129:    */   
/* 130:    */   private void handleFmlaString(byte[] data)
/* 131:    */   {
/* 132:157 */     beforeCellValue(data);
/* 133:158 */     this.xlWideStringBuffer.setLength(0);
/* 134:159 */     XSSFBUtils.readXLWideString(data, XSSFBCellHeader.length, this.xlWideStringBuffer);
/* 135:160 */     handleCellValue(this.xlWideStringBuffer.toString());
/* 136:    */   }
/* 137:    */   
/* 138:    */   private void handleCellError(byte[] data)
/* 139:    */   {
/* 140:164 */     beforeCellValue(data);
/* 141:    */     
/* 142:166 */     handleCellValue("ERROR");
/* 143:    */   }
/* 144:    */   
/* 145:    */   private void handleFmlaError(byte[] data)
/* 146:    */   {
/* 147:170 */     beforeCellValue(data);
/* 148:    */     
/* 149:172 */     handleCellValue("ERROR");
/* 150:    */   }
/* 151:    */   
/* 152:    */   private void handleBoolean(byte[] data)
/* 153:    */   {
/* 154:176 */     beforeCellValue(data);
/* 155:177 */     String formattedVal = data[XSSFBCellHeader.length] == 1 ? "TRUE" : "FALSE";
/* 156:178 */     handleCellValue(formattedVal);
/* 157:    */   }
/* 158:    */   
/* 159:    */   private void handleCellReal(byte[] data)
/* 160:    */   {
/* 161:182 */     beforeCellValue(data);
/* 162:    */     
/* 163:184 */     double val = LittleEndian.getDouble(data, XSSFBCellHeader.length);
/* 164:185 */     handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
/* 165:    */   }
/* 166:    */   
/* 167:    */   private void handleCellRk(byte[] data)
/* 168:    */   {
/* 169:189 */     beforeCellValue(data);
/* 170:190 */     double val = rkNumber(data, XSSFBCellHeader.length);
/* 171:191 */     handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
/* 172:    */   }
/* 173:    */   
/* 174:    */   private String formatVal(double val, int styleIdx)
/* 175:    */   {
/* 176:195 */     String formatString = this.styles.getNumberFormatString(styleIdx);
/* 177:196 */     short styleIndex = this.styles.getNumberFormatIndex(styleIdx);
/* 178:201 */     if (formatString == null)
/* 179:    */     {
/* 180:202 */       formatString = BuiltinFormats.getBuiltinFormat(0);
/* 181:203 */       styleIndex = 0;
/* 182:    */     }
/* 183:205 */     return this.dataFormatter.formatRawCellContents(val, styleIndex, formatString);
/* 184:    */   }
/* 185:    */   
/* 186:    */   private void handleBrtCellIsst(byte[] data)
/* 187:    */   {
/* 188:209 */     beforeCellValue(data);
/* 189:210 */     int idx = XSSFBUtils.castToInt(LittleEndian.getUInt(data, XSSFBCellHeader.length));
/* 190:211 */     XSSFRichTextString rtss = new XSSFRichTextString(this.stringsTable.getEntryAt(idx));
/* 191:212 */     handleCellValue(rtss.getString());
/* 192:    */   }
/* 193:    */   
/* 194:    */   private void handleHeaderFooter(byte[] data)
/* 195:    */   {
/* 196:217 */     XSSFBHeaderFooters headerFooter = XSSFBHeaderFooters.parse(data);
/* 197:218 */     outputHeaderFooter(headerFooter.getHeader());
/* 198:219 */     outputHeaderFooter(headerFooter.getFooter());
/* 199:220 */     outputHeaderFooter(headerFooter.getHeaderEven());
/* 200:221 */     outputHeaderFooter(headerFooter.getFooterEven());
/* 201:222 */     outputHeaderFooter(headerFooter.getHeaderFirst());
/* 202:223 */     outputHeaderFooter(headerFooter.getFooterFirst());
/* 203:    */   }
/* 204:    */   
/* 205:    */   private void outputHeaderFooter(XSSFBHeaderFooter headerFooter)
/* 206:    */   {
/* 207:227 */     String text = headerFooter.getString();
/* 208:228 */     if ((text != null) && (text.trim().length() > 0)) {
/* 209:229 */       this.handler.headerFooter(text, headerFooter.isHeader(), headerFooter.getHeaderFooterTypeLabel());
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   private void checkMissedComments(int currentRow, int colNum)
/* 214:    */   {
/* 215:236 */     if (this.comments == null) {
/* 216:237 */       return;
/* 217:    */     }
/* 218:239 */     Queue<CellAddress> queue = this.comments.getAddresses();
/* 219:240 */     while (queue.size() > 0)
/* 220:    */     {
/* 221:241 */       CellAddress cellAddress = (CellAddress)queue.peek();
/* 222:242 */       if ((cellAddress.getRow() == currentRow) && (cellAddress.getColumn() < colNum))
/* 223:    */       {
/* 224:243 */         cellAddress = (CellAddress)queue.remove();
/* 225:244 */         dumpEmptyCellComment(cellAddress, this.comments.get(cellAddress));
/* 226:    */       }
/* 227:    */       else
/* 228:    */       {
/* 229:245 */         if ((cellAddress.getRow() == currentRow) && (cellAddress.getColumn() == colNum))
/* 230:    */         {
/* 231:246 */           queue.remove();
/* 232:247 */           return;
/* 233:    */         }
/* 234:248 */         if ((cellAddress.getRow() == currentRow) && (cellAddress.getColumn() > colNum)) {
/* 235:249 */           return;
/* 236:    */         }
/* 237:250 */         if (cellAddress.getRow() > currentRow) {
/* 238:251 */           return;
/* 239:    */         }
/* 240:    */       }
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   private void checkMissedComments(int currentRow)
/* 245:    */   {
/* 246:258 */     if (this.comments == null) {
/* 247:259 */       return;
/* 248:    */     }
/* 249:261 */     Queue<CellAddress> queue = this.comments.getAddresses();
/* 250:262 */     int lastInterpolatedRow = -1;
/* 251:263 */     while (queue.size() > 0)
/* 252:    */     {
/* 253:264 */       CellAddress cellAddress = (CellAddress)queue.peek();
/* 254:265 */       if ((currentRow != -1) && (cellAddress.getRow() >= currentRow)) {
/* 255:    */         break;
/* 256:    */       }
/* 257:266 */       cellAddress = (CellAddress)queue.remove();
/* 258:267 */       if (cellAddress.getRow() != lastInterpolatedRow) {
/* 259:268 */         startRow(cellAddress.getRow());
/* 260:    */       }
/* 261:270 */       dumpEmptyCellComment(cellAddress, this.comments.get(cellAddress));
/* 262:271 */       lastInterpolatedRow = cellAddress.getRow();
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   private void startRow(int row)
/* 267:    */   {
/* 268:280 */     if (row == this.lastStartedRow) {
/* 269:281 */       return;
/* 270:    */     }
/* 271:284 */     if (this.lastStartedRow != this.lastEndedRow) {
/* 272:285 */       endRow(this.lastStartedRow);
/* 273:    */     }
/* 274:287 */     this.handler.startRow(row);
/* 275:288 */     this.lastStartedRow = row;
/* 276:    */   }
/* 277:    */   
/* 278:    */   private void endRow(int row)
/* 279:    */   {
/* 280:292 */     if (this.lastEndedRow == row) {
/* 281:293 */       return;
/* 282:    */     }
/* 283:295 */     this.handler.endRow(row);
/* 284:296 */     this.lastEndedRow = row;
/* 285:    */   }
/* 286:    */   
/* 287:    */   private void dumpEmptyCellComment(CellAddress cellAddress, XSSFBComment comment)
/* 288:    */   {
/* 289:300 */     this.handler.cell(cellAddress.formatAsString(), null, comment);
/* 290:    */   }
/* 291:    */   
/* 292:    */   private double rkNumber(byte[] data, int offset)
/* 293:    */   {
/* 294:305 */     byte b0 = data[offset];
/* 295:306 */     String s = Integer.toString(b0, 2);
/* 296:307 */     boolean numDivBy100 = (b0 & 0x1) == 1;
/* 297:308 */     boolean floatingPoint = (b0 >> 1 & 0x1) == 0;
/* 298:    */     
/* 299:    */ 
/* 300:311 */     b0 = (byte)(b0 & 0xFFFFFFFE);
/* 301:312 */     b0 = (byte)(b0 & 0xFFFFFFFD);
/* 302:    */     
/* 303:314 */     this.rkBuffer[4] = b0;
/* 304:315 */     for (int i = 1; i < 4; i++) {
/* 305:316 */       this.rkBuffer[(i + 4)] = data[(offset + i)];
/* 306:    */     }
/* 307:318 */     double d = 0.0D;
/* 308:319 */     if (floatingPoint) {
/* 309:320 */       d = LittleEndian.getDouble(this.rkBuffer);
/* 310:    */     } else {
/* 311:322 */       d = LittleEndian.getInt(this.rkBuffer);
/* 312:    */     }
/* 313:324 */     d = numDivBy100 ? d / 100.0D : d;
/* 314:325 */     return d;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public static abstract interface SheetContentsHandler
/* 318:    */     extends XSSFSheetXMLHandler.SheetContentsHandler
/* 319:    */   {
/* 320:    */     public abstract void hyperlinkCell(String paramString1, String paramString2, String paramString3, String paramString4, XSSFComment paramXSSFComment);
/* 321:    */   }
/* 322:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBSheetHandler
 * JD-Core Version:    0.7.0.1
 */