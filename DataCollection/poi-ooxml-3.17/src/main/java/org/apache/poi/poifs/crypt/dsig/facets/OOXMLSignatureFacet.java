/*   1:    */ package org.apache.poi.poifs.crypt.dsig.facets;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.x2006.digsig.CTSignatureInfoV1;
/*   4:    */ import com.microsoft.schemas.office.x2006.digsig.SignatureInfoV1Document;
/*   5:    */ import com.microsoft.schemas.office.x2006.digsig.SignatureInfoV1Document.Factory;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.net.URISyntaxException;
/*   8:    */ import java.text.DateFormat;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Collections;
/*  13:    */ import java.util.Comparator;
/*  14:    */ import java.util.HashSet;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Locale;
/*  17:    */ import java.util.Set;
/*  18:    */ import javax.xml.crypto.XMLStructure;
/*  19:    */ import javax.xml.crypto.dom.DOMStructure;
/*  20:    */ import javax.xml.crypto.dsig.Manifest;
/*  21:    */ import javax.xml.crypto.dsig.Reference;
/*  22:    */ import javax.xml.crypto.dsig.SignatureProperties;
/*  23:    */ import javax.xml.crypto.dsig.SignatureProperty;
/*  24:    */ import javax.xml.crypto.dsig.Transform;
/*  25:    */ import javax.xml.crypto.dsig.XMLObject;
/*  26:    */ import javax.xml.crypto.dsig.XMLSignatureException;
/*  27:    */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*  28:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  29:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  30:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  31:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  32:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  33:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  34:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  35:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  36:    */ import org.apache.poi.poifs.crypt.dsig.SignatureConfig;
/*  37:    */ import org.apache.poi.poifs.crypt.dsig.services.RelationshipTransformService.RelationshipTransformParameterSpec;
/*  38:    */ import org.apache.poi.util.LocaleUtil;
/*  39:    */ import org.apache.poi.util.POILogFactory;
/*  40:    */ import org.apache.poi.util.POILogger;
/*  41:    */ import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime;
/*  42:    */ import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument;
/*  43:    */ import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument.Factory;
/*  44:    */ import org.w3c.dom.Document;
/*  45:    */ import org.w3c.dom.Element;
/*  46:    */ 
/*  47:    */ public class OOXMLSignatureFacet
/*  48:    */   extends SignatureFacet
/*  49:    */ {
/*  50: 81 */   private static final POILogger LOG = POILogFactory.getLogger(OOXMLSignatureFacet.class);
/*  51:    */   private static final String ID_PACKAGE_OBJECT = "idPackageObject";
/*  52:    */   
/*  53:    */   public void preSign(Document document, List<Reference> references, List<XMLObject> objects)
/*  54:    */     throws XMLSignatureException
/*  55:    */   {
/*  56: 90 */     LOG.log(1, new Object[] { "pre sign" });
/*  57: 91 */     addManifestObject(document, references, objects);
/*  58: 92 */     addSignatureInfo(document, references, objects);
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected void addManifestObject(Document document, List<Reference> references, List<XMLObject> objects)
/*  62:    */     throws XMLSignatureException
/*  63:    */   {
/*  64:101 */     List<Reference> manifestReferences = new ArrayList();
/*  65:102 */     addManifestReferences(manifestReferences);
/*  66:103 */     Manifest manifest = getSignatureFactory().newManifest(manifestReferences);
/*  67:    */     
/*  68:105 */     List<XMLStructure> objectContent = new ArrayList();
/*  69:106 */     objectContent.add(manifest);
/*  70:    */     
/*  71:108 */     addSignatureTime(document, objectContent);
/*  72:    */     
/*  73:110 */     XMLObject xo = getSignatureFactory().newXMLObject(objectContent, "idPackageObject", null, null);
/*  74:111 */     objects.add(xo);
/*  75:    */     
/*  76:113 */     Reference reference = newReference("#idPackageObject", null, "http://www.w3.org/2000/09/xmldsig#Object", null, null);
/*  77:114 */     references.add(reference);
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected void addManifestReferences(List<Reference> manifestReferences)
/*  81:    */     throws XMLSignatureException
/*  82:    */   {
/*  83:121 */     OPCPackage ooxml = this.signatureConfig.getOpcPackage();
/*  84:122 */     List<PackagePart> relsEntryNames = ooxml.getPartsByContentType("application/vnd.openxmlformats-package.relationships+xml");
/*  85:    */     
/*  86:124 */     Set<String> digestedPartNames = new HashSet();
/*  87:125 */     for (PackagePart pp : relsEntryNames)
/*  88:    */     {
/*  89:126 */       String baseUri = pp.getPartName().getName().replaceFirst("(.*)/_rels/.*", "$1");
/*  90:    */       PackageRelationshipCollection prc;
/*  91:    */       try
/*  92:    */       {
/*  93:130 */         prc = new PackageRelationshipCollection(ooxml);
/*  94:131 */         prc.parseRelationshipsPart(pp);
/*  95:    */       }
/*  96:    */       catch (InvalidFormatException e)
/*  97:    */       {
/*  98:133 */         throw new XMLSignatureException("Invalid relationship descriptor: " + pp.getPartName().getName(), e);
/*  99:    */       }
/* 100:136 */       RelationshipTransformService.RelationshipTransformParameterSpec parameterSpec = new RelationshipTransformService.RelationshipTransformParameterSpec();
/* 101:137 */       for (PackageRelationship relationship : prc)
/* 102:    */       {
/* 103:138 */         String relationshipType = relationship.getRelationshipType();
/* 104:145 */         if ((TargetMode.EXTERNAL != relationship.getTargetMode()) && 
/* 105:    */         
/* 106:    */ 
/* 107:    */ 
/* 108:149 */           (isSignedRelationship(relationshipType)))
/* 109:    */         {
/* 110:153 */           parameterSpec.addRelationshipReference(relationship.getId());
/* 111:    */           
/* 112:155 */           String partName = normalizePartName(relationship.getTargetURI(), baseUri);
/* 113:158 */           if (!digestedPartNames.contains(partName))
/* 114:    */           {
/* 115:161 */             digestedPartNames.add(partName);
/* 116:    */             String contentType;
/* 117:    */             try
/* 118:    */             {
/* 119:165 */               PackagePartName relName = PackagingURIHelper.createPartName(partName);
/* 120:166 */               PackagePart pp2 = ooxml.getPart(relName);
/* 121:167 */               contentType = pp2.getContentType();
/* 122:    */             }
/* 123:    */             catch (InvalidFormatException e)
/* 124:    */             {
/* 125:169 */               throw new XMLSignatureException(e);
/* 126:    */             }
/* 127:172 */             if ((relationshipType.endsWith("customXml")) && (!contentType.equals("inkml+xml")) && (!contentType.equals("text/xml")))
/* 128:    */             {
/* 129:174 */               LOG.log(1, new Object[] { "skipping customXml with content type: " + contentType });
/* 130:    */             }
/* 131:    */             else
/* 132:    */             {
/* 133:178 */               String uri = partName + "?ContentType=" + contentType;
/* 134:179 */               Reference reference = newReference(uri, null, null, null, null);
/* 135:180 */               manifestReferences.add(reference);
/* 136:    */             }
/* 137:    */           }
/* 138:    */         }
/* 139:    */       }
/* 140:183 */       if (parameterSpec.hasSourceIds())
/* 141:    */       {
/* 142:184 */         List<Transform> transforms = new ArrayList();
/* 143:185 */         transforms.add(newTransform("http://schemas.openxmlformats.org/package/2006/RelationshipTransform", parameterSpec));
/* 144:186 */         transforms.add(newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"));
/* 145:187 */         String uri = normalizePartName(pp.getPartName().getURI(), baseUri) + "?ContentType=application/vnd.openxmlformats-package.relationships+xml";
/* 146:    */         
/* 147:189 */         Reference reference = newReference(uri, transforms, null, null, null);
/* 148:190 */         manifestReferences.add(reference);
/* 149:    */       }
/* 150:    */     }
/* 151:194 */     Collections.sort(manifestReferences, new Comparator()
/* 152:    */     {
/* 153:    */       public int compare(Reference o1, Reference o2)
/* 154:    */       {
/* 155:196 */         return o1.getURI().compareTo(o2.getURI());
/* 156:    */       }
/* 157:    */     });
/* 158:    */   }
/* 159:    */   
/* 160:    */   private static String normalizePartName(URI partName, String baseUri)
/* 161:    */     throws XMLSignatureException
/* 162:    */   {
/* 163:206 */     String pn = partName.toASCIIString();
/* 164:207 */     if (!pn.startsWith(baseUri)) {
/* 165:208 */       pn = baseUri + pn;
/* 166:    */     }
/* 167:    */     try
/* 168:    */     {
/* 169:211 */       pn = new URI(pn).normalize().getPath().replace('\\', '/');
/* 170:212 */       LOG.log(1, new Object[] { "part name: " + pn });
/* 171:    */     }
/* 172:    */     catch (URISyntaxException e)
/* 173:    */     {
/* 174:214 */       throw new XMLSignatureException(e);
/* 175:    */     }
/* 176:216 */     return pn;
/* 177:    */   }
/* 178:    */   
/* 179:    */   protected void addSignatureTime(Document document, List<XMLStructure> objectContent)
/* 180:    */   {
/* 181:224 */     DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
/* 182:225 */     fmt.setTimeZone(LocaleUtil.TIMEZONE_UTC);
/* 183:226 */     String nowStr = fmt.format(this.signatureConfig.getExecutionTime());
/* 184:227 */     LOG.log(1, new Object[] { "now: " + nowStr });
/* 185:    */     
/* 186:229 */     SignatureTimeDocument sigTime = SignatureTimeDocument.Factory.newInstance();
/* 187:230 */     CTSignatureTime ctTime = sigTime.addNewSignatureTime();
/* 188:231 */     ctTime.setFormat("YYYY-MM-DDThh:mm:ssTZD");
/* 189:232 */     ctTime.setValue(nowStr);
/* 190:    */     
/* 191:234 */     Element n = (Element)document.importNode(ctTime.getDomNode(), true);
/* 192:235 */     List<XMLStructure> signatureTimeContent = new ArrayList();
/* 193:236 */     signatureTimeContent.add(new DOMStructure(n));
/* 194:237 */     SignatureProperty signatureTimeSignatureProperty = getSignatureFactory().newSignatureProperty(signatureTimeContent, "#" + this.signatureConfig.getPackageSignatureId(), "idSignatureTime");
/* 195:    */     
/* 196:    */ 
/* 197:240 */     List<SignatureProperty> signaturePropertyContent = new ArrayList();
/* 198:241 */     signaturePropertyContent.add(signatureTimeSignatureProperty);
/* 199:242 */     SignatureProperties signatureProperties = getSignatureFactory().newSignatureProperties(signaturePropertyContent, null);
/* 200:    */     
/* 201:244 */     objectContent.add(signatureProperties);
/* 202:    */   }
/* 203:    */   
/* 204:    */   protected void addSignatureInfo(Document document, List<Reference> references, List<XMLObject> objects)
/* 205:    */     throws XMLSignatureException
/* 206:    */   {
/* 207:251 */     List<XMLStructure> objectContent = new ArrayList();
/* 208:    */     
/* 209:253 */     SignatureInfoV1Document sigV1 = SignatureInfoV1Document.Factory.newInstance();
/* 210:254 */     CTSignatureInfoV1 ctSigV1 = sigV1.addNewSignatureInfoV1();
/* 211:255 */     ctSigV1.setManifestHashAlgorithm(this.signatureConfig.getDigestMethodUri());
/* 212:256 */     Element n = (Element)document.importNode(ctSigV1.getDomNode(), true);
/* 213:257 */     n.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://schemas.microsoft.com/office/2006/digsig");
/* 214:    */     
/* 215:259 */     List<XMLStructure> signatureInfoContent = new ArrayList();
/* 216:260 */     signatureInfoContent.add(new DOMStructure(n));
/* 217:261 */     SignatureProperty signatureInfoSignatureProperty = getSignatureFactory().newSignatureProperty(signatureInfoContent, "#" + this.signatureConfig.getPackageSignatureId(), "idOfficeV1Details");
/* 218:    */     
/* 219:    */ 
/* 220:    */ 
/* 221:265 */     List<SignatureProperty> signaturePropertyContent = new ArrayList();
/* 222:266 */     signaturePropertyContent.add(signatureInfoSignatureProperty);
/* 223:267 */     SignatureProperties signatureProperties = getSignatureFactory().newSignatureProperties(signaturePropertyContent, null);
/* 224:    */     
/* 225:269 */     objectContent.add(signatureProperties);
/* 226:    */     
/* 227:271 */     String objectId = "idOfficeObject";
/* 228:272 */     objects.add(getSignatureFactory().newXMLObject(objectContent, objectId, null, null));
/* 229:    */     
/* 230:274 */     Reference reference = newReference("#" + objectId, null, "http://www.w3.org/2000/09/xmldsig#Object", null, null);
/* 231:275 */     references.add(reference);
/* 232:    */   }
/* 233:    */   
/* 234:    */   protected static String getRelationshipReferenceURI(String zipEntryName)
/* 235:    */   {
/* 236:279 */     return "/" + zipEntryName + "?ContentType=application/vnd.openxmlformats-package.relationships+xml";
/* 237:    */   }
/* 238:    */   
/* 239:    */   protected static String getResourceReferenceURI(String resourceName, String contentType)
/* 240:    */   {
/* 241:285 */     return "/" + resourceName + "?ContentType=" + contentType;
/* 242:    */   }
/* 243:    */   
/* 244:    */   protected static boolean isSignedRelationship(String relationshipType)
/* 245:    */   {
/* 246:289 */     LOG.log(1, new Object[] { "relationship type: " + relationshipType });
/* 247:290 */     String rt = relationshipType.replaceFirst(".*/relationships/", "");
/* 248:291 */     return (signed.contains(rt)) || (rt.endsWith("customXml"));
/* 249:    */   }
/* 250:    */   
/* 251:297 */   private static final Set<String> signed = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "activeXControlBinary", "aFChunk", "attachedTemplate", "attachedToolbars", "audio", "calcChain", "chart", "chartColorStyle", "chartLayout", "chartsheet", "chartStyle", "chartUserShapes", "commentAuthors", "comments", "connections", "connectorXml", "control", "ctrlProp", "customData", "customData", "customProperty", "customXml", "diagram", "diagramColors", "diagramColorsHeader", "diagramData", "diagramDrawing", "diagramLayout", "diagramLayoutHeader", "diagramQuickStyle", "diagramQuickStyleHeader", "dialogsheet", "dictionary", "documentParts", "downRev", "drawing", "endnotes", "externalLink", "externalLinkPath", "font", "fontTable", "footer", "footnotes", "functionPrototypes", "glossaryDocument", "graphicFrameDoc", "groupShapeXml", "handoutMaster", "hdphoto", "header", "hyperlink", "image", "ink", "inkXml", "keyMapCustomizations", "legacyDiagramText", "legacyDocTextInfo", "mailMergeHeaderSource", "mailMergeRecipientData", "mailMergeSource", "media", "notesMaster", "notesSlide", "numbering", "officeDocument", "officeDocument", "oleObject", "package", "pictureXml", "pivotCacheDefinition", "pivotCacheRecords", "pivotTable", "powerPivotData", "presProps", "printerSettings", "queryTable", "recipientData", "settings", "shapeXml", "sharedStrings", "sheetMetadata", "slicer", "slicer", "slicerCache", "slicerCache", "slide", "slideLayout", "slideMaster", "slideUpdateInfo", "slideUpdateUrl", "smartTags", "styles", "stylesWithEffects", "table", "tableSingleCells", "tableStyles", "tags", "theme", "themeOverride", "timeline", "timelineCache", "transform", "ui/altText", "ui/buttonSize", "ui/controlID", "ui/description", "ui/enabled", "ui/extensibility", "ui/extensibility", "ui/helperText", "ui/imageID", "ui/imageMso", "ui/keyTip", "ui/label", "ui/lcid", "ui/loud", "ui/pressed", "ui/progID", "ui/ribbonID", "ui/showImage", "ui/showLabel", "ui/supertip", "ui/target", "ui/text", "ui/title", "ui/tooltip", "ui/userCustomization", "ui/visible", "userXmlData", "vbaProject", "video", "viewProps", "vmlDrawing", "volatileDependencies", "webSettings", "wordVbaData", "worksheet", "wsSortMap", "xlBinaryIndex", "xlExternalLinkPath/xlAlternateStartup", "xlExternalLinkPath/xlLibrary", "xlExternalLinkPath/xlPathMissing", "xlExternalLinkPath/xlStartup", "xlIntlMacrosheet", "xlMacrosheet", "xmlMaps" })));
/* 252:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.facets.OOXMLSignatureFacet
 * JD-Core Version:    0.7.0.1
 */