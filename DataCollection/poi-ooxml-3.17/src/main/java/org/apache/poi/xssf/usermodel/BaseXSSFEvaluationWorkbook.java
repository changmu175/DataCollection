/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   8:    */ import org.apache.poi.ss.formula.EvaluationName;
/*   9:    */ import org.apache.poi.ss.formula.EvaluationWorkbook;
/*  10:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
/*  11:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
/*  12:    */ import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
/*  13:    */ import org.apache.poi.ss.formula.FormulaParser;
/*  14:    */ import org.apache.poi.ss.formula.FormulaParsingWorkbook;
/*  15:    */ import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
/*  16:    */ import org.apache.poi.ss.formula.FormulaType;
/*  17:    */ import org.apache.poi.ss.formula.NameIdentifier;
/*  18:    */ import org.apache.poi.ss.formula.SheetIdentifier;
/*  19:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  20:    */ import org.apache.poi.ss.formula.ptg.Area3DPxg;
/*  21:    */ import org.apache.poi.ss.formula.ptg.NamePtg;
/*  22:    */ import org.apache.poi.ss.formula.ptg.NameXPtg;
/*  23:    */ import org.apache.poi.ss.formula.ptg.NameXPxg;
/*  24:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  25:    */ import org.apache.poi.ss.formula.ptg.Ref3DPxg;
/*  26:    */ import org.apache.poi.ss.formula.udf.IndexedUDFFinder;
/*  27:    */ import org.apache.poi.ss.formula.udf.UDFFinder;
/*  28:    */ import org.apache.poi.ss.usermodel.Name;
/*  29:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  30:    */ import org.apache.poi.ss.util.AreaReference;
/*  31:    */ import org.apache.poi.ss.util.CellReference;
/*  32:    */ import org.apache.poi.util.Internal;
/*  33:    */ import org.apache.poi.util.NotImplemented;
/*  34:    */ import org.apache.poi.xssf.model.ExternalLinksTable;
/*  35:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
/*  36:    */ 
/*  37:    */ @Internal
/*  38:    */ public abstract class BaseXSSFEvaluationWorkbook
/*  39:    */   implements FormulaRenderingWorkbook, EvaluationWorkbook, FormulaParsingWorkbook
/*  40:    */ {
/*  41:    */   protected final XSSFWorkbook _uBook;
/*  42: 59 */   private Map<String, XSSFTable> _tableCache = null;
/*  43:    */   
/*  44:    */   protected BaseXSSFEvaluationWorkbook(XSSFWorkbook book)
/*  45:    */   {
/*  46: 63 */     this._uBook = book;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void clearAllCachedResultValues()
/*  50:    */   {
/*  51: 71 */     this._tableCache = null;
/*  52:    */   }
/*  53:    */   
/*  54:    */   private int convertFromExternalSheetIndex(int externSheetIndex)
/*  55:    */   {
/*  56: 75 */     return externSheetIndex;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int convertFromExternSheetIndex(int externSheetIndex)
/*  60:    */   {
/*  61: 83 */     return externSheetIndex;
/*  62:    */   }
/*  63:    */   
/*  64:    */   private int convertToExternalSheetIndex(int sheetIndex)
/*  65:    */   {
/*  66: 92 */     return sheetIndex;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getExternalSheetIndex(String sheetName)
/*  70:    */   {
/*  71: 97 */     int sheetIndex = this._uBook.getSheetIndex(sheetName);
/*  72: 98 */     return convertToExternalSheetIndex(sheetIndex);
/*  73:    */   }
/*  74:    */   
/*  75:    */   private int resolveBookIndex(String bookName)
/*  76:    */   {
/*  77:103 */     if ((bookName.startsWith("[")) && (bookName.endsWith("]"))) {
/*  78:104 */       bookName = bookName.substring(1, bookName.length() - 2);
/*  79:    */     }
/*  80:    */     try
/*  81:    */     {
/*  82:109 */       return Integer.parseInt(bookName);
/*  83:    */     }
/*  84:    */     catch (NumberFormatException e)
/*  85:    */     {
/*  86:113 */       List<ExternalLinksTable> tables = this._uBook.getExternalLinksTable();
/*  87:114 */       int index = findExternalLinkIndex(bookName, tables);
/*  88:115 */       if (index != -1) {
/*  89:115 */         return index;
/*  90:    */       }
/*  91:118 */       if ((bookName.startsWith("'file:///")) && (bookName.endsWith("'")))
/*  92:    */       {
/*  93:119 */         String relBookName = bookName.substring(bookName.lastIndexOf('/') + 1);
/*  94:120 */         relBookName = relBookName.substring(0, relBookName.length() - 1);
/*  95:    */         
/*  96:    */ 
/*  97:123 */         index = findExternalLinkIndex(relBookName, tables);
/*  98:124 */         if (index != -1) {
/*  99:124 */           return index;
/* 100:    */         }
/* 101:129 */         ExternalLinksTable fakeLinkTable = new FakeExternalLinksTable(relBookName, null);
/* 102:130 */         tables.add(fakeLinkTable);
/* 103:131 */         return tables.size();
/* 104:    */       }
/* 105:135 */       throw new RuntimeException("Book not linked for filename " + bookName);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   private int findExternalLinkIndex(String bookName, List<ExternalLinksTable> tables)
/* 110:    */   {
/* 111:139 */     int i = 0;
/* 112:140 */     for (ExternalLinksTable table : tables)
/* 113:    */     {
/* 114:141 */       if (table.getLinkedFileName().equals(bookName)) {
/* 115:142 */         return i + 1;
/* 116:    */       }
/* 117:144 */       i++;
/* 118:    */     }
/* 119:146 */     return -1;
/* 120:    */   }
/* 121:    */   
/* 122:    */   private static class FakeExternalLinksTable
/* 123:    */     extends ExternalLinksTable
/* 124:    */   {
/* 125:    */     private final String fileName;
/* 126:    */     
/* 127:    */     private FakeExternalLinksTable(String fileName)
/* 128:    */     {
/* 129:151 */       this.fileName = fileName;
/* 130:    */     }
/* 131:    */     
/* 132:    */     public String getLinkedFileName()
/* 133:    */     {
/* 134:155 */       return this.fileName;
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public EvaluationName getName(String name, int sheetIndex)
/* 139:    */   {
/* 140:170 */     for (int i = 0; i < this._uBook.getNumberOfNames(); i++)
/* 141:    */     {
/* 142:171 */       XSSFName nm = this._uBook.getNameAt(i);
/* 143:172 */       String nameText = nm.getNameName();
/* 144:173 */       int nameSheetindex = nm.getSheetIndex();
/* 145:174 */       if ((name.equalsIgnoreCase(nameText)) && ((nameSheetindex == -1) || (nameSheetindex == sheetIndex))) {
/* 146:176 */         return new Name(nm, i, this);
/* 147:    */       }
/* 148:    */     }
/* 149:179 */     return sheetIndex == -1 ? null : getName(name, -1);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String getSheetName(int sheetIndex)
/* 153:    */   {
/* 154:184 */     return this._uBook.getSheetName(sheetIndex);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public EvaluationWorkbook.ExternalName getExternalName(int externSheetIndex, int externNameIndex)
/* 158:    */   {
/* 159:189 */     throw new IllegalStateException("HSSF-style external references are not supported for XSSF");
/* 160:    */   }
/* 161:    */   
/* 162:    */   public EvaluationWorkbook.ExternalName getExternalName(String nameName, String sheetName, int externalWorkbookNumber)
/* 163:    */   {
/* 164:194 */     if (externalWorkbookNumber > 0)
/* 165:    */     {
/* 166:196 */       int linkNumber = externalWorkbookNumber - 1;
/* 167:197 */       ExternalLinksTable linkTable = (ExternalLinksTable)this._uBook.getExternalLinksTable().get(linkNumber);
/* 168:199 */       for (Name name : linkTable.getDefinedNames()) {
/* 169:200 */         if (name.getNameName().equals(nameName))
/* 170:    */         {
/* 171:203 */           int nameSheetIndex = name.getSheetIndex() + 1;
/* 172:    */           
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:208 */           return new EvaluationWorkbook.ExternalName(nameName, -1, nameSheetIndex);
/* 177:    */         }
/* 178:    */       }
/* 179:211 */       throw new IllegalArgumentException("Name '" + nameName + "' not found in " + "reference to " + linkTable.getLinkedFileName());
/* 180:    */     }
/* 181:215 */     int nameIdx = this._uBook.getNameIndex(nameName);
/* 182:216 */     return new EvaluationWorkbook.ExternalName(nameName, nameIdx, 0);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public NameXPxg getNameXPtg(String name, SheetIdentifier sheet)
/* 186:    */   {
/* 187:227 */     IndexedUDFFinder udfFinder = (IndexedUDFFinder)getUDFFinder();
/* 188:228 */     FreeRefFunction func = udfFinder.findFunction(name);
/* 189:229 */     if (func != null) {
/* 190:230 */       return new NameXPxg(null, name);
/* 191:    */     }
/* 192:234 */     if (sheet == null)
/* 193:    */     {
/* 194:235 */       if (!this._uBook.getNames(name).isEmpty()) {
/* 195:236 */         return new NameXPxg(null, name);
/* 196:    */       }
/* 197:238 */       return null;
/* 198:    */     }
/* 199:240 */     if (sheet._sheetIdentifier == null)
/* 200:    */     {
/* 201:242 */       int bookIndex = resolveBookIndex(sheet._bookName);
/* 202:243 */       return new NameXPxg(bookIndex, null, name);
/* 203:    */     }
/* 204:247 */     String sheetName = sheet._sheetIdentifier.getName();
/* 205:249 */     if (sheet._bookName != null)
/* 206:    */     {
/* 207:250 */       int bookIndex = resolveBookIndex(sheet._bookName);
/* 208:251 */       return new NameXPxg(bookIndex, sheetName, name);
/* 209:    */     }
/* 210:253 */     return new NameXPxg(sheetName, name);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public Ptg get3DReferencePtg(CellReference cell, SheetIdentifier sheet)
/* 214:    */   {
/* 215:258 */     if (sheet._bookName != null)
/* 216:    */     {
/* 217:259 */       int bookIndex = resolveBookIndex(sheet._bookName);
/* 218:260 */       return new Ref3DPxg(bookIndex, sheet, cell);
/* 219:    */     }
/* 220:262 */     return new Ref3DPxg(sheet, cell);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public Ptg get3DReferencePtg(AreaReference area, SheetIdentifier sheet)
/* 224:    */   {
/* 225:267 */     if (sheet._bookName != null)
/* 226:    */     {
/* 227:268 */       int bookIndex = resolveBookIndex(sheet._bookName);
/* 228:269 */       return new Area3DPxg(bookIndex, sheet, area);
/* 229:    */     }
/* 230:271 */     return new Area3DPxg(sheet, area);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public String resolveNameXText(NameXPtg n)
/* 234:    */   {
/* 235:277 */     int idx = n.getNameIndex();
/* 236:278 */     String name = null;
/* 237:    */     
/* 238:    */ 
/* 239:281 */     IndexedUDFFinder udfFinder = (IndexedUDFFinder)getUDFFinder();
/* 240:282 */     name = udfFinder.getFunctionName(idx);
/* 241:283 */     if (name != null) {
/* 242:283 */       return name;
/* 243:    */     }
/* 244:286 */     XSSFName xname = this._uBook.getNameAt(idx);
/* 245:287 */     if (xname != null) {
/* 246:288 */       name = xname.getNameName();
/* 247:    */     }
/* 248:291 */     return name;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public EvaluationWorkbook.ExternalSheet getExternalSheet(int externSheetIndex)
/* 252:    */   {
/* 253:296 */     throw new IllegalStateException("HSSF-style external references are not supported for XSSF");
/* 254:    */   }
/* 255:    */   
/* 256:    */   public EvaluationWorkbook.ExternalSheet getExternalSheet(String firstSheetName, String lastSheetName, int externalWorkbookNumber)
/* 257:    */   {
/* 258:    */     String workbookName;
/* 259:    */     String workbookName;
/* 260:301 */     if (externalWorkbookNumber > 0)
/* 261:    */     {
/* 262:303 */       int linkNumber = externalWorkbookNumber - 1;
/* 263:304 */       ExternalLinksTable linkTable = (ExternalLinksTable)this._uBook.getExternalLinksTable().get(linkNumber);
/* 264:305 */       workbookName = linkTable.getLinkedFileName();
/* 265:    */     }
/* 266:    */     else
/* 267:    */     {
/* 268:308 */       workbookName = null;
/* 269:    */     }
/* 270:311 */     if ((lastSheetName == null) || (firstSheetName.equals(lastSheetName))) {
/* 271:312 */       return new EvaluationWorkbook.ExternalSheet(workbookName, firstSheetName);
/* 272:    */     }
/* 273:314 */     return new EvaluationWorkbook.ExternalSheetRange(workbookName, firstSheetName, lastSheetName);
/* 274:    */   }
/* 275:    */   
/* 276:    */   @NotImplemented
/* 277:    */   public int getExternalSheetIndex(String workbookName, String sheetName)
/* 278:    */   {
/* 279:320 */     throw new RuntimeException("not implemented yet");
/* 280:    */   }
/* 281:    */   
/* 282:    */   public int getSheetIndex(String sheetName)
/* 283:    */   {
/* 284:324 */     return this._uBook.getSheetIndex(sheetName);
/* 285:    */   }
/* 286:    */   
/* 287:    */   public String getSheetFirstNameByExternSheet(int externSheetIndex)
/* 288:    */   {
/* 289:329 */     int sheetIndex = convertFromExternalSheetIndex(externSheetIndex);
/* 290:330 */     return this._uBook.getSheetName(sheetIndex);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public String getSheetLastNameByExternSheet(int externSheetIndex)
/* 294:    */   {
/* 295:335 */     return getSheetFirstNameByExternSheet(externSheetIndex);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public String getNameText(NamePtg namePtg)
/* 299:    */   {
/* 300:340 */     return this._uBook.getNameAt(namePtg.getIndex()).getNameName();
/* 301:    */   }
/* 302:    */   
/* 303:    */   public EvaluationName getName(NamePtg namePtg)
/* 304:    */   {
/* 305:344 */     int ix = namePtg.getIndex();
/* 306:345 */     return new Name(this._uBook.getNameAt(ix), ix, this);
/* 307:    */   }
/* 308:    */   
/* 309:    */   public XSSFName createName()
/* 310:    */   {
/* 311:349 */     return this._uBook.createName();
/* 312:    */   }
/* 313:    */   
/* 314:    */   private static String caseInsensitive(String s)
/* 315:    */   {
/* 316:353 */     return s.toUpperCase(Locale.ROOT);
/* 317:    */   }
/* 318:    */   
/* 319:    */   private Map<String, XSSFTable> getTableCache()
/* 320:    */   {
/* 321:368 */     if (this._tableCache != null) {
/* 322:369 */       return this._tableCache;
/* 323:    */     }
/* 324:372 */     this._tableCache = new HashMap();
/* 325:374 */     for (Sheet sheet : this._uBook) {
/* 326:375 */       for (XSSFTable tbl : ((XSSFSheet)sheet).getTables())
/* 327:    */       {
/* 328:376 */         String lname = caseInsensitive(tbl.getName());
/* 329:377 */         this._tableCache.put(lname, tbl);
/* 330:    */       }
/* 331:    */     }
/* 332:380 */     return this._tableCache;
/* 333:    */   }
/* 334:    */   
/* 335:    */   public XSSFTable getTable(String name)
/* 336:    */   {
/* 337:395 */     if (name == null) {
/* 338:395 */       return null;
/* 339:    */     }
/* 340:396 */     String lname = caseInsensitive(name);
/* 341:397 */     return (XSSFTable)getTableCache().get(lname);
/* 342:    */   }
/* 343:    */   
/* 344:    */   public UDFFinder getUDFFinder()
/* 345:    */   {
/* 346:402 */     return this._uBook.getUDFFinder();
/* 347:    */   }
/* 348:    */   
/* 349:    */   public SpreadsheetVersion getSpreadsheetVersion()
/* 350:    */   {
/* 351:407 */     return SpreadsheetVersion.EXCEL2007;
/* 352:    */   }
/* 353:    */   
/* 354:    */   private static final class Name
/* 355:    */     implements EvaluationName
/* 356:    */   {
/* 357:    */     private final XSSFName _nameRecord;
/* 358:    */     private final int _index;
/* 359:    */     private final FormulaParsingWorkbook _fpBook;
/* 360:    */     
/* 361:    */     public Name(XSSFName name, int index, FormulaParsingWorkbook fpBook)
/* 362:    */     {
/* 363:417 */       this._nameRecord = name;
/* 364:418 */       this._index = index;
/* 365:419 */       this._fpBook = fpBook;
/* 366:    */     }
/* 367:    */     
/* 368:    */     public Ptg[] getNameDefinition()
/* 369:    */     {
/* 370:424 */       return FormulaParser.parse(this._nameRecord.getRefersToFormula(), this._fpBook, FormulaType.NAMEDRANGE, this._nameRecord.getSheetIndex());
/* 371:    */     }
/* 372:    */     
/* 373:    */     public String getNameText()
/* 374:    */     {
/* 375:428 */       return this._nameRecord.getNameName();
/* 376:    */     }
/* 377:    */     
/* 378:    */     public boolean hasFormula()
/* 379:    */     {
/* 380:433 */       CTDefinedName ctn = this._nameRecord.getCTName();
/* 381:434 */       String strVal = ctn.getStringValue();
/* 382:435 */       return (!ctn.getFunction()) && (strVal != null) && (strVal.length() > 0);
/* 383:    */     }
/* 384:    */     
/* 385:    */     public boolean isFunctionName()
/* 386:    */     {
/* 387:439 */       return this._nameRecord.isFunctionName();
/* 388:    */     }
/* 389:    */     
/* 390:    */     public boolean isRange()
/* 391:    */     {
/* 392:443 */       return hasFormula();
/* 393:    */     }
/* 394:    */     
/* 395:    */     public NamePtg createPtg()
/* 396:    */     {
/* 397:446 */       return new NamePtg(this._index);
/* 398:    */     }
/* 399:    */   }
/* 400:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.BaseXSSFEvaluationWorkbook
 * JD-Core Version:    0.7.0.1
 */