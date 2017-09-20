/*   1:    */ package org.apache.poi.sl.image;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Rectangle;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ 
/*   7:    */ @Internal
/*   8:    */ public class ImageHeaderPICT
/*   9:    */ {
/*  10:    */   public static final int PICT_HEADER_OFFSET = 512;
/*  11:    */   public static final double DEFAULT_RESOLUTION = 72.0D;
/*  12: 35 */   private static final byte[] V2_HEADER = { 0, 17, 2, -1, 12, 0, -1, -2, 0, 0 };
/*  13:    */   private final Rectangle bounds;
/*  14:    */   private final double hRes;
/*  15:    */   private final double vRes;
/*  16:    */   
/*  17:    */   public ImageHeaderPICT(byte[] data, int off)
/*  18:    */   {
/*  19: 47 */     int offset = off;
/*  20:    */     
/*  21: 49 */     offset += 2;
/*  22:    */     
/*  23:    */ 
/*  24: 52 */     int y1 = readUnsignedShort(data, offset);offset += 2;
/*  25: 53 */     int x1 = readUnsignedShort(data, offset);offset += 2;
/*  26: 54 */     int y2 = readUnsignedShort(data, offset);offset += 2;
/*  27: 55 */     int x2 = readUnsignedShort(data, offset);offset += 2;
/*  28:    */     
/*  29:    */ 
/*  30: 58 */     boolean isV2 = true;
/*  31: 59 */     for (byte b : V2_HEADER) {
/*  32: 60 */       if (b != data[(offset++)])
/*  33:    */       {
/*  34: 61 */         isV2 = false;
/*  35: 62 */         break;
/*  36:    */       }
/*  37:    */     }
/*  38: 66 */     if (isV2)
/*  39:    */     {
/*  40: 68 */       this.hRes = readFixedPoint(data, offset);offset += 4;
/*  41:    */       
/*  42: 70 */       this.vRes = readFixedPoint(data, offset);offset += 4;
/*  43:    */     }
/*  44:    */     else
/*  45:    */     {
/*  46: 72 */       this.hRes = 72.0D;
/*  47: 73 */       this.vRes = 72.0D;
/*  48:    */     }
/*  49: 76 */     this.bounds = new Rectangle(x1, y1, x2 - x1, y2 - y1);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Dimension getSize()
/*  53:    */   {
/*  54: 80 */     int height = (int)Math.round(this.bounds.height * 72.0D / this.vRes);
/*  55: 81 */     int width = (int)Math.round(this.bounds.width * 72.0D / this.hRes);
/*  56: 82 */     return new Dimension(width, height);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Rectangle getBounds()
/*  60:    */   {
/*  61: 86 */     return this.bounds;
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static int readUnsignedShort(byte[] data, int offset)
/*  65:    */   {
/*  66: 90 */     int b0 = data[offset] & 0xFF;
/*  67: 91 */     int b1 = data[(offset + 1)] & 0xFF;
/*  68: 92 */     return b0 << 8 | b1;
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static double readFixedPoint(byte[] data, int offset)
/*  72:    */   {
/*  73: 96 */     int b0 = data[offset] & 0xFF;
/*  74: 97 */     int b1 = data[(offset + 1)] & 0xFF;
/*  75: 98 */     int b2 = data[(offset + 2)] & 0xFF;
/*  76: 99 */     int b3 = data[(offset + 3)] & 0xFF;
/*  77:100 */     int i = b0 << 24 | b1 << 16 | b2 << 8 | b3;
/*  78:101 */     return i / 65536.0D;
/*  79:    */   }
/*  80:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.image.ImageHeaderPICT
 * JD-Core Version:    0.7.0.1
 */