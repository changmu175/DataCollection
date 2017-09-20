/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import javax.xml.stream.XMLEventFactory;
/*  4:   */ import javax.xml.stream.XMLInputFactory;
/*  5:   */ import javax.xml.stream.XMLOutputFactory;
/*  6:   */ 
/*  7:   */ public final class StaxHelper
/*  8:   */ {
/*  9:29 */   private static final POILogger logger = POILogFactory.getLogger(StaxHelper.class);
/* 10:   */   
/* 11:   */   public static XMLInputFactory newXMLInputFactory()
/* 12:   */   {
/* 13:37 */     XMLInputFactory factory = XMLInputFactory.newFactory();
/* 14:38 */     trySetProperty(factory, "javax.xml.stream.isNamespaceAware", true);
/* 15:39 */     trySetProperty(factory, "javax.xml.stream.isValidating", false);
/* 16:40 */     trySetProperty(factory, "javax.xml.stream.supportDTD", false);
/* 17:41 */     trySetProperty(factory, "javax.xml.stream.isSupportingExternalEntities", false);
/* 18:42 */     return factory;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static XMLOutputFactory newXMLOutputFactory()
/* 22:   */   {
/* 23:49 */     XMLOutputFactory factory = XMLOutputFactory.newFactory();
/* 24:50 */     trySetProperty(factory, "javax.xml.stream.isRepairingNamespaces", true);
/* 25:51 */     return factory;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static XMLEventFactory newXMLEventFactory()
/* 29:   */   {
/* 30:58 */     return XMLEventFactory.newFactory();
/* 31:   */   }
/* 32:   */   
/* 33:   */   private static void trySetProperty(XMLInputFactory factory, String feature, boolean flag)
/* 34:   */   {
/* 35:   */     try
/* 36:   */     {
/* 37:63 */       factory.setProperty(feature, Boolean.valueOf(flag));
/* 38:   */     }
/* 39:   */     catch (Exception e)
/* 40:   */     {
/* 41:65 */       logger.log(5, new Object[] { "StAX Property unsupported", feature, e });
/* 42:   */     }
/* 43:   */     catch (AbstractMethodError ame)
/* 44:   */     {
/* 45:67 */       logger.log(5, new Object[] { "Cannot set StAX property because outdated StAX parser in classpath", feature, ame });
/* 46:   */     }
/* 47:   */   }
/* 48:   */   
/* 49:   */   private static void trySetProperty(XMLOutputFactory factory, String feature, boolean flag)
/* 50:   */   {
/* 51:   */     try
/* 52:   */     {
/* 53:73 */       factory.setProperty(feature, Boolean.valueOf(flag));
/* 54:   */     }
/* 55:   */     catch (Exception e)
/* 56:   */     {
/* 57:75 */       logger.log(5, new Object[] { "StAX Property unsupported", feature, e });
/* 58:   */     }
/* 59:   */     catch (AbstractMethodError ame)
/* 60:   */     {
/* 61:77 */       logger.log(5, new Object[] { "Cannot set StAX property because outdated StAX parser in classpath", feature, ame });
/* 62:   */     }
/* 63:   */   }
/* 64:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.StaxHelper
 * JD-Core Version:    0.7.0.1
 */