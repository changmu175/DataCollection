/*  1:   */ package org.apache.poi.util;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ 
/*  6:   */ public final class TempFile
/*  7:   */ {
/*  8:28 */   private static TempFileCreationStrategy strategy = new DefaultTempFileCreationStrategy();
/*  9:   */   public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
/* 10:   */   
/* 11:   */   public static void setTempFileCreationStrategy(TempFileCreationStrategy strategy)
/* 12:   */   {
/* 13:45 */     if (strategy == null) {
/* 14:46 */       throw new IllegalArgumentException("strategy == null");
/* 15:   */     }
/* 16:48 */     strategy = strategy;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static File createTempFile(String prefix, String suffix)
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:66 */     return strategy.createTempFile(prefix, suffix);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static File createTempDirectory(String name)
/* 26:   */     throws IOException
/* 27:   */   {
/* 28:70 */     return strategy.createTempDirectory(name);
/* 29:   */   }
/* 30:   */   
/* 31:   */   /**
/* 32:   */    * @deprecated
/* 33:   */    */
/* 34:   */   public static class DefaultTempFileCreationStrategy
/* 35:   */     extends DefaultTempFileCreationStrategy
/* 36:   */   {}
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.TempFile
 * JD-Core Version:    0.7.0.1
 */