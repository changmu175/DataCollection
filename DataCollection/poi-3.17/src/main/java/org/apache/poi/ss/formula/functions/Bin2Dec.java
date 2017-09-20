/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.OperationEvaluationContext;
/*   4:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.RefEval;
/*   8:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   9:    */ 
/*  10:    */ public class Bin2Dec
/*  11:    */   extends Fixed1ArgFunction
/*  12:    */   implements FreeRefFunction
/*  13:    */ {
/*  14: 45 */   public static final FreeRefFunction instance = new Bin2Dec();
/*  15:    */   
/*  16:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE)
/*  17:    */   {
/*  18:    */     String number;
/*  19:    */     String number;
/*  20: 49 */     if ((numberVE instanceof RefEval))
/*  21:    */     {
/*  22: 50 */       RefEval re = (RefEval)numberVE;
/*  23: 51 */       number = OperandResolver.coerceValueToString(re.getInnerValueEval(re.getFirstSheetIndex()));
/*  24:    */     }
/*  25:    */     else
/*  26:    */     {
/*  27: 53 */       number = OperandResolver.coerceValueToString(numberVE);
/*  28:    */     }
/*  29: 55 */     if (number.length() > 10) {
/*  30: 56 */       return ErrorEval.NUM_ERROR;
/*  31:    */     }
/*  32:    */     boolean isPositive;
/*  33:    */     String unsigned;
/*  34:    */     boolean isPositive;
/*  35: 63 */     if (number.length() < 10)
/*  36:    */     {
/*  37: 64 */       String unsigned = number;
/*  38: 65 */       isPositive = true;
/*  39:    */     }
/*  40:    */     else
/*  41:    */     {
/*  42: 67 */       unsigned = number.substring(1);
/*  43: 68 */       isPositive = number.startsWith("0");
/*  44:    */     }
/*  45:    */     String value;
/*  46:    */     try
/*  47:    */     {
/*  48:    */       String value;
/*  49: 73 */       if (isPositive)
/*  50:    */       {
/*  51: 75 */         int sum = getDecimalValue(unsigned);
/*  52: 76 */         value = String.valueOf(sum);
/*  53:    */       }
/*  54:    */       else
/*  55:    */       {
/*  56: 80 */         String inverted = toggleBits(unsigned);
/*  57:    */         
/*  58: 82 */         int sum = getDecimalValue(inverted);
/*  59:    */         
/*  60:    */ 
/*  61: 85 */         sum++;
/*  62:    */         
/*  63: 87 */         value = "-" + sum;
/*  64:    */       }
/*  65:    */     }
/*  66:    */     catch (NumberFormatException e)
/*  67:    */     {
/*  68: 90 */       return ErrorEval.NUM_ERROR;
/*  69:    */     }
/*  70: 93 */     return new NumberEval(Long.parseLong(value));
/*  71:    */   }
/*  72:    */   
/*  73:    */   private int getDecimalValue(String unsigned)
/*  74:    */   {
/*  75: 97 */     int sum = 0;
/*  76: 98 */     int numBits = unsigned.length();
/*  77: 99 */     int power = numBits - 1;
/*  78:101 */     for (int i = 0; i < numBits; i++)
/*  79:    */     {
/*  80:102 */       int bit = Integer.parseInt(unsigned.substring(i, i + 1));
/*  81:103 */       int term = (int)(bit * Math.pow(2.0D, power));
/*  82:104 */       sum += term;
/*  83:105 */       power--;
/*  84:    */     }
/*  85:107 */     return sum;
/*  86:    */   }
/*  87:    */   
/*  88:    */   private static String toggleBits(String s)
/*  89:    */   {
/*  90:111 */     long i = Long.parseLong(s, 2);
/*  91:112 */     long i2 = i ^ (1L << s.length()) - 1L;
/*  92:113 */     String s2 = Long.toBinaryString(i2);
/*  93:114 */     while (s2.length() < s.length()) {
/*  94:114 */       s2 = '0' + s2;
/*  95:    */     }
/*  96:115 */     return s2;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec)
/* 100:    */   {
/* 101:119 */     if (args.length != 1) {
/* 102:120 */       return ErrorEval.VALUE_INVALID;
/* 103:    */     }
/* 104:122 */     return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
/* 105:    */   }
/* 106:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Bin2Dec
 * JD-Core Version:    0.7.0.1
 */