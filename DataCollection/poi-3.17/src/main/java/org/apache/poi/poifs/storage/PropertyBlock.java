/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   7:    */ import org.apache.poi.poifs.property.Property;
/*   8:    */ 
/*   9:    */ public final class PropertyBlock
/*  10:    */   extends BigBlock
/*  11:    */ {
/*  12:    */   private Property[] _properties;
/*  13:    */   
/*  14:    */   private PropertyBlock(POIFSBigBlockSize bigBlockSize, Property[] properties, int offset)
/*  15:    */   {
/*  16: 44 */     super(bigBlockSize);
/*  17:    */     
/*  18: 46 */     this._properties = new Property[bigBlockSize.getPropertiesPerBlock()];
/*  19: 47 */     for (int j = 0; j < this._properties.length; j++) {
/*  20: 49 */       this._properties[j] = properties[(j + offset)];
/*  21:    */     }
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static BlockWritable[] createPropertyBlockArray(POIFSBigBlockSize bigBlockSize, List<Property> properties)
/*  25:    */   {
/*  26: 67 */     int _properties_per_block = bigBlockSize.getPropertiesPerBlock();
/*  27: 68 */     int block_count = (properties.size() + _properties_per_block - 1) / _properties_per_block;
/*  28:    */     
/*  29:    */ 
/*  30: 71 */     Property[] to_be_written = new Property[block_count * _properties_per_block];
/*  31:    */     
/*  32:    */ 
/*  33: 74 */     System.arraycopy(properties.toArray(new Property[0]), 0, to_be_written, 0, properties.size());
/*  34: 76 */     for (int j = properties.size(); j < to_be_written.length; j++) {
/*  35: 81 */       to_be_written[j = new Property()
/*  36:    */       {
/*  37:    */         protected void preWrite() {}
/*  38:    */         
/*  39:    */         public boolean isDirectory()
/*  40:    */         {
/*  41: 89 */           return false;
/*  42:    */         }
/*  43:    */       };
/*  44:    */     }
/*  45: 93 */     BlockWritable[] rvalue = new BlockWritable[block_count];
/*  46: 95 */     for (int j = 0; j < block_count; j++) {
/*  47: 97 */       rvalue[j] = new PropertyBlock(bigBlockSize, to_be_written, j * _properties_per_block);
/*  48:    */     }
/*  49:100 */     return rvalue;
/*  50:    */   }
/*  51:    */   
/*  52:    */   void writeData(OutputStream stream)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:118 */     int _properties_per_block = this.bigBlockSize.getPropertiesPerBlock();
/*  56:119 */     for (int j = 0; j < _properties_per_block; j++) {
/*  57:121 */       this._properties[j].writeData(stream);
/*  58:    */     }
/*  59:    */   }
/*  60:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.PropertyBlock
 * JD-Core Version:    0.7.0.1
 */