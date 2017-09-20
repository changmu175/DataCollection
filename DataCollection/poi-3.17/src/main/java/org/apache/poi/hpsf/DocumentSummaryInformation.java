/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*  10:    */ 
/*  11:    */ public class DocumentSummaryInformation
/*  12:    */   extends SpecialPropertySet
/*  13:    */ {
/*  14:    */   public static final String DEFAULT_STREAM_NAME = "\005DocumentSummaryInformation";
/*  15:    */   
/*  16:    */   public PropertyIDMap getPropertySetIDMap()
/*  17:    */   {
/*  18: 46 */     return PropertyIDMap.getDocumentSummaryInformationProperties();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public DocumentSummaryInformation()
/*  22:    */   {
/*  23: 54 */     getFirstSection().setFormatID(org.apache.poi.hpsf.wellknown.SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[0]);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public DocumentSummaryInformation(PropertySet ps)
/*  27:    */     throws UnexpectedPropertySetTypeException
/*  28:    */   {
/*  29: 69 */     super(ps);
/*  30: 70 */     if (!isDocumentSummaryInformation()) {
/*  31: 71 */       throw new UnexpectedPropertySetTypeException("Not a " + getClass().getName());
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DocumentSummaryInformation(InputStream stream)
/*  36:    */     throws NoPropertySetStreamException, MarkUnsupportedException, IOException, UnsupportedEncodingException
/*  37:    */   {
/*  38: 99 */     super(stream);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getCategory()
/*  42:    */   {
/*  43:108 */     return getPropertyStringValue(2);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setCategory(String category)
/*  47:    */   {
/*  48:117 */     getFirstSection().setProperty(2, category);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void removeCategory()
/*  52:    */   {
/*  53:124 */     remove1stProperty(2L);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getPresentationFormat()
/*  57:    */   {
/*  58:136 */     return getPropertyStringValue(3);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setPresentationFormat(String presentationFormat)
/*  62:    */   {
/*  63:145 */     getFirstSection().setProperty(3, presentationFormat);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void removePresentationFormat()
/*  67:    */   {
/*  68:152 */     remove1stProperty(3L);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getByteCount()
/*  72:    */   {
/*  73:164 */     return getPropertyIntValue(4);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setByteCount(int byteCount)
/*  77:    */   {
/*  78:173 */     set1stProperty(4L, byteCount);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void removeByteCount()
/*  82:    */   {
/*  83:180 */     remove1stProperty(4L);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getLineCount()
/*  87:    */   {
/*  88:192 */     return getPropertyIntValue(5);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setLineCount(int lineCount)
/*  92:    */   {
/*  93:201 */     set1stProperty(5L, lineCount);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void removeLineCount()
/*  97:    */   {
/*  98:208 */     remove1stProperty(5L);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public int getParCount()
/* 102:    */   {
/* 103:220 */     return getPropertyIntValue(6);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setParCount(int parCount)
/* 107:    */   {
/* 108:229 */     set1stProperty(6L, parCount);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void removeParCount()
/* 112:    */   {
/* 113:236 */     remove1stProperty(6L);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int getSlideCount()
/* 117:    */   {
/* 118:248 */     return getPropertyIntValue(7);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setSlideCount(int slideCount)
/* 122:    */   {
/* 123:257 */     set1stProperty(7L, slideCount);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void removeSlideCount()
/* 127:    */   {
/* 128:264 */     remove1stProperty(7L);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public int getNoteCount()
/* 132:    */   {
/* 133:276 */     return getPropertyIntValue(8);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setNoteCount(int noteCount)
/* 137:    */   {
/* 138:285 */     set1stProperty(8L, noteCount);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void removeNoteCount()
/* 142:    */   {
/* 143:292 */     remove1stProperty(8L);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public int getHiddenCount()
/* 147:    */   {
/* 148:305 */     return getPropertyIntValue(9);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setHiddenCount(int hiddenCount)
/* 152:    */   {
/* 153:314 */     set1stProperty(9L, hiddenCount);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void removeHiddenCount()
/* 157:    */   {
/* 158:321 */     remove1stProperty(9L);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public int getMMClipCount()
/* 162:    */   {
/* 163:334 */     return getPropertyIntValue(10);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public void setMMClipCount(int mmClipCount)
/* 167:    */   {
/* 168:343 */     set1stProperty(10L, mmClipCount);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void removeMMClipCount()
/* 172:    */   {
/* 173:350 */     remove1stProperty(10L);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public boolean getScale()
/* 177:    */   {
/* 178:362 */     return getPropertyBooleanValue(11);
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void setScale(boolean scale)
/* 182:    */   {
/* 183:371 */     set1stProperty(11L, scale);
/* 184:    */   }
/* 185:    */   
/* 186:    */   public void removeScale()
/* 187:    */   {
/* 188:378 */     remove1stProperty(11L);
/* 189:    */   }
/* 190:    */   
/* 191:    */   public byte[] getHeadingPair()
/* 192:    */   {
/* 193:391 */     notYetImplemented("Reading byte arrays ");
/* 194:392 */     return (byte[])getProperty(12);
/* 195:    */   }
/* 196:    */   
/* 197:    */   public void setHeadingPair(byte[] headingPair)
/* 198:    */   {
/* 199:401 */     notYetImplemented("Writing byte arrays ");
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void removeHeadingPair()
/* 203:    */   {
/* 204:408 */     remove1stProperty(12L);
/* 205:    */   }
/* 206:    */   
/* 207:    */   public byte[] getDocparts()
/* 208:    */   {
/* 209:421 */     notYetImplemented("Reading byte arrays");
/* 210:422 */     return (byte[])getProperty(13);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void setDocparts(byte[] docparts)
/* 214:    */   {
/* 215:433 */     notYetImplemented("Writing byte arrays");
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void removeDocparts()
/* 219:    */   {
/* 220:440 */     remove1stProperty(13L);
/* 221:    */   }
/* 222:    */   
/* 223:    */   public String getManager()
/* 224:    */   {
/* 225:451 */     return getPropertyStringValue(14);
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setManager(String manager)
/* 229:    */   {
/* 230:460 */     set1stProperty(14L, manager);
/* 231:    */   }
/* 232:    */   
/* 233:    */   public void removeManager()
/* 234:    */   {
/* 235:467 */     remove1stProperty(14L);
/* 236:    */   }
/* 237:    */   
/* 238:    */   public String getCompany()
/* 239:    */   {
/* 240:478 */     return getPropertyStringValue(15);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public void setCompany(String company)
/* 244:    */   {
/* 245:487 */     set1stProperty(15L, company);
/* 246:    */   }
/* 247:    */   
/* 248:    */   public void removeCompany()
/* 249:    */   {
/* 250:494 */     remove1stProperty(15L);
/* 251:    */   }
/* 252:    */   
/* 253:    */   public boolean getLinksDirty()
/* 254:    */   {
/* 255:504 */     return getPropertyBooleanValue(16);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public void setLinksDirty(boolean linksDirty)
/* 259:    */   {
/* 260:513 */     set1stProperty(16L, linksDirty);
/* 261:    */   }
/* 262:    */   
/* 263:    */   public void removeLinksDirty()
/* 264:    */   {
/* 265:520 */     remove1stProperty(16L);
/* 266:    */   }
/* 267:    */   
/* 268:    */   public int getCharCountWithSpaces()
/* 269:    */   {
/* 270:532 */     return getPropertyIntValue(17);
/* 271:    */   }
/* 272:    */   
/* 273:    */   public void setCharCountWithSpaces(int count)
/* 274:    */   {
/* 275:541 */     set1stProperty(17L, count);
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void removeCharCountWithSpaces()
/* 279:    */   {
/* 280:548 */     remove1stProperty(17L);
/* 281:    */   }
/* 282:    */   
/* 283:    */   public boolean getHyperlinksChanged()
/* 284:    */   {
/* 285:560 */     return getPropertyBooleanValue(22);
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void setHyperlinksChanged(boolean changed)
/* 289:    */   {
/* 290:570 */     set1stProperty(22L, changed);
/* 291:    */   }
/* 292:    */   
/* 293:    */   public void removeHyperlinksChanged()
/* 294:    */   {
/* 295:578 */     remove1stProperty(22L);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public int getApplicationVersion()
/* 299:    */   {
/* 300:591 */     return getPropertyIntValue(23);
/* 301:    */   }
/* 302:    */   
/* 303:    */   public void setApplicationVersion(int version)
/* 304:    */   {
/* 305:602 */     set1stProperty(23L, version);
/* 306:    */   }
/* 307:    */   
/* 308:    */   public void removeApplicationVersion()
/* 309:    */   {
/* 310:609 */     remove1stProperty(23L);
/* 311:    */   }
/* 312:    */   
/* 313:    */   public byte[] getVBADigitalSignature()
/* 314:    */   {
/* 315:620 */     Object value = getProperty(24);
/* 316:621 */     if ((value != null) && ((value instanceof byte[]))) {
/* 317:622 */       return (byte[])value;
/* 318:    */     }
/* 319:624 */     return null;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public void setVBADigitalSignature(byte[] signature)
/* 323:    */   {
/* 324:634 */     set1stProperty(24L, signature);
/* 325:    */   }
/* 326:    */   
/* 327:    */   public void removeVBADigitalSignature()
/* 328:    */   {
/* 329:641 */     remove1stProperty(24L);
/* 330:    */   }
/* 331:    */   
/* 332:    */   public String getContentType()
/* 333:    */   {
/* 334:651 */     return getPropertyStringValue(26);
/* 335:    */   }
/* 336:    */   
/* 337:    */   public void setContentType(String type)
/* 338:    */   {
/* 339:660 */     set1stProperty(26L, type);
/* 340:    */   }
/* 341:    */   
/* 342:    */   public void removeContentType()
/* 343:    */   {
/* 344:667 */     remove1stProperty(26L);
/* 345:    */   }
/* 346:    */   
/* 347:    */   public String getContentStatus()
/* 348:    */   {
/* 349:677 */     return getPropertyStringValue(27);
/* 350:    */   }
/* 351:    */   
/* 352:    */   public void setContentStatus(String status)
/* 353:    */   {
/* 354:686 */     set1stProperty(27L, status);
/* 355:    */   }
/* 356:    */   
/* 357:    */   public void removeContentStatus()
/* 358:    */   {
/* 359:693 */     remove1stProperty(27L);
/* 360:    */   }
/* 361:    */   
/* 362:    */   public String getLanguage()
/* 363:    */   {
/* 364:703 */     return getPropertyStringValue(28);
/* 365:    */   }
/* 366:    */   
/* 367:    */   public void setLanguage(String language)
/* 368:    */   {
/* 369:712 */     set1stProperty(28L, language);
/* 370:    */   }
/* 371:    */   
/* 372:    */   public void removeLanguage()
/* 373:    */   {
/* 374:719 */     remove1stProperty(28L);
/* 375:    */   }
/* 376:    */   
/* 377:    */   public String getDocumentVersion()
/* 378:    */   {
/* 379:730 */     return getPropertyStringValue(29);
/* 380:    */   }
/* 381:    */   
/* 382:    */   public void setDocumentVersion(String version)
/* 383:    */   {
/* 384:739 */     set1stProperty(29L, version);
/* 385:    */   }
/* 386:    */   
/* 387:    */   public void removeDocumentVersion()
/* 388:    */   {
/* 389:746 */     remove1stProperty(29L);
/* 390:    */   }
/* 391:    */   
/* 392:    */   public CustomProperties getCustomProperties()
/* 393:    */   {
/* 394:756 */     CustomProperties cps = null;
/* 395:757 */     if (getSectionCount() >= 2)
/* 396:    */     {
/* 397:758 */       cps = new CustomProperties();
/* 398:759 */       Section section = (Section)getSections().get(1);
/* 399:760 */       Map<Long, String> dictionary = section.getDictionary();
/* 400:761 */       Property[] properties = section.getProperties();
/* 401:762 */       int propertyCount = 0;
/* 402:763 */       for (Property p : properties)
/* 403:    */       {
/* 404:764 */         long id = p.getID();
/* 405:765 */         if (id == 1L)
/* 406:    */         {
/* 407:766 */           cps.setCodepage(((Integer)p.getValue()).intValue());
/* 408:    */         }
/* 409:767 */         else if (id > 1L)
/* 410:    */         {
/* 411:768 */           propertyCount++;
/* 412:769 */           CustomProperty cp = new CustomProperty(p, (String)dictionary.get(Long.valueOf(id)));
/* 413:770 */           cps.put(cp.getName(), cp);
/* 414:    */         }
/* 415:    */       }
/* 416:773 */       if (cps.size() != propertyCount) {
/* 417:774 */         cps.setPure(false);
/* 418:    */       }
/* 419:    */     }
/* 420:777 */     return cps;
/* 421:    */   }
/* 422:    */   
/* 423:    */   public void setCustomProperties(CustomProperties customProperties)
/* 424:    */   {
/* 425:786 */     ensureSection2();
/* 426:787 */     Section section = (Section)getSections().get(1);
/* 427:788 */     Map<Long, String> dictionary = customProperties.getDictionary();
/* 428:    */     
/* 429:    */ 
/* 430:    */ 
/* 431:    */ 
/* 432:    */ 
/* 433:794 */     int cpCodepage = customProperties.getCodepage();
/* 434:795 */     if (cpCodepage < 0) {
/* 435:796 */       cpCodepage = section.getCodepage();
/* 436:    */     }
/* 437:798 */     if (cpCodepage < 0) {
/* 438:799 */       cpCodepage = 1252;
/* 439:    */     }
/* 440:801 */     customProperties.setCodepage(cpCodepage);
/* 441:802 */     section.setCodepage(cpCodepage);
/* 442:803 */     section.setDictionary(dictionary);
/* 443:804 */     for (CustomProperty p : customProperties.properties()) {
/* 444:805 */       section.setProperty(p);
/* 445:    */     }
/* 446:    */   }
/* 447:    */   
/* 448:    */   private void ensureSection2()
/* 449:    */   {
/* 450:813 */     if (getSectionCount() < 2)
/* 451:    */     {
/* 452:814 */       Section s2 = new MutableSection();
/* 453:815 */       s2.setFormatID(org.apache.poi.hpsf.wellknown.SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[1]);
/* 454:816 */       addSection(s2);
/* 455:    */     }
/* 456:    */   }
/* 457:    */   
/* 458:    */   public void removeCustomProperties()
/* 459:    */   {
/* 460:824 */     if (getSectionCount() < 2) {
/* 461:825 */       throw new HPSFRuntimeException("Illegal internal format of Document SummaryInformation stream: second section is missing.");
/* 462:    */     }
/* 463:828 */     List<Section> l = new LinkedList(getSections());
/* 464:829 */     clearSections();
/* 465:830 */     int idx = 0;
/* 466:831 */     for (Section s : l) {
/* 467:832 */       if (idx++ != 1) {
/* 468:833 */         addSection(s);
/* 469:    */       }
/* 470:    */     }
/* 471:    */   }
/* 472:    */   
/* 473:    */   private void notYetImplemented(String msg)
/* 474:    */   {
/* 475:846 */     throw new UnsupportedOperationException(msg + " is not yet implemented.");
/* 476:    */   }
/* 477:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.DocumentSummaryInformation
 * JD-Core Version:    0.7.0.1
 */