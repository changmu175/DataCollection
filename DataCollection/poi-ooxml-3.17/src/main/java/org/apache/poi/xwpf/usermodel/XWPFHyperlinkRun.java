/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
/*  5:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*  6:   */ 
/*  7:   */ public class XWPFHyperlinkRun
/*  8:   */   extends XWPFRun
/*  9:   */ {
/* 10:   */   private CTHyperlink hyperlink;
/* 11:   */   
/* 12:   */   public XWPFHyperlinkRun(CTHyperlink hyperlink, CTR run, IRunBody p)
/* 13:   */   {
/* 14:31 */     super(run, p);
/* 15:32 */     this.hyperlink = hyperlink;
/* 16:   */   }
/* 17:   */   
/* 18:   */   @Internal
/* 19:   */   public CTHyperlink getCTHyperlink()
/* 20:   */   {
/* 21:37 */     return this.hyperlink;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String getAnchor()
/* 25:   */   {
/* 26:41 */     return this.hyperlink.getAnchor();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getHyperlinkId()
/* 30:   */   {
/* 31:48 */     return this.hyperlink.getId();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setHyperlinkId(String id)
/* 35:   */   {
/* 36:52 */     this.hyperlink.setId(id);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public XWPFHyperlink getHyperlink(XWPFDocument document)
/* 40:   */   {
/* 41:60 */     String id = getHyperlinkId();
/* 42:61 */     if (id == null) {
/* 43:62 */       return null;
/* 44:   */     }
/* 45:64 */     return document.getHyperlinkByID(id);
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun
 * JD-Core Version:    0.7.0.1
 */