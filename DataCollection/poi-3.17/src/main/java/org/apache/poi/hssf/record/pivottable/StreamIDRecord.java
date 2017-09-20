/*  1:   */ package org.apache.poi.hssf.record.pivottable;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.RecordInputStream;
/*  4:   */ import org.apache.poi.hssf.record.StandardRecord;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class StreamIDRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 213;
/* 12:   */   private int idstm;
/* 13:   */   
/* 14:   */   public StreamIDRecord(RecordInputStream in)
/* 15:   */   {
/* 16:36 */     this.idstm = in.readShort();
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected void serialize(LittleEndianOutput out)
/* 20:   */   {
/* 21:41 */     out.writeShort(this.idstm);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected int getDataSize()
/* 25:   */   {
/* 26:46 */     return 2;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public short getSid()
/* 30:   */   {
/* 31:51 */     return 213;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String toString()
/* 35:   */   {
/* 36:56 */     StringBuffer buffer = new StringBuffer();
/* 37:   */     
/* 38:58 */     buffer.append("[SXIDSTM]\n");
/* 39:59 */     buffer.append("    .idstm      =").append(HexDump.shortToHex(this.idstm)).append('\n');
/* 40:   */     
/* 41:61 */     buffer.append("[/SXIDSTM]\n");
/* 42:62 */     return buffer.toString();
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.StreamIDRecord
 * JD-Core Version:    0.7.0.1
 */