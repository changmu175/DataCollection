/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.RoundingMode;
/*   5:    */ import java.text.DecimalFormat;
/*   6:    */ import java.text.NumberFormat;
/*   7:    */ import java.util.Locale;
/*   8:    */ import org.apache.poi.ss.formula.eval.BoolEval;
/*   9:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  11:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*  12:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  13:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  14:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  15:    */ 
/*  16:    */ public final class Fixed
/*  17:    */   implements Function1Arg, Function2Arg, Function3Arg
/*  18:    */ {
/*  19:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2)
/*  20:    */   {
/*  21: 39 */     return fixed(arg0, arg1, arg2, srcRowIndex, srcColumnIndex);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1)
/*  25:    */   {
/*  26: 45 */     return fixed(arg0, arg1, BoolEval.FALSE, srcRowIndex, srcColumnIndex);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  30:    */   {
/*  31: 50 */     return fixed(arg0, new NumberEval(2.0D), BoolEval.FALSE, srcRowIndex, srcColumnIndex);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex)
/*  35:    */   {
/*  36: 55 */     switch (args.length)
/*  37:    */     {
/*  38:    */     case 1: 
/*  39: 57 */       return fixed(args[0], new NumberEval(2.0D), BoolEval.FALSE, srcRowIndex, srcColumnIndex);
/*  40:    */     case 2: 
/*  41: 60 */       return fixed(args[0], args[1], BoolEval.FALSE, srcRowIndex, srcColumnIndex);
/*  42:    */     case 3: 
/*  43: 63 */       return fixed(args[0], args[1], args[2], srcRowIndex, srcColumnIndex);
/*  44:    */     }
/*  45: 65 */     return ErrorEval.VALUE_INVALID;
/*  46:    */   }
/*  47:    */   
/*  48:    */   private ValueEval fixed(ValueEval numberParam, ValueEval placesParam, ValueEval skipThousandsSeparatorParam, int srcRowIndex, int srcColumnIndex)
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52: 73 */       ValueEval numberValueEval = OperandResolver.getSingleValue(numberParam, srcRowIndex, srcColumnIndex);
/*  53:    */       
/*  54:    */ 
/*  55: 76 */       BigDecimal number = new BigDecimal(OperandResolver.coerceValueToDouble(numberValueEval));
/*  56:    */       
/*  57: 78 */       ValueEval placesValueEval = OperandResolver.getSingleValue(placesParam, srcRowIndex, srcColumnIndex);
/*  58:    */       
/*  59:    */ 
/*  60: 81 */       int places = OperandResolver.coerceValueToInt(placesValueEval);
/*  61: 82 */       ValueEval skipThousandsSeparatorValueEval = OperandResolver.getSingleValue(skipThousandsSeparatorParam, srcRowIndex, srcColumnIndex);
/*  62:    */       
/*  63:    */ 
/*  64: 85 */       Boolean skipThousandsSeparator = OperandResolver.coerceValueToBoolean(skipThousandsSeparatorValueEval, false);
/*  65:    */       
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69: 90 */       number = number.setScale(places, RoundingMode.HALF_UP);
/*  70:    */       
/*  71:    */ 
/*  72: 93 */       NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
/*  73: 94 */       DecimalFormat formatter = (DecimalFormat)nf;
/*  74: 95 */       formatter.setGroupingUsed((skipThousandsSeparator == null) || (!skipThousandsSeparator.booleanValue()));
/*  75: 96 */       formatter.setMinimumFractionDigits(places >= 0 ? places : 0);
/*  76: 97 */       formatter.setMaximumFractionDigits(places >= 0 ? places : 0);
/*  77: 98 */       String numberString = formatter.format(number.doubleValue());
/*  78:    */       
/*  79:    */ 
/*  80:101 */       return new StringEval(numberString);
/*  81:    */     }
/*  82:    */     catch (EvaluationException e)
/*  83:    */     {
/*  84:103 */       return e.getErrorEval();
/*  85:    */     }
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Fixed
 * JD-Core Version:    0.7.0.1
 */