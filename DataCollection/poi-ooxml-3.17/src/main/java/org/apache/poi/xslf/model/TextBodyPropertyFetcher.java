/*  1:   */ package org.apache.poi.xslf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.xslf.usermodel.XSLFShape;
/*  4:   */ import org.apache.xmlbeans.XmlObject;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
/*  6:   */ 
/*  7:   */ public abstract class TextBodyPropertyFetcher<T>
/*  8:   */   extends PropertyFetcher<T>
/*  9:   */ {
/* 10:   */   public boolean fetch(XSLFShape shape)
/* 11:   */   {
/* 12:37 */     XmlObject[] o = shape.getXmlObject().selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//p:txBody/a:bodyPr");
/* 13:42 */     if (o.length == 1)
/* 14:   */     {
/* 15:43 */       CTTextBodyProperties props = (CTTextBodyProperties)o[0];
/* 16:44 */       return fetch(props);
/* 17:   */     }
/* 18:47 */     return false;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public abstract boolean fetch(CTTextBodyProperties paramCTTextBodyProperties);
/* 22:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.model.TextBodyPropertyFetcher
 * JD-Core Version:    0.7.0.1
 */