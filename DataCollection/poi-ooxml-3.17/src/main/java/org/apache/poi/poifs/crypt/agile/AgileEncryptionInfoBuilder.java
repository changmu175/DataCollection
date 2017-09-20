/*   1:    */ package org.apache.poi.poifs.crypt.agile;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument;
/*   4:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument.Factory;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import org.apache.poi.EncryptedDocumentException;
/*   8:    */ import org.apache.poi.POIXMLTypeLoader;
/*   9:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  10:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  11:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  12:    */ import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
/*  13:    */ import org.apache.poi.poifs.crypt.EncryptionMode;
/*  14:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  15:    */ import org.apache.poi.util.LittleEndianInput;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ 
/*  18:    */ public class AgileEncryptionInfoBuilder
/*  19:    */   implements EncryptionInfoBuilder
/*  20:    */ {
/*  21:    */   public void initialize(EncryptionInfo info, LittleEndianInput dis)
/*  22:    */     throws IOException
/*  23:    */   {
/*  24: 40 */     EncryptionDocument ed = parseDescriptor((InputStream)dis);
/*  25: 41 */     info.setHeader(new AgileEncryptionHeader(ed));
/*  26: 42 */     info.setVerifier(new AgileEncryptionVerifier(ed));
/*  27: 43 */     if ((info.getVersionMajor() == EncryptionMode.agile.versionMajor) && (info.getVersionMinor() == EncryptionMode.agile.versionMinor))
/*  28:    */     {
/*  29: 45 */       AgileDecryptor dec = new AgileDecryptor();
/*  30: 46 */       dec.setEncryptionInfo(info);
/*  31: 47 */       info.setDecryptor(dec);
/*  32: 48 */       AgileEncryptor enc = new AgileEncryptor();
/*  33: 49 */       enc.setEncryptionInfo(info);
/*  34: 50 */       info.setEncryptor(enc);
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/*  39:    */   {
/*  40: 56 */     if (cipherAlgorithm == null) {
/*  41: 57 */       cipherAlgorithm = CipherAlgorithm.aes128;
/*  42:    */     }
/*  43: 59 */     if (cipherAlgorithm == CipherAlgorithm.rc4) {
/*  44: 60 */       throw new EncryptedDocumentException("RC4 must not be used with agile encryption.");
/*  45:    */     }
/*  46: 62 */     if (hashAlgorithm == null) {
/*  47: 63 */       hashAlgorithm = HashAlgorithm.sha1;
/*  48:    */     }
/*  49: 65 */     if (chainingMode == null) {
/*  50: 66 */       chainingMode = ChainingMode.cbc;
/*  51:    */     }
/*  52: 68 */     if ((chainingMode != ChainingMode.cbc) && (chainingMode != ChainingMode.cfb)) {
/*  53: 69 */       throw new EncryptedDocumentException("Agile encryption only supports CBC/CFB chaining.");
/*  54:    */     }
/*  55: 71 */     if (keyBits == -1) {
/*  56: 72 */       keyBits = cipherAlgorithm.defaultKeySize;
/*  57:    */     }
/*  58: 74 */     if (blockSize == -1) {
/*  59: 75 */       blockSize = cipherAlgorithm.blockSize;
/*  60:    */     }
/*  61: 77 */     boolean found = false;
/*  62: 78 */     for (int ks : cipherAlgorithm.allowedKeySize) {
/*  63: 79 */       found |= ks == keyBits;
/*  64:    */     }
/*  65: 81 */     if (!found) {
/*  66: 82 */       throw new EncryptedDocumentException("KeySize " + keyBits + " not allowed for Cipher " + cipherAlgorithm);
/*  67:    */     }
/*  68: 84 */     info.setHeader(new AgileEncryptionHeader(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/*  69: 85 */     info.setVerifier(new AgileEncryptionVerifier(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode));
/*  70: 86 */     AgileDecryptor dec = new AgileDecryptor();
/*  71: 87 */     dec.setEncryptionInfo(info);
/*  72: 88 */     info.setDecryptor(dec);
/*  73: 89 */     AgileEncryptor enc = new AgileEncryptor();
/*  74: 90 */     enc.setEncryptionInfo(info);
/*  75: 91 */     info.setEncryptor(enc);
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected static EncryptionDocument parseDescriptor(String descriptor)
/*  79:    */   {
/*  80:    */     try
/*  81:    */     {
/*  82: 96 */       return EncryptionDocument.Factory.parse(descriptor, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  83:    */     }
/*  84:    */     catch (XmlException e)
/*  85:    */     {
/*  86: 98 */       throw new EncryptedDocumentException("Unable to parse encryption descriptor", e);
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected static EncryptionDocument parseDescriptor(InputStream descriptor)
/*  91:    */   {
/*  92:    */     try
/*  93:    */     {
/*  94:104 */       return EncryptionDocument.Factory.parse(descriptor, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  95:    */     }
/*  96:    */     catch (Exception e)
/*  97:    */     {
/*  98:106 */       throw new EncryptedDocumentException("Unable to parse encryption descriptor", e);
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.agile.AgileEncryptionInfoBuilder
 * JD-Core Version:    0.7.0.1
 */