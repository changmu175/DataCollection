/*   1:    */ package org.apache.poi.poifs.crypt.cryptoapi;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.EOFException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.security.GeneralSecurityException;
/*   8:    */ import java.security.MessageDigest;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import javax.crypto.Cipher;
/*  11:    */ import javax.crypto.SecretKey;
/*  12:    */ import javax.crypto.spec.SecretKeySpec;
/*  13:    */ import org.apache.poi.EncryptedDocumentException;
/*  14:    */ import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
/*  15:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  16:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  17:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  18:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  19:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  20:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  21:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  22:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  23:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  24:    */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*  25:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  26:    */ import org.apache.poi.util.BitField;
/*  27:    */ import org.apache.poi.util.BitFieldFactory;
/*  28:    */ import org.apache.poi.util.BoundedInputStream;
/*  29:    */ import org.apache.poi.util.IOUtils;
/*  30:    */ import org.apache.poi.util.LittleEndian;
/*  31:    */ import org.apache.poi.util.LittleEndianInputStream;
/*  32:    */ import org.apache.poi.util.StringUtil;
/*  33:    */ 
/*  34:    */ public class CryptoAPIDecryptor
/*  35:    */   extends Decryptor
/*  36:    */   implements Cloneable
/*  37:    */ {
/*  38: 54 */   private long length = -1L;
/*  39: 55 */   private int chunkSize = -1;
/*  40:    */   
/*  41:    */   static class StreamDescriptorEntry
/*  42:    */   {
/*  43: 58 */     static BitField flagStream = BitFieldFactory.getInstance(1);
/*  44:    */     int streamOffset;
/*  45:    */     int streamSize;
/*  46:    */     int block;
/*  47:    */     int flags;
/*  48:    */     int reserved2;
/*  49:    */     String streamName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean verifyPassword(String password)
/*  53:    */   {
/*  54: 73 */     EncryptionVerifier ver = getEncryptionInfo().getVerifier();
/*  55: 74 */     SecretKey skey = generateSecretKey(password, ver);
/*  56:    */     try
/*  57:    */     {
/*  58: 76 */       Cipher cipher = initCipherForBlock(null, 0, getEncryptionInfo(), skey, 2);
/*  59: 77 */       byte[] encryptedVerifier = ver.getEncryptedVerifier();
/*  60: 78 */       byte[] verifier = new byte[encryptedVerifier.length];
/*  61: 79 */       cipher.update(encryptedVerifier, 0, encryptedVerifier.length, verifier);
/*  62: 80 */       setVerifier(verifier);
/*  63: 81 */       byte[] encryptedVerifierHash = ver.getEncryptedVerifierHash();
/*  64: 82 */       byte[] verifierHash = cipher.doFinal(encryptedVerifierHash);
/*  65: 83 */       HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  66: 84 */       MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/*  67: 85 */       byte[] calcVerifierHash = hashAlg.digest(verifier);
/*  68: 86 */       if (Arrays.equals(calcVerifierHash, verifierHash))
/*  69:    */       {
/*  70: 87 */         setSecretKey(skey);
/*  71: 88 */         return true;
/*  72:    */       }
/*  73:    */     }
/*  74:    */     catch (GeneralSecurityException e)
/*  75:    */     {
/*  76: 91 */       throw new EncryptedDocumentException(e);
/*  77:    */     }
/*  78: 93 */     return false;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Cipher initCipherForBlock(Cipher cipher, int block)
/*  82:    */     throws GeneralSecurityException
/*  83:    */   {
/*  84: 99 */     EncryptionInfo ei = getEncryptionInfo();
/*  85:100 */     SecretKey sk = getSecretKey();
/*  86:101 */     return initCipherForBlock(cipher, block, ei, sk, 2);
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfo encryptionInfo, SecretKey skey, int encryptMode)
/*  90:    */     throws GeneralSecurityException
/*  91:    */   {
/*  92:107 */     EncryptionVerifier ver = encryptionInfo.getVerifier();
/*  93:108 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  94:109 */     byte[] blockKey = new byte[4];
/*  95:110 */     LittleEndian.putUInt(blockKey, 0, block);
/*  96:111 */     MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/*  97:112 */     hashAlg.update(skey.getEncoded());
/*  98:113 */     byte[] encKey = hashAlg.digest(blockKey);
/*  99:114 */     EncryptionHeader header = encryptionInfo.getHeader();
/* 100:115 */     int keyBits = header.getKeySize();
/* 101:116 */     encKey = CryptoFunctions.getBlock0(encKey, keyBits / 8);
/* 102:117 */     if (keyBits == 40) {
/* 103:118 */       encKey = CryptoFunctions.getBlock0(encKey, 16);
/* 104:    */     }
/* 105:120 */     SecretKey key = new SecretKeySpec(encKey, skey.getAlgorithm());
/* 106:121 */     if (cipher == null) {
/* 107:122 */       cipher = CryptoFunctions.getCipher(key, header.getCipherAlgorithm(), null, null, encryptMode);
/* 108:    */     } else {
/* 109:124 */       cipher.init(encryptMode, key);
/* 110:    */     }
/* 111:126 */     return cipher;
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver)
/* 115:    */   {
/* 116:130 */     if (password.length() > 255) {
/* 117:131 */       password = password.substring(0, 255);
/* 118:    */     }
/* 119:133 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/* 120:134 */     MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/* 121:135 */     hashAlg.update(ver.getSalt());
/* 122:136 */     byte[] hash = hashAlg.digest(StringUtil.getToUnicodeLE(password));
/* 123:137 */     SecretKey skey = new SecretKeySpec(hash, ver.getCipherAlgorithm().jceId);
/* 124:138 */     return skey;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public ChunkedCipherInputStream getDataStream(DirectoryNode dir)
/* 128:    */     throws IOException, GeneralSecurityException
/* 129:    */   {
/* 130:144 */     throw new IOException("not supported");
/* 131:    */   }
/* 132:    */   
/* 133:    */   public ChunkedCipherInputStream getDataStream(InputStream stream, int size, int initialPos)
/* 134:    */     throws IOException, GeneralSecurityException
/* 135:    */   {
/* 136:150 */     return new CryptoAPICipherInputStream(stream, size, initialPos);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public POIFSFileSystem getSummaryEntries(DirectoryNode root, String encryptedStream)
/* 140:    */     throws IOException, GeneralSecurityException
/* 141:    */   {
/* 142:169 */     DocumentNode es = (DocumentNode)root.getEntry(encryptedStream);
/* 143:170 */     DocumentInputStream dis = root.createDocumentInputStream(es);
/* 144:171 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 145:172 */     IOUtils.copy(dis, bos);
/* 146:173 */     dis.close();
/* 147:174 */     CryptoAPIDocumentInputStream sbis = new CryptoAPIDocumentInputStream(this, bos.toByteArray());
/* 148:175 */     LittleEndianInputStream leis = new LittleEndianInputStream(sbis);
/* 149:176 */     POIFSFileSystem fsOut = null;
/* 150:    */     try
/* 151:    */     {
/* 152:178 */       int streamDescriptorArrayOffset = (int)leis.readUInt();
/* 153:179 */       leis.readUInt();
/* 154:180 */       long skipN = streamDescriptorArrayOffset - 8L;
/* 155:181 */       if (sbis.skip(skipN) < skipN) {
/* 156:182 */         throw new EOFException("buffer underrun");
/* 157:    */       }
/* 158:184 */       sbis.setBlock(0);
/* 159:185 */       int encryptedStreamDescriptorCount = (int)leis.readUInt();
/* 160:186 */       StreamDescriptorEntry[] entries = new StreamDescriptorEntry[encryptedStreamDescriptorCount];
/* 161:187 */       for (int i = 0; i < encryptedStreamDescriptorCount; i++)
/* 162:    */       {
/* 163:188 */         StreamDescriptorEntry entry = new StreamDescriptorEntry();
/* 164:189 */         entries[i] = entry;
/* 165:190 */         entry.streamOffset = ((int)leis.readUInt());
/* 166:191 */         entry.streamSize = ((int)leis.readUInt());
/* 167:192 */         entry.block = leis.readUShort();
/* 168:193 */         int nameSize = leis.readUByte();
/* 169:194 */         entry.flags = leis.readUByte();
/* 170:    */         
/* 171:196 */         entry.reserved2 = leis.readInt();
/* 172:197 */         entry.streamName = StringUtil.readUnicodeLE(leis, nameSize);
/* 173:198 */         leis.readShort();
/* 174:199 */         assert (entry.streamName.length() == nameSize);
/* 175:    */       }
/* 176:202 */       fsOut = new POIFSFileSystem();
/* 177:203 */       for (StreamDescriptorEntry entry : entries)
/* 178:    */       {
/* 179:204 */         sbis.seek(entry.streamOffset);
/* 180:205 */         sbis.setBlock(entry.block);
/* 181:206 */         InputStream is = new BoundedInputStream(sbis, entry.streamSize);
/* 182:207 */         fsOut.createDocument(is, entry.streamName);
/* 183:208 */         is.close();
/* 184:    */       }
/* 185:    */     }
/* 186:    */     catch (Exception e)
/* 187:    */     {
/* 188:211 */       IOUtils.closeQuietly(fsOut);
/* 189:212 */       if ((e instanceof GeneralSecurityException)) {
/* 190:213 */         throw ((GeneralSecurityException)e);
/* 191:    */       }
/* 192:214 */       if ((e instanceof IOException)) {
/* 193:215 */         throw ((IOException)e);
/* 194:    */       }
/* 195:217 */       throw new IOException("summary entries can't be read", e);
/* 196:    */     }
/* 197:    */     finally
/* 198:    */     {
/* 199:220 */       IOUtils.closeQuietly(leis);
/* 200:221 */       IOUtils.closeQuietly(sbis);
/* 201:    */     }
/* 202:223 */     return fsOut;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public long getLength()
/* 206:    */   {
/* 207:231 */     if (this.length == -1L) {
/* 208:232 */       throw new IllegalStateException("Decryptor.getDataStream() was not called");
/* 209:    */     }
/* 210:234 */     return this.length;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setChunkSize(int chunkSize)
/* 214:    */   {
/* 215:239 */     this.chunkSize = chunkSize;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public CryptoAPIDecryptor clone()
/* 219:    */     throws CloneNotSupportedException
/* 220:    */   {
/* 221:244 */     return (CryptoAPIDecryptor)super.clone();
/* 222:    */   }
/* 223:    */   
/* 224:    */   private class CryptoAPICipherInputStream
/* 225:    */     extends ChunkedCipherInputStream
/* 226:    */   {
/* 227:    */     protected Cipher initCipherForBlock(Cipher existing, int block)
/* 228:    */       throws GeneralSecurityException
/* 229:    */     {
/* 230:252 */       return CryptoAPIDecryptor.this.initCipherForBlock(existing, block);
/* 231:    */     }
/* 232:    */     
/* 233:    */     public CryptoAPICipherInputStream(InputStream stream, long size, int initialPos)
/* 234:    */       throws GeneralSecurityException
/* 235:    */     {
/* 236:257 */       super(size, CryptoAPIDecryptor.this.chunkSize, initialPos);
/* 237:    */     }
/* 238:    */   }
/* 239:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIDecryptor
 * JD-Core Version:    0.7.0.1
 */