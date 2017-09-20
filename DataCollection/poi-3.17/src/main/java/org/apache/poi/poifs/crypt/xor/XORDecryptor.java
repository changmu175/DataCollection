/*   1:    */ package org.apache.poi.poifs.crypt.xor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import javax.crypto.Cipher;
/*   7:    */ import javax.crypto.SecretKey;
/*   8:    */ import javax.crypto.spec.SecretKeySpec;
/*   9:    */ import org.apache.poi.EncryptedDocumentException;
/*  10:    */ import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
/*  11:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  12:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  13:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  14:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  15:    */ import org.apache.poi.util.LittleEndian;
/*  16:    */ 
/*  17:    */ public class XORDecryptor
/*  18:    */   extends Decryptor
/*  19:    */   implements Cloneable
/*  20:    */ {
/*  21: 37 */   private long length = -1L;
/*  22: 38 */   private int chunkSize = 512;
/*  23:    */   
/*  24:    */   public boolean verifyPassword(String password)
/*  25:    */   {
/*  26: 45 */     XOREncryptionVerifier ver = (XOREncryptionVerifier)getEncryptionInfo().getVerifier();
/*  27: 46 */     int keyVer = LittleEndian.getUShort(ver.getEncryptedKey());
/*  28: 47 */     int verifierVer = LittleEndian.getUShort(ver.getEncryptedVerifier());
/*  29: 48 */     int keyComp = CryptoFunctions.createXorKey1(password);
/*  30: 49 */     int verifierComp = CryptoFunctions.createXorVerifier1(password);
/*  31: 50 */     if ((keyVer == keyComp) && (verifierVer == verifierComp))
/*  32:    */     {
/*  33: 51 */       byte[] xorArray = CryptoFunctions.createXorArray1(password);
/*  34: 52 */       setSecretKey(new SecretKeySpec(xorArray, "XOR"));
/*  35: 53 */       return true;
/*  36:    */     }
/*  37: 55 */     return false;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Cipher initCipherForBlock(Cipher cipher, int block)
/*  41:    */     throws GeneralSecurityException
/*  42:    */   {
/*  43: 62 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfo encryptionInfo, SecretKey skey, int encryptMode)
/*  47:    */     throws GeneralSecurityException
/*  48:    */   {
/*  49: 68 */     return null;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ChunkedCipherInputStream getDataStream(DirectoryNode dir)
/*  53:    */     throws IOException, GeneralSecurityException
/*  54:    */   {
/*  55: 73 */     throw new EncryptedDocumentException("not supported");
/*  56:    */   }
/*  57:    */   
/*  58:    */   public InputStream getDataStream(InputStream stream, int size, int initialPos)
/*  59:    */     throws IOException, GeneralSecurityException
/*  60:    */   {
/*  61: 79 */     return new XORCipherInputStream(stream, initialPos);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public long getLength()
/*  65:    */   {
/*  66: 85 */     if (this.length == -1L) {
/*  67: 86 */       throw new IllegalStateException("Decryptor.getDataStream() was not called");
/*  68:    */     }
/*  69: 89 */     return this.length;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setChunkSize(int chunkSize)
/*  73:    */   {
/*  74: 94 */     this.chunkSize = chunkSize;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public XORDecryptor clone()
/*  78:    */     throws CloneNotSupportedException
/*  79:    */   {
/*  80: 99 */     return (XORDecryptor)super.clone();
/*  81:    */   }
/*  82:    */   
/*  83:    */   private class XORCipherInputStream
/*  84:    */     extends ChunkedCipherInputStream
/*  85:    */   {
/*  86:    */     private final int initialOffset;
/*  87:104 */     private int recordStart = 0;
/*  88:105 */     private int recordEnd = 0;
/*  89:    */     
/*  90:    */     public XORCipherInputStream(InputStream stream, int initialPos)
/*  91:    */       throws GeneralSecurityException
/*  92:    */     {
/*  93:109 */       super(2147483647L, XORDecryptor.this.chunkSize);
/*  94:110 */       this.initialOffset = initialPos;
/*  95:    */     }
/*  96:    */     
/*  97:    */     protected Cipher initCipherForBlock(Cipher existing, int block)
/*  98:    */       throws GeneralSecurityException
/*  99:    */     {
/* 100:116 */       return XORDecryptor.this.initCipherForBlock(existing, block);
/* 101:    */     }
/* 102:    */     
/* 103:    */     protected int invokeCipher(int totalBytes, boolean doFinal)
/* 104:    */     {
/* 105:121 */       int pos = (int)getPos();
/* 106:122 */       byte[] xorArray = XORDecryptor.this.getEncryptionInfo().getDecryptor().getSecretKey().getEncoded();
/* 107:123 */       byte[] chunk = getChunk();
/* 108:124 */       byte[] plain = getPlain();
/* 109:125 */       int posInChunk = pos & getChunkMask();
/* 110:    */       
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:137 */       int xorArrayIndex = this.initialOffset + this.recordEnd + (pos - this.recordStart);
/* 122:139 */       for (int i = 0; (pos + i < this.recordEnd) && (i < totalBytes); i++)
/* 123:    */       {
/* 124:143 */         byte value = plain[(posInChunk + i)];
/* 125:144 */         value = rotateLeft(value, 3);
/* 126:145 */         value = (byte)(value ^ xorArray[(xorArrayIndex + i & 0xF)]);
/* 127:146 */         chunk[(posInChunk + i)] = value;
/* 128:    */       }
/* 129:150 */       return totalBytes;
/* 130:    */     }
/* 131:    */     
/* 132:    */     private byte rotateLeft(byte bits, int shift)
/* 133:    */     {
/* 134:154 */       return (byte)((bits & 0xFF) << shift | (bits & 0xFF) >>> 8 - shift);
/* 135:    */     }
/* 136:    */     
/* 137:    */     public void setNextRecordSize(int recordSize)
/* 138:    */     {
/* 139:166 */       int pos = (int)getPos();
/* 140:167 */       byte[] chunk = getChunk();
/* 141:168 */       int chunkMask = getChunkMask();
/* 142:169 */       this.recordStart = pos;
/* 143:170 */       this.recordEnd = (this.recordStart + recordSize);
/* 144:171 */       int nextBytes = Math.min(recordSize, chunk.length - (pos & chunkMask));
/* 145:172 */       invokeCipher(nextBytes, true);
/* 146:    */     }
/* 147:    */   }
/* 148:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.xor.XORDecryptor
 * JD-Core Version:    0.7.0.1
 */