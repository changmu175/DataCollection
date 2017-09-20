/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileInputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import java.util.Collections;
/*  12:    */ import java.util.HashMap;
/*  13:    */ import java.util.Iterator;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Map;
/*  16:    */ import java.util.regex.Pattern;
/*  17:    */ import org.apache.poi.POIXMLDocument;
/*  18:    */ import org.apache.poi.POIXMLDocumentPart;
/*  19:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  20:    */ import org.apache.poi.POIXMLException;
/*  21:    */ import org.apache.poi.POIXMLTypeLoader;
/*  22:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  23:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  24:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  25:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  26:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  27:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  28:    */ import org.apache.poi.sl.usermodel.PictureData.PictureType;
/*  29:    */ import org.apache.poi.sl.usermodel.Resources;
/*  30:    */ import org.apache.poi.sl.usermodel.SlideShow;
/*  31:    */ import org.apache.poi.util.IOUtils;
/*  32:    */ import org.apache.poi.util.Internal;
/*  33:    */ import org.apache.poi.util.LittleEndian;
/*  34:    */ import org.apache.poi.util.POILogFactory;
/*  35:    */ import org.apache.poi.util.POILogger;
/*  36:    */ import org.apache.poi.util.PackageHelper;
/*  37:    */ import org.apache.poi.util.Units;
/*  38:    */ import org.apache.xmlbeans.XmlException;
/*  39:    */ import org.apache.xmlbeans.XmlObject;
/*  40:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  41:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList;
/*  42:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry;
/*  43:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;
/*  44:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList;
/*  45:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry;
/*  46:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList;
/*  47:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry;
/*  48:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize;
/*  49:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize.Factory;
/*  50:    */ import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument;
/*  51:    */ import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument.Factory;
/*  52:    */ 
/*  53:    */ public class XMLSlideShow
/*  54:    */   extends POIXMLDocument
/*  55:    */   implements SlideShow<XSLFShape, XSLFTextParagraph>
/*  56:    */ {
/*  57: 75 */   private static final POILogger LOG = POILogFactory.getLogger(XMLSlideShow.class);
/*  58:    */   private CTPresentation _presentation;
/*  59:    */   private List<XSLFSlide> _slides;
/*  60:    */   private List<XSLFSlideMaster> _masters;
/*  61:    */   private List<XSLFPictureData> _pictures;
/*  62:    */   private XSLFTableStyles _tableStyles;
/*  63:    */   private XSLFNotesMaster _notesMaster;
/*  64:    */   private XSLFCommentAuthors _commentAuthors;
/*  65:    */   
/*  66:    */   public XMLSlideShow()
/*  67:    */   {
/*  68: 86 */     this(empty());
/*  69:    */   }
/*  70:    */   
/*  71:    */   public XMLSlideShow(OPCPackage pkg)
/*  72:    */   {
/*  73: 90 */     super(pkg);
/*  74:    */     try
/*  75:    */     {
/*  76: 93 */       if (getCorePart().getContentType().equals(XSLFRelation.THEME_MANAGER.getContentType())) {
/*  77: 94 */         rebase(getPackage());
/*  78:    */       }
/*  79: 98 */       load(XSLFFactory.getInstance());
/*  80:    */     }
/*  81:    */     catch (Exception e)
/*  82:    */     {
/*  83:100 */       throw new POIXMLException(e);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public XMLSlideShow(InputStream is)
/*  88:    */     throws IOException
/*  89:    */   {
/*  90:105 */     this(PackageHelper.open(is));
/*  91:    */   }
/*  92:    */   
/*  93:    */   static OPCPackage empty()
/*  94:    */   {
/*  95:109 */     InputStream is = XMLSlideShow.class.getResourceAsStream("empty.pptx");
/*  96:110 */     if (is == null) {
/*  97:111 */       throw new POIXMLException("Missing resource 'empty.pptx'");
/*  98:    */     }
/*  99:    */     try
/* 100:    */     {
/* 101:114 */       return OPCPackage.open(is);
/* 102:    */     }
/* 103:    */     catch (Exception e)
/* 104:    */     {
/* 105:116 */       throw new POIXMLException(e);
/* 106:    */     }
/* 107:    */     finally
/* 108:    */     {
/* 109:118 */       IOUtils.closeQuietly(is);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected void onDocumentRead()
/* 114:    */     throws IOException
/* 115:    */   {
/* 116:    */     try
/* 117:    */     {
/* 118:125 */       PresentationDocument doc = PresentationDocument.Factory.parse(getCorePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 119:    */       
/* 120:127 */       this._presentation = doc.getPresentation();
/* 121:    */       
/* 122:129 */       Map<String, XSLFSlideMaster> masterMap = new HashMap();
/* 123:130 */       shIdMap = new HashMap();
/* 124:131 */       for (POIXMLDocumentPart.RelationPart rp : getRelationParts())
/* 125:    */       {
/* 126:132 */         POIXMLDocumentPart p = rp.getDocumentPart();
/* 127:133 */         if ((p instanceof XSLFSlide)) {
/* 128:134 */           shIdMap.put(rp.getRelationship().getId(), (XSLFSlide)p);
/* 129:135 */         } else if ((p instanceof XSLFSlideMaster)) {
/* 130:136 */           masterMap.put(getRelationId(p), (XSLFSlideMaster)p);
/* 131:137 */         } else if ((p instanceof XSLFTableStyles)) {
/* 132:138 */           this._tableStyles = ((XSLFTableStyles)p);
/* 133:139 */         } else if ((p instanceof XSLFNotesMaster)) {
/* 134:140 */           this._notesMaster = ((XSLFNotesMaster)p);
/* 135:141 */         } else if ((p instanceof XSLFCommentAuthors)) {
/* 136:142 */           this._commentAuthors = ((XSLFCommentAuthors)p);
/* 137:    */         }
/* 138:    */       }
/* 139:146 */       this._masters = new ArrayList(masterMap.size());
/* 140:147 */       for (CTSlideMasterIdListEntry masterId : this._presentation.getSldMasterIdLst().getSldMasterIdList())
/* 141:    */       {
/* 142:148 */         XSLFSlideMaster master = (XSLFSlideMaster)masterMap.get(masterId.getId2());
/* 143:149 */         this._masters.add(master);
/* 144:    */       }
/* 145:152 */       this._slides = new ArrayList(shIdMap.size());
/* 146:153 */       if (this._presentation.isSetSldIdLst()) {
/* 147:154 */         for (CTSlideIdListEntry slId : this._presentation.getSldIdLst().getSldIdList())
/* 148:    */         {
/* 149:155 */           XSLFSlide sh = (XSLFSlide)shIdMap.get(slId.getId2());
/* 150:156 */           if (sh == null) {
/* 151:157 */             LOG.log(5, new Object[] { "Slide with r:id " + slId.getId() + " was defined, but didn't exist in package, skipping" });
/* 152:    */           } else {
/* 153:160 */             this._slides.add(sh);
/* 154:    */           }
/* 155:    */         }
/* 156:    */       }
/* 157:    */     }
/* 158:    */     catch (XmlException e)
/* 159:    */     {
/* 160:    */       Map<String, XSLFSlide> shIdMap;
/* 161:164 */       throw new POIXMLException(e);
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected void commit()
/* 166:    */     throws IOException
/* 167:    */   {
/* 168:170 */     PackagePart part = getPackagePart();
/* 169:171 */     OutputStream out = part.getOutputStream();
/* 170:172 */     this._presentation.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/* 171:173 */     out.close();
/* 172:    */   }
/* 173:    */   
/* 174:    */   public List<PackagePart> getAllEmbedds()
/* 175:    */     throws OpenXML4JException
/* 176:    */   {
/* 177:181 */     return Collections.unmodifiableList(getPackage().getPartsByName(Pattern.compile("/ppt/embeddings/.*?")));
/* 178:    */   }
/* 179:    */   
/* 180:    */   public List<XSLFPictureData> getPictureData()
/* 181:    */   {
/* 182:188 */     if (this._pictures == null)
/* 183:    */     {
/* 184:189 */       List<PackagePart> mediaParts = getPackage().getPartsByName(Pattern.compile("/ppt/media/.*?"));
/* 185:190 */       this._pictures = new ArrayList(mediaParts.size());
/* 186:191 */       for (PackagePart part : mediaParts)
/* 187:    */       {
/* 188:192 */         XSLFPictureData pd = new XSLFPictureData(part);
/* 189:193 */         pd.setIndex(this._pictures.size());
/* 190:194 */         this._pictures.add(pd);
/* 191:    */       }
/* 192:    */     }
/* 193:197 */     return Collections.unmodifiableList(this._pictures);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public XSLFSlide createSlide(XSLFSlideLayout layout)
/* 197:    */   {
/* 198:207 */     int slideNumber = 256;int cnt = 1;
/* 199:    */     CTSlideIdList slideList;
/* 200:    */     CTSlideIdList slideList;
/* 201:209 */     if (!this._presentation.isSetSldIdLst())
/* 202:    */     {
/* 203:210 */       slideList = this._presentation.addNewSldIdLst();
/* 204:    */     }
/* 205:    */     else
/* 206:    */     {
/* 207:212 */       slideList = this._presentation.getSldIdLst();
/* 208:213 */       for (CTSlideIdListEntry slideId : slideList.getSldIdArray())
/* 209:    */       {
/* 210:214 */         slideNumber = (int)Math.max(slideId.getId() + 1L, slideNumber);
/* 211:215 */         cnt++;
/* 212:    */       }
/* 213:    */       for (;;)
/* 214:    */       {
/* 215:221 */         String slideName = XSLFRelation.SLIDE.getFileName(cnt);
/* 216:222 */         boolean found = false;
/* 217:223 */         for (POIXMLDocumentPart relation : getRelations()) {
/* 218:224 */           if ((relation.getPackagePart() != null) && (slideName.equals(relation.getPackagePart().getPartName().getName())))
/* 219:    */           {
/* 220:227 */             found = true;
/* 221:228 */             break;
/* 222:    */           }
/* 223:    */         }
/* 224:232 */         if ((!found) && (getPackage().getPartsByName(Pattern.compile(Pattern.quote(slideName))).size() > 0)) {
/* 225:235 */           found = true;
/* 226:    */         }
/* 227:238 */         if (!found) {
/* 228:    */           break;
/* 229:    */         }
/* 230:241 */         cnt++;
/* 231:    */       }
/* 232:    */     }
/* 233:245 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSLFRelation.SLIDE, XSLFFactory.getInstance(), cnt, false);
/* 234:    */     
/* 235:247 */     XSLFSlide slide = (XSLFSlide)rp.getDocumentPart();
/* 236:    */     
/* 237:249 */     CTSlideIdListEntry slideId = slideList.addNewSldId();
/* 238:250 */     slideId.setId(slideNumber);
/* 239:251 */     slideId.setId2(rp.getRelationship().getId());
/* 240:    */     
/* 241:253 */     layout.copyLayout(slide);
/* 242:254 */     slide.addRelation(null, XSLFRelation.SLIDE_LAYOUT, layout);
/* 243:    */     
/* 244:256 */     this._slides.add(slide);
/* 245:257 */     return slide;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public XSLFSlide createSlide()
/* 249:    */   {
/* 250:265 */     XSLFSlideMaster sm = (XSLFSlideMaster)this._masters.get(0);
/* 251:266 */     XSLFSlideLayout layout = sm.getLayout(SlideLayout.BLANK);
/* 252:267 */     if (layout == null)
/* 253:    */     {
/* 254:268 */       LOG.log(5, new Object[] { "Blank layout was not found - defaulting to first slide layout in master" });
/* 255:269 */       XSLFSlideLayout[] sl = sm.getSlideLayouts();
/* 256:270 */       if (sl.length == 0) {
/* 257:271 */         throw new POIXMLException("SlideMaster must contain a SlideLayout.");
/* 258:    */       }
/* 259:273 */       layout = sl[0];
/* 260:    */     }
/* 261:276 */     return createSlide(layout);
/* 262:    */   }
/* 263:    */   
/* 264:    */   public XSLFNotes getNotesSlide(XSLFSlide slide)
/* 265:    */   {
/* 266:284 */     XSLFNotes notesSlide = slide.getNotes();
/* 267:285 */     if (notesSlide == null) {
/* 268:286 */       notesSlide = createNotesSlide(slide);
/* 269:    */     }
/* 270:289 */     return notesSlide;
/* 271:    */   }
/* 272:    */   
/* 273:    */   private XSLFNotes createNotesSlide(XSLFSlide slide)
/* 274:    */   {
/* 275:297 */     if (this._notesMaster == null) {
/* 276:298 */       createNotesMaster();
/* 277:    */     }
/* 278:301 */     Integer slideIndex = XSLFRelation.SLIDE.getFileNameIndex(slide);
/* 279:    */     Iterator i$;
/* 280:    */     for (;;)
/* 281:    */     {
/* 282:306 */       String slideName = XSLFRelation.NOTES.getFileName(slideIndex.intValue());
/* 283:307 */       boolean found = false;
/* 284:308 */       for (i$ = getRelations().iterator(); i$.hasNext();)
/* 285:    */       {
/* 286:308 */         relation = (POIXMLDocumentPart)i$.next();
/* 287:309 */         if ((relation.getPackagePart() != null) && (slideName.equals(relation.getPackagePart().getPartName().getName())))
/* 288:    */         {
/* 289:312 */           found = true;
/* 290:313 */           break;
/* 291:    */         }
/* 292:    */       }
/* 293:317 */       if ((!found) && (getPackage().getPartsByName(Pattern.compile(Pattern.quote(slideName))).size() > 0)) {
/* 294:320 */         found = true;
/* 295:    */       }
/* 296:323 */       if (!found) {
/* 297:    */         break;
/* 298:    */       }
/* 299:326 */       i$ = slideIndex;POIXMLDocumentPart relation = slideIndex = Integer.valueOf(slideIndex.intValue() + 1);
/* 300:    */     }
/* 301:330 */     XSLFNotes notesSlide = (XSLFNotes)createRelationship(XSLFRelation.NOTES, XSLFFactory.getInstance(), slideIndex.intValue());
/* 302:    */     
/* 303:    */ 
/* 304:333 */     slide.addRelation(null, XSLFRelation.NOTES, notesSlide);
/* 305:334 */     notesSlide.addRelation(null, XSLFRelation.NOTES_MASTER, this._notesMaster);
/* 306:335 */     notesSlide.addRelation(null, XSLFRelation.SLIDE, slide);
/* 307:    */     
/* 308:337 */     notesSlide.importContent(this._notesMaster);
/* 309:    */     
/* 310:339 */     return notesSlide;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public void createNotesMaster()
/* 314:    */   {
/* 315:346 */     POIXMLDocumentPart.RelationPart rp = createRelationship(XSLFRelation.NOTES_MASTER, XSLFFactory.getInstance(), 1, false);
/* 316:    */     
/* 317:348 */     this._notesMaster = ((XSLFNotesMaster)rp.getDocumentPart());
/* 318:    */     
/* 319:350 */     CTNotesMasterIdList notesMasterIdList = this._presentation.addNewNotesMasterIdLst();
/* 320:351 */     CTNotesMasterIdListEntry notesMasterId = notesMasterIdList.addNewNotesMasterId();
/* 321:352 */     notesMasterId.setId(rp.getRelationship().getId());
/* 322:    */     
/* 323:354 */     Integer themeIndex = Integer.valueOf(1);
/* 324:    */     
/* 325:356 */     List<Integer> themeIndexList = new ArrayList();
/* 326:357 */     for (POIXMLDocumentPart p : getRelations()) {
/* 327:358 */       if ((p instanceof XSLFTheme)) {
/* 328:359 */         themeIndexList.add(XSLFRelation.THEME.getFileNameIndex(p));
/* 329:    */       }
/* 330:    */     }
/* 331:363 */     if (!themeIndexList.isEmpty())
/* 332:    */     {
/* 333:364 */       Boolean found = Boolean.valueOf(false);
/* 334:    */       Integer localInteger1;
/* 335:    */       Integer localInteger2;
/* 336:365 */       for (Integer i = Integer.valueOf(1); i.intValue() <= themeIndexList.size(); localInteger2 = i = Integer.valueOf(i.intValue() + 1))
/* 337:    */       {
/* 338:366 */         if (!themeIndexList.contains(i))
/* 339:    */         {
/* 340:367 */           found = Boolean.valueOf(true);
/* 341:368 */           themeIndex = i;
/* 342:    */         }
/* 343:365 */         localInteger1 = i;
/* 344:    */       }
/* 345:371 */       if (!found.booleanValue()) {
/* 346:372 */         themeIndex = Integer.valueOf(themeIndexList.size() + 1);
/* 347:    */       }
/* 348:    */     }
/* 349:376 */     XSLFTheme theme = (XSLFTheme)createRelationship(XSLFRelation.THEME, XSLFFactory.getInstance(), themeIndex.intValue());
/* 350:    */     
/* 351:378 */     theme.importTheme(((XSLFSlide)getSlides().get(0)).getTheme());
/* 352:    */     
/* 353:380 */     this._notesMaster.addRelation(null, XSLFRelation.THEME, theme);
/* 354:    */   }
/* 355:    */   
/* 356:    */   public XSLFNotesMaster getNotesMaster()
/* 357:    */   {
/* 358:388 */     return this._notesMaster;
/* 359:    */   }
/* 360:    */   
/* 361:    */   public List<XSLFSlideMaster> getSlideMasters()
/* 362:    */   {
/* 363:393 */     return this._masters;
/* 364:    */   }
/* 365:    */   
/* 366:    */   public List<XSLFSlide> getSlides()
/* 367:    */   {
/* 368:401 */     return this._slides;
/* 369:    */   }
/* 370:    */   
/* 371:    */   public XSLFCommentAuthors getCommentAuthors()
/* 372:    */   {
/* 373:409 */     return this._commentAuthors;
/* 374:    */   }
/* 375:    */   
/* 376:    */   public void setSlideOrder(XSLFSlide slide, int newIndex)
/* 377:    */   {
/* 378:417 */     int oldIndex = this._slides.indexOf(slide);
/* 379:418 */     if (oldIndex == -1) {
/* 380:419 */       throw new IllegalArgumentException("Slide not found");
/* 381:    */     }
/* 382:421 */     if (oldIndex == newIndex) {
/* 383:422 */       return;
/* 384:    */     }
/* 385:426 */     this._slides.add(newIndex, this._slides.remove(oldIndex));
/* 386:    */     
/* 387:    */ 
/* 388:429 */     CTSlideIdList sldIdLst = this._presentation.getSldIdLst();
/* 389:430 */     CTSlideIdListEntry[] entries = sldIdLst.getSldIdArray();
/* 390:431 */     CTSlideIdListEntry oldEntry = entries[oldIndex];
/* 391:432 */     if (oldIndex < newIndex) {
/* 392:433 */       System.arraycopy(entries, oldIndex + 1, entries, oldIndex, newIndex - oldIndex);
/* 393:    */     } else {
/* 394:435 */       System.arraycopy(entries, newIndex, entries, newIndex + 1, oldIndex - newIndex);
/* 395:    */     }
/* 396:437 */     entries[newIndex] = oldEntry;
/* 397:438 */     sldIdLst.setSldIdArray(entries);
/* 398:    */   }
/* 399:    */   
/* 400:    */   public XSLFSlide removeSlide(int index)
/* 401:    */   {
/* 402:442 */     XSLFSlide slide = (XSLFSlide)this._slides.remove(index);
/* 403:443 */     removeRelation(slide);
/* 404:444 */     this._presentation.getSldIdLst().removeSldId(index);
/* 405:445 */     return slide;
/* 406:    */   }
/* 407:    */   
/* 408:    */   public Dimension getPageSize()
/* 409:    */   {
/* 410:450 */     CTSlideSize sz = this._presentation.getSldSz();
/* 411:451 */     int cx = sz.getCx();
/* 412:452 */     int cy = sz.getCy();
/* 413:453 */     return new Dimension((int)Units.toPoints(cx), (int)Units.toPoints(cy));
/* 414:    */   }
/* 415:    */   
/* 416:    */   public void setPageSize(Dimension pgSize)
/* 417:    */   {
/* 418:458 */     CTSlideSize sz = CTSlideSize.Factory.newInstance();
/* 419:459 */     sz.setCx(Units.toEMU(pgSize.getWidth()));
/* 420:460 */     sz.setCy(Units.toEMU(pgSize.getHeight()));
/* 421:461 */     this._presentation.setSldSz(sz);
/* 422:    */   }
/* 423:    */   
/* 424:    */   @Internal
/* 425:    */   public CTPresentation getCTPresentation()
/* 426:    */   {
/* 427:467 */     return this._presentation;
/* 428:    */   }
/* 429:    */   
/* 430:    */   public XSLFPictureData addPicture(byte[] pictureData, PictureData.PictureType format)
/* 431:    */   {
/* 432:480 */     XSLFPictureData img = findPictureData(pictureData);
/* 433:482 */     if (img != null) {
/* 434:483 */       return img;
/* 435:    */     }
/* 436:486 */     int imageNumber = this._pictures.size();
/* 437:487 */     XSLFRelation relType = XSLFPictureData.getRelationForType(format);
/* 438:488 */     if (relType == null) {
/* 439:489 */       throw new IllegalArgumentException("Picture type " + format + " is not supported.");
/* 440:    */     }
/* 441:491 */     img = (XSLFPictureData)createRelationship(relType, XSLFFactory.getInstance(), imageNumber + 1, true).getDocumentPart();
/* 442:492 */     img.setIndex(imageNumber);
/* 443:493 */     this._pictures.add(img);
/* 444:    */     try
/* 445:    */     {
/* 446:495 */       OutputStream out = img.getPackagePart().getOutputStream();
/* 447:496 */       out.write(pictureData);
/* 448:497 */       out.close();
/* 449:    */     }
/* 450:    */     catch (IOException e)
/* 451:    */     {
/* 452:499 */       throw new POIXMLException(e);
/* 453:    */     }
/* 454:502 */     return img;
/* 455:    */   }
/* 456:    */   
/* 457:    */   public XSLFPictureData addPicture(InputStream is, PictureData.PictureType format)
/* 458:    */     throws IOException
/* 459:    */   {
/* 460:518 */     return addPicture(IOUtils.toByteArray(is), format);
/* 461:    */   }
/* 462:    */   
/* 463:    */   public XSLFPictureData addPicture(File pict, PictureData.PictureType format)
/* 464:    */     throws IOException
/* 465:    */   {
/* 466:534 */     int length = (int)pict.length();
/* 467:535 */     byte[] data = new byte[length];
/* 468:536 */     FileInputStream is = new FileInputStream(pict);
/* 469:    */     try
/* 470:    */     {
/* 471:538 */       IOUtils.readFully(is, data);
/* 472:    */     }
/* 473:    */     finally
/* 474:    */     {
/* 475:540 */       is.close();
/* 476:    */     }
/* 477:542 */     return addPicture(data, format);
/* 478:    */   }
/* 479:    */   
/* 480:    */   public XSLFPictureData findPictureData(byte[] pictureData)
/* 481:    */   {
/* 482:555 */     long checksum = IOUtils.calculateChecksum(pictureData);
/* 483:556 */     byte[] cs = new byte[8];
/* 484:557 */     LittleEndian.putLong(cs, 0, checksum);
/* 485:559 */     for (XSLFPictureData pic : getPictureData()) {
/* 486:560 */       if (Arrays.equals(pic.getChecksum(), cs)) {
/* 487:561 */         return pic;
/* 488:    */       }
/* 489:    */     }
/* 490:564 */     return null;
/* 491:    */   }
/* 492:    */   
/* 493:    */   public XSLFSlideLayout findLayout(String name)
/* 494:    */   {
/* 495:575 */     for (XSLFSlideMaster master : getSlideMasters())
/* 496:    */     {
/* 497:576 */       XSLFSlideLayout layout = master.getLayout(name);
/* 498:577 */       if (layout != null) {
/* 499:578 */         return layout;
/* 500:    */       }
/* 501:    */     }
/* 502:581 */     return null;
/* 503:    */   }
/* 504:    */   
/* 505:    */   public XSLFTableStyles getTableStyles()
/* 506:    */   {
/* 507:586 */     return this._tableStyles;
/* 508:    */   }
/* 509:    */   
/* 510:    */   CTTextParagraphProperties getDefaultParagraphStyle(int level)
/* 511:    */   {
/* 512:590 */     XmlObject[] o = this._presentation.selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//p:defaultTextStyle/a:lvl" + (level + 1) + "pPr");
/* 513:594 */     if (o.length == 1) {
/* 514:595 */       return (CTTextParagraphProperties)o[0];
/* 515:    */     }
/* 516:597 */     return null;
/* 517:    */   }
/* 518:    */   
/* 519:    */   public MasterSheet<XSLFShape, XSLFTextParagraph> createMasterSheet()
/* 520:    */     throws IOException
/* 521:    */   {
/* 522:603 */     throw new UnsupportedOperationException();
/* 523:    */   }
/* 524:    */   
/* 525:    */   public Resources getResources()
/* 526:    */   {
/* 527:609 */     throw new UnsupportedOperationException();
/* 528:    */   }
/* 529:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XMLSlideShow
 * JD-Core Version:    0.7.0.1
 */