/*  1:   */ package org.apache.poi.poifs.crypt.cryptoapi;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.EncryptedDocumentException;
/*  5:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  6:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  7:   */ import org.apache.poi.poifs.crypt.CipherProvider;
/*  8:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  9:   */ import org.apache.poi.poifs.crypt.standard.StandardEncryptionHeader;
/* 10:   */ import org.apache.poi.util.LittleEndianInput;
/* 11:   */ 
/* 12:   */ public class CryptoAPIEncryptionHeader
/* 13:   */   extends StandardEncryptionHeader
/* 14:   */   implements Cloneable
/* 15:   */ {
/* 16:   */   public CryptoAPIEncryptionHeader(LittleEndianInput is)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:33 */     super(is);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected CryptoAPIEncryptionHeader(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 23:   */   {
/* 24:39 */     super(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setKeySize(int keyBits)
/* 28:   */   {
/* 29:46 */     boolean found = false;
/* 30:47 */     for (int size : getCipherAlgorithm().allowedKeySize) {
/* 31:48 */       if (size == keyBits)
/* 32:   */       {
/* 33:49 */         found = true;
/* 34:50 */         break;
/* 35:   */       }
/* 36:   */     }
/* 37:53 */     if (!found) {
/* 38:54 */       throw new EncryptedDocumentException("invalid keysize " + keyBits + " for cipher algorithm " + getCipherAlgorithm());
/* 39:   */     }
/* 40:56 */     super.setKeySize(keyBits);
/* 41:57 */     if (keyBits > 40) {
/* 42:58 */       setCspName("Microsoft Enhanced Cryptographic Provider v1.0");
/* 43:   */     } else {
/* 44:60 */       setCspName(CipherProvider.rc4.cipherProviderName);
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   public CryptoAPIEncryptionHeader clone()
/* 49:   */     throws CloneNotSupportedException
/* 50:   */   {
/* 51:66 */     return (CryptoAPIEncryptionHeader)super.clone();
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionHeader
 * JD-Core Version:    0.7.0.1
 */