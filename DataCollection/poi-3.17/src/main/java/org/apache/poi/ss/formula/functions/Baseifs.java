/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   4:    */ import org.apache.poi.ss.formula.eval.AreaEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  10:    */ 
/*  11:    */ abstract class Baseifs
/*  12:    */   implements FreeRefFunction
/*  13:    */ {
/*  14:    */   protected abstract boolean hasInitialRange();
/*  15:    */   
/*  16:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  17:    */   {
/*  18: 45 */     boolean hasInitialRange = hasInitialRange();
/*  19: 46 */     int firstCriteria = hasInitialRange ? 1 : 0;
/*  20: 48 */     if ((args.length < 2 + firstCriteria) || (args.length % 2 != firstCriteria)) {
/*  21: 49 */       return ErrorEval.VALUE_INVALID;
/*  22:    */     }
/*  23:    */     try
/*  24:    */     {
/*  25: 53 */       AreaEval sumRange = null;
/*  26: 54 */       if (hasInitialRange) {
/*  27: 55 */         sumRange = convertRangeArg(args[0]);
/*  28:    */       }
/*  29: 59 */       AreaEval[] ae = new AreaEval[(args.length - firstCriteria) / 2];
/*  30: 60 */       CountUtils.I_MatchPredicate[] mp = new CountUtils.I_MatchPredicate[ae.length];
/*  31: 61 */       int i = firstCriteria;
/*  32: 61 */       for (int k = 0; i < args.length; k++)
/*  33:    */       {
/*  34: 62 */         ae[k] = convertRangeArg(args[i]);
/*  35:    */         
/*  36: 64 */         mp[k] = Countif.createCriteriaPredicate(args[(i + 1)], ec.getRowIndex(), ec.getColumnIndex());i += 2;
/*  37:    */       }
/*  38: 67 */       validateCriteriaRanges(sumRange, ae);
/*  39: 68 */       validateCriteria(mp);
/*  40:    */       
/*  41: 70 */       double result = aggregateMatchingCells(sumRange, ae, mp);
/*  42: 71 */       return new NumberEval(result);
/*  43:    */     }
/*  44:    */     catch (EvaluationException e)
/*  45:    */     {
/*  46: 73 */       return e.getErrorEval();
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   private static void validateCriteriaRanges(AreaEval sumRange, AreaEval[] criteriaRanges)
/*  51:    */     throws EvaluationException
/*  52:    */   {
/*  53: 85 */     int h = criteriaRanges[0].getHeight();
/*  54: 86 */     int w = criteriaRanges[0].getWidth();
/*  55: 88 */     if ((sumRange != null) && ((sumRange.getHeight() != h) || (sumRange.getWidth() != w))) {
/*  56: 91 */       throw EvaluationException.invalidValue();
/*  57:    */     }
/*  58: 94 */     for (AreaEval r : criteriaRanges) {
/*  59: 95 */       if ((r.getHeight() != h) || (r.getWidth() != w)) {
/*  60: 97 */         throw EvaluationException.invalidValue();
/*  61:    */       }
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   private static void validateCriteria(CountUtils.I_MatchPredicate[] criteria)
/*  66:    */     throws EvaluationException
/*  67:    */   {
/*  68:109 */     for (CountUtils.I_MatchPredicate predicate : criteria) {
/*  69:112 */       if ((predicate instanceof Countif.ErrorMatcher)) {
/*  70:113 */         throw new EvaluationException(ErrorEval.valueOf(((Countif.ErrorMatcher)predicate).getValue()));
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   private static double aggregateMatchingCells(AreaEval sumRange, AreaEval[] ranges, CountUtils.I_MatchPredicate[] predicates)
/*  76:    */   {
/*  77:126 */     int height = ranges[0].getHeight();
/*  78:127 */     int width = ranges[0].getWidth();
/*  79:    */     
/*  80:129 */     double result = 0.0D;
/*  81:130 */     for (int r = 0; r < height; r++) {
/*  82:131 */       for (int c = 0; c < width; c++)
/*  83:    */       {
/*  84:133 */         boolean matches = true;
/*  85:134 */         for (int i = 0; i < ranges.length; i++)
/*  86:    */         {
/*  87:135 */           AreaEval aeRange = ranges[i];
/*  88:136 */           CountUtils.I_MatchPredicate mp = predicates[i];
/*  89:139 */           if ((mp == null) || (!mp.matches(aeRange.getRelativeValue(r, c))))
/*  90:    */           {
/*  91:140 */             matches = false;
/*  92:141 */             break;
/*  93:    */           }
/*  94:    */         }
/*  95:146 */         if (matches) {
/*  96:147 */           result += accumulate(sumRange, r, c);
/*  97:    */         }
/*  98:    */       }
/*  99:    */     }
/* 100:151 */     return result;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static double accumulate(AreaEval sumRange, int relRowIndex, int relColIndex)
/* 104:    */   {
/* 105:163 */     if (sumRange == null) {
/* 106:163 */       return 1.0D;
/* 107:    */     }
/* 108:165 */     ValueEval addend = sumRange.getRelativeValue(relRowIndex, relColIndex);
/* 109:166 */     if ((addend instanceof NumberEval)) {
/* 110:167 */       return ((NumberEval)addend).getNumberValue();
/* 111:    */     }
/* 112:170 */     return 0.0D;
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected static AreaEval convertRangeArg(ValueEval eval)
/* 116:    */     throws EvaluationException
/* 117:    */   {
/* 118:175 */     if ((eval instanceof AreaEval)) {
/* 119:176 */       return (AreaEval)eval;
/* 120:    */     }
/* 121:178 */     if ((eval instanceof RefEval)) {
/* 122:179 */       return ((RefEval)eval).offset(0, 0, 0, 0);
/* 123:    */     }
/* 124:181 */     throw new EvaluationException(ErrorEval.VALUE_INVALID);
/* 125:    */   }
/* 126:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Baseifs
 * JD-Core Version:    0.7.0.1
 */