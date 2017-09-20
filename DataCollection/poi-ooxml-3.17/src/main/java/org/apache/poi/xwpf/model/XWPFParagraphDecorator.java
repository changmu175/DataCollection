/*  1:   */ package org.apache.poi.xwpf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.xwpf.usermodel.XWPFParagraph;
/*  4:   */ 
/*  5:   */ public abstract class XWPFParagraphDecorator
/*  6:   */ {
/*  7:   */   protected XWPFParagraph paragraph;
/*  8:   */   protected XWPFParagraphDecorator nextDecorator;
/*  9:   */   
/* 10:   */   public XWPFParagraphDecorator(XWPFParagraph paragraph)
/* 11:   */   {
/* 12:29 */     this(paragraph, null);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public XWPFParagraphDecorator(XWPFParagraph paragraph, XWPFParagraphDecorator nextDecorator)
/* 16:   */   {
/* 17:33 */     this.paragraph = paragraph;
/* 18:34 */     this.nextDecorator = nextDecorator;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getText()
/* 22:   */   {
/* 23:38 */     if (this.nextDecorator != null) {
/* 24:39 */       return this.nextDecorator.getText();
/* 25:   */     }
/* 26:41 */     return this.paragraph.getText();
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.model.XWPFParagraphDecorator
 * JD-Core Version:    0.7.0.1
 */