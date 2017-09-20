/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ss.formula.TwoDEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.BlankEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  11:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*  13:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  15:    */ 
/*  16:    */ public final class Mode
/*  17:    */   implements Function
/*  18:    */ {
/*  19:    */   public static double evaluate(double[] v)
/*  20:    */     throws EvaluationException
/*  21:    */   {
/*  22: 48 */     if (v.length < 2) {
/*  23: 49 */       throw new EvaluationException(ErrorEval.NA);
/*  24:    */     }
/*  25: 53 */     int[] counts = new int[v.length];
/*  26: 54 */     Arrays.fill(counts, 1);
/*  27: 55 */     int i = 0;
/*  28: 55 */     for (int iSize = v.length; i < iSize; i++)
/*  29:    */     {
/*  30: 56 */       int j = i + 1;
/*  31: 56 */       for (int jSize = v.length; j < jSize; j++) {
/*  32: 57 */         if (v[i] == v[j]) {
/*  33: 58 */           counts[i] += 1;
/*  34:    */         }
/*  35:    */       }
/*  36:    */     }
/*  37: 61 */     double maxv = 0.0D;
/*  38: 62 */     int maxc = 0;
/*  39: 63 */     int i = 0;
/*  40: 63 */     for (int iSize = counts.length; i < iSize; i++) {
/*  41: 64 */       if (counts[i] > maxc)
/*  42:    */       {
/*  43: 65 */         maxv = v[i];
/*  44: 66 */         maxc = counts[i];
/*  45:    */       }
/*  46:    */     }
/*  47: 69 */     if (maxc > 1) {
/*  48: 70 */       return maxv;
/*  49:    */     }
/*  50: 72 */     throw new EvaluationException(ErrorEval.NA);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol)
/*  54:    */   {
/*  55:    */     double result;
/*  56:    */     try
/*  57:    */     {
/*  58: 79 */       List<Double> temp = new ArrayList();
/*  59: 80 */       for (int i = 0; i < args.length; i++) {
/*  60: 81 */         collectValues(args[i], temp);
/*  61:    */       }
/*  62: 83 */       double[] values = new double[temp.size()];
/*  63: 84 */       for (int i = 0; i < values.length; i++) {
/*  64: 85 */         values[i] = ((Double)temp.get(i)).doubleValue();
/*  65:    */       }
/*  66: 87 */       result = evaluate(values);
/*  67:    */     }
/*  68:    */     catch (EvaluationException e)
/*  69:    */     {
/*  70: 89 */       return e.getErrorEval();
/*  71:    */     }
/*  72: 91 */     return new NumberEval(result);
/*  73:    */   }
/*  74:    */   
/*  75:    */   private static void collectValues(ValueEval arg, List<Double> temp)
/*  76:    */     throws EvaluationException
/*  77:    */   {
/*  78: 95 */     if ((arg instanceof TwoDEval))
/*  79:    */     {
/*  80: 96 */       TwoDEval ae = (TwoDEval)arg;
/*  81: 97 */       int width = ae.getWidth();
/*  82: 98 */       int height = ae.getHeight();
/*  83: 99 */       for (int rrIx = 0; rrIx < height; rrIx++) {
/*  84:100 */         for (int rcIx = 0; rcIx < width; rcIx++)
/*  85:    */         {
/*  86:101 */           ValueEval ve1 = ae.getValue(rrIx, rcIx);
/*  87:102 */           collectValue(ve1, temp, false);
/*  88:    */         }
/*  89:    */       }
/*  90:105 */       return;
/*  91:    */     }
/*  92:107 */     if ((arg instanceof RefEval))
/*  93:    */     {
/*  94:108 */       RefEval re = (RefEval)arg;
/*  95:109 */       int firstSheetIndex = re.getFirstSheetIndex();
/*  96:110 */       int lastSheetIndex = re.getLastSheetIndex();
/*  97:111 */       for (int sIx = firstSheetIndex; sIx <= lastSheetIndex; sIx++) {
/*  98:112 */         collectValue(re.getInnerValueEval(sIx), temp, true);
/*  99:    */       }
/* 100:114 */       return;
/* 101:    */     }
/* 102:116 */     collectValue(arg, temp, true);
/* 103:    */   }
/* 104:    */   
/* 105:    */   private static void collectValue(ValueEval arg, List<Double> temp, boolean mustBeNumber)
/* 106:    */     throws EvaluationException
/* 107:    */   {
/* 108:122 */     if ((arg instanceof ErrorEval)) {
/* 109:123 */       throw new EvaluationException((ErrorEval)arg);
/* 110:    */     }
/* 111:125 */     if ((arg == BlankEval.instance) || ((arg instanceof BoolEval)) || ((arg instanceof StringEval)))
/* 112:    */     {
/* 113:126 */       if (mustBeNumber) {
/* 114:127 */         throw EvaluationException.invalidValue();
/* 115:    */       }
/* 116:129 */       return;
/* 117:    */     }
/* 118:131 */     if ((arg instanceof NumberEval))
/* 119:    */     {
/* 120:132 */       temp.add(new Double(((NumberEval)arg).getNumberValue()));
/* 121:133 */       return;
/* 122:    */     }
/* 123:135 */     throw new RuntimeException("Unexpected value type (" + arg.getClass().getName() + ")");
/* 124:    */   }
/* 125:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Mode
 * JD-Core Version:    0.7.0.1
 */