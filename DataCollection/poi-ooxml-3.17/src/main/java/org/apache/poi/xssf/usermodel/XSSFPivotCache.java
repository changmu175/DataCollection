/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import org.apache.poi.POIXMLDocumentPart;
/*  6:   */ import org.apache.poi.POIXMLTypeLoader;
/*  7:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  8:   */ import org.apache.xmlbeans.XmlException;
/*  9:   */ import org.apache.xmlbeans.XmlOptions;
/* 10:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache;
/* 11:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache.Factory;
/* 12:   */ 
/* 13:   */ public class XSSFPivotCache
/* 14:   */   extends POIXMLDocumentPart
/* 15:   */ {
/* 16:   */   private CTPivotCache ctPivotCache;
/* 17:   */   
/* 18:   */   public XSSFPivotCache()
/* 19:   */   {
/* 20:38 */     this.ctPivotCache = CTPivotCache.Factory.newInstance();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public XSSFPivotCache(CTPivotCache ctPivotCache)
/* 24:   */   {
/* 25:44 */     this.ctPivotCache = ctPivotCache;
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected XSSFPivotCache(PackagePart part)
/* 29:   */     throws IOException
/* 30:   */   {
/* 31:57 */     super(part);
/* 32:58 */     readFrom(part.getInputStream());
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected void readFrom(InputStream is)
/* 36:   */     throws IOException
/* 37:   */   {
/* 38:   */     try
/* 39:   */     {
/* 40:64 */       XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 41:   */       
/* 42:66 */       options.setLoadReplaceDocumentElement(null);
/* 43:67 */       this.ctPivotCache = CTPivotCache.Factory.parse(is, options);
/* 44:   */     }
/* 45:   */     catch (XmlException e)
/* 46:   */     {
/* 47:69 */       throw new IOException(e.getLocalizedMessage());
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   public CTPivotCache getCTPivotCache()
/* 52:   */   {
/* 53:75 */     return this.ctPivotCache;
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPivotCache
 * JD-Core Version:    0.7.0.1
 */