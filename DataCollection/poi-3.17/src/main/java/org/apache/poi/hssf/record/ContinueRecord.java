/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class ContinueRecord
/*  7:   */   extends StandardRecord
/*  8:   */   implements Cloneable
/*  9:   */ {
/* 10:   */   public static final short sid = 60;
/* 11:   */   private byte[] _data;
/* 12:   */   
/* 13:   */   public ContinueRecord(byte[] data)
/* 14:   */   {
/* 15:36 */     this._data = data;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected int getDataSize()
/* 19:   */   {
/* 20:40 */     return this._data.length;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void serialize(LittleEndianOutput out)
/* 24:   */   {
/* 25:44 */     out.write(this._data);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public byte[] getData()
/* 29:   */   {
/* 30:52 */     return this._data;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String toString()
/* 34:   */   {
/* 35:56 */     StringBuffer buffer = new StringBuffer();
/* 36:   */     
/* 37:58 */     buffer.append("[CONTINUE RECORD]\n");
/* 38:59 */     buffer.append("    .data = ").append(HexDump.toHex(this._data)).append("\n");
/* 39:60 */     buffer.append("[/CONTINUE RECORD]\n");
/* 40:61 */     return buffer.toString();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public short getSid()
/* 44:   */   {
/* 45:65 */     return 60;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public ContinueRecord(RecordInputStream in)
/* 49:   */   {
/* 50:69 */     this._data = in.readRemainder();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public ContinueRecord clone()
/* 54:   */   {
/* 55:74 */     return new ContinueRecord(this._data);
/* 56:   */   }
/* 57:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ContinueRecord
 * JD-Core Version:    0.7.0.1
 */