/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import javax.xml.parsers.DocumentBuilderFactory;
/*  4:   */ 
/*  5:   */ public final class XMLHelper
/*  6:   */ {
/*  7:28 */   private static POILogger logger = POILogFactory.getLogger(XMLHelper.class);
/*  8:   */   
/*  9:   */   public static DocumentBuilderFactory getDocumentBuilderFactory()
/* 10:   */   {
/* 11:34 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 12:35 */     factory.setExpandEntityReferences(false);
/* 13:36 */     trySetSAXFeature(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
/* 14:37 */     trySetSAXFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
/* 15:38 */     trySetSAXFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
/* 16:39 */     trySetSAXFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
/* 17:40 */     trySetSAXFeature(factory, "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
/* 18:41 */     return factory;
/* 19:   */   }
/* 20:   */   
/* 21:   */   private static void trySetSAXFeature(DocumentBuilderFactory documentBuilderFactory, String feature, boolean enabled)
/* 22:   */   {
/* 23:   */     try
/* 24:   */     {
/* 25:46 */       documentBuilderFactory.setFeature(feature, enabled);
/* 26:   */     }
/* 27:   */     catch (Exception e)
/* 28:   */     {
/* 29:48 */       logger.log(5, new Object[] { "SAX Feature unsupported", feature, e });
/* 30:   */     }
/* 31:   */     catch (AbstractMethodError ame)
/* 32:   */     {
/* 33:50 */       logger.log(5, new Object[] { "Cannot set SAX feature because outdated XML parser in classpath", feature, ame });
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.XMLHelper
 * JD-Core Version:    0.7.0.1
 */