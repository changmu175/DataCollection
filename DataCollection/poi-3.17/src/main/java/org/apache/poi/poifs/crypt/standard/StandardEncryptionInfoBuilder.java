/*  1:   */ package org.apache.poi.poifs.crypt.standard;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.EncryptedDocumentException;
/*  5:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  6:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  7:   */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  8:   */ import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
/*  9:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/* 10:   */ import org.apache.poi.util.LittleEndianInput;
/* 11:   */ 
/* 12:   */ public class StandardEncryptionInfoBuilder
/* 13:   */   implements EncryptionInfoBuilder
/* 14:   */ {
/* 15:   */   public void initialize(EncryptionInfo info, LittleEndianInput dis)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:36 */     dis.readInt();
/* 19:37 */     StandardEncryptionHeader header = new StandardEncryptionHeader(dis);
/* 20:38 */     info.setHeader(header);
/* 21:39 */     info.setVerifier(new StandardEncryptionVerifier(dis, header));
/* 22:41 */     if ((info.getVersionMinor() == 2) && ((info.getVersionMajor() == 3) || (info.getVersionMajor() == 4)))
/* 23:   */     {
/* 24:42 */       StandardDecryptor dec = new StandardDecryptor();
/* 25:43 */       dec.setEncryptionInfo(info);
/* 26:44 */       info.setDecryptor(dec);
/* 27:   */     }
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 31:   */   {
/* 32:53 */     if (cipherAlgorithm == null) {
/* 33:54 */       cipherAlgorithm = CipherAlgorithm.aes128;
/* 34:   */     }
/* 35:56 */     if ((cipherAlgorithm != CipherAlgorithm.aes128) && (cipherAlgorithm != CipherAlgorithm.aes192) && (cipherAlgorithm != CipherAlgorithm.aes256)) {
/* 36:59 */       throw new EncryptedDocumentException("Standard encryption only supports AES128/192/256.");
/* 37:   */     }
/* 38:62 */     if (hashAlgorithm == null) {
/* 39:63 */       hashAlgorithm = HashAlgorithm.sha1;
/* 40:   */     }
/* 41:65 */     if (hashAlgorithm != HashAlgorithm.sha1) {
/* 42:66 */       throw new EncryptedDocumentException("Standard encryption only supports SHA-1.");
/* 43:   */     }
/* 44:68 */     if (chainingMode == null) {
/* 45:69 */       chainingMode = ChainingMode.ecb;
/* 46:   */     }
/* 47:71 */     if (chainingMode != ChainingMode.ecb) {
/* 48:72 */       throw new EncryptedDocumentException("Standard encryption only supports ECB chaining.");
/* 49:   */     }
/* 50:74 */     if (keyBits == -1) {
/* 51:75 */       keyBits = cipherAlgorithm.defaultKeySize;
/* 52:   */     }
/* 53:77 */     if (blockSize == -1) {
/* 54:78 */       blockSize = cipherAlgorithm.blockSize;
/* 55:   */     }
/* 56:80 */     boolean found = false;
/* 57:81 */     for (int ks : cipherAlgorithm.allowedKeySize) {
/* 58:82 */       found |= ks == keyBits;
/* 59:   */     }
/* 60:84 */     if (!found) {
/* 61:85 */       throw new EncryptedDocumentException("KeySize " + keyBits + " not allowed for Cipher " + cipherAlgorithm);
/* 62:   */     }
/* 63:87 */     info.setHeader(new StandardEncryptionHeader(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/* 64:88 */     info.setVerifier(new StandardEncryptionVerifier(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/* 65:89 */     StandardDecryptor dec = new StandardDecryptor();
/* 66:90 */     dec.setEncryptionInfo(info);
/* 67:91 */     info.setDecryptor(dec);
/* 68:92 */     StandardEncryptor enc = new StandardEncryptor();
/* 69:93 */     enc.setEncryptionInfo(info);
/* 70:94 */     info.setEncryptor(enc);
/* 71:   */   }
/* 72:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.StandardEncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */