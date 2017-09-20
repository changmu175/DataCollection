/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import org.apache.poi.POIXMLDocumentPart;
/*   7:    */ import org.apache.poi.POIXMLTypeLoader;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.xmlbeans.SchemaType;
/*  11:    */ import org.apache.xmlbeans.XmlException;
/*  12:    */ import org.apache.xmlbeans.XmlOptions;
/*  13:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
/*  14:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
/*  15:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument;
/*  16:    */ import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument.Factory;
/*  17:    */ 
/*  18:    */ public final class XSLFChart
/*  19:    */   extends POIXMLDocumentPart
/*  20:    */ {
/*  21:    */   private CTChartSpace chartSpace;
/*  22:    */   private CTChart chart;
/*  23:    */   
/*  24:    */   protected XSLFChart(PackagePart part)
/*  25:    */     throws IOException, XmlException
/*  26:    */   {
/*  27: 66 */     super(part);
/*  28:    */     
/*  29: 68 */     this.chartSpace = ChartSpaceDocument.Factory.parse(part.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getChartSpace();
/*  30: 69 */     this.chart = this.chartSpace.getChart();
/*  31:    */   }
/*  32:    */   
/*  33:    */   @Internal
/*  34:    */   public CTChartSpace getCTChartSpace()
/*  35:    */   {
/*  36: 79 */     return this.chartSpace;
/*  37:    */   }
/*  38:    */   
/*  39:    */   @Internal
/*  40:    */   public CTChart getCTChart()
/*  41:    */   {
/*  42: 89 */     return this.chart;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void commit()
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 94 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  49: 95 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTChartSpace.type.getName().getNamespaceURI(), "chartSpace", "c"));
/*  50:    */     
/*  51: 97 */     PackagePart part = getPackagePart();
/*  52: 98 */     OutputStream out = part.getOutputStream();
/*  53: 99 */     this.chartSpace.save(out, xmlOptions);
/*  54:100 */     out.close();
/*  55:    */   }
/*  56:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFChart
 * JD-Core Version:    0.7.0.1
 */