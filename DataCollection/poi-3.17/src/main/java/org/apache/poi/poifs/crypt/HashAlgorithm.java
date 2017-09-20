/*  1:   */ package org.apache.poi.poifs.crypt;
/*  2:   */ 
/*  3:   */ import org.apache.poi.EncryptedDocumentException;
/*  4:   */ 
/*  5:   */ public enum HashAlgorithm
/*  6:   */ {
/*  7:23 */   none("", 0, "", 0, "", false),  sha1("SHA-1", 32772, "SHA1", 20, "HmacSHA1", false),  sha256("SHA-256", 32780, "SHA256", 32, "HmacSHA256", false),  sha384("SHA-384", 32781, "SHA384", 48, "HmacSHA384", false),  sha512("SHA-512", 32782, "SHA512", 64, "HmacSHA512", false),  md5("MD5", -1, "MD5", 16, "HmacMD5", false),  md2("MD2", -1, "MD2", 16, "Hmac-MD2", true),  md4("MD4", -1, "MD4", 16, "Hmac-MD4", true),  ripemd128("RipeMD128", -1, "RIPEMD-128", 16, "HMac-RipeMD128", true),  ripemd160("RipeMD160", -1, "RIPEMD-160", 20, "HMac-RipeMD160", true),  whirlpool("Whirlpool", -1, "WHIRLPOOL", 64, "HMac-Whirlpool", true),  sha224("SHA-224", -1, "SHA224", 28, "HmacSHA224", true);
/*  8:   */   
/*  9:   */   public final String jceId;
/* 10:   */   public final int ecmaId;
/* 11:   */   public final String ecmaString;
/* 12:   */   public final int hashSize;
/* 13:   */   public final String jceHmacId;
/* 14:   */   public final boolean needsBouncyCastle;
/* 15:   */   
/* 16:   */   private HashAlgorithm(String jceId, int ecmaId, String ecmaString, int hashSize, String jceHmacId, boolean needsBouncyCastle)
/* 17:   */   {
/* 18:47 */     this.jceId = jceId;
/* 19:48 */     this.ecmaId = ecmaId;
/* 20:49 */     this.ecmaString = ecmaString;
/* 21:50 */     this.hashSize = hashSize;
/* 22:51 */     this.jceHmacId = jceHmacId;
/* 23:52 */     this.needsBouncyCastle = needsBouncyCastle;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static HashAlgorithm fromEcmaId(int ecmaId)
/* 27:   */   {
/* 28:56 */     for (HashAlgorithm ha : ) {
/* 29:57 */       if (ha.ecmaId == ecmaId) {
/* 30:57 */         return ha;
/* 31:   */       }
/* 32:   */     }
/* 33:59 */     throw new EncryptedDocumentException("hash algorithm not found");
/* 34:   */   }
/* 35:   */   
/* 36:   */   public static HashAlgorithm fromEcmaId(String ecmaString)
/* 37:   */   {
/* 38:63 */     for (HashAlgorithm ha : ) {
/* 39:64 */       if (ha.ecmaString.equals(ecmaString)) {
/* 40:64 */         return ha;
/* 41:   */       }
/* 42:   */     }
/* 43:66 */     throw new EncryptedDocumentException("hash algorithm not found");
/* 44:   */   }
/* 45:   */   
/* 46:   */   public static HashAlgorithm fromString(String string)
/* 47:   */   {
/* 48:70 */     for (HashAlgorithm ha : ) {
/* 49:71 */       if ((ha.ecmaString.equalsIgnoreCase(string)) || (ha.jceId.equalsIgnoreCase(string))) {
/* 50:71 */         return ha;
/* 51:   */       }
/* 52:   */     }
/* 53:73 */     throw new EncryptedDocumentException("hash algorithm not found");
/* 54:   */   }
/* 55:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.HashAlgorithm
 * JD-Core Version:    0.7.0.1
 */