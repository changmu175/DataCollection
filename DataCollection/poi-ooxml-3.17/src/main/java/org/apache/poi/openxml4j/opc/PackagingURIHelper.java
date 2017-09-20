/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.net.URISyntaxException;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import java.nio.charset.Charset;
/*   7:    */ import java.util.regex.Matcher;
/*   8:    */ import java.util.regex.Pattern;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  10:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  11:    */ import org.apache.poi.util.POILogFactory;
/*  12:    */ import org.apache.poi.util.POILogger;
/*  13:    */ 
/*  14:    */ public final class PackagingURIHelper
/*  15:    */ {
/*  16: 38 */   private static final POILogger _logger = POILogFactory.getLogger(PackagingURIHelper.class);
/*  17:    */   private static URI packageRootUri;
/*  18:    */   public static final String RELATIONSHIP_PART_EXTENSION_NAME;
/*  19:107 */   public static final String RELATIONSHIP_PART_SEGMENT_NAME = "_rels";
/*  20:    */   public static final String PACKAGE_PROPERTIES_SEGMENT_NAME;
/*  21:    */   public static final String PACKAGE_CORE_PROPERTIES_NAME;
/*  22:    */   public static final char FORWARD_SLASH_CHAR;
/*  23:    */   public static final String FORWARD_SLASH_STRING;
/*  24:    */   public static final URI PACKAGE_RELATIONSHIPS_ROOT_URI;
/*  25:    */   public static final PackagePartName PACKAGE_RELATIONSHIPS_ROOT_PART_NAME;
/*  26:    */   public static final URI CORE_PROPERTIES_URI;
/*  27:    */   public static final PackagePartName CORE_PROPERTIES_PART_NAME;
/*  28:    */   public static final URI PACKAGE_ROOT_URI;
/*  29:    */   public static final PackagePartName PACKAGE_ROOT_PART_NAME;
/*  30:    */   
/*  31:    */   static
/*  32:    */   {
/*  33:108 */     RELATIONSHIP_PART_EXTENSION_NAME = ".rels";
/*  34:109 */     FORWARD_SLASH_CHAR = '/';
/*  35:110 */     FORWARD_SLASH_STRING = "/";
/*  36:111 */     PACKAGE_PROPERTIES_SEGMENT_NAME = "docProps";
/*  37:112 */     PACKAGE_CORE_PROPERTIES_NAME = "core.xml";
/*  38:    */     
/*  39:    */ 
/*  40:115 */     URI uriPACKAGE_ROOT_URI = null;
/*  41:116 */     URI uriPACKAGE_RELATIONSHIPS_ROOT_URI = null;
/*  42:117 */     URI uriPACKAGE_PROPERTIES_URI = null;
/*  43:    */     try
/*  44:    */     {
/*  45:119 */       uriPACKAGE_ROOT_URI = new URI("/");
/*  46:120 */       uriPACKAGE_RELATIONSHIPS_ROOT_URI = new URI(FORWARD_SLASH_CHAR + RELATIONSHIP_PART_SEGMENT_NAME + FORWARD_SLASH_CHAR + RELATIONSHIP_PART_EXTENSION_NAME);
/*  47:    */       
/*  48:    */ 
/*  49:123 */       packageRootUri = new URI("/");
/*  50:124 */       uriPACKAGE_PROPERTIES_URI = new URI(FORWARD_SLASH_CHAR + PACKAGE_PROPERTIES_SEGMENT_NAME + FORWARD_SLASH_CHAR + PACKAGE_CORE_PROPERTIES_NAME);
/*  51:    */     }
/*  52:    */     catch (URISyntaxException e) {}
/*  53:130 */     PACKAGE_ROOT_URI = uriPACKAGE_ROOT_URI;
/*  54:131 */     PACKAGE_RELATIONSHIPS_ROOT_URI = uriPACKAGE_RELATIONSHIPS_ROOT_URI;
/*  55:132 */     CORE_PROPERTIES_URI = uriPACKAGE_PROPERTIES_URI;
/*  56:    */     
/*  57:    */ 
/*  58:135 */     PackagePartName tmpPACKAGE_ROOT_PART_NAME = null;
/*  59:136 */     PackagePartName tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME = null;
/*  60:137 */     PackagePartName tmpCORE_PROPERTIES_URI = null;
/*  61:    */     try
/*  62:    */     {
/*  63:139 */       tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME = createPartName(PACKAGE_RELATIONSHIPS_ROOT_URI);
/*  64:140 */       tmpCORE_PROPERTIES_URI = createPartName(CORE_PROPERTIES_URI);
/*  65:141 */       tmpPACKAGE_ROOT_PART_NAME = new PackagePartName(PACKAGE_ROOT_URI, false);
/*  66:    */     }
/*  67:    */     catch (InvalidFormatException e) {}
/*  68:146 */     PACKAGE_RELATIONSHIPS_ROOT_PART_NAME = tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME;
/*  69:147 */     CORE_PROPERTIES_PART_NAME = tmpCORE_PROPERTIES_URI;
/*  70:148 */     PACKAGE_ROOT_PART_NAME = tmpPACKAGE_ROOT_PART_NAME;
/*  71:    */   }
/*  72:    */   
/*  73:151 */   private static final Pattern missingAuthPattern = Pattern.compile("\\w+://");
/*  74:    */   
/*  75:    */   public static URI getPackageRootUri()
/*  76:    */   {
/*  77:159 */     return packageRootUri;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static boolean isRelationshipPartURI(URI partUri)
/*  81:    */   {
/*  82:170 */     if (partUri == null) {
/*  83:171 */       throw new IllegalArgumentException("partUri");
/*  84:    */     }
/*  85:173 */     return partUri.getPath().matches(".*" + RELATIONSHIP_PART_SEGMENT_NAME + ".*" + RELATIONSHIP_PART_EXTENSION_NAME + "$");
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static String getFilename(URI uri)
/*  89:    */   {
/*  90:182 */     if (uri != null)
/*  91:    */     {
/*  92:183 */       String path = uri.getPath();
/*  93:184 */       int len = path.length();
/*  94:185 */       int num2 = len;
/*  95:    */       for (;;)
/*  96:    */       {
/*  97:186 */         num2--;
/*  98:186 */         if (num2 < 0) {
/*  99:    */           break;
/* 100:    */         }
/* 101:187 */         char ch1 = path.charAt(num2);
/* 102:188 */         if (ch1 == FORWARD_SLASH_CHAR) {
/* 103:189 */           return path.substring(num2 + 1, len);
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:192 */     return "";
/* 108:    */   }
/* 109:    */   
/* 110:    */   public static String getFilenameWithoutExtension(URI uri)
/* 111:    */   {
/* 112:199 */     String filename = getFilename(uri);
/* 113:200 */     int dotIndex = filename.lastIndexOf(".");
/* 114:201 */     if (dotIndex == -1) {
/* 115:202 */       return filename;
/* 116:    */     }
/* 117:203 */     return filename.substring(0, dotIndex);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static URI getPath(URI uri)
/* 121:    */   {
/* 122:210 */     if (uri != null)
/* 123:    */     {
/* 124:211 */       String path = uri.getPath();
/* 125:212 */       int len = path.length();
/* 126:213 */       int num2 = len;
/* 127:    */       for (;;)
/* 128:    */       {
/* 129:214 */         num2--;
/* 130:214 */         if (num2 < 0) {
/* 131:    */           break;
/* 132:    */         }
/* 133:215 */         char ch1 = path.charAt(num2);
/* 134:216 */         if (ch1 == FORWARD_SLASH_CHAR) {
/* 135:    */           try
/* 136:    */           {
/* 137:218 */             return new URI(path.substring(0, num2));
/* 138:    */           }
/* 139:    */           catch (URISyntaxException e)
/* 140:    */           {
/* 141:220 */             return null;
/* 142:    */           }
/* 143:    */         }
/* 144:    */       }
/* 145:    */     }
/* 146:225 */     return null;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static URI combine(URI prefix, URI suffix)
/* 150:    */   {
/* 151:237 */     URI retUri = null;
/* 152:    */     try
/* 153:    */     {
/* 154:239 */       retUri = new URI(combine(prefix.getPath(), suffix.getPath()));
/* 155:    */     }
/* 156:    */     catch (URISyntaxException e)
/* 157:    */     {
/* 158:241 */       throw new IllegalArgumentException("Prefix and suffix can't be combine !");
/* 159:    */     }
/* 160:244 */     return retUri;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public static String combine(String prefix, String suffix)
/* 164:    */   {
/* 165:251 */     if ((!prefix.endsWith("" + FORWARD_SLASH_CHAR)) && (!suffix.startsWith("" + FORWARD_SLASH_CHAR))) {
/* 166:253 */       return prefix + FORWARD_SLASH_CHAR + suffix;
/* 167:    */     }
/* 168:254 */     if (((!prefix.endsWith("" + FORWARD_SLASH_CHAR)) && (suffix.startsWith("" + FORWARD_SLASH_CHAR))) || ((prefix.endsWith("" + FORWARD_SLASH_CHAR)) && (!suffix.startsWith("" + FORWARD_SLASH_CHAR)))) {
/* 169:258 */       return prefix + suffix;
/* 170:    */     }
/* 171:260 */     return "";
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static URI relativizeURI(URI sourceURI, URI targetURI, boolean msCompatible)
/* 175:    */   {
/* 176:278 */     StringBuilder retVal = new StringBuilder();
/* 177:279 */     String[] segmentsSource = sourceURI.getPath().split("/", -1);
/* 178:280 */     String[] segmentsTarget = targetURI.getPath().split("/", -1);
/* 179:283 */     if (segmentsSource.length == 0) {
/* 180:284 */       throw new IllegalArgumentException("Can't relativize an empty source URI !");
/* 181:    */     }
/* 182:289 */     if (segmentsTarget.length == 0) {
/* 183:290 */       throw new IllegalArgumentException("Can't relativize an empty target URI !");
/* 184:    */     }
/* 185:296 */     if (sourceURI.toString().equals("/"))
/* 186:    */     {
/* 187:297 */       String path = targetURI.getPath();
/* 188:298 */       if ((msCompatible) && (path.length() > 0) && (path.charAt(0) == '/')) {
/* 189:    */         try
/* 190:    */         {
/* 191:300 */           targetURI = new URI(path.substring(1));
/* 192:    */         }
/* 193:    */         catch (Exception e)
/* 194:    */         {
/* 195:302 */           _logger.log(5, new Object[] { e });
/* 196:303 */           return null;
/* 197:    */         }
/* 198:    */       }
/* 199:306 */       return targetURI;
/* 200:    */     }
/* 201:313 */     int segmentsTheSame = 0;
/* 202:314 */     for (int i = 0; (i < segmentsSource.length) && (i < segmentsTarget.length); i++)
/* 203:    */     {
/* 204:315 */       if (!segmentsSource[i].equals(segmentsTarget[i])) {
/* 205:    */         break;
/* 206:    */       }
/* 207:317 */       segmentsTheSame++;
/* 208:    */     }
/* 209:324 */     if (((segmentsTheSame == 0) || (segmentsTheSame == 1)) && (segmentsSource[0].equals("")) && (segmentsTarget[0].equals("")))
/* 210:    */     {
/* 211:326 */       for (int i = 0; i < segmentsSource.length - 2; i++) {
/* 212:327 */         retVal.append("../");
/* 213:    */       }
/* 214:329 */       for (int i = 0; i < segmentsTarget.length; i++) {
/* 215:330 */         if (!segmentsTarget[i].equals(""))
/* 216:    */         {
/* 217:332 */           retVal.append(segmentsTarget[i]);
/* 218:333 */           if (i != segmentsTarget.length - 1) {
/* 219:334 */             retVal.append("/");
/* 220:    */           }
/* 221:    */         }
/* 222:    */       }
/* 223:    */       try
/* 224:    */       {
/* 225:338 */         return new URI(retVal.toString());
/* 226:    */       }
/* 227:    */       catch (Exception e)
/* 228:    */       {
/* 229:340 */         _logger.log(5, new Object[] { e });
/* 230:341 */         return null;
/* 231:    */       }
/* 232:    */     }
/* 233:346 */     if ((segmentsTheSame == segmentsSource.length) && (segmentsTheSame == segmentsTarget.length))
/* 234:    */     {
/* 235:348 */       if (sourceURI.equals(targetURI)) {
/* 236:353 */         retVal.append(segmentsSource[(segmentsSource.length - 1)]);
/* 237:    */       } else {
/* 238:355 */         retVal.append("");
/* 239:    */       }
/* 240:    */     }
/* 241:    */     else
/* 242:    */     {
/* 243:364 */       if (segmentsTheSame == 1) {
/* 244:365 */         retVal.append("/");
/* 245:    */       } else {
/* 246:367 */         for (int j = segmentsTheSame; j < segmentsSource.length - 1; j++) {
/* 247:368 */           retVal.append("../");
/* 248:    */         }
/* 249:    */       }
/* 250:373 */       for (int j = segmentsTheSame; j < segmentsTarget.length; j++)
/* 251:    */       {
/* 252:374 */         if ((retVal.length() > 0) && (retVal.charAt(retVal.length() - 1) != '/')) {
/* 253:376 */           retVal.append("/");
/* 254:    */         }
/* 255:378 */         retVal.append(segmentsTarget[j]);
/* 256:    */       }
/* 257:    */     }
/* 258:383 */     String fragment = targetURI.getRawFragment();
/* 259:384 */     if (fragment != null) {
/* 260:385 */       retVal.append("#").append(fragment);
/* 261:    */     }
/* 262:    */     try
/* 263:    */     {
/* 264:389 */       return new URI(retVal.toString());
/* 265:    */     }
/* 266:    */     catch (Exception e)
/* 267:    */     {
/* 268:391 */       _logger.log(5, new Object[] { e });
/* 269:    */     }
/* 270:392 */     return null;
/* 271:    */   }
/* 272:    */   
/* 273:    */   public static URI relativizeURI(URI sourceURI, URI targetURI)
/* 274:    */   {
/* 275:408 */     return relativizeURI(sourceURI, targetURI, false);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public static URI resolvePartUri(URI sourcePartUri, URI targetUri)
/* 279:    */   {
/* 280:421 */     if ((sourcePartUri == null) || (sourcePartUri.isAbsolute())) {
/* 281:422 */       throw new IllegalArgumentException("sourcePartUri invalid - " + sourcePartUri);
/* 282:    */     }
/* 283:426 */     if ((targetUri == null) || (targetUri.isAbsolute())) {
/* 284:427 */       throw new IllegalArgumentException("targetUri invalid - " + targetUri);
/* 285:    */     }
/* 286:431 */     return sourcePartUri.resolve(targetUri);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public static URI getURIFromPath(String path)
/* 290:    */   {
/* 291:    */     URI retUri;
/* 292:    */     try
/* 293:    */     {
/* 294:440 */       retUri = toURI(path);
/* 295:    */     }
/* 296:    */     catch (URISyntaxException e)
/* 297:    */     {
/* 298:442 */       throw new IllegalArgumentException("path");
/* 299:    */     }
/* 300:444 */     return retUri;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public static URI getSourcePartUriFromRelationshipPartUri(URI relationshipPartUri)
/* 304:    */   {
/* 305:456 */     if (relationshipPartUri == null) {
/* 306:457 */       throw new IllegalArgumentException("Must not be null");
/* 307:    */     }
/* 308:460 */     if (!isRelationshipPartURI(relationshipPartUri)) {
/* 309:461 */       throw new IllegalArgumentException("Must be a relationship part");
/* 310:    */     }
/* 311:464 */     if (relationshipPartUri.compareTo(PACKAGE_RELATIONSHIPS_ROOT_URI) == 0) {
/* 312:465 */       return PACKAGE_ROOT_URI;
/* 313:    */     }
/* 314:467 */     String filename = relationshipPartUri.getPath();
/* 315:468 */     String filenameWithoutExtension = getFilenameWithoutExtension(relationshipPartUri);
/* 316:469 */     filename = filename.substring(0, filename.length() - filenameWithoutExtension.length() - RELATIONSHIP_PART_EXTENSION_NAME.length());
/* 317:    */     
/* 318:    */ 
/* 319:472 */     filename = filename.substring(0, filename.length() - RELATIONSHIP_PART_SEGMENT_NAME.length() - 1);
/* 320:    */     
/* 321:474 */     filename = combine(filename, filenameWithoutExtension);
/* 322:475 */     return getURIFromPath(filename);
/* 323:    */   }
/* 324:    */   
/* 325:    */   public static PackagePartName createPartName(URI partUri)
/* 326:    */     throws InvalidFormatException
/* 327:    */   {
/* 328:490 */     if (partUri == null) {
/* 329:491 */       throw new IllegalArgumentException("partName");
/* 330:    */     }
/* 331:493 */     return new PackagePartName(partUri, true);
/* 332:    */   }
/* 333:    */   
/* 334:    */   public static PackagePartName createPartName(String partName)
/* 335:    */     throws InvalidFormatException
/* 336:    */   {
/* 337:    */     URI partNameURI;
/* 338:    */     try
/* 339:    */     {
/* 340:510 */       partNameURI = toURI(partName);
/* 341:    */     }
/* 342:    */     catch (URISyntaxException e)
/* 343:    */     {
/* 344:512 */       throw new InvalidFormatException(e.getMessage());
/* 345:    */     }
/* 346:514 */     return createPartName(partNameURI);
/* 347:    */   }
/* 348:    */   
/* 349:    */   public static PackagePartName createPartName(String partName, PackagePart relativePart)
/* 350:    */     throws InvalidFormatException
/* 351:    */   {
/* 352:    */     URI newPartNameURI;
/* 353:    */     try
/* 354:    */     {
/* 355:533 */       newPartNameURI = resolvePartUri(relativePart.getPartName().getURI(), new URI(partName));
/* 356:    */     }
/* 357:    */     catch (URISyntaxException e)
/* 358:    */     {
/* 359:536 */       throw new InvalidFormatException(e.getMessage());
/* 360:    */     }
/* 361:538 */     return createPartName(newPartNameURI);
/* 362:    */   }
/* 363:    */   
/* 364:    */   public static PackagePartName createPartName(URI partName, PackagePart relativePart)
/* 365:    */     throws InvalidFormatException
/* 366:    */   {
/* 367:555 */     URI newPartNameURI = resolvePartUri(relativePart.getPartName().getURI(), partName);
/* 368:    */     
/* 369:557 */     return createPartName(newPartNameURI);
/* 370:    */   }
/* 371:    */   
/* 372:    */   public static boolean isValidPartName(URI partUri)
/* 373:    */   {
/* 374:585 */     if (partUri == null) {
/* 375:586 */       throw new IllegalArgumentException("partUri");
/* 376:    */     }
/* 377:    */     try
/* 378:    */     {
/* 379:589 */       createPartName(partUri);
/* 380:590 */       return true;
/* 381:    */     }
/* 382:    */     catch (Exception e) {}
/* 383:592 */     return false;
/* 384:    */   }
/* 385:    */   
/* 386:    */   public static String decodeURI(URI uri)
/* 387:    */   {
/* 388:606 */     StringBuffer retVal = new StringBuffer();
/* 389:607 */     String uriStr = uri.toASCIIString();
/* 390:    */     
/* 391:609 */     int length = uriStr.length();
/* 392:610 */     for (int i = 0; i < length; i++)
/* 393:    */     {
/* 394:611 */       char c = uriStr.charAt(i);
/* 395:612 */       if (c == '%')
/* 396:    */       {
/* 397:615 */         if (length - i < 2) {
/* 398:616 */           throw new IllegalArgumentException("The uri " + uriStr + " contain invalid encoded character !");
/* 399:    */         }
/* 400:621 */         char decodedChar = (char)Integer.parseInt(uriStr.substring(i + 1, i + 3), 16);
/* 401:    */         
/* 402:623 */         retVal.append(decodedChar);
/* 403:624 */         i += 2;
/* 404:    */       }
/* 405:    */       else
/* 406:    */       {
/* 407:627 */         retVal.append(c);
/* 408:    */       }
/* 409:    */     }
/* 410:629 */     return retVal.toString();
/* 411:    */   }
/* 412:    */   
/* 413:    */   public static PackagePartName getRelationshipPartName(PackagePartName partName)
/* 414:    */   {
/* 415:644 */     if (partName == null) {
/* 416:645 */       throw new IllegalArgumentException("partName");
/* 417:    */     }
/* 418:647 */     if (PACKAGE_ROOT_URI.getPath().equals(partName.getURI().getPath())) {
/* 419:649 */       return PACKAGE_RELATIONSHIPS_ROOT_PART_NAME;
/* 420:    */     }
/* 421:651 */     if (partName.isRelationshipPartURI()) {
/* 422:652 */       throw new InvalidOperationException("Can't be a relationship part");
/* 423:    */     }
/* 424:654 */     String fullPath = partName.getURI().getPath();
/* 425:655 */     String filename = getFilename(partName.getURI());
/* 426:656 */     fullPath = fullPath.substring(0, fullPath.length() - filename.length());
/* 427:657 */     fullPath = combine(fullPath, RELATIONSHIP_PART_SEGMENT_NAME);
/* 428:    */     
/* 429:659 */     fullPath = combine(fullPath, filename);
/* 430:660 */     fullPath = fullPath + RELATIONSHIP_PART_EXTENSION_NAME;
/* 431:    */     PackagePartName retPartName;
/* 432:    */     try
/* 433:    */     {
/* 434:665 */       retPartName = createPartName(fullPath);
/* 435:    */     }
/* 436:    */     catch (InvalidFormatException e)
/* 437:    */     {
/* 438:669 */       return null;
/* 439:    */     }
/* 440:671 */     return retPartName;
/* 441:    */   }
/* 442:    */   
/* 443:    */   public static URI toURI(String value)
/* 444:    */     throws URISyntaxException
/* 445:    */   {
/* 446:699 */     if (value.indexOf("\\") != -1) {
/* 447:700 */       value = value.replace('\\', '/');
/* 448:    */     }
/* 449:705 */     int fragmentIdx = value.indexOf('#');
/* 450:706 */     if (fragmentIdx != -1)
/* 451:    */     {
/* 452:707 */       String path = value.substring(0, fragmentIdx);
/* 453:708 */       String fragment = value.substring(fragmentIdx + 1);
/* 454:    */       
/* 455:710 */       value = path + "#" + encode(fragment);
/* 456:    */     }
/* 457:714 */     if (value.length() > 0)
/* 458:    */     {
/* 459:715 */       StringBuilder b = new StringBuilder();
/* 460:716 */       for (int idx = value.length() - 1; idx >= 0; idx--)
/* 461:    */       {
/* 462:718 */         char c = value.charAt(idx);
/* 463:719 */         if ((!Character.isWhitespace(c)) && (c != ' ')) {
/* 464:    */           break;
/* 465:    */         }
/* 466:720 */         b.append(c);
/* 467:    */       }
/* 468:725 */       if (b.length() > 0) {
/* 469:726 */         value = value.substring(0, idx + 1) + encode(b.reverse().toString());
/* 470:    */       }
/* 471:    */     }
/* 472:732 */     if (missingAuthPattern.matcher(value).matches()) {
/* 473:733 */       value = value + "/";
/* 474:    */     }
/* 475:735 */     return new URI(value);
/* 476:    */   }
/* 477:    */   
/* 478:    */   public static String encode(String s)
/* 479:    */   {
/* 480:749 */     int n = s.length();
/* 481:750 */     if (n == 0) {
/* 482:750 */       return s;
/* 483:    */     }
/* 484:752 */     ByteBuffer bb = ByteBuffer.wrap(s.getBytes(Charset.forName("UTF-8")));
/* 485:753 */     StringBuilder sb = new StringBuilder();
/* 486:754 */     while (bb.hasRemaining())
/* 487:    */     {
/* 488:755 */       int b = bb.get() & 0xFF;
/* 489:756 */       if (isUnsafe(b))
/* 490:    */       {
/* 491:757 */         sb.append('%');
/* 492:758 */         sb.append(hexDigits[(b >> 4 & 0xF)]);
/* 493:759 */         sb.append(hexDigits[(b >> 0 & 0xF)]);
/* 494:    */       }
/* 495:    */       else
/* 496:    */       {
/* 497:761 */         sb.append((char)b);
/* 498:    */       }
/* 499:    */     }
/* 500:764 */     return sb.toString();
/* 501:    */   }
/* 502:    */   
/* 503:767 */   private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/* 504:    */   
/* 505:    */   private static boolean isUnsafe(int ch)
/* 506:    */   {
/* 507:773 */     return (ch > 128) || (Character.isWhitespace(ch));
/* 508:    */   }
/* 509:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackagingURIHelper
 * JD-Core Version:    0.7.0.1
 */