/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.math.BigInteger;
/*   7:    */ import java.security.SecureRandom;
/*   8:    */ import java.util.Arrays;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.EncryptedDocumentException;
/*  11:    */ import org.apache.poi.POIXMLDocumentPart;
/*  12:    */ import org.apache.poi.POIXMLTypeLoader;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  14:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  15:    */ import org.apache.poi.poifs.crypt.HashAlgorithm;
/*  16:    */ import org.apache.xmlbeans.SchemaType;
/*  17:    */ import org.apache.xmlbeans.XmlOptions;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect.Factory;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff.Factory;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings.Factory;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom;
/*  25:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgClass;
/*  26:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STAlgType;
/*  27:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STCryptProv;
/*  28:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STCryptProv.Enum;
/*  29:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocProtect.Enum;
/*  30:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*  31:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument;
/*  32:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument.Factory;
/*  33:    */ 
/*  34:    */ public class XWPFSettings
/*  35:    */   extends POIXMLDocumentPart
/*  36:    */ {
/*  37:    */   private CTSettings ctSettings;
/*  38:    */   
/*  39:    */   public XWPFSettings(PackagePart part)
/*  40:    */     throws IOException
/*  41:    */   {
/*  42: 55 */     super(part);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public XWPFSettings()
/*  46:    */   {
/*  47: 60 */     this.ctSettings = CTSettings.Factory.newInstance();
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected void onDocumentRead()
/*  51:    */     throws IOException
/*  52:    */   {
/*  53: 65 */     super.onDocumentRead();
/*  54: 66 */     readFrom(getPackagePart().getInputStream());
/*  55:    */   }
/*  56:    */   
/*  57:    */   public long getZoomPercent()
/*  58:    */   {
/*  59:    */     CTZoom zoom;
/*  60:    */     CTZoom zoom;
/*  61: 83 */     if (!this.ctSettings.isSetZoom()) {
/*  62: 84 */       zoom = this.ctSettings.addNewZoom();
/*  63:    */     } else {
/*  64: 86 */       zoom = this.ctSettings.getZoom();
/*  65:    */     }
/*  66: 90 */     BigInteger percent = zoom.getPercent();
/*  67: 91 */     if (percent == null) {
/*  68: 92 */       return 100L;
/*  69:    */     }
/*  70: 94 */     return percent.longValue();
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setZoomPercent(long zoomPercent)
/*  74:    */   {
/*  75:108 */     if (!this.ctSettings.isSetZoom()) {
/*  76:109 */       this.ctSettings.addNewZoom();
/*  77:    */     }
/*  78:111 */     CTZoom zoom = this.ctSettings.getZoom();
/*  79:112 */     zoom.setPercent(BigInteger.valueOf(zoomPercent));
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean isEnforcedWith()
/*  83:    */   {
/*  84:129 */     CTDocProtect ctDocProtect = this.ctSettings.getDocumentProtection();
/*  85:131 */     if (ctDocProtect == null) {
/*  86:132 */       return false;
/*  87:    */     }
/*  88:135 */     return ctDocProtect.getEnforcement().equals(STOnOff.X_1);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isEnforcedWith(STDocProtect.Enum editValue)
/*  92:    */   {
/*  93:153 */     CTDocProtect ctDocProtect = this.ctSettings.getDocumentProtection();
/*  94:155 */     if (ctDocProtect == null) {
/*  95:156 */       return false;
/*  96:    */     }
/*  97:159 */     return (ctDocProtect.getEnforcement().equals(STOnOff.X_1)) && (ctDocProtect.getEdit().equals(editValue));
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setEnforcementEditValue(STDocProtect.Enum editValue)
/* 101:    */   {
/* 102:176 */     safeGetDocumentProtection().setEnforcement(STOnOff.X_1);
/* 103:177 */     safeGetDocumentProtection().setEdit(editValue);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setEnforcementEditValue(STDocProtect.Enum editValue, String password, HashAlgorithm hashAlgo)
/* 107:    */   {
/* 108:199 */     safeGetDocumentProtection().setEnforcement(STOnOff.X_1);
/* 109:200 */     safeGetDocumentProtection().setEdit(editValue);
/* 110:202 */     if (password == null)
/* 111:    */     {
/* 112:203 */       if (safeGetDocumentProtection().isSetCryptProviderType()) {
/* 113:204 */         safeGetDocumentProtection().unsetCryptProviderType();
/* 114:    */       }
/* 115:207 */       if (safeGetDocumentProtection().isSetCryptAlgorithmClass()) {
/* 116:208 */         safeGetDocumentProtection().unsetCryptAlgorithmClass();
/* 117:    */       }
/* 118:211 */       if (safeGetDocumentProtection().isSetCryptAlgorithmType()) {
/* 119:212 */         safeGetDocumentProtection().unsetCryptAlgorithmType();
/* 120:    */       }
/* 121:215 */       if (safeGetDocumentProtection().isSetCryptAlgorithmSid()) {
/* 122:216 */         safeGetDocumentProtection().unsetCryptAlgorithmSid();
/* 123:    */       }
/* 124:219 */       if (safeGetDocumentProtection().isSetSalt()) {
/* 125:220 */         safeGetDocumentProtection().unsetSalt();
/* 126:    */       }
/* 127:223 */       if (safeGetDocumentProtection().isSetCryptSpinCount()) {
/* 128:224 */         safeGetDocumentProtection().unsetCryptSpinCount();
/* 129:    */       }
/* 130:227 */       if (safeGetDocumentProtection().isSetHash()) {
/* 131:228 */         safeGetDocumentProtection().unsetHash();
/* 132:    */       }
/* 133:    */     }
/* 134:    */     else
/* 135:    */     {
/* 136:233 */       if (hashAlgo == null) {
/* 137:234 */         hashAlgo = HashAlgorithm.sha1;
/* 138:    */       }
/* 139:    */       STCryptProv.Enum providerType;
/* 140:    */       int sid;
/* 141:237 */       switch (1.$SwitchMap$org$apache$poi$poifs$crypt$HashAlgorithm[hashAlgo.ordinal()])
/* 142:    */       {
/* 143:    */       case 1: 
/* 144:239 */         providerType = STCryptProv.RSA_FULL;
/* 145:240 */         sid = 1;
/* 146:241 */         break;
/* 147:    */       case 2: 
/* 148:243 */         providerType = STCryptProv.RSA_FULL;
/* 149:244 */         sid = 2;
/* 150:245 */         break;
/* 151:    */       case 3: 
/* 152:247 */         providerType = STCryptProv.RSA_FULL;
/* 153:248 */         sid = 3;
/* 154:249 */         break;
/* 155:    */       case 4: 
/* 156:251 */         providerType = STCryptProv.RSA_FULL;
/* 157:252 */         sid = 4;
/* 158:253 */         break;
/* 159:    */       case 5: 
/* 160:255 */         providerType = STCryptProv.RSA_AES;
/* 161:256 */         sid = 12;
/* 162:257 */         break;
/* 163:    */       case 6: 
/* 164:259 */         providerType = STCryptProv.RSA_AES;
/* 165:260 */         sid = 13;
/* 166:261 */         break;
/* 167:    */       case 7: 
/* 168:263 */         providerType = STCryptProv.RSA_AES;
/* 169:264 */         sid = 14;
/* 170:265 */         break;
/* 171:    */       default: 
/* 172:267 */         throw new EncryptedDocumentException("Hash algorithm '" + hashAlgo + "' is not supported for document write protection.");
/* 173:    */       }
/* 174:272 */       SecureRandom random = new SecureRandom();
/* 175:273 */       byte[] salt = random.generateSeed(16);
/* 176:    */       
/* 177:    */ 
/* 178:    */ 
/* 179:277 */       int spinCount = 100000;
/* 180:    */       
/* 181:279 */       String legacyHash = CryptoFunctions.xorHashPasswordReversed(password);
/* 182:    */       
/* 183:    */ 
/* 184:    */ 
/* 185:283 */       byte[] hash = CryptoFunctions.hashPassword(legacyHash, hashAlgo, salt, spinCount, false);
/* 186:    */       
/* 187:285 */       safeGetDocumentProtection().setSalt(salt);
/* 188:286 */       safeGetDocumentProtection().setHash(hash);
/* 189:287 */       safeGetDocumentProtection().setCryptSpinCount(BigInteger.valueOf(spinCount));
/* 190:288 */       safeGetDocumentProtection().setCryptAlgorithmType(STAlgType.TYPE_ANY);
/* 191:289 */       safeGetDocumentProtection().setCryptAlgorithmClass(STAlgClass.HASH);
/* 192:290 */       safeGetDocumentProtection().setCryptProviderType(providerType);
/* 193:291 */       safeGetDocumentProtection().setCryptAlgorithmSid(BigInteger.valueOf(sid));
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean validateProtectionPassword(String password)
/* 198:    */   {
/* 199:302 */     BigInteger sid = safeGetDocumentProtection().getCryptAlgorithmSid();
/* 200:303 */     byte[] hash = safeGetDocumentProtection().getHash();
/* 201:304 */     byte[] salt = safeGetDocumentProtection().getSalt();
/* 202:305 */     BigInteger spinCount = safeGetDocumentProtection().getCryptSpinCount();
/* 203:307 */     if ((sid == null) || (hash == null) || (salt == null) || (spinCount == null)) {
/* 204:307 */       return false;
/* 205:    */     }
/* 206:    */     HashAlgorithm hashAlgo;
/* 207:310 */     switch (sid.intValue())
/* 208:    */     {
/* 209:    */     case 1: 
/* 210:312 */       hashAlgo = HashAlgorithm.md2;
/* 211:313 */       break;
/* 212:    */     case 2: 
/* 213:315 */       hashAlgo = HashAlgorithm.md4;
/* 214:316 */       break;
/* 215:    */     case 3: 
/* 216:318 */       hashAlgo = HashAlgorithm.md5;
/* 217:319 */       break;
/* 218:    */     case 4: 
/* 219:321 */       hashAlgo = HashAlgorithm.sha1;
/* 220:322 */       break;
/* 221:    */     case 12: 
/* 222:324 */       hashAlgo = HashAlgorithm.sha256;
/* 223:325 */       break;
/* 224:    */     case 13: 
/* 225:327 */       hashAlgo = HashAlgorithm.sha384;
/* 226:328 */       break;
/* 227:    */     case 14: 
/* 228:330 */       hashAlgo = HashAlgorithm.sha512;
/* 229:331 */       break;
/* 230:    */     case 5: 
/* 231:    */     case 6: 
/* 232:    */     case 7: 
/* 233:    */     case 8: 
/* 234:    */     case 9: 
/* 235:    */     case 10: 
/* 236:    */     case 11: 
/* 237:    */     default: 
/* 238:333 */       return false;
/* 239:    */     }
/* 240:336 */     String legacyHash = CryptoFunctions.xorHashPasswordReversed(password);
/* 241:    */     
/* 242:    */ 
/* 243:    */ 
/* 244:340 */     byte[] hash2 = CryptoFunctions.hashPassword(legacyHash, hashAlgo, salt, spinCount.intValue(), false);
/* 245:    */     
/* 246:342 */     return Arrays.equals(hash, hash2);
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void removeEnforcement()
/* 250:    */   {
/* 251:351 */     safeGetDocumentProtection().setEnforcement(STOnOff.X_0);
/* 252:    */   }
/* 253:    */   
/* 254:    */   public void setUpdateFields()
/* 255:    */   {
/* 256:367 */     CTOnOff onOff = CTOnOff.Factory.newInstance();
/* 257:368 */     onOff.setVal(STOnOff.TRUE);
/* 258:369 */     this.ctSettings.setUpdateFields(onOff);
/* 259:    */   }
/* 260:    */   
/* 261:    */   boolean isUpdateFields()
/* 262:    */   {
/* 263:373 */     return (this.ctSettings.isSetUpdateFields()) && (this.ctSettings.getUpdateFields().getVal() == STOnOff.TRUE);
/* 264:    */   }
/* 265:    */   
/* 266:    */   public boolean isTrackRevisions()
/* 267:    */   {
/* 268:382 */     return this.ctSettings.isSetTrackRevisions();
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void setTrackRevisions(boolean enable)
/* 272:    */   {
/* 273:391 */     if (enable)
/* 274:    */     {
/* 275:392 */       if (!this.ctSettings.isSetTrackRevisions()) {
/* 276:393 */         this.ctSettings.addNewTrackRevisions();
/* 277:    */       }
/* 278:    */     }
/* 279:396 */     else if (this.ctSettings.isSetTrackRevisions()) {
/* 280:397 */       this.ctSettings.unsetTrackRevisions();
/* 281:    */     }
/* 282:    */   }
/* 283:    */   
/* 284:    */   protected void commit()
/* 285:    */     throws IOException
/* 286:    */   {
/* 287:404 */     if (this.ctSettings == null) {
/* 288:405 */       throw new IllegalStateException("Unable to write out settings that were never read in!");
/* 289:    */     }
/* 290:408 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 291:409 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTSettings.type.getName().getNamespaceURI(), "settings"));
/* 292:    */     
/* 293:411 */     PackagePart part = getPackagePart();
/* 294:412 */     OutputStream out = part.getOutputStream();
/* 295:413 */     this.ctSettings.save(out, xmlOptions);
/* 296:414 */     out.close();
/* 297:    */   }
/* 298:    */   
/* 299:    */   private CTDocProtect safeGetDocumentProtection()
/* 300:    */   {
/* 301:418 */     CTDocProtect documentProtection = this.ctSettings.getDocumentProtection();
/* 302:419 */     if (documentProtection == null)
/* 303:    */     {
/* 304:420 */       documentProtection = CTDocProtect.Factory.newInstance();
/* 305:421 */       this.ctSettings.setDocumentProtection(documentProtection);
/* 306:    */     }
/* 307:423 */     return this.ctSettings.getDocumentProtection();
/* 308:    */   }
/* 309:    */   
/* 310:    */   private void readFrom(InputStream inputStream)
/* 311:    */   {
/* 312:    */     try
/* 313:    */     {
/* 314:428 */       this.ctSettings = SettingsDocument.Factory.parse(inputStream, POIXMLTypeLoader.DEFAULT_XML_OPTIONS).getSettings();
/* 315:    */     }
/* 316:    */     catch (Exception e)
/* 317:    */     {
/* 318:430 */       throw new RuntimeException(e);
/* 319:    */     }
/* 320:    */   }
/* 321:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFSettings
 * JD-Core Version:    0.7.0.1
 */