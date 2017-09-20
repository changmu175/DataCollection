/*  1:   */ package org.apache.poi.xslf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.xslf.usermodel.XSLFShape;
/*  4:   */ import org.apache.xmlbeans.XmlObject;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  6:   */ 
/*  7:   */ public abstract class ParagraphPropertyFetcher<T>
/*  8:   */   extends PropertyFetcher<T>
/*  9:   */ {
/* 10:   */   int _level;
/* 11:   */   
/* 12:   */   public ParagraphPropertyFetcher(int level)
/* 13:   */   {
/* 14:34 */     this._level = level;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public boolean fetch(XSLFShape shape)
/* 18:   */   {
/* 19:39 */     XmlObject[] o = shape.getXmlObject().selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//p:txBody/a:lstStyle/a:lvl" + (this._level + 1) + "pPr");
/* 20:44 */     if (o.length == 1)
/* 21:   */     {
/* 22:45 */       CTTextParagraphProperties props = (CTTextParagraphProperties)o[0];
/* 23:46 */       return fetch(props);
/* 24:   */     }
/* 25:48 */     return false;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public abstract boolean fetch(CTTextParagraphProperties paramCTTextParagraphProperties);
/* 29:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.model.ParagraphPropertyFetcher
 * JD-Core Version:    0.7.0.1
 */