/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.StringTokenizer;
/*   6:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   7:    */ import org.apache.poi.util.Removal;
/*   8:    */ 
/*   9:    */ public class AreaReference
/*  10:    */ {
/*  11:    */   private static final char SHEET_NAME_DELIMITER = '!';
/*  12:    */   private static final char CELL_DELIMITER = ':';
/*  13:    */   private static final char SPECIAL_NAME_DELIMITER = '\'';
/*  14: 35 */   private static final SpreadsheetVersion DEFAULT_SPREADSHEET_VERSION = SpreadsheetVersion.EXCEL97;
/*  15:    */   private final CellReference _firstCell;
/*  16:    */   private final CellReference _lastCell;
/*  17:    */   private final boolean _isSingleCell;
/*  18:    */   private final SpreadsheetVersion _version;
/*  19:    */   
/*  20:    */   public AreaReference(String reference, SpreadsheetVersion version)
/*  21:    */   {
/*  22: 48 */     this._version = (null != version ? version : DEFAULT_SPREADSHEET_VERSION);
/*  23: 49 */     if (!isContiguous(reference)) {
/*  24: 50 */       throw new IllegalArgumentException("References passed to the AreaReference must be contiguous, use generateContiguous(ref) if you have non-contiguous references");
/*  25:    */     }
/*  26: 55 */     String[] parts = separateAreaRefs(reference);
/*  27: 56 */     String part0 = parts[0];
/*  28: 57 */     if (parts.length == 1)
/*  29:    */     {
/*  30: 60 */       this._firstCell = new CellReference(part0);
/*  31:    */       
/*  32: 62 */       this._lastCell = this._firstCell;
/*  33: 63 */       this._isSingleCell = true;
/*  34: 64 */       return;
/*  35:    */     }
/*  36: 66 */     if (parts.length != 2) {
/*  37: 67 */       throw new IllegalArgumentException("Bad area ref '" + reference + "'");
/*  38:    */     }
/*  39: 70 */     String part1 = parts[1];
/*  40: 71 */     if (isPlainColumn(part0))
/*  41:    */     {
/*  42: 72 */       if (!isPlainColumn(part1)) {
/*  43: 73 */         throw new RuntimeException("Bad area ref '" + reference + "'");
/*  44:    */       }
/*  45: 79 */       boolean firstIsAbs = CellReference.isPartAbsolute(part0);
/*  46: 80 */       boolean lastIsAbs = CellReference.isPartAbsolute(part1);
/*  47:    */       
/*  48: 82 */       int col0 = CellReference.convertColStringToIndex(part0);
/*  49: 83 */       int col1 = CellReference.convertColStringToIndex(part1);
/*  50:    */       
/*  51: 85 */       this._firstCell = new CellReference(0, col0, true, firstIsAbs);
/*  52: 86 */       this._lastCell = new CellReference(65535, col1, true, lastIsAbs);
/*  53: 87 */       this._isSingleCell = false;
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57: 90 */       this._firstCell = new CellReference(part0);
/*  58: 91 */       this._lastCell = new CellReference(part1);
/*  59: 92 */       this._isSingleCell = part0.equals(part1);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static boolean isPlainColumn(String refPart)
/*  64:    */   {
/*  65: 97 */     for (int i = refPart.length() - 1; i >= 0; i--)
/*  66:    */     {
/*  67: 98 */       int ch = refPart.charAt(i);
/*  68: 99 */       if ((ch != 36) || (i != 0)) {
/*  69:102 */         if ((ch < 65) || (ch > 90)) {
/*  70:103 */           return false;
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:106 */     return true;
/*  75:    */   }
/*  76:    */   
/*  77:    */   @Deprecated
/*  78:    */   @Removal(version="3.19")
/*  79:    */   public AreaReference(CellReference topLeft, CellReference botRight)
/*  80:    */   {
/*  81:116 */     this(topLeft, botRight, DEFAULT_SPREADSHEET_VERSION);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public AreaReference(CellReference topLeft, CellReference botRight, SpreadsheetVersion version)
/*  85:    */   {
/*  86:123 */     this._version = (null != version ? version : DEFAULT_SPREADSHEET_VERSION);
/*  87:124 */     boolean swapRows = topLeft.getRow() > botRight.getRow();
/*  88:125 */     boolean swapCols = topLeft.getCol() > botRight.getCol();
/*  89:126 */     if ((swapRows) || (swapCols))
/*  90:    */     {
/*  91:    */       boolean lastRowAbs;
/*  92:    */       int firstRow;
/*  93:    */       boolean firstRowAbs;
/*  94:    */       int lastRow;
/*  95:    */       boolean lastRowAbs;
/*  96:135 */       if (swapRows)
/*  97:    */       {
/*  98:136 */         int firstRow = botRight.getRow();
/*  99:137 */         boolean firstRowAbs = botRight.isRowAbsolute();
/* 100:138 */         int lastRow = topLeft.getRow();
/* 101:139 */         lastRowAbs = topLeft.isRowAbsolute();
/* 102:    */       }
/* 103:    */       else
/* 104:    */       {
/* 105:141 */         firstRow = topLeft.getRow();
/* 106:142 */         firstRowAbs = topLeft.isRowAbsolute();
/* 107:143 */         lastRow = botRight.getRow();
/* 108:144 */         lastRowAbs = botRight.isRowAbsolute();
/* 109:    */       }
/* 110:    */       boolean lastColAbs;
/* 111:    */       int firstColumn;
/* 112:    */       boolean firstColAbs;
/* 113:    */       int lastColumn;
/* 114:    */       boolean lastColAbs;
/* 115:146 */       if (swapCols)
/* 116:    */       {
/* 117:147 */         int firstColumn = botRight.getCol();
/* 118:148 */         boolean firstColAbs = botRight.isColAbsolute();
/* 119:149 */         int lastColumn = topLeft.getCol();
/* 120:150 */         lastColAbs = topLeft.isColAbsolute();
/* 121:    */       }
/* 122:    */       else
/* 123:    */       {
/* 124:152 */         firstColumn = topLeft.getCol();
/* 125:153 */         firstColAbs = topLeft.isColAbsolute();
/* 126:154 */         lastColumn = botRight.getCol();
/* 127:155 */         lastColAbs = botRight.isColAbsolute();
/* 128:    */       }
/* 129:157 */       this._firstCell = new CellReference(firstRow, firstColumn, firstRowAbs, firstColAbs);
/* 130:158 */       this._lastCell = new CellReference(lastRow, lastColumn, lastRowAbs, lastColAbs);
/* 131:    */     }
/* 132:    */     else
/* 133:    */     {
/* 134:160 */       this._firstCell = topLeft;
/* 135:161 */       this._lastCell = botRight;
/* 136:    */     }
/* 137:163 */     this._isSingleCell = false;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static boolean isContiguous(String reference)
/* 141:    */   {
/* 142:175 */     int sheetRefEnd = reference.indexOf('!');
/* 143:176 */     if (sheetRefEnd != -1) {
/* 144:177 */       reference = reference.substring(sheetRefEnd);
/* 145:    */     }
/* 146:181 */     return !reference.contains(",");
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static AreaReference getWholeRow(SpreadsheetVersion version, String start, String end)
/* 150:    */   {
/* 151:185 */     if (null == version) {
/* 152:186 */       version = DEFAULT_SPREADSHEET_VERSION;
/* 153:    */     }
/* 154:188 */     return new AreaReference("$A" + start + ":$" + version.getLastColumnName() + end, version);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static AreaReference getWholeColumn(SpreadsheetVersion version, String start, String end)
/* 158:    */   {
/* 159:192 */     if (null == version) {
/* 160:193 */       version = DEFAULT_SPREADSHEET_VERSION;
/* 161:    */     }
/* 162:195 */     return new AreaReference(start + "$1:" + end + "$" + version.getMaxRows(), version);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static boolean isWholeColumnReference(SpreadsheetVersion version, CellReference topLeft, CellReference botRight)
/* 166:    */   {
/* 167:203 */     if (null == version) {
/* 168:204 */       version = DEFAULT_SPREADSHEET_VERSION;
/* 169:    */     }
/* 170:210 */     if ((topLeft.getRow() == 0) && (topLeft.isRowAbsolute()) && (botRight.getRow() == version.getLastRowIndex()) && (botRight.isRowAbsolute())) {
/* 171:212 */       return true;
/* 172:    */     }
/* 173:214 */     return false;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public boolean isWholeColumnReference()
/* 177:    */   {
/* 178:217 */     return isWholeColumnReference(this._version, this._firstCell, this._lastCell);
/* 179:    */   }
/* 180:    */   
/* 181:    */   @Deprecated
/* 182:    */   @Removal(version="3.19")
/* 183:    */   public static AreaReference[] generateContiguous(String reference)
/* 184:    */   {
/* 185:228 */     return generateContiguous(DEFAULT_SPREADSHEET_VERSION, reference);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public static AreaReference[] generateContiguous(SpreadsheetVersion version, String reference)
/* 189:    */   {
/* 190:236 */     if (null == version) {
/* 191:237 */       version = DEFAULT_SPREADSHEET_VERSION;
/* 192:    */     }
/* 193:239 */     List<AreaReference> refs = new ArrayList();
/* 194:240 */     StringTokenizer st = new StringTokenizer(reference, ",");
/* 195:241 */     while (st.hasMoreTokens()) {
/* 196:242 */       refs.add(new AreaReference(st.nextToken(), version));
/* 197:    */     }
/* 198:246 */     return (AreaReference[])refs.toArray(new AreaReference[refs.size()]);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean isSingleCell()
/* 202:    */   {
/* 203:253 */     return this._isSingleCell;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public CellReference getFirstCell()
/* 207:    */   {
/* 208:261 */     return this._firstCell;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public CellReference getLastCell()
/* 212:    */   {
/* 213:272 */     return this._lastCell;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public CellReference[] getAllReferencedCells()
/* 217:    */   {
/* 218:279 */     if (this._isSingleCell) {
/* 219:280 */       return new CellReference[] { this._firstCell };
/* 220:    */     }
/* 221:284 */     int minRow = Math.min(this._firstCell.getRow(), this._lastCell.getRow());
/* 222:285 */     int maxRow = Math.max(this._firstCell.getRow(), this._lastCell.getRow());
/* 223:286 */     int minCol = Math.min(this._firstCell.getCol(), this._lastCell.getCol());
/* 224:287 */     int maxCol = Math.max(this._firstCell.getCol(), this._lastCell.getCol());
/* 225:288 */     String sheetName = this._firstCell.getSheetName();
/* 226:    */     
/* 227:290 */     List<CellReference> refs = new ArrayList();
/* 228:291 */     for (int row = minRow; row <= maxRow; row++) {
/* 229:292 */       for (int col = minCol; col <= maxCol; col++)
/* 230:    */       {
/* 231:293 */         CellReference ref = new CellReference(sheetName, row, col, this._firstCell.isRowAbsolute(), this._firstCell.isColAbsolute());
/* 232:294 */         refs.add(ref);
/* 233:    */       }
/* 234:    */     }
/* 235:297 */     return (CellReference[])refs.toArray(new CellReference[refs.size()]);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public String formatAsString()
/* 239:    */   {
/* 240:315 */     if (isWholeColumnReference()) {
/* 241:316 */       return CellReference.convertNumToColString(this._firstCell.getCol()) + ":" + CellReference.convertNumToColString(this._lastCell.getCol());
/* 242:    */     }
/* 243:322 */     StringBuffer sb = new StringBuffer(32);
/* 244:323 */     sb.append(this._firstCell.formatAsString());
/* 245:324 */     if (!this._isSingleCell)
/* 246:    */     {
/* 247:325 */       sb.append(':');
/* 248:326 */       if (this._lastCell.getSheetName() == null) {
/* 249:327 */         sb.append(this._lastCell.formatAsString());
/* 250:    */       } else {
/* 251:330 */         this._lastCell.appendCellReference(sb);
/* 252:    */       }
/* 253:    */     }
/* 254:333 */     return sb.toString();
/* 255:    */   }
/* 256:    */   
/* 257:    */   public String toString()
/* 258:    */   {
/* 259:337 */     StringBuffer sb = new StringBuffer(64);
/* 260:338 */     sb.append(getClass().getName()).append(" [");
/* 261:339 */     sb.append(formatAsString());
/* 262:340 */     sb.append("]");
/* 263:341 */     return sb.toString();
/* 264:    */   }
/* 265:    */   
/* 266:    */   private static String[] separateAreaRefs(String reference)
/* 267:    */   {
/* 268:359 */     int len = reference.length();
/* 269:360 */     int delimiterPos = -1;
/* 270:361 */     boolean insideDelimitedName = false;
/* 271:362 */     for (int i = 0; i < len; i++)
/* 272:    */     {
/* 273:363 */       switch (reference.charAt(i))
/* 274:    */       {
/* 275:    */       case ':': 
/* 276:365 */         if (insideDelimitedName) {
/* 277:    */           continue;
/* 278:    */         }
/* 279:366 */         if (delimiterPos >= 0) {
/* 280:367 */           throw new IllegalArgumentException("More than one cell delimiter ':' appears in area reference '" + reference + "'");
/* 281:    */         }
/* 282:370 */         delimiterPos = i; break;
/* 283:    */       case '\'': 
/* 284:    */         break;
/* 285:    */       }
/* 286:378 */       if (!insideDelimitedName)
/* 287:    */       {
/* 288:379 */         insideDelimitedName = true;
/* 289:    */       }
/* 290:    */       else
/* 291:    */       {
/* 292:383 */         if (i >= len - 1) {
/* 293:386 */           throw new IllegalArgumentException("Area reference '" + reference + "' ends with special name delimiter '" + '\'' + "'");
/* 294:    */         }
/* 295:389 */         if (reference.charAt(i + 1) == '\'') {
/* 296:391 */           i++;
/* 297:    */         } else {
/* 298:394 */           insideDelimitedName = false;
/* 299:    */         }
/* 300:    */       }
/* 301:    */     }
/* 302:397 */     if (delimiterPos < 0) {
/* 303:398 */       return new String[] { reference };
/* 304:    */     }
/* 305:401 */     String partA = reference.substring(0, delimiterPos);
/* 306:402 */     String partB = reference.substring(delimiterPos + 1);
/* 307:403 */     if (partB.indexOf('!') >= 0) {
/* 308:408 */       throw new RuntimeException("Unexpected ! in second cell reference of '" + reference + "'");
/* 309:    */     }
/* 310:412 */     int plingPos = partA.lastIndexOf('!');
/* 311:413 */     if (plingPos < 0) {
/* 312:414 */       return new String[] { partA, partB };
/* 313:    */     }
/* 314:417 */     String sheetName = partA.substring(0, plingPos + 1);
/* 315:    */     
/* 316:419 */     return new String[] { partA, sheetName + partB };
/* 317:    */   }
/* 318:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.AreaReference
 * JD-Core Version:    0.7.0.1
 */