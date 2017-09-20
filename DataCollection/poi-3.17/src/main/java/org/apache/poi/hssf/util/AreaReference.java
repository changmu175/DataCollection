/*  1:   */ package org.apache.poi.hssf.util;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.SpreadsheetVersion;
/*  4:   */ 
/*  5:   */ @Deprecated
/*  6:   */ public final class AreaReference
/*  7:   */   extends org.apache.poi.ss.util.AreaReference
/*  8:   */ {
/*  9:   */   public AreaReference(String reference)
/* 10:   */   {
/* 11:33 */     super(reference, SpreadsheetVersion.EXCEL97);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public AreaReference(CellReference topLeft, CellReference botRight)
/* 15:   */   {
/* 16:41 */     super(topLeft, botRight);
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.util.AreaReference
 * JD-Core Version:    0.7.0.1
 */