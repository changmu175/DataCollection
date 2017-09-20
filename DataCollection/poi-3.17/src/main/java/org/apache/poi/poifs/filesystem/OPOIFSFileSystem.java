/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Collections;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.List;
/*  14:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  15:    */ import org.apache.poi.poifs.common.POIFSConstants;
/*  16:    */ import org.apache.poi.poifs.dev.POIFSViewable;
/*  17:    */ import org.apache.poi.poifs.property.DirectoryProperty;
/*  18:    */ import org.apache.poi.poifs.property.Property;
/*  19:    */ import org.apache.poi.poifs.property.PropertyTable;
/*  20:    */ import org.apache.poi.poifs.property.RootProperty;
/*  21:    */ import org.apache.poi.poifs.storage.BATBlock;
/*  22:    */ import org.apache.poi.poifs.storage.BlockAllocationTableReader;
/*  23:    */ import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
/*  24:    */ import org.apache.poi.poifs.storage.BlockList;
/*  25:    */ import org.apache.poi.poifs.storage.BlockWritable;
/*  26:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  27:    */ import org.apache.poi.poifs.storage.HeaderBlockWriter;
/*  28:    */ import org.apache.poi.poifs.storage.RawDataBlockList;
/*  29:    */ import org.apache.poi.poifs.storage.SmallBlockTableReader;
/*  30:    */ import org.apache.poi.poifs.storage.SmallBlockTableWriter;
/*  31:    */ import org.apache.poi.util.CloseIgnoringInputStream;
/*  32:    */ import org.apache.poi.util.POILogFactory;
/*  33:    */ import org.apache.poi.util.POILogger;
/*  34:    */ import org.apache.poi.util.Removal;
/*  35:    */ 
/*  36:    */ public class OPOIFSFileSystem
/*  37:    */   implements POIFSViewable
/*  38:    */ {
/*  39: 63 */   private static final POILogger _logger = POILogFactory.getLogger(OPOIFSFileSystem.class);
/*  40:    */   private PropertyTable _property_table;
/*  41:    */   private List<OPOIFSDocument> _documents;
/*  42:    */   private DirectoryNode _root;
/*  43:    */   
/*  44:    */   public static InputStream createNonClosingInputStream(InputStream is)
/*  45:    */   {
/*  46: 70 */     return new CloseIgnoringInputStream(is);
/*  47:    */   }
/*  48:    */   
/*  49: 81 */   private POIFSBigBlockSize bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
/*  50:    */   
/*  51:    */   public OPOIFSFileSystem()
/*  52:    */   {
/*  53: 89 */     HeaderBlock header_block = new HeaderBlock(this.bigBlockSize);
/*  54: 90 */     this._property_table = new PropertyTable(header_block);
/*  55: 91 */     this._documents = new ArrayList();
/*  56: 92 */     this._root = null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public OPOIFSFileSystem(InputStream stream)
/*  60:    */     throws IOException
/*  61:    */   {
/*  62:127 */     this();
/*  63:128 */     boolean success = false;
/*  64:    */     HeaderBlock header_block;
/*  65:    */     RawDataBlockList data_blocks;
/*  66:    */     try
/*  67:    */     {
/*  68:134 */       header_block = new HeaderBlock(stream);
/*  69:135 */       this.bigBlockSize = header_block.getBigBlockSize();
/*  70:    */       
/*  71:    */ 
/*  72:138 */       data_blocks = new RawDataBlockList(stream, this.bigBlockSize);
/*  73:139 */       success = true;
/*  74:    */     }
/*  75:    */     finally
/*  76:    */     {
/*  77:141 */       closeInputStream(stream, success);
/*  78:    */     }
/*  79:147 */     new BlockAllocationTableReader(header_block.getBigBlockSize(), header_block.getBATCount(), header_block.getBATArray(), header_block.getXBATCount(), header_block.getXBATIndex(), data_blocks);
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:155 */     PropertyTable properties = new PropertyTable(header_block, data_blocks);
/*  88:    */     
/*  89:    */ 
/*  90:    */ 
/*  91:159 */     processProperties(SmallBlockTableReader.getSmallDocumentBlocks(this.bigBlockSize, data_blocks, properties.getRoot(), header_block.getSBATStart()), data_blocks, properties.getRoot().getChildren(), null, header_block.getPropertyStart());
/*  92:    */     
/*  93:    */ 
/*  94:    */ 
/*  95:    */ 
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */ 
/* 100:    */ 
/* 101:    */ 
/* 102:    */ 
/* 103:171 */     getRoot().setStorageClsid(properties.getRoot().getStorageClsid());
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected void closeInputStream(InputStream stream, boolean success)
/* 107:    */   {
/* 108:179 */     if ((stream.markSupported()) && (!(stream instanceof ByteArrayInputStream)))
/* 109:    */     {
/* 110:180 */       String msg = "POIFS is closing the supplied input stream of type (" + stream.getClass().getName() + ") which supports mark/reset.  " + "This will be a problem for the caller if the stream will still be used.  " + "If that is the case the caller should wrap the input stream to avoid this close logic.  " + "This warning is only temporary and will not be present in future versions of POI.";
/* 111:    */       
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:185 */       _logger.log(5, new Object[] { msg });
/* 116:    */     }
/* 117:    */     try
/* 118:    */     {
/* 119:188 */       stream.close();
/* 120:    */     }
/* 121:    */     catch (IOException e)
/* 122:    */     {
/* 123:190 */       if (success) {
/* 124:191 */         throw new RuntimeException(e);
/* 125:    */       }
/* 126:195 */       _logger.log(7, new Object[] { "can't close input stream", e });
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   @Deprecated
/* 131:    */   @Removal(version="4.0")
/* 132:    */   public static boolean hasPOIFSHeader(InputStream inp)
/* 133:    */     throws IOException
/* 134:    */   {
/* 135:216 */     return NPOIFSFileSystem.hasPOIFSHeader(inp);
/* 136:    */   }
/* 137:    */   
/* 138:    */   @Deprecated
/* 139:    */   @Removal(version="4.0")
/* 140:    */   public static boolean hasPOIFSHeader(byte[] header8Bytes)
/* 141:    */   {
/* 142:228 */     return NPOIFSFileSystem.hasPOIFSHeader(header8Bytes);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public DocumentEntry createDocument(InputStream stream, String name)
/* 146:    */     throws IOException
/* 147:    */   {
/* 148:247 */     return getRoot().createDocument(name, stream);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer)
/* 152:    */     throws IOException
/* 153:    */   {
/* 154:267 */     return getRoot().createDocument(name, size, writer);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public DirectoryEntry createDirectory(String name)
/* 158:    */     throws IOException
/* 159:    */   {
/* 160:283 */     return getRoot().createDirectory(name);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void writeFilesystem(OutputStream stream)
/* 164:    */     throws IOException
/* 165:    */   {
/* 166:300 */     this._property_table.preWrite();
/* 167:    */     
/* 168:    */ 
/* 169:303 */     SmallBlockTableWriter sbtw = new SmallBlockTableWriter(this.bigBlockSize, this._documents, this._property_table.getRoot());
/* 170:    */     
/* 171:    */ 
/* 172:    */ 
/* 173:307 */     BlockAllocationTableWriter bat = new BlockAllocationTableWriter(this.bigBlockSize);
/* 174:    */     
/* 175:    */ 
/* 176:    */ 
/* 177:    */ 
/* 178:312 */     List<Object> bm_objects = new ArrayList();
/* 179:    */     
/* 180:314 */     bm_objects.addAll(this._documents);
/* 181:315 */     bm_objects.add(this._property_table);
/* 182:316 */     bm_objects.add(sbtw);
/* 183:317 */     bm_objects.add(sbtw.getSBAT());
/* 184:    */     
/* 185:    */ 
/* 186:    */ 
/* 187:321 */     Iterator<Object> iter = bm_objects.iterator();
/* 188:323 */     while (iter.hasNext())
/* 189:    */     {
/* 190:325 */       BATManaged bmo = (BATManaged)iter.next();
/* 191:326 */       int block_count = bmo.countBlocks();
/* 192:328 */       if (block_count != 0) {
/* 193:330 */         bmo.setStartBlock(bat.allocateSpace(block_count));
/* 194:    */       }
/* 195:    */     }
/* 196:343 */     int batStartBlock = bat.createBlocks();
/* 197:    */     
/* 198:    */ 
/* 199:346 */     HeaderBlockWriter header_block_writer = new HeaderBlockWriter(this.bigBlockSize);
/* 200:347 */     BATBlock[] xbat_blocks = header_block_writer.setBATBlocks(bat.countBlocks(), batStartBlock);
/* 201:    */     
/* 202:    */ 
/* 203:    */ 
/* 204:    */ 
/* 205:352 */     header_block_writer.setPropertyStart(this._property_table.getStartBlock());
/* 206:    */     
/* 207:    */ 
/* 208:355 */     header_block_writer.setSBATStart(sbtw.getSBAT().getStartBlock());
/* 209:    */     
/* 210:    */ 
/* 211:358 */     header_block_writer.setSBATBlockCount(sbtw.getSBATBlockCount());
/* 212:    */     
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:365 */     List<Object> writers = new ArrayList();
/* 219:    */     
/* 220:367 */     writers.add(header_block_writer);
/* 221:368 */     writers.addAll(this._documents);
/* 222:369 */     writers.add(this._property_table);
/* 223:370 */     writers.add(sbtw);
/* 224:371 */     writers.add(sbtw.getSBAT());
/* 225:372 */     writers.add(bat);
/* 226:373 */     for (int j = 0; j < xbat_blocks.length; j++) {
/* 227:375 */       writers.add(xbat_blocks[j]);
/* 228:    */     }
/* 229:379 */     iter = writers.iterator();
/* 230:380 */     while (iter.hasNext())
/* 231:    */     {
/* 232:382 */       BlockWritable writer = (BlockWritable)iter.next();
/* 233:    */       
/* 234:384 */       writer.writeBlocks(stream);
/* 235:    */     }
/* 236:    */   }
/* 237:    */   
/* 238:    */   public static void main(String[] args)
/* 239:    */     throws IOException
/* 240:    */   {
/* 241:400 */     if (args.length != 2)
/* 242:    */     {
/* 243:402 */       System.err.println("two arguments required: input filename and output filename");
/* 244:    */       
/* 245:404 */       System.exit(1);
/* 246:    */     }
/* 247:406 */     FileInputStream istream = new FileInputStream(args[0]);
/* 248:407 */     FileOutputStream ostream = new FileOutputStream(args[1]);
/* 249:    */     
/* 250:409 */     new OPOIFSFileSystem(istream).writeFilesystem(ostream);
/* 251:410 */     istream.close();
/* 252:411 */     ostream.close();
/* 253:    */   }
/* 254:    */   
/* 255:    */   public DirectoryNode getRoot()
/* 256:    */   {
/* 257:422 */     if (this._root == null) {
/* 258:424 */       this._root = new DirectoryNode(this._property_table.getRoot(), this, null);
/* 259:    */     }
/* 260:426 */     return this._root;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public DocumentInputStream createDocumentInputStream(String documentName)
/* 264:    */     throws IOException
/* 265:    */   {
/* 266:444 */     return getRoot().createDocumentInputStream(documentName);
/* 267:    */   }
/* 268:    */   
/* 269:    */   void addDocument(OPOIFSDocument document)
/* 270:    */   {
/* 271:455 */     this._documents.add(document);
/* 272:456 */     this._property_table.addProperty(document.getDocumentProperty());
/* 273:    */   }
/* 274:    */   
/* 275:    */   void addDirectory(DirectoryProperty directory)
/* 276:    */   {
/* 277:467 */     this._property_table.addProperty(directory);
/* 278:    */   }
/* 279:    */   
/* 280:    */   void remove(EntryNode entry)
/* 281:    */   {
/* 282:478 */     this._property_table.removeProperty(entry.getProperty());
/* 283:479 */     if (entry.isDocumentEntry()) {
/* 284:481 */       this._documents.remove(((DocumentNode)entry).getDocument());
/* 285:    */     }
/* 286:    */   }
/* 287:    */   
/* 288:    */   private void processProperties(BlockList small_blocks, BlockList big_blocks, Iterator<Property> properties, DirectoryNode dir, int headerPropertiesStartAt)
/* 289:    */     throws IOException
/* 290:    */   {
/* 291:492 */     while (properties.hasNext())
/* 292:    */     {
/* 293:494 */       Property property = (Property)properties.next();
/* 294:495 */       String name = property.getName();
/* 295:496 */       DirectoryNode parent = dir == null ? getRoot() : dir;
/* 296:500 */       if (property.isDirectory())
/* 297:    */       {
/* 298:502 */         DirectoryNode new_dir = (DirectoryNode)parent.createDirectory(name);
/* 299:    */         
/* 300:    */ 
/* 301:505 */         new_dir.setStorageClsid(property.getStorageClsid());
/* 302:    */         
/* 303:507 */         processProperties(small_blocks, big_blocks, ((DirectoryProperty)property).getChildren(), new_dir, headerPropertiesStartAt);
/* 304:    */       }
/* 305:    */       else
/* 306:    */       {
/* 307:514 */         int startBlock = property.getStartBlock();
/* 308:515 */         int size = property.getSize();
/* 309:516 */         OPOIFSDocument document = null;
/* 310:518 */         if (property.shouldUseSmallBlocks()) {
/* 311:520 */           document = new OPOIFSDocument(name, small_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
/* 312:    */         } else {
/* 313:527 */           document = new OPOIFSDocument(name, big_blocks.fetchBlocks(startBlock, headerPropertiesStartAt), size);
/* 314:    */         }
/* 315:532 */         parent.createDocument(document);
/* 316:    */       }
/* 317:    */     }
/* 318:    */   }
/* 319:    */   
/* 320:    */   public Object[] getViewableArray()
/* 321:    */   {
/* 322:548 */     if (preferArray()) {
/* 323:550 */       return getRoot().getViewableArray();
/* 324:    */     }
/* 325:552 */     return new Object[0];
/* 326:    */   }
/* 327:    */   
/* 328:    */   public Iterator<Object> getViewableIterator()
/* 329:    */   {
/* 330:565 */     if (!preferArray()) {
/* 331:567 */       return getRoot().getViewableIterator();
/* 332:    */     }
/* 333:569 */     return Collections.emptyList().iterator();
/* 334:    */   }
/* 335:    */   
/* 336:    */   public boolean preferArray()
/* 337:    */   {
/* 338:582 */     return getRoot().preferArray();
/* 339:    */   }
/* 340:    */   
/* 341:    */   public String getShortDescription()
/* 342:    */   {
/* 343:594 */     return "POIFS FileSystem";
/* 344:    */   }
/* 345:    */   
/* 346:    */   public int getBigBlockSize()
/* 347:    */   {
/* 348:601 */     return this.bigBlockSize.getBigBlockSize();
/* 349:    */   }
/* 350:    */   
/* 351:    */   public POIFSBigBlockSize getBigBlockSizeDetails()
/* 352:    */   {
/* 353:607 */     return this.bigBlockSize;
/* 354:    */   }
/* 355:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.OPOIFSFileSystem
 * JD-Core Version:    0.7.0.1
 */