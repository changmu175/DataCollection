/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.BitField;
/*  4:   */ import org.apache.poi.util.BitFieldFactory;
/*  5:   */ import org.apache.poi.util.HexDump;
/*  6:   */ import org.apache.poi.util.LittleEndianOutput;
/*  7:   */ 
/*  8:   */ public final class ProtectionRev4Record
/*  9:   */   extends StandardRecord
/* 10:   */ {
/* 11:   */   public static final short sid = 431;
/* 12:33 */   private static final BitField protectedFlag = BitFieldFactory.getInstance(1);
/* 13:   */   private int _options;
/* 14:   */   
/* 15:   */   private ProtectionRev4Record(int options)
/* 16:   */   {
/* 17:38 */     this._options = options;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ProtectionRev4Record(boolean protect)
/* 21:   */   {
/* 22:42 */     this(0);
/* 23:43 */     setProtect(protect);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ProtectionRev4Record(RecordInputStream in)
/* 27:   */   {
/* 28:47 */     this(in.readUShort());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setProtect(boolean protect)
/* 32:   */   {
/* 33:55 */     this._options = protectedFlag.setBoolean(this._options, protect);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean getProtect()
/* 37:   */   {
/* 38:63 */     return protectedFlag.isSet(this._options);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String toString()
/* 42:   */   {
/* 43:67 */     StringBuffer buffer = new StringBuffer();
/* 44:   */     
/* 45:69 */     buffer.append("[PROT4REV]\n");
/* 46:70 */     buffer.append("    .options = ").append(HexDump.shortToHex(this._options)).append("\n");
/* 47:71 */     buffer.append("[/PROT4REV]\n");
/* 48:72 */     return buffer.toString();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void serialize(LittleEndianOutput out)
/* 52:   */   {
/* 53:76 */     out.writeShort(this._options);
/* 54:   */   }
/* 55:   */   
/* 56:   */   protected int getDataSize()
/* 57:   */   {
/* 58:80 */     return 2;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public short getSid()
/* 62:   */   {
/* 63:84 */     return 431;
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.ProtectionRev4Record
 * JD-Core Version:    0.7.0.1
 */