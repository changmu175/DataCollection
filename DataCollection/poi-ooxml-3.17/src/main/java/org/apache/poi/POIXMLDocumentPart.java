/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.URI;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  13:    */ import org.apache.poi.openxml4j.exceptions.PartAlreadyExistsException;
/*  14:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  20:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  21:    */ import org.apache.poi.util.Internal;
/*  22:    */ import org.apache.poi.util.POILogFactory;
/*  23:    */ import org.apache.poi.util.POILogger;
/*  24:    */ 
/*  25:    */ public class POIXMLDocumentPart
/*  26:    */ {
/*  27: 52 */   private static final POILogger logger = POILogFactory.getLogger(POIXMLDocumentPart.class);
/*  28: 54 */   private String coreDocumentRel = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";
/*  29:    */   private PackagePart packagePart;
/*  30:    */   private POIXMLDocumentPart parent;
/*  31: 57 */   private Map<String, RelationPart> relations = new LinkedHashMap();
/*  32:    */   
/*  33:    */   public static class RelationPart
/*  34:    */   {
/*  35:    */     private final PackageRelationship relationship;
/*  36:    */     private final POIXMLDocumentPart documentPart;
/*  37:    */     
/*  38:    */     RelationPart(PackageRelationship relationship, POIXMLDocumentPart documentPart)
/*  39:    */     {
/*  40: 70 */       this.relationship = relationship;
/*  41: 71 */       this.documentPart = documentPart;
/*  42:    */     }
/*  43:    */     
/*  44:    */     public PackageRelationship getRelationship()
/*  45:    */     {
/*  46: 78 */       return this.relationship;
/*  47:    */     }
/*  48:    */     
/*  49:    */     public <T extends POIXMLDocumentPart> T getDocumentPart()
/*  50:    */     {
/*  51: 88 */       return this.documentPart;
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55: 96 */   private int relationCounter = 0;
/*  56:    */   
/*  57:    */   int incrementRelationCounter()
/*  58:    */   {
/*  59: 99 */     this.relationCounter += 1;
/*  60:100 */     return this.relationCounter;
/*  61:    */   }
/*  62:    */   
/*  63:    */   int decrementRelationCounter()
/*  64:    */   {
/*  65:104 */     this.relationCounter -= 1;
/*  66:105 */     return this.relationCounter;
/*  67:    */   }
/*  68:    */   
/*  69:    */   int getRelationCounter()
/*  70:    */   {
/*  71:109 */     return this.relationCounter;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public POIXMLDocumentPart(OPCPackage pkg)
/*  75:    */   {
/*  76:118 */     this(pkg, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
/*  77:    */   }
/*  78:    */   
/*  79:    */   public POIXMLDocumentPart(OPCPackage pkg, String coreDocumentRel)
/*  80:    */   {
/*  81:128 */     this(getPartFromOPCPackage(pkg, coreDocumentRel));
/*  82:129 */     this.coreDocumentRel = coreDocumentRel;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public POIXMLDocumentPart() {}
/*  86:    */   
/*  87:    */   public POIXMLDocumentPart(PackagePart part)
/*  88:    */   {
/*  89:150 */     this(null, part);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public POIXMLDocumentPart(POIXMLDocumentPart parent, PackagePart part)
/*  93:    */   {
/*  94:164 */     this.packagePart = part;
/*  95:165 */     this.parent = parent;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected final void rebase(OPCPackage pkg)
/*  99:    */     throws InvalidFormatException
/* 100:    */   {
/* 101:179 */     PackageRelationshipCollection cores = this.packagePart.getRelationshipsByType(this.coreDocumentRel);
/* 102:181 */     if (cores.size() != 1) {
/* 103:182 */       throw new IllegalStateException("Tried to rebase using " + this.coreDocumentRel + " but found " + cores.size() + " parts of the right type");
/* 104:    */     }
/* 105:187 */     this.packagePart = this.packagePart.getRelatedPart(cores.getRelationship(0));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public final PackagePart getPackagePart()
/* 109:    */   {
/* 110:196 */     return this.packagePart;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public final List<POIXMLDocumentPart> getRelations()
/* 114:    */   {
/* 115:205 */     List<POIXMLDocumentPart> l = new ArrayList();
/* 116:206 */     for (RelationPart rp : this.relations.values()) {
/* 117:207 */       l.add(rp.getDocumentPart());
/* 118:    */     }
/* 119:209 */     return Collections.unmodifiableList(l);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public final List<RelationPart> getRelationParts()
/* 123:    */   {
/* 124:218 */     List<RelationPart> l = new ArrayList(this.relations.values());
/* 125:219 */     return Collections.unmodifiableList(l);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public final POIXMLDocumentPart getRelationById(String id)
/* 129:    */   {
/* 130:234 */     RelationPart rp = (RelationPart)this.relations.get(id);
/* 131:235 */     return rp == null ? null : rp.getDocumentPart();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public final String getRelationId(POIXMLDocumentPart part)
/* 135:    */   {
/* 136:251 */     for (RelationPart rp : this.relations.values()) {
/* 137:252 */       if (rp.getDocumentPart() == part) {
/* 138:253 */         return rp.getRelationship().getId();
/* 139:    */       }
/* 140:    */     }
/* 141:256 */     return null;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public final RelationPart addRelation(String relId, POIXMLRelation relationshipType, POIXMLDocumentPart part)
/* 145:    */   {
/* 146:271 */     PackageRelationship pr = this.packagePart.findExistingRelation(part.getPackagePart());
/* 147:272 */     if (pr == null)
/* 148:    */     {
/* 149:273 */       PackagePartName ppn = part.getPackagePart().getPartName();
/* 150:274 */       String relType = relationshipType.getRelation();
/* 151:275 */       pr = this.packagePart.addRelationship(ppn, TargetMode.INTERNAL, relType, relId);
/* 152:    */     }
/* 153:277 */     addRelation(pr, part);
/* 154:278 */     return new RelationPart(pr, part);
/* 155:    */   }
/* 156:    */   
/* 157:    */   private void addRelation(PackageRelationship pr, POIXMLDocumentPart part)
/* 158:    */   {
/* 159:288 */     this.relations.put(pr.getId(), new RelationPart(pr, part));
/* 160:289 */     part.incrementRelationCounter();
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected final void removeRelation(POIXMLDocumentPart part)
/* 164:    */   {
/* 165:300 */     removeRelation(part, true);
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected final boolean removeRelation(POIXMLDocumentPart part, boolean removeUnusedParts)
/* 169:    */   {
/* 170:315 */     String id = getRelationId(part);
/* 171:316 */     if (id == null) {
/* 172:318 */       return false;
/* 173:    */     }
/* 174:321 */     part.decrementRelationCounter();
/* 175:    */     
/* 176:323 */     getPackagePart().removeRelationship(id);
/* 177:    */     
/* 178:325 */     this.relations.remove(id);
/* 179:327 */     if (removeUnusedParts) {
/* 180:329 */       if (part.getRelationCounter() == 0)
/* 181:    */       {
/* 182:    */         try
/* 183:    */         {
/* 184:331 */           part.onDocumentRemove();
/* 185:    */         }
/* 186:    */         catch (IOException e)
/* 187:    */         {
/* 188:333 */           throw new POIXMLException(e);
/* 189:    */         }
/* 190:335 */         getPackagePart().getPackage().removePart(part.getPackagePart());
/* 191:    */       }
/* 192:    */     }
/* 193:338 */     return true;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public final POIXMLDocumentPart getParent()
/* 197:    */   {
/* 198:347 */     return this.parent;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public String toString()
/* 202:    */   {
/* 203:352 */     return this.packagePart == null ? "" : this.packagePart.toString();
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected void commit()
/* 207:    */     throws IOException
/* 208:    */   {}
/* 209:    */   
/* 210:    */   protected final void onSave(Set<PackagePart> alreadySaved)
/* 211:    */     throws IOException
/* 212:    */   {
/* 213:388 */     prepareForCommit();
/* 214:    */     
/* 215:390 */     commit();
/* 216:391 */     alreadySaved.add(getPackagePart());
/* 217:392 */     for (RelationPart rp : this.relations.values())
/* 218:    */     {
/* 219:393 */       POIXMLDocumentPart p = rp.getDocumentPart();
/* 220:394 */       if (!alreadySaved.contains(p.getPackagePart())) {
/* 221:395 */         p.onSave(alreadySaved);
/* 222:    */       }
/* 223:    */     }
/* 224:    */   }
/* 225:    */   
/* 226:    */   protected void prepareForCommit()
/* 227:    */   {
/* 228:408 */     PackagePart part = getPackagePart();
/* 229:409 */     if (part != null) {
/* 230:410 */       part.clear();
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   public final POIXMLDocumentPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory)
/* 235:    */   {
/* 236:426 */     return createRelationship(descriptor, factory, -1, false).getDocumentPart();
/* 237:    */   }
/* 238:    */   
/* 239:    */   public final POIXMLDocumentPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory, int idx)
/* 240:    */   {
/* 241:442 */     return createRelationship(descriptor, factory, idx, false).getDocumentPart();
/* 242:    */   }
/* 243:    */   
/* 244:    */   protected final int getNextPartNumber(POIXMLRelation descriptor, int minIdx)
/* 245:    */   {
/* 246:462 */     OPCPackage pkg = this.packagePart.getPackage();
/* 247:    */     try
/* 248:    */     {
/* 249:465 */       String name = descriptor.getDefaultFileName();
/* 250:466 */       if (name.equals(descriptor.getFileName(9999)))
/* 251:    */       {
/* 252:468 */         PackagePartName ppName = PackagingURIHelper.createPartName(name);
/* 253:469 */         if (pkg.containPart(ppName)) {
/* 254:471 */           return -1;
/* 255:    */         }
/* 256:474 */         return 0;
/* 257:    */       }
/* 258:479 */       int idx = minIdx < 0 ? 1 : minIdx;
/* 259:480 */       int maxIdx = minIdx + pkg.getParts().size();
/* 260:481 */       while (idx <= maxIdx)
/* 261:    */       {
/* 262:482 */         name = descriptor.getFileName(idx);
/* 263:483 */         PackagePartName ppName = PackagingURIHelper.createPartName(name);
/* 264:484 */         if (!pkg.containPart(ppName)) {
/* 265:485 */           return idx;
/* 266:    */         }
/* 267:487 */         idx++;
/* 268:    */       }
/* 269:    */     }
/* 270:    */     catch (InvalidFormatException e)
/* 271:    */     {
/* 272:491 */       throw new POIXMLException(e);
/* 273:    */     }
/* 274:493 */     return -1;
/* 275:    */   }
/* 276:    */   
/* 277:    */   protected final RelationPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory, int idx, boolean noRelation)
/* 278:    */   {
/* 279:    */     try
/* 280:    */     {
/* 281:511 */       PackagePartName ppName = PackagingURIHelper.createPartName(descriptor.getFileName(idx));
/* 282:512 */       PackageRelationship rel = null;
/* 283:513 */       PackagePart part = this.packagePart.getPackage().createPart(ppName, descriptor.getContentType());
/* 284:514 */       if (!noRelation) {
/* 285:516 */         rel = this.packagePart.addRelationship(ppName, TargetMode.INTERNAL, descriptor.getRelation());
/* 286:    */       }
/* 287:518 */       POIXMLDocumentPart doc = factory.newDocumentPart(descriptor);
/* 288:519 */       doc.packagePart = part;
/* 289:520 */       doc.parent = this;
/* 290:521 */       if (!noRelation) {
/* 291:523 */         addRelation(rel, doc);
/* 292:    */       }
/* 293:526 */       return new RelationPart(rel, doc);
/* 294:    */     }
/* 295:    */     catch (PartAlreadyExistsException pae)
/* 296:    */     {
/* 297:530 */       throw pae;
/* 298:    */     }
/* 299:    */     catch (Exception e)
/* 300:    */     {
/* 301:533 */       throw new POIXMLException(e);
/* 302:    */     }
/* 303:    */   }
/* 304:    */   
/* 305:    */   protected void read(POIXMLFactory factory, Map<PackagePart, POIXMLDocumentPart> context)
/* 306:    */     throws OpenXML4JException
/* 307:    */   {
/* 308:547 */     PackagePart pp = getPackagePart();
/* 309:    */     
/* 310:549 */     POIXMLDocumentPart otherChild = (POIXMLDocumentPart)context.put(pp, this);
/* 311:550 */     if ((otherChild != null) && (otherChild != this)) {
/* 312:551 */       throw new POIXMLException("Unique PackagePart-POIXMLDocumentPart relation broken!");
/* 313:    */     }
/* 314:554 */     if (!pp.hasRelationships()) {
/* 315:554 */       return;
/* 316:    */     }
/* 317:556 */     PackageRelationshipCollection rels = this.packagePart.getRelationships();
/* 318:557 */     List<POIXMLDocumentPart> readLater = new ArrayList();
/* 319:560 */     for (PackageRelationship rel : rels) {
/* 320:561 */       if (rel.getTargetMode() == TargetMode.INTERNAL)
/* 321:    */       {
/* 322:562 */         URI uri = rel.getTargetURI();
/* 323:    */         PackagePartName relName;
/* 324:    */         PackagePartName relName;
/* 325:566 */         if (uri.getRawFragment() != null) {
/* 326:567 */           relName = PackagingURIHelper.createPartName(uri.getPath());
/* 327:    */         } else {
/* 328:569 */           relName = PackagingURIHelper.createPartName(uri);
/* 329:    */         }
/* 330:572 */         PackagePart p = this.packagePart.getPackage().getPart(relName);
/* 331:573 */         if (p == null)
/* 332:    */         {
/* 333:574 */           logger.log(7, new Object[] { "Skipped invalid entry " + rel.getTargetURI() });
/* 334:    */         }
/* 335:    */         else
/* 336:    */         {
/* 337:578 */           POIXMLDocumentPart childPart = (POIXMLDocumentPart)context.get(p);
/* 338:579 */           if (childPart == null)
/* 339:    */           {
/* 340:580 */             childPart = factory.createDocumentPart(this, p);
/* 341:581 */             childPart.parent = this;
/* 342:    */             
/* 343:583 */             context.put(p, childPart);
/* 344:584 */             readLater.add(childPart);
/* 345:    */           }
/* 346:587 */           addRelation(rel, childPart);
/* 347:    */         }
/* 348:    */       }
/* 349:    */     }
/* 350:591 */     for (POIXMLDocumentPart childPart : readLater) {
/* 351:592 */       childPart.read(factory, context);
/* 352:    */     }
/* 353:    */   }
/* 354:    */   
/* 355:    */   protected PackagePart getTargetPart(PackageRelationship rel)
/* 356:    */     throws InvalidFormatException
/* 357:    */   {
/* 358:604 */     return getPackagePart().getRelatedPart(rel);
/* 359:    */   }
/* 360:    */   
/* 361:    */   protected void onDocumentCreate()
/* 362:    */     throws IOException
/* 363:    */   {}
/* 364:    */   
/* 365:    */   protected void onDocumentRead()
/* 366:    */     throws IOException
/* 367:    */   {}
/* 368:    */   
/* 369:    */   protected void onDocumentRemove()
/* 370:    */     throws IOException
/* 371:    */   {}
/* 372:    */   
/* 373:    */   @Internal
/* 374:    */   @Deprecated
/* 375:    */   public static void _invokeOnDocumentRead(POIXMLDocumentPart part)
/* 376:    */     throws IOException
/* 377:    */   {
/* 378:647 */     part.onDocumentRead();
/* 379:    */   }
/* 380:    */   
/* 381:    */   private static PackagePart getPartFromOPCPackage(OPCPackage pkg, String coreDocumentRel)
/* 382:    */   {
/* 383:656 */     PackageRelationship coreRel = pkg.getRelationshipsByType(coreDocumentRel).getRelationship(0);
/* 384:658 */     if (coreRel != null)
/* 385:    */     {
/* 386:659 */       PackagePart pp = pkg.getPart(coreRel);
/* 387:660 */       if (pp == null) {
/* 388:661 */         throw new POIXMLException("OOXML file structure broken/invalid - core document '" + coreRel.getTargetURI() + "' not found.");
/* 389:    */       }
/* 390:663 */       return pp;
/* 391:    */     }
/* 392:666 */     coreRel = pkg.getRelationshipsByType("http://purl.oclc.org/ooxml/officeDocument/relationships/officeDocument").getRelationship(0);
/* 393:667 */     if (coreRel != null) {
/* 394:668 */       throw new POIXMLException("Strict OOXML isn't currently supported, please see bug #57699");
/* 395:    */     }
/* 396:671 */     throw new POIXMLException("OOXML file structure broken/invalid - no core document found!");
/* 397:    */   }
/* 398:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLDocumentPart
 * JD-Core Version:    0.7.0.1
 */