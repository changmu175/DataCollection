/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.font.TextLayout;
/*   5:    */ import java.awt.geom.AffineTransform;
/*   6:    */ import java.awt.geom.Rectangle2D;
/*   7:    */ import java.text.AttributedString;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Map;
/*  10:    */ import org.apache.poi.sl.usermodel.Background;
/*  11:    */ import org.apache.poi.sl.usermodel.ConnectorShape;
/*  12:    */ import org.apache.poi.sl.usermodel.FreeformShape;
/*  13:    */ import org.apache.poi.sl.usermodel.GraphicalFrame;
/*  14:    */ import org.apache.poi.sl.usermodel.GroupShape;
/*  15:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*  16:    */ import org.apache.poi.sl.usermodel.PictureShape;
/*  17:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*  18:    */ import org.apache.poi.sl.usermodel.Shape;
/*  19:    */ import org.apache.poi.sl.usermodel.Sheet;
/*  20:    */ import org.apache.poi.sl.usermodel.Slide;
/*  21:    */ import org.apache.poi.sl.usermodel.TableShape;
/*  22:    */ import org.apache.poi.sl.usermodel.TextBox;
/*  23:    */ import org.apache.poi.sl.usermodel.TextParagraph;
/*  24:    */ import org.apache.poi.sl.usermodel.TextShape;
/*  25:    */ import org.apache.poi.util.JvmBugs;
/*  26:    */ 
/*  27:    */ public class DrawFactory
/*  28:    */ {
/*  29: 46 */   protected static final ThreadLocal<DrawFactory> defaultFactory = new ThreadLocal();
/*  30:    */   
/*  31:    */   public static void setDefaultFactory(DrawFactory factory)
/*  32:    */   {
/*  33: 56 */     defaultFactory.set(factory);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static DrawFactory getInstance(Graphics2D graphics)
/*  37:    */   {
/*  38: 69 */     DrawFactory factory = null;
/*  39: 70 */     boolean isHint = false;
/*  40: 71 */     if (graphics != null)
/*  41:    */     {
/*  42: 72 */       factory = (DrawFactory)graphics.getRenderingHint(Drawable.DRAW_FACTORY);
/*  43: 73 */       isHint = factory != null;
/*  44:    */     }
/*  45: 76 */     if (factory == null) {
/*  46: 77 */       factory = (DrawFactory)defaultFactory.get();
/*  47:    */     }
/*  48: 80 */     if (factory == null) {
/*  49: 81 */       factory = new DrawFactory();
/*  50:    */     }
/*  51: 83 */     if ((graphics != null) && (!isHint)) {
/*  52: 84 */       graphics.setRenderingHint(Drawable.DRAW_FACTORY, factory);
/*  53:    */     }
/*  54: 86 */     return factory;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Drawable getDrawable(Shape<?, ?> shape)
/*  58:    */   {
/*  59: 90 */     if ((shape instanceof TextBox)) {
/*  60: 91 */       return getDrawable((TextBox)shape);
/*  61:    */     }
/*  62: 92 */     if ((shape instanceof FreeformShape)) {
/*  63: 93 */       return getDrawable((FreeformShape)shape);
/*  64:    */     }
/*  65: 94 */     if ((shape instanceof TextShape)) {
/*  66: 95 */       return getDrawable((TextShape)shape);
/*  67:    */     }
/*  68: 96 */     if ((shape instanceof TableShape)) {
/*  69: 97 */       return getDrawable((TableShape)shape);
/*  70:    */     }
/*  71: 98 */     if ((shape instanceof GroupShape)) {
/*  72: 99 */       return getDrawable((GroupShape)shape);
/*  73:    */     }
/*  74:100 */     if ((shape instanceof PictureShape)) {
/*  75:101 */       return getDrawable((PictureShape)shape);
/*  76:    */     }
/*  77:102 */     if ((shape instanceof GraphicalFrame)) {
/*  78:103 */       return getDrawable((GraphicalFrame)shape);
/*  79:    */     }
/*  80:104 */     if ((shape instanceof Background)) {
/*  81:105 */       return getDrawable((Background)shape);
/*  82:    */     }
/*  83:106 */     if ((shape instanceof ConnectorShape)) {
/*  84:107 */       return getDrawable((ConnectorShape)shape);
/*  85:    */     }
/*  86:108 */     if ((shape instanceof Slide)) {
/*  87:109 */       return getDrawable((Slide)shape);
/*  88:    */     }
/*  89:110 */     if ((shape instanceof MasterSheet)) {
/*  90:111 */       return getDrawable((MasterSheet)shape);
/*  91:    */     }
/*  92:112 */     if ((shape instanceof Sheet)) {
/*  93:113 */       return getDrawable((Sheet)shape);
/*  94:    */     }
/*  95:114 */     if (shape.getClass().isAnnotationPresent(DrawNotImplemented.class)) {
/*  96:115 */       return new DrawNothing(shape);
/*  97:    */     }
/*  98:118 */     throw new IllegalArgumentException("Unsupported shape type: " + shape.getClass());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public DrawSlide getDrawable(Slide<?, ?> sheet)
/* 102:    */   {
/* 103:122 */     return new DrawSlide(sheet);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public DrawSheet getDrawable(Sheet<?, ?> sheet)
/* 107:    */   {
/* 108:126 */     return new DrawSheet(sheet);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public DrawMasterSheet getDrawable(MasterSheet<?, ?> sheet)
/* 112:    */   {
/* 113:130 */     return new DrawMasterSheet(sheet);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public DrawTextBox getDrawable(TextBox<?, ?> shape)
/* 117:    */   {
/* 118:134 */     return new DrawTextBox(shape);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public DrawFreeformShape getDrawable(FreeformShape<?, ?> shape)
/* 122:    */   {
/* 123:138 */     return new DrawFreeformShape(shape);
/* 124:    */   }
/* 125:    */   
/* 126:    */   public DrawConnectorShape getDrawable(ConnectorShape<?, ?> shape)
/* 127:    */   {
/* 128:142 */     return new DrawConnectorShape(shape);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public DrawTableShape getDrawable(TableShape<?, ?> shape)
/* 132:    */   {
/* 133:146 */     return new DrawTableShape(shape);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public DrawTextShape getDrawable(TextShape<?, ?> shape)
/* 137:    */   {
/* 138:150 */     return new DrawTextShape(shape);
/* 139:    */   }
/* 140:    */   
/* 141:    */   public DrawGroupShape getDrawable(GroupShape<?, ?> shape)
/* 142:    */   {
/* 143:154 */     return new DrawGroupShape(shape);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public DrawPictureShape getDrawable(PictureShape<?, ?> shape)
/* 147:    */   {
/* 148:158 */     return new DrawPictureShape(shape);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public DrawGraphicalFrame getDrawable(GraphicalFrame<?, ?> shape)
/* 152:    */   {
/* 153:162 */     return new DrawGraphicalFrame(shape);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public DrawTextParagraph getDrawable(TextParagraph<?, ?, ?> paragraph)
/* 157:    */   {
/* 158:166 */     return new DrawTextParagraph(paragraph);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public DrawBackground getDrawable(Background<?, ?> shape)
/* 162:    */   {
/* 163:170 */     return new DrawBackground(shape);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public DrawTextFragment getTextFragment(TextLayout layout, AttributedString str)
/* 167:    */   {
/* 168:174 */     return new DrawTextFragment(layout, str);
/* 169:    */   }
/* 170:    */   
/* 171:    */   public DrawPaint getPaint(PlaceableShape<?, ?> shape)
/* 172:    */   {
/* 173:178 */     return new DrawPaint(shape);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void drawShape(Graphics2D graphics, Shape<?, ?> shape, Rectangle2D bounds)
/* 177:    */   {
/* 178:190 */     Rectangle2D shapeBounds = shape.getAnchor();
/* 179:191 */     if ((shapeBounds.isEmpty()) || ((bounds != null) && (bounds.isEmpty()))) {
/* 180:192 */       return;
/* 181:    */     }
/* 182:195 */     AffineTransform txg = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
/* 183:196 */     AffineTransform tx = new AffineTransform();
/* 184:    */     try
/* 185:    */     {
/* 186:198 */       if (bounds != null)
/* 187:    */       {
/* 188:199 */         double scaleX = bounds.getWidth() / shapeBounds.getWidth();
/* 189:200 */         double scaleY = bounds.getHeight() / shapeBounds.getHeight();
/* 190:201 */         tx.translate(bounds.getCenterX(), bounds.getCenterY());
/* 191:202 */         tx.scale(scaleX, scaleY);
/* 192:203 */         tx.translate(-shapeBounds.getCenterX(), -shapeBounds.getCenterY());
/* 193:    */       }
/* 194:205 */       graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, tx);
/* 195:    */       
/* 196:207 */       Drawable d = getDrawable(shape);
/* 197:208 */       d.applyTransform(graphics);
/* 198:209 */       d.draw(graphics);
/* 199:    */     }
/* 200:    */     finally
/* 201:    */     {
/* 202:211 */       graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, txg);
/* 203:    */     }
/* 204:    */   }
/* 205:    */   
/* 206:    */   public void fixFonts(Graphics2D graphics)
/* 207:    */   {
/* 208:223 */     if (!JvmBugs.hasLineBreakMeasurerBug()) {
/* 209:223 */       return;
/* 210:    */     }
/* 211:225 */     Map<String, String> fontMap = (Map)graphics.getRenderingHint(Drawable.FONT_MAP);
/* 212:226 */     if (fontMap == null)
/* 213:    */     {
/* 214:227 */       fontMap = new HashMap();
/* 215:228 */       graphics.setRenderingHint(Drawable.FONT_MAP, fontMap);
/* 216:    */     }
/* 217:231 */     String[][] fonts = { { "Calibri", "Lucida Sans" }, { "Cambria", "Lucida Bright" }, { "Times New Roman", "Lucida Bright" }, { "serif", "Lucida Bright" } };
/* 218:238 */     for (String[] f : fonts) {
/* 219:239 */       if (!fontMap.containsKey(f[0])) {
/* 220:240 */         fontMap.put(f[0], f[1]);
/* 221:    */       }
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   public DrawFontManager getFontManager(Graphics2D graphics)
/* 226:    */   {
/* 227:252 */     DrawFontManager fontHandler = (DrawFontManager)graphics.getRenderingHint(Drawable.FONT_HANDLER);
/* 228:253 */     return fontHandler != null ? fontHandler : new DrawFontManagerDefault();
/* 229:    */   }
/* 230:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawFactory
 * JD-Core Version:    0.7.0.1
 */