/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  12:    */ import org.apache.poi.POIXMLException;
/*  13:    */ import org.apache.poi.POIXMLRelation;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  17:    */ import org.apache.poi.util.IOUtils;
/*  18:    */ import org.apache.poi.util.Internal;
/*  19:    */ import org.apache.xmlbeans.SchemaType;
/*  20:    */ import org.apache.xmlbeans.XmlCursor;
/*  21:    */ import org.apache.xmlbeans.XmlObject;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr.Factory;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  25:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*  26:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  27:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*  28:    */ 
/*  29:    */ public abstract class XWPFHeaderFooter
/*  30:    */   extends POIXMLDocumentPart
/*  31:    */   implements IBody
/*  32:    */ {
/*  33: 45 */   List<XWPFParagraph> paragraphs = new ArrayList();
/*  34: 46 */   List<XWPFTable> tables = new ArrayList();
/*  35: 47 */   List<XWPFPictureData> pictures = new ArrayList();
/*  36: 48 */   List<IBodyElement> bodyElements = new ArrayList();
/*  37:    */   CTHdrFtr headerFooter;
/*  38:    */   XWPFDocument document;
/*  39:    */   
/*  40:    */   XWPFHeaderFooter(XWPFDocument doc, CTHdrFtr hdrFtr)
/*  41:    */   {
/*  42: 54 */     if (doc == null) {
/*  43: 55 */       throw new NullPointerException();
/*  44:    */     }
/*  45: 58 */     this.document = doc;
/*  46: 59 */     this.headerFooter = hdrFtr;
/*  47: 60 */     readHdrFtr();
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected XWPFHeaderFooter()
/*  51:    */   {
/*  52: 64 */     this.headerFooter = CTHdrFtr.Factory.newInstance();
/*  53: 65 */     readHdrFtr();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public XWPFHeaderFooter(POIXMLDocumentPart parent, PackagePart part)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59: 72 */     super(parent, part);
/*  60: 73 */     this.document = ((XWPFDocument)getParent());
/*  61: 75 */     if (this.document == null) {
/*  62: 76 */       throw new NullPointerException();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected void onDocumentRead()
/*  67:    */     throws IOException
/*  68:    */   {
/*  69: 82 */     for (POIXMLDocumentPart poixmlDocumentPart : getRelations()) {
/*  70: 83 */       if ((poixmlDocumentPart instanceof XWPFPictureData))
/*  71:    */       {
/*  72: 84 */         XWPFPictureData xwpfPicData = (XWPFPictureData)poixmlDocumentPart;
/*  73: 85 */         this.pictures.add(xwpfPicData);
/*  74: 86 */         this.document.registerPackagePictureData(xwpfPicData);
/*  75:    */       }
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   @Internal
/*  80:    */   public CTHdrFtr _getHdrFtr()
/*  81:    */   {
/*  82: 93 */     return this.headerFooter;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public List<IBodyElement> getBodyElements()
/*  86:    */   {
/*  87: 97 */     return Collections.unmodifiableList(this.bodyElements);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public List<XWPFParagraph> getParagraphs()
/*  91:    */   {
/*  92:108 */     return Collections.unmodifiableList(this.paragraphs);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public List<XWPFTable> getTables()
/*  96:    */     throws ArrayIndexOutOfBoundsException
/*  97:    */   {
/*  98:121 */     return Collections.unmodifiableList(this.tables);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getText()
/* 102:    */   {
/* 103:130 */     StringBuffer t = new StringBuffer();
/* 104:132 */     for (int i = 0; i < this.paragraphs.size(); i++) {
/* 105:133 */       if (!((XWPFParagraph)this.paragraphs.get(i)).isEmpty())
/* 106:    */       {
/* 107:134 */         String text = ((XWPFParagraph)this.paragraphs.get(i)).getText();
/* 108:135 */         if ((text != null) && (text.length() > 0))
/* 109:    */         {
/* 110:136 */           t.append(text);
/* 111:137 */           t.append('\n');
/* 112:    */         }
/* 113:    */       }
/* 114:    */     }
/* 115:142 */     for (int i = 0; i < this.tables.size(); i++)
/* 116:    */     {
/* 117:143 */       String text = ((XWPFTable)this.tables.get(i)).getText();
/* 118:144 */       if ((text != null) && (text.length() > 0))
/* 119:    */       {
/* 120:145 */         t.append(text);
/* 121:146 */         t.append('\n');
/* 122:    */       }
/* 123:    */     }
/* 124:150 */     for (IBodyElement bodyElement : getBodyElements()) {
/* 125:151 */       if ((bodyElement instanceof XWPFSDT)) {
/* 126:152 */         t.append(((XWPFSDT)bodyElement).getContent().getText() + '\n');
/* 127:    */       }
/* 128:    */     }
/* 129:155 */     return t.toString();
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void setHeaderFooter(CTHdrFtr headerFooter)
/* 133:    */   {
/* 134:162 */     this.headerFooter = headerFooter;
/* 135:163 */     readHdrFtr();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public XWPFTable getTable(CTTbl ctTable)
/* 139:    */   {
/* 140:174 */     for (XWPFTable table : this.tables)
/* 141:    */     {
/* 142:175 */       if (table == null) {
/* 143:176 */         return null;
/* 144:    */       }
/* 145:177 */       if (table.getCTTbl().equals(ctTable)) {
/* 146:178 */         return table;
/* 147:    */       }
/* 148:    */     }
/* 149:180 */     return null;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public XWPFParagraph getParagraph(CTP p)
/* 153:    */   {
/* 154:193 */     for (XWPFParagraph paragraph : this.paragraphs) {
/* 155:194 */       if (paragraph.getCTP().equals(p)) {
/* 156:195 */         return paragraph;
/* 157:    */       }
/* 158:    */     }
/* 159:197 */     return null;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public XWPFParagraph getParagraphArray(int pos)
/* 163:    */   {
/* 164:206 */     if ((pos >= 0) && (pos < this.paragraphs.size())) {
/* 165:207 */       return (XWPFParagraph)this.paragraphs.get(pos);
/* 166:    */     }
/* 167:209 */     return null;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public List<XWPFParagraph> getListParagraph()
/* 171:    */   {
/* 172:218 */     return this.paragraphs;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public List<XWPFPictureData> getAllPictures()
/* 176:    */   {
/* 177:222 */     return Collections.unmodifiableList(this.pictures);
/* 178:    */   }
/* 179:    */   
/* 180:    */   public List<XWPFPictureData> getAllPackagePictures()
/* 181:    */   {
/* 182:231 */     return this.document.getAllPackagePictures();
/* 183:    */   }
/* 184:    */   
/* 185:    */   public String addPictureData(byte[] pictureData, int format)
/* 186:    */     throws InvalidFormatException
/* 187:    */   {
/* 188:244 */     XWPFPictureData xwpfPicData = this.document.findPackagePictureData(pictureData, format);
/* 189:245 */     POIXMLRelation relDesc = XWPFPictureData.RELATIONS[format];
/* 190:247 */     if (xwpfPicData == null)
/* 191:    */     {
/* 192:249 */       int idx = this.document.getNextPicNameNumber(format);
/* 193:250 */       xwpfPicData = (XWPFPictureData)createRelationship(relDesc, XWPFFactory.getInstance(), idx);
/* 194:    */       
/* 195:252 */       PackagePart picDataPart = xwpfPicData.getPackagePart();
/* 196:253 */       OutputStream out = null;
/* 197:    */       try
/* 198:    */       {
/* 199:255 */         out = picDataPart.getOutputStream();
/* 200:256 */         out.write(pictureData);
/* 201:    */         try
/* 202:    */         {
/* 203:261 */           if (out != null) {
/* 204:261 */             out.close();
/* 205:    */           }
/* 206:    */         }
/* 207:    */         catch (IOException e) {}
/* 208:267 */         this.document.registerPackagePictureData(xwpfPicData);
/* 209:    */       }
/* 210:    */       catch (IOException e)
/* 211:    */       {
/* 212:258 */         throw new POIXMLException(e);
/* 213:    */       }
/* 214:    */       finally
/* 215:    */       {
/* 216:    */         try
/* 217:    */         {
/* 218:261 */           if (out != null) {
/* 219:261 */             out.close();
/* 220:    */           }
/* 221:    */         }
/* 222:    */         catch (IOException e) {}
/* 223:    */       }
/* 224:268 */       this.pictures.add(xwpfPicData);
/* 225:269 */       return getRelationId(xwpfPicData);
/* 226:    */     }
/* 227:270 */     if (!getRelations().contains(xwpfPicData))
/* 228:    */     {
/* 229:277 */       POIXMLDocumentPart.RelationPart rp = addRelation(null, XWPFRelation.IMAGES, xwpfPicData);
/* 230:278 */       this.pictures.add(xwpfPicData);
/* 231:279 */       return rp.getRelationship().getId();
/* 232:    */     }
/* 233:282 */     return getRelationId(xwpfPicData);
/* 234:    */   }
/* 235:    */   
/* 236:    */   public String addPictureData(InputStream is, int format)
/* 237:    */     throws InvalidFormatException, IOException
/* 238:    */   {
/* 239:296 */     byte[] data = IOUtils.toByteArray(is);
/* 240:297 */     return addPictureData(data, format);
/* 241:    */   }
/* 242:    */   
/* 243:    */   public XWPFPictureData getPictureDataByID(String blipID)
/* 244:    */   {
/* 245:308 */     POIXMLDocumentPart relatedPart = getRelationById(blipID);
/* 246:309 */     if ((relatedPart != null) && ((relatedPart instanceof XWPFPictureData))) {
/* 247:310 */       return (XWPFPictureData)relatedPart;
/* 248:    */     }
/* 249:312 */     return null;
/* 250:    */   }
/* 251:    */   
/* 252:    */   public XWPFParagraph createParagraph()
/* 253:    */   {
/* 254:321 */     XWPFParagraph paragraph = new XWPFParagraph(this.headerFooter.addNewP(), this);
/* 255:322 */     this.paragraphs.add(paragraph);
/* 256:323 */     this.bodyElements.add(paragraph);
/* 257:324 */     return paragraph;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public XWPFTable createTable(int rows, int cols)
/* 261:    */   {
/* 262:335 */     XWPFTable table = new XWPFTable(this.headerFooter.addNewTbl(), this, rows, cols);
/* 263:336 */     this.tables.add(table);
/* 264:337 */     this.bodyElements.add(table);
/* 265:338 */     return table;
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void removeParagraph(XWPFParagraph paragraph)
/* 269:    */   {
/* 270:347 */     if (this.paragraphs.contains(paragraph))
/* 271:    */     {
/* 272:348 */       CTP ctP = paragraph.getCTP();
/* 273:349 */       XmlCursor c = ctP.newCursor();
/* 274:350 */       c.removeXml();
/* 275:351 */       c.dispose();
/* 276:352 */       this.paragraphs.remove(paragraph);
/* 277:353 */       this.bodyElements.remove(paragraph);
/* 278:    */     }
/* 279:    */   }
/* 280:    */   
/* 281:    */   public void removeTable(XWPFTable table)
/* 282:    */   {
/* 283:363 */     if (this.tables.contains(table))
/* 284:    */     {
/* 285:364 */       CTTbl ctTbl = table.getCTTbl();
/* 286:365 */       XmlCursor c = ctTbl.newCursor();
/* 287:366 */       c.removeXml();
/* 288:367 */       c.dispose();
/* 289:368 */       this.tables.remove(table);
/* 290:369 */       this.bodyElements.remove(table);
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   public void clearHeaderFooter()
/* 295:    */   {
/* 296:377 */     XmlCursor c = this.headerFooter.newCursor();
/* 297:378 */     c.removeXmlContents();
/* 298:379 */     c.dispose();
/* 299:380 */     this.paragraphs.clear();
/* 300:381 */     this.tables.clear();
/* 301:382 */     this.bodyElements.clear();
/* 302:    */   }
/* 303:    */   
/* 304:    */   public XWPFParagraph insertNewParagraph(XmlCursor cursor)
/* 305:    */   {
/* 306:392 */     if (isCursorInHdrF(cursor))
/* 307:    */     {
/* 308:393 */       String uri = CTP.type.getName().getNamespaceURI();
/* 309:394 */       String localPart = "p";
/* 310:395 */       cursor.beginElement(localPart, uri);
/* 311:396 */       cursor.toParent();
/* 312:397 */       CTP p = (CTP)cursor.getObject();
/* 313:398 */       XWPFParagraph newP = new XWPFParagraph(p, this);
/* 314:399 */       XmlObject o = null;
/* 315:400 */       while ((!(o instanceof CTP)) && (cursor.toPrevSibling())) {
/* 316:401 */         o = cursor.getObject();
/* 317:    */       }
/* 318:403 */       if ((!(o instanceof CTP)) || ((CTP)o == p))
/* 319:    */       {
/* 320:404 */         this.paragraphs.add(0, newP);
/* 321:    */       }
/* 322:    */       else
/* 323:    */       {
/* 324:406 */         int pos = this.paragraphs.indexOf(getParagraph((CTP)o)) + 1;
/* 325:407 */         this.paragraphs.add(pos, newP);
/* 326:    */       }
/* 327:409 */       int i = 0;
/* 328:410 */       XmlCursor p2 = p.newCursor();
/* 329:411 */       cursor.toCursor(p2);
/* 330:412 */       p2.dispose();
/* 331:413 */       while (cursor.toPrevSibling())
/* 332:    */       {
/* 333:414 */         o = cursor.getObject();
/* 334:415 */         if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 335:416 */           i++;
/* 336:    */         }
/* 337:    */       }
/* 338:418 */       this.bodyElements.add(i, newP);
/* 339:419 */       p2 = p.newCursor();
/* 340:420 */       cursor.toCursor(p2);
/* 341:421 */       cursor.toEndToken();
/* 342:422 */       p2.dispose();
/* 343:423 */       return newP;
/* 344:    */     }
/* 345:425 */     return null;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public XWPFTable insertNewTbl(XmlCursor cursor)
/* 349:    */   {
/* 350:434 */     if (isCursorInHdrF(cursor))
/* 351:    */     {
/* 352:435 */       String uri = CTTbl.type.getName().getNamespaceURI();
/* 353:436 */       String localPart = "tbl";
/* 354:437 */       cursor.beginElement(localPart, uri);
/* 355:438 */       cursor.toParent();
/* 356:439 */       CTTbl t = (CTTbl)cursor.getObject();
/* 357:440 */       XWPFTable newT = new XWPFTable(t, this);
/* 358:441 */       cursor.removeXmlContents();
/* 359:442 */       XmlObject o = null;
/* 360:443 */       while ((!(o instanceof CTTbl)) && (cursor.toPrevSibling())) {
/* 361:444 */         o = cursor.getObject();
/* 362:    */       }
/* 363:446 */       if (!(o instanceof CTTbl))
/* 364:    */       {
/* 365:447 */         this.tables.add(0, newT);
/* 366:    */       }
/* 367:    */       else
/* 368:    */       {
/* 369:449 */         int pos = this.tables.indexOf(getTable((CTTbl)o)) + 1;
/* 370:450 */         this.tables.add(pos, newT);
/* 371:    */       }
/* 372:452 */       int i = 0;
/* 373:453 */       XmlCursor cursor2 = t.newCursor();
/* 374:454 */       while (cursor2.toPrevSibling())
/* 375:    */       {
/* 376:455 */         o = cursor2.getObject();
/* 377:456 */         if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 378:457 */           i++;
/* 379:    */         }
/* 380:    */       }
/* 381:460 */       cursor2.dispose();
/* 382:461 */       this.bodyElements.add(i, newT);
/* 383:462 */       cursor2 = t.newCursor();
/* 384:463 */       cursor.toCursor(cursor2);
/* 385:464 */       cursor.toEndToken();
/* 386:465 */       cursor2.dispose();
/* 387:466 */       return newT;
/* 388:    */     }
/* 389:468 */     return null;
/* 390:    */   }
/* 391:    */   
/* 392:    */   private boolean isCursorInHdrF(XmlCursor cursor)
/* 393:    */   {
/* 394:477 */     XmlCursor verify = cursor.newCursor();
/* 395:478 */     verify.toParent();
/* 396:479 */     boolean result = verify.getObject() == this.headerFooter;
/* 397:480 */     verify.dispose();
/* 398:481 */     return result;
/* 399:    */   }
/* 400:    */   
/* 401:    */   public POIXMLDocumentPart getOwner()
/* 402:    */   {
/* 403:486 */     return this;
/* 404:    */   }
/* 405:    */   
/* 406:    */   public XWPFTable getTableArray(int pos)
/* 407:    */   {
/* 408:495 */     if ((pos >= 0) && (pos < this.tables.size())) {
/* 409:496 */       return (XWPFTable)this.tables.get(pos);
/* 410:    */     }
/* 411:498 */     return null;
/* 412:    */   }
/* 413:    */   
/* 414:    */   public void insertTable(int pos, XWPFTable table)
/* 415:    */   {
/* 416:508 */     this.bodyElements.add(pos, table);
/* 417:509 */     int i = 0;
/* 418:510 */     for (CTTbl tbl : this.headerFooter.getTblArray())
/* 419:    */     {
/* 420:511 */       if (tbl == table.getCTTbl()) {
/* 421:    */         break;
/* 422:    */       }
/* 423:514 */       i++;
/* 424:    */     }
/* 425:516 */     this.tables.add(i, table);
/* 426:    */   }
/* 427:    */   
/* 428:    */   public void readHdrFtr()
/* 429:    */   {
/* 430:521 */     this.bodyElements = new ArrayList();
/* 431:522 */     this.paragraphs = new ArrayList();
/* 432:523 */     this.tables = new ArrayList();
/* 433:    */     
/* 434:    */ 
/* 435:526 */     XmlCursor cursor = this.headerFooter.newCursor();
/* 436:527 */     cursor.selectPath("./*");
/* 437:528 */     while (cursor.toNextSelection())
/* 438:    */     {
/* 439:529 */       XmlObject o = cursor.getObject();
/* 440:530 */       if ((o instanceof CTP))
/* 441:    */       {
/* 442:531 */         XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/* 443:532 */         this.paragraphs.add(p);
/* 444:533 */         this.bodyElements.add(p);
/* 445:    */       }
/* 446:535 */       if ((o instanceof CTTbl))
/* 447:    */       {
/* 448:536 */         XWPFTable t = new XWPFTable((CTTbl)o, this);
/* 449:537 */         this.tables.add(t);
/* 450:538 */         this.bodyElements.add(t);
/* 451:    */       }
/* 452:    */     }
/* 453:541 */     cursor.dispose();
/* 454:    */   }
/* 455:    */   
/* 456:    */   public XWPFTableCell getTableCell(CTTc cell)
/* 457:    */   {
/* 458:550 */     XmlCursor cursor = cell.newCursor();
/* 459:551 */     cursor.toParent();
/* 460:552 */     XmlObject o = cursor.getObject();
/* 461:553 */     if (!(o instanceof CTRow))
/* 462:    */     {
/* 463:554 */       cursor.dispose();
/* 464:555 */       return null;
/* 465:    */     }
/* 466:557 */     CTRow row = (CTRow)o;
/* 467:558 */     cursor.toParent();
/* 468:559 */     o = cursor.getObject();
/* 469:560 */     cursor.dispose();
/* 470:561 */     if (!(o instanceof CTTbl)) {
/* 471:562 */       return null;
/* 472:    */     }
/* 473:564 */     CTTbl tbl = (CTTbl)o;
/* 474:565 */     XWPFTable table = getTable(tbl);
/* 475:566 */     if (table == null) {
/* 476:567 */       return null;
/* 477:    */     }
/* 478:569 */     XWPFTableRow tableRow = table.getRow(row);
/* 479:570 */     return tableRow.getTableCell(cell);
/* 480:    */   }
/* 481:    */   
/* 482:    */   public XWPFDocument getXWPFDocument()
/* 483:    */   {
/* 484:574 */     if (this.document != null) {
/* 485:575 */       return this.document;
/* 486:    */     }
/* 487:577 */     return (XWPFDocument)getParent();
/* 488:    */   }
/* 489:    */   
/* 490:    */   public void setXWPFDocument(XWPFDocument doc)
/* 491:    */   {
/* 492:582 */     this.document = doc;
/* 493:    */   }
/* 494:    */   
/* 495:    */   public POIXMLDocumentPart getPart()
/* 496:    */   {
/* 497:591 */     return this;
/* 498:    */   }
/* 499:    */   
/* 500:    */   protected void prepareForCommit()
/* 501:    */   {
/* 502:597 */     if (this.bodyElements.size() == 0) {
/* 503:598 */       createParagraph();
/* 504:    */     }
/* 505:602 */     for (XWPFTable tbl : this.tables) {
/* 506:603 */       for (XWPFTableRow row : tbl.tableRows) {
/* 507:604 */         for (XWPFTableCell cell : row.getTableCells()) {
/* 508:605 */           if (cell.getBodyElements().size() == 0) {
/* 509:606 */             cell.addParagraph();
/* 510:    */           }
/* 511:    */         }
/* 512:    */       }
/* 513:    */     }
/* 514:611 */     super.prepareForCommit();
/* 515:    */   }
/* 516:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFHeaderFooter
 * JD-Core Version:    0.7.0.1
 */