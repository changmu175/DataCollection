/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.POIXMLDocumentPart;
/*  5:   */ import org.apache.poi.POIXMLTypeLoader;
/*  6:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  7:   */ import org.apache.xmlbeans.XmlException;
/*  8:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTComment;
/*  9:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList;
/* 10:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument;
/* 11:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument.Factory;
/* 12:   */ 
/* 13:   */ public class XSLFComments
/* 14:   */   extends POIXMLDocumentPart
/* 15:   */ {
/* 16:   */   private final CTCommentList _comments;
/* 17:   */   
/* 18:   */   XSLFComments()
/* 19:   */   {
/* 20:41 */     CmLstDocument doc = CmLstDocument.Factory.newInstance();
/* 21:42 */     this._comments = doc.addNewCmLst();
/* 22:   */   }
/* 23:   */   
/* 24:   */   XSLFComments(PackagePart part)
/* 25:   */     throws IOException, XmlException
/* 26:   */   {
/* 27:54 */     super(part);
/* 28:   */     
/* 29:56 */     CmLstDocument doc = CmLstDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 30:   */     
/* 31:58 */     this._comments = doc.getCmLst();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public CTCommentList getCTCommentsList()
/* 35:   */   {
/* 36:62 */     return this._comments;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getNumberOfComments()
/* 40:   */   {
/* 41:66 */     return this._comments.sizeOfCmArray();
/* 42:   */   }
/* 43:   */   
/* 44:   */   public CTComment getCommentAt(int pos)
/* 45:   */   {
/* 46:70 */     return this._comments.getCmArray(pos);
/* 47:   */   }
/* 48:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFComments
 * JD-Core Version:    0.7.0.1
 */