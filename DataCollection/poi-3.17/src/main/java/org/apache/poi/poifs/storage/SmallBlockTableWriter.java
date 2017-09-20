/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   8:    */ import org.apache.poi.poifs.filesystem.BATManaged;
/*   9:    */ import org.apache.poi.poifs.filesystem.OPOIFSDocument;
/*  10:    */ import org.apache.poi.poifs.property.RootProperty;
/*  11:    */ 
/*  12:    */ public class SmallBlockTableWriter
/*  13:    */   implements BlockWritable, BATManaged
/*  14:    */ {
/*  15:    */   private BlockAllocationTableWriter _sbat;
/*  16:    */   private List<SmallDocumentBlock> _small_blocks;
/*  17:    */   private int _big_block_count;
/*  18:    */   private RootProperty _root;
/*  19:    */   
/*  20:    */   public SmallBlockTableWriter(POIFSBigBlockSize bigBlockSize, List<OPOIFSDocument> documents, RootProperty root)
/*  21:    */   {
/*  22: 57 */     this._sbat = new BlockAllocationTableWriter(bigBlockSize);
/*  23: 58 */     this._small_blocks = new ArrayList();
/*  24: 59 */     this._root = root;
/*  25: 61 */     for (OPOIFSDocument doc : documents)
/*  26:    */     {
/*  27: 63 */       SmallDocumentBlock[] blocks = doc.getSmallBlocks();
/*  28: 65 */       if (blocks.length != 0)
/*  29:    */       {
/*  30: 67 */         doc.setStartBlock(this._sbat.allocateSpace(blocks.length));
/*  31: 68 */         for (int j = 0; j < blocks.length; j++) {
/*  32: 70 */           this._small_blocks.add(blocks[j]);
/*  33:    */         }
/*  34:    */       }
/*  35:    */       else
/*  36:    */       {
/*  37: 73 */         doc.setStartBlock(-2);
/*  38:    */       }
/*  39:    */     }
/*  40: 76 */     this._sbat.simpleCreateBlocks();
/*  41: 77 */     this._root.setSize(this._small_blocks.size());
/*  42: 78 */     this._big_block_count = SmallDocumentBlock.fill(bigBlockSize, this._small_blocks);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getSBATBlockCount()
/*  46:    */   {
/*  47: 89 */     return (this._big_block_count + 15) / 16;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public BlockAllocationTableWriter getSBAT()
/*  51:    */   {
/*  52:100 */     return this._sbat;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int countBlocks()
/*  56:    */   {
/*  57:113 */     return this._big_block_count;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setStartBlock(int start_block)
/*  61:    */   {
/*  62:124 */     this._root.setStartBlock(start_block);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void writeBlocks(OutputStream stream)
/*  66:    */     throws IOException
/*  67:    */   {
/*  68:143 */     for (BlockWritable block : this._small_blocks) {
/*  69:144 */       block.writeBlocks(stream);
/*  70:    */     }
/*  71:    */   }
/*  72:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.SmallBlockTableWriter
 * JD-Core Version:    0.7.0.1
 */