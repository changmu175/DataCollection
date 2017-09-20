/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ 
/*  6:   */ public class XDGFStyleSheet
/*  7:   */   extends XDGFSheet
/*  8:   */ {
/*  9:   */   public XDGFStyleSheet(StyleSheetType styleSheet, XDGFDocument document)
/* 10:   */   {
/* 11:27 */     super(styleSheet, document);
/* 12:   */   }
/* 13:   */   
/* 14:   */   @Internal
/* 15:   */   public StyleSheetType getXmlObject()
/* 16:   */   {
/* 17:33 */     return (StyleSheetType)this._sheet;
/* 18:   */   }
/* 19:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFStyleSheet
 * JD-Core Version:    0.7.0.1
 */