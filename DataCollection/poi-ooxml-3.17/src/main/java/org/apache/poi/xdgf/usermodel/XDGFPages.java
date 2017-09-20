/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.PageType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.PagesDocument;
/*  5:   */ import com.microsoft.schemas.office.visio.x2012.main.PagesDocument.Factory;
/*  6:   */ import com.microsoft.schemas.office.visio.x2012.main.PagesType;
/*  7:   */ import com.microsoft.schemas.office.visio.x2012.main.RelType;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.util.ArrayList;
/* 10:   */ import java.util.Collections;
/* 11:   */ import java.util.List;
/* 12:   */ import org.apache.poi.POIXMLDocumentPart;
/* 13:   */ import org.apache.poi.POIXMLException;
/* 14:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 15:   */ import org.apache.poi.util.Internal;
/* 16:   */ import org.apache.poi.xdgf.exceptions.XDGFException;
/* 17:   */ import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;
/* 18:   */ import org.apache.xmlbeans.XmlException;
/* 19:   */ 
/* 20:   */ public class XDGFPages
/* 21:   */   extends XDGFXMLDocumentPart
/* 22:   */ {
/* 23:   */   PagesType _pagesObject;
/* 24:45 */   List<XDGFPage> _pages = new ArrayList();
/* 25:   */   
/* 26:   */   public XDGFPages(PackagePart part, XDGFDocument document)
/* 27:   */   {
/* 28:51 */     super(part, document);
/* 29:   */   }
/* 30:   */   
/* 31:   */   @Internal
/* 32:   */   PagesType getXmlObject()
/* 33:   */   {
/* 34:56 */     return this._pagesObject;
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected void onDocumentRead()
/* 38:   */   {
/* 39:   */     try
/* 40:   */     {
/* 41:   */       try
/* 42:   */       {
/* 43:63 */         this._pagesObject = PagesDocument.Factory.parse(getPackagePart().getInputStream()).getPages();
/* 44:   */       }
/* 45:   */       catch (XmlException e)
/* 46:   */       {
/* 47:65 */         throw new POIXMLException(e);
/* 48:   */       }
/* 49:   */       catch (IOException e)
/* 50:   */       {
/* 51:67 */         throw new POIXMLException(e);
/* 52:   */       }
/* 53:71 */       for (PageType pageSettings : this._pagesObject.getPageArray())
/* 54:   */       {
/* 55:73 */         String relId = pageSettings.getRel().getId();
/* 56:   */         
/* 57:75 */         POIXMLDocumentPart pageContentsPart = getRelationById(relId);
/* 58:76 */         if (pageContentsPart == null) {
/* 59:77 */           throw new POIXMLException("PageSettings relationship for " + relId + " not found");
/* 60:   */         }
/* 61:79 */         if (!(pageContentsPart instanceof XDGFPageContents)) {
/* 62:80 */           throw new POIXMLException("Unexpected pages relationship for " + relId + ": " + pageContentsPart);
/* 63:   */         }
/* 64:82 */         XDGFPageContents contents = (XDGFPageContents)pageContentsPart;
/* 65:83 */         XDGFPage page = new XDGFPage(pageSettings, contents, this._document, this);
/* 66:   */         
/* 67:85 */         contents.onDocumentRead();
/* 68:   */         
/* 69:87 */         this._pages.add(page);
/* 70:   */       }
/* 71:   */     }
/* 72:   */     catch (POIXMLException e)
/* 73:   */     {
/* 74:91 */       throw XDGFException.wrap(this, e);
/* 75:   */     }
/* 76:   */   }
/* 77:   */   
/* 78:   */   public List<XDGFPage> getPageList()
/* 79:   */   {
/* 80:99 */     return Collections.unmodifiableList(this._pages);
/* 81:   */   }
/* 82:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFPages
 * JD-Core Version:    0.7.0.1
 */