/*   1:    */ package org.apache.poi.poifs.crypt.binaryrc4;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.security.GeneralSecurityException;
/*   7:    */ import java.security.MessageDigest;
/*   8:    */ import java.security.SecureRandom;
/*   9:    */ import java.util.Random;
/*  10:    */ import javax.crypto.Cipher;
/*  11:    */ import javax.crypto.SecretKey;
/*  12:    */ import org.apache.poi.EncryptedDocumentException;
/*  13:    */ import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
/*  14:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  15:    */ import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
/*  16:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  17:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  18:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  19:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  20:    */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  21:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  22:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  23:    */ 
/*  24:    */ public class BinaryRC4Encryptor
/*  25:    */   extends Encryptor
/*  26:    */   implements Cloneable
/*  27:    */ {
/*  28: 44 */   private int chunkSize = 512;
/*  29:    */   
/*  30:    */   public void confirmPassword(String password)
/*  31:    */   {
/*  32: 51 */     Random r = new SecureRandom();
/*  33: 52 */     byte[] salt = new byte[16];
/*  34: 53 */     byte[] verifier = new byte[16];
/*  35: 54 */     r.nextBytes(salt);
/*  36: 55 */     r.nextBytes(verifier);
/*  37: 56 */     confirmPassword(password, null, null, verifier, salt, null);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt)
/*  41:    */   {
/*  42: 63 */     BinaryRC4EncryptionVerifier ver = (BinaryRC4EncryptionVerifier)getEncryptionInfo().getVerifier();
/*  43: 64 */     ver.setSalt(verifierSalt);
/*  44: 65 */     SecretKey skey = BinaryRC4Decryptor.generateSecretKey(password, ver);
/*  45: 66 */     setSecretKey(skey);
/*  46:    */     try
/*  47:    */     {
/*  48: 68 */       Cipher cipher = BinaryRC4Decryptor.initCipherForBlock(null, 0, getEncryptionInfo(), skey, 1);
/*  49: 69 */       byte[] encryptedVerifier = new byte[16];
/*  50: 70 */       cipher.update(verifier, 0, 16, encryptedVerifier);
/*  51: 71 */       ver.setEncryptedVerifier(encryptedVerifier);
/*  52: 72 */       HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  53: 73 */       MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/*  54: 74 */       byte[] calcVerifierHash = hashAlg.digest(verifier);
/*  55: 75 */       byte[] encryptedVerifierHash = cipher.doFinal(calcVerifierHash);
/*  56: 76 */       ver.setEncryptedVerifierHash(encryptedVerifierHash);
/*  57:    */     }
/*  58:    */     catch (GeneralSecurityException e)
/*  59:    */     {
/*  60: 78 */       throw new EncryptedDocumentException("Password confirmation failed", e);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public OutputStream getDataStream(DirectoryNode dir)
/*  65:    */     throws IOException, GeneralSecurityException
/*  66:    */   {
/*  67: 85 */     OutputStream countStream = new BinaryRC4CipherOutputStream(dir);
/*  68: 86 */     return countStream;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public BinaryRC4CipherOutputStream getDataStream(OutputStream stream, int initialOffset)
/*  72:    */     throws IOException, GeneralSecurityException
/*  73:    */   {
/*  74: 92 */     return new BinaryRC4CipherOutputStream(stream);
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected int getKeySizeInBytes()
/*  78:    */   {
/*  79: 96 */     return getEncryptionInfo().getHeader().getKeySize() / 8;
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected void createEncryptionInfoEntry(DirectoryNode dir)
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:100 */     DataSpaceMapUtils.addDefaultDataSpace(dir);
/*  86:101 */     final EncryptionInfo info = getEncryptionInfo();
/*  87:102 */     final BinaryRC4EncryptionHeader header = (BinaryRC4EncryptionHeader)info.getHeader();
/*  88:103 */     final BinaryRC4EncryptionVerifier verifier = (BinaryRC4EncryptionVerifier)info.getVerifier();
/*  89:104 */     EncryptionRecord er = new EncryptionRecord()
/*  90:    */     {
/*  91:    */       public void write(LittleEndianByteArrayOutputStream bos)
/*  92:    */       {
/*  93:107 */         bos.writeShort(info.getVersionMajor());
/*  94:108 */         bos.writeShort(info.getVersionMinor());
/*  95:109 */         header.write(bos);
/*  96:110 */         verifier.write(bos);
/*  97:    */       }
/*  98:112 */     };
/*  99:113 */     DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", er);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setChunkSize(int chunkSize)
/* 103:    */   {
/* 104:118 */     this.chunkSize = chunkSize;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public BinaryRC4Encryptor clone()
/* 108:    */     throws CloneNotSupportedException
/* 109:    */   {
/* 110:123 */     return (BinaryRC4Encryptor)super.clone();
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected class BinaryRC4CipherOutputStream
/* 114:    */     extends ChunkedCipherOutputStream
/* 115:    */   {
/* 116:    */     public BinaryRC4CipherOutputStream(OutputStream stream)
/* 117:    */       throws IOException, GeneralSecurityException
/* 118:    */     {
/* 119:130 */       super(BinaryRC4Encryptor.this.chunkSize);
/* 120:    */     }
/* 121:    */     
/* 122:    */     public BinaryRC4CipherOutputStream(DirectoryNode dir)
/* 123:    */       throws IOException, GeneralSecurityException
/* 124:    */     {
/* 125:135 */       super(BinaryRC4Encryptor.this.chunkSize);
/* 126:    */     }
/* 127:    */     
/* 128:    */     protected Cipher initCipherForBlock(Cipher cipher, int block, boolean lastChunk)
/* 129:    */       throws GeneralSecurityException
/* 130:    */     {
/* 131:141 */       return BinaryRC4Decryptor.initCipherForBlock(cipher, block, BinaryRC4Encryptor.this.getEncryptionInfo(), BinaryRC4Encryptor.this.getSecretKey(), 1);
/* 132:    */     }
/* 133:    */     
/* 134:    */     protected void calculateChecksum(File file, int i) {}
/* 135:    */     
/* 136:    */     protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile)
/* 137:    */       throws IOException, GeneralSecurityException
/* 138:    */     {
/* 139:151 */       BinaryRC4Encryptor.this.createEncryptionInfoEntry(dir);
/* 140:    */     }
/* 141:    */     
/* 142:    */     public void flush()
/* 143:    */       throws IOException
/* 144:    */     {
/* 145:156 */       writeChunk(false);
/* 146:157 */       super.flush();
/* 147:    */     }
/* 148:    */   }
/* 149:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.binaryrc4.BinaryRC4Encryptor
 * JD-Core Version:    0.7.0.1
 */