/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.Insets;
/*   6:    */ import java.awt.geom.Rectangle2D;
/*   7:    */ import java.awt.geom.Rectangle2D.Double;
/*   8:    */ import java.io.IOException;
/*   9:    */ import org.apache.poi.sl.usermodel.PictureData;
/*  10:    */ import org.apache.poi.sl.usermodel.PictureData.PictureType;
/*  11:    */ import org.apache.poi.sl.usermodel.PictureShape;
/*  12:    */ import org.apache.poi.sl.usermodel.RectAlign;
/*  13:    */ import org.apache.poi.util.POILogFactory;
/*  14:    */ import org.apache.poi.util.POILogger;
/*  15:    */ 
/*  16:    */ public class DrawPictureShape
/*  17:    */   extends DrawSimpleShape
/*  18:    */ {
/*  19: 35 */   private static final POILogger LOG = POILogFactory.getLogger(DrawPictureShape.class);
/*  20:    */   private static final String WMF_IMAGE_RENDERER = "org.apache.poi.hwmf.draw.HwmfSLImageRenderer";
/*  21:    */   
/*  22:    */   public DrawPictureShape(PictureShape<?, ?> shape)
/*  23:    */   {
/*  24: 39 */     super(shape);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void drawContent(Graphics2D graphics)
/*  28:    */   {
/*  29: 44 */     PictureData data = getShape().getPictureData();
/*  30: 45 */     if (data == null) {
/*  31: 45 */       return;
/*  32:    */     }
/*  33: 47 */     Rectangle2D anchor = getAnchor(graphics, getShape());
/*  34: 48 */     Insets insets = getShape().getClipping();
/*  35:    */     try
/*  36:    */     {
/*  37: 51 */       ImageRenderer renderer = getImageRenderer(graphics, data.getContentType());
/*  38: 52 */       renderer.loadImage(data.getData(), data.getContentType());
/*  39: 53 */       renderer.drawImage(graphics, anchor, insets);
/*  40:    */     }
/*  41:    */     catch (IOException e)
/*  42:    */     {
/*  43: 55 */       LOG.log(7, new Object[] { "image can't be loaded/rendered.", e });
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static ImageRenderer getImageRenderer(Graphics2D graphics, String contentType)
/*  48:    */   {
/*  49: 66 */     ImageRenderer renderer = (ImageRenderer)graphics.getRenderingHint(Drawable.IMAGE_RENDERER);
/*  50: 67 */     if (renderer != null) {
/*  51: 68 */       return renderer;
/*  52:    */     }
/*  53: 71 */     if (PictureData.PictureType.WMF.contentType.equals(contentType)) {
/*  54:    */       try
/*  55:    */       {
/*  56: 74 */         Class<? extends ImageRenderer> irc = Thread.currentThread().getContextClassLoader().loadClass("org.apache.poi.hwmf.draw.HwmfSLImageRenderer");
/*  57:    */         
/*  58: 76 */         return (ImageRenderer)irc.newInstance();
/*  59:    */       }
/*  60:    */       catch (Exception e)
/*  61:    */       {
/*  62: 80 */         LOG.log(7, new Object[] { "WMF image renderer is not on the classpath - include poi-scratchpad jar!", e });
/*  63:    */       }
/*  64:    */     }
/*  65: 84 */     return new BitmapImageRenderer();
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected PictureShape<?, ?> getShape()
/*  69:    */   {
/*  70: 89 */     return (PictureShape)this.shape;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void resize()
/*  74:    */   {
/*  75: 99 */     PictureShape<?, ?> ps = getShape();
/*  76:100 */     Dimension dim = ps.getPictureData().getImageDimension();
/*  77:    */     
/*  78:102 */     Rectangle2D origRect = ps.getAnchor();
/*  79:103 */     double x = origRect.getX();
/*  80:104 */     double y = origRect.getY();
/*  81:105 */     double w = dim.getWidth();
/*  82:106 */     double h = dim.getHeight();
/*  83:107 */     ps.setAnchor(new Double(x, y, w, h));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void resize(Rectangle2D target)
/*  87:    */   {
/*  88:118 */     resize(target, RectAlign.CENTER);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void resize(Rectangle2D target, RectAlign align)
/*  92:    */   {
/*  93:133 */     PictureShape<?, ?> ps = getShape();
/*  94:134 */     Dimension dim = ps.getPictureData().getImageDimension();
/*  95:135 */     if ((dim.width <= 0) || (dim.height <= 0))
/*  96:    */     {
/*  97:137 */       ps.setAnchor(target);
/*  98:138 */       return;
/*  99:    */     }
/* 100:141 */     double w = target.getWidth();
/* 101:142 */     double h = target.getHeight();
/* 102:    */     
/* 103:    */ 
/* 104:145 */     double sx = w / dim.width;
/* 105:146 */     double sy = h / dim.height;
/* 106:    */     
/* 107:    */ 
/* 108:149 */     double dx = 0.0D;double dy = 0.0D;
/* 109:151 */     if (sx > sy)
/* 110:    */     {
/* 111:153 */       w = sy * dim.width;
/* 112:154 */       dx = target.getWidth() - w;
/* 113:    */     }
/* 114:155 */     else if (sy > sx)
/* 115:    */     {
/* 116:157 */       h = sx * dim.height;
/* 117:158 */       dy = target.getHeight() - h;
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:161 */       ps.setAnchor(target);
/* 122:162 */       return;
/* 123:    */     }
/* 124:166 */     double x = target.getX();
/* 125:167 */     double y = target.getY();
/* 126:168 */     switch (1.$SwitchMap$org$apache$poi$sl$usermodel$RectAlign[align.ordinal()])
/* 127:    */     {
/* 128:    */     case 1: 
/* 129:170 */       x += dx / 2.0D;
/* 130:171 */       break;
/* 131:    */     case 2: 
/* 132:173 */       x += dx;
/* 133:174 */       break;
/* 134:    */     case 3: 
/* 135:176 */       x += dx;
/* 136:177 */       y += dy / 2.0D;
/* 137:178 */       break;
/* 138:    */     case 4: 
/* 139:180 */       x += dx;
/* 140:181 */       y += dy;
/* 141:182 */       break;
/* 142:    */     case 5: 
/* 143:184 */       x += dx / 2.0D;
/* 144:185 */       y += dy;
/* 145:186 */       break;
/* 146:    */     case 6: 
/* 147:188 */       y += dy;
/* 148:189 */       break;
/* 149:    */     case 7: 
/* 150:191 */       y += dy / 2.0D;
/* 151:192 */       break;
/* 152:    */     case 8: 
/* 153:    */       break;
/* 154:    */     default: 
/* 155:197 */       x += dx / 2.0D;
/* 156:198 */       y += dy / 2.0D;
/* 157:    */     }
/* 158:202 */     ps.setAnchor(new Double(x, y, w, h));
/* 159:    */   }
/* 160:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.DrawPictureShape

 * JD-Core Version:    0.7.0.1

 */