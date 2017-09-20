/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public abstract class XYNumericFunction
/*  11:    */   extends Fixed2ArgFunction
/*  12:    */ {
/*  13:    */   protected abstract Accumulator createAccumulator();
/*  14:    */   
/*  15:    */   private static abstract class ValueArray
/*  16:    */     implements LookupUtils.ValueVector
/*  17:    */   {
/*  18:    */     private final int _size;
/*  19:    */     
/*  20:    */     protected ValueArray(int size)
/*  21:    */     {
/*  22: 36 */       this._size = size;
/*  23:    */     }
/*  24:    */     
/*  25:    */     public ValueEval getItem(int index)
/*  26:    */     {
/*  27: 39 */       if ((index < 0) || (index > this._size)) {
/*  28: 40 */         throw new IllegalArgumentException("Specified index " + index + " is outside range (0.." + (this._size - 1) + ")");
/*  29:    */       }
/*  30: 43 */       return getItemInternal(index);
/*  31:    */     }
/*  32:    */     
/*  33:    */     protected abstract ValueEval getItemInternal(int paramInt);
/*  34:    */     
/*  35:    */     public final int getSize()
/*  36:    */     {
/*  37: 47 */       return this._size;
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
/*  48: 54 */       super();
/*  49: 55 */       this._value = value;
/*  50:    */     }
/*  51:    */     
/*  52:    */     protected ValueEval getItemInternal(int index)
/*  53:    */     {
/*  54: 58 */       return this._value;
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
/*  66: 67 */       super();
/*  67: 68 */       this._ref = ref;
/*  68: 69 */       this._width = ref.getNumberOfSheets();
/*  69:    */     }
/*  70:    */     
/*  71:    */     protected ValueEval getItemInternal(int index)
/*  72:    */     {
/*  73: 72 */       int sIx = index % this._width + this._ref.getFirstSheetIndex();
/*  74: 73 */       return this._ref.getInnerValueEval(sIx);
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
/*  86: 82 */       super();
/*  87: 83 */       this._ae = ae;
/*  88: 84 */       this._width = ae.getWidth();
/*  89:    */     }
/*  90:    */     
/*  91:    */     protected ValueEval getItemInternal(int index)
/*  92:    */     {
/*  93: 87 */       int rowIx = index / this._width;
/*  94: 88 */       int colIx = index % this._width;
/*  95: 89 */       return this._ae.getValue(rowIx, colIx);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/* 100:    */   {
/* 101:    */     double result;
/* 102:    */     try
/* 103:    */     {
/* 104:106 */       LookupUtils.ValueVector vvX = createValueVector(arg0);
/* 105:107 */       LookupUtils.ValueVector vvY = createValueVector(arg1);
/* 106:108 */       int size = vvX.getSize();
/* 107:109 */       if ((size == 0) || (vvY.getSize() != size)) {
/* 108:110 */         return ErrorEval.NA;
/* 109:    */       }
/* 110:112 */       result = evaluateInternal(vvX, vvY, size);
/* 111:    */     }
/* 112:    */     catch (EvaluationException e)
/* 113:    */     {
/* 114:114 */       return e.getErrorEval();
/* 115:    */     }
/* 116:116 */     if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/* 117:117 */       return ErrorEval.NUM_ERROR;
/* 118:    */     }
/* 119:119 */     return new NumberEval(result);
/* 120:    */   }
/* 121:    */   
/* 122:    */   private double evaluateInternal(LookupUtils.ValueVector x, LookupUtils.ValueVector y, int size)
/* 123:    */     throws EvaluationException
/* 124:    */   {
/* 125:124 */     Accumulator acc = createAccumulator();
/* 126:    */     
/* 127:    */ 
/* 128:127 */     ErrorEval firstXerr = null;
/* 129:128 */     ErrorEval firstYerr = null;
/* 130:129 */     boolean accumlatedSome = false;
/* 131:130 */     double result = 0.0D;
/* 132:132 */     for (int i = 0; i < size; i++)
/* 133:    */     {
/* 134:133 */       ValueEval vx = x.getItem(i);
/* 135:134 */       ValueEval vy = y.getItem(i);
/* 136:135 */       if (((vx instanceof ErrorEval)) && 
/* 137:136 */         (firstXerr == null))
/* 138:    */       {
/* 139:137 */         firstXerr = (ErrorEval)vx;
/* 140:    */       }
/* 141:141 */       else if (((vy instanceof ErrorEval)) && 
/* 142:142 */         (firstYerr == null))
/* 143:    */       {
/* 144:143 */         firstYerr = (ErrorEval)vy;
/* 145:    */       }
/* 146:148 */       else if (((vx instanceof NumberEval)) && ((vy instanceof NumberEval)))
/* 147:    */       {
/* 148:149 */         accumlatedSome = true;
/* 149:150 */         NumberEval nx = (NumberEval)vx;
/* 150:151 */         NumberEval ny = (NumberEval)vy;
/* 151:152 */         result += acc.accumulate(nx.getNumberValue(), ny.getNumberValue());
/* 152:    */       }
/* 153:    */     }
/* 154:157 */     if (firstXerr != null) {
/* 155:158 */       throw new EvaluationException(firstXerr);
/* 156:    */     }
/* 157:160 */     if (firstYerr != null) {
/* 158:161 */       throw new EvaluationException(firstYerr);
/* 159:    */     }
/* 160:163 */     if (!accumlatedSome) {
/* 161:164 */       throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 162:    */     }
/* 163:166 */     return result;
/* 164:    */   }
/* 165:    */   
/* 166:    */   private static LookupUtils.ValueVector createValueVector(ValueEval arg)
/* 167:    */     throws EvaluationException
/* 168:    */   {
/* 169:170 */     if ((arg instanceof ErrorEval)) {
/* 170:171 */       throw new EvaluationException((ErrorEval)arg);
/* 171:    */     }
/* 172:173 */     if ((arg instanceof TwoDEval)) {
/* 173:174 */       return new AreaValueArray((TwoDEval)arg);
/* 174:    */     }
/* 175:176 */     if ((arg instanceof RefEval)) {
/* 176:177 */       return new RefValueArray((RefEval)arg);
/* 177:    */     }
/* 178:179 */     return new SingleCellValueArray(arg);
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected static abstract interface Accumulator
/* 182:    */   {
/* 183:    */     public abstract double accumulate(double paramDouble1, double paramDouble2);
/* 184:    */   }
/* 185:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.XYNumericFunction

 * JD-Core Version:    0.7.0.1

 */