/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.Date;
/*   7:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   9:    */ import org.apache.poi.openxml4j.opc.ContentTypes;
/*  10:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  15:    */ import org.apache.poi.openxml4j.opc.StreamHelper;
/*  16:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  17:    */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*  18:    */ import org.apache.poi.openxml4j.util.Nullable;
/*  19:    */ import org.apache.xmlbeans.XmlException;
/*  20:    */ import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
/*  21:    */ 
/*  22:    */ public class POIXMLProperties
/*  23:    */ {
/*  24:    */   private OPCPackage pkg;
/*  25:    */   private CoreProperties core;
/*  26:    */   private ExtendedProperties ext;
/*  27:    */   private CustomProperties cust;
/*  28:    */   private PackagePart extPart;
/*  29:    */   private PackagePart custPart;
/*  30: 60 */   private static final org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument NEW_EXT_INSTANCE = ;
/*  31:    */   private static final org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument NEW_CUST_INSTANCE;
/*  32:    */   
/*  33:    */   static
/*  34:    */   {
/*  35: 61 */     NEW_EXT_INSTANCE.addNewProperties();
/*  36:    */     
/*  37: 63 */     NEW_CUST_INSTANCE = org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument.Factory.newInstance();
/*  38: 64 */     NEW_CUST_INSTANCE.addNewProperties();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public POIXMLProperties(OPCPackage docPackage)
/*  42:    */     throws IOException, OpenXML4JException, XmlException
/*  43:    */   {
/*  44: 68 */     this.pkg = docPackage;
/*  45:    */     
/*  46:    */ 
/*  47: 71 */     this.core = new CoreProperties((PackagePropertiesPart)this.pkg.getPackageProperties(), null);
/*  48:    */     
/*  49:    */ 
/*  50: 74 */     PackageRelationshipCollection extRel = this.pkg.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties");
/*  51: 76 */     if (extRel.size() == 1)
/*  52:    */     {
/*  53: 77 */       this.extPart = this.pkg.getPart(extRel.getRelationship(0));
/*  54: 78 */       org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument props = org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument.Factory.parse(this.extPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  55:    */       
/*  56:    */ 
/*  57: 81 */       this.ext = new ExtendedProperties(props, null);
/*  58:    */     }
/*  59:    */     else
/*  60:    */     {
/*  61: 83 */       this.extPart = null;
/*  62: 84 */       this.ext = new ExtendedProperties((org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument)NEW_EXT_INSTANCE.copy(), null);
/*  63:    */     }
/*  64: 88 */     PackageRelationshipCollection custRel = this.pkg.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties");
/*  65: 90 */     if (custRel.size() == 1)
/*  66:    */     {
/*  67: 91 */       this.custPart = this.pkg.getPart(custRel.getRelationship(0));
/*  68: 92 */       org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument props = org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument.Factory.parse(this.custPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  69:    */       
/*  70:    */ 
/*  71: 95 */       this.cust = new CustomProperties(props, null);
/*  72:    */     }
/*  73:    */     else
/*  74:    */     {
/*  75: 97 */       this.custPart = null;
/*  76: 98 */       this.cust = new CustomProperties((org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument)NEW_CUST_INSTANCE.copy(), null);
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public CoreProperties getCoreProperties()
/*  81:    */   {
/*  82:108 */     return this.core;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public ExtendedProperties getExtendedProperties()
/*  86:    */   {
/*  87:117 */     return this.ext;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public CustomProperties getCustomProperties()
/*  91:    */   {
/*  92:126 */     return this.cust;
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected PackagePart getThumbnailPart()
/*  96:    */   {
/*  97:136 */     PackageRelationshipCollection rels = this.pkg.getRelationshipsByType("http://schemas.openxmlformats.org/package/2006/relationships/metadata/thumbnail");
/*  98:138 */     if (rels.size() == 1) {
/*  99:139 */       return this.pkg.getPart(rels.getRelationship(0));
/* 100:    */     }
/* 101:141 */     return null;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getThumbnailFilename()
/* 105:    */   {
/* 106:151 */     PackagePart tPart = getThumbnailPart();
/* 107:152 */     if (tPart == null) {
/* 108:152 */       return null;
/* 109:    */     }
/* 110:153 */     String name = tPart.getPartName().getName();
/* 111:154 */     return name.substring(name.lastIndexOf('/'));
/* 112:    */   }
/* 113:    */   
/* 114:    */   public InputStream getThumbnailImage()
/* 115:    */     throws IOException
/* 116:    */   {
/* 117:164 */     PackagePart tPart = getThumbnailPart();
/* 118:165 */     if (tPart == null) {
/* 119:165 */       return null;
/* 120:    */     }
/* 121:166 */     return tPart.getInputStream();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setThumbnail(String filename, InputStream imageData)
/* 125:    */     throws IOException
/* 126:    */   {
/* 127:178 */     PackagePart tPart = getThumbnailPart();
/* 128:179 */     if (tPart == null)
/* 129:    */     {
/* 130:181 */       this.pkg.addThumbnail(filename, imageData);
/* 131:    */     }
/* 132:    */     else
/* 133:    */     {
/* 134:184 */       String newType = ContentTypes.getContentTypeFromFileExtension(filename);
/* 135:185 */       if (!newType.equals(tPart.getContentType())) {
/* 136:186 */         throw new IllegalArgumentException("Can't set a Thumbnail of type " + newType + " when existing one is of a different type " + tPart.getContentType());
/* 137:    */       }
/* 138:190 */       StreamHelper.copyStream(imageData, tPart.getOutputStream());
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void commit()
/* 143:    */     throws IOException
/* 144:    */   {
/* 145:202 */     if ((this.extPart == null) && (!NEW_EXT_INSTANCE.toString().equals(this.ext.props.toString()))) {
/* 146:    */       try
/* 147:    */       {
/* 148:204 */         PackagePartName prtname = PackagingURIHelper.createPartName("/docProps/app.xml");
/* 149:205 */         this.pkg.addRelationship(prtname, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties");
/* 150:206 */         this.extPart = this.pkg.createPart(prtname, "application/vnd.openxmlformats-officedocument.extended-properties+xml");
/* 151:    */       }
/* 152:    */       catch (InvalidFormatException e)
/* 153:    */       {
/* 154:208 */         throw new POIXMLException(e);
/* 155:    */       }
/* 156:    */     }
/* 157:211 */     if ((this.custPart == null) && (!NEW_CUST_INSTANCE.toString().equals(this.cust.props.toString()))) {
/* 158:    */       try
/* 159:    */       {
/* 160:213 */         PackagePartName prtname = PackagingURIHelper.createPartName("/docProps/custom.xml");
/* 161:214 */         this.pkg.addRelationship(prtname, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties");
/* 162:215 */         this.custPart = this.pkg.createPart(prtname, "application/vnd.openxmlformats-officedocument.custom-properties+xml");
/* 163:    */       }
/* 164:    */       catch (InvalidFormatException e)
/* 165:    */       {
/* 166:217 */         throw new POIXMLException(e);
/* 167:    */       }
/* 168:    */     }
/* 169:220 */     if (this.extPart != null)
/* 170:    */     {
/* 171:221 */       OutputStream out = this.extPart.getOutputStream();
/* 172:222 */       if (this.extPart.getSize() > 0L) {
/* 173:223 */         this.extPart.clear();
/* 174:    */       }
/* 175:225 */       this.ext.props.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 176:226 */       out.close();
/* 177:    */     }
/* 178:228 */     if (this.custPart != null)
/* 179:    */     {
/* 180:229 */       OutputStream out = this.custPart.getOutputStream();
/* 181:230 */       this.cust.props.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 182:231 */       out.close();
/* 183:    */     }
/* 184:    */   }
/* 185:    */   
/* 186:    */   public static class CoreProperties
/* 187:    */   {
/* 188:    */     private PackagePropertiesPart part;
/* 189:    */     
/* 190:    */     private CoreProperties(PackagePropertiesPart part)
/* 191:    */     {
/* 192:241 */       this.part = part;
/* 193:    */     }
/* 194:    */     
/* 195:    */     public String getCategory()
/* 196:    */     {
/* 197:245 */       return (String)this.part.getCategoryProperty().getValue();
/* 198:    */     }
/* 199:    */     
/* 200:    */     public void setCategory(String category)
/* 201:    */     {
/* 202:248 */       this.part.setCategoryProperty(category);
/* 203:    */     }
/* 204:    */     
/* 205:    */     public String getContentStatus()
/* 206:    */     {
/* 207:251 */       return (String)this.part.getContentStatusProperty().getValue();
/* 208:    */     }
/* 209:    */     
/* 210:    */     public void setContentStatus(String contentStatus)
/* 211:    */     {
/* 212:254 */       this.part.setContentStatusProperty(contentStatus);
/* 213:    */     }
/* 214:    */     
/* 215:    */     public String getContentType()
/* 216:    */     {
/* 217:257 */       return (String)this.part.getContentTypeProperty().getValue();
/* 218:    */     }
/* 219:    */     
/* 220:    */     public void setContentType(String contentType)
/* 221:    */     {
/* 222:260 */       this.part.setContentTypeProperty(contentType);
/* 223:    */     }
/* 224:    */     
/* 225:    */     public Date getCreated()
/* 226:    */     {
/* 227:263 */       return (Date)this.part.getCreatedProperty().getValue();
/* 228:    */     }
/* 229:    */     
/* 230:    */     public void setCreated(Nullable<Date> date)
/* 231:    */     {
/* 232:266 */       this.part.setCreatedProperty(date);
/* 233:    */     }
/* 234:    */     
/* 235:    */     public void setCreated(String date)
/* 236:    */     {
/* 237:269 */       this.part.setCreatedProperty(date);
/* 238:    */     }
/* 239:    */     
/* 240:    */     public String getCreator()
/* 241:    */     {
/* 242:272 */       return (String)this.part.getCreatorProperty().getValue();
/* 243:    */     }
/* 244:    */     
/* 245:    */     public void setCreator(String creator)
/* 246:    */     {
/* 247:275 */       this.part.setCreatorProperty(creator);
/* 248:    */     }
/* 249:    */     
/* 250:    */     public String getDescription()
/* 251:    */     {
/* 252:278 */       return (String)this.part.getDescriptionProperty().getValue();
/* 253:    */     }
/* 254:    */     
/* 255:    */     public void setDescription(String description)
/* 256:    */     {
/* 257:281 */       this.part.setDescriptionProperty(description);
/* 258:    */     }
/* 259:    */     
/* 260:    */     public String getIdentifier()
/* 261:    */     {
/* 262:284 */       return (String)this.part.getIdentifierProperty().getValue();
/* 263:    */     }
/* 264:    */     
/* 265:    */     public void setIdentifier(String identifier)
/* 266:    */     {
/* 267:287 */       this.part.setIdentifierProperty(identifier);
/* 268:    */     }
/* 269:    */     
/* 270:    */     public String getKeywords()
/* 271:    */     {
/* 272:290 */       return (String)this.part.getKeywordsProperty().getValue();
/* 273:    */     }
/* 274:    */     
/* 275:    */     public void setKeywords(String keywords)
/* 276:    */     {
/* 277:293 */       this.part.setKeywordsProperty(keywords);
/* 278:    */     }
/* 279:    */     
/* 280:    */     public Date getLastPrinted()
/* 281:    */     {
/* 282:296 */       return (Date)this.part.getLastPrintedProperty().getValue();
/* 283:    */     }
/* 284:    */     
/* 285:    */     public void setLastPrinted(Nullable<Date> date)
/* 286:    */     {
/* 287:299 */       this.part.setLastPrintedProperty(date);
/* 288:    */     }
/* 289:    */     
/* 290:    */     public void setLastPrinted(String date)
/* 291:    */     {
/* 292:302 */       this.part.setLastPrintedProperty(date);
/* 293:    */     }
/* 294:    */     
/* 295:    */     public String getLastModifiedByUser()
/* 296:    */     {
/* 297:306 */       return (String)this.part.getLastModifiedByProperty().getValue();
/* 298:    */     }
/* 299:    */     
/* 300:    */     public void setLastModifiedByUser(String user)
/* 301:    */     {
/* 302:310 */       this.part.setLastModifiedByProperty(user);
/* 303:    */     }
/* 304:    */     
/* 305:    */     public Date getModified()
/* 306:    */     {
/* 307:313 */       return (Date)this.part.getModifiedProperty().getValue();
/* 308:    */     }
/* 309:    */     
/* 310:    */     public void setModified(Nullable<Date> date)
/* 311:    */     {
/* 312:316 */       this.part.setModifiedProperty(date);
/* 313:    */     }
/* 314:    */     
/* 315:    */     public void setModified(String date)
/* 316:    */     {
/* 317:319 */       this.part.setModifiedProperty(date);
/* 318:    */     }
/* 319:    */     
/* 320:    */     public String getSubject()
/* 321:    */     {
/* 322:322 */       return (String)this.part.getSubjectProperty().getValue();
/* 323:    */     }
/* 324:    */     
/* 325:    */     public void setSubjectProperty(String subject)
/* 326:    */     {
/* 327:325 */       this.part.setSubjectProperty(subject);
/* 328:    */     }
/* 329:    */     
/* 330:    */     public void setTitle(String title)
/* 331:    */     {
/* 332:328 */       this.part.setTitleProperty(title);
/* 333:    */     }
/* 334:    */     
/* 335:    */     public String getTitle()
/* 336:    */     {
/* 337:331 */       return (String)this.part.getTitleProperty().getValue();
/* 338:    */     }
/* 339:    */     
/* 340:    */     public String getRevision()
/* 341:    */     {
/* 342:334 */       return (String)this.part.getRevisionProperty().getValue();
/* 343:    */     }
/* 344:    */     
/* 345:    */     public void setRevision(String revision)
/* 346:    */     {
/* 347:    */       try
/* 348:    */       {
/* 349:338 */         Long.valueOf(revision);
/* 350:339 */         this.part.setRevisionProperty(revision);
/* 351:    */       }
/* 352:    */       catch (NumberFormatException e) {}
/* 353:    */     }
/* 354:    */     
/* 355:    */     public PackagePropertiesPart getUnderlyingProperties()
/* 356:    */     {
/* 357:345 */       return this.part;
/* 358:    */     }
/* 359:    */   }
/* 360:    */   
/* 361:    */   public static class ExtendedProperties
/* 362:    */   {
/* 363:    */     private org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument props;
/* 364:    */     
/* 365:    */     private ExtendedProperties(org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.PropertiesDocument props)
/* 366:    */     {
/* 367:355 */       this.props = props;
/* 368:    */     }
/* 369:    */     
/* 370:    */     public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties getUnderlyingProperties()
/* 371:    */     {
/* 372:359 */       return this.props.getProperties();
/* 373:    */     }
/* 374:    */     
/* 375:    */     public String getTemplate()
/* 376:    */     {
/* 377:363 */       if (this.props.getProperties().isSetTemplate()) {
/* 378:364 */         return this.props.getProperties().getTemplate();
/* 379:    */       }
/* 380:366 */       return null;
/* 381:    */     }
/* 382:    */     
/* 383:    */     public String getManager()
/* 384:    */     {
/* 385:369 */       if (this.props.getProperties().isSetManager()) {
/* 386:370 */         return this.props.getProperties().getManager();
/* 387:    */       }
/* 388:372 */       return null;
/* 389:    */     }
/* 390:    */     
/* 391:    */     public String getCompany()
/* 392:    */     {
/* 393:375 */       if (this.props.getProperties().isSetCompany()) {
/* 394:376 */         return this.props.getProperties().getCompany();
/* 395:    */       }
/* 396:378 */       return null;
/* 397:    */     }
/* 398:    */     
/* 399:    */     public String getPresentationFormat()
/* 400:    */     {
/* 401:381 */       if (this.props.getProperties().isSetPresentationFormat()) {
/* 402:382 */         return this.props.getProperties().getPresentationFormat();
/* 403:    */       }
/* 404:384 */       return null;
/* 405:    */     }
/* 406:    */     
/* 407:    */     public String getApplication()
/* 408:    */     {
/* 409:387 */       if (this.props.getProperties().isSetApplication()) {
/* 410:388 */         return this.props.getProperties().getApplication();
/* 411:    */       }
/* 412:390 */       return null;
/* 413:    */     }
/* 414:    */     
/* 415:    */     public String getAppVersion()
/* 416:    */     {
/* 417:393 */       if (this.props.getProperties().isSetAppVersion()) {
/* 418:394 */         return this.props.getProperties().getAppVersion();
/* 419:    */       }
/* 420:396 */       return null;
/* 421:    */     }
/* 422:    */     
/* 423:    */     public int getPages()
/* 424:    */     {
/* 425:400 */       if (this.props.getProperties().isSetPages()) {
/* 426:401 */         return this.props.getProperties().getPages();
/* 427:    */       }
/* 428:403 */       return -1;
/* 429:    */     }
/* 430:    */     
/* 431:    */     public int getWords()
/* 432:    */     {
/* 433:406 */       if (this.props.getProperties().isSetWords()) {
/* 434:407 */         return this.props.getProperties().getWords();
/* 435:    */       }
/* 436:409 */       return -1;
/* 437:    */     }
/* 438:    */     
/* 439:    */     public int getCharacters()
/* 440:    */     {
/* 441:412 */       if (this.props.getProperties().isSetCharacters()) {
/* 442:413 */         return this.props.getProperties().getCharacters();
/* 443:    */       }
/* 444:415 */       return -1;
/* 445:    */     }
/* 446:    */     
/* 447:    */     public int getCharactersWithSpaces()
/* 448:    */     {
/* 449:418 */       if (this.props.getProperties().isSetCharactersWithSpaces()) {
/* 450:419 */         return this.props.getProperties().getCharactersWithSpaces();
/* 451:    */       }
/* 452:421 */       return -1;
/* 453:    */     }
/* 454:    */     
/* 455:    */     public int getLines()
/* 456:    */     {
/* 457:424 */       if (this.props.getProperties().isSetLines()) {
/* 458:425 */         return this.props.getProperties().getLines();
/* 459:    */       }
/* 460:427 */       return -1;
/* 461:    */     }
/* 462:    */     
/* 463:    */     public int getParagraphs()
/* 464:    */     {
/* 465:430 */       if (this.props.getProperties().isSetParagraphs()) {
/* 466:431 */         return this.props.getProperties().getParagraphs();
/* 467:    */       }
/* 468:433 */       return -1;
/* 469:    */     }
/* 470:    */     
/* 471:    */     public int getSlides()
/* 472:    */     {
/* 473:436 */       if (this.props.getProperties().isSetSlides()) {
/* 474:437 */         return this.props.getProperties().getSlides();
/* 475:    */       }
/* 476:439 */       return -1;
/* 477:    */     }
/* 478:    */     
/* 479:    */     public int getNotes()
/* 480:    */     {
/* 481:442 */       if (this.props.getProperties().isSetNotes()) {
/* 482:443 */         return this.props.getProperties().getNotes();
/* 483:    */       }
/* 484:445 */       return -1;
/* 485:    */     }
/* 486:    */     
/* 487:    */     public int getTotalTime()
/* 488:    */     {
/* 489:448 */       if (this.props.getProperties().isSetTotalTime()) {
/* 490:449 */         return this.props.getProperties().getTotalTime();
/* 491:    */       }
/* 492:451 */       return -1;
/* 493:    */     }
/* 494:    */     
/* 495:    */     public int getHiddenSlides()
/* 496:    */     {
/* 497:454 */       if (this.props.getProperties().isSetHiddenSlides()) {
/* 498:455 */         return this.props.getProperties().getHiddenSlides();
/* 499:    */       }
/* 500:457 */       return -1;
/* 501:    */     }
/* 502:    */     
/* 503:    */     public int getMMClips()
/* 504:    */     {
/* 505:460 */       if (this.props.getProperties().isSetMMClips()) {
/* 506:461 */         return this.props.getProperties().getMMClips();
/* 507:    */       }
/* 508:463 */       return -1;
/* 509:    */     }
/* 510:    */     
/* 511:    */     public String getHyperlinkBase()
/* 512:    */     {
/* 513:467 */       if (this.props.getProperties().isSetHyperlinkBase()) {
/* 514:468 */         return this.props.getProperties().getHyperlinkBase();
/* 515:    */       }
/* 516:470 */       return null;
/* 517:    */     }
/* 518:    */   }
/* 519:    */   
/* 520:    */   public static class CustomProperties
/* 521:    */   {
/* 522:    */     public static final String FORMAT_ID = "{D5CDD505-2E9C-101B-9397-08002B2CF9AE}";
/* 523:    */     private org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument props;
/* 524:    */     
/* 525:    */     private CustomProperties(org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument props)
/* 526:    */     {
/* 527:486 */       this.props = props;
/* 528:    */     }
/* 529:    */     
/* 530:    */     public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties getUnderlyingProperties()
/* 531:    */     {
/* 532:490 */       return this.props.getProperties();
/* 533:    */     }
/* 534:    */     
/* 535:    */     private CTProperty add(String name)
/* 536:    */     {
/* 537:500 */       if (contains(name)) {
/* 538:501 */         throw new IllegalArgumentException("A property with this name already exists in the custom properties");
/* 539:    */       }
/* 540:505 */       CTProperty p = this.props.getProperties().addNewProperty();
/* 541:506 */       int pid = nextPid();
/* 542:507 */       p.setPid(pid);
/* 543:508 */       p.setFmtid("{D5CDD505-2E9C-101B-9397-08002B2CF9AE}");
/* 544:509 */       p.setName(name);
/* 545:510 */       return p;
/* 546:    */     }
/* 547:    */     
/* 548:    */     public void addProperty(String name, String value)
/* 549:    */     {
/* 550:522 */       CTProperty p = add(name);
/* 551:523 */       p.setLpwstr(value);
/* 552:    */     }
/* 553:    */     
/* 554:    */     public void addProperty(String name, double value)
/* 555:    */     {
/* 556:535 */       CTProperty p = add(name);
/* 557:536 */       p.setR8(value);
/* 558:    */     }
/* 559:    */     
/* 560:    */     public void addProperty(String name, int value)
/* 561:    */     {
/* 562:548 */       CTProperty p = add(name);
/* 563:549 */       p.setI4(value);
/* 564:    */     }
/* 565:    */     
/* 566:    */     public void addProperty(String name, boolean value)
/* 567:    */     {
/* 568:561 */       CTProperty p = add(name);
/* 569:562 */       p.setBool(value);
/* 570:    */     }
/* 571:    */     
/* 572:    */     protected int nextPid()
/* 573:    */     {
/* 574:571 */       int propid = 1;
/* 575:572 */       for (CTProperty p : this.props.getProperties().getPropertyArray()) {
/* 576:573 */         if (p.getPid() > propid) {
/* 577:573 */           propid = p.getPid();
/* 578:    */         }
/* 579:    */       }
/* 580:575 */       return propid + 1;
/* 581:    */     }
/* 582:    */     
/* 583:    */     public boolean contains(String name)
/* 584:    */     {
/* 585:585 */       for (CTProperty p : this.props.getProperties().getPropertyArray()) {
/* 586:586 */         if (p.getName().equals(name)) {
/* 587:586 */           return true;
/* 588:    */         }
/* 589:    */       }
/* 590:588 */       return false;
/* 591:    */     }
/* 592:    */     
/* 593:    */     public CTProperty getProperty(String name)
/* 594:    */     {
/* 595:603 */       for (CTProperty p : this.props.getProperties().getPropertyArray()) {
/* 596:604 */         if (p.getName().equals(name)) {
/* 597:605 */           return p;
/* 598:    */         }
/* 599:    */       }
/* 600:608 */       return null;
/* 601:    */     }
/* 602:    */   }
/* 603:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLProperties
 * JD-Core Version:    0.7.0.1
 */