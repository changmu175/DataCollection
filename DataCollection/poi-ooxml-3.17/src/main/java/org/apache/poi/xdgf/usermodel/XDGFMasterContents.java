/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.MasterContentsDocument.Factory;
/*  5:   */ import java.io.IOException;
/*  6:   */ import org.apache.poi.POIXMLException;
/*  7:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  8:   */ import org.apache.poi.xdgf.exceptions.XDGFException;
/*  9:   */ import org.apache.xmlbeans.XmlException;
/* 10:   */ 
/* 11:   */ public class XDGFMasterContents
/* 12:   */   extends XDGFBaseContents
/* 13:   */ {
/* 14:   */   protected XDGFMaster _master;
/* 15:   */   
/* 16:   */   public XDGFMasterContents(PackagePart part, XDGFDocument document)
/* 17:   */   {
/* 18:40 */     super(part, document);
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected void onDocumentRead()
/* 22:   */   {
/* 23:   */     try
/* 24:   */     {
/* 25:   */       try
/* 26:   */       {
/* 27:49 */         this._pageContents = MasterContentsDocument.Factory.parse(getPackagePart().getInputStream()).getMasterContents();
/* 28:   */       }
/* 29:   */       catch (XmlException e)
/* 30:   */       {
/* 31:51 */         throw new POIXMLException(e);
/* 32:   */       }
/* 33:   */       catch (IOException e)
/* 34:   */       {
/* 35:53 */         throw new POIXMLException(e);
/* 36:   */       }
/* 37:56 */       super.onDocumentRead();
/* 38:   */     }
/* 39:   */     catch (POIXMLException e)
/* 40:   */     {
/* 41:59 */       throw XDGFException.wrap(this, e);
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public XDGFMaster getMaster()
/* 46:   */   {
/* 47:64 */     return this._master;
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected void setMaster(XDGFMaster master)
/* 51:   */   {
/* 52:68 */     this._master = master;
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFMasterContents
 * JD-Core Version:    0.7.0.1
 */