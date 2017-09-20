/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ChartEndObjectRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 2133;
/* 12:   */   private short rt;
/* 13:   */   private short grbitFrt;
/* 14:   */   private short iObjectKind;
/* 15:   */   private byte[] reserved;
/* 16:   */   
/* 17:   */   public ChartEndObjectRecord(RecordInputStream in)
/* 18:   */   {
/* 19:37 */     this.rt = in.readShort();
/* 20:38 */     this.grbitFrt = in.readShort();
/* 21:39 */     this.iObjectKind = in.readShort();
/* 22:   */     
/* 23:   */ 
/* 24:   */ 
/* 25:   */ 
/* 26:44 */     this.reserved = new byte[6];
/* 27:45 */     if (in.available() != 0) {
/* 28:49 */       in.readFully(this.reserved);
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected int getDataSize()
/* 33:   */   {
/* 34:55 */     return 12;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public short getSid()
/* 38:   */   {
/* 39:60 */     return 2133;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void serialize(LittleEndianOutput out)
/* 43:   */   {
/* 44:65 */     out.writeShort(this.rt);
/* 45:66 */     out.writeShort(this.grbitFrt);
/* 46:67 */     out.writeShort(this.iObjectKind);
/* 47:   */     
/* 48:69 */     out.write(this.reserved);
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String toString()
/* 52:   */   {
/* 53:74 */     StringBuffer buffer = new StringBuffer();
/* 54:   */     
/* 55:76 */     buffer.append("[ENDOBJECT]\n");
/* 56:77 */     buffer.append("    .rt         =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 57:78 */     buffer.append("    .grbitFrt   =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 58:79 */     buffer.append("    .iObjectKind=").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
/* 59:80 */     buffer.append("    .reserved   =").append(HexDump.toHex(this.reserved)).append('\n');
/* 60:81 */     buffer.append("[/ENDOBJECT]\n");
/* 61:82 */     return buffer.toString();
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartEndObjectRecord
 * JD-Core Version:    0.7.0.1
 */