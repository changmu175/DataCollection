/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.security.SecureRandom;
/*   6:    */ 
/*   7:    */ public class DefaultTempFileCreationStrategy
/*   8:    */   implements TempFileCreationStrategy
/*   9:    */ {
/*  10:    */   private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
/*  11:    */   static final String POIFILES = "poifiles";
/*  12:    */   public static final String KEEP_FILES = "poi.keep.tmp.files";
/*  13: 46 */   private static final SecureRandom random = new SecureRandom();
/*  14:    */   private File dir;
/*  15:    */   
/*  16:    */   public DefaultTempFileCreationStrategy()
/*  17:    */   {
/*  18: 57 */     this(null);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public DefaultTempFileCreationStrategy(File dir)
/*  22:    */   {
/*  23: 68 */     this.dir = dir;
/*  24:    */   }
/*  25:    */   
/*  26:    */   private void createPOIFilesDirectory()
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 74 */     if (this.dir == null)
/*  30:    */     {
/*  31: 75 */       String tmpDir = System.getProperty("java.io.tmpdir");
/*  32: 76 */       if (tmpDir == null) {
/*  33: 77 */         throw new IOException("Systems temporary directory not defined - set the -Djava.io.tmpdir jvm property!");
/*  34:    */       }
/*  35: 79 */       this.dir = new File(tmpDir, "poifiles");
/*  36:    */     }
/*  37: 82 */     createTempDirectory(this.dir);
/*  38:    */   }
/*  39:    */   
/*  40:    */   private void createTempDirectory(File directory)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43: 94 */     boolean dirExists = (directory.exists()) || (directory.mkdirs());
/*  44: 96 */     if (!dirExists) {
/*  45: 97 */       throw new IOException("Could not create temporary directory '" + directory + "'");
/*  46:    */     }
/*  47: 99 */     if (!directory.isDirectory()) {
/*  48:100 */       throw new IOException("Could not create temporary directory. '" + directory + "' exists but is not a directory.");
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public File createTempFile(String prefix, String suffix)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:107 */     createPOIFilesDirectory();
/*  56:    */     
/*  57:    */ 
/*  58:110 */     File newFile = File.createTempFile(prefix, suffix, this.dir);
/*  59:113 */     if (System.getProperty("poi.keep.tmp.files") == null) {
/*  60:114 */       newFile.deleteOnExit();
/*  61:    */     }
/*  62:118 */     return newFile;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public File createTempDirectory(String prefix)
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:125 */     createPOIFilesDirectory();
/*  69:    */     
/*  70:    */ 
/*  71:    */ 
/*  72:129 */     long n = random.nextLong();
/*  73:130 */     File newDirectory = new File(this.dir, prefix + Long.toString(n));
/*  74:131 */     createTempDirectory(newDirectory);
/*  75:134 */     if (System.getProperty("poi.keep.tmp.files") == null) {
/*  76:135 */       newDirectory.deleteOnExit();
/*  77:    */     }
/*  78:139 */     return newDirectory;
/*  79:    */   }
/*  80:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.DefaultTempFileCreationStrategy
 * JD-Core Version:    0.7.0.1
 */