/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ 
/*   5:    */ final class MutableFPNumber
/*   6:    */ {
/*   7: 37 */   private static final BigInteger BI_MIN_BASE = new BigInteger("0B5E620F47FFFE666", 16);
/*   8: 43 */   private static final BigInteger BI_MAX_BASE = new BigInteger("0E35FA9319FFFE000", 16);
/*   9:    */   private static final int C_64 = 64;
/*  10:    */   private static final int MIN_PRECISION = 72;
/*  11:    */   private BigInteger _significand;
/*  12:    */   private int _binaryExponent;
/*  13:    */   
/*  14:    */   public MutableFPNumber(BigInteger frac, int binaryExponent)
/*  15:    */   {
/*  16: 57 */     this._significand = frac;
/*  17: 58 */     this._binaryExponent = binaryExponent;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public MutableFPNumber copy()
/*  21:    */   {
/*  22: 63 */     return new MutableFPNumber(this._significand, this._binaryExponent);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void normalise64bit()
/*  26:    */   {
/*  27: 66 */     int oldBitLen = this._significand.bitLength();
/*  28: 67 */     int sc = oldBitLen - 64;
/*  29: 68 */     if (sc == 0) {
/*  30: 69 */       return;
/*  31:    */     }
/*  32: 71 */     if (sc < 0) {
/*  33: 72 */       throw new IllegalStateException("Not enough precision");
/*  34:    */     }
/*  35: 74 */     this._binaryExponent += sc;
/*  36: 75 */     if (sc > 32)
/*  37:    */     {
/*  38: 76 */       int highShift = sc - 1 & 0xFFFFE0;
/*  39: 77 */       this._significand = this._significand.shiftRight(highShift);
/*  40: 78 */       sc -= highShift;
/*  41: 79 */       oldBitLen -= highShift;
/*  42:    */     }
/*  43: 81 */     if (sc < 1) {
/*  44: 82 */       throw new IllegalStateException();
/*  45:    */     }
/*  46: 84 */     this._significand = Rounder.round(this._significand, sc);
/*  47: 85 */     if (this._significand.bitLength() > oldBitLen)
/*  48:    */     {
/*  49: 86 */       sc++;
/*  50: 87 */       this._binaryExponent += 1;
/*  51:    */     }
/*  52: 89 */     this._significand = this._significand.shiftRight(sc);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int get64BitNormalisedExponent()
/*  56:    */   {
/*  57: 92 */     return this._binaryExponent + this._significand.bitLength() - 64;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean isBelowMaxRep()
/*  61:    */   {
/*  62: 97 */     int sc = this._significand.bitLength() - 64;
/*  63: 98 */     return this._significand.compareTo(BI_MAX_BASE.shiftLeft(sc)) < 0;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isAboveMinRep()
/*  67:    */   {
/*  68:101 */     int sc = this._significand.bitLength() - 64;
/*  69:102 */     return this._significand.compareTo(BI_MIN_BASE.shiftLeft(sc)) > 0;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public NormalisedDecimal createNormalisedDecimal(int pow10)
/*  73:    */   {
/*  74:106 */     int missingUnderBits = this._binaryExponent - 39;
/*  75:107 */     int fracPart = this._significand.intValue() << missingUnderBits & 0xFFFF80;
/*  76:108 */     long wholePart = this._significand.shiftRight(64 - this._binaryExponent - 1).longValue();
/*  77:109 */     return new NormalisedDecimal(wholePart, fracPart, pow10);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void multiplyByPowerOfTen(int pow10)
/*  81:    */   {
/*  82:112 */     TenPower tp = TenPower.getInstance(Math.abs(pow10));
/*  83:113 */     if (pow10 < 0) {
/*  84:114 */       mulShift(tp._divisor, tp._divisorShift);
/*  85:    */     } else {
/*  86:116 */       mulShift(tp._multiplicand, tp._multiplierShift);
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   private void mulShift(BigInteger multiplicand, int multiplierShift)
/*  91:    */   {
/*  92:120 */     this._significand = this._significand.multiply(multiplicand);
/*  93:121 */     this._binaryExponent += multiplierShift;
/*  94:    */     
/*  95:123 */     int sc = this._significand.bitLength() - 72 & 0xFFFFFFE0;
/*  96:125 */     if (sc > 0)
/*  97:    */     {
/*  98:127 */       this._significand = this._significand.shiftRight(sc);
/*  99:128 */       this._binaryExponent += sc;
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static final class Rounder
/* 104:    */   {
/* 105:    */     private static final BigInteger[] HALF_BITS;
/* 106:    */     
/* 107:    */     static
/* 108:    */     {
/* 109:136 */       BigInteger[] bis = new BigInteger[33];
/* 110:137 */       long acc = 1L;
/* 111:138 */       for (int i = 1; i < bis.length; i++)
/* 112:    */       {
/* 113:139 */         bis[i] = BigInteger.valueOf(acc);
/* 114:140 */         acc <<= 1;
/* 115:    */       }
/* 116:142 */       HALF_BITS = bis;
/* 117:    */     }
/* 118:    */     
/* 119:    */     public static BigInteger round(BigInteger bi, int nBits)
/* 120:    */     {
/* 121:148 */       if (nBits < 1) {
/* 122:149 */         return bi;
/* 123:    */       }
/* 124:151 */       return bi.add(HALF_BITS[nBits]);
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   private static final class TenPower
/* 129:    */   {
/* 130:159 */     private static final BigInteger FIVE = new BigInteger("5");
/* 131:160 */     private static final TenPower[] _cache = new TenPower[350];
/* 132:    */     public final BigInteger _multiplicand;
/* 133:    */     public final BigInteger _divisor;
/* 134:    */     public final int _divisorShift;
/* 135:    */     public final int _multiplierShift;
/* 136:    */     
/* 137:    */     private TenPower(int index)
/* 138:    */     {
/* 139:168 */       BigInteger fivePowIndex = FIVE.pow(index);
/* 140:    */       
/* 141:170 */       int bitsDueToFiveFactors = fivePowIndex.bitLength();
/* 142:171 */       int px = 80 + bitsDueToFiveFactors;
/* 143:172 */       BigInteger fx = BigInteger.ONE.shiftLeft(px).divide(fivePowIndex);
/* 144:173 */       int adj = fx.bitLength() - 80;
/* 145:174 */       this._divisor = fx.shiftRight(adj);
/* 146:175 */       bitsDueToFiveFactors -= adj;
/* 147:    */       
/* 148:177 */       this._divisorShift = (-(bitsDueToFiveFactors + index + 80));
/* 149:178 */       int sc = fivePowIndex.bitLength() - 68;
/* 150:179 */       if (sc > 0)
/* 151:    */       {
/* 152:180 */         this._multiplierShift = (index + sc);
/* 153:181 */         this._multiplicand = fivePowIndex.shiftRight(sc);
/* 154:    */       }
/* 155:    */       else
/* 156:    */       {
/* 157:183 */         this._multiplierShift = index;
/* 158:184 */         this._multiplicand = fivePowIndex;
/* 159:    */       }
/* 160:    */     }
/* 161:    */     
/* 162:    */     static TenPower getInstance(int index)
/* 163:    */     {
/* 164:189 */       TenPower result = _cache[index];
/* 165:190 */       if (result == null)
/* 166:    */       {
/* 167:191 */         result = new TenPower(index);
/* 168:192 */         _cache[index] = result;
/* 169:    */       }
/* 170:194 */       return result;
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public ExpandedDouble createExpandedDouble()
/* 175:    */   {
/* 176:199 */     return new ExpandedDouble(this._significand, this._binaryExponent);
/* 177:    */   }
/* 178:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.MutableFPNumber
 * JD-Core Version:    0.7.0.1
 */