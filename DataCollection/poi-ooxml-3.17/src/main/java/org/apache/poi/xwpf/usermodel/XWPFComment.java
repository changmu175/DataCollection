/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.math.BigInteger;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment;
/*  5:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  6:   */ 
/*  7:   */ public class XWPFComment
/*  8:   */ {
/*  9:   */   protected String id;
/* 10:   */   protected String author;
/* 11:   */   protected StringBuffer text;
/* 12:   */   
/* 13:   */   public XWPFComment(CTComment comment, XWPFDocument document)
/* 14:   */   {
/* 15:33 */     this.text = new StringBuffer();
/* 16:34 */     this.id = comment.getId().toString();
/* 17:35 */     this.author = comment.getAuthor();
/* 18:37 */     for (CTP ctp : comment.getPArray())
/* 19:   */     {
/* 20:38 */       XWPFParagraph p = new XWPFParagraph(ctp, document);
/* 21:39 */       this.text.append(p.getText());
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getId()
/* 26:   */   {
/* 27:44 */     return this.id;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getAuthor()
/* 31:   */   {
/* 32:48 */     return this.author;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getText()
/* 36:   */   {
/* 37:52 */     return this.text.toString();
/* 38:   */   }
/* 39:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFComment
 * JD-Core Version:    0.7.0.1
 */