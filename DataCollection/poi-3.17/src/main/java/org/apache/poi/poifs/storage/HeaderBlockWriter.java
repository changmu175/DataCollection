/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.nio.ByteBuffer;
/*   7:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   8:    */ 
/*   9:    */ public class HeaderBlockWriter
/*  10:    */   implements HeaderBlockConstants, BlockWritable
/*  11:    */ {
/*  12:    */   private final HeaderBlock _header_block;
/*  13:    */   
/*  14:    */   public HeaderBlockWriter(POIFSBigBlockSize bigBlockSize)
/*  15:    */   {
/*  16: 44 */     this._header_block = new HeaderBlock(bigBlockSize);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public HeaderBlockWriter(HeaderBlock headerBlock)
/*  20:    */   {
/*  21: 53 */     this._header_block = headerBlock;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public BATBlock[] setBATBlocks(int blockCount, int startBlock)
/*  25:    */   {
/*  26: 72 */     POIFSBigBlockSize bigBlockSize = this._header_block.getBigBlockSize();
/*  27:    */     
/*  28: 74 */     this._header_block.setBATCount(blockCount);
/*  29:    */     
/*  30:    */ 
/*  31: 77 */     int limit = Math.min(blockCount, 109);
/*  32: 78 */     int[] bat_blocks = new int[limit];
/*  33: 79 */     for (int j = 0; j < limit; j++) {
/*  34: 80 */       bat_blocks[j] = (startBlock + j);
/*  35:    */     }
/*  36: 82 */     this._header_block.setBATArray(bat_blocks);
/*  37:    */     BATBlock[] rvalue;
/*  38: 85 */     if (blockCount > 109)
/*  39:    */     {
/*  40: 87 */       int excess_blocks = blockCount - 109;
/*  41: 88 */       int[] excess_block_array = new int[excess_blocks];
/*  42: 90 */       for (int j = 0; j < excess_blocks; j++) {
/*  43: 92 */         excess_block_array[j] = (startBlock + j + 109);
/*  44:    */       }
/*  45: 95 */       BATBlock[] rvalue = BATBlock.createXBATBlocks(bigBlockSize, excess_block_array, startBlock + blockCount);
/*  46:    */       
/*  47: 97 */       this._header_block.setXBATStart(startBlock + blockCount);
/*  48:    */     }
/*  49:    */     else
/*  50:    */     {
/*  51:101 */       rvalue = BATBlock.createXBATBlocks(bigBlockSize, new int[0], 0);
/*  52:102 */       this._header_block.setXBATStart(-2);
/*  53:    */     }
/*  54:104 */     this._header_block.setXBATCount(rvalue.length);
/*  55:105 */     return rvalue;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setPropertyStart(int startBlock)
/*  59:    */   {
/*  60:116 */     this._header_block.setPropertyStart(startBlock);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setSBATStart(int startBlock)
/*  64:    */   {
/*  65:127 */     this._header_block.setSBATStart(startBlock);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setSBATBlockCount(int count)
/*  69:    */   {
/*  70:137 */     this._header_block.setSBATBlockCount(count);
/*  71:    */   }
/*  72:    */   
/*  73:    */   static int calculateXBATStorageRequirements(POIFSBigBlockSize bigBlockSize, int blockCount)
/*  74:    */   {
/*  75:151 */     return blockCount > 109 ? BATBlock.calculateXBATStorageRequirements(bigBlockSize, blockCount - 109) : 0;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void writeBlocks(OutputStream stream)
/*  79:    */     throws IOException
/*  80:    */   {
/*  81:171 */     this._header_block.writeData(stream);
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void writeBlock(ByteBuffer block)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:185 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(this._header_block.getBigBlockSize().getBigBlockSize());
/*  88:    */     
/*  89:    */ 
/*  90:188 */     this._header_block.writeData(baos);
/*  91:    */     
/*  92:190 */     block.put(baos.toByteArray());
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.HeaderBlockWriter
 * JD-Core Version:    0.7.0.1
 */