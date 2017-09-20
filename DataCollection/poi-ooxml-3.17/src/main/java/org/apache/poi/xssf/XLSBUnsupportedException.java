/*  1:   */ package org.apache.poi.xssf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.UnsupportedFileFormatException;
/*  4:   */ 
/*  5:   */ public class XLSBUnsupportedException
/*  6:   */   extends UnsupportedFileFormatException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = 7849681804154571175L;
/*  9:   */   public static final String MESSAGE = ".XLSB Binary Workbooks are not supported";
/* 10:   */   
/* 11:   */   public XLSBUnsupportedException()
/* 12:   */   {
/* 13:31 */     super(".XLSB Binary Workbooks are not supported");
/* 14:   */   }
/* 15:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.XLSBUnsupportedException
 * JD-Core Version:    0.7.0.1
 */