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
/*  11:    */ public class Complex
/*  12:    */   extends Var2or3ArgFunction
/*  13:    */   implements FreeRefFunction
/*  14:    */ {
/*  15: 59 */   public static final FreeRefFunction instance = new Complex();
/*  16:    */   public static final String DEFAULT_SUFFIX = "i";
/*  17:    */   public static final String SUPPORTED_SUFFIX = "j";
/*  18:    */   
/*  19:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval real_num, ValueEval i_num)
/*  20:    */   {
/*  21: 65 */     return evaluate(srcRowIndex, srcColumnIndex, real_num, i_num, new StringEval("i"));
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval real_num, ValueEval i_num, ValueEval suffix)
/*  25:    */   {
/*  26:    */     ValueEval veText1;
/*  27:    */     try
/*  28:    */     {
/*  29: 71 */       veText1 = OperandResolver.getSingleValue(real_num, srcRowIndex, srcColumnIndex);
/*  30:    */     }
/*  31:    */     catch (EvaluationException e)
/*  32:    */     {
/*  33: 73 */       return e.getErrorEval();
/*  34:    */     }
/*  35: 75 */     double realNum = 0.0D;
/*  36:    */     try
/*  37:    */     {
/*  38: 77 */       realNum = OperandResolver.coerceValueToDouble(veText1);
/*  39:    */     }
/*  40:    */     catch (EvaluationException e)
/*  41:    */     {
/*  42: 79 */       return ErrorEval.VALUE_INVALID;
/*  43:    */     }
/*  44:    */     ValueEval veINum;
/*  45:    */     try
/*  46:    */     {
/*  47: 84 */       veINum = OperandResolver.getSingleValue(i_num, srcRowIndex, srcColumnIndex);
/*  48:    */     }
/*  49:    */     catch (EvaluationException e)
/*  50:    */     {
/*  51: 86 */       return e.getErrorEval();
/*  52:    */     }
/*  53: 88 */     double realINum = 0.0D;
/*  54:    */     try
/*  55:    */     {
/*  56: 90 */       realINum = OperandResolver.coerceValueToDouble(veINum);
/*  57:    */     }
/*  58:    */     catch (EvaluationException e)
/*  59:    */     {
/*  60: 92 */       return ErrorEval.VALUE_INVALID;
/*  61:    */     }
/*  62: 95 */     String suffixValue = OperandResolver.coerceValueToString(suffix);
/*  63: 96 */     if (suffixValue.length() == 0) {
/*  64: 97 */       suffixValue = "i";
/*  65:    */     }
/*  66: 99 */     if ((suffixValue.equals("i".toUpperCase(Locale.ROOT))) || (suffixValue.equals("j".toUpperCase(Locale.ROOT)))) {
/*  67:101 */       return ErrorEval.VALUE_INVALID;
/*  68:    */     }
/*  69:103 */     if ((!suffixValue.equals("i")) && (!suffixValue.equals("j"))) {
/*  70:104 */       return ErrorEval.VALUE_INVALID;
/*  71:    */     }
/*  72:107 */     StringBuffer strb = new StringBuffer("");
/*  73:108 */     if (realNum != 0.0D) {
/*  74:109 */       if (isDoubleAnInt(realNum)) {
/*  75:110 */         strb.append((int)realNum);
/*  76:    */       } else {
/*  77:112 */         strb.append(realNum);
/*  78:    */       }
/*  79:    */     }
/*  80:115 */     if (realINum != 0.0D)
/*  81:    */     {
/*  82:116 */       if ((strb.length() != 0) && 
/*  83:117 */         (realINum > 0.0D)) {
/*  84:118 */         strb.append("+");
/*  85:    */       }
/*  86:122 */       if ((realINum != 1.0D) && (realINum != -1.0D)) {
/*  87:123 */         if (isDoubleAnInt(realINum)) {
/*  88:124 */           strb.append((int)realINum);
/*  89:    */         } else {
/*  90:126 */           strb.append(realINum);
/*  91:    */         }
/*  92:    */       }
/*  93:130 */       strb.append(suffixValue);
/*  94:    */     }
/*  95:133 */     return new StringEval(strb.toString());
/*  96:    */   }
/*  97:    */   
/*  98:    */   private boolean isDoubleAnInt(double number)
/*  99:    */   {
/* 100:137 */     return (number == Math.floor(number)) && (!Double.isInfinite(number));
/* 101:    */   }
/* 102:    */   
/* 103:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 104:    */   {
/* 105:141 */     if (args.length == 2) {
/* 106:142 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
/* 107:    */     }
/* 108:144 */     if (args.length == 3) {
/* 109:145 */       return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1], args[2]);
/* 110:    */     }
/* 111:148 */     return ErrorEval.VALUE_INVALID;
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Complex
 * JD-Core Version:    0.7.0.1
 */