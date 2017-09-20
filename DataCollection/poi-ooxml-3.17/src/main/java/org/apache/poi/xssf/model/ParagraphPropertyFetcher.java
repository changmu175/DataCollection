/*  1:   */ package org.apache.poi.xssf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.xmlbeans.XmlObject;
/*  5:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
/*  7:   */ 
/*  8:   */ @Internal
/*  9:   */ public abstract class ParagraphPropertyFetcher<T>
/* 10:   */ {
/* 11:   */   private T _value;
/* 12:   */   private int _level;
/* 13:   */   
/* 14:   */   public T getValue()
/* 15:   */   {
/* 16:37 */     return this._value;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setValue(T val)
/* 20:   */   {
/* 21:41 */     this._value = val;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ParagraphPropertyFetcher(int level)
/* 25:   */   {
/* 26:45 */     this._level = level;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean fetch(CTShape shape)
/* 30:   */   {
/* 31:55 */     XmlObject[] o = shape.selectPath("declare namespace xdr='http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//xdr:txBody/a:lstStyle/a:lvl" + (this._level + 1) + "pPr");
/* 32:60 */     if (o.length == 1)
/* 33:   */     {
/* 34:61 */       CTTextParagraphProperties props = (CTTextParagraphProperties)o[0];
/* 35:62 */       return fetch(props);
/* 36:   */     }
/* 37:64 */     return false;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public abstract boolean fetch(CTTextParagraphProperties paramCTTextParagraphProperties);
/* 41:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.ParagraphPropertyFetcher
 * JD-Core Version:    0.7.0.1
 */