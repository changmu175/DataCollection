/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.HexDump;
/*   4:    */ 
/*   5:    */ public abstract class OldCellRecord
/*   6:    */ {
/*   7:    */   private final short sid;
/*   8:    */   private final boolean isBiff2;
/*   9:    */   private final int field_1_row;
/*  10:    */   private final short field_2_column;
/*  11:    */   private int field_3_cell_attrs;
/*  12:    */   private short field_3_xf_index;
/*  13:    */   
/*  14:    */   protected OldCellRecord(RecordInputStream in, boolean isBiff2)
/*  15:    */   {
/*  16: 36 */     this.sid = in.getSid();
/*  17: 37 */     this.isBiff2 = isBiff2;
/*  18: 38 */     this.field_1_row = in.readUShort();
/*  19: 39 */     this.field_2_column = in.readShort();
/*  20: 41 */     if (isBiff2)
/*  21:    */     {
/*  22: 42 */       this.field_3_cell_attrs = (in.readUShort() << 8);
/*  23: 43 */       this.field_3_cell_attrs += in.readUByte();
/*  24:    */     }
/*  25:    */     else
/*  26:    */     {
/*  27: 45 */       this.field_3_xf_index = in.readShort();
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final int getRow()
/*  32:    */   {
/*  33: 50 */     return this.field_1_row;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public final short getColumn()
/*  37:    */   {
/*  38: 54 */     return this.field_2_column;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public final short getXFIndex()
/*  42:    */   {
/*  43: 64 */     return this.field_3_xf_index;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int getCellAttrs()
/*  47:    */   {
/*  48: 69 */     return this.field_3_cell_attrs;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isBiff2()
/*  52:    */   {
/*  53: 78 */     return this.isBiff2;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public short getSid()
/*  57:    */   {
/*  58: 82 */     return this.sid;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public final String toString()
/*  62:    */   {
/*  63: 87 */     StringBuilder sb = new StringBuilder();
/*  64: 88 */     String recordName = getRecordName();
/*  65:    */     
/*  66: 90 */     sb.append("[").append(recordName).append("]\n");
/*  67: 91 */     sb.append("    .row    = ").append(HexDump.shortToHex(getRow())).append("\n");
/*  68: 92 */     sb.append("    .col    = ").append(HexDump.shortToHex(getColumn())).append("\n");
/*  69: 93 */     if (isBiff2()) {
/*  70: 94 */       sb.append("    .cellattrs = ").append(HexDump.shortToHex(getCellAttrs())).append("\n");
/*  71:    */     } else {
/*  72: 96 */       sb.append("    .xfindex   = ").append(HexDump.shortToHex(getXFIndex())).append("\n");
/*  73:    */     }
/*  74: 98 */     appendValueText(sb);
/*  75: 99 */     sb.append("\n");
/*  76:100 */     sb.append("[/").append(recordName).append("]\n");
/*  77:101 */     return sb.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected abstract void appendValueText(StringBuilder paramStringBuilder);
/*  81:    */   
/*  82:    */   protected abstract String getRecordName();
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.OldCellRecord
 * JD-Core Version:    0.7.0.1
 */