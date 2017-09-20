/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class IterationRecord
/*  9:   */   extends StandardRecord
/* 10:   */   implements Cloneable
/* 11:   */ {
/* 12:   */   public static final short sid = 17;
/* 13:36 */   private static final BitField iterationOn = BitFieldFactory.getInstance(1);
/* 14:   */   private int _flags;
/* 15:   */   
/* 16:   */   public IterationRecord(boolean iterateOn)
/* 17:   */   {
/* 18:41 */     this._flags = iterationOn.setBoolean(0, iterateOn);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public IterationRecord(RecordInputStream in)
/* 22:   */   {
/* 23:46 */     this._flags = in.readShort();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setIteration(boolean iterate)
/* 27:   */   {
/* 28:54 */     this._flags = iterationOn.setBoolean(this._flags, iterate);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean getIteration()
/* 32:   */   {
/* 33:63 */     return iterationOn.isSet(this._flags);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String toString()
/* 37:   */   {
/* 38:67 */     StringBuffer buffer = new StringBuffer();
/* 39:   */     
/* 40:69 */     buffer.append("[ITERATION]\n");
/* 41:70 */     buffer.append("    .flags      = ").append(HexDump.shortToHex(this._flags)).append("\n");
/* 42:71 */     buffer.append("[/ITERATION]\n");
/* 43:72 */     return buffer.toString();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void serialize(LittleEndianOutput out)
/* 47:   */   {
/* 48:76 */     out.writeShort(this._flags);
/* 49:   */   }
/* 50:   */   
/* 51:   */   protected int getDataSize()
/* 52:   */   {
/* 53:80 */     return 2;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public short getSid()
/* 57:   */   {
/* 58:84 */     return 17;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public IterationRecord clone()
/* 62:   */   {
/* 63:89 */     return new IterationRecord(getIteration());
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.IterationRecord
 * JD-Core Version:    0.7.0.1
 */