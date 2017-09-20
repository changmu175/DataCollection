/*   1:    */ package org.apache.poi.poifs.crypt.binaryrc4;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import java.security.MessageDigest;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import javax.crypto.Cipher;
/*   9:    */ import javax.crypto.SecretKey;
/*  10:    */ import javax.crypto.spec.SecretKeySpec;
/*  11:    */ import org.apache.poi.EncryptedDocumentException;
/*  12:    */ import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
/*  13:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  14:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  15:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  16:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  17:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  18:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  19:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  20:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  21:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  22:    */ import org.apache.poi.util.LittleEndian;
/*  23:    */ import org.apache.poi.util.StringUtil;
/*  24:    */ 
/*  25:    */ public class BinaryRC4Decryptor
/*  26:    */   extends Decryptor
/*  27:    */   implements Cloneable
/*  28:    */ {
/*  29: 38 */   private long length = -1L;
/*  30: 39 */   private int chunkSize = 512;
/*  31:    */   
/*  32:    */   private class BinaryRC4CipherInputStream
/*  33:    */     extends ChunkedCipherInputStream
/*  34:    */   {
/*  35:    */     protected Cipher initCipherForBlock(Cipher existing, int block)
/*  36:    */       throws GeneralSecurityException
/*  37:    */     {
/*  38: 46 */       return BinaryRC4Decryptor.this.initCipherForBlock(existing, block);
/*  39:    */     }
/*  40:    */     
/*  41:    */     public BinaryRC4CipherInputStream(DocumentInputStream stream, long size)
/*  42:    */       throws GeneralSecurityException
/*  43:    */     {
/*  44: 51 */       super(size, BinaryRC4Decryptor.this.chunkSize);
/*  45:    */     }
/*  46:    */     
/*  47:    */     public BinaryRC4CipherInputStream(InputStream stream, int size, int initialPos)
/*  48:    */       throws GeneralSecurityException
/*  49:    */     {
/*  50: 56 */       super(size, BinaryRC4Decryptor.this.chunkSize, initialPos);
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean verifyPassword(String password)
/*  55:    */   {
/*  56: 65 */     EncryptionVerifier ver = getEncryptionInfo().getVerifier();
/*  57: 66 */     SecretKey skey = generateSecretKey(password, ver);
/*  58:    */     try
/*  59:    */     {
/*  60: 68 */       Cipher cipher = initCipherForBlock(null, 0, getEncryptionInfo(), skey, 2);
/*  61: 69 */       byte[] encryptedVerifier = ver.getEncryptedVerifier();
/*  62: 70 */       byte[] verifier = new byte[encryptedVerifier.length];
/*  63: 71 */       cipher.update(encryptedVerifier, 0, encryptedVerifier.length, verifier);
/*  64: 72 */       setVerifier(verifier);
/*  65: 73 */       byte[] encryptedVerifierHash = ver.getEncryptedVerifierHash();
/*  66: 74 */       byte[] verifierHash = cipher.doFinal(encryptedVerifierHash);
/*  67: 75 */       HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  68: 76 */       MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/*  69: 77 */       byte[] calcVerifierHash = hashAlg.digest(verifier);
/*  70: 78 */       if (Arrays.equals(calcVerifierHash, verifierHash))
/*  71:    */       {
/*  72: 79 */         setSecretKey(skey);
/*  73: 80 */         return true;
/*  74:    */       }
/*  75:    */     }
/*  76:    */     catch (GeneralSecurityException e)
/*  77:    */     {
/*  78: 83 */       throw new EncryptedDocumentException(e);
/*  79:    */     }
/*  80: 85 */     return false;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Cipher initCipherForBlock(Cipher cipher, int block)
/*  84:    */     throws GeneralSecurityException
/*  85:    */   {
/*  86: 91 */     return initCipherForBlock(cipher, block, getEncryptionInfo(), getSecretKey(), 2);
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfo encryptionInfo, SecretKey skey, int encryptMode)
/*  90:    */     throws GeneralSecurityException
/*  91:    */   {
/*  92: 97 */     EncryptionVerifier ver = encryptionInfo.getVerifier();
/*  93: 98 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  94: 99 */     byte[] blockKey = new byte[4];
/*  95:100 */     LittleEndian.putUInt(blockKey, 0, block);
/*  96:101 */     byte[] encKey = CryptoFunctions.generateKey(skey.getEncoded(), hashAlgo, blockKey, 16);
/*  97:102 */     SecretKey key = new SecretKeySpec(encKey, skey.getAlgorithm());
/*  98:103 */     if (cipher == null)
/*  99:    */     {
/* 100:104 */       EncryptionHeader em = encryptionInfo.getHeader();
/* 101:105 */       cipher = CryptoFunctions.getCipher(key, em.getCipherAlgorithm(), null, null, encryptMode);
/* 102:    */     }
/* 103:    */     else
/* 104:    */     {
/* 105:107 */       cipher.init(encryptMode, key);
/* 106:    */     }
/* 107:109 */     return cipher;
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver)
/* 111:    */   {
/* 112:113 */     if (password.length() > 255) {
/* 113:114 */       password = password.substring(0, 255);
/* 114:    */     }
/* 115:116 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/* 116:117 */     MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/* 117:118 */     byte[] hash = hashAlg.digest(StringUtil.getToUnicodeLE(password));
/* 118:119 */     byte[] salt = ver.getSalt();
/* 119:120 */     hashAlg.reset();
/* 120:121 */     for (int i = 0; i < 16; i++)
/* 121:    */     {
/* 122:122 */       hashAlg.update(hash, 0, 5);
/* 123:123 */       hashAlg.update(salt);
/* 124:    */     }
/* 125:126 */     hash = new byte[5];
/* 126:127 */     System.arraycopy(hashAlg.digest(), 0, hash, 0, 5);
/* 127:128 */     SecretKey skey = new SecretKeySpec(hash, ver.getCipherAlgorithm().jceId);
/* 128:129 */     return skey;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public ChunkedCipherInputStream getDataStream(DirectoryNode dir)
/* 132:    */     throws IOException, GeneralSecurityException
/* 133:    */   {
/* 134:136 */     DocumentInputStream dis = dir.createDocumentInputStream("EncryptedPackage");
/* 135:137 */     this.length = dis.readLong();
/* 136:138 */     return new BinaryRC4CipherInputStream(dis, this.length);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public InputStream getDataStream(InputStream stream, int size, int initialPos)
/* 140:    */     throws IOException, GeneralSecurityException
/* 141:    */   {
/* 142:144 */     return new BinaryRC4CipherInputStream(stream, size, initialPos);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public long getLength()
/* 146:    */   {
/* 147:150 */     if (this.length == -1L) {
/* 148:151 */       throw new IllegalStateException("Decryptor.getDataStream() was not called");
/* 149:    */     }
/* 150:154 */     return this.length;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setChunkSize(int chunkSize)
/* 154:    */   {
/* 155:159 */     this.chunkSize = chunkSize;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public BinaryRC4Decryptor clone()
/* 159:    */     throws CloneNotSupportedException
/* 160:    */   {
/* 161:164 */     return (BinaryRC4Decryptor)super.clone();
/* 162:    */   }
/* 163:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4Decryptor
 * JD-Core Version:    0.7.0.1
 */