/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.NoSuchElementException;
/*   7:    */ import org.apache.poi.ss.usermodel.Cell;
/*   8:    */ import org.apache.poi.ss.usermodel.CellRange;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ 
/*  11:    */ @Internal
/*  12:    */ public final class SSCellRange<K extends Cell>
/*  13:    */   implements CellRange<K>
/*  14:    */ {
/*  15:    */   private final int _height;
/*  16:    */   private final int _width;
/*  17:    */   private final K[] _flattenedArray;
/*  18:    */   private final int _firstRow;
/*  19:    */   private final int _firstColumn;
/*  20:    */   
/*  21:    */   private SSCellRange(int firstRow, int firstColumn, int height, int width, K[] flattenedArray)
/*  22:    */   {
/*  23: 44 */     this._firstRow = firstRow;
/*  24: 45 */     this._firstColumn = firstColumn;
/*  25: 46 */     this._height = height;
/*  26: 47 */     this._width = width;
/*  27: 48 */     this._flattenedArray = ((Cell[])flattenedArray.clone());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static <B extends Cell> SSCellRange<B> create(int firstRow, int firstColumn, int height, int width, List<B> flattenedList, Class<B> cellClass)
/*  31:    */   {
/*  32: 52 */     int nItems = flattenedList.size();
/*  33: 53 */     if (height * width != nItems) {
/*  34: 54 */       throw new IllegalArgumentException("Array size mismatch.");
/*  35:    */     }
/*  36: 58 */     B[] flattenedArray = (Cell[])Array.newInstance(cellClass, nItems);
/*  37: 59 */     flattenedList.toArray(flattenedArray);
/*  38: 60 */     return new SSCellRange(firstRow, firstColumn, height, width, flattenedArray);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getHeight()
/*  42:    */   {
/*  43: 64 */     return this._height;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getWidth()
/*  47:    */   {
/*  48: 67 */     return this._width;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int size()
/*  52:    */   {
/*  53: 70 */     return this._height * this._width;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getReferenceText()
/*  57:    */   {
/*  58: 74 */     CellRangeAddress cra = new CellRangeAddress(this._firstRow, this._firstRow + this._height - 1, this._firstColumn, this._firstColumn + this._width - 1);
/*  59: 75 */     return cra.formatAsString();
/*  60:    */   }
/*  61:    */   
/*  62:    */   public K getTopLeftCell()
/*  63:    */   {
/*  64: 79 */     return this._flattenedArray[0];
/*  65:    */   }
/*  66:    */   
/*  67:    */   public K getCell(int relativeRowIndex, int relativeColumnIndex)
/*  68:    */   {
/*  69: 83 */     if ((relativeRowIndex < 0) || (relativeRowIndex >= this._height)) {
/*  70: 84 */       throw new ArrayIndexOutOfBoundsException("Specified row " + relativeRowIndex + " is outside the allowable range (0.." + (this._height - 1) + ").");
/*  71:    */     }
/*  72: 87 */     if ((relativeColumnIndex < 0) || (relativeColumnIndex >= this._width)) {
/*  73: 88 */       throw new ArrayIndexOutOfBoundsException("Specified colummn " + relativeColumnIndex + " is outside the allowable range (0.." + (this._width - 1) + ").");
/*  74:    */     }
/*  75: 91 */     int flatIndex = this._width * relativeRowIndex + relativeColumnIndex;
/*  76: 92 */     return this._flattenedArray[flatIndex];
/*  77:    */   }
/*  78:    */   
/*  79:    */   public K[] getFlattenedCells()
/*  80:    */   {
/*  81: 95 */     return (Cell[])this._flattenedArray.clone();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public K[][] getCells()
/*  85:    */   {
/*  86: 99 */     Class<?> itemCls = this._flattenedArray.getClass();
/*  87:    */     
/*  88:101 */     K[][] result = (Cell[][])Array.newInstance(itemCls, this._height);
/*  89:102 */     itemCls = itemCls.getComponentType();
/*  90:103 */     for (int r = this._height - 1; r >= 0; r--)
/*  91:    */     {
/*  92:105 */       K[] row = (Cell[])Array.newInstance(itemCls, this._width);
/*  93:106 */       int flatIndex = this._width * r;
/*  94:107 */       System.arraycopy(this._flattenedArray, flatIndex, row, 0, this._width);
/*  95:    */     }
/*  96:109 */     return result;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public Iterator<K> iterator()
/* 100:    */   {
/* 101:112 */     return new ArrayIterator(this._flattenedArray);
/* 102:    */   }
/* 103:    */   
/* 104:    */   private static final class ArrayIterator<D>
/* 105:    */     implements Iterator<D>
/* 106:    */   {
/* 107:    */     private final D[] _array;
/* 108:    */     private int _index;
/* 109:    */     
/* 110:    */     public ArrayIterator(D[] array)
/* 111:    */     {
/* 112:120 */       this._array = ((Object[])array.clone());
/* 113:121 */       this._index = 0;
/* 114:    */     }
/* 115:    */     
/* 116:    */     public boolean hasNext()
/* 117:    */     {
/* 118:124 */       return this._index < this._array.length;
/* 119:    */     }
/* 120:    */     
/* 121:    */     public D next()
/* 122:    */     {
/* 123:127 */       if (this._index >= this._array.length) {
/* 124:128 */         throw new NoSuchElementException(String.valueOf(this._index));
/* 125:    */       }
/* 126:130 */       return this._array[(this._index++)];
/* 127:    */     }
/* 128:    */     
/* 129:    */     public void remove()
/* 130:    */     {
/* 131:134 */       throw new UnsupportedOperationException("Cannot remove cells from this CellRange.");
/* 132:    */     }
/* 133:    */   }
/* 134:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.SSCellRange
 * JD-Core Version:    0.7.0.1
 */