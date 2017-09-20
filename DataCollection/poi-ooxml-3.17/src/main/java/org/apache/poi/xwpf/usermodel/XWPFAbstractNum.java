/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
/*  4:   */ 
/*  5:   */ public class XWPFAbstractNum
/*  6:   */ {
/*  7:   */   protected XWPFNumbering numbering;
/*  8:   */   private CTAbstractNum ctAbstractNum;
/*  9:   */   
/* 10:   */   protected XWPFAbstractNum()
/* 11:   */   {
/* 12:30 */     this.ctAbstractNum = null;
/* 13:31 */     this.numbering = null;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public XWPFAbstractNum(CTAbstractNum abstractNum)
/* 17:   */   {
/* 18:36 */     this.ctAbstractNum = abstractNum;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public XWPFAbstractNum(CTAbstractNum ctAbstractNum, XWPFNumbering numbering)
/* 22:   */   {
/* 23:40 */     this.ctAbstractNum = ctAbstractNum;
/* 24:41 */     this.numbering = numbering;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public CTAbstractNum getAbstractNum()
/* 28:   */   {
/* 29:45 */     return this.ctAbstractNum;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public XWPFNumbering getNumbering()
/* 33:   */   {
/* 34:49 */     return this.numbering;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void setNumbering(XWPFNumbering numbering)
/* 38:   */   {
/* 39:53 */     this.numbering = numbering;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public CTAbstractNum getCTAbstractNum()
/* 43:   */   {
/* 44:57 */     return this.ctAbstractNum;
/* 45:   */   }
/* 46:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFAbstractNum
 * JD-Core Version:    0.7.0.1
 */