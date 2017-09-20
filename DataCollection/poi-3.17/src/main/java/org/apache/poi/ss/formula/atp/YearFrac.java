/*   1:    */ package org.apache.poi.ss.formula.atp;
/*   2:    */ 
/*   3:    */ import java.util.Calendar;
/*   4:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ import org.apache.poi.ss.formula.functions.FreeRefFunction;
/*  12:    */ import org.apache.poi.ss.usermodel.DateUtil;
/*  13:    */ 
/*  14:    */ final class YearFrac
/*  15:    */   implements FreeRefFunction
/*  16:    */ {
/*  17: 53 */   public static final FreeRefFunction instance = new YearFrac();
/*  18:    */   
/*  19:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  20:    */   {
/*  21: 60 */     int srcCellRow = ec.getRowIndex();
/*  22: 61 */     int srcCellCol = ec.getColumnIndex();
/*  23:    */     double result;
/*  24:    */     try
/*  25:    */     {
/*  26: 64 */       int basis = 0;
/*  27: 65 */       switch (args.length)
/*  28:    */       {
/*  29:    */       case 3: 
/*  30: 67 */         basis = evaluateIntArg(args[2], srcCellRow, srcCellCol);
/*  31:    */       case 2: 
/*  32:    */         break;
/*  33:    */       default: 
/*  34: 72 */         return ErrorEval.VALUE_INVALID;
/*  35:    */       }
/*  36: 74 */       double startDateVal = evaluateDateArg(args[0], srcCellRow, srcCellCol);
/*  37: 75 */       double endDateVal = evaluateDateArg(args[1], srcCellRow, srcCellCol);
/*  38: 76 */       result = YearFracCalculator.calculate(startDateVal, endDateVal, basis);
/*  39:    */     }
/*  40:    */     catch (EvaluationException e)
/*  41:    */     {
/*  42: 78 */       return e.getErrorEval();
/*  43:    */     }
/*  44: 81 */     return new NumberEval(result);
/*  45:    */   }
/*  46:    */   
/*  47:    */   private static double evaluateDateArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  48:    */     throws EvaluationException
/*  49:    */   {
/*  50: 85 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short)srcCellCol);
/*  51: 87 */     if ((ve instanceof StringEval))
/*  52:    */     {
/*  53: 88 */       String strVal = ((StringEval)ve).getStringValue();
/*  54: 89 */       Double dVal = OperandResolver.parseDouble(strVal);
/*  55: 90 */       if (dVal != null) {
/*  56: 91 */         return dVal.doubleValue();
/*  57:    */       }
/*  58: 93 */       Calendar date = DateParser.parseDate(strVal);
/*  59: 94 */       return DateUtil.getExcelDate(date, false);
/*  60:    */     }
/*  61: 96 */     return OperandResolver.coerceValueToDouble(ve);
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static int evaluateIntArg(ValueEval arg, int srcCellRow, int srcCellCol)
/*  65:    */     throws EvaluationException
/*  66:    */   {
/*  67:100 */     ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short)srcCellCol);
/*  68:101 */     return OperandResolver.coerceValueToInt(ve);
/*  69:    */   }
/*  70:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.atp.YearFrac
 * JD-Core Version:    0.7.0.1
 */