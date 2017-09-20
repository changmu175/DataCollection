/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ 
/*   9:    */ public abstract class AggregateFunction
/*  10:    */   extends MultiOperandNumericFunction
/*  11:    */ {
/*  12:    */   private static final class LargeSmall
/*  13:    */     extends Fixed2ArgFunction
/*  14:    */   {
/*  15:    */     private final boolean _isLarge;
/*  16:    */     
/*  17:    */     protected LargeSmall(boolean isLarge)
/*  18:    */     {
/*  19: 34 */       this._isLarge = isLarge;
/*  20:    */     }
/*  21:    */     
/*  22:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  23:    */     {
/*  24:    */       double dn;
/*  25:    */       try
/*  26:    */       {
/*  27: 41 */         ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
/*  28: 42 */         dn = OperandResolver.coerceValueToDouble(ve1);
/*  29:    */       }
/*  30:    */       catch (EvaluationException e1)
/*  31:    */       {
/*  32: 45 */         return ErrorEval.VALUE_INVALID;
/*  33:    */       }
/*  34: 48 */       if (dn < 1.0D) {
/*  35: 50 */         return ErrorEval.NUM_ERROR;
/*  36:    */       }
/*  37: 53 */       int k = (int)Math.ceil(dn);
/*  38:    */       double result;
/*  39:    */       try
/*  40:    */       {
/*  41: 57 */         double[] ds = ValueCollector.collectValues(new ValueEval[] { arg0 });
/*  42: 58 */         if (k > ds.length) {
/*  43: 59 */           return ErrorEval.NUM_ERROR;
/*  44:    */         }
/*  45: 61 */         result = this._isLarge ? StatsLib.kthLargest(ds, k) : StatsLib.kthSmallest(ds, k);
/*  46: 62 */         NumericFunction.checkValue(result);
/*  47:    */       }
/*  48:    */       catch (EvaluationException e)
/*  49:    */       {
/*  50: 64 */         return e.getErrorEval();
/*  51:    */       }
/*  52: 67 */       return new NumberEval(result);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static final class Percentile
/*  57:    */     extends Fixed2ArgFunction
/*  58:    */   {
/*  59:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  60:    */     {
/*  61:    */       double dn;
/*  62:    */       try
/*  63:    */       {
/*  64: 96 */         ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
/*  65: 97 */         dn = OperandResolver.coerceValueToDouble(ve1);
/*  66:    */       }
/*  67:    */       catch (EvaluationException e1)
/*  68:    */       {
/*  69:100 */         return ErrorEval.VALUE_INVALID;
/*  70:    */       }
/*  71:102 */       if ((dn < 0.0D) || (dn > 1.0D)) {
/*  72:103 */         return ErrorEval.NUM_ERROR;
/*  73:    */       }
/*  74:    */       double result;
/*  75:    */       try
/*  76:    */       {
/*  77:108 */         double[] ds = ValueCollector.collectValues(new ValueEval[] { arg0 });
/*  78:109 */         int N = ds.length;
/*  79:111 */         if ((N == 0) || (N > 8191)) {
/*  80:112 */           return ErrorEval.NUM_ERROR;
/*  81:    */         }
/*  82:115 */         double n = (N - 1) * dn + 1.0D;
/*  83:    */         double result;
/*  84:116 */         if (n == 1.0D)
/*  85:    */         {
/*  86:117 */           result = StatsLib.kthSmallest(ds, 1);
/*  87:    */         }
/*  88:    */         else
/*  89:    */         {
/*  90:    */           double result;
/*  91:118 */           if (Double.compare(n, N) == 0)
/*  92:    */           {
/*  93:119 */             result = StatsLib.kthLargest(ds, 1);
/*  94:    */           }
/*  95:    */           else
/*  96:    */           {
/*  97:121 */             int k = (int)n;
/*  98:122 */             double d = n - k;
/*  99:123 */             result = StatsLib.kthSmallest(ds, k) + d * (StatsLib.kthSmallest(ds, k + 1) - StatsLib.kthSmallest(ds, k));
/* 100:    */           }
/* 101:    */         }
/* 102:127 */         NumericFunction.checkValue(result);
/* 103:    */       }
/* 104:    */       catch (EvaluationException e)
/* 105:    */       {
/* 106:129 */         return e.getErrorEval();
/* 107:    */       }
/* 108:132 */       return new NumberEval(result);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   static final class ValueCollector
/* 113:    */     extends MultiOperandNumericFunction
/* 114:    */   {
/* 115:137 */     private static final ValueCollector instance = new ValueCollector();
/* 116:    */     
/* 117:    */     public ValueCollector()
/* 118:    */     {
/* 119:139 */       super(false);
/* 120:    */     }
/* 121:    */     
/* 122:    */     public static double[] collectValues(ValueEval... operands)
/* 123:    */       throws EvaluationException
/* 124:    */     {
/* 125:142 */       return instance.getNumberArray(operands);
/* 126:    */     }
/* 127:    */     
/* 128:    */     protected double evaluate(double[] values)
/* 129:    */     {
/* 130:145 */       throw new IllegalStateException("should not be called");
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected AggregateFunction()
/* 135:    */   {
/* 136:150 */     super(false, false);
/* 137:    */   }
/* 138:    */   
/* 139:    */   static Function subtotalInstance(Function func)
/* 140:    */   {
/* 141:165 */     AggregateFunction arg = (AggregateFunction)func;
/* 142:166 */     new AggregateFunction()
/* 143:    */     {
/* 144:    */       protected double evaluate(double[] values)
/* 145:    */         throws EvaluationException
/* 146:    */       {
/* 147:169 */         return this.val$arg.evaluate(values);
/* 148:    */       }
/* 149:    */       
/* 150:    */       public boolean isSubtotalCounted()
/* 151:    */       {
/* 152:177 */         return false;
/* 153:    */       }
/* 154:    */     };
/* 155:    */   }
/* 156:    */   
/* 157:183 */   public static final Function AVEDEV = new AggregateFunction()
/* 158:    */   {
/* 159:    */     protected double evaluate(double[] values)
/* 160:    */     {
/* 161:185 */       return StatsLib.avedev(values);
/* 162:    */     }
/* 163:    */   };
/* 164:188 */   public static final Function AVERAGE = new AggregateFunction()
/* 165:    */   {
/* 166:    */     protected double evaluate(double[] values)
/* 167:    */       throws EvaluationException
/* 168:    */     {
/* 169:190 */       if (values.length < 1) {
/* 170:191 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 171:    */       }
/* 172:193 */       return MathX.average(values);
/* 173:    */     }
/* 174:    */   };
/* 175:196 */   public static final Function DEVSQ = new AggregateFunction()
/* 176:    */   {
/* 177:    */     protected double evaluate(double[] values)
/* 178:    */     {
/* 179:198 */       return StatsLib.devsq(values);
/* 180:    */     }
/* 181:    */   };
/* 182:201 */   public static final Function LARGE = new LargeSmall(true);
/* 183:202 */   public static final Function MAX = new AggregateFunction()
/* 184:    */   {
/* 185:    */     protected double evaluate(double[] values)
/* 186:    */     {
/* 187:204 */       return values.length > 0 ? MathX.max(values) : 0.0D;
/* 188:    */     }
/* 189:    */   };
/* 190:207 */   public static final Function MEDIAN = new AggregateFunction()
/* 191:    */   {
/* 192:    */     protected double evaluate(double[] values)
/* 193:    */     {
/* 194:209 */       return StatsLib.median(values);
/* 195:    */     }
/* 196:    */   };
/* 197:212 */   public static final Function MIN = new AggregateFunction()
/* 198:    */   {
/* 199:    */     protected double evaluate(double[] values)
/* 200:    */     {
/* 201:214 */       return values.length > 0 ? MathX.min(values) : 0.0D;
/* 202:    */     }
/* 203:    */   };
/* 204:218 */   public static final Function PERCENTILE = new Percentile();
/* 205:220 */   public static final Function PRODUCT = new AggregateFunction()
/* 206:    */   {
/* 207:    */     protected double evaluate(double[] values)
/* 208:    */     {
/* 209:222 */       return MathX.product(values);
/* 210:    */     }
/* 211:    */   };
/* 212:225 */   public static final Function SMALL = new LargeSmall(false);
/* 213:226 */   public static final Function STDEV = new AggregateFunction()
/* 214:    */   {
/* 215:    */     protected double evaluate(double[] values)
/* 216:    */       throws EvaluationException
/* 217:    */     {
/* 218:228 */       if (values.length < 1) {
/* 219:229 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 220:    */       }
/* 221:231 */       return StatsLib.stdev(values);
/* 222:    */     }
/* 223:    */   };
/* 224:234 */   public static final Function SUM = new AggregateFunction()
/* 225:    */   {
/* 226:    */     protected double evaluate(double[] values)
/* 227:    */     {
/* 228:236 */       return MathX.sum(values);
/* 229:    */     }
/* 230:    */   };
/* 231:239 */   public static final Function SUMSQ = new AggregateFunction()
/* 232:    */   {
/* 233:    */     protected double evaluate(double[] values)
/* 234:    */     {
/* 235:241 */       return MathX.sumsq(values);
/* 236:    */     }
/* 237:    */   };
/* 238:244 */   public static final Function VAR = new AggregateFunction()
/* 239:    */   {
/* 240:    */     protected double evaluate(double[] values)
/* 241:    */       throws EvaluationException
/* 242:    */     {
/* 243:246 */       if (values.length < 1) {
/* 244:247 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 245:    */       }
/* 246:249 */       return StatsLib.var(values);
/* 247:    */     }
/* 248:    */   };
/* 249:252 */   public static final Function VARP = new AggregateFunction()
/* 250:    */   {
/* 251:    */     protected double evaluate(double[] values)
/* 252:    */       throws EvaluationException
/* 253:    */     {
/* 254:254 */       if (values.length < 1) {
/* 255:255 */         throw new EvaluationException(ErrorEval.DIV_ZERO);
/* 256:    */       }
/* 257:257 */       return StatsLib.varp(values);
/* 258:    */     }
/* 259:    */   };
/* 260:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.ss.formula.functions.AggregateFunction

 * JD-Core Version:    0.7.0.1

 */