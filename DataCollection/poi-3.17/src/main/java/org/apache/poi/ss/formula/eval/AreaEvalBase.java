/*   1:    */ package org.apache.poi.ss.formula.eval;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.SheetRange;
/*   4:    */ import org.apache.poi.ss.formula.ptg.AreaI;
/*   5:    */ 
/*   6:    */ public abstract class AreaEvalBase
/*   7:    */   implements AreaEval
/*   8:    */ {
/*   9:    */   private final int _firstSheet;
/*  10:    */   private final int _firstColumn;
/*  11:    */   private final int _firstRow;
/*  12:    */   private final int _lastSheet;
/*  13:    */   private final int _lastColumn;
/*  14:    */   private final int _lastRow;
/*  15:    */   private final int _nColumns;
/*  16:    */   private final int _nRows;
/*  17:    */   
/*  18:    */   protected AreaEvalBase(SheetRange sheets, int firstRow, int firstColumn, int lastRow, int lastColumn)
/*  19:    */   {
/*  20: 38 */     this._firstColumn = firstColumn;
/*  21: 39 */     this._firstRow = firstRow;
/*  22: 40 */     this._lastColumn = lastColumn;
/*  23: 41 */     this._lastRow = lastRow;
/*  24:    */     
/*  25: 43 */     this._nColumns = (this._lastColumn - this._firstColumn + 1);
/*  26: 44 */     this._nRows = (this._lastRow - this._firstRow + 1);
/*  27: 46 */     if (sheets != null)
/*  28:    */     {
/*  29: 47 */       this._firstSheet = sheets.getFirstSheetIndex();
/*  30: 48 */       this._lastSheet = sheets.getLastSheetIndex();
/*  31:    */     }
/*  32:    */     else
/*  33:    */     {
/*  34: 50 */       this._firstSheet = -1;
/*  35: 51 */       this._lastSheet = -1;
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected AreaEvalBase(int firstRow, int firstColumn, int lastRow, int lastColumn)
/*  40:    */   {
/*  41: 55 */     this(null, firstRow, firstColumn, lastRow, lastColumn);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected AreaEvalBase(AreaI ptg)
/*  45:    */   {
/*  46: 59 */     this(ptg, null);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected AreaEvalBase(AreaI ptg, SheetRange sheets)
/*  50:    */   {
/*  51: 62 */     this(sheets, ptg.getFirstRow(), ptg.getFirstColumn(), ptg.getLastRow(), ptg.getLastColumn());
/*  52:    */   }
/*  53:    */   
/*  54:    */   public final int getFirstColumn()
/*  55:    */   {
/*  56: 66 */     return this._firstColumn;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public final int getFirstRow()
/*  60:    */   {
/*  61: 70 */     return this._firstRow;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public final int getLastColumn()
/*  65:    */   {
/*  66: 74 */     return this._lastColumn;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final int getLastRow()
/*  70:    */   {
/*  71: 78 */     return this._lastRow;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getFirstSheetIndex()
/*  75:    */   {
/*  76: 82 */     return this._firstSheet;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getLastSheetIndex()
/*  80:    */   {
/*  81: 85 */     return this._lastSheet;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final ValueEval getAbsoluteValue(int row, int col)
/*  85:    */   {
/*  86: 89 */     int rowOffsetIx = row - this._firstRow;
/*  87: 90 */     int colOffsetIx = col - this._firstColumn;
/*  88: 92 */     if ((rowOffsetIx < 0) || (rowOffsetIx >= this._nRows)) {
/*  89: 93 */       throw new IllegalArgumentException("Specified row index (" + row + ") is outside the allowed range (" + this._firstRow + ".." + this._lastRow + ")");
/*  90:    */     }
/*  91: 96 */     if ((colOffsetIx < 0) || (colOffsetIx >= this._nColumns)) {
/*  92: 97 */       throw new IllegalArgumentException("Specified column index (" + col + ") is outside the allowed range (" + this._firstColumn + ".." + col + ")");
/*  93:    */     }
/*  94:100 */     return getRelativeValue(rowOffsetIx, colOffsetIx);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public final boolean contains(int row, int col)
/*  98:    */   {
/*  99:104 */     return (this._firstRow <= row) && (this._lastRow >= row) && (this._firstColumn <= col) && (this._lastColumn >= col);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public final boolean containsRow(int row)
/* 103:    */   {
/* 104:109 */     return (this._firstRow <= row) && (this._lastRow >= row);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public final boolean containsColumn(int col)
/* 108:    */   {
/* 109:113 */     return (this._firstColumn <= col) && (this._lastColumn >= col);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public final boolean isColumn()
/* 113:    */   {
/* 114:117 */     return this._firstColumn == this._lastColumn;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public final boolean isRow()
/* 118:    */   {
/* 119:121 */     return this._firstRow == this._lastRow;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int getHeight()
/* 123:    */   {
/* 124:124 */     return this._lastRow - this._firstRow + 1;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public final ValueEval getValue(int row, int col)
/* 128:    */   {
/* 129:128 */     return getRelativeValue(row, col);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public final ValueEval getValue(int sheetIndex, int row, int col)
/* 133:    */   {
/* 134:131 */     return getRelativeValue(sheetIndex, row, col);
/* 135:    */   }
/* 136:    */   
/* 137:    */   public abstract ValueEval getRelativeValue(int paramInt1, int paramInt2);
/* 138:    */   
/* 139:    */   public abstract ValueEval getRelativeValue(int paramInt1, int paramInt2, int paramInt3);
/* 140:    */   
/* 141:    */   public int getWidth()
/* 142:    */   {
/* 143:138 */     return this._lastColumn - this._firstColumn + 1;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isSubTotal(int rowIndex, int columnIndex)
/* 147:    */   {
/* 148:146 */     return false;
/* 149:    */   }
/* 150:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.eval.AreaEvalBase
 * JD-Core Version:    0.7.0.1
 */