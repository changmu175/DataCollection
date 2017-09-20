/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ 
/*  5:   */ public final class DrawingRecordForBiffViewer
/*  6:   */   extends AbstractEscherHolderRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 236;
/*  9:   */   
/* 10:   */   public DrawingRecordForBiffViewer() {}
/* 11:   */   
/* 12:   */   public DrawingRecordForBiffViewer(RecordInputStream in)
/* 13:   */   {
/* 14:35 */     super(in);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DrawingRecordForBiffViewer(DrawingRecord r)
/* 18:   */   {
/* 19:40 */     super(convertToInputStream(r));
/* 20:41 */     convertRawBytesToEscherRecords();
/* 21:   */   }
/* 22:   */   
/* 23:   */   private static RecordInputStream convertToInputStream(DrawingRecord r)
/* 24:   */   {
/* 25:45 */     byte[] data = r.serialize();
/* 26:46 */     RecordInputStream rinp = new RecordInputStream(new ByteArrayInputStream(data));
/* 27:   */     
/* 28:   */ 
/* 29:49 */     rinp.nextRecord();
/* 30:50 */     return rinp;
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected String getRecordName()
/* 34:   */   {
/* 35:55 */     return "MSODRAWING";
/* 36:   */   }
/* 37:   */   
/* 38:   */   public short getSid()
/* 39:   */   {
/* 40:60 */     return 236;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DrawingRecordForBiffViewer
 * JD-Core Version:    0.7.0.1
 */