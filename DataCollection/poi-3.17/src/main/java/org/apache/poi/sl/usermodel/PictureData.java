/*  1:   */ package org.apache.poi.sl.usermodel;
/*  2:   */ 
/*  3:   */ import java.awt.Dimension;
/*  4:   */ import java.io.IOException;
/*  5:   */ 
/*  6:   */ public abstract interface PictureData
/*  7:   */ {
/*  8:   */   public abstract String getContentType();
/*  9:   */   
/* 10:   */   public abstract PictureType getType();
/* 11:   */   
/* 12:   */   public abstract byte[] getData();
/* 13:   */   
/* 14:   */   public abstract void setData(byte[] paramArrayOfByte)
/* 15:   */     throws IOException;
/* 16:   */   
/* 17:   */   public abstract byte[] getChecksum();
/* 18:   */   
/* 19:   */   public abstract Dimension getImageDimension();
/* 20:   */   
/* 21:   */   public abstract Dimension getImageDimensionInPixels();
/* 22:   */   
/* 23:   */   public static enum PictureType
/* 24:   */   {
/* 25:27 */     EMF(2, 2, "image/x-emf", ".emf"),  WMF(3, 3, "image/x-wmf", ".wmf"),  PICT(4, 4, "image/pict", ".pict"),  JPEG(5, 5, "image/jpeg", ".jpg"),  PNG(6, 6, "image/png", ".png"),  DIB(7, 7, "image/dib", ".dib"),  GIF(-1, 8, "image/gif", ".gif"),  TIFF(-1, 9, "image/tiff", ".tif"),  EPS(-1, 10, "image/x-eps", ".eps"),  BMP(-1, 11, "image/x-ms-bmp", ".bmp"),  WPG(-1, 12, "image/x-wpg", ".wpg"),  WDP(-1, 13, "image/vnd.ms-photo", ".wdp");
/* 26:   */     
/* 27:   */     public final int nativeId;
/* 28:   */     public final int ooxmlId;
/* 29:   */     public final String contentType;
/* 30:   */     public final String extension;
/* 31:   */     
/* 32:   */     private PictureType(int nativeId, int ooxmlId, String contentType, String extension)
/* 33:   */     {
/* 34:55 */       this.nativeId = nativeId;
/* 35:56 */       this.ooxmlId = ooxmlId;
/* 36:57 */       this.contentType = contentType;
/* 37:58 */       this.extension = extension;
/* 38:   */     }
/* 39:   */     
/* 40:   */     public static PictureType forNativeID(int nativeId)
/* 41:   */     {
/* 42:62 */       for (PictureType ans : ) {
/* 43:63 */         if (ans.nativeId == nativeId) {
/* 44:63 */           return ans;
/* 45:   */         }
/* 46:   */       }
/* 47:65 */       return null;
/* 48:   */     }
/* 49:   */     
/* 50:   */     public static PictureType forOoxmlID(int ooxmlId)
/* 51:   */     {
/* 52:69 */       for (PictureType ans : ) {
/* 53:70 */         if (ans.ooxmlId == ooxmlId) {
/* 54:70 */           return ans;
/* 55:   */         }
/* 56:   */       }
/* 57:72 */       return null;
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.PictureData
 * JD-Core Version:    0.7.0.1
 */