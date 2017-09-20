/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   6:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   7:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   8:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   9:    */ import org.apache.poi.ss.formula.eval.StringEval;
/*  10:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*  11:    */ 
/*  12:    */ public class Imaginary
/*  13:    */   extends Fixed1ArgFunction
/*  14:    */   implements FreeRefFunction
/*  15:    */ {
/*  16: 44 */   public static final FreeRefFunction instance = new Imaginary();
/*  17:    */   public static final String GROUP1_REAL_SIGN_REGEX = "([+-]?)";
/*  18:    */   public static final String GROUP2_REAL_INTEGER_OR_DOUBLE_REGEX = "([0-9]+\\.[0-9]+|[0-9]*)";
/*  19:    */   public static final String GROUP3_IMAGINARY_SIGN_REGEX = "([+-]?)";
/*  20:    */   public static final String GROUP4_IMAGINARY_INTEGER_OR_DOUBLE_REGEX = "([0-9]+\\.[0-9]+|[0-9]*)";
/*  21:    */   public static final String GROUP5_IMAGINARY_GROUP_REGEX = "([ij]?)";
/*  22: 52 */   public static final Pattern COMPLEX_NUMBER_PATTERN = Pattern.compile("([+-]?)([0-9]+\\.[0-9]+|[0-9]*)([+-]?)([0-9]+\\.[0-9]+|[0-9]*)([ij]?)");
/*  23:    */   public static final int GROUP1_REAL_SIGN = 1;
/*  24:    */   public static final int GROUP2_IMAGINARY_INTEGER_OR_DOUBLE = 2;
/*  25:    */   public static final int GROUP3_IMAGINARY_SIGN = 3;
/*  26:    */   public static final int GROUP4_IMAGINARY_INTEGER_OR_DOUBLE = 4;
/*  27:    */   
/*  28:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval inumberVE)
/*  29:    */   {
/*  30:    */     ValueEval veText1;
/*  31:    */     try
/*  32:    */     {
/*  33: 64 */       veText1 = OperandResolver.getSingleValue(inumberVE, srcRowIndex, srcColumnIndex);
/*  34:    */     }
/*  35:    */     catch (EvaluationException e)
/*  36:    */     {
/*  37: 66 */       return e.getErrorEval();
/*  38:    */     }
/*  39: 68 */     String iNumber = OperandResolver.coerceValueToString(veText1);
/*  40:    */     
/*  41: 70 */     Matcher m = COMPLEX_NUMBER_PATTERN.matcher(iNumber);
/*  42: 71 */     boolean result = m.matches();
/*  43:    */     
/*  44: 73 */     String imaginary = "";
/*  45: 74 */     if (result == true)
/*  46:    */     {
/*  47: 75 */       String imaginaryGroup = m.group(5);
/*  48: 76 */       boolean hasImaginaryPart = (imaginaryGroup.equals("i")) || (imaginaryGroup.equals("j"));
/*  49: 78 */       if (imaginaryGroup.length() == 0) {
/*  50: 79 */         return new StringEval(String.valueOf(0));
/*  51:    */       }
/*  52: 82 */       if (hasImaginaryPart)
/*  53:    */       {
/*  54: 83 */         String sign = "";
/*  55: 84 */         String imaginarySign = m.group(3);
/*  56: 85 */         if ((imaginarySign.length() != 0) && (!imaginarySign.equals("+"))) {
/*  57: 86 */           sign = imaginarySign;
/*  58:    */         }
/*  59: 89 */         String groupImaginaryNumber = m.group(4);
/*  60: 90 */         if (groupImaginaryNumber.length() != 0) {
/*  61: 91 */           imaginary = sign + groupImaginaryNumber;
/*  62:    */         } else {
/*  63: 93 */           imaginary = sign + "1";
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:    */     else
/*  68:    */     {
/*  69: 97 */       return ErrorEval.NUM_ERROR;
/*  70:    */     }
/*  71:100 */     return new StringEval(imaginary);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/*  75:    */   {
/*  76:104 */     if (args.length != 1) {
/*  77:105 */       return ErrorEval.VALUE_INVALID;
/*  78:    */     }
/*  79:107 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Imaginary
 * JD-Core Version:    0.7.0.1
 */