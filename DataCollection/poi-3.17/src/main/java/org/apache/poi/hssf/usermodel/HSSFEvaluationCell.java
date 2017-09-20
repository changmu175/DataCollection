/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   4:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   5:    */ import org.apache.poi.ss.usermodel.CellType;
/*   6:    */ 
/*   7:    */ final class HSSFEvaluationCell
/*   8:    */   implements EvaluationCell
/*   9:    */ {
/*  10:    */   private final EvaluationSheet _evalSheet;
/*  11:    */   private final HSSFCell _cell;
/*  12:    */   
/*  13:    */   public HSSFEvaluationCell(HSSFCell cell, EvaluationSheet evalSheet)
/*  14:    */   {
/*  15: 32 */     this._cell = cell;
/*  16: 33 */     this._evalSheet = evalSheet;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public HSSFEvaluationCell(HSSFCell cell)
/*  20:    */   {
/*  21: 36 */     this(cell, new HSSFEvaluationSheet(cell.getSheet()));
/*  22:    */   }
/*  23:    */   
/*  24:    */   public Object getIdentityKey()
/*  25:    */   {
/*  26: 42 */     return this._cell;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public HSSFCell getHSSFCell()
/*  30:    */   {
/*  31: 46 */     return this._cell;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean getBooleanCellValue()
/*  35:    */   {
/*  36: 50 */     return this._cell.getBooleanCellValue();
/*  37:    */   }
/*  38:    */   
/*  39:    */   /**
/*  40:    */    * @deprecated
/*  41:    */    */
/*  42:    */   public int getCellType()
/*  43:    */   {
/*  44: 61 */     return this._cell.getCellType();
/*  45:    */   }
/*  46:    */   
/*  47:    */   /**
/*  48:    */    * @deprecated
/*  49:    */    */
/*  50:    */   public CellType getCellTypeEnum()
/*  51:    */   {
/*  52: 70 */     return this._cell.getCellTypeEnum();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getColumnIndex()
/*  56:    */   {
/*  57: 74 */     return this._cell.getColumnIndex();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int getErrorCellValue()
/*  61:    */   {
/*  62: 78 */     return this._cell.getErrorCellValue();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public double getNumericCellValue()
/*  66:    */   {
/*  67: 82 */     return this._cell.getNumericCellValue();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getRowIndex()
/*  71:    */   {
/*  72: 86 */     return this._cell.getRowIndex();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public EvaluationSheet getSheet()
/*  76:    */   {
/*  77: 90 */     return this._evalSheet;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String getStringCellValue()
/*  81:    */   {
/*  82: 94 */     return this._cell.getRichStringCellValue().getString();
/*  83:    */   }
/*  84:    */   
/*  85:    */   /**
/*  86:    */    * @deprecated
/*  87:    */    */
/*  88:    */   public int getCachedFormulaResultType()
/*  89:    */   {
/*  90:105 */     return this._cell.getCachedFormulaResultType();
/*  91:    */   }
/*  92:    */   
/*  93:    */   /**
/*  94:    */    * @deprecated
/*  95:    */    */
/*  96:    */   public CellType getCachedFormulaResultTypeEnum()
/*  97:    */   {
/*  98:114 */     return this._cell.getCachedFormulaResultTypeEnum();
/*  99:    */   }
/* 100:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFEvaluationCell
 * JD-Core Version:    0.7.0.1
 */