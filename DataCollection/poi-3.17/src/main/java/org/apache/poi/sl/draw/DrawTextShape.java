/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Graphics2D;
/*   4:    */ import java.awt.geom.AffineTransform;
/*   5:    */ import java.awt.geom.Rectangle2D;
/*   6:    */ import java.awt.image.BufferedImage;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import org.apache.poi.sl.usermodel.Insets2D;
/*   9:    */ import org.apache.poi.sl.usermodel.PlaceableShape;
/*  10:    */ import org.apache.poi.sl.usermodel.ShapeContainer;
/*  11:    */ import org.apache.poi.sl.usermodel.TextParagraph;
/*  12:    */ import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
/*  13:    */ import org.apache.poi.sl.usermodel.TextRun;
/*  14:    */ import org.apache.poi.sl.usermodel.TextShape;
/*  15:    */ import org.apache.poi.sl.usermodel.TextShape.TextDirection;
/*  16:    */ 
/*  17:    */ public class DrawTextShape
/*  18:    */   extends DrawSimpleShape
/*  19:    */ {
/*  20:    */   public DrawTextShape(TextShape<?, ?> shape)
/*  21:    */   {
/*  22: 38 */     super(shape);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void drawContent(Graphics2D graphics)
/*  26:    */   {
/*  27: 43 */     DrawFactory.getInstance(graphics).fixFonts(graphics);
/*  28:    */     
/*  29: 45 */     TextShape<?, ?> s = getShape();
/*  30:    */     
/*  31: 47 */     Rectangle2D anchor = DrawShape.getAnchor(graphics, s);
/*  32: 48 */     Insets2D insets = s.getInsets();
/*  33: 49 */     double x = anchor.getX() + insets.left;
/*  34: 50 */     double y = anchor.getY();
/*  35:    */     
/*  36:    */ 
/*  37: 53 */     AffineTransform tx = graphics.getTransform();
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44: 60 */     boolean vertFlip = s.getFlipVertical();
/*  45: 61 */     boolean horzFlip = s.getFlipHorizontal();
/*  46: 62 */     ShapeContainer<?, ?> sc = s.getParent();
/*  47: 63 */     while ((sc instanceof PlaceableShape))
/*  48:    */     {
/*  49: 64 */       PlaceableShape<?, ?> ps = (PlaceableShape)sc;
/*  50: 65 */       vertFlip ^= ps.getFlipVertical();
/*  51: 66 */       horzFlip ^= ps.getFlipHorizontal();
/*  52: 67 */       sc = ps.getParent();
/*  53:    */     }
/*  54: 72 */     if ((horzFlip ^ vertFlip))
/*  55:    */     {
/*  56: 73 */       double ax = anchor.getX();
/*  57: 74 */       double ay = anchor.getY();
/*  58: 75 */       graphics.translate(ax + anchor.getWidth(), ay);
/*  59: 76 */       graphics.scale(-1.0D, 1.0D);
/*  60: 77 */       graphics.translate(-ax, -ay);
/*  61:    */     }
/*  62: 80 */     Double textRot = s.getTextRotation();
/*  63: 81 */     if ((textRot != null) && (textRot.doubleValue() != 0.0D))
/*  64:    */     {
/*  65: 82 */       double cx = anchor.getCenterX();
/*  66: 83 */       double cy = anchor.getCenterY();
/*  67: 84 */       graphics.translate(cx, cy);
/*  68: 85 */       graphics.rotate(Math.toRadians(textRot.doubleValue()));
/*  69: 86 */       graphics.translate(-cx, -cy);
/*  70:    */     }
/*  71:    */     double textHeight;
/*  72: 92 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$VerticalAlignment[s.getVerticalAlignment().ordinal()])
/*  73:    */     {
/*  74:    */     case 1: 
/*  75:    */     default: 
/*  76: 95 */       y += insets.top;
/*  77: 96 */       break;
/*  78:    */     case 2: 
/*  79: 98 */       textHeight = getTextHeight(graphics);
/*  80: 99 */       y += anchor.getHeight() - textHeight - insets.bottom;
/*  81:100 */       break;
/*  82:    */     case 3: 
/*  83:102 */       textHeight = getTextHeight(graphics);
/*  84:103 */       double delta = anchor.getHeight() - textHeight - insets.top - insets.bottom;
/*  85:104 */       y += insets.top + delta / 2.0D;
/*  86:    */     }
/*  87:108 */     TextShape.TextDirection textDir = s.getTextDirection();
/*  88:109 */     if ((textDir == TextShape.TextDirection.VERTICAL) || (textDir == TextShape.TextDirection.VERTICAL_270))
/*  89:    */     {
/*  90:110 */       double deg = textDir == TextShape.TextDirection.VERTICAL ? 90.0D : 270.0D;
/*  91:111 */       double cx = anchor.getCenterX();
/*  92:112 */       double cy = anchor.getCenterY();
/*  93:113 */       graphics.translate(cx, cy);
/*  94:114 */       graphics.rotate(Math.toRadians(deg));
/*  95:115 */       graphics.translate(-cx, -cy);
/*  96:    */       
/*  97:    */ 
/*  98:    */ 
/*  99:119 */       double w = anchor.getWidth();
/* 100:120 */       double h = anchor.getHeight();
/* 101:121 */       double dx = (w - h) / 2.0D;
/* 102:122 */       graphics.translate(dx, -dx);
/* 103:    */     }
/* 104:125 */     drawParagraphs(graphics, x, y);
/* 105:    */     
/* 106:    */ 
/* 107:128 */     graphics.setTransform(tx);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public double drawParagraphs(Graphics2D graphics, double x, double y)
/* 111:    */   {
/* 112:137 */     DrawFactory fact = DrawFactory.getInstance(graphics);
/* 113:    */     
/* 114:139 */     double y0 = y;
/* 115:140 */     Iterator<? extends TextParagraph<?, ?, ? extends TextRun>> paragraphs = getShape().iterator();
/* 116:    */     
/* 117:142 */     boolean isFirstLine = true;
/* 118:143 */     for (int autoNbrIdx = 0; paragraphs.hasNext(); autoNbrIdx++)
/* 119:    */     {
/* 120:144 */       TextParagraph<?, ?, ? extends TextRun> p = (TextParagraph)paragraphs.next();
/* 121:145 */       DrawTextParagraph dp = fact.getDrawable(p);
/* 122:146 */       TextParagraph.BulletStyle bs = p.getBulletStyle();
/* 123:147 */       if ((bs == null) || (bs.getAutoNumberingScheme() == null))
/* 124:    */       {
/* 125:148 */         autoNbrIdx = -1;
/* 126:    */       }
/* 127:    */       else
/* 128:    */       {
/* 129:150 */         Integer startAt = bs.getAutoNumberingStartAt();
/* 130:151 */         if (startAt == null) {
/* 131:151 */           startAt = Integer.valueOf(1);
/* 132:    */         }
/* 133:153 */         if (startAt.intValue() > autoNbrIdx) {
/* 134:153 */           autoNbrIdx = startAt.intValue();
/* 135:    */         }
/* 136:    */       }
/* 137:155 */       dp.setAutoNumberingIdx(autoNbrIdx);
/* 138:156 */       dp.breakText(graphics);
/* 139:158 */       if (isFirstLine)
/* 140:    */       {
/* 141:159 */         y += dp.getFirstLineLeading();
/* 142:    */       }
/* 143:    */       else
/* 144:    */       {
/* 145:162 */         Double spaceBefore = p.getSpaceBefore();
/* 146:163 */         if (spaceBefore == null) {
/* 147:163 */           spaceBefore = Double.valueOf(0.0D);
/* 148:    */         }
/* 149:164 */         if (spaceBefore.doubleValue() > 0.0D) {
/* 150:167 */           y += spaceBefore.doubleValue() * 0.01D * dp.getFirstLineHeight();
/* 151:    */         } else {
/* 152:170 */           y += -spaceBefore.doubleValue();
/* 153:    */         }
/* 154:    */       }
/* 155:173 */       isFirstLine = false;
/* 156:    */       
/* 157:175 */       dp.setPosition(x, y);
/* 158:176 */       dp.draw(graphics);
/* 159:177 */       y += dp.getY();
/* 160:179 */       if (paragraphs.hasNext())
/* 161:    */       {
/* 162:180 */         Double spaceAfter = p.getSpaceAfter();
/* 163:181 */         if (spaceAfter == null) {
/* 164:181 */           spaceAfter = Double.valueOf(0.0D);
/* 165:    */         }
/* 166:182 */         if (spaceAfter.doubleValue() > 0.0D) {
/* 167:185 */           y += spaceAfter.doubleValue() * 0.01D * dp.getLastLineHeight();
/* 168:    */         } else {
/* 169:188 */           y += -spaceAfter.doubleValue();
/* 170:    */         }
/* 171:    */       }
/* 172:    */     }
/* 173:192 */     return y - y0;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public double getTextHeight()
/* 177:    */   {
/* 178:201 */     return getTextHeight(null);
/* 179:    */   }
/* 180:    */   
/* 181:    */   public double getTextHeight(Graphics2D oldGraphics)
/* 182:    */   {
/* 183:212 */     BufferedImage img = new BufferedImage(1, 1, 1);
/* 184:213 */     Graphics2D graphics = img.createGraphics();
/* 185:214 */     if (oldGraphics != null)
/* 186:    */     {
/* 187:215 */       graphics.addRenderingHints(oldGraphics.getRenderingHints());
/* 188:216 */       graphics.setTransform(oldGraphics.getTransform());
/* 189:    */     }
/* 190:218 */     DrawFactory.getInstance(graphics).fixFonts(graphics);
/* 191:219 */     return drawParagraphs(graphics, 0.0D, 0.0D);
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected TextShape<?, ? extends TextParagraph<?, ?, ? extends TextRun>> getShape()
/* 195:    */   {
/* 196:224 */     return (TextShape)this.shape;
/* 197:    */   }
/* 198:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.DrawTextShape
 * JD-Core Version:    0.7.0.1
 */