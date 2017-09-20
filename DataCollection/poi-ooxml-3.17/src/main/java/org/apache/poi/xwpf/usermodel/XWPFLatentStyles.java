/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException;
/*  5:   */ 
/*  6:   */ public class XWPFLatentStyles
/*  7:   */ {
/*  8:   */   protected XWPFStyles styles;
/*  9:   */   private CTLatentStyles latentStyles;
/* 10:   */   
/* 11:   */   protected XWPFLatentStyles() {}
/* 12:   */   
/* 13:   */   protected XWPFLatentStyles(CTLatentStyles latentStyles)
/* 14:   */   {
/* 15:31 */     this(latentStyles, null);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected XWPFLatentStyles(CTLatentStyles latentStyles, XWPFStyles styles)
/* 19:   */   {
/* 20:35 */     this.latentStyles = latentStyles;
/* 21:36 */     this.styles = styles;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getNumberOfStyles()
/* 25:   */   {
/* 26:40 */     return this.latentStyles.sizeOfLsdExceptionArray();
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected boolean isLatentStyle(String latentStyleID)
/* 30:   */   {
/* 31:47 */     for (CTLsdException lsd : this.latentStyles.getLsdExceptionArray()) {
/* 32:48 */       if (lsd.getName().equals(latentStyleID)) {
/* 33:49 */         return true;
/* 34:   */       }
/* 35:   */     }
/* 36:52 */     return false;
/* 37:   */   }
/* 38:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFLatentStyles
 * JD-Core Version:    0.7.0.1
 */