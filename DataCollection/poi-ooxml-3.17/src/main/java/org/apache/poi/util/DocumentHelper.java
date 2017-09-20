/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import javax.xml.parsers.DocumentBuilder;
/*   7:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*   8:    */ import javax.xml.parsers.ParserConfigurationException;
/*   9:    */ import javax.xml.stream.events.Namespace;
/*  10:    */ import org.w3c.dom.Document;
/*  11:    */ import org.w3c.dom.Element;
/*  12:    */ import org.xml.sax.ErrorHandler;
/*  13:    */ import org.xml.sax.InputSource;
/*  14:    */ import org.xml.sax.SAXException;
/*  15:    */ import org.xml.sax.SAXParseException;
/*  16:    */ 
/*  17:    */ public final class DocumentHelper
/*  18:    */ {
/*  19: 38 */   private static POILogger logger = POILogFactory.getLogger(DocumentHelper.class);
/*  20:    */   
/*  21:    */   private static class DocHelperErrorHandler
/*  22:    */     implements ErrorHandler
/*  23:    */   {
/*  24:    */     public void warning(SAXParseException exception)
/*  25:    */       throws SAXException
/*  26:    */     {
/*  27: 45 */       printError(5, exception);
/*  28:    */     }
/*  29:    */     
/*  30:    */     public void error(SAXParseException exception)
/*  31:    */       throws SAXException
/*  32:    */     {
/*  33: 49 */       printError(7, exception);
/*  34:    */     }
/*  35:    */     
/*  36:    */     public void fatalError(SAXParseException exception)
/*  37:    */       throws SAXException
/*  38:    */     {
/*  39: 53 */       printError(9, exception);
/*  40: 54 */       throw exception;
/*  41:    */     }
/*  42:    */     
/*  43:    */     private void printError(int type, SAXParseException ex)
/*  44:    */     {
/*  45: 59 */       StringBuilder sb = new StringBuilder();
/*  46:    */       
/*  47: 61 */       String systemId = ex.getSystemId();
/*  48: 62 */       if (systemId != null)
/*  49:    */       {
/*  50: 63 */         int index = systemId.lastIndexOf('/');
/*  51: 64 */         if (index != -1) {
/*  52: 65 */           systemId = systemId.substring(index + 1);
/*  53:    */         }
/*  54: 66 */         sb.append(systemId);
/*  55:    */       }
/*  56: 68 */       sb.append(':');
/*  57: 69 */       sb.append(ex.getLineNumber());
/*  58: 70 */       sb.append(':');
/*  59: 71 */       sb.append(ex.getColumnNumber());
/*  60: 72 */       sb.append(": ");
/*  61: 73 */       sb.append(ex.getMessage());
/*  62:    */       
/*  63: 75 */       DocumentHelper.logger.log(type, new Object[] { sb.toString(), ex });
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static synchronized DocumentBuilder newDocumentBuilder()
/*  68:    */   {
/*  69:    */     try
/*  70:    */     {
/*  71: 87 */       DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  72: 88 */       documentBuilder.setEntityResolver(SAXHelper.IGNORING_ENTITY_RESOLVER);
/*  73: 89 */       documentBuilder.setErrorHandler(new DocHelperErrorHandler(null));
/*  74: 90 */       return documentBuilder;
/*  75:    */     }
/*  76:    */     catch (ParserConfigurationException e)
/*  77:    */     {
/*  78: 92 */       throw new IllegalStateException("cannot create a DocumentBuilder", e);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82: 96 */   private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  83:    */   
/*  84:    */   static
/*  85:    */   {
/*  86: 98 */     documentBuilderFactory.setNamespaceAware(true);
/*  87: 99 */     documentBuilderFactory.setValidating(false);
/*  88:100 */     trySetSAXFeature(documentBuilderFactory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
/*  89:101 */     trySetXercesSecurityManager(documentBuilderFactory);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private static void trySetSAXFeature(DocumentBuilderFactory dbf, String feature, boolean enabled)
/*  93:    */   {
/*  94:    */     try
/*  95:    */     {
/*  96:106 */       dbf.setFeature(feature, enabled);
/*  97:    */     }
/*  98:    */     catch (Exception e)
/*  99:    */     {
/* 100:108 */       logger.log(5, new Object[] { "SAX Feature unsupported", feature, e });
/* 101:    */     }
/* 102:    */     catch (AbstractMethodError ame)
/* 103:    */     {
/* 104:110 */       logger.log(5, new Object[] { "Cannot set SAX feature because outdated XML parser in classpath", feature, ame });
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static void trySetXercesSecurityManager(DocumentBuilderFactory dbf)
/* 109:    */   {
/* 110:116 */     for (String securityManagerClassName : new String[] { "com.sun.org.apache.xerces.internal.util.SecurityManager", "org.apache.xerces.util.SecurityManager" }) {
/* 111:    */       try
/* 112:    */       {
/* 113:121 */         Object mgr = Class.forName(securityManagerClassName).newInstance();
/* 114:122 */         Method setLimit = mgr.getClass().getMethod("setEntityExpansionLimit", new Class[] { Integer.TYPE });
/* 115:123 */         setLimit.invoke(mgr, new Object[] { Integer.valueOf(4096) });
/* 116:124 */         dbf.setAttribute("http://apache.org/xml/properties/security-manager", mgr);
/* 117:    */         
/* 118:126 */         return;
/* 119:    */       }
/* 120:    */       catch (Throwable e)
/* 121:    */       {
/* 122:128 */         logger.log(5, new Object[] { "SAX Security Manager could not be setup", e });
/* 123:    */       }
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static Document readDocument(InputStream inp)
/* 128:    */     throws IOException, SAXException
/* 129:    */   {
/* 130:140 */     return newDocumentBuilder().parse(inp);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public static Document readDocument(InputSource inp)
/* 134:    */     throws IOException, SAXException
/* 135:    */   {
/* 136:150 */     return newDocumentBuilder().parse(inp);
/* 137:    */   }
/* 138:    */   
/* 139:154 */   private static final DocumentBuilder documentBuilderSingleton = newDocumentBuilder();
/* 140:    */   
/* 141:    */   public static synchronized Document createDocument()
/* 142:    */   {
/* 143:160 */     return documentBuilderSingleton.newDocument();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static void addNamespaceDeclaration(Element element, String namespacePrefix, String namespaceURI)
/* 147:    */   {
/* 148:167 */     element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + namespacePrefix, namespaceURI);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static void addNamespaceDeclaration(Element element, Namespace namespace)
/* 152:    */   {
/* 153:176 */     addNamespaceDeclaration(element, namespace.getPrefix(), namespace.getNamespaceURI());
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.util.DocumentHelper
 * JD-Core Version:    0.7.0.1
 */