/*   1:    */ package org.apache.poi.poifs.crypt.cryptoapi;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.security.GeneralSecurityException;
/*   8:    */ import java.security.MessageDigest;
/*   9:    */ import java.security.SecureRandom;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.Random;
/*  13:    */ import javax.crypto.Cipher;
/*  14:    */ import javax.crypto.SecretKey;
/*  15:    */ import org.apache.poi.EncryptedDocumentException;
/*  16:    */ import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
/*  17:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  18:    */ import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
/*  19:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  20:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  21:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  22:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  23:    */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*  24:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  25:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  26:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  27:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  28:    */ import org.apache.poi.util.BitField;
/*  29:    */ import org.apache.poi.util.IOUtils;
/*  30:    */ import org.apache.poi.util.LittleEndian;
/*  31:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  32:    */ import org.apache.poi.util.StringUtil;
/*  33:    */ 
/*  34:    */ public class CryptoAPIEncryptor
/*  35:    */   extends Encryptor
/*  36:    */   implements Cloneable
/*  37:    */ {
/*  38: 54 */   private int chunkSize = 512;
/*  39:    */   
/*  40:    */   public void confirmPassword(String password)
/*  41:    */   {
/*  42: 61 */     Random r = new SecureRandom();
/*  43: 62 */     byte[] salt = new byte[16];
/*  44: 63 */     byte[] verifier = new byte[16];
/*  45: 64 */     r.nextBytes(salt);
/*  46: 65 */     r.nextBytes(verifier);
/*  47: 66 */     confirmPassword(password, null, null, verifier, salt, null);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt)
/*  51:    */   {
/*  52: 73 */     assert ((verifier != null) && (verifierSalt != null));
/*  53: 74 */     CryptoAPIEncryptionVerifier ver = (CryptoAPIEncryptionVerifier)getEncryptionInfo().getVerifier();
/*  54: 75 */     ver.setSalt(verifierSalt);
/*  55: 76 */     SecretKey skey = CryptoAPIDecryptor.generateSecretKey(password, ver);
/*  56: 77 */     setSecretKey(skey);
/*  57:    */     try
/*  58:    */     {
/*  59: 79 */       Cipher cipher = initCipherForBlock(null, 0);
/*  60: 80 */       byte[] encryptedVerifier = new byte[verifier.length];
/*  61: 81 */       cipher.update(verifier, 0, verifier.length, encryptedVerifier);
/*  62: 82 */       ver.setEncryptedVerifier(encryptedVerifier);
/*  63: 83 */       HashAlgorithm hashAlgo = ver.getHashAlgorithm();
/*  64: 84 */       MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
/*  65: 85 */       byte[] calcVerifierHash = hashAlg.digest(verifier);
/*  66: 86 */       byte[] encryptedVerifierHash = cipher.doFinal(calcVerifierHash);
/*  67: 87 */       ver.setEncryptedVerifierHash(encryptedVerifierHash);
/*  68:    */     }
/*  69:    */     catch (GeneralSecurityException e)
/*  70:    */     {
/*  71: 89 */       throw new EncryptedDocumentException("Password confirmation failed", e);
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Cipher initCipherForBlock(Cipher cipher, int block)
/*  76:    */     throws GeneralSecurityException
/*  77:    */   {
/*  78:103 */     return CryptoAPIDecryptor.initCipherForBlock(cipher, block, getEncryptionInfo(), getSecretKey(), 1);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public ChunkedCipherOutputStream getDataStream(DirectoryNode dir)
/*  82:    */     throws IOException, GeneralSecurityException
/*  83:    */   {
/*  84:109 */     throw new IOException("not supported");
/*  85:    */   }
/*  86:    */   
/*  87:    */   public CryptoAPICipherOutputStream getDataStream(OutputStream stream, int initialOffset)
/*  88:    */     throws IOException, GeneralSecurityException
/*  89:    */   {
/*  90:115 */     return new CryptoAPICipherOutputStream(stream);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setSummaryEntries(DirectoryNode dir, String encryptedStream, NPOIFSFileSystem entries)
/*  94:    */     throws IOException, GeneralSecurityException
/*  95:    */   {
/*  96:127 */     CryptoAPIDocumentOutputStream bos = new CryptoAPIDocumentOutputStream(this);
/*  97:128 */     byte[] buf = new byte[8];
/*  98:    */     
/*  99:130 */     bos.write(buf, 0, 8);
/* 100:131 */     List<CryptoAPIDecryptor.StreamDescriptorEntry> descList = new ArrayList();
/* 101:    */     
/* 102:133 */     int block = 0;
/* 103:134 */     for (Entry entry : entries.getRoot()) {
/* 104:135 */       if (!entry.isDirectoryEntry())
/* 105:    */       {
/* 106:138 */         CryptoAPIDecryptor.StreamDescriptorEntry descEntry = new CryptoAPIDecryptor.StreamDescriptorEntry();
/* 107:139 */         descEntry.block = block;
/* 108:140 */         descEntry.streamOffset = bos.size();
/* 109:141 */         descEntry.streamName = entry.getName();
/* 110:142 */         descEntry.flags = CryptoAPIDecryptor.StreamDescriptorEntry.flagStream.setValue(0, 1);
/* 111:143 */         descEntry.reserved2 = 0;
/* 112:    */         
/* 113:145 */         bos.setBlock(block);
/* 114:146 */         DocumentInputStream dis = dir.createDocumentInputStream(entry);
/* 115:147 */         IOUtils.copy(dis, bos);
/* 116:148 */         dis.close();
/* 117:    */         
/* 118:150 */         descEntry.streamSize = (bos.size() - descEntry.streamOffset);
/* 119:151 */         descList.add(descEntry);
/* 120:    */         
/* 121:153 */         block++;
/* 122:    */       }
/* 123:    */     }
/* 124:156 */     int streamDescriptorArrayOffset = bos.size();
/* 125:    */     
/* 126:158 */     bos.setBlock(0);
/* 127:159 */     LittleEndian.putUInt(buf, 0, descList.size());
/* 128:160 */     bos.write(buf, 0, 4);
/* 129:162 */     for (CryptoAPIDecryptor.StreamDescriptorEntry sde : descList)
/* 130:    */     {
/* 131:163 */       LittleEndian.putUInt(buf, 0, sde.streamOffset);
/* 132:164 */       bos.write(buf, 0, 4);
/* 133:165 */       LittleEndian.putUInt(buf, 0, sde.streamSize);
/* 134:166 */       bos.write(buf, 0, 4);
/* 135:167 */       LittleEndian.putUShort(buf, 0, sde.block);
/* 136:168 */       bos.write(buf, 0, 2);
/* 137:169 */       LittleEndian.putUByte(buf, 0, (short)sde.streamName.length());
/* 138:170 */       bos.write(buf, 0, 1);
/* 139:171 */       LittleEndian.putUByte(buf, 0, (short)sde.flags);
/* 140:172 */       bos.write(buf, 0, 1);
/* 141:173 */       LittleEndian.putUInt(buf, 0, sde.reserved2);
/* 142:174 */       bos.write(buf, 0, 4);
/* 143:175 */       byte[] nameBytes = StringUtil.getToUnicodeLE(sde.streamName);
/* 144:176 */       bos.write(nameBytes, 0, nameBytes.length);
/* 145:177 */       LittleEndian.putShort(buf, 0, (short)0);
/* 146:178 */       bos.write(buf, 0, 2);
/* 147:    */     }
/* 148:181 */     int savedSize = bos.size();
/* 149:182 */     int streamDescriptorArraySize = savedSize - streamDescriptorArrayOffset;
/* 150:183 */     LittleEndian.putUInt(buf, 0, streamDescriptorArrayOffset);
/* 151:184 */     LittleEndian.putUInt(buf, 4, streamDescriptorArraySize);
/* 152:    */     
/* 153:186 */     bos.reset();
/* 154:187 */     bos.setBlock(0);
/* 155:188 */     bos.write(buf, 0, 8);
/* 156:189 */     bos.setSize(savedSize);
/* 157:    */     
/* 158:191 */     dir.createDocument(encryptedStream, new ByteArrayInputStream(bos.getBuf(), 0, savedSize));
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected int getKeySizeInBytes()
/* 162:    */   {
/* 163:195 */     return getEncryptionInfo().getHeader().getKeySize() / 8;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setChunkSize(int chunkSize)
/* 167:    */   {
/* 168:200 */     this.chunkSize = chunkSize;
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected void createEncryptionInfoEntry(DirectoryNode dir)
/* 172:    */     throws IOException
/* 173:    */   {
/* 174:204 */     DataSpaceMapUtils.addDefaultDataSpace(dir);
/* 175:205 */     final EncryptionInfo info = getEncryptionInfo();
/* 176:206 */     final CryptoAPIEncryptionHeader header = (CryptoAPIEncryptionHeader)getEncryptionInfo().getHeader();
/* 177:207 */     final CryptoAPIEncryptionVerifier verifier = (CryptoAPIEncryptionVerifier)getEncryptionInfo().getVerifier();
/* 178:208 */     EncryptionRecord er = new EncryptionRecord()
/* 179:    */     {
/* 180:    */       public void write(LittleEndianByteArrayOutputStream bos)
/* 181:    */       {
/* 182:211 */         bos.writeShort(info.getVersionMajor());
/* 183:212 */         bos.writeShort(info.getVersionMinor());
/* 184:213 */         header.write(bos);
/* 185:214 */         verifier.write(bos);
/* 186:    */       }
/* 187:216 */     };
/* 188:217 */     DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", er);
/* 189:    */   }
/* 190:    */   
/* 191:    */   public CryptoAPIEncryptor clone()
/* 192:    */     throws CloneNotSupportedException
/* 193:    */   {
/* 194:223 */     return (CryptoAPIEncryptor)super.clone();
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected class CryptoAPICipherOutputStream
/* 198:    */     extends ChunkedCipherOutputStream
/* 199:    */   {
/* 200:    */     protected Cipher initCipherForBlock(Cipher cipher, int block, boolean lastChunk)
/* 201:    */       throws IOException, GeneralSecurityException
/* 202:    */     {
/* 203:231 */       flush();
/* 204:232 */       EncryptionInfo ei = CryptoAPIEncryptor.this.getEncryptionInfo();
/* 205:233 */       SecretKey sk = CryptoAPIEncryptor.this.getSecretKey();
/* 206:234 */       return CryptoAPIDecryptor.initCipherForBlock(cipher, block, ei, sk, 1);
/* 207:    */     }
/* 208:    */     
/* 209:    */     protected void calculateChecksum(File file, int i) {}
/* 210:    */     
/* 211:    */     protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile)
/* 212:    */       throws IOException, GeneralSecurityException
/* 213:    */     {
/* 214:244 */       throw new EncryptedDocumentException("createEncryptionInfoEntry not supported");
/* 215:    */     }
/* 216:    */     
/* 217:    */     public CryptoAPICipherOutputStream(OutputStream stream)
/* 218:    */       throws IOException, GeneralSecurityException
/* 219:    */     {
/* 220:249 */       super(CryptoAPIEncryptor.this.chunkSize);
/* 221:    */     }
/* 222:    */     
/* 223:    */     public void flush()
/* 224:    */       throws IOException
/* 225:    */     {
/* 226:254 */       writeChunk(false);
/* 227:255 */       super.flush();
/* 228:    */     }
/* 229:    */   }
/* 230:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptor
 * JD-Core Version:    0.7.0.1
 */