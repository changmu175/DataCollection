/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class WindowProtectRecord
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 25;
/* 12:33 */   private static final BitField settingsProtectedFlag = BitFieldFactory.getInstance(1);
/* 13:   */   private int _options;
/* 14:   */   
/* 15:   */   public WindowProtectRecord(int options)
/* 16:   */   {
/* 17:38 */     this._options = options;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public WindowProtectRecord(RecordInputStream in)
/* 21:   */   {
/* 22:42 */     this(in.readUShort());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public WindowProtectRecord(boolean protect)
/* 26:   */   {
/* 27:46 */     this(0);
/* 28:47 */     setProtect(protect);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setProtect(boolean protect)
/* 32:   */   {
/* 33:55 */     this._options = settingsProtectedFlag.setBoolean(this._options, protect);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean getProtect()
/* 37:   */   {
/* 38:64 */     return settingsProtectedFlag.isSet(this._options);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String toString()
/* 42:   */   {
/* 43:68 */     StringBuffer buffer = new StringBuffer();
/* 44:   */     
/* 45:70 */     buffer.append("[WINDOWPROTECT]\n");
/* 46:71 */     buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 47:72 */     buffer.append("[/WINDOWPROTECT]\n");
/* 48:73 */     return buffer.toString();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void serialize(LittleEndianOutput out)
/* 52:   */   {
/* 53:77 */     out.writeShort(this._options);
/* 54:   */   }
/* 55:   */   
/* 56:   */   protected int getDataSize()
/* 57:   */   {
/* 58:81 */     return 2;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public short getSid()
/* 62:   */   {
/* 63:86 */     return 25;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public Object clone()
/* 67:   */   {
/* 68:90 */     return new WindowProtectRecord(this._options);
/* 69:   */   }
/* 70:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WindowProtectRecord
 * JD-Core Version:    0.7.0.1
 */