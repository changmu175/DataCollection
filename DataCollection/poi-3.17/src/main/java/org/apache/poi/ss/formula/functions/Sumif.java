/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public final class Sumif
/*  11:    */   extends Var2or3ArgFunction
/*  12:    */ {
/*  13:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  14:    */   {
/*  15:    */     AreaEval aeRange;
/*  16:    */     try
/*  17:    */     {
/*  18: 47 */       aeRange = convertRangeArg(arg0);
/*  19:    */     }
/*  20:    */     catch (EvaluationException e)
/*  21:    */     {
/*  22: 49 */       return e.getErrorEval();
/*  23:    */     }
/*  24: 51 */     return eval(srcRowIndex, srcColumnIndex, arg1, aeRange, aeRange);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  28:    */   {
/*  29:    */     AreaEval aeRange;
/*  30:    */     AreaEval aeSum;
/*  31:    */     try
/*  32:    */     {
/*  33: 60 */       aeRange = convertRangeArg(arg0);
/*  34: 61 */       aeSum = createSumRange(arg2, aeRange);
/*  35:    */     }
/*  36:    */     catch (EvaluationException e)
/*  37:    */     {
/*  38: 63 */       return e.getErrorEval();
/*  39:    */     }
/*  40: 65 */     return eval(srcRowIndex, srcColumnIndex, arg1, aeRange, aeSum);
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static ValueEval eval(int srcRowIndex, int srcColumnIndex, ValueEval arg1, AreaEval aeRange, AreaEval aeSum)
/*  44:    */   {
/*  45: 71 */     CountUtils.I_MatchPredicate mp = Countif.createCriteriaPredicate(arg1, srcRowIndex, srcColumnIndex);
/*  46: 74 */     if (mp == null) {
/*  47: 75 */       return NumberEval.ZERO;
/*  48:    */     }
/*  49: 78 */     double result = sumMatchingCells(aeRange, mp, aeSum);
/*  50: 79 */     return new NumberEval(result);
/*  51:    */   }
/*  52:    */   
/*  53:    */   private static double sumMatchingCells(AreaEval aeRange, CountUtils.I_MatchPredicate mp, AreaEval aeSum)
/*  54:    */   {
/*  55: 83 */     int height = aeRange.getHeight();
/*  56: 84 */     int width = aeRange.getWidth();
/*  57:    */     
/*  58: 86 */     double result = 0.0D;
/*  59: 87 */     for (int r = 0; r < height; r++) {
/*  60: 88 */       for (int c = 0; c < width; c++) {
/*  61: 89 */         result += accumulate(aeRange, mp, aeSum, r, c);
/*  62:    */       }
/*  63:    */     }
/*  64: 92 */     return result;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private static double accumulate(AreaEval aeRange, CountUtils.I_MatchPredicate mp, AreaEval aeSum, int relRowIndex, int relColIndex)
/*  68:    */   {
/*  69: 98 */     if (!mp.matches(aeRange.getRelativeValue(relRowIndex, relColIndex))) {
/*  70: 99 */       return 0.0D;
/*  71:    */     }
/*  72:101 */     ValueEval addend = aeSum.getRelativeValue(relRowIndex, relColIndex);
/*  73:102 */     if ((addend instanceof NumberEval)) {
/*  74:103 */       return ((NumberEval)addend).getNumberValue();
/*  75:    */     }
/*  76:106 */     return 0.0D;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private static AreaEval createSumRange(ValueEval eval, AreaEval aeRange)
/*  80:    */     throws EvaluationException
/*  81:    */   {
/*  82:114 */     if ((eval instanceof AreaEval)) {
/*  83:115 */       return ((AreaEval)eval).offset(0, aeRange.getHeight() - 1, 0, aeRange.getWidth() - 1);
/*  84:    */     }
/*  85:117 */     if ((eval instanceof RefEval)) {
/*  86:118 */       return ((RefEval)eval).offset(0, aeRange.getHeight() - 1, 0, aeRange.getWidth() - 1);
/*  87:    */     }
/*  88:120 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/*  89:    */   }
/*  90:    */   
/*  91:    */   private static AreaEval convertRangeArg(ValueEval eval)
/*  92:    */     throws EvaluationException
/*  93:    */   {
/*  94:124 */     if ((eval instanceof AreaEval)) {
/*  95:125 */       return (AreaEval)eval;
/*  96:    */     }
/*  97:127 */     if ((eval instanceof RefEval)) {
/*  98:128 */       return ((RefEval)eval).offset(0, 0, 0, 0);
/*  99:    */     }
/* 100:130 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 101:    */   }
/* 102:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Sumif
 * JD-Core Version:    0.7.0.1
 */