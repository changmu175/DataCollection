/*  1:   */ package org.apache.poi.poifs.crypt.binaryrc4;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  5:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  6:   */ import org.apache.poi.poifs.crypt.Decryptor;
/*  7:   */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  8:   */ import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
/*  9:   */ import org.apache.poi.poifs.crypt.Encryptor;
/* 10:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/* 11:   */ import org.apache.poi.util.LittleEndianInput;
/* 12:   */ 
/* 13:   */ public class BinaryRC4EncryptionInfoBuilder
/* 14:   */   implements EncryptionInfoBuilder
/* 15:   */ {
/* 16:   */   public void initialize(EncryptionInfo info, LittleEndianInput dis)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:32 */     int vMajor = info.getVersionMajor();
/* 20:33 */     int vMinor = info.getVersionMinor();
/* 21:34 */     assert ((vMajor == 1) && (vMinor == 1));
/* 22:   */     
/* 23:36 */     info.setHeader(new BinaryRC4EncryptionHeader());
/* 24:37 */     info.setVerifier(new BinaryRC4EncryptionVerifier(dis));
/* 25:38 */     Decryptor dec = new BinaryRC4Decryptor();
/* 26:39 */     dec.setEncryptionInfo(info);
/* 27:40 */     info.setDecryptor(dec);
/* 28:41 */     Encryptor enc = new BinaryRC4Encryptor();
/* 29:42 */     enc.setEncryptionInfo(info);
/* 30:43 */     info.setEncryptor(enc);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 34:   */   {
/* 35:50 */     info.setHeader(new BinaryRC4EncryptionHeader());
/* 36:51 */     info.setVerifier(new BinaryRC4EncryptionVerifier());
/* 37:52 */     Decryptor dec = new BinaryRC4Decryptor();
/* 38:53 */     dec.setEncryptionInfo(info);
/* 39:54 */     info.setDecryptor(dec);
/* 40:55 */     Encryptor enc = new BinaryRC4Encryptor();
/* 41:56 */     enc.setEncryptionInfo(info);
/* 42:57 */     info.setEncryptor(enc);
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */