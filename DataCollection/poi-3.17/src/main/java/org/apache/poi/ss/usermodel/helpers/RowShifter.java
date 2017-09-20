/*   1:    */ package org.apache.poi.ss.usermodel.helpers;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.apache.poi.ss.formula.FormulaShifter;
/*   8:    */ import org.apache.poi.ss.usermodel.Row;
/*   9:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  10:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*  11:    */ import org.apache.poi.util.Internal;
/*  12:    */ 
/*  13:    */ public abstract class RowShifter
/*  14:    */ {
/*  15:    */   protected final Sheet sheet;
/*  16:    */   
/*  17:    */   public RowShifter(Sheet sh)
/*  18:    */   {
/*  19: 40 */     this.sheet = sh;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public List<CellRangeAddress> shiftMergedRegions(int startRow, int endRow, int n)
/*  23:    */   {
/*  24: 53 */     List<CellRangeAddress> shiftedRegions = new ArrayList();
/*  25: 54 */     Set<Integer> removedIndices = new HashSet();
/*  26:    */     
/*  27: 56 */     int size = this.sheet.getNumMergedRegions();
/*  28: 57 */     for (int i = 0; i < size; i++)
/*  29:    */     {
/*  30: 58 */       CellRangeAddress merged = this.sheet.getMergedRegion(i);
/*  31: 62 */       if (removalNeeded(merged, startRow, endRow, n))
/*  32:    */       {
/*  33: 63 */         removedIndices.add(Integer.valueOf(i));
/*  34:    */       }
/*  35:    */       else
/*  36:    */       {
/*  37: 67 */         boolean inStart = (merged.getFirstRow() >= startRow) || (merged.getLastRow() >= startRow);
/*  38: 68 */         boolean inEnd = (merged.getFirstRow() <= endRow) || (merged.getLastRow() <= endRow);
/*  39: 71 */         if ((inStart) && (inEnd)) {
/*  40: 76 */           if ((!merged.containsRow(startRow - 1)) && (!merged.containsRow(endRow + 1)))
/*  41:    */           {
/*  42: 77 */             merged.setFirstRow(merged.getFirstRow() + n);
/*  43: 78 */             merged.setLastRow(merged.getLastRow() + n);
/*  44:    */             
/*  45: 80 */             shiftedRegions.add(merged);
/*  46: 81 */             removedIndices.add(Integer.valueOf(i));
/*  47:    */           }
/*  48:    */         }
/*  49:    */       }
/*  50:    */     }
/*  51: 85 */     if (!removedIndices.isEmpty()) {
/*  52: 86 */       this.sheet.removeMergedRegions(removedIndices);
/*  53:    */     }
/*  54: 90 */     for (CellRangeAddress region : shiftedRegions) {
/*  55: 91 */       this.sheet.addMergedRegion(region);
/*  56:    */     }
/*  57: 93 */     return shiftedRegions;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private boolean removalNeeded(CellRangeAddress merged, int startRow, int endRow, int n)
/*  61:    */   {
/*  62: 97 */     int movedRows = endRow - startRow + 1;
/*  63:    */     CellRangeAddress overwrite;
/*  64:    */     CellRangeAddress overwrite;
/*  65:102 */     if (n > 0) {
/*  66:104 */       overwrite = new CellRangeAddress(Math.max(endRow + 1, endRow + n - movedRows), endRow + n, 0, 0);
/*  67:    */     } else {
/*  68:107 */       overwrite = new CellRangeAddress(startRow + n, Math.min(startRow - 1, startRow + n + movedRows), 0, 0);
/*  69:    */     }
/*  70:111 */     return merged.intersects(overwrite);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public abstract void updateNamedRanges(FormulaShifter paramFormulaShifter);
/*  74:    */   
/*  75:    */   public abstract void updateFormulas(FormulaShifter paramFormulaShifter);
/*  76:    */   
/*  77:    */   @Internal
/*  78:    */   public abstract void updateRowFormulas(Row paramRow, FormulaShifter paramFormulaShifter);
/*  79:    */   
/*  80:    */   public abstract void updateConditionalFormatting(FormulaShifter paramFormulaShifter);
/*  81:    */   
/*  82:    */   public abstract void updateHyperlinks(FormulaShifter paramFormulaShifter);
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.helpers.RowShifter
 * JD-Core Version:    0.7.0.1
 */