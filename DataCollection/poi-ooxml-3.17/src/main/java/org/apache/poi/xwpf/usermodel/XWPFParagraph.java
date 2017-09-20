/*    1:     */ package org.apache.poi.xwpf.usermodel;
/*    2:     */ 
/*    3:     */ import java.math.BigInteger;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Collections;
/*    6:     */ import java.util.List;
/*    7:     */ import org.apache.poi.POIXMLDocumentPart;
/*    8:     */ import org.apache.poi.util.Internal;
/*    9:     */ import org.apache.poi.wp.usermodel.Paragraph;
/*   10:     */ import org.apache.xmlbeans.XmlCursor;
/*   11:     */ import org.apache.xmlbeans.XmlObject;
/*   12:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
/*   13:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
/*   14:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
/*   15:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef;
/*   16:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
/*   17:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
/*   18:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
/*   19:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText;
/*   20:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
/*   21:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum;
/*   22:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt;
/*   23:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl;
/*   24:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
/*   25:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*   26:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*   27:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
/*   28:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*   29:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr;
/*   30:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*   31:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*   32:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange;
/*   33:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*   34:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
/*   35:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
/*   36:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun;
/*   37:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
/*   38:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*   39:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
/*   40:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
/*   41:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
/*   42:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum;
/*   43:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc.Enum;
/*   44:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
/*   45:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule.Enum;
/*   46:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum;
/*   47:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*   48:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.Enum;
/*   49:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment.Enum;
/*   50:     */ import org.w3c.dom.Node;
/*   51:     */ 
/*   52:     */ public class XWPFParagraph
/*   53:     */   implements IBodyElement, IRunBody, ISDTContents, Paragraph
/*   54:     */ {
/*   55:     */   private final CTP paragraph;
/*   56:     */   protected IBody part;
/*   57:     */   protected XWPFDocument document;
/*   58:     */   protected List<XWPFRun> runs;
/*   59:     */   protected List<IRunElement> iruns;
/*   60:  48 */   private StringBuffer footnoteText = new StringBuffer();
/*   61:     */   
/*   62:     */   public XWPFParagraph(CTP prgrph, IBody part)
/*   63:     */   {
/*   64:  51 */     this.paragraph = prgrph;
/*   65:  52 */     this.part = part;
/*   66:     */     
/*   67:  54 */     this.document = part.getXWPFDocument();
/*   68:  56 */     if (this.document == null) {
/*   69:  57 */       throw new NullPointerException();
/*   70:     */     }
/*   71:  61 */     this.runs = new ArrayList();
/*   72:  62 */     this.iruns = new ArrayList();
/*   73:  63 */     buildRunsInOrderFromXml(this.paragraph);
/*   74:  66 */     for (XWPFRun run : this.runs)
/*   75:     */     {
/*   76:  67 */       CTR r = run.getCTR();
/*   77:     */       
/*   78:     */ 
/*   79:     */ 
/*   80:  71 */       XmlCursor c = r.newCursor();
/*   81:  72 */       c.selectPath("child::*");
/*   82:  73 */       while (c.toNextSelection())
/*   83:     */       {
/*   84:  74 */         XmlObject o = c.getObject();
/*   85:  75 */         if ((o instanceof CTFtnEdnRef))
/*   86:     */         {
/*   87:  76 */           CTFtnEdnRef ftn = (CTFtnEdnRef)o;
/*   88:  77 */           this.footnoteText.append(" [").append(ftn.getId()).append(": ");
/*   89:  78 */           XWPFFootnote footnote = ftn.getDomNode().getLocalName().equals("footnoteReference") ? this.document.getFootnoteByID(ftn.getId().intValue()) : this.document.getEndnoteByID(ftn.getId().intValue());
/*   90:     */           
/*   91:     */ 
/*   92:     */ 
/*   93:     */ 
/*   94:  83 */           boolean first = true;
/*   95:  84 */           for (XWPFParagraph p : footnote.getParagraphs())
/*   96:     */           {
/*   97:  85 */             if (!first) {
/*   98:  86 */               this.footnoteText.append("\n");
/*   99:     */             }
/*  100:  88 */             first = false;
/*  101:  89 */             this.footnoteText.append(p.getText());
/*  102:     */           }
/*  103:  92 */           this.footnoteText.append("] ");
/*  104:     */         }
/*  105:     */       }
/*  106:  95 */       c.dispose();
/*  107:     */     }
/*  108:     */   }
/*  109:     */   
/*  110:     */   private void buildRunsInOrderFromXml(XmlObject object)
/*  111:     */   {
/*  112: 106 */     XmlCursor c = object.newCursor();
/*  113: 107 */     c.selectPath("child::*");
/*  114: 108 */     while (c.toNextSelection())
/*  115:     */     {
/*  116: 109 */       XmlObject o = c.getObject();
/*  117: 110 */       if ((o instanceof CTR))
/*  118:     */       {
/*  119: 111 */         XWPFRun r = new XWPFRun((CTR)o, this);
/*  120: 112 */         this.runs.add(r);
/*  121: 113 */         this.iruns.add(r);
/*  122:     */       }
/*  123: 115 */       if ((o instanceof CTHyperlink))
/*  124:     */       {
/*  125: 116 */         CTHyperlink link = (CTHyperlink)o;
/*  126: 117 */         for (CTR r : link.getRArray())
/*  127:     */         {
/*  128: 118 */           XWPFHyperlinkRun hr = new XWPFHyperlinkRun(link, r, this);
/*  129: 119 */           this.runs.add(hr);
/*  130: 120 */           this.iruns.add(hr);
/*  131:     */         }
/*  132:     */       }
/*  133: 123 */       if ((o instanceof CTSimpleField))
/*  134:     */       {
/*  135: 124 */         CTSimpleField field = (CTSimpleField)o;
/*  136: 125 */         for (CTR r : field.getRArray())
/*  137:     */         {
/*  138: 126 */           XWPFFieldRun fr = new XWPFFieldRun(field, r, this);
/*  139: 127 */           this.runs.add(fr);
/*  140: 128 */           this.iruns.add(fr);
/*  141:     */         }
/*  142:     */       }
/*  143: 131 */       if ((o instanceof CTSdtBlock))
/*  144:     */       {
/*  145: 132 */         XWPFSDT cc = new XWPFSDT((CTSdtBlock)o, this.part);
/*  146: 133 */         this.iruns.add(cc);
/*  147:     */       }
/*  148: 135 */       if ((o instanceof CTSdtRun))
/*  149:     */       {
/*  150: 136 */         XWPFSDT cc = new XWPFSDT((CTSdtRun)o, this.part);
/*  151: 137 */         this.iruns.add(cc);
/*  152:     */       }
/*  153: 139 */       if ((o instanceof CTRunTrackChange)) {
/*  154: 140 */         for (CTR r : ((CTRunTrackChange)o).getRArray())
/*  155:     */         {
/*  156: 141 */           XWPFRun cr = new XWPFRun(r, this);
/*  157: 142 */           this.runs.add(cr);
/*  158: 143 */           this.iruns.add(cr);
/*  159:     */         }
/*  160:     */       }
/*  161: 146 */       if ((o instanceof CTSmartTagRun)) {
/*  162: 149 */         buildRunsInOrderFromXml(o);
/*  163:     */       }
/*  164:     */     }
/*  165: 152 */     c.dispose();
/*  166:     */   }
/*  167:     */   
/*  168:     */   @Internal
/*  169:     */   public CTP getCTP()
/*  170:     */   {
/*  171: 157 */     return this.paragraph;
/*  172:     */   }
/*  173:     */   
/*  174:     */   public List<XWPFRun> getRuns()
/*  175:     */   {
/*  176: 161 */     return Collections.unmodifiableList(this.runs);
/*  177:     */   }
/*  178:     */   
/*  179:     */   public List<IRunElement> getIRuns()
/*  180:     */   {
/*  181: 170 */     return Collections.unmodifiableList(this.iruns);
/*  182:     */   }
/*  183:     */   
/*  184:     */   public boolean isEmpty()
/*  185:     */   {
/*  186: 174 */     return !this.paragraph.getDomNode().hasChildNodes();
/*  187:     */   }
/*  188:     */   
/*  189:     */   public XWPFDocument getDocument()
/*  190:     */   {
/*  191: 179 */     return this.document;
/*  192:     */   }
/*  193:     */   
/*  194:     */   public String getText()
/*  195:     */   {
/*  196: 187 */     StringBuffer out = new StringBuffer();
/*  197: 188 */     for (IRunElement run : this.iruns) {
/*  198: 189 */       if ((run instanceof XWPFRun))
/*  199:     */       {
/*  200: 190 */         XWPFRun xRun = (XWPFRun)run;
/*  201: 192 */         if (!xRun.getCTR().isSetRsidDel()) {
/*  202: 193 */           out.append(xRun);
/*  203:     */         }
/*  204:     */       }
/*  205: 195 */       else if ((run instanceof XWPFSDT))
/*  206:     */       {
/*  207: 196 */         out.append(((XWPFSDT)run).getContent().getText());
/*  208:     */       }
/*  209:     */       else
/*  210:     */       {
/*  211: 198 */         out.append(run);
/*  212:     */       }
/*  213:     */     }
/*  214: 201 */     out.append(this.footnoteText);
/*  215: 202 */     return out.toString();
/*  216:     */   }
/*  217:     */   
/*  218:     */   public String getStyleID()
/*  219:     */   {
/*  220: 212 */     if ((this.paragraph.getPPr() != null) && 
/*  221: 213 */       (this.paragraph.getPPr().getPStyle() != null) && 
/*  222: 214 */       (this.paragraph.getPPr().getPStyle().getVal() != null)) {
/*  223: 215 */       return this.paragraph.getPPr().getPStyle().getVal();
/*  224:     */     }
/*  225: 219 */     return null;
/*  226:     */   }
/*  227:     */   
/*  228:     */   public BigInteger getNumID()
/*  229:     */   {
/*  230: 230 */     if ((this.paragraph.getPPr() != null) && 
/*  231: 231 */       (this.paragraph.getPPr().getNumPr() != null) && 
/*  232: 232 */       (this.paragraph.getPPr().getNumPr().getNumId() != null)) {
/*  233: 233 */       return this.paragraph.getPPr().getNumPr().getNumId().getVal();
/*  234:     */     }
/*  235: 237 */     return null;
/*  236:     */   }
/*  237:     */   
/*  238:     */   public void setNumID(BigInteger numPos)
/*  239:     */   {
/*  240: 246 */     if (this.paragraph.getPPr() == null) {
/*  241: 247 */       this.paragraph.addNewPPr();
/*  242:     */     }
/*  243: 249 */     if (this.paragraph.getPPr().getNumPr() == null) {
/*  244: 250 */       this.paragraph.getPPr().addNewNumPr();
/*  245:     */     }
/*  246: 252 */     if (this.paragraph.getPPr().getNumPr().getNumId() == null) {
/*  247: 253 */       this.paragraph.getPPr().getNumPr().addNewNumId();
/*  248:     */     }
/*  249: 255 */     this.paragraph.getPPr().getNumPr().getNumId().setVal(numPos);
/*  250:     */   }
/*  251:     */   
/*  252:     */   public BigInteger getNumIlvl()
/*  253:     */   {
/*  254: 265 */     if ((this.paragraph.getPPr() != null) && 
/*  255: 266 */       (this.paragraph.getPPr().getNumPr() != null) && 
/*  256: 267 */       (this.paragraph.getPPr().getNumPr().getIlvl() != null)) {
/*  257: 268 */       return this.paragraph.getPPr().getNumPr().getIlvl().getVal();
/*  258:     */     }
/*  259: 272 */     return null;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public String getNumFmt()
/*  263:     */   {
/*  264: 281 */     BigInteger numID = getNumID();
/*  265: 282 */     XWPFNumbering numbering = this.document.getNumbering();
/*  266: 283 */     if ((numID != null) && (numbering != null))
/*  267:     */     {
/*  268: 284 */       XWPFNum num = numbering.getNum(numID);
/*  269: 285 */       if (num != null)
/*  270:     */       {
/*  271: 286 */         BigInteger ilvl = getNumIlvl();
/*  272: 287 */         BigInteger abstractNumId = num.getCTNum().getAbstractNumId().getVal();
/*  273: 288 */         CTAbstractNum anum = numbering.getAbstractNum(abstractNumId).getAbstractNum();
/*  274: 289 */         CTLvl level = null;
/*  275: 290 */         for (int i = 0; i < anum.sizeOfLvlArray(); i++)
/*  276:     */         {
/*  277: 291 */           CTLvl lvl = anum.getLvlArray(i);
/*  278: 292 */           if (lvl.getIlvl().equals(ilvl))
/*  279:     */           {
/*  280: 293 */             level = lvl;
/*  281: 294 */             break;
/*  282:     */           }
/*  283:     */         }
/*  284: 297 */         if ((level != null) && (level.getNumFmt() != null) && (level.getNumFmt().getVal() != null)) {
/*  285: 299 */           return level.getNumFmt().getVal().toString();
/*  286:     */         }
/*  287:     */       }
/*  288:     */     }
/*  289: 303 */     return null;
/*  290:     */   }
/*  291:     */   
/*  292:     */   public String getNumLevelText()
/*  293:     */   {
/*  294: 312 */     BigInteger numID = getNumID();
/*  295: 313 */     XWPFNumbering numbering = this.document.getNumbering();
/*  296: 314 */     if ((numID != null) && (numbering != null))
/*  297:     */     {
/*  298: 315 */       XWPFNum num = numbering.getNum(numID);
/*  299: 316 */       if (num != null)
/*  300:     */       {
/*  301: 317 */         BigInteger ilvl = getNumIlvl();
/*  302: 318 */         CTNum ctNum = num.getCTNum();
/*  303: 319 */         if (ctNum == null) {
/*  304: 320 */           return null;
/*  305:     */         }
/*  306: 323 */         CTDecimalNumber ctDecimalNumber = ctNum.getAbstractNumId();
/*  307: 324 */         if (ctDecimalNumber == null) {
/*  308: 325 */           return null;
/*  309:     */         }
/*  310: 328 */         BigInteger abstractNumId = ctDecimalNumber.getVal();
/*  311: 329 */         if (abstractNumId == null) {
/*  312: 330 */           return null;
/*  313:     */         }
/*  314: 333 */         XWPFAbstractNum xwpfAbstractNum = numbering.getAbstractNum(abstractNumId);
/*  315: 335 */         if (xwpfAbstractNum == null) {
/*  316: 336 */           return null;
/*  317:     */         }
/*  318: 339 */         CTAbstractNum anum = xwpfAbstractNum.getCTAbstractNum();
/*  319: 341 */         if (anum == null) {
/*  320: 342 */           return null;
/*  321:     */         }
/*  322: 345 */         CTLvl level = null;
/*  323: 346 */         for (int i = 0; i < anum.sizeOfLvlArray(); i++)
/*  324:     */         {
/*  325: 347 */           CTLvl lvl = anum.getLvlArray(i);
/*  326: 348 */           if ((lvl != null) && (lvl.getIlvl() != null) && (lvl.getIlvl().equals(ilvl)))
/*  327:     */           {
/*  328: 349 */             level = lvl;
/*  329: 350 */             break;
/*  330:     */           }
/*  331:     */         }
/*  332: 353 */         if ((level != null) && (level.getLvlText() != null) && (level.getLvlText().getVal() != null)) {
/*  333: 355 */           return level.getLvlText().getVal().toString();
/*  334:     */         }
/*  335:     */       }
/*  336:     */     }
/*  337: 359 */     return null;
/*  338:     */   }
/*  339:     */   
/*  340:     */   public BigInteger getNumStartOverride()
/*  341:     */   {
/*  342: 368 */     BigInteger numID = getNumID();
/*  343: 369 */     XWPFNumbering numbering = this.document.getNumbering();
/*  344: 370 */     if ((numID != null) && (numbering != null))
/*  345:     */     {
/*  346: 371 */       XWPFNum num = numbering.getNum(numID);
/*  347: 373 */       if (num != null)
/*  348:     */       {
/*  349: 374 */         CTNum ctNum = num.getCTNum();
/*  350: 375 */         if (ctNum == null) {
/*  351: 376 */           return null;
/*  352:     */         }
/*  353: 378 */         BigInteger ilvl = getNumIlvl();
/*  354: 379 */         CTNumLvl level = null;
/*  355: 380 */         for (int i = 0; i < ctNum.sizeOfLvlOverrideArray(); i++)
/*  356:     */         {
/*  357: 381 */           CTNumLvl ctNumLvl = ctNum.getLvlOverrideArray(i);
/*  358: 382 */           if ((ctNumLvl != null) && (ctNumLvl.getIlvl() != null) && (ctNumLvl.getIlvl().equals(ilvl)))
/*  359:     */           {
/*  360: 384 */             level = ctNumLvl;
/*  361: 385 */             break;
/*  362:     */           }
/*  363:     */         }
/*  364: 388 */         if ((level != null) && (level.getStartOverride() != null)) {
/*  365: 389 */           return level.getStartOverride().getVal();
/*  366:     */         }
/*  367:     */       }
/*  368:     */     }
/*  369: 393 */     return null;
/*  370:     */   }
/*  371:     */   
/*  372:     */   public String getParagraphText()
/*  373:     */   {
/*  374: 401 */     StringBuffer out = new StringBuffer();
/*  375: 402 */     for (XWPFRun run : this.runs) {
/*  376: 403 */       out.append(run);
/*  377:     */     }
/*  378: 405 */     return out.toString();
/*  379:     */   }
/*  380:     */   
/*  381:     */   public String getPictureText()
/*  382:     */   {
/*  383: 412 */     StringBuffer out = new StringBuffer();
/*  384: 413 */     for (XWPFRun run : this.runs) {
/*  385: 414 */       out.append(run.getPictureText());
/*  386:     */     }
/*  387: 416 */     return out.toString();
/*  388:     */   }
/*  389:     */   
/*  390:     */   public String getFootnoteText()
/*  391:     */   {
/*  392: 425 */     return this.footnoteText.toString();
/*  393:     */   }
/*  394:     */   
/*  395:     */   public ParagraphAlignment getAlignment()
/*  396:     */   {
/*  397: 443 */     CTPPr pr = getCTPPr();
/*  398: 444 */     return (pr == null) || (!pr.isSetJc()) ? ParagraphAlignment.LEFT : ParagraphAlignment.valueOf(pr.getJc().getVal().intValue());
/*  399:     */   }
/*  400:     */   
/*  401:     */   public void setAlignment(ParagraphAlignment align)
/*  402:     */   {
/*  403: 463 */     CTPPr pr = getCTPPr();
/*  404: 464 */     CTJc jc = pr.isSetJc() ? pr.getJc() : pr.addNewJc();
/*  405: 465 */     STJc.Enum en = STJc.Enum.forInt(align.getValue());
/*  406: 466 */     jc.setVal(en);
/*  407:     */   }
/*  408:     */   
/*  409:     */   public int getFontAlignment()
/*  410:     */   {
/*  411: 474 */     return getAlignment().getValue();
/*  412:     */   }
/*  413:     */   
/*  414:     */   public void setFontAlignment(int align)
/*  415:     */   {
/*  416: 479 */     ParagraphAlignment pAlign = ParagraphAlignment.valueOf(align);
/*  417: 480 */     setAlignment(pAlign);
/*  418:     */   }
/*  419:     */   
/*  420:     */   public TextAlignment getVerticalAlignment()
/*  421:     */   {
/*  422: 502 */     CTPPr pr = getCTPPr();
/*  423: 503 */     return (pr == null) || (!pr.isSetTextAlignment()) ? TextAlignment.AUTO : TextAlignment.valueOf(pr.getTextAlignment().getVal().intValue());
/*  424:     */   }
/*  425:     */   
/*  426:     */   public void setVerticalAlignment(TextAlignment valign)
/*  427:     */   {
/*  428: 528 */     CTPPr pr = getCTPPr();
/*  429: 529 */     CTTextAlignment textAlignment = pr.isSetTextAlignment() ? pr.getTextAlignment() : pr.addNewTextAlignment();
/*  430:     */     
/*  431: 531 */     STTextAlignment.Enum en = STTextAlignment.Enum.forInt(valign.getValue());
/*  432:     */     
/*  433: 533 */     textAlignment.setVal(en);
/*  434:     */   }
/*  435:     */   
/*  436:     */   public Borders getBorderTop()
/*  437:     */   {
/*  438: 545 */     CTPBdr border = getCTPBrd(false);
/*  439: 546 */     CTBorder ct = null;
/*  440: 547 */     if (border != null) {
/*  441: 548 */       ct = border.getTop();
/*  442:     */     }
/*  443: 550 */     STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
/*  444: 551 */     return Borders.valueOf(ptrn.intValue());
/*  445:     */   }
/*  446:     */   
/*  447:     */   public void setBorderTop(Borders border)
/*  448:     */   {
/*  449: 582 */     CTPBdr ct = getCTPBrd(true);
/*  450: 583 */     if (ct == null) {
/*  451: 584 */       throw new RuntimeException("invalid paragraph state");
/*  452:     */     }
/*  453: 587 */     CTBorder pr = ct.isSetTop() ? ct.getTop() : ct.addNewTop();
/*  454: 588 */     if (border.getValue() == Borders.NONE.getValue()) {
/*  455: 589 */       ct.unsetTop();
/*  456:     */     } else {
/*  457: 591 */       pr.setVal(STBorder.Enum.forInt(border.getValue()));
/*  458:     */     }
/*  459:     */   }
/*  460:     */   
/*  461:     */   public Borders getBorderBottom()
/*  462:     */   {
/*  463: 604 */     CTPBdr border = getCTPBrd(false);
/*  464: 605 */     CTBorder ct = null;
/*  465: 606 */     if (border != null) {
/*  466: 607 */       ct = border.getBottom();
/*  467:     */     }
/*  468: 609 */     STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
/*  469: 610 */     return Borders.valueOf(ptrn.intValue());
/*  470:     */   }
/*  471:     */   
/*  472:     */   public void setBorderBottom(Borders border)
/*  473:     */   {
/*  474: 641 */     CTPBdr ct = getCTPBrd(true);
/*  475: 642 */     CTBorder pr = ct.isSetBottom() ? ct.getBottom() : ct.addNewBottom();
/*  476: 643 */     if (border.getValue() == Borders.NONE.getValue()) {
/*  477: 644 */       ct.unsetBottom();
/*  478:     */     } else {
/*  479: 646 */       pr.setVal(STBorder.Enum.forInt(border.getValue()));
/*  480:     */     }
/*  481:     */   }
/*  482:     */   
/*  483:     */   public Borders getBorderLeft()
/*  484:     */   {
/*  485: 659 */     CTPBdr border = getCTPBrd(false);
/*  486: 660 */     CTBorder ct = null;
/*  487: 661 */     if (border != null) {
/*  488: 662 */       ct = border.getLeft();
/*  489:     */     }
/*  490: 664 */     STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
/*  491: 665 */     return Borders.valueOf(ptrn.intValue());
/*  492:     */   }
/*  493:     */   
/*  494:     */   public void setBorderLeft(Borders border)
/*  495:     */   {
/*  496: 691 */     CTPBdr ct = getCTPBrd(true);
/*  497: 692 */     CTBorder pr = ct.isSetLeft() ? ct.getLeft() : ct.addNewLeft();
/*  498: 693 */     if (border.getValue() == Borders.NONE.getValue()) {
/*  499: 694 */       ct.unsetLeft();
/*  500:     */     } else {
/*  501: 696 */       pr.setVal(STBorder.Enum.forInt(border.getValue()));
/*  502:     */     }
/*  503:     */   }
/*  504:     */   
/*  505:     */   public Borders getBorderRight()
/*  506:     */   {
/*  507: 709 */     CTPBdr border = getCTPBrd(false);
/*  508: 710 */     CTBorder ct = null;
/*  509: 711 */     if (border != null) {
/*  510: 712 */       ct = border.getRight();
/*  511:     */     }
/*  512: 714 */     STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
/*  513: 715 */     return Borders.valueOf(ptrn.intValue());
/*  514:     */   }
/*  515:     */   
/*  516:     */   public void setBorderRight(Borders border)
/*  517:     */   {
/*  518: 741 */     CTPBdr ct = getCTPBrd(true);
/*  519: 742 */     CTBorder pr = ct.isSetRight() ? ct.getRight() : ct.addNewRight();
/*  520: 743 */     if (border.getValue() == Borders.NONE.getValue()) {
/*  521: 744 */       ct.unsetRight();
/*  522:     */     } else {
/*  523: 746 */       pr.setVal(STBorder.Enum.forInt(border.getValue()));
/*  524:     */     }
/*  525:     */   }
/*  526:     */   
/*  527:     */   public Borders getBorderBetween()
/*  528:     */   {
/*  529: 759 */     CTPBdr border = getCTPBrd(false);
/*  530: 760 */     CTBorder ct = null;
/*  531: 761 */     if (border != null) {
/*  532: 762 */       ct = border.getBetween();
/*  533:     */     }
/*  534: 764 */     STBorder.Enum ptrn = ct != null ? ct.getVal() : STBorder.NONE;
/*  535: 765 */     return Borders.valueOf(ptrn.intValue());
/*  536:     */   }
/*  537:     */   
/*  538:     */   public void setBorderBetween(Borders border)
/*  539:     */   {
/*  540: 795 */     CTPBdr ct = getCTPBrd(true);
/*  541: 796 */     CTBorder pr = ct.isSetBetween() ? ct.getBetween() : ct.addNewBetween();
/*  542: 797 */     if (border.getValue() == Borders.NONE.getValue()) {
/*  543: 798 */       ct.unsetBetween();
/*  544:     */     } else {
/*  545: 800 */       pr.setVal(STBorder.Enum.forInt(border.getValue()));
/*  546:     */     }
/*  547:     */   }
/*  548:     */   
/*  549:     */   public boolean isPageBreak()
/*  550:     */   {
/*  551: 820 */     CTPPr ppr = getCTPPr();
/*  552: 821 */     CTOnOff ctPageBreak = ppr.isSetPageBreakBefore() ? ppr.getPageBreakBefore() : null;
/*  553: 822 */     if (ctPageBreak == null) {
/*  554: 823 */       return false;
/*  555:     */     }
/*  556: 825 */     return isTruelike(ctPageBreak.getVal(), false);
/*  557:     */   }
/*  558:     */   
/*  559:     */   private static boolean isTruelike(STOnOff.Enum value, boolean defaultValue)
/*  560:     */   {
/*  561: 829 */     if (value == null) {
/*  562: 830 */       return defaultValue;
/*  563:     */     }
/*  564: 832 */     switch (value.intValue())
/*  565:     */     {
/*  566:     */     case 1: 
/*  567:     */     case 3: 
/*  568:     */     case 6: 
/*  569: 836 */       return true;
/*  570:     */     case 2: 
/*  571:     */     case 4: 
/*  572:     */     case 5: 
/*  573: 840 */       return false;
/*  574:     */     }
/*  575: 842 */     return defaultValue;
/*  576:     */   }
/*  577:     */   
/*  578:     */   public void setPageBreak(boolean pageBreak)
/*  579:     */   {
/*  580: 863 */     CTPPr ppr = getCTPPr();
/*  581: 864 */     CTOnOff ctPageBreak = ppr.isSetPageBreakBefore() ? ppr.getPageBreakBefore() : ppr.addNewPageBreakBefore();
/*  582: 866 */     if (pageBreak) {
/*  583: 867 */       ctPageBreak.setVal(STOnOff.TRUE);
/*  584:     */     } else {
/*  585: 869 */       ctPageBreak.setVal(STOnOff.FALSE);
/*  586:     */     }
/*  587:     */   }
/*  588:     */   
/*  589:     */   public int getSpacingAfter()
/*  590:     */   {
/*  591: 880 */     CTSpacing spacing = getCTSpacing(false);
/*  592: 881 */     return (spacing != null) && (spacing.isSetAfter()) ? spacing.getAfter().intValue() : -1;
/*  593:     */   }
/*  594:     */   
/*  595:     */   public void setSpacingAfter(int spaces)
/*  596:     */   {
/*  597: 897 */     CTSpacing spacing = getCTSpacing(true);
/*  598: 898 */     if (spacing != null)
/*  599:     */     {
/*  600: 899 */       BigInteger bi = new BigInteger("" + spaces);
/*  601: 900 */       spacing.setAfter(bi);
/*  602:     */     }
/*  603:     */   }
/*  604:     */   
/*  605:     */   public int getSpacingAfterLines()
/*  606:     */   {
/*  607: 913 */     CTSpacing spacing = getCTSpacing(false);
/*  608: 914 */     return (spacing != null) && (spacing.isSetAfterLines()) ? spacing.getAfterLines().intValue() : -1;
/*  609:     */   }
/*  610:     */   
/*  611:     */   public void setSpacingAfterLines(int spaces)
/*  612:     */   {
/*  613: 935 */     CTSpacing spacing = getCTSpacing(true);
/*  614: 936 */     BigInteger bi = new BigInteger("" + spaces);
/*  615: 937 */     spacing.setAfterLines(bi);
/*  616:     */   }
/*  617:     */   
/*  618:     */   public int getSpacingBefore()
/*  619:     */   {
/*  620: 948 */     CTSpacing spacing = getCTSpacing(false);
/*  621: 949 */     return (spacing != null) && (spacing.isSetBefore()) ? spacing.getBefore().intValue() : -1;
/*  622:     */   }
/*  623:     */   
/*  624:     */   public void setSpacingBefore(int spaces)
/*  625:     */   {
/*  626: 963 */     CTSpacing spacing = getCTSpacing(true);
/*  627: 964 */     BigInteger bi = new BigInteger("" + spaces);
/*  628: 965 */     spacing.setBefore(bi);
/*  629:     */   }
/*  630:     */   
/*  631:     */   public int getSpacingBeforeLines()
/*  632:     */   {
/*  633: 977 */     CTSpacing spacing = getCTSpacing(false);
/*  634: 978 */     return (spacing != null) && (spacing.isSetBeforeLines()) ? spacing.getBeforeLines().intValue() : -1;
/*  635:     */   }
/*  636:     */   
/*  637:     */   public void setSpacingBeforeLines(int spaces)
/*  638:     */   {
/*  639: 994 */     CTSpacing spacing = getCTSpacing(true);
/*  640: 995 */     BigInteger bi = new BigInteger("" + spaces);
/*  641: 996 */     spacing.setBeforeLines(bi);
/*  642:     */   }
/*  643:     */   
/*  644:     */   public LineSpacingRule getSpacingLineRule()
/*  645:     */   {
/*  646:1009 */     CTSpacing spacing = getCTSpacing(false);
/*  647:1010 */     return (spacing != null) && (spacing.isSetLineRule()) ? LineSpacingRule.valueOf(spacing.getLineRule().intValue()) : LineSpacingRule.AUTO;
/*  648:     */   }
/*  649:     */   
/*  650:     */   public void setSpacingLineRule(LineSpacingRule rule)
/*  651:     */   {
/*  652:1025 */     CTSpacing spacing = getCTSpacing(true);
/*  653:1026 */     spacing.setLineRule(STLineSpacingRule.Enum.forInt(rule.getValue()));
/*  654:     */   }
/*  655:     */   
/*  656:     */   public double getSpacingBetween()
/*  657:     */   {
/*  658:1037 */     CTSpacing spacing = getCTSpacing(false);
/*  659:1038 */     if ((spacing == null) || (!spacing.isSetLine())) {
/*  660:1039 */       return -1.0D;
/*  661:     */     }
/*  662:1040 */     if ((spacing.getLineRule() == null) || (spacing.getLineRule() == STLineSpacingRule.AUTO))
/*  663:     */     {
/*  664:1041 */       BigInteger[] val = spacing.getLine().divideAndRemainder(BigInteger.valueOf(240L));
/*  665:1042 */       return val[0].doubleValue() + val[1].doubleValue() / 240.0D;
/*  666:     */     }
/*  667:1044 */     BigInteger[] val = spacing.getLine().divideAndRemainder(BigInteger.valueOf(20L));
/*  668:1045 */     return val[0].doubleValue() + val[1].doubleValue() / 20.0D;
/*  669:     */   }
/*  670:     */   
/*  671:     */   public void setSpacingBetween(double spacing, LineSpacingRule rule)
/*  672:     */   {
/*  673:1062 */     CTSpacing ctSp = getCTSpacing(true);
/*  674:1063 */     switch (1.$SwitchMap$org$apache$poi$xwpf$usermodel$LineSpacingRule[rule.ordinal()])
/*  675:     */     {
/*  676:     */     case 1: 
/*  677:1065 */       ctSp.setLine(new BigInteger(String.valueOf(Math.round(spacing * 240.0D))));
/*  678:1066 */       break;
/*  679:     */     default: 
/*  680:1068 */       ctSp.setLine(new BigInteger(String.valueOf(Math.round(spacing * 20.0D))));
/*  681:     */     }
/*  682:1070 */     ctSp.setLineRule(STLineSpacingRule.Enum.forInt(rule.getValue()));
/*  683:     */   }
/*  684:     */   
/*  685:     */   public void setSpacingBetween(double spacing)
/*  686:     */   {
/*  687:1079 */     setSpacingBetween(spacing, LineSpacingRule.AUTO);
/*  688:     */   }
/*  689:     */   
/*  690:     */   public int getIndentationLeft()
/*  691:     */   {
/*  692:1096 */     CTInd indentation = getCTInd(false);
/*  693:1097 */     return (indentation != null) && (indentation.isSetLeft()) ? indentation.getLeft().intValue() : -1;
/*  694:     */   }
/*  695:     */   
/*  696:     */   public void setIndentationLeft(int indentation)
/*  697:     */   {
/*  698:1115 */     CTInd indent = getCTInd(true);
/*  699:1116 */     BigInteger bi = new BigInteger("" + indentation);
/*  700:1117 */     indent.setLeft(bi);
/*  701:     */   }
/*  702:     */   
/*  703:     */   public int getIndentationRight()
/*  704:     */   {
/*  705:1135 */     CTInd indentation = getCTInd(false);
/*  706:1136 */     return (indentation != null) && (indentation.isSetRight()) ? indentation.getRight().intValue() : -1;
/*  707:     */   }
/*  708:     */   
/*  709:     */   public void setIndentationRight(int indentation)
/*  710:     */   {
/*  711:1154 */     CTInd indent = getCTInd(true);
/*  712:1155 */     BigInteger bi = new BigInteger("" + indentation);
/*  713:1156 */     indent.setRight(bi);
/*  714:     */   }
/*  715:     */   
/*  716:     */   public int getIndentationHanging()
/*  717:     */   {
/*  718:1173 */     CTInd indentation = getCTInd(false);
/*  719:1174 */     return (indentation != null) && (indentation.isSetHanging()) ? indentation.getHanging().intValue() : -1;
/*  720:     */   }
/*  721:     */   
/*  722:     */   public void setIndentationHanging(int indentation)
/*  723:     */   {
/*  724:1192 */     CTInd indent = getCTInd(true);
/*  725:1193 */     BigInteger bi = new BigInteger("" + indentation);
/*  726:1194 */     indent.setHanging(bi);
/*  727:     */   }
/*  728:     */   
/*  729:     */   public int getIndentationFirstLine()
/*  730:     */   {
/*  731:1213 */     CTInd indentation = getCTInd(false);
/*  732:1214 */     return (indentation != null) && (indentation.isSetFirstLine()) ? indentation.getFirstLine().intValue() : -1;
/*  733:     */   }
/*  734:     */   
/*  735:     */   public void setIndentationFirstLine(int indentation)
/*  736:     */   {
/*  737:1233 */     CTInd indent = getCTInd(true);
/*  738:1234 */     BigInteger bi = new BigInteger("" + indentation);
/*  739:1235 */     indent.setFirstLine(bi);
/*  740:     */   }
/*  741:     */   
/*  742:     */   public int getIndentFromLeft()
/*  743:     */   {
/*  744:1240 */     return getIndentationLeft();
/*  745:     */   }
/*  746:     */   
/*  747:     */   public void setIndentFromLeft(int dxaLeft)
/*  748:     */   {
/*  749:1245 */     setIndentationLeft(dxaLeft);
/*  750:     */   }
/*  751:     */   
/*  752:     */   public int getIndentFromRight()
/*  753:     */   {
/*  754:1250 */     return getIndentationRight();
/*  755:     */   }
/*  756:     */   
/*  757:     */   public void setIndentFromRight(int dxaRight)
/*  758:     */   {
/*  759:1255 */     setIndentationRight(dxaRight);
/*  760:     */   }
/*  761:     */   
/*  762:     */   public int getFirstLineIndent()
/*  763:     */   {
/*  764:1260 */     return getIndentationFirstLine();
/*  765:     */   }
/*  766:     */   
/*  767:     */   public void setFirstLineIndent(int first)
/*  768:     */   {
/*  769:1265 */     setIndentationFirstLine(first);
/*  770:     */   }
/*  771:     */   
/*  772:     */   public boolean isWordWrapped()
/*  773:     */   {
/*  774:1278 */     CTOnOff wordWrap = getCTPPr().isSetWordWrap() ? getCTPPr().getWordWrap() : null;
/*  775:1280 */     if (wordWrap != null) {
/*  776:1281 */       return (wordWrap.getVal() == STOnOff.ON) || (wordWrap.getVal() == STOnOff.TRUE) || (wordWrap.getVal() == STOnOff.X_1);
/*  777:     */     }
/*  778:1285 */     return false;
/*  779:     */   }
/*  780:     */   
/*  781:     */   public void setWordWrapped(boolean wrap)
/*  782:     */   {
/*  783:1298 */     CTOnOff wordWrap = getCTPPr().isSetWordWrap() ? getCTPPr().getWordWrap() : getCTPPr().addNewWordWrap();
/*  784:1300 */     if (wrap) {
/*  785:1301 */       wordWrap.setVal(STOnOff.TRUE);
/*  786:     */     } else {
/*  787:1303 */       wordWrap.unsetVal();
/*  788:     */     }
/*  789:     */   }
/*  790:     */   
/*  791:     */   public boolean isWordWrap()
/*  792:     */   {
/*  793:1308 */     return isWordWrapped();
/*  794:     */   }
/*  795:     */   
/*  796:     */   @Deprecated
/*  797:     */   public void setWordWrap(boolean wrap)
/*  798:     */   {
/*  799:1313 */     setWordWrapped(wrap);
/*  800:     */   }
/*  801:     */   
/*  802:     */   public String getStyle()
/*  803:     */   {
/*  804:1320 */     CTPPr pr = getCTPPr();
/*  805:1321 */     CTString style = pr.isSetPStyle() ? pr.getPStyle() : null;
/*  806:1322 */     return style != null ? style.getVal() : null;
/*  807:     */   }
/*  808:     */   
/*  809:     */   public void setStyle(String newStyle)
/*  810:     */   {
/*  811:1332 */     CTPPr pr = getCTPPr();
/*  812:1333 */     CTString style = pr.getPStyle() != null ? pr.getPStyle() : pr.addNewPStyle();
/*  813:1334 */     style.setVal(newStyle);
/*  814:     */   }
/*  815:     */   
/*  816:     */   private CTPBdr getCTPBrd(boolean create)
/*  817:     */   {
/*  818:1342 */     CTPPr pr = getCTPPr();
/*  819:1343 */     CTPBdr ct = pr.isSetPBdr() ? pr.getPBdr() : null;
/*  820:1344 */     if ((create) && (ct == null)) {
/*  821:1345 */       ct = pr.addNewPBdr();
/*  822:     */     }
/*  823:1347 */     return ct;
/*  824:     */   }
/*  825:     */   
/*  826:     */   private CTSpacing getCTSpacing(boolean create)
/*  827:     */   {
/*  828:1355 */     CTPPr pr = getCTPPr();
/*  829:1356 */     CTSpacing ct = pr.getSpacing() == null ? null : pr.getSpacing();
/*  830:1357 */     if ((create) && (ct == null)) {
/*  831:1358 */       ct = pr.addNewSpacing();
/*  832:     */     }
/*  833:1360 */     return ct;
/*  834:     */   }
/*  835:     */   
/*  836:     */   private CTInd getCTInd(boolean create)
/*  837:     */   {
/*  838:1368 */     CTPPr pr = getCTPPr();
/*  839:1369 */     CTInd ct = pr.getInd() == null ? null : pr.getInd();
/*  840:1370 */     if ((create) && (ct == null)) {
/*  841:1371 */       ct = pr.addNewInd();
/*  842:     */     }
/*  843:1373 */     return ct;
/*  844:     */   }
/*  845:     */   
/*  846:     */   private CTPPr getCTPPr()
/*  847:     */   {
/*  848:1381 */     CTPPr pr = this.paragraph.getPPr() == null ? this.paragraph.addNewPPr() : this.paragraph.getPPr();
/*  849:     */     
/*  850:1383 */     return pr;
/*  851:     */   }
/*  852:     */   
/*  853:     */   protected void addRun(CTR run)
/*  854:     */   {
/*  855:1395 */     int pos = this.paragraph.sizeOfRArray();
/*  856:1396 */     this.paragraph.addNewR();
/*  857:1397 */     this.paragraph.setRArray(pos, run);
/*  858:     */   }
/*  859:     */   
/*  860:     */   public XWPFRun createRun()
/*  861:     */   {
/*  862:1406 */     XWPFRun xwpfRun = new XWPFRun(this.paragraph.addNewR(), this);
/*  863:1407 */     this.runs.add(xwpfRun);
/*  864:1408 */     this.iruns.add(xwpfRun);
/*  865:1409 */     return xwpfRun;
/*  866:     */   }
/*  867:     */   
/*  868:     */   public XWPFRun insertNewRun(int pos)
/*  869:     */   {
/*  870:1420 */     if ((pos >= 0) && (pos <= this.runs.size()))
/*  871:     */     {
/*  872:1424 */       int rPos = 0;
/*  873:1425 */       for (int i = 0; i < pos; i++)
/*  874:     */       {
/*  875:1426 */         XWPFRun currRun = (XWPFRun)this.runs.get(i);
/*  876:1427 */         if ((!(currRun instanceof XWPFHyperlinkRun)) && (!(currRun instanceof XWPFFieldRun))) {
/*  877:1429 */           rPos++;
/*  878:     */         }
/*  879:     */       }
/*  880:1433 */       CTR ctRun = this.paragraph.insertNewR(rPos);
/*  881:1434 */       XWPFRun newRun = new XWPFRun(ctRun, this);
/*  882:     */       
/*  883:     */ 
/*  884:     */ 
/*  885:1438 */       int iPos = this.iruns.size();
/*  886:1439 */       if (pos < this.runs.size())
/*  887:     */       {
/*  888:1440 */         XWPFRun oldAtPos = (XWPFRun)this.runs.get(pos);
/*  889:1441 */         int oldAt = this.iruns.indexOf(oldAtPos);
/*  890:1442 */         if (oldAt != -1) {
/*  891:1443 */           iPos = oldAt;
/*  892:     */         }
/*  893:     */       }
/*  894:1446 */       this.iruns.add(iPos, newRun);
/*  895:     */       
/*  896:     */ 
/*  897:1449 */       this.runs.add(pos, newRun);
/*  898:     */       
/*  899:1451 */       return newRun;
/*  900:     */     }
/*  901:1454 */     return null;
/*  902:     */   }
/*  903:     */   
/*  904:     */   public TextSegement searchText(String searched, PositionInParagraph startPos)
/*  905:     */   {
/*  906:1467 */     int startRun = startPos.getRun();
/*  907:1468 */     int startText = startPos.getText();
/*  908:1469 */     int startChar = startPos.getChar();
/*  909:1470 */     int beginRunPos = 0;int candCharPos = 0;
/*  910:1471 */     boolean newList = false;
/*  911:     */     
/*  912:1473 */     CTR[] rArray = this.paragraph.getRArray();
/*  913:1474 */     for (int runPos = startRun; runPos < rArray.length; runPos++)
/*  914:     */     {
/*  915:1475 */       int beginTextPos = 0;int beginCharPos = 0;int textPos = 0;int charPos = 0;
/*  916:1476 */       CTR ctRun = rArray[runPos];
/*  917:1477 */       XmlCursor c = ctRun.newCursor();
/*  918:1478 */       c.selectPath("./*");
/*  919:     */       try
/*  920:     */       {
/*  921:1480 */         while (c.toNextSelection())
/*  922:     */         {
/*  923:1481 */           XmlObject o = c.getObject();
/*  924:1482 */           if ((o instanceof CTText))
/*  925:     */           {
/*  926:1483 */             if (textPos >= startText)
/*  927:     */             {
/*  928:1484 */               String candidate = ((CTText)o).getStringValue();
/*  929:1485 */               if (runPos == startRun) {
/*  930:1486 */                 charPos = startChar;
/*  931:     */               }
/*  932:1488 */               for (charPos = 0; charPos < candidate.length(); charPos++)
/*  933:     */               {
/*  934:1492 */                 if ((candidate.charAt(charPos) == searched.charAt(0)) && (candCharPos == 0))
/*  935:     */                 {
/*  936:1493 */                   beginTextPos = textPos;
/*  937:1494 */                   beginCharPos = charPos;
/*  938:1495 */                   beginRunPos = runPos;
/*  939:1496 */                   newList = true;
/*  940:     */                 }
/*  941:1498 */                 if (candidate.charAt(charPos) == searched.charAt(candCharPos))
/*  942:     */                 {
/*  943:1499 */                   if (candCharPos + 1 < searched.length())
/*  944:     */                   {
/*  945:1500 */                     candCharPos++;
/*  946:     */                   }
/*  947:1501 */                   else if (newList)
/*  948:     */                   {
/*  949:1502 */                     TextSegement segement = new TextSegement();
/*  950:1503 */                     segement.setBeginRun(beginRunPos);
/*  951:1504 */                     segement.setBeginText(beginTextPos);
/*  952:1505 */                     segement.setBeginChar(beginCharPos);
/*  953:1506 */                     segement.setEndRun(runPos);
/*  954:1507 */                     segement.setEndText(textPos);
/*  955:1508 */                     segement.setEndChar(charPos);
/*  956:1509 */                     return segement;
/*  957:     */                   }
/*  958:     */                 }
/*  959:     */                 else {
/*  960:1512 */                   candCharPos = 0;
/*  961:     */                 }
/*  962:     */               }
/*  963:     */             }
/*  964:1516 */             textPos++;
/*  965:     */           }
/*  966:1517 */           else if ((o instanceof CTProofErr))
/*  967:     */           {
/*  968:1518 */             c.removeXml();
/*  969:     */           }
/*  970:1519 */           else if (!(o instanceof CTRPr))
/*  971:     */           {
/*  972:1522 */             candCharPos = 0;
/*  973:     */           }
/*  974:     */         }
/*  975:     */       }
/*  976:     */       finally
/*  977:     */       {
/*  978:1526 */         c.dispose();
/*  979:     */       }
/*  980:     */     }
/*  981:1529 */     return null;
/*  982:     */   }
/*  983:     */   
/*  984:     */   public String getText(TextSegement segment)
/*  985:     */   {
/*  986:1538 */     int runBegin = segment.getBeginRun();
/*  987:1539 */     int textBegin = segment.getBeginText();
/*  988:1540 */     int charBegin = segment.getBeginChar();
/*  989:1541 */     int runEnd = segment.getEndRun();
/*  990:1542 */     int textEnd = segment.getEndText();
/*  991:1543 */     int charEnd = segment.getEndChar();
/*  992:1544 */     StringBuilder out = new StringBuilder();
/*  993:1545 */     CTR[] rArray = this.paragraph.getRArray();
/*  994:1546 */     for (int i = runBegin; i <= runEnd; i++)
/*  995:     */     {
/*  996:1547 */       CTText[] tArray = rArray[i].getTArray();
/*  997:1548 */       int startText = 0;int endText = tArray.length - 1;
/*  998:1549 */       if (i == runBegin) {
/*  999:1550 */         startText = textBegin;
/* 1000:     */       }
/* 1001:1552 */       if (i == runEnd) {
/* 1002:1553 */         endText = textEnd;
/* 1003:     */       }
/* 1004:1555 */       for (int j = startText; j <= endText; j++)
/* 1005:     */       {
/* 1006:1556 */         String tmpText = tArray[j].getStringValue();
/* 1007:1557 */         int startChar = 0;int endChar = tmpText.length() - 1;
/* 1008:1558 */         if ((j == textBegin) && (i == runBegin)) {
/* 1009:1559 */           startChar = charBegin;
/* 1010:     */         }
/* 1011:1561 */         if ((j == textEnd) && (i == runEnd)) {
/* 1012:1562 */           endChar = charEnd;
/* 1013:     */         }
/* 1014:1564 */         out.append(tmpText.substring(startChar, endChar + 1));
/* 1015:     */       }
/* 1016:     */     }
/* 1017:1567 */     return out.toString();
/* 1018:     */   }
/* 1019:     */   
/* 1020:     */   public boolean removeRun(int pos)
/* 1021:     */   {
/* 1022:1577 */     if ((pos >= 0) && (pos < this.runs.size()))
/* 1023:     */     {
/* 1024:1579 */       XWPFRun run = (XWPFRun)this.runs.get(pos);
/* 1025:1580 */       if (((run instanceof XWPFHyperlinkRun)) || ((run instanceof XWPFFieldRun))) {
/* 1026:1584 */         throw new IllegalArgumentException("Removing Field or Hyperlink runs not yet supported");
/* 1027:     */       }
/* 1028:1586 */       this.runs.remove(pos);
/* 1029:1587 */       this.iruns.remove(run);
/* 1030:     */       
/* 1031:     */ 
/* 1032:1590 */       int rPos = 0;
/* 1033:1591 */       for (int i = 0; i < pos; i++)
/* 1034:     */       {
/* 1035:1592 */         XWPFRun currRun = (XWPFRun)this.runs.get(i);
/* 1036:1593 */         if ((!(currRun instanceof XWPFHyperlinkRun)) && (!(currRun instanceof XWPFFieldRun))) {
/* 1037:1594 */           rPos++;
/* 1038:     */         }
/* 1039:     */       }
/* 1040:1597 */       getCTP().removeR(rPos);
/* 1041:1598 */       return true;
/* 1042:     */     }
/* 1043:1600 */     return false;
/* 1044:     */   }
/* 1045:     */   
/* 1046:     */   public BodyElementType getElementType()
/* 1047:     */   {
/* 1048:1610 */     return BodyElementType.PARAGRAPH;
/* 1049:     */   }
/* 1050:     */   
/* 1051:     */   public IBody getBody()
/* 1052:     */   {
/* 1053:1615 */     return this.part;
/* 1054:     */   }
/* 1055:     */   
/* 1056:     */   public POIXMLDocumentPart getPart()
/* 1057:     */   {
/* 1058:1625 */     if (this.part != null) {
/* 1059:1626 */       return this.part.getPart();
/* 1060:     */     }
/* 1061:1628 */     return null;
/* 1062:     */   }
/* 1063:     */   
/* 1064:     */   public BodyType getPartType()
/* 1065:     */   {
/* 1066:1638 */     return this.part.getPartType();
/* 1067:     */   }
/* 1068:     */   
/* 1069:     */   public void addRun(XWPFRun r)
/* 1070:     */   {
/* 1071:1647 */     if (!this.runs.contains(r)) {
/* 1072:1648 */       this.runs.add(r);
/* 1073:     */     }
/* 1074:     */   }
/* 1075:     */   
/* 1076:     */   public XWPFRun getRun(CTR r)
/* 1077:     */   {
/* 1078:1658 */     for (int i = 0; i < getRuns().size(); i++) {
/* 1079:1659 */       if (((XWPFRun)getRuns().get(i)).getCTR() == r) {
/* 1080:1660 */         return (XWPFRun)getRuns().get(i);
/* 1081:     */       }
/* 1082:     */     }
/* 1083:1663 */     return null;
/* 1084:     */   }
/* 1085:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFParagraph
 * JD-Core Version:    0.7.0.1
 */