/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.OutputStreamWriter;
/*   9:    */ import java.io.PrintStream;
/*  10:    */ import java.nio.charset.Charset;
/*  11:    */ 
/*  12:    */ @Internal
/*  13:    */ public class HexDump
/*  14:    */ {
/*  15: 36 */   public static final String EOL = System.getProperty("line.separator");
/*  16: 37 */   public static final Charset UTF8 = Charset.forName("UTF-8");
/*  17:    */   
/*  18:    */   public static void dump(byte[] data, long offset, OutputStream stream, int index, int length)
/*  19:    */     throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException
/*  20:    */   {
/*  21: 63 */     if (stream == null) {
/*  22: 64 */       throw new IllegalArgumentException("cannot write to nullstream");
/*  23:    */     }
/*  24: 67 */     OutputStreamWriter osw = new OutputStreamWriter(stream, UTF8);
/*  25: 68 */     osw.write(dump(data, offset, index, length));
/*  26: 69 */     osw.flush();
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static synchronized void dump(byte[] data, long offset, OutputStream stream, int index)
/*  30:    */     throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException
/*  31:    */   {
/*  32: 92 */     dump(data, offset, stream, index, 2147483647);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static String dump(byte[] data, long offset, int index)
/*  36:    */   {
/*  37:108 */     return dump(data, offset, index, 2147483647);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static String dump(byte[] data, long offset, int index, int length)
/*  41:    */   {
/*  42:125 */     if ((data == null) || (data.length == 0)) {
/*  43:126 */       return "No Data" + EOL;
/*  44:    */     }
/*  45:129 */     int data_length = (length == 2147483647) || (length < 0) || (index + length < 0) ? data.length : Math.min(data.length, index + length);
/*  46:134 */     if ((index < 0) || (index >= data.length))
/*  47:    */     {
/*  48:135 */       String err = "illegal index: " + index + " into array of length " + data.length;
/*  49:136 */       throw new ArrayIndexOutOfBoundsException(err);
/*  50:    */     }
/*  51:139 */     long display_offset = offset + index;
/*  52:140 */     StringBuilder buffer = new StringBuilder(74);
/*  53:142 */     for (int j = index; j < data_length; j += 16)
/*  54:    */     {
/*  55:143 */       int chars_read = data_length - j;
/*  56:145 */       if (chars_read > 16) {
/*  57:146 */         chars_read = 16;
/*  58:    */       }
/*  59:149 */       writeHex(buffer, display_offset, 8, "");
/*  60:150 */       for (int k = 0; k < 16; k++) {
/*  61:151 */         if (k < chars_read) {
/*  62:152 */           writeHex(buffer, data[(k + j)], 2, " ");
/*  63:    */         } else {
/*  64:154 */           buffer.append("   ");
/*  65:    */         }
/*  66:    */       }
/*  67:157 */       buffer.append(' ');
/*  68:158 */       for (int k = 0; k < chars_read; k++) {
/*  69:159 */         buffer.append(toAscii(data[(k + j)]));
/*  70:    */       }
/*  71:161 */       buffer.append(EOL);
/*  72:162 */       display_offset += chars_read;
/*  73:    */     }
/*  74:164 */     return buffer.toString();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static char toAscii(int dataB)
/*  78:    */   {
/*  79:168 */     char charB = (char)(dataB & 0xFF);
/*  80:169 */     if (Character.isISOControl(charB)) {
/*  81:170 */       return '.';
/*  82:    */     }
/*  83:173 */     switch (charB)
/*  84:    */     {
/*  85:    */     case 'Ý': 
/*  86:    */     case 'ÿ': 
/*  87:177 */       charB = '.';
/*  88:178 */       break;
/*  89:    */     }
/*  90:182 */     return charB;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static String toHex(byte[] value)
/*  94:    */   {
/*  95:193 */     StringBuilder retVal = new StringBuilder();
/*  96:194 */     retVal.append('[');
/*  97:195 */     if ((value != null) && (value.length > 0)) {
/*  98:197 */       for (int x = 0; x < value.length; x++)
/*  99:    */       {
/* 100:199 */         if (x > 0) {
/* 101:200 */           retVal.append(", ");
/* 102:    */         }
/* 103:202 */         retVal.append(toHex(value[x]));
/* 104:    */       }
/* 105:    */     }
/* 106:205 */     retVal.append(']');
/* 107:206 */     return retVal.toString();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public static String toHex(short[] value)
/* 111:    */   {
/* 112:217 */     StringBuilder retVal = new StringBuilder();
/* 113:218 */     retVal.append('[');
/* 114:219 */     for (int x = 0; x < value.length; x++)
/* 115:    */     {
/* 116:221 */       if (x > 0) {
/* 117:222 */         retVal.append(", ");
/* 118:    */       }
/* 119:224 */       retVal.append(toHex(value[x]));
/* 120:    */     }
/* 121:226 */     retVal.append(']');
/* 122:227 */     return retVal.toString();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static String toHex(byte[] value, int bytesPerLine)
/* 126:    */   {
/* 127:240 */     if (value.length == 0) {
/* 128:241 */       return ": 0";
/* 129:    */     }
/* 130:243 */     int digits = (int)Math.round(Math.log(value.length) / Math.log(10.0D) + 0.5D);
/* 131:244 */     StringBuilder retVal = new StringBuilder();
/* 132:245 */     writeHex(retVal, 0L, digits, "");
/* 133:246 */     retVal.append(": ");
/* 134:247 */     int x = 0;
/* 135:247 */     for (int i = -1; x < value.length; x++)
/* 136:    */     {
/* 137:248 */       i++;
/* 138:248 */       if (i == bytesPerLine)
/* 139:    */       {
/* 140:249 */         retVal.append('\n');
/* 141:250 */         writeHex(retVal, x, digits, "");
/* 142:251 */         retVal.append(": ");
/* 143:252 */         i = 0;
/* 144:    */       }
/* 145:253 */       else if (x > 0)
/* 146:    */       {
/* 147:254 */         retVal.append(", ");
/* 148:    */       }
/* 149:256 */       retVal.append(toHex(value[x]));
/* 150:    */     }
/* 151:258 */     return retVal.toString();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static String toHex(short value)
/* 155:    */   {
/* 156:268 */     StringBuilder sb = new StringBuilder(4);
/* 157:269 */     writeHex(sb, value & 0xFFFF, 4, "");
/* 158:270 */     return sb.toString();
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static String toHex(byte value)
/* 162:    */   {
/* 163:280 */     StringBuilder sb = new StringBuilder(2);
/* 164:281 */     writeHex(sb, value & 0xFF, 2, "");
/* 165:282 */     return sb.toString();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public static String toHex(int value)
/* 169:    */   {
/* 170:292 */     StringBuilder sb = new StringBuilder(8);
/* 171:293 */     writeHex(sb, value & 0xFFFFFFFF, 8, "");
/* 172:294 */     return sb.toString();
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static String toHex(long value)
/* 176:    */   {
/* 177:304 */     StringBuilder sb = new StringBuilder(16);
/* 178:305 */     writeHex(sb, value, 16, "");
/* 179:306 */     return sb.toString();
/* 180:    */   }
/* 181:    */   
/* 182:    */   public static String toHex(String value)
/* 183:    */   {
/* 184:316 */     return (value == null) || (value.length() == 0) ? "[]" : toHex(value.getBytes(LocaleUtil.CHARSET_1252));
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static void dump(InputStream in, PrintStream out, int start, int bytesToDump)
/* 188:    */     throws IOException
/* 189:    */   {
/* 190:331 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/* 191:332 */     if (bytesToDump == -1)
/* 192:    */     {
/* 193:334 */       int c = in.read();
/* 194:335 */       while (c != -1)
/* 195:    */       {
/* 196:337 */         buf.write(c);
/* 197:338 */         c = in.read();
/* 198:    */       }
/* 199:    */     }
/* 200:    */     else
/* 201:    */     {
/* 202:343 */       int bytesRemaining = bytesToDump;
/* 203:344 */       while (bytesRemaining-- > 0)
/* 204:    */       {
/* 205:346 */         int c = in.read();
/* 206:347 */         if (c == -1) {
/* 207:    */           break;
/* 208:    */         }
/* 209:350 */         buf.write(c);
/* 210:    */       }
/* 211:    */     }
/* 212:354 */     byte[] data = buf.toByteArray();
/* 213:355 */     dump(data, 0L, out, start, data.length);
/* 214:    */   }
/* 215:    */   
/* 216:    */   public static String longToHex(long value)
/* 217:    */   {
/* 218:362 */     StringBuilder sb = new StringBuilder(18);
/* 219:363 */     writeHex(sb, value, 16, "0x");
/* 220:364 */     return sb.toString();
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static String intToHex(int value)
/* 224:    */   {
/* 225:371 */     StringBuilder sb = new StringBuilder(10);
/* 226:372 */     writeHex(sb, value & 0xFFFFFFFF, 8, "0x");
/* 227:373 */     return sb.toString();
/* 228:    */   }
/* 229:    */   
/* 230:    */   public static String shortToHex(int value)
/* 231:    */   {
/* 232:380 */     StringBuilder sb = new StringBuilder(6);
/* 233:381 */     writeHex(sb, value & 0xFFFF, 4, "0x");
/* 234:382 */     return sb.toString();
/* 235:    */   }
/* 236:    */   
/* 237:    */   public static String byteToHex(int value)
/* 238:    */   {
/* 239:389 */     StringBuilder sb = new StringBuilder(4);
/* 240:390 */     writeHex(sb, value & 0xFF, 2, "0x");
/* 241:391 */     return sb.toString();
/* 242:    */   }
/* 243:    */   
/* 244:    */   private static void writeHex(StringBuilder sb, long value, int nDigits, String prefix)
/* 245:    */   {
/* 246:399 */     sb.append(prefix);
/* 247:400 */     char[] buf = new char[nDigits];
/* 248:401 */     long acc = value;
/* 249:402 */     for (int i = nDigits - 1; i >= 0; i--)
/* 250:    */     {
/* 251:403 */       int digit = (int)(acc & 0xF);
/* 252:404 */       buf[i] = ((char)(digit < 10 ? 48 + digit : 65 + digit - 10));
/* 253:405 */       acc >>>= 4;
/* 254:    */     }
/* 255:407 */     sb.append(buf);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static void main(String[] args)
/* 259:    */     throws IOException
/* 260:    */   {
/* 261:412 */     InputStream in = new FileInputStream(args[0]);
/* 262:413 */     byte[] b = IOUtils.toByteArray(in);
/* 263:414 */     in.close();
/* 264:415 */     System.out.println(dump(b, 0L, 0));
/* 265:    */   }
/* 266:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.HexDump
 * JD-Core Version:    0.7.0.1
 */