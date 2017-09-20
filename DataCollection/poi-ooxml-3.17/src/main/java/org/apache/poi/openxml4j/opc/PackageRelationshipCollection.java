/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.net.URISyntaxException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.Locale;
/*  10:    */ import java.util.TreeMap;
/*  11:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  13:    */ import org.apache.poi.util.DocumentHelper;
/*  14:    */ import org.apache.poi.util.POILogFactory;
/*  15:    */ import org.apache.poi.util.POILogger;
/*  16:    */ import org.w3c.dom.Attr;
/*  17:    */ import org.w3c.dom.Document;
/*  18:    */ import org.w3c.dom.Element;
/*  19:    */ import org.w3c.dom.NodeList;
/*  20:    */ 
/*  21:    */ public final class PackageRelationshipCollection
/*  22:    */   implements Iterable<PackageRelationship>
/*  23:    */ {
/*  24: 43 */   private static final POILogger logger = POILogFactory.getLogger(PackageRelationshipCollection.class);
/*  25:    */   private TreeMap<String, PackageRelationship> relationshipsByID;
/*  26:    */   private TreeMap<String, PackageRelationship> relationshipsByType;
/*  27: 58 */   private HashMap<String, PackageRelationship> internalRelationshipsByTargetName = new HashMap();
/*  28:    */   private PackagePart relationshipPart;
/*  29:    */   private PackagePart sourcePart;
/*  30:    */   private PackagePartName partName;
/*  31:    */   private OPCPackage container;
/*  32: 85 */   private int nextRelationshipId = -1;
/*  33:    */   
/*  34:    */   PackageRelationshipCollection()
/*  35:    */   {
/*  36: 91 */     this.relationshipsByID = new TreeMap();
/*  37: 92 */     this.relationshipsByType = new TreeMap();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public PackageRelationshipCollection(PackageRelationshipCollection coll, String filter)
/*  41:    */   {
/*  42:109 */     this();
/*  43:110 */     for (PackageRelationship rel : coll.relationshipsByID.values()) {
/*  44:111 */       if ((filter == null) || (rel.getRelationshipType().equals(filter))) {
/*  45:112 */         addRelationship(rel);
/*  46:    */       }
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public PackageRelationshipCollection(OPCPackage container)
/*  51:    */     throws InvalidFormatException
/*  52:    */   {
/*  53:121 */     this(container, null);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public PackageRelationshipCollection(PackagePart part)
/*  57:    */     throws InvalidFormatException
/*  58:    */   {
/*  59:135 */     this(part._container, part);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public PackageRelationshipCollection(OPCPackage container, PackagePart part)
/*  63:    */     throws InvalidFormatException
/*  64:    */   {
/*  65:152 */     this();
/*  66:154 */     if (container == null) {
/*  67:155 */       throw new IllegalArgumentException("container needs to be specified");
/*  68:    */     }
/*  69:158 */     if ((part != null) && (part.isRelationshipPart())) {
/*  70:159 */       throw new IllegalArgumentException("part");
/*  71:    */     }
/*  72:161 */     this.container = container;
/*  73:162 */     this.sourcePart = part;
/*  74:163 */     this.partName = getRelationshipPartName(part);
/*  75:164 */     if ((container.getPackageAccess() != PackageAccess.WRITE) && (container.containPart(this.partName)))
/*  76:    */     {
/*  77:166 */       this.relationshipPart = container.getPart(this.partName);
/*  78:167 */       parseRelationshipsPart(this.relationshipPart);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   private static PackagePartName getRelationshipPartName(PackagePart part)
/*  83:    */     throws InvalidOperationException
/*  84:    */   {
/*  85:    */     PackagePartName partName;
/*  86:    */     PackagePartName partName;
/*  87:185 */     if (part == null) {
/*  88:186 */       partName = PackagingURIHelper.PACKAGE_ROOT_PART_NAME;
/*  89:    */     } else {
/*  90:188 */       partName = part.getPartName();
/*  91:    */     }
/*  92:190 */     return PackagingURIHelper.getRelationshipPartName(partName);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void addRelationship(PackageRelationship relPart)
/*  96:    */   {
/*  97:200 */     this.relationshipsByID.put(relPart.getId(), relPart);
/*  98:201 */     this.relationshipsByType.put(relPart.getRelationshipType(), relPart);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public PackageRelationship addRelationship(URI targetUri, TargetMode targetMode, String relationshipType, String id)
/* 102:    */   {
/* 103:220 */     if (id == null)
/* 104:    */     {
/* 105:222 */       if (this.nextRelationshipId == -1) {
/* 106:223 */         this.nextRelationshipId = (size() + 1);
/* 107:    */       }
/* 108:    */       do
/* 109:    */       {
/* 110:228 */         id = "rId" + this.nextRelationshipId++;
/* 111:229 */       } while (this.relationshipsByID.get(id) != null);
/* 112:    */     }
/* 113:232 */     PackageRelationship rel = new PackageRelationship(this.container, this.sourcePart, targetUri, targetMode, relationshipType, id);
/* 114:    */     
/* 115:234 */     this.relationshipsByID.put(rel.getId(), rel);
/* 116:235 */     this.relationshipsByType.put(rel.getRelationshipType(), rel);
/* 117:236 */     if (targetMode == TargetMode.INTERNAL) {
/* 118:237 */       this.internalRelationshipsByTargetName.put(targetUri.toASCIIString(), rel);
/* 119:    */     }
/* 120:239 */     return rel;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void removeRelationship(String id)
/* 124:    */   {
/* 125:249 */     if ((this.relationshipsByID != null) && (this.relationshipsByType != null))
/* 126:    */     {
/* 127:250 */       PackageRelationship rel = (PackageRelationship)this.relationshipsByID.get(id);
/* 128:251 */       if (rel != null)
/* 129:    */       {
/* 130:252 */         this.relationshipsByID.remove(rel.getId());
/* 131:253 */         this.relationshipsByType.values().remove(rel);
/* 132:254 */         this.internalRelationshipsByTargetName.values().remove(rel);
/* 133:    */       }
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public PackageRelationship getRelationship(int index)
/* 138:    */   {
/* 139:266 */     if ((index < 0) || (index > this.relationshipsByID.values().size())) {
/* 140:267 */       throw new IllegalArgumentException("index");
/* 141:    */     }
/* 142:269 */     int i = 0;
/* 143:270 */     for (PackageRelationship rel : this.relationshipsByID.values()) {
/* 144:271 */       if (index == i++) {
/* 145:272 */         return rel;
/* 146:    */       }
/* 147:    */     }
/* 148:275 */     return null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public PackageRelationship getRelationshipByID(String id)
/* 152:    */   {
/* 153:286 */     return (PackageRelationship)this.relationshipsByID.get(id);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public int size()
/* 157:    */   {
/* 158:293 */     return this.relationshipsByID.values().size();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void parseRelationshipsPart(PackagePart relPart)
/* 162:    */     throws InvalidFormatException
/* 163:    */   {
/* 164:    */     try
/* 165:    */     {
/* 166:307 */       logger.log(1, new Object[] { "Parsing relationship: " + relPart.getPartName() });
/* 167:308 */       Document xmlRelationshipsDoc = DocumentHelper.readDocument(relPart.getInputStream());
/* 168:    */       
/* 169:    */ 
/* 170:311 */       Element root = xmlRelationshipsDoc.getDocumentElement();
/* 171:    */       
/* 172:    */ 
/* 173:314 */       boolean fCorePropertiesRelationship = false;
/* 174:    */       
/* 175:316 */       NodeList nodeList = root.getElementsByTagNameNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship");
/* 176:317 */       int nodeCount = nodeList.getLength();
/* 177:318 */       for (int i = 0; i < nodeCount; i++)
/* 178:    */       {
/* 179:319 */         Element element = (Element)nodeList.item(i);
/* 180:    */         
/* 181:321 */         String id = element.getAttribute("Id");
/* 182:    */         
/* 183:323 */         String type = element.getAttribute("Type");
/* 184:327 */         if (type.equals("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties")) {
/* 185:328 */           if (!fCorePropertiesRelationship) {
/* 186:329 */             fCorePropertiesRelationship = true;
/* 187:    */           } else {
/* 188:331 */             throw new InvalidFormatException("OPC Compliance error [M4.1]: there is more than one core properties relationship in the package !");
/* 189:    */           }
/* 190:    */         }
/* 191:337 */         Attr targetModeAttr = element.getAttributeNode("TargetMode");
/* 192:338 */         TargetMode targetMode = TargetMode.INTERNAL;
/* 193:339 */         if (targetModeAttr != null) {
/* 194:340 */           targetMode = targetModeAttr.getValue().toLowerCase(Locale.ROOT).equals("internal") ? TargetMode.INTERNAL : TargetMode.EXTERNAL;
/* 195:    */         }
/* 196:346 */         URI target = PackagingURIHelper.toURI("http://invalid.uri");
/* 197:347 */         String value = element.getAttribute("Target");
/* 198:    */         try
/* 199:    */         {
/* 200:353 */           target = PackagingURIHelper.toURI(value);
/* 201:    */         }
/* 202:    */         catch (URISyntaxException e)
/* 203:    */         {
/* 204:355 */           logger.log(7, new Object[] { "Cannot convert " + value + " in a valid relationship URI-> dummy-URI used", e });
/* 205:    */         }
/* 206:358 */         addRelationship(target, targetMode, type, id);
/* 207:    */       }
/* 208:    */     }
/* 209:    */     catch (Exception e)
/* 210:    */     {
/* 211:361 */       logger.log(7, new Object[] { e });
/* 212:362 */       throw new InvalidFormatException(e.getMessage());
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public PackageRelationshipCollection getRelationships(String typeFilter)
/* 217:    */   {
/* 218:375 */     return new PackageRelationshipCollection(this, typeFilter);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Iterator<PackageRelationship> iterator()
/* 222:    */   {
/* 223:382 */     return this.relationshipsByID.values().iterator();
/* 224:    */   }
/* 225:    */   
/* 226:    */   public Iterator<PackageRelationship> iterator(String typeFilter)
/* 227:    */   {
/* 228:395 */     ArrayList<PackageRelationship> retArr = new ArrayList();
/* 229:396 */     for (PackageRelationship rel : this.relationshipsByID.values()) {
/* 230:397 */       if (rel.getRelationshipType().equals(typeFilter)) {
/* 231:398 */         retArr.add(rel);
/* 232:    */       }
/* 233:    */     }
/* 234:400 */     return retArr.iterator();
/* 235:    */   }
/* 236:    */   
/* 237:    */   public void clear()
/* 238:    */   {
/* 239:407 */     this.relationshipsByID.clear();
/* 240:408 */     this.relationshipsByType.clear();
/* 241:409 */     this.internalRelationshipsByTargetName.clear();
/* 242:    */   }
/* 243:    */   
/* 244:    */   public PackageRelationship findExistingInternalRelation(PackagePart packagePart)
/* 245:    */   {
/* 246:413 */     return (PackageRelationship)this.internalRelationshipsByTargetName.get(packagePart.getPartName().getName());
/* 247:    */   }
/* 248:    */   
/* 249:    */   public String toString()
/* 250:    */   {
/* 251:    */     String str;
/* 252:    */     String str;
/* 253:419 */     if (this.relationshipsByID == null) {
/* 254:420 */       str = "relationshipsByID=null";
/* 255:    */     } else {
/* 256:422 */       str = this.relationshipsByID.size() + " relationship(s) = [";
/* 257:    */     }
/* 258:424 */     if ((this.relationshipPart != null) && (this.relationshipPart._partName != null)) {
/* 259:425 */       str = str + "," + this.relationshipPart._partName;
/* 260:    */     } else {
/* 261:427 */       str = str + ",relationshipPart=null";
/* 262:    */     }
/* 263:431 */     if ((this.sourcePart != null) && (this.sourcePart._partName != null)) {
/* 264:432 */       str = str + "," + this.sourcePart._partName;
/* 265:    */     } else {
/* 266:434 */       str = str + ",sourcePart=null";
/* 267:    */     }
/* 268:436 */     if (this.partName != null) {
/* 269:437 */       str = str + "," + this.partName;
/* 270:    */     } else {
/* 271:439 */       str = str + ",uri=null)";
/* 272:    */     }
/* 273:441 */     return str + "]";
/* 274:    */   }
/* 275:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackageRelationshipCollection
 * JD-Core Version:    0.7.0.1
 */