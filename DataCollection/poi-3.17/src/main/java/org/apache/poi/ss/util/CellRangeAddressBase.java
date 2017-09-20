/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.EnumSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   6:    */ import org.apache.poi.ss.usermodel.Cell;
/*   7:    */ 
/*   8:    */ public abstract class CellRangeAddressBase
/*   9:    */ {
/*  10:    */   private int _firstRow;
/*  11:    */   private int _firstCol;
/*  12:    */   private int _lastRow;
/*  13:    */   private int _lastCol;
/*  14:    */   
/*  15:    */   public static enum CellPosition
/*  16:    */   {
/*  17: 40 */     TOP,  BOTTOM,  LEFT,  RIGHT,  INSIDE;
/*  18:    */     
/*  19:    */     private CellPosition() {}
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected CellRangeAddressBase(int firstRow, int lastRow, int firstCol, int lastCol)
/*  23:    */   {
/*  24: 57 */     this._firstRow = firstRow;
/*  25: 58 */     this._lastRow = lastRow;
/*  26: 59 */     this._firstCol = firstCol;
/*  27: 60 */     this._lastCol = lastCol;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void validate(SpreadsheetVersion ssVersion)
/*  31:    */   {
/*  32: 70 */     validateRow(this._firstRow, ssVersion);
/*  33: 71 */     validateRow(this._lastRow, ssVersion);
/*  34: 72 */     validateColumn(this._firstCol, ssVersion);
/*  35: 73 */     validateColumn(this._lastCol, ssVersion);
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static void validateRow(int row, SpreadsheetVersion ssVersion)
/*  39:    */   {
/*  40: 80 */     int maxrow = ssVersion.getLastRowIndex();
/*  41: 81 */     if (row > maxrow) {
/*  42: 81 */       throw new IllegalArgumentException("Maximum row number is " + maxrow);
/*  43:    */     }
/*  44: 82 */     if (row < 0) {
/*  45: 82 */       throw new IllegalArgumentException("Minumum row number is 0");
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static void validateColumn(int column, SpreadsheetVersion ssVersion)
/*  50:    */   {
/*  51: 90 */     int maxcol = ssVersion.getLastColumnIndex();
/*  52: 91 */     if (column > maxcol) {
/*  53: 91 */       throw new IllegalArgumentException("Maximum column number is " + maxcol);
/*  54:    */     }
/*  55: 92 */     if (column < 0) {
/*  56: 92 */       throw new IllegalArgumentException("Minimum column number is 0");
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public final boolean isFullColumnRange()
/*  61:    */   {
/*  62: 98 */     return ((this._firstRow == 0) && (this._lastRow == SpreadsheetVersion.EXCEL97.getLastRowIndex())) || ((this._firstRow == -1) && (this._lastRow == -1));
/*  63:    */   }
/*  64:    */   
/*  65:    */   public final boolean isFullRowRange()
/*  66:    */   {
/*  67:103 */     return ((this._firstCol == 0) && (this._lastCol == SpreadsheetVersion.EXCEL97.getLastColumnIndex())) || ((this._firstCol == -1) && (this._lastCol == -1));
/*  68:    */   }
/*  69:    */   
/*  70:    */   public final int getFirstColumn()
/*  71:    */   {
/*  72:111 */     return this._firstCol;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final int getFirstRow()
/*  76:    */   {
/*  77:118 */     return this._firstRow;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public final int getLastColumn()
/*  81:    */   {
/*  82:125 */     return this._lastCol;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public final int getLastRow()
/*  86:    */   {
/*  87:132 */     return this._lastRow;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isInRange(int rowInd, int colInd)
/*  91:    */   {
/*  92:145 */     return (this._firstRow <= rowInd) && (rowInd <= this._lastRow) && (this._firstCol <= colInd) && (colInd <= this._lastCol);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isInRange(CellReference ref)
/*  96:    */   {
/*  97:160 */     return isInRange(ref.getRow(), ref.getCol());
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean isInRange(Cell cell)
/* 101:    */   {
/* 102:174 */     return isInRange(cell.getRowIndex(), cell.getColumnIndex());
/* 103:    */   }
/* 104:    */   
/* 105:    */   public boolean containsRow(int rowInd)
/* 106:    */   {
/* 107:184 */     return (this._firstRow <= rowInd) && (rowInd <= this._lastRow);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean containsColumn(int colInd)
/* 111:    */   {
/* 112:194 */     return (this._firstCol <= colInd) && (colInd <= this._lastCol);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean intersects(CellRangeAddressBase other)
/* 116:    */   {
/* 117:205 */     return (this._firstRow <= other._lastRow) && (this._firstCol <= other._lastCol) && (other._firstRow <= this._lastRow) && (other._firstCol <= this._lastCol);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Set<CellPosition> getPosition(int rowInd, int colInd)
/* 121:    */   {
/* 122:219 */     Set<CellPosition> positions = EnumSet.noneOf(CellPosition.class);
/* 123:220 */     if ((rowInd > getFirstRow()) && (rowInd < getLastRow()) && (colInd > getFirstColumn()) && (colInd < getLastColumn()))
/* 124:    */     {
/* 125:221 */       positions.add(CellPosition.INSIDE);
/* 126:222 */       return positions;
/* 127:    */     }
/* 128:225 */     if (rowInd == getFirstRow()) {
/* 129:225 */       positions.add(CellPosition.TOP);
/* 130:    */     }
/* 131:226 */     if (rowInd == getLastRow()) {
/* 132:226 */       positions.add(CellPosition.BOTTOM);
/* 133:    */     }
/* 134:227 */     if (colInd == getFirstColumn()) {
/* 135:227 */       positions.add(CellPosition.LEFT);
/* 136:    */     }
/* 137:228 */     if (colInd == getLastColumn()) {
/* 138:228 */       positions.add(CellPosition.RIGHT);
/* 139:    */     }
/* 140:230 */     return positions;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public final void setFirstColumn(int firstCol)
/* 144:    */   {
/* 145:237 */     this._firstCol = firstCol;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public final void setFirstRow(int firstRow)
/* 149:    */   {
/* 150:244 */     this._firstRow = firstRow;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public final void setLastColumn(int lastCol)
/* 154:    */   {
/* 155:251 */     this._lastCol = lastCol;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public final void setLastRow(int lastRow)
/* 159:    */   {
/* 160:258 */     this._lastRow = lastRow;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int getNumberOfCells()
/* 164:    */   {
/* 165:264 */     return (this._lastRow - this._firstRow + 1) * (this._lastCol - this._firstCol + 1);
/* 166:    */   }
/* 167:    */   
/* 168:    */   public final String toString()
/* 169:    */   {
/* 170:269 */     CellReference crA = new CellReference(this._firstRow, this._firstCol);
/* 171:270 */     CellReference crB = new CellReference(this._lastRow, this._lastCol);
/* 172:271 */     return getClass().getName() + " [" + crA.formatAsString() + ":" + crB.formatAsString() + "]";
/* 173:    */   }
/* 174:    */   
/* 175:    */   protected int getMinRow()
/* 176:    */   {
/* 177:276 */     return Math.min(this._firstRow, this._lastRow);
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected int getMaxRow()
/* 181:    */   {
/* 182:279 */     return Math.max(this._firstRow, this._lastRow);
/* 183:    */   }
/* 184:    */   
/* 185:    */   protected int getMinColumn()
/* 186:    */   {
/* 187:282 */     return Math.min(this._firstCol, this._lastCol);
/* 188:    */   }
/* 189:    */   
/* 190:    */   protected int getMaxColumn()
/* 191:    */   {
/* 192:285 */     return Math.max(this._firstCol, this._lastCol);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean equals(Object other)
/* 196:    */   {
/* 197:290 */     if ((other instanceof CellRangeAddressBase))
/* 198:    */     {
/* 199:291 */       CellRangeAddressBase o = (CellRangeAddressBase)other;
/* 200:292 */       return (getMinRow() == o.getMinRow()) && (getMaxRow() == o.getMaxRow()) && (getMinColumn() == o.getMinColumn()) && (getMaxColumn() == o.getMaxColumn());
/* 201:    */     }
/* 202:297 */     return false;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public int hashCode()
/* 206:    */   {
/* 207:302 */     int code = getMinColumn() + (getMaxColumn() << 8) + (getMinRow() << 16) + (getMaxRow() << 24);
/* 208:    */     
/* 209:    */ 
/* 210:    */ 
/* 211:306 */     return code;
/* 212:    */   }
/* 213:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellRangeAddressBase
 * JD-Core Version:    0.7.0.1
 */