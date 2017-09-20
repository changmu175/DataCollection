/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1;
/*   4:    */ import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentDocument1.Factory;
/*   5:    */ import com.microsoft.schemas.office.visio.x2012.main.VisioDocumentType;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Collection;
/*  10:    */ import java.util.List;
/*  11:    */ import org.apache.poi.POIXMLDocument;
/*  12:    */ import org.apache.poi.POIXMLDocumentPart;
/*  13:    */ import org.apache.poi.POIXMLException;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  15:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  17:    */ import org.apache.poi.util.PackageHelper;
/*  18:    */ import org.apache.xmlbeans.XmlException;
/*  19:    */ 
/*  20:    */ public class XmlVisioDocument
/*  21:    */   extends POIXMLDocument
/*  22:    */ {
/*  23:    */   protected XDGFPages _pages;
/*  24:    */   protected XDGFMasters _masters;
/*  25:    */   protected XDGFDocument _document;
/*  26:    */   
/*  27:    */   public XmlVisioDocument(OPCPackage pkg)
/*  28:    */     throws IOException
/*  29:    */   {
/*  30: 64 */     super(pkg, "http://schemas.microsoft.com/visio/2010/relationships/document");
/*  31:    */     VisioDocumentType document;
/*  32:    */     try
/*  33:    */     {
/*  34: 69 */       document = VisioDocumentDocument1.Factory.parse(getPackagePart().getInputStream()).getVisioDocument();
/*  35:    */     }
/*  36:    */     catch (XmlException e)
/*  37:    */     {
/*  38: 71 */       throw new POIXMLException(e);
/*  39:    */     }
/*  40:    */     catch (IOException e)
/*  41:    */     {
/*  42: 73 */       throw new POIXMLException(e);
/*  43:    */     }
/*  44: 76 */     this._document = new XDGFDocument(document);
/*  45:    */     
/*  46:    */ 
/*  47: 79 */     load(new XDGFFactory(this._document));
/*  48:    */   }
/*  49:    */   
/*  50:    */   public XmlVisioDocument(InputStream is)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53: 83 */     this(PackageHelper.open(is));
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void onDocumentRead()
/*  57:    */     throws IOException
/*  58:    */   {
/*  59: 95 */     for (POIXMLDocumentPart part : getRelations()) {
/*  60: 98 */       if ((part instanceof XDGFPages)) {
/*  61: 99 */         this._pages = ((XDGFPages)part);
/*  62:101 */       } else if ((part instanceof XDGFMasters)) {
/*  63:102 */         this._masters = ((XDGFMasters)part);
/*  64:    */       }
/*  65:    */     }
/*  66:105 */     if (this._masters != null) {
/*  67:106 */       this._masters.onDocumentRead();
/*  68:    */     }
/*  69:108 */     this._pages.onDocumentRead();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public List<PackagePart> getAllEmbedds()
/*  73:    */     throws OpenXML4JException
/*  74:    */   {
/*  75:116 */     return new ArrayList();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Collection<XDGFPage> getPages()
/*  79:    */   {
/*  80:127 */     return this._pages.getPageList();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public XDGFStyleSheet getStyleById(long id)
/*  84:    */   {
/*  85:131 */     return this._document.getStyleById(id);
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XmlVisioDocument
 * JD-Core Version:    0.7.0.1
 */