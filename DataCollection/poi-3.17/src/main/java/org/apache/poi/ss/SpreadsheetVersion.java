/*   1:    */ package org.apache.poi.ss;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellReference;
/*   4:    */ 
/*   5:    */ public enum SpreadsheetVersion
/*   6:    */ {
/*   7: 39 */   EXCEL97(65536, 256, 30, 3, 4000, 32767),  EXCEL2007(1048576, 16384, 255, 2147483647, 64000, 32767);
/*   8:    */   
/*   9:    */   private final int _maxRows;
/*  10:    */   private final int _maxColumns;
/*  11:    */   private final int _maxFunctionArgs;
/*  12:    */   private final int _maxCondFormats;
/*  13:    */   private final int _maxCellStyles;
/*  14:    */   private final int _maxTextLength;
/*  15:    */   
/*  16:    */   private SpreadsheetVersion(int maxRows, int maxColumns, int maxFunctionArgs, int maxCondFormats, int maxCellStyles, int maxText)
/*  17:    */   {
/*  18: 64 */     this._maxRows = maxRows;
/*  19: 65 */     this._maxColumns = maxColumns;
/*  20: 66 */     this._maxFunctionArgs = maxFunctionArgs;
/*  21: 67 */     this._maxCondFormats = maxCondFormats;
/*  22: 68 */     this._maxCellStyles = maxCellStyles;
/*  23: 69 */     this._maxTextLength = maxText;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int getMaxRows()
/*  27:    */   {
/*  28: 76 */     return this._maxRows;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getLastRowIndex()
/*  32:    */   {
/*  33: 83 */     return this._maxRows - 1;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getMaxColumns()
/*  37:    */   {
/*  38: 90 */     return this._maxColumns;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public int getLastColumnIndex()
/*  42:    */   {
/*  43: 97 */     return this._maxColumns - 1;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getMaxFunctionArgs()
/*  47:    */   {
/*  48:104 */     return this._maxFunctionArgs;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int getMaxConditionalFormats()
/*  52:    */   {
/*  53:111 */     return this._maxCondFormats;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getMaxCellStyles()
/*  57:    */   {
/*  58:118 */     return this._maxCellStyles;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getLastColumnName()
/*  62:    */   {
/*  63:127 */     return CellReference.convertNumToColString(getLastColumnIndex());
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getMaxTextLength()
/*  67:    */   {
/*  68:134 */     return this._maxTextLength;
/*  69:    */   }
/*  70:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.SpreadsheetVersion
 * JD-Core Version:    0.7.0.1
 */