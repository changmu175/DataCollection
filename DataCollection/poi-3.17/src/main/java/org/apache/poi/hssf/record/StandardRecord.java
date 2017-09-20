/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public abstract class StandardRecord
/*  7:   */   extends Record
/*  8:   */ {
/*  9:   */   protected abstract int getDataSize();
/* 10:   */   
/* 11:   */   public final int getRecordSize()
/* 12:   */   {
/* 13:30 */     return 4 + getDataSize();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final int serialize(int offset, byte[] data)
/* 17:   */   {
/* 18:41 */     int dataSize = getDataSize();
/* 19:42 */     int recSize = 4 + dataSize;
/* 20:43 */     LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(data, offset, recSize);
/* 21:44 */     out.writeShort(getSid());
/* 22:45 */     out.writeShort(dataSize);
/* 23:46 */     serialize(out);
/* 24:47 */     if (out.getWriteIndex() - offset != recSize) {
/* 25:48 */       throw new IllegalStateException("Error in serialization of (" + getClass().getName() + "): " + "Incorrect number of bytes written - expected " + recSize + " but got " + (out.getWriteIndex() - offset));
/* 26:   */     }
/* 27:52 */     return recSize;
/* 28:   */   }
/* 29:   */   
/* 30:   */   protected abstract void serialize(LittleEndianOutput paramLittleEndianOutput);
/* 31:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.StandardRecord
 * JD-Core Version:    0.7.0.1
 */