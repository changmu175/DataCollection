/*  1:   */ package org.apache.poi.hssf.record.chart;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class DataLabelExtensionRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 2154;
/* 12:   */   private int rt;
/* 13:   */   private int grbitFrt;
/* 14:33 */   private byte[] unused = new byte[8];
/* 15:   */   
/* 16:   */   public DataLabelExtensionRecord(RecordInputStream in)
/* 17:   */   {
/* 18:36 */     this.rt = in.readShort();
/* 19:37 */     this.grbitFrt = in.readShort();
/* 20:38 */     in.readFully(this.unused);
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected int getDataSize()
/* 24:   */   {
/* 25:43 */     return 12;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public short getSid()
/* 29:   */   {
/* 30:48 */     return 2154;
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected void serialize(LittleEndianOutput out)
/* 34:   */   {
/* 35:53 */     out.writeShort(this.rt);
/* 36:54 */     out.writeShort(this.grbitFrt);
/* 37:55 */     out.write(this.unused);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String toString()
/* 41:   */   {
/* 42:60 */     StringBuffer buffer = new StringBuffer();
/* 43:   */     
/* 44:62 */     buffer.append("[DATALABEXT]\n");
/* 45:63 */     buffer.append("    .rt      =").append(HexDump.shortToHex(this.rt)).append('\n');
/* 46:64 */     buffer.append("    .grbitFrt=").append(HexDump.shortToHex(this.grbitFrt)).append('\n');
/* 47:65 */     buffer.append("    .unused  =").append(HexDump.toHex(this.unused)).append('\n');
/* 48:   */     
/* 49:67 */     buffer.append("[/DATALABEXT]\n");
/* 50:68 */     return buffer.toString();
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.chart.DataLabelExtensionRecord
 * JD-Core Version:    0.7.0.1
 */