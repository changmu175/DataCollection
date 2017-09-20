/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public abstract class VariantTypeException
/*  4:   */   extends HPSFException
/*  5:   */ {
/*  6:   */   private Object value;
/*  7:   */   private long variantType;
/*  8:   */   
/*  9:   */   public VariantTypeException(long variantType, Object value, String msg)
/* 10:   */   {
/* 11:43 */     super(msg);
/* 12:44 */     this.variantType = variantType;
/* 13:45 */     this.value = value;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public long getVariantType()
/* 17:   */   {
/* 18:57 */     return this.variantType;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object getValue()
/* 22:   */   {
/* 23:69 */     return this.value;
/* 24:   */   }
/* 25:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.VariantTypeException
 * JD-Core Version:    0.7.0.1
 */