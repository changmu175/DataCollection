/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   4:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   5:    */ import org.apache.poi.ss.usermodel.CellType;
/*   6:    */ import org.apache.poi.util.Internal;
/*   7:    */ 
/*   8:    */ final class XSSFEvaluationCell
/*   9:    */   implements EvaluationCell
/*  10:    */ {
/*  11:    */   private final EvaluationSheet _evalSheet;
/*  12:    */   private final XSSFCell _cell;
/*  13:    */   
/*  14:    */   public XSSFEvaluationCell(XSSFCell cell, XSSFEvaluationSheet evaluationSheet)
/*  15:    */   {
/*  16: 34 */     this._cell = cell;
/*  17: 35 */     this._evalSheet = evaluationSheet;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public XSSFEvaluationCell(XSSFCell cell)
/*  21:    */   {
/*  22: 39 */     this(cell, new XSSFEvaluationSheet(cell.getSheet()));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Object getIdentityKey()
/*  26:    */   {
/*  27: 46 */     return this._cell;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public XSSFCell getXSSFCell()
/*  31:    */   {
/*  32: 50 */     return this._cell;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean getBooleanCellValue()
/*  36:    */   {
/*  37: 54 */     return this._cell.getBooleanCellValue();
/*  38:    */   }
/*  39:    */   
/*  40:    */   /**
/*  41:    */    * @deprecated
/*  42:    */    */
/*  43:    */   public int getCellType()
/*  44:    */   {
/*  45: 65 */     return this._cell.getCellType();
/*  46:    */   }
/*  47:    */   
/*  48:    */   /**
/*  49:    */    * @deprecated
/*  50:    */    */
/*  51:    */   public CellType getCellTypeEnum()
/*  52:    */   {
/*  53: 74 */     return this._cell.getCellTypeEnum();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getColumnIndex()
/*  57:    */   {
/*  58: 78 */     return this._cell.getColumnIndex();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int getErrorCellValue()
/*  62:    */   {
/*  63: 82 */     return this._cell.getErrorCellValue();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double getNumericCellValue()
/*  67:    */   {
/*  68: 86 */     return this._cell.getNumericCellValue();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getRowIndex()
/*  72:    */   {
/*  73: 90 */     return this._cell.getRowIndex();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public EvaluationSheet getSheet()
/*  77:    */   {
/*  78: 94 */     return this._evalSheet;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getStringCellValue()
/*  82:    */   {
/*  83: 98 */     return this._cell.getRichStringCellValue().getString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   /**
/*  87:    */    * @deprecated
/*  88:    */    */
/*  89:    */   public int getCachedFormulaResultType()
/*  90:    */   {
/*  91:109 */     return this._cell.getCachedFormulaResultType();
/*  92:    */   }
/*  93:    */   
/*  94:    */   /**
/*  95:    */    * @deprecated
/*  96:    */    */
/*  97:    */   @Internal(since="POI 3.15 beta 3")
/*  98:    */   public CellType getCachedFormulaResultTypeEnum()
/*  99:    */   {
/* 100:119 */     return this._cell.getCachedFormulaResultTypeEnum();
/* 101:    */   }
/* 102:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFEvaluationCell
 * JD-Core Version:    0.7.0.1
 */