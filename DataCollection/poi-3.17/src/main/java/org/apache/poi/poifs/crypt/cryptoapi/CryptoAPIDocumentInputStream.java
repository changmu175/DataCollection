/*  1:   */ package org.apache.poi.poifs.crypt.cryptoapi;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.security.GeneralSecurityException;
/*  5:   */ import javax.crypto.Cipher;
/*  6:   */ import javax.crypto.ShortBufferException;
/*  7:   */ import org.apache.poi.EncryptedDocumentException;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ 
/* 10:   */ @Internal
/* 11:   */ class CryptoAPIDocumentInputStream
/* 12:   */   extends ByteArrayInputStream
/* 13:   */ {
/* 14:   */   private Cipher cipher;
/* 15:   */   private final CryptoAPIDecryptor decryptor;
/* 16:36 */   private byte[] oneByte = { 0 };
/* 17:   */   
/* 18:   */   public void seek(int newpos)
/* 19:   */   {
/* 20:39 */     if (newpos > this.count) {
/* 21:40 */       throw new ArrayIndexOutOfBoundsException(newpos);
/* 22:   */     }
/* 23:43 */     this.pos = newpos;
/* 24:44 */     this.mark = newpos;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setBlock(int block)
/* 28:   */     throws GeneralSecurityException
/* 29:   */   {
/* 30:48 */     this.cipher = this.decryptor.initCipherForBlock(this.cipher, block);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public synchronized int read()
/* 34:   */   {
/* 35:53 */     int ch = super.read();
/* 36:54 */     if (ch == -1) {
/* 37:55 */       return -1;
/* 38:   */     }
/* 39:57 */     this.oneByte[0] = ((byte)ch);
/* 40:   */     try
/* 41:   */     {
/* 42:59 */       this.cipher.update(this.oneByte, 0, 1, this.oneByte);
/* 43:   */     }
/* 44:   */     catch (ShortBufferException e)
/* 45:   */     {
/* 46:61 */       throw new EncryptedDocumentException(e);
/* 47:   */     }
/* 48:63 */     return this.oneByte[0];
/* 49:   */   }
/* 50:   */   
/* 51:   */   public synchronized int read(byte[] b, int off, int len)
/* 52:   */   {
/* 53:68 */     int readLen = super.read(b, off, len);
/* 54:69 */     if (readLen == -1) {
/* 55:70 */       return -1;
/* 56:   */     }
/* 57:   */     try
/* 58:   */     {
/* 59:73 */       this.cipher.update(b, off, readLen, b, off);
/* 60:   */     }
/* 61:   */     catch (ShortBufferException e)
/* 62:   */     {
/* 63:75 */       throw new EncryptedDocumentException(e);
/* 64:   */     }
/* 65:77 */     return readLen;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public CryptoAPIDocumentInputStream(CryptoAPIDecryptor decryptor, byte[] buf)
/* 69:   */     throws GeneralSecurityException
/* 70:   */   {
/* 71:82 */     super(buf);
/* 72:83 */     this.decryptor = decryptor;
/* 73:84 */     this.cipher = decryptor.initCipherForBlock(null, 0);
/* 74:   */   }
/* 75:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIDocumentInputStream
 * JD-Core Version:    0.7.0.1
 */