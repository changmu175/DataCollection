/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.regex.Matcher;
/*   5:    */ import java.util.regex.Pattern;
/*   6:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   7:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   8:    */ import org.apache.poi.ss.usermodel.Cell;
/*   9:    */ import org.apache.poi.util.StringUtil;
/*  10:    */ 
/*  11:    */ public class CellReference
/*  12:    */ {
/*  13:    */   private static final char ABSOLUTE_REFERENCE_MARKER = '$';
/*  14:    */   private static final char SHEET_NAME_DELIMITER = '!';
/*  15:    */   private static final char SPECIAL_NAME_DELIMITER = '\'';
/*  16:    */   
/*  17:    */   public static enum NameType
/*  18:    */   {
/*  19: 48 */     CELL,  NAMED_RANGE,  COLUMN,  ROW,  BAD_CELL_OR_NAMED_RANGE;
/*  20:    */     
/*  21:    */     private NameType() {}
/*  22:    */   }
/*  23:    */   
/*  24: 68 */   private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Z]+)?(\\$?[0-9]+)?", 2);
/*  25: 75 */   private static final Pattern STRICTLY_CELL_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)\\$?([0-9]+)", 2);
/*  26: 80 */   private static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)", 2);
/*  27: 85 */   private static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");
/*  28: 90 */   private static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Z][_.A-Z0-9]*", 2);
/*  29:    */   private final String _sheetName;
/*  30:    */   private final int _rowIndex;
/*  31:    */   private final int _colIndex;
/*  32:    */   private final boolean _isRowAbs;
/*  33:    */   private final boolean _isColAbs;
/*  34:    */   
/*  35:    */   public CellReference(String cellRef)
/*  36:    */   {
/*  37:109 */     if (StringUtil.endsWithIgnoreCase(cellRef, "#REF!")) {
/*  38:110 */       throw new IllegalArgumentException("Cell reference invalid: " + cellRef);
/*  39:    */     }
/*  40:113 */     CellRefParts parts = separateRefParts(cellRef);
/*  41:114 */     this._sheetName = parts.sheetName;
/*  42:    */     
/*  43:116 */     String colRef = parts.colRef;
/*  44:117 */     this._isColAbs = ((colRef.length() > 0) && (colRef.charAt(0) == '$'));
/*  45:118 */     if (this._isColAbs) {
/*  46:119 */       colRef = colRef.substring(1);
/*  47:    */     }
/*  48:121 */     if (colRef.length() == 0) {
/*  49:122 */       this._colIndex = -1;
/*  50:    */     } else {
/*  51:124 */       this._colIndex = convertColStringToIndex(colRef);
/*  52:    */     }
/*  53:127 */     String rowRef = parts.rowRef;
/*  54:128 */     this._isRowAbs = ((rowRef.length() > 0) && (rowRef.charAt(0) == '$'));
/*  55:129 */     if (this._isRowAbs) {
/*  56:130 */       rowRef = rowRef.substring(1);
/*  57:    */     }
/*  58:132 */     if (rowRef.length() == 0) {
/*  59:133 */       this._rowIndex = -1;
/*  60:    */     } else {
/*  61:136 */       this._rowIndex = (Integer.parseInt(rowRef) - 1);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public CellReference(int pRow, int pCol)
/*  66:    */   {
/*  67:141 */     this(pRow, pCol, false, false);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public CellReference(int pRow, short pCol)
/*  71:    */   {
/*  72:144 */     this(pRow, pCol & 0xFFFF, false, false);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public CellReference(Cell cell)
/*  76:    */   {
/*  77:148 */     this(cell.getRowIndex(), cell.getColumnIndex(), false, false);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol)
/*  81:    */   {
/*  82:152 */     this(null, pRow, pCol, pAbsRow, pAbsCol);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol)
/*  86:    */   {
/*  87:157 */     if (pRow < -1) {
/*  88:158 */       throw new IllegalArgumentException("row index may not be negative, but had " + pRow);
/*  89:    */     }
/*  90:160 */     if (pCol < -1) {
/*  91:161 */       throw new IllegalArgumentException("column index may not be negative, but had " + pCol);
/*  92:    */     }
/*  93:163 */     this._sheetName = pSheetName;
/*  94:164 */     this._rowIndex = pRow;
/*  95:165 */     this._colIndex = pCol;
/*  96:166 */     this._isRowAbs = pAbsRow;
/*  97:167 */     this._isColAbs = pAbsCol;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public int getRow()
/* 101:    */   {
/* 102:170 */     return this._rowIndex;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public short getCol()
/* 106:    */   {
/* 107:171 */     return (short)this._colIndex;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isRowAbsolute()
/* 111:    */   {
/* 112:172 */     return this._isRowAbs;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean isColAbsolute()
/* 116:    */   {
/* 117:173 */     return this._isColAbs;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String getSheetName()
/* 121:    */   {
/* 122:179 */     return this._sheetName;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static boolean isPartAbsolute(String part)
/* 126:    */   {
/* 127:183 */     return part.charAt(0) == '$';
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static int convertColStringToIndex(String ref)
/* 131:    */   {
/* 132:195 */     int retval = 0;
/* 133:196 */     char[] refs = ref.toUpperCase(Locale.ROOT).toCharArray();
/* 134:197 */     for (int k = 0; k < refs.length; k++)
/* 135:    */     {
/* 136:198 */       char thechar = refs[k];
/* 137:199 */       if (thechar == '$')
/* 138:    */       {
/* 139:200 */         if (k != 0) {
/* 140:201 */           throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
/* 141:    */         }
/* 142:    */       }
/* 143:    */       else {
/* 144:207 */         retval = retval * 26 + (thechar - 'A' + 1);
/* 145:    */       }
/* 146:    */     }
/* 147:209 */     return retval - 1;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion)
/* 151:    */   {
/* 152:217 */     int len = str.length();
/* 153:218 */     if (len < 1) {
/* 154:219 */       throw new IllegalArgumentException("Empty string not allowed");
/* 155:    */     }
/* 156:221 */     char firstChar = str.charAt(0);
/* 157:222 */     switch (firstChar)
/* 158:    */     {
/* 159:    */     case '$': 
/* 160:    */     case '.': 
/* 161:    */     case '_': 
/* 162:    */       break;
/* 163:    */     default: 
/* 164:228 */       if ((!Character.isLetter(firstChar)) && (!Character.isDigit(firstChar))) {
/* 165:229 */         throw new IllegalArgumentException("Invalid first char (" + firstChar + ") of cell reference or named range.  Letter expected");
/* 166:    */       }
/* 167:    */       break;
/* 168:    */     }
/* 169:233 */     if (!Character.isDigit(str.charAt(len - 1))) {
/* 170:235 */       return validateNamedRangeName(str, ssVersion);
/* 171:    */     }
/* 172:237 */     Matcher cellRefPatternMatcher = STRICTLY_CELL_REF_PATTERN.matcher(str);
/* 173:238 */     if (!cellRefPatternMatcher.matches()) {
/* 174:239 */       return validateNamedRangeName(str, ssVersion);
/* 175:    */     }
/* 176:241 */     String lettersGroup = cellRefPatternMatcher.group(1);
/* 177:242 */     String digitsGroup = cellRefPatternMatcher.group(2);
/* 178:243 */     if (cellReferenceIsWithinRange(lettersGroup, digitsGroup, ssVersion)) {
/* 179:245 */       return NameType.CELL;
/* 180:    */     }
/* 181:252 */     if (str.indexOf('$') >= 0) {
/* 182:254 */       return NameType.BAD_CELL_OR_NAMED_RANGE;
/* 183:    */     }
/* 184:256 */     return NameType.NAMED_RANGE;
/* 185:    */   }
/* 186:    */   
/* 187:    */   private static NameType validateNamedRangeName(String str, SpreadsheetVersion ssVersion)
/* 188:    */   {
/* 189:260 */     Matcher colMatcher = COLUMN_REF_PATTERN.matcher(str);
/* 190:261 */     if (colMatcher.matches())
/* 191:    */     {
/* 192:262 */       String colStr = colMatcher.group(1);
/* 193:263 */       if (isColumnWithinRange(colStr, ssVersion)) {
/* 194:264 */         return NameType.COLUMN;
/* 195:    */       }
/* 196:    */     }
/* 197:267 */     Matcher rowMatcher = ROW_REF_PATTERN.matcher(str);
/* 198:268 */     if (rowMatcher.matches())
/* 199:    */     {
/* 200:269 */       String rowStr = rowMatcher.group(1);
/* 201:270 */       if (isRowWithinRange(rowStr, ssVersion)) {
/* 202:271 */         return NameType.ROW;
/* 203:    */       }
/* 204:    */     }
/* 205:274 */     if (!NAMED_RANGE_NAME_PATTERN.matcher(str).matches()) {
/* 206:275 */       return NameType.BAD_CELL_OR_NAMED_RANGE;
/* 207:    */     }
/* 208:277 */     return NameType.NAMED_RANGE;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static boolean cellReferenceIsWithinRange(String colStr, String rowStr, SpreadsheetVersion ssVersion)
/* 212:    */   {
/* 213:318 */     if (!isColumnWithinRange(colStr, ssVersion)) {
/* 214:319 */       return false;
/* 215:    */     }
/* 216:321 */     return isRowWithinRange(rowStr, ssVersion);
/* 217:    */   }
/* 218:    */   
/* 219:    */   public static boolean isColumnWithinRange(String colStr, SpreadsheetVersion ssVersion)
/* 220:    */   {
/* 221:327 */     String lastCol = ssVersion.getLastColumnName();
/* 222:328 */     int lastColLength = lastCol.length();
/* 223:    */     
/* 224:330 */     int numberOfLetters = colStr.length();
/* 225:331 */     if (numberOfLetters > lastColLength) {
/* 226:333 */       return false;
/* 227:    */     }
/* 228:335 */     if ((numberOfLetters == lastColLength) && 
/* 229:336 */       (colStr.toUpperCase(Locale.ROOT).compareTo(lastCol) > 0)) {
/* 230:337 */       return false;
/* 231:    */     }
/* 232:343 */     return true;
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static boolean isRowWithinRange(String rowStr, SpreadsheetVersion ssVersion)
/* 236:    */   {
/* 237:353 */     int rowNum = Integer.parseInt(rowStr) - 1;
/* 238:354 */     return isRowWithinRange(rowNum, ssVersion);
/* 239:    */   }
/* 240:    */   
/* 241:    */   public static boolean isRowWithinRange(int rowNum, SpreadsheetVersion ssVersion)
/* 242:    */   {
/* 243:364 */     return (0 <= rowNum) && (rowNum <= ssVersion.getLastRowIndex());
/* 244:    */   }
/* 245:    */   
/* 246:    */   private static final class CellRefParts
/* 247:    */   {
/* 248:    */     final String sheetName;
/* 249:    */     final String rowRef;
/* 250:    */     final String colRef;
/* 251:    */     
/* 252:    */     private CellRefParts(String sheetName, String rowRef, String colRef)
/* 253:    */     {
/* 254:373 */       this.sheetName = sheetName;
/* 255:374 */       this.rowRef = (rowRef != null ? rowRef : "");
/* 256:375 */       this.colRef = (colRef != null ? colRef : "");
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   private static CellRefParts separateRefParts(String reference)
/* 261:    */   {
/* 262:390 */     int plingPos = reference.lastIndexOf('!');
/* 263:391 */     String sheetName = parseSheetName(reference, plingPos);
/* 264:392 */     String cell = reference.substring(plingPos + 1).toUpperCase(Locale.ROOT);
/* 265:393 */     Matcher matcher = CELL_REF_PATTERN.matcher(cell);
/* 266:394 */     if (!matcher.matches()) {
/* 267:395 */       throw new IllegalArgumentException("Invalid CellReference: " + reference);
/* 268:    */     }
/* 269:397 */     String col = matcher.group(1);
/* 270:398 */     String row = matcher.group(2);
/* 271:    */     
/* 272:400 */     CellRefParts cellRefParts = new CellRefParts(sheetName, row, col, null);
/* 273:401 */     return cellRefParts;
/* 274:    */   }
/* 275:    */   
/* 276:    */   private static String parseSheetName(String reference, int indexOfSheetNameDelimiter)
/* 277:    */   {
/* 278:405 */     if (indexOfSheetNameDelimiter < 0) {
/* 279:406 */       return null;
/* 280:    */     }
/* 281:409 */     boolean isQuoted = reference.charAt(0) == '\'';
/* 282:410 */     if (!isQuoted)
/* 283:    */     {
/* 284:412 */       if (!reference.contains(" ")) {
/* 285:413 */         return reference.substring(0, indexOfSheetNameDelimiter);
/* 286:    */       }
/* 287:415 */       throw new IllegalArgumentException("Sheet names containing spaces must be quoted: (" + reference + ")");
/* 288:    */     }
/* 289:418 */     int lastQuotePos = indexOfSheetNameDelimiter - 1;
/* 290:419 */     if (reference.charAt(lastQuotePos) != '\'') {
/* 291:420 */       throw new IllegalArgumentException("Mismatched quotes: (" + reference + ")");
/* 292:    */     }
/* 293:430 */     StringBuffer sb = new StringBuffer(indexOfSheetNameDelimiter);
/* 294:432 */     for (int i = 1; i < lastQuotePos; i++)
/* 295:    */     {
/* 296:433 */       char ch = reference.charAt(i);
/* 297:434 */       if (ch != '\'')
/* 298:    */       {
/* 299:435 */         sb.append(ch);
/* 300:    */       }
/* 301:438 */       else if ((i + 1 < lastQuotePos) && (reference.charAt(i + 1) == '\''))
/* 302:    */       {
/* 303:440 */         i++;
/* 304:441 */         sb.append(ch);
/* 305:    */       }
/* 306:    */       else
/* 307:    */       {
/* 308:444 */         throw new IllegalArgumentException("Bad sheet name quote escaping: (" + reference + ")");
/* 309:    */       }
/* 310:    */     }
/* 311:446 */     return sb.toString();
/* 312:    */   }
/* 313:    */   
/* 314:    */   public static String convertNumToColString(int col)
/* 315:    */   {
/* 316:457 */     int excelColNum = col + 1;
/* 317:    */     
/* 318:459 */     StringBuilder colRef = new StringBuilder(2);
/* 319:460 */     int colRemain = excelColNum;
/* 320:462 */     while (colRemain > 0)
/* 321:    */     {
/* 322:463 */       int thisPart = colRemain % 26;
/* 323:464 */       if (thisPart == 0) {
/* 324:464 */         thisPart = 26;
/* 325:    */       }
/* 326:465 */       colRemain = (colRemain - thisPart) / 26;
/* 327:    */       
/* 328:    */ 
/* 329:468 */       char colChar = (char)(thisPart + 64);
/* 330:469 */       colRef.insert(0, colChar);
/* 331:    */     }
/* 332:472 */     return colRef.toString();
/* 333:    */   }
/* 334:    */   
/* 335:    */   public String formatAsString()
/* 336:    */   {
/* 337:488 */     StringBuffer sb = new StringBuffer(32);
/* 338:489 */     if (this._sheetName != null)
/* 339:    */     {
/* 340:490 */       SheetNameFormatter.appendFormat(sb, this._sheetName);
/* 341:491 */       sb.append('!');
/* 342:    */     }
/* 343:493 */     appendCellReference(sb);
/* 344:494 */     return sb.toString();
/* 345:    */   }
/* 346:    */   
/* 347:    */   public String toString()
/* 348:    */   {
/* 349:499 */     StringBuffer sb = new StringBuffer(64);
/* 350:500 */     sb.append(getClass().getName()).append(" [");
/* 351:501 */     sb.append(formatAsString());
/* 352:502 */     sb.append("]");
/* 353:503 */     return sb.toString();
/* 354:    */   }
/* 355:    */   
/* 356:    */   public String[] getCellRefParts()
/* 357:    */   {
/* 358:516 */     return new String[] { this._sheetName, Integer.toString(this._rowIndex + 1), convertNumToColString(this._colIndex) };
/* 359:    */   }
/* 360:    */   
/* 361:    */   void appendCellReference(StringBuffer sb)
/* 362:    */   {
/* 363:528 */     if (this._colIndex != -1)
/* 364:    */     {
/* 365:529 */       if (this._isColAbs) {
/* 366:530 */         sb.append('$');
/* 367:    */       }
/* 368:532 */       sb.append(convertNumToColString(this._colIndex));
/* 369:    */     }
/* 370:534 */     if (this._rowIndex != -1)
/* 371:    */     {
/* 372:535 */       if (this._isRowAbs) {
/* 373:536 */         sb.append('$');
/* 374:    */       }
/* 375:538 */       sb.append(this._rowIndex + 1);
/* 376:    */     }
/* 377:    */   }
/* 378:    */   
/* 379:    */   public boolean equals(Object o)
/* 380:    */   {
/* 381:551 */     if (this == o) {
/* 382:552 */       return true;
/* 383:    */     }
/* 384:554 */     if (!(o instanceof CellReference)) {
/* 385:555 */       return false;
/* 386:    */     }
/* 387:557 */     CellReference cr = (CellReference)o;
/* 388:558 */     return (this._rowIndex == cr._rowIndex) && (this._colIndex == cr._colIndex) && (this._isRowAbs == cr._isRowAbs) && (this._isColAbs == cr._isColAbs) && (this._sheetName == null ? cr._sheetName == null : this._sheetName.equals(cr._sheetName));
/* 389:    */   }
/* 390:    */   
/* 391:    */   public int hashCode()
/* 392:    */   {
/* 393:569 */     int result = 17;
/* 394:570 */     result = 31 * result + this._rowIndex;
/* 395:571 */     result = 31 * result + this._colIndex;
/* 396:572 */     result = 31 * result + (this._isRowAbs ? 1 : 0);
/* 397:573 */     result = 31 * result + (this._isColAbs ? 1 : 0);
/* 398:574 */     result = 31 * result + (this._sheetName == null ? 0 : this._sheetName.hashCode());
/* 399:575 */     return result;
/* 400:    */   }
/* 401:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellReference
 * JD-Core Version:    0.7.0.1
 */