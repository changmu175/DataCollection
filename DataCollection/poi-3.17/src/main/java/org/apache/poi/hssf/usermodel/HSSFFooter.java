/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.FooterRecord;
/*  4:   */ import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
/*  5:   */ import org.apache.poi.ss.usermodel.Footer;
/*  6:   */ 
/*  7:   */ public final class HSSFFooter
/*  8:   */   extends HeaderFooter
/*  9:   */   implements Footer
/* 10:   */ {
/* 11:   */   private final PageSettingsBlock _psb;
/* 12:   */   
/* 13:   */   protected HSSFFooter(PageSettingsBlock psb)
/* 14:   */   {
/* 15:38 */     this._psb = psb;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected String getRawText()
/* 19:   */   {
/* 20:42 */     FooterRecord hf = this._psb.getFooter();
/* 21:43 */     if (hf == null) {
/* 22:44 */       return "";
/* 23:   */     }
/* 24:46 */     return hf.getText();
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected void setHeaderFooterText(String text)
/* 28:   */   {
/* 29:51 */     FooterRecord hfr = this._psb.getFooter();
/* 30:52 */     if (hfr == null)
/* 31:   */     {
/* 32:53 */       hfr = new FooterRecord(text);
/* 33:54 */       this._psb.setFooter(hfr);
/* 34:   */     }
/* 35:   */     else
/* 36:   */     {
/* 37:56 */       hfr.setText(text);
/* 38:   */     }
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFFooter
 * JD-Core Version:    0.7.0.1
 */