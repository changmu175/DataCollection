/*   1:    */ package org.apache.poi.ss.formula;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.ss.util.CellReference;
/*   8:    */ 
/*   9:    */ final class FormulaUsedBlankCellSet
/*  10:    */ {
/*  11:    */   private final Map<BookSheetKey, BlankCellSheetGroup> _sheetGroupsByBookSheet;
/*  12:    */   
/*  13:    */   public static final class BookSheetKey
/*  14:    */   {
/*  15:    */     private final int _bookIndex;
/*  16:    */     private final int _sheetIndex;
/*  17:    */     
/*  18:    */     public BookSheetKey(int bookIndex, int sheetIndex)
/*  19:    */     {
/*  20: 37 */       this._bookIndex = bookIndex;
/*  21: 38 */       this._sheetIndex = sheetIndex;
/*  22:    */     }
/*  23:    */     
/*  24:    */     public int hashCode()
/*  25:    */     {
/*  26: 42 */       return this._bookIndex * 17 + this._sheetIndex;
/*  27:    */     }
/*  28:    */     
/*  29:    */     public boolean equals(Object obj)
/*  30:    */     {
/*  31: 46 */       if (!(obj instanceof BookSheetKey)) {
/*  32: 47 */         return false;
/*  33:    */       }
/*  34: 49 */       BookSheetKey other = (BookSheetKey)obj;
/*  35: 50 */       return (this._bookIndex == other._bookIndex) && (this._sheetIndex == other._sheetIndex);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   private static final class BlankCellSheetGroup
/*  40:    */   {
/*  41:    */     private final List<BlankCellRectangleGroup> _rectangleGroups;
/*  42:    */     private int _currentRowIndex;
/*  43:    */     private int _firstColumnIndex;
/*  44:    */     private int _lastColumnIndex;
/*  45:    */     private BlankCellRectangleGroup _currentRectangleGroup;
/*  46:    */     
/*  47:    */     public BlankCellSheetGroup()
/*  48:    */     {
/*  49: 62 */       this._rectangleGroups = new ArrayList();
/*  50: 63 */       this._currentRowIndex = -1;
/*  51:    */     }
/*  52:    */     
/*  53:    */     public void addCell(int rowIndex, int columnIndex)
/*  54:    */     {
/*  55: 67 */       if (this._currentRowIndex == -1)
/*  56:    */       {
/*  57: 68 */         this._currentRowIndex = rowIndex;
/*  58: 69 */         this._firstColumnIndex = columnIndex;
/*  59: 70 */         this._lastColumnIndex = columnIndex;
/*  60:    */       }
/*  61: 72 */       else if ((this._currentRowIndex == rowIndex) && (this._lastColumnIndex + 1 == columnIndex))
/*  62:    */       {
/*  63: 73 */         this._lastColumnIndex = columnIndex;
/*  64:    */       }
/*  65:    */       else
/*  66:    */       {
/*  67: 76 */         if (this._currentRectangleGroup == null)
/*  68:    */         {
/*  69: 77 */           this._currentRectangleGroup = new BlankCellRectangleGroup(this._currentRowIndex, this._firstColumnIndex, this._lastColumnIndex);
/*  70:    */         }
/*  71: 79 */         else if (!this._currentRectangleGroup.acceptRow(this._currentRowIndex, this._firstColumnIndex, this._lastColumnIndex))
/*  72:    */         {
/*  73: 80 */           this._rectangleGroups.add(this._currentRectangleGroup);
/*  74: 81 */           this._currentRectangleGroup = new BlankCellRectangleGroup(this._currentRowIndex, this._firstColumnIndex, this._lastColumnIndex);
/*  75:    */         }
/*  76: 84 */         this._currentRowIndex = rowIndex;
/*  77: 85 */         this._firstColumnIndex = columnIndex;
/*  78: 86 */         this._lastColumnIndex = columnIndex;
/*  79:    */       }
/*  80:    */     }
/*  81:    */     
/*  82:    */     public boolean containsCell(int rowIndex, int columnIndex)
/*  83:    */     {
/*  84: 92 */       for (int i = this._rectangleGroups.size() - 1; i >= 0; i--)
/*  85:    */       {
/*  86: 93 */         BlankCellRectangleGroup bcrg = (BlankCellRectangleGroup)this._rectangleGroups.get(i);
/*  87: 94 */         if (bcrg.containsCell(rowIndex, columnIndex)) {
/*  88: 95 */           return true;
/*  89:    */         }
/*  90:    */       }
/*  91: 98 */       if ((this._currentRectangleGroup != null) && (this._currentRectangleGroup.containsCell(rowIndex, columnIndex))) {
/*  92: 99 */         return true;
/*  93:    */       }
/*  94:101 */       if ((this._currentRowIndex != -1) && (this._currentRowIndex == rowIndex) && 
/*  95:102 */         (this._firstColumnIndex <= columnIndex) && (columnIndex <= this._lastColumnIndex)) {
/*  96:103 */         return true;
/*  97:    */       }
/*  98:106 */       return false;
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   private static final class BlankCellRectangleGroup
/* 103:    */   {
/* 104:    */     private final int _firstRowIndex;
/* 105:    */     private final int _firstColumnIndex;
/* 106:    */     private final int _lastColumnIndex;
/* 107:    */     private int _lastRowIndex;
/* 108:    */     
/* 109:    */     public BlankCellRectangleGroup(int firstRowIndex, int firstColumnIndex, int lastColumnIndex)
/* 110:    */     {
/* 111:118 */       this._firstRowIndex = firstRowIndex;
/* 112:119 */       this._firstColumnIndex = firstColumnIndex;
/* 113:120 */       this._lastColumnIndex = lastColumnIndex;
/* 114:121 */       this._lastRowIndex = firstRowIndex;
/* 115:    */     }
/* 116:    */     
/* 117:    */     public boolean containsCell(int rowIndex, int columnIndex)
/* 118:    */     {
/* 119:125 */       if (columnIndex < this._firstColumnIndex) {
/* 120:126 */         return false;
/* 121:    */       }
/* 122:128 */       if (columnIndex > this._lastColumnIndex) {
/* 123:129 */         return false;
/* 124:    */       }
/* 125:131 */       if (rowIndex < this._firstRowIndex) {
/* 126:132 */         return false;
/* 127:    */       }
/* 128:134 */       if (rowIndex > this._lastRowIndex) {
/* 129:135 */         return false;
/* 130:    */       }
/* 131:137 */       return true;
/* 132:    */     }
/* 133:    */     
/* 134:    */     public boolean acceptRow(int rowIndex, int firstColumnIndex, int lastColumnIndex)
/* 135:    */     {
/* 136:141 */       if (firstColumnIndex != this._firstColumnIndex) {
/* 137:142 */         return false;
/* 138:    */       }
/* 139:144 */       if (lastColumnIndex != this._lastColumnIndex) {
/* 140:145 */         return false;
/* 141:    */       }
/* 142:147 */       if (rowIndex != this._lastRowIndex + 1) {
/* 143:148 */         return false;
/* 144:    */       }
/* 145:150 */       this._lastRowIndex = rowIndex;
/* 146:151 */       return true;
/* 147:    */     }
/* 148:    */     
/* 149:    */     public String toString()
/* 150:    */     {
/* 151:155 */       StringBuffer sb = new StringBuffer(64);
/* 152:156 */       CellReference crA = new CellReference(this._firstRowIndex, this._firstColumnIndex, false, false);
/* 153:157 */       CellReference crB = new CellReference(this._lastRowIndex, this._lastColumnIndex, false, false);
/* 154:158 */       sb.append(getClass().getName());
/* 155:159 */       sb.append(" [").append(crA.formatAsString()).append(':').append(crB.formatAsString()).append("]");
/* 156:160 */       return sb.toString();
/* 157:    */     }
/* 158:    */   }
/* 159:    */   
/* 160:    */   public FormulaUsedBlankCellSet()
/* 161:    */   {
/* 162:167 */     this._sheetGroupsByBookSheet = new HashMap();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void addCell(int bookIndex, int sheetIndex, int rowIndex, int columnIndex)
/* 166:    */   {
/* 167:171 */     BlankCellSheetGroup sbcg = getSheetGroup(bookIndex, sheetIndex);
/* 168:172 */     sbcg.addCell(rowIndex, columnIndex);
/* 169:    */   }
/* 170:    */   
/* 171:    */   private BlankCellSheetGroup getSheetGroup(int bookIndex, int sheetIndex)
/* 172:    */   {
/* 173:176 */     BookSheetKey key = new BookSheetKey(bookIndex, sheetIndex);
/* 174:    */     
/* 175:178 */     BlankCellSheetGroup result = (BlankCellSheetGroup)this._sheetGroupsByBookSheet.get(key);
/* 176:179 */     if (result == null)
/* 177:    */     {
/* 178:180 */       result = new BlankCellSheetGroup();
/* 179:181 */       this._sheetGroupsByBookSheet.put(key, result);
/* 180:    */     }
/* 181:183 */     return result;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean containsCell(BookSheetKey key, int rowIndex, int columnIndex)
/* 185:    */   {
/* 186:187 */     BlankCellSheetGroup bcsg = (BlankCellSheetGroup)this._sheetGroupsByBookSheet.get(key);
/* 187:188 */     if (bcsg == null) {
/* 188:189 */       return false;
/* 189:    */     }
/* 190:191 */     return bcsg.containsCell(rowIndex, columnIndex);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public boolean isEmpty()
/* 194:    */   {
/* 195:195 */     return this._sheetGroupsByBookSheet.isEmpty();
/* 196:    */   }
/* 197:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.FormulaUsedBlankCellSet

 * JD-Core Version:    0.7.0.1

 */