/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ public class XWPFHyperlink
/*  4:   */ {
/*  5:   */   String id;
/*  6:   */   String url;
/*  7:   */   
/*  8:   */   public XWPFHyperlink(String id, String url)
/*  9:   */   {
/* 10:28 */     this.id = id;
/* 11:29 */     this.url = url;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String getId()
/* 15:   */   {
/* 16:33 */     return this.id;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String getURL()
/* 20:   */   {
/* 21:37 */     return this.url;
/* 22:   */   }
/* 23:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFHyperlink
 * JD-Core Version:    0.7.0.1
 */