/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URISyntaxException;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  10:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  11:    */ import org.apache.poi.openxml4j.opc.internal.ContentType;
/*  12:    */ 
/*  13:    */ public abstract class PackagePart
/*  14:    */   implements RelationshipSource, Comparable<PackagePart>
/*  15:    */ {
/*  16:    */   protected OPCPackage _container;
/*  17:    */   protected PackagePartName _partName;
/*  18:    */   protected ContentType _contentType;
/*  19:    */   private boolean _isRelationshipPart;
/*  20:    */   private boolean _isDeleted;
/*  21:    */   private PackageRelationshipCollection _relationships;
/*  22:    */   
/*  23:    */   protected PackagePart(OPCPackage pack, PackagePartName partName, ContentType contentType)
/*  24:    */     throws InvalidFormatException
/*  25:    */   {
/*  26: 82 */     this(pack, partName, contentType, true);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected PackagePart(OPCPackage pack, PackagePartName partName, ContentType contentType, boolean loadRelationships)
/*  30:    */     throws InvalidFormatException
/*  31:    */   {
/*  32:102 */     this._partName = partName;
/*  33:103 */     this._contentType = contentType;
/*  34:104 */     this._container = pack;
/*  35:    */     
/*  36:    */ 
/*  37:107 */     this._isRelationshipPart = this._partName.isRelationshipPartURI();
/*  38:110 */     if (loadRelationships) {
/*  39:111 */       loadRelationships();
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public PackagePart(OPCPackage pack, PackagePartName partName, String contentType)
/*  44:    */     throws InvalidFormatException
/*  45:    */   {
/*  46:127 */     this(pack, partName, new ContentType(contentType));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public PackageRelationship findExistingRelation(PackagePart packagePart)
/*  50:    */   {
/*  51:137 */     return this._relationships.findExistingInternalRelation(packagePart);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public PackageRelationship addExternalRelationship(String target, String relationshipType)
/*  55:    */   {
/*  56:157 */     return addExternalRelationship(target, relationshipType, null);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public PackageRelationship addExternalRelationship(String target, String relationshipType, String id)
/*  60:    */   {
/*  61:179 */     if (target == null) {
/*  62:180 */       throw new IllegalArgumentException("target is null for type " + relationshipType);
/*  63:    */     }
/*  64:182 */     if (relationshipType == null) {
/*  65:183 */       throw new IllegalArgumentException("relationshipType");
/*  66:    */     }
/*  67:186 */     if (this._relationships == null) {
/*  68:187 */       this._relationships = new PackageRelationshipCollection();
/*  69:    */     }
/*  70:    */     URI targetURI;
/*  71:    */     try
/*  72:    */     {
/*  73:192 */       targetURI = new URI(target);
/*  74:    */     }
/*  75:    */     catch (URISyntaxException e)
/*  76:    */     {
/*  77:194 */       throw new IllegalArgumentException("Invalid target - " + e);
/*  78:    */     }
/*  79:197 */     return this._relationships.addRelationship(targetURI, TargetMode.EXTERNAL, relationshipType, id);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public PackageRelationship addRelationship(PackagePartName targetPartName, TargetMode targetMode, String relationshipType)
/*  83:    */   {
/*  84:217 */     return addRelationship(targetPartName, targetMode, relationshipType, null);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public PackageRelationship addRelationship(PackagePartName targetPartName, TargetMode targetMode, String relationshipType, String id)
/*  88:    */   {
/*  89:250 */     this._container.throwExceptionIfReadOnly();
/*  90:252 */     if (targetPartName == null) {
/*  91:253 */       throw new IllegalArgumentException("targetPartName");
/*  92:    */     }
/*  93:255 */     if (targetMode == null) {
/*  94:256 */       throw new IllegalArgumentException("targetMode");
/*  95:    */     }
/*  96:258 */     if (relationshipType == null) {
/*  97:259 */       throw new IllegalArgumentException("relationshipType");
/*  98:    */     }
/*  99:262 */     if ((this._isRelationshipPart) || (targetPartName.isRelationshipPartURI())) {
/* 100:263 */       throw new InvalidOperationException("Rule M1.25: The Relationships part shall not have relationships to any other part.");
/* 101:    */     }
/* 102:267 */     if (this._relationships == null) {
/* 103:268 */       this._relationships = new PackageRelationshipCollection();
/* 104:    */     }
/* 105:271 */     return this._relationships.addRelationship(targetPartName.getURI(), targetMode, relationshipType, id);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public PackageRelationship addRelationship(URI targetURI, TargetMode targetMode, String relationshipType)
/* 109:    */   {
/* 110:291 */     return addRelationship(targetURI, targetMode, relationshipType, null);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public PackageRelationship addRelationship(URI targetURI, TargetMode targetMode, String relationshipType, String id)
/* 114:    */   {
/* 115:320 */     this._container.throwExceptionIfReadOnly();
/* 116:322 */     if (targetURI == null) {
/* 117:323 */       throw new IllegalArgumentException("targetPartName");
/* 118:    */     }
/* 119:325 */     if (targetMode == null) {
/* 120:326 */       throw new IllegalArgumentException("targetMode");
/* 121:    */     }
/* 122:328 */     if (relationshipType == null) {
/* 123:329 */       throw new IllegalArgumentException("relationshipType");
/* 124:    */     }
/* 125:334 */     if ((this._isRelationshipPart) || (PackagingURIHelper.isRelationshipPartURI(targetURI))) {
/* 126:336 */       throw new InvalidOperationException("Rule M1.25: The Relationships part shall not have relationships to any other part.");
/* 127:    */     }
/* 128:340 */     if (this._relationships == null) {
/* 129:341 */       this._relationships = new PackageRelationshipCollection();
/* 130:    */     }
/* 131:344 */     return this._relationships.addRelationship(targetURI, targetMode, relationshipType, id);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void clearRelationships()
/* 135:    */   {
/* 136:352 */     if (this._relationships != null) {
/* 137:353 */       this._relationships.clear();
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void removeRelationship(String id)
/* 142:    */   {
/* 143:365 */     this._container.throwExceptionIfReadOnly();
/* 144:366 */     if (this._relationships != null) {
/* 145:367 */       this._relationships.removeRelationship(id);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public PackageRelationshipCollection getRelationships()
/* 150:    */     throws InvalidFormatException
/* 151:    */   {
/* 152:380 */     return getRelationshipsCore(null);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public PackageRelationship getRelationship(String id)
/* 156:    */   {
/* 157:392 */     return this._relationships.getRelationshipByID(id);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public PackageRelationshipCollection getRelationshipsByType(String relationshipType)
/* 161:    */     throws InvalidFormatException
/* 162:    */   {
/* 163:410 */     this._container.throwExceptionIfWriteOnly();
/* 164:    */     
/* 165:412 */     return getRelationshipsCore(relationshipType);
/* 166:    */   }
/* 167:    */   
/* 168:    */   private PackageRelationshipCollection getRelationshipsCore(String filter)
/* 169:    */     throws InvalidFormatException
/* 170:    */   {
/* 171:431 */     this._container.throwExceptionIfWriteOnly();
/* 172:432 */     if (this._relationships == null)
/* 173:    */     {
/* 174:433 */       throwExceptionIfRelationship();
/* 175:434 */       this._relationships = new PackageRelationshipCollection(this);
/* 176:    */     }
/* 177:436 */     return new PackageRelationshipCollection(this._relationships, filter);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public boolean hasRelationships()
/* 181:    */   {
/* 182:447 */     return (!this._isRelationshipPart) && (this._relationships != null) && (this._relationships.size() > 0);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public boolean isRelationshipExists(PackageRelationship rel)
/* 186:    */   {
/* 187:461 */     return this._relationships.getRelationshipByID(rel.getId()) != null;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public PackagePart getRelatedPart(PackageRelationship rel)
/* 191:    */     throws InvalidFormatException
/* 192:    */   {
/* 193:472 */     if (!isRelationshipExists(rel)) {
/* 194:473 */       throw new IllegalArgumentException("Relationship " + rel + " doesn't start with this part " + this._partName);
/* 195:    */     }
/* 196:476 */     URI target = rel.getTargetURI();
/* 197:477 */     if (target.getFragment() != null)
/* 198:    */     {
/* 199:478 */       String t = target.toString();
/* 200:    */       try
/* 201:    */       {
/* 202:480 */         target = new URI(t.substring(0, t.indexOf('#')));
/* 203:    */       }
/* 204:    */       catch (URISyntaxException e)
/* 205:    */       {
/* 206:482 */         throw new InvalidFormatException("Invalid target URI: " + target);
/* 207:    */       }
/* 208:    */     }
/* 209:487 */     PackagePartName relName = PackagingURIHelper.createPartName(target);
/* 210:488 */     PackagePart part = this._container.getPart(relName);
/* 211:489 */     if (part == null) {
/* 212:490 */       throw new IllegalArgumentException("No part found for relationship " + rel);
/* 213:    */     }
/* 214:492 */     return part;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public InputStream getInputStream()
/* 218:    */     throws IOException
/* 219:    */   {
/* 220:504 */     InputStream inStream = getInputStreamImpl();
/* 221:505 */     if (inStream == null) {
/* 222:506 */       throw new IOException("Can't obtain the input stream from " + this._partName.getName());
/* 223:    */     }
/* 224:509 */     return inStream;
/* 225:    */   }
/* 226:    */   
/* 227:    */   public OutputStream getOutputStream()
/* 228:    */   {
/* 229:    */     OutputStream outStream;
/* 230:    */     OutputStream outStream;
/* 231:524 */     if ((this instanceof ZipPackagePart))
/* 232:    */     {
/* 233:526 */       this._container.removePart(this._partName);
/* 234:    */       
/* 235:    */ 
/* 236:529 */       PackagePart part = this._container.createPart(this._partName, this._contentType.toString(), false);
/* 237:531 */       if (part == null) {
/* 238:532 */         throw new InvalidOperationException("Can't create a temporary part !");
/* 239:    */       }
/* 240:535 */       part._relationships = this._relationships;
/* 241:536 */       outStream = part.getOutputStreamImpl();
/* 242:    */     }
/* 243:    */     else
/* 244:    */     {
/* 245:538 */       outStream = getOutputStreamImpl();
/* 246:    */     }
/* 247:540 */     return outStream;
/* 248:    */   }
/* 249:    */   
/* 250:    */   private void throwExceptionIfRelationship()
/* 251:    */     throws InvalidOperationException
/* 252:    */   {
/* 253:551 */     if (this._isRelationshipPart) {
/* 254:552 */       throw new InvalidOperationException("Can do this operation on a relationship part !");
/* 255:    */     }
/* 256:    */   }
/* 257:    */   
/* 258:    */   private void loadRelationships()
/* 259:    */     throws InvalidFormatException
/* 260:    */   {
/* 261:563 */     if ((this._relationships == null) && (!this._isRelationshipPart))
/* 262:    */     {
/* 263:564 */       throwExceptionIfRelationship();
/* 264:565 */       this._relationships = new PackageRelationshipCollection(this);
/* 265:    */     }
/* 266:    */   }
/* 267:    */   
/* 268:    */   public PackagePartName getPartName()
/* 269:    */   {
/* 270:577 */     return this._partName;
/* 271:    */   }
/* 272:    */   
/* 273:    */   public String getContentType()
/* 274:    */   {
/* 275:584 */     return this._contentType.toString();
/* 276:    */   }
/* 277:    */   
/* 278:    */   public ContentType getContentTypeDetails()
/* 279:    */   {
/* 280:591 */     return this._contentType;
/* 281:    */   }
/* 282:    */   
/* 283:    */   public void setContentType(String contentType)
/* 284:    */     throws InvalidFormatException
/* 285:    */   {
/* 286:608 */     if (this._container == null)
/* 287:    */     {
/* 288:609 */       this._contentType = new ContentType(contentType);
/* 289:    */     }
/* 290:    */     else
/* 291:    */     {
/* 292:612 */       this._container.unregisterPartAndContentType(this._partName);
/* 293:613 */       this._contentType = new ContentType(contentType);
/* 294:614 */       this._container.registerPartAndContentType(this);
/* 295:    */     }
/* 296:    */   }
/* 297:    */   
/* 298:    */   public OPCPackage getPackage()
/* 299:    */   {
/* 300:619 */     return this._container;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public boolean isRelationshipPart()
/* 304:    */   {
/* 305:626 */     return this._isRelationshipPart;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public boolean isDeleted()
/* 309:    */   {
/* 310:633 */     return this._isDeleted;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void setDeleted(boolean isDeleted)
/* 314:    */   {
/* 315:641 */     this._isDeleted = isDeleted;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public long getSize()
/* 319:    */   {
/* 320:648 */     return -1L;
/* 321:    */   }
/* 322:    */   
/* 323:    */   public String toString()
/* 324:    */   {
/* 325:653 */     return "Name: " + this._partName + " - Content Type: " + this._contentType;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public int compareTo(PackagePart other)
/* 329:    */   {
/* 330:664 */     if (other == null) {
/* 331:665 */       return -1;
/* 332:    */     }
/* 333:667 */     return PackagePartName.compare(this._partName, other._partName);
/* 334:    */   }
/* 335:    */   
/* 336:    */   protected abstract InputStream getInputStreamImpl()
/* 337:    */     throws IOException;
/* 338:    */   
/* 339:    */   protected abstract OutputStream getOutputStreamImpl();
/* 340:    */   
/* 341:    */   public abstract boolean save(OutputStream paramOutputStream)
/* 342:    */     throws OpenXML4JException;
/* 343:    */   
/* 344:    */   public abstract boolean load(InputStream paramInputStream)
/* 345:    */     throws InvalidFormatException;
/* 346:    */   
/* 347:    */   public abstract void close();
/* 348:    */   
/* 349:    */   public abstract void flush();
/* 350:    */   
/* 351:    */   public void clear() {}
/* 352:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackagePart
 * JD-Core Version:    0.7.0.1
 */