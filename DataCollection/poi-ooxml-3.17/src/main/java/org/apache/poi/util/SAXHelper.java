/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.StringReader;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.concurrent.TimeUnit;
/*   7:    */ import javax.xml.parsers.ParserConfigurationException;
/*   8:    */ import javax.xml.parsers.SAXParser;
/*   9:    */ import javax.xml.parsers.SAXParserFactory;
/*  10:    */ import org.xml.sax.EntityResolver;
/*  11:    */ import org.xml.sax.InputSource;
/*  12:    */ import org.xml.sax.SAXException;
/*  13:    */ import org.xml.sax.XMLReader;
/*  14:    */ 
/*  15:    */ public final class SAXHelper
/*  16:    */ {
/*  17: 39 */   private static final POILogger logger = POILogFactory.getLogger(SAXHelper.class);
/*  18:    */   private static long lastLog;
/*  19:    */   
/*  20:    */   public static synchronized XMLReader newXMLReader()
/*  21:    */     throws SAXException, ParserConfigurationException
/*  22:    */   {
/*  23: 48 */     XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
/*  24: 49 */     xmlReader.setEntityResolver(IGNORING_ENTITY_RESOLVER);
/*  25: 50 */     trySetSAXFeature(xmlReader, "http://javax.xml.XMLConstants/feature/secure-processing");
/*  26: 51 */     trySetXercesSecurityManager(xmlReader);
/*  27: 52 */     return xmlReader;
/*  28:    */   }
/*  29:    */   
/*  30: 55 */   static final EntityResolver IGNORING_ENTITY_RESOLVER = new EntityResolver()
/*  31:    */   {
/*  32:    */     public InputSource resolveEntity(String publicId, String systemId)
/*  33:    */       throws SAXException, IOException
/*  34:    */     {
/*  35: 59 */       return new InputSource(new StringReader(""));
/*  36:    */     }
/*  37:    */   };
/*  38:    */   private static final SAXParserFactory saxFactory;
/*  39:    */   
/*  40:    */   static
/*  41:    */   {
/*  42:    */     try
/*  43:    */     {
/*  44: 66 */       saxFactory = SAXParserFactory.newInstance();
/*  45: 67 */       saxFactory.setValidating(false);
/*  46: 68 */       saxFactory.setNamespaceAware(true);
/*  47:    */     }
/*  48:    */     catch (RuntimeException re)
/*  49:    */     {
/*  50: 70 */       logger.log(5, new Object[] { "Failed to create SAXParserFactory", re });
/*  51: 71 */       throw re;
/*  52:    */     }
/*  53:    */     catch (Exception e)
/*  54:    */     {
/*  55: 73 */       logger.log(5, new Object[] { "Failed to create SAXParserFactory", e });
/*  56: 74 */       throw new RuntimeException("Failed to create SAXParserFactory", e);
/*  57:    */     }
/*  58:    */     catch (Error e)
/*  59:    */     {
/*  60: 80 */       logger.log(5, new Object[] { "Failed to create SAXParserFactory", e });
/*  61: 81 */       throw e;
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   private static void trySetSAXFeature(XMLReader xmlReader, String feature)
/*  66:    */   {
/*  67:    */     try
/*  68:    */     {
/*  69: 87 */       xmlReader.setFeature(feature, true);
/*  70:    */     }
/*  71:    */     catch (Exception e)
/*  72:    */     {
/*  73: 89 */       logger.log(5, new Object[] { "SAX Feature unsupported", feature, e });
/*  74:    */     }
/*  75:    */     catch (AbstractMethodError ame)
/*  76:    */     {
/*  77: 91 */       logger.log(5, new Object[] { "Cannot set SAX feature because outdated XML parser in classpath", feature, ame });
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   private static void trySetXercesSecurityManager(XMLReader xmlReader)
/*  82:    */   {
/*  83: 97 */     for (String securityManagerClassName : new String[] { "com.sun.org.apache.xerces.internal.util.SecurityManager", "org.apache.xerces.util.SecurityManager" }) {
/*  84:    */       try
/*  85:    */       {
/*  86:102 */         Object mgr = Class.forName(securityManagerClassName).newInstance();
/*  87:103 */         Method setLimit = mgr.getClass().getMethod("setEntityExpansionLimit", new Class[] { Integer.TYPE });
/*  88:104 */         setLimit.invoke(mgr, new Object[] { Integer.valueOf(4096) });
/*  89:105 */         xmlReader.setProperty("http://apache.org/xml/properties/security-manager", mgr);
/*  90:    */         
/*  91:107 */         return;
/*  92:    */       }
/*  93:    */       catch (Throwable e)
/*  94:    */       {
/*  95:110 */         if (System.currentTimeMillis() > lastLog + TimeUnit.MINUTES.toMillis(5L))
/*  96:    */         {
/*  97:111 */           logger.log(5, new Object[] { "SAX Security Manager could not be setup [log suppressed for 5 minutes]", e });
/*  98:112 */           lastLog = System.currentTimeMillis();
/*  99:    */         }
/* 100:    */       }
/* 101:    */     }
/* 102:    */   }
/* 103:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.util.SAXHelper
 * JD-Core Version:    0.7.0.1
 */