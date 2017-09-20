/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;
/*  4:   */ 
/*  5:   */ public class XDGFPageSheet
/*  6:   */   extends XDGFSheet
/*  7:   */ {
/*  8:   */   PageSheetType _pageSheet;
/*  9:   */   
/* 10:   */   public XDGFPageSheet(PageSheetType sheet, XDGFDocument document)
/* 11:   */   {
/* 12:27 */     super(sheet, document);
/* 13:28 */     this._pageSheet = sheet;
/* 14:   */   }
/* 15:   */   
/* 16:   */   PageSheetType getXmlObject()
/* 17:   */   {
/* 18:33 */     return this._pageSheet;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFPageSheet
 * JD-Core Version:    0.7.0.1
 */