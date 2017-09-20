/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.Formula;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*   6:    */ import org.apache.poi.ss.usermodel.CellType;
/*   7:    */ import org.apache.poi.util.BitField;
/*   8:    */ import org.apache.poi.util.BitFieldFactory;
/*   9:    */ import org.apache.poi.util.HexDump;
/*  10:    */ import org.apache.poi.util.LittleEndianOutput;
/*  11:    */ import org.apache.poi.util.RecordFormatException;
/*  12:    */ 
/*  13:    */ public final class FormulaRecord
/*  14:    */   extends CellRecord
/*  15:    */   implements Cloneable
/*  16:    */ {
/*  17:    */   public static final short sid = 6;
/*  18: 35 */   private static int FIXED_SIZE = 14;
/*  19: 37 */   private static final BitField alwaysCalc = BitFieldFactory.getInstance(1);
/*  20: 38 */   private static final BitField calcOnLoad = BitFieldFactory.getInstance(2);
/*  21: 39 */   private static final BitField sharedFormula = BitFieldFactory.getInstance(8);
/*  22:    */   private double field_4_value;
/*  23:    */   private short field_5_options;
/*  24:    */   private int field_6_zero;
/*  25:    */   private Formula field_8_parsed_expr;
/*  26:    */   private SpecialCachedValue specialCachedValue;
/*  27:    */   
/*  28:    */   static final class SpecialCachedValue
/*  29:    */   {
/*  30:    */     private static final long BIT_MARKER = -281474976710656L;
/*  31:    */     private static final int VARIABLE_DATA_LENGTH = 6;
/*  32:    */     private static final int DATA_INDEX = 2;
/*  33:    */     public static final int STRING = 0;
/*  34:    */     public static final int BOOLEAN = 1;
/*  35:    */     public static final int ERROR_CODE = 2;
/*  36:    */     public static final int EMPTY = 3;
/*  37:    */     private final byte[] _variableData;
/*  38:    */     
/*  39:    */     private SpecialCachedValue(byte[] data)
/*  40:    */     {
/*  41: 62 */       this._variableData = data;
/*  42:    */     }
/*  43:    */     
/*  44:    */     public int getTypeCode()
/*  45:    */     {
/*  46: 66 */       return this._variableData[0];
/*  47:    */     }
/*  48:    */     
/*  49:    */     public static SpecialCachedValue create(long valueLongBits)
/*  50:    */     {
/*  51: 74 */       if ((0x0 & valueLongBits) != -281474976710656L) {
/*  52: 75 */         return null;
/*  53:    */       }
/*  54: 78 */       byte[] result = new byte[6];
/*  55: 79 */       long x = valueLongBits;
/*  56: 80 */       for (int i = 0; i < 6; i++)
/*  57:    */       {
/*  58: 81 */         result[i] = ((byte)(int)x);
/*  59: 82 */         x >>= 8;
/*  60:    */       }
/*  61: 84 */       switch (result[0])
/*  62:    */       {
/*  63:    */       case 0: 
/*  64:    */       case 1: 
/*  65:    */       case 2: 
/*  66:    */       case 3: 
/*  67:    */         break;
/*  68:    */       default: 
/*  69: 91 */         throw new RecordFormatException("Bad special value code (" + result[0] + ")");
/*  70:    */       }
/*  71: 93 */       return new SpecialCachedValue(result);
/*  72:    */     }
/*  73:    */     
/*  74:    */     public void serialize(LittleEndianOutput out)
/*  75:    */     {
/*  76: 97 */       out.write(this._variableData);
/*  77: 98 */       out.writeShort(65535);
/*  78:    */     }
/*  79:    */     
/*  80:    */     public String formatDebugString()
/*  81:    */     {
/*  82:102 */       return formatValue() + ' ' + HexDump.toHex(this._variableData);
/*  83:    */     }
/*  84:    */     
/*  85:    */     private String formatValue()
/*  86:    */     {
/*  87:106 */       int typeCode = getTypeCode();
/*  88:107 */       switch (typeCode)
/*  89:    */       {
/*  90:    */       case 0: 
/*  91:109 */         return "<string>";
/*  92:    */       case 1: 
/*  93:111 */         return getDataValue() == 0 ? "FALSE" : "TRUE";
/*  94:    */       case 2: 
/*  95:113 */         return ErrorEval.getText(getDataValue());
/*  96:    */       case 3: 
/*  97:115 */         return "<empty>";
/*  98:    */       }
/*  99:117 */       return "#error(type=" + typeCode + ")#";
/* 100:    */     }
/* 101:    */     
/* 102:    */     private int getDataValue()
/* 103:    */     {
/* 104:121 */       return this._variableData[2];
/* 105:    */     }
/* 106:    */     
/* 107:    */     public static SpecialCachedValue createCachedEmptyValue()
/* 108:    */     {
/* 109:125 */       return create(3, 0);
/* 110:    */     }
/* 111:    */     
/* 112:    */     public static SpecialCachedValue createForString()
/* 113:    */     {
/* 114:129 */       return create(0, 0);
/* 115:    */     }
/* 116:    */     
/* 117:    */     public static SpecialCachedValue createCachedBoolean(boolean b)
/* 118:    */     {
/* 119:133 */       return create(1, b ? 1 : 0);
/* 120:    */     }
/* 121:    */     
/* 122:    */     public static SpecialCachedValue createCachedErrorCode(int errorCode)
/* 123:    */     {
/* 124:137 */       return create(2, errorCode);
/* 125:    */     }
/* 126:    */     
/* 127:    */     private static SpecialCachedValue create(int code, int data)
/* 128:    */     {
/* 129:141 */       byte[] vd = { (byte)code, 0, (byte)data, 0, 0, 0 };
/* 130:    */       
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:149 */       return new SpecialCachedValue(vd);
/* 138:    */     }
/* 139:    */     
/* 140:    */     public String toString()
/* 141:    */     {
/* 142:154 */       return getClass().getName() + '[' + formatValue() + ']';
/* 143:    */     }
/* 144:    */     
/* 145:    */     public int getValueType()
/* 146:    */     {
/* 147:158 */       int typeCode = getTypeCode();
/* 148:159 */       switch (typeCode)
/* 149:    */       {
/* 150:    */       case 0: 
/* 151:160 */         return CellType.STRING.getCode();
/* 152:    */       case 1: 
/* 153:161 */         return CellType.BOOLEAN.getCode();
/* 154:    */       case 2: 
/* 155:162 */         return CellType.ERROR.getCode();
/* 156:    */       case 3: 
/* 157:163 */         return CellType.STRING.getCode();
/* 158:    */       }
/* 159:165 */       throw new IllegalStateException("Unexpected type id (" + typeCode + ")");
/* 160:    */     }
/* 161:    */     
/* 162:    */     public boolean getBooleanValue()
/* 163:    */     {
/* 164:169 */       if (getTypeCode() != 1) {
/* 165:170 */         throw new IllegalStateException("Not a boolean cached value - " + formatValue());
/* 166:    */       }
/* 167:172 */       return getDataValue() != 0;
/* 168:    */     }
/* 169:    */     
/* 170:    */     public int getErrorValue()
/* 171:    */     {
/* 172:176 */       if (getTypeCode() != 2) {
/* 173:177 */         throw new IllegalStateException("Not an error cached value - " + formatValue());
/* 174:    */       }
/* 175:179 */       return getDataValue();
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   public FormulaRecord()
/* 180:    */   {
/* 181:201 */     this.field_8_parsed_expr = Formula.create(Ptg.EMPTY_PTG_ARRAY);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public FormulaRecord(RecordInputStream ris)
/* 185:    */   {
/* 186:205 */     super(ris);
/* 187:206 */     long valueLongBits = ris.readLong();
/* 188:207 */     this.field_5_options = ris.readShort();
/* 189:208 */     this.specialCachedValue = SpecialCachedValue.create(valueLongBits);
/* 190:209 */     if (this.specialCachedValue == null) {
/* 191:210 */       this.field_4_value = Double.longBitsToDouble(valueLongBits);
/* 192:    */     }
/* 193:213 */     this.field_6_zero = ris.readInt();
/* 194:    */     
/* 195:215 */     int field_7_expression_len = ris.readShort();
/* 196:216 */     int nBytesAvailable = ris.available();
/* 197:217 */     this.field_8_parsed_expr = Formula.read(field_7_expression_len, ris, nBytesAvailable);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public void setValue(double value)
/* 201:    */   {
/* 202:226 */     this.field_4_value = value;
/* 203:227 */     this.specialCachedValue = null;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void setCachedResultTypeEmptyString()
/* 207:    */   {
/* 208:231 */     this.specialCachedValue = SpecialCachedValue.createCachedEmptyValue();
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setCachedResultTypeString()
/* 212:    */   {
/* 213:234 */     this.specialCachedValue = SpecialCachedValue.createForString();
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setCachedResultErrorCode(int errorCode)
/* 217:    */   {
/* 218:237 */     this.specialCachedValue = SpecialCachedValue.createCachedErrorCode(errorCode);
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void setCachedResultBoolean(boolean value)
/* 222:    */   {
/* 223:240 */     this.specialCachedValue = SpecialCachedValue.createCachedBoolean(value);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public boolean hasCachedResultString()
/* 227:    */   {
/* 228:248 */     return (this.specialCachedValue != null) && (this.specialCachedValue.getTypeCode() == 0);
/* 229:    */   }
/* 230:    */   
/* 231:    */   public int getCachedResultType()
/* 232:    */   {
/* 233:253 */     if (this.specialCachedValue == null) {
/* 234:254 */       return CellType.NUMERIC.getCode();
/* 235:    */     }
/* 236:256 */     return this.specialCachedValue.getValueType();
/* 237:    */   }
/* 238:    */   
/* 239:    */   public boolean getCachedBooleanValue()
/* 240:    */   {
/* 241:260 */     return this.specialCachedValue.getBooleanValue();
/* 242:    */   }
/* 243:    */   
/* 244:    */   public int getCachedErrorValue()
/* 245:    */   {
/* 246:263 */     return this.specialCachedValue.getErrorValue();
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setOptions(short options)
/* 250:    */   {
/* 251:273 */     this.field_5_options = options;
/* 252:    */   }
/* 253:    */   
/* 254:    */   public double getValue()
/* 255:    */   {
/* 256:282 */     return this.field_4_value;
/* 257:    */   }
/* 258:    */   
/* 259:    */   public short getOptions()
/* 260:    */   {
/* 261:291 */     return this.field_5_options;
/* 262:    */   }
/* 263:    */   
/* 264:    */   public boolean isSharedFormula()
/* 265:    */   {
/* 266:295 */     return sharedFormula.isSet(this.field_5_options);
/* 267:    */   }
/* 268:    */   
/* 269:    */   public void setSharedFormula(boolean flag)
/* 270:    */   {
/* 271:298 */     this.field_5_options = sharedFormula.setShortBoolean(this.field_5_options, flag);
/* 272:    */   }
/* 273:    */   
/* 274:    */   public boolean isAlwaysCalc()
/* 275:    */   {
/* 276:303 */     return alwaysCalc.isSet(this.field_5_options);
/* 277:    */   }
/* 278:    */   
/* 279:    */   public void setAlwaysCalc(boolean flag)
/* 280:    */   {
/* 281:306 */     this.field_5_options = alwaysCalc.setShortBoolean(this.field_5_options, flag);
/* 282:    */   }
/* 283:    */   
/* 284:    */   public boolean isCalcOnLoad()
/* 285:    */   {
/* 286:311 */     return calcOnLoad.isSet(this.field_5_options);
/* 287:    */   }
/* 288:    */   
/* 289:    */   public void setCalcOnLoad(boolean flag)
/* 290:    */   {
/* 291:314 */     this.field_5_options = calcOnLoad.setShortBoolean(this.field_5_options, flag);
/* 292:    */   }
/* 293:    */   
/* 294:    */   public Ptg[] getParsedExpression()
/* 295:    */   {
/* 296:322 */     return this.field_8_parsed_expr.getTokens();
/* 297:    */   }
/* 298:    */   
/* 299:    */   public Formula getFormula()
/* 300:    */   {
/* 301:326 */     return this.field_8_parsed_expr;
/* 302:    */   }
/* 303:    */   
/* 304:    */   public void setParsedExpression(Ptg[] ptgs)
/* 305:    */   {
/* 306:330 */     this.field_8_parsed_expr = Formula.create(ptgs);
/* 307:    */   }
/* 308:    */   
/* 309:    */   public short getSid()
/* 310:    */   {
/* 311:335 */     return 6;
/* 312:    */   }
/* 313:    */   
/* 314:    */   protected int getValueDataSize()
/* 315:    */   {
/* 316:340 */     return FIXED_SIZE + this.field_8_parsed_expr.getEncodedSize();
/* 317:    */   }
/* 318:    */   
/* 319:    */   protected void serializeValue(LittleEndianOutput out)
/* 320:    */   {
/* 321:345 */     if (this.specialCachedValue == null) {
/* 322:346 */       out.writeDouble(this.field_4_value);
/* 323:    */     } else {
/* 324:348 */       this.specialCachedValue.serialize(out);
/* 325:    */     }
/* 326:351 */     out.writeShort(getOptions());
/* 327:    */     
/* 328:353 */     out.writeInt(this.field_6_zero);
/* 329:354 */     this.field_8_parsed_expr.serialize(out);
/* 330:    */   }
/* 331:    */   
/* 332:    */   protected String getRecordName()
/* 333:    */   {
/* 334:359 */     return "FORMULA";
/* 335:    */   }
/* 336:    */   
/* 337:    */   protected void appendValueText(StringBuilder sb)
/* 338:    */   {
/* 339:364 */     sb.append("  .value\t = ");
/* 340:365 */     if (this.specialCachedValue == null) {
/* 341:366 */       sb.append(this.field_4_value).append("\n");
/* 342:    */     } else {
/* 343:368 */       sb.append(this.specialCachedValue.formatDebugString()).append("\n");
/* 344:    */     }
/* 345:370 */     sb.append("  .options   = ").append(HexDump.shortToHex(getOptions())).append("\n");
/* 346:371 */     sb.append("    .alwaysCalc= ").append(isAlwaysCalc()).append("\n");
/* 347:372 */     sb.append("    .calcOnLoad= ").append(isCalcOnLoad()).append("\n");
/* 348:373 */     sb.append("    .shared    = ").append(isSharedFormula()).append("\n");
/* 349:374 */     sb.append("  .zero      = ").append(HexDump.intToHex(this.field_6_zero)).append("\n");
/* 350:    */     
/* 351:376 */     Ptg[] ptgs = this.field_8_parsed_expr.getTokens();
/* 352:377 */     for (int k = 0; k < ptgs.length; k++)
/* 353:    */     {
/* 354:378 */       if (k > 0) {
/* 355:379 */         sb.append("\n");
/* 356:    */       }
/* 357:381 */       sb.append("    Ptg[").append(k).append("]=");
/* 358:382 */       Ptg ptg = ptgs[k];
/* 359:383 */       sb.append(ptg).append(ptg.getRVAType());
/* 360:    */     }
/* 361:    */   }
/* 362:    */   
/* 363:    */   public FormulaRecord clone()
/* 364:    */   {
/* 365:389 */     FormulaRecord rec = new FormulaRecord();
/* 366:390 */     copyBaseFields(rec);
/* 367:391 */     rec.field_4_value = this.field_4_value;
/* 368:392 */     rec.field_5_options = this.field_5_options;
/* 369:393 */     rec.field_6_zero = this.field_6_zero;
/* 370:394 */     rec.field_8_parsed_expr = this.field_8_parsed_expr;
/* 371:395 */     rec.specialCachedValue = this.specialCachedValue;
/* 372:396 */     return rec;
/* 373:    */   }
/* 374:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FormulaRecord
 * JD-Core Version:    0.7.0.1
 */