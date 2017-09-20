/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   6:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   7:    */ import org.apache.poi.ss.usermodel.Cell;
/*   8:    */ import org.apache.poi.ss.usermodel.Row;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ 
/*  11:    */ @Internal
/*  12:    */ final class XSSFEvaluationSheet
/*  13:    */   implements EvaluationSheet
/*  14:    */ {
/*  15:    */   private final XSSFSheet _xs;
/*  16:    */   private Map<CellKey, EvaluationCell> _cellCache;
/*  17:    */   
/*  18:    */   public XSSFEvaluationSheet(XSSFSheet sheet)
/*  19:    */   {
/*  20: 39 */     this._xs = sheet;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public XSSFSheet getXSSFSheet()
/*  24:    */   {
/*  25: 43 */     return this._xs;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void clearAllCachedResultValues()
/*  29:    */   {
/*  30: 51 */     this._cellCache = null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public EvaluationCell getCell(int rowIndex, int columnIndex)
/*  34:    */   {
/*  35: 57 */     if (this._cellCache == null)
/*  36:    */     {
/*  37: 58 */       this._cellCache = new HashMap(this._xs.getLastRowNum() * 3);
/*  38: 59 */       for (Row row : this._xs)
/*  39:    */       {
/*  40: 60 */         rowNum = row.getRowNum();
/*  41: 61 */         for (Cell cell : row)
/*  42:    */         {
/*  43: 63 */           CellKey key = new CellKey(rowNum, cell.getColumnIndex());
/*  44: 64 */           EvaluationCell evalcell = new XSSFEvaluationCell((XSSFCell)cell, this);
/*  45: 65 */           this._cellCache.put(key, evalcell);
/*  46:    */         }
/*  47:    */       }
/*  48:    */     }
/*  49:    */     int rowNum;
/*  50: 70 */     CellKey key = new CellKey(rowIndex, columnIndex);
/*  51: 71 */     EvaluationCell evalcell = (EvaluationCell)this._cellCache.get(key);
/*  52: 78 */     if (evalcell == null)
/*  53:    */     {
/*  54: 79 */       XSSFRow row = this._xs.getRow(rowIndex);
/*  55: 80 */       if (row == null) {
/*  56: 81 */         return null;
/*  57:    */       }
/*  58: 83 */       XSSFCell cell = row.getCell(columnIndex);
/*  59: 84 */       if (cell == null) {
/*  60: 85 */         return null;
/*  61:    */       }
/*  62: 87 */       evalcell = new XSSFEvaluationCell(cell, this);
/*  63: 88 */       this._cellCache.put(key, evalcell);
/*  64:    */     }
/*  65: 91 */     return evalcell;
/*  66:    */   }
/*  67:    */   
/*  68:    */   private static class CellKey
/*  69:    */   {
/*  70:    */     private final int _row;
/*  71:    */     private final int _col;
/*  72: 97 */     private int _hash = -1;
/*  73:    */     
/*  74:    */     protected CellKey(int row, int col)
/*  75:    */     {
/*  76:100 */       this._row = row;
/*  77:101 */       this._col = col;
/*  78:    */     }
/*  79:    */     
/*  80:    */     public int hashCode()
/*  81:    */     {
/*  82:106 */       if (this._hash == -1) {
/*  83:107 */         this._hash = ((629 + this._row) * 37 + this._col);
/*  84:    */       }
/*  85:109 */       return this._hash;
/*  86:    */     }
/*  87:    */     
/*  88:    */     public boolean equals(Object obj)
/*  89:    */     {
/*  90:114 */       if (!(obj instanceof CellKey)) {
/*  91:115 */         return false;
/*  92:    */       }
/*  93:118 */       CellKey oKey = (CellKey)obj;
/*  94:119 */       return (this._row == oKey._row) && (this._col == oKey._col);
/*  95:    */     }
/*  96:    */   }
/*  97:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFEvaluationSheet
 * JD-Core Version:    0.7.0.1
 */