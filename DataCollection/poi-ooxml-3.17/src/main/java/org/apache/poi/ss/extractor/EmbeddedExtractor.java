/*   1:    */ package org.apache.poi.ss.extractor;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import org.apache.poi.hpsf.ClassID;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  14:    */ import org.apache.poi.poifs.filesystem.DocumentInputStream;
/*  15:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  16:    */ import org.apache.poi.poifs.filesystem.Ole10Native;
/*  17:    */ import org.apache.poi.poifs.filesystem.Ole10NativeException;
/*  18:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  19:    */ import org.apache.poi.ss.usermodel.Drawing;
/*  20:    */ import org.apache.poi.ss.usermodel.ObjectData;
/*  21:    */ import org.apache.poi.ss.usermodel.Picture;
/*  22:    */ import org.apache.poi.ss.usermodel.PictureData;
/*  23:    */ import org.apache.poi.ss.usermodel.Shape;
/*  24:    */ import org.apache.poi.ss.usermodel.ShapeContainer;
/*  25:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  26:    */ import org.apache.poi.util.IOUtils;
/*  27:    */ import org.apache.poi.util.LocaleUtil;
/*  28:    */ import org.apache.poi.util.POILogFactory;
/*  29:    */ import org.apache.poi.util.POILogger;
/*  30:    */ import org.apache.poi.util.StringUtil;
/*  31:    */ import org.apache.poi.xssf.usermodel.XSSFObjectData;
/*  32:    */ 
/*  33:    */ public class EmbeddedExtractor
/*  34:    */   implements Iterable<EmbeddedExtractor>
/*  35:    */ {
/*  36: 59 */   private static final POILogger LOG = POILogFactory.getLogger(EmbeddedExtractor.class);
/*  37:    */   private static final String CONTENT_TYPE_BYTES = "binary/octet-stream";
/*  38:    */   private static final String CONTENT_TYPE_PDF = "application/pdf";
/*  39:    */   private static final String CONTENT_TYPE_DOC = "application/msword";
/*  40:    */   private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
/*  41:    */   
/*  42:    */   public Iterator<EmbeddedExtractor> iterator()
/*  43:    */   {
/*  44: 72 */     EmbeddedExtractor[] ee = { new Ole10Extractor(), new PdfExtractor(), new BiffExtractor(), new OOXMLExtractor(), new FsExtractor() };
/*  45:    */     
/*  46:    */ 
/*  47: 75 */     return Arrays.asList(ee).iterator();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public EmbeddedData extractOne(DirectoryNode src)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53: 79 */     for (EmbeddedExtractor ee : this) {
/*  54: 80 */       if (ee.canExtract(src)) {
/*  55: 81 */         return ee.extract(src);
/*  56:    */       }
/*  57:    */     }
/*  58: 84 */     return null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public EmbeddedData extractOne(Picture src)
/*  62:    */     throws IOException
/*  63:    */   {
/*  64: 88 */     for (EmbeddedExtractor ee : this) {
/*  65: 89 */       if (ee.canExtract(src)) {
/*  66: 90 */         return ee.extract(src);
/*  67:    */       }
/*  68:    */     }
/*  69: 93 */     return null;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public List<EmbeddedData> extractAll(Sheet sheet)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75: 97 */     Drawing<?> patriarch = sheet.getDrawingPatriarch();
/*  76: 98 */     if (null == patriarch) {
/*  77: 99 */       return Collections.emptyList();
/*  78:    */     }
/*  79:101 */     List<EmbeddedData> embeddings = new ArrayList();
/*  80:102 */     extractAll(patriarch, embeddings);
/*  81:103 */     return embeddings;
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected void extractAll(ShapeContainer<?> parent, List<EmbeddedData> embeddings)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:107 */     for (Shape shape : parent)
/*  88:    */     {
/*  89:108 */       EmbeddedData data = null;
/*  90:109 */       if ((shape instanceof ObjectData))
/*  91:    */       {
/*  92:110 */         ObjectData od = (ObjectData)shape;
/*  93:    */         try
/*  94:    */         {
/*  95:112 */           if (od.hasDirectoryEntry())
/*  96:    */           {
/*  97:113 */             data = extractOne((DirectoryNode)od.getDirectory());
/*  98:    */           }
/*  99:    */           else
/* 100:    */           {
/* 101:115 */             String contentType = "binary/octet-stream";
/* 102:116 */             if ((od instanceof XSSFObjectData)) {
/* 103:117 */               contentType = ((XSSFObjectData)od).getObjectPart().getContentType();
/* 104:    */             }
/* 105:119 */             data = new EmbeddedData(od.getFileName(), od.getObjectData(), contentType);
/* 106:    */           }
/* 107:    */         }
/* 108:    */         catch (Exception e)
/* 109:    */         {
/* 110:122 */           LOG.log(5, new Object[] { "Entry not found / readable - ignoring OLE embedding", e });
/* 111:    */         }
/* 112:    */       }
/* 113:124 */       else if ((shape instanceof Picture))
/* 114:    */       {
/* 115:125 */         data = extractOne((Picture)shape);
/* 116:    */       }
/* 117:126 */       else if ((shape instanceof ShapeContainer))
/* 118:    */       {
/* 119:127 */         extractAll((ShapeContainer)shape, embeddings);
/* 120:    */       }
/* 121:130 */       if (data != null)
/* 122:    */       {
/* 123:134 */         data.setShape(shape);
/* 124:135 */         String filename = data.getFilename();
/* 125:136 */         String extension = (filename == null) || (filename.lastIndexOf('.') == -1) ? ".bin" : filename.substring(filename.lastIndexOf('.'));
/* 126:139 */         if ((filename == null) || ("".equals(filename)) || (filename.startsWith("MBD")) || (filename.startsWith("Root Entry")))
/* 127:    */         {
/* 128:140 */           filename = shape.getShapeName();
/* 129:141 */           if (filename != null) {
/* 130:142 */             filename = filename + extension;
/* 131:    */           }
/* 132:    */         }
/* 133:146 */         if ((filename == null) || ("".equals(filename))) {
/* 134:147 */           filename = "picture_" + embeddings.size() + extension;
/* 135:    */         }
/* 136:149 */         filename = filename.trim();
/* 137:150 */         data.setFilename(filename);
/* 138:    */         
/* 139:152 */         embeddings.add(data);
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean canExtract(DirectoryNode source)
/* 145:    */   {
/* 146:158 */     return false;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public boolean canExtract(Picture source)
/* 150:    */   {
/* 151:162 */     return false;
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected EmbeddedData extract(DirectoryNode dn)
/* 155:    */     throws IOException
/* 156:    */   {
/* 157:166 */     assert (canExtract(dn));
/* 158:167 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(20000);
/* 159:168 */     POIFSFileSystem dest = new POIFSFileSystem();
/* 160:    */     try
/* 161:    */     {
/* 162:170 */       copyNodes(dn, dest.getRoot());
/* 163:    */       
/* 164:172 */       dest.writeFilesystem(bos);
/* 165:    */     }
/* 166:    */     finally
/* 167:    */     {
/* 168:174 */       dest.close();
/* 169:    */     }
/* 170:177 */     return new EmbeddedData(dn.getName(), bos.toByteArray(), "binary/octet-stream");
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected EmbeddedData extract(Picture source)
/* 174:    */     throws IOException
/* 175:    */   {
/* 176:181 */     return null;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static class Ole10Extractor
/* 180:    */     extends EmbeddedExtractor
/* 181:    */   {
/* 182:    */     public boolean canExtract(DirectoryNode dn)
/* 183:    */     {
/* 184:187 */       ClassID clsId = dn.getStorageClsid();
/* 185:188 */       return ClassID.OLE10_PACKAGE.equals(clsId);
/* 186:    */     }
/* 187:    */     
/* 188:    */     public EmbeddedData extract(DirectoryNode dn)
/* 189:    */       throws IOException
/* 190:    */     {
/* 191:    */       try
/* 192:    */       {
/* 193:195 */         Ole10Native ole10 = Ole10Native.createFromEmbeddedOleObject(dn);
/* 194:196 */         return new EmbeddedData(ole10.getFileName(), ole10.getDataBuffer(), "binary/octet-stream");
/* 195:    */       }
/* 196:    */       catch (Ole10NativeException e)
/* 197:    */       {
/* 198:198 */         throw new IOException(e);
/* 199:    */       }
/* 200:    */     }
/* 201:    */   }
/* 202:    */   
/* 203:    */   static class PdfExtractor
/* 204:    */     extends EmbeddedExtractor
/* 205:    */   {
/* 206:204 */     static ClassID PdfClassID = new ClassID("{B801CA65-A1FC-11D0-85AD-444553540000}");
/* 207:    */     
/* 208:    */     public boolean canExtract(DirectoryNode dn)
/* 209:    */     {
/* 210:207 */       ClassID clsId = dn.getStorageClsid();
/* 211:208 */       return (PdfClassID.equals(clsId)) || (dn.hasEntry("CONTENTS"));
/* 212:    */     }
/* 213:    */     
/* 214:    */     public EmbeddedData extract(DirectoryNode dn)
/* 215:    */       throws IOException
/* 216:    */     {
/* 217:214 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 218:215 */       InputStream is = dn.createDocumentInputStream("CONTENTS");
/* 219:216 */       IOUtils.copy(is, bos);
/* 220:217 */       is.close();
/* 221:218 */       return new EmbeddedData(dn.getName() + ".pdf", bos.toByteArray(), "application/pdf");
/* 222:    */     }
/* 223:    */     
/* 224:    */     public boolean canExtract(Picture source)
/* 225:    */     {
/* 226:223 */       PictureData pd = source.getPictureData();
/* 227:224 */       return (pd != null) && (pd.getPictureType() == 2);
/* 228:    */     }
/* 229:    */     
/* 230:    */     protected EmbeddedData extract(Picture source)
/* 231:    */       throws IOException
/* 232:    */     {
/* 233:237 */       PictureData pd = source.getPictureData();
/* 234:238 */       if ((pd == null) || (pd.getPictureType() != 2)) {
/* 235:239 */         return null;
/* 236:    */       }
/* 237:243 */       byte[] pictureBytes = pd.getData();
/* 238:244 */       int idxStart = EmbeddedExtractor.indexOf(pictureBytes, 0, "%PDF-".getBytes(LocaleUtil.CHARSET_1252));
/* 239:245 */       if (idxStart == -1) {
/* 240:246 */         return null;
/* 241:    */       }
/* 242:249 */       int idxEnd = EmbeddedExtractor.indexOf(pictureBytes, idxStart, "%%EOF".getBytes(LocaleUtil.CHARSET_1252));
/* 243:250 */       if (idxEnd == -1) {
/* 244:251 */         return null;
/* 245:    */       }
/* 246:254 */       int pictureBytesLen = idxEnd - idxStart + 6;
/* 247:255 */       byte[] pdfBytes = new byte[pictureBytesLen];
/* 248:256 */       System.arraycopy(pictureBytes, idxStart, pdfBytes, 0, pictureBytesLen);
/* 249:257 */       String filename = source.getShapeName().trim();
/* 250:258 */       if (!StringUtil.endsWithIgnoreCase(filename, ".pdf")) {
/* 251:259 */         filename = filename + ".pdf";
/* 252:    */       }
/* 253:261 */       return new EmbeddedData(filename, pdfBytes, "application/pdf");
/* 254:    */     }
/* 255:    */   }
/* 256:    */   
/* 257:    */   static class OOXMLExtractor
/* 258:    */     extends EmbeddedExtractor
/* 259:    */   {
/* 260:    */     public boolean canExtract(DirectoryNode dn)
/* 261:    */     {
/* 262:270 */       return dn.hasEntry("package");
/* 263:    */     }
/* 264:    */     
/* 265:    */     public EmbeddedData extract(DirectoryNode dn)
/* 266:    */       throws IOException
/* 267:    */     {
/* 268:276 */       ClassID clsId = dn.getStorageClsid();
/* 269:    */       String contentType;
/* 270:    */       String ext;
/* 271:    */       String contentType;
/* 272:279 */       if (ClassID.WORD2007.equals(clsId))
/* 273:    */       {
/* 274:280 */         String ext = ".docx";
/* 275:281 */         contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
/* 276:    */       }
/* 277:    */       else
/* 278:    */       {
/* 279:    */         String contentType;
/* 280:282 */         if (ClassID.WORD2007_MACRO.equals(clsId))
/* 281:    */         {
/* 282:283 */           String ext = ".docm";
/* 283:284 */           contentType = "application/vnd.ms-word.document.macroEnabled.12";
/* 284:    */         }
/* 285:    */         else
/* 286:    */         {
/* 287:    */           String contentType;
/* 288:285 */           if ((ClassID.EXCEL2007.equals(clsId)) || (ClassID.EXCEL2003.equals(clsId)) || (ClassID.EXCEL2010.equals(clsId)))
/* 289:    */           {
/* 290:286 */             String ext = ".xlsx";
/* 291:287 */             contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
/* 292:    */           }
/* 293:    */           else
/* 294:    */           {
/* 295:    */             String contentType;
/* 296:288 */             if (ClassID.EXCEL2007_MACRO.equals(clsId))
/* 297:    */             {
/* 298:289 */               String ext = ".xlsm";
/* 299:290 */               contentType = "application/vnd.ms-excel.sheet.macroEnabled.12";
/* 300:    */             }
/* 301:    */             else
/* 302:    */             {
/* 303:    */               String contentType;
/* 304:291 */               if (ClassID.EXCEL2007_XLSB.equals(clsId))
/* 305:    */               {
/* 306:292 */                 String ext = ".xlsb";
/* 307:293 */                 contentType = "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
/* 308:    */               }
/* 309:    */               else
/* 310:    */               {
/* 311:    */                 String contentType;
/* 312:294 */                 if (ClassID.POWERPOINT2007.equals(clsId))
/* 313:    */                 {
/* 314:295 */                   String ext = ".pptx";
/* 315:296 */                   contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
/* 316:    */                 }
/* 317:    */                 else
/* 318:    */                 {
/* 319:    */                   String contentType;
/* 320:297 */                   if (ClassID.POWERPOINT2007_MACRO.equals(clsId))
/* 321:    */                   {
/* 322:298 */                     String ext = ".ppsm";
/* 323:299 */                     contentType = "application/vnd.ms-powerpoint.slideshow.macroEnabled.12";
/* 324:    */                   }
/* 325:    */                   else
/* 326:    */                   {
/* 327:301 */                     ext = ".zip";
/* 328:302 */                     contentType = "application/zip";
/* 329:    */                   }
/* 330:    */                 }
/* 331:    */               }
/* 332:    */             }
/* 333:    */           }
/* 334:    */         }
/* 335:    */       }
/* 336:305 */       DocumentInputStream dis = dn.createDocumentInputStream("package");
/* 337:306 */       byte[] data = IOUtils.toByteArray(dis);
/* 338:307 */       dis.close();
/* 339:    */       
/* 340:309 */       return new EmbeddedData(dn.getName() + ext, data, contentType);
/* 341:    */     }
/* 342:    */   }
/* 343:    */   
/* 344:    */   static class BiffExtractor
/* 345:    */     extends EmbeddedExtractor
/* 346:    */   {
/* 347:    */     public boolean canExtract(DirectoryNode dn)
/* 348:    */     {
/* 349:316 */       return (canExtractExcel(dn)) || (canExtractWord(dn));
/* 350:    */     }
/* 351:    */     
/* 352:    */     protected boolean canExtractExcel(DirectoryNode dn)
/* 353:    */     {
/* 354:320 */       ClassID clsId = dn.getStorageClsid();
/* 355:321 */       return (ClassID.EXCEL95.equals(clsId)) || (ClassID.EXCEL97.equals(clsId)) || (dn.hasEntry("Workbook"));
/* 356:    */     }
/* 357:    */     
/* 358:    */     protected boolean canExtractWord(DirectoryNode dn)
/* 359:    */     {
/* 360:327 */       ClassID clsId = dn.getStorageClsid();
/* 361:328 */       return (ClassID.WORD95.equals(clsId)) || (ClassID.WORD97.equals(clsId)) || (dn.hasEntry("WordDocument"));
/* 362:    */     }
/* 363:    */     
/* 364:    */     public EmbeddedData extract(DirectoryNode dn)
/* 365:    */       throws IOException
/* 366:    */     {
/* 367:335 */       EmbeddedData ed = super.extract(dn);
/* 368:336 */       if (canExtractExcel(dn))
/* 369:    */       {
/* 370:337 */         ed.setFilename(dn.getName() + ".xls");
/* 371:338 */         ed.setContentType("application/vnd.ms-excel");
/* 372:    */       }
/* 373:339 */       else if (canExtractWord(dn))
/* 374:    */       {
/* 375:340 */         ed.setFilename(dn.getName() + ".doc");
/* 376:341 */         ed.setContentType("application/msword");
/* 377:    */       }
/* 378:344 */       return ed;
/* 379:    */     }
/* 380:    */   }
/* 381:    */   
/* 382:    */   static class FsExtractor
/* 383:    */     extends EmbeddedExtractor
/* 384:    */   {
/* 385:    */     public boolean canExtract(DirectoryNode dn)
/* 386:    */     {
/* 387:351 */       return true;
/* 388:    */     }
/* 389:    */     
/* 390:    */     public EmbeddedData extract(DirectoryNode dn)
/* 391:    */       throws IOException
/* 392:    */     {
/* 393:355 */       EmbeddedData ed = super.extract(dn);
/* 394:356 */       ed.setFilename(dn.getName() + ".ole");
/* 395:    */       
/* 396:358 */       return ed;
/* 397:    */     }
/* 398:    */   }
/* 399:    */   
/* 400:    */   protected static void copyNodes(DirectoryNode src, DirectoryNode dest)
/* 401:    */     throws IOException
/* 402:    */   {
/* 403:363 */     for (Entry e : src) {
/* 404:364 */       if ((e instanceof DirectoryNode))
/* 405:    */       {
/* 406:365 */         DirectoryNode srcDir = (DirectoryNode)e;
/* 407:366 */         DirectoryNode destDir = (DirectoryNode)dest.createDirectory(srcDir.getName());
/* 408:367 */         destDir.setStorageClsid(srcDir.getStorageClsid());
/* 409:368 */         copyNodes(srcDir, destDir);
/* 410:    */       }
/* 411:    */       else
/* 412:    */       {
/* 413:370 */         InputStream is = src.createDocumentInputStream(e);
/* 414:    */         try
/* 415:    */         {
/* 416:372 */           dest.createDocument(e.getName(), is);
/* 417:    */         }
/* 418:    */         finally
/* 419:    */         {
/* 420:374 */           is.close();
/* 421:    */         }
/* 422:    */       }
/* 423:    */     }
/* 424:    */   }
/* 425:    */   
/* 426:    */   private static int indexOf(byte[] data, int offset, byte[] pattern)
/* 427:    */   {
/* 428:387 */     int[] failure = computeFailure(pattern);
/* 429:    */     
/* 430:389 */     int j = 0;
/* 431:390 */     if (data.length == 0) {
/* 432:391 */       return -1;
/* 433:    */     }
/* 434:394 */     for (int i = offset; i < data.length; i++)
/* 435:    */     {
/* 436:395 */       while ((j > 0) && (pattern[j] != data[i])) {
/* 437:396 */         j = failure[(j - 1)];
/* 438:    */       }
/* 439:398 */       if (pattern[j] == data[i]) {
/* 440:398 */         j++;
/* 441:    */       }
/* 442:399 */       if (j == pattern.length) {
/* 443:400 */         return i - pattern.length + 1;
/* 444:    */       }
/* 445:    */     }
/* 446:403 */     return -1;
/* 447:    */   }
/* 448:    */   
/* 449:    */   private static int[] computeFailure(byte[] pattern)
/* 450:    */   {
/* 451:411 */     int[] failure = new int[pattern.length];
/* 452:    */     
/* 453:413 */     int j = 0;
/* 454:414 */     for (int i = 1; i < pattern.length; i++)
/* 455:    */     {
/* 456:415 */       while ((j > 0) && (pattern[j] != pattern[i])) {
/* 457:416 */         j = failure[(j - 1)];
/* 458:    */       }
/* 459:418 */       if (pattern[j] == pattern[i]) {
/* 460:419 */         j++;
/* 461:    */       }
/* 462:421 */       failure[i] = j;
/* 463:    */     }
/* 464:424 */     return failure;
/* 465:    */   }
/* 466:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.ss.extractor.EmbeddedExtractor
 * JD-Core Version:    0.7.0.1
 */