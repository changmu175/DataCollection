/*   1:    */ package org.apache.poi.poifs.crypt.dsig.facets;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Field;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.security.AccessController;
/*   6:    */ import java.security.GeneralSecurityException;
/*   7:    */ import java.security.MessageDigest;
/*   8:    */ import java.security.PrivilegedAction;
/*   9:    */ import java.security.Provider;
/*  10:    */ import java.security.Security;
/*  11:    */ import java.util.List;
/*  12:    */ import javax.xml.crypto.MarshalException;
/*  13:    */ import javax.xml.crypto.dsig.DigestMethod;
/*  14:    */ import javax.xml.crypto.dsig.Reference;
/*  15:    */ import javax.xml.crypto.dsig.Transform;
/*  16:    */ import javax.xml.crypto.dsig.XMLObject;
/*  17:    */ import javax.xml.crypto.dsig.XMLSignatureException;
/*  18:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  19:    */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*  20:    */ import org.apache.jcp.xml.dsig.internal.dom.DOMDigestMethod;
/*  21:    */ import org.apache.jcp.xml.dsig.internal.dom.DOMReference;
/*  22:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  23:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig.SignatureConfigurable;
/*  24:    */ import org.apache.poi.util.POILogFactory;
/*  25:    */ import org.apache.poi.util.POILogger;
/*  26:    */ import org.apache.poi.util.SuppressForbidden;
/*  27:    */ import org.w3c.dom.Document;
/*  28:    */ 
/*  29:    */ public abstract class SignatureFacet
/*  30:    */   implements SignatureConfig.SignatureConfigurable
/*  31:    */ {
/*  32: 63 */   private static final POILogger LOG = POILogFactory.getLogger(SignatureFacet.class);
/*  33:    */   public static final String XML_NS = "http://www.w3.org/2000/xmlns/";
/*  34:    */   public static final String XML_DIGSIG_NS = "http://www.w3.org/2000/09/xmldsig#";
/*  35:    */   public static final String OO_DIGSIG_NS = "http://schemas.openxmlformats.org/package/2006/digital-signature";
/*  36:    */   public static final String MS_DIGSIG_NS = "http://schemas.microsoft.com/office/2006/digsig";
/*  37:    */   public static final String XADES_132_NS = "http://uri.etsi.org/01903/v1.3.2#";
/*  38:    */   public static final String XADES_141_NS = "http://uri.etsi.org/01903/v1.4.1#";
/*  39:    */   protected SignatureConfig signatureConfig;
/*  40:    */   
/*  41:    */   public void setSignatureConfig(SignatureConfig signatureConfig)
/*  42:    */   {
/*  43: 75 */     this.signatureConfig = signatureConfig;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void preSign(Document document, List<Reference> references, List<XMLObject> objects)
/*  47:    */     throws XMLSignatureException
/*  48:    */   {}
/*  49:    */   
/*  50:    */   public void postSign(Document document)
/*  51:    */     throws MarshalException
/*  52:    */   {}
/*  53:    */   
/*  54:    */   protected XMLSignatureFactory getSignatureFactory()
/*  55:    */   {
/*  56:109 */     return this.signatureConfig.getSignatureFactory();
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected Transform newTransform(String canonicalizationMethod)
/*  60:    */     throws XMLSignatureException
/*  61:    */   {
/*  62:113 */     return newTransform(canonicalizationMethod, null);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected Transform newTransform(String canonicalizationMethod, TransformParameterSpec paramSpec)
/*  66:    */     throws XMLSignatureException
/*  67:    */   {
/*  68:    */     try
/*  69:    */     {
/*  70:119 */       return getSignatureFactory().newTransform(canonicalizationMethod, paramSpec);
/*  71:    */     }
/*  72:    */     catch (GeneralSecurityException e)
/*  73:    */     {
/*  74:121 */       throw new XMLSignatureException("unknown canonicalization method: " + canonicalizationMethod, e);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected Reference newReference(String uri, List<Transform> transforms, String type, String id, byte[] digestValue)
/*  79:    */     throws XMLSignatureException
/*  80:    */   {
/*  81:127 */     return newReference(uri, transforms, type, id, digestValue, this.signatureConfig);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static Reference newReference(String uri, List<Transform> transforms, String type, String id, byte[] digestValue, SignatureConfig signatureConfig)
/*  85:    */     throws XMLSignatureException
/*  86:    */   {
/*  87:140 */     String digestMethodUri = signatureConfig.getDigestMethodUri();
/*  88:141 */     XMLSignatureFactory sigFac = signatureConfig.getSignatureFactory();
/*  89:    */     DigestMethod digestMethod;
/*  90:    */     try
/*  91:    */     {
/*  92:144 */       digestMethod = sigFac.newDigestMethod(digestMethodUri, null);
/*  93:    */     }
/*  94:    */     catch (GeneralSecurityException e)
/*  95:    */     {
/*  96:146 */       throw new XMLSignatureException("unknown digest method uri: " + digestMethodUri, e);
/*  97:    */     }
/*  98:    */     Reference reference;
/*  99:    */     Reference reference;
/* 100:150 */     if (digestValue == null) {
/* 101:151 */       reference = sigFac.newReference(uri, digestMethod, transforms, type, id);
/* 102:    */     } else {
/* 103:153 */       reference = sigFac.newReference(uri, digestMethod, transforms, type, id, digestValue);
/* 104:    */     }
/* 105:156 */     brokenJvmWorkaround(reference);
/* 106:    */     
/* 107:158 */     return reference;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public static void brokenJvmWorkaround(final Reference reference)
/* 111:    */   {
/* 112:163 */     DigestMethod digestMethod = reference.getDigestMethod();
/* 113:164 */     String digestMethodUri = digestMethod.getAlgorithm();
/* 114:    */     
/* 115:166 */     final Provider bcProv = Security.getProvider("BC");
/* 116:167 */     if ((bcProv != null) && (!"http://www.w3.org/2000/09/xmldsig#sha1".equals(digestMethodUri))) {
/* 117:170 */       AccessController.doPrivileged(new PrivilegedAction()
/* 118:    */       {
/* 119:    */         @SuppressForbidden("Workaround for a bug, needs access to private JDK members (may fail in Java 9): https://bugzilla.redhat.com/show_bug.cgi?id=1155012")
/* 120:    */         public Void run()
/* 121:    */         {
/* 122:    */           try
/* 123:    */           {
/* 124:175 */             Method m = DOMDigestMethod.class.getDeclaredMethod("getMessageDigestAlgorithm", new Class[0]);
/* 125:176 */             m.setAccessible(true);
/* 126:177 */             String mdAlgo = (String)m.invoke(this.val$digestMethod, new Object[0]);
/* 127:178 */             MessageDigest md = MessageDigest.getInstance(mdAlgo, bcProv);
/* 128:179 */             Field f = DOMReference.class.getDeclaredField("md");
/* 129:180 */             f.setAccessible(true);
/* 130:181 */             f.set(reference, md);
/* 131:    */           }
/* 132:    */           catch (Exception e)
/* 133:    */           {
/* 134:183 */             SignatureFacet.LOG.log(5, new Object[] { "Can't overwrite message digest (workaround for https://bugzilla.redhat.com/show_bug.cgi?id=1155012)", e });
/* 135:    */           }
/* 136:185 */           return null;
/* 137:    */         }
/* 138:    */       });
/* 139:    */     }
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet
 * JD-Core Version:    0.7.0.1
 */