/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.AutoFilter;
/*  4:   */ 
/*  5:   */ public final class XSSFAutoFilter
/*  6:   */   implements AutoFilter
/*  7:   */ {
/*  8:   */   private XSSFSheet _sheet;
/*  9:   */   
/* 10:   */   XSSFAutoFilter(XSSFSheet sheet)
/* 11:   */   {
/* 12:30 */     this._sheet = sheet;
/* 13:   */   }
/* 14:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFAutoFilter
 * JD-Core Version:    0.7.0.1
 */