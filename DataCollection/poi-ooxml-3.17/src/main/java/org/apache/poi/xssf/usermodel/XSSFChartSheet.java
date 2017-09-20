/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import javax.xml.namespace.QName;
/*   9:    */ import org.apache.poi.POIXMLException;
/*  10:    */ import org.apache.poi.POIXMLTypeLoader;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.xmlbeans.SchemaType;
/*  13:    */ import org.apache.xmlbeans.XmlException;
/*  14:    */ import org.apache.xmlbeans.XmlOptions;
/*  15:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument.Factory;
/*  20:    */ 
/*  21:    */ public class XSSFChartSheet
/*  22:    */   extends XSSFSheet
/*  23:    */ {
/*  24: 49 */   private static final byte[] BLANK_WORKSHEET = ;
/*  25:    */   protected CTChartsheet chartsheet;
/*  26:    */   
/*  27:    */   protected XSSFChartSheet(PackagePart part)
/*  28:    */   {
/*  29: 57 */     super(part);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected void read(InputStream is)
/*  33:    */     throws IOException
/*  34:    */   {
/*  35: 62 */     super.read(new ByteArrayInputStream(BLANK_WORKSHEET));
/*  36:    */     try
/*  37:    */     {
/*  38: 65 */       this.chartsheet = ChartsheetDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getChartsheet();
/*  39:    */     }
/*  40:    */     catch (XmlException e)
/*  41:    */     {
/*  42: 67 */       throw new POIXMLException(e);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public CTChartsheet getCTChartsheet()
/*  47:    */   {
/*  48: 77 */     return this.chartsheet;
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected CTDrawing getCTDrawing()
/*  52:    */   {
/*  53: 82 */     return this.chartsheet.getDrawing();
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected CTLegacyDrawing getCTLegacyDrawing()
/*  57:    */   {
/*  58: 87 */     return this.chartsheet.getLegacyDrawing();
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void write(OutputStream out)
/*  62:    */     throws IOException
/*  63:    */   {
/*  64: 92 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  65: 93 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTChartsheet.type.getName().getNamespaceURI(), "chartsheet"));
/*  66:    */     
/*  67: 95 */     this.chartsheet.save(out, xmlOptions);
/*  68:    */   }
/*  69:    */   
/*  70:    */   private static byte[] blankWorksheet()
/*  71:    */   {
/*  72:100 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  73:    */     try
/*  74:    */     {
/*  75:102 */       new XSSFSheet().write(out);
/*  76:    */     }
/*  77:    */     catch (IOException e)
/*  78:    */     {
/*  79:104 */       throw new RuntimeException(e);
/*  80:    */     }
/*  81:106 */     return out.toByteArray();
/*  82:    */   }
/*  83:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFChartSheet
 * JD-Core Version:    0.7.0.1
 */