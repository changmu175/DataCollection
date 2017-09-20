/*  1:   */ package org.apache.poi.xssf.usermodel.helpers;
/*  2:   */ 
/*  3:   */ import org.apache.poi.xssf.usermodel.XSSFTable;
/*  4:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlColumnPr;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType.Enum;
/*  7:   */ 
/*  8:   */ public class XSSFXmlColumnPr
/*  9:   */ {
/* 10:   */   private XSSFTable table;
/* 11:   */   private CTTableColumn ctTableColumn;
/* 12:   */   private CTXmlColumnPr ctXmlColumnPr;
/* 13:   */   
/* 14:   */   public XSSFXmlColumnPr(XSSFTable table, CTTableColumn ctTableColum, CTXmlColumnPr ctXmlColumnPr)
/* 15:   */   {
/* 16:41 */     this.table = table;
/* 17:42 */     this.ctTableColumn = ctTableColum;
/* 18:43 */     this.ctXmlColumnPr = ctXmlColumnPr;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public long getMapId()
/* 22:   */   {
/* 23:47 */     return this.ctXmlColumnPr.getMapId();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getXPath()
/* 27:   */   {
/* 28:51 */     return this.ctXmlColumnPr.getXpath();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public long getId()
/* 32:   */   {
/* 33:58 */     return this.ctTableColumn.getId();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getLocalXPath()
/* 37:   */   {
/* 38:68 */     StringBuilder localXPath = new StringBuilder();
/* 39:69 */     int numberOfCommonXPathAxis = this.table.getCommonXpath().split("/").length - 1;
/* 40:   */     
/* 41:71 */     String[] xPathTokens = this.ctXmlColumnPr.getXpath().split("/");
/* 42:72 */     for (int i = numberOfCommonXPathAxis; i < xPathTokens.length; i++) {
/* 43:73 */       localXPath.append("/" + xPathTokens[i]);
/* 44:   */     }
/* 45:75 */     return localXPath.toString();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public STXmlDataType.Enum getXmlDataType()
/* 49:   */   {
/* 50:80 */     return this.ctXmlColumnPr.getXmlDataType();
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr
 * JD-Core Version:    0.7.0.1
 */