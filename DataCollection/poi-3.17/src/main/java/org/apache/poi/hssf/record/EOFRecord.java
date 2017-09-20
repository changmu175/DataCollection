/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class EOFRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 10;
/* 10:   */   public static final int ENCODED_SIZE = 4;
/* 11:36 */   public static final EOFRecord instance = new EOFRecord();
/* 12:   */   
/* 13:   */   private EOFRecord() {}
/* 14:   */   
/* 15:   */   public EOFRecord(RecordInputStream in) {}
/* 16:   */   
/* 17:   */   public String toString()
/* 18:   */   {
/* 19:51 */     StringBuffer buffer = new StringBuffer();
/* 20:   */     
/* 21:53 */     buffer.append("[EOF]\n");
/* 22:54 */     buffer.append("[/EOF]\n");
/* 23:55 */     return buffer.toString();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void serialize(LittleEndianOutput out) {}
/* 27:   */   
/* 28:   */   protected int getDataSize()
/* 29:   */   {
/* 30:62 */     return 0;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public short getSid()
/* 34:   */   {
/* 35:67 */     return 10;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public EOFRecord clone()
/* 39:   */   {
/* 40:72 */     return instance;
/* 41:   */   }
/* 42:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.EOFRecord
 * JD-Core Version:    0.7.0.1
 */