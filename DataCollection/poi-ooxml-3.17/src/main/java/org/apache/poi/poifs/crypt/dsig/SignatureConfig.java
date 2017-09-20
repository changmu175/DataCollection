/*   1:    */ package org.apache.poi.poifs.crypt.dsig;
/*   2:    */ 
/*   3:    */ import java.security.PrivateKey;
/*   4:    */ import java.security.Provider;
/*   5:    */ import java.security.cert.X509Certificate;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import java.util.UUID;
/*  12:    */ import javax.xml.crypto.URIDereferencer;
/*  13:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  14:    */ import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
/*  15:    */ import org.apache.poi.EncryptedDocumentException;
/*  16:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  17:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  18:    */ import org.apache.poi.poifs.crypt.dsig.facets.KeyInfoSignatureFacet;
/*  19:    */ import org.apache.poi.poifs.crypt.dsig.facets.OOXMLSignatureFacet;
/*  20:    */ import org.apache.poi.poifs.crypt.dsig.facets.Office2010SignatureFacet;
/*  21:    */ import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
/*  22:    */ import org.apache.poi.poifs.crypt.dsig.facets.XAdESSignatureFacet;
/*  23:    */ import org.apache.poi.poifs.crypt.dsig.services.RevocationDataService;
/*  24:    */ import org.apache.poi.poifs.crypt.dsig.services.SignaturePolicyService;
/*  25:    */ import org.apache.poi.poifs.crypt.dsig.services.TSPTimeStampService;
/*  26:    */ import org.apache.poi.poifs.crypt.dsig.services.TimeStampService;
/*  27:    */ import org.apache.poi.poifs.crypt.dsig.services.TimeStampServiceValidator;
/*  28:    */ import org.apache.poi.util.POILogFactory;
/*  29:    */ import org.apache.poi.util.POILogger;
/*  30:    */ import org.w3c.dom.events.EventListener;
/*  31:    */ 
/*  32:    */ public class SignatureConfig
/*  33:    */ {
/*  34: 65 */   private static final POILogger LOG = POILogFactory.getLogger(SignatureConfig.class);
/*  35: 71 */   private ThreadLocal<OPCPackage> opcPackage = new ThreadLocal();
/*  36: 72 */   private ThreadLocal<XMLSignatureFactory> signatureFactory = new ThreadLocal();
/*  37: 73 */   private ThreadLocal<KeyInfoFactory> keyInfoFactory = new ThreadLocal();
/*  38: 74 */   private ThreadLocal<Provider> provider = new ThreadLocal();
/*  39: 76 */   private List<SignatureFacet> signatureFacets = new ArrayList();
/*  40: 77 */   private HashAlgorithm digestAlgo = HashAlgorithm.sha1;
/*  41: 78 */   private Date executionTime = new Date();
/*  42:    */   private PrivateKey key;
/*  43:    */   private List<X509Certificate> signingCertificateChain;
/*  44:    */   private SignaturePolicyService signaturePolicyService;
/*  45: 86 */   private URIDereferencer uriDereferencer = null;
/*  46: 87 */   private String canonicalizationMethod = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*  47: 89 */   private boolean includeEntireCertificateChain = true;
/*  48: 90 */   private boolean includeIssuerSerial = false;
/*  49: 91 */   private boolean includeKeyValue = false;
/*  50: 96 */   private TimeStampService tspService = new TSPTimeStampService();
/*  51:    */   private String tspUrl;
/*  52:101 */   private boolean tspOldProtocol = false;
/*  53:105 */   private HashAlgorithm tspDigestAlgo = null;
/*  54:    */   private String tspUser;
/*  55:    */   private String tspPass;
/*  56:    */   private TimeStampServiceValidator tspValidator;
/*  57:112 */   private String tspRequestPolicy = "1.3.6.1.4.1.13762.3";
/*  58:113 */   private String userAgent = "POI XmlSign Service TSP Client";
/*  59:    */   private String proxyUrl;
/*  60:    */   private RevocationDataService revocationDataService;
/*  61:124 */   private HashAlgorithm xadesDigestAlgo = null;
/*  62:125 */   private String xadesRole = null;
/*  63:126 */   private String xadesSignatureId = "idSignedProperties";
/*  64:127 */   private boolean xadesSignaturePolicyImplied = true;
/*  65:128 */   private String xadesCanonicalizationMethod = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*  66:133 */   private boolean xadesIssuerNameNoReverseOrder = true;
/*  67:139 */   private String packageSignatureId = "idPackageSignature";
/*  68:145 */   private String signatureDescription = "Office OpenXML Document";
/*  69:153 */   EventListener signatureMarshalListener = null;
/*  70:159 */   Map<String, String> namespacePrefixes = new HashMap();
/*  71:    */   
/*  72:    */   protected void init(boolean onlyValidation)
/*  73:    */   {
/*  74:171 */     if (this.opcPackage == null) {
/*  75:172 */       throw new EncryptedDocumentException("opcPackage is null");
/*  76:    */     }
/*  77:174 */     if (this.uriDereferencer == null) {
/*  78:175 */       this.uriDereferencer = new OOXMLURIDereferencer();
/*  79:    */     }
/*  80:177 */     if ((this.uriDereferencer instanceof SignatureConfigurable)) {
/*  81:178 */       ((SignatureConfigurable)this.uriDereferencer).setSignatureConfig(this);
/*  82:    */     }
/*  83:180 */     if (this.namespacePrefixes.isEmpty())
/*  84:    */     {
/*  85:185 */       this.namespacePrefixes.put("http://schemas.openxmlformats.org/package/2006/digital-signature", "mdssi");
/*  86:186 */       this.namespacePrefixes.put("http://uri.etsi.org/01903/v1.3.2#", "xd");
/*  87:    */     }
/*  88:189 */     if (onlyValidation) {
/*  89:189 */       return;
/*  90:    */     }
/*  91:191 */     if (this.signatureMarshalListener == null) {
/*  92:192 */       this.signatureMarshalListener = new SignatureMarshalListener();
/*  93:    */     }
/*  94:195 */     if ((this.signatureMarshalListener instanceof SignatureConfigurable)) {
/*  95:196 */       ((SignatureConfigurable)this.signatureMarshalListener).setSignatureConfig(this);
/*  96:    */     }
/*  97:199 */     if (this.tspService != null) {
/*  98:200 */       this.tspService.setSignatureConfig(this);
/*  99:    */     }
/* 100:203 */     if (this.signatureFacets.isEmpty())
/* 101:    */     {
/* 102:204 */       addSignatureFacet(new OOXMLSignatureFacet());
/* 103:205 */       addSignatureFacet(new KeyInfoSignatureFacet());
/* 104:206 */       addSignatureFacet(new XAdESSignatureFacet());
/* 105:207 */       addSignatureFacet(new Office2010SignatureFacet());
/* 106:    */     }
/* 107:210 */     for (SignatureFacet sf : this.signatureFacets) {
/* 108:211 */       sf.setSignatureConfig(this);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void addSignatureFacet(SignatureFacet signatureFacet)
/* 113:    */   {
/* 114:219 */     this.signatureFacets.add(signatureFacet);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public List<SignatureFacet> getSignatureFacets()
/* 118:    */   {
/* 119:226 */     return this.signatureFacets;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setSignatureFacets(List<SignatureFacet> signatureFacets)
/* 123:    */   {
/* 124:233 */     this.signatureFacets = signatureFacets;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public HashAlgorithm getDigestAlgo()
/* 128:    */   {
/* 129:240 */     return this.digestAlgo;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setDigestAlgo(HashAlgorithm digestAlgo)
/* 133:    */   {
/* 134:247 */     this.digestAlgo = digestAlgo;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public OPCPackage getOpcPackage()
/* 138:    */   {
/* 139:254 */     return (OPCPackage)this.opcPackage.get();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setOpcPackage(OPCPackage opcPackage)
/* 143:    */   {
/* 144:261 */     this.opcPackage.set(opcPackage);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public PrivateKey getKey()
/* 148:    */   {
/* 149:268 */     return this.key;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setKey(PrivateKey key)
/* 153:    */   {
/* 154:275 */     this.key = key;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public List<X509Certificate> getSigningCertificateChain()
/* 158:    */   {
/* 159:283 */     return this.signingCertificateChain;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void setSigningCertificateChain(List<X509Certificate> signingCertificateChain)
/* 163:    */   {
/* 164:292 */     this.signingCertificateChain = signingCertificateChain;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public Date getExecutionTime()
/* 168:    */   {
/* 169:300 */     return this.executionTime;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setExecutionTime(Date executionTime)
/* 173:    */   {
/* 174:307 */     this.executionTime = executionTime;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public SignaturePolicyService getSignaturePolicyService()
/* 178:    */   {
/* 179:314 */     return this.signaturePolicyService;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setSignaturePolicyService(SignaturePolicyService signaturePolicyService)
/* 183:    */   {
/* 184:321 */     this.signaturePolicyService = signaturePolicyService;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public URIDereferencer getUriDereferencer()
/* 188:    */   {
/* 189:328 */     return this.uriDereferencer;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setUriDereferencer(URIDereferencer uriDereferencer)
/* 193:    */   {
/* 194:335 */     this.uriDereferencer = uriDereferencer;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public String getSignatureDescription()
/* 198:    */   {
/* 199:343 */     return this.signatureDescription;
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void setSignatureDescription(String signatureDescription)
/* 203:    */   {
/* 204:351 */     this.signatureDescription = signatureDescription;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public String getCanonicalizationMethod()
/* 208:    */   {
/* 209:358 */     return this.canonicalizationMethod;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public void setCanonicalizationMethod(String canonicalizationMethod)
/* 213:    */   {
/* 214:365 */     this.canonicalizationMethod = canonicalizationMethod;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public String getPackageSignatureId()
/* 218:    */   {
/* 219:373 */     return this.packageSignatureId;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void setPackageSignatureId(String packageSignatureId)
/* 223:    */   {
/* 224:381 */     this.packageSignatureId = ((String)nvl(packageSignatureId, "xmldsig-" + UUID.randomUUID()));
/* 225:    */   }
/* 226:    */   
/* 227:    */   public String getTspUrl()
/* 228:    */   {
/* 229:388 */     return this.tspUrl;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public void setTspUrl(String tspUrl)
/* 233:    */   {
/* 234:395 */     this.tspUrl = tspUrl;
/* 235:    */   }
/* 236:    */   
/* 237:    */   public boolean isTspOldProtocol()
/* 238:    */   {
/* 239:403 */     return this.tspOldProtocol;
/* 240:    */   }
/* 241:    */   
/* 242:    */   public void setTspOldProtocol(boolean tspOldProtocol)
/* 243:    */   {
/* 244:411 */     this.tspOldProtocol = tspOldProtocol;
/* 245:    */   }
/* 246:    */   
/* 247:    */   public HashAlgorithm getTspDigestAlgo()
/* 248:    */   {
/* 249:419 */     return (HashAlgorithm)nvl(this.tspDigestAlgo, this.digestAlgo);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public void setTspDigestAlgo(HashAlgorithm tspDigestAlgo)
/* 253:    */   {
/* 254:427 */     this.tspDigestAlgo = tspDigestAlgo;
/* 255:    */   }
/* 256:    */   
/* 257:    */   public String getProxyUrl()
/* 258:    */   {
/* 259:435 */     return this.proxyUrl;
/* 260:    */   }
/* 261:    */   
/* 262:    */   public void setProxyUrl(String proxyUrl)
/* 263:    */   {
/* 264:443 */     this.proxyUrl = proxyUrl;
/* 265:    */   }
/* 266:    */   
/* 267:    */   public TimeStampService getTspService()
/* 268:    */   {
/* 269:450 */     return this.tspService;
/* 270:    */   }
/* 271:    */   
/* 272:    */   public void setTspService(TimeStampService tspService)
/* 273:    */   {
/* 274:457 */     this.tspService = tspService;
/* 275:    */   }
/* 276:    */   
/* 277:    */   public String getTspUser()
/* 278:    */   {
/* 279:464 */     return this.tspUser;
/* 280:    */   }
/* 281:    */   
/* 282:    */   public void setTspUser(String tspUser)
/* 283:    */   {
/* 284:471 */     this.tspUser = tspUser;
/* 285:    */   }
/* 286:    */   
/* 287:    */   public String getTspPass()
/* 288:    */   {
/* 289:478 */     return this.tspPass;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public void setTspPass(String tspPass)
/* 293:    */   {
/* 294:485 */     this.tspPass = tspPass;
/* 295:    */   }
/* 296:    */   
/* 297:    */   public TimeStampServiceValidator getTspValidator()
/* 298:    */   {
/* 299:492 */     return this.tspValidator;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public void setTspValidator(TimeStampServiceValidator tspValidator)
/* 303:    */   {
/* 304:499 */     this.tspValidator = tspValidator;
/* 305:    */   }
/* 306:    */   
/* 307:    */   public RevocationDataService getRevocationDataService()
/* 308:    */   {
/* 309:507 */     return this.revocationDataService;
/* 310:    */   }
/* 311:    */   
/* 312:    */   public void setRevocationDataService(RevocationDataService revocationDataService)
/* 313:    */   {
/* 314:515 */     this.revocationDataService = revocationDataService;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public HashAlgorithm getXadesDigestAlgo()
/* 318:    */   {
/* 319:522 */     return (HashAlgorithm)nvl(this.xadesDigestAlgo, this.digestAlgo);
/* 320:    */   }
/* 321:    */   
/* 322:    */   public void setXadesDigestAlgo(HashAlgorithm xadesDigestAlgo)
/* 323:    */   {
/* 324:530 */     this.xadesDigestAlgo = xadesDigestAlgo;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public String getUserAgent()
/* 328:    */   {
/* 329:537 */     return this.userAgent;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public void setUserAgent(String userAgent)
/* 333:    */   {
/* 334:544 */     this.userAgent = userAgent;
/* 335:    */   }
/* 336:    */   
/* 337:    */   public String getTspRequestPolicy()
/* 338:    */   {
/* 339:552 */     return this.tspRequestPolicy;
/* 340:    */   }
/* 341:    */   
/* 342:    */   public void setTspRequestPolicy(String tspRequestPolicy)
/* 343:    */   {
/* 344:559 */     this.tspRequestPolicy = tspRequestPolicy;
/* 345:    */   }
/* 346:    */   
/* 347:    */   public boolean isIncludeEntireCertificateChain()
/* 348:    */   {
/* 349:567 */     return this.includeEntireCertificateChain;
/* 350:    */   }
/* 351:    */   
/* 352:    */   public void setIncludeEntireCertificateChain(boolean includeEntireCertificateChain)
/* 353:    */   {
/* 354:575 */     this.includeEntireCertificateChain = includeEntireCertificateChain;
/* 355:    */   }
/* 356:    */   
/* 357:    */   public boolean isIncludeIssuerSerial()
/* 358:    */   {
/* 359:582 */     return this.includeIssuerSerial;
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void setIncludeIssuerSerial(boolean includeIssuerSerial)
/* 363:    */   {
/* 364:589 */     this.includeIssuerSerial = includeIssuerSerial;
/* 365:    */   }
/* 366:    */   
/* 367:    */   public boolean isIncludeKeyValue()
/* 368:    */   {
/* 369:596 */     return this.includeKeyValue;
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void setIncludeKeyValue(boolean includeKeyValue)
/* 373:    */   {
/* 374:603 */     this.includeKeyValue = includeKeyValue;
/* 375:    */   }
/* 376:    */   
/* 377:    */   public String getXadesRole()
/* 378:    */   {
/* 379:611 */     return this.xadesRole;
/* 380:    */   }
/* 381:    */   
/* 382:    */   public void setXadesRole(String xadesRole)
/* 383:    */   {
/* 384:618 */     this.xadesRole = xadesRole;
/* 385:    */   }
/* 386:    */   
/* 387:    */   public String getXadesSignatureId()
/* 388:    */   {
/* 389:626 */     return (String)nvl(this.xadesSignatureId, "idSignedProperties");
/* 390:    */   }
/* 391:    */   
/* 392:    */   public void setXadesSignatureId(String xadesSignatureId)
/* 393:    */   {
/* 394:634 */     this.xadesSignatureId = xadesSignatureId;
/* 395:    */   }
/* 396:    */   
/* 397:    */   public boolean isXadesSignaturePolicyImplied()
/* 398:    */   {
/* 399:642 */     return this.xadesSignaturePolicyImplied;
/* 400:    */   }
/* 401:    */   
/* 402:    */   public void setXadesSignaturePolicyImplied(boolean xadesSignaturePolicyImplied)
/* 403:    */   {
/* 404:649 */     this.xadesSignaturePolicyImplied = xadesSignaturePolicyImplied;
/* 405:    */   }
/* 406:    */   
/* 407:    */   public boolean isXadesIssuerNameNoReverseOrder()
/* 408:    */   {
/* 409:662 */     return this.xadesIssuerNameNoReverseOrder;
/* 410:    */   }
/* 411:    */   
/* 412:    */   public void setXadesIssuerNameNoReverseOrder(boolean xadesIssuerNameNoReverseOrder)
/* 413:    */   {
/* 414:669 */     this.xadesIssuerNameNoReverseOrder = xadesIssuerNameNoReverseOrder;
/* 415:    */   }
/* 416:    */   
/* 417:    */   public EventListener getSignatureMarshalListener()
/* 418:    */   {
/* 419:679 */     return this.signatureMarshalListener;
/* 420:    */   }
/* 421:    */   
/* 422:    */   public void setSignatureMarshalListener(EventListener signatureMarshalListener)
/* 423:    */   {
/* 424:687 */     this.signatureMarshalListener = signatureMarshalListener;
/* 425:    */   }
/* 426:    */   
/* 427:    */   public Map<String, String> getNamespacePrefixes()
/* 428:    */   {
/* 429:694 */     return this.namespacePrefixes;
/* 430:    */   }
/* 431:    */   
/* 432:    */   public void setNamespacePrefixes(Map<String, String> namespacePrefixes)
/* 433:    */   {
/* 434:701 */     this.namespacePrefixes = namespacePrefixes;
/* 435:    */   }
/* 436:    */   
/* 437:    */   protected static <T> T nvl(T value, T defaultValue)
/* 438:    */   {
/* 439:711 */     return value == null ? defaultValue : value;
/* 440:    */   }
/* 441:    */   
/* 442:    */   public byte[] getHashMagic()
/* 443:    */   {
/* 444:    */     byte[] result;
/* 445:723 */     switch (1.$SwitchMap$org$apache$poi$poifs$crypt$HashAlgorithm[getDigestAlgo().ordinal()])
/* 446:    */     {
/* 447:    */     case 1: 
/* 448:724 */       result = new byte[] { 48, 31, 48, 7, 6, 5, 43, 14, 3, 2, 26, 4, 20 };
/* 449:    */       
/* 450:    */ 
/* 451:727 */       break;
/* 452:    */     case 2: 
/* 453:728 */       result = new byte[] { 48, 43, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 2, 4, 4, 28 };
/* 454:    */       
/* 455:    */ 
/* 456:731 */       break;
/* 457:    */     case 3: 
/* 458:732 */       result = new byte[] { 48, 47, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 2, 1, 4, 32 };
/* 459:    */       
/* 460:    */ 
/* 461:735 */       break;
/* 462:    */     case 4: 
/* 463:736 */       result = new byte[] { 48, 63, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 2, 2, 4, 48 };
/* 464:    */       
/* 465:    */ 
/* 466:739 */       break;
/* 467:    */     case 5: 
/* 468:740 */       result = new byte[] { 48, 79, 48, 11, 6, 9, 96, -122, 72, 1, 101, 3, 4, 2, 3, 4, 64 };
/* 469:    */       
/* 470:    */ 
/* 471:743 */       break;
/* 472:    */     case 6: 
/* 473:744 */       result = new byte[] { 48, 27, 48, 7, 6, 5, 43, 36, 3, 2, 2, 4, 16 };
/* 474:    */       
/* 475:    */ 
/* 476:747 */       break;
/* 477:    */     case 7: 
/* 478:748 */       result = new byte[] { 48, 31, 48, 7, 6, 5, 43, 36, 3, 2, 1, 4, 20 };
/* 479:    */       
/* 480:    */ 
/* 481:751 */       break;
/* 482:    */     default: 
/* 483:756 */       throw new EncryptedDocumentException("Hash algorithm " + getDigestAlgo() + " not supported for signing.");
/* 484:    */     }
/* 485:760 */     return result;
/* 486:    */   }
/* 487:    */   
/* 488:    */   public String getSignatureMethodUri()
/* 489:    */   {
/* 490:768 */     switch (1.$SwitchMap$org$apache$poi$poifs$crypt$HashAlgorithm[getDigestAlgo().ordinal()])
/* 491:    */     {
/* 492:    */     case 1: 
/* 493:769 */       return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/* 494:    */     case 2: 
/* 495:770 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha224";
/* 496:    */     case 3: 
/* 497:771 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/* 498:    */     case 4: 
/* 499:772 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
/* 500:    */     case 5: 
/* 501:773 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
/* 502:    */     case 7: 
/* 503:774 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160";
/* 504:    */     }
/* 505:775 */     throw new EncryptedDocumentException("Hash algorithm " + getDigestAlgo() + " not supported for signing.");
/* 506:    */   }
/* 507:    */   
/* 508:    */   public String getDigestMethodUri()
/* 509:    */   {
/* 510:784 */     return getDigestMethodUri(getDigestAlgo());
/* 511:    */   }
/* 512:    */   
/* 513:    */   public static String getDigestMethodUri(HashAlgorithm digestAlgo)
/* 514:    */   {
/* 515:792 */     switch (1.$SwitchMap$org$apache$poi$poifs$crypt$HashAlgorithm[digestAlgo.ordinal()])
/* 516:    */     {
/* 517:    */     case 1: 
/* 518:793 */       return "http://www.w3.org/2000/09/xmldsig#sha1";
/* 519:    */     case 2: 
/* 520:794 */       return "http://www.w3.org/2001/04/xmldsig-more#sha224";
/* 521:    */     case 3: 
/* 522:795 */       return "http://www.w3.org/2001/04/xmlenc#sha256";
/* 523:    */     case 4: 
/* 524:796 */       return "http://www.w3.org/2001/04/xmldsig-more#sha384";
/* 525:    */     case 5: 
/* 526:797 */       return "http://www.w3.org/2001/04/xmlenc#sha512";
/* 527:    */     case 7: 
/* 528:798 */       return "http://www.w3.org/2001/04/xmlenc#ripemd160";
/* 529:    */     }
/* 530:799 */     throw new EncryptedDocumentException("Hash algorithm " + digestAlgo + " not supported for signing.");
/* 531:    */   }
/* 532:    */   
/* 533:    */   public void setSignatureFactory(XMLSignatureFactory signatureFactory)
/* 534:    */   {
/* 535:808 */     this.signatureFactory.set(signatureFactory);
/* 536:    */   }
/* 537:    */   
/* 538:    */   public XMLSignatureFactory getSignatureFactory()
/* 539:    */   {
/* 540:815 */     XMLSignatureFactory sigFac = (XMLSignatureFactory)this.signatureFactory.get();
/* 541:816 */     if (sigFac == null)
/* 542:    */     {
/* 543:817 */       sigFac = XMLSignatureFactory.getInstance("DOM", getProvider());
/* 544:818 */       setSignatureFactory(sigFac);
/* 545:    */     }
/* 546:820 */     return sigFac;
/* 547:    */   }
/* 548:    */   
/* 549:    */   public void setKeyInfoFactory(KeyInfoFactory keyInfoFactory)
/* 550:    */   {
/* 551:827 */     this.keyInfoFactory.set(keyInfoFactory);
/* 552:    */   }
/* 553:    */   
/* 554:    */   public KeyInfoFactory getKeyInfoFactory()
/* 555:    */   {
/* 556:834 */     KeyInfoFactory keyFac = (KeyInfoFactory)this.keyInfoFactory.get();
/* 557:835 */     if (keyFac == null)
/* 558:    */     {
/* 559:836 */       keyFac = KeyInfoFactory.getInstance("DOM", getProvider());
/* 560:837 */       setKeyInfoFactory(keyFac);
/* 561:    */     }
/* 562:839 */     return keyFac;
/* 563:    */   }
/* 564:    */   
/* 565:    */   public Provider getProvider()
/* 566:    */   {
/* 567:856 */     Provider prov = (Provider)this.provider.get();
/* 568:857 */     if (prov == null)
/* 569:    */     {
/* 570:858 */       String[] dsigProviderNames = { System.getProperty("jsr105Provider"), "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI", "org.jcp.xml.dsig.internal.dom.XMLDSigRI" };
/* 571:863 */       for (String pn : dsigProviderNames) {
/* 572:864 */         if (pn != null) {
/* 573:    */           try
/* 574:    */           {
/* 575:866 */             prov = (Provider)Class.forName(pn).newInstance();
/* 576:    */           }
/* 577:    */           catch (Exception e)
/* 578:    */           {
/* 579:869 */             LOG.log(1, new Object[] { "XMLDsig-Provider '" + pn + "' can't be found - trying next." });
/* 580:    */           }
/* 581:    */         }
/* 582:    */       }
/* 583:    */     }
/* 584:874 */     if (prov == null) {
/* 585:875 */       throw new RuntimeException("JRE doesn't support default xml signature provider - set jsr105Provider system property!");
/* 586:    */     }
/* 587:878 */     return prov;
/* 588:    */   }
/* 589:    */   
/* 590:    */   public String getXadesCanonicalizationMethod()
/* 591:    */   {
/* 592:887 */     return this.xadesCanonicalizationMethod;
/* 593:    */   }
/* 594:    */   
/* 595:    */   public void setXadesCanonicalizationMethod(String xadesCanonicalizationMethod)
/* 596:    */   {
/* 597:895 */     this.xadesCanonicalizationMethod = xadesCanonicalizationMethod;
/* 598:    */   }
/* 599:    */   
/* 600:    */   public static abstract interface SignatureConfigurable
/* 601:    */   {
/* 602:    */     public abstract void setSignatureConfig(SignatureConfig paramSignatureConfig);
/* 603:    */   }
/* 604:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.SignatureConfig
 * JD-Core Version:    0.7.0.1
 */