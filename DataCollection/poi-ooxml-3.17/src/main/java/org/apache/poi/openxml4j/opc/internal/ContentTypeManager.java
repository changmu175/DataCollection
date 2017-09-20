/*   1:    */ package org.apache.poi.openxml4j.opc.internal;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URISyntaxException;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Locale;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import java.util.TreeMap;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  13:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JRuntimeException;
/*  15:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  19:    */ import org.apache.poi.util.DocumentHelper;
/*  20:    */ import org.w3c.dom.Document;
/*  21:    */ import org.w3c.dom.Element;
/*  22:    */ import org.w3c.dom.NodeList;
/*  23:    */ import org.xml.sax.SAXException;
/*  24:    */ 
/*  25:    */ public abstract class ContentTypeManager
/*  26:    */ {
/*  27:    */   public static final String CONTENT_TYPES_PART_NAME = "[Content_Types].xml";
/*  28:    */   public static final String TYPES_NAMESPACE_URI = "http://schemas.openxmlformats.org/package/2006/content-types";
/*  29:    */   private static final String TYPES_TAG_NAME = "Types";
/*  30:    */   private static final String DEFAULT_TAG_NAME = "Default";
/*  31:    */   private static final String EXTENSION_ATTRIBUTE_NAME = "Extension";
/*  32:    */   private static final String CONTENT_TYPE_ATTRIBUTE_NAME = "ContentType";
/*  33:    */   private static final String OVERRIDE_TAG_NAME = "Override";
/*  34:    */   private static final String PART_NAME_ATTRIBUTE_NAME = "PartName";
/*  35:    */   protected OPCPackage container;
/*  36:    */   private TreeMap<String, String> defaultContentType;
/*  37:    */   private TreeMap<PackagePartName, String> overrideContentType;
/*  38:    */   
/*  39:    */   public ContentTypeManager(InputStream in, OPCPackage pkg)
/*  40:    */     throws InvalidFormatException
/*  41:    */   {
/*  42: 96 */     this.container = pkg;
/*  43: 97 */     this.defaultContentType = new TreeMap();
/*  44: 98 */     if (in != null) {
/*  45:    */       try
/*  46:    */       {
/*  47:100 */         parseContentTypesFile(in);
/*  48:    */       }
/*  49:    */       catch (InvalidFormatException e)
/*  50:    */       {
/*  51:102 */         InvalidFormatException ex = new InvalidFormatException("Can't read content types part !");
/*  52:    */         
/*  53:    */ 
/*  54:105 */         ex.initCause(e);
/*  55:    */         
/*  56:107 */         throw ex;
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void addContentType(PackagePartName partName, String contentType)
/*  62:    */   {
/*  63:150 */     boolean defaultCTExists = this.defaultContentType.containsValue(contentType);
/*  64:151 */     String extension = partName.getExtension().toLowerCase(Locale.ROOT);
/*  65:152 */     if ((extension.length() == 0) || ((this.defaultContentType.containsKey(extension)) && (!defaultCTExists))) {
/*  66:154 */       addOverrideContentType(partName, contentType);
/*  67:155 */     } else if (!defaultCTExists) {
/*  68:156 */       addDefaultContentType(extension, contentType);
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void addOverrideContentType(PackagePartName partName, String contentType)
/*  73:    */   {
/*  74:169 */     if (this.overrideContentType == null) {
/*  75:170 */       this.overrideContentType = new TreeMap();
/*  76:    */     }
/*  77:171 */     this.overrideContentType.put(partName, contentType);
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void addDefaultContentType(String extension, String contentType)
/*  81:    */   {
/*  82:185 */     this.defaultContentType.put(extension.toLowerCase(Locale.ROOT), contentType);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void removeContentType(PackagePartName partName)
/*  86:    */     throws InvalidOperationException
/*  87:    */   {
/*  88:209 */     if (partName == null) {
/*  89:210 */       throw new IllegalArgumentException("partName");
/*  90:    */     }
/*  91:213 */     if ((this.overrideContentType != null) && (this.overrideContentType.get(partName) != null))
/*  92:    */     {
/*  93:216 */       this.overrideContentType.remove(partName);
/*  94:217 */       return;
/*  95:    */     }
/*  96:221 */     String extensionToDelete = partName.getExtension();
/*  97:222 */     boolean deleteDefaultContentTypeFlag = true;
/*  98:223 */     if (this.container != null) {
/*  99:    */       try
/* 100:    */       {
/* 101:225 */         for (PackagePart part : this.container.getParts()) {
/* 102:226 */           if ((!part.getPartName().equals(partName)) && (part.getPartName().getExtension().equalsIgnoreCase(extensionToDelete)))
/* 103:    */           {
/* 104:229 */             deleteDefaultContentTypeFlag = false;
/* 105:230 */             break;
/* 106:    */           }
/* 107:    */         }
/* 108:    */       }
/* 109:    */       catch (InvalidFormatException e)
/* 110:    */       {
/* 111:234 */         throw new InvalidOperationException(e.getMessage());
/* 112:    */       }
/* 113:    */     }
/* 114:239 */     if (deleteDefaultContentTypeFlag) {
/* 115:240 */       this.defaultContentType.remove(extensionToDelete);
/* 116:    */     }
/* 117:250 */     if (this.container != null) {
/* 118:    */       try
/* 119:    */       {
/* 120:252 */         for (PackagePart part : this.container.getParts()) {
/* 121:253 */           if ((!part.getPartName().equals(partName)) && (getContentType(part.getPartName()) == null)) {
/* 122:255 */             throw new InvalidOperationException("Rule M2.4 is not respected: Nor a default element or override element is associated with the part: " + part.getPartName().getName());
/* 123:    */           }
/* 124:    */         }
/* 125:    */       }
/* 126:    */       catch (InvalidFormatException e)
/* 127:    */       {
/* 128:260 */         throw new InvalidOperationException(e.getMessage());
/* 129:    */       }
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   public boolean isContentTypeRegister(String contentType)
/* 134:    */   {
/* 135:274 */     if (contentType == null) {
/* 136:275 */       throw new IllegalArgumentException("contentType");
/* 137:    */     }
/* 138:277 */     return (this.defaultContentType.values().contains(contentType)) || ((this.overrideContentType != null) && (this.overrideContentType.values().contains(contentType)));
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String getContentType(PackagePartName partName)
/* 142:    */   {
/* 143:321 */     if (partName == null) {
/* 144:322 */       throw new IllegalArgumentException("partName");
/* 145:    */     }
/* 146:324 */     if ((this.overrideContentType != null) && (this.overrideContentType.containsKey(partName))) {
/* 147:326 */       return (String)this.overrideContentType.get(partName);
/* 148:    */     }
/* 149:328 */     String extension = partName.getExtension().toLowerCase(Locale.ROOT);
/* 150:329 */     if (this.defaultContentType.containsKey(extension)) {
/* 151:330 */       return (String)this.defaultContentType.get(extension);
/* 152:    */     }
/* 153:339 */     if ((this.container != null) && (this.container.getPart(partName) != null)) {
/* 154:340 */       throw new OpenXML4JRuntimeException("Rule M2.4 exception : this error should NEVER happen! If you can provide the triggering file, then please raise a bug at https://bz.apache.org/bugzilla/enter_bug.cgi?product=POI and attach the file that triggers it, thanks!");
/* 155:    */     }
/* 156:343 */     return null;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void clearAll()
/* 160:    */   {
/* 161:350 */     this.defaultContentType.clear();
/* 162:351 */     if (this.overrideContentType != null) {
/* 163:352 */       this.overrideContentType.clear();
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public void clearOverrideContentTypes()
/* 168:    */   {
/* 169:360 */     if (this.overrideContentType != null) {
/* 170:361 */       this.overrideContentType.clear();
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   private void parseContentTypesFile(InputStream in)
/* 175:    */     throws InvalidFormatException
/* 176:    */   {
/* 177:    */     try
/* 178:    */     {
/* 179:374 */       Document xmlContentTypetDoc = DocumentHelper.readDocument(in);
/* 180:    */       
/* 181:    */ 
/* 182:377 */       NodeList defaultTypes = xmlContentTypetDoc.getDocumentElement().getElementsByTagNameNS("http://schemas.openxmlformats.org/package/2006/content-types", "Default");
/* 183:378 */       int defaultTypeCount = defaultTypes.getLength();
/* 184:379 */       for (int i = 0; i < defaultTypeCount; i++)
/* 185:    */       {
/* 186:380 */         Element element = (Element)defaultTypes.item(i);
/* 187:381 */         String extension = element.getAttribute("Extension");
/* 188:382 */         String contentType = element.getAttribute("ContentType");
/* 189:383 */         addDefaultContentType(extension, contentType);
/* 190:    */       }
/* 191:387 */       NodeList overrideTypes = xmlContentTypetDoc.getDocumentElement().getElementsByTagNameNS("http://schemas.openxmlformats.org/package/2006/content-types", "Override");
/* 192:388 */       int overrideTypeCount = overrideTypes.getLength();
/* 193:389 */       for (int i = 0; i < overrideTypeCount; i++)
/* 194:    */       {
/* 195:390 */         Element element = (Element)overrideTypes.item(i);
/* 196:391 */         URI uri = new URI(element.getAttribute("PartName"));
/* 197:392 */         PackagePartName partName = PackagingURIHelper.createPartName(uri);
/* 198:393 */         String contentType = element.getAttribute("ContentType");
/* 199:394 */         addOverrideContentType(partName, contentType);
/* 200:    */       }
/* 201:    */     }
/* 202:    */     catch (URISyntaxException urie)
/* 203:    */     {
/* 204:397 */       throw new InvalidFormatException(urie.getMessage());
/* 205:    */     }
/* 206:    */     catch (SAXException e)
/* 207:    */     {
/* 208:399 */       throw new InvalidFormatException(e.getMessage());
/* 209:    */     }
/* 210:    */     catch (IOException e)
/* 211:    */     {
/* 212:401 */       throw new InvalidFormatException(e.getMessage());
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public boolean save(OutputStream outStream)
/* 217:    */   {
/* 218:414 */     Document xmlOutDoc = DocumentHelper.createDocument();
/* 219:    */     
/* 220:    */ 
/* 221:417 */     Element typesElem = xmlOutDoc.createElementNS("http://schemas.openxmlformats.org/package/2006/content-types", "Types");
/* 222:418 */     xmlOutDoc.appendChild(typesElem);
/* 223:421 */     for (Map.Entry<String, String> entry : this.defaultContentType.entrySet()) {
/* 224:422 */       appendDefaultType(typesElem, entry);
/* 225:    */     }
/* 226:426 */     if (this.overrideContentType != null) {
/* 227:427 */       for (Map.Entry<PackagePartName, String> entry : this.overrideContentType.entrySet()) {
/* 228:429 */         appendSpecificTypes(typesElem, entry);
/* 229:    */       }
/* 230:    */     }
/* 231:432 */     xmlOutDoc.normalize();
/* 232:    */     
/* 233:    */ 
/* 234:435 */     return saveImpl(xmlOutDoc, outStream);
/* 235:    */   }
/* 236:    */   
/* 237:    */   private void appendSpecificTypes(Element root, Map.Entry<PackagePartName, String> entry)
/* 238:    */   {
/* 239:449 */     Element specificType = root.getOwnerDocument().createElementNS("http://schemas.openxmlformats.org/package/2006/content-types", "Override");
/* 240:450 */     specificType.setAttribute("PartName", ((PackagePartName)entry.getKey()).getName());
/* 241:451 */     specificType.setAttribute("ContentType", (String)entry.getValue());
/* 242:452 */     root.appendChild(specificType);
/* 243:    */   }
/* 244:    */   
/* 245:    */   private void appendDefaultType(Element root, Map.Entry<String, String> entry)
/* 246:    */   {
/* 247:465 */     Element defaultType = root.getOwnerDocument().createElementNS("http://schemas.openxmlformats.org/package/2006/content-types", "Default");
/* 248:466 */     defaultType.setAttribute("Extension", (String)entry.getKey());
/* 249:467 */     defaultType.setAttribute("ContentType", (String)entry.getValue());
/* 250:468 */     root.appendChild(defaultType);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public abstract boolean saveImpl(Document paramDocument, OutputStream paramOutputStream);
/* 254:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.ContentTypeManager
 * JD-Core Version:    0.7.0.1
 */