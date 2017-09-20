/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLDocumentPart;
/*  4:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  5:   */ 
/*  6:   */ public class XSSFVBAPart
/*  7:   */   extends POIXMLDocumentPart
/*  8:   */ {
/*  9:   */   protected XSSFVBAPart() {}
/* 10:   */   
/* 11:   */   protected XSSFVBAPart(PackagePart part)
/* 12:   */   {
/* 13:40 */     super(part);
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected void prepareForCommit() {}
/* 17:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFVBAPart
 * JD-Core Version:    0.7.0.1
 */