/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class FnGroupCountRecord
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 156;
/*  9:   */   public static final short COUNT = 14;
/* 10:   */   private short field_1_count;
/* 11:   */   
/* 12:   */   public FnGroupCountRecord() {}
/* 13:   */   
/* 14:   */   public FnGroupCountRecord(RecordInputStream in)
/* 15:   */   {
/* 16:51 */     this.field_1_count = in.readShort();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setCount(short count)
/* 20:   */   {
/* 21:62 */     this.field_1_count = count;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public short getCount()
/* 25:   */   {
/* 26:73 */     return this.field_1_count;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:78 */     StringBuffer buffer = new StringBuffer();
/* 32:   */     
/* 33:80 */     buffer.append("[FNGROUPCOUNT]\n");
/* 34:81 */     buffer.append("    .count            = ").append(getCount()).append("\n");
/* 35:   */     
/* 36:83 */     buffer.append("[/FNGROUPCOUNT]\n");
/* 37:84 */     return buffer.toString();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void serialize(LittleEndianOutput out)
/* 41:   */   {
/* 42:88 */     out.writeShort(getCount());
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:92 */     return 2;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:97 */     return 156;
/* 53:   */   }
/* 54:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FnGroupCountRecord
 * JD-Core Version:    0.7.0.1
 */