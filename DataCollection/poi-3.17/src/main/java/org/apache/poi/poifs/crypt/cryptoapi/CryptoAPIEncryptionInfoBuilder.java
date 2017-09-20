/*  1:   */ package org.apache.poi.poifs.crypt.cryptoapi;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  5:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  6:   */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  7:   */ import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
/*  8:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  9:   */ import org.apache.poi.util.LittleEndianInput;
/* 10:   */ 
/* 11:   */ public class CryptoAPIEncryptionInfoBuilder
/* 12:   */   implements EncryptionInfoBuilder
/* 13:   */ {
/* 14:   */   public void initialize(EncryptionInfo info, LittleEndianInput dis)
/* 15:   */     throws IOException
/* 16:   */   {
/* 17:35 */     dis.readInt();
/* 18:36 */     CryptoAPIEncryptionHeader header = new CryptoAPIEncryptionHeader(dis);
/* 19:37 */     info.setHeader(header);
/* 20:38 */     info.setVerifier(new CryptoAPIEncryptionVerifier(dis, header));
/* 21:39 */     CryptoAPIDecryptor dec = new CryptoAPIDecryptor();
/* 22:40 */     dec.setEncryptionInfo(info);
/* 23:41 */     info.setDecryptor(dec);
/* 24:42 */     CryptoAPIEncryptor enc = new CryptoAPIEncryptor();
/* 25:43 */     enc.setEncryptionInfo(info);
/* 26:44 */     info.setEncryptor(enc);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 30:   */   {
/* 31:54 */     if (cipherAlgorithm == null) {
/* 32:55 */       cipherAlgorithm = CipherAlgorithm.rc4;
/* 33:   */     }
/* 34:57 */     if (hashAlgorithm == null) {
/* 35:58 */       hashAlgorithm = HashAlgorithm.sha1;
/* 36:   */     }
/* 37:60 */     if (keyBits == -1) {
/* 38:61 */       keyBits = 40;
/* 39:   */     }
/* 40:63 */     assert ((cipherAlgorithm == CipherAlgorithm.rc4) && (hashAlgorithm == HashAlgorithm.sha1));
/* 41:   */     
/* 42:65 */     info.setHeader(new CryptoAPIEncryptionHeader(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/* 43:66 */     info.setVerifier(new CryptoAPIEncryptionVerifier(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/* 44:67 */     CryptoAPIDecryptor dec = new CryptoAPIDecryptor();
/* 45:68 */     dec.setEncryptionInfo(info);
/* 46:69 */     info.setDecryptor(dec);
/* 47:70 */     CryptoAPIEncryptor enc = new CryptoAPIEncryptor();
/* 48:71 */     enc.setEncryptionInfo(info);
/* 49:72 */     info.setEncryptor(enc);
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */