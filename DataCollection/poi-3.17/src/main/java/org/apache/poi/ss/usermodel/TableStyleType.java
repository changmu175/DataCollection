/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   4:    */ import org.apache.poi.ss.util.CellRangeAddressBase;
/*   5:    */ 
/*   6:    */ public enum TableStyleType
/*   7:    */ {
/*   8: 40 */   wholeTable,  pageFieldLabels,  pageFieldValues,  firstColumnStripe,  secondColumnStripe,  firstRowStripe,  secondRowStripe,  lastColumn,  firstColumn,  headerRow,  totalRow,  firstHeaderCell,  lastHeaderCell,  firstTotalCell,  lastTotalCell,  firstSubtotalColumn,  secondSubtotalColumn,  thirdSubtotalColumn,  blankRow,  firstSubtotalRow,  secondSubtotalRow,  thirdSubtotalRow,  firstColumnSubheading,  secondColumnSubheading,  thirdColumnSubheading,  firstRowSubheading,  secondRowSubheading,  thirdRowSubheading;
/*   9:    */   
/*  10:    */   private TableStyleType() {}
/*  11:    */   
/*  12:    */   public CellRangeAddressBase appliesTo(Table table, Cell cell)
/*  13:    */   {
/*  14:260 */     if ((table == null) || (cell == null)) {
/*  15:260 */       return null;
/*  16:    */     }
/*  17:261 */     if (!cell.getSheet().getSheetName().equals(table.getSheetName())) {
/*  18:261 */       return null;
/*  19:    */     }
/*  20:262 */     if (!table.contains(cell)) {
/*  21:262 */       return null;
/*  22:    */     }
/*  23:264 */     CellRangeAddressBase range = getRange(table, cell);
/*  24:265 */     if ((range != null) && (range.isInRange(cell.getRowIndex(), cell.getColumnIndex()))) {
/*  25:265 */       return range;
/*  26:    */     }
/*  27:267 */     return null;
/*  28:    */   }
/*  29:    */   
/*  30:    */   CellRangeAddressBase getRange(Table table, Cell cell)
/*  31:    */   {
/*  32:276 */     return null;
/*  33:    */   }
/*  34:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.TableStyleType
 * JD-Core Version:    0.7.0.1
 */