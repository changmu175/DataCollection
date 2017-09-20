/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ChartEndBlockRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 2131;
/* 13:   */   private short rt;
/* 14:   */   private short grbitFrt;
/* 15:   */   private short iObjectKind;
/* 16:   */   private byte[] unused;
/* 17:   */   
/* 18:   */   public ChartEndBlockRecord() {}
/* 19:   */   
/* 20:   */   public ChartEndBlockRecord(RecordInputStream in)
/* 21:   */   {
/* 22:42 */     this.rt = in.readShort();
/* 23:43 */     this.grbitFrt = in.readShort();
/* 24:44 */     this.iObjectKind = in.readShort();
/* 25:47 */     if (in.available() == 0)
/* 26:   */     {
/* 27:48 */       this.unused = new byte[0];
/* 28:   */     }
/* 29:   */     else
/* 30:   */     {
/* 31:50 */       this.unused = new byte[6];
/* 32:51 */       in.readFully(this.unused);
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected int getDataSize()
/* 37:   */   {
/* 38:57 */     return 6 + this.unused.length;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public short getSid()
/* 42:   */   {
/* 43:62 */     return 2131;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void serialize(LittleEndianOutput out)
/* 47:   */   {
/* 48:67 */     out.writeShort(this.rt);
/* 49:68 */     out.writeShort(this.grbitFrt);
/* 50:69 */     out.writeShort(this.iObjectKind);
/* 51:   */     
/* 52:71 */     out.write(this.unused);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String toString()
/* 56:   */   {
/* 57:76 */     StringBuffer buffer = new StringBuffer();
/* 58:   */     
/* 59:78 */     buffer.append("[ENDBLOCK]\n");
/* 60:79 */     buffer.append("    .rt         =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 61:80 */     buffer.append("    .grbitFrt   =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 62:81 */     buffer.append("    .iObjectKind=").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
/* 63:82 */     buffer.append("    .unused     =").append(HexDump.toHex(this.unused)).append('\n');
/* 64:83 */     buffer.append("[/ENDBLOCK]\n");
/* 65:84 */     return buffer.toString();
/* 66:   */   }
/* 67:   */   
/* 68:   */   public ChartEndBlockRecord clone()
/* 69:   */   {
/* 70:89 */     ChartEndBlockRecord record = new ChartEndBlockRecord();
/* 71:   */     
/* 72:91 */     record.rt = this.rt;
/* 73:92 */     record.grbitFrt = this.grbitFrt;
/* 74:93 */     record.iObjectKind = this.iObjectKind;
/* 75:94 */     record.unused = ((byte[])this.unused.clone());
/* 76:   */     
/* 77:96 */     return record;
/* 78:   */   }
/* 79:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartEndBlockRecord
 * JD-Core Version:    0.7.0.1
 */