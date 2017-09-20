/*   1:    */ package org.apache.poi.poifs.crypt.dsig.services;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.math.BigInteger;
/*   7:    */ import java.net.HttpURLConnection;
/*   8:    */ import java.net.InetAddress;
/*   9:    */ import java.net.InetSocketAddress;
/*  10:    */ import java.net.Proxy;
/*  11:    */ import java.net.Proxy.Type;
/*  12:    */ import java.net.URL;
/*  13:    */ import java.nio.charset.Charset;
/*  14:    */ import java.security.MessageDigest;
/*  15:    */ import java.security.SecureRandom;
/*  16:    */ import java.security.cert.X509Certificate;
/*  17:    */ import java.util.ArrayList;
/*  18:    */ import java.util.Collection;
/*  19:    */ import java.util.HashMap;
/*  20:    */ import java.util.List;
/*  21:    */ import java.util.Map;
/*  22:    */ import javax.xml.bind.DatatypeConverter;
/*  23:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  24:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  25:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  26:    */ import org.apache.poi.util.HexDump;
/*  27:    */ import org.apache.poi.util.IOUtils;
/*  28:    */ import org.apache.poi.util.POILogFactory;
/*  29:    */ import org.apache.poi.util.POILogger;
/*  30:    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*  31:    */ import org.bouncycastle.asn1.cmp.PKIFailureInfo;
/*  32:    */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*  33:    */ import org.bouncycastle.asn1.x500.X500Name;
/*  34:    */ import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
/*  35:    */ import org.bouncycastle.cert.X509CertificateHolder;
/*  36:    */ import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
/*  37:    */ import org.bouncycastle.cms.DefaultCMSSignatureAlgorithmNameGenerator;
/*  38:    */ import org.bouncycastle.cms.SignerId;
/*  39:    */ import org.bouncycastle.cms.SignerInformationVerifier;
/*  40:    */ import org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder;
/*  41:    */ import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
/*  42:    */ import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
/*  43:    */ import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
/*  44:    */ import org.bouncycastle.tsp.TimeStampRequest;
/*  45:    */ import org.bouncycastle.tsp.TimeStampRequestGenerator;
/*  46:    */ import org.bouncycastle.tsp.TimeStampResponse;
/*  47:    */ import org.bouncycastle.tsp.TimeStampToken;
/*  48:    */ import org.bouncycastle.tsp.TimeStampTokenInfo;
/*  49:    */ import org.bouncycastle.util.Store;
/*  50:    */ 
/*  51:    */ public class TSPTimeStampService
/*  52:    */   implements TimeStampService
/*  53:    */ {
/*  54: 82 */   private static final POILogger LOG = POILogFactory.getLogger(TSPTimeStampService.class);
/*  55:    */   private SignatureConfig signatureConfig;
/*  56:    */   
/*  57:    */   public ASN1ObjectIdentifier mapDigestAlgoToOID(HashAlgorithm digestAlgo)
/*  58:    */   {
/*  59: 90 */     switch (1.$SwitchMap$org$apache$poi$poifs$crypt$HashAlgorithm[digestAlgo.ordinal()])
/*  60:    */     {
/*  61:    */     case 1: 
/*  62: 91 */       return X509ObjectIdentifiers.id_SHA1;
/*  63:    */     case 2: 
/*  64: 92 */       return NISTObjectIdentifiers.id_sha256;
/*  65:    */     case 3: 
/*  66: 93 */       return NISTObjectIdentifiers.id_sha384;
/*  67:    */     case 4: 
/*  68: 94 */       return NISTObjectIdentifiers.id_sha512;
/*  69:    */     }
/*  70: 96 */     throw new IllegalArgumentException("unsupported digest algo: " + digestAlgo);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public byte[] timeStamp(byte[] data, RevocationData revocationData)
/*  74:    */     throws Exception
/*  75:    */   {
/*  76:104 */     MessageDigest messageDigest = CryptoFunctions.getMessageDigest(this.signatureConfig.getTspDigestAlgo());
/*  77:105 */     byte[] digest = messageDigest.digest(data);
/*  78:    */     
/*  79:    */ 
/*  80:108 */     BigInteger nonce = new BigInteger(128, new SecureRandom());
/*  81:109 */     TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
/*  82:110 */     requestGenerator.setCertReq(true);
/*  83:111 */     String requestPolicy = this.signatureConfig.getTspRequestPolicy();
/*  84:112 */     if (requestPolicy != null) {
/*  85:113 */       requestGenerator.setReqPolicy(new ASN1ObjectIdentifier(requestPolicy));
/*  86:    */     }
/*  87:115 */     ASN1ObjectIdentifier digestAlgoOid = mapDigestAlgoToOID(this.signatureConfig.getTspDigestAlgo());
/*  88:116 */     TimeStampRequest request = requestGenerator.generate(digestAlgoOid, digest, nonce);
/*  89:117 */     byte[] encodedRequest = request.getEncoded();
/*  90:    */     
/*  91:    */ 
/*  92:120 */     Proxy proxy = Proxy.NO_PROXY;
/*  93:121 */     if (this.signatureConfig.getProxyUrl() != null)
/*  94:    */     {
/*  95:122 */       URL proxyUrl = new URL(this.signatureConfig.getProxyUrl());
/*  96:123 */       String host = proxyUrl.getHost();
/*  97:124 */       int port = proxyUrl.getPort();
/*  98:125 */       proxy = new Proxy(Type.HTTP, new InetSocketAddress(InetAddress.getByName(host), port == -1 ? 80 : port));
/*  99:    */     }
/* 100:128 */     HttpURLConnection huc = (HttpURLConnection)new URL(this.signatureConfig.getTspUrl()).openConnection(proxy);
/* 101:130 */     if (this.signatureConfig.getTspUser() != null)
/* 102:    */     {
/* 103:131 */       String userPassword = this.signatureConfig.getTspUser() + ":" + this.signatureConfig.getTspPass();
/* 104:132 */       String encoding = DatatypeConverter.printBase64Binary(userPassword.getBytes(Charset.forName("iso-8859-1")));
/* 105:133 */       huc.setRequestProperty("Authorization", "Basic " + encoding);
/* 106:    */     }
/* 107:136 */     huc.setRequestMethod("POST");
/* 108:137 */     huc.setConnectTimeout(20000);
/* 109:138 */     huc.setReadTimeout(20000);
/* 110:139 */     huc.setDoOutput(true);
/* 111:140 */     huc.setRequestProperty("User-Agent", this.signatureConfig.getUserAgent());
/* 112:141 */     huc.setRequestProperty("Content-Type", this.signatureConfig.isTspOldProtocol() ? "application/timestamp-request" : "application/timestamp-query");
/* 113:    */     
/* 114:    */ 
/* 115:    */ 
/* 116:145 */     OutputStream hucOut = huc.getOutputStream();
/* 117:146 */     hucOut.write(encodedRequest);
/* 118:    */     
/* 119:    */ 
/* 120:149 */     huc.connect();
/* 121:    */     
/* 122:151 */     int statusCode = huc.getResponseCode();
/* 123:152 */     if (statusCode != 200)
/* 124:    */     {
/* 125:153 */       LOG.log(7, new Object[] { "Error contacting TSP server ", this.signatureConfig.getTspUrl() + ", had status code " + statusCode + "/" + huc.getResponseMessage() });
/* 126:    */       
/* 127:155 */       throw new IOException("Error contacting TSP server " + this.signatureConfig.getTspUrl() + ", had status code " + statusCode + "/" + huc.getResponseMessage());
/* 128:    */     }
/* 129:160 */     String contentType = huc.getHeaderField("Content-Type");
/* 130:161 */     if (null == contentType) {
/* 131:162 */       throw new RuntimeException("missing Content-Type header");
/* 132:    */     }
/* 133:165 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 134:166 */     IOUtils.copy(huc.getInputStream(), bos);
/* 135:167 */     LOG.log(1, new Object[] { "response content: ", HexDump.dump(bos.toByteArray(), 0L, 0) });
/* 136:169 */     if (!contentType.startsWith(this.signatureConfig.isTspOldProtocol() ? "application/timestamp-response" : "application/timestamp-reply")) {
/* 137:173 */       throw new RuntimeException("invalid Content-Type: " + contentType + ": " + HexDump.dump(bos.toByteArray(), 0L, 0, 200));
/* 138:    */     }
/* 139:178 */     if (bos.size() == 0) {
/* 140:179 */       throw new RuntimeException("Content-Length is zero");
/* 141:    */     }
/* 142:183 */     TimeStampResponse timeStampResponse = new TimeStampResponse(bos.toByteArray());
/* 143:184 */     timeStampResponse.validate(request);
/* 144:186 */     if (0 != timeStampResponse.getStatus())
/* 145:    */     {
/* 146:187 */       LOG.log(1, new Object[] { "status: " + timeStampResponse.getStatus() });
/* 147:188 */       LOG.log(1, new Object[] { "status string: " + timeStampResponse.getStatusString() });
/* 148:189 */       PKIFailureInfo failInfo = timeStampResponse.getFailInfo();
/* 149:190 */       if (null != failInfo)
/* 150:    */       {
/* 151:191 */         LOG.log(1, new Object[] { "fail info int value: " + failInfo.intValue() });
/* 152:192 */         if (256 == failInfo.intValue()) {
/* 153:193 */           LOG.log(1, new Object[] { "unaccepted policy" });
/* 154:    */         }
/* 155:    */       }
/* 156:196 */       throw new RuntimeException("timestamp response status != 0: " + timeStampResponse.getStatus());
/* 157:    */     }
/* 158:199 */     TimeStampToken timeStampToken = timeStampResponse.getTimeStampToken();
/* 159:200 */     SignerId signerId = timeStampToken.getSID();
/* 160:201 */     BigInteger signerCertSerialNumber = signerId.getSerialNumber();
/* 161:202 */     X500Name signerCertIssuer = signerId.getIssuer();
/* 162:203 */     LOG.log(1, new Object[] { "signer cert serial number: " + signerCertSerialNumber });
/* 163:204 */     LOG.log(1, new Object[] { "signer cert issuer: " + signerCertIssuer });
/* 164:    */     
/* 165:    */ 
/* 166:207 */     Collection<X509CertificateHolder> certificates = timeStampToken.getCertificates().getMatches(null);
/* 167:    */     
/* 168:209 */     X509CertificateHolder signerCert = null;
/* 169:210 */     Map<X500Name, X509CertificateHolder> certificateMap = new HashMap();
/* 170:211 */     for (X509CertificateHolder certificate : certificates)
/* 171:    */     {
/* 172:212 */       if ((signerCertIssuer.equals(certificate.getIssuer())) && (signerCertSerialNumber.equals(certificate.getSerialNumber()))) {
/* 173:214 */         signerCert = certificate;
/* 174:    */       }
/* 175:216 */       certificateMap.put(certificate.getSubject(), certificate);
/* 176:    */     }
/* 177:220 */     if (signerCert == null) {
/* 178:221 */       throw new RuntimeException("TSP response token has no signer certificate");
/* 179:    */     }
/* 180:223 */     List<X509Certificate> tspCertificateChain = new ArrayList();
/* 181:224 */     JcaX509CertificateConverter x509converter = new JcaX509CertificateConverter();
/* 182:225 */     x509converter.setProvider("BC");
/* 183:226 */     X509CertificateHolder certificate = signerCert;
/* 184:    */     do
/* 185:    */     {
/* 186:228 */       LOG.log(1, new Object[] { "adding to certificate chain: " + certificate.getSubject() });
/* 187:229 */       tspCertificateChain.add(x509converter.getCertificate(certificate));
/* 188:230 */       if (certificate.getSubject().equals(certificate.getIssuer())) {
/* 189:    */         break;
/* 190:    */       }
/* 191:233 */       certificate = (X509CertificateHolder)certificateMap.get(certificate.getIssuer());
/* 192:234 */     } while (null != certificate);
/* 193:237 */     X509CertificateHolder holder = new X509CertificateHolder(((X509Certificate)tspCertificateChain.get(0)).getEncoded());
/* 194:238 */     DefaultCMSSignatureAlgorithmNameGenerator nameGen = new DefaultCMSSignatureAlgorithmNameGenerator();
/* 195:239 */     DefaultSignatureAlgorithmIdentifierFinder sigAlgoFinder = new DefaultSignatureAlgorithmIdentifierFinder();
/* 196:240 */     DefaultDigestAlgorithmIdentifierFinder hashAlgoFinder = new DefaultDigestAlgorithmIdentifierFinder();
/* 197:241 */     BcDigestCalculatorProvider calculator = new BcDigestCalculatorProvider();
/* 198:242 */     BcRSASignerInfoVerifierBuilder verifierBuilder = new BcRSASignerInfoVerifierBuilder(nameGen, sigAlgoFinder, hashAlgoFinder, calculator);
/* 199:243 */     SignerInformationVerifier verifier = verifierBuilder.build(holder);
/* 200:    */     
/* 201:245 */     timeStampToken.validate(verifier);
/* 202:248 */     if (this.signatureConfig.getTspValidator() != null) {
/* 203:249 */       this.signatureConfig.getTspValidator().validate(tspCertificateChain, revocationData);
/* 204:    */     }
/* 205:252 */     LOG.log(1, new Object[] { "time-stamp token time: " + timeStampToken.getTimeStampInfo().getGenTime() });
/* 206:    */     
/* 207:    */ 
/* 208:255 */     return timeStampToken.getEncoded();
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setSignatureConfig(SignatureConfig signatureConfig)
/* 212:    */   {
/* 213:259 */     this.signatureConfig = signatureConfig;
/* 214:    */   }
/* 215:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.TSPTimeStampService

 * JD-Core Version:    0.7.0.1

 */