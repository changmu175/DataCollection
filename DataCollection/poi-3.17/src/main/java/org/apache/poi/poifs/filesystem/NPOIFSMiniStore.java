/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.nio.ByteBuffer;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   8:    */ import org.apache.poi.poifs.property.NPropertyTable;
/*   9:    */ import org.apache.poi.poifs.property.RootProperty;
/*  10:    */ import org.apache.poi.poifs.storage.BATBlock;
/*  11:    */ import org.apache.poi.poifs.storage.BATBlock.BATBlockAndIndex;
/*  12:    */ import org.apache.poi.poifs.storage.BlockAllocationTableWriter;
/*  13:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  14:    */ 
/*  15:    */ public class NPOIFSMiniStore
/*  16:    */   extends BlockStore
/*  17:    */ {
/*  18:    */   private NPOIFSFileSystem _filesystem;
/*  19:    */   private NPOIFSStream _mini_stream;
/*  20:    */   private List<BATBlock> _sbat_blocks;
/*  21:    */   private HeaderBlock _header;
/*  22:    */   private RootProperty _root;
/*  23:    */   
/*  24:    */   protected NPOIFSMiniStore(NPOIFSFileSystem filesystem, RootProperty root, List<BATBlock> sbats, HeaderBlock header)
/*  25:    */   {
/*  26: 49 */     this._filesystem = filesystem;
/*  27: 50 */     this._sbat_blocks = sbats;
/*  28: 51 */     this._header = header;
/*  29: 52 */     this._root = root;
/*  30:    */     
/*  31: 54 */     this._mini_stream = new NPOIFSStream(filesystem, root.getStartBlock());
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected ByteBuffer getBlockAt(int offset)
/*  35:    */     throws IOException
/*  36:    */   {
/*  37: 62 */     int byteOffset = offset * 64;
/*  38: 63 */     int bigBlockNumber = byteOffset / this._filesystem.getBigBlockSize();
/*  39: 64 */     int bigBlockOffset = byteOffset % this._filesystem.getBigBlockSize();
/*  40:    */     
/*  41:    */ 
/*  42: 67 */     Iterator<ByteBuffer> it = this._mini_stream.getBlockIterator();
/*  43: 68 */     for (int i = 0; i < bigBlockNumber; i++) {
/*  44: 69 */       it.next();
/*  45:    */     }
/*  46: 71 */     ByteBuffer dataBlock = (ByteBuffer)it.next();
/*  47: 72 */     if (dataBlock == null) {
/*  48: 73 */       throw new IndexOutOfBoundsException("Big block " + bigBlockNumber + " outside stream");
/*  49:    */     }
/*  50: 77 */     dataBlock.position(dataBlock.position() + bigBlockOffset);
/*  51:    */     
/*  52:    */ 
/*  53: 80 */     ByteBuffer miniBuffer = dataBlock.slice();
/*  54: 81 */     miniBuffer.limit(64);
/*  55: 82 */     return miniBuffer;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected ByteBuffer createBlockIfNeeded(int offset)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61: 89 */     boolean firstInStore = false;
/*  62: 90 */     if (this._mini_stream.getStartBlock() == -2) {
/*  63: 91 */       firstInStore = true;
/*  64:    */     }
/*  65: 95 */     if (!firstInStore) {
/*  66:    */       try
/*  67:    */       {
/*  68: 97 */         return getBlockAt(offset);
/*  69:    */       }
/*  70:    */       catch (IndexOutOfBoundsException e) {}
/*  71:    */     }
/*  72:106 */     int newBigBlock = this._filesystem.getFreeBlock();
/*  73:107 */     this._filesystem.createBlockIfNeeded(newBigBlock);
/*  74:110 */     if (firstInStore)
/*  75:    */     {
/*  76:111 */       this._filesystem._get_property_table().getRoot().setStartBlock(newBigBlock);
/*  77:112 */       this._mini_stream = new NPOIFSStream(this._filesystem, newBigBlock);
/*  78:    */     }
/*  79:    */     else
/*  80:    */     {
/*  81:115 */       ChainLoopDetector loopDetector = this._filesystem.getChainLoopDetector();
/*  82:116 */       int block = this._mini_stream.getStartBlock();
/*  83:    */       for (;;)
/*  84:    */       {
/*  85:118 */         loopDetector.claim(block);
/*  86:119 */         int next = this._filesystem.getNextBlock(block);
/*  87:120 */         if (next == -2) {
/*  88:    */           break;
/*  89:    */         }
/*  90:123 */         block = next;
/*  91:    */       }
/*  92:125 */       this._filesystem.setNextBlock(block, newBigBlock);
/*  93:    */     }
/*  94:129 */     this._filesystem.setNextBlock(newBigBlock, -2);
/*  95:    */     
/*  96:    */ 
/*  97:132 */     return createBlockIfNeeded(offset);
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected BATBlockAndIndex getBATBlockAndIndex(int offset)
/* 101:    */   {
/* 102:140 */     return BATBlock.getSBATBlockAndIndex(offset, this._header, this._sbat_blocks);
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected int getNextBlock(int offset)
/* 106:    */   {
/* 107:149 */     BATBlockAndIndex bai = getBATBlockAndIndex(offset);
/* 108:150 */     return bai.getBlock().getValueAt(bai.getIndex());
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected void setNextBlock(int offset, int nextBlock)
/* 112:    */   {
/* 113:157 */     BATBlockAndIndex bai = getBATBlockAndIndex(offset);
/* 114:158 */     bai.getBlock().setValueAt(bai.getIndex(), nextBlock);
/* 115:    */   }
/* 116:    */   
/* 117:    */   protected int getFreeBlock()
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:169 */     int sectorsPerSBAT = this._filesystem.getBigBlockSizeDetails().getBATEntriesPerBlock();
/* 121:    */     
/* 122:    */ 
/* 123:172 */     int offset = 0;
/* 124:173 */     for (int i = 0; i < this._sbat_blocks.size(); i++)
/* 125:    */     {
/* 126:175 */       BATBlock sbat = (BATBlock)this._sbat_blocks.get(i);
/* 127:176 */       if (sbat.hasFreeSectors()) {
/* 128:178 */         for (int j = 0; j < sectorsPerSBAT; j++)
/* 129:    */         {
/* 130:179 */           int sbatValue = sbat.getValueAt(j);
/* 131:180 */           if (sbatValue == -1) {
/* 132:182 */             return offset + j;
/* 133:    */           }
/* 134:    */         }
/* 135:    */       }
/* 136:188 */       offset += sectorsPerSBAT;
/* 137:    */     }
/* 138:196 */     BATBlock newSBAT = BATBlock.createEmptyBATBlock(this._filesystem.getBigBlockSizeDetails(), false);
/* 139:197 */     int batForSBAT = this._filesystem.getFreeBlock();
/* 140:198 */     newSBAT.setOurBlockIndex(batForSBAT);
/* 141:201 */     if (this._header.getSBATCount() == 0)
/* 142:    */     {
/* 143:203 */       this._header.setSBATStart(batForSBAT);
/* 144:204 */       this._header.setSBATBlockCount(1);
/* 145:    */     }
/* 146:    */     else
/* 147:    */     {
/* 148:207 */       ChainLoopDetector loopDetector = this._filesystem.getChainLoopDetector();
/* 149:208 */       int batOffset = this._header.getSBATStart();
/* 150:    */       for (;;)
/* 151:    */       {
/* 152:210 */         loopDetector.claim(batOffset);
/* 153:211 */         int nextBat = this._filesystem.getNextBlock(batOffset);
/* 154:212 */         if (nextBat == -2) {
/* 155:    */           break;
/* 156:    */         }
/* 157:215 */         batOffset = nextBat;
/* 158:    */       }
/* 159:219 */       this._filesystem.setNextBlock(batOffset, batForSBAT);
/* 160:    */       
/* 161:    */ 
/* 162:222 */       this._header.setSBATBlockCount(this._header.getSBATCount() + 1);
/* 163:    */     }
/* 164:228 */     this._filesystem.setNextBlock(batForSBAT, -2);
/* 165:229 */     this._sbat_blocks.add(newSBAT);
/* 166:    */     
/* 167:    */ 
/* 168:232 */     return offset;
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected ChainLoopDetector getChainLoopDetector()
/* 172:    */     throws IOException
/* 173:    */   {
/* 174:237 */     return new ChainLoopDetector(this, this._root.getSize());
/* 175:    */   }
/* 176:    */   
/* 177:    */   protected int getBlockStoreBlockSize()
/* 178:    */   {
/* 179:241 */     return 64;
/* 180:    */   }
/* 181:    */   
/* 182:    */   protected void syncWithDataSource()
/* 183:    */     throws IOException
/* 184:    */   {
/* 185:250 */     int blocksUsed = 0;
/* 186:251 */     for (BATBlock sbat : this._sbat_blocks)
/* 187:    */     {
/* 188:252 */       ByteBuffer block = this._filesystem.getBlockAt(sbat.getOurBlockIndex());
/* 189:253 */       BlockAllocationTableWriter.writeBlock(sbat, block);
/* 190:255 */       if (!sbat.hasFreeSectors()) {
/* 191:256 */         blocksUsed += this._filesystem.getBigBlockSizeDetails().getBATEntriesPerBlock();
/* 192:    */       } else {
/* 193:258 */         blocksUsed += sbat.getUsedSectors(false);
/* 194:    */       }
/* 195:    */     }
/* 196:263 */     this._filesystem._get_property_table().getRoot().setSize(blocksUsed);
/* 197:    */   }
/* 198:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.poifs.filesystem.NPOIFSMiniStore

 * JD-Core Version:    0.7.0.1

 */