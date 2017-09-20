/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*   4:    */ import org.apache.poi.util.HexDump;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class SelectionRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 29;
/*  11:    */   private byte field_1_pane;
/*  12:    */   private int field_2_row_active_cell;
/*  13:    */   private int field_3_col_active_cell;
/*  14:    */   private int field_4_active_cell_ref_index;
/*  15:    */   private CellRangeAddress8Bit[] field_6_refs;
/*  16:    */   
/*  17:    */   public SelectionRecord(int activeCellRow, int activeCellCol)
/*  18:    */   {
/*  19: 46 */     this.field_1_pane = 3;
/*  20: 47 */     this.field_2_row_active_cell = activeCellRow;
/*  21: 48 */     this.field_3_col_active_cell = activeCellCol;
/*  22: 49 */     this.field_4_active_cell_ref_index = 0;
/*  23: 50 */     this.field_6_refs = new CellRangeAddress8Bit[] { new CellRangeAddress8Bit(activeCellRow, activeCellRow, activeCellCol, activeCellCol) };
/*  24:    */   }
/*  25:    */   
/*  26:    */   public SelectionRecord(RecordInputStream in)
/*  27:    */   {
/*  28: 56 */     this.field_1_pane = in.readByte();
/*  29: 57 */     this.field_2_row_active_cell = in.readUShort();
/*  30: 58 */     this.field_3_col_active_cell = in.readShort();
/*  31: 59 */     this.field_4_active_cell_ref_index = in.readShort();
/*  32: 60 */     int field_5_num_refs = in.readUShort();
/*  33:    */     
/*  34: 62 */     this.field_6_refs = new CellRangeAddress8Bit[field_5_num_refs];
/*  35: 63 */     for (int i = 0; i < this.field_6_refs.length; i++) {
/*  36: 64 */       this.field_6_refs[i] = new CellRangeAddress8Bit(in);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setPane(byte pane)
/*  41:    */   {
/*  42: 73 */     this.field_1_pane = pane;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setActiveCellRow(int row)
/*  46:    */   {
/*  47: 81 */     this.field_2_row_active_cell = row;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setActiveCellCol(short col)
/*  51:    */   {
/*  52: 89 */     this.field_3_col_active_cell = col;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setActiveCellRef(short ref)
/*  56:    */   {
/*  57: 97 */     this.field_4_active_cell_ref_index = ref;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public byte getPane()
/*  61:    */   {
/*  62:104 */     return this.field_1_pane;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getActiveCellRow()
/*  66:    */   {
/*  67:112 */     return this.field_2_row_active_cell;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getActiveCellCol()
/*  71:    */   {
/*  72:120 */     return this.field_3_col_active_cell;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int getActiveCellRef()
/*  76:    */   {
/*  77:128 */     return this.field_4_active_cell_ref_index;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String toString()
/*  81:    */   {
/*  82:133 */     StringBuffer sb = new StringBuffer();
/*  83:    */     
/*  84:135 */     sb.append("[SELECTION]\n");
/*  85:136 */     sb.append("    .pane            = ").append(HexDump.byteToHex(getPane())).append("\n");
/*  86:137 */     sb.append("    .activecellrow   = ").append(HexDump.shortToHex(getActiveCellRow())).append("\n");
/*  87:138 */     sb.append("    .activecellcol   = ").append(HexDump.shortToHex(getActiveCellCol())).append("\n");
/*  88:139 */     sb.append("    .activecellref   = ").append(HexDump.shortToHex(getActiveCellRef())).append("\n");
/*  89:140 */     sb.append("    .numrefs         = ").append(HexDump.shortToHex(this.field_6_refs.length)).append("\n");
/*  90:141 */     sb.append("[/SELECTION]\n");
/*  91:142 */     return sb.toString();
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected int getDataSize()
/*  95:    */   {
/*  96:146 */     return 9 + CellRangeAddress8Bit.getEncodedSize(this.field_6_refs.length);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void serialize(LittleEndianOutput out)
/* 100:    */   {
/* 101:151 */     out.writeByte(getPane());
/* 102:152 */     out.writeShort(getActiveCellRow());
/* 103:153 */     out.writeShort(getActiveCellCol());
/* 104:154 */     out.writeShort(getActiveCellRef());
/* 105:155 */     int nRefs = this.field_6_refs.length;
/* 106:156 */     out.writeShort(nRefs);
/* 107:157 */     for (CellRangeAddress8Bit field_6_ref : this.field_6_refs) {
/* 108:158 */       field_6_ref.serialize(out);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public short getSid()
/* 113:    */   {
/* 114:164 */     return 29;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Object clone()
/* 118:    */   {
/* 119:169 */     SelectionRecord rec = new SelectionRecord(this.field_2_row_active_cell, this.field_3_col_active_cell);
/* 120:170 */     rec.field_1_pane = this.field_1_pane;
/* 121:171 */     rec.field_4_active_cell_ref_index = this.field_4_active_cell_ref_index;
/* 122:172 */     rec.field_6_refs = this.field_6_refs;
/* 123:173 */     return rec;
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SelectionRecord
 * JD-Core Version:    0.7.0.1
 */