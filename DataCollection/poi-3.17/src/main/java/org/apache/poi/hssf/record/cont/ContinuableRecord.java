/*  1:   */ package org.apache.poi.hssf.record.cont;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.Record;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  5:   */ import org.apache.poi.util.LittleEndianOutput;
/*  6:   */ 
/*  7:   */ public abstract class ContinuableRecord
/*  8:   */   extends Record
/*  9:   */ {
/* 10:   */   protected abstract void serialize(ContinuableRecordOutput paramContinuableRecordOutput);
/* 11:   */   
/* 12:   */   public final int getRecordSize()
/* 13:   */   {
/* 14:53 */     ContinuableRecordOutput out = ContinuableRecordOutput.createForCountingOnly();
/* 15:54 */     serialize(out);
/* 16:55 */     out.terminate();
/* 17:56 */     return out.getTotalSize();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public final int serialize(int offset, byte[] data)
/* 21:   */   {
/* 22:61 */     LittleEndianOutput leo = new LittleEndianByteArrayOutputStream(data, offset);
/* 23:62 */     ContinuableRecordOutput out = new ContinuableRecordOutput(leo, getSid());
/* 24:63 */     serialize(out);
/* 25:64 */     out.terminate();
/* 26:65 */     return out.getTotalSize();
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cont.ContinuableRecord
 * JD-Core Version:    0.7.0.1
 */