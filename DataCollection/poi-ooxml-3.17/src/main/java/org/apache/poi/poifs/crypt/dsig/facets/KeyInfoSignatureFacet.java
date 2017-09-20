/*   1:    */ package org.apache.poi.poifs.crypt.dsig.facets;
/*   2:    */ 
/*   3:    */ import java.security.Key;
/*   4:    */ import java.security.KeyException;
/*   5:    */ import java.security.cert.X509Certificate;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import javax.security.auth.x500.X500Principal;
/*  11:    */ import javax.xml.crypto.MarshalException;
/*  12:    */ import javax.xml.crypto.XMLStructure;
/*  13:    */ import javax.xml.crypto.dom.DOMStructure;
/*  14:    */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*  15:    */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*  16:    */ import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
/*  17:    */ import javax.xml.crypto.dsig.keyinfo.KeyValue;
/*  18:    */ import javax.xml.crypto.dsig.keyinfo.X509Data;
/*  19:    */ import org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfo;
/*  20:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  21:    */ import org.apache.poi.util.POILogFactory;
/*  22:    */ import org.apache.poi.util.POILogger;
/*  23:    */ import org.w3c.dom.Document;
/*  24:    */ import org.w3c.dom.Element;
/*  25:    */ import org.w3c.dom.Node;
/*  26:    */ import org.w3c.dom.NodeList;
/*  27:    */ 
/*  28:    */ public class KeyInfoSignatureFacet
/*  29:    */   extends SignatureFacet
/*  30:    */ {
/*  31: 59 */   private static final POILogger LOG = POILogFactory.getLogger(KeyInfoSignatureFacet.class);
/*  32:    */   
/*  33:    */   public void postSign(Document document)
/*  34:    */     throws MarshalException
/*  35:    */   {
/*  36: 64 */     LOG.log(1, new Object[] { "postSign" });
/*  37:    */     
/*  38: 66 */     NodeList nl = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Object");
/*  39:    */     
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44: 72 */     Node nextSibling = nl.getLength() == 0 ? null : nl.item(0);
/*  45:    */     
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49: 77 */     KeyInfoFactory keyInfoFactory = this.signatureConfig.getKeyInfoFactory();
/*  50: 78 */     List<Object> x509DataObjects = new ArrayList();
/*  51: 79 */     X509Certificate signingCertificate = (X509Certificate)this.signatureConfig.getSigningCertificateChain().get(0);
/*  52:    */     
/*  53: 81 */     List<XMLStructure> keyInfoContent = new ArrayList();
/*  54: 83 */     if (this.signatureConfig.isIncludeKeyValue())
/*  55:    */     {
/*  56:    */       KeyValue keyValue;
/*  57:    */       try
/*  58:    */       {
/*  59: 86 */         keyValue = keyInfoFactory.newKeyValue(signingCertificate.getPublicKey());
/*  60:    */       }
/*  61:    */       catch (KeyException e)
/*  62:    */       {
/*  63: 88 */         throw new RuntimeException("key exception: " + e.getMessage(), e);
/*  64:    */       }
/*  65: 90 */       keyInfoContent.add(keyValue);
/*  66:    */     }
/*  67: 93 */     if (this.signatureConfig.isIncludeIssuerSerial()) {
/*  68: 94 */       x509DataObjects.add(keyInfoFactory.newX509IssuerSerial(signingCertificate.getIssuerX500Principal().toString(), signingCertificate.getSerialNumber()));
/*  69:    */     }
/*  70: 99 */     if (this.signatureConfig.isIncludeEntireCertificateChain()) {
/*  71:100 */       x509DataObjects.addAll(this.signatureConfig.getSigningCertificateChain());
/*  72:    */     } else {
/*  73:102 */       x509DataObjects.add(signingCertificate);
/*  74:    */     }
/*  75:105 */     if (!x509DataObjects.isEmpty())
/*  76:    */     {
/*  77:106 */       X509Data x509Data = keyInfoFactory.newX509Data(x509DataObjects);
/*  78:107 */       keyInfoContent.add(x509Data);
/*  79:    */     }
/*  80:109 */     KeyInfo keyInfo = keyInfoFactory.newKeyInfo(keyInfoContent);
/*  81:110 */     DOMKeyInfo domKeyInfo = (DOMKeyInfo)keyInfo;
/*  82:    */     
/*  83:112 */     Key key = new Key()
/*  84:    */     {
/*  85:    */       private static final long serialVersionUID = 1L;
/*  86:    */       
/*  87:    */       public String getAlgorithm()
/*  88:    */       {
/*  89:116 */         return null;
/*  90:    */       }
/*  91:    */       
/*  92:    */       public byte[] getEncoded()
/*  93:    */       {
/*  94:120 */         return null;
/*  95:    */       }
/*  96:    */       
/*  97:    */       public String getFormat()
/*  98:    */       {
/*  99:124 */         return null;
/* 100:    */       }
/* 101:127 */     };
/* 102:128 */     Element n = document.getDocumentElement();
/* 103:129 */     DOMSignContext domSignContext = nextSibling == null ? new DOMSignContext(key, n) : new DOMSignContext(key, n, nextSibling);
/* 104:132 */     for (Entry<String, String> me : this.signatureConfig.getNamespacePrefixes().entrySet()) {
/* 105:133 */       domSignContext.putNamespacePrefix((String)me.getKey(), (String)me.getValue());
/* 106:    */     }
/* 107:136 */     DOMStructure domStructure = new DOMStructure(n);
/* 108:137 */     domKeyInfo.marshal(domStructure, domSignContext);
/* 109:140 */     if (nextSibling != null)
/* 110:    */     {
/* 111:141 */       NodeList kiNl = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
/* 112:142 */       if (kiNl.getLength() != 1) {
/* 113:143 */         throw new RuntimeException("KeyInfo wasn't set");
/* 114:    */       }
/* 115:145 */       nextSibling.getParentNode().insertBefore(kiNl.item(0), nextSibling);
/* 116:    */     }
/* 117:    */   }
/* 118:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.KeyInfoSignatureFacet

 * JD-Core Version:    0.7.0.1

 */