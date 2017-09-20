/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.CellReference;
/*  4:   */ import org.apache.poi.util.LittleEndianInput;
/*  5:   */ 
/*  6:   */ public final class RefPtg
/*  7:   */   extends Ref2DPtgBase
/*  8:   */ {
/*  9:   */   public static final byte sid = 36;
/* 10:   */   
/* 11:   */   public RefPtg(String cellref)
/* 12:   */   {
/* 13:36 */     super(new CellReference(cellref));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public RefPtg(int row, int column, boolean isRowRelative, boolean isColumnRelative)
/* 17:   */   {
/* 18:40 */     super(row, column, isRowRelative, isColumnRelative);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public RefPtg(LittleEndianInput in)
/* 22:   */   {
/* 23:44 */     super(in);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public RefPtg(CellReference cr)
/* 27:   */   {
/* 28:48 */     super(cr);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected byte getSid()
/* 32:   */   {
/* 33:52 */     return 36;
/* 34:   */   }
/* 35:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.RefPtg
 * JD-Core Version:    0.7.0.1
 */