/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.Closeable;
/*   6:    */ import java.io.File;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.security.GeneralSecurityException;
/*  10:    */ import java.util.List;
/*  11:    */ import org.apache.poi.hpsf.DocumentSummaryInformation;
/*  12:    */ import org.apache.poi.hpsf.PropertySet;
/*  13:    */ import org.apache.poi.hpsf.PropertySetFactory;
/*  14:    */ import org.apache.poi.hpsf.SummaryInformation;
/*  15:    */ import org.apache.poi.hpsf.WritingNotSupportedException;
/*  16:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  17:    */ import org.apache.poi.poifs.crypt.Encryptor;
/*  18:    */ import org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIEncryptor;
/*  19:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  20:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  21:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  22:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*  23:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  24:    */ import org.apache.poi.util.Internal;
/*  25:    */ import org.apache.poi.util.POILogFactory;
/*  26:    */ import org.apache.poi.util.POILogger;
/*  27:    */ 
/*  28:    */ public abstract class POIDocument
/*  29:    */   implements Closeable
/*  30:    */ {
/*  31:    */   private SummaryInformation sInf;
/*  32:    */   private DocumentSummaryInformation dsInf;
/*  33:    */   private DirectoryNode directory;
/*  34: 62 */   private static final POILogger logger = POILogFactory.getLogger(POIDocument.class);
/*  35:    */   private boolean initialized;
/*  36:    */   
/*  37:    */   protected POIDocument(DirectoryNode dir)
/*  38:    */   {
/*  39: 73 */     this.directory = dir;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected POIDocument(OPOIFSFileSystem fs)
/*  43:    */   {
/*  44: 82 */     this(fs.getRoot());
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected POIDocument(NPOIFSFileSystem fs)
/*  48:    */   {
/*  49: 90 */     this(fs.getRoot());
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected POIDocument(POIFSFileSystem fs)
/*  53:    */   {
/*  54: 98 */     this(fs.getRoot());
/*  55:    */   }
/*  56:    */   
/*  57:    */   public DocumentSummaryInformation getDocumentSummaryInformation()
/*  58:    */   {
/*  59:108 */     if (!this.initialized) {
/*  60:109 */       readProperties();
/*  61:    */     }
/*  62:111 */     return this.dsInf;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public SummaryInformation getSummaryInformation()
/*  66:    */   {
/*  67:121 */     if (!this.initialized) {
/*  68:122 */       readProperties();
/*  69:    */     }
/*  70:124 */     return this.sInf;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void createInformationProperties()
/*  74:    */   {
/*  75:137 */     if (!this.initialized) {
/*  76:138 */       readProperties();
/*  77:    */     }
/*  78:140 */     if (this.sInf == null) {
/*  79:141 */       this.sInf = PropertySetFactory.newSummaryInformation();
/*  80:    */     }
/*  81:143 */     if (this.dsInf == null) {
/*  82:144 */       this.dsInf = PropertySetFactory.newDocumentSummaryInformation();
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected void readProperties()
/*  87:    */   {
/*  88:155 */     if (this.initialized) {
/*  89:156 */       return;
/*  90:    */     }
/*  91:158 */     DocumentSummaryInformation dsi = (DocumentSummaryInformation)readPropertySet(DocumentSummaryInformation.class, "\005DocumentSummaryInformation");
/*  92:159 */     if (dsi != null) {
/*  93:160 */       this.dsInf = dsi;
/*  94:    */     }
/*  95:162 */     SummaryInformation si = (SummaryInformation)readPropertySet(SummaryInformation.class, "\005SummaryInformation");
/*  96:163 */     if (si != null) {
/*  97:164 */       this.sInf = si;
/*  98:    */     }
/*  99:168 */     this.initialized = true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private <T> T readPropertySet(Class<T> clazz, String name)
/* 103:    */   {
/* 104:173 */     String localName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
/* 105:    */     try
/* 106:    */     {
/* 107:175 */       PropertySet ps = getPropertySet(name);
/* 108:176 */       if (clazz.isInstance(ps)) {
/* 109:177 */         return ps;
/* 110:    */       }
/* 111:178 */       if (ps != null) {
/* 112:179 */         logger.log(5, new Object[] { localName + " property set came back with wrong class - " + ps.getClass().getName() });
/* 113:    */       } else {
/* 114:181 */         logger.log(5, new Object[] { localName + " property set came back as null" });
/* 115:    */       }
/* 116:    */     }
/* 117:    */     catch (IOException e)
/* 118:    */     {
/* 119:184 */       logger.log(7, new Object[] { "can't retrieve property set", e });
/* 120:    */     }
/* 121:186 */     return null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected PropertySet getPropertySet(String setName)
/* 125:    */     throws IOException
/* 126:    */   {
/* 127:197 */     return getPropertySet(setName, getEncryptionInfo());
/* 128:    */   }
/* 129:    */   
/* 130:    */   /* Error */
/* 131:    */   protected PropertySet getPropertySet(String setName, EncryptionInfo encryptionInfo)
/* 132:    */     throws IOException
/* 133:    */   {
/* 134:    */     // Byte code:
/* 135:    */     //   0: aload_0
/* 136:    */     //   1: getfield 2	org/apache/poi/POIDocument:directory	Lorg/apache/poi/poifs/filesystem/DirectoryNode;
/* 137:    */     //   4: astore_3
/* 138:    */     //   5: aconst_null
/* 139:    */     //   6: astore 4
/* 140:    */     //   8: ldc 37
/* 141:    */     //   10: astore 5
/* 142:    */     //   12: aload_2
/* 143:    */     //   13: ifnull +87 -> 100
/* 144:    */     //   16: aload_2
/* 145:    */     //   17: invokevirtual 38	org/apache/poi/poifs/crypt/EncryptionInfo:isDocPropsEncrypted	()Z
/* 146:    */     //   20: ifeq +80 -> 100
/* 147:    */     //   23: ldc 39
/* 148:    */     //   25: astore 5
/* 149:    */     //   27: aload_0
/* 150:    */     //   28: invokevirtual 40	org/apache/poi/POIDocument:getEncryptedPropertyStreamName	()Ljava/lang/String;
/* 151:    */     //   31: astore 6
/* 152:    */     //   33: aload_3
/* 153:    */     //   34: aload 6
/* 154:    */     //   36: invokevirtual 41	org/apache/poi/poifs/filesystem/DirectoryNode:hasEntry	(Ljava/lang/String;)Z
/* 155:    */     //   39: ifne +36 -> 75
/* 156:    */     //   42: new 42	org/apache/poi/EncryptedDocumentException
/* 157:    */     //   45: dup
/* 158:    */     //   46: new 25	java/lang/StringBuilder
/* 159:    */     //   49: dup
/* 160:    */     //   50: invokespecial 26	java/lang/StringBuilder:<init>	()V
/* 161:    */     //   53: ldc 43
/* 162:    */     //   55: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 163:    */     //   58: aload 6
/* 164:    */     //   60: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 165:    */     //   63: ldc 44
/* 166:    */     //   65: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 167:    */     //   68: invokevirtual 30	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 168:    */     //   71: invokespecial 45	org/apache/poi/EncryptedDocumentException:<init>	(Ljava/lang/String;)V
/* 169:    */     //   74: athrow
/* 170:    */     //   75: aload_2
/* 171:    */     //   76: invokevirtual 46	org/apache/poi/poifs/crypt/EncryptionInfo:getDecryptor	()Lorg/apache/poi/poifs/crypt/Decryptor;
/* 172:    */     //   79: checkcast 47	org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDecryptor
/* 173:    */     //   82: astore 7
/* 174:    */     //   84: aload 7
/* 175:    */     //   86: aload_3
/* 176:    */     //   87: aload 6
/* 177:    */     //   89: invokevirtual 48	org/apache/poi/poifs/crypt/cryptoapi/CryptoAPIDecryptor:getSummaryEntries	(Lorg/apache/poi/poifs/filesystem/DirectoryNode;Ljava/lang/String;)Lorg/apache/poi/poifs/filesystem/POIFSFileSystem;
/* 178:    */     //   92: astore 4
/* 179:    */     //   94: aload 4
/* 180:    */     //   96: invokevirtual 5	org/apache/poi/poifs/filesystem/NPOIFSFileSystem:getRoot	()Lorg/apache/poi/poifs/filesystem/DirectoryNode;
/* 181:    */     //   99: astore_3
/* 182:    */     //   100: aload_3
/* 183:    */     //   101: ifnull +11 -> 112
/* 184:    */     //   104: aload_3
/* 185:    */     //   105: aload_1
/* 186:    */     //   106: invokevirtual 41	org/apache/poi/poifs/filesystem/DirectoryNode:hasEntry	(Ljava/lang/String;)Z
/* 187:    */     //   109: ifne +14 -> 123
/* 188:    */     //   112: aconst_null
/* 189:    */     //   113: astore 6
/* 190:    */     //   115: aload 4
/* 191:    */     //   117: invokestatic 49	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 192:    */     //   120: aload 6
/* 193:    */     //   122: areturn
/* 194:    */     //   123: ldc 37
/* 195:    */     //   125: astore 5
/* 196:    */     //   127: aload_3
/* 197:    */     //   128: aload_3
/* 198:    */     //   129: aload_1
/* 199:    */     //   130: invokevirtual 50	org/apache/poi/poifs/filesystem/DirectoryNode:getEntry	(Ljava/lang/String;)Lorg/apache/poi/poifs/filesystem/Entry;
/* 200:    */     //   133: invokevirtual 51	org/apache/poi/poifs/filesystem/DirectoryNode:createDocumentInputStream	(Lorg/apache/poi/poifs/filesystem/Entry;)Lorg/apache/poi/poifs/filesystem/DocumentInputStream;
/* 201:    */     //   136: astore 6
/* 202:    */     //   138: ldc 52
/* 203:    */     //   140: astore 5
/* 204:    */     //   142: aload 6
/* 205:    */     //   144: invokestatic 53	org/apache/poi/hpsf/PropertySetFactory:create	(Ljava/io/InputStream;)Lorg/apache/poi/hpsf/PropertySet;
/* 206:    */     //   147: astore 7
/* 207:    */     //   149: aload 6
/* 208:    */     //   151: invokevirtual 54	org/apache/poi/poifs/filesystem/DocumentInputStream:close	()V
/* 209:    */     //   154: aload 4
/* 210:    */     //   156: invokestatic 49	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 211:    */     //   159: aload 7
/* 212:    */     //   161: areturn
/* 213:    */     //   162: astore 8
/* 214:    */     //   164: aload 6
/* 215:    */     //   166: invokevirtual 54	org/apache/poi/poifs/filesystem/DocumentInputStream:close	()V
/* 216:    */     //   169: aload 8
/* 217:    */     //   171: athrow
/* 218:    */     //   172: astore 6
/* 219:    */     //   174: aload 6
/* 220:    */     //   176: athrow
/* 221:    */     //   177: astore 6
/* 222:    */     //   179: new 33	java/io/IOException
/* 223:    */     //   182: dup
/* 224:    */     //   183: new 25	java/lang/StringBuilder
/* 225:    */     //   186: dup
/* 226:    */     //   187: invokespecial 26	java/lang/StringBuilder:<init>	()V
/* 227:    */     //   190: ldc 56
/* 228:    */     //   192: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 229:    */     //   195: aload 5
/* 230:    */     //   197: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 231:    */     //   200: ldc 57
/* 232:    */     //   202: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 233:    */     //   205: aload_1
/* 234:    */     //   206: invokevirtual 27	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 235:    */     //   209: invokevirtual 30	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 236:    */     //   212: aload 6
/* 237:    */     //   214: invokespecial 58	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/* 238:    */     //   217: athrow
/* 239:    */     //   218: astore 9
/* 240:    */     //   220: aload 4
/* 241:    */     //   222: invokestatic 49	org/apache/poi/util/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/* 242:    */     //   225: aload 9
/* 243:    */     //   227: athrow
/* 244:    */     // Line number table:
/* 245:    */     //   Java source line #209	-> byte code offset #0
/* 246:    */     //   Java source line #211	-> byte code offset #5
/* 247:    */     //   Java source line #212	-> byte code offset #8
/* 248:    */     //   Java source line #214	-> byte code offset #12
/* 249:    */     //   Java source line #215	-> byte code offset #23
/* 250:    */     //   Java source line #216	-> byte code offset #27
/* 251:    */     //   Java source line #217	-> byte code offset #33
/* 252:    */     //   Java source line #218	-> byte code offset #42
/* 253:    */     //   Java source line #220	-> byte code offset #75
/* 254:    */     //   Java source line #221	-> byte code offset #84
/* 255:    */     //   Java source line #222	-> byte code offset #94
/* 256:    */     //   Java source line #226	-> byte code offset #100
/* 257:    */     //   Java source line #227	-> byte code offset #112
/* 258:    */     //   Java source line #245	-> byte code offset #115
/* 259:    */     //   Java source line #231	-> byte code offset #123
/* 260:    */     //   Java source line #232	-> byte code offset #127
/* 261:    */     //   Java source line #235	-> byte code offset #138
/* 262:    */     //   Java source line #236	-> byte code offset #142
/* 263:    */     //   Java source line #238	-> byte code offset #149
/* 264:    */     //   Java source line #245	-> byte code offset #154
/* 265:    */     //   Java source line #238	-> byte code offset #162
/* 266:    */     //   Java source line #240	-> byte code offset #172
/* 267:    */     //   Java source line #241	-> byte code offset #174
/* 268:    */     //   Java source line #242	-> byte code offset #177
/* 269:    */     //   Java source line #243	-> byte code offset #179
/* 270:    */     //   Java source line #245	-> byte code offset #218
/* 271:    */     // Local variable table:
/* 272:    */     //   start	length	slot	name	signature
/* 273:    */     //   0	228	0	this	POIDocument
/* 274:    */     //   0	228	1	setName	String
/* 275:    */     //   0	228	2	encryptionInfo	EncryptionInfo
/* 276:    */     //   4	125	3	dirNode	DirectoryNode
/* 277:    */     //   6	215	4	encPoifs	NPOIFSFileSystem
/* 278:    */     //   10	186	5	step	String
/* 279:    */     //   31	90	6	encryptedStream	String
/* 280:    */     //   136	29	6	dis	org.apache.poi.poifs.filesystem.DocumentInputStream
/* 281:    */     //   172	3	6	e	IOException
/* 282:    */     //   177	36	6	e	java.lang.Exception
/* 283:    */     //   82	78	7	dec	org.apache.poi.poifs.crypt.cryptoapi.CryptoAPIDecryptor
/* 284:    */     //   162	8	8	localObject1	Object
/* 285:    */     //   218	8	9	localObject2	Object
/* 286:    */     // Exception table:
/* 287:    */     //   from	to	target	type
/* 288:    */     //   138	149	162	finally
/* 289:    */     //   162	164	162	finally
/* 290:    */     //   12	115	172	java/io/IOException
/* 291:    */     //   123	154	172	java/io/IOException
/* 292:    */     //   162	172	172	java/io/IOException
/* 293:    */     //   12	115	177	java/lang/Exception
/* 294:    */     //   123	154	177	java/lang/Exception
/* 295:    */     //   162	172	177	java/lang/Exception
/* 296:    */     //   12	115	218	finally
/* 297:    */     //   123	154	218	finally
/* 298:    */     //   162	220	218	finally
/* 299:    */   }
/* 300:    */   
/* 301:    */   protected void writeProperties()
/* 302:    */     throws IOException
/* 303:    */   {
/* 304:257 */     validateInPlaceWritePossible();
/* 305:258 */     writeProperties(this.directory.getFileSystem(), null);
/* 306:    */   }
/* 307:    */   
/* 308:    */   protected void writeProperties(NPOIFSFileSystem outFS)
/* 309:    */     throws IOException
/* 310:    */   {
/* 311:269 */     writeProperties(outFS, null);
/* 312:    */   }
/* 313:    */   
/* 314:    */   protected void writeProperties(NPOIFSFileSystem outFS, List<String> writtenEntries)
/* 315:    */     throws IOException
/* 316:    */   {
/* 317:280 */     EncryptionInfo ei = getEncryptionInfo();
/* 318:281 */     boolean encryptProps = (ei != null) && (ei.isDocPropsEncrypted());
/* 319:282 */     NPOIFSFileSystem fs = encryptProps ? new NPOIFSFileSystem() : outFS;
/* 320:    */     
/* 321:284 */     SummaryInformation si = getSummaryInformation();
/* 322:285 */     if (si != null)
/* 323:    */     {
/* 324:286 */       writePropertySet("\005SummaryInformation", si, fs);
/* 325:287 */       if (writtenEntries != null) {
/* 326:288 */         writtenEntries.add("\005SummaryInformation");
/* 327:    */       }
/* 328:    */     }
/* 329:291 */     DocumentSummaryInformation dsi = getDocumentSummaryInformation();
/* 330:292 */     if (dsi != null)
/* 331:    */     {
/* 332:293 */       writePropertySet("\005DocumentSummaryInformation", dsi, fs);
/* 333:294 */       if (writtenEntries != null) {
/* 334:295 */         writtenEntries.add("\005DocumentSummaryInformation");
/* 335:    */       }
/* 336:    */     }
/* 337:299 */     if (!encryptProps) {
/* 338:300 */       return;
/* 339:    */     }
/* 340:304 */     dsi = PropertySetFactory.newDocumentSummaryInformation();
/* 341:305 */     writePropertySet("\005DocumentSummaryInformation", dsi, outFS);
/* 342:307 */     if (outFS.getRoot().hasEntry("\005SummaryInformation")) {
/* 343:308 */       outFS.getRoot().getEntry("\005SummaryInformation").delete();
/* 344:    */     }
/* 345:310 */     Encryptor encGen = ei.getEncryptor();
/* 346:311 */     if (!(encGen instanceof CryptoAPIEncryptor)) {
/* 347:312 */       throw new EncryptedDocumentException("Using " + ei.getEncryptionMode() + " encryption. Only CryptoAPI encryption supports encrypted property sets!");
/* 348:    */     }
/* 349:314 */     CryptoAPIEncryptor enc = (CryptoAPIEncryptor)encGen;
/* 350:    */     try
/* 351:    */     {
/* 352:316 */       enc.setSummaryEntries(outFS.getRoot(), getEncryptedPropertyStreamName(), fs);
/* 353:    */     }
/* 354:    */     catch (GeneralSecurityException e)
/* 355:    */     {
/* 356:318 */       throw new IOException(e);
/* 357:    */     }
/* 358:    */     finally
/* 359:    */     {
/* 360:320 */       fs.close();
/* 361:    */     }
/* 362:    */   }
/* 363:    */   
/* 364:    */   protected void writePropertySet(String name, PropertySet set, NPOIFSFileSystem outFS)
/* 365:    */     throws IOException
/* 366:    */   {
/* 367:    */     try
/* 368:    */     {
/* 369:335 */       PropertySet mSet = new PropertySet(set);
/* 370:336 */       ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/* 371:    */       
/* 372:338 */       mSet.write(bOut);
/* 373:339 */       byte[] data = bOut.toByteArray();
/* 374:340 */       ByteArrayInputStream bIn = new ByteArrayInputStream(data);
/* 375:    */       
/* 376:    */ 
/* 377:343 */       outFS.createOrUpdateDocument(bIn, name);
/* 378:    */       
/* 379:345 */       logger.log(3, new Object[] { "Wrote property set " + name + " of size " + data.length });
/* 380:    */     }
/* 381:    */     catch (WritingNotSupportedException wnse)
/* 382:    */     {
/* 383:347 */       logger.log(7, new Object[] { "Couldn't write property set with name " + name + " as not supported by HPSF yet" });
/* 384:    */     }
/* 385:    */   }
/* 386:    */   
/* 387:    */   protected void validateInPlaceWritePossible()
/* 388:    */     throws IllegalStateException
/* 389:    */   {
/* 390:359 */     if (this.directory == null) {
/* 391:360 */       throw new IllegalStateException("Newly created Document, cannot save in-place");
/* 392:    */     }
/* 393:362 */     if (this.directory.getParent() != null) {
/* 394:363 */       throw new IllegalStateException("This is not the root Document, cannot save embedded resource in-place");
/* 395:    */     }
/* 396:365 */     if ((this.directory.getFileSystem() == null) || (!this.directory.getFileSystem().isInPlaceWriteable())) {
/* 397:367 */       throw new IllegalStateException("Opened read-only or via an InputStream, a Writeable File is required");
/* 398:    */     }
/* 399:    */   }
/* 400:    */   
/* 401:    */   public abstract void write()
/* 402:    */     throws IOException;
/* 403:    */   
/* 404:    */   public abstract void write(File paramFile)
/* 405:    */     throws IOException;
/* 406:    */   
/* 407:    */   public abstract void write(OutputStream paramOutputStream)
/* 408:    */     throws IOException;
/* 409:    */   
/* 410:    */   public void close()
/* 411:    */     throws IOException
/* 412:    */   {
/* 413:430 */     if ((this.directory != null) && 
/* 414:431 */       (this.directory.getNFileSystem() != null))
/* 415:    */     {
/* 416:432 */       this.directory.getNFileSystem().close();
/* 417:433 */       clearDirectory();
/* 418:    */     }
/* 419:    */   }
/* 420:    */   
/* 421:    */   @Internal
/* 422:    */   public DirectoryNode getDirectory()
/* 423:    */   {
/* 424:440 */     return this.directory;
/* 425:    */   }
/* 426:    */   
/* 427:    */   @Internal
/* 428:    */   protected void clearDirectory()
/* 429:    */   {
/* 430:448 */     this.directory = null;
/* 431:    */   }
/* 432:    */   
/* 433:    */   @Internal
/* 434:    */   protected boolean initDirectory()
/* 435:    */   {
/* 436:460 */     if (this.directory == null)
/* 437:    */     {
/* 438:461 */       this.directory = new NPOIFSFileSystem().getRoot();
/* 439:462 */       return true;
/* 440:    */     }
/* 441:464 */     return false;
/* 442:    */   }
/* 443:    */   
/* 444:    */   @Internal
/* 445:    */   protected DirectoryNode replaceDirectory(DirectoryNode newDirectory)
/* 446:    */   {
/* 447:476 */     DirectoryNode dn = this.directory;
/* 448:477 */     this.directory = newDirectory;
/* 449:478 */     return dn;
/* 450:    */   }
/* 451:    */   
/* 452:    */   protected String getEncryptedPropertyStreamName()
/* 453:    */   {
/* 454:485 */     return "encryption";
/* 455:    */   }
/* 456:    */   
/* 457:    */   public EncryptionInfo getEncryptionInfo()
/* 458:    */     throws IOException
/* 459:    */   {
/* 460:492 */     return null;
/* 461:    */   }
/* 462:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.POIDocument
 * JD-Core Version:    0.7.0.1
 */