/*   1:    */ package org.apache.poi.extractor;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import org.apache.poi.EncryptedDocumentException;
/*  11:    */ import org.apache.poi.POIOLE2TextExtractor;
/*  12:    */ import org.apache.poi.POITextExtractor;
/*  13:    */ import org.apache.poi.POIXMLTextExtractor;
/*  14:    */ import org.apache.poi.hsmf.MAPIMessage;
/*  15:    */ import org.apache.poi.hsmf.datatypes.AttachmentChunks;
/*  16:    */ import org.apache.poi.hsmf.datatypes.ByteChunk;
/*  17:    */ import org.apache.poi.hsmf.datatypes.DirectoryChunk;
/*  18:    */ import org.apache.poi.hsmf.extractor.OutlookTextExtactor;
/*  19:    */ import org.apache.poi.hssf.extractor.ExcelExtractor;
/*  20:    */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*  21:    */ import org.apache.poi.hwpf.extractor.WordExtractor;
/*  22:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  23:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  24:    */ import org.apache.poi.openxml4j.opc.PackageAccess;
/*  25:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  26:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  27:    */ import org.apache.poi.poifs.crypt.Decryptor;
/*  28:    */ import org.apache.poi.poifs.crypt.EncryptionInfo;
/*  29:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  30:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  31:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  32:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  33:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  34:    */ import org.apache.poi.poifs.filesystem.NotOLE2FileException;
/*  35:    */ import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
/*  36:    */ import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
/*  37:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  38:    */ import org.apache.poi.util.IOUtils;
/*  39:    */ import org.apache.poi.util.NotImplemented;
/*  40:    */ import org.apache.poi.util.POILogFactory;
/*  41:    */ import org.apache.poi.util.POILogger;
/*  42:    */ import org.apache.poi.xdgf.extractor.XDGFVisioExtractor;
/*  43:    */ import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
/*  44:    */ import org.apache.poi.xslf.usermodel.XSLFRelation;
/*  45:    */ import org.apache.poi.xslf.usermodel.XSLFSlideShow;
/*  46:    */ import org.apache.poi.xssf.extractor.XSSFBEventBasedExcelExtractor;
/*  47:    */ import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
/*  48:    */ import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
/*  49:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  50:    */ import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
/*  51:    */ import org.apache.poi.xwpf.usermodel.XWPFRelation;
/*  52:    */ import org.apache.xmlbeans.XmlException;
/*  53:    */ 
/*  54:    */ public class ExtractorFactory
/*  55:    */ {
/*  56: 81 */   private static final POILogger logger = POILogFactory.getLogger(ExtractorFactory.class);
/*  57:    */   public static final String CORE_DOCUMENT_REL = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";
/*  58:    */   protected static final String VISIO_DOCUMENT_REL = "http://schemas.microsoft.com/visio/2010/relationships/document";
/*  59:    */   protected static final String STRICT_DOCUMENT_REL = "http://purl.oclc.org/ooxml/officeDocument/relationships/officeDocument";
/*  60:    */   
/*  61:    */   public static boolean getThreadPrefersEventExtractors()
/*  62:    */   {
/*  63: 93 */     return OLE2ExtractorFactory.getThreadPrefersEventExtractors();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static Boolean getAllThreadsPreferEventExtractors()
/*  67:    */   {
/*  68:102 */     return OLE2ExtractorFactory.getAllThreadsPreferEventExtractors();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static void setThreadPrefersEventExtractors(boolean preferEventExtractors)
/*  72:    */   {
/*  73:110 */     OLE2ExtractorFactory.setThreadPrefersEventExtractors(preferEventExtractors);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static void setAllThreadsPreferEventExtractors(Boolean preferEventExtractors)
/*  77:    */   {
/*  78:118 */     OLE2ExtractorFactory.setAllThreadsPreferEventExtractors(preferEventExtractors);
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected static boolean getPreferEventExtractor()
/*  82:    */   {
/*  83:126 */     return OLE2ExtractorFactory.getPreferEventExtractor();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static POITextExtractor createExtractor(File f)
/*  87:    */     throws IOException, OpenXML4JException, XmlException
/*  88:    */   {
/*  89:130 */     NPOIFSFileSystem fs = null;
/*  90:    */     try
/*  91:    */     {
/*  92:132 */       fs = new NPOIFSFileSystem(f);
/*  93:133 */       if (fs.getRoot().hasEntry("EncryptedPackage")) {
/*  94:134 */         return createEncyptedOOXMLExtractor(fs);
/*  95:    */       }
/*  96:136 */       POIOLE2TextExtractor extractor = createExtractor(fs);
/*  97:137 */       extractor.setFilesystem(fs);
/*  98:138 */       return extractor;
/*  99:    */     }
/* 100:    */     catch (OfficeXmlFileException e)
/* 101:    */     {
/* 102:142 */       IOUtils.closeQuietly(fs);
/* 103:143 */       return createExtractor(OPCPackage.open(f.toString(), PackageAccess.READ));
/* 104:    */     }
/* 105:    */     catch (NotOLE2FileException ne)
/* 106:    */     {
/* 107:147 */       IOUtils.closeQuietly(fs);
/* 108:148 */       throw new IllegalArgumentException("Your File was neither an OLE2 file, nor an OOXML file");
/* 109:    */     }
/* 110:    */     catch (OpenXML4JException e)
/* 111:    */     {
/* 112:152 */       IOUtils.closeQuietly(fs);
/* 113:153 */       throw e;
/* 114:    */     }
/* 115:    */     catch (XmlException e)
/* 116:    */     {
/* 117:157 */       IOUtils.closeQuietly(fs);
/* 118:158 */       throw e;
/* 119:    */     }
/* 120:    */     catch (IOException e)
/* 121:    */     {
/* 122:162 */       IOUtils.closeQuietly(fs);
/* 123:163 */       throw e;
/* 124:    */     }
/* 125:    */     catch (RuntimeException e)
/* 126:    */     {
/* 127:167 */       IOUtils.closeQuietly(fs);
/* 128:168 */       throw e;
/* 129:    */     }
/* 130:    */     catch (Error e)
/* 131:    */     {
/* 132:171 */       IOUtils.closeQuietly(fs);
/* 133:172 */       throw e;
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static POITextExtractor createExtractor(InputStream inp)
/* 138:    */     throws IOException, OpenXML4JException, XmlException
/* 139:    */   {
/* 140:177 */     InputStream is = FileMagic.prepareToCheckMagic(inp);
/* 141:    */     
/* 142:179 */     FileMagic fm = FileMagic.valueOf(is);
/* 143:181 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$FileMagic[fm.ordinal()])
/* 144:    */     {
/* 145:    */     case 1: 
/* 146:183 */       NPOIFSFileSystem fs = new NPOIFSFileSystem(is);
/* 147:184 */       boolean isEncrypted = fs.getRoot().hasEntry("EncryptedPackage");
/* 148:185 */       return isEncrypted ? createEncyptedOOXMLExtractor(fs) : createExtractor(fs);
/* 149:    */     case 2: 
/* 150:187 */       return createExtractor(OPCPackage.open(is));
/* 151:    */     }
/* 152:189 */     throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static POIXMLTextExtractor createExtractor(OPCPackage pkg)
/* 156:    */     throws IOException, OpenXML4JException, XmlException
/* 157:    */   {
/* 158:    */     try
/* 159:    */     {
/* 160:207 */       PackageRelationshipCollection core = pkg.getRelationshipsByType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
/* 161:210 */       if (core.size() == 0) {
/* 162:212 */         core = pkg.getRelationshipsByType("http://purl.oclc.org/ooxml/officeDocument/relationships/officeDocument");
/* 163:    */       }
/* 164:214 */       if (core.size() == 0)
/* 165:    */       {
/* 166:216 */         core = pkg.getRelationshipsByType("http://schemas.microsoft.com/visio/2010/relationships/document");
/* 167:217 */         if (core.size() == 1) {
/* 168:218 */           return new XDGFVisioExtractor(pkg);
/* 169:    */         }
/* 170:    */       }
/* 171:222 */       if (core.size() != 1) {
/* 172:223 */         throw new IllegalArgumentException("Invalid OOXML Package received - expected 1 core document, found " + core.size());
/* 173:    */       }
/* 174:227 */       PackagePart corePart = pkg.getPart(core.getRelationship(0));
/* 175:228 */       String contentType = corePart.getContentType();
/* 176:231 */       for (XSSFRelation rel : XSSFExcelExtractor.SUPPORTED_TYPES) {
/* 177:232 */         if (rel.getContentType().equals(contentType))
/* 178:    */         {
/* 179:233 */           if (getPreferEventExtractor()) {
/* 180:234 */             return new XSSFEventBasedExcelExtractor(pkg);
/* 181:    */           }
/* 182:236 */           return new XSSFExcelExtractor(pkg);
/* 183:    */         }
/* 184:    */       }
/* 185:241 */       for (XWPFRelation rel : XWPFWordExtractor.SUPPORTED_TYPES) {
/* 186:242 */         if (rel.getContentType().equals(contentType)) {
/* 187:243 */           return new XWPFWordExtractor(pkg);
/* 188:    */         }
/* 189:    */       }
/* 190:248 */       for (XSLFRelation rel : XSLFPowerPointExtractor.SUPPORTED_TYPES) {
/* 191:249 */         if (rel.getContentType().equals(contentType)) {
/* 192:250 */           return new XSLFPowerPointExtractor(pkg);
/* 193:    */         }
/* 194:    */       }
/* 195:255 */       if (XSLFRelation.THEME_MANAGER.getContentType().equals(contentType)) {
/* 196:256 */         return new XSLFPowerPointExtractor(new XSLFSlideShow(pkg));
/* 197:    */       }
/* 198:260 */       for (XSSFRelation rel : XSSFBEventBasedExcelExtractor.SUPPORTED_TYPES) {
/* 199:261 */         if (rel.getContentType().equals(contentType)) {
/* 200:262 */           return new XSSFBEventBasedExcelExtractor(pkg);
/* 201:    */         }
/* 202:    */       }
/* 203:266 */       throw new IllegalArgumentException("No supported documents found in the OOXML package (found " + contentType + ")");
/* 204:    */     }
/* 205:    */     catch (IOException e)
/* 206:    */     {
/* 207:271 */       pkg.revert();
/* 208:272 */       throw e;
/* 209:    */     }
/* 210:    */     catch (OpenXML4JException e)
/* 211:    */     {
/* 212:276 */       pkg.revert();
/* 213:277 */       throw e;
/* 214:    */     }
/* 215:    */     catch (XmlException e)
/* 216:    */     {
/* 217:281 */       pkg.revert();
/* 218:282 */       throw e;
/* 219:    */     }
/* 220:    */     catch (RuntimeException e)
/* 221:    */     {
/* 222:286 */       pkg.revert();
/* 223:287 */       throw e;
/* 224:    */     }
/* 225:    */     catch (Error e)
/* 226:    */     {
/* 227:291 */       pkg.revert();
/* 228:292 */       throw e;
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   public static POIOLE2TextExtractor createExtractor(POIFSFileSystem fs)
/* 233:    */     throws IOException, OpenXML4JException, XmlException
/* 234:    */   {
/* 235:297 */     return OLE2ExtractorFactory.createExtractor(fs);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public static POIOLE2TextExtractor createExtractor(NPOIFSFileSystem fs)
/* 239:    */     throws IOException, OpenXML4JException, XmlException
/* 240:    */   {
/* 241:300 */     return OLE2ExtractorFactory.createExtractor(fs);
/* 242:    */   }
/* 243:    */   
/* 244:    */   public static POIOLE2TextExtractor createExtractor(OPOIFSFileSystem fs)
/* 245:    */     throws IOException, OpenXML4JException, XmlException
/* 246:    */   {
/* 247:303 */     return OLE2ExtractorFactory.createExtractor(fs);
/* 248:    */   }
/* 249:    */   
/* 250:    */   public static POITextExtractor createExtractor(DirectoryNode poifsDir)
/* 251:    */     throws IOException, OpenXML4JException, XmlException
/* 252:    */   {
/* 253:309 */     for (String entryName : poifsDir.getEntryNames()) {
/* 254:310 */       if (entryName.equals("Package"))
/* 255:    */       {
/* 256:311 */         OPCPackage pkg = OPCPackage.open(poifsDir.createDocumentInputStream("Package"));
/* 257:312 */         return createExtractor(pkg);
/* 258:    */       }
/* 259:    */     }
/* 260:317 */     return OLE2ExtractorFactory.createExtractor(poifsDir);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public static POITextExtractor[] getEmbededDocsTextExtractors(POIOLE2TextExtractor ext)
/* 264:    */     throws IOException, OpenXML4JException, XmlException
/* 265:    */   {
/* 266:329 */     ArrayList<Entry> dirs = new ArrayList();
/* 267:    */     
/* 268:331 */     ArrayList<InputStream> nonPOIFS = new ArrayList();
/* 269:    */     
/* 270:    */ 
/* 271:334 */     DirectoryEntry root = ext.getRoot();
/* 272:335 */     if (root == null) {
/* 273:336 */       throw new IllegalStateException("The extractor didn't know which POIFS it came from!");
/* 274:    */     }
/* 275:339 */     if ((ext instanceof ExcelExtractor))
/* 276:    */     {
/* 277:341 */       Iterator<Entry> it = root.getEntries();
/* 278:342 */       while (it.hasNext())
/* 279:    */       {
/* 280:343 */         Entry entry = (Entry)it.next();
/* 281:344 */         if (entry.getName().startsWith("MBD")) {
/* 282:345 */           dirs.add(entry);
/* 283:    */         }
/* 284:    */       }
/* 285:    */     }
/* 286:348 */     else if ((ext instanceof WordExtractor))
/* 287:    */     {
/* 288:    */       try
/* 289:    */       {
/* 290:351 */         DirectoryEntry op = (DirectoryEntry)root.getEntry("ObjectPool");
/* 291:352 */         Iterator<Entry> it = op.getEntries();
/* 292:353 */         while (it.hasNext())
/* 293:    */         {
/* 294:354 */           Entry entry = (Entry)it.next();
/* 295:355 */           if (entry.getName().startsWith("_")) {
/* 296:356 */             dirs.add(entry);
/* 297:    */           }
/* 298:    */         }
/* 299:    */       }
/* 300:    */       catch (FileNotFoundException e)
/* 301:    */       {
/* 302:360 */         logger.log(3, new Object[] { "Ignoring FileNotFoundException while extracting Word document", e.getLocalizedMessage() });
/* 303:    */       }
/* 304:    */     }
/* 305:366 */     else if ((ext instanceof OutlookTextExtactor))
/* 306:    */     {
/* 307:368 */       MAPIMessage msg = ((OutlookTextExtactor)ext).getMAPIMessage();
/* 308:369 */       for (AttachmentChunks attachment : msg.getAttachmentFiles()) {
/* 309:370 */         if (attachment.getAttachData() != null)
/* 310:    */         {
/* 311:371 */           byte[] data = attachment.getAttachData().getValue();
/* 312:372 */           nonPOIFS.add(new ByteArrayInputStream(data));
/* 313:    */         }
/* 314:373 */         else if (attachment.getAttachmentDirectory() != null)
/* 315:    */         {
/* 316:374 */           dirs.add(attachment.getAttachmentDirectory().getDirectory());
/* 317:    */         }
/* 318:    */       }
/* 319:    */     }
/* 320:380 */     if ((dirs.size() == 0) && (nonPOIFS.size() == 0)) {
/* 321:381 */       return new POITextExtractor[0];
/* 322:    */     }
/* 323:384 */     ArrayList<POITextExtractor> textExtractors = new ArrayList();
/* 324:385 */     for (Entry dir : dirs) {
/* 325:386 */       textExtractors.add(createExtractor((DirectoryNode)dir));
/* 326:    */     }
/* 327:388 */     for (InputStream nonPOIF : nonPOIFS) {
/* 328:    */       try
/* 329:    */       {
/* 330:390 */         textExtractors.add(createExtractor(nonPOIF));
/* 331:    */       }
/* 332:    */       catch (IllegalArgumentException e)
/* 333:    */       {
/* 334:394 */         logger.log(3, new Object[] { "Format not supported yet", e.getLocalizedMessage() });
/* 335:    */       }
/* 336:    */       catch (XmlException e)
/* 337:    */       {
/* 338:396 */         throw new IOException(e.getMessage(), e);
/* 339:    */       }
/* 340:    */       catch (OpenXML4JException e)
/* 341:    */       {
/* 342:398 */         throw new IOException(e.getMessage(), e);
/* 343:    */       }
/* 344:    */     }
/* 345:401 */     return (POITextExtractor[])textExtractors.toArray(new POITextExtractor[textExtractors.size()]);
/* 346:    */   }
/* 347:    */   
/* 348:    */   @NotImplemented
/* 349:    */   public static POITextExtractor[] getEmbededDocsTextExtractors(POIXMLTextExtractor ext)
/* 350:    */   {
/* 351:414 */     throw new IllegalStateException("Not yet supported");
/* 352:    */   }
/* 353:    */   
/* 354:    */   private static POIXMLTextExtractor createEncyptedOOXMLExtractor(NPOIFSFileSystem fs)
/* 355:    */     throws IOException
/* 356:    */   {
/* 357:419 */     String pass = Biff8EncryptionKey.getCurrentUserPassword();
/* 358:420 */     if (pass == null) {
/* 359:421 */       pass = "VelvetSweatshop";
/* 360:    */     }
/* 361:424 */     EncryptionInfo ei = new EncryptionInfo(fs);
/* 362:425 */     Decryptor dec = ei.getDecryptor();
/* 363:426 */     InputStream is = null;
/* 364:    */     try
/* 365:    */     {
/* 366:428 */       if (!dec.verifyPassword(pass)) {
/* 367:429 */         throw new EncryptedDocumentException("Invalid password specified - use Biff8EncryptionKey.setCurrentUserPassword() before calling extractor");
/* 368:    */       }
/* 369:431 */       is = dec.getDataStream(fs);
/* 370:432 */       return createExtractor(OPCPackage.open(is));
/* 371:    */     }
/* 372:    */     catch (IOException e)
/* 373:    */     {
/* 374:434 */       throw e;
/* 375:    */     }
/* 376:    */     catch (Exception e)
/* 377:    */     {
/* 378:436 */       throw new EncryptedDocumentException(e);
/* 379:    */     }
/* 380:    */     finally
/* 381:    */     {
/* 382:438 */       IOUtils.closeQuietly(is);
/* 383:    */     }
/* 384:    */   }
/* 385:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.extractor.ExtractorFactory
 * JD-Core Version:    0.7.0.1
 */