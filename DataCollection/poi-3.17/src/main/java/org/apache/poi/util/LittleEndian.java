/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ 
/*   7:    */ public class LittleEndian
/*   8:    */   implements LittleEndianConsts
/*   9:    */ {
/*  10:    */   public static final class BufferUnderrunException
/*  11:    */     extends IOException
/*  12:    */   {
/*  13:    */     private static final long serialVersionUID = 8736973884877006145L;
/*  14:    */     
/*  15:    */     BufferUnderrunException()
/*  16:    */     {
/*  17: 52 */       super();
/*  18:    */     }
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static byte[] getByteArray(byte[] data, int offset, int size)
/*  22:    */   {
/*  23: 71 */     byte[] copy = new byte[size];
/*  24: 72 */     System.arraycopy(data, offset, copy, 0, size);
/*  25:    */     
/*  26: 74 */     return copy;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static double getDouble(byte[] data)
/*  30:    */   {
/*  31: 88 */     return Double.longBitsToDouble(getLong(data, 0));
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static double getDouble(byte[] data, int offset)
/*  35:    */   {
/*  36:104 */     return Double.longBitsToDouble(getLong(data, offset));
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static float getFloat(byte[] data)
/*  40:    */   {
/*  41:118 */     return getFloat(data, 0);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static float getFloat(byte[] data, int offset)
/*  45:    */   {
/*  46:134 */     return Float.intBitsToFloat(getInt(data, offset));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static int getInt(byte[] data)
/*  50:    */   {
/*  51:146 */     return getInt(data, 0);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static int getInt(byte[] data, int offset)
/*  55:    */   {
/*  56:160 */     int i = offset;
/*  57:161 */     int b0 = data[(i++)] & 0xFF;
/*  58:162 */     int b1 = data[(i++)] & 0xFF;
/*  59:163 */     int b2 = data[(i++)] & 0xFF;
/*  60:164 */     int b3 = data[(i++)] & 0xFF;
/*  61:165 */     return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static long getLong(byte[] data)
/*  65:    */   {
/*  66:177 */     return getLong(data, 0);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static long getLong(byte[] data, int offset)
/*  70:    */   {
/*  71:191 */     long result = 0xFF & data[(offset + 7)];
/*  72:193 */     for (int j = offset + 8 - 1; j >= offset; j--)
/*  73:    */     {
/*  74:195 */       result <<= 8;
/*  75:196 */       result |= 0xFF & data[j];
/*  76:    */     }
/*  77:198 */     return result;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static short getShort(byte[] data)
/*  81:    */   {
/*  82:210 */     return getShort(data, 0);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static short getShort(byte[] data, int offset)
/*  86:    */   {
/*  87:224 */     int b0 = data[offset] & 0xFF;
/*  88:225 */     int b1 = data[(offset + 1)] & 0xFF;
/*  89:226 */     return (short)((b1 << 8) + (b0 << 0));
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static short[] getShortArray(byte[] data, int offset, int size)
/*  93:    */   {
/*  94:243 */     short[] result = new short[size / 2];
/*  95:244 */     for (int i = 0; i < result.length; i++) {
/*  96:246 */       result[i] = getShort(data, offset + i * 2);
/*  97:    */     }
/*  98:248 */     return result;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static short getUByte(byte[] data)
/* 102:    */   {
/* 103:260 */     return (short)(data[0] & 0xFF);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static short getUByte(byte[] data, int offset)
/* 107:    */   {
/* 108:274 */     return (short)(data[offset] & 0xFF);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static long getUInt(byte[] data)
/* 112:    */   {
/* 113:286 */     return getUInt(data, 0);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static long getUInt(byte[] data, int offset)
/* 117:    */   {
/* 118:300 */     long retNum = getInt(data, offset);
/* 119:301 */     return retNum & 0xFFFFFFFF;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static int getUShort(byte[] data)
/* 123:    */   {
/* 124:313 */     return getUShort(data, 0);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static int getUShort(byte[] data, int offset)
/* 128:    */   {
/* 129:327 */     int b0 = data[offset] & 0xFF;
/* 130:328 */     int b1 = data[(offset + 1)] & 0xFF;
/* 131:329 */     return (b1 << 8) + (b0 << 0);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static void putByte(byte[] data, int offset, int value)
/* 135:    */   {
/* 136:343 */     data[offset] = ((byte)value);
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static void putDouble(byte[] data, int offset, double value)
/* 140:    */   {
/* 141:358 */     putLong(data, offset, Double.doubleToLongBits(value));
/* 142:    */   }
/* 143:    */   
/* 144:    */   public static void putDouble(double value, OutputStream outputStream)
/* 145:    */     throws IOException
/* 146:    */   {
/* 147:374 */     putLong(Double.doubleToLongBits(value), outputStream);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static void putFloat(byte[] data, int offset, float value)
/* 151:    */   {
/* 152:389 */     putInt(data, offset, Float.floatToIntBits(value));
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static void putFloat(float value, OutputStream outputStream)
/* 156:    */     throws IOException
/* 157:    */   {
/* 158:405 */     putInt(Float.floatToIntBits(value), outputStream);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void putInt(byte[] data, int offset, int value)
/* 162:    */   {
/* 163:420 */     int i = offset;
/* 164:421 */     data[(i++)] = ((byte)(value >>> 0 & 0xFF));
/* 165:422 */     data[(i++)] = ((byte)(value >>> 8 & 0xFF));
/* 166:423 */     data[(i++)] = ((byte)(value >>> 16 & 0xFF));
/* 167:424 */     data[(i++)] = ((byte)(value >>> 24 & 0xFF));
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static void putInt(int value, OutputStream outputStream)
/* 171:    */     throws IOException
/* 172:    */   {
/* 173:440 */     outputStream.write((byte)(value >>> 0 & 0xFF));
/* 174:441 */     outputStream.write((byte)(value >>> 8 & 0xFF));
/* 175:442 */     outputStream.write((byte)(value >>> 16 & 0xFF));
/* 176:443 */     outputStream.write((byte)(value >>> 24 & 0xFF));
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static void putLong(byte[] data, int offset, long value)
/* 180:    */   {
/* 181:458 */     data[(offset + 0)] = ((byte)(int)(value >>> 0 & 0xFF));
/* 182:459 */     data[(offset + 1)] = ((byte)(int)(value >>> 8 & 0xFF));
/* 183:460 */     data[(offset + 2)] = ((byte)(int)(value >>> 16 & 0xFF));
/* 184:461 */     data[(offset + 3)] = ((byte)(int)(value >>> 24 & 0xFF));
/* 185:462 */     data[(offset + 4)] = ((byte)(int)(value >>> 32 & 0xFF));
/* 186:463 */     data[(offset + 5)] = ((byte)(int)(value >>> 40 & 0xFF));
/* 187:464 */     data[(offset + 6)] = ((byte)(int)(value >>> 48 & 0xFF));
/* 188:465 */     data[(offset + 7)] = ((byte)(int)(value >>> 56 & 0xFF));
/* 189:    */   }
/* 190:    */   
/* 191:    */   public static void putLong(long value, OutputStream outputStream)
/* 192:    */     throws IOException
/* 193:    */   {
/* 194:481 */     outputStream.write((byte)(int)(value >>> 0 & 0xFF));
/* 195:482 */     outputStream.write((byte)(int)(value >>> 8 & 0xFF));
/* 196:483 */     outputStream.write((byte)(int)(value >>> 16 & 0xFF));
/* 197:484 */     outputStream.write((byte)(int)(value >>> 24 & 0xFF));
/* 198:485 */     outputStream.write((byte)(int)(value >>> 32 & 0xFF));
/* 199:486 */     outputStream.write((byte)(int)(value >>> 40 & 0xFF));
/* 200:487 */     outputStream.write((byte)(int)(value >>> 48 & 0xFF));
/* 201:488 */     outputStream.write((byte)(int)(value >>> 56 & 0xFF));
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static void putShort(byte[] data, int offset, short value)
/* 205:    */   {
/* 206:503 */     int i = offset;
/* 207:504 */     data[(i++)] = ((byte)(value >>> 0 & 0xFF));
/* 208:505 */     data[(i++)] = ((byte)(value >>> 8 & 0xFF));
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static void putShort(OutputStream outputStream, short value)
/* 212:    */     throws IOException
/* 213:    */   {
/* 214:521 */     outputStream.write((byte)(value >>> 0 & 0xFF));
/* 215:522 */     outputStream.write((byte)(value >>> 8 & 0xFF));
/* 216:    */   }
/* 217:    */   
/* 218:    */   public static void putShortArray(byte[] data, int startOffset, short[] value)
/* 219:    */   {
/* 220:538 */     int offset = startOffset;
/* 221:539 */     for (short s : value)
/* 222:    */     {
/* 223:541 */       putShort(data, offset, s);
/* 224:542 */       offset += 2;
/* 225:    */     }
/* 226:    */   }
/* 227:    */   
/* 228:    */   public static void putUByte(byte[] data, int offset, short value)
/* 229:    */   {
/* 230:561 */     data[offset] = ((byte)(value & 0xFF));
/* 231:    */   }
/* 232:    */   
/* 233:    */   public static void putUInt(byte[] data, int offset, long value)
/* 234:    */   {
/* 235:579 */     int i = offset;
/* 236:580 */     data[(i++)] = ((byte)(int)(value >>> 0 & 0xFF));
/* 237:581 */     data[(i++)] = ((byte)(int)(value >>> 8 & 0xFF));
/* 238:582 */     data[(i++)] = ((byte)(int)(value >>> 16 & 0xFF));
/* 239:583 */     data[(i++)] = ((byte)(int)(value >>> 24 & 0xFF));
/* 240:    */   }
/* 241:    */   
/* 242:    */   public static void putUInt(long value, OutputStream outputStream)
/* 243:    */     throws IOException
/* 244:    */   {
/* 245:599 */     outputStream.write((byte)(int)(value >>> 0 & 0xFF));
/* 246:600 */     outputStream.write((byte)(int)(value >>> 8 & 0xFF));
/* 247:601 */     outputStream.write((byte)(int)(value >>> 16 & 0xFF));
/* 248:602 */     outputStream.write((byte)(int)(value >>> 24 & 0xFF));
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static void putUShort(byte[] data, int offset, int value)
/* 252:    */   {
/* 253:620 */     int i = offset;
/* 254:621 */     data[(i++)] = ((byte)(value >>> 0 & 0xFF));
/* 255:622 */     data[(i++)] = ((byte)(value >>> 8 & 0xFF));
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static void putUShort(int value, OutputStream outputStream)
/* 259:    */     throws IOException
/* 260:    */   {
/* 261:638 */     outputStream.write((byte)(value >>> 0 & 0xFF));
/* 262:639 */     outputStream.write((byte)(value >>> 8 & 0xFF));
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static int readInt(InputStream stream)
/* 266:    */     throws IOException, BufferUnderrunException
/* 267:    */   {
/* 268:656 */     int ch1 = stream.read();
/* 269:657 */     int ch2 = stream.read();
/* 270:658 */     int ch3 = stream.read();
/* 271:659 */     int ch4 = stream.read();
/* 272:660 */     if ((ch1 | ch2 | ch3 | ch4) < 0) {
/* 273:662 */       throw new BufferUnderrunException();
/* 274:    */     }
/* 275:664 */     return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public static long readUInt(InputStream stream)
/* 279:    */     throws IOException, BufferUnderrunException
/* 280:    */   {
/* 281:681 */     long retNum = readInt(stream);
/* 282:682 */     return retNum & 0xFFFFFFFF;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public static long readLong(InputStream stream)
/* 286:    */     throws IOException, BufferUnderrunException
/* 287:    */   {
/* 288:699 */     int ch1 = stream.read();
/* 289:700 */     int ch2 = stream.read();
/* 290:701 */     int ch3 = stream.read();
/* 291:702 */     int ch4 = stream.read();
/* 292:703 */     int ch5 = stream.read();
/* 293:704 */     int ch6 = stream.read();
/* 294:705 */     int ch7 = stream.read();
/* 295:706 */     int ch8 = stream.read();
/* 296:707 */     if ((ch1 | ch2 | ch3 | ch4 | ch5 | ch6 | ch7 | ch8) < 0) {
/* 297:709 */       throw new BufferUnderrunException();
/* 298:    */     }
/* 299:712 */     return (ch8 << 56) + (ch7 << 48) + (ch6 << 40) + (ch5 << 32) + (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
/* 300:    */   }
/* 301:    */   
/* 302:    */   public static short readShort(InputStream stream)
/* 303:    */     throws IOException, BufferUnderrunException
/* 304:    */   {
/* 305:733 */     return (short)readUShort(stream);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public static int readUShort(InputStream stream)
/* 309:    */     throws IOException, BufferUnderrunException
/* 310:    */   {
/* 311:739 */     int ch1 = stream.read();
/* 312:740 */     int ch2 = stream.read();
/* 313:741 */     if ((ch1 | ch2) < 0) {
/* 314:743 */       throw new BufferUnderrunException();
/* 315:    */     }
/* 316:745 */     return (ch2 << 8) + (ch1 << 0);
/* 317:    */   }
/* 318:    */   
/* 319:    */   public static int ubyteToInt(byte b)
/* 320:    */   {
/* 321:758 */     return b & 0xFF;
/* 322:    */   }
/* 323:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.util.LittleEndian

 * JD-Core Version:    0.7.0.1

 */