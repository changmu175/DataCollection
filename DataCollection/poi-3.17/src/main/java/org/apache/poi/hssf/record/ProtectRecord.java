/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ProtectRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 18;
/* 12:35 */   private static final BitField protectFlag = BitFieldFactory.getInstance(1);
/* 13:   */   private int _options;
/* 14:   */   
/* 15:   */   private ProtectRecord(int options)
/* 16:   */   {
/* 17:40 */     this._options = options;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ProtectRecord(boolean isProtected)
/* 21:   */   {
/* 22:44 */     this(0);
/* 23:45 */     setProtect(isProtected);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ProtectRecord(RecordInputStream in)
/* 27:   */   {
/* 28:49 */     this(in.readShort());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setProtect(boolean protect)
/* 32:   */   {
/* 33:57 */     this._options = protectFlag.setBoolean(this._options, protect);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean getProtect()
/* 37:   */   {
/* 38:65 */     return protectFlag.isSet(this._options);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String toString()
/* 42:   */   {
/* 43:69 */     StringBuffer buffer = new StringBuffer();
/* 44:   */     
/* 45:71 */     buffer.append("[PROTECT]\n");
/* 46:72 */     buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 47:73 */     buffer.append("[/PROTECT]\n");
/* 48:74 */     return buffer.toString();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void serialize(LittleEndianOutput out)
/* 52:   */   {
/* 53:78 */     out.writeShort(this._options);
/* 54:   */   }
/* 55:   */   
/* 56:   */   protected int getDataSize()
/* 57:   */   {
/* 58:82 */     return 2;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public short getSid()
/* 62:   */   {
/* 63:86 */     return 18;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public Object clone()
/* 67:   */   {
/* 68:90 */     return new ProtectRecord(this._options);
/* 69:   */   }
/* 70:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ProtectRecord
 * JD-Core Version:    0.7.0.1
 */