/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ public enum XSSFWorkbookType
/*  4:   */ {
/*  5:26 */   XLSX(XSSFRelation.WORKBOOK.getContentType(), "xlsx"),  XLSM(XSSFRelation.MACROS_WORKBOOK.getContentType(), "xlsm");
/*  6:   */   
/*  7:   */   private final String _contentType;
/*  8:   */   private final String _extension;
/*  9:   */   
/* 10:   */   private XSSFWorkbookType(String contentType, String extension)
/* 11:   */   {
/* 12:33 */     this._contentType = contentType;
/* 13:34 */     this._extension = extension;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getContentType()
/* 17:   */   {
/* 18:38 */     return this._contentType;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getExtension()
/* 22:   */   {
/* 23:42 */     return this._extension;
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFWorkbookType
 * JD-Core Version:    0.7.0.1
 */