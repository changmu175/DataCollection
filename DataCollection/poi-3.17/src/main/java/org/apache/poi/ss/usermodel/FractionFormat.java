/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ import java.text.FieldPosition;
/*   4:    */ import java.text.Format;
/*   5:    */ import java.text.ParsePosition;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import java.util.regex.Pattern;
/*   8:    */ import org.apache.poi.ss.format.SimpleFraction;
/*   9:    */ import org.apache.poi.ss.formula.eval.NotImplementedException;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ public class FractionFormat
/*  14:    */   extends Format
/*  15:    */ {
/*  16: 44 */   private static final POILogger LOGGER = POILogFactory.getLogger(FractionFormat.class);
/*  17: 45 */   private static final Pattern DENOM_FORMAT_PATTERN = Pattern.compile("(?:(#+)|(\\d+))");
/*  18:    */   private static final int MAX_DENOM_POW = 4;
/*  19:    */   private final int exactDenom;
/*  20:    */   private final int maxDenom;
/*  21:    */   private final String wholePartFormatString;
/*  22:    */   
/*  23:    */   public FractionFormat(String wholePartFormatString, String denomFormatString)
/*  24:    */   {
/*  25: 67 */     this.wholePartFormatString = wholePartFormatString;
/*  26:    */     
/*  27: 69 */     Matcher m = DENOM_FORMAT_PATTERN.matcher(denomFormatString);
/*  28: 70 */     int tmpExact = -1;
/*  29: 71 */     int tmpMax = -1;
/*  30: 72 */     if (m.find()) {
/*  31: 73 */       if (m.group(2) != null)
/*  32:    */       {
/*  33:    */         try
/*  34:    */         {
/*  35: 75 */           tmpExact = Integer.parseInt(m.group(2));
/*  36: 78 */           if (tmpExact == 0) {
/*  37: 79 */             tmpExact = -1;
/*  38:    */           }
/*  39:    */         }
/*  40:    */         catch (NumberFormatException e) {}
/*  41:    */       }
/*  42: 84 */       else if (m.group(1) != null)
/*  43:    */       {
/*  44: 85 */         int len = m.group(1).length();
/*  45: 86 */         len = len > 4 ? 4 : len;
/*  46: 87 */         tmpMax = (int)Math.pow(10.0D, len);
/*  47:    */       }
/*  48:    */       else
/*  49:    */       {
/*  50: 89 */         tmpExact = 100;
/*  51:    */       }
/*  52:    */     }
/*  53: 92 */     if ((tmpExact <= 0) && (tmpMax <= 0)) {
/*  54: 94 */       tmpExact = 100;
/*  55:    */     }
/*  56: 96 */     this.exactDenom = tmpExact;
/*  57: 97 */     this.maxDenom = tmpMax;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String format(Number num)
/*  61:    */   {
/*  62:102 */     double doubleValue = num.doubleValue();
/*  63:    */     
/*  64:104 */     boolean isNeg = doubleValue < 0.0D;
/*  65:105 */     double absDoubleValue = Math.abs(doubleValue);
/*  66:    */     
/*  67:107 */     double wholePart = Math.floor(absDoubleValue);
/*  68:108 */     double decPart = absDoubleValue - wholePart;
/*  69:109 */     if (wholePart + decPart == 0.0D) {
/*  70:110 */       return "0";
/*  71:    */     }
/*  72:122 */     if (Double.compare(decPart, 0.0D) == 0)
/*  73:    */     {
/*  74:124 */       StringBuilder sb = new StringBuilder();
/*  75:125 */       if (isNeg) {
/*  76:126 */         sb.append("-");
/*  77:    */       }
/*  78:128 */       sb.append((int)wholePart);
/*  79:129 */       return sb.toString();
/*  80:    */     }
/*  81:132 */     SimpleFraction fract = null;
/*  82:    */     try
/*  83:    */     {
/*  84:135 */       if (this.exactDenom > 0) {
/*  85:136 */         fract = SimpleFraction.buildFractionExactDenominator(decPart, this.exactDenom);
/*  86:    */       } else {
/*  87:138 */         fract = SimpleFraction.buildFractionMaxDenominator(decPart, this.maxDenom);
/*  88:    */       }
/*  89:    */     }
/*  90:    */     catch (RuntimeException e)
/*  91:    */     {
/*  92:141 */       LOGGER.log(5, new Object[] { "Can't format fraction", e });
/*  93:142 */       return Double.toString(doubleValue);
/*  94:    */     }
/*  95:145 */     StringBuilder sb = new StringBuilder();
/*  96:148 */     if (isNeg) {
/*  97:149 */       sb.append("-");
/*  98:    */     }
/*  99:153 */     if ("".equals(this.wholePartFormatString))
/* 100:    */     {
/* 101:154 */       int trueNum = fract.getDenominator() * (int)wholePart + fract.getNumerator();
/* 102:155 */       sb.append(trueNum).append("/").append(fract.getDenominator());
/* 103:156 */       return sb.toString();
/* 104:    */     }
/* 105:161 */     if (fract.getNumerator() == 0)
/* 106:    */     {
/* 107:162 */       sb.append(Integer.toString((int)wholePart));
/* 108:163 */       return sb.toString();
/* 109:    */     }
/* 110:164 */     if (fract.getNumerator() == fract.getDenominator())
/* 111:    */     {
/* 112:165 */       sb.append(Integer.toString((int)wholePart + 1));
/* 113:166 */       return sb.toString();
/* 114:    */     }
/* 115:169 */     if (wholePart > 0.0D) {
/* 116:170 */       sb.append(Integer.toString((int)wholePart)).append(" ");
/* 117:    */     }
/* 118:172 */     sb.append(fract.getNumerator()).append("/").append(fract.getDenominator());
/* 119:173 */     return sb.toString();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/* 123:    */   {
/* 124:177 */     return toAppendTo.append(format((Number)obj));
/* 125:    */   }
/* 126:    */   
/* 127:    */   public Object parseObject(String source, ParsePosition pos)
/* 128:    */   {
/* 129:181 */     throw new NotImplementedException("Reverse parsing not supported");
/* 130:    */   }
/* 131:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.FractionFormat
 * JD-Core Version:    0.7.0.1
 */