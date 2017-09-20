/*  1:   */ package org.apache.poi.poifs.crypt.temp;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.io.InputStream;
/*  8:   */ import java.io.OutputStream;
/*  9:   */ import java.security.SecureRandom;
/* 10:   */ import javax.crypto.Cipher;
/* 11:   */ import javax.crypto.CipherInputStream;
/* 12:   */ import javax.crypto.CipherOutputStream;
/* 13:   */ import javax.crypto.spec.SecretKeySpec;
/* 14:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/* 15:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/* 16:   */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/* 17:   */ import org.apache.poi.util.POILogFactory;
/* 18:   */ import org.apache.poi.util.POILogger;
/* 19:   */ import org.apache.poi.util.TempFile;
/* 20:   */ 
/* 21:   */ public class EncryptedTempData
/* 22:   */ {
/* 23:48 */   private static POILogger LOG = POILogFactory.getLogger(EncryptedTempData.class);
/* 24:50 */   private static final CipherAlgorithm cipherAlgorithm = CipherAlgorithm.aes128;
/* 25:   */   private final SecretKeySpec skeySpec;
/* 26:   */   private final byte[] ivBytes;
/* 27:   */   private final File tempFile;
/* 28:   */   
/* 29:   */   public EncryptedTempData()
/* 30:   */     throws IOException
/* 31:   */   {
/* 32:56 */     SecureRandom sr = new SecureRandom();
/* 33:57 */     this.ivBytes = new byte[16];
/* 34:58 */     byte[] keyBytes = new byte[16];
/* 35:59 */     sr.nextBytes(this.ivBytes);
/* 36:60 */     sr.nextBytes(keyBytes);
/* 37:61 */     this.skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
/* 38:62 */     this.tempFile = TempFile.createTempFile("poi-temp-data", ".tmp");
/* 39:   */   }
/* 40:   */   
/* 41:   */   public OutputStream getOutputStream()
/* 42:   */     throws IOException
/* 43:   */   {
/* 44:66 */     Cipher ciEnc = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 1, null);
/* 45:67 */     return new CipherOutputStream(new FileOutputStream(this.tempFile), ciEnc);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public InputStream getInputStream()
/* 49:   */     throws IOException
/* 50:   */   {
/* 51:71 */     Cipher ciDec = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 2, null);
/* 52:72 */     return new CipherInputStream(new FileInputStream(this.tempFile), ciDec);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void dispose()
/* 56:   */   {
/* 57:76 */     if (!this.tempFile.delete()) {
/* 58:77 */       LOG.log(5, new Object[] { this.tempFile.getAbsolutePath() + " can't be removed (or was already removed." });
/* 59:   */     }
/* 60:   */   }
/* 61:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.temp.EncryptedTempData
 * JD-Core Version:    0.7.0.1
 */