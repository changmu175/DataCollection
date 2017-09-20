/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import java.util.Date;
/*   5:    */ import org.apache.poi.ss.usermodel.Cell;
/*   6:    */ import org.apache.poi.ss.usermodel.Row;
/*   7:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   8:    */ import org.apache.poi.ss.usermodel.Workbook;
/*   9:    */ 
/*  10:    */ public class SheetBuilder
/*  11:    */ {
/*  12:    */   private final Workbook workbook;
/*  13:    */   private final Object[][] cells;
/*  14: 38 */   private boolean shouldCreateEmptyCells = false;
/*  15: 39 */   private String sheetName = null;
/*  16:    */   
/*  17:    */   public SheetBuilder(Workbook workbook, Object[][] cells)
/*  18:    */   {
/*  19: 42 */     this.workbook = workbook;
/*  20: 43 */     this.cells = ((Object[][])cells.clone());
/*  21:    */   }
/*  22:    */   
/*  23:    */   public boolean getCreateEmptyCells()
/*  24:    */   {
/*  25: 54 */     return this.shouldCreateEmptyCells;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public SheetBuilder setCreateEmptyCells(boolean shouldCreateEmptyCells)
/*  29:    */   {
/*  30: 65 */     this.shouldCreateEmptyCells = shouldCreateEmptyCells;
/*  31: 66 */     return this;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SheetBuilder setSheetName(String sheetName)
/*  35:    */   {
/*  36: 76 */     this.sheetName = sheetName;
/*  37: 77 */     return this;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Sheet build()
/*  41:    */   {
/*  42:100 */     Sheet sheet = this.sheetName == null ? this.workbook.createSheet() : this.workbook.createSheet(this.sheetName);
/*  43:101 */     Row currentRow = null;
/*  44:102 */     Cell currentCell = null;
/*  45:104 */     for (int rowIndex = 0; rowIndex < this.cells.length; rowIndex++)
/*  46:    */     {
/*  47:105 */       Object[] rowArray = this.cells[rowIndex];
/*  48:106 */       currentRow = sheet.createRow(rowIndex);
/*  49:108 */       for (int cellIndex = 0; cellIndex < rowArray.length; cellIndex++)
/*  50:    */       {
/*  51:109 */         Object cellValue = rowArray[cellIndex];
/*  52:110 */         if ((cellValue != null) || (this.shouldCreateEmptyCells))
/*  53:    */         {
/*  54:111 */           currentCell = currentRow.createCell(cellIndex);
/*  55:112 */           setCellValue(currentCell, cellValue);
/*  56:    */         }
/*  57:    */       }
/*  58:    */     }
/*  59:116 */     return sheet;
/*  60:    */   }
/*  61:    */   
/*  62:    */   private void setCellValue(Cell cell, Object value)
/*  63:    */   {
/*  64:126 */     if ((value == null) || (cell == null)) {
/*  65:127 */       return;
/*  66:    */     }
/*  67:128 */     if ((value instanceof Number))
/*  68:    */     {
/*  69:129 */       double doubleValue = ((Number)value).doubleValue();
/*  70:130 */       cell.setCellValue(doubleValue);
/*  71:    */     }
/*  72:131 */     else if ((value instanceof Date))
/*  73:    */     {
/*  74:132 */       cell.setCellValue((Date)value);
/*  75:    */     }
/*  76:133 */     else if ((value instanceof Calendar))
/*  77:    */     {
/*  78:134 */       cell.setCellValue((Calendar)value);
/*  79:    */     }
/*  80:135 */     else if (isFormulaDefinition(value))
/*  81:    */     {
/*  82:136 */       cell.setCellFormula(getFormula(value));
/*  83:    */     }
/*  84:    */     else
/*  85:    */     {
/*  86:138 */       cell.setCellValue(value.toString());
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   private boolean isFormulaDefinition(Object obj)
/*  91:    */   {
/*  92:143 */     if ((obj instanceof String))
/*  93:    */     {
/*  94:144 */       String str = (String)obj;
/*  95:145 */       if (str.length() < 2) {
/*  96:146 */         return false;
/*  97:    */       }
/*  98:148 */       return ((String)obj).charAt(0) == '=';
/*  99:    */     }
/* 100:151 */     return false;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private String getFormula(Object obj)
/* 104:    */   {
/* 105:156 */     return ((String)obj).substring(1);
/* 106:    */   }
/* 107:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.SheetBuilder
 * JD-Core Version:    0.7.0.1
 */