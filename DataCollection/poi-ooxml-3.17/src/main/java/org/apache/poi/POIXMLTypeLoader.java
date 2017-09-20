/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.Reader;
/*   8:    */ import java.io.StringReader;
/*   9:    */ import java.net.URL;
/*  10:    */ import java.util.Collections;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.Map;
/*  13:    */ import javax.xml.stream.XMLStreamReader;
/*  14:    */ import org.apache.poi.util.DocumentHelper;
/*  15:    */ import org.apache.poi.util.Removal;
/*  16:    */ import org.apache.xmlbeans.SchemaType;
/*  17:    */ import org.apache.xmlbeans.SchemaTypeLoader;
/*  18:    */ import org.apache.xmlbeans.XmlBeans;
/*  19:    */ import org.apache.xmlbeans.XmlException;
/*  20:    */ import org.apache.xmlbeans.XmlObject;
/*  21:    */ import org.apache.xmlbeans.XmlOptions;
/*  22:    */ import org.apache.xmlbeans.xml.stream.XMLInputStream;
/*  23:    */ import org.apache.xmlbeans.xml.stream.XMLStreamException;
/*  24:    */ import org.w3c.dom.Document;
/*  25:    */ import org.w3c.dom.Node;
/*  26:    */ import org.xml.sax.InputSource;
/*  27:    */ import org.xml.sax.SAXException;
/*  28:    */ 
/*  29:    */ public class POIXMLTypeLoader
/*  30:    */ {
/*  31: 53 */   private static ThreadLocal<SchemaTypeLoader> typeLoader = new ThreadLocal();
/*  32:    */   private static final String MS_OFFICE_URN = "urn:schemas-microsoft-com:office:office";
/*  33:    */   private static final String MS_EXCEL_URN = "urn:schemas-microsoft-com:office:excel";
/*  34:    */   private static final String MS_WORD_URN = "urn:schemas-microsoft-com:office:word";
/*  35:    */   private static final String MS_VML_URN = "urn:schemas-microsoft-com:vml";
/*  36: 64 */   public static final XmlOptions DEFAULT_XML_OPTIONS = new XmlOptions();
/*  37:    */   
/*  38:    */   static
/*  39:    */   {
/*  40: 65 */     DEFAULT_XML_OPTIONS.setSaveOuter();
/*  41: 66 */     DEFAULT_XML_OPTIONS.setUseDefaultNamespace();
/*  42: 67 */     DEFAULT_XML_OPTIONS.setSaveAggressiveNamespaces();
/*  43: 68 */     DEFAULT_XML_OPTIONS.setCharacterEncoding("UTF-8");
/*  44:    */     
/*  45:    */ 
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54: 79 */     Map<String, String> map = new HashMap();
/*  55: 80 */     map.put("http://schemas.openxmlformats.org/drawingml/2006/main", "a");
/*  56: 81 */     map.put("http://schemas.openxmlformats.org/drawingml/2006/chart", "c");
/*  57: 82 */     map.put("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wp");
/*  58: 83 */     map.put("http://schemas.openxmlformats.org/markup-compatibility/2006", "ve");
/*  59: 84 */     map.put("http://schemas.openxmlformats.org/officeDocument/2006/math", "m");
/*  60: 85 */     map.put("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "r");
/*  61: 86 */     map.put("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "vt");
/*  62: 87 */     map.put("http://schemas.openxmlformats.org/presentationml/2006/main", "p");
/*  63: 88 */     map.put("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "w");
/*  64: 89 */     map.put("http://schemas.microsoft.com/office/word/2006/wordml", "wne");
/*  65: 90 */     map.put("urn:schemas-microsoft-com:office:office", "o");
/*  66: 91 */     map.put("urn:schemas-microsoft-com:office:excel", "x");
/*  67: 92 */     map.put("urn:schemas-microsoft-com:office:word", "w10");
/*  68: 93 */     map.put("urn:schemas-microsoft-com:vml", "v");
/*  69: 94 */     DEFAULT_XML_OPTIONS.setSaveSuggestedPrefixes(Collections.unmodifiableMap(map));
/*  70:    */   }
/*  71:    */   
/*  72:    */   private static XmlOptions getXmlOptions(XmlOptions options)
/*  73:    */   {
/*  74: 98 */     return options == null ? DEFAULT_XML_OPTIONS : options;
/*  75:    */   }
/*  76:    */   
/*  77:    */   private static SchemaTypeLoader getTypeLoader(SchemaType type)
/*  78:    */   {
/*  79:122 */     SchemaTypeLoader tl = (SchemaTypeLoader)typeLoader.get();
/*  80:123 */     if (tl == null)
/*  81:    */     {
/*  82:124 */       ClassLoader cl = type.getClass().getClassLoader();
/*  83:125 */       tl = XmlBeans.typeLoaderForClassLoader(cl);
/*  84:126 */       typeLoader.set(tl);
/*  85:    */     }
/*  86:128 */     return tl;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static XmlObject newInstance(SchemaType type, XmlOptions options)
/*  90:    */   {
/*  91:132 */     return getTypeLoader(type).newInstance(type, getXmlOptions(options));
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static XmlObject parse(String xmlText, SchemaType type, XmlOptions options)
/*  95:    */     throws XmlException
/*  96:    */   {
/*  97:    */     try
/*  98:    */     {
/*  99:137 */       return parse(new StringReader(xmlText), type, options);
/* 100:    */     }
/* 101:    */     catch (IOException e)
/* 102:    */     {
/* 103:139 */       throw new XmlException("Unable to parse xml bean", e);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static XmlObject parse(File file, SchemaType type, XmlOptions options)
/* 108:    */     throws XmlException, IOException
/* 109:    */   {
/* 110:144 */     InputStream is = new FileInputStream(file);
/* 111:    */     try
/* 112:    */     {
/* 113:146 */       return parse(is, type, options);
/* 114:    */     }
/* 115:    */     finally
/* 116:    */     {
/* 117:148 */       is.close();
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static XmlObject parse(URL file, SchemaType type, XmlOptions options)
/* 122:    */     throws XmlException, IOException
/* 123:    */   {
/* 124:153 */     InputStream is = file.openStream();
/* 125:    */     try
/* 126:    */     {
/* 127:155 */       return parse(is, type, options);
/* 128:    */     }
/* 129:    */     finally
/* 130:    */     {
/* 131:157 */       is.close();
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static XmlObject parse(InputStream jiois, SchemaType type, XmlOptions options)
/* 136:    */     throws XmlException, IOException
/* 137:    */   {
/* 138:    */     try
/* 139:    */     {
/* 140:163 */       Document doc = DocumentHelper.readDocument(jiois);
/* 141:164 */       return getTypeLoader(type).parse(doc.getDocumentElement(), type, getXmlOptions(options));
/* 142:    */     }
/* 143:    */     catch (SAXException e)
/* 144:    */     {
/* 145:166 */       throw new IOException("Unable to parse xml bean", e);
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static XmlObject parse(XMLStreamReader xsr, SchemaType type, XmlOptions options)
/* 150:    */     throws XmlException
/* 151:    */   {
/* 152:171 */     return getTypeLoader(type).parse(xsr, type, getXmlOptions(options));
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static XmlObject parse(Reader jior, SchemaType type, XmlOptions options)
/* 156:    */     throws XmlException, IOException
/* 157:    */   {
/* 158:    */     try
/* 159:    */     {
/* 160:176 */       Document doc = DocumentHelper.readDocument(new InputSource(jior));
/* 161:177 */       return getTypeLoader(type).parse(doc.getDocumentElement(), type, getXmlOptions(options));
/* 162:    */     }
/* 163:    */     catch (SAXException e)
/* 164:    */     {
/* 165:179 */       throw new XmlException("Unable to parse xml bean", e);
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static XmlObject parse(Node node, SchemaType type, XmlOptions options)
/* 170:    */     throws XmlException
/* 171:    */   {
/* 172:184 */     return getTypeLoader(type).parse(node, type, getXmlOptions(options));
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static XmlObject parse(XMLInputStream xis, SchemaType type, XmlOptions options)
/* 176:    */     throws XmlException, XMLStreamException
/* 177:    */   {
/* 178:188 */     return getTypeLoader(type).parse(xis, type, getXmlOptions(options));
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, SchemaType type, XmlOptions options)
/* 182:    */     throws XmlException, XMLStreamException
/* 183:    */   {
/* 184:192 */     return getTypeLoader(type).newValidatingXMLInputStream(xis, type, getXmlOptions(options));
/* 185:    */   }
/* 186:    */   
/* 187:    */   @Deprecated
/* 188:    */   @Removal(version="4.0")
/* 189:    */   public static void setClassLoader(ClassLoader cl) {}
/* 190:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLTypeLoader
 * JD-Core Version:    0.7.0.1
 */