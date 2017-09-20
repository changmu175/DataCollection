/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.PageContentsDocument.Factory;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.util.HashMap;
/*  7:   */ import java.util.Map;
/*  8:   */ import org.apache.poi.POIXMLDocumentPart;
/*  9:   */ import org.apache.poi.POIXMLException;
/* 10:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 11:   */ import org.apache.poi.xdgf.exceptions.XDGFException;
/* 12:   */ import org.apache.xmlbeans.XmlException;
/* 13:   */ 
/* 14:   */ public class XDGFPageContents
/* 15:   */   extends XDGFBaseContents
/* 16:   */ {
/* 17:34 */   protected Map<Long, XDGFMaster> _masters = new HashMap();
/* 18:   */   protected XDGFPage _page;
/* 19:   */   
/* 20:   */   public XDGFPageContents(PackagePart part, XDGFDocument document)
/* 21:   */   {
/* 22:41 */     super(part, document);
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void onDocumentRead()
/* 26:   */   {
/* 27:   */     try
/* 28:   */     {
/* 29:   */       try
/* 30:   */       {
/* 31:48 */         this._pageContents = PageContentsDocument.Factory.parse(getPackagePart().getInputStream()).getPageContents();
/* 32:   */       }
/* 33:   */       catch (XmlException e)
/* 34:   */       {
/* 35:50 */         throw new POIXMLException(e);
/* 36:   */       }
/* 37:   */       catch (IOException e)
/* 38:   */       {
/* 39:52 */         throw new POIXMLException(e);
/* 40:   */       }
/* 41:55 */       for (POIXMLDocumentPart part : getRelations()) {
/* 42:56 */         if ((part instanceof XDGFMasterContents))
/* 43:   */         {
/* 44:60 */           XDGFMaster master = ((XDGFMasterContents)part).getMaster();
/* 45:61 */           this._masters.put(Long.valueOf(master.getID()), master);
/* 46:   */         }
/* 47:   */       }
/* 48:64 */       super.onDocumentRead();
/* 49:66 */       for (XDGFShape shape : this._shapes.values()) {
/* 50:67 */         if (shape.isTopmost()) {
/* 51:68 */           shape.setupMaster(this, null);
/* 52:   */         }
/* 53:   */       }
/* 54:   */     }
/* 55:   */     catch (POIXMLException e)
/* 56:   */     {
/* 57:72 */       throw XDGFException.wrap(this, e);
/* 58:   */     }
/* 59:   */   }
/* 60:   */   
/* 61:   */   public XDGFPage getPage()
/* 62:   */   {
/* 63:80 */     return this._page;
/* 64:   */   }
/* 65:   */   
/* 66:   */   protected void setPage(XDGFPage page)
/* 67:   */   {
/* 68:84 */     this._page = page;
/* 69:   */   }
/* 70:   */   
/* 71:   */   public XDGFMaster getMasterById(long id)
/* 72:   */   {
/* 73:88 */     return (XDGFMaster)this._masters.get(Long.valueOf(id));
/* 74:   */   }
/* 75:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFPageContents
 * JD-Core Version:    0.7.0.1
 */