/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ChartStartObjectRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 2132;
/* 12:   */   private short rt;
/* 13:   */   private short grbitFrt;
/* 14:   */   private short iObjectKind;
/* 15:   */   private short iObjectContext;
/* 16:   */   private short iObjectInstance1;
/* 17:   */   private short iObjectInstance2;
/* 18:   */   
/* 19:   */   public ChartStartObjectRecord(RecordInputStream in)
/* 20:   */   {
/* 21:39 */     this.rt = in.readShort();
/* 22:40 */     this.grbitFrt = in.readShort();
/* 23:41 */     this.iObjectKind = in.readShort();
/* 24:42 */     this.iObjectContext = in.readShort();
/* 25:43 */     this.iObjectInstance1 = in.readShort();
/* 26:44 */     this.iObjectInstance2 = in.readShort();
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected int getDataSize()
/* 30:   */   {
/* 31:49 */     return 12;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public short getSid()
/* 35:   */   {
/* 36:54 */     return 2132;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void serialize(LittleEndianOutput out)
/* 40:   */   {
/* 41:59 */     out.writeShort(this.rt);
/* 42:60 */     out.writeShort(this.grbitFrt);
/* 43:61 */     out.writeShort(this.iObjectKind);
/* 44:62 */     out.writeShort(this.iObjectContext);
/* 45:63 */     out.writeShort(this.iObjectInstance1);
/* 46:64 */     out.writeShort(this.iObjectInstance2);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String toString()
/* 50:   */   {
/* 51:69 */     StringBuffer buffer = new StringBuffer();
/* 52:   */     
/* 53:71 */     buffer.append("[STARTOBJECT]\n");
/* 54:72 */     buffer.append("    .rt              =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 55:73 */     buffer.append("    .grbitFrt        =").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 56:74 */     buffer.append("    .iObjectKind     =").append(HexDump.shortToHex(this.iObjectKind)).append('\n');
/* 57:75 */     buffer.append("    .iObjectContext  =").append(HexDump.shortToHex(this.iObjectContext)).append('\n');
/* 58:76 */     buffer.append("    .iObjectInstance1=").append(HexDump.shortToHex(this.iObjectInstance1)).append('\n');
/* 59:77 */     buffer.append("    .iObjectInstance2=").append(HexDump.shortToHex(this.iObjectInstance2)).append('\n');
/* 60:78 */     buffer.append("[/STARTOBJECT]\n");
/* 61:79 */     return buffer.toString();
/* 62:   */   }
/* 63:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.ChartStartObjectRecord
 * JD-Core Version:    0.7.0.1
 */