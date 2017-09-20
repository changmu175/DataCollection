/*  1:   */ package org.apache.poi.openxml4j.opc.internal;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.nio.channels.FileChannel;
/*  8:   */ 
/*  9:   */ public final class FileHelper
/* 10:   */ {
/* 11:   */   public static File getDirectory(File f)
/* 12:   */   {
/* 13:42 */     if (f != null)
/* 14:   */     {
/* 15:43 */       String path = f.getPath();
/* 16:44 */       int len = path.length();
/* 17:45 */       int num2 = len;
/* 18:   */       for (;;)
/* 19:   */       {
/* 20:46 */         num2--;
/* 21:46 */         if (num2 < 0) {
/* 22:   */           break;
/* 23:   */         }
/* 24:47 */         char ch1 = path.charAt(num2);
/* 25:48 */         if (ch1 == File.separatorChar) {
/* 26:49 */           return new File(path.substring(0, num2));
/* 27:   */         }
/* 28:   */       }
/* 29:   */     }
/* 30:53 */     return null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static void copyFile(File in, File out)
/* 34:   */     throws IOException
/* 35:   */   {
/* 36:67 */     FileInputStream fis = new FileInputStream(in);
/* 37:68 */     FileOutputStream fos = new FileOutputStream(out);
/* 38:69 */     FileChannel sourceChannel = fis.getChannel();
/* 39:70 */     FileChannel destinationChannel = fos.getChannel();
/* 40:71 */     sourceChannel.transferTo(0L, sourceChannel.size(), destinationChannel);
/* 41:72 */     sourceChannel.close();
/* 42:73 */     destinationChannel.close();
/* 43:74 */     fos.close();
/* 44:75 */     fis.close();
/* 45:   */   }
/* 46:   */   
/* 47:   */   public static String getFilename(File file)
/* 48:   */   {
/* 49:82 */     if (file != null)
/* 50:   */     {
/* 51:83 */       String path = file.getPath();
/* 52:84 */       int len = path.length();
/* 53:85 */       int num2 = len;
/* 54:   */       for (;;)
/* 55:   */       {
/* 56:86 */         num2--;
/* 57:86 */         if (num2 < 0) {
/* 58:   */           break;
/* 59:   */         }
/* 60:87 */         char ch1 = path.charAt(num2);
/* 61:88 */         if (ch1 == File.separatorChar) {
/* 62:89 */           return path.substring(num2 + 1, len);
/* 63:   */         }
/* 64:   */       }
/* 65:   */     }
/* 66:92 */     return "";
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.FileHelper
 * JD-Core Version:    0.7.0.1
 */