/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class DrawingRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 236;
/* 10:27 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/* 11:   */   private byte[] recordData;
/* 12:   */   private byte[] contd;
/* 13:   */   
/* 14:   */   public DrawingRecord()
/* 15:   */   {
/* 16:33 */     this.recordData = EMPTY_BYTE_ARRAY;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public DrawingRecord(RecordInputStream in)
/* 20:   */   {
/* 21:37 */     this.recordData = in.readRemainder();
/* 22:   */   }
/* 23:   */   
/* 24:   */   @Deprecated
/* 25:   */   public void processContinueRecord(byte[] record)
/* 26:   */   {
/* 27:46 */     this.contd = record;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void serialize(LittleEndianOutput out)
/* 31:   */   {
/* 32:50 */     out.write(this.recordData);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected int getDataSize()
/* 36:   */   {
/* 37:54 */     return this.recordData.length;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public short getSid()
/* 41:   */   {
/* 42:58 */     return 236;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public byte[] getRecordData()
/* 46:   */   {
/* 47:62 */     return this.recordData;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void setData(byte[] thedata)
/* 51:   */   {
/* 52:66 */     if (thedata == null) {
/* 53:67 */       throw new IllegalArgumentException("data must not be null");
/* 54:   */     }
/* 55:69 */     this.recordData = thedata;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public DrawingRecord clone()
/* 59:   */   {
/* 60:78 */     DrawingRecord rec = new DrawingRecord();
/* 61:79 */     rec.recordData = ((byte[])this.recordData.clone());
/* 62:80 */     if (this.contd != null) {
/* 63:82 */       rec.contd = ((byte[])this.contd.clone());
/* 64:   */     }
/* 65:85 */     return rec;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public String toString()
/* 69:   */   {
/* 70:90 */     return "DrawingRecord[" + this.recordData.length + "]";
/* 71:   */   }
/* 72:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DrawingRecord
 * JD-Core Version:    0.7.0.1
 */