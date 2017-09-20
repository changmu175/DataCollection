/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class UncalcedRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 94;
/*  9:   */   private short _reserved;
/* 10:   */   
/* 11:   */   public UncalcedRecord()
/* 12:   */   {
/* 13:36 */     this._reserved = 0;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public short getSid()
/* 17:   */   {
/* 18:40 */     return 94;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public UncalcedRecord(RecordInputStream in)
/* 22:   */   {
/* 23:44 */     this._reserved = in.readShort();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String toString()
/* 27:   */   {
/* 28:48 */     StringBuffer buffer = new StringBuffer();
/* 29:49 */     buffer.append("[UNCALCED]\n");
/* 30:50 */     buffer.append("    _reserved: ").append(this._reserved).append('\n');
/* 31:51 */     buffer.append("[/UNCALCED]\n");
/* 32:52 */     return buffer.toString();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void serialize(LittleEndianOutput out)
/* 36:   */   {
/* 37:56 */     out.writeShort(this._reserved);
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected int getDataSize()
/* 41:   */   {
/* 42:60 */     return 2;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public static int getStaticRecordSize()
/* 46:   */   {
/* 47:64 */     return 6;
/* 48:   */   }
/* 49:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.UncalcedRecord
 * JD-Core Version:    0.7.0.1
 */