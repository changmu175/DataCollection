/*    1:     */ package org.apache.poi.poifs.filesystem;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayInputStream;
/*    4:     */ import java.io.Closeable;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileInputStream;
/*    7:     */ import java.io.FileOutputStream;
/*    8:     */ import java.io.IOException;
/*    9:     */ import java.io.InputStream;
/*   10:     */ import java.io.OutputStream;
/*   11:     */ import java.io.PrintStream;
/*   12:     */ import java.nio.ByteBuffer;
/*   13:     */ import java.nio.channels.Channels;
/*   14:     */ import java.nio.channels.FileChannel;
/*   15:     */ import java.nio.channels.ReadableByteChannel;
/*   16:     */ import java.util.ArrayList;
/*   17:     */ import java.util.Collections;
/*   18:     */ import java.util.Iterator;
/*   19:     */ import java.util.List;
/*   20:     */ import org.apache.poi.EmptyFileException;
/*   21:     */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   22:     */ import org.apache.poi.poifs.common.POIFSConstants;
/*   23:     */ import org.apache.poi.poifs.dev.POIFSViewable;
/*   24:     */ import org.apache.poi.poifs.nio.ByteArrayBackedDataSource;
/*   25:     */ import org.apache.poi.poifs.nio.DataSource;
/*   26:     */ import org.apache.poi.poifs.nio.FileBackedDataSource;
/*   27:     */ import org.apache.poi.poifs.property.DirectoryProperty;
/*   28:     */ import org.apache.poi.poifs.property.DocumentProperty;
/*   29:     */ import org.apache.poi.poifs.property.NPropertyTable;
/*   30:     */ import org.apache.poi.poifs.storage.BATBlock;
/*   31:     */ import org.apache.poi.poifs.storage.BATBlock.BATBlockAndIndex;
/*   32:     */ import org.apache.poi.poifs.storage.BlockAllocationTableReader;
/*   33:     */ import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
/*   34:     */ import org.apache.poi.poifs.storage.HeaderBlock;
/*   35:     */ import org.apache.poi.poifs.storage.HeaderBlockWriter;
/*   36:     */ import org.apache.poi.util.CloseIgnoringInputStream;
/*   37:     */ import org.apache.poi.util.IOUtils;
/*   38:     */ import org.apache.poi.util.Internal;
/*   39:     */ import org.apache.poi.util.POILogFactory;
/*   40:     */ import org.apache.poi.util.POILogger;
/*   41:     */ import org.apache.poi.util.Removal;
/*   42:     */ 
/*   43:     */ public class NPOIFSFileSystem
/*   44:     */   extends BlockStore
/*   45:     */   implements POIFSViewable, Closeable
/*   46:     */ {
/*   47:  71 */   private static final POILogger LOG = POILogFactory.getLogger(NPOIFSFileSystem.class);
/*   48:     */   private NPOIFSMiniStore _mini_store;
/*   49:     */   private NPropertyTable _property_table;
/*   50:     */   private List<BATBlock> _xbat_blocks;
/*   51:     */   private List<BATBlock> _bat_blocks;
/*   52:     */   private HeaderBlock _header;
/*   53:     */   private DirectoryNode _root;
/*   54:     */   private DataSource _data;
/*   55:     */   
/*   56:     */   public static InputStream createNonClosingInputStream(InputStream is)
/*   57:     */   {
/*   58:  77 */     return new CloseIgnoringInputStream(is);
/*   59:     */   }
/*   60:     */   
/*   61:  93 */   private POIFSBigBlockSize bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
/*   62:     */   
/*   63:     */   private NPOIFSFileSystem(boolean newFS)
/*   64:     */   {
/*   65:  98 */     this._header = new HeaderBlock(this.bigBlockSize);
/*   66:  99 */     this._property_table = new NPropertyTable(this._header);
/*   67: 100 */     this._mini_store = new NPOIFSMiniStore(this, this._property_table.getRoot(), new ArrayList(), this._header);
/*   68: 101 */     this._xbat_blocks = new ArrayList();
/*   69: 102 */     this._bat_blocks = new ArrayList();
/*   70: 103 */     this._root = null;
/*   71: 105 */     if (newFS) {
/*   72: 108 */       this._data = new ByteArrayBackedDataSource(new byte[this.bigBlockSize.getBigBlockSize() * 3]);
/*   73:     */     }
/*   74:     */   }
/*   75:     */   
/*   76:     */   public NPOIFSFileSystem()
/*   77:     */   {
/*   78: 117 */     this(true);
/*   79:     */     
/*   80:     */ 
/*   81:     */ 
/*   82: 121 */     this._header.setBATCount(1);
/*   83: 122 */     this._header.setBATArray(new int[] { 1 });
/*   84: 123 */     BATBlock bb = BATBlock.createEmptyBATBlock(this.bigBlockSize, false);
/*   85: 124 */     bb.setOurBlockIndex(1);
/*   86: 125 */     this._bat_blocks.add(bb);
/*   87:     */     
/*   88: 127 */     setNextBlock(0, -2);
/*   89: 128 */     setNextBlock(1, -3);
/*   90:     */     
/*   91: 130 */     this._property_table.setStartBlock(0);
/*   92:     */   }
/*   93:     */   
/*   94:     */   public NPOIFSFileSystem(File file)
/*   95:     */     throws IOException
/*   96:     */   {
/*   97: 148 */     this(file, true);
/*   98:     */   }
/*   99:     */   
/*  100:     */   public NPOIFSFileSystem(File file, boolean readOnly)
/*  101:     */     throws IOException
/*  102:     */   {
/*  103: 167 */     this(null, file, readOnly, true);
/*  104:     */   }
/*  105:     */   
/*  106:     */   public NPOIFSFileSystem(FileChannel channel)
/*  107:     */     throws IOException
/*  108:     */   {
/*  109: 186 */     this(channel, true);
/*  110:     */   }
/*  111:     */   
/*  112:     */   public NPOIFSFileSystem(FileChannel channel, boolean readOnly)
/*  113:     */     throws IOException
/*  114:     */   {
/*  115: 205 */     this(channel, null, readOnly, false);
/*  116:     */   }
/*  117:     */   
/*  118:     */   private NPOIFSFileSystem(FileChannel channel, File srcFile, boolean readOnly, boolean closeChannelOnError)
/*  119:     */     throws IOException
/*  120:     */   {
/*  121: 211 */     this(false);
/*  122:     */     try
/*  123:     */     {
/*  124: 215 */       if (srcFile != null)
/*  125:     */       {
/*  126: 216 */         if (srcFile.length() == 0L) {
/*  127: 217 */           throw new EmptyFileException();
/*  128:     */         }
/*  129: 219 */         FileBackedDataSource d = new FileBackedDataSource(srcFile, readOnly);
/*  130: 220 */         channel = d.getChannel();
/*  131: 221 */         this._data = d;
/*  132:     */       }
/*  133:     */       else
/*  134:     */       {
/*  135: 223 */         this._data = new FileBackedDataSource(channel, readOnly);
/*  136:     */       }
/*  137: 227 */       ByteBuffer headerBuffer = ByteBuffer.allocate(512);
/*  138: 228 */       IOUtils.readFully(channel, headerBuffer);
/*  139:     */       
/*  140:     */ 
/*  141: 231 */       this._header = new HeaderBlock(headerBuffer);
/*  142:     */       
/*  143:     */ 
/*  144: 234 */       readCoreContents();
/*  145:     */     }
/*  146:     */     catch (IOException e)
/*  147:     */     {
/*  148: 238 */       if ((closeChannelOnError) && (channel != null))
/*  149:     */       {
/*  150: 239 */         channel.close();
/*  151: 240 */         channel = null;
/*  152:     */       }
/*  153: 242 */       throw e;
/*  154:     */     }
/*  155:     */     catch (RuntimeException e)
/*  156:     */     {
/*  157: 247 */       if ((closeChannelOnError) && (channel != null))
/*  158:     */       {
/*  159: 248 */         channel.close();
/*  160: 249 */         channel = null;
/*  161:     */       }
/*  162: 251 */       throw e;
/*  163:     */     }
/*  164:     */   }
/*  165:     */   
/*  166:     */   public NPOIFSFileSystem(InputStream stream)
/*  167:     */     throws IOException
/*  168:     */   {
/*  169: 287 */     this(false);
/*  170:     */     
/*  171: 289 */     ReadableByteChannel channel = null;
/*  172: 290 */     boolean success = false;
/*  173:     */     try
/*  174:     */     {
/*  175: 294 */       channel = Channels.newChannel(stream);
/*  176:     */       
/*  177:     */ 
/*  178: 297 */       ByteBuffer headerBuffer = ByteBuffer.allocate(512);
/*  179: 298 */       IOUtils.readFully(channel, headerBuffer);
/*  180:     */       
/*  181:     */ 
/*  182: 301 */       this._header = new HeaderBlock(headerBuffer);
/*  183:     */       
/*  184:     */ 
/*  185: 304 */       BlockAllocationTableReader.sanityCheckBlockCount(this._header.getBATCount());
/*  186:     */       
/*  187:     */ 
/*  188:     */ 
/*  189:     */ 
/*  190: 309 */       long maxSize = BATBlock.calculateMaximumSize(this._header);
/*  191: 310 */       if (maxSize > 2147483647L) {
/*  192: 311 */         throw new IllegalArgumentException("Unable read a >2gb file via an InputStream");
/*  193:     */       }
/*  194: 313 */       ByteBuffer data = ByteBuffer.allocate((int)maxSize);
/*  195:     */       
/*  196:     */ 
/*  197: 316 */       headerBuffer.position(0);
/*  198: 317 */       data.put(headerBuffer);
/*  199: 318 */       data.position(headerBuffer.capacity());
/*  200:     */       
/*  201:     */ 
/*  202: 321 */       IOUtils.readFully(channel, data);
/*  203: 322 */       success = true;
/*  204:     */       
/*  205:     */ 
/*  206: 325 */       this._data = new ByteArrayBackedDataSource(data.array(), data.position());
/*  207:     */     }
/*  208:     */     finally
/*  209:     */     {
/*  210: 328 */       if (channel != null) {
/*  211: 329 */         channel.close();
/*  212:     */       }
/*  213: 330 */       closeInputStream(stream, success);
/*  214:     */     }
/*  215: 334 */     readCoreContents();
/*  216:     */   }
/*  217:     */   
/*  218:     */   private void closeInputStream(InputStream stream, boolean success)
/*  219:     */   {
/*  220:     */     try
/*  221:     */     {
/*  222: 342 */       stream.close();
/*  223:     */     }
/*  224:     */     catch (IOException e)
/*  225:     */     {
/*  226: 344 */       if (success) {
/*  227: 345 */         throw new RuntimeException(e);
/*  228:     */       }
/*  229: 349 */       LOG.log(7, new Object[] { "can't close input stream", e });
/*  230:     */     }
/*  231:     */   }
/*  232:     */   
/*  233:     */   @Deprecated
/*  234:     */   @Removal(version="4.0")
/*  235:     */   public static boolean hasPOIFSHeader(InputStream inp)
/*  236:     */     throws IOException
/*  237:     */   {
/*  238: 370 */     return FileMagic.valueOf(inp) == FileMagic.OLE2;
/*  239:     */   }
/*  240:     */   
/*  241:     */   @Deprecated
/*  242:     */   @Removal(version="4.0")
/*  243:     */   public static boolean hasPOIFSHeader(byte[] header8Bytes)
/*  244:     */   {
/*  245:     */     try
/*  246:     */     {
/*  247: 383 */       return hasPOIFSHeader(new ByteArrayInputStream(header8Bytes));
/*  248:     */     }
/*  249:     */     catch (IOException e)
/*  250:     */     {
/*  251: 385 */       throw new RuntimeException("invalid header check", e);
/*  252:     */     }
/*  253:     */   }
/*  254:     */   
/*  255:     */   private void readCoreContents()
/*  256:     */     throws IOException
/*  257:     */   {
/*  258: 396 */     this.bigBlockSize = this._header.getBigBlockSize();
/*  259:     */     
/*  260:     */ 
/*  261:     */ 
/*  262: 400 */     ChainLoopDetector loopDetector = getChainLoopDetector();
/*  263: 403 */     for (int fatAt : this._header.getBATArray()) {
/*  264: 404 */       readBAT(fatAt, loopDetector);
/*  265:     */     }
/*  266: 408 */     int remainingFATs = this._header.getBATCount() - this._header.getBATArray().length;
/*  267:     */     
/*  268:     */ 
/*  269:     */ 
/*  270: 412 */     int nextAt = this._header.getXBATIndex();
/*  271: 413 */     for (int i = 0; i < this._header.getXBATCount(); i++)
/*  272:     */     {
/*  273: 414 */       loopDetector.claim(nextAt);
/*  274: 415 */       ByteBuffer fatData = getBlockAt(nextAt);
/*  275: 416 */       BATBlock xfat = BATBlock.createBATBlock(this.bigBlockSize, fatData);
/*  276: 417 */       xfat.setOurBlockIndex(nextAt);
/*  277: 418 */       nextAt = xfat.getValueAt(this.bigBlockSize.getXBATEntriesPerBlock());
/*  278: 419 */       this._xbat_blocks.add(xfat);
/*  279:     */       
/*  280:     */ 
/*  281: 422 */       int xbatFATs = Math.min(remainingFATs, this.bigBlockSize.getXBATEntriesPerBlock());
/*  282: 423 */       for (int j = 0; j < xbatFATs; j++)
/*  283:     */       {
/*  284: 424 */         int fatAt = xfat.getValueAt(j);
/*  285: 425 */         if ((fatAt == -1) || (fatAt == -2)) {
/*  286:     */           break;
/*  287:     */         }
/*  288: 426 */         readBAT(fatAt, loopDetector);
/*  289:     */       }
/*  290: 428 */       remainingFATs -= xbatFATs;
/*  291:     */     }
/*  292: 433 */     this._property_table = new NPropertyTable(this._header, this);
/*  293:     */     
/*  294:     */ 
/*  295:     */ 
/*  296: 437 */     List<BATBlock> sbats = new ArrayList();
/*  297: 438 */     this._mini_store = new NPOIFSMiniStore(this, this._property_table.getRoot(), sbats, this._header);
/*  298: 439 */     nextAt = this._header.getSBATStart();
/*  299: 440 */     for (int i = 0; (i < this._header.getSBATCount()) && (nextAt != -2); i++)
/*  300:     */     {
/*  301: 441 */       loopDetector.claim(nextAt);
/*  302: 442 */       ByteBuffer fatData = getBlockAt(nextAt);
/*  303: 443 */       BATBlock sfat = BATBlock.createBATBlock(this.bigBlockSize, fatData);
/*  304: 444 */       sfat.setOurBlockIndex(nextAt);
/*  305: 445 */       sbats.add(sfat);
/*  306: 446 */       nextAt = getNextBlock(nextAt);
/*  307:     */     }
/*  308:     */   }
/*  309:     */   
/*  310:     */   private void readBAT(int batAt, ChainLoopDetector loopDetector)
/*  311:     */     throws IOException
/*  312:     */   {
/*  313: 450 */     loopDetector.claim(batAt);
/*  314: 451 */     ByteBuffer fatData = getBlockAt(batAt);
/*  315: 452 */     BATBlock bat = BATBlock.createBATBlock(this.bigBlockSize, fatData);
/*  316: 453 */     bat.setOurBlockIndex(batAt);
/*  317: 454 */     this._bat_blocks.add(bat);
/*  318:     */   }
/*  319:     */   
/*  320:     */   private BATBlock createBAT(int offset, boolean isBAT)
/*  321:     */     throws IOException
/*  322:     */   {
/*  323: 458 */     BATBlock newBAT = BATBlock.createEmptyBATBlock(this.bigBlockSize, !isBAT);
/*  324: 459 */     newBAT.setOurBlockIndex(offset);
/*  325:     */     
/*  326: 461 */     ByteBuffer buffer = ByteBuffer.allocate(this.bigBlockSize.getBigBlockSize());
/*  327: 462 */     int writeTo = (1 + offset) * this.bigBlockSize.getBigBlockSize();
/*  328: 463 */     this._data.write(buffer, writeTo);
/*  329:     */     
/*  330: 465 */     return newBAT;
/*  331:     */   }
/*  332:     */   
/*  333:     */   protected ByteBuffer getBlockAt(int offset)
/*  334:     */     throws IOException
/*  335:     */   {
/*  336: 474 */     long blockWanted = offset + 1L;
/*  337: 475 */     long startAt = blockWanted * this.bigBlockSize.getBigBlockSize();
/*  338:     */     try
/*  339:     */     {
/*  340: 477 */       return this._data.read(this.bigBlockSize.getBigBlockSize(), startAt);
/*  341:     */     }
/*  342:     */     catch (IndexOutOfBoundsException e)
/*  343:     */     {
/*  344: 479 */       IndexOutOfBoundsException wrapped = new IndexOutOfBoundsException("Block " + offset + " not found");
/*  345: 480 */       wrapped.initCause(e);
/*  346: 481 */       throw wrapped;
/*  347:     */     }
/*  348:     */   }
/*  349:     */   
/*  350:     */   protected ByteBuffer createBlockIfNeeded(int offset)
/*  351:     */     throws IOException
/*  352:     */   {
/*  353:     */     try
/*  354:     */     {
/*  355: 492 */       return getBlockAt(offset);
/*  356:     */     }
/*  357:     */     catch (IndexOutOfBoundsException e)
/*  358:     */     {
/*  359: 495 */       long startAt = (offset + 1L) * this.bigBlockSize.getBigBlockSize();
/*  360:     */       
/*  361: 497 */       ByteBuffer buffer = ByteBuffer.allocate(getBigBlockSize());
/*  362: 498 */       this._data.write(buffer, startAt);
/*  363:     */     }
/*  364: 500 */     return getBlockAt(offset);
/*  365:     */   }
/*  366:     */   
/*  367:     */   protected BATBlockAndIndex getBATBlockAndIndex(int offset)
/*  368:     */   {
/*  369: 510 */     return BATBlock.getBATBlockAndIndex(offset, this._header, this._bat_blocks);
/*  370:     */   }
/*  371:     */   
/*  372:     */   protected int getNextBlock(int offset)
/*  373:     */   {
/*  374: 520 */     BATBlockAndIndex bai = getBATBlockAndIndex(offset);
/*  375: 521 */     return bai.getBlock().getValueAt(bai.getIndex());
/*  376:     */   }
/*  377:     */   
/*  378:     */   protected void setNextBlock(int offset, int nextBlock)
/*  379:     */   {
/*  380: 529 */     BATBlockAndIndex bai = getBATBlockAndIndex(offset);
/*  381: 530 */     bai.getBlock().setValueAt(bai.getIndex(), nextBlock);
/*  382:     */   }
/*  383:     */   
/*  384:     */   protected int getFreeBlock()
/*  385:     */     throws IOException
/*  386:     */   {
/*  387: 542 */     int numSectors = this.bigBlockSize.getBATEntriesPerBlock();
/*  388:     */     
/*  389:     */ 
/*  390: 545 */     int offset = 0;
/*  391: 546 */     for (BATBlock bat : this._bat_blocks)
/*  392:     */     {
/*  393: 547 */       if (bat.hasFreeSectors()) {
/*  394: 549 */         for (int j = 0; j < numSectors; j++)
/*  395:     */         {
/*  396: 550 */           int batValue = bat.getValueAt(j);
/*  397: 551 */           if (batValue == -1) {
/*  398: 553 */             return offset + j;
/*  399:     */           }
/*  400:     */         }
/*  401:     */       }
/*  402: 559 */       offset += numSectors;
/*  403:     */     }
/*  404: 564 */     BATBlock bat = createBAT(offset, true);
/*  405: 565 */     bat.setValueAt(0, -3);
/*  406: 566 */     this._bat_blocks.add(bat);
/*  407: 569 */     if (this._header.getBATCount() >= 109)
/*  408:     */     {
/*  409: 571 */       BATBlock xbat = null;
/*  410: 572 */       for (BATBlock x : this._xbat_blocks) {
/*  411: 573 */         if (x.hasFreeSectors())
/*  412:     */         {
/*  413: 574 */           xbat = x;
/*  414: 575 */           break;
/*  415:     */         }
/*  416:     */       }
/*  417: 578 */       if (xbat == null)
/*  418:     */       {
/*  419: 580 */         xbat = createBAT(offset + 1, false);
/*  420:     */         
/*  421: 582 */         xbat.setValueAt(0, offset);
/*  422:     */         
/*  423: 584 */         bat.setValueAt(1, -4);
/*  424:     */         
/*  425:     */ 
/*  426: 587 */         offset++;
/*  427: 590 */         if (this._xbat_blocks.size() == 0) {
/*  428: 591 */           this._header.setXBATStart(offset);
/*  429:     */         } else {
/*  430: 593 */           ((BATBlock)this._xbat_blocks.get(this._xbat_blocks.size() - 1)).setValueAt(this.bigBlockSize.getXBATEntriesPerBlock(), offset);
/*  431:     */         }
/*  432: 597 */         this._xbat_blocks.add(xbat);
/*  433: 598 */         this._header.setXBATCount(this._xbat_blocks.size());
/*  434:     */       }
/*  435:     */       else
/*  436:     */       {
/*  437: 601 */         for (int i = 0; i < this.bigBlockSize.getXBATEntriesPerBlock(); i++) {
/*  438: 602 */           if (xbat.getValueAt(i) == -1)
/*  439:     */           {
/*  440: 603 */             xbat.setValueAt(i, offset);
/*  441: 604 */             break;
/*  442:     */           }
/*  443:     */         }
/*  444:     */       }
/*  445:     */     }
/*  446:     */     else
/*  447:     */     {
/*  448: 610 */       int[] newBATs = new int[this._header.getBATCount() + 1];
/*  449: 611 */       System.arraycopy(this._header.getBATArray(), 0, newBATs, 0, newBATs.length - 1);
/*  450: 612 */       newBATs[(newBATs.length - 1)] = offset;
/*  451: 613 */       this._header.setBATArray(newBATs);
/*  452:     */     }
/*  453: 615 */     this._header.setBATCount(this._bat_blocks.size());
/*  454:     */     
/*  455:     */ 
/*  456: 618 */     return offset + 1;
/*  457:     */   }
/*  458:     */   
/*  459:     */   protected long size()
/*  460:     */     throws IOException
/*  461:     */   {
/*  462: 622 */     return this._data.size();
/*  463:     */   }
/*  464:     */   
/*  465:     */   protected ChainLoopDetector getChainLoopDetector()
/*  466:     */     throws IOException
/*  467:     */   {
/*  468: 627 */     return new ChainLoopDetector(this, this._data.size());
/*  469:     */   }
/*  470:     */   
/*  471:     */   NPropertyTable _get_property_table()
/*  472:     */   {
/*  473: 635 */     return this._property_table;
/*  474:     */   }
/*  475:     */   
/*  476:     */   public NPOIFSMiniStore getMiniStore()
/*  477:     */   {
/*  478: 643 */     return this._mini_store;
/*  479:     */   }
/*  480:     */   
/*  481:     */   void addDocument(NPOIFSDocument document)
/*  482:     */   {
/*  483: 653 */     this._property_table.addProperty(document.getDocumentProperty());
/*  484:     */   }
/*  485:     */   
/*  486:     */   void addDirectory(DirectoryProperty directory)
/*  487:     */   {
/*  488: 663 */     this._property_table.addProperty(directory);
/*  489:     */   }
/*  490:     */   
/*  491:     */   public DocumentEntry createDocument(InputStream stream, String name)
/*  492:     */     throws IOException
/*  493:     */   {
/*  494: 682 */     return getRoot().createDocument(name, stream);
/*  495:     */   }
/*  496:     */   
/*  497:     */   public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer)
/*  498:     */     throws IOException
/*  499:     */   {
/*  500: 702 */     return getRoot().createDocument(name, size, writer);
/*  501:     */   }
/*  502:     */   
/*  503:     */   public DirectoryEntry createDirectory(String name)
/*  504:     */     throws IOException
/*  505:     */   {
/*  506: 718 */     return getRoot().createDirectory(name);
/*  507:     */   }
/*  508:     */   
/*  509:     */   public DocumentEntry createOrUpdateDocument(InputStream stream, String name)
/*  510:     */     throws IOException
/*  511:     */   {
/*  512: 738 */     return getRoot().createOrUpdateDocument(name, stream);
/*  513:     */   }
/*  514:     */   
/*  515:     */   public boolean isInPlaceWriteable()
/*  516:     */   {
/*  517: 748 */     if (((this._data instanceof FileBackedDataSource)) && 
/*  518: 749 */       (((FileBackedDataSource)this._data).isWriteable())) {
/*  519: 750 */       return true;
/*  520:     */     }
/*  521: 753 */     return false;
/*  522:     */   }
/*  523:     */   
/*  524:     */   public void writeFilesystem()
/*  525:     */     throws IOException
/*  526:     */   {
/*  527: 765 */     if (!(this._data instanceof FileBackedDataSource)) {
/*  528: 768 */       throw new IllegalArgumentException("POIFS opened from an inputstream, so writeFilesystem() may not be called. Use writeFilesystem(OutputStream) instead");
/*  529:     */     }
/*  530: 773 */     if (!((FileBackedDataSource)this._data).isWriteable()) {
/*  531: 774 */       throw new IllegalArgumentException("POIFS opened in read only mode, so writeFilesystem() may not be called. Open the FileSystem in read-write mode first");
/*  532:     */     }
/*  533: 779 */     syncWithDataSource();
/*  534:     */   }
/*  535:     */   
/*  536:     */   public void writeFilesystem(OutputStream stream)
/*  537:     */     throws IOException
/*  538:     */   {
/*  539: 795 */     syncWithDataSource();
/*  540:     */     
/*  541:     */ 
/*  542: 798 */     this._data.copyTo(stream);
/*  543:     */   }
/*  544:     */   
/*  545:     */   private void syncWithDataSource()
/*  546:     */     throws IOException
/*  547:     */   {
/*  548: 808 */     this._mini_store.syncWithDataSource();
/*  549:     */     
/*  550:     */ 
/*  551: 811 */     NPOIFSStream propStream = new NPOIFSStream(this, this._header.getPropertyStart());
/*  552: 812 */     this._property_table.preWrite();
/*  553: 813 */     this._property_table.write(propStream);
/*  554:     */     
/*  555:     */ 
/*  556:     */ 
/*  557: 817 */     HeaderBlockWriter hbw = new HeaderBlockWriter(this._header);
/*  558: 818 */     hbw.writeBlock(getBlockAt(-1));
/*  559: 821 */     for (BATBlock bat : this._bat_blocks)
/*  560:     */     {
/*  561: 822 */       ByteBuffer block = getBlockAt(bat.getOurBlockIndex());
/*  562: 823 */       BlockAllocationTableWriter.writeBlock(bat, block);
/*  563:     */     }
/*  564: 826 */     for (BATBlock bat : this._xbat_blocks)
/*  565:     */     {
/*  566: 827 */       ByteBuffer block = getBlockAt(bat.getOurBlockIndex());
/*  567: 828 */       BlockAllocationTableWriter.writeBlock(bat, block);
/*  568:     */     }
/*  569:     */   }
/*  570:     */   
/*  571:     */   public void close()
/*  572:     */     throws IOException
/*  573:     */   {
/*  574: 838 */     this._data.close();
/*  575:     */   }
/*  576:     */   
/*  577:     */   public static void main(String[] args)
/*  578:     */     throws IOException
/*  579:     */   {
/*  580: 853 */     if (args.length != 2)
/*  581:     */     {
/*  582: 855 */       System.err.println("two arguments required: input filename and output filename");
/*  583:     */       
/*  584: 857 */       System.exit(1);
/*  585:     */     }
/*  586: 859 */     FileInputStream istream = new FileInputStream(args[0]);
/*  587:     */     try
/*  588:     */     {
/*  589: 861 */       FileOutputStream ostream = new FileOutputStream(args[1]);
/*  590:     */       try
/*  591:     */       {
/*  592: 863 */         NPOIFSFileSystem fs = new NPOIFSFileSystem(istream);
/*  593:     */         try
/*  594:     */         {
/*  595: 865 */           fs.writeFilesystem(ostream);
/*  596:     */         }
/*  597:     */         finally {}
/*  598:     */       }
/*  599:     */       finally {}
/*  600:     */     }
/*  601:     */     finally
/*  602:     */     {
/*  603: 873 */       istream.close();
/*  604:     */     }
/*  605:     */   }
/*  606:     */   
/*  607:     */   public DirectoryNode getRoot()
/*  608:     */   {
/*  609: 884 */     if (this._root == null) {
/*  610: 885 */       this._root = new DirectoryNode(this._property_table.getRoot(), this, null);
/*  611:     */     }
/*  612: 887 */     return this._root;
/*  613:     */   }
/*  614:     */   
/*  615:     */   public DocumentInputStream createDocumentInputStream(String documentName)
/*  616:     */     throws IOException
/*  617:     */   {
/*  618: 905 */     return getRoot().createDocumentInputStream(documentName);
/*  619:     */   }
/*  620:     */   
/*  621:     */   void remove(EntryNode entry)
/*  622:     */     throws IOException
/*  623:     */   {
/*  624: 916 */     if ((entry instanceof DocumentEntry))
/*  625:     */     {
/*  626: 917 */       NPOIFSDocument doc = new NPOIFSDocument((DocumentProperty)entry.getProperty(), this);
/*  627: 918 */       doc.free();
/*  628:     */     }
/*  629: 922 */     this._property_table.removeProperty(entry.getProperty());
/*  630:     */   }
/*  631:     */   
/*  632:     */   public Object[] getViewableArray()
/*  633:     */   {
/*  634: 936 */     if (preferArray()) {
/*  635: 938 */       return getRoot().getViewableArray();
/*  636:     */     }
/*  637: 940 */     return new Object[0];
/*  638:     */   }
/*  639:     */   
/*  640:     */   public Iterator<Object> getViewableIterator()
/*  641:     */   {
/*  642: 953 */     if (!preferArray()) {
/*  643: 955 */       return getRoot().getViewableIterator();
/*  644:     */     }
/*  645: 957 */     return Collections.emptyList().iterator();
/*  646:     */   }
/*  647:     */   
/*  648:     */   public boolean preferArray()
/*  649:     */   {
/*  650: 970 */     return getRoot().preferArray();
/*  651:     */   }
/*  652:     */   
/*  653:     */   public String getShortDescription()
/*  654:     */   {
/*  655: 982 */     return "POIFS FileSystem";
/*  656:     */   }
/*  657:     */   
/*  658:     */   public int getBigBlockSize()
/*  659:     */   {
/*  660: 991 */     return this.bigBlockSize.getBigBlockSize();
/*  661:     */   }
/*  662:     */   
/*  663:     */   public POIFSBigBlockSize getBigBlockSizeDetails()
/*  664:     */   {
/*  665: 998 */     return this.bigBlockSize;
/*  666:     */   }
/*  667:     */   
/*  668:     */   protected int getBlockStoreBlockSize()
/*  669:     */   {
/*  670:1003 */     return getBigBlockSize();
/*  671:     */   }
/*  672:     */   
/*  673:     */   @Internal
/*  674:     */   public NPropertyTable getPropertyTable()
/*  675:     */   {
/*  676:1008 */     return this._property_table;
/*  677:     */   }
/*  678:     */   
/*  679:     */   @Internal
/*  680:     */   public HeaderBlock getHeaderBlock()
/*  681:     */   {
/*  682:1013 */     return this._header;
/*  683:     */   }
/*  684:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.filesystem.NPOIFSFileSystem

 * JD-Core Version:    0.7.0.1

 */