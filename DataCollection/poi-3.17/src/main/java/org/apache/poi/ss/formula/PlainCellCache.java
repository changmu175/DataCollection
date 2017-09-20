/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ final class PlainCellCache
/*  7:   */ {
/*  8:   */   private Map<Loc, PlainValueCellCacheEntry> _plainValueEntriesByLoc;
/*  9:   */   
/* 10:   */   public static final class Loc
/* 11:   */   {
/* 12:   */     private final long _bookSheetColumn;
/* 13:   */     private final int _rowIndex;
/* 14:   */     
/* 15:   */     public Loc(int bookIndex, int sheetIndex, int rowIndex, int columnIndex)
/* 16:   */     {
/* 17:32 */       this._bookSheetColumn = toBookSheetColumn(bookIndex, sheetIndex, columnIndex);
/* 18:33 */       this._rowIndex = rowIndex;
/* 19:   */     }
/* 20:   */     
/* 21:   */     public static long toBookSheetColumn(int bookIndex, int sheetIndex, int columnIndex)
/* 22:   */     {
/* 23:37 */       return ((bookIndex & 0xFFFF) << 48) + ((sheetIndex & 0xFFFF) << 32) + ((columnIndex & 0xFFFF) << 0);
/* 24:   */     }
/* 25:   */     
/* 26:   */     public Loc(long bookSheetColumn, int rowIndex)
/* 27:   */     {
/* 28:43 */       this._bookSheetColumn = bookSheetColumn;
/* 29:44 */       this._rowIndex = rowIndex;
/* 30:   */     }
/* 31:   */     
/* 32:   */     public int hashCode()
/* 33:   */     {
/* 34:49 */       return (int)(this._bookSheetColumn ^ this._bookSheetColumn >>> 32) + 17 * this._rowIndex;
/* 35:   */     }
/* 36:   */     
/* 37:   */     public boolean equals(Object obj)
/* 38:   */     {
/* 39:54 */       if (!(obj instanceof Loc)) {
/* 40:55 */         return false;
/* 41:   */       }
/* 42:57 */       Loc other = (Loc)obj;
/* 43:58 */       return (this._bookSheetColumn == other._bookSheetColumn) && (this._rowIndex == other._rowIndex);
/* 44:   */     }
/* 45:   */     
/* 46:   */     public int getRowIndex()
/* 47:   */     {
/* 48:62 */       return this._rowIndex;
/* 49:   */     }
/* 50:   */     
/* 51:   */     public int getColumnIndex()
/* 52:   */     {
/* 53:66 */       return (int)(this._bookSheetColumn & 0xFFFF);
/* 54:   */     }
/* 55:   */     
/* 56:   */     public int getSheetIndex()
/* 57:   */     {
/* 58:70 */       return (int)(this._bookSheetColumn >> 32 & 0xFFFF);
/* 59:   */     }
/* 60:   */     
/* 61:   */     public int getBookIndex()
/* 62:   */     {
/* 63:74 */       return (int)(this._bookSheetColumn >> 48 & 0xFFFF);
/* 64:   */     }
/* 65:   */   }
/* 66:   */   
/* 67:   */   public PlainCellCache()
/* 68:   */   {
/* 69:81 */     this._plainValueEntriesByLoc = new HashMap();
/* 70:   */   }
/* 71:   */   
/* 72:   */   public void put(Loc key, PlainValueCellCacheEntry cce)
/* 73:   */   {
/* 74:84 */     this._plainValueEntriesByLoc.put(key, cce);
/* 75:   */   }
/* 76:   */   
/* 77:   */   public void clear()
/* 78:   */   {
/* 79:87 */     this._plainValueEntriesByLoc.clear();
/* 80:   */   }
/* 81:   */   
/* 82:   */   public PlainValueCellCacheEntry get(Loc key)
/* 83:   */   {
/* 84:90 */     return (PlainValueCellCacheEntry)this._plainValueEntriesByLoc.get(key);
/* 85:   */   }
/* 86:   */   
/* 87:   */   public void remove(Loc key)
/* 88:   */   {
/* 89:93 */     this._plainValueEntriesByLoc.remove(key);
/* 90:   */   }
/* 91:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.PlainCellCache
 * JD-Core Version:    0.7.0.1
 */