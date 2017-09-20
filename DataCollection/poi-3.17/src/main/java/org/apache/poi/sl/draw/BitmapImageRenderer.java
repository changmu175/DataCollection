/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Graphics;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Insets;
/*   7:    */ import java.awt.Shape;
/*   8:    */ import java.awt.geom.AffineTransform;
/*   9:    */ import java.awt.geom.Rectangle2D;
/*  10:    */ import java.awt.image.AffineTransformOp;
/*  11:    */ import java.awt.image.BufferedImage;
/*  12:    */ import java.awt.image.RescaleOp;
/*  13:    */ import java.io.ByteArrayInputStream;
/*  14:    */ import java.io.IOException;
/*  15:    */ import java.io.InputStream;
/*  16:    */ import java.util.Iterator;
/*  17:    */ import javax.imageio.ImageIO;
/*  18:    */ import javax.imageio.ImageReadParam;
/*  19:    */ import javax.imageio.ImageReader;
/*  20:    */ import javax.imageio.ImageTypeSpecifier;
/*  21:    */ import javax.imageio.stream.ImageInputStream;
/*  22:    */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*  23:    */ import org.apache.poi.util.POILogFactory;
/*  24:    */ import org.apache.poi.util.POILogger;
/*  25:    */ 
/*  26:    */ public class BitmapImageRenderer
/*  27:    */   implements ImageRenderer
/*  28:    */ {
/*  29: 49 */   private static final POILogger LOG = POILogFactory.getLogger(ImageRenderer.class);
/*  30:    */   protected BufferedImage img;
/*  31:    */   
/*  32:    */   public void loadImage(InputStream data, String contentType)
/*  33:    */     throws IOException
/*  34:    */   {
/*  35: 55 */     this.img = readImage(data, contentType);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void loadImage(byte[] data, String contentType)
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 60 */     this.img = readImage(new ByteArrayInputStream(data), contentType);
/*  42:    */   }
/*  43:    */   
/*  44:    */   private static BufferedImage readImage(InputStream data, String contentType)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47: 73 */     IOException lastException = null;
/*  48: 74 */     BufferedImage img = null;
/*  49: 75 */     if (data.markSupported()) {
/*  50: 76 */       data.mark(data.available());
/*  51:    */     }
/*  52: 81 */     ImageInputStream iis = new MemoryCacheImageInputStream(data);
/*  53:    */     try
/*  54:    */     {
/*  55: 83 */       iis = new MemoryCacheImageInputStream(data);
/*  56: 84 */       iis.mark();
/*  57:    */       
/*  58: 86 */       Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
/*  59: 87 */       while ((img == null) && (iter.hasNext()))
/*  60:    */       {
/*  61: 88 */         ImageReader reader = (ImageReader)iter.next();
/*  62: 89 */         ImageReadParam param = reader.getDefaultReadParam();
/*  63: 91 */         for (int mode = 0; (img == null) && (mode < 3); mode++)
/*  64:    */         {
/*  65: 92 */           lastException = null;
/*  66:    */           try {}catch (IOException e)
/*  67:    */           {
/*  68: 96 */             if (data.markSupported())
/*  69:    */             {
/*  70: 97 */               data.reset();
/*  71: 98 */               data.mark(data.available());
/*  72: 99 */               iis.close();
/*  73:100 */               iis = new MemoryCacheImageInputStream(data);
/*  74:    */             }
/*  75:    */             else
/*  76:    */             {
/*  77:103 */               lastException = e;
/*  78:104 */               break;
/*  79:    */             }
/*  80:    */           }
/*  81:107 */           iis.mark();
/*  82:    */           try
/*  83:    */           {
/*  84:111 */             switch (mode)
/*  85:    */             {
/*  86:    */             case 0: 
/*  87:113 */               reader.setInput(iis, false, true);
/*  88:114 */               img = reader.read(0, param);
/*  89:115 */               break;
/*  90:    */             case 1: 
/*  91:120 */               Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
/*  92:121 */               while (imageTypes.hasNext())
/*  93:    */               {
/*  94:122 */                 ImageTypeSpecifier imageTypeSpecifier = (ImageTypeSpecifier)imageTypes.next();
/*  95:123 */                 int bufferedImageType = imageTypeSpecifier.getBufferedImageType();
/*  96:124 */                 if (bufferedImageType == 10)
/*  97:    */                 {
/*  98:125 */                   param.setDestinationType(imageTypeSpecifier);
/*  99:126 */                   break;
/* 100:    */                 }
/* 101:    */               }
/* 102:129 */               reader.setInput(iis, false, true);
/* 103:130 */               img = reader.read(0, param);
/* 104:131 */               break;
/* 105:    */             case 2: 
/* 106:136 */               reader.setInput(iis, false, true);
/* 107:137 */               int height = reader.getHeight(0);
/* 108:138 */               int width = reader.getWidth(0);
/* 109:    */               
/* 110:140 */               Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
/* 111:141 */               if (imageTypes.hasNext())
/* 112:    */               {
/* 113:142 */                 ImageTypeSpecifier imageTypeSpecifier = (ImageTypeSpecifier)imageTypes.next();
/* 114:143 */                 img = imageTypeSpecifier.createBufferedImage(width, height);
/* 115:144 */                 param.setDestination(img);
/* 116:    */               }
/* 117:    */               else
/* 118:    */               {
/* 119:146 */                 lastException = new IOException("unable to load even a truncated version of the image.");
/* 120:147 */                 break;
/* 121:    */               }
/* 122:    */               try
/* 123:    */               {
/* 124:151 */                 reader.read(0, param);
/* 125:153 */                 if (img.getType() != 2)
/* 126:    */                 {
/* 127:154 */                   int y = findTruncatedBlackBox(img, width, height);
/* 128:155 */                   if (y < height)
/* 129:    */                   {
/* 130:156 */                     BufferedImage argbImg = new BufferedImage(width, height, 2);
/* 131:157 */                     Graphics2D g = argbImg.createGraphics();
/* 132:158 */                     g.clipRect(0, 0, width, y);
/* 133:159 */                     g.drawImage(img, 0, 0, null);
/* 134:160 */                     g.dispose();
/* 135:161 */                     img.flush();
/* 136:162 */                     img = argbImg;
/* 137:    */                   }
/* 138:    */                 }
/* 139:    */               }
/* 140:    */               finally
/* 141:    */               {
/* 142:153 */                 if (img.getType() != 2)
/* 143:    */                 {
/* 144:154 */                   int y = findTruncatedBlackBox(img, width, height);
/* 145:155 */                   if (y < height)
/* 146:    */                   {
/* 147:156 */                     BufferedImage argbImg = new BufferedImage(width, height, 2);
/* 148:157 */                     Graphics2D g = argbImg.createGraphics();
/* 149:158 */                     g.clipRect(0, 0, width, y);
/* 150:159 */                     g.drawImage(img, 0, 0, null);
/* 151:160 */                     g.dispose();
/* 152:161 */                     img.flush();
/* 153:162 */                     img = argbImg;
/* 154:    */                   }
/* 155:    */                 }
/* 156:    */               }
/* 157:    */             }
/* 158:    */           }
/* 159:    */           catch (IOException e)
/* 160:    */           {
/* 161:171 */             if (mode < 2) {
/* 162:172 */               lastException = e;
/* 163:    */             }
/* 164:    */           }
/* 165:    */           catch (RuntimeException e)
/* 166:    */           {
/* 167:175 */             if (mode < 2) {
/* 168:176 */               lastException = new IOException("ImageIO runtime exception - " + (mode == 0 ? "normal" : "fallback"), e);
/* 169:    */             }
/* 170:    */           }
/* 171:    */         }
/* 172:180 */         reader.dispose();
/* 173:    */       }
/* 174:    */     }
/* 175:    */     finally
/* 176:    */     {
/* 177:183 */       iis.close();
/* 178:    */     }
/* 179:187 */     if (img == null)
/* 180:    */     {
/* 181:188 */       if (lastException != null) {
/* 182:191 */         throw lastException;
/* 183:    */       }
/* 184:193 */       LOG.log(5, new Object[] { "Content-type: " + contentType + " is not support. Image ignored." });
/* 185:194 */       return null;
/* 186:    */     }
/* 187:198 */     if (img.getType() != 2)
/* 188:    */     {
/* 189:199 */       BufferedImage argbImg = new BufferedImage(img.getWidth(), img.getHeight(), 2);
/* 190:200 */       Graphics g = argbImg.getGraphics();
/* 191:201 */       g.drawImage(img, 0, 0, null);
/* 192:202 */       g.dispose();
/* 193:203 */       return argbImg;
/* 194:    */     }
/* 195:206 */     return img;
/* 196:    */   }
/* 197:    */   
/* 198:    */   private static int findTruncatedBlackBox(BufferedImage img, int width, int height)
/* 199:    */   {
/* 200:211 */     for (int h = height - 1; h > 0; h--) {
/* 201:213 */       for (int w = width - 1; w > 0; w -= width / 10)
/* 202:    */       {
/* 203:214 */         int p = img.getRGB(w, h);
/* 204:215 */         if (p != -16777216) {
/* 205:216 */           return h + 1;
/* 206:    */         }
/* 207:    */       }
/* 208:    */     }
/* 209:220 */     return 0;
/* 210:    */   }
/* 211:    */   
/* 212:    */   public BufferedImage getImage()
/* 213:    */   {
/* 214:226 */     return this.img;
/* 215:    */   }
/* 216:    */   
/* 217:    */   public BufferedImage getImage(Dimension dim)
/* 218:    */   {
/* 219:231 */     double w_old = this.img.getWidth();
/* 220:232 */     double h_old = this.img.getHeight();
/* 221:233 */     BufferedImage scaled = new BufferedImage((int)w_old, (int)h_old, 2);
/* 222:234 */     double w_new = dim.getWidth();
/* 223:235 */     double h_new = dim.getHeight();
/* 224:236 */     AffineTransform at = new AffineTransform();
/* 225:237 */     at.scale(w_new / w_old, h_new / h_old);
/* 226:238 */     AffineTransformOp scaleOp = new AffineTransformOp(at, 2);
/* 227:239 */     scaleOp.filter(this.img, scaled);
/* 228:240 */     return scaled;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public Dimension getDimension()
/* 232:    */   {
/* 233:245 */     return this.img == null ? new Dimension(0, 0) : new Dimension(this.img.getWidth(), this.img.getHeight());
/* 234:    */   }
/* 235:    */   
/* 236:    */   public void setAlpha(double alpha)
/* 237:    */   {
/* 238:252 */     if (this.img == null) {
/* 239:252 */       return;
/* 240:    */     }
/* 241:254 */     Dimension dim = getDimension();
/* 242:255 */     BufferedImage newImg = new BufferedImage((int)dim.getWidth(), (int)dim.getHeight(), 2);
/* 243:256 */     Graphics2D g = newImg.createGraphics();
/* 244:257 */     RescaleOp op = new RescaleOp(new float[] { 1.0F, 1.0F, 1.0F, (float)alpha }, new float[] { 0.0F, 0.0F, 0.0F, 0.0F }, null);
/* 245:258 */     g.drawImage(this.img, op, 0, 0);
/* 246:259 */     g.dispose();
/* 247:    */     
/* 248:261 */     this.img = newImg;
/* 249:    */   }
/* 250:    */   
/* 251:    */   public boolean drawImage(Graphics2D graphics, Rectangle2D anchor)
/* 252:    */   {
/* 253:269 */     return drawImage(graphics, anchor, null);
/* 254:    */   }
/* 255:    */   
/* 256:    */   public boolean drawImage(Graphics2D graphics, Rectangle2D anchor, Insets clip)
/* 257:    */   {
/* 258:277 */     if (this.img == null) {
/* 259:277 */       return false;
/* 260:    */     }
/* 261:279 */     boolean isClipped = true;
/* 262:280 */     if (clip == null)
/* 263:    */     {
/* 264:281 */       isClipped = false;
/* 265:282 */       clip = new Insets(0, 0, 0, 0);
/* 266:    */     }
/* 267:285 */     int iw = this.img.getWidth();
/* 268:286 */     int ih = this.img.getHeight();
/* 269:    */     
/* 270:    */ 
/* 271:289 */     double cw = (100000 - clip.left - clip.right) / 100000.0D;
/* 272:290 */     double ch = (100000 - clip.top - clip.bottom) / 100000.0D;
/* 273:291 */     double sx = anchor.getWidth() / (iw * cw);
/* 274:292 */     double sy = anchor.getHeight() / (ih * ch);
/* 275:293 */     double tx = anchor.getX() - iw * sx * clip.left / 100000.0D;
/* 276:294 */     double ty = anchor.getY() - ih * sy * clip.top / 100000.0D;
/* 277:    */     
/* 278:296 */     AffineTransform at = new AffineTransform(sx, 0.0D, 0.0D, sy, tx, ty);
/* 279:    */     
/* 280:298 */     Shape clipOld = graphics.getClip();
/* 281:299 */     if (isClipped) {
/* 282:299 */       graphics.clip(anchor.getBounds2D());
/* 283:    */     }
/* 284:300 */     graphics.drawRenderedImage(this.img, at);
/* 285:301 */     graphics.setClip(clipOld);
/* 286:    */     
/* 287:303 */     return true;
/* 288:    */   }
/* 289:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.BitmapImageRenderer
 * JD-Core Version:    0.7.0.1
 */