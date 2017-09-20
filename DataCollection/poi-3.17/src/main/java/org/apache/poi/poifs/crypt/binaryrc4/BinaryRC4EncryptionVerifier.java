/*  1:   */ package org.apache.poi.poifs.crypt.binaryrc4;
/*  2:   */ 
/*  3:   */ import org.apache.poi.EncryptedDocumentException;
/*  4:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  5:   */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  6:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  7:   */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  8:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  9:   */ import org.apache.poi.util.LittleEndianInput;
/* 10:   */ 
/* 11:   */ public class BinaryRC4EncryptionVerifier
/* 12:   */   extends EncryptionVerifier
/* 13:   */   implements EncryptionRecord, Cloneable
/* 14:   */ {
/* 15:   */   protected BinaryRC4EncryptionVerifier()
/* 16:   */   {
/* 17:29 */     setSpinCount(-1);
/* 18:30 */     setCipherAlgorithm(CipherAlgorithm.rc4);
/* 19:31 */     setChainingMode(null);
/* 20:32 */     setEncryptedKey(null);
/* 21:33 */     setHashAlgorithm(HashAlgorithm.md5);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected BinaryRC4EncryptionVerifier(LittleEndianInput is)
/* 25:   */   {
/* 26:37 */     byte[] salt = new byte[16];
/* 27:38 */     is.readFully(salt);
/* 28:39 */     setSalt(salt);
/* 29:40 */     byte[] encryptedVerifier = new byte[16];
/* 30:41 */     is.readFully(encryptedVerifier);
/* 31:42 */     setEncryptedVerifier(encryptedVerifier);
/* 32:43 */     byte[] encryptedVerifierHash = new byte[16];
/* 33:44 */     is.readFully(encryptedVerifierHash);
/* 34:45 */     setEncryptedVerifierHash(encryptedVerifierHash);
/* 35:46 */     setSpinCount(-1);
/* 36:47 */     setCipherAlgorithm(CipherAlgorithm.rc4);
/* 37:48 */     setChainingMode(null);
/* 38:49 */     setEncryptedKey(null);
/* 39:50 */     setHashAlgorithm(HashAlgorithm.md5);
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected void setSalt(byte[] salt)
/* 43:   */   {
/* 44:55 */     if ((salt == null) || (salt.length != 16)) {
/* 45:56 */       throw new EncryptedDocumentException("invalid verifier salt");
/* 46:   */     }
/* 47:59 */     super.setSalt(salt);
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected void setEncryptedVerifier(byte[] encryptedVerifier)
/* 51:   */   {
/* 52:64 */     super.setEncryptedVerifier(encryptedVerifier);
/* 53:   */   }
/* 54:   */   
/* 55:   */   protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash)
/* 56:   */   {
/* 57:69 */     super.setEncryptedVerifierHash(encryptedVerifierHash);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public void write(LittleEndianByteArrayOutputStream bos)
/* 61:   */   {
/* 62:74 */     byte[] salt = getSalt();
/* 63:75 */     assert (salt.length == 16);
/* 64:76 */     bos.write(salt);
/* 65:77 */     byte[] encryptedVerifier = getEncryptedVerifier();
/* 66:78 */     assert (encryptedVerifier.length == 16);
/* 67:79 */     bos.write(encryptedVerifier);
/* 68:80 */     byte[] encryptedVerifierHash = getEncryptedVerifierHash();
/* 69:81 */     assert (encryptedVerifierHash.length == 16);
/* 70:82 */     bos.write(encryptedVerifierHash);
/* 71:   */   }
/* 72:   */   
/* 73:   */   public BinaryRC4EncryptionVerifier clone()
/* 74:   */     throws CloneNotSupportedException
/* 75:   */   {
/* 76:87 */     return (BinaryRC4EncryptionVerifier)super.clone();
/* 77:   */   }
/* 78:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */