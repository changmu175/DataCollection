/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ public class Configurator
/*  4:   */ {
/*  5:24 */   private static POILogger logger = POILogFactory.getLogger(Configurator.class);
/*  6:   */   
/*  7:   */   public static int getIntValue(String systemProperty, int defaultValue)
/*  8:   */   {
/*  9:27 */     int result = defaultValue;
/* 10:28 */     String property = System.getProperty(systemProperty);
/* 11:   */     try
/* 12:   */     {
/* 13:30 */       result = Integer.parseInt(property);
/* 14:   */     }
/* 15:   */     catch (Exception e)
/* 16:   */     {
/* 17:32 */       logger.log(7, new Object[] { "System property -D" + systemProperty + " do not contains a valid integer " + property });
/* 18:   */     }
/* 19:34 */     return result;
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.Configurator
 * JD-Core Version:    0.7.0.1
 */