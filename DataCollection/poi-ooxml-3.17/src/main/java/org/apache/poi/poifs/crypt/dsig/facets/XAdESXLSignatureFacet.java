/*   1:    */ package org.apache.poi.poifs.crypt.dsig.facets;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.security.Principal;
/*   6:    */ import java.security.cert.CRLException;
/*   7:    */ import java.security.cert.CertificateEncodingException;
/*   8:    */ import java.security.cert.CertificateException;
/*   9:    */ import java.security.cert.CertificateFactory;
/*  10:    */ import java.security.cert.X509CRL;
/*  11:    */ import java.security.cert.X509Certificate;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Calendar;
/*  14:    */ import java.util.Collections;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Locale;
/*  17:    */ import java.util.TimeZone;
/*  18:    */ import java.util.UUID;
/*  19:    */ import javax.xml.crypto.MarshalException;
/*  20:    */ import org.apache.poi.POIXMLTypeLoader;
/*  21:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  22:    */ import org.apache.poi.poifs.crypt.dsig.services.RevocationData;
/*  23:    */ import org.apache.poi.poifs.crypt.dsig.services.RevocationDataService;
/*  24:    */ import org.apache.poi.poifs.crypt.dsig.services.TimeStampService;
/*  25:    */ import org.apache.poi.util.POILogFactory;
/*  26:    */ import org.apache.poi.util.POILogger;
/*  27:    */ import org.apache.xml.security.c14n.Canonicalizer;
/*  28:    */ import org.apache.xmlbeans.XmlException;
/*  29:    */ import org.bouncycastle.asn1.ASN1OctetString;
/*  30:    */ import org.bouncycastle.asn1.DERTaggedObject;
/*  31:    */ import org.bouncycastle.asn1.ocsp.ResponderID;
/*  32:    */ import org.bouncycastle.asn1.x500.X500Name;
/*  33:    */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*  34:    */ import org.bouncycastle.cert.ocsp.OCSPResp;
/*  35:    */ import org.bouncycastle.cert.ocsp.RespID;
/*  36:    */ import org.etsi.uri.x01903.v13.CRLIdentifierType;
/*  37:    */ import org.etsi.uri.x01903.v13.CRLRefType;
/*  38:    */ import org.etsi.uri.x01903.v13.CRLRefsType;
/*  39:    */ import org.etsi.uri.x01903.v13.CRLValuesType;
/*  40:    */ import org.etsi.uri.x01903.v13.CertIDListType;
/*  41:    */ import org.etsi.uri.x01903.v13.CertIDType;
/*  42:    */ import org.etsi.uri.x01903.v13.CertificateValuesType;
/*  43:    */ import org.etsi.uri.x01903.v13.CompleteCertificateRefsType;
/*  44:    */ import org.etsi.uri.x01903.v13.CompleteRevocationRefsType;
/*  45:    */ import org.etsi.uri.x01903.v13.DigestAlgAndValueType;
/*  46:    */ import org.etsi.uri.x01903.v13.EncapsulatedPKIDataType;
/*  47:    */ import org.etsi.uri.x01903.v13.OCSPIdentifierType;
/*  48:    */ import org.etsi.uri.x01903.v13.OCSPRefType;
/*  49:    */ import org.etsi.uri.x01903.v13.OCSPRefsType;
/*  50:    */ import org.etsi.uri.x01903.v13.OCSPValuesType;
/*  51:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesDocument;
/*  52:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesDocument.Factory;
/*  53:    */ import org.etsi.uri.x01903.v13.QualifyingPropertiesType;
/*  54:    */ import org.etsi.uri.x01903.v13.ResponderIDType;
/*  55:    */ import org.etsi.uri.x01903.v13.RevocationValuesType;
/*  56:    */ import org.etsi.uri.x01903.v13.UnsignedPropertiesType;
/*  57:    */ import org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType;
/*  58:    */ import org.etsi.uri.x01903.v13.XAdESTimeStampType;
/*  59:    */ import org.etsi.uri.x01903.v13.XAdESTimeStampType.Factory;
/*  60:    */ import org.etsi.uri.x01903.v14.ValidationDataType;
/*  61:    */ import org.etsi.uri.x01903.v14.ValidationDataType.Factory;
/*  62:    */ import org.w3.x2000.x09.xmldsig.CanonicalizationMethodType;
/*  63:    */ import org.w3c.dom.Document;
/*  64:    */ import org.w3c.dom.Node;
/*  65:    */ import org.w3c.dom.NodeList;
/*  66:    */ 
/*  67:    */ public class XAdESXLSignatureFacet
/*  68:    */   extends SignatureFacet
/*  69:    */ {
/*  70: 89 */   private static final POILogger LOG = POILogFactory.getLogger(XAdESXLSignatureFacet.class);
/*  71:    */   private final CertificateFactory certificateFactory;
/*  72:    */   
/*  73:    */   public XAdESXLSignatureFacet()
/*  74:    */   {
/*  75:    */     try
/*  76:    */     {
/*  77: 95 */       this.certificateFactory = CertificateFactory.getInstance("X.509");
/*  78:    */     }
/*  79:    */     catch (CertificateException e)
/*  80:    */     {
/*  81: 97 */       throw new RuntimeException("X509 JCA error: " + e.getMessage(), e);
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void postSign(Document document)
/*  86:    */     throws MarshalException
/*  87:    */   {
/*  88:103 */     LOG.log(1, new Object[] { "XAdES-X-L post sign phase" });
/*  89:    */     
/*  90:105 */     QualifyingPropertiesDocument qualDoc = null;
/*  91:106 */     QualifyingPropertiesType qualProps = null;
/*  92:    */     
/*  93:    */ 
/*  94:109 */     NodeList qualNl = document.getElementsByTagNameNS("http://uri.etsi.org/01903/v1.3.2#", "QualifyingProperties");
/*  95:110 */     if (qualNl.getLength() == 1)
/*  96:    */     {
/*  97:    */       try
/*  98:    */       {
/*  99:112 */         qualDoc = QualifyingPropertiesDocument.Factory.parse(qualNl.item(0), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 100:    */       }
/* 101:    */       catch (XmlException e)
/* 102:    */       {
/* 103:114 */         throw new MarshalException(e);
/* 104:    */       }
/* 105:116 */       qualProps = qualDoc.getQualifyingProperties();
/* 106:    */     }
/* 107:    */     else
/* 108:    */     {
/* 109:118 */       throw new MarshalException("no XAdES-BES extension present");
/* 110:    */     }
/* 111:122 */     UnsignedPropertiesType unsignedProps = qualProps.getUnsignedProperties();
/* 112:123 */     if (unsignedProps == null) {
/* 113:124 */       unsignedProps = qualProps.addNewUnsignedProperties();
/* 114:    */     }
/* 115:126 */     UnsignedSignaturePropertiesType unsignedSigProps = unsignedProps.getUnsignedSignatureProperties();
/* 116:127 */     if (unsignedSigProps == null) {
/* 117:128 */       unsignedSigProps = unsignedProps.addNewUnsignedSignatureProperties();
/* 118:    */     }
/* 119:133 */     NodeList nlSigVal = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
/* 120:134 */     if (nlSigVal.getLength() != 1) {
/* 121:135 */       throw new IllegalArgumentException("SignatureValue is not set.");
/* 122:    */     }
/* 123:138 */     RevocationData tsaRevocationDataXadesT = new RevocationData();
/* 124:139 */     LOG.log(1, new Object[] { "creating XAdES-T time-stamp" });
/* 125:140 */     XAdESTimeStampType signatureTimeStamp = createXAdESTimeStamp(Collections.singletonList(nlSigVal.item(0)), tsaRevocationDataXadesT);
/* 126:    */     
/* 127:    */ 
/* 128:    */ 
/* 129:144 */     unsignedSigProps.addNewSignatureTimeStamp().set(signatureTimeStamp);
/* 130:147 */     if (tsaRevocationDataXadesT.hasRevocationDataEntries())
/* 131:    */     {
/* 132:148 */       ValidationDataType validationData = createValidationData(tsaRevocationDataXadesT);
/* 133:149 */       XAdESSignatureFacet.insertXChild(unsignedSigProps, validationData);
/* 134:    */     }
/* 135:152 */     if (this.signatureConfig.getRevocationDataService() == null) {
/* 136:157 */       return;
/* 137:    */     }
/* 138:161 */     CompleteCertificateRefsType completeCertificateRefs = unsignedSigProps.addNewCompleteCertificateRefs();
/* 139:    */     
/* 140:    */ 
/* 141:164 */     CertIDListType certIdList = completeCertificateRefs.addNewCertRefs();
/* 142:    */     
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:169 */     List<X509Certificate> certChain = this.signatureConfig.getSigningCertificateChain();
/* 147:170 */     int chainSize = certChain.size();
/* 148:171 */     if (chainSize > 1) {
/* 149:172 */       for (X509Certificate cert : certChain.subList(1, chainSize))
/* 150:    */       {
/* 151:173 */         CertIDType certId = certIdList.addNewCert();
/* 152:174 */         XAdESSignatureFacet.setCertID(certId, this.signatureConfig, false, cert);
/* 153:    */       }
/* 154:    */     }
/* 155:179 */     CompleteRevocationRefsType completeRevocationRefs = unsignedSigProps.addNewCompleteRevocationRefs();
/* 156:    */     
/* 157:181 */     RevocationData revocationData = this.signatureConfig.getRevocationDataService().getRevocationData(certChain);
/* 158:    */     CRLRefsType crlRefs;
/* 159:183 */     if (revocationData.hasCRLs())
/* 160:    */     {
/* 161:184 */       crlRefs = completeRevocationRefs.addNewCRLRefs();
/* 162:185 */       completeRevocationRefs.setCRLRefs(crlRefs);
/* 163:187 */       for (byte[] encodedCrl : revocationData.getCRLs())
/* 164:    */       {
/* 165:188 */         CRLRefType crlRef = crlRefs.addNewCRLRef();
/* 166:    */         X509CRL crl;
/* 167:    */         try
/* 168:    */         {
/* 169:191 */           crl = (X509CRL)this.certificateFactory.generateCRL(new ByteArrayInputStream(encodedCrl));
/* 170:    */         }
/* 171:    */         catch (CRLException e)
/* 172:    */         {
/* 173:194 */           throw new RuntimeException("CRL parse error: " + e.getMessage(), e);
/* 174:    */         }
/* 175:198 */         CRLIdentifierType crlIdentifier = crlRef.addNewCRLIdentifier();
/* 176:199 */         String issuerName = crl.getIssuerDN().getName().replace(",", ", ");
/* 177:200 */         crlIdentifier.setIssuer(issuerName);
/* 178:201 */         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Z"), Locale.ROOT);
/* 179:202 */         cal.setTime(crl.getThisUpdate());
/* 180:203 */         crlIdentifier.setIssueTime(cal);
/* 181:204 */         crlIdentifier.setNumber(getCrlNumber(crl));
/* 182:    */         
/* 183:206 */         DigestAlgAndValueType digestAlgAndValue = crlRef.addNewDigestAlgAndValue();
/* 184:207 */         XAdESSignatureFacet.setDigestAlgAndValue(digestAlgAndValue, encodedCrl, this.signatureConfig.getDigestAlgo());
/* 185:    */       }
/* 186:    */     }
/* 187:    */     OCSPRefsType ocspRefs;
/* 188:210 */     if (revocationData.hasOCSPs())
/* 189:    */     {
/* 190:211 */       ocspRefs = completeRevocationRefs.addNewOCSPRefs();
/* 191:212 */       for (byte[] ocsp : revocationData.getOCSPs()) {
/* 192:    */         try
/* 193:    */         {
/* 194:214 */           OCSPRefType ocspRef = ocspRefs.addNewOCSPRef();
/* 195:    */           
/* 196:216 */           DigestAlgAndValueType digestAlgAndValue = ocspRef.addNewDigestAlgAndValue();
/* 197:217 */           XAdESSignatureFacet.setDigestAlgAndValue(digestAlgAndValue, ocsp, this.signatureConfig.getDigestAlgo());
/* 198:    */           
/* 199:219 */           OCSPIdentifierType ocspIdentifier = ocspRef.addNewOCSPIdentifier();
/* 200:    */           
/* 201:221 */           OCSPResp ocspResp = new OCSPResp(ocsp);
/* 202:    */           
/* 203:223 */           BasicOCSPResp basicOcspResp = (BasicOCSPResp)ocspResp.getResponseObject();
/* 204:    */           
/* 205:225 */           Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Z"), Locale.ROOT);
/* 206:226 */           cal.setTime(basicOcspResp.getProducedAt());
/* 207:227 */           ocspIdentifier.setProducedAt(cal);
/* 208:    */           
/* 209:229 */           ResponderIDType responderId = ocspIdentifier.addNewResponderID();
/* 210:    */           
/* 211:231 */           RespID respId = basicOcspResp.getResponderId();
/* 212:232 */           ResponderID ocspResponderId = respId.toASN1Primitive();
/* 213:233 */           DERTaggedObject derTaggedObject = (DERTaggedObject)ocspResponderId.toASN1Primitive();
/* 214:234 */           if (2 == derTaggedObject.getTagNo())
/* 215:    */           {
/* 216:235 */             ASN1OctetString keyHashOctetString = (ASN1OctetString)derTaggedObject.getObject();
/* 217:236 */             byte[] key = keyHashOctetString.getOctets();
/* 218:237 */             responderId.setByKey(key);
/* 219:    */           }
/* 220:    */           else
/* 221:    */           {
/* 222:239 */             X500Name name = X500Name.getInstance(derTaggedObject.getObject());
/* 223:240 */             String nameStr = name.toString();
/* 224:241 */             responderId.setByName(nameStr);
/* 225:    */           }
/* 226:    */         }
/* 227:    */         catch (Exception e)
/* 228:    */         {
/* 229:244 */           throw new RuntimeException("OCSP decoding error: " + e.getMessage(), e);
/* 230:    */         }
/* 231:    */       }
/* 232:    */     }
/* 233:252 */     List<Node> timeStampNodesXadesX1 = new ArrayList();
/* 234:253 */     timeStampNodesXadesX1.add(nlSigVal.item(0));
/* 235:254 */     timeStampNodesXadesX1.add(signatureTimeStamp.getDomNode());
/* 236:255 */     timeStampNodesXadesX1.add(completeCertificateRefs.getDomNode());
/* 237:256 */     timeStampNodesXadesX1.add(completeRevocationRefs.getDomNode());
/* 238:    */     
/* 239:258 */     RevocationData tsaRevocationDataXadesX1 = new RevocationData();
/* 240:259 */     LOG.log(1, new Object[] { "creating XAdES-X time-stamp" });
/* 241:260 */     XAdESTimeStampType timeStampXadesX1 = createXAdESTimeStamp(timeStampNodesXadesX1, tsaRevocationDataXadesX1);
/* 242:262 */     if (tsaRevocationDataXadesX1.hasRevocationDataEntries())
/* 243:    */     {
/* 244:263 */       ValidationDataType timeStampXadesX1ValidationData = createValidationData(tsaRevocationDataXadesX1);
/* 245:264 */       XAdESSignatureFacet.insertXChild(unsignedSigProps, timeStampXadesX1ValidationData);
/* 246:    */     }
/* 247:268 */     unsignedSigProps.addNewSigAndRefsTimeStamp().set(timeStampXadesX1);
/* 248:    */     
/* 249:    */ 
/* 250:271 */     CertificateValuesType certificateValues = unsignedSigProps.addNewCertificateValues();
/* 251:272 */     for (X509Certificate certificate : certChain)
/* 252:    */     {
/* 253:273 */       EncapsulatedPKIDataType encapsulatedPKIDataType = certificateValues.addNewEncapsulatedX509Certificate();
/* 254:    */       try
/* 255:    */       {
/* 256:275 */         encapsulatedPKIDataType.setByteArrayValue(certificate.getEncoded());
/* 257:    */       }
/* 258:    */       catch (CertificateEncodingException e)
/* 259:    */       {
/* 260:277 */         throw new RuntimeException("certificate encoding error: " + e.getMessage(), e);
/* 261:    */       }
/* 262:    */     }
/* 263:281 */     RevocationValuesType revocationValues = unsignedSigProps.addNewRevocationValues();
/* 264:282 */     createRevocationValues(revocationValues, revocationData);
/* 265:    */     
/* 266:    */ 
/* 267:285 */     Node n = document.importNode(qualProps.getDomNode(), true);
/* 268:286 */     qualNl.item(0).getParentNode().replaceChild(n, qualNl.item(0));
/* 269:    */   }
/* 270:    */   
/* 271:    */   public static byte[] getC14nValue(List<Node> nodeList, String c14nAlgoId)
/* 272:    */   {
/* 273:290 */     ByteArrayOutputStream c14nValue = new ByteArrayOutputStream();
/* 274:    */     try
/* 275:    */     {
/* 276:292 */       for (Node node : nodeList)
/* 277:    */       {
/* 278:297 */         Canonicalizer c14n = Canonicalizer.getInstance(c14nAlgoId);
/* 279:298 */         c14nValue.write(c14n.canonicalizeSubtree(node));
/* 280:    */       }
/* 281:    */     }
/* 282:    */     catch (RuntimeException e)
/* 283:    */     {
/* 284:301 */       throw e;
/* 285:    */     }
/* 286:    */     catch (Exception e)
/* 287:    */     {
/* 288:303 */       throw new RuntimeException("c14n error: " + e.getMessage(), e);
/* 289:    */     }
/* 290:305 */     return c14nValue.toByteArray();
/* 291:    */   }
/* 292:    */   
/* 293:    */   /* Error */
/* 294:    */   private java.math.BigInteger getCrlNumber(X509CRL crl)
/* 295:    */   {
/* 296:    */     // Byte code:
/* 297:    */     //   0: aload_1
/* 298:    */     //   1: getstatic 154	org/bouncycastle/asn1/x509/Extension:cRLNumber	Lorg/bouncycastle/asn1/ASN1ObjectIdentifier;
/* 299:    */     //   4: invokevirtual 155	org/bouncycastle/asn1/ASN1ObjectIdentifier:getId	()Ljava/lang/String;
/* 300:    */     //   7: invokevirtual 156	java/security/cert/X509CRL:getExtensionValue	(Ljava/lang/String;)[B
/* 301:    */     //   10: astore_2
/* 302:    */     //   11: aconst_null
/* 303:    */     //   12: aload_2
/* 304:    */     //   13: if_acmpne +5 -> 18
/* 305:    */     //   16: aconst_null
/* 306:    */     //   17: areturn
/* 307:    */     //   18: aconst_null
/* 308:    */     //   19: astore_3
/* 309:    */     //   20: aconst_null
/* 310:    */     //   21: astore 4
/* 311:    */     //   23: new 157	org/bouncycastle/asn1/ASN1InputStream
/* 312:    */     //   26: dup
/* 313:    */     //   27: aload_2
/* 314:    */     //   28: invokespecial 158	org/bouncycastle/asn1/ASN1InputStream:<init>	([B)V
/* 315:    */     //   31: astore_3
/* 316:    */     //   32: aload_3
/* 317:    */     //   33: invokevirtual 159	org/bouncycastle/asn1/ASN1InputStream:readObject	()Lorg/bouncycastle/asn1/ASN1Primitive;
/* 318:    */     //   36: checkcast 116	org/bouncycastle/asn1/ASN1OctetString
/* 319:    */     //   39: astore 5
/* 320:    */     //   41: aload 5
/* 321:    */     //   43: invokevirtual 117	org/bouncycastle/asn1/ASN1OctetString:getOctets	()[B
/* 322:    */     //   46: astore 6
/* 323:    */     //   48: new 157	org/bouncycastle/asn1/ASN1InputStream
/* 324:    */     //   51: dup
/* 325:    */     //   52: aload 6
/* 326:    */     //   54: invokespecial 158	org/bouncycastle/asn1/ASN1InputStream:<init>	([B)V
/* 327:    */     //   57: astore 4
/* 328:    */     //   59: aload 4
/* 329:    */     //   61: invokevirtual 159	org/bouncycastle/asn1/ASN1InputStream:readObject	()Lorg/bouncycastle/asn1/ASN1Primitive;
/* 330:    */     //   64: checkcast 160	org/bouncycastle/asn1/ASN1Integer
/* 331:    */     //   67: astore 7
/* 332:    */     //   69: aload 7
/* 333:    */     //   71: invokevirtual 161	org/bouncycastle/asn1/ASN1Integer:getPositiveValue	()Ljava/math/BigInteger;
/* 334:    */     //   74: astore 8
/* 335:    */     //   76: aload 4
/* 336:    */     //   78: invokestatic 162	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 337:    */     //   81: aload_3
/* 338:    */     //   82: invokestatic 162	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 339:    */     //   85: aload 8
/* 340:    */     //   87: areturn
/* 341:    */     //   88: astore 9
/* 342:    */     //   90: aload 4
/* 343:    */     //   92: invokestatic 162	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 344:    */     //   95: aload_3
/* 345:    */     //   96: invokestatic 162	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 346:    */     //   99: aload 9
/* 347:    */     //   101: athrow
/* 348:    */     //   102: astore_3
/* 349:    */     //   103: new 6	java/lang/RuntimeException
/* 350:    */     //   106: dup
/* 351:    */     //   107: new 7	java/lang/StringBuilder
/* 352:    */     //   110: dup
/* 353:    */     //   111: invokespecial 8	java/lang/StringBuilder:<init>	()V
/* 354:    */     //   114: ldc 164
/* 355:    */     //   116: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 356:    */     //   119: aload_3
/* 357:    */     //   120: invokevirtual 165	java/io/IOException:getMessage	()Ljava/lang/String;
/* 358:    */     //   123: invokevirtual 10	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 359:    */     //   126: invokevirtual 12	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 360:    */     //   129: aload_3
/* 361:    */     //   130: invokespecial 13	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/* 362:    */     //   133: athrow
/* 363:    */     // Line number table:
/* 364:    */     //   Java source line #309	-> byte code offset #0
/* 365:    */     //   Java source line #310	-> byte code offset #11
/* 366:    */     //   Java source line #311	-> byte code offset #16
/* 367:    */     //   Java source line #315	-> byte code offset #18
/* 368:    */     //   Java source line #317	-> byte code offset #23
/* 369:    */     //   Java source line #318	-> byte code offset #32
/* 370:    */     //   Java source line #319	-> byte code offset #41
/* 371:    */     //   Java source line #320	-> byte code offset #48
/* 372:    */     //   Java source line #321	-> byte code offset #59
/* 373:    */     //   Java source line #322	-> byte code offset #69
/* 374:    */     //   Java source line #324	-> byte code offset #76
/* 375:    */     //   Java source line #325	-> byte code offset #81
/* 376:    */     //   Java source line #324	-> byte code offset #88
/* 377:    */     //   Java source line #325	-> byte code offset #95
/* 378:    */     //   Java source line #327	-> byte code offset #102
/* 379:    */     //   Java source line #328	-> byte code offset #103
/* 380:    */     // Local variable table:
/* 381:    */     //   start	length	slot	name	signature
/* 382:    */     //   0	134	0	this	XAdESXLSignatureFacet
/* 383:    */     //   0	134	1	crl	X509CRL
/* 384:    */     //   10	18	2	crlNumberExtensionValue	byte[]
/* 385:    */     //   19	77	3	asn1IS1	org.bouncycastle.asn1.ASN1InputStream
/* 386:    */     //   102	28	3	e	java.io.IOException
/* 387:    */     //   21	70	4	asn1IS2	org.bouncycastle.asn1.ASN1InputStream
/* 388:    */     //   39	3	5	octetString	ASN1OctetString
/* 389:    */     //   46	7	6	octets	byte[]
/* 390:    */     //   67	3	7	integer	org.bouncycastle.asn1.ASN1Integer
/* 391:    */     //   88	12	9	localObject	Object
/* 392:    */     // Exception table:
/* 393:    */     //   from	to	target	type
/* 394:    */     //   23	76	88	finally
/* 395:    */     //   88	90	88	finally
/* 396:    */     //   18	85	102	java/io/IOException
/* 397:    */     //   88	102	102	java/io/IOException
/* 398:    */   }
/* 399:    */   
/* 400:    */   private XAdESTimeStampType createXAdESTimeStamp(List<Node> nodeList, RevocationData revocationData)
/* 401:    */   {
/* 402:335 */     byte[] c14nSignatureValueElement = getC14nValue(nodeList, this.signatureConfig.getXadesCanonicalizationMethod());
/* 403:    */     
/* 404:337 */     return createXAdESTimeStamp(c14nSignatureValueElement, revocationData);
/* 405:    */   }
/* 406:    */   
/* 407:    */   private XAdESTimeStampType createXAdESTimeStamp(byte[] data, RevocationData revocationData)
/* 408:    */   {
/* 409:    */     byte[] timeStampToken;
/* 410:    */     try
/* 411:    */     {
/* 412:344 */       timeStampToken = this.signatureConfig.getTspService().timeStamp(data, revocationData);
/* 413:    */     }
/* 414:    */     catch (Exception e)
/* 415:    */     {
/* 416:346 */       throw new RuntimeException("error while creating a time-stamp: " + e.getMessage(), e);
/* 417:    */     }
/* 418:351 */     XAdESTimeStampType xadesTimeStamp = XAdESTimeStampType.Factory.newInstance();
/* 419:352 */     xadesTimeStamp.setId("time-stamp-" + UUID.randomUUID());
/* 420:353 */     CanonicalizationMethodType c14nMethod = xadesTimeStamp.addNewCanonicalizationMethod();
/* 421:354 */     c14nMethod.setAlgorithm(this.signatureConfig.getXadesCanonicalizationMethod());
/* 422:    */     
/* 423:    */ 
/* 424:357 */     EncapsulatedPKIDataType encapsulatedTimeStamp = xadesTimeStamp.addNewEncapsulatedTimeStamp();
/* 425:358 */     encapsulatedTimeStamp.setByteArrayValue(timeStampToken);
/* 426:359 */     encapsulatedTimeStamp.setId("time-stamp-token-" + UUID.randomUUID());
/* 427:    */     
/* 428:361 */     return xadesTimeStamp;
/* 429:    */   }
/* 430:    */   
/* 431:    */   private ValidationDataType createValidationData(RevocationData revocationData)
/* 432:    */   {
/* 433:366 */     ValidationDataType validationData = ValidationDataType.Factory.newInstance();
/* 434:367 */     RevocationValuesType revocationValues = validationData.addNewRevocationValues();
/* 435:368 */     createRevocationValues(revocationValues, revocationData);
/* 436:369 */     return validationData;
/* 437:    */   }
/* 438:    */   
/* 439:    */   private void createRevocationValues(RevocationValuesType revocationValues, RevocationData revocationData)
/* 440:    */   {
/* 441:    */     CRLValuesType crlValues;
/* 442:374 */     if (revocationData.hasCRLs())
/* 443:    */     {
/* 444:375 */       crlValues = revocationValues.addNewCRLValues();
/* 445:376 */       for (byte[] crl : revocationData.getCRLs())
/* 446:    */       {
/* 447:377 */         EncapsulatedPKIDataType encapsulatedCrlValue = crlValues.addNewEncapsulatedCRLValue();
/* 448:378 */         encapsulatedCrlValue.setByteArrayValue(crl);
/* 449:    */       }
/* 450:    */     }
/* 451:    */     OCSPValuesType ocspValues;
/* 452:381 */     if (revocationData.hasOCSPs())
/* 453:    */     {
/* 454:382 */       ocspValues = revocationValues.addNewOCSPValues();
/* 455:383 */       for (byte[] ocsp : revocationData.getOCSPs())
/* 456:    */       {
/* 457:384 */         EncapsulatedPKIDataType encapsulatedOcspValue = ocspValues.addNewEncapsulatedOCSPValue();
/* 458:385 */         encapsulatedOcspValue.setByteArrayValue(ocsp);
/* 459:    */       }
/* 460:    */     }
/* 461:    */   }
/* 462:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.XAdESXLSignatureFacet
 * JD-Core Version:    0.7.0.1
 */