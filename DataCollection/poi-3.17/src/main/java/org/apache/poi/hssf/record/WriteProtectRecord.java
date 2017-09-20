/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class WriteProtectRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 134;
/*  9:   */   
/* 10:   */   public WriteProtectRecord() {}
/* 11:   */   
/* 12:   */   public WriteProtectRecord(RecordInputStream in)
/* 13:   */   {
/* 14:40 */     if (in.remaining() == 2) {
/* 15:41 */       in.readShort();
/* 16:   */     }
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:47 */     StringBuffer buffer = new StringBuffer();
/* 22:   */     
/* 23:49 */     buffer.append("[WRITEPROTECT]\n");
/* 24:50 */     buffer.append("[/WRITEPROTECT]\n");
/* 25:51 */     return buffer.toString();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void serialize(LittleEndianOutput out) {}
/* 29:   */   
/* 30:   */   protected int getDataSize()
/* 31:   */   {
/* 32:58 */     return 0;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public short getSid()
/* 36:   */   {
/* 37:63 */     return 134;
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WriteProtectRecord
 * JD-Core Version:    0.7.0.1
 */