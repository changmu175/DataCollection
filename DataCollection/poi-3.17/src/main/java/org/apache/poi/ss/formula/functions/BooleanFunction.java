/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.MissingArgEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ 
/*  12:    */ public abstract class BooleanFunction
/*  13:    */   implements Function
/*  14:    */ {
/*  15:    */   public final ValueEval evaluate(ValueEval[] args, int srcRow, int srcCol)
/*  16:    */   {
/*  17: 43 */     if (args.length < 1) {
/*  18: 44 */       return ErrorEval.VALUE_INVALID;
/*  19:    */     }
/*  20:    */     boolean boolResult;
/*  21:    */     try
/*  22:    */     {
/*  23: 48 */       boolResult = calculate(args);
/*  24:    */     }
/*  25:    */     catch (EvaluationException e)
/*  26:    */     {
/*  27: 50 */       return e.getErrorEval();
/*  28:    */     }
/*  29: 52 */     return BoolEval.valueOf(boolResult);
/*  30:    */   }
/*  31:    */   
/*  32:    */   private boolean calculate(ValueEval[] args)
/*  33:    */     throws EvaluationException
/*  34:    */   {
/*  35: 57 */     boolean result = getInitialResultValue();
/*  36: 58 */     boolean atleastOneNonBlank = false;
/*  37: 63 */     for (ValueEval arg : args) {
/*  38: 65 */       if ((arg instanceof TwoDEval))
/*  39:    */       {
/*  40: 66 */         TwoDEval ae = (TwoDEval)arg;
/*  41: 67 */         int height = ae.getHeight();
/*  42: 68 */         int width = ae.getWidth();
/*  43: 69 */         for (int rrIx = 0; rrIx < height; rrIx++) {
/*  44: 70 */           for (int rcIx = 0; rcIx < width; rcIx++)
/*  45:    */           {
/*  46: 71 */             ValueEval ve = ae.getValue(rrIx, rcIx);
/*  47: 72 */             Boolean tempVe = OperandResolver.coerceValueToBoolean(ve, true);
/*  48: 73 */             if (tempVe != null)
/*  49:    */             {
/*  50: 74 */               result = partialEvaluate(result, tempVe.booleanValue());
/*  51: 75 */               atleastOneNonBlank = true;
/*  52:    */             }
/*  53:    */           }
/*  54:    */         }
/*  55:    */       }
/*  56: 81 */       else if ((arg instanceof RefEval))
/*  57:    */       {
/*  58: 82 */         RefEval re = (RefEval)arg;
/*  59: 83 */         int firstSheetIndex = re.getFirstSheetIndex();
/*  60: 84 */         int lastSheetIndex = re.getLastSheetIndex();
/*  61: 85 */         for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++)
/*  62:    */         {
/*  63: 86 */           ValueEval ve = re.getInnerValueEval(sIx);
/*  64: 87 */           Boolean tempVe = OperandResolver.coerceValueToBoolean(ve, true);
/*  65: 88 */           if (tempVe != null)
/*  66:    */           {
/*  67: 89 */             result = partialEvaluate(result, tempVe.booleanValue());
/*  68: 90 */             atleastOneNonBlank = true;
/*  69:    */           }
/*  70:    */         }
/*  71:    */       }
/*  72:    */       else
/*  73:    */       {
/*  74:    */         Boolean tempVe;
/*  75:    */         Boolean tempVe;
/*  76: 96 */         if (arg == MissingArgEval.instance) {
/*  77: 97 */           tempVe = null;
/*  78:    */         } else {
/*  79: 99 */           tempVe = OperandResolver.coerceValueToBoolean(arg, false);
/*  80:    */         }
/*  81:102 */         if (tempVe != null)
/*  82:    */         {
/*  83:103 */           result = partialEvaluate(result, tempVe.booleanValue());
/*  84:104 */           atleastOneNonBlank = true;
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:108 */     if (!atleastOneNonBlank) {
/*  89:109 */       throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  90:    */     }
/*  91:111 */     return result;
/*  92:    */   }
/*  93:    */   
/*  94:119 */   public static final Function AND = new BooleanFunction()
/*  95:    */   {
/*  96:    */     protected boolean getInitialResultValue()
/*  97:    */     {
/*  98:121 */       return true;
/*  99:    */     }
/* 100:    */     
/* 101:    */     protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue)
/* 102:    */     {
/* 103:124 */       return (cumulativeResult) && (currentValue);
/* 104:    */     }
/* 105:    */   };
/* 106:127 */   public static final Function OR = new BooleanFunction()
/* 107:    */   {
/* 108:    */     protected boolean getInitialResultValue()
/* 109:    */     {
/* 110:129 */       return false;
/* 111:    */     }
/* 112:    */     
/* 113:    */     protected boolean partialEvaluate(boolean cumulativeResult, boolean currentValue)
/* 114:    */     {
/* 115:132 */       return (cumulativeResult) || (currentValue);
/* 116:    */     }
/* 117:    */   };
/* 118:135 */   public static final Function FALSE = new Fixed0ArgFunction()
/* 119:    */   {
/* 120:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 121:    */     {
/* 122:137 */       return BoolEval.FALSE;
/* 123:    */     }
/* 124:    */   };
/* 125:140 */   public static final Function TRUE = new Fixed0ArgFunction()
/* 126:    */   {
/* 127:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex)
/* 128:    */     {
/* 129:142 */       return BoolEval.TRUE;
/* 130:    */     }
/* 131:    */   };
/* 132:145 */   public static final Function NOT = new Fixed1ArgFunction()
/* 133:    */   {
/* 134:    */     public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/* 135:    */     {
/* 136:    */       boolean boolArgVal;
/* 137:    */       try
/* 138:    */       {
/* 139:149 */         ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/* 140:150 */         Boolean b = OperandResolver.coerceValueToBoolean(ve, false);
/* 141:151 */         boolArgVal = b == null ? false : b.booleanValue();
/* 142:    */       }
/* 143:    */       catch (EvaluationException e)
/* 144:    */       {
/* 145:153 */         return e.getErrorEval();
/* 146:    */       }
/* 147:156 */       return BoolEval.valueOf(!boolArgVal);
/* 148:    */     }
/* 149:    */   };
/* 150:    */   
/* 151:    */   protected abstract boolean getInitialResultValue();
/* 152:    */   
/* 153:    */   protected abstract boolean partialEvaluate(boolean paramBoolean1, boolean paramBoolean2);
/* 154:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.BooleanFunction
 * JD-Core Version:    0.7.0.1
 */