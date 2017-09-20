/*   1:    */ package org.apache.poi.poifs.crypt.standard;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.FilterOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.security.GeneralSecurityException;
/*  10:    */ import java.security.MessageDigest;
/*  11:    */ import java.security.SecureRandom;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Random;
/*  14:    */ import javax.crypto.Cipher;
/*  15:    */ import javax.crypto.CipherOutputStream;
/*  16:    */ import javax.crypto.SecretKey;
/*  17:    */ import org.apache.poi.EncryptedDocumentException;
/*  18:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  19:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  20:    */ import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
/*  21:    */ import org.apache.poi.poifs.crypt.EncryptionHeader;
/*  22:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  23:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  24:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  25:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  26:    */ import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
/*  27:    */ import org.apache.poi.poifs.filesystem.POIFSWriterListener;
/*  28:    */ import org.apache.poi.util.IOUtils;
/*  29:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  30:    */ import org.apache.poi.util.LittleEndianOutputStream;
/*  31:    */ import org.apache.poi.util.POILogFactory;
/*  32:    */ import org.apache.poi.util.POILogger;
/*  33:    */ import org.apache.poi.util.TempFile;
/*  34:    */ 
/*  35:    */ public class StandardEncryptor
/*  36:    */   extends Encryptor
/*  37:    */   implements Cloneable
/*  38:    */ {
/*  39: 57 */   private static final POILogger logger = POILogFactory.getLogger(StandardEncryptor.class);
/*  40:    */   
/*  41:    */   public void confirmPassword(String password)
/*  42:    */   {
/*  43: 65 */     Random r = new SecureRandom();
/*  44: 66 */     byte[] salt = new byte[16];byte[] verifier = new byte[16];
/*  45: 67 */     r.nextBytes(salt);
/*  46: 68 */     r.nextBytes(verifier);
/*  47:    */     
/*  48: 70 */     confirmPassword(password, null, null, salt, verifier, null);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt)
/*  52:    */   {
/*  53: 82 */     StandardEncryptionVerifier ver = (StandardEncryptionVerifier)getEncryptionInfo().getVerifier();
/*  54:    */     
/*  55: 84 */     ver.setSalt(verifierSalt);
/*  56: 85 */     SecretKey secretKey = StandardDecryptor.generateSecretKey(password, ver, getKeySizeInBytes());
/*  57: 86 */     setSecretKey(secretKey);
/*  58: 87 */     Cipher cipher = getCipher(secretKey, null);
/*  59:    */     try
/*  60:    */     {
/*  61: 90 */       byte[] encryptedVerifier = cipher.doFinal(verifier);
/*  62: 91 */       MessageDigest hashAlgo = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
/*  63: 92 */       byte[] calcVerifierHash = hashAlgo.digest(verifier);
/*  64:    */       
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:101 */       int encVerHashSize = ver.getCipherAlgorithm().encryptedVerifierHashLength;
/*  73:102 */       byte[] encryptedVerifierHash = cipher.doFinal(Arrays.copyOf(calcVerifierHash, encVerHashSize));
/*  74:    */       
/*  75:104 */       ver.setEncryptedVerifier(encryptedVerifier);
/*  76:105 */       ver.setEncryptedVerifierHash(encryptedVerifierHash);
/*  77:    */     }
/*  78:    */     catch (GeneralSecurityException e)
/*  79:    */     {
/*  80:107 */       throw new EncryptedDocumentException("Password confirmation failed", e);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   private Cipher getCipher(SecretKey key, String padding)
/*  85:    */   {
/*  86:113 */     EncryptionVerifier ver = getEncryptionInfo().getVerifier();
/*  87:114 */     return CryptoFunctions.getCipher(key, ver.getCipherAlgorithm(), ver.getChainingMode(), null, 1, padding);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public OutputStream getDataStream(DirectoryNode dir)
/*  91:    */     throws IOException, GeneralSecurityException
/*  92:    */   {
/*  93:120 */     createEncryptionInfoEntry(dir);
/*  94:121 */     DataSpaceMapUtils.addDefaultDataSpace(dir);
/*  95:122 */     return new StandardCipherOutputStream(dir);
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected class StandardCipherOutputStream
/*  99:    */     extends FilterOutputStream
/* 100:    */     implements POIFSWriterListener
/* 101:    */   {
/* 102:    */     protected long countBytes;
/* 103:    */     protected final File fileOut;
/* 104:    */     protected final DirectoryNode dir;
/* 105:    */     
/* 106:    */     private StandardCipherOutputStream(DirectoryNode dir, File fileOut)
/* 107:    */       throws IOException
/* 108:    */     {
/* 109:142 */       super();
/* 110:    */       
/* 111:    */ 
/* 112:145 */       this.fileOut = fileOut;
/* 113:146 */       this.dir = dir;
/* 114:    */     }
/* 115:    */     
/* 116:    */     protected StandardCipherOutputStream(DirectoryNode dir)
/* 117:    */       throws IOException
/* 118:    */     {
/* 119:150 */       this(dir, TempFile.createTempFile("encrypted_package", "crypt"));
/* 120:    */     }
/* 121:    */     
/* 122:    */     public void write(byte[] b, int off, int len)
/* 123:    */       throws IOException
/* 124:    */     {
/* 125:155 */       this.out.write(b, off, len);
/* 126:156 */       this.countBytes += len;
/* 127:    */     }
/* 128:    */     
/* 129:    */     public void write(int b)
/* 130:    */       throws IOException
/* 131:    */     {
/* 132:161 */       this.out.write(b);
/* 133:162 */       this.countBytes += 1L;
/* 134:    */     }
/* 135:    */     
/* 136:    */     public void close()
/* 137:    */       throws IOException
/* 138:    */     {
/* 139:168 */       super.close();
/* 140:169 */       writeToPOIFS();
/* 141:    */     }
/* 142:    */     
/* 143:    */     void writeToPOIFS()
/* 144:    */       throws IOException
/* 145:    */     {
/* 146:173 */       int oleStreamSize = (int)(this.fileOut.length() + 8L);
/* 147:174 */       this.dir.createDocument("EncryptedPackage", oleStreamSize, this);
/* 148:    */     }
/* 149:    */     
/* 150:    */     public void processPOIFSWriterEvent(POIFSWriterEvent event)
/* 151:    */     {
/* 152:    */       try
/* 153:    */       {
/* 154:181 */         LittleEndianOutputStream leos = new LittleEndianOutputStream(event.getStream());
/* 155:    */         
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:187 */         leos.writeLong(this.countBytes);
/* 161:    */         
/* 162:189 */         FileInputStream fis = new FileInputStream(this.fileOut);
/* 163:    */         try
/* 164:    */         {
/* 165:191 */           IOUtils.copy(fis, leos);
/* 166:    */         }
/* 167:    */         finally
/* 168:    */         {
/* 169:193 */           fis.close();
/* 170:    */         }
/* 171:195 */         if (!this.fileOut.delete()) {
/* 172:196 */           StandardEncryptor.logger.log(7, new Object[] { "Can't delete temporary encryption file: " + this.fileOut });
/* 173:    */         }
/* 174:199 */         leos.close();
/* 175:    */       }
/* 176:    */       catch (IOException e)
/* 177:    */       {
/* 178:201 */         throw new EncryptedDocumentException(e);
/* 179:    */       }
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected int getKeySizeInBytes()
/* 184:    */   {
/* 185:207 */     return getEncryptionInfo().getHeader().getKeySize() / 8;
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected void createEncryptionInfoEntry(DirectoryNode dir)
/* 189:    */     throws IOException
/* 190:    */   {
/* 191:211 */     final EncryptionInfo info = getEncryptionInfo();
/* 192:212 */     final StandardEncryptionHeader header = (StandardEncryptionHeader)info.getHeader();
/* 193:213 */     final StandardEncryptionVerifier verifier = (StandardEncryptionVerifier)info.getVerifier();
/* 194:    */     
/* 195:215 */     EncryptionRecord er = new EncryptionRecord()
/* 196:    */     {
/* 197:    */       public void write(LittleEndianByteArrayOutputStream bos)
/* 198:    */       {
/* 199:218 */         bos.writeShort(info.getVersionMajor());
/* 200:219 */         bos.writeShort(info.getVersionMinor());
/* 201:220 */         bos.writeInt(info.getEncryptionFlags());
/* 202:221 */         header.write(bos);
/* 203:222 */         verifier.write(bos);
/* 204:    */       }
/* 205:225 */     };
/* 206:226 */     DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", er);
/* 207:    */   }
/* 208:    */   
/* 209:    */   public StandardEncryptor clone()
/* 210:    */     throws CloneNotSupportedException
/* 211:    */   {
/* 212:233 */     return (StandardEncryptor)super.clone();
/* 213:    */   }
/* 214:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.standard.StandardEncryptor
 * JD-Core Version:    0.7.0.1
 */