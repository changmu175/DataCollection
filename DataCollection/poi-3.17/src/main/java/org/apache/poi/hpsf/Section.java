/*    1:     */ package org.apache.poi.hpsf;
/*    2:     */ 
/*    3:     */ import java.io.ByteArrayOutputStream;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.OutputStream;
/*    6:     */ import java.io.UnsupportedEncodingException;
/*    7:     */ import java.util.Collection;
/*    8:     */ import java.util.Date;
/*    9:     */ import java.util.HashMap;
/*   10:     */ import java.util.HashSet;
/*   11:     */ import java.util.LinkedHashMap;
/*   12:     */ import java.util.Map;
/*   13:     */ import java.util.Map.Entry;
/*   14:     */ import java.util.Set;
/*   15:     */ import java.util.TreeMap;
/*   16:     */ import org.apache.commons.collections4.OrderedBidiMap;
/*   17:     */ import org.apache.commons.collections4.bidimap.TreeBidiMap;
/*   18:     */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*   19:     */ import org.apache.poi.hpsf.wellknown.SectionIDMap;
/*   20:     */ import org.apache.poi.util.CodePageUtil;
/*   21:     */ import org.apache.poi.util.LittleEndian;
/*   22:     */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   23:     */ import org.apache.poi.util.POILogFactory;
/*   24:     */ import org.apache.poi.util.POILogger;
/*   25:     */ 
/*   26:     */ public class Section
/*   27:     */ {
/*   28:  46 */   private static final POILogger LOG = POILogFactory.getLogger(Section.class);
/*   29:     */   private Map<Long, String> dictionary;
/*   30:     */   private ClassID formatID;
/*   31:  64 */   private final ByteArrayOutputStream sectionBytes = new ByteArrayOutputStream();
/*   32:     */   private final long _offset;
/*   33:  74 */   private final Map<Long, Property> properties = new LinkedHashMap();
/*   34:     */   private boolean wasNull;
/*   35:     */   
/*   36:     */   public Section()
/*   37:     */   {
/*   38:  87 */     this._offset = -1L;
/*   39:     */   }
/*   40:     */   
/*   41:     */   public Section(Section s)
/*   42:     */   {
/*   43:  99 */     this._offset = -1L;
/*   44: 100 */     setFormatID(s.getFormatID());
/*   45: 101 */     for (Property p : s.properties.values()) {
/*   46: 102 */       this.properties.put(Long.valueOf(p.getID()), new MutableProperty(p));
/*   47:     */     }
/*   48: 104 */     setDictionary(s.getDictionary());
/*   49:     */   }
/*   50:     */   
/*   51:     */   public Section(byte[] src, int offset)
/*   52:     */     throws UnsupportedEncodingException
/*   53:     */   {
/*   54: 123 */     this.formatID = new ClassID(src, offset);
/*   55:     */     
/*   56:     */ 
/*   57:     */ 
/*   58:     */ 
/*   59:     */ 
/*   60: 129 */     int offFix = (int)LittleEndian.getUInt(src, offset + 16);
/*   61: 133 */     if (src[offFix] == 0)
/*   62:     */     {
/*   63: 134 */       for (int i = 0; (i < 3) && (src[offFix] == 0); offFix++) {
/*   64: 134 */         i++;
/*   65:     */       }
/*   66: 136 */       for (int i = 0; (i < 3) && ((src[(offFix + 3)] != 0) || (src[(offFix + 7)] != 0) || (src[(offFix + 11)] != 0)); offFix--) {
/*   67: 136 */         i++;
/*   68:     */       }
/*   69:     */     }
/*   70: 139 */     this._offset = offFix;
/*   71:     */     
/*   72: 141 */     LittleEndianByteArrayInputStream leis = new LittleEndianByteArrayInputStream(src, offFix);
/*   73:     */     
/*   74:     */ 
/*   75:     */ 
/*   76:     */ 
/*   77: 146 */     int size = (int)Math.min(leis.readUInt(), src.length - this._offset);
/*   78:     */     
/*   79:     */ 
/*   80:     */ 
/*   81:     */ 
/*   82: 151 */     int propertyCount = (int)leis.readUInt();
/*   83:     */     
/*   84:     */ 
/*   85:     */ 
/*   86:     */ 
/*   87:     */ 
/*   88:     */ 
/*   89:     */ 
/*   90:     */ 
/*   91:     */ 
/*   92:     */ 
/*   93:     */ 
/*   94:     */ 
/*   95:     */ 
/*   96:     */ 
/*   97:     */ 
/*   98:     */ 
/*   99:     */ 
/*  100:     */ 
/*  101:     */ 
/*  102:     */ 
/*  103:     */ 
/*  104:     */ 
/*  105:     */ 
/*  106:     */ 
/*  107:     */ 
/*  108:     */ 
/*  109: 178 */     TreeBidiMap<Long, Long> offset2Id = new TreeBidiMap();
/*  110: 179 */     for (int i = 0; i < propertyCount; i++)
/*  111:     */     {
/*  112: 181 */       long id = (int)leis.readUInt();
/*  113:     */       
/*  114:     */ 
/*  115: 184 */       long off = (int)leis.readUInt();
/*  116:     */       
/*  117: 186 */       offset2Id.put(Long.valueOf(off), Long.valueOf(id));
/*  118:     */     }
/*  119: 189 */     Long cpOffset = (Long)offset2Id.getKey(Long.valueOf(1L));
/*  120:     */     
/*  121:     */ 
/*  122: 192 */     int codepage = -1;
/*  123: 193 */     if (cpOffset != null)
/*  124:     */     {
/*  125: 195 */       leis.setReadIndex((int)(this._offset + cpOffset.longValue()));
/*  126: 196 */       long type = leis.readUInt();
/*  127: 198 */       if (type != 2L) {
/*  128: 199 */         throw new HPSFRuntimeException("Value type of property ID 1 is not VT_I2 but " + type + ".");
/*  129:     */       }
/*  130: 204 */       codepage = leis.readUShort();
/*  131: 205 */       setCodepage(codepage);
/*  132:     */     }
/*  133: 211 */     for (Entry<Long, Long> me : offset2Id.entrySet())
/*  134:     */     {
/*  135: 212 */       long off = ((Long)me.getKey()).longValue();
/*  136: 213 */       long id = ((Long)me.getValue()).longValue();
/*  137: 215 */       if (id != 1L)
/*  138:     */       {
/*  139: 219 */         int pLen = propLen(offset2Id, Long.valueOf(off), size);
/*  140: 220 */         leis.setReadIndex((int)(this._offset + off));
/*  141: 222 */         if (id == 0L)
/*  142:     */         {
/*  143: 223 */           leis.mark(100000);
/*  144: 224 */           if (!readDictionary(leis, pLen, codepage))
/*  145:     */           {
/*  146: 227 */             leis.reset();
/*  147:     */             try
/*  148:     */             {
/*  149: 230 */               id = Math.max(31L, ((Long)offset2Id.inverseBidiMap().lastKey()).longValue()) + 1L;
/*  150: 231 */               setProperty(new MutableProperty(id, leis, pLen, codepage));
/*  151:     */             }
/*  152:     */             catch (RuntimeException e)
/*  153:     */             {
/*  154: 233 */               LOG.log(3, new Object[] { "Dictionary fallback failed - ignoring property" });
/*  155:     */             }
/*  156:     */           }
/*  157:     */         }
/*  158:     */         else
/*  159:     */         {
/*  160: 237 */           setProperty(new MutableProperty(id, leis, pLen, codepage));
/*  161:     */         }
/*  162:     */       }
/*  163:     */     }
/*  164: 241 */     this.sectionBytes.write(src, (int)this._offset, size);
/*  165: 242 */     padSectionBytes();
/*  166:     */   }
/*  167:     */   
/*  168:     */   private static int propLen(TreeBidiMap<Long, Long> offset2Id, Long entryOffset, long maxSize)
/*  169:     */   {
/*  170: 257 */     Long nextKey = (Long)offset2Id.nextKey(entryOffset);
/*  171: 258 */     long begin = entryOffset.longValue();
/*  172: 259 */     long end = nextKey != null ? nextKey.longValue() : maxSize;
/*  173: 260 */     return (int)(end - begin);
/*  174:     */   }
/*  175:     */   
/*  176:     */   public ClassID getFormatID()
/*  177:     */   {
/*  178: 274 */     return this.formatID;
/*  179:     */   }
/*  180:     */   
/*  181:     */   public void setFormatID(ClassID formatID)
/*  182:     */   {
/*  183: 283 */     this.formatID = formatID;
/*  184:     */   }
/*  185:     */   
/*  186:     */   public void setFormatID(byte[] formatID)
/*  187:     */   {
/*  188: 293 */     ClassID fid = getFormatID();
/*  189: 294 */     if (fid == null)
/*  190:     */     {
/*  191: 295 */       fid = new ClassID();
/*  192: 296 */       setFormatID(fid);
/*  193:     */     }
/*  194: 298 */     fid.setBytes(formatID);
/*  195:     */   }
/*  196:     */   
/*  197:     */   public long getOffset()
/*  198:     */   {
/*  199: 307 */     return this._offset;
/*  200:     */   }
/*  201:     */   
/*  202:     */   public int getPropertyCount()
/*  203:     */   {
/*  204: 316 */     return this.properties.size();
/*  205:     */   }
/*  206:     */   
/*  207:     */   public Property[] getProperties()
/*  208:     */   {
/*  209: 325 */     return (Property[])this.properties.values().toArray(new Property[this.properties.size()]);
/*  210:     */   }
/*  211:     */   
/*  212:     */   public void setProperties(Property[] properties)
/*  213:     */   {
/*  214: 334 */     this.properties.clear();
/*  215: 335 */     for (Property p : properties) {
/*  216: 336 */       setProperty(p);
/*  217:     */     }
/*  218:     */   }
/*  219:     */   
/*  220:     */   public Object getProperty(long id)
/*  221:     */   {
/*  222: 351 */     this.wasNull = (!this.properties.containsKey(Long.valueOf(id)));
/*  223: 352 */     return this.wasNull ? null : ((Property)this.properties.get(Long.valueOf(id))).getValue();
/*  224:     */   }
/*  225:     */   
/*  226:     */   public void setProperty(int id, String value)
/*  227:     */   {
/*  228: 362 */     setProperty(id, 30L, value);
/*  229:     */   }
/*  230:     */   
/*  231:     */   public void setProperty(int id, int value)
/*  232:     */   {
/*  233: 375 */     setProperty(id, 3L, Integer.valueOf(value));
/*  234:     */   }
/*  235:     */   
/*  236:     */   public void setProperty(int id, long value)
/*  237:     */   {
/*  238: 390 */     setProperty(id, 20L, Long.valueOf(value));
/*  239:     */   }
/*  240:     */   
/*  241:     */   public void setProperty(int id, boolean value)
/*  242:     */   {
/*  243: 405 */     setProperty(id, 11L, Boolean.valueOf(value));
/*  244:     */   }
/*  245:     */   
/*  246:     */   public void setProperty(int id, long variantType, Object value)
/*  247:     */   {
/*  248: 427 */     setProperty(new MutableProperty(id, variantType, value));
/*  249:     */   }
/*  250:     */   
/*  251:     */   public void setProperty(Property p)
/*  252:     */   {
/*  253: 442 */     Property old = (Property)this.properties.get(Long.valueOf(p.getID()));
/*  254: 443 */     if ((old == null) || (!old.equals(p)))
/*  255:     */     {
/*  256: 444 */       this.properties.put(Long.valueOf(p.getID()), p);
/*  257: 445 */       this.sectionBytes.reset();
/*  258:     */     }
/*  259:     */   }
/*  260:     */   
/*  261:     */   public void setProperty(int id, Object value)
/*  262:     */   {
/*  263: 457 */     if ((value instanceof String)) {
/*  264: 458 */       setProperty(id, (String)value);
/*  265: 459 */     } else if ((value instanceof Long)) {
/*  266: 460 */       setProperty(id, ((Long)value).longValue());
/*  267: 461 */     } else if ((value instanceof Integer)) {
/*  268: 462 */       setProperty(id, ((Integer)value).intValue());
/*  269: 463 */     } else if ((value instanceof Short)) {
/*  270: 464 */       setProperty(id, ((Short)value).intValue());
/*  271: 465 */     } else if ((value instanceof Boolean)) {
/*  272: 466 */       setProperty(id, ((Boolean)value).booleanValue());
/*  273: 467 */     } else if ((value instanceof Date)) {
/*  274: 468 */       setProperty(id, 64L, value);
/*  275:     */     } else {
/*  276: 470 */       throw new HPSFRuntimeException("HPSF does not support properties of type " + value.getClass().getName() + ".");
/*  277:     */     }
/*  278:     */   }
/*  279:     */   
/*  280:     */   protected int getPropertyIntValue(long id)
/*  281:     */   {
/*  282: 489 */     Object o = getProperty(id);
/*  283: 490 */     if (o == null) {
/*  284: 491 */       return 0;
/*  285:     */     }
/*  286: 493 */     if ((!(o instanceof Long)) && (!(o instanceof Integer))) {
/*  287: 494 */       throw new HPSFRuntimeException("This property is not an integer type, but " + o.getClass().getName() + ".");
/*  288:     */     }
/*  289: 498 */     Number i = (Number)o;
/*  290: 499 */     return i.intValue();
/*  291:     */   }
/*  292:     */   
/*  293:     */   protected boolean getPropertyBooleanValue(int id)
/*  294:     */   {
/*  295: 514 */     Boolean b = (Boolean)getProperty(id);
/*  296: 515 */     if (b == null) {
/*  297: 516 */       return false;
/*  298:     */     }
/*  299: 518 */     return b.booleanValue();
/*  300:     */   }
/*  301:     */   
/*  302:     */   protected void setPropertyBooleanValue(int id, boolean value)
/*  303:     */   {
/*  304: 533 */     setProperty(id, 11L, Boolean.valueOf(value));
/*  305:     */   }
/*  306:     */   
/*  307:     */   public int getSize()
/*  308:     */   {
/*  309: 540 */     int size = this.sectionBytes.size();
/*  310: 541 */     if (size > 0) {
/*  311: 542 */       return size;
/*  312:     */     }
/*  313:     */     try
/*  314:     */     {
/*  315: 545 */       return calcSize();
/*  316:     */     }
/*  317:     */     catch (HPSFRuntimeException ex)
/*  318:     */     {
/*  319: 547 */       throw ex;
/*  320:     */     }
/*  321:     */     catch (Exception ex)
/*  322:     */     {
/*  323: 549 */       throw new HPSFRuntimeException(ex);
/*  324:     */     }
/*  325:     */   }
/*  326:     */   
/*  327:     */   private int calcSize()
/*  328:     */     throws WritingNotSupportedException, IOException
/*  329:     */   {
/*  330: 563 */     this.sectionBytes.reset();
/*  331: 564 */     write(this.sectionBytes);
/*  332: 565 */     padSectionBytes();
/*  333: 566 */     return this.sectionBytes.size();
/*  334:     */   }
/*  335:     */   
/*  336:     */   private void padSectionBytes()
/*  337:     */   {
/*  338: 570 */     byte[] padArray = { 0, 0, 0 };
/*  339:     */     
/*  340:     */ 
/*  341: 573 */     int pad = 4 - (this.sectionBytes.size() & 0x3) & 0x3;
/*  342: 574 */     this.sectionBytes.write(padArray, 0, pad);
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean wasNull()
/*  346:     */   {
/*  347: 592 */     return this.wasNull;
/*  348:     */   }
/*  349:     */   
/*  350:     */   public String getPIDString(long pid)
/*  351:     */   {
/*  352: 608 */     String s = null;
/*  353: 609 */     Map<Long, String> dic = getDictionary();
/*  354: 610 */     if (dic != null) {
/*  355: 611 */       s = (String)dic.get(Long.valueOf(pid));
/*  356:     */     }
/*  357: 613 */     if (s == null) {
/*  358: 614 */       s = SectionIDMap.getPIDString(getFormatID(), pid);
/*  359:     */     }
/*  360: 616 */     return s;
/*  361:     */   }
/*  362:     */   
/*  363:     */   public void clear()
/*  364:     */   {
/*  365: 624 */     for (Property p : getProperties()) {
/*  366: 625 */       removeProperty(p.getID());
/*  367:     */     }
/*  368:     */   }
/*  369:     */   
/*  370:     */   public boolean equals(Object o)
/*  371:     */   {
/*  372: 655 */     if (!(o instanceof Section)) {
/*  373: 656 */       return false;
/*  374:     */     }
/*  375: 658 */     Section s = (Section)o;
/*  376: 659 */     if (!s.getFormatID().equals(getFormatID())) {
/*  377: 660 */       return false;
/*  378:     */     }
/*  379: 665 */     Set<Long> propIds = new HashSet(this.properties.keySet());
/*  380: 666 */     propIds.addAll(s.properties.keySet());
/*  381: 667 */     propIds.remove(Long.valueOf(0L));
/*  382: 668 */     propIds.remove(Long.valueOf(1L));
/*  383: 670 */     for (Long id : propIds)
/*  384:     */     {
/*  385: 671 */       Property p1 = (Property)this.properties.get(id);
/*  386: 672 */       Property p2 = (Property)s.properties.get(id);
/*  387: 673 */       if ((p1 == null) || (p2 == null) || (!p1.equals(p2))) {
/*  388: 674 */         return false;
/*  389:     */       }
/*  390:     */     }
/*  391: 679 */     Map<Long, String> d1 = getDictionary();
/*  392: 680 */     Map<Long, String> d2 = s.getDictionary();
/*  393:     */     
/*  394: 682 */     return ((d1 == null) && (d2 == null)) || ((d1 != null) && (d2 != null) && (d1.equals(d2)));
/*  395:     */   }
/*  396:     */   
/*  397:     */   public void removeProperty(long id)
/*  398:     */   {
/*  399: 691 */     if (this.properties.remove(Long.valueOf(id)) != null) {
/*  400: 692 */       this.sectionBytes.reset();
/*  401:     */     }
/*  402:     */   }
/*  403:     */   
/*  404:     */   public int write(OutputStream out)
/*  405:     */     throws WritingNotSupportedException, IOException
/*  406:     */   {
/*  407: 714 */     if (this.sectionBytes.size() > 0)
/*  408:     */     {
/*  409: 715 */       this.sectionBytes.writeTo(out);
/*  410: 716 */       return this.sectionBytes.size();
/*  411:     */     }
/*  412: 721 */     int codepage = getCodepage();
/*  413: 722 */     if (codepage == -1)
/*  414:     */     {
/*  415: 723 */       String msg = "The codepage property is not set although a dictionary is present. Defaulting to ISO-8859-1.";
/*  416:     */       
/*  417:     */ 
/*  418: 726 */       LOG.log(5, new Object[] { msg });
/*  419: 727 */       codepage = 1252;
/*  420:     */     }
/*  421: 731 */     ByteArrayOutputStream propertyStream = new ByteArrayOutputStream();
/*  422:     */     
/*  423:     */ 
/*  424:     */ 
/*  425:     */ 
/*  426: 736 */     ByteArrayOutputStream propertyListStream = new ByteArrayOutputStream();
/*  427:     */     
/*  428:     */ 
/*  429: 739 */     int position = 0;
/*  430:     */     
/*  431:     */ 
/*  432:     */ 
/*  433:     */ 
/*  434: 744 */     position += 8 + getPropertyCount() * 2 * 4;
/*  435: 748 */     for (Property p : this.properties.values())
/*  436:     */     {
/*  437: 749 */       long id = p.getID();
/*  438:     */       
/*  439:     */ 
/*  440: 752 */       LittleEndian.putUInt(id, propertyListStream);
/*  441: 753 */       LittleEndian.putUInt(position, propertyListStream);
/*  442: 759 */       if (id != 0L)
/*  443:     */       {
/*  444: 762 */         position += p.write(propertyStream, codepage);
/*  445:     */       }
/*  446:     */       else
/*  447:     */       {
/*  448: 764 */         if (codepage == -1) {
/*  449: 765 */           throw new IllegalPropertySetDataException("Codepage (property 1) is undefined.");
/*  450:     */         }
/*  451: 767 */         position += writeDictionary(propertyStream, codepage);
/*  452:     */       }
/*  453:     */     }
/*  454: 772 */     int streamLength = 8 + propertyListStream.size() + propertyStream.size();
/*  455:     */     
/*  456:     */ 
/*  457: 775 */     LittleEndian.putInt(streamLength, out);
/*  458:     */     
/*  459:     */ 
/*  460: 778 */     LittleEndian.putInt(getPropertyCount(), out);
/*  461:     */     
/*  462:     */ 
/*  463: 781 */     propertyListStream.writeTo(out);
/*  464:     */     
/*  465:     */ 
/*  466: 784 */     propertyStream.writeTo(out);
/*  467:     */     
/*  468: 786 */     return streamLength;
/*  469:     */   }
/*  470:     */   
/*  471:     */   private boolean readDictionary(LittleEndianByteArrayInputStream leis, int length, int codepage)
/*  472:     */     throws UnsupportedEncodingException
/*  473:     */   {
/*  474: 803 */     Map<Long, String> dic = new HashMap();
/*  475:     */     
/*  476:     */ 
/*  477:     */ 
/*  478:     */ 
/*  479: 808 */     long nrEntries = leis.readUInt();
/*  480:     */     
/*  481: 810 */     long id = -1L;
/*  482: 811 */     boolean isCorrupted = false;
/*  483: 812 */     for (int i = 0; i < nrEntries; i++)
/*  484:     */     {
/*  485: 813 */       String errMsg = "The property set's dictionary contains bogus data. All dictionary entries starting with the one with ID " + id + " will be ignored.";
/*  486:     */       
/*  487:     */ 
/*  488:     */ 
/*  489:     */ 
/*  490:     */ 
/*  491: 819 */       id = leis.readUInt();
/*  492:     */       
/*  493:     */ 
/*  494:     */ 
/*  495:     */ 
/*  496:     */ 
/*  497:     */ 
/*  498: 826 */       long sLength = leis.readUInt();
/*  499:     */       
/*  500:     */ 
/*  501: 829 */       int cp = codepage == -1 ? 1252 : codepage;
/*  502: 830 */       int nrBytes = (int)((sLength - 1L) * (cp == 1200 ? 2 : 1));
/*  503: 831 */       if (nrBytes > 16777215)
/*  504:     */       {
/*  505: 832 */         LOG.log(5, new Object[] { errMsg });
/*  506: 833 */         isCorrupted = true;
/*  507: 834 */         break;
/*  508:     */       }
/*  509:     */       try
/*  510:     */       {
/*  511: 838 */         byte[] buf = new byte[nrBytes];
/*  512: 839 */         leis.readFully(buf, 0, nrBytes);
/*  513: 840 */         String str = CodePageUtil.getStringFromCodePage(buf, 0, nrBytes, cp);
/*  514:     */         
/*  515: 842 */         int pad = 1;
/*  516: 843 */         if (cp == 1200) {
/*  517: 844 */           pad = 2 + (4 - (nrBytes + 2 & 0x3) & 0x3);
/*  518:     */         }
/*  519: 846 */         leis.skip(pad);
/*  520:     */         
/*  521: 848 */         dic.put(Long.valueOf(id), str);
/*  522:     */       }
/*  523:     */       catch (RuntimeException ex)
/*  524:     */       {
/*  525: 850 */         LOG.log(5, new Object[] { errMsg, ex });
/*  526: 851 */         isCorrupted = true;
/*  527: 852 */         break;
/*  528:     */       }
/*  529:     */     }
/*  530: 855 */     setDictionary(dic);
/*  531: 856 */     return !isCorrupted;
/*  532:     */   }
/*  533:     */   
/*  534:     */   private int writeDictionary(OutputStream out, int codepage)
/*  535:     */     throws IOException
/*  536:     */   {
/*  537: 871 */     byte[] padding = new byte[4];
/*  538: 872 */     Map<Long, String> dic = getDictionary();
/*  539:     */     
/*  540: 874 */     LittleEndian.putUInt(dic.size(), out);
/*  541: 875 */     int length = 4;
/*  542: 876 */     for (Entry<Long, String> ls : dic.entrySet())
/*  543:     */     {
/*  544: 878 */       LittleEndian.putUInt(((Long)ls.getKey()).longValue(), out);
/*  545: 879 */       length += 4;
/*  546:     */       
/*  547: 881 */       String value = (String)ls.getValue() + "";
/*  548: 882 */       LittleEndian.putUInt(value.length(), out);
/*  549: 883 */       length += 4;
/*  550:     */       
/*  551: 885 */       byte[] bytes = CodePageUtil.getBytesInCodePage(value, codepage);
/*  552: 886 */       out.write(bytes);
/*  553: 887 */       length += bytes.length;
/*  554: 889 */       if (codepage == 1200)
/*  555:     */       {
/*  556: 890 */         int pad = 4 - (length & 0x3) & 0x3;
/*  557: 891 */         out.write(padding, 0, pad);
/*  558: 892 */         length += pad;
/*  559:     */       }
/*  560:     */     }
/*  561: 896 */     int pad = 4 - (length & 0x3) & 0x3;
/*  562: 897 */     out.write(padding, 0, pad);
/*  563: 898 */     length += pad;
/*  564:     */     
/*  565: 900 */     return length;
/*  566:     */   }
/*  567:     */   
/*  568:     */   public void setDictionary(Map<Long, String> dictionary)
/*  569:     */     throws IllegalPropertySetDataException
/*  570:     */   {
/*  571: 920 */     if (dictionary != null)
/*  572:     */     {
/*  573: 921 */       if (this.dictionary == null) {
/*  574: 922 */         this.dictionary = new TreeMap();
/*  575:     */       }
/*  576: 924 */       this.dictionary.putAll(dictionary);
/*  577:     */       
/*  578:     */ 
/*  579:     */ 
/*  580: 928 */       int cp = getCodepage();
/*  581: 929 */       if (cp == -1) {
/*  582: 930 */         setCodepage(1252);
/*  583:     */       }
/*  584: 936 */       setProperty(0, -1L, dictionary);
/*  585:     */     }
/*  586:     */     else
/*  587:     */     {
/*  588: 940 */       removeProperty(0L);
/*  589: 941 */       this.dictionary = null;
/*  590:     */     }
/*  591:     */   }
/*  592:     */   
/*  593:     */   public int hashCode()
/*  594:     */   {
/*  595: 952 */     long hashCode = 0L;
/*  596: 953 */     hashCode += getFormatID().hashCode();
/*  597: 954 */     Property[] pa = getProperties();
/*  598: 955 */     for (int i = 0; i < pa.length; i++) {
/*  599: 956 */       hashCode += pa[i].hashCode();
/*  600:     */     }
/*  601: 958 */     int returnHashCode = (int)(hashCode & 0xFFFFFFFF);
/*  602: 959 */     return returnHashCode;
/*  603:     */   }
/*  604:     */   
/*  605:     */   public String toString()
/*  606:     */   {
/*  607: 969 */     return toString(null);
/*  608:     */   }
/*  609:     */   
/*  610:     */   public String toString(PropertyIDMap idMap)
/*  611:     */   {
/*  612: 973 */     StringBuffer b = new StringBuffer();
/*  613: 974 */     Property[] pa = getProperties();
/*  614: 975 */     b.append("\n\n\n");
/*  615: 976 */     b.append(getClass().getName());
/*  616: 977 */     b.append('[');
/*  617: 978 */     b.append("formatID: ");
/*  618: 979 */     b.append(getFormatID());
/*  619: 980 */     b.append(", offset: ");
/*  620: 981 */     b.append(getOffset());
/*  621: 982 */     b.append(", propertyCount: ");
/*  622: 983 */     b.append(getPropertyCount());
/*  623: 984 */     b.append(", size: ");
/*  624: 985 */     b.append(getSize());
/*  625: 986 */     b.append(", properties: [\n");
/*  626: 987 */     int codepage = getCodepage();
/*  627: 988 */     if (codepage == -1) {
/*  628: 989 */       codepage = 1252;
/*  629:     */     }
/*  630: 991 */     for (Property p : pa)
/*  631:     */     {
/*  632: 992 */       b.append(p.toString(codepage, idMap));
/*  633: 993 */       b.append(",\n");
/*  634:     */     }
/*  635: 995 */     b.append(']');
/*  636: 996 */     b.append(']');
/*  637: 997 */     return b.toString();
/*  638:     */   }
/*  639:     */   
/*  640:     */   public Map<Long, String> getDictionary()
/*  641:     */   {
/*  642:1014 */     if (this.dictionary == null) {
/*  643:1015 */       this.dictionary = ((Map)getProperty(0L));
/*  644:     */     }
/*  645:1018 */     return this.dictionary;
/*  646:     */   }
/*  647:     */   
/*  648:     */   public int getCodepage()
/*  649:     */   {
/*  650:1029 */     Integer codepage = (Integer)getProperty(1L);
/*  651:1030 */     return codepage == null ? -1 : codepage.intValue();
/*  652:     */   }
/*  653:     */   
/*  654:     */   public void setCodepage(int codepage)
/*  655:     */   {
/*  656:1039 */     setProperty(1, 2L, Integer.valueOf(codepage));
/*  657:     */   }
/*  658:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hpsf.Section

 * JD-Core Version:    0.7.0.1

 */