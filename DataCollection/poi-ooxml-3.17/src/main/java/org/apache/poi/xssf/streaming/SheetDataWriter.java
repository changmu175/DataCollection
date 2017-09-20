/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.Closeable;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.FileInputStream;
/*   7:    */ import java.io.FileOutputStream;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.InputStream;
/*  10:    */ import java.io.OutputStream;
/*  11:    */ import java.io.OutputStreamWriter;
/*  12:    */ import java.io.Writer;
/*  13:    */ import java.util.Iterator;
/*  14:    */ import org.apache.poi.ss.usermodel.Cell;
/*  15:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  16:    */ import org.apache.poi.ss.usermodel.CellType;
/*  17:    */ import org.apache.poi.ss.usermodel.FormulaError;
/*  18:    */ import org.apache.poi.ss.util.CellReference;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ import org.apache.poi.util.TempFile;
/*  22:    */ import org.apache.poi.xssf.model.SharedStringsTable;
/*  23:    */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
/*  25:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType.Enum;
/*  26:    */ 
/*  27:    */ public class SheetDataWriter
/*  28:    */   implements Closeable
/*  29:    */ {
/*  30: 53 */   private static final POILogger logger = POILogFactory.getLogger(SheetDataWriter.class);
/*  31:    */   private final File _fd;
/*  32:    */   private final Writer _out;
/*  33:    */   private int _rownum;
/*  34:    */   private int _numberOfFlushedRows;
/*  35:    */   private int _lowestIndexOfFlushedRows;
/*  36:    */   private int _numberOfCellsOfLastFlushedRow;
/*  37: 61 */   private int _numberLastFlushedRow = -1;
/*  38:    */   private SharedStringsTable _sharedStringSource;
/*  39:    */   
/*  40:    */   public SheetDataWriter()
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 70 */     this._fd = createTempFile();
/*  44: 71 */     this._out = createWriter(this._fd);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public SheetDataWriter(SharedStringsTable sharedStringsTable)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 75 */     this();
/*  51: 76 */     this._sharedStringSource = sharedStringsTable;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public File createTempFile()
/*  55:    */     throws IOException
/*  56:    */   {
/*  57: 87 */     return TempFile.createTempFile("poi-sxssf-sheet", ".xml");
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Writer createWriter(File fd)
/*  61:    */     throws IOException
/*  62:    */   {
/*  63: 96 */     FileOutputStream fos = new FileOutputStream(fd);
/*  64:    */     OutputStream decorated;
/*  65:    */     try
/*  66:    */     {
/*  67: 99 */       decorated = decorateOutputStream(fos);
/*  68:    */     }
/*  69:    */     catch (IOException e)
/*  70:    */     {
/*  71:101 */       fos.close();
/*  72:102 */       throw e;
/*  73:    */     }
/*  74:104 */     return new BufferedWriter(new OutputStreamWriter(decorated, "UTF-8"));
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected OutputStream decorateOutputStream(FileOutputStream fos)
/*  78:    */     throws IOException
/*  79:    */   {
/*  80:119 */     return fos;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void close()
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:127 */     this._out.flush();
/*  87:128 */     this._out.close();
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected File getTempFile()
/*  91:    */   {
/*  92:132 */     return this._fd;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public InputStream getWorksheetXMLInputStream()
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:139 */     File fd = getTempFile();
/*  99:140 */     FileInputStream fis = new FileInputStream(fd);
/* 100:    */     try
/* 101:    */     {
/* 102:142 */       return decorateInputStream(fis);
/* 103:    */     }
/* 104:    */     catch (IOException e)
/* 105:    */     {
/* 106:144 */       fis.close();
/* 107:145 */       throw e;
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected InputStream decorateInputStream(FileInputStream fis)
/* 112:    */     throws IOException
/* 113:    */   {
/* 114:160 */     return fis;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getNumberOfFlushedRows()
/* 118:    */   {
/* 119:164 */     return this._numberOfFlushedRows;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int getNumberOfCellsOfLastFlushedRow()
/* 123:    */   {
/* 124:168 */     return this._numberOfCellsOfLastFlushedRow;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int getLowestIndexOfFlushedRows()
/* 128:    */   {
/* 129:172 */     return this._lowestIndexOfFlushedRows;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int getLastFlushedRow()
/* 133:    */   {
/* 134:176 */     return this._numberLastFlushedRow;
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void finalize()
/* 138:    */     throws Throwable
/* 139:    */   {
/* 140:181 */     if (!this._fd.delete()) {
/* 141:182 */       logger.log(7, new Object[] { "Can't delete temporary encryption file: " + this._fd });
/* 142:    */     }
/* 143:185 */     super.finalize();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void writeRow(int rownum, SXSSFRow row)
/* 147:    */     throws IOException
/* 148:    */   {
/* 149:195 */     if (this._numberOfFlushedRows == 0) {
/* 150:196 */       this._lowestIndexOfFlushedRows = rownum;
/* 151:    */     }
/* 152:197 */     this._numberLastFlushedRow = Math.max(rownum, this._numberLastFlushedRow);
/* 153:198 */     this._numberOfCellsOfLastFlushedRow = row.getLastCellNum();
/* 154:199 */     this._numberOfFlushedRows += 1;
/* 155:200 */     beginRow(rownum, row);
/* 156:201 */     Iterator<Cell> cells = row.allCellsIterator();
/* 157:202 */     int columnIndex = 0;
/* 158:203 */     while (cells.hasNext()) {
/* 159:204 */       writeCell(columnIndex++, (Cell)cells.next());
/* 160:    */     }
/* 161:206 */     endRow();
/* 162:    */   }
/* 163:    */   
/* 164:    */   void beginRow(int rownum, SXSSFRow row)
/* 165:    */     throws IOException
/* 166:    */   {
/* 167:210 */     this._out.write("<row");
/* 168:211 */     writeAttribute("r", Integer.toString(rownum + 1));
/* 169:212 */     if (row.hasCustomHeight())
/* 170:    */     {
/* 171:213 */       writeAttribute("customHeight", "true");
/* 172:214 */       writeAttribute("ht", Float.toString(row.getHeightInPoints()));
/* 173:    */     }
/* 174:216 */     if (row.getZeroHeight()) {
/* 175:217 */       writeAttribute("hidden", "true");
/* 176:    */     }
/* 177:219 */     if (row.isFormatted())
/* 178:    */     {
/* 179:220 */       writeAttribute("s", Integer.toString(row.getRowStyleIndex()));
/* 180:221 */       writeAttribute("customFormat", "1");
/* 181:    */     }
/* 182:223 */     if (row.getOutlineLevel() != 0) {
/* 183:224 */       writeAttribute("outlineLevel", Integer.toString(row.getOutlineLevel()));
/* 184:    */     }
/* 185:226 */     if (row.getHidden() != null) {
/* 186:227 */       writeAttribute("hidden", row.getHidden().booleanValue() ? "1" : "0");
/* 187:    */     }
/* 188:229 */     if (row.getCollapsed() != null) {
/* 189:230 */       writeAttribute("collapsed", row.getCollapsed().booleanValue() ? "1" : "0");
/* 190:    */     }
/* 191:233 */     this._out.write(">\n");
/* 192:234 */     this._rownum = rownum;
/* 193:    */   }
/* 194:    */   
/* 195:    */   void endRow()
/* 196:    */     throws IOException
/* 197:    */   {
/* 198:238 */     this._out.write("</row>\n");
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void writeCell(int columnIndex, Cell cell)
/* 202:    */     throws IOException
/* 203:    */   {
/* 204:242 */     if (cell == null) {
/* 205:243 */       return;
/* 206:    */     }
/* 207:245 */     String ref = new CellReference(this._rownum, columnIndex).formatAsString();
/* 208:246 */     this._out.write("<c");
/* 209:247 */     writeAttribute("r", ref);
/* 210:248 */     CellStyle cellStyle = cell.getCellStyle();
/* 211:249 */     if (cellStyle.getIndex() != 0) {
/* 212:253 */       writeAttribute("s", Integer.toString(cellStyle.getIndex() & 0xFFFF));
/* 213:    */     }
/* 214:255 */     CellType cellType = cell.getCellTypeEnum();
/* 215:256 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()])
/* 216:    */     {
/* 217:    */     case 2: 
/* 218:258 */       this._out.write(62);
/* 219:259 */       break;
/* 220:    */     case 3: 
/* 221:262 */       this._out.write("><f>");
/* 222:263 */       outputQuotedString(cell.getCellFormula());
/* 223:264 */       this._out.write("</f>");
/* 224:265 */       switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCachedFormulaResultTypeEnum().ordinal()])
/* 225:    */       {
/* 226:    */       case 1: 
/* 227:267 */         double nval = cell.getNumericCellValue();
/* 228:268 */         if (!Double.isNaN(nval))
/* 229:    */         {
/* 230:269 */           this._out.write("<v>");
/* 231:270 */           this._out.write(Double.toString(nval));
/* 232:271 */           this._out.write("</v>");
/* 233:    */         }
/* 234:    */         break;
/* 235:    */       }
/* 236:277 */       break;
/* 237:    */     case 4: 
/* 238:280 */       if (this._sharedStringSource != null)
/* 239:    */       {
/* 240:281 */         XSSFRichTextString rt = new XSSFRichTextString(cell.getStringCellValue());
/* 241:282 */         int sRef = this._sharedStringSource.addEntry(rt.getCTRst());
/* 242:    */         
/* 243:284 */         writeAttribute("t", STCellType.S.toString());
/* 244:285 */         this._out.write("><v>");
/* 245:286 */         this._out.write(String.valueOf(sRef));
/* 246:287 */         this._out.write("</v>");
/* 247:    */       }
/* 248:    */       else
/* 249:    */       {
/* 250:289 */         writeAttribute("t", "inlineStr");
/* 251:290 */         this._out.write("><is><t");
/* 252:291 */         if (hasLeadingTrailingSpaces(cell.getStringCellValue())) {
/* 253:292 */           writeAttribute("xml:space", "preserve");
/* 254:    */         }
/* 255:294 */         this._out.write(">");
/* 256:295 */         outputQuotedString(cell.getStringCellValue());
/* 257:296 */         this._out.write("</t></is>");
/* 258:    */       }
/* 259:298 */       break;
/* 260:    */     case 1: 
/* 261:301 */       writeAttribute("t", "n");
/* 262:302 */       this._out.write("><v>");
/* 263:303 */       this._out.write(Double.toString(cell.getNumericCellValue()));
/* 264:304 */       this._out.write("</v>");
/* 265:305 */       break;
/* 266:    */     case 5: 
/* 267:308 */       writeAttribute("t", "b");
/* 268:309 */       this._out.write("><v>");
/* 269:310 */       this._out.write(cell.getBooleanCellValue() ? "1" : "0");
/* 270:311 */       this._out.write("</v>");
/* 271:312 */       break;
/* 272:    */     case 6: 
/* 273:315 */       FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
/* 274:    */       
/* 275:317 */       writeAttribute("t", "e");
/* 276:318 */       this._out.write("><v>");
/* 277:319 */       this._out.write(error.getString());
/* 278:320 */       this._out.write("</v>");
/* 279:321 */       break;
/* 280:    */     default: 
/* 281:324 */       throw new IllegalStateException("Invalid cell type: " + cellType);
/* 282:    */     }
/* 283:327 */     this._out.write("</c>");
/* 284:    */   }
/* 285:    */   
/* 286:    */   private void writeAttribute(String name, String value)
/* 287:    */     throws IOException
/* 288:    */   {
/* 289:331 */     this._out.write(32);
/* 290:332 */     this._out.write(name);
/* 291:333 */     this._out.write("=\"");
/* 292:334 */     this._out.write(value);
/* 293:335 */     this._out.write(34);
/* 294:    */   }
/* 295:    */   
/* 296:    */   boolean hasLeadingTrailingSpaces(String str)
/* 297:    */   {
/* 298:343 */     if ((str != null) && (str.length() > 0))
/* 299:    */     {
/* 300:344 */       char firstChar = str.charAt(0);
/* 301:345 */       char lastChar = str.charAt(str.length() - 1);
/* 302:346 */       return (Character.isWhitespace(firstChar)) || (Character.isWhitespace(lastChar));
/* 303:    */     }
/* 304:348 */     return false;
/* 305:    */   }
/* 306:    */   
/* 307:    */   protected void outputQuotedString(String s)
/* 308:    */     throws IOException
/* 309:    */   {
/* 310:353 */     if ((s == null) || (s.length() == 0)) {
/* 311:354 */       return;
/* 312:    */     }
/* 313:357 */     char[] chars = s.toCharArray();
/* 314:358 */     int last = 0;
/* 315:359 */     int length = s.length();
/* 316:360 */     for (int counter = 0; counter < length; counter++)
/* 317:    */     {
/* 318:361 */       char c = chars[counter];
/* 319:362 */       switch (c)
/* 320:    */       {
/* 321:    */       case '<': 
/* 322:364 */         writeLastChars(this._out, chars, last, counter);
/* 323:365 */         last = counter + 1;
/* 324:366 */         this._out.write("&lt;");
/* 325:367 */         break;
/* 326:    */       case '>': 
/* 327:369 */         writeLastChars(this._out, chars, last, counter);
/* 328:370 */         last = counter + 1;
/* 329:371 */         this._out.write("&gt;");
/* 330:372 */         break;
/* 331:    */       case '&': 
/* 332:374 */         writeLastChars(this._out, chars, last, counter);
/* 333:375 */         last = counter + 1;
/* 334:376 */         this._out.write("&amp;");
/* 335:377 */         break;
/* 336:    */       case '"': 
/* 337:379 */         writeLastChars(this._out, chars, last, counter);
/* 338:380 */         last = counter + 1;
/* 339:381 */         this._out.write("&quot;");
/* 340:382 */         break;
/* 341:    */       case '\n': 
/* 342:385 */         writeLastChars(this._out, chars, last, counter);
/* 343:386 */         this._out.write("&#xa;");
/* 344:387 */         last = counter + 1;
/* 345:388 */         break;
/* 346:    */       case '\r': 
/* 347:390 */         writeLastChars(this._out, chars, last, counter);
/* 348:391 */         this._out.write("&#xd;");
/* 349:392 */         last = counter + 1;
/* 350:393 */         break;
/* 351:    */       case '\t': 
/* 352:395 */         writeLastChars(this._out, chars, last, counter);
/* 353:396 */         this._out.write("&#x9;");
/* 354:397 */         last = counter + 1;
/* 355:398 */         break;
/* 356:    */       case ' ': 
/* 357:400 */         writeLastChars(this._out, chars, last, counter);
/* 358:401 */         this._out.write("&#xa0;");
/* 359:402 */         last = counter + 1;
/* 360:403 */         break;
/* 361:    */       default: 
/* 362:407 */         if (replaceWithQuestionMark(c))
/* 363:    */         {
/* 364:408 */           writeLastChars(this._out, chars, last, counter);
/* 365:409 */           this._out.write(63);
/* 366:410 */           last = counter + 1;
/* 367:    */         }
/* 368:412 */         else if ((Character.isHighSurrogate(c)) || (Character.isLowSurrogate(c)))
/* 369:    */         {
/* 370:413 */           writeLastChars(this._out, chars, last, counter);
/* 371:414 */           this._out.write(c);
/* 372:415 */           last = counter + 1;
/* 373:    */         }
/* 374:417 */         else if (c > '')
/* 375:    */         {
/* 376:418 */           writeLastChars(this._out, chars, last, counter);
/* 377:419 */           last = counter + 1;
/* 378:    */           
/* 379:    */ 
/* 380:422 */           this._out.write("&#");
/* 381:423 */           this._out.write(String.valueOf(c));
/* 382:424 */           this._out.write(";");
/* 383:    */         }
/* 384:    */         break;
/* 385:    */       }
/* 386:    */     }
/* 387:429 */     if (last < length) {
/* 388:430 */       this._out.write(chars, last, length - last);
/* 389:    */     }
/* 390:    */   }
/* 391:    */   
/* 392:    */   private static void writeLastChars(Writer out, char[] chars, int last, int counter)
/* 393:    */     throws IOException
/* 394:    */   {
/* 395:435 */     if (counter > last) {
/* 396:436 */       out.write(chars, last, counter - last);
/* 397:    */     }
/* 398:    */   }
/* 399:    */   
/* 400:    */   static boolean replaceWithQuestionMark(char c)
/* 401:    */   {
/* 402:441 */     return (c < ' ') || ((65534 <= c) && (c <= 65535));
/* 403:    */   }
/* 404:    */   
/* 405:    */   boolean dispose()
/* 406:    */     throws IOException
/* 407:    */   {
/* 408:    */     boolean ret;
/* 409:    */     try
/* 410:    */     {
/* 411:451 */       this._out.close();
/* 412:    */     }
/* 413:    */     finally
/* 414:    */     {
/* 415:453 */       ret = this._fd.delete();
/* 416:    */     }
/* 417:455 */     return ret;
/* 418:    */   }
/* 419:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SheetDataWriter
 * JD-Core Version:    0.7.0.1
 */