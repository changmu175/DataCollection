/*    1:     */ package org.apache.poi.xslf.usermodel;
/*    2:     */ 
/*    3:     */ import java.awt.Color;
/*    4:     */ import java.awt.geom.Rectangle2D;
/*    5:     */ import java.awt.geom.Rectangle2D.Double;
/*    6:     */ import javax.xml.stream.XMLStreamException;
/*    7:     */ import javax.xml.stream.XMLStreamReader;
/*    8:     */ import org.apache.poi.openxml4j.opc.PackagePart;
/*    9:     */ import org.apache.poi.sl.draw.DrawPaint;
/*   10:     */ import org.apache.poi.sl.draw.geom.CustomGeometry;
/*   11:     */ import org.apache.poi.sl.draw.geom.Guide;
/*   12:     */ import org.apache.poi.sl.draw.geom.PresetGeometries;
/*   13:     */ import org.apache.poi.sl.usermodel.ColorStyle;
/*   14:     */ import org.apache.poi.sl.usermodel.FillStyle;
/*   15:     */ import org.apache.poi.sl.usermodel.LineDecoration;
/*   16:     */ import org.apache.poi.sl.usermodel.LineDecoration.DecorationShape;
/*   17:     */ import org.apache.poi.sl.usermodel.LineDecoration.DecorationSize;
/*   18:     */ import org.apache.poi.sl.usermodel.PaintStyle;
/*   19:     */ import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
/*   20:     */ import org.apache.poi.sl.usermodel.Placeholder;
/*   21:     */ import org.apache.poi.sl.usermodel.ShapeType;
/*   22:     */ import org.apache.poi.sl.usermodel.SimpleShape;
/*   23:     */ import org.apache.poi.sl.usermodel.StrokeStyle;
/*   24:     */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCap;
/*   25:     */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCompound;
/*   26:     */ import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
/*   27:     */ import org.apache.poi.util.POILogFactory;
/*   28:     */ import org.apache.poi.util.POILogger;
/*   29:     */ import org.apache.poi.util.Units;
/*   30:     */ import org.apache.poi.xslf.model.PropertyFetcher;
/*   31:     */ import org.apache.xmlbeans.XmlObject;
/*   32:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles;
/*   33:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
/*   34:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
/*   35:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D;
/*   36:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList;
/*   37:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem;
/*   38:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList;
/*   39:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide;
/*   40:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList;
/*   41:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties;
/*   42:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
/*   43:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList;
/*   44:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
/*   45:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet;
/*   46:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect;
/*   47:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect.Factory;
/*   48:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
/*   49:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
/*   50:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
/*   51:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties;
/*   52:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
/*   53:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
/*   54:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
/*   55:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
/*   56:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix;
/*   57:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
/*   58:     */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
/*   59:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine;
/*   60:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine.Enum;
/*   61:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineCap.Enum;
/*   62:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength.Enum;
/*   63:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType.Enum;
/*   64:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth.Enum;
/*   65:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STPresetLineDashVal.Enum;
/*   66:     */ import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.Enum;
/*   67:     */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*   68:     */ 
/*   69:     */ public abstract class XSLFSimpleShape
/*   70:     */   extends XSLFShape
/*   71:     */   implements SimpleShape<XSLFShape, XSLFTextParagraph>
/*   72:     */ {
/*   73:  90 */   private static CTOuterShadowEffect NO_SHADOW = ;
/*   74:  91 */   private static final POILogger LOG = POILogFactory.getLogger(XSLFSimpleShape.class);
/*   75:     */   
/*   76:     */   XSLFSimpleShape(XmlObject shape, XSLFSheet sheet)
/*   77:     */   {
/*   78:  94 */     super(shape, sheet);
/*   79:     */   }
/*   80:     */   
/*   81:     */   public void setShapeType(ShapeType type)
/*   82:     */   {
/*   83:  99 */     XSLFPropertiesDelegate.XSLFGeometryProperties gp = XSLFPropertiesDelegate.getGeometryDelegate(getShapeProperties());
/*   84: 100 */     if (gp == null) {
/*   85: 101 */       return;
/*   86:     */     }
/*   87: 103 */     if (gp.isSetCustGeom()) {
/*   88: 104 */       gp.unsetCustGeom();
/*   89:     */     }
/*   90: 106 */     CTPresetGeometry2D prst = gp.isSetPrstGeom() ? gp.getPrstGeom() : gp.addNewPrstGeom();
/*   91: 107 */     prst.setPrst(STShapeType.Enum.forInt(type.ooxmlId));
/*   92:     */   }
/*   93:     */   
/*   94:     */   public ShapeType getShapeType()
/*   95:     */   {
/*   96: 112 */     XSLFPropertiesDelegate.XSLFGeometryProperties gp = XSLFPropertiesDelegate.getGeometryDelegate(getShapeProperties());
/*   97: 113 */     if ((gp != null) && (gp.isSetPrstGeom()))
/*   98:     */     {
/*   99: 114 */       STShapeType.Enum geom = gp.getPrstGeom().getPrst();
/*  100: 115 */       if (geom != null) {
/*  101: 116 */         return ShapeType.forId(geom.intValue(), true);
/*  102:     */       }
/*  103:     */     }
/*  104: 119 */     return null;
/*  105:     */   }
/*  106:     */   
/*  107:     */   protected CTTransform2D getXfrm(boolean create)
/*  108:     */   {
/*  109: 123 */     PropertyFetcher<CTTransform2D> fetcher = new PropertyFetcher()
/*  110:     */     {
/*  111:     */       public boolean fetch(XSLFShape shape)
/*  112:     */       {
/*  113: 126 */         XmlObject xo = shape.getShapeProperties();
/*  114: 127 */         if (((xo instanceof CTShapeProperties)) && (((CTShapeProperties)xo).isSetXfrm()))
/*  115:     */         {
/*  116: 128 */           setValue(((CTShapeProperties)xo).getXfrm());
/*  117: 129 */           return true;
/*  118:     */         }
/*  119: 131 */         return false;
/*  120:     */       }
/*  121: 133 */     };
/*  122: 134 */     fetchShapeProperty(fetcher);
/*  123:     */     
/*  124: 136 */     CTTransform2D xfrm = (CTTransform2D)fetcher.getValue();
/*  125: 137 */     if ((!create) || (xfrm != null)) {
/*  126: 138 */       return xfrm;
/*  127:     */     }
/*  128: 140 */     XmlObject xo = getShapeProperties();
/*  129: 141 */     if ((xo instanceof CTShapeProperties)) {
/*  130: 142 */       return ((CTShapeProperties)xo).addNewXfrm();
/*  131:     */     }
/*  132: 145 */     LOG.log(5, new Object[] { getClass() + " doesn't have xfrm element." });
/*  133: 146 */     return null;
/*  134:     */   }
/*  135:     */   
/*  136:     */   public Rectangle2D getAnchor()
/*  137:     */   {
/*  138: 154 */     CTTransform2D xfrm = getXfrm(false);
/*  139: 155 */     if (xfrm == null) {
/*  140: 156 */       return null;
/*  141:     */     }
/*  142: 159 */     CTPoint2D off = xfrm.getOff();
/*  143: 160 */     double x = Units.toPoints(off.getX());
/*  144: 161 */     double y = Units.toPoints(off.getY());
/*  145: 162 */     CTPositiveSize2D ext = xfrm.getExt();
/*  146: 163 */     double cx = Units.toPoints(ext.getCx());
/*  147: 164 */     double cy = Units.toPoints(ext.getCy());
/*  148: 165 */     return new Double(x, y, cx, cy);
/*  149:     */   }
/*  150:     */   
/*  151:     */   public void setAnchor(Rectangle2D anchor)
/*  152:     */   {
/*  153: 170 */     CTTransform2D xfrm = getXfrm(true);
/*  154: 171 */     if (xfrm == null) {
/*  155: 172 */       return;
/*  156:     */     }
/*  157: 174 */     CTPoint2D off = xfrm.isSetOff() ? xfrm.getOff() : xfrm.addNewOff();
/*  158: 175 */     long x = Units.toEMU(anchor.getX());
/*  159: 176 */     long y = Units.toEMU(anchor.getY());
/*  160: 177 */     off.setX(x);
/*  161: 178 */     off.setY(y);
/*  162: 179 */     CTPositiveSize2D ext = xfrm.isSetExt() ? xfrm.getExt() : xfrm.addNewExt();
/*  163:     */     
/*  164: 181 */     long cx = Units.toEMU(anchor.getWidth());
/*  165: 182 */     long cy = Units.toEMU(anchor.getHeight());
/*  166: 183 */     ext.setCx(cx);
/*  167: 184 */     ext.setCy(cy);
/*  168:     */   }
/*  169:     */   
/*  170:     */   public void setRotation(double theta)
/*  171:     */   {
/*  172: 189 */     CTTransform2D xfrm = getXfrm(true);
/*  173: 190 */     if (xfrm != null) {
/*  174: 191 */       xfrm.setRot((int)(theta * 60000.0D));
/*  175:     */     }
/*  176:     */   }
/*  177:     */   
/*  178:     */   public double getRotation()
/*  179:     */   {
/*  180: 197 */     CTTransform2D xfrm = getXfrm(false);
/*  181: 198 */     return (xfrm == null) || (!xfrm.isSetRot()) ? 0.0D : xfrm.getRot() / 60000.0D;
/*  182:     */   }
/*  183:     */   
/*  184:     */   public void setFlipHorizontal(boolean flip)
/*  185:     */   {
/*  186: 203 */     CTTransform2D xfrm = getXfrm(true);
/*  187: 204 */     if (xfrm != null) {
/*  188: 205 */       xfrm.setFlipH(flip);
/*  189:     */     }
/*  190:     */   }
/*  191:     */   
/*  192:     */   public void setFlipVertical(boolean flip)
/*  193:     */   {
/*  194: 211 */     CTTransform2D xfrm = getXfrm(true);
/*  195: 212 */     if (xfrm != null) {
/*  196: 213 */       xfrm.setFlipV(flip);
/*  197:     */     }
/*  198:     */   }
/*  199:     */   
/*  200:     */   public boolean getFlipHorizontal()
/*  201:     */   {
/*  202: 219 */     CTTransform2D xfrm = getXfrm(false);
/*  203: 220 */     return (xfrm == null) || (!xfrm.isSetFlipH()) ? false : xfrm.getFlipH();
/*  204:     */   }
/*  205:     */   
/*  206:     */   public boolean getFlipVertical()
/*  207:     */   {
/*  208: 225 */     CTTransform2D xfrm = getXfrm(false);
/*  209: 226 */     return (xfrm == null) || (!xfrm.isSetFlipV()) ? false : xfrm.getFlipV();
/*  210:     */   }
/*  211:     */   
/*  212:     */   CTLineProperties getDefaultLineProperties()
/*  213:     */   {
/*  214: 237 */     CTShapeStyle style = getSpStyle();
/*  215: 238 */     if (style == null) {
/*  216: 239 */       return null;
/*  217:     */     }
/*  218: 241 */     CTStyleMatrixReference lnRef = style.getLnRef();
/*  219: 242 */     if (lnRef == null) {
/*  220: 243 */       return null;
/*  221:     */     }
/*  222: 246 */     int idx = (int)lnRef.getIdx();
/*  223:     */     
/*  224: 248 */     XSLFTheme theme = getSheet().getTheme();
/*  225: 249 */     if (theme == null) {
/*  226: 250 */       return null;
/*  227:     */     }
/*  228: 252 */     CTBaseStyles styles = theme.getXmlObject().getThemeElements();
/*  229: 253 */     if (styles == null) {
/*  230: 254 */       return null;
/*  231:     */     }
/*  232: 256 */     CTStyleMatrix styleMatrix = styles.getFmtScheme();
/*  233: 257 */     if (styleMatrix == null) {
/*  234: 258 */       return null;
/*  235:     */     }
/*  236: 260 */     CTLineStyleList lineStyles = styleMatrix.getLnStyleLst();
/*  237: 261 */     if ((lineStyles == null) || (lineStyles.sizeOfLnArray() < idx)) {
/*  238: 262 */       return null;
/*  239:     */     }
/*  240: 265 */     return lineStyles.getLnArray(idx - 1);
/*  241:     */   }
/*  242:     */   
/*  243:     */   public void setLineColor(Color color)
/*  244:     */   {
/*  245: 273 */     CTLineProperties ln = getLn(this, true);
/*  246: 274 */     if (ln == null) {
/*  247: 275 */       return;
/*  248:     */     }
/*  249: 278 */     if (ln.isSetSolidFill()) {
/*  250: 279 */       ln.unsetSolidFill();
/*  251:     */     }
/*  252: 281 */     if (ln.isSetGradFill()) {
/*  253: 282 */       ln.unsetGradFill();
/*  254:     */     }
/*  255: 284 */     if (ln.isSetPattFill()) {
/*  256: 285 */       ln.unsetPattFill();
/*  257:     */     }
/*  258: 287 */     if (ln.isSetNoFill()) {
/*  259: 288 */       ln.unsetNoFill();
/*  260:     */     }
/*  261: 292 */     if (color == null)
/*  262:     */     {
/*  263: 293 */       ln.addNewNoFill();
/*  264:     */     }
/*  265:     */     else
/*  266:     */     {
/*  267: 295 */       CTSolidColorFillProperties fill = ln.addNewSolidFill();
/*  268: 296 */       XSLFColor col = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/*  269: 297 */       col.setColor(color);
/*  270:     */     }
/*  271:     */   }
/*  272:     */   
/*  273:     */   public Color getLineColor()
/*  274:     */   {
/*  275: 307 */     PaintStyle ps = getLinePaint();
/*  276: 308 */     if ((ps instanceof PaintStyle.SolidPaint)) {
/*  277: 309 */       return ((PaintStyle.SolidPaint)ps).getSolidColor().getColor();
/*  278:     */     }
/*  279: 311 */     return null;
/*  280:     */   }
/*  281:     */   
/*  282:     */   protected PaintStyle getLinePaint()
/*  283:     */   {
/*  284: 315 */     XSLFSheet sheet = getSheet();
/*  285: 316 */     final XSLFTheme theme = sheet.getTheme();
/*  286: 317 */     final boolean hasPlaceholder = getPlaceholder() != null;
/*  287: 318 */     PropertyFetcher<PaintStyle> fetcher = new PropertyFetcher()
/*  288:     */     {
/*  289:     */       public boolean fetch(XSLFShape shape)
/*  290:     */       {
/*  291: 321 */         CTLineProperties spPr = XSLFSimpleShape.getLn(shape, false);
/*  292: 322 */         XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(spPr);
/*  293: 324 */         if ((fp != null) && (fp.isSetNoFill()))
/*  294:     */         {
/*  295: 325 */           setValue(null);
/*  296: 326 */           return true;
/*  297:     */         }
/*  298: 329 */         PackagePart pp = shape.getSheet().getPackagePart();
/*  299: 330 */         PaintStyle paint = XSLFShape.selectPaint(fp, null, pp, theme, hasPlaceholder);
/*  300: 331 */         if (paint != null)
/*  301:     */         {
/*  302: 332 */           setValue(paint);
/*  303: 333 */           return true;
/*  304:     */         }
/*  305: 336 */         CTShapeStyle style = shape.getSpStyle();
/*  306: 337 */         if (style != null)
/*  307:     */         {
/*  308: 338 */           fp = XSLFPropertiesDelegate.getFillDelegate(style.getLnRef());
/*  309: 339 */           paint = XSLFShape.selectPaint(fp, null, pp, theme, hasPlaceholder);
/*  310: 342 */           if (paint == null) {
/*  311: 343 */             paint = getThemePaint(style, pp);
/*  312:     */           }
/*  313:     */         }
/*  314: 347 */         if (paint != null)
/*  315:     */         {
/*  316: 348 */           setValue(paint);
/*  317: 349 */           return true;
/*  318:     */         }
/*  319: 352 */         return false;
/*  320:     */       }
/*  321:     */       
/*  322:     */       PaintStyle getThemePaint(CTShapeStyle style, PackagePart pp)
/*  323:     */       {
/*  324: 357 */         CTStyleMatrixReference lnRef = style.getLnRef();
/*  325: 358 */         if (lnRef == null) {
/*  326: 359 */           return null;
/*  327:     */         }
/*  328: 361 */         int idx = (int)lnRef.getIdx();
/*  329: 362 */         CTSchemeColor phClr = lnRef.getSchemeClr();
/*  330: 363 */         if (idx <= 0) {
/*  331: 364 */           return null;
/*  332:     */         }
/*  333: 367 */         CTLineProperties props = theme.getXmlObject().getThemeElements().getFmtScheme().getLnStyleLst().getLnArray(idx - 1);
/*  334: 368 */         XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(props);
/*  335: 369 */         return XSLFShape.selectPaint(fp, phClr, pp, theme, hasPlaceholder);
/*  336:     */       }
/*  337: 371 */     };
/*  338: 372 */     fetchShapeProperty(fetcher);
/*  339:     */     
/*  340: 374 */     return (PaintStyle)fetcher.getValue();
/*  341:     */   }
/*  342:     */   
/*  343:     */   public void setLineWidth(double width)
/*  344:     */   {
/*  345: 382 */     CTLineProperties lnPr = getLn(this, true);
/*  346: 383 */     if (lnPr == null) {
/*  347: 384 */       return;
/*  348:     */     }
/*  349: 387 */     if (width == 0.0D)
/*  350:     */     {
/*  351: 388 */       if (lnPr.isSetW()) {
/*  352: 389 */         lnPr.unsetW();
/*  353:     */       }
/*  354: 391 */       if (!lnPr.isSetNoFill()) {
/*  355: 392 */         lnPr.addNewNoFill();
/*  356:     */       }
/*  357: 394 */       if (lnPr.isSetSolidFill()) {
/*  358: 395 */         lnPr.unsetSolidFill();
/*  359:     */       }
/*  360: 397 */       if (lnPr.isSetGradFill()) {
/*  361: 398 */         lnPr.unsetGradFill();
/*  362:     */       }
/*  363: 400 */       if (lnPr.isSetPattFill()) {
/*  364: 401 */         lnPr.unsetPattFill();
/*  365:     */       }
/*  366:     */     }
/*  367:     */     else
/*  368:     */     {
/*  369: 404 */       if (lnPr.isSetNoFill()) {
/*  370: 405 */         lnPr.unsetNoFill();
/*  371:     */       }
/*  372: 408 */       lnPr.setW(Units.toEMU(width));
/*  373:     */     }
/*  374:     */   }
/*  375:     */   
/*  376:     */   public double getLineWidth()
/*  377:     */   {
/*  378: 416 */     PropertyFetcher<Double> fetcher = new PropertyFetcher()
/*  379:     */     {
/*  380:     */       public boolean fetch(XSLFShape shape)
/*  381:     */       {
/*  382: 419 */         CTLineProperties ln = XSLFSimpleShape.getLn(shape, false);
/*  383: 420 */         if (ln != null)
/*  384:     */         {
/*  385: 421 */           if (ln.isSetNoFill())
/*  386:     */           {
/*  387: 422 */             setValue(Double.valueOf(0.0D));
/*  388: 423 */             return true;
/*  389:     */           }
/*  390: 426 */           if (ln.isSetW())
/*  391:     */           {
/*  392: 427 */             setValue(Double.valueOf(Units.toPoints(ln.getW())));
/*  393: 428 */             return true;
/*  394:     */           }
/*  395:     */         }
/*  396: 431 */         return false;
/*  397:     */       }
/*  398: 433 */     };
/*  399: 434 */     fetchShapeProperty(fetcher);
/*  400:     */     
/*  401: 436 */     double lineWidth = 0.0D;
/*  402: 437 */     if (fetcher.getValue() == null)
/*  403:     */     {
/*  404: 438 */       CTLineProperties defaultLn = getDefaultLineProperties();
/*  405: 439 */       if ((defaultLn != null) && 
/*  406: 440 */         (defaultLn.isSetW())) {
/*  407: 441 */         lineWidth = Units.toPoints(defaultLn.getW());
/*  408:     */       }
/*  409:     */     }
/*  410:     */     else
/*  411:     */     {
/*  412: 445 */       lineWidth = ((Double)fetcher.getValue()).doubleValue();
/*  413:     */     }
/*  414: 448 */     return lineWidth;
/*  415:     */   }
/*  416:     */   
/*  417:     */   public void setLineCompound(StrokeStyle.LineCompound compound)
/*  418:     */   {
/*  419: 456 */     CTLineProperties ln = getLn(this, true);
/*  420: 457 */     if (ln == null) {
/*  421: 458 */       return;
/*  422:     */     }
/*  423: 460 */     if (compound == null)
/*  424:     */     {
/*  425: 461 */       if (ln.isSetCmpd()) {
/*  426: 462 */         ln.unsetCmpd();
/*  427:     */       }
/*  428:     */     }
/*  429:     */     else
/*  430:     */     {
/*  431:     */       STCompoundLine.Enum xCmpd;
/*  432: 466 */       switch (11.$SwitchMap$org$apache$poi$sl$usermodel$StrokeStyle$LineCompound[compound.ordinal()])
/*  433:     */       {
/*  434:     */       case 1: 
/*  435:     */       default: 
/*  436: 469 */         xCmpd = STCompoundLine.SNG;
/*  437: 470 */         break;
/*  438:     */       case 2: 
/*  439: 472 */         xCmpd = STCompoundLine.DBL;
/*  440: 473 */         break;
/*  441:     */       case 3: 
/*  442: 475 */         xCmpd = STCompoundLine.THICK_THIN;
/*  443: 476 */         break;
/*  444:     */       case 4: 
/*  445: 478 */         xCmpd = STCompoundLine.THIN_THICK;
/*  446: 479 */         break;
/*  447:     */       case 5: 
/*  448: 481 */         xCmpd = STCompoundLine.TRI;
/*  449:     */       }
/*  450: 484 */       ln.setCmpd(xCmpd);
/*  451:     */     }
/*  452:     */   }
/*  453:     */   
/*  454:     */   public StrokeStyle.LineCompound getLineCompound()
/*  455:     */   {
/*  456: 492 */     PropertyFetcher<Integer> fetcher = new PropertyFetcher()
/*  457:     */     {
/*  458:     */       public boolean fetch(XSLFShape shape)
/*  459:     */       {
/*  460: 495 */         CTLineProperties ln = XSLFSimpleShape.getLn(shape, false);
/*  461: 496 */         if (ln != null)
/*  462:     */         {
/*  463: 497 */           STCompoundLine.Enum stCmpd = ln.getCmpd();
/*  464: 498 */           if (stCmpd != null)
/*  465:     */           {
/*  466: 499 */             setValue(Integer.valueOf(stCmpd.intValue()));
/*  467: 500 */             return true;
/*  468:     */           }
/*  469:     */         }
/*  470: 503 */         return false;
/*  471:     */       }
/*  472: 505 */     };
/*  473: 506 */     fetchShapeProperty(fetcher);
/*  474:     */     
/*  475: 508 */     Integer cmpd = (Integer)fetcher.getValue();
/*  476: 509 */     if (cmpd == null)
/*  477:     */     {
/*  478: 510 */       CTLineProperties defaultLn = getDefaultLineProperties();
/*  479: 511 */       if ((defaultLn != null) && (defaultLn.isSetCmpd()))
/*  480:     */       {
/*  481: 512 */         switch (defaultLn.getCmpd().intValue())
/*  482:     */         {
/*  483:     */         case 1: 
/*  484:     */         default: 
/*  485: 515 */           return StrokeStyle.LineCompound.SINGLE;
/*  486:     */         case 2: 
/*  487: 517 */           return StrokeStyle.LineCompound.DOUBLE;
/*  488:     */         case 3: 
/*  489: 519 */           return StrokeStyle.LineCompound.THICK_THIN;
/*  490:     */         case 4: 
/*  491: 521 */           return StrokeStyle.LineCompound.THIN_THICK;
/*  492:     */         }
/*  493: 523 */         return StrokeStyle.LineCompound.TRIPLE;
/*  494:     */       }
/*  495:     */     }
/*  496: 528 */     return null;
/*  497:     */   }
/*  498:     */   
/*  499:     */   public void setLineDash(StrokeStyle.LineDash dash)
/*  500:     */   {
/*  501: 536 */     CTLineProperties ln = getLn(this, true);
/*  502: 537 */     if (ln == null) {
/*  503: 538 */       return;
/*  504:     */     }
/*  505: 540 */     if (dash == null)
/*  506:     */     {
/*  507: 541 */       if (ln.isSetPrstDash()) {
/*  508: 542 */         ln.unsetPrstDash();
/*  509:     */       }
/*  510:     */     }
/*  511:     */     else
/*  512:     */     {
/*  513: 545 */       CTPresetLineDashProperties ldp = ln.isSetPrstDash() ? ln.getPrstDash() : ln.addNewPrstDash();
/*  514: 546 */       ldp.setVal(STPresetLineDashVal.Enum.forInt(dash.ooxmlId));
/*  515:     */     }
/*  516:     */   }
/*  517:     */   
/*  518:     */   public StrokeStyle.LineDash getLineDash()
/*  519:     */   {
/*  520: 555 */     PropertyFetcher<StrokeStyle.LineDash> fetcher = new PropertyFetcher()
/*  521:     */     {
/*  522:     */       public boolean fetch(XSLFShape shape)
/*  523:     */       {
/*  524: 558 */         CTLineProperties ln = XSLFSimpleShape.getLn(shape, false);
/*  525: 559 */         if ((ln == null) || (!ln.isSetPrstDash())) {
/*  526: 560 */           return false;
/*  527:     */         }
/*  528: 563 */         setValue(StrokeStyle.LineDash.fromOoxmlId(ln.getPrstDash().getVal().intValue()));
/*  529: 564 */         return true;
/*  530:     */       }
/*  531: 566 */     };
/*  532: 567 */     fetchShapeProperty(fetcher);
/*  533:     */     
/*  534: 569 */     StrokeStyle.LineDash dash = (StrokeStyle.LineDash)fetcher.getValue();
/*  535: 570 */     if (dash == null)
/*  536:     */     {
/*  537: 571 */       CTLineProperties defaultLn = getDefaultLineProperties();
/*  538: 572 */       if ((defaultLn != null) && (defaultLn.isSetPrstDash())) {
/*  539: 573 */         dash = StrokeStyle.LineDash.fromOoxmlId(defaultLn.getPrstDash().getVal().intValue());
/*  540:     */       }
/*  541:     */     }
/*  542: 576 */     return dash;
/*  543:     */   }
/*  544:     */   
/*  545:     */   public void setLineCap(StrokeStyle.LineCap cap)
/*  546:     */   {
/*  547: 584 */     CTLineProperties ln = getLn(this, true);
/*  548: 585 */     if (ln == null) {
/*  549: 586 */       return;
/*  550:     */     }
/*  551: 589 */     if (cap == null)
/*  552:     */     {
/*  553: 590 */       if (ln.isSetCap()) {
/*  554: 591 */         ln.unsetCap();
/*  555:     */       }
/*  556:     */     }
/*  557:     */     else {
/*  558: 594 */       ln.setCap(STLineCap.Enum.forInt(cap.ooxmlId));
/*  559:     */     }
/*  560:     */   }
/*  561:     */   
/*  562:     */   public StrokeStyle.LineCap getLineCap()
/*  563:     */   {
/*  564: 603 */     PropertyFetcher<StrokeStyle.LineCap> fetcher = new PropertyFetcher()
/*  565:     */     {
/*  566:     */       public boolean fetch(XSLFShape shape)
/*  567:     */       {
/*  568: 606 */         CTLineProperties ln = XSLFSimpleShape.getLn(shape, false);
/*  569: 607 */         if ((ln != null) && (ln.isSetCap()))
/*  570:     */         {
/*  571: 608 */           setValue(StrokeStyle.LineCap.fromOoxmlId(ln.getCap().intValue()));
/*  572: 609 */           return true;
/*  573:     */         }
/*  574: 611 */         return false;
/*  575:     */       }
/*  576: 613 */     };
/*  577: 614 */     fetchShapeProperty(fetcher);
/*  578:     */     
/*  579: 616 */     StrokeStyle.LineCap cap = (StrokeStyle.LineCap)fetcher.getValue();
/*  580: 617 */     if (cap == null)
/*  581:     */     {
/*  582: 618 */       CTLineProperties defaultLn = getDefaultLineProperties();
/*  583: 619 */       if ((defaultLn != null) && (defaultLn.isSetCap())) {
/*  584: 620 */         cap = StrokeStyle.LineCap.fromOoxmlId(defaultLn.getCap().intValue());
/*  585:     */       }
/*  586:     */     }
/*  587: 623 */     return cap;
/*  588:     */   }
/*  589:     */   
/*  590:     */   public void setFillColor(Color color)
/*  591:     */   {
/*  592: 628 */     XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(getShapeProperties());
/*  593: 629 */     if (fp == null) {
/*  594: 630 */       return;
/*  595:     */     }
/*  596: 632 */     if (color == null)
/*  597:     */     {
/*  598: 633 */       if (fp.isSetSolidFill()) {
/*  599: 634 */         fp.unsetSolidFill();
/*  600:     */       }
/*  601: 637 */       if (fp.isSetGradFill()) {
/*  602: 638 */         fp.unsetGradFill();
/*  603:     */       }
/*  604: 641 */       if (fp.isSetPattFill()) {
/*  605: 642 */         fp.unsetGradFill();
/*  606:     */       }
/*  607: 645 */       if (fp.isSetBlipFill()) {
/*  608: 646 */         fp.unsetBlipFill();
/*  609:     */       }
/*  610: 649 */       if (!fp.isSetNoFill()) {
/*  611: 650 */         fp.addNewNoFill();
/*  612:     */       }
/*  613:     */     }
/*  614:     */     else
/*  615:     */     {
/*  616: 653 */       if (fp.isSetNoFill()) {
/*  617: 654 */         fp.unsetNoFill();
/*  618:     */       }
/*  619: 657 */       CTSolidColorFillProperties fill = fp.isSetSolidFill() ? fp.getSolidFill() : fp.addNewSolidFill();
/*  620:     */       
/*  621: 659 */       XSLFColor col = new XSLFColor(fill, getSheet().getTheme(), fill.getSchemeClr());
/*  622: 660 */       col.setColor(color);
/*  623:     */     }
/*  624:     */   }
/*  625:     */   
/*  626:     */   public Color getFillColor()
/*  627:     */   {
/*  628: 666 */     PaintStyle ps = getFillPaint();
/*  629: 667 */     if ((ps instanceof PaintStyle.SolidPaint)) {
/*  630: 668 */       return DrawPaint.applyColorTransform(((PaintStyle.SolidPaint)ps).getSolidColor());
/*  631:     */     }
/*  632: 670 */     return null;
/*  633:     */   }
/*  634:     */   
/*  635:     */   public XSLFShadow getShadow()
/*  636:     */   {
/*  637: 678 */     PropertyFetcher<CTOuterShadowEffect> fetcher = new PropertyFetcher()
/*  638:     */     {
/*  639:     */       public boolean fetch(XSLFShape shape)
/*  640:     */       {
/*  641: 681 */         XSLFPropertiesDelegate.XSLFEffectProperties ep = XSLFPropertiesDelegate.getEffectDelegate(shape.getShapeProperties());
/*  642: 682 */         if ((ep != null) && (ep.isSetEffectLst()))
/*  643:     */         {
/*  644: 683 */           CTOuterShadowEffect obj = ep.getEffectLst().getOuterShdw();
/*  645: 684 */           setValue(obj == null ? XSLFSimpleShape.NO_SHADOW : obj);
/*  646: 685 */           return true;
/*  647:     */         }
/*  648: 687 */         return false;
/*  649:     */       }
/*  650: 689 */     };
/*  651: 690 */     fetchShapeProperty(fetcher);
/*  652:     */     
/*  653: 692 */     CTOuterShadowEffect obj = (CTOuterShadowEffect)fetcher.getValue();
/*  654: 693 */     if (obj == null)
/*  655:     */     {
/*  656: 695 */       CTShapeStyle style = getSpStyle();
/*  657: 696 */       if ((style != null) && (style.getEffectRef() != null))
/*  658:     */       {
/*  659: 698 */         int idx = (int)style.getEffectRef().getIdx();
/*  660: 699 */         if (idx != 0)
/*  661:     */         {
/*  662: 700 */           CTStyleMatrix styleMatrix = getSheet().getTheme().getXmlObject().getThemeElements().getFmtScheme();
/*  663: 701 */           CTEffectStyleItem ef = styleMatrix.getEffectStyleLst().getEffectStyleArray(idx - 1);
/*  664: 702 */           obj = ef.getEffectLst().getOuterShdw();
/*  665:     */         }
/*  666:     */       }
/*  667:     */     }
/*  668: 706 */     return (obj == null) || (obj == NO_SHADOW) ? null : new XSLFShadow(obj, this);
/*  669:     */   }
/*  670:     */   
/*  671:     */   public CustomGeometry getGeometry()
/*  672:     */   {
/*  673: 715 */     XSLFPropertiesDelegate.XSLFGeometryProperties gp = XSLFPropertiesDelegate.getGeometryDelegate(getShapeProperties());
/*  674: 717 */     if (gp == null) {
/*  675: 718 */       return null;
/*  676:     */     }
/*  677: 722 */     PresetGeometries dict = PresetGeometries.getInstance();
/*  678:     */     CustomGeometry geom;
/*  679: 723 */     if (gp.isSetPrstGeom())
/*  680:     */     {
/*  681: 724 */       String name = gp.getPrstGeom().getPrst().toString();
/*  682: 725 */       CustomGeometry geom = (CustomGeometry)dict.get(name);
/*  683: 726 */       if (geom == null) {
/*  684: 727 */         throw new IllegalStateException("Unknown shape geometry: " + name + ", available geometries are: " + dict.keySet());
/*  685:     */       }
/*  686:     */     }
/*  687: 729 */     else if (gp.isSetCustGeom())
/*  688:     */     {
/*  689: 730 */       XMLStreamReader staxReader = gp.getCustGeom().newXMLStreamReader();
/*  690: 731 */       CustomGeometry geom = PresetGeometries.convertCustomGeometry(staxReader);
/*  691:     */       try
/*  692:     */       {
/*  693: 733 */         staxReader.close();
/*  694:     */       }
/*  695:     */       catch (XMLStreamException e)
/*  696:     */       {
/*  697: 736 */         LOG.log(5, new Object[] { "An error occurred while closing a Custom Geometry XML Stream Reader: " + e.getMessage() });
/*  698:     */       }
/*  699:     */     }
/*  700:     */     else
/*  701:     */     {
/*  702: 740 */       geom = (CustomGeometry)dict.get("rect");
/*  703:     */     }
/*  704: 742 */     return geom;
/*  705:     */   }
/*  706:     */   
/*  707:     */   void copy(XSLFShape sh)
/*  708:     */   {
/*  709: 747 */     super.copy(sh);
/*  710:     */     
/*  711: 749 */     XSLFSimpleShape s = (XSLFSimpleShape)sh;
/*  712:     */     
/*  713: 751 */     Color srsSolidFill = s.getFillColor();
/*  714: 752 */     Color tgtSoliFill = getFillColor();
/*  715: 753 */     if ((srsSolidFill != null) && (!srsSolidFill.equals(tgtSoliFill))) {
/*  716: 754 */       setFillColor(srsSolidFill);
/*  717:     */     }
/*  718: 757 */     XSLFPropertiesDelegate.XSLFFillProperties fp = XSLFPropertiesDelegate.getFillDelegate(getShapeProperties());
/*  719: 758 */     if ((fp != null) && (fp.isSetBlipFill()))
/*  720:     */     {
/*  721: 759 */       CTBlip blip = fp.getBlipFill().getBlip();
/*  722: 760 */       String blipId = blip.getEmbed();
/*  723:     */       
/*  724: 762 */       String relId = getSheet().importBlip(blipId, s.getSheet().getPackagePart());
/*  725: 763 */       blip.setEmbed(relId);
/*  726:     */     }
/*  727: 766 */     Color srcLineColor = s.getLineColor();
/*  728: 767 */     Color tgtLineColor = getLineColor();
/*  729: 768 */     if ((srcLineColor != null) && (!srcLineColor.equals(tgtLineColor))) {
/*  730: 769 */       setLineColor(srcLineColor);
/*  731:     */     }
/*  732: 772 */     double srcLineWidth = s.getLineWidth();
/*  733: 773 */     double tgtLineWidth = getLineWidth();
/*  734: 774 */     if (srcLineWidth != tgtLineWidth) {
/*  735: 775 */       setLineWidth(srcLineWidth);
/*  736:     */     }
/*  737: 778 */     StrokeStyle.LineDash srcLineDash = s.getLineDash();
/*  738: 779 */     StrokeStyle.LineDash tgtLineDash = getLineDash();
/*  739: 780 */     if ((srcLineDash != null) && (srcLineDash != tgtLineDash)) {
/*  740: 781 */       setLineDash(srcLineDash);
/*  741:     */     }
/*  742: 784 */     StrokeStyle.LineCap srcLineCap = s.getLineCap();
/*  743: 785 */     StrokeStyle.LineCap tgtLineCap = getLineCap();
/*  744: 786 */     if ((srcLineCap != null) && (srcLineCap != tgtLineCap)) {
/*  745: 787 */       setLineCap(srcLineCap);
/*  746:     */     }
/*  747:     */   }
/*  748:     */   
/*  749:     */   public void setLineHeadDecoration(LineDecoration.DecorationShape style)
/*  750:     */   {
/*  751: 798 */     CTLineProperties ln = getLn(this, true);
/*  752: 799 */     if (ln == null) {
/*  753: 800 */       return;
/*  754:     */     }
/*  755: 802 */     CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();
/*  756: 803 */     if (style == null)
/*  757:     */     {
/*  758: 804 */       if (lnEnd.isSetType()) {
/*  759: 805 */         lnEnd.unsetType();
/*  760:     */       }
/*  761:     */     }
/*  762:     */     else {
/*  763: 808 */       lnEnd.setType(STLineEndType.Enum.forInt(style.ooxmlId));
/*  764:     */     }
/*  765:     */   }
/*  766:     */   
/*  767:     */   public LineDecoration.DecorationShape getLineHeadDecoration()
/*  768:     */   {
/*  769: 816 */     CTLineProperties ln = getLn(this, false);
/*  770: 817 */     LineDecoration.DecorationShape ds = LineDecoration.DecorationShape.NONE;
/*  771: 818 */     if ((ln != null) && (ln.isSetHeadEnd()) && (ln.getHeadEnd().isSetType())) {
/*  772: 819 */       ds = LineDecoration.DecorationShape.fromOoxmlId(ln.getHeadEnd().getType().intValue());
/*  773:     */     }
/*  774: 821 */     return ds;
/*  775:     */   }
/*  776:     */   
/*  777:     */   public void setLineHeadWidth(LineDecoration.DecorationSize style)
/*  778:     */   {
/*  779: 830 */     CTLineProperties ln = getLn(this, true);
/*  780: 831 */     if (ln == null) {
/*  781: 832 */       return;
/*  782:     */     }
/*  783: 834 */     CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();
/*  784: 835 */     if (style == null)
/*  785:     */     {
/*  786: 836 */       if (lnEnd.isSetW()) {
/*  787: 837 */         lnEnd.unsetW();
/*  788:     */       }
/*  789:     */     }
/*  790:     */     else {
/*  791: 840 */       lnEnd.setW(STLineEndWidth.Enum.forInt(style.ooxmlId));
/*  792:     */     }
/*  793:     */   }
/*  794:     */   
/*  795:     */   public LineDecoration.DecorationSize getLineHeadWidth()
/*  796:     */   {
/*  797: 848 */     CTLineProperties ln = getLn(this, false);
/*  798: 849 */     LineDecoration.DecorationSize ds = LineDecoration.DecorationSize.MEDIUM;
/*  799: 850 */     if ((ln != null) && (ln.isSetHeadEnd()) && (ln.getHeadEnd().isSetW())) {
/*  800: 851 */       ds = LineDecoration.DecorationSize.fromOoxmlId(ln.getHeadEnd().getW().intValue());
/*  801:     */     }
/*  802: 853 */     return ds;
/*  803:     */   }
/*  804:     */   
/*  805:     */   public void setLineHeadLength(LineDecoration.DecorationSize style)
/*  806:     */   {
/*  807: 860 */     CTLineProperties ln = getLn(this, true);
/*  808: 861 */     if (ln == null) {
/*  809: 862 */       return;
/*  810:     */     }
/*  811: 865 */     CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();
/*  812: 866 */     if (style == null)
/*  813:     */     {
/*  814: 867 */       if (lnEnd.isSetLen()) {
/*  815: 868 */         lnEnd.unsetLen();
/*  816:     */       }
/*  817:     */     }
/*  818:     */     else {
/*  819: 871 */       lnEnd.setLen(STLineEndLength.Enum.forInt(style.ooxmlId));
/*  820:     */     }
/*  821:     */   }
/*  822:     */   
/*  823:     */   public LineDecoration.DecorationSize getLineHeadLength()
/*  824:     */   {
/*  825: 879 */     CTLineProperties ln = getLn(this, false);
/*  826:     */     
/*  827: 881 */     LineDecoration.DecorationSize ds = LineDecoration.DecorationSize.MEDIUM;
/*  828: 882 */     if ((ln != null) && (ln.isSetHeadEnd()) && (ln.getHeadEnd().isSetLen())) {
/*  829: 883 */       ds = LineDecoration.DecorationSize.fromOoxmlId(ln.getHeadEnd().getLen().intValue());
/*  830:     */     }
/*  831: 885 */     return ds;
/*  832:     */   }
/*  833:     */   
/*  834:     */   public void setLineTailDecoration(LineDecoration.DecorationShape style)
/*  835:     */   {
/*  836: 892 */     CTLineProperties ln = getLn(this, true);
/*  837: 893 */     if (ln == null) {
/*  838: 894 */       return;
/*  839:     */     }
/*  840: 897 */     CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();
/*  841: 898 */     if (style == null)
/*  842:     */     {
/*  843: 899 */       if (lnEnd.isSetType()) {
/*  844: 900 */         lnEnd.unsetType();
/*  845:     */       }
/*  846:     */     }
/*  847:     */     else {
/*  848: 903 */       lnEnd.setType(STLineEndType.Enum.forInt(style.ooxmlId));
/*  849:     */     }
/*  850:     */   }
/*  851:     */   
/*  852:     */   public LineDecoration.DecorationShape getLineTailDecoration()
/*  853:     */   {
/*  854: 911 */     CTLineProperties ln = getLn(this, false);
/*  855:     */     
/*  856: 913 */     LineDecoration.DecorationShape ds = LineDecoration.DecorationShape.NONE;
/*  857: 914 */     if ((ln != null) && (ln.isSetTailEnd()) && (ln.getTailEnd().isSetType())) {
/*  858: 915 */       ds = LineDecoration.DecorationShape.fromOoxmlId(ln.getTailEnd().getType().intValue());
/*  859:     */     }
/*  860: 917 */     return ds;
/*  861:     */   }
/*  862:     */   
/*  863:     */   public void setLineTailWidth(LineDecoration.DecorationSize style)
/*  864:     */   {
/*  865: 924 */     CTLineProperties ln = getLn(this, true);
/*  866: 925 */     if (ln == null) {
/*  867: 926 */       return;
/*  868:     */     }
/*  869: 929 */     CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();
/*  870: 930 */     if (style == null)
/*  871:     */     {
/*  872: 931 */       if (lnEnd.isSetW()) {
/*  873: 932 */         lnEnd.unsetW();
/*  874:     */       }
/*  875:     */     }
/*  876:     */     else {
/*  877: 935 */       lnEnd.setW(STLineEndWidth.Enum.forInt(style.ooxmlId));
/*  878:     */     }
/*  879:     */   }
/*  880:     */   
/*  881:     */   public LineDecoration.DecorationSize getLineTailWidth()
/*  882:     */   {
/*  883: 943 */     CTLineProperties ln = getLn(this, false);
/*  884: 944 */     LineDecoration.DecorationSize ds = LineDecoration.DecorationSize.MEDIUM;
/*  885: 945 */     if ((ln != null) && (ln.isSetTailEnd()) && (ln.getTailEnd().isSetW())) {
/*  886: 946 */       ds = LineDecoration.DecorationSize.fromOoxmlId(ln.getTailEnd().getW().intValue());
/*  887:     */     }
/*  888: 948 */     return ds;
/*  889:     */   }
/*  890:     */   
/*  891:     */   public void setLineTailLength(LineDecoration.DecorationSize style)
/*  892:     */   {
/*  893: 955 */     CTLineProperties ln = getLn(this, true);
/*  894: 956 */     if (ln == null) {
/*  895: 957 */       return;
/*  896:     */     }
/*  897: 960 */     CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();
/*  898: 961 */     if (style == null)
/*  899:     */     {
/*  900: 962 */       if (lnEnd.isSetLen()) {
/*  901: 963 */         lnEnd.unsetLen();
/*  902:     */       }
/*  903:     */     }
/*  904:     */     else {
/*  905: 966 */       lnEnd.setLen(STLineEndLength.Enum.forInt(style.ooxmlId));
/*  906:     */     }
/*  907:     */   }
/*  908:     */   
/*  909:     */   public LineDecoration.DecorationSize getLineTailLength()
/*  910:     */   {
/*  911: 974 */     CTLineProperties ln = getLn(this, false);
/*  912:     */     
/*  913: 976 */     LineDecoration.DecorationSize ds = LineDecoration.DecorationSize.MEDIUM;
/*  914: 977 */     if ((ln != null) && (ln.isSetTailEnd()) && (ln.getTailEnd().isSetLen())) {
/*  915: 978 */       ds = LineDecoration.DecorationSize.fromOoxmlId(ln.getTailEnd().getLen().intValue());
/*  916:     */     }
/*  917: 980 */     return ds;
/*  918:     */   }
/*  919:     */   
/*  920:     */   public boolean isPlaceholder()
/*  921:     */   {
/*  922: 984 */     CTPlaceholder ph = getCTPlaceholder();
/*  923: 985 */     return ph != null;
/*  924:     */   }
/*  925:     */   
/*  926:     */   public Guide getAdjustValue(String name)
/*  927:     */   {
/*  928: 990 */     XSLFPropertiesDelegate.XSLFGeometryProperties gp = XSLFPropertiesDelegate.getGeometryDelegate(getShapeProperties());
/*  929: 992 */     if ((gp != null) && (gp.isSetPrstGeom()) && (gp.getPrstGeom().isSetAvLst())) {
/*  930: 993 */       for (CTGeomGuide g : gp.getPrstGeom().getAvLst().getGdArray()) {
/*  931: 994 */         if (g.getName().equals(name)) {
/*  932: 995 */           return new Guide(g.getName(), g.getFmla());
/*  933:     */         }
/*  934:     */       }
/*  935:     */     }
/*  936:1000 */     return null;
/*  937:     */   }
/*  938:     */   
/*  939:     */   public LineDecoration getLineDecoration()
/*  940:     */   {
/*  941:1005 */     new LineDecoration()
/*  942:     */     {
/*  943:     */       public LineDecoration.DecorationShape getHeadShape()
/*  944:     */       {
/*  945:1008 */         return XSLFSimpleShape.this.getLineHeadDecoration();
/*  946:     */       }
/*  947:     */       
/*  948:     */       public LineDecoration.DecorationSize getHeadWidth()
/*  949:     */       {
/*  950:1013 */         return XSLFSimpleShape.this.getLineHeadWidth();
/*  951:     */       }
/*  952:     */       
/*  953:     */       public LineDecoration.DecorationSize getHeadLength()
/*  954:     */       {
/*  955:1018 */         return XSLFSimpleShape.this.getLineHeadLength();
/*  956:     */       }
/*  957:     */       
/*  958:     */       public LineDecoration.DecorationShape getTailShape()
/*  959:     */       {
/*  960:1023 */         return XSLFSimpleShape.this.getLineTailDecoration();
/*  961:     */       }
/*  962:     */       
/*  963:     */       public LineDecoration.DecorationSize getTailWidth()
/*  964:     */       {
/*  965:1028 */         return XSLFSimpleShape.this.getLineTailWidth();
/*  966:     */       }
/*  967:     */       
/*  968:     */       public LineDecoration.DecorationSize getTailLength()
/*  969:     */       {
/*  970:1033 */         return XSLFSimpleShape.this.getLineTailLength();
/*  971:     */       }
/*  972:     */     };
/*  973:     */   }
/*  974:     */   
/*  975:     */   public FillStyle getFillStyle()
/*  976:     */   {
/*  977:1045 */     new FillStyle()
/*  978:     */     {
/*  979:     */       public PaintStyle getPaint()
/*  980:     */       {
/*  981:1048 */         return XSLFSimpleShape.this.getFillPaint();
/*  982:     */       }
/*  983:     */     };
/*  984:     */   }
/*  985:     */   
/*  986:     */   public StrokeStyle getStrokeStyle()
/*  987:     */   {
/*  988:1055 */     new StrokeStyle()
/*  989:     */     {
/*  990:     */       public PaintStyle getPaint()
/*  991:     */       {
/*  992:1058 */         return XSLFSimpleShape.this.getLinePaint();
/*  993:     */       }
/*  994:     */       
/*  995:     */       public StrokeStyle.LineCap getLineCap()
/*  996:     */       {
/*  997:1063 */         return XSLFSimpleShape.this.getLineCap();
/*  998:     */       }
/*  999:     */       
/* 1000:     */       public StrokeStyle.LineDash getLineDash()
/* 1001:     */       {
/* 1002:1068 */         return XSLFSimpleShape.this.getLineDash();
/* 1003:     */       }
/* 1004:     */       
/* 1005:     */       public double getLineWidth()
/* 1006:     */       {
/* 1007:1073 */         return XSLFSimpleShape.this.getLineWidth();
/* 1008:     */       }
/* 1009:     */       
/* 1010:     */       public StrokeStyle.LineCompound getLineCompound()
/* 1011:     */       {
/* 1012:1078 */         return XSLFSimpleShape.this.getLineCompound();
/* 1013:     */       }
/* 1014:     */     };
/* 1015:     */   }
/* 1016:     */   
/* 1017:     */   public void setStrokeStyle(Object... styles)
/* 1018:     */   {
/* 1019:1086 */     if (styles.length == 0)
/* 1020:     */     {
/* 1021:1088 */       setLineColor(null);
/* 1022:1089 */       return;
/* 1023:     */     }
/* 1024:1093 */     for (Object st : styles) {
/* 1025:1094 */       if ((st instanceof Number)) {
/* 1026:1095 */         setLineWidth(((Number)st).doubleValue());
/* 1027:1096 */       } else if ((st instanceof StrokeStyle.LineCap)) {
/* 1028:1097 */         setLineCap((StrokeStyle.LineCap)st);
/* 1029:1098 */       } else if ((st instanceof StrokeStyle.LineDash)) {
/* 1030:1099 */         setLineDash((StrokeStyle.LineDash)st);
/* 1031:1100 */       } else if ((st instanceof StrokeStyle.LineCompound)) {
/* 1032:1101 */         setLineCompound((StrokeStyle.LineCompound)st);
/* 1033:1102 */       } else if ((st instanceof Color)) {
/* 1034:1103 */         setLineColor((Color)st);
/* 1035:     */       }
/* 1036:     */     }
/* 1037:     */   }
/* 1038:     */   
/* 1039:     */   public void setPlaceholder(Placeholder placeholder)
/* 1040:     */   {
/* 1041:1110 */     super.setPlaceholder(placeholder);
/* 1042:     */   }
/* 1043:     */   
/* 1044:     */   public XSLFHyperlink getHyperlink()
/* 1045:     */   {
/* 1046:1115 */     CTNonVisualDrawingProps cNvPr = getCNvPr();
/* 1047:1116 */     if (!cNvPr.isSetHlinkClick()) {
/* 1048:1117 */       return null;
/* 1049:     */     }
/* 1050:1119 */     return new XSLFHyperlink(cNvPr.getHlinkClick(), getSheet());
/* 1051:     */   }
/* 1052:     */   
/* 1053:     */   public XSLFHyperlink createHyperlink()
/* 1054:     */   {
/* 1055:1124 */     XSLFHyperlink hl = getHyperlink();
/* 1056:1125 */     if (hl == null)
/* 1057:     */     {
/* 1058:1126 */       CTNonVisualDrawingProps cNvPr = getCNvPr();
/* 1059:1127 */       hl = new XSLFHyperlink(cNvPr.addNewHlinkClick(), getSheet());
/* 1060:     */     }
/* 1061:1129 */     return hl;
/* 1062:     */   }
/* 1063:     */   
/* 1064:     */   private static CTLineProperties getLn(XSLFShape shape, boolean create)
/* 1065:     */   {
/* 1066:1133 */     XmlObject pr = shape.getShapeProperties();
/* 1067:1134 */     if (!(pr instanceof CTShapeProperties))
/* 1068:     */     {
/* 1069:1135 */       LOG.log(5, new Object[] { shape.getClass() + " doesn't have line properties" });
/* 1070:1136 */       return null;
/* 1071:     */     }
/* 1072:1139 */     CTShapeProperties spr = (CTShapeProperties)pr;
/* 1073:1140 */     return (spr.isSetLn()) || (!create) ? spr.getLn() : spr.addNewLn();
/* 1074:     */   }
/* 1075:     */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSimpleShape

 * JD-Core Version:    0.7.0.1

 */