/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.zip.InflaterInputStream;
/*   8:    */ import org.apache.poi.util.HexDump;
/*   9:    */ import org.apache.poi.util.HexRead;
/*  10:    */ import org.apache.poi.util.LittleEndian;
/*  11:    */ import org.apache.poi.util.LittleEndian.BufferUnderrunException;
/*  12:    */ 
/*  13:    */ public final class EscherDump
/*  14:    */ {
/*  15:    */   public void dump(byte[] data, int offset, int size, PrintStream out)
/*  16:    */   {
/*  17: 50 */     EscherRecordFactory recordFactory = new DefaultEscherRecordFactory();
/*  18: 51 */     int pos = offset;
/*  19: 52 */     while (pos < offset + size)
/*  20:    */     {
/*  21: 54 */       EscherRecord r = recordFactory.createRecord(data, pos);
/*  22: 55 */       int bytesRead = r.fillFields(data, pos, recordFactory);
/*  23: 56 */       out.println(r);
/*  24: 57 */       pos += bytesRead;
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void dumpOld(long maxLength, InputStream in, PrintStream out)
/*  29:    */     throws IOException, LittleEndian.BufferUnderrunException
/*  30:    */   {
/*  31: 73 */     long remainingBytes = maxLength;
/*  32:    */     
/*  33:    */ 
/*  34:    */ 
/*  35:    */ 
/*  36:    */ 
/*  37:    */ 
/*  38: 80 */     boolean atEOF = false;
/*  39: 82 */     while ((!atEOF) && (remainingBytes > 0L))
/*  40:    */     {
/*  41: 83 */       short options = LittleEndian.readShort(in);
/*  42: 84 */       short recordId = LittleEndian.readShort(in);
/*  43: 85 */       int recordBytesRemaining = LittleEndian.readInt(in);
/*  44:    */       
/*  45: 87 */       remainingBytes -= 8L;
/*  46:    */       String recordName;
/*  47:    */       String recordName;
/*  48: 89 */       switch (recordId)
/*  49:    */       {
/*  50:    */       case -4096: 
/*  51: 92 */         recordName = "MsofbtDggContainer";
/*  52: 93 */         break;
/*  53:    */       case -4090: 
/*  54: 95 */         recordName = "MsofbtDgg";
/*  55: 96 */         break;
/*  56:    */       case -4074: 
/*  57: 98 */         recordName = "MsofbtCLSID";
/*  58: 99 */         break;
/*  59:    */       case -4085: 
/*  60:101 */         recordName = "MsofbtOPT";
/*  61:102 */         break;
/*  62:    */       case -3814: 
/*  63:104 */         recordName = "MsofbtColorMRU";
/*  64:105 */         break;
/*  65:    */       case -3810: 
/*  66:107 */         recordName = "MsofbtSplitMenuColors";
/*  67:108 */         break;
/*  68:    */       case -4095: 
/*  69:110 */         recordName = "MsofbtBstoreContainer";
/*  70:111 */         break;
/*  71:    */       case -4089: 
/*  72:113 */         recordName = "MsofbtBSE";
/*  73:114 */         break;
/*  74:    */       case -4094: 
/*  75:116 */         recordName = "MsofbtDgContainer";
/*  76:117 */         break;
/*  77:    */       case -4088: 
/*  78:119 */         recordName = "MsofbtDg";
/*  79:120 */         break;
/*  80:    */       case -3816: 
/*  81:122 */         recordName = "MsofbtRegroupItem";
/*  82:123 */         break;
/*  83:    */       case -3808: 
/*  84:125 */         recordName = "MsofbtColorScheme";
/*  85:126 */         break;
/*  86:    */       case -4093: 
/*  87:128 */         recordName = "MsofbtSpgrContainer";
/*  88:129 */         break;
/*  89:    */       case -4092: 
/*  90:131 */         recordName = "MsofbtSpContainer";
/*  91:132 */         break;
/*  92:    */       case -4087: 
/*  93:134 */         recordName = "MsofbtSpgr";
/*  94:135 */         break;
/*  95:    */       case -4086: 
/*  96:137 */         recordName = "MsofbtSp";
/*  97:138 */         break;
/*  98:    */       case -4084: 
/*  99:140 */         recordName = "MsofbtTextbox";
/* 100:141 */         break;
/* 101:    */       case -4083: 
/* 102:143 */         recordName = "MsofbtClientTextbox";
/* 103:144 */         break;
/* 104:    */       case -4082: 
/* 105:146 */         recordName = "MsofbtAnchor";
/* 106:147 */         break;
/* 107:    */       case -4081: 
/* 108:149 */         recordName = "MsofbtChildAnchor";
/* 109:150 */         break;
/* 110:    */       case -4080: 
/* 111:152 */         recordName = "MsofbtClientAnchor";
/* 112:153 */         break;
/* 113:    */       case -4079: 
/* 114:155 */         recordName = "MsofbtClientData";
/* 115:156 */         break;
/* 116:    */       case -3809: 
/* 117:158 */         recordName = "MsofbtOleObject";
/* 118:159 */         break;
/* 119:    */       case -3811: 
/* 120:161 */         recordName = "MsofbtDeletedPspl";
/* 121:162 */         break;
/* 122:    */       case -4091: 
/* 123:164 */         recordName = "MsofbtSolverContainer";
/* 124:165 */         break;
/* 125:    */       case -4078: 
/* 126:167 */         recordName = "MsofbtConnectorRule";
/* 127:168 */         break;
/* 128:    */       case -4077: 
/* 129:170 */         recordName = "MsofbtAlignRule";
/* 130:171 */         break;
/* 131:    */       case -4076: 
/* 132:173 */         recordName = "MsofbtArcRule";
/* 133:174 */         break;
/* 134:    */       case -4075: 
/* 135:176 */         recordName = "MsofbtClientRule";
/* 136:177 */         break;
/* 137:    */       case -4073: 
/* 138:179 */         recordName = "MsofbtCalloutRule";
/* 139:180 */         break;
/* 140:    */       case -3815: 
/* 141:182 */         recordName = "MsofbtSelection";
/* 142:183 */         break;
/* 143:    */       case -3806: 
/* 144:185 */         recordName = "MsofbtUDefProp";
/* 145:186 */         break;
/* 146:    */       default: 
/* 147:188 */         if ((recordId >= -4072) && (recordId <= -3817))
/* 148:    */         {
/* 149:189 */           recordName = "MsofbtBLIP";
/* 150:    */         }
/* 151:    */         else
/* 152:    */         {
/* 153:    */           String recordName;
/* 154:190 */           if ((options & 0xF) == 15) {
/* 155:191 */             recordName = "UNKNOWN container";
/* 156:    */           } else {
/* 157:193 */             recordName = "UNKNOWN ID";
/* 158:    */           }
/* 159:    */         }
/* 160:    */         break;
/* 161:    */       }
/* 162:197 */       StringBuilder stringBuf = new StringBuilder();
/* 163:198 */       stringBuf.append("  ");
/* 164:199 */       stringBuf.append(HexDump.toHex(recordId));
/* 165:200 */       stringBuf.append("  ").append(recordName).append(" [");
/* 166:201 */       stringBuf.append(HexDump.toHex(options));
/* 167:202 */       stringBuf.append(',');
/* 168:203 */       stringBuf.append(HexDump.toHex(recordBytesRemaining));
/* 169:204 */       stringBuf.append("]  instance: ");
/* 170:205 */       stringBuf.append(HexDump.toHex((short)(options >> 4)));
/* 171:206 */       out.println(stringBuf);
/* 172:207 */       stringBuf.setLength(0);
/* 173:210 */       if ((recordId == -4089) && (36L <= remainingBytes) && (36 <= recordBytesRemaining))
/* 174:    */       {
/* 175:218 */         stringBuf = stringBuf.append("    btWin32: ");
/* 176:219 */         byte n8 = (byte)in.read();
/* 177:220 */         stringBuf.append(HexDump.toHex(n8));
/* 178:221 */         stringBuf.append(getBlipType(n8));
/* 179:222 */         stringBuf.append("  btMacOS: ");
/* 180:223 */         n8 = (byte)in.read();
/* 181:224 */         stringBuf.append(HexDump.toHex(n8));
/* 182:225 */         stringBuf.append(getBlipType(n8));
/* 183:226 */         out.println(stringBuf);
/* 184:    */         
/* 185:228 */         out.println("    rgbUid:");
/* 186:229 */         HexDump.dump(in, out, 0, 16);
/* 187:    */         
/* 188:231 */         out.print("    tag: ");
/* 189:232 */         outHex(2, in, out);
/* 190:233 */         out.println();
/* 191:234 */         out.print("    size: ");
/* 192:235 */         outHex(4, in, out);
/* 193:236 */         out.println();
/* 194:237 */         out.print("    cRef: ");
/* 195:238 */         outHex(4, in, out);
/* 196:239 */         out.println();
/* 197:240 */         out.print("    offs: ");
/* 198:241 */         outHex(4, in, out);
/* 199:242 */         out.println();
/* 200:243 */         out.print("    usage: ");
/* 201:244 */         outHex(1, in, out);
/* 202:245 */         out.println();
/* 203:246 */         out.print("    cbName: ");
/* 204:247 */         outHex(1, in, out);
/* 205:248 */         out.println();
/* 206:249 */         out.print("    unused2: ");
/* 207:250 */         outHex(1, in, out);
/* 208:251 */         out.println();
/* 209:252 */         out.print("    unused3: ");
/* 210:253 */         outHex(1, in, out);
/* 211:254 */         out.println();
/* 212:    */         
/* 213:    */ 
/* 214:257 */         remainingBytes -= 36L;
/* 215:    */         
/* 216:259 */         recordBytesRemaining = 0;
/* 217:    */       }
/* 218:261 */       else if ((recordId == -4080) && (18L <= remainingBytes) && (18 <= recordBytesRemaining))
/* 219:    */       {
/* 220:266 */         out.print("    Flag: ");
/* 221:267 */         outHex(2, in, out);
/* 222:268 */         out.println();
/* 223:269 */         out.print("    Col1: ");
/* 224:270 */         outHex(2, in, out);
/* 225:271 */         out.print("    dX1: ");
/* 226:272 */         outHex(2, in, out);
/* 227:273 */         out.print("    Row1: ");
/* 228:274 */         outHex(2, in, out);
/* 229:275 */         out.print("    dY1: ");
/* 230:276 */         outHex(2, in, out);
/* 231:277 */         out.println();
/* 232:278 */         out.print("    Col2: ");
/* 233:279 */         outHex(2, in, out);
/* 234:280 */         out.print("    dX2: ");
/* 235:281 */         outHex(2, in, out);
/* 236:282 */         out.print("    Row2: ");
/* 237:283 */         outHex(2, in, out);
/* 238:284 */         out.print("    dY2: ");
/* 239:285 */         outHex(2, in, out);
/* 240:286 */         out.println();
/* 241:    */         
/* 242:288 */         remainingBytes -= 18L;
/* 243:289 */         recordBytesRemaining -= 18;
/* 244:    */       }
/* 245:292 */       else if ((recordId == -4085) || (recordId == -3806))
/* 246:    */       {
/* 247:294 */         int nComplex = 0;
/* 248:295 */         out.println("    PROPID        VALUE");
/* 249:296 */         while ((recordBytesRemaining >= 6 + nComplex) && (remainingBytes >= 6 + nComplex))
/* 250:    */         {
/* 251:300 */           short n16 = LittleEndian.readShort(in);
/* 252:301 */           int n32 = LittleEndian.readInt(in);
/* 253:    */           
/* 254:303 */           recordBytesRemaining -= 6;
/* 255:304 */           remainingBytes -= 6L;
/* 256:305 */           out.print("    ");
/* 257:306 */           out.print(HexDump.toHex(n16));
/* 258:307 */           out.print(" (");
/* 259:308 */           int propertyId = n16 & 0x3FFF;
/* 260:309 */           out.print(" " + propertyId);
/* 261:310 */           if ((n16 & 0xFFFF8000) == 0)
/* 262:    */           {
/* 263:312 */             if ((n16 & 0x4000) != 0) {
/* 264:313 */               out.print(", fBlipID");
/* 265:    */             }
/* 266:315 */             out.print(")  ");
/* 267:    */             
/* 268:317 */             out.print(HexDump.toHex(n32));
/* 269:319 */             if ((n16 & 0x4000) == 0)
/* 270:    */             {
/* 271:321 */               out.print(" (");
/* 272:322 */               out.print(dec1616(n32));
/* 273:323 */               out.print(')');
/* 274:324 */               out.print(" {" + propName((short)propertyId) + "}");
/* 275:    */             }
/* 276:326 */             out.println();
/* 277:    */           }
/* 278:    */           else
/* 279:    */           {
/* 280:330 */             out.print(", fComplex)  ");
/* 281:331 */             out.print(HexDump.toHex(n32));
/* 282:332 */             out.print(" - Complex prop len");
/* 283:333 */             out.println(" {" + propName((short)propertyId) + "}");
/* 284:    */             
/* 285:335 */             nComplex += n32;
/* 286:    */           }
/* 287:    */         }
/* 288:340 */         while ((nComplex & remainingBytes) > 0L)
/* 289:    */         {
/* 290:342 */           short nDumpSize = nComplex > (int)remainingBytes ? (short)(int)remainingBytes : (short)nComplex;
/* 291:343 */           HexDump.dump(in, out, 0, nDumpSize);
/* 292:344 */           nComplex -= nDumpSize;
/* 293:345 */           recordBytesRemaining -= nDumpSize;
/* 294:346 */           remainingBytes -= nDumpSize;
/* 295:    */         }
/* 296:    */       }
/* 297:349 */       else if (recordId == -4078)
/* 298:    */       {
/* 299:351 */         out.print("    Connector rule: ");
/* 300:352 */         out.print(LittleEndian.readInt(in));
/* 301:353 */         out.print("    ShapeID A: ");
/* 302:354 */         out.print(LittleEndian.readInt(in));
/* 303:355 */         out.print("   ShapeID B: ");
/* 304:356 */         out.print(LittleEndian.readInt(in));
/* 305:357 */         out.print("    ShapeID connector: ");
/* 306:358 */         out.print(LittleEndian.readInt(in));
/* 307:359 */         out.print("   Connect pt A: ");
/* 308:360 */         out.print(LittleEndian.readInt(in));
/* 309:361 */         out.print("   Connect pt B: ");
/* 310:362 */         out.println(LittleEndian.readInt(in));
/* 311:    */         
/* 312:364 */         recordBytesRemaining -= 24;
/* 313:365 */         remainingBytes -= 24L;
/* 314:    */       }
/* 315:367 */       else if ((recordId >= -4072) && (recordId < -3817))
/* 316:    */       {
/* 317:369 */         out.println("    Secondary UID: ");
/* 318:370 */         HexDump.dump(in, out, 0, 16);
/* 319:371 */         out.println("    Cache of size: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 320:372 */         out.println("    Boundary top: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 321:373 */         out.println("    Boundary left: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 322:374 */         out.println("    Boundary width: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 323:375 */         out.println("    Boundary height: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 324:376 */         out.println("    X: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 325:377 */         out.println("    Y: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 326:378 */         out.println("    Cache of saved size: " + HexDump.toHex(LittleEndian.readInt(in)));
/* 327:379 */         out.println("    Compression Flag: " + HexDump.toHex((byte)in.read()));
/* 328:380 */         out.println("    Filter: " + HexDump.toHex((byte)in.read()));
/* 329:381 */         out.println("    Data (after decompression): ");
/* 330:    */         
/* 331:383 */         recordBytesRemaining -= 50;
/* 332:384 */         remainingBytes -= 50L;
/* 333:    */         
/* 334:386 */         short nDumpSize = recordBytesRemaining > (int)remainingBytes ? (short)(int)remainingBytes : (short)recordBytesRemaining;
/* 335:    */         
/* 336:    */ 
/* 337:389 */         byte[] buf = new byte[nDumpSize];
/* 338:390 */         int read = in.read(buf);
/* 339:391 */         while ((read != -1) && (read < nDumpSize)) {
/* 340:392 */           read += in.read(buf, read, buf.length);
/* 341:    */         }
/* 342:394 */         ByteArrayInputStream bin = new ByteArrayInputStream(buf);
/* 343:    */         
/* 344:396 */         InputStream in1 = new InflaterInputStream(bin);
/* 345:397 */         int bytesToDump = -1;
/* 346:398 */         HexDump.dump(in1, out, 0, bytesToDump);
/* 347:    */         
/* 348:400 */         recordBytesRemaining -= nDumpSize;
/* 349:401 */         remainingBytes -= nDumpSize;
/* 350:    */       }
/* 351:405 */       boolean isContainer = (options & 0xF) == 15;
/* 352:406 */       if ((isContainer) && (remainingBytes >= 0L))
/* 353:    */       {
/* 354:408 */         if (recordBytesRemaining <= (int)remainingBytes) {
/* 355:409 */           out.println("            completed within");
/* 356:    */         } else {
/* 357:411 */           out.println("            continued elsewhere");
/* 358:    */         }
/* 359:    */       }
/* 360:414 */       else if (remainingBytes >= 0L)
/* 361:    */       {
/* 362:417 */         short nDumpSize = recordBytesRemaining > (int)remainingBytes ? (short)(int)remainingBytes : (short)recordBytesRemaining;
/* 363:419 */         if (nDumpSize != 0)
/* 364:    */         {
/* 365:421 */           HexDump.dump(in, out, 0, nDumpSize);
/* 366:422 */           remainingBytes -= nDumpSize;
/* 367:    */         }
/* 368:    */       }
/* 369:    */       else
/* 370:    */       {
/* 371:425 */         out.println(" >> OVERRUN <<");
/* 372:    */       }
/* 373:    */     }
/* 374:    */   }
/* 375:    */   
/* 376:    */   private String propName(short propertyId)
/* 377:    */   {
/* 378:448 */     Object props = { new Object()new Object
/* 379:    */     {
/* 380:    */       final int _id;
/* 381:    */       final String _name;
/* 382:448 */     }, new Object()new Object
/* 383:    */     {
/* 384:    */       final int _id;
/* 385:    */       final String _name;
/* 386:448 */     }, new Object()new Object
/* 387:    */     {
/* 388:    */       final int _id;
/* 389:    */       final String _name;
/* 390:448 */     }, new Object()new Object
/* 391:    */     {
/* 392:    */       final int _id;
/* 393:    */       final String _name;
/* 394:448 */     }, new Object()new Object
/* 395:    */     {
/* 396:    */       final int _id;
/* 397:    */       final String _name;
/* 398:448 */     }, new Object()new Object
/* 399:    */     {
/* 400:    */       final int _id;
/* 401:    */       final String _name;
/* 402:448 */     }, new Object()new Object
/* 403:    */     {
/* 404:    */       final int _id;
/* 405:    */       final String _name;
/* 406:448 */     }, new Object()new Object
/* 407:    */     {
/* 408:    */       final int _id;
/* 409:    */       final String _name;
/* 410:448 */     }, new Object()new Object
/* 411:    */     {
/* 412:    */       final int _id;
/* 413:    */       final String _name;
/* 414:448 */     }, new Object()new Object
/* 415:    */     {
/* 416:    */       final int _id;
/* 417:    */       final String _name;
/* 418:448 */     }, new Object()new Object
/* 419:    */     {
/* 420:    */       final int _id;
/* 421:    */       final String _name;
/* 422:448 */     }, new Object()new Object
/* 423:    */     {
/* 424:    */       final int _id;
/* 425:    */       final String _name;
/* 426:448 */     }, new Object()new Object
/* 427:    */     {
/* 428:    */       final int _id;
/* 429:    */       final String _name;
/* 430:448 */     }, new Object()new Object
/* 431:    */     {
/* 432:    */       final int _id;
/* 433:    */       final String _name;
/* 434:448 */     }, new Object()new Object
/* 435:    */     {
/* 436:    */       final int _id;
/* 437:    */       final String _name;
/* 438:448 */     }, new Object()new Object
/* 439:    */     {
/* 440:    */       final int _id;
/* 441:    */       final String _name;
/* 442:448 */     }, new Object()new Object
/* 443:    */     {
/* 444:    */       final int _id;
/* 445:    */       final String _name;
/* 446:448 */     }, new Object()new Object
/* 447:    */     {
/* 448:    */       final int _id;
/* 449:    */       final String _name;
/* 450:448 */     }, new Object()new Object
/* 451:    */     {
/* 452:    */       final int _id;
/* 453:    */       final String _name;
/* 454:448 */     }, new Object()new Object
/* 455:    */     {
/* 456:    */       final int _id;
/* 457:    */       final String _name;
/* 458:448 */     }, new Object()new Object
/* 459:    */     {
/* 460:    */       final int _id;
/* 461:    */       final String _name;
/* 462:448 */     }, new Object()new Object
/* 463:    */     {
/* 464:    */       final int _id;
/* 465:    */       final String _name;
/* 466:448 */     }, new Object()new Object
/* 467:    */     {
/* 468:    */       final int _id;
/* 469:    */       final String _name;
/* 470:448 */     }, new Object()new Object
/* 471:    */     {
/* 472:    */       final int _id;
/* 473:    */       final String _name;
/* 474:448 */     }, new Object()new Object
/* 475:    */     {
/* 476:    */       final int _id;
/* 477:    */       final String _name;
/* 478:448 */     }, new Object()new Object
/* 479:    */     {
/* 480:    */       final int _id;
/* 481:    */       final String _name;
/* 482:448 */     }, new Object()new Object
/* 483:    */     {
/* 484:    */       final int _id;
/* 485:    */       final String _name;
/* 486:448 */     }, new Object()new Object
/* 487:    */     {
/* 488:    */       final int _id;
/* 489:    */       final String _name;
/* 490:448 */     }, new Object()new Object
/* 491:    */     {
/* 492:    */       final int _id;
/* 493:    */       final String _name;
/* 494:448 */     }, new Object()new Object
/* 495:    */     {
/* 496:    */       final int _id;
/* 497:    */       final String _name;
/* 498:448 */     }, new Object()new Object
/* 499:    */     {
/* 500:    */       final int _id;
/* 501:    */       final String _name;
/* 502:448 */     }, new Object()new Object
/* 503:    */     {
/* 504:    */       final int _id;
/* 505:    */       final String _name;
/* 506:448 */     }, new Object()new Object
/* 507:    */     {
/* 508:    */       final int _id;
/* 509:    */       final String _name;
/* 510:448 */     }, new Object()new Object
/* 511:    */     {
/* 512:    */       final int _id;
/* 513:    */       final String _name;
/* 514:448 */     }, new Object()new Object
/* 515:    */     {
/* 516:    */       final int _id;
/* 517:    */       final String _name;
/* 518:448 */     }, new Object()new Object
/* 519:    */     {
/* 520:    */       final int _id;
/* 521:    */       final String _name;
/* 522:448 */     }, new Object()new Object
/* 523:    */     {
/* 524:    */       final int _id;
/* 525:    */       final String _name;
/* 526:448 */     }, new Object()new Object
/* 527:    */     {
/* 528:    */       final int _id;
/* 529:    */       final String _name;
/* 530:448 */     }, new Object()new Object
/* 531:    */     {
/* 532:    */       final int _id;
/* 533:    */       final String _name;
/* 534:448 */     }, new Object()new Object
/* 535:    */     {
/* 536:    */       final int _id;
/* 537:    */       final String _name;
/* 538:448 */     }, new Object()new Object
/* 539:    */     {
/* 540:    */       final int _id;
/* 541:    */       final String _name;
/* 542:448 */     }, new Object()new Object
/* 543:    */     {
/* 544:    */       final int _id;
/* 545:    */       final String _name;
/* 546:448 */     }, new Object()new Object
/* 547:    */     {
/* 548:    */       final int _id;
/* 549:    */       final String _name;
/* 550:448 */     }, new Object()new Object
/* 551:    */     {
/* 552:    */       final int _id;
/* 553:    */       final String _name;
/* 554:448 */     }, new Object()new Object
/* 555:    */     {
/* 556:    */       final int _id;
/* 557:    */       final String _name;
/* 558:448 */     }, new Object()new Object
/* 559:    */     {
/* 560:    */       final int _id;
/* 561:    */       final String _name;
/* 562:448 */     }, new Object()new Object
/* 563:    */     {
/* 564:    */       final int _id;
/* 565:    */       final String _name;
/* 566:448 */     }, new Object()new Object
/* 567:    */     {
/* 568:    */       final int _id;
/* 569:    */       final String _name;
/* 570:448 */     }, new Object()new Object
/* 571:    */     {
/* 572:    */       final int _id;
/* 573:    */       final String _name;
/* 574:448 */     }, new Object()new Object
/* 575:    */     {
/* 576:    */       final int _id;
/* 577:    */       final String _name;
/* 578:448 */     }, new Object()new Object
/* 579:    */     {
/* 580:    */       final int _id;
/* 581:    */       final String _name;
/* 582:448 */     }, new Object()new Object
/* 583:    */     {
/* 584:    */       final int _id;
/* 585:    */       final String _name;
/* 586:448 */     }, new Object()new Object
/* 587:    */     {
/* 588:    */       final int _id;
/* 589:    */       final String _name;
/* 590:448 */     }, new Object()new Object
/* 591:    */     {
/* 592:    */       final int _id;
/* 593:    */       final String _name;
/* 594:448 */     }, new Object()new Object
/* 595:    */     {
/* 596:    */       final int _id;
/* 597:    */       final String _name;
/* 598:448 */     }, new Object()new Object
/* 599:    */     {
/* 600:    */       final int _id;
/* 601:    */       final String _name;
/* 602:448 */     }, new Object()new Object
/* 603:    */     {
/* 604:    */       final int _id;
/* 605:    */       final String _name;
/* 606:448 */     }, new Object()new Object
/* 607:    */     {
/* 608:    */       final int _id;
/* 609:    */       final String _name;
/* 610:448 */     }, new Object()new Object
/* 611:    */     {
/* 612:    */       final int _id;
/* 613:    */       final String _name;
/* 614:448 */     }, new Object()new Object
/* 615:    */     {
/* 616:    */       final int _id;
/* 617:    */       final String _name;
/* 618:448 */     }, new Object()new Object
/* 619:    */     {
/* 620:    */       final int _id;
/* 621:    */       final String _name;
/* 622:448 */     }, new Object()new Object
/* 623:    */     {
/* 624:    */       final int _id;
/* 625:    */       final String _name;
/* 626:448 */     }, new Object()new Object
/* 627:    */     {
/* 628:    */       final int _id;
/* 629:    */       final String _name;
/* 630:448 */     }, new Object()new Object
/* 631:    */     {
/* 632:    */       final int _id;
/* 633:    */       final String _name;
/* 634:448 */     }, new Object()new Object
/* 635:    */     {
/* 636:    */       final int _id;
/* 637:    */       final String _name;
/* 638:448 */     }, new Object()new Object
/* 639:    */     {
/* 640:    */       final int _id;
/* 641:    */       final String _name;
/* 642:448 */     }, new Object()new Object
/* 643:    */     {
/* 644:    */       final int _id;
/* 645:    */       final String _name;
/* 646:448 */     }, new Object()new Object
/* 647:    */     {
/* 648:    */       final int _id;
/* 649:    */       final String _name;
/* 650:448 */     }, new Object()new Object
/* 651:    */     {
/* 652:    */       final int _id;
/* 653:    */       final String _name;
/* 654:448 */     }, new Object()new Object
/* 655:    */     {
/* 656:    */       final int _id;
/* 657:    */       final String _name;
/* 658:448 */     }, new Object()new Object
/* 659:    */     {
/* 660:    */       final int _id;
/* 661:    */       final String _name;
/* 662:448 */     }, new Object()new Object
/* 663:    */     {
/* 664:    */       final int _id;
/* 665:    */       final String _name;
/* 666:448 */     }, new Object()new Object
/* 667:    */     {
/* 668:    */       final int _id;
/* 669:    */       final String _name;
/* 670:448 */     }, new Object()new Object
/* 671:    */     {
/* 672:    */       final int _id;
/* 673:    */       final String _name;
/* 674:448 */     }, new Object()new Object
/* 675:    */     {
/* 676:    */       final int _id;
/* 677:    */       final String _name;
/* 678:448 */     }, new Object()new Object
/* 679:    */     {
/* 680:    */       final int _id;
/* 681:    */       final String _name;
/* 682:448 */     }, new Object()new Object
/* 683:    */     {
/* 684:    */       final int _id;
/* 685:    */       final String _name;
/* 686:448 */     }, new Object()new Object
/* 687:    */     {
/* 688:    */       final int _id;
/* 689:    */       final String _name;
/* 690:448 */     }, new Object()new Object
/* 691:    */     {
/* 692:    */       final int _id;
/* 693:    */       final String _name;
/* 694:448 */     }, new Object()new Object
/* 695:    */     {
/* 696:    */       final int _id;
/* 697:    */       final String _name;
/* 698:448 */     }, new Object()new Object
/* 699:    */     {
/* 700:    */       final int _id;
/* 701:    */       final String _name;
/* 702:448 */     }, new Object()new Object
/* 703:    */     {
/* 704:    */       final int _id;
/* 705:    */       final String _name;
/* 706:448 */     }, new Object()new Object
/* 707:    */     {
/* 708:    */       final int _id;
/* 709:    */       final String _name;
/* 710:448 */     }, new Object()new Object
/* 711:    */     {
/* 712:    */       final int _id;
/* 713:    */       final String _name;
/* 714:448 */     }, new Object()new Object
/* 715:    */     {
/* 716:    */       final int _id;
/* 717:    */       final String _name;
/* 718:448 */     }, new Object()new Object
/* 719:    */     {
/* 720:    */       final int _id;
/* 721:    */       final String _name;
/* 722:448 */     }, new Object()new Object
/* 723:    */     {
/* 724:    */       final int _id;
/* 725:    */       final String _name;
/* 726:448 */     }, new Object()new Object
/* 727:    */     {
/* 728:    */       final int _id;
/* 729:    */       final String _name;
/* 730:448 */     }, new Object()new Object
/* 731:    */     {
/* 732:    */       final int _id;
/* 733:    */       final String _name;
/* 734:448 */     }, new Object()new Object
/* 735:    */     {
/* 736:    */       final int _id;
/* 737:    */       final String _name;
/* 738:448 */     }, new Object()new Object
/* 739:    */     {
/* 740:    */       final int _id;
/* 741:    */       final String _name;
/* 742:448 */     }, new Object()new Object
/* 743:    */     {
/* 744:    */       final int _id;
/* 745:    */       final String _name;
/* 746:448 */     }, new Object()new Object
/* 747:    */     {
/* 748:    */       final int _id;
/* 749:    */       final String _name;
/* 750:448 */     }, new Object()new Object
/* 751:    */     {
/* 752:    */       final int _id;
/* 753:    */       final String _name;
/* 754:448 */     }, new Object()new Object
/* 755:    */     {
/* 756:    */       final int _id;
/* 757:    */       final String _name;
/* 758:448 */     }, new Object()new Object
/* 759:    */     {
/* 760:    */       final int _id;
/* 761:    */       final String _name;
/* 762:448 */     }, new Object()new Object
/* 763:    */     {
/* 764:    */       final int _id;
/* 765:    */       final String _name;
/* 766:448 */     }, new Object()new Object
/* 767:    */     {
/* 768:    */       final int _id;
/* 769:    */       final String _name;
/* 770:448 */     }, new Object()new Object
/* 771:    */     {
/* 772:    */       final int _id;
/* 773:    */       final String _name;
/* 774:448 */     }, new Object()new Object
/* 775:    */     {
/* 776:    */       final int _id;
/* 777:    */       final String _name;
/* 778:448 */     }, new Object()new Object
/* 779:    */     {
/* 780:    */       final int _id;
/* 781:    */       final String _name;
/* 782:448 */     }, new Object()new Object
/* 783:    */     {
/* 784:    */       final int _id;
/* 785:    */       final String _name;
/* 786:448 */     }, new Object()new Object
/* 787:    */     {
/* 788:    */       final int _id;
/* 789:    */       final String _name;
/* 790:448 */     }, new Object()new Object
/* 791:    */     {
/* 792:    */       final int _id;
/* 793:    */       final String _name;
/* 794:448 */     }, new Object()new Object
/* 795:    */     {
/* 796:    */       final int _id;
/* 797:    */       final String _name;
/* 798:448 */     }, new Object()new Object
/* 799:    */     {
/* 800:    */       final int _id;
/* 801:    */       final String _name;
/* 802:448 */     }, new Object()new Object
/* 803:    */     {
/* 804:    */       final int _id;
/* 805:    */       final String _name;
/* 806:448 */     }, new Object()new Object
/* 807:    */     {
/* 808:    */       final int _id;
/* 809:    */       final String _name;
/* 810:448 */     }, new Object()new Object
/* 811:    */     {
/* 812:    */       final int _id;
/* 813:    */       final String _name;
/* 814:448 */     }, new Object()new Object
/* 815:    */     {
/* 816:    */       final int _id;
/* 817:    */       final String _name;
/* 818:448 */     }, new Object()new Object
/* 819:    */     {
/* 820:    */       final int _id;
/* 821:    */       final String _name;
/* 822:448 */     }, new Object()new Object
/* 823:    */     {
/* 824:    */       final int _id;
/* 825:    */       final String _name;
/* 826:448 */     }, new Object()new Object
/* 827:    */     {
/* 828:    */       final int _id;
/* 829:    */       final String _name;
/* 830:448 */     }, new Object()new Object
/* 831:    */     {
/* 832:    */       final int _id;
/* 833:    */       final String _name;
/* 834:448 */     }, new Object()new Object
/* 835:    */     {
/* 836:    */       final int _id;
/* 837:    */       final String _name;
/* 838:448 */     }, new Object()new Object
/* 839:    */     {
/* 840:    */       final int _id;
/* 841:    */       final String _name;
/* 842:448 */     }, new Object()new Object
/* 843:    */     {
/* 844:    */       final int _id;
/* 845:    */       final String _name;
/* 846:448 */     }, new Object()new Object
/* 847:    */     {
/* 848:    */       final int _id;
/* 849:    */       final String _name;
/* 850:448 */     }, new Object()new Object
/* 851:    */     {
/* 852:    */       final int _id;
/* 853:    */       final String _name;
/* 854:448 */     }, new Object()new Object
/* 855:    */     {
/* 856:    */       final int _id;
/* 857:    */       final String _name;
/* 858:448 */     }, new Object()new Object
/* 859:    */     {
/* 860:    */       final int _id;
/* 861:    */       final String _name;
/* 862:448 */     }, new Object()new Object
/* 863:    */     {
/* 864:    */       final int _id;
/* 865:    */       final String _name;
/* 866:448 */     }, new Object()new Object
/* 867:    */     {
/* 868:    */       final int _id;
/* 869:    */       final String _name;
/* 870:448 */     }, new Object()new Object
/* 871:    */     {
/* 872:    */       final int _id;
/* 873:    */       final String _name;
/* 874:448 */     }, new Object()new Object
/* 875:    */     {
/* 876:    */       final int _id;
/* 877:    */       final String _name;
/* 878:448 */     }, new Object()new Object
/* 879:    */     {
/* 880:    */       final int _id;
/* 881:    */       final String _name;
/* 882:448 */     }, new Object()new Object
/* 883:    */     {
/* 884:    */       final int _id;
/* 885:    */       final String _name;
/* 886:448 */     }, new Object()new Object
/* 887:    */     {
/* 888:    */       final int _id;
/* 889:    */       final String _name;
/* 890:448 */     }, new Object()new Object
/* 891:    */     {
/* 892:    */       final int _id;
/* 893:    */       final String _name;
/* 894:448 */     }, new Object()new Object
/* 895:    */     {
/* 896:    */       final int _id;
/* 897:    */       final String _name;
/* 898:448 */     }, new Object()new Object
/* 899:    */     {
/* 900:    */       final int _id;
/* 901:    */       final String _name;
/* 902:448 */     }, new Object()new Object
/* 903:    */     {
/* 904:    */       final int _id;
/* 905:    */       final String _name;
/* 906:448 */     }, new Object()new Object
/* 907:    */     {
/* 908:    */       final int _id;
/* 909:    */       final String _name;
/* 910:448 */     }, new Object()new Object
/* 911:    */     {
/* 912:    */       final int _id;
/* 913:    */       final String _name;
/* 914:448 */     }, new Object()new Object
/* 915:    */     {
/* 916:    */       final int _id;
/* 917:    */       final String _name;
/* 918:448 */     }, new Object()new Object
/* 919:    */     {
/* 920:    */       final int _id;
/* 921:    */       final String _name;
/* 922:448 */     }, new Object()new Object
/* 923:    */     {
/* 924:    */       final int _id;
/* 925:    */       final String _name;
/* 926:448 */     }, new Object()new Object
/* 927:    */     {
/* 928:    */       final int _id;
/* 929:    */       final String _name;
/* 930:448 */     }, new Object()new Object
/* 931:    */     {
/* 932:    */       final int _id;
/* 933:    */       final String _name;
/* 934:448 */     }, new Object()new Object
/* 935:    */     {
/* 936:    */       final int _id;
/* 937:    */       final String _name;
/* 938:448 */     }, new Object()new Object
/* 939:    */     {
/* 940:    */       final int _id;
/* 941:    */       final String _name;
/* 942:448 */     }, new Object()new Object
/* 943:    */     {
/* 944:    */       final int _id;
/* 945:    */       final String _name;
/* 946:448 */     }, new Object()new Object
/* 947:    */     {
/* 948:    */       final int _id;
/* 949:    */       final String _name;
/* 950:448 */     }, new Object()new Object
/* 951:    */     {
/* 952:    */       final int _id;
/* 953:    */       final String _name;
/* 954:448 */     }, new Object()new Object
/* 955:    */     {
/* 956:    */       final int _id;
/* 957:    */       final String _name;
/* 958:448 */     }, new Object()new Object
/* 959:    */     {
/* 960:    */       final int _id;
/* 961:    */       final String _name;
/* 962:448 */     }, new Object()new Object
/* 963:    */     {
/* 964:    */       final int _id;
/* 965:    */       final String _name;
/* 966:448 */     }, new Object()new Object
/* 967:    */     {
/* 968:    */       final int _id;
/* 969:    */       final String _name;
/* 970:448 */     }, new Object()new Object
/* 971:    */     {
/* 972:    */       final int _id;
/* 973:    */       final String _name;
/* 974:448 */     }, new Object()new Object
/* 975:    */     {
/* 976:    */       final int _id;
/* 977:    */       final String _name;
/* 978:448 */     }, new Object()new Object
/* 979:    */     {
/* 980:    */       final int _id;
/* 981:    */       final String _name;
/* 982:448 */     }, new Object()new Object
/* 983:    */     {
/* 984:    */       final int _id;
/* 985:    */       final String _name;
/* 986:448 */     }, new Object()new Object
/* 987:    */     {
/* 988:    */       final int _id;
/* 989:    */       final String _name;
/* 990:448 */     }, new Object()new Object
/* 991:    */     {
/* 992:    */       final int _id;
/* 993:    */       final String _name;
/* 994:448 */     }, new Object()new Object
/* 995:    */     {
/* 996:    */       final int _id;
/* 997:    */       final String _name;
/* 998:448 */     }, new Object()new Object
/* 999:    */     {
/* :00:    */       final int _id;
/* :01:    */       final String _name;
/* :02:448 */     }, new Object()new Object
/* :03:    */     {
/* :04:    */       final int _id;
/* :05:    */       final String _name;
/* :06:448 */     }, new Object()new Object
/* :07:    */     {
/* :08:    */       final int _id;
/* :09:    */       final String _name;
/* :10:448 */     }, new Object()new Object
/* :11:    */     {
/* :12:    */       final int _id;
/* :13:    */       final String _name;
/* :14:448 */     }, new Object()new Object
/* :15:    */     {
/* :16:    */       final int _id;
/* :17:    */       final String _name;
/* :18:448 */     }, new Object()new Object
/* :19:    */     {
/* :20:    */       final int _id;
/* :21:    */       final String _name;
/* :22:448 */     }, new Object()new Object
/* :23:    */     {
/* :24:    */       final int _id;
/* :25:    */       final String _name;
/* :26:448 */     }, new Object()new Object
/* :27:    */     {
/* :28:    */       final int _id;
/* :29:    */       final String _name;
/* :30:448 */     }, new Object()new Object
/* :31:    */     {
/* :32:    */       final int _id;
/* :33:    */       final String _name;
/* :34:448 */     }, new Object()new Object
/* :35:    */     {
/* :36:    */       final int _id;
/* :37:    */       final String _name;
/* :38:448 */     }, new Object()new Object
/* :39:    */     {
/* :40:    */       final int _id;
/* :41:    */       final String _name;
/* :42:448 */     }, new Object()new Object
/* :43:    */     {
/* :44:    */       final int _id;
/* :45:    */       final String _name;
/* :46:448 */     }, new Object()new Object
/* :47:    */     {
/* :48:    */       final int _id;
/* :49:    */       final String _name;
/* :50:448 */     }, new Object()new Object
/* :51:    */     {
/* :52:    */       final int _id;
/* :53:    */       final String _name;
/* :54:448 */     }, new Object()new Object
/* :55:    */     {
/* :56:    */       final int _id;
/* :57:    */       final String _name;
/* :58:448 */     }, new Object()new Object
/* :59:    */     {
/* :60:    */       final int _id;
/* :61:    */       final String _name;
/* :62:448 */     }, new Object()new Object
/* :63:    */     {
/* :64:    */       final int _id;
/* :65:    */       final String _name;
/* :66:448 */     }, new Object()new Object
/* :67:    */     {
/* :68:    */       final int _id;
/* :69:    */       final String _name;
/* :70:448 */     }, new Object()new Object
/* :71:    */     {
/* :72:    */       final int _id;
/* :73:    */       final String _name;
/* :74:448 */     }, new Object()new Object
/* :75:    */     {
/* :76:    */       final int _id;
/* :77:    */       final String _name;
/* :78:448 */     }, new Object()new Object
/* :79:    */     {
/* :80:    */       final int _id;
/* :81:    */       final String _name;
/* :82:448 */     }, new Object()new Object
/* :83:    */     {
/* :84:    */       final int _id;
/* :85:    */       final String _name;
/* :86:448 */     }, new Object()new Object
/* :87:    */     {
/* :88:    */       final int _id;
/* :89:    */       final String _name;
/* :90:448 */     }, new Object()new Object
/* :91:    */     {
/* :92:    */       final int _id;
/* :93:    */       final String _name;
/* :94:448 */     }, new Object()new Object
/* :95:    */     {
/* :96:    */       final int _id;
/* :97:    */       final String _name;
/* :98:448 */     }, new Object()new Object
/* :99:    */     {
/* ;00:    */       final int _id;
/* ;01:    */       final String _name;
/* ;02:448 */     }, new Object()new Object
/* ;03:    */     {
/* ;04:    */       final int _id;
/* ;05:    */       final String _name;
/* ;06:448 */     }, new Object()new Object
/* ;07:    */     {
/* ;08:    */       final int _id;
/* ;09:    */       final String _name;
/* ;10:448 */     }, new Object()new Object
/* ;11:    */     {
/* ;12:    */       final int _id;
/* ;13:    */       final String _name;
/* ;14:448 */     }, new Object()new Object
/* ;15:    */     {
/* ;16:    */       final int _id;
/* ;17:    */       final String _name;
/* ;18:448 */     }, new Object()new Object
/* ;19:    */     {
/* ;20:    */       final int _id;
/* ;21:    */       final String _name;
/* ;22:448 */     }, new Object()new Object
/* ;23:    */     {
/* ;24:    */       final int _id;
/* ;25:    */       final String _name;
/* ;26:448 */     }, new Object()new Object
/* ;27:    */     {
/* ;28:    */       final int _id;
/* ;29:    */       final String _name;
/* ;30:448 */     }, new Object()new Object
/* ;31:    */     {
/* ;32:    */       final int _id;
/* ;33:    */       final String _name;
/* ;34:448 */     }, new Object()new Object
/* ;35:    */     {
/* ;36:    */       final int _id;
/* ;37:    */       final String _name;
/* ;38:448 */     }, new Object()new Object
/* ;39:    */     {
/* ;40:    */       final int _id;
/* ;41:    */       final String _name;
/* ;42:448 */     }, new Object()new Object
/* ;43:    */     {
/* ;44:    */       final int _id;
/* ;45:    */       final String _name;
/* ;46:448 */     }, new Object()new Object
/* ;47:    */     {
/* ;48:    */       final int _id;
/* ;49:    */       final String _name;
/* ;50:448 */     }, new Object()new Object
/* ;51:    */     {
/* ;52:    */       final int _id;
/* ;53:    */       final String _name;
/* ;54:448 */     }, new Object()new Object
/* ;55:    */     {
/* ;56:    */       final int _id;
/* ;57:    */       final String _name;
/* ;58:448 */     }, new Object()new Object
/* ;59:    */     {
/* ;60:    */       final int _id;
/* ;61:    */       final String _name;
/* ;62:448 */     }, new Object()new Object
/* ;63:    */     {
/* ;64:    */       final int _id;
/* ;65:    */       final String _name;
/* ;66:448 */     }, new Object()new Object
/* ;67:    */     {
/* ;68:    */       final int _id;
/* ;69:    */       final String _name;
/* ;70:448 */     }, new Object()new Object
/* ;71:    */     {
/* ;72:    */       final int _id;
/* ;73:    */       final String _name;
/* ;74:448 */     }, new Object()new Object
/* ;75:    */     {
/* ;76:    */       final int _id;
/* ;77:    */       final String _name;
/* ;78:448 */     }, new Object()new Object
/* ;79:    */     {
/* ;80:    */       final int _id;
/* ;81:    */       final String _name;
/* ;82:448 */     }, new Object()new Object
/* ;83:    */     {
/* ;84:    */       final int _id;
/* ;85:    */       final String _name;
/* ;86:448 */     }, new Object()new Object
/* ;87:    */     {
/* ;88:    */       final int _id;
/* ;89:    */       final String _name;
/* ;90:448 */     }, new Object()new Object
/* ;91:    */     {
/* ;92:    */       final int _id;
/* ;93:    */       final String _name;
/* ;94:448 */     }, new Object()new Object
/* ;95:    */     {
/* ;96:    */       final int _id;
/* ;97:    */       final String _name;
/* ;98:448 */     }, new Object()new Object
/* ;99:    */     {
/* <00:    */       final int _id;
/* <01:    */       final String _name;
/* <02:448 */     }, new Object()new Object
/* <03:    */     {
/* <04:    */       final int _id;
/* <05:    */       final String _name;
/* <06:448 */     }, new Object()new Object
/* <07:    */     {
/* <08:    */       final int _id;
/* <09:    */       final String _name;
/* <10:448 */     }, new Object()new Object
/* <11:    */     {
/* <12:    */       final int _id;
/* <13:    */       final String _name;
/* <14:448 */     }, new Object()new Object
/* <15:    */     {
/* <16:    */       final int _id;
/* <17:    */       final String _name;
/* <18:448 */     }, new Object()new Object
/* <19:    */     {
/* <20:    */       final int _id;
/* <21:    */       final String _name;
/* <22:448 */     }, new Object()new Object
/* <23:    */     {
/* <24:    */       final int _id;
/* <25:    */       final String _name;
/* <26:448 */     }, new Object()new Object
/* <27:    */     {
/* <28:    */       final int _id;
/* <29:    */       final String _name;
/* <30:448 */     }, new Object()new Object
/* <31:    */     {
/* <32:    */       final int _id;
/* <33:    */       final String _name;
/* <34:448 */     }, new Object()new Object
/* <35:    */     {
/* <36:    */       final int _id;
/* <37:    */       final String _name;
/* <38:448 */     }, new Object()new Object
/* <39:    */     {
/* <40:    */       final int _id;
/* <41:    */       final String _name;
/* <42:448 */     }, new Object()new Object
/* <43:    */     {
/* <44:    */       final int _id;
/* <45:    */       final String _name;
/* <46:448 */     }, new Object()new Object
/* <47:    */     {
/* <48:    */       final int _id;
/* <49:    */       final String _name;
/* <50:448 */     }, new Object()new Object
/* <51:    */     {
/* <52:    */       final int _id;
/* <53:    */       final String _name;
/* <54:448 */     }, new Object()new Object
/* <55:    */     {
/* <56:    */       final int _id;
/* <57:    */       final String _name;
/* <58:448 */     }, new Object()new Object
/* <59:    */     {
/* <60:    */       final int _id;
/* <61:    */       final String _name;
/* <62:448 */     }, new Object()new Object
/* <63:    */     {
/* <64:    */       final int _id;
/* <65:    */       final String _name;
/* <66:448 */     }, new Object()new Object
/* <67:    */     {
/* <68:    */       final int _id;
/* <69:    */       final String _name;
/* <70:448 */     }, new Object()new Object
/* <71:    */     {
/* <72:    */       final int _id;
/* <73:    */       final String _name;
/* <74:448 */     }, new Object()new Object
/* <75:    */     {
/* <76:    */       final int _id;
/* <77:    */       final String _name;
/* <78:448 */     }, new Object()new Object
/* <79:    */     {
/* <80:    */       final int _id;
/* <81:    */       final String _name;
/* <82:448 */     }, new Object()new Object
/* <83:    */     {
/* <84:    */       final int _id;
/* <85:    */       final String _name;
/* <86:448 */     }, new Object()new Object
/* <87:    */     {
/* <88:    */       final int _id;
/* <89:    */       final String _name;
/* <90:448 */     }, new Object()new Object
/* <91:    */     {
/* <92:    */       final int _id;
/* <93:    */       final String _name;
/* <94:448 */     }, new Object()new Object
/* <95:    */     {
/* <96:    */       final int _id;
/* <97:    */       final String _name;
/* <98:448 */     }, new Object()new Object
/* <99:    */     {
/* =00:    */       final int _id;
/* =01:    */       final String _name;
/* =02:448 */     }, new Object()new Object
/* =03:    */     {
/* =04:    */       final int _id;
/* =05:    */       final String _name;
/* =06:448 */     }, new Object()new Object
/* =07:    */     {
/* =08:    */       final int _id;
/* =09:    */       final String _name;
/* =10:448 */     }, new Object()new Object
/* =11:    */     {
/* =12:    */       final int _id;
/* =13:    */       final String _name;
/* =14:448 */     }, new Object()new Object
/* =15:    */     {
/* =16:    */       final int _id;
/* =17:    */       final String _name;
/* =18:448 */     }, new Object()new Object
/* =19:    */     {
/* =20:    */       final int _id;
/* =21:    */       final String _name;
/* =22:448 */     }, new Object()new Object
/* =23:    */     {
/* =24:    */       final int _id;
/* =25:    */       final String _name;
/* =26:448 */     }, new Object()new Object
/* =27:    */     {
/* =28:    */       final int _id;
/* =29:    */       final String _name;
/* =30:448 */     }, new Object()new Object
/* =31:    */     {
/* =32:    */       final int _id;
/* =33:    */       final String _name;
/* =34:448 */     }, new Object()new Object
/* =35:    */     {
/* =36:    */       final int _id;
/* =37:    */       final String _name;
/* =38:448 */     }, new Object()new Object
/* =39:    */     {
/* =40:    */       final int _id;
/* =41:    */       final String _name;
/* =42:448 */     }, new Object()new Object
/* =43:    */     {
/* =44:    */       final int _id;
/* =45:    */       final String _name;
/* =46:448 */     }, new Object()new Object
/* =47:    */     {
/* =48:    */       final int _id;
/* =49:    */       final String _name;
/* =50:448 */     }, new Object()new Object
/* =51:    */     {
/* =52:    */       final int _id;
/* =53:    */       final String _name;
/* =54:448 */     }, new Object()new Object
/* =55:    */     {
/* =56:    */       final int _id;
/* =57:    */       final String _name;
/* =58:448 */     }, new Object()new Object
/* =59:    */     {
/* =60:    */       final int _id;
/* =61:    */       final String _name;
/* =62:448 */     }, new Object()new Object
/* =63:    */     {
/* =64:    */       final int _id;
/* =65:    */       final String _name;
/* =66:448 */     }, new Object()new Object
/* =67:    */     {
/* =68:    */       final int _id;
/* =69:    */       final String _name;
/* =70:448 */     }, new Object()new Object
/* =71:    */     {
/* =72:    */       final int _id;
/* =73:    */       final String _name;
/* =74:448 */     }, new Object()new Object
/* =75:    */     {
/* =76:    */       final int _id;
/* =77:    */       final String _name;
/* =78:448 */     }, new Object()new Object
/* =79:    */     {
/* =80:    */       final int _id;
/* =81:    */       final String _name;
/* =82:448 */     }, new Object()new Object
/* =83:    */     {
/* =84:    */       final int _id;
/* =85:    */       final String _name;
/* =86:448 */     }, new Object()new Object
/* =87:    */     {
/* =88:    */       final int _id;
/* =89:    */       final String _name;
/* =90:448 */     }, new Object()new Object
/* =91:    */     {
/* =92:    */       final int _id;
/* =93:    */       final String _name;
/* =94:448 */     }, new Object()new Object
/* =95:    */     {
/* =96:    */       final int _id;
/* =97:    */       final String _name;
/* =98:448 */     }, new Object()new Object
/* =99:    */     {
/* >00:    */       final int _id;
/* >01:    */       final String _name;
/* >02:448 */     }, new Object()new Object
/* >03:    */     {
/* >04:    */       final int _id;
/* >05:    */       final String _name;
/* >06:448 */     }, new Object()new Object
/* >07:    */     {
/* >08:    */       final int _id;
/* >09:    */       final String _name;
/* >10:448 */     }, new Object()new Object
/* >11:    */     {
/* >12:    */       final int _id;
/* >13:    */       final String _name;
/* >14:448 */     }, new Object()new Object
/* >15:    */     {
/* >16:    */       final int _id;
/* >17:    */       final String _name;
/* >18:448 */     }, new Object()new Object
/* >19:    */     {
/* >20:    */       final int _id;
/* >21:    */       final String _name;
/* >22:448 */     }, new Object()new Object
/* >23:    */     {
/* >24:    */       final int _id;
/* >25:    */       final String _name;
/* >26:448 */     }, new Object()new Object
/* >27:    */     {
/* >28:    */       final int _id;
/* >29:    */       final String _name;
/* >30:448 */     }, new Object()new Object
/* >31:    */     {
/* >32:    */       final int _id;
/* >33:    */       final String _name;
/* >34:448 */     }, new Object()new Object
/* >35:    */     {
/* >36:    */       final int _id;
/* >37:    */       final String _name;
/* >38:448 */     }, new Object()new Object
/* >39:    */     {
/* >40:    */       final int _id;
/* >41:    */       final String _name;
/* >42:448 */     }, new Object()new Object
/* >43:    */     {
/* >44:    */       final int _id;
/* >45:    */       final String _name;
/* >46:448 */     }, new Object()new Object
/* >47:    */     {
/* >48:    */       final int _id;
/* >49:    */       final String _name;
/* >50:448 */     }, new Object()new Object
/* >51:    */     {
/* >52:    */       final int _id;
/* >53:    */       final String _name;
/* >54:448 */     }, new Object()new Object
/* >55:    */     {
/* >56:    */       final int _id;
/* >57:    */       final String _name;
/* >58:448 */     }, new Object()new Object
/* >59:    */     {
/* >60:    */       final int _id;
/* >61:    */       final String _name;
/* >62:448 */     }, new Object()new Object
/* >63:    */     {
/* >64:    */       final int _id;
/* >65:    */       final String _name;
/* >66:448 */     }, new Object()new Object
/* >67:    */     {
/* >68:    */       final int _id;
/* >69:    */       final String _name;
/* >70:448 */     }, new Object()
/* >71:    */     {
/* >72:    */       final int _id;
/* >73:    */       final String _name;
/* >74:    */     } };
/* >75:725 */     for (int i = 0; i < props.length; i++) {
/* >76:726 */       if (props[i]._id == propertyId) {
/* >77:727 */         return props[i]._name;
/* >78:    */       }
/* >79:    */     }
/* >80:731 */     return "unknown property";
/* >81:    */   }
/* >82:    */   
/* >83:    */   private static String getBlipType(byte b)
/* >84:    */   {
/* >85:741 */     return EscherBSERecord.getBlipType(b);
/* >86:    */   }
/* >87:    */   
/* >88:    */   private String dec1616(int n32)
/* >89:    */   {
/* >90:749 */     String result = "";
/* >91:750 */     result = result + (short)(n32 >> 16);
/* >92:751 */     result = result + '.';
/* >93:752 */     result = result + (short)(n32 & 0xFFFF);
/* >94:753 */     return result;
/* >95:    */   }
/* >96:    */   
/* >97:    */   private void outHex(int bytes, InputStream in, PrintStream out)
/* >98:    */     throws IOException, LittleEndian.BufferUnderrunException
/* >99:    */   {
/* ?00:765 */     switch (bytes)
/* ?01:    */     {
/* ?02:    */     case 1: 
/* ?03:768 */       out.print(HexDump.toHex((byte)in.read()));
/* ?04:769 */       break;
/* ?05:    */     case 2: 
/* ?06:771 */       out.print(HexDump.toHex(LittleEndian.readShort(in)));
/* ?07:772 */       break;
/* ?08:    */     case 4: 
/* ?09:774 */       out.print(HexDump.toHex(LittleEndian.readInt(in)));
/* ?10:775 */       break;
/* ?11:    */     case 3: 
/* ?12:    */     default: 
/* ?13:777 */       throw new IOException("Unable to output variable of that width");
/* ?14:    */     }
/* ?15:    */   }
/* ?16:    */   
/* ?17:    */   public static void main(String[] args)
/* ?18:    */   {
/* ?19:787 */     main(args, System.out);
/* ?20:    */   }
/* ?21:    */   
/* ?22:    */   public static void main(String[] args, PrintStream out)
/* ?23:    */   {
/* ?24:791 */     String dump = "0F 00 00 F0 89 07 00 00 00 00 06 F0 18 00 00 00 05 04 00 00 02 00 00 00 05 00 00 00 01 00 00 00 01 00 00 00 05 00 00 00 4F 00 01 F0 2F 07 00 00 42 00 07 F0 B7 01 00 00 03 04 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 FF 00 93 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 8B 01 00 00 3F 14 AE 6B 0F 65 B0 48 BF 5E 94 63 80 E8 91 73 92 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 59 01 00 00 00 FE 78 9C E3 9B C4 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 D9 C8 58 CC E8 CF B0 80 61 3A 8A 7E 0D C6 23 AC 4F E0 E2 98 B6 12 2B 06 73 9D 12 E3 52 56 59 F6 08 8A CC 52 66 A3 50 FF 96 2B 94 E9 DF 4C A1 FE 2D 3A 03 AB 9F 81 C2 F0 A3 54 BF 0F 85 EE A7 54 FF 40 FB 7F A0 E3 9F D2 F4 4F 71 FE 19 58 FF 2B 31 7F 67 36 3B 25 4F 99 1B 4E 53 A6 5F 89 25 95 E9 C4 00 C7 83 12 F3 1F 26 35 4A D3 D2 47 0E 0A C3 41 8E C9 8A 52 37 DC 15 A1 D0 0D BC 4C 06 0C 2B 28 2C 13 28 D4 EF 43 61 5A A0 58 3F 85 71 E0 4B 69 9E 64 65 FE 39 C0 E5 22 30 1D 30 27 0E 74 3A 18 60 FD 4A CC B1 2C 13 7D 07 36 2D 2A 31 85 B2 6A 0D 74 1D 1D 22 4D 99 FE 60 0A F5 9B EC 1C 58 FD 67 06 56 3F 38 0D 84 3C A5 30 0E 28 D3 AF C4 A4 CA FA 44 7A 0D 65 6E 60 7F 4D A1 1B 24 58 F7 49 AF A5 CC 0D CC DF 19 FE 03 00 F0 B1 25 4D 42 00 07 F0 E1 01 00 00 03 04 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 FF 00 BD 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 B5 01 00 00 39 50 BE 98 B0 6F 57 24 31 70 5D 23 2F 9F 10 66 DA 03 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 83 01 00 00 00 FE 78 9C A5 52 BF 4B 42 51 14 3E F7 DC 77 7A 16 45 48 8B 3C 48 A8 16 15 0D 6C 88 D0 04 C3 40 A3 32 1C 84 96 08 21 04 A1 C5 5C A2 35 82 C0 35 6A AB 1C 6A 6B A8 24 5A 83 68 08 84 84 96 A2 86 A0 7F C2 86 5E E7 5E F5 41 E4 10 BC 03 1F E7 FB F1 CE B9 F7 F1 9E 7C 05 2E 7A 37 9B E0 45 7B 10 EC 6F 96 5F 1D 74 13 55 7E B0 6C 5D 20 60 C0 49 A2 9A BD 99 4F 50 83 1B 30 38 13 0E 33 60 A6 A7 6B B5 37 EB F4 10 FA 14 15 A0 B6 6B 37 0C 1E B3 49 73 5B A5 C2 26 48 3E C1 E0 6C 08 4A 30 C9 93 AA 02 B8 20 13 62 05 4E E1 E8 D7 7C C0 B8 14 95 5E BE B8 A7 CF 1E BE 55 2C 56 B9 78 DF 08 7E 88 4C 27 FF 7B DB FF 7A DD B7 1A 17 67 34 6A AE BA DA 35 D1 E7 72 BE FE EC 6E FE DA E5 7C 3D EC 7A DE 03 FD 50 06 0B 23 F2 0E F3 B2 A5 11 91 0D 4C B5 B5 F3 BF 94 C1 8F 24 F7 D9 6F 60 94 3B C9 9A F3 1C 6B E7 BB F0 2E 49 B2 25 2B C6 B1 EE 69 EE 15 63 4F 71 7D CE 85 CC C8 35 B9 C3 28 28 CE D0 5C 67 79 F2 4A A2 14 23 A4 38 43 73 9D 2D 69 2F C1 08 31 9F C5 5C 9B EB 7B C5 69 19 B3 B4 81 F3 DC E3 B4 8E 8B CC B3 94 53 5A E7 41 2A 63 9A AA 38 C5 3D 48 BB EC 57 59 6F 2B AD 73 1F 1D 60 92 AE 70 8C BB 8F CE 31 C1 3C 49 27 4A EB DC A4 5B 8C D1 0B 0E 73 37 E9 11 A7 99 C7 E8 41 69 B0 7F 00 96 F2 A7 E8 42 00 07 F0 B4 01 00 00 03 04 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 FF 00 90 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 88 01 00 00 1A BA F9 D6 A9 B9 3A 03 08 61 E9 90 FF 7B 9E E6 12 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 56 01 00 00 00 FE 78 9C E3 13 62 00 02 D6 BB EC 17 19 04 99 FE F3 30 FC FF 07 E4 FE 82 62 98 0C 08 C8 31 48 FD 07 03 06 46 06 2E B8 8C 21 98 75 87 FD 02 C3 42 86 6D 0C 2C 40 39 46 38 8B 85 C1 02 AB A8 14 C4 64 B0 EC 34 06 36 10 93 91 01 64 3A 58 54 87 81 83 FD 22 AB 63 51 66 62 0E 03 33 D0 06 16 A0 1C 2F 43 26 83 1E 50 27 08 68 B0 6C 64 2C 66 F4 67 58 C0 30 1D 45 BF 06 E3 11 D6 27 70 71 4C 5B 89 15 83 B9 4E 89 71 29 AB 2C 7B 04 45 66 29 B3 51 A8 7F CB 15 CA F4 6F A6 50 FF 16 9D 81 D5 CF 40 61 F8 51 AA DF 87 42 F7 53 AA 7F A0 FD 3F D0 F1 4F 69 FA A7 38 FF 0C AC FF 95 98 BF 33 9B 9D 92 A7 CC 0D A7 29 D3 AF C4 92 CA 74 62 80 E3 41 89 F9 0F 93 1A A5 69 E9 23 07 85 E1 20 C7 64 45 A9 1B EE 8A 50 E8 06 5E 26 03 86 15 14 96 09 14 EA F7 A1 30 2D 50 AC 9F C2 38 F0 A5 34 4F B2 32 FF 1C E0 72 11 98 0E 98 13 07 38 1D 28 31 C7 B2 4C F4 1D D8 B4 A0 C4 14 CA AA 35 D0 75 64 88 34 65 FA 83 29 D4 6F B2 73 60 F5 9F A1 54 FF 0E CA D3 40 C8 53 0A E3 E0 09 85 6E 50 65 7D 22 BD 86 32 37 B0 BF A6 D0 0D 12 AC FB A4 D7 52 E6 06 E6 EF 0C FF 01 97 1D 12 C7 42 00 07 F0 C3 01 00 00 03 04 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 FF 00 9F 01 00 00 01 00 00 00 00 00 00 00 00 00 FF FF 20 54 1C F0 97 01 00 00 BA 4C B6 23 BA 8B 27 BE C8 55 59 86 24 9F 89 D4 AE 0E 00 00 00 00 00 00 00 00 00 00 D1 07 00 00 DD 05 00 00 4A AD 6F 00 8A C5 53 00 65 01 00 00 00 FE 78 9C E3 5B C7 00 04 AC 77 D9 2F 32 08 32 FD E7 61 F8 FF 0F C8 FD 05 C5 30 19 10 90 63 90 FA 0F 06 0C 8C 0C 5C 70 19 43 30 EB 0E FB 05 86 85 0C DB 18 58 80 72 8C 70 16 0B 83 05 56 51 29 88 C9 60 D9 69 0C 6C 20 26 23 03 C8 74 B0 A8 0E 03 07 FB 45 56 C7 A2 CC C4 1C 06 66 A0 0D 2C 40 39 5E 86 4C 06 3D A0 4E 10 D0 60 99 C6 B8 98 D1 9F 61 01 C3 74 14 FD 1A 8C 2B D8 84 B1 88 4B A5 A5 75 03 01 50 DF 59 46 77 46 0F A8 3C A6 AB 88 15 83 B9 5E 89 B1 8B D5 97 2D 82 22 B3 94 29 D5 BF E5 CA C0 EA DF AC 43 A1 FD 14 EA 67 A0 30 FC 28 D5 EF 43 A1 FB 7D 87 B8 FF 07 3A FE 07 3A FD 53 EA 7E 0A C3 4F 89 F9 0E 73 EA 69 79 CA DC 70 8A 32 FD 4A 2C 5E 4C DF 87 7A 3C BC E0 A5 30 1E 3E 31 C5 33 AC A0 30 2F 52 A8 DF 87 C2 30 A4 54 3F A5 65 19 85 65 A9 12 D3 2B 16 0D 8A CB 13 4A F3 E3 27 E6 09 03 9D 0E 06 58 BF 12 B3 13 CB C1 01 4E 8B 4A 4C 56 AC 91 03 5D 37 86 48 53 A6 3F 98 42 FD 26 3B 07 56 FF 99 1D 14 EA A7 CC 7E 70 1A 08 79 42 61 1C 3C A5 D0 0D 9C 6C C2 32 6B 29 73 03 DB 6B CA DC C0 F8 97 F5 AD CC 1A CA DC C0 F4 83 32 37 B0 A4 30 CE FC C7 48 99 1B FE 33 32 FC 07 00 6C CC 2E 23 33 00 0B F0 12 00 00 00 BF 00 08 00 08 00 81 01 09 00 00 08 C0 01 40 00 00 08 40 00 1E F1 10 00 00 00 0D 00 00 08 0C 00 00 08 17 00 00 08 F7 00 00 10                                              ";
/* ?25:    */     
/* ?26:    */ 
/* ?27:    */ 
/* ?28:    */ 
/* ?29:    */ 
/* ?30:    */ 
/* ?31:    */ 
/* ?32:    */ 
/* ?33:    */ 
/* ?34:    */ 
/* ?35:    */ 
/* ?36:    */ 
/* ?37:    */ 
/* ?38:    */ 
/* ?39:    */ 
/* ?40:    */ 
/* ?41:    */ 
/* ?42:    */ 
/* ?43:    */ 
/* ?44:    */ 
/* ?45:    */ 
/* ?46:    */ 
/* ?47:    */ 
/* ?48:    */ 
/* ?49:    */ 
/* ?50:    */ 
/* ?51:    */ 
/* ?52:    */ 
/* ?53:    */ 
/* ?54:    */ 
/* ?55:    */ 
/* ?56:    */ 
/* ?57:    */ 
/* ?58:    */ 
/* ?59:    */ 
/* ?60:    */ 
/* ?61:    */ 
/* ?62:    */ 
/* ?63:    */ 
/* ?64:    */ 
/* ?65:    */ 
/* ?66:    */ 
/* ?67:    */ 
/* ?68:    */ 
/* ?69:    */ 
/* ?70:    */ 
/* ?71:    */ 
/* ?72:    */ 
/* ?73:    */ 
/* ?74:    */ 
/* ?75:    */ 
/* ?76:    */ 
/* ?77:    */ 
/* ?78:    */ 
/* ?79:    */ 
/* ?80:    */ 
/* ?81:    */ 
/* ?82:    */ 
/* ?83:    */ 
/* ?84:    */ 
/* ?85:    */ 
/* ?86:    */ 
/* ?87:    */ 
/* ?88:    */ 
/* ?89:    */ 
/* ?90:    */ 
/* ?91:    */ 
/* ?92:    */ 
/* ?93:    */ 
/* ?94:    */ 
/* ?95:    */ 
/* ?96:    */ 
/* ?97:    */ 
/* ?98:    */ 
/* ?99:    */ 
/* @00:    */ 
/* @01:    */ 
/* @02:    */ 
/* @03:    */ 
/* @04:    */ 
/* @05:    */ 
/* @06:    */ 
/* @07:    */ 
/* @08:    */ 
/* @09:    */ 
/* @10:    */ 
/* @11:    */ 
/* @12:    */ 
/* @13:    */ 
/* @14:    */ 
/* @15:    */ 
/* @16:    */ 
/* @17:    */ 
/* @18:    */ 
/* @19:    */ 
/* @20:    */ 
/* @21:    */ 
/* @22:    */ 
/* @23:    */ 
/* @24:    */ 
/* @25:    */ 
/* @26:    */ 
/* @27:    */ 
/* @28:    */ 
/* @29:    */ 
/* @30:    */ 
/* @31:    */ 
/* @32:    */ 
/* @33:    */ 
/* @34:    */ 
/* @35:    */ 
/* @36:    */ 
/* @37:    */ 
/* @38:    */ 
/* @39:    */ 
/* @40:    */ 
/* @41:    */ 
/* @42:    */ 
/* @43:    */ 
/* @44:    */ 
/* @45:    */ 
/* @46:    */ 
/* @47:    */ 
/* @48:    */ 
/* @49:916 */     byte[] bytes = HexRead.readFromString(dump);
/* @50:    */     
/* @51:918 */     EscherDump dumper = new EscherDump();
/* @52:    */     
/* @53:    */ 
/* @54:921 */     dumper.dump(bytes, 0, bytes.length, out);
/* @55:    */   }
/* @56:    */   
/* @57:    */   public void dump(int recordSize, byte[] data, PrintStream out)
/* @58:    */   {
/* @59:926 */     dump(data, 0, recordSize, out);
/* @60:    */   }
/* @61:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.EscherDump
 * JD-Core Version:    0.7.0.1
 */