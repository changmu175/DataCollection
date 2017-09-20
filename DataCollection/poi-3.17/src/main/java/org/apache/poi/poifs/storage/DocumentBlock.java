/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   8:    */ import org.apache.poi.poifs.common.POIFSConstants;
/*   9:    */ import org.apache.poi.util.IOUtils;
/*  10:    */ 
/*  11:    */ public final class DocumentBlock
/*  12:    */   extends BigBlock
/*  13:    */ {
/*  14:    */   private static final byte _default_value = -1;
/*  15:    */   private byte[] _data;
/*  16:    */   private int _bytes_read;
/*  17:    */   
/*  18:    */   public DocumentBlock(RawDataBlock block)
/*  19:    */     throws IOException
/*  20:    */   {
/*  21: 50 */     super(block.getBigBlockSize() == 512 ? POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS : POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS);
/*  22:    */     
/*  23:    */ 
/*  24:    */ 
/*  25:    */ 
/*  26: 55 */     this._data = block.getData();
/*  27: 56 */     this._bytes_read = this._data.length;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DocumentBlock(InputStream stream, POIFSBigBlockSize bigBlockSize)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 70 */     this(bigBlockSize);
/*  34: 71 */     int count = IOUtils.readFully(stream, this._data);
/*  35:    */     
/*  36: 73 */     this._bytes_read = (count == -1 ? 0 : count);
/*  37:    */   }
/*  38:    */   
/*  39:    */   private DocumentBlock(POIFSBigBlockSize bigBlockSize)
/*  40:    */   {
/*  41: 83 */     super(bigBlockSize);
/*  42: 84 */     this._data = new byte[bigBlockSize.getBigBlockSize()];
/*  43: 85 */     Arrays.fill(this._data, (byte)-1);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int size()
/*  47:    */   {
/*  48: 96 */     return this._bytes_read;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean partiallyRead()
/*  52:    */   {
/*  53:107 */     return this._bytes_read != this.bigBlockSize.getBigBlockSize();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static byte getFillByte()
/*  57:    */   {
/*  58:116 */     return -1;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static DocumentBlock[] convert(POIFSBigBlockSize bigBlockSize, byte[] array, int size)
/*  62:    */   {
/*  63:134 */     DocumentBlock[] rval = new DocumentBlock[(size + bigBlockSize.getBigBlockSize() - 1) / bigBlockSize.getBigBlockSize()];
/*  64:    */     
/*  65:136 */     int offset = 0;
/*  66:138 */     for (int k = 0; k < rval.length; k++)
/*  67:    */     {
/*  68:140 */       rval[k] = new DocumentBlock(bigBlockSize);
/*  69:141 */       if (offset < array.length)
/*  70:    */       {
/*  71:143 */         int length = Math.min(bigBlockSize.getBigBlockSize(), array.length - offset);
/*  72:    */         
/*  73:    */ 
/*  74:146 */         System.arraycopy(array, offset, rval[k]._data, 0, length);
/*  75:147 */         if (length != bigBlockSize.getBigBlockSize()) {
/*  76:149 */           Arrays.fill(rval[k]._data, length, bigBlockSize.getBigBlockSize(), (byte)-1);
/*  77:    */         }
/*  78:    */       }
/*  79:    */       else
/*  80:    */       {
/*  81:156 */         Arrays.fill(rval[k]._data, (byte)-1);
/*  82:    */       }
/*  83:158 */       offset += bigBlockSize.getBigBlockSize();
/*  84:    */     }
/*  85:160 */     return rval;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static DataInputBlock getDataInputBlock(DocumentBlock[] blocks, int offset)
/*  89:    */   {
/*  90:164 */     if ((blocks == null) || (blocks.length == 0)) {
/*  91:165 */       return null;
/*  92:    */     }
/*  93:169 */     POIFSBigBlockSize bigBlockSize = blocks[0].bigBlockSize;
/*  94:170 */     int BLOCK_SHIFT = bigBlockSize.getHeaderValue();
/*  95:171 */     int BLOCK_SIZE = bigBlockSize.getBigBlockSize();
/*  96:172 */     int BLOCK_MASK = BLOCK_SIZE - 1;
/*  97:    */     
/*  98:    */ 
/*  99:175 */     int firstBlockIndex = offset >> BLOCK_SHIFT;
/* 100:176 */     int firstBlockOffset = offset & BLOCK_MASK;
/* 101:177 */     return new DataInputBlock(blocks[firstBlockIndex]._data, firstBlockOffset);
/* 102:    */   }
/* 103:    */   
/* 104:    */   void writeData(OutputStream stream)
/* 105:    */     throws IOException
/* 106:    */   {
/* 107:195 */     doWriteData(stream, this._data);
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.DocumentBlock
 * JD-Core Version:    0.7.0.1
 */