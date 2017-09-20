/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.EncryptedDocumentException;
/*   5:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*   6:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*   7:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*   8:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*   9:    */ import org.apache.poi.util.BitField;
/*  10:    */ import org.apache.poi.util.BitFieldFactory;
/*  11:    */ import org.apache.poi.util.LittleEndianInput;
/*  12:    */ 
/*  13:    */ public class EncryptionInfo
/*  14:    */   implements Cloneable
/*  15:    */ {
/*  16:    */   private final EncryptionMode encryptionMode;
/*  17:    */   private final int versionMajor;
/*  18:    */   private final int versionMinor;
/*  19:    */   private final int encryptionFlags;
/*  20:    */   private EncryptionHeader header;
/*  21:    */   private EncryptionVerifier verifier;
/*  22:    */   private Decryptor decryptor;
/*  23:    */   private Encryptor encryptor;
/*  24: 56 */   public static final BitField flagCryptoAPI = BitFieldFactory.getInstance(4);
/*  25: 62 */   public static final BitField flagDocProps = BitFieldFactory.getInstance(8);
/*  26: 68 */   public static final BitField flagExternal = BitFieldFactory.getInstance(16);
/*  27: 74 */   public static final BitField flagAES = BitFieldFactory.getInstance(32);
/*  28:    */   
/*  29:    */   public EncryptionInfo(POIFSFileSystem fs)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 81 */     this(fs.getRoot());
/*  33:    */   }
/*  34:    */   
/*  35:    */   public EncryptionInfo(OPOIFSFileSystem fs)
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 88 */     this(fs.getRoot());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public EncryptionInfo(NPOIFSFileSystem fs)
/*  42:    */     throws IOException
/*  43:    */   {
/*  44: 95 */     this(fs.getRoot());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public EncryptionInfo(DirectoryNode dir)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50:102 */     this(dir.createDocumentInputStream("EncryptionInfo"), null);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public EncryptionInfo(LittleEndianInput dis, EncryptionMode preferredEncryptionMode)
/*  54:    */     throws IOException
/*  55:    */   {
/*  56:106 */     if (preferredEncryptionMode == EncryptionMode.xor)
/*  57:    */     {
/*  58:107 */       this.versionMajor = EncryptionMode.xor.versionMajor;
/*  59:108 */       this.versionMinor = EncryptionMode.xor.versionMinor;
/*  60:    */     }
/*  61:    */     else
/*  62:    */     {
/*  63:110 */       this.versionMajor = dis.readUShort();
/*  64:111 */       this.versionMinor = dis.readUShort();
/*  65:    */     }
/*  66:114 */     if ((this.versionMajor == EncryptionMode.xor.versionMajor) && (this.versionMinor == EncryptionMode.xor.versionMinor))
/*  67:    */     {
/*  68:116 */       this.encryptionMode = EncryptionMode.xor;
/*  69:117 */       this.encryptionFlags = -1;
/*  70:    */     }
/*  71:118 */     else if ((this.versionMajor == EncryptionMode.binaryRC4.versionMajor) && (this.versionMinor == EncryptionMode.binaryRC4.versionMinor))
/*  72:    */     {
/*  73:120 */       this.encryptionMode = EncryptionMode.binaryRC4;
/*  74:121 */       this.encryptionFlags = -1;
/*  75:    */     }
/*  76:122 */     else if ((2 <= this.versionMajor) && (this.versionMajor <= 4) && (this.versionMinor == 2))
/*  77:    */     {
/*  78:125 */       this.encryptionFlags = dis.readInt();
/*  79:126 */       this.encryptionMode = ((preferredEncryptionMode == EncryptionMode.cryptoAPI) || (!flagAES.isSet(this.encryptionFlags)) ? EncryptionMode.cryptoAPI : EncryptionMode.standard);
/*  80:    */     }
/*  81:130 */     else if ((this.versionMajor == EncryptionMode.agile.versionMajor) && (this.versionMinor == EncryptionMode.agile.versionMinor))
/*  82:    */     {
/*  83:133 */       this.encryptionMode = EncryptionMode.agile;
/*  84:134 */       this.encryptionFlags = dis.readInt();
/*  85:    */     }
/*  86:    */     else
/*  87:    */     {
/*  88:136 */       this.encryptionFlags = dis.readInt();
/*  89:137 */       throw new EncryptedDocumentException("Unknown encryption: version major: " + this.versionMajor + " / version minor: " + this.versionMinor + " / fCrypto: " + flagCryptoAPI.isSet(this.encryptionFlags) + " / fExternal: " + flagExternal.isSet(this.encryptionFlags) + " / fDocProps: " + flagDocProps.isSet(this.encryptionFlags) + " / fAES: " + flagAES.isSet(this.encryptionFlags));
/*  90:    */     }
/*  91:    */     EncryptionInfoBuilder eib;
/*  92:    */     try
/*  93:    */     {
/*  94:148 */       eib = getBuilder(this.encryptionMode);
/*  95:    */     }
/*  96:    */     catch (Exception e)
/*  97:    */     {
/*  98:150 */       throw new IOException(e);
/*  99:    */     }
/* 100:153 */     eib.initialize(this, dis);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public EncryptionInfo(EncryptionMode encryptionMode)
/* 104:    */   {
/* 105:162 */     this(encryptionMode, null, null, -1, -1, null);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public EncryptionInfo(EncryptionMode encryptionMode, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode)
/* 109:    */   {
/* 110:187 */     this.encryptionMode = encryptionMode;
/* 111:188 */     this.versionMajor = encryptionMode.versionMajor;
/* 112:189 */     this.versionMinor = encryptionMode.versionMinor;
/* 113:190 */     this.encryptionFlags = encryptionMode.encryptionFlags;
/* 114:    */     EncryptionInfoBuilder eib;
/* 115:    */     try
/* 116:    */     {
/* 117:194 */       eib = getBuilder(encryptionMode);
/* 118:    */     }
/* 119:    */     catch (Exception e)
/* 120:    */     {
/* 121:196 */       throw new EncryptedDocumentException(e);
/* 122:    */     }
/* 123:199 */     eib.initialize(this, cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected static EncryptionInfoBuilder getBuilder(EncryptionMode encryptionMode)
/* 127:    */     throws ClassNotFoundException, IllegalAccessException, InstantiationException
/* 128:    */   {
/* 129:220 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 130:    */     
/* 131:222 */     EncryptionInfoBuilder eib = (EncryptionInfoBuilder)cl.loadClass(encryptionMode.builder).newInstance();
/* 132:223 */     return eib;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public int getVersionMajor()
/* 136:    */   {
/* 137:227 */     return this.versionMajor;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public int getVersionMinor()
/* 141:    */   {
/* 142:231 */     return this.versionMinor;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public int getEncryptionFlags()
/* 146:    */   {
/* 147:235 */     return this.encryptionFlags;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public EncryptionHeader getHeader()
/* 151:    */   {
/* 152:239 */     return this.header;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public EncryptionVerifier getVerifier()
/* 156:    */   {
/* 157:243 */     return this.verifier;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public Decryptor getDecryptor()
/* 161:    */   {
/* 162:247 */     return this.decryptor;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public Encryptor getEncryptor()
/* 166:    */   {
/* 167:251 */     return this.encryptor;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setHeader(EncryptionHeader header)
/* 171:    */   {
/* 172:255 */     this.header = header;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setVerifier(EncryptionVerifier verifier)
/* 176:    */   {
/* 177:259 */     this.verifier = verifier;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public void setDecryptor(Decryptor decryptor)
/* 181:    */   {
/* 182:263 */     this.decryptor = decryptor;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setEncryptor(Encryptor encryptor)
/* 186:    */   {
/* 187:267 */     this.encryptor = encryptor;
/* 188:    */   }
/* 189:    */   
/* 190:    */   public EncryptionMode getEncryptionMode()
/* 191:    */   {
/* 192:271 */     return this.encryptionMode;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public boolean isDocPropsEncrypted()
/* 196:    */   {
/* 197:279 */     return !flagDocProps.isSet(getEncryptionFlags());
/* 198:    */   }
/* 199:    */   
/* 200:    */   public EncryptionInfo clone()
/* 201:    */     throws CloneNotSupportedException
/* 202:    */   {
/* 203:284 */     EncryptionInfo other = (EncryptionInfo)super.clone();
/* 204:285 */     other.header = this.header.clone();
/* 205:286 */     other.verifier = this.verifier.clone();
/* 206:287 */     other.decryptor = this.decryptor.clone();
/* 207:288 */     other.decryptor.setEncryptionInfo(other);
/* 208:289 */     other.encryptor = this.encryptor.clone();
/* 209:290 */     other.encryptor.setEncryptionInfo(other);
/* 210:291 */     return other;
/* 211:    */   }
/* 212:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.crypt.EncryptionInfo
 * JD-Core Version:    0.7.0.1
 */