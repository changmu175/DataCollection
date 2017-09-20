/*   1:    */ package org.apache.poi.poifs.crypt.agile;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.x2006.encryption.CTDataIntegrity;
/*   4:    */ import com.microsoft.schemas.office.x2006.encryption.CTEncryption;
/*   5:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyData;
/*   6:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument;
/*   7:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherAlgorithm.Enum;
/*   8:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherChaining.Enum;
/*   9:    */ import com.microsoft.schemas.office.x2006.encryption.STHashAlgorithm.Enum;
/*  10:    */ import org.apache.poi.EncryptedDocumentException;
/*  11:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  12:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  13:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  14:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  15:    */ 
/*  16:    */ public class AgileEncryptionHeader
/*  17:    */   extends EncryptionHeader
/*  18:    */   implements Cloneable
/*  19:    */ {
/*  20:    */   private byte[] encryptedHmacKey;
/*  21:    */   private byte[] encryptedHmacValue;
/*  22:    */   
/*  23:    */   public AgileEncryptionHeader(String descriptor)
/*  24:    */   {
/*  25: 34 */     this(AgileEncryptionInfoBuilder.parseDescriptor(descriptor));
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected AgileEncryptionHeader(EncryptionDocument ed)
/*  29:    */   {
/*  30:    */     CTKeyData keyData;
/*  31:    */     try
/*  32:    */     {
/*  33: 40 */       keyData = ed.getEncryption().getKeyData();
/*  34: 41 */       if (keyData == null) {
/*  35: 42 */         throw new NullPointerException("keyData not set");
/*  36:    */       }
/*  37:    */     }
/*  38:    */     catch (Exception e)
/*  39:    */     {
/*  40: 45 */       throw new EncryptedDocumentException("Unable to parse keyData");
/*  41:    */     }
/*  42: 48 */     int keyBits = (int)keyData.getKeyBits();
/*  43:    */     
/*  44: 50 */     CipherAlgorithm ca = CipherAlgorithm.fromXmlId(keyData.getCipherAlgorithm().toString(), keyBits);
/*  45: 51 */     setCipherAlgorithm(ca);
/*  46: 52 */     setCipherProvider(ca.provider);
/*  47:    */     
/*  48: 54 */     setKeySize(keyBits);
/*  49: 55 */     setFlags(0);
/*  50: 56 */     setSizeExtra(0);
/*  51: 57 */     setCspName(null);
/*  52: 58 */     setBlockSize(keyData.getBlockSize());
/*  53: 60 */     switch (keyData.getCipherChaining().intValue())
/*  54:    */     {
/*  55:    */     case 1: 
/*  56: 62 */       setChainingMode(ChainingMode.cbc);
/*  57: 63 */       break;
/*  58:    */     case 2: 
/*  59: 65 */       setChainingMode(ChainingMode.cfb);
/*  60: 66 */       break;
/*  61:    */     default: 
/*  62: 68 */       throw new EncryptedDocumentException("Unsupported chaining mode - " + keyData.getCipherChaining());
/*  63:    */     }
/*  64: 71 */     int hashSize = keyData.getHashSize();
/*  65:    */     
/*  66: 73 */     HashAlgorithm ha = HashAlgorithm.fromEcmaId(keyData.getHashAlgorithm().toString());
/*  67: 74 */     setHashAlgorithm(ha);
/*  68: 76 */     if (getHashAlgorithm().hashSize != hashSize) {
/*  69: 77 */       throw new EncryptedDocumentException("Unsupported hash algorithm: " + keyData.getHashAlgorithm() + " @ " + hashSize + " bytes");
/*  70:    */     }
/*  71: 81 */     int saltLength = keyData.getSaltSize();
/*  72: 82 */     setKeySalt(keyData.getSaltValue());
/*  73: 83 */     if (getKeySalt().length != saltLength) {
/*  74: 84 */       throw new EncryptedDocumentException("Invalid salt length");
/*  75:    */     }
/*  76: 87 */     CTDataIntegrity di = ed.getEncryption().getDataIntegrity();
/*  77: 88 */     setEncryptedHmacKey(di.getEncryptedHmacKey());
/*  78: 89 */     setEncryptedHmacValue(di.getEncryptedHmacValue());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public AgileEncryptionHeader(CipherAlgorithm algorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/*  82:    */   {
/*  83: 94 */     setCipherAlgorithm(algorithm);
/*  84: 95 */     setHashAlgorithm(hashAlgorithm);
/*  85: 96 */     setKeySize(keyBits);
/*  86: 97 */     setBlockSize(blockSize);
/*  87: 98 */     setChainingMode(chainingMode);
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected void setKeySalt(byte[] salt)
/*  91:    */   {
/*  92:104 */     if ((salt == null) || (salt.length != getBlockSize())) {
/*  93:105 */       throw new EncryptedDocumentException("invalid verifier salt");
/*  94:    */     }
/*  95:107 */     super.setKeySalt(salt);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public byte[] getEncryptedHmacKey()
/*  99:    */   {
/* 100:111 */     return this.encryptedHmacKey;
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected void setEncryptedHmacKey(byte[] encryptedHmacKey)
/* 104:    */   {
/* 105:115 */     this.encryptedHmacKey = (encryptedHmacKey == null ? null : (byte[])encryptedHmacKey.clone());
/* 106:    */   }
/* 107:    */   
/* 108:    */   public byte[] getEncryptedHmacValue()
/* 109:    */   {
/* 110:119 */     return this.encryptedHmacValue;
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected void setEncryptedHmacValue(byte[] encryptedHmacValue)
/* 114:    */   {
/* 115:123 */     this.encryptedHmacValue = (encryptedHmacValue == null ? null : (byte[])encryptedHmacValue.clone());
/* 116:    */   }
/* 117:    */   
/* 118:    */   public AgileEncryptionHeader clone()
/* 119:    */     throws CloneNotSupportedException
/* 120:    */   {
/* 121:128 */     AgileEncryptionHeader other = (AgileEncryptionHeader)super.clone();
/* 122:129 */     other.encryptedHmacKey = (this.encryptedHmacKey == null ? null : (byte[])this.encryptedHmacKey.clone());
/* 123:130 */     other.encryptedHmacValue = (this.encryptedHmacValue == null ? null : (byte[])this.encryptedHmacValue.clone());
/* 124:131 */     return other;
/* 125:    */   }
/* 126:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.agile.AgileEncryptionHeader
 * JD-Core Version:    0.7.0.1
 */