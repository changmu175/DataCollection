/*  1:   */ package org.apache.poi.poifs.crypt.cryptoapi;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.security.GeneralSecurityException;
/*  5:   */ import javax.crypto.Cipher;
/*  6:   */ import org.apache.poi.EncryptedDocumentException;
/*  7:   */ import org.apache.poi.util.Internal;
/*  8:   */ 
/*  9:   */ @Internal
/* 10:   */ class CryptoAPIDocumentOutputStream
/* 11:   */   extends ByteArrayOutputStream
/* 12:   */ {
/* 13:   */   private final Cipher cipher;
/* 14:   */   private final CryptoAPIEncryptor encryptor;
/* 15:34 */   private final byte[] oneByte = { 0 };
/* 16:   */   
/* 17:   */   public CryptoAPIDocumentOutputStream(CryptoAPIEncryptor encryptor)
/* 18:   */     throws GeneralSecurityException
/* 19:   */   {
/* 20:37 */     this.encryptor = encryptor;
/* 21:38 */     this.cipher = encryptor.initCipherForBlock(null, 0);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public byte[] getBuf()
/* 25:   */   {
/* 26:42 */     return this.buf;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setSize(int count)
/* 30:   */   {
/* 31:46 */     this.count = count;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setBlock(int block)
/* 35:   */     throws GeneralSecurityException
/* 36:   */   {
/* 37:50 */     this.encryptor.initCipherForBlock(this.cipher, block);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void write(int b)
/* 41:   */   {
/* 42:   */     try
/* 43:   */     {
/* 44:56 */       this.oneByte[0] = ((byte)b);
/* 45:57 */       this.cipher.update(this.oneByte, 0, 1, this.oneByte, 0);
/* 46:58 */       super.write(this.oneByte);
/* 47:   */     }
/* 48:   */     catch (Exception e)
/* 49:   */     {
/* 50:60 */       throw new EncryptedDocumentException(e);
/* 51:   */     }
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void write(byte[] b, int off, int len)
/* 55:   */   {
/* 56:   */     try
/* 57:   */     {
/* 58:67 */       this.cipher.update(b, off, len, b, off);
/* 59:68 */       super.write(b, off, len);
/* 60:   */     }
/* 61:   */     catch (Exception e)
/* 62:   */     {
/* 63:70 */       throw new EncryptedDocumentException(e);
/* 64:   */     }
/* 65:   */   }
/* 66:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIDocumentOutputStream
 * JD-Core Version:    0.7.0.1
 */