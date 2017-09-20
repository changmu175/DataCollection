/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   7:    */ import org.apache.poi.poifs.filesystem.BATManaged;
/*   8:    */ import org.apache.poi.util.IntList;
/*   9:    */ 
/*  10:    */ public final class BlockAllocationTableWriter
/*  11:    */   implements BlockWritable, BATManaged
/*  12:    */ {
/*  13:    */   private IntList _entries;
/*  14:    */   private BATBlock[] _blocks;
/*  15:    */   private int _start_block;
/*  16:    */   private POIFSBigBlockSize _bigBlockSize;
/*  17:    */   
/*  18:    */   public BlockAllocationTableWriter(POIFSBigBlockSize bigBlockSize)
/*  19:    */   {
/*  20: 55 */     this._bigBlockSize = bigBlockSize;
/*  21: 56 */     this._start_block = -2;
/*  22: 57 */     this._entries = new IntList();
/*  23: 58 */     this._blocks = new BATBlock[0];
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int createBlocks()
/*  27:    */   {
/*  28: 68 */     int xbat_blocks = 0;
/*  29: 69 */     int bat_blocks = 0;
/*  30:    */     for (;;)
/*  31:    */     {
/*  32: 73 */       int calculated_bat_blocks = BATBlock.calculateStorageRequirements(this._bigBlockSize, bat_blocks + xbat_blocks + this._entries.size());
/*  33:    */       
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37: 78 */       int calculated_xbat_blocks = HeaderBlockWriter.calculateXBATStorageRequirements(this._bigBlockSize, calculated_bat_blocks);
/*  38: 82 */       if ((bat_blocks == calculated_bat_blocks) && (xbat_blocks == calculated_xbat_blocks)) {
/*  39:    */         break;
/*  40:    */       }
/*  41: 89 */       bat_blocks = calculated_bat_blocks;
/*  42: 90 */       xbat_blocks = calculated_xbat_blocks;
/*  43:    */     }
/*  44: 92 */     int startBlock = allocateSpace(bat_blocks);
/*  45:    */     
/*  46: 94 */     allocateSpace(xbat_blocks);
/*  47: 95 */     simpleCreateBlocks();
/*  48: 96 */     return startBlock;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int allocateSpace(int blockCount)
/*  52:    */   {
/*  53:108 */     int startBlock = this._entries.size();
/*  54:110 */     if (blockCount > 0)
/*  55:    */     {
/*  56:112 */       int limit = blockCount - 1;
/*  57:113 */       int index = startBlock + 1;
/*  58:115 */       for (int k = 0; k < limit; k++) {
/*  59:117 */         this._entries.add(index++);
/*  60:    */       }
/*  61:119 */       this._entries.add(-2);
/*  62:    */     }
/*  63:121 */     return startBlock;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getStartBlock()
/*  67:    */   {
/*  68:131 */     return this._start_block;
/*  69:    */   }
/*  70:    */   
/*  71:    */   void simpleCreateBlocks()
/*  72:    */   {
/*  73:139 */     this._blocks = BATBlock.createBATBlocks(this._bigBlockSize, this._entries.toArray());
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void writeBlocks(OutputStream stream)
/*  77:    */     throws IOException
/*  78:    */   {
/*  79:154 */     for (int j = 0; j < this._blocks.length; j++) {
/*  80:156 */       this._blocks[j].writeBlocks(stream);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static void writeBlock(BATBlock bat, ByteBuffer block)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:166 */     bat.writeData(block);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int countBlocks()
/*  91:    */   {
/*  92:176 */     return this._blocks.length;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setStartBlock(int start_block)
/*  96:    */   {
/*  97:184 */     this._start_block = start_block;
/*  98:    */   }
/*  99:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BlockAllocationTableWriter
 * JD-Core Version:    0.7.0.1
 */