/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.Closeable;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.io.PushbackInputStream;
/*  10:    */ import java.nio.ByteBuffer;
/*  11:    */ import java.nio.channels.ReadableByteChannel;
/*  12:    */ import java.util.zip.CRC32;
/*  13:    */ import java.util.zip.Checksum;
/*  14:    */ import org.apache.poi.EmptyFileException;
/*  15:    */ import org.apache.poi.POIDocument;
/*  16:    */ import org.apache.poi.ss.usermodel.Workbook;
/*  17:    */ 
/*  18:    */ public final class IOUtils
/*  19:    */ {
/*  20: 37 */   private static final POILogger logger = POILogFactory.getLogger(IOUtils.class);
/*  21:    */   private static final int SKIP_BUFFER_SIZE = 2048;
/*  22:    */   private static byte[] SKIP_BYTE_BUFFER;
/*  23:    */   
/*  24:    */   public static byte[] peekFirst8Bytes(InputStream stream)
/*  25:    */     throws IOException, EmptyFileException
/*  26:    */   {
/*  27: 57 */     return peekFirstNBytes(stream, 8);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static byte[] peekFirstNBytes(InputStream stream, int limit)
/*  31:    */     throws IOException, EmptyFileException
/*  32:    */   {
/*  33: 68 */     stream.mark(limit);
/*  34: 69 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(limit);
/*  35: 70 */     copy(new BoundedInputStream(stream, limit), bos);
/*  36:    */     
/*  37: 72 */     int readBytes = bos.size();
/*  38: 73 */     if (readBytes == 0) {
/*  39: 74 */       throw new EmptyFileException();
/*  40:    */     }
/*  41: 77 */     if (readBytes < limit) {
/*  42: 78 */       bos.write(new byte[limit - readBytes]);
/*  43:    */     }
/*  44: 80 */     byte[] peekedBytes = bos.toByteArray();
/*  45: 81 */     if ((stream instanceof PushbackInputStream))
/*  46:    */     {
/*  47: 82 */       PushbackInputStream pin = (PushbackInputStream)stream;
/*  48: 83 */       pin.unread(peekedBytes, 0, readBytes);
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52: 85 */       stream.reset();
/*  53:    */     }
/*  54: 88 */     return peekedBytes;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static byte[] toByteArray(InputStream stream)
/*  58:    */     throws IOException
/*  59:    */   {
/*  60: 97 */     return toByteArray(stream, 2147483647);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static byte[] toByteArray(InputStream stream, int length)
/*  64:    */     throws IOException
/*  65:    */   {
/*  66:104 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(length == 2147483647 ? 4096 : length);
/*  67:    */     
/*  68:106 */     byte[] buffer = new byte[4096];
/*  69:107 */     int totalBytes = 0;
/*  70:    */     int readBytes;
/*  71:    */     do
/*  72:    */     {
/*  73:109 */       readBytes = stream.read(buffer, 0, Math.min(buffer.length, length - totalBytes));
/*  74:110 */       totalBytes += Math.max(readBytes, 0);
/*  75:111 */       if (readBytes > 0) {
/*  76:112 */         baos.write(buffer, 0, readBytes);
/*  77:    */       }
/*  78:114 */     } while ((totalBytes < length) && (readBytes > -1));
/*  79:116 */     if ((length != 2147483647) && (totalBytes < length)) {
/*  80:117 */       throw new IOException("unexpected EOF");
/*  81:    */     }
/*  82:120 */     return baos.toByteArray();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static byte[] toByteArray(ByteBuffer buffer, int length)
/*  86:    */   {
/*  87:130 */     if ((buffer.hasArray()) && (buffer.arrayOffset() == 0)) {
/*  88:132 */       return buffer.array();
/*  89:    */     }
/*  90:135 */     byte[] data = new byte[length];
/*  91:136 */     buffer.get(data);
/*  92:137 */     return data;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static int readFully(InputStream in, byte[] b)
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:144 */     return readFully(in, b, 0, b.length);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static int readFully(InputStream in, byte[] b, int off, int len)
/* 102:    */     throws IOException
/* 103:    */   {
/* 104:162 */     int total = 0;
/* 105:    */     for (;;)
/* 106:    */     {
/* 107:164 */       int got = in.read(b, off + total, len - total);
/* 108:165 */       if (got < 0) {
/* 109:166 */         return total == 0 ? -1 : total;
/* 110:    */       }
/* 111:168 */       total += got;
/* 112:169 */       if (total == len) {
/* 113:170 */         return total;
/* 114:    */       }
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   public static int readFully(ReadableByteChannel channel, ByteBuffer b)
/* 119:    */     throws IOException
/* 120:    */   {
/* 121:187 */     int total = 0;
/* 122:    */     for (;;)
/* 123:    */     {
/* 124:189 */       int got = channel.read(b);
/* 125:190 */       if (got < 0) {
/* 126:191 */         return total == 0 ? -1 : total;
/* 127:    */       }
/* 128:193 */       total += got;
/* 129:194 */       if ((total == b.capacity()) || (b.position() == b.capacity())) {
/* 130:195 */         return total;
/* 131:    */       }
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static void write(POIDocument doc, OutputStream out)
/* 136:    */     throws IOException
/* 137:    */   {
/* 138:    */     try
/* 139:    */     {
/* 140:213 */       doc.write(out);
/* 141:    */     }
/* 142:    */     finally
/* 143:    */     {
/* 144:215 */       closeQuietly(out);
/* 145:    */     }
/* 146:    */   }
/* 147:    */   
/* 148:    */   public static void write(Workbook doc, OutputStream out)
/* 149:    */     throws IOException
/* 150:    */   {
/* 151:    */     try
/* 152:    */     {
/* 153:232 */       doc.write(out);
/* 154:    */     }
/* 155:    */     finally
/* 156:    */     {
/* 157:234 */       closeQuietly(out);
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void writeAndClose(POIDocument doc, OutputStream out)
/* 162:    */     throws IOException
/* 163:    */   {
/* 164:    */     try
/* 165:    */     {
/* 166:252 */       write(doc, out);
/* 167:    */     }
/* 168:    */     finally
/* 169:    */     {
/* 170:254 */       closeQuietly(doc);
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static void writeAndClose(POIDocument doc, File out)
/* 175:    */     throws IOException
/* 176:    */   {
/* 177:    */     try
/* 178:    */     {
/* 179:271 */       doc.write(out);
/* 180:    */     }
/* 181:    */     finally
/* 182:    */     {
/* 183:273 */       closeQuietly(doc);
/* 184:    */     }
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static void writeAndClose(POIDocument doc)
/* 188:    */     throws IOException
/* 189:    */   {
/* 190:    */     try
/* 191:    */     {
/* 192:289 */       doc.write();
/* 193:    */     }
/* 194:    */     finally
/* 195:    */     {
/* 196:291 */       closeQuietly(doc);
/* 197:    */     }
/* 198:    */   }
/* 199:    */   
/* 200:    */   public static void writeAndClose(Workbook doc, OutputStream out)
/* 201:    */     throws IOException
/* 202:    */   {
/* 203:    */     try
/* 204:    */     {
/* 205:299 */       doc.write(out);
/* 206:    */     }
/* 207:    */     finally
/* 208:    */     {
/* 209:301 */       closeQuietly(doc);
/* 210:    */     }
/* 211:    */   }
/* 212:    */   
/* 213:    */   public static void copy(InputStream inp, OutputStream out)
/* 214:    */     throws IOException
/* 215:    */   {
/* 216:310 */     byte[] buff = new byte[4096];
/* 217:    */     int count;
/* 218:312 */     while ((count = inp.read(buff)) != -1)
/* 219:    */     {
/* 220:313 */       if (count < -1) {
/* 221:314 */         throw new RecordFormatException("Can't have read < -1 bytes");
/* 222:    */       }
/* 223:316 */       if (count > 0) {
/* 224:317 */         out.write(buff, 0, count);
/* 225:    */       }
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static long calculateChecksum(byte[] data)
/* 230:    */   {
/* 231:326 */     Checksum sum = new CRC32();
/* 232:327 */     sum.update(data, 0, data.length);
/* 233:328 */     return sum.getValue();
/* 234:    */   }
/* 235:    */   
/* 236:    */   public static long calculateChecksum(InputStream stream)
/* 237:    */     throws IOException
/* 238:    */   {
/* 239:338 */     Checksum sum = new CRC32();
/* 240:    */     
/* 241:340 */     byte[] buf = new byte[4096];
/* 242:    */     int count;
/* 243:342 */     while ((count = stream.read(buf)) != -1) {
/* 244:343 */       if (count > 0) {
/* 245:344 */         sum.update(buf, 0, count);
/* 246:    */       }
/* 247:    */     }
/* 248:347 */     return sum.getValue();
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static void closeQuietly(Closeable closeable)
/* 252:    */   {
/* 253:360 */     if (closeable == null) {
/* 254:361 */       return;
/* 255:    */     }
/* 256:    */     try
/* 257:    */     {
/* 258:365 */       closeable.close();
/* 259:    */     }
/* 260:    */     catch (Exception exc)
/* 261:    */     {
/* 262:367 */       logger.log(7, new Object[] { "Unable to close resource: " + exc, exc });
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   public static long skipFully(InputStream input, long toSkip)
/* 267:    */     throws IOException
/* 268:    */   {
/* 269:405 */     if (toSkip < 0L) {
/* 270:406 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/* 271:    */     }
/* 272:408 */     if (toSkip == 0L) {
/* 273:409 */       return 0L;
/* 274:    */     }
/* 275:416 */     if (SKIP_BYTE_BUFFER == null) {
/* 276:417 */       SKIP_BYTE_BUFFER = new byte[2048];
/* 277:    */     }
/* 278:419 */     long remain = toSkip;
/* 279:420 */     while (remain > 0L)
/* 280:    */     {
/* 281:422 */       long n = input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
/* 282:423 */       if (n < 0L) {
/* 283:    */         break;
/* 284:    */       }
/* 285:426 */       remain -= n;
/* 286:    */     }
/* 287:428 */     if (toSkip == remain) {
/* 288:429 */       return -1L;
/* 289:    */     }
/* 290:431 */     return toSkip - remain;
/* 291:    */   }
/* 292:    */   
/* 293:    */   public static byte[] safelyAllocate(long length, int maxLength)
/* 294:    */   {
/* 295:435 */     if (length < 0L) {
/* 296:436 */       throw new RecordFormatException("Can't allocate an array of length < 0");
/* 297:    */     }
/* 298:438 */     if (length > 2147483647L) {
/* 299:439 */       throw new RecordFormatException("Can't allocate an array > 2147483647");
/* 300:    */     }
/* 301:441 */     if (length > maxLength) {
/* 302:442 */       throw new RecordFormatException("Not allowed to allocate an array > " + maxLength + " for this record type." + "If the file is not corrupt, please open an issue on bugzilla to request " + "increasing the maximum allowable size for this record type");
/* 303:    */     }
/* 304:447 */     return new byte[(int)length];
/* 305:    */   }
/* 306:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.IOUtils
 * JD-Core Version:    0.7.0.1
 */