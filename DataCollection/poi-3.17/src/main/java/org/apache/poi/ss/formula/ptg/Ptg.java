/*   1:    */ package org.apache.poi.ss.formula.ptg;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public abstract class Ptg
/*  10:    */ {
/*  11: 40 */   public static final Ptg[] EMPTY_PTG_ARRAY = new Ptg[0];
/*  12:    */   public static final byte CLASS_REF = 0;
/*  13:    */   public static final byte CLASS_VALUE = 32;
/*  14:    */   public static final byte CLASS_ARRAY = 64;
/*  15:    */   
/*  16:    */   public static Ptg[] readTokens(int size, LittleEndianInput in)
/*  17:    */   {
/*  18: 47 */     List<Ptg> temp = new ArrayList(4 + size / 2);
/*  19: 48 */     int pos = 0;
/*  20: 49 */     boolean hasArrayPtgs = false;
/*  21: 50 */     while (pos < size)
/*  22:    */     {
/*  23: 51 */       Ptg ptg = createPtg(in);
/*  24: 52 */       if ((ptg instanceof ArrayPtg.Initial)) {
/*  25: 53 */         hasArrayPtgs = true;
/*  26:    */       }
/*  27: 55 */       pos += ptg.getSize();
/*  28: 56 */       temp.add(ptg);
/*  29:    */     }
/*  30: 58 */     if (pos != size) {
/*  31: 59 */       throw new RuntimeException("Ptg array size mismatch");
/*  32:    */     }
/*  33: 61 */     if (hasArrayPtgs)
/*  34:    */     {
/*  35: 62 */       Ptg[] result = toPtgArray(temp);
/*  36: 63 */       for (int i = 0; i < result.length; i++) {
/*  37: 64 */         if ((result[i] instanceof ArrayPtg.Initial)) {
/*  38: 65 */           result[i] = ((ArrayPtg.Initial)result[i]).finishReading(in);
/*  39:    */         }
/*  40:    */       }
/*  41: 68 */       return result;
/*  42:    */     }
/*  43: 70 */     return toPtgArray(temp);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static Ptg createPtg(LittleEndianInput in)
/*  47:    */   {
/*  48: 74 */     byte id = in.readByte();
/*  49: 76 */     if (id < 32) {
/*  50: 77 */       return createBasePtg(id, in);
/*  51:    */     }
/*  52: 80 */     Ptg retval = createClassifiedPtg(id, in);
/*  53: 82 */     if (id >= 96) {
/*  54: 83 */       retval.setClass((byte)64);
/*  55: 84 */     } else if (id >= 64) {
/*  56: 85 */       retval.setClass((byte)32);
/*  57:    */     } else {
/*  58: 87 */       retval.setClass((byte)0);
/*  59:    */     }
/*  60: 89 */     return retval;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private static Ptg createClassifiedPtg(byte id, LittleEndianInput in)
/*  64:    */   {
/*  65: 94 */     int baseId = id & 0x1F | 0x20;
/*  66: 96 */     switch (baseId)
/*  67:    */     {
/*  68:    */     case 32: 
/*  69: 97 */       return new ArrayPtg.Initial(in);
/*  70:    */     case 33: 
/*  71: 98 */       return FuncPtg.create(in);
/*  72:    */     case 34: 
/*  73: 99 */       return FuncVarPtg.create(in);
/*  74:    */     case 35: 
/*  75:100 */       return new NamePtg(in);
/*  76:    */     case 36: 
/*  77:101 */       return new RefPtg(in);
/*  78:    */     case 37: 
/*  79:102 */       return new AreaPtg(in);
/*  80:    */     case 38: 
/*  81:103 */       return new MemAreaPtg(in);
/*  82:    */     case 39: 
/*  83:104 */       return new MemErrPtg(in);
/*  84:    */     case 41: 
/*  85:105 */       return new MemFuncPtg(in);
/*  86:    */     case 42: 
/*  87:106 */       return new RefErrorPtg(in);
/*  88:    */     case 43: 
/*  89:107 */       return new AreaErrPtg(in);
/*  90:    */     case 44: 
/*  91:108 */       return new RefNPtg(in);
/*  92:    */     case 45: 
/*  93:109 */       return new AreaNPtg(in);
/*  94:    */     case 57: 
/*  95:111 */       return new NameXPtg(in);
/*  96:    */     case 58: 
/*  97:112 */       return new Ref3DPtg(in);
/*  98:    */     case 59: 
/*  99:113 */       return new Area3DPtg(in);
/* 100:    */     case 60: 
/* 101:114 */       return new DeletedRef3DPtg(in);
/* 102:    */     case 61: 
/* 103:115 */       return new DeletedArea3DPtg(in);
/* 104:    */     }
/* 105:117 */     throw new UnsupportedOperationException(" Unknown Ptg in Formula: 0x" + Integer.toHexString(id) + " (" + id + ")");
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static Ptg createBasePtg(byte id, LittleEndianInput in)
/* 109:    */   {
/* 110:122 */     switch (id)
/* 111:    */     {
/* 112:    */     case 0: 
/* 113:123 */       return new UnknownPtg(id);
/* 114:    */     case 1: 
/* 115:124 */       return new ExpPtg(in);
/* 116:    */     case 2: 
/* 117:125 */       return new TblPtg(in);
/* 118:    */     case 3: 
/* 119:126 */       return AddPtg.instance;
/* 120:    */     case 4: 
/* 121:127 */       return SubtractPtg.instance;
/* 122:    */     case 5: 
/* 123:128 */       return MultiplyPtg.instance;
/* 124:    */     case 6: 
/* 125:129 */       return DividePtg.instance;
/* 126:    */     case 7: 
/* 127:130 */       return PowerPtg.instance;
/* 128:    */     case 8: 
/* 129:131 */       return ConcatPtg.instance;
/* 130:    */     case 9: 
/* 131:132 */       return LessThanPtg.instance;
/* 132:    */     case 10: 
/* 133:133 */       return LessEqualPtg.instance;
/* 134:    */     case 11: 
/* 135:134 */       return EqualPtg.instance;
/* 136:    */     case 12: 
/* 137:135 */       return GreaterEqualPtg.instance;
/* 138:    */     case 13: 
/* 139:136 */       return GreaterThanPtg.instance;
/* 140:    */     case 14: 
/* 141:137 */       return NotEqualPtg.instance;
/* 142:    */     case 15: 
/* 143:138 */       return IntersectionPtg.instance;
/* 144:    */     case 16: 
/* 145:139 */       return UnionPtg.instance;
/* 146:    */     case 17: 
/* 147:140 */       return RangePtg.instance;
/* 148:    */     case 18: 
/* 149:141 */       return UnaryPlusPtg.instance;
/* 150:    */     case 19: 
/* 151:142 */       return UnaryMinusPtg.instance;
/* 152:    */     case 20: 
/* 153:143 */       return PercentPtg.instance;
/* 154:    */     case 21: 
/* 155:144 */       return ParenthesisPtg.instance;
/* 156:    */     case 22: 
/* 157:145 */       return MissingArgPtg.instance;
/* 158:    */     case 23: 
/* 159:147 */       return new StringPtg(in);
/* 160:    */     case 25: 
/* 161:148 */       return new AttrPtg(in);
/* 162:    */     case 28: 
/* 163:149 */       return ErrPtg.read(in);
/* 164:    */     case 29: 
/* 165:150 */       return BoolPtg.read(in);
/* 166:    */     case 30: 
/* 167:151 */       return new IntPtg(in);
/* 168:    */     case 31: 
/* 169:152 */       return new NumberPtg(in);
/* 170:    */     }
/* 171:154 */     throw new RuntimeException("Unexpected base token id (" + id + ")");
/* 172:    */   }
/* 173:    */   
/* 174:    */   private static Ptg[] toPtgArray(List<Ptg> l)
/* 175:    */   {
/* 176:158 */     if (l.isEmpty()) {
/* 177:159 */       return EMPTY_PTG_ARRAY;
/* 178:    */     }
/* 179:161 */     Ptg[] result = new Ptg[l.size()];
/* 180:162 */     l.toArray(result);
/* 181:163 */     return result;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public static int getEncodedSize(Ptg[] ptgs)
/* 185:    */   {
/* 186:171 */     int result = 0;
/* 187:172 */     for (Ptg ptg : ptgs) {
/* 188:173 */       result += ptg.getSize();
/* 189:    */     }
/* 190:175 */     return result;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public static int getEncodedSizeWithoutArrayData(Ptg[] ptgs)
/* 194:    */   {
/* 195:182 */     int result = 0;
/* 196:183 */     for (Ptg ptg : ptgs) {
/* 197:184 */       if ((ptg instanceof ArrayPtg)) {
/* 198:185 */         result += 8;
/* 199:    */       } else {
/* 200:187 */         result += ptg.getSize();
/* 201:    */       }
/* 202:    */     }
/* 203:190 */     return result;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public static int serializePtgs(Ptg[] ptgs, byte[] array, int offset)
/* 207:    */   {
/* 208:200 */     LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(array, offset);
/* 209:    */     
/* 210:202 */     List<Ptg> arrayPtgs = null;
/* 211:204 */     for (Ptg ptg : ptgs)
/* 212:    */     {
/* 213:205 */       ptg.write(out);
/* 214:206 */       if ((ptg instanceof ArrayPtg))
/* 215:    */       {
/* 216:207 */         if (arrayPtgs == null) {
/* 217:208 */           arrayPtgs = new ArrayList(5);
/* 218:    */         }
/* 219:210 */         arrayPtgs.add(ptg);
/* 220:    */       }
/* 221:    */     }
/* 222:213 */     if (arrayPtgs != null) {
/* 223:214 */       for (Ptg arrayPtg : arrayPtgs)
/* 224:    */       {
/* 225:215 */         ArrayPtg p = (ArrayPtg)arrayPtg;
/* 226:216 */         p.writeTokenValueBytes(out);
/* 227:    */       }
/* 228:    */     }
/* 229:219 */     return out.getWriteIndex() - offset;
/* 230:    */   }
/* 231:    */   
/* 232:    */   public abstract int getSize();
/* 233:    */   
/* 234:    */   public abstract void write(LittleEndianOutput paramLittleEndianOutput);
/* 235:    */   
/* 236:    */   public abstract String toFormulaString();
/* 237:    */   
/* 238:    */   public String toString()
/* 239:    */   {
/* 240:240 */     return getClass().toString();
/* 241:    */   }
/* 242:    */   
/* 243:247 */   private byte ptgClass = 0;
/* 244:    */   
/* 245:    */   public final void setClass(byte thePtgClass)
/* 246:    */   {
/* 247:250 */     if (isBaseToken()) {
/* 248:251 */       throw new RuntimeException("setClass should not be called on a base token");
/* 249:    */     }
/* 250:253 */     this.ptgClass = thePtgClass;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public final byte getPtgClass()
/* 254:    */   {
/* 255:260 */     return this.ptgClass;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public final char getRVAType()
/* 259:    */   {
/* 260:268 */     if (isBaseToken()) {
/* 261:269 */       return '.';
/* 262:    */     }
/* 263:271 */     switch (this.ptgClass)
/* 264:    */     {
/* 265:    */     case 0: 
/* 266:272 */       return 'R';
/* 267:    */     case 32: 
/* 268:273 */       return 'V';
/* 269:    */     case 64: 
/* 270:274 */       return 'A';
/* 271:    */     }
/* 272:276 */     throw new RuntimeException("Unknown operand class (" + this.ptgClass + ")");
/* 273:    */   }
/* 274:    */   
/* 275:    */   public abstract byte getDefaultOperandClass();
/* 276:    */   
/* 277:    */   public abstract boolean isBaseToken();
/* 278:    */   
/* 279:    */   public static boolean doesFormulaReferToDeletedCell(Ptg[] ptgs)
/* 280:    */   {
/* 281:287 */     for (Ptg ptg : ptgs) {
/* 282:288 */       if (isDeletedCellRef(ptg)) {
/* 283:289 */         return true;
/* 284:    */       }
/* 285:    */     }
/* 286:292 */     return false;
/* 287:    */   }
/* 288:    */   
/* 289:    */   private static boolean isDeletedCellRef(Ptg ptg)
/* 290:    */   {
/* 291:296 */     if (ptg == ErrPtg.REF_INVALID) {
/* 292:297 */       return true;
/* 293:    */     }
/* 294:299 */     if ((ptg instanceof DeletedArea3DPtg)) {
/* 295:300 */       return true;
/* 296:    */     }
/* 297:302 */     if ((ptg instanceof DeletedRef3DPtg)) {
/* 298:303 */       return true;
/* 299:    */     }
/* 300:305 */     if ((ptg instanceof AreaErrPtg)) {
/* 301:306 */       return true;
/* 302:    */     }
/* 303:308 */     if ((ptg instanceof RefErrorPtg)) {
/* 304:309 */       return true;
/* 305:    */     }
/* 306:311 */     return false;
/* 307:    */   }
/* 308:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.ptg.Ptg
 * JD-Core Version:    0.7.0.1
 */