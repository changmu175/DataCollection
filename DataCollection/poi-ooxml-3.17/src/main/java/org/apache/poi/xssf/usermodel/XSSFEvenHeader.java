/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Header;
/*  4:   */ import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*  6:   */ 
/*  7:   */ public class XSSFEvenHeader
/*  8:   */   extends XSSFHeaderFooter
/*  9:   */   implements Header
/* 10:   */ {
/* 11:   */   public XSSFEvenHeader(CTHeaderFooter headerFooter)
/* 12:   */   {
/* 13:41 */     super(headerFooter);
/* 14:42 */     headerFooter.setDifferentOddEven(true);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getText()
/* 18:   */   {
/* 19:50 */     return getHeaderFooter().getEvenHeader();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setText(String text)
/* 23:   */   {
/* 24:59 */     if (text == null) {
/* 25:60 */       getHeaderFooter().unsetEvenHeader();
/* 26:   */     } else {
/* 27:62 */       getHeaderFooter().setEvenHeader(text);
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFEvenHeader
 * JD-Core Version:    0.7.0.1
 */