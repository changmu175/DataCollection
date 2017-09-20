/*  1:   */ package org.apache.poi.hssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.HeaderRecord;
/*  4:   */ import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
/*  5:   */ import org.apache.poi.ss.usermodel.Header;
/*  6:   */ 
/*  7:   */ public final class HSSFHeader
/*  8:   */   extends HeaderFooter
/*  9:   */   implements Header
/* 10:   */ {
/* 11:   */   private final PageSettingsBlock _psb;
/* 12:   */   
/* 13:   */   protected HSSFHeader(PageSettingsBlock psb)
/* 14:   */   {
/* 15:39 */     this._psb = psb;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected String getRawText()
/* 19:   */   {
/* 20:43 */     HeaderRecord hf = this._psb.getHeader();
/* 21:44 */     if (hf == null) {
/* 22:45 */       return "";
/* 23:   */     }
/* 24:47 */     return hf.getText();
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected void setHeaderFooterText(String text)
/* 28:   */   {
/* 29:52 */     HeaderRecord hfr = this._psb.getHeader();
/* 30:53 */     if (hfr == null)
/* 31:   */     {
/* 32:54 */       hfr = new HeaderRecord(text);
/* 33:55 */       this._psb.setHeader(hfr);
/* 34:   */     }
/* 35:   */     else
/* 36:   */     {
/* 37:57 */       hfr.setText(text);
/* 38:   */     }
/* 39:   */   }
/* 40:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFHeader
 * JD-Core Version:    0.7.0.1
 */