/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*   4:    */ import org.apache.poi.ss.formula.eval.EvaluationException;
/*   5:    */ import org.apache.poi.ss.formula.eval.NumberEval;
/*   6:    */ import org.apache.poi.ss.formula.eval.OperandResolver;
/*   7:    */ import org.apache.poi.ss.formula.eval.ValueEval;
/*   8:    */ 
/*   9:    */ public final class Value
/*  10:    */   extends Fixed1ArgFunction
/*  11:    */ {
/*  12:    */   private static final int MIN_DISTANCE_BETWEEN_THOUSANDS_SEPARATOR = 4;
/*  13: 40 */   private static final Double ZERO = new Double(0.0D);
/*  14:    */   
/*  15:    */   public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0)
/*  16:    */   {
/*  17:    */     ValueEval veText;
/*  18:    */     try
/*  19:    */     {
/*  20: 45 */       veText = OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex);
/*  21:    */     }
/*  22:    */     catch (EvaluationException e)
/*  23:    */     {
/*  24: 47 */       return e.getErrorEval();
/*  25:    */     }
/*  26: 49 */     String strText = OperandResolver.coerceValueToString(veText);
/*  27: 50 */     Double result = convertTextToNumber(strText);
/*  28: 51 */     if (result == null) {
/*  29: 52 */       return ErrorEval.VALUE_INVALID;
/*  30:    */     }
/*  31: 54 */     return new NumberEval(result.doubleValue());
/*  32:    */   }
/*  33:    */   
/*  34:    */   private static Double convertTextToNumber(String strText)
/*  35:    */   {
/*  36: 63 */     boolean foundCurrency = false;
/*  37: 64 */     boolean foundUnaryPlus = false;
/*  38: 65 */     boolean foundUnaryMinus = false;
/*  39: 66 */     boolean foundPercentage = false;
/*  40:    */     
/*  41: 68 */     int len = strText.length();
/*  42: 70 */     for (int i = 0; i < len; i++)
/*  43:    */     {
/*  44: 71 */       char ch = strText.charAt(i);
/*  45: 72 */       if ((Character.isDigit(ch)) || (ch == '.')) {
/*  46:    */         break;
/*  47:    */       }
/*  48: 75 */       switch (ch)
/*  49:    */       {
/*  50:    */       case ' ': 
/*  51:    */         break;
/*  52:    */       case '$': 
/*  53: 80 */         if (foundCurrency) {
/*  54: 82 */           return null;
/*  55:    */         }
/*  56: 84 */         foundCurrency = true;
/*  57: 85 */         break;
/*  58:    */       case '+': 
/*  59: 87 */         if ((foundUnaryMinus) || (foundUnaryPlus)) {
/*  60: 88 */           return null;
/*  61:    */         }
/*  62: 90 */         foundUnaryPlus = true;
/*  63: 91 */         break;
/*  64:    */       case '-': 
/*  65: 93 */         if ((foundUnaryMinus) || (foundUnaryPlus)) {
/*  66: 94 */           return null;
/*  67:    */         }
/*  68: 96 */         foundUnaryMinus = true;
/*  69: 97 */         break;
/*  70:    */       default: 
/*  71:100 */         return null;
/*  72:    */       }
/*  73:    */     }
/*  74:103 */     if (i >= len)
/*  75:    */     {
/*  76:105 */       if ((foundCurrency) || (foundUnaryMinus) || (foundUnaryPlus)) {
/*  77:106 */         return null;
/*  78:    */       }
/*  79:108 */       return ZERO;
/*  80:    */     }
/*  81:113 */     boolean foundDecimalPoint = false;
/*  82:114 */     int lastThousandsSeparatorIndex = -32768;
/*  83:    */     
/*  84:116 */     StringBuffer sb = new StringBuffer(len);
/*  85:117 */     for (; i < len; i++)
/*  86:    */     {
/*  87:118 */       char ch = strText.charAt(i);
/*  88:119 */       if (Character.isDigit(ch)) {
/*  89:120 */         sb.append(ch);
/*  90:    */       } else {
/*  91:123 */         switch (ch)
/*  92:    */         {
/*  93:    */         case ' ': 
/*  94:125 */           String remainingTextTrimmed = strText.substring(i).trim();
/*  95:127 */           if (remainingTextTrimmed.equals("%")) {
/*  96:128 */             foundPercentage = true;
/*  97:131 */           } else if (remainingTextTrimmed.length() > 0) {
/*  98:133 */             return null;
/*  99:    */           }
/* 100:    */           break;
/* 101:    */         case '.': 
/* 102:137 */           if (foundDecimalPoint) {
/* 103:138 */             return null;
/* 104:    */           }
/* 105:140 */           if (i - lastThousandsSeparatorIndex < 4) {
/* 106:141 */             return null;
/* 107:    */           }
/* 108:143 */           foundDecimalPoint = true;
/* 109:144 */           sb.append('.');
/* 110:145 */           break;
/* 111:    */         case ',': 
/* 112:147 */           if (foundDecimalPoint) {
/* 113:149 */             return null;
/* 114:    */           }
/* 115:151 */           int distanceBetweenThousandsSeparators = i - lastThousandsSeparatorIndex;
/* 116:153 */           if (distanceBetweenThousandsSeparators < 4) {
/* 117:154 */             return null;
/* 118:    */           }
/* 119:156 */           lastThousandsSeparatorIndex = i;
/* 120:    */           
/* 121:158 */           break;
/* 122:    */         case 'E': 
/* 123:    */         case 'e': 
/* 124:162 */           if (i - lastThousandsSeparatorIndex < 4) {
/* 125:163 */             return null;
/* 126:    */           }
/* 127:166 */           sb.append(strText.substring(i));
/* 128:167 */           i = len;
/* 129:168 */           break;
/* 130:    */         case '%': 
/* 131:170 */           foundPercentage = true;
/* 132:171 */           break;
/* 133:    */         default: 
/* 134:174 */           return null;
/* 135:    */         }
/* 136:    */       }
/* 137:    */     }
/* 138:177 */     if ((!foundDecimalPoint) && 
/* 139:178 */       (i - lastThousandsSeparatorIndex < 4)) {
/* 140:179 */       return null;
/* 141:    */     }
/* 142:    */     double d;
/* 143:    */     try
/* 144:    */     {
/* 145:184 */       d = Double.parseDouble(sb.toString());
/* 146:    */     }
/* 147:    */     catch (NumberFormatException e)
/* 148:    */     {
/* 149:187 */       return null;
/* 150:    */     }
/* 151:189 */     double result = foundUnaryMinus ? -d : d;
/* 152:190 */     return Double.valueOf(foundPercentage ? result / 100.0D : result);
/* 153:    */   }
/* 154:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.Value
 * JD-Core Version:    0.7.0.1
 */