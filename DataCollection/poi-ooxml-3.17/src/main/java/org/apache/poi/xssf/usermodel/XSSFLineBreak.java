/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  5:   */ 
/*  6:   */ class XSSFLineBreak
/*  7:   */   extends XSSFTextRun
/*  8:   */ {
/*  9:   */   private final CTTextCharacterProperties _brProps;
/* 10:   */   
/* 11:   */   XSSFLineBreak(CTRegularTextRun r, XSSFTextParagraph p, CTTextCharacterProperties brProps)
/* 12:   */   {
/* 13:29 */     super(r, p);
/* 14:30 */     this._brProps = brProps;
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected CTTextCharacterProperties getRPr()
/* 18:   */   {
/* 19:35 */     return this._brProps;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setText(String text)
/* 23:   */   {
/* 24:42 */     throw new IllegalStateException("You cannot change text of a line break, it is always '\\n'");
/* 25:   */   }
/* 26:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFLineBreak
 * JD-Core Version:    0.7.0.1
 */