/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   5:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   7:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   8:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  10:    */ 
/*  11:    */ public final class Dec2Hex
/*  12:    */   extends Var1or2ArgFunction
/*  13:    */   implements FreeRefFunction
/*  14:    */ {
/*  15: 57 */   public static final FreeRefFunction instance = new Dec2Hex();
/*  16: 59 */   private static final long MIN_VALUE = Long.parseLong("-549755813888");
/*  17: 60 */   private static final long MAX_VALUE = Long.parseLong("549755813887");
/*  18:    */   private static final int DEFAULT_PLACES_VALUE = 10;
/*  19:    */   
/*  20:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval number, ValueEval places)
/*  21:    */   {
/*  22:    */     ValueEval veText1;
/*  23:    */     try
/*  24:    */     {
/*  25: 66 */       veText1 = OperandResolver.getSingleValue(number, srcRowIndex, srcColumnIndex);
/*  26:    */     }
/*  27:    */     catch (EvaluationException e)
/*  28:    */     {
/*  29: 68 */       return e.getErrorEval();
/*  30:    */     }
/*  31: 70 */     String strText1 = OperandResolver.coerceValueToString(veText1);
/*  32: 71 */     Double number1 = OperandResolver.parseDouble(strText1);
/*  33: 74 */     if (number1 == null) {
/*  34: 75 */       return ErrorEval.VALUE_INVALID;
/*  35:    */     }
/*  36: 79 */     if ((number1.longValue() < MIN_VALUE) || (number1.longValue() > MAX_VALUE)) {
/*  37: 80 */       return ErrorEval.NUM_ERROR;
/*  38:    */     }
/*  39: 83 */     int placesNumber = 0;
/*  40: 84 */     if (number1.doubleValue() < 0.0D)
/*  41:    */     {
/*  42: 85 */       placesNumber = 10;
/*  43:    */     }
/*  44: 87 */     else if (places != null)
/*  45:    */     {
/*  46:    */       ValueEval placesValueEval;
/*  47:    */       try
/*  48:    */       {
/*  49: 90 */         placesValueEval = OperandResolver.getSingleValue(places, srcRowIndex, srcColumnIndex);
/*  50:    */       }
/*  51:    */       catch (EvaluationException e)
/*  52:    */       {
/*  53: 92 */         return e.getErrorEval();
/*  54:    */       }
/*  55: 94 */       String placesStr = OperandResolver.coerceValueToString(placesValueEval);
/*  56: 95 */       Double placesNumberDouble = OperandResolver.parseDouble(placesStr);
/*  57: 98 */       if (placesNumberDouble == null) {
/*  58: 99 */         return ErrorEval.VALUE_INVALID;
/*  59:    */       }
/*  60:103 */       placesNumber = placesNumberDouble.intValue();
/*  61:105 */       if (placesNumber < 0) {
/*  62:106 */         return ErrorEval.NUM_ERROR;
/*  63:    */       }
/*  64:    */     }
/*  65:    */     String hex;
/*  66:    */     String hex;
/*  67:111 */     if (placesNumber != 0) {
/*  68:112 */       hex = String.format(Locale.ROOT, "%0" + placesNumber + "X", new Object[] { Integer.valueOf(number1.intValue()) });
/*  69:    */     } else {
/*  70:115 */       hex = Long.toHexString(number1.longValue());
/*  71:    */     }
/*  72:118 */     if (number1.doubleValue() < 0.0D) {
/*  73:119 */       hex = "FF" + hex.substring(2);
/*  74:    */     }
/*  75:122 */     return new StringEval(hex.toUpperCase(Locale.ROOT));
/*  76:    */   }
/*  77:    */   
/*  78:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  79:    */   {
/*  80:126 */     return evaluate(srcRowIndex, srcColumnIndex, arg0, null);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  84:    */   {
/*  85:130 */     if (args.length == 1) {
/*  86:131 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/*  87:    */     }
/*  88:133 */     if (args.length == 2) {
/*  89:134 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/*  90:    */     }
/*  91:136 */     return ErrorEval.VALUE_INVALID;
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Dec2Hex
 * JD-Core Version:    0.7.0.1
 */