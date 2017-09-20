/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  10:    */ 
/*  11:    */ public final class SmallDocumentBlock
/*  12:    */   implements BlockWritable, ListManagedBlock
/*  13:    */ {
/*  14:    */   private static final int BLOCK_SHIFT = 6;
/*  15:    */   private byte[] _data;
/*  16:    */   private static final byte _default_fill = -1;
/*  17:    */   private static final int _block_size = 64;
/*  18:    */   private static final int BLOCK_MASK = 63;
/*  19:    */   private final int _blocks_per_big_block;
/*  20:    */   private final POIFSBigBlockSize _bigBlockSize;
/*  21:    */   
/*  22:    */   private SmallDocumentBlock(POIFSBigBlockSize bigBlockSize, byte[] data, int index)
/*  23:    */   {
/*  24: 46 */     this(bigBlockSize);
/*  25: 47 */     System.arraycopy(data, index * 64, this._data, 0, 64);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected SmallDocumentBlock(POIFSBigBlockSize bigBlockSize)
/*  29:    */   {
/*  30: 52 */     this._bigBlockSize = bigBlockSize;
/*  31: 53 */     this._blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
/*  32: 54 */     this._data = new byte[64];
/*  33:    */   }
/*  34:    */   
/*  35:    */   private static int getBlocksPerBigBlock(POIFSBigBlockSize bigBlockSize)
/*  36:    */   {
/*  37: 59 */     return bigBlockSize.getBigBlockSize() / 64;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static SmallDocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, byte[] array, int size)
/*  41:    */   {
/*  42: 76 */     SmallDocumentBlock[] rval = new SmallDocumentBlock[(size + 64 - 1) / 64];
/*  43:    */     
/*  44: 78 */     int offset = 0;
/*  45: 80 */     for (int k = 0; k < rval.length; k++)
/*  46:    */     {
/*  47: 82 */       rval[k] = new SmallDocumentBlock(bigBlockSize);
/*  48: 83 */       if (offset < array.length)
/*  49:    */       {
/*  50: 85 */         int length = Math.min(64, array.length - offset);
/*  51:    */         
/*  52: 87 */         System.arraycopy(array, offset, rval[k]._data, 0, length);
/*  53: 88 */         if (length != 64) {
/*  54: 90 */           Arrays.fill(rval[k]._data, length, 64, (byte)-1);
/*  55:    */         }
/*  56:    */       }
/*  57:    */       else
/*  58:    */       {
/*  59: 96 */         Arrays.fill(rval[k]._data, (byte)-1);
/*  60:    */       }
/*  61: 98 */       offset += 64;
/*  62:    */     }
/*  63:100 */     return rval;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static int fill(POIFSBigBlockSize bigBlockSize, List<SmallDocumentBlock> blocks)
/*  67:    */   {
/*  68:113 */     int _blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
/*  69:    */     
/*  70:115 */     int count = blocks.size();
/*  71:116 */     int big_block_count = (count + _blocks_per_big_block - 1) / _blocks_per_big_block;
/*  72:    */     
/*  73:118 */     int full_count = big_block_count * _blocks_per_big_block;
/*  74:120 */     for (; count < full_count; count++) {
/*  75:122 */       blocks.add(makeEmptySmallDocumentBlock(bigBlockSize));
/*  76:    */     }
/*  77:124 */     return big_block_count;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static SmallDocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, BlockWritable[] store, int size)
/*  81:    */     throws IOException, ArrayIndexOutOfBoundsException
/*  82:    */   {
/*  83:144 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*  84:146 */     for (int j = 0; j < store.length; j++) {
/*  85:148 */       store[j].writeBlocks(stream);
/*  86:    */     }
/*  87:150 */     byte[] data = stream.toByteArray();
/*  88:151 */     SmallDocumentBlock[] rval = new SmallDocumentBlock[convertToBlockCount(size)];
/*  89:154 */     for (int index = 0; index < rval.length; index++) {
/*  90:156 */       rval[index] = new SmallDocumentBlock(bigBlockSize, data, index);
/*  91:    */     }
/*  92:158 */     return rval;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static List<SmallDocumentBlock> extract(POIFSBigBlockSize bigBlockSize, ListManagedBlock[] blocks)
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:172 */     int _blocks_per_big_block = getBlocksPerBigBlock(bigBlockSize);
/*  99:    */     
/* 100:174 */     List<SmallDocumentBlock> sdbs = new ArrayList();
/* 101:176 */     for (int j = 0; j < blocks.length; j++)
/* 102:    */     {
/* 103:178 */       byte[] data = blocks[j].getData();
/* 104:180 */       for (int k = 0; k < _blocks_per_big_block; k++) {
/* 105:182 */         sdbs.add(new SmallDocumentBlock(bigBlockSize, data, k));
/* 106:    */       }
/* 107:    */     }
/* 108:185 */     return sdbs;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static DataInputBlock getDataInputBlock(SmallDocumentBlock[] blocks, int offset)
/* 112:    */   {
/* 113:189 */     int firstBlockIndex = offset >> 6;
/* 114:190 */     int firstBlockOffset = offset & 0x3F;
/* 115:191 */     return new DataInputBlock(blocks[firstBlockIndex]._data, firstBlockOffset);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static int calcSize(int size)
/* 119:    */   {
/* 120:203 */     return size * 64;
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected int getSmallBlocksPerBigBlock()
/* 124:    */   {
/* 125:208 */     return this._blocks_per_big_block;
/* 126:    */   }
/* 127:    */   
/* 128:    */   private static SmallDocumentBlock makeEmptySmallDocumentBlock(POIFSBigBlockSize bigBlockSize)
/* 129:    */   {
/* 130:213 */     SmallDocumentBlock block = new SmallDocumentBlock(bigBlockSize);
/* 131:    */     
/* 132:215 */     Arrays.fill(block._data, (byte)-1);
/* 133:216 */     return block;
/* 134:    */   }
/* 135:    */   
/* 136:    */   private static int convertToBlockCount(int size)
/* 137:    */   {
/* 138:221 */     return (size + 64 - 1) / 64;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void writeBlocks(OutputStream stream)
/* 142:    */     throws IOException
/* 143:    */   {
/* 144:236 */     stream.write(this._data);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public byte[] getData()
/* 148:    */   {
/* 149:247 */     return this._data;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public POIFSBigBlockSize getBigBlockSize()
/* 153:    */   {
/* 154:251 */     return this._bigBlockSize;
/* 155:    */   }
/* 156:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.SmallDocumentBlock
 * JD-Core Version:    0.7.0.1
 */