/*  1:   */ package org.apache.poi.xslf.model;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.xslf.usermodel.XSLFShape;
/*  5:   */ 
/*  6:   */ @Internal
/*  7:   */ public abstract class PropertyFetcher<T>
/*  8:   */ {
/*  9:   */   private T _value;
/* 10:   */   
/* 11:   */   public abstract boolean fetch(XSLFShape paramXSLFShape);
/* 12:   */   
/* 13:   */   public T getValue()
/* 14:   */   {
/* 15:42 */     return this._value;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void setValue(T val)
/* 19:   */   {
/* 20:46 */     this._value = val;
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.model.PropertyFetcher
 * JD-Core Version:    0.7.0.1
 */