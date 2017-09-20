/*  1:   */ package org.apache.poi.poifs.crypt.binaryrc4;
/*  2:   */ 
/*  3:   */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  4:   */ import org.apache.poi.poifs.crypt.CipherProvider;
/*  5:   */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  6:   */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  7:   */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  8:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  9:   */ 
/* 10:   */ public class BinaryRC4EncryptionHeader
/* 11:   */   extends EncryptionHeader
/* 12:   */   implements EncryptionRecord, Cloneable
/* 13:   */ {
/* 14:   */   protected BinaryRC4EncryptionHeader()
/* 15:   */   {
/* 16:30 */     setCipherAlgorithm(CipherAlgorithm.rc4);
/* 17:31 */     setKeySize(40);
/* 18:32 */     setBlockSize(-1);
/* 19:33 */     setCipherProvider(CipherProvider.rc4);
/* 20:34 */     setHashAlgorithm(HashAlgorithm.md5);
/* 21:35 */     setSizeExtra(0);
/* 22:36 */     setFlags(0);
/* 23:37 */     setCspName("");
/* 24:38 */     setChainingMode(null);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void write(LittleEndianByteArrayOutputStream littleendianbytearrayoutputstream) {}
/* 28:   */   
/* 29:   */   public BinaryRC4EncryptionHeader clone()
/* 30:   */     throws CloneNotSupportedException
/* 31:   */   {
/* 32:47 */     return (BinaryRC4EncryptionHeader)super.clone();
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4EncryptionHeader
 * JD-Core Version:    0.7.0.1
 */