/*  1:   */ package org.apache.poi.poifs.crypt;
/*  2:   */ 
/*  3:   */ import org.apache.poi.EncryptedDocumentException;
/*  4:   */ 
/*  5:   */ public enum CipherAlgorithm
/*  6:   */ {
/*  7:25 */   rc4(CipherProvider.rc4, "RC4", 26625, 64, new int[] { 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 128 }, -1, 20, "RC4", false),  aes128(CipherProvider.aes, "AES", 26126, 128, new int[] { 128 }, 16, 32, "AES", false),  aes192(CipherProvider.aes, "AES", 26127, 192, new int[] { 192 }, 16, 32, "AES", false),  aes256(CipherProvider.aes, "AES", 26128, 256, new int[] { 256 }, 16, 32, "AES", false),  rc2(null, "RC2", -1, 128, new int[] { 40, 48, 56, 64, 72, 80, 88, 96, 104, 112, 120, 128 }, 8, 20, "RC2", false),  des(null, "DES", -1, 64, new int[] { 64 }, 8, 32, "DES", false),  des3(null, "DESede", -1, 192, new int[] { 192 }, 8, 32, "3DES", false),  des3_112(null, "DESede", -1, 128, new int[] { 128 }, 8, 32, "3DES_112", true),  rsa(null, "RSA", -1, 1024, new int[] { 1024, 2048, 3072, 4096 }, -1, -1, "", false);
/*  8:   */   
/*  9:   */   public final CipherProvider provider;
/* 10:   */   public final String jceId;
/* 11:   */   public final int ecmaId;
/* 12:   */   public final int defaultKeySize;
/* 13:   */   public final int[] allowedKeySize;
/* 14:   */   public final int blockSize;
/* 15:   */   public final int encryptedVerifierHashLength;
/* 16:   */   public final String xmlId;
/* 17:   */   public final boolean needsBouncyCastle;
/* 18:   */   
/* 19:   */   private CipherAlgorithm(CipherProvider provider, String jceId, int ecmaId, int defaultKeySize, int[] allowedKeySize, int blockSize, int encryptedVerifierHashLength, String xmlId, boolean needsBouncyCastle)
/* 20:   */   {
/* 21:51 */     this.provider = provider;
/* 22:52 */     this.jceId = jceId;
/* 23:53 */     this.ecmaId = ecmaId;
/* 24:54 */     this.defaultKeySize = defaultKeySize;
/* 25:55 */     this.allowedKeySize = ((int[])allowedKeySize.clone());
/* 26:56 */     this.blockSize = blockSize;
/* 27:57 */     this.encryptedVerifierHashLength = encryptedVerifierHashLength;
/* 28:58 */     this.xmlId = xmlId;
/* 29:59 */     this.needsBouncyCastle = needsBouncyCastle;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static CipherAlgorithm fromEcmaId(int ecmaId)
/* 33:   */   {
/* 34:63 */     for (CipherAlgorithm ca : ) {
/* 35:64 */       if (ca.ecmaId == ecmaId) {
/* 36:64 */         return ca;
/* 37:   */       }
/* 38:   */     }
/* 39:66 */     throw new EncryptedDocumentException("cipher algorithm " + ecmaId + " not found");
/* 40:   */   }
/* 41:   */   
/* 42:   */   public static CipherAlgorithm fromXmlId(String xmlId, int keySize)
/* 43:   */   {
/* 44:70 */     for (CipherAlgorithm ca : ) {
/* 45:71 */       if (ca.xmlId.equals(xmlId)) {
/* 46:72 */         for (int ks : ca.allowedKeySize) {
/* 47:73 */           if (ks == keySize) {
/* 48:73 */             return ca;
/* 49:   */           }
/* 50:   */         }
/* 51:   */       }
/* 52:   */     }
/* 53:76 */     throw new EncryptedDocumentException("cipher algorithm " + xmlId + "/" + keySize + " not found");
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.CipherAlgorithm
 * JD-Core Version:    0.7.0.1
 */