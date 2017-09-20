/*   1:    */ package org.apache.poi.ss.util.cellwalk;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.Cell;
/*   4:    */ import org.apache.poi.ss.usermodel.CellType;
/*   5:    */ import org.apache.poi.ss.usermodel.Row;
/*   6:    */ import org.apache.poi.ss.usermodel.Sheet;
/*   7:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   8:    */ 
/*   9:    */ public class CellWalk
/*  10:    */ {
/*  11:    */   private Sheet sheet;
/*  12:    */   private CellRangeAddress range;
/*  13:    */   private boolean traverseEmptyCells;
/*  14:    */   
/*  15:    */   public CellWalk(Sheet sheet, CellRangeAddress range)
/*  16:    */   {
/*  17: 39 */     this.sheet = sheet;
/*  18: 40 */     this.range = range;
/*  19: 41 */     this.traverseEmptyCells = false;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public boolean isTraverseEmptyCells()
/*  23:    */   {
/*  24: 52 */     return this.traverseEmptyCells;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setTraverseEmptyCells(boolean traverseEmptyCells)
/*  28:    */   {
/*  29: 61 */     this.traverseEmptyCells = traverseEmptyCells;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void traverse(CellHandler handler)
/*  33:    */   {
/*  34: 70 */     int firstRow = this.range.getFirstRow();
/*  35: 71 */     int lastRow = this.range.getLastRow();
/*  36: 72 */     int firstColumn = this.range.getFirstColumn();
/*  37: 73 */     int lastColumn = this.range.getLastColumn();
/*  38: 74 */     int width = lastColumn - firstColumn + 1;
/*  39: 75 */     SimpleCellWalkContext ctx = new SimpleCellWalkContext(null);
/*  40: 76 */     Row currentRow = null;
/*  41: 77 */     Cell currentCell = null;
/*  42: 79 */     for (ctx.rowNumber = firstRow; ctx.rowNumber <= lastRow; ctx.rowNumber += 1)
/*  43:    */     {
/*  44: 80 */       currentRow = this.sheet.getRow(ctx.rowNumber);
/*  45: 81 */       if (currentRow != null) {
/*  46: 84 */         for (ctx.colNumber = firstColumn; ctx.colNumber <= lastColumn; ctx.colNumber += 1)
/*  47:    */         {
/*  48: 85 */           currentCell = currentRow.getCell(ctx.colNumber);
/*  49: 87 */           if (currentCell != null) {
/*  50: 90 */             if ((!isEmpty(currentCell)) || (this.traverseEmptyCells))
/*  51:    */             {
/*  52: 94 */               ctx.ordinalNumber = ((ctx.rowNumber - firstRow) * width + (ctx.colNumber - firstColumn + 1));
/*  53:    */               
/*  54:    */ 
/*  55:    */ 
/*  56: 98 */               handler.onCell(currentCell, ctx);
/*  57:    */             }
/*  58:    */           }
/*  59:    */         }
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   private boolean isEmpty(Cell cell)
/*  65:    */   {
/*  66:104 */     return cell.getCellTypeEnum() == CellType.BLANK;
/*  67:    */   }
/*  68:    */   
/*  69:    */   private static class SimpleCellWalkContext
/*  70:    */     implements CellWalkContext
/*  71:    */   {
/*  72:113 */     public long ordinalNumber = 0L;
/*  73:114 */     public int rowNumber = 0;
/*  74:115 */     public int colNumber = 0;
/*  75:    */     
/*  76:    */     public long getOrdinalNumber()
/*  77:    */     {
/*  78:118 */       return this.ordinalNumber;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public int getRowNumber()
/*  82:    */     {
/*  83:122 */       return this.rowNumber;
/*  84:    */     }
/*  85:    */     
/*  86:    */     public int getColumnNumber()
/*  87:    */     {
/*  88:126 */       return this.colNumber;
/*  89:    */     }
/*  90:    */   }
/*  91:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.cellwalk.CellWalk
 * JD-Core Version:    0.7.0.1
 */