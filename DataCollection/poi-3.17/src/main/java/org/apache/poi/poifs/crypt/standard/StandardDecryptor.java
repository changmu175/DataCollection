/*   1:    */ package org.apache.poi.poifs.crypt.standard;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import java.security.MessageDigest;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import javax.crypto.Cipher;
/*   9:    */ import javax.crypto.CipherInputStream;
/*  10:    */ import javax.crypto.SecretKey;
/*  11:    */ import javax.crypto.spec.SecretKeySpec;
/*  12:    */ import org.apache.poi.EncryptedDocumentException;
/*  13:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  14:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  15:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  16:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  17:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  18:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  19:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  20:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  21:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  22:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  23:    */ import org.apache.poi.util.BoundedInputStream;
/*  24:    */ import org.apache.poi.util.LittleEndian;
/*  25:    */ 
/*  26:    */ public class StandardDecryptor
/*  27:    */   extends Decryptor
/*  28:    */   implements Cloneable
/*  29:    */ {
/*  30: 47 */   private long _length = -1L;
/*  31:    */   
/*  32:    */   public boolean verifyPassword(String password)
/*  33:    */   {
/*  34: 54 */     EncryptionVerifier ver = getEncryptionInfo().getVerifier();
/*  35: 55 */     SecretKey skey = generateSecretKey(password, ver, getKeySizeInBytes());
/*  36: 56 */     Cipher cipher = getCipher(skey);
/*  37:    */     try
/*  38:    */     {
/*  39: 59 */       byte[] encryptedVerifier = ver.getEncryptedVerifier();
/*  40: 60 */       byte[] verifier = cipher.doFinal(encryptedVerifier);
/*  41: 61 */       setVerifier(verifier);
/*  42: 62 */       MessageDigest sha1 = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
/*  43: 63 */       byte[] calcVerifierHash = sha1.digest(verifier);
/*  44: 64 */       byte[] encryptedVerifierHash = ver.getEncryptedVerifierHash();
/*  45: 65 */       byte[] decryptedVerifierHash = cipher.doFinal(encryptedVerifierHash);
/*  46:    */       
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50: 70 */       byte[] verifierHash = Arrays.copyOf(decryptedVerifierHash, calcVerifierHash.length);
/*  51: 72 */       if (Arrays.equals(calcVerifierHash, verifierHash))
/*  52:    */       {
/*  53: 73 */         setSecretKey(skey);
/*  54: 74 */         return true;
/*  55:    */       }
/*  56: 76 */       return false;
/*  57:    */     }
/*  58:    */     catch (GeneralSecurityException e)
/*  59:    */     {
/*  60: 79 */       throw new EncryptedDocumentException(e);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver, int keySize)
/*  65:    */   {
/*  66: 84 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  67:    */     
/*  68: 86 */     byte[] pwHash = CryptoFunctions.hashPassword(password, hashAlgo, ver.getSalt(), ver.getSpinCount());
/*  69:    */     
/*  70: 88 */     byte[] blockKey = new byte[4];
/*  71: 89 */     LittleEndian.putInt(blockKey, 0, 0);
/*  72:    */     
/*  73: 91 */     byte[] finalHash = CryptoFunctions.generateKey(pwHash, hashAlgo, blockKey, hashAlgo.hashSize);
/*  74: 92 */     byte[] x1 = fillAndXor(finalHash, (byte)54);
/*  75: 93 */     byte[] x2 = fillAndXor(finalHash, (byte)92);
/*  76:    */     
/*  77: 95 */     byte[] x3 = new byte[x1.length + x2.length];
/*  78: 96 */     System.arraycopy(x1, 0, x3, 0, x1.length);
/*  79: 97 */     System.arraycopy(x2, 0, x3, x1.length, x2.length);
/*  80:    */     
/*  81: 99 */     byte[] key = Arrays.copyOf(x3, keySize);
/*  82:    */     
/*  83:101 */     SecretKey skey = new SecretKeySpec(key, ver.getCipherAlgorithm().jceId);
/*  84:102 */     return skey;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected static byte[] fillAndXor(byte[] hash, byte fillByte)
/*  88:    */   {
/*  89:106 */     byte[] buff = new byte[64];
/*  90:107 */     Arrays.fill(buff, fillByte);
/*  91:109 */     for (int i = 0; i < hash.length; i++) {
/*  92:110 */       buff[i] = ((byte)(buff[i] ^ hash[i]));
/*  93:    */     }
/*  94:113 */     MessageDigest sha1 = CryptoFunctions.getMessageDigest(HashAlgorithm.sha1);
/*  95:114 */     return sha1.digest(buff);
/*  96:    */   }
/*  97:    */   
/*  98:    */   private Cipher getCipher(SecretKey key)
/*  99:    */   {
/* 100:118 */     EncryptionHeader em = getEncryptionInfo().getHeader();
/* 101:119 */     ChainingMode cm = em.getChainingMode();
/* 102:120 */     assert (cm == ChainingMode.ecb);
/* 103:121 */     return CryptoFunctions.getCipher(key, em.getCipherAlgorithm(), cm, null, 2);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public InputStream getDataStream(DirectoryNode dir)
/* 107:    */     throws IOException
/* 108:    */   {
/* 109:127 */     DocumentInputStream dis = dir.createDocumentInputStream("EncryptedPackage");
/* 110:    */     
/* 111:129 */     this._length = dis.readLong();
/* 112:131 */     if (getSecretKey() == null) {
/* 113:132 */       verifyPassword(null);
/* 114:    */     }
/* 115:137 */     int blockSize = getEncryptionInfo().getHeader().getCipherAlgorithm().blockSize;
/* 116:138 */     long cipherLen = (this._length / blockSize + 1L) * blockSize;
/* 117:139 */     Cipher cipher = getCipher(getSecretKey());
/* 118:    */     
/* 119:141 */     InputStream boundedDis = new BoundedInputStream(dis, cipherLen);
/* 120:142 */     return new BoundedInputStream(new CipherInputStream(boundedDis, cipher), this._length);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public long getLength()
/* 124:    */   {
/* 125:150 */     if (this._length == -1L) {
/* 126:151 */       throw new IllegalStateException("Decryptor.getDataStream() was not called");
/* 127:    */     }
/* 128:153 */     return this._length;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public StandardDecryptor clone()
/* 132:    */     throws CloneNotSupportedException
/* 133:    */   {
/* 134:158 */     return (StandardDecryptor)super.clone();
/* 135:    */   }
/* 136:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.StandardDecryptor
 * JD-Core Version:    0.7.0.1
 */