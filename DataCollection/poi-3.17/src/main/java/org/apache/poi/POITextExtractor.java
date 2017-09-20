/*  1:   */ package org.apache.poi;
/*  2:   */ 
/*  3:   */ import java.io.Closeable;
/*  4:   */ import java.io.IOException;
/*  5:   */ 
/*  6:   */ public abstract class POITextExtractor
/*  7:   */   implements Closeable
/*  8:   */ {
/*  9:35 */   private Closeable fsToClose = null;
/* 10:   */   
/* 11:   */   public abstract String getText();
/* 12:   */   
/* 13:   */   public abstract POITextExtractor getMetadataTextExtractor();
/* 14:   */   
/* 15:   */   public void setFilesystem(Closeable fs)
/* 16:   */   {
/* 17:61 */     this.fsToClose = fs;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void close()
/* 21:   */     throws IOException
/* 22:   */   {
/* 23:73 */     if (this.fsToClose != null) {
/* 24:74 */       this.fsToClose.close();
/* 25:   */     }
/* 26:   */   }
/* 27:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.POITextExtractor
 * JD-Core Version:    0.7.0.1
 */