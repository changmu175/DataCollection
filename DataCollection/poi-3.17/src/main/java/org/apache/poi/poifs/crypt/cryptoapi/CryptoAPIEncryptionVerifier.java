/*  1:   */ package org.apache.poi.poifs.crypt.cryptoapi;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  4:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  5:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  6:   */ import org.apache.poi.poifs.crypt.standard.StandardEncryptionVerifier;
/*  7:   */ import org.apache.poi.util.LittleEndianInput;
/*  8:   */ 
/*  9:   */ public class CryptoAPIEncryptionVerifier
/* 10:   */   extends StandardEncryptionVerifier
/* 11:   */   implements Cloneable
/* 12:   */ {
/* 13:   */   protected CryptoAPIEncryptionVerifier(LittleEndianInput is, CryptoAPIEncryptionHeader header)
/* 14:   */   {
/* 15:30 */     super(is, header);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected CryptoAPIEncryptionVerifier(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 19:   */   {
/* 20:36 */     super(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected void setSalt(byte[] salt)
/* 24:   */   {
/* 25:41 */     super.setSalt(salt);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected void setEncryptedVerifier(byte[] encryptedVerifier)
/* 29:   */   {
/* 30:46 */     super.setEncryptedVerifier(encryptedVerifier);
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash)
/* 34:   */   {
/* 35:51 */     super.setEncryptedVerifierHash(encryptedVerifierHash);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public CryptoAPIEncryptionVerifier clone()
/* 39:   */     throws CloneNotSupportedException
/* 40:   */   {
/* 41:56 */     return (CryptoAPIEncryptionVerifier)super.clone();
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */