/*   1:    */ package org.apache.poi.poifs.crypt.xor;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.security.GeneralSecurityException;
/*   7:    */ import java.util.BitSet;
/*   8:    */ import javax.crypto.Cipher;
/*   9:    */ import javax.crypto.SecretKey;
/*  10:    */ import javax.crypto.spec.SecretKeySpec;
/*  11:    */ import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
/*  12:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  13:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  14:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  15:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  16:    */ import org.apache.poi.util.LittleEndian;
/*  17:    */ 
/*  18:    */ public class XOREncryptor
/*  19:    */   extends Encryptor
/*  20:    */   implements Cloneable
/*  21:    */ {
/*  22:    */   public void confirmPassword(String password)
/*  23:    */   {
/*  24: 41 */     int keyComp = CryptoFunctions.createXorKey1(password);
/*  25: 42 */     int verifierComp = CryptoFunctions.createXorVerifier1(password);
/*  26: 43 */     byte[] xorArray = CryptoFunctions.createXorArray1(password);
/*  27:    */     
/*  28: 45 */     byte[] shortBuf = new byte[2];
/*  29: 46 */     XOREncryptionVerifier ver = (XOREncryptionVerifier)getEncryptionInfo().getVerifier();
/*  30: 47 */     LittleEndian.putUShort(shortBuf, 0, keyComp);
/*  31: 48 */     ver.setEncryptedKey(shortBuf);
/*  32: 49 */     LittleEndian.putUShort(shortBuf, 0, verifierComp);
/*  33: 50 */     ver.setEncryptedVerifier(shortBuf);
/*  34: 51 */     setSecretKey(new SecretKeySpec(xorArray, "XOR"));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt)
/*  38:    */   {
/*  39: 58 */     confirmPassword(password);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public OutputStream getDataStream(DirectoryNode dir)
/*  43:    */     throws IOException, GeneralSecurityException
/*  44:    */   {
/*  45: 64 */     return new XORCipherOutputStream(dir);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public XORCipherOutputStream getDataStream(OutputStream stream, int initialOffset)
/*  49:    */     throws IOException, GeneralSecurityException
/*  50:    */   {
/*  51: 70 */     return new XORCipherOutputStream(stream, initialOffset);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected int getKeySizeInBytes()
/*  55:    */   {
/*  56: 74 */     return -1;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setChunkSize(int chunkSize) {}
/*  60:    */   
/*  61:    */   protected void createEncryptionInfoEntry(DirectoryNode dir)
/*  62:    */     throws IOException
/*  63:    */   {}
/*  64:    */   
/*  65:    */   public XOREncryptor clone()
/*  66:    */     throws CloneNotSupportedException
/*  67:    */   {
/*  68: 87 */     return (XOREncryptor)super.clone();
/*  69:    */   }
/*  70:    */   
/*  71:    */   private class XORCipherOutputStream
/*  72:    */     extends ChunkedCipherOutputStream
/*  73:    */   {
/*  74: 91 */     private int recordStart = 0;
/*  75: 92 */     private int recordEnd = 0;
/*  76:    */     
/*  77:    */     public XORCipherOutputStream(OutputStream stream, int initialPos)
/*  78:    */       throws IOException, GeneralSecurityException
/*  79:    */     {
/*  80: 95 */       super(-1);
/*  81:    */     }
/*  82:    */     
/*  83:    */     public XORCipherOutputStream(DirectoryNode dir)
/*  84:    */       throws IOException, GeneralSecurityException
/*  85:    */     {
/*  86: 99 */       super(-1);
/*  87:    */     }
/*  88:    */     
/*  89:    */     protected Cipher initCipherForBlock(Cipher cipher, int block, boolean lastChunk)
/*  90:    */       throws GeneralSecurityException
/*  91:    */     {
/*  92:105 */       return XORDecryptor.initCipherForBlock(cipher, block, XOREncryptor.this.getEncryptionInfo(), XOREncryptor.this.getSecretKey(), 1);
/*  93:    */     }
/*  94:    */     
/*  95:    */     protected void calculateChecksum(File file, int i) {}
/*  96:    */     
/*  97:    */     protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile)
/*  98:    */       throws IOException, GeneralSecurityException
/*  99:    */     {
/* 100:115 */       XOREncryptor.this.createEncryptionInfoEntry(dir);
/* 101:    */     }
/* 102:    */     
/* 103:    */     public void setNextRecordSize(int recordSize, boolean isPlain)
/* 104:    */     {
/* 105:120 */       if ((this.recordEnd > 0) && (!isPlain)) {
/* 106:122 */         invokeCipher((int)getPos(), true);
/* 107:    */       }
/* 108:124 */       this.recordStart = ((int)getTotalPos() + 4);
/* 109:125 */       this.recordEnd = (this.recordStart + recordSize);
/* 110:    */     }
/* 111:    */     
/* 112:    */     public void flush()
/* 113:    */       throws IOException
/* 114:    */     {
/* 115:130 */       setNextRecordSize(0, true);
/* 116:131 */       super.flush();
/* 117:    */     }
/* 118:    */     
/* 119:    */     protected int invokeCipher(int posInChunk, boolean doFinal)
/* 120:    */     {
/* 121:136 */       if (posInChunk == 0) {
/* 122:137 */         return 0;
/* 123:    */       }
/* 124:140 */       int start = Math.max(posInChunk - (this.recordEnd - this.recordStart), 0);
/* 125:    */       
/* 126:142 */       BitSet plainBytes = getPlainByteFlags();
/* 127:143 */       byte[] xorArray = XOREncryptor.this.getEncryptionInfo().getEncryptor().getSecretKey().getEncoded();
/* 128:144 */       byte[] chunk = getChunk();
/* 129:145 */       byte[] plain = plainBytes.isEmpty() ? null : (byte[])chunk.clone();
/* 130:    */       
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:158 */       int xorArrayIndex = this.recordEnd + (start - this.recordStart);
/* 143:160 */       for (int i = start; i < posInChunk; i++)
/* 144:    */       {
/* 145:161 */         byte value = chunk[i];
/* 146:162 */         value = (byte)(value ^ xorArray[(xorArrayIndex++ & 0xF)]);
/* 147:163 */         value = rotateLeft(value, 5);
/* 148:164 */         chunk[i] = value;
/* 149:    */       }
/* 150:167 */       for (int i = plainBytes.nextSetBit(start); (i >= 0) && (i < posInChunk); i = plainBytes.nextSetBit(i + 1)) {
/* 151:168 */         chunk[i] = plain[i];
/* 152:    */       }
/* 153:171 */       return posInChunk;
/* 154:    */     }
/* 155:    */     
/* 156:    */     private byte rotateLeft(byte bits, int shift)
/* 157:    */     {
/* 158:175 */       return (byte)((bits & 0xFF) << shift | (bits & 0xFF) >>> 8 - shift);
/* 159:    */     }
/* 160:    */   }
/* 161:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.xor.XOREncryptor
 * JD-Core Version:    0.7.0.1
 */