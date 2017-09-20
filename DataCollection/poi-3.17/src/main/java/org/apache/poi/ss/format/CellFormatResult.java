/*  1:   */ package org.apache.poi.ss.format;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ 
/*  5:   */ public class CellFormatResult
/*  6:   */ {
/*  7:   */   public final boolean applies;
/*  8:   */   public final String text;
/*  9:   */   public final Color textColor;
/* 10:   */   
/* 11:   */   public CellFormatResult(boolean applies, String text, Color textColor)
/* 12:   */     throws IllegalArgumentException
/* 13:   */   {
/* 14:54 */     this.applies = applies;
/* 15:55 */     if (text == null) {
/* 16:56 */       throw new IllegalArgumentException("CellFormatResult text may not be null");
/* 17:   */     }
/* 18:57 */     this.text = text;
/* 19:58 */     this.textColor = (applies ? textColor : null);
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellFormatResult
 * JD-Core Version:    0.7.0.1
 */