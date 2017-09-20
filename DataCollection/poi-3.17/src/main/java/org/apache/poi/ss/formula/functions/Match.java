/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  12:    */ 
/*  13:    */ public final class Match
/*  14:    */   extends Var2or3ArgFunction
/*  15:    */ {
/*  16:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  17:    */   {
/*  18: 67 */     return eval(srcRowIndex, srcColumnIndex, arg0, arg1, 1.0D);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  22:    */   {
/*  23:    */     double match_type;
/*  24:    */     try
/*  25:    */     {
/*  26: 77 */       match_type = evaluateMatchTypeArg(arg2, srcRowIndex, srcColumnIndex);
/*  27:    */     }
/*  28:    */     catch (EvaluationException e)
/*  29:    */     {
/*  30: 82 */       return ErrorEval.REF_INVALID;
/*  31:    */     }
/*  32: 85 */     return eval(srcRowIndex, srcColumnIndex, arg0, arg1, match_type);
/*  33:    */   }
/*  34:    */   
/*  35:    */   private static ValueEval eval(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, double match_type)
/*  36:    */   {
/*  37: 90 */     boolean matchExact = match_type == 0.0D;
/*  38:    */     
/*  39: 92 */     boolean findLargestLessThanOrEqual = match_type > 0.0D;
/*  40:    */     try
/*  41:    */     {
/*  42: 95 */       ValueEval lookupValue = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  43: 96 */       LookupUtils.ValueVector lookupRange = evaluateLookupRange(arg1);
/*  44: 97 */       int index = findIndexOfValue(lookupValue, lookupRange, matchExact, findLargestLessThanOrEqual);
/*  45: 98 */       return new NumberEval(index + 1);
/*  46:    */     }
/*  47:    */     catch (EvaluationException e)
/*  48:    */     {
/*  49:100 */       return e.getErrorEval();
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   private static final class SingleValueVector
/*  54:    */     implements LookupUtils.ValueVector
/*  55:    */   {
/*  56:    */     private final ValueEval _value;
/*  57:    */     
/*  58:    */     public SingleValueVector(ValueEval value)
/*  59:    */     {
/*  60:109 */       this._value = value;
/*  61:    */     }
/*  62:    */     
/*  63:    */     public ValueEval getItem(int index)
/*  64:    */     {
/*  65:113 */       if (index != 0) {
/*  66:114 */         throw new RuntimeException("Invalid index (" + index + ") only zero is allowed");
/*  67:    */       }
/*  68:117 */       return this._value;
/*  69:    */     }
/*  70:    */     
/*  71:    */     public int getSize()
/*  72:    */     {
/*  73:121 */       return 1;
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   private static LookupUtils.ValueVector evaluateLookupRange(ValueEval eval)
/*  78:    */     throws EvaluationException
/*  79:    */   {
/*  80:126 */     if ((eval instanceof RefEval))
/*  81:    */     {
/*  82:127 */       RefEval re = (RefEval)eval;
/*  83:128 */       if (re.getNumberOfSheets() == 1) {
/*  84:129 */         return new SingleValueVector(re.getInnerValueEval(re.getFirstSheetIndex()));
/*  85:    */       }
/*  86:131 */       return LookupUtils.createVector(re);
/*  87:    */     }
/*  88:134 */     if ((eval instanceof TwoDEval))
/*  89:    */     {
/*  90:135 */       LookupUtils.ValueVector result = LookupUtils.createVector((TwoDEval)eval);
/*  91:136 */       if (result == null) {
/*  92:137 */         throw new EvaluationException(ErrorEval.NA);
/*  93:    */       }
/*  94:139 */       return result;
/*  95:    */     }
/*  96:143 */     if ((eval instanceof NumericValueEval)) {
/*  97:144 */       throw new EvaluationException(ErrorEval.NA);
/*  98:    */     }
/*  99:146 */     if ((eval instanceof StringEval))
/* 100:    */     {
/* 101:147 */       StringEval se = (StringEval)eval;
/* 102:148 */       Double d = OperandResolver.parseDouble(se.getStringValue());
/* 103:149 */       if (d == null) {
/* 104:151 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 105:    */       }
/* 106:154 */       throw new EvaluationException(ErrorEval.NA);
/* 107:    */     }
/* 108:156 */     throw new RuntimeException("Unexpected eval type (" + eval + ")");
/* 109:    */   }
/* 110:    */   
/* 111:    */   private static double evaluateMatchTypeArg(ValueEval arg, int srcCellRow, int srcCellCol)
/* 112:    */     throws EvaluationException
/* 113:    */   {
/* 114:163 */     ValueEval match_type = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
/* 115:165 */     if ((match_type instanceof ErrorEval)) {
/* 116:166 */       throw new EvaluationException((ErrorEval)match_type);
/* 117:    */     }
/* 118:168 */     if ((match_type instanceof NumericValueEval))
/* 119:    */     {
/* 120:169 */       NumericValueEval ne = (NumericValueEval)match_type;
/* 121:170 */       return ne.getNumberValue();
/* 122:    */     }
/* 123:172 */     if ((match_type instanceof StringEval))
/* 124:    */     {
/* 125:173 */       StringEval se = (StringEval)match_type;
/* 126:174 */       Double d = OperandResolver.parseDouble(se.getStringValue());
/* 127:175 */       if (d == null) {
/* 128:177 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 129:    */       }
/* 130:180 */       return d.doubleValue();
/* 131:    */     }
/* 132:182 */     throw new RuntimeException("Unexpected match_type type (" + match_type.getClass().getName() + ")");
/* 133:    */   }
/* 134:    */   
/* 135:    */   private static int findIndexOfValue(ValueEval lookupValue, LookupUtils.ValueVector lookupRange, boolean matchExact, boolean findLargestLessThanOrEqual)
/* 136:    */     throws EvaluationException
/* 137:    */   {
/* 138:191 */     LookupUtils.LookupValueComparer lookupComparer = createLookupComparer(lookupValue, matchExact);
/* 139:    */     
/* 140:193 */     int size = lookupRange.getSize();
/* 141:194 */     if (matchExact)
/* 142:    */     {
/* 143:195 */       for (int i = 0; i < size; i++) {
/* 144:196 */         if (lookupComparer.compareTo(lookupRange.getItem(i)).isEqual()) {
/* 145:197 */           return i;
/* 146:    */         }
/* 147:    */       }
/* 148:200 */       throw new EvaluationException(ErrorEval.NA);
/* 149:    */     }
/* 150:203 */     if (findLargestLessThanOrEqual)
/* 151:    */     {
/* 152:205 */       for (int i = size - 1; i >= 0; i--)
/* 153:    */       {
/* 154:206 */         LookupUtils.CompareResult cmp = lookupComparer.compareTo(lookupRange.getItem(i));
/* 155:207 */         if (!cmp.isTypeMismatch()) {
/* 156:210 */           if (!cmp.isLessThan()) {
/* 157:211 */             return i;
/* 158:    */           }
/* 159:    */         }
/* 160:    */       }
/* 161:214 */       throw new EvaluationException(ErrorEval.NA);
/* 162:    */     }
/* 163:219 */     for (int i = 0; i < size; i++)
/* 164:    */     {
/* 165:220 */       LookupUtils.CompareResult cmp = lookupComparer.compareTo(lookupRange.getItem(i));
/* 166:221 */       if (cmp.isEqual()) {
/* 167:222 */         return i;
/* 168:    */       }
/* 169:224 */       if (cmp.isGreaterThan())
/* 170:    */       {
/* 171:225 */         if (i < 1) {
/* 172:226 */           throw new EvaluationException(ErrorEval.NA);
/* 173:    */         }
/* 174:228 */         return i - 1;
/* 175:    */       }
/* 176:    */     }
/* 177:231 */     return size - 1;
/* 178:    */   }
/* 179:    */   
/* 180:    */   private static LookupUtils.LookupValueComparer createLookupComparer(ValueEval lookupValue, boolean matchExact)
/* 181:    */   {
/* 182:235 */     return LookupUtils.createLookupComparer(lookupValue, matchExact, true);
/* 183:    */   }
/* 184:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Match
 * JD-Core Version:    0.7.0.1
 */