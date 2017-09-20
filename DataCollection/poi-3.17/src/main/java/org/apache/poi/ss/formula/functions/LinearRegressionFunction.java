/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public final class LinearRegressionFunction
/*  11:    */   extends Fixed2ArgFunction
/*  12:    */ {
/*  13:    */   public FUNCTION function;
/*  14:    */   
/*  15:    */   private static abstract class ValueArray
/*  16:    */     implements LookupUtils.ValueVector
/*  17:    */   {
/*  18:    */     private final int _size;
/*  19:    */     
/*  20:    */     protected ValueArray(int size)
/*  21:    */     {
/*  22: 48 */       this._size = size;
/*  23:    */     }
/*  24:    */     
/*  25:    */     public ValueEval getItem(int index)
/*  26:    */     {
/*  27: 52 */       if ((index < 0) || (index > this._size)) {
/*  28: 53 */         throw new IllegalArgumentException("Specified index " + index + " is outside range (0.." + (this._size - 1) + ")");
/*  29:    */       }
/*  30: 56 */       return getItemInternal(index);
/*  31:    */     }
/*  32:    */     
/*  33:    */     protected abstract ValueEval getItemInternal(int paramInt);
/*  34:    */     
/*  35:    */     public final int getSize()
/*  36:    */     {
/*  37: 61 */       return this._size;
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   private static final class SingleCellValueArray
/*  42:    */     extends ValueArray
/*  43:    */   {
/*  44:    */     private final ValueEval _value;
/*  45:    */     
/*  46:    */     public SingleCellValueArray(ValueEval value)
/*  47:    */     {
/*  48: 68 */       super();
/*  49: 69 */       this._value = value;
/*  50:    */     }
/*  51:    */     
/*  52:    */     protected ValueEval getItemInternal(int index)
/*  53:    */     {
/*  54: 73 */       return this._value;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static final class RefValueArray
/*  59:    */     extends ValueArray
/*  60:    */   {
/*  61:    */     private final RefEval _ref;
/*  62:    */     private final int _width;
/*  63:    */     
/*  64:    */     public RefValueArray(RefEval ref)
/*  65:    */     {
/*  66: 81 */       super();
/*  67: 82 */       this._ref = ref;
/*  68: 83 */       this._width = ref.getNumberOfSheets();
/*  69:    */     }
/*  70:    */     
/*  71:    */     protected ValueEval getItemInternal(int index)
/*  72:    */     {
/*  73: 87 */       int sIx = index % this._width + this._ref.getFirstSheetIndex();
/*  74: 88 */       return this._ref.getInnerValueEval(sIx);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   private static final class AreaValueArray
/*  79:    */     extends ValueArray
/*  80:    */   {
/*  81:    */     private final TwoDEval _ae;
/*  82:    */     private final int _width;
/*  83:    */     
/*  84:    */     public AreaValueArray(TwoDEval ae)
/*  85:    */     {
/*  86: 97 */       super();
/*  87: 98 */       this._ae = ae;
/*  88: 99 */       this._width = ae.getWidth();
/*  89:    */     }
/*  90:    */     
/*  91:    */     protected ValueEval getItemInternal(int index)
/*  92:    */     {
/*  93:103 */       int rowIx = index / this._width;
/*  94:104 */       int colIx = index % this._width;
/*  95:105 */       return this._ae.getValue(rowIx, colIx);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static enum FUNCTION
/* 100:    */   {
/* 101:109 */     INTERCEPT,  SLOPE;
/* 102:    */     
/* 103:    */     private FUNCTION() {}
/* 104:    */   }
/* 105:    */   
/* 106:    */   public LinearRegressionFunction(FUNCTION function)
/* 107:    */   {
/* 108:113 */     this.function = function;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 112:    */   {
/* 113:    */     double result;
/* 114:    */     try
/* 115:    */     {
/* 116:120 */       LookupUtils.ValueVector vvY = createValueVector(arg0);
/* 117:121 */       LookupUtils.ValueVector vvX = createValueVector(arg1);
/* 118:122 */       int size = vvX.getSize();
/* 119:123 */       if ((size == 0) || (vvY.getSize() != size)) {
/* 120:124 */         return ErrorEval.NA;
/* 121:    */       }
/* 122:126 */       result = evaluateInternal(vvX, vvY, size);
/* 123:    */     }
/* 124:    */     catch (EvaluationException e)
/* 125:    */     {
/* 126:128 */       return e.getErrorEval();
/* 127:    */     }
/* 128:130 */     if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/* 129:131 */       return ErrorEval.NUM_ERROR;
/* 130:    */     }
/* 131:133 */     return new NumberEval(result);
/* 132:    */   }
/* 133:    */   
/* 134:    */   private double evaluateInternal(LookupUtils.ValueVector x, LookupUtils.ValueVector y, int size)
/* 135:    */     throws EvaluationException
/* 136:    */   {
/* 137:140 */     ErrorEval firstXerr = null;
/* 138:141 */     ErrorEval firstYerr = null;
/* 139:142 */     boolean accumlatedSome = false;
/* 140:    */     
/* 141:144 */     double sumx = 0.0D;double sumy = 0.0D;
/* 142:146 */     for (int i = 0; i < size; i++)
/* 143:    */     {
/* 144:147 */       ValueEval vx = x.getItem(i);
/* 145:148 */       ValueEval vy = y.getItem(i);
/* 146:149 */       if (((vx instanceof ErrorEval)) && 
/* 147:150 */         (firstXerr == null))
/* 148:    */       {
/* 149:151 */         firstXerr = (ErrorEval)vx;
/* 150:    */       }
/* 151:155 */       else if (((vy instanceof ErrorEval)) && 
/* 152:156 */         (firstYerr == null))
/* 153:    */       {
/* 154:157 */         firstYerr = (ErrorEval)vy;
/* 155:    */       }
/* 156:162 */       else if (((vx instanceof NumberEval)) && ((vy instanceof NumberEval)))
/* 157:    */       {
/* 158:163 */         accumlatedSome = true;
/* 159:164 */         NumberEval nx = (NumberEval)vx;
/* 160:165 */         NumberEval ny = (NumberEval)vy;
/* 161:166 */         sumx += nx.getNumberValue();
/* 162:167 */         sumy += ny.getNumberValue();
/* 163:    */       }
/* 164:    */     }
/* 165:172 */     double xbar = sumx / size;
/* 166:173 */     double ybar = sumy / size;
/* 167:    */     
/* 168:    */ 
/* 169:176 */     double xxbar = 0.0D;double xybar = 0.0D;
/* 170:177 */     for (int i = 0; i < size; i++)
/* 171:    */     {
/* 172:178 */       ValueEval vx = x.getItem(i);
/* 173:179 */       ValueEval vy = y.getItem(i);
/* 174:181 */       if (((vx instanceof ErrorEval)) && 
/* 175:182 */         (firstXerr == null))
/* 176:    */       {
/* 177:183 */         firstXerr = (ErrorEval)vx;
/* 178:    */       }
/* 179:187 */       else if (((vy instanceof ErrorEval)) && 
/* 180:188 */         (firstYerr == null))
/* 181:    */       {
/* 182:189 */         firstYerr = (ErrorEval)vy;
/* 183:    */       }
/* 184:195 */       else if (((vx instanceof NumberEval)) && ((vy instanceof NumberEval)))
/* 185:    */       {
/* 186:196 */         NumberEval nx = (NumberEval)vx;
/* 187:197 */         NumberEval ny = (NumberEval)vy;
/* 188:198 */         xxbar += (nx.getNumberValue() - xbar) * (nx.getNumberValue() - xbar);
/* 189:199 */         xybar += (nx.getNumberValue() - xbar) * (ny.getNumberValue() - ybar);
/* 190:    */       }
/* 191:    */     }
/* 192:204 */     double beta1 = xybar / xxbar;
/* 193:205 */     double beta0 = ybar - beta1 * xbar;
/* 194:207 */     if (firstXerr != null) {
/* 195:208 */       throw new EvaluationException(firstXerr);
/* 196:    */     }
/* 197:210 */     if (firstYerr != null) {
/* 198:211 */       throw new EvaluationException(firstYerr);
/* 199:    */     }
/* 200:213 */     if (!accumlatedSome) {
/* 201:214 */       throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 202:    */     }
/* 203:217 */     if (this.function == FUNCTION.INTERCEPT) {
/* 204:218 */       return beta0;
/* 205:    */     }
/* 206:220 */     return beta1;
/* 207:    */   }
/* 208:    */   
/* 209:    */   private static LookupUtils.ValueVector createValueVector(ValueEval arg)
/* 210:    */     throws EvaluationException
/* 211:    */   {
/* 212:225 */     if ((arg instanceof ErrorEval)) {
/* 213:226 */       throw new EvaluationException((ErrorEval)arg);
/* 214:    */     }
/* 215:228 */     if ((arg instanceof TwoDEval)) {
/* 216:229 */       return new AreaValueArray((TwoDEval)arg);
/* 217:    */     }
/* 218:231 */     if ((arg instanceof RefEval)) {
/* 219:232 */       return new RefValueArray((RefEval)arg);
/* 220:    */     }
/* 221:234 */     return new SingleCellValueArray(arg);
/* 222:    */   }
/* 223:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.LinearRegressionFunction

 * JD-Core Version:    0.7.0.1

 */