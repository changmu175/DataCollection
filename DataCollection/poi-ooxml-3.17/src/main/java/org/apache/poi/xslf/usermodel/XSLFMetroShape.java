/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.util.List;
/*  6:   */ import org.apache.poi.POIXMLTypeLoader;
/*  7:   */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  8:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  9:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/* 10:   */ import org.apache.poi.openxml4j.opc.PackagePartName;
/* 11:   */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/* 12:   */ import org.apache.poi.sl.usermodel.Shape;
/* 13:   */ import org.apache.poi.util.Internal;
/* 14:   */ import org.apache.xmlbeans.XmlException;
/* 15:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/* 16:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape.Factory;
/* 17:   */ 
/* 18:   */ @Internal
/* 19:   */ public class XSLFMetroShape
/* 20:   */ {
/* 21:   */   public static Shape<?, ?> parseShape(byte[] metroBytes)
/* 22:   */     throws InvalidFormatException, IOException, XmlException
/* 23:   */   {
/* 24:48 */     PackagePartName shapePN = PackagingURIHelper.createPartName("/drs/shapexml.xml");
/* 25:49 */     OPCPackage pkg = null;
/* 26:   */     try
/* 27:   */     {
/* 28:51 */       pkg = OPCPackage.open(new ByteArrayInputStream(metroBytes));
/* 29:52 */       PackagePart shapePart = pkg.getPart(shapePN);
/* 30:53 */       CTGroupShape gs = CTGroupShape.Factory.parse(shapePart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 31:54 */       XSLFGroupShape xgs = new XSLFGroupShape(gs, null);
/* 32:55 */       return (Shape)xgs.getShapes().get(0);
/* 33:   */     }
/* 34:   */     finally
/* 35:   */     {
/* 36:57 */       if (pkg != null) {
/* 37:58 */         pkg.close();
/* 38:   */       }
/* 39:   */     }
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFMetroShape
 * JD-Core Version:    0.7.0.1
 */