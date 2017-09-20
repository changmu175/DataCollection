/*   1:    */ package org.apache.poi.openxml4j.opc.internal.unmarshallers;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.zip.ZipEntry;
/*   6:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   8:    */ import org.apache.poi.openxml4j.opc.ZipPackage;
/*   9:    */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*  10:    */ import org.apache.poi.openxml4j.opc.internal.PartUnmarshaller;
/*  11:    */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/*  12:    */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  13:    */ import org.apache.poi.util.DocumentHelper;
/*  14:    */ import org.w3c.dom.Attr;
/*  15:    */ import org.w3c.dom.Document;
/*  16:    */ import org.w3c.dom.Element;
/*  17:    */ import org.w3c.dom.NamedNodeMap;
/*  18:    */ import org.w3c.dom.NodeList;
/*  19:    */ import org.xml.sax.SAXException;
/*  20:    */ 
/*  21:    */ public final class PackagePropertiesUnmarshaller
/*  22:    */   implements PartUnmarshaller
/*  23:    */ {
/*  24:    */   protected static final String KEYWORD_CATEGORY = "category";
/*  25:    */   protected static final String KEYWORD_CONTENT_STATUS = "contentStatus";
/*  26:    */   protected static final String KEYWORD_CONTENT_TYPE = "contentType";
/*  27:    */   protected static final String KEYWORD_CREATED = "created";
/*  28:    */   protected static final String KEYWORD_CREATOR = "creator";
/*  29:    */   protected static final String KEYWORD_DESCRIPTION = "description";
/*  30:    */   protected static final String KEYWORD_IDENTIFIER = "identifier";
/*  31:    */   protected static final String KEYWORD_KEYWORDS = "keywords";
/*  32:    */   protected static final String KEYWORD_LANGUAGE = "language";
/*  33:    */   protected static final String KEYWORD_LAST_MODIFIED_BY = "lastModifiedBy";
/*  34:    */   protected static final String KEYWORD_LAST_PRINTED = "lastPrinted";
/*  35:    */   protected static final String KEYWORD_MODIFIED = "modified";
/*  36:    */   protected static final String KEYWORD_REVISION = "revision";
/*  37:    */   protected static final String KEYWORD_SUBJECT = "subject";
/*  38:    */   protected static final String KEYWORD_TITLE = "title";
/*  39:    */   protected static final String KEYWORD_VERSION = "version";
/*  40:    */   
/*  41:    */   public PackagePart unmarshall(UnmarshallContext context, InputStream in)
/*  42:    */     throws InvalidFormatException, IOException
/*  43:    */   {
/*  44: 85 */     PackagePropertiesPart coreProps = new PackagePropertiesPart(context.getPackage(), context.getPartName());
/*  45: 90 */     if (in == null) {
/*  46: 91 */       if (context.getZipEntry() != null)
/*  47:    */       {
/*  48: 92 */         in = ((ZipPackage)context.getPackage()).getZipArchive().getInputStream(context.getZipEntry());
/*  49:    */       }
/*  50: 94 */       else if (context.getPackage() != null)
/*  51:    */       {
/*  52: 96 */         ZipEntry zipEntry = ZipHelper.getCorePropertiesZipEntry((ZipPackage)context.getPackage());
/*  53:    */         
/*  54:    */ 
/*  55: 99 */         in = ((ZipPackage)context.getPackage()).getZipArchive().getInputStream(zipEntry);
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59:102 */         throw new IOException("Error while trying to get the part input stream.");
/*  60:    */       }
/*  61:    */     }
/*  62:    */     Document xmlDoc;
/*  63:    */     try
/*  64:    */     {
/*  65:108 */       xmlDoc = DocumentHelper.readDocument(in);
/*  66:    */       
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:113 */       checkElementForOPCCompliance(xmlDoc.getDocumentElement());
/*  71:    */     }
/*  72:    */     catch (SAXException e)
/*  73:    */     {
/*  74:118 */       throw new IOException(e.getMessage());
/*  75:    */     }
/*  76:121 */     coreProps.setCategoryProperty(loadCategory(xmlDoc));
/*  77:122 */     coreProps.setContentStatusProperty(loadContentStatus(xmlDoc));
/*  78:123 */     coreProps.setContentTypeProperty(loadContentType(xmlDoc));
/*  79:124 */     coreProps.setCreatedProperty(loadCreated(xmlDoc));
/*  80:125 */     coreProps.setCreatorProperty(loadCreator(xmlDoc));
/*  81:126 */     coreProps.setDescriptionProperty(loadDescription(xmlDoc));
/*  82:127 */     coreProps.setIdentifierProperty(loadIdentifier(xmlDoc));
/*  83:128 */     coreProps.setKeywordsProperty(loadKeywords(xmlDoc));
/*  84:129 */     coreProps.setLanguageProperty(loadLanguage(xmlDoc));
/*  85:130 */     coreProps.setLastModifiedByProperty(loadLastModifiedBy(xmlDoc));
/*  86:131 */     coreProps.setLastPrintedProperty(loadLastPrinted(xmlDoc));
/*  87:132 */     coreProps.setModifiedProperty(loadModified(xmlDoc));
/*  88:133 */     coreProps.setRevisionProperty(loadRevision(xmlDoc));
/*  89:134 */     coreProps.setSubjectProperty(loadSubject(xmlDoc));
/*  90:135 */     coreProps.setTitleProperty(loadTitle(xmlDoc));
/*  91:136 */     coreProps.setVersionProperty(loadVersion(xmlDoc));
/*  92:    */     
/*  93:138 */     return coreProps;
/*  94:    */   }
/*  95:    */   
/*  96:    */   private String readElement(Document xmlDoc, String localName, String namespaceURI)
/*  97:    */   {
/*  98:142 */     Element el = (Element)xmlDoc.getDocumentElement().getElementsByTagNameNS(namespaceURI, localName).item(0);
/*  99:143 */     if (el == null) {
/* 100:144 */       return null;
/* 101:    */     }
/* 102:146 */     return el.getTextContent();
/* 103:    */   }
/* 104:    */   
/* 105:    */   private String loadCategory(Document xmlDoc)
/* 106:    */   {
/* 107:150 */     return readElement(xmlDoc, "category", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 108:    */   }
/* 109:    */   
/* 110:    */   private String loadContentStatus(Document xmlDoc)
/* 111:    */   {
/* 112:154 */     return readElement(xmlDoc, "contentStatus", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 113:    */   }
/* 114:    */   
/* 115:    */   private String loadContentType(Document xmlDoc)
/* 116:    */   {
/* 117:158 */     return readElement(xmlDoc, "contentType", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 118:    */   }
/* 119:    */   
/* 120:    */   private String loadCreated(Document xmlDoc)
/* 121:    */   {
/* 122:162 */     return readElement(xmlDoc, "created", "http://purl.org/dc/terms/");
/* 123:    */   }
/* 124:    */   
/* 125:    */   private String loadCreator(Document xmlDoc)
/* 126:    */   {
/* 127:166 */     return readElement(xmlDoc, "creator", "http://purl.org/dc/elements/1.1/");
/* 128:    */   }
/* 129:    */   
/* 130:    */   private String loadDescription(Document xmlDoc)
/* 131:    */   {
/* 132:170 */     return readElement(xmlDoc, "description", "http://purl.org/dc/elements/1.1/");
/* 133:    */   }
/* 134:    */   
/* 135:    */   private String loadIdentifier(Document xmlDoc)
/* 136:    */   {
/* 137:174 */     return readElement(xmlDoc, "identifier", "http://purl.org/dc/elements/1.1/");
/* 138:    */   }
/* 139:    */   
/* 140:    */   private String loadKeywords(Document xmlDoc)
/* 141:    */   {
/* 142:178 */     return readElement(xmlDoc, "keywords", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 143:    */   }
/* 144:    */   
/* 145:    */   private String loadLanguage(Document xmlDoc)
/* 146:    */   {
/* 147:182 */     return readElement(xmlDoc, "language", "http://purl.org/dc/elements/1.1/");
/* 148:    */   }
/* 149:    */   
/* 150:    */   private String loadLastModifiedBy(Document xmlDoc)
/* 151:    */   {
/* 152:186 */     return readElement(xmlDoc, "lastModifiedBy", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 153:    */   }
/* 154:    */   
/* 155:    */   private String loadLastPrinted(Document xmlDoc)
/* 156:    */   {
/* 157:190 */     return readElement(xmlDoc, "lastPrinted", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 158:    */   }
/* 159:    */   
/* 160:    */   private String loadModified(Document xmlDoc)
/* 161:    */   {
/* 162:194 */     return readElement(xmlDoc, "modified", "http://purl.org/dc/terms/");
/* 163:    */   }
/* 164:    */   
/* 165:    */   private String loadRevision(Document xmlDoc)
/* 166:    */   {
/* 167:198 */     return readElement(xmlDoc, "revision", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 168:    */   }
/* 169:    */   
/* 170:    */   private String loadSubject(Document xmlDoc)
/* 171:    */   {
/* 172:202 */     return readElement(xmlDoc, "subject", "http://purl.org/dc/elements/1.1/");
/* 173:    */   }
/* 174:    */   
/* 175:    */   private String loadTitle(Document xmlDoc)
/* 176:    */   {
/* 177:206 */     return readElement(xmlDoc, "title", "http://purl.org/dc/elements/1.1/");
/* 178:    */   }
/* 179:    */   
/* 180:    */   private String loadVersion(Document xmlDoc)
/* 181:    */   {
/* 182:210 */     return readElement(xmlDoc, "version", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void checkElementForOPCCompliance(Element el)
/* 186:    */     throws InvalidFormatException
/* 187:    */   {
/* 188:241 */     NamedNodeMap namedNodeMap = el.getAttributes();
/* 189:242 */     int namedNodeCount = namedNodeMap.getLength();
/* 190:243 */     for (int i = 0; i < namedNodeCount; i++)
/* 191:    */     {
/* 192:244 */       Attr attr = (Attr)namedNodeMap.item(0);
/* 193:246 */       if (attr.getNamespaceURI().equals("http://www.w3.org/2000/xmlns/")) {
/* 194:248 */         if (attr.getValue().equals("http://schemas.openxmlformats.org/markup-compatibility/2006")) {
/* 195:249 */           throw new InvalidFormatException("OPC Compliance error [M4.2]: A format consumer shall consider the use of the Markup Compatibility namespace to be an error.");
/* 196:    */         }
/* 197:    */       }
/* 198:    */     }
/* 199:256 */     String elName = el.getLocalName();
/* 200:257 */     if ((el.getNamespaceURI().equals("http://purl.org/dc/terms/")) && 
/* 201:258 */       (!elName.equals("created")) && (!elName.equals("modified"))) {
/* 202:259 */       throw new InvalidFormatException("OPC Compliance error [M4.3]: Producers shall not create a document element that contains refinements to the Dublin Core elements, except for the two specified in the schema: <dcterms:created> and <dcterms:modified> Consumers shall consider a document element that violates this constraint to be an error.");
/* 203:    */     }
/* 204:263 */     if (el.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "lang") != null) {
/* 205:264 */       throw new InvalidFormatException("OPC Compliance error [M4.4]: Producers shall not create a document element that contains the xml:lang attribute. Consumers shall consider a document element that violates this constraint to be an error.");
/* 206:    */     }
/* 207:268 */     if (el.getNamespaceURI().equals("http://purl.org/dc/terms/"))
/* 208:    */     {
/* 209:270 */       if ((!elName.equals("created")) && (!elName.equals("modified"))) {
/* 210:271 */         throw new InvalidFormatException("Namespace error : " + elName + " shouldn't have the following naemspace -> " + "http://purl.org/dc/terms/");
/* 211:    */       }
/* 212:276 */       Attr typeAtt = el.getAttributeNodeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
/* 213:277 */       if (typeAtt == null) {
/* 214:278 */         throw new InvalidFormatException("The element '" + elName + "' must have the 'xsi:type' attribute present !");
/* 215:    */       }
/* 216:282 */       if (!typeAtt.getValue().equals(el.getPrefix() + ":W3CDTF")) {
/* 217:283 */         throw new InvalidFormatException("The element '" + elName + "' must have the 'xsi:type' attribute with the value '" + el.getPrefix() + ":W3CDTF', but had '" + typeAtt.getValue() + "' !");
/* 218:    */       }
/* 219:    */     }
/* 220:288 */     NodeList childElements = el.getElementsByTagName("*");
/* 221:289 */     int childElementCount = childElements.getLength();
/* 222:290 */     for (int i = 0; i < childElementCount; i++) {
/* 223:291 */       checkElementForOPCCompliance((Element)childElements.item(i));
/* 224:    */     }
/* 225:    */   }
/* 226:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.unmarshallers.PackagePropertiesUnmarshaller
 * JD-Core Version:    0.7.0.1
 */