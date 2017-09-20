/*   1:    */ package org.apache.poi.poifs.crypt.dsig;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.security.GeneralSecurityException;
/*   8:    */ import java.security.MessageDigest;
/*   9:    */ import java.security.Provider;
/*  10:    */ import java.security.Security;
/*  11:    */ import java.security.cert.X509Certificate;
/*  12:    */ import java.util.ArrayList;
/*  13:    */ import java.util.Collections;
/*  14:    */ import java.util.HashMap;
/*  15:    */ import java.util.Iterator;
/*  16:    */ import java.util.List;
/*  17:    */ import java.util.Map;
/*  18:    */ import java.util.Map.Entry;
/*  19:    */ import java.util.NoSuchElementException;
/*  20:    */ import javax.crypto.Cipher;
/*  21:    */ import javax.xml.crypto.MarshalException;
/*  22:    */ import javax.xml.crypto.URIDereferencer;
/*  23:    */ import javax.xml.crypto.XMLStructure;
/*  24:    */ import javax.xml.crypto.dsig.CanonicalizationMethod;
/*  25:    */ import javax.xml.crypto.dsig.Manifest;
/*  26:    */ import javax.xml.crypto.dsig.Reference;
/*  27:    */ import javax.xml.crypto.dsig.SignatureMethod;
/*  28:    */ import javax.xml.crypto.dsig.SignedInfo;
/*  29:    */ import javax.xml.crypto.dsig.XMLObject;
/*  30:    */ import javax.xml.crypto.dsig.XMLSignContext;
/*  31:    */ import javax.xml.crypto.dsig.XMLSignature;
/*  32:    */ import javax.xml.crypto.dsig.XMLSignatureException;
/*  33:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  34:    */ import javax.xml.crypto.dsig.XMLValidateContext;
/*  35:    */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*  36:    */ import javax.xml.crypto.dsig.dom.DOMValidateContext;
/*  37:    */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*  38:    */ import javax.xml.xpath.XPath;
/*  39:    */ import javax.xml.xpath.XPathConstants;
/*  40:    */ import javax.xml.xpath.XPathExpression;
/*  41:    */ import javax.xml.xpath.XPathExpressionException;
/*  42:    */ import javax.xml.xpath.XPathFactory;
/*  43:    */ import org.apache.jcp.xml.dsig.internal.dom.DOMReference;
/*  44:    */ import org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo;
/*  45:    */ import org.apache.poi.EncryptedDocumentException;
/*  46:    */ import org.apache.poi.POIXMLTypeLoader;
/*  47:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  48:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  49:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  50:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  51:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  52:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  53:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  54:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  55:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  56:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  57:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  58:    */ import org.apache.poi.poifs.crypt.dsig.facets.SignatureFacet;
/*  59:    */ import org.apache.poi.poifs.crypt.dsig.services.RelationshipTransformService;
/*  60:    */ import org.apache.poi.util.DocumentHelper;
/*  61:    */ import org.apache.poi.util.POILogFactory;
/*  62:    */ import org.apache.poi.util.POILogger;
/*  63:    */ import org.apache.xml.security.Init;
/*  64:    */ import org.apache.xml.security.utils.Base64;
/*  65:    */ import org.apache.xmlbeans.XmlException;
/*  66:    */ import org.apache.xmlbeans.XmlOptions;
/*  67:    */ import org.w3.x2000.x09.xmldsig.SignatureDocument;
/*  68:    */ import org.w3.x2000.x09.xmldsig.SignatureDocument.Factory;
/*  69:    */ import org.w3c.dom.Document;
/*  70:    */ import org.w3c.dom.Element;
/*  71:    */ import org.w3c.dom.Node;
/*  72:    */ import org.w3c.dom.NodeList;
/*  73:    */ import org.w3c.dom.events.EventListener;
/*  74:    */ import org.w3c.dom.events.EventTarget;
/*  75:    */ import org.xml.sax.SAXException;
/*  76:    */ 
/*  77:    */ public class SignatureInfo
/*  78:    */   implements SignatureConfig.SignatureConfigurable
/*  79:    */ {
/*  80:173 */   private static final POILogger LOG = POILogFactory.getLogger(SignatureInfo.class);
/*  81:174 */   private static boolean isInitialized = false;
/*  82:    */   private SignatureConfig signatureConfig;
/*  83:    */   
/*  84:    */   public class SignaturePart
/*  85:    */   {
/*  86:    */     private final PackagePart signaturePart;
/*  87:    */     private X509Certificate signer;
/*  88:    */     private List<X509Certificate> certChain;
/*  89:    */     
/*  90:    */     private SignaturePart(PackagePart signaturePart)
/*  91:    */     {
/*  92:184 */       this.signaturePart = signaturePart;
/*  93:    */     }
/*  94:    */     
/*  95:    */     public PackagePart getPackagePart()
/*  96:    */     {
/*  97:191 */       return this.signaturePart;
/*  98:    */     }
/*  99:    */     
/* 100:    */     public X509Certificate getSigner()
/* 101:    */     {
/* 102:198 */       return this.signer;
/* 103:    */     }
/* 104:    */     
/* 105:    */     public List<X509Certificate> getCertChain()
/* 106:    */     {
/* 107:205 */       return this.certChain;
/* 108:    */     }
/* 109:    */     
/* 110:    */     public SignatureDocument getSignatureDocument()
/* 111:    */       throws IOException, XmlException
/* 112:    */     {
/* 113:217 */       return SignatureDocument.Factory.parse(this.signaturePart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 114:    */     }
/* 115:    */     
/* 116:    */     public boolean validate()
/* 117:    */     {
/* 118:227 */       KeyInfoKeySelector keySelector = new KeyInfoKeySelector();
/* 119:    */       try
/* 120:    */       {
/* 121:229 */         Document doc = DocumentHelper.readDocument(this.signaturePart.getInputStream());
/* 122:230 */         XPath xpath = XPathFactory.newInstance().newXPath();
/* 123:231 */         NodeList nl = (NodeList)xpath.compile("//*[@Id]").evaluate(doc, XPathConstants.NODESET);
/* 124:232 */         int length = nl.getLength();
/* 125:233 */         for (int i = 0; i < length; i++) {
/* 126:234 */           ((Element)nl.item(i)).setIdAttribute("Id", true);
/* 127:    */         }
/* 128:237 */         DOMValidateContext domValidateContext = new DOMValidateContext(keySelector, doc);
/* 129:238 */         domValidateContext.setProperty("org.jcp.xml.dsig.validateManifests", Boolean.TRUE);
/* 130:239 */         domValidateContext.setURIDereferencer(SignatureInfo.this.signatureConfig.getUriDereferencer());
/* 131:240 */         SignatureInfo.this.brokenJvmWorkaround(domValidateContext);
/* 132:    */         
/* 133:242 */         XMLSignatureFactory xmlSignatureFactory = SignatureInfo.this.signatureConfig.getSignatureFactory();
/* 134:243 */         XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
/* 135:247 */         for (Reference ref : xmlSignature.getSignedInfo().getReferences()) {
/* 136:248 */           SignatureFacet.brokenJvmWorkaround(ref);
/* 137:    */         }
/* 138:250 */         for (XMLObject xo : xmlSignature.getObjects()) {
/* 139:251 */           for (XMLStructure xs : xo.getContent()) {
/* 140:252 */             if ((xs instanceof Manifest)) {
/* 141:253 */               for (Reference ref : ((Manifest)xs).getReferences()) {
/* 142:254 */                 SignatureFacet.brokenJvmWorkaround(ref);
/* 143:    */               }
/* 144:    */             }
/* 145:    */           }
/* 146:    */         }
/* 147:260 */         boolean valid = xmlSignature.validate(domValidateContext);
/* 148:262 */         if (valid)
/* 149:    */         {
/* 150:263 */           this.signer = keySelector.getSigner();
/* 151:264 */           this.certChain = keySelector.getCertChain();
/* 152:    */         }
/* 153:267 */         return valid;
/* 154:    */       }
/* 155:    */       catch (IOException e)
/* 156:    */       {
/* 157:269 */         String s = "error in reading document";
/* 158:270 */         SignatureInfo.LOG.log(7, new Object[] { s, e });
/* 159:271 */         throw new EncryptedDocumentException(s, e);
/* 160:    */       }
/* 161:    */       catch (SAXException e)
/* 162:    */       {
/* 163:273 */         String s = "error in parsing document";
/* 164:274 */         SignatureInfo.LOG.log(7, new Object[] { s, e });
/* 165:275 */         throw new EncryptedDocumentException(s, e);
/* 166:    */       }
/* 167:    */       catch (XPathExpressionException e)
/* 168:    */       {
/* 169:277 */         String s = "error in searching document with xpath expression";
/* 170:278 */         SignatureInfo.LOG.log(7, new Object[] { s, e });
/* 171:279 */         throw new EncryptedDocumentException(s, e);
/* 172:    */       }
/* 173:    */       catch (MarshalException e)
/* 174:    */       {
/* 175:281 */         String s = "error in unmarshalling the signature";
/* 176:282 */         SignatureInfo.LOG.log(7, new Object[] { s, e });
/* 177:283 */         throw new EncryptedDocumentException(s, e);
/* 178:    */       }
/* 179:    */       catch (XMLSignatureException e)
/* 180:    */       {
/* 181:285 */         String s = "error in validating the signature";
/* 182:286 */         SignatureInfo.LOG.log(7, new Object[] { s, e });
/* 183:287 */         throw new EncryptedDocumentException(s, e);
/* 184:    */       }
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   public SignatureInfo()
/* 189:    */   {
/* 190:296 */     initXmlProvider();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public SignatureConfig getSignatureConfig()
/* 194:    */   {
/* 195:303 */     return this.signatureConfig;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setSignatureConfig(SignatureConfig signatureConfig)
/* 199:    */   {
/* 200:310 */     this.signatureConfig = signatureConfig;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public boolean verifySignature()
/* 204:    */   {
/* 205:318 */     Iterator i$ = getSignatureParts().iterator();
/* 206:318 */     if (i$.hasNext())
/* 207:    */     {
/* 208:318 */       SignaturePart sp = (SignaturePart)i$.next();
/* 209:    */       
/* 210:320 */       return sp.validate();
/* 211:    */     }
/* 212:322 */     return false;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void confirmSignature()
/* 216:    */     throws XMLSignatureException, MarshalException
/* 217:    */   {
/* 218:332 */     Document document = DocumentHelper.createDocument();
/* 219:    */     
/* 220:    */ 
/* 221:335 */     DigestInfo digestInfo = preSign(document, null);
/* 222:    */     
/* 223:    */ 
/* 224:338 */     byte[] signatureValue = signDigest(digestInfo.digestValue);
/* 225:    */     
/* 226:    */ 
/* 227:341 */     postSign(document, signatureValue);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public byte[] signDigest(byte[] digest)
/* 231:    */   {
/* 232:352 */     Cipher cipher = CryptoFunctions.getCipher(this.signatureConfig.getKey(), CipherAlgorithm.rsa, ChainingMode.ecb, null, 1, "PKCS1Padding");
/* 233:    */     try
/* 234:    */     {
/* 235:356 */       ByteArrayOutputStream digestInfoValueBuf = new ByteArrayOutputStream();
/* 236:357 */       digestInfoValueBuf.write(this.signatureConfig.getHashMagic());
/* 237:358 */       digestInfoValueBuf.write(digest);
/* 238:359 */       byte[] digestInfoValue = digestInfoValueBuf.toByteArray();
/* 239:360 */       return cipher.doFinal(digestInfoValue);
/* 240:    */     }
/* 241:    */     catch (Exception e)
/* 242:    */     {
/* 243:363 */       throw new EncryptedDocumentException(e);
/* 244:    */     }
/* 245:    */   }
/* 246:    */   
/* 247:    */   public Iterable<SignaturePart> getSignatureParts()
/* 248:    */   {
/* 249:372 */     this.signatureConfig.init(true);
/* 250:373 */     new Iterable()
/* 251:    */     {
/* 252:    */       public Iterator<SignaturePart> iterator()
/* 253:    */       {
/* 254:375 */         new Iterator()
/* 255:    */         {
/* 256:376 */           OPCPackage pkg = SignatureInfo.this.signatureConfig.getOpcPackage();
/* 257:377 */           Iterator<PackageRelationship> sigOrigRels = this.pkg.getRelationshipsByType("http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/origin").iterator();
/* 258:379 */           Iterator<PackageRelationship> sigRels = null;
/* 259:380 */           PackagePart sigPart = null;
/* 260:    */           
/* 261:    */           public boolean hasNext()
/* 262:    */           {
/* 263:383 */             while ((this.sigRels == null) || (!this.sigRels.hasNext()))
/* 264:    */             {
/* 265:384 */               if (!this.sigOrigRels.hasNext()) {
/* 266:384 */                 return false;
/* 267:    */               }
/* 268:385 */               this.sigPart = this.pkg.getPart((PackageRelationship)this.sigOrigRels.next());
/* 269:386 */               SignatureInfo.LOG.log(1, new Object[] { "Digital Signature Origin part", this.sigPart });
/* 270:    */               try
/* 271:    */               {
/* 272:388 */                 this.sigRels = this.sigPart.getRelationshipsByType("http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/signature").iterator();
/* 273:    */               }
/* 274:    */               catch (InvalidFormatException e)
/* 275:    */               {
/* 276:390 */                 SignatureInfo.LOG.log(5, new Object[] { "Reference to signature is invalid.", e });
/* 277:    */               }
/* 278:    */             }
/* 279:393 */             return true;
/* 280:    */           }
/* 281:    */           
/* 282:    */           public SignaturePart next()
/* 283:    */           {
/* 284:397 */             PackagePart sigRelPart = null;
/* 285:    */             do
/* 286:    */             {
/* 287:    */               try
/* 288:    */               {
/* 289:400 */                 if (!hasNext()) {
/* 290:400 */                   throw new NoSuchElementException();
/* 291:    */                 }
/* 292:401 */                 sigRelPart = this.sigPart.getRelatedPart((PackageRelationship)this.sigRels.next());
/* 293:402 */                 SignatureInfo.LOG.log(1, new Object[] { "XML Signature part", sigRelPart });
/* 294:    */               }
/* 295:    */               catch (InvalidFormatException e)
/* 296:    */               {
/* 297:404 */                 SignatureInfo.LOG.log(5, new Object[] { "Reference to signature is invalid.", e });
/* 298:    */               }
/* 299:406 */             } while (this.sigPart == null);
/* 300:407 */             return new SignaturePart(SignatureInfo.this, sigRelPart, null);
/* 301:    */           }
/* 302:    */           
/* 303:    */           public void remove()
/* 304:    */           {
/* 305:411 */             throw new UnsupportedOperationException();
/* 306:    */           }
/* 307:    */         };
/* 308:    */       }
/* 309:    */     };
/* 310:    */   }
/* 311:    */   
/* 312:    */   protected static synchronized void initXmlProvider()
/* 313:    */   {
/* 314:422 */     if (isInitialized) {
/* 315:422 */       return;
/* 316:    */     }
/* 317:423 */     isInitialized = true;
/* 318:    */     try
/* 319:    */     {
/* 320:426 */       Init.init();
/* 321:427 */       RelationshipTransformService.registerDsigProvider();
/* 322:428 */       CryptoFunctions.registerBouncyCastle();
/* 323:    */     }
/* 324:    */     catch (Exception e)
/* 325:    */     {
/* 326:430 */       throw new RuntimeException("Xml & BouncyCastle-Provider initialization failed", e);
/* 327:    */     }
/* 328:    */   }
/* 329:    */   
/* 330:    */   public DigestInfo preSign(Document document, List<DigestInfo> digestInfos)
/* 331:    */     throws XMLSignatureException, MarshalException
/* 332:    */   {
/* 333:441 */     this.signatureConfig.init(false);
/* 334:    */     
/* 335:    */ 
/* 336:    */ 
/* 337:445 */     EventTarget target = (EventTarget)document;
/* 338:446 */     EventListener creationListener = this.signatureConfig.getSignatureMarshalListener();
/* 339:447 */     if (creationListener != null)
/* 340:    */     {
/* 341:448 */       if ((creationListener instanceof SignatureMarshalListener)) {
/* 342:449 */         ((SignatureMarshalListener)creationListener).setEventTarget(target);
/* 343:    */       }
/* 344:451 */       SignatureMarshalListener.setListener(target, creationListener, true);
/* 345:    */     }
/* 346:457 */     XMLSignContext xmlSignContext = new DOMSignContext(this.signatureConfig.getKey(), document);
/* 347:458 */     URIDereferencer uriDereferencer = this.signatureConfig.getUriDereferencer();
/* 348:459 */     if (null != uriDereferencer) {
/* 349:460 */       xmlSignContext.setURIDereferencer(uriDereferencer);
/* 350:    */     }
/* 351:463 */     for (Entry<String, String> me : this.signatureConfig.getNamespacePrefixes().entrySet()) {
/* 352:464 */       xmlSignContext.putNamespacePrefix((String)me.getKey(), (String)me.getValue());
/* 353:    */     }
/* 354:466 */     xmlSignContext.setDefaultNamespacePrefix("");
/* 355:    */     
/* 356:    */ 
/* 357:469 */     brokenJvmWorkaround(xmlSignContext);
/* 358:    */     
/* 359:471 */     XMLSignatureFactory signatureFactory = this.signatureConfig.getSignatureFactory();
/* 360:    */     
/* 361:    */ 
/* 362:    */ 
/* 363:    */ 
/* 364:476 */     List<Reference> references = new ArrayList();
/* 365:477 */     for (DigestInfo digestInfo : safe(digestInfos))
/* 366:    */     {
/* 367:478 */       byte[] documentDigestValue = digestInfo.digestValue;
/* 368:    */       
/* 369:480 */       String uri = new File(digestInfo.description).getName();
/* 370:481 */       Reference reference = SignatureFacet.newReference(uri, null, null, null, documentDigestValue, this.signatureConfig);
/* 371:    */       
/* 372:483 */       references.add(reference);
/* 373:    */     }
/* 374:489 */     List<XMLObject> objects = new ArrayList();
/* 375:490 */     for (SignatureFacet signatureFacet : this.signatureConfig.getSignatureFacets())
/* 376:    */     {
/* 377:491 */       LOG.log(1, new Object[] { "invoking signature facet: " + signatureFacet.getClass().getSimpleName() });
/* 378:492 */       signatureFacet.preSign(document, references, objects);
/* 379:    */     }
/* 380:    */     SignedInfo signedInfo;
/* 381:    */     try
/* 382:    */     {
/* 383:500 */       SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(this.signatureConfig.getSignatureMethodUri(), null);
/* 384:    */       
/* 385:502 */       CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(this.signatureConfig.getCanonicalizationMethod(), (C14NMethodParameterSpec)null);
/* 386:    */       
/* 387:    */ 
/* 388:505 */       signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, references);
/* 389:    */     }
/* 390:    */     catch (GeneralSecurityException e)
/* 391:    */     {
/* 392:508 */       throw new XMLSignatureException(e);
/* 393:    */     }
/* 394:514 */     String signatureValueId = this.signatureConfig.getPackageSignatureId() + "-signature-value";
/* 395:515 */     XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, null, objects, this.signatureConfig.getPackageSignatureId(), signatureValueId);
/* 396:    */     
/* 397:    */ 
/* 398:    */ 
/* 399:    */ 
/* 400:    */ 
/* 401:    */ 
/* 402:522 */     xmlSignature.sign(xmlSignContext);
/* 403:527 */     for (XMLObject object : objects)
/* 404:    */     {
/* 405:528 */       LOG.log(1, new Object[] { "object java type: " + object.getClass().getName() });
/* 406:529 */       List<XMLStructure> objectContentList = object.getContent();
/* 407:530 */       for (XMLStructure objectContent : objectContentList)
/* 408:    */       {
/* 409:531 */         LOG.log(1, new Object[] { "object content java type: " + objectContent.getClass().getName() });
/* 410:532 */         if ((objectContent instanceof Manifest))
/* 411:    */         {
/* 412:533 */           Manifest manifest = (Manifest)objectContent;
/* 413:534 */           List<Reference> manifestReferences = manifest.getReferences();
/* 414:535 */           for (Reference manifestReference : manifestReferences) {
/* 415:536 */             if (manifestReference.getDigestValue() == null)
/* 416:    */             {
/* 417:538 */               DOMReference manifestDOMReference = (DOMReference)manifestReference;
/* 418:539 */               manifestDOMReference.digest(xmlSignContext);
/* 419:    */             }
/* 420:    */           }
/* 421:    */         }
/* 422:    */       }
/* 423:    */     }
/* 424:547 */     List<Reference> signedInfoReferences = signedInfo.getReferences();
/* 425:548 */     for (Reference signedInfoReference : signedInfoReferences)
/* 426:    */     {
/* 427:549 */       DOMReference domReference = (DOMReference)signedInfoReference;
/* 428:552 */       if (domReference.getDigestValue() == null) {
/* 429:554 */         domReference.digest(xmlSignContext);
/* 430:    */       }
/* 431:    */     }
/* 432:560 */     DOMSignedInfo domSignedInfo = (DOMSignedInfo)signedInfo;
/* 433:561 */     ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
/* 434:562 */     domSignedInfo.canonicalize(xmlSignContext, dataStream);
/* 435:563 */     byte[] octets = dataStream.toByteArray();
/* 436:    */     
/* 437:    */ 
/* 438:    */ 
/* 439:    */ 
/* 440:    */ 
/* 441:    */ 
/* 442:570 */     MessageDigest md = CryptoFunctions.getMessageDigest(this.signatureConfig.getDigestAlgo());
/* 443:571 */     byte[] digestValue = md.digest(octets);
/* 444:    */     
/* 445:    */ 
/* 446:574 */     String description = this.signatureConfig.getSignatureDescription();
/* 447:575 */     return new DigestInfo(digestValue, this.signatureConfig.getDigestAlgo(), description);
/* 448:    */   }
/* 449:    */   
/* 450:    */   public void postSign(Document document, byte[] signatureValue)
/* 451:    */     throws MarshalException
/* 452:    */   {
/* 453:584 */     LOG.log(1, new Object[] { "postSign" });
/* 454:    */     
/* 455:    */ 
/* 456:    */ 
/* 457:    */ 
/* 458:589 */     String signatureId = this.signatureConfig.getPackageSignatureId();
/* 459:590 */     if (!signatureId.equals(document.getDocumentElement().getAttribute("Id"))) {
/* 460:591 */       throw new RuntimeException("ds:Signature not found for @Id: " + signatureId);
/* 461:    */     }
/* 462:597 */     NodeList sigValNl = document.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
/* 463:598 */     if (sigValNl.getLength() != 1) {
/* 464:599 */       throw new RuntimeException("preSign has to be called before postSign");
/* 465:    */     }
/* 466:601 */     sigValNl.item(0).setTextContent(Base64.encode(signatureValue));
/* 467:606 */     for (SignatureFacet signatureFacet : this.signatureConfig.getSignatureFacets()) {
/* 468:607 */       signatureFacet.postSign(document);
/* 469:    */     }
/* 470:610 */     writeDocument(document);
/* 471:    */   }
/* 472:    */   
/* 473:    */   protected void writeDocument(Document document)
/* 474:    */     throws MarshalException
/* 475:    */   {
/* 476:620 */     XmlOptions xo = new XmlOptions();
/* 477:621 */     Map<String, String> namespaceMap = new HashMap();
/* 478:622 */     for (Entry<String, String> entry : this.signatureConfig.getNamespacePrefixes().entrySet()) {
/* 479:623 */       namespaceMap.put(entry.getValue(), entry.getKey());
/* 480:    */     }
/* 481:625 */     xo.setSaveSuggestedPrefixes(namespaceMap);
/* 482:626 */     xo.setUseDefaultNamespace();
/* 483:    */     
/* 484:628 */     LOG.log(1, new Object[] { "output signed Office OpenXML document" });
/* 485:    */     
/* 486:    */ 
/* 487:    */ 
/* 488:    */ 
/* 489:    */ 
/* 490:634 */     OPCPackage pkg = this.signatureConfig.getOpcPackage();
/* 491:    */     PackagePartName sigPartName;
/* 492:    */     PackagePartName sigsPartName;
/* 493:    */     try
/* 494:    */     {
/* 495:639 */       sigPartName = PackagingURIHelper.createPartName("/_xmlsignatures/sig1.xml");
/* 496:    */       
/* 497:641 */       sigsPartName = PackagingURIHelper.createPartName("/_xmlsignatures/origin.sigs");
/* 498:    */     }
/* 499:    */     catch (InvalidFormatException e)
/* 500:    */     {
/* 501:643 */       throw new MarshalException(e);
/* 502:    */     }
/* 503:646 */     PackagePart sigPart = pkg.getPart(sigPartName);
/* 504:647 */     if (sigPart == null) {
/* 505:648 */       sigPart = pkg.createPart(sigPartName, "application/vnd.openxmlformats-package.digital-signature-xmlsignature+xml");
/* 506:    */     }
/* 507:    */     try
/* 508:    */     {
/* 509:652 */       OutputStream os = sigPart.getOutputStream();
/* 510:653 */       SignatureDocument sigDoc = SignatureDocument.Factory.parse(document, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 511:654 */       sigDoc.save(os, xo);
/* 512:655 */       os.close();
/* 513:    */     }
/* 514:    */     catch (Exception e)
/* 515:    */     {
/* 516:657 */       throw new MarshalException("Unable to write signature document", e);
/* 517:    */     }
/* 518:660 */     PackagePart sigsPart = pkg.getPart(sigsPartName);
/* 519:661 */     if (sigsPart == null) {
/* 520:663 */       sigsPart = pkg.createPart(sigsPartName, "application/vnd.openxmlformats-package.digital-signature-origin");
/* 521:    */     }
/* 522:666 */     PackageRelationshipCollection relCol = pkg.getRelationshipsByType("http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/origin");
/* 523:667 */     for (PackageRelationship pr : relCol) {
/* 524:668 */       pkg.removeRelationship(pr.getId());
/* 525:    */     }
/* 526:670 */     pkg.addRelationship(sigsPartName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/origin");
/* 527:    */     
/* 528:672 */     sigsPart.addRelationship(sigPartName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/package/2006/relationships/digital-signature/signature");
/* 529:    */   }
/* 530:    */   
/* 531:    */   private static <T> List<T> safe(List<T> other)
/* 532:    */   {
/* 533:682 */     List<T> emptyList = Collections.emptyList();
/* 534:683 */     return other == null ? emptyList : other;
/* 535:    */   }
/* 536:    */   
/* 537:    */   private void brokenJvmWorkaround(XMLSignContext context)
/* 538:    */   {
/* 539:688 */     Provider bcProv = Security.getProvider("BC");
/* 540:689 */     if (bcProv != null) {
/* 541:690 */       context.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", bcProv);
/* 542:    */     }
/* 543:    */   }
/* 544:    */   
/* 545:    */   private void brokenJvmWorkaround(XMLValidateContext context)
/* 546:    */   {
/* 547:696 */     Provider bcProv = Security.getProvider("BC");
/* 548:697 */     if (bcProv != null) {
/* 549:698 */       context.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", bcProv);
/* 550:    */     }
/* 551:    */   }
/* 552:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.SignatureInfo

 * JD-Core Version:    0.7.0.1

 */