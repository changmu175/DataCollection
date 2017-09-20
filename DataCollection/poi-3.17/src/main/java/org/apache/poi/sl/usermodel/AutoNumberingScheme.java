/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ 
/*   5:    */ public enum AutoNumberingScheme
/*   6:    */ {
/*   7: 24 */   alphaLcParenBoth(8, 1),  alphaUcParenBoth(10, 2),  alphaLcParenRight(9, 3),  alphaUcParenRight(11, 4),  alphaLcPeriod(0, 5),  alphaUcPeriod(1, 6),  arabicParenBoth(12, 7),  arabicParenRight(2, 8),  arabicPeriod(3, 9),  arabicPlain(13, 10),  romanLcParenBoth(4, 11),  romanUcParenBoth(14, 12),  romanLcParenRight(5, 13),  romanUcParenRight(15, 14),  romanLcPeriod(6, 15),  romanUcPeriod(7, 16),  circleNumDbPlain(18, 17),  circleNumWdBlackPlain(20, 18),  circleNumWdWhitePlain(19, 19),  arabicDbPeriod(29, 20),  arabicDbPlain(28, 21),  ea1ChsPeriod(17, 22),  ea1ChsPlain(16, 23),  ea1ChtPeriod(21, 24),  ea1ChtPlain(20, 25),  ea1JpnChsDbPeriod(38, 26),  ea1JpnKorPlain(26, 27),  ea1JpnKorPeriod(27, 28),  arabic1Minus(23, 29),  arabic2Minus(24, 30),  hebrew2Minus(25, 31),  thaiAlphaPeriod(30, 32),  thaiAlphaParenRight(31, 33),  thaiAlphaParenBoth(32, 34),  thaiNumPeriod(33, 35),  thaiNumParenRight(34, 36),  thaiNumParenBoth(35, 37),  hindiAlphaPeriod(36, 38),  hindiNumPeriod(37, 39),  hindiNumParenRight(39, 40),  hindiAlpha1Period(39, 41);
/*   8:    */   
/*   9:    */   public final int nativeId;
/*  10:    */   public final int ooxmlId;
/*  11:    */   private static final String ARABIC_LIST = "0123456789";
/*  12:    */   private static final String ALPHA_LIST = "abcdefghijklmnopqrstuvwxyz";
/*  13:    */   private static final String WINGDINGS_WHITE_LIST = "";
/*  14:    */   private static final String WINGDINGS_BLACK_LIST = "";
/*  15:    */   private static final String CIRCLE_DB_LIST = "❶❷❸❹❺❻❼❽❾";
/*  16:    */   
/*  17:    */   private AutoNumberingScheme(int nativeId, int ooxmlId)
/*  18:    */   {
/*  19:109 */     this.nativeId = nativeId;
/*  20:110 */     this.ooxmlId = ooxmlId;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static AutoNumberingScheme forNativeID(int nativeId)
/*  24:    */   {
/*  25:114 */     for (AutoNumberingScheme ans : ) {
/*  26:115 */       if (ans.nativeId == nativeId) {
/*  27:115 */         return ans;
/*  28:    */       }
/*  29:    */     }
/*  30:117 */     return null;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static AutoNumberingScheme forOoxmlID(int ooxmlId)
/*  34:    */   {
/*  35:121 */     for (AutoNumberingScheme ans : ) {
/*  36:122 */       if (ans.ooxmlId == ooxmlId) {
/*  37:122 */         return ans;
/*  38:    */       }
/*  39:    */     }
/*  40:124 */     return null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getDescription()
/*  44:    */   {
/*  45:128 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$AutoNumberingScheme[ordinal()])
/*  46:    */     {
/*  47:    */     case 1: 
/*  48:129 */       return "Lowercase Latin character followed by a period. Example: a., b., c., ...";
/*  49:    */     case 2: 
/*  50:130 */       return "Uppercase Latin character followed by a period. Example: A., B., C., ...";
/*  51:    */     case 3: 
/*  52:131 */       return "Arabic numeral followed by a closing parenthesis. Example: 1), 2), 3), ...";
/*  53:    */     case 4: 
/*  54:132 */       return "Arabic numeral followed by a period. Example: 1., 2., 3., ...";
/*  55:    */     case 5: 
/*  56:133 */       return "Lowercase Roman numeral enclosed in parentheses. Example: (i), (ii), (iii), ...";
/*  57:    */     case 6: 
/*  58:134 */       return "Lowercase Roman numeral followed by a closing parenthesis. Example: i), ii), iii), ...";
/*  59:    */     case 7: 
/*  60:135 */       return "Lowercase Roman numeral followed by a period. Example: i., ii., iii., ...";
/*  61:    */     case 8: 
/*  62:136 */       return "Uppercase Roman numeral followed by a period. Example: I., II., III., ...";
/*  63:    */     case 9: 
/*  64:137 */       return "Lowercase alphabetic character enclosed in parentheses. Example: (a), (b), (c), ...";
/*  65:    */     case 10: 
/*  66:138 */       return "Lowercase alphabetic character followed by a closing parenthesis. Example: a), b), c), ...";
/*  67:    */     case 11: 
/*  68:139 */       return "Uppercase alphabetic character enclosed in parentheses. Example: (A), (B), (C), ...";
/*  69:    */     case 12: 
/*  70:140 */       return "Uppercase alphabetic character followed by a closing parenthesis. Example: A), B), C), ...";
/*  71:    */     case 13: 
/*  72:141 */       return "Arabic numeral enclosed in parentheses. Example: (1), (2), (3), ...";
/*  73:    */     case 14: 
/*  74:142 */       return "Arabic numeral. Example: 1, 2, 3, ...";
/*  75:    */     case 15: 
/*  76:143 */       return "Uppercase Roman numeral enclosed in parentheses. Example: (I), (II), (III), ...";
/*  77:    */     case 16: 
/*  78:144 */       return "Uppercase Roman numeral followed by a closing parenthesis. Example: I), II), III), ...";
/*  79:    */     case 17: 
/*  80:145 */       return "Simplified Chinese.";
/*  81:    */     case 18: 
/*  82:146 */       return "Simplified Chinese with single-byte period.";
/*  83:    */     case 19: 
/*  84:147 */       return "Double byte circle numbers.";
/*  85:    */     case 20: 
/*  86:148 */       return "Wingdings white circle numbers.";
/*  87:    */     case 21: 
/*  88:149 */       return "Wingdings black circle numbers.";
/*  89:    */     case 22: 
/*  90:150 */       return "Traditional Chinese.";
/*  91:    */     case 23: 
/*  92:151 */       return "Traditional Chinese with single-byte period.";
/*  93:    */     case 24: 
/*  94:152 */       return "Bidi Arabic 1 (AraAlpha) with ANSI minus symbol.";
/*  95:    */     case 25: 
/*  96:153 */       return "Bidi Arabic 2 (AraAbjad) with ANSI minus symbol.";
/*  97:    */     case 26: 
/*  98:154 */       return "Bidi Hebrew 2 with ANSI minus symbol.";
/*  99:    */     case 27: 
/* 100:155 */       return "Japanese/Korean.";
/* 101:    */     case 28: 
/* 102:156 */       return "Japanese/Korean with single-byte period.";
/* 103:    */     case 29: 
/* 104:157 */       return "Double-byte Arabic numbers.";
/* 105:    */     case 30: 
/* 106:158 */       return "Double-byte Arabic numbers with double-byte period.";
/* 107:    */     case 31: 
/* 108:159 */       return "Thai alphabetic character followed by a period.";
/* 109:    */     case 32: 
/* 110:160 */       return "Thai alphabetic character followed by a closing parenthesis.";
/* 111:    */     case 33: 
/* 112:161 */       return "Thai alphabetic character enclosed by parentheses.";
/* 113:    */     case 34: 
/* 114:162 */       return "Thai numeral followed by a period.";
/* 115:    */     case 35: 
/* 116:163 */       return "Thai numeral followed by a closing parenthesis.";
/* 117:    */     case 36: 
/* 118:164 */       return "Thai numeral enclosed in parentheses.";
/* 119:    */     case 37: 
/* 120:165 */       return "Hindi alphabetic character followed by a period.";
/* 121:    */     case 38: 
/* 122:166 */       return "Hindi numeric character followed by a period.";
/* 123:    */     case 39: 
/* 124:167 */       return "Japanese with double-byte period.";
/* 125:    */     case 40: 
/* 126:168 */       return "Hindi numeric character followed by a closing parenthesis.";
/* 127:    */     case 41: 
/* 128:169 */       return "Hindi alphabetic character followed by a period.";
/* 129:    */     }
/* 130:170 */     return "Unknown Numbered Scheme";
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String format(int value)
/* 134:    */   {
/* 135:175 */     String index = formatIndex(value);
/* 136:176 */     String cased = formatCase(index);
/* 137:177 */     String seperated = formatSeperator(cased);
/* 138:178 */     return seperated;
/* 139:    */   }
/* 140:    */   
/* 141:    */   private String formatSeperator(String cased)
/* 142:    */   {
/* 143:182 */     String name = name().toLowerCase(Locale.ROOT);
/* 144:183 */     if (name.contains("plain")) {
/* 145:183 */       return cased;
/* 146:    */     }
/* 147:184 */     if (name.contains("parenright")) {
/* 148:184 */       return cased + ")";
/* 149:    */     }
/* 150:185 */     if (name.contains("parenboth")) {
/* 151:185 */       return "(" + cased + ")";
/* 152:    */     }
/* 153:186 */     if (name.contains("period")) {
/* 154:186 */       return cased + ".";
/* 155:    */     }
/* 156:187 */     if (name.contains("minus")) {
/* 157:187 */       return cased + "-";
/* 158:    */     }
/* 159:188 */     return cased;
/* 160:    */   }
/* 161:    */   
/* 162:    */   private String formatCase(String index)
/* 163:    */   {
/* 164:192 */     String name = name().toLowerCase(Locale.ROOT);
/* 165:193 */     if (name.contains("lc")) {
/* 166:193 */       return index.toLowerCase(Locale.ROOT);
/* 167:    */     }
/* 168:194 */     if (name.contains("uc")) {
/* 169:194 */       return index.toUpperCase(Locale.ROOT);
/* 170:    */     }
/* 171:195 */     return index;
/* 172:    */   }
/* 173:    */   
/* 174:    */   private String formatIndex(int value)
/* 175:    */   {
/* 176:208 */     String name = name().toLowerCase(Locale.ROOT);
/* 177:209 */     if (name.startsWith("roman")) {
/* 178:210 */       return formatRomanIndex(value);
/* 179:    */     }
/* 180:211 */     if ((name.startsWith("arabic")) && (!name.contains("db"))) {
/* 181:212 */       return getIndexedList(value, "0123456789", false);
/* 182:    */     }
/* 183:213 */     if (name.startsWith("alpha")) {
/* 184:214 */       return getIndexedList(value, "abcdefghijklmnopqrstuvwxyz", true);
/* 185:    */     }
/* 186:215 */     if (name.contains("WdWhite")) {
/* 187:216 */       return value == 10 ? "" : getIndexedList(value, "", false);
/* 188:    */     }
/* 189:218 */     if (name.contains("WdBlack")) {
/* 190:219 */       return value == 10 ? "" : getIndexedList(value, "", false);
/* 191:    */     }
/* 192:221 */     if (name.contains("NumDb")) {
/* 193:222 */       return value == 10 ? "❿" : getIndexedList(value, "❶❷❸❹❺❻❼❽❾", true);
/* 194:    */     }
/* 195:225 */     return "?";
/* 196:    */   }
/* 197:    */   
/* 198:    */   private static String getIndexedList(int val, String list, boolean oneBased)
/* 199:    */   {
/* 200:230 */     StringBuilder sb = new StringBuilder();
/* 201:231 */     addIndexedChar(val, list, oneBased, sb);
/* 202:232 */     return sb.toString();
/* 203:    */   }
/* 204:    */   
/* 205:    */   private static void addIndexedChar(int val, String list, boolean oneBased, StringBuilder sb)
/* 206:    */   {
/* 207:236 */     if (oneBased) {
/* 208:236 */       val--;
/* 209:    */     }
/* 210:237 */     int len = list.length();
/* 211:238 */     if (val >= len) {
/* 212:239 */       addIndexedChar(val / len, list, oneBased, sb);
/* 213:    */     }
/* 214:241 */     sb.append(list.charAt(val % len));
/* 215:    */   }
/* 216:    */   
/* 217:    */   private String formatRomanIndex(int value)
/* 218:    */   {
/* 219:247 */     int[] VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
/* 220:248 */     String[] ROMAN = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
/* 221:249 */     String[][] conciseList = { { "XLV", "VL" }, { "XCV", "VC" }, { "CDL", "LD" }, { "CML", "LM" }, { "CMVC", "LMVL" }, { "CDXC", "LDXL" }, { "CDVC", "LDVL" }, { "CMXC", "LMXL" }, { "XCIX", "VCIV" }, { "XLIX", "VLIV" }, { "XLIX", "IL" }, { "XCIX", "IC" }, { "CDXC", "XD" }, { "CDVC", "XDV" }, { "CDIC", "XDIX" }, { "LMVL", "XMV" }, { "CMIC", "XMIX" }, { "CMXC", "XM" }, { "XDV", "VD" }, { "XDIX", "VDIV" }, { "XMV", "VM" }, { "XMIX", "VMIV" }, { "VDIV", "ID" }, { "VMIV", "IM" } };
/* 222:    */     
/* 223:    */ 
/* 224:    */ 
/* 225:    */ 
/* 226:    */ 
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:    */ 
/* 233:    */ 
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:    */ 
/* 238:    */ 
/* 239:    */ 
/* 240:    */ 
/* 241:    */ 
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:    */ 
/* 246:    */ 
/* 247:    */ 
/* 248:276 */     StringBuilder sb = new StringBuilder();
/* 249:277 */     for (int i = 0; i < 13; i++) {
/* 250:278 */       while (value >= VALUES[i])
/* 251:    */       {
/* 252:279 */         value -= VALUES[i];
/* 253:280 */         sb.append(ROMAN[i]);
/* 254:    */       }
/* 255:    */     }
/* 256:283 */     String result = sb.toString();
/* 257:284 */     for (String[] cc : conciseList) {
/* 258:285 */       result = result.replace(cc[0], cc[1]);
/* 259:    */     }
/* 260:287 */     return result;
/* 261:    */   }
/* 262:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.AutoNumberingScheme
 * JD-Core Version:    0.7.0.1
 */