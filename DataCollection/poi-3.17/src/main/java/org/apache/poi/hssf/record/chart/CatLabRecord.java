/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class CatLabRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 2134;
/* 12:   */   private short rt;
/* 13:   */   private short grbitFrt;
/* 14:   */   private short wOffset;
/* 15:   */   private short at;
/* 16:   */   private short grbit;
/* 17:   */   private Short unused;
/* 18:   */   
/* 19:   */   public CatLabRecord(RecordInputStream in)
/* 20:   */   {
/* 21:41 */     this.rt = in.readShort();
/* 22:42 */     this.grbitFrt = in.readShort();
/* 23:43 */     this.wOffset = in.readShort();
/* 24:44 */     this.at = in.readShort();
/* 25:45 */     this.grbit = in.readShort();
/* 26:48 */     if (in.available() == 0) {
/* 27:49 */       this.unused = null;
/* 28:   */     } else {
/* 29:51 */       this.unused = Short.valueOf(in.readShort());
/* 30:   */     }
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected int getDataSize()
/* 34:   */   {
/* 35:57 */     return 10 + (this.unused == null ? 0 : 2);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public short getSid()
/* 39:   */   {
/* 40:62 */     return 2134;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void serialize(LittleEndianOutput out)
/* 44:   */   {
/* 45:67 */     out.writeShort(this.rt);
/* 46:68 */     out.writeShort(this.grbitFrt);
/* 47:69 */     out.writeShort(this.wOffset);
/* 48:70 */     out.writeShort(this.at);
/* 49:71 */     out.writeShort(this.grbit);
/* 50:72 */     if (this.unused != null) {
/* 51:73 */       out.writeShort(this.unused.shortValue());
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String toString()
/* 56:   */   {
/* 57:78 */     StringBuffer buffer = new StringBuffer();
/* 58:   */     
/* 59:80 */     buffer.append("[CATLAB]\n");
/* 60:81 */     buffer.append("    .rt      =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 61:82 */     buffer.append("    .grbitFrt=").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 62:83 */     buffer.append("    .wOffset =").append(HexDump.shortToHex(this.wOffset)).append('\n');
/* 63:84 */     buffer.append("    .at      =").append(HexDump.shortToHex(this.at)).append('\n');
/* 64:85 */     buffer.append("    .grbit   =").append(HexDump.shortToHex(this.grbit)).append('\n');
/* 65:86 */     if (this.unused != null) {
/* 66:87 */       buffer.append("    .unused  =").append(HexDump.shortToHex(this.unused.shortValue())).append('\n');
/* 67:   */     }
/* 68:89 */     buffer.append("[/CATLAB]\n");
/* 69:90 */     return buffer.toString();
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.CatLabRecord
 * JD-Core Version:    0.7.0.1
 */