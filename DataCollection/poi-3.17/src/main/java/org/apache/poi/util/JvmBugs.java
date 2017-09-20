/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ 
/*  5:   */ public class JvmBugs
/*  6:   */ {
/*  7:22 */   private static final POILogger LOG = POILogFactory.getLogger(JvmBugs.class);
/*  8:   */   
/*  9:   */   public static boolean hasLineBreakMeasurerBug()
/* 10:   */   {
/* 11:47 */     String version = System.getProperty("java.version");
/* 12:48 */     String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/* 13:49 */     boolean ignore = Boolean.getBoolean("org.apache.poi.JvmBugs.LineBreakMeasurer.ignore");
/* 14:50 */     boolean hasBug = (!ignore) && (os.contains("win")) && (("1.6.0_45".equals(version)) || ("1.7.0_21".equals(version)));
/* 15:51 */     if (hasBug) {
/* 16:52 */       LOG.log(5, new Object[] { "JVM has LineBreakMeasurer bug - see POI bug #54904 - caller code might default to Lucida Sans" });
/* 17:   */     }
/* 18:54 */     return hasBug;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.JvmBugs
 * JD-Core Version:    0.7.0.1
 */