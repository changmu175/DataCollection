/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  6:   */ 
/*  7:   */ @Removal(version="3.18")
/*  8:   */ public class DrawingTextBody
/*  9:   */ {
/* 10:   */   private final CTTextBody textBody;
/* 11:   */   
/* 12:   */   public DrawingTextBody(CTTextBody textBody)
/* 13:   */   {
/* 14:32 */     this.textBody = textBody;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DrawingParagraph[] getParagraphs()
/* 18:   */   {
/* 19:36 */     CTTextParagraph[] paragraphs = this.textBody.getPArray();
/* 20:37 */     DrawingParagraph[] o = new DrawingParagraph[paragraphs.length];
/* 21:39 */     for (int i = 0; i < o.length; i++) {
/* 22:40 */       o[i] = new DrawingParagraph(paragraphs[i]);
/* 23:   */     }
/* 24:43 */     return o;
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingTextBody
 * JD-Core Version:    0.7.0.1
 */