/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum;
/*  4:   */ 
/*  5:   */ public class XWPFNum
/*  6:   */ {
/*  7:   */   protected XWPFNumbering numbering;
/*  8:   */   private CTNum ctNum;
/*  9:   */   
/* 10:   */   public XWPFNum()
/* 11:   */   {
/* 12:30 */     this.ctNum = null;
/* 13:31 */     this.numbering = null;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public XWPFNum(CTNum ctNum)
/* 17:   */   {
/* 18:35 */     this.ctNum = ctNum;
/* 19:36 */     this.numbering = null;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public XWPFNum(XWPFNumbering numbering)
/* 23:   */   {
/* 24:40 */     this.ctNum = null;
/* 25:41 */     this.numbering = numbering;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public XWPFNum(CTNum ctNum, XWPFNumbering numbering)
/* 29:   */   {
/* 30:45 */     this.ctNum = ctNum;
/* 31:46 */     this.numbering = numbering;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public XWPFNumbering getNumbering()
/* 35:   */   {
/* 36:50 */     return this.numbering;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void setNumbering(XWPFNumbering numbering)
/* 40:   */   {
/* 41:54 */     this.numbering = numbering;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public CTNum getCTNum()
/* 45:   */   {
/* 46:58 */     return this.ctNum;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void setCTNum(CTNum ctNum)
/* 50:   */   {
/* 51:62 */     this.ctNum = ctNum;
/* 52:   */   }
/* 53:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFNum
 * JD-Core Version:    0.7.0.1
 */