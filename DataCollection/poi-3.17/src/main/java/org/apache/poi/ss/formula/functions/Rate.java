/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ 
/*  11:    */ public class Rate
/*  12:    */   implements Function
/*  13:    */ {
/*  14: 32 */   private static final POILogger LOG = POILogFactory.getLogger(Rate.class);
/*  15:    */   
/*  16:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  17:    */   {
/*  18: 35 */     if (args.length < 3) {
/*  19: 36 */       return ErrorEval.VALUE_INVALID;
/*  20:    */     }
/*  21: 39 */     double future_val = 0.0D;double type = 0.0D;double estimate = 0.1D;
/*  22:    */     double rate;
/*  23:    */     try
/*  24:    */     {
/*  25: 42 */       ValueEval v1 = OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex);
/*  26: 43 */       ValueEval v2 = OperandResolver.getSingleValue(args[1], srcRowIndex, srcColumnIndex);
/*  27: 44 */       ValueEval v3 = OperandResolver.getSingleValue(args[2], srcRowIndex, srcColumnIndex);
/*  28: 45 */       ValueEval v4 = null;
/*  29: 46 */       if (args.length >= 4) {
/*  30: 47 */         v4 = OperandResolver.getSingleValue(args[3], srcRowIndex, srcColumnIndex);
/*  31:    */       }
/*  32: 48 */       ValueEval v5 = null;
/*  33: 49 */       if (args.length >= 5) {
/*  34: 50 */         v5 = OperandResolver.getSingleValue(args[4], srcRowIndex, srcColumnIndex);
/*  35:    */       }
/*  36: 51 */       ValueEval v6 = null;
/*  37: 52 */       if (args.length >= 6) {
/*  38: 53 */         v6 = OperandResolver.getSingleValue(args[5], srcRowIndex, srcColumnIndex);
/*  39:    */       }
/*  40: 55 */       double periods = OperandResolver.coerceValueToDouble(v1);
/*  41: 56 */       double payment = OperandResolver.coerceValueToDouble(v2);
/*  42: 57 */       double present_val = OperandResolver.coerceValueToDouble(v3);
/*  43: 58 */       if (args.length >= 4) {
/*  44: 59 */         future_val = OperandResolver.coerceValueToDouble(v4);
/*  45:    */       }
/*  46: 60 */       if (args.length >= 5) {
/*  47: 61 */         type = OperandResolver.coerceValueToDouble(v5);
/*  48:    */       }
/*  49: 62 */       if (args.length >= 6) {
/*  50: 63 */         estimate = OperandResolver.coerceValueToDouble(v6);
/*  51:    */       }
/*  52: 64 */       rate = calculateRate(periods, payment, present_val, future_val, type, estimate);
/*  53:    */       
/*  54: 66 */       checkValue(rate);
/*  55:    */     }
/*  56:    */     catch (EvaluationException e)
/*  57:    */     {
/*  58: 68 */       LOG.log(7, new Object[] { "Can't evaluate rate function", e });
/*  59: 69 */       return e.getErrorEval();
/*  60:    */     }
/*  61: 72 */     return new NumberEval(rate);
/*  62:    */   }
/*  63:    */   
/*  64:    */   private double calculateRate(double nper, double pmt, double pv, double fv, double type, double guess)
/*  65:    */   {
/*  66: 77 */     int FINANCIAL_MAX_ITERATIONS = 20;
/*  67: 78 */     double FINANCIAL_PRECISION = 1.0E-007D;
/*  68:    */     
/*  69: 80 */     double x1 = 0.0D;double f = 0.0D;double i = 0.0D;
/*  70: 81 */     double rate = guess;
/*  71:    */     double y;
/*  72:    */     double y;
/*  73: 82 */     if (Math.abs(rate) < FINANCIAL_PRECISION)
/*  74:    */     {
/*  75: 83 */       y = pv * (1.0D + nper * rate) + pmt * (1.0D + rate * type) * nper + fv;
/*  76:    */     }
/*  77:    */     else
/*  78:    */     {
/*  79: 85 */       f = Math.exp(nper * Math.log(1.0D + rate));
/*  80: 86 */       y = pv * f + pmt * (1.0D / rate + type) * (f - 1.0D) + fv;
/*  81:    */     }
/*  82: 88 */     double y0 = pv + pmt * nper + fv;
/*  83: 89 */     double y1 = pv * f + pmt * (1.0D / rate + type) * (f - 1.0D) + fv;
/*  84:    */     double x0;
/*  85: 92 */     i = x0 = 0.0D;
/*  86: 93 */     x1 = rate;
/*  87: 94 */     while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS))
/*  88:    */     {
/*  89: 95 */       rate = (y1 * x0 - y0 * x1) / (y1 - y0);
/*  90: 96 */       x0 = x1;
/*  91: 97 */       x1 = rate;
/*  92: 99 */       if (Math.abs(rate) < FINANCIAL_PRECISION)
/*  93:    */       {
/*  94:100 */         y = pv * (1.0D + nper * rate) + pmt * (1.0D + rate * type) * nper + fv;
/*  95:    */       }
/*  96:    */       else
/*  97:    */       {
/*  98:102 */         f = Math.exp(nper * Math.log(1.0D + rate));
/*  99:103 */         y = pv * f + pmt * (1.0D / rate + type) * (f - 1.0D) + fv;
/* 100:    */       }
/* 101:106 */       y0 = y1;
/* 102:107 */       y1 = y;
/* 103:108 */       i += 1.0D;
/* 104:    */     }
/* 105:110 */     return rate;
/* 106:    */   }
/* 107:    */   
/* 108:    */   static final void checkValue(double result)
/* 109:    */     throws EvaluationException
/* 110:    */   {
/* 111:119 */     if ((Double.isNaN(result)) || (Double.isInfinite(result))) {
/* 112:120 */       throw new EvaluationException(ErrorEval.NUM_ERROR);
/* 113:    */     }
/* 114:    */   }
/* 115:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Rate
 * JD-Core Version:    0.7.0.1
 */