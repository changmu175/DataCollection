/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianInput;
/*   6:    */ import org.apache.poi.util.LittleEndianOutput;
/*   7:    */ 
/*   8:    */ public final class AttrPtg
/*   9:    */   extends ControlPtg
/*  10:    */ {
/*  11:    */   public static final byte sid = 25;
/*  12:    */   private static final int SIZE = 4;
/*  13:    */   private final byte _options;
/*  14:    */   private final short _data;
/*  15:    */   private final int[] _jumpTable;
/*  16:    */   private final int _chooseFuncOffset;
/*  17: 46 */   private static final BitField semiVolatile = BitFieldFactory.getInstance(1);
/*  18: 47 */   private static final BitField optiIf = BitFieldFactory.getInstance(2);
/*  19: 48 */   private static final BitField optiChoose = BitFieldFactory.getInstance(4);
/*  20: 49 */   private static final BitField optiSkip = BitFieldFactory.getInstance(8);
/*  21: 50 */   private static final BitField optiSum = BitFieldFactory.getInstance(16);
/*  22: 51 */   private static final BitField baxcel = BitFieldFactory.getInstance(32);
/*  23: 52 */   private static final BitField space = BitFieldFactory.getInstance(64);
/*  24: 54 */   public static final AttrPtg SUM = new AttrPtg(16, 0, null, -1);
/*  25:    */   
/*  26:    */   public AttrPtg(LittleEndianInput in)
/*  27:    */   {
/*  28: 78 */     this._options = in.readByte();
/*  29: 79 */     this._data = in.readShort();
/*  30: 80 */     if (isOptimizedChoose())
/*  31:    */     {
/*  32: 81 */       int nCases = this._data;
/*  33: 82 */       int[] jumpTable = new int[nCases];
/*  34: 83 */       for (int i = 0; i < jumpTable.length; i++) {
/*  35: 84 */         jumpTable[i] = in.readUShort();
/*  36:    */       }
/*  37: 86 */       this._jumpTable = jumpTable;
/*  38: 87 */       this._chooseFuncOffset = in.readUShort();
/*  39:    */     }
/*  40:    */     else
/*  41:    */     {
/*  42: 89 */       this._jumpTable = null;
/*  43: 90 */       this._chooseFuncOffset = -1;
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   private AttrPtg(int options, int data, int[] jt, int chooseFuncOffset)
/*  48:    */   {
/*  49: 95 */     this._options = ((byte)options);
/*  50: 96 */     this._data = ((short)data);
/*  51: 97 */     this._jumpTable = jt;
/*  52: 98 */     this._chooseFuncOffset = chooseFuncOffset;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static AttrPtg createSpace(int type, int count)
/*  56:    */   {
/*  57:106 */     int data = type & 0xFF | count << 8 & 0xFFFF;
/*  58:107 */     return new AttrPtg(space.set(0), data, null, -1);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static AttrPtg createIf(int dist)
/*  62:    */   {
/*  63:115 */     return new AttrPtg(optiIf.set(0), dist, null, -1);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static AttrPtg createSkip(int dist)
/*  67:    */   {
/*  68:122 */     return new AttrPtg(optiSkip.set(0), dist, null, -1);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static AttrPtg getSumSingle()
/*  72:    */   {
/*  73:126 */     return new AttrPtg(optiSum.set(0), 0, null, -1);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isSemiVolatile()
/*  77:    */   {
/*  78:131 */     return semiVolatile.isSet(this._options);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isOptimizedIf()
/*  82:    */   {
/*  83:135 */     return optiIf.isSet(this._options);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean isOptimizedChoose()
/*  87:    */   {
/*  88:139 */     return optiChoose.isSet(this._options);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isSum()
/*  92:    */   {
/*  93:143 */     return optiSum.isSet(this._options);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean isSkip()
/*  97:    */   {
/*  98:146 */     return optiSkip.isSet(this._options);
/*  99:    */   }
/* 100:    */   
/* 101:    */   private boolean isBaxcel()
/* 102:    */   {
/* 103:151 */     return baxcel.isSet(this._options);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean isSpace()
/* 107:    */   {
/* 108:155 */     return space.isSet(this._options);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public short getData()
/* 112:    */   {
/* 113:159 */     return this._data;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int[] getJumpTable()
/* 117:    */   {
/* 118:162 */     return (int[])this._jumpTable.clone();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int getChooseFuncOffset()
/* 122:    */   {
/* 123:165 */     if (this._jumpTable == null) {
/* 124:166 */       throw new IllegalStateException("Not tAttrChoose");
/* 125:    */     }
/* 126:168 */     return this._chooseFuncOffset;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String toString()
/* 130:    */   {
/* 131:172 */     StringBuffer sb = new StringBuffer(64);
/* 132:173 */     sb.append(getClass().getName()).append(" [");
/* 133:175 */     if (isSemiVolatile()) {
/* 134:176 */       sb.append("volatile ");
/* 135:    */     }
/* 136:178 */     if (isSpace())
/* 137:    */     {
/* 138:179 */       sb.append("space count=").append(this._data >> 8 & 0xFF);
/* 139:180 */       sb.append(" type=").append(this._data & 0xFF).append(" ");
/* 140:    */     }
/* 141:183 */     if (isOptimizedIf()) {
/* 142:184 */       sb.append("if dist=").append(this._data);
/* 143:185 */     } else if (isOptimizedChoose()) {
/* 144:186 */       sb.append("choose nCases=").append(this._data);
/* 145:187 */     } else if (isSkip()) {
/* 146:188 */       sb.append("skip dist=").append(this._data);
/* 147:189 */     } else if (isSum()) {
/* 148:190 */       sb.append("sum ");
/* 149:191 */     } else if (isBaxcel()) {
/* 150:192 */       sb.append("assign ");
/* 151:    */     }
/* 152:194 */     sb.append("]");
/* 153:195 */     return sb.toString();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void write(LittleEndianOutput out)
/* 157:    */   {
/* 158:199 */     out.writeByte(25 + getPtgClass());
/* 159:200 */     out.writeByte(this._options);
/* 160:201 */     out.writeShort(this._data);
/* 161:202 */     int[] jt = this._jumpTable;
/* 162:203 */     if (jt != null)
/* 163:    */     {
/* 164:204 */       for (int i = 0; i < jt.length; i++) {
/* 165:205 */         out.writeShort(jt[i]);
/* 166:    */       }
/* 167:207 */       out.writeShort(this._chooseFuncOffset);
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   public int getSize()
/* 172:    */   {
/* 173:212 */     if (this._jumpTable != null) {
/* 174:213 */       return 4 + (this._jumpTable.length + 1) * 2;
/* 175:    */     }
/* 176:215 */     return 4;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public String toFormulaString(String[] operands)
/* 180:    */   {
/* 181:219 */     if (space.isSet(this._options)) {
/* 182:220 */       return operands[0];
/* 183:    */     }
/* 184:221 */     if (optiIf.isSet(this._options)) {
/* 185:222 */       return toFormulaString() + "(" + operands[0] + ")";
/* 186:    */     }
/* 187:223 */     if (optiSkip.isSet(this._options)) {
/* 188:224 */       return toFormulaString() + operands[0];
/* 189:    */     }
/* 190:226 */     return toFormulaString() + "(" + operands[0] + ")";
/* 191:    */   }
/* 192:    */   
/* 193:    */   public int getNumberOfOperands()
/* 194:    */   {
/* 195:232 */     return 1;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public int getType()
/* 199:    */   {
/* 200:236 */     return -1;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public String toFormulaString()
/* 204:    */   {
/* 205:240 */     if (semiVolatile.isSet(this._options)) {
/* 206:241 */       return "ATTR(semiVolatile)";
/* 207:    */     }
/* 208:243 */     if (optiIf.isSet(this._options)) {
/* 209:244 */       return "IF";
/* 210:    */     }
/* 211:246 */     if (optiChoose.isSet(this._options)) {
/* 212:247 */       return "CHOOSE";
/* 213:    */     }
/* 214:249 */     if (optiSkip.isSet(this._options)) {
/* 215:250 */       return "";
/* 216:    */     }
/* 217:252 */     if (optiSum.isSet(this._options)) {
/* 218:253 */       return "SUM";
/* 219:    */     }
/* 220:255 */     if (baxcel.isSet(this._options)) {
/* 221:256 */       return "ATTR(baxcel)";
/* 222:    */     }
/* 223:258 */     if (space.isSet(this._options)) {
/* 224:259 */       return "";
/* 225:    */     }
/* 226:261 */     return "UNKNOWN ATTRIBUTE";
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static final class SpaceType
/* 230:    */   {
/* 231:    */     public static final int SPACE_BEFORE = 0;
/* 232:    */     public static final int CR_BEFORE = 1;
/* 233:    */     public static final int SPACE_BEFORE_OPEN_PAREN = 2;
/* 234:    */     public static final int CR_BEFORE_OPEN_PAREN = 3;
/* 235:    */     public static final int SPACE_BEFORE_CLOSE_PAREN = 4;
/* 236:    */     public static final int CR_BEFORE_CLOSE_PAREN = 5;
/* 237:    */     public static final int SPACE_AFTER_EQUALITY = 6;
/* 238:    */   }
/* 239:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.AttrPtg
 * JD-Core Version:    0.7.0.1
 */