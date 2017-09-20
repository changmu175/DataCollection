/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.util.CellRangeAddress8Bit;
/*   4:    */ import org.apache.poi.hssf.util.CellReference;
/*   5:    */ import org.apache.poi.util.BitField;
/*   6:    */ import org.apache.poi.util.BitFieldFactory;
/*   7:    */ import org.apache.poi.util.HexDump;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public final class TableRecord
/*  11:    */   extends SharedValueRecordBase
/*  12:    */ {
/*  13:    */   public static final short sid = 566;
/*  14: 40 */   private static final BitField alwaysCalc = BitFieldFactory.getInstance(1);
/*  15: 41 */   private static final BitField calcOnOpen = BitFieldFactory.getInstance(2);
/*  16: 42 */   private static final BitField rowOrColInpCell = BitFieldFactory.getInstance(4);
/*  17: 43 */   private static final BitField oneOrTwoVar = BitFieldFactory.getInstance(8);
/*  18: 44 */   private static final BitField rowDeleted = BitFieldFactory.getInstance(16);
/*  19: 45 */   private static final BitField colDeleted = BitFieldFactory.getInstance(32);
/*  20:    */   private int field_5_flags;
/*  21:    */   private int field_6_res;
/*  22:    */   private int field_7_rowInputRow;
/*  23:    */   private int field_8_colInputRow;
/*  24:    */   private int field_9_rowInputCol;
/*  25:    */   private int field_10_colInputCol;
/*  26:    */   
/*  27:    */   public TableRecord(RecordInputStream in)
/*  28:    */   {
/*  29: 55 */     super(in);
/*  30: 56 */     this.field_5_flags = in.readByte();
/*  31: 57 */     this.field_6_res = in.readByte();
/*  32: 58 */     this.field_7_rowInputRow = in.readShort();
/*  33: 59 */     this.field_8_colInputRow = in.readShort();
/*  34: 60 */     this.field_9_rowInputCol = in.readShort();
/*  35: 61 */     this.field_10_colInputCol = in.readShort();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public TableRecord(CellRangeAddress8Bit range)
/*  39:    */   {
/*  40: 65 */     super(range);
/*  41: 66 */     this.field_6_res = 0;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int getFlags()
/*  45:    */   {
/*  46: 70 */     return this.field_5_flags;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setFlags(int flags)
/*  50:    */   {
/*  51: 73 */     this.field_5_flags = flags;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getRowInputRow()
/*  55:    */   {
/*  56: 77 */     return this.field_7_rowInputRow;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setRowInputRow(int rowInputRow)
/*  60:    */   {
/*  61: 80 */     this.field_7_rowInputRow = rowInputRow;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getColInputRow()
/*  65:    */   {
/*  66: 84 */     return this.field_8_colInputRow;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setColInputRow(int colInputRow)
/*  70:    */   {
/*  71: 87 */     this.field_8_colInputRow = colInputRow;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getRowInputCol()
/*  75:    */   {
/*  76: 91 */     return this.field_9_rowInputCol;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setRowInputCol(int rowInputCol)
/*  80:    */   {
/*  81: 94 */     this.field_9_rowInputCol = rowInputCol;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getColInputCol()
/*  85:    */   {
/*  86: 98 */     return this.field_10_colInputCol;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setColInputCol(int colInputCol)
/*  90:    */   {
/*  91:101 */     this.field_10_colInputCol = colInputCol;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean isAlwaysCalc()
/*  95:    */   {
/*  96:106 */     return alwaysCalc.isSet(this.field_5_flags);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setAlwaysCalc(boolean flag)
/* 100:    */   {
/* 101:109 */     this.field_5_flags = alwaysCalc.setBoolean(this.field_5_flags, flag);
/* 102:    */   }
/* 103:    */   
/* 104:    */   public boolean isRowOrColInpCell()
/* 105:    */   {
/* 106:113 */     return rowOrColInpCell.isSet(this.field_5_flags);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setRowOrColInpCell(boolean flag)
/* 110:    */   {
/* 111:116 */     this.field_5_flags = rowOrColInpCell.setBoolean(this.field_5_flags, flag);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isOneNotTwoVar()
/* 115:    */   {
/* 116:120 */     return oneOrTwoVar.isSet(this.field_5_flags);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setOneNotTwoVar(boolean flag)
/* 120:    */   {
/* 121:123 */     this.field_5_flags = oneOrTwoVar.setBoolean(this.field_5_flags, flag);
/* 122:    */   }
/* 123:    */   
/* 124:    */   public boolean isColDeleted()
/* 125:    */   {
/* 126:127 */     return colDeleted.isSet(this.field_5_flags);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setColDeleted(boolean flag)
/* 130:    */   {
/* 131:130 */     this.field_5_flags = colDeleted.setBoolean(this.field_5_flags, flag);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean isRowDeleted()
/* 135:    */   {
/* 136:134 */     return rowDeleted.isSet(this.field_5_flags);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setRowDeleted(boolean flag)
/* 140:    */   {
/* 141:137 */     this.field_5_flags = rowDeleted.setBoolean(this.field_5_flags, flag);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public short getSid()
/* 145:    */   {
/* 146:142 */     return 566;
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected int getExtraDataSize()
/* 150:    */   {
/* 151:145 */     return 10;
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected void serializeExtraData(LittleEndianOutput out)
/* 155:    */   {
/* 156:150 */     out.writeByte(this.field_5_flags);
/* 157:151 */     out.writeByte(this.field_6_res);
/* 158:152 */     out.writeShort(this.field_7_rowInputRow);
/* 159:153 */     out.writeShort(this.field_8_colInputRow);
/* 160:154 */     out.writeShort(this.field_9_rowInputCol);
/* 161:155 */     out.writeShort(this.field_10_colInputCol);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String toString()
/* 165:    */   {
/* 166:159 */     StringBuffer buffer = new StringBuffer();
/* 167:160 */     buffer.append("[TABLE]\n");
/* 168:161 */     buffer.append("    .range    = ").append(getRange()).append("\n");
/* 169:162 */     buffer.append("    .flags    = ").append(HexDump.byteToHex(this.field_5_flags)).append("\n");
/* 170:163 */     buffer.append("    .alwaysClc= ").append(isAlwaysCalc()).append("\n");
/* 171:164 */     buffer.append("    .reserved = ").append(HexDump.intToHex(this.field_6_res)).append("\n");
/* 172:165 */     CellReference crRowInput = cr(this.field_7_rowInputRow, this.field_8_colInputRow);
/* 173:166 */     CellReference crColInput = cr(this.field_9_rowInputCol, this.field_10_colInputCol);
/* 174:167 */     buffer.append("    .rowInput = ").append(crRowInput.formatAsString()).append("\n");
/* 175:168 */     buffer.append("    .colInput = ").append(crColInput.formatAsString()).append("\n");
/* 176:169 */     buffer.append("[/TABLE]\n");
/* 177:170 */     return buffer.toString();
/* 178:    */   }
/* 179:    */   
/* 180:    */   private static CellReference cr(int rowIx, int colIxAndFlags)
/* 181:    */   {
/* 182:174 */     int colIx = colIxAndFlags & 0xFF;
/* 183:175 */     boolean isRowAbs = (colIxAndFlags & 0x8000) == 0;
/* 184:176 */     boolean isColAbs = (colIxAndFlags & 0x4000) == 0;
/* 185:177 */     return new CellReference(rowIx, colIx, isRowAbs, isColAbs);
/* 186:    */   }
/* 187:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.TableRecord
 * JD-Core Version:    0.7.0.1
 */