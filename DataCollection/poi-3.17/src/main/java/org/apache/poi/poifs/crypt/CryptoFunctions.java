/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import java.security.DigestException;
/*   5:    */ import java.security.GeneralSecurityException;
/*   6:    */ import java.security.Key;
/*   7:    */ import java.security.MessageDigest;
/*   8:    */ import java.security.Provider;
/*   9:    */ import java.security.Security;
/*  10:    */ import java.security.spec.AlgorithmParameterSpec;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Locale;
/*  13:    */ import javax.crypto.Cipher;
/*  14:    */ import javax.crypto.Mac;
/*  15:    */ import javax.crypto.SecretKey;
/*  16:    */ import javax.crypto.spec.IvParameterSpec;
/*  17:    */ import javax.crypto.spec.RC2ParameterSpec;
/*  18:    */ import org.apache.poi.EncryptedDocumentException;
/*  19:    */ import org.apache.poi.util.Internal;
/*  20:    */ import org.apache.poi.util.LittleEndian;
/*  21:    */ import org.apache.poi.util.StringUtil;
/*  22:    */ 
/*  23:    */ @Internal
/*  24:    */ public class CryptoFunctions
/*  25:    */ {
/*  26:    */   public static byte[] hashPassword(String password, HashAlgorithm hashAlgorithm, byte[] salt, int spinCount)
/*  27:    */   {
/*  28: 83 */     return hashPassword(password, hashAlgorithm, salt, spinCount, true);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static byte[] hashPassword(String password, HashAlgorithm hashAlgorithm, byte[] salt, int spinCount, boolean iteratorFirst)
/*  32:    */   {
/*  33:101 */     if (password == null) {
/*  34:102 */       password = "VelvetSweatshop";
/*  35:    */     }
/*  36:105 */     MessageDigest hashAlg = getMessageDigest(hashAlgorithm);
/*  37:    */     
/*  38:107 */     hashAlg.update(salt);
/*  39:108 */     byte[] hash = hashAlg.digest(StringUtil.getToUnicodeLE(password));
/*  40:109 */     byte[] iterator = new byte[4];
/*  41:    */     
/*  42:111 */     byte[] first = iteratorFirst ? iterator : hash;
/*  43:112 */     byte[] second = iteratorFirst ? hash : iterator;
/*  44:    */     try
/*  45:    */     {
/*  46:115 */       for (int i = 0; i < spinCount; i++)
/*  47:    */       {
/*  48:116 */         LittleEndian.putInt(iterator, 0, i);
/*  49:117 */         hashAlg.reset();
/*  50:118 */         hashAlg.update(first);
/*  51:119 */         hashAlg.update(second);
/*  52:120 */         hashAlg.digest(hash, 0, hash.length);
/*  53:    */       }
/*  54:    */     }
/*  55:    */     catch (DigestException e)
/*  56:    */     {
/*  57:123 */       throw new EncryptedDocumentException("error in password hashing");
/*  58:    */     }
/*  59:126 */     return hash;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static byte[] generateIv(HashAlgorithm hashAlgorithm, byte[] salt, byte[] blockKey, int blockSize)
/*  63:    */   {
/*  64:147 */     byte[] iv = salt;
/*  65:148 */     if (blockKey != null)
/*  66:    */     {
/*  67:149 */       MessageDigest hashAlgo = getMessageDigest(hashAlgorithm);
/*  68:150 */       hashAlgo.update(salt);
/*  69:151 */       iv = hashAlgo.digest(blockKey);
/*  70:    */     }
/*  71:153 */     return getBlock36(iv, blockSize);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public static byte[] generateKey(byte[] passwordHash, HashAlgorithm hashAlgorithm, byte[] blockKey, int keySize)
/*  75:    */   {
/*  76:178 */     MessageDigest hashAlgo = getMessageDigest(hashAlgorithm);
/*  77:179 */     hashAlgo.update(passwordHash);
/*  78:180 */     byte[] key = hashAlgo.digest(blockKey);
/*  79:181 */     return getBlock36(key, keySize);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static Cipher getCipher(SecretKey key, CipherAlgorithm cipherAlgorithm, ChainingMode chain, byte[] vec, int cipherMode)
/*  83:    */   {
/*  84:200 */     return getCipher(key, cipherAlgorithm, chain, vec, cipherMode, null);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static Cipher getCipher(Key key, CipherAlgorithm cipherAlgorithm, ChainingMode chain, byte[] vec, int cipherMode, String padding)
/*  88:    */   {
/*  89:220 */     int keySizeInBytes = key.getEncoded().length;
/*  90:221 */     if (padding == null) {
/*  91:221 */       padding = "NoPadding";
/*  92:    */     }
/*  93:    */     try
/*  94:    */     {
/*  95:225 */       if (Cipher.getMaxAllowedKeyLength(cipherAlgorithm.jceId) < keySizeInBytes * 8) {
/*  96:226 */         throw new EncryptedDocumentException("Export Restrictions in place - please install JCE Unlimited Strength Jurisdiction Policy files");
/*  97:    */       }
/*  98:    */       Cipher cipher;
/*  99:    */       Cipher cipher;
/* 100:230 */       if (cipherAlgorithm == CipherAlgorithm.rc4)
/* 101:    */       {
/* 102:231 */         cipher = Cipher.getInstance(cipherAlgorithm.jceId);
/* 103:    */       }
/* 104:    */       else
/* 105:    */       {
/* 106:    */         Cipher cipher;
/* 107:232 */         if (cipherAlgorithm.needsBouncyCastle)
/* 108:    */         {
/* 109:233 */           registerBouncyCastle();
/* 110:234 */           cipher = Cipher.getInstance(cipherAlgorithm.jceId + "/" + chain.jceId + "/" + padding, "BC");
/* 111:    */         }
/* 112:    */         else
/* 113:    */         {
/* 114:236 */           cipher = Cipher.getInstance(cipherAlgorithm.jceId + "/" + chain.jceId + "/" + padding);
/* 115:    */         }
/* 116:    */       }
/* 117:239 */       if (vec == null)
/* 118:    */       {
/* 119:240 */         cipher.init(cipherMode, key);
/* 120:    */       }
/* 121:    */       else
/* 122:    */       {
/* 123:    */         AlgorithmParameterSpec aps;
/* 124:    */         AlgorithmParameterSpec aps;
/* 125:243 */         if (cipherAlgorithm == CipherAlgorithm.rc2) {
/* 126:244 */           aps = new RC2ParameterSpec(key.getEncoded().length * 8, vec);
/* 127:    */         } else {
/* 128:246 */           aps = new IvParameterSpec(vec);
/* 129:    */         }
/* 130:248 */         cipher.init(cipherMode, key, aps);
/* 131:    */       }
/* 132:250 */       return cipher;
/* 133:    */     }
/* 134:    */     catch (GeneralSecurityException e)
/* 135:    */     {
/* 136:252 */       throw new EncryptedDocumentException(e);
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   private static byte[] getBlock36(byte[] hash, int size)
/* 141:    */   {
/* 142:265 */     return getBlockX(hash, size, (byte)54);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static byte[] getBlock0(byte[] hash, int size)
/* 146:    */   {
/* 147:277 */     return getBlockX(hash, size, (byte)0);
/* 148:    */   }
/* 149:    */   
/* 150:    */   private static byte[] getBlockX(byte[] hash, int size, byte fill)
/* 151:    */   {
/* 152:281 */     if (hash.length == size) {
/* 153:281 */       return hash;
/* 154:    */     }
/* 155:283 */     byte[] result = new byte[size];
/* 156:284 */     Arrays.fill(result, fill);
/* 157:285 */     System.arraycopy(hash, 0, result, 0, Math.min(result.length, hash.length));
/* 158:286 */     return result;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static MessageDigest getMessageDigest(HashAlgorithm hashAlgorithm)
/* 162:    */   {
/* 163:    */     try
/* 164:    */     {
/* 165:291 */       if (hashAlgorithm.needsBouncyCastle)
/* 166:    */       {
/* 167:292 */         registerBouncyCastle();
/* 168:293 */         return MessageDigest.getInstance(hashAlgorithm.jceId, "BC");
/* 169:    */       }
/* 170:295 */       return MessageDigest.getInstance(hashAlgorithm.jceId);
/* 171:    */     }
/* 172:    */     catch (GeneralSecurityException e)
/* 173:    */     {
/* 174:298 */       throw new EncryptedDocumentException("hash algo not supported", e);
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static Mac getMac(HashAlgorithm hashAlgorithm)
/* 179:    */   {
/* 180:    */     try
/* 181:    */     {
/* 182:304 */       if (hashAlgorithm.needsBouncyCastle)
/* 183:    */       {
/* 184:305 */         registerBouncyCastle();
/* 185:306 */         return Mac.getInstance(hashAlgorithm.jceHmacId, "BC");
/* 186:    */       }
/* 187:308 */       return Mac.getInstance(hashAlgorithm.jceHmacId);
/* 188:    */     }
/* 189:    */     catch (GeneralSecurityException e)
/* 190:    */     {
/* 191:311 */       throw new EncryptedDocumentException("hmac algo not supported", e);
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public static void registerBouncyCastle()
/* 196:    */   {
/* 197:317 */     if (Security.getProvider("BC") != null) {
/* 198:318 */       return;
/* 199:    */     }
/* 200:    */     try
/* 201:    */     {
/* 202:322 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 203:323 */       String bcProviderName = "org.bouncycastle.jce.provider.BouncyCastleProvider";
/* 204:324 */       Class<Provider> clazz = cl.loadClass(bcProviderName);
/* 205:325 */       Security.addProvider((Provider)clazz.newInstance());
/* 206:    */     }
/* 207:    */     catch (Exception e)
/* 208:    */     {
/* 209:327 */       throw new EncryptedDocumentException("Only the BouncyCastle provider supports your encryption settings - please add it to the classpath.", e);
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:331 */   private static final int[] INITIAL_CODE_ARRAY = { 57840, 7439, 52380, 33984, 4364, 3600, 61902, 12606, 6258, 57657, 54287, 34041, 10252, 43370, 20163 };
/* 214:337 */   private static final byte[] PAD_ARRAY = { -69, -1, -1, -70, -1, -1, -71, -128, 0, -66, 15, 0, -65, 15, 0 };
/* 215:343 */   private static final int[][] ENCRYPTION_MATRIX = { { 44796, 19929, 39858, 10053, 20106, 40212, 10761 }, { 31585, 63170, 64933, 60267, 50935, 40399, 11199 }, { 17763, 35526, 1453, 2906, 5812, 11624, 23248 }, { 885, 1770, 3540, 7080, 14160, 28320, 56640 }, { 55369, 41139, 20807, 41614, 21821, 43642, 17621 }, { 28485, 56970, 44341, 19019, 38038, 14605, 29210 }, { 60195, 50791, 40175, 10751, 21502, 43004, 24537 }, { 18387, 36774, 3949, 7898, 15796, 31592, 63184 }, { 47201, 24803, 49606, 37805, 14203, 28406, 56812 }, { 17824, 35648, 1697, 3394, 6788, 13576, 27152 }, { 43601, 17539, 35078, 557, 1114, 2228, 4456 }, { 30388, 60776, 51953, 34243, 7079, 14158, 28316 }, { 14128, 28256, 56512, 43425, 17251, 34502, 7597 }, { 13105, 26210, 52420, 35241, 883, 1766, 3532 }, { 4129, 8258, 16516, 33032, 4657, 9314, 18628 } };
/* 216:    */   
/* 217:    */   public static int createXorVerifier1(String password)
/* 218:    */   {
/* 219:372 */     byte[] arrByteChars = toAnsiPassword(password);
/* 220:    */     
/* 221:    */ 
/* 222:375 */     short verifier = 0;
/* 223:377 */     if (!"".equals(password))
/* 224:    */     {
/* 225:379 */       for (int i = arrByteChars.length - 1; i >= 0; i--)
/* 226:    */       {
/* 227:381 */         verifier = rotateLeftBase15Bit(verifier);
/* 228:382 */         verifier = (short)(verifier ^ arrByteChars[i]);
/* 229:    */       }
/* 230:387 */       verifier = rotateLeftBase15Bit(verifier);
/* 231:388 */       verifier = (short)(verifier ^ arrByteChars.length);
/* 232:    */       
/* 233:    */ 
/* 234:391 */       verifier = (short)(verifier ^ 0xCE4B);
/* 235:    */     }
/* 236:394 */     return verifier & 0xFFFF;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static int createXorVerifier2(String password)
/* 240:    */   {
/* 241:411 */     byte[] generatedKey = new byte[4];
/* 242:    */     
/* 243:    */ 
/* 244:414 */     int maxPasswordLength = 15;
/* 245:416 */     if (!"".equals(password))
/* 246:    */     {
/* 247:418 */       password = password.substring(0, Math.min(password.length(), 15));
/* 248:    */       
/* 249:420 */       byte[] arrByteChars = toAnsiPassword(password);
/* 250:    */       
/* 251:    */ 
/* 252:    */ 
/* 253:    */ 
/* 254:425 */       int highOrderWord = INITIAL_CODE_ARRAY[(arrByteChars.length - 1)];
/* 255:431 */       for (int i = 0; i < arrByteChars.length; i++)
/* 256:    */       {
/* 257:432 */         int tmp = 15 - arrByteChars.length + i;
/* 258:433 */         for (int intBit = 0; intBit < 7; intBit++) {
/* 259:434 */           if ((arrByteChars[i] & 1 << intBit) != 0) {
/* 260:435 */             highOrderWord ^= ENCRYPTION_MATRIX[tmp][intBit];
/* 261:    */           }
/* 262:    */         }
/* 263:    */       }
/* 264:441 */       int verifier = createXorVerifier1(password);
/* 265:    */       
/* 266:    */ 
/* 267:    */ 
/* 268:    */ 
/* 269:446 */       LittleEndian.putShort(generatedKey, 0, (short)verifier);
/* 270:447 */       LittleEndian.putShort(generatedKey, 2, (short)highOrderWord);
/* 271:    */     }
/* 272:450 */     return LittleEndian.getInt(generatedKey);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static String xorHashPassword(String password)
/* 276:    */   {
/* 277:457 */     int hashedPassword = createXorVerifier2(password);
/* 278:458 */     return String.format(Locale.ROOT, "%1$08X", new Object[] { Integer.valueOf(hashedPassword) });
/* 279:    */   }
/* 280:    */   
/* 281:    */   public static String xorHashPasswordReversed(String password)
/* 282:    */   {
/* 283:466 */     int hashedPassword = createXorVerifier2(password);
/* 284:    */     
/* 285:468 */     return String.format(Locale.ROOT, "%1$02X%2$02X%3$02X%4$02X", new Object[] { Integer.valueOf(hashedPassword >>> 0 & 0xFF), Integer.valueOf(hashedPassword >>> 8 & 0xFF), Integer.valueOf(hashedPassword >>> 16 & 0xFF), Integer.valueOf(hashedPassword >>> 24 & 0xFF) });
/* 286:    */   }
/* 287:    */   
/* 288:    */   public static int createXorKey1(String password)
/* 289:    */   {
/* 290:488 */     return createXorVerifier2(password) >>> 16;
/* 291:    */   }
/* 292:    */   
/* 293:    */   public static byte[] createXorArray1(String password)
/* 294:    */   {
/* 295:501 */     if (password.length() > 15) {
/* 296:502 */       password = password.substring(0, 15);
/* 297:    */     }
/* 298:504 */     byte[] passBytes = password.getBytes(Charset.forName("ASCII"));
/* 299:    */     
/* 300:    */ 
/* 301:    */ 
/* 302:508 */     byte[] obfuscationArray = new byte[16];
/* 303:509 */     System.arraycopy(passBytes, 0, obfuscationArray, 0, passBytes.length);
/* 304:510 */     System.arraycopy(PAD_ARRAY, 0, obfuscationArray, passBytes.length, PAD_ARRAY.length - passBytes.length + 1);
/* 305:    */     
/* 306:512 */     int xorKey = createXorKey1(password);
/* 307:    */     
/* 308:    */ 
/* 309:515 */     int nRotateSize = 2;
/* 310:    */     
/* 311:517 */     byte[] baseKeyLE = { (byte)(xorKey & 0xFF), (byte)(xorKey >>> 8 & 0xFF) };
/* 312:518 */     for (int i = 0; i < obfuscationArray.length; i++)
/* 313:    */     {
/* 314:519 */       int tmp107_105 = i; byte[] tmp107_104 = obfuscationArray;tmp107_104[tmp107_105] = ((byte)(tmp107_104[tmp107_105] ^ baseKeyLE[(i & 0x1)]));
/* 315:520 */       obfuscationArray[i] = rotateLeft(obfuscationArray[i], nRotateSize);
/* 316:    */     }
/* 317:523 */     return obfuscationArray;
/* 318:    */   }
/* 319:    */   
/* 320:    */   private static byte[] toAnsiPassword(String password)
/* 321:    */   {
/* 322:540 */     byte[] arrByteChars = new byte[password.length()];
/* 323:542 */     for (int i = 0; i < password.length(); i++)
/* 324:    */     {
/* 325:543 */       int intTemp = password.charAt(i);
/* 326:544 */       byte lowByte = (byte)(intTemp & 0xFF);
/* 327:545 */       byte highByte = (byte)(intTemp >>> 8 & 0xFF);
/* 328:546 */       arrByteChars[i] = (lowByte != 0 ? lowByte : highByte);
/* 329:    */     }
/* 330:549 */     return arrByteChars;
/* 331:    */   }
/* 332:    */   
/* 333:    */   private static byte rotateLeft(byte bits, int shift)
/* 334:    */   {
/* 335:553 */     return (byte)((bits & 0xFF) << shift | (bits & 0xFF) >>> 8 - shift);
/* 336:    */   }
/* 337:    */   
/* 338:    */   private static short rotateLeftBase15Bit(short verifier)
/* 339:    */   {
/* 340:564 */     short intermediate1 = (short)((verifier & 0x4000) == 0 ? 0 : 1);
/* 341:    */     
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:569 */     short intermediate2 = (short)(verifier << 1 & 0x7FFF);
/* 346:    */     
/* 347:    */ 
/* 348:    */ 
/* 349:573 */     short intermediate3 = (short)(intermediate1 | intermediate2);
/* 350:574 */     return intermediate3;
/* 351:    */   }
/* 352:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.CryptoFunctions
 * JD-Core Version:    0.7.0.1
 */