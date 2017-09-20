/*   1:    */ package org.apache.poi.hssf.record.pivottable;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.hssf.record.StandardRecord;
/*   5:    */ import org.apache.poi.util.HexDump;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ import org.apache.poi.util.StringUtil;
/*   8:    */ 
/*   9:    */ public final class ViewFieldsRecord
/*  10:    */   extends StandardRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 177;
/*  13:    */   private static final int STRING_NOT_PRESENT_LEN = 65535;
/*  14:    */   private static final int BASE_SIZE = 10;
/*  15:    */   private int _sxaxis;
/*  16:    */   private int _cSub;
/*  17:    */   private int _grbitSub;
/*  18:    */   private int _cItm;
/*  19:    */   private String _name;
/*  20:    */   
/*  21:    */   public ViewFieldsRecord(RecordInputStream in)
/*  22:    */   {
/*  23: 58 */     this._sxaxis = in.readShort();
/*  24: 59 */     this._cSub = in.readShort();
/*  25: 60 */     this._grbitSub = in.readShort();
/*  26: 61 */     this._cItm = in.readShort();
/*  27:    */     
/*  28: 63 */     int cchName = in.readUShort();
/*  29: 64 */     if (cchName != 65535)
/*  30:    */     {
/*  31: 65 */       int flag = in.readByte();
/*  32: 66 */       if ((flag & 0x1) != 0) {
/*  33: 67 */         this._name = in.readUnicodeLEString(cchName);
/*  34:    */       } else {
/*  35: 69 */         this._name = in.readCompressedUnicode(cchName);
/*  36:    */       }
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected void serialize(LittleEndianOutput out)
/*  41:    */   {
/*  42: 77 */     out.writeShort(this._sxaxis);
/*  43: 78 */     out.writeShort(this._cSub);
/*  44: 79 */     out.writeShort(this._grbitSub);
/*  45: 80 */     out.writeShort(this._cItm);
/*  46: 82 */     if (this._name != null) {
/*  47: 83 */       StringUtil.writeUnicodeString(out, this._name);
/*  48:    */     } else {
/*  49: 85 */       out.writeShort(65535);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected int getDataSize()
/*  54:    */   {
/*  55: 91 */     if (this._name == null) {
/*  56: 92 */       return 10;
/*  57:    */     }
/*  58: 94 */     return 11 + this._name.length() * (StringUtil.hasMultibyte(this._name) ? 2 : 1);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public short getSid()
/*  62:    */   {
/*  63:101 */     return 177;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String toString()
/*  67:    */   {
/*  68:106 */     StringBuffer buffer = new StringBuffer();
/*  69:107 */     buffer.append("[SXVD]\n");
/*  70:108 */     buffer.append("    .sxaxis    = ").append(HexDump.shortToHex(this._sxaxis)).append('\n');
/*  71:109 */     buffer.append("    .cSub      = ").append(HexDump.shortToHex(this._cSub)).append('\n');
/*  72:110 */     buffer.append("    .grbitSub  = ").append(HexDump.shortToHex(this._grbitSub)).append('\n');
/*  73:111 */     buffer.append("    .cItm      = ").append(HexDump.shortToHex(this._cItm)).append('\n');
/*  74:112 */     buffer.append("    .name      = ").append(this._name).append('\n');
/*  75:    */     
/*  76:114 */     buffer.append("[/SXVD]\n");
/*  77:115 */     return buffer.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static final class Axis
/*  81:    */   {
/*  82:    */     public static final int NO_AXIS = 0;
/*  83:    */     public static final int ROW = 1;
/*  84:    */     public static final int COLUMN = 2;
/*  85:    */     public static final int PAGE = 4;
/*  86:    */     public static final int DATA = 8;
/*  87:    */   }
/*  88:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.pivottable.ViewFieldsRecord
 * JD-Core Version:    0.7.0.1
 */