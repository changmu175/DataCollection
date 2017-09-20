/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ 
/*   6:    */ @Internal
/*   7:    */ public final class POILogFactory
/*   8:    */ {
/*   9: 38 */   private static final Map<String, POILogger> _loggers = new HashMap();
/*  10: 44 */   private static final POILogger _nullLogger = new NullLogger();
/*  11: 49 */   static String _loggerClassName = null;
/*  12:    */   
/*  13:    */   public static POILogger getLogger(Class<?> theclass)
/*  14:    */   {
/*  15: 64 */     return getLogger(theclass.getName());
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static POILogger getLogger(String cat)
/*  19:    */   {
/*  20: 80 */     if (_loggerClassName == null)
/*  21:    */     {
/*  22:    */       try
/*  23:    */       {
/*  24: 82 */         _loggerClassName = System.getProperty("org.apache.poi.util.POILogger");
/*  25:    */       }
/*  26:    */       catch (Exception e) {}
/*  27: 89 */       if (_loggerClassName == null) {
/*  28: 90 */         _loggerClassName = _nullLogger.getClass().getName();
/*  29:    */       }
/*  30:    */     }
/*  31: 96 */     if (_loggerClassName.equals(_nullLogger.getClass().getName())) {
/*  32: 97 */       return _nullLogger;
/*  33:    */     }
/*  34:103 */     POILogger logger = (POILogger)_loggers.get(cat);
/*  35:104 */     if (logger == null)
/*  36:    */     {
/*  37:    */       try
/*  38:    */       {
/*  39:107 */         Class<? extends POILogger> loggerClass = Class.forName(_loggerClassName);
/*  40:    */         
/*  41:109 */         logger = (POILogger)loggerClass.newInstance();
/*  42:110 */         logger.initialize(cat);
/*  43:    */       }
/*  44:    */       catch (Exception e)
/*  45:    */       {
/*  46:113 */         logger = _nullLogger;
/*  47:114 */         _loggerClassName = _nullLogger.getClass().getName();
/*  48:    */       }
/*  49:118 */       _loggers.put(cat, logger);
/*  50:    */     }
/*  51:120 */     return logger;
/*  52:    */   }
/*  53:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.POILogFactory
 * JD-Core Version:    0.7.0.1
 */