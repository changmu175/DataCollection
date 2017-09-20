/*  1:   */ package org.apache.poi.openxml4j.util;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.util.Enumeration;
/*  6:   */ import java.util.zip.ZipEntry;
/*  7:   */ import java.util.zip.ZipFile;
/*  8:   */ 
/*  9:   */ public class ZipFileZipEntrySource
/* 10:   */   implements ZipEntrySource
/* 11:   */ {
/* 12:   */   private ZipFile zipArchive;
/* 13:   */   
/* 14:   */   public ZipFileZipEntrySource(ZipFile zipFile)
/* 15:   */   {
/* 16:33 */     this.zipArchive = zipFile;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void close()
/* 20:   */     throws IOException
/* 21:   */   {
/* 22:37 */     if (this.zipArchive != null) {
/* 23:38 */       this.zipArchive.close();
/* 24:   */     }
/* 25:40 */     this.zipArchive = null;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean isClosed()
/* 29:   */   {
/* 30:43 */     return this.zipArchive == null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Enumeration<? extends ZipEntry> getEntries()
/* 34:   */   {
/* 35:47 */     if (this.zipArchive == null) {
/* 36:48 */       throw new IllegalStateException("Zip File is closed");
/* 37:   */     }
/* 38:50 */     return this.zipArchive.entries();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public InputStream getInputStream(ZipEntry entry)
/* 42:   */     throws IOException
/* 43:   */   {
/* 44:54 */     if (this.zipArchive == null) {
/* 45:55 */       throw new IllegalStateException("Zip File is closed");
/* 46:   */     }
/* 47:57 */     return this.zipArchive.getInputStream(entry);
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.util.ZipFileZipEntrySource
 * JD-Core Version:    0.7.0.1
 */