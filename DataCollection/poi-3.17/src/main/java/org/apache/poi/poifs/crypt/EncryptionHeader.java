/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import org.apache.poi.EncryptedDocumentException;
/*   4:    */ import org.apache.poi.util.Removal;
/*   5:    */ 
/*   6:    */ public abstract class EncryptionHeader
/*   7:    */   implements Cloneable
/*   8:    */ {
/*   9: 27 */   public static final int ALGORITHM_RC4 = CipherAlgorithm.rc4.ecmaId;
/*  10: 28 */   public static final int ALGORITHM_AES_128 = CipherAlgorithm.aes128.ecmaId;
/*  11: 29 */   public static final int ALGORITHM_AES_192 = CipherAlgorithm.aes192.ecmaId;
/*  12: 30 */   public static final int ALGORITHM_AES_256 = CipherAlgorithm.aes256.ecmaId;
/*  13: 32 */   public static final int HASH_NONE = HashAlgorithm.none.ecmaId;
/*  14: 33 */   public static final int HASH_SHA1 = HashAlgorithm.sha1.ecmaId;
/*  15: 34 */   public static final int HASH_SHA256 = HashAlgorithm.sha256.ecmaId;
/*  16: 35 */   public static final int HASH_SHA384 = HashAlgorithm.sha384.ecmaId;
/*  17: 36 */   public static final int HASH_SHA512 = HashAlgorithm.sha512.ecmaId;
/*  18: 38 */   public static final int PROVIDER_RC4 = CipherProvider.rc4.ecmaId;
/*  19: 39 */   public static final int PROVIDER_AES = CipherProvider.aes.ecmaId;
/*  20: 41 */   public static final int MODE_ECB = ChainingMode.ecb.ecmaId;
/*  21: 42 */   public static final int MODE_CBC = ChainingMode.cbc.ecmaId;
/*  22: 43 */   public static final int MODE_CFB = ChainingMode.cfb.ecmaId;
/*  23:    */   private int flags;
/*  24:    */   private int sizeExtra;
/*  25:    */   private CipherAlgorithm cipherAlgorithm;
/*  26:    */   private HashAlgorithm hashAlgorithm;
/*  27:    */   private int keyBits;
/*  28:    */   private int blockSize;
/*  29:    */   private CipherProvider providerType;
/*  30:    */   private ChainingMode chainingMode;
/*  31:    */   private byte[] keySalt;
/*  32:    */   private String cspName;
/*  33:    */   
/*  34:    */   public ChainingMode getChainingMode()
/*  35:    */   {
/*  36: 59 */     return this.chainingMode;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected void setChainingMode(ChainingMode chainingMode)
/*  40:    */   {
/*  41: 63 */     this.chainingMode = chainingMode;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getFlags()
/*  45:    */   {
/*  46: 67 */     return this.flags;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected void setFlags(int flags)
/*  50:    */   {
/*  51: 71 */     this.flags = flags;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getSizeExtra()
/*  55:    */   {
/*  56: 75 */     return this.sizeExtra;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void setSizeExtra(int sizeExtra)
/*  60:    */   {
/*  61: 79 */     this.sizeExtra = sizeExtra;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public CipherAlgorithm getCipherAlgorithm()
/*  65:    */   {
/*  66: 83 */     return this.cipherAlgorithm;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm)
/*  70:    */   {
/*  71: 87 */     this.cipherAlgorithm = cipherAlgorithm;
/*  72: 88 */     if (cipherAlgorithm.allowedKeySize.length == 1) {
/*  73: 89 */       setKeySize(cipherAlgorithm.defaultKeySize);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public HashAlgorithm getHashAlgorithm()
/*  78:    */   {
/*  79: 94 */     return this.hashAlgorithm;
/*  80:    */   }
/*  81:    */   
/*  82:    */   /**
/*  83:    */    * @deprecated
/*  84:    */    */
/*  85:    */   @Removal(version="3.18")
/*  86:    */   public HashAlgorithm getHashAlgorithmEx()
/*  87:    */   {
/*  88:102 */     return this.hashAlgorithm;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void setHashAlgorithm(HashAlgorithm hashAlgorithm)
/*  92:    */   {
/*  93:106 */     this.hashAlgorithm = hashAlgorithm;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public int getKeySize()
/*  97:    */   {
/*  98:110 */     return this.keyBits;
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected void setKeySize(int keyBits)
/* 102:    */   {
/* 103:121 */     this.keyBits = keyBits;
/* 104:122 */     for (int allowedBits : getCipherAlgorithm().allowedKeySize) {
/* 105:123 */       if (allowedBits == keyBits) {
/* 106:124 */         return;
/* 107:    */       }
/* 108:    */     }
/* 109:127 */     throw new EncryptedDocumentException("KeySize " + keyBits + " not allowed for cipher " + getCipherAlgorithm());
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getBlockSize()
/* 113:    */   {
/* 114:131 */     return this.blockSize;
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected void setBlockSize(int blockSize)
/* 118:    */   {
/* 119:135 */     this.blockSize = blockSize;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public byte[] getKeySalt()
/* 123:    */   {
/* 124:139 */     return this.keySalt;
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected void setKeySalt(byte[] salt)
/* 128:    */   {
/* 129:143 */     this.keySalt = (salt == null ? null : (byte[])salt.clone());
/* 130:    */   }
/* 131:    */   
/* 132:    */   public CipherProvider getCipherProvider()
/* 133:    */   {
/* 134:147 */     return this.providerType;
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void setCipherProvider(CipherProvider providerType)
/* 138:    */   {
/* 139:151 */     this.providerType = providerType;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String getCspName()
/* 143:    */   {
/* 144:155 */     return this.cspName;
/* 145:    */   }
/* 146:    */   
/* 147:    */   protected void setCspName(String cspName)
/* 148:    */   {
/* 149:159 */     this.cspName = cspName;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public EncryptionHeader clone()
/* 153:    */     throws CloneNotSupportedException
/* 154:    */   {
/* 155:164 */     EncryptionHeader other = (EncryptionHeader)super.clone();
/* 156:165 */     other.keySalt = (this.keySalt == null ? null : (byte[])this.keySalt.clone());
/* 157:166 */     return other;
/* 158:    */   }
/* 159:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.EncryptionHeader
 * JD-Core Version:    0.7.0.1
 */