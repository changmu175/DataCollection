/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.constant.ConstantValueParser;
/*   4:    */ import org.apache.poi.ss.formula.constant.ErrorConstant;
/*   5:    */ import org.apache.poi.ss.util.NumberToTextConverter;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public final class ArrayPtg
/*  10:    */   extends Ptg
/*  11:    */ {
/*  12:    */   public static final byte sid = 32;
/*  13:    */   private static final int RESERVED_FIELD_LEN = 7;
/*  14:    */   public static final int PLAIN_TOKEN_SIZE = 8;
/*  15:    */   private final int _reserved0Int;
/*  16:    */   private final int _reserved1Short;
/*  17:    */   private final int _reserved2Byte;
/*  18:    */   private final int _nColumns;
/*  19:    */   private final int _nRows;
/*  20:    */   private final Object[] _arrayValues;
/*  21:    */   
/*  22:    */   ArrayPtg(int reserved0, int reserved1, int reserved2, int nColumns, int nRows, Object[] arrayValues)
/*  23:    */   {
/*  24: 58 */     this._reserved0Int = reserved0;
/*  25: 59 */     this._reserved1Short = reserved1;
/*  26: 60 */     this._reserved2Byte = reserved2;
/*  27: 61 */     this._nColumns = nColumns;
/*  28: 62 */     this._nRows = nRows;
/*  29: 63 */     this._arrayValues = ((Object[])arrayValues.clone());
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ArrayPtg(Object[][] values2d)
/*  33:    */   {
/*  34: 69 */     int nColumns = values2d[0].length;
/*  35: 70 */     int nRows = values2d.length;
/*  36:    */     
/*  37: 72 */     this._nColumns = ((short)nColumns);
/*  38: 73 */     this._nRows = ((short)nRows);
/*  39:    */     
/*  40: 75 */     Object[] vv = new Object[this._nColumns * this._nRows];
/*  41: 76 */     for (int r = 0; r < nRows; r++)
/*  42:    */     {
/*  43: 77 */       Object[] rowData = values2d[r];
/*  44: 78 */       for (int c = 0; c < nColumns; c++) {
/*  45: 79 */         vv[getValueIndex(c, r)] = rowData[c];
/*  46:    */       }
/*  47:    */     }
/*  48: 83 */     this._arrayValues = vv;
/*  49: 84 */     this._reserved0Int = 0;
/*  50: 85 */     this._reserved1Short = 0;
/*  51: 86 */     this._reserved2Byte = 0;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Object[][] getTokenArrayValues()
/*  55:    */   {
/*  56: 92 */     if (this._arrayValues == null) {
/*  57: 93 */       throw new IllegalStateException("array values not read yet");
/*  58:    */     }
/*  59: 95 */     Object[][] result = new Object[this._nRows][this._nColumns];
/*  60: 96 */     for (int r = 0; r < this._nRows; r++)
/*  61:    */     {
/*  62: 97 */       Object[] rowData = result[r];
/*  63: 98 */       for (int c = 0; c < this._nColumns; c++) {
/*  64: 99 */         rowData[c] = this._arrayValues[getValueIndex(c, r)];
/*  65:    */       }
/*  66:    */     }
/*  67:102 */     return result;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean isBaseToken()
/*  71:    */   {
/*  72:106 */     return false;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String toString()
/*  76:    */   {
/*  77:110 */     StringBuffer sb = new StringBuffer("[ArrayPtg]\n");
/*  78:    */     
/*  79:112 */     sb.append("nRows = ").append(getRowCount()).append("\n");
/*  80:113 */     sb.append("nCols = ").append(getColumnCount()).append("\n");
/*  81:114 */     if (this._arrayValues == null) {
/*  82:115 */       sb.append("  #values#uninitialised#\n");
/*  83:    */     } else {
/*  84:117 */       sb.append("  ").append(toFormulaString());
/*  85:    */     }
/*  86:119 */     return sb.toString();
/*  87:    */   }
/*  88:    */   
/*  89:    */   int getValueIndex(int colIx, int rowIx)
/*  90:    */   {
/*  91:127 */     if ((colIx < 0) || (colIx >= this._nColumns)) {
/*  92:128 */       throw new IllegalArgumentException("Specified colIx (" + colIx + ") is outside the allowed range (0.." + (this._nColumns - 1) + ")");
/*  93:    */     }
/*  94:131 */     if ((rowIx < 0) || (rowIx >= this._nRows)) {
/*  95:132 */       throw new IllegalArgumentException("Specified rowIx (" + rowIx + ") is outside the allowed range (0.." + (this._nRows - 1) + ")");
/*  96:    */     }
/*  97:135 */     return rowIx * this._nColumns + colIx;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void write(LittleEndianOutput out)
/* 101:    */   {
/* 102:139 */     out.writeByte(32 + getPtgClass());
/* 103:140 */     out.writeInt(this._reserved0Int);
/* 104:141 */     out.writeShort(this._reserved1Short);
/* 105:142 */     out.writeByte(this._reserved2Byte);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public int writeTokenValueBytes(LittleEndianOutput out)
/* 109:    */   {
/* 110:147 */     out.writeByte(this._nColumns - 1);
/* 111:148 */     out.writeShort(this._nRows - 1);
/* 112:149 */     ConstantValueParser.encode(out, this._arrayValues);
/* 113:150 */     return 3 + ConstantValueParser.getEncodedSize(this._arrayValues);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int getRowCount()
/* 117:    */   {
/* 118:154 */     return this._nRows;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int getColumnCount()
/* 122:    */   {
/* 123:158 */     return this._nColumns;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public int getSize()
/* 127:    */   {
/* 128:163 */     return 11 + ConstantValueParser.getEncodedSize(this._arrayValues);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String toFormulaString()
/* 132:    */   {
/* 133:170 */     StringBuffer b = new StringBuffer();
/* 134:171 */     b.append("{");
/* 135:172 */     for (int y = 0; y < this._nRows; y++)
/* 136:    */     {
/* 137:173 */       if (y > 0) {
/* 138:174 */         b.append(";");
/* 139:    */       }
/* 140:176 */       for (int x = 0; x < this._nColumns; x++)
/* 141:    */       {
/* 142:177 */         if (x > 0) {
/* 143:178 */           b.append(",");
/* 144:    */         }
/* 145:180 */         Object o = this._arrayValues[getValueIndex(x, y)];
/* 146:181 */         b.append(getConstantText(o));
/* 147:    */       }
/* 148:    */     }
/* 149:184 */     b.append("}");
/* 150:185 */     return b.toString();
/* 151:    */   }
/* 152:    */   
/* 153:    */   private static String getConstantText(Object o)
/* 154:    */   {
/* 155:190 */     if (o == null) {
/* 156:191 */       throw new RuntimeException("Array item cannot be null");
/* 157:    */     }
/* 158:193 */     if ((o instanceof String)) {
/* 159:194 */       return "\"" + (String)o + "\"";
/* 160:    */     }
/* 161:196 */     if ((o instanceof Double)) {
/* 162:197 */       return NumberToTextConverter.toText(((Double)o).doubleValue());
/* 163:    */     }
/* 164:199 */     if ((o instanceof Boolean)) {
/* 165:200 */       return ((Boolean)o).booleanValue() ? "TRUE" : "FALSE";
/* 166:    */     }
/* 167:202 */     if ((o instanceof ErrorConstant)) {
/* 168:203 */       return ((ErrorConstant)o).getText();
/* 169:    */     }
/* 170:205 */     throw new IllegalArgumentException("Unexpected constant class (" + o.getClass().getName() + ")");
/* 171:    */   }
/* 172:    */   
/* 173:    */   public byte getDefaultOperandClass()
/* 174:    */   {
/* 175:209 */     return 64;
/* 176:    */   }
/* 177:    */   
/* 178:    */   static final class Initial
/* 179:    */     extends Ptg
/* 180:    */   {
/* 181:    */     private final int _reserved0;
/* 182:    */     private final int _reserved1;
/* 183:    */     private final int _reserved2;
/* 184:    */     
/* 185:    */     public Initial(LittleEndianInput in)
/* 186:    */     {
/* 187:224 */       this._reserved0 = in.readInt();
/* 188:225 */       this._reserved1 = in.readUShort();
/* 189:226 */       this._reserved2 = in.readUByte();
/* 190:    */     }
/* 191:    */     
/* 192:    */     private static RuntimeException invalid()
/* 193:    */     {
/* 194:229 */       throw new IllegalStateException("This object is a partially initialised tArray, and cannot be used as a Ptg");
/* 195:    */     }
/* 196:    */     
/* 197:    */     public byte getDefaultOperandClass()
/* 198:    */     {
/* 199:232 */       throw invalid();
/* 200:    */     }
/* 201:    */     
/* 202:    */     public int getSize()
/* 203:    */     {
/* 204:235 */       return 8;
/* 205:    */     }
/* 206:    */     
/* 207:    */     public boolean isBaseToken()
/* 208:    */     {
/* 209:238 */       return false;
/* 210:    */     }
/* 211:    */     
/* 212:    */     public String toFormulaString()
/* 213:    */     {
/* 214:241 */       throw invalid();
/* 215:    */     }
/* 216:    */     
/* 217:    */     public void write(LittleEndianOutput out)
/* 218:    */     {
/* 219:244 */       throw invalid();
/* 220:    */     }
/* 221:    */     
/* 222:    */     public ArrayPtg finishReading(LittleEndianInput in)
/* 223:    */     {
/* 224:252 */       int nColumns = in.readUByte();
/* 225:253 */       short nRows = in.readShort();
/* 226:    */       
/* 227:    */ 
/* 228:    */ 
/* 229:257 */       nColumns++;
/* 230:258 */       nRows = (short)(nRows + 1);
/* 231:    */       
/* 232:260 */       int totalCount = nRows * nColumns;
/* 233:261 */       Object[] arrayValues = ConstantValueParser.parse(in, totalCount);
/* 234:    */       
/* 235:263 */       ArrayPtg result = new ArrayPtg(this._reserved0, this._reserved1, this._reserved2, nColumns, nRows, arrayValues);
/* 236:264 */       result.setClass(getPtgClass());
/* 237:265 */       return result;
/* 238:    */     }
/* 239:    */   }
/* 240:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.ArrayPtg
 * JD-Core Version:    0.7.0.1
 */