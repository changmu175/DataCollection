/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   7:    */ import org.apache.poi.poifs.storage.BlockWritable;
/*   8:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*   9:    */ import org.apache.poi.poifs.storage.PropertyBlock;
/*  10:    */ import org.apache.poi.poifs.storage.RawDataBlockList;
/*  11:    */ 
/*  12:    */ public final class PropertyTable
/*  13:    */   extends PropertyTableBase
/*  14:    */   implements BlockWritable
/*  15:    */ {
/*  16:    */   private POIFSBigBlockSize _bigBigBlockSize;
/*  17:    */   private BlockWritable[] _blocks;
/*  18:    */   
/*  19:    */   public PropertyTable(HeaderBlock headerBlock)
/*  20:    */   {
/*  21: 42 */     super(headerBlock);
/*  22: 43 */     this._bigBigBlockSize = headerBlock.getBigBlockSize();
/*  23: 44 */     this._blocks = null;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public PropertyTable(HeaderBlock headerBlock, RawDataBlockList blockList)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 62 */     super(headerBlock, PropertyFactory.convertToProperties(blockList.fetchBlocks(headerBlock.getPropertyStart(), -1)));
/*  30:    */     
/*  31:    */ 
/*  32:    */ 
/*  33:    */ 
/*  34:    */ 
/*  35: 68 */     this._bigBigBlockSize = headerBlock.getBigBlockSize();
/*  36: 69 */     this._blocks = null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void preWrite()
/*  40:    */   {
/*  41: 77 */     Property[] properties = (Property[])this._properties.toArray(new Property[this._properties.size()]);
/*  42: 80 */     for (int k = 0; k < properties.length; k++) {
/*  43: 82 */       properties[k].setIndex(k);
/*  44:    */     }
/*  45: 86 */     this._blocks = PropertyBlock.createPropertyBlockArray(this._bigBigBlockSize, this._properties);
/*  46: 89 */     for (Property property : properties) {
/*  47: 90 */       property.preWrite();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public int countBlocks()
/*  52:    */   {
/*  53:101 */     return this._blocks == null ? 0 : this._blocks.length;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void writeBlocks(OutputStream stream)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:117 */     if (this._blocks != null) {
/*  60:119 */       for (BlockWritable _block : this._blocks) {
/*  61:120 */         _block.writeBlocks(stream);
/*  62:    */       }
/*  63:    */     }
/*  64:    */   }
/*  65:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.PropertyTable
 * JD-Core Version:    0.7.0.1
 */