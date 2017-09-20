/*  1:   */ package org.apache.poi.ss.formula.functions;
/*  2:   */ 
/*  3:   */ import java.util.regex.Matcher;
/*  4:   */ import java.util.regex.Pattern;
/*  5:   */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*  6:   */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  7:   */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*  8:   */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*  9:   */ import org.apache.poi.ss.formula.eval.StringEval;
/* 10:   */ import org.apache.poi.ss.formula.eval.ValueEval;
/* 11:   */ 
/* 12:   */ public class ImReal
/* 13:   */   extends Fixed1ArgFunction
/* 14:   */   implements FreeRefFunction
/* 15:   */ {
/* 16:48 */   public static final FreeRefFunction instance = new ImReal();
/* 17:   */   
/* 18:   */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval inumberVE)
/* 19:   */   {
/* 20:   */     ValueEval veText1;
/* 21:   */     try
/* 22:   */     {
/* 23:53 */       veText1 = OperandResolver.getSingleValue(inumberVE, srcRowIndex, srcColumnIndex);
/* 24:   */     }
/* 25:   */     catch (EvaluationException e)
/* 26:   */     {
/* 27:55 */       return e.getErrorEval();
/* 28:   */     }
/* 29:57 */     String iNumber = OperandResolver.coerceValueToString(veText1);
/* 30:   */     
/* 31:59 */     Matcher m = Imaginary.COMPLEX_NUMBER_PATTERN.matcher(iNumber);
/* 32:60 */     boolean result = m.matches();
/* 33:   */     
/* 34:62 */     String real = "";
/* 35:63 */     if (result == true)
/* 36:   */     {
/* 37:64 */       String realGroup = m.group(2);
/* 38:65 */       boolean hasRealPart = realGroup.length() != 0;
/* 39:67 */       if (realGroup.length() == 0) {
/* 40:68 */         return new StringEval(String.valueOf(0));
/* 41:   */       }
/* 42:71 */       if (hasRealPart)
/* 43:   */       {
/* 44:72 */         String sign = "";
/* 45:73 */         String realSign = m.group(1);
/* 46:74 */         if ((realSign.length() != 0) && (!realSign.equals("+"))) {
/* 47:75 */           sign = realSign;
/* 48:   */         }
/* 49:78 */         String groupRealNumber = m.group(2);
/* 50:79 */         if (groupRealNumber.length() != 0) {
/* 51:80 */           real = sign + groupRealNumber;
/* 52:   */         } else {
/* 53:82 */           real = sign + "1";
/* 54:   */         }
/* 55:   */       }
/* 56:   */     }
/* 57:   */     else
/* 58:   */     {
/* 59:86 */       return ErrorEval.NUM_ERROR;
/* 60:   */     }
/* 61:89 */     return new StringEval(real);
/* 62:   */   }
/* 63:   */   
/* 64:   */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 65:   */   {
/* 66:93 */     if (args.length != 1) {
/* 67:94 */       return ErrorEval.VALUE_INVALID;
/* 68:   */     }
/* 69:96 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/* 70:   */   }
/* 71:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.ImReal
 * JD-Core Version:    0.7.0.1
 */