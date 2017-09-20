/*   1:    */ package org.apache.poi.xssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.Queue;
/*   5:    */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*   6:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*   7:    */ import org.apache.poi.ss.util.CellAddress;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  11:    */ import org.apache.poi.xssf.model.StylesTable;
/*  12:    */ import org.apache.poi.xssf.usermodel.XSSFCellStyle;
/*  13:    */ import org.apache.poi.xssf.usermodel.XSSFComment;
/*  14:    */ import org.apache.poi.xssf.usermodel.XSSFRichTextString;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments;
/*  18:    */ import org.xml.sax.Attributes;
/*  19:    */ import org.xml.sax.SAXException;
/*  20:    */ import org.xml.sax.helpers.DefaultHandler;
/*  21:    */ 
/*  22:    */ public class XSSFSheetXMLHandler
/*  23:    */   extends DefaultHandler
/*  24:    */ {
/*  25: 45 */   private static final POILogger logger = POILogFactory.getLogger(XSSFSheetXMLHandler.class);
/*  26:    */   private StylesTable stylesTable;
/*  27:    */   private CommentsTable commentsTable;
/*  28:    */   private ReadOnlySharedStringsTable sharedStringsTable;
/*  29:    */   private final SheetContentsHandler output;
/*  30:    */   private boolean vIsOpen;
/*  31:    */   private boolean fIsOpen;
/*  32:    */   private boolean isIsOpen;
/*  33:    */   private boolean hfIsOpen;
/*  34:    */   private xssfDataType nextDataType;
/*  35:    */   private short formatIndex;
/*  36:    */   private String formatString;
/*  37:    */   private final DataFormatter formatter;
/*  38:    */   private int rowNum;
/*  39:    */   private int nextRowNum;
/*  40:    */   private String cellRef;
/*  41:    */   private boolean formulasNotResults;
/*  42:    */   
/*  43:    */   static enum xssfDataType
/*  44:    */   {
/*  45: 53 */     BOOLEAN,  ERROR,  FORMULA,  INLINE_STRING,  SST_STRING,  NUMBER;
/*  46:    */     
/*  47:    */     private xssfDataType() {}
/*  48:    */   }
/*  49:    */   
/*  50:105 */   private StringBuffer value = new StringBuffer();
/*  51:106 */   private StringBuffer formula = new StringBuffer();
/*  52:107 */   private StringBuffer headerFooter = new StringBuffer();
/*  53:    */   private Queue<CellAddress> commentCellRefs;
/*  54:    */   
/*  55:    */   public XSSFSheetXMLHandler(StylesTable styles, CommentsTable comments, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults)
/*  56:    */   {
/*  57:124 */     this.stylesTable = styles;
/*  58:125 */     this.commentsTable = comments;
/*  59:126 */     this.sharedStringsTable = strings;
/*  60:127 */     this.output = sheetContentsHandler;
/*  61:128 */     this.formulasNotResults = formulasNotResults;
/*  62:129 */     this.nextDataType = xssfDataType.NUMBER;
/*  63:130 */     this.formatter = dataFormatter;
/*  64:131 */     init();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults)
/*  68:    */   {
/*  69:146 */     this(styles, null, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, boolean formulasNotResults)
/*  73:    */   {
/*  74:160 */     this(styles, strings, sheetContentsHandler, new DataFormatter(), formulasNotResults);
/*  75:    */   }
/*  76:    */   
/*  77:    */   private void init()
/*  78:    */   {
/*  79:164 */     if (this.commentsTable != null)
/*  80:    */     {
/*  81:165 */       this.commentCellRefs = new LinkedList();
/*  82:167 */       for (CTComment comment : this.commentsTable.getCTComments().getCommentList().getCommentArray()) {
/*  83:168 */         this.commentCellRefs.add(new CellAddress(comment.getRef()));
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   private boolean isTextTag(String name)
/*  89:    */   {
/*  90:174 */     if ("v".equals(name)) {
/*  91:176 */       return true;
/*  92:    */     }
/*  93:178 */     if ("inlineStr".equals(name)) {
/*  94:180 */       return true;
/*  95:    */     }
/*  96:182 */     if (("t".equals(name)) && (this.isIsOpen)) {
/*  97:184 */       return true;
/*  98:    */     }
/*  99:187 */     return false;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/* 103:    */     throws SAXException
/* 104:    */   {
/* 105:195 */     if ((uri != null) && (!uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main"))) {
/* 106:196 */       return;
/* 107:    */     }
/* 108:199 */     if (isTextTag(localName))
/* 109:    */     {
/* 110:200 */       this.vIsOpen = true;
/* 111:    */       
/* 112:202 */       this.value.setLength(0);
/* 113:    */     }
/* 114:203 */     else if ("is".equals(localName))
/* 115:    */     {
/* 116:205 */       this.isIsOpen = true;
/* 117:    */     }
/* 118:206 */     else if ("f".equals(localName))
/* 119:    */     {
/* 120:208 */       this.formula.setLength(0);
/* 121:211 */       if (this.nextDataType == xssfDataType.NUMBER) {
/* 122:212 */         this.nextDataType = xssfDataType.FORMULA;
/* 123:    */       }
/* 124:216 */       String type = attributes.getValue("t");
/* 125:217 */       if ((type != null) && (type.equals("shared")))
/* 126:    */       {
/* 127:219 */         String ref = attributes.getValue("ref");
/* 128:220 */         String si = attributes.getValue("si");
/* 129:222 */         if (ref != null) {
/* 130:225 */           this.fIsOpen = true;
/* 131:230 */         } else if (this.formulasNotResults) {
/* 132:231 */           logger.log(5, new Object[] { "shared formulas not yet supported!" });
/* 133:    */         }
/* 134:    */       }
/* 135:    */       else
/* 136:    */       {
/* 137:238 */         this.fIsOpen = true;
/* 138:    */       }
/* 139:    */     }
/* 140:241 */     else if (("oddHeader".equals(localName)) || ("evenHeader".equals(localName)) || ("firstHeader".equals(localName)) || ("firstFooter".equals(localName)) || ("oddFooter".equals(localName)) || ("evenFooter".equals(localName)))
/* 141:    */     {
/* 142:244 */       this.hfIsOpen = true;
/* 143:    */       
/* 144:246 */       this.headerFooter.setLength(0);
/* 145:    */     }
/* 146:248 */     else if ("row".equals(localName))
/* 147:    */     {
/* 148:249 */       String rowNumStr = attributes.getValue("r");
/* 149:250 */       if (rowNumStr != null) {
/* 150:251 */         this.rowNum = (Integer.parseInt(rowNumStr) - 1);
/* 151:    */       } else {
/* 152:253 */         this.rowNum = this.nextRowNum;
/* 153:    */       }
/* 154:255 */       this.output.startRow(this.rowNum);
/* 155:    */     }
/* 156:258 */     else if ("c".equals(localName))
/* 157:    */     {
/* 158:260 */       this.nextDataType = xssfDataType.NUMBER;
/* 159:261 */       this.formatIndex = -1;
/* 160:262 */       this.formatString = null;
/* 161:263 */       this.cellRef = attributes.getValue("r");
/* 162:264 */       String cellType = attributes.getValue("t");
/* 163:265 */       String cellStyleStr = attributes.getValue("s");
/* 164:266 */       if ("b".equals(cellType))
/* 165:    */       {
/* 166:267 */         this.nextDataType = xssfDataType.BOOLEAN;
/* 167:    */       }
/* 168:268 */       else if ("e".equals(cellType))
/* 169:    */       {
/* 170:269 */         this.nextDataType = xssfDataType.ERROR;
/* 171:    */       }
/* 172:270 */       else if ("inlineStr".equals(cellType))
/* 173:    */       {
/* 174:271 */         this.nextDataType = xssfDataType.INLINE_STRING;
/* 175:    */       }
/* 176:272 */       else if ("s".equals(cellType))
/* 177:    */       {
/* 178:273 */         this.nextDataType = xssfDataType.SST_STRING;
/* 179:    */       }
/* 180:274 */       else if ("str".equals(cellType))
/* 181:    */       {
/* 182:275 */         this.nextDataType = xssfDataType.FORMULA;
/* 183:    */       }
/* 184:    */       else
/* 185:    */       {
/* 186:278 */         XSSFCellStyle style = null;
/* 187:279 */         if (this.stylesTable != null) {
/* 188:280 */           if (cellStyleStr != null)
/* 189:    */           {
/* 190:281 */             int styleIndex = Integer.parseInt(cellStyleStr);
/* 191:282 */             style = this.stylesTable.getStyleAt(styleIndex);
/* 192:    */           }
/* 193:283 */           else if (this.stylesTable.getNumCellStyles() > 0)
/* 194:    */           {
/* 195:284 */             style = this.stylesTable.getStyleAt(0);
/* 196:    */           }
/* 197:    */         }
/* 198:287 */         if (style != null)
/* 199:    */         {
/* 200:288 */           this.formatIndex = style.getDataFormat();
/* 201:289 */           this.formatString = style.getDataFormatString();
/* 202:290 */           if (this.formatString == null) {
/* 203:291 */             this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
/* 204:    */           }
/* 205:    */         }
/* 206:    */       }
/* 207:    */     }
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void endElement(String uri, String localName, String qName)
/* 211:    */     throws SAXException
/* 212:    */   {
/* 213:301 */     if ((uri != null) && (!uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main"))) {
/* 214:302 */       return;
/* 215:    */     }
/* 216:305 */     String thisStr = null;
/* 217:308 */     if (isTextTag(localName))
/* 218:    */     {
/* 219:309 */       this.vIsOpen = false;
/* 220:312 */       switch (1.$SwitchMap$org$apache$poi$xssf$eventusermodel$XSSFSheetXMLHandler$xssfDataType[this.nextDataType.ordinal()])
/* 221:    */       {
/* 222:    */       case 1: 
/* 223:314 */         char first = this.value.charAt(0);
/* 224:315 */         thisStr = first == '0' ? "FALSE" : "TRUE";
/* 225:316 */         break;
/* 226:    */       case 2: 
/* 227:319 */         thisStr = "ERROR:" + this.value;
/* 228:320 */         break;
/* 229:    */       case 3: 
/* 230:323 */         if (this.formulasNotResults)
/* 231:    */         {
/* 232:324 */           thisStr = this.formula.toString();
/* 233:    */         }
/* 234:    */         else
/* 235:    */         {
/* 236:326 */           String fv = this.value.toString();
/* 237:328 */           if (this.formatString != null) {
/* 238:    */             try
/* 239:    */             {
/* 240:331 */               double d = Double.parseDouble(fv);
/* 241:332 */               thisStr = this.formatter.formatRawCellContents(d, this.formatIndex, this.formatString);
/* 242:    */             }
/* 243:    */             catch (NumberFormatException e)
/* 244:    */             {
/* 245:335 */               thisStr = fv;
/* 246:    */             }
/* 247:    */           } else {
/* 248:339 */             thisStr = fv;
/* 249:    */           }
/* 250:    */         }
/* 251:342 */         break;
/* 252:    */       case 4: 
/* 253:346 */         XSSFRichTextString rtsi = new XSSFRichTextString(this.value.toString());
/* 254:347 */         thisStr = rtsi.toString();
/* 255:348 */         break;
/* 256:    */       case 5: 
/* 257:351 */         String sstIndex = this.value.toString();
/* 258:    */         try
/* 259:    */         {
/* 260:353 */           int idx = Integer.parseInt(sstIndex);
/* 261:354 */           XSSFRichTextString rtss = new XSSFRichTextString(this.sharedStringsTable.getEntryAt(idx));
/* 262:355 */           thisStr = rtss.toString();
/* 263:    */         }
/* 264:    */         catch (NumberFormatException ex)
/* 265:    */         {
/* 266:358 */           logger.log(7, new Object[] { "Failed to parse SST index '" + sstIndex, ex });
/* 267:    */         }
/* 268:    */       case 6: 
/* 269:363 */         String n = this.value.toString();
/* 270:364 */         if ((this.formatString != null) && (n.length() > 0)) {
/* 271:365 */           thisStr = this.formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
/* 272:    */         } else {
/* 273:367 */           thisStr = n;
/* 274:    */         }
/* 275:368 */         break;
/* 276:    */       default: 
/* 277:371 */         thisStr = "(TODO: Unexpected type: " + this.nextDataType + ")";
/* 278:    */       }
/* 279:376 */       checkForEmptyCellComments(EmptyCellCommentsCheckType.CELL);
/* 280:377 */       XSSFComment comment = this.commentsTable != null ? this.commentsTable.findCellComment(new CellAddress(this.cellRef)) : null;
/* 281:    */       
/* 282:    */ 
/* 283:380 */       this.output.cell(this.cellRef, thisStr, comment);
/* 284:    */     }
/* 285:381 */     else if ("f".equals(localName))
/* 286:    */     {
/* 287:382 */       this.fIsOpen = false;
/* 288:    */     }
/* 289:383 */     else if ("is".equals(localName))
/* 290:    */     {
/* 291:384 */       this.isIsOpen = false;
/* 292:    */     }
/* 293:385 */     else if ("row".equals(localName))
/* 294:    */     {
/* 295:387 */       checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_ROW);
/* 296:    */       
/* 297:    */ 
/* 298:390 */       this.output.endRow(this.rowNum);
/* 299:    */       
/* 300:    */ 
/* 301:393 */       this.nextRowNum = (this.rowNum + 1);
/* 302:    */     }
/* 303:394 */     else if ("sheetData".equals(localName))
/* 304:    */     {
/* 305:396 */       checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_SHEET_DATA);
/* 306:    */     }
/* 307:398 */     else if (("oddHeader".equals(localName)) || ("evenHeader".equals(localName)) || ("firstHeader".equals(localName)))
/* 308:    */     {
/* 309:400 */       this.hfIsOpen = false;
/* 310:401 */       this.output.headerFooter(this.headerFooter.toString(), true, localName);
/* 311:    */     }
/* 312:403 */     else if (("oddFooter".equals(localName)) || ("evenFooter".equals(localName)) || ("firstFooter".equals(localName)))
/* 313:    */     {
/* 314:405 */       this.hfIsOpen = false;
/* 315:406 */       this.output.headerFooter(this.headerFooter.toString(), false, localName);
/* 316:    */     }
/* 317:    */   }
/* 318:    */   
/* 319:    */   public void characters(char[] ch, int start, int length)
/* 320:    */     throws SAXException
/* 321:    */   {
/* 322:417 */     if (this.vIsOpen) {
/* 323:418 */       this.value.append(ch, start, length);
/* 324:    */     }
/* 325:420 */     if (this.fIsOpen) {
/* 326:421 */       this.formula.append(ch, start, length);
/* 327:    */     }
/* 328:423 */     if (this.hfIsOpen) {
/* 329:424 */       this.headerFooter.append(ch, start, length);
/* 330:    */     }
/* 331:    */   }
/* 332:    */   
/* 333:    */   private void checkForEmptyCellComments(EmptyCellCommentsCheckType type)
/* 334:    */   {
/* 335:432 */     if ((this.commentCellRefs != null) && (!this.commentCellRefs.isEmpty()))
/* 336:    */     {
/* 337:435 */       if (type == EmptyCellCommentsCheckType.END_OF_SHEET_DATA)
/* 338:    */       {
/* 339:436 */         while (!this.commentCellRefs.isEmpty()) {
/* 340:437 */           outputEmptyCellComment((CellAddress)this.commentCellRefs.remove());
/* 341:    */         }
/* 342:439 */         return;
/* 343:    */       }
/* 344:443 */       if (this.cellRef == null)
/* 345:    */       {
/* 346:444 */         if (type == EmptyCellCommentsCheckType.END_OF_ROW)
/* 347:    */         {
/* 348:445 */           while (!this.commentCellRefs.isEmpty()) {
/* 349:446 */             if (((CellAddress)this.commentCellRefs.peek()).getRow() == this.rowNum) {
/* 350:447 */               outputEmptyCellComment((CellAddress)this.commentCellRefs.remove());
/* 351:    */             } else {
/* 352:449 */               return;
/* 353:    */             }
/* 354:    */           }
/* 355:452 */           return;
/* 356:    */         }
/* 357:454 */         throw new IllegalStateException("Cell ref should be null only if there are only empty cells in the row; rowNum: " + this.rowNum);
/* 358:    */       }
/* 359:    */       CellAddress nextCommentCellRef;
/* 360:    */       do
/* 361:    */       {
/* 362:460 */         CellAddress cellRef = new CellAddress(this.cellRef);
/* 363:461 */         CellAddress peekCellRef = (CellAddress)this.commentCellRefs.peek();
/* 364:462 */         if ((type == EmptyCellCommentsCheckType.CELL) && (cellRef.equals(peekCellRef)))
/* 365:    */         {
/* 366:464 */           this.commentCellRefs.remove();
/* 367:465 */           return;
/* 368:    */         }
/* 369:468 */         int comparison = peekCellRef.compareTo(cellRef);
/* 370:469 */         if ((comparison > 0) && (type == EmptyCellCommentsCheckType.END_OF_ROW) && (peekCellRef.getRow() <= this.rowNum))
/* 371:    */         {
/* 372:470 */           CellAddress nextCommentCellRef = (CellAddress)this.commentCellRefs.remove();
/* 373:471 */           outputEmptyCellComment(nextCommentCellRef);
/* 374:    */         }
/* 375:472 */         else if ((comparison < 0) && (type == EmptyCellCommentsCheckType.CELL) && (peekCellRef.getRow() <= this.rowNum))
/* 376:    */         {
/* 377:473 */           CellAddress nextCommentCellRef = (CellAddress)this.commentCellRefs.remove();
/* 378:474 */           outputEmptyCellComment(nextCommentCellRef);
/* 379:    */         }
/* 380:    */         else
/* 381:    */         {
/* 382:476 */           nextCommentCellRef = null;
/* 383:    */         }
/* 384:479 */       } while ((nextCommentCellRef != null) && (!this.commentCellRefs.isEmpty()));
/* 385:    */     }
/* 386:    */   }
/* 387:    */   
/* 388:    */   private void outputEmptyCellComment(CellAddress cellRef)
/* 389:    */   {
/* 390:488 */     XSSFComment comment = this.commentsTable.findCellComment(cellRef);
/* 391:489 */     this.output.cell(cellRef.formatAsString(), null, comment);
/* 392:    */   }
/* 393:    */   
/* 394:    */   private static enum EmptyCellCommentsCheckType
/* 395:    */   {
/* 396:493 */     CELL,  END_OF_ROW,  END_OF_SHEET_DATA;
/* 397:    */     
/* 398:    */     private EmptyCellCommentsCheckType() {}
/* 399:    */   }
/* 400:    */   
/* 401:    */   public static abstract interface SheetContentsHandler
/* 402:    */   {
/* 403:    */     public abstract void startRow(int paramInt);
/* 404:    */     
/* 405:    */     public abstract void endRow(int paramInt);
/* 406:    */     
/* 407:    */     public abstract void cell(String paramString1, String paramString2, XSSFComment paramXSSFComment);
/* 408:    */     
/* 409:    */     public abstract void headerFooter(String paramString1, boolean paramBoolean, String paramString2);
/* 410:    */   }
/* 411:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
 * JD-Core Version:    0.7.0.1
 */