/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.EnumMap;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.util.Internal;
/*  12:    */ import org.apache.xmlbeans.SchemaType;
/*  13:    */ import org.apache.xmlbeans.XmlCursor;
/*  14:    */ import org.apache.xmlbeans.XmlObject;
/*  15:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  16:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
/*  25:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
/*  26:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;
/*  27:    */ 
/*  28:    */ public class XWPFTableCell
/*  29:    */   implements IBody, ICell
/*  30:    */ {
/*  31: 52 */   private static EnumMap<XWPFVertAlign, STVerticalJc.Enum> alignMap = new EnumMap(XWPFVertAlign.class);
/*  32:    */   private static HashMap<Integer, XWPFVertAlign> stVertAlignTypeMap;
/*  33:    */   private final CTTc ctTc;
/*  34:    */   
/*  35:    */   static
/*  36:    */   {
/*  37: 53 */     alignMap.put(XWPFVertAlign.TOP, STVerticalJc.Enum.forInt(1));
/*  38: 54 */     alignMap.put(XWPFVertAlign.CENTER, STVerticalJc.Enum.forInt(2));
/*  39: 55 */     alignMap.put(XWPFVertAlign.BOTH, STVerticalJc.Enum.forInt(3));
/*  40: 56 */     alignMap.put(XWPFVertAlign.BOTTOM, STVerticalJc.Enum.forInt(4));
/*  41:    */     
/*  42: 58 */     stVertAlignTypeMap = new HashMap();
/*  43: 59 */     stVertAlignTypeMap.put(Integer.valueOf(1), XWPFVertAlign.TOP);
/*  44: 60 */     stVertAlignTypeMap.put(Integer.valueOf(2), XWPFVertAlign.CENTER);
/*  45: 61 */     stVertAlignTypeMap.put(Integer.valueOf(3), XWPFVertAlign.BOTH);
/*  46: 62 */     stVertAlignTypeMap.put(Integer.valueOf(4), XWPFVertAlign.BOTTOM);
/*  47:    */   }
/*  48:    */   
/*  49: 67 */   protected List<XWPFParagraph> paragraphs = null;
/*  50: 68 */   protected List<XWPFTable> tables = null;
/*  51: 69 */   protected List<IBodyElement> bodyElements = null;
/*  52:    */   protected IBody part;
/*  53: 72 */   private XWPFTableRow tableRow = null;
/*  54:    */   
/*  55:    */   public XWPFTableCell(CTTc cell, XWPFTableRow tableRow, IBody part)
/*  56:    */   {
/*  57: 78 */     this.ctTc = cell;
/*  58: 79 */     this.part = part;
/*  59: 80 */     this.tableRow = tableRow;
/*  60: 82 */     if (cell.sizeOfPArray() < 1) {
/*  61: 83 */       cell.addNewP();
/*  62:    */     }
/*  63: 84 */     this.bodyElements = new ArrayList();
/*  64: 85 */     this.paragraphs = new ArrayList();
/*  65: 86 */     this.tables = new ArrayList();
/*  66:    */     
/*  67: 88 */     XmlCursor cursor = this.ctTc.newCursor();
/*  68: 89 */     cursor.selectPath("./*");
/*  69: 90 */     while (cursor.toNextSelection())
/*  70:    */     {
/*  71: 91 */       XmlObject o = cursor.getObject();
/*  72: 92 */       if ((o instanceof CTP))
/*  73:    */       {
/*  74: 93 */         XWPFParagraph p = new XWPFParagraph((CTP)o, this);
/*  75: 94 */         this.paragraphs.add(p);
/*  76: 95 */         this.bodyElements.add(p);
/*  77:    */       }
/*  78: 97 */       if ((o instanceof CTTbl))
/*  79:    */       {
/*  80: 98 */         XWPFTable t = new XWPFTable((CTTbl)o, this);
/*  81: 99 */         this.tables.add(t);
/*  82:100 */         this.bodyElements.add(t);
/*  83:    */       }
/*  84:102 */       if ((o instanceof CTSdtBlock))
/*  85:    */       {
/*  86:103 */         XWPFSDT c = new XWPFSDT((CTSdtBlock)o, this);
/*  87:104 */         this.bodyElements.add(c);
/*  88:    */       }
/*  89:106 */       if ((o instanceof CTSdtRun))
/*  90:    */       {
/*  91:107 */         XWPFSDT c = new XWPFSDT((CTSdtRun)o, this);
/*  92:108 */         System.out.println(c.getContent().getText());
/*  93:109 */         this.bodyElements.add(c);
/*  94:    */       }
/*  95:    */     }
/*  96:112 */     cursor.dispose();
/*  97:    */   }
/*  98:    */   
/*  99:    */   @Internal
/* 100:    */   public CTTc getCTTc()
/* 101:    */   {
/* 102:117 */     return this.ctTc;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public List<IBodyElement> getBodyElements()
/* 106:    */   {
/* 107:126 */     return Collections.unmodifiableList(this.bodyElements);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setParagraph(XWPFParagraph p)
/* 111:    */   {
/* 112:130 */     if (this.ctTc.sizeOfPArray() == 0) {
/* 113:131 */       this.ctTc.addNewP();
/* 114:    */     }
/* 115:133 */     this.ctTc.setPArray(0, p.getCTP());
/* 116:    */   }
/* 117:    */   
/* 118:    */   public List<XWPFParagraph> getParagraphs()
/* 119:    */   {
/* 120:140 */     return this.paragraphs;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public XWPFParagraph addParagraph()
/* 124:    */   {
/* 125:149 */     XWPFParagraph p = new XWPFParagraph(this.ctTc.addNewP(), this);
/* 126:150 */     addParagraph(p);
/* 127:151 */     return p;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void addParagraph(XWPFParagraph p)
/* 131:    */   {
/* 132:160 */     this.paragraphs.add(p);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void removeParagraph(int pos)
/* 136:    */   {
/* 137:169 */     this.paragraphs.remove(pos);
/* 138:170 */     this.ctTc.removeP(pos);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public XWPFParagraph getParagraph(CTP p)
/* 142:    */   {
/* 143:183 */     for (XWPFParagraph paragraph : this.paragraphs) {
/* 144:184 */       if (p.equals(paragraph.getCTP())) {
/* 145:185 */         return paragraph;
/* 146:    */       }
/* 147:    */     }
/* 148:188 */     return null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public XWPFTableRow getTableRow()
/* 152:    */   {
/* 153:192 */     return this.tableRow;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public String getColor()
/* 157:    */   {
/* 158:201 */     String color = null;
/* 159:202 */     CTTcPr tcpr = this.ctTc.getTcPr();
/* 160:203 */     if (tcpr != null)
/* 161:    */     {
/* 162:204 */       CTShd ctshd = tcpr.getShd();
/* 163:205 */       if (ctshd != null) {
/* 164:206 */         color = ctshd.xgetFill().getStringValue();
/* 165:    */       }
/* 166:    */     }
/* 167:209 */     return color;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public void setColor(String rgbStr)
/* 171:    */   {
/* 172:219 */     CTTcPr tcpr = this.ctTc.isSetTcPr() ? this.ctTc.getTcPr() : this.ctTc.addNewTcPr();
/* 173:220 */     CTShd ctshd = tcpr.isSetShd() ? tcpr.getShd() : tcpr.addNewShd();
/* 174:221 */     ctshd.setColor("auto");
/* 175:222 */     ctshd.setVal(STShd.CLEAR);
/* 176:223 */     ctshd.setFill(rgbStr);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public XWPFVertAlign getVerticalAlignment()
/* 180:    */   {
/* 181:233 */     XWPFVertAlign vAlign = null;
/* 182:234 */     CTTcPr tcpr = this.ctTc.getTcPr();
/* 183:235 */     if (tcpr != null)
/* 184:    */     {
/* 185:236 */       CTVerticalJc va = tcpr.getVAlign();
/* 186:237 */       if (va != null) {
/* 187:238 */         vAlign = (XWPFVertAlign)stVertAlignTypeMap.get(Integer.valueOf(va.getVal().intValue()));
/* 188:    */       }
/* 189:    */     }
/* 190:241 */     return vAlign;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setVerticalAlignment(XWPFVertAlign vAlign)
/* 194:    */   {
/* 195:250 */     CTTcPr tcpr = this.ctTc.isSetTcPr() ? this.ctTc.getTcPr() : this.ctTc.addNewTcPr();
/* 196:251 */     CTVerticalJc va = tcpr.addNewVAlign();
/* 197:252 */     va.setVal((STVerticalJc.Enum)alignMap.get(vAlign));
/* 198:    */   }
/* 199:    */   
/* 200:    */   public XWPFParagraph insertNewParagraph(XmlCursor cursor)
/* 201:    */   {
/* 202:262 */     if (!isCursorInTableCell(cursor)) {
/* 203:263 */       return null;
/* 204:    */     }
/* 205:266 */     String uri = CTP.type.getName().getNamespaceURI();
/* 206:267 */     String localPart = "p";
/* 207:268 */     cursor.beginElement(localPart, uri);
/* 208:269 */     cursor.toParent();
/* 209:270 */     CTP p = (CTP)cursor.getObject();
/* 210:271 */     XWPFParagraph newP = new XWPFParagraph(p, this);
/* 211:272 */     XmlObject o = null;
/* 212:273 */     while ((!(o instanceof CTP)) && (cursor.toPrevSibling())) {
/* 213:274 */       o = cursor.getObject();
/* 214:    */     }
/* 215:276 */     if ((!(o instanceof CTP)) || ((CTP)o == p))
/* 216:    */     {
/* 217:277 */       this.paragraphs.add(0, newP);
/* 218:    */     }
/* 219:    */     else
/* 220:    */     {
/* 221:279 */       int pos = this.paragraphs.indexOf(getParagraph((CTP)o)) + 1;
/* 222:280 */       this.paragraphs.add(pos, newP);
/* 223:    */     }
/* 224:282 */     int i = 0;
/* 225:283 */     XmlCursor p2 = p.newCursor();
/* 226:284 */     cursor.toCursor(p2);
/* 227:285 */     p2.dispose();
/* 228:286 */     while (cursor.toPrevSibling())
/* 229:    */     {
/* 230:287 */       o = cursor.getObject();
/* 231:288 */       if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 232:289 */         i++;
/* 233:    */       }
/* 234:    */     }
/* 235:291 */     this.bodyElements.add(i, newP);
/* 236:292 */     p2 = p.newCursor();
/* 237:293 */     cursor.toCursor(p2);
/* 238:294 */     p2.dispose();
/* 239:295 */     cursor.toEndToken();
/* 240:296 */     return newP;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public XWPFTable insertNewTbl(XmlCursor cursor)
/* 244:    */   {
/* 245:300 */     if (isCursorInTableCell(cursor))
/* 246:    */     {
/* 247:301 */       String uri = CTTbl.type.getName().getNamespaceURI();
/* 248:302 */       String localPart = "tbl";
/* 249:303 */       cursor.beginElement(localPart, uri);
/* 250:304 */       cursor.toParent();
/* 251:305 */       CTTbl t = (CTTbl)cursor.getObject();
/* 252:306 */       XWPFTable newT = new XWPFTable(t, this);
/* 253:307 */       cursor.removeXmlContents();
/* 254:308 */       XmlObject o = null;
/* 255:309 */       while ((!(o instanceof CTTbl)) && (cursor.toPrevSibling())) {
/* 256:310 */         o = cursor.getObject();
/* 257:    */       }
/* 258:312 */       if (!(o instanceof CTTbl))
/* 259:    */       {
/* 260:313 */         this.tables.add(0, newT);
/* 261:    */       }
/* 262:    */       else
/* 263:    */       {
/* 264:315 */         int pos = this.tables.indexOf(getTable((CTTbl)o)) + 1;
/* 265:316 */         this.tables.add(pos, newT);
/* 266:    */       }
/* 267:318 */       int i = 0;
/* 268:319 */       XmlCursor cursor2 = t.newCursor();
/* 269:320 */       while (cursor2.toPrevSibling())
/* 270:    */       {
/* 271:321 */         o = cursor2.getObject();
/* 272:322 */         if (((o instanceof CTP)) || ((o instanceof CTTbl))) {
/* 273:323 */           i++;
/* 274:    */         }
/* 275:    */       }
/* 276:325 */       cursor2.dispose();
/* 277:326 */       this.bodyElements.add(i, newT);
/* 278:327 */       cursor2 = t.newCursor();
/* 279:328 */       cursor.toCursor(cursor2);
/* 280:329 */       cursor.toEndToken();
/* 281:330 */       cursor2.dispose();
/* 282:331 */       return newT;
/* 283:    */     }
/* 284:333 */     return null;
/* 285:    */   }
/* 286:    */   
/* 287:    */   private boolean isCursorInTableCell(XmlCursor cursor)
/* 288:    */   {
/* 289:340 */     XmlCursor verify = cursor.newCursor();
/* 290:341 */     verify.toParent();
/* 291:342 */     boolean result = verify.getObject() == this.ctTc;
/* 292:343 */     verify.dispose();
/* 293:344 */     return result;
/* 294:    */   }
/* 295:    */   
/* 296:    */   public XWPFParagraph getParagraphArray(int pos)
/* 297:    */   {
/* 298:351 */     if ((pos >= 0) && (pos < this.paragraphs.size())) {
/* 299:352 */       return (XWPFParagraph)this.paragraphs.get(pos);
/* 300:    */     }
/* 301:354 */     return null;
/* 302:    */   }
/* 303:    */   
/* 304:    */   public POIXMLDocumentPart getPart()
/* 305:    */   {
/* 306:363 */     return this.tableRow.getTable().getPart();
/* 307:    */   }
/* 308:    */   
/* 309:    */   public BodyType getPartType()
/* 310:    */   {
/* 311:370 */     return BodyType.TABLECELL;
/* 312:    */   }
/* 313:    */   
/* 314:    */   public XWPFTable getTable(CTTbl ctTable)
/* 315:    */   {
/* 316:379 */     for (int i = 0; i < this.tables.size(); i++) {
/* 317:380 */       if (((XWPFTable)getTables().get(i)).getCTTbl() == ctTable) {
/* 318:380 */         return (XWPFTable)getTables().get(i);
/* 319:    */       }
/* 320:    */     }
/* 321:382 */     return null;
/* 322:    */   }
/* 323:    */   
/* 324:    */   public XWPFTable getTableArray(int pos)
/* 325:    */   {
/* 326:389 */     if ((pos >= 0) && (pos < this.tables.size())) {
/* 327:390 */       return (XWPFTable)this.tables.get(pos);
/* 328:    */     }
/* 329:392 */     return null;
/* 330:    */   }
/* 331:    */   
/* 332:    */   public List<XWPFTable> getTables()
/* 333:    */   {
/* 334:399 */     return Collections.unmodifiableList(this.tables);
/* 335:    */   }
/* 336:    */   
/* 337:    */   public void insertTable(int pos, XWPFTable table)
/* 338:    */   {
/* 339:408 */     this.bodyElements.add(pos, table);
/* 340:409 */     int i = 0;
/* 341:410 */     for (CTTbl tbl : this.ctTc.getTblArray())
/* 342:    */     {
/* 343:411 */       if (tbl == table.getCTTbl()) {
/* 344:    */         break;
/* 345:    */       }
/* 346:414 */       i++;
/* 347:    */     }
/* 348:416 */     this.tables.add(i, table);
/* 349:    */   }
/* 350:    */   
/* 351:    */   public String getText()
/* 352:    */   {
/* 353:420 */     StringBuilder text = new StringBuilder();
/* 354:421 */     for (XWPFParagraph p : this.paragraphs) {
/* 355:422 */       text.append(p.getText());
/* 356:    */     }
/* 357:424 */     return text.toString();
/* 358:    */   }
/* 359:    */   
/* 360:    */   public void setText(String text)
/* 361:    */   {
/* 362:428 */     CTP ctP = this.ctTc.sizeOfPArray() == 0 ? this.ctTc.addNewP() : this.ctTc.getPArray(0);
/* 363:429 */     XWPFParagraph par = new XWPFParagraph(ctP, this);
/* 364:430 */     par.createRun().setText(text);
/* 365:    */   }
/* 366:    */   
/* 367:    */   public String getTextRecursively()
/* 368:    */   {
/* 369:438 */     StringBuffer text = new StringBuffer();
/* 370:439 */     for (int i = 0; i < this.bodyElements.size(); i++)
/* 371:    */     {
/* 372:440 */       boolean isLast = i == this.bodyElements.size() - 1;
/* 373:441 */       appendBodyElementText(text, (IBodyElement)this.bodyElements.get(i), isLast);
/* 374:    */     }
/* 375:444 */     return text.toString();
/* 376:    */   }
/* 377:    */   
/* 378:    */   private void appendBodyElementText(StringBuffer text, IBodyElement e, boolean isLast)
/* 379:    */   {
/* 380:448 */     if ((e instanceof XWPFParagraph))
/* 381:    */     {
/* 382:449 */       text.append(((XWPFParagraph)e).getText());
/* 383:450 */       if (!isLast) {
/* 384:451 */         text.append('\t');
/* 385:    */       }
/* 386:    */     }
/* 387:453 */     else if ((e instanceof XWPFTable))
/* 388:    */     {
/* 389:454 */       XWPFTable eTable = (XWPFTable)e;
/* 390:455 */       for (XWPFTableRow row : eTable.getRows()) {
/* 391:456 */         for (XWPFTableCell cell : row.getTableCells())
/* 392:    */         {
/* 393:457 */           List<IBodyElement> localBodyElements = cell.getBodyElements();
/* 394:458 */           for (int i = 0; i < localBodyElements.size(); i++)
/* 395:    */           {
/* 396:459 */             boolean localIsLast = i == localBodyElements.size() - 1;
/* 397:460 */             appendBodyElementText(text, (IBodyElement)localBodyElements.get(i), localIsLast);
/* 398:    */           }
/* 399:    */         }
/* 400:    */       }
/* 401:465 */       if (!isLast) {
/* 402:466 */         text.append('\n');
/* 403:    */       }
/* 404:    */     }
/* 405:468 */     else if ((e instanceof XWPFSDT))
/* 406:    */     {
/* 407:469 */       text.append(((XWPFSDT)e).getContent().getText());
/* 408:470 */       if (!isLast) {
/* 409:471 */         text.append('\t');
/* 410:    */       }
/* 411:    */     }
/* 412:    */   }
/* 413:    */   
/* 414:    */   public XWPFTableCell getTableCell(CTTc cell)
/* 415:    */   {
/* 416:480 */     XmlCursor cursor = cell.newCursor();
/* 417:481 */     cursor.toParent();
/* 418:482 */     XmlObject o = cursor.getObject();
/* 419:483 */     if (!(o instanceof CTRow)) {
/* 420:484 */       return null;
/* 421:    */     }
/* 422:486 */     CTRow row = (CTRow)o;
/* 423:487 */     cursor.toParent();
/* 424:488 */     o = cursor.getObject();
/* 425:489 */     cursor.dispose();
/* 426:490 */     if (!(o instanceof CTTbl)) {
/* 427:491 */       return null;
/* 428:    */     }
/* 429:493 */     CTTbl tbl = (CTTbl)o;
/* 430:494 */     XWPFTable table = getTable(tbl);
/* 431:495 */     if (table == null) {
/* 432:496 */       return null;
/* 433:    */     }
/* 434:498 */     XWPFTableRow tr = table.getRow(row);
/* 435:499 */     if (tr == null) {
/* 436:500 */       return null;
/* 437:    */     }
/* 438:502 */     return tr.getTableCell(cell);
/* 439:    */   }
/* 440:    */   
/* 441:    */   public XWPFDocument getXWPFDocument()
/* 442:    */   {
/* 443:506 */     return this.part.getXWPFDocument();
/* 444:    */   }
/* 445:    */   
/* 446:    */   public static enum XWPFVertAlign
/* 447:    */   {
/* 448:511 */     TOP,  CENTER,  BOTH,  BOTTOM;
/* 449:    */     
/* 450:    */     private XWPFVertAlign() {}
/* 451:    */   }
/* 452:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFTableCell
 * JD-Core Version:    0.7.0.1
 */