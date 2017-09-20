/*  1:   */ package org.apache.poi.xwpf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  5:   */ 
/*  6:   */ @Deprecated
/*  7:   */ @Removal(version="3.18")
/*  8:   */ public class XMLParagraph
/*  9:   */ {
/* 10:   */   protected CTP paragraph;
/* 11:   */   
/* 12:   */   public XMLParagraph(CTP paragraph)
/* 13:   */   {
/* 14:33 */     this.paragraph = paragraph;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public CTP getCTP()
/* 18:   */   {
/* 19:37 */     return this.paragraph;
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.model.XMLParagraph
 * JD-Core Version:    0.7.0.1
 */