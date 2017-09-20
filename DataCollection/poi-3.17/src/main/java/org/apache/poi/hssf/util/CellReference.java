/*  1:   */ package org.apache.poi.hssf.util;
/*  2:   */ 
/*  3:   */ public final class CellReference
/*  4:   */   extends org.apache.poi.ss.util.CellReference
/*  5:   */ {
/*  6:   */   public CellReference(String cellRef)
/*  7:   */   {
/*  8:31 */     super(cellRef);
/*  9:   */   }
/* 10:   */   
/* 11:   */   public CellReference(int pRow, int pCol)
/* 12:   */   {
/* 13:35 */     super(pRow, pCol, true, true);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol)
/* 17:   */   {
/* 18:39 */     super(null, pRow, pCol, pAbsRow, pAbsCol);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol)
/* 22:   */   {
/* 23:43 */     super(pSheetName, pRow, pCol, pAbsRow, pAbsCol);
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.CellReference
 * JD-Core Version:    0.7.0.1
 */