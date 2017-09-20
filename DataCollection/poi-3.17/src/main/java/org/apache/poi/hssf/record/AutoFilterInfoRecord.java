/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.LittleEndianOutput;
/*  4:   */ 
/*  5:   */ public final class AutoFilterInfoRecord
/*  6:   */   extends StandardRecord
/*  7:   */   implements Cloneable
/*  8:   */ {
/*  9:   */   public static final short sid = 157;
/* 10:   */   private short _cEntries;
/* 11:   */   
/* 12:   */   public AutoFilterInfoRecord() {}
/* 13:   */   
/* 14:   */   public AutoFilterInfoRecord(RecordInputStream in)
/* 15:   */   {
/* 16:44 */     this._cEntries = in.readShort();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setNumEntries(short num)
/* 20:   */   {
/* 21:55 */     this._cEntries = num;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public short getNumEntries()
/* 25:   */   {
/* 26:66 */     return this._cEntries;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:71 */     StringBuffer buffer = new StringBuffer();
/* 32:   */     
/* 33:73 */     buffer.append("[AUTOFILTERINFO]\n");
/* 34:74 */     buffer.append("    .numEntries          = ").append(this._cEntries).append("\n");
/* 35:   */     
/* 36:76 */     buffer.append("[/AUTOFILTERINFO]\n");
/* 37:77 */     return buffer.toString();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void serialize(LittleEndianOutput out)
/* 41:   */   {
/* 42:81 */     out.writeShort(this._cEntries);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected int getDataSize()
/* 46:   */   {
/* 47:85 */     return 2;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public short getSid()
/* 51:   */   {
/* 52:90 */     return 157;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public AutoFilterInfoRecord clone()
/* 56:   */   {
/* 57:96 */     return (AutoFilterInfoRecord)cloneViaReserialise();
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.AutoFilterInfoRecord
 * JD-Core Version:    0.7.0.1
 */