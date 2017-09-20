/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class InterfaceHdrRecord
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 225;
/* 10:   */   private final int _codepage;
/* 11:   */   public static final int CODEPAGE = 1200;
/* 12:   */   
/* 13:   */   public InterfaceHdrRecord(int codePage)
/* 14:   */   {
/* 15:39 */     this._codepage = codePage;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public InterfaceHdrRecord(RecordInputStream in)
/* 19:   */   {
/* 20:43 */     this._codepage = in.readShort();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String toString()
/* 24:   */   {
/* 25:47 */     StringBuffer buffer = new StringBuffer();
/* 26:   */     
/* 27:49 */     buffer.append("[INTERFACEHDR]\n");
/* 28:50 */     buffer.append("    .codepage = ").append(HexDump.shortToHex(this._codepage)).append("\n");
/* 29:51 */     buffer.append("[/INTERFACEHDR]\n");
/* 30:52 */     return buffer.toString();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void serialize(LittleEndianOutput out)
/* 34:   */   {
/* 35:56 */     out.writeShort(this._codepage);
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected int getDataSize()
/* 39:   */   {
/* 40:60 */     return 2;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public short getSid()
/* 44:   */   {
/* 45:64 */     return 225;
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.InterfaceHdrRecord
 * JD-Core Version:    0.7.0.1
 */