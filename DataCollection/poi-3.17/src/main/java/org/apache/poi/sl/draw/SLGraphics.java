/*    1:     */ package org.apache.poi.sl.draw;
/*    2:     */ 
/*    3:     */ import java.awt.BasicStroke;
/*    4:     */ import java.awt.Color;
/*    5:     */ import java.awt.Composite;
/*    6:     */ import java.awt.Font;
/*    7:     */ import java.awt.FontMetrics;
/*    8:     */ import java.awt.Graphics;
/*    9:     */ import java.awt.Graphics2D;
/*   10:     */ import java.awt.GraphicsConfiguration;
/*   11:     */ import java.awt.GraphicsDevice;
/*   12:     */ import java.awt.GraphicsEnvironment;
/*   13:     */ import java.awt.Image;
/*   14:     */ import java.awt.Paint;
/*   15:     */ import java.awt.Polygon;
/*   16:     */ import java.awt.Rectangle;
/*   17:     */ import java.awt.RenderingHints;
/*   18:     */ import java.awt.RenderingHints.Key;
/*   19:     */ import java.awt.Shape;
/*   20:     */ import java.awt.Stroke;
/*   21:     */ import java.awt.Toolkit;
/*   22:     */ import java.awt.font.FontRenderContext;
/*   23:     */ import java.awt.font.GlyphVector;
/*   24:     */ import java.awt.font.TextLayout;
/*   25:     */ import java.awt.geom.AffineTransform;
/*   26:     */ import java.awt.geom.Arc2D;
/*   27:     */ import java.awt.geom.Arc2D.Double;
/*   28:     */ import java.awt.geom.Ellipse2D;
/*   29:     */ import java.awt.geom.Ellipse2D.Double;
/*   30:     */ import java.awt.geom.GeneralPath;
/*   31:     */ import java.awt.geom.Line2D;
/*   32:     */ import java.awt.geom.Line2D.Double;
/*   33:     */ import java.awt.geom.Path2D.Double;
/*   34:     */ import java.awt.geom.RoundRectangle2D;
/*   35:     */ import java.awt.geom.RoundRectangle2D.Double;
/*   36:     */ import java.awt.image.BufferedImage;
/*   37:     */ import java.awt.image.BufferedImageOp;
/*   38:     */ import java.awt.image.ImageObserver;
/*   39:     */ import java.awt.image.RenderedImage;
/*   40:     */ import java.awt.image.renderable.RenderableImage;
/*   41:     */ import java.text.AttributedCharacterIterator;
/*   42:     */ import java.util.List;
/*   43:     */ import java.util.Map;
/*   44:     */ import org.apache.poi.sl.usermodel.FreeformShape;
/*   45:     */ import org.apache.poi.sl.usermodel.GroupShape;
/*   46:     */ import org.apache.poi.sl.usermodel.Insets2D;
/*   47:     */ import org.apache.poi.sl.usermodel.SimpleShape;
/*   48:     */ import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
/*   49:     */ import org.apache.poi.sl.usermodel.TextBox;
/*   50:     */ import org.apache.poi.sl.usermodel.TextParagraph;
/*   51:     */ import org.apache.poi.sl.usermodel.TextRun;
/*   52:     */ import org.apache.poi.sl.usermodel.VerticalAlignment;
/*   53:     */ import org.apache.poi.util.NotImplemented;
/*   54:     */ import org.apache.poi.util.POILogFactory;
/*   55:     */ import org.apache.poi.util.POILogger;
/*   56:     */ import org.apache.poi.util.SuppressForbidden;
/*   57:     */ 
/*   58:     */ public final class SLGraphics
/*   59:     */   extends Graphics2D
/*   60:     */   implements Cloneable
/*   61:     */ {
/*   62:  75 */   protected POILogger log = POILogFactory.getLogger(getClass());
/*   63:     */   private GroupShape<?, ?> _group;
/*   64:     */   private AffineTransform _transform;
/*   65:     */   private Stroke _stroke;
/*   66:     */   private Paint _paint;
/*   67:     */   private Font _font;
/*   68:     */   private Color _foreground;
/*   69:     */   private Color _background;
/*   70:     */   private RenderingHints _hints;
/*   71:     */   
/*   72:     */   public SLGraphics(GroupShape<?, ?> group)
/*   73:     */   {
/*   74:  94 */     this._group = group;
/*   75:     */     
/*   76:  96 */     this._transform = new AffineTransform();
/*   77:  97 */     this._stroke = new BasicStroke();
/*   78:  98 */     this._paint = Color.black;
/*   79:  99 */     this._font = new Font("Arial", 0, 12);
/*   80: 100 */     this._background = Color.black;
/*   81: 101 */     this._foreground = Color.white;
/*   82: 102 */     this._hints = new RenderingHints(null);
/*   83:     */   }
/*   84:     */   
/*   85:     */   public GroupShape<?, ?> getShapeGroup()
/*   86:     */   {
/*   87: 109 */     return this._group;
/*   88:     */   }
/*   89:     */   
/*   90:     */   public Font getFont()
/*   91:     */   {
/*   92: 119 */     return this._font;
/*   93:     */   }
/*   94:     */   
/*   95:     */   public void setFont(Font font)
/*   96:     */   {
/*   97: 133 */     this._font = font;
/*   98:     */   }
/*   99:     */   
/*  100:     */   public Color getColor()
/*  101:     */   {
/*  102: 143 */     return this._foreground;
/*  103:     */   }
/*  104:     */   
/*  105:     */   public void setColor(Color c)
/*  106:     */   {
/*  107: 155 */     setPaint(c);
/*  108:     */   }
/*  109:     */   
/*  110:     */   public Stroke getStroke()
/*  111:     */   {
/*  112: 166 */     return this._stroke;
/*  113:     */   }
/*  114:     */   
/*  115:     */   public void setStroke(Stroke s)
/*  116:     */   {
/*  117: 175 */     this._stroke = s;
/*  118:     */   }
/*  119:     */   
/*  120:     */   public Paint getPaint()
/*  121:     */   {
/*  122: 187 */     return this._paint;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public void setPaint(Paint paint)
/*  126:     */   {
/*  127: 201 */     if (paint == null) {
/*  128: 201 */       return;
/*  129:     */     }
/*  130: 203 */     this._paint = paint;
/*  131: 204 */     if ((paint instanceof Color)) {
/*  132: 204 */       this._foreground = ((Color)paint);
/*  133:     */     }
/*  134:     */   }
/*  135:     */   
/*  136:     */   public AffineTransform getTransform()
/*  137:     */   {
/*  138: 216 */     return new AffineTransform(this._transform);
/*  139:     */   }
/*  140:     */   
/*  141:     */   public void setTransform(AffineTransform Tx)
/*  142:     */   {
/*  143: 228 */     this._transform = new AffineTransform(Tx);
/*  144:     */   }
/*  145:     */   
/*  146:     */   public void draw(Shape shape)
/*  147:     */   {
/*  148: 248 */     Path2D.Double path = new Path2D.Double(this._transform.createTransformedShape(shape));
/*  149: 249 */     FreeformShape<?, ?> p = this._group.createFreeform();
/*  150: 250 */     p.setPath(path);
/*  151: 251 */     p.setFillColor(null);
/*  152: 252 */     applyStroke(p);
/*  153: 253 */     if ((this._paint instanceof Color)) {
/*  154: 254 */       p.setStrokeStyle(new Object[] { (Color)this._paint });
/*  155:     */     }
/*  156:     */   }
/*  157:     */   
/*  158:     */   public void drawString(String s, float x, float y)
/*  159:     */   {
/*  160: 284 */     TextBox<?, ?> txt = this._group.createTextBox();
/*  161:     */     
/*  162: 286 */     TextRun rt = (TextRun)((TextParagraph)txt.getTextParagraphs().get(0)).getTextRuns().get(0);
/*  163: 287 */     rt.setFontSize(Double.valueOf(this._font.getSize()));
/*  164: 288 */     rt.setFontFamily(this._font.getFamily());
/*  165: 290 */     if (getColor() != null) {
/*  166: 290 */       rt.setFontColor(DrawPaint.createSolidPaint(getColor()));
/*  167:     */     }
/*  168: 291 */     if (this._font.isBold()) {
/*  169: 291 */       rt.setBold(true);
/*  170:     */     }
/*  171: 292 */     if (this._font.isItalic()) {
/*  172: 292 */       rt.setItalic(true);
/*  173:     */     }
/*  174: 294 */     txt.setText(s);
/*  175:     */     
/*  176: 296 */     txt.setInsets(new Insets2D(0.0D, 0.0D, 0.0D, 0.0D));
/*  177: 297 */     txt.setWordWrap(false);
/*  178: 298 */     txt.setHorizontalCentered(Boolean.valueOf(false));
/*  179: 299 */     txt.setVerticalAlignment(VerticalAlignment.MIDDLE);
/*  180:     */     
/*  181:     */ 
/*  182: 302 */     TextLayout layout = new TextLayout(s, this._font, getFontRenderContext());
/*  183: 303 */     float ascent = layout.getAscent();
/*  184:     */     
/*  185: 305 */     float width = (float)Math.floor(layout.getAdvance());
/*  186:     */     
/*  187:     */ 
/*  188:     */ 
/*  189:     */ 
/*  190:     */ 
/*  191:     */ 
/*  192: 312 */     float height = ascent * 2.0F;
/*  193:     */     
/*  194:     */ 
/*  195:     */ 
/*  196:     */ 
/*  197:     */ 
/*  198:     */ 
/*  199: 319 */     y -= height / 2.0F + ascent / 2.0F;
/*  200:     */     
/*  201:     */ 
/*  202:     */ 
/*  203:     */ 
/*  204:     */ 
/*  205:     */ 
/*  206: 326 */     txt.setAnchor(new Rectangle((int)x, (int)y, (int)width, (int)height));
/*  207:     */   }
/*  208:     */   
/*  209:     */   public void fill(Shape shape)
/*  210:     */   {
/*  211: 344 */     Path2D.Double path = new Path2D.Double(this._transform.createTransformedShape(shape));
/*  212: 345 */     FreeformShape<?, ?> p = this._group.createFreeform();
/*  213: 346 */     p.setPath(path);
/*  214: 347 */     applyPaint(p);
/*  215: 348 */     p.setStrokeStyle(new Object[0]);
/*  216:     */   }
/*  217:     */   
/*  218:     */   public void translate(int x, int y)
/*  219:     */   {
/*  220: 363 */     this._transform.translate(x, y);
/*  221:     */   }
/*  222:     */   
/*  223:     */   @NotImplemented
/*  224:     */   public void clip(Shape s)
/*  225:     */   {
/*  226: 387 */     if (this.log.check(5)) {
/*  227: 388 */       this.log.log(5, new Object[] { "Not implemented" });
/*  228:     */     }
/*  229:     */   }
/*  230:     */   
/*  231:     */   @NotImplemented
/*  232:     */   public Shape getClip()
/*  233:     */   {
/*  234: 410 */     if (this.log.check(5)) {
/*  235: 411 */       this.log.log(5, new Object[] { "Not implemented" });
/*  236:     */     }
/*  237: 413 */     return null;
/*  238:     */   }
/*  239:     */   
/*  240:     */   public void scale(double sx, double sy)
/*  241:     */   {
/*  242: 436 */     this._transform.scale(sx, sy);
/*  243:     */   }
/*  244:     */   
/*  245:     */   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/*  246:     */   {
/*  247: 457 */     RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
/*  248: 458 */     draw(rect);
/*  249:     */   }
/*  250:     */   
/*  251:     */   public void drawString(String str, int x, int y)
/*  252:     */   {
/*  253: 473 */     drawString(str, x, y);
/*  254:     */   }
/*  255:     */   
/*  256:     */   public void fillOval(int x, int y, int width, int height)
/*  257:     */   {
/*  258: 488 */     Ellipse2D oval = new Ellipse2D.Double(x, y, width, height);
/*  259: 489 */     fill(oval);
/*  260:     */   }
/*  261:     */   
/*  262:     */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
/*  263:     */   {
/*  264: 511 */     RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
/*  265: 512 */     fill(rect);
/*  266:     */   }
/*  267:     */   
/*  268:     */   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/*  269:     */   {
/*  270: 552 */     Arc2D arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, 2);
/*  271: 553 */     fill(arc);
/*  272:     */   }
/*  273:     */   
/*  274:     */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
/*  275:     */   {
/*  276: 594 */     Arc2D arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, 0);
/*  277: 595 */     draw(arc);
/*  278:     */   }
/*  279:     */   
/*  280:     */   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
/*  281:     */   {
/*  282: 613 */     if (nPoints > 0)
/*  283:     */     {
/*  284: 614 */       GeneralPath path = new GeneralPath();
/*  285: 615 */       path.moveTo(xPoints[0], yPoints[0]);
/*  286: 616 */       for (int i = 1; i < nPoints; i++) {
/*  287: 617 */         path.lineTo(xPoints[i], yPoints[i]);
/*  288:     */       }
/*  289: 619 */       draw(path);
/*  290:     */     }
/*  291:     */   }
/*  292:     */   
/*  293:     */   public void drawOval(int x, int y, int width, int height)
/*  294:     */   {
/*  295: 641 */     Ellipse2D oval = new Ellipse2D.Double(x, y, width, height);
/*  296: 642 */     draw(oval);
/*  297:     */   }
/*  298:     */   
/*  299:     */   @NotImplemented
/*  300:     */   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer)
/*  301:     */   {
/*  302: 679 */     if (this.log.check(5)) {
/*  303: 680 */       this.log.log(5, new Object[] { "Not implemented" });
/*  304:     */     }
/*  305: 683 */     return false;
/*  306:     */   }
/*  307:     */   
/*  308:     */   @NotImplemented
/*  309:     */   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer)
/*  310:     */   {
/*  311: 729 */     if (this.log.check(5)) {
/*  312: 730 */       this.log.log(5, new Object[] { "Not implemented" });
/*  313:     */     }
/*  314: 733 */     return false;
/*  315:     */   }
/*  316:     */   
/*  317:     */   @NotImplemented
/*  318:     */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
/*  319:     */   {
/*  320: 789 */     if (this.log.check(5)) {
/*  321: 790 */       this.log.log(5, new Object[] { "Not implemented" });
/*  322:     */     }
/*  323: 792 */     return false;
/*  324:     */   }
/*  325:     */   
/*  326:     */   @NotImplemented
/*  327:     */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer)
/*  328:     */   {
/*  329: 854 */     if (this.log.check(5)) {
/*  330: 855 */       this.log.log(5, new Object[] { "Not implemented" });
/*  331:     */     }
/*  332: 857 */     return false;
/*  333:     */   }
/*  334:     */   
/*  335:     */   @NotImplemented
/*  336:     */   public boolean drawImage(Image img, int x, int y, ImageObserver observer)
/*  337:     */   {
/*  338: 895 */     if (this.log.check(5)) {
/*  339: 896 */       this.log.log(5, new Object[] { "Not implemented" });
/*  340:     */     }
/*  341: 898 */     return false;
/*  342:     */   }
/*  343:     */   
/*  344:     */   public void dispose() {}
/*  345:     */   
/*  346:     */   public void drawLine(int x1, int y1, int x2, int y2)
/*  347:     */   {
/*  348: 941 */     Line2D line = new Line2D.Double(x1, y1, x2, y2);
/*  349: 942 */     draw(line);
/*  350:     */   }
/*  351:     */   
/*  352:     */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
/*  353:     */   {
/*  354: 967 */     Polygon polygon = new Polygon(xPoints, yPoints, nPoints);
/*  355: 968 */     fill(polygon);
/*  356:     */   }
/*  357:     */   
/*  358:     */   public void fillRect(int x, int y, int width, int height)
/*  359:     */   {
/*  360: 991 */     Rectangle rect = new Rectangle(x, y, width, height);
/*  361: 992 */     fill(rect);
/*  362:     */   }
/*  363:     */   
/*  364:     */   public void drawRect(int x, int y, int width, int height)
/*  365:     */   {
/*  366:1012 */     Rectangle rect = new Rectangle(x, y, width, height);
/*  367:1013 */     draw(rect);
/*  368:     */   }
/*  369:     */   
/*  370:     */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
/*  371:     */   {
/*  372:1037 */     Polygon polygon = new Polygon(xPoints, yPoints, nPoints);
/*  373:1038 */     draw(polygon);
/*  374:     */   }
/*  375:     */   
/*  376:     */   public void clipRect(int x, int y, int width, int height)
/*  377:     */   {
/*  378:1061 */     clip(new Rectangle(x, y, width, height));
/*  379:     */   }
/*  380:     */   
/*  381:     */   @NotImplemented
/*  382:     */   public void setClip(Shape clip)
/*  383:     */   {
/*  384:1082 */     if (this.log.check(5)) {
/*  385:1083 */       this.log.log(5, new Object[] { "Not implemented" });
/*  386:     */     }
/*  387:     */   }
/*  388:     */   
/*  389:     */   public Rectangle getClipBounds()
/*  390:     */   {
/*  391:1105 */     Shape c = getClip();
/*  392:1106 */     if (c == null) {
/*  393:1107 */       return null;
/*  394:     */     }
/*  395:1109 */     return c.getBounds();
/*  396:     */   }
/*  397:     */   
/*  398:     */   public void drawString(AttributedCharacterIterator iterator, int x, int y)
/*  399:     */   {
/*  400:1126 */     drawString(iterator, x, y);
/*  401:     */   }
/*  402:     */   
/*  403:     */   public void clearRect(int x, int y, int width, int height)
/*  404:     */   {
/*  405:1149 */     Paint paint = getPaint();
/*  406:1150 */     setColor(getBackground());
/*  407:1151 */     fillRect(x, y, width, height);
/*  408:1152 */     setPaint(paint);
/*  409:     */   }
/*  410:     */   
/*  411:     */   public void copyArea(int x, int y, int width, int height, int dx, int dy) {}
/*  412:     */   
/*  413:     */   public void setClip(int x, int y, int width, int height)
/*  414:     */   {
/*  415:1173 */     setClip(new Rectangle(x, y, width, height));
/*  416:     */   }
/*  417:     */   
/*  418:     */   public void rotate(double theta)
/*  419:     */   {
/*  420:1193 */     this._transform.rotate(theta);
/*  421:     */   }
/*  422:     */   
/*  423:     */   public void rotate(double theta, double x, double y)
/*  424:     */   {
/*  425:1216 */     this._transform.rotate(theta, x, y);
/*  426:     */   }
/*  427:     */   
/*  428:     */   public void shear(double shx, double shy)
/*  429:     */   {
/*  430:1238 */     this._transform.shear(shx, shy);
/*  431:     */   }
/*  432:     */   
/*  433:     */   public FontRenderContext getFontRenderContext()
/*  434:     */   {
/*  435:1263 */     boolean isAntiAliased = RenderingHints.VALUE_TEXT_ANTIALIAS_ON.equals(getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
/*  436:     */     
/*  437:1265 */     boolean usesFractionalMetrics = RenderingHints.VALUE_FRACTIONALMETRICS_ON.equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS));
/*  438:     */     
/*  439:     */ 
/*  440:     */ 
/*  441:1269 */     return new FontRenderContext(new AffineTransform(), isAntiAliased, usesFractionalMetrics);
/*  442:     */   }
/*  443:     */   
/*  444:     */   public void transform(AffineTransform Tx)
/*  445:     */   {
/*  446:1290 */     this._transform.concatenate(Tx);
/*  447:     */   }
/*  448:     */   
/*  449:     */   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y)
/*  450:     */   {
/*  451:1318 */     img = op.filter(img, null);
/*  452:1319 */     drawImage(img, x, y, null);
/*  453:     */   }
/*  454:     */   
/*  455:     */   public void setBackground(Color color)
/*  456:     */   {
/*  457:1339 */     if (color == null) {
/*  458:1340 */       return;
/*  459:     */     }
/*  460:1342 */     this._background = color;
/*  461:     */   }
/*  462:     */   
/*  463:     */   public Color getBackground()
/*  464:     */   {
/*  465:1352 */     return this._background;
/*  466:     */   }
/*  467:     */   
/*  468:     */   @NotImplemented
/*  469:     */   public void setComposite(Composite comp)
/*  470:     */   {
/*  471:1382 */     if (this.log.check(5)) {
/*  472:1383 */       this.log.log(5, new Object[] { "Not implemented" });
/*  473:     */     }
/*  474:     */   }
/*  475:     */   
/*  476:     */   @NotImplemented
/*  477:     */   public Composite getComposite()
/*  478:     */   {
/*  479:1396 */     if (this.log.check(5)) {
/*  480:1397 */       this.log.log(5, new Object[] { "Not implemented" });
/*  481:     */     }
/*  482:1399 */     return null;
/*  483:     */   }
/*  484:     */   
/*  485:     */   public Object getRenderingHint(Key hintKey)
/*  486:     */   {
/*  487:1415 */     return this._hints.get(hintKey);
/*  488:     */   }
/*  489:     */   
/*  490:     */   public void setRenderingHint(Key hintKey, Object hintValue)
/*  491:     */   {
/*  492:1430 */     this._hints.put(hintKey, hintValue);
/*  493:     */   }
/*  494:     */   
/*  495:     */   public void drawGlyphVector(GlyphVector g, float x, float y)
/*  496:     */   {
/*  497:1461 */     Shape glyphOutline = g.getOutline(x, y);
/*  498:1462 */     fill(glyphOutline);
/*  499:     */   }
/*  500:     */   
/*  501:     */   public GraphicsConfiguration getDeviceConfiguration()
/*  502:     */   {
/*  503:1471 */     return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*  504:     */   }
/*  505:     */   
/*  506:     */   public void addRenderingHints(Map<?, ?> hints)
/*  507:     */   {
/*  508:1490 */     this._hints.putAll(hints);
/*  509:     */   }
/*  510:     */   
/*  511:     */   public void translate(double tx, double ty)
/*  512:     */   {
/*  513:1510 */     this._transform.translate(tx, ty);
/*  514:     */   }
/*  515:     */   
/*  516:     */   @NotImplemented
/*  517:     */   public void drawString(AttributedCharacterIterator iterator, float x, float y)
/*  518:     */   {
/*  519:1540 */     if (this.log.check(5)) {
/*  520:1541 */       this.log.log(5, new Object[] { "Not implemented" });
/*  521:     */     }
/*  522:     */   }
/*  523:     */   
/*  524:     */   public boolean hit(Rectangle rect, Shape s, boolean onStroke)
/*  525:     */   {
/*  526:1577 */     if (onStroke) {
/*  527:1578 */       s = getStroke().createStrokedShape(s);
/*  528:     */     }
/*  529:1581 */     s = getTransform().createTransformedShape(s);
/*  530:     */     
/*  531:1583 */     return s.intersects(rect);
/*  532:     */   }
/*  533:     */   
/*  534:     */   public RenderingHints getRenderingHints()
/*  535:     */   {
/*  536:1599 */     return this._hints;
/*  537:     */   }
/*  538:     */   
/*  539:     */   public void setRenderingHints(Map<?, ?> hints)
/*  540:     */   {
/*  541:1616 */     this._hints = new RenderingHints(null);
/*  542:1617 */     this._hints.putAll(hints);
/*  543:     */   }
/*  544:     */   
/*  545:     */   @NotImplemented
/*  546:     */   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs)
/*  547:     */   {
/*  548:1647 */     if (this.log.check(5)) {
/*  549:1648 */       this.log.log(5, new Object[] { "Not implemented" });
/*  550:     */     }
/*  551:1650 */     return false;
/*  552:     */   }
/*  553:     */   
/*  554:     */   @NotImplemented
/*  555:     */   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
/*  556:     */   {
/*  557:1693 */     if (this.log.check(5)) {
/*  558:1694 */       this.log.log(5, new Object[] { "Not implemented" });
/*  559:     */     }
/*  560:1696 */     return false;
/*  561:     */   }
/*  562:     */   
/*  563:     */   public Graphics create()
/*  564:     */   {
/*  565:     */     try
/*  566:     */     {
/*  567:1707 */       return (Graphics)clone();
/*  568:     */     }
/*  569:     */     catch (CloneNotSupportedException e)
/*  570:     */     {
/*  571:1709 */       throw new RuntimeException(e);
/*  572:     */     }
/*  573:     */   }
/*  574:     */   
/*  575:     */   @SuppressForbidden
/*  576:     */   public FontMetrics getFontMetrics(Font f)
/*  577:     */   {
/*  578:1724 */     return Toolkit.getDefaultToolkit().getFontMetrics(f);
/*  579:     */   }
/*  580:     */   
/*  581:     */   @NotImplemented
/*  582:     */   public void setXORMode(Color c1)
/*  583:     */   {
/*  584:1744 */     if (this.log.check(5)) {
/*  585:1745 */       this.log.log(5, new Object[] { "Not implemented" });
/*  586:     */     }
/*  587:     */   }
/*  588:     */   
/*  589:     */   @NotImplemented
/*  590:     */   public void setPaintMode()
/*  591:     */   {
/*  592:1758 */     if (this.log.check(5)) {
/*  593:1759 */       this.log.log(5, new Object[] { "Not implemented" });
/*  594:     */     }
/*  595:     */   }
/*  596:     */   
/*  597:     */   @NotImplemented
/*  598:     */   public void drawRenderedImage(RenderedImage img, AffineTransform xform)
/*  599:     */   {
/*  600:1796 */     if (this.log.check(5)) {
/*  601:1797 */       this.log.log(5, new Object[] { "Not implemented" });
/*  602:     */     }
/*  603:     */   }
/*  604:     */   
/*  605:     */   @NotImplemented
/*  606:     */   public void drawRenderableImage(RenderableImage img, AffineTransform xform)
/*  607:     */   {
/*  608:1824 */     if (this.log.check(5)) {
/*  609:1825 */       this.log.log(5, new Object[] { "Not implemented" });
/*  610:     */     }
/*  611:     */   }
/*  612:     */   
/*  613:     */   protected void applyStroke(SimpleShape<?, ?> shape)
/*  614:     */   {
/*  615:1830 */     if ((this._stroke instanceof BasicStroke))
/*  616:     */     {
/*  617:1831 */       BasicStroke bs = (BasicStroke)this._stroke;
/*  618:1832 */       shape.setStrokeStyle(new Object[] { Double.valueOf(bs.getLineWidth()) });
/*  619:1833 */       float[] dash = bs.getDashArray();
/*  620:1834 */       if (dash != null) {
/*  621:1836 */         shape.setStrokeStyle(new Object[] { StrokeStyle.LineDash.DASH });
/*  622:     */       }
/*  623:     */     }
/*  624:     */   }
/*  625:     */   
/*  626:     */   protected void applyPaint(SimpleShape<?, ?> shape)
/*  627:     */   {
/*  628:1842 */     if ((this._paint instanceof Color)) {
/*  629:1843 */       shape.setFillColor((Color)this._paint);
/*  630:     */     }
/*  631:     */   }
/*  632:     */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.SLGraphics

 * JD-Core Version:    0.7.0.1

 */