/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ChartStartBlockRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 2130;
/* 13:   */   private short rt;
/* 14:   */   private short grbitFrt;
/* 15:   */   private short iObjectKind;
/* 16:   */   private short iObjectContext;
/* 17:   */   private short iObjectInstance1;
/* 18:   */   private short iObjectInstance2;
/* 19:   */   
/* 20:   */   public ChartStartBlockRecord() {}
/* 21:   */   
/* 22:   */   public ChartStartBlockRecord(RecordInputStream in)
/* 23:   */   {
/* 24:42 */     this.rt = in.readShort();
/* 25:43 */     this.grbitFrt = in.readShort();
/* 26:44 */     this.iObjectKind = in.readShort();
/* 27:45 */     this.iObjectContext = in.readShort();
/* 28:46 */     this.iObjectInstance1 = in.readShort();
/* 29:47 */     this.iObjectInstance2 = in.readShort();
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected int getDataSize()
/* 33:   */   {
/* 34:52 */     return 12;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public short getSid()
/* 38:   */   {
/* 39:57 */     return 2130;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void serialize(LittleEndianOutput out)
/* 43:   */   {
/* 44:62 */     out.writeShort(this.rt);
/* 45:63 */     out.writeShort(this.grbitFrt);
/* 46:64 */     out.writeShort(this.iObjectKind);
/* 47:65 */     out.writeShort(this.iObjectContext);
/* 48:66 */     out.writeShort(this.iObjectInstance1);
/* 49:67 */     out.writeShort(this.iObjectInstance2);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public String toString()
/* 53:   */   {
/* 54:72 */     StringBuffer buffer = new StringBuffer();
/* 55:   */     
/* 56:74 */     buffer.append("[STARTBLOCK]\n");
/* 57:75 */     buffer.append("    .rt              =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 58:76 */     buffer.append("    .grbitFrt        =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 59:77 */     buffer.append("    .iObjectKind     =").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
/* 60:78 */     buffer.append("    .iObjectContext  =").append(HexDump.shortToHex(this.iObjectContext)).append('\n');
/* 61:79 */     buffer.append("    .iObjectInstance1=").append(HexDump.shortToHex(this.iObjectInstance1)).append('\n');
/* 62:80 */     buffer.append("    .iObjectInstance2=").append(HexDump.shortToHex(this.iObjectInstance2)).append('\n');
/* 63:81 */     buffer.append("[/STARTBLOCK]\n");
/* 64:82 */     return buffer.toString();
/* 65:   */   }
/* 66:   */   
/* 67:   */   public ChartStartBlockRecord clone()
/* 68:   */   {
/* 69:87 */     ChartStartBlockRecord record = new ChartStartBlockRecord();
/* 70:   */     
/* 71:89 */     record.rt = this.rt;
/* 72:90 */     record.grbitFrt = this.grbitFrt;
/* 73:91 */     record.iObjectKind = this.iObjectKind;
/* 74:92 */     record.iObjectContext = this.iObjectContext;
/* 75:93 */     record.iObjectInstance1 = this.iObjectInstance1;
/* 76:94 */     record.iObjectInstance2 = this.iObjectInstance2;
/* 77:   */     
/* 78:96 */     return record;
/* 79:   */   }
/* 80:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartStartBlockRecord
 * JD-Core Version:    0.7.0.1
 */