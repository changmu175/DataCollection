/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Collections;
/*  11:    */ import java.util.List;
/*  12:    */ import org.apache.poi.EmptyFileException;
/*  13:    */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*  14:    */ import org.apache.poi.hpsf.wellknown.SectionIDMap;
/*  15:    */ import org.apache.poi.poifs.filesystem.DirectoryEntry;
/*  16:    */ import org.apache.poi.poifs.filesystem.Entry;
/*  17:    */ import org.apache.poi.util.CodePageUtil;
/*  18:    */ import org.apache.poi.util.IOUtils;
/*  19:    */ import org.apache.poi.util.LittleEndian;
/*  20:    */ import org.apache.poi.util.NotImplemented;
/*  21:    */ 
/*  22:    */ public class PropertySet
/*  23:    */ {
/*  24:    */   public static final int OS_WIN16 = 0;
/*  25:    */   public static final int OS_MACINTOSH = 1;
/*  26:    */   public static final int OS_WIN32 = 2;
/*  27:    */   static final int BYTE_ORDER_ASSERTION = 65534;
/*  28:    */   static final int FORMAT_ASSERTION = 0;
/*  29:    */   static final int OFFSET_HEADER = 28;
/*  30:    */   private int byteOrder;
/*  31:    */   private int format;
/*  32:    */   private int osVersion;
/*  33:    */   private ClassID classID;
/*  34:137 */   private final List<Section> sections = new ArrayList();
/*  35:    */   
/*  36:    */   public PropertySet()
/*  37:    */   {
/*  38:147 */     this.byteOrder = 65534;
/*  39:    */     
/*  40:    */ 
/*  41:150 */     this.format = 0;
/*  42:    */     
/*  43:    */ 
/*  44:    */ 
/*  45:154 */     this.osVersion = 133636;
/*  46:    */     
/*  47:    */ 
/*  48:157 */     this.classID = new ClassID();
/*  49:    */     
/*  50:    */ 
/*  51:    */ 
/*  52:161 */     addSection(new MutableSection());
/*  53:    */   }
/*  54:    */   
/*  55:    */   public PropertySet(InputStream stream)
/*  56:    */     throws NoPropertySetStreamException, MarkUnsupportedException, IOException, UnsupportedEncodingException
/*  57:    */   {
/*  58:191 */     if (!isPropertySetStream(stream)) {
/*  59:192 */       throw new NoPropertySetStreamException();
/*  60:    */     }
/*  61:195 */     byte[] buffer = IOUtils.toByteArray(stream);
/*  62:196 */     init(buffer, 0, buffer.length);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public PropertySet(byte[] stream, int offset, int length)
/*  66:    */     throws NoPropertySetStreamException, UnsupportedEncodingException
/*  67:    */   {
/*  68:217 */     if (!isPropertySetStream(stream, offset, length)) {
/*  69:218 */       throw new NoPropertySetStreamException();
/*  70:    */     }
/*  71:220 */     init(stream, offset, length);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public PropertySet(byte[] stream)
/*  75:    */     throws NoPropertySetStreamException, UnsupportedEncodingException
/*  76:    */   {
/*  77:236 */     this(stream, 0, stream.length);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public PropertySet(PropertySet ps)
/*  81:    */   {
/*  82:248 */     setByteOrder(ps.getByteOrder());
/*  83:249 */     setFormat(ps.getFormat());
/*  84:250 */     setOSVersion(ps.getOSVersion());
/*  85:251 */     setClassID(ps.getClassID());
/*  86:252 */     for (Section section : ps.getSections()) {
/*  87:253 */       this.sections.add(new MutableSection(section));
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public int getByteOrder()
/*  92:    */   {
/*  93:262 */     return this.byteOrder;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setByteOrder(int byteOrder)
/*  97:    */   {
/*  98:271 */     this.byteOrder = byteOrder;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public int getFormat()
/* 102:    */   {
/* 103:278 */     return this.format;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setFormat(int format)
/* 107:    */   {
/* 108:287 */     this.format = format;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public int getOSVersion()
/* 112:    */   {
/* 113:294 */     return this.osVersion;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setOSVersion(int osVersion)
/* 117:    */   {
/* 118:303 */     this.osVersion = osVersion;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public ClassID getClassID()
/* 122:    */   {
/* 123:311 */     return this.classID;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setClassID(ClassID classID)
/* 127:    */   {
/* 128:320 */     this.classID = classID;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getSectionCount()
/* 132:    */   {
/* 133:327 */     return this.sections.size();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public List<Section> getSections()
/* 137:    */   {
/* 138:334 */     return Collections.unmodifiableList(this.sections);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void addSection(Section section)
/* 142:    */   {
/* 143:347 */     this.sections.add(section);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void clearSections()
/* 147:    */   {
/* 148:354 */     this.sections.clear();
/* 149:    */   }
/* 150:    */   
/* 151:    */   public PropertyIDMap getPropertySetIDMap()
/* 152:    */   {
/* 153:363 */     return null;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static boolean isPropertySetStream(InputStream stream)
/* 157:    */     throws MarkUnsupportedException, IOException
/* 158:    */   {
/* 159:387 */     int BUFFER_SIZE = 50;
/* 160:    */     try
/* 161:    */     {
/* 162:393 */       byte[] buffer = IOUtils.peekFirstNBytes(stream, 50);
/* 163:394 */       return isPropertySetStream(buffer, 0, buffer.length);
/* 164:    */     }
/* 165:    */     catch (EmptyFileException e) {}
/* 166:397 */     return false;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public static boolean isPropertySetStream(byte[] src, int offset, int length)
/* 170:    */   {
/* 171:420 */     int o = offset;
/* 172:421 */     int byteOrder = LittleEndian.getUShort(src, o);
/* 173:422 */     o += 2;
/* 174:423 */     if (byteOrder != 65534) {
/* 175:424 */       return false;
/* 176:    */     }
/* 177:426 */     int format = LittleEndian.getUShort(src, o);
/* 178:427 */     o += 2;
/* 179:428 */     if (format != 0) {
/* 180:429 */       return false;
/* 181:    */     }
/* 182:432 */     o += 4;
/* 183:    */     
/* 184:434 */     o += 16;
/* 185:435 */     long sectionCount = LittleEndian.getUInt(src, o);
/* 186:436 */     return sectionCount >= 0L;
/* 187:    */   }
/* 188:    */   
/* 189:    */   private void init(byte[] src, int offset, int length)
/* 190:    */     throws UnsupportedEncodingException
/* 191:    */   {
/* 192:461 */     int o = offset;
/* 193:462 */     this.byteOrder = LittleEndian.getUShort(src, o);
/* 194:463 */     o += 2;
/* 195:464 */     this.format = LittleEndian.getUShort(src, o);
/* 196:465 */     o += 2;
/* 197:466 */     this.osVersion = ((int)LittleEndian.getUInt(src, o));
/* 198:467 */     o += 4;
/* 199:468 */     this.classID = new ClassID(src, o);
/* 200:469 */     o += 16;
/* 201:470 */     int sectionCount = LittleEndian.getInt(src, o);
/* 202:471 */     o += 4;
/* 203:472 */     if (sectionCount < 0) {
/* 204:473 */       throw new HPSFRuntimeException("Section count " + sectionCount + " is negative.");
/* 205:    */     }
/* 206:493 */     for (int i = 0; i < sectionCount; i++)
/* 207:    */     {
/* 208:494 */       Section s = new MutableSection(src, o);
/* 209:495 */       o += 20;
/* 210:496 */       this.sections.add(s);
/* 211:    */     }
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void write(OutputStream out)
/* 215:    */     throws WritingNotSupportedException, IOException
/* 216:    */   {
/* 217:512 */     int nrSections = getSectionCount();
/* 218:    */     
/* 219:    */ 
/* 220:515 */     LittleEndian.putShort(out, (short)getByteOrder());
/* 221:516 */     LittleEndian.putShort(out, (short)getFormat());
/* 222:517 */     LittleEndian.putInt(getOSVersion(), out);
/* 223:518 */     putClassId(out, getClassID());
/* 224:519 */     LittleEndian.putInt(nrSections, out);
/* 225:520 */     int offset = 28;
/* 226:    */     
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:525 */     offset += nrSections * 20;
/* 231:526 */     int sectionsBegin = offset;
/* 232:527 */     for (Section section : getSections())
/* 233:    */     {
/* 234:528 */       ClassID formatID = section.getFormatID();
/* 235:529 */       if (formatID == null) {
/* 236:530 */         throw new NoFormatIDException();
/* 237:    */       }
/* 238:532 */       putClassId(out, formatID);
/* 239:533 */       LittleEndian.putUInt(offset, out);
/* 240:    */       try
/* 241:    */       {
/* 242:535 */         offset += section.getSize();
/* 243:    */       }
/* 244:    */       catch (HPSFRuntimeException ex)
/* 245:    */       {
/* 246:537 */         Throwable cause = ex.getReason();
/* 247:538 */         if ((cause instanceof UnsupportedEncodingException)) {
/* 248:539 */           throw new IllegalPropertySetDataException(cause);
/* 249:    */         }
/* 250:541 */         throw ex;
/* 251:    */       }
/* 252:    */     }
/* 253:546 */     offset = sectionsBegin;
/* 254:547 */     for (Section section : getSections()) {
/* 255:548 */       offset += section.write(out);
/* 256:    */     }
/* 257:552 */     out.close();
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void write(DirectoryEntry dir, String name)
/* 261:    */     throws WritingNotSupportedException, IOException
/* 262:    */   {
/* 263:568 */     if (dir.hasEntry(name))
/* 264:    */     {
/* 265:569 */       Entry e = dir.getEntry(name);
/* 266:570 */       e.delete();
/* 267:    */     }
/* 268:574 */     dir.createDocument(name, toInputStream());
/* 269:    */   }
/* 270:    */   
/* 271:    */   public InputStream toInputStream()
/* 272:    */     throws IOException, WritingNotSupportedException
/* 273:    */   {
/* 274:592 */     ByteArrayOutputStream psStream = new ByteArrayOutputStream();
/* 275:    */     try
/* 276:    */     {
/* 277:594 */       write(psStream);
/* 278:    */     }
/* 279:    */     finally
/* 280:    */     {
/* 281:596 */       psStream.close();
/* 282:    */     }
/* 283:598 */     byte[] streamData = psStream.toByteArray();
/* 284:599 */     return new ByteArrayInputStream(streamData);
/* 285:    */   }
/* 286:    */   
/* 287:    */   protected String getPropertyStringValue(int propertyId)
/* 288:    */   {
/* 289:611 */     Object propertyValue = getProperty(propertyId);
/* 290:612 */     return getPropertyStringValue(propertyValue);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public static String getPropertyStringValue(Object propertyValue)
/* 294:    */   {
/* 295:624 */     if (propertyValue == null) {
/* 296:625 */       return null;
/* 297:    */     }
/* 298:627 */     if ((propertyValue instanceof String)) {
/* 299:628 */       return (String)propertyValue;
/* 300:    */     }
/* 301:632 */     if ((propertyValue instanceof byte[]))
/* 302:    */     {
/* 303:633 */       byte[] b = (byte[])propertyValue;
/* 304:634 */       switch (b.length)
/* 305:    */       {
/* 306:    */       case 0: 
/* 307:636 */         return "";
/* 308:    */       case 1: 
/* 309:638 */         return Byte.toString(b[0]);
/* 310:    */       case 2: 
/* 311:640 */         return Integer.toString(LittleEndian.getUShort(b));
/* 312:    */       case 4: 
/* 313:642 */         return Long.toString(LittleEndian.getUInt(b));
/* 314:    */       }
/* 315:    */       try
/* 316:    */       {
/* 317:646 */         return CodePageUtil.getStringFromCodePage(b, 1252);
/* 318:    */       }
/* 319:    */       catch (UnsupportedEncodingException e)
/* 320:    */       {
/* 321:649 */         return "";
/* 322:    */       }
/* 323:    */     }
/* 324:653 */     return propertyValue.toString();
/* 325:    */   }
/* 326:    */   
/* 327:    */   public boolean isSummaryInformation()
/* 328:    */   {
/* 329:663 */     if (!this.sections.isEmpty()) {}
/* 330:663 */     return matchesSummary(getFirstSection().getFormatID(), new ClassID[] { SectionIDMap.SUMMARY_INFORMATION_ID });
/* 331:    */   }
/* 332:    */   
/* 333:    */   public boolean isDocumentSummaryInformation()
/* 334:    */   {
/* 335:673 */     return (!this.sections.isEmpty()) && (matchesSummary(getFirstSection().getFormatID(), SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID));
/* 336:    */   }
/* 337:    */   
/* 338:    */   static boolean matchesSummary(ClassID actual, ClassID... expected)
/* 339:    */   {
/* 340:677 */     for (ClassID sum : expected) {
/* 341:678 */       if ((sum.equals(actual)) || (sum.equalsInverted(actual))) {
/* 342:679 */         return true;
/* 343:    */       }
/* 344:    */     }
/* 345:682 */     return false;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public Property[] getProperties()
/* 349:    */     throws NoSingleSectionException
/* 350:    */   {
/* 351:697 */     return getFirstSection().getProperties();
/* 352:    */   }
/* 353:    */   
/* 354:    */   protected Object getProperty(int id)
/* 355:    */     throws NoSingleSectionException
/* 356:    */   {
/* 357:713 */     return getFirstSection().getProperty(id);
/* 358:    */   }
/* 359:    */   
/* 360:    */   protected boolean getPropertyBooleanValue(int id)
/* 361:    */     throws NoSingleSectionException
/* 362:    */   {
/* 363:730 */     return getFirstSection().getPropertyBooleanValue(id);
/* 364:    */   }
/* 365:    */   
/* 366:    */   protected int getPropertyIntValue(int id)
/* 367:    */     throws NoSingleSectionException
/* 368:    */   {
/* 369:748 */     return getFirstSection().getPropertyIntValue(id);
/* 370:    */   }
/* 371:    */   
/* 372:    */   public boolean wasNull()
/* 373:    */     throws NoSingleSectionException
/* 374:    */   {
/* 375:769 */     return getFirstSection().wasNull();
/* 376:    */   }
/* 377:    */   
/* 378:    */   public Section getFirstSection()
/* 379:    */   {
/* 380:780 */     if (this.sections.isEmpty()) {
/* 381:781 */       throw new MissingSectionException("Property set does not contain any sections.");
/* 382:    */     }
/* 383:783 */     return (Section)this.sections.get(0);
/* 384:    */   }
/* 385:    */   
/* 386:    */   public Section getSingleSection()
/* 387:    */   {
/* 388:794 */     int sectionCount = getSectionCount();
/* 389:795 */     if (sectionCount != 1) {
/* 390:796 */       throw new NoSingleSectionException("Property set contains " + sectionCount + " sections.");
/* 391:    */     }
/* 392:798 */     return (Section)this.sections.get(0);
/* 393:    */   }
/* 394:    */   
/* 395:    */   public boolean equals(Object o)
/* 396:    */   {
/* 397:814 */     if ((o == null) || (!(o instanceof PropertySet))) {
/* 398:815 */       return false;
/* 399:    */     }
/* 400:817 */     PropertySet ps = (PropertySet)o;
/* 401:818 */     int byteOrder1 = ps.getByteOrder();
/* 402:819 */     int byteOrder2 = getByteOrder();
/* 403:820 */     ClassID classID1 = ps.getClassID();
/* 404:821 */     ClassID classID2 = getClassID();
/* 405:822 */     int format1 = ps.getFormat();
/* 406:823 */     int format2 = getFormat();
/* 407:824 */     int osVersion1 = ps.getOSVersion();
/* 408:825 */     int osVersion2 = getOSVersion();
/* 409:826 */     int sectionCount1 = ps.getSectionCount();
/* 410:827 */     int sectionCount2 = getSectionCount();
/* 411:828 */     if ((byteOrder1 != byteOrder2) || (!classID1.equals(classID2)) || (format1 != format2) || (osVersion1 != osVersion2) || (sectionCount1 != sectionCount2)) {
/* 412:833 */       return false;
/* 413:    */     }
/* 414:837 */     return getSections().containsAll(ps.getSections());
/* 415:    */   }
/* 416:    */   
/* 417:    */   @NotImplemented
/* 418:    */   public int hashCode()
/* 419:    */   {
/* 420:848 */     throw new UnsupportedOperationException("FIXME: Not yet implemented.");
/* 421:    */   }
/* 422:    */   
/* 423:    */   public String toString()
/* 424:    */   {
/* 425:858 */     StringBuilder b = new StringBuilder();
/* 426:859 */     int sectionCount = getSectionCount();
/* 427:860 */     b.append(getClass().getName());
/* 428:861 */     b.append('[');
/* 429:862 */     b.append("byteOrder: ");
/* 430:863 */     b.append(getByteOrder());
/* 431:864 */     b.append(", classID: ");
/* 432:865 */     b.append(getClassID());
/* 433:866 */     b.append(", format: ");
/* 434:867 */     b.append(getFormat());
/* 435:868 */     b.append(", OSVersion: ");
/* 436:869 */     b.append(getOSVersion());
/* 437:870 */     b.append(", sectionCount: ");
/* 438:871 */     b.append(sectionCount);
/* 439:872 */     b.append(", sections: [\n");
/* 440:873 */     for (Section section : getSections()) {
/* 441:874 */       b.append(section.toString(getPropertySetIDMap()));
/* 442:    */     }
/* 443:876 */     b.append(']');
/* 444:877 */     b.append(']');
/* 445:878 */     return b.toString();
/* 446:    */   }
/* 447:    */   
/* 448:    */   protected void remove1stProperty(long id)
/* 449:    */   {
/* 450:883 */     getFirstSection().removeProperty(id);
/* 451:    */   }
/* 452:    */   
/* 453:    */   protected void set1stProperty(long id, String value)
/* 454:    */   {
/* 455:887 */     getFirstSection().setProperty((int)id, value);
/* 456:    */   }
/* 457:    */   
/* 458:    */   protected void set1stProperty(long id, int value)
/* 459:    */   {
/* 460:891 */     getFirstSection().setProperty((int)id, value);
/* 461:    */   }
/* 462:    */   
/* 463:    */   protected void set1stProperty(long id, boolean value)
/* 464:    */   {
/* 465:895 */     getFirstSection().setProperty((int)id, value);
/* 466:    */   }
/* 467:    */   
/* 468:    */   protected void set1stProperty(long id, byte[] value)
/* 469:    */   {
/* 470:899 */     getFirstSection().setProperty((int)id, value);
/* 471:    */   }
/* 472:    */   
/* 473:    */   private static void putClassId(OutputStream out, ClassID n)
/* 474:    */     throws IOException
/* 475:    */   {
/* 476:903 */     byte[] b = new byte[16];
/* 477:904 */     n.write(b, 0);
/* 478:905 */     out.write(b, 0, b.length);
/* 479:    */   }
/* 480:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.PropertySet
 * JD-Core Version:    0.7.0.1
 */