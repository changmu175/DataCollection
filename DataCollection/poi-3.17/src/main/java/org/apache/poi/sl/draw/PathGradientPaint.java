/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.AlphaComposite;
/*   4:    */ import java.awt.BasicStroke;
/*   5:    */ import java.awt.Color;
/*   6:    */ import java.awt.Graphics2D;
/*   7:    */ import java.awt.LinearGradientPaint;
/*   8:    */ import java.awt.MultipleGradientPaint.ColorSpaceType;
/*   9:    */ import java.awt.MultipleGradientPaint.CycleMethod;
/*  10:    */ import java.awt.Paint;
/*  11:    */ import java.awt.PaintContext;
/*  12:    */ import java.awt.Rectangle;
/*  13:    */ import java.awt.RenderingHints;
/*  14:    */ import java.awt.Shape;
/*  15:    */ import java.awt.geom.AffineTransform;
/*  16:    */ import java.awt.geom.Area;
/*  17:    */ import java.awt.geom.IllegalPathStateException;
/*  18:    */ import java.awt.geom.Point2D;
/*  19:    */ import java.awt.geom.Point2D.Double;
/*  20:    */ import java.awt.geom.Rectangle2D;
/*  21:    */ import java.awt.geom.Rectangle2D.Double;
/*  22:    */ import java.awt.image.BufferedImage;
/*  23:    */ import java.awt.image.ColorModel;
/*  24:    */ import java.awt.image.Raster;
/*  25:    */ import java.awt.image.WritableRaster;
/*  26:    */ 
/*  27:    */ class PathGradientPaint
/*  28:    */   implements Paint
/*  29:    */ {
/*  30:    */   protected final Color[] colors;
/*  31:    */   protected final float[] fractions;
/*  32:    */   protected final int capStyle;
/*  33:    */   protected final int joinStyle;
/*  34:    */   protected final int transparency;
/*  35:    */   
/*  36:    */   public PathGradientPaint(Color[] colors, float[] fractions)
/*  37:    */   {
/*  38: 37 */     this(colors, fractions, 1, 1);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public PathGradientPaint(Color[] colors, float[] fractions, int capStyle, int joinStyle)
/*  42:    */   {
/*  43: 41 */     this.colors = ((Color[])colors.clone());
/*  44: 42 */     this.fractions = ((float[])fractions.clone());
/*  45: 43 */     this.capStyle = capStyle;
/*  46: 44 */     this.joinStyle = joinStyle;
/*  47:    */     
/*  48:    */ 
/*  49: 47 */     boolean opaque = true;
/*  50: 48 */     for (Color c : colors) {
/*  51: 49 */       if (c != null) {
/*  52: 50 */         opaque = (opaque) && (c.getAlpha() == 255);
/*  53:    */       }
/*  54:    */     }
/*  55: 53 */     this.transparency = (opaque ? 1 : 3);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform transform, RenderingHints hints)
/*  59:    */   {
/*  60: 61 */     return new PathGradientContext(cm, deviceBounds, userBounds, transform, hints);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getTransparency()
/*  64:    */   {
/*  65: 65 */     return this.transparency;
/*  66:    */   }
/*  67:    */   
/*  68:    */   class PathGradientContext
/*  69:    */     implements PaintContext
/*  70:    */   {
/*  71:    */     protected final Rectangle deviceBounds;
/*  72:    */     protected final Rectangle2D userBounds;
/*  73:    */     protected final AffineTransform xform;
/*  74:    */     protected final RenderingHints hints;
/*  75:    */     protected final Shape shape;
/*  76:    */     protected final PaintContext pCtx;
/*  77:    */     protected final int gradientSteps;
/*  78:    */     WritableRaster raster;
/*  79:    */     
/*  80:    */     public PathGradientContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
/*  81:    */     {
/*  82: 90 */       this.shape = ((Shape)hints.get(Drawable.GRADIENT_SHAPE));
/*  83: 91 */       if (this.shape == null) {
/*  84: 92 */         throw new IllegalPathStateException("PathGradientPaint needs a shape to be set via the rendering hint Drawable.GRADIANT_SHAPE.");
/*  85:    */       }
/*  86: 95 */       this.deviceBounds = deviceBounds;
/*  87: 96 */       this.userBounds = userBounds;
/*  88: 97 */       this.xform = xform;
/*  89: 98 */       this.hints = hints;
/*  90:    */       
/*  91:100 */       this.gradientSteps = getGradientSteps(this.shape);
/*  92:    */       
/*  93:102 */       Point2D start = new Point2D.Double(0.0D, 0.0D);
/*  94:103 */       Point2D end = new Point2D.Double(this.gradientSteps, 0.0D);
/*  95:104 */       LinearGradientPaint gradientPaint = new LinearGradientPaint(start, end, PathGradientPaint.this.fractions, PathGradientPaint.this.colors, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
/*  96:    */       
/*  97:106 */       Rectangle bounds = new Rectangle(0, 0, this.gradientSteps, 1);
/*  98:107 */       this.pCtx = gradientPaint.createContext(cm, bounds, bounds, new AffineTransform(), hints);
/*  99:    */     }
/* 100:    */     
/* 101:    */     public void dispose() {}
/* 102:    */     
/* 103:    */     public ColorModel getColorModel()
/* 104:    */     {
/* 105:113 */       return this.pCtx.getColorModel();
/* 106:    */     }
/* 107:    */     
/* 108:    */     public Raster getRaster(int xOffset, int yOffset, int w, int h)
/* 109:    */     {
/* 110:117 */       ColorModel cm = getColorModel();
/* 111:118 */       if (this.raster == null) {
/* 112:118 */         createRaster();
/* 113:    */       }
/* 114:121 */       WritableRaster childRaster = cm.createCompatibleWritableRaster(w, h);
/* 115:122 */       Rectangle2D childRect = new Rectangle2D.Double(xOffset, yOffset, w, h);
/* 116:123 */       if (!childRect.intersects(this.deviceBounds)) {
/* 117:125 */         return childRaster;
/* 118:    */       }
/* 119:128 */       Rectangle2D destRect = new Rectangle2D.Double();
/* 120:129 */       Rectangle2D.intersect(childRect, this.deviceBounds, destRect);
/* 121:130 */       int dx = (int)(destRect.getX() - this.deviceBounds.getX());
/* 122:131 */       int dy = (int)(destRect.getY() - this.deviceBounds.getY());
/* 123:132 */       int dw = (int)destRect.getWidth();
/* 124:133 */       int dh = (int)destRect.getHeight();
/* 125:134 */       Object data = this.raster.getDataElements(dx, dy, dw, dh, null);
/* 126:135 */       dx = (int)(destRect.getX() - childRect.getX());
/* 127:136 */       dy = (int)(destRect.getY() - childRect.getY());
/* 128:137 */       childRaster.setDataElements(dx, dy, dw, dh, data);
/* 129:    */       
/* 130:139 */       return childRaster;
/* 131:    */     }
/* 132:    */     
/* 133:    */     protected int getGradientSteps(Shape gradientShape)
/* 134:    */     {
/* 135:143 */       Rectangle rect = gradientShape.getBounds();
/* 136:144 */       int lower = 1;
/* 137:145 */       int upper = (int)(Math.max(rect.getWidth(), rect.getHeight()) / 2.0D);
/* 138:146 */       while (lower < upper - 1)
/* 139:    */       {
/* 140:147 */         int mid = lower + (upper - lower) / 2;
/* 141:148 */         BasicStroke bs = new BasicStroke(mid, PathGradientPaint.this.capStyle, PathGradientPaint.this.joinStyle);
/* 142:149 */         Area area = new Area(bs.createStrokedShape(gradientShape));
/* 143:150 */         if (area.isSingular()) {
/* 144:151 */           upper = mid;
/* 145:    */         } else {
/* 146:153 */           lower = mid;
/* 147:    */         }
/* 148:    */       }
/* 149:156 */       return upper;
/* 150:    */     }
/* 151:    */     
/* 152:    */     protected void createRaster()
/* 153:    */     {
/* 154:162 */       ColorModel cm = getColorModel();
/* 155:163 */       this.raster = cm.createCompatibleWritableRaster((int)this.deviceBounds.getWidth(), (int)this.deviceBounds.getHeight());
/* 156:164 */       BufferedImage img = new BufferedImage(cm, this.raster, false, null);
/* 157:165 */       Graphics2D graphics = img.createGraphics();
/* 158:166 */       graphics.setRenderingHints(this.hints);
/* 159:167 */       graphics.translate(-this.deviceBounds.getX(), -this.deviceBounds.getY());
/* 160:168 */       graphics.transform(this.xform);
/* 161:    */       
/* 162:170 */       Raster img2 = this.pCtx.getRaster(0, 0, this.gradientSteps, 1);
/* 163:171 */       int[] rgb = new int[cm.getNumComponents()];
/* 164:173 */       for (int i = this.gradientSteps - 1; i >= 0; i--)
/* 165:    */       {
/* 166:174 */         img2.getPixel(i, 0, rgb);
/* 167:175 */         Color c = new Color(rgb[0], rgb[1], rgb[2]);
/* 168:176 */         if (rgb.length == 4) {
/* 169:178 */           graphics.setComposite(AlphaComposite.getInstance(2, rgb[3] / 255.0F));
/* 170:    */         }
/* 171:180 */         graphics.setStroke(new BasicStroke(i + 1, PathGradientPaint.this.capStyle, PathGradientPaint.this.joinStyle));
/* 172:181 */         graphics.setColor(c);
/* 173:182 */         graphics.draw(this.shape);
/* 174:    */       }
/* 175:185 */       graphics.dispose();
/* 176:    */     }
/* 177:    */   }
/* 178:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.PathGradientPaint
 * JD-Core Version:    0.7.0.1
 */