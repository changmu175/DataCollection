/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.SpreadsheetVersion;
/*   4:    */ import org.apache.poi.ss.formula.ThreeDEval;
/*   5:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  10:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  13:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.StringValueEval;
/*  15:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  16:    */ 
/*  17:    */ public abstract class MultiOperandNumericFunction
/*  18:    */   implements Function
/*  19:    */ {
/*  20:    */   private final boolean _isReferenceBoolCounted;
/*  21:    */   private final boolean _isBlankCounted;
/*  22:    */   
/*  23:    */   protected MultiOperandNumericFunction(boolean isReferenceBoolCounted, boolean isBlankCounted)
/*  24:    */   {
/*  25: 45 */     this._isReferenceBoolCounted = isReferenceBoolCounted;
/*  26: 46 */     this._isBlankCounted = isBlankCounted;
/*  27:    */   }
/*  28:    */   
/*  29: 49 */   static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
/*  30:    */   
/*  31:    */   private static class DoubleList
/*  32:    */   {
/*  33:    */     private double[] _array;
/*  34:    */     private int _count;
/*  35:    */     
/*  36:    */     public DoubleList()
/*  37:    */     {
/*  38: 56 */       this._array = new double[8];
/*  39: 57 */       this._count = 0;
/*  40:    */     }
/*  41:    */     
/*  42:    */     public double[] toArray()
/*  43:    */     {
/*  44: 61 */       if (this._count < 1) {
/*  45: 62 */         return MultiOperandNumericFunction.EMPTY_DOUBLE_ARRAY;
/*  46:    */       }
/*  47: 64 */       double[] result = new double[this._count];
/*  48: 65 */       System.arraycopy(this._array, 0, result, 0, this._count);
/*  49: 66 */       return result;
/*  50:    */     }
/*  51:    */     
/*  52:    */     private void ensureCapacity(int reqSize)
/*  53:    */     {
/*  54: 70 */       if (reqSize > this._array.length)
/*  55:    */       {
/*  56: 71 */         int newSize = reqSize * 3 / 2;
/*  57: 72 */         double[] newArr = new double[newSize];
/*  58: 73 */         System.arraycopy(this._array, 0, newArr, 0, this._count);
/*  59: 74 */         this._array = newArr;
/*  60:    */       }
/*  61:    */     }
/*  62:    */     
/*  63:    */     public void add(double value)
/*  64:    */     {
/*  65: 79 */       ensureCapacity(this._count + 1);
/*  66: 80 */       this._array[this._count] = value;
/*  67: 81 */       this._count += 1;
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71: 85 */   private static final int DEFAULT_MAX_NUM_OPERANDS = SpreadsheetVersion.EXCEL2007.getMaxFunctionArgs();
/*  72:    */   
/*  73:    */   public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  74:    */   {
/*  75:    */     double d;
/*  76:    */     try
/*  77:    */     {
/*  78: 91 */       double[] values = getNumberArray(args);
/*  79: 92 */       d = evaluate(values);
/*  80:    */     }
/*  81:    */     catch (EvaluationException e)
/*  82:    */     {
/*  83: 94 */       return e.getErrorEval();
/*  84:    */     }
/*  85: 97 */     if ((Double.isNaN(d)) || (Double.isInfinite(d))) {
/*  86: 98 */       return ErrorEval.NUM_ERROR;
/*  87:    */     }
/*  88:100 */     return new NumberEval(d);
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected abstract double evaluate(double[] paramArrayOfDouble)
/*  92:    */     throws EvaluationException;
/*  93:    */   
/*  94:    */   protected int getMaxNumOperands()
/*  95:    */   {
/*  96:110 */     return DEFAULT_MAX_NUM_OPERANDS;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected final double[] getNumberArray(ValueEval[] operands)
/* 100:    */     throws EvaluationException
/* 101:    */   {
/* 102:123 */     if (operands.length > getMaxNumOperands()) {
/* 103:124 */       throw EvaluationException.invalidValue();
/* 104:    */     }
/* 105:126 */     DoubleList retval = new DoubleList();
/* 106:    */     
/* 107:128 */     int i = 0;
/* 108:128 */     for (int iSize = operands.length; i < iSize; i++) {
/* 109:129 */       collectValues(operands[i], retval);
/* 110:    */     }
/* 111:131 */     return retval.toArray();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isSubtotalCounted()
/* 115:    */   {
/* 116:138 */     return true;
/* 117:    */   }
/* 118:    */   
/* 119:    */   private void collectValues(ValueEval operand, DoubleList temp)
/* 120:    */     throws EvaluationException
/* 121:    */   {
/* 122:145 */     if ((operand instanceof ThreeDEval))
/* 123:    */     {
/* 124:146 */       ThreeDEval ae = (ThreeDEval)operand;
/* 125:147 */       for (int sIx = ae.getFirstSheetIndex(); sIx <= ae.getLastSheetIndex(); sIx++)
/* 126:    */       {
/* 127:148 */         int width = ae.getWidth();
/* 128:149 */         int height = ae.getHeight();
/* 129:150 */         for (int rrIx = 0; rrIx < height; rrIx++) {
/* 130:151 */           for (int rcIx = 0; rcIx < width; rcIx++)
/* 131:    */           {
/* 132:152 */             ValueEval ve = ae.getValue(sIx, rrIx, rcIx);
/* 133:153 */             if ((isSubtotalCounted()) || (!ae.isSubTotal(rrIx, rcIx))) {
/* 134:154 */               collectValue(ve, true, temp);
/* 135:    */             }
/* 136:    */           }
/* 137:    */         }
/* 138:    */       }
/* 139:158 */       return;
/* 140:    */     }
/* 141:160 */     if ((operand instanceof TwoDEval))
/* 142:    */     {
/* 143:161 */       TwoDEval ae = (TwoDEval)operand;
/* 144:162 */       int width = ae.getWidth();
/* 145:163 */       int height = ae.getHeight();
/* 146:164 */       for (int rrIx = 0; rrIx < height; rrIx++) {
/* 147:165 */         for (int rcIx = 0; rcIx < width; rcIx++)
/* 148:    */         {
/* 149:166 */           ValueEval ve = ae.getValue(rrIx, rcIx);
/* 150:167 */           if ((isSubtotalCounted()) || (!ae.isSubTotal(rrIx, rcIx))) {
/* 151:168 */             collectValue(ve, true, temp);
/* 152:    */           }
/* 153:    */         }
/* 154:    */       }
/* 155:171 */       return;
/* 156:    */     }
/* 157:173 */     if ((operand instanceof RefEval))
/* 158:    */     {
/* 159:174 */       RefEval re = (RefEval)operand;
/* 160:175 */       for (int sIx = re.getFirstSheetIndex(); sIx <= re.getLastSheetIndex(); sIx++) {
/* 161:176 */         collectValue(re.getInnerValueEval(sIx), true, temp);
/* 162:    */       }
/* 163:178 */       return;
/* 164:    */     }
/* 165:180 */     collectValue(operand, false, temp);
/* 166:    */   }
/* 167:    */   
/* 168:    */   private void collectValue(ValueEval ve, boolean isViaReference, DoubleList temp)
/* 169:    */     throws EvaluationException
/* 170:    */   {
/* 171:183 */     if (ve == null) {
/* 172:184 */       throw new IllegalArgumentException("ve must not be null");
/* 173:    */     }
/* 174:186 */     if ((ve instanceof BoolEval))
/* 175:    */     {
/* 176:187 */       if ((!isViaReference) || (this._isReferenceBoolCounted))
/* 177:    */       {
/* 178:188 */         BoolEval boolEval = (BoolEval)ve;
/* 179:189 */         temp.add(boolEval.getNumberValue());
/* 180:    */       }
/* 181:191 */       return;
/* 182:    */     }
/* 183:193 */     if ((ve instanceof NumericValueEval))
/* 184:    */     {
/* 185:194 */       NumericValueEval ne = (NumericValueEval)ve;
/* 186:195 */       temp.add(ne.getNumberValue());
/* 187:196 */       return;
/* 188:    */     }
/* 189:198 */     if ((ve instanceof StringValueEval))
/* 190:    */     {
/* 191:199 */       if (isViaReference) {
/* 192:201 */         return;
/* 193:    */       }
/* 194:203 */       String s = ((StringValueEval)ve).getStringValue();
/* 195:204 */       Double d = OperandResolver.parseDouble(s);
/* 196:205 */       if (d == null) {
/* 197:206 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 198:    */       }
/* 199:208 */       temp.add(d.doubleValue());
/* 200:209 */       return;
/* 201:    */     }
/* 202:211 */     if ((ve instanceof ErrorEval)) {
/* 203:212 */       throw new EvaluationException((ErrorEval)ve);
/* 204:    */     }
/* 205:214 */     if (ve == BlankEval.instance)
/* 206:    */     {
/* 207:215 */       if (this._isBlankCounted) {
/* 208:216 */         temp.add(0.0D);
/* 209:    */       }
/* 210:218 */       return;
/* 211:    */     }
/* 212:220 */     throw new RuntimeException("Invalid ValueEval type passed for conversion: (" + ve.getClass() + ")");
/* 213:    */   }
/* 214:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.MultiOperandNumericFunction
 * JD-Core Version:    0.7.0.1
 */