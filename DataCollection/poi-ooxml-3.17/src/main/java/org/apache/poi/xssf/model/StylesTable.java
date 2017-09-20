/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Map.Entry;
/*  14:    */ import java.util.Set;
/*  15:    */ import java.util.SortedMap;
/*  16:    */ import java.util.TreeMap;
/*  17:    */ import org.apache.poi.POIXMLDocumentPart;
/*  18:    */ import org.apache.poi.POIXMLTypeLoader;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  20:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  21:    */ import org.apache.poi.ss.usermodel.FontFamily;
/*  22:    */ import org.apache.poi.ss.usermodel.FontScheme;
/*  23:    */ import org.apache.poi.ss.usermodel.TableStyle;
/*  24:    */ import org.apache.poi.util.Internal;
/*  25:    */ import org.apache.poi.xssf.usermodel.CustomIndexedColorMap;
/*  26:    */ import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
/*  27:    */ import org.apache.poi.xssf.usermodel.IndexedColorMap;
/*  28:    */ import org.apache.poi.xssf.usermodel.XSSFBuiltinTableStyle;
/*  29:    */ import org.apache.poi.xssf.usermodel.XSSFCellStyle;
/*  30:    */ import org.apache.poi.xssf.usermodel.XSSFFactory;
/*  31:    */ import org.apache.poi.xssf.usermodel.XSSFFont;
/*  32:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  33:    */ import org.apache.poi.xssf.usermodel.XSSFTableStyle;
/*  34:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  35:    */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
/*  36:    */ import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
/*  37:    */ import org.apache.xmlbeans.XmlException;
/*  38:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
/*  39:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder.Factory;
/*  40:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders;
/*  41:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders.Factory;
/*  42:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs;
/*  43:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs.Factory;
/*  44:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs;
/*  45:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs.Factory;
/*  46:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
/*  47:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs;
/*  48:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs.Factory;
/*  49:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
/*  50:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill.Factory;
/*  51:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills;
/*  52:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills.Factory;
/*  53:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
/*  54:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont.Factory;
/*  55:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts;
/*  56:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts.Factory;
/*  57:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt;
/*  58:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts;
/*  59:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts.Factory;
/*  60:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
/*  61:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;
/*  62:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyle;
/*  63:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles;
/*  64:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
/*  65:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf.Factory;
/*  66:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType;
/*  67:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument;
/*  68:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument.Factory;
/*  69:    */ 
/*  70:    */ public class StylesTable
/*  71:    */   extends POIXMLDocumentPart
/*  72:    */ {
/*  73: 63 */   private final SortedMap<Short, String> numberFormats = new TreeMap();
/*  74: 64 */   private final List<XSSFFont> fonts = new ArrayList();
/*  75: 65 */   private final List<XSSFCellFill> fills = new ArrayList();
/*  76: 66 */   private final List<XSSFCellBorder> borders = new ArrayList();
/*  77: 67 */   private final List<CTXf> styleXfs = new ArrayList();
/*  78: 68 */   private final List<CTXf> xfs = new ArrayList();
/*  79: 70 */   private final List<CTDxf> dxfs = new ArrayList();
/*  80: 71 */   private final Map<String, TableStyle> tableStyles = new HashMap();
/*  81: 73 */   private IndexedColorMap indexedColors = new DefaultIndexedColorMap();
/*  82:    */   public static final int FIRST_CUSTOM_STYLE_ID = 165;
/*  83: 80 */   private static final int MAXIMUM_STYLE_ID = SpreadsheetVersion.EXCEL2007.getMaxCellStyles();
/*  84:    */   private static final short FIRST_USER_DEFINED_NUMBER_FORMAT_ID = 164;
/*  85: 89 */   private int MAXIMUM_NUMBER_OF_DATA_FORMATS = 250;
/*  86:    */   private StyleSheetDocument doc;
/*  87:    */   private XSSFWorkbook workbook;
/*  88:    */   private ThemesTable theme;
/*  89:    */   
/*  90:    */   public void setMaxNumberOfDataFormats(int num)
/*  91:    */   {
/*  92:100 */     if (num < getNumDataFormats())
/*  93:    */     {
/*  94:101 */       if (num < 0) {
/*  95:102 */         throw new IllegalArgumentException("Maximum Number of Data Formats must be greater than or equal to 0");
/*  96:    */       }
/*  97:104 */       throw new IllegalStateException("Cannot set the maximum number of data formats less than the current quantity.Data formats must be explicitly removed (via StylesTable.removeNumberFormat) before the limit can be decreased.");
/*  98:    */     }
/*  99:108 */     this.MAXIMUM_NUMBER_OF_DATA_FORMATS = num;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getMaxNumberOfDataFormats()
/* 103:    */   {
/* 104:118 */     return this.MAXIMUM_NUMBER_OF_DATA_FORMATS;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public StylesTable()
/* 108:    */   {
/* 109:130 */     this.doc = StyleSheetDocument.Factory.newInstance();
/* 110:131 */     this.doc.addNewStyleSheet();
/* 111:    */     
/* 112:133 */     initialize();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public StylesTable(PackagePart part)
/* 116:    */     throws IOException
/* 117:    */   {
/* 118:140 */     super(part);
/* 119:141 */     readFrom(part.getInputStream());
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setWorkbook(XSSFWorkbook wb)
/* 123:    */   {
/* 124:145 */     this.workbook = wb;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public ThemesTable getTheme()
/* 128:    */   {
/* 129:155 */     return this.theme;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setTheme(ThemesTable theme)
/* 133:    */   {
/* 134:159 */     this.theme = theme;
/* 135:161 */     if (theme != null) {
/* 136:161 */       theme.setColorMap(getIndexedColors());
/* 137:    */     }
/* 138:165 */     for (XSSFFont font : this.fonts) {
/* 139:166 */       font.setThemesTable(theme);
/* 140:    */     }
/* 141:168 */     for (XSSFCellBorder border : this.borders) {
/* 142:169 */       border.setThemesTable(theme);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void ensureThemesTable()
/* 147:    */   {
/* 148:179 */     if (this.theme != null) {
/* 149:179 */       return;
/* 150:    */     }
/* 151:181 */     setTheme((ThemesTable)this.workbook.createRelationship(XSSFRelation.THEME, XSSFFactory.getInstance()));
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void readFrom(InputStream is)
/* 155:    */     throws IOException
/* 156:    */   {
/* 157:    */     try
/* 158:    */     {
/* 159:192 */       this.doc = StyleSheetDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 160:    */       
/* 161:194 */       CTStylesheet styleSheet = this.doc.getStyleSheet();
/* 162:    */       
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:199 */       IndexedColorMap customColors = CustomIndexedColorMap.fromColors(styleSheet.getColors());
/* 167:200 */       if (customColors != null) {
/* 168:200 */         this.indexedColors = customColors;
/* 169:    */       }
/* 170:202 */       CTNumFmts ctfmts = styleSheet.getNumFmts();
/* 171:203 */       if (ctfmts != null) {
/* 172:204 */         for (CTNumFmt nfmt : ctfmts.getNumFmtArray())
/* 173:    */         {
/* 174:205 */           short formatId = (short)(int)nfmt.getNumFmtId();
/* 175:206 */           this.numberFormats.put(Short.valueOf(formatId), nfmt.getFormatCode());
/* 176:    */         }
/* 177:    */       }
/* 178:210 */       CTFonts ctfonts = styleSheet.getFonts();
/* 179:211 */       if (ctfonts != null)
/* 180:    */       {
/* 181:212 */         int idx = 0;
/* 182:213 */         for (CTFont font : ctfonts.getFontArray())
/* 183:    */         {
/* 184:215 */           XSSFFont f = new XSSFFont(font, idx, this.indexedColors);
/* 185:216 */           this.fonts.add(f);
/* 186:217 */           idx++;
/* 187:    */         }
/* 188:    */       }
/* 189:220 */       CTFills ctfills = styleSheet.getFills();
/* 190:221 */       if (ctfills != null) {
/* 191:222 */         for (CTFill fill : ctfills.getFillArray()) {
/* 192:223 */           this.fills.add(new XSSFCellFill(fill, this.indexedColors));
/* 193:    */         }
/* 194:    */       }
/* 195:227 */       CTBorders ctborders = styleSheet.getBorders();
/* 196:228 */       if (ctborders != null) {
/* 197:229 */         for (CTBorder border : ctborders.getBorderArray()) {
/* 198:230 */           this.borders.add(new XSSFCellBorder(border, this.indexedColors));
/* 199:    */         }
/* 200:    */       }
/* 201:234 */       CTCellXfs cellXfs = styleSheet.getCellXfs();
/* 202:235 */       if (cellXfs != null) {
/* 203:235 */         this.xfs.addAll(Arrays.asList(cellXfs.getXfArray()));
/* 204:    */       }
/* 205:237 */       CTCellStyleXfs cellStyleXfs = styleSheet.getCellStyleXfs();
/* 206:238 */       if (cellStyleXfs != null) {
/* 207:238 */         this.styleXfs.addAll(Arrays.asList(cellStyleXfs.getXfArray()));
/* 208:    */       }
/* 209:240 */       styleDxfs = styleSheet.getDxfs();
/* 210:241 */       if (styleDxfs != null) {
/* 211:241 */         this.dxfs.addAll(Arrays.asList(styleDxfs.getDxfArray()));
/* 212:    */       }
/* 213:243 */       CTTableStyles ctTableStyles = styleSheet.getTableStyles();
/* 214:244 */       if (ctTableStyles != null)
/* 215:    */       {
/* 216:245 */         idx = 0;
/* 217:246 */         for (CTTableStyle style : Arrays.asList(ctTableStyles.getTableStyleArray()))
/* 218:    */         {
/* 219:247 */           this.tableStyles.put(style.getName(), new XSSFTableStyle(idx, styleDxfs, style, this.indexedColors));
/* 220:248 */           idx++;
/* 221:    */         }
/* 222:    */       }
/* 223:    */     }
/* 224:    */     catch (XmlException e)
/* 225:    */     {
/* 226:    */       CTDxfs styleDxfs;
/* 227:    */       int idx;
/* 228:253 */       throw new IOException(e.getLocalizedMessage());
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public String getNumberFormatAt(short fmtId)
/* 233:    */   {
/* 234:268 */     return (String)this.numberFormats.get(Short.valueOf(fmtId));
/* 235:    */   }
/* 236:    */   
/* 237:    */   private short getNumberFormatId(String fmt)
/* 238:    */   {
/* 239:273 */     for (Entry<Short, String> numFmt : this.numberFormats.entrySet()) {
/* 240:274 */       if (((String)numFmt.getValue()).equals(fmt)) {
/* 241:275 */         return ((Short)numFmt.getKey()).shortValue();
/* 242:    */       }
/* 243:    */     }
/* 244:278 */     throw new IllegalStateException("Number format not in style table: " + fmt);
/* 245:    */   }
/* 246:    */   
/* 247:    */   public int putNumberFormat(String fmt)
/* 248:    */   {
/* 249:293 */     if (this.numberFormats.containsValue(fmt)) {
/* 250:    */       try
/* 251:    */       {
/* 252:295 */         return getNumberFormatId(fmt);
/* 253:    */       }
/* 254:    */       catch (IllegalStateException e)
/* 255:    */       {
/* 256:297 */         throw new IllegalStateException("Found the format, but couldn't figure out where - should never happen!");
/* 257:    */       }
/* 258:    */     }
/* 259:302 */     if (this.numberFormats.size() >= this.MAXIMUM_NUMBER_OF_DATA_FORMATS) {
/* 260:303 */       throw new IllegalStateException("The maximum number of Data Formats was exceeded. You can define up to " + this.MAXIMUM_NUMBER_OF_DATA_FORMATS + " formats in a .xlsx Workbook.");
/* 261:    */     }
/* 262:    */     short formatIndex;
/* 263:    */     short formatIndex;
/* 264:309 */     if (this.numberFormats.isEmpty())
/* 265:    */     {
/* 266:310 */       formatIndex = 164;
/* 267:    */     }
/* 268:    */     else
/* 269:    */     {
/* 270:317 */       short nextKey = (short)(((Short)this.numberFormats.lastKey()).shortValue() + 1);
/* 271:318 */       if (nextKey < 0) {
/* 272:319 */         throw new IllegalStateException("Cowardly avoiding creating a number format with a negative id.This is probably due to arithmetic overflow.");
/* 273:    */       }
/* 274:323 */       formatIndex = (short)Math.max(nextKey, 164);
/* 275:    */     }
/* 276:326 */     this.numberFormats.put(Short.valueOf(formatIndex), fmt);
/* 277:327 */     return formatIndex;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void putNumberFormat(short index, String fmt)
/* 281:    */   {
/* 282:341 */     this.numberFormats.put(Short.valueOf(index), fmt);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public boolean removeNumberFormat(short index)
/* 286:    */   {
/* 287:352 */     String fmt = (String)this.numberFormats.remove(Short.valueOf(index));
/* 288:353 */     boolean removed = fmt != null;
/* 289:354 */     if (removed) {
/* 290:355 */       for (CTXf style : this.xfs) {
/* 291:356 */         if ((style.isSetNumFmtId()) && (style.getNumFmtId() == index))
/* 292:    */         {
/* 293:357 */           style.unsetApplyNumberFormat();
/* 294:358 */           style.unsetNumFmtId();
/* 295:    */         }
/* 296:    */       }
/* 297:    */     }
/* 298:362 */     return removed;
/* 299:    */   }
/* 300:    */   
/* 301:    */   public boolean removeNumberFormat(String fmt)
/* 302:    */   {
/* 303:373 */     short id = getNumberFormatId(fmt);
/* 304:374 */     return removeNumberFormat(id);
/* 305:    */   }
/* 306:    */   
/* 307:    */   public XSSFFont getFontAt(int idx)
/* 308:    */   {
/* 309:378 */     return (XSSFFont)this.fonts.get(idx);
/* 310:    */   }
/* 311:    */   
/* 312:    */   public int putFont(XSSFFont font, boolean forceRegistration)
/* 313:    */   {
/* 314:392 */     int idx = -1;
/* 315:393 */     if (!forceRegistration) {
/* 316:394 */       idx = this.fonts.indexOf(font);
/* 317:    */     }
/* 318:397 */     if (idx != -1) {
/* 319:398 */       return idx;
/* 320:    */     }
/* 321:401 */     idx = this.fonts.size();
/* 322:402 */     this.fonts.add(font);
/* 323:403 */     return idx;
/* 324:    */   }
/* 325:    */   
/* 326:    */   public int putFont(XSSFFont font)
/* 327:    */   {
/* 328:406 */     return putFont(font, false);
/* 329:    */   }
/* 330:    */   
/* 331:    */   public XSSFCellStyle getStyleAt(int idx)
/* 332:    */   {
/* 333:415 */     int styleXfId = 0;
/* 334:417 */     if ((idx < 0) || (idx >= this.xfs.size())) {
/* 335:419 */       return null;
/* 336:    */     }
/* 337:422 */     if (((CTXf)this.xfs.get(idx)).getXfId() > 0L) {
/* 338:423 */       styleXfId = (int)((CTXf)this.xfs.get(idx)).getXfId();
/* 339:    */     }
/* 340:426 */     return new XSSFCellStyle(idx, styleXfId, this, this.theme);
/* 341:    */   }
/* 342:    */   
/* 343:    */   public int putStyle(XSSFCellStyle style)
/* 344:    */   {
/* 345:429 */     CTXf mainXF = style.getCoreXf();
/* 346:431 */     if (!this.xfs.contains(mainXF)) {
/* 347:432 */       this.xfs.add(mainXF);
/* 348:    */     }
/* 349:434 */     return this.xfs.indexOf(mainXF);
/* 350:    */   }
/* 351:    */   
/* 352:    */   public XSSFCellBorder getBorderAt(int idx)
/* 353:    */   {
/* 354:438 */     return (XSSFCellBorder)this.borders.get(idx);
/* 355:    */   }
/* 356:    */   
/* 357:    */   public int putBorder(XSSFCellBorder border)
/* 358:    */   {
/* 359:449 */     int idx = this.borders.indexOf(border);
/* 360:450 */     if (idx != -1) {
/* 361:451 */       return idx;
/* 362:    */     }
/* 363:453 */     this.borders.add(border);
/* 364:454 */     border.setThemesTable(this.theme);
/* 365:455 */     return this.borders.size() - 1;
/* 366:    */   }
/* 367:    */   
/* 368:    */   public XSSFCellFill getFillAt(int idx)
/* 369:    */   {
/* 370:459 */     return (XSSFCellFill)this.fills.get(idx);
/* 371:    */   }
/* 372:    */   
/* 373:    */   public List<XSSFCellBorder> getBorders()
/* 374:    */   {
/* 375:463 */     return Collections.unmodifiableList(this.borders);
/* 376:    */   }
/* 377:    */   
/* 378:    */   public List<XSSFCellFill> getFills()
/* 379:    */   {
/* 380:467 */     return Collections.unmodifiableList(this.fills);
/* 381:    */   }
/* 382:    */   
/* 383:    */   public List<XSSFFont> getFonts()
/* 384:    */   {
/* 385:471 */     return Collections.unmodifiableList(this.fonts);
/* 386:    */   }
/* 387:    */   
/* 388:    */   public Map<Short, String> getNumberFormats()
/* 389:    */   {
/* 390:475 */     return Collections.unmodifiableMap(this.numberFormats);
/* 391:    */   }
/* 392:    */   
/* 393:    */   public int putFill(XSSFCellFill fill)
/* 394:    */   {
/* 395:486 */     int idx = this.fills.indexOf(fill);
/* 396:487 */     if (idx != -1) {
/* 397:488 */       return idx;
/* 398:    */     }
/* 399:490 */     this.fills.add(fill);
/* 400:491 */     return this.fills.size() - 1;
/* 401:    */   }
/* 402:    */   
/* 403:    */   @Internal
/* 404:    */   public CTXf getCellXfAt(int idx)
/* 405:    */   {
/* 406:496 */     return (CTXf)this.xfs.get(idx);
/* 407:    */   }
/* 408:    */   
/* 409:    */   @Internal
/* 410:    */   public int putCellXf(CTXf cellXf)
/* 411:    */   {
/* 412:508 */     this.xfs.add(cellXf);
/* 413:509 */     return this.xfs.size();
/* 414:    */   }
/* 415:    */   
/* 416:    */   @Internal
/* 417:    */   public void replaceCellXfAt(int idx, CTXf cellXf)
/* 418:    */   {
/* 419:514 */     this.xfs.set(idx, cellXf);
/* 420:    */   }
/* 421:    */   
/* 422:    */   @Internal
/* 423:    */   public CTXf getCellStyleXfAt(int idx)
/* 424:    */   {
/* 425:    */     try
/* 426:    */     {
/* 427:520 */       return (CTXf)this.styleXfs.get(idx);
/* 428:    */     }
/* 429:    */     catch (IndexOutOfBoundsException e) {}
/* 430:523 */     return null;
/* 431:    */   }
/* 432:    */   
/* 433:    */   @Internal
/* 434:    */   public int putCellStyleXf(CTXf cellStyleXf)
/* 435:    */   {
/* 436:536 */     this.styleXfs.add(cellStyleXf);
/* 437:    */     
/* 438:538 */     return this.styleXfs.size();
/* 439:    */   }
/* 440:    */   
/* 441:    */   @Internal
/* 442:    */   protected void replaceCellStyleXfAt(int idx, CTXf cellStyleXf)
/* 443:    */   {
/* 444:543 */     this.styleXfs.set(idx, cellStyleXf);
/* 445:    */   }
/* 446:    */   
/* 447:    */   public int getNumCellStyles()
/* 448:    */   {
/* 449:552 */     return this.xfs.size();
/* 450:    */   }
/* 451:    */   
/* 452:    */   public int getNumDataFormats()
/* 453:    */   {
/* 454:559 */     return this.numberFormats.size();
/* 455:    */   }
/* 456:    */   
/* 457:    */   @Internal
/* 458:    */   int _getXfsSize()
/* 459:    */   {
/* 460:567 */     return this.xfs.size();
/* 461:    */   }
/* 462:    */   
/* 463:    */   @Internal
/* 464:    */   public int _getStyleXfsSize()
/* 465:    */   {
/* 466:574 */     return this.styleXfs.size();
/* 467:    */   }
/* 468:    */   
/* 469:    */   @Internal
/* 470:    */   public CTStylesheet getCTStylesheet()
/* 471:    */   {
/* 472:582 */     return this.doc.getStyleSheet();
/* 473:    */   }
/* 474:    */   
/* 475:    */   @Internal
/* 476:    */   public int _getDXfsSize()
/* 477:    */   {
/* 478:587 */     return this.dxfs.size();
/* 479:    */   }
/* 480:    */   
/* 481:    */   public void writeTo(OutputStream out)
/* 482:    */     throws IOException
/* 483:    */   {
/* 484:601 */     CTStylesheet styleSheet = this.doc.getStyleSheet();
/* 485:    */     
/* 486:    */ 
/* 487:604 */     CTNumFmts formats = CTNumFmts.Factory.newInstance();
/* 488:605 */     formats.setCount(this.numberFormats.size());
/* 489:606 */     for (Entry<Short, String> entry : this.numberFormats.entrySet())
/* 490:    */     {
/* 491:607 */       CTNumFmt ctFmt = formats.addNewNumFmt();
/* 492:608 */       ctFmt.setNumFmtId(((Short)entry.getKey()).shortValue());
/* 493:609 */       ctFmt.setFormatCode((String)entry.getValue());
/* 494:    */     }
/* 495:611 */     styleSheet.setNumFmts(formats);
/* 496:    */     
/* 497:    */ 
/* 498:    */ 
/* 499:615 */     CTFonts ctFonts = styleSheet.getFonts();
/* 500:616 */     if (ctFonts == null) {
/* 501:617 */       ctFonts = CTFonts.Factory.newInstance();
/* 502:    */     }
/* 503:619 */     ctFonts.setCount(this.fonts.size());
/* 504:620 */     CTFont[] ctfnt = new CTFont[this.fonts.size()];
/* 505:621 */     int idx = 0;
/* 506:    */     XSSFFont f;
/* 507:622 */     for (Iterator i$ = this.fonts.iterator(); i$.hasNext(); ctfnt[(idx++)] = f.getCTFont()) {
/* 508:622 */       f = (XSSFFont)i$.next();
/* 509:    */     }
/* 510:623 */     ctFonts.setFontArray(ctfnt);
/* 511:624 */     styleSheet.setFonts(ctFonts);
/* 512:    */     
/* 513:    */ 
/* 514:627 */     CTFills ctFills = styleSheet.getFills();
/* 515:628 */     if (ctFills == null) {
/* 516:629 */       ctFills = CTFills.Factory.newInstance();
/* 517:    */     }
/* 518:631 */     ctFills.setCount(this.fills.size());
/* 519:632 */     CTFill[] ctf = new CTFill[this.fills.size()];
/* 520:633 */     idx = 0;
/* 521:    */     XSSFCellFill f;
/* 522:634 */     for (Iterator i$ = this.fills.iterator(); i$.hasNext(); ctf[(idx++)] = f.getCTFill()) {
/* 523:634 */       f = (XSSFCellFill)i$.next();
/* 524:    */     }
/* 525:635 */     ctFills.setFillArray(ctf);
/* 526:636 */     styleSheet.setFills(ctFills);
/* 527:    */     
/* 528:    */ 
/* 529:639 */     CTBorders ctBorders = styleSheet.getBorders();
/* 530:640 */     if (ctBorders == null) {
/* 531:641 */       ctBorders = CTBorders.Factory.newInstance();
/* 532:    */     }
/* 533:643 */     ctBorders.setCount(this.borders.size());
/* 534:644 */     CTBorder[] ctb = new CTBorder[this.borders.size()];
/* 535:645 */     idx = 0;
/* 536:    */     XSSFCellBorder b;
/* 537:646 */     for (Iterator i$ = this.borders.iterator(); i$.hasNext(); ctb[(idx++)] = b.getCTBorder()) {
/* 538:646 */       b = (XSSFCellBorder)i$.next();
/* 539:    */     }
/* 540:647 */     ctBorders.setBorderArray(ctb);
/* 541:648 */     styleSheet.setBorders(ctBorders);
/* 542:651 */     if (this.xfs.size() > 0)
/* 543:    */     {
/* 544:652 */       CTCellXfs ctXfs = styleSheet.getCellXfs();
/* 545:653 */       if (ctXfs == null) {
/* 546:654 */         ctXfs = CTCellXfs.Factory.newInstance();
/* 547:    */       }
/* 548:656 */       ctXfs.setCount(this.xfs.size());
/* 549:657 */       ctXfs.setXfArray((CTXf[])this.xfs.toArray(new CTXf[this.xfs.size()]));
/* 550:    */       
/* 551:    */ 
/* 552:660 */       styleSheet.setCellXfs(ctXfs);
/* 553:    */     }
/* 554:664 */     if (this.styleXfs.size() > 0)
/* 555:    */     {
/* 556:665 */       CTCellStyleXfs ctSXfs = styleSheet.getCellStyleXfs();
/* 557:666 */       if (ctSXfs == null) {
/* 558:667 */         ctSXfs = CTCellStyleXfs.Factory.newInstance();
/* 559:    */       }
/* 560:669 */       ctSXfs.setCount(this.styleXfs.size());
/* 561:670 */       ctSXfs.setXfArray((CTXf[])this.styleXfs.toArray(new CTXf[this.styleXfs.size()]));
/* 562:    */       
/* 563:    */ 
/* 564:673 */       styleSheet.setCellStyleXfs(ctSXfs);
/* 565:    */     }
/* 566:677 */     if (this.dxfs.size() > 0)
/* 567:    */     {
/* 568:678 */       CTDxfs ctDxfs = styleSheet.getDxfs();
/* 569:679 */       if (ctDxfs == null) {
/* 570:680 */         ctDxfs = CTDxfs.Factory.newInstance();
/* 571:    */       }
/* 572:682 */       ctDxfs.setCount(this.dxfs.size());
/* 573:683 */       ctDxfs.setDxfArray((CTDxf[])this.dxfs.toArray(new CTDxf[this.dxfs.size()]));
/* 574:684 */       styleSheet.setDxfs(ctDxfs);
/* 575:    */     }
/* 576:688 */     this.doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 577:    */   }
/* 578:    */   
/* 579:    */   protected void commit()
/* 580:    */     throws IOException
/* 581:    */   {
/* 582:693 */     PackagePart part = getPackagePart();
/* 583:694 */     OutputStream out = part.getOutputStream();
/* 584:695 */     writeTo(out);
/* 585:696 */     out.close();
/* 586:    */   }
/* 587:    */   
/* 588:    */   private void initialize()
/* 589:    */   {
/* 590:701 */     XSSFFont xssfFont = createDefaultFont();
/* 591:702 */     this.fonts.add(xssfFont);
/* 592:    */     
/* 593:704 */     CTFill[] ctFill = createDefaultFills();
/* 594:705 */     this.fills.add(new XSSFCellFill(ctFill[0], this.indexedColors));
/* 595:706 */     this.fills.add(new XSSFCellFill(ctFill[1], this.indexedColors));
/* 596:    */     
/* 597:708 */     CTBorder ctBorder = createDefaultBorder();
/* 598:709 */     this.borders.add(new XSSFCellBorder(ctBorder));
/* 599:    */     
/* 600:711 */     CTXf styleXf = createDefaultXf();
/* 601:712 */     this.styleXfs.add(styleXf);
/* 602:713 */     CTXf xf = createDefaultXf();
/* 603:714 */     xf.setXfId(0L);
/* 604:715 */     this.xfs.add(xf);
/* 605:    */   }
/* 606:    */   
/* 607:    */   private static CTXf createDefaultXf()
/* 608:    */   {
/* 609:719 */     CTXf ctXf = CTXf.Factory.newInstance();
/* 610:720 */     ctXf.setNumFmtId(0L);
/* 611:721 */     ctXf.setFontId(0L);
/* 612:722 */     ctXf.setFillId(0L);
/* 613:723 */     ctXf.setBorderId(0L);
/* 614:724 */     return ctXf;
/* 615:    */   }
/* 616:    */   
/* 617:    */   private static CTBorder createDefaultBorder()
/* 618:    */   {
/* 619:727 */     CTBorder ctBorder = CTBorder.Factory.newInstance();
/* 620:728 */     ctBorder.addNewBottom();
/* 621:729 */     ctBorder.addNewTop();
/* 622:730 */     ctBorder.addNewLeft();
/* 623:731 */     ctBorder.addNewRight();
/* 624:732 */     ctBorder.addNewDiagonal();
/* 625:733 */     return ctBorder;
/* 626:    */   }
/* 627:    */   
/* 628:    */   private static CTFill[] createDefaultFills()
/* 629:    */   {
/* 630:738 */     CTFill[] ctFill = { CTFill.Factory.newInstance(), CTFill.Factory.newInstance() };
/* 631:739 */     ctFill[0].addNewPatternFill().setPatternType(STPatternType.NONE);
/* 632:740 */     ctFill[1].addNewPatternFill().setPatternType(STPatternType.DARK_GRAY);
/* 633:741 */     return ctFill;
/* 634:    */   }
/* 635:    */   
/* 636:    */   private static XSSFFont createDefaultFont()
/* 637:    */   {
/* 638:745 */     CTFont ctFont = CTFont.Factory.newInstance();
/* 639:746 */     XSSFFont xssfFont = new XSSFFont(ctFont, 0, null);
/* 640:747 */     xssfFont.setFontHeightInPoints((short)11);
/* 641:748 */     xssfFont.setColor(XSSFFont.DEFAULT_FONT_COLOR);
/* 642:749 */     xssfFont.setFontName("Calibri");
/* 643:750 */     xssfFont.setFamily(FontFamily.SWISS);
/* 644:751 */     xssfFont.setScheme(FontScheme.MINOR);
/* 645:752 */     return xssfFont;
/* 646:    */   }
/* 647:    */   
/* 648:    */   @Internal
/* 649:    */   public CTDxf getDxfAt(int idx)
/* 650:    */   {
/* 651:757 */     return (CTDxf)this.dxfs.get(idx);
/* 652:    */   }
/* 653:    */   
/* 654:    */   @Internal
/* 655:    */   public int putDxf(CTDxf dxf)
/* 656:    */   {
/* 657:769 */     this.dxfs.add(dxf);
/* 658:770 */     return this.dxfs.size();
/* 659:    */   }
/* 660:    */   
/* 661:    */   public TableStyle getExplicitTableStyle(String name)
/* 662:    */   {
/* 663:781 */     return (TableStyle)this.tableStyles.get(name);
/* 664:    */   }
/* 665:    */   
/* 666:    */   public Set<String> getExplicitTableStyleNames()
/* 667:    */   {
/* 668:789 */     return this.tableStyles.keySet();
/* 669:    */   }
/* 670:    */   
/* 671:    */   public TableStyle getTableStyle(String name)
/* 672:    */   {
/* 673:799 */     if (name == null) {
/* 674:799 */       return null;
/* 675:    */     }
/* 676:    */     try
/* 677:    */     {
/* 678:801 */       return XSSFBuiltinTableStyle.valueOf(name).getStyle();
/* 679:    */     }
/* 680:    */     catch (IllegalArgumentException e) {}
/* 681:803 */     return getExplicitTableStyle(name);
/* 682:    */   }
/* 683:    */   
/* 684:    */   public XSSFCellStyle createCellStyle()
/* 685:    */   {
/* 686:814 */     if (getNumCellStyles() > MAXIMUM_STYLE_ID) {
/* 687:815 */       throw new IllegalStateException("The maximum number of Cell Styles was exceeded. You can define up to " + MAXIMUM_STYLE_ID + " style in a .xlsx Workbook");
/* 688:    */     }
/* 689:819 */     int xfSize = this.styleXfs.size();
/* 690:820 */     CTXf xf = CTXf.Factory.newInstance();
/* 691:821 */     xf.setNumFmtId(0L);
/* 692:822 */     xf.setFontId(0L);
/* 693:823 */     xf.setFillId(0L);
/* 694:824 */     xf.setBorderId(0L);
/* 695:825 */     xf.setXfId(0L);
/* 696:826 */     int indexXf = putCellXf(xf);
/* 697:827 */     return new XSSFCellStyle(indexXf - 1, xfSize - 1, this, this.theme);
/* 698:    */   }
/* 699:    */   
/* 700:    */   public XSSFFont findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline)
/* 701:    */   {
/* 702:834 */     for (XSSFFont font : this.fonts) {
/* 703:835 */       if ((font.getBold() == bold) && (font.getColor() == color) && (font.getFontHeight() == fontHeight) && (font.getFontName().equals(name)) && (font.getItalic() == italic) && (font.getStrikeout() == strikeout) && (font.getTypeOffset() == typeOffset) && (font.getUnderline() == underline)) {
/* 704:844 */         return font;
/* 705:    */       }
/* 706:    */     }
/* 707:847 */     return null;
/* 708:    */   }
/* 709:    */   
/* 710:    */   public IndexedColorMap getIndexedColors()
/* 711:    */   {
/* 712:854 */     return this.indexedColors;
/* 713:    */   }
/* 714:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.model.StylesTable

 * JD-Core Version:    0.7.0.1

 */