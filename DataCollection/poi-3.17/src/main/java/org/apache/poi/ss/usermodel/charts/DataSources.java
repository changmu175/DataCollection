/*   1:    */ package org.apache.poi.ss.usermodel.charts;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.CellType;
/*   4:    */ import org.apache.poi.ss.usermodel.CellValue;
/*   5:    */ import org.apache.poi.ss.usermodel.CreationHelper;
/*   6:    */ import org.apache.poi.ss.usermodel.FormulaEvaluator;
/*   7:    */ import org.apache.poi.ss.usermodel.Row;
/*   8:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   9:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  10:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  11:    */ 
/*  12:    */ public class DataSources
/*  13:    */ {
/*  14:    */   public static <T> ChartDataSource<T> fromArray(T[] elements)
/*  15:    */   {
/*  16: 38 */     return new ArrayDataSource(elements);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static ChartDataSource<Number> fromNumericCellRange(Sheet sheet, CellRangeAddress cellRangeAddress)
/*  20:    */   {
/*  21: 42 */     new AbstractCellRangeDataSource(sheet, cellRangeAddress)
/*  22:    */     {
/*  23:    */       public Number getPointAt(int index)
/*  24:    */       {
/*  25: 44 */         CellValue cellValue = getCellValueAt(index);
/*  26: 45 */         if ((cellValue != null) && (cellValue.getCellTypeEnum() == CellType.NUMERIC)) {
/*  27: 46 */           return Double.valueOf(cellValue.getNumberValue());
/*  28:    */         }
/*  29: 48 */         return null;
/*  30:    */       }
/*  31:    */       
/*  32:    */       public boolean isNumeric()
/*  33:    */       {
/*  34: 53 */         return true;
/*  35:    */       }
/*  36:    */     };
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static ChartDataSource<String> fromStringCellRange(Sheet sheet, CellRangeAddress cellRangeAddress)
/*  40:    */   {
/*  41: 59 */     new AbstractCellRangeDataSource(sheet, cellRangeAddress)
/*  42:    */     {
/*  43:    */       public String getPointAt(int index)
/*  44:    */       {
/*  45: 61 */         CellValue cellValue = getCellValueAt(index);
/*  46: 62 */         if ((cellValue != null) && (cellValue.getCellTypeEnum() == CellType.STRING)) {
/*  47: 63 */           return cellValue.getStringValue();
/*  48:    */         }
/*  49: 65 */         return null;
/*  50:    */       }
/*  51:    */       
/*  52:    */       public boolean isNumeric()
/*  53:    */       {
/*  54: 70 */         return false;
/*  55:    */       }
/*  56:    */     };
/*  57:    */   }
/*  58:    */   
/*  59:    */   private static class ArrayDataSource<T>
/*  60:    */     implements ChartDataSource<T>
/*  61:    */   {
/*  62:    */     private final T[] elements;
/*  63:    */     
/*  64:    */     public ArrayDataSource(T[] elements)
/*  65:    */     {
/*  66: 80 */       this.elements = ((Object[])elements.clone());
/*  67:    */     }
/*  68:    */     
/*  69:    */     public int getPointCount()
/*  70:    */     {
/*  71: 84 */       return this.elements.length;
/*  72:    */     }
/*  73:    */     
/*  74:    */     public T getPointAt(int index)
/*  75:    */     {
/*  76: 88 */       return this.elements[index];
/*  77:    */     }
/*  78:    */     
/*  79:    */     public boolean isReference()
/*  80:    */     {
/*  81: 92 */       return false;
/*  82:    */     }
/*  83:    */     
/*  84:    */     public boolean isNumeric()
/*  85:    */     {
/*  86: 96 */       Class<?> arrayComponentType = this.elements.getClass().getComponentType();
/*  87: 97 */       return Number.class.isAssignableFrom(arrayComponentType);
/*  88:    */     }
/*  89:    */     
/*  90:    */     public String getFormulaString()
/*  91:    */     {
/*  92:101 */       throw new UnsupportedOperationException("Literal data source can not be expressed by reference.");
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static abstract class AbstractCellRangeDataSource<T>
/*  97:    */     implements ChartDataSource<T>
/*  98:    */   {
/*  99:    */     private final Sheet sheet;
/* 100:    */     private final CellRangeAddress cellRangeAddress;
/* 101:    */     private final int numOfCells;
/* 102:    */     private FormulaEvaluator evaluator;
/* 103:    */     
/* 104:    */     protected AbstractCellRangeDataSource(Sheet sheet, CellRangeAddress cellRangeAddress)
/* 105:    */     {
/* 106:112 */       this.sheet = sheet;
/* 107:    */       
/* 108:114 */       this.cellRangeAddress = cellRangeAddress.copy();
/* 109:115 */       this.numOfCells = this.cellRangeAddress.getNumberOfCells();
/* 110:116 */       this.evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
/* 111:    */     }
/* 112:    */     
/* 113:    */     public int getPointCount()
/* 114:    */     {
/* 115:120 */       return this.numOfCells;
/* 116:    */     }
/* 117:    */     
/* 118:    */     public boolean isReference()
/* 119:    */     {
/* 120:124 */       return true;
/* 121:    */     }
/* 122:    */     
/* 123:    */     public String getFormulaString()
/* 124:    */     {
/* 125:128 */       return this.cellRangeAddress.formatAsString(this.sheet.getSheetName(), true);
/* 126:    */     }
/* 127:    */     
/* 128:    */     protected CellValue getCellValueAt(int index)
/* 129:    */     {
/* 130:132 */       if ((index < 0) || (index >= this.numOfCells)) {
/* 131:133 */         throw new IndexOutOfBoundsException("Index must be between 0 and " + (this.numOfCells - 1) + " (inclusive), given: " + index);
/* 132:    */       }
/* 133:136 */       int firstRow = this.cellRangeAddress.getFirstRow();
/* 134:137 */       int firstCol = this.cellRangeAddress.getFirstColumn();
/* 135:138 */       int lastCol = this.cellRangeAddress.getLastColumn();
/* 136:139 */       int width = lastCol - firstCol + 1;
/* 137:140 */       int rowIndex = firstRow + index / width;
/* 138:141 */       int cellIndex = firstCol + index % width;
/* 139:142 */       Row row = this.sheet.getRow(rowIndex);
/* 140:143 */       return row == null ? null : this.evaluator.evaluate(row.getCell(cellIndex));
/* 141:    */     }
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.charts.DataSources
 * JD-Core Version:    0.7.0.1
 */