/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   8:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.NumericValueEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  11:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  13:    */ 
/*  14:    */ public final class Sumproduct
/*  15:    */   implements Function
/*  16:    */ {
/*  17:    */   public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  18:    */   {
/*  19: 59 */     int maxN = args.length;
/*  20: 61 */     if (maxN < 1) {
/*  21: 62 */       return ErrorEval.VALUE_INVALID;
/*  22:    */     }
/*  23: 64 */     ValueEval firstArg = args[0];
/*  24:    */     try
/*  25:    */     {
/*  26: 66 */       if ((firstArg instanceof NumericValueEval)) {
/*  27: 67 */         return evaluateSingleProduct(args);
/*  28:    */       }
/*  29: 69 */       if ((firstArg instanceof RefEval)) {
/*  30: 70 */         return evaluateSingleProduct(args);
/*  31:    */       }
/*  32: 72 */       if ((firstArg instanceof TwoDEval))
/*  33:    */       {
/*  34: 73 */         TwoDEval ae = (TwoDEval)firstArg;
/*  35: 74 */         if ((ae.isRow()) && (ae.isColumn())) {
/*  36: 75 */           return evaluateSingleProduct(args);
/*  37:    */         }
/*  38: 77 */         return evaluateAreaSumProduct(args);
/*  39:    */       }
/*  40:    */     }
/*  41:    */     catch (EvaluationException e)
/*  42:    */     {
/*  43: 80 */       return e.getErrorEval();
/*  44:    */     }
/*  45: 82 */     throw new RuntimeException("Invalid arg type for SUMPRODUCT: (" + firstArg.getClass().getName() + ")");
/*  46:    */   }
/*  47:    */   
/*  48:    */   private static ValueEval evaluateSingleProduct(ValueEval[] evalArgs)
/*  49:    */     throws EvaluationException
/*  50:    */   {
/*  51: 87 */     int maxN = evalArgs.length;
/*  52:    */     
/*  53: 89 */     double term = 1.0D;
/*  54: 90 */     for (int n = 0; n < maxN; n++)
/*  55:    */     {
/*  56: 91 */       double val = getScalarValue(evalArgs[n]);
/*  57: 92 */       term *= val;
/*  58:    */     }
/*  59: 94 */     return new NumberEval(term);
/*  60:    */   }
/*  61:    */   
/*  62:    */   private static double getScalarValue(ValueEval arg)
/*  63:    */     throws EvaluationException
/*  64:    */   {
/*  65:    */     ValueEval eval;
/*  66:    */     ValueEval eval;
/*  67:100 */     if ((arg instanceof RefEval))
/*  68:    */     {
/*  69:101 */       RefEval re = (RefEval)arg;
/*  70:102 */       if (re.getNumberOfSheets() > 1) {
/*  71:103 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  72:    */       }
/*  73:105 */       eval = re.getInnerValueEval(re.getFirstSheetIndex());
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:107 */       eval = arg;
/*  78:    */     }
/*  79:110 */     if (eval == null) {
/*  80:111 */       throw new RuntimeException("parameter may not be null");
/*  81:    */     }
/*  82:113 */     if ((eval instanceof AreaEval))
/*  83:    */     {
/*  84:114 */       AreaEval ae = (AreaEval)eval;
/*  85:116 */       if ((!ae.isColumn()) || (!ae.isRow())) {
/*  86:117 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  87:    */       }
/*  88:119 */       eval = ae.getRelativeValue(0, 0);
/*  89:    */     }
/*  90:122 */     return getProductTerm(eval, true);
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static ValueEval evaluateAreaSumProduct(ValueEval[] evalArgs)
/*  94:    */     throws EvaluationException
/*  95:    */   {
/*  96:126 */     int maxN = evalArgs.length;
/*  97:127 */     TwoDEval[] args = new TwoDEval[maxN];
/*  98:    */     try
/*  99:    */     {
/* 100:129 */       System.arraycopy(evalArgs, 0, args, 0, maxN);
/* 101:    */     }
/* 102:    */     catch (ArrayStoreException e)
/* 103:    */     {
/* 104:132 */       return ErrorEval.VALUE_INVALID;
/* 105:    */     }
/* 106:136 */     TwoDEval firstArg = args[0];
/* 107:    */     
/* 108:138 */     int height = firstArg.getHeight();
/* 109:139 */     int width = firstArg.getWidth();
/* 110:142 */     if (!areasAllSameSize(args, height, width))
/* 111:    */     {
/* 112:145 */       for (int i = 1; i < args.length; i++) {
/* 113:146 */         throwFirstError(args[i]);
/* 114:    */       }
/* 115:148 */       return ErrorEval.VALUE_INVALID;
/* 116:    */     }
/* 117:151 */     double acc = 0.0D;
/* 118:153 */     for (int rrIx = 0; rrIx < height; rrIx++) {
/* 119:154 */       for (int rcIx = 0; rcIx < width; rcIx++)
/* 120:    */       {
/* 121:155 */         double term = 1.0D;
/* 122:156 */         for (int n = 0; n < maxN; n++)
/* 123:    */         {
/* 124:157 */           double val = getProductTerm(args[n].getValue(rrIx, rcIx), false);
/* 125:158 */           term *= val;
/* 126:    */         }
/* 127:160 */         acc += term;
/* 128:    */       }
/* 129:    */     }
/* 130:164 */     return new NumberEval(acc);
/* 131:    */   }
/* 132:    */   
/* 133:    */   private static void throwFirstError(TwoDEval areaEval)
/* 134:    */     throws EvaluationException
/* 135:    */   {
/* 136:168 */     int height = areaEval.getHeight();
/* 137:169 */     int width = areaEval.getWidth();
/* 138:170 */     for (int rrIx = 0; rrIx < height; rrIx++) {
/* 139:171 */       for (int rcIx = 0; rcIx < width; rcIx++)
/* 140:    */       {
/* 141:172 */         ValueEval ve = areaEval.getValue(rrIx, rcIx);
/* 142:173 */         if ((ve instanceof ErrorEval)) {
/* 143:174 */           throw new EvaluationException((ErrorEval)ve);
/* 144:    */         }
/* 145:    */       }
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   private static boolean areasAllSameSize(TwoDEval[] args, int height, int width)
/* 150:    */   {
/* 151:181 */     for (int i = 0; i < args.length; i++)
/* 152:    */     {
/* 153:182 */       TwoDEval areaEval = args[i];
/* 154:184 */       if (areaEval.getHeight() != height) {
/* 155:185 */         return false;
/* 156:    */       }
/* 157:187 */       if (areaEval.getWidth() != width) {
/* 158:188 */         return false;
/* 159:    */       }
/* 160:    */     }
/* 161:191 */     return true;
/* 162:    */   }
/* 163:    */   
/* 164:    */   private static double getProductTerm(ValueEval ve, boolean isScalarProduct)
/* 165:    */     throws EvaluationException
/* 166:    */   {
/* 167:207 */     if (((ve instanceof BlankEval)) || (ve == null))
/* 168:    */     {
/* 169:210 */       if (isScalarProduct) {
/* 170:211 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 171:    */       }
/* 172:213 */       return 0.0D;
/* 173:    */     }
/* 174:216 */     if ((ve instanceof ErrorEval)) {
/* 175:217 */       throw new EvaluationException((ErrorEval)ve);
/* 176:    */     }
/* 177:219 */     if ((ve instanceof StringEval))
/* 178:    */     {
/* 179:220 */       if (isScalarProduct) {
/* 180:221 */         throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 181:    */       }
/* 182:225 */       return 0.0D;
/* 183:    */     }
/* 184:227 */     if ((ve instanceof NumericValueEval))
/* 185:    */     {
/* 186:228 */       NumericValueEval nve = (NumericValueEval)ve;
/* 187:229 */       return nve.getNumberValue();
/* 188:    */     }
/* 189:231 */     throw new RuntimeException("Unexpected value eval class (" + ve.getClass().getName() + ")");
/* 190:    */   }
/* 191:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Sumproduct
 * JD-Core Version:    0.7.0.1
 */