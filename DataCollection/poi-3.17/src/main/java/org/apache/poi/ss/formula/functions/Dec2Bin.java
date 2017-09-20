/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public class Dec2Bin
/*  11:    */   extends Var1or2ArgFunction
/*  12:    */   implements FreeRefFunction
/*  13:    */ {
/*  14: 53 */   public static final FreeRefFunction instance = new Dec2Bin();
/*  15:    */   private static final long MIN_VALUE = -512L;
/*  16:    */   private static final long MAX_VALUE = 511L;
/*  17:    */   private static final int DEFAULT_PLACES_VALUE = 10;
/*  18:    */   
/*  19:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE, ValueEval placesVE)
/*  20:    */   {
/*  21:    */     ValueEval veText1;
/*  22:    */     try
/*  23:    */     {
/*  24: 62 */       veText1 = OperandResolver.getSingleValue(numberVE, srcRowIndex, srcColumnIndex);
/*  25:    */     }
/*  26:    */     catch (EvaluationException e)
/*  27:    */     {
/*  28: 64 */       return e.getErrorEval();
/*  29:    */     }
/*  30: 66 */     String strText1 = OperandResolver.coerceValueToString(veText1);
/*  31: 67 */     Double number = OperandResolver.parseDouble(strText1);
/*  32: 70 */     if (number == null) {
/*  33: 71 */       return ErrorEval.VALUE_INVALID;
/*  34:    */     }
/*  35: 75 */     if ((number.longValue() < -512L) || (number.longValue() > 511L)) {
/*  36: 76 */       return ErrorEval.NUM_ERROR;
/*  37:    */     }
/*  38:    */     int placesNumber;
/*  39:    */     int placesNumber;
/*  40: 80 */     if ((number.doubleValue() < 0.0D) || (placesVE == null))
/*  41:    */     {
/*  42: 81 */       placesNumber = 10;
/*  43:    */     }
/*  44:    */     else
/*  45:    */     {
/*  46:    */       ValueEval placesValueEval;
/*  47:    */       try
/*  48:    */       {
/*  49: 85 */         placesValueEval = OperandResolver.getSingleValue(placesVE, srcRowIndex, srcColumnIndex);
/*  50:    */       }
/*  51:    */       catch (EvaluationException e)
/*  52:    */       {
/*  53: 87 */         return e.getErrorEval();
/*  54:    */       }
/*  55: 89 */       String placesStr = OperandResolver.coerceValueToString(placesValueEval);
/*  56: 90 */       Double placesNumberDouble = OperandResolver.parseDouble(placesStr);
/*  57: 93 */       if (placesNumberDouble == null) {
/*  58: 94 */         return ErrorEval.VALUE_INVALID;
/*  59:    */       }
/*  60: 98 */       placesNumber = placesNumberDouble.intValue();
/*  61:100 */       if ((placesNumber < 0) || (placesNumber == 0)) {
/*  62:101 */         return ErrorEval.NUM_ERROR;
/*  63:    */       }
/*  64:    */     }
/*  65:104 */     String binary = Integer.toBinaryString(number.intValue());
/*  66:106 */     if (binary.length() > 10) {
/*  67:107 */       binary = binary.substring(binary.length() - 10, binary.length());
/*  68:    */     }
/*  69:110 */     if (binary.length() > placesNumber) {
/*  70:111 */       return ErrorEval.NUM_ERROR;
/*  71:    */     }
/*  72:114 */     return new StringEval(binary);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE)
/*  76:    */   {
/*  77:118 */     return evaluate(srcRowIndex, srcColumnIndex, numberVE, null);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  81:    */   {
/*  82:122 */     if (args.length == 1) {
/*  83:123 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/*  84:    */     }
/*  85:125 */     if (args.length == 2) {
/*  86:126 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/*  87:    */     }
/*  88:129 */     return ErrorEval.VALUE_INVALID;
/*  89:    */   }
/*  90:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Dec2Bin
 * JD-Core Version:    0.7.0.1
 */