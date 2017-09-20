/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class DateWindow1904Record
/*  6:   */   extends StandardRecord
/*  7:   */ {
/*  8:   */   public static final short sid = 34;
/*  9:   */   private short field_1_window;
/* 10:   */   
/* 11:   */   public DateWindow1904Record() {}
/* 12:   */   
/* 13:   */   public DateWindow1904Record(RecordInputStream in)
/* 14:   */   {
/* 15:45 */     this.field_1_window = in.readShort();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setWindowing(short window)
/* 19:   */   {
/* 20:55 */     this.field_1_window = window;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public short getWindowing()
/* 24:   */   {
/* 25:65 */     return this.field_1_window;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:70 */     StringBuffer buffer = new StringBuffer();
/* 31:   */     
/* 32:72 */     buffer.append("[1904]\n");
/* 33:73 */     buffer.append("    .is1904          = ").append(Integer.toHexString(getWindowing())).append("\n");
/* 34:   */     
/* 35:75 */     buffer.append("[/1904]\n");
/* 36:76 */     return buffer.toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void serialize(LittleEndianOutput out)
/* 40:   */   {
/* 41:80 */     out.writeShort(getWindowing());
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected int getDataSize()
/* 45:   */   {
/* 46:84 */     return 2;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public short getSid()
/* 50:   */   {
/* 51:89 */     return 34;
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.DateWindow1904Record
 * JD-Core Version:    0.7.0.1
 */