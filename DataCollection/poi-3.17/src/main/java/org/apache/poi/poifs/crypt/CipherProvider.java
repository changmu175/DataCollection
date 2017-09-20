/*  1:   */ package org.apache.poi.poifs.crypt;
/*  2:   */ 
/*  3:   */ import org.apache.poi.EncryptedDocumentException;
/*  4:   */ 
/*  5:   */ public enum CipherProvider
/*  6:   */ {
/*  7:23 */   rc4("RC4", 1, "Microsoft Base Cryptographic Provider v1.0"),  aes("AES", 24, "Microsoft Enhanced RSA and AES Cryptographic Provider");
/*  8:   */   
/*  9:   */   public final String jceId;
/* 10:   */   public final int ecmaId;
/* 11:   */   public final String cipherProviderName;
/* 12:   */   
/* 13:   */   public static CipherProvider fromEcmaId(int ecmaId)
/* 14:   */   {
/* 15:27 */     for (CipherProvider cp : ) {
/* 16:28 */       if (cp.ecmaId == ecmaId) {
/* 17:28 */         return cp;
/* 18:   */       }
/* 19:   */     }
/* 20:30 */     throw new EncryptedDocumentException("cipher provider not found");
/* 21:   */   }
/* 22:   */   
/* 23:   */   private CipherProvider(String jceId, int ecmaId, String cipherProviderName)
/* 24:   */   {
/* 25:37 */     this.jceId = jceId;
/* 26:38 */     this.ecmaId = ecmaId;
/* 27:39 */     this.cipherProviderName = cipherProviderName;
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.CipherProvider
 * JD-Core Version:    0.7.0.1
 */