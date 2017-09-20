/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Map;
/*   7:    */ 
/*   8:    */ @Internal
/*   9:    */ public class StringUtil
/*  10:    */ {
/*  11: 31 */   private static final POILogger logger = POILogFactory.getLogger(StringUtil.class);
/*  12: 33 */   protected static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*  13: 34 */   public static final Charset UTF16LE = Charset.forName("UTF-16LE");
/*  14: 35 */   public static final Charset UTF8 = Charset.forName("UTF-8");
/*  15: 36 */   public static final Charset WIN_1252 = Charset.forName("cp1252");
/*  16: 37 */   public static final Charset BIG5 = Charset.forName("Big5");
/*  17:    */   private static Map<Integer, Integer> msCodepointToUnicode;
/*  18:    */   
/*  19:    */   public static String getFromUnicodeLE(byte[] string, int offset, int len)
/*  20:    */     throws ArrayIndexOutOfBoundsException, IllegalArgumentException
/*  21:    */   {
/*  22: 70 */     if ((offset < 0) || (offset >= string.length)) {
/*  23: 71 */       throw new ArrayIndexOutOfBoundsException("Illegal offset " + offset + " (String data is of length " + string.length + ")");
/*  24:    */     }
/*  25: 73 */     if ((len < 0) || ((string.length - offset) / 2 < len)) {
/*  26: 74 */       throw new IllegalArgumentException("Illegal length " + len);
/*  27:    */     }
/*  28: 77 */     return new String(string, offset, len * 2, UTF16LE);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static String getFromUnicodeLE(byte[] string)
/*  32:    */   {
/*  33: 91 */     if (string.length == 0) {
/*  34: 91 */       return "";
/*  35:    */     }
/*  36: 92 */     return getFromUnicodeLE(string, 0, string.length / 2);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static byte[] getToUnicodeLE(String string)
/*  40:    */   {
/*  41:102 */     return string.getBytes(UTF16LE);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static String getFromCompressedUnicode(byte[] string, int offset, int len)
/*  45:    */   {
/*  46:119 */     int len_to_use = Math.min(len, string.length - offset);
/*  47:120 */     return new String(string, offset, len_to_use, ISO_8859_1);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public static String readCompressedUnicode(LittleEndianInput in, int nChars)
/*  51:    */   {
/*  52:124 */     byte[] buf = new byte[nChars];
/*  53:125 */     in.readFully(buf);
/*  54:126 */     return new String(buf, ISO_8859_1);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static String readUnicodeString(LittleEndianInput in)
/*  58:    */   {
/*  59:142 */     int nChars = in.readUShort();
/*  60:143 */     byte flag = in.readByte();
/*  61:144 */     if ((flag & 0x1) == 0) {
/*  62:145 */       return readCompressedUnicode(in, nChars);
/*  63:    */     }
/*  64:147 */     return readUnicodeLE(in, nChars);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static String readUnicodeString(LittleEndianInput in, int nChars)
/*  68:    */   {
/*  69:162 */     byte is16Bit = in.readByte();
/*  70:163 */     if ((is16Bit & 0x1) == 0) {
/*  71:164 */       return readCompressedUnicode(in, nChars);
/*  72:    */     }
/*  73:166 */     return readUnicodeLE(in, nChars);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static void writeUnicodeString(LittleEndianOutput out, String value)
/*  77:    */   {
/*  78:178 */     int nChars = value.length();
/*  79:179 */     out.writeShort(nChars);
/*  80:180 */     boolean is16Bit = hasMultibyte(value);
/*  81:181 */     out.writeByte(is16Bit ? 1 : 0);
/*  82:182 */     if (is16Bit) {
/*  83:183 */       putUnicodeLE(value, out);
/*  84:    */     } else {
/*  85:185 */       putCompressedUnicode(value, out);
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static void writeUnicodeStringFlagAndData(LittleEndianOutput out, String value)
/*  90:    */   {
/*  91:201 */     boolean is16Bit = hasMultibyte(value);
/*  92:202 */     out.writeByte(is16Bit ? 1 : 0);
/*  93:203 */     if (is16Bit) {
/*  94:204 */       putUnicodeLE(value, out);
/*  95:    */     } else {
/*  96:206 */       putCompressedUnicode(value, out);
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static int getEncodedSize(String value)
/* 101:    */   {
/* 102:214 */     int result = 3;
/* 103:215 */     result += value.length() * (hasMultibyte(value) ? 2 : 1);
/* 104:216 */     return result;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static void putCompressedUnicode(String input, byte[] output, int offset)
/* 108:    */   {
/* 109:230 */     byte[] bytes = input.getBytes(ISO_8859_1);
/* 110:231 */     System.arraycopy(bytes, 0, output, offset, bytes.length);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static void putCompressedUnicode(String input, LittleEndianOutput out)
/* 114:    */   {
/* 115:235 */     byte[] bytes = input.getBytes(ISO_8859_1);
/* 116:236 */     out.write(bytes);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static void putUnicodeLE(String input, byte[] output, int offset)
/* 120:    */   {
/* 121:249 */     byte[] bytes = input.getBytes(UTF16LE);
/* 122:250 */     System.arraycopy(bytes, 0, output, offset, bytes.length);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void putUnicodeLE(String input, LittleEndianOutput out)
/* 126:    */   {
/* 127:253 */     byte[] bytes = input.getBytes(UTF16LE);
/* 128:254 */     out.write(bytes);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static String readUnicodeLE(LittleEndianInput in, int nChars)
/* 132:    */   {
/* 133:258 */     byte[] bytes = new byte[nChars * 2];
/* 134:259 */     in.readFully(bytes);
/* 135:260 */     return new String(bytes, UTF16LE);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static String getPreferredEncoding()
/* 139:    */   {
/* 140:267 */     return ISO_8859_1.name();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static boolean hasMultibyte(String value)
/* 144:    */   {
/* 145:277 */     if (value == null) {
/* 146:278 */       return false;
/* 147:    */     }
/* 148:279 */     for (char c : value.toCharArray()) {
/* 149:280 */       if (c > 'ÿ') {
/* 150:281 */         return true;
/* 151:    */       }
/* 152:    */     }
/* 153:284 */     return false;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static boolean isUnicodeString(String value)
/* 157:    */   {
/* 158:294 */     return !value.equals(new String(value.getBytes(ISO_8859_1), ISO_8859_1));
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static boolean startsWithIgnoreCase(String haystack, String prefix)
/* 162:    */   {
/* 163:301 */     return haystack.regionMatches(true, 0, prefix, 0, prefix.length());
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static boolean endsWithIgnoreCase(String haystack, String suffix)
/* 167:    */   {
/* 168:308 */     int length = suffix.length();
/* 169:309 */     int start = haystack.length() - length;
/* 170:310 */     return haystack.regionMatches(true, start, suffix, 0, length);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public static class StringsIterator
/* 174:    */     implements Iterator<String>
/* 175:    */   {
/* 176:317 */     private String[] strings = new String[0];
/* 177:318 */     private int position = 0;
/* 178:    */     
/* 179:    */     public StringsIterator(String[] strings)
/* 180:    */     {
/* 181:320 */       if (strings != null) {
/* 182:321 */         this.strings = ((String[])strings.clone());
/* 183:    */       }
/* 184:    */     }
/* 185:    */     
/* 186:    */     public boolean hasNext()
/* 187:    */     {
/* 188:326 */       return this.position < this.strings.length;
/* 189:    */     }
/* 190:    */     
/* 191:    */     public String next()
/* 192:    */     {
/* 193:329 */       int ourPos = this.position++;
/* 194:330 */       if (ourPos >= this.strings.length) {
/* 195:331 */         throw new ArrayIndexOutOfBoundsException(ourPos);
/* 196:    */       }
/* 197:333 */       return this.strings[ourPos];
/* 198:    */     }
/* 199:    */     
/* 200:    */     public void remove() {}
/* 201:    */   }
/* 202:    */   
/* 203:    */   public static String mapMsCodepointString(String string)
/* 204:    */   {
/* 205:351 */     if ((string == null) || ("".equals(string))) {
/* 206:351 */       return string;
/* 207:    */     }
/* 208:352 */     initMsCodepointMap();
/* 209:    */     
/* 210:354 */     StringBuilder sb = new StringBuilder();
/* 211:355 */     int length = string.length();
/* 212:356 */     for (int offset = 0; offset < length;)
/* 213:    */     {
/* 214:357 */       Integer msCodepoint = Integer.valueOf(string.codePointAt(offset));
/* 215:358 */       Integer uniCodepoint = (Integer)msCodepointToUnicode.get(msCodepoint);
/* 216:359 */       sb.appendCodePoint((uniCodepoint == null ? msCodepoint : uniCodepoint).intValue());
/* 217:360 */       offset += Character.charCount(msCodepoint.intValue());
/* 218:    */     }
/* 219:363 */     return sb.toString();
/* 220:    */   }
/* 221:    */   
/* 222:    */   public static synchronized void mapMsCodepoint(int msCodepoint, int unicodeCodepoint)
/* 223:    */   {
/* 224:367 */     initMsCodepointMap();
/* 225:368 */     msCodepointToUnicode.put(Integer.valueOf(msCodepoint), Integer.valueOf(unicodeCodepoint));
/* 226:    */   }
/* 227:    */   
/* 228:    */   private static synchronized void initMsCodepointMap()
/* 229:    */   {
/* 230:372 */     if (msCodepointToUnicode != null) {
/* 231:372 */       return;
/* 232:    */     }
/* 233:373 */     msCodepointToUnicode = new HashMap();
/* 234:374 */     int i = 61472;
/* 235:375 */     for (int ch : symbolMap_f020) {
/* 236:376 */       msCodepointToUnicode.put(Integer.valueOf(i++), Integer.valueOf(ch));
/* 237:    */     }
/* 238:378 */     i = 61600;
/* 239:379 */     for (int ch : symbolMap_f0a0) {
/* 240:380 */       msCodepointToUnicode.put(Integer.valueOf(i++), Integer.valueOf(ch));
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:384 */   private static final int[] symbolMap_f020 = { 32, 33, 8704, 35, 8707, 37, 38, 8717, 40, 41, 8727, 43, 44, 8722, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 8773, 913, 914, 935, 916, 917, 934, 915, 919, 921, 977, 922, 923, 924, 925, 927, 928, 920, 929, 931, 932, 933, 962, 937, 926, 936, 918, 91, 8765, 93, 8869, 95, 32, 945, 946, 967, 948, 949, 966, 947, 951, 953, 981, 954, 955, 956, 957, 959, 960, 952, 961, 963, 964, 965, 982, 969, 958, 968, 950, 123, 124, 125, 8764, 32 };
/* 245:483 */   private static final int[] symbolMap_f0a0 = { 8364, 978, 8242, 8804, 8260, 8734, 402, 9827, 9830, 9829, 9824, 8596, 8591, 8593, 8594, 8595, 176, 177, 8243, 8805, 215, 181, 8706, 8729, 247, 8800, 8801, 8776, 8230, 9168, 9135, 8629, 8501, 8475, 8476, 8472, 8855, 8853, 8709, 8745, 8746, 8835, 8839, 8836, 8834, 8838, 8712, 8713, 8736, 8711, 174, 169, 8482, 8719, 8730, 8901, 172, 8743, 8744, 8660, 8656, 8657, 8658, 8659, 9674, 9001, 174, 169, 8482, 8721, 9115, 9116, 9117, 9121, 9122, 9123, 9127, 9128, 9129, 9130, 32, 9002, 8747, 8992, 9134, 8993, 9118, 9119, 9120, 9124, 9125, 9126, 9131, 9132, 9133, 32 };
/* 246:    */   
/* 247:    */   @Internal
/* 248:    */   public static String join(Object[] array, String separator)
/* 249:    */   {
/* 250:586 */     if ((array == null) || (array.length == 0)) {
/* 251:586 */       return "";
/* 252:    */     }
/* 253:587 */     StringBuilder sb = new StringBuilder();
/* 254:588 */     sb.append(array[0]);
/* 255:589 */     for (int i = 1; i < array.length; i++) {
/* 256:590 */       sb.append(separator).append(array[i]);
/* 257:    */     }
/* 258:592 */     return sb.toString();
/* 259:    */   }
/* 260:    */   
/* 261:    */   @Internal
/* 262:    */   public static String join(Object[] array)
/* 263:    */   {
/* 264:597 */     if (array == null) {
/* 265:597 */       return "";
/* 266:    */     }
/* 267:598 */     StringBuilder sb = new StringBuilder();
/* 268:599 */     for (Object o : array) {
/* 269:600 */       sb.append(o);
/* 270:    */     }
/* 271:602 */     return sb.toString();
/* 272:    */   }
/* 273:    */   
/* 274:    */   @Internal
/* 275:    */   public static String join(String separator, Object... array)
/* 276:    */   {
/* 277:607 */     return join(array, separator);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public static int countMatches(CharSequence haystack, char needle)
/* 281:    */   {
/* 282:619 */     if (haystack == null) {
/* 283:619 */       return 0;
/* 284:    */     }
/* 285:620 */     int count = 0;
/* 286:621 */     int length = haystack.length();
/* 287:622 */     for (int i = 0; i < length; i++) {
/* 288:623 */       if (haystack.charAt(i) == needle) {
/* 289:624 */         count++;
/* 290:    */       }
/* 291:    */     }
/* 292:627 */     return count;
/* 293:    */   }
/* 294:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.StringUtil
 * JD-Core Version:    0.7.0.1
 */