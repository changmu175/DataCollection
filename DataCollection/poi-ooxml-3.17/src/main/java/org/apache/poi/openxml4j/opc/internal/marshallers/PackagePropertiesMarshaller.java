/*   1:    */ package org.apache.poi.openxml4j.opc.internal.marshallers;
/*   2:    */ 
/*   3:    */ import java.io.OutputStream;
/*   4:    */ import javax.xml.stream.XMLEventFactory;
/*   5:    */ import javax.xml.stream.events.Namespace;
/*   6:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   8:    */ import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
/*   9:    */ import org.apache.poi.openxml4j.opc.internal.PartMarshaller;
/*  10:    */ import org.apache.poi.openxml4j.util.Nullable;
/*  11:    */ import org.apache.poi.util.DocumentHelper;
/*  12:    */ import org.w3c.dom.Document;
/*  13:    */ import org.w3c.dom.Element;
/*  14:    */ import org.w3c.dom.NodeList;
/*  15:    */ 
/*  16:    */ public class PackagePropertiesMarshaller
/*  17:    */   implements PartMarshaller
/*  18:    */ {
/*  19:    */   private static final Namespace namespaceDC;
/*  20:    */   private static final Namespace namespaceCoreProperties;
/*  21:    */   private static final Namespace namespaceDcTerms;
/*  22:    */   private static final Namespace namespaceXSI;
/*  23:    */   protected static final String KEYWORD_CATEGORY = "category";
/*  24:    */   protected static final String KEYWORD_CONTENT_STATUS = "contentStatus";
/*  25:    */   protected static final String KEYWORD_CONTENT_TYPE = "contentType";
/*  26:    */   protected static final String KEYWORD_CREATED = "created";
/*  27:    */   protected static final String KEYWORD_CREATOR = "creator";
/*  28:    */   protected static final String KEYWORD_DESCRIPTION = "description";
/*  29:    */   protected static final String KEYWORD_IDENTIFIER = "identifier";
/*  30:    */   protected static final String KEYWORD_KEYWORDS = "keywords";
/*  31:    */   protected static final String KEYWORD_LANGUAGE = "language";
/*  32:    */   protected static final String KEYWORD_LAST_MODIFIED_BY = "lastModifiedBy";
/*  33:    */   protected static final String KEYWORD_LAST_PRINTED = "lastPrinted";
/*  34:    */   protected static final String KEYWORD_MODIFIED = "modified";
/*  35:    */   protected static final String KEYWORD_REVISION = "revision";
/*  36:    */   protected static final String KEYWORD_SUBJECT = "subject";
/*  37:    */   protected static final String KEYWORD_TITLE = "title";
/*  38:    */   protected static final String KEYWORD_VERSION = "version";
/*  39:    */   PackagePropertiesPart propsPart;
/*  40:    */   
/*  41:    */   static
/*  42:    */   {
/*  43: 41 */     XMLEventFactory f = XMLEventFactory.newInstance();
/*  44: 42 */     namespaceDC = f.createNamespace("dc", "http://purl.org/dc/elements/1.1/");
/*  45: 43 */     namespaceCoreProperties = f.createNamespace("cp", "http://schemas.openxmlformats.org/package/2006/metadata/core-properties");
/*  46: 44 */     namespaceDcTerms = f.createNamespace("dcterms", "http://purl.org/dc/terms/");
/*  47: 45 */     namespaceXSI = f.createNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
/*  48:    */   }
/*  49:    */   
/*  50: 83 */   Document xmlDoc = null;
/*  51:    */   
/*  52:    */   public boolean marshall(PackagePart part, OutputStream out)
/*  53:    */     throws OpenXML4JException
/*  54:    */   {
/*  55: 92 */     if (!(part instanceof PackagePropertiesPart)) {
/*  56: 93 */       throw new IllegalArgumentException("'part' must be a PackagePropertiesPart instance.");
/*  57:    */     }
/*  58: 95 */     this.propsPart = ((PackagePropertiesPart)part);
/*  59:    */     
/*  60:    */ 
/*  61: 98 */     this.xmlDoc = DocumentHelper.createDocument();
/*  62: 99 */     Element rootElem = this.xmlDoc.createElementNS(namespaceCoreProperties.getNamespaceURI(), getQName("coreProperties", namespaceCoreProperties));
/*  63:    */     
/*  64:101 */     DocumentHelper.addNamespaceDeclaration(rootElem, namespaceCoreProperties);
/*  65:102 */     DocumentHelper.addNamespaceDeclaration(rootElem, namespaceDC);
/*  66:103 */     DocumentHelper.addNamespaceDeclaration(rootElem, namespaceDcTerms);
/*  67:104 */     DocumentHelper.addNamespaceDeclaration(rootElem, namespaceXSI);
/*  68:105 */     this.xmlDoc.appendChild(rootElem);
/*  69:    */     
/*  70:107 */     addCategory();
/*  71:108 */     addContentStatus();
/*  72:109 */     addContentType();
/*  73:110 */     addCreated();
/*  74:111 */     addCreator();
/*  75:112 */     addDescription();
/*  76:113 */     addIdentifier();
/*  77:114 */     addKeywords();
/*  78:115 */     addLanguage();
/*  79:116 */     addLastModifiedBy();
/*  80:117 */     addLastPrinted();
/*  81:118 */     addModified();
/*  82:119 */     addRevision();
/*  83:120 */     addSubject();
/*  84:121 */     addTitle();
/*  85:122 */     addVersion();
/*  86:123 */     return true;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private Element setElementTextContent(String localName, Namespace namespace, Nullable<String> property)
/*  90:    */   {
/*  91:130 */     return setElementTextContent(localName, namespace, property, (String)property.getValue());
/*  92:    */   }
/*  93:    */   
/*  94:    */   private String getQName(String localName, Namespace namespace)
/*  95:    */   {
/*  96:134 */     return namespace.getPrefix() + ':' + localName;
/*  97:    */   }
/*  98:    */   
/*  99:    */   private Element setElementTextContent(String localName, Namespace namespace, Nullable<?> property, String propertyValue)
/* 100:    */   {
/* 101:138 */     if (!property.hasValue()) {
/* 102:139 */       return null;
/* 103:    */     }
/* 104:141 */     Element root = this.xmlDoc.getDocumentElement();
/* 105:142 */     Element elem = (Element)root.getElementsByTagNameNS(namespace.getNamespaceURI(), localName).item(0);
/* 106:143 */     if (elem == null)
/* 107:    */     {
/* 108:145 */       elem = this.xmlDoc.createElementNS(namespace.getNamespaceURI(), getQName(localName, namespace));
/* 109:146 */       root.appendChild(elem);
/* 110:    */     }
/* 111:148 */     elem.setTextContent(propertyValue);
/* 112:149 */     return elem;
/* 113:    */   }
/* 114:    */   
/* 115:    */   private Element setElementTextContent(String localName, Namespace namespace, Nullable<?> property, String propertyValue, String xsiType)
/* 116:    */   {
/* 117:153 */     Element element = setElementTextContent(localName, namespace, property, propertyValue);
/* 118:154 */     if (element != null) {
/* 119:155 */       element.setAttributeNS(namespaceXSI.getNamespaceURI(), getQName("type", namespaceXSI), xsiType);
/* 120:    */     }
/* 121:157 */     return element;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private void addCategory()
/* 125:    */   {
/* 126:165 */     setElementTextContent("category", namespaceCoreProperties, this.propsPart.getCategoryProperty());
/* 127:    */   }
/* 128:    */   
/* 129:    */   private void addContentStatus()
/* 130:    */   {
/* 131:172 */     setElementTextContent("contentStatus", namespaceCoreProperties, this.propsPart.getContentStatusProperty());
/* 132:    */   }
/* 133:    */   
/* 134:    */   private void addContentType()
/* 135:    */   {
/* 136:179 */     setElementTextContent("contentType", namespaceCoreProperties, this.propsPart.getContentTypeProperty());
/* 137:    */   }
/* 138:    */   
/* 139:    */   private void addCreated()
/* 140:    */   {
/* 141:186 */     setElementTextContent("created", namespaceDcTerms, this.propsPart.getCreatedProperty(), this.propsPart.getCreatedPropertyString(), "dcterms:W3CDTF");
/* 142:    */   }
/* 143:    */   
/* 144:    */   private void addCreator()
/* 145:    */   {
/* 146:194 */     setElementTextContent("creator", namespaceDC, this.propsPart.getCreatorProperty());
/* 147:    */   }
/* 148:    */   
/* 149:    */   private void addDescription()
/* 150:    */   {
/* 151:201 */     setElementTextContent("description", namespaceDC, this.propsPart.getDescriptionProperty());
/* 152:    */   }
/* 153:    */   
/* 154:    */   private void addIdentifier()
/* 155:    */   {
/* 156:208 */     setElementTextContent("identifier", namespaceDC, this.propsPart.getIdentifierProperty());
/* 157:    */   }
/* 158:    */   
/* 159:    */   private void addKeywords()
/* 160:    */   {
/* 161:215 */     setElementTextContent("keywords", namespaceCoreProperties, this.propsPart.getKeywordsProperty());
/* 162:    */   }
/* 163:    */   
/* 164:    */   private void addLanguage()
/* 165:    */   {
/* 166:222 */     setElementTextContent("language", namespaceDC, this.propsPart.getLanguageProperty());
/* 167:    */   }
/* 168:    */   
/* 169:    */   private void addLastModifiedBy()
/* 170:    */   {
/* 171:229 */     setElementTextContent("lastModifiedBy", namespaceCoreProperties, this.propsPart.getLastModifiedByProperty());
/* 172:    */   }
/* 173:    */   
/* 174:    */   private void addLastPrinted()
/* 175:    */   {
/* 176:237 */     setElementTextContent("lastPrinted", namespaceCoreProperties, this.propsPart.getLastPrintedProperty(), this.propsPart.getLastPrintedPropertyString());
/* 177:    */   }
/* 178:    */   
/* 179:    */   private void addModified()
/* 180:    */   {
/* 181:244 */     setElementTextContent("modified", namespaceDcTerms, this.propsPart.getModifiedProperty(), this.propsPart.getModifiedPropertyString(), "dcterms:W3CDTF");
/* 182:    */   }
/* 183:    */   
/* 184:    */   private void addRevision()
/* 185:    */   {
/* 186:252 */     setElementTextContent("revision", namespaceCoreProperties, this.propsPart.getRevisionProperty());
/* 187:    */   }
/* 188:    */   
/* 189:    */   private void addSubject()
/* 190:    */   {
/* 191:259 */     setElementTextContent("subject", namespaceDC, this.propsPart.getSubjectProperty());
/* 192:    */   }
/* 193:    */   
/* 194:    */   private void addTitle()
/* 195:    */   {
/* 196:266 */     setElementTextContent("title", namespaceDC, this.propsPart.getTitleProperty());
/* 197:    */   }
/* 198:    */   
/* 199:    */   private void addVersion()
/* 200:    */   {
/* 201:270 */     setElementTextContent("version", namespaceCoreProperties, this.propsPart.getVersionProperty());
/* 202:    */   }
/* 203:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.marshallers.PackagePropertiesMarshaller
 * JD-Core Version:    0.7.0.1
 */