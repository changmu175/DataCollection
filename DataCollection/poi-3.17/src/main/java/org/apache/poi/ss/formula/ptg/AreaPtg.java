/*  1:   */ package org.apache.poi.ss.formula.ptg;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.SpreadsheetVersion;
/*  4:   */ import org.apache.poi.ss.util.AreaReference;
/*  5:   */ import org.apache.poi.util.LittleEndianInput;
/*  6:   */ import org.apache.poi.util.Removal;
/*  7:   */ 
/*  8:   */ public final class AreaPtg
/*  9:   */   extends Area2DPtgBase
/* 10:   */ {
/* 11:   */   public static final short sid = 37;
/* 12:   */   
/* 13:   */   public AreaPtg(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative)
/* 14:   */   {
/* 15:33 */     super(firstRow, lastRow, firstColumn, lastColumn, firstRowRelative, lastRowRelative, firstColRelative, lastColRelative);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public AreaPtg(LittleEndianInput in)
/* 19:   */   {
/* 20:37 */     super(in);
/* 21:   */   }
/* 22:   */   
/* 23:   */   @Deprecated
/* 24:   */   @Removal(version="3.19")
/* 25:   */   public AreaPtg(String arearef)
/* 26:   */   {
/* 27:46 */     this(new AreaReference(arearef, SpreadsheetVersion.EXCEL97));
/* 28:   */   }
/* 29:   */   
/* 30:   */   public AreaPtg(AreaReference arearef)
/* 31:   */   {
/* 32:50 */     super(arearef);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected byte getSid()
/* 36:   */   {
/* 37:55 */     return 37;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AreaPtg
 * JD-Core Version:    0.7.0.1
 */