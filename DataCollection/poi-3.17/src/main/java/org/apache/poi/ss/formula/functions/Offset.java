/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   8:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  10:    */ 
/*  11:    */ public final class Offset
/*  12:    */   implements Function
/*  13:    */ {
/*  14:    */   private static final int LAST_VALID_ROW_INDEX = 65535;
/*  15:    */   private static final int LAST_VALID_COLUMN_INDEX = 255;
/*  16:    */   
/*  17:    */   static final class LinearOffsetRange
/*  18:    */   {
/*  19:    */     private final int _offset;
/*  20:    */     private final int _length;
/*  21:    */     
/*  22:    */     public LinearOffsetRange(int offset, int length)
/*  23:    */     {
/*  24: 59 */       if (length == 0) {
/*  25: 61 */         throw new RuntimeException("length may not be zero");
/*  26:    */       }
/*  27: 63 */       this._offset = offset;
/*  28: 64 */       this._length = length;
/*  29:    */     }
/*  30:    */     
/*  31:    */     public short getFirstIndex()
/*  32:    */     {
/*  33: 68 */       return (short)this._offset;
/*  34:    */     }
/*  35:    */     
/*  36:    */     public short getLastIndex()
/*  37:    */     {
/*  38: 71 */       return (short)(this._offset + this._length - 1);
/*  39:    */     }
/*  40:    */     
/*  41:    */     public LinearOffsetRange normaliseAndTranslate(int translationAmount)
/*  42:    */     {
/*  43: 86 */       if (this._length > 0)
/*  44:    */       {
/*  45: 87 */         if (translationAmount == 0) {
/*  46: 88 */           return this;
/*  47:    */         }
/*  48: 90 */         return new LinearOffsetRange(translationAmount + this._offset, this._length);
/*  49:    */       }
/*  50: 92 */       return new LinearOffsetRange(translationAmount + this._offset + this._length + 1, -this._length);
/*  51:    */     }
/*  52:    */     
/*  53:    */     public boolean isOutOfBounds(int lowValidIx, int highValidIx)
/*  54:    */     {
/*  55: 96 */       if (this._offset < lowValidIx) {
/*  56: 97 */         return true;
/*  57:    */       }
/*  58: 99 */       if (getLastIndex() > highValidIx) {
/*  59:100 */         return true;
/*  60:    */       }
/*  61:102 */       return false;
/*  62:    */     }
/*  63:    */     
/*  64:    */     public String toString()
/*  65:    */     {
/*  66:105 */       StringBuffer sb = new StringBuffer(64);
/*  67:106 */       sb.append(getClass().getName()).append(" [");
/*  68:107 */       sb.append(this._offset).append("...").append(getLastIndex());
/*  69:108 */       sb.append("]");
/*  70:109 */       return sb.toString();
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   private static final class BaseRef
/*  75:    */   {
/*  76:    */     private final int _firstRowIndex;
/*  77:    */     private final int _firstColumnIndex;
/*  78:    */     private final int _width;
/*  79:    */     private final int _height;
/*  80:    */     private final RefEval _refEval;
/*  81:    */     private final AreaEval _areaEval;
/*  82:    */     
/*  83:    */     public BaseRef(RefEval re)
/*  84:    */     {
/*  85:125 */       this._refEval = re;
/*  86:126 */       this._areaEval = null;
/*  87:127 */       this._firstRowIndex = re.getRow();
/*  88:128 */       this._firstColumnIndex = re.getColumn();
/*  89:129 */       this._height = 1;
/*  90:130 */       this._width = 1;
/*  91:    */     }
/*  92:    */     
/*  93:    */     public BaseRef(AreaEval ae)
/*  94:    */     {
/*  95:134 */       this._refEval = null;
/*  96:135 */       this._areaEval = ae;
/*  97:136 */       this._firstRowIndex = ae.getFirstRow();
/*  98:137 */       this._firstColumnIndex = ae.getFirstColumn();
/*  99:138 */       this._height = (ae.getLastRow() - ae.getFirstRow() + 1);
/* 100:139 */       this._width = (ae.getLastColumn() - ae.getFirstColumn() + 1);
/* 101:    */     }
/* 102:    */     
/* 103:    */     public int getWidth()
/* 104:    */     {
/* 105:143 */       return this._width;
/* 106:    */     }
/* 107:    */     
/* 108:    */     public int getHeight()
/* 109:    */     {
/* 110:146 */       return this._height;
/* 111:    */     }
/* 112:    */     
/* 113:    */     public int getFirstRowIndex()
/* 114:    */     {
/* 115:149 */       return this._firstRowIndex;
/* 116:    */     }
/* 117:    */     
/* 118:    */     public int getFirstColumnIndex()
/* 119:    */     {
/* 120:152 */       return this._firstColumnIndex;
/* 121:    */     }
/* 122:    */     
/* 123:    */     public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx)
/* 124:    */     {
/* 125:157 */       if (this._refEval == null) {
/* 126:158 */         return this._areaEval.offset(relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
/* 127:    */       }
/* 128:160 */       return this._refEval.offset(relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/* 133:    */   {
/* 134:166 */     if ((args.length < 3) || (args.length > 5)) {
/* 135:167 */       return ErrorEval.VALUE_INVALID;
/* 136:    */     }
/* 137:    */     try
/* 138:    */     {
/* 139:171 */       BaseRef baseRef = evaluateBaseRef(args[0]);
/* 140:172 */       int rowOffset = evaluateIntArg(args[1], srcCellRow, srcCellCol);
/* 141:173 */       int columnOffset = evaluateIntArg(args[2], srcCellRow, srcCellCol);
/* 142:174 */       int height = baseRef.getHeight();
/* 143:175 */       int width = baseRef.getWidth();
/* 144:178 */       switch (args.length)
/* 145:    */       {
/* 146:    */       case 5: 
/* 147:180 */         if (!(args[4] instanceof MissingArgEval)) {
/* 148:181 */           width = evaluateIntArg(args[4], srcCellRow, srcCellCol);
/* 149:    */         }
/* 150:    */       case 4: 
/* 151:185 */         if (!(args[3] instanceof MissingArgEval)) {
/* 152:186 */           height = evaluateIntArg(args[3], srcCellRow, srcCellCol);
/* 153:    */         }
/* 154:    */         break;
/* 155:    */       }
/* 156:195 */       if ((height == 0) || (width == 0)) {
/* 157:196 */         return ErrorEval.REF_INVALID;
/* 158:    */       }
/* 159:198 */       LinearOffsetRange rowOffsetRange = new LinearOffsetRange(rowOffset, height);
/* 160:199 */       LinearOffsetRange colOffsetRange = new LinearOffsetRange(columnOffset, width);
/* 161:200 */       return createOffset(baseRef, rowOffsetRange, colOffsetRange);
/* 162:    */     }
/* 163:    */     catch (EvaluationException e)
/* 164:    */     {
/* 165:202 */       return e.getErrorEval();
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   private static AreaEval createOffset(BaseRef baseRef, LinearOffsetRange orRow, LinearOffsetRange orCol)
/* 170:    */     throws EvaluationException
/* 171:    */   {
/* 172:208 */     LinearOffsetRange absRows = orRow.normaliseAndTranslate(baseRef.getFirstRowIndex());
/* 173:209 */     LinearOffsetRange absCols = orCol.normaliseAndTranslate(baseRef.getFirstColumnIndex());
/* 174:211 */     if (absRows.isOutOfBounds(0, 65535)) {
/* 175:212 */       throw new EvaluationException(ErrorEval.REF_INVALID);
/* 176:    */     }
/* 177:214 */     if (absCols.isOutOfBounds(0, 255)) {
/* 178:215 */       throw new EvaluationException(ErrorEval.REF_INVALID);
/* 179:    */     }
/* 180:217 */     return baseRef.offset(orRow.getFirstIndex(), orRow.getLastIndex(), orCol.getFirstIndex(), orCol.getLastIndex());
/* 181:    */   }
/* 182:    */   
/* 183:    */   private static BaseRef evaluateBaseRef(ValueEval eval)
/* 184:    */     throws EvaluationException
/* 185:    */   {
/* 186:222 */     if ((eval instanceof RefEval)) {
/* 187:223 */       return new BaseRef((RefEval)eval);
/* 188:    */     }
/* 189:225 */     if ((eval instanceof AreaEval)) {
/* 190:226 */       return new BaseRef((AreaEval)eval);
/* 191:    */     }
/* 192:228 */     if ((eval instanceof ErrorEval)) {
/* 193:229 */       throw new EvaluationException((ErrorEval)eval);
/* 194:    */     }
/* 195:231 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 196:    */   }
/* 197:    */   
/* 198:    */   static int evaluateIntArg(ValueEval eval, int srcCellRow, int srcCellCol)
/* 199:    */     throws EvaluationException
/* 200:    */   {
/* 201:238 */     ValueEval ve = OperandResolver.getSingleValue(eval, srcCellRow, srcCellCol);
/* 202:239 */     return OperandResolver.coerceValueToInt(ve);
/* 203:    */   }
/* 204:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Offset
 * JD-Core Version:    0.7.0.1
 */