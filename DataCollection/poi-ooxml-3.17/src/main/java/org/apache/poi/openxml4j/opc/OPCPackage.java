/*    1:     */ package org.apache.poi.openxml4j.opc;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayOutputStream;
/*    4:     */ import java.io.Closeable;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileInputStream;
/*    7:     */ import java.io.FileOutputStream;
/*    8:     */ import java.io.IOException;
/*    9:     */ import java.io.InputStream;
/*   10:     */ import java.io.OutputStream;
/*   11:     */ import java.net.URI;
/*   12:     */ import java.net.URISyntaxException;
/*   13:     */ import java.util.ArrayList;
/*   14:     */ import java.util.Collections;
/*   15:     */ import java.util.Date;
/*   16:     */ import java.util.HashMap;
/*   17:     */ import java.util.List;
/*   18:     */ import java.util.Map;
/*   19:     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*   20:     */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*   21:     */ import java.util.regex.Matcher;
/*   22:     */ import java.util.regex.Pattern;
/*   23:     */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   24:     */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*   25:     */ import org.apache.poi.openxml4j.exceptions.OpenXML4JRuntimeException;
/*   26:     */ import org.apache.poi.openxml4j.exceptions.PartAlreadyExistsException;
/*   27:     */ import org.apache.poi.openxml4j.opc.internal.ContentType;
/*   28:     */ import org.apache.poi.openxml4j.opc.internal.ContentTypeManager;
/*   29:     */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*   30:     */ import org.apache.poi.openxml4j.opc.internal.PartMarshaller;
/*   31:     */ import org.apache.poi.openxml4j.opc.internal.PartUnmarshaller;
/*   32:     */ import org.apache.poi.openxml4j.opc.internal.ZipContentTypeManager;
/*   33:     */ import org.apache.poi.openxml4j.opc.internal.marshallers.DefaultMarshaller;
/*   34:     */ import org.apache.poi.openxml4j.opc.internal.marshallers.ZipPackagePropertiesMarshaller;
/*   35:     */ import org.apache.poi.openxml4j.opc.internal.unmarshallers.PackagePropertiesUnmarshaller;
/*   36:     */ import org.apache.poi.openxml4j.opc.internal.unmarshallers.UnmarshallContext;
/*   37:     */ import org.apache.poi.openxml4j.util.Nullable;
/*   38:     */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*   39:     */ import org.apache.poi.util.IOUtils;
/*   40:     */ import org.apache.poi.util.NotImplemented;
/*   41:     */ import org.apache.poi.util.POILogFactory;
/*   42:     */ import org.apache.poi.util.POILogger;
/*   43:     */ 
/*   44:     */ public abstract class OPCPackage
/*   45:     */   implements RelationshipSource, Closeable
/*   46:     */ {
/*   47:  69 */   private static final POILogger logger = POILogFactory.getLogger(OPCPackage.class);
/*   48:  74 */   protected static final PackageAccess defaultPackageAccess = PackageAccess.READ_WRITE;
/*   49:     */   private PackageAccess packageAccess;
/*   50:     */   protected PackagePartCollection partList;
/*   51:     */   protected PackageRelationshipCollection relationships;
/*   52:     */   protected Map<ContentType, PartMarshaller> partMarshallers;
/*   53:     */   protected PartMarshaller defaultPartMarshaller;
/*   54:     */   protected Map<ContentType, PartUnmarshaller> partUnmarshallers;
/*   55:     */   protected PackagePropertiesPart packageProperties;
/*   56:     */   protected ContentTypeManager contentTypeManager;
/*   57: 119 */   protected boolean isDirty = false;
/*   58:     */   protected String originalPackagePath;
/*   59:     */   protected OutputStream output;
/*   60:     */   
/*   61:     */   OPCPackage(PackageAccess access)
/*   62:     */   {
/*   63: 138 */     if (getClass() != ZipPackage.class) {
/*   64: 139 */       throw new IllegalArgumentException("PackageBase may not be subclassed");
/*   65:     */     }
/*   66: 141 */     init();
/*   67: 142 */     this.packageAccess = access;
/*   68:     */   }
/*   69:     */   
/*   70:     */   private void init()
/*   71:     */   {
/*   72: 149 */     this.partMarshallers = new HashMap(5);
/*   73: 150 */     this.partUnmarshallers = new HashMap(2);
/*   74:     */     try
/*   75:     */     {
/*   76: 154 */       this.partUnmarshallers.put(new ContentType("application/vnd.openxmlformats-package.core-properties+xml"), new PackagePropertiesUnmarshaller());
/*   77:     */       
/*   78:     */ 
/*   79:     */ 
/*   80:     */ 
/*   81: 159 */       this.defaultPartMarshaller = new DefaultMarshaller();
/*   82:     */       
/*   83: 161 */       this.partMarshallers.put(new ContentType("application/vnd.openxmlformats-package.core-properties+xml"), new ZipPackagePropertiesMarshaller());
/*   84:     */     }
/*   85:     */     catch (InvalidFormatException e)
/*   86:     */     {
/*   87: 166 */       throw new OpenXML4JRuntimeException("Package.init() : this exception should never happen, if you read this message please send a mail to the developers team. : " + e.getMessage(), e);
/*   88:     */     }
/*   89:     */   }
/*   90:     */   
/*   91:     */   public static OPCPackage open(String path)
/*   92:     */     throws InvalidFormatException
/*   93:     */   {
/*   94: 187 */     return open(path, defaultPackageAccess);
/*   95:     */   }
/*   96:     */   
/*   97:     */   public static OPCPackage open(File file)
/*   98:     */     throws InvalidFormatException
/*   99:     */   {
/*  100: 201 */     return open(file, defaultPackageAccess);
/*  101:     */   }
/*  102:     */   
/*  103:     */   public static OPCPackage open(ZipEntrySource zipEntry)
/*  104:     */     throws InvalidFormatException
/*  105:     */   {
/*  106: 216 */     OPCPackage pack = new ZipPackage(zipEntry, PackageAccess.READ);
/*  107:     */     try
/*  108:     */     {
/*  109: 218 */       if (pack.partList == null) {
/*  110: 219 */         pack.getParts();
/*  111:     */       }
/*  112: 222 */       return pack;
/*  113:     */     }
/*  114:     */     catch (InvalidFormatException e)
/*  115:     */     {
/*  116: 224 */       IOUtils.closeQuietly(pack);
/*  117: 225 */       throw e;
/*  118:     */     }
/*  119:     */     catch (RuntimeException e)
/*  120:     */     {
/*  121: 227 */       IOUtils.closeQuietly(pack);
/*  122: 228 */       throw e;
/*  123:     */     }
/*  124:     */   }
/*  125:     */   
/*  126:     */   public static OPCPackage open(String path, PackageAccess access)
/*  127:     */     throws InvalidFormatException, InvalidOperationException
/*  128:     */   {
/*  129: 248 */     if ((path == null) || ("".equals(path.trim()))) {
/*  130: 249 */       throw new IllegalArgumentException("'path' must be given");
/*  131:     */     }
/*  132: 252 */     File file = new File(path);
/*  133: 253 */     if ((file.exists()) && (file.isDirectory())) {
/*  134: 254 */       throw new IllegalArgumentException("path must not be a directory");
/*  135:     */     }
/*  136: 257 */     OPCPackage pack = new ZipPackage(path, access);
/*  137: 258 */     boolean success = false;
/*  138: 259 */     if ((pack.partList == null) && (access != PackageAccess.WRITE)) {
/*  139:     */       try
/*  140:     */       {
/*  141: 261 */         pack.getParts();
/*  142: 262 */         success = true;
/*  143:     */       }
/*  144:     */       finally
/*  145:     */       {
/*  146: 264 */         if (!success) {
/*  147: 265 */           IOUtils.closeQuietly(pack);
/*  148:     */         }
/*  149:     */       }
/*  150:     */     }
/*  151: 270 */     pack.originalPackagePath = new File(path).getAbsolutePath();
/*  152: 271 */     return pack;
/*  153:     */   }
/*  154:     */   
/*  155:     */   public static OPCPackage open(File file, PackageAccess access)
/*  156:     */     throws InvalidFormatException
/*  157:     */   {
/*  158: 288 */     if (file == null) {
/*  159: 289 */       throw new IllegalArgumentException("'file' must be given");
/*  160:     */     }
/*  161: 291 */     if ((file.exists()) && (file.isDirectory())) {
/*  162: 292 */       throw new IllegalArgumentException("file must not be a directory");
/*  163:     */     }
/*  164: 295 */     OPCPackage pack = new ZipPackage(file, access);
/*  165:     */     try
/*  166:     */     {
/*  167: 297 */       if ((pack.partList == null) && (access != PackageAccess.WRITE)) {
/*  168: 298 */         pack.getParts();
/*  169:     */       }
/*  170: 300 */       pack.originalPackagePath = file.getAbsolutePath();
/*  171: 301 */       return pack;
/*  172:     */     }
/*  173:     */     catch (InvalidFormatException e)
/*  174:     */     {
/*  175: 303 */       IOUtils.closeQuietly(pack);
/*  176: 304 */       throw e;
/*  177:     */     }
/*  178:     */     catch (RuntimeException e)
/*  179:     */     {
/*  180: 306 */       IOUtils.closeQuietly(pack);
/*  181: 307 */       throw e;
/*  182:     */     }
/*  183:     */   }
/*  184:     */   
/*  185:     */   public static OPCPackage open(InputStream in)
/*  186:     */     throws InvalidFormatException, IOException
/*  187:     */   {
/*  188: 324 */     OPCPackage pack = new ZipPackage(in, PackageAccess.READ_WRITE);
/*  189:     */     try
/*  190:     */     {
/*  191: 326 */       if (pack.partList == null) {
/*  192: 327 */         pack.getParts();
/*  193:     */       }
/*  194:     */     }
/*  195:     */     catch (InvalidFormatException e)
/*  196:     */     {
/*  197: 330 */       IOUtils.closeQuietly(pack);
/*  198: 331 */       throw e;
/*  199:     */     }
/*  200:     */     catch (RuntimeException e)
/*  201:     */     {
/*  202: 333 */       IOUtils.closeQuietly(pack);
/*  203: 334 */       throw e;
/*  204:     */     }
/*  205: 336 */     return pack;
/*  206:     */   }
/*  207:     */   
/*  208:     */   public static OPCPackage openOrCreate(File file)
/*  209:     */     throws InvalidFormatException
/*  210:     */   {
/*  211: 350 */     if (file.exists()) {
/*  212: 351 */       return open(file.getAbsolutePath());
/*  213:     */     }
/*  214: 353 */     return create(file);
/*  215:     */   }
/*  216:     */   
/*  217:     */   public static OPCPackage create(String path)
/*  218:     */   {
/*  219: 365 */     return create(new File(path));
/*  220:     */   }
/*  221:     */   
/*  222:     */   public static OPCPackage create(File file)
/*  223:     */   {
/*  224: 376 */     if ((file == null) || ((file.exists()) && (file.isDirectory()))) {
/*  225: 377 */       throw new IllegalArgumentException("file");
/*  226:     */     }
/*  227: 380 */     if (file.exists()) {
/*  228: 381 */       throw new InvalidOperationException("This package (or file) already exists : use the open() method or delete the file.");
/*  229:     */     }
/*  230: 386 */     OPCPackage pkg = new ZipPackage();
/*  231: 387 */     pkg.originalPackagePath = file.getAbsolutePath();
/*  232:     */     
/*  233: 389 */     configurePackage(pkg);
/*  234: 390 */     return pkg;
/*  235:     */   }
/*  236:     */   
/*  237:     */   public static OPCPackage create(OutputStream output)
/*  238:     */   {
/*  239: 394 */     OPCPackage pkg = new ZipPackage();
/*  240: 395 */     pkg.originalPackagePath = null;
/*  241: 396 */     pkg.output = output;
/*  242:     */     
/*  243: 398 */     configurePackage(pkg);
/*  244: 399 */     return pkg;
/*  245:     */   }
/*  246:     */   
/*  247:     */   private static void configurePackage(OPCPackage pkg)
/*  248:     */   {
/*  249:     */     try
/*  250:     */     {
/*  251: 405 */       pkg.contentTypeManager = new ZipContentTypeManager(null, pkg);
/*  252:     */       
/*  253:     */ 
/*  254: 408 */       pkg.contentTypeManager.addContentType(PackagingURIHelper.createPartName(PackagingURIHelper.PACKAGE_RELATIONSHIPS_ROOT_URI), "application/vnd.openxmlformats-package.relationships+xml");
/*  255:     */       
/*  256:     */ 
/*  257:     */ 
/*  258: 412 */       pkg.contentTypeManager.addContentType(PackagingURIHelper.createPartName("/default.xml"), "application/xml");
/*  259:     */       
/*  260:     */ 
/*  261:     */ 
/*  262:     */ 
/*  263: 417 */       pkg.packageProperties = new PackagePropertiesPart(pkg, PackagingURIHelper.CORE_PROPERTIES_PART_NAME);
/*  264:     */       
/*  265: 419 */       pkg.packageProperties.setCreatorProperty("Generated by Apache POI OpenXML4J");
/*  266: 420 */       pkg.packageProperties.setCreatedProperty(new Nullable(new Date()));
/*  267:     */     }
/*  268:     */     catch (InvalidFormatException e)
/*  269:     */     {
/*  270: 423 */       throw new IllegalStateException(e);
/*  271:     */     }
/*  272:     */   }
/*  273:     */   
/*  274:     */   public void flush()
/*  275:     */   {
/*  276: 433 */     throwExceptionIfReadOnly();
/*  277: 435 */     if (this.packageProperties != null) {
/*  278: 436 */       this.packageProperties.flush();
/*  279:     */     }
/*  280: 439 */     flushImpl();
/*  281:     */   }
/*  282:     */   
/*  283:     */   public void close()
/*  284:     */     throws IOException
/*  285:     */   {
/*  286: 453 */     if (this.packageAccess == PackageAccess.READ)
/*  287:     */     {
/*  288: 454 */       logger.log(5, new Object[] { "The close() method is intended to SAVE a package. This package is open in READ ONLY mode, use the revert() method instead !" });
/*  289:     */       
/*  290: 456 */       revert();
/*  291: 457 */       return;
/*  292:     */     }
/*  293: 459 */     if (this.contentTypeManager == null)
/*  294:     */     {
/*  295: 460 */       logger.log(5, new Object[] { "Unable to call close() on a package that hasn't been fully opened yet" });
/*  296:     */       
/*  297: 462 */       revert();
/*  298: 463 */       return;
/*  299:     */     }
/*  300: 467 */     ReentrantReadWriteLock l = new ReentrantReadWriteLock();
/*  301:     */     try
/*  302:     */     {
/*  303: 469 */       l.writeLock().lock();
/*  304: 470 */       if ((this.originalPackagePath != null) && (!"".equals(this.originalPackagePath.trim())))
/*  305:     */       {
/*  306: 472 */         File targetFile = new File(this.originalPackagePath);
/*  307: 473 */         if ((!targetFile.exists()) || (!this.originalPackagePath.equalsIgnoreCase(targetFile.getAbsolutePath()))) {
/*  308: 477 */           save(targetFile);
/*  309:     */         } else {
/*  310: 479 */           closeImpl();
/*  311:     */         }
/*  312:     */       }
/*  313: 481 */       else if (this.output != null)
/*  314:     */       {
/*  315: 482 */         save(this.output);
/*  316: 483 */         this.output.close();
/*  317:     */       }
/*  318:     */     }
/*  319:     */     finally
/*  320:     */     {
/*  321: 486 */       l.writeLock().unlock();
/*  322:     */     }
/*  323: 490 */     this.contentTypeManager.clearAll();
/*  324:     */   }
/*  325:     */   
/*  326:     */   public void revert()
/*  327:     */   {
/*  328: 498 */     revertImpl();
/*  329:     */   }
/*  330:     */   
/*  331:     */   public void addThumbnail(String path)
/*  332:     */     throws IOException
/*  333:     */   {
/*  334: 510 */     if ((path == null) || (path.isEmpty())) {
/*  335: 511 */       throw new IllegalArgumentException("path");
/*  336:     */     }
/*  337: 513 */     String name = path.substring(path.lastIndexOf(File.separatorChar) + 1);
/*  338:     */     
/*  339: 515 */     FileInputStream is = new FileInputStream(path);
/*  340:     */     try
/*  341:     */     {
/*  342: 517 */       addThumbnail(name, is);
/*  343:     */     }
/*  344:     */     finally
/*  345:     */     {
/*  346: 519 */       is.close();
/*  347:     */     }
/*  348:     */   }
/*  349:     */   
/*  350:     */   public void addThumbnail(String filename, InputStream data)
/*  351:     */     throws IOException
/*  352:     */   {
/*  353: 532 */     if ((filename == null) || (filename.isEmpty())) {
/*  354: 533 */       throw new IllegalArgumentException("filename");
/*  355:     */     }
/*  356: 537 */     String contentType = ContentTypes.getContentTypeFromFileExtension(filename);
/*  357:     */     PackagePartName thumbnailPartName;
/*  358:     */     try
/*  359:     */     {
/*  360: 541 */       thumbnailPartName = PackagingURIHelper.createPartName("/docProps/" + filename);
/*  361:     */     }
/*  362:     */     catch (InvalidFormatException e)
/*  363:     */     {
/*  364: 544 */       String partName = "/docProps/thumbnail" + filename.substring(filename.lastIndexOf(".") + 1);
/*  365:     */       try
/*  366:     */       {
/*  367: 547 */         thumbnailPartName = PackagingURIHelper.createPartName(partName);
/*  368:     */       }
/*  369:     */       catch (InvalidFormatException e2)
/*  370:     */       {
/*  371: 549 */         throw new InvalidOperationException("Can't add a thumbnail file named '" + filename + "'", e2);
/*  372:     */       }
/*  373:     */     }
/*  374: 555 */     if (getPart(thumbnailPartName) != null) {
/*  375: 556 */       throw new InvalidOperationException("You already add a thumbnail named '" + filename + "'");
/*  376:     */     }
/*  377: 561 */     PackagePart thumbnailPart = createPart(thumbnailPartName, contentType, false);
/*  378:     */     
/*  379:     */ 
/*  380:     */ 
/*  381: 565 */     addRelationship(thumbnailPartName, TargetMode.INTERNAL, "http://schemas.openxmlformats.org/package/2006/relationships/metadata/thumbnail");
/*  382:     */     
/*  383:     */ 
/*  384:     */ 
/*  385: 569 */     StreamHelper.copyStream(data, thumbnailPart.getOutputStream());
/*  386:     */   }
/*  387:     */   
/*  388:     */   void throwExceptionIfReadOnly()
/*  389:     */     throws InvalidOperationException
/*  390:     */   {
/*  391: 581 */     if (this.packageAccess == PackageAccess.READ) {
/*  392: 582 */       throw new InvalidOperationException("Operation not allowed, document open in read only mode!");
/*  393:     */     }
/*  394:     */   }
/*  395:     */   
/*  396:     */   void throwExceptionIfWriteOnly()
/*  397:     */     throws InvalidOperationException
/*  398:     */   {
/*  399: 596 */     if (this.packageAccess == PackageAccess.WRITE) {
/*  400: 597 */       throw new InvalidOperationException("Operation not allowed, document open in write only mode!");
/*  401:     */     }
/*  402:     */   }
/*  403:     */   
/*  404:     */   public PackageProperties getPackageProperties()
/*  405:     */     throws InvalidFormatException
/*  406:     */   {
/*  407: 609 */     throwExceptionIfWriteOnly();
/*  408: 611 */     if (this.packageProperties == null) {
/*  409: 612 */       this.packageProperties = new PackagePropertiesPart(this, PackagingURIHelper.CORE_PROPERTIES_PART_NAME);
/*  410:     */     }
/*  411: 615 */     return this.packageProperties;
/*  412:     */   }
/*  413:     */   
/*  414:     */   public PackagePart getPart(PackagePartName partName)
/*  415:     */   {
/*  416: 626 */     throwExceptionIfWriteOnly();
/*  417: 628 */     if (partName == null) {
/*  418: 629 */       throw new IllegalArgumentException("partName");
/*  419:     */     }
/*  420: 633 */     if (this.partList == null) {
/*  421:     */       try
/*  422:     */       {
/*  423: 635 */         getParts();
/*  424:     */       }
/*  425:     */       catch (InvalidFormatException e)
/*  426:     */       {
/*  427: 637 */         return null;
/*  428:     */       }
/*  429:     */     }
/*  430: 640 */     return getPartImpl(partName);
/*  431:     */   }
/*  432:     */   
/*  433:     */   public ArrayList<PackagePart> getPartsByContentType(String contentType)
/*  434:     */   {
/*  435: 651 */     ArrayList<PackagePart> retArr = new ArrayList();
/*  436: 652 */     for (PackagePart part : this.partList.sortedValues()) {
/*  437: 653 */       if (part.getContentType().equals(contentType)) {
/*  438: 654 */         retArr.add(part);
/*  439:     */       }
/*  440:     */     }
/*  441: 657 */     return retArr;
/*  442:     */   }
/*  443:     */   
/*  444:     */   public ArrayList<PackagePart> getPartsByRelationshipType(String relationshipType)
/*  445:     */   {
/*  446: 671 */     if (relationshipType == null) {
/*  447: 672 */       throw new IllegalArgumentException("relationshipType");
/*  448:     */     }
/*  449: 674 */     ArrayList<PackagePart> retArr = new ArrayList();
/*  450: 675 */     for (PackageRelationship rel : getRelationshipsByType(relationshipType))
/*  451:     */     {
/*  452: 676 */       PackagePart part = getPart(rel);
/*  453: 677 */       if (part != null) {
/*  454: 678 */         retArr.add(part);
/*  455:     */       }
/*  456:     */     }
/*  457: 681 */     Collections.sort(retArr);
/*  458: 682 */     return retArr;
/*  459:     */   }
/*  460:     */   
/*  461:     */   public List<PackagePart> getPartsByName(Pattern namePattern)
/*  462:     */   {
/*  463: 694 */     if (namePattern == null) {
/*  464: 695 */       throw new IllegalArgumentException("name pattern must not be null");
/*  465:     */     }
/*  466: 697 */     Matcher matcher = namePattern.matcher("");
/*  467: 698 */     ArrayList<PackagePart> result = new ArrayList();
/*  468: 699 */     for (PackagePart part : this.partList.sortedValues())
/*  469:     */     {
/*  470: 700 */       PackagePartName partName = part.getPartName();
/*  471: 701 */       if (matcher.reset(partName.getName()).matches()) {
/*  472: 702 */         result.add(part);
/*  473:     */       }
/*  474:     */     }
/*  475: 705 */     return result;
/*  476:     */   }
/*  477:     */   
/*  478:     */   public PackagePart getPart(PackageRelationship partRel)
/*  479:     */   {
/*  480: 715 */     PackagePart retPart = null;
/*  481: 716 */     ensureRelationships();
/*  482: 717 */     for (PackageRelationship rel : this.relationships) {
/*  483: 718 */       if (rel.getRelationshipType().equals(partRel.getRelationshipType())) {
/*  484:     */         try
/*  485:     */         {
/*  486: 720 */           retPart = getPart(PackagingURIHelper.createPartName(rel.getTargetURI()));
/*  487:     */         }
/*  488:     */         catch (InvalidFormatException e) {}
/*  489:     */       }
/*  490:     */     }
/*  491: 728 */     return retPart;
/*  492:     */   }
/*  493:     */   
/*  494:     */   public ArrayList<PackagePart> getParts()
/*  495:     */     throws InvalidFormatException
/*  496:     */   {
/*  497: 744 */     throwExceptionIfWriteOnly();
/*  498: 747 */     if (this.partList == null)
/*  499:     */     {
/*  500: 753 */       boolean hasCorePropertiesPart = false;
/*  501: 754 */       boolean needCorePropertiesPart = true;
/*  502:     */       
/*  503: 756 */       PackagePart[] parts = getPartsImpl();
/*  504: 757 */       this.partList = new PackagePartCollection();
/*  505: 758 */       for (PackagePart part : parts)
/*  506:     */       {
/*  507: 759 */         if (this.partList.containsKey(part._partName)) {
/*  508: 760 */           throw new InvalidFormatException("A part with the name '" + part._partName + "' already exist : Packages shall not contain equivalent " + "part names and package implementers shall neither create " + "nor recognize packages with equivalent part names. [M1.12]");
/*  509:     */         }
/*  510: 769 */         if (part.getContentType().equals("application/vnd.openxmlformats-package.core-properties+xml")) {
/*  511: 771 */           if (!hasCorePropertiesPart) {
/*  512: 772 */             hasCorePropertiesPart = true;
/*  513:     */           } else {
/*  514: 774 */             logger.log(5, new Object[] { "OPC Compliance error [M4.1]: there is more than one core properties relationship in the package! POI will use only the first, but other software may reject this file." });
/*  515:     */           }
/*  516:     */         }
/*  517: 780 */         PartUnmarshaller partUnmarshaller = (PartUnmarshaller)this.partUnmarshallers.get(part._contentType);
/*  518: 782 */         if (partUnmarshaller != null)
/*  519:     */         {
/*  520: 783 */           UnmarshallContext context = new UnmarshallContext(this, part._partName);
/*  521:     */           try
/*  522:     */           {
/*  523: 786 */             PackagePart unmarshallPart = partUnmarshaller.unmarshall(context, part.getInputStream());
/*  524:     */             
/*  525: 788 */             this.partList.put(unmarshallPart._partName, unmarshallPart);
/*  526: 792 */             if (((unmarshallPart instanceof PackagePropertiesPart)) && (hasCorePropertiesPart) && (needCorePropertiesPart))
/*  527:     */             {
/*  528: 795 */               this.packageProperties = ((PackagePropertiesPart)unmarshallPart);
/*  529: 796 */               needCorePropertiesPart = false;
/*  530:     */             }
/*  531:     */           }
/*  532:     */           catch (IOException ioe)
/*  533:     */           {
/*  534: 799 */             logger.log(5, new Object[] { "Unmarshall operation : IOException for " + part._partName });
/*  535:     */             
/*  536: 801 */             continue;
/*  537:     */           }
/*  538:     */           catch (InvalidOperationException invoe)
/*  539:     */           {
/*  540: 803 */             throw new InvalidFormatException(invoe.getMessage(), invoe);
/*  541:     */           }
/*  542:     */         }
/*  543:     */         else
/*  544:     */         {
/*  545:     */           try
/*  546:     */           {
/*  547: 807 */             this.partList.put(part._partName, part);
/*  548:     */           }
/*  549:     */           catch (InvalidOperationException e)
/*  550:     */           {
/*  551: 809 */             throw new InvalidFormatException(e.getMessage(), e);
/*  552:     */           }
/*  553:     */         }
/*  554:     */       }
/*  555:     */     }
/*  556: 814 */     return new ArrayList(this.partList.sortedValues());
/*  557:     */   }
/*  558:     */   
/*  559:     */   public PackagePart createPart(PackagePartName partName, String contentType)
/*  560:     */   {
/*  561: 833 */     return createPart(partName, contentType, true);
/*  562:     */   }
/*  563:     */   
/*  564:     */   PackagePart createPart(PackagePartName partName, String contentType, boolean loadRelationships)
/*  565:     */   {
/*  566: 857 */     throwExceptionIfReadOnly();
/*  567: 858 */     if (partName == null) {
/*  568: 859 */       throw new IllegalArgumentException("partName");
/*  569:     */     }
/*  570: 862 */     if ((contentType == null) || (contentType.equals(""))) {
/*  571: 863 */       throw new IllegalArgumentException("contentType");
/*  572:     */     }
/*  573: 867 */     if ((this.partList.containsKey(partName)) && (!this.partList.get(partName).isDeleted())) {
/*  574: 869 */       throw new PartAlreadyExistsException("A part with the name '" + partName.getName() + "'" + " already exists : Packages shall not contain equivalent part names and package" + " implementers shall neither create nor recognize packages with equivalent part names. [M1.12]");
/*  575:     */     }
/*  576: 884 */     if ((contentType.equals("application/vnd.openxmlformats-package.core-properties+xml")) && 
/*  577: 885 */       (this.packageProperties != null)) {
/*  578: 886 */       throw new InvalidOperationException("OPC Compliance error [M4.1]: you try to add more than one core properties relationship in the package !");
/*  579:     */     }
/*  580: 893 */     PackagePart part = createPartImpl(partName, contentType, loadRelationships);
/*  581:     */     
/*  582: 895 */     this.contentTypeManager.addContentType(partName, contentType);
/*  583: 896 */     this.partList.put(partName, part);
/*  584: 897 */     this.isDirty = true;
/*  585: 898 */     return part;
/*  586:     */   }
/*  587:     */   
/*  588:     */   public PackagePart createPart(PackagePartName partName, String contentType, ByteArrayOutputStream content)
/*  589:     */   {
/*  590: 918 */     PackagePart addedPart = createPart(partName, contentType);
/*  591: 919 */     if (addedPart == null) {
/*  592: 920 */       return null;
/*  593:     */     }
/*  594: 923 */     if (content != null) {
/*  595:     */       try
/*  596:     */       {
/*  597: 925 */         OutputStream partOutput = addedPart.getOutputStream();
/*  598: 926 */         if (partOutput == null) {
/*  599: 927 */           return null;
/*  600:     */         }
/*  601: 930 */         partOutput.write(content.toByteArray(), 0, content.size());
/*  602: 931 */         partOutput.close();
/*  603:     */       }
/*  604:     */       catch (IOException ioe)
/*  605:     */       {
/*  606: 934 */         return null;
/*  607:     */       }
/*  608:     */     } else {
/*  609: 937 */       return null;
/*  610:     */     }
/*  611: 939 */     return addedPart;
/*  612:     */   }
/*  613:     */   
/*  614:     */   protected PackagePart addPackagePart(PackagePart part)
/*  615:     */   {
/*  616: 956 */     throwExceptionIfReadOnly();
/*  617: 957 */     if (part == null) {
/*  618: 958 */       throw new IllegalArgumentException("part");
/*  619:     */     }
/*  620: 961 */     if (this.partList.containsKey(part._partName))
/*  621:     */     {
/*  622: 962 */       if (!this.partList.get(part._partName).isDeleted()) {
/*  623: 963 */         throw new InvalidOperationException("A part with the name '" + part._partName.getName() + "' already exists : Packages shall not contain equivalent part names and package implementers shall neither create nor recognize packages with equivalent part names. [M1.12]");
/*  624:     */       }
/*  625: 970 */       part.setDeleted(false);
/*  626:     */       
/*  627: 972 */       this.partList.remove(part._partName);
/*  628:     */     }
/*  629: 974 */     this.partList.put(part._partName, part);
/*  630: 975 */     this.isDirty = true;
/*  631: 976 */     return part;
/*  632:     */   }
/*  633:     */   
/*  634:     */   public void removePart(PackagePart part)
/*  635:     */   {
/*  636: 988 */     if (part != null) {
/*  637: 989 */       removePart(part.getPartName());
/*  638:     */     }
/*  639:     */   }
/*  640:     */   
/*  641:     */   public void removePart(PackagePartName partName)
/*  642:     */   {
/*  643:1001 */     throwExceptionIfReadOnly();
/*  644:1002 */     if ((partName == null) || (!containPart(partName))) {
/*  645:1003 */       throw new IllegalArgumentException("partName");
/*  646:     */     }
/*  647:1007 */     if (this.partList.containsKey(partName))
/*  648:     */     {
/*  649:1008 */       this.partList.get(partName).setDeleted(true);
/*  650:1009 */       removePartImpl(partName);
/*  651:1010 */       this.partList.remove(partName);
/*  652:     */     }
/*  653:     */     else
/*  654:     */     {
/*  655:1012 */       removePartImpl(partName);
/*  656:     */     }
/*  657:1016 */     this.contentTypeManager.removeContentType(partName);
/*  658:1020 */     if (partName.isRelationshipPartURI())
/*  659:     */     {
/*  660:1021 */       URI sourceURI = PackagingURIHelper.getSourcePartUriFromRelationshipPartUri(partName.getURI());
/*  661:     */       PackagePartName sourcePartName;
/*  662:     */       try
/*  663:     */       {
/*  664:1025 */         sourcePartName = PackagingURIHelper.createPartName(sourceURI);
/*  665:     */       }
/*  666:     */       catch (InvalidFormatException e)
/*  667:     */       {
/*  668:1027 */         logger.log(7, new Object[] { "Part name URI '" + sourceURI + "' is not valid ! This message is not intended to be displayed !" });
/*  669:     */         
/*  670:     */ 
/*  671:     */ 
/*  672:1031 */         return;
/*  673:     */       }
/*  674:1033 */       if (sourcePartName.getURI().equals(PackagingURIHelper.PACKAGE_ROOT_URI))
/*  675:     */       {
/*  676:1035 */         clearRelationships();
/*  677:     */       }
/*  678:1036 */       else if (containPart(sourcePartName))
/*  679:     */       {
/*  680:1037 */         PackagePart part = getPart(sourcePartName);
/*  681:1038 */         if (part != null) {
/*  682:1039 */           part.clearRelationships();
/*  683:     */         }
/*  684:     */       }
/*  685:     */     }
/*  686:1044 */     this.isDirty = true;
/*  687:     */   }
/*  688:     */   
/*  689:     */   public void removePartRecursive(PackagePartName partName)
/*  690:     */     throws InvalidFormatException
/*  691:     */   {
/*  692:1061 */     PackagePart relPart = this.partList.get(PackagingURIHelper.getRelationshipPartName(partName));
/*  693:     */     
/*  694:     */ 
/*  695:1064 */     PackagePart partToRemove = this.partList.get(partName);
/*  696:1066 */     if (relPart != null)
/*  697:     */     {
/*  698:1067 */       PackageRelationshipCollection partRels = new PackageRelationshipCollection(partToRemove);
/*  699:1069 */       for (PackageRelationship rel : partRels)
/*  700:     */       {
/*  701:1070 */         PackagePartName partNameToRemove = PackagingURIHelper.createPartName(PackagingURIHelper.resolvePartUri(rel.getSourceURI(), rel.getTargetURI()));
/*  702:     */         
/*  703:     */ 
/*  704:1073 */         removePart(partNameToRemove);
/*  705:     */       }
/*  706:1077 */       removePart(relPart._partName);
/*  707:     */     }
/*  708:1081 */     removePart(partToRemove._partName);
/*  709:     */   }
/*  710:     */   
/*  711:     */   public void deletePart(PackagePartName partName)
/*  712:     */   {
/*  713:1094 */     if (partName == null) {
/*  714:1095 */       throw new IllegalArgumentException("partName");
/*  715:     */     }
/*  716:1099 */     removePart(partName);
/*  717:     */     
/*  718:1101 */     removePart(PackagingURIHelper.getRelationshipPartName(partName));
/*  719:     */   }
/*  720:     */   
/*  721:     */   public void deletePartRecursive(PackagePartName partName)
/*  722:     */   {
/*  723:1115 */     if ((partName == null) || (!containPart(partName))) {
/*  724:1116 */       throw new IllegalArgumentException("partName");
/*  725:     */     }
/*  726:1119 */     PackagePart partToDelete = getPart(partName);
/*  727:     */     
/*  728:1121 */     removePart(partName);
/*  729:     */     try
/*  730:     */     {
/*  731:1124 */       for (PackageRelationship relationship : partToDelete.getRelationships())
/*  732:     */       {
/*  733:1126 */         PackagePartName targetPartName = PackagingURIHelper.createPartName(PackagingURIHelper.resolvePartUri(partName.getURI(), relationship.getTargetURI()));
/*  734:     */         
/*  735:     */ 
/*  736:1129 */         deletePartRecursive(targetPartName);
/*  737:     */       }
/*  738:     */     }
/*  739:     */     catch (InvalidFormatException e)
/*  740:     */     {
/*  741:1132 */       logger.log(5, new Object[] { "An exception occurs while deleting part '" + partName.getName() + "'. Some parts may remain in the package. - " + e.getMessage() });
/*  742:     */       
/*  743:     */ 
/*  744:     */ 
/*  745:1136 */       return;
/*  746:     */     }
/*  747:1139 */     PackagePartName relationshipPartName = PackagingURIHelper.getRelationshipPartName(partName);
/*  748:1141 */     if ((relationshipPartName != null) && (containPart(relationshipPartName))) {
/*  749:1142 */       removePart(relationshipPartName);
/*  750:     */     }
/*  751:     */   }
/*  752:     */   
/*  753:     */   public boolean containPart(PackagePartName partName)
/*  754:     */   {
/*  755:1155 */     return getPart(partName) != null;
/*  756:     */   }
/*  757:     */   
/*  758:     */   public PackageRelationship addRelationship(PackagePartName targetPartName, TargetMode targetMode, String relationshipType, String relID)
/*  759:     */   {
/*  760:1193 */     if ((relationshipType.equals("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties")) && (this.packageProperties != null)) {
/*  761:1195 */       throw new InvalidOperationException("OPC Compliance error [M4.1]: can't add another core properties part ! Use the built-in package method instead.");
/*  762:     */     }
/*  763:1205 */     if (targetPartName.isRelationshipPartURI()) {
/*  764:1206 */       throw new InvalidOperationException("Rule M1.25: The Relationships part shall not have relationships to any other part.");
/*  765:     */     }
/*  766:1212 */     ensureRelationships();
/*  767:1213 */     PackageRelationship retRel = this.relationships.addRelationship(targetPartName.getURI(), targetMode, relationshipType, relID);
/*  768:     */     
/*  769:1215 */     this.isDirty = true;
/*  770:1216 */     return retRel;
/*  771:     */   }
/*  772:     */   
/*  773:     */   public PackageRelationship addRelationship(PackagePartName targetPartName, TargetMode targetMode, String relationshipType)
/*  774:     */   {
/*  775:1233 */     return addRelationship(targetPartName, targetMode, relationshipType, null);
/*  776:     */   }
/*  777:     */   
/*  778:     */   public PackageRelationship addExternalRelationship(String target, String relationshipType)
/*  779:     */   {
/*  780:1255 */     return addExternalRelationship(target, relationshipType, null);
/*  781:     */   }
/*  782:     */   
/*  783:     */   public PackageRelationship addExternalRelationship(String target, String relationshipType, String id)
/*  784:     */   {
/*  785:1278 */     if (target == null) {
/*  786:1279 */       throw new IllegalArgumentException("target");
/*  787:     */     }
/*  788:1281 */     if (relationshipType == null) {
/*  789:1282 */       throw new IllegalArgumentException("relationshipType");
/*  790:     */     }
/*  791:     */     URI targetURI;
/*  792:     */     try
/*  793:     */     {
/*  794:1287 */       targetURI = new URI(target);
/*  795:     */     }
/*  796:     */     catch (URISyntaxException e)
/*  797:     */     {
/*  798:1289 */       throw new IllegalArgumentException("Invalid target - " + e);
/*  799:     */     }
/*  800:1292 */     ensureRelationships();
/*  801:1293 */     PackageRelationship retRel = this.relationships.addRelationship(targetURI, TargetMode.EXTERNAL, relationshipType, id);
/*  802:     */     
/*  803:1295 */     this.isDirty = true;
/*  804:1296 */     return retRel;
/*  805:     */   }
/*  806:     */   
/*  807:     */   public void removeRelationship(String id)
/*  808:     */   {
/*  809:1307 */     if (this.relationships != null)
/*  810:     */     {
/*  811:1308 */       this.relationships.removeRelationship(id);
/*  812:1309 */       this.isDirty = true;
/*  813:     */     }
/*  814:     */   }
/*  815:     */   
/*  816:     */   public PackageRelationshipCollection getRelationships()
/*  817:     */   {
/*  818:1322 */     return getRelationshipsHelper(null);
/*  819:     */   }
/*  820:     */   
/*  821:     */   public PackageRelationshipCollection getRelationshipsByType(String relationshipType)
/*  822:     */   {
/*  823:1335 */     throwExceptionIfWriteOnly();
/*  824:1336 */     if (relationshipType == null) {
/*  825:1337 */       throw new IllegalArgumentException("relationshipType");
/*  826:     */     }
/*  827:1339 */     return getRelationshipsHelper(relationshipType);
/*  828:     */   }
/*  829:     */   
/*  830:     */   private PackageRelationshipCollection getRelationshipsHelper(String id)
/*  831:     */   {
/*  832:1350 */     throwExceptionIfWriteOnly();
/*  833:1351 */     ensureRelationships();
/*  834:1352 */     return this.relationships.getRelationships(id);
/*  835:     */   }
/*  836:     */   
/*  837:     */   public void clearRelationships()
/*  838:     */   {
/*  839:1360 */     if (this.relationships != null)
/*  840:     */     {
/*  841:1361 */       this.relationships.clear();
/*  842:1362 */       this.isDirty = true;
/*  843:     */     }
/*  844:     */   }
/*  845:     */   
/*  846:     */   public void ensureRelationships()
/*  847:     */   {
/*  848:1370 */     if (this.relationships == null) {
/*  849:     */       try
/*  850:     */       {
/*  851:1372 */         this.relationships = new PackageRelationshipCollection(this);
/*  852:     */       }
/*  853:     */       catch (InvalidFormatException e)
/*  854:     */       {
/*  855:1374 */         this.relationships = new PackageRelationshipCollection();
/*  856:     */       }
/*  857:     */     }
/*  858:     */   }
/*  859:     */   
/*  860:     */   public PackageRelationship getRelationship(String id)
/*  861:     */   {
/*  862:1384 */     return this.relationships.getRelationshipByID(id);
/*  863:     */   }
/*  864:     */   
/*  865:     */   public boolean hasRelationships()
/*  866:     */   {
/*  867:1392 */     return this.relationships.size() > 0;
/*  868:     */   }
/*  869:     */   
/*  870:     */   public boolean isRelationshipExists(PackageRelationship rel)
/*  871:     */   {
/*  872:1400 */     for (PackageRelationship r : this.relationships) {
/*  873:1401 */       if (r == rel) {
/*  874:1402 */         return true;
/*  875:     */       }
/*  876:     */     }
/*  877:1405 */     return false;
/*  878:     */   }
/*  879:     */   
/*  880:     */   public void addMarshaller(String contentType, PartMarshaller marshaller)
/*  881:     */   {
/*  882:     */     try
/*  883:     */     {
/*  884:1418 */       this.partMarshallers.put(new ContentType(contentType), marshaller);
/*  885:     */     }
/*  886:     */     catch (InvalidFormatException e)
/*  887:     */     {
/*  888:1420 */       logger.log(5, new Object[] { "The specified content type is not valid: '" + e.getMessage() + "'. The marshaller will not be added !" });
/*  889:     */     }
/*  890:     */   }
/*  891:     */   
/*  892:     */   public void addUnmarshaller(String contentType, PartUnmarshaller unmarshaller)
/*  893:     */   {
/*  894:     */     try
/*  895:     */     {
/*  896:1436 */       this.partUnmarshallers.put(new ContentType(contentType), unmarshaller);
/*  897:     */     }
/*  898:     */     catch (InvalidFormatException e)
/*  899:     */     {
/*  900:1438 */       logger.log(5, new Object[] { "The specified content type is not valid: '" + e.getMessage() + "'. The unmarshaller will not be added !" });
/*  901:     */     }
/*  902:     */   }
/*  903:     */   
/*  904:     */   public void removeMarshaller(String contentType)
/*  905:     */   {
/*  906:     */     try
/*  907:     */     {
/*  908:1452 */       this.partMarshallers.remove(new ContentType(contentType));
/*  909:     */     }
/*  910:     */     catch (InvalidFormatException e)
/*  911:     */     {
/*  912:1454 */       throw new RuntimeException(e);
/*  913:     */     }
/*  914:     */   }
/*  915:     */   
/*  916:     */   public void removeUnmarshaller(String contentType)
/*  917:     */   {
/*  918:     */     try
/*  919:     */     {
/*  920:1466 */       this.partUnmarshallers.remove(new ContentType(contentType));
/*  921:     */     }
/*  922:     */     catch (InvalidFormatException e)
/*  923:     */     {
/*  924:1468 */       throw new RuntimeException(e);
/*  925:     */     }
/*  926:     */   }
/*  927:     */   
/*  928:     */   public PackageAccess getPackageAccess()
/*  929:     */   {
/*  930:1480 */     return this.packageAccess;
/*  931:     */   }
/*  932:     */   
/*  933:     */   @NotImplemented
/*  934:     */   public boolean validatePackage(OPCPackage pkg)
/*  935:     */     throws InvalidFormatException
/*  936:     */   {
/*  937:1490 */     throw new InvalidOperationException("Not implemented yet !!!");
/*  938:     */   }
/*  939:     */   
/*  940:     */   public void save(File targetFile)
/*  941:     */     throws IOException
/*  942:     */   {
/*  943:1503 */     if (targetFile == null) {
/*  944:1504 */       throw new IllegalArgumentException("targetFile");
/*  945:     */     }
/*  946:1507 */     throwExceptionIfReadOnly();
/*  947:1510 */     if ((targetFile.exists()) && (targetFile.getAbsolutePath().equals(this.originalPackagePath))) {
/*  948:1512 */       throw new InvalidOperationException("You can't call save(File) to save to the currently open file. To save to the current file, please just call close()");
/*  949:     */     }
/*  950:1519 */     FileOutputStream fos = null;
/*  951:     */     try
/*  952:     */     {
/*  953:1521 */       fos = new FileOutputStream(targetFile);
/*  954:1522 */       save(fos);
/*  955:     */     }
/*  956:     */     finally
/*  957:     */     {
/*  958:1524 */       if (fos != null) {
/*  959:1525 */         fos.close();
/*  960:     */       }
/*  961:     */     }
/*  962:     */   }
/*  963:     */   
/*  964:     */   public void save(OutputStream outputStream)
/*  965:     */     throws IOException
/*  966:     */   {
/*  967:1538 */     throwExceptionIfReadOnly();
/*  968:1539 */     saveImpl(outputStream);
/*  969:     */   }
/*  970:     */   
/*  971:     */   protected abstract PackagePart createPartImpl(PackagePartName paramPackagePartName, String paramString, boolean paramBoolean);
/*  972:     */   
/*  973:     */   protected abstract void removePartImpl(PackagePartName paramPackagePartName);
/*  974:     */   
/*  975:     */   protected abstract void flushImpl();
/*  976:     */   
/*  977:     */   protected abstract void closeImpl()
/*  978:     */     throws IOException;
/*  979:     */   
/*  980:     */   protected abstract void revertImpl();
/*  981:     */   
/*  982:     */   protected abstract void saveImpl(OutputStream paramOutputStream)
/*  983:     */     throws IOException;
/*  984:     */   
/*  985:     */   protected abstract PackagePart getPartImpl(PackagePartName paramPackagePartName);
/*  986:     */   
/*  987:     */   protected abstract PackagePart[] getPartsImpl()
/*  988:     */     throws InvalidFormatException;
/*  989:     */   
/*  990:     */   public boolean replaceContentType(String oldContentType, String newContentType)
/*  991:     */   {
/*  992:1641 */     boolean success = false;
/*  993:1642 */     ArrayList<PackagePart> list = getPartsByContentType(oldContentType);
/*  994:1643 */     for (PackagePart packagePart : list) {
/*  995:1644 */       if (packagePart.getContentType().equals(oldContentType))
/*  996:     */       {
/*  997:1645 */         PackagePartName partName = packagePart.getPartName();
/*  998:1646 */         this.contentTypeManager.addContentType(partName, newContentType);
/*  999:     */         try
/* 1000:     */         {
/* 1001:1648 */           packagePart.setContentType(newContentType);
/* 1002:     */         }
/* 1003:     */         catch (InvalidFormatException e)
/* 1004:     */         {
/* 1005:1650 */           throw new OpenXML4JRuntimeException("invalid content type - " + newContentType, e);
/* 1006:     */         }
/* 1007:1652 */         success = true;
/* 1008:1653 */         this.isDirty = true;
/* 1009:     */       }
/* 1010:     */     }
/* 1011:1656 */     return success;
/* 1012:     */   }
/* 1013:     */   
/* 1014:     */   public void registerPartAndContentType(PackagePart part)
/* 1015:     */   {
/* 1016:1667 */     addPackagePart(part);
/* 1017:1668 */     this.contentTypeManager.addContentType(part.getPartName(), part.getContentType());
/* 1018:1669 */     this.isDirty = true;
/* 1019:     */   }
/* 1020:     */   
/* 1021:     */   public void unregisterPartAndContentType(PackagePartName partName)
/* 1022:     */   {
/* 1023:1680 */     removePart(partName);
/* 1024:1681 */     this.contentTypeManager.removeContentType(partName);
/* 1025:1682 */     this.isDirty = true;
/* 1026:     */   }
/* 1027:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.OPCPackage
 * JD-Core Version:    0.7.0.1
 */