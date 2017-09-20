/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.util.AreaReference;
/*   5:    */ import org.apache.poi.ss.util.CellReference;
/*   6:    */ import org.apache.poi.util.BitField;
/*   7:    */ import org.apache.poi.util.BitFieldFactory;
/*   8:    */ import org.apache.poi.util.LittleEndianInput;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ 
/*  11:    */ public abstract class AreaPtgBase
/*  12:    */   extends OperandPtg
/*  13:    */   implements AreaI
/*  14:    */ {
/*  15:    */   private int field_1_first_row;
/*  16:    */   private int field_2_last_row;
/*  17:    */   private int field_3_first_column;
/*  18:    */   private int field_4_last_column;
/*  19:    */   
/*  20:    */   protected final RuntimeException notImplemented()
/*  21:    */   {
/*  22: 39 */     return new RuntimeException("Coding Error: This method should never be called. This ptg should be converted");
/*  23:    */   }
/*  24:    */   
/*  25: 51 */   private static final BitField rowRelative = BitFieldFactory.getInstance(32768);
/*  26: 52 */   private static final BitField colRelative = BitFieldFactory.getInstance(16384);
/*  27: 53 */   private static final BitField columnMask = BitFieldFactory.getInstance(16383);
/*  28:    */   
/*  29:    */   protected AreaPtgBase() {}
/*  30:    */   
/*  31:    */   protected AreaPtgBase(AreaReference ar)
/*  32:    */   {
/*  33: 60 */     CellReference firstCell = ar.getFirstCell();
/*  34: 61 */     CellReference lastCell = ar.getLastCell();
/*  35: 62 */     setFirstRow(firstCell.getRow());
/*  36: 63 */     setFirstColumn(firstCell.getCol() == -1 ? 0 : firstCell.getCol());
/*  37: 64 */     setLastRow(lastCell.getRow());
/*  38: 65 */     setLastColumn(lastCell.getCol() == -1 ? 255 : lastCell.getCol());
/*  39: 66 */     setFirstColRelative(!firstCell.isColAbsolute());
/*  40: 67 */     setLastColRelative(!lastCell.isColAbsolute());
/*  41: 68 */     setFirstRowRelative(!firstCell.isRowAbsolute());
/*  42: 69 */     setLastRowRelative(!lastCell.isRowAbsolute());
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected AreaPtgBase(int firstRow, int lastRow, int firstColumn, int lastColumn, boolean firstRowRelative, boolean lastRowRelative, boolean firstColRelative, boolean lastColRelative)
/*  46:    */   {
/*  47: 75 */     if (lastRow >= firstRow)
/*  48:    */     {
/*  49: 76 */       setFirstRow(firstRow);
/*  50: 77 */       setLastRow(lastRow);
/*  51: 78 */       setFirstRowRelative(firstRowRelative);
/*  52: 79 */       setLastRowRelative(lastRowRelative);
/*  53:    */     }
/*  54:    */     else
/*  55:    */     {
/*  56: 81 */       setFirstRow(lastRow);
/*  57: 82 */       setLastRow(firstRow);
/*  58: 83 */       setFirstRowRelative(lastRowRelative);
/*  59: 84 */       setLastRowRelative(firstRowRelative);
/*  60:    */     }
/*  61: 87 */     if (lastColumn >= firstColumn)
/*  62:    */     {
/*  63: 88 */       setFirstColumn(firstColumn);
/*  64: 89 */       setLastColumn(lastColumn);
/*  65: 90 */       setFirstColRelative(firstColRelative);
/*  66: 91 */       setLastColRelative(lastColRelative);
/*  67:    */     }
/*  68:    */     else
/*  69:    */     {
/*  70: 93 */       setFirstColumn(lastColumn);
/*  71: 94 */       setLastColumn(firstColumn);
/*  72: 95 */       setFirstColRelative(lastColRelative);
/*  73: 96 */       setLastColRelative(firstColRelative);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void sortTopLeftToBottomRight()
/*  78:    */   {
/*  79:107 */     if (getFirstRow() > getLastRow())
/*  80:    */     {
/*  81:110 */       int firstRow = getFirstRow();
/*  82:111 */       boolean firstRowRel = isFirstRowRelative();
/*  83:112 */       setFirstRow(getLastRow());
/*  84:113 */       setFirstRowRelative(isLastRowRelative());
/*  85:114 */       setLastRow(firstRow);
/*  86:115 */       setLastRowRelative(firstRowRel);
/*  87:    */     }
/*  88:117 */     if (getFirstColumn() > getLastColumn())
/*  89:    */     {
/*  90:120 */       int firstCol = getFirstColumn();
/*  91:121 */       boolean firstColRel = isFirstColRelative();
/*  92:122 */       setFirstColumn(getLastColumn());
/*  93:123 */       setFirstColRelative(isLastColRelative());
/*  94:124 */       setLastColumn(firstCol);
/*  95:125 */       setLastColRelative(firstColRel);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected final void readCoordinates(LittleEndianInput in)
/* 100:    */   {
/* 101:130 */     this.field_1_first_row = in.readUShort();
/* 102:131 */     this.field_2_last_row = in.readUShort();
/* 103:132 */     this.field_3_first_column = in.readUShort();
/* 104:133 */     this.field_4_last_column = in.readUShort();
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected final void writeCoordinates(LittleEndianOutput out)
/* 108:    */   {
/* 109:136 */     out.writeShort(this.field_1_first_row);
/* 110:137 */     out.writeShort(this.field_2_last_row);
/* 111:138 */     out.writeShort(this.field_3_first_column);
/* 112:139 */     out.writeShort(this.field_4_last_column);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public final int getFirstRow()
/* 116:    */   {
/* 117:146 */     return this.field_1_first_row;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public final void setFirstRow(int rowIx)
/* 121:    */   {
/* 122:154 */     this.field_1_first_row = rowIx;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public final int getLastRow()
/* 126:    */   {
/* 127:161 */     return this.field_2_last_row;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public final void setLastRow(int rowIx)
/* 131:    */   {
/* 132:168 */     this.field_2_last_row = rowIx;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public final int getFirstColumn()
/* 136:    */   {
/* 137:175 */     return columnMask.getValue(this.field_3_first_column);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public final short getFirstColumnRaw()
/* 141:    */   {
/* 142:182 */     return (short)this.field_3_first_column;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public final boolean isFirstRowRelative()
/* 146:    */   {
/* 147:189 */     return rowRelative.isSet(this.field_3_first_column);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public final void setFirstRowRelative(boolean rel)
/* 151:    */   {
/* 152:197 */     this.field_3_first_column = rowRelative.setBoolean(this.field_3_first_column, rel);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public final boolean isFirstColRelative()
/* 156:    */   {
/* 157:204 */     return colRelative.isSet(this.field_3_first_column);
/* 158:    */   }
/* 159:    */   
/* 160:    */   public final void setFirstColRelative(boolean rel)
/* 161:    */   {
/* 162:211 */     this.field_3_first_column = colRelative.setBoolean(this.field_3_first_column, rel);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public final void setFirstColumn(int colIx)
/* 166:    */   {
/* 167:218 */     this.field_3_first_column = columnMask.setValue(this.field_3_first_column, colIx);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public final void setFirstColumnRaw(int column)
/* 171:    */   {
/* 172:225 */     this.field_3_first_column = column;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public final int getLastColumn()
/* 176:    */   {
/* 177:232 */     return columnMask.getValue(this.field_4_last_column);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public final short getLastColumnRaw()
/* 181:    */   {
/* 182:239 */     return (short)this.field_4_last_column;
/* 183:    */   }
/* 184:    */   
/* 185:    */   public final boolean isLastRowRelative()
/* 186:    */   {
/* 187:246 */     return rowRelative.isSet(this.field_4_last_column);
/* 188:    */   }
/* 189:    */   
/* 190:    */   public final void setLastRowRelative(boolean rel)
/* 191:    */   {
/* 192:255 */     this.field_4_last_column = rowRelative.setBoolean(this.field_4_last_column, rel);
/* 193:    */   }
/* 194:    */   
/* 195:    */   public final boolean isLastColRelative()
/* 196:    */   {
/* 197:262 */     return colRelative.isSet(this.field_4_last_column);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public final void setLastColRelative(boolean rel)
/* 201:    */   {
/* 202:269 */     this.field_4_last_column = colRelative.setBoolean(this.field_4_last_column, rel);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public final void setLastColumn(int colIx)
/* 206:    */   {
/* 207:276 */     this.field_4_last_column = columnMask.setValue(this.field_4_last_column, colIx);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public final void setLastColumnRaw(short column)
/* 211:    */   {
/* 212:283 */     this.field_4_last_column = column;
/* 213:    */   }
/* 214:    */   
/* 215:    */   protected final String formatReferenceAsString()
/* 216:    */   {
/* 217:286 */     CellReference topLeft = new CellReference(getFirstRow(), getFirstColumn(), !isFirstRowRelative(), !isFirstColRelative());
/* 218:287 */     CellReference botRight = new CellReference(getLastRow(), getLastColumn(), !isLastRowRelative(), !isLastColRelative());
/* 219:289 */     if (AreaReference.isWholeColumnReference(SpreadsheetVersion.EXCEL97, topLeft, botRight)) {
/* 220:290 */       return new AreaReference(topLeft, botRight, SpreadsheetVersion.EXCEL97).formatAsString();
/* 221:    */     }
/* 222:292 */     return topLeft.formatAsString() + ":" + botRight.formatAsString();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public String toFormulaString()
/* 226:    */   {
/* 227:296 */     return formatReferenceAsString();
/* 228:    */   }
/* 229:    */   
/* 230:    */   public byte getDefaultOperandClass()
/* 231:    */   {
/* 232:300 */     return 0;
/* 233:    */   }
/* 234:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AreaPtgBase
 * JD-Core Version:    0.7.0.1
 */