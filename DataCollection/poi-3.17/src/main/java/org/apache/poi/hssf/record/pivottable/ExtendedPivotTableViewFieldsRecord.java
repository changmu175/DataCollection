/*   1:    */ package org.apache.poi.hssf.record.pivottable;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.RecordFormatException;
/*   8:    */ import org.apache.poi.util.StringUtil;
/*   9:    */ 
/*  10:    */ public final class ExtendedPivotTableViewFieldsRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13:    */   public static final short sid = 256;
/*  14:    */   private static final int STRING_NOT_PRESENT_LEN = 65535;
/*  15:    */   private int _grbit1;
/*  16:    */   private int _grbit2;
/*  17:    */   private int _citmShow;
/*  18:    */   private int _isxdiSort;
/*  19:    */   private int _isxdiShow;
/*  20:    */   private int _reserved1;
/*  21:    */   private int _reserved2;
/*  22:    */   private String _subtotalName;
/*  23:    */   
/*  24:    */   public ExtendedPivotTableViewFieldsRecord(RecordInputStream in)
/*  25:    */   {
/*  26: 50 */     this._grbit1 = in.readInt();
/*  27: 51 */     this._grbit2 = in.readUByte();
/*  28: 52 */     this._citmShow = in.readUByte();
/*  29: 53 */     this._isxdiSort = in.readUShort();
/*  30: 54 */     this._isxdiShow = in.readUShort();
/*  31: 56 */     switch (in.remaining())
/*  32:    */     {
/*  33:    */     case 0: 
/*  34: 60 */       this._reserved1 = 0;
/*  35: 61 */       this._reserved2 = 0;
/*  36: 62 */       this._subtotalName = null; return;
/*  37:    */     case 10: 
/*  38:    */       break;
/*  39:    */     default: 
/*  40: 68 */       throw new RecordFormatException("Unexpected remaining size (" + in.remaining() + ")");
/*  41:    */     }
/*  42: 70 */     int cchSubName = in.readUShort();
/*  43: 71 */     this._reserved1 = in.readInt();
/*  44: 72 */     this._reserved2 = in.readInt();
/*  45: 73 */     if (cchSubName != 65535) {
/*  46: 74 */       this._subtotalName = in.readUnicodeLEString(cchSubName);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected void serialize(LittleEndianOutput out)
/*  51:    */   {
/*  52: 81 */     out.writeInt(this._grbit1);
/*  53: 82 */     out.writeByte(this._grbit2);
/*  54: 83 */     out.writeByte(this._citmShow);
/*  55: 84 */     out.writeShort(this._isxdiSort);
/*  56: 85 */     out.writeShort(this._isxdiShow);
/*  57: 87 */     if (this._subtotalName == null) {
/*  58: 88 */       out.writeShort(65535);
/*  59:    */     } else {
/*  60: 90 */       out.writeShort(this._subtotalName.length());
/*  61:    */     }
/*  62: 93 */     out.writeInt(this._reserved1);
/*  63: 94 */     out.writeInt(this._reserved2);
/*  64: 95 */     if (this._subtotalName != null) {
/*  65: 96 */       StringUtil.putUnicodeLE(this._subtotalName, out);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected int getDataSize()
/*  70:    */   {
/*  71:103 */     return 20 + (this._subtotalName == null ? 0 : 2 * this._subtotalName.length());
/*  72:    */   }
/*  73:    */   
/*  74:    */   public short getSid()
/*  75:    */   {
/*  76:109 */     return 256;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String toString()
/*  80:    */   {
/*  81:114 */     StringBuffer buffer = new StringBuffer();
/*  82:    */     
/*  83:116 */     buffer.append("[SXVDEX]\n");
/*  84:    */     
/*  85:118 */     buffer.append("    .grbit1 =").append(HexDump.intToHex(this._grbit1)).append("\n");
/*  86:119 */     buffer.append("    .grbit2 =").append(HexDump.byteToHex(this._grbit2)).append("\n");
/*  87:120 */     buffer.append("    .citmShow =").append(HexDump.byteToHex(this._citmShow)).append("\n");
/*  88:121 */     buffer.append("    .isxdiSort =").append(HexDump.shortToHex(this._isxdiSort)).append("\n");
/*  89:122 */     buffer.append("    .isxdiShow =").append(HexDump.shortToHex(this._isxdiShow)).append("\n");
/*  90:123 */     buffer.append("    .subtotalName =").append(this._subtotalName).append("\n");
/*  91:124 */     buffer.append("[/SXVDEX]\n");
/*  92:125 */     return buffer.toString();
/*  93:    */   }
/*  94:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.ExtendedPivotTableViewFieldsRecord
 * JD-Core Version:    0.7.0.1
 */