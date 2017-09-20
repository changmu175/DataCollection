/*  1:   */ package org.apache.poi.ss.formula;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.SpreadsheetVersion;
/*  4:   */ import org.apache.poi.ss.formula.ptg.AreaPtg;
/*  5:   */ import org.apache.poi.ss.formula.ptg.AreaPtgBase;
/*  6:   */ import org.apache.poi.ss.formula.ptg.OperandPtg;
/*  7:   */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  8:   */ import org.apache.poi.ss.formula.ptg.RefPtg;
/*  9:   */ import org.apache.poi.ss.formula.ptg.RefPtgBase;
/* 10:   */ 
/* 11:   */ public class SharedFormula
/* 12:   */ {
/* 13:   */   private final int _columnWrappingMask;
/* 14:   */   private final int _rowWrappingMask;
/* 15:   */   
/* 16:   */   public SharedFormula(SpreadsheetVersion ssVersion)
/* 17:   */   {
/* 18:31 */     this._columnWrappingMask = ssVersion.getLastColumnIndex();
/* 19:32 */     this._rowWrappingMask = ssVersion.getLastRowIndex();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public Ptg[] convertSharedFormulas(Ptg[] ptgs, int formulaRow, int formulaColumn)
/* 23:   */   {
/* 24:46 */     Ptg[] newPtgStack = new Ptg[ptgs.length];
/* 25:48 */     for (int k = 0; k < ptgs.length; k++)
/* 26:   */     {
/* 27:49 */       Ptg ptg = ptgs[k];
/* 28:50 */       byte originalOperandClass = -1;
/* 29:51 */       if (!ptg.isBaseToken()) {
/* 30:52 */         originalOperandClass = ptg.getPtgClass();
/* 31:   */       }
/* 32:54 */       if ((ptg instanceof RefPtgBase))
/* 33:   */       {
/* 34:55 */         RefPtgBase refNPtg = (RefPtgBase)ptg;
/* 35:56 */         ptg = new RefPtg(fixupRelativeRow(formulaRow, refNPtg.getRow(), refNPtg.isRowRelative()), fixupRelativeColumn(formulaColumn, refNPtg.getColumn(), refNPtg.isColRelative()), refNPtg.isRowRelative(), refNPtg.isColRelative());
/* 36:   */         
/* 37:   */ 
/* 38:   */ 
/* 39:60 */         ptg.setClass(originalOperandClass);
/* 40:   */       }
/* 41:61 */       else if ((ptg instanceof AreaPtgBase))
/* 42:   */       {
/* 43:62 */         AreaPtgBase areaNPtg = (AreaPtgBase)ptg;
/* 44:63 */         ptg = new AreaPtg(fixupRelativeRow(formulaRow, areaNPtg.getFirstRow(), areaNPtg.isFirstRowRelative()), fixupRelativeRow(formulaRow, areaNPtg.getLastRow(), areaNPtg.isLastRowRelative()), fixupRelativeColumn(formulaColumn, areaNPtg.getFirstColumn(), areaNPtg.isFirstColRelative()), fixupRelativeColumn(formulaColumn, areaNPtg.getLastColumn(), areaNPtg.isLastColRelative()), areaNPtg.isFirstRowRelative(), areaNPtg.isLastRowRelative(), areaNPtg.isFirstColRelative(), areaNPtg.isLastColRelative());
/* 45:   */         
/* 46:   */ 
/* 47:   */ 
/* 48:   */ 
/* 49:   */ 
/* 50:   */ 
/* 51:   */ 
/* 52:71 */         ptg.setClass(originalOperandClass);
/* 53:   */       }
/* 54:72 */       else if ((ptg instanceof OperandPtg))
/* 55:   */       {
/* 56:74 */         ptg = ((OperandPtg)ptg).copy();
/* 57:   */       }
/* 58:78 */       newPtgStack[k] = ptg;
/* 59:   */     }
/* 60:80 */     return newPtgStack;
/* 61:   */   }
/* 62:   */   
/* 63:   */   private int fixupRelativeColumn(int currentcolumn, int column, boolean relative)
/* 64:   */   {
/* 65:84 */     if (relative) {
/* 66:86 */       return column + currentcolumn & this._columnWrappingMask;
/* 67:   */     }
/* 68:88 */     return column;
/* 69:   */   }
/* 70:   */   
/* 71:   */   private int fixupRelativeRow(int currentrow, int row, boolean relative)
/* 72:   */   {
/* 73:92 */     if (relative) {
/* 74:93 */       return row + currentrow & this._rowWrappingMask;
/* 75:   */     }
/* 76:95 */     return row;
/* 77:   */   }
/* 78:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.SharedFormula
 * JD-Core Version:    0.7.0.1
 */