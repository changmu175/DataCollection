/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Set;
/*   7:    */ 
/*   8:    */ public class CodePageUtil
/*   9:    */ {
/*  10: 34 */   public static final Set<Charset> DOUBLE_BYTE_CHARSETS = Collections.singleton(StringUtil.BIG5);
/*  11:    */   public static final int CP_037 = 37;
/*  12:    */   public static final int CP_SJIS = 932;
/*  13:    */   public static final int CP_GBK = 936;
/*  14:    */   public static final int CP_MS949 = 949;
/*  15:    */   public static final int CP_UTF16 = 1200;
/*  16:    */   public static final int CP_UTF16_BE = 1201;
/*  17:    */   public static final int CP_WINDOWS_1250 = 1250;
/*  18:    */   public static final int CP_WINDOWS_1251 = 1251;
/*  19:    */   public static final int CP_WINDOWS_1252 = 1252;
/*  20:    */   public static final int CP_WINDOWS_1252_BIFF23 = 32769;
/*  21:    */   public static final int CP_WINDOWS_1253 = 1253;
/*  22:    */   public static final int CP_WINDOWS_1254 = 1254;
/*  23:    */   public static final int CP_WINDOWS_1255 = 1255;
/*  24:    */   public static final int CP_WINDOWS_1256 = 1256;
/*  25:    */   public static final int CP_WINDOWS_1257 = 1257;
/*  26:    */   public static final int CP_WINDOWS_1258 = 1258;
/*  27:    */   public static final int CP_JOHAB = 1361;
/*  28:    */   public static final int CP_MAC_ROMAN = 10000;
/*  29:    */   public static final int CP_MAC_ROMAN_BIFF23 = 32768;
/*  30:    */   public static final int CP_MAC_JAPAN = 10001;
/*  31:    */   public static final int CP_MAC_CHINESE_TRADITIONAL = 10002;
/*  32:    */   public static final int CP_MAC_KOREAN = 10003;
/*  33:    */   public static final int CP_MAC_ARABIC = 10004;
/*  34:    */   public static final int CP_MAC_HEBREW = 10005;
/*  35:    */   public static final int CP_MAC_GREEK = 10006;
/*  36:    */   public static final int CP_MAC_CYRILLIC = 10007;
/*  37:    */   public static final int CP_MAC_CHINESE_SIMPLE = 10008;
/*  38:    */   public static final int CP_MAC_ROMANIA = 10010;
/*  39:    */   public static final int CP_MAC_UKRAINE = 10017;
/*  40:    */   public static final int CP_MAC_THAI = 10021;
/*  41:    */   public static final int CP_MAC_CENTRAL_EUROPE = 10029;
/*  42:    */   public static final int CP_MAC_ICELAND = 10079;
/*  43:    */   public static final int CP_MAC_TURKISH = 10081;
/*  44:    */   public static final int CP_MAC_CROATIAN = 10082;
/*  45:    */   public static final int CP_US_ACSII = 20127;
/*  46:    */   public static final int CP_KOI8_R = 20866;
/*  47:    */   public static final int CP_ISO_8859_1 = 28591;
/*  48:    */   public static final int CP_ISO_8859_2 = 28592;
/*  49:    */   public static final int CP_ISO_8859_3 = 28593;
/*  50:    */   public static final int CP_ISO_8859_4 = 28594;
/*  51:    */   public static final int CP_ISO_8859_5 = 28595;
/*  52:    */   public static final int CP_ISO_8859_6 = 28596;
/*  53:    */   public static final int CP_ISO_8859_7 = 28597;
/*  54:    */   public static final int CP_ISO_8859_8 = 28598;
/*  55:    */   public static final int CP_ISO_8859_9 = 28599;
/*  56:    */   public static final int CP_ISO_2022_JP1 = 50220;
/*  57:    */   public static final int CP_ISO_2022_JP2 = 50221;
/*  58:    */   public static final int CP_ISO_2022_JP3 = 50222;
/*  59:    */   public static final int CP_ISO_2022_KR = 50225;
/*  60:    */   public static final int CP_EUC_JP = 51932;
/*  61:    */   public static final int CP_EUC_KR = 51949;
/*  62:    */   public static final int CP_GB2312 = 52936;
/*  63:    */   public static final int CP_GB18030 = 54936;
/*  64:    */   public static final int CP_US_ASCII2 = 65000;
/*  65:    */   public static final int CP_UTF8 = 65001;
/*  66:    */   public static final int CP_UNICODE = 1200;
/*  67:    */   
/*  68:    */   public static byte[] getBytesInCodePage(String string, int codepage)
/*  69:    */     throws UnsupportedEncodingException
/*  70:    */   {
/*  71:214 */     String encoding = codepageToEncoding(codepage);
/*  72:215 */     return string.getBytes(encoding);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static String getStringFromCodePage(byte[] string, int codepage)
/*  76:    */     throws UnsupportedEncodingException
/*  77:    */   {
/*  78:227 */     return getStringFromCodePage(string, 0, string.length, codepage);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static String getStringFromCodePage(byte[] string, int offset, int length, int codepage)
/*  82:    */     throws UnsupportedEncodingException
/*  83:    */   {
/*  84:239 */     String encoding = codepageToEncoding(codepage);
/*  85:240 */     return new String(string, offset, length, encoding);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static String codepageToEncoding(int codepage)
/*  89:    */     throws UnsupportedEncodingException
/*  90:    */   {
/*  91:261 */     return codepageToEncoding(codepage, false);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static String codepageToEncoding(int codepage, boolean javaLangFormat)
/*  95:    */     throws UnsupportedEncodingException
/*  96:    */   {
/*  97:282 */     if (codepage <= 0) {
/*  98:283 */       throw new UnsupportedEncodingException("Codepage number may not be " + codepage);
/*  99:    */     }
/* 100:285 */     switch (codepage)
/* 101:    */     {
/* 102:    */     case 1200: 
/* 103:287 */       return "UTF-16LE";
/* 104:    */     case 1201: 
/* 105:289 */       return "UTF-16BE";
/* 106:    */     case 65001: 
/* 107:291 */       return "UTF-8";
/* 108:    */     case 37: 
/* 109:293 */       return "cp037";
/* 110:    */     case 936: 
/* 111:295 */       return "GBK";
/* 112:    */     case 949: 
/* 113:297 */       return "ms949";
/* 114:    */     case 1250: 
/* 115:299 */       if (javaLangFormat) {
/* 116:300 */         return "Cp1250";
/* 117:    */       }
/* 118:302 */       return "windows-1250";
/* 119:    */     case 1251: 
/* 120:304 */       if (javaLangFormat) {
/* 121:305 */         return "Cp1251";
/* 122:    */       }
/* 123:307 */       return "windows-1251";
/* 124:    */     case 1252: 
/* 125:    */     case 32769: 
/* 126:310 */       if (javaLangFormat) {
/* 127:311 */         return "Cp1252";
/* 128:    */       }
/* 129:313 */       return "windows-1252";
/* 130:    */     case 1253: 
/* 131:315 */       if (javaLangFormat) {
/* 132:316 */         return "Cp1253";
/* 133:    */       }
/* 134:318 */       return "windows-1253";
/* 135:    */     case 1254: 
/* 136:320 */       if (javaLangFormat) {
/* 137:321 */         return "Cp1254";
/* 138:    */       }
/* 139:323 */       return "windows-1254";
/* 140:    */     case 1255: 
/* 141:325 */       if (javaLangFormat) {
/* 142:326 */         return "Cp1255";
/* 143:    */       }
/* 144:328 */       return "windows-1255";
/* 145:    */     case 1256: 
/* 146:330 */       if (javaLangFormat) {
/* 147:331 */         return "Cp1255";
/* 148:    */       }
/* 149:333 */       return "windows-1256";
/* 150:    */     case 1257: 
/* 151:335 */       if (javaLangFormat) {
/* 152:336 */         return "Cp1257";
/* 153:    */       }
/* 154:338 */       return "windows-1257";
/* 155:    */     case 1258: 
/* 156:340 */       if (javaLangFormat) {
/* 157:341 */         return "Cp1258";
/* 158:    */       }
/* 159:343 */       return "windows-1258";
/* 160:    */     case 1361: 
/* 161:345 */       return "johab";
/* 162:    */     case 10000: 
/* 163:    */     case 32768: 
/* 164:348 */       return "MacRoman";
/* 165:    */     case 10001: 
/* 166:350 */       return "SJIS";
/* 167:    */     case 10002: 
/* 168:352 */       return "Big5";
/* 169:    */     case 10003: 
/* 170:354 */       return "EUC-KR";
/* 171:    */     case 10004: 
/* 172:356 */       return "MacArabic";
/* 173:    */     case 10005: 
/* 174:358 */       return "MacHebrew";
/* 175:    */     case 10006: 
/* 176:360 */       return "MacGreek";
/* 177:    */     case 10007: 
/* 178:362 */       return "MacCyrillic";
/* 179:    */     case 10008: 
/* 180:364 */       return "EUC_CN";
/* 181:    */     case 10010: 
/* 182:366 */       return "MacRomania";
/* 183:    */     case 10017: 
/* 184:368 */       return "MacUkraine";
/* 185:    */     case 10021: 
/* 186:370 */       return "MacThai";
/* 187:    */     case 10029: 
/* 188:372 */       return "MacCentralEurope";
/* 189:    */     case 10079: 
/* 190:374 */       return "MacIceland";
/* 191:    */     case 10081: 
/* 192:376 */       return "MacTurkish";
/* 193:    */     case 10082: 
/* 194:378 */       return "MacCroatian";
/* 195:    */     case 20127: 
/* 196:    */     case 65000: 
/* 197:381 */       return "US-ASCII";
/* 198:    */     case 20866: 
/* 199:383 */       return "KOI8-R";
/* 200:    */     case 28591: 
/* 201:385 */       if (javaLangFormat) {
/* 202:386 */         return "ISO8859_1";
/* 203:    */       }
/* 204:388 */       return "ISO-8859-1";
/* 205:    */     case 28592: 
/* 206:390 */       if (javaLangFormat) {
/* 207:391 */         return "ISO8859_2";
/* 208:    */       }
/* 209:393 */       return "ISO-8859-2";
/* 210:    */     case 28593: 
/* 211:395 */       if (javaLangFormat) {
/* 212:396 */         return "ISO8859_3";
/* 213:    */       }
/* 214:398 */       return "ISO-8859-3";
/* 215:    */     case 28594: 
/* 216:400 */       if (javaLangFormat) {
/* 217:401 */         return "ISO8859_4";
/* 218:    */       }
/* 219:403 */       return "ISO-8859-4";
/* 220:    */     case 28595: 
/* 221:405 */       if (javaLangFormat) {
/* 222:406 */         return "ISO8859_5";
/* 223:    */       }
/* 224:408 */       return "ISO-8859-5";
/* 225:    */     case 28596: 
/* 226:410 */       if (javaLangFormat) {
/* 227:411 */         return "ISO8859_6";
/* 228:    */       }
/* 229:413 */       return "ISO-8859-6";
/* 230:    */     case 28597: 
/* 231:415 */       if (javaLangFormat) {
/* 232:416 */         return "ISO8859_7";
/* 233:    */       }
/* 234:418 */       return "ISO-8859-7";
/* 235:    */     case 28598: 
/* 236:420 */       if (javaLangFormat) {
/* 237:421 */         return "ISO8859_8";
/* 238:    */       }
/* 239:423 */       return "ISO-8859-8";
/* 240:    */     case 28599: 
/* 241:425 */       if (javaLangFormat) {
/* 242:426 */         return "ISO8859_9";
/* 243:    */       }
/* 244:428 */       return "ISO-8859-9";
/* 245:    */     case 50220: 
/* 246:    */     case 50221: 
/* 247:    */     case 50222: 
/* 248:432 */       return "ISO-2022-JP";
/* 249:    */     case 50225: 
/* 250:434 */       return "ISO-2022-KR";
/* 251:    */     case 51932: 
/* 252:436 */       return "EUC-JP";
/* 253:    */     case 51949: 
/* 254:438 */       return "EUC-KR";
/* 255:    */     case 52936: 
/* 256:440 */       return "GB2312";
/* 257:    */     case 54936: 
/* 258:442 */       return "GB18030";
/* 259:    */     case 932: 
/* 260:444 */       return "SJIS";
/* 261:    */     }
/* 262:446 */     return "cp" + codepage;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static String cp950ToString(byte[] data, int offset, int lengthInBytes)
/* 266:    */   {
/* 267:462 */     StringBuilder sb = new StringBuilder();
/* 268:463 */     LittleEndianCP950Reader reader = new LittleEndianCP950Reader(data, offset, lengthInBytes);
/* 269:464 */     int c = reader.read();
/* 270:465 */     while (c != -1)
/* 271:    */     {
/* 272:466 */       sb.append((char)c);
/* 273:467 */       c = reader.read();
/* 274:    */     }
/* 275:469 */     reader.close();
/* 276:470 */     return sb.toString();
/* 277:    */   }
/* 278:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.CodePageUtil
 * JD-Core Version:    0.7.0.1
 */