/*   1:    */ package org.apache.poi.poifs.crypt.dsig.facets;
/*   2:    */ 
/*   3:    */ import java.security.MessageDigest;
/*   4:    */ import java.security.Principal;
/*   5:    */ import java.security.cert.CertificateEncodingException;
/*   6:    */ import java.security.cert.X509Certificate;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Calendar;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Locale;
/*  12:    */ import java.util.Map;
/*  13:    */ import java.util.Map.Entry;
/*  14:    */ import java.util.TimeZone;
/*  15:    */ import javax.security.auth.x500.X500Principal;
/*  16:    */ import javax.xml.crypto.XMLStructure;
/*  17:    */ import javax.xml.crypto.dom.DOMStructure;
/*  18:    */ import javax.xml.crypto.dsig.Reference;
/*  19:    */ import javax.xml.crypto.dsig.Transform;
/*  20:    */ import javax.xml.crypto.dsig.XMLObject;
/*  21:    */ import javax.xml.crypto.dsig.XMLSignatureException;
/*  22:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  23:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  24:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  25:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  26:    */ import org.apache.poi.poifs.crypt.dsig.services.SignaturePolicyService;
/*  27:    */ import org.apache.poi.util.POILogFactory;
/*  28:    */ import org.apache.poi.util.POILogger;
/*  29:    */ import org.apache.xmlbeans.XmlCursor;
/*  30:    */ import org.apache.xmlbeans.XmlObject;
/*  31:    */ import org.apache.xmlbeans.XmlString;
/*  32:    */ import org.apache.xmlbeans.XmlString.Factory;
/*  33:    */ import org.etsi.uri.x01903.v13.AnyType;
/*  34:    */ import org.etsi.uri.x01903.v13.CertIDListType;
/*  35:    */ import org.etsi.uri.x01903.v13.CertIDType;
/*  36:    */ import org.etsi.uri.x01903.v13.ClaimedRolesListType;
/*  37:    */ import org.etsi.uri.x01903.v13.DataObjectFormatType;
/*  38:    */ import org.etsi.uri.x01903.v13.DataObjectFormatType.Factory;
/*  39:    */ import org.etsi.uri.x01903.v13.DigestAlgAndValueType;
/*  40:    */ import org.etsi.uri.x01903.v13.IdentifierType;
/*  41:    */ import org.etsi.uri.x01903.v13.ObjectIdentifierType;
/*  42:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesDocument;
/*  43:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesDocument.Factory;
/*  44:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesType;
/*  45:    */ import org.etsi.uri.x01903.v13.SigPolicyQualifiersListType;
/*  46:    */ import org.etsi.uri.x01903.v13.SignaturePolicyIdType;
/*  47:    */ import org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType;
/*  48:    */ import org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType;
/*  49:    */ import org.etsi.uri.x01903.v13.SignedPropertiesType;
/*  50:    */ import org.etsi.uri.x01903.v13.SignedSignaturePropertiesType;
/*  51:    */ import org.etsi.uri.x01903.v13.SignerRoleType;
/*  52:    */ import org.w3.x2000.x09.xmldsig.DigestMethodType;
/*  53:    */ import org.w3.x2000.x09.xmldsig.X509IssuerSerialType;
/*  54:    */ import org.w3c.dom.Document;
/*  55:    */ import org.w3c.dom.Element;
/*  56:    */ 
/*  57:    */ public class XAdESSignatureFacet
/*  58:    */   extends SignatureFacet
/*  59:    */ {
/*  60: 91 */   private static final POILogger LOG = POILogFactory.getLogger(XAdESSignatureFacet.class);
/*  61:    */   private static final String XADES_TYPE = "http://uri.etsi.org/01903#SignedProperties";
/*  62: 95 */   private Map<String, String> dataObjectFormatMimeTypes = new HashMap();
/*  63:    */   
/*  64:    */   public void preSign(Document document, List<Reference> references, List<XMLObject> objects)
/*  65:    */     throws XMLSignatureException
/*  66:    */   {
/*  67:104 */     LOG.log(1, new Object[] { "preSign" });
/*  68:    */     
/*  69:    */ 
/*  70:107 */     QualifyingPropertiesDocument qualDoc = QualifyingPropertiesDocument.Factory.newInstance();
/*  71:108 */     QualifyingPropertiesType qualifyingProperties = qualDoc.addNewQualifyingProperties();
/*  72:109 */     qualifyingProperties.setTarget("#" + this.signatureConfig.getPackageSignatureId());
/*  73:    */     
/*  74:    */ 
/*  75:112 */     SignedPropertiesType signedProperties = qualifyingProperties.addNewSignedProperties();
/*  76:113 */     signedProperties.setId(this.signatureConfig.getXadesSignatureId());
/*  77:    */     
/*  78:    */ 
/*  79:116 */     SignedSignaturePropertiesType signedSignatureProperties = signedProperties.addNewSignedSignatureProperties();
/*  80:    */     
/*  81:    */ 
/*  82:119 */     Calendar xmlGregorianCalendar = Calendar.getInstance(TimeZone.getTimeZone("Z"), Locale.ROOT);
/*  83:120 */     xmlGregorianCalendar.setTime(this.signatureConfig.getExecutionTime());
/*  84:121 */     xmlGregorianCalendar.clear(14);
/*  85:122 */     signedSignatureProperties.setSigningTime(xmlGregorianCalendar);
/*  86:125 */     if ((this.signatureConfig.getSigningCertificateChain() == null) || (this.signatureConfig.getSigningCertificateChain().isEmpty())) {
/*  87:127 */       throw new RuntimeException("no signing certificate chain available");
/*  88:    */     }
/*  89:129 */     CertIDListType signingCertificates = signedSignatureProperties.addNewSigningCertificate();
/*  90:130 */     CertIDType certId = signingCertificates.addNewCert();
/*  91:131 */     X509Certificate certificate = (X509Certificate)this.signatureConfig.getSigningCertificateChain().get(0);
/*  92:132 */     setCertID(certId, this.signatureConfig, this.signatureConfig.isXadesIssuerNameNoReverseOrder(), certificate);
/*  93:    */     
/*  94:    */ 
/*  95:135 */     String role = this.signatureConfig.getXadesRole();
/*  96:136 */     if ((role != null) && (!role.isEmpty()))
/*  97:    */     {
/*  98:137 */       SignerRoleType signerRole = signedSignatureProperties.addNewSignerRole();
/*  99:138 */       signedSignatureProperties.setSignerRole(signerRole);
/* 100:139 */       ClaimedRolesListType claimedRolesList = signerRole.addNewClaimedRoles();
/* 101:140 */       AnyType claimedRole = claimedRolesList.addNewClaimedRole();
/* 102:141 */       XmlString roleString = XmlString.Factory.newInstance();
/* 103:142 */       roleString.setStringValue(role);
/* 104:143 */       insertXChild(claimedRole, roleString);
/* 105:    */     }
/* 106:147 */     SignaturePolicyService policyService = this.signatureConfig.getSignaturePolicyService();
/* 107:148 */     if (policyService != null)
/* 108:    */     {
/* 109:149 */       SignaturePolicyIdentifierType signaturePolicyIdentifier = signedSignatureProperties.addNewSignaturePolicyIdentifier();
/* 110:    */       
/* 111:    */ 
/* 112:152 */       SignaturePolicyIdType signaturePolicyId = signaturePolicyIdentifier.addNewSignaturePolicyId();
/* 113:    */       
/* 114:154 */       ObjectIdentifierType objectIdentifier = signaturePolicyId.addNewSigPolicyId();
/* 115:155 */       objectIdentifier.setDescription(policyService.getSignaturePolicyDescription());
/* 116:    */       
/* 117:157 */       IdentifierType identifier = objectIdentifier.addNewIdentifier();
/* 118:158 */       identifier.setStringValue(policyService.getSignaturePolicyIdentifier());
/* 119:    */       
/* 120:160 */       byte[] signaturePolicyDocumentData = policyService.getSignaturePolicyDocument();
/* 121:161 */       DigestAlgAndValueType sigPolicyHash = signaturePolicyId.addNewSigPolicyHash();
/* 122:162 */       setDigestAlgAndValue(sigPolicyHash, signaturePolicyDocumentData, this.signatureConfig.getDigestAlgo());
/* 123:    */       
/* 124:164 */       String signaturePolicyDownloadUrl = policyService.getSignaturePolicyDownloadUrl();
/* 125:165 */       if (null != signaturePolicyDownloadUrl)
/* 126:    */       {
/* 127:166 */         SigPolicyQualifiersListType sigPolicyQualifiers = signaturePolicyId.addNewSigPolicyQualifiers();
/* 128:167 */         AnyType sigPolicyQualifier = sigPolicyQualifiers.addNewSigPolicyQualifier();
/* 129:168 */         XmlString spUriElement = XmlString.Factory.newInstance();
/* 130:169 */         spUriElement.setStringValue(signaturePolicyDownloadUrl);
/* 131:170 */         insertXChild(sigPolicyQualifier, spUriElement);
/* 132:    */       }
/* 133:    */     }
/* 134:172 */     else if (this.signatureConfig.isXadesSignaturePolicyImplied())
/* 135:    */     {
/* 136:173 */       SignaturePolicyIdentifierType signaturePolicyIdentifier = signedSignatureProperties.addNewSignaturePolicyIdentifier();
/* 137:    */       
/* 138:175 */       signaturePolicyIdentifier.addNewSignaturePolicyImplied();
/* 139:    */     }
/* 140:    */     List<DataObjectFormatType> dataObjectFormats;
/* 141:179 */     if (!this.dataObjectFormatMimeTypes.isEmpty())
/* 142:    */     {
/* 143:180 */       SignedDataObjectPropertiesType signedDataObjectProperties = signedProperties.addNewSignedDataObjectProperties();
/* 144:    */       
/* 145:    */ 
/* 146:183 */       dataObjectFormats = signedDataObjectProperties.getDataObjectFormatList();
/* 147:185 */       for (Entry<String, String> dataObjectFormatMimeType : this.dataObjectFormatMimeTypes.entrySet())
/* 148:    */       {
/* 149:187 */         DataObjectFormatType dataObjectFormat = DataObjectFormatType.Factory.newInstance();
/* 150:188 */         dataObjectFormat.setObjectReference("#" + (String)dataObjectFormatMimeType.getKey());
/* 151:189 */         dataObjectFormat.setMimeType((String)dataObjectFormatMimeType.getValue());
/* 152:190 */         dataObjectFormats.add(dataObjectFormat);
/* 153:    */       }
/* 154:    */     }
/* 155:195 */     List<XMLStructure> xadesObjectContent = new ArrayList();
/* 156:196 */     Element qualDocElSrc = (Element)qualifyingProperties.getDomNode();
/* 157:197 */     Element qualDocEl = (Element)document.importNode(qualDocElSrc, true);
/* 158:198 */     xadesObjectContent.add(new DOMStructure(qualDocEl));
/* 159:199 */     XMLObject xadesObject = getSignatureFactory().newXMLObject(xadesObjectContent, null, null, null);
/* 160:200 */     objects.add(xadesObject);
/* 161:    */     
/* 162:    */ 
/* 163:203 */     List<Transform> transforms = new ArrayList();
/* 164:204 */     Transform exclusiveTransform = newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
/* 165:205 */     transforms.add(exclusiveTransform);
/* 166:206 */     Reference reference = newReference("#" + this.signatureConfig.getXadesSignatureId(), transforms, "http://uri.etsi.org/01903#SignedProperties", null, null);
/* 167:    */     
/* 168:208 */     references.add(reference);
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected static void setDigestAlgAndValue(DigestAlgAndValueType digestAlgAndValue, byte[] data, HashAlgorithm digestAlgo)
/* 172:    */   {
/* 173:222 */     DigestMethodType digestMethod = digestAlgAndValue.addNewDigestMethod();
/* 174:223 */     digestMethod.setAlgorithm(SignatureConfig.getDigestMethodUri(digestAlgo));
/* 175:    */     
/* 176:225 */     MessageDigest messageDigest = CryptoFunctions.getMessageDigest(digestAlgo);
/* 177:226 */     byte[] digestValue = messageDigest.digest(data);
/* 178:227 */     digestAlgAndValue.setDigestValue(digestValue);
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected static void setCertID(CertIDType certId, SignatureConfig signatureConfig, boolean issuerNameNoReverseOrder, X509Certificate certificate)
/* 182:    */   {
/* 183:235 */     X509IssuerSerialType issuerSerial = certId.addNewIssuerSerial();
/* 184:    */     String issuerName;
/* 185:    */     String issuerName;
/* 186:237 */     if (issuerNameNoReverseOrder) {
/* 187:247 */       issuerName = certificate.getIssuerDN().getName().replace(",", ", ");
/* 188:    */     } else {
/* 189:249 */       issuerName = certificate.getIssuerX500Principal().toString();
/* 190:    */     }
/* 191:251 */     issuerSerial.setX509IssuerName(issuerName);
/* 192:252 */     issuerSerial.setX509SerialNumber(certificate.getSerialNumber());
/* 193:    */     byte[] encodedCertificate;
/* 194:    */     try
/* 195:    */     {
/* 196:256 */       encodedCertificate = certificate.getEncoded();
/* 197:    */     }
/* 198:    */     catch (CertificateEncodingException e)
/* 199:    */     {
/* 200:258 */       throw new RuntimeException("certificate encoding error: " + e.getMessage(), e);
/* 201:    */     }
/* 202:261 */     DigestAlgAndValueType certDigest = certId.addNewCertDigest();
/* 203:262 */     setDigestAlgAndValue(certDigest, encodedCertificate, signatureConfig.getXadesDigestAlgo());
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void addMimeType(String dsReferenceUri, String mimetype)
/* 207:    */   {
/* 208:273 */     this.dataObjectFormatMimeTypes.put(dsReferenceUri, mimetype);
/* 209:    */   }
/* 210:    */   
/* 211:    */   protected static void insertXChild(XmlObject root, XmlObject child)
/* 212:    */   {
/* 213:277 */     XmlCursor rootCursor = root.newCursor();
/* 214:278 */     rootCursor.toEndToken();
/* 215:279 */     XmlCursor childCursor = child.newCursor();
/* 216:280 */     childCursor.toNextToken();
/* 217:281 */     childCursor.moveXml(rootCursor);
/* 218:282 */     childCursor.dispose();
/* 219:283 */     rootCursor.dispose();
/* 220:    */   }
/* 221:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.XAdESSignatureFacet

 * JD-Core Version:    0.7.0.1

 */