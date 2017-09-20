/*   1:    */ package org.apache.poi.ss.formula.functions;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.RoundingMode;
/*   5:    */ import org.apache.poi.ss.util.NumberToTextConverter;
/*   6:    */ 
/*   7:    */ final class MathX
/*   8:    */ {
/*   9:    */   public static double round(double n, int p)
/*  10:    */   {
/*  11: 51 */     return round(n, p, RoundingMode.HALF_UP);
/*  12:    */   }
/*  13:    */   
/*  14:    */   public static double roundUp(double n, int p)
/*  15:    */   {
/*  16: 70 */     return round(n, p, RoundingMode.UP);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static double roundDown(double n, int p)
/*  20:    */   {
/*  21: 89 */     return round(n, p, RoundingMode.DOWN);
/*  22:    */   }
/*  23:    */   
/*  24:    */   private static double round(double n, int p, RoundingMode rounding)
/*  25:    */   {
/*  26: 93 */     if ((Double.isNaN(n)) || (Double.isInfinite(n))) {
/*  27: 94 */       return (0.0D / 0.0D);
/*  28:    */     }
/*  29: 97 */     String excelNumber = NumberToTextConverter.toText(n);
/*  30: 98 */     return new BigDecimal(excelNumber).setScale(p, rounding).doubleValue();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static short sign(double d)
/*  34:    */   {
/*  35:114 */     return (short)(d < 0.0D ? -1 : d == 0.0D ? 0 : 1);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static double average(double[] values)
/*  39:    */   {
/*  40:126 */     double ave = 0.0D;
/*  41:127 */     double sum = 0.0D;
/*  42:128 */     int i = 0;
/*  43:128 */     for (int iSize = values.length; i < iSize; i++) {
/*  44:129 */       sum += values[i];
/*  45:    */     }
/*  46:131 */     ave = sum / values.length;
/*  47:132 */     return ave;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static double sum(double[] values)
/*  51:    */   {
/*  52:141 */     double sum = 0.0D;
/*  53:142 */     int i = 0;
/*  54:142 */     for (int iSize = values.length; i < iSize; i++) {
/*  55:143 */       sum += values[i];
/*  56:    */     }
/*  57:145 */     return sum;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static double sumsq(double[] values)
/*  61:    */   {
/*  62:153 */     double sumsq = 0.0D;
/*  63:154 */     int i = 0;
/*  64:154 */     for (int iSize = values.length; i < iSize; i++) {
/*  65:155 */       sumsq += values[i] * values[i];
/*  66:    */     }
/*  67:157 */     return sumsq;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static double product(double[] values)
/*  71:    */   {
/*  72:166 */     double product = 0.0D;
/*  73:167 */     if ((values != null) && (values.length > 0))
/*  74:    */     {
/*  75:168 */       product = 1.0D;
/*  76:169 */       int i = 0;
/*  77:169 */       for (int iSize = values.length; i < iSize; i++) {
/*  78:170 */         product *= values[i];
/*  79:    */       }
/*  80:    */     }
/*  81:173 */     return product;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static double min(double[] values)
/*  85:    */   {
/*  86:182 */     double min = (1.0D / 0.0D);
/*  87:183 */     int i = 0;
/*  88:183 */     for (int iSize = values.length; i < iSize; i++) {
/*  89:184 */       min = Math.min(min, values[i]);
/*  90:    */     }
/*  91:186 */     return min;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static double max(double[] values)
/*  95:    */   {
/*  96:195 */     double max = (-1.0D / 0.0D);
/*  97:196 */     int i = 0;
/*  98:196 */     for (int iSize = values.length; i < iSize; i++) {
/*  99:197 */       max = Math.max(max, values[i]);
/* 100:    */     }
/* 101:199 */     return max;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static double floor(double n, double s)
/* 105:    */   {
/* 106:    */     double f;
/* 107:    */     double f;
/* 108:220 */     if (((n < 0.0D) && (s > 0.0D)) || ((n > 0.0D) && (s < 0.0D)) || ((s == 0.0D) && (n != 0.0D))) {
/* 109:221 */       f = (0.0D / 0.0D);
/* 110:    */     } else {
/* 111:224 */       f = (n == 0.0D) || (s == 0.0D) ? 0.0D : Math.floor(n / s) * s;
/* 112:    */     }
/* 113:227 */     return f;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static double ceiling(double n, double s)
/* 117:    */   {
/* 118:    */     double c;
/* 119:    */     double c;
/* 120:248 */     if (((n < 0.0D) && (s > 0.0D)) || ((n > 0.0D) && (s < 0.0D))) {
/* 121:249 */       c = (0.0D / 0.0D);
/* 122:    */     } else {
/* 123:252 */       c = (n == 0.0D) || (s == 0.0D) ? 0.0D : Math.ceil(n / s) * s;
/* 124:    */     }
/* 125:255 */     return c;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public static double factorial(int n)
/* 129:    */   {
/* 130:269 */     double d = 1.0D;
/* 131:271 */     if (n >= 0)
/* 132:    */     {
/* 133:272 */       if (n <= 170) {
/* 134:273 */         for (int i = 1; i <= n; i++) {
/* 135:274 */           d *= i;
/* 136:    */         }
/* 137:    */       } else {
/* 138:278 */         d = (1.0D / 0.0D);
/* 139:    */       }
/* 140:    */     }
/* 141:    */     else {
/* 142:282 */       d = (0.0D / 0.0D);
/* 143:    */     }
/* 144:284 */     return d;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static double mod(double n, double d)
/* 148:    */   {
/* 149:304 */     double result = 0.0D;
/* 150:306 */     if (d == 0.0D) {
/* 151:307 */       result = (0.0D / 0.0D);
/* 152:309 */     } else if (sign(n) == sign(d)) {
/* 153:310 */       result = n % d;
/* 154:    */     } else {
/* 155:313 */       result = (n % d + d) % d;
/* 156:    */     }
/* 157:316 */     return result;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public static double acosh(double d)
/* 161:    */   {
/* 162:324 */     return Math.log(Math.sqrt(Math.pow(d, 2.0D) - 1.0D) + d);
/* 163:    */   }
/* 164:    */   
/* 165:    */   public static double asinh(double d)
/* 166:    */   {
/* 167:332 */     return Math.log(Math.sqrt(d * d + 1.0D) + d);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static double atanh(double d)
/* 171:    */   {
/* 172:340 */     return Math.log((1.0D + d) / (1.0D - d)) / 2.0D;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static double cosh(double d)
/* 176:    */   {
/* 177:348 */     double ePowX = Math.pow(2.718281828459045D, d);
/* 178:349 */     double ePowNegX = Math.pow(2.718281828459045D, -d);
/* 179:350 */     return (ePowX + ePowNegX) / 2.0D;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static double sinh(double d)
/* 183:    */   {
/* 184:358 */     double ePowX = Math.pow(2.718281828459045D, d);
/* 185:359 */     double ePowNegX = Math.pow(2.718281828459045D, -d);
/* 186:360 */     return (ePowX - ePowNegX) / 2.0D;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public static double tanh(double d)
/* 190:    */   {
/* 191:368 */     double ePowX = Math.pow(2.718281828459045D, d);
/* 192:369 */     double ePowNegX = Math.pow(2.718281828459045D, -d);
/* 193:370 */     return (ePowX - ePowNegX) / (ePowX + ePowNegX);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public static double nChooseK(int n, int k)
/* 197:    */   {
/* 198:385 */     double d = 1.0D;
/* 199:386 */     if ((n < 0) || (k < 0) || (n < k))
/* 200:    */     {
/* 201:387 */       d = (0.0D / 0.0D);
/* 202:    */     }
/* 203:    */     else
/* 204:    */     {
/* 205:390 */       int minnk = Math.min(n - k, k);
/* 206:391 */       int maxnk = Math.max(n - k, k);
/* 207:392 */       for (int i = maxnk; i < n; i++) {
/* 208:393 */         d *= (i + 1);
/* 209:    */       }
/* 210:395 */       d /= factorial(minnk);
/* 211:    */     }
/* 212:398 */     return d;
/* 213:    */   }
/* 214:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.functions.MathX
 * JD-Core Version:    0.7.0.1
 */