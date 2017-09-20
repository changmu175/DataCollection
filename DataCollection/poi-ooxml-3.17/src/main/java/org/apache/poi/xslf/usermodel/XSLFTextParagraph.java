/*    1:     */ package org.apache.poi.xslf.usermodel;
/*    2:     */ 
/*    3:     */ import java.awt.Color;
/*    4:     */ import java.util.ArrayList;
/*    5:     */ import java.util.Iterator;
/*    6:     */ import java.util.List;
/*    7:     */ import org.apache.poi.sl.draw.DrawPaint;
/*    8:     */ import org.apache.poi.sl.usermodel.AutoNumberingScheme;
/*    9:     */ import org.apache.poi.sl.usermodel.PaintStyle;
/*   10:     */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*   11:     */ import org.apache.poi.sl.usermodel.TextParagraph;
/*   12:     */ import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
/*   13:     */ import org.apache.poi.sl.usermodel.TextParagraph.FontAlign;
/*   14:     */ import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
/*   15:     */ import org.apache.poi.util.Internal;
/*   16:     */ import org.apache.poi.util.Units;
/*   17:     */ import org.apache.poi.xslf.model.ParagraphPropertyFetcher;
/*   18:     */ import org.apache.xmlbeans.XmlCursor;
/*   19:     */ import org.apache.xmlbeans.XmlObject;
/*   20:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
/*   21:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*   22:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
/*   23:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextAutonumberBullet;
/*   24:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*   25:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePercent;
/*   26:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBulletSizePoint;
/*   27:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet;
/*   28:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*   29:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextField;
/*   30:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
/*   31:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
/*   32:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
/*   33:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*   34:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*   35:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing;
/*   36:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing.Factory;
/*   37:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent;
/*   38:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint;
/*   39:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop;
/*   40:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList;
/*   41:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.Enum;
/*   42:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.Enum;
/*   43:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontAlignType.Enum;
/*   44:     */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*   45:     */ import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum;
/*   46:     */ 
/*   47:     */ public class XSLFTextParagraph
/*   48:     */   implements TextParagraph<XSLFShape, XSLFTextParagraph, XSLFTextRun>
/*   49:     */ {
/*   50:     */   private final CTTextParagraph _p;
/*   51:     */   private final List<XSLFTextRun> _runs;
/*   52:     */   private final XSLFTextShape _shape;
/*   53:     */   
/*   54:     */   XSLFTextParagraph(CTTextParagraph p, XSLFTextShape shape)
/*   55:     */   {
/*   56:  71 */     this._p = p;
/*   57:  72 */     this._runs = new ArrayList();
/*   58:  73 */     this._shape = shape;
/*   59:     */     
/*   60:  75 */     XmlCursor c = this._p.newCursor();
/*   61:     */     try
/*   62:     */     {
/*   63:  77 */       if (c.toFirstChild()) {
/*   64:     */         do
/*   65:     */         {
/*   66:  79 */           XmlObject r = c.getObject();
/*   67:  80 */           if ((r instanceof CTTextLineBreak)) {
/*   68:  81 */             this._runs.add(new XSLFLineBreak((CTTextLineBreak)r, this));
/*   69:  82 */           } else if (((r instanceof CTRegularTextRun)) || ((r instanceof CTTextField))) {
/*   70:  83 */             this._runs.add(new XSLFTextRun(r, this));
/*   71:     */           }
/*   72:  85 */         } while (c.toNextSibling());
/*   73:     */       }
/*   74:     */     }
/*   75:     */     finally
/*   76:     */     {
/*   77:  88 */       c.dispose();
/*   78:     */     }
/*   79:     */   }
/*   80:     */   
/*   81:     */   public String getText()
/*   82:     */   {
/*   83:  93 */     StringBuilder out = new StringBuilder();
/*   84:  94 */     for (XSLFTextRun r : this._runs) {
/*   85:  95 */       out.append(r.getRawText());
/*   86:     */     }
/*   87:  97 */     return out.toString();
/*   88:     */   }
/*   89:     */   
/*   90:     */   String getRenderableText()
/*   91:     */   {
/*   92: 101 */     StringBuilder out = new StringBuilder();
/*   93: 102 */     for (XSLFTextRun r : this._runs) {
/*   94: 103 */       out.append(r.getRenderableText());
/*   95:     */     }
/*   96: 105 */     return out.toString();
/*   97:     */   }
/*   98:     */   
/*   99:     */   @Internal
/*  100:     */   public CTTextParagraph getXmlObject()
/*  101:     */   {
/*  102: 110 */     return this._p;
/*  103:     */   }
/*  104:     */   
/*  105:     */   public XSLFTextShape getParentShape()
/*  106:     */   {
/*  107: 114 */     return this._shape;
/*  108:     */   }
/*  109:     */   
/*  110:     */   public List<XSLFTextRun> getTextRuns()
/*  111:     */   {
/*  112: 120 */     return this._runs;
/*  113:     */   }
/*  114:     */   
/*  115:     */   public Iterator<XSLFTextRun> iterator()
/*  116:     */   {
/*  117: 124 */     return this._runs.iterator();
/*  118:     */   }
/*  119:     */   
/*  120:     */   public XSLFTextRun addNewTextRun()
/*  121:     */   {
/*  122: 133 */     CTRegularTextRun r = this._p.addNewR();
/*  123: 134 */     CTTextCharacterProperties rPr = r.addNewRPr();
/*  124: 135 */     rPr.setLang("en-US");
/*  125: 136 */     XSLFTextRun run = newTextRun(r);
/*  126: 137 */     this._runs.add(run);
/*  127: 138 */     return run;
/*  128:     */   }
/*  129:     */   
/*  130:     */   public XSLFTextRun addLineBreak()
/*  131:     */   {
/*  132: 147 */     XSLFLineBreak run = new XSLFLineBreak(this._p.addNewBr(), this);
/*  133: 148 */     CTTextCharacterProperties brProps = run.getRPr(true);
/*  134: 149 */     if (this._runs.size() > 0)
/*  135:     */     {
/*  136: 151 */       CTTextCharacterProperties prevRun = ((XSLFTextRun)this._runs.get(this._runs.size() - 1)).getRPr(true);
/*  137: 152 */       brProps.set(prevRun);
/*  138:     */     }
/*  139: 154 */     this._runs.add(run);
/*  140: 155 */     return run;
/*  141:     */   }
/*  142:     */   
/*  143:     */   public TextParagraph.TextAlign getTextAlign()
/*  144:     */   {
/*  145: 160 */     ParagraphPropertyFetcher<TextParagraph.TextAlign> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  146:     */     {
/*  147:     */       public boolean fetch(CTTextParagraphProperties props)
/*  148:     */       {
/*  149: 162 */         if (props.isSetAlgn())
/*  150:     */         {
/*  151: 163 */           TextParagraph.TextAlign val = TextParagraph.TextAlign.values()[(props.getAlgn().intValue() - 1)];
/*  152: 164 */           setValue(val);
/*  153: 165 */           return true;
/*  154:     */         }
/*  155: 167 */         return false;
/*  156:     */       }
/*  157: 169 */     };
/*  158: 170 */     fetchParagraphProperty(fetcher);
/*  159: 171 */     return (TextParagraph.TextAlign)fetcher.getValue();
/*  160:     */   }
/*  161:     */   
/*  162:     */   public void setTextAlign(TextParagraph.TextAlign align)
/*  163:     */   {
/*  164: 176 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  165: 177 */     if (align == null)
/*  166:     */     {
/*  167: 178 */       if (pr.isSetAlgn()) {
/*  168: 178 */         pr.unsetAlgn();
/*  169:     */       }
/*  170:     */     }
/*  171:     */     else {
/*  172: 180 */       pr.setAlgn(STTextAlignType.Enum.forInt(align.ordinal() + 1));
/*  173:     */     }
/*  174:     */   }
/*  175:     */   
/*  176:     */   public TextParagraph.FontAlign getFontAlign()
/*  177:     */   {
/*  178: 186 */     ParagraphPropertyFetcher<TextParagraph.FontAlign> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  179:     */     {
/*  180:     */       public boolean fetch(CTTextParagraphProperties props)
/*  181:     */       {
/*  182: 188 */         if (props.isSetFontAlgn())
/*  183:     */         {
/*  184: 189 */           TextParagraph.FontAlign val = TextParagraph.FontAlign.values()[(props.getFontAlgn().intValue() - 1)];
/*  185: 190 */           setValue(val);
/*  186: 191 */           return true;
/*  187:     */         }
/*  188: 193 */         return false;
/*  189:     */       }
/*  190: 195 */     };
/*  191: 196 */     fetchParagraphProperty(fetcher);
/*  192: 197 */     return (TextParagraph.FontAlign)fetcher.getValue();
/*  193:     */   }
/*  194:     */   
/*  195:     */   public void setFontAlign(TextParagraph.FontAlign align)
/*  196:     */   {
/*  197: 208 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  198: 209 */     if (align == null)
/*  199:     */     {
/*  200: 210 */       if (pr.isSetFontAlgn()) {
/*  201: 210 */         pr.unsetFontAlgn();
/*  202:     */       }
/*  203:     */     }
/*  204:     */     else {
/*  205: 212 */       pr.setFontAlgn(STTextFontAlignType.Enum.forInt(align.ordinal() + 1));
/*  206:     */     }
/*  207:     */   }
/*  208:     */   
/*  209:     */   public String getBulletFont()
/*  210:     */   {
/*  211: 222 */     ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  212:     */     {
/*  213:     */       public boolean fetch(CTTextParagraphProperties props)
/*  214:     */       {
/*  215: 224 */         if (props.isSetBuFont())
/*  216:     */         {
/*  217: 225 */           setValue(props.getBuFont().getTypeface());
/*  218: 226 */           return true;
/*  219:     */         }
/*  220: 228 */         return false;
/*  221:     */       }
/*  222: 230 */     };
/*  223: 231 */     fetchParagraphProperty(fetcher);
/*  224: 232 */     return (String)fetcher.getValue();
/*  225:     */   }
/*  226:     */   
/*  227:     */   public void setBulletFont(String typeface)
/*  228:     */   {
/*  229: 236 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  230: 237 */     CTTextFont font = pr.isSetBuFont() ? pr.getBuFont() : pr.addNewBuFont();
/*  231: 238 */     font.setTypeface(typeface);
/*  232:     */   }
/*  233:     */   
/*  234:     */   public String getBulletCharacter()
/*  235:     */   {
/*  236: 245 */     ParagraphPropertyFetcher<String> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  237:     */     {
/*  238:     */       public boolean fetch(CTTextParagraphProperties props)
/*  239:     */       {
/*  240: 247 */         if (props.isSetBuChar())
/*  241:     */         {
/*  242: 248 */           setValue(props.getBuChar().getChar());
/*  243: 249 */           return true;
/*  244:     */         }
/*  245: 251 */         return false;
/*  246:     */       }
/*  247: 253 */     };
/*  248: 254 */     fetchParagraphProperty(fetcher);
/*  249: 255 */     return (String)fetcher.getValue();
/*  250:     */   }
/*  251:     */   
/*  252:     */   public void setBulletCharacter(String str)
/*  253:     */   {
/*  254: 259 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  255: 260 */     CTTextCharBullet c = pr.isSetBuChar() ? pr.getBuChar() : pr.addNewBuChar();
/*  256: 261 */     c.setChar(str);
/*  257:     */   }
/*  258:     */   
/*  259:     */   public PaintStyle getBulletFontColor()
/*  260:     */   {
/*  261: 270 */     final XSLFTheme theme = getParentShape().getSheet().getTheme();
/*  262: 271 */     ParagraphPropertyFetcher<Color> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  263:     */     {
/*  264:     */       public boolean fetch(CTTextParagraphProperties props)
/*  265:     */       {
/*  266: 273 */         if (props.isSetBuClr())
/*  267:     */         {
/*  268: 274 */           XSLFColor c = new XSLFColor(props.getBuClr(), theme, null);
/*  269: 275 */           setValue(c.getColor());
/*  270: 276 */           return true;
/*  271:     */         }
/*  272: 278 */         return false;
/*  273:     */       }
/*  274: 280 */     };
/*  275: 281 */     fetchParagraphProperty(fetcher);
/*  276: 282 */     Color col = (Color)fetcher.getValue();
/*  277: 283 */     return col == null ? null : DrawPaint.createSolidPaint(col);
/*  278:     */   }
/*  279:     */   
/*  280:     */   public void setBulletFontColor(Color color)
/*  281:     */   {
/*  282: 287 */     setBulletFontColor(DrawPaint.createSolidPaint(color));
/*  283:     */   }
/*  284:     */   
/*  285:     */   public void setBulletFontColor(PaintStyle color)
/*  286:     */   {
/*  287: 297 */     if (!(color instanceof PaintStyle.SolidPaint)) {
/*  288: 298 */       throw new IllegalArgumentException("Currently XSLF only supports SolidPaint");
/*  289:     */     }
/*  290: 302 */     PaintStyle.SolidPaint sp = (PaintStyle.SolidPaint)color;
/*  291: 303 */     Color col = DrawPaint.applyColorTransform(sp.getSolidColor());
/*  292:     */     
/*  293: 305 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  294: 306 */     CTColor c = pr.isSetBuClr() ? pr.getBuClr() : pr.addNewBuClr();
/*  295: 307 */     CTSRgbColor clr = c.isSetSrgbClr() ? c.getSrgbClr() : c.addNewSrgbClr();
/*  296: 308 */     clr.setVal(new byte[] { (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue() });
/*  297:     */   }
/*  298:     */   
/*  299:     */   public Double getBulletFontSize()
/*  300:     */   {
/*  301: 322 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  302:     */     {
/*  303:     */       public boolean fetch(CTTextParagraphProperties props)
/*  304:     */       {
/*  305: 324 */         if (props.isSetBuSzPct())
/*  306:     */         {
/*  307: 325 */           setValue(Double.valueOf(props.getBuSzPct().getVal() * 0.001D));
/*  308: 326 */           return true;
/*  309:     */         }
/*  310: 328 */         if (props.isSetBuSzPts())
/*  311:     */         {
/*  312: 329 */           setValue(Double.valueOf(-props.getBuSzPts().getVal() * 0.01D));
/*  313: 330 */           return true;
/*  314:     */         }
/*  315: 332 */         return false;
/*  316:     */       }
/*  317: 334 */     };
/*  318: 335 */     fetchParagraphProperty(fetcher);
/*  319: 336 */     return (Double)fetcher.getValue();
/*  320:     */   }
/*  321:     */   
/*  322:     */   public void setBulletFontSize(double bulletSize)
/*  323:     */   {
/*  324: 348 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  325: 350 */     if (bulletSize >= 0.0D)
/*  326:     */     {
/*  327: 351 */       CTTextBulletSizePercent pt = pr.isSetBuSzPct() ? pr.getBuSzPct() : pr.addNewBuSzPct();
/*  328: 352 */       pt.setVal((int)(bulletSize * 1000.0D));
/*  329: 353 */       if (pr.isSetBuSzPts()) {
/*  330: 353 */         pr.unsetBuSzPts();
/*  331:     */       }
/*  332:     */     }
/*  333:     */     else
/*  334:     */     {
/*  335: 355 */       CTTextBulletSizePoint pt = pr.isSetBuSzPts() ? pr.getBuSzPts() : pr.addNewBuSzPts();
/*  336: 356 */       pt.setVal((int)(-bulletSize * 100.0D));
/*  337: 357 */       if (pr.isSetBuSzPct()) {
/*  338: 357 */         pr.unsetBuSzPct();
/*  339:     */       }
/*  340:     */     }
/*  341:     */   }
/*  342:     */   
/*  343:     */   public AutoNumberingScheme getAutoNumberingScheme()
/*  344:     */   {
/*  345: 365 */     ParagraphPropertyFetcher<AutoNumberingScheme> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  346:     */     {
/*  347:     */       public boolean fetch(CTTextParagraphProperties props)
/*  348:     */       {
/*  349: 367 */         if (props.isSetBuAutoNum())
/*  350:     */         {
/*  351: 368 */           AutoNumberingScheme ans = AutoNumberingScheme.forOoxmlID(props.getBuAutoNum().getType().intValue());
/*  352: 369 */           if (ans != null)
/*  353:     */           {
/*  354: 370 */             setValue(ans);
/*  355: 371 */             return true;
/*  356:     */           }
/*  357:     */         }
/*  358: 374 */         return false;
/*  359:     */       }
/*  360: 376 */     };
/*  361: 377 */     fetchParagraphProperty(fetcher);
/*  362: 378 */     return (AutoNumberingScheme)fetcher.getValue();
/*  363:     */   }
/*  364:     */   
/*  365:     */   public Integer getAutoNumberingStartAt()
/*  366:     */   {
/*  367: 385 */     ParagraphPropertyFetcher<Integer> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  368:     */     {
/*  369:     */       public boolean fetch(CTTextParagraphProperties props)
/*  370:     */       {
/*  371: 387 */         if ((props.isSetBuAutoNum()) && 
/*  372: 388 */           (props.getBuAutoNum().isSetStartAt()))
/*  373:     */         {
/*  374: 389 */           setValue(Integer.valueOf(props.getBuAutoNum().getStartAt()));
/*  375: 390 */           return true;
/*  376:     */         }
/*  377: 393 */         return false;
/*  378:     */       }
/*  379: 395 */     };
/*  380: 396 */     fetchParagraphProperty(fetcher);
/*  381: 397 */     return (Integer)fetcher.getValue();
/*  382:     */   }
/*  383:     */   
/*  384:     */   public void setIndent(Double indent)
/*  385:     */   {
/*  386: 403 */     if ((indent == null) && (!this._p.isSetPPr())) {
/*  387: 403 */       return;
/*  388:     */     }
/*  389: 404 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  390: 405 */     if (indent == null)
/*  391:     */     {
/*  392: 406 */       if (pr.isSetIndent()) {
/*  393: 406 */         pr.unsetIndent();
/*  394:     */       }
/*  395:     */     }
/*  396:     */     else {
/*  397: 408 */       pr.setIndent(Units.toEMU(indent.doubleValue()));
/*  398:     */     }
/*  399:     */   }
/*  400:     */   
/*  401:     */   public Double getIndent()
/*  402:     */   {
/*  403: 415 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  404:     */     {
/*  405:     */       public boolean fetch(CTTextParagraphProperties props)
/*  406:     */       {
/*  407: 417 */         if (props.isSetIndent())
/*  408:     */         {
/*  409: 418 */           setValue(Double.valueOf(Units.toPoints(props.getIndent())));
/*  410: 419 */           return true;
/*  411:     */         }
/*  412: 421 */         return false;
/*  413:     */       }
/*  414: 423 */     };
/*  415: 424 */     fetchParagraphProperty(fetcher);
/*  416:     */     
/*  417: 426 */     return (Double)fetcher.getValue();
/*  418:     */   }
/*  419:     */   
/*  420:     */   public void setLeftMargin(Double leftMargin)
/*  421:     */   {
/*  422: 431 */     if ((leftMargin == null) && (!this._p.isSetPPr())) {
/*  423: 431 */       return;
/*  424:     */     }
/*  425: 432 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  426: 433 */     if (leftMargin == null)
/*  427:     */     {
/*  428: 434 */       if (pr.isSetMarL()) {
/*  429: 434 */         pr.unsetMarL();
/*  430:     */       }
/*  431:     */     }
/*  432:     */     else {
/*  433: 436 */       pr.setMarL(Units.toEMU(leftMargin.doubleValue()));
/*  434:     */     }
/*  435:     */   }
/*  436:     */   
/*  437:     */   public Double getLeftMargin()
/*  438:     */   {
/*  439: 446 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  440:     */     {
/*  441:     */       public boolean fetch(CTTextParagraphProperties props)
/*  442:     */       {
/*  443: 448 */         if (props.isSetMarL())
/*  444:     */         {
/*  445: 449 */           double val = Units.toPoints(props.getMarL());
/*  446: 450 */           setValue(Double.valueOf(val));
/*  447: 451 */           return true;
/*  448:     */         }
/*  449: 453 */         return false;
/*  450:     */       }
/*  451: 455 */     };
/*  452: 456 */     fetchParagraphProperty(fetcher);
/*  453:     */     
/*  454: 458 */     return (Double)fetcher.getValue();
/*  455:     */   }
/*  456:     */   
/*  457:     */   public void setRightMargin(Double rightMargin)
/*  458:     */   {
/*  459: 463 */     if ((rightMargin == null) && (!this._p.isSetPPr())) {
/*  460: 463 */       return;
/*  461:     */     }
/*  462: 464 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  463: 465 */     if (rightMargin == null)
/*  464:     */     {
/*  465: 466 */       if (pr.isSetMarR()) {
/*  466: 466 */         pr.unsetMarR();
/*  467:     */       }
/*  468:     */     }
/*  469:     */     else {
/*  470: 468 */       pr.setMarR(Units.toEMU(rightMargin.doubleValue()));
/*  471:     */     }
/*  472:     */   }
/*  473:     */   
/*  474:     */   public Double getRightMargin()
/*  475:     */   {
/*  476: 478 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  477:     */     {
/*  478:     */       public boolean fetch(CTTextParagraphProperties props)
/*  479:     */       {
/*  480: 480 */         if (props.isSetMarR())
/*  481:     */         {
/*  482: 481 */           double val = Units.toPoints(props.getMarR());
/*  483: 482 */           setValue(Double.valueOf(val));
/*  484: 483 */           return true;
/*  485:     */         }
/*  486: 485 */         return false;
/*  487:     */       }
/*  488: 487 */     };
/*  489: 488 */     fetchParagraphProperty(fetcher);
/*  490: 489 */     return (Double)fetcher.getValue();
/*  491:     */   }
/*  492:     */   
/*  493:     */   public Double getDefaultTabSize()
/*  494:     */   {
/*  495: 494 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  496:     */     {
/*  497:     */       public boolean fetch(CTTextParagraphProperties props)
/*  498:     */       {
/*  499: 496 */         if (props.isSetDefTabSz())
/*  500:     */         {
/*  501: 497 */           double val = Units.toPoints(props.getDefTabSz());
/*  502: 498 */           setValue(Double.valueOf(val));
/*  503: 499 */           return true;
/*  504:     */         }
/*  505: 501 */         return false;
/*  506:     */       }
/*  507: 503 */     };
/*  508: 504 */     fetchParagraphProperty(fetcher);
/*  509: 505 */     return (Double)fetcher.getValue();
/*  510:     */   }
/*  511:     */   
/*  512:     */   public double getTabStop(final int idx)
/*  513:     */   {
/*  514: 509 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  515:     */     {
/*  516:     */       public boolean fetch(CTTextParagraphProperties props)
/*  517:     */       {
/*  518: 511 */         if (props.isSetTabLst())
/*  519:     */         {
/*  520: 512 */           CTTextTabStopList tabStops = props.getTabLst();
/*  521: 513 */           if (idx < tabStops.sizeOfTabArray())
/*  522:     */           {
/*  523: 514 */             CTTextTabStop ts = tabStops.getTabArray(idx);
/*  524: 515 */             double val = Units.toPoints(ts.getPos());
/*  525: 516 */             setValue(Double.valueOf(val));
/*  526: 517 */             return true;
/*  527:     */           }
/*  528:     */         }
/*  529: 520 */         return false;
/*  530:     */       }
/*  531: 522 */     };
/*  532: 523 */     fetchParagraphProperty(fetcher);
/*  533: 524 */     return fetcher.getValue() == null ? 0.0D : ((Double)fetcher.getValue()).doubleValue();
/*  534:     */   }
/*  535:     */   
/*  536:     */   public void addTabStop(double value)
/*  537:     */   {
/*  538: 528 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  539: 529 */     CTTextTabStopList tabStops = pr.isSetTabLst() ? pr.getTabLst() : pr.addNewTabLst();
/*  540: 530 */     tabStops.addNewTab().setPos(Units.toEMU(value));
/*  541:     */   }
/*  542:     */   
/*  543:     */   public void setLineSpacing(Double lineSpacing)
/*  544:     */   {
/*  545: 535 */     if ((lineSpacing == null) && (!this._p.isSetPPr())) {
/*  546: 535 */       return;
/*  547:     */     }
/*  548: 536 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  549: 537 */     if (lineSpacing == null)
/*  550:     */     {
/*  551: 538 */       if (pr.isSetLnSpc()) {
/*  552: 538 */         pr.unsetLnSpc();
/*  553:     */       }
/*  554:     */     }
/*  555:     */     else
/*  556:     */     {
/*  557: 540 */       CTTextSpacing spc = pr.isSetLnSpc() ? pr.getLnSpc() : pr.addNewLnSpc();
/*  558: 541 */       if (lineSpacing.doubleValue() >= 0.0D)
/*  559:     */       {
/*  560: 542 */         (spc.isSetSpcPct() ? spc.getSpcPct() : spc.addNewSpcPct()).setVal((int)(lineSpacing.doubleValue() * 1000.0D));
/*  561: 543 */         if (spc.isSetSpcPts()) {
/*  562: 543 */           spc.unsetSpcPts();
/*  563:     */         }
/*  564:     */       }
/*  565:     */       else
/*  566:     */       {
/*  567: 545 */         (spc.isSetSpcPts() ? spc.getSpcPts() : spc.addNewSpcPts()).setVal((int)(-lineSpacing.doubleValue() * 100.0D));
/*  568: 546 */         if (spc.isSetSpcPct()) {
/*  569: 546 */           spc.unsetSpcPct();
/*  570:     */         }
/*  571:     */       }
/*  572:     */     }
/*  573:     */   }
/*  574:     */   
/*  575:     */   public Double getLineSpacing()
/*  576:     */   {
/*  577: 553 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  578:     */     {
/*  579:     */       public boolean fetch(CTTextParagraphProperties props)
/*  580:     */       {
/*  581: 555 */         if (props.isSetLnSpc())
/*  582:     */         {
/*  583: 556 */           CTTextSpacing spc = props.getLnSpc();
/*  584: 558 */           if (spc.isSetSpcPct()) {
/*  585: 558 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/*  586: 559 */           } else if (spc.isSetSpcPts()) {
/*  587: 559 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/*  588:     */           }
/*  589: 560 */           return true;
/*  590:     */         }
/*  591: 562 */         return false;
/*  592:     */       }
/*  593: 564 */     };
/*  594: 565 */     fetchParagraphProperty(fetcher);
/*  595:     */     
/*  596: 567 */     Double lnSpc = (Double)fetcher.getValue();
/*  597: 568 */     if ((lnSpc != null) && (lnSpc.doubleValue() > 0.0D))
/*  598:     */     {
/*  599: 570 */       CTTextNormalAutofit normAutofit = getParentShape().getTextBodyPr().getNormAutofit();
/*  600: 571 */       if (normAutofit != null)
/*  601:     */       {
/*  602: 572 */         double scale = 1.0D - normAutofit.getLnSpcReduction() / 100000.0D;
/*  603: 573 */         lnSpc = Double.valueOf(lnSpc.doubleValue() * scale);
/*  604:     */       }
/*  605:     */     }
/*  606: 577 */     return lnSpc;
/*  607:     */   }
/*  608:     */   
/*  609:     */   public void setSpaceBefore(Double spaceBefore)
/*  610:     */   {
/*  611: 582 */     if ((spaceBefore == null) && (!this._p.isSetPPr())) {
/*  612: 583 */       return;
/*  613:     */     }
/*  614: 587 */     if (spaceBefore == null)
/*  615:     */     {
/*  616: 588 */       if (this._p.getPPr().isSetSpcBef()) {
/*  617: 589 */         this._p.getPPr().unsetSpcBef();
/*  618:     */       }
/*  619: 591 */       return;
/*  620:     */     }
/*  621: 594 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  622: 595 */     CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
/*  623: 597 */     if (spaceBefore.doubleValue() >= 0.0D) {
/*  624: 598 */       spc.addNewSpcPct().setVal((int)(spaceBefore.doubleValue() * 1000.0D));
/*  625:     */     } else {
/*  626: 600 */       spc.addNewSpcPts().setVal((int)(-spaceBefore.doubleValue() * 100.0D));
/*  627:     */     }
/*  628: 602 */     pr.setSpcBef(spc);
/*  629:     */   }
/*  630:     */   
/*  631:     */   public Double getSpaceBefore()
/*  632:     */   {
/*  633: 607 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  634:     */     {
/*  635:     */       public boolean fetch(CTTextParagraphProperties props)
/*  636:     */       {
/*  637: 609 */         if (props.isSetSpcBef())
/*  638:     */         {
/*  639: 610 */           CTTextSpacing spc = props.getSpcBef();
/*  640: 612 */           if (spc.isSetSpcPct()) {
/*  641: 612 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/*  642: 613 */           } else if (spc.isSetSpcPts()) {
/*  643: 613 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/*  644:     */           }
/*  645: 614 */           return true;
/*  646:     */         }
/*  647: 616 */         return false;
/*  648:     */       }
/*  649: 618 */     };
/*  650: 619 */     fetchParagraphProperty(fetcher);
/*  651:     */     
/*  652: 621 */     return (Double)fetcher.getValue();
/*  653:     */   }
/*  654:     */   
/*  655:     */   public void setSpaceAfter(Double spaceAfter)
/*  656:     */   {
/*  657: 626 */     if ((spaceAfter == null) && (!this._p.isSetPPr())) {
/*  658: 627 */       return;
/*  659:     */     }
/*  660: 631 */     if (spaceAfter == null)
/*  661:     */     {
/*  662: 632 */       if (this._p.getPPr().isSetSpcAft()) {
/*  663: 633 */         this._p.getPPr().unsetSpcAft();
/*  664:     */       }
/*  665: 635 */       return;
/*  666:     */     }
/*  667: 638 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  668: 639 */     CTTextSpacing spc = CTTextSpacing.Factory.newInstance();
/*  669: 641 */     if (spaceAfter.doubleValue() >= 0.0D) {
/*  670: 642 */       spc.addNewSpcPct().setVal((int)(spaceAfter.doubleValue() * 1000.0D));
/*  671:     */     } else {
/*  672: 644 */       spc.addNewSpcPts().setVal((int)(-spaceAfter.doubleValue() * 100.0D));
/*  673:     */     }
/*  674: 646 */     pr.setSpcAft(spc);
/*  675:     */   }
/*  676:     */   
/*  677:     */   public Double getSpaceAfter()
/*  678:     */   {
/*  679: 651 */     ParagraphPropertyFetcher<Double> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  680:     */     {
/*  681:     */       public boolean fetch(CTTextParagraphProperties props)
/*  682:     */       {
/*  683: 653 */         if (props.isSetSpcAft())
/*  684:     */         {
/*  685: 654 */           CTTextSpacing spc = props.getSpcAft();
/*  686: 656 */           if (spc.isSetSpcPct()) {
/*  687: 656 */             setValue(Double.valueOf(spc.getSpcPct().getVal() * 0.001D));
/*  688: 657 */           } else if (spc.isSetSpcPts()) {
/*  689: 657 */             setValue(Double.valueOf(-spc.getSpcPts().getVal() * 0.01D));
/*  690:     */           }
/*  691: 658 */           return true;
/*  692:     */         }
/*  693: 660 */         return false;
/*  694:     */       }
/*  695: 662 */     };
/*  696: 663 */     fetchParagraphProperty(fetcher);
/*  697: 664 */     return (Double)fetcher.getValue();
/*  698:     */   }
/*  699:     */   
/*  700:     */   public void setIndentLevel(int level)
/*  701:     */   {
/*  702: 669 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  703: 670 */     pr.setLvl(level);
/*  704:     */   }
/*  705:     */   
/*  706:     */   public int getIndentLevel()
/*  707:     */   {
/*  708: 675 */     CTTextParagraphProperties pr = this._p.getPPr();
/*  709: 676 */     return (pr == null) || (!pr.isSetLvl()) ? 0 : pr.getLvl();
/*  710:     */   }
/*  711:     */   
/*  712:     */   public boolean isBullet()
/*  713:     */   {
/*  714: 683 */     ParagraphPropertyFetcher<Boolean> fetcher = new ParagraphPropertyFetcher(getIndentLevel())
/*  715:     */     {
/*  716:     */       public boolean fetch(CTTextParagraphProperties props)
/*  717:     */       {
/*  718: 685 */         if (props.isSetBuNone())
/*  719:     */         {
/*  720: 686 */           setValue(Boolean.valueOf(false));
/*  721: 687 */           return true;
/*  722:     */         }
/*  723: 689 */         if ((props.isSetBuFont()) || (props.isSetBuChar()))
/*  724:     */         {
/*  725: 690 */           setValue(Boolean.valueOf(true));
/*  726: 691 */           return true;
/*  727:     */         }
/*  728: 693 */         return false;
/*  729:     */       }
/*  730: 695 */     };
/*  731: 696 */     fetchParagraphProperty(fetcher);
/*  732: 697 */     return fetcher.getValue() == null ? false : ((Boolean)fetcher.getValue()).booleanValue();
/*  733:     */   }
/*  734:     */   
/*  735:     */   public void setBullet(boolean flag)
/*  736:     */   {
/*  737: 705 */     if (isBullet() == flag) {
/*  738: 705 */       return;
/*  739:     */     }
/*  740: 707 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  741: 708 */     if (flag)
/*  742:     */     {
/*  743: 709 */       pr.addNewBuFont().setTypeface("Arial");
/*  744: 710 */       pr.addNewBuChar().setChar("•");
/*  745:     */     }
/*  746:     */     else
/*  747:     */     {
/*  748: 712 */       if (pr.isSetBuFont()) {
/*  749: 712 */         pr.unsetBuFont();
/*  750:     */       }
/*  751: 713 */       if (pr.isSetBuChar()) {
/*  752: 713 */         pr.unsetBuChar();
/*  753:     */       }
/*  754: 714 */       if (pr.isSetBuAutoNum()) {
/*  755: 714 */         pr.unsetBuAutoNum();
/*  756:     */       }
/*  757: 715 */       if (pr.isSetBuBlip()) {
/*  758: 715 */         pr.unsetBuBlip();
/*  759:     */       }
/*  760: 716 */       if (pr.isSetBuClr()) {
/*  761: 716 */         pr.unsetBuClr();
/*  762:     */       }
/*  763: 717 */       if (pr.isSetBuClrTx()) {
/*  764: 717 */         pr.unsetBuClrTx();
/*  765:     */       }
/*  766: 718 */       if (pr.isSetBuFont()) {
/*  767: 718 */         pr.unsetBuFont();
/*  768:     */       }
/*  769: 719 */       if (pr.isSetBuFontTx()) {
/*  770: 719 */         pr.unsetBuFontTx();
/*  771:     */       }
/*  772: 720 */       if (pr.isSetBuSzPct()) {
/*  773: 720 */         pr.unsetBuSzPct();
/*  774:     */       }
/*  775: 721 */       if (pr.isSetBuSzPts()) {
/*  776: 721 */         pr.unsetBuSzPts();
/*  777:     */       }
/*  778: 722 */       if (pr.isSetBuSzTx()) {
/*  779: 722 */         pr.unsetBuSzTx();
/*  780:     */       }
/*  781: 723 */       pr.addNewBuNone();
/*  782:     */     }
/*  783:     */   }
/*  784:     */   
/*  785:     */   public void setBulletAutoNumber(AutoNumberingScheme scheme, int startAt)
/*  786:     */   {
/*  787: 735 */     if (startAt < 1) {
/*  788: 735 */       throw new IllegalArgumentException("Start Number must be greater or equal that 1");
/*  789:     */     }
/*  790: 736 */     CTTextParagraphProperties pr = this._p.isSetPPr() ? this._p.getPPr() : this._p.addNewPPr();
/*  791: 737 */     CTTextAutonumberBullet lst = pr.isSetBuAutoNum() ? pr.getBuAutoNum() : pr.addNewBuAutoNum();
/*  792: 738 */     lst.setType(STTextAutonumberScheme.Enum.forInt(scheme.ooxmlId));
/*  793: 739 */     lst.setStartAt(startAt);
/*  794:     */   }
/*  795:     */   
/*  796:     */   public String toString()
/*  797:     */   {
/*  798: 744 */     return "[" + getClass() + "]" + getText();
/*  799:     */   }
/*  800:     */   
/*  801:     */   CTTextParagraphProperties getDefaultMasterStyle()
/*  802:     */   {
/*  803: 753 */     CTPlaceholder ph = this._shape.getCTPlaceholder();
/*  804:     */     String defaultStyleSelector;
/*  805: 755 */     switch (ph == null ? -1 : ph.getType().intValue())
/*  806:     */     {
/*  807:     */     case 1: 
/*  808:     */     case 3: 
/*  809: 758 */       defaultStyleSelector = "titleStyle";
/*  810: 759 */       break;
/*  811:     */     case -1: 
/*  812:     */     case 5: 
/*  813:     */     case 6: 
/*  814:     */     case 7: 
/*  815: 764 */       defaultStyleSelector = "otherStyle";
/*  816: 765 */       break;
/*  817:     */     case 0: 
/*  818:     */     case 2: 
/*  819:     */     case 4: 
/*  820:     */     default: 
/*  821: 767 */       defaultStyleSelector = "bodyStyle";
/*  822:     */     }
/*  823: 770 */     int level = getIndentLevel();
/*  824:     */     
/*  825:     */ 
/*  826: 773 */     String nsPML = "http://schemas.openxmlformats.org/presentationml/2006/main";
/*  827: 774 */     String nsDML = "http://schemas.openxmlformats.org/drawingml/2006/main";
/*  828: 775 */     XSLFSheet masterSheet = this._shape.getSheet();
/*  829: 776 */     for (XSLFSheet m = masterSheet; m != null; m = (XSLFSheet)m.getMasterSheet())
/*  830:     */     {
/*  831: 777 */       masterSheet = m;
/*  832: 778 */       XmlObject xo = masterSheet.getXmlObject();
/*  833: 779 */       XmlCursor cur = xo.newCursor();
/*  834:     */       try
/*  835:     */       {
/*  836: 781 */         cur.push();
/*  837: 782 */         if (((cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", "txStyles")) && (cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", defaultStyleSelector))) || ((cur.pop()) && (cur.toChild("http://schemas.openxmlformats.org/presentationml/2006/main", "notesStyle")))) {
/*  838: 784 */           while (level >= 0)
/*  839:     */           {
/*  840: 785 */             cur.push();
/*  841: 786 */             if (cur.toChild("http://schemas.openxmlformats.org/drawingml/2006/main", "lvl" + (level + 1) + "pPr")) {
/*  842: 787 */               return (CTTextParagraphProperties)cur.getObject();
/*  843:     */             }
/*  844: 789 */             cur.pop();
/*  845: 790 */             level--;
/*  846:     */           }
/*  847:     */         }
/*  848:     */       }
/*  849:     */       finally
/*  850:     */       {
/*  851: 794 */         cur.dispose();
/*  852:     */       }
/*  853:     */     }
/*  854: 798 */     return null;
/*  855:     */   }
/*  856:     */   
/*  857:     */   private <T> boolean fetchParagraphProperty(ParagraphPropertyFetcher<T> visitor)
/*  858:     */   {
/*  859: 802 */     boolean ok = false;
/*  860: 803 */     XSLFTextShape shape = getParentShape();
/*  861: 804 */     XSLFSheet sheet = shape.getSheet();
/*  862: 806 */     if (this._p.isSetPPr()) {
/*  863: 806 */       ok = visitor.fetch(this._p.getPPr());
/*  864:     */     }
/*  865: 807 */     if (ok) {
/*  866: 807 */       return true;
/*  867:     */     }
/*  868: 809 */     ok = shape.fetchShapeProperty(visitor);
/*  869: 810 */     if (ok) {
/*  870: 810 */       return true;
/*  871:     */     }
/*  872: 813 */     CTPlaceholder ph = shape.getCTPlaceholder();
/*  873: 814 */     if (ph == null)
/*  874:     */     {
/*  875: 817 */       XMLSlideShow ppt = sheet.getSlideShow();
/*  876: 818 */       CTTextParagraphProperties themeProps = ppt.getDefaultParagraphStyle(getIndentLevel());
/*  877: 819 */       if (themeProps != null) {
/*  878: 819 */         ok = visitor.fetch(themeProps);
/*  879:     */       }
/*  880:     */     }
/*  881: 821 */     if (ok) {
/*  882: 821 */       return true;
/*  883:     */     }
/*  884: 824 */     CTTextParagraphProperties defaultProps = getDefaultMasterStyle();
/*  885: 826 */     if (defaultProps != null) {
/*  886: 826 */       ok = visitor.fetch(defaultProps);
/*  887:     */     }
/*  888: 827 */     if (ok) {
/*  889: 827 */       return true;
/*  890:     */     }
/*  891: 829 */     return false;
/*  892:     */   }
/*  893:     */   
/*  894:     */   void copy(XSLFTextParagraph other)
/*  895:     */   {
/*  896: 833 */     if (other == this) {
/*  897: 833 */       return;
/*  898:     */     }
/*  899: 835 */     CTTextParagraph thisP = getXmlObject();
/*  900: 836 */     CTTextParagraph otherP = other.getXmlObject();
/*  901: 838 */     if (thisP.isSetPPr()) {
/*  902: 838 */       thisP.unsetPPr();
/*  903:     */     }
/*  904: 839 */     if (thisP.isSetEndParaRPr()) {
/*  905: 839 */       thisP.unsetEndParaRPr();
/*  906:     */     }
/*  907: 841 */     this._runs.clear();
/*  908: 842 */     for (int i = thisP.sizeOfBrArray(); i > 0; i--) {
/*  909: 843 */       thisP.removeBr(i - 1);
/*  910:     */     }
/*  911: 845 */     for (int i = thisP.sizeOfRArray(); i > 0; i--) {
/*  912: 846 */       thisP.removeR(i - 1);
/*  913:     */     }
/*  914: 848 */     for (int i = thisP.sizeOfFldArray(); i > 0; i--) {
/*  915: 849 */       thisP.removeFld(i - 1);
/*  916:     */     }
/*  917: 852 */     XmlCursor thisC = thisP.newCursor();
/*  918: 853 */     thisC.toEndToken();
/*  919: 854 */     XmlCursor otherC = otherP.newCursor();
/*  920: 855 */     otherC.copyXmlContents(thisC);
/*  921: 856 */     otherC.dispose();
/*  922: 857 */     thisC.dispose();
/*  923:     */     
/*  924: 859 */     List<XSLFTextRun> otherRs = other.getTextRuns();
/*  925: 860 */     int i = 0;
/*  926: 861 */     for (CTRegularTextRun rtr : thisP.getRArray())
/*  927:     */     {
/*  928: 862 */       XSLFTextRun run = newTextRun(rtr);
/*  929: 863 */       run.copy((XSLFTextRun)otherRs.get(i++));
/*  930: 864 */       this._runs.add(run);
/*  931:     */     }
/*  932: 870 */     TextParagraph.TextAlign srcAlign = other.getTextAlign();
/*  933: 871 */     if (srcAlign != getTextAlign()) {
/*  934: 872 */       setTextAlign(srcAlign);
/*  935:     */     }
/*  936: 875 */     boolean isBullet = other.isBullet();
/*  937: 876 */     if (isBullet != isBullet())
/*  938:     */     {
/*  939: 877 */       setBullet(isBullet);
/*  940: 878 */       if (isBullet)
/*  941:     */       {
/*  942: 879 */         String buFont = other.getBulletFont();
/*  943: 880 */         if ((buFont != null) && (!buFont.equals(getBulletFont()))) {
/*  944: 881 */           setBulletFont(buFont);
/*  945:     */         }
/*  946: 883 */         String buChar = other.getBulletCharacter();
/*  947: 884 */         if ((buChar != null) && (!buChar.equals(getBulletCharacter()))) {
/*  948: 885 */           setBulletCharacter(buChar);
/*  949:     */         }
/*  950: 887 */         PaintStyle buColor = other.getBulletFontColor();
/*  951: 888 */         if ((buColor != null) && (!buColor.equals(getBulletFontColor()))) {
/*  952: 889 */           setBulletFontColor(buColor);
/*  953:     */         }
/*  954: 891 */         Double buSize = other.getBulletFontSize();
/*  955: 892 */         if (!doubleEquals(buSize, getBulletFontSize())) {
/*  956: 893 */           setBulletFontSize(buSize.doubleValue());
/*  957:     */         }
/*  958:     */       }
/*  959:     */     }
/*  960: 898 */     Double leftMargin = other.getLeftMargin();
/*  961: 899 */     if (!doubleEquals(leftMargin, getLeftMargin())) {
/*  962: 900 */       setLeftMargin(leftMargin);
/*  963:     */     }
/*  964: 903 */     Double indent = other.getIndent();
/*  965: 904 */     if (!doubleEquals(indent, getIndent())) {
/*  966: 905 */       setIndent(indent);
/*  967:     */     }
/*  968: 908 */     Double spaceAfter = other.getSpaceAfter();
/*  969: 909 */     if (!doubleEquals(spaceAfter, getSpaceAfter())) {
/*  970: 910 */       setSpaceAfter(spaceAfter);
/*  971:     */     }
/*  972: 913 */     Double spaceBefore = other.getSpaceBefore();
/*  973: 914 */     if (!doubleEquals(spaceBefore, getSpaceBefore())) {
/*  974: 915 */       setSpaceBefore(spaceBefore);
/*  975:     */     }
/*  976: 918 */     Double lineSpacing = other.getLineSpacing();
/*  977: 919 */     if (!doubleEquals(lineSpacing, getLineSpacing())) {
/*  978: 920 */       setLineSpacing(lineSpacing);
/*  979:     */     }
/*  980:     */   }
/*  981:     */   
/*  982:     */   private static boolean doubleEquals(Double d1, Double d2)
/*  983:     */   {
/*  984: 925 */     return (d1 == d2) || ((d1 != null) && (d1.equals(d2)));
/*  985:     */   }
/*  986:     */   
/*  987:     */   public Double getDefaultFontSize()
/*  988:     */   {
/*  989: 930 */     CTTextCharacterProperties endPr = this._p.getEndParaRPr();
/*  990: 931 */     if ((endPr == null) || (!endPr.isSetSz()))
/*  991:     */     {
/*  992: 933 */       CTTextParagraphProperties masterStyle = getDefaultMasterStyle();
/*  993: 934 */       if (masterStyle != null) {
/*  994: 935 */         endPr = masterStyle.getDefRPr();
/*  995:     */       }
/*  996:     */     }
/*  997: 938 */     return Double.valueOf((endPr == null) || (!endPr.isSetSz()) ? 12.0D : endPr.getSz() / 100.0D);
/*  998:     */   }
/*  999:     */   
/* 1000:     */   public String getDefaultFontFamily()
/* 1001:     */   {
/* 1002: 943 */     return this._runs.isEmpty() ? "Arial" : ((XSLFTextRun)this._runs.get(0)).getFontFamily();
/* 1003:     */   }
/* 1004:     */   
/* 1005:     */   public TextParagraph.BulletStyle getBulletStyle()
/* 1006:     */   {
/* 1007: 948 */     if (!isBullet()) {
/* 1008: 948 */       return null;
/* 1009:     */     }
/* 1010: 949 */     new TextParagraph.BulletStyle()
/* 1011:     */     {
/* 1012:     */       public String getBulletCharacter()
/* 1013:     */       {
/* 1014: 952 */         return XSLFTextParagraph.this.getBulletCharacter();
/* 1015:     */       }
/* 1016:     */       
/* 1017:     */       public String getBulletFont()
/* 1018:     */       {
/* 1019: 957 */         return XSLFTextParagraph.this.getBulletFont();
/* 1020:     */       }
/* 1021:     */       
/* 1022:     */       public Double getBulletFontSize()
/* 1023:     */       {
/* 1024: 962 */         return XSLFTextParagraph.this.getBulletFontSize();
/* 1025:     */       }
/* 1026:     */       
/* 1027:     */       public PaintStyle getBulletFontColor()
/* 1028:     */       {
/* 1029: 967 */         return XSLFTextParagraph.this.getBulletFontColor();
/* 1030:     */       }
/* 1031:     */       
/* 1032:     */       public void setBulletFontColor(Color color)
/* 1033:     */       {
/* 1034: 972 */         setBulletFontColor(DrawPaint.createSolidPaint(color));
/* 1035:     */       }
/* 1036:     */       
/* 1037:     */       public void setBulletFontColor(PaintStyle color)
/* 1038:     */       {
/* 1039: 977 */         XSLFTextParagraph.this.setBulletFontColor(color);
/* 1040:     */       }
/* 1041:     */       
/* 1042:     */       public AutoNumberingScheme getAutoNumberingScheme()
/* 1043:     */       {
/* 1044: 982 */         return XSLFTextParagraph.this.getAutoNumberingScheme();
/* 1045:     */       }
/* 1046:     */       
/* 1047:     */       public Integer getAutoNumberingStartAt()
/* 1048:     */       {
/* 1049: 987 */         return XSLFTextParagraph.this.getAutoNumberingStartAt();
/* 1050:     */       }
/* 1051:     */     };
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   public void setBulletStyle(Object... styles)
/* 1055:     */   {
/* 1056: 995 */     if (styles.length == 0)
/* 1057:     */     {
/* 1058: 996 */       setBullet(false);
/* 1059:     */     }
/* 1060:     */     else
/* 1061:     */     {
/* 1062: 998 */       setBullet(true);
/* 1063: 999 */       for (Object ostyle : styles) {
/* 1064:1000 */         if ((ostyle instanceof Number)) {
/* 1065:1001 */           setBulletFontSize(((Number)ostyle).doubleValue());
/* 1066:1002 */         } else if ((ostyle instanceof Color)) {
/* 1067:1003 */           setBulletFontColor((Color)ostyle);
/* 1068:1004 */         } else if ((ostyle instanceof Character)) {
/* 1069:1005 */           setBulletCharacter(ostyle.toString());
/* 1070:1006 */         } else if ((ostyle instanceof String)) {
/* 1071:1007 */           setBulletFont((String)ostyle);
/* 1072:1008 */         } else if ((ostyle instanceof AutoNumberingScheme)) {
/* 1073:1009 */           setBulletAutoNumber((AutoNumberingScheme)ostyle, 0);
/* 1074:     */         }
/* 1075:     */       }
/* 1076:     */     }
/* 1077:     */   }
/* 1078:     */   
/* 1079:     */   void clearButKeepProperties()
/* 1080:     */   {
/* 1081:1020 */     CTTextParagraph thisP = getXmlObject();
/* 1082:1021 */     for (int i = thisP.sizeOfBrArray(); i > 0; i--) {
/* 1083:1022 */       thisP.removeBr(i - 1);
/* 1084:     */     }
/* 1085:1024 */     for (int i = thisP.sizeOfFldArray(); i > 0; i--) {
/* 1086:1025 */       thisP.removeFld(i - 1);
/* 1087:     */     }
/* 1088:1027 */     if (!this._runs.isEmpty())
/* 1089:     */     {
/* 1090:1028 */       int size = this._runs.size();
/* 1091:1029 */       XSLFTextRun lastRun = (XSLFTextRun)this._runs.get(size - 1);
/* 1092:1030 */       CTTextCharacterProperties cpOther = lastRun.getRPr(false);
/* 1093:1031 */       if (cpOther != null)
/* 1094:     */       {
/* 1095:1032 */         if (thisP.isSetEndParaRPr()) {
/* 1096:1033 */           thisP.unsetEndParaRPr();
/* 1097:     */         }
/* 1098:1035 */         CTTextCharacterProperties cp = thisP.addNewEndParaRPr();
/* 1099:1036 */         cp.set(cpOther);
/* 1100:     */       }
/* 1101:1038 */       for (int i = size; i > 0; i--) {
/* 1102:1039 */         thisP.removeR(i - 1);
/* 1103:     */       }
/* 1104:1041 */       this._runs.clear();
/* 1105:     */     }
/* 1106:     */   }
/* 1107:     */   
/* 1108:     */   public boolean isHeaderOrFooter()
/* 1109:     */   {
/* 1110:1047 */     CTPlaceholder ph = this._shape.getCTPlaceholder();
/* 1111:1048 */     int phId = ph == null ? -1 : ph.getType().intValue();
/* 1112:1049 */     switch (phId)
/* 1113:     */     {
/* 1114:     */     case 5: 
/* 1115:     */     case 6: 
/* 1116:     */     case 7: 
/* 1117:     */     case 8: 
/* 1118:1054 */       return true;
/* 1119:     */     }
/* 1120:1056 */     return false;
/* 1121:     */   }
/* 1122:     */   
/* 1123:     */   protected XSLFTextRun newTextRun(CTRegularTextRun r)
/* 1124:     */   {
/* 1125:1070 */     return new XSLFTextRun(r, this);
/* 1126:     */   }
/* 1127:     */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFTextParagraph
 * JD-Core Version:    0.7.0.1
 */