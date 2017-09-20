/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
/*  4:   */ 
/*  5:   */ class XSLFLineBreak
/*  6:   */   extends XSLFTextRun
/*  7:   */ {
/*  8:   */   protected XSLFLineBreak(CTTextLineBreak r, XSLFTextParagraph p)
/*  9:   */   {
/* 10:26 */     super(r, p);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void setText(String text)
/* 14:   */   {
/* 15:30 */     throw new IllegalStateException("You cannot change text of a line break, it is always '\\n'");
/* 16:   */   }
/* 17:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFLineBreak
 * JD-Core Version:    0.7.0.1
 */