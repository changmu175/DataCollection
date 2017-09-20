/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.net.URI;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Collection;
/*  12:    */ import java.util.Enumeration;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.zip.ZipEntry;
/*  15:    */ import java.util.zip.ZipFile;
/*  16:    */ import java.util.zip.ZipOutputStream;
/*  17:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  18:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  19:    */ import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
/*  20:    */ import org.apache.poi.openxml4j.exceptions.ODFNotOfficeXmlFileException;
/*  21:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  22:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JRuntimeException;
/*  23:    */ import org.apache.poi.openxml4j.opc.internal.ContentTypeManager;
/*  24:    */ import org.apache.poi.openxml4j.opc.internal.FileHelper;
/*  25:    */ import org.apache.poi.openxml4j.opc.internal.MemoryPackagePart;
/*  26:    */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*  27:    */ import org.apache.poi.openxml4j.opc.internal.PartMarshaller;
/*  28:    */ import org.apache.poi.openxml4j.opc.internal.ZipContentTypeManager;
/*  29:    */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/*  30:    */ import org.apache.poi.openxml4j.opc.internal.marshallers.ZipPartMarshaller;
/*  31:    */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  32:    */ import org.apache.poi.openxml4j.util.ZipFileZipEntrySource;
/*  33:    */ import org.apache.poi.openxml4j.util.ZipInputStreamZipEntrySource;
/*  34:    */ import org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream;
/*  35:    */ import org.apache.poi.util.IOUtils;
/*  36:    */ import org.apache.poi.util.POILogFactory;
/*  37:    */ import org.apache.poi.util.POILogger;
/*  38:    */ import org.apache.poi.util.TempFile;
/*  39:    */ 
/*  40:    */ public final class ZipPackage
/*  41:    */   extends OPCPackage
/*  42:    */ {
/*  43:    */   private static final String MIMETYPE = "mimetype";
/*  44:    */   private static final String SETTINGS_XML = "settings.xml";
/*  45: 60 */   private static final POILogger LOG = POILogFactory.getLogger(ZipPackage.class);
/*  46:    */   private final ZipEntrySource zipArchive;
/*  47:    */   
/*  48:    */   public ZipPackage()
/*  49:    */   {
/*  50: 72 */     super(defaultPackageAccess);
/*  51: 73 */     this.zipArchive = null;
/*  52:    */     try
/*  53:    */     {
/*  54: 76 */       this.contentTypeManager = new ZipContentTypeManager(null, this);
/*  55:    */     }
/*  56:    */     catch (InvalidFormatException e)
/*  57:    */     {
/*  58: 78 */       LOG.log(5, new Object[] { "Could not parse ZipPackage", e });
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   ZipPackage(InputStream in, PackageAccess access)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65: 97 */     super(access);
/*  66: 98 */     ZipSecureFile.ThresholdInputStream zis = ZipHelper.openZipStream(in);
/*  67:    */     try
/*  68:    */     {
/*  69:100 */       this.zipArchive = new ZipInputStreamZipEntrySource(zis);
/*  70:    */     }
/*  71:    */     catch (IOException e)
/*  72:    */     {
/*  73:102 */       IOUtils.closeQuietly(zis);
/*  74:103 */       throw new IOException("Failed to read zip entry source", e);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   ZipPackage(String path, PackageAccess access)
/*  79:    */     throws InvalidOperationException
/*  80:    */   {
/*  81:117 */     this(new File(path), access);
/*  82:    */   }
/*  83:    */   
/*  84:    */   ZipPackage(File file, PackageAccess access)
/*  85:    */     throws InvalidOperationException
/*  86:    */   {
/*  87:130 */     super(access);
/*  88:    */     ZipEntrySource ze;
/*  89:    */     try
/*  90:    */     {
/*  91:134 */       ZipFile zipFile = ZipHelper.openZipFile(file);
/*  92:135 */       ze = new ZipFileZipEntrySource(zipFile);
/*  93:    */     }
/*  94:    */     catch (IOException e)
/*  95:    */     {
/*  96:138 */       if (access == PackageAccess.WRITE) {
/*  97:139 */         throw new InvalidOperationException("Can't open the specified file: '" + file + "'", e);
/*  98:    */       }
/*  99:141 */       LOG.log(7, new Object[] { "Error in zip file " + file + " - falling back to stream processing (i.e. ignoring zip central directory)" });
/* 100:142 */       ze = openZipEntrySourceStream(file);
/* 101:    */     }
/* 102:144 */     this.zipArchive = ze;
/* 103:    */   }
/* 104:    */   
/* 105:    */   private static ZipEntrySource openZipEntrySourceStream(File file)
/* 106:    */     throws InvalidOperationException
/* 107:    */   {
/* 108:    */     FileInputStream fis;
/* 109:    */     try
/* 110:    */     {
/* 111:152 */       fis = new FileInputStream(file);
/* 112:    */     }
/* 113:    */     catch (FileNotFoundException e)
/* 114:    */     {
/* 115:155 */       throw new InvalidOperationException("Can't open the specified file input stream from file: '" + file + "'", e);
/* 116:    */     }
/* 117:    */     try
/* 118:    */     {
/* 119:161 */       return openZipEntrySourceStream(fis);
/* 120:    */     }
/* 121:    */     catch (Exception e)
/* 122:    */     {
/* 123:164 */       IOUtils.closeQuietly(fis);
/* 124:165 */       if ((e instanceof InvalidOperationException)) {
/* 125:166 */         throw ((InvalidOperationException)e);
/* 126:    */       }
/* 127:168 */       throw new InvalidOperationException("Failed to read the file input stream from file: '" + file + "'", e);
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   private static ZipEntrySource openZipEntrySourceStream(FileInputStream fis)
/* 132:    */     throws InvalidOperationException
/* 133:    */   {
/* 134:    */     ZipSecureFile.ThresholdInputStream zis;
/* 135:    */     try
/* 136:    */     {
/* 137:178 */       zis = ZipHelper.openZipStream(fis);
/* 138:    */     }
/* 139:    */     catch (IOException e)
/* 140:    */     {
/* 141:181 */       throw new InvalidOperationException("Could not open the file input stream", e);
/* 142:    */     }
/* 143:    */     try
/* 144:    */     {
/* 145:187 */       return openZipEntrySourceStream(zis);
/* 146:    */     }
/* 147:    */     catch (Exception e)
/* 148:    */     {
/* 149:190 */       IOUtils.closeQuietly(zis);
/* 150:191 */       if ((e instanceof InvalidOperationException)) {
/* 151:192 */         throw ((InvalidOperationException)e);
/* 152:    */       }
/* 153:194 */       throw new InvalidOperationException("Failed to read the zip entry source stream", e);
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   private static ZipEntrySource openZipEntrySourceStream(ZipSecureFile.ThresholdInputStream zis)
/* 158:    */     throws InvalidOperationException
/* 159:    */   {
/* 160:    */     try
/* 161:    */     {
/* 162:203 */       return new ZipInputStreamZipEntrySource(zis);
/* 163:    */     }
/* 164:    */     catch (IOException e)
/* 165:    */     {
/* 166:205 */       throw new InvalidOperationException("Could not open the specified zip entry source stream", e);
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   ZipPackage(ZipEntrySource zipEntry, PackageAccess access)
/* 171:    */   {
/* 172:220 */     super(access);
/* 173:221 */     this.zipArchive = zipEntry;
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected PackagePart[] getPartsImpl()
/* 177:    */     throws InvalidFormatException
/* 178:    */   {
/* 179:235 */     if (this.partList == null) {
/* 180:238 */       this.partList = new PackagePartCollection();
/* 181:    */     }
/* 182:241 */     if (this.zipArchive == null) {
/* 183:242 */       return (PackagePart[])this.partList.sortedValues().toArray(new PackagePart[this.partList.size()]);
/* 184:    */     }
/* 185:247 */     Enumeration<? extends ZipEntry> entries = this.zipArchive.getEntries();
/* 186:248 */     while (entries.hasMoreElements())
/* 187:    */     {
/* 188:249 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/* 189:250 */       if (entry.getName().equalsIgnoreCase("[Content_Types].xml")) {
/* 190:    */         try
/* 191:    */         {
/* 192:253 */           this.contentTypeManager = new ZipContentTypeManager(getZipArchive().getInputStream(entry), this);
/* 193:    */         }
/* 194:    */         catch (IOException e)
/* 195:    */         {
/* 196:256 */           throw new InvalidFormatException(e.getMessage(), e);
/* 197:    */         }
/* 198:    */       }
/* 199:    */     }
/* 200:263 */     if (this.contentTypeManager == null)
/* 201:    */     {
/* 202:265 */       int numEntries = 0;
/* 203:266 */       boolean hasMimetype = false;
/* 204:267 */       boolean hasSettingsXML = false;
/* 205:268 */       entries = this.zipArchive.getEntries();
/* 206:269 */       while (entries.hasMoreElements())
/* 207:    */       {
/* 208:270 */         ZipEntry entry = (ZipEntry)entries.nextElement();
/* 209:271 */         String name = entry.getName();
/* 210:272 */         if ("mimetype".equals(name)) {
/* 211:273 */           hasMimetype = true;
/* 212:    */         }
/* 213:275 */         if ("settings.xml".equals(name)) {
/* 214:276 */           hasSettingsXML = true;
/* 215:    */         }
/* 216:278 */         numEntries++;
/* 217:    */       }
/* 218:280 */       if ((hasMimetype) && (hasSettingsXML)) {
/* 219:281 */         throw new ODFNotOfficeXmlFileException("The supplied data appears to be in ODF (Open Document) Format. Formats like these (eg ODS, ODP) are not supported, try Apache ODFToolkit");
/* 220:    */       }
/* 221:285 */       if (numEntries == 0) {
/* 222:286 */         throw new NotOfficeXmlFileException("No valid entries or contents found, this is not a valid OOXML (Office Open XML) file");
/* 223:    */       }
/* 224:292 */       throw new InvalidFormatException("Package should contain a content type part [M1.13]");
/* 225:    */     }
/* 226:300 */     entries = this.zipArchive.getEntries();
/* 227:301 */     while (entries.hasMoreElements())
/* 228:    */     {
/* 229:302 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/* 230:303 */       PackagePartName partName = buildPartName(entry);
/* 231:304 */       if (partName != null)
/* 232:    */       {
/* 233:309 */         String contentType = this.contentTypeManager.getContentType(partName);
/* 234:310 */         if ((contentType != null) && (contentType.equals("application/vnd.openxmlformats-package.relationships+xml"))) {
/* 235:    */           try
/* 236:    */           {
/* 237:312 */             PackagePart part = new ZipPackagePart(this, entry, partName, contentType);
/* 238:313 */             this.partList.put(partName, part);
/* 239:    */           }
/* 240:    */           catch (InvalidOperationException e)
/* 241:    */           {
/* 242:315 */             throw new InvalidFormatException(e.getMessage(), e);
/* 243:    */           }
/* 244:    */         }
/* 245:    */       }
/* 246:    */     }
/* 247:321 */     entries = this.zipArchive.getEntries();
/* 248:322 */     while (entries.hasMoreElements())
/* 249:    */     {
/* 250:323 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/* 251:324 */       PackagePartName partName = buildPartName(entry);
/* 252:325 */       if (partName != null)
/* 253:    */       {
/* 254:329 */         String contentType = this.contentTypeManager.getContentType(partName);
/* 255:330 */         if ((contentType == null) || (!contentType.equals("application/vnd.openxmlformats-package.relationships+xml"))) {
/* 256:333 */           if (contentType != null) {
/* 257:    */             try
/* 258:    */             {
/* 259:335 */               PackagePart part = new ZipPackagePart(this, entry, partName, contentType);
/* 260:336 */               this.partList.put(partName, part);
/* 261:    */             }
/* 262:    */             catch (InvalidOperationException e)
/* 263:    */             {
/* 264:338 */               throw new InvalidFormatException(e.getMessage(), e);
/* 265:    */             }
/* 266:    */           } else {
/* 267:341 */             throw new InvalidFormatException("The part " + partName.getURI().getPath() + " does not have any content type ! Rule: Package require content types when retrieving a part from a package. [M.1.14]");
/* 268:    */           }
/* 269:    */         }
/* 270:    */       }
/* 271:    */     }
/* 272:347 */     return (PackagePart[])this.partList.sortedValues().toArray(new PackagePart[this.partList.size()]);
/* 273:    */   }
/* 274:    */   
/* 275:    */   private PackagePartName buildPartName(ZipEntry entry)
/* 276:    */   {
/* 277:    */     try
/* 278:    */     {
/* 279:358 */       if (entry.getName().equalsIgnoreCase("[Content_Types].xml")) {
/* 280:360 */         return null;
/* 281:    */       }
/* 282:362 */       return PackagingURIHelper.createPartName(ZipHelper.getOPCNameFromZipItemName(entry.getName()));
/* 283:    */     }
/* 284:    */     catch (Exception e)
/* 285:    */     {
/* 286:366 */       LOG.log(5, new Object[] { "Entry " + entry.getName() + " is not valid, so this part won't be add to the package.", e });
/* 287:    */     }
/* 288:369 */     return null;
/* 289:    */   }
/* 290:    */   
/* 291:    */   protected PackagePart createPartImpl(PackagePartName partName, String contentType, boolean loadRelationships)
/* 292:    */   {
/* 293:386 */     if (contentType == null) {
/* 294:387 */       throw new IllegalArgumentException("contentType");
/* 295:    */     }
/* 296:390 */     if (partName == null) {
/* 297:391 */       throw new IllegalArgumentException("partName");
/* 298:    */     }
/* 299:    */     try
/* 300:    */     {
/* 301:395 */       return new MemoryPackagePart(this, partName, contentType, loadRelationships);
/* 302:    */     }
/* 303:    */     catch (InvalidFormatException e)
/* 304:    */     {
/* 305:397 */       LOG.log(5, new Object[] { e });
/* 306:    */     }
/* 307:398 */     return null;
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected void removePartImpl(PackagePartName partName)
/* 311:    */   {
/* 312:410 */     if (partName == null) {
/* 313:411 */       throw new IllegalArgumentException("partUri");
/* 314:    */     }
/* 315:    */   }
/* 316:    */   
/* 317:    */   protected void flushImpl() {}
/* 318:    */   
/* 319:    */   protected void closeImpl()
/* 320:    */     throws IOException
/* 321:    */   {
/* 322:431 */     flush();
/* 323:433 */     if ((this.originalPackagePath == null) || ("".equals(this.originalPackagePath))) {
/* 324:434 */       return;
/* 325:    */     }
/* 326:438 */     File targetFile = new File(this.originalPackagePath);
/* 327:439 */     if (!targetFile.exists()) {
/* 328:440 */       throw new InvalidOperationException("Can't close a package not previously open with the open() method !");
/* 329:    */     }
/* 330:445 */     String tempFileName = generateTempFileName(FileHelper.getDirectory(targetFile));
/* 331:446 */     File tempFile = TempFile.createTempFile(tempFileName, ".tmp");
/* 332:    */     try
/* 333:    */     {
/* 334:450 */       save(tempFile);
/* 335:    */     }
/* 336:    */     finally
/* 337:    */     {
/* 338:453 */       IOUtils.closeQuietly(this.zipArchive);
/* 339:    */       try
/* 340:    */       {
/* 341:456 */         FileHelper.copyFile(tempFile, targetFile);
/* 342:    */       }
/* 343:    */       finally
/* 344:    */       {
/* 345:459 */         if (!tempFile.delete()) {
/* 346:460 */           LOG.log(5, new Object[] { "The temporary file: '" + targetFile.getAbsolutePath() + "' cannot be deleted ! Make sure that no other application use it." });
/* 347:    */         }
/* 348:    */       }
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   private synchronized String generateTempFileName(File directory)
/* 353:    */   {
/* 354:    */     File tmpFilename;
/* 355:    */     do
/* 356:    */     {
/* 357:476 */       tmpFilename = new File(directory.getAbsoluteFile() + File.separator + "OpenXML4J" + System.nanoTime());
/* 358:478 */     } while (tmpFilename.exists());
/* 359:479 */     return FileHelper.getFilename(tmpFilename.getAbsoluteFile());
/* 360:    */   }
/* 361:    */   
/* 362:    */   protected void revertImpl()
/* 363:    */   {
/* 364:    */     try
/* 365:    */     {
/* 366:489 */       if (this.zipArchive != null) {
/* 367:490 */         this.zipArchive.close();
/* 368:    */       }
/* 369:    */     }
/* 370:    */     catch (IOException e) {}
/* 371:    */   }
/* 372:    */   
/* 373:    */   protected PackagePart getPartImpl(PackagePartName partName)
/* 374:    */   {
/* 375:506 */     if (this.partList.containsKey(partName)) {
/* 376:507 */       return this.partList.get(partName);
/* 377:    */     }
/* 378:509 */     return null;
/* 379:    */   }
/* 380:    */   
/* 381:    */   public void saveImpl(OutputStream outputStream)
/* 382:    */   {
/* 383:524 */     throwExceptionIfReadOnly();
/* 384:    */     try
/* 385:    */     {
/* 386:    */       ZipOutputStream zos;
/* 387:    */       ZipOutputStream zos;
/* 388:528 */       if (!(outputStream instanceof ZipOutputStream)) {
/* 389:529 */         zos = new ZipOutputStream(outputStream);
/* 390:    */       } else {
/* 391:531 */         zos = (ZipOutputStream)outputStream;
/* 392:    */       }
/* 393:536 */       if ((getPartsByRelationshipType("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties").size() == 0) && (getPartsByRelationshipType("http://schemas.openxmlformats.org/officedocument/2006/relationships/metadata/core-properties").size() == 0))
/* 394:    */       {
/* 395:538 */         LOG.log(1, new Object[] { "Save core properties part" });
/* 396:    */         
/* 397:    */ 
/* 398:541 */         getPackageProperties();
/* 399:    */         
/* 400:543 */         addPackagePart(this.packageProperties);
/* 401:    */         
/* 402:545 */         this.relationships.addRelationship(this.packageProperties.getPartName().getURI(), TargetMode.INTERNAL, "http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties", null);
/* 403:549 */         if (!this.contentTypeManager.isContentTypeRegister("application/vnd.openxmlformats-package.core-properties+xml")) {
/* 404:551 */           this.contentTypeManager.addContentType(this.packageProperties.getPartName(), "application/vnd.openxmlformats-package.core-properties+xml");
/* 405:    */         }
/* 406:    */       }
/* 407:558 */       LOG.log(1, new Object[] { "Save package relationships" });
/* 408:559 */       ZipPartMarshaller.marshallRelationshipPart(getRelationships(), PackagingURIHelper.PACKAGE_RELATIONSHIPS_ROOT_PART_NAME, zos);
/* 409:    */       
/* 410:    */ 
/* 411:    */ 
/* 412:    */ 
/* 413:564 */       LOG.log(1, new Object[] { "Save content types part" });
/* 414:565 */       this.contentTypeManager.save(zos);
/* 415:568 */       for (PackagePart part : getParts()) {
/* 416:571 */         if (!part.isRelationshipPart())
/* 417:    */         {
/* 418:575 */           PackagePartName ppn = part.getPartName();
/* 419:576 */           LOG.log(1, new Object[] { "Save part '" + ZipHelper.getZipItemNameFromOPCName(ppn.getName()) + "'" });
/* 420:577 */           PartMarshaller marshaller = (PartMarshaller)this.partMarshallers.get(part._contentType);
/* 421:578 */           String errMsg = "The part " + ppn.getURI() + " failed to be saved in the stream with marshaller ";
/* 422:580 */           if (marshaller != null)
/* 423:    */           {
/* 424:581 */             if (!marshaller.marshall(part, zos)) {
/* 425:582 */               throw new OpenXML4JException(errMsg + marshaller);
/* 426:    */             }
/* 427:    */           }
/* 428:585 */           else if (!this.defaultPartMarshaller.marshall(part, zos)) {
/* 429:586 */             throw new OpenXML4JException(errMsg + this.defaultPartMarshaller);
/* 430:    */           }
/* 431:    */         }
/* 432:    */       }
/* 433:590 */       zos.close();
/* 434:    */     }
/* 435:    */     catch (OpenXML4JRuntimeException e)
/* 436:    */     {
/* 437:593 */       throw e;
/* 438:    */     }
/* 439:    */     catch (Exception e)
/* 440:    */     {
/* 441:595 */       throw new OpenXML4JRuntimeException("Fail to save: an error occurs while saving the package : " + e.getMessage(), e);
/* 442:    */     }
/* 443:    */   }
/* 444:    */   
/* 445:    */   public ZipEntrySource getZipArchive()
/* 446:    */   {
/* 447:607 */     return this.zipArchive;
/* 448:    */   }
/* 449:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.ZipPackage
 * JD-Core Version:    0.7.0.1
 */