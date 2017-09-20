/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Map;
/*  11:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*  12:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*  13:    */ import org.apache.poi.ddf.EscherDgRecord;
/*  14:    */ import org.apache.poi.ddf.EscherRecord;
/*  15:    */ import org.apache.poi.ddf.EscherRecordFactory;
/*  16:    */ import org.apache.poi.ddf.EscherSerializationListener;
/*  17:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  18:    */ import org.apache.poi.ddf.EscherSpgrRecord;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ import org.apache.poi.util.RecordFormatException;
/*  22:    */ 
/*  23:    */ public final class EscherAggregate
/*  24:    */   extends AbstractEscherHolderRecord
/*  25:    */ {
/*  26:    */   public static final short sid = 9876;
/*  27: 87 */   private static POILogger log = POILogFactory.getLogger(EscherAggregate.class);
/*  28:    */   public static final short ST_MIN = 0;
/*  29:    */   public static final short ST_NOT_PRIMATIVE = 0;
/*  30:    */   public static final short ST_RECTANGLE = 1;
/*  31:    */   public static final short ST_ROUNDRECTANGLE = 2;
/*  32:    */   public static final short ST_ELLIPSE = 3;
/*  33:    */   public static final short ST_DIAMOND = 4;
/*  34:    */   public static final short ST_ISOCELESTRIANGLE = 5;
/*  35:    */   public static final short ST_RIGHTTRIANGLE = 6;
/*  36:    */   public static final short ST_PARALLELOGRAM = 7;
/*  37:    */   public static final short ST_TRAPEZOID = 8;
/*  38:    */   public static final short ST_HEXAGON = 9;
/*  39:    */   public static final short ST_OCTAGON = 10;
/*  40:    */   public static final short ST_PLUS = 11;
/*  41:    */   public static final short ST_STAR = 12;
/*  42:    */   public static final short ST_ARROW = 13;
/*  43:    */   public static final short ST_THICKARROW = 14;
/*  44:    */   public static final short ST_HOMEPLATE = 15;
/*  45:    */   public static final short ST_CUBE = 16;
/*  46:    */   public static final short ST_BALLOON = 17;
/*  47:    */   public static final short ST_SEAL = 18;
/*  48:    */   public static final short ST_ARC = 19;
/*  49:    */   public static final short ST_LINE = 20;
/*  50:    */   public static final short ST_PLAQUE = 21;
/*  51:    */   public static final short ST_CAN = 22;
/*  52:    */   public static final short ST_DONUT = 23;
/*  53:    */   public static final short ST_TEXTSIMPLE = 24;
/*  54:    */   public static final short ST_TEXTOCTAGON = 25;
/*  55:    */   public static final short ST_TEXTHEXAGON = 26;
/*  56:    */   public static final short ST_TEXTCURVE = 27;
/*  57:    */   public static final short ST_TEXTWAVE = 28;
/*  58:    */   public static final short ST_TEXTRING = 29;
/*  59:    */   public static final short ST_TEXTONCURVE = 30;
/*  60:    */   public static final short ST_TEXTONRING = 31;
/*  61:    */   public static final short ST_STRAIGHTCONNECTOR1 = 32;
/*  62:    */   public static final short ST_BENTCONNECTOR2 = 33;
/*  63:    */   public static final short ST_BENTCONNECTOR3 = 34;
/*  64:    */   public static final short ST_BENTCONNECTOR4 = 35;
/*  65:    */   public static final short ST_BENTCONNECTOR5 = 36;
/*  66:    */   public static final short ST_CURVEDCONNECTOR2 = 37;
/*  67:    */   public static final short ST_CURVEDCONNECTOR3 = 38;
/*  68:    */   public static final short ST_CURVEDCONNECTOR4 = 39;
/*  69:    */   public static final short ST_CURVEDCONNECTOR5 = 40;
/*  70:    */   public static final short ST_CALLOUT1 = 41;
/*  71:    */   public static final short ST_CALLOUT2 = 42;
/*  72:    */   public static final short ST_CALLOUT3 = 43;
/*  73:    */   public static final short ST_ACCENTCALLOUT1 = 44;
/*  74:    */   public static final short ST_ACCENTCALLOUT2 = 45;
/*  75:    */   public static final short ST_ACCENTCALLOUT3 = 46;
/*  76:    */   public static final short ST_BORDERCALLOUT1 = 47;
/*  77:    */   public static final short ST_BORDERCALLOUT2 = 48;
/*  78:    */   public static final short ST_BORDERCALLOUT3 = 49;
/*  79:    */   public static final short ST_ACCENTBORDERCALLOUT1 = 50;
/*  80:    */   public static final short ST_ACCENTBORDERCALLOUT2 = 51;
/*  81:    */   public static final short ST_ACCENTBORDERCALLOUT3 = 52;
/*  82:    */   public static final short ST_RIBBON = 53;
/*  83:    */   public static final short ST_RIBBON2 = 54;
/*  84:    */   public static final short ST_CHEVRON = 55;
/*  85:    */   public static final short ST_PENTAGON = 56;
/*  86:    */   public static final short ST_NOSMOKING = 57;
/*  87:    */   public static final short ST_SEAL8 = 58;
/*  88:    */   public static final short ST_SEAL16 = 59;
/*  89:    */   public static final short ST_SEAL32 = 60;
/*  90:    */   public static final short ST_WEDGERECTCALLOUT = 61;
/*  91:    */   public static final short ST_WEDGERRECTCALLOUT = 62;
/*  92:    */   public static final short ST_WEDGEELLIPSECALLOUT = 63;
/*  93:    */   public static final short ST_WAVE = 64;
/*  94:    */   public static final short ST_FOLDEDCORNER = 65;
/*  95:    */   public static final short ST_LEFTARROW = 66;
/*  96:    */   public static final short ST_DOWNARROW = 67;
/*  97:    */   public static final short ST_UPARROW = 68;
/*  98:    */   public static final short ST_LEFTRIGHTARROW = 69;
/*  99:    */   public static final short ST_UPDOWNARROW = 70;
/* 100:    */   public static final short ST_IRREGULARSEAL1 = 71;
/* 101:    */   public static final short ST_IRREGULARSEAL2 = 72;
/* 102:    */   public static final short ST_LIGHTNINGBOLT = 73;
/* 103:    */   public static final short ST_HEART = 74;
/* 104:    */   public static final short ST_PICTUREFRAME = 75;
/* 105:    */   public static final short ST_QUADARROW = 76;
/* 106:    */   public static final short ST_LEFTARROWCALLOUT = 77;
/* 107:    */   public static final short ST_RIGHTARROWCALLOUT = 78;
/* 108:    */   public static final short ST_UPARROWCALLOUT = 79;
/* 109:    */   public static final short ST_DOWNARROWCALLOUT = 80;
/* 110:    */   public static final short ST_LEFTRIGHTARROWCALLOUT = 81;
/* 111:    */   public static final short ST_UPDOWNARROWCALLOUT = 82;
/* 112:    */   public static final short ST_QUADARROWCALLOUT = 83;
/* 113:    */   public static final short ST_BEVEL = 84;
/* 114:    */   public static final short ST_LEFTBRACKET = 85;
/* 115:    */   public static final short ST_RIGHTBRACKET = 86;
/* 116:    */   public static final short ST_LEFTBRACE = 87;
/* 117:    */   public static final short ST_RIGHTBRACE = 88;
/* 118:    */   public static final short ST_LEFTUPARROW = 89;
/* 119:    */   public static final short ST_BENTUPARROW = 90;
/* 120:    */   public static final short ST_BENTARROW = 91;
/* 121:    */   public static final short ST_SEAL24 = 92;
/* 122:    */   public static final short ST_STRIPEDRIGHTARROW = 93;
/* 123:    */   public static final short ST_NOTCHEDRIGHTARROW = 94;
/* 124:    */   public static final short ST_BLOCKARC = 95;
/* 125:    */   public static final short ST_SMILEYFACE = 96;
/* 126:    */   public static final short ST_VERTICALSCROLL = 97;
/* 127:    */   public static final short ST_HORIZONTALSCROLL = 98;
/* 128:    */   public static final short ST_CIRCULARARROW = 99;
/* 129:    */   public static final short ST_NOTCHEDCIRCULARARROW = 100;
/* 130:    */   public static final short ST_UTURNARROW = 101;
/* 131:    */   public static final short ST_CURVEDRIGHTARROW = 102;
/* 132:    */   public static final short ST_CURVEDLEFTARROW = 103;
/* 133:    */   public static final short ST_CURVEDUPARROW = 104;
/* 134:    */   public static final short ST_CURVEDDOWNARROW = 105;
/* 135:    */   public static final short ST_CLOUDCALLOUT = 106;
/* 136:    */   public static final short ST_ELLIPSERIBBON = 107;
/* 137:    */   public static final short ST_ELLIPSERIBBON2 = 108;
/* 138:    */   public static final short ST_FLOWCHARTPROCESS = 109;
/* 139:    */   public static final short ST_FLOWCHARTDECISION = 110;
/* 140:    */   public static final short ST_FLOWCHARTINPUTOUTPUT = 111;
/* 141:    */   public static final short ST_FLOWCHARTPREDEFINEDPROCESS = 112;
/* 142:    */   public static final short ST_FLOWCHARTINTERNALSTORAGE = 113;
/* 143:    */   public static final short ST_FLOWCHARTDOCUMENT = 114;
/* 144:    */   public static final short ST_FLOWCHARTMULTIDOCUMENT = 115;
/* 145:    */   public static final short ST_FLOWCHARTTERMINATOR = 116;
/* 146:    */   public static final short ST_FLOWCHARTPREPARATION = 117;
/* 147:    */   public static final short ST_FLOWCHARTMANUALINPUT = 118;
/* 148:    */   public static final short ST_FLOWCHARTMANUALOPERATION = 119;
/* 149:    */   public static final short ST_FLOWCHARTCONNECTOR = 120;
/* 150:    */   public static final short ST_FLOWCHARTPUNCHEDCARD = 121;
/* 151:    */   public static final short ST_FLOWCHARTPUNCHEDTAPE = 122;
/* 152:    */   public static final short ST_FLOWCHARTSUMMINGJUNCTION = 123;
/* 153:    */   public static final short ST_FLOWCHARTOR = 124;
/* 154:    */   public static final short ST_FLOWCHARTCOLLATE = 125;
/* 155:    */   public static final short ST_FLOWCHARTSORT = 126;
/* 156:    */   public static final short ST_FLOWCHARTEXTRACT = 127;
/* 157:    */   public static final short ST_FLOWCHARTMERGE = 128;
/* 158:    */   public static final short ST_FLOWCHARTOFFLINESTORAGE = 129;
/* 159:    */   public static final short ST_FLOWCHARTONLINESTORAGE = 130;
/* 160:    */   public static final short ST_FLOWCHARTMAGNETICTAPE = 131;
/* 161:    */   public static final short ST_FLOWCHARTMAGNETICDISK = 132;
/* 162:    */   public static final short ST_FLOWCHARTMAGNETICDRUM = 133;
/* 163:    */   public static final short ST_FLOWCHARTDISPLAY = 134;
/* 164:    */   public static final short ST_FLOWCHARTDELAY = 135;
/* 165:    */   public static final short ST_TEXTPLAINTEXT = 136;
/* 166:    */   public static final short ST_TEXTSTOP = 137;
/* 167:    */   public static final short ST_TEXTTRIANGLE = 138;
/* 168:    */   public static final short ST_TEXTTRIANGLEINVERTED = 139;
/* 169:    */   public static final short ST_TEXTCHEVRON = 140;
/* 170:    */   public static final short ST_TEXTCHEVRONINVERTED = 141;
/* 171:    */   public static final short ST_TEXTRINGINSIDE = 142;
/* 172:    */   public static final short ST_TEXTRINGOUTSIDE = 143;
/* 173:    */   public static final short ST_TEXTARCHUPCURVE = 144;
/* 174:    */   public static final short ST_TEXTARCHDOWNCURVE = 145;
/* 175:    */   public static final short ST_TEXTCIRCLECURVE = 146;
/* 176:    */   public static final short ST_TEXTBUTTONCURVE = 147;
/* 177:    */   public static final short ST_TEXTARCHUPPOUR = 148;
/* 178:    */   public static final short ST_TEXTARCHDOWNPOUR = 149;
/* 179:    */   public static final short ST_TEXTCIRCLEPOUR = 150;
/* 180:    */   public static final short ST_TEXTBUTTONPOUR = 151;
/* 181:    */   public static final short ST_TEXTCURVEUP = 152;
/* 182:    */   public static final short ST_TEXTCURVEDOWN = 153;
/* 183:    */   public static final short ST_TEXTCASCADEUP = 154;
/* 184:    */   public static final short ST_TEXTCASCADEDOWN = 155;
/* 185:    */   public static final short ST_TEXTWAVE1 = 156;
/* 186:    */   public static final short ST_TEXTWAVE2 = 157;
/* 187:    */   public static final short ST_TEXTWAVE3 = 158;
/* 188:    */   public static final short ST_TEXTWAVE4 = 159;
/* 189:    */   public static final short ST_TEXTINFLATE = 160;
/* 190:    */   public static final short ST_TEXTDEFLATE = 161;
/* 191:    */   public static final short ST_TEXTINFLATEBOTTOM = 162;
/* 192:    */   public static final short ST_TEXTDEFLATEBOTTOM = 163;
/* 193:    */   public static final short ST_TEXTINFLATETOP = 164;
/* 194:    */   public static final short ST_TEXTDEFLATETOP = 165;
/* 195:    */   public static final short ST_TEXTDEFLATEINFLATE = 166;
/* 196:    */   public static final short ST_TEXTDEFLATEINFLATEDEFLATE = 167;
/* 197:    */   public static final short ST_TEXTFADERIGHT = 168;
/* 198:    */   public static final short ST_TEXTFADELEFT = 169;
/* 199:    */   public static final short ST_TEXTFADEUP = 170;
/* 200:    */   public static final short ST_TEXTFADEDOWN = 171;
/* 201:    */   public static final short ST_TEXTSLANTUP = 172;
/* 202:    */   public static final short ST_TEXTSLANTDOWN = 173;
/* 203:    */   public static final short ST_TEXTCANUP = 174;
/* 204:    */   public static final short ST_TEXTCANDOWN = 175;
/* 205:    */   public static final short ST_FLOWCHARTALTERNATEPROCESS = 176;
/* 206:    */   public static final short ST_FLOWCHARTOFFPAGECONNECTOR = 177;
/* 207:    */   public static final short ST_CALLOUT90 = 178;
/* 208:    */   public static final short ST_ACCENTCALLOUT90 = 179;
/* 209:    */   public static final short ST_BORDERCALLOUT90 = 180;
/* 210:    */   public static final short ST_ACCENTBORDERCALLOUT90 = 181;
/* 211:    */   public static final short ST_LEFTRIGHTUPARROW = 182;
/* 212:    */   public static final short ST_SUN = 183;
/* 213:    */   public static final short ST_MOON = 184;
/* 214:    */   public static final short ST_BRACKETPAIR = 185;
/* 215:    */   public static final short ST_BRACEPAIR = 186;
/* 216:    */   public static final short ST_SEAL4 = 187;
/* 217:    */   public static final short ST_DOUBLEWAVE = 188;
/* 218:    */   public static final short ST_ACTIONBUTTONBLANK = 189;
/* 219:    */   public static final short ST_ACTIONBUTTONHOME = 190;
/* 220:    */   public static final short ST_ACTIONBUTTONHELP = 191;
/* 221:    */   public static final short ST_ACTIONBUTTONINFORMATION = 192;
/* 222:    */   public static final short ST_ACTIONBUTTONFORWARDNEXT = 193;
/* 223:    */   public static final short ST_ACTIONBUTTONBACKPREVIOUS = 194;
/* 224:    */   public static final short ST_ACTIONBUTTONEND = 195;
/* 225:    */   public static final short ST_ACTIONBUTTONBEGINNING = 196;
/* 226:    */   public static final short ST_ACTIONBUTTONRETURN = 197;
/* 227:    */   public static final short ST_ACTIONBUTTONDOCUMENT = 198;
/* 228:    */   public static final short ST_ACTIONBUTTONSOUND = 199;
/* 229:    */   public static final short ST_ACTIONBUTTONMOVIE = 200;
/* 230:    */   public static final short ST_HOSTCONTROL = 201;
/* 231:    */   public static final short ST_TEXTBOX = 202;
/* 232:    */   public static final short ST_NIL = 4095;
/* 233:298 */   private final Map<EscherRecord, Record> shapeToObj = new HashMap();
/* 234:303 */   private final Map<Integer, NoteRecord> tailRec = new LinkedHashMap();
/* 235:    */   
/* 236:    */   public EscherAggregate(boolean createDefaultTree)
/* 237:    */   {
/* 238:311 */     if (createDefaultTree) {
/* 239:312 */       buildBaseTree();
/* 240:    */     }
/* 241:    */   }
/* 242:    */   
/* 243:    */   public short getSid()
/* 244:    */   {
/* 245:320 */     return 9876;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public String toString()
/* 249:    */   {
/* 250:328 */     String nl = System.getProperty("line.separtor");
/* 251:    */     
/* 252:330 */     StringBuilder result = new StringBuilder();
/* 253:331 */     result.append('[').append(getRecordName()).append(']').append(nl);
/* 254:332 */     for (EscherRecord escherRecord : getEscherRecords()) {
/* 255:333 */       result.append(escherRecord);
/* 256:    */     }
/* 257:335 */     result.append("[/").append(getRecordName()).append(']').append(nl);
/* 258:    */     
/* 259:337 */     return result.toString();
/* 260:    */   }
/* 261:    */   
/* 262:    */   public String toXml(String tab)
/* 263:    */   {
/* 264:347 */     StringBuilder builder = new StringBuilder();
/* 265:348 */     builder.append(tab).append("<").append(getRecordName()).append(">\n");
/* 266:349 */     for (EscherRecord escherRecord : getEscherRecords()) {
/* 267:350 */       builder.append(escherRecord.toXml(tab + "\t"));
/* 268:    */     }
/* 269:352 */     builder.append(tab).append("</").append(getRecordName()).append(">\n");
/* 270:353 */     return builder.toString();
/* 271:    */   }
/* 272:    */   
/* 273:    */   private static boolean isDrawingLayerRecord(short sid)
/* 274:    */   {
/* 275:361 */     return (sid == 236) || (sid == 60) || (sid == 93) || (sid == 438);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public static EscherAggregate createAggregate(List<RecordBase> records, int locFirstDrawingRecord)
/* 279:    */   {
/* 280:379 */     List<EscherRecord> shapeRecords = new ArrayList();
/* 281:380 */     EscherRecordFactory recordFactory = new DefaultEscherRecordFactory()
/* 282:    */     {
/* 283:    */       public EscherRecord createRecord(byte[] data, int offset)
/* 284:    */       {
/* 285:382 */         EscherRecord r = super.createRecord(data, offset);
/* 286:383 */         if ((r.getRecordId() == -4079) || (r.getRecordId() == -4083)) {
/* 287:384 */           this.val$shapeRecords.add(r);
/* 288:    */         }
/* 289:386 */         return r;
/* 290:    */       }
/* 291:390 */     };
/* 292:391 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 293:392 */     EscherAggregate agg = new EscherAggregate(false);
/* 294:393 */     int loc = locFirstDrawingRecord;
/* 295:395 */     while ((loc + 1 < records.size()) && (isDrawingLayerRecord(sid(records, loc))))
/* 296:    */     {
/* 297:    */       try
/* 298:    */       {
/* 299:397 */         if ((sid(records, loc) != 236) && (sid(records, loc) != 60))
/* 300:    */         {
/* 301:398 */           loc++;
/* 302:399 */           continue;
/* 303:    */         }
/* 304:401 */         if (sid(records, loc) == 236) {
/* 305:402 */           buffer.write(((DrawingRecord)records.get(loc)).getRecordData());
/* 306:    */         } else {
/* 307:404 */           buffer.write(((ContinueRecord)records.get(loc)).getData());
/* 308:    */         }
/* 309:    */       }
/* 310:    */       catch (IOException e)
/* 311:    */       {
/* 312:407 */         throw new RuntimeException("Couldn't get data from drawing/continue records", e);
/* 313:    */       }
/* 314:409 */       loc++;
/* 315:    */     }
/* 316:414 */     int pos = 0;
/* 317:415 */     while (pos < buffer.size())
/* 318:    */     {
/* 319:416 */       EscherRecord r = recordFactory.createRecord(buffer.toByteArray(), pos);
/* 320:417 */       int bytesRead = r.fillFields(buffer.toByteArray(), pos, recordFactory);
/* 321:418 */       agg.addEscherRecord(r);
/* 322:419 */       pos += bytesRead;
/* 323:    */     }
/* 324:423 */     loc = locFirstDrawingRecord + 1;
/* 325:424 */     int shapeIndex = 0;
/* 326:426 */     while ((loc < records.size()) && (isDrawingLayerRecord(sid(records, loc)))) {
/* 327:427 */       if (!isObjectRecord(records, loc))
/* 328:    */       {
/* 329:428 */         loc++;
/* 330:    */       }
/* 331:    */       else
/* 332:    */       {
/* 333:431 */         Record objRecord = (Record)records.get(loc);
/* 334:432 */         agg.shapeToObj.put(shapeRecords.get(shapeIndex++), objRecord);
/* 335:433 */         loc++;
/* 336:    */       }
/* 337:    */     }
/* 338:437 */     while ((loc < records.size()) && 
/* 339:438 */       (sid(records, loc) == 28))
/* 340:    */     {
/* 341:439 */       NoteRecord r = (NoteRecord)records.get(loc);
/* 342:440 */       agg.tailRec.put(Integer.valueOf(r.getShapeId()), r);
/* 343:    */       
/* 344:    */ 
/* 345:    */ 
/* 346:444 */       loc++;
/* 347:    */     }
/* 348:447 */     int locLastDrawingRecord = loc;
/* 349:    */     
/* 350:449 */     records.subList(locFirstDrawingRecord, locLastDrawingRecord).clear();
/* 351:450 */     records.add(locFirstDrawingRecord, agg);
/* 352:451 */     return agg;
/* 353:    */   }
/* 354:    */   
/* 355:    */   public int serialize(int offset, byte[] data)
/* 356:    */   {
/* 357:464 */     List<EscherRecord> records = getEscherRecords();
/* 358:465 */     int size = getEscherRecordSize(records);
/* 359:466 */     byte[] buffer = new byte[size];
/* 360:    */     
/* 361:    */ 
/* 362:469 */     final List<Integer> spEndingOffsets = new ArrayList();
/* 363:470 */     final List<EscherRecord> shapes = new ArrayList();
/* 364:471 */     int pos = 0;
/* 365:472 */     for (Object record : records)
/* 366:    */     {
/* 367:473 */       EscherRecord e = (EscherRecord)record;
/* 368:474 */       pos += e.serialize(pos, buffer, new EscherSerializationListener()
/* 369:    */       {
/* 370:    */         public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {}
/* 371:    */         
/* 372:    */         public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record)
/* 373:    */         {
/* 374:479 */           if ((recordId == -4079) || (recordId == -4083))
/* 375:    */           {
/* 376:480 */             spEndingOffsets.add(Integer.valueOf(offset));
/* 377:481 */             shapes.add(record);
/* 378:    */           }
/* 379:    */         }
/* 380:    */       });
/* 381:    */     }
/* 382:486 */     shapes.add(0, null);
/* 383:487 */     spEndingOffsets.add(0, Integer.valueOf(0));
/* 384:    */     
/* 385:    */ 
/* 386:    */ 
/* 387:491 */     pos = offset;
/* 388:492 */     int writtenEscherBytes = 0;
/* 389:494 */     for (int i = 1; i < shapes.size(); i++)
/* 390:    */     {
/* 391:495 */       int endOffset = ((Integer)spEndingOffsets.get(i)).intValue() - 1;
/* 392:    */       int startOffset;
/* 393:    */       int startOffset;
/* 394:497 */       if (i == 1) {
/* 395:498 */         startOffset = 0;
/* 396:    */       } else {
/* 397:500 */         startOffset = ((Integer)spEndingOffsets.get(i - 1)).intValue();
/* 398:    */       }
/* 399:502 */       byte[] drawingData = new byte[endOffset - startOffset + 1];
/* 400:503 */       System.arraycopy(buffer, startOffset, drawingData, 0, drawingData.length);
/* 401:504 */       pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
/* 402:    */       
/* 403:506 */       writtenEscherBytes += drawingData.length;
/* 404:    */       
/* 405:    */ 
/* 406:509 */       Record obj = (Record)this.shapeToObj.get(shapes.get(i));
/* 407:510 */       pos += obj.serialize(pos, data);
/* 408:512 */       if ((i == shapes.size() - 1) && (endOffset < buffer.length - 1))
/* 409:    */       {
/* 410:513 */         drawingData = new byte[buffer.length - endOffset - 1];
/* 411:514 */         System.arraycopy(buffer, endOffset + 1, drawingData, 0, drawingData.length);
/* 412:515 */         pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
/* 413:    */       }
/* 414:    */     }
/* 415:518 */     if (pos - offset < buffer.length - 1)
/* 416:    */     {
/* 417:519 */       byte[] drawingData = new byte[buffer.length - (pos - offset)];
/* 418:520 */       System.arraycopy(buffer, pos - offset, drawingData, 0, drawingData.length);
/* 419:521 */       pos += writeDataIntoDrawingRecord(drawingData, writtenEscherBytes, pos, data, i);
/* 420:    */     }
/* 421:524 */     for (NoteRecord noteRecord : this.tailRec.values())
/* 422:    */     {
/* 423:525 */       Record rec = noteRecord;
/* 424:526 */       pos += rec.serialize(pos, data);
/* 425:    */     }
/* 426:528 */     int bytesWritten = pos - offset;
/* 427:529 */     if (bytesWritten != getRecordSize()) {
/* 428:530 */       throw new RecordFormatException(bytesWritten + " bytes written but getRecordSize() reports " + getRecordSize());
/* 429:    */     }
/* 430:531 */     return bytesWritten;
/* 431:    */   }
/* 432:    */   
/* 433:    */   private int writeDataIntoDrawingRecord(byte[] drawingData, int writtenEscherBytes, int pos, byte[] data, int i)
/* 434:    */   {
/* 435:544 */     int temp = 0;
/* 436:546 */     if ((writtenEscherBytes + drawingData.length > 8224) && (i != 1)) {
/* 437:547 */       for (int j = 0; j < drawingData.length; j += 8224)
/* 438:    */       {
/* 439:548 */         byte[] buf = new byte[Math.min(8224, drawingData.length - j)];
/* 440:549 */         System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
/* 441:550 */         ContinueRecord drawing = new ContinueRecord(buf);
/* 442:551 */         temp += drawing.serialize(pos + temp, data);
/* 443:    */       }
/* 444:    */     } else {
/* 445:554 */       for (int j = 0; j < drawingData.length; j += 8224) {
/* 446:555 */         if (j == 0)
/* 447:    */         {
/* 448:556 */           DrawingRecord drawing = new DrawingRecord();
/* 449:557 */           byte[] buf = new byte[Math.min(8224, drawingData.length - j)];
/* 450:558 */           System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
/* 451:559 */           drawing.setData(buf);
/* 452:560 */           temp += drawing.serialize(pos + temp, data);
/* 453:    */         }
/* 454:    */         else
/* 455:    */         {
/* 456:562 */           byte[] buf = new byte[Math.min(8224, drawingData.length - j)];
/* 457:563 */           System.arraycopy(drawingData, j, buf, 0, Math.min(8224, drawingData.length - j));
/* 458:564 */           ContinueRecord drawing = new ContinueRecord(buf);
/* 459:565 */           temp += drawing.serialize(pos + temp, data);
/* 460:    */         }
/* 461:    */       }
/* 462:    */     }
/* 463:569 */     return temp;
/* 464:    */   }
/* 465:    */   
/* 466:    */   private int getEscherRecordSize(List<EscherRecord> records)
/* 467:    */   {
/* 468:579 */     int size = 0;
/* 469:580 */     for (EscherRecord record : records) {
/* 470:581 */       size += record.getRecordSize();
/* 471:    */     }
/* 472:583 */     return size;
/* 473:    */   }
/* 474:    */   
/* 475:    */   public int getRecordSize()
/* 476:    */   {
/* 477:592 */     int continueRecordsHeadersSize = 0;
/* 478:    */     
/* 479:594 */     List<EscherRecord> records = getEscherRecords();
/* 480:595 */     int rawEscherSize = getEscherRecordSize(records);
/* 481:596 */     byte[] buffer = new byte[rawEscherSize];
/* 482:597 */     final List<Integer> spEndingOffsets = new ArrayList();
/* 483:598 */     int pos = 0;
/* 484:599 */     for (EscherRecord e : records) {
/* 485:600 */       pos += e.serialize(pos, buffer, new EscherSerializationListener()
/* 486:    */       {
/* 487:    */         public void beforeRecordSerialize(int offset, short recordId, EscherRecord record) {}
/* 488:    */         
/* 489:    */         public void afterRecordSerialize(int offset, short recordId, int size, EscherRecord record)
/* 490:    */         {
/* 491:605 */           if ((recordId == -4079) || (recordId == -4083)) {
/* 492:606 */             spEndingOffsets.add(Integer.valueOf(offset));
/* 493:    */           }
/* 494:    */         }
/* 495:    */       });
/* 496:    */     }
/* 497:611 */     spEndingOffsets.add(0, Integer.valueOf(0));
/* 498:613 */     for (int i = 1; i < spEndingOffsets.size(); i++)
/* 499:    */     {
/* 500:614 */       if ((i == spEndingOffsets.size() - 1) && (((Integer)spEndingOffsets.get(i)).intValue() < pos)) {
/* 501:615 */         continueRecordsHeadersSize += 4;
/* 502:    */       }
/* 503:617 */       if (((Integer)spEndingOffsets.get(i)).intValue() - ((Integer)spEndingOffsets.get(i - 1)).intValue() > 8224) {
/* 504:620 */         continueRecordsHeadersSize += (((Integer)spEndingOffsets.get(i)).intValue() - ((Integer)spEndingOffsets.get(i - 1)).intValue()) / 8224 * 4;
/* 505:    */       }
/* 506:    */     }
/* 507:623 */     int drawingRecordSize = rawEscherSize + this.shapeToObj.size() * 4;
/* 508:624 */     if ((rawEscherSize != 0) && (spEndingOffsets.size() == 1)) {
/* 509:625 */       continueRecordsHeadersSize += 4;
/* 510:    */     }
/* 511:627 */     int objRecordSize = 0;
/* 512:628 */     for (Record r : this.shapeToObj.values()) {
/* 513:629 */       objRecordSize += r.getRecordSize();
/* 514:    */     }
/* 515:631 */     int tailRecordSize = 0;
/* 516:632 */     for (NoteRecord noteRecord : this.tailRec.values()) {
/* 517:633 */       tailRecordSize += noteRecord.getRecordSize();
/* 518:    */     }
/* 519:635 */     return drawingRecordSize + objRecordSize + tailRecordSize + continueRecordsHeadersSize;
/* 520:    */   }
/* 521:    */   
/* 522:    */   public void associateShapeToObjRecord(EscherRecord r, Record objRecord)
/* 523:    */   {
/* 524:644 */     this.shapeToObj.put(r, objRecord);
/* 525:    */   }
/* 526:    */   
/* 527:    */   public void removeShapeToObjRecord(EscherRecord rec)
/* 528:    */   {
/* 529:652 */     this.shapeToObj.remove(rec);
/* 530:    */   }
/* 531:    */   
/* 532:    */   protected String getRecordName()
/* 533:    */   {
/* 534:659 */     return "ESCHERAGGREGATE";
/* 535:    */   }
/* 536:    */   
/* 537:    */   private static boolean isObjectRecord(List<RecordBase> records, int loc)
/* 538:    */   {
/* 539:671 */     return (sid(records, loc) == 93) || (sid(records, loc) == 438);
/* 540:    */   }
/* 541:    */   
/* 542:    */   private void buildBaseTree()
/* 543:    */   {
/* 544:687 */     EscherContainerRecord dgContainer = new EscherContainerRecord();
/* 545:688 */     EscherContainerRecord spgrContainer = new EscherContainerRecord();
/* 546:689 */     EscherContainerRecord spContainer1 = new EscherContainerRecord();
/* 547:690 */     EscherSpgrRecord spgr = new EscherSpgrRecord();
/* 548:691 */     EscherSpRecord sp1 = new EscherSpRecord();
/* 549:692 */     dgContainer.setRecordId((short)-4094);
/* 550:693 */     dgContainer.setOptions((short)15);
/* 551:694 */     EscherDgRecord dg = new EscherDgRecord();
/* 552:695 */     dg.setRecordId((short)-4088);
/* 553:696 */     short dgId = 1;
/* 554:697 */     dg.setOptions((short)(dgId << 4));
/* 555:698 */     dg.setNumShapes(0);
/* 556:699 */     dg.setLastMSOSPID(1024);
/* 557:700 */     spgrContainer.setRecordId((short)-4093);
/* 558:701 */     spgrContainer.setOptions((short)15);
/* 559:702 */     spContainer1.setRecordId((short)-4092);
/* 560:703 */     spContainer1.setOptions((short)15);
/* 561:704 */     spgr.setRecordId((short)-4087);
/* 562:705 */     spgr.setOptions((short)1);
/* 563:706 */     spgr.setRectX1(0);
/* 564:707 */     spgr.setRectY1(0);
/* 565:708 */     spgr.setRectX2(1023);
/* 566:709 */     spgr.setRectY2(255);
/* 567:710 */     sp1.setRecordId((short)-4086);
/* 568:    */     
/* 569:712 */     sp1.setOptions((short)2);
/* 570:713 */     sp1.setVersion((short)2);
/* 571:714 */     sp1.setShapeId(-1);
/* 572:715 */     sp1.setFlags(5);
/* 573:716 */     dgContainer.addChildRecord(dg);
/* 574:717 */     dgContainer.addChildRecord(spgrContainer);
/* 575:718 */     spgrContainer.addChildRecord(spContainer1);
/* 576:719 */     spContainer1.addChildRecord(spgr);
/* 577:720 */     spContainer1.addChildRecord(sp1);
/* 578:721 */     addEscherRecord(dgContainer);
/* 579:    */   }
/* 580:    */   
/* 581:    */   public void setDgId(short dgId)
/* 582:    */   {
/* 583:732 */     EscherContainerRecord dgContainer = getEscherContainer();
/* 584:733 */     EscherDgRecord dg = (EscherDgRecord)dgContainer.getChildById((short)-4088);
/* 585:734 */     dg.setOptions((short)(dgId << 4));
/* 586:    */   }
/* 587:    */   
/* 588:    */   public void setMainSpRecordId(int shapeId)
/* 589:    */   {
/* 590:749 */     EscherContainerRecord dgContainer = getEscherContainer();
/* 591:750 */     EscherContainerRecord spgrConatiner = (EscherContainerRecord)dgContainer.getChildById((short)-4093);
/* 592:751 */     EscherContainerRecord spContainer = (EscherContainerRecord)spgrConatiner.getChild(0);
/* 593:752 */     EscherSpRecord sp = (EscherSpRecord)spContainer.getChildById((short)-4086);
/* 594:753 */     sp.setShapeId(shapeId);
/* 595:    */   }
/* 596:    */   
/* 597:    */   private static short sid(List<RecordBase> records, int loc)
/* 598:    */   {
/* 599:762 */     RecordBase record = (RecordBase)records.get(loc);
/* 600:763 */     if ((record instanceof Record)) {
/* 601:764 */       return ((Record)record).getSid();
/* 602:    */     }
/* 603:768 */     return -1;
/* 604:    */   }
/* 605:    */   
/* 606:    */   public Map<EscherRecord, Record> getShapeToObjMapping()
/* 607:    */   {
/* 608:779 */     return Collections.unmodifiableMap(this.shapeToObj);
/* 609:    */   }
/* 610:    */   
/* 611:    */   public Map<Integer, NoteRecord> getTailRecords()
/* 612:    */   {
/* 613:787 */     return Collections.unmodifiableMap(this.tailRec);
/* 614:    */   }
/* 615:    */   
/* 616:    */   public NoteRecord getNoteRecordByObj(ObjRecord obj)
/* 617:    */   {
/* 618:795 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)obj.getSubRecords().get(0);
/* 619:796 */     return (NoteRecord)this.tailRec.get(Integer.valueOf(cod.getObjectId()));
/* 620:    */   }
/* 621:    */   
/* 622:    */   public void addTailRecord(NoteRecord note)
/* 623:    */   {
/* 624:804 */     this.tailRec.put(Integer.valueOf(note.getShapeId()), note);
/* 625:    */   }
/* 626:    */   
/* 627:    */   public void removeTailRecord(NoteRecord note)
/* 628:    */   {
/* 629:812 */     this.tailRec.remove(Integer.valueOf(note.getShapeId()));
/* 630:    */   }
/* 631:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.EscherAggregate
 * JD-Core Version:    0.7.0.1
 */