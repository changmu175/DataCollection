/*  1:   */ package org.apache.poi.poifs.filesystem;
/*  2:   */ 
/*  3:   */ import java.io.FilterInputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import java.security.GeneralSecurityException;
/*  7:   */ import org.apache.poi.EncryptedDocumentException;
/*  8:   */ import org.apache.poi.poifs.crypt.Decryptor;
/*  9:   */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/* 10:   */ import org.apache.poi.util.Internal;
/* 11:   */ import org.apache.poi.util.Removal;
/* 12:   */ 
/* 13:   */ @Internal
/* 14:   */ public class DocumentFactoryHelper
/* 15:   */ {
/* 16:   */   public static InputStream getDecryptedStream(final NPOIFSFileSystem fs, String password)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:48 */     EncryptionInfo info = new EncryptionInfo(fs);
/* 20:49 */     Decryptor d = Decryptor.getInstance(info);
/* 21:   */     try
/* 22:   */     {
/* 23:52 */       boolean passwordCorrect = false;
/* 24:53 */       if ((password != null) && (d.verifyPassword(password))) {
/* 25:54 */         passwordCorrect = true;
/* 26:   */       }
/* 27:56 */       if ((!passwordCorrect) && (d.verifyPassword("VelvetSweatshop"))) {
/* 28:57 */         passwordCorrect = true;
/* 29:   */       }
/* 30:60 */       if (passwordCorrect) {
/* 31:63 */         new FilterInputStream(d.getDataStream(fs.getRoot()))
/* 32:   */         {
/* 33:   */           public void close()
/* 34:   */             throws IOException
/* 35:   */           {
/* 36:66 */             fs.close();
/* 37:   */             
/* 38:68 */             super.close();
/* 39:   */           }
/* 40:   */         };
/* 41:   */       }
/* 42:72 */       if (password != null) {
/* 43:73 */         throw new EncryptedDocumentException("Password incorrect");
/* 44:   */       }
/* 45:75 */       throw new EncryptedDocumentException("The supplied spreadsheet is protected, but no password was supplied");
/* 46:   */     }
/* 47:   */     catch (GeneralSecurityException e)
/* 48:   */     {
/* 49:78 */       throw new IOException(e);
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   @Deprecated
/* 54:   */   @Removal(version="4.0")
/* 55:   */   public static boolean hasOOXMLHeader(InputStream inp)
/* 56:   */     throws IOException
/* 57:   */   {
/* 58:97 */     return FileMagic.valueOf(inp) == FileMagic.OOXML;
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.DocumentFactoryHelper
 * JD-Core Version:    0.7.0.1
 */