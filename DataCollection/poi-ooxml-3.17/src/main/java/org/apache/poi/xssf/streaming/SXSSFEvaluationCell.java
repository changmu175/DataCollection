/*   1:    */ package org.apache.poi.xssf.streaming;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.EvaluationCell;
/*   4:    */ import org.apache.poi.ss.formula.EvaluationSheet;
/*   5:    */ import org.apache.poi.ss.usermodel.CellType;
/*   6:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ 
/*   9:    */ final class SXSSFEvaluationCell
/*  10:    */   implements EvaluationCell
/*  11:    */ {
/*  12:    */   private final EvaluationSheet _evalSheet;
/*  13:    */   private final SXSSFCell _cell;
/*  14:    */   
/*  15:    */   public SXSSFEvaluationCell(SXSSFCell cell, SXSSFEvaluationSheet evaluationSheet)
/*  16:    */   {
/*  17: 33 */     this._cell = cell;
/*  18: 34 */     this._evalSheet = evaluationSheet;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public SXSSFEvaluationCell(SXSSFCell cell)
/*  22:    */   {
/*  23: 38 */     this(cell, new SXSSFEvaluationSheet(cell.getSheet()));
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Object getIdentityKey()
/*  27:    */   {
/*  28: 45 */     return this._cell;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SXSSFCell getSXSSFCell()
/*  32:    */   {
/*  33: 49 */     return this._cell;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean getBooleanCellValue()
/*  37:    */   {
/*  38: 53 */     return this._cell.getBooleanCellValue();
/*  39:    */   }
/*  40:    */   
/*  41:    */   /**
/*  42:    */    * @deprecated
/*  43:    */    */
/*  44:    */   public int getCellType()
/*  45:    */   {
/*  46: 64 */     return this._cell.getCellType();
/*  47:    */   }
/*  48:    */   
/*  49:    */   /**
/*  50:    */    * @deprecated
/*  51:    */    */
/*  52:    */   @Internal(since="POI 3.15 beta 3")
/*  53:    */   public CellType getCellTypeEnum()
/*  54:    */   {
/*  55: 74 */     return this._cell.getCellTypeEnum();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getColumnIndex()
/*  59:    */   {
/*  60: 78 */     return this._cell.getColumnIndex();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getErrorCellValue()
/*  64:    */   {
/*  65: 82 */     return this._cell.getErrorCellValue();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public double getNumericCellValue()
/*  69:    */   {
/*  70: 86 */     return this._cell.getNumericCellValue();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getRowIndex()
/*  74:    */   {
/*  75: 90 */     return this._cell.getRowIndex();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public EvaluationSheet getSheet()
/*  79:    */   {
/*  80: 94 */     return this._evalSheet;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String getStringCellValue()
/*  84:    */   {
/*  85: 98 */     return this._cell.getRichStringCellValue().getString();
/*  86:    */   }
/*  87:    */   
/*  88:    */   /**
/*  89:    */    * @deprecated
/*  90:    */    */
/*  91:    */   public int getCachedFormulaResultType()
/*  92:    */   {
/*  93:109 */     return this._cell.getCachedFormulaResultType();
/*  94:    */   }
/*  95:    */   
/*  96:    */   /**
/*  97:    */    * @deprecated
/*  98:    */    */
/*  99:    */   @Internal(since="POI 3.15 beta 3")
/* 100:    */   public CellType getCachedFormulaResultTypeEnum()
/* 101:    */   {
/* 102:119 */     return this._cell.getCachedFormulaResultTypeEnum();
/* 103:    */   }
/* 104:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.streaming.SXSSFEvaluationCell
 * JD-Core Version:    0.7.0.1
 */