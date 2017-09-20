/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.Footer;
/*  4:   */ import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*  6:   */ 
/*  7:   */ public class XSSFOddFooter
/*  8:   */   extends XSSFHeaderFooter
/*  9:   */   implements Footer
/* 10:   */ {
/* 11:   */   public XSSFOddFooter(CTHeaderFooter headerFooter)
/* 12:   */   {
/* 13:38 */     super(headerFooter);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getText()
/* 17:   */   {
/* 18:46 */     return getHeaderFooter().getOddFooter();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setText(String text)
/* 22:   */   {
/* 23:55 */     if (text == null) {
/* 24:56 */       getHeaderFooter().unsetOddFooter();
/* 25:   */     } else {
/* 26:58 */       getHeaderFooter().setOddFooter(text);
/* 27:   */     }
/* 28:   */   }
/* 29:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFOddFooter
 * JD-Core Version:    0.7.0.1
 */