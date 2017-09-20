/*   1:    */ package org.apache.poi.poifs.crypt.agile;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.x2006.encryption.CTEncryption;
/*   4:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor;
/*   5:    */ import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptors;
/*   6:    */ import com.microsoft.schemas.office.x2006.encryption.EncryptionDocument;
/*   7:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherAlgorithm.Enum;
/*   8:    */ import com.microsoft.schemas.office.x2006.encryption.STCipherChaining.Enum;
/*   9:    */ import com.microsoft.schemas.office.x2006.encryption.STHashAlgorithm.Enum;
/*  10:    */ import com.microsoft.schemas.office.x2006.keyEncryptor.certificate.CTCertificateKeyEncryptor;
/*  11:    */ import com.microsoft.schemas.office.x2006.keyEncryptor.password.CTPasswordKeyEncryptor;
/*  12:    */ import java.io.ByteArrayInputStream;
/*  13:    */ import java.security.GeneralSecurityException;
/*  14:    */ import java.security.cert.CertificateFactory;
/*  15:    */ import java.security.cert.X509Certificate;
/*  16:    */ import java.util.ArrayList;
/*  17:    */ import java.util.Iterator;
/*  18:    */ import java.util.List;
/*  19:    */ import org.apache.poi.EncryptedDocumentException;
/*  20:    */ import org.apache.poi.poifs.crypt.ChainingMode;
/*  21:    */ import org.apache.poi.poifs.crypt.CipherAlgorithm;
/*  22:    */ import org.apache.poi.poifs.crypt.EncryptionVerifier;
/*  23:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  24:    */ 
/*  25:    */ public class AgileEncryptionVerifier
/*  26:    */   extends EncryptionVerifier
/*  27:    */   implements Cloneable
/*  28:    */ {
/*  29: 50 */   private List<AgileCertificateEntry> certList = new ArrayList();
/*  30: 51 */   private int keyBits = -1;
/*  31: 52 */   private int blockSize = -1;
/*  32:    */   
/*  33:    */   public AgileEncryptionVerifier(String descriptor)
/*  34:    */   {
/*  35: 55 */     this(AgileEncryptionInfoBuilder.parseDescriptor(descriptor));
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected AgileEncryptionVerifier(EncryptionDocument ed)
/*  39:    */   {
/*  40: 59 */     Iterator<CTKeyEncryptor> encList = ed.getEncryption().getKeyEncryptors().getKeyEncryptorList().iterator();
/*  41:    */     CTPasswordKeyEncryptor keyData;
/*  42:    */     try
/*  43:    */     {
/*  44: 62 */       keyData = ((CTKeyEncryptor)encList.next()).getEncryptedPasswordKey();
/*  45: 63 */       if (keyData == null) {
/*  46: 64 */         throw new NullPointerException("encryptedKey not set");
/*  47:    */       }
/*  48:    */     }
/*  49:    */     catch (Exception e)
/*  50:    */     {
/*  51: 67 */       throw new EncryptedDocumentException("Unable to parse keyData", e);
/*  52:    */     }
/*  53: 70 */     int kb = (int)keyData.getKeyBits();
/*  54: 71 */     CipherAlgorithm ca = CipherAlgorithm.fromXmlId(keyData.getCipherAlgorithm().toString(), kb);
/*  55: 72 */     setCipherAlgorithm(ca);
/*  56:    */     
/*  57: 74 */     setKeySize(kb);
/*  58:    */     
/*  59: 76 */     int blockSize = keyData.getBlockSize();
/*  60: 77 */     setBlockSize(blockSize);
/*  61:    */     
/*  62: 79 */     int hashSize = keyData.getHashSize();
/*  63:    */     
/*  64: 81 */     HashAlgorithm ha = HashAlgorithm.fromEcmaId(keyData.getHashAlgorithm().toString());
/*  65: 82 */     setHashAlgorithm(ha);
/*  66: 84 */     if (getHashAlgorithm().hashSize != hashSize) {
/*  67: 85 */       throw new EncryptedDocumentException("Unsupported hash algorithm: " + keyData.getHashAlgorithm() + " @ " + hashSize + " bytes");
/*  68:    */     }
/*  69: 89 */     setSpinCount(keyData.getSpinCount());
/*  70: 90 */     setEncryptedVerifier(keyData.getEncryptedVerifierHashInput());
/*  71: 91 */     setSalt(keyData.getSaltValue());
/*  72: 92 */     setEncryptedKey(keyData.getEncryptedKeyValue());
/*  73: 93 */     setEncryptedVerifierHash(keyData.getEncryptedVerifierHashValue());
/*  74:    */     
/*  75: 95 */     int saltSize = keyData.getSaltSize();
/*  76: 96 */     if (saltSize != getSalt().length) {
/*  77: 97 */       throw new EncryptedDocumentException("Invalid salt size");
/*  78:    */     }
/*  79:100 */     switch (keyData.getCipherChaining().intValue())
/*  80:    */     {
/*  81:    */     case 1: 
/*  82:102 */       setChainingMode(ChainingMode.cbc);
/*  83:103 */       break;
/*  84:    */     case 2: 
/*  85:105 */       setChainingMode(ChainingMode.cfb);
/*  86:106 */       break;
/*  87:    */     default: 
/*  88:108 */       throw new EncryptedDocumentException("Unsupported chaining mode - " + keyData.getCipherChaining());
/*  89:    */     }
/*  90:111 */     if (!encList.hasNext()) {
/*  91:112 */       return;
/*  92:    */     }
/*  93:    */     try
/*  94:    */     {
/*  95:116 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/*  96:117 */       while (encList.hasNext())
/*  97:    */       {
/*  98:118 */         CTCertificateKeyEncryptor certKey = ((CTKeyEncryptor)encList.next()).getEncryptedCertificateKey();
/*  99:119 */         AgileCertificateEntry ace = new AgileCertificateEntry();
/* 100:120 */         ace.certVerifier = certKey.getCertVerifier();
/* 101:121 */         ace.encryptedKey = certKey.getEncryptedKeyValue();
/* 102:122 */         ace.x509 = ((X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certKey.getX509Certificate())));
/* 103:123 */         this.certList.add(ace);
/* 104:    */       }
/* 105:    */     }
/* 106:    */     catch (GeneralSecurityException e)
/* 107:    */     {
/* 108:126 */       throw new EncryptedDocumentException("can't parse X509 certificate", e);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public AgileEncryptionVerifier(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 113:    */   {
/* 114:131 */     setCipherAlgorithm(cipherAlgorithm);
/* 115:132 */     setHashAlgorithm(hashAlgorithm);
/* 116:133 */     setChainingMode(chainingMode);
/* 117:134 */     setKeySize(keyBits);
/* 118:135 */     setBlockSize(blockSize);
/* 119:136 */     setSpinCount(100000);
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected void setSalt(byte[] salt)
/* 123:    */   {
/* 124:141 */     if ((salt == null) || (salt.length != getCipherAlgorithm().blockSize)) {
/* 125:142 */       throw new EncryptedDocumentException("invalid verifier salt");
/* 126:    */     }
/* 127:144 */     super.setSalt(salt);
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected void setEncryptedVerifier(byte[] encryptedVerifier)
/* 131:    */   {
/* 132:150 */     super.setEncryptedVerifier(encryptedVerifier);
/* 133:    */   }
/* 134:    */   
/* 135:    */   protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash)
/* 136:    */   {
/* 137:156 */     super.setEncryptedVerifierHash(encryptedVerifierHash);
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected void setEncryptedKey(byte[] encryptedKey)
/* 141:    */   {
/* 142:162 */     super.setEncryptedKey(encryptedKey);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void addCertificate(X509Certificate x509)
/* 146:    */   {
/* 147:166 */     AgileCertificateEntry ace = new AgileCertificateEntry();
/* 148:167 */     ace.x509 = x509;
/* 149:168 */     this.certList.add(ace);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public List<AgileCertificateEntry> getCertificates()
/* 153:    */   {
/* 154:172 */     return this.certList;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public AgileEncryptionVerifier clone()
/* 158:    */     throws CloneNotSupportedException
/* 159:    */   {
/* 160:177 */     AgileEncryptionVerifier other = (AgileEncryptionVerifier)super.clone();
/* 161:    */     
/* 162:179 */     other.certList = new ArrayList(this.certList);
/* 163:180 */     return other;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public int getKeySize()
/* 167:    */   {
/* 168:192 */     return this.keyBits;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public int getBlockSize()
/* 172:    */   {
/* 173:203 */     return this.blockSize;
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected void setKeySize(int keyBits)
/* 177:    */   {
/* 178:212 */     this.keyBits = keyBits;
/* 179:213 */     for (int allowedBits : getCipherAlgorithm().allowedKeySize) {
/* 180:214 */       if (allowedBits == keyBits) {
/* 181:215 */         return;
/* 182:    */       }
/* 183:    */     }
/* 184:218 */     throw new EncryptedDocumentException("KeySize " + keyBits + " not allowed for cipher " + getCipherAlgorithm());
/* 185:    */   }
/* 186:    */   
/* 187:    */   protected void setBlockSize(int blockSize)
/* 188:    */   {
/* 189:228 */     this.blockSize = blockSize;
/* 190:    */   }
/* 191:    */   
/* 192:    */   protected final void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm)
/* 193:    */   {
/* 194:233 */     super.setCipherAlgorithm(cipherAlgorithm);
/* 195:234 */     if (cipherAlgorithm.allowedKeySize.length == 1) {
/* 196:235 */       setKeySize(cipherAlgorithm.defaultKeySize);
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static class AgileCertificateEntry
/* 201:    */   {
/* 202:    */     X509Certificate x509;
/* 203:    */     byte[] encryptedKey;
/* 204:    */     byte[] certVerifier;
/* 205:    */   }
/* 206:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.agile.AgileEncryptionVerifier
 * JD-Core Version:    0.7.0.1
 */