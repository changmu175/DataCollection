/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   5:    */ import org.apache.poi.poifs.property.RootProperty;
/*   6:    */ 
/*   7:    */ public final class SmallBlockTableReader
/*   8:    */ {
/*   9:    */   private static BlockList prepareSmallDocumentBlocks(POIFSBigBlockSize bigBlockSize, RawDataBlockList blockList, RootProperty root, int sbatStart)
/*  10:    */     throws IOException
/*  11:    */   {
/*  12: 37 */     ListManagedBlock[] smallBlockBlocks = blockList.fetchBlocks(root.getStartBlock(), -1);
/*  13:    */     
/*  14:    */ 
/*  15:    */ 
/*  16: 41 */     BlockList list = new SmallDocumentBlockList(SmallDocumentBlock.extract(bigBlockSize, smallBlockBlocks));
/*  17:    */     
/*  18:    */ 
/*  19: 44 */     return list;
/*  20:    */   }
/*  21:    */   
/*  22:    */   private static BlockAllocationTableReader prepareReader(POIFSBigBlockSize bigBlockSize, RawDataBlockList blockList, BlockList list, RootProperty root, int sbatStart)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 53 */     return new BlockAllocationTableReader(bigBlockSize, blockList.fetchBlocks(sbatStart, -1), list);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static BlockAllocationTableReader _getSmallDocumentBlockReader(POIFSBigBlockSize bigBlockSize, RawDataBlockList blockList, RootProperty root, int sbatStart)
/*  29:    */     throws IOException
/*  30:    */   {
/*  31: 80 */     BlockList list = prepareSmallDocumentBlocks(bigBlockSize, blockList, root, sbatStart);
/*  32:    */     
/*  33: 82 */     return prepareReader(bigBlockSize, blockList, list, root, sbatStart);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static BlockList getSmallDocumentBlocks(POIFSBigBlockSize bigBlockSize, RawDataBlockList blockList, RootProperty root, int sbatStart)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39:105 */     BlockList list = prepareSmallDocumentBlocks(bigBlockSize, blockList, root, sbatStart);
/*  40:    */     
/*  41:107 */     prepareReader(bigBlockSize, blockList, list, root, sbatStart);
/*  42:108 */     return list;
/*  43:    */   }
/*  44:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.SmallBlockTableReader
 * JD-Core Version:    0.7.0.1
 */