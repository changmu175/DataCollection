/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ public final class NumberToTextConverter
/*   4:    */ {
/*   5:    */   private static final long EXCEL_NAN_BITS = -276939487313920L;
/*   6:    */   private static final int MAX_TEXT_LEN = 20;
/*   7:    */   
/*   8:    */   public static String toText(double value)
/*   9:    */   {
/*  10:129 */     return rawDoubleBitsToText(Double.doubleToLongBits(value));
/*  11:    */   }
/*  12:    */   
/*  13:    */   static String rawDoubleBitsToText(long pRawBits)
/*  14:    */   {
/*  15:133 */     long rawBits = pRawBits;
/*  16:134 */     boolean isNegative = rawBits < 0L;
/*  17:135 */     if (isNegative) {
/*  18:136 */       rawBits &= 0xFFFFFFFF;
/*  19:    */     }
/*  20:138 */     if (rawBits == 0L) {
/*  21:139 */       return isNegative ? "-0" : "0";
/*  22:    */     }
/*  23:141 */     ExpandedDouble ed = new ExpandedDouble(rawBits);
/*  24:142 */     if (ed.getBinaryExponent() < -1022) {
/*  25:145 */       return isNegative ? "-0" : "0";
/*  26:    */     }
/*  27:147 */     if (ed.getBinaryExponent() == 1024)
/*  28:    */     {
/*  29:151 */       if (rawBits == -276939487313920L) {
/*  30:152 */         return "3.484840871308E+308";
/*  31:    */       }
/*  32:157 */       isNegative = false;
/*  33:    */     }
/*  34:159 */     NormalisedDecimal nd = ed.normaliseBaseTen();
/*  35:160 */     StringBuilder sb = new StringBuilder(21);
/*  36:161 */     if (isNegative) {
/*  37:162 */       sb.append('-');
/*  38:    */     }
/*  39:164 */     convertToText(sb, nd);
/*  40:165 */     return sb.toString();
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static void convertToText(StringBuilder sb, NormalisedDecimal pnd)
/*  44:    */   {
/*  45:168 */     NormalisedDecimal rnd = pnd.roundUnits();
/*  46:169 */     int decExponent = rnd.getDecimalExponent();
/*  47:    */     String decimalDigits;
/*  48:171 */     if (Math.abs(decExponent) > 98)
/*  49:    */     {
/*  50:172 */       String decimalDigits = rnd.getSignificantDecimalDigitsLastDigitRounded();
/*  51:173 */       if (decimalDigits.length() == 16) {
/*  52:175 */         decExponent++;
/*  53:    */       }
/*  54:    */     }
/*  55:    */     else
/*  56:    */     {
/*  57:178 */       decimalDigits = rnd.getSignificantDecimalDigits();
/*  58:    */     }
/*  59:180 */     int countSigDigits = countSignifantDigits(decimalDigits);
/*  60:181 */     if (decExponent < 0) {
/*  61:182 */       formatLessThanOne(sb, decimalDigits, decExponent, countSigDigits);
/*  62:    */     } else {
/*  63:184 */       formatGreaterThanOne(sb, decimalDigits, decExponent, countSigDigits);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   private static void formatLessThanOne(StringBuilder sb, String decimalDigits, int decExponent, int countSigDigits)
/*  68:    */   {
/*  69:190 */     int nLeadingZeros = -decExponent - 1;
/*  70:191 */     int normalLength = 2 + nLeadingZeros + countSigDigits;
/*  71:193 */     if (needsScientificNotation(normalLength))
/*  72:    */     {
/*  73:194 */       sb.append(decimalDigits.charAt(0));
/*  74:195 */       if (countSigDigits > 1)
/*  75:    */       {
/*  76:196 */         sb.append('.');
/*  77:197 */         sb.append(decimalDigits.subSequence(1, countSigDigits));
/*  78:    */       }
/*  79:199 */       sb.append("E-");
/*  80:200 */       appendExp(sb, -decExponent);
/*  81:201 */       return;
/*  82:    */     }
/*  83:203 */     sb.append("0.");
/*  84:204 */     for (int i = nLeadingZeros; i > 0; i--) {
/*  85:205 */       sb.append('0');
/*  86:    */     }
/*  87:207 */     sb.append(decimalDigits.subSequence(0, countSigDigits));
/*  88:    */   }
/*  89:    */   
/*  90:    */   private static void formatGreaterThanOne(StringBuilder sb, String decimalDigits, int decExponent, int countSigDigits)
/*  91:    */   {
/*  92:212 */     if (decExponent > 19)
/*  93:    */     {
/*  94:214 */       sb.append(decimalDigits.charAt(0));
/*  95:215 */       if (countSigDigits > 1)
/*  96:    */       {
/*  97:216 */         sb.append('.');
/*  98:217 */         sb.append(decimalDigits.subSequence(1, countSigDigits));
/*  99:    */       }
/* 100:219 */       sb.append("E+");
/* 101:220 */       appendExp(sb, decExponent);
/* 102:221 */       return;
/* 103:    */     }
/* 104:223 */     int nFractionalDigits = countSigDigits - decExponent - 1;
/* 105:224 */     if (nFractionalDigits > 0)
/* 106:    */     {
/* 107:225 */       sb.append(decimalDigits.subSequence(0, decExponent + 1));
/* 108:226 */       sb.append('.');
/* 109:227 */       sb.append(decimalDigits.subSequence(decExponent + 1, countSigDigits));
/* 110:228 */       return;
/* 111:    */     }
/* 112:230 */     sb.append(decimalDigits.subSequence(0, countSigDigits));
/* 113:231 */     for (int i = -nFractionalDigits; i > 0; i--) {
/* 114:232 */       sb.append('0');
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   private static boolean needsScientificNotation(int nDigits)
/* 119:    */   {
/* 120:237 */     return nDigits > 20;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static int countSignifantDigits(String sb)
/* 124:    */   {
/* 125:241 */     int result = sb.length() - 1;
/* 126:242 */     while (sb.charAt(result) == '0')
/* 127:    */     {
/* 128:243 */       result--;
/* 129:244 */       if (result < 0) {
/* 130:245 */         throw new RuntimeException("No non-zero digits found");
/* 131:    */       }
/* 132:    */     }
/* 133:248 */     return result + 1;
/* 134:    */   }
/* 135:    */   
/* 136:    */   private static void appendExp(StringBuilder sb, int val)
/* 137:    */   {
/* 138:252 */     if (val < 10)
/* 139:    */     {
/* 140:253 */       sb.append('0');
/* 141:254 */       sb.append((char)(48 + val));
/* 142:255 */       return;
/* 143:    */     }
/* 144:257 */     sb.append(val);
/* 145:    */   }
/* 146:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.NumberToTextConverter
 * JD-Core Version:    0.7.0.1
 */