/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ 
/*   6:    */ final class NormalisedDecimal
/*   7:    */ {
/*   8:    */   private static final int EXPONENT_OFFSET = 14;
/*   9: 51 */   private static final BigDecimal BD_2_POW_24 = new BigDecimal(BigInteger.ONE.shiftLeft(24));
/*  10:    */   private static final int LOG_BASE_10_OF_2_TIMES_2_POW_20 = 315653;
/*  11:    */   private static final int C_2_POW_19 = 524288;
/*  12:    */   private static final int FRAC_HALF = 8388608;
/*  13:    */   private static final long MAX_REP_WHOLE_PART = 1000000000000000L;
/*  14:    */   private final int _relativeDecimalExponent;
/*  15:    */   private final long _wholePart;
/*  16:    */   private final int _fractionalPart;
/*  17:    */   
/*  18:    */   public static NormalisedDecimal create(BigInteger frac, int binaryExponent)
/*  19:    */   {
/*  20:    */     int pow10;
/*  21:    */     int pow10;
/*  22: 79 */     if ((binaryExponent > 49) || (binaryExponent < 46))
/*  23:    */     {
/*  24: 83 */       int x = 15204352 - binaryExponent * 315653;
/*  25: 84 */       x += 524288;
/*  26: 85 */       pow10 = -(x >> 20);
/*  27:    */     }
/*  28:    */     else
/*  29:    */     {
/*  30: 87 */       pow10 = 0;
/*  31:    */     }
/*  32: 89 */     MutableFPNumber cc = new MutableFPNumber(frac, binaryExponent);
/*  33: 90 */     if (pow10 != 0) {
/*  34: 91 */       cc.multiplyByPowerOfTen(-pow10);
/*  35:    */     }
/*  36: 94 */     switch (cc.get64BitNormalisedExponent())
/*  37:    */     {
/*  38:    */     case 46: 
/*  39: 96 */       if (cc.isAboveMinRep()) {
/*  40:    */         break;
/*  41:    */       }
/*  42:    */     case 44: 
/*  43:    */     case 45: 
/*  44:101 */       cc.multiplyByPowerOfTen(1);
/*  45:102 */       pow10--;
/*  46:103 */       break;
/*  47:    */     case 47: 
/*  48:    */     case 48: 
/*  49:    */       break;
/*  50:    */     case 49: 
/*  51:108 */       if (cc.isBelowMaxRep()) {
/*  52:    */         break;
/*  53:    */       }
/*  54:    */     case 50: 
/*  55:112 */       cc.multiplyByPowerOfTen(-1);
/*  56:113 */       pow10++;
/*  57:114 */       break;
/*  58:    */     default: 
/*  59:117 */       throw new IllegalStateException("Bad binary exp " + cc.get64BitNormalisedExponent() + ".");
/*  60:    */     }
/*  61:119 */     cc.normalise64bit();
/*  62:    */     
/*  63:121 */     return cc.createNormalisedDecimal(pow10);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public NormalisedDecimal roundUnits()
/*  67:    */   {
/*  68:128 */     long wholePart = this._wholePart;
/*  69:129 */     if (this._fractionalPart >= 8388608) {
/*  70:130 */       wholePart += 1L;
/*  71:    */     }
/*  72:133 */     int de = this._relativeDecimalExponent;
/*  73:135 */     if (wholePart < 1000000000000000L) {
/*  74:136 */       return new NormalisedDecimal(wholePart, 0, de);
/*  75:    */     }
/*  76:138 */     return new NormalisedDecimal(wholePart / 10L, 0, de + 1);
/*  77:    */   }
/*  78:    */   
/*  79:    */   NormalisedDecimal(long wholePart, int fracPart, int decimalExponent)
/*  80:    */   {
/*  81:160 */     this._wholePart = wholePart;
/*  82:161 */     this._fractionalPart = fracPart;
/*  83:162 */     this._relativeDecimalExponent = decimalExponent;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ExpandedDouble normaliseBaseTwo()
/*  87:    */   {
/*  88:177 */     MutableFPNumber cc = new MutableFPNumber(composeFrac(), 39);
/*  89:178 */     cc.multiplyByPowerOfTen(this._relativeDecimalExponent);
/*  90:179 */     cc.normalise64bit();
/*  91:180 */     return cc.createExpandedDouble();
/*  92:    */   }
/*  93:    */   
/*  94:    */   BigInteger composeFrac()
/*  95:    */   {
/*  96:187 */     long wp = this._wholePart;
/*  97:188 */     int fp = this._fractionalPart;
/*  98:189 */     return new BigInteger(new byte[] { (byte)(int)(wp >> 56), (byte)(int)(wp >> 48), (byte)(int)(wp >> 40), (byte)(int)(wp >> 32), (byte)(int)(wp >> 24), (byte)(int)(wp >> 16), (byte)(int)(wp >> 8), (byte)(int)(wp >> 0), (byte)(fp >> 16), (byte)(fp >> 8), (byte)(fp >> 0) });
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getSignificantDecimalDigits()
/* 102:    */   {
/* 103:205 */     return Long.toString(this._wholePart);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public String getSignificantDecimalDigitsLastDigitRounded()
/* 107:    */   {
/* 108:213 */     long wp = this._wholePart + 5L;
/* 109:214 */     StringBuilder sb = new StringBuilder(24);
/* 110:215 */     sb.append(wp);
/* 111:216 */     sb.setCharAt(sb.length() - 1, '0');
/* 112:217 */     return sb.toString();
/* 113:    */   }
/* 114:    */   
/* 115:    */   public int getDecimalExponent()
/* 116:    */   {
/* 117:224 */     return this._relativeDecimalExponent + 14;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public int compareNormalised(NormalisedDecimal other)
/* 121:    */   {
/* 122:231 */     int cmp = this._relativeDecimalExponent - other._relativeDecimalExponent;
/* 123:232 */     if (cmp != 0) {
/* 124:233 */       return cmp;
/* 125:    */     }
/* 126:235 */     if (this._wholePart > other._wholePart) {
/* 127:236 */       return 1;
/* 128:    */     }
/* 129:238 */     if (this._wholePart < other._wholePart) {
/* 130:239 */       return -1;
/* 131:    */     }
/* 132:241 */     return this._fractionalPart - other._fractionalPart;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public BigDecimal getFractionalPart()
/* 136:    */   {
/* 137:244 */     return new BigDecimal(this._fractionalPart).divide(BD_2_POW_24);
/* 138:    */   }
/* 139:    */   
/* 140:    */   private String getFractionalDigits()
/* 141:    */   {
/* 142:248 */     if (this._fractionalPart == 0) {
/* 143:249 */       return "0";
/* 144:    */     }
/* 145:251 */     return getFractionalPart().toString().substring(2);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public String toString()
/* 149:    */   {
/* 150:257 */     StringBuilder sb = new StringBuilder();
/* 151:258 */     sb.append(getClass().getName());
/* 152:259 */     sb.append(" [");
/* 153:260 */     String ws = String.valueOf(this._wholePart);
/* 154:261 */     sb.append(ws.charAt(0));
/* 155:262 */     sb.append('.');
/* 156:263 */     sb.append(ws.substring(1));
/* 157:264 */     sb.append(' ');
/* 158:265 */     sb.append(getFractionalDigits());
/* 159:266 */     sb.append("E");
/* 160:267 */     sb.append(getDecimalExponent());
/* 161:268 */     sb.append("]");
/* 162:269 */     return sb.toString();
/* 163:    */   }
/* 164:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.NormalisedDecimal
 * JD-Core Version:    0.7.0.1
 */