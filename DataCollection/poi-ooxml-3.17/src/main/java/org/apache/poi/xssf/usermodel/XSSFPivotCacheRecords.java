/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import javax.xml.namespace.QName;
/*  7:   */ import org.apache.poi.POIXMLDocumentPart;
/*  8:   */ import org.apache.poi.POIXMLTypeLoader;
/*  9:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 10:   */ import org.apache.poi.util.Internal;
/* 11:   */ import org.apache.xmlbeans.SchemaType;
/* 12:   */ import org.apache.xmlbeans.XmlException;
/* 13:   */ import org.apache.xmlbeans.XmlOptions;
/* 14:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords;
/* 15:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords.Factory;
/* 16:   */ 
/* 17:   */ public class XSSFPivotCacheRecords
/* 18:   */   extends POIXMLDocumentPart
/* 19:   */ {
/* 20:   */   private CTPivotCacheRecords ctPivotCacheRecords;
/* 21:   */   
/* 22:   */   public XSSFPivotCacheRecords()
/* 23:   */   {
/* 24:41 */     this.ctPivotCacheRecords = CTPivotCacheRecords.Factory.newInstance();
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected XSSFPivotCacheRecords(PackagePart part)
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:54 */     super(part);
/* 31:55 */     readFrom(part.getInputStream());
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected void readFrom(InputStream is)
/* 35:   */     throws IOException
/* 36:   */   {
/* 37:   */     try
/* 38:   */     {
/* 39:61 */       XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 40:   */       
/* 41:63 */       options.setLoadReplaceDocumentElement(null);
/* 42:64 */       this.ctPivotCacheRecords = CTPivotCacheRecords.Factory.parse(is, options);
/* 43:   */     }
/* 44:   */     catch (XmlException e)
/* 45:   */     {
/* 46:66 */       throw new IOException(e.getLocalizedMessage());
/* 47:   */     }
/* 48:   */   }
/* 49:   */   
/* 50:   */   @Internal
/* 51:   */   public CTPivotCacheRecords getCtPivotCacheRecords()
/* 52:   */   {
/* 53:73 */     return this.ctPivotCacheRecords;
/* 54:   */   }
/* 55:   */   
/* 56:   */   protected void commit()
/* 57:   */     throws IOException
/* 58:   */   {
/* 59:79 */     PackagePart part = getPackagePart();
/* 60:80 */     OutputStream out = part.getOutputStream();
/* 61:81 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 62:   */     
/* 63:83 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTPivotCacheRecords.type.getName().getNamespaceURI(), "pivotCacheRecords"));
/* 64:   */     
/* 65:85 */     this.ctPivotCacheRecords.save(out, xmlOptions);
/* 66:86 */     out.close();
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFPivotCacheRecords
 * JD-Core Version:    0.7.0.1
 */