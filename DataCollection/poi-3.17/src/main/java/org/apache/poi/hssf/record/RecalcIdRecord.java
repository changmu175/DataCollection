/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.HexDump;
/*  4:   */ import org.apache.poi.util.LittleEndianOutput;
/*  5:   */ 
/*  6:   */ public final class RecalcIdRecord
/*  7:   */   extends StandardRecord
/*  8:   */ {
/*  9:   */   public static final short sid = 449;
/* 10:   */   private final int _reserved0;
/* 11:   */   private int _engineId;
/* 12:   */   
/* 13:   */   public RecalcIdRecord()
/* 14:   */   {
/* 15:48 */     this._reserved0 = 0;
/* 16:49 */     this._engineId = 0;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public RecalcIdRecord(RecordInputStream in)
/* 20:   */   {
/* 21:53 */     in.readUShort();
/* 22:54 */     this._reserved0 = in.readUShort();
/* 23:55 */     this._engineId = in.readInt();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean isNeeded()
/* 27:   */   {
/* 28:59 */     return true;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setEngineId(int val)
/* 32:   */   {
/* 33:63 */     this._engineId = val;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public int getEngineId()
/* 37:   */   {
/* 38:67 */     return this._engineId;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String toString()
/* 42:   */   {
/* 43:71 */     StringBuffer buffer = new StringBuffer();
/* 44:   */     
/* 45:73 */     buffer.append("[RECALCID]\n");
/* 46:74 */     buffer.append("    .reserved = ").append(HexDump.shortToHex(this._reserved0)).append("\n");
/* 47:75 */     buffer.append("    .engineId = ").append(HexDump.intToHex(this._engineId)).append("\n");
/* 48:76 */     buffer.append("[/RECALCID]\n");
/* 49:77 */     return buffer.toString();
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void serialize(LittleEndianOutput out)
/* 53:   */   {
/* 54:81 */     out.writeShort(449);
/* 55:82 */     out.writeShort(this._reserved0);
/* 56:83 */     out.writeInt(this._engineId);
/* 57:   */   }
/* 58:   */   
/* 59:   */   protected int getDataSize()
/* 60:   */   {
/* 61:87 */     return 8;
/* 62:   */   }
/* 63:   */   
/* 64:   */   public short getSid()
/* 65:   */   {
/* 66:91 */     return 449;
/* 67:   */   }
/* 68:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.RecalcIdRecord
 * JD-Core Version:    0.7.0.1
 */