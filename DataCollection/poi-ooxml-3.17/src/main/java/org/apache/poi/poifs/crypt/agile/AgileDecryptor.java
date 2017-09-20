/*   1:    */ package org.apache.poi.poifs.crypt.agile;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import java.security.KeyPair;
/*   7:    */ import java.security.MessageDigest;
/*   8:    */ import java.security.cert.X509Certificate;
/*   9:    */ import java.security.spec.AlgorithmParameterSpec;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import javax.crypto.Cipher;
/*  12:    */ import javax.crypto.Mac;
/*  13:    */ import javax.crypto.SecretKey;
/*  14:    */ import javax.crypto.spec.IvParameterSpec;
/*  15:    */ import javax.crypto.spec.RC2ParameterSpec;
/*  16:    */ import javax.crypto.spec.SecretKeySpec;
/*  17:    */ import org.apache.poi.EncryptedDocumentException;
/*  18:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  19:    */ import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
/*  20:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  21:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  22:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  23:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  24:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  25:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  26:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  27:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  28:    */ import org.apache.poi.util.LittleEndian;
/*  29:    */ 
/*  30:    */ public class AgileDecryptor
/*  31:    */   extends Decryptor
/*  32:    */   implements Cloneable
/*  33:    */ {
/*  34: 60 */   private long _length = -1L;
/*  35: 69 */   static final byte[] kVerifierInputBlock = { -2, -89, -46, 118, 59, 75, -98, 121 };
/*  36: 72 */   static final byte[] kHashedVerifierBlock = { -41, -86, 15, 109, 48, 97, 52, 78 };
/*  37: 75 */   static final byte[] kCryptoKeyBlock = { 20, 110, 11, -25, -85, -84, -48, -42 };
/*  38: 78 */   static final byte[] kIntegrityKeyBlock = { 95, -78, -83, 1, 12, -71, -31, -10 };
/*  39: 81 */   static final byte[] kIntegrityValueBlock = { -96, 103, 127, 2, -78, 44, -124, 51 };
/*  40:    */   
/*  41:    */   public boolean verifyPassword(String password)
/*  42:    */     throws GeneralSecurityException
/*  43:    */   {
/*  44: 94 */     AgileEncryptionVerifier ver = (AgileEncryptionVerifier)getEncryptionInfo().getVerifier();
/*  45: 95 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/*  46:    */     
/*  47: 97 */     int blockSize = header.getBlockSize();
/*  48:    */     
/*  49: 99 */     byte[] pwHash = CryptoFunctions.hashPassword(password, ver.getHashAlgorithm(), ver.getSalt(), ver.getSpinCount());
/*  50:    */     
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:    */ 
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:114 */     byte[] verfierInputEnc = hashInput(ver, pwHash, kVerifierInputBlock, ver.getEncryptedVerifier(), 2);
/*  65:115 */     setVerifier(verfierInputEnc);
/*  66:116 */     MessageDigest hashMD = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
/*  67:117 */     byte[] verifierHash = hashMD.digest(verfierInputEnc);
/*  68:    */     
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:131 */     byte[] verifierHashDec = hashInput(ver, pwHash, kHashedVerifierBlock, ver.getEncryptedVerifierHash(), 2);
/*  82:132 */     verifierHashDec = CryptoFunctions.getBlock0(verifierHashDec, ver.getHashAlgorithm().hashSize);
/*  83:    */     
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:147 */     byte[] keyspec = hashInput(ver, pwHash, kCryptoKeyBlock, ver.getEncryptedKey(), 2);
/*  98:148 */     keyspec = CryptoFunctions.getBlock0(keyspec, header.getKeySize() / 8);
/*  99:149 */     SecretKeySpec secretKey = new SecretKeySpec(keyspec, header.getCipherAlgorithm().jceId);
/* 100:    */     
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:164 */     byte[] vec = CryptoFunctions.generateIv(header.getHashAlgorithm(), header.getKeySalt(), kIntegrityKeyBlock, blockSize);
/* 115:165 */     CipherAlgorithm cipherAlgo = header.getCipherAlgorithm();
/* 116:166 */     Cipher cipher = CryptoFunctions.getCipher(secretKey, cipherAlgo, header.getChainingMode(), vec, 2);
/* 117:167 */     byte[] hmacKey = cipher.doFinal(header.getEncryptedHmacKey());
/* 118:168 */     hmacKey = CryptoFunctions.getBlock0(hmacKey, header.getHashAlgorithm().hashSize);
/* 119:    */     
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:179 */     vec = CryptoFunctions.generateIv(header.getHashAlgorithm(), header.getKeySalt(), kIntegrityValueBlock, blockSize);
/* 130:180 */     cipher = CryptoFunctions.getCipher(secretKey, cipherAlgo, ver.getChainingMode(), vec, 2);
/* 131:181 */     byte[] hmacValue = cipher.doFinal(header.getEncryptedHmacValue());
/* 132:182 */     hmacValue = CryptoFunctions.getBlock0(hmacValue, header.getHashAlgorithm().hashSize);
/* 133:184 */     if (Arrays.equals(verifierHashDec, verifierHash))
/* 134:    */     {
/* 135:185 */       setSecretKey(secretKey);
/* 136:186 */       setIntegrityHmacKey(hmacKey);
/* 137:187 */       setIntegrityHmacValue(hmacValue);
/* 138:188 */       return true;
/* 139:    */     }
/* 140:190 */     return false;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public boolean verifyPassword(KeyPair keyPair, X509Certificate x509)
/* 144:    */     throws GeneralSecurityException
/* 145:    */   {
/* 146:206 */     AgileEncryptionVerifier ver = (AgileEncryptionVerifier)getEncryptionInfo().getVerifier();
/* 147:207 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/* 148:208 */     HashAlgorithm hashAlgo = header.getHashAlgorithm();
/* 149:209 */     CipherAlgorithm cipherAlgo = header.getCipherAlgorithm();
/* 150:210 */     int blockSize = header.getBlockSize();
/* 151:    */     
/* 152:212 */     AgileEncryptionVerifier.AgileCertificateEntry ace = null;
/* 153:213 */     for (AgileEncryptionVerifier.AgileCertificateEntry aceEntry : ver.getCertificates()) {
/* 154:214 */       if (x509.equals(aceEntry.x509))
/* 155:    */       {
/* 156:215 */         ace = aceEntry;
/* 157:216 */         break;
/* 158:    */       }
/* 159:    */     }
/* 160:219 */     if (ace == null) {
/* 161:220 */       return false;
/* 162:    */     }
/* 163:223 */     Cipher cipher = Cipher.getInstance("RSA");
/* 164:224 */     cipher.init(2, keyPair.getPrivate());
/* 165:225 */     byte[] keyspec = cipher.doFinal(ace.encryptedKey);
/* 166:226 */     SecretKeySpec secretKey = new SecretKeySpec(keyspec, ver.getCipherAlgorithm().jceId);
/* 167:    */     
/* 168:228 */     Mac x509Hmac = CryptoFunctions.getMac(hashAlgo);
/* 169:229 */     x509Hmac.init(secretKey);
/* 170:230 */     byte[] certVerifier = x509Hmac.doFinal(ace.x509.getEncoded());
/* 171:    */     
/* 172:232 */     byte[] vec = CryptoFunctions.generateIv(hashAlgo, header.getKeySalt(), kIntegrityKeyBlock, blockSize);
/* 173:233 */     cipher = CryptoFunctions.getCipher(secretKey, cipherAlgo, header.getChainingMode(), vec, 2);
/* 174:234 */     byte[] hmacKey = cipher.doFinal(header.getEncryptedHmacKey());
/* 175:235 */     hmacKey = CryptoFunctions.getBlock0(hmacKey, hashAlgo.hashSize);
/* 176:    */     
/* 177:237 */     vec = CryptoFunctions.generateIv(hashAlgo, header.getKeySalt(), kIntegrityValueBlock, blockSize);
/* 178:238 */     cipher = CryptoFunctions.getCipher(secretKey, cipherAlgo, header.getChainingMode(), vec, 2);
/* 179:239 */     byte[] hmacValue = cipher.doFinal(header.getEncryptedHmacValue());
/* 180:240 */     hmacValue = CryptoFunctions.getBlock0(hmacValue, hashAlgo.hashSize);
/* 181:243 */     if (Arrays.equals(ace.certVerifier, certVerifier))
/* 182:    */     {
/* 183:244 */       setSecretKey(secretKey);
/* 184:245 */       setIntegrityHmacKey(hmacKey);
/* 185:246 */       setIntegrityHmacValue(hmacValue);
/* 186:247 */       return true;
/* 187:    */     }
/* 188:249 */     return false;
/* 189:    */   }
/* 190:    */   
/* 191:    */   protected static int getNextBlockSize(int inputLen, int blockSize)
/* 192:    */   {
/* 193:255 */     for (int fillSize = blockSize; fillSize < inputLen; fillSize += blockSize) {}
/* 194:256 */     return fillSize;
/* 195:    */   }
/* 196:    */   
/* 197:    */   static byte[] hashInput(AgileEncryptionVerifier ver, byte[] pwHash, byte[] blockKey, byte[] inputKey, int cipherMode)
/* 198:    */   {
/* 199:260 */     CipherAlgorithm cipherAlgo = ver.getCipherAlgorithm();
/* 200:261 */     ChainingMode chainMode = ver.getChainingMode();
/* 201:262 */     int keySize = ver.getKeySize() / 8;
/* 202:263 */     int blockSize = ver.getBlockSize();
/* 203:264 */     HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/* 204:    */     
/* 205:266 */     byte[] intermedKey = CryptoFunctions.generateKey(pwHash, hashAlgo, blockKey, keySize);
/* 206:267 */     SecretKey skey = new SecretKeySpec(intermedKey, cipherAlgo.jceId);
/* 207:268 */     byte[] iv = CryptoFunctions.generateIv(hashAlgo, ver.getSalt(), null, blockSize);
/* 208:269 */     Cipher cipher = CryptoFunctions.getCipher(skey, cipherAlgo, chainMode, iv, cipherMode);
/* 209:    */     try
/* 210:    */     {
/* 211:273 */       inputKey = CryptoFunctions.getBlock0(inputKey, getNextBlockSize(inputKey.length, blockSize));
/* 212:274 */       return cipher.doFinal(inputKey);
/* 213:    */     }
/* 214:    */     catch (GeneralSecurityException e)
/* 215:    */     {
/* 216:277 */       throw new EncryptedDocumentException(e);
/* 217:    */     }
/* 218:    */   }
/* 219:    */   
/* 220:    */   public InputStream getDataStream(DirectoryNode dir)
/* 221:    */     throws IOException, GeneralSecurityException
/* 222:    */   {
/* 223:283 */     DocumentInputStream dis = dir.createDocumentInputStream("EncryptedPackage");
/* 224:284 */     this._length = dis.readLong();
/* 225:285 */     return new AgileCipherInputStream(dis, this._length);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public long getLength()
/* 229:    */   {
/* 230:290 */     if (this._length == -1L) {
/* 231:291 */       throw new IllegalStateException("EcmaDecryptor.getDataStream() was not called");
/* 232:    */     }
/* 233:293 */     return this._length;
/* 234:    */   }
/* 235:    */   
/* 236:    */   protected static Cipher initCipherForBlock(Cipher existing, int block, boolean lastChunk, EncryptionInfo encryptionInfo, SecretKey skey, int encryptionMode)
/* 237:    */     throws GeneralSecurityException
/* 238:    */   {
/* 239:299 */     EncryptionHeader header = encryptionInfo.getHeader();
/* 240:300 */     String padding = lastChunk ? "PKCS5Padding" : "NoPadding";
/* 241:301 */     if ((existing == null) || (!existing.getAlgorithm().endsWith(padding))) {
/* 242:302 */       existing = CryptoFunctions.getCipher(skey, header.getCipherAlgorithm(), header.getChainingMode(), header.getKeySalt(), encryptionMode, padding);
/* 243:    */     }
/* 244:305 */     byte[] blockKey = new byte[4];
/* 245:306 */     LittleEndian.putInt(blockKey, 0, block);
/* 246:307 */     byte[] iv = CryptoFunctions.generateIv(header.getHashAlgorithm(), header.getKeySalt(), blockKey, header.getBlockSize());
/* 247:    */     AlgorithmParameterSpec aps;
/* 248:    */     AlgorithmParameterSpec aps;
/* 249:310 */     if (header.getCipherAlgorithm() == CipherAlgorithm.rc2) {
/* 250:311 */       aps = new RC2ParameterSpec(skey.getEncoded().length * 8, iv);
/* 251:    */     } else {
/* 252:313 */       aps = new IvParameterSpec(iv);
/* 253:    */     }
/* 254:316 */     existing.init(encryptionMode, skey, aps);
/* 255:    */     
/* 256:318 */     return existing;
/* 257:    */   }
/* 258:    */   
/* 259:    */   private class AgileCipherInputStream
/* 260:    */     extends ChunkedCipherInputStream
/* 261:    */   {
/* 262:    */     public AgileCipherInputStream(DocumentInputStream stream, long size)
/* 263:    */       throws GeneralSecurityException
/* 264:    */     {
/* 265:339 */       super(size, 4096);
/* 266:    */     }
/* 267:    */     
/* 268:    */     protected Cipher initCipherForBlock(Cipher cipher, int block)
/* 269:    */       throws GeneralSecurityException
/* 270:    */     {
/* 271:348 */       return AgileDecryptor.initCipherForBlock(cipher, block, false, AgileDecryptor.this.getEncryptionInfo(), AgileDecryptor.this.getSecretKey(), 2);
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   public AgileDecryptor clone()
/* 276:    */     throws CloneNotSupportedException
/* 277:    */   {
/* 278:354 */     return (AgileDecryptor)super.clone();
/* 279:    */   }
/* 280:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.agile.AgileDecryptor
 * JD-Core Version:    0.7.0.1
 */