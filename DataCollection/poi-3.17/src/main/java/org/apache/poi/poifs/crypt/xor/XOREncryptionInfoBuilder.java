/*  1:   */ package org.apache.poi.poifs.crypt.xor;
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
/* 13:   */ public class XOREncryptionInfoBuilder
/* 14:   */   implements EncryptionInfoBuilder
/* 15:   */ {
/* 16:   */   public void initialize(EncryptionInfo info, LittleEndianInput dis)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:39 */     info.setHeader(new XOREncryptionHeader());
/* 20:40 */     info.setVerifier(new XOREncryptionVerifier(dis));
/* 21:41 */     Decryptor dec = new XORDecryptor();
/* 22:42 */     dec.setEncryptionInfo(info);
/* 23:43 */     info.setDecryptor(dec);
/* 24:44 */     Encryptor enc = new XOREncryptor();
/* 25:45 */     enc.setEncryptionInfo(info);
/* 26:46 */     info.setEncryptor(enc);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 30:   */   {
/* 31:53 */     info.setHeader(new XOREncryptionHeader());
/* 32:54 */     info.setVerifier(new XOREncryptionVerifier());
/* 33:55 */     Decryptor dec = new XORDecryptor();
/* 34:56 */     dec.setEncryptionInfo(info);
/* 35:57 */     info.setDecryptor(dec);
/* 36:58 */     Encryptor enc = new XOREncryptor();
/* 37:59 */     enc.setEncryptionInfo(info);
/* 38:60 */     info.setEncryptor(enc);
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.xor.XOREncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */