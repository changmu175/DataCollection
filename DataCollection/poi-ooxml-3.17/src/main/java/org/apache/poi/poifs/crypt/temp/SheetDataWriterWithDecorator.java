/*  1:   */ package org.apache.poi.poifs.crypt.temp;
/*  2:   */ 
/*  3:   */ import java.io.FileInputStream;
/*  4:   */ import java.io.FileOutputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.InputStream;
/*  7:   */ import java.io.OutputStream;
/*  8:   */ import java.security.SecureRandom;
/*  9:   */ import javax.crypto.Cipher;
/* 10:   */ import javax.crypto.CipherInputStream;
/* 11:   */ import javax.crypto.CipherOutputStream;
/* 12:   */ import javax.crypto.spec.SecretKeySpec;
/* 13:   */ import org.apache.poi.poifs.crypt.ChainingMode;
/* 14:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/* 15:   */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/* 16:   */ import org.apache.poi.xssf.streaming.SheetDataWriter;
/* 17:   */ 
/* 18:   */ public class SheetDataWriterWithDecorator
/* 19:   */   extends SheetDataWriter
/* 20:   */ {
/* 21:42 */   static final CipherAlgorithm cipherAlgorithm = CipherAlgorithm.aes128;
/* 22:   */   SecretKeySpec skeySpec;
/* 23:   */   byte[] ivBytes;
/* 24:   */   
/* 25:   */   public SheetDataWriterWithDecorator()
/* 26:   */     throws IOException
/* 27:   */   {}
/* 28:   */   
/* 29:   */   void init()
/* 30:   */   {
/* 31:51 */     if (this.skeySpec == null)
/* 32:   */     {
/* 33:52 */       SecureRandom sr = new SecureRandom();
/* 34:53 */       this.ivBytes = new byte[16];
/* 35:54 */       byte[] keyBytes = new byte[16];
/* 36:55 */       sr.nextBytes(this.ivBytes);
/* 37:56 */       sr.nextBytes(keyBytes);
/* 38:57 */       this.skeySpec = new SecretKeySpec(keyBytes, cipherAlgorithm.jceId);
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected OutputStream decorateOutputStream(FileOutputStream fos)
/* 43:   */   {
/* 44:63 */     init();
/* 45:64 */     Cipher ciEnc = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 1, "PKCS5Padding");
/* 46:65 */     return new CipherOutputStream(fos, ciEnc);
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected InputStream decorateInputStream(FileInputStream fis)
/* 50:   */   {
/* 51:70 */     Cipher ciDec = CryptoFunctions.getCipher(this.skeySpec, cipherAlgorithm, ChainingMode.cbc, this.ivBytes, 2, "PKCS5Padding");
/* 52:71 */     return new CipherInputStream(fis, ciDec);
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.temp.SheetDataWriterWithDecorator
 * JD-Core Version:    0.7.0.1
 */