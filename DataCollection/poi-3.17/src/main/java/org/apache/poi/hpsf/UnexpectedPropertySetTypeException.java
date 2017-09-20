/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ public class UnexpectedPropertySetTypeException
/*  4:   */   extends HPSFException
/*  5:   */ {
/*  6:   */   public UnexpectedPropertySetTypeException() {}
/*  7:   */   
/*  8:   */   public UnexpectedPropertySetTypeException(String msg)
/*  9:   */   {
/* 10:48 */     super(msg);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public UnexpectedPropertySetTypeException(Throwable reason)
/* 14:   */   {
/* 15:61 */     super(reason);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public UnexpectedPropertySetTypeException(String msg, Throwable reason)
/* 19:   */   {
/* 20:76 */     super(msg, reason);
/* 21:   */   }
/* 22:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.UnexpectedPropertySetTypeException
 * JD-Core Version:    0.7.0.1
 */