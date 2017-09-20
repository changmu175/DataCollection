/*  1:   */ package org.apache.poi.xdgf.xml;
/*  2:   */ 
/*  3:   */ import org.apache.poi.POIXMLDocumentPart;
/*  4:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  5:   */ import org.apache.poi.xdgf.usermodel.XDGFDocument;
/*  6:   */ 
/*  7:   */ public class XDGFXMLDocumentPart
/*  8:   */   extends POIXMLDocumentPart
/*  9:   */ {
/* 10:   */   protected XDGFDocument _document;
/* 11:   */   
/* 12:   */   public XDGFXMLDocumentPart(PackagePart part, XDGFDocument document)
/* 13:   */   {
/* 14:31 */     super(part);
/* 15:32 */     this._document = document;
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.xml.XDGFXMLDocumentPart
 * JD-Core Version:    0.7.0.1
 */