/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import java.util.NoSuchElementException;
/*   7:    */ import java.util.SortedMap;
/*   8:    */ import java.util.TreeMap;
/*   9:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*  10:    */ import org.apache.poi.ss.usermodel.Cell;
/*  11:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  12:    */ import org.apache.poi.ss.usermodel.CellType;
/*  13:    */ import org.apache.poi.ss.usermodel.Row;
/*  14:    */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*  15:    */ import org.apache.poi.util.Internal;
/*  16:    */ 
/*  17:    */ public class SXSSFRow
/*  18:    */   implements Row, Comparable<SXSSFRow>
/*  19:    */ {
/*  20: 39 */   private static final Boolean UNDEFINED = null;
/*  21:    */   private final SXSSFSheet _sheet;
/*  22: 42 */   private final SortedMap<Integer, SXSSFCell> _cells = new TreeMap();
/*  23: 43 */   private short _style = -1;
/*  24: 44 */   private short _height = -1;
/*  25: 45 */   private boolean _zHeight = false;
/*  26: 46 */   private int _outlineLevel = 0;
/*  27: 48 */   private Boolean _hidden = UNDEFINED;
/*  28: 49 */   private Boolean _collapsed = UNDEFINED;
/*  29:    */   
/*  30:    */   public SXSSFRow(SXSSFSheet sheet)
/*  31:    */   {
/*  32: 53 */     this._sheet = sheet;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public Iterator<Cell> allCellsIterator()
/*  36:    */   {
/*  37: 58 */     return new CellIterator();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public boolean hasCustomHeight()
/*  41:    */   {
/*  42: 62 */     return this._height != -1;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getOutlineLevel()
/*  46:    */   {
/*  47: 67 */     return this._outlineLevel;
/*  48:    */   }
/*  49:    */   
/*  50:    */   void setOutlineLevel(int level)
/*  51:    */   {
/*  52: 70 */     this._outlineLevel = level;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Boolean getHidden()
/*  56:    */   {
/*  57: 79 */     return this._hidden;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setHidden(Boolean hidden)
/*  61:    */   {
/*  62: 88 */     this._hidden = hidden;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Boolean getCollapsed()
/*  66:    */   {
/*  67: 92 */     return this._collapsed;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setCollapsed(Boolean collapsed)
/*  71:    */   {
/*  72: 96 */     this._collapsed = collapsed;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Iterator<Cell> iterator()
/*  76:    */   {
/*  77:105 */     return new FilledCellIterator();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public SXSSFCell createCell(int column)
/*  81:    */   {
/*  82:122 */     return createCell(column, CellType.BLANK);
/*  83:    */   }
/*  84:    */   
/*  85:    */   /**
/*  86:    */    * @deprecated
/*  87:    */    */
/*  88:    */   public SXSSFCell createCell(int column, int type)
/*  89:    */   {
/*  90:140 */     return createCell(column, CellType.forInt(type));
/*  91:    */   }
/*  92:    */   
/*  93:    */   public SXSSFCell createCell(int column, CellType type)
/*  94:    */   {
/*  95:156 */     checkBounds(column);
/*  96:157 */     SXSSFCell cell = new SXSSFCell(this, type);
/*  97:158 */     this._cells.put(Integer.valueOf(column), cell);
/*  98:159 */     return cell;
/*  99:    */   }
/* 100:    */   
/* 101:    */   private static void checkBounds(int cellIndex)
/* 102:    */   {
/* 103:166 */     SpreadsheetVersion v = SpreadsheetVersion.EXCEL2007;
/* 104:167 */     int maxcol = SpreadsheetVersion.EXCEL2007.getLastColumnIndex();
/* 105:168 */     if ((cellIndex < 0) || (cellIndex > maxcol)) {
/* 106:169 */       throw new IllegalArgumentException("Invalid column index (" + cellIndex + ").  Allowable column range for " + v.name() + " is (0.." + maxcol + ") or ('A'..'" + v.getLastColumnName() + "')");
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void removeCell(Cell cell)
/* 111:    */   {
/* 112:183 */     int index = getCellIndex((SXSSFCell)cell);
/* 113:184 */     this._cells.remove(Integer.valueOf(index));
/* 114:    */   }
/* 115:    */   
/* 116:    */   int getCellIndex(SXSSFCell cell)
/* 117:    */   {
/* 118:196 */     for (Map.Entry<Integer, SXSSFCell> entry : this._cells.entrySet()) {
/* 119:197 */       if (entry.getValue() == cell) {
/* 120:198 */         return ((Integer)entry.getKey()).intValue();
/* 121:    */       }
/* 122:    */     }
/* 123:201 */     return -1;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setRowNum(int rowNum)
/* 127:    */   {
/* 128:213 */     this._sheet.changeRowNum(this, rowNum);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getRowNum()
/* 132:    */   {
/* 133:224 */     return this._sheet.getRowNum(this);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public SXSSFCell getCell(int cellnum)
/* 137:    */   {
/* 138:239 */     Row.MissingCellPolicy policy = this._sheet.getWorkbook().getMissingCellPolicy();
/* 139:240 */     return getCell(cellnum, policy);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public SXSSFCell getCell(int cellnum, Row.MissingCellPolicy policy)
/* 143:    */   {
/* 144:252 */     checkBounds(cellnum);
/* 145:    */     
/* 146:254 */     SXSSFCell cell = (SXSSFCell)this._cells.get(Integer.valueOf(cellnum));
/* 147:255 */     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$Row$MissingCellPolicy[policy.ordinal()])
/* 148:    */     {
/* 149:    */     case 1: 
/* 150:257 */       return cell;
/* 151:    */     case 2: 
/* 152:259 */       boolean isBlank = (cell != null) && (cell.getCellTypeEnum() == CellType.BLANK);
/* 153:260 */       return isBlank ? null : cell;
/* 154:    */     case 3: 
/* 155:262 */       return cell == null ? createCell(cellnum, CellType.BLANK) : cell;
/* 156:    */     }
/* 157:264 */     throw new IllegalArgumentException("Illegal policy " + policy);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public short getFirstCellNum()
/* 161:    */   {
/* 162:    */     try
/* 163:    */     {
/* 164:278 */       return ((Integer)this._cells.firstKey()).shortValue();
/* 165:    */     }
/* 166:    */     catch (NoSuchElementException e) {}
/* 167:280 */     return -1;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public short getLastCellNum()
/* 171:    */   {
/* 172:306 */     return this._cells.isEmpty() ? -1 : (short)(((Integer)this._cells.lastKey()).intValue() + 1);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public int getPhysicalNumberOfCells()
/* 176:    */   {
/* 177:318 */     return this._cells.size();
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setHeight(short height)
/* 181:    */   {
/* 182:330 */     this._height = height;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setZeroHeight(boolean zHeight)
/* 186:    */   {
/* 187:341 */     this._zHeight = zHeight;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public boolean getZeroHeight()
/* 191:    */   {
/* 192:352 */     return this._zHeight;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setHeightInPoints(float height)
/* 196:    */   {
/* 197:363 */     if (height == -1.0F) {
/* 198:364 */       this._height = -1;
/* 199:    */     } else {
/* 200:366 */       this._height = ((short)(int)(height * 20.0F));
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   public short getHeight()
/* 205:    */   {
/* 206:378 */     return (short)(int)(this._height == -1 ? getSheet().getDefaultRowHeightInPoints() * 20.0F : this._height);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public float getHeightInPoints()
/* 210:    */   {
/* 211:391 */     return (float)(this._height == -1 ? getSheet().getDefaultRowHeightInPoints() : this._height / 20.0D);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public boolean isFormatted()
/* 215:    */   {
/* 216:401 */     return this._style > -1;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public CellStyle getRowStyle()
/* 220:    */   {
/* 221:410 */     if (!isFormatted()) {
/* 222:410 */       return null;
/* 223:    */     }
/* 224:412 */     return getSheet().getWorkbook().getCellStyleAt(this._style);
/* 225:    */   }
/* 226:    */   
/* 227:    */   @Internal
/* 228:    */   int getRowStyleIndex()
/* 229:    */   {
/* 230:417 */     return this._style;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void setRowStyle(CellStyle style)
/* 234:    */   {
/* 235:426 */     if (style == null) {
/* 236:427 */       this._style = -1;
/* 237:    */     } else {
/* 238:429 */       this._style = style.getIndex();
/* 239:    */     }
/* 240:    */   }
/* 241:    */   
/* 242:    */   public Iterator<Cell> cellIterator()
/* 243:    */   {
/* 244:439 */     return iterator();
/* 245:    */   }
/* 246:    */   
/* 247:    */   public SXSSFSheet getSheet()
/* 248:    */   {
/* 249:450 */     return this._sheet;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public class FilledCellIterator
/* 253:    */     implements Iterator<Cell>
/* 254:    */   {
/* 255:465 */     private final Iterator<SXSSFCell> iter = SXSSFRow.this._cells.values().iterator();
/* 256:    */     
/* 257:    */     public FilledCellIterator() {}
/* 258:    */     
/* 259:    */     public boolean hasNext()
/* 260:    */     {
/* 261:470 */       return this.iter.hasNext();
/* 262:    */     }
/* 263:    */     
/* 264:    */     public Cell next()
/* 265:    */       throws NoSuchElementException
/* 266:    */     {
/* 267:475 */       return (Cell)this.iter.next();
/* 268:    */     }
/* 269:    */     
/* 270:    */     public void remove()
/* 271:    */     {
/* 272:480 */       throw new UnsupportedOperationException();
/* 273:    */     }
/* 274:    */   }
/* 275:    */   
/* 276:    */   public class CellIterator
/* 277:    */     implements Iterator<Cell>
/* 278:    */   {
/* 279:492 */     final int maxColumn = SXSSFRow.this.getLastCellNum();
/* 280:493 */     int pos = 0;
/* 281:    */     
/* 282:    */     public CellIterator() {}
/* 283:    */     
/* 284:    */     public boolean hasNext()
/* 285:    */     {
/* 286:498 */       return this.pos < this.maxColumn;
/* 287:    */     }
/* 288:    */     
/* 289:    */     public Cell next()
/* 290:    */       throws NoSuchElementException
/* 291:    */     {
/* 292:503 */       if (hasNext()) {
/* 293:504 */         return (Cell)SXSSFRow.this._cells.get(Integer.valueOf(this.pos++));
/* 294:    */       }
/* 295:506 */       throw new NoSuchElementException();
/* 296:    */     }
/* 297:    */     
/* 298:    */     public void remove()
/* 299:    */     {
/* 300:511 */       throw new UnsupportedOperationException();
/* 301:    */     }
/* 302:    */   }
/* 303:    */   
/* 304:    */   public int compareTo(SXSSFRow other)
/* 305:    */   {
/* 306:538 */     if (getSheet() != other.getSheet()) {
/* 307:539 */       throw new IllegalArgumentException("The compared rows must belong to the same sheet");
/* 308:    */     }
/* 309:542 */     Integer thisRow = Integer.valueOf(getRowNum());
/* 310:543 */     Integer otherRow = Integer.valueOf(other.getRowNum());
/* 311:544 */     return thisRow.compareTo(otherRow);
/* 312:    */   }
/* 313:    */   
/* 314:    */   public boolean equals(Object obj)
/* 315:    */   {
/* 316:550 */     if (!(obj instanceof SXSSFRow)) {
/* 317:552 */       return false;
/* 318:    */     }
/* 319:554 */     SXSSFRow other = (SXSSFRow)obj;
/* 320:    */     
/* 321:556 */     return (getRowNum() == other.getRowNum()) && (getSheet() == other.getSheet());
/* 322:    */   }
/* 323:    */   
/* 324:    */   public int hashCode()
/* 325:    */   {
/* 326:562 */     return this._cells.hashCode();
/* 327:    */   }
/* 328:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFRow
 * JD-Core Version:    0.7.0.1
 */