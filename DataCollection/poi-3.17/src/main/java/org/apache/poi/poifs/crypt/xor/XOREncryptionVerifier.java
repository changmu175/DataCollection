/*  1:   */ package org.apache.poi.poifs.crypt.xor;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  4:   */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  5:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  6:   */ import org.apache.poi.util.LittleEndianInput;
/*  7:   */ 
/*  8:   */ public class XOREncryptionVerifier
/*  9:   */   extends EncryptionVerifier
/* 10:   */   implements EncryptionRecord, Cloneable
/* 11:   */ {
/* 12:   */   protected XOREncryptionVerifier()
/* 13:   */   {
/* 14:28 */     setEncryptedKey(new byte[2]);
/* 15:29 */     setEncryptedVerifier(new byte[2]);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected XOREncryptionVerifier(LittleEndianInput is)
/* 19:   */   {
/* 20:38 */     byte[] key = new byte[2];
/* 21:39 */     is.readFully(key);
/* 22:40 */     setEncryptedKey(key);
/* 23:   */     
/* 24:   */ 
/* 25:   */ 
/* 26:   */ 
/* 27:   */ 
/* 28:46 */     byte[] verifier = new byte[2];
/* 29:47 */     is.readFully(verifier);
/* 30:48 */     setEncryptedVerifier(verifier);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void write(LittleEndianByteArrayOutputStream bos)
/* 34:   */   {
/* 35:53 */     bos.write(getEncryptedKey());
/* 36:54 */     bos.write(getEncryptedVerifier());
/* 37:   */   }
/* 38:   */   
/* 39:   */   public XOREncryptionVerifier clone()
/* 40:   */     throws CloneNotSupportedException
/* 41:   */   {
/* 42:59 */     return (XOREncryptionVerifier)super.clone();
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected final void setEncryptedVerifier(byte[] encryptedVerifier)
/* 46:   */   {
/* 47:64 */     super.setEncryptedVerifier(encryptedVerifier);
/* 48:   */   }
/* 49:   */   
/* 50:   */   protected final void setEncryptedKey(byte[] encryptedKey)
/* 51:   */   {
/* 52:69 */     super.setEncryptedKey(encryptedKey);
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.xor.XOREncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */