/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Reader;
/*   5:    */ import java.nio.ByteBuffer;
/*   6:    */ import java.nio.CharBuffer;
/*   7:    */ import java.nio.charset.Charset;
/*   8:    */ import java.nio.charset.CharsetDecoder;
/*   9:    */ 
/*  10:    */ @Internal
/*  11:    */ public class LittleEndianCP950Reader
/*  12:    */   extends Reader
/*  13:    */ {
/*  14: 32 */   private static final POILogger LOGGER = POILogFactory.getLogger(LittleEndianCP950Reader.class);
/*  15:    */   private static final char UNMAPPABLE = '?';
/*  16: 36 */   private final ByteBuffer doubleByteBuffer = ByteBuffer.allocate(2);
/*  17: 37 */   private final CharBuffer charBuffer = CharBuffer.allocate(2);
/*  18: 38 */   private final CharsetDecoder decoder = StringUtil.BIG5.newDecoder();
/*  19:    */   private static final char range1Low = '腀';
/*  20:    */   private static final char range1High = '跾';
/*  21:    */   private static final char range2Low = '蹀';
/*  22:    */   private static final char range2High = 'ꃾ';
/*  23:    */   private static final char range3Low = '욡';
/*  24:    */   private static final char range3High = '죾';
/*  25:    */   private static final char range4Low = '懲';
/*  26:    */   private static final char range4High = '﻾';
/*  27:    */   private final byte[] data;
/*  28:    */   private final int startOffset;
/*  29:    */   private final int length;
/*  30:    */   private int offset;
/*  31:    */   private int trailing;
/*  32:    */   private int leading;
/*  33: 57 */   int cnt = 0;
/*  34:    */   
/*  35:    */   public LittleEndianCP950Reader(byte[] data)
/*  36:    */   {
/*  37: 61 */     this(data, 0, data.length);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public LittleEndianCP950Reader(byte[] data, int offset, int length)
/*  41:    */   {
/*  42: 65 */     this.data = data;
/*  43: 66 */     this.startOffset = offset;
/*  44: 67 */     this.offset = this.startOffset;
/*  45: 68 */     this.length = length;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int read()
/*  49:    */   {
/*  50: 73 */     if ((this.offset + 1 > this.data.length) || (this.offset - this.startOffset > this.length)) {
/*  51: 74 */       return -1;
/*  52:    */     }
/*  53: 76 */     this.trailing = (this.data[(this.offset++)] & 0xFF);
/*  54: 77 */     this.leading = (this.data[(this.offset++)] & 0xFF);
/*  55: 78 */     this.decoder.reset();
/*  56: 79 */     if (this.leading < 129) {
/*  57: 82 */       return this.trailing;
/*  58:    */     }
/*  59: 83 */     if (this.leading == 249) {
/*  60: 84 */       return handleF9(this.trailing);
/*  61:    */     }
/*  62: 86 */     int ch = (this.leading << 8) + this.trailing;
/*  63: 87 */     if ((ch >= 33088) && (ch <= 36350)) {
/*  64: 88 */       return handleRange1(this.leading, this.trailing);
/*  65:    */     }
/*  66: 89 */     if ((ch >= 36416) && (ch <= 41214)) {
/*  67: 90 */       return handleRange2(this.leading, this.trailing);
/*  68:    */     }
/*  69: 91 */     if ((ch >= 50849) && (ch <= 51454)) {
/*  70: 92 */       return handleRange3(this.leading, this.trailing);
/*  71:    */     }
/*  72: 93 */     if ((ch >= 64064) && (ch <= 65278)) {
/*  73: 94 */       return handleRange4(this.leading, this.trailing);
/*  74:    */     }
/*  75: 97 */     this.charBuffer.clear();
/*  76: 98 */     this.doubleByteBuffer.clear();
/*  77: 99 */     this.doubleByteBuffer.put((byte)this.leading);
/*  78:100 */     this.doubleByteBuffer.put((byte)this.trailing);
/*  79:101 */     this.doubleByteBuffer.flip();
/*  80:102 */     this.decoder.decode(this.doubleByteBuffer, this.charBuffer, true);
/*  81:103 */     this.charBuffer.flip();
/*  82:105 */     if (this.charBuffer.length() == 0)
/*  83:    */     {
/*  84:106 */       LOGGER.log(5, new Object[] { "couldn't create char for: " + Integer.toString(this.leading & 0xFF, 16) + " " + Integer.toString(this.trailing & 0xFF, 16) });
/*  85:    */       
/*  86:    */ 
/*  87:109 */       return 63;
/*  88:    */     }
/*  89:111 */     return Character.codePointAt(this.charBuffer, 0);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public int read(char[] cbuf, int off, int len)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:123 */     for (int i = off; i < off + len; i++)
/*  96:    */     {
/*  97:124 */       int c = read();
/*  98:125 */       if (c == -1) {
/*  99:126 */         return i - off;
/* 100:    */       }
/* 101:128 */       cbuf[i] = ((char)c);
/* 102:    */     }
/* 103:130 */     return len;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void close() {}
/* 107:    */   
/* 108:    */   private int handleRange1(int leading, int trailing)
/* 109:    */   {
/* 110:138 */     return 61112 + 157 * (leading - 129) + (trailing < 128 ? trailing - 64 : trailing - 98);
/* 111:    */   }
/* 112:    */   
/* 113:    */   private int handleRange2(int leading, int trailing)
/* 114:    */   {
/* 115:143 */     return 58129 + 157 * (leading - 142) + (trailing < 128 ? trailing - 64 : trailing - 98);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private int handleRange3(int leading, int trailing)
/* 119:    */   {
/* 120:148 */     return 63090 + 157 * (leading - 198) + (trailing < 128 ? trailing - 64 : trailing - 98);
/* 121:    */   }
/* 122:    */   
/* 123:    */   private int handleRange4(int leading, int trailing)
/* 124:    */   {
/* 125:153 */     return 57344 + 157 * (leading - 250) + (trailing < 128 ? trailing - 64 : trailing - 98);
/* 126:    */   }
/* 127:    */   
/* 128:    */   private int handleF9(int trailing)
/* 129:    */   {
/* 130:158 */     switch (trailing)
/* 131:    */     {
/* 132:    */     case 64: 
/* 133:160 */       return 32408;
/* 134:    */     case 65: 
/* 135:162 */       return 32411;
/* 136:    */     case 66: 
/* 137:164 */       return 32409;
/* 138:    */     case 67: 
/* 139:166 */       return 33248;
/* 140:    */     case 68: 
/* 141:168 */       return 33249;
/* 142:    */     case 69: 
/* 143:170 */       return 34374;
/* 144:    */     case 70: 
/* 145:172 */       return 34375;
/* 146:    */     case 71: 
/* 147:174 */       return 34376;
/* 148:    */     case 72: 
/* 149:176 */       return 35193;
/* 150:    */     case 73: 
/* 151:178 */       return 35194;
/* 152:    */     case 74: 
/* 153:180 */       return 35196;
/* 154:    */     case 75: 
/* 155:182 */       return 35195;
/* 156:    */     case 76: 
/* 157:184 */       return 35327;
/* 158:    */     case 77: 
/* 159:186 */       return 35736;
/* 160:    */     case 78: 
/* 161:188 */       return 35737;
/* 162:    */     case 79: 
/* 163:190 */       return 36517;
/* 164:    */     case 80: 
/* 165:192 */       return 36516;
/* 166:    */     case 81: 
/* 167:194 */       return 36515;
/* 168:    */     case 82: 
/* 169:196 */       return 37998;
/* 170:    */     case 83: 
/* 171:198 */       return 37997;
/* 172:    */     case 84: 
/* 173:200 */       return 37999;
/* 174:    */     case 85: 
/* 175:202 */       return 38001;
/* 176:    */     case 86: 
/* 177:204 */       return 38003;
/* 178:    */     case 87: 
/* 179:206 */       return 38729;
/* 180:    */     case 88: 
/* 181:208 */       return 39026;
/* 182:    */     case 89: 
/* 183:210 */       return 39263;
/* 184:    */     case 90: 
/* 185:212 */       return 40040;
/* 186:    */     case 91: 
/* 187:214 */       return 40046;
/* 188:    */     case 92: 
/* 189:216 */       return 40045;
/* 190:    */     case 93: 
/* 191:218 */       return 40459;
/* 192:    */     case 94: 
/* 193:220 */       return 40461;
/* 194:    */     case 95: 
/* 195:222 */       return 40464;
/* 196:    */     case 96: 
/* 197:224 */       return 40463;
/* 198:    */     case 97: 
/* 199:226 */       return 40466;
/* 200:    */     case 98: 
/* 201:228 */       return 40465;
/* 202:    */     case 99: 
/* 203:230 */       return 40609;
/* 204:    */     case 100: 
/* 205:232 */       return 40693;
/* 206:    */     case 101: 
/* 207:234 */       return 40713;
/* 208:    */     case 102: 
/* 209:236 */       return 40775;
/* 210:    */     case 103: 
/* 211:238 */       return 40824;
/* 212:    */     case 104: 
/* 213:240 */       return 40827;
/* 214:    */     case 105: 
/* 215:242 */       return 40826;
/* 216:    */     case 106: 
/* 217:244 */       return 40825;
/* 218:    */     case 107: 
/* 219:246 */       return 22302;
/* 220:    */     case 108: 
/* 221:248 */       return 28774;
/* 222:    */     case 109: 
/* 223:250 */       return 31855;
/* 224:    */     case 110: 
/* 225:252 */       return 34876;
/* 226:    */     case 111: 
/* 227:254 */       return 36274;
/* 228:    */     case 112: 
/* 229:256 */       return 36518;
/* 230:    */     case 113: 
/* 231:258 */       return 37315;
/* 232:    */     case 114: 
/* 233:260 */       return 38004;
/* 234:    */     case 115: 
/* 235:262 */       return 38008;
/* 236:    */     case 116: 
/* 237:264 */       return 38006;
/* 238:    */     case 117: 
/* 239:266 */       return 38005;
/* 240:    */     case 118: 
/* 241:268 */       return 39520;
/* 242:    */     case 119: 
/* 243:270 */       return 40052;
/* 244:    */     case 120: 
/* 245:272 */       return 40051;
/* 246:    */     case 121: 
/* 247:274 */       return 40049;
/* 248:    */     case 122: 
/* 249:276 */       return 40053;
/* 250:    */     case 123: 
/* 251:278 */       return 40468;
/* 252:    */     case 124: 
/* 253:280 */       return 40467;
/* 254:    */     case 125: 
/* 255:282 */       return 40694;
/* 256:    */     case 126: 
/* 257:284 */       return 40714;
/* 258:    */     case 161: 
/* 259:286 */       return 40868;
/* 260:    */     case 162: 
/* 261:288 */       return 28776;
/* 262:    */     case 163: 
/* 263:290 */       return 28773;
/* 264:    */     case 164: 
/* 265:292 */       return 31991;
/* 266:    */     case 165: 
/* 267:294 */       return 34410;
/* 268:    */     case 166: 
/* 269:296 */       return 34878;
/* 270:    */     case 167: 
/* 271:298 */       return 34877;
/* 272:    */     case 168: 
/* 273:300 */       return 34879;
/* 274:    */     case 169: 
/* 275:302 */       return 35742;
/* 276:    */     case 170: 
/* 277:304 */       return 35996;
/* 278:    */     case 171: 
/* 279:306 */       return 36521;
/* 280:    */     case 172: 
/* 281:308 */       return 36553;
/* 282:    */     case 173: 
/* 283:310 */       return 38731;
/* 284:    */     case 174: 
/* 285:312 */       return 39027;
/* 286:    */     case 175: 
/* 287:314 */       return 39028;
/* 288:    */     case 176: 
/* 289:316 */       return 39116;
/* 290:    */     case 177: 
/* 291:318 */       return 39265;
/* 292:    */     case 178: 
/* 293:320 */       return 39339;
/* 294:    */     case 179: 
/* 295:322 */       return 39524;
/* 296:    */     case 180: 
/* 297:324 */       return 39526;
/* 298:    */     case 181: 
/* 299:326 */       return 39527;
/* 300:    */     case 182: 
/* 301:328 */       return 39716;
/* 302:    */     case 183: 
/* 303:330 */       return 40469;
/* 304:    */     case 184: 
/* 305:332 */       return 40471;
/* 306:    */     case 185: 
/* 307:334 */       return 40776;
/* 308:    */     case 186: 
/* 309:336 */       return 25095;
/* 310:    */     case 187: 
/* 311:338 */       return 27422;
/* 312:    */     case 188: 
/* 313:340 */       return 29223;
/* 314:    */     case 189: 
/* 315:342 */       return 34380;
/* 316:    */     case 190: 
/* 317:344 */       return 36520;
/* 318:    */     case 191: 
/* 319:346 */       return 38018;
/* 320:    */     case 192: 
/* 321:348 */       return 38016;
/* 322:    */     case 193: 
/* 323:350 */       return 38017;
/* 324:    */     case 194: 
/* 325:352 */       return 39529;
/* 326:    */     case 195: 
/* 327:354 */       return 39528;
/* 328:    */     case 196: 
/* 329:356 */       return 39726;
/* 330:    */     case 197: 
/* 331:358 */       return 40473;
/* 332:    */     case 198: 
/* 333:360 */       return 29225;
/* 334:    */     case 199: 
/* 335:362 */       return 34379;
/* 336:    */     case 200: 
/* 337:364 */       return 35743;
/* 338:    */     case 201: 
/* 339:366 */       return 38019;
/* 340:    */     case 202: 
/* 341:368 */       return 40057;
/* 342:    */     case 203: 
/* 343:370 */       return 40631;
/* 344:    */     case 204: 
/* 345:372 */       return 30325;
/* 346:    */     case 205: 
/* 347:374 */       return 39531;
/* 348:    */     case 206: 
/* 349:376 */       return 40058;
/* 350:    */     case 207: 
/* 351:378 */       return 40477;
/* 352:    */     case 208: 
/* 353:380 */       return 28777;
/* 354:    */     case 209: 
/* 355:382 */       return 28778;
/* 356:    */     case 210: 
/* 357:384 */       return 40612;
/* 358:    */     case 211: 
/* 359:386 */       return 40830;
/* 360:    */     case 212: 
/* 361:388 */       return 40777;
/* 362:    */     case 213: 
/* 363:390 */       return 40856;
/* 364:    */     case 214: 
/* 365:392 */       return 30849;
/* 366:    */     case 215: 
/* 367:394 */       return 37561;
/* 368:    */     case 216: 
/* 369:396 */       return 35023;
/* 370:    */     case 217: 
/* 371:398 */       return 22715;
/* 372:    */     case 218: 
/* 373:400 */       return 24658;
/* 374:    */     case 219: 
/* 375:402 */       return 31911;
/* 376:    */     case 220: 
/* 377:404 */       return 23290;
/* 378:    */     case 221: 
/* 379:406 */       return 9556;
/* 380:    */     case 222: 
/* 381:408 */       return 9574;
/* 382:    */     case 223: 
/* 383:410 */       return 9559;
/* 384:    */     case 224: 
/* 385:412 */       return 9568;
/* 386:    */     case 225: 
/* 387:414 */       return 9580;
/* 388:    */     case 226: 
/* 389:416 */       return 9571;
/* 390:    */     case 227: 
/* 391:418 */       return 9562;
/* 392:    */     case 228: 
/* 393:420 */       return 9577;
/* 394:    */     case 229: 
/* 395:422 */       return 9565;
/* 396:    */     case 230: 
/* 397:424 */       return 9554;
/* 398:    */     case 231: 
/* 399:426 */       return 9572;
/* 400:    */     case 232: 
/* 401:428 */       return 9557;
/* 402:    */     case 233: 
/* 403:430 */       return 9566;
/* 404:    */     case 234: 
/* 405:432 */       return 9578;
/* 406:    */     case 235: 
/* 407:434 */       return 9569;
/* 408:    */     case 236: 
/* 409:436 */       return 9560;
/* 410:    */     case 237: 
/* 411:438 */       return 9575;
/* 412:    */     case 238: 
/* 413:440 */       return 9563;
/* 414:    */     case 239: 
/* 415:442 */       return 9555;
/* 416:    */     case 240: 
/* 417:444 */       return 9573;
/* 418:    */     case 241: 
/* 419:446 */       return 9558;
/* 420:    */     case 242: 
/* 421:448 */       return 9567;
/* 422:    */     case 243: 
/* 423:450 */       return 9579;
/* 424:    */     case 244: 
/* 425:452 */       return 9570;
/* 426:    */     case 245: 
/* 427:454 */       return 9561;
/* 428:    */     case 246: 
/* 429:456 */       return 9576;
/* 430:    */     case 247: 
/* 431:458 */       return 9564;
/* 432:    */     case 248: 
/* 433:460 */       return 9553;
/* 434:    */     case 249: 
/* 435:462 */       return 9552;
/* 436:    */     case 250: 
/* 437:464 */       return 9581;
/* 438:    */     case 251: 
/* 439:466 */       return 9582;
/* 440:    */     case 252: 
/* 441:468 */       return 9584;
/* 442:    */     case 253: 
/* 443:470 */       return 9583;
/* 444:    */     case 254: 
/* 445:472 */       return 9619;
/* 446:    */     }
/* 447:474 */     LOGGER.log(5, new Object[] { "couldn't create char for: f9 " + Integer.toString(trailing & 0xFF, 16) });
/* 448:    */     
/* 449:476 */     return 63;
/* 450:    */   }
/* 451:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.LittleEndianCP950Reader
 * JD-Core Version:    0.7.0.1
 */