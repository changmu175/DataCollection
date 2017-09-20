/*    1:     */ package org.apache.poi.xwpf.usermodel;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.InputStream;
/*    5:     */ import java.io.StringReader;
/*    6:     */ import java.math.BigInteger;
/*    7:     */ import java.util.ArrayList;
/*    8:     */ import java.util.Arrays;
/*    9:     */ import java.util.List;
/*   10:     */ import javax.xml.namespace.QName;
/*   11:     */ import org.apache.poi.POIXMLDocumentPart;
/*   12:     */ import org.apache.poi.POIXMLException;
/*   13:     */ import org.apache.poi.POIXMLTypeLoader;
/*   14:     */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   15:     */ import org.apache.poi.util.DocumentHelper;
/*   16:     */ import org.apache.poi.util.IdentifierManager;
/*   17:     */ import org.apache.poi.util.Internal;
/*   18:     */ import org.apache.poi.wp.usermodel.CharacterRun;
/*   19:     */ import org.apache.xmlbeans.SchemaType;
/*   20:     */ import org.apache.xmlbeans.XmlCursor;
/*   21:     */ import org.apache.xmlbeans.XmlException;
/*   22:     */ import org.apache.xmlbeans.XmlObject;
/*   23:     */ import org.apache.xmlbeans.XmlString;
/*   24:     */ import org.apache.xmlbeans.XmlToken.Factory;
/*   25:     */ import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
/*   26:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*   27:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*   28:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
/*   29:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
/*   30:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*   31:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties;
/*   32:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking;
/*   33:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*   34:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*   35:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*   36:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*   37:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties;
/*   38:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*   39:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
/*   40:     */ import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
/*   41:     */ import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture.Factory;
/*   42:     */ import org.openxmlformats.schemas.drawingml.x2006.picture.CTPictureNonVisual;
/*   43:     */ import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
/*   44:     */ import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
/*   45:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
/*   46:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
/*   47:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
/*   48:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
/*   49:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox;
/*   50:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData;
/*   51:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
/*   52:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
/*   53:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef;
/*   54:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight;
/*   55:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
/*   56:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*   57:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
/*   58:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*   59:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*   60:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby;
/*   61:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent;
/*   62:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure;
/*   63:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure;
/*   64:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
/*   65:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;
/*   66:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun;
/*   67:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrClear.Enum;
/*   68:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum;
/*   69:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
/*   70:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
/*   71:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;
/*   72:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*   73:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.Enum;
/*   74:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline.Enum;
/*   75:     */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalAlignRun.Enum;
/*   76:     */ import org.w3c.dom.Document;
/*   77:     */ import org.w3c.dom.Node;
/*   78:     */ import org.w3c.dom.NodeList;
/*   79:     */ import org.w3c.dom.Text;
/*   80:     */ import org.xml.sax.InputSource;
/*   81:     */ import org.xml.sax.SAXException;
/*   82:     */ 
/*   83:     */ public class XWPFRun
/*   84:     */   implements ISDTContents, IRunElement, CharacterRun
/*   85:     */ {
/*   86:     */   private CTR run;
/*   87:     */   private String pictureText;
/*   88:     */   private IRunBody parent;
/*   89:     */   private List<XWPFPicture> pictures;
/*   90:     */   
/*   91:     */   public XWPFRun(CTR r, IRunBody p)
/*   92:     */   {
/*   93: 104 */     this.run = r;
/*   94: 105 */     this.parent = p;
/*   95: 111 */     for (CTDrawing ctDrawing : r.getDrawingArray())
/*   96:     */     {
/*   97: 112 */       for (CTAnchor anchor : ctDrawing.getAnchorArray()) {
/*   98: 113 */         if (anchor.getDocPr() != null) {
/*   99: 114 */           getDocument().getDrawingIdManager().reserve(anchor.getDocPr().getId());
/*  100:     */         }
/*  101:     */       }
/*  102: 117 */       for (CTInline inline : ctDrawing.getInlineArray()) {
/*  103: 118 */         if (inline.getDocPr() != null) {
/*  104: 119 */           getDocument().getDrawingIdManager().reserve(inline.getDocPr().getId());
/*  105:     */         }
/*  106:     */       }
/*  107:     */     }
/*  108: 125 */     StringBuilder text = new StringBuilder();
/*  109: 126 */     List<XmlObject> pictTextObjs = new ArrayList();
/*  110: 127 */     pictTextObjs.addAll(Arrays.asList(r.getPictArray()));
/*  111: 128 */     pictTextObjs.addAll(Arrays.asList(r.getDrawingArray()));
/*  112: 129 */     for (XmlObject o : pictTextObjs)
/*  113:     */     {
/*  114: 130 */       XmlObject[] ts = o.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//w:t");
/*  115: 131 */       for (XmlObject t : ts)
/*  116:     */       {
/*  117: 132 */         NodeList kids = t.getDomNode().getChildNodes();
/*  118: 133 */         for (int n = 0; n < kids.getLength(); n++) {
/*  119: 134 */           if ((kids.item(n) instanceof Text))
/*  120:     */           {
/*  121: 135 */             if (text.length() > 0) {
/*  122: 136 */               text.append("\n");
/*  123:     */             }
/*  124: 137 */             text.append(kids.item(n).getNodeValue());
/*  125:     */           }
/*  126:     */         }
/*  127:     */       }
/*  128:     */     }
/*  129: 142 */     this.pictureText = text.toString();
/*  130:     */     
/*  131:     */ 
/*  132:     */ 
/*  133: 146 */     this.pictures = new ArrayList();
/*  134: 147 */     for (XmlObject o : pictTextObjs) {
/*  135: 148 */       for (CTPicture pict : getCTPictures(o))
/*  136:     */       {
/*  137: 149 */         XWPFPicture picture = new XWPFPicture(pict, this);
/*  138: 150 */         this.pictures.add(picture);
/*  139:     */       }
/*  140:     */     }
/*  141:     */   }
/*  142:     */   
/*  143:     */   /**
/*  144:     */    * @deprecated
/*  145:     */    */
/*  146:     */   public XWPFRun(CTR r, XWPFParagraph p)
/*  147:     */   {
/*  148: 159 */     this(r, p);
/*  149:     */   }
/*  150:     */   
/*  151:     */   static void preserveSpaces(XmlString xs)
/*  152:     */   {
/*  153: 168 */     String text = xs.getStringValue();
/*  154: 169 */     if ((text != null) && ((text.startsWith(" ")) || (text.endsWith(" "))))
/*  155:     */     {
/*  156: 170 */       XmlCursor c = xs.newCursor();
/*  157: 171 */       c.toNextToken();
/*  158: 172 */       c.insertAttributeWithValue(new QName("http://www.w3.org/XML/1998/namespace", "space"), "preserve");
/*  159: 173 */       c.dispose();
/*  160:     */     }
/*  161:     */   }
/*  162:     */   
/*  163:     */   private List<CTPicture> getCTPictures(XmlObject o)
/*  164:     */   {
/*  165: 178 */     List<CTPicture> pics = new ArrayList();
/*  166: 179 */     XmlObject[] picts = o.selectPath("declare namespace pic='" + CTPicture.type.getName().getNamespaceURI() + "' .//pic:pic");
/*  167: 180 */     for (XmlObject pict : picts)
/*  168:     */     {
/*  169: 181 */       if ((pict instanceof XmlAnyTypeImpl)) {
/*  170:     */         try
/*  171:     */         {
/*  172: 184 */           pict = CTPicture.Factory.parse(pict.toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  173:     */         }
/*  174:     */         catch (XmlException e)
/*  175:     */         {
/*  176: 186 */           throw new POIXMLException(e);
/*  177:     */         }
/*  178:     */       }
/*  179: 189 */       if ((pict instanceof CTPicture)) {
/*  180: 190 */         pics.add((CTPicture)pict);
/*  181:     */       }
/*  182:     */     }
/*  183: 193 */     return pics;
/*  184:     */   }
/*  185:     */   
/*  186:     */   @Internal
/*  187:     */   public CTR getCTR()
/*  188:     */   {
/*  189: 203 */     return this.run;
/*  190:     */   }
/*  191:     */   
/*  192:     */   public IRunBody getParent()
/*  193:     */   {
/*  194: 212 */     return this.parent;
/*  195:     */   }
/*  196:     */   
/*  197:     */   /**
/*  198:     */    * @deprecated
/*  199:     */    */
/*  200:     */   public XWPFParagraph getParagraph()
/*  201:     */   {
/*  202: 221 */     if ((this.parent instanceof XWPFParagraph)) {
/*  203: 222 */       return (XWPFParagraph)this.parent;
/*  204:     */     }
/*  205: 223 */     return null;
/*  206:     */   }
/*  207:     */   
/*  208:     */   public XWPFDocument getDocument()
/*  209:     */   {
/*  210: 231 */     if (this.parent != null) {
/*  211: 232 */       return this.parent.getDocument();
/*  212:     */     }
/*  213: 234 */     return null;
/*  214:     */   }
/*  215:     */   
/*  216:     */   private static boolean isCTOnOff(CTOnOff onoff)
/*  217:     */   {
/*  218: 241 */     if (!onoff.isSetVal()) {
/*  219: 242 */       return true;
/*  220:     */     }
/*  221: 243 */     STOnOff.Enum val = onoff.getVal();
/*  222: 244 */     return (STOnOff.TRUE == val) || (STOnOff.X_1 == val) || (STOnOff.ON == val);
/*  223:     */   }
/*  224:     */   
/*  225:     */   public boolean isBold()
/*  226:     */   {
/*  227: 258 */     CTRPr pr = this.run.getRPr();
/*  228: 259 */     if ((pr == null) || (!pr.isSetB())) {
/*  229: 260 */       return false;
/*  230:     */     }
/*  231: 262 */     return isCTOnOff(pr.getB());
/*  232:     */   }
/*  233:     */   
/*  234:     */   public void setBold(boolean value)
/*  235:     */   {
/*  236: 290 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  237: 291 */     CTOnOff bold = pr.isSetB() ? pr.getB() : pr.addNewB();
/*  238: 292 */     bold.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  239:     */   }
/*  240:     */   
/*  241:     */   public String getColor()
/*  242:     */   {
/*  243: 299 */     String color = null;
/*  244: 300 */     if (this.run.isSetRPr())
/*  245:     */     {
/*  246: 301 */       CTRPr pr = this.run.getRPr();
/*  247: 302 */       if (pr.isSetColor())
/*  248:     */       {
/*  249: 303 */         CTColor clr = pr.getColor();
/*  250: 304 */         color = clr.xgetVal().getStringValue();
/*  251:     */       }
/*  252:     */     }
/*  253: 307 */     return color;
/*  254:     */   }
/*  255:     */   
/*  256:     */   public void setColor(String rgbStr)
/*  257:     */   {
/*  258: 316 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  259: 317 */     CTColor color = pr.isSetColor() ? pr.getColor() : pr.addNewColor();
/*  260: 318 */     color.setVal(rgbStr);
/*  261:     */   }
/*  262:     */   
/*  263:     */   public String getText(int pos)
/*  264:     */   {
/*  265: 327 */     return this.run.sizeOfTArray() == 0 ? null : this.run.getTArray(pos).getStringValue();
/*  266:     */   }
/*  267:     */   
/*  268:     */   public String getPictureText()
/*  269:     */   {
/*  270: 335 */     return this.pictureText;
/*  271:     */   }
/*  272:     */   
/*  273:     */   public void setText(String value)
/*  274:     */   {
/*  275: 344 */     setText(value, this.run.sizeOfTArray());
/*  276:     */   }
/*  277:     */   
/*  278:     */   public void setText(String value, int pos)
/*  279:     */   {
/*  280: 354 */     if (pos > this.run.sizeOfTArray()) {
/*  281: 355 */       throw new ArrayIndexOutOfBoundsException("Value too large for the parameter position in XWPFRun.setText(String value,int pos)");
/*  282:     */     }
/*  283: 356 */     CTText t = (pos < this.run.sizeOfTArray()) && (pos >= 0) ? this.run.getTArray(pos) : this.run.addNewT();
/*  284: 357 */     t.setStringValue(value);
/*  285: 358 */     preserveSpaces(t);
/*  286:     */   }
/*  287:     */   
/*  288:     */   public boolean isItalic()
/*  289:     */   {
/*  290: 368 */     CTRPr pr = this.run.getRPr();
/*  291: 369 */     if ((pr == null) || (!pr.isSetI())) {
/*  292: 370 */       return false;
/*  293:     */     }
/*  294: 371 */     return isCTOnOff(pr.getI());
/*  295:     */   }
/*  296:     */   
/*  297:     */   public void setItalic(boolean value)
/*  298:     */   {
/*  299: 400 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  300: 401 */     CTOnOff italic = pr.isSetI() ? pr.getI() : pr.addNewI();
/*  301: 402 */     italic.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  302:     */   }
/*  303:     */   
/*  304:     */   public UnderlinePatterns getUnderline()
/*  305:     */   {
/*  306: 413 */     CTRPr pr = this.run.getRPr();
/*  307: 414 */     return (pr != null) && (pr.isSetU()) && (pr.getU().getVal() != null) ? UnderlinePatterns.valueOf(pr.getU().getVal().intValue()) : UnderlinePatterns.NONE;
/*  308:     */   }
/*  309:     */   
/*  310:     */   public void setUnderline(UnderlinePatterns value)
/*  311:     */   {
/*  312: 435 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  313: 436 */     CTUnderline underline = pr.getU() == null ? pr.addNewU() : pr.getU();
/*  314: 437 */     underline.setVal(STUnderline.Enum.forInt(value.getValue()));
/*  315:     */   }
/*  316:     */   
/*  317:     */   public boolean isStrikeThrough()
/*  318:     */   {
/*  319: 447 */     CTRPr pr = this.run.getRPr();
/*  320: 448 */     if ((pr == null) || (!pr.isSetStrike())) {
/*  321: 449 */       return false;
/*  322:     */     }
/*  323: 450 */     return isCTOnOff(pr.getStrike());
/*  324:     */   }
/*  325:     */   
/*  326:     */   public void setStrikeThrough(boolean value)
/*  327:     */   {
/*  328: 478 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  329: 479 */     CTOnOff strike = pr.isSetStrike() ? pr.getStrike() : pr.addNewStrike();
/*  330: 480 */     strike.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  331:     */   }
/*  332:     */   
/*  333:     */   @Deprecated
/*  334:     */   public boolean isStrike()
/*  335:     */   {
/*  336: 485 */     return isStrikeThrough();
/*  337:     */   }
/*  338:     */   
/*  339:     */   @Deprecated
/*  340:     */   public void setStrike(boolean value)
/*  341:     */   {
/*  342: 490 */     setStrikeThrough(value);
/*  343:     */   }
/*  344:     */   
/*  345:     */   public boolean isDoubleStrikeThrough()
/*  346:     */   {
/*  347: 500 */     CTRPr pr = this.run.getRPr();
/*  348: 501 */     if ((pr == null) || (!pr.isSetDstrike())) {
/*  349: 502 */       return false;
/*  350:     */     }
/*  351: 503 */     return isCTOnOff(pr.getDstrike());
/*  352:     */   }
/*  353:     */   
/*  354:     */   public void setDoubleStrikethrough(boolean value)
/*  355:     */   {
/*  356: 513 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  357: 514 */     CTOnOff dstrike = pr.isSetDstrike() ? pr.getDstrike() : pr.addNewDstrike();
/*  358: 515 */     dstrike.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  359:     */   }
/*  360:     */   
/*  361:     */   public boolean isSmallCaps()
/*  362:     */   {
/*  363: 519 */     CTRPr pr = this.run.getRPr();
/*  364: 520 */     if ((pr == null) || (!pr.isSetSmallCaps())) {
/*  365: 521 */       return false;
/*  366:     */     }
/*  367: 522 */     return isCTOnOff(pr.getSmallCaps());
/*  368:     */   }
/*  369:     */   
/*  370:     */   public void setSmallCaps(boolean value)
/*  371:     */   {
/*  372: 526 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  373: 527 */     CTOnOff caps = pr.isSetSmallCaps() ? pr.getSmallCaps() : pr.addNewSmallCaps();
/*  374: 528 */     caps.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  375:     */   }
/*  376:     */   
/*  377:     */   public boolean isCapitalized()
/*  378:     */   {
/*  379: 532 */     CTRPr pr = this.run.getRPr();
/*  380: 533 */     if ((pr == null) || (!pr.isSetCaps())) {
/*  381: 534 */       return false;
/*  382:     */     }
/*  383: 535 */     return isCTOnOff(pr.getCaps());
/*  384:     */   }
/*  385:     */   
/*  386:     */   public void setCapitalized(boolean value)
/*  387:     */   {
/*  388: 539 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  389: 540 */     CTOnOff caps = pr.isSetCaps() ? pr.getCaps() : pr.addNewCaps();
/*  390: 541 */     caps.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  391:     */   }
/*  392:     */   
/*  393:     */   public boolean isShadowed()
/*  394:     */   {
/*  395: 545 */     CTRPr pr = this.run.getRPr();
/*  396: 546 */     if ((pr == null) || (!pr.isSetShadow())) {
/*  397: 547 */       return false;
/*  398:     */     }
/*  399: 548 */     return isCTOnOff(pr.getShadow());
/*  400:     */   }
/*  401:     */   
/*  402:     */   public void setShadow(boolean value)
/*  403:     */   {
/*  404: 552 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  405: 553 */     CTOnOff shadow = pr.isSetShadow() ? pr.getShadow() : pr.addNewShadow();
/*  406: 554 */     shadow.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  407:     */   }
/*  408:     */   
/*  409:     */   public boolean isImprinted()
/*  410:     */   {
/*  411: 558 */     CTRPr pr = this.run.getRPr();
/*  412: 559 */     if ((pr == null) || (!pr.isSetImprint())) {
/*  413: 560 */       return false;
/*  414:     */     }
/*  415: 561 */     return isCTOnOff(pr.getImprint());
/*  416:     */   }
/*  417:     */   
/*  418:     */   public void setImprinted(boolean value)
/*  419:     */   {
/*  420: 565 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  421: 566 */     CTOnOff imprinted = pr.isSetImprint() ? pr.getImprint() : pr.addNewImprint();
/*  422: 567 */     imprinted.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  423:     */   }
/*  424:     */   
/*  425:     */   public boolean isEmbossed()
/*  426:     */   {
/*  427: 571 */     CTRPr pr = this.run.getRPr();
/*  428: 572 */     if ((pr == null) || (!pr.isSetEmboss())) {
/*  429: 573 */       return false;
/*  430:     */     }
/*  431: 574 */     return isCTOnOff(pr.getEmboss());
/*  432:     */   }
/*  433:     */   
/*  434:     */   public void setEmbossed(boolean value)
/*  435:     */   {
/*  436: 578 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  437: 579 */     CTOnOff emboss = pr.isSetEmboss() ? pr.getEmboss() : pr.addNewEmboss();
/*  438: 580 */     emboss.setVal(value ? STOnOff.TRUE : STOnOff.FALSE);
/*  439:     */   }
/*  440:     */   
/*  441:     */   public VerticalAlign getSubscript()
/*  442:     */   {
/*  443: 593 */     CTRPr pr = this.run.getRPr();
/*  444: 594 */     return (pr != null) && (pr.isSetVertAlign()) ? VerticalAlign.valueOf(pr.getVertAlign().getVal().intValue()) : VerticalAlign.BASELINE;
/*  445:     */   }
/*  446:     */   
/*  447:     */   public void setSubscript(VerticalAlign valign)
/*  448:     */   {
/*  449: 614 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  450: 615 */     CTVerticalAlignRun ctValign = pr.isSetVertAlign() ? pr.getVertAlign() : pr.addNewVertAlign();
/*  451: 616 */     ctValign.setVal(STVerticalAlignRun.Enum.forInt(valign.getValue()));
/*  452:     */   }
/*  453:     */   
/*  454:     */   public int getKerning()
/*  455:     */   {
/*  456: 620 */     CTRPr pr = this.run.getRPr();
/*  457: 621 */     if ((pr == null) || (!pr.isSetKern())) {
/*  458: 622 */       return 0;
/*  459:     */     }
/*  460: 623 */     return pr.getKern().getVal().intValue();
/*  461:     */   }
/*  462:     */   
/*  463:     */   public void setKerning(int kern)
/*  464:     */   {
/*  465: 627 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  466: 628 */     CTHpsMeasure kernmes = pr.isSetKern() ? pr.getKern() : pr.addNewKern();
/*  467: 629 */     kernmes.setVal(BigInteger.valueOf(kern));
/*  468:     */   }
/*  469:     */   
/*  470:     */   public boolean isHighlighted()
/*  471:     */   {
/*  472: 633 */     CTRPr pr = this.run.getRPr();
/*  473: 634 */     if ((pr == null) || (!pr.isSetHighlight())) {
/*  474: 635 */       return false;
/*  475:     */     }
/*  476: 636 */     if (pr.getHighlight().getVal() == STHighlightColor.NONE) {
/*  477: 637 */       return false;
/*  478:     */     }
/*  479: 638 */     return true;
/*  480:     */   }
/*  481:     */   
/*  482:     */   public int getCharacterSpacing()
/*  483:     */   {
/*  484: 644 */     CTRPr pr = this.run.getRPr();
/*  485: 645 */     if ((pr == null) || (!pr.isSetSpacing())) {
/*  486: 646 */       return 0;
/*  487:     */     }
/*  488: 647 */     return pr.getSpacing().getVal().intValue();
/*  489:     */   }
/*  490:     */   
/*  491:     */   public void setCharacterSpacing(int twips)
/*  492:     */   {
/*  493: 651 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  494: 652 */     CTSignedTwipsMeasure spc = pr.isSetSpacing() ? pr.getSpacing() : pr.addNewSpacing();
/*  495: 653 */     spc.setVal(BigInteger.valueOf(twips));
/*  496:     */   }
/*  497:     */   
/*  498:     */   public String getFontFamily()
/*  499:     */   {
/*  500: 664 */     return getFontFamily(null);
/*  501:     */   }
/*  502:     */   
/*  503:     */   public void setFontFamily(String fontFamily)
/*  504:     */   {
/*  505: 678 */     setFontFamily(fontFamily, null);
/*  506:     */   }
/*  507:     */   
/*  508:     */   public String getFontName()
/*  509:     */   {
/*  510: 685 */     return getFontFamily();
/*  511:     */   }
/*  512:     */   
/*  513:     */   public String getFontFamily(FontCharRange fcr)
/*  514:     */   {
/*  515: 696 */     CTRPr pr = this.run.getRPr();
/*  516: 697 */     if ((pr == null) || (!pr.isSetRFonts())) {
/*  517: 697 */       return null;
/*  518:     */     }
/*  519: 699 */     CTFonts fonts = pr.getRFonts();
/*  520: 700 */     switch (1.$SwitchMap$org$apache$poi$xwpf$usermodel$XWPFRun$FontCharRange[fcr.ordinal()])
/*  521:     */     {
/*  522:     */     case 1: 
/*  523:     */     default: 
/*  524: 703 */       return fonts.getAscii();
/*  525:     */     case 2: 
/*  526: 705 */       return fonts.getCs();
/*  527:     */     case 3: 
/*  528: 707 */       return fonts.getEastAsia();
/*  529:     */     }
/*  530: 709 */     return fonts.getHAnsi();
/*  531:     */   }
/*  532:     */   
/*  533:     */   public void setFontFamily(String fontFamily, FontCharRange fcr)
/*  534:     */   {
/*  535: 723 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  536: 724 */     CTFonts fonts = pr.isSetRFonts() ? pr.getRFonts() : pr.addNewRFonts();
/*  537: 726 */     if (fcr == null)
/*  538:     */     {
/*  539: 727 */       fonts.setAscii(fontFamily);
/*  540: 728 */       if (!fonts.isSetHAnsi()) {
/*  541: 729 */         fonts.setHAnsi(fontFamily);
/*  542:     */       }
/*  543: 731 */       if (!fonts.isSetCs()) {
/*  544: 732 */         fonts.setCs(fontFamily);
/*  545:     */       }
/*  546: 734 */       if (!fonts.isSetEastAsia()) {
/*  547: 735 */         fonts.setEastAsia(fontFamily);
/*  548:     */       }
/*  549:     */     }
/*  550:     */     else
/*  551:     */     {
/*  552: 738 */       switch (1.$SwitchMap$org$apache$poi$xwpf$usermodel$XWPFRun$FontCharRange[fcr.ordinal()])
/*  553:     */       {
/*  554:     */       case 1: 
/*  555: 740 */         fonts.setAscii(fontFamily);
/*  556: 741 */         break;
/*  557:     */       case 2: 
/*  558: 743 */         fonts.setCs(fontFamily);
/*  559: 744 */         break;
/*  560:     */       case 3: 
/*  561: 746 */         fonts.setEastAsia(fontFamily);
/*  562: 747 */         break;
/*  563:     */       case 4: 
/*  564: 749 */         fonts.setHAnsi(fontFamily);
/*  565:     */       }
/*  566:     */     }
/*  567:     */   }
/*  568:     */   
/*  569:     */   public int getFontSize()
/*  570:     */   {
/*  571: 762 */     CTRPr pr = this.run.getRPr();
/*  572: 763 */     return (pr != null) && (pr.isSetSz()) ? pr.getSz().getVal().divide(new BigInteger("2")).intValue() : -1;
/*  573:     */   }
/*  574:     */   
/*  575:     */   public void setFontSize(int size)
/*  576:     */   {
/*  577: 779 */     BigInteger bint = new BigInteger("" + size);
/*  578: 780 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  579: 781 */     CTHpsMeasure ctSize = pr.isSetSz() ? pr.getSz() : pr.addNewSz();
/*  580: 782 */     ctSize.setVal(bint.multiply(new BigInteger("2")));
/*  581:     */   }
/*  582:     */   
/*  583:     */   public int getTextPosition()
/*  584:     */   {
/*  585: 794 */     CTRPr pr = this.run.getRPr();
/*  586: 795 */     return (pr != null) && (pr.isSetPosition()) ? pr.getPosition().getVal().intValue() : -1;
/*  587:     */   }
/*  588:     */   
/*  589:     */   public void setTextPosition(int val)
/*  590:     */   {
/*  591: 822 */     BigInteger bint = new BigInteger("" + val);
/*  592: 823 */     CTRPr pr = this.run.isSetRPr() ? this.run.getRPr() : this.run.addNewRPr();
/*  593: 824 */     CTSignedHpsMeasure position = pr.isSetPosition() ? pr.getPosition() : pr.addNewPosition();
/*  594: 825 */     position.setVal(bint);
/*  595:     */   }
/*  596:     */   
/*  597:     */   public void removeBreak() {}
/*  598:     */   
/*  599:     */   public void addBreak()
/*  600:     */   {
/*  601: 845 */     this.run.addNewBr();
/*  602:     */   }
/*  603:     */   
/*  604:     */   public void addBreak(BreakType type)
/*  605:     */   {
/*  606: 863 */     CTBr br = this.run.addNewBr();
/*  607: 864 */     br.setType(STBrType.Enum.forInt(type.getValue()));
/*  608:     */   }
/*  609:     */   
/*  610:     */   public void addBreak(BreakClear clear)
/*  611:     */   {
/*  612: 881 */     CTBr br = this.run.addNewBr();
/*  613: 882 */     br.setType(STBrType.Enum.forInt(BreakType.TEXT_WRAPPING.getValue()));
/*  614: 883 */     br.setClear(STBrClear.Enum.forInt(clear.getValue()));
/*  615:     */   }
/*  616:     */   
/*  617:     */   public void addTab()
/*  618:     */   {
/*  619: 891 */     this.run.addNewTab();
/*  620:     */   }
/*  621:     */   
/*  622:     */   public void removeTab() {}
/*  623:     */   
/*  624:     */   public void addCarriageReturn()
/*  625:     */   {
/*  626: 911 */     this.run.addNewCr();
/*  627:     */   }
/*  628:     */   
/*  629:     */   public void removeCarriageReturn() {}
/*  630:     */   
/*  631:     */   public XWPFPicture addPicture(InputStream pictureData, int pictureType, String filename, int width, int height)
/*  632:     */     throws InvalidFormatException, IOException
/*  633:     */   {
/*  634:     */     XWPFPictureData picData;
/*  635:     */     XWPFPictureData picData;
/*  636: 943 */     if ((this.parent.getPart() instanceof XWPFHeaderFooter))
/*  637:     */     {
/*  638: 944 */       XWPFHeaderFooter headerFooter = (XWPFHeaderFooter)this.parent.getPart();
/*  639: 945 */       String relationId = headerFooter.addPictureData(pictureData, pictureType);
/*  640: 946 */       picData = (XWPFPictureData)headerFooter.getRelationById(relationId);
/*  641:     */     }
/*  642:     */     else
/*  643:     */     {
/*  644: 949 */       XWPFDocument doc = this.parent.getDocument();
/*  645: 950 */       String relationId = doc.addPictureData(pictureData, pictureType);
/*  646: 951 */       picData = (XWPFPictureData)doc.getRelationById(relationId);
/*  647:     */     }
/*  648:     */     try
/*  649:     */     {
/*  650: 956 */       CTDrawing drawing = this.run.addNewDrawing();
/*  651: 957 */       CTInline inline = drawing.addNewInline();
/*  652:     */       
/*  653:     */ 
/*  654:     */ 
/*  655: 961 */       String xml = "<a:graphic xmlns:a=\"" + CTGraphicalObject.type.getName().getNamespaceURI() + "\">" + "<a:graphicData uri=\"" + CTPicture.type.getName().getNamespaceURI() + "\">" + "<pic:pic xmlns:pic=\"" + CTPicture.type.getName().getNamespaceURI() + "\" />" + "</a:graphicData>" + "</a:graphic>";
/*  656:     */       
/*  657:     */ 
/*  658:     */ 
/*  659:     */ 
/*  660:     */ 
/*  661: 967 */       InputSource is = new InputSource(new StringReader(xml));
/*  662: 968 */       Document doc = DocumentHelper.readDocument(is);
/*  663: 969 */       inline.set(XmlToken.Factory.parse(doc.getDocumentElement(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS));
/*  664:     */       
/*  665:     */ 
/*  666: 972 */       inline.setDistT(0L);
/*  667: 973 */       inline.setDistR(0L);
/*  668: 974 */       inline.setDistB(0L);
/*  669: 975 */       inline.setDistL(0L);
/*  670:     */       
/*  671: 977 */       CTNonVisualDrawingProps docPr = inline.addNewDocPr();
/*  672: 978 */       long id = getParent().getDocument().getDrawingIdManager().reserveNew();
/*  673: 979 */       docPr.setId(id);
/*  674:     */       
/*  675: 981 */       docPr.setName("Drawing " + id);
/*  676: 982 */       docPr.setDescr(filename);
/*  677:     */       
/*  678: 984 */       CTPositiveSize2D extent = inline.addNewExtent();
/*  679: 985 */       extent.setCx(width);
/*  680: 986 */       extent.setCy(height);
/*  681:     */       
/*  682:     */ 
/*  683: 989 */       CTGraphicalObject graphic = inline.getGraphic();
/*  684: 990 */       CTGraphicalObjectData graphicData = graphic.getGraphicData();
/*  685: 991 */       CTPicture pic = (CTPicture)getCTPictures(graphicData).get(0);
/*  686:     */       
/*  687:     */ 
/*  688: 994 */       CTPictureNonVisual nvPicPr = pic.addNewNvPicPr();
/*  689:     */       
/*  690: 996 */       CTNonVisualDrawingProps cNvPr = nvPicPr.addNewCNvPr();
/*  691:     */       
/*  692: 998 */       cNvPr.setId(0L);
/*  693:     */       
/*  694:1000 */       cNvPr.setName("Picture " + id);
/*  695:1001 */       cNvPr.setDescr(filename);
/*  696:     */       
/*  697:1003 */       CTNonVisualPictureProperties cNvPicPr = nvPicPr.addNewCNvPicPr();
/*  698:1004 */       cNvPicPr.addNewPicLocks().setNoChangeAspect(true);
/*  699:     */       
/*  700:1006 */       CTBlipFillProperties blipFill = pic.addNewBlipFill();
/*  701:1007 */       CTBlip blip = blipFill.addNewBlip();
/*  702:1008 */       blip.setEmbed(this.parent.getPart().getRelationId(picData));
/*  703:1009 */       blipFill.addNewStretch().addNewFillRect();
/*  704:     */       
/*  705:1011 */       CTShapeProperties spPr = pic.addNewSpPr();
/*  706:1012 */       CTTransform2D xfrm = spPr.addNewXfrm();
/*  707:     */       
/*  708:1014 */       CTPoint2D off = xfrm.addNewOff();
/*  709:1015 */       off.setX(0L);
/*  710:1016 */       off.setY(0L);
/*  711:     */       
/*  712:1018 */       CTPositiveSize2D ext = xfrm.addNewExt();
/*  713:1019 */       ext.setCx(width);
/*  714:1020 */       ext.setCy(height);
/*  715:     */       
/*  716:1022 */       CTPresetGeometry2D prstGeom = spPr.addNewPrstGeom();
/*  717:1023 */       prstGeom.setPrst(STShapeType.RECT);
/*  718:1024 */       prstGeom.addNewAvLst();
/*  719:     */       
/*  720:     */ 
/*  721:1027 */       XWPFPicture xwpfPicture = new XWPFPicture(pic, this);
/*  722:1028 */       this.pictures.add(xwpfPicture);
/*  723:1029 */       return xwpfPicture;
/*  724:     */     }
/*  725:     */     catch (XmlException e)
/*  726:     */     {
/*  727:1031 */       throw new IllegalStateException(e);
/*  728:     */     }
/*  729:     */     catch (SAXException e)
/*  730:     */     {
/*  731:1033 */       throw new IllegalStateException(e);
/*  732:     */     }
/*  733:     */   }
/*  734:     */   
/*  735:     */   public List<XWPFPicture> getEmbeddedPictures()
/*  736:     */   {
/*  737:1043 */     return this.pictures;
/*  738:     */   }
/*  739:     */   
/*  740:     */   public String toString()
/*  741:     */   {
/*  742:1050 */     String phonetic = getPhonetic();
/*  743:1051 */     if (phonetic.length() > 0) {
/*  744:1052 */       return text() + " (" + phonetic.toString() + ")";
/*  745:     */     }
/*  746:1054 */     return text();
/*  747:     */   }
/*  748:     */   
/*  749:     */   public String text()
/*  750:     */   {
/*  751:1063 */     StringBuffer text = new StringBuffer();
/*  752:     */     
/*  753:     */ 
/*  754:     */ 
/*  755:1067 */     XmlCursor c = this.run.newCursor();
/*  756:1068 */     c.selectPath("./*");
/*  757:1069 */     while (c.toNextSelection())
/*  758:     */     {
/*  759:1070 */       XmlObject o = c.getObject();
/*  760:1071 */       if ((o instanceof CTRuby)) {
/*  761:1072 */         handleRuby(o, text, false);
/*  762:     */       } else {
/*  763:1075 */         _getText(o, text);
/*  764:     */       }
/*  765:     */     }
/*  766:1077 */     c.dispose();
/*  767:1078 */     return text.toString();
/*  768:     */   }
/*  769:     */   
/*  770:     */   public String getPhonetic()
/*  771:     */   {
/*  772:1087 */     StringBuffer text = new StringBuffer();
/*  773:     */     
/*  774:     */ 
/*  775:     */ 
/*  776:1091 */     XmlCursor c = this.run.newCursor();
/*  777:1092 */     c.selectPath("./*");
/*  778:1093 */     while (c.toNextSelection())
/*  779:     */     {
/*  780:1094 */       XmlObject o = c.getObject();
/*  781:1095 */       if ((o instanceof CTRuby)) {
/*  782:1096 */         handleRuby(o, text, true);
/*  783:     */       }
/*  784:     */     }
/*  785:1100 */     if ((this.pictureText != null) && (this.pictureText.length() > 0)) {
/*  786:1101 */       text.append("\n").append(this.pictureText).append("\n");
/*  787:     */     }
/*  788:1103 */     c.dispose();
/*  789:1104 */     return text.toString();
/*  790:     */   }
/*  791:     */   
/*  792:     */   private void handleRuby(XmlObject rubyObj, StringBuffer text, boolean extractPhonetic)
/*  793:     */   {
/*  794:1114 */     XmlCursor c = rubyObj.newCursor();
/*  795:     */     
/*  796:     */ 
/*  797:     */ 
/*  798:     */ 
/*  799:     */ 
/*  800:1120 */     c.selectPath(".//*");
/*  801:1121 */     boolean inRT = false;
/*  802:1122 */     boolean inBase = false;
/*  803:1123 */     while (c.toNextSelection())
/*  804:     */     {
/*  805:1124 */       XmlObject o = c.getObject();
/*  806:1125 */       if ((o instanceof CTRubyContent))
/*  807:     */       {
/*  808:1126 */         String tagName = o.getDomNode().getNodeName();
/*  809:1127 */         if ("w:rt".equals(tagName))
/*  810:     */         {
/*  811:1128 */           inRT = true;
/*  812:     */         }
/*  813:1129 */         else if ("w:rubyBase".equals(tagName))
/*  814:     */         {
/*  815:1130 */           inRT = false;
/*  816:1131 */           inBase = true;
/*  817:     */         }
/*  818:     */       }
/*  819:1134 */       else if ((extractPhonetic) && (inRT))
/*  820:     */       {
/*  821:1135 */         _getText(o, text);
/*  822:     */       }
/*  823:1136 */       else if ((!extractPhonetic) && (inBase))
/*  824:     */       {
/*  825:1137 */         _getText(o, text);
/*  826:     */       }
/*  827:     */     }
/*  828:1141 */     c.dispose();
/*  829:     */   }
/*  830:     */   
/*  831:     */   private void _getText(XmlObject o, StringBuffer text)
/*  832:     */   {
/*  833:1146 */     if ((o instanceof CTText))
/*  834:     */     {
/*  835:1147 */       String tagName = o.getDomNode().getNodeName();
/*  836:1151 */       if (!"w:instrText".equals(tagName)) {
/*  837:1152 */         text.append(((CTText)o).getStringValue());
/*  838:     */       }
/*  839:     */     }
/*  840:1157 */     if ((o instanceof CTFldChar))
/*  841:     */     {
/*  842:1158 */       CTFldChar ctfldChar = (CTFldChar)o;
/*  843:1159 */       if ((ctfldChar.getFldCharType() == STFldCharType.BEGIN) && 
/*  844:1160 */         (ctfldChar.getFfData() != null)) {
/*  845:1161 */         for (CTFFCheckBox checkBox : ctfldChar.getFfData().getCheckBoxList()) {
/*  846:1162 */           if ((checkBox.getDefault() != null) && (checkBox.getDefault().getVal() == STOnOff.X_1)) {
/*  847:1163 */             text.append("|X|");
/*  848:     */           } else {
/*  849:1165 */             text.append("|_|");
/*  850:     */           }
/*  851:     */         }
/*  852:     */       }
/*  853:     */     }
/*  854:1172 */     if ((o instanceof CTPTab)) {
/*  855:1173 */       text.append("\t");
/*  856:     */     }
/*  857:1175 */     if ((o instanceof CTBr)) {
/*  858:1176 */       text.append("\n");
/*  859:     */     }
/*  860:1178 */     if ((o instanceof CTEmpty))
/*  861:     */     {
/*  862:1184 */       String tagName = o.getDomNode().getNodeName();
/*  863:1185 */       if (("w:tab".equals(tagName)) || ("tab".equals(tagName))) {
/*  864:1186 */         text.append("\t");
/*  865:     */       }
/*  866:1188 */       if (("w:br".equals(tagName)) || ("br".equals(tagName))) {
/*  867:1189 */         text.append("\n");
/*  868:     */       }
/*  869:1191 */       if (("w:cr".equals(tagName)) || ("cr".equals(tagName))) {
/*  870:1192 */         text.append("\n");
/*  871:     */       }
/*  872:     */     }
/*  873:1195 */     if ((o instanceof CTFtnEdnRef))
/*  874:     */     {
/*  875:1196 */       CTFtnEdnRef ftn = (CTFtnEdnRef)o;
/*  876:1197 */       String footnoteRef = "[endnoteRef:" + ftn.getId().intValue() + "]";
/*  877:     */       
/*  878:1199 */       text.append(footnoteRef);
/*  879:     */     }
/*  880:     */   }
/*  881:     */   
/*  882:     */   public static enum FontCharRange
/*  883:     */   {
/*  884:1207 */     ascii,  cs,  eastAsia,  hAnsi;
/*  885:     */     
/*  886:     */     private FontCharRange() {}
/*  887:     */   }
/*  888:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFRun
 * JD-Core Version:    0.7.0.1
 */