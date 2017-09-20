/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*  5:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
/*  6:   */ 
/*  7:   */ public class XWPFDefaultParagraphStyle
/*  8:   */ {
/*  9:   */   private CTPPr ppr;
/* 10:   */   
/* 11:   */   public XWPFDefaultParagraphStyle(CTPPr ppr)
/* 12:   */   {
/* 13:30 */     this.ppr = ppr;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected CTPPr getPPr()
/* 17:   */   {
/* 18:34 */     return this.ppr;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getSpacingAfter()
/* 22:   */   {
/* 23:38 */     if (this.ppr.isSetSpacing()) {
/* 24:39 */       return this.ppr.getSpacing().getAfter().intValue();
/* 25:   */     }
/* 26:40 */     return -1;
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFDefaultParagraphStyle
 * JD-Core Version:    0.7.0.1
 */