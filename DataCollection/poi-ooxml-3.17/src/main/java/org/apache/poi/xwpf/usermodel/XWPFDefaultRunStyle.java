/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
/*  5:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*  6:   */ 
/*  7:   */ public class XWPFDefaultRunStyle
/*  8:   */ {
/*  9:   */   private CTRPr rpr;
/* 10:   */   
/* 11:   */   public XWPFDefaultRunStyle(CTRPr rpr)
/* 12:   */   {
/* 13:30 */     this.rpr = rpr;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected CTRPr getRPr()
/* 17:   */   {
/* 18:34 */     return this.rpr;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getFontSize()
/* 22:   */   {
/* 23:38 */     if (this.rpr.isSetSz()) {
/* 24:39 */       return this.rpr.getSz().getVal().intValue() / 2;
/* 25:   */     }
/* 26:40 */     return -1;
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFDefaultRunStyle
 * JD-Core Version:    0.7.0.1
 */