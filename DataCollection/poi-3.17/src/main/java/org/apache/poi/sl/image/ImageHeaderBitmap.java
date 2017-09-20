/*  1:   */ package org.apache.poi.sl.image;
/*  2:   */ 
/*  3:   */ import java.awt.Dimension;
/*  4:   */ import java.awt.image.BufferedImage;
/*  5:   */ import java.io.ByteArrayInputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import javax.imageio.ImageIO;
/*  8:   */ import org.apache.poi.util.Internal;
/*  9:   */ import org.apache.poi.util.POILogFactory;
/* 10:   */ import org.apache.poi.util.POILogger;
/* 11:   */ import org.apache.poi.util.Units;
/* 12:   */ 
/* 13:   */ @Internal
/* 14:   */ public class ImageHeaderBitmap
/* 15:   */ {
/* 16:34 */   private static final POILogger LOG = POILogFactory.getLogger(ImageHeaderBitmap.class);
/* 17:   */   private final Dimension size;
/* 18:   */   
/* 19:   */   public ImageHeaderBitmap(byte[] data, int offset)
/* 20:   */   {
/* 21:39 */     BufferedImage img = null;
/* 22:   */     try
/* 23:   */     {
/* 24:41 */       img = ImageIO.read(new ByteArrayInputStream(data, offset, data.length - offset));
/* 25:   */     }
/* 26:   */     catch (IOException e)
/* 27:   */     {
/* 28:43 */       LOG.log(5, new Object[] { "Can't determine image dimensions", e });
/* 29:   */     }
/* 30:46 */     this.size = (img == null ? new Dimension(200, 200) : new Dimension((int)Units.pixelToPoints(img.getWidth()), (int)Units.pixelToPoints(img.getHeight())));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Dimension getSize()
/* 34:   */   {
/* 35:55 */     return this.size;
/* 36:   */   }
/* 37:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.image.ImageHeaderBitmap
 * JD-Core Version:    0.7.0.1
 */