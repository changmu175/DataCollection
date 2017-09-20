/*   1:    */ package org.apache.poi.poifs.filesystem;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.nio.ByteBuffer;
/*   5:    */ import org.apache.poi.poifs.storage.BATBlock.BATBlockAndIndex;
/*   6:    */ 
/*   7:    */ public abstract class BlockStore
/*   8:    */ {
/*   9:    */   protected abstract int getBlockStoreBlockSize();
/*  10:    */   
/*  11:    */   protected abstract ByteBuffer getBlockAt(int paramInt)
/*  12:    */     throws IOException;
/*  13:    */   
/*  14:    */   protected abstract ByteBuffer createBlockIfNeeded(int paramInt)
/*  15:    */     throws IOException;
/*  16:    */   
/*  17:    */   protected abstract BATBlock.BATBlockAndIndex getBATBlockAndIndex(int paramInt);
/*  18:    */   
/*  19:    */   protected abstract int getNextBlock(int paramInt);
/*  20:    */   
/*  21:    */   protected abstract void setNextBlock(int paramInt1, int paramInt2);
/*  22:    */   
/*  23:    */   protected abstract int getFreeBlock()
/*  24:    */     throws IOException;
/*  25:    */   
/*  26:    */   protected abstract ChainLoopDetector getChainLoopDetector()
/*  27:    */     throws IOException;
/*  28:    */   
/*  29:    */   protected class ChainLoopDetector
/*  30:    */   {
/*  31:    */     private boolean[] used_blocks;
/*  32:    */     
/*  33:    */     protected ChainLoopDetector(long rawSize)
/*  34:    */     {
/*  35: 83 */       int blkSize = BlockStore.this.getBlockStoreBlockSize();
/*  36: 84 */       int numBlocks = (int)(rawSize / blkSize);
/*  37: 85 */       if (rawSize % blkSize != 0L) {
/*  38: 86 */         numBlocks++;
/*  39:    */       }
/*  40: 88 */       this.used_blocks = new boolean[numBlocks];
/*  41:    */     }
/*  42:    */     
/*  43:    */     protected void claim(int offset)
/*  44:    */     {
/*  45: 91 */       if (offset >= this.used_blocks.length) {
/*  46: 95 */         return;
/*  47:    */       }
/*  48: 99 */       if (this.used_blocks[offset] != 0) {
/*  49:100 */         throw new IllegalStateException("Potential loop detected - Block " + offset + " was already claimed but was just requested again");
/*  50:    */       }
/*  51:105 */       this.used_blocks[offset] = true;
/*  52:    */     }
/*  53:    */   }
/*  54:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.BlockStore
 * JD-Core Version:    0.7.0.1
 */