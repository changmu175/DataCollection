/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   8:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.RefListEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ 
/*  12:    */ public class Rank
/*  13:    */   extends Var2or3ArgFunction
/*  14:    */ {
/*  15:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  16:    */   {
/*  17:    */     try
/*  18:    */     {
/*  19: 43 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  20: 44 */       double result = OperandResolver.coerceValueToDouble(ve);
/*  21: 45 */       if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/*  22: 46 */         throw new EvaluationException(ErrorEval.NUM_ERROR);
/*  23:    */       }
/*  24: 49 */       if ((arg1 instanceof RefListEval)) {
/*  25: 50 */         return eval(result, (RefListEval)arg1, true);
/*  26:    */       }
/*  27: 53 */       AreaEval aeRange = convertRangeArg(arg1);
/*  28:    */       
/*  29: 55 */       return eval(result, aeRange, true);
/*  30:    */     }
/*  31:    */     catch (EvaluationException e)
/*  32:    */     {
/*  33: 57 */       return e.getErrorEval();
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  38:    */   {
/*  39:    */     try
/*  40:    */     {
/*  41: 63 */       ValueEval ve = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  42: 64 */       double result = OperandResolver.coerceValueToDouble(ve);
/*  43: 65 */       if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/*  44: 66 */         throw new EvaluationException(ErrorEval.NUM_ERROR);
/*  45:    */       }
/*  46: 69 */       ve = OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex);
/*  47: 70 */       int order_value = OperandResolver.coerceValueToInt(ve);
/*  48:    */       boolean order;
/*  49: 72 */       if (order_value == 0)
/*  50:    */       {
/*  51: 73 */         order = true;
/*  52:    */       }
/*  53:    */       else
/*  54:    */       {
/*  55:    */         boolean order;
/*  56: 74 */         if (order_value == 1) {
/*  57: 75 */           order = false;
/*  58:    */         } else {
/*  59: 77 */           throw new EvaluationException(ErrorEval.NUM_ERROR);
/*  60:    */         }
/*  61:    */       }
/*  62:    */       boolean order;
/*  63: 80 */       if ((arg1 instanceof RefListEval)) {
/*  64: 81 */         return eval(result, (RefListEval)arg1, order);
/*  65:    */       }
/*  66: 84 */       AreaEval aeRange = convertRangeArg(arg1);
/*  67: 85 */       return eval(result, aeRange, order);
/*  68:    */     }
/*  69:    */     catch (EvaluationException e)
/*  70:    */     {
/*  71: 87 */       return e.getErrorEval();
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   private static ValueEval eval(double arg0, AreaEval aeRange, boolean descending_order)
/*  76:    */   {
/*  77: 92 */     int rank = 1;
/*  78: 93 */     int height = aeRange.getHeight();
/*  79: 94 */     int width = aeRange.getWidth();
/*  80: 95 */     for (int r = 0; r < height; r++) {
/*  81: 96 */       for (int c = 0; c < width; c++)
/*  82:    */       {
/*  83: 98 */         Double value = getValue(aeRange, r, c);
/*  84: 99 */         if ((value != null) && (
/*  85:100 */           ((descending_order) && (value.doubleValue() > arg0)) || ((!descending_order) && (value.doubleValue() < arg0)))) {
/*  86:101 */           rank++;
/*  87:    */         }
/*  88:    */       }
/*  89:    */     }
/*  90:105 */     return new NumberEval(rank);
/*  91:    */   }
/*  92:    */   
/*  93:    */   private static ValueEval eval(double arg0, RefListEval aeRange, boolean descending_order)
/*  94:    */   {
/*  95:109 */     int rank = 1;
/*  96:110 */     for (ValueEval ve : aeRange.getList())
/*  97:    */     {
/*  98:111 */       if ((ve instanceof RefEval)) {
/*  99:112 */         ve = ((RefEval)ve).getInnerValueEval(((RefEval)ve).getFirstSheetIndex());
/* 100:    */       }
/* 101:116 */       if ((ve instanceof NumberEval))
/* 102:    */       {
/* 103:117 */         Double value = Double.valueOf(((NumberEval)ve).getNumberValue());
/* 104:122 */         if (((descending_order) && (value.doubleValue() > arg0)) || ((!descending_order) && (value.doubleValue() < arg0))) {
/* 105:123 */           rank++;
/* 106:    */         }
/* 107:    */       }
/* 108:    */     }
/* 109:127 */     return new NumberEval(rank);
/* 110:    */   }
/* 111:    */   
/* 112:    */   private static Double getValue(AreaEval aeRange, int relRowIndex, int relColIndex)
/* 113:    */   {
/* 114:131 */     ValueEval addend = aeRange.getRelativeValue(relRowIndex, relColIndex);
/* 115:132 */     if ((addend instanceof NumberEval)) {
/* 116:133 */       return Double.valueOf(((NumberEval)addend).getNumberValue());
/* 117:    */     }
/* 118:136 */     return null;
/* 119:    */   }
/* 120:    */   
/* 121:    */   private static AreaEval convertRangeArg(ValueEval eval)
/* 122:    */     throws EvaluationException
/* 123:    */   {
/* 124:140 */     if ((eval instanceof AreaEval)) {
/* 125:141 */       return (AreaEval)eval;
/* 126:    */     }
/* 127:143 */     if ((eval instanceof RefEval)) {
/* 128:144 */       return ((RefEval)eval).offset(0, 0, 0, 0);
/* 129:    */     }
/* 130:146 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 131:    */   }
/* 132:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Rank
 * JD-Core Version:    0.7.0.1
 */