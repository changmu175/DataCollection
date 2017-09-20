/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import java.math.BigInteger;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.LinkedList;
/*   9:    */ import java.util.List;
/*  10:    */ import org.apache.poi.util.CodePageUtil;
/*  11:    */ import org.apache.poi.util.LittleEndian;
/*  12:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  13:    */ import org.apache.poi.util.POILogFactory;
/*  14:    */ import org.apache.poi.util.POILogger;
/*  15:    */ import org.apache.poi.util.Removal;
/*  16:    */ 
/*  17:    */ public class VariantSupport
/*  18:    */   extends Variant
/*  19:    */ {
/*  20: 56 */   public static final int[] SUPPORTED_TYPES = { 0, 2, 3, 20, 5, 64, 30, 31, 71, 11 };
/*  21: 62 */   private static final POILogger logger = POILogFactory.getLogger(VariantSupport.class);
/*  22:    */   private static boolean logUnsupportedTypes;
/*  23:    */   private static List<Long> unsupportedMessage;
/*  24: 71 */   private static final byte[] paddingBytes = new byte[3];
/*  25:    */   
/*  26:    */   public static void setLogUnsupportedTypes(boolean logUnsupportedTypes)
/*  27:    */   {
/*  28: 82 */     logUnsupportedTypes = logUnsupportedTypes;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static boolean isLogUnsupportedTypes()
/*  32:    */   {
/*  33: 93 */     return logUnsupportedTypes;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected static void writeUnsupportedTypeMessage(UnsupportedVariantTypeException ex)
/*  37:    */   {
/*  38:107 */     if (isLogUnsupportedTypes())
/*  39:    */     {
/*  40:109 */       if (unsupportedMessage == null) {
/*  41:110 */         unsupportedMessage = new LinkedList();
/*  42:    */       }
/*  43:112 */       Long vt = Long.valueOf(ex.getVariantType());
/*  44:113 */       if (!unsupportedMessage.contains(vt))
/*  45:    */       {
/*  46:115 */         logger.log(7, new Object[] { ex.getMessage() });
/*  47:116 */         unsupportedMessage.add(vt);
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isSupportedType(int variantType)
/*  53:    */   {
/*  54:134 */     for (int st : SUPPORTED_TYPES) {
/*  55:135 */       if (variantType == st) {
/*  56:136 */         return true;
/*  57:    */       }
/*  58:    */     }
/*  59:139 */     return false;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static Object read(byte[] src, int offset, int length, long type, int codepage)
/*  63:    */     throws ReadingNotSupportedException, UnsupportedEncodingException
/*  64:    */   {
/*  65:164 */     LittleEndianByteArrayInputStream lei = new LittleEndianByteArrayInputStream(src, offset);
/*  66:165 */     return read(lei, length, type, codepage);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static Object read(LittleEndianByteArrayInputStream lei, int length, long type, int codepage)
/*  70:    */     throws ReadingNotSupportedException, UnsupportedEncodingException
/*  71:    */   {
/*  72:171 */     int offset = lei.getReadIndex();
/*  73:172 */     TypedPropertyValue typedPropertyValue = new TypedPropertyValue((int)type, null);
/*  74:    */     try
/*  75:    */     {
/*  76:174 */       typedPropertyValue.readValue(lei);
/*  77:    */     }
/*  78:    */     catch (UnsupportedOperationException exc)
/*  79:    */     {
/*  80:176 */       int propLength = Math.min(length, lei.available());
/*  81:177 */       byte[] v = new byte[propLength];
/*  82:178 */       lei.readFully(v, 0, propLength);
/*  83:179 */       throw new ReadingNotSupportedException(type, v);
/*  84:    */     }
/*  85:182 */     switch ((int)type)
/*  86:    */     {
/*  87:    */     case 0: 
/*  88:    */     case 3: 
/*  89:    */     case 4: 
/*  90:    */     case 5: 
/*  91:    */     case 16: 
/*  92:    */     case 17: 
/*  93:    */     case 18: 
/*  94:    */     case 19: 
/*  95:    */     case 20: 
/*  96:    */     case 21: 
/*  97:199 */       return typedPropertyValue.getValue();
/*  98:    */     case 2: 
/*  99:206 */       return Integer.valueOf(((Short)typedPropertyValue.getValue()).intValue());
/* 100:    */     case 64: 
/* 101:209 */       Filetime filetime = (Filetime)typedPropertyValue.getValue();
/* 102:210 */       return filetime.getJavaValue();
/* 103:    */     case 30: 
/* 104:213 */       CodePageString cpString = (CodePageString)typedPropertyValue.getValue();
/* 105:214 */       return cpString.getJavaValue(codepage);
/* 106:    */     case 31: 
/* 107:217 */       UnicodeString uniString = (UnicodeString)typedPropertyValue.getValue();
/* 108:218 */       return uniString.toJavaString();
/* 109:    */     case 71: 
/* 110:239 */       ClipboardData clipboardData = (ClipboardData)typedPropertyValue.getValue();
/* 111:240 */       return clipboardData.toByteArray();
/* 112:    */     case 11: 
/* 113:243 */       VariantBool bool = (VariantBool)typedPropertyValue.getValue();
/* 114:244 */       return Boolean.valueOf(bool.getValue());
/* 115:    */     }
/* 116:251 */     int unpadded = lei.getReadIndex() - offset;
/* 117:252 */     lei.setReadIndex(offset);
/* 118:253 */     byte[] v = new byte[unpadded];
/* 119:254 */     lei.readFully(v, 0, unpadded);
/* 120:255 */     throw new ReadingNotSupportedException(type, v);
/* 121:    */   }
/* 122:    */   
/* 123:    */   @Deprecated
/* 124:    */   @Removal(version="3.18")
/* 125:    */   public static String codepageToEncoding(int codepage)
/* 126:    */     throws UnsupportedEncodingException
/* 127:    */   {
/* 128:280 */     return CodePageUtil.codepageToEncoding(codepage);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public static int write(OutputStream out, long type, Object value, int codepage)
/* 132:    */     throws IOException, WritingNotSupportedException
/* 133:    */   {
/* 134:301 */     int length = -1;
/* 135:302 */     switch ((int)type)
/* 136:    */     {
/* 137:    */     case 11: 
/* 138:304 */       if ((value instanceof Boolean))
/* 139:    */       {
/* 140:305 */         int bb = ((Boolean)value).booleanValue() ? 255 : 0;
/* 141:306 */         out.write(bb);
/* 142:307 */         out.write(bb);
/* 143:308 */         length = 2;
/* 144:    */       }
/* 145:309 */       break;
/* 146:    */     case 30: 
/* 147:314 */       if ((value instanceof String))
/* 148:    */       {
/* 149:315 */         CodePageString codePageString = new CodePageString();
/* 150:316 */         codePageString.setJavaValue((String)value, codepage);
/* 151:317 */         length = codePageString.write(out);
/* 152:    */       }
/* 153:318 */       break;
/* 154:    */     case 31: 
/* 155:322 */       if ((value instanceof String))
/* 156:    */       {
/* 157:323 */         UnicodeString uniString = new UnicodeString();
/* 158:324 */         uniString.setJavaValue((String)value);
/* 159:325 */         length = uniString.write(out);
/* 160:    */       }
/* 161:326 */       break;
/* 162:    */     case 71: 
/* 163:330 */       if ((value instanceof byte[]))
/* 164:    */       {
/* 165:331 */         byte[] cf = (byte[])value;
/* 166:332 */         out.write(cf);
/* 167:333 */         length = cf.length;
/* 168:    */       }
/* 169:334 */       break;
/* 170:    */     case 0: 
/* 171:338 */       LittleEndian.putUInt(0L, out);
/* 172:339 */       length = 4;
/* 173:340 */       break;
/* 174:    */     case 2: 
/* 175:343 */       if ((value instanceof Number))
/* 176:    */       {
/* 177:344 */         LittleEndian.putShort(out, ((Number)value).shortValue());
/* 178:345 */         length = 2;
/* 179:    */       }
/* 180:    */       break;
/* 181:    */     case 18: 
/* 182:350 */       if ((value instanceof Number))
/* 183:    */       {
/* 184:351 */         LittleEndian.putUShort(((Number)value).intValue(), out);
/* 185:352 */         length = 2;
/* 186:    */       }
/* 187:    */       break;
/* 188:    */     case 3: 
/* 189:357 */       if ((value instanceof Number))
/* 190:    */       {
/* 191:358 */         LittleEndian.putInt(((Number)value).intValue(), out);
/* 192:359 */         length = 4;
/* 193:    */       }
/* 194:    */       break;
/* 195:    */     case 19: 
/* 196:364 */       if ((value instanceof Number))
/* 197:    */       {
/* 198:365 */         LittleEndian.putUInt(((Number)value).longValue(), out);
/* 199:366 */         length = 4;
/* 200:    */       }
/* 201:    */       break;
/* 202:    */     case 20: 
/* 203:371 */       if ((value instanceof Number))
/* 204:    */       {
/* 205:372 */         LittleEndian.putLong(((Number)value).longValue(), out);
/* 206:373 */         length = 8;
/* 207:    */       }
/* 208:    */       break;
/* 209:    */     case 21: 
/* 210:378 */       if ((value instanceof Number))
/* 211:    */       {
/* 212:379 */         BigInteger bi = (value instanceof BigInteger) ? (BigInteger)value : BigInteger.valueOf(((Number)value).longValue());
/* 213:380 */         if (bi.bitLength() > 64) {
/* 214:381 */           throw new WritingNotSupportedException(type, value);
/* 215:    */         }
/* 216:384 */         byte[] biBytesBE = bi.toByteArray();byte[] biBytesLE = new byte[8];
/* 217:385 */         int i = biBytesBE.length;
/* 218:386 */         for (byte b : biBytesBE)
/* 219:    */         {
/* 220:387 */           if (i <= 8) {
/* 221:388 */             biBytesLE[(i - 1)] = b;
/* 222:    */           }
/* 223:390 */           i--;
/* 224:    */         }
/* 225:393 */         out.write(biBytesLE);
/* 226:394 */         length = 8;
/* 227:    */       }
/* 228:395 */       break;
/* 229:    */     case 4: 
/* 230:400 */       if ((value instanceof Number))
/* 231:    */       {
/* 232:401 */         int floatBits = Float.floatToIntBits(((Number)value).floatValue());
/* 233:402 */         LittleEndian.putInt(floatBits, out);
/* 234:403 */         length = 4;
/* 235:    */       }
/* 236:404 */       break;
/* 237:    */     case 5: 
/* 238:409 */       if ((value instanceof Number))
/* 239:    */       {
/* 240:410 */         LittleEndian.putDouble(((Number)value).doubleValue(), out);
/* 241:411 */         length = 8;
/* 242:    */       }
/* 243:    */       break;
/* 244:    */     case 64: 
/* 245:416 */       Filetime filetimeValue = (value instanceof Date) ? new Filetime((Date)value) : new Filetime();
/* 246:417 */       length = filetimeValue.write(out);
/* 247:418 */       break;
/* 248:    */     }
/* 249:426 */     if (length == -1) {
/* 250:427 */       if ((value instanceof byte[]))
/* 251:    */       {
/* 252:428 */         byte[] b = (byte[])value;
/* 253:429 */         out.write(b);
/* 254:430 */         length = b.length;
/* 255:431 */         writeUnsupportedTypeMessage(new WritingNotSupportedException(type, value));
/* 256:    */       }
/* 257:    */       else
/* 258:    */       {
/* 259:433 */         throw new WritingNotSupportedException(type, value);
/* 260:    */       }
/* 261:    */     }
/* 262:438 */     int padding = 4 - (length & 0x3) & 0x3;
/* 263:439 */     out.write(paddingBytes, 0, padding);
/* 264:    */     
/* 265:441 */     return length + padding;
/* 266:    */   }
/* 267:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.VariantSupport
 * JD-Core Version:    0.7.0.1
 */