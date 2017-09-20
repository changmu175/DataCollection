/*  1:   */ package org.apache.poi.hssf.record.crypto;
/*  2:   */ 
/*  3:   */ public final class Biff8EncryptionKey
/*  4:   */ {
/*  5:27 */   private static final ThreadLocal<String> _userPasswordTLS = new ThreadLocal();
/*  6:   */   
/*  7:   */   public static void setCurrentUserPassword(String password)
/*  8:   */   {
/*  9:35 */     if (password == null) {
/* 10:36 */       _userPasswordTLS.remove();
/* 11:   */     } else {
/* 12:38 */       _userPasswordTLS.set(password);
/* 13:   */     }
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static String getCurrentUserPassword()
/* 17:   */   {
/* 18:47 */     return (String)_userPasswordTLS.get();
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.crypto.Biff8EncryptionKey
 * JD-Core Version:    0.7.0.1
 */