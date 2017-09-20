/*  1:   */ package org.apache.poi.xssf.usermodel.helpers;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.util.CellReference;
/*  4:   */ import org.apache.poi.xssf.model.SingleXmlCells;
/*  5:   */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  6:   */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*  7:   */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  8:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell;
/*  9:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlCellPr;
/* 10:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlPr;
/* 11:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType.Enum;
/* 12:   */ 
/* 13:   */ public class XSSFSingleXmlCell
/* 14:   */ {
/* 15:   */   private CTSingleXmlCell singleXmlCell;
/* 16:   */   private SingleXmlCells parent;
/* 17:   */   
/* 18:   */   public XSSFSingleXmlCell(CTSingleXmlCell singleXmlCell, SingleXmlCells parent)
/* 19:   */   {
/* 20:46 */     this.singleXmlCell = singleXmlCell;
/* 21:47 */     this.parent = parent;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public XSSFCell getReferencedCell()
/* 25:   */   {
/* 26:55 */     XSSFCell cell = null;
/* 27:   */     
/* 28:   */ 
/* 29:58 */     CellReference cellReference = new CellReference(this.singleXmlCell.getR());
/* 30:   */     
/* 31:60 */     XSSFRow row = this.parent.getXSSFSheet().getRow(cellReference.getRow());
/* 32:61 */     if (row == null) {
/* 33:62 */       row = this.parent.getXSSFSheet().createRow(cellReference.getRow());
/* 34:   */     }
/* 35:65 */     cell = row.getCell(cellReference.getCol());
/* 36:66 */     if (cell == null) {
/* 37:67 */       cell = row.createCell(cellReference.getCol());
/* 38:   */     }
/* 39:71 */     return cell;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String getXpath()
/* 43:   */   {
/* 44:75 */     CTXmlCellPr xmlCellPr = this.singleXmlCell.getXmlCellPr();
/* 45:76 */     CTXmlPr xmlPr = xmlCellPr.getXmlPr();
/* 46:77 */     String xpath = xmlPr.getXpath();
/* 47:78 */     return xpath;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public long getMapId()
/* 51:   */   {
/* 52:82 */     return this.singleXmlCell.getXmlCellPr().getXmlPr().getMapId();
/* 53:   */   }
/* 54:   */   
/* 55:   */   public STXmlDataType.Enum getXmlDataType()
/* 56:   */   {
/* 57:86 */     CTXmlCellPr xmlCellPr = this.singleXmlCell.getXmlCellPr();
/* 58:87 */     CTXmlPr xmlPr = xmlCellPr.getXmlPr();
/* 59:88 */     return xmlPr.getXmlDataType();
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.helpers.XSSFSingleXmlCell
 * JD-Core Version:    0.7.0.1
 */