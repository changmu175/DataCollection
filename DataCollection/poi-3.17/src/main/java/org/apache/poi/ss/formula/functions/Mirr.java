/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ 
/*   6:    */ public class Mirr
/*   7:    */   extends MultiOperandNumericFunction
/*   8:    */ {
/*   9:    */   public Mirr()
/*  10:    */   {
/*  11: 49 */     super(false, false);
/*  12:    */   }
/*  13:    */   
/*  14:    */   protected int getMaxNumOperands()
/*  15:    */   {
/*  16: 54 */     return 3;
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected double evaluate(double[] values)
/*  20:    */     throws EvaluationException
/*  21:    */   {
/*  22: 60 */     double financeRate = values[(values.length - 1)];
/*  23: 61 */     double reinvestRate = values[(values.length - 2)];
/*  24:    */     
/*  25: 63 */     double[] mirrValues = new double[values.length - 2];
/*  26: 64 */     System.arraycopy(values, 0, mirrValues, 0, mirrValues.length);
/*  27:    */     
/*  28: 66 */     boolean mirrValuesAreAllNegatives = true;
/*  29: 67 */     for (double mirrValue : mirrValues) {
/*  30: 68 */       mirrValuesAreAllNegatives &= mirrValue < 0.0D;
/*  31:    */     }
/*  32: 70 */     if (mirrValuesAreAllNegatives) {
/*  33: 71 */       return -1.0D;
/*  34:    */     }
/*  35: 74 */     boolean mirrValuesAreAllPositives = true;
/*  36: 75 */     for (double mirrValue : mirrValues) {
/*  37: 76 */       mirrValuesAreAllPositives &= mirrValue > 0.0D;
/*  38:    */     }
/*  39: 78 */     if (mirrValuesAreAllPositives) {
/*  40: 79 */       throw new EvaluationException(ErrorEval.DIV_ZERO);
/*  41:    */     }
/*  42: 82 */     return mirr(mirrValues, financeRate, reinvestRate);
/*  43:    */   }
/*  44:    */   
/*  45:    */   private static double mirr(double[] in, double financeRate, double reinvestRate)
/*  46:    */   {
/*  47: 86 */     double value = 0.0D;
/*  48: 87 */     int numOfYears = in.length - 1;
/*  49: 88 */     double pv = 0.0D;
/*  50: 89 */     double fv = 0.0D;
/*  51:    */     
/*  52: 91 */     int indexN = 0;
/*  53: 92 */     for (double anIn : in) {
/*  54: 93 */       if (anIn < 0.0D) {
/*  55: 94 */         pv += anIn / Math.pow(1.0D + financeRate + reinvestRate, indexN++);
/*  56:    */       }
/*  57:    */     }
/*  58: 98 */     for (double anIn : in) {
/*  59: 99 */       if (anIn > 0.0D) {
/*  60:100 */         fv += anIn * Math.pow(1.0D + financeRate, numOfYears - indexN++);
/*  61:    */       }
/*  62:    */     }
/*  63:104 */     if ((fv != 0.0D) && (pv != 0.0D)) {
/*  64:105 */       value = Math.pow(-fv / pv, 1.0D / numOfYears) - 1.0D;
/*  65:    */     }
/*  66:107 */     return value;
/*  67:    */   }
/*  68:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Mirr
 * JD-Core Version:    0.7.0.1
 */