/*   1:    */ package org.apache.poi.poifs.crypt;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import org.apache.poi.EncryptedDocumentException;
/*   6:    */ import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
/*   7:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*   8:    */ import org.apache.poi.poifs.filesystem.DocumentEntry;
/*   9:    */ import org.apache.poi.poifs.filesystem.DocumentOutputStream;
/*  10:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  11:    */ import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
/*  12:    */ import org.apache.poi.poifs.filesystem.POIFSWriterListener;
/*  13:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  14:    */ import org.apache.poi.util.LittleEndianInput;
/*  15:    */ import org.apache.poi.util.LittleEndianOutput;
/*  16:    */ import org.apache.poi.util.StringUtil;
/*  17:    */ 
/*  18:    */ public class DataSpaceMapUtils
/*  19:    */ {
/*  20:    */   public static void addDefaultDataSpace(DirectoryEntry dir)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 37 */     DataSpaceMapEntry dsme = new DataSpaceMapEntry(new int[] { 0 }, new String[] { "EncryptedPackage" }, "StrongEncryptionDataSpace");
/*  24:    */     
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28: 42 */     DataSpaceMap dsm = new DataSpaceMap(new DataSpaceMapEntry[] { dsme });
/*  29: 43 */     createEncryptionEntry(dir, "\006DataSpaces/DataSpaceMap", dsm);
/*  30:    */     
/*  31: 45 */     DataSpaceDefinition dsd = new DataSpaceDefinition(new String[] { "StrongEncryptionTransform" });
/*  32: 46 */     createEncryptionEntry(dir, "\006DataSpaces/DataSpaceInfo/StrongEncryptionDataSpace", dsd);
/*  33:    */     
/*  34: 48 */     TransformInfoHeader tih = new TransformInfoHeader(1, "{FF9A3F03-56EF-4613-BDD5-5A41C1D07246}", "Microsoft.Container.EncryptionTransform", 1, 0, 1, 0, 1, 0);
/*  35:    */     
/*  36:    */ 
/*  37:    */ 
/*  38:    */ 
/*  39:    */ 
/*  40: 54 */     IRMDSTransformInfo irm = new IRMDSTransformInfo(tih, 0, null);
/*  41: 55 */     createEncryptionEntry(dir, "\006DataSpaces/TransformInfo/StrongEncryptionTransform/\006Primary", irm);
/*  42:    */     
/*  43: 57 */     DataSpaceVersionInfo dsvi = new DataSpaceVersionInfo("Microsoft.Container.DataSpaces", 1, 0, 1, 0, 1, 0);
/*  44: 58 */     createEncryptionEntry(dir, "\006DataSpaces/Version", dsvi);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static DocumentEntry createEncryptionEntry(DirectoryEntry dir, String path, EncryptionRecord out)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 62 */     String[] parts = path.split("/");
/*  51: 63 */     for (int i = 0; i < parts.length - 1; i++) {
/*  52: 64 */       dir = dir.hasEntry(parts[i]) ? (DirectoryEntry)dir.getEntry(parts[i]) : dir.createDirectory(parts[i]);
/*  53:    */     }
/*  54: 69 */     byte[] buf = new byte[5000];
/*  55: 70 */     LittleEndianByteArrayOutputStream bos = new LittleEndianByteArrayOutputStream(buf, 0);
/*  56: 71 */     out.write(bos);
/*  57:    */     
/*  58: 73 */     String fileName = parts[(parts.length - 1)];
/*  59: 75 */     if (dir.hasEntry(fileName)) {
/*  60: 76 */       dir.getEntry(fileName).delete();
/*  61:    */     }
/*  62: 79 */     dir.createDocument(fileName, bos.getWriteIndex(), new POIFSWriterListener()
/*  63:    */     {
/*  64:    */       public void processPOIFSWriterEvent(POIFSWriterEvent event)
/*  65:    */       {
/*  66:    */         try
/*  67:    */         {
/*  68: 82 */           event.getStream().write(this.val$buf, 0, event.getLimit());
/*  69:    */         }
/*  70:    */         catch (IOException e)
/*  71:    */         {
/*  72: 84 */           throw new EncryptedDocumentException(e);
/*  73:    */         }
/*  74:    */       }
/*  75:    */     });
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static class DataSpaceMap
/*  79:    */     implements EncryptionRecord
/*  80:    */   {
/*  81:    */     DataSpaceMapEntry[] entries;
/*  82:    */     
/*  83:    */     public DataSpaceMap(DataSpaceMapEntry[] entries)
/*  84:    */     {
/*  85: 94 */       this.entries = ((DataSpaceMapEntry[])entries.clone());
/*  86:    */     }
/*  87:    */     
/*  88:    */     public DataSpaceMap(LittleEndianInput is)
/*  89:    */     {
/*  90: 98 */       is.readInt();
/*  91: 99 */       int entryCount = is.readInt();
/*  92:100 */       this.entries = new DataSpaceMapEntry[entryCount];
/*  93:101 */       for (int i = 0; i < entryCount; i++) {
/*  94:102 */         this.entries[i] = new DataSpaceMapEntry(is);
/*  95:    */       }
/*  96:    */     }
/*  97:    */     
/*  98:    */     public void write(LittleEndianByteArrayOutputStream os)
/*  99:    */     {
/* 100:107 */       os.writeInt(8);
/* 101:108 */       os.writeInt(this.entries.length);
/* 102:109 */       for (DataSpaceMapEntry dsme : this.entries) {
/* 103:110 */         dsme.write(os);
/* 104:    */       }
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static class DataSpaceMapEntry
/* 109:    */     implements EncryptionRecord
/* 110:    */   {
/* 111:    */     final int[] referenceComponentType;
/* 112:    */     final String[] referenceComponent;
/* 113:    */     final String dataSpaceName;
/* 114:    */     
/* 115:    */     public DataSpaceMapEntry(int[] referenceComponentType, String[] referenceComponent, String dataSpaceName)
/* 116:    */     {
/* 117:121 */       this.referenceComponentType = ((int[])referenceComponentType.clone());
/* 118:122 */       this.referenceComponent = ((String[])referenceComponent.clone());
/* 119:123 */       this.dataSpaceName = dataSpaceName;
/* 120:    */     }
/* 121:    */     
/* 122:    */     public DataSpaceMapEntry(LittleEndianInput is)
/* 123:    */     {
/* 124:127 */       is.readInt();
/* 125:128 */       int referenceComponentCount = is.readInt();
/* 126:129 */       this.referenceComponentType = new int[referenceComponentCount];
/* 127:130 */       this.referenceComponent = new String[referenceComponentCount];
/* 128:131 */       for (int i = 0; i < referenceComponentCount; i++)
/* 129:    */       {
/* 130:132 */         this.referenceComponentType[i] = is.readInt();
/* 131:133 */         this.referenceComponent[i] = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 132:    */       }
/* 133:135 */       this.dataSpaceName = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 134:    */     }
/* 135:    */     
/* 136:    */     public void write(LittleEndianByteArrayOutputStream os)
/* 137:    */     {
/* 138:139 */       int start = os.getWriteIndex();
/* 139:140 */       LittleEndianOutput sizeOut = os.createDelayedOutput(4);
/* 140:141 */       os.writeInt(this.referenceComponent.length);
/* 141:142 */       for (int i = 0; i < this.referenceComponent.length; i++)
/* 142:    */       {
/* 143:143 */         os.writeInt(this.referenceComponentType[i]);
/* 144:144 */         DataSpaceMapUtils.writeUnicodeLPP4(os, this.referenceComponent[i]);
/* 145:    */       }
/* 146:146 */       DataSpaceMapUtils.writeUnicodeLPP4(os, this.dataSpaceName);
/* 147:147 */       sizeOut.writeInt(os.getWriteIndex() - start);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static class DataSpaceDefinition
/* 152:    */     implements EncryptionRecord
/* 153:    */   {
/* 154:    */     String[] transformer;
/* 155:    */     
/* 156:    */     public DataSpaceDefinition(String[] transformer)
/* 157:    */     {
/* 158:155 */       this.transformer = ((String[])transformer.clone());
/* 159:    */     }
/* 160:    */     
/* 161:    */     public DataSpaceDefinition(LittleEndianInput is)
/* 162:    */     {
/* 163:159 */       is.readInt();
/* 164:160 */       int transformReferenceCount = is.readInt();
/* 165:161 */       this.transformer = new String[transformReferenceCount];
/* 166:162 */       for (int i = 0; i < transformReferenceCount; i++) {
/* 167:163 */         this.transformer[i] = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 168:    */       }
/* 169:    */     }
/* 170:    */     
/* 171:    */     public void write(LittleEndianByteArrayOutputStream bos)
/* 172:    */     {
/* 173:168 */       bos.writeInt(8);
/* 174:169 */       bos.writeInt(this.transformer.length);
/* 175:170 */       for (String str : this.transformer) {
/* 176:171 */         DataSpaceMapUtils.writeUnicodeLPP4(bos, str);
/* 177:    */       }
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static class IRMDSTransformInfo
/* 182:    */     implements EncryptionRecord
/* 183:    */   {
/* 184:    */     TransformInfoHeader transformInfoHeader;
/* 185:    */     int extensibilityHeader;
/* 186:    */     String xrMLLicense;
/* 187:    */     
/* 188:    */     public IRMDSTransformInfo(TransformInfoHeader transformInfoHeader, int extensibilityHeader, String xrMLLicense)
/* 189:    */     {
/* 190:182 */       this.transformInfoHeader = transformInfoHeader;
/* 191:183 */       this.extensibilityHeader = extensibilityHeader;
/* 192:184 */       this.xrMLLicense = xrMLLicense;
/* 193:    */     }
/* 194:    */     
/* 195:    */     public IRMDSTransformInfo(LittleEndianInput is)
/* 196:    */     {
/* 197:188 */       this.transformInfoHeader = new TransformInfoHeader(is);
/* 198:189 */       this.extensibilityHeader = is.readInt();
/* 199:190 */       this.xrMLLicense = DataSpaceMapUtils.readUtf8LPP4(is);
/* 200:    */     }
/* 201:    */     
/* 202:    */     public void write(LittleEndianByteArrayOutputStream bos)
/* 203:    */     {
/* 204:195 */       this.transformInfoHeader.write(bos);
/* 205:196 */       bos.writeInt(this.extensibilityHeader);
/* 206:197 */       DataSpaceMapUtils.writeUtf8LPP4(bos, this.xrMLLicense);
/* 207:198 */       bos.writeInt(4);
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static class TransformInfoHeader
/* 212:    */     implements EncryptionRecord
/* 213:    */   {
/* 214:206 */     int readerVersionMajor = 1;
/* 215:206 */     int readerVersionMinor = 0;
/* 216:207 */     int updaterVersionMinor = 0;
/* 217:207 */     int updaterVersionMajor = 1;
/* 218:208 */     int writerVersionMinor = 0;
/* 219:208 */     int writerVersionMajor = 1;
/* 220:    */     int transformType;
/* 221:    */     String transformerId;
/* 222:    */     String transformerName;
/* 223:    */     
/* 224:    */     public TransformInfoHeader(int transformType, String transformerId, String transformerName, int readerVersionMajor, int readerVersionMinor, int updaterVersionMajor, int updaterVersionMinor, int writerVersionMajor, int writerVersionMinor)
/* 225:    */     {
/* 226:218 */       this.transformType = transformType;
/* 227:219 */       this.transformerId = transformerId;
/* 228:220 */       this.transformerName = transformerName;
/* 229:221 */       this.readerVersionMajor = readerVersionMajor;
/* 230:222 */       this.readerVersionMinor = readerVersionMinor;
/* 231:223 */       this.updaterVersionMajor = updaterVersionMajor;
/* 232:224 */       this.updaterVersionMinor = updaterVersionMinor;
/* 233:225 */       this.writerVersionMajor = writerVersionMajor;
/* 234:226 */       this.writerVersionMinor = writerVersionMinor;
/* 235:    */     }
/* 236:    */     
/* 237:    */     public TransformInfoHeader(LittleEndianInput is)
/* 238:    */     {
/* 239:230 */       is.readInt();
/* 240:231 */       this.transformType = is.readInt();
/* 241:232 */       this.transformerId = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 242:233 */       this.transformerName = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 243:234 */       this.readerVersionMajor = is.readShort();
/* 244:235 */       this.readerVersionMinor = is.readShort();
/* 245:236 */       this.updaterVersionMajor = is.readShort();
/* 246:237 */       this.updaterVersionMinor = is.readShort();
/* 247:238 */       this.writerVersionMajor = is.readShort();
/* 248:239 */       this.writerVersionMinor = is.readShort();
/* 249:    */     }
/* 250:    */     
/* 251:    */     public void write(LittleEndianByteArrayOutputStream bos)
/* 252:    */     {
/* 253:243 */       int start = bos.getWriteIndex();
/* 254:244 */       LittleEndianOutput sizeOut = bos.createDelayedOutput(4);
/* 255:245 */       bos.writeInt(this.transformType);
/* 256:246 */       DataSpaceMapUtils.writeUnicodeLPP4(bos, this.transformerId);
/* 257:247 */       sizeOut.writeInt(bos.getWriteIndex() - start);
/* 258:248 */       DataSpaceMapUtils.writeUnicodeLPP4(bos, this.transformerName);
/* 259:249 */       bos.writeShort(this.readerVersionMajor);
/* 260:250 */       bos.writeShort(this.readerVersionMinor);
/* 261:251 */       bos.writeShort(this.updaterVersionMajor);
/* 262:252 */       bos.writeShort(this.updaterVersionMinor);
/* 263:253 */       bos.writeShort(this.writerVersionMajor);
/* 264:254 */       bos.writeShort(this.writerVersionMinor);
/* 265:    */     }
/* 266:    */   }
/* 267:    */   
/* 268:    */   public static class DataSpaceVersionInfo
/* 269:    */     implements EncryptionRecord
/* 270:    */   {
/* 271:260 */     int readerVersionMajor = 1;
/* 272:260 */     int readerVersionMinor = 0;
/* 273:261 */     int updaterVersionMajor = 1;
/* 274:261 */     int updaterVersionMinor = 0;
/* 275:262 */     int writerVersionMajor = 1;
/* 276:262 */     int writerVersionMinor = 0;
/* 277:    */     String featureIdentifier;
/* 278:    */     
/* 279:    */     public DataSpaceVersionInfo(LittleEndianInput is)
/* 280:    */     {
/* 281:265 */       this.featureIdentifier = DataSpaceMapUtils.readUnicodeLPP4(is);
/* 282:266 */       this.readerVersionMajor = is.readShort();
/* 283:267 */       this.readerVersionMinor = is.readShort();
/* 284:268 */       this.updaterVersionMajor = is.readShort();
/* 285:269 */       this.updaterVersionMinor = is.readShort();
/* 286:270 */       this.writerVersionMajor = is.readShort();
/* 287:271 */       this.writerVersionMinor = is.readShort();
/* 288:    */     }
/* 289:    */     
/* 290:    */     public DataSpaceVersionInfo(String featureIdentifier, int readerVersionMajor, int readerVersionMinor, int updaterVersionMajor, int updaterVersionMinor, int writerVersionMajor, int writerVersionMinor)
/* 291:    */     {
/* 292:280 */       this.featureIdentifier = featureIdentifier;
/* 293:281 */       this.readerVersionMajor = readerVersionMajor;
/* 294:282 */       this.readerVersionMinor = readerVersionMinor;
/* 295:283 */       this.updaterVersionMajor = updaterVersionMajor;
/* 296:284 */       this.updaterVersionMinor = updaterVersionMinor;
/* 297:285 */       this.writerVersionMajor = writerVersionMajor;
/* 298:286 */       this.writerVersionMinor = writerVersionMinor;
/* 299:    */     }
/* 300:    */     
/* 301:    */     public void write(LittleEndianByteArrayOutputStream bos)
/* 302:    */     {
/* 303:290 */       DataSpaceMapUtils.writeUnicodeLPP4(bos, this.featureIdentifier);
/* 304:291 */       bos.writeShort(this.readerVersionMajor);
/* 305:292 */       bos.writeShort(this.readerVersionMinor);
/* 306:293 */       bos.writeShort(this.updaterVersionMajor);
/* 307:294 */       bos.writeShort(this.updaterVersionMinor);
/* 308:295 */       bos.writeShort(this.writerVersionMajor);
/* 309:296 */       bos.writeShort(this.writerVersionMinor);
/* 310:    */     }
/* 311:    */   }
/* 312:    */   
/* 313:    */   public static String readUnicodeLPP4(LittleEndianInput is)
/* 314:    */   {
/* 315:301 */     int length = is.readInt();
/* 316:302 */     if (length % 2 != 0) {
/* 317:303 */       throw new EncryptedDocumentException("UNICODE-LP-P4 structure is a multiple of 4 bytes. If Padding is present, it MUST be exactly 2 bytes long");
/* 318:    */     }
/* 319:308 */     String result = StringUtil.readUnicodeLE(is, length / 2);
/* 320:309 */     if (length % 4 == 2) {
/* 321:313 */       is.readShort();
/* 322:    */     }
/* 323:316 */     return result;
/* 324:    */   }
/* 325:    */   
/* 326:    */   public static void writeUnicodeLPP4(LittleEndianOutput os, String string)
/* 327:    */   {
/* 328:320 */     byte[] buf = StringUtil.getToUnicodeLE(string);
/* 329:321 */     os.writeInt(buf.length);
/* 330:322 */     os.write(buf);
/* 331:323 */     if (buf.length % 4 == 2) {
/* 332:324 */       os.writeShort(0);
/* 333:    */     }
/* 334:    */   }
/* 335:    */   
/* 336:    */   public static String readUtf8LPP4(LittleEndianInput is)
/* 337:    */   {
/* 338:329 */     int length = is.readInt();
/* 339:330 */     if ((length == 0) || (length == 4))
/* 340:    */     {
/* 341:331 */       is.readInt();
/* 342:332 */       return length == 0 ? null : "";
/* 343:    */     }
/* 344:335 */     byte[] data = new byte[length];
/* 345:336 */     is.readFully(data);
/* 346:    */     
/* 347:    */ 
/* 348:    */ 
/* 349:    */ 
/* 350:    */ 
/* 351:    */ 
/* 352:343 */     int scratchedBytes = length % 4;
/* 353:344 */     if (scratchedBytes > 0) {
/* 354:345 */       for (int i = 0; i < 4 - scratchedBytes; i++) {
/* 355:346 */         is.readByte();
/* 356:    */       }
/* 357:    */     }
/* 358:350 */     return new String(data, 0, data.length, Charset.forName("UTF-8"));
/* 359:    */   }
/* 360:    */   
/* 361:    */   public static void writeUtf8LPP4(LittleEndianOutput os, String str)
/* 362:    */   {
/* 363:354 */     if ((str == null) || ("".equals(str)))
/* 364:    */     {
/* 365:355 */       os.writeInt(str == null ? 0 : 4);
/* 366:356 */       os.writeInt(0);
/* 367:    */     }
/* 368:    */     else
/* 369:    */     {
/* 370:358 */       byte[] buf = str.getBytes(Charset.forName("UTF-8"));
/* 371:359 */       os.writeInt(buf.length);
/* 372:360 */       os.write(buf);
/* 373:361 */       int scratchBytes = buf.length % 4;
/* 374:362 */       if (scratchBytes > 0) {
/* 375:363 */         for (int i = 0; i < 4 - scratchBytes; i++) {
/* 376:364 */           os.writeByte(0);
/* 377:    */         }
/* 378:    */       }
/* 379:    */     }
/* 380:    */   }
/* 381:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.crypt.DataSpaceMapUtils

 * JD-Core Version:    0.7.0.1

 */