/*  1:   */ package org.apache.poi.xwpf.model;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ import org.apache.poi.xwpf.usermodel.XWPFComment;
/*  5:   */ import org.apache.poi.xwpf.usermodel.XWPFDocument;
/*  6:   */ import org.apache.poi.xwpf.usermodel.XWPFParagraph;
/*  7:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange;
/*  8:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  9:   */ 
/* 10:   */ public class XWPFCommentsDecorator
/* 11:   */   extends XWPFParagraphDecorator
/* 12:   */ {
/* 13:   */   private StringBuffer commentText;
/* 14:   */   
/* 15:   */   public XWPFCommentsDecorator(XWPFParagraphDecorator nextDecorator)
/* 16:   */   {
/* 17:33 */     this(nextDecorator.paragraph, nextDecorator);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public XWPFCommentsDecorator(XWPFParagraph paragraph, XWPFParagraphDecorator nextDecorator)
/* 21:   */   {
/* 22:37 */     super(paragraph, nextDecorator);
/* 23:   */     
/* 24:   */ 
/* 25:40 */     this.commentText = new StringBuffer();
/* 26:42 */     for (CTMarkupRange anchor : paragraph.getCTP().getCommentRangeStartArray())
/* 27:   */     {
/* 28:   */       XWPFComment comment;
/* 29:43 */       if ((comment = paragraph.getDocument().getCommentByID(anchor.getId().toString())) != null) {
/* 30:44 */         this.commentText.append("\tComment by " + comment.getAuthor() + ": " + comment.getText());
/* 31:   */       }
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getCommentText()
/* 36:   */   {
/* 37:49 */     return this.commentText.toString();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String getText()
/* 41:   */   {
/* 42:53 */     return super.getText() + this.commentText;
/* 43:   */   }
/* 44:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.model.XWPFCommentsDecorator
 * JD-Core Version:    0.7.0.1
 */