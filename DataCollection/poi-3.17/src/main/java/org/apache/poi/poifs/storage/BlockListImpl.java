/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.util.Internal;
/*   5:    */ 
/*   6:    */ abstract class BlockListImpl
/*   7:    */   implements BlockList
/*   8:    */ {
/*   9:    */   private ListManagedBlock[] _blocks;
/*  10:    */   private BlockAllocationTableReader _bat;
/*  11:    */   
/*  12:    */   protected BlockListImpl()
/*  13:    */   {
/*  14: 35 */     this._blocks = new ListManagedBlock[0];
/*  15: 36 */     this._bat = null;
/*  16:    */   }
/*  17:    */   
/*  18:    */   protected void setBlocks(ListManagedBlock[] blocks)
/*  19:    */   {
/*  20: 46 */     this._blocks = ((ListManagedBlock[])blocks.clone());
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void zap(int index)
/*  24:    */   {
/*  25: 57 */     if ((index >= 0) && (index < this._blocks.length)) {
/*  26: 59 */       this._blocks[index] = null;
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   @Internal
/*  31:    */   public ListManagedBlock get(int index)
/*  32:    */   {
/*  33: 69 */     return this._blocks[index];
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ListManagedBlock remove(int index)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 85 */     ListManagedBlock result = null;
/*  40:    */     try
/*  41:    */     {
/*  42: 89 */       result = this._blocks[index];
/*  43: 90 */       if (result == null) {
/*  44: 92 */         throw new IOException("block[ " + index + " ] already removed - " + "does your POIFS have circular or duplicate block references?");
/*  45:    */       }
/*  46: 97 */       this._blocks[index] = null;
/*  47:    */     }
/*  48:    */     catch (ArrayIndexOutOfBoundsException ignored)
/*  49:    */     {
/*  50:101 */       throw new IOException("Cannot remove block[ " + index + " ]; out of range[ 0 - " + (this._blocks.length - 1) + " ]");
/*  51:    */     }
/*  52:105 */     return result;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ListManagedBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock)
/*  56:    */     throws IOException
/*  57:    */   {
/*  58:121 */     if (this._bat == null) {
/*  59:123 */       throw new IOException("Improperly initialized list: no block allocation table provided");
/*  60:    */     }
/*  61:126 */     return this._bat.fetchBlocks(startBlock, headerPropertiesStartBlock, this);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setBAT(BlockAllocationTableReader bat)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67:137 */     if (this._bat != null) {
/*  68:139 */       throw new IOException("Attempt to replace existing BlockAllocationTable");
/*  69:    */     }
/*  70:142 */     this._bat = bat;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int blockCount()
/*  74:    */   {
/*  75:149 */     return this._blocks.length;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected int remainingBlocks()
/*  79:    */   {
/*  80:155 */     int c = 0;
/*  81:156 */     for (int i = 0; i < this._blocks.length; i++) {
/*  82:157 */       if (this._blocks[i] != null) {
/*  83:157 */         c++;
/*  84:    */       }
/*  85:    */     }
/*  86:159 */     return c;
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BlockListImpl
 * JD-Core Version:    0.7.0.1
 */