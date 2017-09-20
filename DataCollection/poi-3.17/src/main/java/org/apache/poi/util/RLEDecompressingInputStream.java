/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.util.Locale;
/*   8:    */ 
/*   9:    */ public class RLEDecompressingInputStream
/*  10:    */   extends InputStream
/*  11:    */ {
/*  12: 36 */   private static final int[] POWER2 = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768 };
/*  13:    */   private final InputStream in;
/*  14:    */   private final byte[] buf;
/*  15:    */   private int pos;
/*  16:    */   private int len;
/*  17:    */   
/*  18:    */   public RLEDecompressingInputStream(InputStream in)
/*  19:    */     throws IOException
/*  20:    */   {
/*  21: 74 */     this.in = in;
/*  22: 75 */     this.buf = new byte[4096];
/*  23: 76 */     this.pos = 0;
/*  24: 77 */     int header = in.read();
/*  25: 78 */     if (header != 1) {
/*  26: 79 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Header byte 0x01 expected, received 0x%02X", new Object[] { Integer.valueOf(header & 0xFF) }));
/*  27:    */     }
/*  28: 81 */     this.len = readChunk();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int read()
/*  32:    */     throws IOException
/*  33:    */   {
/*  34: 86 */     if (this.len == -1) {
/*  35: 87 */       return -1;
/*  36:    */     }
/*  37: 89 */     if ((this.pos >= this.len) && 
/*  38: 90 */       ((this.len = readChunk()) == -1)) {
/*  39: 91 */       return -1;
/*  40:    */     }
/*  41: 94 */     return this.buf[(this.pos++)] & 0xFF;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public int read(byte[] b)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47: 99 */     return read(b, 0, b.length);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int read(byte[] b, int off, int l)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53:104 */     if (this.len == -1) {
/*  54:105 */       return -1;
/*  55:    */     }
/*  56:107 */     int offset = off;
/*  57:108 */     int length = l;
/*  58:109 */     while (length > 0)
/*  59:    */     {
/*  60:110 */       if ((this.pos >= this.len) && 
/*  61:111 */         ((this.len = readChunk()) == -1)) {
/*  62:112 */         return offset > off ? offset - off : -1;
/*  63:    */       }
/*  64:115 */       int c = Math.min(length, this.len - this.pos);
/*  65:116 */       System.arraycopy(this.buf, this.pos, b, offset, c);
/*  66:117 */       this.pos += c;
/*  67:118 */       length -= c;
/*  68:119 */       offset += c;
/*  69:    */     }
/*  70:121 */     return l;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public long skip(long n)
/*  74:    */     throws IOException
/*  75:    */   {
/*  76:126 */     long length = n;
/*  77:127 */     while (length > 0L)
/*  78:    */     {
/*  79:128 */       if ((this.pos >= this.len) && 
/*  80:129 */         ((this.len = readChunk()) == -1)) {
/*  81:130 */         return -1L;
/*  82:    */       }
/*  83:133 */       int c = (int)Math.min(n, this.len - this.pos);
/*  84:134 */       this.pos += c;
/*  85:135 */       length -= c;
/*  86:    */     }
/*  87:137 */     return n;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int available()
/*  91:    */   {
/*  92:142 */     return this.len > 0 ? this.len - this.pos : 0;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void close()
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:147 */     this.in.close();
/*  99:    */   }
/* 100:    */   
/* 101:    */   private int readChunk()
/* 102:    */     throws IOException
/* 103:    */   {
/* 104:157 */     this.pos = 0;
/* 105:158 */     int w = readShort(this.in);
/* 106:159 */     if (w == -1) {
/* 107:160 */       return -1;
/* 108:    */     }
/* 109:162 */     int chunkSize = (w & 0xFFF) + 1;
/* 110:163 */     if ((w & 0x7000) != 12288) {
/* 111:164 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Chunksize header A should be 0x3000, received 0x%04X", new Object[] { Integer.valueOf(w & 0xE000) }));
/* 112:    */     }
/* 113:166 */     boolean rawChunk = (w & 0x8000) == 0;
/* 114:167 */     if (rawChunk)
/* 115:    */     {
/* 116:168 */       if (this.in.read(this.buf, 0, chunkSize) < chunkSize) {
/* 117:169 */         throw new IllegalStateException(String.format(Locale.ROOT, "Not enough bytes read, expected %d", new Object[] { Integer.valueOf(chunkSize) }));
/* 118:    */       }
/* 119:171 */       return chunkSize;
/* 120:    */     }
/* 121:173 */     int inOffset = 0;
/* 122:174 */     int outOffset = 0;
/* 123:175 */     while (inOffset < chunkSize)
/* 124:    */     {
/* 125:176 */       int tokenFlags = this.in.read();
/* 126:177 */       inOffset++;
/* 127:178 */       if (tokenFlags == -1) {
/* 128:    */         break;
/* 129:    */       }
/* 130:181 */       for (int n = 0; n < 8; n++)
/* 131:    */       {
/* 132:182 */         if (inOffset >= chunkSize) {
/* 133:    */           break;
/* 134:    */         }
/* 135:185 */         if ((tokenFlags & POWER2[n]) == 0)
/* 136:    */         {
/* 137:187 */           int b = this.in.read();
/* 138:188 */           if (b == -1) {
/* 139:189 */             return -1;
/* 140:    */           }
/* 141:191 */           this.buf[(outOffset++)] = ((byte)b);
/* 142:192 */           inOffset++;
/* 143:    */         }
/* 144:    */         else
/* 145:    */         {
/* 146:195 */           int token = readShort(this.in);
/* 147:196 */           if (token == -1) {
/* 148:197 */             return -1;
/* 149:    */           }
/* 150:199 */           inOffset += 2;
/* 151:200 */           int copyLenBits = getCopyLenBits(outOffset - 1);
/* 152:201 */           int copyOffset = (token >> copyLenBits) + 1;
/* 153:202 */           int copyLen = (token & POWER2[copyLenBits] - 1) + 3;
/* 154:203 */           int startPos = outOffset - copyOffset;
/* 155:204 */           int endPos = startPos + copyLen;
/* 156:205 */           for (int i = startPos; i < endPos; i++) {
/* 157:206 */             this.buf[(outOffset++)] = this.buf[i];
/* 158:    */           }
/* 159:    */         }
/* 160:    */       }
/* 161:    */     }
/* 162:211 */     return outOffset;
/* 163:    */   }
/* 164:    */   
/* 165:    */   static int getCopyLenBits(int offset)
/* 166:    */   {
/* 167:222 */     for (int n = 11; n >= 4; n--) {
/* 168:223 */       if ((offset & POWER2[n]) != 0) {
/* 169:224 */         return 15 - n;
/* 170:    */       }
/* 171:    */     }
/* 172:227 */     return 12;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public int readShort()
/* 176:    */     throws IOException
/* 177:    */   {
/* 178:237 */     return readShort(this);
/* 179:    */   }
/* 180:    */   
/* 181:    */   public int readInt()
/* 182:    */     throws IOException
/* 183:    */   {
/* 184:247 */     return readInt(this);
/* 185:    */   }
/* 186:    */   
/* 187:    */   private int readShort(InputStream stream)
/* 188:    */     throws IOException
/* 189:    */   {
/* 190:    */     int b0;
/* 191:252 */     if ((b0 = stream.read()) == -1) {
/* 192:253 */       return -1;
/* 193:    */     }
/* 194:    */     int b1;
/* 195:255 */     if ((b1 = stream.read()) == -1) {
/* 196:256 */       return -1;
/* 197:    */     }
/* 198:258 */     return b0 & 0xFF | (b1 & 0xFF) << 8;
/* 199:    */   }
/* 200:    */   
/* 201:    */   private int readInt(InputStream stream)
/* 202:    */     throws IOException
/* 203:    */   {
/* 204:    */     int b0;
/* 205:263 */     if ((b0 = stream.read()) == -1) {
/* 206:264 */       return -1;
/* 207:    */     }
/* 208:    */     int b1;
/* 209:266 */     if ((b1 = stream.read()) == -1) {
/* 210:267 */       return -1;
/* 211:    */     }
/* 212:    */     int b2;
/* 213:269 */     if ((b2 = stream.read()) == -1) {
/* 214:270 */       return -1;
/* 215:    */     }
/* 216:    */     int b3;
/* 217:272 */     if ((b3 = stream.read()) == -1) {
/* 218:273 */       return -1;
/* 219:    */     }
/* 220:275 */     return b0 & 0xFF | (b1 & 0xFF) << 8 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 24;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static byte[] decompress(byte[] compressed)
/* 224:    */     throws IOException
/* 225:    */   {
/* 226:279 */     return decompress(compressed, 0, compressed.length);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public static byte[] decompress(byte[] compressed, int offset, int length)
/* 230:    */     throws IOException
/* 231:    */   {
/* 232:283 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 233:284 */     InputStream instream = new ByteArrayInputStream(compressed, offset, length);
/* 234:285 */     InputStream stream = new RLEDecompressingInputStream(instream);
/* 235:286 */     IOUtils.copy(stream, out);
/* 236:287 */     stream.close();
/* 237:288 */     out.close();
/* 238:289 */     return out.toByteArray();
/* 239:    */   }
/* 240:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.RLEDecompressingInputStream
 * JD-Core Version:    0.7.0.1
 */