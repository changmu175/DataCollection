/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.POIXMLDocumentPart;
/*  5:   */ import org.apache.poi.POIXMLTypeLoader;
/*  6:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  7:   */ import org.apache.xmlbeans.XmlException;
/*  8:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor;
/*  9:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList;
/* 10:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument;
/* 11:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument.Factory;
/* 12:   */ 
/* 13:   */ public class XSLFCommentAuthors
/* 14:   */   extends POIXMLDocumentPart
/* 15:   */ {
/* 16:   */   private final CTCommentAuthorList _authors;
/* 17:   */   
/* 18:   */   XSLFCommentAuthors()
/* 19:   */   {
/* 20:41 */     CmAuthorLstDocument doc = CmAuthorLstDocument.Factory.newInstance();
/* 21:42 */     this._authors = doc.addNewCmAuthorLst();
/* 22:   */   }
/* 23:   */   
/* 24:   */   XSLFCommentAuthors(PackagePart part)
/* 25:   */     throws IOException, XmlException
/* 26:   */   {
/* 27:54 */     super(part);
/* 28:55 */     CmAuthorLstDocument doc = CmAuthorLstDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 29:   */     
/* 30:57 */     this._authors = doc.getCmAuthorLst();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public CTCommentAuthorList getCTCommentAuthorsList()
/* 34:   */   {
/* 35:61 */     return this._authors;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public CTCommentAuthor getAuthorById(long id)
/* 39:   */   {
/* 40:66 */     for (CTCommentAuthor author : this._authors.getCmAuthorArray()) {
/* 41:67 */       if (author.getId() == id) {
/* 42:68 */         return author;
/* 43:   */       }
/* 44:   */     }
/* 45:71 */     return null;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFCommentAuthors
 * JD-Core Version:    0.7.0.1
 */