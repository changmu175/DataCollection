/*   1:    */ package org.apache.poi.poifs.crypt.standard;
/*   2:    */ 
/*   3:    */ import org.apache.poi.EncryptedDocumentException;
/*   4:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*   5:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*   6:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*   7:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*   8:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   9:    */ import org.apache.poi.util.LittleEndianInput;
/*  10:    */ 
/*  11:    */ public class StandardEncryptionVerifier
/*  12:    */   extends EncryptionVerifier
/*  13:    */   implements EncryptionRecord, Cloneable
/*  14:    */ {
/*  15:    */   private static final int SPIN_COUNT = 50000;
/*  16:    */   private final int verifierHashSize;
/*  17:    */   
/*  18:    */   protected StandardEncryptionVerifier(LittleEndianInput is, StandardEncryptionHeader header)
/*  19:    */   {
/*  20: 35 */     int saltSize = is.readInt();
/*  21: 37 */     if (saltSize != 16) {
/*  22: 38 */       throw new RuntimeException("Salt size != 16 !?");
/*  23:    */     }
/*  24: 41 */     byte[] salt = new byte[16];
/*  25: 42 */     is.readFully(salt);
/*  26: 43 */     setSalt(salt);
/*  27:    */     
/*  28: 45 */     byte[] encryptedVerifier = new byte[16];
/*  29: 46 */     is.readFully(encryptedVerifier);
/*  30: 47 */     setEncryptedVerifier(encryptedVerifier);
/*  31:    */     
/*  32: 49 */     this.verifierHashSize = is.readInt();
/*  33:    */     
/*  34: 51 */     byte[] encryptedVerifierHash = new byte[header.getCipherAlgorithm().encryptedVerifierHashLength];
/*  35: 52 */     is.readFully(encryptedVerifierHash);
/*  36: 53 */     setEncryptedVerifierHash(encryptedVerifierHash);
/*  37:    */     
/*  38: 55 */     setSpinCount(50000);
/*  39: 56 */     setCipherAlgorithm(header.getCipherAlgorithm());
/*  40: 57 */     setChainingMode(header.getChainingMode());
/*  41: 58 */     setEncryptedKey(null);
/*  42: 59 */     setHashAlgorithm(header.getHashAlgorithm());
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected StandardEncryptionVerifier(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/*  46:    */   {
/*  47: 63 */     setCipherAlgorithm(cipherAlgorithm);
/*  48: 64 */     setHashAlgorithm(hashAlgorithm);
/*  49: 65 */     setChainingMode(chainingMode);
/*  50: 66 */     setSpinCount(50000);
/*  51: 67 */     this.verifierHashSize = hashAlgorithm.hashSize;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected void setSalt(byte[] salt)
/*  55:    */   {
/*  56: 73 */     if ((salt == null) || (salt.length != 16)) {
/*  57: 74 */       throw new EncryptedDocumentException("invalid verifier salt");
/*  58:    */     }
/*  59: 76 */     super.setSalt(salt);
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected void setEncryptedVerifier(byte[] encryptedVerifier)
/*  63:    */   {
/*  64: 82 */     super.setEncryptedVerifier(encryptedVerifier);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash)
/*  68:    */   {
/*  69: 88 */     super.setEncryptedVerifierHash(encryptedVerifierHash);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void write(LittleEndianByteArrayOutputStream bos)
/*  73:    */   {
/*  74: 94 */     byte[] salt = getSalt();
/*  75: 95 */     assert (salt.length == 16);
/*  76: 96 */     bos.writeInt(salt.length);
/*  77: 97 */     bos.write(salt);
/*  78:    */     
/*  79:    */ 
/*  80:100 */     byte[] encryptedVerifier = getEncryptedVerifier();
/*  81:101 */     assert (encryptedVerifier.length == 16);
/*  82:102 */     bos.write(encryptedVerifier);
/*  83:    */     
/*  84:    */ 
/*  85:    */ 
/*  86:106 */     bos.writeInt(20);
/*  87:    */     
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:114 */     byte[] encryptedVerifierHash = getEncryptedVerifierHash();
/*  95:115 */     assert (encryptedVerifierHash.length == getCipherAlgorithm().encryptedVerifierHashLength);
/*  96:116 */     bos.write(encryptedVerifierHash);
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected int getVerifierHashSize()
/* 100:    */   {
/* 101:120 */     return this.verifierHashSize;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public StandardEncryptionVerifier clone()
/* 105:    */     throws CloneNotSupportedException
/* 106:    */   {
/* 107:125 */     return (StandardEncryptionVerifier)super.clone();
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.StandardEncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */