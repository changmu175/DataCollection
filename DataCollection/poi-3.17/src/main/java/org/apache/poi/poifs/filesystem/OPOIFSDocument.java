/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  12:    */ import org.apache.poi.poifs.common.POIFSConstants;
/*  13:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*  14:    */ import org.apache.poi.poifs.property.DocumentProperty;
/*  15:    */ import org.apache.poi.poifs.property.Property;
/*  16:    */ import org.apache.poi.poifs.storage.BlockWritable;
/*  17:    */ import org.apache.poi.poifs.storage.DataInputBlock;
/*  18:    */ import org.apache.poi.poifs.storage.DocumentBlock;
/*  19:    */ import org.apache.poi.poifs.storage.ListManagedBlock;
/*  20:    */ import org.apache.poi.poifs.storage.RawDataBlock;
/*  21:    */ import org.apache.poi.poifs.storage.SmallDocumentBlock;
/*  22:    */ import org.apache.poi.util.HexDump;
/*  23:    */ 
/*  24:    */ public final class OPOIFSDocument
/*  25:    */   implements BATManaged, BlockWritable, POIFSViewable
/*  26:    */ {
/*  27: 47 */   private static final DocumentBlock[] EMPTY_BIG_BLOCK_ARRAY = new DocumentBlock[0];
/*  28: 48 */   private static final SmallDocumentBlock[] EMPTY_SMALL_BLOCK_ARRAY = new SmallDocumentBlock[0];
/*  29:    */   private DocumentProperty _property;
/*  30:    */   private int _size;
/*  31:    */   private final POIFSBigBlockSize _bigBigBlockSize;
/*  32:    */   private SmallBlockStore _small_store;
/*  33:    */   private BigBlockStore _big_store;
/*  34:    */   
/*  35:    */   public OPOIFSDocument(String name, RawDataBlock[] blocks, int length)
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 66 */     this._size = length;
/*  39: 67 */     if (blocks.length == 0) {
/*  40: 68 */       this._bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
/*  41:    */     } else {
/*  42: 70 */       this._bigBigBlockSize = (blocks[0].getBigBlockSize() == 512 ? POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS : POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS);
/*  43:    */     }
/*  44: 76 */     this._big_store = new BigBlockStore(this._bigBigBlockSize, convertRawBlocksToBigBlocks(blocks));
/*  45: 77 */     this._property = new DocumentProperty(name, this._size);
/*  46: 78 */     this._small_store = new SmallBlockStore(this._bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
/*  47: 79 */     this._property.setDocument(this);
/*  48:    */   }
/*  49:    */   
/*  50:    */   private static DocumentBlock[] convertRawBlocksToBigBlocks(ListManagedBlock[] blocks)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53: 84 */     DocumentBlock[] result = new DocumentBlock[blocks.length];
/*  54: 85 */     for (int i = 0; i < result.length; i++) {
/*  55: 86 */       result[i] = new DocumentBlock((RawDataBlock)blocks[i]);
/*  56:    */     }
/*  57: 88 */     return result;
/*  58:    */   }
/*  59:    */   
/*  60:    */   private static SmallDocumentBlock[] convertRawBlocksToSmallBlocks(ListManagedBlock[] blocks)
/*  61:    */   {
/*  62: 91 */     if ((blocks instanceof SmallDocumentBlock[])) {
/*  63: 92 */       return (SmallDocumentBlock[])blocks;
/*  64:    */     }
/*  65: 94 */     SmallDocumentBlock[] result = new SmallDocumentBlock[blocks.length];
/*  66: 95 */     System.arraycopy(blocks, 0, result, 0, blocks.length);
/*  67: 96 */     return result;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public OPOIFSDocument(String name, SmallDocumentBlock[] blocks, int length)
/*  71:    */   {
/*  72:107 */     this._size = length;
/*  73:109 */     if (blocks.length == 0) {
/*  74:110 */       this._bigBigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
/*  75:    */     } else {
/*  76:112 */       this._bigBigBlockSize = blocks[0].getBigBlockSize();
/*  77:    */     }
/*  78:115 */     this._big_store = new BigBlockStore(this._bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
/*  79:116 */     this._property = new DocumentProperty(name, this._size);
/*  80:117 */     this._small_store = new SmallBlockStore(this._bigBigBlockSize, blocks);
/*  81:118 */     this._property.setDocument(this);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public OPOIFSDocument(String name, POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks, int length)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:129 */     this._size = length;
/*  88:130 */     this._bigBigBlockSize = bigBlockSize;
/*  89:131 */     this._property = new DocumentProperty(name, this._size);
/*  90:132 */     this._property.setDocument(this);
/*  91:133 */     if (Property.isSmall(this._size))
/*  92:    */     {
/*  93:134 */       this._big_store = new BigBlockStore(bigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
/*  94:135 */       this._small_store = new SmallBlockStore(bigBlockSize, convertRawBlocksToSmallBlocks(blocks));
/*  95:    */     }
/*  96:    */     else
/*  97:    */     {
/*  98:137 */       this._big_store = new BigBlockStore(bigBlockSize, convertRawBlocksToBigBlocks(blocks));
/*  99:138 */       this._small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public OPOIFSDocument(String name, ListManagedBlock[] blocks, int length)
/* 104:    */     throws IOException
/* 105:    */   {
/* 106:142 */     this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, blocks, length);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public OPOIFSDocument(String name, POIFSBigBlockSize bigBlockSize, InputStream stream)
/* 110:    */     throws IOException
/* 111:    */   {
/* 112:152 */     List<DocumentBlock> blocks = new ArrayList();
/* 113:    */     
/* 114:154 */     this._size = 0;
/* 115:155 */     this._bigBigBlockSize = bigBlockSize;
/* 116:    */     for (;;)
/* 117:    */     {
/* 118:157 */       DocumentBlock block = new DocumentBlock(stream, bigBlockSize);
/* 119:158 */       int blockSize = block.size();
/* 120:160 */       if (blockSize > 0)
/* 121:    */       {
/* 122:161 */         blocks.add(block);
/* 123:162 */         this._size += blockSize;
/* 124:    */       }
/* 125:164 */       if (block.partiallyRead()) {
/* 126:    */         break;
/* 127:    */       }
/* 128:    */     }
/* 129:168 */     DocumentBlock[] bigBlocks = (DocumentBlock[])blocks.toArray(new DocumentBlock[blocks.size()]);
/* 130:    */     
/* 131:170 */     this._big_store = new BigBlockStore(bigBlockSize, bigBlocks);
/* 132:171 */     this._property = new DocumentProperty(name, this._size);
/* 133:172 */     this._property.setDocument(this);
/* 134:173 */     if (this._property.shouldUseSmallBlocks())
/* 135:    */     {
/* 136:174 */       this._small_store = new SmallBlockStore(bigBlockSize, SmallDocumentBlock.convert(bigBlockSize, bigBlocks, this._size));
/* 137:175 */       this._big_store = new BigBlockStore(bigBlockSize, new DocumentBlock[0]);
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:177 */       this._small_store = new SmallBlockStore(bigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public OPOIFSDocument(String name, InputStream stream)
/* 146:    */     throws IOException
/* 147:    */   {
/* 148:181 */     this(name, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, stream);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public OPOIFSDocument(String name, int size, POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, POIFSWriterListener writer)
/* 152:    */   {
/* 153:193 */     this._size = size;
/* 154:194 */     this._bigBigBlockSize = bigBlockSize;
/* 155:195 */     this._property = new DocumentProperty(name, this._size);
/* 156:196 */     this._property.setDocument(this);
/* 157:197 */     if (this._property.shouldUseSmallBlocks())
/* 158:    */     {
/* 159:198 */       this._small_store = new SmallBlockStore(this._bigBigBlockSize, path, name, size, writer);
/* 160:199 */       this._big_store = new BigBlockStore(this._bigBigBlockSize, EMPTY_BIG_BLOCK_ARRAY);
/* 161:    */     }
/* 162:    */     else
/* 163:    */     {
/* 164:201 */       this._small_store = new SmallBlockStore(this._bigBigBlockSize, EMPTY_SMALL_BLOCK_ARRAY);
/* 165:202 */       this._big_store = new BigBlockStore(this._bigBigBlockSize, path, name, size, writer);
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   public OPOIFSDocument(String name, int size, POIFSDocumentPath path, POIFSWriterListener writer)
/* 170:    */   {
/* 171:206 */     this(name, size, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, path, writer);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public SmallDocumentBlock[] getSmallBlocks()
/* 175:    */   {
/* 176:213 */     return this._small_store.getBlocks();
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int getSize()
/* 180:    */   {
/* 181:220 */     return this._size;
/* 182:    */   }
/* 183:    */   
/* 184:    */   void read(byte[] buffer, int offset)
/* 185:    */   {
/* 186:231 */     int len = buffer.length;
/* 187:    */     
/* 188:233 */     DataInputBlock currentBlock = getDataInputBlock(offset);
/* 189:    */     
/* 190:235 */     int blockAvailable = currentBlock.available();
/* 191:236 */     if (blockAvailable > len)
/* 192:    */     {
/* 193:237 */       currentBlock.readFully(buffer, 0, len);
/* 194:238 */       return;
/* 195:    */     }
/* 196:241 */     int remaining = len;
/* 197:242 */     int writePos = 0;
/* 198:243 */     int currentOffset = offset;
/* 199:244 */     while (remaining > 0)
/* 200:    */     {
/* 201:245 */       boolean blockIsExpiring = remaining >= blockAvailable;
/* 202:    */       int reqSize;
/* 203:    */       int reqSize;
/* 204:247 */       if (blockIsExpiring) {
/* 205:248 */         reqSize = blockAvailable;
/* 206:    */       } else {
/* 207:250 */         reqSize = remaining;
/* 208:    */       }
/* 209:252 */       currentBlock.readFully(buffer, writePos, reqSize);
/* 210:253 */       remaining -= reqSize;
/* 211:254 */       writePos += reqSize;
/* 212:255 */       currentOffset += reqSize;
/* 213:256 */       if (blockIsExpiring)
/* 214:    */       {
/* 215:257 */         if (currentOffset == this._size)
/* 216:    */         {
/* 217:258 */           if (remaining > 0) {
/* 218:259 */             throw new IllegalStateException("reached end of document stream unexpectedly");
/* 219:    */           }
/* 220:261 */           currentBlock = null;
/* 221:262 */           break;
/* 222:    */         }
/* 223:264 */         currentBlock = getDataInputBlock(currentOffset);
/* 224:265 */         blockAvailable = currentBlock.available();
/* 225:    */       }
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   DataInputBlock getDataInputBlock(int offset)
/* 230:    */   {
/* 231:274 */     if (offset >= this._size)
/* 232:    */     {
/* 233:275 */       if (offset > this._size) {
/* 234:276 */         throw new RuntimeException("Request for Offset " + offset + " doc size is " + this._size);
/* 235:    */       }
/* 236:278 */       return null;
/* 237:    */     }
/* 238:280 */     if (this._property.shouldUseSmallBlocks()) {
/* 239:281 */       return SmallDocumentBlock.getDataInputBlock(this._small_store.getBlocks(), offset);
/* 240:    */     }
/* 241:283 */     return DocumentBlock.getDataInputBlock(this._big_store.getBlocks(), offset);
/* 242:    */   }
/* 243:    */   
/* 244:    */   DocumentProperty getDocumentProperty()
/* 245:    */   {
/* 246:291 */     return this._property;
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void writeBlocks(OutputStream stream)
/* 250:    */     throws IOException
/* 251:    */   {
/* 252:302 */     this._big_store.writeBlocks(stream);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public int countBlocks()
/* 256:    */   {
/* 257:314 */     return this._big_store.countBlocks();
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void setStartBlock(int index)
/* 261:    */   {
/* 262:323 */     this._property.setStartBlock(index);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public Object[] getViewableArray()
/* 266:    */   {
/* 267:335 */     String result = "<NO DATA>";
/* 268:    */     try
/* 269:    */     {
/* 270:338 */       BlockWritable[] blocks = null;
/* 271:340 */       if (this._big_store.isValid()) {
/* 272:341 */         blocks = this._big_store.getBlocks();
/* 273:342 */       } else if (this._small_store.isValid()) {
/* 274:343 */         blocks = this._small_store.getBlocks();
/* 275:    */       }
/* 276:345 */       if (blocks != null)
/* 277:    */       {
/* 278:346 */         ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 279:347 */         for (BlockWritable bw : blocks) {
/* 280:348 */           bw.writeBlocks(output);
/* 281:    */         }
/* 282:350 */         int length = Math.min(output.size(), this._property.getSize());
/* 283:351 */         result = HexDump.dump(output.toByteArray(), 0L, 0, length);
/* 284:    */       }
/* 285:    */     }
/* 286:    */     catch (IOException e)
/* 287:    */     {
/* 288:354 */       result = e.getMessage();
/* 289:    */     }
/* 290:356 */     return new String[] { result };
/* 291:    */   }
/* 292:    */   
/* 293:    */   public Iterator<Object> getViewableIterator()
/* 294:    */   {
/* 295:366 */     return Collections.emptyList().iterator();
/* 296:    */   }
/* 297:    */   
/* 298:    */   public boolean preferArray()
/* 299:    */   {
/* 300:377 */     return true;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public String getShortDescription()
/* 304:    */   {
/* 305:387 */     StringBuffer buffer = new StringBuffer();
/* 306:    */     
/* 307:389 */     buffer.append("Document: \"").append(this._property.getName()).append("\"");
/* 308:390 */     buffer.append(" size = ").append(getSize());
/* 309:391 */     return buffer.toString();
/* 310:    */   }
/* 311:    */   
/* 312:    */   private static final class SmallBlockStore
/* 313:    */   {
/* 314:    */     private SmallDocumentBlock[] _smallBlocks;
/* 315:    */     private final POIFSDocumentPath _path;
/* 316:    */     private final String _name;
/* 317:    */     private final int _size;
/* 318:    */     private final POIFSWriterListener _writer;
/* 319:    */     private final POIFSBigBlockSize _bigBlockSize;
/* 320:    */     
/* 321:    */     SmallBlockStore(POIFSBigBlockSize bigBlockSize, SmallDocumentBlock[] blocks)
/* 322:    */     {
/* 323:409 */       this._bigBlockSize = bigBlockSize;
/* 324:410 */       this._smallBlocks = ((SmallDocumentBlock[])blocks.clone());
/* 325:411 */       this._path = null;
/* 326:412 */       this._name = null;
/* 327:413 */       this._size = -1;
/* 328:414 */       this._writer = null;
/* 329:    */     }
/* 330:    */     
/* 331:    */     SmallBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, String name, int size, POIFSWriterListener writer)
/* 332:    */     {
/* 333:427 */       this._bigBlockSize = bigBlockSize;
/* 334:428 */       this._smallBlocks = new SmallDocumentBlock[0];
/* 335:429 */       this._path = path;
/* 336:430 */       this._name = name;
/* 337:431 */       this._size = size;
/* 338:432 */       this._writer = writer;
/* 339:    */     }
/* 340:    */     
/* 341:    */     boolean isValid()
/* 342:    */     {
/* 343:439 */       return (this._smallBlocks.length > 0) || (this._writer != null);
/* 344:    */     }
/* 345:    */     
/* 346:    */     SmallDocumentBlock[] getBlocks()
/* 347:    */     {
/* 348:446 */       if ((isValid()) && (this._writer != null))
/* 349:    */       {
/* 350:447 */         ByteArrayOutputStream stream = new ByteArrayOutputStream(this._size);
/* 351:448 */         DocumentOutputStream dstream = new DocumentOutputStream(stream, this._size);
/* 352:    */         
/* 353:450 */         this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, this._path, this._name, this._size));
/* 354:451 */         this._smallBlocks = SmallDocumentBlock.convert(this._bigBlockSize, stream.toByteArray(), this._size);
/* 355:    */       }
/* 356:453 */       return this._smallBlocks;
/* 357:    */     }
/* 358:    */   }
/* 359:    */   
/* 360:    */   private static final class BigBlockStore
/* 361:    */   {
/* 362:    */     private DocumentBlock[] bigBlocks;
/* 363:    */     private final POIFSDocumentPath _path;
/* 364:    */     private final String _name;
/* 365:    */     private final int _size;
/* 366:    */     private final POIFSWriterListener _writer;
/* 367:    */     private final POIFSBigBlockSize _bigBlockSize;
/* 368:    */     
/* 369:    */     BigBlockStore(POIFSBigBlockSize bigBlockSize, DocumentBlock[] blocks)
/* 370:    */     {
/* 371:471 */       this._bigBlockSize = bigBlockSize;
/* 372:472 */       this.bigBlocks = ((DocumentBlock[])blocks.clone());
/* 373:473 */       this._path = null;
/* 374:474 */       this._name = null;
/* 375:475 */       this._size = -1;
/* 376:476 */       this._writer = null;
/* 377:    */     }
/* 378:    */     
/* 379:    */     BigBlockStore(POIFSBigBlockSize bigBlockSize, POIFSDocumentPath path, String name, int size, POIFSWriterListener writer)
/* 380:    */     {
/* 381:489 */       this._bigBlockSize = bigBlockSize;
/* 382:490 */       this.bigBlocks = new DocumentBlock[0];
/* 383:491 */       this._path = path;
/* 384:492 */       this._name = name;
/* 385:493 */       this._size = size;
/* 386:494 */       this._writer = writer;
/* 387:    */     }
/* 388:    */     
/* 389:    */     boolean isValid()
/* 390:    */     {
/* 391:501 */       return (this.bigBlocks.length > 0) || (this._writer != null);
/* 392:    */     }
/* 393:    */     
/* 394:    */     DocumentBlock[] getBlocks()
/* 395:    */     {
/* 396:508 */       if ((isValid()) && (this._writer != null))
/* 397:    */       {
/* 398:509 */         ByteArrayOutputStream stream = new ByteArrayOutputStream(this._size);
/* 399:510 */         DocumentOutputStream dstream = new DocumentOutputStream(stream, this._size);
/* 400:    */         
/* 401:512 */         this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, this._path, this._name, this._size));
/* 402:513 */         this.bigBlocks = DocumentBlock.convert(this._bigBlockSize, stream.toByteArray(), this._size);
/* 403:    */       }
/* 404:515 */       return this.bigBlocks;
/* 405:    */     }
/* 406:    */     
/* 407:    */     void writeBlocks(OutputStream stream)
/* 408:    */       throws IOException
/* 409:    */     {
/* 410:524 */       if (isValid()) {
/* 411:525 */         if (this._writer != null)
/* 412:    */         {
/* 413:526 */           DocumentOutputStream dstream = new DocumentOutputStream(stream, this._size);
/* 414:    */           
/* 415:528 */           this._writer.processPOIFSWriterEvent(new POIFSWriterEvent(dstream, this._path, this._name, this._size));
/* 416:529 */           dstream.writeFiller(countBlocks() * this._bigBlockSize.getBigBlockSize(), DocumentBlock.getFillByte());
/* 417:    */         }
/* 418:    */         else
/* 419:    */         {
/* 420:532 */           for (int k = 0; k < this.bigBlocks.length; k++) {
/* 421:533 */             this.bigBlocks[k].writeBlocks(stream);
/* 422:    */           }
/* 423:    */         }
/* 424:    */       }
/* 425:    */     }
/* 426:    */     
/* 427:    */     int countBlocks()
/* 428:    */     {
/* 429:544 */       if (isValid())
/* 430:    */       {
/* 431:545 */         if (this._writer == null) {
/* 432:546 */           return this.bigBlocks.length;
/* 433:    */         }
/* 434:548 */         return (this._size + this._bigBlockSize.getBigBlockSize() - 1) / this._bigBlockSize.getBigBlockSize();
/* 435:    */       }
/* 436:551 */       return 0;
/* 437:    */     }
/* 438:    */   }
/* 439:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.OPOIFSDocument
 * JD-Core Version:    0.7.0.1
 */