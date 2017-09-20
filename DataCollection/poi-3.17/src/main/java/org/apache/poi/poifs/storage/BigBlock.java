/*  1:   */ package org.apache.poi.poifs.storage;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*  6:   */ 
/*  7:   */ abstract class BigBlock
/*  8:   */   implements BlockWritable
/*  9:   */ {
/* 10:   */   protected POIFSBigBlockSize bigBlockSize;
/* 11:   */   
/* 12:   */   protected BigBlock(POIFSBigBlockSize bigBlockSize)
/* 13:   */   {
/* 14:49 */     this.bigBlockSize = bigBlockSize;
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected void doWriteData(OutputStream stream, byte[] data)
/* 18:   */     throws IOException
/* 19:   */   {
/* 20:67 */     stream.write(data);
/* 21:   */   }
/* 22:   */   
/* 23:   */   abstract void writeData(OutputStream paramOutputStream)
/* 24:   */     throws IOException;
/* 25:   */   
/* 26:   */   public void writeBlocks(OutputStream stream)
/* 27:   */     throws IOException
/* 28:   */   {
/* 29:98 */     writeData(stream);
/* 30:   */   }
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BigBlock
 * JD-Core Version:    0.7.0.1
 */