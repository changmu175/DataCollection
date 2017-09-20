/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Footer;
/*  4:   */ import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*  6:   */ 
/*  7:   */ public class XSSFEvenFooter
/*  8:   */   extends XSSFHeaderFooter
/*  9:   */   implements Footer
/* 10:   */ {
/* 11:   */   public XSSFEvenFooter(CTHeaderFooter headerFooter)
/* 12:   */   {
/* 13:40 */     super(headerFooter);
/* 14:41 */     headerFooter.setDifferentOddEven(true);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getText()
/* 18:   */   {
/* 19:49 */     return getHeaderFooter().getEvenFooter();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setText(String text)
/* 23:   */   {
/* 24:58 */     if (text == null) {
/* 25:59 */       getHeaderFooter().unsetEvenFooter();
/* 26:   */     } else {
/* 27:61 */       getHeaderFooter().setEvenFooter(text);
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFEvenFooter
 * JD-Core Version:    0.7.0.1
 */