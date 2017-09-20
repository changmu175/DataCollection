/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.AutoFilter;
/*  4:   */ 
/*  5:   */ public final class HSSFAutoFilter
/*  6:   */   implements AutoFilter
/*  7:   */ {
/*  8:   */   private HSSFSheet _sheet;
/*  9:   */   
/* 10:   */   HSSFAutoFilter(HSSFSheet sheet)
/* 11:   */   {
/* 12:28 */     this._sheet = sheet;
/* 13:   */   }
/* 14:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFAutoFilter
 * JD-Core Version:    0.7.0.1
 */