/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   7:    */ import org.apache.poi.util.IntList;
/*   8:    */ import org.apache.poi.util.Internal;
/*   9:    */ import org.apache.poi.util.LittleEndian;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ public final class BlockAllocationTableReader
/*  14:    */ {
/*  15: 44 */   private static final POILogger _logger = POILogFactory.getLogger(BlockAllocationTableReader.class);
/*  16:    */   private static final int MAX_BLOCK_COUNT = 65535;
/*  17:    */   private final IntList _entries;
/*  18:    */   private POIFSBigBlockSize bigBlockSize;
/*  19:    */   
/*  20:    */   public BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize, int block_count, int[] block_array, int xbat_count, int xbat_index, BlockList raw_block_list)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 81 */     this(bigBlockSize);
/*  24:    */     
/*  25: 83 */     sanityCheckBlockCount(block_count);
/*  26:    */     
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33: 91 */     int limit = Math.min(block_count, block_array.length);
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37: 95 */     RawDataBlock[] blocks = new RawDataBlock[block_count];
/*  38: 98 */     for (int block_index = 0; block_index < limit; block_index++)
/*  39:    */     {
/*  40:101 */       int nextOffset = block_array[block_index];
/*  41:102 */       if (nextOffset > raw_block_list.blockCount()) {
/*  42:103 */         throw new IOException("Your file contains " + raw_block_list.blockCount() + " sectors, but the initial DIFAT array at index " + block_index + " referenced block # " + nextOffset + ". This isn't allowed and " + " your file is corrupt");
/*  43:    */       }
/*  44:109 */       blocks[block_index] = ((RawDataBlock)raw_block_list.remove(nextOffset));
/*  45:    */     }
/*  46:114 */     if (block_index < block_count)
/*  47:    */     {
/*  48:118 */       if (xbat_index < 0) {
/*  49:120 */         throw new IOException("BAT count exceeds limit, yet XBAT index indicates no valid entries");
/*  50:    */       }
/*  51:123 */       int chain_index = xbat_index;
/*  52:124 */       int max_entries_per_block = bigBlockSize.getXBATEntriesPerBlock();
/*  53:125 */       int chain_index_offset = bigBlockSize.getNextXBATChainOffset();
/*  54:130 */       for (int j = 0; j < xbat_count; j++)
/*  55:    */       {
/*  56:132 */         limit = Math.min(block_count - block_index, max_entries_per_block);
/*  57:    */         
/*  58:134 */         byte[] data = raw_block_list.remove(chain_index).getData();
/*  59:135 */         int offset = 0;
/*  60:137 */         for (int k = 0; k < limit; k++)
/*  61:    */         {
/*  62:139 */           blocks[(block_index++)] = ((RawDataBlock)raw_block_list.remove(LittleEndian.getInt(data, offset)));
/*  63:    */           
/*  64:    */ 
/*  65:142 */           offset += 4;
/*  66:    */         }
/*  67:144 */         chain_index = LittleEndian.getInt(data, chain_index_offset);
/*  68:145 */         if (chain_index == -2) {
/*  69:    */           break;
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:151 */     if (block_index != block_count) {
/*  74:153 */       throw new IOException("Could not find all blocks");
/*  75:    */     }
/*  76:158 */     setEntries(blocks, raw_block_list);
/*  77:    */   }
/*  78:    */   
/*  79:    */   BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks, BlockList raw_block_list)
/*  80:    */     throws IOException
/*  81:    */   {
/*  82:171 */     this(bigBlockSize);
/*  83:172 */     setEntries(blocks, raw_block_list);
/*  84:    */   }
/*  85:    */   
/*  86:    */   BlockAllocationTableReader(POIFSBigBlockSize bigBlockSize)
/*  87:    */   {
/*  88:176 */     this.bigBlockSize = bigBlockSize;
/*  89:177 */     this._entries = new IntList();
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static void sanityCheckBlockCount(int block_count)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:181 */     if (block_count <= 0) {
/*  96:182 */       throw new IOException("Illegal block count; minimum count is 1, got " + block_count + " instead");
/*  97:    */     }
/*  98:187 */     if (block_count > 65535) {
/*  99:188 */       throw new IOException("Block count " + block_count + " is too high. POI maximum is " + 65535 + ".");
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   ListManagedBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock, BlockList blockList)
/* 104:    */     throws IOException
/* 105:    */   {
/* 106:209 */     List<ListManagedBlock> blocks = new ArrayList();
/* 107:210 */     int currentBlock = startBlock;
/* 108:211 */     boolean firstPass = true;
/* 109:212 */     ListManagedBlock dataBlock = null;
/* 110:218 */     while (currentBlock != -2) {
/* 111:    */       try
/* 112:    */       {
/* 113:221 */         dataBlock = blockList.remove(currentBlock);
/* 114:222 */         blocks.add(dataBlock);
/* 115:    */         
/* 116:224 */         currentBlock = this._entries.get(currentBlock);
/* 117:225 */         firstPass = false;
/* 118:    */       }
/* 119:    */       catch (IOException e)
/* 120:    */       {
/* 121:227 */         if (currentBlock == headerPropertiesStartBlock)
/* 122:    */         {
/* 123:229 */           _logger.log(5, new Object[] { "Warning, header block comes after data blocks in POIFS block listing" });
/* 124:230 */           currentBlock = -2;
/* 125:    */         }
/* 126:231 */         else if ((currentBlock == 0) && (firstPass))
/* 127:    */         {
/* 128:234 */           _logger.log(5, new Object[] { "Warning, incorrectly terminated empty data blocks in POIFS block listing (should end at -2, ended at 0)" });
/* 129:235 */           currentBlock = -2;
/* 130:    */         }
/* 131:    */         else
/* 132:    */         {
/* 133:238 */           throw e;
/* 134:    */         }
/* 135:    */       }
/* 136:    */     }
/* 137:243 */     return (ListManagedBlock[])blocks.toArray(new ListManagedBlock[blocks.size()]);
/* 138:    */   }
/* 139:    */   
/* 140:    */   boolean isUsed(int index)
/* 141:    */   {
/* 142:    */     try
/* 143:    */     {
/* 144:258 */       return this._entries.get(index) != -1;
/* 145:    */     }
/* 146:    */     catch (IndexOutOfBoundsException e) {}
/* 147:261 */     return false;
/* 148:    */   }
/* 149:    */   
/* 150:    */   int getNextBlockIndex(int index)
/* 151:    */     throws IOException
/* 152:    */   {
/* 153:277 */     if (isUsed(index)) {
/* 154:278 */       return this._entries.get(index);
/* 155:    */     }
/* 156:280 */     throw new IOException("index " + index + " is unused");
/* 157:    */   }
/* 158:    */   
/* 159:    */   private void setEntries(ListManagedBlock[] blocks, BlockList raw_blocks)
/* 160:    */     throws IOException
/* 161:    */   {
/* 162:291 */     int limit = this.bigBlockSize.getBATEntriesPerBlock();
/* 163:293 */     for (int block_index = 0; block_index < blocks.length; block_index++)
/* 164:    */     {
/* 165:295 */       byte[] data = blocks[block_index].getData();
/* 166:296 */       int offset = 0;
/* 167:298 */       for (int k = 0; k < limit; k++)
/* 168:    */       {
/* 169:300 */         int entry = LittleEndian.getInt(data, offset);
/* 170:302 */         if (entry == -1) {
/* 171:304 */           raw_blocks.zap(this._entries.size());
/* 172:    */         }
/* 173:306 */         this._entries.add(entry);
/* 174:307 */         offset += 4;
/* 175:    */       }
/* 176:311 */       blocks[block_index] = null;
/* 177:    */     }
/* 178:313 */     raw_blocks.setBAT(this);
/* 179:    */   }
/* 180:    */   
/* 181:    */   @Internal
/* 182:    */   public IntList getEntries()
/* 183:    */   {
/* 184:318 */     return this._entries;
/* 185:    */   }
/* 186:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BlockAllocationTableReader
 * JD-Core Version:    0.7.0.1
 */