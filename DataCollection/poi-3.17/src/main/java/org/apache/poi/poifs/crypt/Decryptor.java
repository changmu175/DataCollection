/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import javax.crypto.Cipher;
/*   7:    */ import javax.crypto.SecretKey;
/*   8:    */ import javax.crypto.spec.SecretKeySpec;
/*   9:    */ import org.apache.poi.EncryptedDocumentException;
/*  10:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  11:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  12:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*  13:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  14:    */ 
/*  15:    */ public abstract class Decryptor
/*  16:    */   implements Cloneable
/*  17:    */ {
/*  18:    */   public static final String DEFAULT_PASSWORD = "VelvetSweatshop";
/*  19:    */   public static final String DEFAULT_POIFS_ENTRY = "EncryptedPackage";
/*  20:    */   protected EncryptionInfo encryptionInfo;
/*  21:    */   private SecretKey secretKey;
/*  22:    */   private byte[] verifier;
/*  23:    */   private byte[] integrityHmacKey;
/*  24:    */   private byte[] integrityHmacValue;
/*  25:    */   
/*  26:    */   public abstract InputStream getDataStream(DirectoryNode paramDirectoryNode)
/*  27:    */     throws IOException, GeneralSecurityException;
/*  28:    */   
/*  29:    */   public InputStream getDataStream(InputStream stream, int size, int initialPos)
/*  30:    */     throws IOException, GeneralSecurityException
/*  31:    */   {
/*  32: 70 */     throw new EncryptedDocumentException("this decryptor doesn't support reading from a stream");
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setChunkSize(int chunkSize)
/*  36:    */   {
/*  37: 81 */     throw new EncryptedDocumentException("this decryptor doesn't support changing the chunk size");
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Cipher initCipherForBlock(Cipher cipher, int block)
/*  41:    */     throws GeneralSecurityException
/*  42:    */   {
/*  43: 94 */     throw new EncryptedDocumentException("this decryptor doesn't support initCipherForBlock");
/*  44:    */   }
/*  45:    */   
/*  46:    */   public abstract boolean verifyPassword(String paramString)
/*  47:    */     throws GeneralSecurityException;
/*  48:    */   
/*  49:    */   public abstract long getLength();
/*  50:    */   
/*  51:    */   public static Decryptor getInstance(EncryptionInfo info)
/*  52:    */   {
/*  53:118 */     Decryptor d = info.getDecryptor();
/*  54:119 */     if (d == null) {
/*  55:120 */       throw new EncryptedDocumentException("Unsupported version");
/*  56:    */     }
/*  57:122 */     return d;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public InputStream getDataStream(NPOIFSFileSystem fs)
/*  61:    */     throws IOException, GeneralSecurityException
/*  62:    */   {
/*  63:126 */     return getDataStream(fs.getRoot());
/*  64:    */   }
/*  65:    */   
/*  66:    */   public InputStream getDataStream(OPOIFSFileSystem fs)
/*  67:    */     throws IOException, GeneralSecurityException
/*  68:    */   {
/*  69:130 */     return getDataStream(fs.getRoot());
/*  70:    */   }
/*  71:    */   
/*  72:    */   public InputStream getDataStream(POIFSFileSystem fs)
/*  73:    */     throws IOException, GeneralSecurityException
/*  74:    */   {
/*  75:134 */     return getDataStream(fs.getRoot());
/*  76:    */   }
/*  77:    */   
/*  78:    */   public byte[] getVerifier()
/*  79:    */   {
/*  80:139 */     return this.verifier;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public SecretKey getSecretKey()
/*  84:    */   {
/*  85:143 */     return this.secretKey;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public byte[] getIntegrityHmacKey()
/*  89:    */   {
/*  90:147 */     return this.integrityHmacKey;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public byte[] getIntegrityHmacValue()
/*  94:    */   {
/*  95:151 */     return this.integrityHmacValue;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void setSecretKey(SecretKey secretKey)
/*  99:    */   {
/* 100:155 */     this.secretKey = secretKey;
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected void setVerifier(byte[] verifier)
/* 104:    */   {
/* 105:159 */     this.verifier = (verifier == null ? null : (byte[])verifier.clone());
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected void setIntegrityHmacKey(byte[] integrityHmacKey)
/* 109:    */   {
/* 110:163 */     this.integrityHmacKey = (integrityHmacKey == null ? null : (byte[])integrityHmacKey.clone());
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected void setIntegrityHmacValue(byte[] integrityHmacValue)
/* 114:    */   {
/* 115:167 */     this.integrityHmacValue = (integrityHmacValue == null ? null : (byte[])integrityHmacValue.clone());
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected int getBlockSizeInBytes()
/* 119:    */   {
/* 120:171 */     return this.encryptionInfo.getHeader().getBlockSize();
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected int getKeySizeInBytes()
/* 124:    */   {
/* 125:175 */     return this.encryptionInfo.getHeader().getKeySize() / 8;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public EncryptionInfo getEncryptionInfo()
/* 129:    */   {
/* 130:179 */     return this.encryptionInfo;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setEncryptionInfo(EncryptionInfo encryptionInfo)
/* 134:    */   {
/* 135:183 */     this.encryptionInfo = encryptionInfo;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public Decryptor clone()
/* 139:    */     throws CloneNotSupportedException
/* 140:    */   {
/* 141:188 */     Decryptor other = (Decryptor)super.clone();
/* 142:189 */     other.integrityHmacKey = ((byte[])this.integrityHmacKey.clone());
/* 143:190 */     other.integrityHmacValue = ((byte[])this.integrityHmacValue.clone());
/* 144:191 */     other.verifier = ((byte[])this.verifier.clone());
/* 145:192 */     other.secretKey = new SecretKeySpec(this.secretKey.getEncoded(), this.secretKey.getAlgorithm());
/* 146:    */     
/* 147:194 */     return other;
/* 148:    */   }
/* 149:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.Decryptor
 * JD-Core Version:    0.7.0.1
 */