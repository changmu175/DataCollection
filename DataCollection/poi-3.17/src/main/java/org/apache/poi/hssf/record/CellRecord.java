/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ 
/*   6:    */ public abstract class CellRecord
/*   7:    */   extends StandardRecord
/*   8:    */   implements CellValueRecordInterface
/*   9:    */ {
/*  10:    */   private int _rowIndex;
/*  11:    */   private int _columnIndex;
/*  12:    */   private int _formatIndex;
/*  13:    */   
/*  14:    */   protected CellRecord() {}
/*  15:    */   
/*  16:    */   protected CellRecord(RecordInputStream in)
/*  17:    */   {
/*  18: 37 */     this._rowIndex = in.readUShort();
/*  19: 38 */     this._columnIndex = in.readUShort();
/*  20: 39 */     this._formatIndex = in.readUShort();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public final void setRow(int row)
/*  24:    */   {
/*  25: 44 */     this._rowIndex = row;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public final void setColumn(short col)
/*  29:    */   {
/*  30: 49 */     this._columnIndex = col;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public final void setXFIndex(short xf)
/*  34:    */   {
/*  35: 60 */     this._formatIndex = xf;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public final int getRow()
/*  39:    */   {
/*  40: 65 */     return this._rowIndex;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public final short getColumn()
/*  44:    */   {
/*  45: 70 */     return (short)this._columnIndex;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public final short getXFIndex()
/*  49:    */   {
/*  50: 81 */     return (short)this._formatIndex;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public final String toString()
/*  54:    */   {
/*  55: 86 */     StringBuilder sb = new StringBuilder();
/*  56: 87 */     String recordName = getRecordName();
/*  57:    */     
/*  58: 89 */     sb.append("[").append(recordName).append("]\n");
/*  59: 90 */     sb.append("    .row    = ").append(HexDump.shortToHex(getRow())).append("\n");
/*  60: 91 */     sb.append("    .col    = ").append(HexDump.shortToHex(getColumn())).append("\n");
/*  61: 92 */     sb.append("    .xfindex= ").append(HexDump.shortToHex(getXFIndex())).append("\n");
/*  62: 93 */     appendValueText(sb);
/*  63: 94 */     sb.append("\n");
/*  64: 95 */     sb.append("[/").append(recordName).append("]\n");
/*  65: 96 */     return sb.toString();
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected abstract void appendValueText(StringBuilder paramStringBuilder);
/*  69:    */   
/*  70:    */   protected abstract String getRecordName();
/*  71:    */   
/*  72:    */   protected abstract void serializeValue(LittleEndianOutput paramLittleEndianOutput);
/*  73:    */   
/*  74:    */   protected abstract int getValueDataSize();
/*  75:    */   
/*  76:    */   public final void serialize(LittleEndianOutput out)
/*  77:    */   {
/*  78:129 */     out.writeShort(getRow());
/*  79:130 */     out.writeShort(getColumn());
/*  80:131 */     out.writeShort(getXFIndex());
/*  81:132 */     serializeValue(out);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected final int getDataSize()
/*  85:    */   {
/*  86:137 */     return 6 + getValueDataSize();
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected final void copyBaseFields(CellRecord rec)
/*  90:    */   {
/*  91:141 */     rec._rowIndex = this._rowIndex;
/*  92:142 */     rec._columnIndex = this._columnIndex;
/*  93:143 */     rec._formatIndex = this._formatIndex;
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.CellRecord
 * JD-Core Version:    0.7.0.1
 */