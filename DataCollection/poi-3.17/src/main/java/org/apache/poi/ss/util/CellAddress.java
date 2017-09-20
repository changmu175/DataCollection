/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.ss.usermodel.Cell;
/*   5:    */ 
/*   6:    */ public class CellAddress
/*   7:    */   implements Comparable<CellAddress>
/*   8:    */ {
/*   9: 37 */   public static final CellAddress A1 = new CellAddress(0, 0);
/*  10:    */   private final int _row;
/*  11:    */   private final int _col;
/*  12:    */   
/*  13:    */   public CellAddress(int row, int column)
/*  14:    */   {
/*  15: 50 */     this._row = row;
/*  16: 51 */     this._col = column;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public CellAddress(String address)
/*  20:    */   {
/*  21: 62 */     int length = address.length();
/*  22: 64 */     for (int loc = 0; loc < length; loc++)
/*  23:    */     {
/*  24: 67 */       char ch = address.charAt(loc);
/*  25: 68 */       if (Character.isDigit(ch)) {
/*  26:    */         break;
/*  27:    */       }
/*  28:    */     }
/*  29: 73 */     String sCol = address.substring(0, loc).toUpperCase(Locale.ROOT);
/*  30: 74 */     String sRow = address.substring(loc);
/*  31:    */     
/*  32:    */ 
/*  33: 77 */     this._row = (Integer.parseInt(sRow) - 1);
/*  34: 78 */     this._col = CellReference.convertColStringToIndex(sCol);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public CellAddress(CellReference reference)
/*  38:    */   {
/*  39: 87 */     this(reference.getRow(), reference.getCol());
/*  40:    */   }
/*  41:    */   
/*  42:    */   public CellAddress(CellAddress address)
/*  43:    */   {
/*  44: 96 */     this(address.getRow(), address.getColumn());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public CellAddress(Cell cell)
/*  48:    */   {
/*  49:105 */     this(cell.getRowIndex(), cell.getColumnIndex());
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getRow()
/*  53:    */   {
/*  54:114 */     return this._row;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getColumn()
/*  58:    */   {
/*  59:123 */     return this._col;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int compareTo(CellAddress other)
/*  63:    */   {
/*  64:139 */     int r = this._row - other._row;
/*  65:140 */     if (r != 0) {
/*  66:140 */       return r;
/*  67:    */     }
/*  68:142 */     r = this._col - other._col;
/*  69:143 */     if (r != 0) {
/*  70:143 */       return r;
/*  71:    */     }
/*  72:145 */     return 0;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public boolean equals(Object o)
/*  76:    */   {
/*  77:150 */     if (this == o) {
/*  78:151 */       return true;
/*  79:    */     }
/*  80:153 */     if (!(o instanceof CellAddress)) {
/*  81:154 */       return false;
/*  82:    */     }
/*  83:157 */     CellAddress other = (CellAddress)o;
/*  84:158 */     return (this._row == other._row) && (this._col == other._col);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public int hashCode()
/*  88:    */   {
/*  89:164 */     return this._row + this._col << 16;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String toString()
/*  93:    */   {
/*  94:169 */     return formatAsString();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String formatAsString()
/*  98:    */   {
/*  99:177 */     return CellReference.convertNumToColString(this._col) + (this._row + 1);
/* 100:    */   }
/* 101:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellAddress
 * JD-Core Version:    0.7.0.1
 */