/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public abstract class FinanceFunction
/*  11:    */   implements Function3Arg, Function4Arg
/*  12:    */ {
/*  13: 31 */   private static final ValueEval DEFAULT_ARG3 = NumberEval.ZERO;
/*  14: 32 */   private static final ValueEval DEFAULT_ARG4 = BoolEval.FALSE;
/*  15:    */   
/*  16:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  17:    */   {
/*  18: 41 */     return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3)
/*  22:    */   {
/*  23: 45 */     return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, arg3, DEFAULT_ARG4);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3, ValueEval arg4)
/*  27:    */   {
/*  28:    */     double result;
/*  29:    */     try
/*  30:    */     {
/*  31: 51 */       double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
/*  32: 52 */       double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
/*  33: 53 */       double d2 = NumericFunction.singleOperandEvaluate(arg2, srcRowIndex, srcColumnIndex);
/*  34: 54 */       double d3 = NumericFunction.singleOperandEvaluate(arg3, srcRowIndex, srcColumnIndex);
/*  35: 55 */       double d4 = NumericFunction.singleOperandEvaluate(arg4, srcRowIndex, srcColumnIndex);
/*  36: 56 */       result = evaluate(d0, d1, d2, d3, d4 != 0.0D);
/*  37: 57 */       NumericFunction.checkValue(result);
/*  38:    */     }
/*  39:    */     catch (EvaluationException e)
/*  40:    */     {
/*  41: 59 */       return e.getErrorEval();
/*  42:    */     }
/*  43: 61 */     return new NumberEval(result);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  47:    */   {
/*  48: 64 */     switch (args.length)
/*  49:    */     {
/*  50:    */     case 3: 
/*  51: 66 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], DEFAULT_ARG3, DEFAULT_ARG4);
/*  52:    */     case 4: 
/*  53: 68 */       ValueEval arg3 = args[3];
/*  54: 69 */       if (arg3 == MissingArgEval.instance) {
/*  55: 70 */         arg3 = DEFAULT_ARG3;
/*  56:    */       }
/*  57: 72 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg3, DEFAULT_ARG4);
/*  58:    */     case 5: 
/*  59: 75 */       ValueEval arg3 = args[3];
/*  60: 76 */       if (arg3 == MissingArgEval.instance) {
/*  61: 77 */         arg3 = DEFAULT_ARG3;
/*  62:    */       }
/*  63: 79 */       ValueEval arg4 = args[4];
/*  64: 80 */       if (arg4 == MissingArgEval.instance) {
/*  65: 81 */         arg4 = DEFAULT_ARG4;
/*  66:    */       }
/*  67: 83 */       return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg3, arg4);
/*  68:    */     }
/*  69: 86 */     return ErrorEval.VALUE_INVALID;
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected double evaluate(double[] ds)
/*  73:    */     throws EvaluationException
/*  74:    */   {
/*  75: 96 */     double arg3 = 0.0D;
/*  76: 97 */     double arg4 = 0.0D;
/*  77: 99 */     switch (ds.length)
/*  78:    */     {
/*  79:    */     case 5: 
/*  80:101 */       arg4 = ds[4];
/*  81:    */     case 4: 
/*  82:104 */       arg3 = ds[3];
/*  83:    */     case 3: 
/*  84:    */       break;
/*  85:    */     default: 
/*  86:109 */       throw new IllegalStateException("Wrong number of arguments");
/*  87:    */     }
/*  88:111 */     return evaluate(ds[0], ds[1], ds[2], arg3, arg4 != 0.0D);
/*  89:    */   }
/*  90:    */   
/*  91:117 */   public static final Function FV = new FinanceFunction()
/*  92:    */   {
/*  93:    */     protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type)
/*  94:    */     {
/*  95:119 */       return FinanceLib.fv(rate, arg1, arg2, arg3, type);
/*  96:    */     }
/*  97:    */   };
/*  98:122 */   public static final Function NPER = new FinanceFunction()
/*  99:    */   {
/* 100:    */     protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type)
/* 101:    */     {
/* 102:124 */       return FinanceLib.nper(rate, arg1, arg2, arg3, type);
/* 103:    */     }
/* 104:    */   };
/* 105:127 */   public static final Function PMT = new FinanceFunction()
/* 106:    */   {
/* 107:    */     protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type)
/* 108:    */     {
/* 109:129 */       return FinanceLib.pmt(rate, arg1, arg2, arg3, type);
/* 110:    */     }
/* 111:    */   };
/* 112:132 */   public static final Function PV = new FinanceFunction()
/* 113:    */   {
/* 114:    */     protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type)
/* 115:    */     {
/* 116:134 */       return FinanceLib.pv(rate, arg1, arg2, arg3, type);
/* 117:    */     }
/* 118:    */   };
/* 119:    */   
/* 120:    */   protected abstract double evaluate(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, boolean paramBoolean)
/* 121:    */     throws EvaluationException;
/* 122:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.FinanceFunction
 * JD-Core Version:    0.7.0.1
 */