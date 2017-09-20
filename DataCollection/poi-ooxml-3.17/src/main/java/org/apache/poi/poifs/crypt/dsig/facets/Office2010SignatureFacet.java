/*  1:   */ package org.apache.poi.poifs.crypt.dsig.facets;
/*  2:   */ 
/*  3:   */ import javax.xml.crypto.MarshalException;
/*  4:   */ import org.apache.poi.POIXMLTypeLoader;
/*  5:   */ import org.apache.xmlbeans.XmlException;
/*  6:   */ import org.etsi.uri.x01903.v13.QualifyingPropertiesType;
/*  7:   */ import org.etsi.uri.x01903.v13.QualifyingPropertiesType.Factory;
/*  8:   */ import org.etsi.uri.x01903.v13.UnsignedPropertiesType;
/*  9:   */ import org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType;
/* 10:   */ import org.w3c.dom.Document;
/* 11:   */ import org.w3c.dom.Node;
/* 12:   */ import org.w3c.dom.NodeList;
/* 13:   */ 
/* 14:   */ public class Office2010SignatureFacet
/* 15:   */   extends SignatureFacet
/* 16:   */ {
/* 17:   */   public void postSign(Document document)
/* 18:   */     throws MarshalException
/* 19:   */   {
/* 20:54 */     NodeList nl = document.getElementsByTagNameNS("http://uri.etsi.org/01903/v1.3.2#", "QualifyingProperties");
/* 21:55 */     if (nl.getLength() != 1) {
/* 22:56 */       throw new MarshalException("no XAdES-BES extension present");
/* 23:   */     }
/* 24:   */     QualifyingPropertiesType qualProps;
/* 25:   */     try
/* 26:   */     {
/* 27:61 */       qualProps = QualifyingPropertiesType.Factory.parse(nl.item(0), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 28:   */     }
/* 29:   */     catch (XmlException e)
/* 30:   */     {
/* 31:63 */       throw new MarshalException(e);
/* 32:   */     }
/* 33:67 */     UnsignedPropertiesType unsignedProps = qualProps.getUnsignedProperties();
/* 34:68 */     if (unsignedProps == null) {
/* 35:69 */       unsignedProps = qualProps.addNewUnsignedProperties();
/* 36:   */     }
/* 37:71 */     UnsignedSignaturePropertiesType unsignedSigProps = unsignedProps.getUnsignedSignatureProperties();
/* 38:72 */     if (unsignedSigProps == null) {
/* 39:73 */       unsignedProps.addNewUnsignedSignatureProperties();
/* 40:   */     }
/* 41:76 */     Node n = document.importNode(qualProps.getDomNode().getFirstChild(), true);
/* 42:77 */     nl.item(0).getParentNode().replaceChild(n, nl.item(0));
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.Office2010SignatureFacet
 * JD-Core Version:    0.7.0.1
 */