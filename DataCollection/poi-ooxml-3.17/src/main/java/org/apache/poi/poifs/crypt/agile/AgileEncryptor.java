/*   1:    */ package org.apache.poi.poifs.crypt.agile;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.x2006.encryption.CTDataIntegrity;
/*   4:    */ import com.microsoft.schemas.office.x2006.encryption.CTEncryption;
/*   5:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyData;
/*   6:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor;
/*   7:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri;
/*   8:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri.Enum;
/*   9:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptors;
/*  10:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument;
/*  11:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument.Factory;
/*  12:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherAlgorithm.Enum;
/*  13:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherChaining;
/*  14:    */ import com.microsoft.schemas.office.x2006.encryption.STHashAlgorithm.Enum;
/*  15:    */ import com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor;
/*  16:    */ import com.microsoft.schemas.office.x2006.keyEncryptor.password.CTPasswordKeyEncryptor;
/*  17:    */ import java.io.ByteArrayOutputStream;
/*  18:    */ import java.io.File;
/*  19:    */ import java.io.FileInputStream;
/*  20:    */ import java.io.IOException;
/*  21:    */ import java.io.InputStream;
/*  22:    */ import java.io.OutputStream;
/*  23:    */ import java.security.GeneralSecurityException;
/*  24:    */ import java.security.MessageDigest;
/*  25:    */ import java.security.SecureRandom;
/*  26:    */ import java.security.cert.CertificateEncodingException;
/*  27:    */ import java.security.cert.X509Certificate;
/*  28:    */ import java.util.HashMap;
/*  29:    */ import java.util.Map;
/*  30:    */ import java.util.Random;
/*  31:    */ import javax.crypto.Cipher;
/*  32:    */ import javax.crypto.Mac;
/*  33:    */ import javax.crypto.SecretKey;
/*  34:    */ import javax.crypto.spec.SecretKeySpec;
/*  35:    */ import org.apache.poi.EncryptedDocumentException;
/*  36:    */ import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
/*  37:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  38:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  39:    */ import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
/*  40:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  41:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  42:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  43:    */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  44:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  45:    */ import org.apache.poi.util.LittleEndian;
/*  46:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  47:    */ import org.apache.xmlbeans.XmlOptions;
/*  48:    */ 
/*  49:    */ public class AgileEncryptor
/*  50:    */   extends Encryptor
/*  51:    */   implements Cloneable
/*  52:    */ {
/*  53:    */   private byte[] integritySalt;
/*  54:    */   private byte[] pwHash;
/*  55:    */   
/*  56:    */   public void confirmPassword(String password)
/*  57:    */   {
/*  58: 88 */     Random r = new SecureRandom();
/*  59: 89 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/*  60: 90 */     int blockSize = header.getBlockSize();
/*  61: 91 */     int keySize = header.getKeySize() / 8;
/*  62: 92 */     int hashSize = header.getHashAlgorithm().hashSize;
/*  63:    */     
/*  64: 94 */     byte[] newVerifierSalt = new byte[blockSize];
/*  65: 95 */     byte[] newVerifier = new byte[blockSize];
/*  66: 96 */     byte[] newKeySalt = new byte[blockSize];
/*  67: 97 */     byte[] newKeySpec = new byte[keySize];
/*  68: 98 */     byte[] newIntegritySalt = new byte[hashSize];
/*  69: 99 */     r.nextBytes(newVerifierSalt);
/*  70:100 */     r.nextBytes(newVerifier);
/*  71:101 */     r.nextBytes(newKeySalt);
/*  72:102 */     r.nextBytes(newKeySpec);
/*  73:103 */     r.nextBytes(newIntegritySalt);
/*  74:    */     
/*  75:105 */     confirmPassword(password, newKeySpec, newKeySalt, newVerifierSalt, newVerifier, newIntegritySalt);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt)
/*  79:    */   {
/*  80:110 */     AgileEncryptionVerifier ver = (AgileEncryptionVerifier)getEncryptionInfo().getVerifier();
/*  81:111 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/*  82:    */     
/*  83:113 */     ver.setSalt(verifierSalt);
/*  84:114 */     header.setKeySalt(keySalt);
/*  85:    */     
/*  86:116 */     int blockSize = header.getBlockSize();
/*  87:    */     
/*  88:118 */     this.pwHash = CryptoFunctions.hashPassword(password, ver.getHashAlgorithm(), verifierSalt, ver.getSpinCount());
/*  89:    */     
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */ 
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:133 */     byte[] encryptedVerifier = AgileDecryptor.hashInput(ver, this.pwHash, AgileDecryptor.kVerifierInputBlock, verifier, 1);
/* 104:134 */     ver.setEncryptedVerifier(encryptedVerifier);
/* 105:    */     
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:149 */     MessageDigest hashMD = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
/* 120:150 */     byte[] hashedVerifier = hashMD.digest(verifier);
/* 121:151 */     byte[] encryptedVerifierHash = AgileDecryptor.hashInput(ver, this.pwHash, AgileDecryptor.kHashedVerifierBlock, hashedVerifier, 1);
/* 122:152 */     ver.setEncryptedVerifierHash(encryptedVerifierHash);
/* 123:    */     
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:167 */     byte[] encryptedKey = AgileDecryptor.hashInput(ver, this.pwHash, AgileDecryptor.kCryptoKeyBlock, keySpec, 1);
/* 138:168 */     ver.setEncryptedKey(encryptedKey);
/* 139:    */     
/* 140:170 */     SecretKey secretKey = new SecretKeySpec(keySpec, header.getCipherAlgorithm().jceId);
/* 141:171 */     setSecretKey(secretKey);
/* 142:    */     
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:197 */     this.integritySalt = ((byte[])integritySalt.clone());
/* 168:    */     try
/* 169:    */     {
/* 170:200 */       byte[] vec = CryptoFunctions.generateIv(header.getHashAlgorithm(), header.getKeySalt(), AgileDecryptor.kIntegrityKeyBlock, header.getBlockSize());
/* 171:201 */       cipher = CryptoFunctions.getCipher(secretKey, header.getCipherAlgorithm(), header.getChainingMode(), vec, 1);
/* 172:202 */       byte[] hmacKey = CryptoFunctions.getBlock0(this.integritySalt, AgileDecryptor.getNextBlockSize(this.integritySalt.length, blockSize));
/* 173:203 */       byte[] encryptedHmacKey = cipher.doFinal(hmacKey);
/* 174:204 */       header.setEncryptedHmacKey(encryptedHmacKey);
/* 175:    */       
/* 176:206 */       cipher = Cipher.getInstance("RSA");
/* 177:207 */       for (AgileEncryptionVerifier.AgileCertificateEntry ace : ver.getCertificates())
/* 178:    */       {
/* 179:208 */         cipher.init(1, ace.x509.getPublicKey());
/* 180:209 */         ace.encryptedKey = cipher.doFinal(getSecretKey().getEncoded());
/* 181:210 */         Mac x509Hmac = CryptoFunctions.getMac(header.getHashAlgorithm());
/* 182:211 */         x509Hmac.init(getSecretKey());
/* 183:212 */         ace.certVerifier = x509Hmac.doFinal(ace.x509.getEncoded());
/* 184:    */       }
/* 185:    */     }
/* 186:    */     catch (GeneralSecurityException e)
/* 187:    */     {
/* 188:    */       Cipher cipher;
/* 189:215 */       throw new EncryptedDocumentException(e);
/* 190:    */     }
/* 191:    */   }
/* 192:    */   
/* 193:    */   public OutputStream getDataStream(DirectoryNode dir)
/* 194:    */     throws IOException, GeneralSecurityException
/* 195:    */   {
/* 196:223 */     AgileCipherOutputStream countStream = new AgileCipherOutputStream(dir);
/* 197:224 */     return countStream;
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected void updateIntegrityHMAC(File tmpFile, int oleStreamSize)
/* 201:    */     throws GeneralSecurityException, IOException
/* 202:    */   {
/* 203:240 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/* 204:241 */     int blockSize = header.getBlockSize();
/* 205:242 */     HashAlgorithm hashAlgo = header.getHashAlgorithm();
/* 206:243 */     Mac integrityMD = CryptoFunctions.getMac(hashAlgo);
/* 207:244 */     byte[] hmacKey = CryptoFunctions.getBlock0(this.integritySalt, AgileDecryptor.getNextBlockSize(this.integritySalt.length, blockSize));
/* 208:245 */     integrityMD.init(new SecretKeySpec(hmacKey, hashAlgo.jceHmacId));
/* 209:    */     
/* 210:247 */     byte[] buf = new byte[1024];
/* 211:248 */     LittleEndian.putLong(buf, 0, oleStreamSize);
/* 212:249 */     integrityMD.update(buf, 0, 8);
/* 213:    */     
/* 214:251 */     InputStream fis = new FileInputStream(tmpFile);
/* 215:    */     try
/* 216:    */     {
/* 217:    */       int readBytes;
/* 218:254 */       while ((readBytes = fis.read(buf)) != -1) {
/* 219:255 */         integrityMD.update(buf, 0, readBytes);
/* 220:    */       }
/* 221:    */     }
/* 222:    */     finally
/* 223:    */     {
/* 224:258 */       fis.close();
/* 225:    */     }
/* 226:261 */     byte[] hmacValue = integrityMD.doFinal();
/* 227:262 */     byte[] hmacValueFilled = CryptoFunctions.getBlock0(hmacValue, AgileDecryptor.getNextBlockSize(hmacValue.length, blockSize));
/* 228:    */     
/* 229:264 */     byte[] iv = CryptoFunctions.generateIv(header.getHashAlgorithm(), header.getKeySalt(), AgileDecryptor.kIntegrityValueBlock, blockSize);
/* 230:265 */     Cipher cipher = CryptoFunctions.getCipher(getSecretKey(), header.getCipherAlgorithm(), header.getChainingMode(), iv, 1);
/* 231:266 */     byte[] encryptedHmacValue = cipher.doFinal(hmacValueFilled);
/* 232:    */     
/* 233:268 */     header.setEncryptedHmacValue(encryptedHmacValue);
/* 234:    */   }
/* 235:    */   
/* 236:271 */   private final CTKeyEncryptor.Uri.Enum passwordUri = CTKeyEncryptor.Uri.HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_PASSWORD;
/* 237:273 */   private final CTKeyEncryptor.Uri.Enum certificateUri = CTKeyEncryptor.Uri.HTTP_SCHEMAS_MICROSOFT_COM_OFFICE_2006_KEY_ENCRYPTOR_CERTIFICATE;
/* 238:    */   
/* 239:    */   protected EncryptionDocument createEncryptionDocument()
/* 240:    */   {
/* 241:277 */     AgileEncryptionVerifier ver = (AgileEncryptionVerifier)getEncryptionInfo().getVerifier();
/* 242:278 */     AgileEncryptionHeader header = (AgileEncryptionHeader)getEncryptionInfo().getHeader();
/* 243:    */     
/* 244:280 */     EncryptionDocument ed = EncryptionDocument.Factory.newInstance();
/* 245:281 */     CTEncryption edRoot = ed.addNewEncryption();
/* 246:    */     
/* 247:283 */     CTKeyData keyData = edRoot.addNewKeyData();
/* 248:284 */     CTKeyEncryptors keyEncList = edRoot.addNewKeyEncryptors();
/* 249:285 */     CTKeyEncryptor keyEnc = keyEncList.addNewKeyEncryptor();
/* 250:286 */     keyEnc.setUri(this.passwordUri);
/* 251:287 */     CTPasswordKeyEncryptor keyPass = keyEnc.addNewEncryptedPasswordKey();
/* 252:    */     
/* 253:289 */     keyPass.setSpinCount(ver.getSpinCount());
/* 254:    */     
/* 255:291 */     keyData.setSaltSize(header.getBlockSize());
/* 256:292 */     keyPass.setSaltSize(ver.getBlockSize());
/* 257:    */     
/* 258:294 */     keyData.setBlockSize(header.getBlockSize());
/* 259:295 */     keyPass.setBlockSize(ver.getBlockSize());
/* 260:    */     
/* 261:297 */     keyData.setKeyBits(header.getKeySize());
/* 262:298 */     keyPass.setKeyBits(ver.getKeySize());
/* 263:    */     
/* 264:300 */     keyData.setHashSize(header.getHashAlgorithm().hashSize);
/* 265:301 */     keyPass.setHashSize(ver.getHashAlgorithm().hashSize);
/* 266:304 */     if (!header.getCipherAlgorithm().xmlId.equals(ver.getCipherAlgorithm().xmlId)) {
/* 267:305 */       throw new EncryptedDocumentException("Cipher algorithm of header and verifier have to match");
/* 268:    */     }
/* 269:307 */     STCipherAlgorithm.Enum xmlCipherAlgo = STCipherAlgorithm.Enum.forString(header.getCipherAlgorithm().xmlId);
/* 270:308 */     if (xmlCipherAlgo == null) {
/* 271:309 */       throw new EncryptedDocumentException("CipherAlgorithm " + header.getCipherAlgorithm() + " not supported.");
/* 272:    */     }
/* 273:311 */     keyData.setCipherAlgorithm(xmlCipherAlgo);
/* 274:312 */     keyPass.setCipherAlgorithm(xmlCipherAlgo);
/* 275:314 */     switch (2.$SwitchMap$org$apache$poi$poifs$crypt$ChainingMode[header.getChainingMode().ordinal()])
/* 276:    */     {
/* 277:    */     case 1: 
/* 278:316 */       keyData.setCipherChaining(STCipherChaining.CHAINING_MODE_CBC);
/* 279:317 */       keyPass.setCipherChaining(STCipherChaining.CHAINING_MODE_CBC);
/* 280:318 */       break;
/* 281:    */     case 2: 
/* 282:320 */       keyData.setCipherChaining(STCipherChaining.CHAINING_MODE_CFB);
/* 283:321 */       keyPass.setCipherChaining(STCipherChaining.CHAINING_MODE_CFB);
/* 284:322 */       break;
/* 285:    */     default: 
/* 286:324 */       throw new EncryptedDocumentException("ChainingMode " + header.getChainingMode() + " not supported.");
/* 287:    */     }
/* 288:327 */     keyData.setHashAlgorithm(mapHashAlgorithm(header.getHashAlgorithm()));
/* 289:328 */     keyPass.setHashAlgorithm(mapHashAlgorithm(ver.getHashAlgorithm()));
/* 290:    */     
/* 291:330 */     keyData.setSaltValue(header.getKeySalt());
/* 292:331 */     keyPass.setSaltValue(ver.getSalt());
/* 293:332 */     keyPass.setEncryptedVerifierHashInput(ver.getEncryptedVerifier());
/* 294:333 */     keyPass.setEncryptedVerifierHashValue(ver.getEncryptedVerifierHash());
/* 295:334 */     keyPass.setEncryptedKeyValue(ver.getEncryptedKey());
/* 296:    */     
/* 297:336 */     CTDataIntegrity hmacData = edRoot.addNewDataIntegrity();
/* 298:337 */     hmacData.setEncryptedHmacKey(header.getEncryptedHmacKey());
/* 299:338 */     hmacData.setEncryptedHmacValue(header.getEncryptedHmacValue());
/* 300:340 */     for (AgileEncryptionVerifier.AgileCertificateEntry ace : ver.getCertificates())
/* 301:    */     {
/* 302:341 */       keyEnc = keyEncList.addNewKeyEncryptor();
/* 303:342 */       keyEnc.setUri(this.certificateUri);
/* 304:343 */       CTCertificateKeyEncryptor certData = keyEnc.addNewEncryptedCertificateKey();
/* 305:    */       try
/* 306:    */       {
/* 307:345 */         certData.setX509Certificate(ace.x509.getEncoded());
/* 308:    */       }
/* 309:    */       catch (CertificateEncodingException e)
/* 310:    */       {
/* 311:347 */         throw new EncryptedDocumentException(e);
/* 312:    */       }
/* 313:349 */       certData.setEncryptedKeyValue(ace.encryptedKey);
/* 314:350 */       certData.setCertVerifier(ace.certVerifier);
/* 315:    */     }
/* 316:353 */     return ed;
/* 317:    */   }
/* 318:    */   
/* 319:    */   private static STHashAlgorithm.Enum mapHashAlgorithm(HashAlgorithm hashAlgo)
/* 320:    */   {
/* 321:357 */     STHashAlgorithm.Enum xmlHashAlgo = STHashAlgorithm.Enum.forString(hashAlgo.ecmaString);
/* 322:358 */     if (xmlHashAlgo == null) {
/* 323:359 */       throw new EncryptedDocumentException("HashAlgorithm " + hashAlgo + " not supported.");
/* 324:    */     }
/* 325:361 */     return xmlHashAlgo;
/* 326:    */   }
/* 327:    */   
/* 328:    */   protected void marshallEncryptionDocument(EncryptionDocument ed, LittleEndianByteArrayOutputStream os)
/* 329:    */   {
/* 330:365 */     XmlOptions xo = new XmlOptions();
/* 331:366 */     xo.setCharacterEncoding("UTF-8");
/* 332:367 */     Map<String, String> nsMap = new HashMap();
/* 333:368 */     nsMap.put(this.passwordUri.toString(), "p");
/* 334:369 */     nsMap.put(this.certificateUri.toString(), "c");
/* 335:370 */     xo.setUseDefaultNamespace();
/* 336:371 */     xo.setSaveSuggestedPrefixes(nsMap);
/* 337:372 */     xo.setSaveNamespacesFirst();
/* 338:373 */     xo.setSaveAggressiveNamespaces();
/* 339:    */     
/* 340:    */ 
/* 341:    */ 
/* 342:377 */     xo.setSaveNoXmlDecl();
/* 343:378 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 344:    */     try
/* 345:    */     {
/* 346:380 */       bos.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n".getBytes("UTF-8"));
/* 347:381 */       ed.save(bos, xo);
/* 348:382 */       bos.writeTo(os);
/* 349:    */     }
/* 350:    */     catch (IOException e)
/* 351:    */     {
/* 352:384 */       throw new EncryptedDocumentException("error marshalling encryption info document", e);
/* 353:    */     }
/* 354:    */   }
/* 355:    */   
/* 356:    */   protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile)
/* 357:    */     throws IOException, GeneralSecurityException
/* 358:    */   {
/* 359:390 */     DataSpaceMapUtils.addDefaultDataSpace(dir);
/* 360:    */     
/* 361:392 */     final EncryptionInfo info = getEncryptionInfo();
/* 362:    */     
/* 363:394 */     EncryptionRecord er = new EncryptionRecord()
/* 364:    */     {
/* 365:    */       public void write(LittleEndianByteArrayOutputStream bos)
/* 366:    */       {
/* 367:399 */         bos.writeShort(info.getVersionMajor());
/* 368:400 */         bos.writeShort(info.getVersionMinor());
/* 369:    */         
/* 370:402 */         bos.writeInt(info.getEncryptionFlags());
/* 371:    */         
/* 372:404 */         EncryptionDocument ed = AgileEncryptor.this.createEncryptionDocument();
/* 373:405 */         AgileEncryptor.this.marshallEncryptionDocument(ed, bos);
/* 374:    */       }
/* 375:408 */     };
/* 376:409 */     DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", er);
/* 377:    */   }
/* 378:    */   
/* 379:    */   private class AgileCipherOutputStream
/* 380:    */     extends ChunkedCipherOutputStream
/* 381:    */   {
/* 382:    */     public AgileCipherOutputStream(DirectoryNode dir)
/* 383:    */       throws IOException, GeneralSecurityException
/* 384:    */     {
/* 385:430 */       super(4096);
/* 386:    */     }
/* 387:    */     
/* 388:    */     protected Cipher initCipherForBlock(Cipher existing, int block, boolean lastChunk)
/* 389:    */       throws GeneralSecurityException
/* 390:    */     {
/* 391:436 */       return AgileDecryptor.initCipherForBlock(existing, block, lastChunk, AgileEncryptor.this.getEncryptionInfo(), AgileEncryptor.this.getSecretKey(), 1);
/* 392:    */     }
/* 393:    */     
/* 394:    */     protected void calculateChecksum(File fileOut, int oleStreamSize)
/* 395:    */       throws GeneralSecurityException, IOException
/* 396:    */     {
/* 397:443 */       AgileEncryptor.this.updateIntegrityHMAC(fileOut, oleStreamSize);
/* 398:    */     }
/* 399:    */     
/* 400:    */     protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile)
/* 401:    */       throws IOException, GeneralSecurityException
/* 402:    */     {
/* 403:449 */       AgileEncryptor.this.createEncryptionInfoEntry(dir, tmpFile);
/* 404:    */     }
/* 405:    */   }
/* 406:    */   
/* 407:    */   public AgileEncryptor clone()
/* 408:    */     throws CloneNotSupportedException
/* 409:    */   {
/* 410:455 */     AgileEncryptor other = (AgileEncryptor)super.clone();
/* 411:456 */     other.integritySalt = (this.integritySalt == null ? null : (byte[])this.integritySalt.clone());
/* 412:457 */     other.pwHash = (this.pwHash == null ? null : (byte[])this.pwHash.clone());
/* 413:458 */     return other;
/* 414:    */   }
/* 415:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.agile.AgileEncryptor
 * JD-Core Version:    0.7.0.1
 */