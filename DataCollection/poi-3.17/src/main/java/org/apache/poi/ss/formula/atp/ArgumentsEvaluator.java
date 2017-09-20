/*   1:    */ package org.apache.poi.ss.formula.atp;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Calendar;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ss.formula.eval.AreaEvalBase;
/*   7:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  12:    */ 
/*  13:    */ final class ArgumentsEvaluator
/*  14:    */ {
/*  15: 38 */   public static final ArgumentsEvaluator instance = new ArgumentsEvaluator();
/*  16:    */   
/*  17:    */   public double evaluateDateArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  18:    */     throws EvaluationException
/*  19:    */   {
/*  20: 54 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short)srcCellCol);
/*  21: 56 */     if ((ve instanceof StringEval))
/*  22:    */     {
/*  23: 57 */       String strVal = ((StringEval)ve).getStringValue();
/*  24: 58 */       Double dVal = OperandResolver.parseDouble(strVal);
/*  25: 59 */       if (dVal != null) {
/*  26: 60 */         return dVal.doubleValue();
/*  27:    */       }
/*  28: 62 */       Calendar date = DateParser.parseDate(strVal);
/*  29: 63 */       return DateUtil.getExcelDate(date, false);
/*  30:    */     }
/*  31: 65 */     return OperandResolver.coerceValueToDouble(ve);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public double[] evaluateDatesArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  35:    */     throws EvaluationException
/*  36:    */   {
/*  37: 78 */     if (arg == null) {
/*  38: 79 */       return new double[0];
/*  39:    */     }
/*  40: 82 */     if ((arg instanceof StringEval)) {
/*  41: 83 */       return new double[] { evaluateDateArg(arg, srcCellRow, srcCellCol) };
/*  42:    */     }
/*  43: 84 */     if ((arg instanceof AreaEvalBase))
/*  44:    */     {
/*  45: 85 */       List<Double> valuesList = new ArrayList();
/*  46: 86 */       AreaEvalBase area = (AreaEvalBase)arg;
/*  47: 87 */       for (int i = area.getFirstRow(); i <= area.getLastRow(); i++) {
/*  48: 88 */         for (int j = area.getFirstColumn(); j <= area.getLastColumn(); j++) {
/*  49: 91 */           valuesList.add(Double.valueOf(evaluateDateArg(area.getAbsoluteValue(i, j), i, j)));
/*  50:    */         }
/*  51:    */       }
/*  52: 94 */       double[] values = new double[valuesList.size()];
/*  53: 95 */       for (int i = 0; i < valuesList.size(); i++) {
/*  54: 96 */         values[i] = ((Double)valuesList.get(i)).doubleValue();
/*  55:    */       }
/*  56: 98 */       return values;
/*  57:    */     }
/*  58:100 */     return new double[] { OperandResolver.coerceValueToDouble(arg) };
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double evaluateNumberArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  62:    */     throws EvaluationException
/*  63:    */   {
/*  64:113 */     if (arg == null) {
/*  65:114 */       return 0.0D;
/*  66:    */     }
/*  67:117 */     return OperandResolver.coerceValueToDouble(arg);
/*  68:    */   }
/*  69:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.ArgumentsEvaluator
 * JD-Core Version:    0.7.0.1
 */