/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Removal;
/*   4:    */ 
/*   5:    */ public abstract class EncryptionVerifier
/*   6:    */   implements Cloneable
/*   7:    */ {
/*   8:    */   private byte[] salt;
/*   9:    */   private byte[] encryptedVerifier;
/*  10:    */   private byte[] encryptedVerifierHash;
/*  11:    */   private byte[] encryptedKey;
/*  12:    */   private int spinCount;
/*  13:    */   private CipherAlgorithm cipherAlgorithm;
/*  14:    */   private ChainingMode chainingMode;
/*  15:    */   private HashAlgorithm hashAlgorithm;
/*  16:    */   
/*  17:    */   public byte[] getSalt()
/*  18:    */   {
/*  19: 38 */     return this.salt;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public byte[] getEncryptedVerifier()
/*  23:    */   {
/*  24: 42 */     return this.encryptedVerifier;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public byte[] getEncryptedVerifierHash()
/*  28:    */   {
/*  29: 46 */     return this.encryptedVerifierHash;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getSpinCount()
/*  33:    */   {
/*  34: 50 */     return this.spinCount;
/*  35:    */   }
/*  36:    */   
/*  37:    */   /**
/*  38:    */    * @deprecated
/*  39:    */    */
/*  40:    */   @Removal(version="3.18")
/*  41:    */   public int getCipherMode()
/*  42:    */   {
/*  43: 58 */     return this.chainingMode.ecmaId;
/*  44:    */   }
/*  45:    */   
/*  46:    */   /**
/*  47:    */    * @deprecated
/*  48:    */    */
/*  49:    */   public int getAlgorithm()
/*  50:    */   {
/*  51: 65 */     return this.cipherAlgorithm.ecmaId;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public byte[] getEncryptedKey()
/*  55:    */   {
/*  56: 69 */     return this.encryptedKey;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public CipherAlgorithm getCipherAlgorithm()
/*  60:    */   {
/*  61: 73 */     return this.cipherAlgorithm;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public HashAlgorithm getHashAlgorithm()
/*  65:    */   {
/*  66: 77 */     return this.hashAlgorithm;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public ChainingMode getChainingMode()
/*  70:    */   {
/*  71: 81 */     return this.chainingMode;
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected void setSalt(byte[] salt)
/*  75:    */   {
/*  76: 85 */     this.salt = (salt == null ? null : (byte[])salt.clone());
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected void setEncryptedVerifier(byte[] encryptedVerifier)
/*  80:    */   {
/*  81: 89 */     this.encryptedVerifier = (encryptedVerifier == null ? null : (byte[])encryptedVerifier.clone());
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash)
/*  85:    */   {
/*  86: 93 */     this.encryptedVerifierHash = (encryptedVerifierHash == null ? null : (byte[])encryptedVerifierHash.clone());
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected void setEncryptedKey(byte[] encryptedKey)
/*  90:    */   {
/*  91: 97 */     this.encryptedKey = (encryptedKey == null ? null : (byte[])encryptedKey.clone());
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected void setSpinCount(int spinCount)
/*  95:    */   {
/*  96:101 */     this.spinCount = spinCount;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm)
/* 100:    */   {
/* 101:105 */     this.cipherAlgorithm = cipherAlgorithm;
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected void setChainingMode(ChainingMode chainingMode)
/* 105:    */   {
/* 106:109 */     this.chainingMode = chainingMode;
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected void setHashAlgorithm(HashAlgorithm hashAlgorithm)
/* 110:    */   {
/* 111:113 */     this.hashAlgorithm = hashAlgorithm;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public EncryptionVerifier clone()
/* 115:    */     throws CloneNotSupportedException
/* 116:    */   {
/* 117:118 */     EncryptionVerifier other = (EncryptionVerifier)super.clone();
/* 118:119 */     other.salt = (this.salt == null ? null : (byte[])this.salt.clone());
/* 119:120 */     other.encryptedVerifier = (this.encryptedVerifier == null ? null : (byte[])this.encryptedVerifier.clone());
/* 120:121 */     other.encryptedVerifierHash = (this.encryptedVerifierHash == null ? null : (byte[])this.encryptedVerifierHash.clone());
/* 121:122 */     other.encryptedKey = (this.encryptedKey == null ? null : (byte[])this.encryptedKey.clone());
/* 122:123 */     return other;
/* 123:    */   }
/* 124:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.EncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */