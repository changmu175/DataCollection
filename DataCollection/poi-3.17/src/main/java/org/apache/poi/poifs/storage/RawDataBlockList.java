/*  1:   */ package org.apache.poi.poifs.storage;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.List;
/*  7:   */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  8:   */ 
/*  9:   */ public class RawDataBlockList
/* 10:   */   extends BlockListImpl
/* 11:   */ {
/* 12:   */   public RawDataBlockList(InputStream stream, POIFSBigBlockSize bigBlockSize)
/* 13:   */     throws IOException
/* 14:   */   {
/* 15:51 */     List<RawDataBlock> blocks = new ArrayList();
/* 16:   */     for (;;)
/* 17:   */     {
/* 18:55 */       RawDataBlock block = new RawDataBlock(stream, bigBlockSize.getBigBlockSize());
/* 19:58 */       if (block.hasData()) {
/* 20:59 */         blocks.add(block);
/* 21:   */       }
/* 22:63 */       if (block.eof()) {
/* 23:   */         break;
/* 24:   */       }
/* 25:   */     }
/* 26:67 */     setBlocks((ListManagedBlock[])blocks.toArray(new RawDataBlock[blocks.size()]));
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.RawDataBlockList
 * JD-Core Version:    0.7.0.1
 */