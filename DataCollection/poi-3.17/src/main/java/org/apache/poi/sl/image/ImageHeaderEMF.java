/*  1:   */ package org.apache.poi.sl.image;
/*  2:   */ 
/*  3:   */ import java.awt.Dimension;
/*  4:   */ import java.awt.Rectangle;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ import org.apache.poi.util.LittleEndian;
/*  7:   */ import org.apache.poi.util.LocaleUtil;
/*  8:   */ import org.apache.poi.util.POILogFactory;
/*  9:   */ import org.apache.poi.util.POILogger;
/* 10:   */ 
/* 11:   */ @Internal
/* 12:   */ public class ImageHeaderEMF
/* 13:   */ {
/* 14:31 */   private static final POILogger LOG = POILogFactory.getLogger(ImageHeaderEMF.class);
/* 15:   */   private static final String EMF_SIGNATURE = " EMF";
/* 16:   */   private final Rectangle deviceBounds;
/* 17:   */   
/* 18:   */   public ImageHeaderEMF(byte[] data, int off)
/* 19:   */   {
/* 20:40 */     int offset = off;
/* 21:41 */     int type = (int)LittleEndian.getUInt(data, offset);offset += 4;
/* 22:42 */     if (type != 1)
/* 23:   */     {
/* 24:43 */       LOG.log(5, new Object[] { "Invalid EMF picture - invalid type" });
/* 25:44 */       this.deviceBounds = new Rectangle(0, 0, 200, 200);
/* 26:45 */       return;
/* 27:   */     }
/* 28:48 */     offset += 4;
/* 29:49 */     int left = LittleEndian.getInt(data, offset);offset += 4;
/* 30:50 */     int top = LittleEndian.getInt(data, offset);offset += 4;
/* 31:51 */     int right = LittleEndian.getInt(data, offset);offset += 4;
/* 32:52 */     int bottom = LittleEndian.getInt(data, offset);offset += 4;
/* 33:53 */     this.deviceBounds = new Rectangle(left, top, right - left, bottom - top);
/* 34:   */     
/* 35:55 */     offset += 16;
/* 36:56 */     String signature = new String(data, offset, " EMF".length(), LocaleUtil.CHARSET_1252);
/* 37:57 */     if (!" EMF".equals(signature)) {
/* 38:58 */       LOG.log(5, new Object[] { "Invalid EMF picture - invalid signature" });
/* 39:   */     }
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Dimension getSize()
/* 43:   */   {
/* 44:63 */     return this.deviceBounds.getSize();
/* 45:   */   }
/* 46:   */   
/* 47:   */   public Rectangle getBounds()
/* 48:   */   {
/* 49:67 */     return this.deviceBounds;
/* 50:   */   }
/* 51:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.image.ImageHeaderEMF
 * JD-Core Version:    0.7.0.1
 */