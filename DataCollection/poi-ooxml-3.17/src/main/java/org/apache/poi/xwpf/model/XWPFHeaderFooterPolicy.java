/*   1:    */ package org.apache.poi.xwpf.model;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.office.CTLock;
/*   4:    */ import com.microsoft.schemas.office.office.STConnectType;
/*   5:    */ import com.microsoft.schemas.vml.CTF;
/*   6:    */ import com.microsoft.schemas.vml.CTFormulas;
/*   7:    */ import com.microsoft.schemas.vml.CTGroup;
/*   8:    */ import com.microsoft.schemas.vml.CTGroup.Factory;
/*   9:    */ import com.microsoft.schemas.vml.CTH;
/*  10:    */ import com.microsoft.schemas.vml.CTHandles;
/*  11:    */ import com.microsoft.schemas.vml.CTPath;
/*  12:    */ import com.microsoft.schemas.vml.CTShape;
/*  13:    */ import com.microsoft.schemas.vml.CTShapetype;
/*  14:    */ import com.microsoft.schemas.vml.CTTextPath;
/*  15:    */ import com.microsoft.schemas.vml.STExt;
/*  16:    */ import com.microsoft.schemas.vml.STTrueFalse;
/*  17:    */ import org.apache.poi.POIXMLDocumentPart;
/*  18:    */ import org.apache.poi.POIXMLDocumentPart.RelationPart;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  20:    */ import org.apache.poi.xwpf.usermodel.XWPFDocument;
/*  21:    */ import org.apache.poi.xwpf.usermodel.XWPFFactory;
/*  22:    */ import org.apache.poi.xwpf.usermodel.XWPFFooter;
/*  23:    */ import org.apache.poi.xwpf.usermodel.XWPFHeader;
/*  24:    */ import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
/*  25:    */ import org.apache.poi.xwpf.usermodel.XWPFParagraph;
/*  26:    */ import org.apache.poi.xwpf.usermodel.XWPFRelation;
/*  27:    */ import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
/*  28:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
/*  29:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
/*  30:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
/*  31:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
/*  32:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  33:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP.Factory;
/*  34:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*  35:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture;
/*  36:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*  37:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*  38:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
/*  39:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*  40:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
/*  41:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument.Factory;
/*  42:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
/*  43:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument.Factory;
/*  44:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
/*  45:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr.Enum;
/*  46:    */ 
/*  47:    */ public class XWPFHeaderFooterPolicy
/*  48:    */ {
/*  49: 66 */   public static final STHdrFtr.Enum DEFAULT = STHdrFtr.DEFAULT;
/*  50: 67 */   public static final STHdrFtr.Enum EVEN = STHdrFtr.EVEN;
/*  51: 68 */   public static final STHdrFtr.Enum FIRST = STHdrFtr.FIRST;
/*  52:    */   private XWPFDocument doc;
/*  53:    */   private XWPFHeader firstPageHeader;
/*  54:    */   private XWPFFooter firstPageFooter;
/*  55:    */   private XWPFHeader evenPageHeader;
/*  56:    */   private XWPFFooter evenPageFooter;
/*  57:    */   private XWPFHeader defaultHeader;
/*  58:    */   private XWPFFooter defaultFooter;
/*  59:    */   
/*  60:    */   public XWPFHeaderFooterPolicy(XWPFDocument doc)
/*  61:    */   {
/*  62: 87 */     this(doc, null);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public XWPFHeaderFooterPolicy(XWPFDocument doc, CTSectPr sectPr)
/*  66:    */   {
/*  67:100 */     if (sectPr == null)
/*  68:    */     {
/*  69:101 */       CTBody ctBody = doc.getDocument().getBody();
/*  70:102 */       sectPr = ctBody.isSetSectPr() ? ctBody.getSectPr() : ctBody.addNewSectPr();
/*  71:    */     }
/*  72:106 */     this.doc = doc;
/*  73:107 */     for (int i = 0; i < sectPr.sizeOfHeaderReferenceArray(); i++)
/*  74:    */     {
/*  75:109 */       CTHdrFtrRef ref = sectPr.getHeaderReferenceArray(i);
/*  76:110 */       POIXMLDocumentPart relatedPart = doc.getRelationById(ref.getId());
/*  77:111 */       XWPFHeader hdr = null;
/*  78:112 */       if ((relatedPart != null) && ((relatedPart instanceof XWPFHeader))) {
/*  79:113 */         hdr = (XWPFHeader)relatedPart;
/*  80:    */       }
/*  81:    */       STHdrFtr.Enum type;
/*  82:    */       try
/*  83:    */       {
/*  84:118 */         type = ref.getType();
/*  85:    */       }
/*  86:    */       catch (XmlValueOutOfRangeException e)
/*  87:    */       {
/*  88:120 */         type = STHdrFtr.DEFAULT;
/*  89:    */       }
/*  90:123 */       assignHeader(hdr, type);
/*  91:    */     }
/*  92:125 */     for (int i = 0; i < sectPr.sizeOfFooterReferenceArray(); i++)
/*  93:    */     {
/*  94:127 */       CTHdrFtrRef ref = sectPr.getFooterReferenceArray(i);
/*  95:128 */       POIXMLDocumentPart relatedPart = doc.getRelationById(ref.getId());
/*  96:129 */       XWPFFooter ftr = null;
/*  97:130 */       if ((relatedPart != null) && ((relatedPart instanceof XWPFFooter))) {
/*  98:131 */         ftr = (XWPFFooter)relatedPart;
/*  99:    */       }
/* 100:    */       STHdrFtr.Enum type;
/* 101:    */       try
/* 102:    */       {
/* 103:136 */         type = ref.getType();
/* 104:    */       }
/* 105:    */       catch (XmlValueOutOfRangeException e)
/* 106:    */       {
/* 107:138 */         type = STHdrFtr.DEFAULT;
/* 108:    */       }
/* 109:140 */       assignFooter(ftr, type);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   private void assignFooter(XWPFFooter ftr, STHdrFtr.Enum type)
/* 114:    */   {
/* 115:145 */     if (type == STHdrFtr.FIRST) {
/* 116:146 */       this.firstPageFooter = ftr;
/* 117:147 */     } else if (type == STHdrFtr.EVEN) {
/* 118:148 */       this.evenPageFooter = ftr;
/* 119:    */     } else {
/* 120:150 */       this.defaultFooter = ftr;
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   private void assignHeader(XWPFHeader hdr, STHdrFtr.Enum type)
/* 125:    */   {
/* 126:155 */     if (type == STHdrFtr.FIRST) {
/* 127:156 */       this.firstPageHeader = hdr;
/* 128:157 */     } else if (type == STHdrFtr.EVEN) {
/* 129:158 */       this.evenPageHeader = hdr;
/* 130:    */     } else {
/* 131:160 */       this.defaultHeader = hdr;
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public XWPFHeader createHeader(STHdrFtr.Enum type)
/* 136:    */   {
/* 137:169 */     return createHeader(type, null);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public XWPFHeader createHeader(STHdrFtr.Enum type, XWPFParagraph[] pars)
/* 141:    */   {
/* 142:178 */     XWPFHeader header = getHeader(type);
/* 143:180 */     if (header == null)
/* 144:    */     {
/* 145:181 */       HdrDocument hdrDoc = HdrDocument.Factory.newInstance();
/* 146:    */       
/* 147:183 */       XWPFRelation relation = XWPFRelation.HEADER;
/* 148:184 */       int i = getRelationIndex(relation);
/* 149:    */       
/* 150:186 */       XWPFHeader wrapper = (XWPFHeader)this.doc.createRelationship(relation, XWPFFactory.getInstance(), i);
/* 151:    */       
/* 152:188 */       wrapper.setXWPFDocument(this.doc);
/* 153:    */       
/* 154:190 */       String pStyle = "Header";
/* 155:191 */       CTHdrFtr hdr = buildHdr(type, pStyle, wrapper, pars);
/* 156:192 */       wrapper.setHeaderFooter(hdr);
/* 157:193 */       hdrDoc.setHdr(hdr);
/* 158:194 */       assignHeader(wrapper, type);
/* 159:195 */       header = wrapper;
/* 160:    */     }
/* 161:198 */     return header;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public XWPFFooter createFooter(STHdrFtr.Enum type)
/* 165:    */   {
/* 166:206 */     return createFooter(type, null);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public XWPFFooter createFooter(STHdrFtr.Enum type, XWPFParagraph[] pars)
/* 170:    */   {
/* 171:215 */     XWPFFooter footer = getFooter(type);
/* 172:217 */     if (footer == null)
/* 173:    */     {
/* 174:218 */       FtrDocument ftrDoc = FtrDocument.Factory.newInstance();
/* 175:    */       
/* 176:220 */       XWPFRelation relation = XWPFRelation.FOOTER;
/* 177:221 */       int i = getRelationIndex(relation);
/* 178:    */       
/* 179:223 */       XWPFFooter wrapper = (XWPFFooter)this.doc.createRelationship(relation, XWPFFactory.getInstance(), i);
/* 180:    */       
/* 181:225 */       wrapper.setXWPFDocument(this.doc);
/* 182:    */       
/* 183:227 */       String pStyle = "Footer";
/* 184:228 */       CTHdrFtr ftr = buildFtr(type, pStyle, wrapper, pars);
/* 185:229 */       wrapper.setHeaderFooter(ftr);
/* 186:230 */       ftrDoc.setFtr(ftr);
/* 187:231 */       assignFooter(wrapper, type);
/* 188:232 */       footer = wrapper;
/* 189:    */     }
/* 190:235 */     return footer;
/* 191:    */   }
/* 192:    */   
/* 193:    */   private int getRelationIndex(XWPFRelation relation)
/* 194:    */   {
/* 195:239 */     int i = 1;
/* 196:240 */     for (POIXMLDocumentPart.RelationPart rp : this.doc.getRelationParts()) {
/* 197:241 */       if (rp.getRelationship().getRelationshipType().equals(relation.getRelation())) {
/* 198:242 */         i++;
/* 199:    */       }
/* 200:    */     }
/* 201:245 */     return i;
/* 202:    */   }
/* 203:    */   
/* 204:    */   private CTHdrFtr buildFtr(STHdrFtr.Enum type, String pStyle, XWPFHeaderFooter wrapper, XWPFParagraph[] pars)
/* 205:    */   {
/* 206:250 */     CTHdrFtr ftr = buildHdrFtr(pStyle, pars, wrapper);
/* 207:251 */     setFooterReference(type, wrapper);
/* 208:252 */     return ftr;
/* 209:    */   }
/* 210:    */   
/* 211:    */   private CTHdrFtr buildHdr(STHdrFtr.Enum type, String pStyle, XWPFHeaderFooter wrapper, XWPFParagraph[] pars)
/* 212:    */   {
/* 213:257 */     CTHdrFtr hdr = buildHdrFtr(pStyle, pars, wrapper);
/* 214:258 */     setHeaderReference(type, wrapper);
/* 215:259 */     return hdr;
/* 216:    */   }
/* 217:    */   
/* 218:    */   private CTHdrFtr buildHdrFtr(String pStyle, XWPFParagraph[] paragraphs, XWPFHeaderFooter wrapper)
/* 219:    */   {
/* 220:275 */     CTHdrFtr ftr = wrapper._getHdrFtr();
/* 221:276 */     if (paragraphs != null) {
/* 222:277 */       for (int i = 0; i < paragraphs.length; i++)
/* 223:    */       {
/* 224:278 */         CTP p = ftr.addNewP();
/* 225:279 */         ftr.setPArray(i, paragraphs[i].getCTP());
/* 226:    */       }
/* 227:    */     }
/* 228:296 */     return ftr;
/* 229:    */   }
/* 230:    */   
/* 231:    */   private void setFooterReference(STHdrFtr.Enum type, XWPFHeaderFooter wrapper)
/* 232:    */   {
/* 233:301 */     CTHdrFtrRef ref = this.doc.getDocument().getBody().getSectPr().addNewFooterReference();
/* 234:302 */     ref.setType(type);
/* 235:303 */     ref.setId(this.doc.getRelationId(wrapper));
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void setHeaderReference(STHdrFtr.Enum type, XWPFHeaderFooter wrapper)
/* 239:    */   {
/* 240:308 */     CTHdrFtrRef ref = this.doc.getDocument().getBody().getSectPr().addNewHeaderReference();
/* 241:309 */     ref.setType(type);
/* 242:310 */     ref.setId(this.doc.getRelationId(wrapper));
/* 243:    */   }
/* 244:    */   
/* 245:    */   public XWPFHeader getFirstPageHeader()
/* 246:    */   {
/* 247:314 */     return this.firstPageHeader;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public XWPFFooter getFirstPageFooter()
/* 251:    */   {
/* 252:318 */     return this.firstPageFooter;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public XWPFHeader getOddPageHeader()
/* 256:    */   {
/* 257:326 */     return this.defaultHeader;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public XWPFFooter getOddPageFooter()
/* 261:    */   {
/* 262:334 */     return this.defaultFooter;
/* 263:    */   }
/* 264:    */   
/* 265:    */   public XWPFHeader getEvenPageHeader()
/* 266:    */   {
/* 267:338 */     return this.evenPageHeader;
/* 268:    */   }
/* 269:    */   
/* 270:    */   public XWPFFooter getEvenPageFooter()
/* 271:    */   {
/* 272:342 */     return this.evenPageFooter;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public XWPFHeader getDefaultHeader()
/* 276:    */   {
/* 277:346 */     return this.defaultHeader;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public XWPFFooter getDefaultFooter()
/* 281:    */   {
/* 282:350 */     return this.defaultFooter;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public XWPFHeader getHeader(int pageNumber)
/* 286:    */   {
/* 287:360 */     if ((pageNumber == 1) && (this.firstPageHeader != null)) {
/* 288:361 */       return this.firstPageHeader;
/* 289:    */     }
/* 290:363 */     if ((pageNumber % 2 == 0) && (this.evenPageHeader != null)) {
/* 291:364 */       return this.evenPageHeader;
/* 292:    */     }
/* 293:366 */     return this.defaultHeader;
/* 294:    */   }
/* 295:    */   
/* 296:    */   public XWPFHeader getHeader(STHdrFtr.Enum type)
/* 297:    */   {
/* 298:376 */     if (type == STHdrFtr.EVEN) {
/* 299:377 */       return this.evenPageHeader;
/* 300:    */     }
/* 301:378 */     if (type == STHdrFtr.FIRST) {
/* 302:379 */       return this.firstPageHeader;
/* 303:    */     }
/* 304:381 */     return this.defaultHeader;
/* 305:    */   }
/* 306:    */   
/* 307:    */   public XWPFFooter getFooter(int pageNumber)
/* 308:    */   {
/* 309:391 */     if ((pageNumber == 1) && (this.firstPageFooter != null)) {
/* 310:392 */       return this.firstPageFooter;
/* 311:    */     }
/* 312:394 */     if ((pageNumber % 2 == 0) && (this.evenPageFooter != null)) {
/* 313:395 */       return this.evenPageFooter;
/* 314:    */     }
/* 315:397 */     return this.defaultFooter;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public XWPFFooter getFooter(STHdrFtr.Enum type)
/* 319:    */   {
/* 320:407 */     if (type == STHdrFtr.EVEN) {
/* 321:408 */       return this.evenPageFooter;
/* 322:    */     }
/* 323:409 */     if (type == STHdrFtr.FIRST) {
/* 324:410 */       return this.firstPageFooter;
/* 325:    */     }
/* 326:412 */     return this.defaultFooter;
/* 327:    */   }
/* 328:    */   
/* 329:    */   public void createWatermark(String text)
/* 330:    */   {
/* 331:417 */     XWPFParagraph[] pars = new XWPFParagraph[1];
/* 332:418 */     pars[0] = getWatermarkParagraph(text, 1);
/* 333:419 */     createHeader(DEFAULT, pars);
/* 334:420 */     pars[0] = getWatermarkParagraph(text, 2);
/* 335:421 */     createHeader(FIRST, pars);
/* 336:422 */     pars[0] = getWatermarkParagraph(text, 3);
/* 337:423 */     createHeader(EVEN, pars);
/* 338:    */   }
/* 339:    */   
/* 340:    */   private XWPFParagraph getWatermarkParagraph(String text, int idx)
/* 341:    */   {
/* 342:431 */     CTP p = CTP.Factory.newInstance();
/* 343:432 */     byte[] rsidr = this.doc.getDocument().getBody().getPArray(0).getRsidR();
/* 344:433 */     byte[] rsidrdefault = this.doc.getDocument().getBody().getPArray(0).getRsidRDefault();
/* 345:434 */     p.setRsidP(rsidr);
/* 346:435 */     p.setRsidRDefault(rsidrdefault);
/* 347:436 */     CTPPr pPr = p.addNewPPr();
/* 348:437 */     pPr.addNewPStyle().setVal("Header");
/* 349:    */     
/* 350:439 */     CTR r = p.addNewR();
/* 351:440 */     CTRPr rPr = r.addNewRPr();
/* 352:441 */     rPr.addNewNoProof();
/* 353:442 */     CTPicture pict = r.addNewPict();
/* 354:443 */     CTGroup group = CTGroup.Factory.newInstance();
/* 355:444 */     CTShapetype shapetype = group.addNewShapetype();
/* 356:445 */     shapetype.setId("_x0000_t136");
/* 357:446 */     shapetype.setCoordsize("1600,21600");
/* 358:447 */     shapetype.setSpt(136.0F);
/* 359:448 */     shapetype.setAdj("10800");
/* 360:449 */     shapetype.setPath2("m@7,0l@8,0m@5,21600l@6,21600e");
/* 361:450 */     CTFormulas formulas = shapetype.addNewFormulas();
/* 362:451 */     formulas.addNewF().setEqn("sum #0 0 10800");
/* 363:452 */     formulas.addNewF().setEqn("prod #0 2 1");
/* 364:453 */     formulas.addNewF().setEqn("sum 21600 0 @1");
/* 365:454 */     formulas.addNewF().setEqn("sum 0 0 @2");
/* 366:455 */     formulas.addNewF().setEqn("sum 21600 0 @3");
/* 367:456 */     formulas.addNewF().setEqn("if @0 @3 0");
/* 368:457 */     formulas.addNewF().setEqn("if @0 21600 @1");
/* 369:458 */     formulas.addNewF().setEqn("if @0 0 @2");
/* 370:459 */     formulas.addNewF().setEqn("if @0 @4 21600");
/* 371:460 */     formulas.addNewF().setEqn("mid @5 @6");
/* 372:461 */     formulas.addNewF().setEqn("mid @8 @5");
/* 373:462 */     formulas.addNewF().setEqn("mid @7 @8");
/* 374:463 */     formulas.addNewF().setEqn("mid @6 @7");
/* 375:464 */     formulas.addNewF().setEqn("sum @6 0 @5");
/* 376:465 */     CTPath path = shapetype.addNewPath();
/* 377:466 */     path.setTextpathok(STTrueFalse.T);
/* 378:467 */     path.setConnecttype(STConnectType.CUSTOM);
/* 379:468 */     path.setConnectlocs("@9,0;@10,10800;@11,21600;@12,10800");
/* 380:469 */     path.setConnectangles("270,180,90,0");
/* 381:470 */     CTTextPath shapeTypeTextPath = shapetype.addNewTextpath();
/* 382:471 */     shapeTypeTextPath.setOn(STTrueFalse.T);
/* 383:472 */     shapeTypeTextPath.setFitshape(STTrueFalse.T);
/* 384:473 */     CTHandles handles = shapetype.addNewHandles();
/* 385:474 */     CTH h = handles.addNewH();
/* 386:475 */     h.setPosition("#0,bottomRight");
/* 387:476 */     h.setXrange("6629,14971");
/* 388:477 */     CTLock lock = shapetype.addNewLock();
/* 389:478 */     lock.setExt(STExt.EDIT);
/* 390:479 */     CTShape shape = group.addNewShape();
/* 391:480 */     shape.setId("PowerPlusWaterMarkObject" + idx);
/* 392:481 */     shape.setSpid("_x0000_s102" + (4 + idx));
/* 393:482 */     shape.setType("#_x0000_t136");
/* 394:483 */     shape.setStyle("position:absolute;margin-left:0;margin-top:0;width:415pt;height:207.5pt;z-index:-251654144;mso-wrap-edited:f;mso-position-horizontal:center;mso-position-horizontal-relative:margin;mso-position-vertical:center;mso-position-vertical-relative:margin");
/* 395:484 */     shape.setWrapcoords("616 5068 390 16297 39 16921 -39 17155 7265 17545 7186 17467 -39 17467 18904 17467 10507 17467 8710 17545 18904 17077 18787 16843 18358 16297 18279 12554 19178 12476 20701 11774 20779 11228 21131 10059 21248 8811 21248 7563 20975 6316 20935 5380 19490 5146 14022 5068 2616 5068");
/* 396:485 */     shape.setFillcolor("black");
/* 397:486 */     shape.setStroked(STTrueFalse.FALSE);
/* 398:487 */     CTTextPath shapeTextPath = shape.addNewTextpath();
/* 399:488 */     shapeTextPath.setStyle("font-family:&quot;Cambria&quot;;font-size:1pt");
/* 400:489 */     shapeTextPath.setString(text);
/* 401:490 */     pict.set(group);
/* 402:    */     
/* 403:492 */     return new XWPFParagraph(p, this.doc);
/* 404:    */   }
/* 405:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy
 * JD-Core Version:    0.7.0.1
 */