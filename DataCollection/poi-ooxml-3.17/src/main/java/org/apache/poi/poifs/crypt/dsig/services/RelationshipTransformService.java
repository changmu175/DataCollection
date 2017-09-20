/*   1:    */ package org.apache.poi.poifs.crypt.dsig.services;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.security.InvalidAlgorithmParameterException;
/*   6:    */ import java.security.Provider;
/*   7:    */ import java.security.Security;
/*   8:    */ import java.security.spec.AlgorithmParameterSpec;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.TreeMap;
/*  12:    */ import javax.xml.crypto.Data;
/*  13:    */ import javax.xml.crypto.MarshalException;
/*  14:    */ import javax.xml.crypto.OctetStreamData;
/*  15:    */ import javax.xml.crypto.XMLCryptoContext;
/*  16:    */ import javax.xml.crypto.XMLStructure;
/*  17:    */ import javax.xml.crypto.dom.DOMStructure;
/*  18:    */ import javax.xml.crypto.dsig.TransformException;
/*  19:    */ import javax.xml.crypto.dsig.TransformService;
/*  20:    */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*  21:    */ import org.apache.jcp.xml.dsig.internal.dom.ApacheNodeSetData;
/*  22:    */ import org.apache.poi.POIXMLTypeLoader;
/*  23:    */ import org.apache.poi.util.DocumentHelper;
/*  24:    */ import org.apache.poi.util.POILogFactory;
/*  25:    */ import org.apache.poi.util.POILogger;
/*  26:    */ import org.apache.xml.security.signature.XMLSignatureInput;
/*  27:    */ import org.apache.xmlbeans.SchemaType;
/*  28:    */ import org.apache.xmlbeans.XmlException;
/*  29:    */ import org.apache.xmlbeans.XmlObject;
/*  30:    */ import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference;
/*  31:    */ import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipReferenceDocument;
/*  32:    */ import org.w3.x2000.x09.xmldsig.TransformDocument;
/*  33:    */ import org.w3.x2000.x09.xmldsig.TransformDocument.Factory;
/*  34:    */ import org.w3.x2000.x09.xmldsig.TransformType;
/*  35:    */ import org.w3c.dom.Document;
/*  36:    */ import org.w3c.dom.Element;
/*  37:    */ import org.w3c.dom.Node;
/*  38:    */ import org.w3c.dom.NodeList;
/*  39:    */ 
/*  40:    */ public class RelationshipTransformService
/*  41:    */   extends TransformService
/*  42:    */ {
/*  43:    */   public static final String TRANSFORM_URI = "http://schemas.openxmlformats.org/package/2006/RelationshipTransform";
/*  44:    */   private final List<String> sourceIds;
/*  45: 79 */   private static final POILogger LOG = POILogFactory.getLogger(RelationshipTransformService.class);
/*  46:    */   
/*  47:    */   public static class RelationshipTransformParameterSpec
/*  48:    */     implements TransformParameterSpec
/*  49:    */   {
/*  50: 85 */     List<String> sourceIds = new ArrayList();
/*  51:    */     
/*  52:    */     public void addRelationshipReference(String relationshipId)
/*  53:    */     {
/*  54: 87 */       this.sourceIds.add(relationshipId);
/*  55:    */     }
/*  56:    */     
/*  57:    */     public boolean hasSourceIds()
/*  58:    */     {
/*  59: 90 */       return !this.sourceIds.isEmpty();
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public RelationshipTransformService()
/*  64:    */   {
/*  65: 97 */     LOG.log(1, new Object[] { "constructor" });
/*  66: 98 */     this.sourceIds = new ArrayList();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static synchronized void registerDsigProvider()
/*  70:    */   {
/*  71:109 */     String dsigProvider = "POIXmlDsigProvider";
/*  72:110 */     if (Security.getProperty("POIXmlDsigProvider") == null)
/*  73:    */     {
/*  74:111 */       Provider p = new Provider("POIXmlDsigProvider", 1.0D, "POIXmlDsigProvider")
/*  75:    */       {
/*  76:    */         static final long serialVersionUID = 1L;
/*  77:113 */       };
/*  78:114 */       p.put("TransformService.http://schemas.openxmlformats.org/package/2006/RelationshipTransform", RelationshipTransformService.class.getName());
/*  79:115 */       p.put("TransformService.http://schemas.openxmlformats.org/package/2006/RelationshipTransform MechanismType", "DOM");
/*  80:116 */       Security.addProvider(p);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void init(TransformParameterSpec params)
/*  85:    */     throws InvalidAlgorithmParameterException
/*  86:    */   {
/*  87:123 */     LOG.log(1, new Object[] { "init(params)" });
/*  88:124 */     if (!(params instanceof RelationshipTransformParameterSpec)) {
/*  89:125 */       throw new InvalidAlgorithmParameterException();
/*  90:    */     }
/*  91:127 */     RelationshipTransformParameterSpec relParams = (RelationshipTransformParameterSpec)params;
/*  92:128 */     for (String sourceId : relParams.sourceIds) {
/*  93:129 */       this.sourceIds.add(sourceId);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void init(XMLStructure parent, XMLCryptoContext context)
/*  98:    */     throws InvalidAlgorithmParameterException
/*  99:    */   {
/* 100:135 */     LOG.log(1, new Object[] { "init(parent,context)" });
/* 101:136 */     LOG.log(1, new Object[] { "parent java type: " + parent.getClass().getName() });
/* 102:137 */     DOMStructure domParent = (DOMStructure)parent;
/* 103:138 */     Node parentNode = domParent.getNode();
/* 104:    */     try
/* 105:    */     {
/* 106:141 */       TransformDocument transDoc = TransformDocument.Factory.parse(parentNode, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 107:142 */       XmlObject[] xoList = transDoc.getTransform().selectChildren(RelationshipReferenceDocument.type.getDocumentElementName());
/* 108:143 */       if (xoList.length == 0) {
/* 109:144 */         LOG.log(5, new Object[] { "no RelationshipReference/@SourceId parameters present" });
/* 110:    */       }
/* 111:146 */       for (XmlObject xo : xoList)
/* 112:    */       {
/* 113:147 */         String sourceId = ((CTRelationshipReference)xo).getSourceId();
/* 114:148 */         LOG.log(1, new Object[] { "sourceId: ", sourceId });
/* 115:149 */         this.sourceIds.add(sourceId);
/* 116:    */       }
/* 117:    */     }
/* 118:    */     catch (XmlException e)
/* 119:    */     {
/* 120:152 */       throw new InvalidAlgorithmParameterException(e);
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void marshalParams(XMLStructure parent, XMLCryptoContext context)
/* 125:    */     throws MarshalException
/* 126:    */   {
/* 127:158 */     LOG.log(1, new Object[] { "marshallParams(parent,context)" });
/* 128:159 */     DOMStructure domParent = (DOMStructure)parent;
/* 129:160 */     Element parentNode = (Element)domParent.getNode();
/* 130:161 */     Document doc = parentNode.getOwnerDocument();
/* 131:163 */     for (String sourceId : this.sourceIds)
/* 132:    */     {
/* 133:164 */       Element el = doc.createElementNS("http://schemas.openxmlformats.org/package/2006/digital-signature", "mdssi:RelationshipReference");
/* 134:165 */       el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:mdssi", "http://schemas.openxmlformats.org/package/2006/digital-signature");
/* 135:166 */       el.setAttribute("SourceId", sourceId);
/* 136:167 */       parentNode.appendChild(el);
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public AlgorithmParameterSpec getParameterSpec()
/* 141:    */   {
/* 142:172 */     LOG.log(1, new Object[] { "getParameterSpec" });
/* 143:173 */     return null;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Data transform(Data data, XMLCryptoContext context)
/* 147:    */     throws TransformException
/* 148:    */   {
/* 149:184 */     LOG.log(1, new Object[] { "transform(data,context)" });
/* 150:185 */     LOG.log(1, new Object[] { "data java type: " + data.getClass().getName() });
/* 151:186 */     OctetStreamData octetStreamData = (OctetStreamData)data;
/* 152:187 */     LOG.log(1, new Object[] { "URI: " + octetStreamData.getURI() });
/* 153:188 */     InputStream octetStream = octetStreamData.getOctetStream();
/* 154:    */     Document doc;
/* 155:    */     try
/* 156:    */     {
/* 157:192 */       doc = DocumentHelper.readDocument(octetStream);
/* 158:    */     }
/* 159:    */     catch (Exception e)
/* 160:    */     {
/* 161:194 */       throw new TransformException(e.getMessage(), e);
/* 162:    */     }
/* 163:198 */     Element root = doc.getDocumentElement();
/* 164:199 */     NodeList nl = root.getChildNodes();
/* 165:200 */     TreeMap<String, Element> rsList = new TreeMap();
/* 166:201 */     for (int i = nl.getLength() - 1; i >= 0; i--)
/* 167:    */     {
/* 168:202 */       Node n = nl.item(i);
/* 169:203 */       if ("Relationship".equals(n.getLocalName()))
/* 170:    */       {
/* 171:204 */         Element el = (Element)n;
/* 172:205 */         String id = el.getAttribute("Id");
/* 173:206 */         if (this.sourceIds.contains(id))
/* 174:    */         {
/* 175:207 */           String targetMode = el.getAttribute("TargetMode");
/* 176:208 */           if ("".equals(targetMode)) {
/* 177:209 */             el.setAttribute("TargetMode", "Internal");
/* 178:    */           }
/* 179:211 */           rsList.put(id, el);
/* 180:    */         }
/* 181:    */       }
/* 182:214 */       root.removeChild(n);
/* 183:    */     }
/* 184:217 */     for (Element el : rsList.values()) {
/* 185:218 */       root.appendChild(el);
/* 186:    */     }
/* 187:221 */     LOG.log(1, new Object[] { "# Relationship elements: ", Integer.valueOf(rsList.size()) });
/* 188:    */     
/* 189:223 */     return new ApacheNodeSetData(new XMLSignatureInput(root));
/* 190:    */   }
/* 191:    */   
/* 192:    */   public Data transform(Data data, XMLCryptoContext context, OutputStream os)
/* 193:    */     throws TransformException
/* 194:    */   {
/* 195:227 */     LOG.log(1, new Object[] { "transform(data,context,os)" });
/* 196:228 */     return null;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public boolean isFeatureSupported(String feature)
/* 200:    */   {
/* 201:232 */     LOG.log(1, new Object[] { "isFeatureSupported(feature)" });
/* 202:233 */     return false;
/* 203:    */   }
/* 204:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.dsig.services.RelationshipTransformService
 * JD-Core Version:    0.7.0.1
 */