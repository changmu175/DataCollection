/*  1:   */ package org.apache.poi.openxml4j.util;
/*  2:   */ 
/*  3:   */ public final class Nullable<E>
/*  4:   */ {
/*  5:   */   private E value;
/*  6:   */   
/*  7:   */   public Nullable() {}
/*  8:   */   
/*  9:   */   public Nullable(E value)
/* 10:   */   {
/* 11:44 */     this.value = value;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public E getValue()
/* 15:   */   {
/* 16:53 */     return this.value;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean hasValue()
/* 20:   */   {
/* 21:63 */     return this.value != null;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void nullify()
/* 25:   */   {
/* 26:70 */     this.value = null;
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.util.Nullable
 * JD-Core Version:    0.7.0.1
 */